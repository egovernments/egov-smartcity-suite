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

import java.util.Map;

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
    private String fromDate = StringUtils.EMPTY;
    private String toDate = StringUtils.EMPTY;
    private String month = StringUtils.EMPTY;
    private Double cyTaxRevenueToTotalIncomeRatio = 0.0;
    private Double lyTaxRevenueToTotalIncomeRatio = 0.0;
    private Double cyAssignedRevenuesToTotalIncomeRatio = 0.0;
    private Double lyAssignedRevenuesToTotalIncomeRatio = 0.0;
    private Double cyRevenueGrantsToTotalIncomeRatio = 0.0;
    private Double lyRevenueGrantsToTotalIncomeRatio = 0.0;
    private Double cyRentalIncomeToTotalIncomeRatio = 0.0;
    private Double lyRentalIncomeToTotalIncomeRatio = 0.0;
    private Double cyFeeUserChargesToTotalIncomeRatio = 0.0;
    private Double lyFeeUserChargesToTotalIncomeRatio = 0.0;
    private Double cyIncomeRatio = 0.0;
    private Double lyIncomeRatio = 0.0;
    private Double cyEstablishmentExpensesToTotalReRatio = 0.0;
    private Double lyEstablishmentExpensesToTotalReRatio = 0.0;
    private Double cyAdministrativeExpensesToTotalReRatio = 0.0;
    private Double lyAdministrativeExpensesToTotalReRatio = 0.0;
    private Double cyOmExpensesToTotalReRatio = 0.0;
    private Double lyOmExpensesToTotalReRatio = 0.0;
    private Double cyExpenseRatio = 0.0;
    private Double lyExpenseRatio = 0.0;
    private Double cyGrantsReceiptsToTotalReceipts = 0.0;
    private Double lyGrantsReceiptsToTotalReceipts = 0.0;
    private Double cyCapitalExpenditureToTotalExpenditure = 0.0;
    private Double lyCapitalExpenditureToTotalExpenditure = 0.0;
    private Double cyCapitalRatio = 0.0;
    private Double lyCapitalRatio = 0.0;
    private Map<String, Double> cyDepartmentWiseRevenueExpenses;
    private Map<String, Double> lyDepartmentWiseRevenueExpenses;


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

    public String getUlbGrade() {
        return ulbGrade;
    }

    public void setUlbGrade(String ulbGrade) {
        this.ulbGrade = ulbGrade;
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

    public String getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(String financialYear) {
        this.financialYear = financialYear;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Double getCyTaxRevenueToTotalIncomeRatio() {
        return cyTaxRevenueToTotalIncomeRatio;
    }

    public void setCyTaxRevenueToTotalIncomeRatio(Double cyTaxRevenueToTotalIncomeRatio) {
        this.cyTaxRevenueToTotalIncomeRatio = cyTaxRevenueToTotalIncomeRatio;
    }

    public Double getLyTaxRevenueToTotalIncomeRatio() {
        return lyTaxRevenueToTotalIncomeRatio;
    }

    public void setLyTaxRevenueToTotalIncomeRatio(Double lyTaxRevenueToTotalIncomeRatio) {
        this.lyTaxRevenueToTotalIncomeRatio = lyTaxRevenueToTotalIncomeRatio;
    }

    public Double getCyAssignedRevenuesToTotalIncomeRatio() {
        return cyAssignedRevenuesToTotalIncomeRatio;
    }

    public void setCyAssignedRevenuesToTotalIncomeRatio(Double cyAssignedRevenuesToTotalIncomeRatio) {
        this.cyAssignedRevenuesToTotalIncomeRatio = cyAssignedRevenuesToTotalIncomeRatio;
    }

    public Double getLyAssignedRevenuesToTotalIncomeRatio() {
        return lyAssignedRevenuesToTotalIncomeRatio;
    }

    public void setLyAssignedRevenuesToTotalIncomeRatio(Double lyAssignedRevenuesToTotalIncomeRatio) {
        this.lyAssignedRevenuesToTotalIncomeRatio = lyAssignedRevenuesToTotalIncomeRatio;
    }

    public Double getCyRevenueGrantsToTotalIncomeRatio() {
        return cyRevenueGrantsToTotalIncomeRatio;
    }

    public void setCyRevenueGrantsToTotalIncomeRatio(Double cyRevenueGrantsToTotalIncomeRatio) {
        this.cyRevenueGrantsToTotalIncomeRatio = cyRevenueGrantsToTotalIncomeRatio;
    }

    public Double getLyRevenueGrantsToTotalIncomeRatio() {
        return lyRevenueGrantsToTotalIncomeRatio;
    }

    public void setLyRevenueGrantsToTotalIncomeRatio(Double lyRevenueGrantsToTotalIncomeRatio) {
        this.lyRevenueGrantsToTotalIncomeRatio = lyRevenueGrantsToTotalIncomeRatio;
    }

    public Double getCyRentalIncomeToTotalIncomeRatio() {
        return cyRentalIncomeToTotalIncomeRatio;
    }

    public void setCyRentalIncomeToTotalIncomeRatio(Double cyRentalIncomeToTotalIncomeRatio) {
        this.cyRentalIncomeToTotalIncomeRatio = cyRentalIncomeToTotalIncomeRatio;
    }

    public Double getLyRentalIncomeToTotalIncomeRatio() {
        return lyRentalIncomeToTotalIncomeRatio;
    }

    public void setLyRentalIncomeToTotalIncomeRatio(Double lyRentalIncomeToTotalIncomeRatio) {
        this.lyRentalIncomeToTotalIncomeRatio = lyRentalIncomeToTotalIncomeRatio;
    }

    public Double getCyFeeUserChargesToTotalIncomeRatio() {
        return cyFeeUserChargesToTotalIncomeRatio;
    }

    public void setCyFeeUserChargesToTotalIncomeRatio(Double cyFeeUserChargesToTotalIncomeRatio) {
        this.cyFeeUserChargesToTotalIncomeRatio = cyFeeUserChargesToTotalIncomeRatio;
    }

    public Double getLyFeeUserChargesToTotalIncomeRatio() {
        return lyFeeUserChargesToTotalIncomeRatio;
    }

    public void setLyFeeUserChargesToTotalIncomeRatio(Double lyFeeUserChargesToTotalIncomeRatio) {
        this.lyFeeUserChargesToTotalIncomeRatio = lyFeeUserChargesToTotalIncomeRatio;
    }

    public Double getCyIncomeRatio() {
        return cyIncomeRatio;
    }

    public void setCyIncomeRatio(Double cyIncomeRatio) {
        this.cyIncomeRatio = cyIncomeRatio;
    }

    public Double getLyIncomeRatio() {
        return lyIncomeRatio;
    }

    public void setLyIncomeRatio(Double lyIncomeRatio) {
        this.lyIncomeRatio = lyIncomeRatio;
    }

    public Double getCyEstablishmentExpensesToTotalReRatio() {
        return cyEstablishmentExpensesToTotalReRatio;
    }

    public void setCyEstablishmentExpensesToTotalReRatio(Double cyEstablishmentExpensesToTotalReRatio) {
        this.cyEstablishmentExpensesToTotalReRatio = cyEstablishmentExpensesToTotalReRatio;
    }

    public Double getLyEstablishmentExpensesToTotalReRatio() {
        return lyEstablishmentExpensesToTotalReRatio;
    }

    public void setLyEstablishmentExpensesToTotalReRatio(Double lyEstablishmentExpensesToTotalReRatio) {
        this.lyEstablishmentExpensesToTotalReRatio = lyEstablishmentExpensesToTotalReRatio;
    }

    public Double getCyAdministrativeExpensesToTotalReRatio() {
        return cyAdministrativeExpensesToTotalReRatio;
    }

    public void setCyAdministrativeExpensesToTotalReRatio(Double cyAdministrativeExpensesToTotalReRatio) {
        this.cyAdministrativeExpensesToTotalReRatio = cyAdministrativeExpensesToTotalReRatio;
    }

    public Double getLyAdministrativeExpensesToTotalReRatio() {
        return lyAdministrativeExpensesToTotalReRatio;
    }

    public void setLyAdministrativeExpensesToTotalReRatio(Double lyAdministrativeExpensesToTotalReRatio) {
        this.lyAdministrativeExpensesToTotalReRatio = lyAdministrativeExpensesToTotalReRatio;
    }

    public Double getCyOmExpensesToTotalReRatio() {
        return cyOmExpensesToTotalReRatio;
    }

    public void setCyOmExpensesToTotalReRatio(Double cyOmExpensesToTotalReRatio) {
        this.cyOmExpensesToTotalReRatio = cyOmExpensesToTotalReRatio;
    }

    public Double getLyOmExpensesToTotalReRatio() {
        return lyOmExpensesToTotalReRatio;
    }

    public void setLyOmExpensesToTotalReRatio(Double lyOmExpensesToTotalReRatio) {
        this.lyOmExpensesToTotalReRatio = lyOmExpensesToTotalReRatio;
    }

    public Double getCyExpenseRatio() {
        return cyExpenseRatio;
    }

    public void setCyExpenseRatio(Double cyExpenseRatio) {
        this.cyExpenseRatio = cyExpenseRatio;
    }

    public Double getLyExpenseRatio() {
        return lyExpenseRatio;
    }

    public void setLyExpenseRatio(Double lyExpenseRatio) {
        this.lyExpenseRatio = lyExpenseRatio;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public Double getCyGrantsReceiptsToTotalReceipts() {
        return cyGrantsReceiptsToTotalReceipts;
    }

    public void setCyGrantsReceiptsToTotalReceipts(Double cyGrantsReceiptsToTotalReceipts) {
        this.cyGrantsReceiptsToTotalReceipts = cyGrantsReceiptsToTotalReceipts;
    }

    public Double getLyGrantsReceiptsToTotalReceipts() {
        return lyGrantsReceiptsToTotalReceipts;
    }

    public void setLyGrantsReceiptsToTotalReceipts(Double lyGrantsReceiptsToTotalReceipts) {
        this.lyGrantsReceiptsToTotalReceipts = lyGrantsReceiptsToTotalReceipts;
    }

    public Double getCyCapitalExpenditureToTotalExpenditure() {
        return cyCapitalExpenditureToTotalExpenditure;
    }

    public void setCyCapitalExpenditureToTotalExpenditure(Double cyCapitalExpenditureToTotalExpenditure) {
        this.cyCapitalExpenditureToTotalExpenditure = cyCapitalExpenditureToTotalExpenditure;
    }

    public Double getLyCapitalExpenditureToTotalExpenditure() {
        return lyCapitalExpenditureToTotalExpenditure;
    }

    public void setLyCapitalExpenditureToTotalExpenditure(Double lyCapitalExpenditureToTotalExpenditure) {
        this.lyCapitalExpenditureToTotalExpenditure = lyCapitalExpenditureToTotalExpenditure;
    }

    public Double getCyCapitalRatio() {
        return cyCapitalRatio;
    }

    public void setCyCapitalRatio(Double cyCapitalRatio) {
        this.cyCapitalRatio = cyCapitalRatio;
    }

    public Double getLyCapitalRatio() {
        return lyCapitalRatio;
    }

    public void setLyCapitalRatio(Double lyCapitalRatio) {
        this.lyCapitalRatio = lyCapitalRatio;
    }

    public Map<String, Double> getCyDepartmentWiseRevenueExpenses() {
        return cyDepartmentWiseRevenueExpenses;
    }

    public void setCyDepartmentWiseRevenueExpenses(Map<String, Double> cyDepartmentWiseRevenueExpenses) {
        this.cyDepartmentWiseRevenueExpenses = cyDepartmentWiseRevenueExpenses;
    }

    public Map<String, Double> getLyDepartmentWiseRevenueExpenses() {
        return lyDepartmentWiseRevenueExpenses;
    }

    public void setLyDepartmentWiseRevenueExpenses(Map<String, Double> lyDepartmentWiseRevenueExpenses) {
        this.lyDepartmentWiseRevenueExpenses = lyDepartmentWiseRevenueExpenses;
    }
}
