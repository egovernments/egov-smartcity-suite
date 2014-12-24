package org.egov.ptis.service.collection;

import static org.egov.ptis.constants.PropertyTaxConstants.DMD_STATUS_CHEQUE_BOUNCED;
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.CHQ_BOUNCE_PENALTY;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_FIRE_SERVICE_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_GENERAL_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_LIGHTINGTAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_SEWERAGE_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_STR_CHQ_BOUNCE_PENALTY;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.FIRST_REBATETAX_PERC;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.GLCODEMAP_FOR_ARREARTAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.GLCODEMAP_FOR_CURRENTTAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.GLCODES_FOR_CURRENTTAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.GLCODE_FOR_TAXREBATE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.SECOND_REBATETAX_PERC;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.Installment;
import org.egov.demand.dao.DCBDaoFactory;
import org.egov.demand.dao.DemandGenericDao;
import org.egov.demand.dao.DemandGenericHibDao;
import org.egov.demand.dao.EgBillDao;
import org.egov.demand.integration.TaxCollection;
import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.demand.model.EgDemandReasonMaster;
import org.egov.erpcollection.integration.models.BillReceiptInfo;
import org.egov.erpcollection.integration.models.ReceiptAccountInfo;
import org.egov.erpcollection.integration.models.ReceiptInstrumentInfo;
import org.egov.erpcollection.models.ReceiptDetail;
import org.egov.infstr.commons.Module;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.EgovUtils;
import org.egov.ptis.nmc.constants.NMCPTISConstants;
import org.egov.ptis.nmc.service.CollectionApportioner;

/**
 * This class is used to persist Collections .This is used for the integration
 * of Collections and Bills and property tax.
 * 
 */

public class PropertyTaxCollection extends TaxCollection {

	private static final Logger LOGGER = Logger.getLogger(PropertyTaxCollection.class);
	private PersistenceService persistenceService;
	private BigDecimal totalAmount = BigDecimal.ZERO;
	
	@Override
	protected Module module() {
		return GenericDaoFactory.getDAOFactory().getModuleDao().getModuleByName(PTMODULENAME);
	}

	@Override
	public void updateDemandDetails(BillReceiptInfo billRcptInfo) {
		totalAmount = billRcptInfo.getTotalAmount();
		LOGGER.debug("updateDemandDetails : Updating Demand Details Started, billRcptInfo : " + billRcptInfo);
		EgDemand demand = getDemandFromBillNo(Long.valueOf(billRcptInfo.getBillReferenceNum()));
		LOGGER.info("updateDemandDetails : Demand before proceeding : " + demand);

		if (billRcptInfo.getEvent().equals(EVENT_RECEIPT_CREATED)) {
			updateCollForRcptCreate(demand, billRcptInfo);

		} else if (billRcptInfo.getEvent().equals(EVENT_RECEIPT_CANCELLED)) {
			reconcileCollForRcptCancel(demand, billRcptInfo);

		} else if (billRcptInfo.getEvent().equals(EVENT_INSTRUMENT_BOUNCED)) {
			reconcileCollForChequeBounce(demand, billRcptInfo);
		}

		LOGGER.info("updateDemandDetails : Demand after processed : " + demand);
		LOGGER.debug("updateDemandDetails : Updating Demand Details Finished...");
	}

	/**
	 * This method is invoked from Collections end when an event related to
	 * receipt in bill generation occurs.
	 */
	@Override
	public Boolean updateReceiptDetails(Set<BillReceiptInfo> billReceipts) {
		LOGGER.debug("updateReceiptDetails : Updating Receipt Details Started, billReceipts : " + billReceipts);
		Boolean status = false;
		if (billReceipts != null) {
			status = super.updateReceiptDetails(billReceipts);
		}
		LOGGER.debug("updateReceiptDetails : Updating Receipt Details Finished, status : " + status);
		return status;
	}

