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
package org.egov.ptis.domain.model.calculator;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@XStreamAlias("taxcalculationinfo")
public abstract class TaxCalculationInfo {
	@XStreamAsAttribute
	private String propertyOwnerName;
	@XStreamAsAttribute
	private String propertyAddress;
	private String houseNumber;
	private String zone;
	private String ward;
	private String block;
	private String locality;
	private BigDecimal propertyArea;
	private BigDecimal totalTaxPayable;
	private String propertyType;
	private String propertyId;
	private String taxCalculationInfoXML;
	private BigDecimal totalNetARV;
	private Date occupencyDate;

	/**
	 * unitTaxCalculationInfos is a list of UnitTaxCalculation(s) In case of
	 * multiple UnitTaxCalculation each object represent tax calculation for
	 * different base rents
	 */
	@XStreamAlias("unittax")
	private List<UnitTaxCalculationInfo> unitTaxCalculationInfos = new ArrayList<UnitTaxCalculationInfo>();

	public String getPropertyOwnerName() {
		return propertyOwnerName;
	}

	public String getPropertyAddress() {
		return propertyAddress;
	}

	public String getHouseNumber() {
		return houseNumber;
	}

	public String getZone() {
		return zone;
	}

	public String getWard() {
		return ward;
	}

	public BigDecimal getTotalTaxPayable() {
		return totalTaxPayable;
	}

	public List<UnitTaxCalculationInfo> getUnitTaxCalculationInfos() {
		return unitTaxCalculationInfos;
	}

	public void setPropertyOwnerName(String propertyOwnerName) {
		this.propertyOwnerName = propertyOwnerName;
	}

	public void setPropertyAddress(String propertyAddress) {
		this.propertyAddress = propertyAddress;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public void setWard(String ward) {
		this.ward = ward;
	}

	public void setTotalTaxPayable(BigDecimal totalTaxPayable) {
		this.totalTaxPayable = totalTaxPayable;
	}

	public void setUnitTaxCalculationInfo(List<UnitTaxCalculationInfo> unitTaxCalculationInfos) {
		this.unitTaxCalculationInfos = unitTaxCalculationInfos;
	}

	public BigDecimal getPropertyArea() {
		return propertyArea;
	}

	public void setPropertyArea(BigDecimal propertyArea) {
		this.propertyArea = propertyArea;
	}

	public void addUnitTaxCalculationInfo(UnitTaxCalculationInfo unitTaxCalculationInfo) {
		getUnitTaxCalculationInfos().add(unitTaxCalculationInfo);
	}

	public String getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}

	public String getTaxCalculationInfoXML() {
		return taxCalculationInfoXML;
	}

	public void setTaxCalculationInfoXML(String taxCalculationInfoXML) {
		this.taxCalculationInfoXML = taxCalculationInfoXML;
	}

	public Date getOccupencyDate() {
		return occupencyDate;
	}

	public void setOccupencyDate(Date occupencyDate) {
		this.occupencyDate = occupencyDate;
	}

	public String getBlock() {
		return block;
	}

	public void setBlock(String block) {
		this.block = block;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	public BigDecimal getTotalNetARV() {
		return totalNetARV;
	}

	public void setTotalNetARV(BigDecimal totalNetARV) {
		this.totalNetARV = totalNetARV;
	}

	@Override
    public int hashCode() {
        int hashCode = this.propertyOwnerName.hashCode() + this.propertyAddress.hashCode()
                + this.houseNumber.hashCode() + this.locality.hashCode() + this.zone.hashCode() + this.ward.hashCode()
                + this.totalTaxPayable.hashCode() + this.propertyType.hashCode()
                + this.taxCalculationInfoXML.hashCode() + this.totalNetARV.hashCode();
        return hashCode;
    }

}
