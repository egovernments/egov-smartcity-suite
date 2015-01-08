/*
 * @(#)Sequence.java 3.0, 18 Jun, 2013 12:11:24 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.utils;

public class Sequence {
	private final long number;
	private final String objectType;
	private final String formattedNumber;

	public Sequence(final String objectType, final long number, final String formattedNumber) {
		this.objectType = objectType;
		this.number = number;
		this.formattedNumber = formattedNumber;
	}

	public Long getNumber() {
		return this.number;
	}

	public String getObjectType() {
		return this.objectType;
	}

	public String getFormattedNumber() {
		return this.formattedNumber;
	}

}
