/*
 * TaxPerc.java Created on Dec 27, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.ptis.domain.entity.property;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * This is the Master Objects which will have Information about the Tax
 * percentage to be applied to the Base Tax calculated for the Property. Tax
 * percentage depends on the Category of the Property and Usage of the Property
 * 
 * @author Neetu
 * @version 2.00
 */

public class TaxPerc implements Serializable {

	private Integer id;
	private Category category;
	private PropertyUsage propertyUsage;
	private Float tax_perc;
	private BigDecimal fromAmt;
	private BigDecimal toAmt;
	private Date fromDate;
	private Date toDate;

	/**
	 * @return Returns the category.
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * @param category
	 *            The category to set.
	 */
	public void setCategory(Category category) {
		this.category = category;
	}

	/**
	 * @return Returns the propertyUsage.
	 */
	public PropertyUsage getPropertyUsage() {
		return propertyUsage;
	}

	/**
	 * @param propertyUsage
	 *            The propertyUsage to set.
	 */
	public void setPropertyUsage(PropertyUsage propertyUsage) {
		this.propertyUsage = propertyUsage;
	}

	/**
	 * @return Returns the id.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return Returns the tax_perc.
	 */
	public Float getTax_perc() {
		return tax_perc;
	}

	/**
	 * @param tax_perc
	 *            The tax_perc to set.
	 */
	public void setTax_perc(Float tax_perc) {
		this.tax_perc = tax_perc;
	}

	public BigDecimal getFromAmt() {
		return fromAmt;
	}

	public void setFromAmt(BigDecimal fromAmt) {
		this.fromAmt = fromAmt;
	}

	public BigDecimal getToAmt() {
		return toAmt;
	}

	public void setToAmt(BigDecimal toAmt) {
		this.toAmt = toAmt;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	@Override
	public String toString() {
		StringBuilder objStr = new StringBuilder();

		objStr.append("Id: " + getId()).append("|Category: ").append(getCategory()).append("|PropertyUsage: ").append(
				getPropertyUsage()).append("|TaxPerc: ").append(getTax_perc());

		return objStr.toString();
	}
}
