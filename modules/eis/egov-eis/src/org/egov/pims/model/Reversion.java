package org.egov.pims.model;

import java.util.Date;

import org.egov.commons.CFunction;
import org.egov.commons.EgwStatus;
import org.egov.commons.Fund;
import org.egov.infstr.models.StateAware;
import org.egov.infstr.models.validator.Required;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.commons.Position;

@SuppressWarnings("serial")
public class Reversion extends StateAware
{
	private PersonalInformation employee;
	private Fund fund;
	private CFunction function;
	private String remarks;
	private EgwStatus status;
	private Long docNo;
	private Assignment assignment;
	//this is to display the emp details and will not be persisted
	private EmployeeView empDetails;
	
	@Required(message="dept.required")
	private DepartmentImpl dept;
	@Required(message="desig.required")
	private DesignationMaster desig;
	@Required(message="position.required")
	private Position position;
	@Required(message="requestDate.required")
	private Date requestDate;
	@Required(message="reversion.effdate.required")
	private Date reversionEffFrom;
	
	
	public PersonalInformation getEmployee() {
		return employee;
	}


	public void setEmployee(PersonalInformation employee) {
		this.employee = employee;
	}


	public Fund getFund() {
		return fund;
	}


	public void setFund(Fund fund) {
		this.fund = fund;
	}


	public CFunction getFunction() {
		return function;
	}


	public void setFunction(CFunction function) {
		this.function = function;
	}


	public Date getRequestDate() {
		return requestDate;
	}


	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public Date getReversionEffFrom() {
		return reversionEffFrom;
	}


	public void setReversionEffFrom(Date reversionEffFrom) {
		this.reversionEffFrom = reversionEffFrom;
	}


	public String getRemarks() {
		return remarks;
	}


	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}


	public EgwStatus getStatus() {
		return status;
	}


	public void setStatus(EgwStatus status) {
		this.status = status;
	}


	public DepartmentImpl getDept() {
		return dept;
	}


	public void setDept(DepartmentImpl dept) {
		this.dept = dept;
	}


	public DesignationMaster getDesig() {
		return desig;
	}


	public void setDesig(DesignationMaster desig) {
		this.desig = desig;
	}


	public Position getPosition() {
		return position;
	}


	public void setPosition(Position position) {
		this.position = position;
	}


	public Long getDocNo() {
		return docNo;
	}


	public void setDocNo(Long docNo) {
		this.docNo = docNo;
	}
	
	public Assignment getAssignment() {
		return assignment;
	}


	public void setAssignment(Assignment assignment) {
		this.assignment = assignment;
	}


	public EmployeeView getEmpDetails() {
		return empDetails;
	}


	public void setEmpDetails(EmployeeView empDetails) {
		this.empDetails = empDetails;
	}


	@Override
	public String getStateDetails() {
		
		return " "+employee.getCode()+" - "+employee.getEmployeeName()+" - "+dept.getDeptName();
	}
	

}
