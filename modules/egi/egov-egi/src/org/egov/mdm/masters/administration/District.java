/*
 * @(#)District.java 3.0, 7 Jun, 2013 8:44:17 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.mdm.masters.administration;

public class District {

	private Integer id;

	private String name;

	private String districtNameLocal;

	private String districtConst;

	private Integer stateId;

	/**
	 * @return Returns the districtConst.
	 */
	public String getDistrictConst() {
		return districtConst;
	}

	/**
	 * @param districtConst
	 *            The districtConst to set.
	 */
	public void setDistrictConst(String districtConst) {
		this.districtConst = districtConst;
	}

	/**
	 * @return Returns the districtNameLocal.
	 */
	public String getDistrictNameLocal() {
		return districtNameLocal;
	}

	/**
	 * @param districtNameLocal
	 *            The districtNameLocal to set.
	 */
	public void setDistrictNameLocal(String districtNameLocal) {
		this.districtNameLocal = districtNameLocal;
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
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the stateId.
	 */
	public Integer getStateId() {
		return stateId;
	}

	/**
	 * @param stateId
	 *            The stateId to set.
	 */
	public void setStateId(Integer stateId) {
		this.stateId = stateId;
	}
	
}
