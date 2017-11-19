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

package org.egov.council.web.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.egov.council.entity.CouncilMember;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * @author vinoth
 *
 */
@Component
public class CouncilMemberValidator implements Validator {

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String MOBILE_PATTERN = "[0-9]{10}";
    private Pattern pattern;
    private Matcher matcher;

    @Override
    public boolean supports(Class<?> clazz) {
        return CouncilMember.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        CouncilMember member = (CouncilMember) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "designation", "notempty.cncl.designation");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "qualification", "notempty.cncl.qualification");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "notempty.cncl.member.name");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "gender", "notempty.cncl.gender");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "mobileNumber", "notempty.cncl.mobileNumber");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "birthDate", "notempty.cncl.birthDate");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "emailId", "notempty.cncl.emailId");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "residentialAddress", "notempty.cncl.residentialAddress");

        if (member != null && member.getDesignation() != null
                && "Co-Option".equalsIgnoreCase(member.getDesignation().getName())) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "category", "notempty.cncl.category");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "caste", "notempty.cncl.caste");
        } else if (member != null && member.getDesignation() != null
                && "Special Officer".equalsIgnoreCase(member.getDesignation().getName())) {
            // In case of special officer there is no extra validation is to do.
        } else {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "caste", "notempty.cncl.caste");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "electionDate", "notempty.cncl.electionDate");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "oathDate", "notempty.cncl.oathDate");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "partyAffiliation", "notempty.cncl.partyAffiliation");
        }

        validateMobileNumber(errors, member);

        validateEmail(errors, member);

        validateOathAndElectionDate(errors, member);
    }

    private void validateOathAndElectionDate(Errors errors, CouncilMember member) {
        if (member != null && member.getElectionDate() != null && member.getOathDate() != null
                && member.getOathDate().compareTo(member.getElectionDate()) < 0) {
            errors.rejectValue("oathDate", "validate.oathdate");
        }
    }

    private void validateEmail(Errors errors, CouncilMember member) {
        if (member != null && member.getEmailId() != null) {
            pattern = Pattern.compile(EMAIL_PATTERN);
            matcher = pattern.matcher(member.getEmailId());
            if (!matcher.matches()) {
                errors.rejectValue("emailId", "validate.email.id");
            }
        }
    }

    private void validateMobileNumber(Errors errors, CouncilMember member) {
        if (member != null && member.getMobileNumber() != null) {
            pattern = Pattern.compile(MOBILE_PATTERN);
            matcher = pattern.matcher(member.getMobileNumber());
            if (!matcher.matches()) {
                errors.rejectValue("mobileNumber", "validate.mobile.no");
            }
        }
    }

}
