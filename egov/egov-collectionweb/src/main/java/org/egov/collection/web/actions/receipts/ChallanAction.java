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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.AccountPayeeDetail;
import org.egov.collection.entity.Challan;
import org.egov.collection.entity.ReceiptDetail;
import org.egov.collection.entity.ReceiptDetailInfo;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.collection.entity.ReceiptMisc;
import org.egov.collection.entity.ReceiptVoucher;
import org.egov.collection.service.ChallanService;
import org.egov.collection.service.ReceiptHeaderService;
import org.egov.collection.utils.CollectionCommon;
import org.egov.collection.utils.CollectionsUtil;
import org.egov.collection.utils.FinancialsUtil;
import org.egov.commons.Accountdetailkey;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccountDetail;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.Fund;
import org.egov.commons.service.CommonsServiceImpl;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.workflow.service.WorkflowService;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.models.ServiceDetails;
import org.egov.infstr.utils.NumberUtil;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.pims.commons.Position;
import org.hibernate.StaleObjectStateException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


@ParentPackage("egov")
@Transactional(readOnly=true)
public class ChallanAction extends BaseFormAction {
	
	private static final Logger LOGGER = Logger.getLogger(ChallanAction.class);
	private static final long serialVersionUID = 1L;
	
	protected List<String> headerFields ;

	protected List<String> mandatoryFields;
	private List<ReceiptDetailInfo> subLedgerlist;
	private List<ReceiptDetailInfo> billDetailslist;
	
	private ReceiptHeader receiptHeader=new ReceiptHeader();
	private ChallanService challanService;
	private WorkflowService<Challan> challanWorkflowService;
	
	private CollectionsUtil collectionsUtil;
	private FinancialsUtil financialsUtil;
	private CommonsServiceImpl commonsServiceImpl;
	private BoundaryDAO boundaryDAO;
	private String deptId;
	private Long boundaryId;
	
	private Department dept;
	private Boundary boundary;
	private CFunction function;
	
	private Long receiptId;

	private final String VIEW=CollectionConstants.VIEW;
	private CollectionCommon collectionCommon;
	private ReceiptHeaderService receiptHeaderService;
	
	@Autowired
	private AppConfigValueService appConfigValuesService;
	
	//Added for Challan Approval	
	private String challanId;
	private String approvalRemarks;
	private Long positionUser;
	private Integer designationId;
	
	/**
	 * A <code>String</code> value representing the challan number for which the challan 
	 * has to be retrieved 
	 */
	private String challanNumber;
	

	private Boolean cashAllowed = Boolean.TRUE;
	private Boolean cardAllowed = Boolean.TRUE;
	private Boolean chequeDDAllowed = Boolean.TRUE;
	
	/**
	 * An instance of <code>InstrumentHeader</code> representing the cash instrument 
	 * details entered by the user during receipt creation
	 */
	private InstrumentHeader instrHeaderCash;
	
	/**
	 * An instance of <code>InstrumentHeader</code> representing the card instrument 
	 * details entered by the user during receipt creation
	 */
	private InstrumentHeader instrHeaderCard;
	
	// Instrument information derived from UI
	private List<InstrumentHeader> instrumentProxyList;
	private int instrumentCount;	
	
	private BigDecimal cashOrCardInstrumenttotal=BigDecimal.ZERO;
	
	private BigDecimal chequeInstrumenttotal=BigDecimal.ZERO;
	
	private String instrumentTypeCashOrCard;
	
	/**
	 * A String representing the action chosen to be performed from the UI
	 */
	private String actionName;
	
	/**
	 * A String representing the action chosen to be performed from the UI
	 */
	private String sourcePage;
	
	/**
	 * A String representing the voucher number for the challan
	 */
	private String voucherNumber;
	
	List<ValidationError> errors=new ArrayList<ValidationError>();
	
	private Integer reportId = -1;
	
	Position position=null;
	
	/**
	 * A <code>Long</code> array of receipt header ids , which have to be displayed for
	 * view/print/cancel purposes
	 */
	private Long[] selectedReceipts;
	
	private String currentFinancialYearId;
	
	/**
	 * An array of  <code>ReceiptHeader</code> instances which have to be displayed for
	 * view/print/cancel purposes
	 */
	//private ReceiptHeader[] receipts;
	
	//private String reasonForCancellation;
	
	public ChallanAction(){
		addRelatedEntity("receiptMisc.fund", Fund.class);
		addRelatedEntity("challan.service", ServiceDetails.class);
	}
	
	@Override
	public Object getModel() {
		if(receiptHeader.getReceiptMisc()==null)
			receiptHeader.setReceiptMisc(new ReceiptMisc());
		if(receiptHeader.getChallan()==null)
			receiptHeader.setChallan(new Challan());
		return receiptHeader;
	}
	
	
	/**
	 * This method is invoked when the user clicks on Create Challan from Menu Tree
	 * @return
	 */
	@Action(value="/receipts/challan-newform",results = { @Result(name = NEW,type="redirect") })
	public String newform(){
		//addDropdownData("designationMasterList",collectionsUtil.
				//getDesignationsAllowedForChallanApproval(collectionsUtil.getLoggedInUser(),receiptHeader));
		addDropdownData("approverDepartmentList", collectionsUtil.getDepartmentsAllowedForChallanApproval(
				collectionsUtil.getLoggedInUser(),receiptHeader));
		return NEW;
	}
	
	/**
	 * This method is invoked when user clicks on Save challan from Create Challan UI.
	 * 
	 * All workflow transitions for the challan happen through this method.
	 *
	 * @return the string
	 */
	@ValidationErrorPage(value = "view")
	@Transactional
	public String save(){
				
		if(!actionName.equals(CollectionConstants.WF_ACTION_NAME_REJECT_CHALLAN)){
			if(getPositionUser()==null || getPositionUser()==-1) {
				position=collectionsUtil.getPositionOfUser(
						collectionsUtil.getLoggedInUser());
			} else {
				position=collectionsUtil.getPositionById(positionUser);
			}
		}
		
		// this should changed later and made applicable to all wflow states
		if(actionName.equals(CollectionConstants.WF_ACTION_NAME_NEW_CHALLAN) ||
				actionName.equals(CollectionConstants.WF_ACTION_NAME_MODIFY_CHALLAN) || 
				actionName.equals(CollectionConstants.WF_ACTION_NAME_VALIDATE_CHALLAN))
		{
			return saveChallan();
		}
		
		challanService.workflowtransition(
				receiptHeader.getChallan(), position, actionName, approvalRemarks);

		return SUCCESS;
	}
	