	/**
	 * Adds the collected amounts in the appropriate buckets.
	 */
	private void updateCollForRcptCreate(EgDemand demand, BillReceiptInfo billRcptInfo) {
		LOGGER.debug("updateCollForRcptCreate : Updating Collection Started For Demand : " + demand
				+ " with BillReceiptInfo - " + billRcptInfo);
		LOGGER.info("updateCollForRcptCreate : Total amount collected : " + totalAmount);
		demand.addCollected(totalAmount);
		if (demand.getMinAmtPayable() != null && demand.getMinAmtPayable().compareTo(BigDecimal.ZERO) > 0) {
			demand.setMinAmtPayable(BigDecimal.ZERO);
		}
		saveCollectionDetails(billRcptInfo.getAccountDetails(), demand, billRcptInfo);
		LOGGER.debug("updateCollForRcptCreate : Updating Demand For Collection finished...");
	}

	/**
	 * Deducts the collected amounts as per the amount of the cancelled receipt.
	 */
	private void reconcileCollForRcptCancel(EgDemand demand, BillReceiptInfo billRcptInfo) {
		LOGGER.debug("reconcileCollForRcptCancel : Updating Collection Started For Demand : " + demand
				+ " with BillReceiptInfo - " + billRcptInfo);
		cancelBill(Long.valueOf(billRcptInfo.getBillReferenceNum()));
		demand.setAmtCollected(demand.getAmtCollected().subtract(billRcptInfo.getTotalAmount()));
		updateDmdDetForRcptCancel(demand, billRcptInfo);
		LOGGER.debug("reconcileCollForRcptCancel : Updating Collection finished For Demand : " + demand);
	}

	/**
	 * Deducts the collected amounts as per the amount of the bounced cheque,
	 * and also imposes a cheque-bounce penalty.
	 */
	private void reconcileCollForChequeBounce(EgDemand demand, BillReceiptInfo billRcptInfo) {
		LOGGER.debug("reconcileCollForChequeBounce : Updating Collection Started For Demand : " + demand
				+ " with BillReceiptInfo - " + billRcptInfo);
		BigDecimal totalCollChqBounced = getTotalChequeAmt(billRcptInfo);
		BigDecimal chqBouncePenalty = getChqBouncePenaltyAmt(totalCollChqBounced);
		cancelBill(Long.valueOf(billRcptInfo.getBillReferenceNum()));
		EgDemandDetails dmdDet = null;
		EgDemandDetails penaltyDmdDet = getDemandDetail(demand, getCurrentInstallment(),
				DEMANDRSN_STR_CHQ_BOUNCE_PENALTY);
		if (penaltyDmdDet == null) {
			dmdDet = insertPenalty(DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY, chqBouncePenalty, getCurrentInstallment());
		} else {
			BigDecimal existDmdDetAmt = penaltyDmdDet.getAmount();
			existDmdDetAmt = (existDmdDetAmt == null || existDmdDetAmt.equals(BigDecimal.ZERO)) ? existDmdDetAmt = BigDecimal.ZERO
					: existDmdDetAmt;
			penaltyDmdDet.setAmount(existDmdDetAmt.add(chqBouncePenalty));
			dmdDet = penaltyDmdDet;
		}

		// setting this min amount into demand to check next payment should be
		// min of this amount with mode of payment cash or DD
		demand.setMinAmtPayable(totalCollChqBounced.add(chqBouncePenalty));
		demand.setAmtCollected(demand.getAmtCollected().subtract(billRcptInfo.getTotalAmount()));
		demand.setBaseDemand(demand.getBaseDemand().add(chqBouncePenalty));
		demand.setStatus(DMD_STATUS_CHEQUE_BOUNCED);
		demand.addEgDemandDetails(dmdDet);
		updateDmdDetForChqBounce(demand, billRcptInfo, totalCollChqBounced);
		LOGGER.debug("reconcileCollForChequeBounce : Updating Collection finished For Demand : " + demand);
	}

