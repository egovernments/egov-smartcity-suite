package org.egov.works.web.actions.tender;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.workflow.service.WorkflowService;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.models.Money;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EisUtilService;
import org.egov.pims.service.EmployeeService;
import org.egov.pims.service.PersonalInformationService;
import org.egov.web.actions.BaseFormAction;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.tender.WorksPackage;
import org.egov.works.models.tender.WorksPackageDetails;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.WorksPackageService;
import org.egov.works.services.WorksService;
import org.egov.works.web.actions.estimate.AjaxEstimateAction;
import org.springframework.beans.factory.annotation.Autowired;

@Result(name=WorksPackageAction.PRINT,type="StreamResult.class",location="WorkspackagePDF", params={"inputName","WorkspackagePDF","contentType","application/pdf","contentDisposition","no-cache"})
public class WorksPackageAction extends BaseFormAction{	
	private String editableDate="yes";
	private String createdBySelection="no";
	private WorksService worksService;
	@Autowired
        private EmployeeService employeeService;
	private DepartmentService departmentService;
	private WorksPackage worksPackage = new WorksPackage();
	private String designation;
	private Integer empId;
	private Long[] estId;
	private String sourcepage;
	private Money worktotalValue;
	@Autowired
        private UserService userService;
	private WorkflowService<WorksPackage> workflowService;
	private WorksPackageService workspackageService;
	private AbstractEstimateService abstractEstimateService;
	private List<AbstractEstimate> abstractEstimateList=new ArrayList<AbstractEstimate>();
	private Long id;
	private String actionName;
	private String messageKey;
	private String nextEmployeeName;
	private String nextDesignation;
	private String packageNumber;
	private static final String PREPARED_BY_LIST = "preparedByList";
	private static final String DEPARTMENT_LIST = "departmentList";
	private static final String SOURCE_INBOX="inbox";
	private static final String SAVE_ACTION = "save";
	private EisUtilService eisService;
	private String loggedInUserEmployeeCode;
	
	/**
	 * pdf variable declaration
	 */
	
	public static final String PRINT = "print";	
	private InputStream WorkspackagePDF;
	private ReportService reportService;
	private PersonalInformationService personalInformationService;

	public WorksPackageAction() {
		addRelatedEntity("userDepartment", Department.class);
	}
	
	public void prepare(){	
		AjaxEstimateAction ajaxEstimateAction = new AjaxEstimateAction();
		ajaxEstimateAction.setPersistenceService(getPersistenceService());
		ajaxEstimateAction.setEmployeeService(employeeService);
		ajaxEstimateAction.setPersonalInformationService(personalInformationService);
		ajaxEstimateAction.setAbstractEstimateService(abstractEstimateService);
		ajaxEstimateAction.setEisService(eisService);
		super.prepare();
		if (id != null) {			
			worksPackage= workspackageService.findById(id, false);
		}
		if (id == null && packageNumber!=null && StringUtils.isNotBlank(packageNumber)) {
			worksPackage= workspackageService.find("from WorksPackage where wpNumber=? and egwStatus.code='APPROVED'", packageNumber);
		}
		setupDropdownDataExcluding("userDepartment");
		
		addDropdownData("executingDepartmentList",departmentService.getAllDepartments());
		Assignment latestAssignment = abstractEstimateService.getLatestAssignmentForCurrentLoginUser();
		if(latestAssignment != null) {
			worksPackage.setWorkflowDepartmentId(abstractEstimateService.getLatestAssignmentForCurrentLoginUser().getDeptId().getId());
			if(worksPackage.getPreparedBy() == null)
				loggedInUserEmployeeCode = latestAssignment.getEmployee().getCode();
			else
				loggedInUserEmployeeCode = worksPackage.getPreparedBy().getEmployeeCode();
			if(worksPackage.getUserDepartment() == null) {
				worksPackage.setUserDepartment(latestAssignment.getDeptId());			 
				setDesignation(latestAssignment.getDesigId().getDesignationName());
			}
		}
		
		if(StringUtils.isNotBlank(getCreatedBy()) && "yes".equalsIgnoreCase(getCreatedBy())){
			setCreatedBySelection(getCreatedBy());
			addDropdownData(DEPARTMENT_LIST,departmentService.getAllDepartments());
			populatePreparedByList(ajaxEstimateAction, worksPackage.getUserDepartment() != null);
		}
		else {
			if(id==null || (worksPackage.getEgwStatus()!=null 
					&& (worksPackage.getEgwStatus().getCode().equals(WorksPackage.WorkPacakgeStatus.REJECTED.toString()) 
							|| worksPackage.getEgwStatus().getCode().equals("NEW")))) {
				addDropdownData(DEPARTMENT_LIST,worksService.getAllDeptmentsForLoggedInUser());
			}
			else {
				addDropdownData(DEPARTMENT_LIST,departmentService.getAllDepartments());
			}
			populatePreparedByList(ajaxEstimateAction, worksPackage.getUserDepartment() != null);
			empId=getEmployee().getId();
		}
		if(StringUtils.isNotBlank(getPastDate()))
			setEditableDate(getPastDate());
		
	}