	/**
	 * This method in invoked when the user clicks on Create Challan Receipt 
	 * from Menu Tree.
	 *
	 * @return the string
	 */
	@ValidationErrorPage(value = "createReceipt")
	@Action(value="/receipts/challan-createReceipt",results = { @Result(name = CollectionConstants.CREATERECEIPT,type="redirect")})
	@Transactional
	public String createReceipt(){
		if(challanNumber!=null && !"".equals(challanNumber)){
			receiptHeader=(ReceiptHeader) persistenceService.findByNamedQuery(
					CollectionConstants.QUERY_VALIDRECEIPT_BY_CHALLANNO,challanNumber);
			
			if(receiptHeader==null){
				receiptHeader=new ReceiptHeader();
				errors.add(new ValidationError(getText("challan.notfound.message"),
						"No Valid Challan Found. Please check the challan number."));
				throw new ValidationException(errors);
			}
			
			if(CollectionConstants.RECEIPT_STATUS_CODE_PENDING.equals(
					receiptHeader.getStatus().getCode())){
				loadReceiptDetails();
				setCollectionModesNotAllowed();
			}
			else{
				errors.add(new ValidationError(
						getText("challanreceipt.created.message",new String[]{
								receiptHeader.getReceiptnumber()}),
					"Receipt Already Created For this Challan. Receipt Number is " 
								+ receiptHeader.getReceiptnumber()));
				throw new ValidationException(errors);
			}
		}
		
		return CollectionConstants.CREATERECEIPT;
	}
	
	
	
	@ValidationErrorPage(value = "new")
	@Transactional
	public String saveChallan(){
		
		receiptHeader.getReceiptDetails().clear();
		errors.clear();
		
		//addDropdownData("designationMasterList",collectionsUtil.
				//getDesignationsAllowedForChallanApproval(collectionsUtil.getLoggedInUser(),receiptHeader));
		addDropdownData("approverDepartmentList", collectionsUtil.getDepartmentsAllowedForChallanApproval(
				collectionsUtil.getLoggedInUser(),receiptHeader));
		
		populateAndPersistChallanReceipt();
		
		challanService.workflowtransition(
				receiptHeader.getChallan(), position, 
				actionName, approvalRemarks);
		
		addActionMessage(getText("challan.savechallan.success",
				new String[]{receiptHeader.getChallan().getChallanNumber()}));

		if(CollectionConstants.WF_ACTION_NAME_VALIDATE_CHALLAN.equals(actionName)){
			return SUCCESS;
		}

		return viewChallan();
	}
	
	/**
	 * This method is invoked to view the challan. 
	 * 
	 * @return
	 */
	@Action(value="/receipts/challan-viewChallan" ,results = { @Result(name = VIEW,type="redirect") })
	public String viewChallan(){
		if(challanId==null){
			receiptHeader=receiptHeaderService.findById(receiptId, false);
		}
		else{
			receiptHeader=(ReceiptHeader) persistenceService.findByNamedQuery(
					CollectionConstants.QUERY_RECEIPT_BY_CHALLANID,
					Long.valueOf(challanId));
		}
		//addDropdownData("designationMasterList",collectionsUtil.
				//getDesignationsAllowedForChallanApproval(collectionsUtil.getLoggedInUser(),receiptHeader));
		addDropdownData("approverDepartmentList", collectionsUtil.getDepartmentsAllowedForChallanApproval(
				collectionsUtil.getLoggedInUser(),receiptHeader));
		loadReceiptDetails();
		return VIEW;
	}
	
	
	/**
	 * This method is invoked on click of create button from 
	 * Create Challan Receipt Screen  
	 * @return
	 */
	@ValidationErrorPage(value = "createReceipt")
	@Transactional
	public String saveOrupdate(){
		try {
			errors.clear();
		
		// for post remittance cancellation
		if(receiptHeader.getReceiptHeader()!=null){
			collectionCommon.cancelChallanReceiptOnCreation(receiptHeader);
		}
		
		boolean setInstrument=true;
		List<InstrumentHeader> receiptInstrList=new ArrayList<InstrumentHeader>();
		
		receiptHeader.setIsReconciled(Boolean.FALSE);
		receiptHeader.setIsModifiable(Boolean.TRUE);
		receiptHeader.setCollectiontype(CollectionConstants.COLLECTION_TYPE_COUNTER);
		//is this reqd
		receiptHeader.setLocation(collectionsUtil.getLocationOfUser(getSession()));
		receiptHeader.setStatus(collectionsUtil.getEgwStatusForModuleAndCode(
				CollectionConstants.MODULE_NAME_RECEIPTHEADER, 
				CollectionConstants.RECEIPT_STATUS_CODE_TO_BE_SUBMITTED));
		receiptHeader.setCreatedBy(collectionsUtil.getLoggedInUser());
		receiptHeader.setCreatedDate(new Date());
		
		if(setInstrument){
			receiptInstrList=populateInstrumentDetails();
			setInstrument=false;
		}

		receiptHeader.setReceiptInstrument(new HashSet(receiptInstrList));
		
		BigDecimal debitAmount = BigDecimal.ZERO;
		
		for(ReceiptDetail receiptDetail : receiptHeader.getReceiptDetails()){
			debitAmount= debitAmount.add(receiptDetail.getCramount());
		}
		
		receiptHeader.addReceiptDetail(collectionCommon.addDebitAccountHeadDetails(
				debitAmount, receiptHeader,chequeInstrumenttotal,cashOrCardInstrumenttotal,
				instrumentTypeCashOrCard));
		
		if(chequeInstrumenttotal!=null && chequeInstrumenttotal.compareTo(BigDecimal.ZERO)!=0){
			receiptHeader.setTotalAmount(chequeInstrumenttotal);
		}
		
		if(cashOrCardInstrumenttotal!=null && cashOrCardInstrumenttotal.compareTo(BigDecimal.ZERO)!=0){
			receiptHeader.setTotalAmount(cashOrCardInstrumenttotal);
		}
		
		receiptHeaderService.persist(receiptHeader);
		
		//Start work flow for all newly created receipts This might internally
		//create vouchers also based on configuration
		receiptHeaderService.startWorkflow(receiptHeader,Boolean.FALSE);
		receiptHeaderService.getSession().flush();
		LOGGER.info("Workflow started for newly created receipts");

		List<CVoucherHeader> voucherHeaderList = new ArrayList<CVoucherHeader>();
		
		// If vouchers are created during work flow step, add them to the list
		Set<ReceiptVoucher> receiptVouchers = receiptHeader.getReceiptVoucher();
		for (ReceiptVoucher receiptVoucher : receiptVouchers) {
			try {
				voucherHeaderList.add(receiptVoucher.getVoucherheader());
			} catch (Exception e) {
				LOGGER.error("Error in getting voucher header for id ["
						+ receiptVoucher.getVoucherheader() + "]", e);
			}
		}
		
		if (voucherHeaderList != null && receiptInstrList != null) {
			receiptHeaderService.updateInstrument(voucherHeaderList,
					receiptInstrList);
		}
		
		
		ReceiptHeader[] receipts = new ReceiptHeader[1];
		receipts[0]=receiptHeader;

		try {
			reportId=collectionCommon.generateReport(
					receipts, getSession(), true);
		} catch (Exception e) {
			LOGGER.error(CollectionConstants.REPORT_GENERATION_ERROR, e);
			throw new EGOVRuntimeException(CollectionConstants.REPORT_GENERATION_ERROR, e);
		}
		return CollectionConstants.REPORT;
		}catch (StaleObjectStateException exp) {
			errors.add(new ValidationError(getText("challanreceipt.created.staleobjectstate"),
					"Receipt Already Created For this Challan.Go to Search Receipt screen to Re-print the receipt."));
			throw new ValidationException(errors);
		} catch (Exception exp) {
			errors.add(new ValidationError(getText("challanreceipt.create.errorincreate"), "Error occured in Challan Receipt creation, please try again."));
			throw new ValidationException(errors);
		}
	}
	
