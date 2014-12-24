package org.egov.works.web.actions.retentionMoney;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.EgwStatus;
import org.egov.commons.service.CommonsService;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.models.StateAware;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.workflow.WorkflowService;
import org.egov.model.bills.EgBillregister;
import org.egov.pims.commons.Position;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;
import org.egov.web.actions.workflow.GenericWorkFlowAction;
import org.egov.works.models.masters.Contractor;
import org.egov.works.models.retentionMoney.RetentionMoneyRefund;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.RetentionMoneyRefundService;
import org.egov.works.services.WorkOrderService;
import org.egov.works.services.WorksService;
import org.egov.works.utils.WorksConstants;



@ParentPackage("egov")
public class RetentionMoneyRefundAction extends GenericWorkFlowAction{
	private static final Logger logger = Logger.getLogger(RetentionMoneyRefundAction.class);
	private static final String SAVE_ACTION = "save";
	private static final String RMR_COA_LIST = "rmcoaList";
	private static final String RM_OUTSTANDING_AMOUNT="RMOutstanding";
	private static final String RETENTIONMONEYREF_MODULE_KEY = "RetentionMoneyRefund";
	private static final String SEARCH="search";
	private static final String DATE_FORMAT="dd-MMM-yyyy";
	private static final String RETENTIONMONEYREFUND_SHOW_ACTIONS="RETENTIONMONEYREFUND_SHOW_ACTIONS";
	private static final String VIEW_SEARCH="viewSearch";
	
	private RetentionMoneyRefund retentionMoneyRefund=new RetentionMoneyRefund();
	private Long id;
	private Long workOrderId;
	private Long contractorId;
	private RetentionMoneyRefundService retentionMoneyRefundService;
	
	private Long glcodeId;
	private WorkOrder workOrder;
	private WorkflowService<RetentionMoneyRefund> retentionMoneyRefundWorkflowService;
	private String messageKey;
	private WorksService worksService;
	private String designation;
	private String employeeName;
	private PersistenceService<EgBillregister, Long> egBillregisterService;
	private String sourcepage="";
	private CommonsService commonsService;
	private double outstandingAmount;
	
	private EmployeeService employeeService;
	private AbstractEstimateService abstractEstimateService;
	private String mode="";
	private String additionalRuleValue;
	private String departmentName;
	private WorkOrderService workOrderService;
	private double totalWorkValue;
	private Long[] appConfigValueId;
	private Integer deptId;	
	private double oldRetentionMoneyRefundAmount;
	
	private String estimateNumber;
	private Date fromDate;
	private Date toDate;
	
	@Override
	public StateAware getModel() {
		return retentionMoneyRefund;
	}
	
	public RetentionMoneyRefundAction(){
		addRelatedEntity("glcode", CChartOfAccounts.class);
	}
	
	public void prepare(){
		super.prepare();
		setupDropdownDataExcluding("glcode");
		addDropdownData("executingDepartmentList",getPersistenceService().findAllBy("from DepartmentImpl order by upper(deptName)"));
		//Do nothing for search
		if(id == null && sourcepage.equalsIgnoreCase(SEARCH))
			return;
		if (id != null) {
			retentionMoneyRefund = retentionMoneyRefundService.findById(id, false);
			workOrder=retentionMoneyRefund.getWorkOrder();
			oldRetentionMoneyRefundAmount=retentionMoneyRefund.getRetentionMoneyBeingRefunded();
		}
		else{
			workOrder = (WorkOrder) persistenceService.find("from WorkOrder where id=?",workOrderId);
			Contractor contractor = workOrder.getContractor();
			retentionMoneyRefund.setContractor(contractor);
			retentionMoneyRefund.setWorkOrder(workOrder);
			departmentName = workOrder.getWorkOrderEstimates().get(0).getEstimate().getExecutingDepartment().getDeptName();
		}
		PersonalInformation emp = employeeService.getEmployeeforPosition(workOrder.getCurrentState().getOwner());
		
		if(emp != null)
			workOrder.setOwner(emp.getEmployeeName());
		
		try {
			addDropdownData(RMR_COA_LIST,retentionMoneyRefundService.getRetentionMRCOAList());
		}catch (Exception e) {
			addFieldError("glcode", "Unable to load coa list");
		}
		
		if(!getWorkFlowDepartment().equals(""))
			departmentName = getWorkFlowDepartment();
		
		BigDecimal workOrderAmount =  workOrder.getTotalWorkOrderAmount();
		totalWorkValue = (workOrderAmount==null)?0d:workOrderAmount.doubleValue();
		totalWorkValue += workOrderService.getRevisionEstimateWOAmount(workOrder).doubleValue();
	}
	
