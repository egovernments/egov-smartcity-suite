/*
 * @(#)BillNumberMaster.java 3.0, 7 Jun, 2013 8:52:30 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.masters.model;

import org.egov.infra.admin.master.entity.Department;
import org.egov.pims.commons.Position;

/**
 * Bill Number is the concept used for approving the payslips and reports.
 * @author nayeem
 */
public class BillNumberMaster {

	private Integer id;
	private String billNumber;
	private Department department;
	private Position position;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	@Override
	public String toString() {
		StringBuilder strObject = new StringBuilder(50).append("BillNumberMaster").append("[").append("Id=").append(this.id).append(", BillNumber=").append(this.billNumber).append(", Department=")
				.append((this.department != null) ? this.department.getName() : "").append(", Position=").append((this.position != null) ? this.position.getName() : "").append("]");
		return strObject.toString();
	}
}
