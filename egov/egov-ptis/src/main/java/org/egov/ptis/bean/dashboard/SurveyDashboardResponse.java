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

public class SurveyDashboardResponse {

    private String ulbCode;
    private String applicationNo;
    private String assessmentNo;
    private String doorNo;
    private String revenueWard;
    private String revenueBlock;
    private String locality;
    private String electionWard;
    private double systemTax = 0;
    private double gisTax = 0;
    private double applicationTax = 0;
    private String appStatus;
    private String assistantName;
    private String riName;
    private Boolean isreffered;
    private Boolean isVerified;
    private String appViewURL;
    private String serviceName;
    private int ageing;
    private String functionaryName;
    private String wfStatus;

    public String getUlbCode() {
        return ulbCode;
    }

    public void setUlbCode(String ulbCode) {
        this.ulbCode = ulbCode;
    }

    public String getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(String applicationNo) {
        this.applicationNo = applicationNo;
    }

    public String getAssessmentNo() {
        return assessmentNo;
    }

    public void setAssessmentNo(String assessmentNo) {
        this.assessmentNo = assessmentNo;
    }

    public String getDoorNo() {
        return doorNo;
    }

    public void setDoorNo(String doorNo) {
        this.doorNo = doorNo;
    }

    public String getRevenueWard() {
        return revenueWard;
    }

    public void setRevenueWard(String revenueWard) {
        this.revenueWard = revenueWard;
    }

    public String getRevenueBlock() {
        return revenueBlock;
    }

    public void setRevenueBlock(String revenueBlock) {
        this.revenueBlock = revenueBlock;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getElectionWard() {
        return electionWard;
    }

    public void setElectionWard(String electionWard) {
        this.electionWard = electionWard;
    }

    public Double getSystemTax() {
        return systemTax;
    }

    public void setSystemTax(Double systemTax) {
        this.systemTax = systemTax;
    }

    public Double getGisTax() {
        return gisTax;
    }

    public void setGisTax(Double gisTax) {
        this.gisTax = gisTax;
    }

    public Double getApplicationTax() {
        return applicationTax;
    }

    public void setApplicationTax(Double applicationTax) {
        this.applicationTax = applicationTax;
    }

    public String getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(String appStatus) {
        this.appStatus = appStatus;
    }

    public String getAssistantName() {
        return assistantName;
    }

    public void setAssistantName(String assistantName) {
        this.assistantName = assistantName;
    }

    public String getRiName() {
        return riName;
    }

    public void setRiName(String riName) {
        this.riName = riName;
    }

    public Boolean getIsreffered() {
        return isreffered;
    }

    public void setIsreffered(Boolean isreffered) {
        this.isreffered = isreffered;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVarified(Boolean isVerified) {
        this.isVerified = isVerified;
    }

    public String getAppViewURL() {
        return appViewURL;
    }

    public void setAppViewURL(String appViewURL) {
        this.appViewURL = appViewURL;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getAgeing() {
        return ageing;
    }

    public void setAgeing(int ageing) {
        this.ageing = ageing;
    }

    public String getFunctionaryName() {
        return functionaryName;
    }

    public void setFunctionaryName(String functionaryName) {
        this.functionaryName = functionaryName;
    }

    public String getWfStatus() {
        return wfStatus;
    }

    public void setWfStatus(String wfStatus) {
        this.wfStatus = wfStatus;
    }

}
