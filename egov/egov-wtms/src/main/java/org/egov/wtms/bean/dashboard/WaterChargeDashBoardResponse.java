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

package org.egov.wtms.bean.dashboard;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.egov.ptis.domain.model.ErrorDetails;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_EMPTY)
public class WaterChargeDashBoardResponse {

    private String regionName;
    private String districtName;
    private String ulbCode;
    private String ulbName;
    private String ulbGrade;
    private String wardName;
    private String domainURL;
    private BigDecimal currentYearTillDateColl = BigDecimal.ZERO;
    private BigDecimal totalDmd = BigDecimal.ZERO;
    private BigDecimal performance = BigDecimal.ZERO;
    private BigDecimal lastYearTillDateColl = BigDecimal.ZERO;
    private BigDecimal lastYearVar = BigDecimal.ZERO;// lastYearVar (%)
    private BigDecimal todayColl = BigDecimal.ZERO;
    private BigDecimal lastYearTodayColl = BigDecimal.ZERO;// LastYearTodayColl
    private BigDecimal currentYearTillDateDmd = BigDecimal.ZERO;// currentYearTillDateDemand
    private String month;
    private BigDecimal previousYearColl = BigDecimal.ZERO;// previousYearCollection
                                                          // ex:2014
    private BigDecimal lastYearColl = BigDecimal.ZERO;// lasYearColl
    private BigDecimal currentYearColl = BigDecimal.ZERO;
    private ErrorDetails errorDetails;
    private BigDecimal currentYearTillDateBalDmd = BigDecimal.ZERO;// currentYearTilldateBalanceDeamnd
    private Long todayRcptsCount = 0L;
    private Long currentYearTillDateRcptsCount = 0L;
    private Long lastYearTillDateRcptsCount = 0L;
    private Long previousYearRcptsCount = 0L;
    private BigDecimal currDayColl = BigDecimal.ZERO;// currentDayColl

    private Long lastYearRcptsCount = 0L;
    private Long currentYearRcptsCount = 0L;

    private String billCollector = StringUtils.EMPTY;

