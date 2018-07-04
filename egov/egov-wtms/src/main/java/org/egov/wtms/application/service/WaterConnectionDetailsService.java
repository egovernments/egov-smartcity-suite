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
package org.egov.wtms.application.service;

import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_UP;
import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.egov.commons.entity.Source.CSC;
import static org.egov.commons.entity.Source.MEESEVA;
import static org.egov.commons.entity.Source.SURVEY;
import static org.egov.infra.config.core.ApplicationThreadLocals.setUserId;
import static org.egov.infra.utils.DateUtils.noOfMonthsBetween;
import static org.egov.wtms.masters.entity.enums.ConnectionStatus.ACTIVE;
import static org.egov.wtms.masters.entity.enums.ConnectionStatus.CLOSED;
import static org.egov.wtms.masters.entity.enums.ConnectionStatus.INACTIVE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.ADDNLCONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATIONSTATUSCLOSED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_APPROVED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_CANCELLED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_CLOSERAPRROVED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_CLOSERDIGSIGNPENDING;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_CLOSERINITIATED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_CLOSERINPROGRESS;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_CLOSERSANCTIONED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_CREATED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_DIGITALSIGNPENDING;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_ESTIMATENOTICEGEN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_FEEPAID;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_NEW;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_RECONNCTIONAPPROVED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_RECONNCTIONINPROGRESS;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_RECONNCTIONSANCTIONED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_RECONNDIGSIGNPENDING;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_SANCTIONED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_VERIFIED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_WOGENERATED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPROVEWORKFLOWACTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.ASSISTANT_ENGINEER_DESIGN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.ASSISTANT_EXECUTIVE_ENGINEER_DESIGN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.CATEGORY_BPL;
import static org.egov.wtms.utils.constants.WaterTaxConstants.CHANGEOFUSE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.CLOSINGCONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.COMMISSIONER_DESGN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.CONNECTIONTYPE_METERED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.DEPUTY_ENGINEER_DESIGN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.EGMODULES_NAME;
import static org.egov.wtms.utils.constants.WaterTaxConstants.EXECUTIVE_ENGINEER_DESIGN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.FILESTORE_MODULECODE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.FORWARDWORKFLOWACTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.JUNIOR_OR_SENIOR_ASSISTANT_DESIGN_REVENUE_CLERK;
import static org.egov.wtms.utils.constants.WaterTaxConstants.METERED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.MODULETYPE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.MODULE_NAME;
import static org.egov.wtms.utils.constants.WaterTaxConstants.MUNICIPAL_ENGINEER_DESIGN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.NEWCONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.NON_METERED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.NON_METERED_CODE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.PTIS_DETAILS_URL;
import static org.egov.wtms.utils.constants.WaterTaxConstants.RECONNECTIONCONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.REGULARIZE_CONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SIGNED_DOCUMENT_PREFIX;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SIGNWORKFLOWACTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.STATUS;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SUPERIENTEND_ENGINEER_DESIGN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SUPERINTENDING_ENGINEER_DESIGNATION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SYSTEM;
import static org.egov.wtms.utils.constants.WaterTaxConstants.TAP_INSPPECTOR_DESIGN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.TEMPERARYCLOSECODE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.USERNAME_MEESEVA;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WATER_RATES_NONMETERED_PTMODULE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WFLOW_ACTION_STEP_CANCEL;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WFLOW_ACTION_STEP_REJECT;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WF_PREVIEW_BUTTON;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WF_STATE_AE_APPROVAL_PENDING;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WF_STATE_AE_REJECTION_PENDING;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WF_STATE_BUTTON_GENERATEESTIMATE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WF_STATE_DEE_APPROVE_PENDING;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WF_STATE_DEE_FORWARD_PENDING;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WF_STATE_EE_APPROVE_PENDING;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WF_STATE_EE_FORWARD_PENDING;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WF_STATE_ME_APPROVE_PENDING;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WF_STATE_ME_FORWARD_PENDING;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WF_STATE_REJECTED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WF_STATE_SE_APPROVE_PENDING;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WF_STATE_SE_FORWARD_PENDING;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WF_STATE_TAP_EXECUTION_DATE_BUTTON;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WORKFLOW_CLOSUREADDITIONALRULE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WORKFLOW_RECONNCTIONINITIATED;

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
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import org.egov.commons.EgModules;
import org.egov.commons.Installment;
import org.egov.commons.dao.FinancialYearDAO;
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
import org.egov.infra.utils.DateUtils;
import org.egov.infra.web.utils.WebUtils;
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
import org.egov.stms.masters.entity.SewerageApplicationType;
import org.egov.stms.masters.entity.enums.PropertyType;
import org.egov.stms.masters.entity.enums.SewerageConnectionStatus;
import org.egov.stms.masters.service.SewerageApplicationTypeService;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.transactions.entity.SewerageConnection;
import org.egov.stms.utils.SewerageTaxUtils;
import org.egov.stms.utils.constants.SewerageTaxConstants;
import org.egov.wtms.application.entity.ApplicationDocuments;
import org.egov.wtms.application.entity.WaterConnExecutionDetails;
import org.egov.wtms.application.entity.WaterConnection;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.entity.WaterConnectionExecutionResponse;
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
import org.egov.wtms.masters.service.MeterCostService;
import org.egov.wtms.service.es.WaterChargeDocumentService;
import org.egov.wtms.utils.PropertyExtnUtils;
import org.egov.wtms.utils.WaterTaxNumberGenerator;
import org.egov.wtms.utils.WaterTaxUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jfree.util.Log;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


@Service
@Transactional(readOnly = true)
public class WaterConnectionDetailsService {

    private static final String WTMS_APPLICATION_VIEW = "/wtms/application/view/%s";
    private static final Logger LOG = LoggerFactory.getLogger(WaterConnectionDetailsService.class);
    private static final String APPLICATION_NO = "Application no ";
    private static final String REGARDING = " regarding ";
    private static final String APPROVED = "Approved";
    private static final String EXECUTION_DATE = "executionDate";
    private static final String DATE_VALIDATION_FAILED = "DateValidationFailed";
    private static final String DEPARTMENT = "department";
    private static final String EMPTY_LIST = "EmptyList";
    private static final String UPDATE_FAILED = "UpdateExecutionFailed";
    private static final String SUCCESS = "Success";
    private static final String METER_MAKE = "meterMake";
    private static final String INITIAL_READING = "initialReading";
    private static final String METER_SERIAL_NUMBER = "meterSerialNumber";
    private static final String APPLICATION_NUMBER = "applicationNumber";
    private static final String REQ_METER_MAKER = "MeterMakerRequired";
    private static final String REQ_EXECUTION_DATE = "ExecutionDateRequired";
    private static final String REQ_INITIAL_READING = "InitialReadingRequired";
    private static final String REQ_METER_SERIAL_NUMBER = "MeterSerialNumberRequired";
    private static final String ERR_WATER_RATES_NOT_DEFINED = "WaterRatesNotDefined";

    @Autowired
    protected WaterConnectionDetailsRepository waterConnectionDetailsRepository;

    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;

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
    private ModuleService moduleDao;

    @Autowired
    private PortalInboxService portalInboxService;

    @Autowired
    private MeterCostService meterCostService;

    @Autowired
    private FinancialYearDAO financialYearDAO;

    @Autowired
    private SewerageTaxUtils sewerageTaxUtils;

    @Autowired
    private SewerageApplicationTypeService sewerageApplicationTypeService;

    @Autowired
    private ConnectionAddressService connectionAddressService;
    
    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource wcmsMessageSource;

    public WaterConnectionDetails findBy(Long waterConnectionId) {
        return waterConnectionDetailsRepository.findOne(waterConnectionId);
    }

    public List<WaterConnectionDetails> findAll() {
        return waterConnectionDetailsRepository.findAll(new Sort(Sort.Direction.ASC, APPLICATION_NUMBER));
    }

    public WaterConnectionDetails findByApplicationNumber(String applicationNumber) {
        return waterConnectionDetailsRepository.findByApplicationNumber(applicationNumber);
    }

    public WaterConnectionDetails load(Long id) {
        return waterConnectionDetailsRepository.getOne(id);
    }

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Transactional
    public WaterConnectionDetails createNewWaterConnection(WaterConnectionDetails waterConnectionDetails,
                                                           Long approvalPosition, String approvalComent, String additionalRule,
                                                           String workFlowAction, String sourceChannel) {
        if (isBlank(waterConnectionDetails.getApplicationNumber()))
            waterConnectionDetails.setApplicationNumber(applicationNumberGenerator.generate());
        waterConnectionDetails.setApplicationDate(new Date());
        Integer appProcessTime = applicationProcessTimeService.getApplicationProcessTime(
                waterConnectionDetails.getApplicationType(), waterConnectionDetails.getCategory());

        if (appProcessTime != null)
            waterConnectionDetails.setDisposalDate(getDisposalDate(waterConnectionDetails, appProcessTime));

        WaterConnectionDetails savedWaterConnectionDetails = waterConnectionDetailsRepository.save(waterConnectionDetails);
        connectionAddressService.createConnectionAddress(savedWaterConnectionDetails);
        User meesevaUser = userService.getUserById(waterConnectionDetails.getCreatedBy().getId());
        if (meesevaUser.getUsername().equals(USERNAME_MEESEVA)) {
            setUserId(meesevaUser.getId());
            savedWaterConnectionDetails.setCreatedBy(meesevaUser);
        }
        if (LOG.isDebugEnabled())
            LOG.debug(" persisting WaterConnectionDetail object is completed and WorkFlow API Stared ");
        ApplicationWorkflowCustomDefaultImpl applicationWorkflow = getInitialisedWorkFlowBean();
        if (LOG.isDebugEnabled())
            LOG.debug("applicationWorkflowCustomDefaultImpl initialization is done");

        applicationWorkflow.createCommonWorkflowTransition(savedWaterConnectionDetails,
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
    public WaterConnectionDetails createExisting(WaterConnectionDetails waterConnectionDetails) {

        if (waterConnectionDetails.getConnection() != null && waterConnectionDetails.getConnection().getConsumerCode() == null)
            waterConnectionDetails.getConnection().setConsumerCode(waterTaxNumberGenerator.getNextConsumerNumber());

        waterConnectionDetails.getExistingConnection().setWaterConnectionDetails(waterConnectionDetails);
        waterConnectionDetails.setApplicationNumber(waterConnectionDetails.getConnection().getConsumerCode());
        waterConnectionDetails.setApplicationDate(waterConnectionDetails.getExecutionDate());
        waterConnectionDetails.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_SANCTIONED, MODULETYPE));
        if (waterConnectionDetails.getApplicationType().getCode().equalsIgnoreCase(ADDNLCONNECTION)) {
            WaterConnectionDetails primaryConnectionDetails = getPrimaryConnectionDetailsByPropertyIdentifier(
                    waterConnectionDetails.getConnection().getPropertyIdentifier());
            waterConnectionDetails.getConnection().setParentConnection(primaryConnectionDetails.getConnection());
        }
        WaterConnectionDetails savedWaterConnectionDetails = waterConnectionDetailsRepository.save(waterConnectionDetails);
        updateConsumerIndex(savedWaterConnectionDetails);
        return savedWaterConnectionDetails;
    }

