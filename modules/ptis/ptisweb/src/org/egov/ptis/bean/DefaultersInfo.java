package org.egov.ptis.bean;

import java.math.BigDecimal;

public class DefaultersInfo {
		
	private String zoneNo;
	private String wardNo;
	private String partNo;
	private String indexNo;
	private String ownerName;
	private String houseNo;
	private BigDecimal arrearsTax;
	private BigDecimal currentTax;
	private BigDecimal total;
		
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
	
	public BigDecimal getArrearsTax() {
		return arrearsTax;
	}
	
	public void setArrearsTax(BigDecimal arrearsTax) {
		this.arrearsTax = arrearsTax;
	}
	
	public BigDecimal getCurrentTax() {
		return currentTax;
	}

	public void setCurrentTax(BigDecimal currentTax) {
		this.currentTax = currentTax;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}
}
