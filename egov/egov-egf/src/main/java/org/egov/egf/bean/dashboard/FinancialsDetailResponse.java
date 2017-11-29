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

public class FinancialsDetailResponse {

    private String region = StringUtils.EMPTY;
    private String district = StringUtils.EMPTY;
    private String grade = StringUtils.EMPTY;
    private String ulbCode = StringUtils.EMPTY;
    private String ulbName = StringUtils.EMPTY;
    private String month = StringUtils.EMPTY;
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
    private BigDecimal cyIncomeDebitAmount = BigDecimal.ZERO;
    private BigDecimal lyIncomeDebitAmount = BigDecimal.ZERO;
    private BigDecimal cyIncomeCreditAmount = BigDecimal.ZERO;
    private BigDecimal lyIncomeCreditAmount = BigDecimal.ZERO;
    private BigDecimal cyExpenseDebitAmount = BigDecimal.ZERO;
    private BigDecimal lyExpenseDebitAmount = BigDecimal.ZERO;
    private BigDecimal cyExpenseCreditAmount = BigDecimal.ZERO;
    private BigDecimal lyExpenseCreditAmount = BigDecimal.ZERO;
    private BigDecimal cyIncomeNetAmount = BigDecimal.ZERO;
    private BigDecimal lyIncomeNetAmount = BigDecimal.ZERO;
    private BigDecimal cyExpenseNetAmount = BigDecimal.ZERO;
    private BigDecimal lyExpenseNetAmount = BigDecimal.ZERO;
    private BigDecimal cyIeNetAmount = BigDecimal.ZERO;
    private BigDecimal lyIeNetAmount = BigDecimal.ZERO;
    private BigDecimal cyLiabilitiesDebitAmount = BigDecimal.ZERO;
    private BigDecimal lyLiabilitiesDebitAmount = BigDecimal.ZERO;
    private BigDecimal cyLiabilitiesCreditAmount = BigDecimal.ZERO;
    private BigDecimal lyLiabilitiesCreditAmount = BigDecimal.ZERO;
    private BigDecimal cyAssetsDebitAmount = BigDecimal.ZERO;
    private BigDecimal lyAssetsDebitAmount = BigDecimal.ZERO;
    private BigDecimal cyAssetsCreditAmount = BigDecimal.ZERO;
    private BigDecimal lyAssetsCreditAmount = BigDecimal.ZERO;
    private BigDecimal cyLiabilitiesNetAmount = BigDecimal.ZERO;
    private BigDecimal lyLiabilitiesNetAmount = BigDecimal.ZERO;
    private BigDecimal cyAssetsNetAmount = BigDecimal.ZERO;
    private BigDecimal lyAssetsNetAmount = BigDecimal.ZERO;
    private BigDecimal cyAlNetAmount = BigDecimal.ZERO;
    private BigDecimal lyAlNetAmount = BigDecimal.ZERO;
    private BigDecimal cyLiabilitiesOpbAmount = BigDecimal.ZERO;
    private BigDecimal lyLiabilitiesOpbAmount = BigDecimal.ZERO;
    private BigDecimal cyAssetsOpbAmount = BigDecimal.ZERO;
    private BigDecimal lyAssetsOpbAmount = BigDecimal.ZERO;


    public BigDecimal getCyIncomeDebitAmount() {
        return cyIncomeDebitAmount;
    }

    public void setCyIncomeDebitAmount(BigDecimal cyIncomeDebitAmount) {
        this.cyIncomeDebitAmount = cyIncomeDebitAmount;
    }

    public BigDecimal getLyIncomeDebitAmount() {
        return lyIncomeDebitAmount;
    }

    public void setLyIncomeDebitAmount(BigDecimal lyIncomeDebitAmount) {
        this.lyIncomeDebitAmount = lyIncomeDebitAmount;
    }

    public BigDecimal getCyIncomeCreditAmount() {
        return cyIncomeCreditAmount;
    }

    public void setCyIncomeCreditAmount(BigDecimal cyIncomeCreditAmount) {
        this.cyIncomeCreditAmount = cyIncomeCreditAmount;
    }

    public BigDecimal getLyIncomeCreditAmount() {
        return lyIncomeCreditAmount;
    }

    public void setLyIncomeCreditAmount(BigDecimal lyIncomeCreditAmount) {
        this.lyIncomeCreditAmount = lyIncomeCreditAmount;
    }

    public BigDecimal getCyExpenseDebitAmount() {
        return cyExpenseDebitAmount;
    }

    public void setCyExpenseDebitAmount(BigDecimal cyExpenseDebitAmount) {
        this.cyExpenseDebitAmount = cyExpenseDebitAmount;
    }

