package org.egov.web.actions.budget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.workflow.WorkflowService;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.lib.rjbac.user.UserImpl;
import org.egov.model.budget.Budget;
import org.egov.model.budget.BudgetDetail;
import org.egov.model.budget.BudgetGroup;
import org.egov.pims.commons.Position;
import org.egov.pims.commons.service.EisCommonsService;
import org.egov.services.budget.BudgetDetailService;
import org.egov.services.budget.BudgetService;
import org.egov.utils.BudgetDetailConfig;
import org.egov.utils.BudgetDetailHelper;
import org.egov.utils.Constants;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPage;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.ValueStack;

public abstract class BaseBudgetDetailAction extends BaseFormAction{
	private static final long serialVersionUID = 1L;
	protected BudgetDetail budgetDetail = new BudgetDetail();
	protected List<BudgetDetail> budgetDetailList = new ArrayList<BudgetDetail>();
	protected List<BudgetDetail> savedbudgetDetailList = new ArrayList<BudgetDetail>();
	protected BudgetDetailService budgetDetailService;
	BudgetService budgetService;
	private PersistenceService<CFinancialYear, Long> finYearService;
	protected List<String> headerFields = new ArrayList<String>();
	protected List<String> gridFields = new ArrayList<String>();
	protected List<String> mandatoryFields = new ArrayList<String>();
	protected WorkflowService<Budget> budgetWorkflowService;
	protected WorkflowService<BudgetDetail> budgetDetailWorkflowService;
	protected boolean headerDisabled = false;
	protected List<BudgetAmountView> budgetAmountView = new ArrayList<BudgetAmountView>();
	protected BudgetDetailConfig budgetDetailConfig;
	protected String currentYearRange;
	protected String previousYearRange;
	private String nextYearRange;
	protected String lastButOneYearRange;
	protected List<Scheme> subSchemes;
	protected Integer schemeId;
	protected Date asOnDate;
	private EisCommonsService eisCommonsService;
	BudgetDetailHelper budgetDetailHelper;
	final static Integer INT_ZERO = 0;
	final static Long LONG_ZERO =  Long.valueOf(0);
	FinancialYearDAO financialYearDAO;
	protected boolean re = false;
	private boolean showMessage = false;
	List<BigDecimal> beAmounts = new ArrayList<BigDecimal>();
	private Budget referenceBudget;
	private CFinancialYear financialYear;
	protected List<Budget> budgetList = new ArrayList<Budget>();
	protected boolean showRe;
	Long budgetDocumentNumber;
	
	public void setBudgetDocumentNumber(Long documentNumber) {
		this.budgetDocumentNumber = documentNumber;
	}

	public Long getBudgetDocumentNumber() {
		return budgetDocumentNumber;
	}

	public void setFinancialYearDAO(FinancialYearDAO financialYearDAO) {
		this.financialYearDAO = financialYearDAO;
	}
	
	public boolean isRe() {
		return re;
	}

	public void setBudgetDetailHelper(BudgetDetailHelper budgetHelper) {
		this.budgetDetailHelper = budgetHelper;
	}
	
	public void setFinYearService(PersistenceService<CFinancialYear, Long> finYearService) {
		this.finYearService = finYearService;
	}

	public Date getAsOnDate() {
		return asOnDate == null? new Date() : asOnDate;
	}
	
	public void setAsOnDate(Date asOnDate) {
		this.asOnDate = asOnDate;
	}
	
	public Integer getSchemeId() {
		return schemeId;
	}
	
	public void setSchemeId(Integer schemeId) {
		this.schemeId = schemeId;
	}
	
	public List<Scheme> getSubSchemes() {
		return subSchemes;
	}
	
