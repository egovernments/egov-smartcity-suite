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
package org.egov.lcms.transactions.entity;

import com.google.gson.annotations.Expose;
import org.apache.commons.lang.StringUtils;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.lcms.masters.entity.GovernmentDepartment;
import org.egov.lcms.utils.constants.LcmsConstants;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "EGLC_BIPARTISANDETAILS")
@SequenceGenerator(name = BipartisanDetails.SEQ_EGLC_BIPARTISANDETAILS, sequenceName = BipartisanDetails.SEQ_EGLC_BIPARTISANDETAILS, allocationSize = 1)
@AuditOverrides({@AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedBy"),
        @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedDate")})
public class BipartisanDetails extends AbstractAuditable {

    public static final String SEQ_EGLC_BIPARTISANDETAILS = "SEQ_EGLC_BIPARTISANDETAILS";

    @Expose
    @Id
    @GeneratedValue(generator = SEQ_EGLC_BIPARTISANDETAILS, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "legalcase")
    @Audited
    private LegalCase legalCase;

    @NotNull
    @Length(max = 128)
    @Audited
    private String name;

    @Length(max = 256)
    @Audited
    private String address;

    @Pattern(regexp = LcmsConstants.numericiValForPhoneNo)
    @Audited
    private String contactNumber;

    @Column(name = "isrespondent")
    @Audited
    private Boolean isRepondent = false;

    @ManyToOne
    @Valid
    @JoinColumn(name = "respondentgovtdept")
    @Audited
    private GovernmentDepartment governmentDepartment;

    @Column(name = "isrespondentgovernment", nullable = false)
    @Audited
    private Boolean isRespondentGovernment = false;

    @Column(name = "serialnumber")
    @Audited
    private Long serialNumber;

    public GovernmentDepartment getGovernmentDepartment() {
        return governmentDepartment;
    }

    public void setGovernmentDepartment(final GovernmentDepartment governmentDepartment) {
        this.governmentDepartment = governmentDepartment;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(final String contactNumber) {
        this.contactNumber = contactNumber;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public List<ValidationError> validate() {
        final List<ValidationError> errors = new ArrayList<ValidationError>();
        if (getIsRespondentGovernment() && getGovernmentDepartment() == null)
            if (getIsRepondent())
                errors.add(new ValidationError("govtDept", "respondent.govtDept.select"));
            else
                errors.add(new ValidationError("govtDept", "petitioner.govtDept.select"));
        if (!getIsRespondentGovernment()) {
            if (getIsRepondent() && StringUtils.isBlank(getName()))
                errors.add(new ValidationError("respondent", "respondentName.null"));
            if (!getIsRepondent() && StringUtils.isBlank(getName()))
                errors.add(new ValidationError("petitioner", "petitionerName.null"));
        }
        return errors;
    }

    public Long getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(final Long serialNumber) {
        this.serialNumber = serialNumber;
    }

    public LegalCase getLegalCase() {
        return legalCase;
    }

    public void setLegalCase(final LegalCase legalCase) {
        this.legalCase = legalCase;
    }

    public Boolean getIsRepondent() {
        return isRepondent;
    }

    public void setIsRepondent(final Boolean isRepondent) {
        this.isRepondent = isRepondent;
    }

    public Boolean getIsRespondentGovernment() {
        return isRespondentGovernment;
    }

    public void setIsRespondentGovernment(final Boolean isRespondentGovernment) {
        this.isRespondentGovernment = isRespondentGovernment;
    }

}