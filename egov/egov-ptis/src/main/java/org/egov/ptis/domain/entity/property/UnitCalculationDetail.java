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

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static java.math.BigDecimal.ZERO;

public class UnitCalculationDetail implements Serializable {

	@NotNull
	private Long id;
	@NotNull
	private Integer unitNumber;
	@NotNull
	private BigDecimal unitArea;
	@NotNull
	private Date occupancyDate;
	@NotNull
	private BigDecimal guidanceValue;
	@NotNull
	private Date guidValEffectiveDate;
	@NotNull
	private Date installmentFromDate;
	@NotNull
	private String unitOccupation;
	@NotNull
	private BigDecimal monthlyRent = ZERO;
	@NotNull
	private BigDecimal monthlyRentTenant = ZERO;

	@NotNull
	private BigDecimal alv = ZERO;
	@NotNull
	private BigDecimal residentialALV = ZERO;
	@NotNull
	private BigDecimal nonResidentialALV = ZERO;
	@NotNull
	private BigDecimal waterTaxALV = ZERO;
	@NotNull
	private BigDecimal bigBuildingTaxALV = ZERO;

	@NotNull
	private BigDecimal taxPayable = ZERO;

	@NotNull
	@Max(value=366)
	private Integer taxDays;

	private Date fromDate;
	@NotNull
	private BigDecimal generalTax = ZERO;
	private Date generalTaxFromDate;
	@NotNull
	private BigDecimal sewerageTax = ZERO;
	private Date sewerageTaxFromDate;
	@NotNull
	private BigDecimal fireTax = ZERO;
	private Date fireTaxFromDate;
	@NotNull
	private BigDecimal lightTax = ZERO;
	private Date lightTaxFromDate;

	@NotNull
    private BigDecimal waterTax = ZERO;
    private Date waterTaxFromDate;

    @NotNull
    private BigDecimal eduCessResd = ZERO;
    private Date eduCessResdFromDate;

    @NotNull
    private BigDecimal eduCessNonResd = ZERO;
    private Date eduCessNonResdFromDate;

    @NotNull
    private BigDecimal empGrntCess = ZERO;
    private Date empGrntCessFromDate;

    @NotNull
    private BigDecimal bigBuildingTax = ZERO;
	private Date bigBuildingTaxFromDate;

    @NotNull
    private Date createdTimeStamp;
    @NotNull
    private Date lastUpdatedTimeStamp;
    @NotNull
    private PropertyImpl property;

    @NotNull
    private BigDecimal serviceCharge = BigDecimal.ZERO;

    @NotNull
    private BigDecimal buildingCost = BigDecimal.ZERO;

    /**
	 * @Size(min=1) is not working when we modify a migrated property, Reason is because
	 * for the migrated property the tax xml is not there so when we try to modify the
	 * migrated property the active property will not be having the unitCalculationDetails
	 *
	 */

    @Valid
    private Set<UnitAreaCalculationDetail> unitAreaCalculationDetails = new HashSet<>();

    public UnitCalculationDetail() {}