	/**
	 * Update the collection to respective account heads paid
	 * 
	 * @param accountDetails
	 * @param demand
	 * @param billRcptInfo
	 */
	private void saveCollectionDetails(Set<ReceiptAccountInfo> accountDetails, EgDemand demand,
			BillReceiptInfo billRcptInfo) {
		LOGGER.debug("Entering method saveCollectionDetails");
		BigDecimal rebateAmt = BigDecimal.ZERO;

		for (ReceiptAccountInfo rcptAccInfo : accountDetails) {
			if (rcptAccInfo.getGlCode().equalsIgnoreCase(GLCODE_FOR_TAXREBATE)) {
				rebateAmt = rcptAccInfo.getDrAmount();
				break;
			}
		}
		
		LOGGER.info("saveCollectionDetails : Start get demandDetailList");
		
		List<EgDemandDetails> demandDetailList = (ArrayList<EgDemandDetails>) persistenceService.findAllBy(
				"FROM EgDemandDetails dmdet WHERE dmdet.egDemand=?", demand);
		
		LOGGER.info("saveCollectionDetails : End get demandDetailList");
		
		for (ReceiptAccountInfo rcptAccInfo : accountDetails) {
			if (rcptAccInfo.getDescription() != null && !rcptAccInfo.getDescription().isEmpty()) {
				String[] desc = rcptAccInfo.getDescription().split("-", 2);
				String reason = desc[0].trim();
				String instDesc = desc[1].trim();
				
				if ((rcptAccInfo.getCrAmount() != null && rcptAccInfo.getCrAmount().compareTo(BigDecimal.ZERO) == 1)) {
					
					EgDemandDetails demandDetail = null;
					for(EgDemandDetails dmdDtls : demandDetailList) {
						EgDemandReason dmdRsn = dmdDtls.getEgDemandReason();
						if(dmdRsn.getEgDemandReasonMaster().getReasonMaster().equalsIgnoreCase(reason) 
								&& dmdRsn.getEgInstallmentMaster().getDescription().equalsIgnoreCase(instDesc)) {
							demandDetail = dmdDtls;
							break;
						}
					}
					
					if (rcptAccInfo.getGlCode().equalsIgnoreCase(
							GLCODEMAP_FOR_CURRENTTAX.get(NMCPTISConstants.GLCODE_FOR_PENALTY))) {
						demandDetail.addCollected(rcptAccInfo.getCrAmount());
					} else {
						if (rcptAccInfo.getGlCode().equalsIgnoreCase(
								GLCODEMAP_FOR_CURRENTTAX.get(DEMANDRSN_CODE_GENERAL_TAX))
								&& getCurrentInstallment().getDescription().equals(instDesc)) {
							if (!rebateAmt.equals(BigDecimal.ZERO)) {
								demandDetail.addRebateAmt(rebateAmt);
							}
						}
						demandDetail.addCollectedWithOnePaisaTolerance(rcptAccInfo.getCrAmount());
					}
					persistCollectedReceipts(demandDetail, billRcptInfo.getReceiptNum(), totalAmount,
							billRcptInfo.getReceiptDate(), demandDetail.getAmtCollected());
					LOGGER.info("Persisted demand and receipt details for tax : " + reason + " installment : "
							+ instDesc + " with receipt No : " + billRcptInfo.getReceiptNum() + " for Rs. "
							+ demandDetail.getAmtCollected());
				}
			}
		}
		LOGGER.debug("Exiting method saveCollectionDetails");
	}

