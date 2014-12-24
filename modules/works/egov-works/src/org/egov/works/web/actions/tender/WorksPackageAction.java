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
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.models.Money;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.workflow.SimpleWorkflowService;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.lib.rjbac.dept.ejb.api.DepartmentService;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.PersonalInformation;
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

@Result(name=WorksPackageAction.PRINT,type="stream",location="WorkspackagePDF", params={"inputName","WorkspackagePDF","contentType","application/pdf","contentDisposition","no-cache"})
public class WorksPackageAction extends BaseFormAction{	
	private String editableDate="yes";
	private String createdBySelection="no";
	private WorksService worksService;
	private EmployeeService employeeService;
	private DepartmentService departmentService;
	private WorksPackage worksPackage = new WorksPackage();
	private String designation;
	private Integer empId;
	private Long[] estId;
	private String sourcepage;
	private Money worktotalValue;
	private SimpleWorkflowService<WorksPackage> workflowService;
	private WorksPackageService workspackageService;
	private AbstractEstimateService abstractEstimateService;
	private List<AbstractEstimate> abstractEstimateList=new ArrayList<AbstractEstimate>();
	private Long id;
	private String actionName;
	private String messageKey;
	private String nextEmployeeName;
	private String nextDesignation;
	private static final String PREPARED_BY_LIST = "preparedByList";
	private static final String DEPARTMENT_LIST = "departmentList";
	
	
	/**
	 * pdf variable declaration
	 */
	
	public static final String PRINT = "print";	
	private InputStream WorkspackagePDF;
	private ReportService reportService;
	private PersonalInformationService personalInformationService;

	public WorksPackageAction() {
		addRelatedEntity("userDepartment", DepartmentImpl.class);
	}
	
	public void prepare(){	
		AjaxEstimateAction ajaxEstimateAction = new AjaxEstimateAction();
		ajaxEstimateAction.setPersistenceService(getPersistenceService());
		ajaxEstimateAction.setEmployeeService(employeeService);
		ajaxEstimateAction.setPersonalInformationService(personalInformationService);
		ajaxEstimateAction.setAbstractEstimateService(abstractEstimateService);
		super.prepare();
		if (id != null) {			
			worksPackage= workspackageService.findById(id, false);
		}
		setupDropdownDataExcluding("userDepartment");
		if(StringUtils.isNotBlank(getCreatedBy()) && "yes".equalsIgnoreCase(getCreatedBy())){
			setCreatedBySelection(getCreatedBy());
			addDropdownData(DEPARTMENT_LIST,departmentService.getAllDepartments());
			populatePreparedByList(ajaxEstimateAction, worksPackage.getUserDepartment() != null);
		}
		else {
			addDropdownData(PREPARED_BY_LIST,Arrays.asList(getEmployee()));
			addDropdownData(DEPARTMENT_LIST,Arrays.asList(getAssignment(getEmployee()).getDeptId()));
		}
		if(StringUtils.isNotBlank(getPastDate()))
			setEditableDate(getPastDate());
		addDropdownData("executingDepartmentList",departmentService.getAllDepartments());
		if(abstractEstimateService.getLatestAssignmentForCurrentLoginUser()!=null) {
			worksPackage.setWorkflowDepartmentId(abstractEstimateService.getLatestAssignmentForCurrentLoginUser().getDeptId().getId());
		}
	}

	public String newform(){
		PersonalInformation pi = getEmployee();
		Assignment assignment = getAssignment(pi);
		if(assignment!=null && "no".equalsIgnoreCase(getCreatedBy())){
			setDesignation(assignment.getDesigId().getDesignationName());
			worksPackage.setUserDepartment(assignment.getDeptId());
			setEmpId(pi.getId());
		}
		
		return NEW;
	}
	
	
	public String edit()
	{
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
		
		if(worksPackage.getId()!=null)worksPackage.getWorksPackageDetails().clear();
		populateEstimatesList(estId);
		worksPackage.setPreparedBy(employeeService.getEmloyeeById(empId));
		workspackageService.setWorksPackageNumber(worksPackage,abstractEstimateService.getCurrentFinancialYear(worksPackage.getPackageDate()));
		worksPackage=workspackageService.persist(worksPackage);
		workflowService.transition(actionName, worksPackage,"");
		messageKey="worksPackage."+actionName;
		addActionMessage(getText(messageKey,"The Works Package was saved successfully"));
		getDesignation(worksPackage);	
		return SUCCESS; 
	}
	
	public String cancel(){
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
		reportParams.put("departmentName",worksPackage.getUserDepartment().getDeptName());
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
		if(wp.getCurrentState()!= null 
				&& !"NEW".equalsIgnoreCase(wp.getCurrentState().getValue())) {
			String result = worksService.getEmpNameDesignation(wp.getState().getOwner(),wp.getState().getCreatedDate());
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
			return employeeService.getEmpForUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
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

	public void setPackageWorkflowService(SimpleWorkflowService<WorksPackage> workflow) {
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

	public void setWorkflowService(
			SimpleWorkflowService<WorksPackage> workflowService) {
		this.workflowService = workflowService;
	}
	
}