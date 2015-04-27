/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.collection.web.actions.receipts;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.CollectionStgAccAmount;
import org.egov.collection.entity.CollectionStgInstrument;
import org.egov.collection.entity.CollectionStgReceipt;
import org.egov.collection.entity.ReceiptDetail;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.collection.entity.ReceiptMisc;
import org.egov.collection.service.ReceiptHeaderService;
import org.egov.collection.utils.CollectionCommon;
import org.egov.collection.utils.CollectionsUtil;
import org.egov.collection.utils.FinancialsUtil;
import org.egov.commons.Bank;
import org.egov.commons.Fund;
import org.egov.commons.service.CommonsServiceImpl;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infstr.models.ServiceDetails;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.services.receipt.ReceiptService;
import org.egov.web.actions.BaseFormAction;

@ParentPackage("egov")  
public class HistoricDataUploadAction extends BaseFormAction{  
	private static final Logger LOGGER = Logger.getLogger(MiscellaneousFileUploadAction.class);
	    
    
    
	/**
	 * A <code>List</code> of <code>ReceiptPayeeDetails</code> representing the
	 * model for the action.
	 */
	//private List<ReceiptPayeeDetails> modelPayeeList = new ArrayList<ReceiptPayeeDetails>();
	
	 
    public void setReceiptPayeeDetailsService(
			ReceiptService receiptPayeeDetailsService) {
		this.receiptPayeeDetailsService = receiptPayeeDetailsService;
	}

	private ReceiptAction receiptAction;
    
    private CollectionCommon collectionCommon;
    private FinancialsUtil financialsUtil;
    private CollectionsUtil collectionsUtil;
    
    private CommonsServiceImpl commonsServiceImpl;
    
    private ReceiptHeaderService receiptHeaderService;
    private ReceiptService receiptPayeeDetailsService;
    private List<ReceiptDetail> receiptDetailList = new ArrayList<ReceiptDetail>();
    private String instrumentTypeCashOrCard;
    private PersistenceService<CollectionStgReceipt,Long> collStgReceiptService;  
    
    
    
	private void injectObjectsIntoAction(){
		receiptAction=new ReceiptAction();

		receiptAction.setSession(getSession());
		receiptAction.setPersistenceService(getPersistenceService());
		
		receiptAction.setCollectionCommon(collectionCommon);
		receiptAction.setCollectionsUtil(collectionsUtil);
		//receiptAction.setCommonsManager(commonsServiceImpl);
		receiptAction.setFinancialsUtil(financialsUtil);
		
		receiptAction.setReceiptHeaderService(receiptHeaderService);
		receiptAction.setReceiptPayeeDetailsService(receiptPayeeDetailsService);
		receiptAction.setReceiptBulkUpload(Boolean.TRUE);
	}
	
	private void initialiseObjectsInAction(CollectionStgReceipt collectionStgReceiptObj){
		//modelPayeeList.clear();
		receiptAction.setPayeename(collectionStgReceiptObj.getPaidBy());
		receiptAction.setPaidBy(collectionStgReceiptObj.getPaidBy());
		receiptAction.setManualReceiptDate(collectionStgReceiptObj.getRcptDate());
		receiptAction.setVoucherDate(collectionStgReceiptObj.getRcptDate());
		
		//modelPayeeList.add(createReceiptPayee(collectionStgReceiptObj));
		
		receiptAction.setReceiptDetailList(receiptDetailList);
		receiptAction.setInstrumentTypeCashOrCard(instrumentTypeCashOrCard);
	}
	
	/*private ReceiptPayeeDetails createReceiptPayee(CollectionStgReceipt collectionStgReceiptObj) {
		ReceiptPayeeDetails receiptPayee = new ReceiptPayeeDetails();
		receiptPayee.setPayeename(collectionStgReceiptObj.getPaidBy());
		receiptPayee.addReceiptHeader(createReceiptHeader(collectionStgReceiptObj,receiptPayee));
		return receiptPayee;
	}*/
    