	public String newform(){
		/*PersonalInformation pi = getEmployee();
		Assignment assignment = getAssignment(pi);
		if(assignment!=null && "no".equalsIgnoreCase(getCreatedBy())){
			setDesignation(assignment.getDesigId().getDesignationName());
			worksPackage.setUserDepartment(assignment.getDeptId());
			setEmpId(pi.getId());
		}*/
		
		return NEW;
	}
	
	
	public String edit()
	{
		if(SOURCE_INBOX.equalsIgnoreCase(sourcepage)){
			User user=userService.getUserById(worksService.getCurrentLoggedInUserId());
			boolean isValidUser=worksService.validateWorkflowForUser(worksPackage,user);
			if(isValidUser){
					throw new EGOVRuntimeException("Error: Invalid Owner - No permission to view this page.");
			}
		}
		else if(StringUtils.isEmpty(sourcepage)){
			sourcepage="search";
		}
		
		setDesignation(getAssignment(worksPackage.getPreparedBy()).getDesigId().getDesignationName());
		setEmpId(worksPackage.getPreparedBy().getIdPersonalInformation());
		abstractEstimateList = workspackageService.getAbStractEstimateListByWorksPackage(worksPackage);
		setWorktotalValue(abstractEstimateService.getWorkValueIncludingTaxesForEstList(abstractEstimateList));
		return EDIT;
	}
	
	public String save()
	{
		if(validTenderFileNo()){
			throw new ValidationException(Arrays.asList(new ValidationError("wp.tenderfilenumber.isunique","wp.tenderfilenumber.isunique")));
		}
		
		if(worksPackage.getId()!=null &&  
				 ( worksPackage.getEgwStatus().getCode().equalsIgnoreCase(WorksPackage.WorkPacakgeStatus.REJECTED.toString())
				 || worksPackage.getEgwStatus().getCode().equalsIgnoreCase("NEW")))
			worksPackage.getWorksPackageDetails().clear();
		if(worksPackage.getId()==null 
				|| worksPackage.getEgwStatus().getCode().equalsIgnoreCase(WorksPackage.WorkPacakgeStatus.REJECTED.toString())
				|| worksPackage.getEgwStatus().getCode().equalsIgnoreCase("NEW"))
			populateEstimatesList(estId);
		validateManualWPNumber();
		if (worksPackage.getId() == null && worksPackage.getEgwStatus() == null) {
			validateFinancingSource(abstractEstimateList);
			validateEstimateForUniqueness();
			
		}
		worksPackage.setPreparedBy(employeeService.getEmloyeeById(empId));
		try{
			workspackageService.setWorksPackageNumber(worksPackage,abstractEstimateService.getCurrentFinancialYear(worksPackage.getPackageDate()));
		}catch(ValidationException sequenceException){
			List<ValidationError> errorList=sequenceException.getErrors();
			for(ValidationError error:errorList){
			  if(error.getMessage().contains("DatabaseSequenceFirstTimeException")){
				  prepare();
				  throw new ValidationException(Arrays.asList(new ValidationError("error",error.getMessage())));
			  	}
			  }
		}
		worksPackage=workspackageService.persist(worksPackage);
		workflowService.transition(actionName, worksPackage,"");
		messageKey="worksPackage."+actionName;
		addActionMessage(getText(messageKey,"The Works Package was saved successfully"));
		getDesignation(worksPackage);
		if(SAVE_ACTION.equals(actionName)){
			sourcepage="inbox";
		}  
		
		return SAVE_ACTION.equals(actionName)?EDIT:SUCCESS;
	}

