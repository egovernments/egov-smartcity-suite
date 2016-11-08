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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.egov.commons.EgwStatus;
import org.egov.demand.model.EgDemandDetails;
import org.egov.eis.service.EisCommonService;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.mrs.application.MarriageConstants;
import org.egov.mrs.application.MarriageUtils;
import org.egov.mrs.application.service.MarriageCertificateService;
import org.egov.mrs.application.service.MarriageRegistrationDemandService;
import org.egov.mrs.application.service.workflow.RegistrationWorkflowService;
import org.egov.mrs.autonumber.MarriageRegistrationApplicationNumberGenerator;
import org.egov.mrs.autonumber.MarriageRegistrationNumberGenerator;
import org.egov.mrs.domain.elasticsearch.service.MarriageRegistrationUpdateIndexesService;
import org.egov.mrs.domain.entity.IdentityProof;
import org.egov.mrs.domain.entity.MarriageCertificate;
import org.egov.mrs.domain.entity.MarriageDocument;
import org.egov.mrs.domain.entity.MarriageRegistration;
import org.egov.mrs.domain.entity.MarriageRegistrationSearchFilter;
import org.egov.mrs.domain.entity.MrApplicant;
import org.egov.mrs.domain.entity.ReIssue;
import org.egov.mrs.domain.entity.RegistrationDocument;
import org.egov.mrs.domain.entity.SearchModel;
import org.egov.mrs.domain.repository.MarriageRegistrationRepository;
import org.egov.mrs.domain.repository.WitnessRepository;
import org.egov.mrs.masters.service.MarriageActService;
import org.egov.mrs.masters.service.MarriageFeeService;
import org.egov.mrs.masters.service.MarriageRegistrationUnitService;
import org.egov.mrs.masters.service.ReligionService;
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



    private static final Logger LOG = Logger.getLogger(MarriageRegistrationService.class);
    @Autowired
    private final MarriageRegistrationRepository registrationRepository;
    @PersistenceContext
    private EntityManager entityManager;


    @Autowired
    private MarriageSmsAndEmailService marriageSmsAndEmailService;
    

    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource mrsMessageSource;
    

    @Autowired
    private ReligionService religionService;

    @Autowired
    private MarriageActService marriageActService;

    @Autowired
    private MarriageRegistrationDemandService marriageRegistrationDemandService;

    @Autowired
    private BoundaryService boundaryService;

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
    private MarriageFeeService marriageFeeService;
    
    @Autowired
    private MarriageRegistrationUpdateIndexesService marriageRegistrationUpdateIndexesService;
    
    @Autowired
    private MarriageRegistrationUnitService marriageRegistrationUnitService;
    
    
    
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
    
    public void setMarriageRegData(MarriageRegistration registration){
        registration.setApplicationDate(new Date());
        registration.getHusband().setReligion(religionService.getProxy(registration.getHusband().getReligion().getId()));
        registration.getWife().setReligion(religionService.getProxy(registration.getWife().getReligion().getId()));
        registration.getWitnesses().forEach(witness -> witness.setRegistration(registration));
        registration.setMarriageAct(marriageActService.getAct(registration.getMarriageAct().getId()));
        if (registration.getFeePaid() != null && registration.getDemand() == null){
                registration.setDemand(
                                marriageRegistrationDemandService.createDemand(new BigDecimal(registration.getFeePaid())));
        }
        if (registration.getMarriageRegistrationUnit() != null)
        registration.setMarriageRegistrationUnit(marriageRegistrationUnitService.findById(registration.getMarriageRegistrationUnit().getId()));
        
        if (registration.getPriest().getReligion().getId() != null)
            registration.getPriest().setReligion(religionService.getProxy(registration.getPriest().getReligion().getId()));
        else
            registration.setPriest(null);

        registration.setZone(boundaryService.getBoundaryById(registration.getZone().getId()));
        if(registration.getFeeCriteria()!=null)
        registration.setFeeCriteria(marriageFeeService.getFee(registration.getFeeCriteria().getId()));

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
    }

    @Transactional
    public String createRegistration(final MarriageRegistration registration, final WorkflowContainer workflowContainer) {
        if (StringUtils.isBlank(registration.getApplicationNo())) {
            registration.setApplicationNo(marriageRegistrationApplicationNumberGenerator.getNextApplicationNumberForMarriageRegistration(registration));
        }
        setMarriageRegData(registration);
       registration.setStatus(
             marriageUtils.getStatusByCodeAndModuleType(MarriageRegistration.RegistrationStatus.CREATED.toString(), MarriageConstants.MODULE_NAME)); 
        workflowService.transition(registration, workflowContainer, registration.getApprovalComent());
        create(registration);
        marriageRegistrationUpdateIndexesService.updateIndexes(registration);
        marriageSmsAndEmailService.sendSMS(registration,MarriageRegistration.RegistrationStatus.CREATED.toString());
        marriageSmsAndEmailService.sendEmail(registration,MarriageRegistration.RegistrationStatus.CREATED.toString());

        return registration.getApplicationNo();
    }
    
    @Transactional
    public String createDataEntryMrgRegistration(final MarriageRegistration registration) {
        setMarriageRegData(registration);
        if(registration.getDemand()!=null)
            registration.getDemand().setAmtCollected(registration.getDemand().getBaseDemand());
        registration.setStatus(
                marriageUtils.getStatusByCodeAndModuleType(MarriageRegistration.RegistrationStatus.REGISTERED.toString(), MarriageConstants.MODULE_NAME));
        if(registration.getDemand()!=null && !registration.getDemand().getEgDemandDetails().isEmpty())
            for(EgDemandDetails dd : registration.getDemand().getEgDemandDetails()){
                if(dd!=null)
                    dd.setAmtCollected(dd.getAmount());
            }
        registration.setActive(true);
        registration.setLegacy(true);
        registration.setRegistrationNo(marriageRegistrationNumberGenerator.generateMarriageRegistrationNumber(registration));
        create(registration);
        marriageRegistrationUpdateIndexesService.updateIndexes(registration);
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
        registration.setStatus(
                marriageUtils.getStatusByCodeAndModuleType(MarriageRegistration.RegistrationStatus.CREATED.toString(), MarriageConstants.MODULE_NAME));

        workflowService.transition(registration, workflowContainer, registration.getApprovalComent());
        marriageRegistrationUpdateIndexesService.updateIndexes(registration);
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
        if(regModel.getFeeCriteria()!=null && regModel.getFeeCriteria().getId()!=null)
                registration.setFeeCriteria(marriageFeeService.getFee(regModel.getFeeCriteria().getId()));
        registration.setFeePaid(regModel.getFeePaid());
        registration.setMarriageAct(marriageActService.getAct(registration.getMarriageAct().getId()));
        if (registration.getMarriageRegistrationUnit() != null)
        registration.setMarriageRegistrationUnit(marriageRegistrationUnitService.findById(regModel.getMarriageRegistrationUnit().getId()));
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
                if(!registration.getStatus().getCode().equalsIgnoreCase(MarriageRegistration.RegistrationStatus.APPROVED.toString())){
                        registration.setStatus(
                marriageUtils.getStatusByCodeAndModuleType(MarriageRegistration.RegistrationStatus.CREATED.toString(), MarriageConstants.MODULE_NAME));
                }
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

    private void updateApplicantInfo(final MrApplicant modelApplicant, final MrApplicant dbApplicant) {
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
        registration.setStatus(
                marriageUtils.getStatusByCodeAndModuleType(MarriageRegistration.RegistrationStatus.APPROVED.toString(), MarriageConstants.MODULE_NAME));
        registration.setRegistrationNo(marriageRegistrationNumberGenerator.generateMarriageRegistrationNumber(registration));
        registration = update(registration);
        workflowService.transition(registration, workflowContainer, workflowContainer.getApproverComments());
        marriageRegistrationUpdateIndexesService.updateIndexes(registration);
        marriageSmsAndEmailService.sendSMS(registration,MarriageRegistration.RegistrationStatus.APPROVED.toString());
        marriageSmsAndEmailService.sendEmail(registration,MarriageRegistration.RegistrationStatus.APPROVED.toString());
 
        return registration;
    }
    
    @Transactional
    public MarriageRegistration printCertificate(MarriageRegistration registration, final WorkflowContainer workflowContainer,final HttpServletRequest request) throws IOException {
     
        MarriageCertificate marriageCertificate = marriageCertificateService.generateMarriageCertificate(registration,request);
        registration.setStatus(
                marriageUtils.getStatusByCodeAndModuleType(MarriageRegistration.RegistrationStatus.REGISTERED.toString(), MarriageConstants.MODULE_NAME));
        registration.addCertificate(marriageCertificate);
        registration.setActive(true);
        workflowService.transition(registration, workflowContainer, workflowContainer.getApproverComments()); 
        marriageRegistrationUpdateIndexesService.updateIndexes(registration);
        return registration;
    }
    

    @Transactional
    public MarriageRegistration rejectRegistration(MarriageRegistration registration, final WorkflowContainer workflowContainer) {
        registration.setStatus(workflowContainer.getWorkFlowAction().equalsIgnoreCase(MarriageConstants.WFLOW_ACTION_STEP_REJECT)?
                marriageUtils.getStatusByCodeAndModuleType(MarriageRegistration.RegistrationStatus.REJECTED.toString(), MarriageConstants.MODULE_NAME)
                :marriageUtils.getStatusByCodeAndModuleType(MarriageRegistration.RegistrationStatus.CANCELLED.toString(), MarriageConstants.MODULE_NAME));
        registration.setRejectionReason(workflowContainer.getApproverComments());
        workflowService.transition(registration, workflowContainer, workflowContainer.getApproverComments());
        marriageRegistrationUpdateIndexesService.updateIndexes(registration);
        marriageSmsAndEmailService.sendSMS(registration,MarriageRegistration.RegistrationStatus.REJECTED.toString());
        marriageSmsAndEmailService.sendEmail(registration,MarriageRegistration.RegistrationStatus.REJECTED.toString());
        return registration;
    } 

    public List<MarriageRegistration> getRegistrations() {
        return registrationRepository.findAll();
    }

    @SuppressWarnings("unchecked")
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

            //TODO : need to change to egwstatus
            // Commented for time being. need to fix
          /*  ApplicationStatus status = searchModel.isRegistrationApproved() ? ApplicationStatus.Approved
                    : ApplicationStatus.Rejected;
            criteria.add(Restrictions.eq("status", status));*/
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
                    file.getContentType(), MarriageConstants.FILESTORE_MODULECODE);
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
                    fileStoreService.delete(regDoc.getFileStoreMapper().getFileStoreId(), MarriageConstants.FILESTORE_MODULECODE);
                    return regDoc;
                }).collect(Collectors.toList())
                .forEach(regDoc -> toDelete.add(regDoc));

        registrationDocumentService.delete(toDelete);
    }

    public List<MarriageRegistration> searchRegistrationBetweenDateAndStatus(final SearchModel searchModel) {
        EgwStatus status = searchModel.isRegistrationApproved() ? 
                marriageUtils.getStatusByCodeAndModuleType(MarriageRegistration.RegistrationStatus.APPROVED.toString(), MarriageConstants.MODULE_NAME)
                : marriageUtils.getStatusByCodeAndModuleType(MarriageRegistration.RegistrationStatus.REJECTED.toString(), MarriageConstants.MODULE_NAME);
        return registrationRepository.findByCreatedDateAfterAndCreatedDateBeforeAndStatus(searchModel.getFromDate(),
                searchModel.getToDate(), status);
    }

    public void prepareDocumentsForView(final MarriageRegistration registration) {
       if(registration.getRegistrationDocuments()!=null){ registration.getRegistrationDocuments().forEach(appDoc -> {
            final File file = fileStoreService.fetch(appDoc.getFileStoreMapper().getFileStoreId(), MarriageConstants.FILESTORE_MODULECODE);
            try {
                appDoc.setBase64EncodedFile(Base64.getEncoder().encodeToString(FileCopyUtils.copyToByteArray(file)));
            } catch (final Exception e) {
                LOG.error("Error while preparing the document for view", e);
            }
        });
       }
    }
    
    
    @SuppressWarnings("unchecked")
        public List<MarriageRegistration> searchMarriageRegistrations(MarriageRegistration registration) throws ParseException {
                final Criteria criteria = getCurrentSession().createCriteria(MarriageRegistration.class,"marriageRegistration");
                buildMarriageRegistrationSearchCriteria(registration, criteria);
                return criteria.list();
        }
    
    @SuppressWarnings("unchecked")
    public List<MarriageRegistration> searchMarriageRegistrationsForFeeCollection(MarriageRegistration registration) throws ParseException {
            final Criteria criteria = getCurrentSession().createCriteria(MarriageRegistration.class,"marriageRegistration")
                            .createAlias("marriageRegistration.status", "status");
     buildMarriageRegistrationSearchCriteria(registration, criteria);
             criteria.add(Restrictions.in("status.code", new String[] { MarriageRegistration.RegistrationStatus.APPROVED.toString()}));
            return criteria.list();
    }

    
    @SuppressWarnings("unchecked")
    public List<ReIssue> searchApprovedReIssueRecordsForFeeCollection(final MarriageRegistrationSearchFilter mrSearchFilter) throws ParseException {
            final Criteria criteria = getCurrentSession().createCriteria(ReIssue.class,"reIssue")
                            .createAlias("reIssue.status", "status");
            buildReIssueSearchCriteria(mrSearchFilter, criteria);
             criteria.add(Restrictions.in("status.code", new String[] { ReIssue.ReIssueStatus.APPROVED.toString()}));
            return criteria.list();
    }
    
    
    private void buildReIssueSearchCriteria(final MarriageRegistrationSearchFilter mrSearchFilter, final Criteria criteria) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        if (mrSearchFilter.getRegistrationNo() != null)
                criteria.createAlias("reIssue.registration", "registration").add(Restrictions.ilike("registration.registrationNo", mrSearchFilter.getRegistrationNo(),
                                        MatchMode.ANYWHERE)); 
                if ( mrSearchFilter.getHusbandName() != null)
                criteria.createAlias("reIssue.registration.husband", "husband").add(Restrictions.disjunction( Restrictions.ilike("husband.name.firstName", mrSearchFilter.getHusbandName(),
                                MatchMode.ANYWHERE)).add(Restrictions.ilike("husband.name.lastName", mrSearchFilter.getHusbandName(),
                                                MatchMode.ANYWHERE)).add(Restrictions.ilike("husband.name.middleName", mrSearchFilter.getHusbandName(),
                                                                MatchMode.ANYWHERE))); 
                if ( mrSearchFilter.getWifeName()!=null)
                criteria.createAlias("reIssue.registration.wife", "wife").add(Restrictions.disjunction(Restrictions.ilike("wife.name.firstName",  mrSearchFilter.getWifeName(),
                                MatchMode.ANYWHERE)).add(Restrictions.ilike("wife.name.lastName",  mrSearchFilter.getWifeName(),
                                                MatchMode.ANYWHERE)).add(Restrictions.ilike("wife.name.middleName",  mrSearchFilter.getWifeName(),
                                                                MatchMode.ANYWHERE)));
                if ( mrSearchFilter.getApplicationDate() != null)
                        criteria.add(Restrictions.between("reIssue.applicationDate", sdf.parse(mrSearchFilter.getApplicationDate()),
                                        DateUtils.addDays(sdf.parse(mrSearchFilter.getApplicationDate()), 1)));
                if ( mrSearchFilter.getDateOfMarriage() != null)
                    criteria.createAlias("reIssue.registration", "registration").add(Restrictions.between("registration.dateOfMarriage", sdf.parse(mrSearchFilter.getDateOfMarriage()),
                                        DateUtils.addDays(sdf.parse(mrSearchFilter.getDateOfMarriage()), 1)));
        }
    
    
    private void buildMarriageRegistrationSearchCriteria(MarriageRegistration registration, final Criteria criteria) throws ParseException {
        if (registration.getRegistrationNo() != null)
                        criteria.add(Restrictions.ilike("marriageRegistration.registrationNo", registration.getRegistrationNo(),
                                        MatchMode.ANYWHERE));
        if (registration.getApplicationNo() != null)
                        criteria.add(Restrictions.ilike("marriageRegistration.applicationNo", registration.getApplicationNo(),
                                        MatchMode.ANYWHERE));
                if ( registration.getHusband().getName() != null &&  !registration.getHusband().getFullName().equals("null") && registration.getHusband().getFullName()!=null )
                criteria.createAlias("marriageRegistration.husband", "husband").add(Restrictions.disjunction( Restrictions.ilike("husband.name.firstName", registration.getHusband().getFullName(),
                                MatchMode.ANYWHERE)).add(Restrictions.ilike("husband.name.lastName", registration.getHusband().getFullName(),
                                                MatchMode.ANYWHERE)).add(Restrictions.ilike("husband.name.middleName", registration.getHusband().getFullName(),
                                                                MatchMode.ANYWHERE))); 
                if ( registration.getWife().getName() != null && registration.getWife().getFullName()!=null &&  !registration.getWife().getFullName().equals("null"))
                criteria.createAlias("marriageRegistration.wife", "wife").add(Restrictions.disjunction(Restrictions.ilike("wife.name.firstName", registration.getWife().getFullName(),
                                MatchMode.ANYWHERE)).add(Restrictions.ilike("wife.name.lastName", registration.getWife().getFullName(),
                                                MatchMode.ANYWHERE)).add(Restrictions.ilike("wife.name.middleName", registration.getWife().getFullName(),
                                                                MatchMode.ANYWHERE)));
                if ( registration.getApplicationDate() != null)
                        criteria.add(Restrictions.between("marriageRegistration.applicationDate", registration.getApplicationDate(),
                                        DateUtils.addDays(registration.getApplicationDate(), 1)));
                if ( registration.getDateOfMarriage() != null)
                        criteria.add(Restrictions.between("marriageRegistration.dateOfMarriage", registration.getDateOfMarriage(),
                                        DateUtils.addDays(registration.getDateOfMarriage(), 1)));
                if ( registration.getFromDate() != null && registration.getToDate() != null)
                        criteria.add(Restrictions.between("marriageRegistration.dateOfMarriage", registration.getFromDate(),
                                        DateUtils.addDays(registration.getToDate(), 1)));
                if(null!=registration.getMarriageRegistrationUnit() &&	registration.getMarriageRegistrationUnit().getId() != null)
                	criteria.add(Restrictions.eq("marriageRegistrationUnit.id",registration.getMarriageRegistrationUnit().getId() ));
        }
    
    /**
     * @param registration
     * @return
     */
    public List<Hashtable<String, Object>> getHistory(final MarriageRegistration registration) {
        User user = null;
        final List<Hashtable<String, Object>> historyTable = new ArrayList<Hashtable<String, Object>>();
        final State state = registration.getState();
        final Hashtable<String, Object> map = new Hashtable<String, Object>(0);
        if (null != state) {
            if (!registration.getStateHistory().isEmpty()
                    && registration.getStateHistory() != null)
                Collections.reverse(registration.getStateHistory());
            for (final StateHistory stateHistory : registration.getStateHistory()) {
                final Hashtable<String, Object> HistoryMap = new Hashtable<String, Object>(0);
                HistoryMap.put("date", stateHistory.getDateInfo());
                HistoryMap.put("comments", stateHistory.getComments()!=null?stateHistory.getComments():"");
                HistoryMap.put("updatedBy", stateHistory.getLastModifiedBy().getUsername() + "::"
                        + stateHistory.getLastModifiedBy().getName());
                HistoryMap.put("status", stateHistory.getValue());
                final Position owner = stateHistory.getOwnerPosition();
                user = stateHistory.getOwnerUser();
                if (null != user) {
                    HistoryMap.put("user", user.getUsername() + "::" + user.getName());
                    HistoryMap.put("department",
                            null != eisCommonService.getDepartmentForUser(user.getId()) ? eisCommonService
                                    .getDepartmentForUser(user.getId()).getName() : "");
                } else if (null != owner && null != owner.getDeptDesig()) {
                    user = eisCommonService.getUserForPosition(owner.getId(), new Date());
                    HistoryMap
                            .put("user", null != user.getUsername() ? user.getUsername() + "::" + user.getName() : "");
                    HistoryMap.put("department", null != owner.getDeptDesig().getDepartment() ? owner.getDeptDesig()
                            .getDepartment().getName() : "");
                }
                historyTable.add(HistoryMap);
            }

            map.put("date", state.getDateInfo());
            map.put("comments", state.getComments() != null ? state.getComments() : "");
            map.put("updatedBy", state.getLastModifiedBy().getUsername() + "::" + state.getLastModifiedBy().getName());
            map.put("status", state.getValue());
            final Position ownerPosition = state.getOwnerPosition();
            user = state.getOwnerUser();
            if (null != user) {
                map.put("user", user.getUsername() + "::" + user.getName());
                map.put("department", null != eisCommonService.getDepartmentForUser(user.getId()) ? eisCommonService
                        .getDepartmentForUser(user.getId()).getName() : "");
            } else if (null != ownerPosition && null != ownerPosition.getDeptDesig()) {
                user = eisCommonService.getUserForPosition(ownerPosition.getId(), new Date());
                map.put("user", null != user.getUsername() ? user.getUsername() + "::" + user.getName() : "");
                map.put("department", null != ownerPosition.getDeptDesig().getDepartment() ? ownerPosition
                        .getDeptDesig().getDepartment().getName() : "");
            }
            historyTable.add(map);
        }
        return historyTable;
    }

        @SuppressWarnings("unchecked")
                public List<MarriageRegistration> searchRegistrationByStatus(MarriageRegistration registration,String status) throws ParseException {

                final Criteria criteria = getCurrentSession().createCriteria(MarriageRegistration.class,"marriageRegistration")
                                .createAlias("marriageRegistration.status", "status");
         buildMarriageRegistrationSearchCriteria(registration, criteria);
        if(status != null)
                 criteria.add(Restrictions.in("status.code", new String[] {status}));
                return criteria.list();
        
        }
        
       
}