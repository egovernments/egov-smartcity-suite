package org.egov.works.web.actions.revisionEstimate;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.dispatcher.StreamResult;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.service.CommonsService;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.models.StateAware;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.workflow.WorkflowService;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.rjbac.dept.ejb.api.DepartmentService;
import org.egov.pims.commons.Position;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;
import org.egov.web.actions.workflow.GenericWorkFlowAction;
import org.egov.works.models.estimate.EstimateRateContract;
import org.egov.works.models.revisionEstimate.RevisionAbstractEstimate;
import org.egov.works.models.revisionEstimate.RevisionType;
import org.egov.works.models.revisionEstimate.RevisionWOCreationType;
import org.egov.works.models.revisionEstimate.RevisionWorkOrder;
import org.egov.works.models.tender.TenderFile;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.models.workorder.WorkOrderActivity;
import org.egov.works.models.workorder.WorkOrderEstimate;
import org.egov.works.services.WorkOrderService;
import org.egov.works.services.WorksService;

@ParentPackage("egov")
@Results({
@Result(name = RevisionWorkOrderAction.PRINT, type="stream", location = "pdfInputStream", params = {"inputName", "pdfInputStream", "contentType", "application/pdf","contentDisposition", "no-cache" })
})
public class RevisionWorkOrderAction extends GenericWorkFlowAction{ 
	
	private static final Logger LOGGER = Logger.getLogger(RevisionWorkOrderAction.class);
	private RevisionWorkOrder revisionWorkOrder = new RevisionWorkOrder();
	private Long id;
	private String sourcepage="";
	private Long revEstimateId; 
	private Long revWorkOrderId;
	private RevisionAbstractEstimate revisionEstimate = new RevisionAbstractEstimate();
	private WorkOrder workOrder = new WorkOrder();
	List<RevisionWorkOrder> revisionWOList=new LinkedList<RevisionWorkOrder>() ;
	private WorkOrderService workOrderService;
	private WorksService worksService;
	private DepartmentService departmentService;
	private EmployeeService employeeService;
	private String empName="";
	private PersistenceService<RevisionAbstractEstimate,Long> revisionAbstractEstimateService;
	private PersistenceService<RevisionWorkOrder,Long> revisionWorkOrderService;
	private static final String SAVE_ACTION = "save";
	
	private boolean isSpillOverWorks;
	private String departmentName;
	private String quotationFlag;
	private static final String PUBLIC_WORKS_DEPARTMENT="Public Work";
	private String additionalRuleValue;
	private CommonsService commonsService;
	private WorkflowService<RevisionWorkOrder> workflowService;
	private String messageKey;
	private String employeeName;
	private String designation;
	private Long preparedBy;
	public static final String PRINT = "print";
	private InputStream pdfInputStream;
	private ReportService reportService;
	
	public String newform(){
		LOGGER.debug("RevisionWorkOrderAction | view | Start"); 
		return NEW;
	}
	
	public String view(){
		LOGGER.debug("RevisionWorkOrderAction | view | Start"); 
		return NEW;
	}

	@Override
	public StateAware getModel() {
		// TODO Auto-generated method stub
		return revisionWorkOrder;
	}
	
	public RevisionWorkOrderAction() {
		addRelatedEntity("workOrderPreparedBy", PersonalInformation.class);
	} 
	
