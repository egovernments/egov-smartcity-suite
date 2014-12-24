package org.egov.ptis.bean;

import java.math.BigDecimal;

public class DemandCollInfo {

	private Integer orderNo;
	private String taxType;
	private BigDecimal arrDemand;
	private BigDecimal curDemand;
	private BigDecimal arrColl;
	private BigDecimal curColl;
	
	public Integer getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
	public String getTaxType() {
		return taxType;
	}
	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}
	public BigDecimal getArrDemand() {
		return arrDemand;
	}
	public void setArrDemand(BigDecimal arrDemand) {
		this.arrDemand = arrDemand;
	}
	public BigDecimal getCurDemand() {
		return curDemand;
	}
	public void setCurDemand(BigDecimal curDemand) {
		this.curDemand = curDemand;
	}
	public BigDecimal getArrColl() {
		return arrColl;
	}
	public void setArrColl(BigDecimal arrColl) {
		this.arrColl = arrColl;
	}
	public BigDecimal getCurColl() {
		return curColl;
	}
	public void setCurColl(BigDecimal curColl) {
		this.curColl = curColl;
	}
	
	
}
