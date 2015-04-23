package org.egov.ptis.bean;

import java.math.BigDecimal;

/**
 * 
 * @author subhash
 *
 */

public class TaxCollectionInfo {

	private String taxType;
	private BigDecimal sewerageTax = BigDecimal.ZERO;
	private BigDecimal waterTax = BigDecimal.ZERO;
	private BigDecimal generalTax = BigDecimal.ZERO;
	private BigDecimal fireTax = BigDecimal.ZERO;
	private BigDecimal lightTax = BigDecimal.ZERO;
	private BigDecimal sewerageBenefitTax = BigDecimal.ZERO;
	private BigDecimal waterBenefitTax = BigDecimal.ZERO;
	private BigDecimal streetTax = BigDecimal.ZERO;
	private BigDecimal municipalEduCess = BigDecimal.ZERO;
	private BigDecimal eduCess = BigDecimal.ZERO;
	private BigDecimal egsCess = BigDecimal.ZERO;
	private BigDecimal bigBuildingCess = BigDecimal.ZERO;
	private BigDecimal miscTax = BigDecimal.ZERO;
	private BigDecimal total = BigDecimal.ZERO;
	
	public BigDecimal getSewerageTax() {
		return sewerageTax;
	}
	public void setSewerageTax(BigDecimal sewerageTax) {
		this.sewerageTax = sewerageTax;
	}
	public BigDecimal getWaterTax() {
		return waterTax;
	}
	public void setWaterTax(BigDecimal waterTax) {
		this.waterTax = waterTax;
	}
	public BigDecimal getGeneralTax() {
		return generalTax;
	}
	public void setGeneralTax(BigDecimal generalTax) {
		this.generalTax = generalTax;
	}
	public BigDecimal getFireTax() {
		return fireTax;
	}
	public void setFireTax(BigDecimal fireTax) {
		this.fireTax = fireTax;
	}
	public BigDecimal getLightTax() {
		return lightTax;
	}
	public void setLightTax(BigDecimal lightTax) {
		this.lightTax = lightTax;
	}
	public BigDecimal getSewerageBenefitTax() {
		return sewerageBenefitTax;
	}
	public void setSewerageBenefitTax(BigDecimal sewerageBenefitTax) {
		this.sewerageBenefitTax = sewerageBenefitTax;
	}
	public BigDecimal getWaterBenefitTax() {
		return waterBenefitTax;
	}
	public void setWaterBenefitTax(BigDecimal waterBenefitTax) {
		this.waterBenefitTax = waterBenefitTax;
	}
	public BigDecimal getStreetTax() {
		return streetTax;
	}
	public void setStreetTax(BigDecimal streetTax) {
		this.streetTax = streetTax;
	}
	public BigDecimal getMunicipalEduCess() {
		return municipalEduCess;
	}
	public void setMunicipalEduCess(BigDecimal municipalEduCess) {
		this.municipalEduCess = municipalEduCess;
	}
	public BigDecimal getEduCess() {
		return eduCess;
	}
	public void setEduCess(BigDecimal eduCess) {
		this.eduCess = eduCess;
	}
	public BigDecimal getEgsCess() {
		return egsCess;
	}
	public void setEgsCess(BigDecimal egsCess) {
		this.egsCess = egsCess;
	}
	public BigDecimal getBigBuildingCess() {
		return bigBuildingCess;
	}
	public void setBigBuildingCess(BigDecimal bigBuildingCess) {
		this.bigBuildingCess = bigBuildingCess;
	}
	public BigDecimal getMiscTax() {
		return miscTax;
	}
	public void setMiscTax(BigDecimal miscTax) {
		this.miscTax = miscTax;
	}
	public String getTaxType() {
		return taxType;
	}
	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
}
