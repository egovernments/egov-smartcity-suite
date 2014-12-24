/*
 * Created on Jan 17, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.egov.dao.budget;



import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.egov.commons.CChartOfAccounts;
import org.egov.infstr.ValidationException;
import org.egov.infstr.dao.GenericDAO;
import org.egov.model.budget.BudgetGroup;
import org.egov.model.budget.BudgetUsage;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface BudgetDetailsDAO extends GenericDAO{
	public boolean consumeEncumbranceBudget(Long financialyearid,Integer moduleid,String referencenumber,Integer departmentid, Long functionid,Integer functionaryid,Integer schemeid,Integer subschemeid,Integer fieldid,List<Long> budgetheadid,Integer fundid,double amount,String appropriationnumber) throws ValidationException;
	public void releaseEncumbranceBudget(Long financialyearid,Integer moduleid,String referencenumber,Integer departmentid, Long functionid,Integer functionaryid,Integer schemeid,Integer subschemeid,Integer fieldid,List<Long> budgetheadid,Integer fundid,double amount,String appropriationnumber) throws ValidationException;
	public BigDecimal getActualBudgetUtilized(Map<String, Object> paramMap) throws ValidationException;
	public BigDecimal getSanctionedPlanningBudget(Map<String, Object> paramMap) throws ValidationException;
	public BigDecimal getPlanningBudgetAvailable(Long financialyearid, Integer departmentid, Long functionid,Integer functionaryid, Integer schemeid, Integer subschemeid,Integer boundaryid, List<Long> budgetheadid,Integer fundid) throws ValidationException;
	public BigDecimal getBudgetedAmtForYear(Map<String, Object> paramMap) throws ValidationException;
	public BigDecimal getBudgetedAmtForYearRegardingBEorRE(Map<String, Object> paramMap, String type) throws ValidationException;
	public boolean budgetaryCheck(Map<String, Object> paramMap)throws ValidationException;
	public boolean budgetaryCheckForBill(Map<String,Object> paramMap) throws ValidationException;
	public List<BudgetUsage> getListBudgetUsage(Map<String, Object> queryParamMap) throws ValidationException;
	public BigDecimal getBillAmountForBudgetCheck(Map<String,Object> paramMap) throws ValidationException;
	public BigDecimal getActualBudgetUtilizedForBudgetaryCheck(Map<String,Object> paramMap) throws ValidationException;
	public List<BudgetGroup> getBudgetHeadByGlcode(CChartOfAccounts coa) throws ValidationException;
}
