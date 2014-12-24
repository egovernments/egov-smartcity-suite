package org.egov.works.web.actions.workorder;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.EgwStatus;
import org.egov.commons.service.CommonsService;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.mail.Email;
import org.egov.infstr.models.Money;
import org.egov.infstr.models.StateAware;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.infstr.workflow.WorkflowService;
import org.egov.lib.rjbac.dept.ejb.api.DepartmentService;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.commons.Position;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.EmployeeView;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EisUtilService;
import org.egov.pims.service.EmployeeService;
import org.egov.pims.service.PersonalInformationService;
import org.egov.tender.TenderableGroupType;
import org.egov.tender.model.GenericTenderResponse;
import org.egov.tender.model.TenderResponseLine;
import org.egov.tender.services.common.GenericTenderService;
import org.egov.tender.services.tenderresponse.TenderResponseService;
import org.egov.web.actions.workflow.GenericWorkFlowAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.estimate.Activity;
import org.egov.works.models.estimate.AssetsForEstimate;
import org.egov.works.models.estimate.EstimateRateContract;
import org.egov.works.models.estimate.EstimateRateContractDetail;
import org.egov.works.models.estimate.MeasurementSheet;
import org.egov.works.models.masters.Contractor;
import org.egov.works.models.measurementbook.MBBill;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.revisionEstimate.RevisionWorkOrder;
import org.egov.works.models.tender.EstimateLineItemsForWP;
import org.egov.works.models.tender.SetStatus;
import org.egov.works.models.tender.TenderFile;
import org.egov.works.models.workorder.AssetsForWorkOrder;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.models.workorder.WorkOrderActivity;
import org.egov.works.models.workorder.WorkOrderEstimate;
import org.egov.works.models.workorder.WorkOrderMeasurementSheet;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.WorkOrderService;
import org.egov.works.services.WorksService;
import org.egov.works.utils.DateConversionUtil;
import org.egov.works.utils.WorksConstants;
import org.egov.works.web.actions.estimate.AjaxEstimateAction;


@ParentPackage("egov")
@Results({
@Result(name = WorkOrderAction.PRINT, type="stream", location = "WorkOrderPDF", params = {"inputName", "WorkOrderPDF", "contentType", "application/pdf","contentDisposition", "no-cache,filename=WorkOrderPDF.pdf" }),
@Result(name = WorkOrderAction.PRINT_WOIL, type="stream", location = "woIntimationPDF", params = {"inputName", "woIntimationPDF", "contentType", "application/pdf","contentDisposition", "no-cache" })
})
public class WorkOrderAction extends GenericWorkFlowAction{
	private static final String SAVE_ACTION = "save";
	public final static String APPROVED="APPROVED";
	private WorkOrder workOrder=new WorkOrder();
	private WorkOrderService workOrderService;
	private WorksService worksService;
	private TenderResponseService genericTenderResponseService;
	private AbstractEstimateService abstractEstimateService;
	private PersistenceService<SetStatus, Long> worksStatusService;
	private EmployeeService employeeService;
	private DepartmentService departmentService;
	private EisUtilService eisService;
	private Long tenderRespId; 
	private GenericTenderResponse tenderResponse; 
	private GenericTenderService genericTenderService;
	private Integer deptId;
	private Integer empId;
	private String editableDate;
	private String createdBySelection;
	private String status;
	private Date fromDate;
	private Date toDate;
	private Long assignedTo1; 
	private Long assignedTo2; 
	private String messageKey;
	private Long id;
	private String setStatus;
	private String mode="";
	private Integer mbPreparedBy=-1;
	private String mbRefNo;
	private static final String PREPARED_BY_LIST = "preparedByList";
	private static final String DEPARTMENT_LIST = "departmentList";
	private static final String ASSIGNED_TO_LIST = "assignedToList";
	private static final String ASSIGNED_USER_LIST1 = "assignedUserList1";
	private static final String ASSIGNED_USER_LIST2 = "assignedUserList2";
	private static final String OBJECT_TYPE = "TenderResponse";
	private static final String WO_OBJECT_TYPE = "WorkOrder";
	private static final String STATUS_OBJECTID = "getStatusDateByObjectId_Type_Desc";
	private static final String WORK_ORDER_CREATIONDATE = "STATUS_FOR_WORKORDER_CREATION";
	private static final String SITE_HAND_OVER = "Site handed over";
	private static final String WORK_COMMENCED = "Work commenced";
	private static final String WF_APPROVED="APPROVED";
	private static final String CANCELLED="CANCELLED";
	private static final String SEARCH_WO="searchWorkOrder"; 
	private static final String DATE_FORMAT="dd-MMM-yyyy";
	
	private Map<String,Object> criteriaMap=null;
	private List<DesignationMaster> workOrderDesigList=new ArrayList<DesignationMaster>();
	private List<WorkOrder> workOrderList=null;
	//private List<String> workOrderActions;
	private Long workOrderId; 
	private CommonsService commonsService;
	private String sourcepage=""; 
	private Long estimateId;
	private String type="";
	private WorkflowService<WorkOrder> workOrderWorkflowService;

	private SetStatus setStatusObj;
	public static final String PRINT = "print";
	public static final String PRINT_WOIL="print_WOIL";
	private InputStream workOrderPDF;
	private ReportService reportService;
	
	private String employeeName;
	private String designation;
	private String estimateNumber;
	private String wpNumber;
	private String tenderFileNumber;
	private PersonalInformationService personalInformationService;
	private Long tenderRespContrId;
	private Double securityDepositConfValue;
	//private Double labourWelfareFundConfValue;
	private List<WorkOrderActivity> actionWorkOrderActivities=new LinkedList<WorkOrderActivity>();
	private List<WorkOrderActivity> woActivities=new LinkedList<WorkOrderActivity>();
	private PersistenceService<Activity, Long> activityService;
	private List<WorkOrderMeasurementSheet> woMeasurementSheetList=new LinkedList<WorkOrderMeasurementSheet>();
	private AbstractEstimate estimate;
	private int noOfTenderResponses;
	private Contractor contractors;
	private Double emdAmountDeposited;
	private WorkflowService<WorkOrder> workflowService;
	private String additionalRuleValue;
	private Long estimateRcId;
	private EstimateRateContract rcEstimate;
	private String departmentName;
	private String quotationFlag;
	private boolean isSpillOverWorks;
	private InputStream woIntimationPDF;
	private String emailMsg;
	private static final String PUBLIC_WORKS_DEPARTMENT="Public Work";
	private String woStatus;
	private String cancelRemarks;
	private String workOrderNo;
	private PersistenceService<RevisionWorkOrder,Long> revisionWorkOrderService;
	private String preparedByTF = "";
	private transient ScriptService scriptExecutionService;

	public WorkOrderAction() {
		addRelatedEntity("contractor", Contractor.class);
		addRelatedEntity("engineerIncharge", PersonalInformation.class);
		addRelatedEntity("engineerIncharge2", PersonalInformation.class);
		addRelatedEntity("workOrderPreparedBy", PersonalInformation.class);
	} 

