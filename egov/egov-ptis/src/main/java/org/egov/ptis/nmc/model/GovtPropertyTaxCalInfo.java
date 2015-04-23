package org.egov.ptis.nmc.model;

import java.math.BigDecimal;

/**
 * 
 * @author subhash
 *
 */

public class GovtPropertyTaxCalInfo {

	private String amenities;
	private String propertyArea;
	private BigDecimal buildingCost;
	private BigDecimal annualLettingValue;
	private Double alvPercentage;
	private String effectiveDate;
	
	public String getAmenities() {
		return amenities;
	}
	public void setAmenities(String amenities) {
		this.amenities = amenities;
	}
	public String getPropertyArea() {
		return propertyArea;
	}
	public void setPropertyArea(String propertyArea) {
		this.propertyArea = propertyArea;
	}
	public BigDecimal getBuildingCost() {
		return buildingCost;
	}
	public void setBuildingCost(BigDecimal buildingCost) {
		this.buildingCost = buildingCost;
	}
	public BigDecimal getAnnualLettingValue() {
		return annualLettingValue;
	}
	public void setAnnualLettingValue(BigDecimal annualLettingValue) {
		this.annualLettingValue = annualLettingValue;
	}
	public Double getAlvPercentage() {
		return alvPercentage;
	}
	public void setAlvPercentage(Double alvPercentage) {
		this.alvPercentage = alvPercentage;
	}
	public String getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
}
