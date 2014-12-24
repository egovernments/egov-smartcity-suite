package org.egov.ptis.bean;

import java.math.BigDecimal;

/**
 * 
 * @author subhash
 *
 */

public class TaxInfo {

	
	private String installment;
	private BigDecimal conservancyTax;
	private BigDecimal waterTax;
	private BigDecimal generalTax;
	private BigDecimal fireServiceTax;
	private BigDecimal lightTax;
	private BigDecimal eduCess;
	private BigDecimal egsCess;
	private BigDecimal bigBuildingCess;
	private BigDecimal total;
	private BigDecimal grandTotal;
	
	public String getInstallment() {
		return installment;
	}
	public void setInstallment(String installment) {
		this.installment = installment;
	}
	public BigDecimal getConservancyTax() {
		return conservancyTax;
	}
	public void setConservancyTax(BigDecimal conservancyTax) {
		this.conservancyTax = conservancyTax;
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
	public BigDecimal getFireServiceTax() {
		return fireServiceTax;
	}
	public void setFireServiceTax(BigDecimal fireServiceTax) {
		this.fireServiceTax = fireServiceTax;
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
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
	public BigDecimal getGrandTotal() {
		return grandTotal;
	}
	
	public void setGrandTotal(BigDecimal grandTotal) {
		this.grandTotal = grandTotal;
	}	
}
