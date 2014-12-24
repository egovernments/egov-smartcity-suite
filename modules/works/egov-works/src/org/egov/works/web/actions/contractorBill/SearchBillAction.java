package org.egov.works.web.actions.contractorBill;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.commons.EgwStatus;
import org.egov.commons.service.CommonsService;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.DateUtils;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;
import org.egov.web.actions.SearchFormAction;
import org.egov.web.annotation.ValidationErrorPageExt;
import org.egov.works.models.contractorBill.ContractorBillRegister;
import org.egov.works.models.masters.Contractor;
import org.egov.works.models.measurementbook.MBBill;
import org.egov.works.models.measurementbook.MBDetails;
import org.egov.works.models.measurementbook.MBForCancelledBill;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.services.ContractorBillService;
import org.egov.works.services.WorkOrderService;
import org.egov.works.services.WorksService;
import org.egov.works.services.impl.ContractorBillServiceImpl;
import org.egov.works.services.impl.MeasurementBookServiceImpl;
import org.egov.works.utils.WorksConstants;

@ParentPackage("egov")
public class SearchBillAction extends SearchFormAction{
	private String status;
	private String contractor;
	private Date fromDate;
	private Date toDate;
	private String workordercode;
	private String billno;
	private WorkOrderService workOrderService;
	private List<ContractorBillRegister> results=new LinkedList<ContractorBillRegister>();
	private String workorderid;
	private Long contractorId;
	private EmployeeService employeeService;
	private ContractorBillService contractorBillService;
	private WorksService worksService;
	private CommonsService commonsService;
	private Map<String, Object> criteriaMap=null;
	private static final String BILL_MODULE_KEY = "CONTRACTORBILL";
	private static final String NEW_STATUS = "NEW";
	private static final String DATE_FORMAT_DD_MMM_YYYY = "dd-MMM-yyyy";
	
	private ContractorBillRegister contractorBillRegister = new ContractorBillRegister(); 
	private MeasurementBookServiceImpl measurementBookService;
	private PersistenceService<MBForCancelledBill,Long> cancelBillService;
	private Long contractorBillId;
	private String messageKey;
	private String sourcePage; 
	private String billNumber;
	
