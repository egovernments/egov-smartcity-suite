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
package org.egov.restapi.web.controller.marriage;

import static org.egov.mrs.application.MarriageConstants.MAX_SIZE;
import static org.egov.mrs.application.MarriageConstants.getMarriageVenues;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.egov.commons.service.EducationalQualificationService;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.repository.BoundaryRepository;
import org.egov.infra.admin.master.repository.BoundaryTypeRepository;
import org.egov.infra.utils.StringUtils;
import org.egov.mrs.masters.service.ReligionService;
import org.egov.restapi.web.contracts.marriageregistration.MarriageDocumentUpload;
import org.egov.restapi.web.contracts.marriageregistration.MarriageRegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class MarriageAPIValidator implements Validator {
    
    private static final String MOBILE_PATTERN = "[0-9]{10}";
    private Pattern pattern= Pattern.compile(MOBILE_PATTERN);
    private Matcher matcher;

    @Autowired
    private BoundaryTypeRepository boundaryTypeRepository;
    @Autowired
    private BoundaryRepository boundaryRepository;
    @Autowired
    private ReligionService religionService;
    @Autowired
    private EducationalQualificationService educationalQualificationService;

    @Override
    public boolean supports(Class<?> clazz) {
        return MarriageRegistrationRequest.class.equals(clazz);
    }
    
    @Override
    public void validate(final Object target, final Errors errors) {

        final MarriageRegistrationRequest marriageRegistrationRequest = (MarriageRegistrationRequest) target;     
        validateBrideGroomInformation(marriageRegistrationRequest,errors);
        validateBrideInformation(marriageRegistrationRequest,errors);

        if (marriageRegistrationRequest.getLocality() != null) {
            BoundaryType locality = boundaryTypeRepository.findByNameAndHierarchyTypeName("Locality", "LOCATION");
            Boundary childBoundary = boundaryRepository.findByBoundaryTypeAndBoundaryNum(locality,
                    marriageRegistrationRequest.getLocality());
            if (childBoundary == null)
                errors.rejectValue("locality", "Invalid Locality", "Invalid Locality");
        }

        if (marriageRegistrationRequest.getHusbandName() != null
                && StringUtils.isBlank(marriageRegistrationRequest.getHusbandName().getFirstName())) {
            errors.rejectValue("husbandName.firstName", "Husband firstName cannot be empty", "Husband firstName cannot be empty");
        } else if (!isCorrectNamePattern(new String[] { marriageRegistrationRequest.getHusbandName().getFirstName(),
                marriageRegistrationRequest.getHusbandName().getMiddleName(),
                marriageRegistrationRequest.getHusbandName().getLastName() })) {
            errors.rejectValue("husbandName", "Husband firstName/MiddleName/LastName must contain alphabets only",
                    "Husband firstName/MiddleName/LastName must contain alphabets only");
        }

        if (marriageRegistrationRequest.getWifeName() != null
                && StringUtils.isBlank(marriageRegistrationRequest.getWifeName().getFirstName())) {
            errors.rejectValue("wifeName.firstName", "Wife firstName cannot be empty", "Wife firstName cannot be empty");
        } else if (!isCorrectNamePattern(new String[] { marriageRegistrationRequest.getWifeName().getFirstName(),
                marriageRegistrationRequest.getWifeName().getMiddleName(),
                marriageRegistrationRequest.getWifeName().getLastName() })) {
            errors.rejectValue("wifeName", "Wife firstName/MiddleName/LastName must contain alphabets only",
                    "Wife firstName/MiddleName/LastName must contain alphabets only");
        }

        if (marriageRegistrationRequest.getHusbandreligion() != null
                && religionService.getReligion(marriageRegistrationRequest.getHusbandreligion()) == null) {
            errors.rejectValue("husbandreligion", "Invalid bridegroom's religion", "Invalid bridegroom's religion");
        }      
        if (marriageRegistrationRequest.getWifereligion() != null
                && religionService.getReligion(marriageRegistrationRequest.getWifereligion()) == null) {
            errors.rejectValue("wifereligion", "Invalid bride's religion", "Invalid bride's religion");
        }
        if (marriageRegistrationRequest.getHusbandQualification() != null &&
                educationalQualificationService.findByName(marriageRegistrationRequest.getHusbandQualification()) == null) {
            errors.rejectValue("husbandQualification", "Invalid bridegroom's education qualification",
                    "Invalid bridegroom's education qualification");
        }
        if (marriageRegistrationRequest.getWifeQualification() != null &&
                educationalQualificationService.findByName(marriageRegistrationRequest.getWifeQualification()) == null) {
            errors.rejectValue("wifeQualification", "Invalid bride's education qualification",
                    "Invalid bride's education qualification");
        }
        if (marriageRegistrationRequest.getVenue() != null
                && !getMarriageVenues().contains(marriageRegistrationRequest.getVenue())) {
            errors.rejectValue("venue", "Invalid Venue Type",
                    "venue should be in [Residence,Function Hall,Worship Place,Others ");
        }

    }
    
    public boolean isCorrectNamePattern(String[] applicantNames) {
        for (String applicantName : applicantNames) {
            if (!StringUtils.isBlank(applicantName)) {
                CharSequence inputStr = applicantName;
                Pattern namePattern = Pattern.compile("^[a-zA-Z\\s]*$", Pattern.CASE_INSENSITIVE);
                matcher = namePattern.matcher(inputStr);
                if (!matcher.matches())
                    return false;
            }
        }
        return true;

    }

    private void validateBrideGroomInformation(final MarriageRegistrationRequest registration, final Errors errors) {
        if (registration.getHusbnadAgeInYearsAsOnMarriage() != null
                && registration.getHusbnadAgeInYearsAsOnMarriage() < 21)
            errors.rejectValue("husbnadAgeInYearsAsOnMarriage", "BrideGroom age should be more than 21 yrs",
                    "BrideGroom should be more than 21 yrs");
        if (registration.getHusbandAgeInMonthsAsOnMarriage() != null
                && registration.getHusbandAgeInMonthsAsOnMarriage() > 12)
            errors.rejectValue("husbandAgeInMonthsAsOnMarriage", "Invalid month for BrideGroom", "Invalid month for BrideGroom");
        validateBrideGroomMobileNo(registration, errors);
    }

    private void validateBrideInformation(final MarriageRegistrationRequest registration, final Errors errors) {
        if (registration.getWifeAgeInYearsAsOnMarriage() != null
                && registration.getWifeAgeInYearsAsOnMarriage() < 18)
            errors.rejectValue("wifeAgeInYearsAsOnMarriage", "Bride age should be more than 18 yrs",
                    "Bride age should be more than 18 yrs");
        if (registration.getWifeAgeInMonthsAsOnMarriage() != null
                && registration.getWifeAgeInMonthsAsOnMarriage() > 12)
            errors.rejectValue("wifeAgeInMonthsAsOnMarriage", "Invalid month for bride", "Invalid month for bride");
        validateBrideMobileNo(registration, errors);
    }

    public void validateBrideMobileNo(final MarriageRegistrationRequest marriageRegistrationRequest, final Errors errors) {
        if (marriageRegistrationRequest.getWifeContactInfo().getMobileNo() != null) {
            matcher = pattern.matcher(marriageRegistrationRequest.getWifeContactInfo().getMobileNo());
            if (!matcher.matches() || marriageRegistrationRequest.getWifeContactInfo().getMobileNo().length() != 10)
                errors.rejectValue("wifeContactInfo.mobileNo", "Invalid MobileNo. for Bride", "Invalid MobileNo. for Bride");
        }
    }

    public void validateBrideGroomMobileNo(final MarriageRegistrationRequest marriageRegistrationRequest, final Errors errors) {
        if (marriageRegistrationRequest.getHusbandContactInfo().getMobileNo() != null) {
            matcher = pattern.matcher(marriageRegistrationRequest.getHusbandContactInfo().getMobileNo());
            if (!matcher.matches() || marriageRegistrationRequest.getWifeContactInfo().getMobileNo().length() != 10)
                errors.rejectValue("husbandContactInfo.mobileNo", "Invalid MobileNo. for BrideGroom",
                        "Invalid MobileNo. for BrideGroom");
        }
    }

    public void validateDocuments(final Object target, final Errors errors) {

        final MarriageDocumentUpload marriageDocumentUpload = (MarriageDocumentUpload) target;

        if (marriageDocumentUpload.getMarriagePhotoFile() == null) {
            errors.rejectValue("marriagePhotoFile", "Provide Marriage Photo", "Provide Marriage Photo");
        }
        if (marriageDocumentUpload.getHusbandPhotoFile() == null) {
            errors.rejectValue("husbandPhotoFile", "Provide husband Photo", "Provide husband Photo");
        }
        if (marriageDocumentUpload.getWifePhotoFile() == null) {
            errors.rejectValue("wifePhotoFile", "Provide wife Photo", "Provide wife Photo");
        }   
        if (marriageDocumentUpload.getDataSheet() != null && marriageDocumentUpload.getDataSheet().getSize() > MAX_SIZE) {
            errors.rejectValue("dataSheet", "Max Upload size exceeded(2 MB)","Max Upload size exceeded(2 MB)");
        }
    }

}
