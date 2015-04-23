package org.egov.ptis.nmc.bill;

import java.util.Date;

public class PenaltyBill implements Comparable<PenaltyBill> {
	private Date createdDate;
	private Date occupancyDate;
	private Date billDate;
	private boolean isBillGeneratedAfterRollover;
	
	public PenaltyBill(Date createdDate, Date occupancyDate, Date billDate, boolean isBillGeneratedAfterRollover) {
		this.createdDate = createdDate;
		this.occupancyDate = occupancyDate;
		this.billDate = billDate;
		this.isBillGeneratedAfterRollover = isBillGeneratedAfterRollover;
	}
	
	public String toString() {
		return new StringBuilder(200).append("PenaltyBill [")
				.append("createdDate=").append(createdDate)
				.append(", occupancyDate=").append(occupancyDate)
				.append(", billDate=").append(billDate)
				.append(", isBillGeneratedAfterRollover=").append(isBillGeneratedAfterRollover).append("] ").toString();
	}
	
	public Date getCreatedDate() {
		return createdDate;
	}
	
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	public Date getOccupancyDate() {
		return occupancyDate;
	}
	
	public void setOccupancyDate(Date occupancyDate) {
		this.occupancyDate = occupancyDate;
	}
	
	public Date getBillDate() {
		return billDate;
	}
	
	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	public boolean getIsBillGeneratedAfterRollover() {
		return isBillGeneratedAfterRollover;
	}

	public void setIsBillGeneratedAfterRollover(boolean isBillGeneratedAfterRollover) {
		this.isBillGeneratedAfterRollover = isBillGeneratedAfterRollover;
	}

	@Override
	public int compareTo(PenaltyBill o) {
		return this.billDate.compareTo(o.getBillDate());
	}
}
