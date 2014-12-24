package org.egov.ptis.bean;

import java.math.BigDecimal;

/**
 * 
 * @author subhash
 *
 */

public class TaxCollectionInfo {

	private String taxType;
	private BigDecimal sewerageTax;
	private BigDecimal waterTax;
	private BigDecimal generalTax;
	private BigDecimal fireTax;
	private BigDecimal lightTax;
	private BigDecimal eduCess;
	private BigDecimal egsCess;
	private BigDecimal bigBuildingCess;
	private BigDecimal miscTax;
	private BigDecimal total;
	
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
