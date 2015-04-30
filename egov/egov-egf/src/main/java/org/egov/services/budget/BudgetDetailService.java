/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency, 
 *    accountability and the service delivery of the government  organizations.
 * 
 *     Copyright (C) <2015>  eGovernments Foundation
 * 
 *     The updated version of eGov suite of products as by eGovernments Foundation 
 *     is available at http://www.egovernments.org
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or 
 *     http://www.gnu.org/licenses/gpl.html .
 * 
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 * 
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.services.budget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.script.ScriptContext;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.eis.service.EisCommonService;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.workflow.service.WorkflowService;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.models.Script;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.ScriptService;
import org.egov.model.budget.Budget;
import org.egov.model.budget.BudgetDetail;
import org.egov.model.budget.BudgetGroup;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.commons.Position;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.PersonalInformation;
import org.egov.utils.Constants;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;

//This fix is for Phoenix Migration.
public class BudgetDetailService extends PersistenceService<BudgetDetail, Long>{
    protected EisCommonService eisCommonService;
    protected WorkflowService<BudgetDetail> budgetDetailWorkflowService;
    private GenericHibernateDaoFactory genericDao;
    private ScriptService scriptExecutionService;
    PersistenceService persistenceService;
    private static final Logger LOGGER=Logger.getLogger(BudgetDetailService.class);


    public boolean canViewApprovedAmount(PersistenceService persistenceService, Budget budget){
        Script script = (Script) persistenceService.findAllByNamedQuery(Script.BY_NAME, "budget.report.view.access").get(0);
        ScriptContext context = ScriptService.createContext("wfItem",budget,"eisCommonServiceBean", eisCommonService,"userId",EGOVThreadLocals.getUserId().intValue());
        Integer result = (Integer)scriptExecutionService.executeScript(script, context);
        if(result == 1)
            return true;
        return false;
    }

