package org.egov.payroll.client.payslipApprove;

import org.apache.struts.action.ActionForm;
import org.egov.payroll.model.EmpPayroll;

public class PayslipInfoForm extends ActionForm{
	EmpPayroll empPayroll;

	public EmpPayroll getEmpPayroll() {
		return empPayroll;
	}

	public void setEmpPayroll(EmpPayroll empPayroll) {
		this.empPayroll = empPayroll;
	}
}
