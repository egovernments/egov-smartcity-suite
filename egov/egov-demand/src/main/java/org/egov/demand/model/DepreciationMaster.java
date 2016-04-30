/*
 * DepreciationMaster.java Created on Nov 24, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.demand.model;

import org.egov.commons.Installment;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.entity.User;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author Administrator
 * @version 1.00
 * @since 1.00
 */
public class DepreciationMaster implements Serializable {
	private Integer id = null;
	private Integer year = null;
	private Float depreciationPct = null;
	private Date lastUpdatedTimeStamp;
	private Module module = null;
	private char isHistory;
	private User userId = null;
	private Installment startInstallment;
	private String depreciationName;
	private String depreciationType;
	private Date fromDate;
	private Date toDate;

	/**
	 * @return Returns the startInstallment.
	 */
	public Installment getStartInstallment() {
		return startInstallment;
	}

	/**
	 * @param startInstallment
	 *            The startInstallment to set.
	 */
	public void setStartInstallment(Installment startInstallment) {
		this.startInstallment = startInstallment;
	}

	/**
	 * @return Returns the depreciationPct.
	 */
	public Float getDepreciationPct() {
		return depreciationPct;
	}

	/**
	 * @param depreciationPct
	 *            The depreciationPct to set.
	 */
	public void setDepreciationPct(Float depreciationPct) {
		this.depreciationPct = depreciationPct;
	}

	/**
	 * @return Returns the id.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return Returns the lastUpdatedTimeStamp.
	 */
	public Date getLastUpdatedTimeStamp() {
		return lastUpdatedTimeStamp;
	}

	/**
	 * @param lastUpdatedTimeStamp
	 *            The lastUpdatedTimeStamp to set.
	 */
	public void setLastUpdatedTimeStamp(Date lastUpdatedTimeStamp) {
		this.lastUpdatedTimeStamp = lastUpdatedTimeStamp;
	}

	/**
	 * @return Returns the module.
	 */
	public Module getModule() {
		return module;
	}

	/**
	 * @param module
	 *            The module to set.
	 */
	public void setModule(Module module) {
		this.module = module;
	}

	/**
	 * @return Returns the year.
	 */
	public Integer getYear() {
		return year;
	}

	/**
	 * @param year
	 *            The year to set.
	 */
	public void setYear(Integer year) {
		this.year = year;
	}

	/**
	 * @return Returns the isHistory.
	 */
	public char getIsHistory() {
		return isHistory;
	}

	/**
	 * @param isHistory
	 *            The isHistory to set.
	 */
	public void setIsHistory(char isHistory) {
		this.isHistory = isHistory;
	}

	/**
	 * @return Returns the userId.
	 */
	public User getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            The userId to set.
	 */
	public void setUserId(User userId) {
		this.userId = userId;
	}

	public String getDepreciationName() {
		return depreciationName;
	}

	public void setDepreciationName(String depreciationName) {
		this.depreciationName = depreciationName;
	}

	public String getDepreciationType() {
		return depreciationType;
	}

	public void setDepreciationType(String depreciationType) {
		this.depreciationType = depreciationType;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

}