	public BaseBudgetDetailAction(BudgetDetailConfig budgetDetailConfig){
		this.budgetDetailConfig = budgetDetailConfig;
		headerFields = budgetDetailConfig.getHeaderFields();
		gridFields = budgetDetailConfig.getGridFields();
		mandatoryFields = budgetDetailConfig.getMandatoryFields();
		addRelatedEntity("budget", Budget.class);
		addRelatedEntity(Constants.BUDGET_GROUP, BudgetGroup.class);
		if(shouldShowField(Constants.FUNCTIONARY))
			addRelatedEntity(Constants.FUNCTIONARY, Functionary.class);
		if(shouldShowField(Constants.FUNCTION))
			addRelatedEntity(Constants.FUNCTION, CFunction.class);
		if(shouldShowField(Constants.SCHEME))
			addRelatedEntity(Constants.SCHEME, Scheme.class);
		if(shouldShowField(Constants.SUB_SCHEME))
			addRelatedEntity(Constants.SUB_SCHEME, SubScheme.class);
		if(shouldShowField(Constants.FUND))
			addRelatedEntity(Constants.FUND, Fund.class);
		if(shouldShowField(Constants.EXECUTING_DEPARTMENT))
			addRelatedEntity(Constants.EXECUTING_DEPARTMENT, DepartmentImpl.class);
		if(shouldShowField(Constants.BOUNDARY))
			addRelatedEntity(Constants.BOUNDARY, BoundaryImpl.class);
	}
	
	public String execute() throws Exception {
		return NEW;
	}
	
	public String create() {
		validateMandatoryFields();
		budgetDetailHelper.removeEmptyBudgetDetails(budgetDetailList);
		saveBudgetDetails(false,financialYear);
		setAsOnDateOnSelectedBudget();
		showMessage = true;
		addActionMessage("Budget details saved successfully");
		return NEW;
	}
	
	@ValidationErrorPage(value="new-re")
	public String createRe() {
		showRe = true;
		try {
			getActionMessages().clear();
			validateMandatoryFields();
			budgetDetailHelper.removeEmptyBudgetDetails(budgetDetailList);
			validateAmounts(budgetDetailList);
			saveBudgetDetails(true,financialYear);
			setAsOnDateOnSelectedBudget();
			showMessage = true;
			addActionMessage("RE proposal for current year and BE proposal for next year saved successfully");
			dropdownData.put("budgetList", Collections.EMPTY_LIST);
			budgetDetail = new BudgetDetail();
			budgetDetail.setExecutingDepartment(null);
			financialYear = null;
		} catch (ValidationException e) {
			loadBudgets("RE");
			dropdownData.put("budgetList", budgetList);
			referenceBudget = budgetService.getReferenceBudgetFor(budgetDetail.getBudget());
			throw e;
		}
		return "new-re";
	}

	@ValidationErrorPage(value="new-re")
	public String createReAndForward() {
		showRe = true;
		try {
			getActionMessages().clear();
			budgetDetailHelper.removeEmptyBudgetDetails(budgetDetailList);
			validateIsPrimary();
			if(!rowsEmpty()){
				validateMandatoryFields();
				validateAmounts(budgetDetailList);
				saveBudgetDetails(true,financialYear);
			}
			setAsOnDateOnSelectedBudget();
			approve();
			showMessage = true;
			addActionMessage("RE proposal for current year and BE proposal for next year saved successfully");
			dropdownData.put("budgetList", Collections.EMPTY_LIST);
			budgetDetail = new BudgetDetail();
			budgetDetail.setExecutingDepartment(null);
			financialYear = null;
		} catch (ValidationException e) {
			loadBudgets("RE");
			dropdownData.put("budgetList", budgetList);
			referenceBudget = budgetService.getReferenceBudgetFor(budgetDetail.getBudget());
			throw e;
		}
		return "new-re";
	}
	
	private void validateIsPrimary() {
		if(budgetDetail.getBudget()!=null && !budgetDetail.getBudget().getIsPrimaryBudget())
			throw new ValidationException(Arrays.asList(new ValidationError("budget.not.primary","budget.not.primary")));
		Budget referenceBudgetFor = budgetService.getReferenceBudgetFor(budgetDetail.getBudget());
		if(budgetDetail.getBudget()!=null && referenceBudgetFor !=null && !referenceBudgetFor.getIsPrimaryBudget())
			throw new ValidationException(Arrays.asList(new ValidationError("budget.not.primary","budget.not.primary")));
	}

