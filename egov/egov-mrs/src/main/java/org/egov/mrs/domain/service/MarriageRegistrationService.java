/* eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2016>  eGovernments Foundation

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

import static org.egov.mrs.application.MarriageConstants.AFFIDAVIT;
import static org.egov.mrs.application.MarriageConstants.CF_STAMP;
import static org.egov.mrs.application.MarriageConstants.MIC;
import static org.egov.mrs.application.MarriageConstants.MOM;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.egov.commons.EgwStatus;
import org.egov.demand.model.EgDemandDetails;
import org.egov.eis.service.EisCommonService;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.mrs.application.MarriageConstants;
import org.egov.mrs.application.MarriageUtils;
import org.egov.mrs.application.reports.service.MarriageRegistrationReportsService;
import org.egov.mrs.application.service.MarriageCertificateService;
import org.egov.mrs.application.service.MarriageRegistrationDemandService;
import org.egov.mrs.application.service.workflow.RegistrationWorkflowService;
import org.egov.mrs.autonumber.MarriageRegistrationApplicationNumberGenerator;
import org.egov.mrs.autonumber.MarriageRegistrationNumberGenerator;
import org.egov.mrs.domain.entity.MarriageCertificate;
import org.egov.mrs.domain.entity.MarriageDocument;
import org.egov.mrs.domain.entity.MarriageRegistration;
import org.egov.mrs.domain.entity.MarriageRegistrationSearchFilter;
import org.egov.mrs.domain.entity.ReIssue;
import org.egov.mrs.domain.entity.RegistrationDocument;
import org.egov.mrs.domain.entity.SearchModel;
import org.egov.mrs.domain.repository.MarriageRegistrationRepository;
import org.egov.mrs.domain.repository.WitnessRepository;
import org.egov.mrs.service.es.MarriageRegistrationUpdateIndexesService;
import org.egov.pims.commons.Position;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class MarriageRegistrationService {
    private static final String MRG_REGISTRATION_UNIT = "registrationUnit";
    private static final String MARRIAGE_ACKNOWLEDGEMENT_REPORT_FILE = "mrs_acknowledgement";
    private static final String REISSUE_MARRIAGE_CERTIFICATE = "Reissue Marriage Certificate (Duplicate)";
    private static final String NEW_MARRIAGE_REGISTRATION = "New Marriage Registration";
    private static final String APP_TYPE = "appType";
    private static final String APPLICATION_CENTRE = "ApplicationCentre";
    private static final String DUE_DATE = "dueDate";
    private static final String ADDRESS = "address";
    private static final String CURRENT_DATE = "currentDate";
    private static final String ACKNOWLEDGEMENT_NO = "acknowledgementNo";
    private static final String APPLICANT_NAME = "applicantName";
    private static final String ZONE_NAME = "zoneName";
    private static final String CITYNAME = "cityname";
    private static final String MUNICIPALITY = "municipality";
    private static final String OFFICE_S_COPY = "Office's Copy";
    private static final String PARTY_S_COPY = "Party's Copy";
    private static final String USER = "user";
    private static final Logger LOG = Logger.getLogger(MarriageRegistrationService.class);
    private static final String STATUS_DOT_CODE = "status.code";
    private static final String MARRIAGE_REGISTRATION_DOT_APPLICATION_DATE = "marriageRegistration.applicationDate";
    private static final String MARRIAGE_REGISTRATION = "marriageRegistration";
    private static final String DEPARTMENT = "department";
    private static final String STATUS = "status";
    private static final String ERROR_WHILE_COPYING_MULTIPART_FILE_BYTES = "Error while copying Multipart file bytes";

    @Autowired
    private final MarriageRegistrationRepository registrationRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private MarriageSmsAndEmailService marriageSmsAndEmailService;
    @Autowired
    private MarriageRegistrationDemandService marriageRegistrationDemandService;
    @Autowired
    private MarriageRegistrationApplicationNumberGenerator marriageRegistrationApplicationNumberGenerator;
    @Autowired
    private RegistrationWorkflowService workflowService;
    @Autowired
    private MarriageRegistrationNumberGenerator marriageRegistrationNumberGenerator;
    @Autowired
    private FileStoreService fileStoreService;
    @Autowired
    private MarriageDocumentService marriageDocumentService;
    @Autowired
    private MarriageApplicantService marriageApplicantService;
    @Autowired
    private RegistrationDocumentService registrationDocumentService;
    @Autowired
    protected WitnessRepository witnessRepository;
    @Autowired
    private MarriageUtils marriageUtils;
    @Autowired
    private MarriageCertificateService marriageCertificateService;
    @Autowired
    private EisCommonService eisCommonService;
    @Autowired
    private MarriageRegistrationUpdateIndexesService marriageRegistrationUpdateIndexesService;
    @Autowired
    private MarriageRegistrationReportsService marriageRegistrationReportsService;

    @Autowired
    private SecurityUtils securityUtils;
    
    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource marriageMessageSource;

    @Autowired
    private ReportService reportService;

    @Autowired
    public MarriageRegistrationService(final MarriageRegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

    private Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Transactional
    public void create(final MarriageRegistration registration) {
        registrationRepository.save(registration);
    }

    @Transactional
    public MarriageRegistration update(final MarriageRegistration registration) {
        return entityManager.merge(registration);
    }

    public MarriageRegistration get(final Long id) {
        return registrationRepository.findById(id);
    }

    public MarriageRegistration findByRegistrationNo(final String registrationNo) {
        return registrationRepository.findByRegistrationNo(registrationNo);
    }

    public MarriageRegistration findByApplicationNo(final String applicationNo) {
        return registrationRepository.findByApplicationNo(applicationNo);
    }

    public MarriageRegistration findById(final Long id) {
        return registrationRepository.findById(id);
    }

    public void setMarriageRegData(final MarriageRegistration registration) {

        registration.setApplicationDate(new Date());
        registration.getWitnesses().forEach(witness -> witness.setRegistration(registration));
        if (registration.getFeePaid() != null && registration.getDemand() == null)
            registration.setDemand(
                    marriageRegistrationDemandService.createDemand(new BigDecimal(registration.getFeePaid())));
        try {
            registration.getHusband().copyPhotoAndSignatureToByteArray();
            registration.getWife().copyPhotoAndSignatureToByteArray();
        } catch (final IOException e) {
            LOG.error(ERROR_WHILE_COPYING_MULTIPART_FILE_BYTES, e);
        }
        registration.getWitnesses().forEach(witness -> {
            try {
                if (witness.getPhotoFile().getSize() != 0) {
                    witness.setPhoto(FileCopyUtils.copyToByteArray(witness.getPhotoFile().getInputStream()));
                    witness.setPhotoFileStore(addToFileStore(witness.getPhotoFile()));
                }
            } catch (final IOException e) {
                LOG.error(ERROR_WHILE_COPYING_MULTIPART_FILE_BYTES, e);
            }
        });
        registration.setMarriagePhotoFileStore(addToFileStore(registration.getMarriagePhotoFile()));
        if (registration.getWife().getPhotoFile().getSize() != 0)
            registration.getWife().setPhotoFileStore(addToFileStore(registration.getWife().getPhotoFile()));
        if (registration.getHusband().getPhotoFile().getSize() != 0)
            registration.getHusband().setPhotoFileStore(addToFileStore(registration.getHusband().getPhotoFile()));

        final Map<Long, MarriageDocument> generalDocumentAndId = new HashMap<>();
        marriageDocumentService.getGeneralDocuments().forEach(document -> generalDocumentAndId.put(document.getId(), document));

        addDocumentsToFileStore(registration, generalDocumentAndId);

        addMarriageDocumentsToFileStore(registration);
    }

    @Transactional
    public String createRegistration(final MarriageRegistration registration, final WorkflowContainer workflowContainer) {
        if (org.apache.commons.lang.StringUtils.isBlank(registration.getApplicationNo()))
            registration.setApplicationNo(marriageRegistrationApplicationNumberGenerator
                    .getNextApplicationNumberForMarriageRegistration(registration));
        setMarriageRegData(registration);
        registration.setStatus(
                marriageUtils.getStatusByCodeAndModuleType(MarriageRegistration.RegistrationStatus.CREATED.toString(),
                        MarriageConstants.MODULE_NAME));
        create(registration);
        workflowService.transition(registration, workflowContainer, registration.getApprovalComent());
        marriageRegistrationUpdateIndexesService.updateIndexes(registration);
        marriageSmsAndEmailService.sendSMS(registration, MarriageRegistration.RegistrationStatus.CREATED.toString());
        marriageSmsAndEmailService.sendEmail(registration, MarriageRegistration.RegistrationStatus.CREATED.toString());

        return registration.getApplicationNo();
    }

    @Transactional
    public String createDataEntryMrgRegistration(final MarriageRegistration registration) {
        setMarriageRegData(registration);
        if (registration.getDemand() != null)
            registration.getDemand().setAmtCollected(registration.getDemand().getBaseDemand());
        registration.setStatus(
                marriageUtils.getStatusByCodeAndModuleType(MarriageRegistration.RegistrationStatus.REGISTERED.toString(),
                        MarriageConstants.MODULE_NAME));
        if (registration.getDemand() != null && !registration.getDemand().getEgDemandDetails().isEmpty())
            for (final EgDemandDetails dd : registration.getDemand().getEgDemandDetails())
                if (dd != null)
                    dd.setAmtCollected(dd.getAmount());
        registration.setActive(true);
        registration.setLegacy(true);
        create(registration);
        marriageRegistrationUpdateIndexesService.updateIndexes(registration);
        return registration.getApplicationNo();
    }

    @Transactional
    public MarriageRegistration forwardRegistration(final MarriageRegistration marriageRegistration,
            final WorkflowContainer workflowContainer) {
        updateRegistrationdata(marriageRegistration);
        updateDocuments(marriageRegistration);
        marriageRegistration.setStatus(
                marriageUtils.getStatusByCodeAndModuleType(MarriageRegistration.RegistrationStatus.CREATED.toString(),
                        MarriageConstants.MODULE_NAME));

        workflowService.transition(marriageRegistration, workflowContainer, marriageRegistration.getApprovalComent());
        marriageRegistrationUpdateIndexesService.updateIndexes(marriageRegistration);
        return update(marriageRegistration);
    }

    private void updateRegistrationdata(final MarriageRegistration marriageRegistration) {
        if (marriageRegistration.getFeePaid() != null)
            if (marriageRegistration.getDemand() == null)
                marriageRegistration.setDemand(
                        marriageRegistrationDemandService.createDemand(new BigDecimal(marriageRegistration.getFeePaid())));
            else
                marriageRegistrationDemandService.updateDemand(marriageRegistration.getDemand(),
                        new BigDecimal(marriageRegistration.getFeePaid()));
        try {
            marriageRegistration.getHusband().isCopyFilesToByteArray();
            marriageRegistration.getWife().copyPhotoAndSignatureToByteArray();

        } catch (final IOException e) {
            LOG.error(ERROR_WHILE_COPYING_MULTIPART_FILE_BYTES, e);
        }
        marriageRegistration.getWitnesses().forEach(witness -> {
            try {
                witness.setPhoto(FileCopyUtils.copyToByteArray(witness.getPhotoFile().getInputStream()));
            } catch (final IOException e) {
                LOG.error(ERROR_WHILE_COPYING_MULTIPART_FILE_BYTES, e);
            }
            witness.setPhotoFileStore(addToFileStore(witness.getPhotoFile()));

        });
        if (marriageRegistration.getMarriagePhotoFile().getSize() != 0)
            marriageRegistration.setMarriagePhotoFileStore(addToFileStore(marriageRegistration.getMarriagePhotoFile()));
        if (marriageRegistration.getHusband().getPhotoFile().getSize() != 0)
            marriageRegistration.getHusband().setPhotoFileStore(addToFileStore(marriageRegistration.getHusband().getPhotoFile()));
        if (marriageRegistration.getWife().getPhotoFile().getSize() != 0)
            marriageRegistration.getWife().setPhotoFileStore(addToFileStore(marriageRegistration.getWife().getPhotoFile()));
    }

    @Transactional
    public MarriageRegistration updateRegistration(final MarriageRegistration marriageRegistration) {
        updateRegistrationdata(marriageRegistration);
        updateDocuments(marriageRegistration);
        return update(marriageRegistration);
    }

    /**
     * Adds the uploaded registration document to file store and associates with the registration
     *
     * @param registration
     */
    private void addDocumentsToFileStore(final MarriageRegistration registration,
            final Map<Long, MarriageDocument> documentAndId) {
        final List<MarriageDocument> documents = registration.getDocuments();
        documents.stream()
                .filter(document -> !document.getFile().isEmpty() && document.getFile().getSize() > 0)
                .map(document -> {
                    final RegistrationDocument registrationDocument = new RegistrationDocument();
                    setCommonDocumentsFalg(registration, document);
                    registrationDocument.setRegistration(registration);
                    registrationDocument.setDocument(documentAndId.get(document.getId()));
                    registrationDocument.setFileStoreMapper(addToFileStore(document.getFile()));
                    return registrationDocument;
                }).collect(Collectors.toList())
                .forEach(doc -> registration.addRegistrationDocument(doc));
    }

    private void setCommonDocumentsFalg(final MarriageRegistration registration, final MarriageDocument document) {
        final MarriageDocument marriageDocument = marriageDocumentService.get(document.getId());
        if (marriageDocument.getCode().equals(MOM))
            registration.setMemorandumOfMarriage(true);
        if (marriageDocument.getCode().equals(CF_STAMP))
            registration.setCourtFeeStamp(true);
        if (marriageDocument.getCode().equals(MIC))
            registration.setMarriageCard(true);
        if (marriageDocument.getCode().equals(AFFIDAVIT))
            registration.setAffidavit(true);
    }

    private void updateDocuments(final MarriageRegistration registration) {
        final MarriageRegistration marriageRegistration = get(registration.getId());
        marriageApplicantService.deleteDocuments(marriageRegistration.getHusband(), registration.getHusband());
        marriageApplicantService.deleteDocuments(marriageRegistration.getWife(), registration.getWife());
        deleteDocuments(marriageRegistration, registration);
        final Map<Long, MarriageDocument> generalDocumentAndId = new HashMap<>();
        marriageDocumentService.getGeneralDocuments().forEach(document -> generalDocumentAndId.put(document.getId(), document));

        addDocumentsToFileStore(registration, generalDocumentAndId);

        addMarriageDocumentsToFileStore(registration);
    }

    private void addMarriageDocumentsToFileStore(final MarriageRegistration registration) {
        final Map<Long, MarriageDocument> individualDocumentAndId = new HashMap<>();
        marriageDocumentService.getIndividualDocuments().forEach(
                document -> individualDocumentAndId.put(document.getId(), document));

        marriageApplicantService.addDocumentsToFileStore(registration.getHusband(), individualDocumentAndId);
        marriageApplicantService.addDocumentsToFileStore(registration.getWife(), individualDocumentAndId);
    }

    @Transactional
    public MarriageRegistration approveRegistration(final MarriageRegistration marriageRegistration,
            final WorkflowContainer workflowContainer) {
        marriageRegistration.setStatus(
                marriageUtils.getStatusByCodeAndModuleType(MarriageRegistration.RegistrationStatus.APPROVED.toString(),
                        MarriageConstants.MODULE_NAME));
        marriageRegistration.setRegistrationNo(marriageRegistrationNumberGenerator
                .generateMarriageRegistrationNumber(marriageRegistration));
        User user = securityUtils.getCurrentUser();
        if(user!=null)
        marriageRegistration.setRegistrarName(user.getName());
        
        updateRegistrationdata(marriageRegistration);
        updateDocuments(marriageRegistration);
        update(marriageRegistration);
        workflowService.transition(marriageRegistration, workflowContainer, workflowContainer.getApproverComments());
        marriageRegistrationUpdateIndexesService.updateIndexes(marriageRegistration);
        marriageSmsAndEmailService.sendSMS(marriageRegistration, MarriageRegistration.RegistrationStatus.APPROVED.toString());
        marriageSmsAndEmailService.sendEmail(marriageRegistration, MarriageRegistration.RegistrationStatus.APPROVED.toString());
        return marriageRegistration;
    }

    @Transactional
    public MarriageCertificate generateMarriageCertificate(final MarriageRegistration marriageRegistration,
            final WorkflowContainer workflowContainer, final HttpServletRequest request) throws IOException {
        final MarriageCertificate marriageCertificate = marriageCertificateService.generateMarriageCertificate(
                marriageRegistration, request);
        marriageRegistration.addCertificate(marriageCertificate);
        return marriageCertificate;
    }

    @Transactional
    public MarriageRegistration digiSignCertificate(final MarriageRegistration marriageRegistration,
            final WorkflowContainer workflowContainer, final HttpServletRequest request) throws IOException {
        marriageRegistration.setStatus(marriageUtils.getStatusByCodeAndModuleType(
                MarriageRegistration.RegistrationStatus.DIGITALSIGNED.toString(), MarriageConstants.MODULE_NAME));
        workflowService.transition(marriageRegistration, workflowContainer, workflowContainer.getApproverComments());
        marriageRegistrationUpdateIndexesService.updateIndexes(marriageRegistration);
        // TODO : send sms and email after digital sign is done
        /*
         * marriageSmsAndEmailService.sendSMS(marriageRegistration,
         * MarriageRegistration.RegistrationStatus.DIGITALSIGNED.toString());
         * marriageSmsAndEmailService.sendEmail(marriageRegistration,
         * MarriageRegistration.RegistrationStatus.DIGITALSIGNED.toString());
         */
        return marriageRegistration;
    }

    @Transactional
    public MarriageRegistration printCertificate(final MarriageRegistration marriageRegistration,
            final WorkflowContainer workflowContainer, final HttpServletRequest request) throws IOException {
        if (marriageRegistration.getMarriageCertificate().isEmpty()) {
            final MarriageCertificate marriageCertificate = marriageCertificateService.generateMarriageCertificate(
                    marriageRegistration, request);
            marriageRegistration.addCertificate(marriageCertificate);
        }
        marriageRegistration.setStatus(marriageUtils.getStatusByCodeAndModuleType(
                MarriageRegistration.RegistrationStatus.REGISTERED.toString(), MarriageConstants.MODULE_NAME));
        marriageRegistration.setActive(true);
        workflowService.transition(marriageRegistration, workflowContainer, workflowContainer.getApproverComments());
        marriageRegistrationUpdateIndexesService.updateIndexes(marriageRegistration);
        return marriageRegistration;
    }

    @Transactional
    public MarriageRegistration rejectRegistration(final MarriageRegistration marriageRegistration,
            final WorkflowContainer workflowContainer) {
        marriageRegistration.setStatus(workflowContainer.getWorkFlowAction().equalsIgnoreCase(
                MarriageConstants.WFLOW_ACTION_STEP_REJECT)
                        ? marriageUtils.getStatusByCodeAndModuleType(MarriageRegistration.RegistrationStatus.REJECTED.toString(),
                                MarriageConstants.MODULE_NAME)
                        : marriageUtils.getStatusByCodeAndModuleType(MarriageRegistration.RegistrationStatus.CANCELLED.toString(),
                                MarriageConstants.MODULE_NAME));
        marriageRegistration.setRejectionReason(workflowContainer.getApproverComments());
        workflowService.transition(marriageRegistration, workflowContainer, workflowContainer.getApproverComments());
        marriageRegistrationUpdateIndexesService.updateIndexes(marriageRegistration);
        marriageSmsAndEmailService.sendSMS(marriageRegistration, MarriageRegistration.RegistrationStatus.REJECTED.toString());
        marriageSmsAndEmailService.sendEmail(marriageRegistration, MarriageRegistration.RegistrationStatus.REJECTED.toString());
        return marriageRegistration;
    }

    public List<MarriageRegistration> getRegistrations() {
        return registrationRepository.findAll();
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

    public void deleteDocuments(final MarriageRegistration regModel, final MarriageRegistration registration) {
        final List<RegistrationDocument> toDelete = new ArrayList<>();
        final Map<Long, RegistrationDocument> documentIdAndRegistrationDoc = new HashMap<>();
        registration.getRegistrationDocuments()
                .forEach(regDoc -> documentIdAndRegistrationDoc.put(regDoc.getDocument().getId(), regDoc));

        regModel.getDocuments()
                .stream()
                .filter(doc -> doc.getFile().getSize() > 0)
                .map(doc -> {
                    final RegistrationDocument regDoc = documentIdAndRegistrationDoc.get(doc.getId());
                    if (null != regDoc)
                        fileStoreService.delete(regDoc.getFileStoreMapper().getFileStoreId(),
                                MarriageConstants.FILESTORE_MODULECODE);
                    return regDoc;
                }).collect(Collectors.toList())
                .forEach(regDoc -> toDelete.add(regDoc));

        registrationDocumentService.delete(toDelete);
    }

    public List<MarriageRegistration> searchRegistrationBetweenDateAndStatus(final SearchModel searchModel) {
        final EgwStatus status = searchModel.isRegistrationApproved()
                ? marriageUtils.getStatusByCodeAndModuleType(MarriageRegistration.RegistrationStatus.APPROVED.toString(),
                        MarriageConstants.MODULE_NAME)
                : marriageUtils.getStatusByCodeAndModuleType(MarriageRegistration.RegistrationStatus.REJECTED.toString(),
                        MarriageConstants.MODULE_NAME);
        return registrationRepository.findByCreatedDateAfterAndCreatedDateBeforeAndStatus(searchModel.getFromDate(),
                searchModel.getToDate(), status);
    }

    public void prepareDocumentsForView(final MarriageRegistration registration) {
        if (registration.getRegistrationDocuments() != null)
            registration.getRegistrationDocuments().forEach(
                    appDoc -> {
                        final File file = fileStoreService.fetch(appDoc.getFileStoreMapper().getFileStoreId(),
                                MarriageConstants.FILESTORE_MODULECODE);
                        try {
                            appDoc.setBase64EncodedFile(Base64.getEncoder().encodeToString(FileCopyUtils.copyToByteArray(file)));
                        } catch (final IOException e) {
                            LOG.error("Error while preparing the document for view", e);
                        }
                    });

        if (registration.getMarriagePhotoFileStore() != null) {
            final File file = fileStoreService.fetch(registration.getMarriagePhotoFileStore().getFileStoreId(),
                    MarriageConstants.FILESTORE_MODULECODE);
            try {
                registration.setEncodedMarriagePhoto(Base64.getEncoder().encodeToString(FileCopyUtils.copyToByteArray(file)));
            } catch (final IOException e) {
                LOG.error("Error while preparing the document for view", e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public List<MarriageRegistration> searchMarriageRegistrations(final MarriageRegistration registration) throws ParseException {
        final Criteria criteria = getCurrentSession().createCriteria(MarriageRegistration.class, MARRIAGE_REGISTRATION);
        buildMarriageRegistrationSearchCriteria(registration, criteria);
        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    public List<MarriageRegistration> searchMarriageRegistrationsForFeeCollection(final MarriageRegistration registration)
            throws ParseException {
        final Criteria criteria = getCurrentSession().createCriteria(MarriageRegistration.class, MARRIAGE_REGISTRATION)
                .createAlias("marriageRegistration.status", STATUS);
        buildMarriageRegistrationSearchCriteria(registration, criteria);
        criteria.add(Restrictions.in(STATUS_DOT_CODE, new String[] { MarriageRegistration.RegistrationStatus.CREATED.toString(),
                MarriageRegistration.RegistrationStatus.APPROVED.toString() }));
        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    public List<ReIssue> searchApprovedReIssueRecordsForFeeCollection(final MarriageRegistrationSearchFilter mrSearchFilter)
            throws ParseException {
        final Criteria criteria = getCurrentSession().createCriteria(ReIssue.class, "reIssue")
                .createAlias("reIssue.status", STATUS);
        buildReIssueSearchCriteria(mrSearchFilter, criteria);
        criteria.add(Restrictions.in(STATUS_DOT_CODE, new String[] { ReIssue.ReIssueStatus.CREATED.toString(),
                ReIssue.ReIssueStatus.APPROVED.toString() }));
        return criteria.list();
    }

    private void buildReIssueSearchCriteria(final MarriageRegistrationSearchFilter mrSearchFilter, final Criteria criteria)
            throws ParseException {
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        criteria.createAlias("reIssue.registration", "registration");
        if (mrSearchFilter.getRegistrationNo() != null)
            criteria.add(
                    Restrictions.ilike("registration.registrationNo", mrSearchFilter.getRegistrationNo(),
                            MatchMode.ANYWHERE));
        if (mrSearchFilter.getApplicationNo() != null)
            criteria.add(
                    Restrictions.ilike("reIssue.applicationNo", mrSearchFilter.getApplicationNo(),
                            MatchMode.ANYWHERE));
        if (mrSearchFilter.getHusbandName() != null)
            criteria.createAlias("registration.husband", "husband").add(
                    Restrictions.ilike("husband.name.fullname", mrSearchFilter.getHusbandName(), MatchMode.ANYWHERE));
        if (mrSearchFilter.getWifeName() != null)
            criteria.createAlias("registration.wife", "wife").add(
                    Restrictions.ilike("wife.name.fullname", mrSearchFilter.getWifeName(), MatchMode.ANYWHERE));
        if (mrSearchFilter.getApplicationDate() != null)
            criteria.add(Restrictions.between("reIssue.applicationDate", sdf.parse(mrSearchFilter.getApplicationDate()),
                    org.apache.commons.lang3.time.DateUtils.addDays(sdf.parse(mrSearchFilter.getApplicationDate()), 1)));
        if (mrSearchFilter.getDateOfMarriage() != null)
            criteria.add(
                    Restrictions.between("registration.dateOfMarriage", sdf.parse(mrSearchFilter.getDateOfMarriage()),
                            org.apache.commons.lang3.time.DateUtils.addDays(sdf.parse(mrSearchFilter.getDateOfMarriage()), 0)));
        if (mrSearchFilter.getFromDate() != null)
            criteria.add(Restrictions.ge("reIssue.applicationDate",
                    marriageRegistrationReportsService.resetFromDateTimeStamp(mrSearchFilter.getFromDate())));
        if (mrSearchFilter.getToDate() != null)
            criteria.add(Restrictions.le("reIssue.applicationDate",
                    marriageRegistrationReportsService.resetToDateTimeStamp(mrSearchFilter.getToDate())));
        if (mrSearchFilter.getMarriageRegistrationUnit() != null)
            criteria.add(Restrictions.eq("marriageRegistrationUnit.id", mrSearchFilter.getMarriageRegistrationUnit()));
    }

    private void buildMarriageRegistrationSearchCriteria(final MarriageRegistration registration, final Criteria criteria)
            throws ParseException {
        if (registration.getRegistrationNo() != null)
            criteria.add(Restrictions.ilike("marriageRegistration.registrationNo", registration.getRegistrationNo(),
                    MatchMode.ANYWHERE));
        if (registration.getApplicationNo() != null)
            criteria.add(Restrictions.ilike("marriageRegistration.applicationNo", registration.getApplicationNo(),
                    MatchMode.ANYWHERE));
        if (registration.getHusband() != null && registration.getHusband().getName() != null
                && !"null".equals(registration.getHusband().getFullName()) && registration.getHusband().getFullName() != null)
            criteria.createAlias("marriageRegistration.husband", "husband").add(
                    Restrictions.ilike("husband.name.fullname", registration.getHusband().getFullName(),
                            MatchMode.ANYWHERE));
        if (registration.getWife() != null && registration.getWife().getName() != null
                && !"null".equals(registration.getWife().getFullName()) && registration.getWife().getFullName() != null)
            criteria.createAlias("marriageRegistration.wife", "wife").add(
                    Restrictions.ilike("wife.name.fullname", registration.getWife().getFullName(),
                            MatchMode.ANYWHERE));
        if (registration.getApplicationDate() != null)
            criteria.add(Restrictions.between(MARRIAGE_REGISTRATION_DOT_APPLICATION_DATE, registration.getApplicationDate(),
                    org.apache.commons.lang3.time.DateUtils.addDays(registration.getApplicationDate(), 1)));
        if (registration.getDateOfMarriage() != null)
            criteria.add(Restrictions.between("marriageRegistration.dateOfMarriage", registration.getDateOfMarriage(),
                    org.apache.commons.lang3.time.DateUtils.addDays(registration.getDateOfMarriage(), 0)));
        if (registration.getFromDate() != null)
            criteria.add(Restrictions.ge(MARRIAGE_REGISTRATION_DOT_APPLICATION_DATE,
                    marriageRegistrationReportsService.resetFromDateTimeStamp(registration.getFromDate())));
        if (registration.getToDate() != null)
            criteria.add(Restrictions.le(MARRIAGE_REGISTRATION_DOT_APPLICATION_DATE,
                    marriageRegistrationReportsService.resetToDateTimeStamp(registration.getToDate())));
        if (null != registration.getMarriageRegistrationUnit() && registration.getMarriageRegistrationUnit().getId() != null)
            criteria.add(Restrictions.eq("marriageRegistrationUnit.id", registration.getMarriageRegistrationUnit().getId()));
    }

    /**
     * @param registration
     * @return
     */
    public List<Map<String, Object>> getHistory(final MarriageRegistration registration) {
        User user;
        final List<Map<String, Object>> historyTable = new ArrayList<>();
        final State state = registration.getState();
        final Map<String, Object> map = new HashMap<>(0);
        if (null != state) {
            if (!registration.getStateHistory().isEmpty()
                    && registration.getStateHistory() != null)
                Collections.reverse(registration.getStateHistory());
            Map<String, Object> historyMap;
            for (final StateHistory stateHistory : registration.getStateHistory()) {
                historyMap = new HashMap<>(0);
                historyMap.put("date", stateHistory.getDateInfo());
                historyMap.put("comments", stateHistory.getComments() != null ? stateHistory.getComments() : "");
                historyMap.put("updatedBy", stateHistory.getLastModifiedBy().getUsername() + "::"
                        + stateHistory.getLastModifiedBy().getName());
                historyMap.put(STATUS, stateHistory.getValue());
                final Position owner = stateHistory.getOwnerPosition();
                user = stateHistory.getOwnerUser();
                if (null != user) {
                    historyMap.put(USER, user.getUsername() + "::" + user.getName());
                    historyMap.put(DEPARTMENT,
                            null != eisCommonService.getDepartmentForUser(user.getId()) ? eisCommonService
                                    .getDepartmentForUser(user.getId()).getName() : "");
                } else if (null != owner && null != owner.getDeptDesig()) {
                    user = eisCommonService.getUserForPosition(owner.getId(), new Date());
                    historyMap
                            .put(USER, null != user.getUsername() ? user.getUsername() + "::" + user.getName() : "");
                    historyMap.put(DEPARTMENT, null != owner.getDeptDesig().getDepartment() ? owner.getDeptDesig()
                            .getDepartment().getName() : "");
                }
                historyTable.add(historyMap);
            }
            map.put("date", state.getDateInfo());
            map.put("comments", state.getComments() != null ? state.getComments() : "");
            map.put("updatedBy", state.getLastModifiedBy().getUsername() + "::" + state.getLastModifiedBy().getName());
            map.put(STATUS, state.getValue());
            final Position ownerPosition = state.getOwnerPosition();
            user = state.getOwnerUser();
            if (null != user) {
                map.put(USER, user.getUsername() + "::" + user.getName());
                map.put(DEPARTMENT, null != eisCommonService.getDepartmentForUser(user.getId()) ? eisCommonService
                        .getDepartmentForUser(user.getId()).getName() : "");
            } else if (null != ownerPosition && null != ownerPosition.getDeptDesig()) {
                user = eisCommonService.getUserForPosition(ownerPosition.getId(), new Date());
                map.put(USER, null != user.getUsername() ? user.getUsername() + "::" + user.getName() : "");
                map.put(DEPARTMENT, null != ownerPosition.getDeptDesig().getDepartment() ? ownerPosition
                        .getDeptDesig().getDepartment().getName() : "");
            }
            historyTable.add(map);
        }
        return historyTable;
    }

    @SuppressWarnings("unchecked")
    public List<MarriageRegistration> searchRegistrationByStatus(final MarriageRegistration registration, final String status)
            throws ParseException {

        final Criteria criteria = getCurrentSession().createCriteria(MarriageRegistration.class, MARRIAGE_REGISTRATION)
                .createAlias("marriageRegistration.status", STATUS);
        buildMarriageRegistrationSearchCriteria(registration, criteria);
        if (status != null)
            criteria.add(Restrictions.in(STATUS_DOT_CODE, new String[] { status }));
        return criteria.list();
    }

    public MarriageRegistration findBySerialNo(final String serialNo) {
        return registrationRepository.findBySerialNo(serialNo);
    }

    public ReportOutput getReportParamsForAcknowdgementForMrgReg(final MarriageRegistration registration,
            final String municipalityName, final String cityName) {
        String applicantName = null;
        final Map<String, Object> reportParams = new HashMap<>();
        reportParams.put(MUNICIPALITY, municipalityName);
        reportParams.put(CITYNAME, cityName);
        reportParams.put(ZONE_NAME, registration.getZone().getName());
        reportParams.put(MRG_REGISTRATION_UNIT, registration.getMarriageRegistrationUnit().getName());
        if (registration.getHusband() != null && registration.getWife() != null)
            applicantName = registration.getHusband().getFullName().concat(" / ").concat(registration.getWife().getFullName());
        reportParams.put(APPLICANT_NAME, applicantName);
        reportParams.put(ACKNOWLEDGEMENT_NO, registration.getApplicationNo());
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        reportParams.put(CURRENT_DATE, formatter.format(new Date()));
        reportParams.put(ADDRESS, registration.getHusband().getContactInfo().getResidenceAddress());
        reportParams.put(DUE_DATE, formatter.format(calculateDueDateForMrgReg()));
        reportParams.put(PARTY_S_COPY, PARTY_S_COPY);
        reportParams.put(OFFICE_S_COPY, OFFICE_S_COPY);
        reportParams.put(APPLICATION_CENTRE, marriageMessageSource.getMessage("msg.application.centre",
                new String[] {}, Locale.getDefault()));
        reportParams.put(APP_TYPE, NEW_MARRIAGE_REGISTRATION);

        final ReportRequest reportInput = new ReportRequest(MARRIAGE_ACKNOWLEDGEMENT_REPORT_FILE, registration, reportParams);

        return reportService.createReport(reportInput);

    }

    public ReportOutput getReportParamsForAcknowdgementForMrgReissue(final ReIssue reIssue,
            final String municipalityName, final String cityName) {
        String applicantName = null;
        final Map<String, Object> reportParams = new HashMap<>();
        reportParams.put(MUNICIPALITY, municipalityName);
        reportParams.put(CITYNAME, cityName);
        reportParams.put(MRG_REGISTRATION_UNIT, reIssue.getRegistration().getMarriageRegistrationUnit().getName());
        reportParams.put(ZONE_NAME, reIssue.getZone().getName());
        if (reIssue.getRegistration().getHusband() != null && reIssue.getRegistration().getWife() != null)
            applicantName = reIssue.getRegistration().getHusband().getFullName().concat(" / ")
                    .concat(reIssue.getRegistration().getWife().getFullName());
        reportParams.put(APPLICANT_NAME, applicantName);
        reportParams.put(ACKNOWLEDGEMENT_NO, reIssue.getApplicationNo());
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        reportParams.put(CURRENT_DATE, formatter.format(new Date()));
        reportParams.put(ADDRESS, reIssue.getRegistration().getHusband().getContactInfo().getResidenceAddress());
        reportParams.put(DUE_DATE, formatter.format(calculateDueDateForMrgReIssue()));
        reportParams.put(PARTY_S_COPY, PARTY_S_COPY);
        reportParams.put(OFFICE_S_COPY, OFFICE_S_COPY);
        reportParams.put(APPLICATION_CENTRE, marriageMessageSource.getMessage("msg.application.centre",
                new String[] {}, Locale.getDefault()));
        reportParams.put(APP_TYPE, REISSUE_MARRIAGE_CERTIFICATE);

        final ReportRequest reportInput = new ReportRequest(MARRIAGE_ACKNOWLEDGEMENT_REPORT_FILE, reIssue, reportParams);

        return reportService.createReport(reportInput);

    }

    public Date calculateDueDateForMrgReg() {
        Date dueDate;
        final Date currentDate = new Date();
        final String slaNewAdvertisement = marriageMessageSource.getMessage("msg.new.marriage.registration.sla",
                new String[] {}, Locale.getDefault());
        dueDate = org.apache.commons.lang3.time.DateUtils.addDays(currentDate, Integer.parseInt(slaNewAdvertisement));
        return dueDate;

    }

    public Date calculateDueDateForMrgReIssue() {
        Date dueDate;
        final Date currentDate = new Date();
        final String slaNewAdvertisement = marriageMessageSource.getMessage("msg.marriage.reissue.certificate.sla",
                new String[] {}, Locale.getDefault());
        dueDate = org.apache.commons.lang3.time.DateUtils.addDays(currentDate, Integer.parseInt(slaNewAdvertisement));
        return dueDate;

    }

}