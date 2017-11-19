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

public class FinancialsDetailsRequest {

    private String region;
    private String district;
    private String grade;
    private String ulbCode;
    private String ulbName;
    private String fromDate;
    private String toDate;
    private String admZone;
    private String admWard;
    private String fundCode;
    private String departmentCode;
    private String functionCode;
    private String fundSource;
    private String schemeCode;
    private String subschemeCode;
    private String majorCode = "";
    private String minorCode = "";
    private String detailedCode = "";
    private String aggregationLevel;
    private String currentFinancialYear;
    private String lastFinancialYear;
    private String financialYear;

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

    public String getUlbCode() {
        return ulbCode;
    }

    public void setUlbCode(final String ulbCode) {
        this.ulbCode = ulbCode;
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

    public String getAdmZone() {
        return admZone;
    }

    public void setAdmZone(final String admZone) {
        this.admZone = admZone;
    }

    public String getAdmWard() {
        return admWard;
    }

    public void setAdmWard(final String admWard) {
        this.admWard = admWard;
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

    public String getAggregationLevel() {
        return aggregationLevel;
    }

    public void setAggregationLevel(final String aggregationLevel) {
        this.aggregationLevel = aggregationLevel;
    }

    public String getCurrentFinancialYear() {
        return currentFinancialYear;
    }

    public void setCurrentFinancialYear(final String currentFinancialYear) {
        this.currentFinancialYear = currentFinancialYear;
    }

    public String getUlbName() {
        return ulbName;
    }

    public void setUlbName(final String ulbName) {
        this.ulbName = ulbName;
    }

    public String getLastFinancialYear() {
        return lastFinancialYear;
    }

    public void setLastFinancialYear(final String lastFinancialYear) {
        this.lastFinancialYear = lastFinancialYear;
    }

    public String getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(String financialYear) {
        this.financialYear = financialYear;
    }
}
