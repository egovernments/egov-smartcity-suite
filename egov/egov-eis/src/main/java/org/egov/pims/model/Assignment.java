/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
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
import org.egov.infra.admin.master.entity.Department;
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
	private Department deptId;
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
			hodDeptIds.add(Integer.valueOf(dep.getHodept().getId().intValue()));
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

	public Department getDeptId() {
		return deptId;
	}

	public void setDeptId(Department deptId) {
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
