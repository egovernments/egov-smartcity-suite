/*
 * @(#)Taluk.java 3.0, 7 Jun, 2013 8:47:44 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.mdm.masters.administration;

public class Taluk {

	private Integer id;

	private String name;

	private String talukNameLocal;

	private String talukConst;

	private Integer stateId;

	private Integer districtId;

	/**
	 * @return Returns the districtId.
	 */
	public Integer getDistrictId() {
		return districtId;
	}

	/**
	 * @param districtId The districtId to set.
	 */
	public void setDistrictId(Integer districtId) {
		this.districtId = districtId;
	}

	/**
	 * @return Returns the talukConst.
	 */
	public String getTalukConst() {
		return talukConst;
	}

	/**
	 * @param talukConst The talukConst to set.
	 */
	public void setTalukConst(String talukConst) {
		this.talukConst = talukConst;
	}

	/**
	 * @return Returns the talukNameLocal.
	 */
	public String getTalukNameLocal() {
		return talukNameLocal;
	}

	/**
	 * @param talukNameLocal The talukNameLocal to set.
	 */
	public void setTalukNameLocal(String talukNameLocal) {
		this.talukNameLocal = talukNameLocal;
	}

	/**
	 * @return Returns the id.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id The id to set.
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
	 * @param name The name to set.
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
	 * @param stateId The stateId to set.
	 */
	public void setStateId(Integer stateId) {
		this.stateId = stateId;
	}

}
