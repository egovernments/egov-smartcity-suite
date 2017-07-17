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

import org.egov.infra.utils.StringUtils;

public class FinancialsRatioAnalysisResponse {

    private String region = StringUtils.EMPTY;
    private String district = StringUtils.EMPTY;
    private String grade = StringUtils.EMPTY;
    private String ulbCode = StringUtils.EMPTY;
    private String ulbName = StringUtils.EMPTY;
    private String ulbGrade = StringUtils.EMPTY;
    private String admZoneName = StringUtils.EMPTY;
    private String admWardName = StringUtils.EMPTY;
    private String fundCode = StringUtils.EMPTY;
    private String fundName = StringUtils.EMPTY;
    private String departmentCode = StringUtils.EMPTY;
    private String departmentName = StringUtils.EMPTY;
    private String functionCode = StringUtils.EMPTY;
    private String functionName = StringUtils.EMPTY;
    private String financialYear = StringUtils.EMPTY;
    private String month = StringUtils.EMPTY;
    private Double taxRevenueToTotalIncomeRatio = 0.0;
    private Double assignedRevenuesToTotalIncomeRatio = 0.0;
    private Double revenueGrantsToTotalIncomeRatio = 0.0;
    private Double rentalIncomeToTotalIncomeRatio = 0.0;
    private Double feeUserChargesToTotalIncomeRatio = 0.0;
    private Double incomeRatio = 0.0;
    private Double establishmentExpensesToTotalReRatio = 0.0;
    private Double administrativeExpensesToTotalReRatio = 0.0;
    private Double omExpensesToTotalReRatio = 0.0;
    private Double expenseRatio = 0.0;
    private Double capitalRatio = 0.0;


    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getUlbCode() {
        return ulbCode;
    }

    public void setUlbCode(String ulbCode) {
        this.ulbCode = ulbCode;
    }

    public String getUlbName() {
        return ulbName;
    }

    public void setUlbName(String ulbName) {
        this.ulbName = ulbName;
    }

    public String getAdmZoneName() {
        return admZoneName;
    }

    public void setAdmZoneName(String admZoneName) {
        this.admZoneName = admZoneName;
    }

    public String getAdmWardName() {
        return admWardName;
    }

    public void setAdmWardName(String admWardName) {
        this.admWardName = admWardName;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getFunctionCode() {
        return functionCode;
    }

    public void setFunctionCode(String functionCode) {
        this.functionCode = functionCode;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public Double getTaxRevenueToTotalIncomeRatio() {
        return taxRevenueToTotalIncomeRatio;
    }

    public void setTaxRevenueToTotalIncomeRatio(Double taxRevenueToTotalIncomeRatio) {
        this.taxRevenueToTotalIncomeRatio = taxRevenueToTotalIncomeRatio;
    }

    public Double getAssignedRevenuesToTotalIncomeRatio() {
        return assignedRevenuesToTotalIncomeRatio;
    }

    public void setAssignedRevenuesToTotalIncomeRatio(Double assignedRevenuesToTotalIncomeRatio) {
        this.assignedRevenuesToTotalIncomeRatio = assignedRevenuesToTotalIncomeRatio;
    }

    public Double getRevenueGrantsToTotalIncomeRatio() {
        return revenueGrantsToTotalIncomeRatio;
    }

    public void setRevenueGrantsToTotalIncomeRatio(Double revenueGrantsToTotalIncomeRatio) {
        this.revenueGrantsToTotalIncomeRatio = revenueGrantsToTotalIncomeRatio;
    }

    public Double getRentalIncomeToTotalIncomeRatio() {
        return rentalIncomeToTotalIncomeRatio;
    }

    public void setRentalIncomeToTotalIncomeRatio(Double rentalIncomeToTotalIncomeRatio) {
        this.rentalIncomeToTotalIncomeRatio = rentalIncomeToTotalIncomeRatio;
    }

    public Double getFeeUserChargesToTotalIncomeRatio() {
        return feeUserChargesToTotalIncomeRatio;
    }

    public void setFeeUserChargesToTotalIncomeRatio(Double feeUserChargesToTotalIncomeRatio) {
        this.feeUserChargesToTotalIncomeRatio = feeUserChargesToTotalIncomeRatio;
    }

    public Double getIncomeRatio() {
        return incomeRatio;
    }

    public void setIncomeRatio(Double incomeRatio) {
        this.incomeRatio = incomeRatio;
    }

    public Double getEstablishmentExpensesToTotalReRatio() {
        return establishmentExpensesToTotalReRatio;
    }

    public void setEstablishmentExpensesToTotalReRatio(Double establishmentExpensesToTotalReRatio) {
        this.establishmentExpensesToTotalReRatio = establishmentExpensesToTotalReRatio;
    }

    public Double getAdministrativeExpensesToTotalReRatio() {
        return administrativeExpensesToTotalReRatio;
    }

    public void setAdministrativeExpensesToTotalReRatio(Double administrativeExpensesToTotalReRatio) {
        this.administrativeExpensesToTotalReRatio = administrativeExpensesToTotalReRatio;
    }

    public Double getOmExpensesToTotalReRatio() {
        return omExpensesToTotalReRatio;
    }

    public void setOmExpensesToTotalReRatio(Double omExpensesToTotalReRatio) {
        this.omExpensesToTotalReRatio = omExpensesToTotalReRatio;
    }

    public Double getCapitalRatio() {
        return capitalRatio;
    }

    public void setCapitalRatio(Double capitalRatio) {
        this.capitalRatio = capitalRatio;
    }

    public String getUlbGrade() {
        return ulbGrade;
    }

    public void setUlbGrade(String ulbGrade) {
        this.ulbGrade = ulbGrade;
    }

    public String getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(String financialYear) {
        this.financialYear = financialYear;
    }

    public Double getExpenseRatio() {
        return expenseRatio;
    }

    public void setExpenseRatio(Double expenseRatio) {
        this.expenseRatio = expenseRatio;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
