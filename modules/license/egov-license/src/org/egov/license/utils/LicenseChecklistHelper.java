/*
 * @(#)LicenseChecklistHelper.java 3.0, 29 Jul, 2013 1:24:25 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.license.utils;

public class LicenseChecklistHelper {

	private Integer id;
	private String name;
	private String val;
	private String checked;

	public Integer getId() {
		return this.id;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getVal() {
		return this.val;
	}

	public void setVal(final String val) {
		this.val = val;
	}

	public String getChecked() {
		return this.checked;
	}

	public void setChecked(final String checked) {
		this.checked = checked;
	}

	public LicenseChecklistHelper(final String name, final String val, final String checked) {
		super();
		this.name = name;
		this.val = val;
		this.checked = checked;
	}
}