	private ReceiptHeader createReceiptHeader(CollectionStgReceipt collectionStgReceiptObj)
	{
		ReceiptHeader receiptHeader = new ReceiptHeader();
		receiptHeader.setCollectiontype(collectionStgReceiptObj.getCollType());
		receiptHeader.setCollModesNotAllwd(null);
		receiptHeader.setConsumerCode(collectionStgReceiptObj.getBillingSysId());
		receiptHeader.setManualreceiptdate(collectionStgReceiptObj.getRcptDate());
		receiptHeader.setManualreceiptnumber(collectionStgReceiptObj.getRcptNo());
		receiptHeader.setMinimumAmount(collectionStgReceiptObj.getRcptAmount());
		receiptHeader.setPaidBy(collectionStgReceiptObj.getPaidBy());
		receiptHeader.setPartPaymentAllowed(false);
		receiptHeader.setReceipttype(CollectionConstants.RECEIPT_TYPE_BILL);
		receiptHeader.setReferencenumber(String.valueOf(collectionStgReceiptObj.getId()));
		receiptHeader.setService((ServiceDetails)persistenceService.findByNamedQuery(
				CollectionConstants.QUERY_SERVICE_BY_CODE,
				collectionStgReceiptObj.getModule()));
		//receiptHeader.setReceiptPayeeDetails(payeeDetails);
		if(receiptHeader.getService().getCode().equals(CollectionConstants.SERVICECODE_PROPERTYTAX))
		{
			receiptHeader.setReferenceDesc(CollectionConstants.PROPERTYTAX_REFERENCEDESCRIPTION+receiptHeader.getConsumerCode());
		}
		else if(receiptHeader.getService().getCode().equals(CollectionConstants.SERVICECODE_PROFESSIONALTAX))
		{
			receiptHeader.setReferenceDesc(CollectionConstants.PROFESSIONALTAX_REFERENCEDESCRIPTION+receiptHeader.getConsumerCode());
		} 
		
		receiptHeader.setReceiptDetails(createCreditReceiptDetail(
				collectionStgReceiptObj.getCollectionStgAccAmounts(),
				receiptHeader));
		
		receiptHeader.setReceiptMisc(createReceiptMis(
				collectionStgReceiptObj, receiptHeader));
		
		//Add Instrument Detail For this receipt
		addInstrumentDetails(collectionStgReceiptObj);
		
		return receiptHeader;
	}
	
	private void addInstrumentDetails(CollectionStgReceipt collectionStgReceipt)
	{
		/*ArrayList<String> instrumentNumber = new ArrayList<String>(0);
		ArrayList<String> instrumentType = new ArrayList<String>(0);
		ArrayList<String> instrumentDate = new ArrayList<String>(0);
		ArrayList<String> instrumentAmount = new ArrayList<String>(0);
		ArrayList<String> instrumentBankId = new ArrayList<String>(0);
		ArrayList<String> instrumentBranchName = new ArrayList<String>(0);
		ArrayList<String> instrumentBankName = new ArrayList<String>(0);*/
		List<InstrumentHeader> instrumentProxyList = new ArrayList<InstrumentHeader>();

		for(CollectionStgInstrument collectionStgInstrument:collectionStgReceipt.getCollectionStgInstruments())
		{
			InstrumentHeader instrumentHeader = new InstrumentHeader();
							
			instrumentTypeCashOrCard = collectionStgInstrument.getCollMode();
			
		
			
			if(instrumentTypeCashOrCard.equals(CollectionConstants.INSTRUMENTTYPE_CASH))
			{
				instrumentHeader.setInstrumentAmount(collectionStgInstrument.getAmount());
				receiptAction.setInstrHeaderCash(instrumentHeader);
			}
			if(instrumentTypeCashOrCard.equals(CollectionConstants.INSTRUMENTTYPE_CARD))
			{
				instrumentHeader.setInstrumentNumber(collectionStgInstrument.getInstrNo());
				instrumentHeader.setInstrumentDate(collectionStgInstrument.getInstrDate());
				instrumentHeader.setTransactionNumber(collectionStgInstrument.getInstrNo());
				instrumentHeader.setTransactionDate(collectionStgInstrument.getInstrDate());
				instrumentHeader.setInstrumentAmount(collectionStgInstrument.getAmount());
				receiptAction.setInstrHeaderBank(instrumentHeader);
			}	
			if (collectionStgInstrument.getCollMode().equals(
					CollectionConstants.INSTRUMENTTYPE_CHEQUE)
					|| collectionStgInstrument.getCollMode().equals(
							CollectionConstants.INSTRUMENTTYPE_DD)) {
				
			    if (collectionStgInstrument.getCollMode().equals(CollectionConstants.INSTRUMENTTYPE_CHEQUE)) {
    				//instrumentHeader.setInstrumentType(financialsUtil.getInstrumentTypeByType(CollectionConstants.INSTRUMENTTYPE_CHEQUE));
    				} else if (collectionStgInstrument.getCollMode().equals(CollectionConstants.INSTRUMENTTYPE_DD)) {
    				//instrumentHeader.setInstrumentType(financialsUtil.getInstrumentTypeByType(CollectionConstants.INSTRUMENTTYPE_DD));
    				}
            			instrumentHeader.setInstrumentNumber(collectionStgInstrument.getInstrNo());
            			instrumentHeader.setInstrumentDate(collectionStgInstrument.getInstrDate());
            			instrumentHeader.setInstrumentAmount(collectionStgInstrument.getAmount());
               			Bank bank = (Bank) persistenceService.find("from Bank where name=?", collectionStgInstrument.getBank());
               			instrumentHeader.setBankId(bank);
               			instrumentHeader.setBankBranchName(collectionStgInstrument.getBranch());
               			instrumentProxyList.add(instrumentHeader);
           		 }
		}
		
		if(instrumentTypeCashOrCard.equals(CollectionConstants.INSTRUMENTTYPE_CHEQUE)
				|| instrumentTypeCashOrCard.equals(CollectionConstants.INSTRUMENTTYPE_DD))
		{
			receiptAction.setInstrumentProxyList(instrumentProxyList);
			/*receiptAction.setInstrumentType(instrumentType.toArray(new String[0]));
			receiptAction.setInstrumentNumber(instrumentNumber.toArray(new String[0]));
			receiptAction.setInstrumentDate(instrumentDate.toArray(new String[0]));
			receiptAction.setInstrumentAmount(instrumentAmount.toArray(new String[0]));
			receiptAction.setInstrumentBankId(instrumentBankId.toArray(new String[0]));
			receiptAction.setInstrumentBankName(instrumentBankName.toArray(new String[0]));
			receiptAction.setInstrumentBranchName(instrumentBranchName.toArray(new String[0]));*/
		}		
	}
	
