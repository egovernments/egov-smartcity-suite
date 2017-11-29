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

package org.egov.mrs.domain.service;

import org.apache.log4j.Logger;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.mrs.application.MarriageConstants;
import org.egov.mrs.domain.entity.IdentityProof;
import org.egov.mrs.domain.entity.MarriageDocument;
import org.egov.mrs.domain.entity.MrApplicant;
import org.egov.mrs.domain.entity.MrApplicantDocument;
import org.egov.mrs.domain.repository.MarriageApplicantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.egov.mrs.application.MarriageConstants.AADHAR;
import static org.egov.mrs.application.MarriageConstants.BIRTH_CERTIFICATE;
import static org.egov.mrs.application.MarriageConstants.DEATH_CERTIFICATE;
import static org.egov.mrs.application.MarriageConstants.DIVORCE_CERTIFICATE;
import static org.egov.mrs.application.MarriageConstants.PASSPORT;
import static org.egov.mrs.application.MarriageConstants.RATION_CRAD;
import static org.egov.mrs.application.MarriageConstants.SCHOOL_LEAVING_CERT;
import static org.egov.mrs.application.MarriageConstants.TELEPHONE_BILL;

@Service
@Transactional(readOnly = true)
public class MarriageApplicantService {

    private static final String ERROR_WHILE_PREPARING_THE_DOCUMENT_FOR_VIEW = "Error while preparing the document for view";
    private static final Logger LOG = Logger.getLogger(MarriageRegistrationService.class);

    private final MarriageApplicantRepository applicantRepository;
    @Autowired
    private FileStoreService fileStoreService;
    @Autowired
    private ApplicantDocumentService applicantDocumentService;
    @Autowired
    private MarriageDocumentService marriageDocumentService;

    @Autowired
    public MarriageApplicantService(final MarriageApplicantRepository applicantRepository) {
        this.applicantRepository = applicantRepository;
    }

    @Transactional
    public void createApplicant(final MrApplicant applicant) {
        applicantRepository.save(applicant);
    }

    @Transactional
    public MrApplicant updateApplicant(final MrApplicant applicant) {
        return applicantRepository.saveAndFlush(applicant);
    }

    public MrApplicant getApplicant(final Long id) {
        return applicantRepository.findById(id);
    }

    public void prepareDocumentsForView(final MrApplicant applicant) {

        if (applicant.getPhotoFileStore() != null) {
            final File file = fileStoreService.fetch(applicant.getPhotoFileStore().getFileStoreId(),
                    MarriageConstants.FILESTORE_MODULECODE);
            try {
                applicant.setEncodedPhoto(Base64.getEncoder().encodeToString(FileCopyUtils.copyToByteArray(file)));
            } catch (final IOException e) {
                LOG.error(ERROR_WHILE_PREPARING_THE_DOCUMENT_FOR_VIEW, e);
            }
        }

        if (applicant.getSignatureFileStore() != null) {
            final File file = fileStoreService.fetch(applicant.getSignatureFileStore().getFileStoreId(),
                    MarriageConstants.FILESTORE_MODULECODE);
            try {
                applicant.setEncodedSignature(Base64.getEncoder().encodeToString(FileCopyUtils.copyToByteArray(file)));
            } catch (final IOException e) {
                LOG.error(ERROR_WHILE_PREPARING_THE_DOCUMENT_FOR_VIEW, e);
            }
        }

        applicant.getApplicantDocuments().forEach(
                appDoc -> {

                    try {
                        if (appDoc.getFileStoreMapper() != null) {
                            final File file = fileStoreService.fetch(appDoc.getFileStoreMapper().getFileStoreId(),
                                    MarriageConstants.FILESTORE_MODULECODE);
                            appDoc.setBase64EncodedFile(Base64.getEncoder().encodeToString(FileCopyUtils.copyToByteArray(file)));
                        }

                    } catch (final IOException e) {
                        LOG.error(ERROR_WHILE_PREPARING_THE_DOCUMENT_FOR_VIEW, e);
                    }
                });
    }

