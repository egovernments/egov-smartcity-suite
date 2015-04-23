package org.egov.ptis.bean;

import java.math.BigDecimal;

public class DefaultersInfo {
		
	private String zoneNo;
	private String wardNo;
	private String partNo;
	private String indexNo;
	private String ownerName;
	private String houseNo;
	private BigDecimal arrearsDue;
	private BigDecimal currentDue;
	private BigDecimal total;
	private Integer fromYear;
	private Integer toYear;
		
	public String getZoneNo() {
		return zoneNo;
	}

	public void setZoneNo(String zoneNo) {
		this.zoneNo = zoneNo;
	}

	public String getWardNo() {
		return wardNo;
	}

	public void setWardNo(String wardNo) {
		this.wardNo = wardNo;
	}

	public String getPartNo() {
		return partNo;
	}

	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}

	public String getIndexNo() {
		return indexNo;
	}
	
	public void setIndexNo(String indexNo) {
		this.indexNo = indexNo;
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
	
	public BigDecimal getArrearsDue() {
		return arrearsDue;
	}

	public void setArrearsDue(BigDecimal arrearsDue) {
		this.arrearsDue = arrearsDue;
	}

	public BigDecimal getCurrentDue() {
		return currentDue;
	}

	public void setCurrentDue(BigDecimal currentDue) {
		this.currentDue = currentDue;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public Integer getFromYear() {
		return fromYear;
	}

	public void setFromYear(Integer fromYear) {
		this.fromYear = fromYear;
	}

	public Integer getToYear() {
		return toYear;
	}

	public void setToYear(Integer toYear) {
		this.toYear = toYear;
	}
	
}