	public void prepare() {
		AjaxEstimateAction ajaxEstimateAction = new AjaxEstimateAction();
		AjaxWorkOrderAction ajaxWorkOrderAction = new AjaxWorkOrderAction();
		ajaxWorkOrderAction.setPersistenceService(getPersistenceService());
		ajaxWorkOrderAction.setEisService(eisService);
		ajaxWorkOrderAction.setPersonalInformationService(personalInformationService);
		ajaxWorkOrderAction.setScriptExecutionService(scriptExecutionService);
		ajaxEstimateAction.setPersistenceService(getPersistenceService());
		ajaxEstimateAction.setEmployeeService(employeeService);
		ajaxEstimateAction.setPersonalInformationService(personalInformationService);
		ajaxEstimateAction.setAbstractEstimateService(abstractEstimateService);
		addDropdownData("executingDepartmentList",getPersistenceService().findAllBy("from DepartmentImpl order by upper(deptName)")); 
		if(sourcepage!=null && sourcepage.equalsIgnoreCase("searchWOForBillCreation")) {
			addDropdownData("preparedByListForMB", getPersistenceService().findAllBy("select distinct mbh.mbPreparedBy from MBHeader mbh"));
		}
		if(tenderRespId!=null){
			tenderResponse = genericTenderResponseService.getTenderResponseById(tenderRespId);
			}
		if (id != null) {
			workOrder= workOrderService.findById(id, false);
			
			if(workOrder.getNegotiationNumber()!=null){
			tenderResponse =genericTenderService.getGenericResponseByNumber(workOrder.getNegotiationNumber());
			tenderRespId=tenderResponse.getId();
			}
			List<EmployeeView> emps = employeeService.getEmployeeInfoBasedOnDeptAndDate(workOrder.getWorkOrderEstimates().get(0).getEstimate().getExecutingDepartment().getId(), workOrder.getCreatedDate()==null?new Date():workOrder.getCreatedDate());
		if(workOrder.getEngineerIncharge() != null && getAssignedTo1() == null) {
			EmployeeView empFinal1 = new EmployeeView();
				for (EmployeeView eview : emps){
					if (eview.getEmployeeCode().equalsIgnoreCase(workOrder.getEngineerIncharge().getEmployeeCode()))
						empFinal1 = eview;
				}
				if (null != empFinal1.getDesigId())
					setAssignedTo1(Long.valueOf(empFinal1.getDesigId().getDesignationId()));
		}
		
		if(workOrder.getEngineerIncharge2()!=null && getAssignedTo2()==null){
			EmployeeView empFinal2 = new EmployeeView();
				for (EmployeeView eview : emps){
					if (eview.getEmployeeCode().equalsIgnoreCase(workOrder.getEngineerIncharge2().getEmployeeCode()))
						empFinal2 = eview;
				}
				if (null != empFinal2.getDesigId())
					setAssignedTo2(Long.valueOf(empFinal2.getDesigId().getDesignationId()));
			}
		setWorkOrderActivities(workOrder);
			
			if(tenderRespId==null ){
				estimate=workOrder.getWorkOrderEstimates().get(0).getEstimate();
				if(!workOrder.getWorkOrderEstimates().get(0).getEstimate().getEstimateRateContractList().isEmpty()) {
				populateWorkorderForRCEstimate();
					type="workOrderForRC";
		}
			}else{
				contractors=workOrder.getContractor();
		}
		}
		if(id==null && type.equals("workOrderForRC"))
			{
			rcEstimate=(EstimateRateContract) persistenceService.find("from EstimateRateContract where id=?",estimateRcId);
			estimate=rcEstimate.getEstimate();
				populateWorkorderForRCEstimate();
			}
				
		super.prepare();
		if(tenderResponse!=null) {
			estimate=getEstimateFromTenderResponse(tenderResponse);
			if(estimate!=null){
				deptId =estimate.getExecutingDepartment().getId();
				departmentName=estimate.getExecutingDepartment().getDeptName();
				List<GenericTenderResponse> approvedTenderResponses=genericTenderService.getAcceptedTenderResponse(estimate);
				if(approvedTenderResponses!=null)
					noOfTenderResponses=approvedTenderResponses.size();
				
		}
			if(noOfTenderResponses==1){
				workOrder.setWorkOrderAmount(tenderResponse.getBidValue().doubleValue());
			}/*else if(noOfTenderResponses>1){
				workOrder.setWorkOrderAmount(0.0D);
			}*/
			}
		setupDropdownDataExcluding("contractor","engineerIncharge","engineerIncharge2","workOrderPreparedBy");
		if(StringUtils.isNotBlank(getCreatedBy()) && "yes".equalsIgnoreCase(getCreatedBy())){
			setCreatedBySelection(getCreatedBy());
			addDropdownData(DEPARTMENT_LIST,departmentService.getAllDepartments());
			populatePreparedByList(ajaxEstimateAction,deptId!=null) ;
		}
		else {
			addDropdownData(PREPARED_BY_LIST,Arrays.asList(getEmployee()));
			addDropdownData(DEPARTMENT_LIST,Arrays.asList(getAssignment(getEmployee()).getDeptId()));
			setCreatedBySelection(getCreatedBy());
		}
		
		if(StringUtils.isNotBlank(getPastDate()))
			setEditableDate(getPastDate());
		populateWorkOrderAssignedToList(ajaxWorkOrderAction,deptId!=null);
		populateWorkOrderUsersList1(ajaxWorkOrderAction,assignedTo1!=null,deptId!=null);
		populateWorkOrderUsersList2(ajaxWorkOrderAction,assignedTo2!=null,deptId!=null);
		addDropdownData("deptListForSearch",departmentService.getAllDepartments());
		getDeptList();
		if(!getWorkFlowDepartment().equals("")){
			departmentName=getWorkFlowDepartment();
	}
		quotationFlag=getAdditionalRule();
		
		if(estimate!=null){
			isSpillOverWorks=estimate.getIsSpillOverWorks();
		}else{
			isSpillOverWorks=false;
	}

	if("cancelWO".equals(sourcepage)) {
		setWoStatus("APPROVED");
	}

	}
	
	public void populateTenderResposeLineIds(Collection<WorkOrderActivity> workOrderActivities){
		for(WorkOrderActivity workOrderActivity:workOrderActivities) {
			workOrderActivity.setTenderResponseActivityId(getTenderResponseLineId(workOrder.getNegotiationNumber(), workOrder.getWorkOrderEstimates().get(0).getEstimate().getEstimateNumber(), workOrderActivity.getActivity()));
		}
	}
	
	public void populateWorkorderForRCEstimate() {
		deptId=estimate.getExecutingDepartment().getId();
		departmentName=estimate.getExecutingDepartment().getDeptName();
		if(estimate.getId()!=null && !estimate.getEstimateRateContractList().isEmpty()){
			if(rcEstimate==null){
				rcEstimate=(EstimateRateContract) persistenceService.find("from EstimateRateContract where estimate.id=? and rateContract.contractor.id=?",estimate.getId(),workOrder.getContractor().getId());
			}
			contractors=rcEstimate.getRateContract().getContractor();
			workOrder.setWorkOrderAmount(getWorkOrderAmount());
		}
	}
	
	private Long getTenderResponseLineId(String responseNumber,String estimateNumber,Activity activity) {
		StringBuilder sb = new StringBuilder(300);
		
		sb.append("select trl.id from TenderResponseLine trl where trl.tenderResponse.number='"+responseNumber+"'");
		
		if(activity.getSchedule()!=null){
			sb.append(" and UPPER(trl.tenderableEntity.number) like '%^" +activity.getSchedule().getCode().toUpperCase()+"%'");
		}
		else{
			sb.append(" and trl.tenderableEntity.number='" +activity.getNonSor().getId().toString()+"'"); 
		}
		if(StringUtils.isNotBlank(estimateNumber)){
			sb.append(" and trl.tenderableEntity.tenderableEntityGroup.number= '"+estimateNumber+"'");
		}
		return (Long) HibernateUtil.getCurrentSession().createQuery(sb.toString()).uniqueResult();
	}
	
