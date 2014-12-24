package org.egov.payroll.services.payslip;


import org.egov.pims.empLeave.model.EmployeeAttendenceReport;

/**
 * TODO Brief Description of the puprpose of the class/interface
 * 
 * @author Lokesh
 * @version 2.00
 * @see AutoGenPayslipJobSchedulerManager.java
 */
public interface AutoGenPayslipJobSchedulerService  {
	public void autoGenPayslipJob(String cityURL, String jndi,
			String hibFactName);
	public Boolean autoGenPaySlipForEmp(Integer empId,Integer month,Integer finyr) throws Exception;
	public Boolean createSuppPaySlip(Integer empId,Integer month,Integer finyr,EmployeeAttendenceReport empatcreport) throws Exception;
}