	private void validateManualWPNumber() {
		if(StringUtils.isNotBlank(actionName) && StringUtils.isBlank(worksPackage.getManualWPNumber())
				&& (actionName.equalsIgnoreCase("submit_for_approval") || actionName.equalsIgnoreCase("approve")))
		{
			if(worksPackage.getId()==null)
				throw new ValidationException(Arrays.asList(new ValidationError("wp.enter.manual.wpnumber","wp.enter.manual.wpnumber")));
			if(worksPackage.getId()!=null && worksPackage.getEgwStatus().getCode().equalsIgnoreCase("NEW"))
				throw new ValidationException(Arrays.asList(new ValidationError("wp.enter.manual.wpnumber","wp.enter.manual.wpnumber")));
			if(worksPackage.getId()!=null && !worksPackage.getEgwStatus().getCode().equalsIgnoreCase("NEW"))
				throw new ValidationException(Arrays.asList(new ValidationError("wp.cannot.forward.without.manual.wpno","wp.cannot.forward.without.manual.wpno")));
		}
	}

	private void validateFinancingSource(List<AbstractEstimate> estimateList){
		Integer fundSourceId1;
		fundSourceId1 = abstractEstimateList.get(0).getFundSource().getId();
		for(int i=1;i<abstractEstimateList.size();i++){
			if(fundSourceId1!=abstractEstimateList.get(i).getFundSource().getId()){
					throw new ValidationException(Arrays.asList(new ValidationError("wp.estimate.different.fund.source.not.allowed","wp.estimate.different.fund.source.not.allowed")));
			}
		}
		
	}
	
	private void validateEstimateForUniqueness() {
		WorksPackage wp = null;
		Map<String, List<AbstractEstimate>> wpMap = new HashMap<String, List<AbstractEstimate>>();
		List<AbstractEstimate> estimateList = null;
		for (AbstractEstimate estimate : abstractEstimateList) {
			wp = workspackageService.getWorksPackageForAbstractEstimate(estimate);
			if (wp != null) {
				String wpString = wp.getWpNumber() + "~!"
						+ (wp.getEgwStatus().getDescription() != null ? wp.getEgwStatus().getDescription() : " ");
				if(wpMap.get(wpString) == null) {
					estimateList = new ArrayList<AbstractEstimate>();
					estimateList.add(estimate);
					wpMap.put(wpString, estimateList);
				} else {
					wpMap.get(wpString).add(estimate);
				}
			}
		}
		if (!wpMap.isEmpty()) {
			List<ValidationError> errors = new ArrayList<ValidationError>();
			for (String wpnumber : wpMap.keySet()) {
				List<AbstractEstimate> estList = wpMap.get(wpnumber);
				StringBuffer estimatesSting = null;
				for (AbstractEstimate absEstimate : estList) {
					if (estimatesSting == null) {
						estimatesSting = new StringBuffer();
						estimatesSting.append(absEstimate.getEstimateNumber());
					} else {
						estimatesSting.append(", ").append(absEstimate.getEstimateNumber());
					}
				}
				String[] str = StringUtils.split(wpnumber, "~!");
				errors.add(new ValidationError("worksPackage.uniqueCheck.message",
						"worksPackage.uniqueCheck.message", new String[] { str[0],
								estimatesSting.toString(), str[1]}));
			}
			throw new ValidationException(errors);
		}
	}
	
	public String cancelWorkflow(){
		if(worksPackage.getId()!=null){
			workflowService.transition(WorksPackage.Actions.CANCEL.toString(), worksPackage,worksPackage.getWorkflowapproverComments());
			worksPackage=workspackageService.persist(worksPackage);
		}
		messageKey="worksPackage.cancel";	
		getDesignation(worksPackage);
		return SUCCESS;
	}	
	
	public Object getModel() {
		return worksPackage;
	}
	
	public void setModel(WorksPackage worksPackage) {
		this.worksPackage = worksPackage;
	}
	
