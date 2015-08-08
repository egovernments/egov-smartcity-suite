/**
 
 */
package org.egov.collection.integration.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.ReceiptDetail;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.collection.entity.ReceiptVoucher;
import org.egov.collection.integration.models.BillInfo;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.collection.integration.models.BillReceiptInfoImpl;
import org.egov.collection.integration.models.PaymentInfo;
import org.egov.collection.integration.models.PaymentInfoBank;
import org.egov.collection.integration.models.PaymentInfoCash;
import org.egov.collection.integration.models.PaymentInfoChequeDD;
import org.egov.collection.service.ReceiptHeaderService;
import org.egov.collection.utils.CollectionCommon;
import org.egov.collection.utils.CollectionsUtil;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.Fund;
import org.egov.commons.service.CommonsServiceImpl;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infstr.ValidationError;
import org.egov.infstr.services.PersistenceService;
import org.egov.lib.security.terminal.model.Location;
import org.egov.model.instrument.InstrumentHeader;
import org.springframework.beans.factory.annotation.Autowired;

/**
 Collections integration service implementation - exposes APIs that can be
 used by other applications (typically billing systems) to interact with the
 collections module.
 */
public class CollectionIntegrationServiceImpl extends PersistenceService<ReceiptHeader, Long> implements CollectionIntegrationService {

	private static final Logger LOGGER = Logger.getLogger(CollectionIntegrationServiceImpl.class);

	@Autowired
	private CommonsServiceImpl commonsServiceImpl;

	private PersistenceService persistenceService;

	@Autowired
	private CollectionsUtil collectionsUtil;

	@Autowired
	private ReceiptHeaderService receiptHeaderService;

	List<ValidationError> errors = new ArrayList<ValidationError>();

	public void setReceiptHeaderService(ReceiptHeaderService receiptHeaderService) {
		this.receiptHeaderService = receiptHeaderService;
	}

	public void setCollectionsUtil(CollectionsUtil collectionsUtil) {
		this.collectionsUtil = collectionsUtil;
	}

	private CollectionCommon collectionCommon;

	public void setCollectionCommon(CollectionCommon collectionCommon) {
		this.collectionCommon = collectionCommon;
	}

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	/*
	 (non-Javadoc)
	 
	 @seeorg.egov.infstr.collections.integration.ICollectionInterface#
	 getBillReceiptInfo(java.lang.String, java.lang.String)
	 */
	@Override
	public List<BillReceiptInfo> getBillReceiptInfo(String serviceCode, String refNum) {
		ArrayList<BillReceiptInfo> receipts = new ArrayList<BillReceiptInfo>();

		// Get all receipt headers for given reference number
		List<ReceiptHeader> receiptHeaders = findAllByNamedQuery(CollectionConstants.QUERY_RECEIPTS_BY_REFNUM_AND_SERVICECODE, refNum, serviceCode);
		if (receiptHeaders == null || receiptHeaders.isEmpty()) {
			return null;
		}

		for (ReceiptHeader receiptHeader : receiptHeaders) {
			receipts.add(new BillReceiptInfoImpl(receiptHeader));
		}
		return receipts;
	}

	/*
	 (non-Javadoc)
	 
	 @seeorg.egov.infstr.collections.integration.ICollectionInterface#
	 getBillReceiptInfo(java.lang.String, java.util.Set)
	 */
	@Override
	public Map<String, List<BillReceiptInfo>> getBillReceiptInfo(String serviceCode, Set<String> refNums) {
		HashMap<String, List<BillReceiptInfo>> receipts = new HashMap<String, List<BillReceiptInfo>>();

		for (String refNum : refNums) {
			receipts.put(refNum, getBillReceiptInfo(serviceCode, refNum));
		}

		return receipts;
	}

	/*
	 (non-Javadoc)
	 
	 @seeorg.egov.infstr.collections.integration.ICollectionInterface#
	 getInstrumentReceiptInfo(java.lang.String, java.lang.String)
	 */
	@Override
	public List<BillReceiptInfo> getInstrumentReceiptInfo(String serviceCode, String instrumentNum) {
		ArrayList<BillReceiptInfo> receipts = new ArrayList<BillReceiptInfo>();

		List<ReceiptHeader> receiptHeaders = (List<ReceiptHeader>) findAllByNamedQuery(CollectionConstants.QUERY_RECEIPTS_BY_INSTRUMENTNO_AND_SERVICECODE, instrumentNum,
				serviceCode);
		if (receiptHeaders == null || receiptHeaders.isEmpty()) {
			return null;
		}

		for (ReceiptHeader receiptHeader : receiptHeaders) {
			receipts.add(new BillReceiptInfoImpl(receiptHeader));
		}
		return receipts;
	}

