/*
 * @(#)Address.java 3.0, 7 Jun, 2013 8:56:36 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.address.model;

import static org.apache.commons.lang.StringUtils.isBlank;

import java.util.Date;

public class Address implements AddressIF {

	public Integer addressID;
	private String streetAddress1;
	private String streetAddress2;
	private String block;
	private String locality;
	private String cityTownVillage;
	private String district;
	private String state;
	private String streetAddress1Local;
	private String streetAddress2Local;
	private String blockLocal;
	private String localityLocal;
	private String cityTownVillageLocal;
	private String districtLocal;
	private String stateLocal;
	private Integer pinCode;
	private Date lastUpdatedTimeStamp;
	private String taluk;
	private String talukLocal;
	private String houseNo;

	private AddressTypeMaster addTypeMaster;

	public String toString() {
		final StringBuilder address = new StringBuilder();
		address.append(isBlank(houseNo) ? "" : houseNo + ",\n");
		address.append(isBlank(streetAddress1) ? "" : streetAddress1 + ",\n");
		address.append(isBlank(streetAddress1Local) ? "" : streetAddress1Local + ",\n");
		address.append(isBlank(streetAddress2) ? "" : streetAddress2 + ",\n");
		address.append(isBlank(streetAddress2Local) ? "" : streetAddress2Local + ",\n");
		address.append(isBlank(block) ? "" : block + ",\n");
		address.append(isBlank(blockLocal) ? "" : blockLocal + ",\n");
		address.append(isBlank(locality) ? "" : locality + "\n");
		address.append(isBlank(localityLocal) ? "" : localityLocal + ",\n");
		address.append(isBlank(cityTownVillage) ? "" : cityTownVillage + ",\n");
		address.append(isBlank(cityTownVillageLocal) ? "" : cityTownVillageLocal + ",\n");
		address.append(isBlank(taluk) ? "" : taluk + ",\n");
		address.append(isBlank(talukLocal) ? "" : talukLocal + ",\n");
		address.append(isBlank(district) ? "" : district + ",\n");
		address.append(isBlank(districtLocal) ? "" : districtLocal + ",\n");
		address.append(isBlank(state) ? "" : state + ",\n");
		address.append(isBlank(stateLocal) ? "" : stateLocal + ",\n");
		address.append(pinCode == null ? "" : "Pin : " + pinCode);
		return address.toString();
	}

	/**
	 * @return Returns the addressID.
	 */
	public Integer getAddressID() {
		return addressID;
	}

	/**
	 * @return Returns the block.
	 */
	public String getBlock() {
		return block;
	}

	/**
	 * @return Returns the blockLocal.
	 */
	public String getBlockLocal() {
		return blockLocal;
	}

	/**
	 * @return Returns the cityTownVillage.
	 */
	public String getCityTownVillage() {
		return cityTownVillage;
	}

	/**
	 * @return Returns the cityTownVillageLocal.
	 */
	public String getCityTownVillageLocal() {
		return cityTownVillageLocal;
	}

	/**
	 * @return Returns the district.
	 */
	public String getDistrict() {
		return district;
	}

	/**
	 * @return Returns the houseNo.
	 */
	public String getHouseNo() {
		return houseNo;
	}

	/**
	 * @param houseNo The houseNo to set.
	 */
	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	/**
	 * @return Returns the districtLocal.
	 */
	public String getDistrictLocal() {
		return districtLocal;
	}

	/**
	 * @return Returns the locality.
	 */
	public String getLocality() {
		return locality;
	}

	/**
	 * @return Returns the localityLocal.
	 */
	public String getLocalityLocal() {
		return localityLocal;
	}

	/**
	 * @return Returns the pinCode.
	 */
	public Integer getPinCode() {
		return pinCode;
	}

	/**
	 * @return Returns the state.
	 */
	public String getState() {
		return state;
	}

	/**
	 * @return Returns the stateLocal.
	 */
	public String getStateLocal() {
		return stateLocal;
	}

	/**
	 * @return Returns the streetAddress1.
	 */
	public String getStreetAddress1() {
		return streetAddress1;
	}

	/**
	 * @return Returns the streetAddress1Local.
	 */
	public String getStreetAddress1Local() {
		return streetAddress1Local;
	}

	/**
	 * @return Returns the streetAddress2.
	 */
	public String getStreetAddress2() {
		return streetAddress2;
	}

	/**
	 * @return Returns the streetAddress2Local.
	 */
	public String getStreetAddress2Local() {
		return streetAddress2Local;
	}

	/**
	 * @param addressID The addressID to set.
	 */
	public void setAddressID(Integer addressID) {
		this.addressID = addressID;
	}

	/**
	 * @param block The block to set.
	 */
	public void setBlock(String block) {
		this.block = block;
	}

	/**
	 * @param blockLocal The blockLocal to set.
	 */
	public void setBlockLocal(String blockLocal) {
		this.blockLocal = blockLocal;
	}

	/**
	 * @param cityTownVillage The cityTownVillage to set.
	 */
	public void setCityTownVillage(String cityTownVillage) {
		this.cityTownVillage = cityTownVillage;
	}

	/**
	 * @param cityTownVillageLocal The cityTownVillageLocal to set.
	 */
	public void setCityTownVillageLocal(String cityTownVillageLocal) {
		this.cityTownVillageLocal = cityTownVillageLocal;
	}

	/**
	 * @param district The district to set.
	 */
	public void setDistrict(String district) {
		this.district = district;
	}

	/**
	 * @param districtLocal The districtLocal to set.
	 */
	public void setDistrictLocal(String districtLocal) {
		this.districtLocal = districtLocal;
	}

	/**
	 * @param locality The locality to set.
	 */
	public void setLocality(String locality) {
		this.locality = locality;
	}

	/**
	 * @param localityLocal The localityLocal to set.
	 */
	public void setLocalityLocal(String localityLocal) {
		this.localityLocal = localityLocal;
	}

	/**
	 * @param pinCode The pinCode to set.
	 */
	public void setPinCode(Integer pinCode) {
		this.pinCode = pinCode;
	}

	/**
	 * @param state The state to set.
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @param stateLocal The stateLocal to set.
	 */
	public void setStateLocal(String stateLocal) {
		this.stateLocal = stateLocal;
	}

	/**
	 * @param streetAddress1 The streetAddress1 to set.
	 */
	public void setStreetAddress1(String streetAddress1) {
		this.streetAddress1 = streetAddress1;
	}

	/**
	 * @param streetAddress1Local The streetAddress1Local to set.
	 */
	public void setStreetAddress1Local(String streetAddress1Local) {
		this.streetAddress1Local = streetAddress1Local;
	}

	/**
	 * @param streetAddress2 The streetAddress2 to set.
	 */
	public void setStreetAddress2(String streetAddress2) {
		this.streetAddress2 = streetAddress2;
	}

	/**
	 * @param streetAddress2Local The streetAddress2Local to set.
	 */
	public void setStreetAddress2Local(String streetAddress2Local) {
		this.streetAddress2Local = streetAddress2Local;
	}

	/**
	 * @return Returns the lastUpdatedTimeStamp.
	 */
	public Date getLastUpdatedTimeStamp() {
		return lastUpdatedTimeStamp;
	}

	/**
	 * @param lastUpdatedTimeStamp The lastUpdatedTimeStamp to set.
	 */
	public void setLastUpdatedTimeStamp(Date lastUpdatedTimeStamp) {
		this.lastUpdatedTimeStamp = lastUpdatedTimeStamp;
	}

	public Address() {
	}

	public Address(Integer addressID, String streetAddress1, String streetAddress2, String block, String locality, String cityTownVillage, String district, String state, String streetAddress1Local, String streetAddress2Local, String blockLocal,
			String localityLocal, String cityTownVillageLocal, String districtLocal, String stateLocal, String taluk, String talukLocal, String houseNo, Integer pinCode, Date lastUpdatedTimeStamp) {
		this(addressID, streetAddress1, streetAddress2, block, locality, cityTownVillage, district, state, streetAddress1Local, streetAddress2Local, blockLocal, localityLocal, cityTownVillageLocal, districtLocal, stateLocal, pinCode, lastUpdatedTimeStamp);
		this.taluk = taluk;
		this.talukLocal = talukLocal;
		this.houseNo = houseNo;
	}

	/**
	 * @param addressID
	 * @param streetAddress1
	 * @param streetAddress2
	 * @param block
	 * @param locality
	 * @param cityTownVillage
	 * @param district
	 * @param state
	 * @param streetAddress1Local
	 * @param streetAddress2Local
	 * @param blockLocal
	 * @param localityLocal
	 * @param cityTownVillageLocal
	 * @param districtLocal
	 * @param stateLocal
	 * @param pinCode
	 * @param lastUpdatedTimeStamp
	 */
	public Address(Integer addressID, String streetAddress1, String streetAddress2, String block, String locality, String cityTownVillage, String district, String state, String streetAddress1Local, String streetAddress2Local, String blockLocal,
			String localityLocal, String cityTownVillageLocal, String districtLocal, String stateLocal, Integer pinCode, Date lastUpdatedTimeStamp) {
		super();
		this.addressID = addressID;
		this.streetAddress1 = streetAddress1;
		this.streetAddress2 = streetAddress2;
		this.block = block;
		this.locality = locality;
		this.cityTownVillage = cityTownVillage;
		this.district = district;
		this.state = state;
		this.streetAddress1Local = streetAddress1Local;
		this.streetAddress2Local = streetAddress2Local;
		this.blockLocal = blockLocal;
		this.localityLocal = localityLocal;
		this.cityTownVillageLocal = cityTownVillageLocal;
		this.districtLocal = districtLocal;
		this.stateLocal = stateLocal;
		this.pinCode = pinCode;
		this.lastUpdatedTimeStamp = lastUpdatedTimeStamp;
	}

	/**
	 * @return Returns if the given Object is equal to Address
	 */
	public boolean equals(Object that) {
		if (that == null)
			return false;

		if (this == that)
			return true;

		// NOTE :The instanceof operator condition fails to return false if the argument is a subclass of the class
		if (that.getClass() != this.getClass())
			return false;
		final Address thatAddress = (Address) that;

		if (this.getAddressID() != null && thatAddress.getAddressID() != null) {
			if (getAddressID().toString().equals(thatAddress.getAddressID().toString())) {
				return true;
			} else
				return false;
		} else
			return false;

	}

	/**
	 * @return Returns the hashCode
	 */
	public int hashCode() {
		int hashCode = 0;
		if (this.getAddressID() != null) {
			hashCode += this.getAddressID().toString().hashCode();
		}
		return hashCode;
	}

	/**
	 * @return Returns the boolean after validating the current object
	 */
	public boolean validate() {
		/*
		 * if(getAddressID() == 0) throw new EGOVRuntimeException("AddressID is Invalid, Please Check !!");
		 */
		return true;
	}

	/**
	 * @return Returns the addTypeMaster.
	 */
	public AddressTypeMaster getAddTypeMaster() {
		return addTypeMaster;
	}

	/**
	 * @param addTypeMaster The addTypeMaster to set.
	 */
	public void setAddTypeMaster(AddressTypeMaster addTypeMaster) {
		this.addTypeMaster = addTypeMaster;
	}

	/**
	 * @return Returns the taluk.
	 */
	public String getTaluk() {
		return taluk;
	}

	/**
	 * @param taluk The taluk to set.
	 */
	public void setTaluk(String taluk) {
		this.taluk = taluk;
	}

	/**
	 * @return Returns the talukLocal.
	 */
	/**
	 * @return Returns the talukLocal.
	 */
	public String getTalukLocal() {
		return talukLocal;
	}

	/**
	 * @param talukLocal The talukLocal to set.
	 */
	public void setTalukLocal(String talukLocal) {
		this.talukLocal = talukLocal;
	}

}