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
package org.egov.wtms.application.service;

import static org.egov.infra.reporting.util.ReportUtil.reportAsResponseEntity;
import static org.egov.infra.utils.DateUtils.toDefaultDateFormat;
import static org.egov.wtms.utils.constants.WaterTaxConstants.ADDNLCONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_CREATED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_DIGITALSIGNPENDING;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_ESTIMATENOTICEGEN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPROVEWORKFLOWACTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.CLOSURECONN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.CLOSURE_ESTIMATION_NOTICE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.CONNECTION_WORK_ORDER;
import static org.egov.wtms.utils.constants.WaterTaxConstants.DESG_COMM_NAME;
import static org.egov.wtms.utils.constants.WaterTaxConstants.FILESTORE_MODULECODE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.MODULETYPE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.MODULE_NAME;
import static org.egov.wtms.utils.constants.WaterTaxConstants.NEWCONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.PERMENENTCLOSE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.RECONNECTIONWITHSLASH;
import static org.egov.wtms.utils.constants.WaterTaxConstants.RECONNECTION_ESTIMATION_NOTICE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SIGNED_DOCUMENT_PREFIX;
import static org.egov.wtms.utils.constants.WaterTaxConstants.TEMPERARYCLOSE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WATERCHARGES_CONSUMERCODE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WCMS_SERVICE_CHARGES;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WF_SIGN_BUTTON;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.infra.admin.master.entity.AppConfig;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigService;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.reporting.engine.ReportFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.NumberToWordConverter;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.OwnerName;
import org.egov.ptis.domain.model.enums.BasicPropertyStatus;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.autonumber.EstimationNumberGenerator;
import org.egov.wtms.masters.service.ApplicationProcessTimeService;
import org.egov.wtms.utils.PropertyExtnUtils;
import org.egov.wtms.utils.WaterTaxUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportGenerationService {
    private static final Logger LOG = LoggerFactory.getLogger(ReportGenerationService.class);
    private static final String APPLICATIONTYPE = "applicationtype";
    private static final String DISTRICT = "district";
    private static final String USER_ID = "userId";
    private static final String COMMISSIONER_NAME = "commissionerName";
    private static final String SUPERVISION_CHARGES = "superVisionCharges";
    private static final String ROAD_CUTTING_CHARGES = "roadCuttingCharges";
    private static final String SECURITY_DEPOSIT = "securityDeposit";
    private static final String DONATION_CHARGES = "donationCharges";
    private static final String APPLICATION_DATE = "applicationDate";
    private static final String ADDRESS = "address";
    private static final String APPLICANT_NAME = "applicantName";
    private static final String WORK_FLOW_ACTION = "workFlowAction";
    private static final String HOUSE_NO = "houseNo";
    private static final String CITY_NAME = "cityName";
    private static final String ESTIMATION_NOTICE = "estimationNotice";
    private static final String CITIZEN_ACKNOWLDGEMENT = "citizenAcknowledgement";
    private static final String CONNTITLE = "conntitle";
    private static final String APPLICATION_TYPE = "applicationType";
    private static final String WORK_ORDER_NO = "workOrderNo";
    private static final String WORK_ORDER_DATE = "workOrderDate";
    private static final String CONNECTIONWORKORDER = "connectionWorkOrder";
    private static final String WORKORDER_GENERATE_EXCEPTION = "Exception in generating work order notice";
    private static final String CLOSURE_ACK_GENERATE_EXCEPTION = "Exception in generating closure acknowledgement";
    private static final String RECONN_ACK_GENERATE_EXCEPTION = "Exception in generating Reconnecton acknowledgement";
    private static final String MUNICIPALITY_NAME = "municipalityName";
    private static final String FROM_INSTALLMENT = "fromInstallment";
    private static final String TO_INSTALLMENT = "toInstallment";
    private static final String WATER_CHARGES = "Water Charges";
    private static final String REGULARISE_CONN_DEMAND_NOTE = "regulariseConnectionDemandNote";
    private static final String REGULARISE_CONN_PROCEEDINGS = "regulariseConnectionProceedings";
    private static final String REGULARISE_CONN_DEMAND_NOTE_EXCEPTION = "Exception in generating regularise connection demand note";
    private static final String REGULARISE_CONN_PROCEEDINGS_EXCEPTION = "Exception in generating regularise connection proceedings";
    private static final String CONSUMERNUMBER = "consumerNumber";
    private static final String DOORNO = "doorno";
    private static final String LOCALITY = "locality";

    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource wcmsMessageSource;

    @Autowired
    private PropertyExtnUtils propertyExtnUtils;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private ReportService reportService;

    @Autowired
    private WaterTaxUtils waterTaxUtils;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private UserService userService;

    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;

    @Autowired
    private CityService cityService;

    @Autowired
    private ApplicationProcessTimeService applicationProcessTimeService;

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    private DesignationService designationService;

    @Autowired
    private AppConfigService appConfigService;

    @Autowired
    private ConnectionDemandService connectionDemandService;

    @Autowired
    private AutonumberServiceBeanResolver beanResolver;

    public ReportOutput getReportOutput(final WaterConnectionDetails connectionDetails, final String workFlowAction) {
        final Map<String, Object> reportParams = new HashMap<>(0);
        ReportRequest reportInput = null;
        ReportOutput reportOutput = null;
        if (connectionDetails != null) {
            final AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
                    connectionDetails.getConnection().getPropertyIdentifier(),
                    PropertyExternalService.FLAG_FULL_DETAILS, BasicPropertyStatus.ALL);
            final String propAddress = assessmentDetails.getPropertyAddress();
            String[] doorno = null;
            double total = 0;
            if (null != propAddress && !propAddress.isEmpty())
                doorno = propAddress.split(",");
            String ownerName = "";
            Iterator<OwnerName> iterator = null;
            if (!assessmentDetails.getOwnerNames().isEmpty())
                iterator = assessmentDetails.getOwnerNames().iterator();
            if (iterator != null && iterator.hasNext())
                ownerName = iterator.next().getOwnerName();
            final List<Assignment> assignList = assignmentService
                    .findPrimaryAssignmentForDesignationName(DESG_COMM_NAME);
            String commissionerName = "";
            if (!assignList.isEmpty())
                commissionerName = assignList.get(0).getEmployee().getName();
            if (NEWCONNECTION.equalsIgnoreCase(connectionDetails.getApplicationType().getCode()))
                reportParams.put(APPLICATIONTYPE, wcmsMessageSource.getMessage("msg.new.watertap.conn", null, null));
            else if (ADDNLCONNECTION
                    .equalsIgnoreCase(connectionDetails.getApplicationType().getCode()))
                reportParams.put(APPLICATIONTYPE, wcmsMessageSource.getMessage("msg.add.watertap.conn", null, null));
            else
                reportParams.put(APPLICATIONTYPE,
                        wcmsMessageSource.getMessage("msg.changeofuse.watertap.conn", null, null));
            reportParams.put(CONNTITLE,
                    WordUtils.capitalize(connectionDetails.getApplicationType().getName()));
            reportParams.put("municipality", cityService.getMunicipalityName());
            reportParams.put(DISTRICT, cityService.getDistrictName());
            reportParams.put("purpose", connectionDetails.getUsageType().getName());
            if (workFlowAction != null)
                if (workFlowAction.equalsIgnoreCase(APPROVEWORKFLOWACTION)) {
                    reportParams.put(WORK_ORDER_DATE, toDefaultDateFormat(connectionDetails.getWorkOrderDate()));
                    reportParams.put(WORK_ORDER_NO, connectionDetails.getWorkOrderNumber());
                } else if (workFlowAction.equalsIgnoreCase(WF_SIGN_BUTTON))
                    reportParams.put(USER_ID, securityUtils.getCurrentUser().getId());

            final User user = securityUtils.getCurrentUser();
            Assignment assignment = assignmentService.getPrimaryAssignmentForUser(user.getId());
            if (assignment == null) {
                final List<Assignment> assignmentList = assignmentService.findByEmployeeAndGivenDate(user.getId(), new Date());
                if (!assignmentList.isEmpty())
                    assignment = assignmentList.get(0);
            }
            String userDesignation = null;
            if (assignment != null && assignment.getDesignation().getName().equals(DESG_COMM_NAME))
                userDesignation = assignment.getDesignation().getName();
            else
                userDesignation = null;

            reportParams.put(WORK_FLOW_ACTION, workFlowAction);
            reportParams.put(CONSUMERNUMBER, connectionDetails.getConnection().getConsumerCode());
            reportParams.put(APPLICANT_NAME, WordUtils.capitalize(ownerName));
            reportParams.put(ADDRESS, propAddress);
            reportParams.put(DOORNO, doorno == null ? "" : doorno[0]);
            reportParams.put("userSignature", securityUtils.getCurrentUser().getSignature() != null
                    ? new ByteArrayInputStream(securityUtils.getCurrentUser().getSignature()) : null);
            reportParams.put(APPLICATION_DATE, toDefaultDateFormat(connectionDetails.getApplicationDate()));
            reportParams.put(DONATION_CHARGES, connectionDetails.getDonationCharges());
            reportParams.put(SECURITY_DEPOSIT, connectionDetails.getFieldInspectionDetails().getSecurityDeposit());
            reportParams.put(ROAD_CUTTING_CHARGES,
                    connectionDetails.getFieldInspectionDetails().getRoadCuttingCharges());
            reportParams.put(SUPERVISION_CHARGES,
                    connectionDetails.getFieldInspectionDetails().getSupervisionCharges());
            reportParams.put(LOCALITY, assessmentDetails.getBoundaryDetails().getLocalityName());
            total = connectionDetails.getDonationCharges()
                    + connectionDetails.getFieldInspectionDetails().getSecurityDeposit()
                    + connectionDetails.getFieldInspectionDetails().getRoadCuttingCharges()
                    + connectionDetails.getFieldInspectionDetails().getSupervisionCharges();
            reportParams.put("total", total);
            reportParams.put(COMMISSIONER_NAME, commissionerName);
            reportParams.put("designation", userDesignation);
            reportInput = new ReportRequest(CONNECTION_WORK_ORDER, connectionDetails, reportParams);
        }
        reportOutput = reportService.createReport(reportInput);
        return reportOutput;
    }

    public ReportOutput generateReconnectionReport(final WaterConnectionDetails waterConnectionDetails,
            final String workFlowAction) {
        final Map<String, Object> reportParams = new HashMap<>();
        ReportRequest reportInput = null;
        ReportOutput reportOutput = null;
        if (waterConnectionDetails != null)
            if (waterConnectionDetails.getReconnectionFileStore() == null) {
                Assignment assignment = null;
                User user = null;
                final AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
                        waterConnectionDetails.getConnection().getPropertyIdentifier(),
                        PropertyExternalService.FLAG_FULL_DETAILS, BasicPropertyStatus.ALL);
                final String[] doorNo = assessmentDetails.getPropertyAddress().split(",");
                String ownerName = "";
                Iterator<OwnerName> iterator = null;
                if (!assessmentDetails.getOwnerNames().isEmpty())
                    iterator = assessmentDetails.getOwnerNames().iterator();
                if (iterator != null && iterator.hasNext())
                    ownerName = iterator.next().getOwnerName();
                List<Assignment> asignList = new ArrayList<>();
                final Position approverPos = waterTaxUtils.getCityLevelCommissionerPosition("Commissioner",
                        waterConnectionDetails.getConnection().getPropertyIdentifier());
                if (approverPos != null) {
                    assignment = assignmentService.getPrimaryAssignmentForPositionAndDate(approverPos.getId(), new Date());
                    if (assignment != null && assignment.getEmployee() != null) {
                        asignList = new ArrayList<>();
                        asignList.add(assignment);
                    } else if (assignment == null)
                        asignList = assignmentService.getAssignmentsForPosition(approverPos.getId(), new Date());
                    if (!asignList.isEmpty())
                        user = userService.getUserById(asignList.get(0).getEmployee().getId());
                }
                reportParams.put(APPLICATION_TYPE, WordUtils.capitalize(RECONNECTIONWITHSLASH));
                reportParams.put(CITY_NAME, cityService.getMunicipalityName());
                reportParams.put(DISTRICT, cityService.getDistrictName());
                reportParams.put(APPLICATION_DATE, toDefaultDateFormat(waterConnectionDetails.getApplicationDate()));
                reportParams.put("reconnApprovalDate",
                        toDefaultDateFormat(waterConnectionDetails.getReconnectionApprovalDate() == null
                                ? new Date() : waterConnectionDetails.getReconnectionApprovalDate()));
                reportParams.put(APPLICANT_NAME, ownerName);
                reportParams.put(WATERCHARGES_CONSUMERCODE, waterConnectionDetails.getConnection().getConsumerCode());
                reportParams.put(COMMISSIONER_NAME,
                        user != null && user.getUsername() != null ? user.getName() : ownerName);
                reportParams.put(ADDRESS, assessmentDetails.getPropertyAddress());
                reportParams.put(HOUSE_NO, doorNo[0]);
                reportParams.put("usersignature", securityUtils.getCurrentUser().getSignature() != null
                        ? new ByteArrayInputStream(securityUtils.getCurrentUser().getSignature()) : null);
                reportParams.put(USER_ID, securityUtils.getCurrentUser().getId());
                reportParams.put(WORK_FLOW_ACTION, workFlowAction);
                reportInput = new ReportRequest(RECONNECTION_ESTIMATION_NOTICE,
                        waterConnectionDetails, reportParams);
                reportOutput = reportService.createReport(reportInput);
            } else
                reportOutput = getReconnAcknowledgement(waterConnectionDetails);
        return reportOutput;
    }

    public ReportOutput getReconnAcknowledgement(final WaterConnectionDetails waterConnectionDetails) {
        final FileStoreMapper fmp = waterConnectionDetails.getReconnectionFileStore();
        final File file = fileStoreService.fetch(fmp, FILESTORE_MODULECODE);
        ReportOutput reportOutput = new ReportOutput();
        try {
            reportOutput.setReportName(waterConnectionDetails.getApplicationNumber() + "-ReconnACK");
            reportOutput.setReportOutputData(FileUtils.readFileToByteArray(file));
            reportOutput.setReportFormat(ReportFormat.PDF);
        } catch (final IOException e) {
            LOG.error(RECONN_ACK_GENERATE_EXCEPTION, e);
        }
        return reportOutput;
    }

    public ReportOutput generateClosureConnectionReport(final WaterConnectionDetails waterConnectionDetails,
            final String workFlowAction) {
        final Map<String, Object> reportParams = new HashMap<>();
        ReportRequest reportInput = null;
        ReportOutput reportOutput = null;
        if (waterConnectionDetails != null)
            if (waterConnectionDetails.getClosureFileStore() == null) {
                final AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
                        waterConnectionDetails.getConnection().getPropertyIdentifier(),
                        PropertyExternalService.FLAG_FULL_DETAILS, BasicPropertyStatus.ALL);
                final String[] doorNo = assessmentDetails.getPropertyAddress().split(",");
                final StringBuilder ownerName = new StringBuilder();
                for (final OwnerName names : assessmentDetails.getOwnerNames()) {
                    if (assessmentDetails.getOwnerNames().size() > 1)
                        ownerName.append(", ");
                    ownerName.append(names.getOwnerName());
                }
                reportParams.put(APPLICATION_TYPE, WordUtils.capitalize(CLOSURECONN));
                reportParams.put(CITY_NAME, cityService.getMunicipalityName());
                reportParams.put(DISTRICT, cityService.getDistrictName());
                reportParams.put(APPLICATION_DATE, toDefaultDateFormat(waterConnectionDetails.getApplicationDate()));
                reportParams.put(APPLICANT_NAME, ownerName);
                reportParams.put(WATERCHARGES_CONSUMERCODE, waterConnectionDetails.getConnection().getConsumerCode());
                reportParams.put(ADDRESS, assessmentDetails.getPropertyAddress());
                reportParams.put(HOUSE_NO, doorNo[0]);
                reportParams.put("usersignature", securityUtils.getCurrentUser().getSignature() != null
                        ? new ByteArrayInputStream(securityUtils.getCurrentUser().getSignature()) : null);
                reportParams.put("closeApprovalDate", toDefaultDateFormat(waterConnectionDetails.getCloseApprovalDate() != null
                        ? waterConnectionDetails.getCloseApprovalDate() : new Date()));
                reportParams.put("closeConnectionType",
                        waterConnectionDetails.getCloseConnectionType().equals("T") ? "Temporary" : "Permanent");
                reportParams.put(USER_ID, securityUtils.getCurrentUser().getId());
                reportParams.put(WORK_FLOW_ACTION, workFlowAction);
                reportInput = new ReportRequest(CLOSURE_ESTIMATION_NOTICE,
                        waterConnectionDetails.getEstimationDetails(), reportParams);
                reportOutput = reportService.createReport(reportInput);

            } else
                reportOutput = getClosureAcknowledgement(waterConnectionDetails);
        return reportOutput;
    }

    public ReportOutput getClosureAcknowledgement(WaterConnectionDetails waterConnectionDetails) {
        final FileStoreMapper fmp = waterConnectionDetails.getClosureFileStore();
        final File file = fileStoreService.fetch(fmp, FILESTORE_MODULECODE);
        ReportOutput reportOutput = new ReportOutput();
        try {
            reportOutput.setReportOutputData(FileUtils.readFileToByteArray(file));
            reportOutput.setReportFormat(ReportFormat.PDF);
            reportOutput.setReportName(waterConnectionDetails.getApplicationNumber() + "-ClosureACK");
        } catch (final IOException e) {
            LOG.error(CLOSURE_ACK_GENERATE_EXCEPTION, e);
        }
        return reportOutput;
    }

    public ReportOutput generateEstimationNoticeReport(final WaterConnectionDetails waterConnectionDetails,
            final String cityMunicipalityName, final String districtName) {
        ReportRequest reportInput = null;
        if (waterConnectionDetails != null) {
            final Map<String, Object> reportParams = new HashMap<>();
            final AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
                    waterConnectionDetails.getConnection().getPropertyIdentifier(),
                    PropertyExternalService.FLAG_FULL_DETAILS, BasicPropertyStatus.ACTIVE);
            final String[] doorNo = assessmentDetails.getPropertyAddress().split(",");
            final StringBuilder ownerName = new StringBuilder();

            for (final OwnerName names : assessmentDetails.getOwnerNames()) {
                if (assessmentDetails.getOwnerNames().size() > 1)
                    ownerName.append(", ");
                ownerName.append(names.getOwnerName());
            }

            reportParams.put(APPLICATION_TYPE,
                    WordUtils.capitalize(waterConnectionDetails.getApplicationType().getName()));
            reportParams.put(CITY_NAME, cityMunicipalityName);
            reportParams.put(DISTRICT, districtName);
            reportParams.put("estimationDate",
                    toDefaultDateFormat(waterConnectionDetails.getFieldInspectionDetails().getCreatedDate()));
            reportParams.put("estimationNumber", waterConnectionDetails.getEstimationNumber());
            reportParams.put(DONATION_CHARGES, waterConnectionDetails.getDonationCharges());
            final double totalCharges = waterConnectionDetails.getDonationCharges()
                    + waterConnectionDetails.getFieldInspectionDetails().getSupervisionCharges()
                    + waterConnectionDetails.getFieldInspectionDetails().getRoadCuttingCharges()
                    + waterConnectionDetails.getFieldInspectionDetails().getSecurityDeposit();
            reportParams.put("totalCharges", totalCharges);
            reportParams.put(APPLICATION_DATE, toDefaultDateFormat(waterConnectionDetails.getApplicationDate()));
            reportParams.put(APPLICANT_NAME, ownerName.toString());
            reportParams.put(ADDRESS, assessmentDetails.getPropertyAddress());
            reportParams.put(HOUSE_NO, doorNo[0]);
            reportParams.put("propertyID", waterConnectionDetails.getConnection().getPropertyIdentifier());
            reportParams.put("amountInWords", getTotalAmntInWords(totalCharges));
            reportParams.put(SECURITY_DEPOSIT,
                    waterConnectionDetails.getFieldInspectionDetails().getSecurityDeposit());
            reportParams.put(ROAD_CUTTING_CHARGES,
                    waterConnectionDetails.getFieldInspectionDetails().getRoadCuttingCharges());
            reportParams.put(SUPERVISION_CHARGES,
                    waterConnectionDetails.getFieldInspectionDetails().getSupervisionCharges());
            reportInput = new ReportRequest(ESTIMATION_NOTICE, waterConnectionDetails, reportParams);
        }
        ReportOutput reportOutput;
        reportOutput = reportService.createReport(reportInput);
        return reportOutput;
    }

    public String getTotalAmntInWords(final Double totalCharges) {
        return NumberToWordConverter.amountInWordsWithCircumfix(BigDecimal.valueOf(totalCharges));
    }

    public ReportRequest generateCitizenAckReport(final WaterConnectionDetails waterConnectionDetails,final String sewApplicationNum) {
        ReportRequest reportInput = null;
        final Map<String, Object> reportParams = new HashMap<>();
        if (waterConnectionDetails != null) {
            final AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
                    waterConnectionDetails.getConnection().getPropertyIdentifier(),
                    PropertyExternalService.FLAG_FULL_DETAILS, BasicPropertyStatus.ACTIVE);
            final String[] doorNo = assessmentDetails.getPropertyAddress().split(",");
            reportParams.put(HOUSE_NO, doorNo[0]);
            String ownerName = "";
            Iterator<OwnerName> iterator = null;
            if (!assessmentDetails.getOwnerNames().isEmpty())
                iterator = assessmentDetails.getOwnerNames().iterator();
            if (iterator != null && iterator.hasNext())
                ownerName = iterator.next().getOwnerName();
            reportParams.put(APPLICANT_NAME, ownerName);
            final Integer appProcessTime = applicationProcessTimeService.getApplicationProcessTime(
                    waterConnectionDetails.getApplicationType(), waterConnectionDetails.getCategory());
            if (StringUtils.isBlank(sewApplicationNum)) {
                reportParams.put("sewerageApplicationNo", null);
            } else {
                reportParams.put("sewerageApplicationNo", sewApplicationNum);
            }

            if (appProcessTime == null)
                reportParams.put("applicationDueDate", null);
            else
                reportParams.put("applicationDueDate",
                        toDefaultDateFormat(
                                waterConnectionDetailsService.getDisposalDate(waterConnectionDetails, appProcessTime)));
            reportParams.put(ADDRESS, assessmentDetails.getPropertyAddress());
            reportParams.put("electionWard", assessmentDetails.getBoundaryDetails().getAdminWardName());
            reportInput = setReportParameters(reportParams, waterConnectionDetails);
        }
        return reportInput;
    }

    public ReportRequest setReportParameters(final Map<String, Object> reportParams,
            final WaterConnectionDetails waterConnectionDetails) {
        final String districtName = cityService.getDistrictName();
        reportParams.put("cityUrl", (cityService.findAll().isEmpty() ? districtName.toLowerCase()
                : cityService.findAll().get(0).getName().toLowerCase()) + ".cdma.ap.gov.in");
        reportParams.put(APPLICATION_TYPE,
                WordUtils.capitalize(waterConnectionDetails.getApplicationType().getName()));
        reportParams.put(CITY_NAME, cityService.getMunicipalityName());
        reportParams.put(DISTRICT, districtName);
        reportParams.put("applicationNumber", waterConnectionDetails.getApplicationNumber());
        reportParams.put(APPLICATION_DATE, toDefaultDateFormat(waterConnectionDetails.getApplicationDate()));

        reportParams.put("propertyID", waterConnectionDetails.getConnection().getPropertyIdentifier());

        if (waterConnectionDetails.getCloseConnectionType() != null)
            if ("T".equals(waterConnectionDetails.getCloseConnectionType()))
                waterConnectionDetails.setCloseConnectionType(TEMPERARYCLOSE);
            else if ("P".equals(waterConnectionDetails.getCloseConnectionType()))
                waterConnectionDetails.setCloseConnectionType(PERMENENTCLOSE);
        reportParams.put("closeconnectiontype", waterConnectionDetails.getCloseConnectionType());
        if (waterConnectionDetails.getCloseconnectionreason() != null)
            reportParams.put("closeconnectionreason", waterConnectionDetails.getCloseconnectionreason());
        if (waterConnectionDetails.getReConnectionReason() != null)
            reportParams.put("reconnectionreason", waterConnectionDetails.getReConnectionReason());
        return new ReportRequest(CITIZEN_ACKNOWLDGEMENT, waterConnectionDetails, reportParams);
    }

    public ReportOutput generateWorkOrderNotice(final WaterConnectionDetails connectionDetails, final String workFlowAction) {
        final ReportRequest reportInput = null;
        ReportOutput reportOutput = null;
        if (connectionDetails != null)
            if (connectionDetails.getFileStore() == null) {
                final Map<String, Object> reportParams = new HashMap<>();
                final AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
                        connectionDetails.getConnection().getPropertyIdentifier(),
                        PropertyExternalService.FLAG_FULL_DETAILS, BasicPropertyStatus.ACTIVE);
                final String[] doorno = assessmentDetails.getPropertyAddress().split(",");
                reportParams.put(DOORNO, doorno[0]);
                String ownerName = "";
                String commissionerName = "";
                Iterator<OwnerName> iterator = null;
                if (!assessmentDetails.getOwnerNames().isEmpty())
                    iterator = assessmentDetails.getOwnerNames().iterator();
                if (iterator != null && iterator.hasNext())
                    ownerName = iterator.next().getOwnerName();
                reportParams.put(APPLICANT_NAME, WordUtils.capitalize(ownerName));
                final Designation designation = designationService.getDesignationByName(DESG_COMM_NAME);
                if (designation != null) {
                    final List<Assignment> assignList = assignmentService.getAllActiveAssignments(designation.getId());
                    commissionerName = assignList.isEmpty() ? "" : assignList.get(0).getEmployee().getName();
                }

                if (NEWCONNECTION.equalsIgnoreCase(connectionDetails.getApplicationType().getCode())) {
                    reportParams.put(CONNTITLE,
                            WordUtils.capitalize(connectionDetails.getApplicationType().getName()));
                    reportParams.put(APPLICATION_TYPE, wcmsMessageSource.getMessage("msg.new.watertap.conn", null, null));
                } else if (ADDNLCONNECTION
                        .equalsIgnoreCase(connectionDetails.getApplicationType().getCode())) {
                    reportParams.put(CONNTITLE,
                            WordUtils.capitalize(connectionDetails.getApplicationType().getName()));
                    reportParams.put(APPLICATION_TYPE, wcmsMessageSource.getMessage("msg.add.watertap.conn", null, null));
                } else {
                    reportParams.put(CONNTITLE,
                            WordUtils.capitalize(connectionDetails.getApplicationType().getName()));
                    reportParams.put(APPLICATION_TYPE,
                            wcmsMessageSource.getMessage("msg.changeofuse.watertap.conn", null, null));
                }
                reportParams.put("municipality", cityService.getMunicipalityName());
                reportParams.put(DISTRICT, cityService.getDistrictName());
                reportParams.put("purpose", connectionDetails.getUsageType().getName());
                final User user = securityUtils.getCurrentUser();
                Assignment assignment = assignmentService.getPrimaryAssignmentForUser(user.getId());
                if (assignment == null) {
                    final List<Assignment> assignmentList = assignmentService.findByEmployeeAndGivenDate(user.getId(),
                            new Date());
                    if (!assignmentList.isEmpty())
                        assignment = assignmentList.get(0);
                }
                String userDesignation = null;
                if (assignment != null && assignment.getDesignation().getName().equals(DESG_COMM_NAME))
                    userDesignation = assignment.getDesignation().getName();
                else
                    userDesignation = null;
                reportParams.put("designation", userDesignation);
                if (workFlowAction != null)
                    if (workFlowAction.equalsIgnoreCase(APPROVEWORKFLOWACTION)) {
                        reportParams.put(WORK_ORDER_DATE, toDefaultDateFormat(connectionDetails.getWorkOrderDate()));
                        reportParams.put(WORK_ORDER_NO, connectionDetails.getWorkOrderNumber());
                    } else if (workFlowAction.equalsIgnoreCase(WF_SIGN_BUTTON))
                        reportParams.put(USER_ID, securityUtils.getCurrentUser().getId());
                reportParams.put(WORK_FLOW_ACTION, workFlowAction);
                reportParams.put(ADDRESS, assessmentDetails.getPropertyAddress());
                reportParams.put(LOCALITY, assessmentDetails.getBoundaryDetails().getLocalityName());
                reportParams.put(COMMISSIONER_NAME, commissionerName);
                setReportParameters(reportParams, connectionDetails);
                reportOutput = reportService.createReport(reportInput);
                reportOutput.setReportName(connectionDetails.getWorkOrderNumber());
                reportOutput.setReportFormat(ReportFormat.PDF);
            } else
                reportOutput = getWorkOrderNotice(connectionDetails);
        return reportOutput;

    }

    public ReportOutput getWorkOrderNotice(final WaterConnectionDetails waterConnectionDetails) {

        final FileStoreMapper fmp = waterConnectionDetails.getFileStore();
        final File file = fileStoreService.fetch(fmp, FILESTORE_MODULECODE);
        final ReportOutput reportOutput = new ReportOutput();
        try {
            reportOutput.setReportOutputData(FileUtils.readFileToByteArray(file));
            reportOutput.setReportName(waterConnectionDetails.getWorkOrderNumber());
            reportOutput.setReportFormat(ReportFormat.PDF);
        } catch (final IOException e) {
            LOG.error(WORKORDER_GENERATE_EXCEPTION, e);
        }
        return reportOutput;
    }

    public ReportRequest setReportParams(final Map<String, Object> reportParams, final WaterConnectionDetails connectionDetails) {
        double total;
        reportParams.put(CONSUMERNUMBER, connectionDetails.getConnection().getConsumerCode());
        reportParams.put("applicantionDate", toDefaultDateFormat(connectionDetails.getApplicationDate()));
        reportParams.put("userSignature", securityUtils.getCurrentUser().getSignature() == null
                ? null : new ByteArrayInputStream(securityUtils.getCurrentUser().getSignature()));
        reportParams.put(APPLICATION_DATE, toDefaultDateFormat(connectionDetails.getApplicationDate()));
        reportParams.put(DONATION_CHARGES, connectionDetails.getDonationCharges());
        reportParams.put(SECURITY_DEPOSIT, connectionDetails.getFieldInspectionDetails().getSecurityDeposit());
        reportParams.put(ROAD_CUTTING_CHARGES,
                connectionDetails.getFieldInspectionDetails().getRoadCuttingCharges());
        reportParams.put(SUPERVISION_CHARGES,
                connectionDetails.getFieldInspectionDetails().getSupervisionCharges());

        total = connectionDetails.getDonationCharges()
                + connectionDetails.getFieldInspectionDetails().getSecurityDeposit()
                + connectionDetails.getFieldInspectionDetails().getRoadCuttingCharges()
                + connectionDetails.getFieldInspectionDetails().getSupervisionCharges();
        reportParams.put("total", total);
        return new ReportRequest(CONNECTIONWORKORDER, connectionDetails, reportParams);
    }

    public ReportOutput generateRegulariseConnDemandNote(final WaterConnectionDetails waterConnectionDetails) {
        ReportOutput reportOutput = null;
        ReportRequest reportRequest = null;
        if (waterConnectionDetails.getEstimationNoticeFileStoreId() == null) {
            Map<String, Object> reportParams = new HashMap<>();

            EstimationNumberGenerator estimationNumberGenerator;
            if (waterConnectionDetails.getEstimationNumber() == null) {
                estimationNumberGenerator = beanResolver.getAutoNumberServiceFor(EstimationNumberGenerator.class);
                waterConnectionDetails.setEstimationNumber(estimationNumberGenerator.generateEstimationNumber());
                waterConnectionDetails.setEstimationNoticeDate(new Date());
            }
            reportParams = setReglnConnCommonReportParameters(reportParams, waterConnectionDetails);
            reportParams.put("noticeNumber", waterConnectionDetails.getEstimationNumber());
            reportRequest = new ReportRequest(REGULARISE_CONN_DEMAND_NOTE, waterConnectionDetails, reportParams);
            reportOutput = reportService.createReport(reportRequest);
            saveRegulariseConnDemandNote(waterConnectionDetails, reportOutput);
            waterConnectionDetailsService.updateIndexes(waterConnectionDetails, null);
        } else
            reportOutput = getRegulariseConnDemandNote(waterConnectionDetails.getEstimationNoticeFileStoreId());
        reportOutput.setReportName(waterConnectionDetails.getEstimationNumber());
        reportOutput.setReportFormat(ReportFormat.PDF);
        return reportOutput;
    }

    public Map<String, Object> setReglnConnCommonReportParameters(final Map<String, Object> reportParams,
            final WaterConnectionDetails waterConnectionDetails) {

        final AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
                waterConnectionDetails.getConnection().getPropertyIdentifier(), PropertyExternalService.FLAG_FULL_DETAILS,
                BasicPropertyStatus.ACTIVE);
        final String[] doorno = assessmentDetails.getPropertyAddress().split(",");
        reportParams.put(DOORNO, doorno[0]);

        Iterator<OwnerName> iterator = null;
        if (!assessmentDetails.getOwnerNames().isEmpty())
            iterator = assessmentDetails.getOwnerNames().iterator();
        if (iterator != null && iterator.hasNext()) {
            reportParams.put("mobileNumber", assessmentDetails.getOwnerNames().iterator().next().getMobileNumber());
            reportParams.put(APPLICANT_NAME, WordUtils.capitalize(iterator.next().getOwnerName()));
        }
        final Designation designation = designationService.getDesignationByName(DESG_COMM_NAME);
        if (designation != null) {
            List<Assignment> assignmentsList = assignmentService.getAllActiveAssignments(designation.getId());
            reportParams.put(COMMISSIONER_NAME,
                    assignmentsList.isEmpty() ? "" : assignmentsList.get(0).getEmployee().getName());
        }
        reportParams.put(CONSUMERNUMBER, waterConnectionDetails.getConnection().getConsumerCode());
        reportParams.put("applicationNumber", waterConnectionDetails.getApplicationNumber());
        reportParams.put(APPLICATION_TYPE, waterConnectionDetails.getApplicationType().getName());
        reportParams.put(DISTRICT, cityService.getDistrictName());
        reportParams.put(MUNICIPALITY_NAME, cityService.getMunicipalityName());
        reportParams.put(CITY_NAME, cityService.getMunicipalityName());
        reportParams.put("assessmentNumber", waterConnectionDetails.getConnection().getPropertyIdentifier());
        reportParams.put("date", toDefaultDateFormat(new Date()));
        reportParams.put(LOCALITY, assessmentDetails.getBoundaryDetails().getLocalityName());
        reportParams.put(ADDRESS, assessmentDetails.getPropertyAddress());
        reportParams.put("electionWard", assessmentDetails.getBoundaryDetails().getAdminWardName());
        reportParams.put("revenueWard", assessmentDetails.getBoundaryDetails().getWardName());
        reportParams.put(DONATION_CHARGES, BigDecimal.valueOf(waterConnectionDetails.getDonationCharges()));
        Map<String, String> resultMap = connectionDemandService.getMonthlyWaterChargesDue(waterConnectionDetails);
        BigDecimal waterCharges = BigDecimal.valueOf(Double.parseDouble(resultMap.get(WATER_CHARGES))) == null ? BigDecimal.ZERO
                : BigDecimal.valueOf(Double.parseDouble(resultMap.get(WATER_CHARGES)));
        reportParams.put("waterCharges", waterCharges);
        reportParams.put(FROM_INSTALLMENT, resultMap.get(FROM_INSTALLMENT));
        reportParams.put(TO_INSTALLMENT, resultMap.get(TO_INSTALLMENT));
        reportParams.put("penaltyCharges", BigDecimal.valueOf(waterConnectionDetails.getDonationCharges()));
        AppConfig appConfig = appConfigService.getAppConfigByModuleNameAndKeyName(MODULE_NAME, WCMS_SERVICE_CHARGES);
        BigDecimal serviceCharges = appConfig.getConfValues().isEmpty() ? BigDecimal.ZERO
                : BigDecimal.valueOf(Long.parseLong(appConfig.getConfValues().get(0).getValue()));
        reportParams.put("serviceCharges", serviceCharges);
        BigDecimal totalCharges = BigDecimal.valueOf(waterConnectionDetails.getDonationCharges())
                .add(BigDecimal.valueOf(waterConnectionDetails.getDonationCharges()))
                .add(serviceCharges)
                .add(waterCharges);
        reportParams.put("totalCharges", totalCharges);
        reportParams.put("amountInWords", getTotalAmntInWords(totalCharges.doubleValue()));
        return reportParams;

    }

    public ReportOutput getRegulariseConnDemandNote(final FileStoreMapper fileStoreMapper) {
        final File file = fileStoreService.fetch(fileStoreMapper, FILESTORE_MODULECODE);
        ReportOutput reportOutput = new ReportOutput();
        try {
            reportOutput.setReportOutputData(FileUtils.readFileToByteArray(file));
            reportOutput.setReportFormat(ReportFormat.PDF);
        } catch (IOException e) {
            throw new ApplicationRuntimeException(REGULARISE_CONN_DEMAND_NOTE_EXCEPTION + e);
        }
        return reportOutput;
    }

    @Transactional
    public void saveRegulariseConnDemandNote(WaterConnectionDetails waterConnectionDetails, ReportOutput reportOutput) {
        if (reportOutput != null) {
            String fileName;
            fileName = SIGNED_DOCUMENT_PREFIX + waterConnectionDetails.getEstimationNumber() + ".pdf";
            InputStream fileStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
            FileStoreMapper fileStore = fileStoreService.store(fileStream, fileName, "application/pdf", FILESTORE_MODULECODE);
            waterConnectionDetails.setEstimationNoticeFileStoreId(fileStore);
            if (APPLICATION_STATUS_CREATED.equalsIgnoreCase(waterConnectionDetails.getStatus().getCode()))
                waterConnectionDetails.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_ESTIMATENOTICEGEN,
                        MODULETYPE));
            waterConnectionDetailsService.save(waterConnectionDetails);
        }
    }

    public ReportOutput generateRegulariseConnProceedings(final WaterConnectionDetails waterConnectionDetails) {
        ReportOutput reportOutput = null;
        ReportRequest reportRequest = null;
        if (waterConnectionDetails.getFileStore() == null) {
            Map<String, Object> reportParams = new HashMap<>();
            reportParams = setReglnConnCommonReportParameters(reportParams, waterConnectionDetails);
            reportParams.put("noticeNumber", waterConnectionDetails.getWorkOrderNumber());
            reportRequest = new ReportRequest(REGULARISE_CONN_PROCEEDINGS, waterConnectionDetails, reportParams);
            reportOutput = reportService.createReport(reportRequest);
            saveRegulariseConnProceedings(waterConnectionDetails, reportOutput);
        } else
            reportOutput = getRegularizationConnProceedings(waterConnectionDetails.getFileStore());
        reportOutput.setReportName(waterConnectionDetails.getWorkOrderNumber());
        reportOutput.setReportFormat(ReportFormat.PDF);
        return reportOutput;
    }

    public ReportOutput getRegularizationConnProceedings(final FileStoreMapper fileStoreMapper) {
        final File file = fileStoreService.fetch(fileStoreMapper, FILESTORE_MODULECODE);
        ReportOutput outputObject = new ReportOutput();
        try {
            outputObject.setReportOutputData(FileUtils.readFileToByteArray(file));
            outputObject.setReportFormat(ReportFormat.PDF);
        } catch (IOException e) {
            throw new ApplicationRuntimeException(REGULARISE_CONN_PROCEEDINGS_EXCEPTION + e);
        }
        return outputObject;
    }

    @Transactional
    public void saveRegulariseConnProceedings(final WaterConnectionDetails waterConnectionDetails, ReportOutput reportOutput) {
        if (reportOutput != null) {
            String fileName;
            fileName = SIGNED_DOCUMENT_PREFIX + waterConnectionDetails.getWorkOrderNumber() + ".pdf";
            InputStream fileInputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
            FileStoreMapper fileStore = fileStoreService.store(fileInputStream, fileName, "application/pdf",
                    FILESTORE_MODULECODE);
            waterConnectionDetails.setFileStore(fileStore);
            waterConnectionDetails
                    .setStatus(waterTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_DIGITALSIGNPENDING, MODULETYPE));
            waterConnectionDetailsService.save(waterConnectionDetails);
        }
    }

    public ResponseEntity<InputStreamResource> generateReport(final WaterConnectionDetails waterConnectionDetails,final String sewerageApplicationNum ) {
        ReportOutput reportOutput = reportService.createReport(generateCitizenAckReport(waterConnectionDetails,sewerageApplicationNum));
        reportOutput.setReportFormat(ReportFormat.PDF);
        reportOutput.setReportName(waterConnectionDetails.getApplicationNumber());
        return reportAsResponseEntity(reportOutput);
    }
}
