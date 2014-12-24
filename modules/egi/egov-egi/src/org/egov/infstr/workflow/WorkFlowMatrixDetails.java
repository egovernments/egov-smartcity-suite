/*
 * @(#)WorkFlowMatrixDetails.java 3.0, 17 Jun, 2013 4:37:44 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.workflow;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WorkFlowMatrixDetails implements Comparable<WorkFlowMatrixDetails> {

	private Long approverNo;
	private String[] designation;
	private String state;
	private String action;
	private String status;
	private String[] buttons;
	private String department;
	private String objectType;
	private BigDecimal fromQty;
	private BigDecimal toQty;
	private Date fromDate;
	private Date toDate;
	private String additionalRule;
	private String objectTypeAlias;
	private BigDecimal fromQtyAlias;
	private BigDecimal toQtyAlias;
	private Date fromDateAlias;
	private Date toDateAlias;
	private String departmentAlias;
	private String additionalRuleAlias;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private String objectTypeDisplay;
	private Long rejectApproverNo;
	private String[] rejectDesignation;
	private String rejectState;
	private String rejectAction;
	private String rejectStatus;
	private String[] rejectButtons;

	public Long getRejectApproverNo() {
		return rejectApproverNo;
	}

	public void setRejectApproverNo(Long rejectApproverNo) {
		this.rejectApproverNo = rejectApproverNo;
	}

	public String[] getRejectDesignation() {
		return rejectDesignation;
	}

	public void setRejectDesignation(String[] rejectDesignation) {
		this.rejectDesignation = rejectDesignation;
	}

	public String getRejectState() {
		return rejectState;
	}

	public void setRejectState(String rejectState) {
		this.rejectState = rejectState;
	}

	public String getRejectAction() {
		return rejectAction;
	}

	public void setRejectAction(String rejectAction) {
		this.rejectAction = rejectAction;
	}

	public String getRejectStatus() {
		return rejectStatus;
	}

	public void setRejectStatus(String rejectStatus) {
		this.rejectStatus = rejectStatus;
	}

	public String[] getRejectButtons() {
		return rejectButtons;
	}

	public void setRejectButtons(String[] rejectButtons) {
		this.rejectButtons = rejectButtons;
	}

	public String getObjectTypeDisplay() {
		return objectTypeDisplay;
	}

	public void setObjectTypeDisplay(String objectTypeDisplay) {
		this.objectTypeDisplay = objectTypeDisplay;
	}

	public String getDepartmentAlias() {
		return departmentAlias;
	}

	public void setDepartmentAlias(String departmentAlias) {
		this.departmentAlias = departmentAlias;
		this.department = departmentAlias;
	}

	public String getAdditionalRuleAlias() {
		return additionalRuleAlias;
	}

	public void setAdditionalRuleAlias(String additionalRuleAlias) {
		this.additionalRuleAlias = additionalRuleAlias;
		this.additionalRule = additionalRuleAlias;
	}

	public String getObjectTypeAlias() {
		return objectTypeAlias;
	}

	public void setObjectTypeAlias(String objectTypeAlias) {
		this.objectTypeAlias = objectTypeAlias;
		this.objectType = objectTypeAlias;
	}

	public BigDecimal getFromQtyAlias() {
		return fromQtyAlias;
	}

	public void setFromQtyAlias(BigDecimal fromQtyAlias) {
		this.fromQtyAlias = fromQtyAlias;
		this.fromQty = fromQtyAlias;
	}

	public BigDecimal getToQtyAlias() {
		return toQtyAlias;
	}

	public void setToQtyAlias(BigDecimal toQtyAlias) {
		this.toQtyAlias = toQtyAlias;
		this.toQty = toQtyAlias;
	}

	public Date getFromDateAlias() {
		return fromDateAlias;
	}

	public void setFromDateAlias(Date fromDateAlias) {
		this.fromDateAlias = fromDateAlias;
		this.fromDate = fromDateAlias;
	}

	public Date getToDateAlias() {
		return toDateAlias;
	}

	public void setToDateAlias(Date toDateAlias) {
		this.toDateAlias = toDateAlias;
		this.toDate = toDateAlias;
	}

	private List<WorkFlowMatrixDetails> matrixdetails = new ArrayList<WorkFlowMatrixDetails>();

	public String getAdditionalRule() {
		return additionalRule;
	}

	public void setAdditionalRule(String additionalRule) {
		this.additionalRule = additionalRule;
	}

	public List<WorkFlowMatrixDetails> getMatrixdetails() {
		return matrixdetails;
	}

	public void setMatrixdetails(List<WorkFlowMatrixDetails> matrixdetails) {
		this.matrixdetails = matrixdetails;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public BigDecimal getFromQty() {
		return fromQty;
	}

	public void setFromQty(BigDecimal fromQty) {
		this.fromQty = fromQty;
	}

	public BigDecimal getToQty() {
		return toQty;
	}

	public void setToQty(BigDecimal toQty) {
		this.toQty = toQty;
	}

	public Date getFromDate() {

		return fromDate;
	}

	public String getFromDateString() {

		return sdf.format(fromDate);
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public String getToDateString() {
		return sdf.format(toDate);
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String[] getButtons() {
		return buttons;
	}

	public void setButtons(String[] buttons) {
		this.buttons = buttons;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Long getApproverNo() {
		return approverNo;
	}

	public void setApproverNo(Long approverNo) {
		this.approverNo = approverNo;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String[] getDesignation() {
		return designation;
	}

	public void setDesignation(String[] designation) {
		this.designation = designation;
	}

	public String getDesignationString() {
		StringBuffer str = new StringBuffer();
		if (getDesignation() != null) {
			for (String designationname : getDesignation()) {
				str.append(designationname);
				str.append(",");
			}
			String designationString = new String(str);
			return designationString.substring(0, designationString.length() - 1);
		} else
			return "";
	}

	public String getButtonString() {
		StringBuffer str = new StringBuffer();
		if (getButtons() != null) {
			for (String buttonname : getButtons()) {
				str.append(buttonname);
				str.append(",");
			}
			String buttonsString = new String(str);
			return buttonsString.substring(0, buttonsString.length() - 1);
		} else
			return "";
	}

	public String getRejectdesignationString() {
		StringBuffer str = new StringBuffer();
		if (getRejectDesignation() != null) {
			for (String designationname : getRejectDesignation()) {
				str.append(designationname);
				str.append(",");
			}
			String designationString = new String(str);
			return designationString.substring(0, designationString.length() - 1);
		} else
			return "";
	}

	public String getRejectbuttonString() {
		StringBuffer str = new StringBuffer();
		if (getRejectButtons() != null) {
			for (String buttonname : getRejectButtons()) {
				str.append(buttonname);
				str.append(",");
			}
			String buttonsString = new String(str);
			return buttonsString.substring(0, buttonsString.length() - 1);
		} else
			return "";
	}

	@Override
	public int compareTo(WorkFlowMatrixDetails o) {

		if (this.getApproverNo() < o.getApproverNo())
			return -1;
		else if (this.getApproverNo() < o.getApproverNo())
			return 1;
		else
			return 0;
	}

}