	public String newform(){
		return NEW;
	}
	
	public String viewSearch(){
		return VIEW_SEARCH;
	}
	
	public String searchList(){
		search();
		setOwnerAndActions(searchResult.getList());
		return VIEW_SEARCH;
	}
	
	private void setOwnerAndActions(List<RetentionMoneyRefund>  rsdList) {
		for(RetentionMoneyRefund retMR:rsdList)
		{
			String actions = worksService.getWorksConfigValue(RETENTIONMONEYREFUND_SHOW_ACTIONS);
			if(actions!=null && ! actions.equals(""))
				retMR.getRefundRetentionMoneyActions().addAll(Arrays.asList(actions.split(",")));
			PersonalInformation emp = employeeService.getEmployeeforPosition(retMR.getCurrentState().getOwner());
			if(emp!=null)
				retMR.setOwner(emp.getEmployeeName());
		}
	}
	
	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) {
		String query = " from RetentionMoneyRefund rmr  where rmr.id is not null ";
		WorkOrder workOrder = retentionMoneyRefund.getWorkOrder();
		List<Object> paramList = new ArrayList<Object>();
		
		if( workOrder !=null )
		{
			if(workOrder.getContractor()!= null && workOrder.getContractor().getId()!=null && workOrder.getContractor().getId()!=-1)
			{
				query =query +  " and rmr.workOrder.contractor.id = ? ";
				paramList.add(workOrder.getContractor().getId());
			}
			if(workOrder.getWorkOrderNumber()!=null && !workOrder.getWorkOrderNumber().equals(""))
			{
				query =query +  " and UPPER(rmr.workOrder.workOrderNumber) like '%"+workOrder.getWorkOrderNumber().trim().toUpperCase()+"%'";
			}
		}
		if(retentionMoneyRefund.getEgwStatus()!=null 
				&& retentionMoneyRefund.getEgwStatus().getCode()!=null
				&& !retentionMoneyRefund.getEgwStatus().getCode().equals("-1") 
				&& !retentionMoneyRefund.getEgwStatus().getCode().equals("") )
		{
			query =query +  " and rmr.egwStatus.code = ? ";
			paramList.add(retentionMoneyRefund.getEgwStatus().getCode());
		}
		if(fromDate!=null)
		{
			query =query +  " and  trunc(rmr.createdDate) >= '"+DateUtils.getFormattedDate(fromDate,DATE_FORMAT )+"' ";
		}
		if(toDate!=null)
		{
			query = query +  " and trunc(rmr.createdDate)  <= '"+DateUtils.getFormattedDate(toDate,DATE_FORMAT )+"' ";
		}
		if(deptId!=null && deptId!=-1)
		{
			query = query + " and rmr.workOrder.id in (select we.workOrder.id from WorkOrderEstimate we where we.workOrder.id=rmr.workOrder.id and " +
					" we.estimate.executingDepartment.id = ?) ";
			paramList.add(deptId);
		}
		if(estimateNumber!=null && !estimateNumber.equalsIgnoreCase(""))
		{
			query = query + "  and rmr.workOrder.id in (select distinct woe.workOrder.id from WorkOrderEstimate woe where " +
					"UPPER(woe.estimate.estimateNumber) like '%"+estimateNumber.trim().toUpperCase()+"%')";
		}
		String actions = worksService.getWorksConfigValue(RETENTIONMONEYREFUND_SHOW_ACTIONS);
		workOrder.getWorkOrderActions().addAll(Arrays.asList(actions.split(",")));
		
		setPageSize(WorksConstants.PAGE_SIZE);
		String workOrderSearchQuery="select distinct rmr "+	query;
		String countQuery = "select distinct count(rmr) " + query;
		return new SearchQueryHQL(workOrderSearchQuery, countQuery, paramList);
		
	}
	
	public String findRMOutstandingAmountAjax(){
		outstandingAmount = retentionMoneyRefundService.getTotalOutstandingRetentionMoney(workOrder,glcodeId,true);
		return RM_OUTSTANDING_AMOUNT;
	}
	public String edit(){
		return EDIT;
	}
	
	public String save() throws Exception{
		
		String actionName = parameters.get("actionName")[0]; 
		try{
			retentionMoneyRefund.setAdditionalWfRule(getAdditionalRule());
			retentionMoneyRefund.setAmountWfRule(getAmountRule());
			String curStatus;
			if(retentionMoneyRefund.getCurrentState()!=null){
				curStatus=retentionMoneyRefund.getCurrentState().getValue(); 
			}
			else
				curStatus="NEW";
			if((actionName.equalsIgnoreCase("Forward")||actionName.equalsIgnoreCase("Approve")) && customizedWorkFlowService.getWfMatrix(getModel().getStateType(), getWorkFlowDepartment(), getAmountRule(), getAdditionalRule(),curStatus, getPendingActions())==null){
				throw new ValidationException(Arrays.asList(new ValidationError("retentionmr.workflow.notdefined",getText("retentionmr.workflow.notdefined",new String[]{getWorkFlowDepartment()}))));
			} 
		
			if(actionName.equalsIgnoreCase("save")){
				retentionMoneyRefund.setEgwStatus(commonsService.getStatusByModuleAndCode(RETENTIONMONEYREF_MODULE_KEY,"NEW"));
				if(id ==null){
					Position pos = employeeService.getPositionforEmp(employeeService.getEmpForUserId(abstractEstimateService.getCurrentUserId()).getIdPersonalInformation());
					retentionMoneyRefund = (RetentionMoneyRefund)retentionMoneyRefundWorkflowService.start(retentionMoneyRefund,pos, "Retention Money Refund Created.");
				}
				messageKey="rmr."+actionName;
				addActionMessage(getText(messageKey,"The Retention Money Refund was saved successfully."));
				retentionMoneyRefund = retentionMoneyRefundService.persist(retentionMoneyRefund);
				getDesignation(retentionMoneyRefund);
			}
			else if(mode!=null && !mode.equals("edit")){
				
				if(id==null){
					retentionMoneyRefund.setEgwStatus(commonsService.getStatusByModuleAndCode(RETENTIONMONEYREF_MODULE_KEY,"NEW"));
					Position pos = employeeService.getPositionforEmp(employeeService.getEmpForUserId(abstractEstimateService.getCurrentUserId()).getIdPersonalInformation());
					retentionMoneyRefund = (RetentionMoneyRefund) retentionMoneyRefundWorkflowService.start(retentionMoneyRefund, pos, "Retention Money Refund Created.");
				}
				retentionMoneyRefund.getApproverPositionId();
				retentionMoneyRefundWorkflowService.transition(actionName, retentionMoneyRefund, approverComments);
				retentionMoneyRefund = retentionMoneyRefundService.persist(retentionMoneyRefund);
				messageKey="rmr."+actionName;
				getDesignation(retentionMoneyRefund);
			}
			workOrder.setRetentionMoneyFromBill(retentionMoneyRefundService.getTotalOutstandingRetentionMoney(workOrder,retentionMoneyRefund.getGlcode().getId(),false));
			if(actionName.equalsIgnoreCase("Cancel"))
				workOrder.setRetentionMoneyRefunded(workOrder.getRetentionMoneyRefunded()-retentionMoneyRefund.getRetentionMoneyBeingRefunded());
			else
				workOrder.setRetentionMoneyRefunded(workOrder.getRetentionMoneyRefunded()-oldRetentionMoneyRefundAmount+retentionMoneyRefund.getRetentionMoneyBeingRefunded());
			
			if(actionName.equalsIgnoreCase("Approve")){
				
				EgBillregister expenseBill = retentionMoneyRefundService.createExpenseBill(retentionMoneyRefund);
				expenseBill=egBillregisterService.persist(expenseBill);
				egBillregisterService.getSession().flush();
				egBillregisterService.getSession().refresh(expenseBill);
				expenseBill.getEgBillregistermis().setSourcePath("/egworks/retentionMoney/retentionMoneyRefund!edit.action?id="+retentionMoneyRefund.getId()+"&sourcepage=search");
				retentionMoneyRefund.setEgBillregister(expenseBill);
				messageKey=getText("rmr.generatedbill.number",new String[]{expenseBill.getBillnumber()});
				
			}
		}
		catch(Exception e)
		{
			logger.error("Error-- RetentionMoneyRefundAction --save--"+e.getMessage());
		}
		return SAVE_ACTION.equalsIgnoreCase(actionName)?EDIT:SUCCESS;
	}
	
	protected String getAdditionalRule() {
		if(departmentName.equals("Garden"))
			additionalRuleValue=retentionMoneyRefund.getWorkOrder().getWorkOrderEstimates().get(0).getEstimate().getParentCategory().getCode().equals("CW")?"CivilWorks":retentionMoneyRefund.getWorkOrder().getWorkOrderEstimates().get(0).getEstimate().getParentCategory().getCode().equals("HW")?"HorticultureWork":"";
		return additionalRuleValue;
	}
	
	public void getDesignation(RetentionMoneyRefund retentionMoneyRefund){
		/* start for customizing workflow message display */
		if(retentionMoneyRefund.getCurrentState()!= null 
				&& !"NEW".equalsIgnoreCase(retentionMoneyRefund.getCurrentState().getValue())) {
			String result = worksService.getEmpNameDesignation(retentionMoneyRefund.getState().getOwner(),retentionMoneyRefund.getState().getCreatedDate());
			if(result != null && !"@".equalsIgnoreCase(result)) {
				String empName = result.substring(0,result.lastIndexOf('@'));
				String designation =result.substring(result.lastIndexOf('@')+1,result.length());
				setEmployeeName(empName);
				setDesignation(designation);
			}
		}
		/* end */	
	}
	
	public String cancel(){
		String actionName = parameters.get("actionName")[0]; 
		if(retentionMoneyRefund.getId()!=null){
			retentionMoneyRefundWorkflowService.transition(actionName, retentionMoneyRefund,approverComments);
			retentionMoneyRefund = retentionMoneyRefundService.persist(retentionMoneyRefund);
		}
		messageKey="rateContract.Cancel";		
		return SUCCESS;
	} 
	
	public List<EgwStatus> getRMRefudStatuses() {
		List<EgwStatus> rmrStatusList=commonsService.getStatusByModule(RetentionMoneyRefund.class.getSimpleName());
		rmrStatusList.remove(commonsService.getStatusByModuleAndCode(RetentionMoneyRefund.class.getSimpleName(), "NEW"));
		return rmrStatusList;
	}
	
	public Map<String,Object> getContractorForApprovedWorkOrder() {
		Map<String,Object> contractorsWithWOList = new LinkedHashMap<String, Object>();		
		if(workOrderService.getContractorsWithWO()!=null) {
			for(Contractor contractor :workOrderService.getContractorsWithWO()){
				contractorsWithWOList.put(contractor.getId()+"", contractor.getCode()+" - "+contractor.getName());
			}			
		}
		return contractorsWithWOList; 
	} 
	
	public String getPendingActions()
	{
		return retentionMoneyRefund==null?"":
			(retentionMoneyRefund.getCurrentState()==null?"":retentionMoneyRefund.getCurrentState().getNextAction());
	}

	public String getWorkFlowDepartment(){
		if(retentionMoneyRefund == null || retentionMoneyRefund.getWorkOrder() == null
			|| retentionMoneyRefund.getWorkOrder().getWorkOrderEstimates() == null
			|| retentionMoneyRefund.getWorkOrder().getWorkOrderEstimates().get(0).getEstimate() == null
			|| retentionMoneyRefund.getWorkOrder().getWorkOrderEstimates().get(0).getEstimate().getExecutingDepartment() == null)
			
			return "";
		else
			return retentionMoneyRefund.getWorkOrder().getWorkOrderEstimates().get(0).getEstimate().getExecutingDepartment().getDeptName();
	}
	
	
	protected BigDecimal getAmountRule() {
		return retentionMoneyRefund==null?null:(new BigDecimal(retentionMoneyRefund.getRetentionMoneyBeingRefunded())==null?null:(new BigDecimal(retentionMoneyRefund.getRetentionMoneyBeingRefunded())));
	}
	
	public List<EgwStatus> getRetentionMRStatuses() {
		List<EgwStatus> rmrStatusList=commonsService.getStatusByModule(RetentionMoneyRefund.class.getSimpleName());
		rmrStatusList.remove(commonsService.getStatusByModuleAndCode(RetentionMoneyRefund.class.getSimpleName(), "NEW"));
		return rmrStatusList;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getWorkOrderId() {
		return workOrderId;
	}
	public void setWorkOrderId(Long workOrderId) {
		this.workOrderId = workOrderId;
	}
	public Long getContractorId() {
		return contractorId;
	}
	public void setContractorId(Long contractorId) {
		this.contractorId = contractorId;
	}
	public Long getGlcodeId() {
		return glcodeId;
	}
	public void setGlcodeId(Long glcodeId) {
		this.glcodeId = glcodeId;
	}
	public WorkOrder getWorkOrder() {
		return workOrder;
	}
	public void setWorkOrder(WorkOrder workOrder) {
		this.workOrder = workOrder;
	}
	public String getMessageKey() {
		return messageKey;
	}
	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}
	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}
	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public void setEgBillregisterService(
			PersistenceService<EgBillregister, Long> egBillregisterService) {
		this.egBillregisterService = egBillregisterService;
	}
	public String getSourcepage() {
		return sourcepage;
	}
	public void setSourcepage(String sourcepage) {
		this.sourcepage = sourcepage;
	}
	public CommonsService getCommonsService() {
		return commonsService;
	}
	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}
	public EmployeeService getEmployeeService() {
		return employeeService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public void setAbstractEstimateService(
			AbstractEstimateService abstractEstimateService) {
		this.abstractEstimateService = abstractEstimateService;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getAdditionalRuleValue() { 
		getAdditionalRule();
		return additionalRuleValue;
	}
	public void setAdditionalRuleValue(String additionalRuleValue) {
		this.additionalRuleValue = additionalRuleValue;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public double getTotalWorkValue() {
		return totalWorkValue;
	}
	public void setTotalWorkValue(double totalWorkValue) {
		this.totalWorkValue = totalWorkValue;
	}
	public void setWorkOrderService(WorkOrderService workOrderService) {
		this.workOrderService = workOrderService;
	}
	public Long[] getAppConfigValueId() {
		return appConfigValueId;
	}

	public void setAppConfigValueId(Long[] appConfigValueId) {
		this.appConfigValueId = appConfigValueId;
	}
	public Integer getDeptId() {
		return deptId;
	}
	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}
	public String getEstimateNumber() {
		return estimateNumber;
	}
	public void setEstimateNumber(String estimateNumber) {
		this.estimateNumber = estimateNumber;
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
	public double getOldRetentionMoneyRefundAmount() {
		return oldRetentionMoneyRefundAmount;
	}
	public void setOldRetentionMoneyRefundAmount(
			double oldRetentionMoneyRefundAmount) {
		this.oldRetentionMoneyRefundAmount = oldRetentionMoneyRefundAmount;
	}
	public RetentionMoneyRefundService getRetentionMoneyRefundService() {
		return retentionMoneyRefundService;
	}
	public void setRetentionMoneyRefundService(
			RetentionMoneyRefundService retentionMoneyRefundService) {
		this.retentionMoneyRefundService = retentionMoneyRefundService;
	}
	public double getOutstandingAmount() {
		return outstandingAmount;
	}
	public void setOutstandingAmount(double outstandingAmount) {
		this.outstandingAmount = outstandingAmount;
	}
	public RetentionMoneyRefund getRetentionMoneyRefund() {
		return retentionMoneyRefund;
	}
	public void setRetentionMoneyRefund(RetentionMoneyRefund retentionMoneyRefund) {
		this.retentionMoneyRefund = retentionMoneyRefund;
	}
	public void setRetentionMoneyRefundWorkflowService(
			WorkflowService<RetentionMoneyRefund> retentionMoneyRefundWorkflowService) {
		this.retentionMoneyRefundWorkflowService = retentionMoneyRefundWorkflowService;
	}
	
}
