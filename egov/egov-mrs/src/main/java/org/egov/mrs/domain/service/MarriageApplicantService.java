/* eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It
	   is required that all modified versions of this material be marked in
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program
	   with regards to rights under trademark law for use of the trade names
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.mrs.domain.service;

import static org.egov.mrs.application.MarriageConstants.Aadhar;
import static org.egov.mrs.application.MarriageConstants.Birth_certificate;
import static org.egov.mrs.application.MarriageConstants.Death_certificate;
import static org.egov.mrs.application.MarriageConstants.Divorce_certificate;
import static org.egov.mrs.application.MarriageConstants.Passport;
import static org.egov.mrs.application.MarriageConstants.RationCard;
import static org.egov.mrs.application.MarriageConstants.Schooll_Leaveing_Cert;
import static org.egov.mrs.application.MarriageConstants.TelephoneBill;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

@Service
@Transactional(readOnly = true)
public class MarriageApplicantService {

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
                e.printStackTrace();
            }
        }

        if (applicant.getSignatureFileStore() != null) {
            final File file = fileStoreService.fetch(applicant.getSignatureFileStore().getFileStoreId(),
                    MarriageConstants.FILESTORE_MODULECODE);
            try {
                applicant.setEncodedSignature(Base64.getEncoder().encodeToString(FileCopyUtils.copyToByteArray(file)));
            } catch (final IOException e) {
                e.printStackTrace();
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

                    } catch (final Exception e) {
                        LOG.error("Error while preparing the document for view", e);
                    }
                });
    }

    public void deleteDocuments(final MrApplicant applicantModel, final MrApplicant applicant) {

        final List<MrApplicantDocument> toDelete = new ArrayList<MrApplicantDocument>();
        final Map<Long, MrApplicantDocument> documentIdAndApplicantDoc = new HashMap<Long, MrApplicantDocument>();
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
        if (marriageDocument.getCode().equals(Passport))
            identityProof.setPassport(true);
        if (marriageDocument.getCode().equals(RationCard))
            identityProof.setRationCard(true);
        if (marriageDocument.getCode().equals(Aadhar))
            identityProof.setAadhar(true);
        if (marriageDocument.getCode().equals(Schooll_Leaveing_Cert))
            identityProof.setSchoolLeavingCertificate(true);
        if (marriageDocument.getCode().equals(TelephoneBill))
            identityProof.setTelephoneBill(true);
        if (marriageDocument.getCode().equals(Birth_certificate))
            identityProof.setBirthCertificate(true);
        if (marriageDocument.getCode().equals(Death_certificate))
            identityProof.setDeaceasedDeathCertificate(true);
        if (marriageDocument.getCode().equals(Divorce_certificate))
            identityProof.setDivorceCertificate(true);
        if (mrApplicant.getPhotoFile().getSize() != 0)
            identityProof.setPhotograph(true);
    }

    private FileStoreMapper addToFileStore(final MultipartFile file) {
        FileStoreMapper fileStoreMapper = null;
        try {
            fileStoreMapper = fileStoreService.store(file.getInputStream(), file.getOriginalFilename(),
                    file.getContentType(), MarriageConstants.FILESTORE_MODULECODE);
        } catch (final Exception e) {
            throw new ApplicationRuntimeException("Error occurred while getting inputstream", e);
        }
        return fileStoreMapper;
    }
}