	private ReceiptMisc createReceiptMis(
			CollectionStgReceipt collectionStgReceiptObj,ReceiptHeader receiptHeader) {
		ReceiptMisc receiptMisc = new ReceiptMisc();
		
		receiptMisc.setReceiptHeader(receiptHeader);
		
		BigInteger bigInt = BigInteger.valueOf(collectionStgReceiptObj.getWardNo());
		
		if (collectionStgReceiptObj.getModule().equals(
				CollectionConstants.SERVICECODE_PROPERTYTAX)) {
			receiptMisc.setFund((Fund) persistenceService.find(
					"from Fund  where name=? ", "01-Municipal Fund"));
			receiptMisc.setDepartment((Department) persistenceService.find(
					"from Department d where d.deptName=? ", "R-Revenue"));
			if(bigInt!=null)
			{	
				receiptMisc.setBoundary((Boundary) persistenceService.find(
						"from Boundary where boundaryNum=? ", bigInt));
			}
		}
		
		else if (collectionStgReceiptObj.getModule().equals(
					CollectionConstants.SERVICECODE_PROFESSIONALTAX)) {
			receiptMisc.setFund((Fund) persistenceService.find(
					"from Fund  where name=? ", "01-Municipal Fund"));
			receiptMisc.setDepartment((Department) persistenceService.find(
					"from Department d where d.deptName=? ", "R-Revenue"));
						
			if(bigInt!=null)
			{	
				receiptMisc.setBoundary((Boundary) persistenceService.find(
					"from Boundary where boundaryNum=? ",bigInt));
			}	
		}
		return receiptMisc;
	}
	
