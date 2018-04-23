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

import org.egov.commons.service.EducationalQualificationService;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.repository.BoundaryRepository;
import org.egov.infra.admin.master.repository.BoundaryTypeRepository;
import org.egov.mrs.masters.service.ReligionService;
import org.egov.restapi.web.contracts.marriageregistration.MarriageDocumentUpload;
import org.egov.restapi.web.contracts.marriageregistration.MarriageRegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class MarriageAPIValidator implements Validator {

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

        if (marriageRegistrationRequest.getLocality() != null) {
            BoundaryType locality = boundaryTypeRepository.findByNameAndHierarchyTypeName("Locality", "LOCATION");
            Boundary childBoundary = boundaryRepository.findByBoundaryTypeAndBoundaryNum(locality,
                    marriageRegistrationRequest.getLocality());
            if (childBoundary == null)
                errors.rejectValue("locality", "Invalid Locality", "Invalid Locality");
        }

        if (marriageRegistrationRequest.getHusbandreligion() != null
                && religionService.getReligion(marriageRegistrationRequest.getHusbandreligion()) == null) {
            errors.rejectValue("husbandreligion", "Invalid bridegroom's religion", "Invalid bridegroom's religion");
        }

        if (marriageRegistrationRequest.getHusbandQualification() != null &&
                educationalQualificationService.findByCode(marriageRegistrationRequest.getHusbandQualification()) == null) {
            errors.rejectValue("husbandQualification", "Invalid bridegroom's education qualification",
                    "Invalid bridegroom's education qualification");
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
        if (marriageDocumentUpload.getHusbandBirthCertificate() == null) {
            errors.rejectValue("husbandBirthCertificate", "Provide husband Birth Certificate",
                    "Provide husband Birth Certificate");
        }
        if (marriageDocumentUpload.getHusbandRationCard() == null) {
            errors.rejectValue("husbandRationCard", "Provide husband Ration card", "Provide husband Ration card");
        }
        if (marriageDocumentUpload.getHusbandAadhar() == null) {
            errors.rejectValue("husbandAadhar", "Provide husband Aadhar", "Provide husband Aadhar");
        }

        if (marriageDocumentUpload.getWifeBirthCertificate() == null) {
            errors.rejectValue("wifeBirthCertificate", "Provide wife Birth Certificate", "Provide wife Birth Certificate");
        }
        if (marriageDocumentUpload.getWifeRationCard() == null) {
            errors.rejectValue("wifeRationCard", "Provide wife Ration card", "Provide wife Ration card");
        }
        if (marriageDocumentUpload.getWifeAadhar() == null) {
            errors.rejectValue("wifeAadhar", "Provide wife Aadhar", "Provide wife Aadhar");
        }

    }

}
