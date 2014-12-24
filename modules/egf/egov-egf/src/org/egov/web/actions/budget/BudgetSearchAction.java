package org.egov.web.actions.budget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.infstr.workflow.SimpleWorkflowService;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.lib.rjbac.user.UserImpl;
import org.egov.model.budget.Budget;
import org.egov.model.budget.BudgetDetail;
import org.egov.model.budget.BudgetGroup;
import org.egov.pims.commons.Position;
import org.egov.pims.commons.service.EisCommonsService;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;
import org.egov.services.budget.BudgetDetailService;
import org.egov.services.budget.BudgetService;
import org.egov.utils.BudgetDetailConfig;
import org.egov.utils.BudgetDetailHelper;
import org.egov.utils.Constants;
import org.egov.web.actions.BaseFormAction;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.ValueStack;

@ParentPackage("egov")
public class BudgetSearchAction extends BaseFormAction{
	private static final long serialVersionUID = 1L;
	protected List<String> headerFields = new ArrayList<String>();
	protected List<String> gridFields = new ArrayList<String>();
	private BudgetDetail budgetDetail = new BudgetDetail();
	private List<Budget> budgetList=new ArrayList<Budget>();
	protected List<BudgetDetail> savedbudgetDetailList=new ArrayList<BudgetDetail>();
	protected BudgetDetailConfig budgetDetailConfig;
	protected BudgetDetailService budgetDetailService;
	protected BudgetService budgetService;
	protected List<BudgetAmountView> budgetAmountView = new ArrayList<BudgetAmountView>();
	protected SimpleWorkflowService<BudgetDetail> budgetDetailWorkflowService;
	protected Long financialYear;
	protected List<Budget> budgets;
	protected boolean isApproveAction=false;
	protected String mode;
	protected boolean showButton=true;
	protected EisCommonsService eisCommonsService;
	protected boolean disableBudget = false;
	private EmployeeService employeeService;
	BudgetDetailHelper budgetDetailHelper;
	boolean errorMessage = true;
	boolean re;
	Budget topBudget=null;
	String message = "";
	protected List<String> mandatoryFields = new ArrayList<String>();
	private Map<String,String> budgetDetailIdsAndAmount = new HashMap<String, String>();
	private Map<String,String> previousYearBudgetDetailIdsAndAmount = new HashMap<String, String>();
	private FinancialYearHibernateDAO financialYearDAO;
	protected String currentfinYearRange = "";
	private String previousfinYearRange = "";
	
	public String getMessage(){
		return message;
	}
	
	public boolean isRe() {
		return re;
	}
	
	public boolean isErrorMessage() {
		return errorMessage;
	}

	public void setBudgetDetailHelper(BudgetDetailHelper budgetHelper) {
		this.budgetDetailHelper = budgetHelper;
	}

	public BudgetDetailService getBudgetDetailService() {
		return budgetDetailService;
	}

	public boolean isDisableBudget() {
		return disableBudget;
	}
	public void setDisableBudget(boolean disableBudget) {
		this.disableBudget = disableBudget;
	}
	public List<Budget> getBudgets() {
		return budgets;
	}
	public Long getFinancialYear() {
		return financialYear == null? budgetDetailHelper.getFinancialYear(): financialYear;
	}
	
	public void setFinancialYear(Long financialYearRange) {
		this.financialYear = financialYearRange;
	}
	
	public List<BudgetAmountView> getBudgetAmountView() {
		return budgetAmountView;
	}
	
	protected String getMessage(String key) {
		return getText(key);
	}
	
	public List<BudgetDetail> getSavedbudgetDetailList() {
		return savedbudgetDetailList;
	}
	
	public void setBudgetDetailService(BudgetDetailService budgetDetailService) {
		this.budgetDetailService = budgetDetailService;
	}
	
	public void setBudgetService(BudgetService budgetService) {
		this.budgetService = budgetService;
	}
	
	public List<Budget> getBudgetList() {
		return budgetList;
	}
	
	@Override
	public String execute() throws Exception {
		if(parameters.containsKey(Constants.MODE))
			setMode(parameters.get(Constants.MODE)[0]);
		errorMessage = false;
		return Constants.LIST;
	}

	public boolean showbuttons(){
		return showButton;
	}
	