	public String newform(){
		PersonalInformation pi = getEmployee();
		Assignment assignment = getAssignment(pi);
		workOrder.setSecurityDeposit((getSecurityDepositConfValue()/100)*workOrder.getWorkOrderAmount());
		//workOrder.setLabourWelfareFund((getLabourWelfareFundConfValue()/100)*workOrder.getWorkOrderAmount());
		if(assignment!=null && "no".equalsIgnoreCase(getCreatedBy())){
			workOrder.setWorkOrderPreparedBy(pi);
		}
		if(tenderResponse!=null){
			if(tenderResponse.getTenderUnit().getEmd()!=null){
				workOrder.setEmdAmountDeposited(tenderResponse.getTenderUnit().getEmd().doubleValue());
			}
		}
		return NEW;
	}
			
	
	public String save() {
		String actionName = parameters.get("actionName")[0]; 
		workOrder.setAdditionalWfRule(getAdditionalRule());
		workOrder.getWorkOrderEstimates().clear();
		populateWorkOrderActivities();	
		workOrderService.setWorkOrderNumber(estimate,workOrder,null);
		
		if(!type.equals("workOrderForRC")){
		validateWorkOrderDate();
			if(!type.equals("workOrderForRC") && !"quotation".equals(quotationFlag))
		validateEmdAmountDeposited();
		}
		
		String deptName=getWorkFlowDepartment();
		String curStatus;
		if(workOrder.getCurrentState()!=null){
			curStatus=workOrder.getCurrentState().getValue();
		}
		else
			curStatus="NEW"; 
		
		if((actionName.equalsIgnoreCase("Forward")||actionName.equalsIgnoreCase("Approve")) && customizedWorkFlowService.getWfMatrix(getModel().getStateType(), deptName, getAmountRule(), getAdditionalRule(),curStatus, getPendingActions())==null && !workOrder.getWorkOrderEstimates().get(0).getEstimate().getIsSpillOverWorks()){
			throw new ValidationException(Arrays.asList(new ValidationError("workorder.workflow.notdefined",getText("workorder.workflow.notdefined",new String[]{deptName}))));
		}
		
		if(workOrder.getWorkOrderEstimates().get(0).getEstimate().getIsSpillOverWorks()){
			workOrder.setAdditionalWfRule("spillOverWorks");
		}
		if(actionName.equalsIgnoreCase("save")){
			workOrder.setEgwStatus(commonsService.getStatusByModuleAndCode("WorkOrder","NEW"));
			if(id ==null){
			Position pos = employeeService.getPositionforEmp(employeeService.getEmpForUserId(abstractEstimateService.getCurrentUserId()).getIdPersonalInformation());
			workOrder = (WorkOrder) workflowService.start(workOrder, pos, "WorkOrder created.");
		}
			messageKey="workorder."+actionName;
			addActionMessage(getText(messageKey,"The WorkOrder was saved successfully"));
			workOrder = workOrderService.persist(workOrder);
			getDesignation(workOrder);
			}
		else{
			if(id==null){
			workOrder.setEgwStatus(commonsService.getStatusByModuleAndCode("WorkOrder","NEW"));
			Position pos = employeeService.getPositionforEmp(employeeService.getEmpForUserId(abstractEstimateService.getCurrentUserId()).getIdPersonalInformation());
			workOrder = (WorkOrder) workflowService.start(workOrder, pos, "WorkOrder created.");
			}
			workflowService.transition(actionName, workOrder, approverComments);
			workOrder = workOrderService.persist(workOrder);
			messageKey="workorder."+actionName; 
			getDesignation(workOrder);
			}
		
		setWorkOrderActivities(workOrder);
		if(workOrder.getEgwStatus().getCode().equalsIgnoreCase("APPROVED") && !workOrder.getWorkOrderEstimates().get(0).getEstimate().getIsSpillOverWorks()){
			if(workOrder.getContractor().getEmail()!=null && StringUtils.isNotEmpty(workOrder.getContractor().getEmail())){
				try{
				String body=getText("email.body",new String[]{workOrder.getContractor().getName(),getText("reports.title.corporation_name")});
				Email email = new Email.Builder(workOrder.getContractor().getEmail(),body)
				.subject(getText("email.subject"))
				.attachment(getWOIntimationLetterStream(), "WO-Letter of Intimation.pdf", "Work Order Letter of Intimation")
				.build();
				email.send();
				emailMsg=getText("email.success.msg",new String[]{workOrder.getContractor().getName()});
				}
				catch(EGOVRuntimeException egovExp){
					emailMsg=getText("email.failure.msg1");
				}
			}else{
				emailMsg= getText("email.failure.msg2");
			}
		}else{
			emailMsg= "";
		}
		return SAVE_ACTION.equalsIgnoreCase(actionName)?EDIT:SUCCESS;
	}
	
	
	protected String getAdditionalRule() {
		String additionalRuleValue = null;
		/**
		 * Commented because additional rule is not required as of now 
		 */
		/*TenderFile tf = null;
		EstimateRateContract erc=null;
		String estFunctionCode=null;
		if(!workOrder.getWorkOrderEstimates().isEmpty()){
			if(workOrder.getWorkOrderEstimates().get(0).getEstimate()!=null){
					erc=(EstimateRateContract)getPersistenceService().find("from EstimateRateContract erc where erc.estimate.id=?",workOrder.getWorkOrderEstimates().get(0).getEstimate().getId());
					estFunctionCode=workOrder.getWorkOrderEstimates().get(0).getEstimate().getFinancialDetails().get(0).getFunction().getCode();
			}
		}
		if(erc!=null){
			 if(StringUtils.isNotBlank(getWorkFlowDepartment()) && PUBLIC_WORKS_DEPARTMENT.equalsIgnoreCase(getWorkFlowDepartment())){ 
				 List functionCodes = getFunctionCodes(); 
				 if(functionCodes!=null && !functionCodes.isEmpty() && functionCodes.contains(estFunctionCode)){
					 additionalRuleValue="ZonalPublicWork";
				 }
				 else{
					 additionalRuleValue="HQPublicWork";
				 }
			 }
			 else
			additionalRuleValue="rateContract";
		}
		else{		
			if(tenderResponse!=null){
				tf=(TenderFile)getPersistenceService().find("from TenderFile tf where tf.fileNumber=?",tenderResponse.getNotice().getTenderFileRefNumber());
		}
			 if(estimate!=null && PUBLIC_WORKS_DEPARTMENT.equalsIgnoreCase(estimate.getExecutingDepartment().getDeptName())){
				 List functionCodes = getFunctionCodes();
				 if(functionCodes!=null && !functionCodes.isEmpty() && functionCodes.contains(estimate.getFinancialDetails().get(0).getFunction().getCode())){				
					 additionalRuleValue="ZonalPublicWork";
				}
				else{
					 additionalRuleValue="HQPublicWork";
				}
			 }
			 else{
		additionalRuleValue=tf==null?"":(tf.getQuotationFlag()?"quotation":"noQuotation");
	}
		}*/
		return additionalRuleValue;
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
	
	public String cancel(){
		String actionName = parameters.get("actionName")[0]; 
		if(workOrder.getId()!=null){
			workOrderWorkflowService.transition(actionName, workOrder,approverComments);
			workOrder=workOrderService.persist(workOrder);
		}
		messageKey="workorder.cancel";		
		return SUCCESS;
	}	
	
	public void getDesignation(WorkOrder workOrder){
		/* start for customizing workflow message display */
		if(workOrder.getCurrentState()!= null 
				&& !"NEW".equalsIgnoreCase(workOrder.getCurrentState().getValue())) {
			String result = worksService.getEmpNameDesignation(workOrder.getState().getOwner(),workOrder.getState().getCreatedDate());
			if(result != null && !"@".equalsIgnoreCase(result)) {
				String empName = result.substring(0,result.lastIndexOf('@'));
				String designation =result.substring(result.lastIndexOf('@')+1,result.length());
				setEmployeeName(empName);
				setDesignation(designation);
			}
		}
		/* end */	
	}

	private void validateWorkOrderDate() {
		if(getWorkOrderCreationDate()==null && workOrder.getWorkOrderDate()!=null && tenderResponse.getState()!=null &&
				"APPROVED".equals(tenderResponse.getState().getPrevious().getValue()) 
				&& DateConversionUtil.isBeforeByDate(workOrder.getWorkOrderDate(),tenderResponse.getState().getCreatedDate())) {				
				throw new ValidationException(Arrays.asList(new ValidationError("workorder.workorderDate.lessthan.approvedDate","workorder.workorderDate.lessthan.approvedDate")));
		}
	else if(getWorkOrderCreationDate()!=null && workOrder.getWorkOrderDate()!=null && DateConversionUtil.isBeforeByDate(workOrder.getWorkOrderDate(),getWorkOrderCreationDate())){		
			throw new ValidationException(Arrays.asList(new ValidationError("workorder.workorderDate.lessthan.statusDate",
				getText("workorder.workorderDate.lessthan.statusDate",new String[]{setStatusObj.getEgwStatus().getDescription()}))));
		}
	}
		
	private void validateEmdAmountDeposited() {
		if(emdAmountDeposited==0.00)
		{
			throw new ValidationException(Arrays.asList(new ValidationError("workorder.emdAmountDeposited.null","workorder.emdAmountDeposited.null")));
	}
	}
	
	@SkipValidation
	public String viewWorkOrderPdf() throws JRException, Exception {
		ReportRequest reportRequest = null;
		List<RevisionWorkOrder> revisedWorkOrderList=null;
		revisedWorkOrderList=revisionWorkOrderService.findAllByNamedQuery("GET_REVISION_WORKORDER", workOrder.getId());
		Map<String, Object> reportParams = null;
		workOrder.setSiteHandOverDate(getSiteHandOverDate()); 
		workOrder.setWorkCommencedDate(getWorkCommencedDate());
		if (workOrder.getPackageNumber() == null){
			reportParams = workOrderService.createHeaderParams(workOrder,"estimate");
			reportParams.put("WORKORDER_BILLOFQUANTITIES_LIST",workOrderService.getActivitiesForWorkorder(workOrder));
			reportParams.put("revisedWorkOrderList", revisedWorkOrderList);
			reportParams.put("bidType", tenderResponse.getBidType().toString());
			reportParams.put("bidPercentage", tenderResponse.getPercentage());
			reportParams.put("tenderNoticeDate", DateUtils.getFormattedDate(tenderResponse.getTenderUnit().getTenderNotice().getNoticeDate(),"dd/MM/yyyy"));
			reportParams.put("tenderNoticeNumber", tenderResponse.getTenderUnit().getTenderNotice().getNumber());
			reportParams.put("timePeriod", tenderResponse.getTenderUnit().getTimeLimit());
			reportRequest = new ReportRequest("workOrderLetter",workOrder, reportParams);			
		} else {
			reportParams = workOrderService.createHeaderParams(workOrder, "wp");
			reportParams.put("WORKORDER_BILLOFQUANTITIES_LIST",workOrderService.getActivitiesForWorkorder(workOrder));
			reportRequest = new ReportRequest("workorderForWp",workOrderService.getAeForWp(workOrder), reportParams);
		}
		ReportOutput reportOutput = reportService.createReport(reportRequest);
		if (reportOutput != null && reportOutput.getReportOutputData() != null)
			workOrderPDF = new ByteArrayInputStream(reportOutput.getReportOutputData());

		return PRINT;
	}

	@SkipValidation
	public String viewWorkOrderIntimatationLetter(){
		woIntimationPDF=getWOIntimationLetterStream();
		return PRINT_WOIL;
	}

	private InputStream getWOIntimationLetterStream(){
		InputStream inputStream=null;
		ReportRequest reportRequest = null;
		Map<String, Object> reportParams = new HashMap<String, Object>();
		WorkOrderEstimate workOrderEstimate=(WorkOrderEstimate)(getPersistenceService().findAllBy("from WorkOrderEstimate where workOrder.id=?", workOrder.getId())).get(0);
		if (workOrderEstimate != null){
			if(tenderResponse!=null) {
				reportParams.put("tenderNoticeDate",DateUtils.getFormattedDate(tenderResponse.getTenderUnit().getTenderNotice().getNoticeDate(),"dd/MM/yyyy"));
				reportParams.put("bidSubmissionNumber",workOrder.getNegotiationNumber());
				reportParams.put("bidAcceptedDate", DateUtils.getFormattedDate(genericTenderService.getBidAcceptedDate(workOrder.getNegotiationNumber()),"dd/MM/yyyy"));
			}
			else {
				reportParams.put("tenderNoticeDate","  *  ");
				reportParams.put("bidSubmissionNumber","  *  ");
				reportParams.put("bidAcceptedDate", "  *  ");
			}
			reportParams.put("zoneOrWard",workOrderEstimate.getEstimate().getWard().getName());
			reportParams.put("workName",workOrderEstimate.getEstimate().getName());
			reportParams.put("contractorName",workOrderEstimate.getWorkOrder().getContractor().getName());
			reportParams.put("contractorAddress",workOrderEstimate.getWorkOrder().getContractor().getAddress());
			reportParams.put("zone",workOrderEstimate.getEstimate().getWard().getName());
			/*For Email Id */
		//  reportParams.put("emailId",workOrderEstimate.getWorkOrder().getContractor().getEmail());
			reportParams.put("emailId","");
			reportParams.put("deptCode",workOrderEstimate.getEstimate().getExecutingDepartment().getDeptCode());
			reportParams.put("currentYear",Calendar.getInstance().get(Calendar.YEAR)); 
		} 

		reportRequest = new ReportRequest("WOIntimationLetter",Collections.EMPTY_LIST, reportParams);
		
		ReportOutput reportOutput = reportService.createReport(reportRequest);
		if (reportOutput != null && reportOutput.getReportOutputData() != null)
			inputStream=new ByteArrayInputStream(reportOutput.getReportOutputData());

		
		return inputStream;
	}
	
	public AbstractEstimate getEstimateFromTenderResponse(GenericTenderResponse tenderResponse){
		AbstractEstimate abstractEstimate=null;
		for(TenderResponseLine trl:tenderResponse.getResponseLines()){
			if(trl.getTenderableEntity().getTenderableEntityGroup().getTenderableGroupType().equals(TenderableGroupType.ESTIMATE)){
				abstractEstimate=abstractEstimateService.getAbstractEstimateByNumber(trl.getTenderableEntity().getTenderableEntityGroup().getNumber());
				}
		}
		return abstractEstimate;
	}
	
	private void populateWorkOrderActivities(){
		if(estimate!=null){
			if(!type.equals("workOrderForRC")){
			if(noOfTenderResponses==1){
				WorkOrderEstimate workOrderEstimate = new WorkOrderEstimate();
					workOrderEstimate.setEstimate(estimate);
					workOrderEstimate.setWorkOrder(workOrder);
			for(TenderResponseLine trl:tenderResponse.getResponseLines()){
				addWorkOrderActivities(workOrderEstimate,trl,estimate);
							}
					workOrderEstimate=populateAssets(workOrderEstimate,estimate);
					workOrder.addWorkOrderEstimate(workOrderEstimate);
				}
			else if(noOfTenderResponses>1){
			Map<Long,WorkOrderEstimate> workOrderEstimateMap=new HashMap<Long,WorkOrderEstimate>();
			for(WorkOrderActivity woActivity:getActionWorkOrderActivityList()){
				if(!workOrderEstimateMap.containsKey(woActivity.getActivity().getAbstractEstimate().getId()))
				{
					WorkOrderEstimate workOrderEstimate = new WorkOrderEstimate();
					workOrderEstimate.setEstimate(woActivity.getActivity().getAbstractEstimate());
					workOrderEstimate.setWorkOrder(workOrder);
					workOrderEstimate=populateAssets(workOrderEstimate,woActivity.getActivity().getAbstractEstimate());
					workOrderEstimateMap.put(woActivity.getActivity().getAbstractEstimate().getId(), workOrderEstimate);
				}
				WorkOrderActivity workOrderActivity=new WorkOrderActivity();
				workOrderActivity.setActivity(woActivity.getActivity());
				workOrderActivity.setApprovedRate(woActivity.getApprovedRate());
				workOrderActivity.setApprovedQuantity(woActivity.getApprovedQuantity());
				workOrderActivity.setApprovedAmount(new Money(woActivity.getApprovedRate() * woActivity.getApprovedQuantity() 
						* woActivity.getActivity().getConversionFactor()).getValue());
				WorkOrderEstimate workOrderEstimate =workOrderEstimateMap.get(woActivity.getActivity().getAbstractEstimate().getId());
				workOrderActivity.setWorkOrderEstimate(workOrderEstimate);
					if(!woMeasurementSheetList.isEmpty()){
						 for (WorkOrderMeasurementSheet woMS: woMeasurementSheetList) { 
							   	 if (woMS!=null && woMS.getMeasurementSheet()!=null) {
									if(((woMS.getWoActivity().getActivity().getId()!=null && workOrderActivity.getActivity().getId()!=null && 
											woMS.getWoActivity().getActivity().getId().equals(workOrderActivity.getActivity().getId())))) {
										MeasurementSheet msheet=(MeasurementSheet)getPersistenceService().find("from MeasurementSheet where id=?", woMS.getMeasurementSheet().getId());
										woMS.setMeasurementSheet(msheet);
										woMS.setWoActivity(workOrderActivity);
										workOrderActivity.addWoMeasurementSheet(woMS);
									 }
						 } 
					 }
				 }
				workOrderEstimate.addWorkOrderActivity(workOrderActivity);
			}
			for(WorkOrderEstimate workOrderEstimate:workOrderEstimateMap.values()){
				workOrder.addWorkOrderEstimate(workOrderEstimate);
			}
		}
		}else{
			WorkOrderEstimate workOrderEstimate = new WorkOrderEstimate();
			workOrderEstimate.setEstimate(estimate);
			workOrderEstimate.setWorkOrder(workOrder);
			addWorkOrderEstimateActivities(workOrderEstimate, rcEstimate);
			workOrderEstimate=populateAssets(workOrderEstimate,estimate);
			workOrder.addWorkOrderEstimate(workOrderEstimate);
	}

		}		
	}

	public String edit(){
		workOrder.setSiteHandOverDate(getSiteHandOverDate());
		workOrder.setWorkCommencedDate(getWorkCommencedDate());
		return EDIT;
	}
	
	public Collection<EstimateLineItemsForWP> getActivitiesForWorkorder() {
		 Collection<EstimateLineItemsForWP> li=null;
		if(id==null){
			 li= workOrderService.getActivitiesForWorkorder(tenderResponse,estimate);
		}else{
			li= workOrderService.getActivitiesForWorkorder(workOrder);
		}
		return li;
	}
	
	
	public StateAware getModel() {
		return workOrder;
	}
	
	@ValidationErrorPage(value=SEARCH_WO)
	public String searchWorkOrder(){
		return SEARCH_WO;
	}
	
	public List<EgwStatus> getWorkOrderStatuses() {
		List<EgwStatus> woStatusList=commonsService.getStatusByModule(WorkOrder.class.getSimpleName());
		woStatusList.remove(commonsService.getStatusByModuleAndCode(WorkOrder.class.getSimpleName(), "NEW"));
		return woStatusList;
	}
	
	public List<EgwStatus> getWorkOrderStatusesForMBCreation() {
		return commonsService.getStatusListByModuleAndCodeList(WorkOrder.class.getSimpleName(),worksService.getNatureOfWorkAppConfigValues("Works", "WORKORDER_STATUS"));
	}
	
	public String getWOCreationForEstimateOrWP()
	{
		return worksService.getWorksConfigValue("ESTIMATE_OR_WP_FOR_WO");
	}
	
	private WorkOrderEstimate populateAssets(WorkOrderEstimate workOrderEstimate,AbstractEstimate estimate) {		
		 for(AssetsForEstimate assetValue: estimate.getAssetValues()) {
				 AssetsForWorkOrder assetsForWorkOrder=new AssetsForWorkOrder();
				 assetsForWorkOrder.setAsset(assetValue.getAsset());
				 assetsForWorkOrder.setWorkOrderEstimate(workOrderEstimate);
				 workOrderEstimate.addAssetValue(assetsForWorkOrder);
			 }	
		 return workOrderEstimate;
	}
	private void addWorkOrderEstimateActivities(WorkOrderEstimate workOrderEstimate, EstimateRateContract rcEstimate){
	    if(rcEstimate!=null) {
			for (EstimateRateContractDetail estimateRCDetail : rcEstimate.getEstimateRCDetailList()) {
				Activity activity=estimateRCDetail.getActivity();
			WorkOrderActivity workOrderActivity=new WorkOrderActivity();
			workOrderActivity.setActivity(activity);
			workOrderActivity.setApprovedRate(activity.getRate().getValue());
			workOrderActivity.setApprovedQuantity(activity.getQuantity());
			workOrderActivity.setApprovedAmount(new Money(workOrderActivity.getApprovedRate() * workOrderActivity.getApprovedQuantity() 
					* activity.getConversionFactor()).getValue());
			workOrderActivity.setWorkOrderEstimate(workOrderEstimate);
			for(MeasurementSheet measurementSheet: activity.getMeasurementSheetList()) {
				WorkOrderMeasurementSheet woMsheet=new WorkOrderMeasurementSheet();
				woMsheet.setNo(measurementSheet.getNo());
				woMsheet.setLength(measurementSheet.getUomLength());
				woMsheet.setWidth(measurementSheet.getWidth());
				woMsheet.setDepthOrHeight(measurementSheet.getDepthOrHeight());
				woMsheet.setQuantity(measurementSheet.getQuantity()); 
				woMsheet.setMeasurementSheet(measurementSheet);
				woMsheet.setWoActivity(workOrderActivity);
				workOrderActivity.addWoMeasurementSheet(woMsheet);
			}			
			workOrderEstimate.addWorkOrderActivity(workOrderActivity);
		}
	    }		
	}
	
	private void addWorkOrderActivities(WorkOrderEstimate workOrderEstimate,TenderResponseLine trl,AbstractEstimate estimate){
		Activity activity=workOrderService.getActivityFromTenderResponseLineAndEstimate(trl, estimate);
			WorkOrderActivity workOrderActivity=new WorkOrderActivity();
		workOrderActivity.setActivity(activity);
		workOrderActivity.setApprovedRate(trl.getBidRateByUom().doubleValue());
		workOrderActivity.setApprovedQuantity(trl.getQuantityByUom().doubleValue());
			workOrderActivity.setApprovedAmount(new Money(workOrderActivity.getApprovedRate() * workOrderActivity.getApprovedQuantity() 
				* activity.getConversionFactor()).getValue());
			workOrderActivity.setWorkOrderEstimate(workOrderEstimate);
		for(MeasurementSheet measurementSheet: activity.getMeasurementSheetList()) {
			WorkOrderMeasurementSheet woMsheet=new WorkOrderMeasurementSheet();
			woMsheet.setNo(measurementSheet.getNo());
			woMsheet.setLength(measurementSheet.getUomLength());
			woMsheet.setWidth(measurementSheet.getWidth());
			woMsheet.setDepthOrHeight(measurementSheet.getDepthOrHeight());
			woMsheet.setQuantity(measurementSheet.getQuantity());
			woMsheet.setMeasurementSheet(measurementSheet);
			woMsheet.setWoActivity(workOrderActivity);
			workOrderActivity.addWoMeasurementSheet(woMsheet);
		}			
			workOrderEstimate.addWorkOrderActivity(workOrderActivity);
	}

	private Date getWorkOrderCreationDate() {
		String statusForCreation = getWorkOrderCreationConfValue();
		if("0".equals(statusForCreation)){
			setStatusObj=((SetStatus)getPersistenceService().findByNamedQuery("getmaxStatusByObjectId",tenderRespId,
					tenderRespId,OBJECT_TYPE));
		}else{
			setStatusObj=(SetStatus) getPersistenceService().findByNamedQuery("getStatusDateByObjectId_Type_Desc",tenderRespId,
					OBJECT_TYPE,statusForCreation);			
		}
		if(setStatusObj!=null)
			return setStatusObj.getStatusDate(); 
		return null;
	}

	private String getWorkOrderCreationConfValue() {
		return worksService.getWorksConfigValue(WORK_ORDER_CREATIONDATE);
	}
	
	public double getWorkOrderAmount()
	{
		double totalAmt=0;
		if(tenderResponse!=null){
		for(EstimateLineItemsForWP act:workOrderService.getActivitiesForWorkorder(tenderResponse,estimate)){
		   totalAmt+=act.getAmt();
		}
		}
		else{
			for (EstimateRateContractDetail estimateRCDetail : rcEstimate.getEstimateRCDetailList()) {
				totalAmt+=estimateRCDetail.getActivity().getAmount().getValue()+estimateRCDetail.getActivity().getTaxAmount().getValue();
			}
		}
		return totalAmt;
	}
	 
	@ValidationErrorPage(value=SEARCH_WO)
	public String searchWorkOrderDetails(){
		criteriaMap = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(status) && !getStatus().equals("-1"))	{
			criteriaMap.put("STATUS",status);
		}
		if(mbPreparedBy!=null && mbPreparedBy!=-1)
			criteriaMap.put("MB_PREPARED_BY",mbPreparedBy);
		if(mbRefNo!=null && StringUtils.isNotBlank(mbRefNo))
			criteriaMap.put("MBREF_NO",mbRefNo);
		if(StringUtils.isNotBlank(workOrder.getWorkOrderNumber()))
			criteriaMap.put("WORKORDER_NO",workOrder.getWorkOrderNumber());
		if(StringUtils.isNotBlank(getEstimateNumber()))
			criteriaMap.put("ESTIMATE_NO",getEstimateNumber());
		if(getDeptId()!=null && getDeptId()>0)
			criteriaMap.put("DEPT_ID",getDeptId()); 
		if(StringUtils.isNotBlank(getWpNumber()))
			criteriaMap.put("WP_NO",getWpNumber());
		if(StringUtils.isNotBlank(getTenderFileNumber()))
			criteriaMap.put("TENDER_FILE_NO",getTenderFileNumber());
		if(workOrder.getContractor()!=null && workOrder.getContractor().getId()!=-1)
			criteriaMap.put("CONTRACTOR_ID", workOrder.getContractor().getId());
		if(fromDate!=null && toDate!=null && !DateUtils.compareDates(getToDate(),getFromDate()))
			addFieldError("enddate",getText("greaterthan.endDate.fromDate"));
		if(toDate!=null && !DateUtils.compareDates(new Date(),getToDate()))
			addFieldError("enddate",getText("greaterthan.endDate.currentdate"));
		if(!getFieldErrors().isEmpty())
			return SEARCH_WO;
		if(fromDate!=null && toDate==null){
			criteriaMap.put("FROM_DATE",new Date(DateUtils.getFormattedDate(getFromDate(),DATE_FORMAT )));
		}else if(toDate!=null && fromDate==null){
			 criteriaMap.put("TO_DATE",new Date(DateUtils.getFormattedDate(getToDate(),DATE_FORMAT )));
		}else if(fromDate!=null && toDate!=null && getFieldErrors().isEmpty()){
			criteriaMap.put("FROM_DATE", new Date(DateUtils.getFormattedDate(getFromDate(),DATE_FORMAT )));
		    criteriaMap.put("TO_DATE",new Date(DateUtils.getFormattedDate(getToDate(),DATE_FORMAT)));
		}
		if("searchWOForMBCreation".equals(sourcepage)){
			workOrderList=workOrderService.searchWOForMB(criteriaMap);
		}
		else if("searchWOForBillCreation".equals(sourcepage)){			
			workOrderList=workOrderService.searchWOForBilling(criteriaMap); 
		}
		else if("searchWOForReturnSD".equals(sourcepage)){
			workOrderList=workOrderService.searchWOForReturnSD(criteriaMap);
		}
		else if("searchWOForRetentionMR".equals(sourcepage)){
			workOrderList=workOrderService.searchWOForRetentionMR(criteriaMap);
		}
		else {
			setPageSize(WorksConstants.PAGE_SIZE);
			search();
			if(searchResult.getFullListSize() !=0){
	            workOrderList = getPositionAndUser(searchResult.getList());
			    searchResult.getList().clear();
			    searchResult.getList().addAll(workOrderList);
			}
		}
		
		if("searchWOForMBCreation".equals(sourcepage) ||  "searchWOForBillCreation".equals(sourcepage)||  "searchWOForReturnSD".equals(sourcepage)
				|| "searchWOForRetentionMR".equals(sourcepage)){
			if(!workOrderList.isEmpty()){
		    	  workOrderList = getPositionAndUser(workOrderList);
        
			}
		}
		return SEARCH_WO;
	}
	