	private boolean rowsEmpty() {
		if(budgetDetailList == null) return true;
		for (BudgetDetail row : budgetDetailList) {
			if(gridFields.contains(Constants.EXECUTING_DEPARTMENT) && row.getExecutingDepartment().getId()!=-1 && row.getExecutingDepartment().getId()!=0)
				return false;
			if(gridFields.contains(Constants.FUNCTION) && row.getFunction().getId()!=-1 && row.getFunction().getId()!=0)
				return false;
			if(gridFields.contains(Constants.FUND) && row.getFund().getId()!=-1 && row.getFund().getId()!=0)
				return false;
			if(gridFields.contains(Constants.SCHEME) && row.getScheme().getId()!=-1 && row.getScheme().getId()!=0)
				return false;
			if(gridFields.contains(Constants.SUB_SCHEME) && row.getSubScheme().getId()!=-1 && row.getSubScheme().getId()!=0)
				return false;
			if(gridFields.contains(Constants.BOUNDARY) && row.getBoundary().getId()!=-1 && row.getBoundary().getId()!=0)
				return false;
			if(row.getBudgetGroup().getId()!=-1 && row.getBudgetGroup().getId()!=0)
				return false;
		}
		return true;
	}

	private void validateAmounts(List<BudgetDetail> detailList) {
		for (int i = 0; i < detailList.size(); i++) {
			if(beAmounts.get(i) == null || BigDecimal.ZERO.equals(beAmounts.get(i))){
				throw new ValidationException(Arrays.asList(new ValidationError("budgetDetail.re.amount","budgetDetail.re.amount")));
			}
			if(detailList.get(i).getOriginalAmount() == null || BigDecimal.ZERO.equals(detailList.get(i).getOriginalAmount())){
				throw new ValidationException(Arrays.asList(new ValidationError("budgetDetail.be.amount","budgetDetail.be.amount")));
			}
		}
	}

	protected void validateMandatoryFields() {
		checkHeaderMandatoryField(Constants.EXECUTING_DEPARTMENT,budgetDetail.getExecutingDepartment(),"budgetDetail.executingDepartment.mandatory");
		checkHeaderMandatoryField(Constants.FUNCTION,budgetDetail.getFunction(),"budgetDetail.function.mandatory");
		checkHeaderMandatoryField(Constants.FUNCTIONARY,budgetDetail.getFunctionary(),"budgetDetail.functionary.mandatory");
		checkHeaderMandatoryField(Constants.SCHEME,budgetDetail.getScheme(),"budgetDetail.scheme.mandatory");
		checkHeaderMandatoryField(Constants.SUB_SCHEME,budgetDetail.getSubScheme(),"budgetDetail.subScheme.mandatory");
		checkHeaderMandatoryField(Constants.FUND,budgetDetail.getFund(),"budgetDetail.fund.mandatory");
		checkHeaderMandatoryField(Constants.BOUNDARY,budgetDetail.getBoundary(),"budgetDetail.boundary.mandatory");
		for (BudgetDetail detail : budgetDetailList) {
			setRelatedValues(detail);
			checkGridMandatoryField(Constants.EXECUTING_DEPARTMENT,detail.getExecutingDepartment(),"budgetDetail.executingDepartment.mandatory");
			checkGridMandatoryField(Constants.FUNCTION,detail.getFunction(),"budgetDetail.function.mandatory");
			checkGridMandatoryField(Constants.FUNCTIONARY,detail.getFunctionary(),"budgetDetail.functionary.mandatory");
			checkGridMandatoryField(Constants.SCHEME,detail.getScheme(),"budgetDetail.scheme.mandatory");
			checkGridMandatoryField(Constants.SUB_SCHEME,detail.getSubScheme(),"budgetDetail.subScheme.mandatory");
			checkGridMandatoryField(Constants.FUND,detail.getFund(),"budgetDetail.fund.mandatory");
			checkGridMandatoryField(Constants.BOUNDARY,detail.getBoundary(),"budgetDetail.boundary.mandatory");
		}
	}

