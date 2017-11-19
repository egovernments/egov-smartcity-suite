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
package org.egov.wtms.reports.entity;

import java.math.BigDecimal;

public class ArrearRegisterReport {
    private String zoneName;
    private String zone;
    private String name;
    private String ward;
    private String block;
    private String locality;
    private String boundaryname;

    private Long basicPropId;
    private String indexNumber;
    private BigDecimal waterCharge;
    private BigDecimal waterChargeColl;
    private String ownerName;
    private String houseNo;
    private String arrearInstallmentDesc;
    private BigDecimal arrearLibraryCess;
    private BigDecimal arrearPropertyTax;
    private BigDecimal arrearPenalty;
    private BigDecimal totalArrearTax;
    private BigDecimal arrearEducationCess;
    private BigDecimal arrearVacantLandTax;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getBoundaryname() {
        return boundaryname;
    }

    public void setBoundaryname(final String boundaryname) {
        this.boundaryname = boundaryname;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(final String zoneName) {
        this.zoneName = zoneName;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(final String ward) {
        this.ward = ward;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(final String block) {
        this.block = block;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(final String zone) {
        this.zone = zone;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(final String locality) {
        this.locality = locality;
    }

    public Long getBasicPropId() {
        return basicPropId;
    }

    public void setBasicPropId(final Long basicPropId) {
        this.basicPropId = basicPropId;
    }

    public String getIndexNumber() {
        return indexNumber;
    }

    public void setIndexNumber(final String indexNumber) {
        this.indexNumber = indexNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(final String ownerName) {
        this.ownerName = ownerName;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(final String houseNo) {
        this.houseNo = houseNo;
    }

    public String getArrearInstallmentDesc() {
        return arrearInstallmentDesc;
    }

    public void setArrearInstallmentDesc(final String arrearInstallmentDesc) {
        this.arrearInstallmentDesc = arrearInstallmentDesc;
    }

    public BigDecimal getArrearLibraryCess() {
        return arrearLibraryCess;
    }

    public void setArrearLibraryCess(final BigDecimal arrearLibraryCess) {
        this.arrearLibraryCess = arrearLibraryCess;
    }

    public BigDecimal getArrearPropertyTax() {
        return arrearPropertyTax;
    }

    public void setArrearPropertyTax(final BigDecimal arrearPropertyTax) {
        this.arrearPropertyTax = arrearPropertyTax;
    }

    public BigDecimal getArrearPenalty() {
        return arrearPenalty;
    }

    public void setArrearPenalty(final BigDecimal arrearPenalty) {
        this.arrearPenalty = arrearPenalty;
    }

    public BigDecimal getTotalArrearTax() {
        return totalArrearTax;
    }

    public void setTotalArrearTax(final BigDecimal totalArrearTax) {
        this.totalArrearTax = totalArrearTax;
    }

    public BigDecimal getArrearEducationCess() {
        return arrearEducationCess;
    }

    public void setArrearEducationCess(final BigDecimal arrearEducationCess) {
        this.arrearEducationCess = arrearEducationCess;
    }

    public BigDecimal getArrearVacantLandTax() {
        return arrearVacantLandTax;
    }

    public void setArrearVacantLandTax(final BigDecimal arrearVacantLandTax) {
        this.arrearVacantLandTax = arrearVacantLandTax;
    }

    public BigDecimal getWaterCharge() {
        return waterCharge;
    }

    public void setWaterCharge(final BigDecimal waterCharge) {
        this.waterCharge = waterCharge;
    }

    public BigDecimal getWaterChargeColl() {
        return waterChargeColl;
    }

    public void setWaterChargeColl(final BigDecimal waterChargeColl) {
        this.waterChargeColl = waterChargeColl;
    }

}
