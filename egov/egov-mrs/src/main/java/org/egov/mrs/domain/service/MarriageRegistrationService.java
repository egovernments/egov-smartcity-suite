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

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.messaging.MessagingService;
import org.egov.infra.search.elastic.entity.ApplicationIndexBuilder;
import org.egov.infra.search.elastic.service.ApplicationIndexService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.ApplicationNumberGenerator;
import org.egov.mrs.application.MarriageConstants;
import org.egov.mrs.application.service.MarriageRegistrationDemandService;
import org.egov.mrs.application.service.workflow.RegistrationWorkflowService;
import org.egov.mrs.domain.entity.Applicant;
import org.egov.mrs.domain.entity.IdentityProof;
import org.egov.mrs.domain.entity.MarriageDocument;
import org.egov.mrs.domain.entity.MarriageRegistration;
import org.egov.mrs.domain.entity.RegistrationDocument;
import org.egov.mrs.domain.entity.SearchModel;
import org.egov.mrs.domain.enums.ApplicationStatus;
import org.egov.mrs.domain.enums.FeeType;
import org.egov.mrs.domain.repository.MarriageRegistrationRepository;
import org.egov.mrs.domain.repository.WitnessRepository;
import org.egov.mrs.masters.service.ActService;
import org.egov.mrs.masters.service.ReligionService;
import org.egov.mrs.utils.MarriageRegistrationNoGenerator;
import org.hibernate.Criteria;
import org.hibernate.Session;
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

    private final String MSG_KEY_SMS_REGISTRATION_NEW = "msg.newregistration.sms";
    private final String MSG_KEY_SMS_REGISTRATION_REJECTION = "msg.rejectregistration.sms";
    private final String MSG_KEY_EMAIL_REGISTRATION_NEW_EMAIL = "msg.newregistration.mail";
    private final String MSG_KEY_EMAIL_REGISTRATION_NEW_SUBJECT = "msg.newregistration.mail.subject";
    private final String MSG_KEY_EMAIL_REGISTRATION_REJECTION_EMAIL = "msg.rejectionregistration.mail";
    private final String MSG_KEY_EMAIL_REGISTRATION_REJECTION_SUBJECT = "msg.rejectionregistration.mail.subject";

    private static final Logger LOG = Logger.getLogger(MarriageRegistrationService.class);
    private final MarriageRegistrationRepository registrationRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private MessagingService messagingService;
    

    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource mrsMessageSource;
    

    @Autowired
    private ReligionService religionService;

    @Autowired
    private ActService actService;

    @Autowired
    private MarriageRegistrationDemandService marriageRegistrationDemandService;

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private ApplicationNumberGenerator applicationNumberGenerator;

    @Autowired
    private RegistrationWorkflowService workflowService;

    @Autowired
    private MarriageRegistrationNoGenerator registrationNoGenerator;

    @Autowired
    private ApplicationIndexService applicationIndexService;

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
    private AssignmentService assignmentService;

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

    public MarriageRegistration get(final String registrationNo) {
        return registrationRepository.findByRegistrationNo(registrationNo);
    }

    @Transactional
    public String createRegistration(final MarriageRegistration registration, final WorkflowContainer workflowContainer) {

        if (StringUtils.isBlank(registration.getApplicationNo())) {
            registration.setApplicationNo(applicationNumberGenerator.generate());
            registration.setApplicationDate(new Date());
        }

        registration.getHusband().setReligion(religionService.getProxy(registration.getHusband().getReligion().getId()));
        registration.getWife().setReligion(religionService.getProxy(registration.getWife().getReligion().getId()));
        registration.getWitnesses().forEach(witness -> witness.setRegistration(registration));
        registration.setMarriageAct(actService.getAct(registration.getMarriageAct().getId()));
                if (registration.getFeePaid() != null && registration.getDemand() == null){
                        registration.setDemand(
                                        marriageRegistrationDemandService.createDemand(new BigDecimal(registration.getFeePaid())));
                }
                  registration.setStatus(ApplicationStatus.Created);

        if (registration.getPriest().getReligion().getId() != null)
            registration.getPriest().setReligion(religionService.getProxy(registration.getPriest().getReligion().getId()));
        else
            registration.setPriest(null);

        registration.setZone(boundaryService.getBoundaryById(registration.getZone().getId()));

        try {
            registration.getHusband().copyPhotoAndSignatureToByteArray();
            registration.getWife().copyPhotoAndSignatureToByteArray();
        } catch (IOException e) {
            LOG.error("Error while copying Multipart file bytes", e);
        }

        registration.getWitnesses().forEach(witness -> {
            try {
                //witness.setPhoto(FileCopyUtils.copyToByteArray(witness.getPhotoFile().getInputStream()));
            	 witness.setPhotoFileStore(addToFileStore(witness.getPhotoFile()));
            } catch (Exception e) {
                LOG.error("Error while copying Multipart file bytes", e);
            }
        });
        
        try {
        	registration.getWife().setPhotoFileStore(addToFileStore(registration.getWife().getPhotoFile()));
        	registration.getWife().setSignatureFileStore(addToFileStore(registration.getWife().getSignatureFile()));
        	registration.getHusband().setPhotoFileStore(addToFileStore(registration.getHusband().getPhotoFile()));
        	registration.getHusband().setSignatureFileStore(addToFileStore(registration.getHusband().getSignatureFile()));
        } catch (Exception e) {
            LOG.error("Error while saving documents!!!!!", e);
        }
        
        final Map<Long, MarriageDocument> generalDocumentAndId = new HashMap<Long, MarriageDocument>();
        marriageDocumentService.getGeneralDocuments().forEach(document -> generalDocumentAndId.put(document.getId(), document));

        addDocumentsToFileStore(null, registration, generalDocumentAndId);

        final Map<Long, MarriageDocument> individualDocumentAndId = new HashMap<Long, MarriageDocument>();
        marriageDocumentService.getIndividualDocuments().forEach(document -> individualDocumentAndId.put(document.getId(), document));

        marriageApplicantService.addDocumentsToFileStore(null, registration.getHusband(), individualDocumentAndId);
        marriageApplicantService.addDocumentsToFileStore(null, registration.getWife(), individualDocumentAndId);

        workflowService.transition(registration, workflowContainer, registration.getApprovalComent());

        create(registration);

        return registration.getApplicationNo();
    }

    @Transactional
    public MarriageRegistration forwardRegistration(final Long id, final MarriageRegistration regModel,
            final WorkflowContainer workflowContainer) {
       
        final MarriageRegistration registration = get(id);

        //Commented for time being as its throwing null pointer in edit mode
        //Need to fix
        //updateRegistrationData(regModel, registration);
        
        // Remove this loc once above issue fixed
        registration.setStatus(ApplicationStatus.Created);

        workflowService.transition(registration, workflowContainer, registration.getApprovalComent());
        return update(registration);
    }
    
    @Transactional
    public MarriageRegistration updateRegistration(final Long id, final MarriageRegistration regModel) {
        final MarriageRegistration registration = get(id);
        updateRegistrationData(regModel, registration);
        return update(registration);
    }
    
    /**
     * Adds the uploaded registration document to file store and associates with the registration
     *
     * @param registration
     */
    private void addDocumentsToFileStore(final MarriageRegistration registrationModel, final MarriageRegistration registration,
            final Map<Long, MarriageDocument> documentAndId) {
        List<MarriageDocument> documents = registrationModel == null ? registration.getDocuments() : registrationModel.getDocuments();
        documents.stream()
                .filter(document -> !document.getFile().isEmpty() && document.getFile().getSize() > 0)
                .map(document -> {
                    final RegistrationDocument registrationDocument = new RegistrationDocument();
                    registrationDocument.setRegistration(registration);
                    registrationDocument.setDocument(documentAndId.get(document.getId()));
                    registrationDocument.setFileStoreMapper(addToFileStore(document.getFile()));
                    return registrationDocument;
                }).collect(Collectors.toList())
                .forEach(doc -> registration.addRegistrationDocument(doc));
    }

    private void updateRegistrationData(final MarriageRegistration regModel, final MarriageRegistration registration) {
        registration.setDateOfMarriage(regModel.getDateOfMarriage());
        registration.setPlaceOfMarriage(regModel.getPlaceOfMarriage());
        registration.setFeeCriteria(regModel.getFeeCriteria());
        registration.setFeePaid(regModel.getFeePaid());
        registration.setMarriageAct(actService.getAct(registration.getMarriageAct().getId()));
        
                if (registration.getFeePaid() != null) {
                        if (registration.getDemand() == null){
                                registration.setDemand(
                                                marriageRegistrationDemandService.createDemand(new BigDecimal(registration.getFeePaid())));
                        }
                        else{
                                marriageRegistrationDemandService.updateDemand(registration.getDemand(),
                                                new BigDecimal(registration.getFeePaid()));
                        }

                }
        registration.setStatus(ApplicationStatus.Created);

        witnessRepository.delete(registration.getWitnesses());
        registration.getWitnesses().clear();

        regModel.getWitnesses().stream().forEach(witness -> registration.addWitness(witness));
        
        updateApplicantInfo(regModel.getHusband(), registration.getHusband());
        updateApplicantInfo(regModel.getWife(), registration.getWife());

        updateDocuments(regModel, registration);
        
        updateProofInfo(regModel.getHusband().getProofsAttached(), registration.getHusband().getProofsAttached());
        updateProofInfo(regModel.getWife().getProofsAttached(), registration.getWife().getProofsAttached());
    }

    private void updateDocuments(final MarriageRegistration regModel, final MarriageRegistration registration) {
        deleteDocuments(regModel, registration);
        marriageApplicantService.deleteDocuments(regModel.getHusband(), registration.getHusband());
        marriageApplicantService.deleteDocuments(regModel.getWife(), registration.getWife());

        final Map<Long, MarriageDocument> generalDocumentAndId = new HashMap<Long, MarriageDocument>();
        marriageDocumentService.getGeneralDocuments().forEach(document -> generalDocumentAndId.put(document.getId(), document));

        addDocumentsToFileStore(regModel, registration, generalDocumentAndId);

        final Map<Long, MarriageDocument> individualDocumentAndId = new HashMap<Long, MarriageDocument>();
        marriageDocumentService.getIndividualDocuments().forEach(document -> individualDocumentAndId.put(document.getId(), document));

        marriageApplicantService.addDocumentsToFileStore(regModel.getHusband(), registration.getHusband(), individualDocumentAndId);
        marriageApplicantService.addDocumentsToFileStore(regModel.getWife(), registration.getWife(), individualDocumentAndId);
    }

    private void updateApplicantInfo(final Applicant modelApplicant, final Applicant dbApplicant) {
        dbApplicant.getName().setFirstName(modelApplicant.getName().getFirstName());
        dbApplicant.getName().setMiddleName(modelApplicant.getName().getMiddleName());
        dbApplicant.getName().setLastName(modelApplicant.getName().getLastName());
        dbApplicant.setOtherName(modelApplicant.getOtherName());

        try {
            if (modelApplicant.isCopyFilesToByteArray())
                modelApplicant.copyPhotoAndSignatureToByteArray();
        } catch (IOException e) {
            LOG.error("Error while copying Multipart file bytes", e);
        }

        dbApplicant.setPhoto(modelApplicant.getPhoto());
        dbApplicant.setSignature(modelApplicant.getSignature());
        dbApplicant.setReligion(religionService.getProxy(modelApplicant.getReligion().getId()));
        dbApplicant.setReligionPractice(modelApplicant.getReligionPractice());
        dbApplicant.setAgeInYearsAsOnMarriage(modelApplicant.getAgeInYearsAsOnMarriage());
        dbApplicant.setAgeInMonthsAsOnMarriage(modelApplicant.getAgeInMonthsAsOnMarriage());
        dbApplicant.setMaritalStatus(modelApplicant.getMaritalStatus());
        dbApplicant.setOccupation(modelApplicant.getOccupation());
        dbApplicant.getContactInfo().setResidenceAddress(modelApplicant.getContactInfo().getResidenceAddress());
        dbApplicant.getContactInfo().setOfficeAddress(modelApplicant.getContactInfo().getOfficeAddress());
        dbApplicant.getContactInfo().setMobileNo(modelApplicant.getContactInfo().getMobileNo());
        dbApplicant.getContactInfo().setEmail(modelApplicant.getContactInfo().getEmail());
    }

    private void updateProofInfo(final IdentityProof fromModel, final IdentityProof fromDb) {
        fromDb.setPhotograph(fromModel.getPhotograph());
        fromDb.setDeaceasedDeathCertificate(fromModel.getDeaceasedDeathCertificate());
        fromDb.setDivorceCertificate(fromModel.getDivorceCertificate());
        fromDb.setSchoolLeavingCertificate(fromModel.getSchoolLeavingCertificate());
        fromDb.setBirthCertificate(fromModel.getBirthCertificate());
        fromDb.setPassport(fromModel.getPassport());
        fromDb.setRationCard(fromModel.getRationCard());
        fromDb.setMsebBill(fromModel.getMsebBill());
        fromDb.setTelephoneBill(fromModel.getTelephoneBill());
    }

    @Transactional
    public MarriageRegistration approveRegistration( MarriageRegistration registration, final WorkflowContainer workflowContainer) {
        registration.setStatus(ApplicationStatus.Approved);
        registration.setRegistrationNo(registrationNoGenerator.generateRegistrationNo());
        registration = update(registration);
        workflowService.transition(registration, workflowContainer, workflowContainer.getApproverComments());
        sendSMS(registration);
        sendEmail(registration);
        createRegistrationAppIndex(registration); 
        return registration;
    }
    
    @Transactional
    public MarriageRegistration printCertificate(MarriageRegistration registration, final WorkflowContainer workflowContainer) {
        registration.setStatus(ApplicationStatus.Registered);
        workflowService.transition(registration, workflowContainer, workflowContainer.getApproverComments()); 
        sendSMS(registration);
        sendEmail(registration);
        return registration;
    }
    

    private void createRegistrationAppIndex(final MarriageRegistration registration) {
        final User user = securityUtils.getCurrentUser();
        final ApplicationIndexBuilder applicationIndexBuilder = new ApplicationIndexBuilder(MarriageConstants.MODULE_NAME,
                registration.getApplicationNo(),
                registration.getApplicationDate(), FeeType.REGISTRATION.name(),
                registration.getHusband().getFullName().concat(registration.getWife().getFullName()),
                registration.getStatus().name(),
                "/mrs/registration/" + registration.getId(),
                registration.getHusband().getContactInfo().getResidenceAddress(), user.getUsername() + "::" + user.getName(), "")//TODO CHECK THIS
                        .mobileNumber(registration.getHusband().getContactInfo().getMobileNo());

        applicationIndexService.createApplicationIndex(applicationIndexBuilder.build());
    }

    private void sendSMS(final MarriageRegistration registration) {
        String msgKey = MSG_KEY_SMS_REGISTRATION_NEW;

        if (registration.getStatus() == ApplicationStatus.Rejected)
            msgKey = MSG_KEY_SMS_REGISTRATION_REJECTION;

        final String message = mrsMessageSource.getMessage(msgKey,
                new String[] { registration.getHusband().getFullName(), registration.getWife().getFullName(),
                        registration.getRegistrationNo() },
                null);
        messagingService.sendSMS(registration.getHusband().getContactInfo().getMobileNo(), message);
        messagingService.sendSMS(registration.getWife().getContactInfo().getMobileNo(), message);
    }

    private void sendEmail(final MarriageRegistration registration) {
        String msgKeyMail = MSG_KEY_EMAIL_REGISTRATION_NEW_EMAIL;
        String msgKeyMailSubject = MSG_KEY_EMAIL_REGISTRATION_NEW_SUBJECT;

        if (registration.getStatus() == ApplicationStatus.Rejected) {
            msgKeyMail = MSG_KEY_EMAIL_REGISTRATION_REJECTION_EMAIL;
            msgKeyMailSubject = MSG_KEY_EMAIL_REGISTRATION_REJECTION_SUBJECT;
        }

        final String message = mrsMessageSource.getMessage(msgKeyMail,
                new String[] { registration.getHusband().getFullName(), registration.getWife().getFullName(),
                        registration.getRegistrationNo() },
                null);

        final String subject = mrsMessageSource.getMessage(msgKeyMailSubject, null, null);
        messagingService.sendEmail(registration.getHusband().getContactInfo().getEmail(), subject, message);
        messagingService.sendEmail(registration.getWife().getContactInfo().getEmail(), subject, message);
    }

    @Transactional
    public MarriageRegistration rejectRegistration(MarriageRegistration registration, final WorkflowContainer workflowContainer) {
        registration.setStatus(workflowContainer.getWorkFlowAction().equalsIgnoreCase(MarriageConstants.WFLOW_ACTION_STEP_REJECT)?
                ApplicationStatus.Rejected:ApplicationStatus.Cancelled);
        registration.setRejectionReason(workflowContainer.getApproverComments());
        workflowService.transition(registration, workflowContainer, workflowContainer.getApproverComments());
        sendSMS(registration);
        sendEmail(registration);
        return registration;
    }

    public List<MarriageRegistration> getRegistrations() {
        return registrationRepository.findAll();
    }

    public List<MarriageRegistration> searchRegistration(final SearchModel searchModel, final boolean isForReport) {

        final Criteria criteria = getCurrentSession().createCriteria(MarriageRegistration.class, "registration");

        if (StringUtils.isNotBlank(searchModel.getRegistrationNo()))
            criteria.add(Restrictions.eq("registrationNo", searchModel.getRegistrationNo()));

        if (searchModel.getDateOfMarriage() != null)
            criteria.add(Restrictions.eq("dateOfMarriage", searchModel.getDateOfMarriage()));

        if (StringUtils.isNotBlank(searchModel.getHusbandName()))
            criteria.createCriteria("husband").add(Restrictions.like("name.firstName", searchModel.getHusbandName()));

        if (StringUtils.isNotBlank(searchModel.getWifeName()))
            criteria.createCriteria("wife").add(Restrictions.ilike("name.firstName", searchModel.getWifeName()));

        if (isForReport) {
            if (searchModel.getFromDate() != null && searchModel.getToDate() != null)
                criteria.add(Restrictions.between("createdDate", searchModel.getFromDate(), searchModel.getToDate()));

            ApplicationStatus status = searchModel.isRegistrationApproved() ? ApplicationStatus.Approved
                    : ApplicationStatus.Rejected;
            criteria.add(Restrictions.eq("status", status));
        }

        /*
         * QRegistration registration = QRegistration.registration; BooleanExpression withRegistrationNo = null; BooleanExpression
         * withDateOfMarriage = null; BooleanExpression withHusbandName = null; BooleanExpression withWifeName = null;
         * BooleanExpression expression = null; if (StringUtils.isNotBlank(searchModel.getRegistrationNo())) { withRegistrationNo
         * = registration.registrationNo.eq(searchModel.getRegistrationNo()); expression = withRegistrationNo; } if
         * (searchModel.getDateOfMarriage() != null) { withDateOfMarriage =
         * registration.dateOfMarriage.eq(searchModel.getDateOfMarriage()); expression.and(withDateOfMarriage); } if
         * (StringUtils.isNotBlank(searchModel.getHusbandName())) { withHusbandName =
         * registration.husband().name().firstName.equalsIgnoreCase(searchModel.getHusbandName());
         * expression.and(withHusbandName); } if (StringUtils.isNotBlank(searchModel.getWifeName())) { withWifeName =
         * registration.wife().name().firstName.equalsIgnoreCase(searchModel.getWifeName()); expression.and(withWifeName); }
         */
        // registrationRepository.findAll(expression);
        return criteria.list();
    }

    private FileStoreMapper addToFileStore(final MultipartFile file) {
        FileStoreMapper fileStoreMapper = null;
        try {
            fileStoreMapper = fileStoreService.store(file.getInputStream(), file.getOriginalFilename(),
                    file.getContentType(), MarriageConstants.MODULE_NAME);
        } catch (final Exception e) {
            throw new ApplicationRuntimeException("Error occurred while getting inputstream", e);
        }
        return fileStoreMapper;
    }

    public void deleteDocuments(final MarriageRegistration regModel, final MarriageRegistration registration) {
        List<RegistrationDocument> toDelete = new ArrayList<RegistrationDocument>();
        Map<Long, RegistrationDocument> documentIdAndRegistrationDoc = new HashMap<Long, RegistrationDocument>();
        registration.getRegistrationDocuments()
                .forEach(regDoc -> documentIdAndRegistrationDoc.put(regDoc.getDocument().getId(), regDoc));

        regModel.getDocuments().stream()
                .filter(doc -> doc.getFile().getSize() > 0)
                .map(doc -> {
                    RegistrationDocument regDoc = documentIdAndRegistrationDoc.get(doc.getId());
                    fileStoreService.delete(regDoc.getFileStoreMapper().getFileStoreId(), MarriageConstants.MODULE_NAME);
                    return regDoc;
                }).collect(Collectors.toList())
                .forEach(regDoc -> toDelete.add(regDoc));

        registrationDocumentService.delete(toDelete);
    }

    public List<MarriageRegistration> searchRegistrationBetweenDateAndStatus(final SearchModel searchModel) {
        ApplicationStatus status = searchModel.isRegistrationApproved() ? ApplicationStatus.Approved : ApplicationStatus.Rejected;
        return registrationRepository.findByCreatedDateAfterAndCreatedDateBeforeAndStatus(searchModel.getFromDate(),
                searchModel.getToDate(), status);
    }

    public void prepareDocumentsForView(final MarriageRegistration registration) {
       if(registration.getRegistrationDocuments()!=null){ registration.getRegistrationDocuments().forEach(appDoc -> {
            final File file = fileStoreService.fetch(appDoc.getFileStoreMapper().getFileStoreId(), MarriageConstants.MODULE_NAME);
            try {
                appDoc.setBase64EncodedFile(Base64.getEncoder().encodeToString(FileCopyUtils.copyToByteArray(file)));
            } catch (final Exception e) {
                LOG.error("Error while preparing the document for view", e);
            }
        });
       }
    }

}