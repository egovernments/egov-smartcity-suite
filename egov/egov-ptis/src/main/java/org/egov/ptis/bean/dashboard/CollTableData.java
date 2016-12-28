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

package org.egov.ptis.bean.dashboard;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

public class CollTableData {

    private String regionName = StringUtils.EMPTY;
    private String districtName = StringUtils.EMPTY;
    private String ulbGrade = StringUtils.EMPTY;
    private String ulbName = StringUtils.EMPTY;
    private String wardName = StringUtils.EMPTY;
    private String billCollector = StringUtils.EMPTY;
    private String mobileNumber = StringUtils.EMPTY;
    private BigDecimal todayColl = BigDecimal.ZERO;
    private BigDecimal totalDmd = BigDecimal.ZERO;
    private BigDecimal cytdDmd = BigDecimal.ZERO;
    private BigDecimal cytdColl = BigDecimal.ZERO;
    private BigDecimal performance = BigDecimal.ZERO;
    private BigDecimal cytdBalDmd = BigDecimal.ZERO;
    private BigDecimal lytdColl = BigDecimal.ZERO;
    private BigDecimal arrearColl = BigDecimal.ZERO;
    private BigDecimal currentColl = BigDecimal.ZERO;
    private BigDecimal interest = BigDecimal.ZERO;
    private BigDecimal lyVar = BigDecimal.ZERO;

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

    public String getUlbName() {
        return ulbName;
    }

    public void setUlbName(String ulbName) {
        this.ulbName = ulbName;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public String getBillCollector() {
        return billCollector;
    }

    public void setBillCollector(String billCollector) {
        this.billCollector = billCollector;
    }

    public BigDecimal getTotalDmd() {
        return totalDmd;
    }

    public void setTotalDmd(BigDecimal totalDmd) {
        this.totalDmd = totalDmd;
    }

    public BigDecimal getCytdDmd() {
        return cytdDmd;
    }

    public void setCytdDmd(BigDecimal cytdDmd) {
        this.cytdDmd = cytdDmd;
    }

    public BigDecimal getCytdColl() {
        return cytdColl;
    }

    public void setCytdColl(BigDecimal cytdColl) {
        this.cytdColl = cytdColl;
    }

    public BigDecimal getPerformance() {
        return performance;
    }

    public void setPerformance(BigDecimal performance) {
        this.performance = performance;
    }

    public BigDecimal getCytdBalDmd() {
        return cytdBalDmd;
    }

    public void setCytdBalDmd(BigDecimal cytdBalDmd) {
        this.cytdBalDmd = cytdBalDmd;
    }

    public BigDecimal getLytdColl() {
        return lytdColl;
    }

    public void setLytdColl(BigDecimal lytdColl) {
        this.lytdColl = lytdColl;
    }

    public BigDecimal getLyVar() {
        return lyVar;
    }

    public void setLyVar(BigDecimal lyVar) {
        this.lyVar = lyVar;
    }

    public BigDecimal getTodayColl() {
        return todayColl;
    }

    public void setTodayColl(BigDecimal todayColl) {
        this.todayColl = todayColl;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public BigDecimal getArrearColl() {
        return arrearColl;
    }

    public void setArrearColl(BigDecimal arrearColl) {
        this.arrearColl = arrearColl;
    }

    public BigDecimal getCurrentColl() {
        return currentColl;
    }

    public void setCurrentColl(BigDecimal currentColl) {
        this.currentColl = currentColl;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }
}