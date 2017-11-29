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

public class CommonReportBean {

    private Boolean isMajor;
    private String accCode;
    private String name;
    private String deptName;
    private Integer slNo;

    public Integer getSlNo() {
        return slNo;
    }

    public void setSlNo(final Integer slNo) {
        this.slNo = slNo;
    }

    // Name of the schedule in schedule mapping
    private String schedule;
    private Long FIEscheduleId;
    private BigDecimal computedBalance;

    public CommonReportBean(final String accCode, final String name, final BigDecimal beSum,
            final BigDecimal reSum, final BigDecimal beAppSum, final BigDecimal reAppSum,
            final BigDecimal amountSum) {
        this.accCode = accCode;
        this.name = name;
        beAmount = beSum;
        reAmount = reSum;
        beAppAmount = beAppSum;
        reAppAmount = reAppSum;
        amount = amountSum;
        isMajor = false;
    }

    public CommonReportBean(final String accCode, final String name, final BigDecimal beSum,
            final BigDecimal reSum, final BigDecimal beAppSum, final BigDecimal reAppSum,
            final BigDecimal amountSum, final BigDecimal pyAmountSum) {
        this.accCode = accCode;
        this.name = name;
        beAmount = beSum;
        reAmount = reSum;
        beAppAmount = beAppSum;
        reAppAmount = reAppSum;
        amount = amountSum;
        pyAmount = pyAmountSum;
        isMajor = false;
    }

    public CommonReportBean() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Long getFIEscheduleId() {
        return FIEscheduleId;
    }

    public void setFIEscheduleId(final Long escheduleId) {
        FIEscheduleId = escheduleId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(final String deptName) {
        this.deptName = deptName;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(final String schedule) {
        this.schedule = schedule;
    }

    public BigDecimal getBeAmount() {
        if (beAmount == null)
            return BigDecimal.ZERO;
        else
            return beAmount;
    }

    public void setBeAmount(final BigDecimal beAmount) {
        this.beAmount = beAmount;
    }

    public BigDecimal getReAmount() {
        if (reAmount == null)
            return BigDecimal.ZERO;
        else
            return reAmount;
    }

    public void setReAmount(final BigDecimal reAmount) {
        this.reAmount = reAmount;
    }

    public BigDecimal getReAppAmount() {
        if (reAppAmount == null)
            return BigDecimal.ZERO;
        else
            return reAppAmount;
    }

    public void setReAppAmount(final BigDecimal reAppAmount) {
        this.reAppAmount = reAppAmount;
    }

    // Used for GL amount
    private BigDecimal amount;
    private BigDecimal pyAmount;
    private BigDecimal beAmount;
    private BigDecimal reAmount;
    private BigDecimal beAppAmount;

    public BigDecimal getBeAppAmount() {
        if (beAppAmount == null)
            return BigDecimal.ZERO;
        else
            return beAppAmount;
    }

    public void setBeAppAmount(final BigDecimal beAppAmount) {
        this.beAppAmount = beAppAmount;
    }

    private BigDecimal reAppAmount;

    public Boolean getIsMajor() {
        return isMajor;
    }

    public void setIsMajor(final Boolean isMajor) {
        this.isMajor = isMajor;
    }

    public String getAccCode() {
        return accCode;
    }

    public void setAccCode(final String accCode) {
        this.accCode = accCode;
    }

    public BigDecimal getAmount() {
        if (amount == null)
            return BigDecimal.ZERO;
        else
            return amount;
    }

    public void setAmount(final BigDecimal amount) {

        this.amount = amount;
    }

    public boolean isZero()
    {
        if ((beAmount == null || beAmount.compareTo(BigDecimal.ZERO) == 0)
                && (reAmount == null || reAmount.compareTo(BigDecimal.ZERO) == 0)
                && (beAppAmount == null || beAppAmount.compareTo(BigDecimal.ZERO) == 0)
                && (reAppAmount == null || reAppAmount.compareTo(BigDecimal.ZERO) == 0)
                && (amount == null || amount.compareTo(BigDecimal.ZERO) == 0))
            return true;
        else
            return false;
    }

    @Override
    public String toString()
    {
        return "" + isMajor + "\t" + deptName + "\t" + accCode + "\t" + name + "\t" + beAmount + "\t" + beAppAmount + "\t"
                + reAmount + "\t" + reAppAmount + "\t" + amount + "\t" + getComputedBalance();
    }

    public BigDecimal getComputedBalance()
    {

        if (reAmount != null && reAmount.compareTo(BigDecimal.ZERO) != 0)
        {
            computedBalance = reAmount;
            if (reAppAmount != null)
                computedBalance = computedBalance.add(reAppAmount);
            if (amount != null)
                computedBalance = computedBalance.subtract(amount);
        } else if (beAmount != null && beAmount.compareTo(BigDecimal.ZERO) != 0)
        {
            computedBalance = beAmount;
            if (beAppAmount != null)
                computedBalance = computedBalance.add(beAppAmount);
            if (amount != null)
                computedBalance = computedBalance.subtract(amount);
        } else
        {
            computedBalance = BigDecimal.ZERO;
            if (amount != null)
                computedBalance = computedBalance.subtract(amount);
        }

        return computedBalance;
    }

    public boolean isZeroForIncome() {
        if ((beAmount == null || beAmount.compareTo(BigDecimal.ZERO) == 0)
                && (amount == null || amount.compareTo(BigDecimal.ZERO) == 0))
            return true;
        else
            return false;

    }

    public BigDecimal getPyAmount() {
        if (pyAmount == null)
            return BigDecimal.ZERO;
        else
            return pyAmount;
    }

    public void setPyAmount(final BigDecimal pyAmount) {
        this.pyAmount = pyAmount;
    }
}
