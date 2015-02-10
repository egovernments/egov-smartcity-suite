/*
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pims.commons;

import org.egov.commons.CChartOfAccounts;


public class DesignationMaster {
	
	private Integer designationId;
	private String designationName;
	private String designationDescription;
	private CChartOfAccounts chartOfAccounts;
	
	public DesignationMaster() {
	}
	
	public String getDesignationDescription() {
		return designationDescription;
	}
	
	public void setDesignationDescription(String designationDescription) {
		this.designationDescription = designationDescription;
	}
	public String getDesignationName() {
		return designationName;
	}
	
	public void setDesignationName(String designationName) {
		this.designationName = designationName;
	}

	public Integer getDesignationId() {
		return designationId;
	}

	public void setDesignationId(Integer designationId) {
		this.designationId = designationId;
	}

	public CChartOfAccounts getChartOfAccounts() {
		return chartOfAccounts;
	}

	public void setChartOfAccounts(CChartOfAccounts chartOfAccounts) {
		this.chartOfAccounts = chartOfAccounts;
	}
	
}
