/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
package org.egov.collection.integration.models;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

    // true => call the new API (of billing system), false => default logic
    // (done by collection system).
    private Boolean callbackForApportioning;

    private Boolean overrideAccountHeadsAllowed;

    private COLLECTIONTYPE collectionType;

    @XStreamImplicit(itemFieldName = "collectionModeNotAllowed")
    private List<String> collectionModesNotAllowed;

    @XStreamAlias("payees")
    private List<BillPayeeDetails> payees = new ArrayList<>(0);

    private String transactionReferenceNumber;

    private String source;

    @Override
    public String getServiceCode() {
        return serviceCode;
    }

    public BillInfoImpl() {
    }

    public BillInfoImpl(final String serviceCode, final String fundCode, final BigDecimal functionaryCode,
            final String fundSourceCode, final String departmentCode, final String displayMessage, final String paidBy,
            final Boolean partPaymentAllowed, final Boolean overrideAccountHeadsAllowed,
            final List<String> collectionModesNotAllowed, final COLLECTIONTYPE collectionType) {
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

    @Override
    public String getFundCode() {
        return fundCode;
    }

    @Override
    public BigDecimal getFunctionaryCode() {
        return functionaryCode;
    }

    @Override
    public String getFundSourceCode() {
        return fundSourceCode;
    }

    @Override
    public String getDepartmentCode() {
        return departmentCode;
    }

    @Override
    public String getDisplayMessage() {
        return displayMessage;
    }

    @Override
    public Boolean getPartPaymentAllowed() {
        return partPaymentAllowed;
    }

    @Override
    public Boolean getOverrideAccountHeadsAllowed() {
        return overrideAccountHeadsAllowed;
    }

    @Override
    public List<String> getCollectionModesNotAllowed() {
        return collectionModesNotAllowed;
    }

    @Override
    public List<BillPayeeDetails> getPayees() {
        return payees;
    }

    @Override
    public void setPayees(final List<BillPayeeDetails> payees) {
        this.payees = payees;
    }

    @Override
    public void addPayees(final BillPayeeDetails payee) {
        payees.add(payee);
    }

    @Override
    public COLLECTIONTYPE getCollectionType() {
        return collectionType;
    }

    @Override
    public String getPaidBy() {
        return paidBy;
    }

    @Override
    public Boolean getCallbackForApportioning() {
        return callbackForApportioning;
    }

    public void setCallbackForApportioning(final Boolean callbackForApportioning) {
        this.callbackForApportioning = callbackForApportioning;
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof BillInfoImpl))
            return false;

        final BillInfoImpl billColl = (BillInfoImpl) obj;
        if (serviceCode.equals(billColl.getServiceCode()) && fundCode.equals(billColl.fundCode)
                && functionaryCode.equals(billColl.functionaryCode) && fundSourceCode.equals(billColl.fundSourceCode)
                && departmentCode.equals(billColl.departmentCode) && displayMessage.equals(billColl.displayMessage)
                && partPaymentAllowed == billColl.partPaymentAllowed
                && overrideAccountHeadsAllowed == billColl.overrideAccountHeadsAllowed
                && callbackForApportioning == billColl.callbackForApportioning
                && getPayees().containsAll(billColl.getPayees()))
            return collectionModesNotAllowed == billColl.getCollectionModesNotAllowed()
            || collectionModesNotAllowed != null
                    && collectionModesNotAllowed.containsAll(billColl.getCollectionModesNotAllowed());
        else
            return false;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int hashCode = serviceCode.hashCode() + fundCode.hashCode() + functionaryCode.hashCode()
                + fundSourceCode.hashCode() + departmentCode.hashCode() + displayMessage.hashCode()
                + partPaymentAllowed.hashCode() + overrideAccountHeadsAllowed.hashCode();
        for (final String collectionModeNotAllowed : collectionModesNotAllowed)
            hashCode += collectionModeNotAllowed.hashCode();
        return hashCode;
    }

    public String getTransactionReferenceNumber() {
        return this.transactionReferenceNumber;
    }

    public void setTransactionReferenceNumber(String transactionReferenceNumber) {
        this.transactionReferenceNumber = transactionReferenceNumber;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