	protected void populatePreparedByList(AjaxEstimateAction ajaxEstimateAction, boolean executingDeptPopulated){
		if (executingDeptPopulated) {
			ajaxEstimateAction.setExecutingDepartment(worksPackage.getUserDepartment().getId());

			if(StringUtils.isNotBlank(loggedInUserEmployeeCode)) {
				ajaxEstimateAction.setEmployeeCode(loggedInUserEmployeeCode); 
			}
			ajaxEstimateAction.usersInExecutingDepartment();
			addDropdownData(PREPARED_BY_LIST,ajaxEstimateAction.getUsersInExecutingDepartment());
		}
		else {
			addDropdownData(PREPARED_BY_LIST,Collections.EMPTY_LIST);
		}
	}
	
	protected void populateEstimatesList(Long[] estimateID)
	{
		if(estimateID!=null && estimateID.length>0){
			abstractEstimateList=abstractEstimateService.getAbEstimateListById(StringUtils.join(estId,"`~`"));
			setWorktotalValue(abstractEstimateService.getWorkValueIncludingTaxesForEstList(abstractEstimateList));
		}
		setWPDetails();
	}
	
	public void setWPDetails()
	{
		if(!abstractEstimateList.isEmpty())
		{
			for(AbstractEstimate ab:abstractEstimateList)
			{
				WorksPackageDetails wpDetails = new WorksPackageDetails();
				wpDetails.setEstimate(ab);
				wpDetails.setWorksPackage(worksPackage);
				worksPackage.addEstimates(wpDetails);
			}
		}
	}
	

	public boolean validTenderFileNo() {
		boolean status=false;
		if(worksPackage!=null && worksPackage.getTenderFileNumber()!=null) {
			AjaxWorksPackageAction ajaxWorksPackageAction = new AjaxWorksPackageAction();
			ajaxWorksPackageAction.setPersistenceService(getPersistenceService());
			ajaxWorksPackageAction.setWorkspackageService(workspackageService);
			ajaxWorksPackageAction.setTenderFileNumber(worksPackage.getTenderFileNumber());
			ajaxWorksPackageAction.setId(id);
			if(ajaxWorksPackageAction.getTenderFileNumberCheck()){
				status= true;
			}
		}
		return status;
	}
	
	/**
	 * print pdf
	 * @throws JRException,Exception 
	 */
	@SkipValidation
	public String viewWorksPackagePdf() throws JRException,Exception{
		ReportRequest reportRequest = new ReportRequest("Workspackage",worksPackage.getActivitiesForEstimate(),
				createHeaderParams());
		ReportOutput reportOutput = reportService.createReport(reportRequest);
		if(reportOutput!=null && reportOutput.getReportOutputData()!=null)
			WorkspackagePDF = new ByteArrayInputStream(reportOutput.getReportOutputData());
		return PRINT;
	}
	
	public Map createHeaderParams(){
		Map<String,Object> reportParams = new HashMap<String,Object>();
		List<WorksPackageDetails> worksPackageDetails=worksPackage.getWorksPackageDetails();
		AbstractEstimate estimate=worksPackageDetails.get(0).getEstimate();
		Boundary b = getTopLevelBoundary(estimate.getWard());
		reportParams.put("financialYear",abstractEstimateService.getCurrentFinancialYear(new Date()).getFinYearRange());
		reportParams.put("total",worksPackage.getTotalAmount());
		reportParams.put("cityName",b==null?"":b.getName());
		reportParams.put("workPackageName",worksPackage.getName());
		reportParams.put("worksPackageNumber",worksPackage.getWpNumber());
		reportParams.put("departmentName",worksPackage.getUserDepartment().getName());
		reportParams.put("tenderFileNumber",worksPackage.getTenderFileNumber());
		reportParams.put("estimateNumbers", getEstimateNumbers(worksPackage));
		return reportParams;
	}
	private String getEstimateNumbers(WorksPackage wp){
		String estimateNumbers="";
		for(WorksPackageDetails wpDetail:wp.getWorksPackageDetails()){
			estimateNumbers=estimateNumbers.concat(wpDetail.getEstimate().getEstimateNumber()).concat(",");
		}
		if(estimateNumbers.length()>1){
			estimateNumbers=estimateNumbers.substring(0,estimateNumbers.length()-1);
		}
		return estimateNumbers;
	}
	
	protected Boundary getTopLevelBoundary(Boundary boundary) {
		Boundary b = boundary;
		while(b!=null && b.getParent()!=null){
			b=b.getParent();
		}
		return b;
	}
	
