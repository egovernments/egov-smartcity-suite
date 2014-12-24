/**
 * 
 */
package org.egov.erpcollection.integration.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.Fund;
import org.egov.commons.service.CommonsService;
import org.egov.erpcollection.integration.models.BillInfo;
import org.egov.erpcollection.integration.models.BillReceiptInfo;
import org.egov.erpcollection.integration.models.BillReceiptInfoImpl;
import org.egov.erpcollection.integration.models.PaymentInfo;
import org.egov.erpcollection.integration.models.PaymentInfoBank;
import org.egov.erpcollection.integration.models.PaymentInfoCash;
import org.egov.erpcollection.integration.models.PaymentInfoChequeDD;
import org.egov.erpcollection.models.ReceiptDetail;
import org.egov.erpcollection.models.ReceiptHeader;
import org.egov.erpcollection.models.ReceiptPayeeDetails;
import org.egov.erpcollection.models.ReceiptVoucher;
import org.egov.erpcollection.services.ReceiptHeaderService;
import org.egov.erpcollection.services.ReceiptService;
import org.egov.erpcollection.util.CollectionCommon;
import org.egov.erpcollection.util.CollectionsUtil;
import org.egov.erpcollection.web.constants.CollectionConstants;
import org.egov.infstr.services.PersistenceService;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.model.instrument.InstrumentHeader;

/**
 * Collections integration service implementation - exposes APIs that can be used by other
 * applications (typically billing systems) to interact with the collections
 * module.
 */
