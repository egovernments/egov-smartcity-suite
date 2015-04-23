package org.egov.ptis.bean;

import java.math.BigDecimal;

/**
 * 
 * @author subhash
 *
 */
public class ActiveDemandInfo {

	private Integer boundaryId;
	private String boundaryName;
	private String partNo;
	private Integer count;
	private BigDecimal arrDmd;
	private BigDecimal currDmd;
	private BigDecimal totDmd;

	public Integer getBoundaryId() {
		return boundaryId;
	}

	public void setBoundaryId(BigDecimal boundaryId) {
		this.boundaryId = Integer.valueOf(boundaryId.toString());
	}

	public String getBoundaryName() {
		return boundaryName;
	}

	public void setBoundaryName(String boundaryName) {
		this.boundaryName = boundaryName;
	}

	public String getPartNo() {
		return partNo;
	}

	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(BigDecimal count) {
		this.count = Integer.valueOf(count.toString());
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

	public BigDecimal getTotDmd() {
		return totDmd;
	}

	public void setTotDmd(BigDecimal totDmd) {
		this.totDmd = totDmd;
	}

}
