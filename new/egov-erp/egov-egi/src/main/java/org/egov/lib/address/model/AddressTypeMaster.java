/*
 * @(#)AddressTypeMaster.java 3.0, 7 Jun, 2013 8:58:37 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.address.model;

import java.util.Date;

public class AddressTypeMaster {
	private Integer addressTypeID = null;
	private String addressTypeName = null;
	private String addressTypeNameLocal = null;
	private Date updatedTimestamp = null;

	/**
	 * @return Returns the addressTypeID.
	 */
	public Integer getAddressTypeID() {
		return addressTypeID;
	}

	/**
	 * @param addressTypeID The addressTypeID to set.
	 */
	public void setAddressTypeID(Integer addressTypeID) {
		this.addressTypeID = addressTypeID;
	}

	/**
	 * @return Returns the addressTypeName.
	 */
	public String getAddressTypeName() {
		return addressTypeName;
	}

	/**
	 * @param addressTypeName The addressTypeName to set.
	 */
	public void setAddressTypeName(String addressTypeName) {
		this.addressTypeName = addressTypeName;
	}

	/**
	 * @return Returns the addressTypeNameLocal.
	 */
	public String getAddressTypeNameLocal() {
		return addressTypeNameLocal;
	}

	/**
	 * @param addressTypeNameLocal The addressTypeNameLocal to set.
	 */
	public void setAddressTypeNameLocal(String addressTypeNameLocal) {
		this.addressTypeNameLocal = addressTypeNameLocal;
	}

	/**
	 * @return Returns the updatedTimestamp.
	 */
	public Date getUpdatedTimestamp() {
		return updatedTimestamp;
	}

	/**
	 * @param updatedTimestamp The updatedTimestamp to set.
	 */
	public void setUpdatedTimestamp(Date updatedTimestamp) {
		this.updatedTimestamp = updatedTimestamp;
	}
}