    public BudgetDetail createBudgetDetail(BudgetDetail detail,Position position,PersistenceService service){
        try {
            setRelatedEntitesOn(detail, service);
           // chequeUnique(detail,service);
            return detail;
        }
        catch (ConstraintViolationException e) {
            throw new ValidationException(Arrays.asList(new ValidationError("budgetDetail.duplicate","budgetdetail.exists")));
        }
    }
    /**
     *
     * @param detail
     * @param service
     * As of now it validates only department selected is of same as budget by checking the name of budget
     * So budget should be named as A-BE-Rev-2013-14  where A is department accountCentral cell
     * This format is necessary to check
     * later we can add dept into budget and check it
     */
    private void chequeUnique(BudgetDetail detail, PersistenceService service) {

        String name = detail.getBudget().getName();
        if(name!=null)
        {
        
        	
        	if(!name.split("-")[0].equalsIgnoreCase(detail.getExecutingDepartment().getCode()))
                throw new ValidationException(Arrays.asList(new ValidationError("budgetDetail.duplicate.department","Creating detail with different department")));

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
            Criteria criteria =null;//HibernateUtil.getCurrentSession().createCriteria(BudgetDetail.class);
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
        if(LOGGER.isDebugEnabled())     LOGGER.debug("Starting findAllBudgetDetailsWithReAppropriation...");
        List<BudgetDetail> budgetDetails = findAllBudgetDetailsFor(budget,example);
        if(LOGGER.isDebugEnabled())     LOGGER.debug("Done findAllBudgetDetailsWithReAppropriation.");
        return budgetDetails;
    }

    private Budget findBudget(Budget budget) {
        return (Budget)null;//HibernateUtil.getCurrentSession().load(Budget.class, budget.getId());
    }


    public List<Budget> findBudgetsForFY(Long financialYear) {
        Criteria criteria =null;//HibernateUtil.getCurrentSession().createCriteria(Budget.class);
        return criteria.add(Restrictions.eq("financialYear.id", financialYear)).add(Restrictions.eq("isActiveBudget", true)).list();
    }

    public List<Budget> findApprovedBudgetsForFY(Long financialYear) {
        if(LOGGER.isDebugEnabled())     LOGGER.debug("starting findApprovedBudgetsForFY...");
        Criteria criteria =null;//HibernateUtil.getCurrentSession().createCriteria(Budget.class);
        return criteria.add(Restrictions.eq("financialYear.id", financialYear)).add(Restrictions.eq("isActiveBudget", true))
            .addOrder(Property.forName("name").asc())
            .createCriteria(Constants.STATE, Constants.STATE).add(Restrictions.eq("state.value","END"))
            .list();
    }

    public List<Budget> findBudgetsForFYWithNewState(Long financialYear) {
        Criteria criteria =null;//HibernateUtil.getCurrentSession().createCriteria(Budget.class);
        criteria.createCriteria(Constants.STATE, Constants.STATE).add(Restrictions.eq("state.value","NEW"));
        return criteria.add(Restrictions.eq("financialYear.id", financialYear)).add(Restrictions.eq("isActiveBudget", true)).list();
    }

    public List<Budget> findPrimaryBudgetForFY(Long financialYear) {
        Criteria criteria =null;//HibernateUtil.getCurrentSession().createCriteria(Budget.class);
        return criteria.add(Restrictions.eq("financialYear.id", financialYear)).add(Restrictions.eq("isActiveBudget", true)).add(Restrictions.eq("isPrimaryBudget", true)).add(Restrictions.isNull("parent")).list();
    }

    public Budget findApprovedPrimaryParentBudgetForFY(Long financialYear) {
        Criteria criteria =null;//HibernateUtil.getCurrentSession().createCriteria(Budget.class);
        List<Budget> budgetList = criteria.add(Restrictions.eq("financialYear.id", financialYear)).add(Restrictions.eq("isbere", "RE"))
.add(Restrictions.eq("isActiveBudget", true)).add(Restrictions.eq("isPrimaryBudget", true))
.add(Restrictions.isNull("parent"))
.addOrder(Property.forName("name").asc())
                                    .createCriteria(Constants.STATE, Constants.STATE).add(Restrictions.eq("state.value","END"))
                                    .list();
        if(budgetList.isEmpty()){
            Criteria c =null;//HibernateUtil.getCurrentSession().createCriteria(Budget.class);
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
        Criteria criteria =null;//HibernateUtil.getCurrentSession().createCriteria(BudgetDetail.class);
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
    	try {
    		detail.setUniqueNo(detail.getFund().getId() + "-" + detail.getExecutingDepartment().getId() + "-"
    				+ detail.getFunction().getId() + "-" + detail.getBudgetGroup().getId());	
    	checkForDuplicates(detail);
    	return super.persist(detail);
    	} catch (Exception e) {
    	throw new ValidationException(Arrays.asList(new ValidationError("budgetDetail.duplicate","budgetdetail.exists")));
    	}
    	}

    public void checkForDuplicates(BudgetDetail detail) {
        Criteria criteria =null;//HibernateUtil.getCurrentSession().createCriteria(BudgetDetail.class);
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

    protected User getUser() {
        return (User) ((PersistenceService)this).find(" from User where id=?",EGOVThreadLocals.getUserId());
    }

    public Position getPositionForEmployee(PersonalInformation emp)throws EGOVRuntimeException{
        return eisCommonService.getPrimaryAssignmentPositionForEmp(emp.getIdPersonalInformation());
    }

    public void setEisCommonService(EisCommonService eisCommonService) {
        this.eisCommonService = eisCommonService;
    }

    /**
     *
     * @param detail
     * @return department of the budgetdetail
     * @throws EGOVRuntimeException
     */
    public Department getDepartmentForBudget(BudgetDetail detail)throws EGOVRuntimeException
    {
        Department dept=null;
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
    public Department depertmentForEmployee(PersonalInformation emp)
    {
        Department dept=null;
        Date currDate=new Date();
        try {
            Assignment empAssignment = eisCommonService.getLatestAssignmentForEmployeeByToDate( emp.getIdPersonalInformation(),currDate);
            dept=empAssignment.getDeptId();
            return (Department)dept;
        }catch(NullPointerException ne)
        {
            throw new EGOVRuntimeException(ne.getMessage());
        }
        catch (Exception e) {
            throw new EGOVRuntimeException("Error while getting Department fort the employee"+emp.getEmployeeFirstName());
        }


    }

    public List<BudgetDetail> getRemainingDetailsForApproveOrReject(Budget budget) {
        Criteria criteria =null;//HibernateUtil.getCurrentSession().createCriteria(BudgetDetail.class);
        //criteria.createCriteria("materializedPath", "state").add(Restrictions.eq("state.value","NEW"));
criteria.createCriteria(Constants.BUDGET,Constants.BUDGET).add(Restrictions.eq("budget.id",budget.getId()));
        return criteria.list();

    }
    public List<BudgetDetail> getRemainingDetailsForSave(
            Budget budget,Position currPos) {
        Criteria criteria =null;//HibernateUtil.getCurrentSession().createCriteria(BudgetDetail.class);
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
            detail.setExecutingDepartment((Department) service.find("from Department where ID_DEPT=?",detail.getExecutingDepartment().getId()));
        if(detail.getScheme() != null)
            detail.setScheme((Scheme)service.find("from Scheme where id=?", detail.getScheme().getId()));
        if(detail.getSubScheme() != null)
            detail.setSubScheme((SubScheme)service.find("from SubScheme where id=?", detail.getSubScheme().getId()));
        if(detail.getFund() != null)
            detail.setFund((Fund)service.find("from Fund where id=?", detail.getFund().getId()));
        if(detail.getBudgetGroup() != null)
            detail.setBudgetGroup((BudgetGroup)service.find("from BudgetGroup where id=?", detail.getBudgetGroup().getId()));
        if(detail.getBoundary() != null)
            detail.setBoundary((Boundary)service.find("from Boundary where id=?", detail.getBoundary().getId()));
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
       // budgetDetailWorkflowService.end(detail, position);
    }

    public List<Object[]> fetchActualsForFYDate(String fromDate,String toVoucherDate,List<String> mandatoryFields) {
        if(LOGGER.isDebugEnabled())     LOGGER.debug("Starting fetchActualsForFY"+fromDate );
        List<AppConfigValues> list = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(Constants.EGF,"exclude_status_forbudget_actual");
        if(list.isEmpty())
            throw new ValidationException("","exclude_status_forbudget_actual is not defined in AppConfig");
        StringBuffer miscQuery = getMiscQuery(mandatoryFields,"vmis","gl","vh");
        StringBuffer budgetGroupQuery = new StringBuffer();
        budgetGroupQuery.append(" (select bg1.id as id,bg1.accounttype as accounttype,decode(c1.glcode,null,-1,to_number(c1.glcode)) " +
                "as mincode,decode(c2.glcode,null,999999999,c2.glcode) as maxcode,decode(c3.glcode,null,-1,to_number(c3.glcode)) as majorcode " +
                "from egf_budgetgroup bg1 left outer join chartofaccounts c1 on c1.id=bg1.mincode left outer join chartofaccounts c2 on " +
                "c2.id=bg1.maxcode left outer join chartofaccounts c3 on c3.id=bg1.majorcode ) bg ");
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
        List<Object[]> result =null;//HibernateUtil.getCurrentSession().createSQLQuery(query.toString()).list();
        if(LOGGER.isDebugEnabled())     LOGGER.debug("Finished fetchActualsForFY"+fromDate );
        return result;
    }


    /**
     * vouchers are of the passed finaicial year
     * budget is of passed topBudgets financialyear
     * @param fy
     * @param mandatoryFields
     * @param topBudget
     * @param referingTopBudget
     * @param date
     * @param dept
     * @param fun
     * @param excludelist TODO
     * @return
     */

    public List<Object[]> fetchActualsForFY(CFinancialYear fy,List<String> mandatoryFields,Budget topBudget, Budget referingTopBudget, Date date,Integer dept,Long fun) {
        if(LOGGER.isInfoEnabled()) LOGGER.info("==============================================================================================" );
        if(LOGGER.isInfoEnabled())     LOGGER.info("Starting fetchActualsForFY"+fy.getStartingDate().getYear()+"-"+fy.getEndingDate().getYear() );
        String dateCondition = "";
        if(date != null){
            dateCondition = " AND vh.voucherdate <='"+Constants.DDMMYYYYFORMAT1.format(date)+"' ";
        }
        List<AppConfigValues> list = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(Constants.EGF,"exclude_status_forbudget_actual");
        if(list.isEmpty())
            throw new ValidationException("","exclude_status_forbudget_actual is not defined in AppConfig");
        StringBuffer miscQuery = getMiscQuery(mandatoryFields,"vmis","gl","vh");
        if(dept!=null)
        {
            miscQuery.append(" and bd.executing_department="+dept);
        }
        if(fun!=null)
        {
            miscQuery=miscQuery.append(" AND bd.function="+fun);
        }
        StringBuffer referingUniqueNoQry=new StringBuffer(200);
        referingUniqueNoQry.append(" ");
        if(referingTopBudget!=null)
            referingUniqueNoQry.append(" and bd.uniqueno in (select uniqueno from egf_budgetdetail where MATERIALIZEDPATH like '"+referingTopBudget.getMaterializedPath()+"%'  )");


        StringBuffer budgetGroupQuery = new StringBuffer();
        budgetGroupQuery.append(" (select bg1.id as id,bg1.accounttype as accounttype,decode(c1.glcode,null,-1,to_number(c1.glcode)) " +
                "as mincode,decode(c2.glcode,null,999999999,c2.glcode) as maxcode,decode(c3.glcode,null,-1,to_number(c3.glcode)) as majorcode " +
                "from egf_budgetgroup bg1 left outer join chartofaccounts c1 on c1.id=bg1.mincode left outer join chartofaccounts c2 on " +
                "c2.id=bg1.maxcode left outer join chartofaccounts c3 on c3.id=bg1.majorcode ) bg ");
        String voucherstatusExclude = ((AppConfigValues)list.get(0)).getValue();
        StringBuffer query = new StringBuffer();
        /*query = query.append("select bd.uniqueno,SUM(gl.debitAmount)-SUM(gl.creditAmount) from egf_budgetdetail bd," +
                "vouchermis vmis,"+budgetGroupQuery+",egf_budget b,financialyear f,fiscalperiod p,voucherheader vh,generalledger gl " +
                        "where bd.budget=b.id and p.financialyearid=f.id and f.id="+fy.getId()+" and vh.fiscalperiodid=p.id "+dateCondition+" and " +
                        " b.financialyearid="+topBudget.getFinancialYear().getId()+" and b.MATERIALIZEDPATH like '"+topBudget.getMaterializedPath()+"%' " +referingUniqueNoQry.toString()+
                        " and  vmis.VOUCHERHEADERID=vh.id and gl.VOUCHERHEADERID=vh.id " +
                        " and bd.budgetgroup=bg.id and (bg.ACCOUNTTYPE='REVENUE_EXPENDITURE' or bg.ACCOUNTTYPE='CAPITAL_EXPENDITURE')" +
                        " and vh.status not in ("+voucherstatusExclude+")  " +miscQuery+" " +
                        " and ((gl.glcode between bg.mincode and bg.maxcode) or gl.glcode=bg.majorcode) and bg.mincode!=bg.maxcode group by bd.uniqueno"+
                " union "+
                "select bd.uniqueno,SUM(gl.creditAmount)-SUM(gl.debitAmount) from egf_budgetdetail bd,generalledger gl,voucherheader vh," +
                "vouchermis vmis,"+budgetGroupQuery+",egf_budget b,financialyear f,fiscalperiod p where bd.budget=b.id " +
                        "and p.financialyearid=f.id and f.id="+fy.getId()+" and vh.fiscalperiodid=p.id "+dateCondition+" and b.financialyearid="+topBudget.getFinancialYear().getId()+
                        " and b.MATERIALIZEDPATH like '"+topBudget.getMaterializedPath()+"%' and vmis.VOUCHERHEADERID=vh.id and gl.VOUCHERHEADERID=vh.id " +
                        referingUniqueNoQry.toString()+
                        " and bd.budgetgroup=bg.id and (bg.ACCOUNTTYPE='REVENUE_RECEIPTS' or bg.ACCOUNTTYPE='CAPITAL_RECEIPTS')" +
                        " and vh.status not in ("+voucherstatusExclude+") " +miscQuery+" and ((gl.glcode between bg.mincode and bg.maxcode)" +
                        " or gl.glcode=bg.majorcode) and bg.mincode!=bg.maxcode group by bd.uniqueno");
        */


        query = query.append("  select bd.uniqueno,SUM(gl.debitAmount)-SUM(gl.creditAmount) from egf_budgetdetail bd," +
                "vouchermis vmis,egf_budgetgroup bg,egf_budget b,financialyear f,fiscalperiod p,voucherheader vh,generalledger gl " +
                        "where bd.budget=b.id and p.financialyearid=f.id and f.id="+fy.getId()+" and vh.fiscalperiodid=p.id "+dateCondition+" and " +
                        " b.financialyearid="+topBudget.getFinancialYear().getId()+" and b.MATERIALIZEDPATH like '"+topBudget.getMaterializedPath()+"%' " +
                        referingUniqueNoQry.toString()+
                        " and  vmis.VOUCHERHEADERID=vh.id and gl.VOUCHERHEADERID=vh.id " +
                        " and bd.budgetgroup=bg.id " +
                        " and vh.status not in ("+voucherstatusExclude+")  " +miscQuery+" " +
                        " and gl.glcodeid=bg.mincode and gl.glcodeid=bg.maxcode and  bg.majorcode is null group by bd.uniqueno");
            /*    " union "+
                "select bd.uniqueno,SUM(gl.creditAmount)-SUM(gl.debitAmount) from egf_budgetdetail bd,generalledger gl,voucherheader vh," +
                "vouchermis vmis,egf_budgetgroup bg,egf_budget b,financialyear f,fiscalperiod p where bd.budget=b.id " +
                        " and p.financialyearid=f.id and f.id="+fy.getId()+" and vh.fiscalperiodid=p.id "+dateCondition+" and b.financialyearid="+topBudget.getFinancialYear().getId()+
                        " and b.MATERIALIZEDPATH like '"+topBudget.getMaterializedPath()+"%' and vmis.VOUCHERHEADERID=vh.id and gl.VOUCHERHEADERID=vh.id " +
                        referingUniqueNoQry.toString()+
                        " and bd.budgetgroup=bg.id and (bg.ACCOUNTTYPE='REVENUE_RECEIPTS' or bg.ACCOUNTTYPE='CAPITAL_RECEIPTS')" +
                        " and vh.status not in ("+voucherstatusExclude+") " +miscQuery+" and gl.glcodeid= bg.mincode and gl.glcodeid=bg.maxcode  and bg.majorcode is null group by bd.uniqueno");
        */

    //    if(LOGGER.isDebugEnabled())     LOGGER.debug("Query for fetchActualsForFY "+fy.getStartingDate().getYear()+"-"+fy.getEndingDate().getYear()+"------"+query.toString());
        List<Object[]> result =null;//HibernateUtil.getCurrentSession().createSQLQuery(query.toString()).list();
        if(LOGGER.isInfoEnabled())     LOGGER.info("Finished fetchActualsForFY "+result.size()+"      "+query.toString());
        if(LOGGER.isInfoEnabled()) LOGGER.info("==============================================================================================" );
        return result;
    }

    /*
     * Copy of fetchActualsForFY
     *  passing exclude_status_forbudget_actual as list to reduce db hit
     */

    public List<Object[]> fetchActualsForFinYear(CFinancialYear fy,List<String> mandatoryFields,Budget topBudget, Budget referingTopBudget, Date date,Integer dept,Long fun, List<AppConfigValues> list) {
        if(LOGGER.isInfoEnabled()) LOGGER.info("==============================================================================================" );
        if(LOGGER.isInfoEnabled())     LOGGER.info("Starting fetchActualsForFY"+fy.getStartingDate().getYear()+"-"+fy.getEndingDate().getYear() );
        String dateCondition = "";
        if(date != null){
            dateCondition = " AND vh.voucherdate <='"+Constants.DDMMYYYYFORMAT1.format(date)+"' ";
        }
    //    List<AppConfigValues> list = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(Constants.EGF,"exclude_status_forbudget_actual");

        StringBuffer miscQuery = getMiscQuery(mandatoryFields,"vmis","gl","vh");
        if(dept!=null)
        {
            miscQuery.append(" and bd.executing_department="+dept);
        }
        if(fun!=null)
        {
            miscQuery=miscQuery.append(" AND bd.function="+fun);
        }
        StringBuffer referingUniqueNoQry=new StringBuffer(200);
        referingUniqueNoQry.append(" ");
        if(referingTopBudget!=null)
            referingUniqueNoQry.append(" and bd.uniqueno in (select uniqueno from egf_budgetdetail where MATERIALIZEDPATH like '"+referingTopBudget.getMaterializedPath()+"%'  )");


        StringBuffer budgetGroupQuery = new StringBuffer();
        budgetGroupQuery.append(" (select bg1.id as id,bg1.accounttype as accounttype,decode(c1.glcode,null,-1,to_number(c1.glcode)) " +
                "as mincode,decode(c2.glcode,null,999999999,c2.glcode) as maxcode,decode(c3.glcode,null,-1,to_number(c3.glcode)) as majorcode " +
                "from egf_budgetgroup bg1 left outer join chartofaccounts c1 on c1.id=bg1.mincode left outer join chartofaccounts c2 on " +
                "c2.id=bg1.maxcode left outer join chartofaccounts c3 on c3.id=bg1.majorcode ) bg ");
        String voucherstatusExclude = ((AppConfigValues)list.get(0)).getValue();
        StringBuffer query = new StringBuffer();
        /*query = query.append("select bd.uniqueno,SUM(gl.debitAmount)-SUM(gl.creditAmount) from egf_budgetdetail bd," +
                "vouchermis vmis,"+budgetGroupQuery+",egf_budget b,financialyear f,fiscalperiod p,voucherheader vh,generalledger gl " +
                        "where bd.budget=b.id and p.financialyearid=f.id and f.id="+fy.getId()+" and vh.fiscalperiodid=p.id "+dateCondition+" and " +
                        " b.financialyearid="+topBudget.getFinancialYear().getId()+" and b.MATERIALIZEDPATH like '"+topBudget.getMaterializedPath()+"%' " +referingUniqueNoQry.toString()+
                        " and  vmis.VOUCHERHEADERID=vh.id and gl.VOUCHERHEADERID=vh.id " +
                        " and bd.budgetgroup=bg.id and (bg.ACCOUNTTYPE='REVENUE_EXPENDITURE' or bg.ACCOUNTTYPE='CAPITAL_EXPENDITURE')" +
                        " and vh.status not in ("+voucherstatusExclude+")  " +miscQuery+" " +
                        " and ((gl.glcode between bg.mincode and bg.maxcode) or gl.glcode=bg.majorcode) and bg.mincode!=bg.maxcode group by bd.uniqueno"+
                " union "+
                "select bd.uniqueno,SUM(gl.creditAmount)-SUM(gl.debitAmount) from egf_budgetdetail bd,generalledger gl,voucherheader vh," +
                "vouchermis vmis,"+budgetGroupQuery+",egf_budget b,financialyear f,fiscalperiod p where bd.budget=b.id " +
                        "and p.financialyearid=f.id and f.id="+fy.getId()+" and vh.fiscalperiodid=p.id "+dateCondition+" and b.financialyearid="+topBudget.getFinancialYear().getId()+
                        " and b.MATERIALIZEDPATH like '"+topBudget.getMaterializedPath()+"%' and vmis.VOUCHERHEADERID=vh.id and gl.VOUCHERHEADERID=vh.id " +
                        referingUniqueNoQry.toString()+
                        " and bd.budgetgroup=bg.id and (bg.ACCOUNTTYPE='REVENUE_RECEIPTS' or bg.ACCOUNTTYPE='CAPITAL_RECEIPTS')" +
                        " and vh.status not in ("+voucherstatusExclude+") " +miscQuery+" and ((gl.glcode between bg.mincode and bg.maxcode)" +
                        " or gl.glcode=bg.majorcode) and bg.mincode!=bg.maxcode group by bd.uniqueno");
        */

        String sum="";
        if(topBudget.getName().contains("Receipt"))
        {
             sum = "SUM(gl.creditAmount)-SUM(gl.debitAmount)";
        }else
        {
             sum = "SUM(gl.debitAmount)-SUM(gl.creditAmount)";
        }

        query = query.append("  select bd.uniqueno,"+sum+" from egf_budgetdetail bd," +
                "vouchermis vmis,egf_budgetgroup bg,egf_budget b,financialyear f,fiscalperiod p,voucherheader vh,generalledger gl " +
                        "where bd.budget=b.id and p.financialyearid=f.id and f.id="+fy.getId()+" and vh.fiscalperiodid=p.id "+dateCondition+" and " +
                        " b.financialyearid="+topBudget.getFinancialYear().getId()+" and b.MATERIALIZEDPATH like '"+topBudget.getMaterializedPath()+"%' " +
                        referingUniqueNoQry.toString()+
                        " and  vmis.VOUCHERHEADERID=vh.id and gl.VOUCHERHEADERID=vh.id " +
                        " and bd.budgetgroup=bg.id " +
                        " and vh.status not in ("+voucherstatusExclude+")  " +miscQuery+" " +
                        " and gl.glcodeid=bg.mincode and gl.glcodeid=bg.maxcode and  bg.majorcode is null group by bd.uniqueno");
            /*    " union "+
                "select bd.uniqueno,SUM(gl.creditAmount)-SUM(gl.debitAmount) from egf_budgetdetail bd,generalledger gl,voucherheader vh," +
                "vouchermis vmis,egf_budgetgroup bg,egf_budget b,financialyear f,fiscalperiod p where bd.budget=b.id " +
                        " and p.financialyearid=f.id and f.id="+fy.getId()+" and vh.fiscalperiodid=p.id "+dateCondition+" and b.financialyearid="+topBudget.getFinancialYear().getId()+
                        " and b.MATERIALIZEDPATH like '"+topBudget.getMaterializedPath()+"%' and vmis.VOUCHERHEADERID=vh.id and gl.VOUCHERHEADERID=vh.id " +
                        referingUniqueNoQry.toString()+
                        " and bd.budgetgroup=bg.id and (bg.ACCOUNTTYPE='REVENUE_RECEIPTS' or bg.ACCOUNTTYPE='CAPITAL_RECEIPTS')" +
                        " and vh.status not in ("+voucherstatusExclude+") " +miscQuery+" and gl.glcodeid= bg.mincode and gl.glcodeid=bg.maxcode  and bg.majorcode is null group by bd.uniqueno");
        */

    //    if(LOGGER.isDebugEnabled())     LOGGER.debug("Query for fetchActualsForFY "+fy.getStartingDate().getYear()+"-"+fy.getEndingDate().getYear()+"------"+query.toString());
        List<Object[]> result =null;//HibernateUtil.getCurrentSession().createSQLQuery(query.toString()).list();
        if(LOGGER.isInfoEnabled())     LOGGER.info("Finished fetchActualsForFY "+result.size()+"      "+query.toString());
        if(LOGGER.isInfoEnabled()) LOGGER.info("==============================================================================================" );
        return result;
    }


    /**
     * vouchers are of the passed finaicial year
     * budget is of passed topBudgets financialyear
     */

    public List<Object[]> fetchMajorCodeAndActuals(CFinancialYear financialYear, Budget topBudget, Date date, CFunction function,Department dept, Position pos){
        if(LOGGER.isInfoEnabled())     LOGGER.info("Starting fetchMajorCodeAndActuals................");
        StringBuffer query = new StringBuffer();
        String dateCondition = "";
        if(date != null){
            dateCondition = " AND vh.voucherdate <='"+Constants.DDMMYYYYFORMAT1.format(date)+"' ";
        }
        String functionCondition = "";
        if(function!=null)
            functionCondition = " and gl.functionId="+function.getId();
        List<AppConfigValues> list = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(Constants.EGF,"exclude_status_forbudget_actual");
        if(list.isEmpty())
            throw new ValidationException("","exclude_status_forbudget_actual is not defined in AppConfig");
        String voucherstatusExclude = ((AppConfigValues)list.get(0)).getValue();
        /*query = query.append("SELECT substr(gl.glcode,1,3), SUM(gl.debitAmount)-SUM(gl.creditAmount) FROM egf_budgetdetail bd, vouchermis vmis," +
                " (SELECT bg1.id AS id, bg1.accounttype AS accounttype, DECODE(c1.glcode,NULL,-1,to_number(c1.glcode)) AS mincode, DECODE(c2.glcode,NULL,999999999,c2.glcode) AS maxcode, DECODE(c3.glcode,NULL,-1,to_number(c3.glcode)) AS majorcode" +
                " FROM egf_budgetgroup bg1 LEFT OUTER JOIN chartofaccounts c1 ON c1.id=bg1.mincode LEFT OUTER JOIN chartofaccounts c2 ON c2.id=bg1.maxcode LEFT OUTER JOIN chartofaccounts c3 ON c3.id=bg1.majorcode) bg ," +
                " egf_budget b, financialyear f, fiscalperiod p, voucherheader vh, generalledger gl, eg_wf_states wf" +
                " WHERE bd.budget =b.id AND p.financialyearid=f.id AND f.id ="+financialYear.getId()+" AND vh.fiscalperiodid=p.id "+dateCondition+" AND b.financialyearid="+topBudget.getFinancialYear().getId()+" AND b.MATERIALIZEDPATH LIKE '"+topBudget.getMaterializedPath()+"%' AND vmis.VOUCHERHEADERID=vh.id AND gl.VOUCHERHEADERID  =vh.id" +
                " AND bd.budgetgroup =bg.id AND (bg.ACCOUNTTYPE ='REVENUE_EXPENDITURE' OR bg.ACCOUNTTYPE ='CAPITAL_EXPENDITURE') AND vh.status NOT IN ("+voucherstatusExclude+") AND vh.fundId =bd.fund "+functionCondition+" AND vmis.departmentid =bd.executing_department and bd.executing_department =" +dept.getId()+
                " AND ((gl.glcode BETWEEN bg.mincode AND bg.maxcode) OR gl.glcode =bg.majorcode) AND bg.mincode!=bg.maxcode AND (wf.value='END' OR wf.owner="+pos.getId()+") AND bd.state_id = wf.id GROUP BY substr(gl.glcode,1,3)");

        query = query.append(" UNION ");

        query = query.append("SELECT substr(gl.glcode,1,3), SUM(gl.creditAmount)-SUM(gl.debitAmount) FROM egf_budgetdetail bd, generalledger gl, voucherheader vh, vouchermis vmis," +
                " (SELECT bg1.id AS id, bg1.accounttype AS accounttype, DECODE(c1.glcode,NULL,-1,to_number(c1.glcode)) AS mincode, DECODE(c2.glcode,NULL,999999999,c2.glcode)     AS maxcode, DECODE(c3.glcode,NULL,-1,to_number(c3.glcode)) AS majorcode" +
                " FROM egf_budgetgroup bg1 LEFT OUTER JOIN chartofaccounts c1 ON c1.id=bg1.mincode LEFT OUTER JOIN chartofaccounts c2 ON c2.id=bg1.maxcode LEFT OUTER JOIN chartofaccounts c3 ON c3.id=bg1.majorcode) bg ," +
                " egf_budget b, financialyear f, fiscalperiod p, eg_wf_states wf" +
                " WHERE bd.budget =b.id AND p.financialyearid=f.id AND f.id ="+financialYear.getId()+" AND vh.fiscalperiodid=p.id "+dateCondition+" AND b.financialyearid="+topBudget.getFinancialYear().getId()+" AND b.MATERIALIZEDPATH LIKE '"+topBudget.getMaterializedPath()+"%' AND vmis.VOUCHERHEADERID=vh.id AND gl.VOUCHERHEADERID  =vh.id" +
                " AND bd.budgetgroup =bg.id AND (bg.ACCOUNTTYPE ='REVENUE_RECEIPTS' OR bg.ACCOUNTTYPE ='CAPITAL_RECEIPTS') AND vh.status NOT IN ("+voucherstatusExclude+") AND vh.fundId =bd.fund "+functionCondition+" AND vmis.departmentid =bd.executing_department and bd.executing_department =" +dept.getId()+
                " AND ((gl.glcode BETWEEN bg.mincode AND bg.maxcode) OR gl.glcode   =bg.majorcode) AND bg.mincode!=bg.maxcode AND (wf.value='END' OR wf.owner="+pos.getId()+") AND bd.state_id = wf.id GROUP BY substr(gl.glcode,1,3)");

        query = query.append(" UNION ");*/
        String sum="";
        if(topBudget.getName().contains("Receipt"))
        {
             sum = "SUM(gl.creditAmount)-SUM(gl.debitAmount)";
        }else
        {
             sum = "SUM(gl.debitAmount)-SUM(gl.creditAmount)";
        }

        query = query.append("SELECT substr(gl.glcode,1,3),"+sum+" FROM egf_budgetdetail bd, vouchermis vmis, egf_budgetgroup bg, egf_budget b, financialyear f, fiscalperiod p, voucherheader vh, generalledger gl, eg_wf_states wf" +
                " WHERE bd.budget      =b.id AND p.financialyearid=f.id AND f.id ="+financialYear.getId()+" AND vh.fiscalperiodid=p.id "+dateCondition+" AND b.financialyearid="+topBudget.getFinancialYear().getId()+" AND b.id = "+topBudget.getId()+" AND vmis.VOUCHERHEADERID=vh.id AND gl.VOUCHERHEADERID  =vh.id" +
                " AND bd.budgetgroup      =bg.id  AND vh.status NOT      IN ("+voucherstatusExclude+") AND vh.fundId =bd.fund AND gl.functionId =bd.function "+functionCondition+"" +
                " AND vmis.departmentid   =bd.executing_department and bd.executing_department =" +dept.getId()+" AND gl.glcodeid         =bg.mincode AND gl.glcodeid         =bg.maxcode AND bg.majorcode       IS NULL AND (wf.value='END' OR wf.owner="+pos.getId()+") AND bd.state_id = wf.id GROUP BY substr(gl.glcode,1,3)");

        /*query = query.append(" UNION ");

        query = query.append("SELECT substr(gl.glcode,1,3), SUM(gl.creditAmount)-SUM(gl.debitAmount) FROM egf_budgetdetail bd, generalledger gl, voucherheader vh, vouchermis vmis, egf_budgetgroup bg, egf_budget b, financialyear f, fiscalperiod p, eg_wf_states wf" +
                " WHERE bd.budget      =b.id AND p.financialyearid=f.id AND f.id ="+financialYear.getId()+" AND vh.fiscalperiodid=p.id "+dateCondition+" AND b.financialyearid="+topBudget.getFinancialYear().getId()+" AND b.MATERIALIZEDPATH LIKE '"+topBudget.getMaterializedPath()+"%' AND vmis.VOUCHERHEADERID=vh.id AND gl.VOUCHERHEADERID  =vh.id" +
                " AND bd.budgetgroup      =bg.id AND (bg.ACCOUNTTYPE     ='REVENUE_RECEIPTS' OR bg.ACCOUNTTYPE ='CAPITAL_RECEIPTS') AND vh.status NOT      IN ("+voucherstatusExclude+") AND vh.fundId           =bd.fund "+functionCondition+"" +
                " AND vmis.departmentid   =bd.executing_department and bd.executing_department =" +dept.getId()+" AND gl.glcodeid         = bg.mincode AND gl.glcodeid         =bg.maxcode AND bg.majorcode       IS NULL AND (wf.value='END' OR wf.owner="+pos.getId()+") AND bd.state_id = wf.id GROUP BY substr(gl.glcode,1,3)");
        */
        List<Object[]> result =null;//HibernateUtil.getCurrentSession().createSQLQuery(query.toString()).list();
        if(LOGGER.isInfoEnabled())     LOGGER.info("Finished fetchMajorCodeAndActuals......."+query.toString());

        return result;
    }

    public List<Object[]> fetchMajorCodeAndName(Budget topBudget, BudgetDetail budgetDetail, CFunction function, Position pos){
        if(LOGGER.isInfoEnabled())     LOGGER.info("Starting fetchMajorCodeAndName............");
        StringBuffer query = new StringBuffer();
        String functionCondition = "";
        if(function != null)
            functionCondition = " AND bd.function = "+function.getId();
        /*query = query.append("SELECT cao.majorcode, cao1.glcode||'-'||cao1.name FROM egf_budgetdetail bd, egf_budgetgroup bg, egf_budget b, chartofaccounts cao, chartofaccounts cao1, financialyear f, eg_wf_states wf" +
                " WHERE bd.budget=b.id AND f.id="+topBudget.getFinancialYear().getId()+" AND b.financialyearid="+topBudget.getFinancialYear().getId()+" AND b.MATERIALIZEDPATH LIKE '"+topBudget.getMaterializedPath()+"%' AND bd.budgetgroup  =bg.id AND (bg.ACCOUNTTYPE ='REVENUE_EXPENDITURE' OR bg.ACCOUNTTYPE   ='CAPITAL_EXPENDITURE')" +
                " AND ((cao.id BETWEEN bg.mincode AND bg.maxcode) OR cao.majorcode=bg.majorcode) AND bg.mincode!=bg.maxcode AND bd.executing_department = "+budgetDetail.getExecutingDepartment().getId()+functionCondition+ " AND (wf.value='END' OR wf.owner="+pos.getId()+") AND bd.state_id = wf.id GROUP BY cao.majorcode, cao1.glcode||'-'||cao1.name");

        query = query.append(" UNION ");

        query = query.append("SELECT cao.majorcode, cao1.glcode||'-'||cao1.name FROM egf_budgetdetail bd, egf_budgetgroup bg, egf_budget b, chartofaccounts cao, chartofaccounts cao1, financialyear f, eg_wf_states wf" +
                " WHERE bd.budget=b.id AND f.id="+topBudget.getFinancialYear().getId()+" AND b.financialyearid="+topBudget.getFinancialYear().getId()+" AND b.MATERIALIZEDPATH LIKE '"+topBudget.getMaterializedPath()+"%' AND bd.budgetgroup=bg.id AND (bg.ACCOUNTTYPE ='REVENUE_RECEIPTS' OR bg.ACCOUNTTYPE='CAPITAL_RECEIPTS')" +
                " AND ((cao.id BETWEEN bg.mincode AND bg.maxcode) OR cao.majorcode=bg.majorcode) AND bg.mincode!=bg.maxcode AND bd.executing_department = "+budgetDetail.getExecutingDepartment().getId()+functionCondition+" AND (wf.value='END' OR wf.owner="+pos.getId()+") AND bd.state_id = wf.id GROUP BY cao.majorcode, cao1.glcode||'-'||cao1.name");

        query = query.append(" UNION ");*/

        query = query.append("SELECT cao.majorcode, cao1.glcode||'-'||cao1.name FROM egf_budgetdetail bd, egf_budgetgroup bg, egf_budget b, chartofaccounts cao, chartofaccounts cao1, financialyear f, eg_wf_states wf" +
                " WHERE bd.budget=b.id AND f.id="+topBudget.getFinancialYear().getId()+" AND b.financialyearid="+topBudget.getFinancialYear().getId()+" AND b.MATERIALIZEDPATH LIKE '"+topBudget.getMaterializedPath()+"%' AND bd.budgetgroup=bg.id " +
                " AND cao.id=bg.mincode AND cao.id=bg.maxcode AND bg.majorcode IS NULL AND bd.executing_department = "+budgetDetail.getExecutingDepartment().getId()+functionCondition+" and cao1.glcode = cao.majorcode AND (wf.value='END' OR wf.owner="+pos.getId()+") AND bd.state_id = wf.id GROUP BY cao.majorcode, cao1.glcode||'-'||cao1.name");

        /*query = query.append(" UNION ");

        query = query.append("SELECT cao.majorcode, cao1.glcode||'-'||cao1.name FROM egf_budgetdetail bd, egf_budgetgroup bg, egf_budget b, chartofaccounts cao, chartofaccounts cao1, financialyear f, eg_wf_states wf" +
                " WHERE bd.budget=b.id AND f.id="+topBudget.getFinancialYear().getId()+" AND b.financialyearid="+topBudget.getFinancialYear().getId()+" AND b.MATERIALIZEDPATH LIKE '"+topBudget.getMaterializedPath()+"%' AND bd.budgetgroup=bg.id AND (bg.ACCOUNTTYPE='REVENUE_RECEIPTS' OR bg.ACCOUNTTYPE='CAPITAL_RECEIPTS')" +
                " AND cao.id =bg.mincode AND cao.id =bg.maxcode AND bg.majorcode IS NULL and cao1.glcode = cao.majorcode AND bd.executing_department = "+budgetDetail.getExecutingDepartment().getId()+functionCondition+" AND (wf.value='END' OR wf.owner="+pos.getId()+") AND bd.state_id = wf.id GROUP BY cao.majorcode, cao1.glcode||'-'||cao1.name");
        */
        List<Object[]> result =null;//HibernateUtil.getCurrentSession().createSQLQuery(query.toString()).list();
        if(LOGGER.isInfoEnabled())     LOGGER.info("Finished fetchMajorCodeAndName..........."+query.toString());

        return result;
    }

    public List<Object[]> fetchMajorCodeAndBEAmount(Budget topBudget, BudgetDetail budgetDetail, CFunction function, Position pos){
        if(LOGGER.isInfoEnabled())     LOGGER.info("Starting fetchMajorCodeAndBEAmount................");
        StringBuffer query = new StringBuffer();
        String functionCondition1 = "";
        String functionCondition2 = "";
        if(function != null){
            functionCondition1 = " AND bd1.function = "+function.getId();
            functionCondition2 = " AND bd2.function = "+function.getId();
        }
        /*query = query.append("SELECT cao.majorcode, SUM(bd2.approvedamount) FROM egf_budgetdetail bd1, egf_budgetdetail bd2, egf_budgetgroup bg, egf_budget b1, egf_budget b2, chartofaccounts cao, financialyear f, eg_wf_states wf" +
                " WHERE bd1.budget =b1.id AND bd2.budget =b2.id AND f.id ="+topBudget.getFinancialYear().getId()+" AND b1.financialyearid="+topBudget.getFinancialYear().getId()+" AND b2.financialyearid="+topBudget.getFinancialYear().getId()+" AND b1.MATERIALIZEDPATH LIKE '"+topBudget.getMaterializedPath()+"%' AND bd2.budgetgroup =bg.id AND (bg.ACCOUNTTYPE ='REVENUE_EXPENDITURE' OR bg.ACCOUNTTYPE ='CAPITAL_EXPENDITURE')" +
                " AND ((cao.id BETWEEN bg.mincode AND bg.maxcode) OR cao.majorcode   =bg.majorcode) AND bd2.executing_department = "+budgetDetail.getExecutingDepartment().getId()+functionCondition2+" AND bd1.executing_department = "+budgetDetail.getExecutingDepartment().getId()+functionCondition1+" AND bd1.uniqueno = bd2.uniqueno AND (wf.value='END' OR wf.owner="+pos.getId()+") AND bd1.state_id = wf.id GROUP BY cao.majorcode");

        query = query.append(" UNION ");

        query = query.append("SELECT cao.majorcode, SUM(bd2.approvedamount) FROM egf_budgetdetail bd1, egf_budgetdetail bd2, egf_budgetgroup bg, egf_budget b1, egf_budget b2, chartofaccounts cao, financialyear f, eg_wf_states wf" +
                " WHERE bd1.budget =b1.id AND bd2.budget =b2.id AND f.id ="+topBudget.getFinancialYear().getId()+" AND b1.financialyearid="+topBudget.getFinancialYear().getId()+" AND b2.financialyearid="+topBudget.getFinancialYear().getId()+" AND b1.MATERIALIZEDPATH LIKE '"+topBudget.getMaterializedPath()+"%' AND bd2.budgetgroup =bg.id AND (bg.ACCOUNTTYPE ='REVENUE_RECEIPTS' OR bg.ACCOUNTTYPE ='CAPITAL_RECEIPTS')" +
                " AND ((cao.id BETWEEN bg.mincode AND bg.maxcode) OR cao.majorcode   =bg.majorcode) AND bd2.executing_department = "+budgetDetail.getExecutingDepartment().getId()+functionCondition2+" AND bd1.executing_department = "+budgetDetail.getExecutingDepartment().getId()+functionCondition1+" AND bd1.uniqueno = bd2.uniqueno AND (wf.value='END' OR wf.owner="+pos.getId()+") AND bd1.state_id = wf.id GROUP BY cao.majorcode");

        query = query.append(" UNION ");*/
        /// need to add b2.isbere='BE'
        query = query.append("SELECT cao.majorcode, SUM(bd2.approvedamount) FROM egf_budgetdetail bd1, egf_budgetdetail bd2, egf_budgetgroup bg, egf_budget b1, egf_budget b2, chartofaccounts cao, financialyear f, eg_wf_states wf" +
                " WHERE bd1.budget =b1.id AND bd2.budget =b2.id AND f.id ="+topBudget.getFinancialYear().getId()+" AND b1.financialyearid="+topBudget.getFinancialYear().getId()+" AND b2.financialyearid="+topBudget.getFinancialYear().getId()+" AND b1.MATERIALIZEDPATH LIKE '"+topBudget.getMaterializedPath()+"%' and b2.isbere='BE' AND bd2.budgetgroup =bg.id  " +
                " AND cao.id =bg.mincode AND cao.id =bg.maxcode AND bg.majorcode IS NULL AND bd2.executing_department = "+budgetDetail.getExecutingDepartment().getId()+functionCondition2+" AND bd1.executing_department = "+budgetDetail.getExecutingDepartment().getId()+functionCondition1+" AND bd1.uniqueno = bd2.uniqueno AND (wf.value='END' OR wf.owner="+pos.getId()+") AND bd1.state_id = wf.id GROUP BY cao.majorcode");

        /*query = query.append(" UNION ");

        query = query.append("SELECT cao.majorcode, SUM(bd2.approvedamount) FROM egf_budgetdetail bd1, egf_budgetdetail bd2, egf_budgetgroup bg, egf_budget b1, egf_budget b2, chartofaccounts cao, financialyear f, eg_wf_states wf" +
                " WHERE bd1.budget =b1.id AND bd2.budget =b2.id AND f.id ="+topBudget.getFinancialYear().getId()+" AND b1.financialyearid="+topBudget.getFinancialYear().getId()+" AND b2.financialyearid="+topBudget.getFinancialYear().getId()+" AND b1.MATERIALIZEDPATH LIKE '"+topBudget.getMaterializedPath()+"%' AND bd2.budgetgroup =bg.id AND (bg.ACCOUNTTYPE ='REVENUE_RECEIPTS' OR bg.ACCOUNTTYPE ='CAPITAL_RECEIPTS')" +
                " AND cao.id =bg.mincode AND cao.id =bg.maxcode AND bg.majorcode IS NULL AND bd2.executing_department = "+budgetDetail.getExecutingDepartment().getId()+functionCondition2+" AND bd1.executing_department = "+budgetDetail.getExecutingDepartment().getId()+functionCondition1+" AND bd1.uniqueno = bd2.uniqueno AND (wf.value='END' OR wf.owner="+pos.getId()+") AND bd1.state_id = wf.id GROUP BY cao.majorcode");
        */
        List<Object[]> result =null;//HibernateUtil.getCurrentSession().createSQLQuery(query.toString()).list();
        if(LOGGER.isInfoEnabled())     LOGGER.info("Finished fetchMajorCodeAndBEAmount");

        return result;
    }

    public List<Object[]> fetchUniqueNoAndBEAmount(Budget topBudget, BudgetDetail budgetDetail, CFunction function, Position pos){
        if(LOGGER.isInfoEnabled())     LOGGER.info("Starting fetchUniqueNoAndBEAmount................");
        StringBuffer query = new StringBuffer();
        String functionCondition1 = "";
        String functionCondition2 = "";
        if(function != null){
            functionCondition1 = " AND bd1.function = "+function.getId();
            functionCondition2 = " AND bd2.function = "+function.getId();
        }

        query = query.append("SELECT bd2.uniqueno, SUM(bd2.approvedamount) FROM egf_budgetdetail bd1, egf_budgetdetail bd2, egf_budgetgroup bg, egf_budget b1, egf_budget b2, chartofaccounts cao, financialyear f, eg_wf_states wf" +
                " WHERE bd1.budget =b1.id AND bd2.budget =b2.id AND f.id ="+topBudget.getFinancialYear().getId()+" AND b1.financialyearid="+topBudget.getFinancialYear().getId()+" AND b2.financialyearid="+topBudget.getFinancialYear().getId()+" AND b1.MATERIALIZEDPATH LIKE '"+topBudget.getMaterializedPath()+"%' and b2.isbere='BE' AND bd2.budgetgroup =bg.id  " +
                " AND cao.id =bg.mincode AND cao.id =bg.maxcode AND bg.majorcode IS NULL AND bd2.executing_department = "+budgetDetail.getExecutingDepartment().getId()+functionCondition2+" AND bd1.executing_department = "+budgetDetail.getExecutingDepartment().getId()+functionCondition1+" AND bd1.uniqueno = bd2.uniqueno AND (wf.value='END' OR wf.owner="+pos.getId()+") AND bd1.state_id = wf.id GROUP BY bd2.uniqueno");

        List<Object[]> result =null;//HibernateUtil.getCurrentSession().createSQLQuery(query.toString()).list();
        if(LOGGER.isInfoEnabled())     LOGGER.info("Finished fetchUniqueNoAndBEAmount");

        return result;
    }

    public List<Object[]> fetchMajorCodeAndAppropriation(Budget topBudget, BudgetDetail budgetDetail, CFunction function, Position pos, Date asOnDate){
        if(LOGGER.isInfoEnabled())     LOGGER.info("Starting fetchMajorCodeAndAppropriation................");
        StringBuffer query = new StringBuffer();
        String functionCondition1 = "";
        String functionCondition2 = "";
        String dateCondition = "";
        String ReappropriationTable=" ";
        if(function != null){
            functionCondition1 = " AND bd1.function = "+function.getId();
            functionCondition2 = " AND bd2.function = "+function.getId();
        }
        if(asOnDate!=null){
            ReappropriationTable=" egf_reappropriation_misc bmisc,";
            dateCondition=" and bapp.reappropriation_misc= bmisc.id and  bmisc.reappropriation_date <= '"+Constants.DDMMYYYYFORMAT1.format(asOnDate)+"'";
        }
        /*query = query.append("SELECT cao.majorcode, SUM(bapp.addition_amount)-SUM(bapp.deduction_amount) FROM egf_budgetdetail bd1, egf_budgetdetail bd2, egf_budgetgroup bg, egf_budget b1, egf_budget b2, chartofaccounts cao, financialyear f, egf_budget_reappropriation bapp, eg_wf_states wf" +
                " WHERE bd1.budget      =b1.id and bd2.budget =b2.id AND f.id ="+topBudget.getFinancialYear().getId()+" AND b1.financialyearid="+topBudget.getFinancialYear().getId()+" AND b2.financialyearid="+topBudget.getFinancialYear().getId()+" AND b1.MATERIALIZEDPATH LIKE '"+topBudget.getMaterializedPath()+"%' AND bd2.budgetgroup  =bg.id AND (bg.ACCOUNTTYPE ='REVENUE_EXPENDITURE' OR bg.ACCOUNTTYPE   ='CAPITAL_EXPENDITURE')" +
                " AND ((cao.id BETWEEN bg.mincode AND bg.maxcode) OR cao.majorcode            =bg.majorcode) AND bg.mincode! =bg.maxcode AND bd1.executing_department = "+budgetDetail.getExecutingDepartment().getId()+" "+functionCondition1+" AND bd2.executing_department = "+budgetDetail.getExecutingDepartment().getId()+" "+functionCondition2+" " +
                " AND bapp.budgetdetail       = bd2.id AND (wf.value               ='END' OR wf.owner ="+pos.getId()+") AND bd1.state_id             = wf.id and bd1.uniqueno = bd2.uniqueno GROUP BY cao.majorcode");

        query = query.append(" UNION ");

        query = query.append("SELECT cao.majorcode, SUM(bapp.addition_amount)-SUM(bapp.deduction_amount) FROM egf_budgetdetail bd1, egf_budgetdetail bd2, egf_budgetgroup bg, egf_budget b1, egf_budget b2, chartofaccounts cao, financialyear f, egf_budget_reappropriation bapp, eg_wf_states wf" +
                " WHERE bd1.budget      =b1.id and bd2.budget =b2.id AND f.id ="+topBudget.getFinancialYear().getId()+" AND b1.financialyearid="+topBudget.getFinancialYear().getId()+" AND b2.financialyearid="+topBudget.getFinancialYear().getId()+" AND b1.MATERIALIZEDPATH LIKE '"+topBudget.getMaterializedPath()+"%' AND bd2.budgetgroup  =bg.id AND (bg.ACCOUNTTYPE ='REVENUE_RECEIPTS' OR bg.ACCOUNTTYPE   ='CAPITAL_RECEIPTS')" +
                " AND ((cao.id BETWEEN bg.mincode AND bg.maxcode) OR cao.majorcode            =bg.majorcode) AND bg.mincode! =bg.maxcode AND bd1.executing_department = "+budgetDetail.getExecutingDepartment().getId()+" "+functionCondition1+" AND bd2.executing_department = "+budgetDetail.getExecutingDepartment().getId()+" "+functionCondition2+" " +
                " AND bapp.budgetdetail       = bd2.id AND (wf.value               ='END' OR wf.owner ="+pos.getId()+") AND bd1.state_id             = wf.id and bd1.uniqueno = bd2.uniqueno GROUP BY cao.majorcode");

        query = query.append(" UNION ");*/

        query = query.append("SELECT cao.majorcode, SUM(bapp.addition_amount)-SUM(bapp.deduction_amount) FROM egf_budgetdetail bd1, egf_budgetdetail bd2, egf_budgetgroup bg, egf_budget b1, egf_budget b2, chartofaccounts cao, financialyear f, egf_budget_reappropriation bapp, "+ReappropriationTable +" eg_wf_states wf" +
                " WHERE bd1.budget=b1.id and bd2.budget=b2.id AND f.id   ="+topBudget.getFinancialYear().getId()+" AND b1.financialyearid="+topBudget.getFinancialYear().getId()+" AND b2.financialyearid="+topBudget.getFinancialYear().getId()+" AND b1.MATERIALIZEDPATH LIKE '"
                +topBudget.getMaterializedPath()+"%' and b2.isbere='BE' AND bd2.budgetgroup          =bg.id " +dateCondition+
                " AND cao.id=bg.mincode AND cao.id=bg.maxcode AND bg.majorcode IS NULL AND bd1.executing_department = "+budgetDetail.getExecutingDepartment().getId()+" "+functionCondition1+" AND bd2.executing_department = "+budgetDetail.getExecutingDepartment().getId()+"" +
                " "+functionCondition2+" AND bapp.budgetdetail  = bd2.id AND (wf.value ='END' OR wf.owner ="+pos.getId()+") AND bd1.state_id             = wf.id and bd1.uniqueno = bd2.uniqueno GROUP BY cao.majorcode");

        /*query = query.append(" UNION ");

        query = query.append("SELECT cao.majorcode, SUM(bapp.addition_amount)-SUM(bapp.deduction_amount) FROM egf_budgetdetail bd1, egf_budgetdetail bd2, egf_budgetgroup bg, egf_budget b1, egf_budget b2, chartofaccounts cao, financialyear f, egf_budget_reappropriation bapp, eg_wf_states wf" +
                " WHERE bd1.budget      =b1.id and bd2.budget =b2.id AND f.id ="+topBudget.getFinancialYear().getId()+" AND b1.financialyearid="+topBudget.getFinancialYear().getId()+" AND b2.financialyearid="+topBudget.getFinancialYear().getId()+" AND b1.MATERIALIZEDPATH LIKE '"+topBudget.getMaterializedPath()+"%' AND bd2.budgetgroup          =bg.id AND (bg.ACCOUNTTYPE ='REVENUE_RECEIPTS'" +
                " OR bg.ACCOUNTTYPE           ='CAPITAL_RECEIPTS') AND cao.id                  =bg.mincode AND cao.id =bg.maxcode AND bg.majorcode           IS NULL AND bd1.executing_department = "+budgetDetail.getExecutingDepartment().getId()+" "+functionCondition1+" AND bd2.executing_department = "+budgetDetail.getExecutingDepartment().getId()+"" +
                " "+functionCondition2+" AND bapp.budgetdetail = bd2.id AND (wf.value               ='END' OR wf.owner                 ="+pos.getId()+") AND bd1.state_id             = wf.id and bd1.uniqueno = bd2.uniqueno GROUP BY cao.majorcode");
        */
        List<Object[]> result =null;//HibernateUtil.getCurrentSession().createSQLQuery(query.toString()).list();
        if(LOGGER.isInfoEnabled())     LOGGER.info("Finished fetchMajorCodeAndAppropriation");

        return result;
    }

    public List<Object[]> fetchUniqueNoAndApprAmount(Budget topBudget, BudgetDetail budgetDetail, CFunction function, Position pos){
        if(LOGGER.isInfoEnabled())     LOGGER.info("Starting fetchUniqueNoAndApprAmount................");
        StringBuffer query = new StringBuffer();
        String functionCondition1 = "";
        String functionCondition2 = "";
        if(function != null){
            functionCondition1 = " AND bd1.function = "+function.getId();
            functionCondition2 = " AND bd2.function = "+function.getId();
        }

        query = query.append("SELECT bd2.uniqueno, SUM(bapp.addition_amount)-SUM(bapp.deduction_amount) FROM egf_budgetdetail bd1, egf_budgetdetail bd2, egf_budgetgroup bg, egf_budget b1, egf_budget b2, chartofaccounts cao, financialyear f, egf_budget_reappropriation bapp, eg_wf_states wf" +
                " WHERE bd1.budget      =b1.id and bd2.budget =b2.id AND f.id ="+topBudget.getFinancialYear().getId()+" AND b1.financialyearid="+topBudget.getFinancialYear().getId()+" AND b2.financialyearid="+topBudget.getFinancialYear().getId()+" AND b1.MATERIALIZEDPATH LIKE '"+topBudget.getMaterializedPath()+"%' and b2.isbere='BE' AND bd2.budgetgroup          =bg.id " +
                " AND cao.id                  =bg.mincode AND cao.id                  =bg.maxcode AND bg.majorcode           IS NULL AND bd1.executing_department = "+budgetDetail.getExecutingDepartment().getId()+" "+functionCondition1+" AND bd2.executing_department = "+budgetDetail.getExecutingDepartment().getId()+"" +
                " "+functionCondition2+" AND bapp.budgetdetail = bd2.id AND (wf.value               ='END' OR wf.owner                 ="+pos.getId()+") AND bd1.state_id             = wf.id and bd1.uniqueno = bd2.uniqueno GROUP BY bd2.uniqueno");

        List<Object[]> result =null;//HibernateUtil.getCurrentSession().createSQLQuery(query.toString()).list();
        if(LOGGER.isInfoEnabled())     LOGGER.info("Finished fetchUniqueNoAndApprAmount");

        return result;
    }

    public List<Object[]> fetchMajorCodeAndAnticipatory(Budget topBudget, BudgetDetail budgetDetail, CFunction function, Position pos){
        if(LOGGER.isInfoEnabled())     LOGGER.info("Starting fetchMajorCodeAndAnticipatory................");
        StringBuffer query = new StringBuffer();
        String functionCondition = "";
        if(function != null)
            functionCondition = " AND bd.function = "+function.getId();
        /*query = query.append("SELECT cao.majorcode, SUM(bd.anticipatory_amount) FROM egf_budgetdetail bd, egf_budgetgroup bg, egf_budget b, chartofaccounts cao, financialyear f, eg_wf_states wf" +
                " WHERE bd.budget =b.id AND f.id ="+topBudget.getFinancialYear().getId()+" AND b.financialyearid="+topBudget.getFinancialYear().getId()+" AND b.MATERIALIZEDPATH LIKE '"+topBudget.getMaterializedPath()+"%' AND bd.budgetgroup  =bg.id AND (bg.ACCOUNTTYPE ='REVENUE_EXPENDITURE' OR bg.ACCOUNTTYPE   ='CAPITAL_EXPENDITURE') AND ((cao.id BETWEEN bg.mincode AND bg.maxcode) OR cao.majorcode =bg.majorcode) AND bg.mincode! =bg.maxcode AND bd.executing_department = "+budgetDetail.getExecutingDepartment().getId()+functionCondition+" AND (wf.value='END' OR wf.owner="+pos.getId()+") AND bd.state_id = wf.id GROUP BY cao.majorcode");

        query = query.append(" UNION ");

        query = query.append("SELECT cao.majorcode, SUM(bd.anticipatory_amount) FROM egf_budgetdetail bd, egf_budgetgroup bg, egf_budget b, chartofaccounts cao, financialyear f, eg_wf_states wf" +
                " WHERE bd.budget =b.id AND f.id ="+topBudget.getFinancialYear().getId()+" AND b.financialyearid="+topBudget.getFinancialYear().getId()+" AND b.MATERIALIZEDPATH LIKE '"+topBudget.getMaterializedPath()+"%' AND bd.budgetgroup  =bg.id AND (bg.ACCOUNTTYPE ='REVENUE_RECEIPTS' OR bg.ACCOUNTTYPE ='CAPITAL_RECEIPTS') AND ((cao.id BETWEEN bg.mincode AND bg.maxcode) OR cao.majorcode =bg.majorcode) AND bg.mincode! =bg.maxcode AND bd.executing_department = "+budgetDetail.getExecutingDepartment().getId()+functionCondition+" AND (wf.value='END' OR wf.owner="+pos.getId()+") AND bd.state_id = wf.id GROUP BY cao.majorcode");

        query = query.append(" UNION ");
        */
        query = query.append("SELECT cao.majorcode, SUM(bd.anticipatory_amount), SUM(bd.originalamount),SUM(bd.approvedamount) FROM egf_budgetdetail bd, egf_budgetgroup bg, egf_budget b, chartofaccounts cao, financialyear f, eg_wf_states wf" +
                " WHERE bd.budget =b.id AND f.id ="+topBudget.getFinancialYear().getId()+" AND b.financialyearid="+topBudget.getFinancialYear().getId()+" AND b.MATERIALIZEDPATH LIKE '"+topBudget.getMaterializedPath()+"%' AND bd.budgetgroup =bg.id  AND cao.id =bg.mincode AND cao.id =bg.maxcode AND bg.majorcode IS NULL AND bd.executing_department = "+budgetDetail.getExecutingDepartment().getId()+functionCondition+" AND (wf.value='END' OR wf.owner="+pos.getId()+") AND bd.state_id = wf.id GROUP BY cao.majorcode");

        /*query = query.append(" UNION ");

        query = query.append("SELECT cao.majorcode, SUM(bd.anticipatory_amount) FROM egf_budgetdetail bd, egf_budgetgroup bg, egf_budget b, chartofaccounts cao, financialyear f, eg_wf_states wf" +
                " WHERE bd.budget =b.id AND f.id ="+topBudget.getFinancialYear().getId()+" AND b.financialyearid="+topBudget.getFinancialYear().getId()+" AND b.MATERIALIZEDPATH LIKE '"+topBudget.getMaterializedPath()+"%' AND bd.budgetgroup =bg.id AND (bg.ACCOUNTTYPE ='REVENUE_RECEIPTS' OR bg.ACCOUNTTYPE ='CAPITAL_RECEIPTS') AND cao.id =bg.mincode AND cao.id =bg.maxcode AND bg.majorcode IS NULL AND bd.executing_department = "+budgetDetail.getExecutingDepartment().getId()+functionCondition+" AND (wf.value='END' OR wf.owner="+pos.getId()+") AND bd.state_id = wf.id GROUP BY cao.majorcode");
        */
        List<Object[]> result =null;//HibernateUtil.getCurrentSession().createSQLQuery(query.toString()).list();
        if(LOGGER.isInfoEnabled())     LOGGER.info("Finished fetchMajorCodeAndAnticipatory");

        return result;
    }

    public List<Object[]> fetchMajorCodeAndOriginalAmount(Budget topBudget, BudgetDetail budgetDetail, CFunction function, Position pos){
        if(LOGGER.isInfoEnabled())     LOGGER.info("Starting fetchMajorCodeAndOriginalAmount................");
        StringBuffer query = new StringBuffer();
        String functionCondition = "";
        if(function != null)
            functionCondition = " AND bd.function = "+function.getId();
        /*query = query.append("SELECT cao.majorcode, SUM(bd.originalamount) FROM egf_budgetdetail bd, egf_budgetgroup bg, egf_budget b, chartofaccounts cao, financialyear f, eg_wf_states wf" +
                " WHERE bd.budget =b.id AND f.id ="+topBudget.getFinancialYear().getId()+" AND b.financialyearid="+topBudget.getFinancialYear().getId()+" AND b.MATERIALIZEDPATH LIKE '"+topBudget.getMaterializedPath()+"%' AND bd.budgetgroup  =bg.id AND (bg.ACCOUNTTYPE ='REVENUE_EXPENDITURE' OR bg.ACCOUNTTYPE   ='CAPITAL_EXPENDITURE') AND ((cao.id BETWEEN bg.mincode AND bg.maxcode) OR cao.majorcode =bg.majorcode) AND bg.mincode! =bg.maxcode AND bd.executing_department = "+budgetDetail.getExecutingDepartment().getId()+functionCondition+" AND (wf.value='END' OR wf.owner="+pos.getId()+") AND bd.state_id = wf.id GROUP BY cao.majorcode");

        query = query.append(" UNION ");

        query = query.append("SELECT cao.majorcode, SUM(bd.originalamount) FROM egf_budgetdetail bd, egf_budgetgroup bg, egf_budget b, chartofaccounts cao, financialyear f, eg_wf_states wf" +
                " WHERE bd.budget =b.id AND f.id ="+topBudget.getFinancialYear().getId()+" AND b.financialyearid="+topBudget.getFinancialYear().getId()+" AND b.MATERIALIZEDPATH LIKE '"+topBudget.getMaterializedPath()+"%' AND bd.budgetgroup  =bg.id AND (bg.ACCOUNTTYPE ='REVENUE_RECEIPTS' OR bg.ACCOUNTTYPE ='CAPITAL_RECEIPTS') AND ((cao.id BETWEEN bg.mincode AND bg.maxcode) OR cao.majorcode =bg.majorcode) AND bg.mincode! =bg.maxcode AND bd.executing_department = "+budgetDetail.getExecutingDepartment().getId()+functionCondition+" AND (wf.value='END' OR wf.owner="+pos.getId()+") AND bd.state_id = wf.id GROUP BY cao.majorcode");

        query = query.append(" UNION ");*/

        query = query.append("SELECT cao.majorcode, SUM(bd.originalamount) FROM egf_budgetdetail bd, egf_budgetgroup bg, egf_budget b, chartofaccounts cao, financialyear f, eg_wf_states wf" +
                " WHERE bd.budget =b.id AND f.id ="+topBudget.getFinancialYear().getId()+" AND b.financialyearid="+topBudget.getFinancialYear().getId()+" AND b.MATERIALIZEDPATH LIKE '"+topBudget.getMaterializedPath()+"%' AND bd.budgetgroup =bg.id  AND cao.id =bg.mincode AND cao.id =bg.maxcode AND bg.majorcode IS NULL AND bd.executing_department = "+budgetDetail.getExecutingDepartment().getId()+functionCondition+" AND (wf.value='END' OR wf.owner="+pos.getId()+") AND bd.state_id = wf.id GROUP BY cao.majorcode");

        /*query = query.append(" UNION ");

        query = query.append("SELECT cao.majorcode, SUM(bd.originalamount) FROM egf_budgetdetail bd, egf_budgetgroup bg, egf_budget b, chartofaccounts cao, financialyear f, eg_wf_states wf" +
                " WHERE bd.budget =b.id AND f.id ="+topBudget.getFinancialYear().getId()+" AND b.financialyearid="+topBudget.getFinancialYear().getId()+" AND b.MATERIALIZEDPATH LIKE '"+topBudget.getMaterializedPath()+"%' AND bd.budgetgroup =bg.id AND (bg.ACCOUNTTYPE ='REVENUE_RECEIPTS' OR bg.ACCOUNTTYPE ='CAPITAL_RECEIPTS') AND cao.id =bg.mincode AND cao.id =bg.maxcode AND bg.majorcode IS NULL AND bd.executing_department = "+budgetDetail.getExecutingDepartment().getId()+functionCondition+" AND (wf.value='END' OR wf.owner="+pos.getId()+") AND bd.state_id = wf.id GROUP BY cao.majorcode");
        */
        List<Object[]> result =null;//HibernateUtil.getCurrentSession().createSQLQuery(query.toString()).list();
        if(LOGGER.isInfoEnabled())     LOGGER.info("Finished fetchMajorCodeAndOriginalAmount");

        return result;
    }

    public List<Object[]> fetchMajorCodeAndBENextYr(Budget topBudget, BudgetDetail budgetDetail, CFunction function, Position pos){
        if(LOGGER.isInfoEnabled())     LOGGER.info("Starting fetchMajorCodeAndBENextYr................");
        StringBuffer query = new StringBuffer();
        String functionCondition1 = "";
        String functionCondition2 = "";
        if(function != null){
            functionCondition1 = " AND bd1.function = "+function.getId();
            functionCondition2 = " AND bd2.function = "+function.getId();
        }
        /*query = query.append("SELECT cao.majorcode, SUM(bd2.originalamount) FROM egf_budgetdetail bd1, egf_budgetdetail bd2, egf_budgetgroup bg, egf_budget b1, egf_budget b2, chartofaccounts cao, eg_wf_states wf" +
                " WHERE bd1.budget =b1.id AND bd2.budget =b2.id AND b1.financialyearid="+topBudget.getFinancialYear().getId()+" AND b1.MATERIALIZEDPATH LIKE '"+topBudget.getMaterializedPath()+"%' AND bd2.budgetgroup =bg.id AND (bg.ACCOUNTTYPE ='REVENUE_EXPENDITURE' OR bg.ACCOUNTTYPE ='CAPITAL_EXPENDITURE')" +
                " AND ((cao.id BETWEEN bg.mincode AND bg.maxcode) OR cao.majorcode   =bg.majorcode) AND bd2.executing_department = "+budgetDetail.getExecutingDepartment().getId()+functionCondition2+" AND bd1.executing_department = "+budgetDetail.getExecutingDepartment().getId()+functionCondition1+" AND bd1.uniqueno = bd2.uniqueno AND b2.reference_budget = b1.id AND (wf.value='END' OR wf.owner="+pos.getId()+") AND bd1.state_id = wf.id GROUP BY cao.majorcode");

        query = query.append(" UNION ");

        query = query.append("SELECT cao.majorcode, SUM(bd2.originalamount) FROM egf_budgetdetail bd1, egf_budgetdetail bd2, egf_budgetgroup bg, egf_budget b1, egf_budget b2, chartofaccounts cao, eg_wf_states wf" +
                " WHERE bd1.budget =b1.id AND bd2.budget =b2.id AND b1.financialyearid="+topBudget.getFinancialYear().getId()+" AND b1.MATERIALIZEDPATH LIKE '"+topBudget.getMaterializedPath()+"%' AND bd2.budgetgroup =bg.id AND (bg.ACCOUNTTYPE ='REVENUE_RECEIPTS' OR bg.ACCOUNTTYPE ='CAPITAL_RECEIPTS')" +
                " AND ((cao.id BETWEEN bg.mincode AND bg.maxcode) OR cao.majorcode   =bg.majorcode) AND bd2.executing_department = "+budgetDetail.getExecutingDepartment().getId()+functionCondition2+" AND bd1.executing_department = "+budgetDetail.getExecutingDepartment().getId()+functionCondition1+" AND bd1.uniqueno = bd2.uniqueno AND b2.reference_budget = b1.id AND (wf.value='END' OR wf.owner="+pos.getId()+") AND bd1.state_id = wf.id GROUP BY cao.majorcode");

        query = query.append(" UNION ");*/

        query = query.append("SELECT cao.majorcode, SUM(bd2.originalamount), SUM(bd2.approvedamount) FROM egf_budgetdetail bd1, egf_budgetdetail bd2, egf_budgetgroup bg, egf_budget b1, egf_budget b2, chartofaccounts cao, eg_wf_states wf" +
                " WHERE bd1.budget =b1.id AND bd2.budget =b2.id AND b1.financialyearid="+topBudget.getFinancialYear().getId()+" AND b1.MATERIALIZEDPATH LIKE '"+topBudget.getMaterializedPath()+"%' AND bd2.budgetgroup =bg.id " +
                " AND cao.id =bg.mincode AND cao.id =bg.maxcode AND bg.majorcode IS NULL AND bd2.executing_department = "+budgetDetail.getExecutingDepartment().getId()+functionCondition2+" AND bd1.executing_department = "+budgetDetail.getExecutingDepartment().getId()+functionCondition1+" AND bd1.uniqueno = bd2.uniqueno AND b2.reference_budget = b1.id AND (wf.value='END' OR wf.owner="+pos.getId()+") AND bd1.state_id = wf.id GROUP BY cao.majorcode");

        /*query = query.append(" UNION ");

        query = query.append("SELECT cao.majorcode, SUM(bd2.originalamount) FROM egf_budgetdetail bd1, egf_budgetdetail bd2, egf_budgetgroup bg, egf_budget b1, egf_budget b2, chartofaccounts cao, eg_wf_states wf" +
                " WHERE bd1.budget =b1.id AND bd2.budget =b2.id AND b1.financialyearid="+topBudget.getFinancialYear().getId()+" AND b1.MATERIALIZEDPATH LIKE '"+topBudget.getMaterializedPath()+"%' AND bd2.budgetgroup =bg.id AND (bg.ACCOUNTTYPE ='REVENUE_RECEIPTS' OR bg.ACCOUNTTYPE ='CAPITAL_RECEIPTS')" +
                " AND cao.id =bg.mincode AND cao.id =bg.maxcode AND bg.majorcode IS NULL AND bd2.executing_department = "+budgetDetail.getExecutingDepartment().getId()+functionCondition2+" AND bd1.executing_department = "+budgetDetail.getExecutingDepartment().getId()+functionCondition1+" AND bd1.uniqueno = bd2.uniqueno AND b2.reference_budget = b1.id AND (wf.value='END' OR wf.owner="+pos.getId()+") AND bd1.state_id = wf.id GROUP BY cao.majorcode");
        */
        List<Object[]> result =null;//HibernateUtil.getCurrentSession().createSQLQuery(query.toString()).list();
        if(LOGGER.isInfoEnabled())     LOGGER.info("Finished fetchMajorCodeAndBENextYr");

        return result;
    }

    public List<Object[]> fetchMajorCodeAndApprovedAmount(Budget topBudget, BudgetDetail budgetDetail, CFunction function, Position pos){
        if(LOGGER.isInfoEnabled())     LOGGER.info("Starting fetchMajorCodeAndApprovedAmount................");
        StringBuffer query = new StringBuffer();
        String functionCondition = "";
        if(function != null)
            functionCondition = " AND bd.function = "+function.getId();
        /*query = query.append("SELECT cao.majorcode, SUM(bd.approvedamount) FROM egf_budgetdetail bd, egf_budgetgroup bg, egf_budget b, chartofaccounts cao, financialyear f, eg_wf_states wf" +
                " WHERE bd.budget =b.id AND f.id ="+topBudget.getFinancialYear().getId()+" AND b.financialyearid="+topBudget.getFinancialYear().getId()+" AND b.MATERIALIZEDPATH LIKE '"+topBudget.getMaterializedPath()+"%' AND bd.budgetgroup  =bg.id AND (bg.ACCOUNTTYPE ='REVENUE_EXPENDITURE' OR bg.ACCOUNTTYPE   ='CAPITAL_EXPENDITURE') AND ((cao.id BETWEEN bg.mincode AND bg.maxcode) OR cao.majorcode =bg.majorcode) AND bg.mincode! =bg.maxcode AND bd.executing_department = "+budgetDetail.getExecutingDepartment().getId()+functionCondition+" AND (wf.value='END' OR wf.owner="+pos.getId()+") AND bd.state_id = wf.id GROUP BY cao.majorcode");

        query = query.append(" UNION ");

        query = query.append("SELECT cao.majorcode, SUM(bd.approvedamount) FROM egf_budgetdetail bd, egf_budgetgroup bg, egf_budget b, chartofaccounts cao, financialyear f, eg_wf_states wf" +
                " WHERE bd.budget =b.id AND f.id ="+topBudget.getFinancialYear().getId()+" AND b.financialyearid="+topBudget.getFinancialYear().getId()+" AND b.MATERIALIZEDPATH LIKE '"+topBudget.getMaterializedPath()+"%' AND bd.budgetgroup  =bg.id AND (bg.ACCOUNTTYPE ='REVENUE_RECEIPTS' OR bg.ACCOUNTTYPE ='CAPITAL_RECEIPTS') AND ((cao.id BETWEEN bg.mincode AND bg.maxcode) OR cao.majorcode =bg.majorcode) AND bg.mincode! =bg.maxcode AND bd.executing_department = "+budgetDetail.getExecutingDepartment().getId()+functionCondition+" AND (wf.value='END' OR wf.owner="+pos.getId()+") AND bd.state_id = wf.id GROUP BY cao.majorcode");

        query = query.append(" UNION ");*/

        query = query.append("SELECT cao.majorcode, SUM(bd.approvedamount) FROM egf_budgetdetail bd, egf_budgetgroup bg, egf_budget b, chartofaccounts cao, financialyear f, eg_wf_states wf" +
                " WHERE bd.budget =b.id AND f.id ="+topBudget.getFinancialYear().getId()+" AND b.financialyearid="+topBudget.getFinancialYear().getId()+" AND b.MATERIALIZEDPATH LIKE '"+topBudget.getMaterializedPath()+"%' AND bd.budgetgroup =bg.id  AND cao.id =bg.mincode AND cao.id =bg.maxcode AND bg.majorcode IS NULL AND bd.executing_department = "+budgetDetail.getExecutingDepartment().getId()+functionCondition+" AND (wf.value='END' OR wf.owner="+pos.getId()+") AND bd.state_id = wf.id GROUP BY cao.majorcode");

        /*query = query.append(" UNION ");

        query = query.append("SELECT cao.majorcode, SUM(bd.approvedamount) FROM egf_budgetdetail bd, egf_budgetgroup bg, egf_budget b, chartofaccounts cao, financialyear f, eg_wf_states wf" +
                " WHERE bd.budget =b.id AND f.id ="+topBudget.getFinancialYear().getId()+" AND b.financialyearid="+topBudget.getFinancialYear().getId()+" AND b.MATERIALIZEDPATH LIKE '"+topBudget.getMaterializedPath()+"%' AND bd.budgetgroup =bg.id AND (bg.ACCOUNTTYPE ='REVENUE_RECEIPTS' OR bg.ACCOUNTTYPE ='CAPITAL_RECEIPTS') AND cao.id =bg.mincode AND cao.id =bg.maxcode AND bg.majorcode IS NULL AND bd.executing_department = "+budgetDetail.getExecutingDepartment().getId()+functionCondition+" AND (wf.value='END' OR wf.owner="+pos.getId()+") AND bd.state_id = wf.id GROUP BY cao.majorcode");
        */
        List<Object[]> result =null;//HibernateUtil.getCurrentSession().createSQLQuery(query.toString()).list();
        if(LOGGER.isInfoEnabled())     LOGGER.info("Finished fetchMajorCodeAndApprovedAmount");

        return result;
    }

    public List<Object[]> fetchMajorCodeAndBENextYrApproved(Budget topBudget, BudgetDetail budgetDetail, CFunction function, Position pos){
        if(LOGGER.isInfoEnabled())     LOGGER.info("Starting fetchMajorCodeAndBENextYrApproved................");
        StringBuffer query = new StringBuffer();
        String functionCondition1 = "";
        String functionCondition2 = "";
        if(function != null){
            functionCondition1 = " AND bd1.function = "+function.getId();
            functionCondition2 = " AND bd2.function = "+function.getId();
        }
        /*query = query.append("SELECT cao.majorcode, SUM(bd2.approvedamount) FROM egf_budgetdetail bd1, egf_budgetdetail bd2, egf_budgetgroup bg, egf_budget b1, egf_budget b2, chartofaccounts cao, eg_wf_states wf" +
                " WHERE bd1.budget =b1.id AND bd2.budget =b2.id AND b1.financialyearid="+topBudget.getFinancialYear().getId()+" AND b1.MATERIALIZEDPATH LIKE '"+topBudget.getMaterializedPath()+"%' AND bd2.budgetgroup =bg.id AND (bg.ACCOUNTTYPE ='REVENUE_EXPENDITURE' OR bg.ACCOUNTTYPE ='CAPITAL_EXPENDITURE')" +
                " AND ((cao.id BETWEEN bg.mincode AND bg.maxcode) OR cao.majorcode   =bg.majorcode) AND bd2.executing_department = "+budgetDetail.getExecutingDepartment().getId()+functionCondition2+" AND bd1.executing_department = "+budgetDetail.getExecutingDepartment().getId()+functionCondition1+" AND bd1.uniqueno = bd2.uniqueno AND b2.reference_budget = b1.id AND (wf.value='END' OR wf.owner="+pos.getId()+") AND bd1.state_id = wf.id GROUP BY cao.majorcode");

        query = query.append(" UNION ");

        query = query.append("SELECT cao.majorcode, SUM(bd2.approvedamount) FROM egf_budgetdetail bd1, egf_budgetdetail bd2, egf_budgetgroup bg, egf_budget b1, egf_budget b2, chartofaccounts cao, eg_wf_states wf" +
                " WHERE bd1.budget =b1.id AND bd2.budget =b2.id AND b1.financialyearid="+topBudget.getFinancialYear().getId()+" AND b1.MATERIALIZEDPATH LIKE '"+topBudget.getMaterializedPath()+"%' AND bd2.budgetgroup =bg.id AND (bg.ACCOUNTTYPE ='REVENUE_RECEIPTS' OR bg.ACCOUNTTYPE ='CAPITAL_RECEIPTS')" +
                " AND ((cao.id BETWEEN bg.mincode AND bg.maxcode) OR cao.majorcode   =bg.majorcode) AND bd2.executing_department = "+budgetDetail.getExecutingDepartment().getId()+functionCondition2+" AND bd1.executing_department = "+budgetDetail.getExecutingDepartment().getId()+functionCondition1+" AND bd1.uniqueno = bd2.uniqueno AND b2.reference_budget = b1.id AND (wf.value='END' OR wf.owner="+pos.getId()+") AND bd1.state_id = wf.id GROUP BY cao.majorcode");

        query = query.append(" UNION ");*/

        query = query.append("SELECT cao.majorcode, SUM(bd2.approvedamount) FROM egf_budgetdetail bd1, egf_budgetdetail bd2, egf_budgetgroup bg, egf_budget b1, egf_budget b2, chartofaccounts cao, eg_wf_states wf" +
                " WHERE bd1.budget =b1.id AND bd2.budget =b2.id AND b1.financialyearid="+topBudget.getFinancialYear().getId()+" AND b1.MATERIALIZEDPATH LIKE '"+topBudget.getMaterializedPath()+"%' AND bd2.budgetgroup =bg.id " +
                " AND cao.id =bg.mincode AND cao.id =bg.maxcode AND bg.majorcode IS NULL AND bd2.executing_department = "+budgetDetail.getExecutingDepartment().getId()+functionCondition2+" AND bd1.executing_department = "+budgetDetail.getExecutingDepartment().getId()+functionCondition1+" AND bd1.uniqueno = bd2.uniqueno AND b2.reference_budget = b1.id AND (wf.value='END' OR wf.owner="+pos.getId()+") AND bd1.state_id = wf.id GROUP BY cao.majorcode");

        /*query = query.append(" UNION ");

        query = query.append("SELECT cao.majorcode, SUM(bd2.approvedamount) FROM egf_budgetdetail bd1, egf_budgetdetail bd2, egf_budgetgroup bg, egf_budget b1, egf_budget b2, chartofaccounts cao, eg_wf_states wf" +
                " WHERE bd1.budget =b1.id AND bd2.budget =b2.id AND b1.financialyearid="+topBudget.getFinancialYear().getId()+" AND b1.MATERIALIZEDPATH LIKE '"+topBudget.getMaterializedPath()+"%' AND bd2.budgetgroup =bg.id AND (bg.ACCOUNTTYPE ='REVENUE_RECEIPTS' OR bg.ACCOUNTTYPE ='CAPITAL_RECEIPTS')" +
                " AND cao.id =bg.mincode AND cao.id =bg.maxcode AND bg.majorcode IS NULL AND bd2.executing_department = "+budgetDetail.getExecutingDepartment().getId()+functionCondition2+" AND bd1.executing_department = "+budgetDetail.getExecutingDepartment().getId()+functionCondition1+" AND bd1.uniqueno = bd2.uniqueno AND b2.reference_budget = b1.id AND (wf.value='END' OR wf.owner="+pos.getId()+") AND bd1.state_id = wf.id GROUP BY cao.majorcode");
        */
        List<Object[]> result =null;//HibernateUtil.getCurrentSession().createSQLQuery(query.toString()).list();
        if(LOGGER.isInfoEnabled())     LOGGER.info("Finished fetchMajorCodeAndBENextYrApproved");

        return result;
    }


    //For Consolidate Budget Report.
    public List<Object[]> fetchMajorCodeAndNameForReport(CFinancialYear financialYear, String fundType, String budgetType){
        if(LOGGER.isInfoEnabled())     LOGGER.info("Starting fetchMajorCodeAndName............");
        String budgetingType=fundType.toUpperCase()+"_"+budgetType.toUpperCase();
        String excludeDept=" and bd.executing_department!=(Select id_dept from eg_department where dept_code='Z') ";
        StringBuffer query = new StringBuffer();
        query = query.append("SELECT cao.majorcode, cao1.glcode||'-'||cao1.name FROM egf_budgetdetail bd, egf_budgetgroup bg, egf_budget b, chartofaccounts cao, chartofaccounts cao1, financialyear f, eg_wf_states wf" +
                " WHERE bd.budget=b.id AND b.isbere='RE' AND f.id="+financialYear.getId()+" AND b.financialyearid="+financialYear.getId()+" AND bd.budgetgroup =bg.id AND bg.ACCOUNTTYPE ='" +budgetingType+"'"+excludeDept+
                " AND ((cao.id BETWEEN bg.mincode AND bg.maxcode) OR cao.majorcode=bg.majorcode) AND bg.mincode!=bg.maxcode AND wf.value='END' AND bd.state_id = wf.id GROUP BY cao.majorcode, cao1.glcode||'-'||cao1.name");

        query = query.append(" UNION ");

        query = query.append("SELECT cao.majorcode, cao1.glcode||'-'||cao1.name FROM egf_budgetdetail bd, egf_budgetgroup bg, egf_budget b, chartofaccounts cao, chartofaccounts cao1, financialyear f, eg_wf_states wf" +
                " WHERE bd.budget=b.id AND b.isbere='RE' AND f.id="+financialYear.getId()+" AND b.financialyearid="+financialYear.getId()+" AND bd.budgetgroup=bg.id AND bg.ACCOUNTTYPE ='" +budgetingType+"'"+excludeDept+
                " AND cao.id=bg.mincode AND cao.id=bg.maxcode AND bg.majorcode IS NULL and cao1.glcode = cao.majorcode AND wf.value='END' AND bd.state_id = wf.id GROUP BY cao.majorcode, cao1.glcode||'-'||cao1.name");

        List<Object[]> result =null;//HibernateUtil.getCurrentSession().createSQLQuery(query.toString()).list();
        if(LOGGER.isInfoEnabled())     LOGGER.info("Finished fetchMajorCodeAndName");

        return result;
    }

    //For Consolidated Budget Report
    public List<Object[]> fetchMajorCodeAndActualsForReport(CFinancialYear financialYear, CFinancialYear prevFinYear, String fundType, String budgetType){
        if(LOGGER.isInfoEnabled())     LOGGER.info("Starting fetchMajorCodeAndActuals................");
        String excludeDept=" and bd.executing_department!=(Select id_dept from eg_department where dept_code='Z') ";
        String budgetingType=fundType.toUpperCase()+"_"+budgetType.toUpperCase();
        String condition=" SUM(gl.debitAmount)-SUM(gl.creditAmount) ";
        if(budgetingType.contains("RECEIPT"))
        {
            condition=" SUM(gl.creditAmount)-SUM(gl.debitAmount) ";
        }
        StringBuffer query = new StringBuffer();
        List<AppConfigValues> list = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(Constants.EGF,"exclude_status_forbudget_actual");
        if(list.isEmpty())
            throw new ValidationException("","exclude_status_forbudget_actual is not defined in AppConfig");
        String voucherstatusExclude = ((AppConfigValues)list.get(0)).getValue();
        query = query.append("SELECT substr(gl.glcode,1,3), "+condition+" FROM egf_budgetdetail bd, vouchermis vmis," +
                " (SELECT bg1.id AS id, bg1.accounttype AS accounttype, DECODE(c1.glcode,NULL,-1,to_number(c1.glcode)) AS mincode, DECODE(c2.glcode,NULL,999999999,c2.glcode) AS maxcode, DECODE(c3.glcode,NULL,-1,to_number(c3.glcode)) AS majorcode" +
                " FROM egf_budgetgroup bg1 LEFT OUTER JOIN chartofaccounts c1 ON c1.id=bg1.mincode LEFT OUTER JOIN chartofaccounts c2 ON c2.id=bg1.maxcode LEFT OUTER JOIN chartofaccounts c3 ON c3.id=bg1.majorcode) bg ," +
                " egf_budget b, financialyear f, fiscalperiod p, voucherheader vh, generalledger gl, eg_wf_states wf" +
                " WHERE bd.budget =b.id AND b.isbere='RE' AND p.financialyearid=f.id AND f.id ="+prevFinYear.getId()+" AND vh.fiscalperiodid=p.id AND b.financialyearid="+financialYear.getId()+" AND vmis.VOUCHERHEADERID=vh.id AND gl.VOUCHERHEADERID  =vh.id" +
                " AND bd.budgetgroup =bg.id AND bg.ACCOUNTTYPE ='"+budgetingType+"'"+excludeDept+" AND vh.status NOT IN ("+voucherstatusExclude+") AND vh.fundId =bd.fund AND vmis.departmentid =bd.executing_department AND gl.functionid = bd.function " +
                " AND ((gl.glcode BETWEEN bg.mincode AND bg.maxcode) OR gl.glcode =bg.majorcode) AND bg.mincode!=bg.maxcode AND wf.value='END' AND bd.state_id = wf.id GROUP BY substr(gl.glcode,1,3)");

        query = query.append(" UNION ");

        query = query.append("SELECT substr(gl.glcode,1,3), "+condition+" FROM egf_budgetdetail bd, vouchermis vmis, egf_budgetgroup bg, egf_budget b, financialyear f, fiscalperiod p, voucherheader vh, generalledger gl, eg_wf_states wf" +
                " WHERE bd.budget      =b.id AND b.isbere='RE' AND p.financialyearid=f.id AND f.id             ="+prevFinYear.getId()+" AND vh.fiscalperiodid=p.id AND b.financialyearid="+financialYear.getId()+" AND vmis.VOUCHERHEADERID=vh.id AND gl.VOUCHERHEADERID  =vh.id" +
                " AND bd.budgetgroup      =bg.id AND bg.ACCOUNTTYPE ='"+budgetingType+"'"+excludeDept+" AND vh.status NOT      IN ("+voucherstatusExclude+") AND vh.fundId           =bd.fund AND gl.functionid = bd.function " +
                " AND vmis.departmentid   =bd.executing_department AND gl.glcodeid         =bg.mincode AND gl.glcodeid =bg.maxcode AND bg.majorcode       IS NULL AND wf.value='END' AND bd.state_id = wf.id GROUP BY substr(gl.glcode,1,3)");

        List<Object[]> result =null;//HibernateUtil.getCurrentSession().createSQLQuery(query.toString()).list();
        if(LOGGER.isInfoEnabled())     LOGGER.info("Finished fetchMajorCodeAndActuals");

        return result;
    }

    //For Consolidated Budget Report
    public List<Object[]> fetchMajorCodeAndBEAmountForReport(CFinancialYear financialYear, String fundType, String budgetType){
        if(LOGGER.isInfoEnabled())     LOGGER.info("Starting fetchMajorCodeAndBEAmount................");
        String excludeDept=" and bd2.executing_department!=(Select id_dept from eg_department where dept_code='Z') ";
        String budgetingType=fundType.toUpperCase()+"_"+budgetType.toUpperCase();
        StringBuffer query = new StringBuffer();

        query = query.append("SELECT cao.majorcode, SUM(round(bd2.approvedamount/1000,0)) FROM egf_budgetdetail bd1, egf_budgetdetail bd2, egf_budgetgroup bg, egf_budget b1, egf_budget b2, chartofaccounts cao, financialyear f, eg_wf_states wf" +
                " WHERE bd1.budget =b1.id AND bd2.budget =b2.id AND b1.isbere='RE' AND b2.isbere='BE' AND f.id ="+financialYear.getId()+" AND b1.financialyearid="+financialYear.getId()+" AND b2.financialyearid="+financialYear.getId()+" AND bd2.budgetgroup =bg.id AND bg.ACCOUNTTYPE ='"+budgetingType+"'"+excludeDept+
                " AND ((cao.id BETWEEN bg.mincode AND bg.maxcode) OR cao.majorcode   =bg.majorcode) AND bd1.uniqueno = bd2.uniqueno AND wf.value='END' AND bd1.state_id = wf.id GROUP BY cao.majorcode");

        query = query.append(" UNION ");

        query = query.append("SELECT cao.majorcode, SUM(round(bd2.approvedamount/1000,0)) FROM egf_budgetdetail bd1, egf_budgetdetail bd2, egf_budgetgroup bg, egf_budget b1, egf_budget b2, chartofaccounts cao, financialyear f, eg_wf_states wf" +
                " WHERE bd1.budget =b1.id AND bd2.budget =b2.id AND b1.isbere='RE' AND b2.isbere='BE' AND f.id ="+financialYear.getId()+" AND b1.financialyearid="+financialYear.getId()+" AND b2.financialyearid="+financialYear.getId()+"  AND bd2.budgetgroup =bg.id AND bg.ACCOUNTTYPE ='"+budgetingType+"'"+excludeDept+
                " AND cao.id =bg.mincode AND cao.id =bg.maxcode AND bg.majorcode IS NULL AND bd1.uniqueno = bd2.uniqueno AND wf.value='END' AND bd1.state_id = wf.id GROUP BY cao.majorcode");

        List<Object[]> result =null;//HibernateUtil.getCurrentSession().createSQLQuery(query.toString()).list();
        if(LOGGER.isInfoEnabled()) LOGGER.info("------------------------------------------------------------------------------------------------------");
        if(LOGGER.isInfoEnabled())     LOGGER.info("Finished fetchMajorCodeAndBEAmount"+query.toString());
        if(LOGGER.isInfoEnabled()) LOGGER.info("------------------------------------------------------------------------------------------------------");

        return result;
    }

    //For Consolidated Budget Report
    public List<Object[]> fetchMajorCodeAndApprovedAmountForReport(CFinancialYear financialYear, String fundType, String budgetType){
        if(LOGGER.isInfoEnabled())     LOGGER.info("Starting fetchMajorCodeAndApprovedAmount................");
        String excludeDept=" and bd.executing_department!=(Select id_dept from eg_department where dept_code='Z') ";
        String budgetingType=fundType.toUpperCase()+"_"+budgetType.toUpperCase();
        StringBuffer query = new StringBuffer();

        query = query.append("SELECT cao.majorcode, SUM(round(bd.approvedamount/1000,0)) FROM egf_budgetdetail bd, egf_budgetgroup bg, egf_budget b, chartofaccounts cao, financialyear f, eg_wf_states wf" +
                " WHERE bd.budget =b.id AND b.isbere='RE' AND f.id ="+financialYear.getId()+" AND b.financialyearid="+financialYear.getId()+" AND bd.budgetgroup =bg.id AND bg.ACCOUNTTYPE ='"+budgetingType+"'"+excludeDept+" AND ((cao.id BETWEEN bg.mincode AND bg.maxcode) OR cao.majorcode =bg.majorcode) AND bg.mincode! =bg.maxcode AND wf.value='END' AND bd.state_id = wf.id GROUP BY cao.majorcode");

        query = query.append(" UNION ");

        query = query.append("SELECT cao.majorcode, SUM(round(bd.approvedamount/1000,0)) FROM egf_budgetdetail bd, egf_budgetgroup bg, egf_budget b, chartofaccounts cao, financialyear f, eg_wf_states wf" +
                " WHERE bd.budget =b.id AND b.isbere='RE' AND f.id ="+financialYear.getId()+" AND b.financialyearid="+financialYear.getId()+" AND bd.budgetgroup =bg.id AND bg.ACCOUNTTYPE ='"+budgetingType+"'"+excludeDept+" AND cao.id =bg.mincode AND cao.id =bg.maxcode AND bg.majorcode IS NULL AND wf.value='END' AND bd.state_id = wf.id GROUP BY cao.majorcode");

        List<Object[]> result =null;//HibernateUtil.getCurrentSession().createSQLQuery(query.toString()).list();
        if(LOGGER.isInfoEnabled())     LOGGER.info("Finished fetchMajorCodeAndApprovedAmount");

        return result;
    }

    //For Consolidated Budget Report
    public List<Object[]> fetchMajorCodeAndBENextYrApprovedForReport(CFinancialYear financialYear, String fundType, String budgetType){
        if(LOGGER.isInfoEnabled())     LOGGER.info("Starting fetchMajorCodeAndBENextYrApproved................");
        String excludeDept=" and bd2.executing_department!=(Select id_dept from eg_department where dept_code='Z') ";
        String budgetingType=fundType.toUpperCase()+"_"+budgetType.toUpperCase();
        StringBuffer query = new StringBuffer();

        query = query.append("SELECT cao.majorcode, SUM(round(bd2.approvedamount/1000,0)) FROM egf_budgetdetail bd1, egf_budgetdetail bd2, egf_budgetgroup bg, egf_budget b1, egf_budget b2, chartofaccounts cao, eg_wf_states wf" +
                " WHERE bd1.budget =b1.id AND bd2.budget =b2.id AND b1.isbere='RE' AND b2.isbere='BE' AND b1.financialyearid="+financialYear.getId()+" AND bd2.budgetgroup =bg.id AND bg.ACCOUNTTYPE ='"+budgetingType+"'"+excludeDept+
                " AND ((cao.id BETWEEN bg.mincode AND bg.maxcode) OR cao.majorcode   =bg.majorcode) AND bd1.uniqueno = bd2.uniqueno AND b2.reference_budget = b1.id AND wf.value='END' AND bd1.state_id = wf.id GROUP BY cao.majorcode");

        query = query.append(" UNION ");

        query = query.append("SELECT cao.majorcode, SUM(round(bd2.approvedamount/1000,0)) FROM egf_budgetdetail bd1, egf_budgetdetail bd2, egf_budgetgroup bg, egf_budget b1, egf_budget b2, chartofaccounts cao, eg_wf_states wf" +
                " WHERE bd1.budget =b1.id AND bd2.budget =b2.id AND b1.isbere='RE' AND b2.isbere='BE' AND b1.financialyearid="+financialYear.getId()+" AND bd2.budgetgroup =bg.id AND bg.ACCOUNTTYPE ='"+budgetingType+"'"+excludeDept+
                " AND cao.id =bg.mincode AND cao.id =bg.maxcode AND bg.majorcode IS NULL AND bd1.uniqueno = bd2.uniqueno AND b2.reference_budget = b1.id AND wf.value='END' AND bd1.state_id = wf.id GROUP BY cao.majorcode");

        List<Object[]> result =null;//HibernateUtil.getCurrentSession().createSQLQuery(query.toString()).list();
        if(LOGGER.isInfoEnabled())     LOGGER.info("Finished fetchMajorCodeAndBENextYrApproved");

        return result;
    }

    //For Consolidate Budget Report.
    public List<Object[]> fetchGlCodeAndNameForReport(CFinancialYear financialYear, String fundType, String budgetType){
        if(LOGGER.isInfoEnabled())     LOGGER.info("Starting fetchGlCodeAndNameForReport............");
        String excludeDept=" and bd.executing_department!=(Select id_dept from eg_department where dept_code='Z') ";
        String budgetingType=fundType.toUpperCase()+"_"+budgetType.toUpperCase();
        StringBuffer query = new StringBuffer();
        query = query.append("SELECT substr(cao.glcode,0,3)||'-'||substr(cao.glcode,4,2)||'-'||substr(cao.glcode,6,2)||'-'||substr(cao.glcode,8,2), cao.glcode||'-'||cao.name FROM egf_budgetdetail bd, egf_budgetgroup bg, egf_budget b, chartofaccounts cao, chartofaccounts cao1, financialyear f, eg_wf_states wf" +
                " WHERE bd.budget=b.id AND b.isbere='RE' AND f.id="+financialYear.getId()+" AND b.financialyearid="+financialYear.getId()+" AND bd.budgetgroup =bg.id AND bg.ACCOUNTTYPE ='" +budgetingType+"'"+excludeDept+
                " AND ((cao.id BETWEEN bg.mincode AND bg.maxcode) OR cao.majorcode=bg.majorcode) AND bg.mincode!=bg.maxcode AND wf.value='END' AND bd.state_id = wf.id GROUP BY substr(cao.glcode,0,3)||'-'||substr(cao.glcode,4,2)||'-'||substr(cao.glcode,6,2)||'-'||substr(cao.glcode,8,2), cao.glcode||'-'||cao.name");

        query = query.append(" UNION ");

        query = query.append("SELECT substr(cao.glcode,0,3)||'-'||substr(cao.glcode,4,2)||'-'||substr(cao.glcode,6,2)||'-'||substr(cao.glcode,8,2), cao.glcode||'-'||cao.name FROM egf_budgetdetail bd, egf_budgetgroup bg, egf_budget b, chartofaccounts cao, chartofaccounts cao1, financialyear f, eg_wf_states wf" +
                " WHERE bd.budget=b.id AND b.isbere='RE' AND f.id="+financialYear.getId()+" AND b.financialyearid="+financialYear.getId()+" AND bd.budgetgroup=bg.id AND bg.ACCOUNTTYPE ='" +budgetingType+"'"+excludeDept+
                " AND cao.id=bg.mincode AND cao.id=bg.maxcode AND bg.majorcode IS NULL and cao1.glcode = cao.majorcode AND wf.value='END' AND bd.state_id = wf.id GROUP BY substr(cao.glcode,0,3)||'-'||substr(cao.glcode,4,2)||'-'||substr(cao.glcode,6,2)||'-'||substr(cao.glcode,8,2), cao.glcode||'-'||cao.name");

        List<Object[]> result =null;//HibernateUtil.getCurrentSession().createSQLQuery(query.toString()).list();
        if(LOGGER.isInfoEnabled())     LOGGER.info("Finished fetchGlCodeAndNameForReport");

        return result;
    }

    //For Consolidated Budget Report
    public List<Object[]> fetchActualsForReport(CFinancialYear financialYear, CFinancialYear prevFinYear, String fundType, String budgetType){
        if(LOGGER.isInfoEnabled())     LOGGER.info("Starting fetchActualsForReport................");
        String excludeDept=" and bd.executing_department!=(Select id_dept from eg_department where dept_code='Z') ";
        String budgetingType=fundType.toUpperCase()+"_"+budgetType.toUpperCase();
        String condition=" SUM(gl.debitAmount)-SUM(gl.creditAmount) ";
        if(budgetingType.contains("RECEIPT"))
        {
            condition=" SUM(gl.creditAmount)-SUM(gl.debitAmount) ";
        }
        StringBuffer query = new StringBuffer();
        List<AppConfigValues> list = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(Constants.EGF,"exclude_status_forbudget_actual");
        if(list.isEmpty())
            throw new ValidationException("","exclude_status_forbudget_actual is not defined in AppConfig");
        String voucherstatusExclude = ((AppConfigValues)list.get(0)).getValue();
        query = query.append("SELECT substr(gl.glcode,0,3)||'-'||substr(gl.glcode,4,2)||'-'||substr(gl.glcode,6,2)||'-'||substr(gl.glcode,8,2),"+condition+" FROM egf_budgetdetail bd, vouchermis vmis," +
                " (SELECT bg1.id AS id, bg1.accounttype AS accounttype, DECODE(c1.glcode,NULL,-1,to_number(c1.glcode)) AS mincode, DECODE(c2.glcode,NULL,999999999,c2.glcode) AS maxcode, DECODE(c3.glcode,NULL,-1,to_number(c3.glcode)) AS majorcode" +
                " FROM egf_budgetgroup bg1 LEFT OUTER JOIN chartofaccounts c1 ON c1.id=bg1.mincode LEFT OUTER JOIN chartofaccounts c2 ON c2.id=bg1.maxcode LEFT OUTER JOIN chartofaccounts c3 ON c3.id=bg1.majorcode) bg ," +
                " egf_budget b, financialyear f, fiscalperiod p, voucherheader vh, generalledger gl, eg_wf_states wf" +
                " WHERE bd.budget =b.id AND b.isbere='RE' AND p.financialyearid=f.id AND f.id ="+prevFinYear.getId()+" AND vh.fiscalperiodid=p.id AND b.financialyearid="+financialYear.getId()+" AND vmis.VOUCHERHEADERID=vh.id AND gl.VOUCHERHEADERID  =vh.id" +
                " AND bd.budgetgroup =bg.id AND bg.ACCOUNTTYPE ='"+budgetingType+"'"+excludeDept+" AND vh.status NOT IN ("+voucherstatusExclude+") AND vh.fundId =bd.fund AND vmis.departmentid =bd.executing_department AND gl.functionid = bd.function " +
                " AND ((gl.glcode BETWEEN bg.mincode AND bg.maxcode) OR gl.glcode =bg.majorcode) AND bg.mincode!=bg.maxcode AND wf.value='END' AND bd.state_id = wf.id GROUP BY substr(gl.glcode,0,3)||'-'||substr(gl.glcode,4,2)||'-'||substr(gl.glcode,6,2)||'-'||substr(gl.glcode,8,2)");

        query = query.append(" UNION ");

        query = query.append("SELECT substr(gl.glcode,0,3)||'-'||substr(gl.glcode,4,2)||'-'||substr(gl.glcode,6,2)||'-'||substr(gl.glcode,8,2),"+condition+" FROM egf_budgetdetail bd, vouchermis vmis, egf_budgetgroup bg, egf_budget b, financialyear f, fiscalperiod p, voucherheader vh, generalledger gl, eg_wf_states wf" +
                " WHERE bd.budget      =b.id AND b.isbere='RE' AND p.financialyearid=f.id AND f.id             ="+prevFinYear.getId()+" AND vh.fiscalperiodid=p.id AND b.financialyearid="+financialYear.getId()+" AND vmis.VOUCHERHEADERID=vh.id AND gl.VOUCHERHEADERID  =vh.id" +
                " AND bd.budgetgroup      =bg.id AND bg.ACCOUNTTYPE ='"+budgetingType+"'"+excludeDept+" AND vh.status NOT      IN ("+voucherstatusExclude+") AND vh.fundId           =bd.fund AND gl.functionid = bd.function " +
                " AND vmis.departmentid   =bd.executing_department AND gl.glcodeid         =bg.mincode AND gl.glcodeid =bg.maxcode AND bg.majorcode       IS NULL AND wf.value='END' AND bd.state_id = wf.id GROUP BY substr(gl.glcode,0,3)||'-'||substr(gl.glcode,4,2)||'-'||substr(gl.glcode,6,2)||'-'||substr(gl.glcode,8,2)");

        List<Object[]> result =null;//HibernateUtil.getCurrentSession().createSQLQuery(query.toString()).list();
        if(LOGGER.isInfoEnabled())     LOGGER.info("Finished fetchActualsForReport");

        return result;
    }

    //For Consolidated Budget Report
    public List<Object[]> fetchGlCodeAndBEAmountForReport(CFinancialYear financialYear, String fundType, String budgetType){
        if(LOGGER.isInfoEnabled())     LOGGER.info("Starting fetchGlCodeAndBEAmountForReport................");
        String excludeDept=" and bd2.executing_department!=(Select id_dept from eg_department where dept_code='Z') ";
        String budgetingType=fundType.toUpperCase()+"_"+budgetType.toUpperCase();
        StringBuffer query = new StringBuffer();

        query = query.append("SELECT substr(cao.glcode,0,3)||'-'||substr(cao.glcode,4,2)||'-'||substr(cao.glcode,6,2)||'-'||substr(cao.glcode,8,2), SUM(round(bd2.approvedamount/1000,0)) FROM egf_budgetdetail bd1, egf_budgetdetail bd2, egf_budgetgroup bg, egf_budget b1, egf_budget b2, chartofaccounts cao, financialyear f, eg_wf_states wf" +
                " WHERE bd1.budget =b1.id AND bd2.budget =b2.id AND b1.isbere='RE' AND b2.isbere='BE' AND f.id ="+financialYear.getId()+" AND b1.financialyearid="+financialYear.getId()+" AND b2.financialyearid="+financialYear.getId()+" AND bd2.budgetgroup =bg.id AND bg.ACCOUNTTYPE ='"+budgetingType+"'"+excludeDept+
                " AND ((cao.id BETWEEN bg.mincode AND bg.maxcode) OR cao.majorcode   =bg.majorcode) AND bd1.uniqueno = bd2.uniqueno AND wf.value='END' AND bd1.state_id = wf.id GROUP BY substr(cao.glcode,0,3)||'-'||substr(cao.glcode,4,2)||'-'||substr(cao.glcode,6,2)||'-'||substr(cao.glcode,8,2)");

        query = query.append(" UNION ");

        query = query.append("SELECT substr(cao.glcode,0,3)||'-'||substr(cao.glcode,4,2)||'-'||substr(cao.glcode,6,2)||'-'||substr(cao.glcode,8,2), SUM(round(bd2.approvedamount/1000,0)) FROM egf_budgetdetail bd1, egf_budgetdetail bd2, egf_budgetgroup bg, egf_budget b1, egf_budget b2, chartofaccounts cao, financialyear f, eg_wf_states wf" +
                " WHERE bd1.budget =b1.id AND bd2.budget =b2.id AND b1.isbere='RE' AND b2.isbere='BE' AND f.id ="+financialYear.getId()+" AND b1.financialyearid="+financialYear.getId()+" AND b2.financialyearid="+financialYear.getId()+"  AND bd2.budgetgroup =bg.id AND bg.ACCOUNTTYPE ='"+budgetingType+"'"+excludeDept+
                " AND cao.id =bg.mincode AND cao.id =bg.maxcode AND bg.majorcode IS NULL AND bd1.uniqueno = bd2.uniqueno AND wf.value='END' AND bd1.state_id = wf.id GROUP BY substr(cao.glcode,0,3)||'-'||substr(cao.glcode,4,2)||'-'||substr(cao.glcode,6,2)||'-'||substr(cao.glcode,8,2)");

        List<Object[]> result =null;//HibernateUtil.getCurrentSession().createSQLQuery(query.toString()).list();
        if(LOGGER.isInfoEnabled()) LOGGER.info("------------------------------------------------------------------------------------------------------");
        if(LOGGER.isInfoEnabled())     LOGGER.info("Finished fetchGlCodeAndBEAmountForReport"+query.toString());
        if(LOGGER.isInfoEnabled()) LOGGER.info("------------------------------------------------------------------------------------------------------");

        return result;
    }

    //For Consolidated Budget Report
    public List<Object[]> fetchGlCodeAndApprovedAmountForReport(CFinancialYear financialYear, String fundType, String budgetType){
        if(LOGGER.isInfoEnabled())     LOGGER.info("Starting fetchGlCodeAndApprovedAmountForReport................");
        String excludeDept=" and bd.executing_department!=(Select id_dept from eg_department where dept_code='Z') ";
        String budgetingType=fundType.toUpperCase()+"_"+budgetType.toUpperCase();
        StringBuffer query = new StringBuffer();

        query = query.append("SELECT substr(cao.glcode,0,3)||'-'||substr(cao.glcode,4,2)||'-'||substr(cao.glcode,6,2)||'-'||substr(cao.glcode,8,2), SUM(round(bd.approvedamount/1000,0)) FROM egf_budgetdetail bd, egf_budgetgroup bg, egf_budget b, chartofaccounts cao, financialyear f, eg_wf_states wf" +
                " WHERE bd.budget =b.id AND b.isbere='RE' AND f.id ="+financialYear.getId()+" AND b.financialyearid="+financialYear.getId()+" AND bd.budgetgroup =bg.id AND bg.ACCOUNTTYPE ='"+budgetingType+"'"+excludeDept+" AND ((cao.id BETWEEN bg.mincode AND bg.maxcode) OR cao.majorcode =bg.majorcode) AND bg.mincode! =bg.maxcode AND wf.value='END' AND bd.state_id = wf.id GROUP BY substr(cao.glcode,0,3)||'-'||substr(cao.glcode,4,2)||'-'||substr(cao.glcode,6,2)||'-'||substr(cao.glcode,8,2)");

        query = query.append(" UNION ");

        query = query.append("SELECT substr(cao.glcode,0,3)||'-'||substr(cao.glcode,4,2)||'-'||substr(cao.glcode,6,2)||'-'||substr(cao.glcode,8,2), SUM(round(bd.approvedamount/1000,0)) FROM egf_budgetdetail bd, egf_budgetgroup bg, egf_budget b, chartofaccounts cao, financialyear f, eg_wf_states wf" +
                " WHERE bd.budget =b.id AND b.isbere='RE' AND f.id ="+financialYear.getId()+" AND b.financialyearid="+financialYear.getId()+" AND bd.budgetgroup =bg.id AND bg.ACCOUNTTYPE ='"+budgetingType+"'"+excludeDept+" AND cao.id =bg.mincode AND cao.id =bg.maxcode AND bg.majorcode IS NULL AND wf.value='END' AND bd.state_id = wf.id GROUP BY substr(cao.glcode,0,3)||'-'||substr(cao.glcode,4,2)||'-'||substr(cao.glcode,6,2)||'-'||substr(cao.glcode,8,2)");

        List<Object[]> result =null;//HibernateUtil.getCurrentSession().createSQLQuery(query.toString()).list();
        if(LOGGER.isInfoEnabled())     LOGGER.info("Finished fetchGlCodeAndApprovedAmountForReport");

        return result;
    }

    //For Consolidated Budget Report
    public List<Object[]> fetchGlCodeAndBENextYrApprovedForReport(CFinancialYear financialYear, String fundType, String budgetType){
        if(LOGGER.isInfoEnabled())     LOGGER.info("Starting fetchGlCodeAndBENextYrApprovedForReport................");
        String excludeDept=" and bd2.executing_department!=(Select id_dept from eg_department where dept_code='Z') ";
        String budgetingType=fundType.toUpperCase()+"_"+budgetType.toUpperCase();
        StringBuffer query = new StringBuffer();

        query = query.append("SELECT substr(cao.glcode,0,3)||'-'||substr(cao.glcode,4,2)||'-'||substr(cao.glcode,6,2)||'-'||substr(cao.glcode,8,2), SUM(round(bd2.approvedamount/1000,0)) FROM egf_budgetdetail bd1, egf_budgetdetail bd2, egf_budgetgroup bg, egf_budget b1, egf_budget b2, chartofaccounts cao, eg_wf_states wf" +
                " WHERE bd1.budget =b1.id AND bd2.budget =b2.id AND b1.isbere='RE' AND b2.isbere='BE' AND b1.financialyearid="+financialYear.getId()+" AND bd2.budgetgroup =bg.id AND bg.ACCOUNTTYPE ='"+budgetingType+"'"+excludeDept+
                " AND ((cao.id BETWEEN bg.mincode AND bg.maxcode) OR cao.majorcode   =bg.majorcode) AND bd1.uniqueno = bd2.uniqueno AND b2.reference_budget = b1.id AND wf.value='END' AND bd1.state_id = wf.id GROUP BY substr(cao.glcode,0,3)||'-'||substr(cao.glcode,4,2)||'-'||substr(cao.glcode,6,2)||'-'||substr(cao.glcode,8,2)");

        query = query.append(" UNION ");

        query = query.append("SELECT substr(cao.glcode,0,3)||'-'||substr(cao.glcode,4,2)||'-'||substr(cao.glcode,6,2)||'-'||substr(cao.glcode,8,2), SUM(round(bd2.approvedamount/1000,0)) FROM egf_budgetdetail bd1, egf_budgetdetail bd2, egf_budgetgroup bg, egf_budget b1, egf_budget b2, chartofaccounts cao, eg_wf_states wf" +
                " WHERE bd1.budget =b1.id AND bd2.budget =b2.id AND b1.isbere='RE' AND b2.isbere='BE' AND b1.financialyearid="+financialYear.getId()+" AND bd2.budgetgroup =bg.id AND bg.ACCOUNTTYPE ='"+budgetingType+"'"+excludeDept+
                " AND cao.id =bg.mincode AND cao.id =bg.maxcode AND bg.majorcode IS NULL AND bd1.uniqueno = bd2.uniqueno AND b2.reference_budget = b1.id AND wf.value='END' AND bd1.state_id = wf.id GROUP BY substr(cao.glcode,0,3)||'-'||substr(cao.glcode,4,2)||'-'||substr(cao.glcode,6,2)||'-'||substr(cao.glcode,8,2)");

        List<Object[]> result =null;//HibernateUtil.getCurrentSession().createSQLQuery(query.toString()).list();
        if(LOGGER.isInfoEnabled())     LOGGER.info("Finished fetchGlCodeAndBENextYrApprovedForReport");

        return result;
    }

    public List<Object[]> fetchActualsForBill(String fromDate,String toVoucherDate,List<String> mandatoryFields) {
        StringBuffer miscQuery = getMiscQuery(mandatoryFields,"bmis","bdetail","bmis");
        StringBuffer query = new StringBuffer();
        query = query.append("select bd.id,SUM(decode(bdetail.debitAmount,null,0,bdetail.debitAmount))-SUM(decode(bdetail.creditAmount,null,0,bdetail.creditAmount)) from egf_budgetdetail bd,eg_billdetails bdetail, eg_billregistermis bmis, eg_billregister br," +
                "egf_budgetgroup bg where bmis.billid=br.id and bdetail.billid=br.id and bd.budgetgroup=bg.id and " +
                "(bg.ACCOUNTTYPE='REVENUE_EXPENDITURE' or bg.ACCOUNTTYPE='CAPITAL_EXPENDITURE') and br.billstatus != 'Cancelled'  and " +
                "bmis.voucherheaderid is null and br.billdate>=to_date('"+fromDate+"','dd/MM/yyyy') and br.billdate <= to_date("+toVoucherDate+",'dd/MM/yyyy') "+miscQuery+" and " +
                " (bmis.budgetCheckReq is null or bmis.budgetCheckReq=1) and "+
                "((bdetail.glcodeid between bg.mincode and bg.maxcode) or bdetail.glcodeid=bg.majorcode) group by bd.id"+
                " union "+
                "select bd.id,SUM(decode(bdetail.creditAmount,null,0,bdetail.creditAmount))-SUM(decode(bdetail.debitAmount,null,0,bdetail.debitAmount)) from egf_budgetdetail bd,eg_billdetails bdetail, eg_billregistermis bmis, eg_billregister br," +
                "egf_budgetgroup bg where bmis.billid=br.id and bdetail.billid=br.id and bd.budgetgroup=bg.id and " +
                " (bmis.budgetCheckReq is null or bmis.budgetCheckReq=1) and "+
                "(bg.ACCOUNTTYPE='REVENUE_RECEIPTS' or bg.ACCOUNTTYPE='CAPITAL_RECEIPTS') and br.billstatus != 'Cancelled' and bmis.voucherheaderid " +
                "is null and br.billdate>= to_date('"+fromDate+"','dd/MM/yyyy') and br.billdate <= to_date("+toVoucherDate+",'dd/MM/yyyy') "+miscQuery+" and ((bdetail.glcodeid between bg.mincode " +
                "and bg.maxcode) or bdetail.glcodeid=bg.majorcode) group by bd.id");
        List<Object[]> result =null;//HibernateUtil.getCurrentSession().createSQLQuery(query.toString()).list();
        return result;
    }
    public List<Object[]> fetchActualsForFYWithParams(String fromDate,String toVoucherDate,StringBuffer miscQuery) {
            List<AppConfigValues> list = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(Constants.EGF,"exclude_status_forbudget_actual");
            if(list.isEmpty())
                throw new ValidationException("","exclude_status_forbudget_actual is not defined in AppConfig");
            StringBuffer budgetGroupQuery = new StringBuffer();
            budgetGroupQuery.append(" (select bg1.id as id,bg1.accounttype as accounttype,decode(c1.glcode,null,-1,to_number(c1.glcode)) " +
                    "as mincode,decode(c2.glcode,null,999999999,c2.glcode) as maxcode,decode(c3.glcode,null,-1,to_number(c3.glcode)) as majorcode " +
                    "from egf_budgetgroup bg1 left outer join chartofaccounts c1 on c1.id=bg1.mincode left outer join chartofaccounts c2 on " +
            "c2.id=bg1.maxcode left outer join chartofaccounts  c3 on c3.id=bg1.majorcode ) bg ");
            String voucherstatusExclude = ((AppConfigValues)list.get(0)).getValue();
            StringBuffer query = new StringBuffer();
            query = query.append("select bd.id,SUM(gl.debitAmount)-SUM(gl.creditAmount) from egf_budgetdetail bd,generalledger gl,voucherheader vh," +
                    "vouchermis vmis,"+budgetGroupQuery+",egf_budget b where bd.budget=b.id and vmis.VOUCHERHEADERID=vh.id and gl.VOUCHERHEADERID=vh.id and bd.budgetgroup=bg.id and " +
                    "(bg.ACCOUNTTYPE='REVENUE_EXPENDITURE' or bg.ACCOUNTTYPE='CAPITAL_EXPENDITURE') and vh.status not in ("+voucherstatusExclude+") and (vmis.budgetary_appnumber  != 'null' and vmis.budgetary_appnumber is not null) and " +
                    "vh.voucherDate>= to_date('"+fromDate+"','dd/MM/yyyy') and vh.voucherDate <= to_date("+toVoucherDate+",'dd/MM/yyyy') "+miscQuery+" and ((gl.glcode between bg.mincode and bg.maxcode) or gl.glcode=bg.majorcode) group by bd.id"+
                        " union "+
                        "select bd.id,SUM(gl.creditAmount)-SUM(gl.debitAmount) from egf_budgetdetail bd,generalledger gl,voucherheader vh," +
                        "vouchermis vmis,"+budgetGroupQuery+",egf_budget b where bd.budget=b.id and vmis.VOUCHERHEADERID=vh.id and gl.VOUCHERHEADERID=vh.id and bd.budgetgroup=bg.id and " +
                        "(bg.ACCOUNTTYPE='REVENUE_RECEIPTS' or bg.ACCOUNTTYPE='CAPITAL_RECEIPTS') and vh.status not in ("+voucherstatusExclude+") and (vmis.budgetary_appnumber  != 'null' and vmis.budgetary_appnumber is not null) and " +
                        "vh.voucherDate>= to_date('"+fromDate+"','dd/MM/yyyy') and vh.voucherDate <= to_date("+toVoucherDate+",'dd/MM/yyyy') "+miscQuery+" and ((gl.glcode between bg.mincode and bg.maxcode) or gl.glcode=bg.majorcode) group by bd.id");
            List<Object[]> result =null;//HibernateUtil.getCurrentSession().createSQLQuery(query.toString()).list();

            return result;
    }
    public List<Object[]> fetchActualsForBillWithParams(String fromDate,String toVoucherDate,StringBuffer miscQuery) {
            StringBuffer query = new StringBuffer();
            query = query.append("select bud,sum(amt) from ("+
                    "select bd.id as bud,SUM(decode(bdetail.debitAmount,null,0,bdetail.debitAmount))-SUM(decode(bdetail.creditAmount,null,0,bdetail.creditAmount)) as amt from egf_budgetdetail bd,eg_billdetails bdetail, eg_billregistermis bmis, eg_billregister br," +
                    "egf_budgetgroup bg where bmis.billid=br.id and bdetail.billid=br.id and bd.budgetgroup=bg.id and " +
                    "(bg.ACCOUNTTYPE='REVENUE_EXPENDITURE' or bg.ACCOUNTTYPE='CAPITAL_EXPENDITURE') and br.statusid not in (select id from egw_status where description='Cancelled' and moduletype in ('EXPENSEBILL', 'SALBILL', 'WORKSBILL', 'PURCHBILL', 'CBILL', 'SBILL', 'CONTRACTORBILL'))  and " +
                    "bmis.voucherheaderid is null and br.billdate>=to_date('"+fromDate+"','dd/MM/yyyy') and br.billdate <= to_date("+toVoucherDate+",'dd/MM/yyyy') "+miscQuery+" and " +
                    " (bmis.budgetCheckReq is null or bmis.budgetCheckReq=1) and "+
                    "((bdetail.glcodeid between bg.mincode and bg.maxcode) or bdetail.glcodeid=bg.majorcode) group by bd.id"+
                    " union "+
                    "select bd.id as bud,SUM(decode(bdetail.debitAmount,null,0,bdetail.debitAmount))-SUM(decode(bdetail.creditAmount,null,0,bdetail.creditAmount)) as amt from egf_budgetdetail bd,eg_billdetails bdetail, eg_billregistermis bmis, eg_billregister br," +
                    "egf_budgetgroup bg,voucherheader vh where bmis.billid=br.id and bdetail.billid=br.id and bd.budgetgroup=bg.id and " +
                    "(bg.ACCOUNTTYPE='REVENUE_EXPENDITURE' or bg.ACCOUNTTYPE='CAPITAL_EXPENDITURE') and br.statusid not in (select id from egw_status where description='Cancelled' and moduletype in ('EXPENSEBILL', 'SALBILL', 'WORKSBILL', 'PURCHBILL', 'CBILL', 'SBILL', 'CONTRACTORBILL'))  and " +
                    "bmis.voucherheaderid =vh.id and vh.status=4 and br.billdate>=to_date('"+fromDate+"','dd/MM/yyyy') and br.billdate <= to_date("+toVoucherDate+",'dd/MM/yyyy') "+miscQuery+" and " +
                    " (bmis.budgetCheckReq is null or bmis.budgetCheckReq=1) and "+
                    "((bdetail.glcodeid between bg.mincode and bg.maxcode) or bdetail.glcodeid=bg.majorcode) group by bd.id"+
                    " union "+
                    "select bd.id as bud,SUM(decode(bdetail.creditAmount,null,0,bdetail.creditAmount))-SUM(decode(bdetail.debitAmount,null,0,bdetail.debitAmount)) as amt from egf_budgetdetail bd,eg_billdetails bdetail, eg_billregistermis bmis, eg_billregister br," +
                    "egf_budgetgroup bg,voucherheader vh where bmis.billid=br.id and bdetail.billid=br.id and bd.budgetgroup=bg.id and " +
                    " (bmis.budgetCheckReq is null or bmis.budgetCheckReq=1) and "+
                    "(bg.ACCOUNTTYPE='REVENUE_RECEIPTS' or bg.ACCOUNTTYPE='CAPITAL_RECEIPTS') and br.statusid not in (select id from egw_status where description='Cancelled' and moduletype in ('EXPENSEBILL', 'SALBILL', 'WORKSBILL', 'PURCHBILL', 'CBILL', 'SBILL', 'CONTRACTORBILL'))  and "+
                    " bmis.voucherheaderid =vh.id and vh.status=4 and br.billdate>= to_date('"+fromDate+"','dd/MM/yyyy') and br.billdate <= to_date("+toVoucherDate+",'dd/MM/yyyy') "+miscQuery+" and ((bdetail.glcodeid between bg.mincode " +
                    "and bg.maxcode) or bdetail.glcodeid=bg.majorcode) group by bd.id" +
                    " union "+
                    "select bd.id as bud,SUM(decode(bdetail.creditAmount,null,0,bdetail.creditAmount))-SUM(decode(bdetail.debitAmount,null,0,bdetail.debitAmount)) as amt from egf_budgetdetail bd,eg_billdetails bdetail, eg_billregistermis bmis, eg_billregister br," +
                    "egf_budgetgroup bg where bmis.billid=br.id and bdetail.billid=br.id and bd.budgetgroup=bg.id and " +
                    " (bmis.budgetCheckReq is null or bmis.budgetCheckReq=1) and "+
                    "(bg.ACCOUNTTYPE='REVENUE_RECEIPTS' or bg.ACCOUNTTYPE='CAPITAL_RECEIPTS') and br.statusid not in (select id from egw_status where description='Cancelled' and moduletype in ('EXPENSEBILL', 'SALBILL', 'WORKSBILL', 'PURCHBILL', 'CBILL', 'SBILL', 'CONTRACTORBILL'))  and bmis.voucherheaderid " +
                    "is null and br.billdate>= to_date('"+fromDate+"','dd/MM/yyyy') and br.billdate <= to_date("+toVoucherDate+",'dd/MM/yyyy') "+miscQuery+" and ((bdetail.glcodeid between bg.mincode " +
                    "and bg.maxcode) or bdetail.glcodeid=bg.majorcode) group by bd.id"+
                    " ) group by bud ");
            if(LOGGER.isDebugEnabled())     LOGGER.debug(" Main Query :"+query);
            List<Object[]> result =null;//HibernateUtil.getCurrentSession().createSQLQuery(query.toString()).list();
            return result;
    }
    /*
     * Similar to fetchActualsForBillWithParams() except that this will only consider bills for which vouchers are present
     * and the vouchers are uncancelled and BAN numbers are present for the bills and not vouchers
     */
    public List<Object[]> fetchActualsForBillWithVouchersParams(String fromDate,String toVoucherDate,StringBuffer miscQuery) {
        StringBuffer query = new StringBuffer();
        query = query.append("select bud,sum(amt) from ("+
                "select bd.id as bud,SUM(decode(bdetail.debitAmount,null,0,bdetail.debitAmount))-SUM(decode(bdetail.creditAmount,null,0,bdetail.creditAmount)) as amt from egf_budgetdetail bd,eg_billdetails bdetail, eg_billregistermis bmis, eg_billregister br," +
                "egf_budgetgroup bg,voucherheader vh, vouchermis vmis where bmis.billid=br.id and bdetail.billid=br.id and bd.budgetgroup=bg.id and " +
                "(bg.ACCOUNTTYPE='REVENUE_EXPENDITURE' or bg.ACCOUNTTYPE='CAPITAL_EXPENDITURE') and br.statusid not in (select id from egw_status where description='Cancelled' and moduletype in ('EXPENSEBILL', 'SALBILL', 'WORKSBILL', 'PURCHBILL', 'CBILL', 'SBILL', 'CONTRACTORBILL'))  and " +
                "bmis.voucherheaderid =vh.id and vh.status!=4 and br.billdate>=to_date('"+fromDate+"','dd/MM/yyyy') and br.billdate <= to_date("+toVoucherDate+",'dd/MM/yyyy') "+miscQuery+" and " +
                " (bmis.budgetCheckReq is null or bmis.budgetCheckReq=1) and vh.id = vmis.voucherheaderid and (bmis.budgetary_appnumber != 'null' and bmis.budgetary_appnumber is not null) "+
                " and ((bdetail.glcodeid between bg.mincode and bg.maxcode) or bdetail.glcodeid=bg.majorcode) group by bd.id"+
                " union "+
                "select bd.id as bud,SUM(decode(bdetail.creditAmount,null,0,bdetail.creditAmount))-SUM(decode(bdetail.debitAmount,null,0,bdetail.debitAmount)) as amt from egf_budgetdetail bd,eg_billdetails bdetail, eg_billregistermis bmis, eg_billregister br," +
                "egf_budgetgroup bg,voucherheader vh, vouchermis vmis where bmis.billid=br.id and bdetail.billid=br.id and bd.budgetgroup=bg.id and " +
                " (bmis.budgetCheckReq is null or bmis.budgetCheckReq=1) and vh.id = vmis.voucherheaderid and (bmis.budgetary_appnumber != 'null' and bmis.budgetary_appnumber is not null) "+
                " and (bg.ACCOUNTTYPE='REVENUE_RECEIPTS' or bg.ACCOUNTTYPE='CAPITAL_RECEIPTS') and br.statusid not in (select id from egw_status where description='Cancelled' and moduletype in ('EXPENSEBILL', 'SALBILL', 'WORKSBILL', 'PURCHBILL', 'CBILL', 'SBILL', 'CONTRACTORBILL'))  and "+
                " bmis.voucherheaderid =vh.id and vh.status!=4 and br.billdate>= to_date('"+fromDate+"','dd/MM/yyyy') and br.billdate <= to_date("+toVoucherDate+",'dd/MM/yyyy') "+miscQuery+" and ((bdetail.glcodeid between bg.mincode " +
                "and bg.maxcode) or bdetail.glcodeid=bg.majorcode) group by bd.id" +
                " ) group by bud ");
        if(LOGGER.isDebugEnabled())     LOGGER.debug(" Main Query :"+query);
        List<Object[]> result =null;//HibernateUtil.getCurrentSession().createSQLQuery(query.toString()).list();
        return result;
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

    public void setGenericDao(GenericHibernateDaoFactory genericDao) {
        this.genericDao = genericDao;
    }
    public PersonalInformation getEmpForCurrentUser()
    {
        return eisCommonService.getEmployeeByUserId(EGOVThreadLocals.getUserId());
    }
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
    public boolean toBeConsolidated()
    {
        PersonalInformation emp = eisCommonService.getEmployeeByUserId(EGOVThreadLocals.getUserId());
        Assignment empAssignment = eisCommonService.getLatestAssignmentForEmployeeByToDate(emp.getIdPersonalInformation(),new Date());
        Functionary empfunctionary=empAssignment.getFunctionary();
        DesignationMaster designation = empAssignment.getDesigId();
        Boolean consolidateBudget=Boolean.FALSE;
        List<AppConfigValues> list = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(Constants.EGF,"budget_toplevel_approver_designation");
        if(list.isEmpty())
            throw new ValidationException("","budget_toplevel_approver_designation is not defined in AppConfig");

        List<AppConfigValues> list2 = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(Constants.EGF,"budget_secondlevel_approver_designation");
        if(list2.isEmpty())
            throw new ValidationException("","budget_secondlevel_approver_designation is not defined in AppConfig");

        //String[] functionAndDesg=list2.get(0).getValue().split(",");
        String[] functionaryDesignationObj=list2.get(0).getValue().split(",");
        for(String strObj:functionaryDesignationObj){
            if(strObj.contains(":")){
                String[] functionaryName=strObj.split(":");
                if(empfunctionary!=null && empfunctionary.getName().equalsIgnoreCase(functionaryName[0])){
                    consolidateBudget=Boolean.TRUE;
                    break;
                }
            }else if(designation.getDesignationName().equalsIgnoreCase(strObj)){
                consolidateBudget=Boolean.TRUE;
                break;
            }else{
                consolidateBudget=Boolean.FALSE;
            }
        }

        return consolidateBudget;
    }
} 
