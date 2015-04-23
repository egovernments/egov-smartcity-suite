package org.egov.ptis.nmc.service;

import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_GENERAL_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_PENALTY_FINES;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_STR_ADVANCE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_STR_GENERAL_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.GLCODEMAP_FOR_ARREARTAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.GLCODEMAP_FOR_CURRENTTAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.GLCODES_FOR_CURRENTTAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.GLCODE_FOR_TAXREBATE;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.erpcollection.models.ReceiptDetail;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.utils.EgovUtils;
import org.egov.ptis.nmc.constants.NMCPTISConstants;

public class CollectionApportioner {

	public static final String STRING_FULLTAX = "FULLTAX";
	public static final String STRING_ADVANCE = "ADVANCE";
	private static final Logger LOGGER = Logger.getLogger(CollectionApportioner.class);
	private boolean isEligibleForCurrentRebate;
	private boolean isEligibleForAdvanceRebate;
	private BigDecimal rebate;

	public CollectionApportioner(boolean isEligibleForCurrentRebate, boolean isEligibleForAdvanceRebate,
			BigDecimal rebate) {
		this.isEligibleForCurrentRebate = isEligibleForCurrentRebate;
		this.isEligibleForAdvanceRebate = isEligibleForAdvanceRebate;
		// this.rebate = rebate;
	}

