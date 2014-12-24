package org.egov.erpcollection.integration.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("bill")
public class BillDetails {

	@XStreamAsAttribute
	private final String refNo;

	@XStreamAsAttribute
	private final Date billDate;

	@XStreamAsAttribute
	private final String consumerCode;

	private final String boundaryNum;
	private final String boundaryType;
	private final String description;

	private final BigDecimal totalAmount;
	private final BigDecimal minimumAmount;

	@XStreamAlias("accounts")
	private final List<BillAccountDetails> accounts = new ArrayList<BillAccountDetails>();

	public BillDetails(String refNo, Date billDate, String consumerCode,
			String boundaryNum, String boundaryType, String description,
			BigDecimal totalAmount, BigDecimal minimumAmount) {
		this.refNo = refNo;
		this.billDate = billDate;
		this.boundaryNum = boundaryNum;
		this.boundaryType = boundaryType;
		this.description = description;
		this.totalAmount = totalAmount;
		this.minimumAmount = minimumAmount;
		this.consumerCode = consumerCode;
	}

	public String getRefNo() {
		return refNo;
	}

	public List<BillAccountDetails> getAccounts() {
		return accounts;
	}

	public void addBillAccountDetails(BillAccountDetails billAccountDetail) {
		this.accounts.add(billAccountDetail);
	}

	public String getDescription() {
		return description;
	}

	public Date getBilldate() {
		return billDate;
	}

	public String getBoundaryNum() {
		return boundaryNum;
	}

	public String getBoundaryType() {
		return boundaryType;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public BigDecimal getMinimumAmount() {
		return minimumAmount;
	}

	public String getConsumerCode() {
		return consumerCode;
	}

	public boolean equals(Object obj) {
		if (obj instanceof BillDetails) {
			BillDetails billDetail = (BillDetails) obj;
			if (this.refNo.equals(billDetail.refNo)
					&& this.billDate.equals(billDetail.billDate)
					&& this.boundaryNum.equals(billDetail.boundaryNum)
					&& this.boundaryType.equals(billDetail.boundaryType)
					&& this.description.equals(billDetail.description)
					&& this.minimumAmount.equals(billDetail.minimumAmount)
					&& this.consumerCode.equals(billDetail.consumerCode)
					&& this.totalAmount.equals(billDetail.totalAmount)) {
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.billDate.hashCode() + this.refNo.hashCode()
				+ this.boundaryNum.hashCode() + this.boundaryType.hashCode()
				+ this.description.hashCode() + this.minimumAmount.hashCode()
				+ this.consumerCode.hashCode() + this.totalAmount.hashCode();
	}
	
	/*public String toString(){
		return "BillDetails [ RefNo : " + this.refNo + " BillAccounts : " + this.accounts + " ]";  
	}*/
}
