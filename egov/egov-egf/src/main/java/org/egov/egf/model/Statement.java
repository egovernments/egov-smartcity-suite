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

import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Statement {
    private String period;
    private CFinancialYear financialYear;
    private Date asOndate;
    private Date fromDate;
    private Date toDate;
    private String currency;
    private BigDecimal currencyInAmount;
    private Department department;
    private Functionary functionary;
    private CFunction function;
    private Boundary field;
    private Fund fund;
    private List<Fund> fundList = new ArrayList<Fund>();
    private List<IEStatementEntry> ieEntries = new ArrayList<IEStatementEntry>();
    private List<StatementEntry> entries = new ArrayList<StatementEntry>();

    public void setEntries(final List<StatementEntry> entries) {
        this.entries = entries;
    }

    public List<IEStatementEntry> getIeEntries() {
        return ieEntries;
    }

    public void setIeEntries(final List<IEStatementEntry> ieEntries) {
        this.ieEntries = ieEntries;
    }

    public void setPeriod(final String period) {
        this.period = period;
    }

    public void setFinancialYear(final CFinancialYear financialYear) {
        this.financialYear = financialYear;
    }

    public void setAsOndate(final Date asOndate) {
        this.asOndate = asOndate;
    }

    public void setCurrency(final String currency) {
        this.currency = currency;
        if (this.currency.equalsIgnoreCase("rupees"))
            currencyInAmount = new BigDecimal(1);
        if (this.currency.equalsIgnoreCase("thousands"))
            currencyInAmount = new BigDecimal(1000);
        if (this.currency.equalsIgnoreCase("lakhs"))
            currencyInAmount = new BigDecimal(100000);
    }

    public void setDepartment(final Department department) {
        this.department = department;
    }

    public void setFunctionary(final Functionary functionary) {
        this.functionary = functionary;
    }

    public void setFunction(final CFunction function) {
        this.function = function;
    }

    public void setField(final Boundary field) {
        this.field = field;
    }

    public String getPeriod() {
        return period;
    }

    public CFinancialYear getFinancialYear() {
        return financialYear;
    }

    public Date getAsOndate() {
        return asOndate;
    }

    public String getCurrency() {
        return currency;
    }

    public Department getDepartment() {
        return department;
    }

    public Functionary getFunctionary() {
        return functionary;
    }

    public CFunction getFunction() {
        return function;
    }

    public Boundary getField() {
        return field;
    }

    public List<Fund> getFunds() {
        return fundList;
    }

    public Fund getFund() {
        return fund;
    }

    public void setFund(final Fund fund) {
        this.fund = fund;
    }

    public void setFunds(final List<Fund> list) {
        fundList = list;
    }

    public void add(final StatementEntry entry) {
        entries.add(entry);
    }

    public void addIE(final IEStatementEntry entry) {
        ieEntries.add(entry);
    }

    public List<StatementEntry> getEntries() {
        return entries;
    }

    public int size() {
        return entries.size();
    }

    public int sizeIE() {
        return ieEntries.size();
    }

    public StatementEntry get(final int index) {
        return entries.get(index);
    }

    public void addAll(final Statement balanceSheet) {
        entries.addAll(balanceSheet.getEntries());
    }

    public void addAllIE(final Statement balanceSheet) {
        ieEntries.addAll(balanceSheet.getIeEntries());
    }

    public IEStatementEntry getIE(final int index) {
        return ieEntries.get(index);
    }

    public BigDecimal getDivisor() {
        if ("Thousands".equalsIgnoreCase(currency))
            return new BigDecimal(1000);
        if ("Lakhs".equalsIgnoreCase(currency))
            return new BigDecimal(100000);
        return BigDecimal.ONE;
    }

    public boolean containsStatementEntryScheduleNo(final String scheduleNo) {
        if (scheduleNo == null)
            return false;
        for (final StatementEntry StatementEntryObj : getEntries())
            if (StatementEntryObj.getScheduleNo() != null && scheduleNo.equals(StatementEntryObj.getScheduleNo()))
                return true;
        return false;
    }

    public boolean containsStatementEntryOfDetailedCode(final String glcode) {
        if (glcode == null)
            return false;
        for (final StatementEntry StatementEntryObj : getEntries())
            if (StatementEntryObj.getGlCode() != null && glcode.equals(StatementEntryObj.getGlCode()))
                return true;
        return false;
    }

    public boolean containsBalanceSheetEntry(final String glCode) {
        if (glCode == null)
            return false;
        for (final StatementEntry balanceSheetEntry : getEntries())
            if (balanceSheetEntry.getGlCode() != null && glCode.equals(balanceSheetEntry.getGlCode()))
                return true;
        return false;
    }

    public boolean containsIEStatementEntry(final String glCode) {
        if (glCode == null)
            return false;
        for (final IEStatementEntry balanceSheetEntry : getIeEntries())
            if (balanceSheetEntry.getGlCode() != null && glCode.equals(balanceSheetEntry.getGlCode()))
                return true;
        return false;
    }

    public boolean containsMajorCodeEntry(final String majorcode) {
        if (majorcode == null)
            return false;
        for (final IEStatementEntry balanceSheetEntry : getIeEntries())
            if (balanceSheetEntry.getMajorCode() != null && majorcode.equals(balanceSheetEntry.getMajorCode()))
                return true;
        return false;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(final Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(final Date toDate) {
        this.toDate = toDate;
    }

    public BigDecimal getCurrencyInAmount() {
        return currencyInAmount;
    }

}