	/**
	 * Reconciles the collection for respective account heads thats been paid
	 * with given cancel receipt
	 * 
	 * @param demand
	 * @param billRcptInfo
	 */
	private void updateDmdDetForRcptCancel(EgDemand demand, BillReceiptInfo billRcptInfo) {
		LOGGER.debug("Entering method updateDmdDetForRcptCancel");
		ReceiptAccountInfo rebateRcptAccInfo = null;
		for (ReceiptAccountInfo rcptAccInfo : billRcptInfo.getAccountDetails()) {
			if (rcptAccInfo.getGlCode().equalsIgnoreCase(GLCODE_FOR_TAXREBATE)) {
				rebateRcptAccInfo = rcptAccInfo;
				break;
				
			}
		}
		for (ReceiptAccountInfo rcptAccInfo : billRcptInfo.getAccountDetails()) {
			if ((rcptAccInfo.getCrAmount() != null && rcptAccInfo.getCrAmount().compareTo(BigDecimal.ZERO) == 1)
					&& !rcptAccInfo.getIsRevenueAccount()) {
				String[] desc = rcptAccInfo.getDescription().split("-", 2);
				String reason = desc[0].trim();
				String installment = desc[1].trim();
				for (EgDemandDetails demandDetail : demand.getEgDemandDetails()) {
					if (reason.equals(demandDetail.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster())
							&& installment.equals(demandDetail.getEgDemandReason().getEgInstallmentMaster()
									.getDescription())) {
						if (rebateRcptAccInfo !=null
								&& demandDetail.getAmtRebate().compareTo(BigDecimal.ZERO) > 0
								&& demandDetail.getEgDemandReason().getEgDemandReasonMaster().getCode()
										.equals(DEMANDRSN_CODE_GENERAL_TAX)) {
							demandDetail.setAmtRebate(demandDetail.getAmtRebate().subtract(rebateRcptAccInfo.getDrAmount()));
						}
						demandDetail
								.setAmtCollected(demandDetail.getAmtCollected().subtract(rcptAccInfo.getCrAmount()));
						LOGGER.info("Deducted Collected amount and receipt details for tax : " + reason
								+ " installment : " + installment + " with receipt No : "
								+ billRcptInfo.getReceiptNum() + " for Rs. " + demandDetail.getAmtCollected());
					}
				}
			}
		}
		updateReceiptStatusWhenCancelled(billRcptInfo.getReceiptNum());
		LOGGER.debug("Exiting method saveCollectionAndDemandDetails");
	}

	/**
	 * Reconciles the collection amounts for respective account heads depends on
	 * the amount of Cheque bounced the order of reconciling is first advances,
	 * penalty, current tax and finally arrears.
	 * 
	 * @param demand
	 * @param billRcptInfo
	 */
	private void updateDmdDetForChqBounce(EgDemand demand, BillReceiptInfo billRcptInfo, BigDecimal chqBounceAmt) {
		BigDecimal carry = chqBounceAmt;
		BigDecimal amtCollected = BigDecimal.ZERO;
		BigDecimal reconcileCollAmt = BigDecimal.ZERO;
		List<Installment> installmentList = persistenceService.findAllBy(
				"from Installment where module.moduleName = ? order by fromDate desc", PTMODULENAME);
		for (Installment installment : installmentList) {
			List<EgDemandDetails> demandDetails = persistenceService.findAllBy(
					"FROM EgDemandDetails dmdet WHERE dmdet.egDemandReason.egInstallmentMaster=? "
							+ "and dmdet.egDemand=? order by dmdet.egDemandReason.id desc", installment, demand);
			for (EgDemandDetails demandDetail : demandDetails) {
				if (carry.equals(BigDecimal.ZERO)) {
					break;
				}
				amtCollected = demandDetail.getAmtCollected();
				if (amtCollected != null && amtCollected.compareTo(BigDecimal.ZERO) > 0) {
					// checking for current General Tax as this account head
					// only
					// applicable for rebate
					if (demandDetail.getEgDemandReason().getGlcodeId().getGlcode()
							.equalsIgnoreCase(GLCODEMAP_FOR_CURRENTTAX.get(DEMANDRSN_CODE_GENERAL_TAX))
							&& demandDetail.getEgDemandReason().getEgInstallmentMaster()
									.equals(getCurrentInstallment())) {
						carry = reconcileRebateDmdDetForCB(demandDetail, carry);
					} else {// for account heads which are not applicable for
						// rebate
						if (carry.compareTo(BigDecimal.ZERO) > 0
								&& carry.subtract(amtCollected).compareTo(BigDecimal.ZERO) > 0) {
							reconcileCollAmt = BigDecimal.ZERO;
							carry = carry.subtract(reconcileCollAmt);
						} else if (carry.compareTo(BigDecimal.ZERO) > 0
								&& carry.subtract(amtCollected).compareTo(BigDecimal.ZERO) <= 0) {
							reconcileCollAmt = amtCollected.subtract(carry);
							carry = BigDecimal.ZERO;
						}
						demandDetail.setAmtCollected(reconcileCollAmt);
					}
				}
			}
		}
		updateReceiptStatusWhenCancelled(billRcptInfo.getReceiptNum());
	}

