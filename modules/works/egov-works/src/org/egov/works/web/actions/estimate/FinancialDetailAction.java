package org.egov.works.web.actions.estimate;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.dispatcher.StreamResult;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.commons.service.CommonsService;
import org.egov.dao.budget.BudgetDetailsDAO;
import org.egov.dao.budget.BudgetGroupDAO;
import org.egov.infstr.ValidationException;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.workflow.WorkflowService;
import org.egov.model.budget.BudgetGroup;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.estimate.BudgetFolioDetail;
import org.egov.works.models.estimate.FinancialDetail;
import org.egov.works.models.estimate.FinancingSource;
import org.egov.works.models.masters.DepositCode;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.DepositWorksUsageService;
import org.egov.works.services.WorksService;

@Result(name=FinancialDetailAction.PRINT,type="stream",location="budgetFolioPDF", params={"inputName","budgetFolioPDF","contentType","application/pdf","contentDisposition","no-cache"})
public class FinancialDetailAction extends BaseFormAction
{
	private static final Logger logger = Logger.getLogger(FinancialDetailAction.class);
	private static final String BUDGET_GROUP_LIST = "budgetGroupList"; 
	private FinancialDetail financialDetail = new FinancialDetail();
	private AbstractEstimateService abstractEstimateService;
	private WorkflowService<AbstractEstimate> estimateWorkflowService;
	private static final String MODULE_NAME = "Works";
	private static final String KEY_NAME = "SKIP_BUDGET_CHECK";
	
	private AbstractEstimate abstractEstimate;
	private List<FinancingSource> financingSourceList = new LinkedList<FinancingSource>();
	private List<Fundsource> fundSourceList;
	private Long estimateId;
	private Long id;
	private CommonsService commonsService;	
	private String status="TECH_SANCTIONED";
	public static final String ADD="add";
	private BudgetGroupDAO budgetGroupDAO;
	private Date financialYearStartDate;
	private Integer approverUserId;
	private Integer departmentId; 
	private Integer designationId;
	private String approverComments;
	private boolean skipBudget=false;
	
	/* added by prashanth on 2nd nov 09 for disp user and desgination in success page*/
	String employeeName;
	String designation;
	private WorksService worksService;
	
	
	private static final String VIEW_BUDGET_FOLIO="viewBudgetFolio";
	public static final String PRINT = "print";	
	private List<BudgetFolioDetail> approvedBudgetFolioDetails;	
	private BigDecimal totalGrant=BigDecimal.ZERO;
	private BigDecimal totalGrantPerc=BigDecimal.ZERO;
	private InputStream budgetFolioPDF;
	private ReportService reportService;
	private Double latestCumulative=0.00D;
	private BigDecimal latestBalance=BigDecimal.ZERO;
	private BigDecimal totalDepositAmount=BigDecimal.ZERO;
	private static final String PERCENTAGE_GRANT="percentage_grant";
	private static final String SEARCH_BUDGET_FOLIO = "searchBudgetFolio";
	private static final String SEARCH_DEPOSIT_WORKS_FOLIO = "searchDepositWorksFolio";
	private static final String BUDGET_GROUP_SEARCH_LIST = "budgetHeadList";
	private Map<String, Object> queryParamMap=new HashMap<String,Object>();
	private Integer executingDepartment;
	private Date reportDate;
	private BudgetDetailsDAO budgetDetailsDAO;
	private Map<String, String> mandatoryFields = new HashMap<String, String>();
	private String option;
	private String deptName="";
	private static final String Fund="fund";
	private static final String FUNCTION="function";
	private static final String COA_LIST="coaList";	
	private static final String APP="app";
	private static final String KEY_DEPOSIT ="WORKS_DEPOSIT_OTHER_WORKS";
	private Long depositCodeId;
	
	private String appValue;
	
	private String appValueLabel;
	private DepositWorksUsageService depositWorksUsageService;
	private PersistenceService<DepositCode,Long> depositCodeService;
	public static final String RESULTS="searchResult"; 
	private Date asOnDate;
	private String code; 
	
	
	public FinancialDetail getFinancialDetail() {
		return financialDetail;
	}
	
	public void setFinancialDetail(FinancialDetail financialDetail) {
		this.financialDetail = financialDetail;
	}

	public Long getEstimateId() {
		return estimateId;
	}

	public void setEstimateId(Long estimateId) {
		this.estimateId = estimateId;
	}

	public FinancialDetailAction()
	{
		addRelatedEntity(Fund, Fund.class);
		addRelatedEntity(FUNCTION, CFunction.class);
		addRelatedEntity("functionary", Functionary.class);
		addRelatedEntity("scheme", Scheme.class);
		addRelatedEntity("subScheme", SubScheme.class);
		addRelatedEntity("budgetGroup", BudgetGroup.class);
		addRelatedEntity("coa", CChartOfAccounts.class);
		
	}
	
	public String execute()
	{
		return ADD;
	}
	