	public String cancelApprovedWO() {  
		WorkOrder workOrder = workOrderService.findById(workOrderId, false);
		workOrder.getCurrentState().getPrevious().setValue("CANCELLED");
		workOrder.setEgwStatus(commonsService.getStatusByModuleAndCode("WorkOrder","CANCELLED"));
		
		PersonalInformation prsnlInfo=employeeService.getEmpForUserId(Integer.valueOf(getLoggedInUserId()));			
		String empName="";
		if(prsnlInfo.getEmployeeFirstName()!=null)
			empName=prsnlInfo.getEmployeeFirstName();
		if(prsnlInfo.getEmployeeLastName()!=null)
			empName=empName.concat(" ").concat(prsnlInfo.getEmployeeLastName()); 			
		workOrder.getCurrentState().getPrevious().setText1(cancelRemarks+". Work Order Cancelled by: "+empName);
		
		workOrderNo=workOrder.getWorkOrderNumber(); 
		messageKey=workOrderNo+": The Work Order was Cancelled successfully"; 
		return SUCCESS;
	}
	
	public int getLoggedInUserId() {
		return Integer.parseInt(EGOVThreadLocals.getUserId());
	}
	
	
	protected List<WorkOrder> getPositionAndUser(List<WorkOrder> results){
		List<WorkOrder> workOrderList = new ArrayList<WorkOrder>();
		for(WorkOrder workOrder :results){
			PersonalInformation emp = employeeService.getEmployeeforPosition(workOrder.getCurrentState().getOwner());
			if(emp!=null)
				workOrder.setOwner(emp.getEmployeeName());
			//workOrder.setWorkOrderAmount(getWorkOrderActvitiesAmount(workOrder));
			workOrderList.add(workOrder);
			
			if(workOrder.getState()!=null && workOrder.getState().getPrevious()!=null &&
					workOrder.getState().getPrevious().getValue().equals(WF_APPROVED)){
				SetStatus set_status=((SetStatus)persistenceService.findByNamedQuery("getmaxStatusByObjectId_Type",workOrder.getId(),
						workOrder.getId(),WorkOrder.class.getSimpleName(),WorkOrder.class.getSimpleName()));
				if(set_status==null)
					workOrder.setStatus(workOrder.getState().getPrevious().getValue());
				else
					workOrder.setStatus(set_status.getEgwStatus().getCode());
			}
			else{
				if(workOrder.getState()!=null && workOrder.getState().getPrevious()!=null && 
						(workOrder.getState().getPrevious().getValue().equals(APPROVED) || workOrder.getState().getPrevious().getValue().equals(CANCELLED))){
					workOrder.setStatus(workOrder.getState().getPrevious().getValue());
				}
				else{
					workOrder.setStatus(workOrder.getState().getValue());
				}
			}
			
			
			String approved =getApprovedValue();
			String actions = worksService.getWorksConfigValue("WORKORDER_SHOW_ACTIONS");
			if(StringUtils.isNotBlank(actions)){
				//workOrderActions = new ArrayList<String>();
				workOrder.getWorkOrderActions().addAll(Arrays.asList(actions.split(",")));
				if(StringUtils.isNotBlank(approved) && workOrder.getCurrentState()!=null && workOrder.getCurrentState().getPrevious()!=null &&
						approved.equals(workOrder.getCurrentState().getPrevious().getValue())){
					String setStat = worksService.getWorksConfigValue("WORKS_SETSTATUS_VALUE");
					if(StringUtils.isNotBlank(setStat)){
						workOrder.getWorkOrderActions().add(setStat);
					}
					
					if(workOrder.getMbHeaders()!=null){
						boolean finalBillCreated=false;
						String finalBillType=worksService.getWorksConfigValue(WorksConstants.BILL_TYPE_FINALBILL);
						for(MBHeader mbh:workOrder.getMbHeaders()){
							for(MBBill bill:mbh.getMbBills()){
								if(bill.getEgBillregister().getBilltype().equalsIgnoreCase(finalBillType) && bill.getEgBillregister().getBillstatus().equals(WF_APPROVED)){
									finalBillCreated=true;
				}
			}
						}
						if(!finalBillCreated){
							workOrder.getWorkOrderActions().add("Extend Work Completion Date");
						}
					}
					else{
						workOrder.getWorkOrderActions().add("Extend Work Completion Date");
					}
				}
			}
		}	
		
		return workOrderList;
	}
	

