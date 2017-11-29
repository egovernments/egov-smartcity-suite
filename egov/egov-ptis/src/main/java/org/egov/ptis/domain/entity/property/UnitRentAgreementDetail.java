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

import org.hibernate.validator.constraints.Range;

import java.util.Date;

/**
 * Represents the rent agreement details for the unit
 * 
 * @author nayeem
 *
 */
public class UnitRentAgreementDetail{
	
	private Long id;
	private Floor floor;
	
	/**
	 * The period the rent agreement is valid
	 */
	@Range(min=1, max=12)
	private Byte agreementPeriod;
	
	/**
	 * The Date on which rent agreement was made
	 */
	private Date agreementDate;
	
	/**
	 * The percentage in which the rent has to increased
	 */
	@Range(min=1, max=100)
	private Byte incrementInRent;
	
	public String toString() {
		return new StringBuilder(250).append("UnitRentAgreementDetail [")
				.append("id=").append(id)
				.append(", agreementPeriod=").append(agreementPeriod)
				.append(", agreementDate=").append(agreementDate)
				.append(", incrementInRent=").append(incrementInRent).toString();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Floor getFloor() {
		return floor;
	}

	public void setFloor(Floor floor) {
		this.floor = floor;
	}

	public Byte getAgreementPeriod() {
		return agreementPeriod;
	}

	public void setAgreementPeriod(Byte agreementPeriod) {
		this.agreementPeriod = agreementPeriod;
	}

	public Date getAgreementDate() {
		return agreementDate;
	}

	public void setAgreementDate(Date agreementDate) {
		this.agreementDate = agreementDate;
	}

	public Byte getIncrementInRent() {
		return incrementInRent;
	}

	public void setIncrementInRent(Byte incrementInRent) {
		this.incrementInRent = incrementInRent;
	}
}

