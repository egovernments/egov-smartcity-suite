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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_EMPTY)
public class WaterChargeConnectionTypeResponse {

    private String ulbName = StringUtils.EMPTY;
    private BigDecimal residentialtotalCollection = BigDecimal.ZERO;
    private BigDecimal comercialtotalCollection = BigDecimal.ZERO;
    private Long residentialConnectionCount = 0L;
    private Long commercialConnectionCount = 0L;
    private BigDecimal waterChargeResidentialaverage;
    private BigDecimal waterChargeCommercialaverage;
    private String regionName = StringUtils.EMPTY;
    private String districtName = StringUtils.EMPTY;
    private String ulbGrade = StringUtils.EMPTY;
    private String wardName = StringUtils.EMPTY;
    private BigDecimal todayColl = BigDecimal.ZERO;
    private BigDecimal lastYearTodayColl = BigDecimal.ZERO;
    private BigDecimal currentYearTillDateColl = BigDecimal.ZERO;
    private BigDecimal lastYearTillDateColl = BigDecimal.ZERO;
    private BigDecimal totalDmd = BigDecimal.ZERO;
    private BigDecimal currentYearTillDateDmd = BigDecimal.ZERO;
    private BigDecimal previousYearResidentialColl = BigDecimal.ZERO;//previousYearCollection ex:2014
    private BigDecimal lastYearResidentialColl = BigDecimal.ZERO;//lasYearColl
    private BigDecimal currentYearResidentialColl = BigDecimal.ZERO;
    
    private BigDecimal previousYearCommercialColl = BigDecimal.ZERO;//previousYearCollection ex:2014
    private BigDecimal lastYearCommercialColl = BigDecimal.ZERO;//lasYearColl
    private BigDecimal currentYearCommercialColl = BigDecimal.ZERO;
    private BigDecimal performance = BigDecimal.ZERO;
    private BigDecimal lastYearVar = BigDecimal.ZERO;

    private String month;
    private BigDecimal previousYearColl = BigDecimal.ZERO;
    private BigDecimal lastYearColl = BigDecimal.ZERO;
    private BigDecimal currentYearColl = BigDecimal.ZERO;
    private BigDecimal residentialAchievement = BigDecimal.ZERO;
    private BigDecimal commercialAchievement = BigDecimal.ZERO;

    public BigDecimal getCurrentYearTillDateDmd() {
        return currentYearTillDateDmd;
    }

    public void setCurrentYearTillDateDmd(final BigDecimal currentYearTillDateDmd) {
        this.currentYearTillDateDmd = currentYearTillDateDmd;
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

    public String getUlbName() {
        return ulbName;
    }

    public void setUlbName(final String ulbName) {
        this.ulbName = ulbName;
    }

    public BigDecimal getWaterChargeResidentialaverage() {
        return waterChargeResidentialaverage;
    }

    public void setWaterChargeResidentialaverage(final BigDecimal waterChargeResidentialaverage) {
        this.waterChargeResidentialaverage = waterChargeResidentialaverage;
    }

    public BigDecimal getWaterChargeCommercialaverage() {
        return waterChargeCommercialaverage;
    }

    public void setWaterChargeCommercialaverage(final BigDecimal waterChargeCommercialaverage) {
        this.waterChargeCommercialaverage = waterChargeCommercialaverage;
    }

    public BigDecimal getResidentialAchievement() {
        return residentialAchievement;
    }

    public void setResidentialAchievement(final BigDecimal residentialAchievement) {
        this.residentialAchievement = residentialAchievement;
    }

    public BigDecimal getCommercialAchievement() {
        return commercialAchievement;
    }

    public void setCommercialAchievement(final BigDecimal commercialAchievement) {
        this.commercialAchievement = commercialAchievement;
    }

    public BigDecimal getResidentialtotalCollection() {
        return residentialtotalCollection;
    }

    public void setResidentialtotalCollection(final BigDecimal residentialtotalCollection) {
        this.residentialtotalCollection = residentialtotalCollection;
    }

    public BigDecimal getComercialtotalCollection() {
        return comercialtotalCollection;
    }

    public void setComercialtotalCollection(final BigDecimal comercialtotalCollection) {
        this.comercialtotalCollection = comercialtotalCollection;
    }

    public Long getResidentialConnectionCount() {
        return residentialConnectionCount;
    }

    public void setResidentialConnectionCount(final Long residentialConnectionCount) {
        this.residentialConnectionCount = residentialConnectionCount;
    }

    public Long getCommercialConnectionCount() {
        return commercialConnectionCount;
    }

    public void setCommercialConnectionCount(final Long commercialConnectionCount) {
        this.commercialConnectionCount = commercialConnectionCount;
    }

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

    public BigDecimal getTotalDmd() {
        return totalDmd;
    }

    public void setTotalDmd(final BigDecimal totalDmd) {
        this.totalDmd = totalDmd;
    }

    public BigDecimal getPreviousYearResidentialColl() {
        return previousYearResidentialColl;
    }

    public void setPreviousYearResidentialColl(BigDecimal previousYearResidentialColl) {
        this.previousYearResidentialColl = previousYearResidentialColl;
    }

    public BigDecimal getLastYearResidentialColl() {
        return lastYearResidentialColl;
    }

    public void setLastYearResidentialColl(BigDecimal lastYearResidentialColl) {
        this.lastYearResidentialColl = lastYearResidentialColl;
    }

    public BigDecimal getCurrentYearResidentialColl() {
        return currentYearResidentialColl;
    }

    public void setCurrentYearResidentialColl(BigDecimal currentYearResidentialColl) {
        this.currentYearResidentialColl = currentYearResidentialColl;
    }

    public BigDecimal getPreviousYearCommercialColl() {
        return previousYearCommercialColl;
    }

    public void setPreviousYearCommercialColl(BigDecimal previousYearCommercialColl) {
        this.previousYearCommercialColl = previousYearCommercialColl;
    }

    public BigDecimal getLastYearCommercialColl() {
        return lastYearCommercialColl;
    }

    public void setLastYearCommercialColl(BigDecimal lastYearCommercialColl) {
        this.lastYearCommercialColl = lastYearCommercialColl;
    }

    public BigDecimal getCurrentYearCommercialColl() {
        return currentYearCommercialColl;
    }

    public void setCurrentYearCommercialColl(BigDecimal currentYearCommercialColl) {
        this.currentYearCommercialColl = currentYearCommercialColl;
    }

}
