package org.egov.payroll.web.actions.billNumber;

/**
 * 
 * @author subhash
 *
 */

public class BillNumberBean {
	private String billNumberId;
	private String billNumber;
	private String positionId;
	private String positionName;
	public String getBillNumber() {
		return billNumber;
	}
	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}
	public String getPositionId() {
		return positionId;
	}
	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}
	public String getPositionName() {
		return positionName;
	}
	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}
	public String getBillNumberId() {
		return billNumberId;
	}
	public void setBillNumberId(String billNumberId) {
		this.billNumberId = billNumberId;
	}	
}