	/**
	 * This method generates the report for the requested challan
	 * 
	 * @return
	 */
	public String printChallan(){
		
		try {
			reportId=collectionCommon.generateChallan(
					receiptHeader, getSession(), true);
		} catch (Exception e) {
			LOGGER.error(CollectionConstants.REPORT_GENERATION_ERROR, e);
			throw new EGOVRuntimeException(CollectionConstants.REPORT_GENERATION_ERROR, e);
		}
		setSourcePage("viewChallan");
		return CollectionConstants.REPORT;
	}
	
	/**
	 * This method directs the user to cancel the requested challan receipt
	 * 
	 * @return
	 */
	public String cancelReceipt(){
		if (getSelectedReceipts() != null && 
				getSelectedReceipts().length > 0) {
			receiptHeader = receiptHeaderService.findById(
					Long.valueOf(selectedReceipts[0]), false);
			loadReceiptDetails();
		}
		return CollectionConstants.CANCELRECEIPT;
	}
	
	/**
	 * This method is invoked when receipt is cancelled
	 * 
	 * @return
	 */
	@Transactional
	public String saveOnCancel() {
		boolean isInstrumentDeposited = false;
		setSourcePage(CollectionConstants.CANCELRECEIPT);
		
		/**
		 * the model is the receipt header which has to be cancelled
		 */
		for (InstrumentHeader instrumentHeader : receiptHeader.getReceiptInstrument()){
			
			if(instrumentHeader.getInstrumentType().getType().equals(
					CollectionConstants.INSTRUMENTTYPE_CASH)){
				if (instrumentHeader.getStatusId().getDescription().equals(
						CollectionConstants.INSTRUMENT_RECONCILED_STATUS)){
					isInstrumentDeposited = true;
					break;
				}
			}
			else{	
				if (instrumentHeader.getStatusId().getDescription().equals(
						CollectionConstants.INSTRUMENT_DEPOSITED_STATUS)){
					isInstrumentDeposited = true;
					break;
				}
			}	
		}
		
		//post remittance cancellation
		if(isInstrumentDeposited){
			
			if(collectionsUtil.checkChallanValidity(receiptHeader.getChallan())){
			/**
			 * if instrument has been deposited create a new receipt in place of the cancelled
			 * the model is turned into a copy of the receipt to be cancelled without the instrument details
			 */
				
		    // the reason for cancellation has to be persisted
			receiptHeaderService.persist(receiptHeader);
			
			receiptHeader=collectionCommon.createPendingReceiptFromCancelledChallanReceipt(receiptHeader);
			//receiptPayeeDetailsService.persist(receiptHeader.getReceiptPayeeDetails());
			
			LOGGER.info(" Created a receipt in PENDING status in lieu of the cancelled receipt ");
			
			loadReceiptDetails();
			
			// set collection modes allowed rule through script
			setCollectionModesNotAllowed();
			
			return CollectionConstants.CREATERECEIPT;
			}
			else{
				// the receipt is cancelled, voucher is reversed, but instrument is not cancelled
				collectionCommon.cancelChallanReceipt(receiptHeader,false);
				
				addActionMessage(getText("challan.expired.message", 
						"Please note that a new receipt can not be created as the corresponding challan " + 
						receiptHeader.getChallan().getChallanNumber() + " has expired"));
			}
		}
		//if instrument has not been deposited, cancel the old instrument, reverse the
		//voucher and persist
		else{
			collectionCommon.cancelChallanReceipt(receiptHeader,true);
			
			//End work-flow for the cancelled receipt
			if(!receiptHeader.getState().getValue().equals(CollectionConstants.WF_STATE_END))
			{
				receiptHeaderService.endReceiptWorkFlowOnCancellation(receiptHeader);
			}	
			//if the challan is valid, recreate a new receipt in pending state and populate it with the
			//cancelled receipt details (except instrument and voucher details)
			if(collectionsUtil.checkChallanValidity(receiptHeader.getChallan())){
				
				ReceiptHeader newReceipt=collectionCommon.createPendingReceiptFromCancelledChallanReceipt(
						receiptHeader);
				
				receiptHeaderService.persist(newReceipt);
				
				LOGGER.info(" Created a receipt in PENDING status in lieu of the cancelled receipt ");
			}
		}
		return SUCCESS;
	}
	
	/*public List<Action> getValidActions(){
		Challan challan = receiptHeader.getChallan();
		if(challan == null)
			challan = new Challan();
		
		List<Action> actions = challanWorkflowService.getValidActions(challan);
		
		return actions;
	}*/
	