	@Override
	public void apportionCollection(String billRefNo, BigDecimal amtPaid, List<ReceiptDetail> receiptDetails) {
		boolean isEligibleForCurrentRebate = false;
		boolean isEligibleForAdvanceRebate = false;
		BigDecimal rebate = BigDecimal.ZERO;

		BigDecimal currentDemandAmount = getRebateApplAmount(receiptDetails);

		if (isRebatePeriodActive()) {
			isEligibleForCurrentRebate = true;
			rebate = calcEarlyPayRebate(currentDemandAmount);
		}

		CollectionApportioner apportioner = new CollectionApportioner(isEligibleForCurrentRebate,
				isEligibleForAdvanceRebate, rebate);
		Map<String, BigDecimal> instDemand = getInstDemand(receiptDetails);
		apportioner.apportion(amtPaid, receiptDetails, instDemand);
	}

	private EgDemand getDemandFromBillNo(Long billId) {
		EgDemand egDemand = null;
		if (billId != null) {
			EgBillDao egBillDao = DCBDaoFactory.getDaoFactory().getEgBillDao();
			EgBill egBill = (EgBill) egBillDao.findById(billId, false);
			if (egBill != null) {
				egDemand = egBill.getEgDemand();
			}
		}
		return egDemand;
	}

	private EgDemand cancelBill(Long billId) {
		EgDemand egDemand = null;
		if (billId != null) {
			EgBillDao egBillDao = DCBDaoFactory.getDaoFactory().getEgBillDao();
			EgBill egBill = (EgBill) egBillDao.findById(billId, false);
			egBill.setIs_Cancelled("Y");
		}
		return egDemand;
	}

	/**
	 * Checks if we are within a rebate period.
	 * 
	 * @return
	 */
	public static boolean isRebatePeriodActive() {
		boolean isActive = false;
		Date today = new Date();
		Calendar dateWithRbtDays = Calendar.getInstance();
		int currMonth = dateWithRbtDays.get(Calendar.MONTH);
		if (currMonth <= 2) {
			dateWithRbtDays.set(Calendar.YEAR, dateWithRbtDays.get(Calendar.YEAR) - 1);
		}
		dateWithRbtDays.set(Calendar.DAY_OF_MONTH, 30);
		dateWithRbtDays.set(Calendar.MONTH, Calendar.NOVEMBER);
		dateWithRbtDays.set(Calendar.HOUR_OF_DAY, 23);
		dateWithRbtDays.set(Calendar.MINUTE, 59);
		dateWithRbtDays.set(Calendar.SECOND, 59);
		if (today.before(dateWithRbtDays.getTime())) {
			isActive = true;
		}
		return isActive;
	}

