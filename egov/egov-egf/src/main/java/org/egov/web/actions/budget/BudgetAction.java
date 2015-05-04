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
package org.egov.web.actions.budget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.model.budget.Budget;
import org.egov.model.budget.BudgetDetail;
import org.egov.pims.commons.Position;
import org.egov.web.actions.BaseFormAction;
import org.springframework.transaction.annotation.Transactional;

import com.opensymphony.xwork2.validator.annotations.Validation;

@Result(name="success", type="ServletRedirectResult.class", location = "budget.action")
@ParentPackage("egov")
@Validation
@Transactional(readOnly=true)
public class BudgetAction extends BaseFormAction{
	private static final long serialVersionUID = 1L;
	private Budget budget=new Budget();
	private PersistenceService<Budget, Long> budgetService;
	private PersistenceService<CFinancialYear, Long> finYearService;
	private List<String> isbereList;
	private List<Budget> budgetList=new ArrayList<Budget>(); 
	private static final  String SEARCH="search";
	private Integer parentId;
	private Integer referenceId;
	private Map<Long, String> parMap =new HashMap<Long, String>();
	private SimpleWorkflowService<Budget> budgetWorkflowService;
	private String target="",bere="";
	private Long tempId=null;
	private List<Budget> parentBudgets;
	private static final String PARENTBUDGETS="parentbudgets";
	private static final Logger	LOGGER	= Logger.getLogger(BudgetAction.class);
	private EisCommonService eisCommonService=null;
	private List<Budget> referenceBudgetList=new ArrayList<Budget>(); 
	private CFinancialYear financialYear;
	private FinancialYearDAO financialYearDAO;
	private Long financialYearId;
	
	public BudgetAction(){
		addRelatedEntity("financialYear", CFinancialYear.class);
	}

	public Object getModel() {
		return budget;
	}

	public String execute(){
		return INDEX;
	}
	public void prepare(){
		super.prepare();
		isbereList = new ArrayList<String>(); 
		isbereList.add("BE");
		isbereList.add("RE");
		//consider isactiveforPosting since ui supports this one 
		
		addDropdownData("financialYearList", getPersistenceService().findAllBy("from CFinancialYear where isActive=1 order by finYearRange desc "));
		addDropdownData("referenceBudgetList", Collections.EMPTY_LIST);
		setupDropdownDataExcluding("financialYear");  
	}
	@SkipValidation
	@Action(value="/budget/budget-newform")
	public String newform(){
		return NEW;
	}
	@Transactional
	public String create(){
		addMaterializedPath(budget);
		budgetService.create(budget);
		if(getParentId()!=null &&getParentId()>0){
			budget.setParent(budgetService.findById(Long.valueOf(getParentId()), false));
		}
		if(getReferenceId()!=null && getReferenceId()!=-1){
			budget.setReferenceBudget(budgetService.findById(Long.valueOf(getReferenceId()), false));
		}
		if(budget.getParent()!=null && budget.getParent().getIsPrimaryBudget() == true)
			budget.setIsPrimaryBudget(true);
		Position p = eisCommonService.getPositionByUserId(EGOVThreadLocals.getUserId());
		budget.start().withOwner(p);
		addActionMessage(getMessage("budget.create")); 
		target="SUCCESS";
		return NEW;
	}