	public String getApprovedValue() {
		return worksService.getWorksConfigValue("WORKS_PACKAGE_STATUS");
	}
	
	public double getWorkOrderActvitiesAmount(WorkOrder workOrder){
		double totalAmt=0;
		for(WorkOrderEstimate workOrderEstimate:workOrder.getWorkOrderEstimates()){
			for(WorkOrderActivity woAct:workOrderEstimate.getWorkOrderActivities()){
				 totalAmt+=woAct.getApprovedAmount();
			}
		}
		return totalAmt;
	}
	
	
	
	public String getPendingActions()
	{
		return workOrder==null?"":
			(workOrder.getCurrentState()==null?"":workOrder.getCurrentState().getNextAction());
	}

	public String getWorkFlowDepartment(){
		return workOrder==null?"":(workOrder.getWorkOrderEstimates().isEmpty()?"":(workOrder.getWorkOrderEstimates().get(0).getEstimate()==null?"":(workOrder.getWorkOrderEstimates().get(0).getEstimate().getExecutingDepartment()==null?"":workOrder.getWorkOrderEstimates().get(0).getEstimate().getExecutingDepartment().getDeptName())));
	}
	
	public String getPastDate() {
		return worksService.getWorksConfigValue("WORK_ORDER_PASTDATE");
	}
	
