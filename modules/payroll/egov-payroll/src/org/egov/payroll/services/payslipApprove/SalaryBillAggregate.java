package org.egov.payroll.services.payslipApprove;

import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.lib.rjbac.dept.Department;
import org.egov.pims.commons.DrawingOfficer;
import org.egov.pims.model.EmployeeStatusMaster;



public class SalaryBillAggregate {
	
	
	private	Fund fund;
	private CFunction function;
	private Functionary functionary;
	private Department department;
	private EmployeeStatusMaster empType;
	private DrawingOfficer drawingOfficer;
	private String billType;
	
	
	
	public Functionary getFunctionary() {
		return functionary;
	}	
	public void setFunctionary(Functionary functionary) {
		this.functionary = functionary;
	}
	public CFunction getFunction() {
		return function;
	}
	public void setFunction(CFunction function) {
		this.function = function;
	}
	public Fund getFund() {
		return fund;
	}
	public void setFund(Fund fund) {
		this.fund = fund;
	}	
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
	public EmployeeStatusMaster getEmpType() {
		return empType;
	}
	public void setEmpType(EmployeeStatusMaster empType) {
		this.empType = empType;
	}
	public DrawingOfficer getDrawingOfficer() {
		return drawingOfficer;
	}
	public void setDrawingOfficer(DrawingOfficer drawingOfficer) {
		this.drawingOfficer = drawingOfficer;
	}
	public String getBillType() {
		return billType;
	}
	public void setBillType(String billType) {
		this.billType = billType;
	}
	public boolean equals(Object o) {
		if ((o instanceof SalaryBillAggregate) && (((SalaryBillAggregate)o).getFund()== this.getFund()) &&
				((SalaryBillAggregate)o).getFunction() == this.getFunction() &&
				((SalaryBillAggregate)o).getFunctionary() == this.getFunctionary() &&
				((SalaryBillAggregate)o).getEmpType() == this.getEmpType() &&
				((SalaryBillAggregate)o).getDepartment() == this.getDepartment() && 
				((SalaryBillAggregate)o).getDrawingOfficer() == this.getDrawingOfficer() &&
				((SalaryBillAggregate)o).getBillType().equals(this.getBillType())){
		return true;
		} else {
		return false;
		}
	}
	
	public int hashCode() {
		Integer hash = 0;
		if(fund != null)
			{
			hash = hash + fund.getId();
			}		
		if(function != null)
			{
			hash = hash + function.getId().intValue();
			}			
		if(functionary != null)
			{
			hash = hash + functionary.getId();
			}
		if(department != null)
			{
			hash = hash + department.getId();
			}
		if(empType != null)
			{
			hash = hash + empType.getId();
			}
		if(drawingOfficer != null){
			hash = hash + drawingOfficer.getId();
		}
		if(billType != null){
			//hash = hash + billType.hashCode();
			int asc = 0;
			for (int i=0; i<billType.length();i++)
				asc += (int)billType.charAt(i);
			hash = hash + asc;
		}
	
		return hash;
	}
	
	
	
}
