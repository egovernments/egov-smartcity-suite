/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 *           Further, all user interfaces, including but not limited to citizen facing interfaces,
 *           Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *           derived works should carry eGovernments Foundation logo on the top right corner.
 *
 * 	       For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 * 	       For any further queries on attribution, including queries on brand guidelines,
 *           please contact contact@egovernments.org
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
 */

package org.egov.egf.bean.dashboard;

import java.math.BigDecimal;

public class FinancialsDetail {

    private BigDecimal incomeDebitAmount = BigDecimal.ZERO;
    private BigDecimal incomeCreditAmount = BigDecimal.ZERO;
    private BigDecimal expenseDebitAmount = BigDecimal.ZERO;
    private BigDecimal expenseCreditAmount = BigDecimal.ZERO;
    private BigDecimal incomeNetAmount = BigDecimal.ZERO;
    private BigDecimal expenseNetAmount = BigDecimal.ZERO;
    private BigDecimal netAmount = BigDecimal.ZERO;
    private BigDecimal opbAmount = BigDecimal.ZERO;
    private String region;
    private String district;
    private String grade;
    private String ulbName;
    private String fromDate;
    private String toDate;
    private String admZoneName;
    private String admWardName;
    private String fundCode;
    private String departmentCode;
    private String functionCode;
    private String fundSource;
    private String schemeCode;
    private String subschemeCode;
    private String majorCode;
    private String minorCode;
    private String detailedCode;

    public BigDecimal getIncomeDebitAmount() {
        return incomeDebitAmount;
    }

    public void setIncomeDebitAmount(final BigDecimal incomeDebitAmount) {
        this.incomeDebitAmount = incomeDebitAmount;
    }

    public BigDecimal getIncomeCreditAmount() {
        return incomeCreditAmount;
    }

    public void setIncomeCreditAmount(final BigDecimal incomeCreditAmount) {
        this.incomeCreditAmount = incomeCreditAmount;
    }

    public BigDecimal getExpenseDebitAmount() {
        return expenseDebitAmount;
    }

    public void setExpenseDebitAmount(final BigDecimal expenseDebitAmount) {
        this.expenseDebitAmount = expenseDebitAmount;
    }

    public BigDecimal getExpenseCreditAmount() {
        return expenseCreditAmount;
    }

    public void setExpenseCreditAmount(final BigDecimal expenseCreditAmount) {
        this.expenseCreditAmount = expenseCreditAmount;
    }

    public BigDecimal getIncomeNetAmount() {
        return incomeNetAmount;
    }

    public void setIncomeNetAmount(final BigDecimal incomeNetAmount) {
        this.incomeNetAmount = incomeNetAmount;
    }

    public BigDecimal getExpenseNetAmount() {
        return expenseNetAmount;
    }

    public void setExpenseNetAmount(final BigDecimal expenseNetAmount) {
        this.expenseNetAmount = expenseNetAmount;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(final String region) {
        this.region = region;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(final String district) {
        this.district = district;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(final String grade) {
        this.grade = grade;
    }

    public String getUlbName() {
        return ulbName;
    }

    public void setUlbName(final String ulbName) {
        this.ulbName = ulbName;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(final String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(final String toDate) {
        this.toDate = toDate;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(final String fundCode) {
        this.fundCode = fundCode;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(final String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getFunctionCode() {
        return functionCode;
    }

    public void setFunctionCode(final String functionCode) {
        this.functionCode = functionCode;
    }

    public String getFundSource() {
        return fundSource;
    }

    public void setFundSource(final String fundSource) {
        this.fundSource = fundSource;
    }

    public String getSchemeCode() {
        return schemeCode;
    }

    public void setSchemeCode(final String schemeCode) {
        this.schemeCode = schemeCode;
    }

    public String getSubschemeCode() {
        return subschemeCode;
    }

    public void setSubschemeCode(final String subschemeCode) {
        this.subschemeCode = subschemeCode;
    }

    public String getMajorCode() {
        return majorCode;
    }

    public void setMajorCode(final String majorCode) {
        this.majorCode = majorCode;
    }

    public String getMinorCode() {
        return minorCode;
    }

    public void setMinorCode(final String minorCode) {
        this.minorCode = minorCode;
    }

    public String getDetailedCode() {
        return detailedCode;
    }

    public void setDetailedCode(final String detailedCode) {
        this.detailedCode = detailedCode;
    }

    public BigDecimal getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(final BigDecimal netAmount) {
        this.netAmount = netAmount;
    }

    public BigDecimal getOpbAmount() {
        return opbAmount;
    }

    public void setOpbAmount(final BigDecimal opbAmount) {
        this.opbAmount = opbAmount;
    }

    public String getAdmZoneName() {
        return admZoneName;
    }

    public void setAdmZoneName(final String admZoneName) {
        this.admZoneName = admZoneName;
    }

    public String getAdmWardName() {
        return admWardName;
    }

    public void setAdmWardName(final String admWardName) {
        this.admWardName = admWardName;
    }
}
