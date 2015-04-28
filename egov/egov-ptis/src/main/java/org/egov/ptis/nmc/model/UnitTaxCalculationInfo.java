/*******************************************************************************
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
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org
 ******************************************************************************/
package org.egov.ptis.nmc.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("unitinfo")
public class UnitTaxCalculationInfo {
	@XStreamAsAttribute
    private Date baseRentEffectiveDate;

    @XStreamAsAttribute
    private String floorNumber;

    @XStreamAsAttribute
    private Integer unitNumber;

    @XStreamAsAttribute
    private BigDecimal unitArea;

    private BigDecimal unitAreaInSqFt;
    private String unitOccupation;
    private String unitUsage;
    private BigDecimal baseRent;
    private BigDecimal baseRentPerSqMtPerMonth;
    private BigDecimal monthlyRent;
    private BigDecimal annualRentBeforeDeduction;
    private BigDecimal annualRentAfterDeduction;
    private BigDecimal totalTaxPayable;
    private BigDecimal monthlyRentPaidByTenant;

    private Date occpancyDate;
    private Date effectiveAssessmentDate;

    private String structuralFactorCode;
    private String structuralFactorIndex;
    private BigDecimal structuralFactorValue;
    private String locationFactorCode;
    private String locationFactorIndex;
    private BigDecimal locationFactorValue;
    private String usageFactorCode;
    private String usageFactorIndex;
    private BigDecimal usageFactorValue;
    private String occupancyFactorCode;
    private String occupancyFactorIndex;
    private BigDecimal occupancyFactorValue;
    private String ageFactorCode;
    private String ageFactorIndex;
    private BigDecimal ageFactorvalue;
    private Integer floorNumberInteger;
    private String unitOccupier;
    private String instDate;
    private String manualAlv;
    private BigDecimal residentialALV = BigDecimal.ZERO;
    private BigDecimal nonResidentialALV  = BigDecimal.ZERO;
    private BigDecimal resdEduCess  = BigDecimal.ZERO;
    private BigDecimal nonResdEduCess  = BigDecimal.ZERO;
    private BigDecimal egCess  = BigDecimal.ZERO;
    private Boolean hasALVChanged = Boolean.FALSE;
    private BigDecimal bigBuildingTaxALV = BigDecimal.ZERO;

    private String taxExemptionReason;

    @XStreamOmitField
    private Boolean isHistory;
    @XStreamOmitField
    private Date propertyCreatedDate;

	@XStreamAlias("misctaxes")
    private List<MiscellaneousTax> miscellaneousTaxes = new ArrayList<MiscellaneousTax>();

    @XStreamAlias("areataxes")
    private List<AreaTaxCalculationInfo> areaTaxCalculationInfos = new ArrayList<AreaTaxCalculationInfo>();

    public UnitTaxCalculationInfo() {}

    public UnitTaxCalculationInfo(UnitTaxCalculationInfo unit) {
    	this.floorNumber = unit.getFloorNumber();
		this.unitNumber = unit.getUnitNumber();
		this.unitArea = unit.getUnitArea();
		this.unitOccupation = unit.getUnitOccupation();
		this.unitUsage = unit.getUnitUsage();
		this.baseRent = unit.getBaseRent();
		this.baseRentPerSqMtPerMonth = unit.getBaseRentPerSqMtPerMonth();
		this.monthlyRent = unit.getMonthlyRent();
		this.annualRentBeforeDeduction = unit.getAnnualRentBeforeDeduction();
		this.annualRentAfterDeduction = unit.getAnnualRentAfterDeduction();
		this.totalTaxPayable = unit.getTotalTaxPayable();
		this.monthlyRentPaidByTenant = unit.getMonthlyRentPaidByTenant();
		this.occpancyDate = new Date(unit.getOccpancyDate().getTime());
		this.effectiveAssessmentDate = unit.getEffectiveAssessmentDate();
		this.floorNumberInteger = unit.getFloorNumberInteger();
		this.unitOccupier = unit.getUnitOccupier();
		this.instDate = unit.getInstDate();
		this.residentialALV = unit.getResidentialALV();
		this.nonResidentialALV = unit.getNonResidentialALV();
		this.resdEduCess = unit.getResdEduCess();
		this.nonResdEduCess = unit.getNonResdEduCess();
		this.egCess = unit.getEgCess();
		this.baseRentEffectiveDate = unit.getBaseRentEffectiveDate();
		this.bigBuildingTaxALV = unit.getBigBuildingTaxALV();

		if (unit.getHasALVChanged() == null) {
			this.hasALVChanged = Boolean.FALSE;
		} else {
			this.hasALVChanged = unit.getHasALVChanged();
		}

		this.isHistory = unit.getIsHistory();
		this.propertyCreatedDate = unit.getPropertyCreatedDate();
		this.taxExemptionReason = unit.getTaxExemptionReason();

    }