	public String add()
	{
		return ADD;
	}
	
	public String edit(){
		return ADD;
	}
	
	public String save()
	{
		populateFinancialDetail();
		persistFinancialDetail();
		
		addActionMessage("The financial details for estimate " 
				+ abstractEstimate.getEstimateNumber() + " was saved successfully");   
		return EDIT;  
	}
	
	
	public String saveAndSubmit(){	
		populateFinancialDetail();		
		persistFinancialDetail();
		String actionName = parameters.get("actionName")[0];
		abstractEstimate.setApproverUserId(approverUserId);
		abstractEstimate = (AbstractEstimate) estimateWorkflowService.transition(actionName, abstractEstimate, approverComments);
		
		addActionMessage("The financial details for estimate " 
			+ abstractEstimate.getEstimateNumber() + " was submitted successfully.");
		
		/* start for customizing workflow message display */
		if(abstractEstimate.getCurrentState()!= null 
				&& !"NEW".equalsIgnoreCase(abstractEstimate.getCurrentState().getValue())) {
			String result = worksService.getEmpNameDesignation(abstractEstimate.getState().getOwner(),abstractEstimate.getState().getCreatedDate());
			if(result != null && !"@".equalsIgnoreCase(result)) {
				String empName = result.substring(0,result.lastIndexOf('@'));
				String designation =result.substring(result.lastIndexOf('@')+1,result.length());
				setEmployeeName(empName);
				setDesignation(designation);
			}
		}
		/* end */
		return SUCCESS;
	}
	
	private void populateFinancialDetail(){
		financialDetail.getFinancingSources().clear();
		populateFinancingSourceDetails();
		
	}
	
	private void persistFinancialDetail(){
	
		if(depositCodeId!=null && depositCodeId!=-1) {
			abstractEstimate.setDepositCode(depositCodeService.findById(depositCodeId, false));
		}
		abstractEstimate = abstractEstimateService.persistFinancialDetail(
				financialDetail, abstractEstimate);
		//to lazy load the financial detail id.
		abstractEstimate.getFinancialDetails().get(0).getId();
		
		financialDetail=abstractEstimate.getFinancialDetails().get(0);
	}
	
	
	public void prepare()  {
		AjaxFinancialDetailAction ajaxFinancialDetailAction = 
			new AjaxFinancialDetailAction();
		ajaxFinancialDetailAction.setPersistenceService(getPersistenceService());
		ajaxFinancialDetailAction.setBudgetGroupDAO(budgetGroupDAO);
		abstractEstimateService.setBudgetGroupDAO(budgetGroupDAO);
		abstractEstimate = abstractEstimateService.findById(estimateId, false);
		if(abstractEstimate!=null){
			if(abstractEstimate.getFinancialDetails()!=null && 
					abstractEstimate.getFinancialDetails().size()>0){
				financialDetail=abstractEstimate.getFinancialDetails().get(0);
			}

			financialDetail.setAbstractEstimate(abstractEstimate);
		}
		super.prepare(); 
	
		setupDropdownDataExcluding(Fund,FUNCTION,"functionary","scheme","subScheme","budgetGroup","coa");
		
		addDropdownData("fundList", commonsService.getAllActiveIsLeafFunds());
		addDropdownData("functionList", commonsService.getAllFunction());
		addDropdownData("functionaryList", commonsService.getActiveFunctionaries());
		addDropdownData("executingDepartmentList", getPersistenceService().findAllBy("from DepartmentImpl")); 
		try {
			addDropdownData(BUDGET_GROUP_LIST, 
					(List<BudgetGroup>) budgetGroupDAO.getBudgetGroupList());
			addDropdownData(BUDGET_GROUP_SEARCH_LIST,new ArrayList<BudgetGroup>());
		} catch (Exception e1) {
			addFieldError("budgetunavailable", "Unable to load budget information");
		}
		populateSchemeList(ajaxFinancialDetailAction, financialDetail.getFund()!=null,
				financialDetail.getAbstractEstimate()!=null);
		populateSubSchemeList(ajaxFinancialDetailAction, 
				financialDetail.getScheme() != null,
				financialDetail.getAbstractEstimate()!=null);
		try {
			populateBudgetGroupList(ajaxFinancialDetailAction, 
					financialDetail.getFunction()!=null, 
					financialDetail.getAbstractEstimate()!=null);
			populateBudgetHeadList(ajaxFinancialDetailAction, 
					financialDetail.getFunction()!=null, 
					getReportDate()!=null);
		} catch (EGOVException e) {
			addFieldError("budgetunavailable", "Unable to load budget data");
		} catch (Exception e) {
			addFieldError("budgetunavailable", "Unable to load budget data from database");
		}
		if(getDropdownData().get(BUDGET_GROUP_LIST)==null){
			addDropdownData(BUDGET_GROUP_LIST, new ArrayList<BudgetGroup>());
		}
		
		try {
			fundSourceList = commonsService.getAllActiveIsLeafFundSources();
		} catch (EGOVException e) {
			addFieldError("fundsourceunavailable", "Unable to load fund source information");
		}
		if(abstractEstimateService.getLatestAssignmentForCurrentLoginUser()!=null) {
			departmentId=abstractEstimateService.getLatestAssignmentForCurrentLoginUser().getDeptId().getId();
		}
		checkMandataryFields();
		if(isSkipBudgetCheck()) {
			try {			
					if(StringUtils.isNotBlank(worksService.getWorksConfigValue(KEY_DEPOSIT))){
						addDropdownData(COA_LIST, commonsService.getAccountCodeByPurpose(Integer.valueOf(worksService.getWorksConfigValue(KEY_DEPOSIT))));
					}
					else  
						addDropdownData(COA_LIST,Collections.EMPTY_LIST);			
			}
			catch(EGOVException v) {		
				logger.error("Unable to load Account Codes for Deposit Works" + v.getMessage());
				addFieldError("accountcodes.notfound", "abstractEstimate.deposit.accountcodes.error");
			}
		}	
		else   
			addDropdownData(COA_LIST,Collections.EMPTY_LIST);
		
		if(!StringUtils.isBlank(option) && ("input".equalsIgnoreCase(option) || "searchDepositWorksFolioDetails".equalsIgnoreCase(option))){
			asOnDate=new Date();
			try{	
				String config = worksService.getWorksConfigValue("SLDEPOSITCODE_SHOW_FUNDS"); 
				List<String> code = new ArrayList<String>();
				
				if(config==null) {
					addDropdownData("fundList",code);						 
				} 
				else {
					for(int i=0;i<config.split(",").length;i++)
						code.add(config.split(",")[i]);
					addDropdownData("fundList",getPersistenceService().findAllByNamedQuery("getListOfFundsForCodes", code));
				}
					
			}catch(Exception v){	
				addFieldError("Fund.notfound", "depositWorksFolioReport.loadFund.error");
			}	
			try{		
				if(StringUtils.isNotBlank(worksService.getWorksConfigValue(KEY_DEPOSIT))){
					addDropdownData(COA_LIST, commonsService.getAccountCodeByPurpose(Integer.valueOf(worksService.getWorksConfigValue(KEY_DEPOSIT))));
				}
				else{
					addDropdownData(COA_LIST,Collections.EMPTY_LIST);
				}
			}catch(EGOVException v) {	
				addFieldError("accountcodes.notfound", "depositWorksFolioReport.loadAccountCodes.error");
			}
		}
	}
	
