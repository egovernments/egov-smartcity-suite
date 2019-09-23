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

import static org.egov.mrs.application.MarriageConstants.APPROVED;
import static org.egov.mrs.application.MarriageConstants.REGISTERED;
import static org.egov.mrs.application.MarriageConstants.REJECTED;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.egov.commons.entity.Source;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.utils.StringUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.mrs.application.service.MarriageCertificateService;
import org.egov.mrs.domain.entity.MarriageCertificate;
import org.egov.mrs.domain.entity.MarriageRegistration;
import org.egov.mrs.domain.service.MarriageRegistrationService;
import org.egov.restapi.service.MarriageAPIService;
import org.egov.restapi.web.contracts.marriageregistration.MarriageCertificateResponse;
import org.egov.restapi.web.contracts.marriageregistration.MarriageDocumentUpload;
import org.egov.restapi.web.contracts.marriageregistration.MarriageRegistrationRequest;
import org.egov.restapi.web.contracts.marriageregistration.MarriageRegistrationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MarriageRegistrationAPIController {

    private static final String MRS_REGISTRATION_PRINTCERTFICATE = "/mrs/registration/printcertficate/";
    private static final String DATE_FORMAT = "dd/MM/yyyy";
    @Autowired
    private MarriageAPIService marriageAPIService;
    @Autowired
    private MarriageAPIValidator marriageAPIValidator;
    @Autowired
    private MarriageRegistrationService marriageRegistrationService;
    @Autowired
    private MarriageCertificateService marriageCertificateService;
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private UserService userService;

    @PostMapping("/marriageregistration/create")
    public MarriageRegistrationResponse createMarriageRegistration(
            @Valid @RequestBody MarriageRegistrationRequest marriageRegistrationRequest,
            BindingResult binding) {
        return create(marriageRegistrationRequest, binding);
    }

    @PostMapping("/v1.0/marriageregistration/create")
    public MarriageRegistrationResponse securedCreateMarriageRegistration(
            @Valid @RequestBody MarriageRegistrationRequest marriageRegistrationRequest,
            BindingResult binding, OAuth2Authentication authentication) {
        return create(marriageRegistrationRequest, binding);
    }

    @RequestMapping(value = "/marriageregistration/getmarriagecertificate/{applicationNumber}", method = RequestMethod.GET)
    public MarriageCertificateResponse getMarriageCertificate(
            @PathVariable final String applicationNumber) {
        return getCertificate(applicationNumber);

    }

    @RequestMapping(value = "/v1.0/marriageregistration/getmarriagecertificate/{applicationNumber}", method = RequestMethod.GET)
    public MarriageCertificateResponse securedGetMarriageCertificate(
            @PathVariable final String applicationNumber, OAuth2Authentication authentication) {
        return getCertificate(applicationNumber);

    }

    @RequestMapping(value = "/marriageregistration/uploaddocuments/{applicationNo}", method = RequestMethod.POST)
    public MarriageRegistrationResponse uploadSupportDocs(@PathVariable final String applicationNo,
            MarriageDocumentUpload marriageDocumentUpload, BindingResult binding) {
        return uploadDocs(applicationNo, marriageDocumentUpload, binding);
    }

    @RequestMapping(value = "/v1.0/marriageregistration/uploaddocuments/{applicationNo}", method = RequestMethod.POST)
    public MarriageRegistrationResponse securedUploadSupportDocs(@PathVariable final String applicationNo,
            MarriageDocumentUpload marriageDocumentUpload, BindingResult binding, OAuth2Authentication authentication) {
        return uploadDocs(applicationNo, marriageDocumentUpload, binding);
    }

    /**
     * @param marriageRegistrationRequest
     * @param binding
     * @return
     */
    public MarriageRegistrationResponse create(MarriageRegistrationRequest marriageRegistrationRequest, BindingResult binding) {
        marriageAPIValidator.validate(marriageRegistrationRequest, binding);
        if (binding.hasErrors()) {
            List<String> marriageRegistrationResponse = binding.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return new MarriageRegistrationResponse(false, HttpStatus.BAD_REQUEST.toString(),
                    marriageRegistrationResponse.toString());
        } else {
            return new MarriageRegistrationResponse(true,
                    marriageAPIService.createMarriageRegistration(marriageRegistrationRequest).getApplicationNo(),
                    HttpStatus.OK.toString(),
                    "Marriage Application Created Successfully");
        }
    }

    /**
     * @param applicationNumber
     * @return
     */
    public MarriageCertificateResponse getCertificate(final String applicationNumber) {
        final MarriageRegistration marriageRegistration = marriageRegistrationService.findByApplicationNo(applicationNumber);
        if (marriageRegistration == null)
            return new MarriageCertificateResponse(StringUtils.EMPTY,
                    "Application Number Does not exist");
        if (marriageRegistration.getStatus().getCode().equals(REGISTERED)
                && marriageRegistration.getSource().equals(Source.CHPK.toString())) {

            MarriageCertificate marriageCertificate = marriageCertificateService.getGeneratedCertificate(marriageRegistration);
            Assignment assignment = assignmentService
                    .getPrimaryAssignmentForUser(
                            userService.getUsersByNameLike(marriageRegistration.getRegistrarName()).get(0).getId());

            return new MarriageCertificateResponse(marriageRegistration.getStatus().getDescription(),
                    ApplicationThreadLocals.getDomainURL() + MRS_REGISTRATION_PRINTCERTFICATE + marriageCertificate.getId(),
                    marriageCertificate.getCertificateNo(),
                    DateUtils.getFormattedDate(marriageCertificate.getCertificateDate(), DATE_FORMAT),
                    marriageRegistration.getRegistrarName(), assignment == null ? "N/A" : assignment.getDesignation().getName(),
                    "Marriage Certificate sent Successfully");
        } else if (marriageRegistration.getStatus().getCode().equals(REJECTED)
                && marriageRegistration.getSource().equals(Source.CHPK.toString())) {
            return new MarriageCertificateResponse(marriageRegistration.getStatus().getDescription(),
                    "Marriage Registration  for Application Number :" + marriageRegistration.getApplicationNo()
                            + " is Rejected",
                    StringUtils.emptyIfNull(marriageRegistration.getRejectionReason()));
        } else
            return new MarriageCertificateResponse(marriageRegistration.getStatus().getDescription(),
                    "Marriage Certificate Not Generated this application");
    }

    /**
     * @param applicationNo
     * @param marriageDocumentUpload
     * @param binding
     * @return
     */
    public MarriageRegistrationResponse uploadDocs(final String applicationNo, MarriageDocumentUpload marriageDocumentUpload,
            BindingResult binding) {
        final MarriageRegistration marriageRegistration = marriageRegistrationService.findByApplicationNo(applicationNo);
        if (marriageRegistration == null)
            return new MarriageRegistrationResponse(false, applicationNo, HttpStatus.BAD_REQUEST.toString(),
                    "Application Number Does not exist");
        if (marriageRegistration.getStatus().getCode().equals(APPROVED) ||
                marriageRegistration.getStatus().getCode().equals(REGISTERED))
            return new MarriageRegistrationResponse(false, applicationNo, HttpStatus.BAD_REQUEST.toString(),
                    "Application is already Processed");
        marriageAPIValidator.validateDocuments(marriageDocumentUpload, binding);
        if (binding.hasErrors()) {
            List<String> marriageRegistrationResponse = binding.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return new MarriageRegistrationResponse(false, HttpStatus.BAD_REQUEST.toString(),
                    marriageRegistrationResponse.toString());
        }

        marriageAPIService.uploadApplicantPhoto(marriageDocumentUpload.getMarriagePhotoFile(),
                marriageDocumentUpload.getHusbandPhotoFile(), marriageDocumentUpload.getWifePhotoFile(), marriageRegistration);
        if (marriageDocumentUpload.getDataSheet() != null) {
            marriageAPIService.uploadDataSheet(marriageDocumentUpload.getDataSheet(), marriageRegistration);
        }
        if (marriageDocumentUpload.getMemorandumOfMarriage() != null) {
            marriageAPIService.uploadMarriageDocument(marriageDocumentUpload.getMemorandumOfMarriage(), marriageRegistration);
        }
        marriageAPIService.uploadApplicantDocuments(marriageDocumentUpload.getHusbandBirthCertificate(),
                marriageDocumentUpload.getHusbandRationCard(),
                marriageDocumentUpload.getHusbandAadhar(), marriageDocumentUpload.getWifeBirthCertificate(),
                marriageDocumentUpload.getWifeRationCard(),
                marriageDocumentUpload.getWifeAadhar(), marriageRegistration);
        marriageRegistrationService.update(marriageRegistration);
        return new MarriageRegistrationResponse(true, applicationNo,
                HttpStatus.OK.toString(),
                "file uploaded Successfully");
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> validationErrors(ValidationException exception) {
        List<String> errors = exception.getErrors().stream().map(ValidationError::getMessage).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MarriageRegistrationResponse(false, HttpStatus.BAD_REQUEST.toString(), errors.toString()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> restErrors() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MarriageRegistrationResponse(false,
                HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Server Error"));
    }

    @ExceptionHandler(ApplicationRuntimeException.class)
    public ResponseEntity<Object> configurationErrors(ApplicationRuntimeException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MarriageRegistrationResponse(false, "", HttpStatus.BAD_REQUEST.toString(), exception.toString()));
    }

}