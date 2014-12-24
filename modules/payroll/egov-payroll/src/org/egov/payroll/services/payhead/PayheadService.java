/*
 * PropertyManager.java Created on Sep 30, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.payroll.services.payhead;

import java.util.List;

import org.egov.payroll.model.PayGenUpdationRule;
import org.egov.payroll.model.PayheadRule;
import org.egov.payroll.model.SalaryCategoryMaster;
import org.egov.payroll.model.SalaryCodes;




public interface PayheadService
{
	public void createPayHead(SalaryCodes payHead)throws Exception; 
	public SalaryCategoryMaster getSalCategoryMaster(String name)throws Exception;
	public SalaryCategoryMaster getSalCategoryMasterCach(String name)throws Exception;
	public List getAllSalaryCodes()throws Exception;
	public List<SalaryCodes> getAllSAlaryCodesSortedByOrder()throws Exception;
	public List<SalaryCodes> getAllSalaryCodesByCache()throws Exception;
	public SalaryCodes getSalaryCodeByHead(String head)throws Exception;
	public List getAllSalaryCodesByType(String type)throws Exception;
	public List getAllSalaryCodesByTypeAsSortedByOrder(String type)throws Exception;
	public List<SalaryCategoryMaster> getAllCategoryMasterByType(String type)throws Exception;
	public List<SalaryCodes> getAllSalarycodesByCategoryName(String categoryName)throws Exception;
	public List<SalaryCodes> getAllSalarycodesByCategoryId(Integer categoryId)throws Exception;
	/*
	 * To create rule Object
	 */
	public void createRule(PayGenUpdationRule ruleObj) throws Exception;
	/*
	 * To check uniqueness for salarycode,month,financial year
	 */
	public abstract PayGenUpdationRule checkRuleBasedOnSalCodeMonFnYrEmpGrp(Integer salaryCodeId,Integer monthId,Integer finId,Integer empGrpMstrId) throws Exception;
	
	/*
	 * To get Rule Obj by Id
	 */
	
	public abstract PayGenUpdationRule getRuleMstrById(Integer ruleId)throws Exception;
	
	public abstract List getAllRuleMstr()throws Exception;
	
	public void updateRule(PayGenUpdationRule ruleObj)throws Exception;
	//To search rule based on month and financial year
	public List getRulemasterByMonFnYr(Integer monthId,Integer finId);
	
	//To Modify rule 
	public boolean checkRuleExists(Integer ruleId,Integer salaryCodeId,Integer monthId,Integer finId,Integer empGrpMstrId)throws Exception;
	
	/**
	 *	Get payheadRule by id  
	 */
	public PayheadRule getPayheadRuleById(Integer id);
	
	/**
	 *	save payheadRule 
	 */
	public PayheadRule savePayheadRule(PayheadRule payheadRule);
	 
	public abstract void updatePayheadRule(PayheadRule payheadRule);
	/**
	 * Get all payheadrule
	 */
	public List<PayheadRule> getAllPayheadRule();
	public List<PayheadRule> getAllPayheadRulesBySalCode(Integer id);
	public void deletePayrule(PayheadRule prule);
}