    public UnitCalculationDetail(UnitCalculationDetail other) {
    	this.unitNumber = other.unitNumber;
    	this.unitArea = other.unitArea;
    	this.occupancyDate = other.occupancyDate;
    	this.guidanceValue = other.guidanceValue;
    	this.guidValEffectiveDate = other.guidValEffectiveDate;
    	this.installmentFromDate = other.installmentFromDate;
    	this.unitOccupation = other.unitOccupation;
    	this.monthlyRent = other.monthlyRent;
    	this.alv = other.alv;
    	this.residentialALV = other.residentialALV;
    	this.nonResidentialALV = other.nonResidentialALV;
    	this.waterTaxALV = other.waterTaxALV;
    	this.bigBuildingTaxALV = other.bigBuildingTaxALV;
    	this.taxPayable = other.taxPayable;
    	this.taxDays = other.taxDays;
    	this.fromDate = other.getFromDate();
    	this.generalTax = other.generalTax;
    	this.generalTaxFromDate = other.generalTaxFromDate;
    	this.sewerageTax = other.sewerageTax;
    	this.sewerageTaxFromDate = other.sewerageTaxFromDate;
    	this.fireTax = other.fireTax;
    	this.fireTaxFromDate = other.fireTaxFromDate;
    	this.lightTax = other.lightTax;
    	this.lightTaxFromDate = other.lightTaxFromDate;
        this.waterTax = other.waterTax;
        this.waterTaxFromDate = other.waterTaxFromDate;
        this.eduCessResd = other.eduCessResd;
        this.eduCessResdFromDate = other.eduCessResdFromDate;
        this.eduCessNonResd = other.eduCessNonResd;
        this.eduCessNonResdFromDate = other.eduCessNonResdFromDate;
        this.empGrntCess = other.empGrntCess;
        this.empGrntCessFromDate = other.empGrntCessFromDate;
        this.bigBuildingTax = other.bigBuildingTax;
        this.bigBuildingTaxFromDate = other.bigBuildingTaxFromDate;
        this.createdTimeStamp = new Date();
        this.lastUpdatedTimeStamp = new Date();
        this.serviceCharge = other.serviceCharge;
        this.buildingCost = other.buildingCost;

        for (UnitAreaCalculationDetail unitAreaCalcDtl : other.unitAreaCalculationDetails) {
        	this.addUnitAreaCalculationDetail(new UnitAreaCalculationDetail(unitAreaCalcDtl));
        }
    }


	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the createdTimeStamp
	 */
	public Date getCreatedTimeStamp() {
		return createdTimeStamp;
	}

	/**
	 * @param createdTimeStamp the createdTimeStamp to set
	 */
	public void setCreatedTimeStamp(Date createdTimeStamp) {
		this.createdTimeStamp = createdTimeStamp;
	}

	/**
	 * @return the lastUpdatedTimeStamp
	 */
	public Date getLastUpdatedTimeStamp() {
		return lastUpdatedTimeStamp;
	}

	/**
	 * @param lastUpdatedTimeStamp the lastUpdatedTimeStamp to set
	 */
	public void setLastUpdatedTimeStamp(Date lastUpdatedTimeStamp) {
		this.lastUpdatedTimeStamp = lastUpdatedTimeStamp;
	}



	public Integer getUnitNumber() {
		return unitNumber;
	}

	public void setUnitNumber(Integer unitNumber) {
		this.unitNumber = unitNumber;
	}

	public BigDecimal getUnitArea() {
		return unitArea;
	}

	public void setUnitArea(BigDecimal unitArea) {
		this.unitArea = unitArea;
	}

	public Date getOccupancyDate() {
		return occupancyDate;
	}

	public void setOccupancyDate(Date occupancyDate) {
		this.occupancyDate = occupancyDate;
	}

	public BigDecimal getGuidanceValue() {
		return guidanceValue;
	}

	public void setGuidanceValue(BigDecimal guidanceValue) {
		this.guidanceValue = guidanceValue;
	}

	public Date getGuidValEffectiveDate() {
		return guidValEffectiveDate;
	}

	public void setGuidValEffectiveDate(Date guidValEffectiveDate) {
		this.guidValEffectiveDate = guidValEffectiveDate;
	}

	public Date getInstallmentFromDate() {
		return installmentFromDate;
	}

	public void setInstallmentFromDate(Date installmentFromDate) {
		this.installmentFromDate = installmentFromDate;
	}

	public String getUnitOccupation() {
		return unitOccupation;
	}

	public void setUnitOccupation(String unitOccupation) {
		this.unitOccupation = unitOccupation;
	}

	public BigDecimal getMonthlyRent() {
		return monthlyRent;
	}

	public void setMonthlyRent(BigDecimal monthlyRent) {
		this.monthlyRent = monthlyRent;
	}

	public BigDecimal getMonthlyRentTenant() {
		return monthlyRentTenant;
	}

	public void setMonthlyRentTenant(BigDecimal monthlyRentTenant) {
		this.monthlyRentTenant = monthlyRentTenant;
	}

	public BigDecimal getAlv() {
		return alv;
	}