    public BigDecimal getLyExpenseDebitAmount() {
        return lyExpenseDebitAmount;
    }

    public void setLyExpenseDebitAmount(BigDecimal lyExpenseDebitAmount) {
        this.lyExpenseDebitAmount = lyExpenseDebitAmount;
    }

    public BigDecimal getCyExpenseCreditAmount() {
        return cyExpenseCreditAmount;
    }

    public void setCyExpenseCreditAmount(BigDecimal cyExpenseCreditAmount) {
        this.cyExpenseCreditAmount = cyExpenseCreditAmount;
    }

    public BigDecimal getLyExpenseCreditAmount() {
        return lyExpenseCreditAmount;
    }

    public void setLyExpenseCreditAmount(BigDecimal lyExpenseCreditAmount) {
        this.lyExpenseCreditAmount = lyExpenseCreditAmount;
    }

    public BigDecimal getCyIncomeNetAmount() {
        return cyIncomeNetAmount;
    }

    public void setCyIncomeNetAmount(BigDecimal cyIncomeNetAmount) {
        this.cyIncomeNetAmount = cyIncomeNetAmount;
    }

    public BigDecimal getLyIncomeNetAmount() {
        return lyIncomeNetAmount;
    }

    public void setLyIncomeNetAmount(BigDecimal lyIncomeNetAmount) {
        this.lyIncomeNetAmount = lyIncomeNetAmount;
    }

    public BigDecimal getCyExpenseNetAmount() {
        return cyExpenseNetAmount;
    }

    public void setCyExpenseNetAmount(BigDecimal cyExpenseNetAmount) {
        this.cyExpenseNetAmount = cyExpenseNetAmount;
    }

    public BigDecimal getLyExpenseNetAmount() {
        return lyExpenseNetAmount;
    }

    public void setLyExpenseNetAmount(BigDecimal lyExpenseNetAmount) {
        this.lyExpenseNetAmount = lyExpenseNetAmount;
    }

    public BigDecimal getCyIeNetAmount() {
        return cyIeNetAmount;
    }

    public void setCyIeNetAmount(BigDecimal cyIeNetAmount) {
        this.cyIeNetAmount = cyIeNetAmount;
    }

    public BigDecimal getLyIeNetAmount() {
        return lyIeNetAmount;
    }

    public void setLyIeNetAmount(BigDecimal lyIeNetAmount) {
        this.lyIeNetAmount = lyIeNetAmount;
    }

    public BigDecimal getCyLiabilitiesDebitAmount() {
        return cyLiabilitiesDebitAmount;
    }

    public void setCyLiabilitiesDebitAmount(BigDecimal cyLiabilitiesDebitAmount) {
        this.cyLiabilitiesDebitAmount = cyLiabilitiesDebitAmount;
    }

    public BigDecimal getLyLiabilitiesDebitAmount() {
        return lyLiabilitiesDebitAmount;
    }

    public void setLyLiabilitiesDebitAmount(BigDecimal lyLiabilitiesDebitAmount) {
        this.lyLiabilitiesDebitAmount = lyLiabilitiesDebitAmount;
    }

    public BigDecimal getCyLiabilitiesCreditAmount() {
        return cyLiabilitiesCreditAmount;
    }

    public void setCyLiabilitiesCreditAmount(BigDecimal cyLiabilitiesCreditAmount) {
        this.cyLiabilitiesCreditAmount = cyLiabilitiesCreditAmount;
    }

    public BigDecimal getLyLiabilitiesCreditAmount() {
        return lyLiabilitiesCreditAmount;
    }

    public void setLyLiabilitiesCreditAmount(BigDecimal lyLiabilitiesCreditAmount) {
        this.lyLiabilitiesCreditAmount = lyLiabilitiesCreditAmount;
    }

    public BigDecimal getCyAssetsDebitAmount() {
        return cyAssetsDebitAmount;
    }

    public void setCyAssetsDebitAmount(BigDecimal cyAssetsDebitAmount) {
        this.cyAssetsDebitAmount = cyAssetsDebitAmount;
    }

    public BigDecimal getLyAssetsDebitAmount() {
        return lyAssetsDebitAmount;
    }

    public void setLyAssetsDebitAmount(BigDecimal lyAssetsDebitAmount) {
        this.lyAssetsDebitAmount = lyAssetsDebitAmount;
    }

    public BigDecimal getCyAssetsCreditAmount() {
        return cyAssetsCreditAmount;
    }

    public void setCyAssetsCreditAmount(BigDecimal cyAssetsCreditAmount) {
        this.cyAssetsCreditAmount = cyAssetsCreditAmount;
    }