	/**
	 * Calculates Early Payment Rebate for given Tax Amount
	 * 
	 * @param taxAmount
	 *            for which Rebate has to be calculated
	 * @return rebate amount.
	 */
	public BigDecimal calcEarlyPayRebate(BigDecimal taxAmount) {
		BigDecimal rebate = BigDecimal.ZERO;
		Date today = new Date();
		Calendar firstRebateDate = Calendar.getInstance();
		int currMonth = firstRebateDate.get(Calendar.MONTH);
		if (currMonth <= 2) {
			firstRebateDate.set(Calendar.YEAR, firstRebateDate.get(Calendar.YEAR) - 1);
		}
		firstRebateDate.set(Calendar.DAY_OF_MONTH, 31);
		firstRebateDate.set(Calendar.MONTH, Calendar.MAY);
		firstRebateDate.set(Calendar.HOUR_OF_DAY, 23);
		firstRebateDate.set(Calendar.MINUTE, 59);
		firstRebateDate.set(Calendar.SECOND, 59);

		Calendar secondRebateDate = Calendar.getInstance();
		if (currMonth <= 2) {
			secondRebateDate.set(Calendar.YEAR, secondRebateDate.get(Calendar.YEAR) - 1);
		}
		secondRebateDate.set(Calendar.DAY_OF_MONTH, 30);
		secondRebateDate.set(Calendar.MONTH, Calendar.NOVEMBER);
		secondRebateDate.set(Calendar.HOUR_OF_DAY, 23);
		secondRebateDate.set(Calendar.MINUTE, 59);
		secondRebateDate.set(Calendar.SECOND, 59);
		if (today.before(firstRebateDate.getTime()) || today.equals(firstRebateDate.getTime())) {
			rebate = EgovUtils.roundOff((taxAmount.multiply(FIRST_REBATETAX_PERC)).divide(BigDecimal.valueOf(100)));
		} else if (today.before(secondRebateDate.getTime()) || today.equals(secondRebateDate.getTime())) {
			rebate = EgovUtils.roundOff((taxAmount.multiply(SECOND_REBATETAX_PERC)).divide(BigDecimal.valueOf(100)));
		}
		return rebate;
	}

	/**
	 * Gives the tax amount of Account head for which Rebate applicable
	 * 
	 * @param List
	 *            of <code>ReceiptDetail</code>
	 * @return rebate applicable tax amount.
	 */
	public BigDecimal getRebateApplAmount(List<ReceiptDetail> receiptDetails) {
		BigDecimal taxAmount = BigDecimal.ZERO;
		for (ReceiptDetail rd : receiptDetails) {
			if (rd.getAccounthead().getGlcode().equals(GLCODEMAP_FOR_CURRENTTAX.get(DEMANDRSN_CODE_GENERAL_TAX))) {
				//changed from getCramount() to getCramountToBePaid() because before receipt created 
				//Cr Amount is Zero and it will updated as part of receipt creation.
				taxAmount = rd.getCramountToBePaid();
				break;
			}
		}
		return taxAmount;
	}

	public Map<String, BigDecimal> getInstDemand(List<ReceiptDetail> receiptDetails) {
		Map<String, BigDecimal> retMap = new HashMap<String, BigDecimal>();
		String installment = "";
		String[] desc;

		for (ReceiptDetail rd : receiptDetails) {
			String glCode = rd.getAccounthead().getGlcode();
			installment = "";
			desc = rd.getDescription().split("-", 2);
			installment = desc[1].trim();
			if (!glCode.equalsIgnoreCase(GLCODE_FOR_TAXREBATE)
					&& (glCode.equalsIgnoreCase(GLCODEMAP_FOR_ARREARTAX.get(DEMANDRSN_CODE_GENERAL_TAX))
							|| glCode.equalsIgnoreCase(GLCODEMAP_FOR_ARREARTAX.get(DEMANDRSN_CODE_SEWERAGE_TAX))
							|| glCode.equalsIgnoreCase(GLCODEMAP_FOR_ARREARTAX.get(DEMANDRSN_CODE_LIGHTINGTAX))
							|| glCode.equalsIgnoreCase(GLCODEMAP_FOR_ARREARTAX.get(DEMANDRSN_CODE_FIRE_SERVICE_TAX))
							|| glCode.equalsIgnoreCase(GLCODEMAP_FOR_CURRENTTAX.get(DEMANDRSN_CODE_GENERAL_TAX))
							|| glCode.equalsIgnoreCase(GLCODEMAP_FOR_CURRENTTAX.get(DEMANDRSN_CODE_SEWERAGE_TAX))
							|| glCode.equalsIgnoreCase(GLCODEMAP_FOR_CURRENTTAX.get(DEMANDRSN_CODE_LIGHTINGTAX)) || glCode
								.equalsIgnoreCase(GLCODEMAP_FOR_CURRENTTAX.get(DEMANDRSN_CODE_FIRE_SERVICE_TAX)))) {
				if (retMap.get(installment) == null) {
					retMap.put(installment, rd.getCramountToBePaid());
				} else {
					retMap.put(installment, retMap.get(installment).add(rd.getCramountToBePaid()));
				}
			}
			if (GLCODES_FOR_CURRENTTAX.contains(glCode)) {
				if (retMap.get(installment + "CURRENT") == null) {
					retMap.put(installment + "CURRENT", rd.getCramountToBePaid());
				} else {
					retMap.put(installment + "CURRENT",
							retMap.get(installment + "CURRENT").add(rd.getCramountToBePaid()));
				}
			}
		}
		return retMap;
	}