	public BudgetSearchAction(BudgetDetailConfig budgetDetailConfig){
		this.budgetDetailConfig = budgetDetailConfig;
		headerFields = budgetDetailConfig.getHeaderFields();
		gridFields = budgetDetailConfig.getGridFields();
		mandatoryFields = budgetDetailConfig.getMandatoryFields();
		addRelatedEntity("budget", Budget.class);
		addRelatedEntity("budgetGroup", BudgetGroup.class);
		if(shouldShowField(Constants.FUNCTIONARY))
			addRelatedEntity(Constants.FUNCTIONARY, Functionary.class);
		if(shouldShowField(Constants.FUNCTION))
			addRelatedEntity(Constants.FUNCTION, CFunction.class);
		if(shouldShowField(Constants.FUND))
			addRelatedEntity(Constants.FUND, Fund.class);
		if(shouldShowField(Constants.SCHEME))
			addRelatedEntity(Constants.SCHEME, Scheme.class);
		if(shouldShowField(Constants.SUB_SCHEME))
			addRelatedEntity(Constants.SUB_SCHEME, SubScheme.class);
		if(shouldShowField(Constants.EXECUTING_DEPARTMENT))
			addRelatedEntity(Constants.EXECUTING_DEPARTMENT, DepartmentImpl.class);
		if(shouldShowField(Constants.BOUNDARY))
			addRelatedEntity(Constants.BOUNDARY, BoundaryImpl.class);
	}
	@Override
	public void prepare() {
		super.prepare();
		if(!parameters.containsKey("skipPrepare")){
			EgovMasterDataCaching masterCache = EgovMasterDataCaching.getInstance();
			headerFields = budgetDetailConfig.getHeaderFields();
			gridFields = budgetDetailConfig.getGridFields();
	//		setupDropdownDataExcluding(Constants.SUB_SCHEME);
			dropdownData.put("budgetGroupList", masterCache.get("egf-budgetGroup"));
			dropdownData.put("budgetList", budgetDetailService.findApprovedBudgetsForFY(getFinancialYear()));
			dropdownData.put("financialYearList", persistenceService.findAllBy("from CFinancialYear where isActive=1 order by finYearRange desc"));
			if(shouldShowField(Constants.SUB_SCHEME))
				dropdownData.put("subSchemeList", Collections.EMPTY_LIST);
			if(shouldShowField(Constants.FUNCTIONARY))
				dropdownData.put("functionaryList", masterCache.get("egi-functionary"));
			if(shouldShowField(Constants.FUNCTION))
				dropdownData.put("functionList", masterCache.get("egi-function"));
			if(shouldShowField(Constants.SCHEME))
				dropdownData.put("schemeList", persistenceService.findAllBy("from Scheme where isActive=1 order by name"));
			if(shouldShowField(Constants.EXECUTING_DEPARTMENT))
				dropdownData.put("executingDepartmentList", masterCache.get("egi-department"));
			if(shouldShowField(Constants.BOUNDARY))
				dropdownData.put("boundaryList", persistenceService.findAllBy("from BoundaryImpl order by name"));
			if(shouldShowField(Constants.FUND))
				dropdownData.put("fundList", persistenceService.findAllBy("from Fund where isNotLeaf='0' and isActive='0' order by name"));
		}
	}
	
	@Override
	public Object getModel() {
		return budgetDetail;
	}
	
	//for modify screen
	public String list() {
		if(parameters.containsKey(Constants.MODE) && ("approve".equals(parameters.get(Constants.MODE)[0]))){
			setMode(parameters.get(Constants.MODE)[0]);
			isApproveAction=true;
			disableBudget = true;
		}
		if(budgetDetail.getBudget() != null){
			HibernateUtil.getCurrentSession().refresh(budgetDetail.getBudget());
			financialYear=budgetDetail.getBudget().getFinancialYear().getId();
			if(isApproveAction==true)
				budgetList.add(budgetService.find("select budget from Budget budget  join budget.state as state where budget.id=? and state.owner=? ", budgetDetail.getBudget().getId(),getPosition()));
			else
				budgetList.add(budgetService.find("select budget from Budget budget  join budget.state as state where budget.id=? and state.value=? ", budgetDetail.getBudget().getId(),"NEW"));
			
			
		}else{
			List<BudgetDetail> budgetDetails = budgetDetailService.searchByCriteriaAndFY(financialYear,budgetDetail,isApproveAction,getPosition());
			for (BudgetDetail budgetDetail : budgetDetails) {
				if(!budgetList.contains(budgetDetail.getBudget())) budgetList.add(budgetDetail.getBudget());
			}
		}
		//budgetList=removeReferenceBudgets(budgetList);
		getSession().put(Constants.SEARCH_CRITERIA_KEY, budgetDetail);
		getSession().put("financialyearid", financialYear);
		if(budgetList.isEmpty())
			message = getText("no.data.found");
		return Constants.LIST;  
	}
	
