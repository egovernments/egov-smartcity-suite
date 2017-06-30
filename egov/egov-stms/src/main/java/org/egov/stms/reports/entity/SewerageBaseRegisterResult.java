/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2017>  eGovernments Foundation
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

package org.egov.stms.reports.entity;

import java.math.BigDecimal;
import java.util.List;

import org.egov.infra.admin.master.entity.Boundary;

public class SewerageBaseRegisterResult {

    private String shscNumber;
    private String assementNo;
    private String ownerName;
    private List<Boundary> wards;
    private String revenueWard;
    private String doorNo;
    private String propertyType;
    private Integer residentialClosets;
    private Integer nonResidentialClosets;
    private String period;
    private BigDecimal arrears = BigDecimal.ZERO;
    private BigDecimal currentDemand = BigDecimal.ZERO;
    private BigDecimal advanceAmount = BigDecimal.ZERO;
    private BigDecimal arrearsCollected = BigDecimal.ZERO;
    private BigDecimal currentTaxCollected = BigDecimal.ZERO;
    private BigDecimal totalTaxCollected = BigDecimal.ZERO;
    private BigDecimal totalDemand = BigDecimal.ZERO;

    public String getShscNumber() {
        return shscNumber;
    }

    public void setShscNumber(final String shscNumber) {
        this.shscNumber = shscNumber;
    }

    public String getAssementNo() {
        return assementNo;
    }

    public void setAssementNo(final String assementNo) {
        this.assementNo = assementNo;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(final String ownerName) {
        this.ownerName = ownerName;
    }

    public String getDoorNo() {
        return doorNo;
    }

    public void setDoorNo(final String doorNo) {
        this.doorNo = doorNo;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(final String propertyType) {
        this.propertyType = propertyType;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(final String period) {
        this.period = period;
    }

    public BigDecimal getArrears() {
        return arrears;
    }

    public void setArrears(final BigDecimal arrears) {
        this.arrears = arrears;
    }

    public BigDecimal getTotalDemand() {
        return totalDemand;
    }

    public void setTotalDemand(final BigDecimal totalDemand) {
        this.totalDemand = totalDemand;
    }

    public Integer getResidentialClosets() {
        return residentialClosets;
    }

    public void setResidentialClosets(final Integer residentialClosets) {
        this.residentialClosets = residentialClosets;
    }

    public Integer getNonResidentialClosets() {
        return nonResidentialClosets;
    }

    public void setNonResidentialClosets(final Integer nonResidentialClosets) {
        this.nonResidentialClosets = nonResidentialClosets;
    }

    public List<Boundary> getWards() {
        return wards;
    }

    public void setWards(final List<Boundary> wards) {
        this.wards = wards;
    }

    public String getRevenueWard() {
        return revenueWard;
    }

    public void setRevenueWard(final String revenueWard) {
        this.revenueWard = revenueWard;
    }

    public BigDecimal getCurrentDemand() {
        return currentDemand;
    }

    public void setCurrentDemand(final BigDecimal currentDemand) {
        this.currentDemand = currentDemand;
    }

    public BigDecimal getAdvanceAmount() {
        return advanceAmount;
    }

    public void setAdvanceAmount(final BigDecimal advanceAmount) {
        this.advanceAmount = advanceAmount;
    }

    public BigDecimal getArrearsCollected() {
        return arrearsCollected;
    }

    public void setArrearsCollected(final BigDecimal arrearsCollected) {
        this.arrearsCollected = arrearsCollected;
    }

    public BigDecimal getCurrentTaxCollected() {
        return currentTaxCollected;
    }

    public void setCurrentTaxCollected(final BigDecimal currentTaxCollected) {
        this.currentTaxCollected = currentTaxCollected;
    }

    public BigDecimal getTotalTaxCollected() {
        return totalTaxCollected;
    }

    public void setTotalTaxCollected(final BigDecimal totalTaxCollected) {
        this.totalTaxCollected = totalTaxCollected;
    }

}
