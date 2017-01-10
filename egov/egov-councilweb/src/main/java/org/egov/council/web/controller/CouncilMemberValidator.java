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