	private void setRelatedValues(BudgetDetail detail) {
		if(detail.getExecutingDepartment() != null && detail.getExecutingDepartment().getId()==0)
			detail.setExecutingDepartment(null);
		if(detail.getFunction() != null && detail.getFunction().getId()==0)
			detail.setFunction(null);
		if(detail.getScheme() != null && detail.getScheme().getId()==0)
			detail.setScheme(null);
		if(detail.getSubScheme() != null && detail.getSubScheme().getId()==0)
			detail.setSubScheme(null);
		if(detail.getFunctionary() !=null && detail.getFunctionary().getId()==0)
			detail.setFunctionary(null);
		if(detail.getBoundary() != null && detail.getBoundary().getId()==0)
			detail.setBoundary(null);
		if(detail.getFund() != null && detail.getFund().getId()==0)
			detail.setFund(null);
	}

	protected void checkHeaderMandatoryField(String fieldName,Object value,String errorKey) {
		if(headerFields.contains(fieldName) && mandatoryFields.contains(fieldName) && value == null)
			throw new ValidationException(Arrays.asList(new ValidationError(errorKey,errorKey)));
	}

	protected void checkGridMandatoryField(String fieldName,Object value,String errorKey) {
		if(gridFields.contains(fieldName) && mandatoryFields.contains(fieldName) && value == null)
			throw new ValidationException(Arrays.asList(new ValidationError(errorKey,errorKey)));
	}

	public boolean isFieldMandatory(String field){
		return mandatoryFields.contains(field);
	}

	private void saveBudgetDetails(boolean withRe, CFinancialYear finYear) {
		int index = 0;
		for (BudgetDetail detail : budgetDetailList) {
			if(detail != null){
				if(withRe)
					saveAndStartWorkFlowForRe(detail,index,finYear);
				else
					saveAndStartWorkFlow(detail);
			}
			index++;
		}
		budgetDetailList.clear();
	}
	
	private void setAsOnDateOnSelectedBudget() {
		if(budgetDetail.getBudget()!=null && budgetDetail.getBudget().getId()!=null){
			Budget selectedBudget = (Budget)getPersistenceService().find("from Budget where id=?", budgetDetail.getBudget().getId());
			selectedBudget.setAsOnDate(getAsOnDate());
			budgetService.persist(selectedBudget);
		}
	}
	
	void loadBudgets(String bere) {
		budgetList = new ArrayList<Budget>();
		budgetList.addAll(persistenceService.findAllBy("from Budget where id not in (select parent from Budget where parent is not null) " +
					"and isactivebudget = 1 and state.type='Budget' and isbere='"+bere.toUpperCase()+"' and state.value='NEW' and financialYear.id = " +
							getFinancialYear().getId()+" order by name"));
	}

	@Override
	public void prepare() {
		super.prepare();
		populateFinancialYear();
		headerFields = budgetDetailConfig.getHeaderFields();
		gridFields = budgetDetailConfig.getGridFields();
		mandatoryFields = budgetDetailConfig.getMandatoryFields();
		defaultToCurrentUsersExecutingDepartment();
		setupDropdownsInHeader();
		re = budgetService.hasReForYear(Long.valueOf(financialYearDAO.getCurrYearFiscalId()));
	}
	