	/**
	 * Populate Instrument Details.
	 *
	 * @return the list
	 */
	private List<InstrumentHeader> populateInstrumentDetails(){
		List<InstrumentHeader> instrumentHeaderList=new ArrayList<InstrumentHeader>();
		
		if(CollectionConstants.INSTRUMENTTYPE_CASH.equals(instrumentTypeCashOrCard)){
			instrHeaderCash.setInstrumentType(
					financialsUtil.getInstrumentTypeByType(
							CollectionConstants.INSTRUMENTTYPE_CASH));
			
			instrHeaderCash.setIsPayCheque(CollectionConstants.ZERO_INT);
			
			//the cash amount is set into the object through binding
			//this total is needed for creating debit account head
			cashOrCardInstrumenttotal = cashOrCardInstrumenttotal.add(
					instrHeaderCash.getInstrumentAmount());
			
			instrumentHeaderList.add(instrHeaderCash);
		}
		if(CollectionConstants.INSTRUMENTTYPE_CARD.equals(instrumentTypeCashOrCard)){
			instrHeaderCard.setInstrumentType(financialsUtil.getInstrumentTypeByType(
							CollectionConstants.INSTRUMENTTYPE_CARD));
			
			if(instrHeaderCard.getTransactionDate()==null){
				instrHeaderCard.setTransactionDate(new Date());
			}
			instrHeaderCard.setIsPayCheque(CollectionConstants.ZERO_INT);
			
			//the instrumentNumber, transactionNumber, instrumentAmount are 
			//set into the object directly through binding
			cashOrCardInstrumenttotal = cashOrCardInstrumenttotal.add(instrHeaderCard.getInstrumentAmount());
			
			instrumentHeaderList.add(instrHeaderCard);
		}
		
		// cheque/DD types
		if(instrumentProxyList!=null){
		if (instrumentProxyList.get(0).getInstrumentType().getType().equals(CollectionConstants.INSTRUMENTTYPE_CHEQUE)
				|| instrumentProxyList.get(0).getInstrumentType().getType().equals(CollectionConstants.INSTRUMENTTYPE_DD)) {
			instrumentHeaderList = populateInstrumentHeaderForChequeDD(instrumentHeaderList, instrumentProxyList);
		}
		}
		instrumentHeaderList=receiptHeaderService.createInstrument(instrumentHeaderList);
		
		return instrumentHeaderList;
	  }
	
	/**
	 * This instrument creates instrument header instances for the receipt, when the 
	 * instrument type is Cheque or DD. The created <code>InstrumentHeader</code> 
	 * instance is persisted 
	 * 
	 * @param k an int value representing the index of the instrument type as chosen 
	 * from the front end
	 *  
	 * @return an <code>InstrumentHeader</code> instance populated with the instrument 
	 * details
	 */
	private List<InstrumentHeader> populateInstrumentHeaderForChequeDD(List<InstrumentHeader> instrumentHeaderList,
			List<InstrumentHeader> instrumentProxyList){
		
		for (InstrumentHeader instrumentHeader : instrumentProxyList) {
			if (instrumentHeader.getInstrumentType().getType().equals(CollectionConstants.INSTRUMENTTYPE_CHEQUE)) {
				instrumentHeader.setInstrumentType(financialsUtil.getInstrumentTypeByType(CollectionConstants.INSTRUMENTTYPE_CHEQUE));
			} else if (instrumentHeader.getInstrumentType().getType().equals(CollectionConstants.INSTRUMENTTYPE_DD)) {
				instrumentHeader.setInstrumentType(financialsUtil.getInstrumentTypeByType(CollectionConstants.INSTRUMENTTYPE_DD));
			}
		        if (instrumentHeader.getBankId() != null) {
				instrumentHeader.setBankId(commonsServiceImpl.getBankById(Integer.valueOf(instrumentHeader.getBankId().getId())));
			}
		        chequeInstrumenttotal = chequeInstrumenttotal.add(instrumentHeader.getInstrumentAmount());
			instrumentHeader.setIsPayCheque(CollectionConstants.ZERO_INT);
			instrumentHeaderList.add(instrumentHeader);
		}
		return instrumentHeaderList;
	}
	
	
	/**
	 * This method creates a receipt along with the challan. The receipt is created in 
	 * PENDING status where as the challan is created with a CREATED status. 
	 * 
	 * The receipt is actually created later when there is a request for it to be 
	 * created against the challan.
	 */
	@Transactional
	private void populateAndPersistChallanReceipt(){

		if(voucherNumber!=null && !"".equals(voucherNumber)){
			CVoucherHeader voucherHeader = (CVoucherHeader) persistenceService.findByNamedQuery(
					CollectionConstants.QUERY_VOUCHERHEADER_BY_VOUCHERNUMBER , voucherNumber);
			
			if(voucherHeader==null){
				errors.add(new ValidationError("challan.invalid.vouchernumber",
						"Voucher not found. Please check the voucher number."));
			}
			receiptHeader.getChallan().setVoucherHeader(voucherHeader);
		}
		
		receiptHeader.setService((ServiceDetails) getPersistenceService().findByNamedQuery(
				CollectionConstants.QUERY_SERVICE_BY_CODE, 
				CollectionConstants.SERVICE_CODE_COLLECTIONS));
		receiptHeader.setStatus(collectionsUtil.getEgwStatusForModuleAndCode(
				CollectionConstants.MODULE_NAME_RECEIPTHEADER,
				CollectionConstants.RECEIPT_STATUS_CODE_PENDING));
		
		// recon flag should be set as false when the receipt is actually
		// created against the challan
		receiptHeader.setIsReconciled(Boolean.TRUE);
		receiptHeader.setIsModifiable(Boolean.FALSE);
		receiptHeader.setReceipttype(CollectionConstants.RECEIPT_TYPE_CHALLAN);
		receiptHeader.setPaidBy(CollectionConstants.CHAIRPERSON);
		
		
		receiptHeader.getReceiptMisc().setFund(
				commonsServiceImpl.fundById(receiptHeader.getReceiptMisc().getFund().getId()));
		
		Department dept = (Department) getPersistenceService().findByNamedQuery(
				CollectionConstants.QUERY_DEPARTMENT_BY_ID, 
				Integer.valueOf(this.deptId));
		receiptHeader.getReceiptMisc().setDepartment(dept);
		if(boundaryId!=null){
			receiptHeader.getReceiptMisc().setBoundary(boundaryDAO.getBoundary(boundaryId));
		}
		receiptHeader.getReceiptMisc().setReceiptHeader(receiptHeader);
		
		
		BigDecimal debitamount = BigDecimal.ZERO;
		removeEmptyRows(billDetailslist);
		removeEmptyRows(subLedgerlist);
		int m = 0;
		
		validateAccountDetails();
		
		BigDecimal totalAmt = BigDecimal.ZERO;
		
		for (ReceiptDetailInfo rDetails : billDetailslist) {
			CChartOfAccounts account = commonsServiceImpl.getCChartOfAccountsByGlCode(
					rDetails.getGlcodeDetail());
			CFunction function=null;
			if(rDetails.getFunctionIdDetail()!=null){
				function =  commonsServiceImpl.getFunctionById(
					rDetails.getFunctionIdDetail());
			}
			ReceiptDetail receiptDetail = new ReceiptDetail(
					account,function,rDetails.getCreditAmountDetail(),
					rDetails.getDebitAmountDetail(),null,
					Long.valueOf(m),null,null,receiptHeader);
			receiptDetail.setCramount(rDetails.getCreditAmountDetail());
			
			totalAmt = totalAmt.add(
					receiptDetail.getCramount()).subtract(
							receiptDetail.getDramount());

			CFinancialYear financialYear=commonsServiceImpl.getFinancialYearById(rDetails.getFinancialYearId());
			receiptDetail.setFinancialYear(financialYear);

			if(rDetails.getCreditAmountDetail() == null) {
				receiptDetail.setCramount(BigDecimal.ZERO);
			} else {
				receiptDetail.setCramount(rDetails.getCreditAmountDetail());
			}
			
			if(rDetails.getDebitAmountDetail() == null) {
				receiptDetail.setDramount(BigDecimal.ZERO);
			} else {
				receiptDetail.setDramount(rDetails.getDebitAmountDetail());
			}
			
			receiptDetail=setAccountPayeeDetails(subLedgerlist,receiptDetail);
			receiptHeader.addReceiptDetail(receiptDetail);
			debitamount = debitamount.add(rDetails.getCreditAmountDetail());
			debitamount = debitamount.subtract(rDetails.getDebitAmountDetail());
			m++;
		}
		
		receiptHeader.setTotalAmount(totalAmt);
			
		if(receiptHeader.getChallan().getCreatedBy()==null){
			receiptHeader.getChallan().setCreatedBy(
					collectionsUtil.getLoggedInUser());
		}
			
		receiptHeader.getChallan().setStatus(collectionsUtil.getEgwStatusForModuleAndCode(
				CollectionConstants.MODULE_NAME_CHALLAN, 
				CollectionConstants.CHALLAN_STATUS_CODE_CREATED));
		//set service in challan
		if(receiptHeader.getChallan().getService()!=null && receiptHeader.getChallan().getService().getId()!=null)
		{
			receiptHeader.getChallan().setService((ServiceDetails) getPersistenceService().findByNamedQuery(
					CollectionConstants.QUERY_SERVICE_BY_ID, 
					receiptHeader.getChallan().getService().getId()));
		}
		receiptHeaderService.persistChallan(receiptHeader);
		/*ReceiptPayeeDetails receiptPayee = receiptHeader.getReceiptPayeeDetails();
		receiptPayee.addReceiptHeader(receiptHeader);
		
		receiptPayee=receiptPayeeDetailsService.persistChallan(receiptPayee);
		receiptHeader=receiptPayee.getReceiptHeaders().iterator().next();*/
		receiptId=receiptHeader.getId();
		
		LOGGER.info("Persisted Challan and Created Receipt In Pending State For the Challan");
	}
	
