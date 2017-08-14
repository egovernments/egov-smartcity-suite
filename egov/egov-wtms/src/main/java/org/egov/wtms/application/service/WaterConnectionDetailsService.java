/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */
package org.egov.wtms.application.service;

import static org.egov.wtms.utils.constants.WaterTaxConstants.WFLOW_ACTION_STEP_REJECT;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ValidationException;

import org.egov.commons.EgModules;
import org.egov.commons.Installment;
import org.egov.commons.entity.Source;
import org.egov.demand.model.EgDemand;
import org.egov.eis.entity.Assignment;
import org.egov.eis.entity.AssignmentAdaptor;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.infra.elasticsearch.entity.ApplicationIndex;
import org.egov.infra.elasticsearch.entity.enums.ApprovalStatus;
import org.egov.infra.elasticsearch.entity.enums.ClosureStatus;
import org.egov.infra.elasticsearch.service.ApplicationIndexService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.persistence.entity.enums.UserType;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.ApplicationNumberGenerator;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.pims.commons.Position;
import org.egov.portal.entity.PortalInbox;
import org.egov.portal.entity.PortalInboxBuilder;
import org.egov.portal.service.PortalInboxService;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.OwnerName;
import org.egov.ptis.domain.model.enums.BasicPropertyStatus;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.wtms.application.entity.ApplicationDocuments;
import org.egov.wtms.application.entity.WaterConnection;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.repository.WaterConnectionDetailsRepository;
import org.egov.wtms.application.workflow.ApplicationWorkflowCustomDefaultImpl;
import org.egov.wtms.entity.es.WaterChargeDocument;
import org.egov.wtms.masters.entity.ApplicationType;
import org.egov.wtms.masters.entity.DocumentNames;
import org.egov.wtms.masters.entity.DonationDetails;
import org.egov.wtms.masters.entity.WaterRatesDetails;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.masters.entity.enums.ConnectionType;
import org.egov.wtms.masters.service.ApplicationProcessTimeService;
import org.egov.wtms.masters.service.ApplicationTypeService;
import org.egov.wtms.masters.service.DocumentNamesService;
import org.egov.wtms.service.es.WaterChargeDocumentService;
import org.egov.wtms.utils.PropertyExtnUtils;
import org.egov.wtms.utils.WaterTaxNumberGenerator;
import org.egov.wtms.utils.WaterTaxUtils;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jfree.util.Log;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@Service
@Transactional(readOnly = true)
public class WaterConnectionDetailsService {

    private static final String WTMS_APPLICATION_VIEW = "/wtms/application/view/%s";
    protected WaterConnectionDetailsRepository waterConnectionDetailsRepository;
    private static final Logger LOG = LoggerFactory.getLogger(WaterConnectionDetailsService.class);
    private static final String APPLICATION_NO = "Application no ";
    private static final String REGARDING = " regarding ";
    private static final String STATUS = " status ";

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationNumberGenerator applicationNumberGenerator;

    @Autowired
    private ApplicationTypeService applicationTypeService;

    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<WaterConnectionDetails> waterConnectionWorkflowService;

    @Autowired
    private ApplicationProcessTimeService applicationProcessTimeService;

    @Autowired
    private ApplicationIndexService applicationIndexService;

    @Autowired
    private DocumentNamesService documentNamesService;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private PropertyExtnUtils propertyExtnUtils;

    @Autowired
    private EisCommonService eisCommonService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private WaterTaxNumberGenerator waterTaxNumberGenerator;

    @Autowired
    private WaterChargeDocumentService waterChargeIndexService;

    @Autowired
    private WaterTaxUtils waterTaxUtils;

    @Autowired
    private ConnectionDemandService connectionDemandService;

    @Autowired
    private WaterConnectionSmsAndEmailService waterConnectionSmsAndEmailService;

    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;

    @Autowired
    private ModuleService moduleDao;

    @Autowired
    private PortalInboxService portalInboxService;

    @Autowired
    public WaterConnectionDetailsService(final WaterConnectionDetailsRepository waterConnectionDetailsRepository) {
        this.waterConnectionDetailsRepository = waterConnectionDetailsRepository;
    }

    public WaterConnectionDetails findBy(final Long waterConnectionId) {
        return waterConnectionDetailsRepository.findOne(waterConnectionId);
    }

    public List<WaterConnectionDetails> findAll() {
        return waterConnectionDetailsRepository
                .findAll(new Sort(Sort.Direction.ASC, WaterTaxConstants.APPLICATION_NUMBER));
    }

    public WaterConnectionDetails findByApplicationNumber(final String applicationNumber) {
        return waterConnectionDetailsRepository.findByApplicationNumber(applicationNumber);
    }

    public WaterConnectionDetails load(final Long id) {
        return waterConnectionDetailsRepository.getOne(id);
    }

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    public Page<WaterConnectionDetails> getListWaterConnectionDetails(final Integer pageNumber,
            final Integer pageSize) {
        final Pageable pageable = new PageRequest(pageNumber - 1, pageSize, Sort.Direction.ASC,
                WaterTaxConstants.APPLICATION_NUMBER);
        return waterConnectionDetailsRepository.findAll(pageable);
    }

    @Transactional
    public WaterConnectionDetails createNewWaterConnection(final WaterConnectionDetails waterConnectionDetails,
            final Long approvalPosition, final String approvalComent, final String additionalRule,
            final String workFlowAction, final String sourceChannel) {
        if (waterConnectionDetails.getApplicationNumber() == null)
            waterConnectionDetails.setApplicationNumber(applicationNumberGenerator.generate());
        waterConnectionDetails.setApplicationDate(new Date());
        final Integer appProcessTime = applicationProcessTimeService.getApplicationProcessTime(
                waterConnectionDetails.getApplicationType(), waterConnectionDetails.getCategory());
        if (appProcessTime != null)
            waterConnectionDetails.setDisposalDate(getDisposalDate(waterConnectionDetails, appProcessTime));
        final WaterConnectionDetails savedWaterConnectionDetails = waterConnectionDetailsRepository
                .save(waterConnectionDetails);
        final User meesevaUser = userService.getUserById(waterConnectionDetails.getCreatedBy().getId());
        if (meesevaUser.getUsername().equals(WaterTaxConstants.USERNAME_MEESEVA)) {
            ApplicationThreadLocals.setUserId(meesevaUser.getId());
            savedWaterConnectionDetails.setCreatedBy(meesevaUser);
        }
        if (LOG.isDebugEnabled())
            LOG.debug(" persisting WaterConnectionDetail object is completed and WorkFlow API Stared ");
        final ApplicationWorkflowCustomDefaultImpl applicationWorkflowCustomDefaultImpl = getInitialisedWorkFlowBean();
        if (LOG.isDebugEnabled())
            LOG.debug("applicationWorkflowCustomDefaultImpl initialization is done");
        applicationWorkflowCustomDefaultImpl.createCommonWorkflowTransition(savedWaterConnectionDetails,
                approvalPosition, approvalComent, additionalRule, workFlowAction);
        if (waterTaxUtils.isCitizenPortalUser(securityUtils.getCurrentUser()))
            pushPortalMessage(savedWaterConnectionDetails);
        updateIndexes(savedWaterConnectionDetails, sourceChannel);
        waterConnectionSmsAndEmailService.sendSmsAndEmail(waterConnectionDetails, workFlowAction);
        if (LOG.isDebugEnabled())
            LOG.debug("updating water Connection Deatail is completed");

        return savedWaterConnectionDetails;
    }

