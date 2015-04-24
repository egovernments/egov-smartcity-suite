package org.egov.ptis.service.collection;

import static org.egov.ptis.constants.PropertyTaxConstants.DMD_STATUS_CHEQUE_BOUNCED;
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.CHQ_BOUNCE_PENALTY;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_ADVANCE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_FIRE_SERVICE_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_GENERAL_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_MUNICIPAL_EDUCATIONAL_CESS;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_SEWERAGE_BENEFIT_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_SEWERAGE_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_STREET_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_WATER_BENEFIT_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_STR_ADVANCE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_STR_CHQ_BOUNCE_PENALTY;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.FIRST_REBATETAX_PERC;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.GLCODEMAP_FOR_ARREARTAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.GLCODEMAP_FOR_CURRENTTAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.GLCODES_FOR_ARREARTAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.GLCODES_FOR_CURRENTTAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.GLCODE_FOR_ADVANCE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.GLCODE_FOR_TAXREBATE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.SECOND_REBATETAX_PERC;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.collection.entity.ReceiptDetail;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.collection.integration.models.BillReceiptInfoImpl;
import org.egov.collection.integration.models.ReceiptAccountInfo;
import org.egov.collection.integration.models.ReceiptInstrumentInfo;
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
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.commons.Module;
import org.egov.infstr.commons.dao.ModuleDao;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.EgovUtils;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.nmc.constants.NMCPTISConstants;
import org.egov.ptis.nmc.service.CollectionApportioner;
import org.egov.ptis.nmc.util.PropertyTaxUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * This class is used to persist Collections .This is used for the integration
 * of Collections and Bills and property tax.
 *
 */

public class PropertyTaxCollection extends TaxCollection {

	private static final Logger LOGGER = Logger.getLogger(PropertyTaxCollection.class);
	private PersistenceService persistenceService;
	private BigDecimal totalAmount = BigDecimal.ZERO;
	@Autowired
	@Qualifier(value = "moduleDAO")
	private ModuleDao moduleDao;

	@Override
	protected Module module() {
		return moduleDao.getModuleByName(PTMODULENAME);
	}

	@Override
	public void updateDemandDetails(BillReceiptInfo billRcptInfo) {
		totalAmount = billRcptInfo.getTotalAmount();
		LOGGER.debug("updateDemandDetails : Updating Demand Details Started, billRcptInfo : " + billRcptInfo);
		EgDemand demand = getCurrentDemand(Long.valueOf(billRcptInfo.getBillReferenceNum()));
		String indexNo = ((BillReceiptInfoImpl) billRcptInfo).getReceiptMisc().getReceiptHeader().getConsumerCode();
		LOGGER.info("updateDemandDetails : Demand before proceeding : " + demand);
		LOGGER.info("updateDemandDetails : collection back update started for property : " + indexNo
				+ " and receipt event is " + billRcptInfo.getEvent() + ". Total Receipt amount is." + totalAmount
				+ " with receipt no." + billRcptInfo.getReceiptNum());
		if (billRcptInfo.getEvent().equals(EVENT_RECEIPT_CREATED)) {
			updateCollForRcptCreate(demand, billRcptInfo);

		} else if (billRcptInfo.getEvent().equals(EVENT_RECEIPT_CANCELLED)) {
			updateCollForRcptCancel(demand, billRcptInfo);

		} else if (billRcptInfo.getEvent().equals(EVENT_INSTRUMENT_BOUNCED)) {
			updateCollForChequeBounce(demand, billRcptInfo);
		}

		LOGGER.info("updateDemandDetails : Demand after processed : " + demand);
		LOGGER.debug("updateDemandDetails : Updating Demand Details Finished...");
	}

	/**
	 * This method is invoked from Collections end when an event related to
	 * receipt in bill generation occurs.
	 */
	@Override
	public void updateReceiptDetails(Set<BillReceiptInfo> billReceipts) {
		LOGGER.debug("updateReceiptDetails : Updating Receipt Details Started, billReceipts : " + billReceipts);
		Boolean status = false;
		if (billReceipts != null) {
			super.updateReceiptDetails(billReceipts);
		}
		LOGGER.debug("updateReceiptDetails : Updating Receipt Details Finished, status : " + status);
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
		updateDmdDetForRcptCreate(billRcptInfo.getAccountDetails(), demand, billRcptInfo);
		LOGGER.debug("updateCollForRcptCreate : Updating Demand For Collection finished...");
	}

