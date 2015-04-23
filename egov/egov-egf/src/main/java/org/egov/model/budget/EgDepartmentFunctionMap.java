package org.egov.model.budget;



import org.egov.commons.CFunction;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infstr.models.BaseModel;

// department code and function code

public class EgDepartmentFunctionMap extends BaseModel {

	/** 
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Department department;
	private CFunction function;
	private String budgetAccountType;
	
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
	public CFunction getFunction() {
		return function;
	}
	public void setFunction(CFunction function) {
		this.function = function;
	}
	public String getBudgetAccountType() {
		return budgetAccountType;
	}
	public void setBudgetAccountType(String budgetAccountType) {
		this.budgetAccountType = budgetAccountType;
	}
	
}