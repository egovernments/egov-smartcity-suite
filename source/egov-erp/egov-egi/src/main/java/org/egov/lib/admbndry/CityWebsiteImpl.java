/*
 * @(#)CityWebsiteImpl.java 3.0, 16 Jun, 2013 3:34:24 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.admbndry;


public class CityWebsiteImpl implements CityWebsite {

	public static final String QUERY_CITY_BY_URL = "CITY_BY_URL";
	private Integer id;
	private Boundary boundaryId;
	private String cityName;
	private String cityNameLocal;
	private Integer isActive;
	private String cityBaseURL;
	private String logo;

	/**
	 * @return Returns the boundaryId.
	 */
	public Boundary getBoundaryId() {
		return boundaryId;
	}

	/**
	 * @param boundaryId The boundaryId to set.
	 */
	public void setBoundaryId(Boundary boundaryId) {
		this.boundaryId = boundaryId;
	}

	/**
	 * @return Returns the cityBaseURL.
	 */
	public String getCityBaseURL() {
		return cityBaseURL;
	}

	/**
	 * @param cityBaseURL The cityBaseURL to set.
	 */
	public void setCityBaseURL(String cityBaseURL) {
		this.cityBaseURL = cityBaseURL;
	}

	/**
	 * @return Returns the cityName.
	 */
	public String getCityName() {
		return cityName;
	}

	/**
	 * @param cityName The cityName to set.
	 */
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	/**
	 * @return Returns the cityNameLocal.
	 */
	public String getCityNameLocal() {
		return cityNameLocal;
	}

	/**
	 * @param cityNameLocal The cityNameLocal to set.
	 */
	public void setCityNameLocal(String cityNameLocal) {
		this.cityNameLocal = cityNameLocal;
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
	 * @see org.egov.lib.admbndry.CityWebsite#getCityNameHeading()
	 */

	public String getLogo() {
		return logo;
	}

	/**
	 * @see org.egov.lib.admbndry.CityWebsite#setCityNameHeading(java.lang.String)
	 */
	public void setLogo(String logo) {
		this.logo = logo;

	}
}