	public void prepare(){
			if(revWorkOrderId!=null || id!=null) {
				if(revWorkOrderId==null){
				revWorkOrderId=id;
				} 
				revisionWorkOrder = revisionWorkOrderService.findById(revWorkOrderId, false);
				revisionEstimate = revisionAbstractEstimateService.findById(revisionWorkOrder.getWorkOrderEstimates().get(0).getEstimate().getId(), false);
				revisionWorkOrder.setCreatedBy(revisionEstimate.getCreatedBy());
			}
			else {				
				revisionEstimate = revisionAbstractEstimateService.findById(revEstimateId, false);
				revisionWorkOrder=(RevisionWorkOrder) persistenceService.find(" from RevisionWorkOrder where id =(select woe.workOrder.id from WorkOrderEstimate woe where (woe.workOrder.egwStatus.code='CANCELLED' " +
						" or woe.workOrder.egwStatus.code='NEW') and woe.estimate.id=?)",revEstimateId);
				revisionWorkOrder.setCreatedBy(revisionEstimate.getCreatedBy());
			}
			
		super.prepare();
		//setupDropdownDataExcluding("");
		addDropdownData("executingDepartmentList",getPersistenceService().findAllBy("from DepartmentImpl order by upper(deptName)"));
		if(revWorkOrderId!=null)
			revWorkOrderId=revisionWorkOrder.getId();
		
		if(revWorkOrderId==null){
			PersonalInformation prsnlInfo=employeeService.getEmpForUserId(Integer.valueOf(getLoggedInUserId()));			
			if(prsnlInfo.getEmployeeFirstName()!=null)
				empName=prsnlInfo.getEmployeeFirstName();
			if(prsnlInfo.getEmployeeLastName()!=null)
				empName=empName.concat(" ").concat(prsnlInfo.getEmployeeLastName()); 
			preparedBy=prsnlInfo.getId().longValue();
		}
		else{
			if(revisionWorkOrder.getWorkOrderPreparedBy().getEmployeeFirstName()!=null)   
				empName=revisionWorkOrder.getWorkOrderPreparedBy().getEmployeeFirstName();
			if(revisionWorkOrder.getWorkOrderPreparedBy().getEmployeeLastName()!=null) 
				empName=empName.concat(" ").concat(revisionWorkOrder.getWorkOrderPreparedBy().getEmployeeLastName());
			preparedBy=revisionWorkOrder.getWorkOrderPreparedBy().getId().longValue(); 
		}
		revisionWOList=revisionWorkOrderService.findAllByNamedQuery("GET_REVISION_WORKORDER", revisionWorkOrder.getParent().getId());
		
		if(!getWorkFlowDepartment().equals("")){
			departmentName=getWorkFlowDepartment(); 
		} 
		quotationFlag=getAdditionalRule();
		
		if(revisionEstimate!=null){
			isSpillOverWorks=revisionEstimate.getParent().getIsSpillOverWorks();
		}else{
			isSpillOverWorks=false;
		}
	}
	
	public String save() {
		String actionName = parameters.get("actionName")[0]; 
		revisionWorkOrder.setAdditionalWfRule(getAdditionalRule()); 
		revisionWorkOrder.setCreationType(RevisionWOCreationType.EXTERNAL);
		/*if(actionName.equalsIgnoreCase("save")){
			revisionWorkOrder.setCreationType(RevisionWOCreationType.EXTERNAL);
			revisionWorkOrder = revisionWorkOrderService.persist(revisionWorkOrder);
			}*/
		
		String deptName=getWorkFlowDepartment();
		String curStatus;
		if(revisionWorkOrder.getCurrentState()!=null){
			curStatus=revisionWorkOrder.getCurrentState().getValue();
		}
		else
			curStatus="NEW"; 
		
		if((actionName.equalsIgnoreCase("Forward")||actionName.equalsIgnoreCase("Approve")) && customizedWorkFlowService.getWfMatrix(getModel().getStateType(), deptName, getAmountRule(), getAdditionalRule(),curStatus, getPendingActions())==null && !revisionWorkOrder.getWorkOrderEstimates().get(0).getEstimate().getParent().getIsSpillOverWorks()){
			throw new ValidationException(Arrays.asList(new ValidationError("workorder.workflow.notdefined",getText("revisionWorkOrder.workflow.notdefined",new String[]{deptName}))));
		}
		
		if(revisionWorkOrder.getWorkOrderEstimates().get(0).getEstimate().getParent().getIsSpillOverWorks()){
			revisionWorkOrder.setAdditionalWfRule("spillOverWorks");
		}
		if(curStatus.equals("NEW")) {
			Position pos = employeeService.getPositionforEmp(employeeService.getEmpForUserId(getLoggedInUserId()).getIdPersonalInformation());
			revisionWorkOrder = (RevisionWorkOrder)workflowService.start(revisionWorkOrder,pos, "Revision Work Order Submitted");  
		}
		
		workflowService.transition(actionName, revisionWorkOrder, approverComments);
		revisionWorkOrder = revisionWorkOrderService.persist(revisionWorkOrder);
		messageKey="revisionWorkOrder."+actionName; 
		getDesignation(revisionWorkOrder);
		
		return SAVE_ACTION.equalsIgnoreCase(actionName)?EDIT:SUCCESS;
	}
	 