	/**
	 * Deducts the collected amounts as per the amount of the cancelled receipt.
	 */
	private void updateCollForRcptCancel(EgDemand demand, BillReceiptInfo billRcptInfo) {
		LOGGER.debug("reconcileCollForRcptCancel : Updating Collection Started For Demand : " + demand
				+ " with BillReceiptInfo - " + billRcptInfo);
		cancelBill(Long.valueOf(billRcptInfo.getBillReferenceNum()));

		if (demand.getAmtCollected() != null) {
			demand.setAmtCollected(demand.getAmtCollected().subtract(billRcptInfo.getTotalAmount()));
		}

		updateDmdDetForRcptCancel(demand, billRcptInfo);
		LOGGER.debug("reconcileCollForRcptCancel : Updating Collection finished For Demand : " + demand);
	}

	/**
	 * Deducts the collected amounts as per the amount of the bounced cheque,
	 * and also imposes a cheque-bounce penalty.
	 */
	private void updateCollForChequeBounce(EgDemand demand, BillReceiptInfo billRcptInfo) {
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
		updateDmdDetForRcptCancel(demand, billRcptInfo);
		LOGGER.debug("reconcileCollForChequeBounce : Updating Collection finished For Demand : " + demand);
	}

	/**
	 * Update the collection to respective account heads paid
	 *
	 * @param accountDetails
	 * @param demand
	 * @param billRcptInfo
	 */
	@SuppressWarnings("unchecked")
	private void updateDmdDetForRcptCreate(Set<ReceiptAccountInfo> accountDetails, EgDemand demand,
			BillReceiptInfo billRcptInfo) {
		LOGGER.debug("Entering method saveCollectionDetails");

		BigDecimal rebateAmt = BigDecimal.ZERO;
		Map<String, BigDecimal> rebateMap = new HashMap<String, BigDecimal>();

		for (ReceiptAccountInfo rcptAccInfo : accountDetails) {
			if (rcptAccInfo.getGlCode().equalsIgnoreCase(GLCODE_FOR_TAXREBATE)
					|| rcptAccInfo.getGlCode().equalsIgnoreCase(NMCPTISConstants.GLCODE_FOR_ADVANCE_REBATE)) {
				rebateMap.put(rcptAccInfo.getDescription().split("-", 2)[1].trim(), rcptAccInfo.getDrAmount());
			}
		}

		LOGGER.info("saveCollectionDetails : Start get demandDetailList");

		List<EgDemandDetails> demandDetailList = persistenceService.findAllBy(
				"select dmdet FROM EgDemandDetails dmdet " + "left join fetch dmdet.egDemandReason dmdRsn "
						+ "left join fetch dmdRsn.egDemandReasonMaster dmdRsnMstr "
						+ "left join fetch dmdRsn.egInstallmentMaster installment WHERE dmdet.egDemand = ?", demand);

		LOGGER.info("saveCollectionDetails : End get demandDetailList");

		Map<String, Map<String, EgDemandDetails>> installmentWiseDemandDetailsByReason = new HashMap<String, Map<String, EgDemandDetails>>();
		Map<String, EgDemandDetails> demandDetailByReason = new HashMap<String, EgDemandDetails>();

		EgDemandReason dmdRsn = null;
		String installmentDesc = null;

		for (EgDemandDetails dmdDtls : demandDetailList) {

			if (dmdDtls.getAmount().compareTo(BigDecimal.ZERO) > 0
					|| dmdDtls.getEgDemandReason().getEgDemandReasonMaster().getCode()
							.equalsIgnoreCase(DEMANDRSN_CODE_ADVANCE)) {

				dmdRsn = dmdDtls.getEgDemandReason();
				installmentDesc = dmdRsn.getEgInstallmentMaster().getDescription();
				demandDetailByReason = new HashMap<String, EgDemandDetails>();

				if (installmentWiseDemandDetailsByReason.get(installmentDesc) == null) {
					demandDetailByReason.put(dmdRsn.getEgDemandReasonMaster().getReasonMaster(), dmdDtls);
					installmentWiseDemandDetailsByReason.put(installmentDesc, demandDetailByReason);
				} else {
					installmentWiseDemandDetailsByReason.get(installmentDesc).put(
							dmdRsn.getEgDemandReasonMaster().getReasonMaster(), dmdDtls);
				}
			} else {
				LOGGER.info("saveCollectionDetails - demand detail amount is zero " + dmdDtls);
			}
		}

		LOGGER.info("saveCollectionDetails - installment demandDetails size = "
				+ installmentWiseDemandDetailsByReason.size());

		Installment currentInstallment = PropertyTaxUtil.getCurrentInstallment();

		for (ReceiptAccountInfo rcptAccInfo : accountDetails) {
			if (rcptAccInfo.getDescription() != null && !rcptAccInfo.getDescription().isEmpty()) {
				if ((rcptAccInfo.getCrAmount() != null && rcptAccInfo.getCrAmount().compareTo(BigDecimal.ZERO) == 1)) {
					String[] desc = rcptAccInfo.getDescription().split("-", 2);
					String reason = desc[0].trim();
					String instDesc = desc[1].trim();
					rebateAmt = rebateMap.get(instDesc);

					EgDemandDetails demandDetail = null;

					if (reason.equalsIgnoreCase(DEMANDRSN_STR_ADVANCE)) {
						demandDetail = installmentWiseDemandDetailsByReason.get(currentInstallment.getDescription())
								.get(reason);
					} else {
						demandDetail = installmentWiseDemandDetailsByReason.get(instDesc).get(reason);
					}

					if (rcptAccInfo.getGlCode().equalsIgnoreCase(
							GLCODEMAP_FOR_CURRENTTAX.get(NMCPTISConstants.GLCODE_FOR_PENALTY))) {

						demandDetail.addCollected(rcptAccInfo.getCrAmount());

					} else if (rcptAccInfo.getGlCode().equalsIgnoreCase(GLCODE_FOR_ADVANCE)) {

						if (demandDetail != null) {
							demandDetail.setAmtCollected(demandDetail.getAmtCollected().add(rcptAccInfo.getCrAmount()));
						} else {
							demandDetail = insertAdvanceCollection(NMCPTISConstants.DEMANDRSN_CODE_ADVANCE,
									rcptAccInfo.getCrAmount(), currentInstallment);

							demand.addEgDemandDetails(demandDetail);
							HibernateUtil.getCurrentSession().flush();

							if (installmentWiseDemandDetailsByReason.get(currentInstallment.getDescription()) == null) {
								Map<String, EgDemandDetails> reasonAndDemandDetail = new HashMap<String, EgDemandDetails>();
								reasonAndDemandDetail.put(DEMANDRSN_STR_ADVANCE, demandDetail);
								installmentWiseDemandDetailsByReason.put(currentInstallment.getDescription(),
										reasonAndDemandDetail);
							} else {
								installmentWiseDemandDetailsByReason.get(currentInstallment.getDescription()).put(
										DEMANDRSN_STR_ADVANCE, demandDetail);
							}

						}
					} else {
						if (isArrearOrCurrentGenTaxGlCode(rcptAccInfo.getGlCode())) {
							if (rebateAmt != null && !rebateAmt.equals(BigDecimal.ZERO)) {
								demandDetail.addRebateAmt(rebateAmt);
							}
						}
						demandDetail.addCollectedWithOnePaisaTolerance(rcptAccInfo.getCrAmount());
					}

					//TODO -- Fix me (Commented to Resolve compilation issues)
					/*persistCollectedReceipts(demandDetail, billRcptInfo.getReceiptNum(), totalAmount,
							billRcptInfo.getReceiptDate(), demandDetail.getAmtCollected());
					LOGGER.info("Persisted demand and receipt details for tax : " + reason + " installment : "
							+ instDesc + " with receipt No : " + billRcptInfo.getReceiptNum() + " for Rs. "
							+ rcptAccInfo.getCrAmount());*/
				}
			}
		}

		Ptdemand ptDemand = (Ptdemand) demand;

		// Activating the demand on payment
		if (ptDemand.getEgptProperty().getStatus().equals(PropertyTaxConstants.STATUS_DEMAND_INACTIVE)) {
			ptDemand.getEgptProperty().setStatus(PropertyTaxConstants.STATUS_ISACTIVE);
		}

		LOGGER.debug("Exiting method saveCollectionDetails");
	}

