package org.egov.works.web.actions.rateContract;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.service.CommonsService;
import org.egov.dao.budget.BudgetDetailsDAO;
import org.egov.dao.budget.BudgetGroupDAO;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.model.budget.BudgetGroup;
import org.egov.utils.Constants;
import org.egov.web.actions.BaseFormAction;
import org.egov.works.services.WorksService;
import org.egov.works.web.actions.estimate.ShowBudgetDetailsAction;

public class AjaxIndentRateContractAction extends BaseFormAction {
	private static final String BUDGETGROUPS = "budgetgroups";
	private static final String VIEWBUDGET = "viewBudget";
	
	private List<BudgetGroup>  budgetGroups;
	private BudgetGroupDAO budgetGroupDAO;
	private Date indentDate;
	private Long functionId;
	private CFunction function; 
	private Long budgetheadid;
	private Integer fundid;
	private Integer deptid;
	private Integer boundaryId;
	private Long functionid;
	private String startDate;
	private Integer functionaryid;
	private Integer schemeid;
	private Integer subschemeid;
	private Map<String, Object> searchMap = new HashMap<String, Object>();
	private List<BudgetGroup> budgetGroupsList = new ArrayList<BudgetGroup>();
	private ShowBudgetDetailsAction showBudgetDetailsAction = new ShowBudgetDetailsAction();
	private BudgetDetailsDAO budgetDetailsDAO;
	private WorksService worksService = new WorksService();
	private BigDecimal totalBudget;
	private BigDecimal budgetAvailable;
	private BigDecimal budgetUtilized;
	private CommonsService commonsService;
	
	public String execute(){
		return SUCCESS;
	}
	
	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}


public AjaxIndentRateContractAction(){} 
	