	public int getLoggedInUserId() {
		return Integer.parseInt(EGOVThreadLocals.getUserId());
	}
	
	public void getDesignation(RevisionWorkOrder revisionWorkOrder){
		/* start for customizing workflow message display */
		if(revisionWorkOrder.getCurrentState()!= null 
				&& !"NEW".equalsIgnoreCase(revisionWorkOrder.getCurrentState().getValue())) {
			String result = worksService.getEmpNameDesignation(revisionWorkOrder.getState().getOwner(),revisionWorkOrder.getState().getCreatedDate());
			if(result != null && !"@".equalsIgnoreCase(result)) {
				String empName = result.substring(0,result.lastIndexOf('@'));
				String designation =result.substring(result.lastIndexOf('@')+1,result.length());
				setEmployeeName(empName);
				setDesignation(designation);
			}
		}
		/* end */	
	}
	
	protected String getAdditionalRule() {
		TenderFile tf = null;
		EstimateRateContract erc=null;
		String estFunctionCode=null;
		if(revisionEstimate != null){
			if(revisionEstimate.getParent()!=null){
					erc=(EstimateRateContract)getPersistenceService().find("from EstimateRateContract erc where erc.estimate.id=?",revisionEstimate.getParent().getId());
					estFunctionCode=revisionEstimate.getParent().getFinancialDetails().get(0).getFunction().getCode();
			}
		}
		if(erc!=null){
			 if(StringUtils.isNotBlank(getWorkFlowDepartment()) && PUBLIC_WORKS_DEPARTMENT.equalsIgnoreCase(getWorkFlowDepartment())){ 
				 List functionCodes = getFunctionCodes(); 
				 if(functionCodes!=null && !functionCodes.isEmpty() && functionCodes.contains(estFunctionCode)){
					 additionalRuleValue="HQPublicWork";
				 }
				 else{
					 additionalRuleValue="ZonalPublicWork";
				 }
			 }
			 else
			additionalRuleValue="rateContract";
		}
		else{
			if(revisionEstimate!=null){
				tf=(TenderFile)getPersistenceService().find("select tfd.tenderFile from TenderFileDetail tfd where tfd.abstractEstimate.id=?",revisionEstimate.getParent().getId());
			}
			 if(revisionEstimate!=null && PUBLIC_WORKS_DEPARTMENT.equalsIgnoreCase(revisionEstimate.getParent().getExecutingDepartment().getDeptName())){
				 List functionCodes = getFunctionCodes();
				 LOGGER.info("functionCodes==>"+functionCodes);
				 LOGGER.info("revisionEstimate.getParent().getFinancialDetails().get(0).getFunction().getCode()==>"+revisionEstimate.getParent().getFinancialDetails().get(0).getFunction().getCode());
				 if(functionCodes!=null && !functionCodes.isEmpty() && functionCodes.contains(revisionEstimate.getParent().getFinancialDetails().get(0).getFunction().getCode())){				
					 additionalRuleValue="HQPublicWork";
				}
				else{
					 additionalRuleValue="ZonalPublicWork";
				}
			 }
			 else{
				 additionalRuleValue=tf==null?"":(tf.getQuotationFlag()?"quotation":"noQuotation");
			 }
		}
		return additionalRuleValue;
	}
	
