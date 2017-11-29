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
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@XStreamAlias("bill")
public class BillDetails {

    @XStreamAsAttribute
    private final String refNo;

    @XStreamAsAttribute
    private final Date billDate;

    @XStreamAsAttribute
    private final String consumerCode;

    private final String consumerType;

    private final String boundaryNum;
    private final String boundaryType;
    private final String description;

    private final BigDecimal totalAmount;
    private final BigDecimal minimumAmount;

    @XStreamAlias("accounts")
    private final List<BillAccountDetails> accounts = new ArrayList<BillAccountDetails>(0);

    public BillDetails(final String refNo, final Date billDate, final String consumerCode, final String consumerType,
            final String boundaryNum, final String boundaryType, final String description, final BigDecimal totalAmount,
            final BigDecimal minimumAmount) {
        this.refNo = refNo;
        this.billDate = billDate;
        this.boundaryNum = boundaryNum;
        this.boundaryType = boundaryType;
        this.description = description;
        this.totalAmount = totalAmount;
        this.minimumAmount = minimumAmount;
        this.consumerCode = consumerCode;
        this.consumerType = consumerType;
    }

    public String getRefNo() {
        return refNo;
    }

    public List<BillAccountDetails> getAccounts() {
        return accounts;
    }

    public void addBillAccountDetails(final BillAccountDetails billAccountDetail) {
        accounts.add(billAccountDetail);
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
    
    public String getConsumerType() {
        return consumerType;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof BillDetails) {
            final BillDetails billDetail = (BillDetails) obj;
            if (refNo.equals(billDetail.refNo) && billDate.equals(billDetail.billDate)
                    && boundaryNum.equals(billDetail.boundaryNum) && boundaryType.equals(billDetail.boundaryType)
                    && description.equals(billDetail.description) && minimumAmount.equals(billDetail.minimumAmount)
                    && consumerCode.equals(billDetail.consumerCode) && totalAmount.equals(billDetail.totalAmount)
                    && consumerType.equals(billDetail.consumerType))
                return true;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return billDate.hashCode() + refNo.hashCode() + boundaryNum.hashCode() + boundaryType.hashCode()
                + description.hashCode() + minimumAmount.hashCode() + consumerCode.hashCode() + totalAmount.hashCode()
                + consumerType.hashCode();
    }

}
