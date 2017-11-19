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
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@XStreamAlias("unitinfo")
public abstract class UnitTaxCalculationInfo {
    @XStreamAsAttribute
    private Date baseRateEffectiveDate;

    @XStreamAsAttribute
    private String floorNumber;

    @XStreamAsAttribute
    private BigDecimal floorArea;

    private BigDecimal unitAreaInSqFt;
    private String unitOccupation;
    private String unitOccupier;
    private String unitUsage;
    private String unitStructure;
    private BigDecimal baseRate;
    private BigDecimal baseRatePerSqMtPerMonth;
    private BigDecimal mrv;
    private BigDecimal buildingValue;
    private BigDecimal siteValue;
    private BigDecimal grossARV;
    private BigDecimal depreciation;
    private BigDecimal netARV;

    private Date occpancyDate;
    private Date effectiveAssessmentDate;

    @XStreamOmitField
    private Date propertyCreatedDate;

    @XStreamAlias("misctaxes")
    private List<MiscellaneousTax> miscellaneousTaxes = new ArrayList<MiscellaneousTax>();

    private BigDecimal totalTaxPayable;

    public UnitTaxCalculationInfo() {
    }

    public UnitTaxCalculationInfo(UnitTaxCalculationInfo unit) {
        this.floorNumber = unit.getFloorNumber();
        this.unitOccupation = unit.getUnitOccupation();
        this.unitUsage = unit.getUnitUsage();
        this.occpancyDate = new Date(unit.getOccpancyDate().getTime());
        this.effectiveAssessmentDate = unit.getEffectiveAssessmentDate();
        this.propertyCreatedDate = unit.getPropertyCreatedDate();
    }

    public Date getBaseRateEffectiveDate() {
        return baseRateEffectiveDate;
    }

    public void setBaseRateEffectiveDate(Date baseRateEffectiveDate) {
        this.baseRateEffectiveDate = baseRateEffectiveDate;
    }

    public String getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(String floorNumber) {
        this.floorNumber = floorNumber;
    }

    public BigDecimal getFloorArea() {
        return floorArea;
    }

    public void setFloorArea(BigDecimal floorArea) {
        this.floorArea = floorArea;
    }

    public BigDecimal getUnitAreaInSqFt() {
        return unitAreaInSqFt;
    }

    public void setUnitAreaInSqFt(BigDecimal unitAreaInSqFt) {
        this.unitAreaInSqFt = unitAreaInSqFt;
    }

    public String getUnitOccupation() {
        return unitOccupation;
    }

    public void setUnitOccupation(String unitOccupation) {
        this.unitOccupation = unitOccupation;
    }

    public String getUnitOccupier() {
        return unitOccupier;
    }

    public void setUnitOccupier(String unitOccupier) {
        this.unitOccupier = unitOccupier;
    }

    public String getUnitUsage() {
        return unitUsage;
    }

    public void setUnitUsage(String unitUsage) {
        this.unitUsage = unitUsage;
    }

    public String getUnitStructure() {
        return unitStructure;
    }

    public void setUnitStructure(String unitStructure) {
        this.unitStructure = unitStructure;
    }

    public BigDecimal getBaseRate() {
        return baseRate;
    }

    public void setBaseRate(BigDecimal baseRate) {
        this.baseRate = baseRate;
    }

    public BigDecimal getBaseRatePerSqMtPerMonth() {
        return baseRatePerSqMtPerMonth;
    }

    public void setBaseRatePerSqMtPerMonth(BigDecimal baseRatePerSqMtPerMonth) {
        this.baseRatePerSqMtPerMonth = baseRatePerSqMtPerMonth;
    }

    public BigDecimal getMrv() {
        return mrv;
    }

    public void setMrv(BigDecimal mrv) {
        this.mrv = mrv;
    }

    public BigDecimal getBuildingValue() {
        return buildingValue;
    }

    public void setBuildingValue(BigDecimal buildingValue) {
        this.buildingValue = buildingValue;
    }

    public BigDecimal getSiteValue() {
        return siteValue;
    }

    public void setSiteValue(BigDecimal siteValue) {
        this.siteValue = siteValue;
    }

    public BigDecimal getGrossARV() {
        return grossARV;
    }

    public void setGrossARV(BigDecimal grossARV) {
        this.grossARV = grossARV;
    }

    public BigDecimal getDepreciation() {
        return depreciation;
    }

    public void setDepreciation(BigDecimal depreciation) {
        this.depreciation = depreciation;
    }

    public BigDecimal getNetARV() {
        return netARV;
    }

    public void setNetARV(BigDecimal netARV) {
        this.netARV = netARV;
    }

    public Date getOccpancyDate() {
        return occpancyDate;
    }

    public void setOccpancyDate(Date occpancyDate) {
        this.occpancyDate = occpancyDate;
    }

    public Date getEffectiveAssessmentDate() {
        return effectiveAssessmentDate;
    }

    public void setEffectiveAssessmentDate(Date effectiveAssessmentDate) {
        this.effectiveAssessmentDate = effectiveAssessmentDate;
    }

    public Date getPropertyCreatedDate() {
        return propertyCreatedDate;
    }

    public void setPropertyCreatedDate(Date propertyCreatedDate) {
        this.propertyCreatedDate = propertyCreatedDate;
    }

    public List<MiscellaneousTax> getMiscellaneousTaxes() {
        return miscellaneousTaxes;
    }

    public void setMiscellaneousTaxes(List<MiscellaneousTax> miscellaneousTaxes) {
        this.miscellaneousTaxes = miscellaneousTaxes;
    }

    public void addMiscellaneousTaxes(MiscellaneousTax miscellaneousTaxes) {
        getMiscellaneousTaxes().add(miscellaneousTaxes);
    }

    public BigDecimal getTotalTaxPayable() {
        return totalTaxPayable;
    }

    public void setTotalTaxPayable(BigDecimal totalTaxPayable) {
        this.totalTaxPayable = totalTaxPayable;
    }

    @Override
    public int hashCode() {

        int hashCode = this.effectiveAssessmentDate.hashCode() + this.floorNumber.hashCode()
                + this.occpancyDate.hashCode() + this.unitOccupation.hashCode() + this.unitUsage.hashCode();
        return hashCode;
    }
}
