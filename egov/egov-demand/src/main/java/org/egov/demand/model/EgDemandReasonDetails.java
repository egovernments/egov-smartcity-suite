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
package org.egov.demand.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * EgDemandReasonDetails entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class EgDemandReasonDetails implements java.io.Serializable {

	// Fields

	private Long id;
	private EgDemandReason egDemandReason;
	// ToDo: Make it Float
	private BigDecimal percentage;
	private Date fromDate;
	private Date toDate;
	private BigDecimal lowLimit;
	private BigDecimal highLimit;
	private Date createDate;
	private Date modifiedDate;
	private BigDecimal flatAmount;
	private Integer isFlatAmntMax;

	// Property accessors

	@Override
	public Object clone() {
		EgDemandReasonDetails clone = null;
		try {
			clone = (EgDemandReasonDetails) super.clone();
		} catch (CloneNotSupportedException e) {
			// this should never happen
			throw new InternalError(e.toString());
		}
		clone.setId(null);
		return clone;
	}

	public BigDecimal getFlatAmount() {
		return flatAmount;
	}

	public void setFlatAmount(BigDecimal flatAmount) {
		this.flatAmount = flatAmount;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EgDemandReason getEgDemandReason() {
		return this.egDemandReason;
	}

	public void setEgDemandReason(EgDemandReason egDemandReason) {
		this.egDemandReason = egDemandReason;
	}

	public BigDecimal getPercentage() {
		return this.percentage;
	}

	public void setPercentage(BigDecimal percentage) {
		this.percentage = percentage;
	}

	public Date getFromDate() {
		return this.fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return this.toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public BigDecimal getLowLimit() {
		return this.lowLimit;
	}

	public void setLowLimit(BigDecimal lowLimit) {
		this.lowLimit = lowLimit;
	}

	public BigDecimal getHighLimit() {
		return this.highLimit;
	}

	public void setHighLimit(BigDecimal highLimit) {
		this.highLimit = highLimit;
	}

	public Integer getIsFlatAmntMax() {
		return isFlatAmntMax;
	}

	public void setIsFlatAmntMax(Integer isFlatAmntMax) {
		this.isFlatAmntMax = isFlatAmntMax;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

}