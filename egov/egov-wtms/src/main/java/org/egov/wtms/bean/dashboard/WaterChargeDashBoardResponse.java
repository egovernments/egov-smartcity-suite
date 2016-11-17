/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *     accountability and the service delivery of the government  organizations.
 *
 *      Copyright (C) 2016  eGovernments Foundation
 *
 *      The updated version of eGov suite of products as by eGovernments Foundation
 *      is available at http://www.egovernments.org
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with this program. If not, see http://www.gnu.org/licenses/ or
 *      http://www.gnu.org/licenses/gpl.html .
 *
 *      In addition to the terms of the GPL license to be adhered to in using this
 *      program, the following additional terms are to be complied with:
 *
 *          1) All versions of this program, verbatim or modified must carry this
 *             Legal Notice.
 *
 *          2) Any misrepresentation of the origin of the material is prohibited. It
 *             is required that all modified versions of this material be marked in
 *             reasonable ways as different from the original version.
 *
 *          3) This license does not grant any rights to any user of the program
 *             with regards to rights under trademark law for use of the trade names
 *             or trademarks of eGovernments Foundation.
 *
 *    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
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
    private BigDecimal lastYearVar = BigDecimal.ZERO;//lastYearVar (%)
    private BigDecimal todayColl = BigDecimal.ZERO;
    private BigDecimal lastYearTodayColl = BigDecimal.ZERO;//LastYearTodayColl
    private BigDecimal currentYearTillDateDmd = BigDecimal.ZERO;//currentYearTillDateDemand
    private String month;
    private BigDecimal previousYearColl = BigDecimal.ZERO;//previousYearCollection ex:2014
    private BigDecimal lastYearColl = BigDecimal.ZERO;//lasYearColl
    private BigDecimal currentYearColl = BigDecimal.ZERO;
    private ErrorDetails errorDetails;
    private BigDecimal currentYearTillDateBalDmd = BigDecimal.ZERO;//currentYearTilldateBalanceDeamnd
    private Long todayRcptsCount = 0L;
    private Long currentYearTillDateRcptsCount = 0L;
    private Long lastYearTillDateRcptsCount = 0L;
    private Long previousYearRcptsCount = 0L;
    private BigDecimal currDayColl = BigDecimal.ZERO;//currentDayColl

    private Long lastYearRcptsCount = 0L;
    private Long currentYearRcptsCount = 0L;
    
    private String billCollector = StringUtils.EMPTY;
   
    private BigDecimal achievement = BigDecimal.ZERO;// performanace
    
    public String getRegionName() {
        return regionName;
    }
    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }
    public String getDistrictName() {
        return districtName;
    }
    public void setDistrictName(String districtName) {
        this.districtName = districtName;
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
    public String getWardName() {
        return wardName;
    }
    public void setWardName(String wardName) {
        this.wardName = wardName;
    }
    public String getDomainURL() {
        return domainURL;
    }
    public void setDomainURL(String domainURL) {
        this.domainURL = domainURL;
    }
    
    public BigDecimal getTotalDmd() {
        return totalDmd;
    }
    public void setTotalDmd(BigDecimal totalDmd) {
        this.totalDmd = totalDmd;
    }
    public BigDecimal getCurrentYearTillDateColl() {
        return currentYearTillDateColl;
    }
    public void setCurrentYearTillDateColl(BigDecimal currentYearTillDateColl) {
        this.currentYearTillDateColl = currentYearTillDateColl;
    }
    public BigDecimal getLastYearTillDateColl() {
        return lastYearTillDateColl;
    }
    public void setLastYearTillDateColl(BigDecimal lastYearTillDateColl) {
        this.lastYearTillDateColl = lastYearTillDateColl;
    }
    public BigDecimal getPerformance() {
        return performance;
    }
    public void setPerformance(BigDecimal performance) {
        this.performance = performance;
    }
    public BigDecimal getLastYearVar() {
        return lastYearVar;
    }
    public void setLastYearVar(BigDecimal lastYearVar) {
        this.lastYearVar = lastYearVar;
    }
    public BigDecimal getTodayColl() {
        return todayColl;
    }
    public void setTodayColl(BigDecimal todayColl) {
        this.todayColl = todayColl;
    }
    public BigDecimal getLastYearTodayColl() {
        return lastYearTodayColl;
    }
    public void setLastYearTodayColl(BigDecimal lastYearTodayColl) {
        this.lastYearTodayColl = lastYearTodayColl;
    }
    public BigDecimal getCurrentYearTillDateDmd() {
        return currentYearTillDateDmd;
    }
    public void setCurrentYearTillDateDmd(BigDecimal currentYearTillDateDmd) {
        this.currentYearTillDateDmd = currentYearTillDateDmd;
    }
    public String getMonth() {
        return month;
    }
    public void setMonth(String month) {
        this.month = month;
    }
    public BigDecimal getPreviousYearColl() {
        return previousYearColl;
    }
    public void setPreviousYearColl(BigDecimal previousYearColl) {
        this.previousYearColl = previousYearColl;
    }
    public BigDecimal getLastYearColl() {
        return lastYearColl;
    }
    public void setLastYearColl(BigDecimal lastYearColl) {
        this.lastYearColl = lastYearColl;
    }
    public BigDecimal getCurrentYearColl() {
        return currentYearColl;
    }
    public void setCurrentYearColl(BigDecimal currentYearColl) {
        this.currentYearColl = currentYearColl;
    }
    public ErrorDetails getErrorDetails() {
        return errorDetails;
    }
    public void setErrorDetails(ErrorDetails errorDetails) {
        this.errorDetails = errorDetails;
    }
    public BigDecimal getCurrentYearTillDateBalDmd() {
        return currentYearTillDateBalDmd;
    }
    public void setCurrentYearTillDateBalDmd(BigDecimal currentYearTillDateBalDmd) {
        this.currentYearTillDateBalDmd = currentYearTillDateBalDmd;
    }
    public Long getTodayRcptsCount() {
        return todayRcptsCount;
    }
    public void setTodayRcptsCount(Long todayRcptsCount) {
        this.todayRcptsCount = todayRcptsCount;
    }
    public Long getCurrentYearTillDateRcptsCount() {
        return currentYearTillDateRcptsCount;
    }
    public void setCurrentYearTillDateRcptsCount(Long currentYearTillDateRcptsCount) {
        this.currentYearTillDateRcptsCount = currentYearTillDateRcptsCount;
    }
    public Long getLastYearTillDateRcptsCount() {
        return lastYearTillDateRcptsCount;
    }
    public void setLastYearTillDateRcptsCount(Long lastYearTillDateRcptsCount) {
        this.lastYearTillDateRcptsCount = lastYearTillDateRcptsCount;
    }
    public Long getPreviousYearRcptsCount() {
        return previousYearRcptsCount;
    }
    public void setPreviousYearRcptsCount(Long previousYearRcptsCount) {
        this.previousYearRcptsCount = previousYearRcptsCount;
    }
    public BigDecimal getCurrDayColl() {
        return currDayColl;
    }
    public void setCurrDayColl(BigDecimal currDayColl) {
        this.currDayColl = currDayColl;
    }
    public Long getLastYearRcptsCount() {
        return lastYearRcptsCount;
    }
    public void setLastYearRcptsCount(Long lastYearRcptsCount) {
        this.lastYearRcptsCount = lastYearRcptsCount;
    }
    public Long getCurrentYearRcptsCount() {
        return currentYearRcptsCount;
    }
    public void setCurrentYearRcptsCount(Long currentYearRcptsCount) {
        this.currentYearRcptsCount = currentYearRcptsCount;
    }
    public String getBillCollector() {
        return billCollector;
    }
    public void setBillCollector(String billCollector) {
        this.billCollector = billCollector;
    }
    public BigDecimal getAchievement() {
        return achievement;
    }
    public void setAchievement(BigDecimal achievement) {
        this.achievement = achievement;
    }
   
   
    
}
