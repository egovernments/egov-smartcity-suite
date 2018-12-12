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

package org.egov.infra.admin.common.contracts;

import org.egov.infra.persistence.entity.enums.Gender;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.util.Date;

import static org.egov.infra.validation.constants.ValidationErrorCode.INVALID_MOBILE_NUMBER;
import static org.egov.infra.validation.constants.ValidationErrorCode.INVALID_PAN_NUMBER;
import static org.egov.infra.validation.constants.ValidationErrorCode.INVALID_PERSON_NAME;
import static org.egov.infra.validation.constants.ValidationErrorCode.INVALID_PHONE_NUMBER;
import static org.egov.infra.validation.constants.ValidationErrorCode.INVALID_SALUTATION;
import static org.egov.infra.validation.constants.ValidationRegex.EMAIL;
import static org.egov.infra.validation.constants.ValidationRegex.MOBILE_NUMBER;
import static org.egov.infra.validation.constants.ValidationRegex.PAN_NUMBER;
import static org.egov.infra.validation.constants.ValidationRegex.PERSON_NAME;
import static org.egov.infra.validation.constants.ValidationRegex.PHONE_NUMBER;
import static org.egov.infra.validation.constants.ValidationRegex.SALUTATION;

public class UserProfile {

    @SafeHtml
    @Length(max = 10)
    @Pattern(regexp = SALUTATION, message = INVALID_SALUTATION)
    private String salutation;

    @NotBlank
    @SafeHtml
    @Length(min = 2, max = 100)
    @Pattern(regexp = PERSON_NAME, message = INVALID_PERSON_NAME)
    private String name;

    @NotNull
    private Gender gender;

    @SafeHtml
    @Length(max = 10)
    @Pattern(regexp = MOBILE_NUMBER, message = INVALID_MOBILE_NUMBER)
    private String mobileNumber;

    @SafeHtml
    @Length(max = 128)
    @Email(regexp = EMAIL)
    private String emailId;

    @SafeHtml
    @Length(max = 15)
    @Pattern(regexp = PHONE_NUMBER, message = INVALID_PHONE_NUMBER)
    private String altContactNumber;

    @SafeHtml
    @Length(max = 10)
    @Pattern(regexp = PAN_NUMBER, message = INVALID_PAN_NUMBER)
    private String pan;

    @Past
    private Date dob;

    @NotBlank
    @Length(max = 15)
    @SafeHtml
    private String locale = "en_IN";

    private boolean useMultiFA;

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getAltContactNumber() {
        return altContactNumber;
    }

    public void setAltContactNumber(String altContactNumber) {
        this.altContactNumber = altContactNumber;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public boolean isUseMultiFA() {
        return useMultiFA;
    }

    public void setUseMultiFA(boolean useMultiFA) {
        this.useMultiFA = useMultiFA;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
