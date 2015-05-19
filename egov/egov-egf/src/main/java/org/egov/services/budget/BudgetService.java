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
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.commons.CChartOfAccounts;
import org.egov.eis.service.EisCommonService;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infra.workflow.service.WorkflowService;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.EGovConfig;
import org.egov.model.budget.Budget;
import org.egov.model.budget.BudgetDetail;
import org.egov.model.budget.BudgetGroup;
import org.egov.pims.commons.Position;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.PersonalInformation;
import org.hibernate.Query;

/**
 * @author Manikanta
 *
 */
public class BudgetService extends PersistenceService<Budget,Long>{
	private static final Logger LOGGER = Logger.getLogger(BudgetService.class);
	protected EisCommonService eisCommonService;
	protected WorkflowService<Budget> budgetWorkflowService;
	protected BudgetDetailService budgetDetailService;
	protected SimpleWorkflowService<BudgetDetail> budgetDetailWorkflowService;
	
	public void setEisCommonService(EisCommonService eisCommonService) {
		this.eisCommonService = eisCommonService;
	}
	
	public Budget persist(Budget budget){ 
		super.validate(budget); 
		super.persist(budget);
		return budget;
	}
	
	public User getUser(){
		return (User) ((PersistenceService)this).find(" from User where id=?",EGOVThreadLocals.getUserId());
	}
	
	public Position getPositionForEmployee(PersonalInformation emp)throws EGOVRuntimeException
	{   
		return eisCommonService.getPrimaryAssignmentPositionForEmp(emp.getIdPersonalInformation());
	}
	
