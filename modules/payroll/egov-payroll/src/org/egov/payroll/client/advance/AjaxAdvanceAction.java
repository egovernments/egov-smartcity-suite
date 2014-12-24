package org.egov.payroll.client.advance;

import java.util.List;

import org.apache.struts2.convention.annotation.ParentPackage ;
import org.egov.payroll.model.Advance;
import org.egov.payroll.model.AdvanceSchedule;
import org.egov.web.actions.BaseFormAction;

@ParentPackage("egov")
public class AjaxAdvanceAction extends BaseFormAction {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String employeeCodeId;
	private String salaryCode;
	private static final String PENDING_PREVIOUS_AMT ="pendingPreviousAmt"; 
	private double pendingPrevAmt;

	@Override
	public Object getModel() {
		return null;
	}
	
	public String getPendingPreviousAmt()
	{
		pendingPrevAmt  =0;
		List<Advance> advanceList =  getPersistenceService().findAllBy("from Advance where employee.idPersonalInformation=? and salaryCodes.head=? ", Integer.parseInt(employeeCodeId) ,salaryCode );
		for(Advance advance:advanceList)
		{
			pendingPrevAmt += advance.getPendingAmt().doubleValue();
		}
		return PENDING_PREVIOUS_AMT ;
	}
	
	public String getEmployeeCodeId() {
		return employeeCodeId;
	}

	public void setEmployeeCodeId(String employeeCodeId) {
		this.employeeCodeId = employeeCodeId;
	}

	public String getSalaryCode() {
		return salaryCode;
	}

	public void setSalaryCode(String salaryCode) {
		this.salaryCode = salaryCode;
	}

	public double getPendingPrevAmt() {
		return pendingPrevAmt;
	}

	public void setPendingPrevAmt(double pendingPrevAmt) {
		this.pendingPrevAmt = pendingPrevAmt;
	}


	

}
