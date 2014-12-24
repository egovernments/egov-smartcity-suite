/*
 * @(#)Number.java 3.0, 18 Jun, 2013 12:08:27 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.utils;

import java.util.Date;

public class Number {
	private Long id;
	private String objectType;
	private long number;
	private String formattedNumber;
	private Date updatedtimestamp;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public long getNumber() {
		return number;
	}

	public void setNumber(long number) {
		this.number = number;
	}

	public Date getUpdatedtimestamp() {
		return updatedtimestamp;
	}

	public void setUpdatedtimestamp(Date updatedtimestamp) {
		this.updatedtimestamp = updatedtimestamp;
	}

	public String getFormattedNumber() {
		return formattedNumber;
	}

	public void setFormattedNumber(String formattedNumber) {
		this.formattedNumber = formattedNumber;
	}

}
