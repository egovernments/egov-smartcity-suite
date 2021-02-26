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
package org.egov.stms.transactions.service;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.String.format;
import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.time.DateUtils.addDays;
import static org.egov.infra.utils.DateUtils.getFormattedDate;
import static org.egov.stms.utils.constants.SewerageTaxConstants.ANONYMOUS_USER;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_ANONYMOUSCREATED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_CANCELLED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_CITIZENCREATED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_CLOSERSANCTIONED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_COLLECTINSPECTIONFEE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_CREATED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_CSCCREATED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_DEEAPPROVED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_EEAPPROVED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_ESTIMATENOTICEGEN;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_FEECOLLECTIONPENDING;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_FEEPAID;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_FINALAPPROVED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_INITIALAPPROVED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_INSPECTIONFEEPAID;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_REJECTED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_SANCTIONED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_WARDSECRETARYCREATED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_WOGENERATED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPL_INDEX_MODULE_NAME;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPROVEWORKFLOWACTION;
import static org.egov.stms.utils.constants.SewerageTaxConstants.CHANGEINCLOSETS;
import static org.egov.stms.utils.constants.SewerageTaxConstants.CHANGEINCLOSETS_NOCOLLECTION;
import static org.egov.stms.utils.constants.SewerageTaxConstants.CLOSESEWERAGECONNECTION;
import static org.egov.stms.utils.constants.SewerageTaxConstants.DEMANDISHISTORY;
import static org.egov.stms.utils.constants.SewerageTaxConstants.DOCTYPE_OTHERS;
import static org.egov.stms.utils.constants.SewerageTaxConstants.EXECUTIVE_ENGINEER_APPROVAL_PENDING;
import static org.egov.stms.utils.constants.SewerageTaxConstants.FEES_DONATIONCHARGE_CODE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.MODULETYPE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.NA;
import static org.egov.stms.utils.constants.SewerageTaxConstants.NEWSEWERAGECONNECTION;
import static org.egov.stms.utils.constants.SewerageTaxConstants.NOTICE_TYPE_WORK_ORDER_NOTICE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WARDSECRETARY_EVENTPUBLISH_MODE_CREATE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WARDSECRETARY_EVENTPUBLISH_MODE_UPDATE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WARDSECRETARY_TRANSACTIONID_CODE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WFLOW_ACTION_STEP_CANCEL;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WFLOW_ACTION_STEP_FORWARD;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WFLOW_ACTION_STEP_REJECT;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WF_INSPECTIONFEE_COLLECTION;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WF_STATE_ASSISTANT_APPROVED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WF_STATE_CLERK_APPROVED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WF_STATE_DEPUTY_EXE_APPROVED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WF_STATE_EE_APPROVED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WF_STATE_INSPECTIONFEE_COLLECTED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WF_STATE_INSPECTIONFEE_PENDING;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WF_STATE_PAYMENTDONE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WF_STATE_REJECTED;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.egov.commons.Installment;
import org.egov.commons.entity.Source;
import org.egov.demand.model.EgDemand;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.EisCommonService;
import org.egov.eis.service.PositionMasterService;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.integration.event.model.ApplicationDetails;
import org.egov.infra.integration.event.model.enums.ApplicationStatus;
import org.egov.infra.integration.event.model.enums.TransactionStatus;
import org.egov.infra.integration.event.publisher.ThirdPartyApplicationEventPublisher;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.egov.infra.web.utils.WebUtils;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.pims.commons.Position;
import org.egov.portal.entity.PortalInbox;
import org.egov.portal.entity.PortalInboxBuilder;
import org.egov.portal.service.PortalInboxService;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.OwnerName;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.search.elasticsearch.entity.ApplicationIndex;
import org.egov.search.elasticsearch.entity.enums.ApprovalStatus;
import org.egov.search.elasticsearch.entity.enums.ClosureStatus;
import org.egov.search.elasticsearch.service.ApplicationIndexService;
import org.egov.stms.autonumber.SewerageApplicationNumberGenerator;
import org.egov.stms.autonumber.SewerageCloseConnectionNoticeNumberGenerator;
import org.egov.stms.autonumber.SewerageEstimationNumberGenerator;
import org.egov.stms.autonumber.SewerageRejectionNoticeNumberGenerator;
import org.egov.stms.autonumber.SewerageSHSCNumberGenerator;
import org.egov.stms.autonumber.SewerageWorkOrderNumberGenerator;
import org.egov.stms.masters.entity.enums.SewerageConnectionStatus;
import org.egov.stms.masters.service.DocumentTypeMasterService;
import org.egov.stms.notice.entity.SewerageNotice;
import org.egov.stms.notice.service.SewerageNoticeService;
import org.egov.stms.service.es.SewerageIndexService;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.transactions.entity.SewerageApplicationDetailsDocument;
import org.egov.stms.transactions.entity.SewerageConnection;
import org.egov.stms.transactions.entity.SewerageConnectionFee;
import org.egov.stms.transactions.entity.SewerageDemandConnection;
import org.egov.stms.transactions.entity.SewerageDemandDetail;
import org.egov.stms.transactions.repository.SewerageApplicationDetailsRepository;
import org.egov.stms.transactions.workflow.ApplicationWorkflowCustomDefaultImpl;
import org.egov.stms.utils.SewerageTaxUtils;
import org.egov.stms.utils.constants.SewerageTaxConstants;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class SewerageApplicationDetailsService {

    private static final String SHOW_APPROVAL_DTLS = "showApprovalDtls";
    private static final String MODE = "mode";
    private static final String ELECTION_WARD = "electionWard";
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
    private static final String APP_TYPE = "appType";

    private static final Logger LOG = LoggerFactory.getLogger(SewerageApplicationDetailsService.class);
    private static final String STMS_APPLICATION_VIEW = "/stms/application/view/%s/%s";
    private static final String STMS_APPLICATION_UPDATE = "/stms/transactions/citizenupdate/%s";
    private static final String APPLICATION_WORKFLOW_INITIALIZATION_DONE = "applicationWorkflowCustomDefaultImpl initialization is done";
    private static final String DEPARTMENT = "department";
    private static final String NEW_STATE = "NEW";

    @Autowired
    private SewerageApplicationDetailsRepository sewerageApplicationDetailsRepository;

    @Autowired
    private SewerageTaxUtils sewerageTaxUtils;

    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource stmsMessageSource;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private ApplicationIndexService applicationIndexService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private AutonumberServiceBeanResolver beanResolver;

    @Autowired
    private SewerageDemandService sewerageDemandService;

    @Autowired
    @Qualifier("seweargeApplicationWorkflowCustomDefaultImpl")
    private ApplicationWorkflowCustomDefaultImpl applicationWorkflowCustomDefaultImpl;

    @Autowired
    private EisCommonService eisCommonService;

    @Autowired
    private SewerageIndexService sewerageIndexService;

    @Autowired
    private SewerageConnectionSmsAndEmailService sewerageConnectionSmsAndEmailService;

    @Autowired
    private SewerageNoticeService sewerageNoticeService;

    @Autowired
    private DocumentTypeMasterService documentTypeMasterService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private PortalInboxService portalInboxService;
    
    @Autowired
    private PositionMasterService positionMasterService;
    
    @Autowired
    private SewerageWorkflowService sewerageWorkflowService;
    
    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<SewerageApplicationDetails> sewerageApplicationWorkflowService;

    private ThirdPartyApplicationEventPublisher thirdPartyApplicationEventPublisher;
    
    @Autowired
    private SewerageDemandVoucherService sewerageDemandVoucherService;

    public SewerageApplicationDetails findBy(final Long id) {
        return sewerageApplicationDetailsRepository.findOne(id);
    }

    public SewerageApplicationDetails findByApplicationNumber(final String applicationNumber) {
        return sewerageApplicationDetailsRepository.findByApplicationNumber(applicationNumber);
    }

    public SewerageApplicationDetails findByApplicationNumberAndConnectionStatus(final String applicationNumber,
                                                                                 final SewerageConnectionStatus status) {
        return sewerageApplicationDetailsRepository.findByApplicationNumberAndConnectionStatus(applicationNumber,
                status);
    }


    public SewerageApplicationDetails findByConnectionShscNumberAndConnectionStatus(final String shscNumber,
                                                                                    final SewerageConnectionStatus status) {
        return sewerageApplicationDetailsRepository.findByConnectionShscNumberAndConnectionStatus(shscNumber,
                status);
    }

    public List<SewerageApplicationDetails> findByConnectionShscNumber(final String shscNumber) {
        return sewerageApplicationDetailsRepository.findByConnectionShscNumber(shscNumber);
    }

    @Transactional
    public void updateExecutionDate(final SewerageApplicationDetails connectionExeList) {
        sewerageApplicationDetailsRepository.saveAndFlush(connectionExeList);
    }

    @Transactional
    public SewerageApplicationDetails createLegacySewerageConnection(
            final SewerageApplicationDetails sewerageApplicationDetails, final HttpServletRequest request) {

        if (sewerageApplicationDetails.getApplicationNumber() == null) {
            final SewerageApplicationNumberGenerator sewerageApplnNumberGenerator = beanResolver
                    .getAutoNumberServiceFor(SewerageApplicationNumberGenerator.class);
            if (sewerageApplnNumberGenerator != null)
                sewerageApplicationDetails.setApplicationNumber(sewerageApplnNumberGenerator
                        .generateNextApplicationNumber(sewerageApplicationDetails));
        }
        sewerageApplicationDetails.getConnection().setLegacy(true);
        sewerageApplicationDetails.getConnection().setStatus(SewerageConnectionStatus.ACTIVE);
        sewerageApplicationDetails.setActive(true);
        sewerageApplicationDetails.setApplicationDate(new Date());
        final Date disposalDate = getDisposalDate(sewerageApplicationDetails,
                sewerageApplicationDetails.getApplicationType().getProcessingTime());
        sewerageApplicationDetails.setDisposalDate(disposalDate);
        // checking is donation charge collection is required ?
        if (sewerageTaxUtils.isDonationChargeCollectionRequiredForLegacy()) {
            // Capturing pending Donation charge for legacy records
            SewerageDemandDetail sewerageDemandDetail = new SewerageDemandDetail();
            BigDecimal donationaAmtCollected = new BigDecimal(request.getParameter("amountCollected"));
            sewerageDemandDetail.setActualCollection(donationaAmtCollected);
            for (final SewerageConnectionFee fees : sewerageApplicationDetails.getConnectionFees()) {
                if (FEES_DONATIONCHARGE_CODE.equals(fees.getFeesDetail().getCode())) {
                    sewerageDemandDetail.setActualAmount(BigDecimal.valueOf(fees.getAmount()));
                }
            }
            sewerageDemandDetail.setInstallmentId(sewerageDemandService.getCurrentInstallment().getId());
            sewerageDemandDetail.setReasonMaster(FEES_DONATIONCHARGE_CODE);
            sewerageApplicationDetails.getDemandDetailBeanList().add(sewerageDemandDetail);
        }

        if (sewerageApplicationDetails.getCurrentDemand() == null) {
            final EgDemand demand = sewerageDemandService.createDemandOnLegacyConnection(
                    sewerageApplicationDetails.getDemandDetailBeanList(), sewerageApplicationDetails);
            if (demand != null) {
                final SewerageDemandConnection sdc = new SewerageDemandConnection();
                sdc.setDemand(demand);
                sdc.setApplicationDetails(sewerageApplicationDetails);
                sewerageApplicationDetails.addDemandConnections(sdc);
            }
        }
        sewerageApplicationDetailsRepository.save(sewerageApplicationDetails);
        updateIndexes(sewerageApplicationDetails);
        return sewerageApplicationDetails;
    }

    @Transactional
    public SewerageApplicationDetails createNewSewerageConnection(
            final SewerageApplicationDetails sewerageApplicationDetails, final MultipartFile[] files,
            final HttpServletRequest request) {

        if (sewerageApplicationDetails.getApplicationNumber() == null) {
            final SewerageApplicationNumberGenerator sewerageApplnNumberGenerator = beanResolver
                    .getAutoNumberServiceFor(SewerageApplicationNumberGenerator.class);
            if (sewerageApplnNumberGenerator != null)
                sewerageApplicationDetails.setApplicationNumber(sewerageApplnNumberGenerator
                        .generateNextApplicationNumber(sewerageApplicationDetails));
        }
        sewerageApplicationDetails.setApplicationDate(new Date());
        final Date disposalDate = getDisposalDate(sewerageApplicationDetails,
                sewerageApplicationDetails.getApplicationType().getProcessingTime());
        sewerageApplicationDetails.setDisposalDate(disposalDate);

        if (LOG.isDebugEnabled())
            LOG.debug(APPLICATION_WORKFLOW_INITIALIZATION_DONE);
        if (!sewerageApplicationDetails.getApplicationType().getCode().equalsIgnoreCase(CLOSESEWERAGECONNECTION)
                && sewerageApplicationDetails.getCurrentDemand() == null) {
            final EgDemand demand = sewerageDemandService.createDemandOnNewConnection(
                    sewerageApplicationDetails.getConnectionFees(), sewerageApplicationDetails);
            if (demand != null) {
                final SewerageDemandConnection sdc = new SewerageDemandConnection();
                sdc.setDemand(demand);
                sdc.setApplicationDetails(sewerageApplicationDetails);
                sewerageApplicationDetails.addDemandConnections(sdc);
            }
        }
        final Set<FileStoreMapper> fileStoreSet = sewerageTaxUtils.addToFileStore(files);
        if (fileStoreSet != null && !fileStoreSet.isEmpty()) {
            final List<SewerageApplicationDetailsDocument> appDetailDocList = new ArrayList<>();
            final SewerageApplicationDetailsDocument appDetailDoc = new SewerageApplicationDetailsDocument();
            appDetailDoc.setApplicationDetails(sewerageApplicationDetails);
            appDetailDoc.setDocumentTypeMaster(documentTypeMasterService
                    .findByApplicationTypeAndDescription(sewerageApplicationDetails.getApplicationType(), DOCTYPE_OTHERS));
            appDetailDoc.setFileStore(fileStoreSet);
            appDetailDocList.add(appDetailDoc);
            sewerageApplicationDetails.setAppDetailsDocument(appDetailDocList);
        }
        SewerageApplicationDetails savedSewerageApplicationDetails = sewerageApplicationDetailsRepository.save(sewerageApplicationDetails);

        if (sewerageApplicationDetails.getApplicationType().getCode().equalsIgnoreCase(CLOSESEWERAGECONNECTION))
            applicationWorkflowCustomDefaultImpl.createCloseConnectionWorkflowTransition(sewerageApplicationDetails);

        else
            applicationWorkflowCustomDefaultImpl.createCommonWorkflowTransition(sewerageApplicationDetails);

        if (securityUtils.currentUserIsCitizen())
            pushPortalMessage(savedSewerageApplicationDetails);
        updateIndexes(sewerageApplicationDetails);
        if (APPLICATION_STATUS_COLLECTINSPECTIONFEE.equalsIgnoreCase(sewerageApplicationDetails
                .getStatus().getCode()) || APPLICATION_STATUS_CREATED.equalsIgnoreCase(sewerageApplicationDetails
                .getStatus().getCode()))
            sewerageConnectionSmsAndEmailService.sendSmsAndEmail(sewerageApplicationDetails, request);
        return sewerageApplicationDetails;
    }

    @Transactional
    public void save(final SewerageApplicationDetails detail) {
        sewerageApplicationDetailsRepository.save(detail);
    }

    public Date getDisposalDate(final SewerageApplicationDetails sewerageApplicationDetails,
                                final Integer appProcessTime) {
        final Calendar c = Calendar.getInstance();
        c.setTime(sewerageApplicationDetails.getApplicationDate());
        c.add(Calendar.DATE, appProcessTime);
        return c.getTime();
    }

    public List<SewerageApplicationDetails> getSewerageConnectionDetailsByPropertyIDentifier(final String propertyIdentifier) {
        return sewerageApplicationDetailsRepository.findByIsActiveTrueAndConnectionDetailPropertyIdentifierOrderByIdDesc(propertyIdentifier);
    }

    public SewerageApplicationDetails findByShscNumberAndIsActive(final String shscNumber) {
        return sewerageApplicationDetailsRepository.findByConnectionShscNumberAndIsActiveTrue(shscNumber);
    }

    public String checkValidPropertyAssessmentNumber(final String asessmentNumber) {
        String errorMessage = "";
        final AssessmentDetails assessmentDetails = sewerageTaxUtils.getAssessmentDetailsForFlag(asessmentNumber,
                PropertyExternalService.FLAG_FULL_DETAILS);
        errorMessage = validateProperty(assessmentDetails);
        if (errorMessage.isEmpty())
            errorMessage = validatePTDue(asessmentNumber, assessmentDetails);
        return errorMessage;
    }

    /**
     * @param assessmentDetails
     * @return ErrorMessage If PropertyId is Not Valid
     */
    private String validateProperty(final AssessmentDetails assessmentDetails) {
        String errorMessage = "";
        if (assessmentDetails.getErrorDetails() != null && assessmentDetails.getErrorDetails().getErrorCode() != null)
            errorMessage = assessmentDetails.getErrorDetails().getErrorMessage();
        return errorMessage;
    }

    private String validatePTDue(final String asessmentNumber, final AssessmentDetails assessmentDetails) {
        String errorMessage = EMPTY;
        /**
         * If property tax due present and configuration value is 'NO' then restrict not to allow new water tap connection
         * application. If configuration value is 'YES' then new water tap connection can be created even though there is
         * Property Tax Due present.
         **/
        if (!sewerageTaxUtils.isNewConnectionAllowedIfPTDuePresent()
                && assessmentDetails.getPropertyDetails() != null
                && assessmentDetails.getPropertyDetails().getTaxDue().doubleValue() > 0)
            errorMessage = stmsMessageSource.getMessage("err.validate.seweragenewconnection.property.taxdue", new String[]{
                    assessmentDetails.getPropertyDetails().getTaxDue().toString(), asessmentNumber, "new"}, null);
        return errorMessage;
    }

    public String checkConnectionPresentForProperty(final String propertyID) {
        String validationMessage = "";
        SewerageApplicationDetails sewerageApplicationDetails = getApplicationDetailByPropertyIdAndStatusExcluded(propertyID,
                Arrays.asList("CANCELLED", "CLOSERSANCTIONED"));
        if (sewerageApplicationDetails != null)
            if (sewerageApplicationDetails.getConnection().getStatus().toString()
                    .equalsIgnoreCase(SewerageConnectionStatus.ACTIVE.toString()))
                validationMessage = stmsMessageSource.getMessage("err.validate.seweragenewconnection.active", new String[]{
                        sewerageApplicationDetails.getConnection().getShscNumber(), propertyID}, null);
            else if (sewerageApplicationDetails.getConnection().getStatus().toString()
                    .equalsIgnoreCase(SewerageConnectionStatus.INPROGRESS.toString()))
                validationMessage = stmsMessageSource.getMessage("err.validate.seweragenewconnection.application.inprocess",
                        new String[]{propertyID, sewerageApplicationDetails.getApplicationNumber()}, null);
            else if (sewerageApplicationDetails.getConnection().getStatus().toString()
                    .equalsIgnoreCase(SewerageConnectionStatus.INACTIVE.toString()))
                validationMessage = stmsMessageSource.getMessage("err.validate.seweragenewconnection.inactive", new String[]{
                        sewerageApplicationDetails.getConnection().getShscNumber(), propertyID}, null);
        return validationMessage;
    }

    public void updateIndexes(final SewerageApplicationDetails sewerageApplicationDetails) {
        final SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        try {
            if (sewerageApplicationDetails.getConnection() != null
                    && sewerageApplicationDetails.getConnection().getExecutionDate() != null) {
                final String executionDate = myFormat.format(sewerageApplicationDetails.getConnection().getExecutionDate());
                sewerageApplicationDetails.getConnection().setExecutionDate(myFormat.parse(executionDate));
            }
            if (sewerageApplicationDetails.getDisposalDate() != null) {
                final String disposalDate = myFormat.format(sewerageApplicationDetails.getDisposalDate());
                sewerageApplicationDetails.setDisposalDate(myFormat.parse(disposalDate));
            }
            if (sewerageApplicationDetails.getApplicationDate() != null) {
                final String applicationDate = myFormat.format(sewerageApplicationDetails.getApplicationDate());
                sewerageApplicationDetails.setApplicationDate(myFormat.parse(applicationDate));
            }
            if (sewerageApplicationDetails.getEstimationDate() != null) {
                final String estimationDate = myFormat.format(sewerageApplicationDetails.getEstimationDate());
                sewerageApplicationDetails.setEstimationDate(myFormat.parse(estimationDate));
            }
            if (sewerageApplicationDetails.getWorkOrderDate() != null) {
                final String workOrderDate = myFormat.format(sewerageApplicationDetails.getWorkOrderDate());
                sewerageApplicationDetails.setWorkOrderDate(myFormat.parse(workOrderDate));
            }
            if (sewerageApplicationDetails.getClosureNoticeDate() != null) {
                final String closureNoticeDate = myFormat.format(sewerageApplicationDetails.getClosureNoticeDate());
                sewerageApplicationDetails.setClosureNoticeDate(myFormat.parse(closureNoticeDate));
            }
        } catch (final ParseException e) {
            LOG.error("Exception parsing Date " + e.getMessage());
        }

        // Pending : Need to make Rest API call to get assessmentdetails
        final AssessmentDetails assessmentDetails = sewerageTaxUtils.getAssessmentDetailsForFlag(
                sewerageApplicationDetails.getConnectionDetail().getPropertyIdentifier(),
                PropertyExternalService.FLAG_FULL_DETAILS);
        if (LOG.isDebugEnabled())
            LOG.debug(" updating Indexes Started... ");
        Iterator<OwnerName> ownerNameItr = null;
        if (null != assessmentDetails.getOwnerNames())
            ownerNameItr = assessmentDetails.getOwnerNames().iterator();
        final StringBuilder consumerName = new StringBuilder();
        final StringBuilder mobileNumber = new StringBuilder();
        Assignment assignment = null;
        User user = null;
        final StringBuilder aadharNumber = new StringBuilder();
        if (null != ownerNameItr && ownerNameItr.hasNext()) {
            final OwnerName primaryOwner = ownerNameItr.next();
            consumerName.append(primaryOwner.getOwnerName() != null ? primaryOwner.getOwnerName() : "");
            mobileNumber.append(primaryOwner.getMobileNumber() != null ? primaryOwner.getMobileNumber() : "");
            aadharNumber.append(primaryOwner.getAadhaarNumber() != null ? primaryOwner.getAadhaarNumber() : "");
            while (ownerNameItr.hasNext()) {
                final OwnerName secondaryOwner = ownerNameItr.next();
                consumerName.append(",").append(
                        secondaryOwner.getOwnerName() != null ? secondaryOwner.getOwnerName() : "");
                mobileNumber.append(",").append(
                        secondaryOwner.getMobileNumber() != null ? secondaryOwner.getMobileNumber() : "");
                aadharNumber.append(",").append(
                        secondaryOwner.getAadhaarNumber() != null ? secondaryOwner.getAadhaarNumber() : "");
            }

        }
        List<Assignment> asignList = null;
        if (sewerageApplicationDetails.getState() != null
                && sewerageApplicationDetails.getState().getOwnerPosition() != null) {
            assignment = assignmentService.getPrimaryAssignmentForPositionAndDate(sewerageApplicationDetails.getState()
                    .getOwnerPosition().getId(), new Date());
            if (assignment != null) {
                asignList = new ArrayList<>();
                asignList.add(assignment);
            } else if (assignment == null)
                asignList = assignmentService.getAssignmentsForPosition(sewerageApplicationDetails.getState()
                        .getOwnerPosition().getId(), new Date());
            if (!asignList.isEmpty())
                user = userService.getUserById(asignList.get(0).getEmployee().getId());
        } else
            user = securityUtils.getCurrentUser();

        // For legacy application - create only SewarageIndex
        if (sewerageApplicationDetails.getConnection().getLegacy()
                && (null == sewerageApplicationDetails.getId()
                || null != sewerageApplicationDetails.getId() && sewerageApplicationDetails
                .getStatus().getCode().equalsIgnoreCase(APPLICATION_STATUS_SANCTIONED))) {
            sewerageIndexService.createSewarageIndex(sewerageApplicationDetails, assessmentDetails);
            return;
        }

        ApplicationIndex applicationIndex = applicationIndexService.findByApplicationNumber(sewerageApplicationDetails
                .getApplicationNumber());
        // update existing application index
        if (applicationIndex != null && null != sewerageApplicationDetails.getId()) {
            applicationIndex.setStatus(sewerageApplicationDetails.getStatus().getDescription());
            applicationIndex.setOwnerName(user == null ? EMPTY : user.getUsername() + "::" + user.getName());

            // mark application index as closed on Connection Sanction
            if (APPLICATION_STATUS_SANCTIONED.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode())) {
                applicationIndex.setApproved(ApprovalStatus.APPROVED);
                applicationIndex.setClosed(ClosureStatus.YES);
            }
            // mark application index as rejected and closed on Connection
            // cancellation
            else if (APPLICATION_STATUS_CANCELLED.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode())) {
                applicationIndex.setApproved(ApprovalStatus.REJECTED);
                applicationIndex.setClosed(ClosureStatus.YES);
            }

            if (sewerageApplicationDetails.getConnection().getShscNumber() != null)
                applicationIndex.setConsumerCode(sewerageApplicationDetails.getConnection().getShscNumber());
            applicationIndexService.updateApplicationIndex(applicationIndex);

            sewerageIndexService.createSewarageIndex(sewerageApplicationDetails, assessmentDetails);
        } else {
            // Create New ApplicationIndex on create sewerage connection
            if (sewerageApplicationDetails.getApplicationDate() == null)
                sewerageApplicationDetails.setApplicationDate(new Date());
            if (LOG.isDebugEnabled())
                LOG.debug("Application Index creation Started... ");

            Integer slaForSewerageConn = sewerageTaxUtils.getSlaAppConfigValues(sewerageApplicationDetails.getApplicationType());
            applicationIndex = ApplicationIndex.builder().withModuleName(APPL_INDEX_MODULE_NAME)
                    .withApplicationNumber(sewerageApplicationDetails.getApplicationNumber())
                    .withApplicationDate(sewerageApplicationDetails.getApplicationDate())
                    .withApplicationType(sewerageApplicationDetails.getApplicationType().getName())
                    .withApplicantName(consumerName.toString())
                    .withStatus(sewerageApplicationDetails.getStatus().getDescription()).withUrl(
                            String.format(STMS_APPLICATION_VIEW, sewerageApplicationDetails.getApplicationNumber(),
                                    sewerageApplicationDetails.getConnectionDetail().getPropertyIdentifier()))
                    .withApplicantAddress(assessmentDetails.getPropertyAddress())
                    .withOwnername(user == null ? EMPTY : user.getUsername() + "::" + user.getName())
                    .withChannel(sewerageApplicationDetails.getSource() == null ? Source.SYSTEM.toString()
                            : sewerageApplicationDetails.getSource())
                    .withDisposalDate(sewerageApplicationDetails.getDisposalDate())
                    .withMobileNumber(mobileNumber.toString()).withClosed(ClosureStatus.NO)
                    .withAadharNumber(aadharNumber.toString())
                    .withSla(slaForSewerageConn)
                    .withApproved(ApprovalStatus.INPROGRESS).build();
            applicationIndexService.createApplicationIndex(applicationIndex);
            if (LOG.isDebugEnabled())
                LOG.debug("Application Index creation completed...");
            sewerageIndexService.createSewarageIndex(sewerageApplicationDetails, assessmentDetails);
        }
    }


    public BigDecimal getTotalAmount(final SewerageApplicationDetails sewerageApplicationDetails) {
        final BigDecimal balance = BigDecimal.ZERO;
        if (sewerageApplicationDetails != null) {
            final EgDemand currentDemand = sewerageApplicationDetails.getCurrentDemand();
            if (currentDemand != null) {
                /*
                 * final List<Object> instVsAmt = connectionDemandService.getDmdCollAmtInstallmentWise (currentDemand); for (final
                 * Object object : instVsAmt) { final Object[] ddObject = (Object[]) object; final BigDecimal dmdAmt =
                 * (BigDecimal) ddObject[2]; BigDecimal collAmt = BigDecimal.ZERO; if (ddObject[2] != null) collAmt = new
                 * BigDecimal((Double) ddObject[3]); balance = balance.add(dmdAmt.subtract(collAmt)); }
                 */
            }
        }
        return balance;
    }

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    public Map<String, String> showApprovalDetailsByApplcationCurState(
            final SewerageApplicationDetails sewerageApplicationDetails) {
        final Map<String, String> modelParams = new HashMap<>();
        if (sewerageApplicationDetails.getState() != null) {
            final String currentState = sewerageApplicationDetails.getState().getValue();
            if (currentState.equalsIgnoreCase(WF_STATE_INSPECTIONFEE_PENDING)
                    || (currentState.equalsIgnoreCase(WF_STATE_DEPUTY_EXE_APPROVED) && !sewerageApplicationDetails.getState()
                    .getNextAction().equalsIgnoreCase(EXECUTIVE_ENGINEER_APPROVAL_PENDING))
                    || currentState.equalsIgnoreCase(WF_STATE_EE_APPROVED))
                modelParams.put(SHOW_APPROVAL_DTLS, "no");
            else
                modelParams.put(SHOW_APPROVAL_DTLS, "yes");
            if (currentState.equalsIgnoreCase(WF_STATE_INSPECTIONFEE_COLLECTED)
                    || currentState.equalsIgnoreCase(WF_STATE_ASSISTANT_APPROVED)
                    || (currentState.equalsIgnoreCase(WF_STATE_DEPUTY_EXE_APPROVED) && !sewerageApplicationDetails.getState()
                    .getNextAction().equalsIgnoreCase(EXECUTIVE_ENGINEER_APPROVAL_PENDING))
                    || currentState.equalsIgnoreCase(WF_STATE_CLERK_APPROVED))
                modelParams.put(MODE, "edit");
            else if (currentState.equalsIgnoreCase(WF_STATE_REJECTED))
                modelParams.put(MODE, "editOnReject");
			else if (NEW_STATE.equalsIgnoreCase(currentState)
					&& (APPLICATION_STATUS_CSCCREATED.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode())
							|| APPLICATION_STATUS_ANONYMOUSCREATED
									.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode())
							|| APPLICATION_STATUS_CITIZENCREATED
									.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode())
							|| APPLICATION_STATUS_WARDSECRETARYCREATED
									.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode())))
                modelParams.put(MODE, "closetview");
            else
                modelParams.put(MODE, "view");
        }
        return modelParams;
    }

    public SewerageApplicationDetails updateSewerageApplicationDetails(
            final SewerageApplicationDetails sewerageApplicationDetails) {
        return sewerageApplicationDetailsRepository.saveAndFlush(sewerageApplicationDetails);
    }

    @Transactional
    public SewerageApplicationDetails updateSewerageApplicationDetails(
            final SewerageApplicationDetails sewerageApplicationDetails, final String mode,
            final HttpServletRequest request, final HttpSession session, boolean generateDemandVoucher) {

        // In change in closet if demand reduced, sewerage tax collection shld not be done. Hence directly fwd application from
        // DEE to EE and
        // also generate workorder notice no for dee approved applications
        String workFlowAction = sewerageApplicationDetails.getWorkflowContainer().getWorkFlowAction();
        String additionalRule = sewerageApplicationDetails.getWorkflowContainer().getAdditionalRule();
        String approverComments = sewerageApplicationDetails.getWorkflowContainer().getApproverComments();
        if (sewerageApplicationDetails.getStatus().getCode().equalsIgnoreCase(APPLICATION_STATUS_FEEPAID)
                && (sewerageApplicationDetails.getState().getValue().equalsIgnoreCase(WF_STATE_PAYMENTDONE)
                || sewerageApplicationDetails.getState().getValue().equalsIgnoreCase(WF_STATE_EE_APPROVED))
                && workFlowAction.equals(APPROVEWORKFLOWACTION)
                || sewerageApplicationDetails.getStatus().getCode().equalsIgnoreCase(APPLICATION_STATUS_DEEAPPROVED)
                && additionalRule != null && additionalRule.equalsIgnoreCase(CHANGEINCLOSETS_NOCOLLECTION)) {
            if (sewerageApplicationDetails.getWorkOrderNumber() == null) {
                final SewerageWorkOrderNumberGenerator workOrderNumberGenerator = beanResolver
                        .getAutoNumberServiceFor(SewerageWorkOrderNumberGenerator.class);
                if (workOrderNumberGenerator != null) {
                    sewerageApplicationDetails.setWorkOrderNumber(workOrderNumberGenerator.generateSewerageWorkOrderNumber());
                    sewerageApplicationDetails.setWorkOrderDate(new Date());
                }
            }
            if (sewerageApplicationDetails.getConnection().getShscNumber() == null) {
                final SewerageSHSCNumberGenerator shscNumberGenerator = beanResolver
                        .getAutoNumberServiceFor(SewerageSHSCNumberGenerator.class);
                if (shscNumberGenerator != null)
                    sewerageApplicationDetails.getConnection()
                            .setShscNumber(shscNumberGenerator.generateNextSHSCNumber(sewerageApplicationDetails));
            }
        }

        if (sewerageApplicationDetails.getStatus().getCode() != null
                && sewerageApplicationDetails.getStatus().getCode().equalsIgnoreCase(APPLICATION_STATUS_INITIALAPPROVED)
                || sewerageApplicationDetails.getStatus().getCode().equalsIgnoreCase(APPLICATION_STATUS_DEEAPPROVED)
                || sewerageApplicationDetails.getStatus().getCode().equalsIgnoreCase(APPLICATION_STATUS_INSPECTIONFEEPAID)
                || sewerageApplicationDetails.getStatus().getCode().equalsIgnoreCase(APPLICATION_STATUS_CREATED)) {
            if (sewerageApplicationDetails != null && sewerageApplicationDetails.getCurrentDemand() == null) {
                final EgDemand demand = sewerageDemandService.createDemandOnNewConnection(
                        sewerageApplicationDetails.getConnectionFees(), sewerageApplicationDetails);
                if (demand != null) {
                    final SewerageDemandConnection sdc = new SewerageDemandConnection();
                    sdc.setDemand(demand);
                    sdc.setApplicationDetails(sewerageApplicationDetails);
                    sewerageApplicationDetails.addDemandConnections(sdc);
                }
            } else if (sewerageApplicationDetails.getApplicationType().getCode().equalsIgnoreCase(CHANGEINCLOSETS)) {
                final SewerageApplicationDetails oldSewerageAppDtls = findByShscNumberAndIsActive(
                        sewerageApplicationDetails.getConnection().getShscNumber());
                if (!workFlowAction.equals(WFLOW_ACTION_STEP_REJECT))
                    if (sewerageApplicationDetails.getStatus().getCode()
                            .equalsIgnoreCase(APPLICATION_STATUS_INITIALAPPROVED))
                        sewerageDemandService.updateDemandOnChangeInClosets(oldSewerageAppDtls,
                                sewerageApplicationDetails.getConnectionFees(),
                                sewerageApplicationDetails.getCurrentDemand(), TRUE);
                    else
                        sewerageDemandService.updateDemandOnChangeInClosets(oldSewerageAppDtls,
                                sewerageApplicationDetails.getConnectionFees(),
                                sewerageApplicationDetails.getCurrentDemand(), FALSE);
            } else
                sewerageDemandService.updateDemand(sewerageApplicationDetails.getConnectionFees(),
                        sewerageApplicationDetails.getCurrentDemand());
        }
        if (APPLICATION_STATUS_DEEAPPROVED.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode())
                && WF_STATE_DEPUTY_EXE_APPROVED.equalsIgnoreCase(sewerageApplicationDetails.getState().getValue())
                && additionalRule != null && !CHANGEINCLOSETS_NOCOLLECTION.equalsIgnoreCase(additionalRule)
                && sewerageApplicationDetails.getEstimationNumber() == null) {
            final SewerageEstimationNumberGenerator estimationNumberGenerator = beanResolver
                    .getAutoNumberServiceFor(SewerageEstimationNumberGenerator.class);
            if (estimationNumberGenerator != null) {
                sewerageApplicationDetails.setEstimationNumber(estimationNumberGenerator.generateEstimationNumber());
                sewerageApplicationDetails.setEstimationDate(new Date());
            }
        }
        if (additionalRule != null && additionalRule.equalsIgnoreCase(CHANGEINCLOSETS_NOCOLLECTION))
            applicationStatusChange(sewerageApplicationDetails, workFlowAction, additionalRule, request, session);
        else
            applicationStatusChange(sewerageApplicationDetails, workFlowAction, mode, request, session);

        if (sewerageApplicationDetails.getStatus().getCode()
                .equalsIgnoreCase(APPLICATION_STATUS_ESTIMATENOTICEGEN)) {
            final SewerageNotice sewerageNotice = sewerageNoticeService.generateReportForEstimation(sewerageApplicationDetails);
            if (sewerageNotice != null)
                sewerageApplicationDetails.addNotice(sewerageNotice);
        } else if (APPLICATION_STATUS_WOGENERATED
                .equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode())
                || APPLICATION_STATUS_FINALAPPROVED.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode())) {
            final SewerageNotice existingSewerageNotice = sewerageNoticeService
                    .findByNoticeNoAndNoticeType(sewerageApplicationDetails.getWorkOrderNumber(), NOTICE_TYPE_WORK_ORDER_NOTICE);
            if (existingSewerageNotice == null) {
                final SewerageNotice sewerageNotice = sewerageNoticeService.generateReportForWorkOrder(sewerageApplicationDetails);
                if (sewerageNotice != null)
                    sewerageApplicationDetails.addNotice(sewerageNotice);
            }
        }

        final SewerageApplicationDetails updatedSewerageApplicationDetails = sewerageApplicationDetailsRepository
                .save(sewerageApplicationDetails);

        if (LOG.isDebugEnabled())
            LOG.debug(APPLICATION_WORKFLOW_INITIALIZATION_DONE);

        applicationWorkflowCustomDefaultImpl.createCommonWorkflowTransition(updatedSewerageApplicationDetails);

        if (generateDemandVoucher
                && NEWSEWERAGECONNECTION.equalsIgnoreCase(sewerageApplicationDetails.getApplicationType().getCode())
                && sewerageDemandVoucherService.getDemandVoucherEnable()) {
            sewerageDemandVoucherService.createDemandVoucher(sewerageApplicationDetails);
        }

        if (sewerageApplicationDetails.getApplicationType().getCode().equalsIgnoreCase(CHANGEINCLOSETS)
                && sewerageApplicationDetails.getParent() != null)
            updateIndexes(sewerageApplicationDetails.getParent());
        updatePortalMessage(updatedSewerageApplicationDetails);
        updateIndexes(sewerageApplicationDetails);

        if (APPLICATION_STATUS_COLLECTINSPECTIONFEE.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode())
                || APPLICATION_STATUS_DEEAPPROVED.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode())
                || APPLICATION_STATUS_CREATED.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode())) {
            sewerageApplicationDetails.setApprovalComent(approverComments);
            sewerageConnectionSmsAndEmailService.sendSmsAndEmail(sewerageApplicationDetails, request);
        } else if (APPLICATION_STATUS_FINALAPPROVED.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode())
                || APPLICATION_STATUS_REJECTED.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode())) {
            sewerageApplicationDetails.setApprovalComent(approverComments);
            sewerageConnectionSmsAndEmailService.sendSmsAndEmail(sewerageApplicationDetails, request);
        }
        publishEventUpdateForWardSecretary(sewerageApplicationDetails, workFlowAction);
        return updatedSewerageApplicationDetails;
    }

    private void publishEventUpdateForWardSecretary(final SewerageApplicationDetails sewerageApplicationDetails,
            String workFlowAction) {
        if (StringUtils.isNotBlank(sewerageApplicationDetails.getSource())
                && Source.WARDSECRETARY.toString().equalsIgnoreCase(sewerageApplicationDetails.getSource().toString())
                && (WFLOW_ACTION_STEP_CANCEL.equalsIgnoreCase(workFlowAction)
                || APPROVEWORKFLOWACTION.equalsIgnoreCase(workFlowAction))) {
            if (NEWSEWERAGECONNECTION.equalsIgnoreCase(sewerageApplicationDetails.getApplicationType().getCode())
                    || CLOSESEWERAGECONNECTION.equalsIgnoreCase(sewerageApplicationDetails.getApplicationType().getCode())
                    || CHANGEINCLOSETS.equalsIgnoreCase(sewerageApplicationDetails.getApplicationType().getCode())) {
                publishEventForWardSecretary(null, sewerageApplicationDetails.getApplicationNumber(),
                        sewerageApplicationDetails.getApplicationType().getName(), true,
                        WARDSECRETARY_EVENTPUBLISH_MODE_UPDATE, workFlowAction,
                        sewerageApplicationDetails.getConnectionDetail().getPropertyIdentifier());
            }
        }
    }
    // Pending : commented out code as statuses are changed. Need to correct

    public void applicationStatusChange(final SewerageApplicationDetails sewerageApplicationDetails,
                                        final String workFlowAction, final String mode, final HttpServletRequest request, final HttpSession session) {

        if (null != sewerageApplicationDetails && null != sewerageApplicationDetails.getStatus()
                && null != sewerageApplicationDetails.getStatus().getCode())
            if (WFLOW_ACTION_STEP_REJECT.equalsIgnoreCase(workFlowAction))
                sewerageApplicationDetails
                        .setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_REJECTED, MODULETYPE));
            else if (WFLOW_ACTION_STEP_CANCEL.equalsIgnoreCase(workFlowAction)) {
                // In case of change in closet, mark only the application as cancelled on cancel application from creator's inbox
                if (sewerageApplicationDetails.getApplicationType().getCode().equalsIgnoreCase(CHANGEINCLOSETS)) {
                    sewerageApplicationDetails
                            .setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_CANCELLED, MODULETYPE));
                    sewerageApplicationDetails.getConnection().setStatus(SewerageConnectionStatus.ACTIVE);
                } else {   // In case of new connection, connection is made inactive on cancel application from creator's inbox
                    sewerageApplicationDetails.getConnection().setStatus(SewerageConnectionStatus.INACTIVE);
                    sewerageApplicationDetails
                            .setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_CANCELLED, MODULETYPE));
                }
                if (sewerageApplicationDetails.getRejectionNumber() == null) {
                    final SewerageRejectionNoticeNumberGenerator rejectionNumberGenerator = beanResolver
                            .getAutoNumberServiceFor(SewerageRejectionNoticeNumberGenerator.class);
                    if (rejectionNumberGenerator != null) {
						sewerageApplicationDetails.setRejectionNumber(rejectionNumberGenerator
								.generateRejectionNoticeNumber());
                        sewerageApplicationDetails.setRejectionDate(new Date());
                    }
                }

                final SewerageNotice sewerageNotice = sewerageNoticeService.generateReportForRejection(
                        sewerageApplicationDetails, session, request);
                if (sewerageNotice != null)
                    sewerageApplicationDetails.addNotice(sewerageNotice);
            } else if (NEW_STATE.equalsIgnoreCase(sewerageApplicationDetails.getState().getValue())
                    && (APPLICATION_STATUS_CSCCREATED
                    .equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode())
                    || APPLICATION_STATUS_WARDSECRETARYCREATED
                    .equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode()))
                    || APPLICATION_STATUS_ANONYMOUSCREATED
                    .equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode())) {
                if (sewerageTaxUtils.isInspectionFeeCollectionRequired()) {
                    sewerageApplicationDetails.setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(
                            APPLICATION_STATUS_COLLECTINSPECTIONFEE, MODULETYPE));

                } else {
                    sewerageApplicationDetails.setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(
                            APPLICATION_STATUS_CREATED, MODULETYPE));
                }
            } else if (APPLICATION_STATUS_FEECOLLECTIONPENDING.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode())) {
                sewerageApplicationDetails.setStatus(
                        sewerageTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_INSPECTIONFEEPAID, MODULETYPE));
            } else if ((sewerageApplicationDetails.getStatus().getCode().equals(APPLICATION_STATUS_CREATED))
                    || (sewerageApplicationDetails.getStatus().getCode().equals(APPLICATION_STATUS_INSPECTIONFEEPAID) && !sewerageApplicationDetails.getState().getValue().equalsIgnoreCase(NEW_STATE)))
                sewerageApplicationDetails
                        .setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_INITIALAPPROVED, MODULETYPE));
            else if (sewerageApplicationDetails.getStatus().getCode().equals(APPLICATION_STATUS_COLLECTINSPECTIONFEE))
                sewerageApplicationDetails.setStatus(
                        sewerageTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_INSPECTIONFEEPAID, MODULETYPE));
            else if (sewerageApplicationDetails.getStatus().getCode().equals(APPLICATION_STATUS_INITIALAPPROVED))
                sewerageApplicationDetails
                        .setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_DEEAPPROVED, MODULETYPE));
            else if (sewerageApplicationDetails.getStatus().getCode().equals(APPLICATION_STATUS_DEEAPPROVED)) {
                if (mode != null && mode.equalsIgnoreCase(CHANGEINCLOSETS_NOCOLLECTION))
                    sewerageApplicationDetails.setStatus(
                            sewerageTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_FINALAPPROVED, MODULETYPE));
                else
                    sewerageApplicationDetails.setStatus(
                            sewerageTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_ESTIMATENOTICEGEN, MODULETYPE));
            } else if (sewerageApplicationDetails.getStatus().getCode().equals(APPLICATION_STATUS_ESTIMATENOTICEGEN))
                sewerageApplicationDetails
                        .setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_FEEPAID, MODULETYPE));
            else if (sewerageApplicationDetails.getStatus().getCode().equals(APPLICATION_STATUS_FEEPAID)
                    && (sewerageApplicationDetails.getState().getValue().equalsIgnoreCase(WF_STATE_PAYMENTDONE) ||
                    sewerageApplicationDetails.getState().getValue().equalsIgnoreCase(WF_STATE_EE_APPROVED))
                    && workFlowAction.equals(APPROVEWORKFLOWACTION)
                    )
                sewerageApplicationDetails
                        .setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_FINALAPPROVED, MODULETYPE));
            else if (sewerageApplicationDetails.getStatus().getCode().equals(APPLICATION_STATUS_FINALAPPROVED))
                sewerageApplicationDetails
                        .setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_WOGENERATED, MODULETYPE));
            else if (sewerageApplicationDetails.getStatus().getCode().equals(APPLICATION_STATUS_WOGENERATED)) {
                sewerageApplicationDetails
                        .setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_SANCTIONED, MODULETYPE));
                sewerageApplicationDetails.setActive(true);
                // Make Connection status active on connection execution
                if (sewerageApplicationDetails.getConnection().getStatus().equals(SewerageConnectionStatus.INPROGRESS))
                    sewerageApplicationDetails.getConnection().setStatus(SewerageConnectionStatus.ACTIVE);
            } else if (sewerageApplicationDetails.getStatus().getCode().equals(APPLICATION_STATUS_REJECTED))
                if (sewerageTaxUtils.isInspectionFeeCollectionRequired()) {
                    if (sewerageApplicationDetails.getCurrentDemand().getAmtCollected()
                            .compareTo(BigDecimal.ZERO) == 0)
                        sewerageApplicationDetails.setStatus(sewerageTaxUtils
                                .getStatusByCodeAndModuleType(APPLICATION_STATUS_COLLECTINSPECTIONFEE, MODULETYPE));
                    else
                        sewerageApplicationDetails.setStatus(
                                sewerageTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_INSPECTIONFEEPAID, MODULETYPE));
                } else
                    sewerageApplicationDetails
                            .setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_CREATED, MODULETYPE));
    }

    @Transactional
    public SewerageApplicationDetails updateCloseSewerageApplicationDetails(
            final SewerageApplicationDetails sewerageApplicationDetails,
            final ReportOutput reportOutput, final HttpServletRequest request, final HttpSession session) {
        String workFlowAction = sewerageApplicationDetails.getWorkflowContainer().getWorkFlowAction();
        // Generate closure notice number and date
        if (CLOSESEWERAGECONNECTION.equalsIgnoreCase(sewerageApplicationDetails.getApplicationType().getCode())
                && APPROVEWORKFLOWACTION.equalsIgnoreCase(workFlowAction)) {
            final SewerageCloseConnectionNoticeNumberGenerator closeConnectionNoticeNumberGenerator = beanResolver
                    .getAutoNumberServiceFor(SewerageCloseConnectionNoticeNumberGenerator.class);
            if (closeConnectionNoticeNumberGenerator != null && sewerageApplicationDetails.getClosureNoticeNumber() == null) {
                sewerageApplicationDetails
                        .setClosureNoticeNumber(closeConnectionNoticeNumberGenerator.generateCloserNoticeNumber());
                sewerageApplicationDetails.setClosureNoticeDate(new Date());
            }
        }
        // Application status change
        updateStatusOnCloseConnection(sewerageApplicationDetails, workFlowAction);

        // Generate the sewerage notices based on type of notice and save into DB.
        if (APPLICATION_STATUS_CLOSERSANCTIONED.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode())
                && APPROVEWORKFLOWACTION.equalsIgnoreCase(workFlowAction)) {
            final SewerageNotice sewerageNotice = sewerageNoticeService
                    .generateReportForCloseConnection(sewerageApplicationDetails, session);
            if (sewerageNotice != null)
                sewerageApplicationDetails.addNotice(sewerageNotice);
        }
        
        if (WFLOW_ACTION_STEP_CANCEL.equalsIgnoreCase(workFlowAction)) {
            if (sewerageApplicationDetails.getRejectionNumber() == null) {
                SewerageRejectionNoticeNumberGenerator rejectionNumberGenerator = beanResolver
                        .getAutoNumberServiceFor(SewerageRejectionNoticeNumberGenerator.class);
                if (rejectionNumberGenerator != null) {
                    sewerageApplicationDetails.setRejectionNumber(rejectionNumberGenerator
                            .generateRejectionNoticeNumber());
                    sewerageApplicationDetails.setRejectionDate(new Date());
                }

                SewerageNotice sewerageNotice = sewerageNoticeService
                        .generateReportForRejection(sewerageApplicationDetails, session, request);
                if (sewerageNotice != null)
                    sewerageApplicationDetails.addNotice(sewerageNotice);
            }
        }

        final SewerageApplicationDetails updatedSewerageApplicationDetails = sewerageApplicationDetailsRepository
                .save(sewerageApplicationDetails);

        if (LOG.isDebugEnabled())
            LOG.debug(APPLICATION_WORKFLOW_INITIALIZATION_DONE);

        applicationWorkflowCustomDefaultImpl.createCloseConnectionWorkflowTransition(updatedSewerageApplicationDetails);

        if (sewerageApplicationDetails.getParent() != null)
            updateIndexes(sewerageApplicationDetails.getParent());
        updateIndexes(sewerageApplicationDetails);
        // support sms and email for close connection
        if (APPLICATION_STATUS_CLOSERSANCTIONED.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode())
                && APPROVEWORKFLOWACTION.equalsIgnoreCase(workFlowAction) ||
                APPLICATION_STATUS_CREATED.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode()) ||
                APPLICATION_STATUS_REJECTED.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode())) {
            sewerageApplicationDetails.setApprovalComent(sewerageApplicationDetails.getWorkflowContainer().getApproverComments());
            sewerageConnectionSmsAndEmailService.sendSmsAndEmail(sewerageApplicationDetails, request);
        }
        publishEventUpdateForWardSecretary(sewerageApplicationDetails,workFlowAction);
        return updatedSewerageApplicationDetails;
    }

    private void updateStatusOnCloseConnection(SewerageApplicationDetails sewerageApplicationDetails, String workFlowAction) {
        if (sewerageApplicationDetails != null && sewerageApplicationDetails.getStatus() != null
                && sewerageApplicationDetails.getStatus().getCode() != null)
            if (WFLOW_ACTION_STEP_REJECT.equalsIgnoreCase(workFlowAction))
                sewerageApplicationDetails
                        .setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_REJECTED, MODULETYPE));
            else if (WFLOW_ACTION_STEP_CANCEL.equalsIgnoreCase(workFlowAction)) {
                sewerageApplicationDetails.getConnection().setStatus(SewerageConnectionStatus.ACTIVE);
                sewerageApplicationDetails
                        .setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_CANCELLED, MODULETYPE));
			} else if (APPLICATION_STATUS_ANONYMOUSCREATED.equals(sewerageApplicationDetails.getStatus().getCode()))
				sewerageApplicationDetails.setStatus(
						sewerageTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_CREATED, MODULETYPE));
			else if (APPLICATION_STATUS_CREATED.equals(sewerageApplicationDetails.getStatus().getCode()))
                sewerageApplicationDetails
                        .setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_INITIALAPPROVED, MODULETYPE));
            else if (APPLICATION_STATUS_INITIALAPPROVED.equals(sewerageApplicationDetails.getStatus().getCode()))
                sewerageApplicationDetails
                        .setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_DEEAPPROVED, MODULETYPE));
            else if (APPLICATION_STATUS_DEEAPPROVED.equals(sewerageApplicationDetails.getStatus().getCode())
                    && WFLOW_ACTION_STEP_FORWARD.equals(workFlowAction))
                sewerageApplicationDetails
                        .setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_EEAPPROVED, MODULETYPE));
            else if ((APPLICATION_STATUS_DEEAPPROVED.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode())
                    || WF_STATE_EE_APPROVED.equalsIgnoreCase(sewerageApplicationDetails.getState().getValue()))
                    && APPROVEWORKFLOWACTION.equalsIgnoreCase(workFlowAction)) {
                // Make Connection status closed on EE approval
                sewerageApplicationDetails.getConnection().setStatus(SewerageConnectionStatus.CLOSED);
                sewerageApplicationDetails.setActive(true);
                sewerageApplicationDetails.setStatus(
                        sewerageTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_CLOSERSANCTIONED, MODULETYPE));
            } else if (APPLICATION_STATUS_REJECTED.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode()))
                sewerageApplicationDetails
                        .setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_CREATED, MODULETYPE));
    }

    /*
     * updating application State and State History table info
     */
    public List<HashMap<String, Object>> populateHistory(final SewerageApplicationDetails sewerageApplicationDetails) {
        final List<HashMap<String, Object>> historyTable = new ArrayList<>();
        final State<Position> state = sewerageApplicationDetails.getState();
        if (state != null) {
            final HashMap<String, Object> stateMap = new HashMap<>();
            stateMap.put("date", state.getLastModifiedDate());
            stateMap.put("comments", state.getComments());
            stateMap.put("updatedBy", state.getSenderName());
            stateMap.put("status", state.getValue());
            setUserAndDepartment(state.getOwnerPosition(), state.getOwnerUser(), stateMap);
            historyTable.add(stateMap);
            state.getHistory().stream().sorted(Comparator.comparing(StateHistory<Position>::getLastModifiedDate).reversed())
                    .forEach(sh -> historyTable.add(updateHistoryTableInfo(sh)));
        }
        return historyTable;
    }

    private HashMap<String, Object> updateHistoryTableInfo(StateHistory<Position> stateHistory) {
        Position position = stateHistory.getOwnerPosition();
        final HashMap<String, Object> stateHistoryMap = new HashMap<>();
        stateHistoryMap.put("date", stateHistory.getLastModifiedDate());
        stateHistoryMap.put("comments", defaultString(stateHistory.getComments()));
        stateHistoryMap.put("updatedBy", stateHistory.getSenderName());
        stateHistoryMap.put("status", stateHistory.getValue());
        setUserAndDepartment(position, stateHistory.getOwnerUser(), stateHistoryMap);
        return stateHistoryMap;
    }

    private void setUserAndDepartment(final Position ownerPosition, User owner, final HashMap<String, Object> applicationHistoryMap) {
        if (ownerPosition != null) {
            User user = eisCommonService.getUserForPosition(ownerPosition.getId(), new Date());
            applicationHistoryMap
                    .put("user", user == null ? NA : user.getUsername() + "::" + user.getName());
            applicationHistoryMap.put(DEPARTMENT, ownerPosition.getDeptDesig().getDepartment().getName());
        } else {
            applicationHistoryMap
                    .put("user", owner.getUsername() == null ? NA : owner.getUsername() + "::" + owner.getName());
            applicationHistoryMap.put(DEPARTMENT, NA);
        }
    }

    public void updateStateTransition(final SewerageApplicationDetails sewerageApplicationDetails,
                                      final Long approvalPosition, final String approvalComent, final String additionalRule,
                                      final String workFlowAction) {
        if (approvalPosition != null && additionalRule != null
                && org.apache.commons.lang.StringUtils.isNotEmpty(workFlowAction))
            applicationWorkflowCustomDefaultImpl.createCommonWorkflowTransition(sewerageApplicationDetails);
        // update index on collection
    }

    public SewerageApplicationDetails isApplicationInProgress(final String shscNumber) {
        return sewerageApplicationDetailsRepository.findByConnectionShscNumberAndIsActiveFalseAndStatusCodeNotIn(shscNumber, Arrays.asList("CANCELLED", "SANCTIONED"));
    }

    public BigDecimal getPendingTaxAmount(final SewerageApplicationDetails sewerageApplicationDetails) {
        return sewerageDemandService.checkForPendingTaxAmountToCollect(sewerageApplicationDetails.getCurrentDemand());
    }

    public String isConnectionExistsForProperty(final String propertyId) {
        return checkConnectionPresentForProperty(propertyId);
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public List<SewerageApplicationDetails> findActiveSewerageApplnsByCurrentInstallmentAndNumberOfResultToFetch(
            Installment installment,
            int noOfResultToFetch) {

        final Criteria sewerageCriteria = entityManager.unwrap(Session.class)
                .createCriteria(SewerageApplicationDetails.class, "sewerageDetails")
                .createAlias("sewerageDetails.demandConnections", "demandConnections")
                .createAlias("demandConnections.demand", "demand");
        sewerageCriteria.add(Restrictions.eq("demand.isHistory", DEMANDISHISTORY));
        sewerageCriteria.add(Restrictions.eq("sewerageDetails.isActive", true));
        if (installment != null)
            sewerageCriteria.add(Restrictions.eq("demand.egInstallmentMaster.id", installment.getId()));
        sewerageCriteria.setMaxResults(noOfResultToFetch);
        sewerageCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return sewerageCriteria.list();
    }

    @Transactional
    public SewerageApplicationDetails updateLegacySewerageConnection(
            final SewerageApplicationDetails sewerageApplicationDetails, final HttpServletRequest request) {
        if (sewerageTaxUtils.isDonationChargeCollectionRequiredForLegacy()) {
            // Capturing pending Donation charge for legacy records
            SewerageDemandDetail sewerageDemandDetail = new SewerageDemandDetail();
            BigDecimal donationaAmtCollected = new BigDecimal(request.getParameter("amountCollected"));
            sewerageDemandDetail.setActualCollection(donationaAmtCollected);
            for (final SewerageConnectionFee fees : sewerageApplicationDetails.getConnectionFees()) {
                if (FEES_DONATIONCHARGE_CODE.equals(fees.getFeesDetail().getCode())) {
                    sewerageDemandDetail.setActualAmount(BigDecimal.valueOf(fees.getAmount()));
                }
            }
            sewerageDemandDetail.setInstallmentId(sewerageDemandService.getCurrentInstallment().getId());
            sewerageDemandDetail.setReasonMaster(FEES_DONATIONCHARGE_CODE);
            sewerageApplicationDetails.getDemandDetailBeanList().add(sewerageDemandDetail);
        }

        sewerageDemandService.updateLegacyDemand(sewerageApplicationDetails.getDemandDetailBeanList(),
                sewerageApplicationDetails.getCurrentDemand());
        sewerageApplicationDetailsRepository.saveAndFlush(sewerageApplicationDetails);
        updateIndexes(sewerageApplicationDetails);
        return sewerageApplicationDetails;
    }

    public ReportOutput getReportParamsForSewerageAcknowledgement(final SewerageApplicationDetails sewerageApplicationDetails,
                                                                  final String municipalityName, final String cityName) {
        final Map<String, Object> reportParams = new HashMap<>();
        String ownerName = "";
        reportParams.put(MUNICIPALITY, municipalityName);
        reportParams.put(CITYNAME, cityName);
        final AssessmentDetails assessmentDetails = sewerageTaxUtils.getAssessmentDetailsForFlag(
                sewerageApplicationDetails.getConnectionDetail().getPropertyIdentifier(),
                PropertyExternalService.FLAG_FULL_DETAILS);
        if (assessmentDetails != null) {
            ownerName = assessmentDetails.getOwnerNames().iterator().next().getOwnerName();
            reportParams.put(ZONE_NAME, defaultString(assessmentDetails.getBoundaryDetails().getZoneName()));
            reportParams.put(ELECTION_WARD, defaultString(assessmentDetails.getBoundaryDetails().getWardName()));
            reportParams.put(ADDRESS, defaultString(assessmentDetails.getPropertyAddress()));
            reportParams.put(APPLICANT_NAME, defaultString(ownerName));
        }
        reportParams.put(ACKNOWLEDGEMENT_NO, sewerageApplicationDetails.getApplicationNumber());
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        reportParams.put(CURRENT_DATE, formatter.format(new Date()));

        reportParams.put(DUE_DATE, formatter.format(calculateDueDate(sewerageApplicationDetails)));
        reportParams.put(PARTY_S_COPY, PARTY_S_COPY);
        reportParams.put(OFFICE_S_COPY, OFFICE_S_COPY);
        reportParams.put(APPLICATION_CENTRE, stmsMessageSource.getMessage("msg.application.centre",
                new String[]{}, Locale.getDefault()));
        reportParams.put(APP_TYPE, WordUtils.capitalize(sewerageApplicationDetails.getApplicationType().getName()));

        final ReportRequest reportInput = new ReportRequest("sewerageAcknowledgementReceipt", sewerageApplicationDetails,
                reportParams);

        return reportService.createReport(reportInput);

    }

    private Date calculateDueDate(SewerageApplicationDetails sewerageApplicationDetails) {
        return addDays(new Date(), sewerageTaxUtils.getSlaAppConfigValues(sewerageApplicationDetails.getApplicationType()));
    }

    /**
     * Method to push data for citizen portal inbox
     */

    @Transactional
    public void pushPortalMessage(final SewerageApplicationDetails sewerageApplicationDetails) {
        final SewerageConnection sewerageConnection = sewerageApplicationDetails.getConnection();
        final Module module = sewerageTaxUtils.getModule();

        final PortalInboxBuilder portalInboxBuilder = new PortalInboxBuilder(module,
                sewerageApplicationDetails.getState().getNatureOfTask() + " : " + module.getDisplayName(),
                sewerageApplicationDetails.getApplicationNumber(), sewerageConnection.getShscNumber(), sewerageConnection.getId(),
                sewerageApplicationDetails.getCloseConnectionReason(), "Sucess",
                String.format(STMS_APPLICATION_UPDATE, sewerageApplicationDetails.getApplicationNumber()),
                isResolved(sewerageApplicationDetails), sewerageApplicationDetails.getStatus().getDescription(),
                calculateDueDate(sewerageApplicationDetails), sewerageApplicationDetails.getState(),
                Arrays.asList(securityUtils.getCurrentUser()));
        final PortalInbox portalInbox = portalInboxBuilder.build();
        portalInboxService.pushInboxMessage(portalInbox);
    }

    private boolean isResolved(final SewerageApplicationDetails sewerageApplicationDetails) {
        return "END".equalsIgnoreCase(sewerageApplicationDetails.getState().getValue())
                || "CLOSED".equalsIgnoreCase(sewerageApplicationDetails.getState().getValue());
    }

    public PortalInbox getPortalInbox(final String applicationNumber) {
        return portalInboxService.getPortalInboxByApplicationNo(applicationNumber, sewerageTaxUtils.getModule().getId());
    }

    /**
     * Method to update data for citizen portal inbox
     */
    @Transactional
    public void updatePortalMessage(final SewerageApplicationDetails sewerageApplicationDetails) {
        final SewerageConnection sewerageConnection = sewerageApplicationDetails.getConnection();
        String url;
        String status;
        if (sewerageApplicationDetails.getStatus().getCode()
                .equalsIgnoreCase(APPLICATION_STATUS_FEECOLLECTIONPENDING)) {
            url = STMS_APPLICATION_UPDATE;
            status = "Inspection fee paid";
        } else {
            url = STMS_APPLICATION_VIEW;
            status = sewerageApplicationDetails.getStatus().getDescription();
        }
        portalInboxService.updateInboxMessage(sewerageApplicationDetails.getApplicationNumber(), sewerageTaxUtils.getModule().getId(),
                status,
                isResolved(sewerageApplicationDetails), calculateDueDate(sewerageApplicationDetails),
                sewerageApplicationDetails.getState(),
                null,
                sewerageConnection.getShscNumber(),
                String.format(url, sewerageApplicationDetails.getApplicationNumber(),
                        sewerageApplicationDetails.getConnectionDetail().getPropertyIdentifier()));
    }

    public SewerageApplicationDetails getApplicationDetailByPropertyIdAndStatusExcluded(final String propertyId,
                                                                                        List<String> connectionStatus) {
        return sewerageApplicationDetailsRepository
                .findFirstByConnectionDetailPropertyIdentifierAndStatusCodeNotInOrderByLastModifiedDateDesc(propertyId,
                        connectionStatus);
    }
    
    public boolean isValidApprover(SewerageApplicationDetails sewerageApplicationDetails) {
        WorkFlowMatrix workflowMatrix = null;
        WorkflowContainer workflowContainer = sewerageApplicationDetails.getWorkflowContainer();
        String additionalRule = workflowContainer.getAdditionalRule();
        Long approvalPosition = workflowContainer.getApproverPositionId();
        Position nextStateOwner = positionMasterService.getPositionById(approvalPosition);

        String pendingActions = null;
        if ((!sewerageApplicationDetails.getApplicationType().getCode().equalsIgnoreCase(CLOSESEWERAGECONNECTION)) &&
                sewerageApplicationDetails.getState() != null
                && ("new".equalsIgnoreCase(sewerageApplicationDetails.getState().getValue())
                        && APPLICATION_STATUS_COLLECTINSPECTIONFEE
                                .equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode()))
                && (sewerageWorkflowService.isCscOperator(sewerageApplicationDetails.getCreatedBy())
                        || ANONYMOUS_USER.equalsIgnoreCase(sewerageApplicationDetails.getCreatedBy().getUsername())
                        || sewerageTaxUtils.isCitizenPortalUser(sewerageApplicationDetails.getCreatedBy()))
                && sewerageTaxUtils.isInspectionFeeCollectionRequired())
            pendingActions = WF_INSPECTIONFEE_COLLECTION;
        workflowMatrix = sewerageApplicationWorkflowService.getWfMatrix(sewerageApplicationDetails.getStateType(), null,
                null, additionalRule, sewerageApplicationDetails.getCurrentState().getValue(), pendingActions);
        return (!eisCommonService.isValidAppover(workflowMatrix, nextStateOwner)) ? Boolean.FALSE : Boolean.TRUE;
    }

    public Boolean isApplicationOwner(User currentUser, StateAware state) {
        return positionMasterService.getPositionsForEmployee(currentUser.getId())
                .contains(state.getCurrentState().getOwnerPosition());
    }

    @Transactional
    public void persistAndPublishEventForWardSecretary(SewerageApplicationDetails sewerageApplicationDetails,
            final MultipartFile[] files, final HttpServletRequest request, String workFlowAction, String mode) {
        try {
            publishEventForWardSecretary(request, sewerageApplicationDetails.getApplicationNumber(),
                    sewerageApplicationDetails.getApplicationType().getName(), true, mode, workFlowAction,
                    sewerageApplicationDetails.getConnectionDetail().getPropertyIdentifier());
        } catch (Exception e) {
            publishEventForWardSecretary(request, sewerageApplicationDetails.getApplicationNumber(),
                    sewerageApplicationDetails.getApplicationType().getName(), false, mode, workFlowAction,
                    sewerageApplicationDetails.getConnectionDetail().getPropertyIdentifier());
        }
    }

    public void publishEventForWardSecretary(HttpServletRequest request, String applicationNo, String applicationType,
            boolean isSuccess, String mode, String workFlowAction, String propertyId) {
        if (isSuccess) {
            if (WARDSECRETARY_EVENTPUBLISH_MODE_CREATE.equalsIgnoreCase(mode)) {
                thirdPartyApplicationEventPublisher.publishEvent(ApplicationDetails.builder()
                        .withApplicationNumber((CLOSESEWERAGECONNECTION.equalsIgnoreCase(applicationType)
                                || CHANGEINCLOSETS.equalsIgnoreCase(applicationType))
                                        ? applicationNo.concat("~").concat(getFormattedDate(new Date(), "dd-MM-yyyy"))
                                        : applicationNo)
                        .withViewLink(format(SewerageTaxConstants.VIEW_LINK,
                                WebUtils.extractRequestDomainURL(request, false), applicationNo, propertyId))
                        .withTransactionStatus(TransactionStatus.SUCCESS)
                        .withApplicationStatus(ApplicationStatus.INPROGRESS)
                        .withRemark(applicationType.concat(" created"))
                        .withTransactionId(request.getParameter(WARDSECRETARY_TRANSACTIONID_CODE)).build());
            } else if (WARDSECRETARY_EVENTPUBLISH_MODE_UPDATE.equalsIgnoreCase(mode)) {
                ApplicationDetails applicationDetails = ApplicationDetails.builder()
                        .withApplicationNumber(applicationNo)
                        .withApplicationStatus(APPROVEWORKFLOWACTION.equalsIgnoreCase(workFlowAction)
                                ? ApplicationStatus.APPROVED : ApplicationStatus.REJECTED)
                        .withRemark(APPROVEWORKFLOWACTION.equalsIgnoreCase(workFlowAction)
                                ? applicationType.concat(" approved") : applicationType.concat(" cancelled"))
                        .withDateOfCompletion(new Date()).build();
                thirdPartyApplicationEventPublisher.publishEvent(applicationDetails);
            }
        } else {
            thirdPartyApplicationEventPublisher
                    .publishEvent(ApplicationDetails.builder().withTransactionStatus(TransactionStatus.FAILED)
                            .withRemark(applicationType.concat(" creation failed"))
                            .withTransactionId(request.getParameter(WARDSECRETARY_TRANSACTIONID_CODE)).build());
        }
    }
}