	public ReceiptDetail setAccountPayeeDetails(List<ReceiptDetailInfo> subLedgerlist,ReceiptDetail receiptDetail){
		for (ReceiptDetailInfo subvoucherDetails : subLedgerlist) {
			if(subvoucherDetails.getGlcode()!=null && subvoucherDetails.getGlcode().getId()!=0 &&
				subvoucherDetails.getGlcode().getId().equals(receiptDetail.getAccounthead().getId())) {
				
				Accountdetailtype accdetailtype= (Accountdetailtype)getPersistenceService().findByNamedQuery(
						CollectionConstants.QUERY_ACCOUNTDETAILTYPE_BY_ID, 
						subvoucherDetails.getDetailType().getId());
				Accountdetailkey accdetailkey= (Accountdetailkey)getPersistenceService().findByNamedQuery(
						CollectionConstants.QUERY_ACCOUNTDETAILKEY_BY_DETAILKEY, 
						subvoucherDetails.getDetailKeyId(),subvoucherDetails.getDetailType().getId());
				
				AccountPayeeDetail accPayeeDetail = new AccountPayeeDetail(
						accdetailtype,accdetailkey,subvoucherDetails.getAmount(),receiptDetail);
		
				receiptDetail.addAccountPayeeDetail(accPayeeDetail);
			}
		}
		return receiptDetail;
	}
	
	
	/**
	 * Validate Account Details.
	 */
	protected void validateAccountDetails(){
		BigDecimal totalDrAmt = BigDecimal.ZERO;
		BigDecimal totalCrAmt = BigDecimal.ZERO;
		int index=0;
		
		for (ReceiptDetailInfo rDetails : billDetailslist) {
			index = index+1;
			totalDrAmt = totalDrAmt.add(rDetails.getDebitAmountDetail());
			totalCrAmt = totalCrAmt.add(rDetails.getCreditAmountDetail());
			
			if (rDetails.getDebitAmountDetail().compareTo(BigDecimal.ZERO)==0 
					&& rDetails.getCreditAmountDetail().compareTo(BigDecimal.ZERO) == 0 
					&& rDetails.getGlcodeDetail().trim().length()==0) {
				errors.add(new ValidationError("challan.accdetail.emptyaccrow",
						"No data entered in Account Details grid row : {0}",
						new String[]{""+index}));
			}
			else if(rDetails.getDebitAmountDetail().compareTo(BigDecimal.ZERO)==0 && 
					rDetails.getCreditAmountDetail().compareTo(BigDecimal.ZERO) == 0 
					&& rDetails.getGlcodeDetail().trim().length()!=0){
				errors.add(new ValidationError("challan.accdetail.amountZero",
						"Enter debit/credit amount for the account code : {0}",
						new String[]{rDetails.getGlcodeDetail()}));
			}else if (rDetails.getDebitAmountDetail().compareTo(BigDecimal.ZERO) >0 && 
					rDetails.getCreditAmountDetail().compareTo(BigDecimal.ZERO)>0) {
				errors.add(new ValidationError("challan.accdetail.amount",
						"Please enter either debit/credit amount for the account code : {0}",
						new String[]{rDetails.getGlcodeDetail()}));
			}else if (( rDetails.getDebitAmountDetail().compareTo(BigDecimal.ZERO) >0 
					||rDetails.getCreditAmountDetail().compareTo(BigDecimal.ZERO) >0)
					&& rDetails.getGlcodeDetail().trim().length()==0) {
				errors.add(new ValidationError("challan.accdetail.accmissing",
						"Account code is missing for credit/debit supplied field in account grid row :{0}",
						new String[]{""+index}));
			}
		}
		
		validateSubledgerDetails();
		
		if(!errors.isEmpty()){
			throw new ValidationException(errors);
		}
	}
	

