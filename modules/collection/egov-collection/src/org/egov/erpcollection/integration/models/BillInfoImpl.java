package org.egov.erpcollection.integration.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("bill-collect")
public class BillInfoImpl implements BillInfo {

	private String serviceCode;
	private String fundCode;
	private BigDecimal functionaryCode;
	private String fundSourceCode;
	private String departmentCode;

	private String displayMessage;
	private String paidBy;
	private Boolean partPaymentAllowed;
	
	// true => call the new API (of billing system), false => default logic (done by collection system).
	private Boolean callbackForApportioning;

	private Boolean overrideAccountHeadsAllowed;
	
	private COLLECTIONTYPE collectionType;

	@XStreamImplicit(itemFieldName = "collectionModeNotAllowed")
	private List<String> collectionModesNotAllowed;

	@XStreamAlias("payees")
	private List<BillPayeeDetails> payees = new ArrayList<BillPayeeDetails>();

	public String getServiceCode() {
		return serviceCode;
	}

	public BillInfoImpl() {
	}

	public BillInfoImpl(String serviceCode, String fundCode,
			BigDecimal functionaryCode, String fundSourceCode,
			String departmentCode, String displayMessage, String paidBy,
			Boolean partPaymentAllowed, Boolean overrideAccountHeadsAllowed,
			List<String> collectionModesNotAllowed,COLLECTIONTYPE collectionType) {
		this.serviceCode = serviceCode;
		this.fundCode = fundCode;
		this.functionaryCode = functionaryCode;
		this.fundSourceCode = fundSourceCode;
		this.departmentCode = departmentCode;
		this.displayMessage = displayMessage;
		this.paidBy = paidBy;
		this.partPaymentAllowed = partPaymentAllowed;
		this.overrideAccountHeadsAllowed = overrideAccountHeadsAllowed;
		this.collectionModesNotAllowed = collectionModesNotAllowed;
		this.collectionType = collectionType;
	}

	public String getFundCode() {
		return fundCode;
	}

	public BigDecimal getFunctionaryCode() {
		return functionaryCode;
	}

	public String getFundSourceCode() {
		return fundSourceCode;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	public String getDisplayMessage() {
		return displayMessage;
	}

	public Boolean getPartPaymentAllowed() {
		return partPaymentAllowed;
	}

	public Boolean getOverrideAccountHeadsAllowed() {
		return overrideAccountHeadsAllowed;
	}

	public List<String> getCollectionModesNotAllowed() {
		return collectionModesNotAllowed;
	}

	public List<BillPayeeDetails> getPayees() {
		return payees;
	}

	public void setPayees(List<BillPayeeDetails> payees) {
		this.payees = payees;
	}

	public void addPayees(BillPayeeDetails payee) {
		this.payees.add(payee);
	}
	
	public COLLECTIONTYPE getCollectionType() {
		return collectionType;
	}
	
	public String getPaidBy() {
		return paidBy;
	}
	
	public Boolean getCallbackForApportioning() {
		return callbackForApportioning;
	}

	public void setCallbackForApportioning(Boolean callbackForApportioning) {
		this.callbackForApportioning = callbackForApportioning;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof BillInfoImpl)) {
			return false;
		}

		BillInfoImpl billColl = (BillInfoImpl) obj;
		if (this.serviceCode.equals(billColl.getServiceCode())
				&& this.fundCode.equals(billColl.fundCode)
				&& this.functionaryCode.equals(billColl.functionaryCode)
				&& this.fundSourceCode.equals(billColl.fundSourceCode)
				&& this.departmentCode.equals(billColl.departmentCode)
				&& this.displayMessage.equals(billColl.displayMessage)
				&& this.partPaymentAllowed == billColl.partPaymentAllowed
				&& this.overrideAccountHeadsAllowed == billColl.overrideAccountHeadsAllowed
				&& this.callbackForApportioning == billColl.callbackForApportioning
				&& this.getPayees().containsAll(billColl.getPayees())) {

			return this.collectionModesNotAllowed == billColl
					.getCollectionModesNotAllowed()
					|| (this.collectionModesNotAllowed != null && this.collectionModesNotAllowed
							.containsAll(billColl
									.getCollectionModesNotAllowed()));
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hashCode = this.serviceCode.hashCode() + this.fundCode.hashCode()
				+ this.functionaryCode.hashCode()
				+ this.fundSourceCode.hashCode()
				+ this.departmentCode.hashCode()
				+ this.displayMessage.hashCode()
				+ this.partPaymentAllowed.hashCode()
				+ this.overrideAccountHeadsAllowed.hashCode();
		for (String collectionModeNotAllowed : this.collectionModesNotAllowed) {
			hashCode += collectionModeNotAllowed.hashCode();
		}
		return hashCode;
	}
}