    @Transactional
    public WaterConnectionDetails createExisting(final WaterConnectionDetails waterConnectionDetails) {
        if (waterConnectionDetails.getConnection() != null && waterConnectionDetails.getConnection().getConsumerCode() == null)
            waterConnectionDetails.getConnection().setConsumerCode(waterTaxNumberGenerator.getNextConsumerNumber());
        waterConnectionDetails.getExistingConnection().setWaterConnectionDetails(waterConnectionDetails);
        waterConnectionDetails.setApplicationNumber(waterConnectionDetails.getConnection().getConsumerCode());
        waterConnectionDetails.setApplicationDate(waterConnectionDetails.getExecutionDate());
        waterConnectionDetails.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(
                WaterTaxConstants.APPLICATION_STATUS_SANCTIONED, WaterTaxConstants.MODULETYPE));
        if (waterConnectionDetails.getApplicationType().getCode().equalsIgnoreCase(WaterTaxConstants.ADDNLCONNECTION)) {
            final WaterConnectionDetails primaryConnectionDetails = getPrimaryConnectionDetailsByPropertyIdentifier(
                    waterConnectionDetails.getConnection().getPropertyIdentifier());
            waterConnectionDetails.getConnection().setParentConnection(primaryConnectionDetails.getConnection());
        }
        final WaterConnectionDetails savedWaterConnectionDetails = waterConnectionDetailsRepository
                .save(waterConnectionDetails);
        updateConsumerIndex(savedWaterConnectionDetails);
        // TODO Updation of Demand should be done here also fixupdate indexes
        return savedWaterConnectionDetails;
    }

    public List<ConnectionType> getAllConnectionTypes() {
        return Arrays.asList(ConnectionType.values());
    }

    public Map<String, String> getConnectionTypesMap() {
        final Map<String, String> connectionTypeMap = new LinkedHashMap<>(0);
        connectionTypeMap.put(ConnectionType.METERED.toString(), WaterTaxConstants.METERED);
        connectionTypeMap.put(ConnectionType.NON_METERED.toString(), WaterTaxConstants.NON_METERED);
        return connectionTypeMap;
    }

    public List<DocumentNames> getAllActiveDocumentNames(final ApplicationType applicationType) {
        return documentNamesService.getAllActiveDocumentNamesByApplicationType(applicationType);
    }

    public WaterConnectionDetails findByApplicationNumberOrConsumerCodeAndStatus(final String number,
            final ConnectionStatus connectionStatus) {
        return waterConnectionDetailsRepository
                .findConnectionDetailsByApplicationNumberOrConsumerCodeAndConnectionStatus(number, number,
                        connectionStatus);
    }

    public WaterConnectionDetails findByApplicationNumberOrConsumerCode(final String number) {
        return waterConnectionDetailsRepository.findConnectionDetailsByApplicationNumberOrConsumerCode(number, number);
    }

    public WaterConnectionDetails findByConnection(final WaterConnection waterConnection) {
        return waterConnectionDetailsRepository.findByConnection(waterConnection);
    }

    public WaterConnectionDetails findByConsumerCodeAndConnectionStatus(final String consumerCode,
            final ConnectionStatus connectionStatus) {
        return waterConnectionDetailsRepository.findConnectionDetailsByConsumerCodeAndConnectionStatus(consumerCode,
                connectionStatus);
    }

    public WaterConnectionDetails findParentConnectionDetailsByConsumerCodeAndConnectionStatus(final String consumerCode,
            final ConnectionStatus connectionStatus) {
        return waterConnectionDetailsRepository.findParentConnectionDetailsByConsumerCodeAndConnectionStatus(consumerCode,
                connectionStatus);
    }

    public WaterConnectionDetails findByOldConsumerNumberAndConnectionStatus(final String oldConsumerNumber,
            final ConnectionStatus connectionStatus) {
        return waterConnectionDetailsRepository.findByConnectionOldConsumerNumberAndConnectionStatus(oldConsumerNumber,
                connectionStatus);
    }

    public WaterConnectionDetails getActiveConnectionDetailsByConnection(final WaterConnection waterConnection) {
        return waterConnectionDetailsRepository.findByConnectionAndConnectionStatus(waterConnection,
                ConnectionStatus.ACTIVE);
    }

    public WaterConnectionDetails getPrimaryConnectionDetailsByPropertyIdentifier(final String propertyIdentifier) {
        return waterConnectionDetailsRepository.getPrimaryConnectionDetailsByPropertyID(propertyIdentifier);
    }

    public WaterConnectionDetails getPrimaryConnectionDetailsByPropertyAssessmentNumbers(final List<String> propertyIdentifier) {
        WaterConnectionDetails waterConnectionDetails = null;
        for (final String assessmentNumber : propertyIdentifier) {
            waterConnectionDetails = waterConnectionDetailsRepository
                    .getPrimaryConnectionDetailsByPropertyAssessmentNumber(assessmentNumber);
            if (waterConnectionDetails != null)
                break;
        }
        return waterConnectionDetails;
    }

    public List<WaterConnectionDetails> getChildConnectionDetailsByPropertyID(final String propertyIdentifier) {
        return waterConnectionDetailsRepository.getChildConnectionDetailsByPropertyID(propertyIdentifier);
    }

    public List<WaterConnectionDetails> getAllConnectionDetailsByParentConnection(final Long parentId) {
        return waterConnectionDetailsRepository.getAllConnectionDetailsByParentConnection(parentId);
    }

    public List<WaterConnectionDetails> getAllConnectionDetailsExceptInactiveStatusByPropertyID(final String propertyIdentifier) {
        return waterConnectionDetailsRepository.getAllConnectionDetailsExceptInactiveStatusByPropertyID(propertyIdentifier);
    }

    @ReadOnly
    public List<HashMap<String, Object>> getHistory(final WaterConnectionDetails waterConnectionDetails) {
        User user;
        final List<HashMap<String, Object>> historyTable = new ArrayList<>(0);
        final State state = waterConnectionDetails.getState();
        final HashMap<String, Object> map = new HashMap<>(0);
        if (null != state) {
            map.put("date", state.getDateInfo());
            map.put("comments", state.getComments() != null ? state.getComments() : "");
            map.put("updatedBy", state.getLastModifiedBy().getUsername() + "::" + state.getLastModifiedBy().getName());
            map.put("status", state.getValue());
            final Position ownerPosition = state.getOwnerPosition();
            user = state.getOwnerUser();
            if (null != user) {
                map.put("user", user.getUsername() + "::" + user.getName());
                map.put("department", null != eisCommonService.getDepartmentForUser(user.getId())
                        ? eisCommonService.getDepartmentForUser(user.getId()).getName() : "");
            } else if (null != ownerPosition && null != ownerPosition.getDeptDesig()) {
                user = eisCommonService.getUserForPosition(ownerPosition.getId(), new Date());
                map.put("user", null != user.getUsername() ? user.getUsername() + "::" + user.getName() : "");
                map.put("department", null != ownerPosition.getDeptDesig().getDepartment()
                        ? ownerPosition.getDeptDesig().getDepartment().getName() : "");
            }
            historyTable.add(map);
            if (!waterConnectionDetails.getStateHistory().isEmpty() && waterConnectionDetails.getStateHistory() != null)
                Collections.reverse(waterConnectionDetails.getStateHistory());
            for (final StateHistory stateHistory : waterConnectionDetails.getStateHistory()) {
                final HashMap<String, Object> historyMap = new HashMap<>(0);
                historyMap.put("date", stateHistory.getDateInfo());
                historyMap.put("comments", stateHistory.getComments() != null ? stateHistory.getComments() : "");
                historyMap.put("updatedBy", stateHistory.getLastModifiedBy().getUsername() + "::"
                        + stateHistory.getLastModifiedBy().getName());
                historyMap.put("status", stateHistory.getValue());
                final Position owner = stateHistory.getOwnerPosition();
                user = stateHistory.getOwnerUser();
                if (null != user) {
                    historyMap.put("user", user.getUsername() + "::" + user.getName());
                    historyMap.put("department", null != eisCommonService.getDepartmentForUser(user.getId())
                            ? eisCommonService.getDepartmentForUser(user.getId()).getName() : "");
                } else if (null != owner && null != owner.getDeptDesig()) {
                    try {
                        user = eisCommonService.getUserForPosition(owner.getId(), stateHistory.getCreatedDate());
                    } catch (final ApplicationRuntimeException e) {
                        if (Log.isErrorEnabled())
                            Log.error("Exception while getting history of record :" + e);
                        throw new ApplicationRuntimeException("err.user.not.found");
                    }
                    historyMap.put("user",
                            null != user.getUsername() ? user.getUsername() + "::" + user.getName() : "");
                    historyMap.put("department", null != owner.getDeptDesig().getDepartment()
                            ? owner.getDeptDesig().getDepartment().getName() : "");
                }
                historyTable.add(historyMap);
            }
        }
        return historyTable;
    }

    @Transactional
    public WaterConnectionDetails updateWaterConnection(final WaterConnectionDetails waterConnectionDetails,
            final Long approvalPosition, final String approvalComent, String additionalRule,
            final String workFlowAction, final String mode, final ReportOutput reportOutput, final String sourceChannel)
            throws ValidationException {
        applicationStatusChange(waterConnectionDetails, workFlowAction, mode, sourceChannel);
        if (WaterTaxConstants.APPLICATION_STATUS_CLOSERDIGSIGNPENDING
                .equals(waterConnectionDetails.getStatus().getCode())
                && waterConnectionDetails.getCloseConnectionType() != null
                && workFlowAction.equals(WaterTaxConstants.APPROVEWORKFLOWACTION)) {
            waterConnectionDetails
                    .setApplicationType(applicationTypeService.findByCode(WaterTaxConstants.CLOSINGCONNECTION));
            waterConnectionDetails.setCloseApprovalDate(new Date());
        }
        if (WaterTaxConstants.APPLICATION_STATUS_RECONNDIGSIGNPENDING
                .equals(waterConnectionDetails.getStatus().getCode())
                && waterConnectionDetails.getCloseConnectionType().equals(WaterTaxConstants.TEMPERARYCLOSECODE)
                && waterConnectionDetails.getReConnectionReason() != null
                && workFlowAction.equals(WaterTaxConstants.APPROVEWORKFLOWACTION)) {
            waterConnectionDetails
                    .setApplicationType(applicationTypeService.findByCode(WaterTaxConstants.RECONNECTIONCONNECTION));
            waterConnectionDetails.setConnectionStatus(ConnectionStatus.ACTIVE);
            waterConnectionDetails.setReconnectionApprovalDate(new Date());
            if (ConnectionType.NON_METERED.equals(waterConnectionDetails.getConnectionType())) {
                Installment nonMeterReconnInstallment;
                Boolean reconnInSameInstallment;
                if (checkTwoDatesAreInSameInstallment(waterConnectionDetails)) {
                    final Installment nonMeterCurrentInstallment = connectionDemandService.getCurrentInstallment(
                            WaterTaxConstants.WATER_RATES_NONMETERED_PTMODULE, null,
                            waterConnectionDetails.getReconnectionApprovalDate());
                    final Calendar cal = Calendar.getInstance();
                    cal.setTime(nonMeterCurrentInstallment.getToDate());
                    cal.add(Calendar.DATE, 1);
                    final Date newDateForNextInstall = cal.getTime();
                    nonMeterReconnInstallment = connectionDemandService.getCurrentInstallment(
                            WaterTaxConstants.WATER_RATES_NONMETERED_PTMODULE, null, newDateForNextInstall);
                    reconnInSameInstallment = Boolean.TRUE;
                } else {
                    nonMeterReconnInstallment = connectionDemandService.getCurrentInstallment(
                            WaterTaxConstants.WATER_RATES_NONMETERED_PTMODULE, null,
                            waterConnectionDetails.getReconnectionApprovalDate());
                    reconnInSameInstallment = Boolean.FALSE;
                }
                connectionDemandService.updateDemandForNonmeteredConnection(waterConnectionDetails,
                        nonMeterReconnInstallment, reconnInSameInstallment, null);
            }
            updateIndexes(waterConnectionDetails, sourceChannel);
        }

        if (ConnectionType.NON_METERED.equals(waterConnectionDetails.getConnectionType())
                && WaterTaxConstants.APPLICATION_STATUS_SANCTIONED
                        .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {
            connectionDemandService.updateDemandForNonmeteredConnection(waterConnectionDetails, null, null, workFlowAction);
            updateIndexes(waterConnectionDetails, sourceChannel);
        }

        // Setting FileStoreMap object object while Commissioner Sign's the
        // document
        if (workFlowAction != null && workFlowAction.equalsIgnoreCase(WaterTaxConstants.SIGNWORKFLOWACTION)
                && reportOutput != null) {
            final String fileName = WaterTaxConstants.SIGNED_DOCUMENT_PREFIX
                    + waterConnectionDetails.getWorkOrderNumber() + ".pdf";
            final InputStream fileStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
            final FileStoreMapper fileStore = fileStoreService.store(fileStream, fileName, "application/pdf",
                    WaterTaxConstants.FILESTORE_MODULECODE);
            waterConnectionDetails.setFileStore(fileStore);
        }

        WaterConnectionDetails updatedWaterConnectionDetails = waterConnectionDetailsRepository
                .save(waterConnectionDetails);
        final ApplicationWorkflowCustomDefaultImpl applicationWorkflowCustomDefaultImpl = getInitialisedWorkFlowBean();
        if (waterConnectionDetails.getCloseConnectionType() != null)
            additionalRule = WaterTaxConstants.WORKFLOW_CLOSUREADDITIONALRULE;

        if (waterConnectionDetails.getReConnectionReason() != null)
            additionalRule = WaterTaxConstants.RECONNECTIONCONNECTION;
        applicationWorkflowCustomDefaultImpl.createCommonWorkflowTransition(updatedWaterConnectionDetails,
                approvalPosition, approvalComent, additionalRule, workFlowAction);

        // To backUpdate waterConnectiondetails after ClosureConnection is
        // cancelled
        if (waterConnectionDetails.getCloseConnectionType() != null
                && waterConnectionDetails.getReConnectionReason() == null
                && waterConnectionDetails.getStatus().getCode().equals(WaterTaxConstants.APPLICATION_STATUS_CANCELLED)
                && waterConnectionDetails.getConnectionStatus().equals(ConnectionStatus.INACTIVE)) {
            waterConnectionDetails.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(
                    WaterTaxConstants.APPLICATION_STATUS_SANCTIONED, WaterTaxConstants.MODULETYPE));
            waterConnectionDetails.setConnectionStatus(ConnectionStatus.ACTIVE);
            waterConnectionDetails.setCloseConnectionType(null);
            waterConnectionDetails.setCloseconnectionreason(null);
            waterConnectionDetails.setApplicationType(
                    applicationTypeService.findByCode(waterConnectionDetails.getPreviousApplicationType()));
            updateIndexes(waterConnectionDetails, sourceChannel);
            updatedWaterConnectionDetails = waterConnectionDetailsRepository.save(waterConnectionDetails);
        }
        // back to CLoserSanctioned Status if Reconnection is Rejected 2 times
        if (waterConnectionDetails.getReConnectionReason() != null
                && waterConnectionDetails.getCloseConnectionType() == WaterTaxConstants.TEMPERARYCLOSECODE
                && waterConnectionDetails.getStatus().getCode().equals(WaterTaxConstants.APPLICATION_STATUS_CANCELLED)
                && waterConnectionDetails.getConnectionStatus().equals(ConnectionStatus.INACTIVE)) {
            waterConnectionDetails.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(
                    WaterTaxConstants.APPLICATION_STATUS_CLOSERSANCTIONED, WaterTaxConstants.MODULETYPE));
            waterConnectionDetails.setConnectionStatus(ConnectionStatus.CLOSED);
            waterConnectionDetails.setReConnectionReason(null);
            waterConnectionDetails
                    .setApplicationType(applicationTypeService.findByCode(WaterTaxConstants.CLOSINGCONNECTION));
            updateIndexes(waterConnectionDetails, sourceChannel);
            updatedWaterConnectionDetails = waterConnectionDetailsRepository.save(waterConnectionDetails);
        }
        if (!workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_REJECT))
            waterConnectionSmsAndEmailService.sendSmsAndEmail(waterConnectionDetails, workFlowAction);

        updateIndexes(waterConnectionDetails, sourceChannel);
        if (waterConnectionDetails.getSource() != null
                && Source.CITIZENPORTAL.toString().equalsIgnoreCase(waterConnectionDetails.getSource().toString())
                && getPortalInbox(waterConnectionDetails.getApplicationNumber()) != null)
            updatePortalMessage(waterConnectionDetails);

        return updatedWaterConnectionDetails;
    }

    /**
     * @return Initialise Bean ApplicationWorkflowCustomDefaultImpl
     */
    public ApplicationWorkflowCustomDefaultImpl getInitialisedWorkFlowBean() {
        ApplicationWorkflowCustomDefaultImpl applicationWorkflowCustomDefaultImpl = null;
        if (null != context)
            applicationWorkflowCustomDefaultImpl = (ApplicationWorkflowCustomDefaultImpl) context
                    .getBean("applicationWorkflowCustomDefaultImpl");
        return applicationWorkflowCustomDefaultImpl;
    }

    public Boolean checkTwoDatesAreInSameInstallment(final WaterConnectionDetails waterConnectionDetails) {
        Boolean dateInSameInstallment = Boolean.FALSE;

        final Installment nonMeterClosedInstallment = connectionDemandService.getCurrentInstallment(
                WaterTaxConstants.WATER_RATES_NONMETERED_PTMODULE, null, waterConnectionDetails.getCloseApprovalDate());
        final Installment nonMeterReconnInstallment = connectionDemandService.getCurrentInstallment(
                WaterTaxConstants.WATER_RATES_NONMETERED_PTMODULE, null,
                waterConnectionDetails.getReconnectionApprovalDate());
        if (nonMeterClosedInstallment.getDescription().equals(nonMeterReconnInstallment.getDescription()))
            dateInSameInstallment = Boolean.TRUE;

        return dateInSameInstallment;
    }

    public void applicationStatusChange(final WaterConnectionDetails waterConnectionDetails,
            final String workFlowAction, final String mode, final String sourceChannel) {
        if (null != waterConnectionDetails && null != waterConnectionDetails.getStatus()
                && null != waterConnectionDetails.getStatus().getCode())
            if (waterConnectionDetails.getStatus().getCode().equals(WaterTaxConstants.APPLICATION_STATUS_CREATED)
                    && waterConnectionDetails.getState() != null && "Submit".equals(workFlowAction))
                waterConnectionDetails.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(
                        WaterTaxConstants.APPLICATION_STATUS_VERIFIED, WaterTaxConstants.MODULETYPE));
            else if (workFlowAction.equals(WaterTaxConstants.WF_STATE_BUTTON_GENERATEESTIMATE)
                    && waterConnectionDetails.getStatus().getCode().equals(WaterTaxConstants.APPLICATION_STATUS_VERIFIED))
                waterConnectionDetails.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(
                        WaterTaxConstants.APPLICATION_STATUS_ESTIMATENOTICEGEN, WaterTaxConstants.MODULETYPE));
            else if (waterConnectionDetails.getStatus().getCode().equals(WaterTaxConstants.APPLICATION_STATUS_FEEPAID)
                    && workFlowAction.equalsIgnoreCase(WaterTaxConstants.APPROVEWORKFLOWACTION)) {

                if (waterConnectionDetails.getConnection().getConsumerCode() == null)
                    waterConnectionDetails.getConnection()
                            .setConsumerCode(waterTaxNumberGenerator.getNextConsumerNumber());

                waterConnectionDetails.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(
                        WaterTaxConstants.APPLICATION_STATUS_DIGITALSIGNPENDING, WaterTaxConstants.MODULETYPE));

            } else if (workFlowAction.equals(WaterTaxConstants.SIGNWORKFLOWACTION) && waterConnectionDetails.getStatus()
                    .getCode().equals(WaterTaxConstants.APPLICATION_STATUS_DIGITALSIGNPENDING))
                waterConnectionDetails.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(
                        WaterTaxConstants.APPLICATION_STATUS_APPROVED, WaterTaxConstants.MODULETYPE));
            else if (workFlowAction.equals(WaterTaxConstants.WF_WORKORDER_BUTTON) && waterConnectionDetails.getStatus()
                    .getCode().equals(WaterTaxConstants.APPLICATION_STATUS_APPROVED))
                waterConnectionDetails.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(
                        WaterTaxConstants.APPLICATION_STATUS_WOGENERATED, WaterTaxConstants.MODULETYPE));
            else if (workFlowAction.equals(WaterTaxConstants.WF_STATE_TAP_EXECUTION_DATE)
                    && WaterTaxConstants.APPLICATION_STATUS_WOGENERATED
                            .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode()))
                waterConnectionDetails.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(
                        WaterTaxConstants.APPLICATION_STATUS_SANCTIONED, WaterTaxConstants.MODULETYPE));
            else if (WaterTaxConstants.APPLICATION_STATUS_SANCTIONED
                    .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())
                    && waterConnectionDetails.getCloseConnectionType() != null) {
                waterConnectionDetails.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(
                        WaterTaxConstants.APPLICATION_STATUS_CLOSERINITIATED, WaterTaxConstants.MODULETYPE));
                updateIndexes(waterConnectionDetails, sourceChannel);
            } else if (!"Reject".equals(workFlowAction))
                if (!"closeredit".equals(mode)
                        && WaterTaxConstants.APPLICATION_STATUS_CLOSERINITIATED
                                .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())
                        && waterConnectionDetails.getCloseConnectionType() != null)
                    waterConnectionDetails.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(
                            WaterTaxConstants.APPLICATION_STATUS_CLOSERINPROGRESS, WaterTaxConstants.MODULETYPE));
                else if (workFlowAction.equals(WaterTaxConstants.APPROVEWORKFLOWACTION)
                        && WaterTaxConstants.APPLICATION_STATUS_CLOSERINPROGRESS
                                .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())
                        && waterConnectionDetails.getCloseConnectionType() != null)
                    waterConnectionDetails.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(
                            WaterTaxConstants.APPLICATION_STATUS_CLOSERDIGSIGNPENDING, WaterTaxConstants.MODULETYPE));
                else if (workFlowAction.equals(WaterTaxConstants.SIGNWORKFLOWACTION)
                        && WaterTaxConstants.APPLICATION_STATUS_CLOSERDIGSIGNPENDING
                                .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())
                        && waterConnectionDetails.getCloseConnectionType() != null) {
                    waterConnectionDetails.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(
                            WaterTaxConstants.APPLICATION_STATUS_CLOSERAPRROVED, WaterTaxConstants.MODULETYPE));

                    updateIndexes(waterConnectionDetails, sourceChannel);
                } else if (WaterTaxConstants.APPLICATION_STATUS_CLOSERAPRROVED
                        .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())
                        && waterConnectionDetails.getCloseConnectionType() != null) {
                    waterConnectionDetails.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(
                            WaterTaxConstants.APPLICATION_STATUS_CLOSERSANCTIONED, WaterTaxConstants.MODULETYPE));
                    updateIndexes(waterConnectionDetails, sourceChannel);
                } else if (WaterTaxConstants.APPLICATION_STATUS_CLOSERSANCTIONED
                        .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())
                        && waterConnectionDetails.getCloseConnectionType() != null
                        && waterConnectionDetails.getCloseConnectionType()
                                .equals(WaterTaxConstants.TEMPERARYCLOSECODE)) {
                    waterConnectionDetails.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(
                            WaterTaxConstants.WORKFLOW_RECONNCTIONINITIATED, WaterTaxConstants.MODULETYPE));
                    updateIndexes(waterConnectionDetails, sourceChannel);
                } else if (!"Reject".equals(workFlowAction))
                    if (!"reconnectioneredit".equals(mode))
                        if (WaterTaxConstants.WORKFLOW_RECONNCTIONINITIATED
                                .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())
                                && waterConnectionDetails.getCloseConnectionType()
                                        .equals(WaterTaxConstants.TEMPERARYCLOSECODE))
                            waterConnectionDetails.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(
                                    WaterTaxConstants.APPLICATION_STATUS__RECONNCTIONINPROGRESS,
                                    WaterTaxConstants.MODULETYPE));
                        else if (workFlowAction.equals(WaterTaxConstants.APPROVEWORKFLOWACTION)
                                && WaterTaxConstants.APPLICATION_STATUS__RECONNCTIONINPROGRESS
                                        .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())
                                && waterConnectionDetails.getCloseConnectionType()
                                        .equals(WaterTaxConstants.TEMPERARYCLOSECODE))
                            waterConnectionDetails.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(
                                    WaterTaxConstants.APPLICATION_STATUS_RECONNDIGSIGNPENDING,
                                    WaterTaxConstants.MODULETYPE));
                        else if (workFlowAction.equals(WaterTaxConstants.SIGNWORKFLOWACTION)
                                && WaterTaxConstants.APPLICATION_STATUS_RECONNDIGSIGNPENDING
                                        .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())
                                && waterConnectionDetails.getCloseConnectionType()
                                        .equals(WaterTaxConstants.TEMPERARYCLOSECODE))
                            waterConnectionDetails.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(
                                    WaterTaxConstants.APPLICATION_STATUS__RECONNCTIONAPPROVED,
                                    WaterTaxConstants.MODULETYPE));
                        else if (WaterTaxConstants.APPLICATION_STATUS__RECONNCTIONAPPROVED
                                .equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())
                                && waterConnectionDetails.getCloseConnectionType()
                                        .equals(WaterTaxConstants.TEMPERARYCLOSECODE))
                            waterConnectionDetails.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(
                                    WaterTaxConstants.APPLICATION_STATUS__RECONNCTIONSANCTIONED,
                                    WaterTaxConstants.MODULETYPE));

    }

    public Long getApprovalPositionByMatrixDesignation(final WaterConnectionDetails waterConnectionDetails,
            Long approvalPosition, final String additionalRule, final String mode, final String workFlowAction) {
        final String loggedInUserDesignation = waterTaxUtils.loggedInUserDesignation(waterConnectionDetails);
        WorkFlowMatrix wfmatrix = null;
        if (loggedInUserDesignation != null && !"".equals(loggedInUserDesignation)
                && (loggedInUserDesignation.equalsIgnoreCase(WaterTaxConstants.COMMISSIONER_DESGN)
                        || loggedInUserDesignation.equalsIgnoreCase(WaterTaxConstants.EXECUTIVE_ENGINEER_DESIGN)
                        || loggedInUserDesignation.equalsIgnoreCase(WaterTaxConstants.MUNICIPAL_ENGINEER_DESIGN)
                        || loggedInUserDesignation.equalsIgnoreCase(WaterTaxConstants.SUPERIENTEND_ENGINEER_DESIGN)
                        || loggedInUserDesignation.equalsIgnoreCase(WaterTaxConstants.TAP_INSPPECTOR_DESIGN)
                        || loggedInUserDesignation.equalsIgnoreCase(WaterTaxConstants.ASSISTANT_ENGINEER_DESIGN)
                        || loggedInUserDesignation
                                .equalsIgnoreCase(WaterTaxConstants.ASSISTANT_EXECUTIVE_ENGINEER_DESIGN))
                && (waterConnectionDetails.getStatus().getCode()
                        .equals(WaterTaxConstants.APPLICATION_STATUS_VERIFIED)
                        || waterConnectionDetails.getStatus().getCode()
                                .equals(WaterTaxConstants.APPLICATION_STATUS_WOGENERATED)
                        || waterConnectionDetails.getStatus().getCode()
                                .equals(WaterTaxConstants.APPLICATION_STATUS_CLOSERINPROGRESS)
                        || waterConnectionDetails.getStatus().getCode()
                                .equals(WaterTaxConstants.APPLICATION_STATUS__RECONNCTIONINPROGRESS)))
            wfmatrix = waterConnectionWorkflowService.getWfMatrix(waterConnectionDetails.getStateType(), null, null,
                    additionalRule, waterConnectionDetails.getCurrentState().getValue(), null, null,
                    loggedInUserDesignation);
        else
            wfmatrix = waterConnectionWorkflowService.getWfMatrix(waterConnectionDetails.getStateType(), null, null,
                    additionalRule, waterConnectionDetails.getCurrentState().getValue(), null);

        if (waterConnectionDetails.getStatus().getCode()
                .equals(WaterTaxConstants.APPLICATION_STATUS_ESTIMATENOTICEGEN))
            approvalPosition = waterTaxUtils.getApproverPosition(
                    WaterTaxConstants.JUNIOR_OR_SENIOR_ASSISTANT_DESIGN_REVENUE_CLERK,
                    waterConnectionDetails);
        if (waterConnectionDetails != null && waterConnectionDetails.getStatus() != null
                && waterConnectionDetails.getStatus().getCode() != null)
            if (waterConnectionDetails.getStatus().getCode().equals(WaterTaxConstants.APPLICATION_STATUS_CREATED)
                    && waterConnectionDetails.getState() != null)
                if ("edit".equals(mode))
                    approvalPosition = waterConnectionDetails.getState().getOwnerPosition().getId();
                else
                    approvalPosition = waterTaxUtils.getApproverPosition(wfmatrix.getNextDesignation(),
                            waterConnectionDetails);
            else if (waterConnectionDetails.getStatus().getCode().equals(WaterTaxConstants.APPLICATION_STATUS_APPROVED)
                    || !"".equals(workFlowAction) && workFlowAction.equals(WFLOW_ACTION_STEP_REJECT)
                            && waterConnectionDetails.getStatus().getCode()
                                    .equals(WaterTaxConstants.APPLICATION_STATUS_CLOSERINITIATED)
                            && waterConnectionDetails.getState().getValue().equals(WaterTaxConstants.WF_STATE_REJECTED))
                approvalPosition = waterTaxUtils.getApproverPosition(wfmatrix.getNextDesignation(),
                        waterConnectionDetails);
            else if (waterConnectionDetails.getStatus().getCode()
                    .equals(WaterTaxConstants.APPLICATION_STATUS_DIGITALSIGNPENDING))
                approvalPosition = waterTaxUtils.getApproverPosition(
                        WaterTaxConstants.JUNIOR_OR_SENIOR_ASSISTANT_DESIGN, waterConnectionDetails);
            else if (wfmatrix.getNextDesignation() != null
                    && !workFlowAction.equals(WaterTaxConstants.APPROVEWORKFLOWACTION)
                    && (waterConnectionDetails.getStatus().getCode()
                            .equals(WaterTaxConstants.APPLICATION_STATUS_FEEPAID)
                            || waterConnectionDetails.getStatus().getCode()
                                    .equals(WaterTaxConstants.APPLICATION_STATUS_VERIFIED)
                            || waterConnectionDetails.getStatus().getCode()
                                    .equals(WaterTaxConstants.APPLICATION_STATUS_CLOSERDIGSIGNPENDING)
                            || waterConnectionDetails.getStatus().getCode()
                                    .equals(WaterTaxConstants.APPLICATION_STATUS_DIGITALSIGNPENDING)
                            || waterConnectionDetails.getStatus().getCode()
                                    .equals(WaterTaxConstants.APPLICATION_STATUS__RECONNCTIONAPPROVED)
                            || workFlowAction.equals(WFLOW_ACTION_STEP_REJECT) && waterConnectionDetails.getStatus()
                                    .getCode().equals(WaterTaxConstants.WORKFLOW_RECONNCTIONINITIATED)
                            || workFlowAction.equals(WFLOW_ACTION_STEP_REJECT) && waterConnectionDetails.getStatus()
                                    .getCode().equals(WaterTaxConstants.WORKFLOW_RECONNCTIONINITIATED)
                            || workFlowAction.equals(WFLOW_ACTION_STEP_REJECT) && waterConnectionDetails.getStatus()
                                    .getCode().equals(WaterTaxConstants.APPLICATION_STATUS_CLOSERINITIATED)))
                approvalPosition = waterTaxUtils.getApproverPosition(wfmatrix.getNextDesignation(),
                        waterConnectionDetails);
            else if (wfmatrix.getNextDesignation() != null && (!workFlowAction.equals(WFLOW_ACTION_STEP_REJECT)
                    && waterConnectionDetails.getStatus().getCode()
                            .equals(WaterTaxConstants.APPLICATION_STATUS_CLOSERINITIATED)
                    || !waterConnectionDetails.getState().getValue().equals(WaterTaxConstants.WF_STATE_REJECTED)
                            && !workFlowAction.equals(WFLOW_ACTION_STEP_REJECT)
                            && (waterConnectionDetails.getStatus().getCode()
                                    .equals(WaterTaxConstants.WORKFLOW_RECONNCTIONINITIATED)
                                    || waterConnectionDetails.getStatus().getCode()
                                            .equals(WaterTaxConstants.APPLICATION_STATUS__RECONNCTIONINPROGRESS))
                    || waterConnectionDetails.getStatus().getCode()
                            .equals(WaterTaxConstants.APPLICATION_STATUS_APPROVED))) {
                final Position posobj = waterTaxUtils.getCityLevelCommissionerPosition(wfmatrix.getNextDesignation(),
                        waterConnectionDetails.getConnection().getPropertyIdentifier());
                if (posobj != null)
                    approvalPosition = posobj.getId();
            }
        if (workFlowAction.equals(WaterTaxConstants.SIGNWORKFLOWACTION))
            approvalPosition = waterTaxUtils.getApproverPosition(WaterTaxConstants.JUNIOR_OR_SENIOR_ASSISTANT_DESIGN,
                    waterConnectionDetails);
        if (workFlowAction.equals(WaterTaxConstants.APPROVEWORKFLOWACTION)
                && (waterConnectionDetails.getStatus().getCode().equals(WaterTaxConstants.APPLICATION_STATUS_FEEPAID)
                        || waterConnectionDetails.getStatus().getCode()
                                .equals(WaterTaxConstants.APPLICATION_STATUS_CLOSERINPROGRESS)
                        || waterConnectionDetails
                                .getStatus().getCode()
                                .equals(WaterTaxConstants.APPLICATION_STATUS__RECONNCTIONINPROGRESS)
                        || waterConnectionDetails.getStatus().getCode()
                                .equals(WaterTaxConstants.APPLICATION_STATUS_APPROVED)))
            approvalPosition = waterConnectionDetails.getState().getOwnerPosition().getId();

        return approvalPosition;
    }

    public void updateConsumerIndex(final WaterConnectionDetails waterConnectionDetails) {
        final AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
                waterConnectionDetails.getConnection().getPropertyIdentifier(),
                PropertyExternalService.FLAG_FULL_DETAILS, BasicPropertyStatus.ALL);
        final BigDecimal amountTodisplayInIndex = getTotalAmount(waterConnectionDetails);
        if (waterConnectionDetails.getLegacy())
            createWaterChargeIndex(waterConnectionDetails, assessmentDetails, amountTodisplayInIndex);
    }

    public WaterChargeDocument createWaterChargeIndex(final WaterConnectionDetails waterConnectionDetails,
            final AssessmentDetails assessmentDetails, final BigDecimal amountTodisplayInIndex) {
        return waterChargeIndexService.createWaterChargeIndex(waterConnectionDetails, assessmentDetails,
                amountTodisplayInIndex);

    }

    public void updateIndexes(final WaterConnectionDetails waterConnectionDetails, final String sourceChannel) {

        final AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
                waterConnectionDetails.getConnection().getPropertyIdentifier(),
                PropertyExternalService.FLAG_FULL_DETAILS, BasicPropertyStatus.ALL);
        if (LOG.isDebugEnabled())
            LOG.debug(" updating Indexes Started... ");
        BigDecimal amountTodisplayInIndex = BigDecimal.ZERO;
        if (waterConnectionDetails.getConnection().getConsumerCode() != null)
            amountTodisplayInIndex = getTotalAmountTillCurrentFinYear(waterConnectionDetails);
        if (waterConnectionDetails.getLegacy() && (null == waterConnectionDetails.getId()
                || null != waterConnectionDetails.getId() && waterConnectionDetails.getStatus().getCode()
                        .equals(WaterTaxConstants.APPLICATION_STATUS_SANCTIONED))) {
            createWaterChargeIndex(waterConnectionDetails, assessmentDetails, amountTodisplayInIndex);
            return;
        }
        Iterator<OwnerName> ownerNameItr = null;
        if (null != assessmentDetails.getOwnerNames())
            ownerNameItr = assessmentDetails.getOwnerNames().iterator();
        final StringBuilder consumerName = new StringBuilder();
        final StringBuilder mobileNumber = new StringBuilder();
        Assignment assignment;
        User user = null;
        final StringBuilder aadharNumber = new StringBuilder();
        if (null != ownerNameItr && ownerNameItr.hasNext()) {
            final OwnerName primaryOwner = ownerNameItr.next();
            consumerName.append(primaryOwner.getOwnerName() != null ? primaryOwner.getOwnerName() : "");
            mobileNumber.append(primaryOwner.getMobileNumber() != null ? primaryOwner.getMobileNumber() : "");
            aadharNumber.append(primaryOwner.getAadhaarNumber() != null ? primaryOwner.getAadhaarNumber() : "");
            while (ownerNameItr.hasNext()) {
                final OwnerName secondaryOwner = ownerNameItr.next();
                consumerName.append(',')
                        .append(secondaryOwner.getOwnerName() != null ? secondaryOwner.getOwnerName() : "");
                mobileNumber.append(',')
                        .append(secondaryOwner.getMobileNumber() != null ? secondaryOwner.getMobileNumber() : "");
                aadharNumber.append(',')
                        .append(secondaryOwner.getAadhaarNumber() != null ? secondaryOwner.getAadhaarNumber() : "");
            }

        }
        List<Assignment> asignList = null;
        if (waterConnectionDetails.getState() != null && waterConnectionDetails.getState().getOwnerPosition() != null) {
            assignment = assignmentService.getPrimaryAssignmentForPositionAndDate(
                    waterConnectionDetails.getState().getOwnerPosition().getId(), new Date());
            if (assignment != null) {
                asignList = new ArrayList<>();
                asignList.add(assignment);
            } else if (assignment == null)
                asignList = assignmentService.getAssignmentsForPosition(
                        waterConnectionDetails.getState().getOwnerPosition().getId(), new Date());
            if (!asignList.isEmpty())
                user = userService.getUserById(asignList.get(0).getEmployee().getId());
        } else
            user = securityUtils.getCurrentUser();
        ApplicationIndex applicationIndex = applicationIndexService
                .findByApplicationNumber(waterConnectionDetails.getApplicationNumber());
        if (applicationIndex != null
                && waterConnectionDetails.getStatus().getCode().equals(WaterTaxConstants.APPLICATION_STATUS_CREATED)) {
            applicationIndex.setOwnerName(user.getUsername() + "::" + user.getName());
            applicationIndexService.updateApplicationIndex(applicationIndex);
        }
        if (applicationIndex != null && null != waterConnectionDetails.getId()
                && waterConnectionDetails.getStatus() != null
                && !waterConnectionDetails.getStatus().getCode().equals(WaterTaxConstants.APPLICATION_STATUS_CREATED)) {
            if (waterConnectionDetails.getStatus() != null && !waterConnectionDetails.getStatus().getCode()
                    .equals(WaterTaxConstants.APPLICATION_STATUS__RECONNCTIONAPPROVED)
                    && !waterConnectionDetails.getStatus().getCode()
                            .equals(WaterTaxConstants.APPLICATION_STATUS__RECONNCTIONSANCTIONED)
                    && !waterConnectionDetails.getStatus().getCode()
                            .equals(WaterTaxConstants.APPLICATION_STATUS_SANCTIONED)) {
                applicationIndex.setApplicantAddress(assessmentDetails.getPropertyAddress());
                applicationIndex.setApproved(ApprovalStatus.INPROGRESS);
                applicationIndex.setClosed(ClosureStatus.NO);
                applicationIndex.setStatus(waterConnectionDetails.getStatus().getDescription());
                if (!waterConnectionDetails.getStatus().getCode().equals(WaterTaxConstants.APPLICATION_STATUS_SANCTIONED))
                    applicationIndex.setOwnerName(user.getUsername() + "::" + user.getName());
                applicationIndex.setSla(applicationProcessTimeService.getApplicationProcessTime(
                        waterConnectionDetails.getApplicationType(), waterConnectionDetails.getCategory()));
                if (waterTaxUtils.isCSCoperator(waterConnectionDetails.getCreatedBy())
                        && UserType.BUSINESS.equals(waterConnectionDetails.getCreatedBy().getType()))
                    applicationIndex.setChannel(Source.CSC.toString());
                else if (waterTaxUtils.isCitizenPortalUser(waterConnectionDetails.getCreatedBy()))
                    applicationIndex.setChannel(Source.CITIZENPORTAL.toString());
                else if (sourceChannel == null)
                    applicationIndex.setChannel(WaterTaxConstants.SYSTEM);
                else
                    applicationIndex.setChannel(sourceChannel);
            }
            if (waterConnectionDetails.getStatus().getCode()
                    .equals(WaterTaxConstants.APPLICATION_STATUS__RECONNCTIONSANCTIONED)
                    || waterConnectionDetails.getStatus().getCode()
                            .equals(WaterTaxConstants.APPLICATION_STATUS_SANCTIONED)
                    || waterConnectionDetails.getStatus().getCode()
                            .equals(WaterTaxConstants.APPLICATION_STATUS_CLOSERSANCTIONED)) {
                applicationIndex.setStatus(waterConnectionDetails.getStatus().getDescription());
                applicationIndex.setApproved(ApprovalStatus.APPROVED);
                applicationIndex.setClosed(ClosureStatus.YES);
            }
            if (waterConnectionDetails.getStatus().getCode()
                    .equals(WaterTaxConstants.APPLICATION_STATUS_CANCELLED)) {
                applicationIndex.setApproved(ApprovalStatus.REJECTED);
                applicationIndex.setClosed(ClosureStatus.YES);
            }
            if (waterConnectionDetails.getConnection().getConsumerCode() != null)
                applicationIndex.setConsumerCode(waterConnectionDetails.getConnection().getConsumerCode());
            applicationIndexService.updateApplicationIndex(applicationIndex);

            // Creating Consumer Index only on Sanction
            if (waterConnectionDetails.getStatus().getCode().equals(WaterTaxConstants.APPLICATION_STATUS_SANCTIONED))
                if (waterConnectionDetails.getConnectionStatus().equals(ConnectionStatus.INPROGRESS)
                        && !waterConnectionDetails.getApplicationType().getCode()
                                .equalsIgnoreCase(WaterTaxConstants.CHANGEOFUSE)) {
                    waterConnectionDetails.setConnectionStatus(ConnectionStatus.ACTIVE);
                    if (LOG.isDebugEnabled())
                        LOG.debug(" updating Consumer Index Started... ");
                    if (!waterConnectionDetails.getConnectionStatus().equals(ConnectionStatus.INACTIVE)
                            || !waterConnectionDetails.getConnectionStatus().equals(ConnectionStatus.INPROGRESS))
                        createWaterChargeIndex(waterConnectionDetails, assessmentDetails, amountTodisplayInIndex);
                    if (LOG.isDebugEnabled())
                        LOG.debug(" updating Consumer Index completed... ");
                }
            // To Update After ClosureConnection is rejected
            if (waterConnectionDetails.getStatus().getCode().equals(WaterTaxConstants.APPLICATION_STATUS_SANCTIONED)
                    && waterConnectionDetails.getConnectionStatus().equals(ConnectionStatus.ACTIVE))
                createWaterChargeIndex(waterConnectionDetails, assessmentDetails, amountTodisplayInIndex);
            if (waterConnectionDetails.getStatus().getCode()
                    .equals(WaterTaxConstants.APPLICATION_STATUS_CLOSERSANCTIONED)
                    || waterConnectionDetails.getStatus().getCode()
                            .equals(WaterTaxConstants.APPLICATION_STATUS_CLOSERAPRROVED)
                            && waterConnectionDetails.getConnectionStatus().equals(ConnectionStatus.CLOSED))
                createWaterChargeIndex(waterConnectionDetails, assessmentDetails, amountTodisplayInIndex);

            if (waterConnectionDetails.getCloseConnectionType() != null
                    && waterConnectionDetails.getCloseConnectionType().equals(WaterTaxConstants.TEMPERARYCLOSECODE)
                    && (waterConnectionDetails.getStatus().getCode()
                            .equals(WaterTaxConstants.APPLICATION_STATUS__RECONNCTIONAPPROVED)
                            || waterConnectionDetails.getStatus().getCode()
                                    .equals(WaterTaxConstants.APPLICATION_STATUS__RECONNCTIONSANCTIONED))) {
                waterConnectionDetails.setConnectionStatus(ConnectionStatus.ACTIVE);
                createWaterChargeIndex(waterConnectionDetails, assessmentDetails, amountTodisplayInIndex);
            }
        } else {
            final Integer appProcessTime = applicationProcessTimeService.getApplicationProcessTime(
                    waterConnectionDetails.getApplicationType(),
                    waterConnectionDetails.getCategory());
            final String strQuery = "select md from EgModules md where md.name=:name";
            final Query hql = getCurrentSession().createQuery(strQuery);
            hql.setParameter("name", WaterTaxConstants.EGMODULES_NAME);
            if (waterConnectionDetails.getApplicationDate() == null)
                waterConnectionDetails.setApplicationDate(new DateTime().toDate());
            if (waterConnectionDetails.getApplicationNumber() == null)
                waterConnectionDetails.setApplicationNumber(waterConnectionDetails.getConnection().getConsumerCode());
            if (applicationIndex == null) {
                if (LOG.isDebugEnabled())
                    LOG.debug(" updating Application Index creation Started... ");
                String channel;
                if (waterTaxUtils.isCSCoperator(waterConnectionDetails.getCreatedBy())
                        && UserType.BUSINESS.equals(waterConnectionDetails.getCreatedBy().getType()))
                    channel = Source.CSC.toString();
                else
                    channel = WaterTaxConstants.SYSTEM;
                applicationIndex = ApplicationIndex.builder().withModuleName(((EgModules) hql.uniqueResult()).getName())
                        .withApplicationNumber(waterConnectionDetails.getApplicationNumber())
                        .withApplicationDate(new DateTime(waterConnectionDetails.getApplicationDate()).toDate())
                        .withApplicationType(waterConnectionDetails.getApplicationType().getName())
                        .withApplicantName(consumerName.toString())
                        .withStatus(waterConnectionDetails.getStatus().getDescription())
                        .withUrl(String.format(WTMS_APPLICATION_VIEW, waterConnectionDetails.getApplicationNumber()))
                        .withApplicantAddress(assessmentDetails.getPropertyAddress())
                        .withOwnername(user.getUsername() + "::" + user.getName())
                        .withChannel(sourceChannel != null ? sourceChannel : channel)
                        .withMobileNumber(mobileNumber.toString()).withClosed(ClosureStatus.NO)
                        .withAadharNumber(aadharNumber.toString()).withApproved(ApprovalStatus.INPROGRESS)
                        .withSla(appProcessTime != null ? appProcessTime : 0).build();
                if (!waterConnectionDetails.getLegacy() && !waterConnectionDetails.getStatus().getCode()
                        .equals(WaterTaxConstants.APPLICATION_STATUS_SANCTIONED))
                    applicationIndexService.createApplicationIndex(applicationIndex);
            }
            if (LOG.isDebugEnabled())
                LOG.debug(" updating Application Index creation complted... ");
        }
    }

    public Date getDisposalDate(final WaterConnectionDetails waterConnectionDetails, final Integer appProcessTime) {
        final Calendar c = Calendar.getInstance();
        c.setTime(waterConnectionDetails.getApplicationDate());
        c.add(Calendar.DATE, appProcessTime);
        return c.getTime();
    }

    public WaterConnectionDetails getParentConnectionDetails(final String propertyIdentifier,
            final ConnectionStatus connectionStatus) {
        return waterConnectionDetailsRepository
                .findByConnection_PropertyIdentifierAndConnectionStatusAndConnection_ParentConnectionIsNull(
                        propertyIdentifier, connectionStatus);
    }

    public WaterConnectionDetails getParentConnectionDetailsForParentConnectionNotNull(final String consumercode,
            final ConnectionStatus connectionStatus) {
        return waterConnectionDetailsRepository
                .findByConnection_ConsumerCodeAndConnectionStatusAndConnection_ParentConnectionIsNotNull(consumercode,
                        connectionStatus);
    }

    public WaterConnectionDetails getWaterConnectionDetailsByDemand(final EgDemand demand) {
        return waterConnectionDetailsRepository.findByDemand(demand);
    }

    @Transactional
    public void save(final WaterConnectionDetails detail) {
        waterConnectionDetailsRepository.save(detail);
    }

    public WaterConnectionDetails getActiveNonHistoryConnectionDetailsByConnection(
            final WaterConnection waterConnection) {
        return waterConnectionDetailsRepository.findByConnectionAndConnectionStatusAndIsHistory(waterConnection,
                ConnectionStatus.ACTIVE, Boolean.FALSE);
    }

    public BigDecimal getTotalAmount(final WaterConnectionDetails waterConnectionDetails) {
        final EgDemand currentDemand = waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand();
        BigDecimal balance = BigDecimal.ZERO;
        if (currentDemand != null) {
            final List<Object> instVsAmt = connectionDemandService.getDmdCollAmtInstallmentWise(currentDemand);
            for (final Object object : instVsAmt) {
                final Object[] ddObject = (Object[]) object;
                final BigDecimal dmdAmt = new BigDecimal((Double) ddObject[2]);
                BigDecimal collAmt = BigDecimal.ZERO;
                if (ddObject[2] != null)
                    collAmt = new BigDecimal((Double) ddObject[3]);
                balance = balance.add(dmdAmt.subtract(collAmt));
            }
        }
        return balance;
    }

    public BigDecimal getTotalAmountTillCurrentFinYear(final WaterConnectionDetails waterConnectionDetails) {
        final EgDemand currentDemand = waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand();
        BigDecimal balance = BigDecimal.ZERO;
        if (currentDemand != null) {
            final List<Object> instVsAmt = connectionDemandService
                    .getDmdCollAmtInstallmentWiseUptoCurrentFinYear(currentDemand, waterConnectionDetails);
            for (final Object object : instVsAmt) {
                final Object[] ddObject = (Object[]) object;
                final BigDecimal dmdAmt = new BigDecimal((Double) ddObject[2]);
                BigDecimal collAmt = BigDecimal.ZERO;
                if (ddObject[2] != null)
                    collAmt = new BigDecimal((Double) ddObject[3]);
                balance = balance.add(dmdAmt.subtract(collAmt));
            }
        }
        if (balance.compareTo(BigDecimal.ZERO) < 0)
            balance = BigDecimal.ZERO;
        return balance;
    }

    public BigDecimal getCurrentDue(final WaterConnectionDetails waterConnectionDetails) {
        final EgDemand currentDemand = waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand();
        BigDecimal balance = BigDecimal.ZERO;
        if (currentDemand != null) {
            final List<Object> instVsAmt = connectionDemandService
                    .getDmdCollAmtInstallmentWiseUptoCurrentInstallmemt(currentDemand, waterConnectionDetails);
            for (final Object object : instVsAmt) {
                final Object[] ddObject = (Object[]) object;
                final BigDecimal dmdAmt = new BigDecimal((Double) ddObject[2]);
                BigDecimal collAmt = BigDecimal.ZERO;
                if (ddObject[2] != null)
                    collAmt = new BigDecimal((Double) ddObject[3]);
                balance = balance.add(dmdAmt.subtract(collAmt));
            }
        }
        return balance;
    }

    @Transactional(readOnly = true)
    public List<ApplicationDocuments> getApplicationDocForExceptClosureAndReConnection(
            final WaterConnectionDetails waterConnectionDetails) {
        final List<ApplicationDocuments> tempDocList = new ArrayList<>(0);
        if (waterConnectionDetails != null)
            for (final ApplicationDocuments appDoc : waterConnectionDetails.getApplicationDocs())
                if (appDoc.getDocumentNames() != null && (appDoc.getDocumentNames().getApplicationType().getCode()
                        .equals(WaterTaxConstants.NEWCONNECTION)
                        || appDoc.getDocumentNames().getApplicationType().getCode()
                                .equals(WaterTaxConstants.ADDNLCONNECTION)
                        || appDoc.getDocumentNames().getApplicationType().getCode()
                                .equals(WaterTaxConstants.CHANGEOFUSE)))
                    tempDocList.add(appDoc);
        return tempDocList;
    }

    public WaterConnectionDetails createNewWaterConnection(final WaterConnectionDetails waterConnectionDetails,
            final Long approvalPosition, final String approvalComent, final String code, final String workFlowAction,
            final HashMap<String, String> meesevaParams, final String sourceChannel) {
        return createNewWaterConnection(waterConnectionDetails, approvalPosition, approvalComent, code, workFlowAction,
                sourceChannel);

    }

    public void validateWaterRateAndDonationHeader(final WaterConnectionDetails waterConnectionDetails) {
        final DonationDetails donationDetails = connectionDemandService.getDonationDetails(waterConnectionDetails);
        if (donationDetails == null)
            throw new ValidationException("donation.combination.required");
        if (waterConnectionDetails.getConnectionType().equals(ConnectionType.NON_METERED)) {
            final WaterRatesDetails waterRatesDetails = connectionDemandService
                    .getWaterRatesDetailsForDemandUpdate(waterConnectionDetails);
            if (waterRatesDetails == null)
                throw new ValidationException("err.water.rate.not.found");
        }
    }

    public String getApprovalPositionOnValidate(final Long approvalPositionId) {
        Assignment assignmentObj;
        final List<Assignment> assignmentList = new ArrayList<>();
        if (approvalPositionId != null && approvalPositionId != 0 && approvalPositionId != -1) {
            assignmentObj = assignmentService.getPrimaryAssignmentForPositionAndDate(approvalPositionId, new Date());
            if (assignmentObj == null)
                throw new ValidationException("err.user.not.defined");
            assignmentList.add(assignmentObj);

            final Gson jsonCreator = new GsonBuilder().registerTypeAdapter(Assignment.class, new AssignmentAdaptor())
                    .create();
            return jsonCreator.toJson(assignmentList, new TypeToken<Collection<Assignment>>() {
            }.getType());
        }
        return "[]";
    }

    @Transactional
    public WaterConnectionDetails updateWaterConnectionDetailsWithFileStore(
            final WaterConnectionDetails waterConnectionDetails) {
        return entityManager.merge(waterConnectionDetails);
    }

    public Map<String, String> getNonMeteredConnectionTypesMap() {
        final Map<String, String> connectionTypeMap = new LinkedHashMap<>(0);
        connectionTypeMap.put(ConnectionType.NON_METERED.toString(), WaterTaxConstants.NON_METERED);
        return connectionTypeMap;
    }

    public BigDecimal getTotalAmountTillPreviousFinYear(final WaterConnectionDetails waterConnectionDetails) {
        final EgDemand currentDemand = waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand();
        BigDecimal balance = BigDecimal.ZERO;
        if (currentDemand != null) {
            final List<Object> instVsAmt = connectionDemandService
                    .getDmdCollAmtInstallmentWiseUptoPreviousFinYear(currentDemand, waterConnectionDetails);
            for (final Object object : instVsAmt) {
                final Object[] ddObject = (Object[]) object;
                final BigDecimal dmdAmt = new BigDecimal((Double) ddObject[2]);
                BigDecimal collAmt = BigDecimal.ZERO;
                if (ddObject[2] != null)
                    collAmt = new BigDecimal((Double) ddObject[3]);
                balance = balance.add(dmdAmt.subtract(collAmt));
            }
        }
        if (balance.compareTo(BigDecimal.ZERO) < 0)
            balance = BigDecimal.ZERO;
        return balance;
    }

    public BigDecimal getArrearsDemand(final WaterConnectionDetails waterConnectionDetails) {
        final EgDemand currentDemand = waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand();
        BigDecimal balance = BigDecimal.ZERO;
        if (currentDemand != null) {
            final List<Object> instVsAmt = connectionDemandService
                    .getDmdCollAmtInstallmentWiseUptoPreviousFinYear(currentDemand, waterConnectionDetails);
            balance = getTotalBalance(instVsAmt);
        }
        if (balance.compareTo(BigDecimal.ZERO) < 0)
            balance = BigDecimal.ZERO;
        return balance;
    }

    public BigDecimal getTotalDemandTillCurrentFinYear(final WaterConnectionDetails waterConnectionDetails) {
        final EgDemand currentDemand = waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand();
        BigDecimal balance = BigDecimal.ZERO;
        if (currentDemand != null) {
            final List<Object> instVsAmt = connectionDemandService
                    .getDmdCollAmtInstallmentWiseUptoCurrentFinYear(currentDemand, waterConnectionDetails);
            balance = getTotalBalance(instVsAmt);
        }
        if (balance.compareTo(BigDecimal.ZERO) < 0)
            balance = BigDecimal.ZERO;
        return balance;
    }

    public BigDecimal getTotalBalance(final List<Object> instVsAmt) {
        BigDecimal balance = BigDecimal.ZERO;
        for (final Object object : instVsAmt) {
            final Object[] ddObject = (Object[]) object;
            if (ddObject[2] != null) {
                final BigDecimal dmdAmt = new BigDecimal((Double) ddObject[2]);
                balance = balance.add(dmdAmt);
            }
        }
        return balance;
    }

    @Transactional
    public void saveAndFlushWaterConnectionDetail(final WaterConnectionDetails waterConnectionDetails) {
        waterConnectionDetailsRepository.saveAndFlush(waterConnectionDetails);
    }

    public List<WaterConnectionDetails> getAllConnectionDetailsByPropertyID(final String propertyId) {
        return waterConnectionDetailsRepository.getAllConnectionDetailsByPropertyID(propertyId);
    }

    /**
     * Method to push data for citizen portal inbox
     */

    @Transactional
    public void pushPortalMessage(final WaterConnectionDetails waterConnectionDetails) {
        final Module module = moduleDao.getModuleByName(WaterTaxConstants.MODULE_NAME);
        final WaterConnection waterConnection = waterConnectionDetails.getConnection();
        final PortalInboxBuilder portalInboxBuilder = new PortalInboxBuilder(module,
                waterConnectionDetails.getState().getNatureOfTask() + " : " + module.getDisplayName(),
                waterConnectionDetails.getApplicationNumber(), waterConnection.getConsumerCode(), waterConnection.getId(),
                waterConnectionDetails.getConnectionReason(), getDetailedMessage(waterConnectionDetails),
                String.format(WTMS_APPLICATION_VIEW, waterConnectionDetails.getApplicationNumber()),
                isResolved(waterConnectionDetails), waterConnectionDetails.getStatus().getDescription(),
                getSlaEndDate(waterConnectionDetails), waterConnectionDetails.getState(),
                Arrays.asList(securityUtils.getCurrentUser()));
        final PortalInbox portalInbox = portalInboxBuilder.build();
        portalInboxService.pushInboxMessage(portalInbox);
    }

    private boolean isResolved(final WaterConnectionDetails waterConnectionDetails) {
        return "END".equalsIgnoreCase(waterConnectionDetails.getState().getValue())
                || "CLOSED".equalsIgnoreCase(waterConnectionDetails.getState().getValue());
    }

    private Date getSlaEndDate(final WaterConnectionDetails waterConnectionDetails) {
        final Integer appProcessTime = applicationProcessTimeService.getApplicationProcessTime(
                waterConnectionDetails.getApplicationType(),
                waterConnectionDetails.getCategory());
        final DateTime dt = new DateTime(new Date());
        return dt.plusDays(appProcessTime).toDate();
    }

    private String getDetailedMessage(final WaterConnectionDetails waterConnectionDetails) {
        final Module module = moduleDao.getModuleByName(WaterTaxConstants.MODULE_NAME);
        final StringBuilder detailedMessage = new StringBuilder();
        if (waterConnectionDetails.getApplicationType() != null)
            detailedMessage.append(APPLICATION_NO).append(waterConnectionDetails.getApplicationNumber()).append(REGARDING)
                    .append(waterConnectionDetails.getState().getNatureOfTask() + " " + module.getDisplayName()).append(" in ")
                    .append(waterConnectionDetails.getStatus().getDescription()).append(STATUS);
        return detailedMessage.toString();
    }

    public PortalInbox getPortalInbox(final String applicationNumber) {
        final Module module = moduleDao.getModuleByName(WaterTaxConstants.MODULE_NAME);
        return portalInboxService.getPortalInboxByApplicationNo(applicationNumber, module.getId());
    }

    /**
     * Method to update data for citizen portal inbox
     */
    @Transactional
    public void updatePortalMessage(final WaterConnectionDetails waterConnectionDetails) {
        final Module module = moduleDao.getModuleByName(WaterTaxConstants.MODULE_NAME);
        final WaterConnection waterConnection = waterConnectionDetails.getConnection();
        portalInboxService.updateInboxMessage(waterConnectionDetails.getApplicationNumber(), module.getId(),
                waterConnectionDetails.getState().getValue(),
                isResolved(waterConnectionDetails), getSlaEndDate(waterConnectionDetails), waterConnectionDetails.getState(),
                null,
                waterConnection.getConsumerCode(),
                String.format(WTMS_APPLICATION_VIEW, waterConnectionDetails.getApplicationNumber()));
    }
}