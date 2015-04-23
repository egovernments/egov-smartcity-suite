package org.egov.ptis.actions.reports;

import java.math.BigDecimal;

public class BoundryWisePropUsgeBean {

	private String zoneId;
	private Integer wardId;
	private Integer propUsgeId;
	private BigDecimal arrDmd;
	private BigDecimal currDmd;
	private BigDecimal totalDemand;
	private Integer aggProps;
	private BigDecimal aggCurrDemand;
	private BigDecimal aggArrDemand;
	private BigDecimal aggTotDemand;
	private Integer propCount;

	private BigDecimal indCurrDemand = BigDecimal.ZERO;
	private BigDecimal indArrDemand = BigDecimal.ZERO;
	private BigDecimal indTotDemand = BigDecimal.ZERO;
	private BigDecimal indAvAmt = BigDecimal.ZERO;

	public BigDecimal getIndAvAmt() {
		return indAvAmt;
	}

	public void setIndAvAmt(BigDecimal indAvAmt) {
		this.indAvAmt = indAvAmt;
	}

	private Integer indNoOfPropCount = 0;

	public String getZoneId() {
		return zoneId;
	}

	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public Integer getPropUsgeId() {
		return propUsgeId;
	}

	public void setPropUsgeId(Integer propUsgeId) {
		this.propUsgeId = propUsgeId;
	}

	public BigDecimal getArrDmd() {
		return arrDmd;
	}

	public void setArrDmd(BigDecimal arrDmd) {
		this.arrDmd = arrDmd;
	}

	public BigDecimal getCurrDmd() {
		return currDmd;
	}

	public void setCurrDmd(BigDecimal currDmd) {
		this.currDmd = currDmd;
	}

	public Integer getPropCount() {
		return propCount;
	}

	public void setPropCount(Integer propCount) {
		this.propCount = propCount;
	}

	public BigDecimal getTotalDemand() {
		return totalDemand;
	}

	public void setTotalDemand(BigDecimal totalDemand) {
		this.totalDemand = totalDemand;
	}

	public Integer getAggProps() {
		return aggProps;
	}

	public void setAggProps(Integer aggProps) {
		this.aggProps = aggProps;
	}

	public BigDecimal getAggCurrDemand() {
		return aggCurrDemand;
	}

	public void setAggCurrDemand(BigDecimal aggCurrDemand) {
		this.aggCurrDemand = aggCurrDemand;
	}

	public BigDecimal getAggArrDemand() {
		return aggArrDemand;
	}

	public void setAggArrDemand(BigDecimal aggArrDemand) {
		this.aggArrDemand = aggArrDemand;
	}

	public BigDecimal getAggTotDemand() {
		return aggTotDemand;
	}

	public void setAggTotDemand(BigDecimal aggTotDemand) {
		this.aggTotDemand = aggTotDemand;
	}

	public BigDecimal getIndCurrDemand() {
		return indCurrDemand;
	}

	public void setIndCurrDemand(BigDecimal indCurrDemand) {
		this.indCurrDemand = indCurrDemand;
	}

	public BigDecimal getIndArrDemand() {
		return indArrDemand;
	}

	public void setIndArrDemand(BigDecimal indArrDemand) {
		this.indArrDemand = indArrDemand;
	}

	public BigDecimal getIndTotDemand() {
		return indTotDemand;
	}

	public void setIndTotDemand(BigDecimal indTotDemand) {
		this.indTotDemand = indTotDemand;
	}

	public Integer getIndNoOfPropCount() {
		return indNoOfPropCount;
	}

	public void setIndNoOfPropCount(Integer indNoOfPropCount) {
		this.indNoOfPropCount = indNoOfPropCount;
	}

}
