/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
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