	private void setupDropdownsInHeader() {
		EgovMasterDataCaching masterCache = EgovMasterDataCaching.getInstance();
		setupDropdownDataExcluding(Constants.SUB_SCHEME);
		setBudgetDropDown();
		dropdownData.put("budgetGroupList", masterCache.get("egf-budgetGroup"));
		if(shouldShowField(Constants.SUB_SCHEME))
			dropdownData.put("subSchemeList", Collections.EMPTY_LIST);
		if(shouldShowField(Constants.FUNCTIONARY))
			dropdownData.put("functionaryList", masterCache.get("egi-functionary"));
		if(shouldShowField(Constants.FUNCTION))
			dropdownData.put("functionList",  masterCache.get("egi-function"));
		if(shouldShowField(Constants.SCHEME))
			dropdownData.put("schemeList", persistenceService.findAllBy("from Scheme where isActive=1 order by name"));
		if(shouldShowField(Constants.EXECUTING_DEPARTMENT)) 
			dropdownData.put("executingDepartmentList", masterCache.get("egi-department"));
		if(shouldShowField(Constants.FUND))
			dropdownData.put("fundList", persistenceService.findAllBy("from Fund where isNotLeaf='0' and isactive='1' order by name"));
		if(shouldShowField(Constants.BOUNDARY))
			dropdownData.put("boundaryList", persistenceService.findAllBy("from BoundaryImpl order by name"));
		addDropdownData("financialYearList", getPersistenceService().findAllBy("from CFinancialYear where isActive=1 order by " +
				"finYearRange desc "));
		dropdownData.put("departmentList", masterCache.get("egi-department"));
		dropdownData.put("designationList", Collections.EMPTY_LIST);
		dropdownData.put("userList", Collections.EMPTY_LIST);
	}
	
	
	protected void populateFinancialYear() {
		Budget budget = budgetDetail.getBudget();
		if(budget != null){
			populateYearRange(budget); 
			return;
		}
		if(request.get("id") != null){
			Long id = (Long)request.get("id");
			if(!Long.valueOf(0).equals(id)){
				Budget b = budgetService.findById(id,false);
				populateYearRange(b);
				return;
			}
		}
		CFinancialYear finYear = finYearService.findById(budgetDetailHelper.getFinancialYear(),false);
		currentYearRange = finYear.getFinYearRange();
		computePreviousYearRange();
		computeLastButOneYearRange();
		computeNextYearRange();
	}

	private void populateYearRange(Budget budget) {
		if(budget!=null){
			if(budget.getFinancialYear()!=null)
				currentYearRange = budget.getFinancialYear().getFinYearRange();
			else
				currentYearRange = finYearService.findById(budgetDetailHelper.getFinancialYear(),false).getFinYearRange();
			computePreviousYearRange();
			computeLastButOneYearRange();
			computeNextYearRange();
		}
	}
	
	public String computeYearRange(String range) {
		if(StringUtils.isNotBlank(range)){
			String[] list = range.split("-");
			return subtract(list[0]) +"-"+subtract(list[1]);
		}
		return "";
	}
	public String addYearRange(String range) {
		if(StringUtils.isNotBlank(range)){
			String[] list = range.split("-");
			return add(list[0]) +"-"+add(list[1]);
		}
		return "";
	}

	private void computeLastButOneYearRange() {
		if(StringUtils.isNotBlank(previousYearRange)){
			String[] list = previousYearRange.split("-");
			lastButOneYearRange = subtract(list[0]) +"-"+subtract(list[1]);
		}
	}

	protected void computePreviousYearRange() {
		if(StringUtils.isNotBlank(currentYearRange)){
			String[] list = currentYearRange.split("-");
			previousYearRange = subtract(list[0]) +"-"+subtract(list[1]);
		}
	}
	protected void computeNextYearRange() {
		if(StringUtils.isNotBlank(currentYearRange)){
			String[] list = currentYearRange.split("-");
			nextYearRange = add(list[0]) +"-"+add(list[1]);
		}
	}
	
	protected String subtract(String value) {
		int val = Integer.parseInt(value) - 1;
		if(val<10)
			return "0"+ val;
		return String.valueOf(val);
	}
	protected String add(String value) {
		int val = Integer.parseInt(value) + 1;
		if(val<10)
			return "0"+ val;
		return String.valueOf(val);
	}

	public String getCurrentYearRange() {
		return currentYearRange;
	}
	
	public String getPreviousYearRange() {
		return previousYearRange;
	}
	
	public String getlastButOneYearRange() {
		return lastButOneYearRange;
	}
	
	protected void setBudgetDropDown() {
		dropdownData.put("budgetList", persistenceService.findAllBy("from Budget where id not in (select parent from Budget where parent is not null) and isactivebudget = 1 and state.type='Budget' and state.value='NEW' order by name"));
	}
	
	public List<BudgetDetail> getSavedbudgetDetailList() {
		return savedbudgetDetailList;
	}
	