    public String getUnitOccupation() {
        return unitOccupation;
    }

    public BigDecimal getUnitArea() {
        return unitArea;
    }

    public Date getOccpancyDate() {
        return occpancyDate;
    }

    public Date getEffectiveAssessmentDate() {
        return effectiveAssessmentDate;
    }

    public BigDecimal getMonthlyRent() {
        return monthlyRent;
    }

    public Integer getUnitNumber() {
        return unitNumber;
    }

    public List<MiscellaneousTax> getMiscellaneousTaxes() {
        return miscellaneousTaxes;
    }

    public BigDecimal getBaseRentPerSqMtPerMonth() {
        return baseRentPerSqMtPerMonth;
    }

    public BigDecimal getAnnualRentBeforeDeduction() {
        return annualRentBeforeDeduction;
    }

    public BigDecimal getAnnualRentAfterDeduction() {
        return annualRentAfterDeduction;
    }

    public BigDecimal getTotalTaxPayable() {
        return totalTaxPayable;
    }

    public void setUnitNumber(Integer unitNumber) {
        this.unitNumber = unitNumber;
    }

    public void setUnitOccupation(String unitOccupation) {
        this.unitOccupation = unitOccupation;
    }

    public void setUnitArea(BigDecimal unitArea) {
        this.unitArea = unitArea;
    }

    public void setBaseRentPerSqMtPerMonth(BigDecimal baseRentPerSqMtPerMonth) {
        this.baseRentPerSqMtPerMonth = baseRentPerSqMtPerMonth;
    }

    public void setMonthlyRent(BigDecimal monthlyRent) {
        this.monthlyRent = monthlyRent;
    }

    public void setAnnualRentBeforeDeduction(BigDecimal annualRentBeforeDeduction) {
        this.annualRentBeforeDeduction = annualRentBeforeDeduction;
    }

    public void setAnnualRentAfterDeduction(BigDecimal annualRentAfterDeduction) {
        this.annualRentAfterDeduction = annualRentAfterDeduction;
    }

    public void setTotalTaxPayable(BigDecimal totalTaxPayable) {
        this.totalTaxPayable = totalTaxPayable;
    }

    public void setOccpancyDate(Date occpancyDate) {
        this.occpancyDate = occpancyDate;
    }

    public void setEffectiveAssessmentDate(Date effectiveAssessmentDate) {
        this.effectiveAssessmentDate = effectiveAssessmentDate;
    }

    public void setMiscellaneousTaxes(List<MiscellaneousTax> miscellaneousTaxes) {
        this.miscellaneousTaxes = miscellaneousTaxes;
    }

    public void addMiscellaneousTaxes(MiscellaneousTax miscellaneousTaxes) {
        this.getMiscellaneousTaxes().add(miscellaneousTaxes);
    }

    public void addAreaTaxCalculationInfo(AreaTaxCalculationInfo areaTaxCalculationInfo) {
        this.getAreaTaxCalculationInfos().add(areaTaxCalculationInfo);
    }

    public String getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(String floorNumber) {
        this.floorNumber = floorNumber;
    }

    public BigDecimal getBaseRent() {
        return baseRent;
    }

    public void setBaseRent(BigDecimal baseRent) {
        this.baseRent = baseRent;
    }

    public String getUnitUsage() {
        return unitUsage;
    }

    public void setUnitUsage(String unitUsage) {
        this.unitUsage = unitUsage;
    }

    public String getStructuralFactorCode() {
        return structuralFactorCode;
    }

    public void setStructuralFactorCode(String structuralFactorCode) {
        this.structuralFactorCode = structuralFactorCode;
    }

    public BigDecimal getStructuralFactorValue() {
        return structuralFactorValue;
    }

    public void setStructuralFactorValue(BigDecimal structuralFactorValue) {
        this.structuralFactorValue = structuralFactorValue;
    }

    public String getLocationFactorCode() {
        return locationFactorCode;
    }

    public void setLocationFactorCode(String locationFactorCode) {
        this.locationFactorCode = locationFactorCode;
    }

    public BigDecimal getLocationFactorValue() {
        return locationFactorValue;
    }

    public void setLocationFactorValue(BigDecimal locationFactorValue) {
        this.locationFactorValue = locationFactorValue;
    }

    public String getUsageFactorCode() {
        return usageFactorCode;
    }

