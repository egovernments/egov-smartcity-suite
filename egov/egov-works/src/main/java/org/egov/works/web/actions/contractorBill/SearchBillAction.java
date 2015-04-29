package org.egov.works.web.actions.contractorBill;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.egov.commons.EgwStatus;
import org.egov.commons.service.CommonsService;
import org.egov.infstr.services.Page;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.DateUtils;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPageExt;
import org.egov.web.utils.EgovPaginatedList;
import org.egov.works.models.contractorBill.ContractorBillRegister;
import org.egov.works.models.masters.Contractor;
import org.egov.works.models.measurementbook.MBForCancelledBill;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.services.ContractorBillService;
import org.egov.works.services.WorkOrderService;
import org.egov.works.services.WorksService;
import org.egov.works.services.impl.ContractorBillServiceImpl;
import org.egov.works.services.impl.MeasurementBookServiceImpl;
import org.egov.works.utils.WorksConstants;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;

public class SearchBillAction extends BaseFormAction{
	private String status;
	private String contractor;
	private Date fromDate;
	private Date toDate;
	private String workordercode;
	private String billno;
	private WorkOrderService workOrderService;
	private String workorderid;
	private Long contractorId;
	@Autowired
        private EmployeeService employeeService;
	private ContractorBillService contractorBillService;
	private WorksService worksService;
	@Autowired
        private CommonsService commonsService;
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
	private String cancellationReason;
	private String cancelRemarks;
	private Integer execDeptid;
	private Integer billDeptId;
	private Integer page=1;
	private Integer pageSize=30;
	private EgovPaginatedList pagedResults;
	List<ContractorBillRegister> contractorBillList =null;
	private String estimateNo;
	
	
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
		Map<String, Object> criteriaMap = new HashMap<String, Object>(); 
		List<Object> paramList = new ArrayList<Object>();
		List<String> qryObj = new ArrayList<String>();
		Page resPage;
		Long count;
		Object[] params;
		if(StringUtils.isNotBlank(getWorkordercode()))
			criteriaMap.put(ContractorBillServiceImpl.WORKORDER_NO, getWorkordercode()); 
		if(getContractorId()!=-1)
			criteriaMap.put(ContractorBillServiceImpl.CONTRACTOR_ID, getContractorId());
		if(StringUtils.isNotBlank(getBillno()))
			criteriaMap.put(ContractorBillServiceImpl.BILLNO, getBillno());
		if(fromDate!=null && toDate!=null && !DateUtils.compareDates(getToDate(),getFromDate()))
			addFieldError("fromDate",getText("greaterthan.endDate.fromDate"));
		if(toDate!=null && !DateUtils.compareDates(new Date(),getToDate()))
			addFieldError("toDate",getText("greaterthan.endDate.currentdate"));
					
		if(fromDate!=null && toDate==null){
			criteriaMap.put(ContractorBillServiceImpl.FROM_DATE,new Date(DateUtils.getFormattedDate(getFromDate(),DATE_FORMAT_DD_MMM_YYYY )));
		}else if(toDate!=null && fromDate==null){
			 criteriaMap.put(ContractorBillServiceImpl.TO_DATE,new Date(DateUtils.getFormattedDate(getToDate(),DATE_FORMAT_DD_MMM_YYYY )));
		}else if(fromDate!=null && toDate!=null && getFieldErrors().isEmpty()){
			criteriaMap.put(ContractorBillServiceImpl.FROM_DATE, new Date(DateUtils.getFormattedDate(getFromDate(),DATE_FORMAT_DD_MMM_YYYY )));
		    criteriaMap.put(ContractorBillServiceImpl.TO_DATE,new Date(DateUtils.getFormattedDate(getToDate(),DATE_FORMAT_DD_MMM_YYYY )));
		}	
		
		if(execDeptid != null && execDeptid != -1)
			criteriaMap.put(ContractorBillServiceImpl.EXEC_DEPT_ID, execDeptid);
		