	public void apportion(BigDecimal amtPaid, List<ReceiptDetail> receiptDetails, Map<String, BigDecimal> instDmdMap) {
		LOGGER.info("receiptDetails before apportioning amount " + amtPaid + ": " + receiptDetails);

		Amount balance = new Amount(amtPaid);
		BigDecimal crAmountToBePaid = null;
		boolean isRebateToBeApplied = false;
		boolean isPartialPayment = false;
		String glCode = null;
		String currInst = "";
		BigDecimal instDmd = BigDecimal.ZERO;
		BigDecimal halfRebate = BigDecimal.ZERO;
		Map<String, BigDecimal> rebateMap = new HashMap<String, BigDecimal>();
		
		for (ReceiptDetail rd : receiptDetails) {
			
			glCode = rd.getAccounthead().getGlcode();
			
			if (glCode.endsWith(NMCPTISConstants.GLCODE_FOR_TAXREBATE)
					|| glCode.equalsIgnoreCase(NMCPTISConstants.GLCODE_FOR_ADVANCE_REBATE)) {
				rebateMap.put(rd.getDescription().split("-", 2)[1].trim(), rd.getDramount());
			}
		}

		LOGGER.info("apportion - rebateMap=" + rebateMap);

		for (ReceiptDetail rd : receiptDetails) {

			if (balance.isZero() && !isRebateToBeApplied) {
				// nothing left to apportion
				rd.zeroDrAndCrAmounts();
				continue;
			}

			crAmountToBePaid = rd.getCramountToBePaid();
			glCode = rd.getAccounthead().getGlcode();
			String[] desc = rd.getDescription().split("-", 2);
			String reason = desc[0].trim();
			String installment = desc[1].trim();
			// getting current installment
			if (currInst.isEmpty()) {
				currInst = GLCODES_FOR_CURRENTTAX.contains(glCode) ? installment : "";
			}
			
			rebate = rebateMap.get(installment) == null ? BigDecimal.ZERO : rebateMap.get(installment);

			if (reason.equalsIgnoreCase(DEMANDRSN_CODE_PENALTY_FINES)
					|| reason.equalsIgnoreCase(DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY)) {
				if (crAmountToBePaid.compareTo(balance.amount) >= 1) {
					rd.setCramount(balance.amount);
					balance = Amount.ZERO;
				} else {
					rd.setCramount(crAmountToBePaid);
					balance = balance.minus(crAmountToBePaid);
				}
				continue;
			} else if (reason.equalsIgnoreCase(DEMANDRSN_STR_GENERAL_TAX)) {

				/*
				 * as General tax order no is one in individual installment,
				 * checking for partial or full payment
				 */
				instDmd = instDmdMap.get(installment + STRING_FULLTAX);

				if (!balance.isZero()) {
					isPartialPayment = balance.plus(rebate).isLessThan(instDmd) ? true : false;
					/*
					 * calculating half reabte if current installment paid
					 * amount equal or greater than half tax that is 6 months
					 * tax
					 */
					if (isPartialPayment && rebate.compareTo(BigDecimal.ZERO) == 1 && currInst.equals(installment)) {
						halfRebate = EgovUtils.roundOff(rebate.divide(new BigDecimal(2)));
						if (balance.plus(halfRebate).isGreaterThanOrEqualTo((instDmd.divide(new BigDecimal(2))))) {
							rebate = halfRebate;
						}
					}
				}
			} else if ( reason.equalsIgnoreCase(DEMANDRSN_STR_ADVANCE) ) {
				instDmd = instDmdMap.get(installment + STRING_ADVANCE);
				isPartialPayment = balance.plus(rebate).isLessThan(instDmd) ? true : false;
				isRebateToBeApplied = false;
				/*if (isPartialPayment) {
					isRebateToBeApplied = false;
				}*/
			}

			if (isPartialPayment) {
				
				isRebateToBeApplied = false;
				
				if (isRebate(glCode)) {
					/*
					 * the installment was not fully paid, therefore this rebate
					 * amount must be zeroed.
					 */
					rd.setDramount(BigDecimal.ZERO);
				} else {
					//giving half rebate for current installment if citizen is paying first 6 months tax
					if (isRebateToBeApplied && reason.equalsIgnoreCase(DEMANDRSN_STR_GENERAL_TAX)
							&& currInst.equals(installment) && rebate.compareTo(BigDecimal.ZERO) == 1) {
						if (balance.plus(rebate).isLessThanOrEqualTo(crAmountToBePaid)) {
							rd.setCramount(balance.plus(rebate).amount);
							balance = Amount.ZERO;
						} else {
							rd.setCramount(crAmountToBePaid);
							balance = balance.plus(rebate).minus(crAmountToBePaid);
						}
					} else {
						if (balance.isLessThanOrEqualTo(crAmountToBePaid)) {
							rd.setCramount(balance.amount);
							balance = Amount.ZERO;
						} else {
							rd.setCramount(crAmountToBePaid);
							balance = balance.minus(crAmountToBePaid);
						}
					}
				}
			} else {
				if (reason.equalsIgnoreCase(DEMANDRSN_STR_GENERAL_TAX)) {
					BigDecimal instTax = instDmdMap.get(installment + STRING_FULLTAX);
					if (balance.plus(rebate).isGreaterThanOrEqualTo(instTax)) {
						// exact payment for current installment, so rebate
						isRebateToBeApplied = true;
					} else {
						isRebateToBeApplied = false;
					}
				} else if ( reason.equalsIgnoreCase(DEMANDRSN_STR_ADVANCE) ) {
					//BigDecimal instTax = instDmdMap.get(installment + STRING_ADVANCE);
					isRebateToBeApplied = false;
					//isRebateToBeApplied = balance.plus(rebate).isGreaterThanOrEqualTo(instTax) ? true : false;
				}
				
				if (!isRebateToBeApplied && !isRebate(glCode)) {
					if (balance.isLessThanOrEqualTo(crAmountToBePaid)) {
						// partial or exact payment
						rd.setCramount(balance.amount);
						balance = Amount.ZERO;
					} else { // excess payment
						rd.setCramount(crAmountToBePaid);
						balance = balance.minus(crAmountToBePaid);
					}
				
					continue;
				}
				if (isRebateToBeApplied && !isRebate(glCode)) {
					// exact payment
					rd.setCramount(crAmountToBePaid);
					if (glCode.equalsIgnoreCase(GLCODEMAP_FOR_CURRENTTAX.get(DEMANDRSN_CODE_GENERAL_TAX))
							|| glCode.equalsIgnoreCase(GLCODEMAP_FOR_ARREARTAX.get(DEMANDRSN_CODE_GENERAL_TAX))) {
						balance = balance.plus(rebate).minus(crAmountToBePaid);
					} else {
						balance = balance.minus(crAmountToBePaid);
					}
					
				
					continue;
				}

				if (isRebate(glCode)) {
					if (isRebateToBeApplied) {
						/*
						 * the installment (previous row) was fully paid,
						 * therefore this rebate applies
						 */
						rd.setDramount(rebate);
						// reset the flag
						isRebateToBeApplied = false;
					} else {
						/*
						 * the installment (previous row) was not fully paid,
						 * therefore this rebate amount must be zeroed.
						 */
						rd.setDramount(BigDecimal.ZERO);
					}
					
					continue;
				}
			}
		}
		//setting half rebate for current installment if available
		if (!currInst.isEmpty() && halfRebate.compareTo(BigDecimal.ZERO) == 1) {
			for (ReceiptDetail rd : receiptDetails) {
				if (rd.getAccounthead().getGlcode().endsWith(NMCPTISConstants.GLCODE_FOR_TAXREBATE)) {
					if (rd.getDescription().split("-", 2)[1].trim().equals(currInst)) {
						rd.setDramount(halfRebate);
						break;
					}
				}
			}
		}
		if (balance.isGreaterThanZero()) {
			LOGGER.error("Apportioning failed: excess payment!");
			throw new ValidationException(Arrays.asList(new ValidationError(
					"Paid Amount is greater than Total Amount to be paid",
					"Paid Amount is greater than Total Amount to be paid")));
		}

		LOGGER.info("receiptDetails after apportioning: " + receiptDetails);
	}