    public void setUsageFactorCode(String usageFactorCode) {
        this.usageFactorCode = usageFactorCode;
    }

    public BigDecimal getUsageFactorValue() {
        return usageFactorValue;
    }

    public void setUsageFactorValue(BigDecimal usageFactorValue) {
        this.usageFactorValue = usageFactorValue;
    }

    public String getOccupancyFactorCode() {
        return occupancyFactorCode;
    }

    public void setOccupancyFactorCode(String occupancyFactorCode) {
        this.occupancyFactorCode = occupancyFactorCode;
    }

    public BigDecimal getOccupancyFactorValue() {
        return occupancyFactorValue;
    }

    public void setOccupancyFactorValue(BigDecimal occupancyFactorValue) {
        this.occupancyFactorValue = occupancyFactorValue;
    }

    public String getAgeFactorCode() {
        return ageFactorCode;
    }

    public void setAgeFactorCode(String ageFactorCode) {
        this.ageFactorCode = ageFactorCode;
    }

    public BigDecimal getAgeFactorvalue() {
        return ageFactorvalue;
    }

    public void setAgeFactorvalue(BigDecimal ageFactorvalue) {
        this.ageFactorvalue = ageFactorvalue;
    }

    public String getStructuralFactorIndex() {
        return structuralFactorIndex;
    }

    public void setStructuralFactorIndex(String structuralFactorIndex) {
        this.structuralFactorIndex = structuralFactorIndex;
    }

    public String getLocationFactorIndex() {
        return locationFactorIndex;
    }

    public void setLocationFactorIndex(String locationFactorIndex) {
        this.locationFactorIndex = locationFactorIndex;
    }

    public String getUsageFactorIndex() {
        return usageFactorIndex;
    }

    public void setUsageFactorIndex(String usageFactorIndex) {
        this.usageFactorIndex = usageFactorIndex;
    }

    public String getOccupancyFactorIndex() {
        return occupancyFactorIndex;
    }

    public void setOccupancyFactorIndex(String occupancyFactorIndex) {
        this.occupancyFactorIndex = occupancyFactorIndex;
    }

    public String getAgeFactorIndex() {
        return ageFactorIndex;
    }

    public void setAgeFactorIndex(String ageFactorIndex) {
        this.ageFactorIndex = ageFactorIndex;
    }

    public Integer getFloorNumberInteger() {
        return floorNumberInteger;
    }

    public void setFloorNumberInteger(Integer floorNumberInteger) {
        this.floorNumberInteger = floorNumberInteger;
    }

    public List<AreaTaxCalculationInfo> getAreaTaxCalculationInfos() {
        return areaTaxCalculationInfos;
    }

    public void setAreaTaxCalculationInfos(List<AreaTaxCalculationInfo> areaTaxCalculationInfos) {
        this.areaTaxCalculationInfos = areaTaxCalculationInfos;
    }

    public BigDecimal getMonthlyRentPaidByTenant() {
        return monthlyRentPaidByTenant;
    }

    public void setMonthlyRentPaidByTenant(BigDecimal monthlyRentPaidByTenant) {
        this.monthlyRentPaidByTenant = monthlyRentPaidByTenant;
    }

    public String getUnitOccupier() {
		return unitOccupier;
	}

	public void setUnitOccupier(String unitOccupier) {
		this.unitOccupier = unitOccupier;
	}
	public String getInstDate() {
		return instDate;
	}

	public void setInstDate(String instDate) {
		this.instDate = instDate;
	}

	public String getManualAlv() {
		return manualAlv;
	}

	public void setManualAlv(String manualAlv) {
		this.manualAlv = manualAlv;
	}

	public BigDecimal getResidentialALV() {
		return residentialALV;
	}

	public void setResidentialALV(BigDecimal residentialALV) {
		this.residentialALV = residentialALV;
	}

	public BigDecimal getNonResidentialALV() {
		return nonResidentialALV;
	}

	public void setNonResidentialALV(BigDecimal nonResidentialALV) {
		this.nonResidentialALV = nonResidentialALV;
	}

	public BigDecimal getResdEduCess() {
		return resdEduCess;
	}

	public void setResdEduCess(BigDecimal resdEduCess) {
		this.resdEduCess = resdEduCess;
	}

	public BigDecimal getNonResdEduCess() {
		return nonResdEduCess;
	}

	public void setNonResdEduCess(BigDecimal nonResdEduCess) {
		this.nonResdEduCess = nonResdEduCess;
	}

	public BigDecimal getEgCess() {
		return egCess;
	}

	public void setEgCess(BigDecimal egCess) {
		this.egCess = egCess;
	}

