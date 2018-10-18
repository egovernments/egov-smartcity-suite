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
package org.egov.ptis.bean.aadharseeding;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.egov.ptis.domain.entity.property.PropertyOwnerInfo;

public class AadharSeedingRequest {

    private Long wardId;
    private Long electionWardId;
    private String doorNo;
    private String assessmentNo;
    private String address;
    private String ownershipCategory;
    private String zoneName;
    private String revenueWardName;
    private String blockName;
    private String electionWardName;
    private String localty;
    private List<PropertyOwnerInfo> propertyOwnerInfo;
    private List<PropertyOwnerInfo> propertyOwnerInfoProxy;
    private Double latitude;
    private Double longitude;
    private BigDecimal extentOfSite;
    private BigDecimal plinthArea;
    private String propertyType;
    private String docNo;
    private Date docDate;
    private String docType;
    private String surveyNumber;
    private String documentType;
    private String successMessage;
    
    public Long getWardId() {
        return wardId;
    }

    public void setWardId(Long wardId) {
        this.wardId = wardId;
    }

    public Long getElectionWardId() {
        return electionWardId;
    }

    public void setElectionWardId(Long electionWardId) {
        this.electionWardId = electionWardId;
    }

    public String getDoorNo() {
        return doorNo;
    }

    public void setDoorNo(String doorNo) {
        this.doorNo = doorNo;
    }

    public String getAssessmentNo() {
        return assessmentNo;
    }

    public void setAssessmentNo(String assessmentNo) {
        this.assessmentNo = assessmentNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOwnershipCategory() {
        return ownershipCategory;
    }

    public void setOwnershipCategory(String ownershipCategory) {
        this.ownershipCategory = ownershipCategory;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getRevenueWardName() {
        return revenueWardName;
    }

    public void setRevenueWardName(String revenueWardName) {
        this.revenueWardName = revenueWardName;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String getElectionWardName() {
        return electionWardName;
    }

    public void setElectionWardName(String electionWardName) {
        this.electionWardName = electionWardName;
    }

    public String getLocalty() {
        return localty;
    }

    public void setLocalty(String localty) {
        this.localty = localty;
    }

    public List<PropertyOwnerInfo> getPropertyOwnerInfo() {
        return propertyOwnerInfo;
    }

    public void setPropertyOwnerInfo(List<PropertyOwnerInfo> propertyOwnerInfo) {
        this.propertyOwnerInfo = propertyOwnerInfo;
    }

    public List<PropertyOwnerInfo> getPropertyOwnerInfoProxy() {
        return propertyOwnerInfoProxy;
    }

    public void setPropertyOwnerInfoProxy(List<PropertyOwnerInfo> propertyOwnerInfoProxy) {
        this.propertyOwnerInfoProxy = propertyOwnerInfoProxy;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getExtentOfSite() {
        return extentOfSite;
    }

    public void setExtentOfSite(BigDecimal extentOfSite) {
        this.extentOfSite = extentOfSite;
    }

    public BigDecimal getPlinthArea() {
        return plinthArea;
    }

    public void setPlinthArea(BigDecimal plinthArea) {
        this.plinthArea = plinthArea;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getDocNo() {
        return docNo;
    }

    public void setDocNo(String docNo) {
        this.docNo = docNo;
    }

    public Date getDocDate() {
        return docDate;
    }

    public void setDocDate(Date docDate) {
        this.docDate = docDate;
    }

    public String getSurveyNumber() {
        return surveyNumber;
    }

    public void setSurveyNumber(String surveyNumber) {
        this.surveyNumber = surveyNumber;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }
}