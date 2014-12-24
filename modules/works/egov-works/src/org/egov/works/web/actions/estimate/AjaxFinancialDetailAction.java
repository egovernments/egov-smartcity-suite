package org.egov.works.web.actions.estimate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.commons.CFunction;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.dao.budget.BudgetDetailsDAO;
import org.egov.dao.budget.BudgetGroupDAO;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.model.budget.BudgetGroup;
import org.egov.web.actions.BaseFormAction;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.WorksService;

public class AjaxFinancialDetailAction extends BaseFormAction {	
	private BudgetGroupDAO budgetGroupDAO;
	private static final String SUBSCHEMES = "subschemes";
	private static final String VIEWBUDGET = "viewBudget";
	private static final String SCHEMES = "schemes";
	private static final String BUDGETGROUPS = "budgetgroups";
	private List<SubScheme> subSchemes;
	private List<Scheme> schemes;
	private List<BudgetGroup> budgetGroups;
	private Integer schemeId;
	private Integer fundId; 
	private Long functionId;
	private Date estimateDate;
	private BigDecimal budgetAvailable;
	private Long finYear;
	private Integer deptId;
	private Integer boundaryId;
	private AbstractEstimateService abstractEstimateService = new AbstractEstimateService();
	private WorksService worksService = new WorksService();
	private List<BudgetGroup> budgetGroupsList = new ArrayList<BudgetGroup>();
	private Map<String, Object> searchMap = new HashMap<String, Object>();
	private ShowBudgetDetailsAction showBudgetDetailsAction = new ShowBudgetDetailsAction();
	private BudgetDetailsDAO budgetDetailsDAO;
	private BigDecimal budgetUtilized;
	private Long functionid;
	private Long estimateId;
	private Integer functionaryid;
	private Integer fundid;
	private Long budgetheadid;
	private Integer schemeid;
	private Integer subschemeid;
	private BigDecimal totalBudget;

	public BigDecimal getBudgetUtilized() {
		return budgetUtilized;
	}

	public void setBudgetUtilized(BigDecimal budgetUtilized) {
		this.budgetUtilized = budgetUtilized;
	}
	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public void setFinYear(Long finYear) {
		this.finYear = finYear;
	}
		
	public BigDecimal getBudgetAvailable() {
		return budgetAvailable;
	}
	
	public void setBudgetAvailable(BigDecimal budgetAvailable) {
		this.budgetAvailable = budgetAvailable;
	}


	public BudgetDetailsDAO getBudgetDetailsDAO() {
		return budgetDetailsDAO;
	}
	
	public void setBudgetDetailsDAO(BudgetDetailsDAO budgetDetailsDAO) {
		this.budgetDetailsDAO = budgetDetailsDAO;
			}
	public ShowBudgetDetailsAction getShowBudgetDetailsAction() {
		return showBudgetDetailsAction;
			}

	public void setShowBudgetDetailsAction(
			ShowBudgetDetailsAction showBudgetDetailsAction) {
		this.showBudgetDetailsAction = showBudgetDetailsAction;
			}

	public Long getFunctionid() {
		return functionid;
			}
			
	public void setFunctionid(Long functionid) {
		this.functionid = functionid;
		}

	public Integer getFunctionaryid() {
		return functionaryid;
	}

	public void setFunctionaryid(Integer functionaryid) {
		this.functionaryid = functionaryid;
	}
	
	public Integer getFundid() {
		return fundid;
	}

	public void setFundid(Integer fundid) {
		this.fundid = fundid;
	}

	public Long getBudgetheadid() {
		return budgetheadid;
	}