	public String getWorkFlowDepartment() {   	                                                                                                   
		return revisionEstimate==null?"":revisionEstimate.getParent().getIsSpillOverWorks()?"":revisionEstimate.getParent().getExecutingDepartment()==null?"":revisionEstimate.getParent().getExecutingDepartment().getDeptName(); 
	}
	
	public List getFunctionCodes(){
		List<AppConfigValues> appConfigList = worksService.getAppConfigValue("Works","WORKS_PWD_FUNCTIONWISE_WF");
		List functionCodes = new LinkedList();
		if(appConfigList!=null && !appConfigList.isEmpty()){
			if(appConfigList.get(0).getValue()!="" && appConfigList.get(0).getValue()!=null){
				String[] configVals=appConfigList.get(0).getValue().split(",");
				for(int i=0; i<configVals.length;i++)
					functionCodes.add(configVals[i]);
			}
		}
		return functionCodes;
	}
	
	@SkipValidation
	public String viewRevWorkOrderPdf() throws JRException, Exception {
		ReportRequest reportRequest = null;
		List<RevisionWorkOrder> revisedWorkOrderList=null;
		revisedWorkOrderList=(List<RevisionWorkOrder>)revisionWorkOrderService.findAllBy("from RevisionWorkOrder rwo where rwo.parent.id=? and rwo.creationType='EXTERNAL' order by rwo.id", revisionWorkOrder.getParent().getId());
		Map<String, Object> reportParams = null;
			reportParams = createHeaderParams(revisionWorkOrder,"estimate");
			reportParams.put("revisedWorkOrderList",revisedWorkOrderList);
			reportParams.put("WORKORDER_BILLOFQUANTITIES_LIST",revisionWorkOrder.getWorkOrderEstimates().get(0).getWorkOrderActivities());
			reportRequest = new ReportRequest("RevisionWorkOrderForEstimate",revisedWorkOrderList, reportParams);			
		 
		ReportOutput reportOutput = reportService.createReport(reportRequest);
		if (reportOutput != null && reportOutput.getReportOutputData() != null)
			pdfInputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());