	private String getMBCreationBySelection() {
		return worksService.getWorksConfigValue("MB_CREATED_BY_SELECTION");
	}
	
	public void getDeptList() {
		if(StringUtils.isNotBlank(getMBCreationBySelection())){
			if("no".equals(getMBCreationBySelection())){
				PersonalInformation pi = employeeService.getEmpForUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
				Assignment ass= employeeService.getAssignmentByEmpAndDate(new Date(), pi.getIdPersonalInformation());
				addDropdownData("deptListForMB", Arrays.asList(ass.getDeptId()));
			}
			else
				addDropdownData("deptListForMB", departmentService.getAllDepartments());
		}
		else addDropdownData("deptListForMB", Collections.EMPTY_LIST);
	}

	public String getCreatedBy() {
		return worksService.getWorksConfigValue("WORK_ORDER_CREATEDBY");
	}
	
	
	private void populatePreparedByList(AjaxEstimateAction ajaxEstimateAction, boolean executingDeptPopulated){
		if (executingDeptPopulated) {
			ajaxEstimateAction.setExecutingDepartment(deptId);
			ajaxEstimateAction.setWorksService(worksService);
			
			if((sourcepage.equalsIgnoreCase("inbox") 
					&& workOrder!=null && workOrder.getCurrentState()!=null 
					&& workOrder.getWorkOrderPreparedBy()!=null 
					&& !workOrder.getCurrentState().getValue().equalsIgnoreCase("NEW") 
					&& !workOrder.getCurrentState().getValue().equalsIgnoreCase("REJECTED"))
					|| sourcepage.equalsIgnoreCase("search"))
			{	
				ajaxEstimateAction.userWithEmpCodeInDeptOnDate(workOrder.getWorkOrderPreparedBy().getEmployeeCode(),
						workOrder.getCreatedDate());
				List <EmployeeView> empViewList = ajaxEstimateAction.getUsersInExecutingDepartment();
				if(empViewList!=null && !empViewList.isEmpty())
					preparedByTF = empViewList.get(0).getEmployeeName() +"-"+ empViewList.get(0).getDesigId().getDesignationName();
				else
					preparedByTF = workOrder.getWorkOrderPreparedBy().getEmployeeName();
			}
			else
				ajaxEstimateAction.usersInExecutingDepartment();
			addDropdownData(PREPARED_BY_LIST, ajaxEstimateAction.getUsersInExecutingDepartment());
		}
		else {
			addDropdownData(PREPARED_BY_LIST,Collections.EMPTY_LIST);
		}
	}
	
	private void populateWorkOrderAssignedToList(AjaxWorkOrderAction ajaxWorkOrderAction, boolean executingDeptPopulated){
		if (executingDeptPopulated && deptId>0) {
			ajaxWorkOrderAction.setDepartmentName(departmentService.getDepartmentById(Long.valueOf(deptId)).getDeptName());
			ajaxWorkOrderAction.getDesignationByDeptId();
			addDropdownData(ASSIGNED_TO_LIST,ajaxWorkOrderAction.getWorkOrderDesigList());
		}
		else {
			addDropdownData(ASSIGNED_TO_LIST,Collections.EMPTY_LIST);
		}
	}
	
	private void populateWorkOrderUsersList1(AjaxWorkOrderAction ajaxWorkOrderAction, boolean desgId,boolean executingDeptPopulated){
		if (desgId && executingDeptPopulated && deptId>0) {
			ajaxWorkOrderAction.setDesgId(getAssignedTo1());
			ajaxWorkOrderAction.setExecutingDepartment(deptId);
			ajaxWorkOrderAction.getUsersForDesg();
			addDropdownData(ASSIGNED_USER_LIST1,ajaxWorkOrderAction.getUserList());
		}
		else {
			addDropdownData(ASSIGNED_USER_LIST1,Collections.EMPTY_LIST);
		}
	}
	
	private void populateWorkOrderUsersList2(AjaxWorkOrderAction ajaxWorkOrderAction, boolean desgId, boolean executingDeptPopulated){
		if (desgId && executingDeptPopulated && deptId>0) {
			ajaxWorkOrderAction.setDesgId(getAssignedTo2());
			ajaxWorkOrderAction.setExecutingDepartment(deptId);
			ajaxWorkOrderAction.getUsersForDesg();
			addDropdownData(ASSIGNED_USER_LIST2,ajaxWorkOrderAction.getUserList());
		}
		else {
			addDropdownData(ASSIGNED_USER_LIST2,Collections.EMPTY_LIST);
		}
	}
	
	
	private PersonalInformation getEmployee() {
		if(workOrder.getWorkOrderPreparedBy()==null)
			return employeeService.getEmpForUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
		else
			return workOrder.getWorkOrderPreparedBy();
	}

	private Assignment getAssignment(PersonalInformation pi) {
		if(workOrder.getWorkOrderPreparedBy()==null)
			return employeeService.getAssignmentByEmpAndDate(new Date(), pi.getIdPersonalInformation());
		else return employeeService.getAssignmentByEmpAndDate(workOrder.getCreatedDate()==null?new Date():workOrder.getCreatedDate(), workOrder.getWorkOrderPreparedBy().getIdPersonalInformation());
	}
	
	public Double getSecurityDepositConfValue() {
		this.securityDepositConfValue=workOrderService.getSecurityDepositConfValue();
		return securityDepositConfValue;
	}
	
	public void setSecurityDepositConfValue(Double securityDepositConfValue) {	
		this.securityDepositConfValue=securityDepositConfValue;
	}
	
	/*public Double getLabourWelfareFundConfValue() {	
		this.labourWelfareFundConfValue=workOrderService.getLabourWelfareFundConfValue();
		return labourWelfareFundConfValue;
	}
	
	public void setLabourWelfareFundConfValue(Double labourWelfareFundConfValue) {		
		this.labourWelfareFundConfValue=labourWelfareFundConfValue;
	}*/
	
	public String getAssignedToRequiredOrNot() {		
		return worksService.getWorksConfigValue("WORKORDER_ASSIGNEDTO_REQUIRED");
	}
	
	public WorkOrder getWorkOrder() {
		return workOrder;
	}

	public void setWorkOrder(WorkOrder workOrder) {
		this.workOrder = workOrder;
	}
	public String getCreatedBySelection() {
		return createdBySelection;
	}