	//serach screen
	public String groupedBudgets() {
		Budget budget =budgetDetail.getBudget();
		//Dont restrict search by the selected budget, but by all budgets in the tree of selected budget
		budgetDetail.setBudget(null);
		if(budget==null)
			budgetList.addAll(budgetDetailService.findBudgetTree(budgetDetailService.findApprovedPrimaryParentBudgetForFY(financialYear),budgetDetail));
		else
			budgetList.addAll(budgetDetailService.findBudgetTree(budget,budgetDetail));
		getSession().put(Constants.SEARCH_CRITERIA_KEY, budgetDetail);
		return Constants.LIST;  
	}
	
	
	public void setBudgetDetailConfig(BudgetDetailConfig budgetDetailConfig) {
		this.budgetDetailConfig = budgetDetailConfig;
	}
	
	public void setBudgetDetail(BudgetDetail budgetDetail) {
		this.budgetDetail = budgetDetail;
	}
	
	public final boolean shouldShowHeaderField(String field){
		return headerFields.isEmpty() || headerFields.contains(field);
	}
	
	public final boolean shouldShowGridField(String field){
		return gridFields.isEmpty() || gridFields.contains(field);
	}
	
	public boolean showApprovalDetails(){ 
		boolean result=false;
		String mode=getMode();
		if(mode!=null && mode.equals("approve") )
		{
			isApproveAction=true;
			result=isApproveAction;
		}
		return result;
	}
	
	//for modify screen
	public String budgetDetailList(){
		if(parameters.get("budget.id")!=null)
		{		
			Budget	Budget=budgetService.findById(Long.valueOf(parameters.get("budget.id")[0]), false);
			setTopBudget(Budget);
		}
		BudgetDetail criteria = (BudgetDetail) getSession().get(Constants.SEARCH_CRITERIA_KEY);
		criteria.setBudget(budgetDetail.getBudget());
		savedbudgetDetailList = budgetDetailService.searchBy(criteria);
		re = checkRe(budgetDetail.getBudget());
		computeAmounts(savedbudgetDetailList);
		return Constants.DETAILLIST;
	}
	
	
	
	
	protected boolean checkRe(Budget budget){
		if(budget!=null){
			if("RE".equalsIgnoreCase(budget.getIsbere()))
				return true;
		}
		return false;
	}
	 
	//for search screen
	public String groupedBudgetDetailList(){
		BudgetDetail criteria = (BudgetDetail) getSession().get(Constants.SEARCH_CRITERIA_KEY);
		Budget budget = budgetDetail.getBudget();
		if(budget!=null && budget.getId()!=null){
			budget = (Budget) persistenceService.find("from Budget where id=?", budget.getId());
			currentfinYearRange = budget.getFinancialYear().getFinYearRange();
			computePreviousYearRange();
		}
		criteria.setBudget(null);
		savedbudgetDetailList = budgetDetailService.findAllBudgetDetailsWithReAppropriation(budget, criteria);
		re = checkRe(budget);
		computeAmounts(savedbudgetDetailList);
		populateActualData(budget.getFinancialYear());
		return Constants.DETAILLIST;
	}
	
	protected void computePreviousYearRange() {
		if(StringUtils.isNotBlank(currentfinYearRange)){
			String[] list = currentfinYearRange.split("-");
			previousfinYearRange = subtract(list[0]) +"-"+subtract(list[1]);
		}
	}
	
	protected String subtract(String value) {
		int val = Integer.parseInt(value) - 1;
		if(val<10)
			return "0"+ val;
		return String.valueOf(val);
	}
	
	protected ValueStack getValueStack() {
		return ActionContext.getContext().getValueStack();
	}
	
	public void computeAmounts(List<BudgetDetail> budgetDetails){
		budgetAmountView = new ArrayList<BudgetAmountView>();
		for (BudgetDetail detail : budgetDetails) {
			BudgetAmountView view = new BudgetAmountView();
			budgetAmountView.add(view);
			if(detail.getState()!=null)
				detail.setComment(detail.getState().getText1());
			BigDecimal approvedAmt = detail.getApprovedAmount()==null?BigDecimal.ZERO:detail.getApprovedAmount().setScale(2);
			if(re) {
				view.setCurrentYearReApproved(approvedAmt.setScale(2).toString());
			} else{
				view.setCurrentYearBeApproved(approvedAmt.setScale(2).toString());
			}
			detail.setAnticipatoryAmount(detail.getAnticipatoryAmount()==null?BigDecimal.ZERO:detail.getAnticipatoryAmount().setScale(2));
			detail.getOriginalAmount().setScale(2);
		}
	}
	