		return PRINT;
	}
	
	public Map createHeaderParams(RevisionWorkOrder revisionWorkOrder,String type){
		Map<String,Object> reportParams = new HashMap<String,Object>();
		if(revisionWorkOrder!=null) {
			if("estimate".equalsIgnoreCase(type)){
				for(WorkOrderEstimate workOrderEstimate:revisionWorkOrder.getWorkOrderEstimates()){
					if(workOrderEstimate!=null && workOrderEstimate.getEstimate()!=null){
						reportParams.put("deptName", workOrderEstimate.getEstimate().getExecutingDepartment().getDeptName());
						Boundary b = getTopLevelBoundary(workOrderEstimate.getEstimate().getWard());
						reportParams.put("cityName",b==null?"":b.getName());
					
						reportParams.put("deptAddress",workOrderEstimate.getEstimate().getExecutingDepartment().getDeptAddress());
						reportParams.put("aeWorkNameForEstimate",workOrderEstimate.getEstimate().getName());
						reportParams.put("negotiatedAmtForEstimate",revisionWorkOrder.getWorkOrderAmount());
						reportParams.put("woNumber",workOrderEstimate.getWorkOrder().getParent().getWorkOrderNumber());
						reportParams.put("revisionWorkOrderNumber",workOrderEstimate.getWorkOrder().getWorkOrderNumber());
						reportParams.put("estimateNo",workOrderEstimate.getEstimate().getParent().getEstimateNumber());
						reportParams.put("estimateDate",workOrderEstimate.getEstimate().getParent().getEstimateDate());
						reportParams.put("revEstimateNo",workOrderEstimate.getEstimate().getEstimateNumber());
						reportParams.put("revEstimateDate",workOrderEstimate.getEstimate().getEstimateDate());
						
						
				
						if(workOrderEstimate.getEstimate().getParent().getProjectCode()!=null)
							reportParams.put("projectCode",workOrderEstimate.getEstimate().getParent().getProjectCode().getCode());
					}
			  }
			} 
		}
		if(revisionWorkOrder!=null && revisionWorkOrder.getContractor()!=null){
			String contractorAddress = revisionWorkOrder.getContractor().getName()+"  ,  "+revisionWorkOrder.getContractor().getCode();
			if(revisionWorkOrder.getContractor().getPaymentAddress()!=null)
				contractorAddress = contractorAddress+"  ,  "+revisionWorkOrder.getContractor().getPaymentAddress();
			reportParams.put("contractorAddress",contractorAddress);
		}
		 getTotalOfquantites();
		 reportParams.put("RevWorkOrderObj",revisionWorkOrder);

		return reportParams;
	}
	
	protected Boundary getTopLevelBoundary(Boundary boundary) {
		Boundary b = boundary;
		while(b!=null && b.getParent()!=null){
			b=b.getParent();
		}
		return b;
	}
	
	public void getTotalOfquantites(){
		double totalofActivityQuantity=0.00;
		List<RevisionWorkOrder> revisionWorkOrderList=null;
		revisionWorkOrderList=(List<RevisionWorkOrder>)revisionWorkOrderService.findAllByNamedQuery("GET_REVISION_WORKORDER", revisionWorkOrder.getParent().getId());
		revisionWorkOrderList.remove(revisionWorkOrder);
		
			for(RevisionWorkOrder revWorkOrder : revisionWorkOrderList){
				for(WorkOrderActivity woa : revWorkOrder.getWorkOrderEstimates().get(0).getWorkOrderActivities()){
					for(WorkOrderActivity woa1 : revisionWorkOrder.getWorkOrderEstimates().get(0).getWorkOrderActivities()){
						//for SOR
						totalofActivityQuantity=0.00;
						if(woa1.getActivity().getNonSor()==null || woa1.getActivity().getNonSor().getId()==0){
							if(woa1.getActivity().getSchedule().equals(woa.getActivity().getSchedule())){
								if(woa1.getActivity().getRevisionType().equals(woa.getActivity().getRevisionType()) && (woa.getActivity().getRevisionType().equals(RevisionType.ADDITITONAL_QUANTITY))){
									totalofActivityQuantity+=woa.getApprovedQuantity();
									if(woa1.getActivity().getParent()!=null){
										totalofActivityQuantity+=woa1.getActivity().getParent().getQuantity();
									}
								}
							else if(woa1.getActivity().getRevisionType().equals(woa.getActivity().getRevisionType()) && (woa.getActivity().getRevisionType().equals(RevisionType.REDUCED_QUANTITY))){
								totalofActivityQuantity-=woa.getApprovedQuantity();
								if(woa1.getActivity().getParent()!=null){
									totalofActivityQuantity+=woa1.getActivity().getParent().getQuantity();
								}
							}
							woa1.setApprovedTotalQuantityForRevWOs(totalofActivityQuantity);
							
						}
						
					}
						//For Non SOR
						else{ 
							if(woa1.getActivity().getNonSor().equals(woa.getActivity().getNonSor())){
							if(woa1.getActivity().getRevisionType().equals(woa.getActivity().getRevisionType()) && (woa.getActivity().getRevisionType().equals(RevisionType.ADDITITONAL_QUANTITY))){
								totalofActivityQuantity+=woa.getApprovedQuantity();
								if(woa1.getActivity().getParent()!=null){
									totalofActivityQuantity+=woa1.getActivity().getParent().getQuantity();
								}
							}
							else if(woa1.getActivity().getRevisionType().equals(woa.getActivity().getRevisionType()) && (woa.getActivity().getRevisionType().equals(RevisionType.REDUCED_QUANTITY))){
								totalofActivityQuantity-=woa.getApprovedQuantity();
								if(woa1.getActivity().getParent()!=null){
									totalofActivityQuantity+=woa1.getActivity().getParent().getQuantity();
								}
							}
							woa1.setApprovedTotalQuantityForRevWOs(totalofActivityQuantity);
						}
				
					}
				}
			}
		}
}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id; 
	}

	public String getSourcepage() {
		return sourcepage;
	}

	public void setSourcepage(String sourcepage) {
		this.sourcepage = sourcepage;
	}

	public Long getRevEstimateId() {
		return revEstimateId;
	}

	public void setRevEstimateId(Long revEstimateId) {
		this.revEstimateId = revEstimateId;
	}

	public Long getRevWorkOrderId() {
		return revWorkOrderId;
	}

	public void setRevWorkOrderId(Long revWorkOrderId) {
		this.revWorkOrderId = revWorkOrderId;
	}
	public RevisionWorkOrder getRevisionWorkOrder() {
		return revisionWorkOrder;
	}

	public void setRevisionWorkOrder(RevisionWorkOrder revisionWorkOrder) {
		this.revisionWorkOrder = revisionWorkOrder;
	}

	public WorkOrder getWorkOrder() {
		return workOrder;
	}

	public void setWorkOrder(WorkOrder workOrder) {
		this.workOrder = workOrder;
	}

	public RevisionAbstractEstimate getRevisionEstimate() {
		return revisionEstimate;
	}

	public void setRevisionEstimate(RevisionAbstractEstimate revisionEstimate) {
		this.revisionEstimate = revisionEstimate;
	}

	public WorkOrderService getWorkOrderService() {
		return workOrderService;
	}

	public void setWorkOrderService(WorkOrderService workOrderService) {
		this.workOrderService = workOrderService;
	}

	public List<RevisionWorkOrder> getRevisionWOList() {
		return revisionWOList;
	}

	public void setRevisionWOList(List<RevisionWorkOrder> revisionWOList) {
		this.revisionWOList = revisionWOList;
	}

	public WorksService getWorksService() {
		return worksService;
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}

	public DepartmentService getDepartmentService() {
		return departmentService;
	}

	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	public EmployeeService getEmployeeService() {
		return employeeService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public void setRevisionAbstractEstimateService(
			PersistenceService<RevisionAbstractEstimate, Long> revisionAbstractEstimateService) {
		this.revisionAbstractEstimateService = revisionAbstractEstimateService;
	}

	public void setRevisionWorkOrderService(
			PersistenceService<RevisionWorkOrder, Long> revisionWorkOrderService) {
		this.revisionWorkOrderService = revisionWorkOrderService;
	}
	
	public void setIsSpillOverWorks(boolean isSpillOverWorks) {
		this.isSpillOverWorks = isSpillOverWorks;
	}

	public boolean getIsSpillOverWorks() {
		return isSpillOverWorks;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getQuotationFlag() {
		return quotationFlag;
	}

	public void setQuotationFlag(String quotationFlag) {
		this.quotationFlag = quotationFlag;
	}
	
	public String getAdditionalRuleValue() {
		additionalRuleValue=getAdditionalRule();
		return additionalRuleValue;
	}

	public void setAdditionalRuleValue(String additionalRuleValue) {
		this.additionalRuleValue = additionalRuleValue;
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}
	
	public void setRevisionWorkOrderWorkflowService(WorkflowService<RevisionWorkOrder> workflowService) {
		this.workflowService = workflowService;
	}

	public String getMessageKey() {
		return messageKey;
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

	public Long getPreparedBy() {
		return preparedBy;
	}

	public void setPreparedBy(Long preparedBy) {
		this.preparedBy = preparedBy;
	}

	public InputStream getPdfInputStream() {
		return pdfInputStream;
	}

	public void setPdfInputStream(InputStream pdfInputStream) {
		this.pdfInputStream = pdfInputStream;
	}

	public ReportService getReportService() {
		return reportService;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}


}
