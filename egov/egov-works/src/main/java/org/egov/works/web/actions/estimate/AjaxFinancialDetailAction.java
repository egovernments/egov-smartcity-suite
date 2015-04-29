package org.egov.works.web.actions.estimate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.commons.CFunction;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.commons.service.CommonsService;
import org.egov.dao.budget.BudgetGroupDAO;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.model.budget.BudgetGroup;
import org.egov.web.actions.BaseFormAction;
import org.springframework.beans.factory.annotation.Autowired;

public class AjaxFinancialDetailAction extends BaseFormAction {
	private static final Logger logger = Logger.getLogger(AjaxFinancialDetailAction.class);
	private BudgetGroupDAO budgetGroupDAO;
	private static final String SUBSCHEMES = "subschemes";
	private static final String SCHEMES = "schemes";
	private static final String BUDGETGROUPS = "budgetgroups";
	private List<SubScheme> subSchemes;
	private List<Scheme> schemes;
	private List<BudgetGroup> budgetGroups;
	private Integer schemeId;
	private Integer fundId; 
	private Long functionId;
	private Date estimateDate;
	@Autowired
        private CommonsService commonsService;
	private String loadBudgetGroupsValidationError="";
	
	public Date getEstimateDate() {
		return estimateDate;
	}

	public void setEstimateDate(Date estimateDate) {
		this.estimateDate = estimateDate;
	}

	public AjaxFinancialDetailAction()
	{
		
	}
	
	public String loadSchemes() {
		schemes = getPersistenceService().findAllBy(
				"from org.egov.commons.Scheme sc where sc.isactive=1 and sc.fund.id=? and ? between validfrom and validto",fundId,estimateDate);
		return SCHEMES;
	}

	public String loadSubSchemes() {
		subSchemes = getPersistenceService().findAllBy("from org.egov.commons.SubScheme where scheme.id=? and ? between validfrom and validto", schemeId,estimateDate);
		return SUBSCHEMES;
	}
	
	public String loadBudgetGroups() {
		try {
			if(functionId==-1){
				budgetGroups = (List<BudgetGroup>) budgetGroupDAO.getBudgetGroupList();
			}
			else{
			CFunction function = (CFunction) getPersistenceService().find("from org.egov.commons.CFunction where id = ? ", functionId);
			if(function==null){
				throw new ValidationException(Arrays.asList(new ValidationError("nobudgetforfunction","Budget head information not available for the chosen function")));
			}
			else{
			budgetGroups = (List<BudgetGroup>)budgetGroupDAO.getBudgetHeadByFunction(function.getCode());
			}
			}
		} catch (ValidationException egovEx) {
			logger.error("Unable to load budget head information>>>"+egovEx.getMessage());
			budgetGroups = new ArrayList<BudgetGroup>();
			addActionError("Unable to load budget head information");
			return BUDGETGROUPS;
			
		} catch (Exception e) {
			logger.error("Budgetunavailable : Unable to load budget head information>>>"+e.getMessage());
			addFieldError("budgetunavailable", "Unable to load budget head information");
		}
		return BUDGETGROUPS;
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


	public void setBudgetGroupDAO(BudgetGroupDAO budgetGroupDAO) {
		this.budgetGroupDAO = budgetGroupDAO;
	}
	
	public BudgetGroupDAO getBudgetGroupDAO() {
		return budgetGroupDAO;
	}

	public Object getModel() {
		return null;
	}
	
	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}
	
	public String getLoadBudgetGroupsValidationError() {
		return loadBudgetGroupsValidationError;
	}

	public void setLoadBudgetGroupsValidationError(
			String loadBudgetGroupsValidationError) {
		this.loadBudgetGroupsValidationError = loadBudgetGroupsValidationError;
	}
}
