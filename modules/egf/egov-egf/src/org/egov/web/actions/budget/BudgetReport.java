package org.egov.web.actions.budget;

import java.util.HashMap;
import java.util.Map;

import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.lib.rjbac.dept.DepartmentImpl;

public class BudgetReport 
{
	private CFinancialYear financialYear;
	private DepartmentImpl department;
	private CFunction function;
	private String type;
	private static Map<String,String> typeValue = new HashMap<String, String>();
	static{
		typeValue.put("I", "Revenue");
		typeValue.put("E", "Expense");
		typeValue.put("L", "Liability");
		typeValue.put("A", "Asset");
		typeValue.put("IE", "Revenue & Expense");
	}
	
	public CFinancialYear getFinancialYear() {
		return financialYear;
	}
	public void setFinancialYear(final CFinancialYear financialYear) {
		this.financialYear = financialYear;
	}
	public DepartmentImpl getDepartment() {
		return department;
	}
	public void setDepartment(final DepartmentImpl department) {
		this.department = department;
	}
	public CFunction getFunction() {
		return function;
	}
	public void setFunction(final CFunction function) {
		this.function = function;
	}
	public String getType() {
		return type;
	}
	public void setType(final String type) {
		this.type = type;
	}
	
	static String getValueFor(final String type){
		return typeValue.get(type);
	}
	
}