		if(billDeptId != null && billDeptId != -1)
			criteriaMap.put(ContractorBillServiceImpl.BILL_DEPT_ID, billDeptId);
		
		if(estimateNo != null && !"".equalsIgnoreCase(estimateNo))
			criteriaMap.put(ContractorBillServiceImpl.EST_NO, estimateNo);
		
		if(!getStatus().equals("-1"))
			criteriaMap.put(ContractorBillServiceImpl.BILLSTATUS, getStatus()); 
		
		qryObj= contractorBillService.searchContractorBill(criteriaMap, paramList);
		String qry=qryObj.get(0);
		if(paramList.isEmpty()){
			params=null;
			count = (Long)persistenceService.find(qryObj.get(0));
			Query qry1=persistenceService.getSession().createQuery(qry);
			resPage=new Page(qry1,page,pageSize);  
		 }else{
			 params = new Object[paramList.size()];
			 params = paramList.toArray(params);
			 count = (Long)persistenceService.find(qryObj.get(1),params);
			 resPage= persistenceService.findPageBy(qry,page,pageSize,params);  
		 }
		pagedResults = new EgovPaginatedList(resPage, count.intValue());
		contractorBillList=	(pagedResults!=null?pagedResults.getList():null);
		
		if(!contractorBillList.isEmpty() )
			contractorBillList = getPositionAndUser(contractorBillList);
		