	/*
	 (non-Javadoc)
	 
	 @seeorg.egov.infstr.collections.integration.ICollectionInterface#
	 getInstrumentReceiptInfo(java.lang.String, java.util.Set)
	 */
	@Override
	public Map<String, List<BillReceiptInfo>> getInstrumentReceiptInfo(String serviceCode, Set<String> instrumentNums) {
		HashMap<String, List<BillReceiptInfo>> receipts = new HashMap<String, List<BillReceiptInfo>>();

		for (String instrumentNum : instrumentNums) {
			receipts.put(instrumentNum, getInstrumentReceiptInfo(serviceCode, instrumentNum));
		}

		return receipts;
	}

	/*
	 (non-Javadoc)
	 
	 @see
	 org.egov.infstr.collections.integration.CollectionIntegrationService#
	 getReceiptInfo (java.lang.String, java.lang.String)
	 */
	@Override
	public BillReceiptInfo getReceiptInfo(String serviceCode, String receiptNum) {
		ReceiptHeader receiptHeader = (ReceiptHeader) findByNamedQuery(CollectionConstants.QUERY_RECEIPT_BY_RECEIPTNUM_AND_SERVICECODE, serviceCode, receiptNum, receiptNum);
		if (receiptHeader == null) {
			return null;
		} else {
			// Create bill receipt info
			BillReceiptInfoImpl receiptInfo = new BillReceiptInfoImpl(receiptHeader);

			return receiptInfo;
		}
	}

	/*
	 (non-Javadoc)
	 
	 @see
	 org.egov.infstr.collections.integration.CollectionIntegrationService#
	 getReceiptInfo (java.lang.String, java.util.Set)
	 */
	@Override
	public Map<String, BillReceiptInfo> getReceiptInfo(String serviceCode, Set<String> receiptNums) {
		HashMap<String, BillReceiptInfo> receipts = new HashMap<String, BillReceiptInfo>();

		for (String receiptNum : receiptNums) {
			receipts.put(receiptNum, getReceiptInfo(serviceCode, receiptNum));
		}

		return receipts;
	}

