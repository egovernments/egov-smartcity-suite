/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.
    Copyright (C) <2015>  eGovernments Foundation
    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .
    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:
        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.
        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.
        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.
  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.bpa.application.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.egov.bpa.application.entity.enums.StakeHolderType;
import org.egov.infra.admin.master.entity.User;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "EGBPA_MSTR_STAKEHOLDER")
public class StakeHolder extends User {

    private static final long serialVersionUID = 3078684328383202788L;
    @Enumerated(EnumType.ORDINAL)
    @NotNull
    private StakeHolderType stakeHolderType;

    // ISACTIVE, RULES.. BELONG TO WHICH WARD/DEPARTMENT
    @NotNull
    @Length(min = 1, max = 128)
    @Column(name = "code", unique = true)
    private String code;

    @NotNull
    @Length(min = 1, max = 64)
    private String businessLicenceNumber;
    @NotNull
    @Temporal(value = TemporalType.DATE)
    private Date businessLicenceDueDate;
    @Length(min = 1, max = 64)
    private String coaEnrolmentNumber;
    @Temporal(value = TemporalType.DATE)
    private Date coaEnrolmentDueDate;
    private Boolean isEnrolWithLocalBody;
    @Length(min = 1, max = 128)
    private String organizationName;
    @Length(min = 1, max = 128)
    private String organizationAddress;
    @Length(min = 1, max = 64)
    private String organizationUrl;
    @Length(min = 1, max = 15)
    private String organizationMobNo;
    private Boolean isOnbehalfOfOrganization;
    @NotNull
    private Boolean isActive;
    @Length(max = 11)
    private String tinNumber;

    @OneToMany(mappedBy = "stakeHolder", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<StakeHolderDocument> stakeHolderDocument = new ArrayList<>(0);

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(final Boolean isActive) {
        this.isActive = isActive;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public StakeHolderType getStakeHolderType() {
        return stakeHolderType;
    }

    public void setStakeHolderType(final StakeHolderType stakeHolderType) {
        this.stakeHolderType = stakeHolderType;
    }

    public String getBusinessLicenceNumber() {
        return businessLicenceNumber;
    }

    public void setBusinessLicenceNumber(final String businessLicenceNumber) {
        this.businessLicenceNumber = businessLicenceNumber;
    }

    public String getCoaEnrolmentNumber() {
        return coaEnrolmentNumber;
    }

    public void setCoaEnrolmentNumber(final String coaEnrolmentNumber) {
        this.coaEnrolmentNumber = coaEnrolmentNumber;
    }

    public Boolean getIsEnrolWithLocalBody() {
        return isEnrolWithLocalBody;
    }

    public void setIsEnrolWithLocalBody(final Boolean isEnrolWithLocalBody) {
        this.isEnrolWithLocalBody = isEnrolWithLocalBody;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(final String organizationName) {
        this.organizationName = organizationName;
    }

    public String getOrganizationAddress() {
        return organizationAddress;
    }

    public void setOrganizationAddress(final String organizationAddress) {
        this.organizationAddress = organizationAddress;
    }

    public String getOrganizationUrl() {
        return organizationUrl;
    }

    public void setOrganizationUrl(final String organizationUrl) {
        this.organizationUrl = organizationUrl;
    }

    public String getOrganizationMobNo() {
        return organizationMobNo;
    }

    public void setOrganizationMobNo(final String organizationMobNo) {
        this.organizationMobNo = organizationMobNo;
    }

    public Boolean getIsOnbehalfOfOrganization() {
        return isOnbehalfOfOrganization;
    }

    public void setIsOnbehalfOfOrganization(final Boolean isOnbehalfOfOrganization) {
        this.isOnbehalfOfOrganization = isOnbehalfOfOrganization;
    }

    public String getTinNumber() {
        return tinNumber;
    }

    public void setTinNumber(final String tinNumber) {
        this.tinNumber = tinNumber;
    }

    public Date getBusinessLicenceDueDate() {
        return businessLicenceDueDate;
    }

    public void setBusinessLicenceDueDate(final Date businessLicenceDueDate) {
        this.businessLicenceDueDate = businessLicenceDueDate;
    }

    public List<StakeHolderDocument> getStakeHolderDocument() {
        return stakeHolderDocument;
    }

    public Date getCoaEnrolmentDueDate() {
        return coaEnrolmentDueDate;
    }

    public void setCoaEnrolmentDueDate(final Date coaEnrolmentDueDate) {
        this.coaEnrolmentDueDate = coaEnrolmentDueDate;
    }

}