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
package org.egov.tl.entity.transfer;

import java.util.Date;
import java.util.List;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.persistence.entity.Address;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.tl.entity.License;
import org.egov.tl.entity.LicenseDocument;

public class LicenseTransfer extends StateAware {
    private Long id;
    private License license;
    private String oldApplicantName;
    private String oldApplicationNumber;
    private Date oldApplicationDate;
    private String oldNameOfEstablishment;
    private Address oldAddress;
    private String oldPhoneNumber;
    private String oldHomePhoneNumber;
    private String oldMobileNumber;
    private String oldEmailId;
    private String oldUid;
    protected Boundary boundary;
    private List<LicenseDocument> documents;

    public Boundary getBoundary() {
        return boundary;
    }

    public void setBoundary(Boundary boundary) {
        this.boundary = boundary;
    }

    private boolean approved;

    public String getOldNameOfEstablishment() {
        return oldNameOfEstablishment;
    }

    public void setOldNameOfEstablishment(String oldNameOfEstablishment) {
        this.oldNameOfEstablishment = oldNameOfEstablishment;
    }

    public String getOldPhoneNumber() {
        return oldPhoneNumber;
    }

    public void setOldPhoneNumber(String oldPhoneNumber) {
        this.oldPhoneNumber = oldPhoneNumber;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public License getLicense() {
        return license;
    }

    public void setLicense(License license) {
        this.license = license;
    }

    public String getOldApplicantName() {
        return oldApplicantName;
    }

    public void setOldApplicantName(String oldApplicantName) {
        this.oldApplicantName = oldApplicantName;
    }

    public String getOldApplicationNumber() {
        return oldApplicationNumber;
    }

    public void setOldApplicationNumber(String oldApplicationNumber) {
        this.oldApplicationNumber = oldApplicationNumber;
    }

    public Date getOldApplicationDate() {
        return oldApplicationDate;
    }

    public void setOldApplicationDate(Date oldApplicationDate) {
        this.oldApplicationDate = oldApplicationDate;
    }

    public Address getOldAddress() {
        return oldAddress;
    }

    public void setOldAddress(Address oldAddress) {
        this.oldAddress = oldAddress;
    }

    @Override
    public String getStateDetails() {
        return this.license.getApplicationNumber();
    }

    public String getOldHomePhoneNumber() {
        return oldHomePhoneNumber;
    }

    public void setOldHomePhoneNumber(String oldHomePhoneNumber) {
        this.oldHomePhoneNumber = oldHomePhoneNumber;
    }

    public String getOldMobileNumber() {
        return oldMobileNumber;
    }

    public void setOldMobileNumber(String oldMobileNumber) {
        this.oldMobileNumber = oldMobileNumber;
    }

    public String getOldEmailId() {
        return oldEmailId;
    }

    public void setOldEmailId(String oldEmailId) {
        this.oldEmailId = oldEmailId;
    }

    public String getOldUid() {
        return oldUid;
    }

    public void setOldUid(String oldUid) {
        this.oldUid = oldUid;
    }

    @Override
    public String toString() {
        final StringBuilder str = new StringBuilder();
        str.append("LicenseTransfer={");
        str.append("oldApplicantName=").append(oldApplicantName == null ? "null" : oldApplicantName.toString());
        str.append("oldApplicationNumber=").append(oldApplicationNumber == null ? "null" : oldApplicationNumber.toString());
        str.append("oldApplicationDate=").append(oldApplicationDate == null ? "null" : oldApplicationDate.toString());
        str.append("oldNameOfEstablishment=").append(oldNameOfEstablishment == null ? "null" : oldNameOfEstablishment.toString());
        str.append("oldAddress=").append(oldAddress == null ? "null" : oldAddress.toString());
        str.append("oldPhoneNumber=").append(oldPhoneNumber == null ? "null" : oldPhoneNumber.toString());
        str.append("oldHomePhoneNumber=").append(oldHomePhoneNumber == null ? "null" : oldHomePhoneNumber.toString());
        str.append("oldMobileNumber=").append(oldMobileNumber == null ? "null" : oldMobileNumber.toString());
        str.append("oldEmailId=").append(oldEmailId == null ? "null" : oldEmailId.toString());
        str.append("oldUid=").append(oldUid == null ? "null" : oldUid.toString());
        str.append("boundary=").append(boundary == null ? "null" : boundary.toString());
        str.append("}");
        return str.toString();
    }

    @Override
    protected void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    public List<LicenseDocument> getDocuments() {
        return documents;
    }

    public void setDocuments(List<LicenseDocument> documents) {
        this.documents = documents;
    }
    
}
