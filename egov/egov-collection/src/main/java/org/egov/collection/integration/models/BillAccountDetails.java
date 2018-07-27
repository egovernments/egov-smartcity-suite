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

import java.math.BigDecimal;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("account")
public class BillAccountDetails implements Comparable<BillAccountDetails> {

    public enum PURPOSE {
        ARREAR_AMOUNT, CURRENT_AMOUNT, ADVANCE_AMOUNT, ARREAR_LATEPAYMENT_CHARGES, CURRENT_LATEPAYMENT_CHARGES, CHEQUE_BOUNCE_PENALTY, REBATE, OTHERS, SERVICETAX, CG_SERVICETAX, SG_SERVICETAX
    };

    @XStreamAsAttribute
    private final String glCode;

    @XStreamAsAttribute
    private final Integer order;

    @XStreamAsAttribute
    private final String description;

    @XStreamAlias("crAmount")
    private final BigDecimal crAmount;
    @XStreamAlias("drAmount")
    private final BigDecimal drAmount;
    private final String functionCode;

    @XStreamAsAttribute
    private final Boolean isActualDemand;

    @XStreamAsAttribute
    private final PURPOSE purpose;

    @XStreamAsAttribute
    private Integer groupId;

    public BillAccountDetails(final String glCode, final Integer order, final BigDecimal crAmount,
            final BigDecimal drAmount, final String functionCode, final String description, final Boolean isActualDemand,
            final PURPOSE purpose) {
        this.glCode = glCode;
        this.order = order;
        this.crAmount = crAmount;
        this.drAmount = drAmount;
        this.functionCode = functionCode;
        this.description = description;
        this.isActualDemand = isActualDemand;
        this.purpose = purpose;
    }

    public BillAccountDetails(final String glCode, final Integer order, final BigDecimal crAmount,
            final BigDecimal drAmount, final String functionCode, final String description, final Boolean isActualDemand,
            final PURPOSE purpose, final Integer groupId) {
        this.glCode = glCode;
        this.order = order;
        this.crAmount = crAmount;
        this.drAmount = drAmount;
        this.functionCode = functionCode;
        this.description = description;
        this.isActualDemand = isActualDemand;
        this.purpose = purpose;
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(order).append(",").append(glCode).append(",").append(crAmount).append(",").append(crAmount)
                .append(",").append(description).append(",").append(isActualDemand);
        return sb.toString();
    }

    public String getGlCode() {
        return glCode;
    }

    public Integer getOrder() {
        return order;
    }

    public BigDecimal getDrAmount() {
        return drAmount;
    }

    public BigDecimal getCrAmount() {
        return crAmount;
    }

    public String getFunctionCode() {
        return functionCode;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getIsActualDemand() {
        return isActualDemand;
    }

    public PURPOSE getPurpose() {
        return purpose;
    }

    @Override
    public int compareTo(final BillAccountDetails obj) {
        return order - obj.order;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof BillAccountDetails) {
            final BillAccountDetails account = (BillAccountDetails) obj;
            if (glCode.equals(account.glCode) && order.equals(account.order) && crAmount.equals(account.crAmount)
                    && drAmount.equals(account.drAmount) && description.equals(account.description)
                    && functionCode.equals(account.functionCode) && isActualDemand.equals(account.isActualDemand))
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
        return glCode.hashCode() + order.hashCode() + crAmount.hashCode() + drAmount.hashCode()
                + description.hashCode() + functionCode.hashCode() + isActualDemand.hashCode();
    }

    public Integer getGroupId() {
        return groupId;
    }

}
