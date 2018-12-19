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

package org.egov.tl.entity;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

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
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static org.egov.infra.validation.constants.ValidationErrorCode.INVALID_ADDRESS;
import static org.egov.infra.validation.constants.ValidationErrorCode.INVALID_MOBILE_NUMBER;
import static org.egov.infra.validation.constants.ValidationErrorCode.INVALID_NUMERIC;
import static org.egov.infra.validation.constants.ValidationErrorCode.INVALID_PERSON_NAME;
import static org.egov.infra.validation.constants.ValidationRegex.ADDRESS;
import static org.egov.infra.validation.constants.ValidationRegex.EMAIL;
import static org.egov.infra.validation.constants.ValidationRegex.MOBILE_NUMBER;
import static org.egov.infra.validation.constants.ValidationRegex.NUMERIC;
import static org.egov.infra.validation.constants.ValidationRegex.PERSON_NAME;
import static org.egov.tl.entity.Licensee.SEQ_LICENSEE;

@Entity
@Table(name = "EGTL_LICENSEE")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@SequenceGenerator(name = SEQ_LICENSEE, sequenceName = SEQ_LICENSEE, allocationSize = 1)
public class Licensee extends AbstractAuditable {
    protected static final String SEQ_LICENSEE = "SEQ_EGTL_LICENSEE";
    private static final long serialVersionUID = 6723590685484215531L;

    @Id
    @GeneratedValue(generator = SEQ_LICENSEE, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank
    @Length(max = 256)
    @SafeHtml
    @Column(name = "APPLICANT_NAME")
    @Pattern(regexp = PERSON_NAME, message = INVALID_PERSON_NAME)
    private String applicantName;

    @NotBlank
    @SafeHtml
    @Length(max = 256)
    @Column(name = "FATHER_SPOUSE_NAME")
    @Pattern(regexp = PERSON_NAME, message = INVALID_PERSON_NAME)
    private String fatherOrSpouseName;

    @SafeHtml
    @Length(min = 10, max = 10)
    @Column(name = "MOBILE_PHONENUMBER")
    @Pattern(regexp = MOBILE_NUMBER, message = INVALID_MOBILE_NUMBER)
    private String mobilePhoneNumber;

    @SafeHtml
    @Length(max = 12)
    @Column(name = "UNIQUEID")
    @Pattern(regexp = NUMERIC, message = INVALID_NUMERIC)
    private String uid;

    @NotBlank
    @SafeHtml
    @Length(max = 64)
    @Column(name = "EMAIL_ID")
    @Email(regexp = EMAIL)
    private String emailId;

    @NotBlank
    @Length(max = 250)
    @SafeHtml
    @Pattern(regexp = ADDRESS, message = INVALID_ADDRESS)
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_LICENSE", updatable = false)
    private TradeLicense license;

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    protected void setId(final Long id) {
        this.id = id;
    }

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public void setMobilePhoneNumber(final String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(final String uid) {
        this.uid = uid;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(final String emailId) {
        this.emailId = emailId;
    }

    public TradeLicense getLicense() {
        return license;
    }

    public void setLicense(final TradeLicense license) {
        this.license = license;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(final String applicantName) {
        this.applicantName = applicantName;
    }

    public String getFatherOrSpouseName() {
        return fatherOrSpouseName;
    }

    public void setFatherOrSpouseName(final String fatherOrSpouseName) {
        this.fatherOrSpouseName = fatherOrSpouseName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
