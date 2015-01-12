/*
 * @(#)AddressIF.java 3.0, 7 Jun, 2013 8:57:05 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.address.model;

public interface AddressIF {
	Integer getAddressID();

	String getStreetAddress1();

	String getStreetAddress2();

	String getBlock();

	String getLocality();

	String getCityTownVillage();

	String getDistrict();

	String getState();

	String getStreetAddress1Local();

	String getStreetAddress2Local();

	String getBlockLocal();

	String getLocalityLocal();

	String getCityTownVillageLocal();

	String getDistrictLocal();

	String getStateLocal();

	Integer getPinCode();

	String getHouseNo();

	void setHouseNo(String houseNo);
}
