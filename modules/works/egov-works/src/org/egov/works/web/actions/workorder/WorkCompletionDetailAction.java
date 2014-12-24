package org.egov.works.web.actions.workorder;

import java.util.Arrays;
import java.util.List;

import org.egov.commons.service.CommonsService;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.models.StateAware;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.workflow.WorkflowService;
import org.egov.pims.commons.Position;
import org.egov.pims.service.EmployeeService;
import org.egov.web.actions.workflow.GenericWorkFlowAction;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.securityDeposit.ReturnSecurityDeposit;
import org.egov.works.models.workorder.WorkCompletionDetail;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.WorkOrderService;
import org.egov.works.services.WorksService;
import org.egov.works.utils.WorksConstants;

public class WorkCompletionDetailAction extends GenericWorkFlowAction{
	private static final String SAVE_ACTION = "save";
	private WorkCompletionDetail workCompletionDetail=new WorkCompletionDetail();
	private WorkOrderService workOrderService;
	private WorksService worksService;
	private PersistenceService<WorkCompletionDetail, Long> workCompletionDetailService;
	private WorkflowService<WorkCompletionDetail> workCompletionDetailWorkflowService;
	private Long id;
	private CommonsService commonsService;
	private EmployeeService employeeService;
	private AbstractEstimateService abstractEstimateService;
	private String messageKey;
	private String sourcepage="";
	private String employeeName;
	private String designation;
	private Long workOrderId; 
	private WorkOrder workOrder;
	private AbstractEstimate estimate;
	private String mode="";
	private List<WorkCompletionDetail> workCompletionDetailsList;
	private String departmentName;
	
	public WorkCompletionDetailAction(){
		addRelatedEntity("workOrder", WorkOrder.class);
	}
	
	public void prepare(){

		if(id!=null){
			workCompletionDetail=workCompletionDetailService.findById(id, false);
			workOrder=workCompletionDetail.getWorkOrder();
			estimate=workOrder.getWorkOrderEstimates().get(0).getEstimate();
		}
		else if(workOrderId!=null){
			workOrder= workOrderService.findById(workOrderId, false);
			if(workOrder!=null){
				workCompletionDetail.setWorkOrder(workOrder);
				estimate=workOrder.getWorkOrderEstimates().get(0).getEstimate();
				departmentName=workOrder.getWorkOrderEstimates().get(0).getEstimate().getExecutingDepartment().getDeptName();
			}
		}
		if(getWorkFlowDepartment()!=null && !getWorkFlowDepartment().equals("")){
			departmentName=getWorkFlowDepartment();
		}
		workCompletionDetailsList=workCompletionDetailService.findAllBy("select distinct wcd from WorkCompletionDetail wcd left join wcd.state st left join st.previous prev where wcd.workOrder.id=? and (st is null or (prev is not null and prev.value=?)) order by wcd.createdDate",workOrder.getId(),"APPROVED");
		super.prepare();
		addDropdownData("executingDepartmentList",getPersistenceService().findAllBy("from DepartmentImpl order by upper(deptName)")); 
	}
	
	public String newform(){
		return NEW;
	}
	
	public String edit(){
		return EDIT;
	}
	