	/**
	 * Method used to calculate the Total Cheque amount from he BillreceiptInfo
	 * object which is received from Collections Module.
	 * 
	 * @param billRcptInfo
	 * 
	 * @return Total Cheque amount
	 * @exception EGOVRuntimeException
	 */

	public BigDecimal getTotalChequeAmt(BillReceiptInfo billRcptInfo) {
		BigDecimal totalCollAmt = BigDecimal.ZERO;
		try {
			if (billRcptInfo != null) {
				for (ReceiptInstrumentInfo rctInst : billRcptInfo.getBouncedInstruments()) {
					if (rctInst.getInstrumentAmount() != null) {
						totalCollAmt = totalCollAmt.add(rctInst.getInstrumentAmount());
					}
				}
			}
		} catch (EGOVRuntimeException e) {
			throw new EGOVRuntimeException("Exception in calculate Total Collected Amt" + e);
		}

		return totalCollAmt;
	}

	/**
	 * Gives the Cheque bounce penalty charges for given cheque amount
	 * 
	 * @param totalChqAmount
	 * @return {@link BigDecimal}
	 */
	public BigDecimal getChqBouncePenaltyAmt(BigDecimal totalChqAmount) {
		return CHQ_BOUNCE_PENALTY;
	}

	/**
	 * Method used to insert penalty in EgDemandDetail table. Penalty Amount
	 * will be calculated depending upon the cheque Amount.
	 * 
	 * @see createDemandDetails() -- EgDemand Details are created
	 * @see getPenaltyAmount() --Penalty Amount is calculated
	 * @param chqBouncePenalty
	 * 
	 * @return New EgDemandDetails Object
	 */
	public EgDemandDetails insertPenalty(String demandReason, BigDecimal penaltyAmount, Installment inst) {
		EgDemandDetails demandDetail = null;
		if (penaltyAmount != null && penaltyAmount.compareTo(BigDecimal.ZERO) > 0) {
			DemandGenericDao demandGenericDao = new DemandGenericHibDao();

			EgDemandReasonMaster egDemandReasonMaster = (EgDemandReasonMaster) demandGenericDao
					.getDemandReasonMasterByCode(demandReason, module());
			if (egDemandReasonMaster == null) {
				throw new EGOVRuntimeException(" Penalty Demand reason Master is null in method  insertPenalty");
			}
			EgDemandReason egDemandReason = (EgDemandReason) demandGenericDao
					.getDmdReasonByDmdReasonMsterInstallAndMod(egDemandReasonMaster, inst, module());
			if (egDemandReason == null) {
				throw new EGOVRuntimeException(" Penalty Demand reason is null in method  insertPenalty ");
			}
			demandDetail = createDemandDetails(egDemandReason, BigDecimal.ZERO, penaltyAmount);
		}
		return demandDetail;
	}