	/*
	 
	 @see
	 org.egov.infstr.collections.integration.CollectionIntegrationService#
	 createReceipt (BillInfo bill, List<PaymentInfo> paymentInfoList)
	 */
	@Override
	public BillReceiptInfo createReceipt(BillInfo bill, PaymentInfo paymentInfo) {
		LOGGER.info("Logs For HandHeldDevice Permance Test : Receipt Creation Started....");
		Fund fund = commonsServiceImpl.fundByCode(bill.getFundCode());
		if (fund == null) {
			throw new EGOVRuntimeException("Fund not present for the fund code [" + bill.getFundCode() + "].");
		}

		Department dept = (Department) persistenceService.findByNamedQuery(CollectionConstants.QUERY_DEPARTMENT_BY_CODE, bill.getDepartmentCode());

		if (dept == null) {
			throw new EGOVRuntimeException("Department not present for the department code [" + bill.getDepartmentCode() + "].");
		}
		ReceiptHeader receiptHeader =collectionCommon.initialiseReceiptModelWithBillInfo(bill, fund, dept);

		receiptHeader.setCreatedDate(new Date());
		receiptHeader.setReceiptdate(new Date());
		receiptHeader.setReceipttype(CollectionConstants.RECEIPT_TYPE_BILL);
		receiptHeader.setIsModifiable(Boolean.TRUE);
		receiptHeader.setIsReconciled(Boolean.FALSE);
		receiptHeader.setCollectiontype(CollectionConstants.COLLECTION_TYPE_FIELDCOLLECTION);
		receiptHeader.setStatus(collectionsUtil.getEgwStatusForModuleAndCode(CollectionConstants.MODULE_NAME_RECEIPTHEADER, CollectionConstants.RECEIPT_STATUS_CODE_APPROVED));
		receiptHeader.setPaidBy(bill.getPaidBy());

		if (EgovThreadLocals.getUserId() != null) {
		        User user = collectionsUtil.getUserById(EgovThreadLocals.getUserId());
			receiptHeader.setCreatedBy(user);
			receiptHeader.setLastModifiedBy(user);
			Location location = collectionsUtil.getLocationByUser(EgovThreadLocals.getUserId());
			if (location != null) {
				receiptHeader.setLocation(location);
			}
		}

		BigDecimal chequeDDInstrumenttotal = BigDecimal.ZERO;
		BigDecimal otherInstrumenttotal = BigDecimal.ZERO;

		// populate instrument details
		List<InstrumentHeader> instrumentHeaderList = new ArrayList<InstrumentHeader>();

			String instrType = paymentInfo.getInstrumentType().toString();

			if (CollectionConstants.INSTRUMENTTYPE_CASH.equals(instrType)) {
				PaymentInfoCash paymentInfoCash = (PaymentInfoCash) paymentInfo;

				instrumentHeaderList.add(collectionCommon.validateAndConstructCashInstrument(paymentInfoCash));
				otherInstrumenttotal = paymentInfo.getInstrumentAmount();
			}

			
			/* if(CollectionConstants.INSTRUMENTTYPE_CARD.equals(instrType)){
			 PaymentInfoCard paymentInfoCard = (PaymentInfoCard)paymentInfo;
			 
			 instrumentHeaderList.add(
			 collectionCommon.validateAndConstructCardInstrument(
			 paymentInfoCard,receiptHeader));
			 
			 otherInstrumenttotal = paymentInfoCard.getInstrumentAmount(); }*/
			 

			if (CollectionConstants.INSTRUMENTTYPE_BANK.equals(instrType)) {
				PaymentInfoBank paymentInfoBank = (PaymentInfoBank) paymentInfo;

				instrumentHeaderList.add(collectionCommon.validateAndConstructBankInstrument(paymentInfoBank));

				otherInstrumenttotal = paymentInfoBank.getInstrumentAmount();
			}

			if (CollectionConstants.INSTRUMENTTYPE_CHEQUE.equals(instrType) || CollectionConstants.INSTRUMENTTYPE_DD.equals(instrType)) {

				PaymentInfoChequeDD paymentInfoChequeDD = (PaymentInfoChequeDD) paymentInfo;

				instrumentHeaderList.add(collectionCommon.validateAndConstructChequeDDInstrument(paymentInfoChequeDD));

				chequeDDInstrumenttotal = chequeDDInstrumenttotal.add(paymentInfoChequeDD.getInstrumentAmount());
			}
			
			/* if(CollectionConstants.INSTRUMENTTYPE_ATM.equals(instrType)){
			 PaymentInfoATM paymentInfoATM = (PaymentInfoATM)paymentInfo;
			 
			 instrumentHeaderList.add(
			 collectionCommon.validateAndConstructATMInstrument(
			 paymentInfoATM));
			 
			 otherInstrumenttotal = paymentInfoATM.getInstrumentAmount(); }*/
			 

		instrumentHeaderList = receiptHeaderService.createInstrument(instrumentHeaderList);
		LOGGER.info("	Instrument List created	");

		receiptHeader.setReceiptInstrument(new HashSet(instrumentHeaderList));
		 
		BigDecimal debitAmount = BigDecimal.ZERO;

		for (ReceiptDetail receiptDetail : receiptHeader.getReceiptDetails()) {
			debitAmount = debitAmount.add(receiptDetail.getCramount());
			debitAmount = debitAmount.subtract(receiptDetail.getDramount());
		}

		receiptHeader.addReceiptDetail(collectionCommon.addDebitAccountHeadDetails(debitAmount, receiptHeader, chequeDDInstrumenttotal, otherInstrumenttotal, paymentInfo
				.getInstrumentType().toString()));

		receiptHeaderService.persist(receiptHeader);
		receiptHeaderService.getSession().flush();
		LOGGER.info("Receipt Created with receipt number: " + receiptHeader.getReceiptnumber());

		/*try {
			receiptHeaderService.createVoucherForReceipt(receiptHeader, Boolean.FALSE);
			LOGGER.debug("Updated financial systems and created voucher.");
		} catch (EGOVRuntimeException ex) {
			errors.add(new ValidationError("Receipt creation transaction rolled back as update to financial system failed. Payment is in PENDING state.",
					"Receipt creation transaction rolled back as update to financial system failed. Payment is in PENDING state."));
			LOGGER.error("Update to financial systems failed");
		}*/

		collectionCommon.updateBillingSystemWithReceiptInfo(receiptHeader);
		LOGGER.info("Billing system updated with receipt info");

		/*// Create Vouchers
		List<CVoucherHeader> voucherHeaderList = new ArrayList<CVoucherHeader>();

		LOGGER.info("Receipt Voucher created with vouchernumber:	" + receiptHeader.getVoucherNum());

		for (ReceiptVoucher receiptVoucher : receiptHeader.getReceiptVoucher()) {
			voucherHeaderList.add(receiptVoucher.getVoucherheader());
		}*/

		/*if (voucherHeaderList != null && !instrumentHeaderList.isEmpty()) {
			receiptHeaderService.updateInstrument(voucherHeaderList, instrumentHeaderList);
		}*/
		LOGGER.info("Logs For HandHeldDevice Permance Test : Receipt Creation Finished....");
		return new BillReceiptInfoImpl(receiptHeader);
	}

