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
package org.egov.egf.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class StatementEntry {
    private String glCode;
    private String accountName;
    private String scheduleNo;
    // FundCode is added for RP report
    private String fundCode;
    private BigDecimal previousYearTotal = BigDecimal.ZERO;
    private BigDecimal currentYearTotal = BigDecimal.ZERO;
    private Map<String, BigDecimal> fundWiseAmount = new HashMap<String, BigDecimal>();
    private boolean displayBold = false;

    public boolean isDisplayBold() {
        return displayBold;
    }

    public void setDisplayBold(final boolean displayBold) {
        this.displayBold = displayBold;
    }

    public StatementEntry() {
    }

    public void setGlCode(final String accountCode) {
        glCode = accountCode;
    }

    public void setAccountName(final String accountName) {
        this.accountName = accountName;
    }

    public void setScheduleNo(final String scheduleNo) {
        this.scheduleNo = scheduleNo;
    }

    public void setPreviousYearTotal(final BigDecimal previousYearTotal) {
        this.previousYearTotal = previousYearTotal;
    }

    public void setCurrentYearTotal(final BigDecimal currentYearTotal) {
        this.currentYearTotal = currentYearTotal;
    }

    public void setFundWiseAmount(final Map<String, BigDecimal> fundWiseAmount) {
        this.fundWiseAmount = fundWiseAmount;
    }

    public StatementEntry(final String accountCode, final String accountName, final String scheduleNo,
            final BigDecimal previousYearTotal,
            final BigDecimal currentYearTotal, final boolean displayBold) {
        glCode = accountCode;
        this.accountName = accountName;
        this.scheduleNo = scheduleNo;
        this.previousYearTotal = previousYearTotal;
        this.currentYearTotal = currentYearTotal;
        this.displayBold = displayBold;
    }

    public StatementEntry(final String accountCode, final String accountName) {
        glCode = accountCode;
        this.accountName = accountName;
    }

    public StatementEntry(final String accountName, final Map<String, BigDecimal> fundWiseAmount, final boolean displayBold) {
        this.accountName = accountName;
        this.fundWiseAmount = fundWiseAmount;
        this.displayBold = displayBold;
    }

    public String getGlCode() {
        return glCode;
    }

    public Map<String, BigDecimal> getFundWiseAmount() {
        return fundWiseAmount;
    }

    public void putFundWiseAmount(final String fundName, final BigDecimal amount) {
        fundWiseAmount.put(fundName, amount);
    }

    public String getAccountName() {
        return accountName;
    }

    public String getScheduleNo() {
        return scheduleNo;
    }

    public BigDecimal getPreviousYearTotal() {
        return previousYearTotal;
    }

    public BigDecimal getCurrentYearTotal() {
        return currentYearTotal;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(final String fundCode) {
        this.fundCode = fundCode;
    }

}