	private boolean isArrear(String glCode) {
		return !GLCODES_FOR_CURRENTTAX.contains(glCode) && !glCode.equals(GLCODE_FOR_TAXREBATE);
	}

	private boolean isCurrent(String glCode) {
		return GLCODES_FOR_CURRENTTAX.contains(glCode);
	}

	
	private boolean isAdvance(String glCode) {
		return glCode.equals(NMCPTISConstants.GLCODE_FOR_ADVANCE);
	}
	 

	private boolean isRebate(String glCode) {
		return glCode.equals(GLCODE_FOR_TAXREBATE) || glCode.equalsIgnoreCase(NMCPTISConstants.GLCODE_FOR_ADVANCE_REBATE);
	}

	private static class Amount {
		private BigDecimal amount;
		private static Amount ZERO = new Amount(BigDecimal.ZERO);

		private Amount(BigDecimal amount) {
			this.amount = amount;
		}

		private boolean isZero() {
			return amount.compareTo(BigDecimal.ZERO) == 0;
		}

		private boolean isGreaterThan(BigDecimal bd) {
			return amount.compareTo(bd) > 0;
		}

		private boolean isGreaterThanZero() {
			return isGreaterThan(BigDecimal.ZERO);
		}

		private boolean isGreaterThanOrEqualTo(BigDecimal bd) {
			return amount.compareTo(bd) >= 0;
		}

		private boolean isLessThanOrEqualTo(BigDecimal bd) {
			return amount.compareTo(bd) <= 0;
		}

		private boolean isLessThan(BigDecimal bd) {
			return amount.compareTo(bd) < 0;
		}

		private boolean isGreaterThanOrEqualToZero() {
			return isGreaterThanOrEqualTo(BigDecimal.ZERO);
		}

		private Amount minus(BigDecimal bd) {
			return new Amount(amount.subtract(bd));
		}

		private Amount plus(BigDecimal bd) {
			return new Amount(amount.add(bd));
		}

		private Amount multiply(BigDecimal bd) {
			return new Amount(amount.multiply(bd));
		}

		private Amount divide(BigDecimal bd) {
			return new Amount(amount.divide(bd));
		}

	}

	void setEligibleForCurrentRebate(boolean isEligibleForCurrentRebate) {
		this.isEligibleForCurrentRebate = isEligibleForCurrentRebate;
	}

	void setEligibleForAdvanceRebate(boolean isEligibleForAdvanceRebate) {
		this.isEligibleForAdvanceRebate = isEligibleForAdvanceRebate;
	}
}