	public BigDecimal calculateTotal(BudgetDetail detail){
		BigDecimal approvedAmount = detail.getApprovedAmount()==null?BigDecimal.ZERO:detail.getApprovedAmount();
		BigDecimal approvedReAppropriationsTotal = detail.getApprovedReAppropriationsTotal()==null?BigDecimal.ZERO:detail.getApprovedReAppropriationsTotal();
		return approvedAmount.add(approvedReAppropriationsTotal);
	}
	
	private void populateActualData(CFinancialYear financialYear){
		String fromDate = Constants.DDMMYYYYFORMAT1.format(financialYear.getStartingDate());
		List<Object[]> result = budgetDetailService.fetchActualsForFY(fromDate,"decode(b.as_on_date,null,sysdate,b.as_on_date)",mandatoryFields);
		for (Object[] row : result) {
			budgetDetailIdsAndAmount.put(row[0].toString(), row[1].toString());
		}
		fromDate = Constants.DDMMYYYYFORMAT1.format(subtractYear(financialYear.getStartingDate()));
		List<Object[]> previousYearResult = budgetDetailService.fetchActualsForFY(fromDate,"decode(b.as_on_date,null,sysdate+numtoyminterval(-1,'year'),b.as_on_date+numtoyminterval(-1,'year'))",mandatoryFields);
		for (Object[] row : previousYearResult) {
			previousYearBudgetDetailIdsAndAmount.put(row[0].toString(), row[1].toString());
		}
	}

	public Date subtractYear(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, -1);
		return cal.getTime();
	}

	
	public String ajaxLoadBudget(){
		budgets = budgetDetailService.findApprovedBudgetsForFY(getFinancialYear());
		return Constants.BUDGETS;
	}
	public Position getPosition()throws EGOVRuntimeException
	{
		Position pos;
		try {
			PersonalInformation emp=employeeService.getEmpForUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
			pos=employeeService.getPositionforEmp(emp.getIdPersonalInformation());
			} catch (Exception e) {
			throw new EGOVRuntimeException("Unable to get Position for the user");
		}
		return pos;
	}
	protected UserImpl getUser() {
		UserImpl user = (UserImpl) persistenceService.find("from UserImpl where id_user=?",Integer.parseInt(EGOVThreadLocals.getUserId()));
		return user;
	}
	
	public void setBudgetDetailWorkflowService(SimpleWorkflowService<BudgetDetail> workflowService) {
		this.budgetDetailWorkflowService = workflowService;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	/**
	 * @return the topBudget
	 */
	public Budget getTopBudget()
	{
		return topBudget;
	}
	/**
	 * @param topBudget the topBudget to set
	 */
	public void setTopBudget(Budget topBudget)
	{
		this.topBudget = topBudget;
	}
	
	public final boolean shouldShowField(String field){
		if(headerFields.isEmpty() && gridFields.isEmpty())
			return true;
		return shouldShowHeaderField(field) || shouldShowGridField(field);
	}

	public void setBudgetDetailIdsAndAmount(Map<String,String> budgetDetailIdsAndAmount) {
		this.budgetDetailIdsAndAmount = budgetDetailIdsAndAmount;
	}

	public Map<String,String> getBudgetDetailIdsAndAmount() {
		return budgetDetailIdsAndAmount;
	}

	public void setPreviousYearBudgetDetailIdsAndAmount(
			Map<String,String> previousYearBudgetDetailIdsAndAmount) {
		this.previousYearBudgetDetailIdsAndAmount = previousYearBudgetDetailIdsAndAmount;
	}

	public Map<String,String> getPreviousYearBudgetDetailIdsAndAmount() {
		return previousYearBudgetDetailIdsAndAmount;
	}

	public void setFinancialYearDAO(FinancialYearHibernateDAO financialYearDAO) {
		this.financialYearDAO = financialYearDAO;
	}

	public FinancialYearHibernateDAO getFinancialYearDAO() {
		return financialYearDAO;
	}

	public void setCurrentfinYearRange(String currentfinYearRange) {
		this.currentfinYearRange = currentfinYearRange;
	}

	public String getCurrentfinYearRange() {
		return currentfinYearRange;
	}

	public void setPreviousfinYearRange(String previousfinYearRange) {
		this.previousfinYearRange = previousfinYearRange;
	}

	public String getPreviousfinYearRange() {
		return previousfinYearRange;
	}

	public void setEisCommonsService(EisCommonsService eisCommonsService) {
		this.eisCommonsService = eisCommonsService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
}
