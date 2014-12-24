package org.egov.ptis.bean;

import java.math.BigDecimal;

public class UnitWiseInfo {

	private String indexNumber;
	private String propType;
	private String houseNo;
	private String unitNo;
	private String ownerName;
	private BigDecimal alv;
	private BigDecimal conservancyTax;
	private String waterScheme;
	private BigDecimal waterTax;
	private BigDecimal generalTax; 
	private BigDecimal fireServiceTax;
	private BigDecimal lightTax;
	private BigDecimal totalTax; 
	private BigDecimal eduCess;
	private BigDecimal egsCess;
	private BigDecimal bigBuildingTax;
	
	public String getIndexNumber() {
		return indexNumber;
	}
	public void setIndexNumber(String indexNumber) {
		this.indexNumber = indexNumber;
	}
	public String getPropType() {
		return propType;
	}
	public void setPropType(String propType) {
		this.propType = propType;
	}
	public String getHouseNo() {
		return houseNo;
	}
	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}
	public String getUnitNo() {
		return unitNo;
	}
	public void setUnitNo(String unitNo) {
		this.unitNo = unitNo;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public BigDecimal getAlv() {
		return alv;
	}
	public void setAlv(BigDecimal alv) {
		this.alv = alv;
	}
	public BigDecimal getConservancyTax() {
		return conservancyTax;
	}
	public void setConservancyTax(BigDecimal conservancyTax) {
		this.conservancyTax = conservancyTax;
	}
	public String getWaterScheme() {
		return waterScheme;
	}
	public void setWaterScheme(String waterScheme) {
		this.waterScheme = waterScheme;
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
	public BigDecimal getTotalTax() {
		return totalTax;
	}
	public void setTotalTax(BigDecimal totalTax) {
		this.totalTax = totalTax;
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
	public BigDecimal getBigBuildingTax() {
		return bigBuildingTax;
	}
	public void setBigBuildingTax(BigDecimal bigBuildingTax) {
		this.bigBuildingTax = bigBuildingTax;
	}
}
