package org.egov.ptis.bean.dashboard;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

public class TaxPayerDetails {
	
	private String regionName = StringUtils.EMPTY;
	private String districtName = StringUtils.EMPTY;
	private String ulbGrade = StringUtils.EMPTY;
	private String ulbName = StringUtils.EMPTY;
	private String wardName = StringUtils.EMPTY;
	private BigDecimal totalDmd = BigDecimal.ZERO;
	private BigDecimal cytdDmd = BigDecimal.ZERO;
	private BigDecimal cytdColl = BigDecimal.ZERO;
	private BigDecimal achievement = BigDecimal.ZERO;
	private BigDecimal cytdBalDmd = BigDecimal.ZERO;
	private BigDecimal lytdColl = BigDecimal.ZERO;
	private BigDecimal lyVar = BigDecimal.ZERO;
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
	public BigDecimal getTotalDmd() {
		return totalDmd;
	}
	public void setTotalDmd(BigDecimal totalDmd) {
		this.totalDmd = totalDmd;
	}
	public BigDecimal getCytdDmd() {
		return cytdDmd;
	}
	public void setCytdDmd(BigDecimal cytdDmd) {
		this.cytdDmd = cytdDmd;
	}
	public BigDecimal getCytdColl() {
		return cytdColl;
	}
	public void setCytdColl(BigDecimal cytdColl) {
		this.cytdColl = cytdColl;
	}
	public BigDecimal getAchievement() {
		return achievement;
	}
	public void setAchievement(BigDecimal achievement) {
		this.achievement = achievement;
	}
	public BigDecimal getCytdBalDmd() {
		return cytdBalDmd;
	}
	public void setCytdBalDmd(BigDecimal cytdBalDmd) {
		this.cytdBalDmd = cytdBalDmd;
	}
	public BigDecimal getLytdColl() {
		return lytdColl;
	}
	public void setLytdColl(BigDecimal lytdColl) {
		this.lytdColl = lytdColl;
	}
	public BigDecimal getLyVar() {
		return lyVar;
	}
	public void setLyVar(BigDecimal lyVar) {
		this.lyVar = lyVar;
	}
}
