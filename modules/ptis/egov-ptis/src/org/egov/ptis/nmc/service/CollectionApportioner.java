package org.egov.ptis.nmc.service;

import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_FIRE_SERVICE_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_GENERAL_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_LIGHTINGTAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_PENALTY_FINES;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_SEWERAGE_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.GLCODEMAP_FOR_ARREARTAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.GLCODEMAP_FOR_CURRENTTAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.GLCODES_FOR_CURRENTTAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.GLCODE_FOR_TAXREBATE;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.erpcollection.models.ReceiptDetail;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.utils.MoneyUtils;

public class CollectionApportioner {
	private static final Logger LOGGER = Logger.getLogger(CollectionApportioner.class);
	private boolean isEligibleForCurrentRebate;
	private boolean isEligibleForAdvanceRebate;
	private Boolean isEligibleForCurrRebtLocal = Boolean.FALSE;
	private BigDecimal rebate;

	public CollectionApportioner(boolean isEligibleForCurrentRebate, boolean isEligibleForAdvanceRebate,
			BigDecimal rebate) {
		this.isEligibleForCurrentRebate = isEligibleForCurrentRebate;
		this.isEligibleForAdvanceRebate = isEligibleForAdvanceRebate;
		this.rebate = rebate;
	}

	public void apportion(BigDecimal amtPaid, List<ReceiptDetail> receiptDetails, Map<String, BigDecimal> instDmdMap) {
		LOGGER.info("receiptDetails before apportioning amount " + amtPaid + ": " + receiptDetails);

		Amount balance = new Amount(amtPaid);
		BigDecimal crAmountToBePaid = null;
		boolean isRebateToBeApplied = false;
		boolean isPartialPayment = false;
		String glCode = null;
		BigDecimal partialPayment = BigDecimal.ZERO;
		BigDecimal instDmd = BigDecimal.ZERO;
		BigDecimal generalTax = BigDecimal.ZERO;
		BigDecimal sewerageTax = BigDecimal.ZERO;
		BigDecimal lightTax = BigDecimal.ZERO;
		BigDecimal fireServTax = BigDecimal.ZERO;

		for (ReceiptDetail rd : receiptDetails) {
			crAmountToBePaid = rd.getCramountToBePaid();
			glCode = rd.getAccounthead().getGlcode();
			String[] desc = rd.getDescription().split("-", 2);
			String reason = desc[0].trim();
			String installment = desc[1].trim();
			if (balance.isZero() && !isRebateToBeApplied) {
				// nothing left to apportion
				rd.zeroDrAndCrAmounts();
				continue;
			}
			if (reason.equalsIgnoreCase(DEMANDRSN_CODE_PENALTY_FINES)
					|| reason.equalsIgnoreCase(DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY)) {
				if (crAmountToBePaid.compareTo(balance.amount) > 1) {
					rd.setCramount(balance.amount);
					balance.isZero();
				} else {
					rd.setCramount(crAmountToBePaid);
					balance.minus(crAmountToBePaid);
				}
				rd.setCramount(balance.amount);
			}

			if (reason.equalsIgnoreCase("GENERALTAX")) {
				if (isEligibleForCurrentRebate) {
					isEligibleForCurrRebtLocal = Boolean.TRUE;
				} else {
					isEligibleForCurrRebtLocal = Boolean.FALSE;
				}
				// as General tax order no is one in individual installment,
				// checking for partial or full payment
				instDmd = instDmdMap.get(installment);
				if (!balance.isZero() && balance.isLessThanOrEqualTo(instDmd)) {
					isPartialPayment = true;
					// if its a partial payment setting balance amount to
					// partialPayment
					// to serve to first four reasons with respective %ages
					if (partialPayment.equals(BigDecimal.ZERO) && amtPaid.equals(balance.amount)) {
						partialPayment = amtPaid;
					} else {
						partialPayment = balance.amount;
					}
					long partialPayInPaisa = partialPayment.multiply(new BigDecimal(100)).longValue();
					long[] weights = new long[] { 27, 12, 1, 1 };
					if (partialPayment != null) {
						generalTax = BigDecimal.ZERO;
						sewerageTax = BigDecimal.ZERO;
						lightTax = BigDecimal.ZERO;
						fireServTax = BigDecimal.ZERO;
						long[] amtAllocation = MoneyUtils.allocate(partialPayInPaisa, weights);
						generalTax = new BigDecimal(amtAllocation[0]).divide(new BigDecimal(100));
						sewerageTax = new BigDecimal(amtAllocation[1]).divide(new BigDecimal(100));
						lightTax = new BigDecimal(amtAllocation[2]).divide(new BigDecimal(100));
						fireServTax = new BigDecimal(amtAllocation[3]).divide(new BigDecimal(100));
					}
				} else {
					isPartialPayment = false;
				}
			} else {
				isEligibleForCurrRebtLocal = Boolean.FALSE;
			}
			// in case of partial payment serving amount based on %ages
			if (isPartialPayment) {
				//Added by Ramki, in case of partial we should not give rebate so, below code is added
				if (isCurrent(glCode) && reason.equalsIgnoreCase("GENERALTAX") && isEligibleForCurrentRebate) {
					//in case of partial payment for current installment rebate is not allowed
					isEligibleForCurrRebtLocal = false;
					isRebateToBeApplied = false;
				}
				if (isRebate(glCode) && !isRebateToBeApplied) {
					// the installment was not fully paid, therefore this rebate amount must be zeroed.
					rd.setDramount(BigDecimal.ZERO);
				}
				if (glCode.equalsIgnoreCase(GLCODEMAP_FOR_ARREARTAX.get(DEMANDRSN_CODE_GENERAL_TAX))
						|| glCode.equalsIgnoreCase(GLCODEMAP_FOR_CURRENTTAX.get(DEMANDRSN_CODE_GENERAL_TAX))) {
					rd.setCramount(generalTax);
					balance = balance.minus(generalTax);
				}
				if (glCode.equalsIgnoreCase(GLCODEMAP_FOR_ARREARTAX.get(DEMANDRSN_CODE_SEWERAGE_TAX))
						|| glCode.equalsIgnoreCase(GLCODEMAP_FOR_CURRENTTAX.get(DEMANDRSN_CODE_SEWERAGE_TAX))) {
					rd.setCramount(sewerageTax);
					balance = balance.minus(sewerageTax);
				}
				if (glCode.equalsIgnoreCase(GLCODEMAP_FOR_ARREARTAX.get(DEMANDRSN_CODE_LIGHTINGTAX))
						|| glCode.equalsIgnoreCase(GLCODEMAP_FOR_CURRENTTAX.get(DEMANDRSN_CODE_LIGHTINGTAX))) {
					rd.setCramount(lightTax);
					balance = balance.minus(lightTax);
				}
				if (glCode.equalsIgnoreCase(GLCODEMAP_FOR_ARREARTAX.get(DEMANDRSN_CODE_FIRE_SERVICE_TAX))
						|| glCode.equalsIgnoreCase(GLCODEMAP_FOR_CURRENTTAX.get(DEMANDRSN_CODE_FIRE_SERVICE_TAX))) {
					rd.setCramount(fireServTax);
					balance = balance.minus(fireServTax);
				}
			} else {
				if (isCurrent(glCode) && reason.equalsIgnoreCase("GENERALTAX") && isEligibleForCurrentRebate) {
					BigDecimal currentTax = instDmdMap.get(installment + "CURRENT");
					if (balance.plus(rebate).isGreaterThanOrEqualTo(currentTax)) {
						// exact payment for current installment, so rebate
						isEligibleForCurrRebtLocal = true;
						isRebateToBeApplied = true;
					} else {
						isEligibleForCurrRebtLocal = false;
						isRebateToBeApplied = false;
					}
				}
				if (isArrear(glCode) || (isCurrent(glCode) && !isEligibleForCurrRebtLocal)) {
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
				if (isCurrent(glCode) && isEligibleForCurrRebtLocal) {
					// exact payment
					rd.setCramount(crAmountToBePaid);
					if (glCode.equalsIgnoreCase(GLCODEMAP_FOR_CURRENTTAX.get(DEMANDRSN_CODE_GENERAL_TAX))) {
						balance = balance.plus(rebate).minus(crAmountToBePaid);
					} else {
						balance = balance.minus(crAmountToBePaid);
					}
					continue;
				}

				if (isRebate(glCode)) {
					if (isRebateToBeApplied) {
						// the installment (previous row) was fully paid,
						// therefore
						// this rebate applies
						rd.setDramount(rebate);
						// reset the flag
						isRebateToBeApplied = false;
					} else {
						// the installment (previous row) was not fully paid,
						// therefore this rebate
						// amount must be zeroed.
						rd.setDramount(BigDecimal.ZERO);
					}
					continue;
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

	/*
	 * private boolean isAdvance(String glCode) { return
	 * glCode.equals(GLCODE_FOR_ADVANCE); }
	 */

	private boolean isRebate(String glCode) {
		return glCode.equals(GLCODE_FOR_TAXREBATE);
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
