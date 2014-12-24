package org.egov.services.budget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.script.ScriptContext;

import org.apache.commons.lang.StringUtils;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.models.Script;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.workflow.WorkflowService;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.lib.rjbac.user.UserImpl;
import org.egov.model.budget.Budget;
import org.egov.model.budget.BudgetDetail;
import org.egov.model.budget.BudgetGroup;
import org.egov.pims.commons.Position;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;
import org.egov.utils.Constants;
import org.egov.web.actions.report.BudgetVarianceEntry;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;


public class BudgetDetailService extends PersistenceService<BudgetDetail, Long>{
	//protected EisManager eisManager;
	private EmployeeService employeeService;
	protected WorkflowService<BudgetDetail> budgetDetailWorkflowService;
	private GenericHibernateDaoFactory genericDao;
	private ScriptService scriptExecutionService;
	PersistenceService persistenceService;
	
	public void setBudgetDetailWorkflowService(WorkflowService<BudgetDetail> budgetDetailWorkflowService) {
		this.budgetDetailWorkflowService = budgetDetailWorkflowService;
	}
	public BudgetDetailService(){
		setType(BudgetDetail.class);
	}
	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}
	
	public void setScriptExecutionService(ScriptService scriptService) {
		this.scriptExecutionService = scriptService;
	}
	
	public boolean canViewApprovedAmount(PersistenceService persistenceService, Budget budget){
		Script script = (Script) persistenceService.findAllByNamedQuery(Script.BY_NAME, "budget.report.view.access").get(0);
		ScriptContext context = ScriptService.createContext("wfItem",budget,"eisManagerBean", employeeService,"userId",Integer.valueOf(EGOVThreadLocals.getUserId().trim()));
		Integer result = (Integer)scriptExecutionService.executeScript(script, context);
		if(result == 1)
			return true;
		return false;
	}

	public BudgetDetail createBudgetDetail(BudgetDetail detail,Position position,PersistenceService service){
		try {
			setRelatedEntitesOn(detail, service);
			BudgetDetail savedDetail = budgetDetailWorkflowService.start(detail, position,"");
			findAll();
			return savedDetail;
		}
		catch (ConstraintViolationException e) {
			throw new ValidationException(Arrays.asList(new ValidationError("budgetDetail.duplicate","budgetdetail.exists")));
		}
	}
	
	public List<BudgetDetail> searchBy(BudgetDetail detail) {
		return constructCriteria(detail).list();
		
	}

	public List<BudgetDetail> searchByCriteriaAndFY(Long financialYear,BudgetDetail detail,boolean isApprove,Position pos) {
		Criteria criteria = constructCriteria(detail).createCriteria(Constants.BUDGET).add(Restrictions.eq("financialYear.id", financialYear));
		if(isApprove)
			criteria.createCriteria(Constants.STATE).add(Restrictions.eq("owner",pos));	
		else
			criteria.createCriteria(Constants.STATE).add(Restrictions.eq("value","NEW"));
		return criteria.list();
	}
	
	public List<BudgetDetail> searchByCriteriaWithTypeAndFY(Long financialYear,String type,BudgetDetail detail) {
		if(detail.getBudget()!=null && detail.getBudget().getId()!=0l){
			Map<String, Object> map = new HashMap<String, Object>();
			addCriteriaExcludingBudget(detail, map);
			Criteria criteria = getSession().createCriteria(BudgetDetail.class);
			addBudgetDetailCriteria(map, criteria);
			return criteria.createCriteria(Constants.BUDGET).add(Restrictions.eq("financialYear.id", financialYear)).add(Restrictions.eq("isbere", type)).list();
		}
		return constructCriteria(detail).createCriteria(Constants.BUDGET).add(Restrictions.eq("financialYear.id", financialYear)).add(Restrictions.eq("isbere", type)).list();
	}
	
	private Map<String, Object> createCriteriaMap(BudgetDetail detail) {
		Map<String, Object> map = new HashMap<String, Object>();
		addCriteriaExcludingBudget(detail, map);
		map.put(Constants.BUDGET, detail.getBudget()==null?0l:detail.getBudget().getId());
		return map;
	}
	
	protected void addCriteriaExcludingBudget(BudgetDetail detail,Map<String, Object> map) {
		map.put("budgetGroup", detail.getBudgetGroup()==null?0l:detail.getBudgetGroup().getId());
		map.put("function", detail.getFunction()==null?0l:detail.getFunction().getId());
		map.put("functionary", detail.getFunctionary()==null?0:detail.getFunctionary().getId());
		map.put("scheme", detail.getScheme()==null?0:detail.getScheme().getId());
		map.put("subScheme", detail.getSubScheme()==null?0:detail.getSubScheme().getId());
		map.put("executingDepartment", detail.getExecutingDepartment()==null?0:detail.getExecutingDepartment().getId());
		map.put("boundary", detail.getBoundary()==null?0:detail.getBoundary().getId());
		map.put("fund", detail.getFund()==null?0:detail.getFund().getId());
	}
	
	
	public List<BudgetDetail> findAllBudgetDetailsFor(Budget budget,BudgetDetail example){
		List<Budget> budgets = new ArrayList<Budget>();
		collectLeafBudgets(budget, budgets);
		budgets.add(findBudget(budget));
		Criteria criteria = constructCriteria(example);
		criteria.add(Restrictions.in(Constants.BUDGET, budgets));
		criteria.addOrder(Property.forName("budget").asc());
		criteria.createAlias("budgetGroup", "bg");
		criteria.addOrder(Property.forName("bg.name").asc());
		return criteria.list();
	}
	public List<BudgetDetail> findAllBudgetDetailsForParent(Budget budget,BudgetDetail example, PersistenceService persistenceService){
		if(budget==null || budget.getId()==null) return Collections.EMPTY_LIST;
		budget = (Budget) persistenceService.find("from Budget where id=?",budget.getId());
		BudgetDetail detail = new BudgetDetail();
		detail.copyFrom(example);
		detail.setBudget(null);
		String materializedPath = budget.getMaterializedPath();
		return constructCriteria(detail).addOrder(Property.forName("executingDepartment").asc()).createCriteria(Constants.BUDGET)
								.add(Restrictions.like("materializedPath", materializedPath==null?"":materializedPath.concat("%"))).list();
	}
	
	public List<BudgetDetail> findAllBudgetDetailsWithReAppropriation(Budget budget,BudgetDetail example){
		List<BudgetDetail> budgetDetails = findAllBudgetDetailsFor(budget,example);
		return budgetDetails;
	}

	private Budget findBudget(Budget budget) {
		return (Budget) getSession().load(Budget.class, budget.getId());
	}
	
	
	public List<Budget> findBudgetsForFY(Long financialYear) {
		Criteria criteria = getSession().createCriteria(Budget.class);
		return criteria.add(Restrictions.eq("financialYear.id", financialYear)).add(Restrictions.eq("isActiveBudget", true)).list();
	}
	public List<Budget> findApprovedBudgetsForFY(Long financialYear) {
		Criteria criteria = getSession().createCriteria(Budget.class);
		return criteria.add(Restrictions.eq("financialYear.id", financialYear)).add(Restrictions.eq("isActiveBudget", true))
			.addOrder(Property.forName("name").asc())
			.createCriteria(Constants.STATE, Constants.STATE).add(Restrictions.eq("state.value","END"))
			.list();
	}

	public List<Budget> findBudgetsForFYWithNewState(Long financialYear) {
		Criteria criteria = getSession().createCriteria(Budget.class);
		criteria.createCriteria(Constants.STATE, Constants.STATE).add(Restrictions.eq("state.value","NEW"));	
		return criteria.add(Restrictions.eq("financialYear.id", financialYear)).add(Restrictions.eq("isActiveBudget", true)).list();
	}

	public List<Budget> findPrimaryBudgetForFY(Long financialYear) {
		Criteria criteria = getSession().createCriteria(Budget.class);
		return criteria.add(Restrictions.eq("financialYear.id", financialYear)).add(Restrictions.eq("isActiveBudget", true)).add(Restrictions.eq("isPrimaryBudget", true)).add(Restrictions.isNull("parent")).list();
	}

	public Budget findApprovedPrimaryParentBudgetForFY(Long financialYear) {
		Criteria criteria = getSession().createCriteria(Budget.class);
		List<Budget> budgetList =  criteria.add(Restrictions.eq("financialYear.id", financialYear)).add(Restrictions.eq("isbere", "RE"))
									.add(Restrictions.eq("isActiveBudget", true)).add(Restrictions.eq("isPrimaryBudget", true))
									.add(Restrictions.isNull("parent"))
									.addOrder(Property.forName("name").asc())
									.createCriteria(Constants.STATE, Constants.STATE).add(Restrictions.eq("state.value","END"))
									.list();
		if(budgetList.isEmpty()){
			Criteria c = getSession().createCriteria(Budget.class);
			budgetList =  c.add(Restrictions.eq("financialYear.id", financialYear)).add(Restrictions.eq("isbere", "BE"))
							.add(Restrictions.eq("isActiveBudget", true)).add(Restrictions.eq("isPrimaryBudget", true))
							.add(Restrictions.isNull("parent"))
							.addOrder(Property.forName("name").asc())
							.createCriteria(Constants.STATE, Constants.STATE).add(Restrictions.eq("state.value","END"))
							.list();
			if(budgetList.isEmpty())
				return null;
		}
		return budgetList.get(0);	
	}
	
	public Set<Budget> findBudgetTree(Budget budget,BudgetDetail example) {
		if(budget==null) return Collections.EMPTY_SET;
		Criteria budgetDetailCriteria = constructCriteria(example);
		budgetDetailCriteria.createCriteria(Constants.BUDGET);
		List<Budget> leafBudgets = budgetDetailCriteria.setProjection(Projections.distinct(Projections.property(Constants.BUDGET))).list();
		List<Budget> parents = new ArrayList<Budget>();
		Set<Budget> budgetTree = new LinkedHashSet<Budget>();
		for (Budget leaf : leafBudgets) {
			parents.clear();
			while(leaf!=null && leaf.getId()!=budget.getId()){
				parents.add(leaf);
				leaf=leaf.getParent();
			}
			if(leaf!=null){
				parents.add(leaf);
				budgetTree.addAll(parents);
			}
		}
		return budgetTree;
	}

	private List<Budget> findChildren(Budget parent) {
		return (List<Budget>) ((PersistenceService)this).findAllBy("from Budget b where b.parent=?", parent);
	}
	
	private void collectLeafBudgets(Budget parent, List<Budget> children) {
		List<Budget> myChildren = findChildren(parent);
		for (Budget child : myChildren) {
			collectLeafBudgets(child, children);
			if(findChildren(child).isEmpty()) children.add(child);
		}
	}

	private Criteria constructCriteria(BudgetDetail example) {
		Map<String, Object> map = createCriteriaMap(example);
		Criteria criteria = getSession().createCriteria(BudgetDetail.class);
		addBudgetDetailCriteria(map, criteria);
		return criteria;
		
	}
	private void addBudgetDetailCriteria(Map<String, Object> map,Criteria criteria) {
		for (Entry<String,Object> criterion : map.entrySet()) {
			if(isIdPresent(criterion.getValue()))
				criteria.createCriteria(criterion.getKey()).add(Restrictions.idEq(criterion.getValue()));
		}
	}

	private void addBudgetDetailCriteriaIncudingNullRestrictions(Map<String, Object> map,Criteria criteria) {
		for (Entry<String,Object> criterion : map.entrySet()) {
			if(isIdPresent(criterion.getValue()))
				criteria.createCriteria(criterion.getKey()).add(Restrictions.idEq(criterion.getValue()));
			else
				criteria.add(Restrictions.isNull(criterion.getKey()));
		}
	}
	protected boolean isIdPresent(Object value) {
		return Long.valueOf(value.toString())!=0l && Long.valueOf(value.toString())!=-1;
	}

	public BudgetDetail persist(BudgetDetail detail) {
		checkForDuplicates(detail);
		return super.persist(detail);
	}

	public void checkForDuplicates(BudgetDetail detail) {
		Criteria criteria = getSession().createCriteria(BudgetDetail.class);
		Map<String, Object> map = new HashMap<String, Object>();
		addCriteriaExcludingBudget(detail, map);
		addBudgetDetailCriteriaIncudingNullRestrictions(map, criteria);
		if(detail.getBudget()==null || detail.getBudget().getId()==null || detail.getBudget().getId()==0 || detail.getBudget().getId()==-1) 
			return;
		//add restriction to check if budgetdetail with is combination exists in the current year within a tree
		Budget root = getRootFor(detail.getBudget());
		criteria.createCriteria(Constants.BUDGET).add(Restrictions.eq("materializedPath", root==null?"":root.getMaterializedPath()));
		List<BudgetDetail> existingDetails = criteria.list();
		if(!existingDetails.isEmpty() && !existingDetails.get(0).getId().equals(detail.getId())) 
			throw new ValidationException(Arrays.asList(new ValidationError("budgetDetail.duplicate","budgetdetail.exists")));
	}
	
	private Budget getRootFor(Budget budget) {
		if(budget == null || StringUtils.isBlank(budget.getMaterializedPath())) return null;
		if(budget.getMaterializedPath().length()==1) return budget;
		return (Budget) persistenceService.find("from Budget where materializedPath=?",budget.getMaterializedPath().split("\\.")[0]);
	}

	protected UserImpl getUser() {
		return (UserImpl) ((PersistenceService)this).find(" from UserImpl where id=?",Integer.parseInt(EGOVThreadLocals.getUserId()));
	} 
	
	public Position getPositionForEmployee(PersonalInformation emp)throws EGOVRuntimeException{
		return employeeService.getPositionforEmp(emp.getIdPersonalInformation());
	}
	
	/**
	 * 
	 * @param detail
	 * @return department of the budgetdetail
	 * @throws EGOVRuntimeException
	 */
	public DepartmentImpl getDepartmentForBudget(BudgetDetail detail)throws EGOVRuntimeException
	{	
		DepartmentImpl dept=null;
		if(detail.getExecutingDepartment()!=null){
			dept=detail.getExecutingDepartment();
		}else{
			throw new EGOVRuntimeException("Department not found for the Budget"+detail.getId());
		}
		return dept;
	}
	
	/**
	 * returns department  of the employee from assignment for the current date
	 * @param emp
	 * @return
	 */
	public DepartmentImpl depertmentForEmployee(PersonalInformation emp)
	{
		Department dept=null;
		Date currDate=new Date();
		try {
			Assignment empAssignment = employeeService.getAssignmentByEmpAndDate(
					currDate, emp.getIdPersonalInformation());
			dept=empAssignment.getDeptId();
			return (DepartmentImpl)dept;
		}catch(NullPointerException ne)
		{
			throw new EGOVRuntimeException(ne.getMessage());
		}
		catch (Exception e) {
			throw new EGOVRuntimeException("Error while getting Department fort the employee"+emp.getEmployeeFirstName());
		}
		
		
	}

	public List<BudgetDetail> getRemainingDetailsForApproveOrReject(Budget budget) {
		Criteria criteria = getSession().createCriteria(BudgetDetail.class);
		//criteria.createCriteria("materializedPath", "state").add(Restrictions.eq("state.value","NEW"));	
		criteria.createCriteria(Constants.BUDGET,Constants.BUDGET).add(Restrictions.eq("budget.id",budget.getId()));
		return criteria.list();
	
	}
	public List<BudgetDetail> getRemainingDetailsForSave(
			Budget budget,Position currPos) {
		Criteria criteria = getSession().createCriteria(BudgetDetail.class);
		criteria.createCriteria(Constants.STATE, Constants.STATE).add(Restrictions.eq("state.owner",currPos));	
		criteria.createCriteria(Constants.BUDGET,Constants.BUDGET).add(Restrictions.eq("budget.id",budget.getId()));
		return criteria.list();
	
	}

	public void setRelatedEntitesOn(BudgetDetail detail,PersistenceService service) {
		if(detail.getBudget() != null){
			detail.setBudget((Budget)service.find("from Budget where id=?", detail.getBudget().getId()));
		    addMaterializedPath(detail);
		}
		if(detail.getFunction() != null)
			detail.setFunction((CFunction)service.find("from CFunction where id=?", detail.getFunction().getId()));
		if(detail.getFunctionary() != null)
			detail.setFunctionary((Functionary)service.find("from Functionary where id=?", detail.getFunctionary().getId()));
		if(detail.getExecutingDepartment() != null)
			detail.setExecutingDepartment((DepartmentImpl) service.find("from DepartmentImpl where ID_DEPT=?",detail.getExecutingDepartment().getId()));
		if(detail.getScheme() != null)
			detail.setScheme((Scheme)service.find("from Scheme where id=?", detail.getScheme().getId()));
		if(detail.getSubScheme() != null)
			detail.setSubScheme((SubScheme)service.find("from SubScheme where id=?", detail.getSubScheme().getId()));
		if(detail.getFund() != null)
			detail.setFund((Fund)service.find("from Fund where id=?", detail.getFund().getId()));
		if(detail.getBudgetGroup() != null)
			detail.setBudgetGroup((BudgetGroup)service.find("from BudgetGroup where id=?", detail.getBudgetGroup().getId()));
		if(detail.getBoundary() != null)
			detail.setBoundary((BoundaryImpl)service.find("from BoundaryImpl where id=?", detail.getBoundary().getId()));
	}
	
	private void addMaterializedPath(BudgetDetail detail){
		String materializedPath="";
		String count="";
		if(detail.getBudget()!=null){
			materializedPath=detail.getBudget().getMaterializedPath();
			List<BudgetDetail> parallelBudgetDetails=findAllBy("from BudgetDetail bd where bd.budget=?",detail.getBudget());
			if(parallelBudgetDetails!=null)
				count = String.valueOf(parallelBudgetDetails.size()+1);
			if(materializedPath!=null && !materializedPath.isEmpty())
				materializedPath=materializedPath+"."+count;
			detail.setMaterializedPath(materializedPath);
		}
	}

	public void transitionToEnd(BudgetDetail detail, Position position) {
		budgetDetailWorkflowService.end(detail, position);
	}
	
	public List<Object[]> fetchActualsForFY(String fromDate,String toVoucherDate,List<String> mandatoryFields) {
		List<AppConfigValues> list = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(Constants.EGF,"exclude_status_forbudget_actual");
		if(list.isEmpty())
			throw new ValidationException("","exclude_status_forbudget_actual is not defined in AppConfig");
		StringBuffer miscQuery = getMiscQuery(mandatoryFields,"vmis","gl","vh");
		StringBuffer budgetGroupQuery = new StringBuffer();
		budgetGroupQuery.append(" (select bg1.id as id,bg1.accounttype as accounttype,decode(c1.glcode,null,-1,to_number(c1.glcode)) " +
				"as mincode,decode(c2.glcode,null,999999999,c2.glcode) as maxcode,decode(c3.glcode,null,-1,to_number(c3.glcode)) as majorcode " +
				"from egf_budgetgroup bg1 left outer join chartofaccounts c1 on c1.id=bg1.mincode left outer join chartofaccounts c2 on " +
				"c2.id=bg1.maxcode left outer join chartofaccounts  c3 on c3.id=bg1.majorcode ) bg ");
		String voucherstatusExclude = ((AppConfigValues)list.get(0)).getValue();
		StringBuffer query = new StringBuffer();
		query = query.append("select bd.id,SUM(gl.debitAmount)-SUM(gl.creditAmount) from egf_budgetdetail bd,generalledger gl,voucherheader vh," +
				"vouchermis vmis,"+budgetGroupQuery+",egf_budget b where bd.budget=b.id and vmis.VOUCHERHEADERID=vh.id and gl.VOUCHERHEADERID=vh.id and bd.budgetgroup=bg.id and " +
				"(bg.ACCOUNTTYPE='REVENUE_EXPENDITURE' or bg.ACCOUNTTYPE='CAPITAL_EXPENDITURE') and vh.status not in ("+voucherstatusExclude+") and " +
						"vh.voucherDate>= to_date('"+fromDate+"','dd/MM/yyyy') and vh.voucherDate <= to_date("+toVoucherDate+",'dd/MM/yyyy') "+miscQuery+" and ((gl.glcode between bg.mincode and bg.maxcode) or gl.glcode=bg.majorcode) group by bd.id"+
				" union "+
				"select bd.id,SUM(gl.creditAmount)-SUM(gl.debitAmount) from egf_budgetdetail bd,generalledger gl,voucherheader vh," +
				"vouchermis vmis,"+budgetGroupQuery+",egf_budget b where bd.budget=b.id and vmis.VOUCHERHEADERID=vh.id and gl.VOUCHERHEADERID=vh.id and bd.budgetgroup=bg.id and " +
				"(bg.ACCOUNTTYPE='REVENUE_RECEIPTS' or bg.ACCOUNTTYPE='CAPITAL_RECEIPTS') and vh.status not in ("+voucherstatusExclude+") and " +
						"vh.voucherDate>= to_date('"+fromDate+"','dd/MM/yyyy') and vh.voucherDate <= to_date("+toVoucherDate+",'dd/MM/yyyy') "+miscQuery+" and ((gl.glcode between bg.mincode and bg.maxcode) or gl.glcode=bg.majorcode) group by bd.id");
		List<Object[]> result = getSession().createSQLQuery(query.toString()).list();
		return result;
	}

	public List<Object[]> fetchActualsForBill(String fromDate,String toVoucherDate,List<String> mandatoryFields) {
		StringBuffer miscQuery = getMiscQuery(mandatoryFields,"bmis","bdetail","bmis");
		StringBuffer query = new StringBuffer();
		query = query.append("select bd.id,SUM(decode(bdetail.debitAmount,null,0,bdetail.debitAmount))-SUM(decode(bdetail.creditAmount,null,0,bdetail.creditAmount)) from egf_budgetdetail bd,eg_billdetails bdetail, eg_billregistermis bmis, eg_billregister br," +
				"egf_budgetgroup bg where bmis.billid=br.id and bdetail.billid=br.id and bd.budgetgroup=bg.id and " +
				"(bg.ACCOUNTTYPE='REVENUE_EXPENDITURE' or bg.ACCOUNTTYPE='CAPITAL_EXPENDITURE') and br.billstatus != 'Cancelled'  and " +
				"bmis.voucherheaderid is null and br.billdate>=to_date('"+fromDate+"','dd/MM/yyyy') and br.billdate <= to_date("+toVoucherDate+",'dd/MM/yyyy') "+miscQuery+" and " +
				" (bmis.budgetCheckReq is null or bmis.budgetCheckReq='1') and "+
				"((bdetail.glcodeid between bg.mincode and bg.maxcode) or bdetail.glcodeid=bg.majorcode) group by bd.id"+
				" union "+
				"select bd.id,SUM(decode(bdetail.creditAmount,null,0,bdetail.creditAmount))-SUM(decode(bdetail.debitAmount,null,0,bdetail.debitAmount)) from egf_budgetdetail bd,eg_billdetails bdetail, eg_billregistermis bmis, eg_billregister br," +
				"egf_budgetgroup bg where bmis.billid=br.id and bdetail.billid=br.id and bd.budgetgroup=bg.id and " +
				" (bmis.budgetCheckReq is null or bmis.budgetCheckReq='1') and "+
				"(bg.ACCOUNTTYPE='REVENUE_RECEIPTS' or bg.ACCOUNTTYPE='CAPITAL_RECEIPTS') and br.billstatus != 'Cancelled'  and bmis.voucherheaderid " +
				"is null and br.billdate>= to_date('"+fromDate+"','dd/MM/yyyy') and br.billdate <= to_date("+toVoucherDate+",'dd/MM/yyyy') "+miscQuery+" and ((bdetail.glcodeid between bg.mincode " +
				"and bg.maxcode) or bdetail.glcodeid=bg.majorcode) group by bd.id");
		List<Object[]> result = getSession().createSQLQuery(query.toString()).list();
		return result;
	}
	public List<Object[]> fetchActualsForFYWithParams(String fromDate,String toVoucherDate,List<String> mandatoryFields,Map<String,Integer> queryParamMap) {
		if(queryParamMap!=null  && queryParamMap.size()!=0)
		{
			List<AppConfigValues> list = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(Constants.EGF,"exclude_status_forbudget_actual");
			if(list.isEmpty())
				throw new ValidationException("","exclude_status_forbudget_actual is not defined in AppConfig");
			StringBuffer miscQuery = getParamMiscQuery(mandatoryFields,"vmis","gl","vh",queryParamMap);
			StringBuffer budgetGroupQuery = new StringBuffer();
			budgetGroupQuery.append(" (select bg1.id as id,bg1.accounttype as accounttype,decode(c1.glcode,null,-1,to_number(c1.glcode)) " +
					"as mincode,decode(c2.glcode,null,999999999,c2.glcode) as maxcode,decode(c3.glcode,null,-1,to_number(c3.glcode)) as majorcode " +
					"from egf_budgetgroup bg1 left outer join chartofaccounts c1 on c1.id=bg1.mincode left outer join chartofaccounts c2 on " +
			"c2.id=bg1.maxcode left outer join chartofaccounts  c3 on c3.id=bg1.majorcode ) bg ");
			String voucherstatusExclude = ((AppConfigValues)list.get(0)).getValue();
			StringBuffer query = new StringBuffer();
			query = query.append("select bd.id,SUM(gl.debitAmount)-SUM(gl.creditAmount) from egf_budgetdetail bd,generalledger gl,voucherheader vh," +
					"vouchermis vmis,"+budgetGroupQuery+",egf_budget b where bd.budget=b.id and vmis.VOUCHERHEADERID=vh.id and gl.VOUCHERHEADERID=vh.id and bd.budgetgroup=bg.id and " +
					"(bg.ACCOUNTTYPE='REVENUE_EXPENDITURE' or bg.ACCOUNTTYPE='CAPITAL_EXPENDITURE') and vh.status not in ("+voucherstatusExclude+") and " +
					"vh.voucherDate>= to_date('"+fromDate+"','dd/MM/yyyy') and vh.voucherDate <= to_date("+toVoucherDate+",'dd/MM/yyyy') "+miscQuery+" and ((gl.glcode between bg.mincode and bg.maxcode) or gl.glcode=bg.majorcode) group by bd.id"+
						" union "+
						"select bd.id,SUM(gl.creditAmount)-SUM(gl.debitAmount) from egf_budgetdetail bd,generalledger gl,voucherheader vh," +
						"vouchermis vmis,"+budgetGroupQuery+",egf_budget b where bd.budget=b.id and vmis.VOUCHERHEADERID=vh.id and gl.VOUCHERHEADERID=vh.id and bd.budgetgroup=bg.id and " +
						"(bg.ACCOUNTTYPE='REVENUE_RECEIPTS' or bg.ACCOUNTTYPE='CAPITAL_RECEIPTS') and vh.status not in ("+voucherstatusExclude+") and " +
						"vh.voucherDate>= to_date('"+fromDate+"','dd/MM/yyyy') and vh.voucherDate <= to_date("+toVoucherDate+",'dd/MM/yyyy') "+miscQuery+" and ((gl.glcode between bg.mincode and bg.maxcode) or gl.glcode=bg.majorcode) group by bd.id");
			List<Object[]> result = getSession().createSQLQuery(query.toString()).list();
			return result;
		}
		else 
			return null;
	}
	public List<Object[]> fetchActualsForBillWithParams(String fromDate,String toVoucherDate,List<String> mandatoryFields,Map<String,Integer> queryParamMap) {
		if(queryParamMap!=null  && queryParamMap.size()!=0)
		{
			StringBuffer miscQuery = getParamMiscQuery(mandatoryFields,"bmis","bdetail","bmis",queryParamMap);
			StringBuffer query = new StringBuffer();
			query = query.append("select bd.id,SUM(decode(bdetail.debitAmount,null,0,bdetail.debitAmount))-SUM(decode(bdetail.creditAmount,null,0,bdetail.creditAmount)) from egf_budgetdetail bd,eg_billdetails bdetail, eg_billregistermis bmis, eg_billregister br," +
					"egf_budgetgroup bg where bmis.billid=br.id and bdetail.billid=br.id and bd.budgetgroup=bg.id and " +
					"(bg.ACCOUNTTYPE='REVENUE_EXPENDITURE' or bg.ACCOUNTTYPE='CAPITAL_EXPENDITURE') and br.statusid not in (select id from egw_status where description='Cancelled' and moduletype in ('EXPENSEBILL', 'SALBILL', 'WORKSBILL', 'PURCHBILL', 'CBILL', 'SBILL', 'CONTRACTORBILL'))  and " +
					"bmis.voucherheaderid is null and br.billdate>=to_date('"+fromDate+"','dd/MM/yyyy') and br.billdate <= to_date("+toVoucherDate+",'dd/MM/yyyy') "+miscQuery+" and " +
					" (bmis.budgetCheckReq is null or bmis.budgetCheckReq='1') and "+
					"((bdetail.glcodeid between bg.mincode and bg.maxcode) or bdetail.glcodeid=bg.majorcode) group by bd.id"+
					" union "+
					"select bd.id,SUM(decode(bdetail.creditAmount,null,0,bdetail.creditAmount))-SUM(decode(bdetail.debitAmount,null,0,bdetail.debitAmount)) from egf_budgetdetail bd,eg_billdetails bdetail, eg_billregistermis bmis, eg_billregister br," +
					"egf_budgetgroup bg where bmis.billid=br.id and bdetail.billid=br.id and bd.budgetgroup=bg.id and " +
					" (bmis.budgetCheckReq is null or bmis.budgetCheckReq='1') and "+
					"(bg.ACCOUNTTYPE='REVENUE_RECEIPTS' or bg.ACCOUNTTYPE='CAPITAL_RECEIPTS') and br.statusid not in (select id from egw_status where description='Cancelled' and moduletype in ('EXPENSEBILL', 'SALBILL', 'WORKSBILL', 'PURCHBILL', 'CBILL', 'SBILL', 'CONTRACTORBILL'))  and bmis.voucherheaderid " +
					"is null and br.billdate>= to_date('"+fromDate+"','dd/MM/yyyy') and br.billdate <= to_date("+toVoucherDate+",'dd/MM/yyyy') "+miscQuery+" and ((bdetail.glcodeid between bg.mincode " +
			"and bg.maxcode) or bdetail.glcodeid=bg.majorcode) group by bd.id");
			List<Object[]> result = getSession().createSQLQuery(query.toString()).list();
			return result;
		}
		else 
			return null;
	}


	private StringBuffer getMiscQuery(List<String> mandatoryFields,String mis,String gl,String detail) {
		StringBuffer miscQuery = new StringBuffer();
		if(mandatoryFields.contains(Constants.FIELD)){
			miscQuery = miscQuery.append(" and "+mis+".divisionid=bd.boundary ");
		}
		if(mandatoryFields.contains(Constants.FUND)){
			miscQuery = miscQuery.append(" and "+detail+".fundId=bd.fund ");
		}
		if(mandatoryFields.contains(Constants.SCHEME)){
			miscQuery = miscQuery.append(" and "+mis+".schemeid=bd.scheme ");
		}
		if(mandatoryFields.contains(Constants.SUB_SCHEME)){
			miscQuery = miscQuery.append(" and "+mis+".subschemeid=bd.subscheme ");
		}
		if(mandatoryFields.contains(Constants.FUNCTIONARY)){
			miscQuery = miscQuery.append(" and "+mis+".functionaryid=bd.functionary ");
		}
		if(mandatoryFields.contains(Constants.FUNCTION)){
			miscQuery = miscQuery.append(" and "+gl+".functionId=bd.function ");
		}
		if(mandatoryFields.contains(Constants.EXECUTING_DEPARTMENT)){
			miscQuery = miscQuery.append(" and "+mis+".departmentid=bd.executing_department ");
		}
		return miscQuery;
	}
	private StringBuffer getParamMiscQuery(List<String> mandatoryFields,String mis,String gl,String detail,Map<String,Integer> queryParamMap) {
		StringBuffer miscQuery = new StringBuffer();
		if(mandatoryFields.contains(Constants.FIELD)){
			miscQuery = miscQuery.append(" and "+mis+".divisionid=bd.boundary ");
		}
		if(mandatoryFields.contains(Constants.FUND)){
			miscQuery = miscQuery.append(" and "+detail+".fundId=bd.fund ");
			if(queryParamMap.containsKey("fundId"))
				miscQuery = miscQuery.append(" and bd.fund= "+queryParamMap.get("fundId"));
		}
		if(mandatoryFields.contains(Constants.SCHEME)){
			miscQuery = miscQuery.append(" and "+mis+".schemeid=bd.scheme ");
		}
		if(mandatoryFields.contains(Constants.SUB_SCHEME)){
			miscQuery = miscQuery.append(" and "+mis+".subschemeid=bd.subscheme ");
		}
		if(mandatoryFields.contains(Constants.FUNCTIONARY)){
			miscQuery = miscQuery.append(" and "+mis+".functionaryid=bd.functionary ");
		}
		if(mandatoryFields.contains(Constants.FUNCTION)){
			miscQuery = miscQuery.append(" and "+gl+".functionId=bd.function ");
			if(queryParamMap.containsKey("functionId"))
				miscQuery = miscQuery.append(" and bd.function= "+Long.parseLong(queryParamMap.get("functionId").toString()));
		}
		if(mandatoryFields.contains(Constants.EXECUTING_DEPARTMENT)){
			miscQuery = miscQuery.append(" and "+mis+".departmentid=bd.executing_department ");
			if(queryParamMap.containsKey("deptId"))
				miscQuery = miscQuery.append(" and bd.executing_department= "+queryParamMap.get("deptId"));

		}
		return miscQuery;
	}
	
	public void setGenericDao(GenericHibernateDaoFactory genericDao) {
		this.genericDao = genericDao;
	}
	public PersonalInformation getEmpForCurrentUser()
	{
		return employeeService.getEmpForUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
	}
	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
}

