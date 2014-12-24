/*
 * @(#)CChartOfAccountDetail.java 3.0, 6 Jun, 2013 2:55:27 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons;

import org.egov.infstr.models.BaseModel;

public class CChartOfAccountDetail extends BaseModel {

	private static final long serialVersionUID = 1L;
	private Long id;
	private CChartOfAccounts glCodeId;
	private Accountdetailtype detailTypeId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CChartOfAccounts getGlCodeId() {
		return glCodeId;
	}

	public void setGlCodeId(CChartOfAccounts glCodeId) {
		this.glCodeId = glCodeId;
	}

	public Accountdetailtype getDetailTypeId() {
		return detailTypeId;
	}

	public void setDetailTypeId(Accountdetailtype detailTypeId) {
		this.detailTypeId = detailTypeId;
	}
}
