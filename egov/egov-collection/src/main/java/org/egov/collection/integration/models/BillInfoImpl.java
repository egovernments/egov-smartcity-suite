/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.collection.integration.models;

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
