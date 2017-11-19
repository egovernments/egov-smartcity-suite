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

import org.apache.commons.lang.WordUtils;
import org.egov.commons.Installment;
import org.egov.commons.entity.Source;
import org.egov.demand.model.EgDemand;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.elasticsearch.entity.ApplicationIndex;
import org.egov.infra.elasticsearch.entity.enums.ApprovalStatus;
import org.egov.infra.elasticsearch.entity.enums.ClosureStatus;
import org.egov.infra.elasticsearch.service.ApplicationIndexService;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.pims.commons.Position;
import org.egov.portal.entity.PortalInbox;
import org.egov.portal.entity.PortalInboxBuilder;
import org.egov.portal.service.PortalInboxService;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.OwnerName;
import org.egov.ptis.domain.service.property.PropertyExternalService;
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static org.egov.stms.utils.constants.SewerageTaxConstants.*;

@Service
@Transactional(readOnly = true)
public class SewerageApplicationDetailsService {

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
    private static final String STMS_APPLICATION_VIEW = "/stms/existing/sewerage/view/%s/%s";
    private static final String STMS_APPLICATION_UPDATE = "/stms/transactions/citizenupdate/%s";
    private static final String APPLICATION_WORKFLOW_INITIALIZATION_DONE = "applicationWorkflowCustomDefaultImpl initialization is done";
    private static final String DEPARTMENT = "department";
    protected SewerageApplicationDetailsRepository sewerageApplicationDetailsRepository;
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
    private AppConfigValueService appConfigValuesService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private SewerageWorkflowService sewerageWorkflowService;

    @Autowired
    private PortalInboxService portalInboxService;

    @Autowired
    private ModuleService moduleDao;

    @Autowired
    public SewerageApplicationDetailsService(
            final SewerageApplicationDetailsRepository sewerageApplicationDetailsRepository) {
        this.sewerageApplicationDetailsRepository = sewerageApplicationDetailsRepository;
    }

    public SewerageApplicationDetails findBy(final Long id) {
        return sewerageApplicationDetailsRepository.findOne(id);
    }

    public SewerageApplicationDetails findByApplicationNumber(final String applicationNumber) {
        return sewerageApplicationDetailsRepository.findByApplicationNumber(applicationNumber);
    }

    public SewerageApplicationDetails findByApplicationNumberAndConnectionStatus(final String applicationNumber,
                                                                                 final SewerageConnectionStatus status) {
        return sewerageApplicationDetailsRepository.findByApplicationNumberAndConnection_Status(applicationNumber,
                status);
    }

    public SewerageApplicationDetails findByConnectionShscNumberAndConnectionStatus(final String shscNumber,
                                                                                    final SewerageConnectionStatus status) {
        return sewerageApplicationDetailsRepository.findByConnection_ShscNumberAndConnection_Status(shscNumber,
                status);
    }