	/**
	 * return the department of any one budget detail 
	 * will work for only leaf budget not for non leaf budgets
	 * @param budget
	 * @return Department
	 * @throws EGOVRuntimeException
	 */
	public Department getDepartmentForBudget(Budget budget)throws EGOVRuntimeException
	{
		
		Department dept=null;
		List<BudgetDetail> detailList = (List<BudgetDetail>) (((PersistenceService)this).findAllBy("from  BudgetDetail budgetDetail where budgetDetail.budget=?",budget));
		if(detailList.isEmpty() || detailList.size()==0)
		{
			throw new EGOVRuntimeException("Details not found for the Budget"+budget.getName());
		}
		else
		{
			if(detailList.get(0).getExecutingDepartment()==null)
			{
				throw new EGOVRuntimeException("Department not found for the Budget"+budget.getName());
			}
			else
			{
				dept=detailList.get(0).getExecutingDepartment();
			}
		}
		return dept;
	}
	/**
	 * returns department  of the employee from assignment for the current date
	 * @param emp
	 * @return department
	 */
	public Department depertmentForEmployee(PersonalInformation emp)
	{
		Department dept=null;
		Date currDate=new Date();
		try {
			Assignment empAssignment = eisCommonService.getLatestAssignmentForEmployeeByToDate(emp.getIdPersonalInformation(),currDate);
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
	
	public boolean hasReForYear(Long financialYear) {
		return checkForRe("from  Budget where financialYear.id=? and isbere='RE' and isActiveBudget=1",financialYear);
	}
	public boolean hasApprovedBeForYear(Long financialYear) {
		return checkForRe("from  Budget where financialYear.id=? and isbere='BE' and isActiveBudget=1 and parent is null and isPrimaryBudget=1 and state.value='END'",financialYear);
	}
	
	public boolean hasApprovedReForYear(Long financialYear) {
		return checkForRe("from  Budget where financialYear.id=? and isbere='RE' and isActiveBudget=1 and parent is null and isPrimaryBudget=1 and state.value='END'",financialYear);
	}
	/**
	 * 
	 * @param financialYear
	 * @return boolean
	 *  Finds out whether RE is created and Approved for the given date
	 */
	public boolean hasApprovedReAsonDate(Long finYearId,Date budgetApprovedDate) {
		Query qry = getSearchSession().createQuery("select name from  Budget where financialYear.id=:finYearId and isbere='RE' " +
				"and isActiveBudget=1 and parent is null and isPrimaryBudget=1 and state.value='END' and to_date(state.createdDate)<=:budgetApprovedDate");
		qry.setParameter("finYearId", finYearId);
		qry.setParameter("budgetApprovedDate", budgetApprovedDate);
		String approvedBudgetName = (String) qry.uniqueResult();
		return approvedBudgetName==null?false:true;
	}
	

	private boolean checkForRe(String query,Long financialYear) {
		Budget budget = (Budget) ((PersistenceService)this).find(query,financialYear);
		if(budget == null)
			return false;
		return true;
	}
	
	/**
	 * finds all the child budget tree for approval or rejection
	 * @param b
	 * @param position
	 * @return
	 */	
	public List<Budget> moveBudgetTree(Budget b,Position position){
		List<Budget> budgetsList=findAllBy("from Budget b where b.materializedPath like '"+b.getMaterializedPath()+".%'");
		return budgetsList;
	}
	
	
	/**
	 * @param budgetsList
	 * @return
	 * The Base Budget Item is set from action class for which the details are moved
	 * Budget b is used to avoid recursive class
	 */
	public void moveDetailsTree(List budgetsList,Budget b,String actionName){
		for(Object o: budgetsList){
			Budget childBudget=(Budget)o;
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Budget name "+childBudget.getName()+"moved details are ...");
			List<BudgetDetail> unsavedbudgetDetailList=new ArrayList<BudgetDetail>();
			unsavedbudgetDetailList.addAll(budgetDetailService.getRemainingDetailsForApproveOrReject(childBudget));
			//move rest of the details  
			for( BudgetDetail detail :unsavedbudgetDetailList){
				if(LOGGER.isDebugEnabled())     LOGGER.debug("moveDetails"+detail.getApprovedAmount());
				budgetDetailWorkflowService.transition(actionName, detail, detail.getComment());
			}
		}
	}
	
	
	public boolean canForwardParent(Position position,Budget b){
		Budget treeBudget = b;
		List<Budget> totalCountList = findAllBy("from Budget where parent=? and isActiveBudget=?",treeBudget.getParent(),true);
		//can forward the budget even though one of the child is not in the state of forwarding if isActiveBudget is set as false
		List<Budget> budgetList = findAllBy("from Budget where state.owner=? and parent.id=? and isActiveBudget=?",position,treeBudget.getParent().getId(),true);
		if(totalCountList.size()==budgetList.size()){
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Approving Parent Budget:... "+treeBudget.getParent().getName());
			return true;
		}else{		 
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Still some ChildBudgets are pending to  move  Parent .Exiting.... ");
			return false;
		}
	}
	
	/**
	 * 
	 * @param bgroupList
	 * @return List of CChartOfAccounts objects associated with each BudgetGroup object in the list , null if no CChartOfAccounts objects are associated with any BudgetGroup object in the list.
	 * @throws ValidationException
	 * 
	 */
	
	@SuppressWarnings("unchecked")
	public List<CChartOfAccounts>  getAccountCodeForBudgetHead(List <BudgetGroup> bgroupList) throws ValidationException
	{
		
		if(bgroupList==null)
		{
			throw new ValidationException(Arrays.asList(new ValidationError("BudgetGroup List is Null","budgetgroup.list.is.null")));
		}
		if(bgroupList.size()==0) 
		{
			throw new ValidationException(Arrays.asList(new ValidationError("BudgetGroup List is Empty","budgetgroup.list.is.empty")));
		}
		List<CChartOfAccounts> coaList = new ArrayList<CChartOfAccounts>() ;
		Integer maxpossibleGlcodeLength=Integer.parseInt(EGovConfig.getProperty("egf_config.xml","glcodeMaxLength","","AccountCode"));

		CChartOfAccounts maxCode, minCode;
		String maxGlcodeStr , minGlcodeStr,glcodeFrom,glcodeTo;
		String multipleZeros = new String("00000000000000000000");
		String multipleNines = new String("99999999999999999999");
		for(BudgetGroup bdgtgrp:bgroupList)
		{
			if((((PersistenceService)this).find("from BudgetGroup where id = ? ", bdgtgrp.getId()))==null)
				throw new ValidationException(Arrays.asList(new ValidationError("BudgetGroup with id:"+bdgtgrp.getId()+" and name:"+bdgtgrp.getName()+" does not exist ","BudgetGroup with id:"+bdgtgrp.getId()+" and name:"+bdgtgrp.getName()+" does not exist ")));
		}
		List<CChartOfAccounts> clist ;
		for(BudgetGroup bdgtgrp:bgroupList)
		{
			if(bdgtgrp.getMajorCode()==null)
			{
				maxCode=bdgtgrp.getMaxCode();
				minCode=bdgtgrp.getMinCode();
				if (maxCode != null && minCode != null) {
					maxGlcodeStr = maxCode.getGlcode();
					minGlcodeStr = minCode.getGlcode();
					if(maxpossibleGlcodeLength==maxGlcodeStr.length())
					{
						glcodeFrom=minGlcodeStr;
						glcodeTo=maxGlcodeStr;
					}
					else
					{
						glcodeFrom = minGlcodeStr+multipleZeros.substring(0, (maxpossibleGlcodeLength-minGlcodeStr.length()) );
						glcodeTo = maxGlcodeStr+multipleNines.substring(0, (maxpossibleGlcodeLength-maxGlcodeStr.length()) ); 
					}
					String query = new String("from  CChartOfAccounts coa where cast(coa.glcode,long) between ? and ? and coa.classification = ?  and coa.isActiveForPosting=? ");
					clist= (List<CChartOfAccounts>) ((PersistenceService)this).findAllBy(query,Long.parseLong(glcodeFrom),Long.parseLong(glcodeTo),Long.valueOf(4),Long.valueOf(1));
					
				}
				else throw new ValidationException(Arrays.asList(new ValidationError("Maxcode or Mincode is null also Majorcode is null for BudgetGroup:"+bdgtgrp.getName(),"maxcode.or.mincode.is.null.and.majorcode.is.null.for.budgetgroup:"+bdgtgrp.getName())));
			}
			else
			{
				clist= (List<CChartOfAccounts>) ((PersistenceService)this).findAllBy("from  CChartOfAccounts coa where coa.glcode like '"+bdgtgrp.getMajorCode().getGlcode().toString()+"%' and coa.classification = ? and coa.isActiveForPosting= ? ",Long.valueOf(4),Long.valueOf(1));
				
			}
			if(clist!=null && clist.size()!=0)
			{
				coaList.addAll(clist);
			}
		}
	
		return coaList.size()==0?null:coaList;
	}
	
	public void setBudgetWorkflowService(WorkflowService<Budget> budgetWorkflowService){
		this.budgetWorkflowService = budgetWorkflowService;
	}
	
	public void setBudgetDetailService(BudgetDetailService budgetDetailService){
		this.budgetDetailService = budgetDetailService;
	}
	
	public void setBudgetDetailWorkflowService(SimpleWorkflowService<BudgetDetail> budgetDetailWorkflowService){
		this.budgetDetailWorkflowService = budgetDetailWorkflowService;
	}
	
	public User getUserForPosition (Integer posId, Date date) {
		String query = "select 	emp.userMaster  from org.egov.pims.model.EmployeeView emp where emp.position.id = ? and ((emp.toDate is null and emp.fromDate <= ? ) OR (emp.fromDate <= ? AND emp.toDate > ?))";
		User user =null;;//This fix is for Phoenix Migration.= (User) super.find(query, posId, date, date, date);
		return user;
	}
	
	public String getEmployeeNameAndDesignationForPosition(Position pos)throws EGOVRuntimeException{
		PersonalInformation pi =eisCommonService.getPrimaryAssignmentEmployeeForPos(pos.getId());
		Assignment assignment = eisCommonService.getLatestAssignmentForEmployee(pi.getIdPersonalInformation());
		return pi.getEmployeeFirstName()+" ("+assignment.getDesigId().getName()+")";
	}
	
	public PersonalInformation getEmpForCurrentUser(){
		return eisCommonService.getEmployeeByUserId(EGOVThreadLocals.getUserId());
	}

	public Budget getReferenceBudgetFor(Budget budget) {
		Budget refBudget = null;
		try {
			refBudget = find("from Budget where referenceBudget.id=?",budget.getId());
		} catch (Exception e) {
			throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(),e.getMessage())));
		}
		return refBudget;
	}

	public boolean isLeaf(Budget budget) {
		List<Budget> budgetList = findAllBy("from Budget where financialYear.id=? and id in (select parent from Budget where financialYear.id=? and parent.id=?)",
				budget.getFinancialYear().getId(),budget.getFinancialYear().getId(),budget.getId());
		return budgetList == null || budgetList.isEmpty();
	}

	public List getFYForNonApprovedBudgets() {
		return findAllBy("select distinct b.financialYear from Budget b where b.state.value!='END' and isActiveBudget=1 and isPrimaryBudget=1 order by b.financialYear.finYearRange desc");
	}

}
