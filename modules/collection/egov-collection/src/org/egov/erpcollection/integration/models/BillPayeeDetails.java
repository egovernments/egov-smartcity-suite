package org.egov.erpcollection.integration.models;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("payee")
public class BillPayeeDetails {

	private final String payeeName;
	private final String payeeAddress;

	@XStreamAlias("bills")
	private final List<BillDetails> billDetails = new ArrayList<BillDetails>();

	public BillPayeeDetails(String payeeName, String payeeAddress) {
		super();
		this.payeeName = payeeName;
		this.payeeAddress = payeeAddress;
	}

	public String getPayeeName() {
		return payeeName;
	}

	public String getPayeeAddress() {
		return payeeAddress;
	}

	public List<BillDetails> getBillDetails() {
		return billDetails;
	}

	public void addBillDetails(BillDetails billDetail) {
		this.billDetails.add(billDetail);
	}

	public boolean equals(Object obj) {
		if (obj instanceof BillPayeeDetails) {
			BillPayeeDetails payee = (BillPayeeDetails) obj;
			if (this.payeeName.equals(payee.payeeName)
					&& this.payeeAddress.equals(payee.payeeAddress)) {
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
		return this.payeeName.hashCode() + this.payeeAddress.hashCode();
	}
}