public class CollectionIntegrationServiceImpl extends
		PersistenceService<ReceiptHeader, Long> implements
		CollectionIntegrationService {

	private static final Logger LOGGER = Logger.getLogger(
			CollectionIntegrationServiceImpl.class);
	
	private CommonsService commonsService;
	
	private PersistenceService persistenceService;
	
	private CollectionsUtil collectionsUtil;
	
	private ReceiptService receiptPayeeDetailsService;
	
	private ReceiptHeaderService receiptHeaderService;
	
	public void setReceiptHeaderService(ReceiptHeaderService receiptHeaderService) {
		this.receiptHeaderService = receiptHeaderService;
	}

	public void setReceiptPayeeDetailsService(
			ReceiptService receiptPayeeDetailsService) {
		this.receiptPayeeDetailsService = receiptPayeeDetailsService;
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

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.egov.infstr.collections.integration.ICollectionInterface#
	 * getBillReceiptInfo(java.lang.String, java.lang.String)
	 */
	@Override
	public List<BillReceiptInfo> getBillReceiptInfo(String serviceCode,String refNum) {
		ArrayList<BillReceiptInfo> receipts = new ArrayList<BillReceiptInfo>();

		// Get all receipt headers for given reference number
		List<ReceiptHeader> receiptHeaders = findAllByNamedQuery(
				CollectionConstants.QUERY_RECEIPTS_BY_REFNUM_AND_SERVICECODE, 
				refNum, serviceCode);
		if (receiptHeaders == null || receiptHeaders.isEmpty()) {
			return null;
		}

		for (ReceiptHeader receiptHeader : receiptHeaders) {
			receipts.add(new BillReceiptInfoImpl(receiptHeader));
		}
		return receipts;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.egov.infstr.collections.integration.ICollectionInterface#
	 * getBillReceiptInfo(java.lang.String, java.util.Set)
	 */
	@Override
	public Map<String, List<BillReceiptInfo>> getBillReceiptInfo(
			String serviceCode,Set<String> refNums) {
		HashMap<String, List<BillReceiptInfo>> receipts = new HashMap<String, List<BillReceiptInfo>>();

		for (String refNum : refNums) {
			receipts.put(refNum, getBillReceiptInfo(serviceCode,refNum));
		}

		return receipts;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.egov.infstr.collections.integration.ICollectionInterface#
	 * getInstrumentReceiptInfo(java.lang.String, java.lang.String)
	 */
	@Override
	public List<BillReceiptInfo> getInstrumentReceiptInfo(String serviceCode,String instrumentNum) {
		ArrayList<BillReceiptInfo> receipts = new ArrayList<BillReceiptInfo>();

		List<ReceiptHeader> receiptHeaders = (List<ReceiptHeader>) findAllByNamedQuery(
				CollectionConstants.QUERY_RECEIPTS_BY_INSTRUMENTNO_AND_SERVICECODE, instrumentNum,serviceCode);
		if (receiptHeaders == null || receiptHeaders.isEmpty()) {
			return null;
		}

		for (ReceiptHeader receiptHeader : receiptHeaders) {
			receipts.add(new BillReceiptInfoImpl(receiptHeader));
		}
		return receipts;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.egov.infstr.collections.integration.ICollectionInterface#
	 * getInstrumentReceiptInfo(java.lang.String, java.util.Set)
	 */
	@Override
	public Map<String, List<BillReceiptInfo>> getInstrumentReceiptInfo(
			String serviceCode,Set<String> instrumentNums) {
		HashMap<String, List<BillReceiptInfo>> receipts = new HashMap<String, List<BillReceiptInfo>>();

		for (String instrumentNum : instrumentNums) {
			receipts
					.put(instrumentNum, getInstrumentReceiptInfo(serviceCode,instrumentNum));
		}

		return receipts;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.egov.infstr.collections.integration.CollectionIntegrationService#
	 * getReceiptInfo (java.lang.String, java.lang.String)
	 */
	@Override
	public BillReceiptInfo getReceiptInfo(String serviceCode,String receiptNum) {
		ReceiptHeader receiptHeader = (ReceiptHeader) findByNamedQuery(
				CollectionConstants.QUERY_RECEIPT_BY_RECEIPTNUM_AND_SERVICECODE, serviceCode, receiptNum, receiptNum);
		if (receiptHeader == null) {
			return null;
		} else {
			// Create bill receipt info
			BillReceiptInfoImpl receiptInfo = new BillReceiptInfoImpl(receiptHeader);

			return receiptInfo;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.egov.infstr.collections.integration.CollectionIntegrationService#
	 * getReceiptInfo (java.lang.String, java.util.Set)
	 */
	@Override
	public Map<String, BillReceiptInfo> getReceiptInfo(
			String serviceCode,Set<String> receiptNums) {
		HashMap<String, BillReceiptInfo> receipts = new HashMap<String, BillReceiptInfo>();

		for (String receiptNum : receiptNums) {
			receipts.put(receiptNum, getReceiptInfo(serviceCode,receiptNum));
		}

		return receipts;
	}
	
	/*
	 * 
	 * @see
	 * org.egov.infstr.collections.integration.CollectionIntegrationService#
	 * createReceipt (BillInfo bill, 
			List<PaymentInfo> paymentInfoList)
	 */
	@Override
	public BillReceiptInfo createReceipt(BillInfo bill, 
			List<PaymentInfo> paymentInfoList) { 
		LOGGER.info("Logs For HandHeldDevice Permance Test : Receipt Creation Started....");
		Fund fund = commonsService
				.fundByCode(bill.getFundCode());
		if (fund == null) {
			throw new EGOVRuntimeException("Fund not present for the fund code ["+
					bill.getFundCode()+"].");
		}
		
		DepartmentImpl dept = (DepartmentImpl) persistenceService.findByNamedQuery(
						CollectionConstants.QUERY_DEPARTMENT_BY_CODE,
						bill.getDepartmentCode());
		
		if (dept == null) {
			throw new EGOVRuntimeException("Department not present for the department code ["+
					bill.getDepartmentCode()+"].");
		}
		
		ReceiptPayeeDetails receiptPayee = collectionCommon
				.initialiseReceiptModelWithBillInfo(bill, fund,
						dept).get(0);
		ReceiptHeader receiptHeader = receiptPayee.getReceiptHeaders().iterator().next();
		
		receiptHeader.setCreatedDate(new Date());
		receiptHeader.setReceipttype(CollectionConstants.RECEIPT_TYPE_BILL);
		receiptHeader.setIsModifiable(Boolean.TRUE);
		receiptHeader.setIsReconciled(Boolean.FALSE);
		receiptHeader.setCollectiontype(CollectionConstants.COLLECTION_TYPE_FIELDCOLLECTION); 
		
		/*receiptHeader.setLocation(collectionsUtil
				.getLocationOfUser(collectionsUtil
						.getLoggedInUserName(getSession())));*/
		
		receiptHeader.setStatus(collectionsUtil.getEgwStatusForModuleAndCode(
						CollectionConstants.MODULE_NAME_RECEIPTHEADER,
						CollectionConstants.RECEIPT_STATUS_CODE_APPROVED));
		
		receiptHeader.setPaidBy(bill.getPaidBy());
		
		BigDecimal chequeDDInstrumenttotal = BigDecimal.ZERO;
		BigDecimal otherInstrumenttotal = BigDecimal.ZERO;
		
		// populate instrument details
		List<InstrumentHeader> instrumentHeaderList = new ArrayList<InstrumentHeader>();
		
		for(PaymentInfo paytInfo : paymentInfoList){
			String instrType = paytInfo.getInstrumentType().toString();
			
			if(CollectionConstants.INSTRUMENTTYPE_CASH.equals(instrType)){
				PaymentInfoCash paytInfoCash = (PaymentInfoCash)paytInfo;
				
				instrumentHeaderList.add(
						collectionCommon.validateAndConstructCashInstrument(
								paytInfoCash));
				otherInstrumenttotal = paytInfo.getInstrumentAmount();
			}
			
			/*if(CollectionConstants.INSTRUMENTTYPE_CARD.equals(instrType)){
				PaymentInfoCard paytInfoCard = (PaymentInfoCard)paytInfo;
				
				instrumentHeaderList.add(
						collectionCommon.validateAndConstructCardInstrument(
								paytInfoCard,receiptHeader));
				
				otherInstrumenttotal = paytInfoCard.getInstrumentAmount();
			}*/
			
			if(CollectionConstants.INSTRUMENTTYPE_BANK.equals(instrType)){
				PaymentInfoBank paytInfoBank = (PaymentInfoBank)paytInfo;
				
				instrumentHeaderList.add(
						collectionCommon.validateAndConstructBankInstrument(
								paytInfoBank));
				
				otherInstrumenttotal = paytInfoBank.getInstrumentAmount();
			}
			
			if(CollectionConstants.INSTRUMENTTYPE_CHEQUE.equals(instrType) ||
					CollectionConstants.INSTRUMENTTYPE_DD.equals(instrType)){
				
				PaymentInfoChequeDD paytInfoChequeDD = (PaymentInfoChequeDD)paytInfo;
				
				instrumentHeaderList.add(collectionCommon.
						validateAndConstructChequeDDInstrument(
								paytInfoChequeDD));
				
				chequeDDInstrumenttotal = chequeDDInstrumenttotal.add(
						paytInfoChequeDD.getInstrumentAmount());
			}
			/*if(CollectionConstants.INSTRUMENTTYPE_ATM.equals(instrType)){
				PaymentInfoATM paytInfoATM = (PaymentInfoATM)paytInfo;
				
				instrumentHeaderList.add(
						collectionCommon.validateAndConstructATMInstrument(
								paytInfoATM));
				
				otherInstrumenttotal = paytInfoATM.getInstrumentAmount();
			}*/
		}
		
		instrumentHeaderList = receiptPayeeDetailsService.createInstrument(instrumentHeaderList);
		LOGGER.info("	Instrument List created	");
		
		receiptHeader.setReceiptInstrument(new HashSet(instrumentHeaderList));
		
		BigDecimal debitAmount = BigDecimal.ZERO;
		
		for (ReceiptDetail receiptDetail : receiptHeader.getReceiptDetails()) {
			debitAmount = debitAmount.add(receiptDetail.getCramount());
			debitAmount = debitAmount.subtract(receiptDetail.getDramount());
		}
		
		receiptHeader.addReceiptDetail(collectionCommon
				.addDebitAccountHeadDetails(debitAmount,
						receiptHeader, chequeDDInstrumenttotal, 
						otherInstrumenttotal, 
						paymentInfoList.get(0).getInstrumentType().toString()));
		
		receiptPayeeDetailsService.persist(receiptHeader.getReceiptPayeeDetails());
		receiptPayeeDetailsService.getSession().flush();
		LOGGER.info("Receipt Created with receipt number: "+receiptHeader.getReceiptnumber());
		List<ReceiptHeader> receiptHeaderList = new ArrayList<ReceiptHeader>();
		receiptHeaderList.add(receiptHeader);
		
		//update billing system
		receiptHeaderService.startWorkflow(receiptHeaderList,Boolean.TRUE);
		LOGGER.info("Workflow started for newly created receipts");
		
		collectionCommon.updateBillingSystemWithReceiptInfo(receiptHeader.getReceiptPayeeDetails());
		LOGGER.info("Billing system updated with receipt info");
		
		//Create Vouchers
		List<CVoucherHeader> voucherHeaderList = new ArrayList<CVoucherHeader>();
		
		LOGGER.info("Receipt Voucher created with vouchernumber:	"+receiptHeader.getVoucherNum());
		
		for (ReceiptVoucher receiptVoucher : receiptHeader.getReceiptVoucher()) {
				voucherHeaderList.add(receiptVoucher.getVoucherheader());
		}
		
		if (voucherHeaderList != null && !instrumentHeaderList.isEmpty()) {
			receiptPayeeDetailsService.updateInstrument(voucherHeaderList,
					instrumentHeaderList);
		}
		LOGGER.info("Logs For HandHeldDevice Permance Test : Receipt Creation Finished....");
		return new BillReceiptInfoImpl(receiptHeader);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.egov.infstr.collections.integration.ICollectionInterface#
	 * getPendingReceiptsInfo(java.lang.String, java.lang.String)
	 */
	@Override
	public List<BillReceiptInfo> getOnlinePendingReceipts(String serviceCode,String consumerCode) {
		ArrayList<BillReceiptInfo> receipts = new ArrayList<BillReceiptInfo>();
		List<ReceiptHeader> receiptHeaders = (List<ReceiptHeader>) findAllByNamedQuery(
				CollectionConstants.QUERY_ONLINE_PENDING_RECEIPTS_BY_CONSUMERCODE_AND_SERVICECODE, serviceCode, consumerCode, CollectionConstants.ONLINEPAYMENT_STATUS_CODE_PENDING);
		if (receiptHeaders == null || receiptHeaders.isEmpty()) {
			return null;
		}
		else {
			for (ReceiptHeader receiptHeader : receiptHeaders) {
			receipts.add(new BillReceiptInfoImpl(receiptHeader));
			}
			return receipts;
		}
		
	}
}
