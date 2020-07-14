/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2020  eGovernments Foundation
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
package org.egov.ptis.domain.model.reportregister;

import java.util.Date;

public class RevisedAssessmentDetailsBean {

    private String applicationType;
    private Date constructionDate;
    private Date effectiveDate;
    private Date demoltionEffectiveDate;
    private String roofType;
    private String floorType;
    private String woodType;
    private String wallType;
    private String electricity;
    private String waterTap;
    private String attachedBathroom;
    private String specialNotice;
    private Date specialNoticeDate;
    private String applicationNoRP;
    private Date applicationDateRP;
    private TaxDetailsBean revisedTaxDetails;

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType;
    }

    public Date getConstructionDate() {
        return constructionDate;
    }

    public void setConstructionDate(Date constructionDate) {
        this.constructionDate = constructionDate;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getDemoltionEffectiveDate() {
        return demoltionEffectiveDate;
    }

    public void setDemoltionEffectiveDate(Date demoltionEffectiveDate) {
        this.demoltionEffectiveDate = demoltionEffectiveDate;
    }

    public String getRoofType() {
        return roofType;
    }

    public void setRoofType(String roofType) {
        this.roofType = roofType;
    }

    public String getFloorType() {
        return floorType;
    }

    public void setFloorType(String floorType) {
        this.floorType = floorType;
    }

    public String getWoodType() {
        return woodType;
    }

    public void setWoodType(String woodType) {
        this.woodType = woodType;
    }

    public String getWallType() {
        return wallType;
    }

    public void setWallType(String wallType) {
        this.wallType = wallType;
    }

    public String getElectricity() {
        return electricity;
    }

    public void setElectricity(String electricity) {
        this.electricity = electricity;
    }

    public String getWaterTap() {
        return waterTap;
    }

    public void setWaterTap(String waterTap) {
        this.waterTap = waterTap;
    }

    public String getAttachedBathroom() {
        return attachedBathroom;
    }

    public void setAttachedBathroom(String attachedBathroom) {
        this.attachedBathroom = attachedBathroom;
    }

    public String getSpecialNotice() {
        return specialNotice;
    }

    public void setSpecialNotice(String specialNotice) {
        this.specialNotice = specialNotice;
    }

    public String getApplicationNoRP() {
        return applicationNoRP;
    }

    public void setApplicationNoRP(String applicationNoRP) {
        this.applicationNoRP = applicationNoRP;
    }

    public Date getApplicationDateRP() {
        return applicationDateRP;
    }

    public void setApplicationDateRP(Date applicationDateRP) {
        this.applicationDateRP = applicationDateRP;
    }

    public TaxDetailsBean getRevisedTaxDetails() {
        return revisedTaxDetails;
    }

    public void setRevisedTaxDetails(TaxDetailsBean revisedTaxDetails) {
        this.revisedTaxDetails = revisedTaxDetails;
    }

    public Date getSpecialNoticeDate() {
        return specialNoticeDate;
    }

    public void setSpecialNoticeDate(Date specialNoticeDate) {
        this.specialNoticeDate = specialNoticeDate;
    }
}
