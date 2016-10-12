package org.egov.ptis.bean.dashboard;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

public class TaxPayerDetails  implements Comparable<TaxPayerDetails>{
	
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
	@Override
	public int compareTo(TaxPayerDetails object) {
		// TODO Auto-generated method stub
		return achievement.compareTo(object.getAchievement());
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((achievement == null) ? 0 : achievement.hashCode());
		result = prime * result
				+ ((cytdBalDmd == null) ? 0 : cytdBalDmd.hashCode());
		result = prime * result
				+ ((cytdColl == null) ? 0 : cytdColl.hashCode());
		result = prime * result + ((cytdDmd == null) ? 0 : cytdDmd.hashCode());
		result = prime * result
				+ ((districtName == null) ? 0 : districtName.hashCode());
		result = prime * result + ((lyVar == null) ? 0 : lyVar.hashCode());
		result = prime * result
				+ ((lytdColl == null) ? 0 : lytdColl.hashCode());
		result = prime * result
				+ ((regionName == null) ? 0 : regionName.hashCode());
		result = prime * result
				+ ((totalDmd == null) ? 0 : totalDmd.hashCode());
		result = prime * result
				+ ((ulbGrade == null) ? 0 : ulbGrade.hashCode());
		result = prime * result + ((ulbName == null) ? 0 : ulbName.hashCode());
		result = prime * result
				+ ((wardName == null) ? 0 : wardName.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaxPayerDetails other = (TaxPayerDetails) obj;
		if (achievement == null) {
			if (other.achievement != null)
				return false;
		} else if (!achievement.equals(other.achievement))
			return false;
		if (cytdBalDmd == null) {
			if (other.cytdBalDmd != null)
				return false;
		} else if (!cytdBalDmd.equals(other.cytdBalDmd))
			return false;
		if (cytdColl == null) {
			if (other.cytdColl != null)
				return false;
		} else if (!cytdColl.equals(other.cytdColl))
			return false;
		if (cytdDmd == null) {
			if (other.cytdDmd != null)
				return false;
		} else if (!cytdDmd.equals(other.cytdDmd))
			return false;
		if (districtName == null) {
			if (other.districtName != null)
				return false;
		} else if (!districtName.equals(other.districtName))
			return false;
		if (lyVar == null) {
			if (other.lyVar != null)
				return false;
		} else if (!lyVar.equals(other.lyVar))
			return false;
		if (lytdColl == null) {
			if (other.lytdColl != null)
				return false;
		} else if (!lytdColl.equals(other.lytdColl))
			return false;
		if (regionName == null) {
			if (other.regionName != null)
				return false;
		} else if (!regionName.equals(other.regionName))
			return false;
		if (totalDmd == null) {
			if (other.totalDmd != null)
				return false;
		} else if (!totalDmd.equals(other.totalDmd))
			return false;
		if (ulbGrade == null) {
			if (other.ulbGrade != null)
				return false;
		} else if (!ulbGrade.equals(other.ulbGrade))
			return false;
		if (ulbName == null) {
			if (other.ulbName != null)
				return false;
		} else if (!ulbName.equals(other.ulbName))
			return false;
		if (wardName == null) {
			if (other.wardName != null)
				return false;
		} else if (!wardName.equals(other.wardName))
			return false;
		return true;
	}
	
}
