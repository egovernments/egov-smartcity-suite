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

package org.egov.adtax.entity;

import org.egov.adtax.entity.enums.AgencyStatus;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.infra.validation.regex.Constants;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "EGADTAX_AGENCY")
@SequenceGenerator(name = Agency.SEQ_AGENCY, sequenceName = Agency.SEQ_AGENCY, allocationSize = 1)
@Unique(id = "id", tableName = "EGADTAX_AGENCY", columnName = { "code", "name" }, fields = {
        "code", "name"}, enableDfltMsg = true, message = "Already Exist.Value should be unique.")
public class Agency extends AbstractAuditable {
    private static final long serialVersionUID = 4958014584254475596L;
    public static final String SEQ_AGENCY = "SEQ_EGADTAX_AGENCY";
    @Id
    @GeneratedValue(generator = SEQ_AGENCY, strategy = GenerationType.SEQUENCE)
    private Long id;
    @NotNull
    @Column(name = "code", unique = true, updatable = false)
    @SafeHtml
    private String code;
    @NotNull
    @Column(name = "name", unique = true)
    @SafeHtml
    private String name;
    @SafeHtml
    private String ssId;
    @Email(regexp = Constants.EMAIL)
    @Length(max = 128)
    @SafeHtml
    private String emailId;
    @NotNull
    @Pattern(regexp = Constants.MOBILE_NUM)
    @Length(max = 15)
    @SafeHtml
    private String mobileNumber;
    @SafeHtml
    private String address;
    @NotNull
    @Enumerated(EnumType.STRING)
    private AgencyStatus status;

    @NotNull
    private Double depositAmount;

    @OrderBy("ID DESC")
    @OneToMany(mappedBy = "agency", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<AdvertisementPermitDetail> advertisementPermits = new HashSet<AdvertisementPermitDetail>(0);

    public Set<AdvertisementPermitDetail> getAdvertisementPermits() {
        return advertisementPermits;
    }

    public void setAdvertisementPermits(Set<AdvertisementPermitDetail> advertisementPermits) {
        this.advertisementPermits = advertisementPermits;
    }

    @Override
    protected void setId(final Long id) {
        this.id = id;

    }

    @Override
    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getSsId() {
        return ssId;
    }

    public void setSsId(final String ssId) {
        this.ssId = ssId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(final String emailId) {
        this.emailId = emailId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(final String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public AgencyStatus getStatus() {
        return status;
    }

    public void setStatus(final AgencyStatus status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public Double getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(final Double depositAmount) {
        this.depositAmount = depositAmount;
    }

   

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Agency other = (Agency) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

}