	public BigDecimal getUnitAreaInSqFt() {
		return unitAreaInSqFt;
	}

	public void setUnitAreaInSqFt(BigDecimal unitAreaInSqFt) {
		this.unitAreaInSqFt = unitAreaInSqFt;
	}

	public Boolean getHasALVChanged() {
		return hasALVChanged;
	}

	public void setHasALVChanged(Boolean hasALVChanged) {
		this.hasALVChanged = hasALVChanged;
	}

	public Date getBaseRentEffectiveDate() {
		return baseRentEffectiveDate;
	}

	public void setBaseRentEffectiveDate(Date baseRentEffectiveDate) {
		this.baseRentEffectiveDate = baseRentEffectiveDate;
	}

	public Boolean getIsHistory() {
		return isHistory;
	}

	public void setIsHistory(Boolean isHistory) {
		this.isHistory = isHistory;
	}


	public Date getPropertyCreatedDate() {
		return propertyCreatedDate;
	}

	public void setPropertyCreatedDate(Date propertyCreatedDate) {
		this.propertyCreatedDate = propertyCreatedDate;
	}

	public BigDecimal getBigBuildingTaxALV() {
		return bigBuildingTaxALV;
	}

	public void setBigBuildingTaxALV(BigDecimal bigBuildingTaxALV) {
		this.bigBuildingTaxALV = bigBuildingTaxALV;
	}

	@Override
    public int hashCode() {

		int hashCode = this.ageFactorCode.hashCode() + this.ageFactorvalue.hashCode()
				+ this.annualRentAfterDeduction.hashCode() + this.annualRentBeforeDeduction.hashCode()
				+ this.baseRent.hashCode() + this.baseRentPerSqMtPerMonth.hashCode()
				+ this.effectiveAssessmentDate.hashCode() + this.floorNumber.hashCode()
				+ this.locationFactorCode.hashCode() + this.locationFactorValue.hashCode()
				+ this.monthlyRent.hashCode() + this.occpancyDate.hashCode() + this.occupancyFactorCode.hashCode()
				+ this.occupancyFactorValue.hashCode() + this.structuralFactorCode.hashCode()
				+ this.structuralFactorValue.hashCode() + this.totalTaxPayable.hashCode() + this.unitArea.hashCode()
				+ this.unitNumber.hashCode() + this.unitOccupation.hashCode() + this.unitUsage.hashCode()
				+ this.usageFactorCode.hashCode() + this.usageFactorValue.hashCode()
				+ this.structuralFactorIndex.hashCode() + this.locationFactorIndex.hashCode()
				+ this.ageFactorIndex.hashCode() + this.occupancyFactorIndex.hashCode()
				+ this.usageFactorIndex.hashCode() + this.floorNumberInteger + this.monthlyRentPaidByTenant.hashCode();
		return hashCode;
    }

    public void addApplicableFactor(List<ApplicableFactor> applicableFactors) {
        for (ApplicableFactor applicableFactor : applicableFactors) {
            if (applicableFactor.getFactorName().equals("SF")) {
                this.setStructuralFactorCode(applicableFactor.getFactorName());
                this.setStructuralFactorIndex(applicableFactor.getFactorIndex());
                this.setStructuralFactorValue(applicableFactor.getFactorValue());
            } else if (applicableFactor.getFactorName().equals("UF")) {
                this.setUsageFactorCode(applicableFactor.getFactorName());
                this.setUsageFactorIndex(applicableFactor.getFactorIndex());
                this.setUsageFactorValue(applicableFactor.getFactorValue());
            } else if (applicableFactor.getFactorName().equals("OF")) {
                this.setOccupancyFactorCode(applicableFactor.getFactorName());
                this.setOccupancyFactorIndex(applicableFactor.getFactorIndex());
                this.setOccupancyFactorValue(applicableFactor.getFactorValue());
            } else if (applicableFactor.getFactorName().equals("AF")) {
                this.setAgeFactorCode(applicableFactor.getFactorName());
                this.setAgeFactorIndex(applicableFactor.getFactorIndex());
                this.setAgeFactorvalue(applicableFactor.getFactorValue());
            } else if (applicableFactor.getFactorName().equals("LF")) {
                this.setLocationFactorCode(applicableFactor.getFactorName());
                this.setLocationFactorIndex(applicableFactor.getFactorIndex());
                this.setLocationFactorValue(applicableFactor.getFactorValue());
            }

        }

    }

	public String getTaxExemptionReason() {
		return taxExemptionReason;
	}

	public void setTaxExemptionReason(String taxExemptionReason) {
		this.taxExemptionReason = taxExemptionReason;
	}
}
