/*
 * @(#)LicenseBillService.java 3.0, 29 Jul, 2013 1:24:28 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.license.domain.service.integration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.egov.InvalidAccountHeadException;
import org.egov.commons.Installment;
import org.egov.commons.dao.CommonsDaoFactory;
import org.egov.demand.dao.DCBDaoFactory;
import org.egov.demand.dao.DemandGenericDao;
import org.egov.demand.dao.DemandGenericHibDao;
import org.egov.demand.dao.EgBillDao;
import org.egov.demand.dao.EgBillDetailsDao;
import org.egov.demand.dao.EgBillReceiptDao;
import org.egov.demand.dao.EgdmCollectedReceiptDao;
import org.egov.demand.interfaces.BillServiceInterface;
import org.egov.demand.interfaces.Billable;
import org.egov.demand.model.BillReceipt;
import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgBillDetails;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.demand.model.EgDemandReasonMaster;
import org.egov.demand.model.EgReasonCategory;
import org.egov.demand.model.EgdmCollectedReceipt;
import org.egov.demand.utils.DemandConstants;
import org.egov.erpcollection.integration.models.BillReceiptInfo;
import org.egov.erpcollection.integration.models.BillReceiptInfoImpl;
import org.egov.erpcollection.integration.models.ReceiptAccountInfo;
import org.egov.erpcollection.integration.models.ReceiptInstrumentInfo;
import org.egov.erpcollection.integration.services.BillingIntegrationService;
import org.egov.erpcollection.models.ReceiptDetail;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.commons.Module;
import org.egov.infstr.services.PersistenceService;
import org.egov.license.domain.entity.License;
import org.egov.license.domain.entity.LicenseDemand;
import org.egov.license.utils.Constants;
import org.egov.license.utils.LicenseUtils;
import org.hibernate.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LicenseBillService extends BillServiceInterface implements BillingIntegrationService {
	private static final Logger LOG = LoggerFactory.getLogger(LicenseBillService.class);

	protected License license;
	protected EgBillDao billDao;
	private EgBillDetailsDao billDetDao;
	private EgBillReceiptDao billRctDao;
	protected PersistenceService persistenceService;
	private final DemandGenericDao dmdGenDao = new DemandGenericHibDao();

	public void setBillDao(final EgBillDao billDao) {
		this.billDao = billDao;
	}

	public void setLicense(final License license) {
		this.license = license;
	}

	public void setPersistenceService(final PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EgBillDetails> getBilldetails(final Billable billObj) {
		final List<EgBillDetails> billDetails = new ArrayList<EgBillDetails>();
		// final Set<LicenseDemand> demands = this.license.getDemandSet();
		final EgDemand demand = this.license.getCurrentDemand();
		final Date currentDate = new Date();
		final Map installmentWise = new HashMap<Installment, List<EgDemandDetails>>();
		final Set<Installment> sortedInstallmentSet = new TreeSet<Installment>();
		final DemandComparatorByOrderId demandComparatorByOrderId = new DemandComparatorByOrderId();
		final Module module = this.license.getTradeName().getLicenseType().getModule();
		final List<EgDemandDetails> orderedDetailsList = new ArrayList<EgDemandDetails>();
		for (final EgDemandDetails demandDetail : demand.getEgDemandDetails()) {
			// TODO: Code was reviewed by Satyam, No changes required
			final Installment installment = demandDetail.getEgDemandReason().getEgInstallmentMaster();
			if (installmentWise.get(installment) == null) {
				final List<EgDemandDetails> detailsList = new ArrayList<EgDemandDetails>();
				detailsList.add(demandDetail);
				installmentWise.put(demandDetail.getEgDemandReason().getEgInstallmentMaster(), detailsList);
				sortedInstallmentSet.add((installment));
			} else {
				((List<EgDemandDetails>) installmentWise.get(demandDetail.getEgDemandReason().getEgInstallmentMaster())).add(demandDetail);
			}
		}
		for (final Installment i : sortedInstallmentSet) {
			final List<EgDemandDetails> installmentWiseDetails = (List<EgDemandDetails>) installmentWise.get(i);
			Collections.sort(installmentWiseDetails, demandComparatorByOrderId);
			orderedDetailsList.addAll(installmentWiseDetails);
		}

		// for (final EgDemand demand : demands) {
		// if(demand.getBaseDemand().subtract(demand.getAmtCollected()).equals(BigDecimal.ZERO))
		int i = 1;
		for (final EgDemandDetails demandDetail : orderedDetailsList) {

			final EgDemandReason reason = demandDetail.getEgDemandReason();
			final Installment installment = demandDetail.getEgDemandReason().getEgInstallmentMaster();
			// TODO: Code was reviewed by Satyam, No changes required
			// if((installment.getFromDate()).compareTo(LicenseUtils.getCurrInstallment(this.license.getTradeName().getLicenseType().getModule()).getFromDate())<=0)
			// {

			if (demandDetail.getEgDemandReason().getEgDemandReasonMaster().getIsDebit().equalsIgnoreCase("N") && (demandDetail.getAmount().subtract(demandDetail.getAmtRebate())).compareTo(demandDetail.getAmtCollected()) != 0) {
				final EgBillDetails billdetail = new EgBillDetails();
				final EgBillDetails billdetailRebate = new EgBillDetails();
				if (demandDetail.getAmtRebate() != null && !demandDetail.getAmtRebate().equals(BigDecimal.ZERO)) {
					final EgReasonCategory reasonCategory = this.dmdGenDao.getReasonCategoryByCode(Constants.DEMANDRSN_REBATE);
					final List<EgDemandReasonMaster> demandReasonMasterByCategory = this.dmdGenDao.getDemandReasonMasterByCategoryAndModule(reasonCategory, module);
					for (final EgDemandReasonMaster demandReasonMaster : demandReasonMasterByCategory) {
						final EgDemandReason reasonDed = this.dmdGenDao.getDmdReasonByDmdReasonMsterInstallAndMod(demandReasonMaster, installment, module);
						if (demandDetail.getEgDemandReason().getId().equals(reasonDed.getEgDemandReason().getId())) {
							billdetailRebate.setDrAmount(demandDetail.getAmtRebate());
							billdetailRebate.setCrAmount(BigDecimal.ZERO);
							billdetailRebate.setGlcode(reasonDed.getGlcodeId().getGlcode());
							billdetailRebate.setEgDemandReason(demandDetail.getEgDemandReason());
							billdetailRebate.setAdditionalFlag(1);
							billdetailRebate.setCreateTimeStamp(currentDate);
							billdetailRebate.setLastUpdatedTimestamp(currentDate);
							billdetailRebate.setOrderNo(i++);
							billdetailRebate.setDescription(reasonDed.getEgDemandReasonMaster().getReasonMaster() + " - " + installment.getDescription());
							billDetails.add(billdetailRebate);
						}
					}
				}
				if (demandDetail.getAmount() != null) {
					billdetail.setDrAmount(BigDecimal.ZERO);
					billdetail.setCrAmount(demandDetail.getAmount());
				}

				LOGGER.info("demandDetail.getEgDemandReason()" + demandDetail.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster() + " glcodeerror" + demandDetail.getEgDemandReason().getGlcodeId());
				billdetail.setGlcode(demandDetail.getEgDemandReason().getGlcodeId().getGlcode());
				billdetail.setEgDemandReason(demandDetail.getEgDemandReason());
				billdetail.setAdditionalFlag(1);
				billdetail.setCreateTimeStamp(currentDate);
				billdetail.setLastUpdatedTimestamp(currentDate);
				// billdetail.setOrderNo(Integer.valueOf(demandDetail.getEgDemandReason().getEgDemandReasonMaster().getOrderId().toString()));
				billdetail.setOrderNo(i++);
				billdetail.setDescription(reason.getEgDemandReasonMaster().getReasonMaster() + " - " + installment.getDescription());
				billDetails.add(billdetail);
			}
			// }
			// }

		}
		LOG.debug("created Bill Details");
		return billDetails;
	}

	@Override
	public Boolean updateReceiptDetails(final Set<BillReceiptInfo> billReceipts) {
		Boolean status = false;
		if (billReceipts != null) {

			try {
				status = this.updateNewReceipt(billReceipts);
			} catch (final ObjectNotFoundException e) {
				return status;
			} catch (final InvalidAccountHeadException e) {
				return status;
			}
		}
		return status;
	}

	public Boolean updateDemandDetails(final BillReceiptInfo billReceipt) {

		try {
			BillReceiptInfoImpl billReceiptInfoImpl;
			billReceiptInfoImpl = (BillReceiptInfoImpl) billReceipt;
			final EgBill bill = ((EgBill) this.billDao.findById(Long.valueOf(billReceiptInfoImpl.getBillReferenceNum()), false));
			final EgDemand demand = bill.getEgDemand();
			if (billReceipt.getEvent().equals(EVENT_RECEIPT_CREATED)) {
				BigDecimal amtCollected = BigDecimal.ZERO;
				BigDecimal amtRebate = BigDecimal.ZERO;
				final LicenseDemand ld = (LicenseDemand) this.persistenceService.find("from LicenseDemand where id=?", demand.getId());
				final Module module = ld.getLicense().getTradeName().getLicenseType().getModule();
				for (final EgDemandDetails demandDetail : demand.getEgDemandDetails()) {
					final Installment installment = demandDetail.getEgDemandReason().getEgInstallmentMaster();
					if ((installment.getFromDate()).compareTo(LicenseUtils.getCurrInstallment((module)).getFromDate()) <= 0) {

						final Set<ReceiptAccountInfo> accountDetails = billReceipt.getAccountDetails();
						for (final ReceiptAccountInfo rInfo : accountDetails) {
							if (rInfo.getGlCode().equalsIgnoreCase(Constants.GLCODE_FOR_TAXREBATE) && rInfo.getDrAmount() != null && rInfo.getDrAmount().compareTo(BigDecimal.ZERO) == 1) {
								amtRebate = rInfo.getDrAmount();
							}
						}
						for (final ReceiptAccountInfo rInfo : accountDetails) {
							if (rInfo.getGlCode().equalsIgnoreCase(demandDetail.getEgDemandReason().getGlcodeId().getGlcode())) {
								if ((rInfo.getCrAmount() != null && rInfo.getCrAmount().compareTo(BigDecimal.ZERO) == 1)) {
									final EgReasonCategory reasonCategory = this.dmdGenDao.getReasonCategoryByCode(Constants.DEMANDRSN_REBATE);
									final List<EgDemandReasonMaster> demandReasonMasterByCategory = this.dmdGenDao.getDemandReasonMasterByCategoryAndModule(reasonCategory, module);
									for (final EgDemandReasonMaster demandReasonMaster : demandReasonMasterByCategory) {
										final EgDemandReason reasonDed = this.dmdGenDao.getDmdReasonByDmdReasonMsterInstallAndMod(demandReasonMaster, installment, module);
										if (!amtRebate.equals(BigDecimal.ZERO) && demandDetail.getEgDemandReason().getId().equals(reasonDed.getEgDemandReason().getId())) {
											demandDetail.setAmtRebate(amtRebate);
											demandDetail.setAmtCollected(rInfo.getCrAmount().subtract(amtRebate));
											break;
										} else {
											demandDetail.setAmtCollected(rInfo.getCrAmount());
										}
									}
									amtCollected = amtCollected.add(rInfo.getCrAmount());
								}
							}
						}
					}
				}
				amtCollected = amtCollected.subtract(amtRebate);
				demand.setAmtRebate(amtRebate);
				demand.setAmtCollected(amtCollected);
				// update only if it is new License else there is no workflow
				this.updateWorkflowState(ld.getLicense());

			} else if (billReceipt.getEvent().equals(EVENT_RECEIPT_CANCELLED)) {
				reconcileCollForRcptCancel(demand, billReceipt);

			} else if (billReceipt.getEvent().equals(EVENT_INSTRUMENT_BOUNCED)) {
				reconcileCollForChequeBounce(demand, billReceipt);// needs to be done for multiple
			}
		} catch (final Exception e) {
			LOGGER.error("Exception", e);
			throw new EGOVRuntimeException(e.getMessage());
		}

		return true;
	}

	/**
	 * Deducts the collected amounts as per the amount of the cancelled receipt.
	 */
	protected void reconcileCollForRcptCancel(final EgDemand demand, final BillReceiptInfo billRcptInfo) {
		LOGGER.debug("updateDemandForCancellation : Updating Collection For Demand : Demand - " + demand + " with BillReceiptInfo - " + billRcptInfo);
		cancelBill(Long.valueOf(billRcptInfo.getBillReferenceNum()));
		demand.setAmtCollected(demand.getAmtCollected().subtract(billRcptInfo.getTotalAmount()));
		updateDmdDetForRcptCancel(demand, billRcptInfo);
		LOGGER.debug("updateDemandForCancellation : Updated Collection For Demand : " + demand);
	}

	private EgDemand cancelBill(final Long billId) {
		final EgDemand egDemand = null;
		if (billId != null) {
			final EgBillDao egBillDao = DCBDaoFactory.getDaoFactory().getEgBillDao();
			final EgBill egBill = (EgBill) egBillDao.findById(billId, false);
			egBill.setIs_Cancelled("Y");
		}
		return egDemand;
	}

	/**
	 * Reconciles the collection for respective account heads thats been paid with given cancel receipt
	 * @param demand
	 * @param billRcptInfo
	 */
	private void updateDmdDetForRcptCancel(final EgDemand demand, final BillReceiptInfo billRcptInfo) {
		LOGGER.debug("Entering method updateDmdDetForRcptCancel");
		for (final ReceiptAccountInfo rcptAccInfo : billRcptInfo.getAccountDetails()) {
			if ((rcptAccInfo.getCrAmount() != null && rcptAccInfo.getCrAmount().compareTo(BigDecimal.ZERO) == 1) && !rcptAccInfo.getIsRevenueAccount()) {
				final String[] desc = rcptAccInfo.getDescription().split("-", 2);
				final String reason = desc[0].trim();
				final String installment = desc[1].trim();
				for (final EgDemandDetails demandDetail : demand.getEgDemandDetails()) {
					if (reason.equals(demandDetail.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster()) && installment.equals(demandDetail.getEgDemandReason().getEgInstallmentMaster().getDescription())) {
						demandDetail.setAmtCollected(demandDetail.getAmtCollected().subtract(rcptAccInfo.getCrAmount()));
						LOGGER.info("Deducted Collected amount and receipt details for tax : " + reason + " installment : " + installment + " with receipt No : " + billRcptInfo.getReceiptNum() + " for Rs. " + demandDetail.getAmtCollected());
					}
				}
			}
		}
		updateReceiptStatusWhenCancelled(billRcptInfo.getReceiptNum());
		LOGGER.debug("Exiting method saveCollectionAndDemandDetails");
	}

	protected void updateReceiptStatusWhenCancelled(final String receiptNumber) {
		final DemandGenericDao dmdGenDao = new DemandGenericHibDao();
		final List<EgdmCollectedReceipt> egdmCollectedReceipts = dmdGenDao.getAllEgdmCollectedReceipts(receiptNumber);
		if (egdmCollectedReceipts != null && !egdmCollectedReceipts.isEmpty()) {
			for (final EgdmCollectedReceipt egDmCollectedReceipt : egdmCollectedReceipts) {
				egDmCollectedReceipt.setStatus(DemandConstants.CANCELLED_RECEIPT);
				egDmCollectedReceipt.setUpdatedTime(new Date());
			}
		}
	}

	protected BigDecimal reconcileCollForChequeBounce(final EgDemand demand, final BillReceiptInfo billRcptInfo) {

		/**
		 * Deducts the collected amounts as per the amount of the bounced cheque, and also imposes a cheque-bounce penalty.
		 */
		LOGGER.debug("updateDemandForChequeBounce : Updating Collection For Demand : Demand - " + demand + " with BillReceiptInfo - " + billRcptInfo);
		final LicenseDemand ld = (LicenseDemand) demand;
		BigDecimal totalCollChqBounced = getTotalChequeAmt(billRcptInfo);
		final BigDecimal chqBouncePenalty = Constants.CHQ_BOUNCE_PENALTY;
		// cancelBill(Long.valueOf(billRcptInfo.getBillReferenceNum()));
		EgDemandDetails dmdDet = null;
		final EgDemandDetails penaltyDmdDet = getDemandDetail(demand, getCurrentInstallment(ld.getLicense().getTradeName().getLicenseType().getModule()), Constants.DEMANDRSN_STR_CHQ_BOUNCE_PENALTY);
		if (penaltyDmdDet == null) {
			dmdDet = insertPenalty(chqBouncePenalty, ld.getLicense().getTradeName().getLicenseType().getModule());
		} else {
			BigDecimal existDmdDetAmt = penaltyDmdDet.getAmount();
			existDmdDetAmt = (existDmdDetAmt == null || existDmdDetAmt.equals(BigDecimal.ZERO)) ? existDmdDetAmt = BigDecimal.ZERO : existDmdDetAmt;
			penaltyDmdDet.setAmount(existDmdDetAmt.add(chqBouncePenalty));
			dmdDet = penaltyDmdDet;
		}

		// setting this min amount into demand to check next payment should be
		// min of this amount with mode of payment cash or DD
		demand.setMinAmtPayable(totalCollChqBounced.add(chqBouncePenalty));
		demand.setAmtCollected(demand.getAmtCollected().subtract(billRcptInfo.getTotalAmount()));
		demand.setBaseDemand(demand.getBaseDemand().add(chqBouncePenalty));
		demand.setStatus(Constants.DMD_STATUS_CHEQUE_BOUNCED);
		demand.addEgDemandDetails(dmdDet);
		totalCollChqBounced = updateDmdDetForChqBounce(demand, billRcptInfo, totalCollChqBounced);
		LOGGER.debug("updateDemandForChequeBounce : Updated Collection For Demand : " + demand);
		return totalCollChqBounced;
	}

	/**
	 * reverse the amount collected for each demand detail
	 * @param demand
	 * @param c
	 * @param totalCollChqBounced
	 */

	private BigDecimal updateDmdDetForChqBounce(final EgDemand demand, final BillReceiptInfo c, BigDecimal totalCollChqBounced) {
		new TreeSet<EgDemandDetails>();
		List<EgDemandDetails> demandList = new ArrayList<EgDemandDetails>();
		demandList = (List<EgDemandDetails>) demand.getEgDemandDetails();
		Collections.sort(demandList, new DemandComparatorByOrderId());
		Collections.reverse(demandList);
		for (final EgDemandDetails dd : demandList) {
			final BigDecimal amtCollected = dd.getAmtCollected();
			totalCollChqBounced = totalCollChqBounced.subtract(amtCollected);
			if (totalCollChqBounced.longValue() >= 0) {
				dd.setAmtCollected(BigDecimal.ZERO);
				demand.setBaseDemand(demand.getBaseDemand().subtract(amtCollected));
			} else {
				dd.setAmtCollected(amtCollected.subtract(totalCollChqBounced));
				demand.setBaseDemand(demand.getBaseDemand().subtract(totalCollChqBounced));
				totalCollChqBounced = BigDecimal.ZERO;

			}

		}
		return totalCollChqBounced;
	}

	private EgDemandDetails getDemandDetail(final EgDemand demand, final Installment currentInstallment, final String demandrsnStrChqBouncePenalty) {
		EgDemandDetails chqBounceDemand = null;
		for (final EgDemandDetails dd : demand.getEgDemandDetails()) {
			if (dd.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster().equalsIgnoreCase(demandrsnStrChqBouncePenalty)) {
				chqBounceDemand = dd;
				break;
			}
		}
		return chqBounceDemand;
	}

	protected void updateWorkflowState(final License license2) {
		if (license2.getState().getValue().contains(Constants.WORKFLOW_STATE_TYPE_RENEWLICENSE)) {
			license2.changeState(Constants.WORKFLOW_STATE_TYPE_RENEWLICENSE + Constants.WORKFLOW_STATE_COLLECTED, license2.getState().getOwner(), "Amount Collected ");
			license2.updateExpiryDate(new Date());
		} else {
			license2.changeState(Constants.WORKFLOW_STATE_TYPE_CREATENEWLICENSE + Constants.WORKFLOW_STATE_COLLECTED, license2.getState().getOwner(), "Amount Collected ");
		}

	}

	protected boolean updateNewReceipt(final Set<BillReceiptInfo> billReceipts) throws InvalidAccountHeadException, ObjectNotFoundException {
		try {
			for (final BillReceiptInfo bri : billReceipts) {
				this.linkBillToReceipt(bri);
				this.updateBillDetails(bri);
				this.updateDemandDetails(bri);
			}
		} catch (final Exception e) {
			return false;
		}
		return true;

	}

	private EgBill updateBill(final BillReceiptInfo bri, final EgBill egBill, final BigDecimal totalCollectedAmt) throws InvalidAccountHeadException {
		this.billDao = DCBDaoFactory.getDaoFactory().getEgBillDao();
		this.billDetDao = DCBDaoFactory.getDaoFactory().getEgBillDetailsDao();
		if (bri != null) {
			for (final EgBillDetails billDet : egBill.getEgBillDetails()) {
				Boolean glCodeExist = false;
				for (final ReceiptAccountInfo acctDet : bri.getAccountDetails()) {
					if (billDet.getGlcode().equals(acctDet.getGlCode())) {
						glCodeExist = true;
						BigDecimal amtCollected = billDet.getCollectedAmount();
						if (amtCollected == null) {
							amtCollected = BigDecimal.ZERO;
						}
						billDet.setCollectedAmount(acctDet.getCrAmount().subtract(amtCollected));
						this.billDetDao.update(billDet);
					}
				}
				if (!glCodeExist) {
					throw new InvalidAccountHeadException("GlCode does not exist for " + billDet.getGlcode());
				}
			}
			egBill.setTotalCollectedAmount(totalCollectedAmt);
			this.billDao.update(egBill);
		}
		return egBill;
	}

	public BigDecimal getEgBillDetailCollection(final EgBillDetails billdet) {
		BigDecimal collectedAmt = billdet.getCollectedAmount();
		if (billdet.getCollectedAmount() == null) {
			collectedAmt = BigDecimal.ZERO;
		}
		return collectedAmt;

	}

	public EgBill updateBillForChqBounce(final BillReceiptInfo bri, final EgBill egBill, final BigDecimal totalChqAmt) {
		this.billDetDao = DCBDaoFactory.getDaoFactory().getEgBillDetailsDao();
		this.billDao = DCBDaoFactory.getDaoFactory().getEgBillDao();
		final BigDecimal zeroVal = BigDecimal.ZERO;
		if ((totalChqAmt != null) && !totalChqAmt.equals(zeroVal) && (egBill != null)) {
			final List<EgBillDetails> billList = new ArrayList<EgBillDetails>(egBill.getEgBillDetails());
			// Reversed the list because the knocking off the amount should
			// start from current Installment to least Installment.
			Collections.reverse(billList);
			BigDecimal carry = totalChqAmt;
			for (final EgBillDetails billdet : billList) {
				BigDecimal balanceAmt = BigDecimal.ZERO;
				BigDecimal remAmount = BigDecimal.ZERO;
				balanceAmt = this.getEgBillDetailCollection(billdet);
				if ((balanceAmt != null) && (balanceAmt.compareTo(zeroVal) > 0)) {
					if ((carry.compareTo(zeroVal) > 0) && (carry.subtract(balanceAmt).compareTo(zeroVal) > 0)) {
						carry = carry.subtract(balanceAmt);
						remAmount = balanceAmt;
					} else if ((carry.compareTo(zeroVal) > 0) && (carry.subtract(balanceAmt).compareTo(zeroVal) <= 0)) {
						remAmount = carry;
						carry = BigDecimal.ZERO;
					}
					if (remAmount.compareTo(zeroVal) > 0) {
						billdet.setCollectedAmount(remAmount);
						this.billDetDao.update(billdet);
					}
				}
			}
			egBill.setTotalCollectedAmount(totalChqAmt);
			this.billDao.update(egBill);
		}
		return egBill;
	}

	EgBill updateBillDetails(final BillReceiptInfo bri) throws InvalidAccountHeadException {
		EgBill egBill = null;
		this.billDao = DCBDaoFactory.getDaoFactory().getEgBillDao();
		this.billDetDao = DCBDaoFactory.getDaoFactory().getEgBillDetailsDao();
		if (bri == null) {
			throw new EGOVRuntimeException(" BillReceiptInfo Object is null ");
		}
		egBill = (EgBill) this.billDao.findById(new Long(bri.getBillReferenceNum()), false);
		final List<EgBillDetails> billDetList = this.billDetDao.getBillDetailsByBill(egBill);

		if ((bri.getEvent() != null) && bri.getEvent().equals(BillingIntegrationService.EVENT_RECEIPT_CREATED)) {
			final BigDecimal totalCollectedAmt = this.calculateTotalCollectedAmt(bri, billDetList);
			egBill = this.updateBill(bri, egBill, totalCollectedAmt);
		} else if ((bri.getEvent() != null) && bri.getEvent().equals(BillingIntegrationService.EVENT_INSTRUMENT_BOUNCED)) {
			egBill = this.updateBillForChqBounce(bri, egBill, this.getTotalChequeAmt(bri));
		}
		return egBill;
	}

	public BigDecimal getTotalChequeAmt(final BillReceiptInfo bri) {
		BigDecimal totalCollAmt = BigDecimal.ZERO;
		try {
			if (bri != null) {
				for (final ReceiptInstrumentInfo rctInst : bri.getBouncedInstruments()) {
					if (rctInst.getInstrumentAmount() != null) {
						totalCollAmt = totalCollAmt.add(rctInst.getInstrumentAmount());
					}
				}
			}
		} catch (final EGOVRuntimeException e) {
			throw new EGOVRuntimeException("Exception in calculate Total Collected Amt" + e);
		}

		return totalCollAmt;
	}

	public BigDecimal calculateTotalCollectedAmt(final BillReceiptInfo bri, final List<EgBillDetails> billDetList) throws InvalidAccountHeadException {
		BigDecimal totalCollAmt = BigDecimal.ZERO;
		try {
			if ((bri != null) && (billDetList != null)) {
				for (final EgBillDetails billDet : billDetList) {
					Boolean glCodeExist = false;
					for (final ReceiptAccountInfo acctDet : bri.getAccountDetails()) {
						if (billDet.getGlcode().equals(acctDet.getGlCode())) {
							glCodeExist = true;
							totalCollAmt = totalCollAmt.add(acctDet.getCrAmount());
						}
					}
					if (!glCodeExist) {
						throw new InvalidAccountHeadException("GlCode does not exist for " + billDet.getGlcode());
					}
				}
			}
		} catch (final EGOVRuntimeException e) {
			throw new EGOVRuntimeException("Exception in calculate Total Collected Amt" + e);
		}

		return totalCollAmt;
	}

	private BillReceipt prepareBillReceiptBean(final BillReceiptInfo bri, final EgBill egBill, final BigDecimal totalCollectedAmt) {

		BillReceipt billRecpt = null;
		if ((bri != null) && (egBill != null) && (totalCollectedAmt != null)) {
			billRecpt = new BillReceipt();
			billRecpt.setBillId(egBill);
			billRecpt.setReceiptAmt(totalCollectedAmt);
			billRecpt.setReceiptNumber(bri.getReceiptNum());
			billRecpt.setReceiptDate(bri.getReceiptDate());
			billRecpt.setCollectionStatus(bri.getReceiptStatus().getCode());
			billRecpt.setCreatedBy(bri.getCreatedBy());
			billRecpt.setModifiedBy(bri.getModifiedBy());
			billRecpt.setCreatedDate(new Date());
			billRecpt.setModifiedDate(new Date());
			billRecpt.setIsCancelled(Boolean.FALSE);
		}
		return billRecpt;
	}

	private BillReceipt linkBillToReceipt(final BillReceiptInfo bri) throws InvalidAccountHeadException, ObjectNotFoundException {
		this.billDao = DCBDaoFactory.getDaoFactory().getEgBillDao();
		this.billDetDao = DCBDaoFactory.getDaoFactory().getEgBillDetailsDao();
		this.billRctDao = DCBDaoFactory.getDaoFactory().getEgBillReceiptDao();
		BillReceipt billRecpt = null;
		if (bri == null) {
			throw new EGOVRuntimeException(" BillReceiptInfo Object is null ");
		}
		final EgBill egBill = (EgBill) this.billDao.findById(new Long(bri.getBillReferenceNum()), false);
		if (egBill == null) {
			throw new EGOVRuntimeException(" EgBill Object is null for the Bill Number" + bri.getBillReferenceNum());
		}
		final List<EgBillDetails> billDetList = this.billDetDao.getBillDetailsByBill(egBill);
		final BigDecimal totalCollectedAmt = this.calculateTotalCollectedAmt(bri, billDetList);
		if (bri.getEvent() == null) {
			throw new EGOVRuntimeException(" Event in BillReceiptInfo Object is Null");
		}
		if (bri.getEvent().equals(BillingIntegrationService.EVENT_RECEIPT_CREATED)) {
			billRecpt = this.prepareBillReceiptBean(bri, egBill, totalCollectedAmt);
			this.billRctDao.create(billRecpt);

		} else if (bri.getEvent().equals(BillingIntegrationService.EVENT_RECEIPT_CANCELLED)) {
			billRecpt = this.updateBillReceiptForCancellation(bri, egBill, totalCollectedAmt);
		}
		return billRecpt;
	}

	private BillReceipt updateBillReceiptForCancellation(final BillReceiptInfo bri, final EgBill egBill, final BigDecimal totalCollectedAmt) {
		BillReceipt billRecpt = null;
		if (bri == null) {
			throw new EGOVRuntimeException(" BillReceiptInfo Object is null ");
		}
		if ((egBill != null) && (totalCollectedAmt != null)) {
			this.billRctDao = DCBDaoFactory.getDaoFactory().getEgBillReceiptDao();
			billRecpt = this.billRctDao.getBillReceiptByEgBill(egBill);
			if (billRecpt == null) {
				throw new EGOVRuntimeException(" Bill receipt Object is null for the EgBill " + egBill.getId());
			}
			if (bri.getEvent().equals(BillingIntegrationService.EVENT_RECEIPT_CANCELLED)) {
				billRecpt.setIsCancelled(Boolean.TRUE);
			}
			billRecpt.setReceiptAmt(totalCollectedAmt.subtract(billRecpt.getReceiptAmt()));
		} else {
			throw new EGOVRuntimeException(" EgBill Object is null for the Bill Number" + bri.getBillReferenceNum() + "in updateBillReceiptForCancellation method");
		}
		return billRecpt;
	}

	protected EgdmCollectedReceipt persistCollectedReceipts(final EgDemandDetails egDemandDetails, final String receiptNumber, final BigDecimal receiptAmount, final Date receiptDate, final BigDecimal reasonAmount) {
		final EgdmCollectedReceiptDao egdmCollectedReceiptDao = DCBDaoFactory.getDaoFactory().getEgdmCollectedReceiptsDao();
		final EgdmCollectedReceipt egDmCollectedReceipt = new EgdmCollectedReceipt();
		egDmCollectedReceipt.setReceiptNumber(receiptNumber);
		egDmCollectedReceipt.setReceiptDate(receiptDate);
		egDmCollectedReceipt.setAmount(receiptAmount);
		egDmCollectedReceipt.setReasonAmount(reasonAmount);
		egDmCollectedReceipt.setStatus(DemandConstants.NEWRECEIPT);
		egDmCollectedReceipt.setEgdemandDetail(egDemandDetails);
		egdmCollectedReceiptDao.create(egDmCollectedReceipt);
		return egDmCollectedReceipt;
	}

	@Override
	public void apportionPaidAmount(final String billReferenceNumber, final BigDecimal actualAmountPaid, final ArrayList<ReceiptDetail> receiptDetailsArray) {
	}

	/**
	 * Method used to insert penalty in EgDemandDetail table. Penalty Amount will be calculated depending upon the cheque Amount.
	 * @see createDemandDetails() -- EgDemand Details are created
	 * @see getPenaltyAmount() --Penalty Amount is calculated
	 * @param chqBouncePenalty
	 * @return New EgDemandDetails Object
	 */
	EgDemandDetails insertPenalty(final BigDecimal chqBouncePenalty, final Module module) {
		EgDemandDetails demandDetail = null;
		if (chqBouncePenalty != null && chqBouncePenalty.compareTo(BigDecimal.ZERO) > 0) {
			final DemandGenericDao demandGenericDao = new DemandGenericHibDao();
			final Installment currInstallment = getCurrentInstallment(module);
			final EgDemandReasonMaster egDemandReasonMaster = demandGenericDao.getDemandReasonMasterByCode(Constants.DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY, module);
			if (egDemandReasonMaster == null) {
				throw new EGOVRuntimeException(" Penalty Demand reason Master is null in method  insertPenalty");
			}
			final EgDemandReason egDemandReason = demandGenericDao.getDmdReasonByDmdReasonMsterInstallAndMod(egDemandReasonMaster, currInstallment, module);
			if (egDemandReason == null) {
				throw new EGOVRuntimeException(" Penalty Demand reason is null in method  insertPenalty ");
			}
			demandDetail = EgDemandDetails.fromReasonAndAmounts(chqBouncePenalty, egDemandReason, BigDecimal.ZERO);
		}
		return demandDetail;
	}

	protected Installment getInstallmentForDate(final Date date, final Module module) {
		return CommonsDaoFactory.getDAOFactory().getInstallmentDao().getInsatllmentByModuleForGivenDate(module, date);
	}

	protected Installment getCurrentInstallment(final Module module) {
		return getInstallmentForDate(new Date(), module);
	}

	@Override
	public void cancelBill() {

	}

}