		pagedResults.setList(contractorBillList);
		return "search";
	}
	
	public void prepare(){	
		super.prepare();
		addDropdownData("ContractorList",workOrderService.getAllContractorForWorkOrder());
		List departmentList = getPersistenceService().findAllBy("from DepartmentImpl");
		addDropdownData("billDepartmentList", departmentList);
		addDropdownData("executingDepartmentList", departmentList);
		if("cancelBill".equals(sourcePage)) {
			setStatus(ContractorBillRegister.BillStatus.APPROVED.toString());
		}
	}
	
	@ValidationErrorPageExt(action = "search", makeCall = true, toMethod = "searchBill")
	public String cancelApprovedBill() throws Exception{  
		contractorBillRegister= contractorBillService.findById(contractorBillId, false);
		List<MBHeader> mbHeaderListForBillId  = measurementBookService.findAllByNamedQuery("getAllMBsForBillId", 
				MBHeader.MeasurementBookStatus.APPROVED.toString(),	contractorBillRegister.getId());
		String cancellationText = null;
		for(MBHeader mbObj : mbHeaderListForBillId) {
			MBForCancelledBill mbCB = new MBForCancelledBill();
	       	mbCB.setEgBillregister(mbObj.getEgBillregister());
	    	mbCB.setMbHeader(mbObj);
	    	cancelBillService.persist(mbCB);			
	    }
				
		contractorBillRegister.setStatus(commonsService.getStatusByModuleAndCode(BILL_MODULE_KEY,ContractorBillRegister.BillStatus.CANCELLED.toString()));
		contractorBillRegister.setBillstatus(ContractorBillRegister.BillStatus.CANCELLED.toString());
		
		PersonalInformation prsnlInfo=employeeService.getEmpForUserId(worksService.getCurrentLoggedInUserId());			
		String empName="";
		if(prsnlInfo.getEmployeeFirstName()!=null)
			empName=prsnlInfo.getEmployeeFirstName();
		if(prsnlInfo.getEmployeeLastName()!=null)
			empName=empName.concat(" ").concat(prsnlInfo.getEmployeeLastName()); 	
		if(cancelRemarks != null  && StringUtils.isNotBlank(cancelRemarks))
			cancellationText=cancellationReason+" : "+cancelRemarks+". Contractor Bill cancelled by: "+empName;
		else
			cancellationText=cancellationReason+". Contractor Bill cancelled by: "+empName;
		
		//TODO - The setter methods of variables in State.java are protected. Need to alternative way to solve this issue.
		//Set the status and workflow state to cancelled
		/*State oldEndState = contractorBillRegister.getCurrentState();
		Position owner = prsnlInfo.getAssignment(new Date()).getPosition();
		oldEndState.setCreatedBy(prsnlInfo.getUserMaster());
		oldEndState.setLastModifiedBy(prsnlInfo.getUserMaster());
		oldEndState.setCreatedDate(new DateTime());
		oldEndState.setLastModifiedDate(new DateTime());
		oldEndState.setOwner(owner);
		oldEndState.setValue(WorksConstants.CANCELLED_STATUS);
		oldEndState.setText1(cancellationText);
		
		contractorBillRegister.changeState("END", owner, null);*/
		
		billNumber=contractorBillRegister.getBillnumber();
		messageKey="contractorBill."+contractorBillRegister.getBillstatus(); 
		return SUCCESS;
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
			if(!br.getStatus().getCode().equalsIgnoreCase(WorksConstants.APPROVED) && !br.getStatus().getCode().equalsIgnoreCase(WorksConstants.CANCELLED_STATUS)){
				if(br.getCurrentState()!=null)
					emp =employeeService.getEmployeeforPosition(br.getCurrentState().getOwnerPosition());
				if(emp!=null && StringUtils.isNotBlank(emp.getEmployeeName()))
					br.setOwner(emp.getEmployeeName());
			}
			
			billList.add(br);
			String actions = worksService.getWorksConfigValue("BILL_SHOW_ACTIONS");
			if(StringUtils.isNotBlank(actions)){
				List<String> showBillActionsList=Arrays.asList(actions.split(","));
				for(String action:showBillActionsList){
					if(action.equalsIgnoreCase(WorksConstants.BILL_ACTION_VIEWCOMPLETIONCERTIFICATE) && br.getBilltype().equals(worksService.getWorksConfigValue(WorksConstants.BILL_TYPE_FINALBILL)) ){
						br.getBillActions().add(action);
					}
					else if(!action.equalsIgnoreCase(WorksConstants.BILL_ACTION_VIEWCOMPLETIONCERTIFICATE)){
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

	public String getFinalBillTypeConfigValue() {		
		return worksService.getWorksConfigValue("FinalBillType");
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

	public void setMeasurementBookService(
			MeasurementBookServiceImpl measurementBookService) {
		this.measurementBookService = measurementBookService;
	}

	public void setCancelBillService(
			PersistenceService<MBForCancelledBill, Long> cancelBillService) {
		this.cancelBillService = cancelBillService;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public String getCancellationReason() {
		return cancellationReason;
	}

	public void setCancellationReason(String cancellationReason) {
		this.cancellationReason = cancellationReason;
	}

	public String getCancelRemarks() {
		return cancelRemarks;
	}

	public void setCancelRemarks(String cancelRemarks) {
		this.cancelRemarks = cancelRemarks;
	} 
	

	public ContractorBillRegister getContractorBillRegister() {
		return contractorBillRegister;
	}

	public void setContractorBillRegister(
			ContractorBillRegister contractorBillRegister) {
		this.contractorBillRegister = contractorBillRegister;
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

	public String getSourcePage() {
		return sourcePage;
	}

	public void setSourcePage(String sourcePage) {
		this.sourcePage = sourcePage;
	}

	public Integer getExecDeptid() {
		return execDeptid;
	}

	public void setExecDeptid(Integer execDeptid) {
		this.execDeptid = execDeptid;
	}

	public List<ContractorBillRegister> getContractorBillList() {
		return contractorBillList;
	}

	public EgovPaginatedList getPagedResults() {
		return pagedResults;
	}

	public void setPagedResults(EgovPaginatedList pagedResults) {
		this.pagedResults = pagedResults;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getBillDeptId() {
		return billDeptId;
	}

	public void setBillDeptId(Integer billDeptId) {
		this.billDeptId = billDeptId;
	}

	public String getEstimateNo() {
		return estimateNo;
	}

	public void setEstimateNo(String estimateNo) {
		this.estimateNo = estimateNo;
	}

}
