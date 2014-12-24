package org.egov.works.web.actions.rateContract;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.ContractorGrade;
import org.egov.commons.EgwStatus;
import org.egov.commons.Fund;
import org.egov.commons.service.CommonsService;
import org.egov.dao.budget.BudgetDetailsDAO;
import org.egov.dao.budget.BudgetGroupDAO;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.DateUtils;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.model.budget.BudgetGroup;
import org.egov.lib.rjbac.dept.ejb.api.DepartmentService;
import org.egov.infstr.workflow.WorkflowService;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;
import org.egov.pims.service.PersonalInformationService;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.config.AppConfigValues;
import org.egov.works.web.actions.estimate.AjaxEstimateAction;
import org.egov.utils.Constants;
import org.egov.web.actions.SearchFormAction;
import org.egov.web.actions.workflow.GenericWorkFlowAction;
import org.egov.works.models.masters.ScheduleOfRate;
import org.egov.works.models.rateContract.Indent;
import org.egov.works.models.rateContract.IndentDetail;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.IndentRateContractService;
import org.egov.works.services.WorksService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.web.actions.estimate.ShowBudgetDetailsAction;
import org.egov.infstr.models.StateAware;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportService;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.dispatcher.StreamResult;
import org.egov.pims.commons.Position;


@ParentPackage("egov")
@Result(name = IndentRateContractAction.PRINTINDENTRATECONTRACT, type="stream", location = "PrintIndentRateContractPDF", params = {"inputName", "PrintIndentRateContractPDF", "contentType", "application/pdf","contentDisposition", "no-cache" })
@SuppressWarnings("serial")
public class IndentRateContractAction extends GenericWorkFlowAction{

	private  Indent indent=new Indent();
	private Integer deptId;
	private String messageKey;
	private String sourcepage=""; 
	private String nextEmployeeName;
	private String nextDesignation;
	private String designation;
	private Long stateValue;
	
	private DepartmentService departmentService;
	private EmployeeService employeeService;
	private WorkflowService<Indent> workflowService;
	private PersonalInformationService personalInformationService;
	private PersistenceService<Indent, Long> indentService;
	private List<IndentDetail> sorIndentDetails = new LinkedList<IndentDetail>();
	private List<IndentDetail> nonSorIndentDetails = new LinkedList<IndentDetail>();
	private ShowBudgetDetailsAction showBudgetDetailsAction = new ShowBudgetDetailsAction();
	private WorksService worksService = new WorksService();
	private Map<String, Object> searchMap = new HashMap<String, Object>();
	public static final String PRINTINDENTRATECONTRACT = "printIndentRateContract";
	private static final String CONTRACT_MODULE_KEY = "IndentRateContract";
	private static final String Fund="fund";
	private static final String FUNCTION="function";
   	private CommonsService commonsService;
	private Date fromCreateDate;
	private Date toCreateDate;
	private static final String DATE_FORMAT = "dd-MMM-yyyy";
	private BudgetGroupDAO budgetGroupDAO;
	private IndentRateContractService indentRateContractService;
	private AbstractEstimateService abstractEstimateService;
	private Long id;
	private String mode = "";
	private String actionName="";
	private String indentType;
	private BigDecimal availableBudget;
	private BudgetDetailsDAO budgetDetailsDAO;
	private Integer functionaryid;
	private Integer schemeid;
	private Integer subschemeid;
	private BigDecimal budgetAvailable;
	private ReportService reportService;
	private InputStream printIndentRateContractPDF;
	private static final String PREPARED_BY_LIST = "preparedByList";
	private static final String DEPARTMENT_LIST = "departmentList";
	private static final String SAVE_ACTION = "save";
	
	private Integer dept=-1;
	private String tenderFileDate;
	
	private String source = "";
	private String status;

	public String newform() {
		return NEW;
	 }

	@Override
	public StateAware getModel() {
		// TODO Auto-generated method stub
		return indent;
	}
	protected void setModel(Indent indent) {
		this.indent = indent;
	}
	
	public String edit(){
		return EDIT;
	}
	