    public Map<String, String> getConnectionTypesMap() {
        Map<String, String> connectionTypeMap = new ConcurrentHashMap<>(0);
        connectionTypeMap.put(ConnectionType.METERED.toString(), METERED);
        connectionTypeMap.put(ConnectionType.NON_METERED.toString(), NON_METERED);
        return connectionTypeMap;
    }

    public List<DocumentNames> getAllActiveDocumentNames(ApplicationType applicationType) {
        return documentNamesService.getAllActiveDocumentNamesByApplicationType(applicationType);
    }

    public WaterConnectionDetails findByApplicationNumberOrConsumerCodeAndStatus(String number,
                                                                                 ConnectionStatus connectionStatus) {
        return waterConnectionDetailsRepository
                .findConnectionDetailsByApplicationNumberOrConsumerCodeAndConnectionStatus(number, number, connectionStatus);
    }

    public WaterConnectionDetails findByApplicationNumberOrConsumerCode(String number) {
        return waterConnectionDetailsRepository.findConnectionDetailsByApplicationNumberOrConsumerCode(number, number);
    }

    public WaterConnectionDetails findByConnection(WaterConnection waterConnection) {
        return waterConnectionDetailsRepository.findByConnection(waterConnection);
    }

    public WaterConnectionDetails findByConsumerCodeAndConnectionStatus(String consumerCode,
                                                                        ConnectionStatus connectionStatus) {
        return waterConnectionDetailsRepository.findConnectionDetailsByConsumerCodeAndConnectionStatus(consumerCode,
                connectionStatus);
    }

    public WaterConnectionDetails findParentConnectionDetailsByConsumerCodeAndConnectionStatus(String consumerCode,
                                                                                               ConnectionStatus connectionStatus) {
        return waterConnectionDetailsRepository.findParentConnectionDetailsByConsumerCodeAndConnectionStatus(consumerCode,
                connectionStatus);
    }

    public WaterConnectionDetails findByOldConsumerNumberAndConnectionStatus(String oldConsumerNumber,
                                                                             ConnectionStatus connectionStatus) {
        return waterConnectionDetailsRepository.findByConnectionOldConsumerNumberAndConnectionStatus(oldConsumerNumber,
                connectionStatus);
    }

    public WaterConnectionDetails getActiveConnectionDetailsByConnection(WaterConnection waterConnection) {
        return waterConnectionDetailsRepository.findByConnectionAndConnectionStatus(waterConnection, ACTIVE);
    }

    public WaterConnectionDetails getPrimaryConnectionDetailsByPropertyIdentifier(String propertyIdentifier) {
        return waterConnectionDetailsRepository.getPrimaryConnectionDetailsByPropertyID(propertyIdentifier);
    }

    public WaterConnectionDetails getPrimaryConnectionDetailsByPropertyAssessmentNumbers(List<String> propertyIdentifier) {
        WaterConnectionDetails waterConnectionDetails = null;
        for (String assessmentNumber : propertyIdentifier) {
            waterConnectionDetails = waterConnectionDetailsRepository.getPrimaryConnectionDetailsByPropertyAssessmentNumber(assessmentNumber);
            if (waterConnectionDetails != null)
                break;
        }
        return waterConnectionDetails;
    }

    public List<WaterConnectionDetails> getChildConnectionDetailsByPropertyID(String propertyIdentifier) {
        return waterConnectionDetailsRepository.getChildConnectionDetailsByPropertyID(propertyIdentifier);
    }

    public List<WaterConnectionDetails> getAllConnectionDetailsByParentConnection(Long parentId) {
        return waterConnectionDetailsRepository.getAllConnectionDetailsByParentConnection(parentId);
    }

    public List<WaterConnectionDetails> getAllConnectionDetailsExceptInactiveStatusByPropertyID(String propertyIdentifier) {
        return waterConnectionDetailsRepository.getAllConnectionDetailsExceptInactiveStatusByPropertyID(propertyIdentifier);
    }

    @ReadOnly
    public List<HashMap<String, Object>> getHistory(WaterConnectionDetails waterConnectionDetails) {
        User user;
        List<HashMap<String, Object>> historyTable = new ArrayList<>(0);
        State<Position> state = waterConnectionDetails.getState();
        HashMap<String, Object> map = new HashMap<>(0);
        if (state != null) {
            map.put("date", state.getDateInfo());
            map.put("comments", state.getComments() == null ? EMPTY : state.getComments());
            map.put("updatedBy", state.getLastModifiedBy().getUsername() + "::" + state.getLastModifiedBy().getName());
            map.put(STATUS, state.getValue());
            Position ownerPosition = state.getOwnerPosition();
            user = state.getOwnerUser();
            if (user != null) {
                map.put("user", user.getUsername() + "::" + user.getName());
                map.put(DEPARTMENT, eisCommonService.getDepartmentForUser(user.getId()) == null
                        ? EMPTY : eisCommonService.getDepartmentForUser(user.getId()).getName());
            } else if (ownerPosition != null && ownerPosition.getDeptDesig() != null) {
                user = eisCommonService.getUserForPosition(ownerPosition.getId(), new Date());
                map.put("user", user.getUsername() == null ? EMPTY : user.getUsername() + "::" + user.getName());
                map.put(DEPARTMENT, ownerPosition.getDeptDesig().getDepartment() == null
                        ? EMPTY : ownerPosition.getDeptDesig().getDepartment().getName());
            }
            historyTable.add(map);
            if (!waterConnectionDetails.getStateHistory().isEmpty() && waterConnectionDetails.getStateHistory() != null)
                Collections.reverse(waterConnectionDetails.getStateHistory());
            for (StateHistory<Position> stateHistory : waterConnectionDetails.getStateHistory()) {
                HashMap<String, Object> historyMap = new HashMap<>(0);
                historyMap.put("date", stateHistory.getDateInfo());
                historyMap.put("comments", stateHistory.getComments() == null ? EMPTY : stateHistory.getComments());
                historyMap.put("updatedBy", stateHistory.getLastModifiedBy().getUsername() + "::"
                        + stateHistory.getLastModifiedBy().getName());
                historyMap.put(STATUS, stateHistory.getValue());
                Position owner = stateHistory.getOwnerPosition();
                user = stateHistory.getOwnerUser();
                if (user != null) {
                    historyMap.put("user", user.getUsername() + "::" + user.getName());
                    historyMap.put(DEPARTMENT, eisCommonService.getDepartmentForUser(user.getId()) == null
                            ? EMPTY : eisCommonService.getDepartmentForUser(user.getId()).getName());
                } else if (owner != null && owner.getDeptDesig() != null) {
                    try {
                        user = eisCommonService.getUserForPosition(owner.getId(), stateHistory.getLastModifiedDate());
                    } catch (ApplicationRuntimeException e) {
                        if (Log.isErrorEnabled())
                            Log.error("Exception while getting history of record :" + e);
                        throw new ApplicationRuntimeException("err.user.not.found");
                    }
                    historyMap.put("user", user.getUsername() == null ? EMPTY : user.getUsername() + "::" + user.getName());
                    historyMap.put(DEPARTMENT, owner.getDeptDesig().getDepartment() == null
                            ? EMPTY : owner.getDeptDesig().getDepartment().getName());
                }
                historyTable.add(historyMap);
            }
        }
        return historyTable;
    }

