/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
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
    private Long residentialConnectionCount =0L;
    private Long commercialConnectionCount =0L;
    private BigDecimal waterChargeResidentialaverage;
    private BigDecimal waterChargeCommercialaverage ;
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
    public void setCurrentYearTillDateDmd(BigDecimal currentYearTillDateDmd) {
        this.currentYearTillDateDmd = currentYearTillDateDmd;
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
    public String getUlbName() {
        return ulbName;
    }
    public void setUlbName(String ulbName) {
        this.ulbName = ulbName;
    }
   
    
    public BigDecimal getWaterChargeResidentialaverage() {
        return waterChargeResidentialaverage;
    }
    public void setWaterChargeResidentialaverage(BigDecimal waterChargeResidentialaverage) {
        this.waterChargeResidentialaverage = waterChargeResidentialaverage;
    }
    public BigDecimal getWaterChargeCommercialaverage() {
        return waterChargeCommercialaverage;
    }
    public void setWaterChargeCommercialaverage(BigDecimal waterChargeCommercialaverage) {
        this.waterChargeCommercialaverage = waterChargeCommercialaverage;
    }
    public BigDecimal getResidentialAchievement() {
        return residentialAchievement;
    }
    public void setResidentialAchievement(BigDecimal residentialAchievement) {
        this.residentialAchievement = residentialAchievement;
    }
    public BigDecimal getCommercialAchievement() {
        return commercialAchievement;
    }
    public void setCommercialAchievement(BigDecimal commercialAchievement) {
        this.commercialAchievement = commercialAchievement;
    }
    public BigDecimal getResidentialtotalCollection() {
        return residentialtotalCollection;
    }
    public void setResidentialtotalCollection(BigDecimal residentialtotalCollection) {
        this.residentialtotalCollection = residentialtotalCollection;
    }
    public BigDecimal getComercialtotalCollection() {
        return comercialtotalCollection;
    }
    public void setComercialtotalCollection(BigDecimal comercialtotalCollection) {
        this.comercialtotalCollection = comercialtotalCollection;
    }
   
    public Long getResidentialConnectionCount() {
        return residentialConnectionCount;
    }
    public void setResidentialConnectionCount(Long residentialConnectionCount) {
        this.residentialConnectionCount = residentialConnectionCount;
    }
    public Long getCommercialConnectionCount() {
        return commercialConnectionCount;
    }
    public void setCommercialConnectionCount(Long commercialConnectionCount) {
        this.commercialConnectionCount = commercialConnectionCount;
    }
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
    public BigDecimal getTotalDmd() {
        return totalDmd;
    }
    public void setTotalDmd(BigDecimal totalDmd) {
        this.totalDmd = totalDmd;
    }
  
    
    
   
   
  


}
