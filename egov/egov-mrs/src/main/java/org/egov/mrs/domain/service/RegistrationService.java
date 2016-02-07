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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
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
import org.egov.mrs.application.Constants;
import org.egov.mrs.application.service.RegistrationDemandService;
import org.egov.mrs.application.service.workflow.RegistrationWorkflowService;
import org.egov.mrs.domain.entity.AbstractDocument;
import org.egov.mrs.domain.entity.Applicant;
import org.egov.mrs.domain.entity.ApplicantDocument;
import org.egov.mrs.domain.entity.Document;
import org.egov.mrs.domain.entity.Registration;
import org.egov.mrs.domain.entity.RegistrationDocument;
import org.egov.mrs.domain.entity.SearchModel;
import org.egov.mrs.domain.enums.ApplicationStatus;
import org.egov.mrs.domain.enums.FeeType;
import org.egov.mrs.domain.repository.RegistrationRepository;
import org.egov.mrs.masters.service.ActService;
import org.egov.mrs.masters.service.ReligionService;
import org.egov.mrs.utils.MarriageRegistrationNoGenerator;
import org.elasticsearch.common.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class RegistrationService {

    private final String MSG_KEY_SMS_REGISTRATION_NEW = "msg.newregistration.sms";
    private final String MSG_KEY_SMS_REGISTRATION_REJECTION = "msg.rejectregistration.sms";
    private final String MSG_KEY_EMAIL_REGISTRATION_NEW_EMAIL = "msg.newregistration.mail";
    private final String MSG_KEY_EMAIL_REGISTRATION_NEW_SUBJECT = "msg.newregistration.mail.subject";
    private final String MSG_KEY_EMAIL_REGISTRATION_REJECTION_EMAIL = "msg.rejectionregistration.mail";
    private final String MSG_KEY_EMAIL_REGISTRATION_REJECTION_SUBJECT = "msg.rejectionregistration.mail.subject";

    private static final Logger LOG = Logger.getLogger(RegistrationService.class);
    private final RegistrationRepository registrationRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private MessagingService messagingService;

    @Autowired
    private ResourceBundleMessageSource messageSource;

    @Autowired
    private ReligionService religionService;

    @Autowired
    private ActService actService;

    @Autowired
    private RegistrationDemandService registrationDemandService;

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
    private DocumentService documentService;
    
    @Autowired
    private ApplicantService applicantService;
    
    @Autowired
    private RegistrationDocumentService registrationDocumentService;

    @Autowired
    public RegistrationService(final RegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

    private Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Transactional
    public void create(final Registration registration) {
        registrationRepository.save(registration);
    }

    @Transactional
    public Registration update(final Registration registration) {
        return registrationRepository.saveAndFlush(registration);
    }

    public Registration get(final Long id) {
        return registrationRepository.findById(id);
    }

    public Registration get(final String registrationNo) {
        return registrationRepository.findByRegistrationNo(registrationNo);
    }

    @Transactional
    public String createRegistration(final Registration registration, final WorkflowContainer workflowContainer) {

        if (StringUtils.isBlank(registration.getApplicationNo())) {
            registration.setApplicationNo(applicationNumberGenerator.generate());
            registration.setApplicationDate(new Date());
        }

        registration.getHusband().setReligion(religionService.getProxy(registration.getHusband().getReligion().getId()));
        registration.getHusband().setReligion(religionService.getProxy(registration.getHusband().getReligion().getId()));
        registration.getWife().setReligion(religionService.getProxy(registration.getWife().getReligion().getId()));
        registration.getWitnesses().forEach(witness -> witness.setRegistration(registration));
        registration.setMarriageAct(actService.getAct(registration.getMarriageAct().getId()));
        registration.setDemand(registrationDemandService.createDemand(new BigDecimal(registration.getFeePaid())));
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

        final Map<Long, Document> generalDocumentAndId = new HashMap<Long, Document>();
        documentService.getGeneralDocuments().forEach(document -> generalDocumentAndId.put(document.getId(), document));
        
        addDocumentsToFileStore(null, registration, generalDocumentAndId);
        
        final Map<Long, Document> individualDocumentAndId = new HashMap<Long, Document>();
        documentService.getIndividualDocuments().forEach(document -> individualDocumentAndId.put(document.getId(), document));
        
        applicantService.addDocumentsToFileStore(null, registration.getHusband(), individualDocumentAndId);
        applicantService.addDocumentsToFileStore(null, registration.getWife(), individualDocumentAndId);

        workflowService.transition(registration, workflowContainer, registration.getApprovalComent());

        create(registration);

        return registration.getApplicationNo();
    }

    /**
     * Adds the uploaded registration document to file store and associates with the registration
     *
     * @param registration
     */
    private void addDocumentsToFileStore(final Registration registrationModel, final Registration registration, final Map<Long, Document> documentAndId) {
        List<Document> documents = registrationModel == null ? registration.getDocuments() : registrationModel.getDocuments();
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

    @Transactional
    public Registration forwardRegistration(final Long id, final Registration regModel,
            final WorkflowContainer workflowContainer) {
        final Registration registration = get(id);

        updateRegistrationData(regModel, registration);

        workflowService.transition(registration, workflowContainer, registration.getApprovalComent());
        return update(registration);
    }

    private void updateRegistrationData(final Registration regModel, final Registration registration) {
        registration.setDateOfMarriage(regModel.getDateOfMarriage());
        registration.setPlaceOfMarriage(regModel.getPlaceOfMarriage());
        registration.setFeeCriteria(regModel.getFeeCriteria());
        registration.setFeePaid(regModel.getFeePaid());
        
        registration.setHusband(regModel.getHusband());
        registration.setWife(regModel.getWife());

        registration.getHusband().setReligion(religionService.getProxy(registration.getHusband().getReligion().getId()));
        registration.getWife().setReligion(religionService.getProxy(registration.getWife().getReligion().getId()));
        registration.setMarriageAct(actService.getAct(registration.getMarriageAct().getId()));
        registration.setDemand(registrationDemandService.createDemand(new BigDecimal(registration.getFeePaid())));
        registration.setStatus(ApplicationStatus.Created);

        registration.getWitnesses().clear();

        regModel.getWitnesses().stream().forEach(witness -> {
            witness.setRegistration(registration);
            registration.getWitnesses().add(witness);
        });
        
       try {
           if (registration.getHusband().getPhotoFile().getSize() > 0)
               registration.getHusband().copyPhotoAndSignatureToByteArray();
           
           if (registration.getWife().getPhotoFile().getSize() > 0)
               registration.getWife().copyPhotoAndSignatureToByteArray();
       } catch (IOException e) {
           LOG.error("Error while copying Multipart file bytes", e);
       }

       deleteDocuments(regModel, registration);
       applicantService.deleteDocuments(regModel.getHusband(), registration.getHusband());
       applicantService.deleteDocuments(regModel.getWife(), registration.getWife());
       
       final Map<Long, Document> generalDocumentAndId = new HashMap<Long, Document>();
       documentService.getGeneralDocuments().forEach(document -> generalDocumentAndId.put(document.getId(), document));
       
       addDocumentsToFileStore(regModel, registration, generalDocumentAndId);
       
       final Map<Long, Document> individualDocumentAndId = new HashMap<Long, Document>();
       documentService.getIndividualDocuments().forEach(document -> individualDocumentAndId.put(document.getId(), document));
       
       applicantService.addDocumentsToFileStore(regModel.getHusband(), registration.getHusband(), individualDocumentAndId);
       applicantService.addDocumentsToFileStore(regModel.getWife(), registration.getWife(), individualDocumentAndId);


        registration.getHusband().setProofsAttached(regModel.getHusband().getProofsAttached());
        registration.getWife().setProofsAttached(regModel.getWife().getProofsAttached());
    }

    @Transactional
    public Registration approveRegistration(final Long id, final WorkflowContainer workflowContainer) {
        final Registration registration = get(id);
        registration.setStatus(ApplicationStatus.Approved);
        registration.setRegistrationNo(registrationNoGenerator.generateRegistrationNo());
        workflowService.transition(registration, workflowContainer, registration.getApprovalComent());
        sendSMS(registration);
        sendEmail(registration);
        createRegistrationAppIndex(registration);
        return update(registration);
    }

    private void createRegistrationAppIndex(final Registration registration) {
        final User user = securityUtils.getCurrentUser();
        final ApplicationIndexBuilder applicationIndexBuilder = new ApplicationIndexBuilder(Constants.MODULE_NAME,
                registration.getApplicationNo(),
                registration.getApplicationDate(), FeeType.REGISTRATION.name(),
                registration.getHusband().getFullName().concat(registration.getWife().getFullName()),
                registration.getStatus().name(),
                "/mrs/registration/" + registration.getId(),
                registration.getHusband().getContactInfo().getResidenceAddress(), user.getUsername() + "::" + user.getName())
                        .mobileNumber(registration.getHusband().getContactInfo().getMobileNo());

        applicationIndexService.createApplicationIndex(applicationIndexBuilder.build());
    }

    private void sendSMS(final Registration registration) {
        String msgKey = MSG_KEY_SMS_REGISTRATION_NEW;

        if (registration.getStatus() == ApplicationStatus.Rejected)
            msgKey = MSG_KEY_SMS_REGISTRATION_REJECTION;

        final String message = messageSource.getMessage(msgKey,
                new String[] { registration.getHusband().getFullName(), registration.getWife().getFullName(),
                        registration.getRegistrationNo() },
                null);
        messagingService.sendSMS(registration.getHusband().getContactInfo().getMobileNo(), message);
        messagingService.sendSMS(registration.getWife().getContactInfo().getMobileNo(), message);
    }

    private void sendEmail(final Registration registration) {
        String msgKeyMail = MSG_KEY_EMAIL_REGISTRATION_NEW_EMAIL;
        String msgKeyMailSubject = MSG_KEY_EMAIL_REGISTRATION_NEW_SUBJECT;

        if (registration.getStatus() == ApplicationStatus.Rejected) {
            msgKeyMail = MSG_KEY_EMAIL_REGISTRATION_REJECTION_EMAIL;
            msgKeyMailSubject = MSG_KEY_EMAIL_REGISTRATION_REJECTION_SUBJECT;
        }

        final String message = messageSource.getMessage(msgKeyMail,
                new String[] { registration.getHusband().getFullName(), registration.getWife().getFullName(),
                        registration.getRegistrationNo() },
                null);

        final String subject = messageSource.getMessage(msgKeyMailSubject, null, null);
        messagingService.sendEmail(registration.getHusband().getContactInfo().getEmail(), subject, message);
        messagingService.sendEmail(registration.getWife().getContactInfo().getEmail(), subject, message);
    }

    @Transactional
    public Registration rejectRegistration(final Long id, final WorkflowContainer workflowContainer) {
        final Registration registration = get(id);
        registration.setStatus(ApplicationStatus.Rejected);
        registration.setRejectionReason(workflowContainer.getApproverComments());
        sendSMS(registration);
        sendEmail(registration);
        workflowService.transition(registration, workflowContainer, registration.getApprovalComent());

        return update(registration);
    }

    public List<Registration> getRegistrations() {
        return Collections.emptyList();
        //return registrationRepository.findAll();
    }

    public List<Registration> searchRegistration(final SearchModel searchModel) {
        
        final Criteria criteria = getCurrentSession().createCriteria(Registration.class, "registration");

        if (StringUtils.isNotBlank(searchModel.getRegistrationNo()))
            criteria.add(Restrictions.eq("registrationNo", searchModel.getRegistrationNo()));

        if (searchModel.getDateOfMarriage() != null)
            criteria.add(Restrictions.eq("dateOfMarriage", searchModel.getDateOfMarriage()));

        if (StringUtils.isNotBlank(searchModel.getHusbandName()))
            criteria.createCriteria("husband").add(Restrictions.like("name.firstName", searchModel.getHusbandName()));

        if (StringUtils.isNotBlank(searchModel.getWifeName()))
            criteria.createCriteria("wife").add(Restrictions.ilike("name.firstName", searchModel.getWifeName()));
        
        /*QRegistration registration = QRegistration.registration;
        BooleanExpression withRegistrationNo = null;
        BooleanExpression withDateOfMarriage = null;
        BooleanExpression withHusbandName = null;
        BooleanExpression withWifeName = null;
        
        BooleanExpression expression = null;
        
        if (StringUtils.isNotBlank(searchModel.getRegistrationNo())) {
            withRegistrationNo = registration.registrationNo.eq(searchModel.getRegistrationNo());
            expression = withRegistrationNo;
        }

        if (searchModel.getDateOfMarriage() != null) {
            withDateOfMarriage = registration.dateOfMarriage.eq(searchModel.getDateOfMarriage());
            expression.and(withDateOfMarriage);
        }

        if (StringUtils.isNotBlank(searchModel.getHusbandName())) {
            withHusbandName = registration.husband().name().firstName.equalsIgnoreCase(searchModel.getHusbandName());
            expression.and(withHusbandName);
        }
            
        if (StringUtils.isNotBlank(searchModel.getWifeName())) {
            withWifeName = registration.wife().name().firstName.equalsIgnoreCase(searchModel.getWifeName());
            expression.and(withWifeName);
        }*/
        
         return criteria.list(); //registrationRepository.findAll(expression);
    }

    private FileStoreMapper addToFileStore(final MultipartFile file) {
        FileStoreMapper fileStoreMapper = null;
        try {
            fileStoreMapper = fileStoreService.store(file.getInputStream(), file.getOriginalFilename(),
                    file.getContentType(), Constants.MODULE_NAME);
        } catch (final Exception e) {
            throw new ApplicationRuntimeException("Error occurred while getting inputstream", e);
        }
        return fileStoreMapper;
    }
    
    public void deleteDocuments(final Registration regModel, final Registration registration) {
        List<RegistrationDocument> toDelete = new ArrayList<RegistrationDocument>();
        Map<Long, RegistrationDocument> documentIdAndRegistrationDoc = new HashMap<Long, RegistrationDocument>();
        registration.getRegistrationDocuments().forEach(regDoc -> documentIdAndRegistrationDoc.put(regDoc.getDocument().getId(), regDoc));

        regModel.getDocuments().stream()
            .filter(doc -> doc.getFile().getSize() > 0)
            .map(doc -> {
                RegistrationDocument regDoc = documentIdAndRegistrationDoc.get(doc.getId());
                fileStoreService.delete(regDoc.getFileStoreMapper().getFileStoreId(), Constants.MODULE_NAME);
                return regDoc;
            }).collect(Collectors.toList())
            .forEach(regDoc -> toDelete.add(regDoc));
        
        registrationDocumentService.delete(toDelete);
    }
   
    @Transactional
    public Registration updateRegistration(final Long id, final Registration regModel) {
        final Registration registration = get(id);
        updateRegistrationData(regModel, registration);
        return update(registration);
    }
    
    public List<Registration> searchRegistrationBetweenDateAndStatus(final SearchModel searchModel) {
        ApplicationStatus status = searchModel.isRegistrationApproved() ? ApplicationStatus.Approved : ApplicationStatus.Rejected;
        return registrationRepository.findByCreatedDateAfterAndCreatedDateBeforeAndStatus(searchModel.getFromDate(), searchModel.getToDate(), status);
    }
    
    public void prepareDocumentsForView(final Registration registration) {
        registration.getRegistrationDocuments().forEach(appDoc -> {
            final File file = fileStoreService.fetch(appDoc.getFileStoreMapper().getFileStoreId(), Constants.MODULE_NAME);
            try {
                appDoc.setBase64EncodedFile(Base64.getEncoder().encodeToString(FileCopyUtils.copyToByteArray(file)));
            } catch (final Exception e) {
                LOG.error("Error while preparing the document for view", e);
            }
        });
    }

}