	//@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<EgwStatus> getBillStatuses() {
		List<EgwStatus> statusList = commonsService.getStatusByModule(BILL_MODULE_KEY);
		List<EgwStatus> latestStatusList = new ArrayList<EgwStatus>();
		if(!statusList.isEmpty()){
			for(EgwStatus egwStatus : statusList){
				if(!egwStatus.getCode().equals(NEW_STATUS))
					latestStatusList.add(egwStatus);
			}
		}
		return latestStatusList;
	}
	
	public String search(){	
		  return "search";
	}
	
	public String edit(){
		return searchBill();
	}
	
	public String execute(){
		return searchBill();
	}
	public String searchBill(){
		criteriaMap = new HashMap<String, Object>(); 
		boolean errors=false;
		if(StringUtils.isNotBlank(getWorkordercode()))
			criteriaMap.put(ContractorBillServiceImpl.WORKORDER_NO, getWorkordercode()); 
		if(getContractorId()!=null && getContractorId()!=-1)
			criteriaMap.put(ContractorBillServiceImpl.CONTRACTOR_ID, getContractorId());
		if(StringUtils.isNotBlank(getBillno()))
			criteriaMap.put(ContractorBillServiceImpl.BILLNO, getBillno());
		if(fromDate!=null && toDate!=null && !DateUtils.compareDates(getToDate(),getFromDate())) {
			errors=true;
			throw new ValidationException(Arrays.asList(new ValidationError("greaterthan.endDate.fromDate",getText("greaterthan.endDate.fromDate"))));
			//addFieldError("fromDate",getText("greaterthan.endDate.fromDate"));			
		}
		if(toDate!=null && !DateUtils.compareDates(new Date(),getToDate())) {
			errors=true;
			throw new ValidationException(Arrays.asList(new ValidationError("greaterthan.endDate.currentdate",getText("greaterthan.endDate.currentdate"))));
			//addFieldError("toDate",getText("greaterthan.endDate.currentdate"));
		}
					
		if(fromDate!=null && toDate==null){
			criteriaMap.put(ContractorBillServiceImpl.FROM_DATE,new Date(DateUtils.getFormattedDate(getFromDate(),DATE_FORMAT_DD_MMM_YYYY )));
		}else if(toDate!=null && fromDate==null){
			 criteriaMap.put(ContractorBillServiceImpl.TO_DATE,new Date(DateUtils.getFormattedDate(getToDate(),DATE_FORMAT_DD_MMM_YYYY )));
		}else if(fromDate!=null && toDate!=null && errors==false){
			criteriaMap.put(ContractorBillServiceImpl.FROM_DATE, new Date(DateUtils.getFormattedDate(getFromDate(),DATE_FORMAT_DD_MMM_YYYY )));
		    criteriaMap.put(ContractorBillServiceImpl.TO_DATE,new Date(DateUtils.getFormattedDate(getToDate(),DATE_FORMAT_DD_MMM_YYYY )));
		}		
		
		if(getStatus()!=null && !getStatus().equals("-1"))
			criteriaMap.put(ContractorBillServiceImpl.BILLSTATUS, getStatus()); 
		
		List<ContractorBillRegister> contractorBillList=null;
		setPageSize(WorksConstants.PAGE_SIZE);
		super.search();
		if(searchResult.getFullListSize() !=0){
			contractorBillList = getPositionAndUser(searchResult.getList());
		    searchResult.getList().clear();
		    searchResult.getList().addAll(contractorBillList);
		}
		
		return "search";
	}
	
	public void prepare(){	
		super.prepare();
		if("cancelBill".equals(sourcePage)) {
			setStatus(ContractorBillRegister.BillStatus.APPROVED.toString());
		}
		addDropdownData("ContractorList",workOrderService.getAllContractorForWorkOrder());
	}
	
	@ValidationErrorPageExt(action = "search", makeCall = true, toMethod = "searchBill")
	public String cancelApprovedBill() throws Exception{  
		contractorBillRegister= contractorBillService.findById(contractorBillId, false);
		List<MBHeader> mbHeaderListForBillId  = measurementBookService.findAllByNamedQuery("getAllMBsForBillId", contractorBillRegister.getId());
		boolean isPartRateApplicable=false;
		List<MBHeader> partRateAppliedMBHeader=new ArrayList<MBHeader>();
		List<MBBill> validBills=new ArrayList<MBBill>();
		for(MBHeader mbObj : mbHeaderListForBillId)
	    {
			for(MBDetails mbd:mbObj.getMbDetails()){
				if(mbd.getPartRate()>0){
					isPartRateApplicable=true;
					if(!partRateAppliedMBHeader.contains(mbObj)){
						partRateAppliedMBHeader.add(mbObj);
					}
				}
			}
	    }
		for(MBHeader mbH : partRateAppliedMBHeader){
			for(MBBill mbBill:mbH.getMbBills()){
				if (mbBill.getEgBillregister().getBillstatus()!=null && !mbBill.getEgBillregister().getBillstatus().equalsIgnoreCase("CANCELLED"))
					validBills.add(mbBill);
			}
			if(validBills.size()>1){
				if(validBills.get(0).getId()>validBills.get(1).getId()){
					if(contractorBillId.equals(validBills.get(1).getEgBillregister().getId())){
						String msg="This bill cannot be cancelled as one or more MB(s) is associated with another bill with part rate. ";
						throw new ValidationException(Arrays.asList(new ValidationError(null,msg)));
					}
				}
				else if(validBills.get(0).getId()<validBills.get(1).getId()){
					if(contractorBillId.equals(validBills.get(0).getEgBillregister().getId())){
						String msg="This bill cannot be cancelled as one or more MB(s) is associated with another bill with part rate. ";
						throw new ValidationException(Arrays.asList(new ValidationError(null,msg)));
					}
				}
			}
		}
		
		contractorBillRegister.setStatus(commonsService.getStatusByModuleAndCode(BILL_MODULE_KEY,ContractorBillRegister.BillStatus.CANCELLED.toString()));
		contractorBillRegister.setBillstatus(ContractorBillRegister.BillStatus.CANCELLED.toString());
		contractorBillRegister.getCurrentState().getPrevious().setValue(ContractorBillRegister.BillStatus.CANCELLED.toString());
		
		PersonalInformation prsnlInfo=employeeService.getEmpForUserId(Integer.valueOf(getLoggedInUserId()));			
		String empName="";
		if(prsnlInfo.getEmployeeFirstName()!=null)
			empName=prsnlInfo.getEmployeeFirstName();
		if(prsnlInfo.getEmployeeLastName()!=null)
			empName=empName.concat(" ").concat(prsnlInfo.getEmployeeLastName()); 			
		contractorBillRegister.getCurrentState().getPrevious().setText1("Contractor Bill cancelled by: "+empName);
		cancel();
		billNumber=contractorBillRegister.getBillnumber();
		messageKey="contractorBill."+contractorBillRegister.getBillstatus(); 
		return SUCCESS;
	}
	
	private void cancel(){
		List<MBHeader> mbHeaderListForBillId  = measurementBookService.findAllByNamedQuery("getAllMBsForBillId", contractorBillRegister.getId());								
		for(MBHeader mbObj : mbHeaderListForBillId)
	    {
			for(MBBill mbBill:mbObj.getMbBills()){
				if(mbBill.getEgBillregister().getId().equals(contractorBillRegister.getId())){
			MBForCancelledBill mbCB=new MBForCancelledBill();
	       	mbCB.setEgBillregister(mbBill.getEgBillregister());
	    	mbCB.setMbHeader(mbObj);
	    	cancelBillService.persist(mbCB);
	    }
			}
	    }
	}	
	
	public int getLoggedInUserId() {
		return Integer.parseInt(EGOVThreadLocals.getUserId());
	}
	
	public String getContractor() {
		return contractor;
	}
	
	public String getWorkordercode() {
		return workordercode;
	}

	public String getBillno() {
		return billno;
	}

	public void setContractor(String contractor) {
		this.contractor = contractor;
	}

	public void setWorkordercode(String workordercode) {
		this.workordercode = workordercode;
	}


	public void setBillno(String billno) {
		this.billno = billno;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}

	public void setWorkOrderService(WorkOrderService workOrderService) {
		this.workOrderService = workOrderService;
	}
	
	public List<ContractorBillRegister> getResults() {
		return results;
	}
	
	public String getWorkorderid() {
		return workorderid;
	}


	public void setWorkorderid(String workorderid) {
		this.workorderid = workorderid;
	}

	public Long getContractorId(){
		return contractorId;
	}


	public void setContractorId(Long contractorId) {
		this.contractorId = contractorId;
	}
	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
	protected List<ContractorBillRegister> getPositionAndUser(List<ContractorBillRegister> results){
		List<ContractorBillRegister> billList = new ArrayList<ContractorBillRegister>();
		for(ContractorBillRegister br :results){
			PersonalInformation emp = null;
			if(br.getCurrentState()!=null)
				emp =employeeService.getEmployeeforPosition(br.getCurrentState().getOwner());
			if(emp!=null)
				br.setOwner(emp.getEmployeeName());
			billList.add(br);
			String actions = worksService.getWorksConfigValue("BILL_SHOW_ACTIONS");
			if(StringUtils.isNotBlank(actions)){
				List<String> showBillActionsList=Arrays.asList(actions.split(","));
				for(String action:showBillActionsList){
					if(action.equalsIgnoreCase(WorksConstants.BILL_ACTION_VIEWCOMPLETIONCERTIFICATE) && br.getBilltype().equals(worksService.getWorksConfigValue(WorksConstants.BILL_TYPE_FINALBILL)) ){
						br.getBillActions().add(action);
					}
					if(action.equalsIgnoreCase(WorksConstants.BILL_ACTION_VIEWCONTRACTCERTIFICATE) && !br.getBilltype().equals(worksService.getWorksConfigValue(WorksConstants.BILL_TYPE_FINALBILL)) ){
						br.getBillActions().add(action);
					}
					else if(!action.equalsIgnoreCase(WorksConstants.BILL_ACTION_VIEWCOMPLETIONCERTIFICATE) && !action.equalsIgnoreCase(WorksConstants.BILL_ACTION_VIEWCONTRACTCERTIFICATE)){
						br.getBillActions().add(action);
				}
				}
				
			}
			// To get workorder ID by passing work order number.
			WorkOrder workOrderObj=(WorkOrder) getPersistenceService().find("from WorkOrder where workOrderNumber = ?", br.getWorkordernumber());
			br.setWorkOrderId(workOrderObj.getId());
		}	
		return billList;
	}
	
	 public Map<String,Object> getContractorForApprovedWorkOrder() {
			Map<String,Object> contractorsWithWOList = new HashMap<String, Object>();		
			if(workOrderService.getContractorsWithWO()!=null) {
				for(Contractor contractor :workOrderService.getContractorsWithWO()){ 
					contractorsWithWOList.put(contractor.getId()+"", contractor.getCode()+" - "+contractor.getName());
				}			
			}
			return contractorsWithWOList; 
		}


	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}


	public WorksService getWorksService() {
		return worksService;
	}


	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}


	public void setContractorBillService(ContractorBillService contractorBillService) {
		this.contractorBillService = contractorBillService;
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	} 

	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder){
		String dynQuery = " from ContractorBillRegister cbr where cbr.id != null and cbr.state.value!='NEW' " ;
		List<Object> paramList = new ArrayList<Object>();

	     if(criteriaMap.get("WORKORDER_NO") != null){
			dynQuery = dynQuery + " and cbr.workordernumber like '%"
								+ criteriaMap.get("WORKORDER_NO")
								+ "%'";
}
		if(criteriaMap.get("CONTRACTOR_ID") != null && !"-1".equals(criteriaMap.get("CONTRACTOR_ID"))) {
			dynQuery = dynQuery + " and (cbr.id in (select mbBills.egBillregister.id from MBHeader mbh left join mbh.mbBills mbBills where mbBills.egBillregister.id=cbr.id and mbh.workOrder.contractor.id = ?)" + " OR cbr.id in (select mbcb.egBillregister.id from MBForCancelledBill mbcb where mbcb.egBillregister.id=cbr.id and mbcb.mbHeader.workOrder.contractor.id = ?))";
			paramList.add(criteriaMap.get("CONTRACTOR_ID"));
			paramList.add(criteriaMap.get("CONTRACTOR_ID"));
			
		}
		if(criteriaMap.get("FROM_DATE") != null && criteriaMap.get("TO_DATE")==null) {
			dynQuery = dynQuery + " and cbr.billdate >= ? ";
			paramList.add(criteriaMap.get("FROM_DATE"));

		}else if(criteriaMap.get("TO_DATE") != null && criteriaMap.get("FROM_DATE")==null) {
			dynQuery = dynQuery + " and cbr.billdate <= ? ";
			paramList.add(criteriaMap.get("TO_DATE"));
		}else if(criteriaMap.get("FROM_DATE") != null && criteriaMap.get("TO_DATE")!=null){
			dynQuery = dynQuery + " and cbr.billdate between ? and ? ";
			paramList.add(criteriaMap.get("FROM_DATE"));
			paramList.add(criteriaMap.get("TO_DATE"));
		}
		
		if(criteriaMap.get("BILLSTATUS") != null && !criteriaMap.get("BILLSTATUS").equals("-1")){
			if(criteriaMap.get("BILLSTATUS").equals(ContractorBillRegister.BillStatus.APPROVED.toString()) ||
					criteriaMap.get("BILLSTATUS").equals(ContractorBillRegister.BillStatus.CANCELLED.toString())){
				if("cancelBill".equals(sourcePage)){
					dynQuery = dynQuery + " and ((cbr.billstatus=? and cbr.egBillregistermis.voucherHeader is null) or ( cbr.billstatus=? and cbr.id in (select egbrm.egBillregister.id from EgBillregistermis egbrm " +
					" where egbrm.voucherHeader.status=4 and cbr.billstatus!='CANCELLED')))";
					paramList.add(criteriaMap.get("BILLSTATUS"));
					paramList.add(criteriaMap.get("BILLSTATUS"));
				}
			   else{
				dynQuery = dynQuery + " and cbr.billstatus=?";
			paramList.add(criteriaMap.get("BILLSTATUS"));
		}
		}
			else {
		if("cancelBill".equals(sourcePage)){
					dynQuery = dynQuery + " and ((cbr.billstatus=? and cbr.egBillregistermis.voucherHeader is null) or ( cbr.billstatus=? and cbr.id in (select egbrm.egBillregister.id from EgBillregistermis egbrm " +
					" where egbrm.voucherHeader.status=4 and cbr.billstatus!='CANCELLED')))";
					paramList.add(criteriaMap.get("BILLSTATUS"));
					paramList.add(criteriaMap.get("BILLSTATUS"));
				}
			   else{
				   dynQuery = dynQuery + " and cbr.billstatus=?";
					paramList.add(criteriaMap.get("BILLSTATUS"));
			   }
			}
		}
		else if("cancelBill".equals(sourcePage)){
			dynQuery = dynQuery + " and cbr.egBillregistermis.voucherHeader.status=4 and cbr.billstatus!='CANCELLED' ";
		}
		
		if(criteriaMap.get("BILLNO") != null){
			dynQuery = dynQuery + " and cbr.billnumber like '%"
			+ criteriaMap.get("BILLNO")
			+ "%'";
			//dynQuery = dynQuery + " and cbr.billnumber= ? ";
			//paramList.add(criteriaMap.get("BILLNO"));
		}
		String billSearchQuery="select distinct cbr "+	dynQuery;
		String countQuery = "select distinct count(cbr) " + dynQuery;
		return new SearchQueryHQL(billSearchQuery, countQuery, paramList);
	}

	public String getSourcePage() {
		return sourcePage;
	}

	public void setSourcePage(String sourcePage) {
		this.sourcePage = sourcePage;
	}


	public void setMeasurementBookService(
			MeasurementBookServiceImpl measurementBookService) {
		this.measurementBookService = measurementBookService;
	}

	public void setCancelBillService(
			PersistenceService<MBForCancelledBill, Long> cancelBillService) {
		this.cancelBillService = cancelBillService;
	}

	public Long getContractorBillId() {
		return contractorBillId;
	}

	public void setContractorBillId(Long contractorBillId) {
		this.contractorBillId = contractorBillId;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

}
