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
package org.egov.infra.microservice.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.egov.infra.persistence.entity.enums.UserType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserRequest {

    private Long id;
    private String userName;
    private String salutation;
    private String name;
    private String gender;
    private String mobileNumber;
    private String emailId;
    private String altContactNumber;
    private String pan;
    private String aadhaarNumber;
    private String permanentAddress;
    private String permanentCity;
    private String permanentPinCode;
    private String correspondenceAddress;
    private String correspondenceCity;
    private String correspondencePinCode;
    private Boolean active;
    private String locale;
    private UserType type;
    private Boolean accountLocked;
    private String fatherOrHusbandName;
    private String signature;
    private String bloodGroup;
    private String photo;
    private String identificationMark;
    private Long createdBy;
    private String password;
    private String otpReference;
    private Long lastModifiedBy;
    private String tenantId;
    private List<RoleRequest> roles;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
    private Date createdDate;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
    private Date lastModifiedDate;
    @JsonFormat(pattern = "MM/dd/YYYY")
    private Date dob;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
    private Date pwdExpiryDate;

    public UserRequest() {
    }

    public UserRequest(final org.egov.infra.admin.master.entity.User user, final String tenantId) {

        id = user.getId();
        userName = user.getUsername();
        salutation = user.getSalutation();
        name = user.getName();
        gender = user.getGender() != null ? user.getGender().toString() : null;
        mobileNumber = user.getMobileNumber();
        emailId = user.getEmailId();
        altContactNumber = user.getAltContactNumber();
        pan = user.getPan();
        aadhaarNumber = user.getAadhaarNumber();
        active = user.isActive();
        dob = user.getDob();
        password = user.getPassword();
        pwdExpiryDate = user.getPwdExpiryDate().toDate();
        locale = user.getLocale();
        type = user.getType();
        accountLocked = user.isAccountLocked();
        createdBy = user.getCreatedBy() == null ? 0l : user.getCreatedBy().getId();
        createdDate = user.getCreatedDate();
        lastModifiedBy = user.getLastModifiedBy() == null ? 0l : user.getLastModifiedBy().getId();
        lastModifiedDate = user.getLastModifiedDate();
        this.tenantId = tenantId;
        roles = convertRoleEntitiesToContract(user.getRoles());
    }

    private List<RoleRequest> convertRoleEntitiesToContract(final Set<org.egov.infra.admin.master.entity.Role> domainRoles) {
        if (domainRoles == null)
            return new ArrayList<>();
        return domainRoles.stream().map(RoleRequest::new).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getSalutation() {
        return salutation;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getAltContactNumber() {
        return altContactNumber;
    }

    public String getPan() {
        return pan;
    }

    public String getAadhaarNumber() {
        return aadhaarNumber;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public String getPermanentCity() {
        return permanentCity;
    }

    public String getPermanentPinCode() {
        return permanentPinCode;
    }

    public String getCorrespondenceAddress() {
        return correspondenceAddress;
    }

    public String getCorrespondenceCity() {
        return correspondenceCity;
    }

    public String getCorrespondencePinCode() {
        return correspondencePinCode;
    }

    public Boolean getActive() {
        return active;
    }

    public String getLocale() {
        return locale;
    }

    public UserType getType() {
        return type;
    }

    public Boolean getAccountLocked() {
        return accountLocked;
    }

    public String getFatherOrHusbandName() {
        return fatherOrHusbandName;
    }

    public String getSignature() {
        return signature;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public String getPhoto() {
        return photo;
    }

    public String getIdentificationMark() {
        return identificationMark;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public String getPassword() {
        return password;
    }

    public String getOtpReference() {
        return otpReference;
    }

    public Long getLastModifiedBy() {
        return lastModifiedBy;
    }

    public String getTenantId() {
        return tenantId;
    }

    public List<RoleRequest> getRoles() {
        return roles;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Date getDob() {
        return dob;
    }

    public Date getPwdExpiryDate() {
        return pwdExpiryDate;
    }

}
