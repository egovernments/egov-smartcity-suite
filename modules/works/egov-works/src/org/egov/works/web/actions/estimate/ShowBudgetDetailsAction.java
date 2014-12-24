package org.egov.works.web.actions.estimate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.dao.budget.BudgetDetailsDAO;
import org.egov.dao.budget.BudgetDetailsHibernateDAO;
import org.egov.dao.budget.BudgetGroupDAO;
import org.egov.dao.budget.BudgetGroupHibernateDAO;
import org.egov.infstr.ValidationException;
import org.egov.model.budget.BudgetGroup;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.WorksService;

import com.opensymphony.xwork2.ActionSupport;

public class ShowBudgetDetailsAction extends ActionSupport {

	private static final Logger LOGGER = Logger
			.getLogger(ShowBudgetDetailsAction.class);
	private AbstractEstimateService abstractEstimateService = new AbstractEstimateService();
	private WorksService worksService = new WorksService();
	private Long functionid;
	private Long estimateId;
	private Integer functionaryid;
	private Integer fundid;
	private Long budgetheadid;
	private Integer schemeid;
	private Integer subschemeid;

	Map<String, Object> searchMap = new HashMap<String, Object>();

	BudgetDetailsDAO budgetDetailsDAO;

	public void setBudgetDetailsDAO(BudgetDetailsDAO budgetDetailsDAO) {
		this.budgetDetailsDAO = budgetDetailsDAO;
	}

	public BudgetDetailsDAO getBudgetDetailsDAO() {
		return budgetDetailsDAO;
	}

	public Map<String, Object> getSearchMap() {
		return searchMap;
	}

	public void setSearchMap(Map<String, Object> searchMap) {
		this.searchMap = searchMap;
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
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

	public Long getEstimateId() {
		return estimateId;
	}

	public void setEstimateId(Long estimateId) {
		this.estimateId = estimateId;
	}

	public BigDecimal getTotalBudget(Map<String, Object> searchMap) {
		BigDecimal totalbudget = new BigDecimal(0);
		try {
			String appValue = null;

			// here we need to get API method with req properties.

			totalbudget = budgetDetailsDAO.getBudgetedAmtForYear(searchMap);

			if (StringUtils.isNotBlank(worksService
					.getWorksConfigValue("percentage_grant")))
				appValue = worksService.getWorksConfigValue("percentage_grant");
			if (StringUtils.isNotEmpty(appValue)) {
				java.math.BigDecimal appValuebigd = new BigDecimal(appValue);
				totalbudget = totalbudget.multiply(appValuebigd);
			}

		} catch (ValidationException valEx) {
			LOGGER.error(valEx);
		}

		return totalbudget;

	}

	public BigDecimal getBudgetAvailable(Map<String, Object> searchMap) {

		List<BudgetGroup> budgetGroupList = (List<BudgetGroup>)searchMap.get("budgetheadid");
		List<Long> budgetHeadidList = new ArrayList<Long>();
		budgetHeadidList.add(budgetGroupList.get(0).getId());
		BigDecimal dugetavilable = budgetDetailsDAO.getPlanningBudgetAvailable(
				Long.valueOf(searchMap.get("financialyearid").toString()),
				Integer.valueOf(searchMap.get("deptid").toString()),
				Long.valueOf(searchMap.get("functionid").toString()),
				searchMap.get("functionaryid")==null?null:Integer.valueOf(searchMap.get("functionaryid").toString()), 
				searchMap.get("schemeid")==null?null:Integer.valueOf(searchMap.get("schemeid").toString()), 
				searchMap.get("subschemeid")==null?null:Integer.valueOf(searchMap.get("subschemeid").toString()), 
				searchMap.get("boundaryid")==null?null:Integer.valueOf(searchMap.get("boundaryid").toString()),
				budgetHeadidList, 
				Integer.valueOf(searchMap.get("fundid").toString()));
		
		
		return dugetavilable;
	}

	public BigDecimal getBudgetUtilized(BigDecimal totalBudget,
			BigDecimal budgetAvailable) {

		return totalBudget.subtract(budgetAvailable);
	}

	public AbstractEstimateService getAbstractEstimateService() {
		return abstractEstimateService;
	}

	public void setAbstractEstimateService(
			AbstractEstimateService abstractEstimateService) {
		this.abstractEstimateService = abstractEstimateService;
	}

}