	/**
	 * Validate Subledger Details.
	 */
	protected void validateSubledgerDetails(){
		
		Map<String,Object> accountDetailMap ;
		Map<String,BigDecimal> subledAmtmap = new HashMap<String,BigDecimal> ();
		 // this list will contain  the details about the account code those are detail codes.
		List<Map<String,Object>> subLegAccMap= null;
		for (ReceiptDetailInfo rDetails : billDetailslist) {
			
			CChartOfAccountDetail  chartOfAccountDetail = 
				(CChartOfAccountDetail) getPersistenceService().find(
						" from CChartOfAccountDetail" +
					" where glCodeId=(select id from CChartOfAccounts where glcode=?)", 
					rDetails.getGlcodeDetail());
			
			if(null != chartOfAccountDetail){
				accountDetailMap = new HashMap<String,Object>();
				accountDetailMap.put("glcodeId", rDetails.getGlcodeIdDetail());
				accountDetailMap.put("glcode", rDetails.getGlcodeDetail());
				if(rDetails.getDebitAmountDetail().compareTo(BigDecimal.ZERO)==0){
					accountDetailMap.put("amount", rDetails.getCreditAmountDetail());
				}else if (rDetails.getCreditAmountDetail().compareTo(BigDecimal.ZERO)==0) {
					accountDetailMap.put("amount", rDetails.getDebitAmountDetail());
				}
				if(null == subLegAccMap){
					subLegAccMap = new ArrayList<Map<String,Object>>();
					subLegAccMap.add(accountDetailMap);
				}else {
					subLegAccMap.add(accountDetailMap);
				}
			}
		}
		
		if(null != subLegAccMap){
			Map<String, String> subLedgerMap = new HashMap<String, String> ();
			
			for (ReceiptDetailInfo rDetails : subLedgerlist) {
				if(rDetails.getGlcode()!=null && rDetails.getGlcode().getId() != 0){
					if(null == subledAmtmap.get(rDetails.getGlcode().getId().toString())) {
						subledAmtmap.put(rDetails.getGlcode().getId().toString(),rDetails.getAmount());
					} else {
						BigDecimal debitTotalAmount =subledAmtmap.get(
								rDetails.getGlcode().getId().toString()).add(rDetails.getAmount());
						subledAmtmap.put(rDetails.getGlcode().getId().toString(),debitTotalAmount);
					}
					
					StringBuffer subledgerDetailRow = new StringBuffer();
					subledgerDetailRow.append( rDetails.getGlcode().getId().toString()).
						append(rDetails.getDetailType().getId().toString()).
						append(rDetails.getDetailKeyId().toString());
					if(null == subLedgerMap.get(subledgerDetailRow.toString())){
						subLedgerMap.put(subledgerDetailRow.toString(),subledgerDetailRow.toString());
					}// to check for  the same subledger should not allow for same gl code
					else{
						errors.add(new ValidationError("miscreciept.samesubledger.repeated",
								"Same subledger should not allow for same account code"));
					}
				}
			}
			
			for (Map<String,Object> map : subLegAccMap) {
				String glcodeId = map.get("glcodeId").toString();
				if(null == subledAmtmap.get(glcodeId) ){
					errors.add(new ValidationError("miscreciept.samesubledger.entrymissing",
							"Subledger detail entry is missing for account code: {0}",
							new String[]{map.get("glcode").toString()}));
				}else if(! subledAmtmap.get(glcodeId).equals(
						new BigDecimal(map.get("amount").toString()))){
					errors.add(new ValidationError("miscreciept.samesubledger.entrymissing",
							"Total subledger amount is not matching for account code : {0}",
							new String[]{map.get("glcode").toString()}));
				}
			}
		}
	}
	
	/**
	 * Removes the empty rows.
	 *
	 * @param list the list
	 */
	void removeEmptyRows(List list) {
		for (Iterator<ReceiptDetailInfo> detail = list.iterator(); detail.hasNext();) {
			if (detail.next() == null) {
				detail.remove();
			}
		}
	}
	
	/**
	 * Load receipt details.
	 */
	private void loadReceiptDetails(){
		setDeptId(receiptHeader.getReceiptMisc().getDepartment().getId().toString());
		setDept(receiptHeader.getReceiptMisc().getDepartment());
		setBoundary(receiptHeader.getReceiptMisc().getBoundary());
		setBillDetailslist(collectionCommon.setReceiptDetailsList(receiptHeader,CollectionConstants.COLLECTIONSAMOUNTTPE_BOTH));
		setSubLedgerlist(collectionCommon.setAccountPayeeList(receiptHeader));
		for (ReceiptDetail rDetails : receiptHeader.getReceiptDetails()) {
			if(rDetails.getFunction()!=null){
				setFunction(rDetails.getFunction());
				//break;
			}
		}

		if(receiptHeader.getReceiptMisc().getBoundary()!=null){
			setBoundaryId(receiptHeader.getReceiptMisc().getBoundary().getId());
		}
		
		if(receiptHeader.getChallan()!=null && receiptHeader.getChallan().getVoucherHeader()!=null){
			setVoucherNumber(receiptHeader.getChallan().getVoucherHeader().getVoucherNumber());
		}
		if(receiptHeader.getTotalAmount()!=null){
			receiptHeader.setTotalAmount(receiptHeader.getTotalAmount().setScale(CollectionConstants.AMOUNT_PRECISION_DEFAULT,BigDecimal.ROUND_UP));
		}
	}
	
