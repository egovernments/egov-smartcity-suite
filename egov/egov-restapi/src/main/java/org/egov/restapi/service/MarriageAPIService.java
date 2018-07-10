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
package org.egov.restapi.service;

import static org.egov.mrs.application.MarriageConstants.INDAIN_NATIONALITY;
import static org.egov.mrs.application.MarriageConstants.LOCATION_HIERARCHY_TYPE;

import java.util.HashMap;
import java.util.Map;

import org.egov.commons.repository.NationalityRepository;
import org.egov.commons.service.EducationalQualificationService;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.repository.BoundaryRepository;
import org.egov.infra.admin.master.repository.BoundaryTypeRepository;
import org.egov.infra.utils.DateUtils;
import org.egov.mrs.domain.entity.IdentityProof;
import org.egov.mrs.domain.entity.MarriageRegistration;
import org.egov.mrs.domain.entity.MrApplicant;
import org.egov.mrs.domain.entity.MrApplicantDocument;
import org.egov.mrs.domain.entity.RegistrationDocument;
import org.egov.mrs.domain.service.MarriageApplicantService;
import org.egov.mrs.domain.service.MarriageDocumentService;
import org.egov.mrs.domain.service.MarriageRegistrationService;
import org.egov.mrs.masters.service.ReligionService;
import org.egov.restapi.web.contracts.marriageregistration.MarriageRegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class MarriageAPIService {

    private static final String AADHAR = "Aadhar";
    private static final String RATION_CARD = "Ration Card";
    private static final String BIRTH_CERTIFICATE = "Birth Certificate";
    @Autowired
    private MarriageRegistrationService marriageRegistrationService;
    @Autowired
    private EducationalQualificationService educationalQualificationService;
    @Autowired
    private BoundaryTypeRepository boundaryTypeRepository;
    @Autowired
    private BoundaryRepository boundaryRepository;
    @Autowired
    private ReligionService religionService;
    @Autowired
    private NationalityRepository nationalityRepository;
    @Autowired
    private MarriageApplicantService marriageApplicantService;
    @Autowired
    private MarriageDocumentService marriageDocumentService;

    public MarriageRegistration createMarriageRegistration(MarriageRegistrationRequest marriageRegistrationRequest) {
        final MarriageRegistration marriageRegistration = new MarriageRegistration();
        MrApplicant husband = new MrApplicant();
        MrApplicant wife = new MrApplicant();

        // setting husband details
        husband.setName(marriageRegistrationRequest.getHusbandName());
        husband.setReligion(religionService.getReligion(marriageRegistrationRequest.getHusbandreligion()));
        husband.setAadhaarNo(marriageRegistrationRequest.getHusbandAadhaarNo());
        husband.setParentsName(marriageRegistrationRequest.getHusbandparentsName());
        husband.setAgeInMonthsAsOnMarriage(marriageRegistrationRequest.getHusbandAgeInMonthsAsOnMarriage());
        husband.setAgeInYearsAsOnMarriage(marriageRegistrationRequest.getHusbnadAgeInYearsAsOnMarriage());
        husband.setOccupation(marriageRegistrationRequest.getHusbandOccupation());
        husband.setContactInfo(marriageRegistrationRequest.getHusbandContactInfo());
        husband.setCity(marriageRegistrationRequest.getHusbandCity());
        husband.setStreet(marriageRegistrationRequest.getHusbandStreet());
        husband.setLocality(marriageRegistrationRequest.getHusbandLocality());
        husband.setQualification(
                educationalQualificationService.findByName(marriageRegistrationRequest.getHusbandQualification()));
        husband.setHandicapped(marriageRegistrationRequest.isHusbandHandicapped());
        husband.setMaritalStatus(marriageRegistrationRequest.getHusbandMaritalStatus());
        husband.setNationality(nationalityRepository.findByName(INDAIN_NATIONALITY).get(0));

        // setting wife details
        wife.setName(marriageRegistrationRequest.getWifeName());
        wife.setReligion(religionService.getReligion(marriageRegistrationRequest.getWifereligion()));
        wife.setAadhaarNo(marriageRegistrationRequest.getWifeAadhaarNo());
        wife.setParentsName(marriageRegistrationRequest.getWifeparentsName());
        wife.setAgeInMonthsAsOnMarriage(marriageRegistrationRequest.getWifeAgeInMonthsAsOnMarriage());
        wife.setAgeInYearsAsOnMarriage(marriageRegistrationRequest.getWifeAgeInYearsAsOnMarriage());
        wife.setOccupation(marriageRegistrationRequest.getWifeOccupation());
        wife.setContactInfo(marriageRegistrationRequest.getWifeContactInfo());
        wife.setCity(marriageRegistrationRequest.getWifeCity());
        wife.setStreet(marriageRegistrationRequest.getWifeStreet());
        wife.setLocality(marriageRegistrationRequest.getWifeLocality());
        wife.setQualification(educationalQualificationService.findByName(marriageRegistrationRequest.getWifeQualification()));
        wife.setMaritalStatus(marriageRegistrationRequest.getWifeMaritalStatus());
        wife.setHandicapped(marriageRegistrationRequest.isWifeHandicapped());
        wife.setNationality(nationalityRepository.findByName(INDAIN_NATIONALITY).get(0));

        // setting applicant details
        marriageRegistration.setApplicationDate(DateUtils.now());
        marriageRegistration.setCity(marriageRegistrationRequest.getCity());
        marriageRegistration.setStreet(marriageRegistrationRequest.getStreet());
        BoundaryType locality = boundaryTypeRepository.findByNameAndHierarchyTypeName("Locality",
                LOCATION_HIERARCHY_TYPE);
        Boundary childBoundary = boundaryRepository.findByBoundaryTypeAndBoundaryNum(locality,
                marriageRegistrationRequest.getLocality());
        marriageRegistration.setLocality(childBoundary);
        marriageRegistration.setDateOfMarriage(DateUtils.startOfDay(marriageRegistrationRequest.getDateOfMarriage()));
        marriageRegistration.setVenue(marriageRegistrationRequest.getVenue());
        marriageRegistration.setPlaceOfMarriage(marriageRegistrationRequest.getPlaceOfMarriage());
        marriageRegistration.setHusband(husband);
        marriageRegistration.setWife(wife);
        return marriageRegistrationService.createRegistrationAPI(marriageRegistration);
    }

    public void uploadApplicantPhoto(final MultipartFile marriagePhotoFile, final MultipartFile husbandPhotoFile,
            final MultipartFile wifePhotoFile, final MarriageRegistration marriageRegistration) {
        if (marriagePhotoFile.getSize() != 0) {
            marriageRegistration.setMarriagePhotoFileStore(marriageRegistrationService.addToFileStore(marriagePhotoFile));
        }
        if (wifePhotoFile.getSize() != 0) {
            marriageRegistration.getWife().setPhotoFileStore(marriageRegistrationService.addToFileStore(wifePhotoFile));
        }
        if (husbandPhotoFile.getSize() != 0) {
            marriageRegistration.getHusband().setPhotoFileStore(marriageRegistrationService.addToFileStore(husbandPhotoFile));
        }
    }

    public void uploadMarriageDocument(final MultipartFile memorandumOfMarriage,
            final MarriageRegistration marriageRegistration) {
        if (memorandumOfMarriage.getSize() != 0) {
            RegistrationDocument registrationDocument = new RegistrationDocument();
            registrationDocument.setRegistration(marriageRegistration);
            registrationDocument.setDocument(marriageDocumentService.get("Memorandum of Marriage"));
            registrationDocument.setFileStoreMapper(marriageRegistrationService.addToFileStore(memorandumOfMarriage));
            marriageRegistration.addRegistrationDocument(registrationDocument);
            marriageRegistrationService.setCommonDocumentsFalg(marriageRegistration,
                    marriageDocumentService.get("Memorandum of Marriage"));
        }
    }
    
    public void uploadDataSheet(final MultipartFile dataSheet, final MarriageRegistration marriageRegistration) {
        if (dataSheet.getSize() != 0) {
            marriageRegistration.setDatasheetFileStore(marriageRegistrationService.addToFileStore(dataSheet));
        }
    }

    public void uploadApplicantDocuments(final MultipartFile husbandBirthCertificate, final MultipartFile husbandRationCard,
            final MultipartFile husbandAadhar, final MultipartFile wifeBirthCertificate, final MultipartFile wifeRationCard,
            final MultipartFile wifeAadhar, final MarriageRegistration marriageRegistration) {
        MrApplicant husband = marriageRegistration.getHusband();
        MrApplicant wife = marriageRegistration.getWife();
        final Map<String, MultipartFile> husbandDocuments = new HashMap<>();
        final Map<String, MultipartFile> wifeDocuments = new HashMap<>();

        husbandDocuments.put(BIRTH_CERTIFICATE, husbandBirthCertificate);
        husbandDocuments.put(RATION_CARD, husbandRationCard);
        husbandDocuments.put(AADHAR, husbandAadhar);

        wifeDocuments.put(BIRTH_CERTIFICATE, wifeBirthCertificate);
        wifeDocuments.put(RATION_CARD, wifeRationCard);
        wifeDocuments.put(AADHAR, wifeAadhar);
        
        for (Map.Entry<String, MultipartFile> husbandDocument : husbandDocuments.entrySet()) {
            if (husbandDocument.getValue() != null && !husbandDocument.getValue().isEmpty()) {
            MrApplicantDocument applicantDocument = new MrApplicantDocument();
            applicantDocument.setApplicant(husband);
            applicantDocument.setDocument(marriageDocumentService.get(husbandDocument.getKey()));
            applicantDocument.setFileStoreMapper(marriageRegistrationService.addToFileStore(husbandDocument.getValue()));
            husband.addApplicantDocument(applicantDocument);
                IdentityProof identityProof = husband.getProofsAttached() == null ? new IdentityProof()
                        : husband.getProofsAttached();     
            husband.setProofsAttached(identityProof);
            marriageApplicantService.setApplicantDocumentsFalg(wife, marriageDocumentService.get(husbandDocument.getKey()),
                    identityProof);
            }
        }

        for (Map.Entry<String, MultipartFile> wifeDocument : wifeDocuments.entrySet()) {
            if (wifeDocument.getValue() != null && !wifeDocument.getValue().isEmpty()) {
            MrApplicantDocument applicantDocument = new MrApplicantDocument();
            applicantDocument.setApplicant(wife);
            applicantDocument.setDocument(marriageDocumentService.get(wifeDocument.getKey()));
            applicantDocument.setFileStoreMapper(marriageRegistrationService.addToFileStore(wifeDocument.getValue()));
            wife.addApplicantDocument(applicantDocument);
                IdentityProof identityProof = wife.getProofsAttached() == null ? new IdentityProof() : wife.getProofsAttached();
            wife.setProofsAttached(identityProof);
            marriageApplicantService.setApplicantDocumentsFalg(wife, marriageDocumentService.get(wifeDocument.getKey()),
                    identityProof);
            }
        }
    }

}
