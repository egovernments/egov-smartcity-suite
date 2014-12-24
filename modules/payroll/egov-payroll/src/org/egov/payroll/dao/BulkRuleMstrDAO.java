package org.egov.payroll.dao;

import java.util.Date;
import java.util.List;

import org.egov.payroll.model.PayGenUpdationRule;
import org.egov.pims.model.EmployeeGroupMaster;

public interface BulkRuleMstrDAO extends org.egov.infstr.dao.GenericDAO 
{
	/*
	 * checks for uniqueness of salaryCode,Month,Financial year
	 */
	PayGenUpdationRule checkRuleBasedOnSalCodeMonFnYrEmpGrp(Integer salaryCodeId,Integer monthId,Integer finId,Integer empGrpMstrId);
	/*
	 * get rule Obj by rule Id
	 */
	PayGenUpdationRule getRuleMstrById(Integer ruleId);
	//To search rule based on month and financial year
	public List getRulemasterByMonFnYr(Integer monthId,Integer finId);
	
	//To Modify rule 
	public boolean checkRuleExists(Integer ruleId,Integer salaryCodeId,Integer monthId,Integer finId,Integer empGrpMstrId)throws Exception;
	//To get latest rule by payslipgen date
	public PayGenUpdationRule getLatestRulemasterAsOnDate(Integer salaryCodeId,Date payslipGenDate,Integer empGrpMstrs);
}