	public IndentRateContractAction() {
        addRelatedEntity("department", DepartmentImpl.class);
        addRelatedEntity("contractorGrade", ContractorGrade.class);
		addRelatedEntity(Fund, Fund.class);
		addRelatedEntity(FUNCTION, CFunction.class);
		addRelatedEntity("boundary", BoundaryImpl.class);
        addRelatedEntity("budgetGroup",BudgetGroup.class);
		addRelatedEntity("egwStatus",EgwStatus.class);
		addRelatedEntity("preparedBy", PersonalInformation.class);
	}
	
	public void prepare(){
		AjaxEstimateAction ajaxEstimateAction = new AjaxEstimateAction();
		if (id != null) 
		{
			indent = indentRateContractService.findById(id, false);
			setDesignation(getAssignment(indent.getPreparedBy()).getDesigId().getDesignationName());
			indentType = indent.getIndentType();
		}
		super.prepare();
		setupDropdownDataExcluding("egwStatus", "budgetGroup");
		addDropdownData("scheduleCategoryList", getPersistenceService().findAllBy("from ScheduleCategory order by upper(code)"));
		addDropdownData("uomList", getPersistenceService().findAllBy("from EgUom order by upper(uom)"));
		addDropdownData("boundaryList", getPersistenceService().findAllBy("from BoundaryImpl where upper(boundaryType.name) in ('ZONE') order by name"));
		addDropdownData("fundList", commonsService.getAllActiveIsLeafFunds());
		addDropdownData("functionList", commonsService.getAllFunction());
		AjaxIndentRateContractAction ajaxBudgetGroupAction = new AjaxIndentRateContractAction();
        if(indent.getDepartment()!=null && indent.getDepartment().getId()!=null){
        	deptId=indent.getDepartment().getId(); 
        }
		if(StringUtils.isNotBlank(getCreatedBy())){
			addDropdownData(DEPARTMENT_LIST,departmentService.getAllDepartments());
			ajaxEstimateAction.setPersonalInformationService(personalInformationService);
			populatePreparedByList(ajaxEstimateAction,deptId!=null) ;
		}
		else {
			addDropdownData(PREPARED_BY_LIST,Arrays.asList(getEmployee()));
			addDropdownData(DEPARTMENT_LIST,Arrays.asList(getAssignment(getEmployee()).getDeptId()));
		}

		if (id==null) 
		{
			populateBudgetGroupList(ajaxBudgetGroupAction, indent.getFunction() != null);
		} 
		else if(!indent.getIndentType().equals("Item"))
		{
			addDropdownData("budgetGroupList", getPersistenceService().findAllBy("from BudgetGroup where id = ?", indent.getBudgetGroup().getId()));
			
		}
		else 
		{
			addDropdownData("budgetGroupList", Collections.emptyList());
		}
        ArrayList<EgwStatus> statusList=(ArrayList<EgwStatus>) commonsService.getStatusByModule(CONTRACT_MODULE_KEY);
        statusList.remove(commonsService.getStatusByModuleAndCode(CONTRACT_MODULE_KEY, "NEW"));
        addDropdownData("statusList", statusList);	
		addDropdownData("executingDepartmentList",departmentService.getAllDepartments()); 
/*		if(abstractEstimateService.getLatestAssignmentForCurrentLoginUser()!=null) {
			indent.setWorkflowDepartmentId(abstractEstimateService.getLatestAssignmentForCurrentLoginUser().getDeptId().getId());
			
		}
*/		if(StringUtils.isNotBlank(source) && "tenderFile".equals(source)){
			indent.setEgwStatus(commonsService.getStatusByModuleAndCode(CONTRACT_MODULE_KEY, status));
		}
	}

	protected void populateBudgetGroupList(AjaxIndentRateContractAction ajaxBudgetGroupAction, boolean budgetPopulated) {
		if (budgetPopulated) 
		{
			ajaxBudgetGroupAction.setFunctionId(indent.getFunction().getId());
			ajaxBudgetGroupAction.loadBudgetGroups();
				if(ajaxBudgetGroupAction.getBudgetGroups()==null){
					addDropdownData("budgetGroupList", Collections.emptyList());
				}else{
			addDropdownData("budgetGroupList", ajaxBudgetGroupAction.getBudgetGroups());
				}
		} 
		else if (mode.equals("search")) 
		{
			addDropdownData("budgetList", getPersistenceService().findAllBy("select distinct indent.budgetGroup from Indent indent order by upper(indent.budgetGroup.name)"));
		} 
		else 
		{
			addDropdownData("budgetGroupList", Collections.emptyList());
		}
	}
	