    public BigDecimal getLyAssetsCreditAmount() {
        return lyAssetsCreditAmount;
    }

    public void setLyAssetsCreditAmount(BigDecimal lyAssetsCreditAmount) {
        this.lyAssetsCreditAmount = lyAssetsCreditAmount;
    }

    public BigDecimal getCyLiabilitiesNetAmount() {
        return cyLiabilitiesNetAmount;
    }

    public void setCyLiabilitiesNetAmount(BigDecimal cyLiabilitiesNetAmount) {
        this.cyLiabilitiesNetAmount = cyLiabilitiesNetAmount;
    }

    public BigDecimal getLyLiabilitiesNetAmount() {
        return lyLiabilitiesNetAmount;
    }

    public void setLyLiabilitiesNetAmount(BigDecimal lyLiabilitiesNetAmount) {
        this.lyLiabilitiesNetAmount = lyLiabilitiesNetAmount;
    }

    public BigDecimal getCyAssetsNetAmount() {
        return cyAssetsNetAmount;
    }

    public void setCyAssetsNetAmount(BigDecimal cyAssetsNetAmount) {
        this.cyAssetsNetAmount = cyAssetsNetAmount;
    }

    public BigDecimal getLyAssetsNetAmount() {
        return lyAssetsNetAmount;
    }

    public void setLyAssetsNetAmount(BigDecimal lyAssetsNetAmount) {
        this.lyAssetsNetAmount = lyAssetsNetAmount;
    }

    public BigDecimal getCyAlNetAmount() {
        return cyAlNetAmount;
    }

    public void setCyAlNetAmount(BigDecimal cyAlNetAmount) {
        this.cyAlNetAmount = cyAlNetAmount;
    }

    public BigDecimal getLyAlNetAmount() {
        return lyAlNetAmount;
    }

    public void setLyAlNetAmount(BigDecimal lyAlNetAmount) {
        this.lyAlNetAmount = lyAlNetAmount;
    }

    public BigDecimal getCyLiabilitiesOpbAmount() {
        return cyLiabilitiesOpbAmount;
    }

    public void setCyLiabilitiesOpbAmount(BigDecimal cyLiabilitiesOpbAmount) {
        this.cyLiabilitiesOpbAmount = cyLiabilitiesOpbAmount;
    }

    public BigDecimal getLyLiabilitiesOpbAmount() {
        return lyLiabilitiesOpbAmount;
    }

    public void setLyLiabilitiesOpbAmount(BigDecimal lyLiabilitiesOpbAmount) {
        this.lyLiabilitiesOpbAmount = lyLiabilitiesOpbAmount;
    }

    public BigDecimal getCyAssetsOpbAmount() {
        return cyAssetsOpbAmount;
    }

    public void setCyAssetsOpbAmount(BigDecimal cyAssetsOpbAmount) {
        this.cyAssetsOpbAmount = cyAssetsOpbAmount;
    }

    public BigDecimal getLyAssetsOpbAmount() {
        return lyAssetsOpbAmount;
    }

    public void setLyAssetsOpbAmount(BigDecimal lyAssetsOpbAmount) {
        this.lyAssetsOpbAmount = lyAssetsOpbAmount;
    }

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

    public String getUlbName() {
        return ulbName;
    }

    public void setUlbName(String ulbName) {
        this.ulbName = ulbName;
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

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getFunctionCode() {
        return functionCode;
    }

    public void setFunctionCode(String functionCode) {
        this.functionCode = functionCode;
    }

    public String getFundSource() {
        return fundSource;
    }

    public void setFundSource(String fundSource) {
        this.fundSource = fundSource;
    }

    public String getSchemeCode() {
        return schemeCode;
    }

    public void setSchemeCode(String schemeCode) {
        this.schemeCode = schemeCode;
    }

    public String getSubschemeCode() {
        return subschemeCode;
    }

    public void setSubschemeCode(String subschemeCode) {
        this.subschemeCode = subschemeCode;
    }

    public String getMajorCode() {
        return majorCode;
    }

    public void setMajorCode(String majorCode) {
        this.majorCode = majorCode;
    }

    public String getMinorCode() {
        return minorCode;
    }

    public void setMinorCode(String minorCode) {
        this.minorCode = minorCode;
    }

    public String getDetailedCode() {
        return detailedCode;
    }

    public void setDetailedCode(String detailedCode) {
        this.detailedCode = detailedCode;
    }

    public String getUlbCode() {
        return ulbCode;
    }

    public void setUlbCode(String ulbCode) {
        this.ulbCode = ulbCode;
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

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
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
}
