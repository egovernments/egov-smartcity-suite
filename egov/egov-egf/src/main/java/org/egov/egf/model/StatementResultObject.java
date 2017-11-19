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

public class StatementResultObject {
    private static final BigDecimal NEGATIVE = new BigDecimal(-1);
    BigDecimal amount;
    Integer fundId;
    String fundCode;
    String glCode = "";
    Character type;
    String scheduleNumber = "";
    String scheduleName = "";
    String majorCode;
    BigDecimal budgetAmount;

    public BigDecimal getBudgetAmount() {
        return budgetAmount;
    }

    public void setBudgetAmount(final BigDecimal budgetAmount) {
        this.budgetAmount = budgetAmount;
    }

    public String getScheduleNumber() {
        return scheduleNumber;
    }

    public void setScheduleNumber(final String scheduleNumber) {
        this.scheduleNumber = scheduleNumber;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(final String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getFundId() {
        return fundId;
    }

    public void setFundId(final BigDecimal fundId) {
        this.fundId = Integer.valueOf(fundId.intValue());
    }

    public String getGlCode() {
        return glCode;
    }

    public void setGlCode(final String glCode) {
        this.glCode = glCode;
    }

    public Character getType() {
        return type;
    }

    public void setType(final Character type) {
        this.type = type;
    }

    public boolean isLiability() {
        return type != null ? "L".equalsIgnoreCase(type.toString()) : false;
    }

    public boolean isIncome() {
        return type != null ? "I".equalsIgnoreCase(type.toString()) : false;
    }

    public void negateAmount() {
        amount = amount.multiply(NEGATIVE);
    }

    public String getMajorCode() {
        return majorCode;
    }

    public void setMajorCode(final String majorCode) {
        this.majorCode = majorCode;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(final String fundCode) {
        this.fundCode = fundCode;
    }
}
