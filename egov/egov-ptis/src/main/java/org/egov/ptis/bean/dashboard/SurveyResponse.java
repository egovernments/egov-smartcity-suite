/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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
package org.egov.ptis.bean.dashboard;

import org.apache.commons.lang3.StringUtils;

public class SurveyResponse {

    private String regionName = StringUtils.EMPTY;
    private String districtName = StringUtils.EMPTY;
    private String ulbGrade = StringUtils.EMPTY;
    private String ulbCode = StringUtils.EMPTY;
    private String ulbName = StringUtils.EMPTY;
    private String wardName = StringUtils.EMPTY;
    private String billCollector = StringUtils.EMPTY;
    private String billCollMobile = StringUtils.EMPTY;
    private String serviceName = StringUtils.EMPTY;
    private String functionaryName = StringUtils.EMPTY;
    private long totalReceived = 0;
    private long totalCompleted = 0;
    private long totalPending = 0;
    private double pctCompleted = 0;
    private long avgProcessingTime;
    private long verifyPending;
    private long verifyDone;
    private double exptdIncr = 0;
    private double actlIncr = 0;
    private double diffFromSurveytax = 0;
    private double difference = 0;
    private long totalCancelled=0;
    private long totalClosed=0;

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

    public String getBillCollMobile() {
        return billCollMobile;
    }

    public void setBillCollMobile(String billCollMobile) {
        this.billCollMobile = billCollMobile;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public long getTotalReceived() {
        return totalReceived;
    }

    public void setTotalReceived(long totalReceived) {
        this.totalReceived = totalReceived;
    }

    public long getTotalCompleted() {
        return totalCompleted;
    }

    public void setTotalCompleted(long totalCompleted) {
        this.totalCompleted = totalCompleted;
    }

    public long getTotalPending() {
        return totalPending;
    }

    public void setTotalPending(long totalPending) {
        this.totalPending = totalPending;
    }

    public double getPctCompleted() {
        return pctCompleted;
    }

    public void setPctCompleted(double pctCompleted) {
        this.pctCompleted = pctCompleted;
    }

    public long getAvgProcessingTime() {
        return avgProcessingTime;
    }

    public void setAvgProcessingTime(long avgProcessingTime) {
        this.avgProcessingTime = avgProcessingTime;
    }

    public long getVerifyPending() {
        return verifyPending;
    }

    public void setVerifyPending(long verifyPending) {
        this.verifyPending = verifyPending;
    }

    public long getVerifyDone() {
        return verifyDone;
    }

    public void setVerifyDone(long verifyDone) {
        this.verifyDone = verifyDone;
    }

    public double getExptdIncr() {
        return exptdIncr;
    }

    public void setExptdIncr(double exptdIncr) {
        this.exptdIncr = exptdIncr;
    }

    public double getActlIncr() {
        return actlIncr;
    }

    public void setActlIncr(double actlIncr) {
        this.actlIncr = actlIncr;
    }

    public double getDifference() {
        return difference;
    }

    public void setDifference(double difference) {
        this.difference = difference;
    }

    public String getFunctionaryName() {
        return functionaryName;
    }

    public void setFunctionaryName(String functionaryName) {
        this.functionaryName = functionaryName;
    }

    public double getDiffFromSurveytax() {
        return diffFromSurveytax;
    }

    public void setDiffFromSurveytax(double diffFromSurveytax) {
        this.diffFromSurveytax = diffFromSurveytax;
    }
    public long getTotalCancelled() {
        return totalCancelled;
    }

    public void setTotalCancelled(long totalCancelled) {
        this.totalCancelled = totalCancelled;
    }

    public long getTotalClosed() {
        return totalClosed;
    }

    public void setTotalClosed(long totalClosed) {
        this.totalClosed = totalClosed;
    }


}
