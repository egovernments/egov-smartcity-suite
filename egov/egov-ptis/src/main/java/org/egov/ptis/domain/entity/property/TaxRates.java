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