	/*
	 (non-Javadoc)
	 
	 @seeorg.egov.infstr.collections.integration.ICollectionInterface#
	 getPendingReceiptsInfo(java.lang.String, java.lang.String)
	 */
	@Override
	public List<BillReceiptInfo> getOnlinePendingReceipts(String serviceCode, String consumerCode) {
		ArrayList<BillReceiptInfo> receipts = new ArrayList<BillReceiptInfo>();
		List<ReceiptHeader> receiptHeaders = (List<ReceiptHeader>) findAllByNamedQuery(CollectionConstants.QUERY_ONLINE_PENDING_RECEIPTS_BY_CONSUMERCODE_AND_SERVICECODE,
				serviceCode, consumerCode, CollectionConstants.ONLINEPAYMENT_STATUS_CODE_PENDING);
		if (receiptHeaders == null || receiptHeaders.isEmpty()) {
			return null;
		} else {
			for (ReceiptHeader receiptHeader : receiptHeaders) {
				receipts.add(new BillReceiptInfoImpl(receiptHeader));
			}
			return receipts;
		}

	}

	/*
	 
	 @see
	 org.egov.infstr.collections.integration.CollectionIntegrationService#
	 createMiscellaneousReceipt (BillInfo bill, List<PaymentInfo>
	 paymentInfoList)
	 */
	@Override
	public BillReceiptInfo createMiscellaneousReceipt(BillInfo bill, PaymentInfo paymentInfo) {
		LOGGER.info("Logs For Miscellaneous Receipt : Receipt Creation Started....");
		Fund fund = commonsServiceImpl.fundByCode(bill.getFundCode());
		if (fund == null) {
			throw new EGOVRuntimeException("Fund not present for the fund code [" + bill.getFundCode() + "].");
		}

		Department dept = (Department) persistenceService.findByNamedQuery(CollectionConstants.QUERY_DEPARTMENT_BY_CODE, bill.getDepartmentCode());

		if (dept == null) {
			throw new EGOVRuntimeException("Department not present for the department code [" + bill.getDepartmentCode() + "].");
		}

		ReceiptHeader receiptHeader = collectionCommon.initialiseReceiptModelWithBillInfo(bill, fund, dept);

		receiptHeader.setCreatedDate(new Date());
		receiptHeader.setReceipttype(CollectionConstants.RECEIPT_TYPE_ADHOC);
		receiptHeader.setIsModifiable(Boolean.TRUE);
		receiptHeader.setIsReconciled(Boolean.TRUE);
		receiptHeader.setCollectiontype(CollectionConstants.COLLECTION_TYPE_COUNTER);

		receiptHeader.setStatus(collectionsUtil.getEgwStatusForModuleAndCode(CollectionConstants.MODULE_NAME_RECEIPTHEADER, CollectionConstants.RECEIPT_STATUS_CODE_APPROVED));

		receiptHeader.setPaidBy(bill.getPaidBy());

		if (EgovThreadLocals.getUserId() != null) {
			receiptHeader.setCreatedBy(collectionsUtil.getUserById(EgovThreadLocals.getUserId()));
			Location location = collectionsUtil.getLocationByUser(EgovThreadLocals.getUserId());
			if (location != null) {
				receiptHeader.setLocation(location);
			}
		}

		BigDecimal chequeDDInstrumenttotal = BigDecimal.ZERO;
		BigDecimal otherInstrumenttotal = BigDecimal.ZERO;

		/*// populate instrument details
		List<InstrumentHeader> instrumentHeaderList = new ArrayList<InstrumentHeader>();

		for (PaymentInfo paymentInfo : paymentInfoList) {
			String instrType = paymentInfo.getInstrumentType().toString();

			if (CollectionConstants.INSTRUMENTTYPE_CASH.equals(instrType)) {
				PaymentInfoCash paymentInfoCash = (PaymentInfoCash) paymentInfo;

				instrumentHeaderList.add(collectionCommon.validateAndConstructCashInstrument(paymentInfoCash));
				otherInstrumenttotal = paymentInfo.getInstrumentAmount();
			}

			if (CollectionConstants.INSTRUMENTTYPE_BANK.equals(instrType)) {
				PaymentInfoBank paymentInfoBank = (PaymentInfoBank) paymentInfo;

				instrumentHeaderList.add(collectionCommon.validateAndConstructBankInstrument(paymentInfoBank));

				otherInstrumenttotal = paymentInfoBank.getInstrumentAmount();
			}

			if (CollectionConstants.INSTRUMENTTYPE_CHEQUE.equals(instrType) || CollectionConstants.INSTRUMENTTYPE_DD.equals(instrType)) {

				PaymentInfoChequeDD paymentInfoChequeDD = (PaymentInfoChequeDD) paymentInfo;

				instrumentHeaderList.add(collectionCommon.validateAndConstructChequeDDInstrument(paymentInfoChequeDD));

				chequeDDInstrumenttotal = chequeDDInstrumenttotal.add(paymentInfoChequeDD.getInstrumentAmount());
			}
		}

		instrumentHeaderList = receiptHeaderService.createInstrument(instrumentHeaderList);
		LOGGER.info("	Instrument List created	");

		receiptHeader.setReceiptInstrument(new HashSet(instrumentHeaderList));*/

		BigDecimal debitAmount = BigDecimal.ZERO;

		for (ReceiptDetail receiptDetail : receiptHeader.getReceiptDetails()) {
			debitAmount = debitAmount.add(receiptDetail.getCramount());
			debitAmount = debitAmount.subtract(receiptDetail.getDramount());
		}

		receiptHeader.addReceiptDetail(collectionCommon.addDebitAccountHeadDetails(debitAmount, receiptHeader, chequeDDInstrumenttotal, otherInstrumenttotal, paymentInfo
				.getInstrumentType().toString()));

		receiptHeaderService.persist(receiptHeader);
		receiptHeaderService.getSession().flush();
		LOGGER.info("Miscellaneous Receipt Created with receipt number: " + receiptHeader.getReceiptnumber());

		try {
			receiptHeaderService.createVoucherForReceipt(receiptHeader, Boolean.FALSE);
			LOGGER.debug("Updated financial systems and created voucher.");
		} catch (EGOVRuntimeException ex) {
			errors.add(new ValidationError("Miscellaneous Receipt creation transaction rolled back as update to financial system failed.",
					"Miscellaneous Receipt creation transaction rolled back as update to financial system failed."));
			LOGGER.error("Update to financial systems failed");
		}

		// Create Vouchers
		List<CVoucherHeader> voucherHeaderList = new ArrayList<CVoucherHeader>();

		LOGGER.info("Receipt Voucher created with vouchernumber:	" + receiptHeader.getVoucherNum());

		for (ReceiptVoucher receiptVoucher : receiptHeader.getReceiptVoucher()) {
			voucherHeaderList.add(receiptVoucher.getVoucherheader());
		}

	/*	if (voucherHeaderList != null && !instrumentHeaderList.isEmpty()) {
			receiptHeaderService.updateInstrument(voucherHeaderList, instrumentHeaderList);
		}*/
		LOGGER.info("Logs For HandHeldDevice Permance Test : Receipt Creation Finished....");
		return new BillReceiptInfoImpl(receiptHeader);
	}

}
