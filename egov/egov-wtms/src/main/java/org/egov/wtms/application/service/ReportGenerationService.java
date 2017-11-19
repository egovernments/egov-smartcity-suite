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

import static org.egov.wtms.utils.constants.WaterTaxConstants.FILESTORE_MODULECODE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WATERCHARGES_CONSUMERCODE;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.WordUtils;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.reporting.engine.ReportFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.utils.NumberToWordConverter;
import org.egov.pims.commons.Position;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.OwnerName;
import org.egov.ptis.domain.model.enums.BasicPropertyStatus;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.utils.PropertyExtnUtils;
import org.egov.wtms.utils.WaterTaxUtils;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class ReportGenerationService {

    public static final String ESTIMATION_NOTICE = "estimationNotice";

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

    public ReportOutput getReportOutput(final WaterConnectionDetails connectionDetails, final String workFlowAction,
            final String cityMunicipalityName, final String districtName) {
        final Map<String, Object> reportParams = new HashMap<>(0);
        ReportRequest reportInput = null;
        ReportOutput reportOutput = null;
        if (null != connectionDetails) {
            final AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
                    connectionDetails.getConnection().getPropertyIdentifier(),
                    PropertyExternalService.FLAG_FULL_DETAILS, BasicPropertyStatus.ALL);
            final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            final String propAddress = assessmentDetails.getPropertyAddress();
            String doorno[] = null;
            double total = 0;
            if (null != propAddress && !propAddress.isEmpty())
                doorno = propAddress.split(",");
            String ownerName = "";
            final Set<OwnerName> ownerNames = assessmentDetails.getOwnerNames();
            if (null != ownerNames && !ownerNames.isEmpty())
                for (final OwnerName names : assessmentDetails.getOwnerNames()) {
                    ownerName = names.getOwnerName();
                    break;
                }
            final List<Assignment> assignList = assignmentService
                    .findPrimaryAssignmentForDesignationName(WaterTaxConstants.DESG_COMM_NAME);
            String commissionerName = "";
            if (!assignList.isEmpty())
                commissionerName = assignList.get(0).getEmployee().getName();
            if (WaterTaxConstants.NEWCONNECTION.equalsIgnoreCase(connectionDetails.getApplicationType().getCode()))
                reportParams.put("applicationtype", wcmsMessageSource.getMessage("msg.new.watertap.conn", null, null));
            else if (WaterTaxConstants.ADDNLCONNECTION
                    .equalsIgnoreCase(connectionDetails.getApplicationType().getCode()))
                reportParams.put("applicationtype", wcmsMessageSource.getMessage("msg.add.watertap.conn", null, null));
            else
                reportParams.put("applicationtype",
                        wcmsMessageSource.getMessage("msg.changeofuse.watertap.conn", null, null));
            reportParams.put("conntitle",
                    WordUtils.capitalize(connectionDetails.getApplicationType().getName()));
            reportParams.put("municipality", cityMunicipalityName);
            reportParams.put("district", districtName);
            reportParams.put("purpose", connectionDetails.getUsageType().getName());
            if (null != workFlowAction) {
                if (workFlowAction.equalsIgnoreCase(WaterTaxConstants.WF_WORKORDER_BUTTON)
                        || workFlowAction.equalsIgnoreCase(WaterTaxConstants.WF_SIGN_BUTTON)) {
                    reportParams.put("workOrderDate", formatter.format(connectionDetails.getWorkOrderDate()));
                    reportParams.put("workOrderNo", connectionDetails.getWorkOrderNumber());
                    if (workFlowAction.equalsIgnoreCase(WaterTaxConstants.WF_SIGN_BUTTON)) {
                        final User user = securityUtils.getCurrentUser();
                        reportParams.put("userId", user.getId());
                    }
                }
                if (workFlowAction.equalsIgnoreCase(WaterTaxConstants.WF_PREVIEW_BUTTON)) {
                    reportParams.put("workOrderDate", "");
                    reportParams.put("workOrderNo", "");
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

            reportParams.put("workFlowAction", workFlowAction);
            reportParams.put("consumerNumber", connectionDetails.getConnection().getConsumerCode());
            reportParams.put("applicantName", WordUtils.capitalize(ownerName));
            reportParams.put("address", propAddress);
            reportParams.put("doorno", doorno != null ? doorno[0] : "");
            reportParams.put("userSignature", securityUtils.getCurrentUser().getSignature() != null
                    ? new ByteArrayInputStream(securityUtils.getCurrentUser().getSignature()) : null);
            reportParams.put("applicationDate", formatter.format(connectionDetails.getApplicationDate()));
            reportParams.put("donationCharges", connectionDetails.getDonationCharges());
            reportParams.put("securityDeposit", connectionDetails.getFieldInspectionDetails().getSecurityDeposit());
            reportParams.put("roadCuttingCharges",
                    connectionDetails.getFieldInspectionDetails().getRoadCuttingCharges());
            reportParams.put("superVisionCharges",
                    connectionDetails.getFieldInspectionDetails().getSupervisionCharges());
            reportParams.put("locality", assessmentDetails.getBoundaryDetails().getLocalityName());
            total = connectionDetails.getDonationCharges()
                    + connectionDetails.getFieldInspectionDetails().getSecurityDeposit()
                    + connectionDetails.getFieldInspectionDetails().getRoadCuttingCharges()
                    + connectionDetails.getFieldInspectionDetails().getSupervisionCharges();
            reportParams.put("total", total);
            reportParams.put("commissionerName", commissionerName);
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
                final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                Assignment assignment = null;
                User user = null;
                final AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
                        waterConnectionDetails.getConnection().getPropertyIdentifier(),
                        PropertyExternalService.FLAG_FULL_DETAILS, BasicPropertyStatus.ALL);
                final String doorNo[] = assessmentDetails.getPropertyAddress().split(",");
                String ownerName = "";
                for (final OwnerName names : assessmentDetails.getOwnerNames()) {
                    ownerName = names.getOwnerName();
                    break;
                }
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
                reportParams.put("applicationType", WordUtils.capitalize(WaterTaxConstants.RECONNECTIONWITHSLASH));
                reportParams.put("cityName", cityMunicipalityName);
                reportParams.put("district", districtName);
                reportParams.put("applicationDate", formatter.format(waterConnectionDetails.getApplicationDate()));
                reportParams.put("reconnApprovalDate",
                        formatter.format(waterConnectionDetails.getReconnectionApprovalDate() != null
                                ? waterConnectionDetails.getReconnectionApprovalDate() : new Date()));
                reportParams.put("applicantName", ownerName);
                reportParams.put(WATERCHARGES_CONSUMERCODE, waterConnectionDetails.getConnection().getConsumerCode());
                reportParams.put("commissionerName",
                        user != null && user.getUsername() != null ? user.getName() : ownerName);
                reportParams.put("address", assessmentDetails.getPropertyAddress());
                reportParams.put("houseNo", doorNo[0]);
                reportParams.put("usersignature", securityUtils.getCurrentUser().getSignature() != null
                        ? new ByteArrayInputStream(securityUtils.getCurrentUser().getSignature()) : null);
                user = securityUtils.getCurrentUser();
                reportParams.put("userId", user.getId());
                reportParams.put("workFlowAction", workFlowAction);
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

                final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
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

                reportParams.put("applicationType", WordUtils.capitalize(WaterTaxConstants.CLOSURECONN));
                reportParams.put("cityName", cityMunicipalityName);
                reportParams.put("district", districtName);
                reportParams.put("applicationDate", formatter.format(waterConnectionDetails.getApplicationDate()));
                reportParams.put("applicantName", ownerName);
                reportParams.put(WATERCHARGES_CONSUMERCODE, waterConnectionDetails.getConnection().getConsumerCode());
                reportParams.put("address", assessmentDetails.getPropertyAddress());
                reportParams.put("houseNo", doorNo[0]);
                reportParams.put("usersignature", securityUtils.getCurrentUser().getSignature() != null
                        ? new ByteArrayInputStream(securityUtils.getCurrentUser().getSignature()) : null);
                reportParams.put("closeApprovalDate", formatter.format(waterConnectionDetails.getCloseApprovalDate() != null
                        ? waterConnectionDetails.getCloseApprovalDate() : new Date()));
                reportParams.put("closeConnectionType",
                        waterConnectionDetails.getCloseConnectionType().equals("T") ? "Temporary" : "Permanent");
                final User user = securityUtils.getCurrentUser();
                reportParams.put("userId", user.getId());
                reportParams.put("workFlowAction", workFlowAction);
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

            reportParams.put("applicationType",
                    WordUtils.capitalize(waterConnectionDetails.getApplicationType().getName()));
            reportParams.put("cityName", cityMunicipalityName);
            reportParams.put("district", districtName);
            reportParams.put("estimationDate",
                    DateUtils.toDefaultDateFormat(waterConnectionDetails.getFieldInspectionDetails().getCreatedDate()));
            reportParams.put("estimationNumber", waterConnectionDetails.getEstimationNumber());
            reportParams.put("donationCharges", waterConnectionDetails.getDonationCharges());
            final double totalCharges = waterConnectionDetails.getDonationCharges()
                    + waterConnectionDetails.getFieldInspectionDetails().getSupervisionCharges()
                    + waterConnectionDetails.getFieldInspectionDetails().getRoadCuttingCharges()
                    + waterConnectionDetails.getFieldInspectionDetails().getSecurityDeposit();
            reportParams.put("totalCharges", totalCharges);
            reportParams.put("applicationDate", DateUtils.toDefaultDateFormat(waterConnectionDetails.getApplicationDate()));
            reportParams.put("applicantName", ownerName.toString());
            reportParams.put("address", assessmentDetails.getPropertyAddress());
            reportParams.put("houseNo", doorNo[0]);
            reportParams.put("propertyID", waterConnectionDetails.getConnection().getPropertyIdentifier());
            reportParams.put("amountInWords", getTotalAmntInWords(totalCharges));
            reportParams.put("securityDeposit",
                    waterConnectionDetails.getFieldInspectionDetails().getSecurityDeposit());
            reportParams.put("roadCuttingCharges",
                    waterConnectionDetails.getFieldInspectionDetails().getRoadCuttingCharges());
            reportParams.put("superVisionCharges",
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

}