	public void setBudgetheadid(Long budgetheadid) {
		this.budgetheadid = budgetheadid;
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

	public Map<String, Object> getSearchMap() {
		return searchMap;
	}

	public void setSearchMap(Map<String, Object> searchMap) {
		this.searchMap = searchMap;
	}

	

	public WorksService getWorksService() {
		return worksService;
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}

	public AbstractEstimateService getAbstractEstimateService() {
		return abstractEstimateService;
	}

	public void setAbstractEstimateService(
			AbstractEstimateService abstractEstimateService) {
		this.abstractEstimateService = abstractEstimateService;
	}

	public Long getEstimateId() {
		return estimateId;
	}

	public void setEstimateId(Long estimateId) {
		this.estimateId = estimateId;
	}

	public BigDecimal getTotalBudget() {
		return totalBudget;
	}

	public void setTotalBudget(BigDecimal totalBudget) {
		this.totalBudget = totalBudget;
	}

	public Date getEstimateDate() {
		return estimateDate;
	}

	public void setEstimateDate(Date estimateDate) {
		this.estimateDate = estimateDate;
	}

	public Integer getSchemeId() {
		return schemeId;
	}

	public void setSchemeId(Integer schemeId) {
		this.schemeId = schemeId;
	}

	public Integer getFundId() {
		return fundId;
	}

	public void setFundId(Integer fundId) {
		this.fundId = fundId;
	}
	
	public Long getFunctionId() {
		return functionId;
	}

	public void setFunctionId(Long functionId) {
		this.functionId = functionId;
	}
	
	public List<SubScheme> getSubSchemes() {
		return subSchemes;
	}

	public void setSubSchemes(List<SubScheme> subSchemes) {
		this.subSchemes = subSchemes;
	}

	public List<Scheme> getSchemes() {
		return schemes;
	}

	public void setSchemes(List<Scheme> schemes) {
		this.schemes = schemes;
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
	
	public Object getModel() {
		return null;
	}

	public AjaxFinancialDetailAction() {

}

	public String loadSchemes() {
		schemes = getPersistenceService()
				.findAllBy(
						"from org.egov.commons.Scheme sc where sc.isactive=1 and sc.fund.id=? and ? between validfrom and validto",
						fundId, estimateDate);
		return SCHEMES;
	}

	public String loadSubSchemes() {
		subSchemes = getPersistenceService()
				.findAllBy(
						"from org.egov.commons.SubScheme where scheme.id=? and ? between validfrom and validto",
						schemeId, estimateDate);
		return SUBSCHEMES;
	}

	public String loadBudgetGroups() {
		try {
			if (functionId == -1) {
				budgetGroups = (List<BudgetGroup>) budgetGroupDAO
						.getBudgetGroupList();
			} else {
				CFunction function = (CFunction) getPersistenceService().find(
						"from org.egov.commons.CFunction where id = ? ",
						functionId);
				if (function == null) {
					throw new ValidationException(
							Arrays
									.asList(new ValidationError(
											"nobudgetforfunction",
											"Budget head information not available for the chosen function")));
				} else {
					budgetGroups = (List<BudgetGroup>) budgetGroupDAO.getBudgetHeadByFunction(function.getCode());
				}
			}
		} catch (ValidationException egovEx) {
			budgetGroups = new ArrayList<BudgetGroup>();
			addActionError("Unable to load budget head information");
			return BUDGETGROUPS;

		} catch (Exception e) {
			addFieldError("budgetunavailable",
					"Unable to load budget head information");
		}
		return BUDGETGROUPS;
	}

	/*
	 * to show the popup msg of budget details by thanooj 20-12-2010 - 12:50pm
	 */

	public String showBudgetDetails() {

		BudgetGroup budgetGroup = budgetGroupDAO.getBudgetHeadById(budgetheadid);
		budgetGroupsList.add(budgetGroup);
	
		getSearchMap().put("financialyearid", finYear);
		getSearchMap().put("budgetheadid", budgetGroupsList);
		getSearchMap().put("functionid", getFunctionid());
		getSearchMap().put("functionaryid", getFunctionaryid());
		getSearchMap().put("fundid", getFundid());
		getSearchMap().put("schemeid", getSchemeid());
		getSearchMap().put("subschemeid", getSubschemeid());
		getSearchMap().put("deptid", deptId);
		getSearchMap().put("boundaryid", boundaryId);
			
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

	public void setBoundaryId(Integer boundaryId) {
		this.boundaryId = boundaryId;
	}

	
}