	public void checkMandataryFields(){		
		List<AppConfigValues> appConfigList = worksService.getAppConfigValue("EGF","budgetaryCheck_groupby_values");
		AppConfigValues appConfigValues =null; 
		if(appConfigList!=null)
		 appConfigValues = appConfigList.get(0);
		
		if(appConfigValues!=null)
			if(appConfigValues.getValue().indexOf(",") == -1){ 
				mandatoryFields.put(appConfigValues.getValue(), "M");
			}else{
				String[] values = StringUtils.split(appConfigValues.getValue(),",");
				for (String value : values) {
					mandatoryFields.put(value, "M");
				}
			}
	}
	
	
	protected void populateSubSchemeList(
			AjaxFinancialDetailAction ajaxFinancialDetailAction, 
			boolean schemePopulated,boolean datePresent) {
		if (schemePopulated && datePresent) {
			ajaxFinancialDetailAction.setSchemeId(financialDetail.getScheme().getId());
			ajaxFinancialDetailAction.setEstimateDate(
					financialDetail.getAbstractEstimate().getEstimateDate());
			ajaxFinancialDetailAction.loadSubSchemes();
			addDropdownData("subSchemeList", ajaxFinancialDetailAction.getSubSchemes());		
		}
		else {
			addDropdownData("subSchemeList", Collections.emptyList());
		}
	}
	
	protected void populateSchemeList(AjaxFinancialDetailAction ajaxFinancialDetailAction,
			boolean fundPopulated, boolean datePresent) {
		if (fundPopulated && datePresent) {
			ajaxFinancialDetailAction.setFundId(financialDetail.getFund().getId());
			ajaxFinancialDetailAction.setEstimateDate(
					financialDetail.getAbstractEstimate().getEstimateDate());
			ajaxFinancialDetailAction.loadSchemes();
			addDropdownData("schemeList", ajaxFinancialDetailAction.getSchemes());		
		}
		else {
			addDropdownData("schemeList", Collections.emptyList());
		}
	}
	
	
	protected void populateBudgetGroupList(
			AjaxFinancialDetailAction ajaxFinancialDetailAction, 
			boolean functionPopulated, boolean datePresent) throws Exception {
		if (functionPopulated && datePresent) {
			ajaxFinancialDetailAction.setFunctionId(
					financialDetail.getFunction().getId());
			ajaxFinancialDetailAction.setEstimateDate(
					financialDetail.getAbstractEstimate().getEstimateDate());
			ajaxFinancialDetailAction.loadBudgetGroups();
			addDropdownData(
					BUDGET_GROUP_LIST, ajaxFinancialDetailAction.getBudgetGroups());		
		}
		else if(!functionPopulated){
			try {
				addDropdownData(BUDGET_GROUP_LIST, 
						(List<BudgetGroup>) budgetGroupDAO.getBudgetGroupList());
			} catch (EGOVRuntimeException e) {
				addFieldError("budgetheadexception", "Unable to load budget head ");
			}
		}
	}
	
