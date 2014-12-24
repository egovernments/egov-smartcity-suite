package org.egov.ptis.domain.entity.property;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

public class PropertyMaterlizeView implements Serializable {

	private Integer basicPropertyID;
	private String propertyId;
	private String ownerName;
	private String houseNo;
	private String propertyAddress;
	private PropertyTypeMaster propTypeMstrID;
	private Integer propUsageMstrID;
	private Integer wardID;
	private Integer zoneID;
	private Integer streetID;
	private Integer sourceID;
	private BigDecimal sitalArea;
	private BigDecimal toalBuiltUpArea;
	private Integer latestStatus;
	private BigDecimal aggrCurrDmd;
	private BigDecimal aggrArrDmd;
	private BigDecimal aggrCurrColl;
	private BigDecimal aggrArrColl;
	private BigDecimal totalDemand;
	private String gisRefNo;
	private Set<InstDmdCollMaterializeView> instDmdColl;
	private Set<CurrFloorDmdCalcMaterializeView> currFloorDmdCalc;
	private String waterScheme;
	private String partNo;
	private BigDecimal alv;
	
	public Integer getBasicPropertyID() {
		return basicPropertyID;
	}

	public void setBasicPropertyID(Integer tbasicPropertyID) {
		this.basicPropertyID = tbasicPropertyID;
	}

	public String getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public String getPropertyAddress() {
		return propertyAddress;
	}

	public void setPropertyAddress(String propertyAddress) {
		this.propertyAddress = propertyAddress;
	}

	public Integer getWardID() {
		return wardID;
	}

	public void setWardID(Integer wardID) {
		this.wardID = wardID;
	}

	public Integer getZoneID() {
		return zoneID;
	}

	public void setZoneID(Integer zoneID) {
		this.zoneID = zoneID;
	}

	public Integer getStreetID() {
		return streetID;
	}

	public void setStreetID(Integer streetID) {
		this.streetID = streetID;
	}

	public Integer getSourceID() {
		return sourceID;
	}

	public void setSourceID(Integer sourceID) {
		this.sourceID = sourceID;
	}

	public BigDecimal getSitalArea() {
		return sitalArea;
	}

	public void setSitalArea(BigDecimal sitalArea) {
		this.sitalArea = sitalArea;
	}

	public BigDecimal getToalBuiltUpArea() {
		return toalBuiltUpArea;
	}

	public void setToalBuiltUpArea(BigDecimal toalBuiltUpArea) {
		this.toalBuiltUpArea = toalBuiltUpArea;
	}

	public Integer getLatestStatus() {
		return latestStatus;
	}

	public void setLatestStatus(Integer latestStatus) {
		this.latestStatus = latestStatus;
	}

	public BigDecimal getAggrCurrDmd() {
		return aggrCurrDmd;
	}

	public void setAggrCurrDmd(BigDecimal aggrCurrDmd) {
		this.aggrCurrDmd = aggrCurrDmd;
	}

	public PropertyTypeMaster getPropTypeMstrID() {
		return propTypeMstrID;
	}

	public void setPropTypeMstrID(PropertyTypeMaster propTypeMstrID) {
		this.propTypeMstrID = propTypeMstrID;
	}

	public Integer getPropUsageMstrID() {
		return propUsageMstrID;
	}

	public void setPropUsageMstrID(Integer propUsageMstrID) {
		this.propUsageMstrID = propUsageMstrID;
	}

	public BigDecimal getAggrArrDmd() {
		return aggrArrDmd;
	}

	public void setAggrArrDmd(BigDecimal aggrArrDmd) {
		this.aggrArrDmd = aggrArrDmd;
	}

	public BigDecimal getAggrCurrColl() {
		return aggrCurrColl;
	}

	public void setAggrCurrColl(BigDecimal aggrCurrColl) {
		this.aggrCurrColl = aggrCurrColl;
	}

	public BigDecimal getAggrArrColl() {
		return aggrArrColl;
	}

	public void setAggrArrColl(BigDecimal aggrArrColl) {
		this.aggrArrColl = aggrArrColl;
	}

	public BigDecimal getTotalDemand() {
		return totalDemand;
	}

	public void setTotalDemand(BigDecimal totalDemand) {
		this.totalDemand = totalDemand;
	}

	public String getGisRefNo() {
		return gisRefNo;
	}

	public void setGisRefNo(String gisRefNo) {
		this.gisRefNo = gisRefNo;
	}

	public Set<InstDmdCollMaterializeView> getInstDmdColl() {
		return instDmdColl;
	}

	public void setInstDmdColl(Set<InstDmdCollMaterializeView> instDmdColl) {
		this.instDmdColl = instDmdColl;
	}

	public Set<CurrFloorDmdCalcMaterializeView> getCurrFloorDmdCalc() {
		return currFloorDmdCalc;
	}

	public void setCurrFloorDmdCalc(Set<CurrFloorDmdCalcMaterializeView> currFloorDmdCalc) {
		this.currFloorDmdCalc = currFloorDmdCalc;
	}

	public String getWaterScheme() {
		return waterScheme;
	}

	public void setWaterScheme(String waterScheme) {
		this.waterScheme = waterScheme;
	}

	public String getPartNo() {
		return partNo;
	}

	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}

	public BigDecimal getAlv() {
		return alv;
	}

	public void setAlv(BigDecimal alv) {
		this.alv = alv;
	}

	@Override
	public String toString() {
		StringBuilder objStr = new StringBuilder();

		objStr.append("BasicPropertyId: " + getBasicPropertyID()).append("|PropertyId: ").append(getPropertyId())
				.append("|SitalArea: ").append(getSitalArea()).append("|AggCurrDemand: ").append(getAggrCurrDmd())
				.append("|AggArrDemand: ").append(getAggrArrDmd()).append("|AggCurrColl: ").append(getAggrCurrColl())
				.append("|AggArrColl: ").append(getAggrArrColl()).append("|TotalDemand: ").append(getTotalDemand());
				//.append("|AVAmount: ").append(getAvAmt());

		return objStr.toString();
	}
}