	/**
	 * Return true if Arrear or Current General Tax GlCode or Advance GlCode
	 * 
	 * @param rcptAccInfo
	 * @return true if Arrear or Current General Tax GlCode or Advance GlCode
	 *         else false
	 */
	private boolean isArrearOrCurrentGenTaxGlCode(String glCode) {
		return glCode.equalsIgnoreCase(GLCODEMAP_FOR_CURRENTTAX.get(DEMANDRSN_CODE_GENERAL_TAX))
				|| glCode.equalsIgnoreCase(GLCODEMAP_FOR_ARREARTAX.get(DEMANDRSN_CODE_GENERAL_TAX));
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

		Map<String, ReceiptAccountInfo> rebateReceiptAccInfoByInstallment = getRebteReceiptAccountInfosByInstallment(billRcptInfo);

		for (ReceiptAccountInfo rcptAccInfo : billRcptInfo.getAccountDetails()) {
			if ((rcptAccInfo.getCrAmount() != null && rcptAccInfo.getCrAmount().compareTo(BigDecimal.ZERO) == 1)
					&& !rcptAccInfo.getIsRevenueAccount()) {
				String[] desc = rcptAccInfo.getDescription().split("-", 2);
				String reason = desc[0].trim();
				String installment = desc[1].trim();
				EgDemandReasonMaster demandReasonMaster = null;

				rebateRcptAccInfo = rebateReceiptAccInfoByInstallment.get(installment);

				for (EgDemandDetails demandDetail : demand.getEgDemandDetails()) {

					demandReasonMaster = demandDetail.getEgDemandReason().getEgDemandReasonMaster();

					if (reason.equals(demandReasonMaster.getReasonMaster())) {
						if (reason.equalsIgnoreCase(DEMANDRSN_CODE_ADVANCE)
								|| installment.equals(demandDetail.getEgDemandReason().getEgInstallmentMaster()
										.getDescription())) {

							if (rebateRcptAccInfo != null) {
								if (demandDetail.getAmtRebate().compareTo(BigDecimal.ZERO) > 0
										&& (demandReasonMaster.getCode().equals(DEMANDRSN_CODE_GENERAL_TAX) || demandReasonMaster
												.getCode().equalsIgnoreCase(DEMANDRSN_CODE_ADVANCE))) {
									demandDetail.setAmtRebate(demandDetail.getAmtRebate().subtract(
											rebateRcptAccInfo.getDrAmount()));
								}
							}

							if (demandDetail.getAmtCollected().compareTo(rcptAccInfo.getCrAmount()) < 0) {
								throw new EGOVRuntimeException(
										"updateDmdDetForRcptCancel : Exception while updating cancel receipt, "
												+ "to be deducted amount " + rcptAccInfo.getCrAmount()
												+ " is greater than the collected amount "
												+ demandDetail.getAmtCollected() + " for demandDetail " + demandDetail);
							}

							demandDetail.setAmtCollected(demandDetail.getAmtCollected().subtract(
									rcptAccInfo.getCrAmount()));

							LOGGER.info("Deducted Collected amount Rs." + rcptAccInfo.getCrAmount() + " for tax : "
									+ reason + " and installment : " + installment);
						}
					}
				}
			}
		}
		updateReceiptStatusWhenCancelled(billRcptInfo.getReceiptNum());
		LOGGER.debug("Exiting method updateDmdDetForRcptCancel");
	}