	/**
	 * This method checks for the modes of payment allowed 
	 */
	private void setCollectionModesNotAllowed() {
		List<String> modesNotAllowed = collectionsUtil.getCollectionModesNotAllowed(
				collectionsUtil.getLoggedInUser());
		
		if(modesNotAllowed.contains(CollectionConstants.INSTRUMENTTYPE_CASH)){
			setCashAllowed(Boolean.FALSE);
		}
			
		if(modesNotAllowed.contains(CollectionConstants.INSTRUMENTTYPE_CARD)){
			setCardAllowed(Boolean.FALSE);
		}
		if(modesNotAllowed.contains(CollectionConstants.INSTRUMENTTYPE_CHEQUE) || 
				modesNotAllowed.contains(CollectionConstants.INSTRUMENTTYPE_DD)){
			setChequeDDAllowed(Boolean.FALSE);
		}
		
	}

	/* (non-Javadoc)
	 * @see org.egov.infra.web.struts.actions.BaseFormAction#prepare()
	 */
	@Override
	public void prepare() {
		
		setupChallanPage();
		
		if(receiptId!=null){
			receiptHeader = receiptHeaderService.findById(receiptId, false);
			receiptHeader = receiptHeaderService.merge(receiptHeader);
			
			if(receiptHeader.getChallan()!=null && 
					receiptHeader.getChallan().getService()!=null && 
					receiptHeader.getChallan().getService().getId()==-1)
			{	
				receiptHeader.getChallan().setService(null);
			}	
		}
		
		Department loginUserDepartment=collectionsUtil.getDepartmentOfLoggedInUser();
		
		addDropdownData("designationMasterList", new ArrayList());
		addDropdownData("postionUserList", new ArrayList());
		setDeptId(loginUserDepartment.getId().toString());
		setDept(loginUserDepartment);
		setCurrentFinancialYearId(collectionCommon.getFinancialYearIdByDate(new Date()));
		/**  
		 * super class prepare is called at the end to ensure that the modified values
		 * are available to the model.
		 * The super class prepare need not run for cancel challan as the values 
		 * should not be modified
		 * **/
		if(!CollectionConstants.WF_ACTION_NAME_CANCEL_CHALLAN.equals(actionName))
			super.prepare();
	}

	/**
	 * Setup challan page.
	 */
	public void setupChallanPage(){
		headerFields=new ArrayList<String>();
		mandatoryFields = new ArrayList<String>();
		getHeaderMandateFields();
		
		if(headerFields.contains(CollectionConstants.FUND)){
			setupDropdownDataExcluding("receiptMisc.fund");
			addDropdownData("fundList", persistenceService.findAllByNamedQuery(
					CollectionConstants.QUERY_ALL_FUND));
		}
		if(headerFields.contains(CollectionConstants.DEPARTMENT)){
			addDropdownData("departmentList", persistenceService.findAllByNamedQuery(
					CollectionConstants.QUERY_ALL_DEPARTMENTS));
		}
		if(headerFields.contains(CollectionConstants.FIELD)){
			addDropdownData("fieldList", persistenceService.findAllByNamedQuery(
					CollectionConstants.QUERY_ALL_FIELD));
		}
		setupDropdownDataExcluding("challan.service");
		addDropdownData("serviceList",persistenceService.findAllByNamedQuery(
				CollectionConstants.QUERY_CHALLAN_SERVICES,CollectionConstants.CHALLAN_SERVICE_TYPE));
		
		addDropdownData("financialYearList",persistenceService.findAllByNamedQuery(
				CollectionConstants.QUERY_ALL_ACTIVE_FINANCIAL_YEAR));
		if(getBillDetailslist()==null){
			setBillDetailslist(new ArrayList<ReceiptDetailInfo>());
			getBillDetailslist().add(new ReceiptDetailInfo());
		}
		if(getSubLedgerlist()==null){
			setSubLedgerlist(new ArrayList<ReceiptDetailInfo>());
			getSubLedgerlist().add(new ReceiptDetailInfo());
		}
		setHeaderFields(headerFields);
		setMandatoryFields(mandatoryFields);
		if (instrumentProxyList == null){
			instrumentCount = 0;
		}else{
			instrumentCount = instrumentProxyList.size();
		}
	}
	
	
	public boolean isFieldMandatory(String field){
		return mandatoryFields.contains(field);
	}
	
	
	public boolean shouldShowHeaderField(String field){
		return  headerFields.contains(field);
	}
	
	
	/**
	 * Gets the header mandate fields.
	 *
	 * @return the header mandate fields
	 */
	protected void getHeaderMandateFields() 
	{
		List<AppConfigValues> appConfigList=collectionsUtil.getAppConfigValues(
				CollectionConstants.MISMandatoryAttributesModule,
				CollectionConstants.MISMandatoryAttributesKey);
		
		for (AppConfigValues appConfigVal : appConfigList) 
		{
			String value = appConfigVal.getValue();
			String header=value.substring(0, value.indexOf('|'));
			headerFields.add(header);
			String mandate = value.substring(value.indexOf('|')+1);
			if(CollectionConstants.Mandatory.equalsIgnoreCase(mandate)){
				mandatoryFields.add(header);
			}
		}
		
		String isBillNoReqd = appConfigValuesService.getAppConfigValue(
				CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
				CollectionConstants.APPCONFIG_VALUE_ISBILLNUMREQD,
				CollectionConstants.NO);
		
		String isServiceReqd = appConfigValuesService.getAppConfigValue(
				CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
				CollectionConstants.APPCONFIG_VALUE_ISSERVICEREQD,
				CollectionConstants.NO);
		
		if(!CollectionConstants.NO.equals(isBillNoReqd)){
			headerFields.add(CollectionConstants.BILLNUMBER);
			mandatoryFields.add(CollectionConstants.BILLNUMBER);
		}
		
		
		if(!CollectionConstants.NO.equals(isServiceReqd)){
			headerFields.add(CollectionConstants.SERVICE);
			mandatoryFields.add(CollectionConstants.SERVICE);
		}
	}
	
	public List getHeaderFields(){
		return headerFields;
	}
	
	public void setHeaderFields(List headerFields){
		this.headerFields=headerFields;
	}
	
	
	public List getMandatoryFields(){
		return mandatoryFields;
	}
	
	public void setMandatoryFields(List mandatoryFields){
		this.mandatoryFields=mandatoryFields;
	}
	
	public String getChallanNumber() {
		return challanNumber;
	}


	public void setChallanNumber(String challanNumber) {
		this.challanNumber = challanNumber;
	}
	
	public List<ReceiptDetailInfo> getBillDetailslist() {
		return billDetailslist;
	}
	
	
	public void setBillDetailslist(List<ReceiptDetailInfo> billDetailslist) {
		this.billDetailslist = billDetailslist;
	}
	public List<ReceiptDetailInfo> getSubLedgerlist() {
		return subLedgerlist; 
	}