	public String save() {
		String actionName = parameters.get("actionName")[0]; 
		String curStatus;
		if(workCompletionDetail.getCurrentState()!=null){
			curStatus=workCompletionDetail.getCurrentState().getValue(); 
		}
		else
			curStatus="NEW";
		if((actionName.equalsIgnoreCase("Forward")||actionName.equalsIgnoreCase("Approve")) && customizedWorkFlowService.getWfMatrix(getModel().getStateType(), getWorkFlowDepartment(), getAmountRule(), getAdditionalRule(),curStatus, getPendingActions())==null){
			throw new ValidationException(Arrays.asList(new ValidationError("workCompletionDetail.workflow.notdefined",getText("workCompletionDetail.workflow.notdefined",new String[]{getWorkFlowDepartment()}))));
		} 
		if(workCompletionDetailsList.isEmpty() && id==null && (workOrder.getWorkCompletionDate()!=null || workOrder.getContractPeriod()!=null || workOrder.getDefectLiabilityPeriod()>0)){
			WorkCompletionDetail originalDetail=new WorkCompletionDetail();
			originalDetail.setWorkOrder(workOrder);
			originalDetail.setWorkCompletionDate(workOrder.getWorkCompletionDate());
			originalDetail.setContractPeriod(workOrder.getContractPeriod());
			originalDetail.setDefectLiabilityPeriod(workOrder.getDefectLiabilityPeriod());
			originalDetail.setCreatedBy(workOrder.getCreatedBy());
			originalDetail.setCreatedDate(workOrder.getCreatedDate());
			originalDetail.setModifiedBy(workOrder.getModifiedBy());
			originalDetail.setModifiedDate(workOrder.getModifiedDate());
			originalDetail = workCompletionDetailService.persist(originalDetail);
		}
		if(actionName.equalsIgnoreCase("save")){
			workCompletionDetail.setEgwStatus(commonsService.getStatusByModuleAndCode(WorksConstants.WORKCOMPLETIONDETAIL,"NEW"));
			if(id ==null){
				Position pos = employeeService.getPositionforEmp(employeeService.getEmpForUserId(abstractEstimateService.getCurrentUserId()).getIdPersonalInformation());
				workCompletionDetail = workCompletionDetailWorkflowService.start(workCompletionDetail,pos, "Work Completion Detail Created.");
			}
			messageKey="wcd."+actionName;
			addActionMessage(getText(messageKey,"The Work Completion Extension Details was saved successfully"));
			workCompletionDetail = workCompletionDetailService.persist(workCompletionDetail);
			getDesignation(workCompletionDetail);
		}
		else if(actionName.equalsIgnoreCase("Approve")){
			workCompletionDetailWorkflowService.transition(actionName, workCompletionDetail, approverComments);
			
			workOrder.setWorkCompletionDate(workCompletionDetail.getWorkCompletionDate());
			if(workCompletionDetail.getContractPeriod()!=null){
				workOrder.setContractPeriod(workCompletionDetail.getContractPeriod());
			}
			if(workCompletionDetail.getDefectLiabilityPeriod()>0){
				workOrder.setDefectLiabilityPeriod(workCompletionDetail.getDefectLiabilityPeriod());
			}
			workCompletionDetail = workCompletionDetailService.persist(workCompletionDetail);
			messageKey="wcd."+actionName; 
			getDesignation(workCompletionDetail);
		}
		else{
			if(id==null){
				workCompletionDetail.setEgwStatus(commonsService.getStatusByModuleAndCode("workCompletionDetail","NEW"));
				Position pos = employeeService.getPositionforEmp(employeeService.getEmpForUserId(abstractEstimateService.getCurrentUserId()).getIdPersonalInformation());
				workCompletionDetail = workCompletionDetailWorkflowService.start(workCompletionDetail, pos, "Work Completion Detail Created.");
			}
			workCompletionDetailWorkflowService.transition(actionName, workCompletionDetail, approverComments);
			workCompletionDetail = workCompletionDetailService.persist(workCompletionDetail);
			messageKey="wcd."+actionName; 
			getDesignation(workCompletionDetail);
		}
		return SAVE_ACTION.equalsIgnoreCase(actionName)?EDIT:SUCCESS;
	}

	public void getDesignation(WorkCompletionDetail workCompletionDetail){
		/* start for customizing workflow message display */
		if(workCompletionDetail.getCurrentState()!= null 
				&& !"NEW".equalsIgnoreCase(workCompletionDetail.getCurrentState().getValue())) {
			String result = worksService.getEmpNameDesignation(workCompletionDetail.getState().getOwner(),workCompletionDetail.getState().getCreatedDate());
			if(result != null && !"@".equalsIgnoreCase(result)) {
				String empName = result.substring(0,result.lastIndexOf('@'));
				String designation =result.substring(result.lastIndexOf('@')+1,result.length());
				setEmployeeName(empName);
				setDesignation(designation);
			}
		}
		/* end */	
	}
	
	@Override
	public StateAware getModel() {
		return workCompletionDetail;
	}
	
	public static boolean isNumeric(String str)
	{
	  return str.matches("-?\\d+(.\\d+)?");
	}

	
	public WorkCompletionDetail getWorkCompletionDetail() {
		return workCompletionDetail;
	}

	public void setWorkCompletionDetail(WorkCompletionDetail workCompletionDetail) {
		this.workCompletionDetail = workCompletionDetail;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public void setAbstractEstimateService(
			AbstractEstimateService abstractEstimateService) {
		this.abstractEstimateService = abstractEstimateService;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	public String getSourcepage() {
		return sourcepage;
	}

	public void setSourcepage(String sourcepage) {
		this.sourcepage = sourcepage;
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

	public Long getWorkOrderId() {
		return workOrderId;
	}

	public void setWorkOrderId(Long workOrderId) {
		this.workOrderId = workOrderId;
	}

	public void setWorkOrderService(WorkOrderService workOrderService) {
		this.workOrderService = workOrderService;
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}

	public void setWorkCompletionDetailService(
			PersistenceService<WorkCompletionDetail, Long> workCompletionDetailService) {
		this.workCompletionDetailService = workCompletionDetailService;
	}

	public WorkOrder getWorkOrder() {
		return workOrder;
	}

	public void setWorkOrder(WorkOrder workOrder) {
		this.workOrder = workOrder;
	}

	public AbstractEstimate getEstimate() {
		return estimate;
	}

	public void setEstimate(AbstractEstimate estimate) {
		this.estimate = estimate;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public List<WorkCompletionDetail> getWorkCompletionDetailsList() {
		return workCompletionDetailsList;
	}

	public void setWorkCompletionDetailsList(
			List<WorkCompletionDetail> workCompletionDetailsList) {
		this.workCompletionDetailsList = workCompletionDetailsList;
	}

	public void setWorkCompletionDetailWorkflowService(
			WorkflowService<WorkCompletionDetail> workCompletionDetailWorkflowService) {
		this.workCompletionDetailWorkflowService = workCompletionDetailWorkflowService;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

}
