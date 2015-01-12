/*
 * @(#)CChartOfAccounts.java 3.0, 6 Jun, 2013 2:57:45 PM 
 * Copyright 2013 eGovernments Foundation. All rights reserved.  
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons;

import java.util.HashSet;
import java.util.Set;

import org.egov.infstr.models.BaseModel;

public class CChartOfAccounts extends BaseModel {
	private static final long serialVersionUID = 1L;

	private Long id = null;
	private String glcode;
	private String name;
	private String purposeId;
	private String desc;
	private Long isActiveForPosting;
	private Long parentId;
	private Long schedule;
	private Character operation;
	private Character type;
	private Long classification;
	private Long functionReqd;
	private Long budgetCheckReq;
	private String majorCode;
	private Long myClass;
	private Set<CChartOfAccountDetail> chartOfAccountDetails = new HashSet<CChartOfAccountDetail>();

	public Set<CChartOfAccountDetail> getChartOfAccountDetails() {
		return chartOfAccountDetails;
	}

	public void setChartOfAccountDetails(Set<CChartOfAccountDetail> chartOfAccountDetail) {
		this.chartOfAccountDetails = chartOfAccountDetail;
	}

	public String getMajorCode() {
		return majorCode;
	}

	public void setMajorCode(String majorCode) {
		this.majorCode = majorCode;
	}

	/**
	 * @return Returns the myClass.
	 */
	public Long getMyClass() {
		return myClass;
	}

	/**
	 * @param myClass The myClass to set.
	 */
	public void setMyClass(Long myClass) {
		this.myClass = myClass;
	}

	/**
	 * @return Returns the glcode.
	 */
	public String getGlcode() {
		return glcode;
	}

	/**
	 * @param glcode The glcode to set.
	 */
	public void setGlcode(String glcode) {
		this.glcode = glcode;
	}

	/**
	 * @return Returns the id.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the purposeId.
	 */
	public String getPurposeId() {
		return purposeId;
	}

	/**
	 * @param purposeId The purposeId to set.
	 */
	public void setPurposeId(String purposeId) {
		this.purposeId = purposeId;
	}

	/**
	 * @return Returns the classification.
	 */
	public Long getClassification() {
		return classification;
	}

	/**
	 * @param classification The classification to set.
	 */
	public void setClassification(Long classification) {
		this.classification = classification;
	}

	/**
	 * @return Returns the desc.
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc The desc to set.
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @return Returns the functionReqd.
	 */
	public Long getFunctionReqd() {

		return functionReqd;

	}

	/**
	 * @param functionReqd The functionReqd to set.
	 */
	public void setFunctionReqd(Long functionReqd) {
		this.functionReqd = functionReqd;
	}

	public void setFunctionReqd(boolean functionReqd) {
		if (functionReqd == true)
			this.functionReqd = Long.valueOf("1");
		else
			this.functionReqd = Long.valueOf("0");
	}

	/**
	 * @return Returns the isActiveForPosting.
	 */
	public Long getIsActiveForPosting() {
		return isActiveForPosting;
	}

	/**
	 * @param isActiveForPosting The isActiveForPosting to set.
	 */
	public void setIsActiveForPosting(Long isActiveForPosting) {
		this.isActiveForPosting = isActiveForPosting;
	}

	public void setIsActiveForPosting(boolean isActiveForPosting) {
		if (isActiveForPosting == true)
			this.isActiveForPosting = Long.valueOf("1");
		else
			this.isActiveForPosting = Long.valueOf("0");
	}

	/**
	 * @return Returns the operation.
	 */
	public Character getOperation() {
		return operation;
	}

	/**
	 * @param operation The operation to set.
	 */
	public void setOperation(Character operation) {
		this.operation = operation;
	}

	/**
	 * @return Returns the parentId.
	 */
	public Long getParentId() {
		return parentId;
	}

	/**
	 * @param parentId The parentId to set.
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return Returns the schedule.
	 */
	public Long getSchedule() {
		return schedule;
	}

	/**
	 * @param schedule The schedule to set.
	 */
	public void setSchedule(Long schedule) {
		this.schedule = schedule;
	}

	/**
	 * @return Returns the type.
	 */
	public Character getType() {
		return type;
	}

	/**
	 * @param type The type to set.
	 */
	public void setType(Character type) {
		this.type = type;
	}

	public boolean equals(Object o) {
		if ((o instanceof CChartOfAccounts) && (((CChartOfAccounts) o).getId().equals(this.getId()))) {
			return true;
		} else {
			return false;
		}
	}

	public int hashCode() {
		return (Integer.valueOf(glcode));
	}

	public Long getBudgetCheckReq() {
		return budgetCheckReq;
	}

	public void setBudgetCheckReq(Long budgetCheckReq) {
		this.budgetCheckReq = budgetCheckReq;
	}

	public void setBudgetCheckReq(boolean budgetCheckReq) {
		if (budgetCheckReq == true)
			this.budgetCheckReq = Long.valueOf("1");
		else
			this.budgetCheckReq = Long.valueOf("0");
	}
}