	public void getDesignation(WorksPackage wp){
		if(wp.getEgwStatus()!= null 
				&& !"NEW".equalsIgnoreCase(wp.getEgwStatus().getCode())) {
			String result = worksService.getEmpNameDesignation(wp.getState().getOwnerPosition(), wp.getState().getCreatedDate().toDate());
			if(result != null && !"@".equalsIgnoreCase(result)) {
				String empName = result.substring(0,result.lastIndexOf('@'));
				String designation =result.substring(result.lastIndexOf('@')+1,result.length());
				setNextEmployeeName(empName);
				setNextDesignation(designation);
			}
		}
	}
	
	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}

	public String getEditableDate() {
		return editableDate;
	}

	public void setEditableDate(String editableDate) {
		this.editableDate = editableDate;
	}

	public String getCreatedBySelection() {
		return createdBySelection;
	}

	public void setCreatedBySelection(String createdBySelection) {
		this.createdBySelection = createdBySelection;
	}
	
	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	public Integer getEmpId() {
		return empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public void setWorkspackageService(WorksPackageService workspackageService) {
		this.workspackageService = workspackageService;
	}

	private PersonalInformation getEmployee() {
		if(worksPackage.getPreparedBy()==null)
			return employeeService.getEmpForUserId(worksService.getCurrentLoggedInUserId());
		else
			return worksPackage.getPreparedBy();
	}

	private Assignment getAssignment(PersonalInformation pi) {
		if(worksPackage.getPreparedBy()==null)
			return employeeService.getAssignmentByEmpAndDate(new Date(), pi.getIdPersonalInformation());
		else return employeeService.getAssignmentByEmpAndDate(new Date(), worksPackage.getPreparedBy().getIdPersonalInformation());
	}
	
	public String getPastDate() {
		return worksService.getWorksConfigValue("WORKS_PACKAGE_PASTDATE");
	}

	public String getCreatedBy() {
		return worksService.getWorksConfigValue("WORKS_PACKAGE_CREATEDBY");
	}
	
	public Long[] getEstId() {
		return estId;
	}

	public void setEstId(Long[] estId) {
		this.estId = estId;
	}

	public void setAbstractEstimateService(
			AbstractEstimateService abstractEstimateService) {
		this.abstractEstimateService = abstractEstimateService;
	}

	public List<AbstractEstimate> getAbstractEstimateList() {
		return abstractEstimateList;
	}

	public void setAbstractEstimateList(List<AbstractEstimate> abstractEstimateList) {
		this.abstractEstimateList = abstractEstimateList;
	}

	public Money getWorktotalValue() {
		return worktotalValue;
	}

	public void setWorktotalValue(Money worktotalValue) {
		this.worktotalValue = worktotalValue;
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

	public static String getPRINT() {
		return PRINT;
	}

	public InputStream getWorkspackagePDF() {
		return WorkspackagePDF;
	}

	public ReportService getReportService() {
		return reportService;
	}

	public void setWorkspackagePDF(InputStream workspackagePDF) {
		WorkspackagePDF = workspackagePDF;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public void setPackageWorkflowService(WorkflowService<WorksPackage> workflow) {
		this.workflowService = workflow;
	}
	
	public List<org.egov.infstr.workflow.Action> getValidActions(){
		return workflowService.getValidActions(worksPackage);
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	
	public String getMessageKey() {
		return messageKey;
	}

	public String getNextEmployeeName() {
		return nextEmployeeName;
	}

	public void setNextEmployeeName(String nextEmployeeName) {
		this.nextEmployeeName = nextEmployeeName;
	}

	public String getNextDesignation() {
		return nextDesignation;
	}

	public void setNextDesignation(String nextDesignation) {
		this.nextDesignation = nextDesignation;
	}
	public void setPersonalInformationService(
			PersonalInformationService personalInformationService) {
		this.personalInformationService = personalInformationService;
	}
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public String getPackageNumber() {
		return packageNumber;
	}

	public void setPackageNumber(String packageNumber) {
		this.packageNumber = packageNumber;
	}

	public void setEisService(EisUtilService eisService) {
		this.eisService = eisService;
	}

	public String getLoggedInUserEmployeeCode() {
		return loggedInUserEmployeeCode;
	}

	public void setLoggedInUserEmployeeCode(String loggedInUserEmployeeCode) {
		this.loggedInUserEmployeeCode = loggedInUserEmployeeCode;
	}

}