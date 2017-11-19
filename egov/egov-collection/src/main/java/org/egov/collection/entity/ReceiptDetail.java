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
package org.egov.collection.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;

public class ReceiptDetail implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private static final String TO_STRING_SEP = "-";
    private Long id;
    private ReceiptHeader receiptHeader;
    private CChartOfAccounts accounthead;
    private BigDecimal dramount;

    /**
     * A <code>BigDecimal</code> representing the actual amount to be paid by the user
     */
    private BigDecimal cramountToBePaid;

    /**
     * A <code>BigDecimal</code> representing the actual amount paid by the user
     */
    private BigDecimal cramount;
    private Long ordernumber;

    /**
     * A <code>String</code> representing the glcode description sent from billing system
     */
    private String description;
    private CFunction function;

    /**
     * A <code>Long</code> representing the glcode is part of actual demand or not
     */
    private Boolean isActualDemand;

    private Set<AccountPayeeDetail> accountPayeeDetails = new HashSet<AccountPayeeDetail>(0);

    private CFinancialYear financialYear;

    private String purpose;

    public ReceiptDetail() {
    }

    public ReceiptDetail(final CChartOfAccounts accounthead, final CFunction function, final BigDecimal cramountToBePaid,
            final BigDecimal dramount, final BigDecimal cramount, final Long ordernumber, final String description,
            final Boolean isActualDemand,
            final ReceiptHeader receiptHeader, final String purpose) {
        this.accounthead = accounthead;
        this.function = function;
        this.cramountToBePaid = cramountToBePaid;
        this.dramount = dramount;
        this.cramount = cramount;
        this.ordernumber = ordernumber;
        this.description = description;
        this.isActualDemand = isActualDemand;
        this.receiptHeader = receiptHeader;
        this.purpose = purpose;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb
                .append(ordernumber).append(TO_STRING_SEP)
                .append(accounthead.getGlcode()).append(TO_STRING_SEP)
                .append(cramountToBePaid).append(TO_STRING_SEP)
                .append(cramount).append(TO_STRING_SEP)
                .append(dramount).append(TO_STRING_SEP)
                .append(description).append(TO_STRING_SEP)
                .append(isActualDemand);
        return sb.toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public ReceiptHeader getReceiptHeader() {
        return receiptHeader;
    }

    public void setReceiptHeader(
            final ReceiptHeader receiptHeader) {
        this.receiptHeader = receiptHeader;
    }

    public BigDecimal getDramount() {
        return dramount;
    }

    public void setDramount(final BigDecimal dramount) {
        this.dramount = dramount;
    }

    public BigDecimal getCramount() {
        return cramount;
    }

    public void setCramount(final BigDecimal cramount) {
        this.cramount = cramount;
    }

    /**
     * Sets both DR and CR amounts to zero.
     */
    public void zeroDrAndCrAmounts() {
        dramount = BigDecimal.ZERO;
        cramount = BigDecimal.ZERO;
    }

    public Long getOrdernumber() {
        return ordernumber;
    }

    public void setOrdernumber(final Long ordernumber) {
        this.ordernumber = ordernumber;
    }

    public CFunction getFunction() {
        return function;
    }

    public void setFunction(final CFunction function) {
        this.function = function;
    }

    /**
     * @return the accounthead
     */
    public CChartOfAccounts getAccounthead() {
        return accounthead;
    }

    /**
     * @param accounthead the accounthead to set
     */
    public void setAccounthead(final CChartOfAccounts accounthead) {
        this.accounthead = accounthead;
    }

    public BigDecimal getCramountToBePaid() {
        return cramountToBePaid;
    }

    public void setCramountToBePaid(final BigDecimal cramountToBePaid) {
        this.cramountToBePaid = cramountToBePaid;
    }

    public Set<AccountPayeeDetail> getAccountPayeeDetails() {
        return accountPayeeDetails;
    }

    public void setAccountPayeeDetails(final Set<AccountPayeeDetail> accountPayeeDetails) {
        this.accountPayeeDetails = accountPayeeDetails;
    }

    public void addAccountPayeeDetail(final AccountPayeeDetail accountPayeeDetails) {
        getAccountPayeeDetails().add(accountPayeeDetails);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public CFinancialYear getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(final CFinancialYear financialYear) {
        this.financialYear = financialYear;
    }

    /**
     * @return the isActualDemand
     */
    public Boolean getIsActualDemand() {
        return isActualDemand;
    }

    /**
     * @param isActualDemand the isActualDemand to set
     */
    public void setIsActualDemand(final Boolean isActualDemand) {
        this.isActualDemand = isActualDemand;
    }

    /**
     * @return the purpose
     */
    public String getPurpose() {
        return purpose;
    }

    /**
     * @param purpose the purpose to set
     */
    public void setPurpose(final String purpose) {
        this.purpose = purpose;
    }
}
