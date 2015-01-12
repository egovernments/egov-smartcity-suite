/*
 * @(#)CFinancialYear.java 3.0, 6 Jun, 2013 3:00:02 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons;

import java.sql.Timestamp;
import java.util.Date;


public class CFinancialYear {
	private Long id = null;
	private String finYearRange = "";
	private Date startingDate;
	private Date endingDate;
	private Integer isActive = 1;
	private Date created;
	private Timestamp lastModified;
	private Integer modifiedBy = 0;
	private Integer isActiveForPosting = 0;
	private Integer isClosed = 0;
	private Integer transferClosingBalance = 0;
	
	/**
	 * @return Returns the created.
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * @param created The created to set.
	 */
	public void setCreated(Date created) {
		this.created = created;
	}

	/**
	 * @return Returns the endingDate.
	 */
	public Date getEndingDate() {
		return endingDate;
	}

	/**
	 * @param endingDate The endingDate to set.
	 */
	public void setEndingDate(Date endingDate) {
		this.endingDate = endingDate;
	}

	/**
	 * @return Returns the finYearRange.
	 */
	public String getFinYearRange() {
		return finYearRange;
	}

	/**
	 * @param finYearRange The finYearRange to set.
	 */
	public void setFinYearRange(String finYearRange) {
		this.finYearRange = finYearRange;
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
	 * @return Returns the isActive.
	 */
	public Integer getIsActive() {
		return isActive;
	}

	/**
	 * @param isActive The isActive to set.
	 */
	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return Returns the isActiveForPosting.
	 */
	public Integer getIsActiveForPosting() {
		return isActiveForPosting;
	}

	/**
	 * @param isActiveForPosting The isActiveForPosting to set.
	 */
	public void setIsActiveForPosting(Integer isActiveForPosting) {
		this.isActiveForPosting = isActiveForPosting;
	}

	/**
	 * @return Returns the isClosed.
	 */
	public Integer getIsClosed() {
		return isClosed;
	}

	/**
	 * @param isClosed The isClosed to set.
	 */
	public void setIsClosed(Integer isClosed) {
		this.isClosed = isClosed;
	}

	/**
	 * @return Returns the lastModified.
	 */
	public Timestamp getLastModified() {
		return lastModified;
	}

	/**
	 * @param lastModified The lastModified to set.
	 */
	public void setLastModified(Timestamp lastModified) {
		this.lastModified = lastModified;
	}

	/**
	 * @return Returns the modifiedBy.
	 */
	public Integer getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * @param modifiedBy The modifiedBy to set.
	 */
	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/**
	 * @return Returns the startingDate.
	 */
	public Date getStartingDate() {
		return startingDate;
	}

	/**
	 * @param startingDate The startingDate to set.
	 */
	public void setStartingDate(Date startingDate) {
		this.startingDate = startingDate;
	}

	/**
	 * @return Returns the transferClosingBalance.
	 */
	public Integer getTransferClosingBalance() {
		return transferClosingBalance;
	}

	/**
	 * @param transferClosingBalance The transferClosingBalance to set.
	 */
	public void setTransferClosingBalance(Integer transferClosingBalance) {
		this.transferClosingBalance = transferClosingBalance;
	}
}