	private void populatePreparedByList(AjaxEstimateAction ajaxEstimateAction, boolean executingDeptPopulated){
		if (executingDeptPopulated) {
			ajaxEstimateAction.setExecutingDepartment(deptId);
			ajaxEstimateAction.setWorksService(worksService);
			ajaxEstimateAction.setPersistenceService(persistenceService);
			ajaxEstimateAction.usersInExecutingDepartment();
			addDropdownData(PREPARED_BY_LIST,ajaxEstimateAction.getUsersInExecutingDepartment());
		}
		else {
			addDropdownData(PREPARED_BY_LIST,Collections.EMPTY_LIST);
		}
	}

	public boolean budgetCheckRequired(){
		boolean budgetCheck=false;
		List<AppConfigValues> list =worksService.getAppConfigValue("EGF","budgetCheckRequired");
		if(!list.isEmpty())
		{
			String value = list.get(0).getValue();
			if(value.equalsIgnoreCase("Y"))
			{
				budgetCheck=true;      
			}
		}
		return budgetCheck;
	}

	public String save(){
		String wfStatus=null;
		String actionName = parameters.get("actionName")[0];
		indent.getIndentDetails().clear();
		populateSorDetails();
		populateNonSorDetails();
		indent.setIsBudgetCheckRequired(budgetCheckRequired());
		if(getModel().getCurrentState()!=null){
			wfStatus=getModel().getCurrentState().getValue();
		}
		else{
			wfStatus="NEW";
		}
		if((actionName.equalsIgnoreCase("Forward")||actionName.equalsIgnoreCase("Approve")) && customizedWorkFlowService.getWfMatrix(getModel().getStateType(), getWorkFlowDepartment(), getAmountRule(), getAdditionalRule(),wfStatus, getPendingActions())==null){
			String msg="Workflow is not available for "+getWorkFlowDepartment();
			throw new ValidationException(Arrays.asList(new ValidationError(null,msg)));
		}

		if(!indent.getIndentType().equals("Item") && indent.getIsBudgetCheckRequired())
		{
			validateAmount();
		}
		else if(indent.getIndentType().equals("Item"))
		{
			indent.setFund(null);
			indent.setFunction(null);
			indent.setBudgetGroup(null);
			indent.setIndentAmount(null);
		}
		if(actionName.equalsIgnoreCase("save")){
			indent.setEgwStatus(commonsService.getStatusByModuleAndCode(CONTRACT_MODULE_KEY,"NEW"));
			indentRateContractService.setIndentRateContractNumber(indent, indentRateContractService.getCurrentFinancialYear(indent.getIndentDate())); 
			if(id ==null){
			Position pos = employeeService.getPositionforEmp(employeeService.getEmpForUserId(abstractEstimateService.getCurrentUserId()).getIdPersonalInformation());
			indent = (Indent) workflowService.start(indent, pos, "Indent contract created.");
			}
			messageKey="indent."+indent.getEgwStatus().getCode();
			addActionMessage(getText(messageKey,"The Indent was saved successfully"));
			indent = indentRateContractService.persist(indent);
			getDesignation(indent);
		}
		else if(mode!=null && !mode.equals("edit")){
			if(id ==null){
				indent.setEgwStatus(commonsService.getStatusByModuleAndCode(CONTRACT_MODULE_KEY,"NEW"));
				indentRateContractService.setIndentRateContractNumber(indent, indentRateContractService.getCurrentFinancialYear(indent.getIndentDate()));
				Position pos = employeeService.getPositionforEmp(employeeService.getEmpForUserId(abstractEstimateService.getCurrentUserId()).getIdPersonalInformation());
				indent = (Indent) workflowService.start(indent, pos, "Indent contract created.");
			}
			workflowService.transition(actionName, indent, approverComments);
			indent = indentRateContractService.persist(indent);
			messageKey="indent."+indent.getEgwStatus().getCode();
			getDesignation(indent);
		}
		if(SAVE_ACTION.equalsIgnoreCase(parameters.get("actionName")[0]) && indent.getIndentType().equals("Amount")){
			addDropdownData("budgetGroupList", getPersistenceService().findAllBy("from BudgetGroup where id = ?", indent.getBudgetGroup().getId()));
		}
		return SAVE_ACTION.equalsIgnoreCase(actionName)?EDIT:SUCCESS;
	}

/*	public String cancel(){
		if(indent.getId()!=null){
			workflowService.transition("Cancel", indent,approverComments);
			indent=indentRateContractService.persist(indent);
		}
		messageKey="indent.cancel";	
		getDesignation(indent);
		return SUCCESS;
	}	

	public String reject(){
		workflowService.transition(Indent.Actions.REJECT.toString(),indent,approverComments);
		indent=indentRateContractService.persist(indent);
		messageKey="indent.reject";	
		getDesignation(indent);
		return SUCCESS;
	}	
*/	
	public void validateAmount() {
		BudgetGroup budgetGroup = budgetGroupDAO.getBudgetHeadById(indent.getBudgetGroup().getId());
		List<BudgetGroup> budgetGroupsList = new ArrayList<BudgetGroup>();
		budgetGroupsList.add(budgetGroup);
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Constants.LOCALE);
		CFinancialYear financialyear = commonsService.getFinancialYearByDate(indent.getValidity().getStartDate());
		getSearchMap().put("financialyearid", financialyear.getId());
		getSearchMap().put("budgetheadid", budgetGroupsList);
		getSearchMap().put("functionid", indent.getFunction().getId());
		getSearchMap().put("functionaryid", getFunctionaryid());
		getSearchMap().put("fundid", indent.getFund().getId());
		getSearchMap().put("schemeid", getSchemeid());
		getSearchMap().put("subschemeid", getSubschemeid());
		getSearchMap().put("deptid", indent.getDepartment().getId());
		getSearchMap().put("boundaryid", indent.getBoundary().getId());
		getShowBudgetDetailsAction().setBudgetDetailsDAO(budgetDetailsDAO);
		getShowBudgetDetailsAction().setWorksService(worksService);
		List<Long> budgethead = new ArrayList<Long>();
		budgethead.add(indent.getBudgetGroup().getId());
		BigDecimal bugetavilable = getShowBudgetDetailsAction().getBudgetAvailable(searchMap);
		Double availBudget = bugetavilable.doubleValue();
		Double amount = indent.getIndentAmount().getValue();
		if (amount > availBudget) 
		{
			addDropdownData("budgetGroupList", getPersistenceService().findAllBy("from BudgetGroup where id = ?", indent.getBudgetGroup().getId()));
			throw new ValidationException(Arrays.asList(new ValidationError("rateContract.budget.notavailable","rateContract.budget.notavailable")));
		}
	}

	protected void populateSorDetails() {
		for(IndentDetail detail: sorIndentDetails) 
		 {
			 if (validSorDetails(detail)) 
			 {
				 detail.setScheduleOfRate((ScheduleOfRate) getPersistenceService().find("from ScheduleOfRate where id = ?", detail.getScheduleOfRate().getId()));
				 detail.setIndent(indent);
				 indent.addIndentDetails(detail);
			}
		 }
	 }
	 
	protected boolean validSorDetails(IndentDetail detail) {
		 if (detail != null && detail.getScheduleOfRate() != null && detail.getScheduleOfRate().getId() != null) {
			 return true;
		 }
		 
		 return false;
	 }
	 
	protected void populateNonSorDetails() {
		 for (IndentDetail detail: nonSorIndentDetails) {
			 if (detail!=null) 
			 {
				 detail.setIndent(indent);
				 indent.addIndentDetails(detail);
			 }
		 }
	 }
	 
	public String search(){
		 return "search";
	 }

	@SkipValidation
	public String viewIndentRateContractPDF() throws Exception{
		int isSor=0;
		int isNonSor=0;
		Map<String,Object> reportParams = new HashMap<String,Object>();
		if(indent != null){
			reportParams.put("indent",indent);

			for(IndentDetail indentDetail:indent.getIndentDetails()){
				if(indentDetail.getScheduleOfRate()!=null){
					isSor=1;
					break;
		}
			}
			for(IndentDetail indentDetail:indent.getIndentDetails()){
				if(indentDetail.getNonSor()!=null){
					isNonSor=1;
					break;
				}
			}
		}
		reportParams.put("isSor", isSor);
		reportParams.put("isNonSor", isNonSor);
		ReportRequest reportInput = new ReportRequest("indentRateContract",new ArrayList<Object>(), reportParams);
		ReportOutput reportOutput = reportService.createReport(reportInput);   
		if (reportOutput != null && reportOutput.getReportOutputData() != null)
			printIndentRateContractPDF = new ByteArrayInputStream(reportOutput.getReportOutputData());
		return PRINTINDENTRATECONTRACT;
	}

	 public String searchDetails(){
		String actions = worksService.getWorksConfigValue("INDENT_SHOW_ACTIONS");
		if(toCreateDate!=null && !DateUtils.compareDates(new Date(),getToCreateDate())){
			addFieldError("toCreateDate",getText("greaterthan.toCreateDate.currentdate"));
		}
		if(fromCreateDate!=null && !DateUtils.compareDates(new Date(),getFromCreateDate())){
			addFieldError("toCreateDate",getText("greaterthan.fromCreateDate.currentdate"));
		}
	 	if(fromCreateDate!=null && toCreateDate!=null && !DateUtils.compareDates(getToCreateDate(),getFromCreateDate())){
			addFieldError("fromCreateDate",getText("greaterthan.toCreateDate.fromCreateDate"));
	 	}
		if(!getFieldErrors().isEmpty())
			return "search";

		setPageSize(WorksConstants.PAGE_SIZE);
		super.search();
		if(searchResult.getFullListSize() !=0){
			List<Indent> indentList=new ArrayList<Indent>();
			  for(Indent indent:(List<Indent>) searchResult.getList()){
				if(StringUtils.isNotBlank(actions)){
					indent.getIndentActions().addAll(Arrays.asList(actions.split(",")));
				}
				indentList.add(indent);
			  }
			searchResult.getList().clear();
			searchResult.getList().addAll(indentList);
		}

		return "search";
	 }
	 
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public Indent getIndent() {
		return indent;
	}

	public void setIndent(Indent indent) {
		this.indent = indent;
	}
	
	public Date getFromCreateDate() {
		return fromCreateDate;
	}

	public void setFromCreateDate(Date fromCreateDate) {
		this.fromCreateDate = fromCreateDate;
	}

	public Date getToCreateDate() {
		return toCreateDate;
	}

	public void setToCreateDate(Date toCreateDate) {
		this.toCreateDate = toCreateDate;
	}
	
	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	public PersistenceService<Indent, Long> getIndentService() {
		return indentService;
	}

	public void setIndentService(PersistenceService<Indent, Long> indentService) {
		this.indentService = indentService;
	}

	public CommonsService getCommonsService() {
		return commonsService;
	}

	public List<IndentDetail> getSorIndentDetails() {
		return sorIndentDetails;
	}

	public void setSorIndentDetails(List<IndentDetail> sorIndentDetails) {
		this.sorIndentDetails = sorIndentDetails;
	}

	public List<IndentDetail> getNonSorIndentDetails() {
		return nonSorIndentDetails;
	}

	public void setNonSorIndentDetails(List<IndentDetail> nonSorIndentDetails) {
		this.nonSorIndentDetails = nonSorIndentDetails;
	}

	public BudgetGroupDAO getBudgetGroupDAO() {
		return budgetGroupDAO;
	}

	public void setBudgetGroupDAO(BudgetGroupDAO budgetGroupDAO) {
		this.budgetGroupDAO = budgetGroupDAO;
	}	
	
	public IndentRateContractService getIndentRateContractService() {
		return indentRateContractService;
	}

	public void setIndentRateContractService(
			IndentRateContractService indentRateContractService) {
		this.indentRateContractService = indentRateContractService;
	}
	
	public AbstractEstimateService getAbstractEstimateService() {
		return abstractEstimateService;
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

	
	public String getIndentType() {
		return indentType;
	}

	public void setIndentType(String indentType) {
		this.indentType = indentType;
	}

	public BigDecimal getAvailableBudget() {
		return availableBudget;
	}

	public void setAvailableBudget(BigDecimal availableBudget) {
		this.availableBudget = availableBudget;
	}

	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder){
		StringBuffer indentSql = new StringBuffer();
		List<Object> paramList = new ArrayList<Object>();
		boolean isError=false;
		EgwStatus egwStatus=(EgwStatus) commonsService.getStatusByModuleAndCode(CONTRACT_MODULE_KEY,"NEW");

		indentSql.append(" from Indent indent where indent.id is not null and indent.egwStatus.id<>"+egwStatus.getId());

		if(fromCreateDate!=null && toCreateDate==null){
			indentSql.append(" and indent.indentDate >= ? ");
			paramList.add(new Date(DateUtils.getFormattedDate(getFromCreateDate(),DATE_FORMAT )));
		}else if(fromCreateDate!=null && toCreateDate!=null){
			indentSql.append(" and indent.indentDate between ? and ? ");
			paramList.add(new Date(DateUtils.getFormattedDate(getFromCreateDate(),DATE_FORMAT )));
			paramList.add(new Date(DateUtils.getFormattedDate(getToCreateDate(),DATE_FORMAT)));
		}
		if(indent.getDepartment()!=null && indent.getDepartment().getId()!=-1){
			indentSql.append(" and indent.department.id = ? ");
			paramList.add(indent.getDepartment().getId());
		}
		if(StringUtils.isNotBlank(indent.getIndentNumber())){
			indentSql.append(" and UPPER(indent.indentNumber) like '%"+indent.getIndentNumber().toString().trim().toUpperCase()+"%'");
		}
		if(indent.getIndentType()!=null && !indent.getIndentType().equals("-1")){
			indentSql.append(" and indent.indentType=?");
			paramList.add(indent.getIndentType());
		}
		if(indent.getEgwStatus()!=null && indent.getEgwStatus().getId()!=-1){
			indentSql.append(" and indent.egwStatus.id=?");
			paramList.add(indent.getEgwStatus().getId());
		}
		if(indent.getBudgetGroup()!=null && indent.getBudgetGroup().getId()!=-1){
			indentSql.append(" and indent.budgetGroup.id=?");
			paramList.add(indent.getBudgetGroup().getId());
		}
		if(indent.getContractorGrade()!=null && indent.getContractorGrade().getId()!=-1){
			indentSql.append(" and indent.contractorGrade.id=?");
			paramList.add(indent.getContractorGrade().getId());
		}
		if(StringUtils.isNotBlank(tenderFileDate)){
			Date fileDate=null;
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy",new Locale("en","IN"));
			try{
				fileDate = formatter.parse(tenderFileDate);
			}catch(ParseException e){
				isError=true;
				addFieldError("parse exception", "Date Conversion Error");  
			}
			indentSql.append(" and trunc(indent.state.previous.createdDate)<='"+DateUtils.getFormattedDate(fileDate,DATE_FORMAT )+"' ");
		}	
				
		indentSql.append(" order by indent.id");

		String countQuery = "select count(*) " + indentSql.toString();
		return new SearchQueryHQL(indentSql.toString(), countQuery, paramList);
	}

	public Map<String, Object> getSearchMap() {
		return searchMap;
	}

	public void setSearchMap(Map<String, Object> searchMap) {
		this.searchMap = searchMap;
	}

	public BudgetDetailsDAO getBudgetDetailsDAO() {
		return budgetDetailsDAO;
	}

	public void setBudgetDetailsDAO(BudgetDetailsDAO budgetDetailsDAO) {
		this.budgetDetailsDAO = budgetDetailsDAO;
	}

	public Integer getFunctionaryid() {
		return functionaryid;
	}

	public void setFunctionaryid(Integer functionaryid) {
		this.functionaryid = functionaryid;
	}

	public Integer getSchemeid() {
		return schemeid;
	}

	public void setSchemeid(Integer schemeid) {
		this.schemeid = schemeid;
	}

	public Integer getSubschemeid() {
		return subschemeid;
	}

	public void setSubschemeid(Integer subschemeid) {
		this.subschemeid = subschemeid;
	}

	public ShowBudgetDetailsAction getShowBudgetDetailsAction() {
		return showBudgetDetailsAction;
	}

	public void setShowBudgetDetailsAction(
			ShowBudgetDetailsAction showBudgetDetailsAction) {
		this.showBudgetDetailsAction = showBudgetDetailsAction;
	}

	public WorksService getWorksService() {
		return worksService;
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}

	public BigDecimal getBudgetAvailable() {
		return budgetAvailable;
	}

	public void setBudgetAvailable(BigDecimal budgetAvailable) {
		this.budgetAvailable = budgetAvailable;
	}
	
	public ReportService getReportService() {
		return reportService;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public InputStream getPrintIndentRateContractPDF() {
		return printIndentRateContractPDF;
	}

	public void setPrintIndentRateContractPDF(InputStream printIndentRateContractPDF) {
		this.printIndentRateContractPDF = printIndentRateContractPDF;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getSource() {
		return source;
	}
	
	public void setSource(String source) {
		this.source = source;
	}
	
	public Integer getDept() {
		return dept;
	}
	
	public void setDept(Integer dept) {
		this.dept = dept;
	}

	public String getTenderFileDate() {
		return tenderFileDate;
	}

	public void setTenderFileDate(String tenderFileDate) {
		this.tenderFileDate = tenderFileDate;
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

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
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

	public PersonalInformationService getPersonalInformationService() {
		return personalInformationService;
	}

	public void setPersonalInformationService(
			PersonalInformationService personalInformationService) {
		this.personalInformationService = personalInformationService;
	}
	private PersonalInformation getEmployee() {
		if(indent.getPreparedBy()==null)
			return employeeService.getEmpForUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
		else
			return indent.getPreparedBy();
	}
	
	private Assignment getAssignment(PersonalInformation pi) {
		if(indent.getPreparedBy()==null)
			return employeeService.getAssignmentByEmpAndDate(new Date(), pi.getIdPersonalInformation());
		else return employeeService.getAssignmentByEmpAndDate(new Date(), indent.getPreparedBy().getIdPersonalInformation());
	}
	
	public String getCreatedBy() {
		return worksService.getWorksConfigValue("INDENT_PREPAREDBY"); 
	}
	
	public void getDesignation(Indent indent){
		if(indent.getCurrentState()!= null 
				&& !"NEW".equalsIgnoreCase(indent.getCurrentState().getValue())) {
			String result = worksService.getEmpNameDesignation(indent.getState().getOwner(),indent.getState().getCreatedDate());
			if(result != null && !"@".equalsIgnoreCase(result)) {
				String empName = result.substring(0,result.lastIndexOf('@'));
				String designation =result.substring(result.lastIndexOf('@')+1,result.length());
				setNextEmployeeName(empName);
				setNextDesignation(designation);
			}
		}
	}
	
	/*public List<org.egov.infstr.workflow.Action> getValidActions(){
		return workflowService.getValidActions(indent); 		
	}*/
	
	public void setIndentWorkflowService(WorkflowService<Indent> workflow) {
		this.workflowService = workflow;
	}

	public Long getStateValue() {
		return stateValue;
	}

	public void setStateValue(Long stateValue) {
		this.stateValue = stateValue;
	}

	public String workflowHistory(){
		return "history";
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getPendingActions()
	{
		return indent==null?"":
			(indent.getCurrentState()==null?"":indent.getCurrentState().getNextAction());
	}

	public String getWorkFlowDepartment(){
		return indent==null?"":(indent.getDepartment()==null?"":indent.getDepartment().getDeptName());
	}
	

}