	private void defaultToCurrentUsersExecutingDepartment() {
		if(shouldShowHeaderField("executingDepartment") ){
			UserImpl user = getUser();
			if(user.getDepartment() != null){
				budgetDetail.setExecutingDepartment(findDepartment(user.getDepartment().getId()));
			}
		}
	}
	
	private DepartmentImpl findDepartment(Integer id) {
		return (DepartmentImpl) persistenceService.find("from DepartmentImpl where ID_DEPT=?",id);
	}
	
	public List<BudgetDetail> getBudgetDetailList() {
		return budgetDetailList;
	}
	
	public List<BudgetAmountView> getBudgetAmountView() {
		return budgetAmountView;
	}
	
	protected abstract void saveAndStartWorkFlow(BudgetDetail detail) ;
	protected abstract void saveAndStartWorkFlowForRe(BudgetDetail detail,int index, CFinancialYear finYear) ;
	protected abstract void approve() ;
	protected UserImpl getUser() {
		return (UserImpl) persistenceService.find("from UserImpl where id_user=?",Integer.parseInt(EGOVThreadLocals.getUserId()));
	}
	
	protected Position getPosition() {
		return eisCommonsService.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
	}
	
	public List<String> getHeaderFields() {
		return headerFields;
	}
	
	public void setBudgetDetailWorkflowService(WorkflowService<BudgetDetail> budgetDetailWorkflowService) {
		this.budgetDetailWorkflowService = budgetDetailWorkflowService;
	}
	
	@Override
	public Object getModel() {
		return budgetDetail;
	}
	
	public BudgetDetail getBudgetDetail() {
		return budgetDetail;
	}
	
	public void setBudgetDetail(BudgetDetail budgetDetails) {
		this.budgetDetail = budgetDetails;
	}
	public void setBudgetDetailService(BudgetDetailService budgetDetailsService) {
		this.budgetDetailService = budgetDetailsService;
	}
	
	public String loadActuals(){
		validateAsOnDate();
		savedbudgetDetailList = budgetDetailService.findAllBy("from BudgetDetail where budget.id=?", budgetDetail.getBudget().getId());
		re = budgetService.hasReForYear(budgetDetail.getBudget().getFinancialYear().getId());
		budgetDetailHelper.removeEmptyBudgetDetails(budgetDetailList);
		budgetAmountView.addAll(populateAmountData(budgetDetailList, getAsOnDate(),budgetDetail.getBudget().getFinancialYear()));
		return NEW;
	}

	@ValidationErrorPage(value="new-re")
	public String loadActualsForRe(){
		showRe = true;
		try {
			loadActuals();
		} catch (ValidationException e) {
			populateBudgetList();
			throw e;
		}
		populateBudgetList();
		return "new-re";
	}

	private void populateBudgetList() {
		loadBudgets("RE");
		dropdownData.put("budgetList", budgetList);
		if(budgetDetail.getBudget()!=null && budgetDetail.getBudget().getId()!=null)
			referenceBudget = budgetService.getReferenceBudgetFor(budgetDetail.getBudget());
	}
	private void validateAsOnDate() {
		if(budgetDetail.getBudget()==null)
			throw new ValidationException(Arrays.asList(new ValidationError("budgetDetail.budget.mandatory","budgetDetail.budget.mandatory")));
		if(getAsOnDate().compareTo(budgetDetail.getBudget().getFinancialYear().getStartingDate())<0 || getAsOnDate().compareTo(budgetDetail.getBudget().getFinancialYear().getEndingDate())>0)
			throw new ValidationException(Arrays.asList(new ValidationError("budgetDetail.invalid.asondate","budgetDetail.invalid.asondate")));
	}
	
	public Date getPreviousYearFor(Date date) {
		GregorianCalendar previousYearToDate = new GregorianCalendar();
		previousYearToDate.setTime(date);
	    int prevYear = previousYearToDate.get(Calendar.YEAR) - 1;
	    previousYearToDate.set(Calendar.YEAR,prevYear);
		return previousYearToDate.getTime();
	}


	public List<String> getGridFields() {
		return gridFields;
	}
	
