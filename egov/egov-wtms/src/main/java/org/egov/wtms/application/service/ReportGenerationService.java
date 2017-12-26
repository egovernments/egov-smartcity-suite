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

import static org.egov.infra.utils.DateUtils.toDefaultDateFormat;
import static org.egov.wtms.utils.constants.WaterTaxConstants.FILESTORE_MODULECODE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.PERMENENTCLOSE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.TEMPERARYCLOSE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WATERCHARGES_CONSUMERCODE;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.WordUtils;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.infra.admin.master.entity.User;
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
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.OwnerName;
import org.egov.ptis.domain.model.enums.BasicPropertyStatus;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.masters.service.ApplicationProcessTimeService;
import org.egov.wtms.utils.PropertyExtnUtils;
import org.egov.wtms.utils.WaterTaxUtils;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class ReportGenerationService {

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
    public static final String ESTIMATION_NOTICE = "estimationNotice";
    public static final String CITIZEN_ACKNOWLDGEMENT = "citizenAcknowledgement";
    private static final String CONNTITLE = "conntitle";
    private static final String APPLICATION_TYPE = "applicationType";
    private static final String WORK_ORDER_NO = "workOrderNo";
    private static final String WORK_ORDER_DATE = "workOrderDate";
    public static final String CONNECTIONWORKORDER = "connectionWorkOrder";

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
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @Autowired
    private DesignationService designationService;

    public ReportOutput getReportOutput(final WaterConnectionDetails connectionDetails, final String workFlowAction,
            final String cityMunicipalityName, final String districtName) {
        final Map<String, Object> reportParams = new HashMap<>(0);
        ReportRequest reportInput = null;
        ReportOutput reportOutput = null;
        if (null != connectionDetails) {
            final AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
                    connectionDetails.getConnection().getPropertyIdentifier(),
                    PropertyExternalService.FLAG_FULL_DETAILS, BasicPropertyStatus.ALL);
            final String propAddress = assessmentDetails.getPropertyAddress();
            String doorno[] = null;
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
                    .findPrimaryAssignmentForDesignationName(WaterTaxConstants.DESG_COMM_NAME);
            String commissionerName = "";
            if (!assignList.isEmpty())
                commissionerName = assignList.get(0).getEmployee().getName();
            if (WaterTaxConstants.NEWCONNECTION.equalsIgnoreCase(connectionDetails.getApplicationType().getCode()))
                reportParams.put(APPLICATIONTYPE, wcmsMessageSource.getMessage("msg.new.watertap.conn", null, null));
            else if (WaterTaxConstants.ADDNLCONNECTION
                    .equalsIgnoreCase(connectionDetails.getApplicationType().getCode()))
                reportParams.put(APPLICATIONTYPE, wcmsMessageSource.getMessage("msg.add.watertap.conn", null, null));
            else
                reportParams.put(APPLICATIONTYPE,
                        wcmsMessageSource.getMessage("msg.changeofuse.watertap.conn", null, null));
            reportParams.put("conntitle",
                    WordUtils.capitalize(connectionDetails.getApplicationType().getName()));
            reportParams.put("municipality", cityMunicipalityName);
            reportParams.put(DISTRICT, districtName);
            reportParams.put("purpose", connectionDetails.getUsageType().getName());
            if (null != workFlowAction) {
                if (workFlowAction.equalsIgnoreCase(WaterTaxConstants.WF_WORKORDER_BUTTON)
                        || workFlowAction.equalsIgnoreCase(WaterTaxConstants.WF_SIGN_BUTTON)) {
                    reportParams.put(WORK_ORDER_DATE, toDefaultDateFormat(connectionDetails.getWorkOrderDate()));
                    reportParams.put(WORK_ORDER_NO, connectionDetails.getWorkOrderNumber());
                    if (workFlowAction.equalsIgnoreCase(WaterTaxConstants.WF_SIGN_BUTTON)) {
                        final User user = securityUtils.getCurrentUser();
                        reportParams.put(USER_ID, user.getId());
                    }
                }
                if (workFlowAction.equalsIgnoreCase(WaterTaxConstants.WF_PREVIEW_BUTTON)) {
                    reportParams.put(WORK_ORDER_DATE, "");
                    reportParams.put(WORK_ORDER_NO, "");
                }

            }

            final User user = securityUtils.getCurrentUser();
            Assignment assignment = assignmentService.getPrimaryAssignmentForUser(user.getId());
            if (assignment == null) {
                final List<Assignment> assignmentList = assignmentService.findByEmployeeAndGivenDate(user.getId(), new Date());
                if (!assignmentList.isEmpty())
                    assignment = assignmentList.get(0);
            }
            String userDesignation = null;
            if (assignment != null && assignment.getDesignation().getName().equals(WaterTaxConstants.DESG_COMM_NAME))
                userDesignation = assignment.getDesignation().getName();
            else
                userDesignation = null;

            reportParams.put(WORK_FLOW_ACTION, workFlowAction);
            reportParams.put("consumerNumber", connectionDetails.getConnection().getConsumerCode());
            reportParams.put(APPLICANT_NAME, WordUtils.capitalize(ownerName));
            reportParams.put(ADDRESS, propAddress);
            reportParams.put("doorno", doorno != null ? doorno[0] : "");
            reportParams.put("userSignature", securityUtils.getCurrentUser().getSignature() != null
                    ? new ByteArrayInputStream(securityUtils.getCurrentUser().getSignature()) : null);
            reportParams.put(APPLICATION_DATE, toDefaultDateFormat(connectionDetails.getApplicationDate()));
            reportParams.put(DONATION_CHARGES, connectionDetails.getDonationCharges());
            reportParams.put(SECURITY_DEPOSIT, connectionDetails.getFieldInspectionDetails().getSecurityDeposit());
            reportParams.put(ROAD_CUTTING_CHARGES,
                    connectionDetails.getFieldInspectionDetails().getRoadCuttingCharges());
            reportParams.put(SUPERVISION_CHARGES,
                    connectionDetails.getFieldInspectionDetails().getSupervisionCharges());
            reportParams.put("locality", assessmentDetails.getBoundaryDetails().getLocalityName());
            total = connectionDetails.getDonationCharges()
                    + connectionDetails.getFieldInspectionDetails().getSecurityDeposit()
                    + connectionDetails.getFieldInspectionDetails().getRoadCuttingCharges()
                    + connectionDetails.getFieldInspectionDetails().getSupervisionCharges();
            reportParams.put("total", total);
            reportParams.put(COMMISSIONER_NAME, commissionerName);
            reportParams.put("designation", userDesignation);
            reportInput = new ReportRequest(WaterTaxConstants.CONNECTION_WORK_ORDER, connectionDetails, reportParams);
        }
        reportOutput = reportService.createReport(reportInput);
        return reportOutput;
    }

    public ReportOutput generateReconnectionReport(final WaterConnectionDetails waterConnectionDetails,
            final String workFlowAction, final String cityMunicipalityName, final String districtName) {
        final Map<String, Object> reportParams = new HashMap<>();
        ReportRequest reportInput = null;
        ReportOutput reportOutput = null;
        if (waterConnectionDetails != null)
            if (waterConnectionDetails.getReconnectionFileStore() != null) {
                final FileStoreMapper fmp = waterConnectionDetails.getReconnectionFileStore();
                final File file = fileStoreService.fetch(fmp, FILESTORE_MODULECODE);
                reportOutput = new ReportOutput();
                try {
                    reportOutput.setReportOutputData(FileUtils.readFileToByteArray(file));
                    reportOutput.setReportFormat(ReportFormat.PDF);
                } catch (final IOException e) {
                    throw new ApplicationRuntimeException("Exception in generating work order notice" + e);
                }
            } else {
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
                reportParams.put(APPLICATION_TYPE, WordUtils.capitalize(WaterTaxConstants.RECONNECTIONWITHSLASH));
                reportParams.put(CITY_NAME, cityMunicipalityName);
                reportParams.put(DISTRICT, districtName);
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
                user = securityUtils.getCurrentUser();
                reportParams.put(USER_ID, user.getId());
                reportParams.put(WORK_FLOW_ACTION, workFlowAction);
                reportInput = new ReportRequest(WaterTaxConstants.RECONNECTION_ESTIMATION_NOTICE,
                        waterConnectionDetails, reportParams);
                reportOutput = reportService.createReport(reportInput);
            }

        return reportOutput;
    }

    public ReportOutput generateClosureConnectionReport(final WaterConnectionDetails waterConnectionDetails,
            final String workFlowAction, final String cityMunicipalityName, final String districtName) {
        final Map<String, Object> reportParams = new HashMap<>();
        ReportRequest reportInput = null;
        ReportOutput reportOutput = null;
        if (waterConnectionDetails != null)
            if (waterConnectionDetails.getClosureFileStore() != null) {
                final FileStoreMapper fmp = waterConnectionDetails.getClosureFileStore();
                final File file = fileStoreService.fetch(fmp, FILESTORE_MODULECODE);
                reportOutput = new ReportOutput();
                try {
                    reportOutput.setReportOutputData(FileUtils.readFileToByteArray(file));
                    reportOutput.setReportFormat(ReportFormat.PDF);
                } catch (final IOException e) {
                    throw new ApplicationRuntimeException("Exception in generating work order notice" + e);
                }
            } else {

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

                reportParams.put(APPLICATION_TYPE, WordUtils.capitalize(WaterTaxConstants.CLOSURECONN));
                reportParams.put(CITY_NAME, cityMunicipalityName);
                reportParams.put(DISTRICT, districtName);
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
                final User user = securityUtils.getCurrentUser();
                reportParams.put(USER_ID, user.getId());
                reportParams.put(WORK_FLOW_ACTION, workFlowAction);
                reportInput = new ReportRequest(WaterTaxConstants.CLOSURE_ESTIMATION_NOTICE,
                        waterConnectionDetails.getEstimationDetails(), reportParams);
                reportOutput = reportService.createReport(reportInput);
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

    public ReportRequest generateCitizenAckReport(final WaterConnectionDetails waterConnectionDetails) {
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
                reportParams.put("doorno", doorno[0]);
                String ownerName = "";
                String commissionerName = "";
                Iterator<OwnerName> iterator = null;
                if (!assessmentDetails.getOwnerNames().isEmpty())
                    iterator = assessmentDetails.getOwnerNames().iterator();
                if (iterator != null && iterator.hasNext())
                    ownerName = iterator.next().getOwnerName();
                reportParams.put(APPLICANT_NAME, WordUtils.capitalize(ownerName));
                final Designation designation = designationService.getDesignationByName(WaterTaxConstants.DESG_COMM_NAME);
                if (designation != null) {
                    final List<Assignment> assignList = assignmentService.getAllActiveAssignments(designation.getId());
                    commissionerName = assignList.isEmpty() ? "" : assignList.get(0).getEmployee().getName();
                }

                if (WaterTaxConstants.NEWCONNECTION.equalsIgnoreCase(connectionDetails.getApplicationType().getCode())) {
                    reportParams.put(CONNTITLE,
                            WordUtils.capitalize(connectionDetails.getApplicationType().getName()));
                    reportParams.put(APPLICATION_TYPE, messageSource.getMessage("msg.new.watertap.conn", null, null));
                } else if (WaterTaxConstants.ADDNLCONNECTION
                        .equalsIgnoreCase(connectionDetails.getApplicationType().getCode())) {
                    reportParams.put(CONNTITLE,
                            WordUtils.capitalize(connectionDetails.getApplicationType().getName()));
                    reportParams.put(APPLICATION_TYPE, messageSource.getMessage("msg.add.watertap.conn", null, null));
                } else {
                    reportParams.put(CONNTITLE,
                            WordUtils.capitalize(connectionDetails.getApplicationType().getName()));
                    reportParams.put(APPLICATION_TYPE,
                            messageSource.getMessage("msg.changeofuse.watertap.conn", null, null));
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
                if (assignment != null && assignment.getDesignation().getName().equals(WaterTaxConstants.DESG_COMM_NAME))
                    userDesignation = assignment.getDesignation().getName();
                else
                    userDesignation = null;
                reportParams.put("designation", userDesignation);
                if (workFlowAction != null) {
                    if (workFlowAction.equalsIgnoreCase(WaterTaxConstants.WF_WORKORDER_BUTTON)) {
                        reportParams.put(WORK_ORDER_DATE, toDefaultDateFormat(connectionDetails.getWorkOrderDate()));
                        reportParams.put(WORK_ORDER_NO, connectionDetails.getWorkOrderNumber());
                    }
                    if (workFlowAction.equalsIgnoreCase(WaterTaxConstants.WF_PREVIEW_BUTTON)) {
                        reportParams.put(WORK_ORDER_DATE, "");
                        reportParams.put(WORK_ORDER_NO, "");
                    }
                    if (workFlowAction.equalsIgnoreCase(WaterTaxConstants.WF_SIGN_BUTTON)) {
                        reportParams.put(WORK_ORDER_DATE, toDefaultDateFormat(connectionDetails.getWorkOrderDate()));
                        reportParams.put(WORK_ORDER_NO, connectionDetails.getWorkOrderNumber());
                        reportParams.put(USER_ID, user.getId());
                    }
                }
                reportParams.put(WORK_FLOW_ACTION, workFlowAction);
                reportParams.put(ADDRESS, assessmentDetails.getPropertyAddress());
                reportParams.put("locality", assessmentDetails.getBoundaryDetails().getLocalityName());
                reportParams.put(COMMISSIONER_NAME, commissionerName);
                setReportParameters(reportParams, connectionDetails);

                reportOutput = reportService.createReport(reportInput);
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
            reportOutput.setReportFormat(ReportFormat.PDF);
        } catch (final IOException e) {
            throw new ApplicationRuntimeException("Exception in generating work order notice" + e);
        }
        return reportOutput;
    }

    public ReportRequest setReportParams(final Map<String, Object> reportParams, final WaterConnectionDetails connectionDetails) {
        double total;
        reportParams.put("consumerNumber", connectionDetails.getConnection().getConsumerCode());
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
}
