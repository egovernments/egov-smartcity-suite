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

package org.egov.egf.bean.dashboard;

import org.egov.infra.utils.StringUtils;

import java.math.BigDecimal;

public class FinancialsBudgetDetailResponse {

    private String region = StringUtils.EMPTY;
    private String district = StringUtils.EMPTY;
    private String grade = StringUtils.EMPTY;
    private String ulbCode = StringUtils.EMPTY;
    private String ulbName = StringUtils.EMPTY;
    private String fromDate = StringUtils.EMPTY;
    private String toDate = StringUtils.EMPTY;
    private String admZoneName = StringUtils.EMPTY;
    private String admWardName = StringUtils.EMPTY;
    private String fundCode = StringUtils.EMPTY;
    private String fundName = StringUtils.EMPTY;
    private String departmentCode = StringUtils.EMPTY;
    private String departmentName = StringUtils.EMPTY;
    private String functionCode = StringUtils.EMPTY;
    private String functionName = StringUtils.EMPTY;
    private String fundSource = StringUtils.EMPTY;
    private String schemeCode = StringUtils.EMPTY;
    private String schemeName = StringUtils.EMPTY;
    private String subschemeCode = StringUtils.EMPTY;
    private String subschemeName = StringUtils.EMPTY;
    private String majorCode = StringUtils.EMPTY;
    private String minorCode = StringUtils.EMPTY;
    private String detailedCode = StringUtils.EMPTY;
    private String majorCodeDescription = StringUtils.EMPTY;
    private String minorCodeDescription = StringUtils.EMPTY;
    private String detailedCodeDescription = StringUtils.EMPTY;
    private BigDecimal budgetApprovedAmount = BigDecimal.ZERO;
    private BigDecimal reAppropriationAmount = BigDecimal.ZERO;
    private BigDecimal allocatedBudget = BigDecimal.ZERO;
    private BigDecimal actualAmount = BigDecimal.ZERO;
    private BigDecimal previouYearActualAmount = BigDecimal.ZERO;
    private BigDecimal committedExpenditure = BigDecimal.ZERO;
    private BigDecimal budgetUsageVariance = BigDecimal.ZERO;
    private Double budgetVariance = 0.0;


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

    public String getUlbCode() {
        return ulbCode;
    }

    public void setUlbCode(final String ulbCode) {
        this.ulbCode = ulbCode;
    }

    public BigDecimal getReAppropriationAmount() {
        return reAppropriationAmount;
    }

    public void setReAppropriationAmount(final BigDecimal reAppropriationAmount) {
        this.reAppropriationAmount = reAppropriationAmount;
    }

    public BigDecimal getAllocatedBudget() {
        return allocatedBudget;
    }

    public void setAllocatedBudget(BigDecimal allocatedBudget) {
        this.allocatedBudget = allocatedBudget;
    }

    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(final BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    public BigDecimal getPreviouYearActualAmount() {
        return previouYearActualAmount;
    }

    public void setPreviouYearActualAmount(final BigDecimal previouYearActualAmount) {
        this.previouYearActualAmount = previouYearActualAmount;
    }

    public BigDecimal getCommittedExpenditure() {
        return committedExpenditure;
    }

    public void setCommittedExpenditure(final BigDecimal committedExpenditure) {
        this.committedExpenditure = committedExpenditure;
    }


    public BigDecimal getBudgetApprovedAmount() {
        return budgetApprovedAmount;
    }

    public void setBudgetApprovedAmount(final BigDecimal budgetApprovedAmount) {
        this.budgetApprovedAmount = budgetApprovedAmount;
    }

    public String getMajorCodeDescription() {
        return majorCodeDescription;
    }

    public void setMajorCodeDescription(String majorCodeDescription) {
        this.majorCodeDescription = majorCodeDescription;
    }

    public String getMinorCodeDescription() {
        return minorCodeDescription;
    }

    public void setMinorCodeDescription(String minorCodeDescription) {
        this.minorCodeDescription = minorCodeDescription;
    }

    public String getDetailedCodeDescription() {
        return detailedCodeDescription;
    }

    public void setDetailedCodeDescription(String detailedCodeDescription) {
        this.detailedCodeDescription = detailedCodeDescription;
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getSchemeName() {
        return schemeName;
    }

    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
    }

    public String getSubschemeName() {
        return subschemeName;
    }

    public void setSubschemeName(String subschemeName) {
        this.subschemeName = subschemeName;
    }

    public BigDecimal getBudgetUsageVariance() {
        return budgetUsageVariance;
    }

    public void setBudgetUsageVariance(BigDecimal budgetUsageVariance) {
        this.budgetUsageVariance = budgetUsageVariance;
    }

    public Double getBudgetVariance() {
        return budgetVariance;
    }

    public void setBudgetVariance(Double budgetVariance) {
        this.budgetVariance = budgetVariance;
    }
}