	protected void populateBudgetHeadList(
			AjaxFinancialDetailAction ajaxFinancialDetailAction, 
			boolean functionPopulated, boolean datePresent) throws Exception {
		if (functionPopulated && datePresent) {
			ajaxFinancialDetailAction.setFunctionId(
					financialDetail.getFunction().getId());
			ajaxFinancialDetailAction.setEstimateDate(getReportDate());
			ajaxFinancialDetailAction.loadBudgetGroups();
			addDropdownData(BUDGET_GROUP_SEARCH_LIST, ajaxFinancialDetailAction.getBudgetGroups());		
		}
		else
			addDropdownData(BUDGET_GROUP_SEARCH_LIST,new ArrayList<BudgetGroup>());
	}
	
	protected void populateFinancingSourceDetails(){
		for(FinancingSource finSource: financingSourceList){
			if(validFinancingSource(finSource)){
			finSource.setFundSource((Fundsource) getPersistenceService().find(
					"from Fundsource where id = ? ",finSource.getFundSource().getId()));
			financialDetail.addFinancingSource(finSource);
			}
		}
	}
	
	protected boolean validFinancingSource(FinancingSource finSource){
		if(finSource!=null && finSource.getFundSource()!=null && 
				finSource.getFundSource().getId()!=null){
			return true;
		}
		
		return false;
	}
	
	
	public Object getModel() {
		return financialDetail;
	}
	
	protected void setModel(FinancialDetail financialDetail) {
		this.financialDetail = financialDetail;
	}
	
	public AbstractEstimate getAbstractEstimate() {
		return abstractEstimate;
	}