    @Transactional
    public WaterConnectionDetails updateWaterConnection(WaterConnectionDetails waterConnectionDetails,
                                                        Long approvalPosition, String approvalComent, String additionalRule,
                                                        String workFlowAction, String mode, ReportOutput reportOutput,
                                                        String sourceChannel) {

        applicationStatusChange(waterConnectionDetails, workFlowAction, mode);
        if (APPLICATION_STATUS_CLOSERDIGSIGNPENDING.equals(waterConnectionDetails.getStatus().getCode())
                && waterConnectionDetails.getCloseConnectionType() != null
                && workFlowAction.equals(APPROVEWORKFLOWACTION)) {
            waterConnectionDetails.setApplicationType(applicationTypeService.findByCode(CLOSINGCONNECTION));
            waterConnectionDetails.setCloseApprovalDate(new Date());
        }

        if (APPLICATION_STATUS_RECONNDIGSIGNPENDING.equals(waterConnectionDetails.getStatus().getCode())
                && waterConnectionDetails.getCloseConnectionType().equals(TEMPERARYCLOSECODE)
                && waterConnectionDetails.getReConnectionReason() != null
                && workFlowAction.equals(APPROVEWORKFLOWACTION)) {
            waterConnectionDetails.setApplicationType(applicationTypeService.findByCode(RECONNECTIONCONNECTION));
            waterConnectionDetails.setConnectionStatus(ACTIVE);
            waterConnectionDetails.setReconnectionApprovalDate(new Date());
            if (ConnectionType.NON_METERED.equals(waterConnectionDetails.getConnectionType())) {
                Installment nonMeterReconnInstallment;
                boolean reconnInSameInstallment;
                if (checkTwoDatesAreInSameInstallment(waterConnectionDetails)) {
                    Installment nonMeterCurrentInstallment = connectionDemandService.getCurrentInstallment(
                            WATER_RATES_NONMETERED_PTMODULE, null, waterConnectionDetails.getReconnectionApprovalDate());
                    Date newDateForNextInstall;
                    int numberOfMonths = 6;
                    if (noOfMonthsBetween(waterConnectionDetails.getReconnectionApprovalDate(),
                            financialYearDAO.getFinancialYearByDate(new Date()).getEndingDate()) >= numberOfMonths) {
                        newDateForNextInstall = DateUtils.addDays(nonMeterCurrentInstallment.getToDate(), 1);
                    } else {
                        newDateForNextInstall = waterConnectionDetails.getReconnectionApprovalDate();
                    }
                    nonMeterReconnInstallment = connectionDemandService.getCurrentInstallment(
                            WATER_RATES_NONMETERED_PTMODULE, null, newDateForNextInstall);
                    reconnInSameInstallment = true;
                } else {
                    nonMeterReconnInstallment = connectionDemandService.getCurrentInstallment(
                            WATER_RATES_NONMETERED_PTMODULE, null,
                            waterConnectionDetails.getReconnectionApprovalDate());
                    reconnInSameInstallment = false;
                }
                connectionDemandService.updateDemandForNonmeteredConnection(waterConnectionDetails,
                        nonMeterReconnInstallment, reconnInSameInstallment, null);
            }
            updateIndexes(waterConnectionDetails, sourceChannel);
        }

        if (ConnectionType.NON_METERED.equals(waterConnectionDetails.getConnectionType())
                && APPLICATION_STATUS_SANCTIONED.equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {
            connectionDemandService.updateDemandForNonmeteredConnection(waterConnectionDetails, null, null, workFlowAction);
            updateIndexes(waterConnectionDetails, sourceChannel);
        }

        // Setting FileStoreMap object object while Commissioner Sign's the
        // document
        if (workFlowAction != null && workFlowAction.equalsIgnoreCase(SIGNWORKFLOWACTION) && reportOutput != null) {
            String fileName = SIGNED_DOCUMENT_PREFIX + waterConnectionDetails.getWorkOrderNumber() + ".pdf";
            InputStream fileStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
            FileStoreMapper fileStore = fileStoreService.store(fileStream, fileName, "application/pdf", FILESTORE_MODULECODE);
            waterConnectionDetails.setFileStore(fileStore);
        }

        WaterConnectionDetails updatedWaterConnectionDetails = waterConnectionDetailsRepository.save(waterConnectionDetails);
        ApplicationWorkflowCustomDefaultImpl applicationWorkflowCustomDefaultImpl = getInitialisedWorkFlowBean();
        if (waterConnectionDetails.getCloseConnectionType() != null)
            additionalRule = WORKFLOW_CLOSUREADDITIONALRULE;

        if (waterConnectionDetails.getReConnectionReason() != null)
            additionalRule = RECONNECTIONCONNECTION;
        applicationWorkflowCustomDefaultImpl.createCommonWorkflowTransition(updatedWaterConnectionDetails,
                approvalPosition, approvalComent, additionalRule, workFlowAction);

        // To backUpdate waterConnectiondetails after ClosureConnection is
        // cancelled
        if (waterConnectionDetails.getCloseConnectionType() != null
                && waterConnectionDetails.getReConnectionReason() == null
                && waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_CANCELLED)
                && waterConnectionDetails.getConnectionStatus().equals(INACTIVE)) {
            waterConnectionDetails.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_SANCTIONED, MODULETYPE));
            waterConnectionDetails.setConnectionStatus(ACTIVE);
            waterConnectionDetails.setCloseConnectionType(null);
            waterConnectionDetails.setCloseconnectionreason(null);
            waterConnectionDetails.setApplicationType(
                    applicationTypeService.findByCode(waterConnectionDetails.getPreviousApplicationType()));
            updateIndexes(waterConnectionDetails, sourceChannel);
            updatedWaterConnectionDetails = waterConnectionDetailsRepository.save(waterConnectionDetails);
        }
        // back to CLoserSanctioned Status if Reconnection is Rejected 2 times
        if (waterConnectionDetails.getReConnectionReason() != null
                && waterConnectionDetails.getCloseConnectionType() == TEMPERARYCLOSECODE
                && waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_CANCELLED)
                && waterConnectionDetails.getConnectionStatus().equals(INACTIVE)) {
            waterConnectionDetails.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(
                    APPLICATION_STATUS_CLOSERSANCTIONED, MODULETYPE));
            waterConnectionDetails.setConnectionStatus(CLOSED);
            waterConnectionDetails.setReConnectionReason(null);
            waterConnectionDetails.setApplicationType(applicationTypeService.findByCode(CLOSINGCONNECTION));
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
        if (context != null)
            applicationWorkflowCustomDefaultImpl = (ApplicationWorkflowCustomDefaultImpl) context
                    .getBean("applicationWorkflowCustomDefaultImpl");
        return applicationWorkflowCustomDefaultImpl;
    }

    public boolean checkTwoDatesAreInSameInstallment(WaterConnectionDetails waterConnectionDetails) {

        Installment nonMeterClosedInstallment = connectionDemandService.getCurrentInstallment(
                WATER_RATES_NONMETERED_PTMODULE, null, waterConnectionDetails.getCloseApprovalDate());
        Installment nonMeterReconnInstallment = connectionDemandService.getCurrentInstallment(
                WATER_RATES_NONMETERED_PTMODULE, null, waterConnectionDetails.getReconnectionApprovalDate());

        return nonMeterClosedInstallment.getDescription().equals(nonMeterReconnInstallment.getDescription());
    }

    public void applicationStatusChange(WaterConnectionDetails waterConnectionDetails,
                                        String workFlowAction, String mode) {
        if (waterConnectionDetails == null)
            throw new ValidationException("err.application.not.exist");
        else if (NEWCONNECTION.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode()) ||
                CHANGEOFUSE.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode()) ||
                ADDNLCONNECTION.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode()) ||
                REGULARIZE_CONNECTION.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode()))
            applicationStatusUpdate(waterConnectionDetails, workFlowAction);
        else if (RECONNECTIONCONNECTION.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode()))
            reconnectionStatusUpdate(waterConnectionDetails, workFlowAction);
        else if (CLOSINGCONNECTION.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode()))
            closureStatusUpdate(waterConnectionDetails, workFlowAction, mode);

    }

    public void applicationStatusUpdate(WaterConnectionDetails waterConnectionDetails, String workFlowAction) {
        if (waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_CREATED)
                && waterConnectionDetails.getState() != null && "Submit".equals(workFlowAction))
            waterConnectionDetails.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(
                    APPLICATION_STATUS_VERIFIED, MODULETYPE));
        else if (workFlowAction.equals(WF_STATE_BUTTON_GENERATEESTIMATE)
                && waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_VERIFIED))
            waterConnectionDetails.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(
                    APPLICATION_STATUS_ESTIMATENOTICEGEN, MODULETYPE));
        else if (waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_FEEPAID)
                && workFlowAction.equalsIgnoreCase(APPROVEWORKFLOWACTION))
            waterConnectionDetails.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(
                    APPLICATION_STATUS_DIGITALSIGNPENDING, MODULETYPE));
        else if (WF_STATE_REJECTED.equalsIgnoreCase(waterConnectionDetails.getState().getValue()) &&
                APPLICATION_STATUS_ESTIMATENOTICEGEN.equalsIgnoreCase(waterConnectionDetails.getStatus().getCode()) &&
                FORWARDWORKFLOWACTION.equalsIgnoreCase(workFlowAction))
            waterConnectionDetails.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_CREATED, MODULETYPE));
        else if (SIGNWORKFLOWACTION.equals(workFlowAction)
                && APPLICATION_STATUS_DIGITALSIGNPENDING.equals(waterConnectionDetails.getStatus().getCode())
                && REGULARIZE_CONNECTION.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode())) {
            waterConnectionDetails.setConnectionStatus(ACTIVE);
            waterConnectionDetails.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_SANCTIONED, MODULETYPE));
        } else if (SIGNWORKFLOWACTION.equals(workFlowAction)
                && APPLICATION_STATUS_DIGITALSIGNPENDING.equals(waterConnectionDetails.getStatus().getCode()))
            waterConnectionDetails.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_APPROVED, MODULETYPE));
    }

    public void closureStatusUpdate(WaterConnectionDetails waterConnectionDetails, String workFlowAction,
                                    String mode) {
        if (APPLICATION_STATUS_SANCTIONED.equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())
                && isNotBlank(waterConnectionDetails.getCloseConnectionType()))
            waterConnectionDetails.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_CLOSERINITIATED, MODULETYPE));
        else if (!"closeredit".equals(mode)
                && APPLICATION_STATUS_CLOSERINITIATED.equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())
                && isNotBlank(waterConnectionDetails.getCloseConnectionType()))
            waterConnectionDetails.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_CLOSERINPROGRESS, MODULETYPE));
        else if (APPROVEWORKFLOWACTION.equals(workFlowAction)
                && APPLICATION_STATUS_CLOSERINPROGRESS.equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())
                && isNotBlank(waterConnectionDetails.getCloseConnectionType()))
            waterConnectionDetails.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(
                    APPLICATION_STATUS_CLOSERDIGSIGNPENDING, MODULETYPE));
        else if (SIGNWORKFLOWACTION.equals(workFlowAction)
                && APPLICATION_STATUS_CLOSERDIGSIGNPENDING.equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())
                && isNotBlank(waterConnectionDetails.getCloseConnectionType()))
            waterConnectionDetails.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(
                    APPLICATION_STATUS_CLOSERSANCTIONED, MODULETYPE));
    }

    public void reconnectionStatusUpdate(WaterConnectionDetails waterConnectionDetails, String workFlowAction) {

        if (APPLICATION_STATUS_CLOSERSANCTIONED.equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())
                && isNotBlank(waterConnectionDetails.getCloseConnectionType())
                && TEMPERARYCLOSECODE.equals(waterConnectionDetails.getCloseConnectionType()))
            waterConnectionDetails.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(WORKFLOW_RECONNCTIONINITIATED, MODULETYPE));
        else if (WORKFLOW_RECONNCTIONINITIATED.equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())
                && TEMPERARYCLOSECODE.equals(waterConnectionDetails.getCloseConnectionType()))
            waterConnectionDetails.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(
                    APPLICATION_STATUS_RECONNCTIONINPROGRESS, MODULETYPE));
        else if (APPROVEWORKFLOWACTION.equals(workFlowAction)
               && APPLICATION_STATUS_RECONNCTIONINPROGRESS.equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())
                && TEMPERARYCLOSECODE.equals(waterConnectionDetails.getCloseConnectionType()))
            waterConnectionDetails.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(
                    APPLICATION_STATUS_RECONNDIGSIGNPENDING, MODULETYPE));
        else if (SIGNWORKFLOWACTION.equals(workFlowAction)
                && APPLICATION_STATUS_RECONNDIGSIGNPENDING.equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())
                && TEMPERARYCLOSECODE.equals(waterConnectionDetails.getCloseConnectionType()))
            waterConnectionDetails.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(
                    APPLICATION_STATUS_RECONNCTIONSANCTIONED, MODULETYPE));
    }

    public Long getApprovalPositionByMatrixDesignation(WaterConnectionDetails waterConnectionDetails,
                                                       Long approvalPosition, String additionalRule, String mode, String workFlowAction) {

        String loggedInUserDesignation = waterTaxUtils.loggedInUserDesignation(waterConnectionDetails);
        WorkFlowMatrix wfmatrix;
        String pendingAction = null;
        if (REGULARIZE_CONNECTION.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode()))
            pendingAction = getReglnConnectionPendingAction(waterConnectionDetails, loggedInUserDesignation, workFlowAction);

        if (isNotBlank(loggedInUserDesignation)
                && (COMMISSIONER_DESGN.equalsIgnoreCase(loggedInUserDesignation)
                || EXECUTIVE_ENGINEER_DESIGN.equalsIgnoreCase(loggedInUserDesignation)
                || MUNICIPAL_ENGINEER_DESIGN.equalsIgnoreCase(loggedInUserDesignation)
                || SUPERIENTEND_ENGINEER_DESIGN.equalsIgnoreCase(loggedInUserDesignation)
                || TAP_INSPPECTOR_DESIGN.equalsIgnoreCase(loggedInUserDesignation)
                || DEPUTY_ENGINEER_DESIGN.equalsIgnoreCase(loggedInUserDesignation)
                || ASSISTANT_ENGINEER_DESIGN.equalsIgnoreCase(loggedInUserDesignation)
                || ASSISTANT_EXECUTIVE_ENGINEER_DESIGN.equalsIgnoreCase(loggedInUserDesignation))
                && (APPLICATION_STATUS_VERIFIED.equals(waterConnectionDetails.getStatus().getCode())
                || APPLICATION_STATUS_CREATED.equals(waterConnectionDetails.getStatus().getCode())
                || APPLICATION_STATUS_WOGENERATED.equals(waterConnectionDetails.getStatus().getCode())
                || APPLICATION_STATUS_CLOSERINPROGRESS.equals(waterConnectionDetails.getStatus().getCode())
                || APPLICATION_STATUS_RECONNCTIONINPROGRESS.equals(waterConnectionDetails.getStatus().getCode()))
                || APPLICATION_STATUS_FEEPAID.equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())
                && (FORWARDWORKFLOWACTION.equalsIgnoreCase(workFlowAction) ||
                APPROVEWORKFLOWACTION.equalsIgnoreCase(workFlowAction)))
            wfmatrix = waterConnectionWorkflowService.getWfMatrix(waterConnectionDetails.getStateType(), null, null,
                    additionalRule, waterConnectionDetails.getCurrentState().getValue(), pendingAction, null,
                    loggedInUserDesignation);
        else
            wfmatrix = waterConnectionWorkflowService.getWfMatrix(waterConnectionDetails.getStateType(), null, null,
                    additionalRule, waterConnectionDetails.getCurrentState().getValue(), null);

        if (APPLICATION_STATUS_ESTIMATENOTICEGEN.equals(waterConnectionDetails.getStatus().getCode()))
            approvalPosition = waterTaxUtils.getApproverPosition(
                    JUNIOR_OR_SENIOR_ASSISTANT_DESIGN_REVENUE_CLERK, waterConnectionDetails);
        if (WFLOW_ACTION_STEP_CANCEL.equalsIgnoreCase(workFlowAction))
            approvalPosition = waterConnectionDetails.getState().getOwnerPosition().getId();
        else if (waterConnectionDetails != null && waterConnectionDetails.getStatus() != null
            && isNotBlank(waterConnectionDetails.getStatus().getCode())) {
            if (APPLICATION_STATUS_CREATED.equals(waterConnectionDetails.getStatus().getCode())
                    && waterConnectionDetails.getState() != null)
                if ("edit".equals(mode) && !waterConnectionDetails.getStateHistory().isEmpty())
                    approvalPosition = waterConnectionDetails.getState().getOwnerPosition().getId();
                else
                    approvalPosition = waterTaxUtils.getApproverPosition(wfmatrix.getNextDesignation(), waterConnectionDetails);
            else if (waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_APPROVED)
                    && isNotBlank(wfmatrix.getNextDesignation())
                    || isNotBlank(workFlowAction) && workFlowAction.equals(WFLOW_ACTION_STEP_REJECT)
                    && waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_CLOSERINITIATED)
                    && waterConnectionDetails.getState().getValue().equals(WF_STATE_REJECTED))
                approvalPosition = waterTaxUtils.getApproverPosition(wfmatrix.getNextDesignation(), waterConnectionDetails);

            else if (wfmatrix != null && isNotBlank(wfmatrix.getNextDesignation())
                    && !workFlowAction.equals(APPROVEWORKFLOWACTION)
                    && !workFlowAction.equals(SIGNWORKFLOWACTION)
                    && !workFlowAction.equals(WF_PREVIEW_BUTTON)
                    && (waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_FEEPAID)
                    || waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_VERIFIED)
                    || waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_CLOSERDIGSIGNPENDING)
                    || waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_DIGITALSIGNPENDING)
                    || waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_RECONNCTIONAPPROVED)
                    || workFlowAction.equals(WFLOW_ACTION_STEP_REJECT)
                    && (waterConnectionDetails.getStatus().getCode().equals(WORKFLOW_RECONNCTIONINITIATED)
                    || waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_CLOSERINITIATED)))) {
                approvalPosition = waterTaxUtils.getApproverPosition(wfmatrix.getNextDesignation(), waterConnectionDetails);
            } else if (WF_PREVIEW_BUTTON.equals(workFlowAction) || workFlowAction.equals(SIGNWORKFLOWACTION)
                    || workFlowAction.equals(APPROVEWORKFLOWACTION)
                    && (waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_FEEPAID)
                    || APPLICATION_STATUS_DIGITALSIGNPENDING.equals(waterConnectionDetails.getStatus().getCode())
                    || waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_CLOSERINPROGRESS)
                    || waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_RECONNCTIONINPROGRESS)
                    || waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_APPROVED))) {
                approvalPosition = waterConnectionDetails.getState().getOwnerPosition().getId();
            } else if (wfmatrix != null && isNotBlank(wfmatrix.getNextDesignation()) && !workFlowAction.equals(WFLOW_ACTION_STEP_REJECT)
                    && (waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_CLOSERINITIATED)
                    || !waterConnectionDetails.getState().getValue().equals(WF_STATE_REJECTED)
                    && (waterConnectionDetails.getStatus().getCode().equals(WORKFLOW_RECONNCTIONINITIATED)
                    || waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_RECONNCTIONINPROGRESS))
                    || waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_APPROVED))) {
                Position posobj = waterTaxUtils.getCityLevelCommissionerPosition(wfmatrix.getNextDesignation(),
                        waterConnectionDetails.getConnection().getPropertyIdentifier());
                if (posobj != null)
                    approvalPosition = posobj.getId();
            }
        }
        return approvalPosition;
    }

    public void updateConsumerIndex(WaterConnectionDetails waterConnectionDetails) {
        AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
                waterConnectionDetails.getConnection().getPropertyIdentifier(),
                PropertyExternalService.FLAG_FULL_DETAILS, BasicPropertyStatus.ALL);
        if (waterConnectionDetails.getLegacy())
            createWaterChargeIndex(waterConnectionDetails, assessmentDetails, getTotalAmount(waterConnectionDetails));
    }

    public WaterChargeDocument createWaterChargeIndex(WaterConnectionDetails waterConnectionDetails,
                                                      AssessmentDetails assessmentDetails, BigDecimal amountTodisplayInIndex) {

        return waterChargeIndexService.createWaterChargeIndex(waterConnectionDetails, assessmentDetails, amountTodisplayInIndex);
    }

    @Transactional
    public void updateIndexes(WaterConnectionDetails waterConnectionDetails, String sourceChannel) {

        AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
                waterConnectionDetails.getConnection().getPropertyIdentifier(),
                PropertyExternalService.FLAG_FULL_DETAILS, BasicPropertyStatus.ALL);
        if (LOG.isDebugEnabled())
            LOG.debug(" updating Indexes Started... ");
        BigDecimal amountTodisplayInIndex = ZERO;
        if (waterConnectionDetails.getConnection().getConsumerCode() != null)
            amountTodisplayInIndex = getTotalAmountTillCurrentFinYear(waterConnectionDetails);
        if ((SURVEY.equals(waterConnectionDetails.getSource()) || waterConnectionDetails.getLegacy())
                && APPLICATION_STATUS_SANCTIONED.equals(waterConnectionDetails.getStatus().getCode())) {
            createWaterChargeIndex(waterConnectionDetails, assessmentDetails, amountTodisplayInIndex);
            return;
        }
        Iterator<OwnerName> ownerNameItr = null;
        if (assessmentDetails.getOwnerNames() != null)
            ownerNameItr = assessmentDetails.getOwnerNames().iterator();

        Assignment assignment;
        User user = null;
        StringBuilder aadharNumber = new StringBuilder();
        StringBuilder consumerName = new StringBuilder();
        StringBuilder mobileNumber = new StringBuilder();
        if (ownerNameItr != null && ownerNameItr.hasNext()) {
            OwnerName primaryOwner = ownerNameItr.next();
            if (primaryOwner != null) {
                consumerName.append(primaryOwner.getOwnerName() == null ? EMPTY : primaryOwner.getOwnerName());
                mobileNumber.append(primaryOwner.getMobileNumber() == null ? EMPTY : primaryOwner.getMobileNumber());
                aadharNumber.append(primaryOwner.getAadhaarNumber() == null ? EMPTY : primaryOwner.getAadhaarNumber());
            }
            while (ownerNameItr.hasNext()) {
                OwnerName secondaryOwner = ownerNameItr.next();
                consumerName.append(',').append(secondaryOwner.getOwnerName() == null ? EMPTY : secondaryOwner.getOwnerName());
                if (isBlank(mobileNumber.toString()))
                    mobileNumber.append(',').append(secondaryOwner.getMobileNumber() == null ? EMPTY : secondaryOwner.getMobileNumber());
                if (isBlank(aadharNumber.toString()))
                    aadharNumber.append(',').append(secondaryOwner.getAadhaarNumber() == null ? EMPTY : secondaryOwner.getAadhaarNumber());
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
        ApplicationIndex applicationIndex = applicationIndexService.findByApplicationNumber(waterConnectionDetails.getApplicationNumber());
        if (applicationIndex != null
                && waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_CREATED)) {
            applicationIndex.setOwnerName(user == null ? EMPTY : user.getUsername() + "::" + user.getName());
            applicationIndexService.updateApplicationIndex(applicationIndex);
        }
        if (applicationIndex != null && waterConnectionDetails.getId() != null
                && waterConnectionDetails.getStatus() != null
                && !waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_CREATED)) {
            if (waterConnectionDetails.getStatus() != null
                    && !waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_RECONNCTIONAPPROVED)
                    && !waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_RECONNCTIONSANCTIONED)
                    && !waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_SANCTIONED)) {
                applicationIndex.setApplicationType(waterConnectionDetails.getApplicationType().getName());
                applicationIndex.setApplicantAddress(assessmentDetails.getPropertyAddress());
                applicationIndex.setApproved(ApprovalStatus.INPROGRESS);
                applicationIndex.setClosed(ClosureStatus.NO);
                applicationIndex.setStatus(waterConnectionDetails.getStatus().getDescription());
                if (!waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_SANCTIONED))
                    applicationIndex.setOwnerName(user == null ? EMPTY : user.getUsername() + "::" + user.getName());
                applicationIndex.setSla(applicationProcessTimeService.getApplicationProcessTime(
                        waterConnectionDetails.getApplicationType(), waterConnectionDetails.getCategory()));
                if (applicationIndex.getChannel() == null)
                    if (waterTaxUtils.isCSCoperator(waterConnectionDetails.getCreatedBy())
                            && UserType.BUSINESS.equals(waterConnectionDetails.getCreatedBy().getType()))
                        applicationIndex.setChannel(Source.CSC.toString());
                    else if (waterTaxUtils.isCitizenPortalUser(waterConnectionDetails.getCreatedBy()))
                        applicationIndex.setChannel(Source.CITIZENPORTAL.toString());
                    else if (sourceChannel == null)
                        applicationIndex.setChannel(SYSTEM);
                    else if (MEESEVA.toString().equalsIgnoreCase(waterConnectionDetails.getSource().toString()))
                        applicationIndex.setChannel(MEESEVA.toString());
                    else
                        applicationIndex.setChannel(sourceChannel);
            }
            if (waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_RECONNCTIONSANCTIONED)
                    || waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_APPROVED)
                    || waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_SANCTIONED)
                    || waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_CLOSERSANCTIONED)) {
                applicationIndex.setStatus(APPLICATIONSTATUSCLOSED);
                applicationIndex.setApproved(ApprovalStatus.APPROVED);
                applicationIndex.setClosed(ClosureStatus.YES);
                applicationIndex.setOwnerName(user == null ? EMPTY : user.getUsername() + "::" + user.getName());

            }
            if (waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_CANCELLED)) {
                applicationIndex.setStatus(APPLICATIONSTATUSCLOSED);
                applicationIndex.setApproved(ApprovalStatus.REJECTED);
                applicationIndex.setClosed(ClosureStatus.YES);
            }
            if (isNotBlank(waterConnectionDetails.getConnection().getConsumerCode()))
                applicationIndex.setConsumerCode(waterConnectionDetails.getConnection().getConsumerCode());
            applicationIndexService.updateApplicationIndex(applicationIndex);

            // Creating Consumer Index only on Sanction
            if (waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_SANCTIONED)
                    && waterConnectionDetails.getConnectionStatus().equals(ConnectionStatus.INPROGRESS)
                    && !waterConnectionDetails.getApplicationType().getCode().equalsIgnoreCase(CHANGEOFUSE)) {
                waterConnectionDetails.setConnectionStatus(ACTIVE);
                if (LOG.isDebugEnabled())
                    LOG.debug(" updating Consumer Index Started... ");
                if (!waterConnectionDetails.getConnectionStatus().equals(INACTIVE)
                        || !waterConnectionDetails.getConnectionStatus().equals(ConnectionStatus.INPROGRESS))
                    createWaterChargeIndex(waterConnectionDetails, assessmentDetails, amountTodisplayInIndex);
                if (LOG.isDebugEnabled())
                    LOG.debug(" updating Consumer Index completed... ");
            }
            // To Update After ClosureConnection is rejected
            if (waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_SANCTIONED)
                    && waterConnectionDetails.getConnectionStatus().equals(ACTIVE))
                createWaterChargeIndex(waterConnectionDetails, assessmentDetails, amountTodisplayInIndex);
            if (waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_CLOSERSANCTIONED)
                    || waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_CLOSERAPRROVED)
                    && waterConnectionDetails.getConnectionStatus().equals(CLOSED))
                createWaterChargeIndex(waterConnectionDetails, assessmentDetails, amountTodisplayInIndex);

            if (waterConnectionDetails.getCloseConnectionType() != null
                    && waterConnectionDetails.getCloseConnectionType().equals(TEMPERARYCLOSECODE)
                    && (waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_RECONNCTIONAPPROVED)
                    || waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_RECONNCTIONSANCTIONED))) {
                waterConnectionDetails.setConnectionStatus(ACTIVE);
                createWaterChargeIndex(waterConnectionDetails, assessmentDetails, amountTodisplayInIndex);
            }
        } else if (!SURVEY.equals(waterConnectionDetails.getSource())) {
            Integer appProcessTime = applicationProcessTimeService.getApplicationProcessTime(
                    waterConnectionDetails.getApplicationType(), waterConnectionDetails.getCategory());
            String strQuery = "select md from EgModules md where md.name=:name";
            Query hql = getCurrentSession().createQuery(strQuery);
            hql.setParameter("name", EGMODULES_NAME);
            if (waterConnectionDetails.getApplicationDate() == null)
                waterConnectionDetails.setApplicationDate(new DateTime().toDate());
            if (waterConnectionDetails.getApplicationNumber() == null)
                waterConnectionDetails.setApplicationNumber(waterConnectionDetails.getConnection().getConsumerCode());
            if (applicationIndex == null && !waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_SANCTIONED)) {
                if (LOG.isDebugEnabled())
                    LOG.debug(" updating Application Index creation Started... ");
                String channel;
                if (waterConnectionDetails.getSource() == null) {
                    if (waterTaxUtils.isCSCoperator(waterConnectionDetails.getCreatedBy())
                            && UserType.BUSINESS.equals(waterConnectionDetails.getCreatedBy().getType()))
                        channel = CSC.toString();
                    else if (sourceChannel == null)
                        channel = SYSTEM;
                    else
                        channel = sourceChannel;
                } else
                    channel = waterConnectionDetails.getSource().toString();

                applicationIndex = ApplicationIndex.builder().withModuleName(((EgModules) hql.uniqueResult()).getName())
                        .withApplicationNumber(waterConnectionDetails.getApplicationNumber())
                        .withApplicationDate(new DateTime(waterConnectionDetails.getApplicationDate()).toDate())
                        .withApplicationType(waterConnectionDetails.getApplicationType().getName())
                        .withApplicantName(consumerName.toString())
                        .withStatus(waterConnectionDetails.getStatus().getDescription())
                        .withUrl(String.format(WTMS_APPLICATION_VIEW, waterConnectionDetails.getApplicationNumber()))
                        .withApplicantAddress(assessmentDetails.getPropertyAddress())
                        .withOwnername(user.getUsername() + "::" + user.getName())
                        .withChannel(sourceChannel == null ? channel : sourceChannel)
                        .withMobileNumber(mobileNumber.toString()).withClosed(ClosureStatus.NO)
                        .withAadharNumber(aadharNumber.toString()).withApproved(ApprovalStatus.INPROGRESS)
                        .withSla(appProcessTime == null ? 0 : appProcessTime).build();
                if ((!waterConnectionDetails.getLegacy()
                        || CLOSINGCONNECTION.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode()))
                        && !waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_SANCTIONED))
                    applicationIndexService.createApplicationIndex(applicationIndex);
            }
            if (LOG.isDebugEnabled())
                LOG.debug(" updating Application Index creation complted... ");
        }
    }

    public Date getDisposalDate(WaterConnectionDetails waterConnectionDetails, Integer appProcessTime) {
        Calendar c = Calendar.getInstance();
        c.setTime(waterConnectionDetails.getApplicationDate());
        c.add(Calendar.DATE, appProcessTime);
        return c.getTime();
    }

    public WaterConnectionDetails getParentConnectionDetails(String propertyIdentifier,
                                                             ConnectionStatus connectionStatus) {
        return waterConnectionDetailsRepository
                .findByConnection_PropertyIdentifierAndConnectionStatusAndConnection_ParentConnectionIsNull(
                        propertyIdentifier, connectionStatus);
    }

    public WaterConnectionDetails getParentConnectionDetailsForParentConnectionNotNull(String consumercode,
                                                                                       ConnectionStatus connectionStatus) {
        return waterConnectionDetailsRepository
                .findByConnection_ConsumerCodeAndConnectionStatusAndConnection_ParentConnectionIsNotNull(consumercode,
                        connectionStatus);
    }

    public WaterConnectionDetails getWaterConnectionDetailsByDemand(EgDemand demand) {
        return waterConnectionDetailsRepository.findByDemand(demand);
    }

    @Transactional
    public void save(WaterConnectionDetails detail) {
        waterConnectionDetailsRepository.save(detail);
    }

    public WaterConnectionDetails getActiveNonHistoryConnectionDetailsByConnection(WaterConnection waterConnection) {
        return waterConnectionDetailsRepository.findByConnectionAndConnectionStatusAndIsHistory(waterConnection,
                ACTIVE, Boolean.FALSE);
    }

    public BigDecimal getTotalAmount(WaterConnectionDetails waterConnectionDetails) {
        EgDemand currentDemand = waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand();
        BigDecimal balance = ZERO;
        if (currentDemand != null) {
            List<Object> instVsAmt = connectionDemandService.getDmdCollAmtInstallmentWise(currentDemand);
            for (Object object : instVsAmt) {
                Object[] ddObject = (Object[]) object;
                BigDecimal dmdAmt = new BigDecimal((Double) ddObject[2]);
                BigDecimal collAmt = ZERO;
                if (ddObject[2] != null)
                    collAmt = new BigDecimal((Double) ddObject[3]);
                balance = balance.add(dmdAmt.subtract(collAmt)).setScale(0, HALF_UP);
            }
        }
        return balance;
    }

    public BigDecimal getTotalAmountTillCurrentFinYear(WaterConnectionDetails waterConnectionDetails) {
        EgDemand currentDemand = waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand();
        BigDecimal balance = ZERO;
        if (currentDemand != null) {
            List<Object> instVsAmt = connectionDemandService.getDmdCollAmtInstallmentWiseUptoCurrentFinYear(currentDemand);
            for (Object object : instVsAmt) {
                Object[] ddObject = (Object[]) object;
                BigDecimal dmdAmt = new BigDecimal((Double) ddObject[2]);
                BigDecimal collAmt = ZERO;
                if (ddObject[2] != null)
                    collAmt = new BigDecimal((Double) ddObject[3]);
                balance = balance.add(dmdAmt.subtract(collAmt));
            }
        }
        if (balance.signum() < 0)
            balance = ZERO;
        return balance;
    }

    public BigDecimal getCurrentDue(WaterConnectionDetails waterConnectionDetails) {
        EgDemand currentDemand = waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand();
        BigDecimal balance = ZERO;
        if (currentDemand != null) {
            List<Object> instVsAmt = connectionDemandService.getDmdCollAmtInstallmentWiseUptoCurrentInstallmemt(currentDemand, waterConnectionDetails);
            for (Object object : instVsAmt) {
                Object[] ddObject = (Object[]) object;
                BigDecimal dmdAmt = new BigDecimal((Double) ddObject[2]);
                BigDecimal collAmt = ZERO;
                if (ddObject[2] != null)
                    collAmt = new BigDecimal((Double) ddObject[3]);
                balance = balance.add(dmdAmt.subtract(collAmt));
            }
        }
        return balance;
    }

    public List<ApplicationDocuments> getApplicationDocForExceptClosureAndReConnection(WaterConnectionDetails waterConnectionDetails) {

        List<ApplicationDocuments> tempDocList = new ArrayList<>(0);
        if (waterConnectionDetails != null)
            for (ApplicationDocuments appDoc : waterConnectionDetails.getApplicationDocs())
                if (appDoc.getDocumentNames() != null
                        && (appDoc.getDocumentNames().getApplicationType().getCode().equals(NEWCONNECTION)
                        || appDoc.getDocumentNames().getApplicationType().getCode().equals(ADDNLCONNECTION)
                        || appDoc.getDocumentNames().getApplicationType().getCode().equals(CHANGEOFUSE))
                        || REGULARIZE_CONNECTION.equals(appDoc.getDocumentNames().getApplicationType().getCode())) {
                    tempDocList.add(appDoc);
                }
        return tempDocList;
    }

    public void validateWaterRateAndDonationHeader(WaterConnectionDetails waterConnectionDetails) {
        DonationDetails donationDetails = connectionDemandService.getDonationDetails(waterConnectionDetails);
        if (donationDetails == null)
            throw new ValidationException("donation.combination.required");
        WaterRatesDetails waterRatesDetails = connectionDemandService.getWaterRatesDetailsForDemandUpdate(waterConnectionDetails);
        if (waterRatesDetails == null)
            throw new ValidationException("err.water.rate.not.found");
    }

    public String getApprovalPositionOnValidate(Long approvalPositionId) {
        Assignment assignmentObj;
        List<Assignment> assignmentList = new ArrayList<>();
        if (approvalPositionId != null && approvalPositionId != 0 && approvalPositionId != -1) {
            assignmentObj = assignmentService.getPrimaryAssignmentForPositionAndDate(approvalPositionId, new Date());
            if (assignmentObj == null)
                throw new ValidationException("err.user.not.defined");
            assignmentList.add(assignmentObj);

            Gson jsonCreator = new GsonBuilder().registerTypeAdapter(Assignment.class, new AssignmentAdaptor()).create();
            return jsonCreator.toJson(assignmentList, new TypeToken<Collection<Assignment>>() {
            }.getType());
        }
        return "[]";
    }

    @Transactional
    public WaterConnectionDetails updateWaterConnectionDetailsWithFileStore(WaterConnectionDetails waterConnectionDetails) {
        return entityManager.merge(waterConnectionDetails);
    }

    public Map<String, String> getNonMeteredConnectionTypesMap() {
        Map<String, String> connectionTypeMap = new LinkedHashMap<>(0);
        connectionTypeMap.put(ConnectionType.NON_METERED.toString(), NON_METERED);
        return connectionTypeMap;
    }

    public BigDecimal getTotalAmountTillPreviousFinYear(WaterConnectionDetails waterConnectionDetails) {
        EgDemand currentDemand = waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand();
        BigDecimal balance = ZERO;
        if (currentDemand != null) {
            List<Object> instVsAmt = connectionDemandService.getDmdCollAmtInstallmentWiseUptoPreviousFinYear(currentDemand);
            for (Object object : instVsAmt) {
                Object[] ddObject = (Object[]) object;
                BigDecimal dmdAmt = new BigDecimal((Double) ddObject[2]);
                BigDecimal collAmt = ZERO;
                if (ddObject[2] != null)
                    collAmt = new BigDecimal((Double) ddObject[3]);
                balance = balance.add(dmdAmt.subtract(collAmt));
            }
        }
        if (balance.signum() < 0)
            balance = ZERO;
        return balance;
    }

    public BigDecimal getArrearsDemand(WaterConnectionDetails waterConnectionDetails) {
        EgDemand currentDemand = waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand();
        BigDecimal balance = ZERO;
        if (currentDemand != null) {
            List<Object> instVsAmt = connectionDemandService.getDmdCollAmtInstallmentWiseUptoPreviousFinYear(currentDemand);
            balance = getTotalBalance(instVsAmt);
        }
        if (balance.signum() < 0)
            balance = ZERO;
        return balance;
    }

    public BigDecimal getTotalDemandTillCurrentFinYear(WaterConnectionDetails waterConnectionDetails) {
        EgDemand currentDemand = waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand();
        BigDecimal balance = ZERO;
        if (currentDemand != null) {
            List<Object> instVsAmt = connectionDemandService.getDmdCollAmtInstallmentWiseUptoCurrentFinYear(currentDemand);
            balance = getTotalBalance(instVsAmt);
        }
        if (balance.signum() < 0)
            balance = ZERO;
        return balance;
    }

    public BigDecimal getTotalBalance(List<Object> instVsAmt) {
        BigDecimal balance = ZERO;
        for (Object object : instVsAmt) {
            Object[] ddObject = (Object[]) object;
            if (ddObject[2] != null) {
                balance = balance.add(new BigDecimal((Double) ddObject[2]));
            }
        }
        return balance;
    }

    @Transactional
    public void saveAndFlushWaterConnectionDetail(WaterConnectionDetails waterConnectionDetails) {
        waterConnectionDetailsRepository.saveAndFlush(waterConnectionDetails);
    }

    public List<WaterConnectionDetails> getAllConnectionDetailsByPropertyID(String propertyId) {
        return waterConnectionDetailsRepository.getAllConnectionDetailsByPropertyID(propertyId);
    }

    /**
     * Method to push data for citizen portal inbox
     */

    @Transactional
    public void pushPortalMessage(WaterConnectionDetails waterConnectionDetails) {
        Module module = moduleDao.getModuleByName(MODULE_NAME);
        WaterConnection waterConnection = waterConnectionDetails.getConnection();
        PortalInboxBuilder portalInboxBuilder = new PortalInboxBuilder(module,
                waterConnectionDetails.getState().getNatureOfTask() + " : " + module.getDisplayName(),
                waterConnectionDetails.getApplicationNumber(), waterConnection.getConsumerCode(), waterConnection.getId(),
                waterConnectionDetails.getConnectionReason(), getDetailedMessage(waterConnectionDetails),
                String.format(WTMS_APPLICATION_VIEW, waterConnectionDetails.getApplicationNumber()),
                isResolved(waterConnectionDetails), waterConnectionDetails.getStatus().getDescription(),
                getSlaEndDate(waterConnectionDetails), waterConnectionDetails.getState(),
                Arrays.asList(securityUtils.getCurrentUser()));
        PortalInbox portalInbox = portalInboxBuilder.build();
        portalInboxService.pushInboxMessage(portalInbox);
    }

    private boolean isResolved(WaterConnectionDetails waterConnectionDetails) {
        return "END".equalsIgnoreCase(waterConnectionDetails.getState().getValue())
                || "CLOSED".equalsIgnoreCase(waterConnectionDetails.getState().getValue());
    }

    private Date getSlaEndDate(WaterConnectionDetails waterConnectionDetails) {
        Integer appProcessTime = applicationProcessTimeService.getApplicationProcessTime(
                waterConnectionDetails.getApplicationType(),
                waterConnectionDetails.getCategory());
        if (appProcessTime == null)
            throw new ApplicationRuntimeException("err.applicationprocesstime.undefined");
        return new DateTime().plusDays(appProcessTime).toDate();
    }

    private String getDetailedMessage(WaterConnectionDetails waterConnectionDetails) {
        Module module = moduleDao.getModuleByName(MODULE_NAME);
        StringBuilder detailedMessage = new StringBuilder();
        if (waterConnectionDetails.getApplicationType() != null)
            detailedMessage.append(APPLICATION_NO).append(waterConnectionDetails.getApplicationNumber()).append(REGARDING)
                    .append(waterConnectionDetails.getState().getNatureOfTask() + " " + module.getDisplayName()).append(" in ")
                    .append(waterConnectionDetails.getStatus().getDescription()).append(" status ");
        return detailedMessage.toString();
    }

    public PortalInbox getPortalInbox(String applicationNumber) {
        return portalInboxService.getPortalInboxByApplicationNo(applicationNumber, moduleDao.getModuleByName(MODULE_NAME).getId());
    }

    /**
     * Method to update data for citizen portal inbox
     */
    @Transactional
    public void updatePortalMessage(WaterConnectionDetails waterConnectionDetails) {
        Module module = moduleDao.getModuleByName(MODULE_NAME);
        WaterConnection waterConnection = waterConnectionDetails.getConnection();
        portalInboxService.updateInboxMessage(waterConnectionDetails.getApplicationNumber(), module.getId(),
                waterConnectionDetails.getState().getValue(), isResolved(waterConnectionDetails),
                getSlaEndDate(waterConnectionDetails), waterConnectionDetails.getState(), null, waterConnection.getConsumerCode(),
                String.format(WTMS_APPLICATION_VIEW, waterConnectionDetails.getApplicationNumber()));
    }

    public List<Object[]> getApplicationResultList(WaterConnExecutionDetails executionDetails) {
        StringBuilder queryString = new StringBuilder();
        queryString.append(
                "select conndetails.applicationnumber, conn.consumercode, mvp.ownersname, apptype.name, status.description,  ")
                .append(" conndetails.approvaldate, boundary.localname, conndetails.id, mvp.address from  egwtr_connection conn ")
                .append(" INNER JOIN egwtr_connectiondetails conndetails ON conn.id=conndetails.connection ")
                .append(" INNER JOIN egpt_mv_propertyinfo mvp ON  conn.propertyidentifier=mvp.upicno ")
                .append(" INNER JOIN eg_boundary boundary ON mvp.wardid=boundary.id ")
                .append(" INNER JOIN egwtr_application_type apptype ON conndetails.applicationtype=apptype.id ")
                .append(" INNER JOIN egw_status status ON conndetails.statusid=status.id ")
                .append(" where apptype.name=:applicationtype and status.description=:status")
                .append(" and conndetails.connectiontype=:connectionType");

        queryString = setQueryParameters(executionDetails, queryString);
        Query query = getCurrentSession().createSQLQuery(queryString.toString());
        query.setParameter("connectionType", NON_METERED_CODE);
        return setParameterDetails(executionDetails, query);
    }

    public List<Object[]> setParameterDetails(WaterConnExecutionDetails executionDetails, Query query) {
        query.setParameter("applicationtype", executionDetails.getApplicationType());
        query.setParameter(STATUS, APPROVED);

        if (isNotBlank(executionDetails.getApplicationNumber()))
            query.setParameter("applicationnumber", executionDetails.getApplicationNumber());
        if (isNotBlank(executionDetails.getConsumerNumber()))
            query.setParameter("consumernumber", executionDetails.getConsumerNumber());
        if (executionDetails.getFromDate() != null)
            query.setParameter("fromdate", DateUtils.endOfDay(executionDetails.getFromDate()));
        if (executionDetails.getToDate() != null)
            query.setParameter("todate", DateUtils.endOfDay(executionDetails.getToDate()));
        if (isNotBlank(executionDetails.getRevenueWard()))
            query.setParameter("revenueWard", executionDetails.getRevenueWard());
        return query.list();
    }

    public List<Object[]> getMeteredApplicationList(WaterConnExecutionDetails executionDetails) {
        StringBuilder queryString = new StringBuilder();
        queryString.append(
                "select conndetails.applicationnumber, conn.consumercode, mvp.ownersname, apptype.name, status.description,  ")
                .append(" conndetails.approvaldate, boundary.localname, conndetails.id, mvp.address from  egwtr_connection conn ")
                .append(" INNER JOIN egwtr_connectiondetails conndetails ON conn.id=conndetails.connection ")
                .append(" INNER JOIN egpt_mv_propertyinfo mvp ON  conn.propertyidentifier=mvp.upicno ")
                .append(" INNER JOIN eg_boundary boundary ON mvp.wardid=boundary.id ")
                .append(" INNER JOIN egwtr_application_type apptype ON conndetails.applicationtype=apptype.id ")
                .append(" INNER JOIN egw_status status ON conndetails.statusid=status.id ")
                .append(" where apptype.name=:applicationtype and status.description=:status ")
                .append(" and conndetails.connectiontype=:connectionType ");

        queryString = setQueryParameters(executionDetails, queryString);
        Query query = getCurrentSession().createSQLQuery(queryString.toString());
        query.setParameter("connectionType", CONNECTIONTYPE_METERED);
        return setParameterDetails(executionDetails, query);
    }

    public StringBuilder setQueryParameters(WaterConnExecutionDetails executionDetails, StringBuilder queryString) {
        if (isNotBlank(executionDetails.getApplicationNumber()))
            queryString.append(" and conndetails.applicationnumber=:applicationnumber");
        if (isNotBlank(executionDetails.getConsumerNumber()))
            queryString.append(" and conn.consumercode=:consumernumber");
        if (executionDetails.getFromDate() != null)
            queryString.append(" and conndetails.approvaldate>=:fromdate");
        if (executionDetails.getToDate() != null)
            queryString.append(" and conndetails.approvaldate<=:todate");
        if (isNotBlank(executionDetails.getRevenueWard()))
            queryString.append(" and boundary.name=:revenueWard");
        return queryString;
    }

    public String validateInput(WaterConnectionExecutionResponse executionDetailsResponse,
                                List<WaterConnectionDetails> connectionDetailsList) {
        JSONObject jsonObject = new JSONObject(executionDetailsResponse);
        JSONArray jsonArray = jsonObject.getJSONArray("executeWaterApplicationDetails");
        String status = EMPTY;
        for (int i = 0; i < jsonArray.length(); ++i) {
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            WaterConnectionDetails connectionDetails = findBy(jsonObj.getLong("id"));
            if (validateDonationDetails(connectionDetails)) {
                if (connectionDetails != null && isNotBlank(jsonObj.getString(EXECUTION_DATE))) {
                    connectionDetails.setExecutionDate(DateUtils.toDateUsingDefaultPattern(jsonObj.getString(EXECUTION_DATE)));
                    if (connectionDetails.getExecutionDate() != null
                            && connectionDetails.getExecutionDate().compareTo(DateUtils.toDateUsingDefaultPattern(
                            DateUtils.getDefaultFormattedDate(connectionDetails.getApprovalDate()))) < 0)
                        status = DATE_VALIDATION_FAILED;
                    else
                        connectionDetailsList.add(connectionDetails);
                }
            } else
                status = ERR_WATER_RATES_NOT_DEFINED;
        }

        return status;
    }

    @Transactional
    public Boolean updateStatus(List<WaterConnectionDetails> connectionDetailsList) {
        if (!connectionDetailsList.isEmpty()) {
            for (WaterConnectionDetails waterConnectionDetails : connectionDetailsList) {
                waterConnectionDetails = updateApplicationStatus(waterConnectionDetails);
                if (ConnectionType.NON_METERED.equals(waterConnectionDetails.getConnectionType())
                        && APPLICATION_STATUS_SANCTIONED.equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {
                    connectionDemandService.updateDemandForNonmeteredConnection(waterConnectionDetails, null, null,
                            WF_STATE_TAP_EXECUTION_DATE_BUTTON);
                }

                waterConnectionDetailsRepository.save(waterConnectionDetails);
                waterConnectionSmsAndEmailService.sendSmsAndEmail(waterConnectionDetails, null);
                updatePortalMessage(waterConnectionDetails);
                updateIndexes(waterConnectionDetails, waterConnectionDetails.getSource() == null
                        ? null : waterConnectionDetails.getSource().toString());
            }
            return true;
        }
        return false;
    }

    public List<WaterConnExecutionDetails> getConnExecutionObjectList(List<Object[]> detailList) {
        List<WaterConnExecutionDetails> resultList = new ArrayList<>();
        for (Object[] resultObject : detailList) {
            WaterConnExecutionDetails details = new WaterConnExecutionDetails();
            if (resultObject[0] != null)
                details.setApplicationNumber(resultObject[0].toString());
            if (resultObject[1] != null)
                details.setConsumerNumber(resultObject[1].toString());
            if (resultObject[2] != null)
                details.setOwnerName(resultObject[2].toString());
            if (resultObject[3] != null)
                details.setApplicationType(resultObject[3].toString());
            if (resultObject[4] != null)
                details.setApplicationStatus(resultObject[4].toString());
            if (resultObject[5] != null)
                details.setApprovalDate(resultObject[5].toString());
            if (resultObject[6] != null)
                details.setRevenueWard(resultObject[6].toString());
            if (resultObject[7] != null)
                details.setId(Long.parseLong(resultObject[7].toString()));
            if (resultObject[8] != null)
                details.setAddress(resultObject[8].toString());
            resultList.add(details);
        }
        return resultList;
    }

    public String getResultStatus(WaterConnectionExecutionResponse waterApplicationDetails, String validationStatus,
                                  Boolean updateStatus) {
        String response;
        if (waterApplicationDetails.getExecuteWaterApplicationDetails().length <= 0)
            response = EMPTY_LIST;
        else if (isNotBlank(validationStatus))
            response = validationStatus;
        else if (!updateStatus)
            response = UPDATE_FAILED;
        else
            response = SUCCESS;
        return response;
    }

    public String validateMeterDetails(WaterConnectionExecutionResponse executionResponse,
                                       List<WaterConnectionDetails> applicationList) {
        String validStatus;

        JSONObject jsonObject = new JSONObject(executionResponse);
        JSONArray jsonArray = jsonObject.getJSONArray("executeWaterApplicationDetails");
        JSONObject jsonObj = jsonArray.getJSONObject(0);
        validStatus = validateRequiredFeilds(jsonObj);
        if (!validStatus.isEmpty())
            return validStatus;
        WaterConnectionDetails waterConnectionDetails = findByApplicationNumber(jsonObj.getString(APPLICATION_NUMBER));
        if (executionResponse != null && isNotBlank(jsonObj.getString(EXECUTION_DATE))) {
            waterConnectionDetails.setExecutionDate(DateUtils.toDateUsingDefaultPattern(jsonObj.getString(EXECUTION_DATE)));
            if (waterConnectionDetails.getExecutionDate() != null
                    && waterConnectionDetails.getExecutionDate().compareTo(DateUtils.toDateUsingDefaultPattern(
                    DateUtils.getDefaultFormattedDate(waterConnectionDetails.getApplicationDate()))) < 0)
                validStatus = DATE_VALIDATION_FAILED;
            else
                applicationList.add(waterConnectionDetails);
        }
        waterConnectionDetails.getConnection().setMeterSerialNumber(jsonObj.getString(METER_SERIAL_NUMBER));
        waterConnectionDetails.getConnection().setInitialReading(Long.valueOf(jsonObj.getString(INITIAL_READING)));
        waterConnectionDetails.getConnection().setMeter(meterCostService.findByMeterMake(jsonObj.getString(METER_MAKE)).get(0));
        waterConnectionDetails.setExecutionDate(DateUtils.toDateUsingDefaultPattern(jsonObj.getString(EXECUTION_DATE)));
        waterConnectionDetailsRepository.saveAndFlush(waterConnectionDetails);
        return validStatus;
    }

    public String validateRequiredFeilds(final JSONObject jsonObject) {
        String status = EMPTY;
        if (isBlank(jsonObject.getString(METER_MAKE)))
            status = REQ_METER_MAKER;
        else if (isBlank(jsonObject.getString(EXECUTION_DATE)))
            status = REQ_EXECUTION_DATE;
        else if (isBlank(jsonObject.getString(INITIAL_READING)))
            status = REQ_INITIAL_READING;
        else if (isBlank(jsonObject.getString(METER_SERIAL_NUMBER)))
            status = REQ_METER_SERIAL_NUMBER;
        return status;
    }

    @Transactional
    public Boolean updateMeterDetails(final List<WaterConnectionDetails> detailList) {
        WaterConnectionDetails waterConnectionDetails;
        if (!detailList.isEmpty()) {
            waterConnectionDetails = detailList.get(0);
            waterConnectionDetails = updateApplicationStatus(waterConnectionDetails);
            waterConnectionDetailsRepository.saveAndFlush(waterConnectionDetails);
            waterConnectionSmsAndEmailService.sendSmsAndEmail(waterConnectionDetails, null);
            updatePortalMessage(waterConnectionDetails);
            updateIndexes(waterConnectionDetails, waterConnectionDetails.getSource() == null ? null
                    : waterConnectionDetails.getSource().toString());
            return true;
        }
        return false;

    }

    public WaterConnectionDetails updateApplicationStatus(WaterConnectionDetails waterConnectionDetails) {
        if (CHANGEOFUSE.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode())) {
            WaterConnectionDetails connectionToBeDeactivated = waterConnectionDetailsRepository
                    .findConnectionDetailsByConsumerCodeAndConnectionStatus(
                            waterConnectionDetails.getConnection().getConsumerCode(), ACTIVE);
            if (connectionToBeDeactivated != null) {
                connectionToBeDeactivated.setConnectionStatus(INACTIVE);
                connectionToBeDeactivated.setIsHistory(true);
                waterConnectionDetailsRepository.saveAndFlush(connectionToBeDeactivated);
            }
        }

        if (NEWCONNECTION.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode())
                || ADDNLCONNECTION.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode())
                || CHANGEOFUSE.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode())) {
            waterConnectionDetails.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_SANCTIONED, MODULETYPE));
            waterConnectionDetails.setConnectionStatus(ACTIVE);
        }

        return waterConnectionDetails;
    }

    public Boolean validateDonationDetails(WaterConnectionDetails waterConnectionDetails) {
        WaterRatesDetails waterRatesDetails = connectionDemandService.getWaterRatesDetailsForDemandUpdate(waterConnectionDetails);
        return waterRatesDetails == null ? false : true;
    }

    public String getReglnConnectionPendingAction(WaterConnectionDetails waterConnectionDetails,
            String loggedInUserDesignation, String workFlowAction) {
        if (APPLICATION_STATUS_FEEPAID.equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())
                && FORWARDWORKFLOWACTION.equalsIgnoreCase(workFlowAction))
            return getReglnForwardPendingAction(loggedInUserDesignation);
        if ((APPLICATION_STATUS_FEEPAID.equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())
                || APPLICATION_STATUS_DIGITALSIGNPENDING.equalsIgnoreCase(waterConnectionDetails.getStatus().getCode()))
                && APPROVEWORKFLOWACTION.equalsIgnoreCase(workFlowAction))
            return getReglnApprovePendingAction(loggedInUserDesignation);
        else if (APPLICATION_STATUS_CREATED.equalsIgnoreCase(waterConnectionDetails.getStatus().getCode()) &&
                (WFLOW_ACTION_STEP_REJECT.equalsIgnoreCase(workFlowAction)))
            return WF_STATE_AE_REJECTION_PENDING;
        else if (APPLICATION_STATUS_CREATED.equalsIgnoreCase(waterConnectionDetails.getStatus().getCode()) &&
                (FORWARDWORKFLOWACTION.equalsIgnoreCase(workFlowAction)) && loggedInUserDesignation != null)
            return WF_STATE_AE_APPROVAL_PENDING;
        else if (APPLICATION_STATUS_NEW.equalsIgnoreCase(waterConnectionDetails.getState().getValue()) &&
                FORWARDWORKFLOWACTION.equalsIgnoreCase(workFlowAction))
            return waterConnectionDetails.getState().getNextAction();

        return null;
    }

    public String getReglnForwardPendingAction(String loggedInUserDesignation) {
        if (DEPUTY_ENGINEER_DESIGN.equalsIgnoreCase(loggedInUserDesignation))
            return WF_STATE_DEE_FORWARD_PENDING;
        else if (EXECUTIVE_ENGINEER_DESIGN.equalsIgnoreCase(loggedInUserDesignation))
            return WF_STATE_EE_FORWARD_PENDING;
        else if (SUPERINTENDING_ENGINEER_DESIGNATION.equalsIgnoreCase(loggedInUserDesignation))
            return WF_STATE_SE_FORWARD_PENDING;
        else if (MUNICIPAL_ENGINEER_DESIGN.equalsIgnoreCase(loggedInUserDesignation))
            return WF_STATE_ME_FORWARD_PENDING;
        else
            return null;
    }

    public String getReglnApprovePendingAction(String loggedInUserDesignation) {
        if (DEPUTY_ENGINEER_DESIGN.equalsIgnoreCase(loggedInUserDesignation))
            return WF_STATE_DEE_APPROVE_PENDING;
        else if (EXECUTIVE_ENGINEER_DESIGN.equalsIgnoreCase(loggedInUserDesignation))
            return WF_STATE_EE_APPROVE_PENDING;
        else if (SUPERINTENDING_ENGINEER_DESIGNATION.equalsIgnoreCase(loggedInUserDesignation))
            return WF_STATE_SE_APPROVE_PENDING;
        else if (MUNICIPAL_ENGINEER_DESIGN.equalsIgnoreCase(loggedInUserDesignation))
            return WF_STATE_ME_APPROVE_PENDING;
        else
            return null;
    }

    // water and sewerage integration
    public void prepareNewForm(Model model, WaterConnectionDetails waterConnectionDetails) {
        SewerageApplicationDetails sewerageApplicationDetails = new SewerageApplicationDetails();
        SewerageConnection connection = new SewerageConnection();
        model.addAttribute("sewerageApplicationDetails", waterConnectionDetails.getSewerageApplicationDetails());
        model.addAttribute("sewerageadditionalrule", sewerageApplicationTypeService.findByCode(SewerageTaxConstants.NEWSEWERAGECONNECTION));
        model.addAttribute("sewpropertyTypes", PropertyType.values());
        waterConnectionDetails.setSewerageApplicationDetails(sewerageApplicationDetails);
        sewerageApplicationDetails.setApplicationDate(new Date());
        connection.setStatus(SewerageConnectionStatus.INPROGRESS);
        sewerageApplicationDetails.setConnection(connection);
        SewerageApplicationType applicationType = sewerageApplicationTypeService.findByCode(SewerageTaxConstants.NEWSEWERAGECONNECTION);
        sewerageApplicationDetails.setApplicationType(applicationType);
        model.addAttribute("sewerageallowIfPTDueExists", sewerageTaxUtils.isNewConnectionAllowedIfPTDuePresent());
        model.addAttribute("seweragetypeOfConnection", SewerageTaxConstants.NEWSEWERAGECONNECTION);

    }
    
    public AssessmentDetails getPropertyDetails(final String assessmentNumber, final HttpServletRequest request) {
        final RestTemplate restTemplate = new RestTemplate();
        final String url = String.format(PTIS_DETAILS_URL, WebUtils.extractRequestDomainURL(request, false));
        return restTemplate.getForObject(url, AssessmentDetails.class,
                assessmentNumber);
    }
    
    
    public void validateConnectionCategory(final WaterConnectionDetails waterConnectionDetails, final BindingResult errors,
            HttpServletRequest request) {
        AssessmentDetails assessmentDetails = getPropertyDetails(
                waterConnectionDetails.getConnection().getPropertyIdentifier(), request);
        if (waterConnectionDetails.getCategory().getName().equalsIgnoreCase(CATEGORY_BPL)) {
            if (assessmentDetails.getPropertyDetails().getCurrentTax().compareTo(new BigDecimal(500)) >= 0) {
                String errorMessage = wcmsMessageSource.getMessage("msg.propertytax.nonBPLcategory", new String[] {},
                        Locale.getDefault());
                errors.rejectValue("category", errorMessage, errorMessage);
            }

        } else {
            if (assessmentDetails.getPropertyDetails().getCurrentTax().compareTo(new BigDecimal(500)) <= 0) {
                String errorMessage = wcmsMessageSource.getMessage("msg.propertytax.BPLcategory", new String[] {},
                        Locale.getDefault());
                errors.rejectValue("category", errorMessage, errorMessage);
            }
        }

    }
}