	/**
	 * Method used to create new EgDemandDetail Object depending upon the
	 * EgDemandReason , Collected amount and Demand amount(which are
	 * compulsory),Other wise returns Empty EgDemandDetails Object.
	 * 
	 * @param egDemandReason
	 * @param amtCollected
	 * @param dmdAmount
	 * 
	 * @return New EgDemandDetails Object
	 * 
	 */

	public EgDemandDetails createDemandDetails(EgDemandReason egDemandReason, BigDecimal amtCollected,
			BigDecimal dmdAmount) {
		return EgDemandDetails.fromReasonAndAmounts(dmdAmount, egDemandReason, amtCollected);
	}

	/**
	 * Reconciles amounts collected for account heads where rebates is
	 * applicable in case of Cheque bounce
	 * <p>
	 * Note :Implemented only General Tax, advance and needs to implement
	 * whenever the requirement comes.
	 * </p>
	 * 
	 * @param dmdDetail
	 * @param carry
	 * @return {@link BigDecimal}
	 */
	public BigDecimal reconcileRebateDmdDetForCB(EgDemandDetails dmdDetail, BigDecimal carry) {
		BigDecimal carryDummy = carry;
		BigDecimal reconcileCollAmt = BigDecimal.ZERO;
		BigDecimal reconcileRbtAmt = BigDecimal.ZERO;
		BigDecimal totalCollectedAmt = dmdDetail.getAmtCollected();
		BigDecimal collRbtAmt = dmdDetail.getAmtRebate();
		reconcileCollAmt = (reconcileCollAmt == null) ? BigDecimal.ZERO : reconcileCollAmt;
		collRbtAmt = (collRbtAmt == null) ? BigDecimal.ZERO : collRbtAmt;
		if (carryDummy.compareTo(BigDecimal.ZERO) > 0
				&& carryDummy.subtract(totalCollectedAmt).compareTo(BigDecimal.ZERO) > 0) {
			reconcileCollAmt = totalCollectedAmt;
			reconcileRbtAmt = collRbtAmt;
			carryDummy = carryDummy.subtract(reconcileCollAmt.subtract(reconcileRbtAmt));
		} else if (carryDummy.compareTo(BigDecimal.ZERO) > 0
				&& carryDummy.subtract(totalCollectedAmt).compareTo(BigDecimal.ZERO) <= 0) {
			reconcileCollAmt = carryDummy;
			reconcileRbtAmt = calcEarlyPayRebate(reconcileCollAmt);
			reconcileCollAmt = carryDummy.add(reconcileRbtAmt);
			carryDummy = BigDecimal.ZERO;
		}
		/*
		 * if
		 * (dmdDetail.getEgDemandReason().getEgDemandReasonMaster().getCode().
		 * equalsIgnoreCase(
		 * PropertyTaxConstants.ADVANCEDEMANDREASONMASTERCODE)) {
		 * dmdDetail.setAmtCollected
		 * (totalCollectedAmt.subtract(reconcileCollAmt));
		 * dmdDetail.setAmtRebate(collRbtAmt.subtract(reconcileRbtAmt));
		 * dmdDetsList.remove(BigDecimal.valueOf(dmdDetail.getId())); }
		 */
		if (dmdDetail.getEgDemandReason().getEgDemandReasonMaster().getCode()
				.equalsIgnoreCase(GLCODEMAP_FOR_CURRENTTAX.get(DEMANDRSN_CODE_GENERAL_TAX))) {
			dmdDetail.setAmtCollected(totalCollectedAmt.subtract(reconcileCollAmt));
			dmdDetail.setAmtRebate(collRbtAmt.subtract(reconcileRbtAmt));
		}
		/*
		 * if
		 * (dmdDetail.getEgDemandReason().getEgDemandReasonMaster().getCode().
		 * equalsIgnoreCase( PropertyTaxConstants.LPPAY_PENALTY_DMDRSNCODE)) {
		 * dmdDetail.setAmtCollected(reconcileCollAmt); }
		 */
		return carry;
	}

	public PersistenceService getPersistenceService() {
		return persistenceService;
	}

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

}