public String loadBudgetGroups() { 
		try {
			if (functionId != -1) {
				function = (CFunction) getPersistenceService().find("from org.egov.commons.CFunction where id = ? ", functionId);
				if (function == null) 
				{
					throw new ValidationException(Arrays.asList(new ValidationError("nobudgetforfunction","Budget head information not available for the chosen function")));
				} 
				else 
				{
					budgetGroups = (List<BudgetGroup>) budgetGroupDAO.getBudgetHeadByFunction(function.getCode());
				}
			}
		} 
		catch (ValidationException egovEx) 
		{
			budgetGroups = new ArrayList<BudgetGroup>();
			addActionError("Unable to get budget head information");
			return BUDGETGROUPS;

		} 
		catch (Exception e) 
		{
			addFieldError("budgetunavailable","Unable to load budget head information");
		}
		return BUDGETGROUPS;
	}

	public CFunction getFunction() {
		return function;
	}

	public void setFunction(CFunction function) {
		this.function = function;
	}

	public List<BudgetGroup> getBudgetGroups() {
		return budgetGroups;
	}

	public void setBudgetGroups(List<BudgetGroup> budgetGroups) {
		this.budgetGroups = budgetGroups;
	}

	public BudgetGroupDAO getBudgetGroupDAO() {
		return budgetGroupDAO;
	}

	public void setBudgetGroupDAO(BudgetGroupDAO budgetGroupDAO) {
		this.budgetGroupDAO = budgetGroupDAO;
	}

	public Date getIndentDate() {
		return indentDate;
	}

	public void setIndentDate(Date indentDate) {
		this.indentDate = indentDate;
	}

	public Long getFunctionId() {
		return functionId;
	}

	public void setFunctionId(Long functionId) {
		this.functionId = functionId;
	}
	
	public Map<String, Object> getSearchMap() {
		return searchMap;
	}

	public void setSearchMap(Map<String, Object> searchMap) {
		this.searchMap = searchMap;
	}
	
	public Long getBudgetheadid() {
		return budgetheadid;
	}

	public void setBudgetheadid(Long budgetheadid) {
		this.budgetheadid = budgetheadid;
	}

	public Integer getFundid() {
		return fundid;
	}

	public void setFundid(Integer fundid) {
		this.fundid = fundid;
	}

	public List<BudgetGroup> getBudgetGroupsList() {
		return budgetGroupsList;
	}

	public void setBudgetGroupsList(List<BudgetGroup> budgetGroupsList) {
		this.budgetGroupsList = budgetGroupsList;
	}

	public Integer getBoundaryId() {
		return boundaryId;
	}

	public void setBoundaryId(Integer boundaryId) {
		this.boundaryId = boundaryId;
	}

	public Integer getDeptid() {
		return deptid;
	}

	public void setDeptid(Integer deptid) {
		this.deptid = deptid;
	}

	public ShowBudgetDetailsAction getShowBudgetDetailsAction() {
		return showBudgetDetailsAction;
	}

	public void setShowBudgetDetailsAction(
			ShowBudgetDetailsAction showBudgetDetailsAction) {
		this.showBudgetDetailsAction = showBudgetDetailsAction;
	}


	public BudgetDetailsDAO getBudgetDetailsDAO() {
		return budgetDetailsDAO;
	}

	public void setBudgetDetailsDAO(BudgetDetailsDAO budgetDetailsDAO) {
		this.budgetDetailsDAO = budgetDetailsDAO;
	}

	
	public WorksService getWorksService() {
		return worksService;
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}


	public BigDecimal getTotalBudget() {
		return totalBudget;
	}

	public void setTotalBudget(BigDecimal totalBudget) {
		this.totalBudget = totalBudget;
	}

	public BigDecimal getBudgetAvailable() {
		return budgetAvailable;
	}

	public void setBudgetAvailable(BigDecimal budgetAvailable) {
		this.budgetAvailable = budgetAvailable;
	}

	public BigDecimal getBudgetUtilized() {
		return budgetUtilized;
	}

	public void setBudgetUtilized(BigDecimal budgetUtilized) {
		this.budgetUtilized = budgetUtilized;
	}

	public Long getFunctionid() {
		return functionid;
	}

	public void setFunctionid(Long functionid) {
		this.functionid = functionid;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
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
	
	public String showBudgetDetails() throws ParseException {
		BudgetGroup budgetGroup = budgetGroupDAO.getBudgetHeadById(budgetheadid);
		budgetGroupsList.add(budgetGroup);
		SimpleDateFormat sdf= new SimpleDateFormat("dd-MMM-yyyy",Constants.LOCALE);
		CFinancialYear financialyear = commonsService.getFinancialYearByDate(sdf.parse(startDate));
	
		getSearchMap().put("financialyearid", financialyear.getId());
		getSearchMap().put("budgetheadid", budgetGroupsList);
		getSearchMap().put("functionid", getFunctionid());
		getSearchMap().put("functionaryid", getFunctionaryid());
		getSearchMap().put("fundid", getFundid());
		getSearchMap().put("schemeid", getSchemeid());
		getSearchMap().put("subschemeid", getSubschemeid());
		getSearchMap().put("deptid", getDeptid());
		getSearchMap().put("boundaryid", getBoundaryId());
			
		getShowBudgetDetailsAction().setBudgetDetailsDAO(budgetDetailsDAO);
		getShowBudgetDetailsAction().setWorksService(worksService);
		List<Long> budgethead = new ArrayList<Long>();
		budgethead.add(budgetheadid);// budgetGroupID using as parameter for getPlanningBudgetAvailable().
		
		//calling getTotalBudget() in ShowBudgetDetailsAction
		BigDecimal tatalbudget = getShowBudgetDetailsAction().getTotalBudget(getSearchMap());
		setTotalBudget(tatalbudget);
		
		//calling getBudgetAvailable() in ShowBudgetDetailsAction
		BigDecimal dugetavilable =getShowBudgetDetailsAction().getBudgetAvailable(searchMap);
		setBudgetAvailable(dugetavilable);
		
		//calling getBudgetUtilized() in ShowBudgetDetailsAction
		BigDecimal budgetUtilized = getShowBudgetDetailsAction().getBudgetUtilized(tatalbudget,dugetavilable);
		setBudgetUtilized(budgetUtilized);
		
		return VIEWBUDGET;

	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}
	
}
