/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */
package org.egov.lcms.masters.entity;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Max;

import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.infstr.models.BaseModel;
import org.egov.lcms.transactions.entity.Legalcase;
import org.hibernate.validator.constraints.Length;

/**
 * CourtMaster entity.
 *
 * @author srikanth
 */
@Unique(fields = { "courtName" }, id = "id", tableName = "EGLC_COURT_MASTER", columnName = {
		"COURT_NAME" }, message = "masters.courtMaster.isunique")
public class CourtMaster extends BaseModel {
	/**
	 * Serial version uid
	 */
	private static final long serialVersionUID = 1L;
	private Boolean active;

	public Boolean getActive() {
		return active;
	}

	public void setActive(final Boolean active) {
		this.active = active;
	}

	@Required(message = "masters.courtMaster.courtTypeSelect")
	private CourttypeMaster eglcCourttypeMaster;

	@Required(message = "masters.courtMaster.courtNameNull")
	@Length(max = 100, message = "masters.courtMaster.nameLength")
	// @OptionalPattern(regex = "[0-9a-zA-Z-&, .]+", message =
	// "masters.courtMaster.courtNamePattern2")
	private String courtName;

	@Required(message = "masters.courtMaster.courtAddressNull")
	@Length(max = 256, message = "masters.courtMaster.addressLength")
	private String courtAddress;
	@Max(value = 1000, message = "masters.orderNumber.length")
	private Long ordernumber;

	private Set<Legalcase> eglcLegalcases = new HashSet<Legalcase>(0);

	public CourttypeMaster getEglcCourttypeMaster() {
		return eglcCourttypeMaster;
	}

	public void setEglcCourttypeMaster(final CourttypeMaster eglcCourttypeMaster) {
		this.eglcCourttypeMaster = eglcCourttypeMaster;
	}

	public String getCourtName() {
		return courtName;
	}

	public void setCourtName(final String courtName) {
		this.courtName = courtName;
	}

	public String getCourtAddress() {
		return courtAddress;
	}

	public void setCourtAddress(final String courtAddress) {
		this.courtAddress = courtAddress;
	}

	public Long getOrdernumber() {
		return ordernumber;
	}

	public void setOrdernumber(final Long ordernumber) {
		this.ordernumber = ordernumber;
	}

	public Set<Legalcase> getEglcLegalcases() {
		return eglcLegalcases;
	}

	public void setEglcLegalcases(final Set<Legalcase> eglcLegalcases) {
		this.eglcLegalcases = eglcLegalcases;
	}

}