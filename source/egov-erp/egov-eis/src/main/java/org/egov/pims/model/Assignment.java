package org.egov.pims.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.infstr.models.BaseModel;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.commons.Position;

public class Assignment extends BaseModel implements java.io.Serializable
{
	//extends base model
	private static final long serialVersionUID = 1L;
	private Position position;
	private Functionary functionary;
	private Fund fundId;
	private CFunction functionId;
	private DesignationMaster desigId;
	private DepartmentImpl deptId;
	private char isPrimary;
	//private Integer reports_to; commenting as of now
	private Date fromDate;
	private PersonalInformation employee;
	private Date toDate;
	private String govtOrderNo;
	private GradeMaster gradeId;
	private List<Integer> hodDeptIds=new ArrayList<Integer>();
	private Set<org.egov.pims.model.EmployeeDepartment>deptSet = new HashSet<org.egov.pims.model.EmployeeDepartment>(
			0);
	
	public Assignment()
	{
	}

	public List<Integer> getHodDeptIds() {
		for(EmployeeDepartment dep:deptSet)
		{
			hodDeptIds.add(dep.getHodept().getId());
		}
		return hodDeptIds;
	}
	/**
	 * @return Returns the desigId.
	 */
	public org.egov.pims.commons.DesignationMaster getDesigId() {
		return desigId;
	}
	/**
	 * @param desigId The desigId to set.
	 */
	public void setDesigId(org.egov.pims.commons.DesignationMaster desigId) {
		this.desigId = desigId;
	}

	/**
	 * @return Returns the functionary.
	 */
	public Functionary getFunctionary() {
		return functionary;
	}
	/**
	 * @param functionary The functionary to set.
	 */
	public void setFunctionary(Functionary functionary) {
		this.functionary = functionary;
	}
	/**
	 * @return Returns the functionId.
	 */
	public CFunction getFunctionId() {
		return functionId;
	}
	/**
	 * @param functionId The functionId to set.
	 */
	public void setFunctionId(CFunction functionId) {
		this.functionId = functionId;
	}
	/**
	 * @return Returns the fundId.
	 */
	public Fund getFundId() {
		return fundId;
	}
	/**
	 * @param fundId The fundId to set.
	 */
	public void setFundId(Fund fundId) {
		this.fundId = fundId;
	}

	public String getGovtOrderNo() {
		return govtOrderNo;
	}

	public void setGovtOrderNo(String govtOrderNo) {
		this.govtOrderNo = govtOrderNo;
	}

	public GradeMaster getGradeId() {
		return gradeId;
	}

	public void setGradeId(GradeMaster gradeId) {
		this.gradeId = gradeId;
	}

	public char getIsPrimary() {
		return isPrimary;
	}

	public void setIsPrimary(char isPrimary) {
		this.isPrimary = isPrimary;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public DepartmentImpl getDeptId() {
		return deptId;
	}

	public void setDeptId(DepartmentImpl deptId) {
		this.deptId = deptId;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public PersonalInformation getEmployee() {
		return employee;
	}

	public void setEmployee(PersonalInformation employee) {
		this.employee = employee;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Set<org.egov.pims.model.EmployeeDepartment> getDeptSet() {
		return deptSet;
	}

	public void setDeptSet(Set<org.egov.pims.model.EmployeeDepartment> deptSet) {
		this.deptSet = deptSet;
	}

}
