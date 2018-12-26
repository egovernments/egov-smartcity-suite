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
import static org.egov.wtms.masters.entity.enums.ConnectionStatus.INPROGRESS;
import static org.egov.wtms.utils.constants.WaterTaxConstants.ADDNLCONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.AE_APPROVAL_PENDING;
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
import static org.egov.wtms.utils.constants.WaterTaxConstants.CATEGORY_BPL;
import static org.egov.wtms.utils.constants.WaterTaxConstants.CHANGEOFUSE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.CLOSECONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.CLOSINGCONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.COMMISSIONER_DESGN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.COMM_APPROVAL_PENDING;
import static org.egov.wtms.utils.constants.WaterTaxConstants.CONNECTIONTYPE_METERED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.DEMANDRSN_CODE_ADVANCE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.DEPUTY_ENGINEER_DESIGN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.EGMODULES_NAME;
import static org.egov.wtms.utils.constants.WaterTaxConstants.END;
import static org.egov.wtms.utils.constants.WaterTaxConstants.EXECUTIVE_ENGINEER_DESIGN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.FILESTORE_MODULECODE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.FORWARDWORKFLOWACTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.JUNIOR_ASSISTANT_DESIGN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.JUNIOR_OR_SENIOR_ASSISTANT_DESIGN_REVENUE_CLERK;
import static org.egov.wtms.utils.constants.WaterTaxConstants.METERED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.METERED_CHARGES_REASON_CODE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.MODULETYPE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.MODULE_NAME;
import static org.egov.wtms.utils.constants.WaterTaxConstants.MUNICIPAL_ENGINEER_DESIGN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.NEWCONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.NON_METERED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.NON_METERED_CODE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.PENDING_DIGI_SIGN_BY_COMM;
import static org.egov.wtms.utils.constants.WaterTaxConstants.PENDING_DIGI_SIGN_BY_DEE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.PENDING_DIGI_SIGN_BY_EE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.PENDING_DIGI_SIGN_BY_ME;
import static org.egov.wtms.utils.constants.WaterTaxConstants.PENDING_DIGI_SIGN_BY_SE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.PROPERTY_MODULE_NAME;
import static org.egov.wtms.utils.constants.WaterTaxConstants.PTIS_DETAILS_URL;
import static org.egov.wtms.utils.constants.WaterTaxConstants.RECONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.REGULARIZE_CONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SENIOR_ASSISTANT_DESIGN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SIGNED_DOCUMENT_PREFIX;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SIGNWORKFLOWACTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.STATUS;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SUBMITWORKFLOWACTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SUPERINTENDING_ENGINEER_DESIGNATION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SYSTEM;
import static org.egov.wtms.utils.constants.WaterTaxConstants.TAP_INSPPECTOR_DESIGN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.TEMPERARYCLOSECODE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.USERNAME_MEESEVA;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WATERTAXREASONCODE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WATERTAX_CONNECTION_CHARGE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WFLOW_ACTION_STEP_CANCEL;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WFLOW_ACTION_STEP_REJECT;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WF_STATE_AE_APPROVAL_PENDING;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WF_STATE_AE_REJECTION_PENDING;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WF_STATE_BUTTON_GENERATEESTIMATE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WF_STATE_CLERK_APPROVED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WF_STATE_DEE_APPROVE_PENDING;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WF_STATE_DEE_FORWARD_PENDING;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WF_STATE_EE_APPROVE_PENDING;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WF_STATE_ME_APPROVE_PENDING;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WF_STATE_PENDING_FORWARD_BY_EE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WF_STATE_PENDING_FORWARD_BY_ME;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WF_STATE_PENDING_FORWARD_BY_SE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WF_STATE_REJECTED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WF_STATE_SE_APPROVE_PENDING;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WF_STATE_TAP_EXECUTION_DATE_BUTTON;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WORKFLOW_RECONNCTIONINITIATED;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
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
import org.egov.demand.model.EgDemandDetails;
import org.egov.eis.entity.Assignment;
import org.egov.eis.entity.AssignmentAdaptor;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.Department;
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
import org.egov.wtms.masters.entity.ConnectionAddress;
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
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;
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
	private static final String INVALID_EXECUTION_DATE = "InvalidExecutionDate";

	@Autowired
	@Qualifier("fileStoreService")
	protected FileStoreService fileStoreService;

	@Autowired
	protected WaterConnectionDetailsRepository waterConnectionDetailsRepository;

	@Autowired
	private ModuleService moduleDao;

	@Autowired
	private UserService userService;

	@Autowired
	private ApplicationContext context;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private SecurityUtils securityUtils;

	@Autowired
	private WaterTaxUtils waterTaxUtils;

	@Autowired
	private PropertyExtnUtils propertyExtnUtils;

	@Autowired
	private EisCommonService eisCommonService;

	@Autowired
	private AssignmentService assignmentService;

	@Autowired
	private PortalInboxService portalInboxService;

	@Autowired
	private MeterCostService meterCostService;

	@Autowired
	private FinancialYearDAO financialYearDAO;

	@Autowired
	private SewerageTaxUtils sewerageTaxUtils;

	@Autowired
	@Qualifier("parentMessageSource")
	private MessageSource wcmsMessageSource;

	@Autowired
	private DocumentNamesService documentNamesService;

	@Autowired
	private WaterTaxNumberGenerator waterTaxNumberGenerator;

	@Autowired
	private WaterChargeDocumentService waterChargeIndexService;

	@Autowired
	private ConnectionDemandService connectionDemandService;

	@Autowired
	private ApplicationTypeService applicationTypeService;

	@Autowired
	private ApplicationIndexService applicationIndexService;

	@Autowired
	private ConnectionAddressService connectionAddressService;

	@Autowired
	private ApplicationNumberGenerator applicationNumberGenerator;

	@Autowired
	private SewerageApplicationTypeService sewerageApplicationTypeService;

	@Autowired
	private ApplicationProcessTimeService applicationProcessTimeService;

	@Autowired
	private WaterConnectionSmsAndEmailService waterConnectionSmsAndEmailService;

	@Autowired
	@Qualifier("workflowService")
	private SimpleWorkflowService<WaterConnectionDetails> waterConnectionWorkflowService;

	public WaterConnectionDetails findBy(Long waterConnectionId) {
		return waterConnectionDetailsRepository.findOne(waterConnectionId);
	}

	public List<WaterConnectionDetails> findAll() {
		return waterConnectionDetailsRepository.findAll(new Sort(Sort.Direction.ASC, APPLICATION_NUMBER));
	}

	public WaterConnectionDetails findByApplicationNumber(String applicationNumber) {
		return waterConnectionDetailsRepository.findByApplicationNumber(applicationNumber);
	}

	public WaterConnectionDetails findByConsumerCode(String consumerCode) {
		return waterConnectionDetailsRepository.findByConnectionConsumerCode(consumerCode);
	}

	public WaterConnectionDetails load(Long id) {
		return waterConnectionDetailsRepository.getOne(id);
	}

	public Session getCurrentSession() {
		return entityManager.unwrap(Session.class);
	}

	@Transactional
	public WaterConnectionDetails createNewWaterConnection(WaterConnectionDetails waterConnectionDetails,
			Long approvalPosition, String approvalComent, String additionalRule, String workFlowAction) {
		if (isBlank(waterConnectionDetails.getApplicationNumber()))
			waterConnectionDetails.setApplicationNumber(applicationNumberGenerator.generate());
		waterConnectionDetails.setApplicationDate(new Date());

		Integer appProcessTime = applicationProcessTimeService.getApplicationProcessTime(
				waterConnectionDetails.getApplicationType(), waterConnectionDetails.getCategory());
		if (appProcessTime != null)
			waterConnectionDetails.setDisposalDate(getDisposalDate(waterConnectionDetails, appProcessTime));

		WaterConnectionDetails savedWaterConnectionDetails = waterConnectionDetailsRepository
				.save(waterConnectionDetails);
		connectionAddressService.createConnectionAddress(savedWaterConnectionDetails);
		User meesevaUser = userService.getUserById(waterConnectionDetails.getCreatedBy().getId());
		if (USERNAME_MEESEVA.equals(meesevaUser.getUsername())) {
			setUserId(meesevaUser.getId());
			savedWaterConnectionDetails.setCreatedBy(meesevaUser);
		}

		ApplicationWorkflowCustomDefaultImpl applicationWorkflow = getInitialisedWorkFlowBean();
		applicationWorkflow.createCommonWorkflowTransition(savedWaterConnectionDetails, approvalPosition,
				approvalComent, additionalRule, workFlowAction);

		if (waterTaxUtils.isCitizenPortalUser(securityUtils.getCurrentUser()))
			pushPortalMessage(savedWaterConnectionDetails);

		updateIndexes(savedWaterConnectionDetails);
		waterConnectionSmsAndEmailService.sendSmsAndEmail(waterConnectionDetails, workFlowAction);
		return savedWaterConnectionDetails;
	}

	@Transactional
	public WaterConnectionDetails createExisting(WaterConnectionDetails waterConnectionDetails) {

		if (waterConnectionDetails.getConnection() != null
				&& isBlank(waterConnectionDetails.getConnection().getConsumerCode()))
			waterConnectionDetails.getConnection().setConsumerCode(waterTaxNumberGenerator.getNextConsumerNumber());

		waterConnectionDetails.getExistingConnection().setWaterConnectionDetails(waterConnectionDetails);
		waterConnectionDetails.setApplicationNumber(waterConnectionDetails.getConnection().getConsumerCode());
		waterConnectionDetails.setApplicationDate(waterConnectionDetails.getExecutionDate());
		waterConnectionDetails
				.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_SANCTIONED, MODULETYPE));

		if (waterConnectionDetails.getApplicationType().getCode().equalsIgnoreCase(ADDNLCONNECTION)) {
			List<WaterConnectionDetails> connectionDetailsList = getPrimaryConnectionDetailsByPropertyIdentifier(
					waterConnectionDetails.getConnection().getPropertyIdentifier());
			WaterConnectionDetails primaryConnectionDetails;
			if (!connectionDetailsList.isEmpty()) {
				primaryConnectionDetails = connectionDetailsList.get(0);
				waterConnectionDetails.getConnection().setParentConnection(primaryConnectionDetails.getConnection());
			}
		}
		WaterConnectionDetails savedWaterConnectionDetails = waterConnectionDetailsRepository
				.save(waterConnectionDetails);
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
				.findConnectionDetailsByApplicationNumberOrConsumerCodeAndConnectionStatus(number, number,
						connectionStatus);
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
		return waterConnectionDetailsRepository
				.findParentConnectionDetailsByConsumerCodeAndConnectionStatus(consumerCode, connectionStatus);
	}

	public WaterConnectionDetails findByOldConsumerNumberAndConnectionStatus(String oldConsumerNumber,
			ConnectionStatus connectionStatus) {
		return waterConnectionDetailsRepository.findByConnectionOldConsumerNumberAndConnectionStatus(oldConsumerNumber,
				connectionStatus);
	}

	public WaterConnectionDetails getActiveConnectionDetailsByConnection(WaterConnection waterConnection) {
		return waterConnectionDetailsRepository.findByConnectionAndConnectionStatus(waterConnection, ACTIVE);
	}

	public List<WaterConnectionDetails> getPrimaryConnectionDetailsByPropertyIdentifier(String propertyIdentifier) {
		return waterConnectionDetailsRepository.getAllPrimaryConnectionDetailsByPropertyID(propertyIdentifier);
	}

	public WaterConnectionDetails getPrimaryConnectionDetailsByPropertyAssessmentNumbers(
			List<String> propertyIdentifier) {
		WaterConnectionDetails waterConnectionDetails = null;
		for (String assessmentNumber : propertyIdentifier) {
			waterConnectionDetails = waterConnectionDetailsRepository
					.getActivePrimaryConnectionDetailsByPropertyID(assessmentNumber);
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

	public List<WaterConnectionDetails> getAllConnectionDetailsExceptInactiveStatusByPropertyID(
			String propertyIdentifier) {
		return waterConnectionDetailsRepository
				.getAllConnectionDetailsExceptInactiveStatusByPropertyID(propertyIdentifier);
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
			getUserAndDepartmentHistory(state.getOwnerUser(), null, map, state.getOwnerPosition());
			historyTable.add(map);

			if (!waterConnectionDetails.getStateHistory().isEmpty())
				Collections.reverse(waterConnectionDetails.getStateHistory());
			for (StateHistory<Position> stateHistory : waterConnectionDetails.getStateHistory()) {
				HashMap<String, Object> historyMap = new HashMap<>(0);
				historyMap.put("date", stateHistory.getDateInfo());
				historyMap.put("comments", stateHistory.getComments() == null ? EMPTY : stateHistory.getComments());
				User lastUser = stateHistory.getLastModifiedBy();
				historyMap.put("updatedBy", lastUser.getUsername() + "::" + lastUser.getName());
				historyMap.put(STATUS, stateHistory.getValue());
				Position owner = stateHistory.getOwnerPosition();
				user = stateHistory.getOwnerUser();
				getUserAndDepartmentHistory(user, stateHistory, historyMap, owner);
				historyTable.add(historyMap);
			}
		}
		return historyTable;
	}

	private void getUserAndDepartmentHistory(User user, StateHistory<Position> stateHistory,
			HashMap<String, Object> historyMap, Position owner) {
		Department department;
		if (user != null) {
			historyMap.put("user", user.getUsername() + "::" + user.getName());
			department = eisCommonService.getDepartmentForUser(user.getId());
			historyMap.put(DEPARTMENT, department == null ? EMPTY : department.getName());
		} else if (owner != null && owner.getDeptDesig() != null) {
			user = eisCommonService.getUserForPosition(owner.getId(),
					stateHistory == null ? new Date() : stateHistory.getLastModifiedDate());
			department = owner.getDeptDesig().getDepartment();
			historyMap.put("user", user == null ? EMPTY : user.getUsername() + "::" + user.getName());
			historyMap.put(DEPARTMENT, department == null ? EMPTY : department.getName());
		}
	}

	@Transactional
	public WaterConnectionDetails updateWaterConnection(WaterConnectionDetails waterConnectionDetails,
			Long approvalPosition, String approvalComent, String additionalRule, String workFlowAction, String mode,
			ReportOutput reportOutput, String sourceChannel) {

		applicationStatusChange(waterConnectionDetails, workFlowAction, mode);
		if (APPLICATION_STATUS_CLOSERDIGSIGNPENDING.equals(waterConnectionDetails.getStatus().getCode())
				&& waterConnectionDetails.getCloseConnectionType() != null
				&& APPROVEWORKFLOWACTION.equals(workFlowAction)) {
			waterConnectionDetails.setApplicationType(applicationTypeService.findByCode(CLOSINGCONNECTION));
			waterConnectionDetails.setCloseApprovalDate(new Date());
		}

		if (APPLICATION_STATUS_RECONNDIGSIGNPENDING.equals(waterConnectionDetails.getStatus().getCode())
				&& TEMPERARYCLOSECODE.equals(waterConnectionDetails.getCloseConnectionType())
				&& waterConnectionDetails.getReConnectionReason() != null
				&& APPROVEWORKFLOWACTION.equals(workFlowAction)) {
			waterConnectionDetails.setApplicationType(applicationTypeService.findByCode(RECONNECTION));
			waterConnectionDetails.setConnectionStatus(ACTIVE);
			waterConnectionDetails.setReconnectionApprovalDate(new Date());
			if (ConnectionType.NON_METERED.equals(waterConnectionDetails.getConnectionType())) {
				Installment nonMeterReconnInstallment;
				boolean reconnInSameInstallment;
				if (checkTwoDatesAreInSameInstallment(waterConnectionDetails)) {
					Installment nonMeterCurrentInstallment = connectionDemandService.getCurrentInstallment(
							PROPERTY_MODULE_NAME, null, waterConnectionDetails.getReconnectionApprovalDate());
					Date newDateForNextInstall;
					int numberOfMonths = 6;
					if (noOfMonthsBetween(waterConnectionDetails.getReconnectionApprovalDate(),
							financialYearDAO.getFinancialYearByDate(new Date()).getEndingDate()) >= numberOfMonths)
						newDateForNextInstall = org.apache.commons.lang3.time.DateUtils
								.addDays(nonMeterCurrentInstallment.getToDate(), 1);
					else
						newDateForNextInstall = waterConnectionDetails.getReconnectionApprovalDate();
					nonMeterReconnInstallment = connectionDemandService.getCurrentInstallment(PROPERTY_MODULE_NAME,
							null, newDateForNextInstall);
					reconnInSameInstallment = true;
				} else {
					nonMeterReconnInstallment = connectionDemandService.getCurrentInstallment(PROPERTY_MODULE_NAME,
							null, waterConnectionDetails.getReconnectionApprovalDate());
					reconnInSameInstallment = false;
				}
				connectionDemandService.updateDemandForNonmeteredConnection(waterConnectionDetails,
						nonMeterReconnInstallment, reconnInSameInstallment, null);
			}
			updateIndexes(waterConnectionDetails);
		}

		if (ConnectionType.NON_METERED.equals(waterConnectionDetails.getConnectionType())
				&& APPLICATION_STATUS_SANCTIONED.equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())) {
			connectionDemandService.updateDemandForNonmeteredConnection(waterConnectionDetails, null, null,
					workFlowAction);
			updateIndexes(waterConnectionDetails);
		}

		// Setting FileStoreMap object object while Commissioner Sign's the
		// document
		if (SIGNWORKFLOWACTION.equalsIgnoreCase(workFlowAction) && reportOutput != null) {
			String fileName = SIGNED_DOCUMENT_PREFIX + waterConnectionDetails.getWorkOrderNumber() + ".pdf";
			InputStream fileStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
			FileStoreMapper fileStore = fileStoreService.store(fileStream, fileName, "application/pdf",
					FILESTORE_MODULECODE);
			waterConnectionDetails.setFileStore(fileStore);
		}

		WaterConnectionDetails updatedWaterConnectionDetails = waterConnectionDetailsRepository
				.save(waterConnectionDetails);
		ApplicationWorkflowCustomDefaultImpl applicationWorkflowCustomDefaultImpl = getInitialisedWorkFlowBean();
		if (waterConnectionDetails.getCloseConnectionType() != null)
			additionalRule = CLOSECONNECTION;

		if (waterConnectionDetails.getReConnectionReason() != null)
			additionalRule = RECONNECTION;
		applicationWorkflowCustomDefaultImpl.createCommonWorkflowTransition(updatedWaterConnectionDetails,
				approvalPosition, approvalComent, additionalRule, workFlowAction);

		// To backUpdate waterConnectiondetails after ClosureConnection is
		// cancelled
		if (waterConnectionDetails.getCloseConnectionType() != null
				&& waterConnectionDetails.getReConnectionReason() == null
				&& waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_CANCELLED)
				&& waterConnectionDetails.getConnectionStatus().equals(INACTIVE)) {
			waterConnectionDetails
					.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_SANCTIONED, MODULETYPE));
			waterConnectionDetails.setConnectionStatus(ACTIVE);
			waterConnectionDetails.setCloseConnectionType(null);
			waterConnectionDetails.setCloseconnectionreason(null);
			waterConnectionDetails.setApplicationType(
					applicationTypeService.findByCode(waterConnectionDetails.getPreviousApplicationType()));
			updateIndexes(waterConnectionDetails);
			updatedWaterConnectionDetails = waterConnectionDetailsRepository.save(waterConnectionDetails);
		}
		// back to CLoserSanctioned Status if Reconnection is Rejected 2 times
		if (waterConnectionDetails.getReConnectionReason() != null
				&& waterConnectionDetails.getCloseConnectionType() == TEMPERARYCLOSECODE
				&& waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_CANCELLED)
				&& waterConnectionDetails.getConnectionStatus().equals(INACTIVE)) {
			waterConnectionDetails.setConnectionStatus(CLOSED);
			waterConnectionDetails.setReConnectionReason(null);
			waterConnectionDetails.setStatus(
					waterTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_CLOSERSANCTIONED, MODULETYPE));
			waterConnectionDetails.setApplicationType(applicationTypeService.findByCode(CLOSINGCONNECTION));
			updateIndexes(waterConnectionDetails);
			updatedWaterConnectionDetails = waterConnectionDetailsRepository.save(waterConnectionDetails);
		}
		if (!WFLOW_ACTION_STEP_REJECT.equalsIgnoreCase(workFlowAction))
			waterConnectionSmsAndEmailService.sendSmsAndEmail(waterConnectionDetails, workFlowAction);

		updateIndexes(waterConnectionDetails);
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

		Installment nonMeterClosedInstallment = connectionDemandService.getCurrentInstallment(PROPERTY_MODULE_NAME,
				null, waterConnectionDetails.getCloseApprovalDate());
		Installment nonMeterReconnInstallment = connectionDemandService.getCurrentInstallment(PROPERTY_MODULE_NAME,
				null, waterConnectionDetails.getReconnectionApprovalDate());
		return nonMeterClosedInstallment.getDescription().equals(nonMeterReconnInstallment.getDescription());
	}

	public void applicationStatusChange(WaterConnectionDetails waterConnectionDetails, String workFlowAction,
			String mode) {

		if (waterConnectionDetails == null)
			throw new ValidationException("err.application.not.exist");
		else if (waterTaxUtils.checkWithApplicationType(waterConnectionDetails.getApplicationType().getCode()))
			applicationStatusUpdate(waterConnectionDetails, workFlowAction);
		else if (RECONNECTION.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode()))
			reconnectionStatusUpdate(waterConnectionDetails, workFlowAction);
		else if (CLOSINGCONNECTION.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode()))
			closureStatusUpdate(waterConnectionDetails, workFlowAction, mode);

	}

	public void applicationStatusUpdate(WaterConnectionDetails waterConnectionDetails, String workFlowAction) {

		if (APPLICATION_STATUS_CREATED.equals(waterConnectionDetails.getStatus().getCode())
				&& waterConnectionDetails.getState() != null && SUBMITWORKFLOWACTION.equals(workFlowAction))
			waterConnectionDetails
					.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_VERIFIED, MODULETYPE));
		else if (WF_STATE_BUTTON_GENERATEESTIMATE.equals(workFlowAction)
				&& APPLICATION_STATUS_VERIFIED.equals(waterConnectionDetails.getStatus().getCode()))
			waterConnectionDetails.setStatus(
					waterTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_ESTIMATENOTICEGEN, MODULETYPE));
		else if (APPROVEWORKFLOWACTION.equals(workFlowAction)
				&& APPLICATION_STATUS_FEEPAID.equals(waterConnectionDetails.getStatus().getCode()))
			waterConnectionDetails.setStatus(
					waterTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_DIGITALSIGNPENDING, MODULETYPE));
		else if (FORWARDWORKFLOWACTION.equalsIgnoreCase(workFlowAction)
				&& WF_STATE_REJECTED.equalsIgnoreCase(waterConnectionDetails.getState().getValue())
				&& APPLICATION_STATUS_ESTIMATENOTICEGEN.equalsIgnoreCase(waterConnectionDetails.getStatus().getCode()))
			waterConnectionDetails
					.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_CREATED, MODULETYPE));
		else if (SIGNWORKFLOWACTION.equals(workFlowAction)
				&& APPLICATION_STATUS_DIGITALSIGNPENDING.equals(waterConnectionDetails.getStatus().getCode())
				&& REGULARIZE_CONNECTION.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode())) {
			waterConnectionDetails.setConnectionStatus(ACTIVE);
			waterConnectionDetails
					.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_SANCTIONED, MODULETYPE));
		} else if (SIGNWORKFLOWACTION.equals(workFlowAction)
				&& APPLICATION_STATUS_DIGITALSIGNPENDING.equals(waterConnectionDetails.getStatus().getCode()))
			waterConnectionDetails
					.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_APPROVED, MODULETYPE));
	}

	public void closureStatusUpdate(WaterConnectionDetails waterConnectionDetails, String workFlowAction, String mode) {

		if (APPLICATION_STATUS_SANCTIONED.equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())
				&& isNotBlank(waterConnectionDetails.getCloseConnectionType()))
			waterConnectionDetails.setStatus(
					waterTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_CLOSERINITIATED, MODULETYPE));
		else if (!"closeredit".equals(mode) && isNotBlank(waterConnectionDetails.getCloseConnectionType())
				&& APPLICATION_STATUS_CLOSERINITIATED.equalsIgnoreCase(waterConnectionDetails.getStatus().getCode()))
			waterConnectionDetails.setStatus(
					waterTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_CLOSERINPROGRESS, MODULETYPE));
		else if (APPROVEWORKFLOWACTION.equals(workFlowAction)
				&& isNotBlank(waterConnectionDetails.getCloseConnectionType())
				&& APPLICATION_STATUS_CLOSERINPROGRESS.equalsIgnoreCase(waterConnectionDetails.getStatus().getCode()))
			waterConnectionDetails.setStatus(
					waterTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_CLOSERDIGSIGNPENDING, MODULETYPE));
		else if (SIGNWORKFLOWACTION.equals(workFlowAction)
				&& isNotBlank(waterConnectionDetails.getCloseConnectionType())
				&& APPLICATION_STATUS_CLOSERDIGSIGNPENDING
						.equalsIgnoreCase(waterConnectionDetails.getStatus().getCode()))
			waterConnectionDetails.setStatus(
					waterTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_CLOSERSANCTIONED, MODULETYPE));
	}

	public void reconnectionStatusUpdate(WaterConnectionDetails waterConnectionDetails, String workFlowAction) {

		if (APPLICATION_STATUS_CLOSERSANCTIONED.equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())
				&& isNotBlank(waterConnectionDetails.getCloseConnectionType())
				&& TEMPERARYCLOSECODE.equals(waterConnectionDetails.getCloseConnectionType()))
			waterConnectionDetails
					.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(WORKFLOW_RECONNCTIONINITIATED, MODULETYPE));
		else if (WORKFLOW_RECONNCTIONINITIATED.equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())
				&& TEMPERARYCLOSECODE.equals(waterConnectionDetails.getCloseConnectionType()))
			waterConnectionDetails.setStatus(
					waterTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_RECONNCTIONINPROGRESS, MODULETYPE));
		else if (APPROVEWORKFLOWACTION.equals(workFlowAction)
				&& APPLICATION_STATUS_RECONNCTIONINPROGRESS
						.equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())
				&& TEMPERARYCLOSECODE.equals(waterConnectionDetails.getCloseConnectionType()))
			waterConnectionDetails.setStatus(
					waterTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_RECONNDIGSIGNPENDING, MODULETYPE));
		else if (SIGNWORKFLOWACTION.equals(workFlowAction)
				&& APPLICATION_STATUS_RECONNDIGSIGNPENDING
						.equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())
				&& TEMPERARYCLOSECODE.equals(waterConnectionDetails.getCloseConnectionType()))
			waterConnectionDetails.setStatus(
					waterTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_RECONNCTIONSANCTIONED, MODULETYPE));
	}

	public Long getApprovalPositionByMatrixDesignation(WaterConnectionDetails waterConnectionDetails,
			Long approvalPosition, String additionalRule, String mode, String workFlowAction) {

		String loggedInUserDesignation = waterTaxUtils.loggedInUserDesignation(waterConnectionDetails);
		String pendingAction = null;
		Long approverPosition = 0l;
		if (REGULARIZE_CONNECTION.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode()))
			pendingAction = getReglnConnectionPendingAction(waterConnectionDetails, loggedInUserDesignation,
					workFlowAction);

		WorkFlowMatrix wfmatrix;
		String connectionStatusCode = waterConnectionDetails.getStatus() == null ? EMPTY
				: waterConnectionDetails.getStatus().getCode();
		if (isNotBlank(loggedInUserDesignation)
				&& (TAP_INSPPECTOR_DESIGN.equalsIgnoreCase(loggedInUserDesignation)
						|| ASSISTANT_ENGINEER_DESIGN.equalsIgnoreCase(loggedInUserDesignation)
						|| waterTaxUtils.currentUserIsApprover(loggedInUserDesignation))
				&& checkConnectionStatusAndWorkflowAction(workFlowAction, connectionStatusCode))
			wfmatrix = waterConnectionWorkflowService.getWfMatrix(waterConnectionDetails.getStateType(), null, null,
					additionalRule, waterConnectionDetails.getCurrentState().getValue(), pendingAction, null,
					loggedInUserDesignation);
		else
			wfmatrix = waterConnectionWorkflowService.getWfMatrix(waterConnectionDetails.getStateType(), null, null,
					additionalRule, waterConnectionDetails.getCurrentState().getValue(), pendingAction,
					REGULARIZE_CONNECTION.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode())
							? waterConnectionDetails.getApplicationDate() : null);
		if (APPLICATION_STATUS_ESTIMATENOTICEGEN.equals(connectionStatusCode))
			approverPosition = waterTaxUtils.getApproverPosition(JUNIOR_OR_SENIOR_ASSISTANT_DESIGN_REVENUE_CLERK,
					waterConnectionDetails);
		if (WFLOW_ACTION_STEP_CANCEL.equalsIgnoreCase(workFlowAction))
			approverPosition = waterConnectionDetails.getState().getOwnerPosition().getId();
		else if (waterConnectionDetails.getStatus() != null)
			approverPosition = getApprovalPosition(waterConnectionDetails, approvalPosition, mode, workFlowAction,
					wfmatrix);
		return approverPosition == 0l ? approvalPosition : approverPosition;
	}

	private boolean checkConnectionStatusAndWorkflowAction(String workFlowAction, String connectionStatusCode) {
		return Arrays.asList(FORWARDWORKFLOWACTION, APPROVEWORKFLOWACTION, WFLOW_ACTION_STEP_REJECT)
				.contains(
						workFlowAction)
				&& Arrays.asList(APPLICATION_STATUS_VERIFIED, APPLICATION_STATUS_CREATED,
						APPLICATION_STATUS_WOGENERATED, APPLICATION_STATUS_CLOSERINPROGRESS,
						APPLICATION_STATUS_RECONNCTIONINPROGRESS, APPLICATION_STATUS_FEEPAID)
						.contains(connectionStatusCode);
	}

	private Long getApprovalPosition(WaterConnectionDetails waterConnectionDetails, Long approvalPosition, String mode,
			String workFlowAction, WorkFlowMatrix wfmatrix) {

		Long approverPosition = 0l;
		String connectionStatusCode = waterConnectionDetails.getStatus().getCode();
		if (waterConnectionDetails.getState() != null) {

			State currentState = waterConnectionDetails.getState();
			if (APPLICATION_STATUS_CREATED.equals(connectionStatusCode))
				if ("edit".equals(mode) && !waterConnectionDetails.getStateHistory().isEmpty())
					approverPosition = currentState.getOwnerPosition().getId();
				else
					approverPosition = waterTaxUtils.getApproverPosition(wfmatrix.getNextDesignation(),
							waterConnectionDetails);

			else if (APPLICATION_STATUS_APPROVED.equals(connectionStatusCode)
					&& isNotBlank(wfmatrix.getNextDesignation())
					|| isNotBlank(workFlowAction) && WFLOW_ACTION_STEP_REJECT.equals(workFlowAction)
							&& APPLICATION_STATUS_CLOSERINITIATED.equals(connectionStatusCode)
							&& WF_STATE_REJECTED.equals(currentState.getValue()))
				approverPosition = waterTaxUtils.getApproverPosition(wfmatrix.getNextDesignation(),
						waterConnectionDetails);
			else if (waterTaxUtils.validateWorkflow(workFlowAction)
					&& (APPLICATION_STATUS_FEEPAID.equals(connectionStatusCode)
							|| APPLICATION_STATUS_DIGITALSIGNPENDING.equals(connectionStatusCode)
							|| APPLICATION_STATUS_CLOSERINPROGRESS.equals(connectionStatusCode)
							|| APPLICATION_STATUS_RECONNCTIONINPROGRESS.equals(connectionStatusCode)
							|| APPLICATION_STATUS_APPROVED.equals(connectionStatusCode)))
				approverPosition = currentState.getOwnerPosition().getId();
			else if (wfmatrix != null && isNotBlank(wfmatrix.getNextDesignation())
					&& !WFLOW_ACTION_STEP_REJECT.equals(workFlowAction)
					&& (APPLICATION_STATUS_CLOSERINITIATED.equals(connectionStatusCode)
							|| !WF_STATE_REJECTED.equals(currentState.getValue())
									&& (WORKFLOW_RECONNCTIONINITIATED.equals(connectionStatusCode)
											|| APPLICATION_STATUS_RECONNCTIONINPROGRESS.equals(connectionStatusCode))
							|| APPLICATION_STATUS_APPROVED.equals(connectionStatusCode))) {
				Position posObj = waterTaxUtils.getCityLevelCommissionerPosition(wfmatrix.getNextDesignation(),
						waterConnectionDetails.getConnection().getPropertyIdentifier());
				if (posObj != null)
					approverPosition = posObj.getId();
			}
		}
		if (wfmatrix != null && isNotBlank(wfmatrix.getNextDesignation())
				&& !waterTaxUtils.validateWorkflow(workFlowAction)
				&& (APPLICATION_STATUS_FEEPAID.equals(connectionStatusCode)
						|| APPLICATION_STATUS_VERIFIED.equals(connectionStatusCode)
						|| APPLICATION_STATUS_CLOSERDIGSIGNPENDING.equals(connectionStatusCode)
						|| APPLICATION_STATUS_DIGITALSIGNPENDING.equals(connectionStatusCode)
						|| APPLICATION_STATUS_RECONNCTIONAPPROVED.equals(connectionStatusCode)
						|| WFLOW_ACTION_STEP_REJECT.equals(workFlowAction)
								&& (WORKFLOW_RECONNCTIONINITIATED.equals(connectionStatusCode)
										|| APPLICATION_STATUS_CLOSERINITIATED.equals(connectionStatusCode))))
			approverPosition = waterTaxUtils.getApproverPosition(wfmatrix.getNextDesignation(), waterConnectionDetails);
		return approverPosition == 0l ? approvalPosition : approverPosition;
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

		return waterChargeIndexService.createWaterChargeIndex(waterConnectionDetails, assessmentDetails,
				amountTodisplayInIndex);
	}

	@Transactional
	public void updateIndexes(WaterConnectionDetails waterConnectionDetails) {

		AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
				waterConnectionDetails.getConnection().getPropertyIdentifier(),
				PropertyExternalService.FLAG_FULL_DETAILS, BasicPropertyStatus.ALL);
		BigDecimal amountTodisplayInIndex = ZERO;
		if (waterConnectionDetails.getConnection().getConsumerCode() != null)
			amountTodisplayInIndex = getTotalAmountTillCurrentFinYear(waterConnectionDetails);

		String connectionStatusCode = waterConnectionDetails.getStatus().getCode();
		if ((SURVEY.equals(waterConnectionDetails.getSource()) || waterConnectionDetails.getLegacy())
				&& APPLICATION_STATUS_SANCTIONED.equals(connectionStatusCode)) {
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
				consumerName.append(',')
						.append(secondaryOwner.getOwnerName() == null ? EMPTY : secondaryOwner.getOwnerName());
				if (isBlank(mobileNumber.toString()))
					mobileNumber.append(',').append(
							secondaryOwner.getMobileNumber() == null ? EMPTY : secondaryOwner.getMobileNumber());
				if (isBlank(aadharNumber.toString()))
					aadharNumber.append(',').append(
							secondaryOwner.getAadhaarNumber() == null ? EMPTY : secondaryOwner.getAadhaarNumber());
			}

		}
		List<Assignment> asignList = new ArrayList<>();
		if (waterConnectionDetails.getState() != null && waterConnectionDetails.getState().getOwnerPosition() != null) {
			assignment = assignmentService.getPrimaryAssignmentForPositionAndDate(
					waterConnectionDetails.getState().getOwnerPosition().getId(),
					waterConnectionDetails.getState().getLastModifiedDate());
			if (assignment == null)
				asignList = assignmentService.getAssignmentsForPosition(
						waterConnectionDetails.getState().getOwnerPosition().getId(),
						waterConnectionDetails.getState().getLastModifiedDate());
			else
				asignList.add(assignment);
			if (!asignList.isEmpty())
				user = userService.getUserById(asignList.get(0).getEmployee().getId());
		} else
			user = securityUtils.getCurrentUser();

		ApplicationIndex applicationIndex = applicationIndexService
				.findByApplicationNumber(waterConnectionDetails.getApplicationNumber());
		if (applicationIndex != null && connectionStatusCode.equals(APPLICATION_STATUS_CREATED)) {
			applicationIndex.setOwnerName(user == null ? EMPTY : user.getUsername() + "::" + user.getName());
			applicationIndexService.updateApplicationIndex(applicationIndex);
		}
		if (applicationIndex != null && waterConnectionDetails.getId() != null
				&& waterConnectionDetails.getStatus() != null
				&& !connectionStatusCode.equals(APPLICATION_STATUS_CREATED)) {
			if (waterConnectionDetails.getStatus() != null
					&& !Arrays.asList(APPLICATION_STATUS_RECONNCTIONAPPROVED, APPLICATION_STATUS_RECONNCTIONSANCTIONED,
							APPLICATION_STATUS_SANCTIONED).contains(connectionStatusCode)) {
				applicationIndex.setApplicationType(waterConnectionDetails.getApplicationType().getName());
				applicationIndex.setApplicantAddress(assessmentDetails.getPropertyAddress());
				applicationIndex.setApproved(ApprovalStatus.INPROGRESS);
				applicationIndex.setClosed(ClosureStatus.NO);
				applicationIndex.setStatus(waterConnectionDetails.getStatus().getDescription());
				if (!connectionStatusCode.equals(APPLICATION_STATUS_SANCTIONED))
					applicationIndex.setOwnerName(user == null ? EMPTY : user.getUsername() + "::" + user.getName());
				applicationIndex.setSla(applicationProcessTimeService.getApplicationProcessTime(
						waterConnectionDetails.getApplicationType(), waterConnectionDetails.getCategory()));
				if (applicationIndex.getChannel() == null)
					if (waterTaxUtils.isCSCoperator(waterConnectionDetails.getCreatedBy())
							&& UserType.BUSINESS.equals(waterConnectionDetails.getCreatedBy().getType()))
						applicationIndex.setChannel(Source.CSC.toString());
					else if (waterTaxUtils.isCitizenPortalUser(waterConnectionDetails.getCreatedBy()))
						applicationIndex.setChannel(Source.CITIZENPORTAL.toString());
					else if (waterConnectionDetails.getSource() == null)
						applicationIndex.setChannel(SYSTEM);
					else if (MEESEVA.toString().equalsIgnoreCase(waterConnectionDetails.getSource().toString()))
						applicationIndex.setChannel(MEESEVA.toString());
					else
						applicationIndex.setChannel(waterConnectionDetails.getSource().name());
			}
			if (Arrays
					.asList(APPLICATION_STATUS_RECONNCTIONSANCTIONED, APPLICATION_STATUS_APPROVED,
							APPLICATION_STATUS_SANCTIONED, APPLICATION_STATUS_CLOSERSANCTIONED)
					.contains(connectionStatusCode)) {
				applicationIndex.setStatus(connectionStatusCode);
				applicationIndex.setApproved(ApprovalStatus.APPROVED);
				applicationIndex.setClosed(ClosureStatus.YES);
				applicationIndex.setOwnerName(user == null ? EMPTY : user.getUsername() + "::" + user.getName());

			}
			if (connectionStatusCode.equals(APPLICATION_STATUS_CANCELLED)) {
				applicationIndex.setStatus(connectionStatusCode);
				applicationIndex.setApproved(ApprovalStatus.REJECTED);
				applicationIndex.setClosed(ClosureStatus.YES);
			}
			if (isNotBlank(waterConnectionDetails.getConnection().getConsumerCode()))
				applicationIndex.setConsumerCode(waterConnectionDetails.getConnection().getConsumerCode());
			applicationIndexService.updateApplicationIndex(applicationIndex);

			// Creating Consumer Index only on Sanction
			if (APPLICATION_STATUS_SANCTIONED.equals(connectionStatusCode)
					&& waterConnectionDetails.getConnectionStatus().equals(ConnectionStatus.INPROGRESS)
					&& !waterConnectionDetails.getApplicationType().getCode().equalsIgnoreCase(CHANGEOFUSE)) {
				waterConnectionDetails.setConnectionStatus(ACTIVE);
				if (!waterConnectionDetails.getConnectionStatus().equals(INACTIVE)
						|| !waterConnectionDetails.getConnectionStatus().equals(ConnectionStatus.INPROGRESS))
					createWaterChargeIndex(waterConnectionDetails, assessmentDetails, amountTodisplayInIndex);
			}
			// To Update After ClosureConnection is rejected
			if (APPLICATION_STATUS_SANCTIONED.equals(connectionStatusCode)
					&& ACTIVE.equals(waterConnectionDetails.getConnectionStatus()))
				createWaterChargeIndex(waterConnectionDetails, assessmentDetails, amountTodisplayInIndex);
			if (APPLICATION_STATUS_CLOSERSANCTIONED.equals(connectionStatusCode)
					|| APPLICATION_STATUS_CLOSERAPRROVED.equals(connectionStatusCode)
							&& CLOSED.equals(waterConnectionDetails.getConnectionStatus()))
				createWaterChargeIndex(waterConnectionDetails, assessmentDetails, amountTodisplayInIndex);

			if (isNotBlank(waterConnectionDetails.getCloseConnectionType())
					&& TEMPERARYCLOSECODE.equals(waterConnectionDetails.getCloseConnectionType())
					&& (APPLICATION_STATUS_RECONNCTIONAPPROVED.equals(connectionStatusCode)
							|| APPLICATION_STATUS_RECONNCTIONSANCTIONED.equals(connectionStatusCode))) {
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
			if (applicationIndex == null && !APPLICATION_STATUS_SANCTIONED.equals(connectionStatusCode)) {
				String channel;
				if (waterConnectionDetails.getSource() == null) {
					if (waterTaxUtils.isCSCoperator(waterConnectionDetails.getCreatedBy())
							&& UserType.BUSINESS.equals(waterConnectionDetails.getCreatedBy().getType()))
						channel = CSC.toString();
					else
						channel = SYSTEM;
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
						.withOwnername(user.getUsername() + "::" + user.getName()).withChannel(channel)
						.withMobileNumber(mobileNumber.toString()).withClosed(ClosureStatus.NO)
						.withAadharNumber(aadharNumber.toString()).withApproved(ApprovalStatus.INPROGRESS)
						.withSla(appProcessTime == null ? 0 : appProcessTime).build();
				if ((!waterConnectionDetails.getLegacy()
						|| CLOSINGCONNECTION.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode()))
						&& !APPLICATION_STATUS_SANCTIONED.equals(connectionStatusCode))
					applicationIndexService.createApplicationIndex(applicationIndex);
			}
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
		return waterConnectionDetailsRepository.findByConnectionAndConnectionStatusAndIsHistory(waterConnection, ACTIVE,
				Boolean.FALSE);
	}

	public BigDecimal getTotalAmount(WaterConnectionDetails waterConnectionDetails) {
		EgDemand currentDemand = waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand();
		BigDecimal balance = ZERO;
		if (currentDemand != null) {
			List<Object> instVsAmt = connectionDemandService.getDmdCollAmtInstallmentWise(currentDemand);
			balance = getAmountByInstallment(instVsAmt);
		}
		return balance;
	}

	public BigDecimal getWaterTaxDueAmount(WaterConnectionDetails waterConnectionDetails) {
		EgDemand currentDemand = waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand();
		BigDecimal waterTaxAmount = ZERO;
		List<String> demandCodes = Arrays.asList(METERED_CHARGES_REASON_CODE, WATERTAXREASONCODE,
				DEMANDRSN_CODE_ADVANCE, WATERTAX_CONNECTION_CHARGE);
		if (currentDemand != null)
			for (EgDemandDetails demandDetails : currentDemand.getEgDemandDetails())
				if (demandCodes.contains(demandDetails.getEgDemandReason().getEgDemandReasonMaster().getCode()))
					waterTaxAmount = waterTaxAmount.add(demandDetails.getAmount()
							.subtract(demandDetails.getAmtCollected().add(demandDetails.getAmtRebate())));
		return waterTaxAmount;
	}

	public BigDecimal getTotalAmountTillCurrentFinYear(WaterConnectionDetails waterConnectionDetails) {
		EgDemand currentDemand = waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand();
		BigDecimal balance = ZERO;
		if (currentDemand != null) {
			List<Object> instVsAmt = connectionDemandService
					.getDmdCollAmtInstallmentWiseUptoCurrentFinYear(currentDemand);
			balance = getAmountByInstallment(instVsAmt);

		}
		return balance.signum() < 0 ? ZERO : balance;
	}

	public BigDecimal getCurrentDue(WaterConnectionDetails waterConnectionDetails) {
		EgDemand currentDemand = waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand();
		BigDecimal balance = ZERO;
		if (currentDemand != null) {
			List<Object> instVsAmt = connectionDemandService
					.getDmdCollAmtInstallmentWiseUptoCurrentInstallmemt(currentDemand, waterConnectionDetails);
			balance = getAmountByInstallment(instVsAmt);
		}
		return balance;
	}

	public List<ApplicationDocuments> getApplicationDocForExceptClosureAndReConnection(
			WaterConnectionDetails waterConnectionDetails) {

		List<ApplicationDocuments> tempDocList = new ArrayList<>(0);
		if (waterConnectionDetails != null)
			for (ApplicationDocuments appDoc : waterConnectionDetails.getApplicationDocs())
				if (appDoc.getDocumentNames() != null && waterTaxUtils
						.checkWithApplicationType(appDoc.getDocumentNames().getApplicationType().getCode()))
					tempDocList.add(appDoc);
		return tempDocList;
	}

	public void validateWaterRateAndDonationHeader(WaterConnectionDetails waterConnectionDetails) {
		DonationDetails donationDetails = connectionDemandService.getDonationDetails(waterConnectionDetails);
		if (donationDetails == null)
			throw new ApplicationRuntimeException("donation.combination.required");
		WaterRatesDetails waterRatesDetails = connectionDemandService
				.getWaterRatesDetailsForDemandUpdate(waterConnectionDetails);
		if (waterRatesDetails == null)
			throw new ApplicationRuntimeException("err.water.rate.not.configured.within.period");
	}

	public String getApprovalPositionOnValidate(Long approvalPositionId) {
		Assignment assignmentObj;
		List<Assignment> assignmentList = new ArrayList<>();
		if (approvalPositionId != null && approvalPositionId > 0) {
			assignmentObj = assignmentService.getPrimaryAssignmentForPositionAndDate(approvalPositionId, new Date());
			if (assignmentObj == null)
				throw new ValidationException("err.user.not.defined");

			assignmentList.add(assignmentObj);
			Gson jsonCreator = new GsonBuilder().registerTypeAdapter(Assignment.class, new AssignmentAdaptor())
					.create();
			return jsonCreator.toJson(assignmentList, new TypeToken<Collection<Assignment>>() {
			}.getType());
		}
		return "[]";
	}

	@Transactional
	public WaterConnectionDetails updateWaterConnectionDetailsWithFileStore(
			WaterConnectionDetails waterConnectionDetails) {
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
			List<Object> instVsAmt = connectionDemandService
					.getDmdCollAmtInstallmentWiseUptoPreviousFinYear(currentDemand);
			balance = getAmountByInstallment(instVsAmt);
		}
		return balance.signum() < 0 ? ZERO : balance;
	}

	private BigDecimal getAmountByInstallment(List<Object> instVsAmt) {
		BigDecimal balance = ZERO;
		BigDecimal rebateAmount = ZERO;
		for (Object object : instVsAmt) {
			Object[] ddObject = (Object[]) object;
			BigDecimal dmdAmt = new BigDecimal((Double) ddObject[2]);
			BigDecimal collAmt = ZERO;

			if (ddObject[3] != null)
				collAmt = new BigDecimal((Double) ddObject[3]);
			if (ddObject[4] != null)
				rebateAmount = new BigDecimal((Double) ddObject[4]);
			balance = balance.add(dmdAmt.subtract(collAmt.add(rebateAmount))).setScale(0, HALF_UP);
		}
		return balance;
	}

	public BigDecimal getArrearsDemand(WaterConnectionDetails waterConnectionDetails) {
		EgDemand currentDemand = waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand();
		BigDecimal balance = ZERO;
		if (currentDemand != null) {
			List<Object> instVsAmt = connectionDemandService
					.getDmdCollAmtInstallmentWiseUptoPreviousFinYear(currentDemand);
			balance = getTotalBalance(instVsAmt);
		}
		return balance.signum() < 0 ? ZERO : balance;
	}

	public BigDecimal getTotalDemandTillCurrentFinYear(WaterConnectionDetails waterConnectionDetails) {
		EgDemand currentDemand = waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand();
		BigDecimal balance = ZERO;
		if (currentDemand != null) {
			List<Object> instVsAmt = connectionDemandService
					.getDmdCollAmtInstallmentWiseUptoCurrentFinYear(currentDemand);
			balance = getTotalBalance(instVsAmt);
		}
		return balance.signum() < 0 ? ZERO : balance;
	}

	public BigDecimal getTotalBalance(List<Object> instVsAmt) {
		BigDecimal balance = ZERO;
		for (Object object : instVsAmt) {
			Object[] ddObject = (Object[]) object;
			if (ddObject[2] != null)
				balance = balance.add(new BigDecimal((Double) ddObject[2]));
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
				waterConnectionDetails.getApplicationNumber(), waterConnection.getConsumerCode(),
				waterConnection.getId(), waterConnectionDetails.getConnectionReason(),
				getDetailedMessage(waterConnectionDetails),
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
				waterConnectionDetails.getApplicationType(), waterConnectionDetails.getCategory());
		if (appProcessTime == null)
			throw new ApplicationRuntimeException("err.applicationprocesstime.undefined");

		return new DateTime().plusDays(appProcessTime).toDate();
	}

	private String getDetailedMessage(WaterConnectionDetails waterConnectionDetails) {
		Module module = moduleDao.getModuleByName(MODULE_NAME);
		StringBuilder detailedMessage = new StringBuilder();
		if (waterConnectionDetails.getApplicationType() != null)
			detailedMessage.append(APPLICATION_NO).append(waterConnectionDetails.getApplicationNumber())
					.append(REGARDING)
					.append(waterConnectionDetails.getState().getNatureOfTask() + " " + module.getDisplayName())
					.append(" in ").append(waterConnectionDetails.getStatus().getDescription()).append(" status ");
		return detailedMessage.toString();
	}

	public PortalInbox getPortalInbox(String applicationNumber) {
		return portalInboxService.getPortalInboxByApplicationNo(applicationNumber,
				moduleDao.getModuleByName(MODULE_NAME).getId());
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
				getSlaEndDate(waterConnectionDetails), waterConnectionDetails.getState(), null,
				waterConnection.getConsumerCode(),
				String.format(WTMS_APPLICATION_VIEW, waterConnectionDetails.getApplicationNumber()));
	}

	public List<Object[]> getApplicationResultList(WaterConnExecutionDetails executionDetails) {
		StringBuilder queryString = new StringBuilder();
		queryString
				.append("select conndetails.applicationnumber, conn.consumercode, mvp.ownersname, apptype.name, status.description,  ")
				.append(" conndetails.approvaldate, boundary.localname, conndetails.id, mvp.address, conndetails.pipesize from  egwtr_connection conn ")
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
		queryString
				.append("select conndetails.applicationnumber, conn.consumercode, mvp.ownersname, apptype.name, status.description,  ")
				.append(" conndetails.approvaldate, boundary.localname, conndetails.id, mvp.address,conndetails.pipesize from  egwtr_connection conn ")
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
			if (connectionDetails != null && validateDonationDetails(connectionDetails)
					&& isNotBlank(jsonObj.getString(EXECUTION_DATE))) {
				connectionDetails
						.setExecutionDate(DateUtils.toDateUsingDefaultPattern(jsonObj.getString(EXECUTION_DATE)));
				if (connectionDetails.getExecutionDate() != null)
					if (connectionDetails.getExecutionDate().compareTo(DateUtils.toDateUsingDefaultPattern(
							DateUtils.getDefaultFormattedDate(connectionDetails.getApprovalDate()))) < 0)
						status = DATE_VALIDATION_FAILED;
					else if (connectionDetails.getExecutionDate().compareTo(new Date()) > 0)
						status = INVALID_EXECUTION_DATE;
					else
						connectionDetailsList.add(connectionDetails);
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
						&& APPLICATION_STATUS_SANCTIONED.equalsIgnoreCase(waterConnectionDetails.getStatus().getCode()))
					connectionDemandService.updateDemandForNonmeteredConnection(waterConnectionDetails, null, null,
							WF_STATE_TAP_EXECUTION_DATE_BUTTON);

				waterConnectionDetailsRepository.save(waterConnectionDetails);
				waterConnectionSmsAndEmailService.sendSmsAndEmail(waterConnectionDetails, null);
				updatePortalMessage(waterConnectionDetails);
				updateIndexes(waterConnectionDetails);
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
			if (resultObject[9] instanceof BigInteger)
				details.setPipeSizeId((BigInteger) resultObject[9]);
			resultList.add(details);
		}
		return resultList;
	}

	public List<WaterConnExecutionDetails> getApplicationObjectList(List<ConnectionAddress> detailList) {
		List<WaterConnExecutionDetails> resultList = new ArrayList<>();
		for (ConnectionAddress resultObject : detailList) {
			WaterConnExecutionDetails details = new WaterConnExecutionDetails();
			if (resultObject.getWaterConnectionDetails() != null) {
				details.setApplicationNumber(resultObject.getWaterConnectionDetails().getApplicationNumber());
				details.setConsumerNumber(resultObject.getWaterConnectionDetails().getConnection().getConsumerCode());
				details.setApplicationType(resultObject.getWaterConnectionDetails().getApplicationType().getName());
				details.setApplicationStatus(resultObject.getWaterConnectionDetails().getStatus().getDescription());
				details.setApprovalDate(resultObject.getWaterConnectionDetails().getApplicationDate().toString());
				details.setId(resultObject.getWaterConnectionDetails().getId());
			}
			details.setOwnerName(resultObject.getOwnerName());
			details.setRevenueWard(resultObject.getRevenueWard().getName());
			details.setAddress(resultObject.getAddress());
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
			waterConnectionDetails
					.setExecutionDate(DateUtils.toDateUsingDefaultPattern(jsonObj.getString(EXECUTION_DATE)));
			if (waterConnectionDetails.getExecutionDate() != null)
				if (waterConnectionDetails.getExecutionDate().compareTo(DateUtils.toDateUsingDefaultPattern(
						DateUtils.getDefaultFormattedDate(waterConnectionDetails.getApprovalDate()))) < 0)
					validStatus = DATE_VALIDATION_FAILED;
				else if (waterConnectionDetails.getExecutionDate().compareTo(new Date()) > 0)
					validStatus = INVALID_EXECUTION_DATE;
				else
					applicationList.add(waterConnectionDetails);
		}
		waterConnectionDetails.getConnection().setMeterSerialNumber(jsonObj.getString(METER_SERIAL_NUMBER));
		waterConnectionDetails.getConnection().setInitialReading(Long.valueOf(jsonObj.getString(INITIAL_READING)));
		waterConnectionDetails.getConnection()
				.setMeter(meterCostService.findByMeterMake(jsonObj.getString(METER_MAKE)).get(0));
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
			updateIndexes(waterConnectionDetails);
			return true;
		}
		return false;

	}

	public WaterConnectionDetails updateApplicationStatus(WaterConnectionDetails waterConnectionDetails) {
		if (CHANGEOFUSE.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode())) {
			WaterConnectionDetails previousApplication = waterConnectionDetailsRepository
					.findConnectionDetailsByConsumerCodeAndConnectionStatus(
							waterConnectionDetails.getConnection().getConsumerCode(), ACTIVE);
			if (previousApplication != null) {
				previousApplication.setConnectionStatus(INACTIVE);
				previousApplication.setIsHistory(true);
				waterConnectionDetailsRepository.saveAndFlush(previousApplication);
			}
		}

		if (waterTaxUtils.checkWithApplicationType(waterConnectionDetails.getApplicationType().getCode())
				&& !REGULARIZE_CONNECTION.equals(waterConnectionDetails.getApplicationType().getCode())) {
			waterConnectionDetails
					.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_SANCTIONED, MODULETYPE));
			waterConnectionDetails.setConnectionStatus(ACTIVE);
		}

		return waterConnectionDetails;
	}

	public Boolean validateDonationDetails(WaterConnectionDetails waterConnectionDetails) {
		WaterRatesDetails waterRatesDetails = connectionDemandService
				.getWaterRatesDetailsForDemandUpdate(waterConnectionDetails);
		return waterRatesDetails == null ? false : true;
	}

	public String getReglnConnectionPendingAction(WaterConnectionDetails waterConnectionDetails,
			String loggedInUserDesignation, String workFlowAction) {

		String connectionStatusCode = waterConnectionDetails.getStatus().getCode();
		if (APPLICATION_STATUS_CREATED.equalsIgnoreCase(connectionStatusCode) && isBlank(workFlowAction)
				&& WF_STATE_REJECTED.equalsIgnoreCase(waterConnectionDetails.getState().getValue())
				&& Arrays.asList(JUNIOR_ASSISTANT_DESIGN, SENIOR_ASSISTANT_DESIGN).contains(loggedInUserDesignation))
			return WF_STATE_AE_APPROVAL_PENDING;
		else if ((APPLICATION_STATUS_FEEPAID.equals(connectionStatusCode)
				|| APPLICATION_STATUS_DIGITALSIGNPENDING.equals(connectionStatusCode))
				&& (FORWARDWORKFLOWACTION.equals(workFlowAction) || APPROVEWORKFLOWACTION.equals(workFlowAction)))
			return getReglnPendingAction(workFlowAction, loggedInUserDesignation, connectionStatusCode);
		else if (APPLICATION_STATUS_CREATED.equalsIgnoreCase(connectionStatusCode)
				&& WFLOW_ACTION_STEP_REJECT.equalsIgnoreCase(workFlowAction))
			return WF_STATE_AE_REJECTION_PENDING;
		else if (APPLICATION_STATUS_CREATED.equalsIgnoreCase(connectionStatusCode)
				&& FORWARDWORKFLOWACTION.equalsIgnoreCase(workFlowAction) && loggedInUserDesignation != null)
			return WF_STATE_AE_APPROVAL_PENDING;
		else if ((APPLICATION_STATUS_FEEPAID.equalsIgnoreCase(connectionStatusCode)
				|| APPLICATION_STATUS_CREATED.equalsIgnoreCase(connectionStatusCode))
				&& WF_STATE_CLERK_APPROVED.equalsIgnoreCase(waterConnectionDetails.getState().getValue())
				&& isBlank(workFlowAction) && isNotBlank(loggedInUserDesignation))
			return AE_APPROVAL_PENDING;
		else if (APPLICATION_STATUS_NEW.equalsIgnoreCase(waterConnectionDetails.getState().getValue())
				&& FORWARDWORKFLOWACTION.equalsIgnoreCase(workFlowAction))
			return waterConnectionDetails.getState().getNextAction();
		else if (APPLICATION_STATUS_FEEPAID.equalsIgnoreCase(connectionStatusCode)
				&& DEPUTY_ENGINEER_DESIGN.equalsIgnoreCase(loggedInUserDesignation))
			return WF_STATE_DEE_FORWARD_PENDING;
		else if (APPLICATION_STATUS_DIGITALSIGNPENDING.equalsIgnoreCase(connectionStatusCode)
				&& COMMISSIONER_DESGN.equals(loggedInUserDesignation) && isBlank(workFlowAction))
			return PENDING_DIGI_SIGN_BY_COMM;
		else if (APPLICATION_STATUS_DIGITALSIGNPENDING.equalsIgnoreCase(connectionStatusCode)
				&& DEPUTY_ENGINEER_DESIGN.equals(loggedInUserDesignation) && isBlank(workFlowAction))
			return PENDING_DIGI_SIGN_BY_DEE;

		return null;
	}

	public String getReglnPendingAction(String workflowAction, String loggedInUserDesignation,
			String connectionStatusCode) {

		if (DEPUTY_ENGINEER_DESIGN.equalsIgnoreCase(loggedInUserDesignation)) {
			if (APPLICATION_STATUS_DIGITALSIGNPENDING.equalsIgnoreCase(connectionStatusCode)
					&& FORWARDWORKFLOWACTION.equals(workflowAction))
				return PENDING_DIGI_SIGN_BY_DEE;
			return FORWARDWORKFLOWACTION.equals(workflowAction) ? WF_STATE_DEE_FORWARD_PENDING
					: WF_STATE_DEE_APPROVE_PENDING;
		} else if (EXECUTIVE_ENGINEER_DESIGN.equalsIgnoreCase(loggedInUserDesignation)) {
			if (APPLICATION_STATUS_DIGITALSIGNPENDING.equalsIgnoreCase(connectionStatusCode)
					&& FORWARDWORKFLOWACTION.equals(workflowAction))
				return PENDING_DIGI_SIGN_BY_EE;
			return FORWARDWORKFLOWACTION.equals(workflowAction) ? WF_STATE_PENDING_FORWARD_BY_EE
					: WF_STATE_EE_APPROVE_PENDING;
		} else if (SUPERINTENDING_ENGINEER_DESIGNATION.equalsIgnoreCase(loggedInUserDesignation)) {
			if (APPLICATION_STATUS_DIGITALSIGNPENDING.equalsIgnoreCase(connectionStatusCode)
					&& FORWARDWORKFLOWACTION.equals(workflowAction))
				return PENDING_DIGI_SIGN_BY_SE;
			return FORWARDWORKFLOWACTION.equals(workflowAction) ? WF_STATE_PENDING_FORWARD_BY_SE
					: WF_STATE_SE_APPROVE_PENDING;
		} else if (MUNICIPAL_ENGINEER_DESIGN.equalsIgnoreCase(loggedInUserDesignation)) {
			if (APPLICATION_STATUS_DIGITALSIGNPENDING.equalsIgnoreCase(connectionStatusCode)
					&& FORWARDWORKFLOWACTION.equals(workflowAction))
				return PENDING_DIGI_SIGN_BY_ME;
			return FORWARDWORKFLOWACTION.equals(workflowAction) ? WF_STATE_PENDING_FORWARD_BY_ME
					: WF_STATE_ME_APPROVE_PENDING;
		} else if (ASSISTANT_ENGINEER_DESIGN.equalsIgnoreCase(loggedInUserDesignation))
			return FORWARDWORKFLOWACTION.equals(workflowAction) ? AE_APPROVAL_PENDING : EMPTY;
		else if (COMMISSIONER_DESGN.equalsIgnoreCase(loggedInUserDesignation)) {
			if (APPLICATION_STATUS_DIGITALSIGNPENDING.equalsIgnoreCase(connectionStatusCode)
					&& !APPROVEWORKFLOWACTION.equals(workflowAction))
				return PENDING_DIGI_SIGN_BY_COMM;
			return APPROVEWORKFLOWACTION.equals(workflowAction) ? COMM_APPROVAL_PENDING : PENDING_DIGI_SIGN_BY_COMM;
		} else
			return EMPTY;
	}

	// water and sewerage integration
	public void prepareNewForm(Model model, WaterConnectionDetails waterConnectionDetails) {
		SewerageApplicationDetails sewerageApplicationDetails = new SewerageApplicationDetails();
		SewerageConnection connection = new SewerageConnection();
		model.addAttribute("sewerageApplicationDetails", waterConnectionDetails.getSewerageApplicationDetails());
		model.addAttribute("sewerageadditionalrule",
				sewerageApplicationTypeService.findByCode(SewerageTaxConstants.NEWSEWERAGECONNECTION));
		model.addAttribute("sewpropertyTypes", PropertyType.values());
		waterConnectionDetails.setSewerageApplicationDetails(sewerageApplicationDetails);
		sewerageApplicationDetails.setApplicationDate(new Date());
		connection.setStatus(SewerageConnectionStatus.INPROGRESS);
		sewerageApplicationDetails.setConnection(connection);
		SewerageApplicationType applicationType = sewerageApplicationTypeService
				.findByCode(SewerageTaxConstants.NEWSEWERAGECONNECTION);
		sewerageApplicationDetails.setApplicationType(applicationType);
		model.addAttribute("sewerageallowIfPTDueExists", sewerageTaxUtils.isNewConnectionAllowedIfPTDuePresent());
		model.addAttribute("seweragetypeOfConnection", SewerageTaxConstants.NEWSEWERAGECONNECTION);

	}

	public AssessmentDetails getPropertyDetails(String assessmentNumber, HttpServletRequest request) {
		RestTemplate restTemplate = new RestTemplate();
		String url = String.format(PTIS_DETAILS_URL, WebUtils.extractRequestDomainURL(request, false));
		return restTemplate.getForObject(url, AssessmentDetails.class, assessmentNumber);
	}

	public void validateConnectionCategory(final WaterConnectionDetails waterConnectionDetails,
			final BindingResult errors, HttpServletRequest request) {
		if (Arrays.asList(NEWCONNECTION, ADDNLCONNECTION)
				.contains(waterConnectionDetails.getApplicationType().getCode())) {
			AssessmentDetails assessmentDetails = getPropertyDetails(
					waterConnectionDetails.getConnection().getPropertyIdentifier(), request);
			if (waterConnectionDetails.getCategory().getName().equalsIgnoreCase(CATEGORY_BPL)) {
				if (assessmentDetails.getPropertyDetails().getCurrentTax().compareTo(new BigDecimal(500)) > 0) {
					String errorMessage = wcmsMessageSource.getMessage("msg.propertytax.nonBPLcategory",
							new String[] {}, Locale.getDefault());
					errors.rejectValue("category", errorMessage, errorMessage);
				}

			} else if (assessmentDetails.getPropertyDetails().getCurrentTax().compareTo(new BigDecimal(500)) <= 0) {
				String errorMessage = wcmsMessageSource.getMessage("msg.propertytax.BPLcategory", new String[] {},
						Locale.getDefault());
				errors.rejectValue("category", errorMessage, errorMessage);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public List<ConnectionAddress> getSearchResultList(final WaterConnExecutionDetails waterConnExecutionDetails) {
		Criteria connectionAddressCriteria = getCurrentSession()
				.createCriteria(ConnectionAddress.class, "connectionAddress")
				.createAlias("connectionAddress.waterConnectionDetails", "connectionDetails")
				.createAlias("connectionAddress.revenueWard", "revenueWard")
				.createAlias("connectionDetails.applicationType", "applicationType")
				.createAlias("connectionDetails.waterDemandConnection", "demandConnection")
				.createAlias("demandConnection.demand", "demand").createAlias("connectionDetails.state", "state")
				.createAlias("connectionDetails.connection", "connection")
				.createAlias("connectionDetails.category", "category");

		Disjunction disjunction = Restrictions.disjunction();
		disjunction.add(Restrictions.eq("connectionDetails.connectionStatus", INPROGRESS));
		disjunction.add(Restrictions.eq("connectionDetails.connectionStatus", ACTIVE));
		connectionAddressCriteria.add(disjunction);
		connectionAddressCriteria.add(Restrictions.eq("connectionDetails.legacy", false));
		connectionAddressCriteria.add(Restrictions.eq("connectionDetails.isHistory", false));
		connectionAddressCriteria.add(Restrictions.ne("category.name", CATEGORY_BPL));
		connectionAddressCriteria.add(Restrictions.eq("demand.isHistory", "N"));
		Disjunction stateDisjunction = Restrictions.disjunction();
		stateDisjunction.add(Restrictions.eq("state.value", END));
		stateDisjunction.add(Restrictions.eq("state.value", APPLICATIONSTATUSCLOSED));
		connectionAddressCriteria.add(stateDisjunction);

		if (waterConnExecutionDetails.getApplicationNumber() != null)
			connectionAddressCriteria.add(Restrictions.eq("connectionDetails.applicationNumber",
					waterConnExecutionDetails.getApplicationNumber()));
		if (waterConnExecutionDetails.getConsumerNumber() != null)
			connectionAddressCriteria
					.add(Restrictions.eq("connection.consumerCode", waterConnExecutionDetails.getConsumerNumber()));
		if (waterConnExecutionDetails.getApplicationType() != null)
			connectionAddressCriteria
					.add(Restrictions.eq("applicationType.name", waterConnExecutionDetails.getApplicationType()));
		if (waterConnExecutionDetails.getRevenueWard() != null)
			connectionAddressCriteria
					.add(Restrictions.eq("revenueWard.name", waterConnExecutionDetails.getRevenueWard()));
		if (waterConnExecutionDetails.getFromDate() != null)
			connectionAddressCriteria
					.add(Restrictions.ge("connectionDetails.applicationDate", waterConnExecutionDetails.getFromDate()));
		if (waterConnExecutionDetails.getToDate() != null)
			connectionAddressCriteria
					.add(Restrictions.le("connectionDetails.applicationDate", waterConnExecutionDetails.getToDate()));
		return connectionAddressCriteria.list();
	}
}