    public void deleteDocuments(final MrApplicant applicantModel, final MrApplicant applicant) {

        final List<MrApplicantDocument> toDelete = new ArrayList<>();
        final Map<Long, MrApplicantDocument> documentIdAndApplicantDoc = new HashMap<>();
        applicant.getApplicantDocuments().forEach(appDoc -> documentIdAndApplicantDoc.put(appDoc.getDocument().getId(), appDoc));

        if (applicantModel.getDocuments() != null) {
            applicantModel
                    .getDocuments()
                    .stream()
                    .filter(doc -> doc.getFile().getSize() > 0)
                    .map(doc -> {
                        final MrApplicantDocument appDoc = documentIdAndApplicantDoc.get(doc.getId());
                        if (appDoc != null)
                            fileStoreService.delete(appDoc.getFileStoreMapper().getFileStoreId(),
                                    MarriageConstants.FILESTORE_MODULECODE);
                        return appDoc;
                    }).collect(Collectors.toList())
                    .forEach(appDoc -> toDelete.add(appDoc)); // seems like redundent, check
            applicantDocumentService.delete(toDelete);
        }
    }

    /**
     * Adds the uploaded applicant document to file store and associates with the applicant
     *
     * @param applicant
     */
    public void addDocumentsToFileStore(final MrApplicant applicant, final Map<Long, MarriageDocument> documentAndId) {
        final List<MarriageDocument> documents = applicant.getDocuments();
        IdentityProof identityProof;
        if (applicant.getProofsAttached() != null)
            identityProof = applicant.getProofsAttached();
        else
            identityProof = new IdentityProof();

        if (documents != null)
            documents.stream()
                    .filter(document -> !document.getFile().isEmpty() && document.getFile().getSize() > 0)
                    .map(document -> {
                        final MrApplicantDocument applicantDocument = new MrApplicantDocument();
                        setApplicantDocumentsFalg(applicant, document, identityProof);
                        applicantDocument.setApplicant(applicant);
                        applicantDocument.setDocument(documentAndId.get(document.getId()));
                        applicantDocument.setFileStoreMapper(addToFileStore(document.getFile()));
                        return applicantDocument;
                    }).collect(Collectors.toList())
                    .forEach(doc -> applicant.addApplicantDocument(doc));
        applicant.setProofsAttached(identityProof);
    }

    public void setApplicantDocumentsFalg(final MrApplicant mrApplicant, final MarriageDocument document,
            final IdentityProof identityProof) {
        final MarriageDocument marriageDocument = marriageDocumentService.get(document.getId());
        if (marriageDocument.getCode().equals(PASSPORT))
            identityProof.setPassport(true);
        if (marriageDocument.getCode().equals(RATION_CRAD))
            identityProof.setRationCard(true);
        if (marriageDocument.getCode().equals(AADHAR))
            identityProof.setAadhar(true);
        if (marriageDocument.getCode().equals(SCHOOL_LEAVING_CERT))
            identityProof.setSchoolLeavingCertificate(true);
        if (marriageDocument.getCode().equals(TELEPHONE_BILL))
            identityProof.setTelephoneBill(true);
        if (marriageDocument.getCode().equals(BIRTH_CERTIFICATE))
            identityProof.setBirthCertificate(true);
        if (marriageDocument.getCode().equals(DEATH_CERTIFICATE))
            identityProof.setDeaceasedDeathCertificate(true);
        if (marriageDocument.getCode().equals(DIVORCE_CERTIFICATE))
            identityProof.setDivorceCertificate(true);
        if (mrApplicant.getPhotoFile() != null && mrApplicant.getPhotoFile().getSize() != 0)
            identityProof.setPhotograph(true);
    }

    private FileStoreMapper addToFileStore(final MultipartFile file) {
        FileStoreMapper fileStoreMapper = null;
        try {
            fileStoreMapper = fileStoreService.store(file.getInputStream(), file.getOriginalFilename(),
                    file.getContentType(), MarriageConstants.FILESTORE_MODULECODE);
        } catch (final IOException e) {
            throw new ApplicationRuntimeException("Error occurred while getting inputstream", e);
        }
        return fileStoreMapper;
    }
}
