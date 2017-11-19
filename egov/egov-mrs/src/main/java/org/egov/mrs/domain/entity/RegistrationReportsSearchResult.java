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

package org.egov.mrs.domain.entity;

/**
 * Bean to hold the search results
 *
 * @author nayeem
 *
 */
public class RegistrationReportsSearchResult {

    private Long registrationId;
    private String registrationNo;
    private String applicationNo;
    private String registrationDate;
    private String dateOfMarriage;
    private String husbandName;
    private String wifeName;
    private boolean certificateIssued;
    private String status;
    private Double feePaid;
    private boolean feeCollectionPending;
    private String applicationType;
    private String remarks;
    private String userName;
    private String pendingAction;
    private String placeOfMarriage;
    private String zone;
    private String registrationUnit;

    public Long getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(final Long registrationId) {
        this.registrationId = registrationId;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(final String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(final String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getDateOfMarriage() {
        return dateOfMarriage;
    }

    public void setDateOfMarriage(final String dateOfMarriage) {
        this.dateOfMarriage = dateOfMarriage;
    }

    public String getHusbandName() {
        return husbandName;
    }

    public void setHusbandName(final String husbandName) {
        this.husbandName = husbandName;
    }

    public String getWifeName() {
        return wifeName;
    }

    public void setWifeName(final String wifeName) {
        this.wifeName = wifeName;
    }

    public boolean isCertificateIssued() {
        return certificateIssued;
    }

    public void setCertificateIssued(final boolean certificateIssued) {
        this.certificateIssued = certificateIssued;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public Double getFeePaid() {
        return feePaid;
    }

    public void setFeePaid(final Double feePaid) {
        this.feePaid = feePaid;
    }

    public boolean isFeeCollectionPending() {
        return feeCollectionPending;
    }

    public void setFeeCollectionPending(final boolean feeCollectionPending) {
        this.feeCollectionPending = feeCollectionPending;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(final String applicationType) {
        this.applicationType = applicationType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPendingAction() {
        return pendingAction;
    }

    public void setPendingAction(String pendingAction) {
        this.pendingAction = pendingAction;
    }

    public String getPlaceOfMarriage() {
        return placeOfMarriage;
    }

    public void setPlaceOfMarriage(String placeOfMarriage) {
        this.placeOfMarriage = placeOfMarriage;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getRegistrationUnit() {
        return registrationUnit;
    }

    public void setRegistrationUnit(String registrationUnit) {
        this.registrationUnit = registrationUnit;
    }

    public String getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(String applicationNo) {
        this.applicationNo = applicationNo;
    }
}