    public List<SewerageApplicationDetails> findByConnectionShscNumber(final String shscNumber) {
        return sewerageApplicationDetailsRepository.findByConnection_ShscNumber(shscNumber);
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
            final SewerageApplicationDetails sewerageApplicationDetails, final Long approvalPosition,
            final String approvalComent, final String additionalRule, final MultipartFile[] files, final String workFlowAction,
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
        if (!sewerageApplicationDetails.getApplicationType().getCode().equalsIgnoreCase(CLOSESEWERAGECONNECTION))
            if (sewerageApplicationDetails != null && sewerageApplicationDetails.getCurrentDemand() == null) {
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
            applicationWorkflowCustomDefaultImpl.createCloseConnectionWorkflowTransition(sewerageApplicationDetails,
                    approvalPosition, approvalComent, additionalRule, workFlowAction);

        else
            applicationWorkflowCustomDefaultImpl.createCommonWorkflowTransition(sewerageApplicationDetails,
                    approvalPosition, approvalComent, additionalRule, workFlowAction);

        if (sewerageWorkflowService.isCitizenPortalUser(securityUtils.getCurrentUser()))
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
        return sewerageApplicationDetailsRepository.getSewerageConnectionDetailsByPropertyID(propertyIdentifier);
    }

    public SewerageApplicationDetails findByConnection_ShscNumberAndIsActive(final String shscNumber) {
        return sewerageApplicationDetailsRepository.getActiveSewerageApplicationByShscNumber(shscNumber);
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
        String errorMessage = "";
        if (assessmentDetails.getPropertyDetails() != null
                && assessmentDetails.getPropertyDetails().getTaxDue() != null
                && assessmentDetails.getPropertyDetails().getTaxDue().doubleValue() > 0)

        /**
         * If property tax due present and configuration value is 'NO' then restrict not to allow new water tap connection
         * application. If configuration value is 'YES' then new water tap connection can be created even though there is
         * Property Tax Due present.
         **/
            if (!sewerageTaxUtils.isNewConnectionAllowedIfPTDuePresent())
                errorMessage = stmsMessageSource.getMessage("err.validate.seweragenewconnection.property.taxdue", new String[]{
                        assessmentDetails.getPropertyDetails().getTaxDue().toString(), asessmentNumber, "new"}, null);
        return errorMessage;
    }

    public String checkConnectionPresentForProperty(final String propertyID) {
        String validationMessage = "";
        final List<SewerageApplicationDetails> sewerageApplicationDetails = getSewerageConnectionDetailsByPropertyIDentifier(
                propertyID);
        if (sewerageApplicationDetails != null && !sewerageApplicationDetails.isEmpty())
            if (sewerageApplicationDetails.get(0).getConnection().getStatus().toString()
                    .equalsIgnoreCase(SewerageConnectionStatus.ACTIVE.toString()))
                validationMessage = stmsMessageSource.getMessage("err.validate.seweragenewconnection.active", new String[]{
                        sewerageApplicationDetails.get(0).getConnection().getShscNumber(), propertyID}, null);
            else if (sewerageApplicationDetails.get(0).getConnection().getStatus().toString()
                    .equalsIgnoreCase(SewerageConnectionStatus.INPROGRESS.toString()))
                validationMessage = stmsMessageSource.getMessage("err.validate.seweragenewconnection.application.inprocess",
                        new String[]{propertyID, sewerageApplicationDetails.get(0).getApplicationNumber()}, null);
            else if (sewerageApplicationDetails.get(0).getConnection().getStatus().toString()
                    .equalsIgnoreCase(SewerageConnectionStatus.INACTIVE.toString()))
                validationMessage = stmsMessageSource.getMessage("err.validate.seweragenewconnection.inactive", new String[]{
                        sewerageApplicationDetails.get(0).getConnection().getShscNumber(), propertyID}, null);
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
            applicationIndex.setOwnerName(user != null ? user.getUsername() + "::" + user.getName() : "");

            // mark application index as closed on Connection Sanction
            if (sewerageApplicationDetails.getStatus().getCode()
                    .equals(APPLICATION_STATUS_SANCTIONED)) {
                applicationIndex.setApproved(ApprovalStatus.APPROVED);
                applicationIndex.setClosed(ClosureStatus.YES);
            }
            // mark application index as rejected and closed on Connection
            // cancellation
            else if (sewerageApplicationDetails.getStatus().getCode()
                    .equals(APPLICATION_STATUS_CANCELLED)) {
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

            AppConfigValues slaForSewerageConn = null;
            if (sewerageApplicationDetails != null && sewerageApplicationDetails.getApplicationType() != null
                    && SewerageTaxConstants.APPLICATION_TYPE_NAME_NEWCONNECTION
                    .equals(sewerageApplicationDetails.getApplicationType().getName())) {
                slaForSewerageConn = getSlaAppConfigValuesForMarriageReg(SewerageTaxConstants.MODULE_NAME,
                        SewerageTaxConstants.SLAFORNEWSEWERAGECONNECTION);
            } else if (sewerageApplicationDetails != null && sewerageApplicationDetails.getApplicationType() != null
                    && SewerageTaxConstants.APPLICATION_TYPE_NAME_CHANGEINCLOSETS
                    .equals(sewerageApplicationDetails.getApplicationType().getName())) {
                slaForSewerageConn = getSlaAppConfigValuesForMarriageReg(SewerageTaxConstants.MODULE_NAME,
                        SewerageTaxConstants.SLAFORCHANGEINCLOSET);
            }
            applicationIndex = ApplicationIndex.builder().withModuleName(APPL_INDEX_MODULE_NAME)
                    .withApplicationNumber(sewerageApplicationDetails.getApplicationNumber())
                    .withApplicationDate(sewerageApplicationDetails.getApplicationDate())
                    .withApplicationType(sewerageApplicationDetails.getApplicationType().getName())
                    .withApplicantName(consumerName.toString())
                    .withStatus(sewerageApplicationDetails.getStatus().getDescription()).withUrl(
                            String.format(STMS_APPLICATION_VIEW, sewerageApplicationDetails.getApplicationNumber(),
                                    sewerageApplicationDetails.getConnectionDetail().getPropertyIdentifier()))
                    .withApplicantAddress(assessmentDetails.getPropertyAddress())
                    .withOwnername(user.getUsername() + "::" + user.getName())
                    .withChannel(sewerageApplicationDetails.getSource() == null ? Source.SYSTEM.toString()
                            : sewerageApplicationDetails.getSource())
                    .withDisposalDate(sewerageApplicationDetails.getDisposalDate())
                    .withMobileNumber(mobileNumber.toString()).withClosed(ClosureStatus.NO)
                    .withAadharNumber(aadharNumber.toString())
                    .withSla(slaForSewerageConn != null && slaForSewerageConn.getValue() != null
                            ? Integer.valueOf(slaForSewerageConn.getValue()) : 0)
                    .withApproved(ApprovalStatus.INPROGRESS).build();
            applicationIndexService.createApplicationIndex(applicationIndex);
            if (LOG.isDebugEnabled())
                LOG.debug("Application Index creation completed...");
            sewerageIndexService.createSewarageIndex(sewerageApplicationDetails, assessmentDetails);
        }
    }

    public AppConfigValues getSlaAppConfigValuesForMarriageReg(final String moduleName, final String keyName) {
        final List<AppConfigValues> appConfigValues = appConfigValuesService.getConfigValuesByModuleAndKey(moduleName, keyName);
        return !appConfigValues.isEmpty() ? appConfigValues.get(0) : null;
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
                    || currentState.equalsIgnoreCase(WF_STATE_ASSISTANT_APPROVED)
                    || currentState.equalsIgnoreCase(WF_STATE_DEPUTY_EXE_APPROVED)
                    || currentState.equalsIgnoreCase(WF_STATE_PAYMENTDONE))
                modelParams.put("showApprovalDtls", "no");
            else
                modelParams.put("showApprovalDtls", "yes");
            if (currentState.equalsIgnoreCase(WF_STATE_INSPECTIONFEE_COLLECTED)
                    || currentState.equalsIgnoreCase(WF_STATE_CLERK_APPROVED))
                modelParams.put("mode", "edit");
            else if (currentState.equalsIgnoreCase(WF_STATE_REJECTED))
                modelParams.put("mode", "editOnReject");
            else if ("NEW".equalsIgnoreCase(currentState) && (SewerageTaxConstants.APPLICATION_STATUS_CSCCREATED
                    .equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode())
                    || SewerageTaxConstants.APPLICATION_STATUS_ANONYMOUSCREATED.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode()) || SewerageTaxConstants.APPLICATION_STATUS_CITIZENCREATED.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode())))
                modelParams.put("mode", "closetview");
            else
                modelParams.put("mode", "view");
        }
        return modelParams;
    }

    public SewerageApplicationDetails updateSewerageApplicationDetails(
            final SewerageApplicationDetails sewerageApplicationDetails) {
        return sewerageApplicationDetailsRepository.saveAndFlush(sewerageApplicationDetails);
    }

    @Transactional
    public SewerageApplicationDetails updateSewerageApplicationDetails(
            final SewerageApplicationDetails sewerageApplicationDetails, final Long approvalPosition,
            final String approvalComent, final String additionalRule, final String workFlowAction, final String mode,
            final ReportOutput reportOutput, final HttpServletRequest request, final HttpSession session) {

        // In change in closet if demand reduced, sewerage tax collection shld not be done. Hence directly fwd application from
        // DEE to EE and
        // also generate workorder notice no for dee approved applications
        if (sewerageApplicationDetails.getStatus().getCode()
                .equalsIgnoreCase(APPLICATION_STATUS_FEEPAID)
                && sewerageApplicationDetails.getState().getValue()
                .equalsIgnoreCase(WF_STATE_PAYMENTDONE)
                || sewerageApplicationDetails.getStatus().getCode()
                .equalsIgnoreCase(APPLICATION_STATUS_DEEAPPROVED)
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
                    sewerageApplicationDetails.getConnection().setShscNumber(
                            shscNumberGenerator.generateNextSHSCNumber(sewerageApplicationDetails));
            }
        }

        if (sewerageApplicationDetails.getStatus().getCode() != null
                && sewerageApplicationDetails.getStatus().getCode()
                .equalsIgnoreCase(APPLICATION_STATUS_INITIALAPPROVED)
                || sewerageApplicationDetails.getStatus().getCode()
                .equalsIgnoreCase(APPLICATION_STATUS_INSPECTIONFEEPAID)
                || sewerageApplicationDetails.getStatus().getCode()
                .equalsIgnoreCase(APPLICATION_STATUS_CREATED))
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
                final SewerageApplicationDetails oldSewerageAppDtls = findByConnection_ShscNumberAndIsActive(
                        sewerageApplicationDetails.getConnection().getShscNumber());
                if (!workFlowAction.equals(WFLOW_ACTION_STEP_REJECT))
                    if (sewerageApplicationDetails.getStatus().getCode()
                            .equalsIgnoreCase(APPLICATION_STATUS_INITIALAPPROVED))
                        sewerageDemandService.updateDemandOnChangeInClosets(oldSewerageAppDtls,
                                sewerageApplicationDetails.getConnectionFees(),
                                sewerageApplicationDetails.getCurrentDemand(), Boolean.TRUE);
                    else
                        sewerageDemandService.updateDemandOnChangeInClosets(oldSewerageAppDtls,
                                sewerageApplicationDetails.getConnectionFees(),
                                sewerageApplicationDetails.getCurrentDemand(), Boolean.FALSE);
            } else
                sewerageDemandService.updateDemand(sewerageApplicationDetails.getConnectionFees(),
                        sewerageApplicationDetails.getCurrentDemand());

        if (sewerageApplicationDetails.getStatus().getCode().equalsIgnoreCase(APPLICATION_STATUS_DEEAPPROVED) &&
                sewerageApplicationDetails.getState().getValue().equalsIgnoreCase(WF_STATE_DEPUTY_EXE_APPROVED) &&
                additionalRule != null && !additionalRule.equalsIgnoreCase(CHANGEINCLOSETS_NOCOLLECTION))
            if (sewerageApplicationDetails.getEstimationNumber() == null) {
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

        applicationWorkflowCustomDefaultImpl.createCommonWorkflowTransition(updatedSewerageApplicationDetails,
                approvalPosition, approvalComent, additionalRule, workFlowAction);

        if (sewerageApplicationDetails.getApplicationType().getCode().equalsIgnoreCase(CHANGEINCLOSETS)
                && sewerageApplicationDetails.getParent() != null)
            updateIndexes(sewerageApplicationDetails.getParent());
        updatePortalMessage(updatedSewerageApplicationDetails);
        updateIndexes(sewerageApplicationDetails);

        if (APPLICATION_STATUS_COLLECTINSPECTIONFEE.equalsIgnoreCase(sewerageApplicationDetails
                .getStatus().getCode())
                || APPLICATION_STATUS_DEEAPPROVED.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode())
                || APPLICATION_STATUS_CREATED.equalsIgnoreCase(sewerageApplicationDetails
                .getStatus().getCode())) {
            sewerageApplicationDetails.setApprovalComent(approvalComent);
            sewerageConnectionSmsAndEmailService.sendSmsAndEmail(sewerageApplicationDetails, request);
        } else if (APPLICATION_STATUS_FINALAPPROVED.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode())
                || APPLICATION_STATUS_REJECTED.equalsIgnoreCase(sewerageApplicationDetails
                .getStatus().getCode())) {
            sewerageApplicationDetails.setApprovalComent(approvalComent);
            sewerageConnectionSmsAndEmailService.sendSmsAndEmail(sewerageApplicationDetails, request);
        }

        return updatedSewerageApplicationDetails;
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
                        sewerageApplicationDetails.setRejectionNumber(rejectionNumberGenerator.generateRejectionNoticeNumber());
                        sewerageApplicationDetails.setRejectionDate(new Date());
                    }
                }

                final SewerageNotice sewerageNotice = sewerageNoticeService.generateReportForRejection(
                        sewerageApplicationDetails, session, request);
                if (sewerageNotice != null)
                    sewerageApplicationDetails.addNotice(sewerageNotice);
            } else if ("NEW".equalsIgnoreCase(sewerageApplicationDetails.getState().getValue())
                    && (SewerageTaxConstants.APPLICATION_STATUS_CSCCREATED
                    .equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode())
                    || (SewerageTaxConstants.APPLICATION_STATUS_ANONYMOUSCREATED
                    .equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode())))) {
                if (sewerageTaxUtils.isInspectionFeeCollectionRequired()) {
                    sewerageApplicationDetails.setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(
                            SewerageTaxConstants.APPLICATION_STATUS_COLLECTINSPECTIONFEE, SewerageTaxConstants.MODULETYPE));

                } else {
                    sewerageApplicationDetails.setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(
                            SewerageTaxConstants.APPLICATION_STATUS_CREATED, SewerageTaxConstants.MODULETYPE));
                }

            } else if ((SewerageTaxConstants.APPLICATION_STATUS_FEECOLLECTIONPENDING.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode()))) {
                sewerageApplicationDetails.setStatus(
                        sewerageTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_INSPECTIONFEEPAID, MODULETYPE));
            } else if ((sewerageApplicationDetails.getStatus().getCode().equals(APPLICATION_STATUS_CREATED))
                    || (sewerageApplicationDetails.getStatus().getCode().equals(APPLICATION_STATUS_INSPECTIONFEEPAID) && !sewerageApplicationDetails.getState().getValue().equalsIgnoreCase("NEW")))
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
                    && sewerageApplicationDetails.getState().getValue().equalsIgnoreCase(WF_STATE_PAYMENTDONE))
                sewerageApplicationDetails
                        .setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_FINALAPPROVED, MODULETYPE));
            else if (sewerageApplicationDetails.getStatus().getCode().equals(APPLICATION_STATUS_FINALAPPROVED))
                sewerageApplicationDetails
                        .setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_WOGENERATED, MODULETYPE));
            else if (sewerageApplicationDetails.getStatus().getCode().equals(APPLICATION_STATUS_WOGENERATED)) {
                sewerageApplicationDetails
                        .setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_SANCTIONED, MODULETYPE));
                // Make Connection status active on connection execution
                if (sewerageApplicationDetails.getConnection().getStatus().equals(SewerageConnectionStatus.INPROGRESS))
                    sewerageApplicationDetails.getConnection().setStatus(SewerageConnectionStatus.ACTIVE);
                sewerageApplicationDetails.setActive(true);
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
            final SewerageApplicationDetails sewerageApplicationDetails, final Long approvalPosition,
            final String approvalComent, final String additionalRule, final String workFlowAction,
            final ReportOutput reportOutput, final HttpServletRequest request, final HttpSession session) {

        // Generate closure notice number and date
        if (sewerageApplicationDetails.getStatus().getCode().equalsIgnoreCase(APPLICATION_STATUS_DEEAPPROVED)
                && sewerageApplicationDetails.getApplicationType().getCode().equalsIgnoreCase(CLOSESEWERAGECONNECTION)
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
        if (null != sewerageApplicationDetails && null != sewerageApplicationDetails.getStatus()
                && null != sewerageApplicationDetails.getStatus().getCode())
            if (WFLOW_ACTION_STEP_REJECT.equalsIgnoreCase(workFlowAction))
                sewerageApplicationDetails
                        .setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_REJECTED, MODULETYPE));
            else if (WFLOW_ACTION_STEP_CANCEL.equalsIgnoreCase(workFlowAction)) {
                sewerageApplicationDetails.getConnection().setStatus(SewerageConnectionStatus.ACTIVE);
                sewerageApplicationDetails
                        .setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_CANCELLED, MODULETYPE));
            } else if (sewerageApplicationDetails.getStatus().getCode().equals(APPLICATION_STATUS_CREATED))
                sewerageApplicationDetails
                        .setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_INITIALAPPROVED, MODULETYPE));
            else if (sewerageApplicationDetails.getStatus().getCode().equals(APPLICATION_STATUS_INITIALAPPROVED))
                sewerageApplicationDetails
                        .setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_DEEAPPROVED, MODULETYPE));
            else if (sewerageApplicationDetails.getStatus().getCode().equals(APPLICATION_STATUS_DEEAPPROVED)) {
                // Make Connection status closed on EE approval
                sewerageApplicationDetails.getConnection().setStatus(SewerageConnectionStatus.CLOSED);
                sewerageApplicationDetails.setActive(true);
                sewerageApplicationDetails.setStatus(
                        sewerageTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_CLOSERSANCTIONED, MODULETYPE));
            } else if (sewerageApplicationDetails.getStatus().getCode().equals(APPLICATION_STATUS_REJECTED))
                sewerageApplicationDetails
                        .setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_CREATED, MODULETYPE));

        // Generate the sewerage notices based on type of notice and save into DB.
        if (sewerageApplicationDetails.getStatus().getCode().equalsIgnoreCase(APPLICATION_STATUS_CLOSERSANCTIONED)
                && APPROVEWORKFLOWACTION.equalsIgnoreCase(workFlowAction)) {
            final SewerageNotice sewerageNotice = sewerageNoticeService.generateReportForCloseConnection(
                    sewerageApplicationDetails,
                    session);
            if (sewerageNotice != null)
                sewerageApplicationDetails.addNotice(sewerageNotice);
        }

        final SewerageApplicationDetails updatedSewerageApplicationDetails = sewerageApplicationDetailsRepository
                .save(sewerageApplicationDetails);

        if (LOG.isDebugEnabled())
            LOG.debug(APPLICATION_WORKFLOW_INITIALIZATION_DONE);

        applicationWorkflowCustomDefaultImpl.createCloseConnectionWorkflowTransition(updatedSewerageApplicationDetails,
                approvalPosition, approvalComent, additionalRule, workFlowAction);

        if (sewerageApplicationDetails.getParent() != null)
            updateIndexes(sewerageApplicationDetails.getParent());
        updateIndexes(sewerageApplicationDetails);

        // support sms and email for close connection
        if (APPLICATION_STATUS_CLOSERSANCTIONED.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode())
                && APPROVEWORKFLOWACTION.equalsIgnoreCase(workFlowAction) ||
                APPLICATION_STATUS_CREATED.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode()) ||
                APPLICATION_STATUS_REJECTED.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode())) {
            sewerageApplicationDetails.setApprovalComent(approvalComent);
            sewerageConnectionSmsAndEmailService.sendSmsAndEmail(sewerageApplicationDetails, request);
        }

        return updatedSewerageApplicationDetails;
    }

    public List<HashMap<String, Object>> getHistory(final SewerageApplicationDetails sewerageApplicationDetails) {
        User user;
        final List<HashMap<String, Object>> historyTable = new ArrayList<>();
        final State<Position> state = sewerageApplicationDetails.getState();
        final HashMap<String, Object> map = new HashMap<>();
        if (null != state) {
            if (!sewerageApplicationDetails.getStateHistory().isEmpty()
                    && sewerageApplicationDetails.getStateHistory() != null)
                Collections.reverse(sewerageApplicationDetails.getStateHistory());
            for (final StateHistory<Position> stateHistory : sewerageApplicationDetails.getStateHistory()) {
                final HashMap<String, Object> HistoryMap = new HashMap<>();
                HistoryMap.put("date", stateHistory.getDateInfo());
                HistoryMap.put("comments", stateHistory.getComments());
                HistoryMap.put("updatedBy", stateHistory.getLastModifiedBy().getUsername() + "::"
                        + stateHistory.getLastModifiedBy().getName());
                HistoryMap.put("status", stateHistory.getValue());
                final Position owner = stateHistory.getOwnerPosition();
                user = stateHistory.getOwnerUser();
                if (null != user && !user.getType().toString().equals("CITIZEN")) {
                    HistoryMap.put("user", user.getUsername() + "::" + user.getName());
                    HistoryMap.put(DEPARTMENT,
                            null != eisCommonService.getDepartmentForUser(user.getId()) ? eisCommonService
                                    .getDepartmentForUser(user.getId()).getName() : "");
                } else if (null != owner && null != owner.getDeptDesig()) {
                    user = eisCommonService.getUserForPosition(owner.getId(), new Date());
                    HistoryMap
                            .put("user", null != user.getUsername() ? user.getUsername() + "::" + user.getName() : "");
                    HistoryMap.put(DEPARTMENT, null != owner.getDeptDesig().getDepartment() ? owner.getDeptDesig()
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
            if (null != user && !user.getType().toString().equals("CITIZEN")) {
                map.put("user", user.getUsername() + "::" + user.getName());
                map.put(DEPARTMENT, null != eisCommonService.getDepartmentForUser(user.getId()) ? eisCommonService
                        .getDepartmentForUser(user.getId()).getName() : "");
            } else if (null != ownerPosition && null != ownerPosition.getDeptDesig()) {
                user = eisCommonService.getUserForPosition(ownerPosition.getId(), new Date());
                map.put("user", null != user.getUsername() ? user.getUsername() + "::" + user.getName() : "");
                map.put(DEPARTMENT, null != ownerPosition.getDeptDesig().getDepartment() ? ownerPosition
                        .getDeptDesig().getDepartment().getName() : "");
            }
            historyTable.add(map);
        }
        return historyTable;
    }

    public void updateStateTransition(final SewerageApplicationDetails sewerageApplicationDetails,
                                      final Long approvalPosition, final String approvalComent, final String additionalRule,
                                      final String workFlowAction) {
        if (approvalPosition != null && additionalRule != null
                && org.apache.commons.lang.StringUtils.isNotEmpty(workFlowAction))
            applicationWorkflowCustomDefaultImpl.createCommonWorkflowTransition(sewerageApplicationDetails,
                    approvalPosition, approvalComent, additionalRule, workFlowAction);
        // update index on collection
    }

    public SewerageApplicationDetails isApplicationInProgress(final String shscNumber) {
        return sewerageApplicationDetailsRepository.getSewerageApplicationInWorkFlow(shscNumber);
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
        sewerageCriteria.add(Restrictions.eq("demand.isHistory", SewerageTaxConstants.DEMANDISHISTORY));
        sewerageCriteria.add(Restrictions.eq("sewerageDetails.isActive", true));
        if (installment != null)
            sewerageCriteria.add(Restrictions.eq("demand.egInstallmentMaster.id", installment.getId()));
        sewerageCriteria.setMaxResults(noOfResultToFetch);
        sewerageCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return sewerageCriteria.list();
    }

    public AppConfigValues getAppConfigValuesForDonationCharge(final String moduleName, final String keyName) {
        final List<AppConfigValues> appConfigValues = appConfigValuesService.getConfigValuesByModuleAndKey(moduleName, keyName);
        return !appConfigValues.isEmpty() ? appConfigValues.get(0) : null;
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

    public ReportOutput getReportParamsForSewerageAcknowdgement(final SewerageApplicationDetails sewerageApplicationDetails,
                                                                final String municipalityName, final String cityName) {
        final Map<String, Object> reportParams = new HashMap<>();
        String ownerName = "";
        reportParams.put(MUNICIPALITY, municipalityName);
        reportParams.put(CITYNAME, cityName);
        final AssessmentDetails assessmentDetails = sewerageTaxUtils.getAssessmentDetailsForFlag(
                sewerageApplicationDetails.getConnectionDetail().getPropertyIdentifier(),
                PropertyExternalService.FLAG_FULL_DETAILS);
        if (null != assessmentDetails.getOwnerNames())
            for (final OwnerName names : assessmentDetails.getOwnerNames()) {
                ownerName = names.getOwnerName();
                break;
            }
        reportParams.put(ZONE_NAME,
                assessmentDetails.getBoundaryDetails() != null ? assessmentDetails.getBoundaryDetails().getZoneName() : "");
        reportParams.put(ELECTION_WARD,
                assessmentDetails.getBoundaryDetails() != null ? assessmentDetails.getBoundaryDetails().getWardName() : "");
        reportParams.put(ADDRESS, assessmentDetails.getPropertyAddress());

        reportParams.put(APPLICANT_NAME, ownerName);
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
        String slaapptype = null;
        if (SewerageTaxConstants.CHANGEINCLOSETS.equalsIgnoreCase(sewerageApplicationDetails.getApplicationType().getCode()))
            slaapptype = SewerageTaxConstants.SLAFORCHANGEINCLOSET;
        else
            slaapptype = SewerageTaxConstants.SLAFORNEWSEWERAGECONNECTION;
        Date dueDate;
        final Date currentDate = new Date();
        final AppConfigValues slaForSewerageConn = getSlaAppConfigValuesForSewerage(SewerageTaxConstants.MODULE_NAME,
                slaapptype);
        dueDate = org.apache.commons.lang3.time.DateUtils.addDays(currentDate,
                (slaForSewerageConn != null && slaForSewerageConn.getValue() != null)
                        ? Integer.valueOf(slaForSewerageConn.getValue()) : 0);
        return dueDate;

    }

    public AppConfigValues getSlaAppConfigValuesForSewerage(final String moduleName, final String keyName) {
        final List<AppConfigValues> appConfigValues = appConfigValuesService.getConfigValuesByModuleAndKey(moduleName, keyName);
        return !appConfigValues.isEmpty() ? appConfigValues.get(0) : null;
    }

    /**
     * Method to push data for citizen portal inbox
     */

    @Transactional
    public void pushPortalMessage(final SewerageApplicationDetails sewerageApplicationDetails) {
        final SewerageConnection sewerageConnection = sewerageApplicationDetails.getConnection();
        final Module module = moduleDao.getModuleByName(SewerageTaxConstants.MODULE_NAME);

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
        final Module module = moduleDao.getModuleByName(SewerageTaxConstants.MODULE_NAME);
        return portalInboxService.getPortalInboxByApplicationNo(applicationNumber, module.getId());
    }

    /**
     * Method to update data for citizen portal inbox
     */
    @Transactional
    public void updatePortalMessage(final SewerageApplicationDetails sewerageApplicationDetails) {
        final Module module = moduleDao.getModuleByName(SewerageTaxConstants.MODULE_NAME);
        final SewerageConnection sewerageConnection = sewerageApplicationDetails.getConnection();
        String url;
        String status;
        if (sewerageApplicationDetails.getStatus().getCode()
                .equalsIgnoreCase(SewerageTaxConstants.APPLICATION_STATUS_FEECOLLECTIONPENDING)) {
            url = STMS_APPLICATION_UPDATE;
            status = "Inspection fee paid";
        } else {
            url = STMS_APPLICATION_VIEW;
            status = sewerageApplicationDetails.getStatus().getDescription();
        }
        portalInboxService.updateInboxMessage(sewerageApplicationDetails.getApplicationNumber(), module.getId(),
                status,
                isResolved(sewerageApplicationDetails), calculateDueDate(sewerageApplicationDetails),
                sewerageApplicationDetails.getState(),
                null,
                sewerageConnection.getShscNumber(),
                String.format(url, sewerageApplicationDetails.getApplicationNumber(),
                        sewerageApplicationDetails.getConnectionDetail().getPropertyIdentifier()));
    }

}