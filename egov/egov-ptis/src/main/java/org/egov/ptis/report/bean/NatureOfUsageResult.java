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
package org.egov.ptis.report.bean;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.ptis.domain.entity.property.PropertyUsage;

import java.math.BigDecimal;
import java.util.List;

public class NatureOfUsageResult {
    private List<PropertyUsage> natureOfUsages;
    private List<Boundary> wards;
    private List<Boundary> blocks;
    private Long natureOfUsage;
    private Long ward;
    private Long block;
    private String assessmentNumber;
    private String ownerName;
    private String mobileNumber;
    private String doorNumber;
    private String address;
    private BigDecimal halfYearTax;

    public List<PropertyUsage> getNatureOfUsages() {
        return natureOfUsages;
    }

    public void setNatureOfUsages(final List<PropertyUsage> natureOfUsages) {
        this.natureOfUsages = natureOfUsages;
    }

    public List<Boundary> getWards() {
        return wards;
    }

    public void setWards(final List<Boundary> wards) {
        this.wards = wards;
    }

    public List<Boundary> getBlocks() {
        return blocks;
    }

    public void setBlocks(final List<Boundary> blocks) {
        this.blocks = blocks;
    }

    public Long getNatureOfUsage() {
        return natureOfUsage;
    }

    public void setNatureOfUsage(final Long natureOfUsage) {
        this.natureOfUsage = natureOfUsage;
    }

    public Long getWard() {
        return ward;
    }

    public void setWard(final Long ward) {
        this.ward = ward;
    }

    public Long getBlock() {
        return block;
    }

    public void setBlock(final Long block) {
        this.block = block;
    }

    public String getAssessmentNumber() {
        return assessmentNumber;
    }

    public void setAssessmentNumber(final String assessmentNumber) {
        this.assessmentNumber = assessmentNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(final String ownerName) {
        this.ownerName = ownerName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(final String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getDoorNumber() {
        return doorNumber;
    }

    public void setDoorNumber(final String doorNumber) {
        this.doorNumber = doorNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public BigDecimal getHalfYearTax() {
        return halfYearTax;
    }

    public void setHalfYearTax(final BigDecimal halfYearTax) {
        this.halfYearTax = halfYearTax;
    }

}