    private BigDecimal achievement = BigDecimal.ZERO;// performanace

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(final String regionName) {
        this.regionName = regionName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(final String districtName) {
        this.districtName = districtName;
    }

    public String getUlbCode() {
        return ulbCode;
    }

    public void setUlbCode(final String ulbCode) {
        this.ulbCode = ulbCode;
    }

    public String getUlbName() {
        return ulbName;
    }

    public void setUlbName(final String ulbName) {
        this.ulbName = ulbName;
    }

    public String getUlbGrade() {
        return ulbGrade;
    }

    public void setUlbGrade(final String ulbGrade) {
        this.ulbGrade = ulbGrade;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(final String wardName) {
        this.wardName = wardName;
    }

    public String getDomainURL() {
        return domainURL;
    }

    public void setDomainURL(final String domainURL) {
        this.domainURL = domainURL;
    }

    public BigDecimal getTotalDmd() {
        return totalDmd;
    }

    public void setTotalDmd(final BigDecimal totalDmd) {
        this.totalDmd = totalDmd;
    }

    public BigDecimal getCurrentYearTillDateColl() {
        return currentYearTillDateColl;
    }

    public void setCurrentYearTillDateColl(final BigDecimal currentYearTillDateColl) {
        this.currentYearTillDateColl = currentYearTillDateColl;
    }

    public BigDecimal getLastYearTillDateColl() {
        return lastYearTillDateColl;
    }

    public void setLastYearTillDateColl(final BigDecimal lastYearTillDateColl) {
        this.lastYearTillDateColl = lastYearTillDateColl;
    }

    public BigDecimal getPerformance() {
        return performance;
    }

    public void setPerformance(final BigDecimal performance) {
        this.performance = performance;
    }

    public BigDecimal getLastYearVar() {
        return lastYearVar;
    }

    public void setLastYearVar(final BigDecimal lastYearVar) {
        this.lastYearVar = lastYearVar;
    }

    public BigDecimal getTodayColl() {
        return todayColl;
    }

    public void setTodayColl(final BigDecimal todayColl) {
        this.todayColl = todayColl;
    }

    public BigDecimal getLastYearTodayColl() {
        return lastYearTodayColl;
    }

    public void setLastYearTodayColl(final BigDecimal lastYearTodayColl) {
        this.lastYearTodayColl = lastYearTodayColl;
    }

    public BigDecimal getCurrentYearTillDateDmd() {
        return currentYearTillDateDmd;
    }

    public void setCurrentYearTillDateDmd(final BigDecimal currentYearTillDateDmd) {
        this.currentYearTillDateDmd = currentYearTillDateDmd;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(final String month) {
        this.month = month;
    }

    public BigDecimal getPreviousYearColl() {
        return previousYearColl;
    }

    public void setPreviousYearColl(final BigDecimal previousYearColl) {
        this.previousYearColl = previousYearColl;
    }

    public BigDecimal getLastYearColl() {
        return lastYearColl;
    }

    public void setLastYearColl(final BigDecimal lastYearColl) {
        this.lastYearColl = lastYearColl;
    }

    public BigDecimal getCurrentYearColl() {
        return currentYearColl;
    }

    public void setCurrentYearColl(final BigDecimal currentYearColl) {
        this.currentYearColl = currentYearColl;
    }

    public ErrorDetails getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(final ErrorDetails errorDetails) {
        this.errorDetails = errorDetails;
    }

    public BigDecimal getCurrentYearTillDateBalDmd() {
        return currentYearTillDateBalDmd;
    }

    public void setCurrentYearTillDateBalDmd(final BigDecimal currentYearTillDateBalDmd) {
        this.currentYearTillDateBalDmd = currentYearTillDateBalDmd;
    }

    public Long getTodayRcptsCount() {
        return todayRcptsCount;
    }

    public void setTodayRcptsCount(final Long todayRcptsCount) {
        this.todayRcptsCount = todayRcptsCount;
    }

    public Long getCurrentYearTillDateRcptsCount() {
        return currentYearTillDateRcptsCount;
    }

    public void setCurrentYearTillDateRcptsCount(final Long currentYearTillDateRcptsCount) {
        this.currentYearTillDateRcptsCount = currentYearTillDateRcptsCount;
    }

    public Long getLastYearTillDateRcptsCount() {
        return lastYearTillDateRcptsCount;
    }

    public void setLastYearTillDateRcptsCount(final Long lastYearTillDateRcptsCount) {
        this.lastYearTillDateRcptsCount = lastYearTillDateRcptsCount;
    }

    public Long getPreviousYearRcptsCount() {
        return previousYearRcptsCount;
    }

    public void setPreviousYearRcptsCount(final Long previousYearRcptsCount) {
        this.previousYearRcptsCount = previousYearRcptsCount;
    }

    public BigDecimal getCurrDayColl() {
        return currDayColl;
    }

    public void setCurrDayColl(final BigDecimal currDayColl) {
        this.currDayColl = currDayColl;
    }

    public Long getLastYearRcptsCount() {
        return lastYearRcptsCount;
    }

    public void setLastYearRcptsCount(final Long lastYearRcptsCount) {
        this.lastYearRcptsCount = lastYearRcptsCount;
    }

    public Long getCurrentYearRcptsCount() {
        return currentYearRcptsCount;
    }

    public void setCurrentYearRcptsCount(final Long currentYearRcptsCount) {
        this.currentYearRcptsCount = currentYearRcptsCount;
    }

    public String getBillCollector() {
        return billCollector;
    }

    public void setBillCollector(final String billCollector) {
        this.billCollector = billCollector;
    }

    public BigDecimal getAchievement() {
        return achievement;
    }

    public void setAchievement(final BigDecimal achievement) {
        this.achievement = achievement;
    }

}