	/**
	 * Returns a map of Installment description and ReceiptAccountInfo
	 * 
	 * @param billRcptInfo
	 * @return
	 */
	private Map<String, ReceiptAccountInfo> getRebteReceiptAccountInfosByInstallment(BillReceiptInfo billRcptInfo) {
		Map<String, ReceiptAccountInfo> rebateReceiptAccInfoByInstallment = new HashMap<String, ReceiptAccountInfo>();

		for (ReceiptAccountInfo rcptAccInfo : billRcptInfo.getAccountDetails()) {
			if (rcptAccInfo.getGlCode().equalsIgnoreCase(GLCODE_FOR_TAXREBATE)
					|| rcptAccInfo.getGlCode().equalsIgnoreCase(NMCPTISConstants.GLCODE_FOR_ADVANCE_REBATE)) {
				rebateReceiptAccInfoByInstallment
						.put(rcptAccInfo.getDescription().split("-", 2)[1].trim(), rcptAccInfo);
			}
		}

		return rebateReceiptAccInfoByInstallment;
	}

	@Override
	public void apportionCollection(String billRefNo, BigDecimal amtPaid, List<ReceiptDetail> receiptDetails) {
		boolean isEligibleForCurrentRebate = false;
		boolean isEligibleForAdvanceRebate = false;

		if (isRebatePeriodActive()) {
			isEligibleForCurrentRebate = true;
		}

		CollectionApportioner apportioner = new CollectionApportioner(isEligibleForCurrentRebate,
				isEligibleForAdvanceRebate, BigDecimal.ZERO);
		Map<String, BigDecimal> instDemand = getInstDemand(receiptDetails);
		apportioner.apportion(amtPaid, receiptDetails, instDemand);
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
	 * @param rebateApplTaxAmt
	 *            for which Rebate has to be calculated
	 * @return rebate amount.
	 */
	public BigDecimal calcEarlyPayRebate(BigDecimal instTaxAmount, BigDecimal rebateApplTaxAmt, BigDecimal collection) {
		BigDecimal rebate = BigDecimal.ZERO;
		Date today = new Date();
		Calendar firstRebateDate = Calendar.getInstance();
		BigDecimal halfYearTax = instTaxAmount.divide(new BigDecimal(2));
		LOGGER.debug("calcEarlyPayRebate instTaxAmount " + instTaxAmount + " halfYearTax " + halfYearTax
				+ " rebateApplTaxAmt " + rebateApplTaxAmt + " collection " + collection);
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
			if (collection.compareTo(BigDecimal.ZERO) == 1) {
				if (collection.compareTo(halfYearTax) <= 0) {
					rebate = EgovUtils.roundOff((rebateApplTaxAmt.multiply(SECOND_REBATETAX_PERC)).divide(BigDecimal
							.valueOf(100)));
				} else {
					rebate = BigDecimal.ZERO;
				}
			} else {
				rebate = EgovUtils.roundOff((rebateApplTaxAmt.multiply(FIRST_REBATETAX_PERC)).divide(BigDecimal
						.valueOf(100)));
			}
		} else if (today.before(secondRebateDate.getTime()) || today.equals(secondRebateDate.getTime())) {
			if (collection.compareTo(halfYearTax) <= 0) {
				rebate = EgovUtils.roundOff((rebateApplTaxAmt.multiply(SECOND_REBATETAX_PERC)).divide(BigDecimal
						.valueOf(100)));
			} else {
				rebate = BigDecimal.ZERO;
			}
		}
		LOGGER.debug("calcEarlyPayRebate rebate " + rebate);
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
				/*
				 * getting rebate amount from getCramountToBePaid() because
				 * before receipt created CrAmount is Zero and it will updated
				 * as part of receipt creation.
				 */
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
							|| glCode.equalsIgnoreCase(GLCODEMAP_FOR_ARREARTAX.get(DEMANDRSN_CODE_FIRE_SERVICE_TAX))
							|| glCode.equalsIgnoreCase(GLCODEMAP_FOR_CURRENTTAX.get(DEMANDRSN_CODE_GENERAL_TAX))
							|| glCode.equalsIgnoreCase(GLCODEMAP_FOR_CURRENTTAX.get(DEMANDRSN_CODE_SEWERAGE_TAX)) || glCode
								.equalsIgnoreCase(GLCODEMAP_FOR_CURRENTTAX.get(DEMANDRSN_CODE_FIRE_SERVICE_TAX)))
					|| glCode.equalsIgnoreCase(GLCODEMAP_FOR_CURRENTTAX.get(DEMANDRSN_CODE_SEWERAGE_BENEFIT_TAX))
					|| glCode.equalsIgnoreCase(GLCODEMAP_FOR_CURRENTTAX.get(DEMANDRSN_CODE_WATER_BENEFIT_TAX))
					|| glCode.equalsIgnoreCase(GLCODEMAP_FOR_CURRENTTAX.get(DEMANDRSN_CODE_MUNICIPAL_EDUCATIONAL_CESS))
					|| glCode.equalsIgnoreCase(GLCODEMAP_FOR_CURRENTTAX.get(DEMANDRSN_CODE_STREET_TAX))) {
				if (retMap.get(installment) == null) {
					retMap.put(installment, rd.getCramountToBePaid());
				} else {
					retMap.put(installment, retMap.get(installment).add(rd.getCramountToBePaid()));
				}
			}
			if (GLCODES_FOR_CURRENTTAX.contains(glCode) || GLCODES_FOR_ARREARTAX.contains(glCode)) {
				prepareTaxMap(retMap, installment, rd, "FULLTAX");
			} else if (NMCPTISConstants.GLCODE_FOR_ADVANCE.equalsIgnoreCase(glCode)) {
				prepareTaxMap(retMap, installment, rd, "ADVANCE");
			}
		}
		return retMap;
	}

	/**
	 * @param retMap
	 * @param installment
	 * @param rd
	 */
	private void prepareTaxMap(Map<String, BigDecimal> retMap, String installment, ReceiptDetail rd, String type) {
		if (retMap.get(installment + type) == null) {
			retMap.put(installment + type, rd.getCramountToBePaid());
		} else {
			retMap.put(installment + type, retMap.get(installment + type).add(rd.getCramountToBePaid()));
		}
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

	@Override
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

			EgDemandReasonMaster egDemandReasonMaster = demandGenericDao.getDemandReasonMasterByCode(demandReason,
					module());
			if (egDemandReasonMaster == null) {
				throw new EGOVRuntimeException(" Penalty Demand reason Master is null in method  insertPenalty");
			}
			EgDemandReason egDemandReason = demandGenericDao.getDmdReasonByDmdReasonMsterInstallAndMod(
					egDemandReasonMaster, inst, module());
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
	 * Gives the Current EgDemand for billId
	 *
	 * @param upicNo
	 * @return EgDemand
	 */
	@SuppressWarnings("unchecked")
	public EgDemand getCurrentDemand(Long billId) {
		LOGGER.debug("Entered into getCurrentDemand");

		EgBill egBill = (EgBill) DCBDaoFactory.getDaoFactory().getEgBillDao().findById(billId, false);

		String query = "SELECT ptd FROM Ptdemand ptd " + "WHERE ptd.egInstallmentMaster = ? "
				+ "AND ptd.egptProperty.basicProperty.upicNo = ? "
				+ "AND (ptd.egptProperty.status = 'I' OR ptd.egptProperty.status = 'A') "
				+ "AND ptd.egptProperty.basicProperty.active = true";

		EgDemand egDemand = (EgDemand) persistenceService.find(query, PropertyTaxUtil.getCurrentInstallment(), egBill
				.getConsumerId().substring(0, egBill.getConsumerId().indexOf('(')));

		LOGGER.debug("Exiting from getCurrentDemand");
		return egDemand;
	}

	/**
	 * Method used to insert advance collection in EgDemandDetail table.
	 *
	 * @see createDemandDetails() -- EgDemand Details are created
	 *
	 * @return New EgDemandDetails Object
	 */
	public EgDemandDetails insertAdvanceCollection(String demandReason, BigDecimal advanceCollectionAmount,
			Installment installment) {
		EgDemandDetails demandDetail = null;

		if (advanceCollectionAmount != null && advanceCollectionAmount.compareTo(BigDecimal.ZERO) > 0) {
			DemandGenericDao demandGenericDao = new DemandGenericHibDao();

			EgDemandReasonMaster egDemandReasonMaster = demandGenericDao.getDemandReasonMasterByCode(demandReason,
					module());

			if (egDemandReasonMaster == null) {
				throw new EGOVRuntimeException(
						" Advance Demand reason Master is null in method  insertAdvanceCollection");
			}

			EgDemandReason egDemandReason = demandGenericDao.getDmdReasonByDmdReasonMsterInstallAndMod(
					egDemandReasonMaster, installment, module());

			if (egDemandReason == null) {
				throw new EGOVRuntimeException(" Advance Demand reason is null in method  insertAdvanceCollection ");
			}

			demandDetail = createDemandDetails(egDemandReason, advanceCollectionAmount, BigDecimal.ZERO);
		}
		return demandDetail;
	}

	public PersistenceService getPersistenceService() {
		return persistenceService;
	}

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}
}