	public void setSubLedgerlist(List<ReceiptDetailInfo> subLedgerlist) {
		this.subLedgerlist = subLedgerlist;
	}
	
	public void setBoundaryDAO(BoundaryDAO boundaryDAO) {
		this.boundaryDAO = boundaryDAO;
	}
	
	public CFunction getFunction() {
		return function;
	}


	public void setFunction(CFunction function) {
		this.function = function;
	}
	
	public Department getDept() {
		return dept;
	}


	public void setDept(Department dept) {
		this.dept = dept;
	}


	public Boundary getBoundary() {
		return boundary;
	}


	public void setBoundary(Boundary boundary) {
		this.boundary = boundary;
	}
	
	public Long getBoundaryId() {
		return boundaryId;
	}


	public void setBoundaryId(Long boundaryId) {
		this.boundaryId = boundaryId;
	}
	
	public String getDeptId(){
		return deptId;
	}
	
	public void setDeptId(String deptId){
		this.deptId=deptId;
	}
	
	public void setCollectionsUtil(CollectionsUtil collectionsUtil){
		this.collectionsUtil=collectionsUtil;
	}
	
	public ReceiptHeader getReceiptHeader() {
		return receiptHeader;
	}

	public void setReceiptHeader(ReceiptHeader receiptHeader) {
		this.receiptHeader = receiptHeader;
	}
	
	public void setChallanService(ChallanService challanService) {
		this.challanService = challanService;
	}
	
	public void setChallanWorkflowService(
			WorkflowService<Challan> challanWorkflowService) {
		this.challanWorkflowService = challanWorkflowService;
	}
	
	public void setReceiptId(Long receiptId){
		this.receiptId=receiptId;
	}
	
	
	public Long getReceiptId(){
		return receiptId;
	}
	
	/**
	 * @return the challanId
	 */
	public String getChallanId() {
		return challanId;
	}


	/**
	 * @param challanId the challanId to set
	 */
	public void setChallanId(String challanId) {
		this.challanId = challanId;
	}


	/**
	 * @return the approvalRemarks
	 */
	public String getApprovalRemarks() {
		return approvalRemarks;
	}


	/**
	 * @param approvalRemarks the approvalRemarks to set
	 */
	public void setApprovalRemarks(String approvalRemarks) {
		this.approvalRemarks = approvalRemarks;
	}


	/**
	 * @return the positionUser
	 */
	public Long getPositionUser() {
		return positionUser;
	}


	/**
	 * @param positionUser the positionUser to set
	 */
	public void setPositionUser(Long positionUser) {
		this.positionUser = positionUser;
	}

	
	/**
	 * @param collectionCommon the collectionCommon to set
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
	
	public Boolean getCashAllowed() {
		return cashAllowed;
	}


	public void setCashAllowed(Boolean cashAllowed) {
		this.cashAllowed = cashAllowed;
	}


	public Boolean getCardAllowed() {
		return cardAllowed;
	}


	public void setCardAllowed(Boolean cardAllowed) {
		this.cardAllowed = cardAllowed;
	}


	public Boolean getChequeDDAllowed() {
		return chequeDDAllowed;
	}


	public void setChequeDDAllowed(Boolean chequeDDAllowed) {
		this.chequeDDAllowed = chequeDDAllowed;
	}


	public InstrumentHeader getInstrHeaderCash() {
		return instrHeaderCash;
	}


	public void setInstrHeaderCash(InstrumentHeader instrHeaderCash) {
		this.instrHeaderCash = instrHeaderCash;
	}


	public InstrumentHeader getInstrHeaderCard() {
		return instrHeaderCard;
	}


	public void setInstrHeaderCard(InstrumentHeader instrHeaderCard) {
		this.instrHeaderCard = instrHeaderCard;
	}
	
	public BigDecimal getCashOrCardInstrumenttotal() {
		return cashOrCardInstrumenttotal;
	}


	public void setCashOrCardInstrumenttotal(BigDecimal cashOrCardInstrumenttotal) {
		this.cashOrCardInstrumenttotal = cashOrCardInstrumenttotal;
	}


	public BigDecimal getChequeInstrumenttotal() {
		return chequeInstrumenttotal;
	}


	public void setChequeInstrumenttotal(BigDecimal chequeInstrumenttotal) {
		this.chequeInstrumenttotal = chequeInstrumenttotal;
	}


	public String getInstrumentTypeCashOrCard() {
		return instrumentTypeCashOrCard;
	}

	public void setFinancialsUtil(FinancialsUtil financialsUtil) {
		this.financialsUtil = financialsUtil;
	}


	public void setInstrumentTypeCashOrCard(String instrumentTypeCashOrCard) {
		this.instrumentTypeCashOrCard = instrumentTypeCashOrCard;
	}

	public String getActionName() {
		return actionName;
	}


	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	
	public Integer getReportId() {
		return reportId;
	}
	
	public String getSourcePage() {
		return sourcePage;
	}

	public void setSourcePage(String sourcePage) {
		this.sourcePage = sourcePage;
	}


	/**
	 * @return the designationId
	 */
	public Integer getDesignationId() {
		return designationId;
	}


	/**
	 * @param designationId the designationId to set
	 */
	public void setDesignationId(Integer designationId) {
		this.designationId = designationId;
	}
	
	public String getVoucherNumber() {
		return voucherNumber;
	}


	public void setVoucherNumber(String voucherNumber) {
		this.voucherNumber = voucherNumber;
	}
	
	public Long[] getSelectedReceipts() {
		return selectedReceipts;
	}

	public void setSelectedReceipts(Long[] selectedReceipts) {
		this.selectedReceipts = selectedReceipts;
	}

	public String getCurrentFinancialYearId() {
		return currentFinancialYearId;
	}

	public void setCurrentFinancialYearId(String currentFinancialYearId) {
		this.currentFinancialYearId = currentFinancialYearId;
	}
	
	public String amountInWords(BigDecimal amount) {
		return NumberUtil.amountInWords(amount);
	}

	public List<InstrumentHeader> getInstrumentProxyList() {
		return instrumentProxyList;
	}

	public void setInstrumentProxyList(List<InstrumentHeader> instrumentProxyList) {
		this.instrumentProxyList = instrumentProxyList;
	}

	public int getInstrumentCount() {
		return instrumentCount;
	}

	public void setInstrumentCount(int instrumentCount) {
		this.instrumentCount = instrumentCount;
	}

}
