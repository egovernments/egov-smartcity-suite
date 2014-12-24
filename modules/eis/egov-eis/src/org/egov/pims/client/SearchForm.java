/*
 *
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.pims.client;
import org.apache.struts.action.ActionForm;
public class SearchForm extends ActionForm
{

	public String departmentId = "0";
	public String designationId = "0";
	public String functionaryId = "0";
	public String searchAll = "";
	public String code = "";
	public String name = "";
	public String desipId = "";
	public String typeId = "";
	public String chargeMemoNo = "";
	public String appNo = "";
	public String monthId = "";
	public String finYear = "";
	public String status="0";
	public String empType ="0";
	private String fromDate;
	private String toDate;
	private String functionId="0";
	private String userName="";
	private boolean userActiveCheckbox=false;
	private String userNameId="";
	private String billId="";

	public String getUserNameId() {
		return userNameId;
	}
	public void setUserNameId(String userNameId) {
		this.userNameId = userNameId;
	}
	public boolean getUserActiveCheckbox() {
		return userActiveCheckbox;
	}
	public void setUserActiveCheckbox(boolean userActiveCheckbox) {
		this.userActiveCheckbox = userActiveCheckbox;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getEmpType() {
		return empType;
	}
	public void setEmpType(String empType) {
		this.empType = empType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return Returns the code.
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code The code to set.
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return Returns the departmentId.
	 */
	public String getDepartmentId() {
		return departmentId;
	}
	/**
	 * @param departmentId The departmentId to set.
	 */
	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}
	/**
	 * @return Returns the designationId.
	 */
	public String getDesignationId() {
		return designationId;
	}
	/**
	 * @param designationId The designationId to set.
	 */
	public void setDesignationId(String designationId) {
		this.designationId = designationId;
	}
	
	public String getFunctionaryId() {
		return functionaryId;
	}
	
	public void setFunctionaryId(String functionaryId) {
		this.functionaryId = functionaryId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getChargeMemoNo() {
		return chargeMemoNo;
	}
	public void setChargeMemoNo(String chargeMemoNo) {
		this.chargeMemoNo = chargeMemoNo;
	}
	public String getDesipId() {
		return desipId;
	}
	public void setDesipId(String desipId) {
		this.desipId = desipId;
	}
	public String getAppNo() {
		return appNo;
	}
	public void setAppNo(String appNo) {
		this.appNo = appNo;
	}
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public String getSearchAll() {
		return searchAll;
	}
	public void setSearchAll(String searchAll) {
		this.searchAll = searchAll;
	}
	public String getFinYear() {
		return finYear;
	}
	public void setFinYear(String finYear) {
		this.finYear = finYear;
	}
	public String getMonthId() {
		return monthId;
	}
	public void setMonthId(String monthId) {
		this.monthId = monthId;
	}
	public String getFunctionId() {
		return functionId;
	}
	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	


}