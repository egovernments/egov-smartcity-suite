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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.Unique;

@Entity
@Table(name = "EGPT_APARTMENT")
@Unique(columnName = { "code" }, fields = { "code" }, enableDfltMsg = true)

@SequenceGenerator(name = Apartment.SEQ_APARTMENT, sequenceName = Apartment.SEQ_APARTMENT, allocationSize = 1)
public class Apartment extends AbstractAuditable {

	private static final long serialVersionUID = 1L;
	public static final String SEQ_APARTMENT = "SEQ_EGPT_APARTMENT";

	@Id
	@GeneratedValue(generator = SEQ_APARTMENT, strategy = GenerationType.SEQUENCE)
	private Long id;

	@NotNull
	private String name;

	@NotNull
	private String code;

	private Double builtUpArea;

	@NotNull
	private Integer totalProperties;

	@NotNull
	private Integer totalFloors;

	private Double openSpaceArea;

	private Boolean liftFacility;

	private Boolean powerBackup;

	private Boolean parkingFacility;

	private Boolean fireFightingFacility;

	private Integer totalResidentialProperties;

	private Integer totalNonResidentialProperties;

	@NotNull
	private String sourceOfWater;

	@NotNull
	private String type;

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Double getBuiltUpArea() {
		return builtUpArea;
	}

	public String getCode() {
		return code;
	}

	public void setCode(final String code) {
		this.code = code.toUpperCase();
	}

	public void setBuiltUpArea(final Double builtUpArea) {
		this.builtUpArea = builtUpArea;
	}

	public Integer getTotalProperties() {
		return totalProperties;
	}

	public void setTotalProperties(final Integer totalProperties) {
		this.totalProperties = totalProperties;
	}

	public Integer getTotalFloors() {
		return totalFloors;
	}

	public void setTotalFloors(final Integer totalFloors) {
		this.totalFloors = totalFloors;
	}

	public Double getOpenSpaceArea() {
		return openSpaceArea;
	}

	public void setOpenSpaceArea(final Double openSpaceArea) {
		this.openSpaceArea = openSpaceArea;
	}

	public Boolean getLiftFacility() {
		return liftFacility;
	}

	public void setLiftFacility(final Boolean liftFacility) {
		this.liftFacility = liftFacility;
	}

	public Boolean getPowerBackup() {
		return powerBackup;
	}

	public void setPowerBackup(final Boolean powerBackup) {
		this.powerBackup = powerBackup;
	}

	public Boolean getParkingFacility() {
		return parkingFacility;
	}

	public void setParkingFacility(final Boolean parkingFacility) {
		this.parkingFacility = parkingFacility;
	}

	public Boolean getFireFightingFacility() {
		return fireFightingFacility;
	}

	public void setFireFightingFacility(final Boolean fireFightingFacility) {
		this.fireFightingFacility = fireFightingFacility;
	}

	public Integer getTotalResidentialProperties() {
		return totalResidentialProperties;
	}

	public void setTotalResidentialProperties(final Integer totalResidentialProperties) {
		this.totalResidentialProperties = totalResidentialProperties;
	}

	public Integer getTotalNonResidentialProperties() {
		return totalNonResidentialProperties;
	}

	public void setTotalNonResidentialProperties(final Integer totalNonResidentialProperties) {
		this.totalNonResidentialProperties = totalNonResidentialProperties;
	}

	public String getSourceOfWater() {
		return sourceOfWater;
	}

	public void setSourceOfWater(final String sourceOfWater) {
		this.sourceOfWater = sourceOfWater;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(final Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(final String type) {
		this.type = type;
	}
}