	/**
	 * 
	 */
	private void addMaterializedPath(Budget budget){
		String materializedPath="";
		String count="";
		if(getParentId()!=null && getParentId()>0){
			Budget parent=budgetService.findById(Long.valueOf(getParentId()), false);
			if(parent!=null){
				materializedPath=parent.getMaterializedPath();
			}
		}
		List<Budget> parallelBudgets=null;
		if(getParentId()==null  || getParentId()<=0){
			parallelBudgets=budgetService.findAllBy("from Budget b where b.parent is null");
		}else{  	 
			parallelBudgets=budgetService.findAllBy("from Budget b where b.parent.id=?",Long.valueOf(getParentId()));
		}
		if(parallelBudgets!=null){
			count = String.valueOf(parallelBudgets.size()+1);
		}	
		if(materializedPath==null || materializedPath.isEmpty()){
			materializedPath=count;
		}else{
			materializedPath=materializedPath+"."+count;
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug("MaterializedPath..."+materializedPath);
		budget.setMaterializedPath(materializedPath);
	}

	protected String getMessage(String key) {
		return getText(key);
	}

	//fetch all RE budgets which have not been assigned as reference budget for any budget
	@SkipValidation
	@Action(value="/budget/budget-ajaxLoadReferenceBudgets")
	public String ajaxLoadReferenceBudgets() {
		referenceBudgetList = new ArrayList<Budget>();
		financialYear = financialYearDAO.getFinancialYearById(financialYear.getId());
		CFinancialYear previousYear = financialYearDAO.getFinancialYearByFinYearRange(computeYearRange(financialYear.getFinYearRange()));
		referenceBudgetList.addAll(getReferenceBudgetsFor(previousYear));
		return "referenceBudgets";
	}

	private List getReferenceBudgetsFor(CFinancialYear previousYear) {
		return persistenceService.findAllBy("from Budget where isactivebudget = 1 and state.type='Budget' and isbere='RE' and " +
				"state.value='NEW' and financialYear.id = "+previousYear.getId()+" and id not in (select referenceBudget.id from Budget where" +
						" financialYear.id="+financialYear.getId()+" and referenceBudget is not null) order by name");
	}
	private String computeYearRange(String range) {
		if(StringUtils.isNotBlank(range)){
			String[] list = range.split("-");
			return subtract(list[0]) +"-"+subtract(list[1]);
		}
		return "";
	}
	protected String subtract(String value) {
		int val = Integer.parseInt(value) - 1;
		if(val<10)
			return "0"+ val;
		return String.valueOf(val);
	}
	@Transactional
	public String save()
	{
		if(getParentId()!=null &&getParentId()>0)
			budget.setParent(budgetService.findById(Long.valueOf(getParentId()), false));
		if(getReferenceId()!=null && getReferenceId()>0)
			budget.setReferenceBudget(budgetService.findById(Long.valueOf(getReferenceId()), false));
		if(budget.getState()!=null){
			State state = (State)persistenceService.find("from org.egov.infra.workflow.entity.State where id=?",budget.getState().getId());
			//This fix is for Phoenix Migration.budget.setState(state);
		}
	HibernateUtil.getCurrentSession().flush();
		budgetService.persist(budget);
		addActionMessage(getMessage("budget.update"));
		target="SUCCESS";
		return NEW; 
	}
	@SkipValidation
	public String list() {  
		budgetList= budgetService.findAllBy(" from Budget b where b.financialYear=? and b.state in (from org.egov.infra.workflow.entity.State where type='Budget' and value='NEW' ) ", budget.getFinancialYear());
		if(budgetList.isEmpty())
			target="EMPTY";
		return SEARCH;  
	}  
	@Transactional
	@SkipValidation
	@Action(value="/budget/budget-edit")
	public String edit(){  
		budget=budgetService.findById(budget.getId(), false);
		if(budget.getParent()!=null)
			setParentId(Integer.valueOf(budget.getParent().getId().toString()));
		if(budget.getReferenceBudget()!=null)
			setReferenceId(Integer.valueOf(budget.getReferenceBudget().getId().toString()));
		// check the budget detail is exist for this budget definiton
		List<BudgetDetail> budgetDetailList = persistenceService.findAllBy(" from BudgetDetail where budget=?",budget);
		financialYear = financialYearDAO.getFinancialYearById(budget.getFinancialYear().getId());
		CFinancialYear previousYear = financialYearDAO.getFinancialYearByFinYearRange(computeYearRange(financialYear.getFinYearRange()));
		referenceBudgetList.addAll(getReferenceBudgetsFor(previousYear));
		if(budget.getReferenceBudget()!=null){
			referenceBudgetList.add(budget.getReferenceBudget());
		}
		if(budgetDetailList.isEmpty())
			target="allowToEdit";
		else
			target="NotAllowToEdit";
		return EDIT;  
	} 
	@SkipValidation
	@Action(value="/budget/budget-search")
	public String search(){
		target="NONE";
		return SEARCH;
	}

	public List<String> getIsbereList(){
	    return isbereList;
	  }
	
	/**
	 * @return budget
	 */
	public Budget getBudget() {
		return budget;
	}
	/**
	 * @param budget the budget to set
	 */
	public void setBudget(Budget budget) {
		this.budget = budget;
	}
	/**
	 * @param budgetService the budgetService to set
	 */
	public void setBudgetService(PersistenceService<Budget, Long> budgetService) {
		this.budgetService = budgetService;
	}
	
	/**
	 * @return budgetList
	 */
	public List<Budget> getBudgetList() {
		return budgetList;
	}
	/**
	 * @return parentId
	 */
	public Integer getParentId() {
		return parentId;
	}

	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return parMap
	 */
	public Map<Long, String> getParMap() 
	{
		List<Budget> parList = null;
		if(budget==null || budget.getId()==null)
			parList = (List<Budget>)getPersistenceService().findAllBy("from Budget b where b.isbere='BE' and b.id not in (select budget from BudgetDetail) and b.state in (from org.egov.infra.workflow.entity.State where type='Budget' and value='NEW' ) ");
		else
			parList = (List<Budget>)getPersistenceService().findAllBy("from Budget b where b.isbere='BE' and b.id not in (select budget from BudgetDetail) and b.state in (from org.egov.infra.workflow.entity.State where type='Budget' and value='NEW' ) and b.id!="+budget.getId());
		for(Budget b:parList)
		{
			parMap.put(b.getId(),b.getName());
		}
		return parMap;
	}

	/**
	 * @param parMap the parMap to set
	 */
	public void setParMap(Map<Long, String> parMap) {
		this.parMap = parMap;
	}
	public void validate(){
		if(getParentId()!=null && getParentId()>0){
			Budget b = budgetService.findById(Long.valueOf(getParentId()), false);
			if(!b.getIsbere().equals(budget.getIsbere()))
				addFieldError("parentId",getText("budget.parent.invalid",new String[]{budget.getIsbere()}));
		}
		Date dt=new Date();
		if(budget.getFinancialYear()!=null){
			CFinancialYear finyear = finYearService.findByNamedQuery("validateFinancialYear", budget.getFinancialYear().getId(),dt,dt,dt);
			if(finyear == null)
			{
				addFieldError("financialYear",getText("budget.finyear.invalid"));
			}
		}
		if(budget.getIsPrimaryBudget() && budget.getFinancialYear()!=null && (getParentId()==null || getParentId()<=0)){
			List<Budget> list = getPersistenceService().findAllBy(" from Budget where isbere=? and financialYear=? and isPrimaryBudget=1 and parent is null", budget.getIsbere(),budget.getFinancialYear());
			if(list!=null && list.size()>1)
				addFieldError("isPrimaryBudget",getText("budget.primary.invalid2",new String[]{budget.getFinancialYear().getFinYearRange(),budget.getIsbere()}));
		}
	}
	
	/**
	 * @param finYearService the finYearService to set
	 */
	public void setFinYearService(PersistenceService<CFinancialYear, Long> finYearService) {
		this.finYearService = finYearService;
	}

	/**
	 * @param workflowService the workflowService to set
	 */
	public void setBudgetWorkflowService(SimpleWorkflowService<Budget> workflowService) {
		this.budgetWorkflowService = workflowService;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}
	@SkipValidation
	@Action(value="/budget/budget-ajaxLoadParentBudgets")
	public String ajaxLoadParentBudgets()
	{
		if(tempId==null){
			parentBudgets = getPersistenceService().findAllBy("from Budget b where b.isbere=? and b.id not in " +
					"(select budget from BudgetDetail) and b.state in (from org.egov.infra.workflow.entity.State where " +
					"type='Budget' and value='NEW' ) and b.financialYear.id=?",bere,financialYearId);
		}else{
			parentBudgets = getPersistenceService().findAllBy("from Budget b where b.isbere=? and b.id not in " +
					"(select budget from BudgetDetail) and b.state in (from org.egov.infra.workflow.entity.State where " +
					"type='Budget' and value='NEW' ) and b.financialYear.id=? and b.id!="+tempId,bere,financialYearId);
		}
		return PARENTBUDGETS;
	}

	public List<Budget> getParentBudgets() {
		return parentBudgets;
	}

	public String getBere() {
		return bere;
	}

	public void setBere(String bere) {
		this.bere = bere;
	}

	public void setEisCommonService(EisCommonService eisCommonService) {
		this.eisCommonService = eisCommonService;
	}

	public Long getTempId() {
		return tempId;
	}

	public void setTempId(Long tempId) {
		this.tempId = tempId;
	}

	public void setFinancialYear(CFinancialYear financialYear) {
		this.financialYear = financialYear;
	}

	public CFinancialYear getFinancialYear() {
		return financialYear;
	}

	public void setReferenceBudgetList(List<Budget> referenceBudgetList) {
		this.referenceBudgetList = referenceBudgetList;
	}

	public List<Budget> getReferenceBudgetList() {
		return referenceBudgetList;
	}

	public void setFinancialYearDAO(FinancialYearDAO financialYearDAO) {
		this.financialYearDAO = financialYearDAO;
	}

	public void setReferenceId(Integer referenceId) {
		this.referenceId = referenceId;
	}

	public Integer getReferenceId() {
		return referenceId;
	}

	public void setFinancialYearId(Long financialYearId) {
		this.financialYearId = financialYearId;
	}

	public Long getFinancialYearId() {
		return financialYearId;
	}

}