	public void setCreatedBySelection(String createdBySelection) {
		this.createdBySelection = createdBySelection;
	}
	public void setWorkOrderService(WorkOrderService workOrderService) {
		this.workOrderService = workOrderService;
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}

	public void setGenericTenderResponseService(TenderResponseService genericTenderResponseService) {
		this.genericTenderResponseService = genericTenderResponseService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	public void setAbstractEstimateService(
			AbstractEstimateService abstractEstimateService) {
		this.abstractEstimateService = abstractEstimateService;
	}

	public String getEditableDate() {
		return editableDate;
	}

	public void setEditableDate(String editableDate) {
		this.editableDate = editableDate;
	}

	public Integer getEmpId() {
		return empId;
	}
	public Collection<EstimateLineItemsForWP> getActivitiesForWorkorderList() {
		//return tenderResponse.getActivitiesForWorkorder();
		return null;
	}
	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

	public Long getTenderRespId() {
		return tenderRespId;
	}

	public void setTenderRespId(Long tenderRespId) {
		this.tenderRespId = tenderRespId;
	}

	public List<DesignationMaster> getWorkOrderDesigList() {
		return workOrderDesigList;
	}

	public void setWorkOrderDesigList(List<DesignationMaster> workOrderDesigList) {
		this.workOrderDesigList = workOrderDesigList;
	}

	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public void setEisService(EisUtilService eisService) {
		this.eisService = eisService;
	}

	public Long getAssignedTo1() {
		return assignedTo1;
	}

	public void setAssignedTo1(Long assignedTo1) {
		this.assignedTo1 = assignedTo1;
	}

	public Long getAssignedTo2() {
		return assignedTo2;
	}

	public void setAssignedTo2(Long assignedTo2) {
		this.assignedTo2 = assignedTo2;
	} 
	
	public List<WorkOrder> getWorkOrderList() {
		return workOrderList;
	}

	public void setWorkOrderList(List<WorkOrder> workOrderList) {
		this.workOrderList = workOrderList;
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

  /*public List<String> getWorkOrderActions() {
				
			return  workOrderActions ;
	}*/


	public Map<String,Object> getContractorForApprovedWorkOrder() {
		Map<String,Object> contractorsWithWOList = new LinkedHashMap<String, Object>();		
		if(workOrderService.getContractorsWithWO()!=null) {
			for(Contractor contractor :workOrderService.getContractorsWithWO()){
				contractorsWithWOList.put(contractor.getId()+"", contractor.getCode()+" - "+contractor.getName());
			}			
		}
		return contractorsWithWOList; 
	} 

	public Long getWorkOrderId() {
		return workOrderId;
	}

	public void setWorkOrderId(Long workOrderId) {
		this.workOrderId = workOrderId;
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getMessageKey() {
		return messageKey;
	}

	public void setWorkOrderWorkflowService(
			WorkflowService<WorkOrder> workOrderWorkflowService) {
		this.workflowService = workOrderWorkflowService;
	}

	public String getSetStatus() {
		return setStatus;
	}

	public void setSetStatus(String setStatus) {
		this.setStatus = setStatus;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
	
	public InputStream getWorkOrderPDF() {
		return workOrderPDF;
	}

	public void setWorkOrderPDF(InputStream workOrderPDF) {
		this.workOrderPDF = workOrderPDF;
	}

	public ReportService getReportService() {
		return reportService;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
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

	public String getSourcepage() {
		return sourcepage;
	}

	public void setSourcepage(String sourcepage) {
		this.sourcepage = sourcepage;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public void setWorksStatusService(
			PersistenceService<SetStatus, Long> worksStatusService) {
		this.worksStatusService = worksStatusService;
	}

	public Date getSiteHandOverDate() {
		if(id!=null){
			SetStatus objStatusForSite = worksStatusService.findByNamedQuery(STATUS_OBJECTID,id,WO_OBJECT_TYPE,SITE_HAND_OVER);
			if(objStatusForSite!=null)
				return objStatusForSite.getStatusDate();
		}
		return null;
	}

	public Date getWorkCommencedDate() {
		if(id!=null){
			SetStatus objStatusForSite = worksStatusService.findByNamedQuery(STATUS_OBJECTID,id,WO_OBJECT_TYPE,WORK_COMMENCED);
			if(objStatusForSite!=null)
				return objStatusForSite.getStatusDate();
		}
		return null;
	}

	public String getEstimateNumber() {
		return estimateNumber;
	}

	public void setEstimateNumber(String estimateNumber) {
		this.estimateNumber = estimateNumber;
	}

	public String getWpNumber() {
		return wpNumber;
	}

	public void setWpNumber(String wpNumber) {
		this.wpNumber = wpNumber;
	}

	public String getTenderFileNumber() {
		return tenderFileNumber;
	}

	public void setTenderFileNumber(String tenderFileNumber) {
		this.tenderFileNumber = tenderFileNumber;
	}

	public void setPersonalInformationService(
			PersonalInformationService personalInformationService) {
		this.personalInformationService = personalInformationService;
	}

	public Long getTenderRespContrId() {
		return tenderRespContrId;
	}

	public void setTenderRespContrId(Long tenderRespContrId) {
		this.tenderRespContrId = tenderRespContrId;
	}

	public List<WorkOrderActivity> getActionWorkOrderActivities() {
		return actionWorkOrderActivities;
	}

	public void setActionWorkOrderActivities(
			List<WorkOrderActivity> actionWorkOrderActivities) {
		this.actionWorkOrderActivities = actionWorkOrderActivities;
	}
	
	public void setActivityService(
			PersistenceService<Activity, Long> activityService) {
		this.activityService = activityService;
	}
	
	public Collection<WorkOrderActivity> getActionWorkOrderActivityList(){
		Collection<WorkOrderActivity> woActivityList=getActionWorkOrderActivitiesList();
		for(WorkOrderActivity workOrderActivity:woActivityList){
			workOrderActivity.setActivity(activityService.findById(workOrderActivity.getActivity().getId(), false));
			workOrderActivity.setUnAssignedQuantity(workOrderActivity.getActivity().getQuantity()-(getAssignedQuantity(workOrderActivity.getActivity().getId(),workOrder.getTenderNumber())));
		}
		return woActivityList;
	}
	
	public List<WorkOrderActivity> getWoActivities() {
		if(workOrder.getId()!=null) {
			populateTenderResposeLineIds(woActivities);
		}
		return woActivities; 
	}

	public void setWoActivities(List<WorkOrderActivity> woActivities) {
		this.woActivities = woActivities;
	}

	public void setWorkOrderActivities(WorkOrder workorder){
		woActivities.clear();
		for (WorkOrderEstimate workOrderEstimate:workorder.getWorkOrderEstimates()){
			if(workOrderEstimate!=null)
			for(WorkOrderActivity workOrderActivity:workOrderEstimate.getWorkOrderActivities()){
				workOrderActivity.setUnAssignedQuantity(workOrderActivity.getActivity().getQuantity()-(getAssignedQuantity(workOrderActivity.getActivity().getId(),workOrderActivity.getWorkOrderEstimate().getWorkOrder().getNegotiationNumber())));
				woActivities.add(workOrderActivity);
			}
		}
	}
	public Collection<WorkOrderActivity> getActionWorkOrderActivitiesList(){
		return CollectionUtils.select(actionWorkOrderActivities, new Predicate(){
			public boolean evaluate(Object workOrderActivity) {
				return ((WorkOrderActivity)workOrderActivity)!=null;
			}});
	}
	
	public void validate(){
		Collection<WorkOrderActivity> woActivityList=getActionWorkOrderActivitiesList();
		for(WorkOrderActivity workOrderActivity:woActivityList){
			if(workOrderActivity.getApprovedRate()==0.0){
				addActionError(getText("WorkOrderActivity.approvedRate.non.negative"));
			}
		
			workOrderActivity.setActivity(activityService.findById(workOrderActivity.getActivity().getId(), false));
			if(workOrderActivity.getActivity().getQuantity()!=0.0 && workOrderActivity.getApprovedQuantity()==0.0){
				addActionError(getText("WorkOrderActivity.approvedQuantity.non.negative"));
			}
		}
	}
	
	private double getAssignedQuantity(Long activityId, String tenderNumber) {
		Object[] params = new Object[]{tenderNumber,activityId};
		Double assignedQty = (Double) getPersistenceService().findByNamedQuery("getAssignedQuantityForActivity",params);	
		Double assignedQtyForNew = (Double) getPersistenceService().findByNamedQuery("getAssignedQuantityForActivityForNewWO",params);
		
		if(assignedQty!=null && assignedQtyForNew!=null)
			assignedQty=assignedQty+assignedQtyForNew;
		if(assignedQty==null && assignedQtyForNew!=null)
			assignedQty=assignedQtyForNew;
		if(assignedQty==null)
			return 0.0d;
		else
			return assignedQty.doubleValue();
	}
	
	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder){
				
		String dynQuery =" from WorkOrder wo left join wo.workOrderEstimates workOrderEstimate" 
			+ " where wo.id is not null and wo.parent is null and wo.state.value<>'NEW' " ;
			List<Object> paramList = new ArrayList<Object>();
			String setStat = worksService.getWorksConfigValue("WorkOrder.setstatus");
			if(criteriaMap.get("STATUS") != null) {
				if(criteriaMap.get("STATUS").equals("APPROVED") || 
						criteriaMap.get("STATUS").equals("CANCELLED")){
					dynQuery = dynQuery + " and wo.state.previous.value = ? and " +
							" wo.id not in (select objectId from SetStatus where objectType='WorkOrder')";
					paramList.add(criteriaMap.get("STATUS"));
				}else if(!criteriaMap.get("STATUS").equals("-1") && Arrays.asList(setStat.split(",")).contains(criteriaMap.get("STATUS")))
				{
				dynQuery = dynQuery + " and wo.id in(select stat.objectId from " +
							"SetStatus stat where stat.egwStatus.code=? and stat.id = (select" +
					" max(stat1.id) from SetStatus stat1 where wo.id=stat1.objectId and stat1.objectType='WorkOrder') and stat.objectType='WorkOrder')";
				paramList.add(criteriaMap.get("STATUS"));
}
				else if(!criteriaMap.get("STATUS").equals("-1") && !Arrays.asList(setStat.split(",")).contains(criteriaMap.get("STATUS")))
				{
				dynQuery = dynQuery + " and wo.state.value = ?";
				paramList.add(criteriaMap.get("STATUS"));
				}
			}
			if(criteriaMap.get("CREATE_DATE") != null) {
				dynQuery = dynQuery + " and wo.workOrderDate = ? ";
				paramList.add(criteriaMap.get("CREATE_DATE"));
			}
							
			if(criteriaMap.get("FROM_DATE") != null && criteriaMap.get("TO_DATE")==null) {
				dynQuery = dynQuery + " and wo.workOrderDate >= ? ";
				paramList.add(criteriaMap.get("FROM_DATE"));

			}else if(criteriaMap.get("TO_DATE") != null && criteriaMap.get("FROM_DATE")==null) {
				dynQuery = dynQuery + " and wo.workOrderDate <= ? ";
				paramList.add(criteriaMap.get("TO_DATE"));
			}else if(criteriaMap.get("FROM_DATE") != null && criteriaMap.get("TO_DATE")!=null) {
				dynQuery = dynQuery + " and wo.workOrderDate between ? and ? ";
				paramList.add(criteriaMap.get("FROM_DATE"));
				paramList.add(criteriaMap.get("TO_DATE"));
			}
			if(criteriaMap.get("WORKORDER_NO") != null){
				dynQuery = dynQuery + " and UPPER(wo.workOrderNumber) like '%"+criteriaMap.get("WORKORDER_NO").toString().trim().toUpperCase()+"%'";
			}
/*			if(criteriaMap.get("ESTIMATE_NO") != null){
				dynQuery = dynQuery + " and wo.negotiationNumber in (select tr.negotiationNumber from TenderResponse tr where " +
			"UPPER(tr.tenderEstimate.abstractEstimate.estimateNumber) like '%"+criteriaMap.get("ESTIMATE_NO").toString().trim().toUpperCase()+"%')";
			}
*/			if(criteriaMap.get("ESTIMATE_NO") != null){
				dynQuery = dynQuery + " and wo.id in (select distinct woe.workOrder.id from WorkOrderEstimate woe where " +
			"UPPER(woe.estimate.estimateNumber) like '%"+criteriaMap.get("ESTIMATE_NO").toString().trim().toUpperCase()+"%')";
			}
/*			if(criteriaMap.get("WP_NO") != null){
				dynQuery = dynQuery + " and UPPER(wo.packageNumber) like '%"+criteriaMap.get("WP_NO").toString().trim().toUpperCase()+"%'";
			}
			if(criteriaMap.get("TENDER_FILE_NO") != null){
				dynQuery = dynQuery + " and wo.negotiationNumber in (select tr1.negotiationNumber from TenderResponse tr1 where " +
			"UPPER(tr1.tenderEstimate.worksPackage.tenderFileNumber) like '%"+criteriaMap.get("TENDER_FILE_NO").toString().trim().toUpperCase()+"%')";
			}
*/			if(criteriaMap.get("CONTRACTOR_ID") != null){
				dynQuery = dynQuery + " and wo.contractor.id = ? ";
				paramList.add(criteriaMap.get("CONTRACTOR_ID"));
			}
			if(criteriaMap.get("DEPT_ID") != null){
				dynQuery = dynQuery + " and wo.id in (select we.workOrder.id from WorkOrderEstimate we where we.workOrder.id=wo.id and " +
						" we.estimate.executingDepartment.id = ?) ";
				paramList.add(criteriaMap.get("DEPT_ID"));
			}
/*			if(criteriaMap.get("TENDER_NO") != null && !"".equalsIgnoreCase((String)criteriaMap.get("TENDER_NO"))){
				dynQuery = dynQuery + " and wo.abstractEstimate.id in "+
				"(select te.abstractEstimate.id from TenderEstimate te where te.tenderHeader.tenderNo = ? ) ";
				paramList.add(criteriaMap.get("TENDER_NO"));
			}
*/			
			if(criteriaMap.get("PROJECT_CODE") != null){
				dynQuery = dynQuery + " and wo.abstractEstimate.projectCode.code like '%" +criteriaMap.get("PROJECT_CODE") +"%'";
			}	
	
		String workOrderSearchQuery="select distinct wo "+	dynQuery;
		String countQuery = "select distinct count(wo) " + dynQuery;
		return new SearchQueryHQL(workOrderSearchQuery, countQuery, paramList);
    }

	public List<WorkOrderMeasurementSheet> getWoMeasurementSheetList() {
		return woMeasurementSheetList;
	}

	public void setWoMeasurementSheetList(
			List<WorkOrderMeasurementSheet> woMeasurementSheetList) {
		this.woMeasurementSheetList = woMeasurementSheetList;
	}
	
	public GenericTenderResponse getTenderResponse() {
		return tenderResponse;
	}

	public void setTenderResponse(GenericTenderResponse tenderResponse) {
		this.tenderResponse = tenderResponse;
	}

	public AbstractEstimate getEstimate() {
		return estimate;
	}

	public void setEstimate(AbstractEstimate estimate) {
		this.estimate = estimate;
	}

	public void setGenericTenderService(GenericTenderService genericTenderService) {
		this.genericTenderService = genericTenderService;
	}

	public int getNoOfTenderResponses() {
		return noOfTenderResponses;
	}

	public void setNoOfTenderResponses(int noOfTenderResponses) {
		this.noOfTenderResponses = noOfTenderResponses;
	}

	public Long getEstimateId() {
		return estimateId;
	}

	public void setEstimateId(Long estimateId) {
		this.estimateId = estimateId;
	}

	public Contractor getContractors() {
		return contractors;
	}

	public void setContractors(Contractor contractors) {
		this.contractors = contractors;
	}

	public Double getEmdAmountDeposited() {
		return emdAmountDeposited;
	}

	public void setEmdAmountDeposited(Double emdAmountDeposited) { 
		this.emdAmountDeposited = emdAmountDeposited;
	}

	public String getAdditionalRuleValue() {
		additionalRuleValue=getAdditionalRule();
		return additionalRuleValue;
	}

	public void setAdditionalRuleValue(String additionalRuleValue) {
		this.additionalRuleValue = additionalRuleValue;
	}

	public EstimateRateContract getRcEstimate() {
		return rcEstimate;
	}

	public void setRcEstimate(EstimateRateContract rcEstimate) {
		this.rcEstimate = rcEstimate;
	}

	public void setEstimateRcId(Long estimateRcId) {
		this.estimateRcId = estimateRcId;
	}

	public Long getEstimateRcId(){
		return estimateRcId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public void setIsSpillOverWorks(boolean isSpillOverWorks) {
		this.isSpillOverWorks = isSpillOverWorks;
	}

	public boolean getIsSpillOverWorks() {
		return isSpillOverWorks;
	}

	public InputStream getWoIntimationPDF() {
		return woIntimationPDF;
	}

	public void setWoIntimationPDF(InputStream woIntimationPDF) {
		this.woIntimationPDF = woIntimationPDF;
	}

	public String getEmailMsg() {
		return emailMsg;
	}
	

	public String getWoStatus() {
		return woStatus;
	}

	public void setWoStatus(String woStatus) {
		this.woStatus = woStatus;
	}

	public String getCancelRemarks() {
		return cancelRemarks;
	}

	public void setCancelRemarks(String cancelRemarks) {
		this.cancelRemarks = cancelRemarks;
	}

	public String getWorkOrderNo() {
		return workOrderNo;
	}

	public void setWorkOrderNo(String workOrderNo) {
		this.workOrderNo = workOrderNo;
	}	
	public Integer getMbPreparedBy() {
		return mbPreparedBy;
	}

	public void setMbPreparedBy(Integer mbPreparedBy) {
		this.mbPreparedBy = mbPreparedBy;
	}

	public String getMbRefNo() {
		return mbRefNo;
	}

	public void setMbRefNo(String mbRefNo) {
		this.mbRefNo = mbRefNo;
	}

	public void setRevisionWorkOrderService(
			PersistenceService<RevisionWorkOrder, Long> revisionWorkOrderService) {
		this.revisionWorkOrderService = revisionWorkOrderService;
	}

	public String getPreparedByTF() {
		return preparedByTF;
	}

	public void setPreparedByTF(String preparedByTF) {
		this.preparedByTF = preparedByTF;
	}

	public void setScriptExecutionService(ScriptService scriptExecutionService) {
		this.scriptExecutionService = scriptExecutionService;
	}
	
}


