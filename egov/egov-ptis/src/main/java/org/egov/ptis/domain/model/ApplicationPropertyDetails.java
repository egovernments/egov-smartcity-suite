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
package org.egov.ptis.domain.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class ApplicationPropertyDetails implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7726649225210947383L;
    private List<OwnerInfo> ownerDetails;
    private String applicationNo;
    private String applicationDate;
    private String applicationStatus;
    private String applicationApprovalDate;
    private String assessmentNo;
    private String areaofSite;
    private String propertyType;
    private String surveyNumber;
    private String pattaNumber;
    private BigDecimal registeredDocumentValue;
    private BigDecimal currentMarketValue;
    private String locality;
    private String revenueWard;
    private String electionWard;
    private String eastBoundary;
    private String westBoundary;
    private String northBoundary;
    private String southBoundary;
    private String documentType;
    private String documentNo;

    public List<OwnerInfo> getOwnerDetails() {
        return ownerDetails;
    }

    public void setOwnerDetails(List<OwnerInfo> ownerDetails) {
        this.ownerDetails = ownerDetails;
    }

    public String getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(String applicationNo) {
        this.applicationNo = applicationNo;
    }

    public String getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(String applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public String getApplicationApprovalDate() {
        return applicationApprovalDate;
    }

    public void setApplicationApprovalDate(String applicationApprovalDate) {
        this.applicationApprovalDate = applicationApprovalDate;
    }

    public String getAssessmentNo() {
        return assessmentNo;
    }

    public void setAssessmentNo(String assessmentNo) {
        this.assessmentNo = assessmentNo;
    }

    public String getAreaofSite() {
        return areaofSite;
    }

    public void setAreaofSite(String areaofSite) {
        this.areaofSite = areaofSite;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getSurveyNumber() {
        return surveyNumber;
    }

    public void setSurveyNumber(String surveyNumber) {
        this.surveyNumber = surveyNumber;
    }

    public String getPattaNumber() {
        return pattaNumber;
    }

    public void setPattaNumber(String pattaNumber) {
        this.pattaNumber = pattaNumber;
    }

    public BigDecimal getRegisteredDocumentValue() {
        return registeredDocumentValue;
    }

    public void setRegisteredDocumentValue(BigDecimal registeredDocumentValue) {
        this.registeredDocumentValue = registeredDocumentValue;
    }

    public BigDecimal getCurrentMarketValue() {
        return currentMarketValue;
    }

    public void setCurrentMarketValue(BigDecimal currentMarketValue) {
        this.currentMarketValue = currentMarketValue;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getRevenueWard() {
        return revenueWard;
    }

    public void setRevenueWard(String revenueWard) {
        this.revenueWard = revenueWard;
    }

    public String getElectionWard() {
        return electionWard;
    }

    public void setElectionWard(String electionWard) {
        this.electionWard = electionWard;
    }

    public String getEastBoundary() {
        return eastBoundary;
    }

    public void setEastBoundary(String eastBoundary) {
        this.eastBoundary = eastBoundary;
    }

    public String getWestBoundary() {
        return westBoundary;
    }

    public void setWestBoundary(String westBoundary) {
        this.westBoundary = westBoundary;
    }

    public String getNorthBoundary() {
        return northBoundary;
    }

    public void setNorthBoundary(String northBoundary) {
        this.northBoundary = northBoundary;
    }

    public String getSouthBoundary() {
        return southBoundary;
    }

    public void setSouthBoundary(String southBoundary) {
        this.southBoundary = southBoundary;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentNo() {
        return documentNo;
    }

    public void setDocumentNo(String documentNo) {
        this.documentNo = documentNo;
    }

    @Override
    public String toString() {
        return "{ownerDetails: \n" + " [{Arrays.toString(getOwnerDetails().toArray())" + "\n}]," + " applicationNo="
                + applicationNo
                + " ,\n applicationDate=" + applicationDate + " ,\n  applicationStatus=" + applicationStatus
                + " ,\n applicationApprovalDate="
                + applicationApprovalDate + ",\n  assessmentNo=" + assessmentNo + " ,\n areaofSite=" + areaofSite
                + " ,\n propertyType="
                + propertyType + "  ,\n surveyNumber=" + surveyNumber + " ,\n pattaNumber=" + pattaNumber
                + " ,\n currentMarketValue="
                + currentMarketValue + " ,\n registeredDocumentValue=" + registeredDocumentValue + " ,\n locality=" + locality
                + " ,\n revenueWard=" + revenueWard + " ,\n electionWard=" + electionWard + " ,\n eastBoundary=" + eastBoundary
                + " ,\n westBoundary=" + westBoundary + " ,\n northBoundary=" + northBoundary + " ,\n southBoundary="
                + southBoundary
                + " ,\n documentType=" + documentType + " ,\n documentNo=" + documentNo + "}";
    }
}
