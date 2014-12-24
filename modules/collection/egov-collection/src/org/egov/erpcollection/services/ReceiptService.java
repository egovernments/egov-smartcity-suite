package org.egov.erpcollection.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CVoucherHeader;
import org.egov.erpcollection.integration.models.BillReceiptInfo;
import org.egov.erpcollection.integration.services.BillingIntegrationService;
import org.egov.erpcollection.models.Challan;
import org.egov.erpcollection.models.ReceiptHeader;
import org.egov.erpcollection.models.ReceiptPayeeDetails;
import org.egov.erpcollection.models.ReceiptVoucher;
import org.egov.erpcollection.util.CollectionsNumberGenerator;
import org.egov.erpcollection.util.CollectionsUtil;
import org.egov.erpcollection.util.FinancialsUtil;
import org.egov.erpcollection.web.constants.CollectionConstants;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.EISServeable.DATE_ORDER;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.pims.service.EisUtilService;

public class ReceiptService extends
		PersistenceService<ReceiptPayeeDetails, Long> {

	private static final Logger LOGGER = Logger.getLogger(ReceiptService.class);

	/**
	 * An instance of <code>CollectionsNumberGenerator</code>
	 */
	private CollectionsNumberGenerator collectionsNumberGenerator;

	private FinancialsUtil financialsUtil;
	private CollectionsUtil collectionsUtil;
	private EisUtilService eisService;

	public ReceiptService() {
		setType(ReceiptPayeeDetails.class);
	}

	/**
	 * This method persists the given <code>ReceiptPayeeDetails</code> entity. 
	 * 
	 * The receipt number for all of the receipts is generated, 
	 * if not already present.
	 * 
	 * If the receipt is associated with a challan, and the challan number is 
	 * not present, the challan number is generated and set into it.
	 */
	public ReceiptPayeeDetails persist(ReceiptPayeeDetails entity) {
		for (ReceiptHeader receiptHeader : entity.getReceiptHeaders()) {
			if (receiptHeader.getReceipttype()!=CollectionConstants.RECEIPT_TYPE_CHALLAN && 
					!CollectionConstants.RECEIPT_STATUS_CODE_PENDING.equals(
							receiptHeader.getStatus().getCode()) && receiptHeader.getReceiptnumber()==null){
				setReceiptNumber(receiptHeader);
			}
			
			if(receiptHeader.getChallan() != null){
				Challan challan = receiptHeader.getChallan();
				if (challan.getChallanNumber() == null) {
					setChallanNumber(challan);
				}
			
				receiptHeader.setChallan(challan);
				LOGGER.info("Persisted challan with challan number " + 
						challan.getChallanNumber());
			}
		}
		return super.persist(entity);
	}
	
	/**
	 * This method persists the given <code>ReceiptPayeeDetails</code> entity. If the receipt
	 * number for all of the receipts is generated, if not already present.
	 */
	public ReceiptPayeeDetails persistChallan(ReceiptPayeeDetails entity) {
		for (ReceiptHeader receiptHeader : entity.getReceiptHeaders()) {
			Integer validUpto = Integer.valueOf(collectionsUtil.getAppConfigValue(
					CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
					CollectionConstants.APPCONFIG_VALUE_CHALLANVALIDUPTO));
			
			Challan challan = receiptHeader.getChallan();
			
			challan.setValidUpto(eisService.getPriorOrAfterWorkingDate(
					challan.getChallanDate(), validUpto, DATE_ORDER.AFTER));
		
			if(challan.getCreatedDate()==null)
				challan.setCreatedDate(new Date());
			
			if (challan.getChallanNumber() == null) {
				setChallanNumber(challan);
			}
			
			challan.setReceiptHeader(receiptHeader);
			receiptHeader.setChallan(challan);
			
			LOGGER.info("Persisting challan with challan number " + challan.getChallanNumber());
		}
		
		return super.persist(entity);
	}

	/**
	 * This method persists the given set of <code>ReceiptPayeeDetails</code>
	 * instances
	 * 
	 * @param entity
	 *            a set of <code>ReceiptPayeeDetails</code> instances to be
	 *            persisted
	 * 
	 * @return the list of persisted <code>ReceiptPayeeDetails</code> instances
	 */
	public List<ReceiptPayeeDetails> persist(Set<ReceiptPayeeDetails> entity) {
		List<ReceiptPayeeDetails> saved = new ArrayList<ReceiptPayeeDetails>();
		Iterator iterator = entity.iterator();
		while (iterator.hasNext()) {
			ReceiptPayeeDetails rpd = (ReceiptPayeeDetails) iterator.next();
			saved.add(this.persist(rpd));
		}
		return saved;
	}
	
	/**
	 This method persists the given set of <code>ReceiptPayeeDetails</code> 
	 * instances with receipt number as Pending
	 * 
	 * @param entity
	 *            a set of <code>ReceiptPayeeDetails</code> instances to be
	 *            persisted
	 * 
	 * @return the list of persisted <code>ReceiptPayeeDetails</code> instances
	 * 
	 */
	public List<ReceiptPayeeDetails> persistPendingReceipts(Set<ReceiptPayeeDetails> entity) {
		List<ReceiptPayeeDetails> saved = new ArrayList<ReceiptPayeeDetails>();
		Iterator iterator = entity.iterator();
		while (iterator.hasNext()) {
			ReceiptPayeeDetails rpd = (ReceiptPayeeDetails) iterator.next();
			//for (ReceiptHeader receiptHeader : rpd.getReceiptHeaders()) {
				//if (receiptHeader.getReceiptnumber() == null) {
				//	receiptHeader.setReceiptnumber(CollectionConstants.RECEIPTNUMBER_PENDING);
				//}
			//}	
			saved.add(super.persist(rpd));
		}
		return saved;
	}
	

	public void setReceiptNumber(ReceiptHeader entity) {
		entity.setReceiptnumber(collectionsNumberGenerator
				.generateReceiptNumber(entity));
	}
	
	private void setChallanNumber(Challan challan) {
		CFinancialYear financialYear = collectionsUtil.
			getFinancialYearforDate(challan.getCreatedDate());
		challan.setChallanNumber(collectionsNumberGenerator
			.generateChallanNumber(challan, financialYear));
	}

	public void setCollectionsNumberGenerator(
			CollectionsNumberGenerator collectionsNumberGenerator) {
		this.collectionsNumberGenerator = collectionsNumberGenerator;
	}

	private BillingIntegrationService getBillingServiceBean(String serviceCode) {
		return (BillingIntegrationService) collectionsUtil.getBean(serviceCode
						+ CollectionConstants.COLLECTIONS_INTERFACE_SUFFIX);
	}

	/**
	 * This method looks up the bean to communicate with the billing system and
	 * updates the billing system.
	 */
	public Boolean updateBillingSystem(String serviceCode,
			Set<BillReceiptInfo> billReceipts) {
		BillingIntegrationService billingService = getBillingServiceBean(serviceCode);
		if (billingService == null) {
			return false;
		} else {
			try {
				return billingService.updateReceiptDetails(billReceipts);
			} catch (Exception e) {
				String errMsg = "Exception while updating billing system ["
					+ serviceCode + "] with receipt details!";
				LOGGER.error(errMsg, e);
				throw new EGOVRuntimeException(errMsg, e);
			}
		}
	}

	/**
	 * This method is called for voucher reversal in case of intra-day receipt
	 * cancellation.
	 * 
	 */
	public void createReversalVoucher(ReceiptVoucher receiptVoucher,
			String instrumentType) {
		List<HashMap<String, Object>> reversalVoucherInfoList = new ArrayList();
		HashMap<String, Object> reversalVoucherInfo = new HashMap();
		
		if(receiptVoucher.getVoucherheader()!=null)
		{	
			reversalVoucherInfo.put(CollectionConstants.FINANCIALS_VOUCHERREVERSAL_ORIGINALVOUCHERID, receiptVoucher
					.getVoucherheader().getId());
			reversalVoucherInfo.put(CollectionConstants.FINANCIALS_VOUCHERREVERSAL_DATE, new Date());
			
			if(receiptVoucher.getVoucherheader().getType().equals(CollectionConstants.FINANCIAL_JOURNALVOUCHER_VOUCHERTYPE)){
				
				reversalVoucherInfo.put(CollectionConstants.FINANCIALS_VOUCHERREVERSAL_TYPE,
						CollectionConstants.FINANCIAL_JOURNALVOUCHER_VOUCHERTYPE);
				reversalVoucherInfo.put(CollectionConstants.FINANCIALS_VOUCHERREVERSAL_NAME,
						CollectionConstants.FINANCIAL_JOURNALVOUCHER_VOUCHERNAME);
			}
			else if(receiptVoucher.getVoucherheader().getType().equals(CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERTYPE)){
				reversalVoucherInfo.put(CollectionConstants.FINANCIALS_VOUCHERREVERSAL_TYPE,
						CollectionConstants.FINANCIAL_PAYMENTVOUCHER_VOUCHERTYPE);
				reversalVoucherInfo.put(CollectionConstants.FINANCIALS_VOUCHERREVERSAL_NAME,
						CollectionConstants.FINANCIAL_PAYMENTVOUCHER_VOUCHERNAME);
			}
		}	
				
		reversalVoucherInfoList.add(reversalVoucherInfo);

		try {
			financialsUtil.getReversalVoucher(reversalVoucherInfoList);
		} catch (Exception e) {
			LOGGER.error("Receipt Service Exception while creating reversal voucher!",e);
			// TODO: Throw EGovRuntimeException ?
		}
	}

	/**
	 * @ Create instrument voucher list from receiptpayeelist and pass it to
	 * financialsutil
	 * 
	 * @param receiptPayeeDetails
	 * @return void
	 */
	public void updateInstrument(List<CVoucherHeader> voucherHeaderList,
			List<InstrumentHeader> instrumentHeaderList) {
		List<Map<String, Object>> instrumentVoucherList = new ArrayList();
		if (voucherHeaderList != null && instrumentHeaderList != null) {
			for (CVoucherHeader voucherHeader : voucherHeaderList) {
				for (InstrumentHeader instrumentHeader : instrumentHeaderList) {
					Map<String, Object> iVoucherMap = new HashMap();
					iVoucherMap
							.put(
									CollectionConstants.FINANCIAL_INSTRUMENTSERVICE_INSTRUMENTHEADEROBJECT,
									instrumentHeader);
					iVoucherMap
							.put(
									CollectionConstants.FINANCIAL_INSTRUMENTSERVICE_VOUCHERHEADEROBJECT,
									voucherHeader);
					instrumentVoucherList.add(iVoucherMap);
				}
			}
			financialsUtil.updateInstrument(instrumentVoucherList);
		}
	}
	
	public List<InstrumentHeader> createInstrument(
			List<InstrumentHeader> instrumentHeaderList) {
		List<Map<String, Object>> instrumentHeaderMapList = new ArrayList();
		if (instrumentHeaderList != null) {
			for (InstrumentHeader instrumentHeader : instrumentHeaderList) {
				Map<String, Object> instrumentHeaderMap = new HashMap();
				instrumentHeaderMap.put(
						CollectionConstants.MAP_KEY_INSTRSERVICE_INSTRUMENTNUMBER, 
						instrumentHeader.getInstrumentNumber());
				instrumentHeaderMap.put(
						CollectionConstants.MAP_KEY_INSTRSERVICE_INSTRUMENTDATE, 
						instrumentHeader.getInstrumentDate());
				instrumentHeaderMap.put(
						CollectionConstants.MAP_KEY_INSTRSERVICE_INSTRUMENTAMOUNT, 
						instrumentHeader.getInstrumentAmount());
				instrumentHeaderMap.put(
						CollectionConstants.MAP_KEY_INSTRSERVICE_INSTRUMENTTYPE, 
						instrumentHeader.getInstrumentType().getType());
				instrumentHeaderMap.put(
						CollectionConstants.MAP_KEY_INSTRSERVICE_ISPAYCHEQUE, 
						instrumentHeader.getIsPayCheque());
				if(instrumentHeader.getBankId()!=null)
					instrumentHeaderMap.put(
							CollectionConstants.MAP_KEY_INSTRSERVICE_BANKCODE, 
							instrumentHeader.getBankId().getCode());
				instrumentHeaderMap.put(
						CollectionConstants.MAP_KEY_INSTRSERVICE_BANKBRANCHNAME, 
						instrumentHeader.getBankBranchName());
				instrumentHeaderMap.put(
						CollectionConstants.MAP_KEY_INSTRSERVICE_TRANSACTIONNUMBER, 
						instrumentHeader.getTransactionNumber());
				instrumentHeaderMap.put(
						CollectionConstants.MAP_KEY_INSTRSERVICE_TRANSACTIONDATE, 
						instrumentHeader.getTransactionDate());
				if(instrumentHeader.getBankAccountId()!=null){
					instrumentHeaderMap.put(
							CollectionConstants.MAP_KEY_INSTRSERVICE_BANKACCOUNTID, 
							instrumentHeader.getBankAccountId().getId());
				}
				instrumentHeaderMapList.add(instrumentHeaderMap);
				// should add bankaccount for bank : key = Bank account id; value = instrumentHeader.getBankAccount.getId()
			}
		}
		return financialsUtil.createInstrument(instrumentHeaderMapList);
	}

	/**
	 * @param financialsUtil
	 *            the financialsUtil to set
	 */
	public void setFinancialsUtil(FinancialsUtil financialsUtil) {
		this.financialsUtil = financialsUtil;
	}

	/**
	 * @param collectionsUtil the collectionsUtil to set
	 */
	public void setCollectionsUtil(CollectionsUtil collectionsUtil) {
		this.collectionsUtil = collectionsUtil;
	}

	public void setEisService(EisUtilService eisService) {
		this.eisService = eisService;
	}

}