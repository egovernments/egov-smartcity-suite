/*
 * TaxRates.java Created on june 13, 2007
 *
 * Copyright 2007 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.ptis.domain.entity.property;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * This is the Master Objects which will have Information about the Tax
 * percentage to be applied to the Base Tax calculated for the Property. Tax
 * percentage depends on the ARV amount and peroid in between which it comes in
 * to existances.
 * 
 * @author Lokesh
 * @version 2.00
 */

public class TaxRates implements Serializable {

	private Integer id;
	private java.util.Date fromDate;
	private java.util.Date toDate;
	private float tax;
	private Integer type;
	private BigDecimal fromAmount;
	private BigDecimal toAmount;

	/**
	 * @return the fromAmount
	 */
	public BigDecimal getFromAmount() {
		return fromAmount;
	}

	/**
	 * @param fromAmount
	 *            the fromAmount to set
	 */
	public void setFromAmount(BigDecimal fromAmount) {
		this.fromAmount = fromAmount;
	}

	/**
	 * @return the fromDate
	 */
	public java.util.Date getFromDate() {
		return fromDate;
	}

	/**
	 * @param fromDate
	 *            the fromDate to set
	 */
	public void setFromDate(java.util.Date fromDate) {
		this.fromDate = fromDate;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the tax
	 */
	public Float getTax() {
		return tax;
	}

	/**
	 * @param tax
	 *            the tax to set
	 */
	public void setTax(Float tax) {
		this.tax = tax;
	}

	/**
	 * @return the toAmount
	 */
	public BigDecimal getToAmount() {
		return toAmount;
	}

	/**
	 * @param toAmount
	 *            the toAmount to set
	 */
	public void setToAmount(BigDecimal toAmount) {
		this.toAmount = toAmount;
	}

	/**
	 * @return the toDate
	 */
	public java.util.Date getToDate() {
		return toDate;
	}

	/**
	 * @param toDate
	 *            the toDate to set
	 */
	public void setToDate(java.util.Date toDate) {
		this.toDate = toDate;
	}

	/**
	 * @return the type
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	@Override
	public String toString() {
		StringBuilder objStr = new StringBuilder();

		objStr.append("Id: " + getId()).append("|Tax: ").append(getTax()).append("|Type: ").append(getType()).append(
				"|FromAmount: ").append(getFromAmount()).append("|ToAmount: ").append(getToAmount());

		return objStr.toString();
	}
}
