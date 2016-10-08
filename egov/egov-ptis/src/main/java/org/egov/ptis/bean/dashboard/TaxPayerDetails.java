package org.egov.ptis.bean.dashboard;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

public class TaxPayerDetails {
	
	private String regionName = StringUtils.EMPTY;
	private String districtName = StringUtils.EMPTY;
	private String ulbGrade = StringUtils.EMPTY;
	private String ulbName = StringUtils.EMPTY;
	private String wardName = StringUtils.EMPTY;
	private BigDecimal totalDemand = BigDecimal.ZERO;
	private BigDecimal proportionalDemand = BigDecimal.ZERO;
	private BigDecimal collections = BigDecimal.ZERO;
	private BigDecimal percentageAchievements = BigDecimal.ZERO;
	private BigDecimal proportionalBalance = BigDecimal.ZERO;
	private BigDecimal LastYearCollection = BigDecimal.ZERO;
	private BigDecimal variation = BigDecimal.ZERO;
	public BigDecimal getTotalDemand() {
		return totalDemand;
	}
	public void setTotalDemand(BigDecimal totalDemand) {
		this.totalDemand = totalDemand;
	}
	public BigDecimal getProportionalDemand() {
		return proportionalDemand;
	}
	public void setProportionalDemand(BigDecimal proportionalDemand) {
		this.proportionalDemand = proportionalDemand;
	}
	public BigDecimal getCollections() {
		return collections;
	}
	public void setCollections(BigDecimal collections) {
		this.collections = collections;
	}
	public BigDecimal getPercentageAchievements() {
		return percentageAchievements;
	}
	public void setPercentageAchievements(BigDecimal percentageAchievements) {
		this.percentageAchievements = percentageAchievements;
	}
	public BigDecimal getProportionalBalance() {
		return proportionalBalance;
	}
	public void setProportionalBalance(BigDecimal proportionalBalance) {
		this.proportionalBalance = proportionalBalance;
	}
	public BigDecimal getLastYearCollection() {
		return LastYearCollection;
	}
	public void setLastYearCollection(BigDecimal lastYearCollection) {
		LastYearCollection = lastYearCollection;
	}
	public BigDecimal getVariation() {
		return variation;
	}
	public void setVariation(BigDecimal variation) {
		this.variation = variation;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public String getDistrictName() {
		return districtName;
	}
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	public String getUlbGrade() {
		return ulbGrade;
	}
	public void setUlbGrade(String ulbGrade) {
		this.ulbGrade = ulbGrade;
	}
	public String getUlbName() {
		return ulbName;
	}
	public void setUlbName(String ulbName) {
		this.ulbName = ulbName;
	}
	public String getWardName() {
		return wardName;
	}
	public void setWardName(String wardName) {
		this.wardName = wardName;
	}
}