	public List<String> getMandatoryFields() {
		return mandatoryFields;
	}

	public void setBudgetDetailConfig(BudgetDetailConfig budgetDetailConfig) {
		this.budgetDetailConfig = budgetDetailConfig;
	}
	
	public boolean isHeaderDisabled() {
		return headerDisabled;
	}
	public final boolean shouldShowHeaderField(String field){
		return headerFields.isEmpty() || headerFields.contains(field);
	}
	
	public final boolean shouldShowField(String field){
		if(headerFields.isEmpty() && gridFields.isEmpty())
			return true;
		return shouldShowHeaderField(field) || shouldShowGridField(field);
	}

	public final boolean shouldShowGridField(String field){
		return gridFields.isEmpty() || gridFields.contains(field);
	}
	public String ajaxLoadSubSchemes() {
		subSchemes = getPersistenceService().findAllBy("from SubScheme where scheme.id=? and isActive=1 order by name", schemeId);
		return Constants.SUBSCHEMES;
	}
	
	public void setBudgetService(BudgetService budgetService) {
		this.budgetService = budgetService;
	}
	
	
	protected ValueStack getValueStack() {
		return ActionContext.getContext().getValueStack();
	}
	
	public List<BudgetAmountView> populateAmountData(List<BudgetDetail> budgetDetails,Date asOnDate, CFinancialYear finYear){
		List<BudgetAmountView> list = new ArrayList<BudgetAmountView>();
		Map<String, Object> paramMap;
		Long finYearId = finYear.getId();
		for (BudgetDetail detail : budgetDetails) {
			paramMap = budgetDetailHelper.constructParamMap(getValueStack(),detail);
			BudgetAmountView view = new BudgetAmountView();
			budgetDetailHelper.populateData(view,paramMap, asOnDate,re);
			BudgetDetail detailWithoutBudget = new BudgetDetail();
			detailWithoutBudget.copyFrom(detail);
			detailWithoutBudget.setBudget(null);
			List<BudgetDetail> budgetDetail = budgetDetailService.searchByCriteriaWithTypeAndFY(finYearId,"BE",detailWithoutBudget);
			if(budgetDetail!=null && budgetDetail.size()>0){
				BigDecimal approvedAmount = budgetDetail.get(0).getApprovedAmount();
				view.setCurrentYearBeApproved(approvedAmount==null?BigDecimal.ZERO.toString():approvedAmount.toString());
				view.setReappropriation(budgetDetail.get(0).getApprovedReAppropriationsTotal().toString());
			}
			view.setTotal(new BigDecimal(view.getCurrentYearBeApproved()).add(new BigDecimal(view.getReappropriation())).toString());
			list.add(view);
		}
		return list;
	}

	public void setShowMessage(boolean showMessage) {
		this.showMessage = showMessage;
	}

	public boolean isShowMessage() {
		return showMessage;
	}

	public void setBeAmounts(List<BigDecimal> beAmounts) {
		this.beAmounts = beAmounts;
	}

	public List<BigDecimal> getBeAmounts() {
		return beAmounts;
	}

	public void setNextYearRange(String nextYearRange) {
		this.nextYearRange = nextYearRange;
	}

	public String getNextYearRange() {
		return nextYearRange;
	}

	public void setReferenceBudget(Budget referenceBudget) {
		this.referenceBudget = referenceBudget;
	}

	public Budget getReferenceBudget() {
		return referenceBudget;
	}

	public void setFinancialYear(CFinancialYear financialYear) {
		this.financialYear = financialYear;
	}

	public CFinancialYear getFinancialYear() {
		return financialYear;
	}

	public void setBudgetList(List<Budget> budgetList) {
		this.budgetList = budgetList;
	}

	public List<Budget> getBudgetList() {
		return budgetList;
	}
	public void setBudgetWorkflowService(WorkflowService<Budget> budgetWorkflowService) {
		this.budgetWorkflowService = budgetWorkflowService;
	}

	public void setEisCommonsService(EisCommonsService eisCommonsService) {
		this.eisCommonsService = eisCommonsService;
	}
}
