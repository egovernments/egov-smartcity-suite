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
import org.egov.commons.Fund;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DepartmentwiseExpenditureReport {

    private String departmentName;
    private Integer fundId;
    private String month;
    private Fund fund;
    private Date concurrenceDate;
    private Date fromDate;
    private Date toDate;
    private Long financialYearId;
    private CFinancialYear finyearObj = null;
    private String reportType;
    private String period;
    private String assetCode;
    private Date previousYearConcurrenceGivenUptoDate;
    private Date currentYearConcurrenceGivenUptoDate;
    private Date previousYearConcurrenceGivenTillDate;
    private Set<String> concurrenceDateSet = new LinkedHashSet<String>();
    private Set<String> previousConcurrenceDateSet = new LinkedHashSet<String>();
    private Map<String, Boolean> rowToBeRemoved = new HashMap<String, Boolean>();
    private String exportType;
    // private Map<String,DepartmentwiseExpenditureResult> currentyearDepartmentMap =new
    // LinkedHashMap<String,DepartmentwiseExpenditureResult>();
    private List<DepartmentwiseExpenditureResult> currentyearDepartmentList = new LinkedList<DepartmentwiseExpenditureResult>();
    private List<DepartmentwiseExpenditureResult> previousyearDepartmentList = new LinkedList<DepartmentwiseExpenditureResult>();

    private BigDecimal concurrenceAmount;

    public void reset() {
        departmentName = null;
        fundId = null;
        month = null;
        fromDate = null;
        toDate = null;
        reportType = null;
        period = null;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public String getMonth() {
        return month;
    }

    public Date getConcurrenceDate() {
        return concurrenceDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public String getReportType() {
        return reportType;
    }

    public BigDecimal getConcurrenceAmount() {
        return concurrenceAmount;
    }

    public void setDepartmentName(final String departmentName) {
        this.departmentName = departmentName;
    }

    public void setMonth(final String month) {
        this.month = month;
    }

    public void setConcurrenceDate(final Date concurrenceDate) {
        this.concurrenceDate = concurrenceDate;
    }

    public void setFromDate(final Date fromDate) {
        this.fromDate = fromDate;
    }

    public void setToDate(final Date toDate) {
        this.toDate = toDate;
    }

    public void setReportType(final String reportType) {
        this.reportType = reportType;
    }

    public void setConcurrenceAmount(final BigDecimal concurrenceAmount) {
        this.concurrenceAmount = concurrenceAmount;
    }

    public List<DepartmentwiseExpenditureResult> getCurrentyearDepartmentList() {
        return currentyearDepartmentList;
    }

    public void setCurrentyearDepartmentList(
            final List<DepartmentwiseExpenditureResult> currentyearDepartmentList) {
        this.currentyearDepartmentList = currentyearDepartmentList;
    }

    public Set<String> getConcurrenceDateSet() {
        return concurrenceDateSet;
    }

    public void setConcurrenceDateSet(final Set<String> concurrenceDateSet) {
        this.concurrenceDateSet = concurrenceDateSet;
    }

    public boolean containsDepartmentInResultList(final String department, final List<DepartmentwiseExpenditureResult> resList) {
        if (department == null)
            return false;
        for (final DepartmentwiseExpenditureResult entry : resList)
            if (department.equalsIgnoreCase(entry.getDepartmentNm()))
                return true;
        return false;
    }

    public Integer getFundId() {
        return fundId;
    }

    public Long getFinancialYearId() {
        return financialYearId;
    }

    public void setFundId(final Integer fundId) {
        this.fundId = fundId;
    }

    public void setFinancialYearId(final Long financialYearId) {
        this.financialYearId = financialYearId;
    }

    public CFinancialYear getFinyearObj() {
        return finyearObj;
    }

    public void setFinyearObj(final CFinancialYear finyearObj) {
        this.finyearObj = finyearObj;
    }

    public String getExportType() {
        return exportType;
    }

    public void setExportType(final String exportType) {
        this.exportType = exportType;
    }

    public void addDepartmentToResultSet(final DepartmentwiseExpenditureResult entry) {
        // if(this.getFromDate())
        if (getPeriod().equalsIgnoreCase("current"))
            getCurrentyearDepartmentList().add(entry);
        else if (getPeriod().equalsIgnoreCase("previous"))
            getPreviousyearDepartmentList().add(entry);
    }

    public Date getRestrictedDepartmentDate() {
        final Calendar date = Calendar.getInstance();
        date.set(Calendar.YEAR, 2013);
        date.set(Calendar.MONTH, 02);
        date.set(Calendar.DATE, 31);
        // date.set(Calendar.DAY_OF_MONTH, date.getActualMaximum(Calendar.DAY_OF_MONTH));
        return date.getTime();
    }

    public Date getPreviousDateFor(final Date date) {

        final GregorianCalendar previousDate = new GregorianCalendar();
        previousDate.setTime(date);
        final int prevDt = previousDate.get(Calendar.DATE) - 1;
        previousDate.set(Calendar.DATE, prevDt);
        return previousDate.getTime();
    }

    public void addDepartmentwiseExistingEntry(final DepartmentwiseExpenditureResult entry) {
        for (final DepartmentwiseExpenditureResult obj : getCurrentyearDepartmentList())
            if (obj.getDepartmentNm().equals(entry.getDepartmentNm())) {

            }

    }

    public List<DepartmentwiseExpenditureResult> getPreviousyearDepartmentList() {
        return previousyearDepartmentList;
    }

    public void setPreviousyearDepartmentList(
            final List<DepartmentwiseExpenditureResult> previousyearDepartmentList) {
        this.previousyearDepartmentList = previousyearDepartmentList;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(final String period) {
        this.period = period;
    }

    public Set<String> getPreviousConcurrenceDateSet() {
        return previousConcurrenceDateSet;
    }

    public void setPreviousConcurrenceDateSet(final Set<String> previousConcurrenceDateSet) {
        this.previousConcurrenceDateSet = previousConcurrenceDateSet;
    }

    public Date getStartOfMonth() {
        String year = "";
        final Calendar date = Calendar.getInstance();
        if (month.equals("01") || month.equals("02") || month.equals("03"))
            year = getFinyearObj().getEndingDate().toString().substring(0, 4);
        else
            year = getFinyearObj().getStartingDate().toString().substring(0, 4);

        date.set(Calendar.YEAR, Integer.parseInt(year));
        date.set(Calendar.MONTH, Integer.parseInt(month));
        date.set(Calendar.DATE, 1);
        date.set(Calendar.DAY_OF_MONTH, date.getActualMaximum(Calendar.DAY_OF_MONTH));
        // date.setTime(date);
        return date.getTime();
    }

    public Date getEndMonth() {
        String year = "";
        final Calendar date = Calendar.getInstance();
        if (month.equals("01") || month.equals("02") || month.equals("03"))
            year = getFinyearObj().getEndingDate().toString().substring(0, 4);
        else
            year = getFinyearObj().getStartingDate().toString().substring(0, 4);

        date.set(Calendar.YEAR, Integer.parseInt(year));
        date.set(Calendar.MONTH, Integer.parseInt(month));
        date.set(Calendar.DATE, 1);
        date.set(Calendar.DAY_OF_MONTH, date.getActualMaximum(Calendar.DAY_OF_MONTH));
        // date.setTime(date);
        return date.getTime();
    }

    public Date getPreviousYearConcurrenceGivenUptoDate() {
        return previousYearConcurrenceGivenUptoDate;
    }

    public Date getPreviousYearConcurrenceGivenTillDate() {
        return previousYearConcurrenceGivenTillDate;
    }

    public void setPreviousYearConcurrenceGivenUptoDate(
            final Date previousYearConcurrenceGivenUptoDate) {
        this.previousYearConcurrenceGivenUptoDate = previousYearConcurrenceGivenUptoDate;
    }

    public void setPreviousYearConcurrenceGivenTillDate(
            final Date previousYearConcurrenceGivenTillDate) {
        this.previousYearConcurrenceGivenTillDate = previousYearConcurrenceGivenTillDate;
    }

    public Date getCurrentYearConcurrenceGivenUptoDate() {
        return currentYearConcurrenceGivenUptoDate;
    }

    public void setCurrentYearConcurrenceGivenUptoDate(
            final Date currentYearConcurrenceGivenUptoDate) {
        this.currentYearConcurrenceGivenUptoDate = currentYearConcurrenceGivenUptoDate;
    }

    public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(final String assetCode) {
        this.assetCode = assetCode;
    }

    public Fund getFund() {
        return fund;
    }

    public void setFund(final Fund fund) {
        this.fund = fund;
    }

    public Map<String, Boolean> getRowToBeRemoved() {
        return rowToBeRemoved;
    }

    public void setRowToBeRemoved(final Map<String, Boolean> rowToBeRemoved) {
        this.rowToBeRemoved = rowToBeRemoved;
    }

}
