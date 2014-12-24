/*
 * @(#)CGeneralLedger.java 3.0, 6 Jun, 2013 3:05:11 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class CGeneralLedger {
	
	private Long id = null;
	private Integer voucherlineId;
	private Date effectiveDate;
	private CChartOfAccounts glcodeId;
	private String glcode;
	private Double debitAmount;
	private Double creditAmount;
	private String description;
	private CVoucherHeader voucherHeaderId;
	private Integer functionId;
	private Set<CGeneralLedgerDetail> generalLedgerDetails = new HashSet<CGeneralLedgerDetail>();

	/**
	 * @return Returns the glcode.
	 */
	public String getGlcode() {
		return glcode;
	}

	/**
	 * @param glcode The glcode to set.
	 */
	public void setGlcode(String glcode) {
		this.glcode = glcode;
	}

	/**
	 * @return Returns the id.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return Returns the effectiveDate.
	 */
	public Date getEffectiveDate() {
		return effectiveDate;
	}

	/**
	 * @param effectiveDate The effectiveDate to set.
	 */
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	/**
	 * @return Returns the voucherlineId.
	 */

	public Integer getVoucherlineId() {
		return voucherlineId;
	}

	/**
	 * @param voucherlineId The voucherlineId to set.
	 */
	public void setVoucherlineId(Integer voucherlineId) {
		this.voucherlineId = voucherlineId;
	}

	/**
	 * @return Returns the glcodeId.
	 */

	public CChartOfAccounts getGlcodeId() {
		return glcodeId;
	}

	/**
	 * @param glcodeId The glcodeId to set.
	 */
	public void setGlcodeId(CChartOfAccounts glcodeId) {
		this.glcodeId = glcodeId;
	}

	/**
	 * @return Returns the voucherHeaderId.
	 */

	public CVoucherHeader getVoucherHeaderId() {
		return voucherHeaderId;
	}

	/**
	 * @param voucherHeaderId The voucherHeaderId to set.
	 */
	public void setVoucherHeaderId(CVoucherHeader voucherHeaderId) {
		this.voucherHeaderId = voucherHeaderId;
	}

	/**
	 * @return Returns the functionId.
	 */

	public Integer getFunctionId() {
		return functionId;
	}

	/**
	 * @param functionId The functionId to set.
	 */
	public void setFunctionId(Integer functionId) {
		this.functionId = functionId;
	}

	/**
	 * @return Returns the debitAmount.
	 */

	public Double getDebitAmount() {
		return debitAmount;
	}

	/**
	 * @param debitAmount The debitAmount to set.
	 */
	public void setDebitAmount(Double debitAmount) {
		this.debitAmount = debitAmount;
	}

	/**
	 * @return Returns the creditAmount.
	 */

	public Double getCreditAmount() {
		return creditAmount;
	}

	/**
	 * @param creditAmount The creditAmount to set.
	 */
	public void setCreditAmount(Double creditAmount) {
		this.creditAmount = creditAmount;
	}

	public Set<CGeneralLedgerDetail> getGeneralLedgerDetails() {
		return generalLedgerDetails;
	}

	public void setGeneralLedgerDetails(Set<CGeneralLedgerDetail> generalLedgerDetails) {
		this.generalLedgerDetails = generalLedgerDetails;
	}

}
