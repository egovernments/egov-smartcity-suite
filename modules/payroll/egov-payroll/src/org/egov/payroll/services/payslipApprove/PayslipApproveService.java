package org.egov.payroll.services.payslipApprove;

import java.math.BigDecimal;
import java.util.List;


import org.egov.commons.EgwStatus;
import org.egov.lib.rjbac.dept.Department;
import org.egov.payroll.model.EmpPayroll;

public interface PayslipApproveService{
	
	public List getAllPayslip()throws Exception;	
	public List getAllPayslipByStatus(EgwStatus status)throws Exception;
	public List getPayslipsOfYearMonthStatus(String year, BigDecimal month, Department department, EgwStatus status)throws Exception;
	public List getAllOtherDeductionChartOfAccount()throws Exception;
	public EmpPayroll getPayslipById(Integer id)throws Exception;
	public List getPayslipsOfYearMonthStatusInternal(String year, BigDecimal month, EgwStatus status)throws Exception;
}