	public void setAbstractEstimate(AbstractEstimate abstractEstimate) {
		this.abstractEstimate = abstractEstimate;
	}


	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	public void setAbstractEstimateService(
			AbstractEstimateService abstractEstimateService) {
		this.abstractEstimateService = abstractEstimateService;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public List getEstimateStatuses() {
		return persistenceService.findAllBy("from EgwStatus s where moduletype=? " +
				"order by orderId",AbstractEstimate.class.getSimpleName());
	}

	public void setBudgetGroupDAO(BudgetGroupDAO budgetGroupDAO) {
		this.budgetGroupDAO = budgetGroupDAO;
	}
	
	public BudgetGroupDAO getBudgetGroupDAO() {
		return budgetGroupDAO;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<FinancingSource> getFinancingSourceList() {
		return financingSourceList;
	}

	public void setFinancingSourceList(List<FinancingSource> financingSourceList) {
		this.financingSourceList = financingSourceList;
	}

	public List<Fundsource> getFundSourceList() {
		return fundSourceList;
	}

	public void setFundSourceList(List<Fundsource> fundSourceList) {
		this.fundSourceList = fundSourceList;
	}

	public void setEstimateWorkflowService(
			WorkflowService<AbstractEstimate> workflow) {
		this.estimateWorkflowService = workflow;
	}

	/**
	 * @return the employeeName
	 */
	public String getEmployeeName() {
		return employeeName;
	}

	/**
	 * @param employeeName the employeeName to set
	 */
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	/**
	 * @return the designation
	 */
	public String getDesignation() {
		return designation;
	}

	/**
	 * @param designation the designation to set
	 */
	public void setDesignation(String designation) {
		this.designation = designation;
	}


	/**
	 * @param worksService the worksService to set
	 */
	public void setWorksService(WorksService worksService) { 
		this.worksService = worksService;
	}
	
	public Date getFinancialYearStartDate() {
		financialYearStartDate = commonsService.getFinancialYearByFinYearRange(worksService.getWorksConfigValue("FINANCIAL_YEAR_RANGE")).getStartingDate();
		return financialYearStartDate;
	}

	public void setFinancialYearStartDate(Date financialYearStartDate) {
		this.financialYearStartDate = financialYearStartDate;
	}

	public Integer getApproverUserId() {
		return approverUserId;
	}

	public void setApproverUserId(Integer approverUserId) {
		this.approverUserId = approverUserId;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getDesignationId() {
		return designationId;
	}

	public void setDesignationId(Integer designationId) {
		this.designationId = designationId;
	}

	public String getApproverComments() {
		return approverComments;
	}

	public void setApproverComments(String approverComments) {
		this.approverComments = approverComments;
	}
	
	
	@ValidationErrorPage(value="searchBudgetFolio")
	public String searchBudgetFolio()
	{	
		if(!StringUtils.isBlank(option) && "searchdetails".equalsIgnoreCase(option)){	
		 search("menu");
		}
		return SEARCH_BUDGET_FOLIO;
	}
	
	// Added for Deposit Works Folio Report
	public String searchDepositWorksFolio(){
		if(!StringUtils.isBlank(option) && "searchDepositWorksFolioDetails".equalsIgnoreCase(option)){	
			 viewDepositWorksFolioReport();
		}
		return SEARCH_DEPOSIT_WORKS_FOLIO;
	}
	
	public String viewDepositWorksFolioReport() throws ValidationException{
		Fund fund =null;
		CChartOfAccounts coa=null; 
		try{
				AbstractEstimate abstractEstimate=new AbstractEstimate();  
				Accountdetailtype accountdetailtype = (Accountdetailtype) persistenceService
				.find("from Accountdetailtype where name=?", "DEPOSITCODE");
				fund = (Fund) persistenceService.find("from Fund where id=?",financialDetail.getFund().getId());
				coa = (CChartOfAccounts) persistenceService.find("from CChartOfAccounts where id=?",financialDetail.getCoa().getId());
				Map<String,Object> reportParams = getDepositFolioDetails(abstractEstimate, fund, coa,	accountdetailtype, depositCodeId,asOnDate);
				totalDepositAmount=(BigDecimal) reportParams.get("totalDeposit");
				
				if(latestCumulative!=0.00D || totalDepositAmount!=BigDecimal.ZERO){ 
					BudgetFolioDetail e=new BudgetFolioDetail(); 
					e.setSrlNo(Integer.getInteger(""));
					e.setBudgetApprNo("<b>Latest Status :</b>");
					e.setEstimateNo("");
					e.setNameOfWork("");
					e.setEstimateDate("");
					e.setCumulativeTotal(latestCumulative);
					e.setBalanceAvailable(totalDepositAmount);
					List<BudgetFolioDetail> tempList = new ArrayList<BudgetFolioDetail>();
					tempList.add(e);
					if(approvedBudgetFolioDetails==null || approvedBudgetFolioDetails.isEmpty())
						approvedBudgetFolioDetails=tempList;
					else
						approvedBudgetFolioDetails.add(e); 
				}
		}catch (ValidationException e) { 
			addFieldError("glCodeValidate",e.getErrors().get(0).getMessage()); 
		}
		return 	RESULTS; 
	}
	
	public void search(String src)
	{
		if(APP.equalsIgnoreCase(src) && abstractEstimate!=null && abstractEstimate.getFinancialDetails().get(0)!=null){
			financialDetail=abstractEstimate.getFinancialDetails().get(0);
		}
		
		if(financialDetail!=null && financialDetail.getFund()!=null && financialDetail.getFund().getId()!=null && 
				financialDetail.getFund().getId()!=-1)
			queryParamMap.put("fundid", financialDetail.getFund().getId());
		
		if(financialDetail!=null && financialDetail.getFunction()!=null && financialDetail.getFunction().getId()!=null && 
				financialDetail.getFunction().getId()!=-1)
			queryParamMap.put("functionid", financialDetail.getFunction().getId());
		if(financialDetail!=null && financialDetail.getBudgetGroup()!=null && financialDetail.getBudgetGroup().getId()!=null && 
				financialDetail.getBudgetGroup().getId()!=-1) {
			List<BudgetGroup> budgetheadid=new ArrayList<BudgetGroup>();
			budgetheadid.add(financialDetail.getBudgetGroup());
			queryParamMap.put("budgetheadid", budgetheadid);
		}
		
		if(APP.equalsIgnoreCase(src) && financialDetail!=null && financialDetail.getAbstractEstimate().getExecutingDepartment()!=null){
			queryParamMap.put("deptid", financialDetail.getAbstractEstimate().getExecutingDepartment().getId());
		}else if(getExecutingDepartment()!=null && getExecutingDepartment()!=-1){
			queryParamMap.put("deptid", getExecutingDepartment());
		}
		
		if(APP.equalsIgnoreCase(src) && abstractEstimate!=null && abstractEstimate.getLeastFinancialYearForEstimate()!=null
				&& abstractEstimate.getLeastFinancialYearForEstimate().getId()!=null){
				queryParamMap.put("financialyearid", financialDetail.getAbstractEstimate().getLeastFinancialYearForEstimate().getId());
				queryParamMap.put("fromDate", financialDetail.getAbstractEstimate().getLeastFinancialYearForEstimate().getStartingDate());
				queryParamMap.put("toDate", new Date());
		}else if(getReportDate()!=null){
			if(!DateUtils.compareDates(new Date(),getReportDate())) addFieldError("reportDate",getText("greaterthan.currentDate.reportDate"));		
			CFinancialYear finyear = abstractEstimateService.getCurrentFinancialYear(getReportDate());
			if(finyear!=null && finyear.getId()!=null)
				queryParamMap.put("financialyearid", finyear.getId());
			queryParamMap.put("toDate", getReportDate());
		}
		
		
		if(!queryParamMap.isEmpty() && getFieldErrors().isEmpty()){
			
			try{				
				totalGrant = budgetDetailsDAO.getBudgetedAmtForYear(queryParamMap);
			}
			catch(ValidationException valEx){
				logger.error(valEx);
			}
			String appValue = worksService.getWorksConfigValue(PERCENTAGE_GRANT);
			if(StringUtils.isNotBlank(appValue)){
				totalGrantPerc=totalGrant.multiply(new BigDecimal(appValue));
				queryParamMap.put("totalGrantPerc", totalGrantPerc);	
			}
			Map<String,List> approvedBudgetFolioDetailsMap=abstractEstimateService.getApprovedAppropriationDetailsForBugetHead(queryParamMap);
			approvedBudgetFolioDetails=new ArrayList<BudgetFolioDetail>();
			if(approvedBudgetFolioDetailsMap!=null && !approvedBudgetFolioDetailsMap.isEmpty()){
				approvedBudgetFolioDetails = (List<BudgetFolioDetail>) approvedBudgetFolioDetailsMap.get("budgetFolioList");
				setReportLatestValues(approvedBudgetFolioDetailsMap);
			}
		}
	}
	
	/**
	 * 	This method display report screen for budgetFolio
	 */
	//@SkipValidation
	public String viewBudgetFolio(){	
		search(APP);
		getTotalGrantAppValue();
		return VIEW_BUDGET_FOLIO;  
	}
	
	public void getTotalGrantAppValue() throws NumberFormatException{
		if(StringUtils.isNotBlank(worksService.getWorksConfigValue(PERCENTAGE_GRANT)))
			 appValue=worksService.getWorksConfigValue(PERCENTAGE_GRANT);
		if(StringUtils.isNotBlank(appValue)){
			Double appValueDbl=Double.parseDouble(appValue);
			appValueDbl=appValueDbl.doubleValue() *100;
			appValueLabel=appValueDbl.toString();
			setAppValueLabel(appValueLabel);
		}
	}
	
	/**
	 * print pdf
	 * @throws JRException,Exception 
	 */
	//@SkipValidation
	public String viewBudgetFolioPdf() throws JRException,Exception{
		Map reportParams =new HashMap();
		if(!StringUtils.isBlank(option) && "searchPdf".equalsIgnoreCase(option)){
			search("menu");
			if(getExecutingDepartment()!=null && getExecutingDepartment()!=-1)
				reportParams.put("departmentName", deptName);
			
			if(financialDetail!=null && financialDetail.getFunction()!=null && financialDetail.getFunction()!=null && financialDetail.getFunction().getName()!=null){
				reportParams.put("functionCenter", financialDetail.getFunction().getName());
			}
			if(financialDetail!=null && financialDetail.getBudgetGroup()!=null && financialDetail.getBudgetGroup().getId()!=null && 
					financialDetail.getBudgetGroup().getId()!=-1){
				reportParams.put("budgetHead", financialDetail.getBudgetGroup().getName());
			}
			
			if(financialDetail!=null && financialDetail.getFund()!=null && financialDetail.getFund().getId()!=null && 
					financialDetail.getFund().getId()!=-1)
				reportParams.put(Fund, financialDetail.getFund().getName());
			
			reportParams.put("totalGrant", totalGrant);
			reportParams.put("totalGrantPer", totalGrantPerc);
			//reportParams.put("appValue", totalGrantPerc);
			getTotalGrantAppValue();
			reportParams.put("appValueLabel", appValueLabel);
		}
		else {
			search(APP);
			reportParams = abstractEstimateService.createBudgetFolioHeaderJasperObject(abstractEstimate,totalGrant,totalGrantPerc);
			getTotalGrantAppValue();
			reportParams.put("appValueLabel", appValueLabel);
		}
		
		
		reportParams.put("latestCumulative", latestCumulative);
		reportParams.put("latestBalance", latestBalance);
		ReportRequest reportRequest = new ReportRequest("BudgetFolio",approvedBudgetFolioDetails,reportParams);
		ReportOutput reportOutput = reportService.createReport(reportRequest);
		if(reportOutput!=null && reportOutput.getReportOutputData()!=null)
			budgetFolioPDF = new ByteArrayInputStream(reportOutput.getReportOutputData());
		return PRINT;
	}

	
	public String viewDepositFolio() throws ParseException {
		Map<String,Object> reportParams = null;
		Fund fund =null;
		CChartOfAccounts coa=null;
		CFunction function=null;
		Date appropriationDate = new Date();
		Accountdetailtype accountdetailtype = (Accountdetailtype) persistenceService
				.find("from Accountdetailtype where name=?", "DEPOSITCODE");
			if(abstractEstimate!=null && abstractEstimate.getFinancialDetails()!=null && !abstractEstimate.getFinancialDetails().isEmpty() && abstractEstimate.getFinancialDetails().get(0)!=null && parameters.get("fundId") == null){
				fund=abstractEstimate.getFinancialDetails().get(0).getFund();
				coa=abstractEstimate.getFinancialDetails().get(0).getCoa();
				if(abstractEstimate.getDepositCode()!=null)
					depositCodeId=abstractEstimate.getDepositCode().getId();
				function=abstractEstimate.getFinancialDetails().get(0).getFunction();
			}
			else{  
			Integer fundId = 0;
			Long glcodeId = null;
			Long functionId=null;
			if (parameters.get("fundId") != null)
				fundId = Integer.parseInt(parameters.get("fundId")[0]);
			if (parameters.get("glcodeId") != null)
				glcodeId = Long.valueOf(parameters.get("glcodeId")[0]);
			if (parameters.get("depositCodeId") != null)
				depositCodeId = Long.valueOf(parameters.get("depositCodeId")[0]);
			if (parameters.get("functionId") != null){
				functionId = Long.valueOf(parameters.get("functionId")[0]);
				function=(CFunction)persistenceService.find("from CFunction where id=?",functionId);
			}
			if(parameters.get("asOnDate") != null){
				SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy",new Locale("en","IN"));
				appropriationDate=sdf.parse(parameters.get("asOnDate")[0]);
			}
			fund = (Fund) persistenceService.find("from Fund where id=?",fundId);
			coa = (CChartOfAccounts) persistenceService.find("from CChartOfAccounts where id=?",glcodeId);
			}
			reportParams = getDepositFolioDetails(abstractEstimate, fund, coa,
					accountdetailtype, depositCodeId,appropriationDate); 
		if(function!=null){
			reportParams.put(FUNCTION, function.getName());
		}
		reportParams.put("ABSTRACT_ESTIMATE", abstractEstimate);
		ReportRequest reportRequest = new ReportRequest("DepositFolio",
				approvedBudgetFolioDetails, reportParams);
		ReportOutput reportOutput = reportService.createReport(reportRequest);
		if (reportOutput != null && reportOutput.getReportOutputData() != null)
			budgetFolioPDF = new ByteArrayInputStream(reportOutput
					.getReportOutputData()); 
		return PRINT;
	}

	public Map<String, Object> getDepositFolioDetails(
			AbstractEstimate abstractEstimate, Fund fund, CChartOfAccounts coa,
			Accountdetailtype accountdetailtype, Long depositCodeId,Date appropriationDate) {
		DepositCode depositCode = (DepositCode) persistenceService
		.find("from DepositCode where id=?", depositCodeId);
		HashMap<String, Object> reportParams = new HashMap<String, Object>();
		Map<String, List> approvedBudgetFolioDetailsMap = depositWorksUsageService
				.getDepositFolioDetails(abstractEstimate, fund, coa,
						accountdetailtype, depositCodeId,appropriationDate);
		
		BigDecimal getTotalDepositAmount = depositWorksUsageService
				.getTotalDepositWorksAmount(fund, coa, accountdetailtype,
						depositCodeId,appropriationDate); 
		
		if(approvedBudgetFolioDetailsMap.isEmpty()){
			latestCumulative = 0.0D;
		}
		else{
			approvedBudgetFolioDetails = new ArrayList<BudgetFolioDetail>();
			approvedBudgetFolioDetails=approvedBudgetFolioDetailsMap.get("depositFolioList");
			List calculatedValuesList = approvedBudgetFolioDetailsMap
			.get("calculatedValues");
			latestCumulative = (Double) calculatedValuesList.get(0);			
		}
		reportParams.put("fund", fund.getName());
		reportParams.put("depositCode", depositCode.getCode());
		reportParams.put("depositworksName", depositCode.getCodeName());
		reportParams.put("totalDeposit", getTotalDepositAmount);
		reportParams.put("latestCumulative", latestCumulative);
		return reportParams;
	}
	
	public void setReportLatestValues(Map<String,List> approvedBudgetFolioDetailsMap){		
		List calculatedValuesList =approvedBudgetFolioDetailsMap.get("calculatedValues");
		latestCumulative=(Double)calculatedValuesList.get(0);
		latestBalance=(BigDecimal)calculatedValuesList.get(1);
	}
	
	public BigDecimal getTotalGrant() {
		return totalGrant;
	}

	public BigDecimal getTotalGrantPerc() {
		return totalGrantPerc;
	}

	public List<BudgetFolioDetail> getApprovedBudgetFolioDetails() {
		return approvedBudgetFolioDetails;
	}

	public InputStream getBudgetFolioPDF() {
		return budgetFolioPDF;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public void setApprovedBudgetFolioDetails(List<BudgetFolioDetail> approvedBudgetFolioDetails) {
		this.approvedBudgetFolioDetails = approvedBudgetFolioDetails;
	}

	public Double getLatestCumulative() {
		return latestCumulative;
	}

	public BigDecimal getLatestBalance() {
		return latestBalance;
	}

	public Map<String, Object> getQueryParamMap() {
		return queryParamMap;
	}

	public void setQueryParamMap(Map<String, Object> queryParamMap) {
		this.queryParamMap = queryParamMap;
	}

	public Integer getExecutingDepartment() {
		return executingDepartment;
	}

	public void setExecutingDepartment(Integer executingDepartment) {
		this.executingDepartment = executingDepartment;
	}

	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) { 
		this.reportDate = reportDate;
	}

	public void setBudgetDetailsDAO(BudgetDetailsDAO budgetDetailsDAO) {
		this.budgetDetailsDAO = budgetDetailsDAO;
	}
	
	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}
	
	public Map<String, String> getMandatoryFields() {
		return mandatoryFields;
	}

	public void setMandatoryFields(Map<String, String> mandatoryFields) {
		this.mandatoryFields = mandatoryFields;
	}
	
	public void validate(){
		if(skipBudget && parameters.get("actionName")!=null) {
			if(financialDetail.getCoa()==null || (financialDetail.getCoa()!=null && (financialDetail.getCoa().getId()==null || financialDetail.getCoa().getId()==-1))) {
				addFieldError("depoist.accountCode",getText("estimate.deposit.accountCode.mandatory"));
			}
			if(depositCodeId==null || depositCodeId==-1) {
				addFieldError("depoistCode",getText("estimate.depositCode.mandatory"));
			}
		}
		
		if("searchdetails".equalsIgnoreCase(option)){
			if(!StringUtils.isBlank(mandatoryFields.get(Fund))  && (financialDetail.getFund()==null 
					|| financialDetail.getFund().getId()==null || financialDetail.getFund().getId()==-1)){
				addFieldError(Fund,getText("budgetfolio.fund.mandatory"));
			}	
			if(!StringUtils.isBlank(mandatoryFields.get("department")) && (executingDepartment==null 
					|| executingDepartment ==-1)){
				addFieldError("executingDepartment",getText("budgetfolio.department.mandatory"));
			}	
			if(financialDetail.getFunction()==null 
					|| financialDetail.getFunction().getId()==null || financialDetail.getFunction().getId()==-1){
				addFieldError(FUNCTION,getText("budgetfolio.function.mandatory"));
			}	
			if((financialDetail.getBudgetGroup()==null 
					|| financialDetail.getBudgetGroup().getId()==null || financialDetail.getBudgetGroup().getId()==-1)){
				addFieldError("budgetGroup",getText("budgetfolio.budgetGroup.mandatory"));
			}	
	    }
	}
	
	public Boolean isSkipBudgetCheck(){
		List<String> depositTypeList=getAppConfigValuesToSkipBudget();
		
		logger.info("lenght of appconfig values>>>>>> "+depositTypeList.size());
		if(abstractEstimate!=null && abstractEstimate.getId()!=null){
			for(String type:depositTypeList){
				if(type.equals(abstractEstimate.getType().getName())){
					skipBudget=true;
				}
			}
		}	
		return skipBudget;
	}
	

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
	public List<String> getAppConfigValuesToSkipBudget(){
		return worksService.getNatureOfWorkAppConfigValues(MODULE_NAME, KEY_NAME);
	}

	public String getAppValue() {
		return appValue;
	}

	public void setAppValue(String appValue) {
		this.appValue = appValue;
	}

	public AbstractEstimateService getAbstractEstimateService() {
		return abstractEstimateService;
	}

	public String getAppValueLabel() {
		return appValueLabel;
	}

	public void setAppValueLabel(String appValueLabel) {
		this.appValueLabel = appValueLabel;
	}
	public void setDepositWorksUsageService(
			DepositWorksUsageService depositWorksUsageService) {
		this.depositWorksUsageService = depositWorksUsageService;
	}

	public boolean isSkipBudget() {
		return skipBudget;
	}

	public void setSkipBudget(boolean skipBudget) {
		this.skipBudget = skipBudget;
	}

	public Long getDepositCodeId() {
		return depositCodeId;
	}

	public void setDepositCodeId(Long depositCodeId) {
		this.depositCodeId = depositCodeId;
	}

	public void setDepositCodeService(
			PersistenceService<DepositCode, Long> depositCodeService) {
		this.depositCodeService = depositCodeService;
	}

	public Date getAsOnDate() {
		return asOnDate;
	}

	public void setAsOnDate(Date asOnDate) {
		this.asOnDate = asOnDate;
	}


	public void setLatestCumulative(Double latestCumulative) {
		this.latestCumulative = latestCumulative;
	}

	public BigDecimal getTotalDepositAmount() {
		return totalDepositAmount;
	}

	public void setTotalDepositAmount(BigDecimal totalDepositAmount) {
		this.totalDepositAmount = totalDepositAmount;
	}

	public void setLatestBalance(BigDecimal latestBalance) {
		this.latestBalance = latestBalance;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
