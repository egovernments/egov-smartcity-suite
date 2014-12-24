/*
 * @(#)BoundryForm.java 3.0, 18 Jun, 2013 1:50:22 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.client.adminBoundry;

import java.io.Serializable;

import org.egov.infstr.client.EgovActionForm;

public class BoundryForm extends EgovActionForm implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String name = "";
	private String boundaryNum;
	private String parentBoundaryNum;
	private Integer bndryTypeHeirarchyLevel;
	private Integer topLevelBoundaryID;
	private String fromDate;
	private String toDate;
	private String bndryId;
	private String heirarchyType;
	private Integer hierarchyTypeid = null;
	private Integer boundaryId = null;
	private String cityNameLocal = "";
	private String cityBaseURL[] = null;
	private String cityLogo;
	private Integer isActive = null;

	/**
	 * @return Returns the bndryId.
	 */
	public String getBndryId() {
		return this.bndryId;
	}

	/**
	 * @param bndryId The bndryId to set.
	 */
	public void setBndryId(final String bndryId) {
		this.bndryId = bndryId;
	}

	/**
	 * @return Returns the bndryTypeHeirarchyLevel.
	 */
	public Integer getBndryTypeHeirarchyLevel() {
		return this.bndryTypeHeirarchyLevel;
	}

	/**
	 * @param bndryTypeHeirarchyLevel The bndryTypeHeirarchyLevel to set.
	 */
	public void setBndryTypeHeirarchyLevel(final Integer bndryTypeHeirarchyLevel) {
		this.bndryTypeHeirarchyLevel = bndryTypeHeirarchyLevel;
	}

	/**
	 * @return Returns the boundaryId.
	 */
	public Integer getBoundaryId() {
		return this.boundaryId;
	}

	/**
	 * @param boundaryId The boundaryId to set.
	 */
	public void setBoundaryId(final Integer boundaryId) {
		this.boundaryId = boundaryId;
	}

	/**
	 * @return Returns the boundaryNum.
	 */
	public String getBoundaryNum() {
		return this.boundaryNum;
	}

	/**
	 * @param boundaryNum The boundaryNum to set.
	 */
	public void setBoundaryNum(final String boundaryNum) {
		this.boundaryNum = boundaryNum;
	}

	/**
	 * @return Returns the cityBaseURL.
	 */
	public String[] getCityBaseURL() {
		return this.cityBaseURL;
	}

	/**
	 * @param cityBaseURL The cityBaseURL to set.
	 */
	public void setCityBaseURL(final String[] cityBaseURL) {
		this.cityBaseURL = cityBaseURL;
	}

	/**
	 * @return Returns the cityNameLocal.
	 */
	public String getCityNameLocal() {
		return this.cityNameLocal;
	}

	/**
	 * @param cityNameLocal The cityNameLocal to set.
	 */
	public void setCityNameLocal(final String cityNameLocal) {
		this.cityNameLocal = cityNameLocal;
	}

	/**
	 * @return Returns the fromDate.
	 */
	public String getFromDate() {
		return this.fromDate;
	}

	/**
	 * @param fromDate The fromDate to set.
	 */
	public void setFromDate(final String fromDate) {
		this.fromDate = fromDate;
	}

	/**
	 * @return Returns the heirarchyType.
	 */
	public String getHeirarchyType() {
		return this.heirarchyType;
	}

	/**
	 * @param heirarchyType The heirarchyType to set.
	 */
	public void setHeirarchyType(final String heirarchyType) {
		this.heirarchyType = heirarchyType;
	}

	/**
	 * @return Returns the hierarchyTypeid.
	 */
	public Integer getHierarchyTypeid() {
		return this.hierarchyTypeid;
	}

	/**
	 * @param hierarchyTypeid The hierarchyTypeid to set.
	 */
	public void setHierarchyTypeid(final Integer hierarchyTypeid) {
		this.hierarchyTypeid = hierarchyTypeid;
	}

	/**
	 * @return Returns the isActive.
	 */
	public Integer getIsActive() {
		return this.isActive;
	}

	/**
	 * @param isActive The isActive to set.
	 */
	public void setIsActive(final Integer isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return Returns the parentBoundaryNum.
	 */
	public String getParentBoundaryNum() {
		return this.parentBoundaryNum;
	}

	/**
	 * @param parentBoundaryNum The parentBoundaryNum to set.
	 */
	public void setParentBoundaryNum(final String parentBoundaryNum) {
		this.parentBoundaryNum = parentBoundaryNum;
	}

	/**
	 * @return Returns the toDate.
	 */
	public String getToDate() {
		return this.toDate;
	}

	/**
	 * @param toDate The toDate to set.
	 */
	public void setToDate(final String toDate) {
		this.toDate = toDate;
	}

	/**
	 * @return Returns the topLevelBoundaryID.
	 */
	public Integer getTopLevelBoundaryID() {
		return this.topLevelBoundaryID;
	}

	/**
	 * @param topLevelBoundaryID The topLevelBoundaryID to set.
	 */
	public void setTopLevelBoundaryID(final Integer topLevelBoundaryID) {
		this.topLevelBoundaryID = topLevelBoundaryID;
	}

	public String getCityLogo() {
		return this.cityLogo;
	}

	public void setCityLogo(final String cityLogo) {
		this.cityLogo = cityLogo;
	}
}