	public void setAlv(BigDecimal alv) {
		this.alv = alv;
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

	public BigDecimal getWaterTaxALV() {
		return waterTaxALV;
	}

	public void setWaterTaxALV(BigDecimal waterTaxALV) {
		this.waterTaxALV = waterTaxALV;
	}

	public BigDecimal getBigBuildingTaxALV() {
		return bigBuildingTaxALV;
	}

	public void setBigBuildingTaxALV(BigDecimal bigBuildingTaxALV) {
		this.bigBuildingTaxALV = bigBuildingTaxALV;
	}

	public BigDecimal getTaxPayable() {
		return taxPayable;
	}

	public void setTaxPayable(BigDecimal taxPayable) {
		this.taxPayable = taxPayable;
	}

	public Integer getTaxDays() {
		return taxDays;
	}

	public void setTaxDays(Integer taxDays) {
		this.taxDays = taxDays;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public BigDecimal getGeneralTax() {
		return generalTax;
	}

	public void setGeneralTax(BigDecimal generalTax) {
		this.generalTax = generalTax;
	}

	public Date getGeneralTaxFromDate() {
		return generalTaxFromDate;
	}

	public void setGeneralTaxFromDate(Date generalTaxFromDate) {
		this.generalTaxFromDate = generalTaxFromDate;
	}

	public BigDecimal getSewerageTax() {
		return sewerageTax;
	}

	public void setSewerageTax(BigDecimal sewerageTax) {
		this.sewerageTax = sewerageTax;
	}

	public Date getSewerageTaxFromDate() {
		return sewerageTaxFromDate;
	}

	public void setSewerageTaxFromDate(Date sewerageTaxFromDate) {
		this.sewerageTaxFromDate = sewerageTaxFromDate;
	}

	public BigDecimal getFireTax() {
		return fireTax;
	}

	public void setFireTax(BigDecimal fireTax) {
		this.fireTax = fireTax;
	}

	public Date getFireTaxFromDate() {
		return fireTaxFromDate;
	}

	public void setFireTaxFromDate(Date fireTaxFromDate) {
		this.fireTaxFromDate = fireTaxFromDate;
	}

	public BigDecimal getLightTax() {
		return lightTax;
	}

	public void setLightTax(BigDecimal lightTax) {
		this.lightTax = lightTax;
	}

	public Date getLightTaxFromDate() {
		return lightTaxFromDate;
	}

	public void setLightTaxFromDate(Date lightTaxFromDate) {
		this.lightTaxFromDate = lightTaxFromDate;
	}

	public BigDecimal getWaterTax() {
		return waterTax;
	}

	public void setWaterTax(BigDecimal waterTax) {
		this.waterTax = waterTax;
	}

	public Date getWaterTaxFromDate() {
		return waterTaxFromDate;
	}

	public void setWaterTaxFromDate(Date waterTaxFromDate) {
		this.waterTaxFromDate = waterTaxFromDate;
	}

	public BigDecimal getEduCessResd() {
		return eduCessResd;
	}

	public void setEduCessResd(BigDecimal eduCessResd) {
		this.eduCessResd = eduCessResd;
	}

	public Date getEduCessResdFromDate() {
		return eduCessResdFromDate;
	}

	public void setEduCessResdFromDate(Date eduCessResdFromDate) {
		this.eduCessResdFromDate = eduCessResdFromDate;
	}

	public BigDecimal getEduCessNonResd() {
		return eduCessNonResd;
	}

	public void setEduCessNonResd(BigDecimal eduCessNonResd) {
		this.eduCessNonResd = eduCessNonResd;
	}

	public Date getEduCessNonResdFromDate() {
		return eduCessNonResdFromDate;
	}

	public void setEduCessNonResdFromDate(Date eduCessNonResdFromDate) {
		this.eduCessNonResdFromDate = eduCessNonResdFromDate;
	}

	public BigDecimal getEmpGrntCess() {
		return empGrntCess;
	}

	public void setEmpGrntCess(BigDecimal empGrntCess) {
		this.empGrntCess = empGrntCess;
	}

	public Date getEmpGrntCessFromDate() {
		return empGrntCessFromDate;
	}

	public void setEmpGrntCessFromDate(Date empGrntCessFromDate) {
		this.empGrntCessFromDate = empGrntCessFromDate;
	}

	public BigDecimal getBigBuildingTax() {
		return bigBuildingTax;
	}

	public void setBigBuildingTax(BigDecimal bigBuildingTax) {
		this.bigBuildingTax = bigBuildingTax;
	}

	public Date getBigBuildingTaxFromDate() {
		return bigBuildingTaxFromDate;
	}

	public void setBigBuildingTaxFromDate(Date bigBuildingTaxFromDate) {
		this.bigBuildingTaxFromDate = bigBuildingTaxFromDate;
	}

	public Property getProperty() {
		return property;
	}

	public void setProperty(PropertyImpl property) {
		this.property = property;
	}

	public Set<UnitAreaCalculationDetail> getUnitAreaCalculationDetails() {
		return unitAreaCalculationDetails;
	}

	public void setUnitAreaCalculationDetails(Set<UnitAreaCalculationDetail> unitAreaCalculationDetails) {
		this.unitAreaCalculationDetails = unitAreaCalculationDetails;
	}

	public BigDecimal getServiceCharge() {
		return serviceCharge;
	}

	public void setServiceCharge(BigDecimal serviceCharge) {
		this.serviceCharge = serviceCharge;
	}

	public BigDecimal getBuildingCost() {
		return buildingCost;
	}

	public void setBuildingCost(BigDecimal buildingCost) {
		this.buildingCost = buildingCost;
	}

	public void addUnitAreaCalculationDetail(UnitAreaCalculationDetail unitAreaCalcDetail) {
		unitAreaCalcDetail.setUnitCalculationDetail(this);
		getUnitAreaCalculationDetails().add(unitAreaCalcDetail);
	}

	public void addUnitAreaCalculationDetails(Set<UnitAreaCalculationDetail> unitAreaCalcDetails) {

		for (UnitAreaCalculationDetail unitAreaCalcDetail : unitAreaCalcDetails) {
			this.addUnitAreaCalculationDetail(unitAreaCalcDetail);
		}

	}

	@Override
	public String toString() {

		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("UnitCalculationDetail [idProperty = ")
				.append(this.property == null ? "null" : this.property.getId())
				.append(", unitNumber=").append(unitNumber)
				.append(", unitArea=").append(unitArea)
				.append(", occupancyDate=").append(occupancyDate)
				.append(", guidanceValue=").append(guidanceValue)
				.append(", guidValEffectiveDate=").append(guidValEffectiveDate)
				.append(", installmentFromDate=").append(installmentFromDate)
				.append(", unitOccupation=").append(unitOccupation)
				.append(", monthlyRent=").append(monthlyRent)
				.append(", alv=").append(alv)
				.append(", residentialALV=").append(residentialALV)
				.append(", nonResidentialALV=").append(nonResidentialALV)
				.append(", waterTaxALV=").append( waterTaxALV)
				.append(", bigBuildingTaxALV=").append(bigBuildingTaxALV)
				.append(", taxPayable=").append(taxPayable)
				.append(", fromDate=").append(fromDate)
				.append(", generalTax=").append(generalTax)
				.append(", generalTaxFromDate=").append(generalTaxFromDate)
				.append(", sewerageTax=").append(sewerageTax)
				.append(", sewerageTaxFromDate=").append(sewerageTaxFromDate)
				.append(", fireTax=").append(fireTax)
				.append(", fireTaxFromDate=").append(fireTaxFromDate)
				.append(", lightTax=").append(lightTax)
				.append(", lightTaxFromDate=").append(lightTaxFromDate)
				.append(", waterTax=").append(waterTax)
				.append(", waterTaxFromDate=").append(waterTaxFromDate)
				.append(", eduCessResd=").append(eduCessResd)
				.append(", eduCessResdFromDate=").append(eduCessResdFromDate)
				.append(", eduCessNonResd=").append(eduCessNonResd)
				.append(", eduCessNonResdFromDate=").append(eduCessNonResdFromDate)
				.append(", empGrntCess=").append(empGrntCess)
				.append(", empGrntCessFromDate=").append(empGrntCessFromDate)
				.append(", serviceCharge=").append(serviceCharge)
				.append(", buildingCost=").append(buildingCost)
				.append(unitAreaCalculationDetails)
				.append("]");

		return stringBuilder.toString();
	}

}

