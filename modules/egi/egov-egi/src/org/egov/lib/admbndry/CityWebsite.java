/*
 * @(#)CityWebsite.java 3.0, 16 Jun, 2013 3:29:41 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.admbndry;

public interface CityWebsite {

	Integer getId();

	void setId(Integer id);

	Boundary getBoundaryId();

	void setBoundaryId(Boundary boundaryId);

	Integer getIsActive();

	void setIsActive(Integer isActive);

	void setCityNameLocal(String cityNameLocal);

	String getCityNameLocal();

	void setCityName(String cityName);

	String getCityName();

	void setCityBaseURL(String cityBaseURL);

	String getCityBaseURL();

	void setLogo(String logo);

	String getLogo();

}
