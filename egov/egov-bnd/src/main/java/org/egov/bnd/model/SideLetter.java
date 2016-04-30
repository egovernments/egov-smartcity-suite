/*******************************************************************************
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
 * 	1) All versions of this program, verbatim or modified must carry this
 * 	   Legal Notice.
 *
 * 	2) Any misrepresentation of the origin of the material is prohibited. It
 * 	   is required that all modified versions of this material be marked in
 * 	   reasonable ways as different from the original version.
 *
 * 	3) This license does not grant any rights to any user of the program
 * 	   with regards to rights under trademark law for use of the trade names
 * 	   or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.bnd.model;

import org.egov.infra.workflow.entity.StateAware;

import java.util.Date;

public class SideLetter extends StateAware {

    private static final long serialVersionUID = -4410046131949379719L;
    private Long id;
    private String referenceNumber;
    private String eventType;
    private String remarks;
    private Date dateOfissue;
    private Date applicationDate;
    private String citizenName;
    private String applicantName;
    private CRelation applicantRelationType;
    private String applicantAddress;
    private BirthRegistration birthReportId;
    private String talukName;

    public String getTalukName() {
        return talukName;
    }

    public void setTalukName(final String talukName) {
        this.talukName = talukName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(final String districtName) {
        this.districtName = districtName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(final String stateName) {
        this.stateName = stateName;
    }

    private String districtName;
    private String stateName;

    public BirthRegistration getBirthReportId() {
        return birthReportId;
    }

    public void setBirthReportId(final BirthRegistration birthReportId) {
        this.birthReportId = birthReportId;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(final String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(final String eventType) {
        this.eventType = eventType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    public Date getDateOfissue() {
        return dateOfissue;
    }

    public void setDateOfissue(final Date dateOfissue) {
        this.dateOfissue = dateOfissue;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(final Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getCitizenName() {
        return citizenName;
    }

    public void setCitizenName(final String citizenName) {
        this.citizenName = citizenName;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(final String applicantName) {
        this.applicantName = applicantName;
    }

    public String getApplicantAddress() {
        return applicantAddress;
    }

    public void setApplicantAddress(final String applicantAddress) {
        this.applicantAddress = applicantAddress;
    }

    public CRelation getApplicantRelationType() {
        return applicantRelationType;
    }

    public void setApplicantRelationType(final CRelation applicantRelationType) {
        this.applicantRelationType = applicantRelationType;
    }

    @Override
    public String getStateDetails() {

        return null;
    }

    @Override
    public String toString() {

        final StringBuffer obj = new StringBuffer();
        obj.append("OBJECT=" + getClass());
        obj.append("ID=" + id);
        obj.append("referenceNumber=" + referenceNumber);
        obj.append("eventType=" + eventType);
        obj.append("remarks=" + remarks);
        obj.append("dateOfissue=" + dateOfissue);
        obj.append("applicationDate=" + applicationDate);
        obj.append("citizenName=" + citizenName);
        obj.append("applicantName=" + applicantName);
        obj.append("applicantRelationType="
                + (applicantRelationType != null ? applicantRelationType.getRelatedAsConst() : null));
        obj.append("applicantAddress=" + applicantAddress);
        obj.append("birthReportId=" + birthReportId);
        obj.append("talukName=" + talukName);
        obj.append("districtName=" + districtName);
        obj.append("stateName=" + stateName);

        return obj.toString();
    }

}