	private Set<ReceiptDetail> createCreditReceiptDetail(Set<CollectionStgAccAmount> collectionStgAccAmountsSet
			,ReceiptHeader receiptHeader)
	{
		Set<ReceiptDetail> receiptDetailsSet = new HashSet<ReceiptDetail>();
		Long ordernumber = new Long(1); 
		
		for(CollectionStgAccAmount egCollectionStgAccAmountsObj:collectionStgAccAmountsSet)
		{	
			ReceiptDetail receiptDetailObj = new ReceiptDetail();
			receiptDetailObj.setReceiptHeader(receiptHeader);
			receiptDetailObj.setDescription(egCollectionStgAccAmountsObj.getDescription());
			receiptDetailObj.setDramount(BigDecimal.ZERO);
			
			receiptDetailObj.setAccounthead(commonsServiceImpl.getCChartOfAccountsByGlCode(
					egCollectionStgAccAmountsObj.getGlCode()));
			
			
			if (egCollectionStgAccAmountsObj.getTaxAmount()!=null && 
					egCollectionStgAccAmountsObj.getTaxAmount().compareTo(BigDecimal.ZERO)!=0)
			{	
				receiptDetailObj.setCramountToBePaid(egCollectionStgAccAmountsObj.getTaxAmount());
				receiptDetailObj.setCramount(egCollectionStgAccAmountsObj.getTaxAmount());
				receiptDetailObj.setOrdernumber(ordernumber);
				ordernumber++;
			}
			if (egCollectionStgAccAmountsObj.getPenalty()!=null && 
					egCollectionStgAccAmountsObj.getPenalty().compareTo(BigDecimal.ZERO)!=0)
			{	
				receiptDetailObj.setCramountToBePaid(egCollectionStgAccAmountsObj.getPenalty());
				receiptDetailObj.setCramount(egCollectionStgAccAmountsObj.getPenalty());
				receiptDetailObj.setOrdernumber(ordernumber);
				ordernumber++;
			}
			if (egCollectionStgAccAmountsObj.getAdvance()!=null && 
					egCollectionStgAccAmountsObj.getAdvance().compareTo(BigDecimal.ZERO)!=0)
			{	
				receiptDetailObj.setCramountToBePaid(egCollectionStgAccAmountsObj.getAdvance());
				receiptDetailObj.setCramount(egCollectionStgAccAmountsObj.getAdvance());
				receiptDetailObj.setOrdernumber(ordernumber);
				ordernumber++;
			}
			
			receiptDetailsSet.add(receiptDetailObj);
			receiptDetailList.add(receiptDetailObj);
		}
		return receiptDetailsSet;
	}
	
    /**
	 * This method is used to persist the data from the staging tables.
	 * 
	 * @return
	 */
	public String save() {
		injectObjectsIntoAction();
		
		List<CollectionStgReceipt> collectionStgReceiptList = new ArrayList<CollectionStgReceipt>();
		
		collectionStgReceiptList = persistenceService.findAllBy("from org.egov.erpcollection.models.CollectionStgReceipt where " +
				"isCollSysUpdated=? or isCollSysUpdated is null", "N");
		
				
		for(CollectionStgReceipt newEgCollectionStgReceiptObj:collectionStgReceiptList)
		{
			initialiseObjectsInAction(newEgCollectionStgReceiptObj);
			//receiptAction.setModelPayeeList(modelPayeeList);
			//HibernateUtil.beginTransaction();
			try{
				receiptAction.save();
				LOGGER.info("Receipt Created for bill no: "
						+ newEgCollectionStgReceiptObj.getBillingSysId()
						+ " of " + "billing system:	"
						+ newEgCollectionStgReceiptObj.getModule());
				newEgCollectionStgReceiptObj.setIsCollSysUpdated('Y');
				collStgReceiptService.persist(newEgCollectionStgReceiptObj);
			}
			catch(Exception ex){
				//Some error in Receipt creation. Stop and move to next record
				String errMsg = "Error in save method of HistoricDataUploadAction!";
				LOGGER.error(errMsg, ex);
				//HibernateUtil.rollbackTransaction();
			}
		}
		
		//HibernateUtil.commitTransaction();
		return SUCCESS;
	}
	
	public String execute() throws Exception {  
		return list();  
	}  
	  
	public String newform(){  
		return NEW;  
	} 
	
	public String list() {  
		return INDEX;  
	}  
  
	public String edit(){  
		  
		return EDIT;  
	}  
	public String create(){  
		  
		return SUCCESS;  
	}  

	public Object getModel() {  
		return null;  
	}
	/**
	 * @param collectionCommon
	 *            the collectionCommon to set
	 */
	public void setCollectionCommon(CollectionCommon collectionCommon) {
		this.collectionCommon = collectionCommon;
	}
	/**
	 * @param receiptHeaderService
	 *            The receipt header service to set
	 */
	public void setReceiptHeaderService(
			ReceiptHeaderService receiptHeaderService) {
		this.receiptHeaderService = receiptHeaderService;
	}
	public void setCollectionsUtil(CollectionsUtil collectionsUtil) {
		this.collectionsUtil = collectionsUtil;
	}
	public void setFinancialsUtil(FinancialsUtil financialsUtil) {
		this.financialsUtil = financialsUtil;
	}

	public void setCollStgReceiptService(
			PersistenceService<CollectionStgReceipt, Long> collStgReceiptService) {
		this.collStgReceiptService = collStgReceiptService;
	}

	

	
}
