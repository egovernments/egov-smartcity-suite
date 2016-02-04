package org.egov.wtms.application.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.WordUtils;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.security.utils.SecurityUtils;
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
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

@Service
public class ReportGenerationService {

    @Autowired
    private ResourceBundleMessageSource messageSource;
    
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
    
    public ReportOutput getReportOutput(final WaterConnectionDetails connectionDetails, final String workFlowAction, final String cityMunicipalityName, final String districtName) {
        Map<String, Object> reportParams = new HashMap<String, Object>();
        ReportRequest reportInput = null;
        ReportOutput reportOutput = null;
        if (null != connectionDetails) {
            final AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
                    connectionDetails.getConnection().getPropertyIdentifier(),
                    PropertyExternalService.FLAG_FULL_DETAILS,BasicPropertyStatus.ALL);
            final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            final String propAddress = assessmentDetails.getPropertyAddress();
            String doorno[] = null;
            if(null != propAddress && !propAddress.isEmpty()) {
                doorno = propAddress.split(",");
            }
            String ownerName = "";
            final Set<OwnerName> ownerNames = assessmentDetails.getOwnerNames();
            if(null != ownerNames && !ownerNames.isEmpty()) {
                for (final OwnerName names : assessmentDetails.getOwnerNames()) {
                    ownerName = names.getOwnerName();
                    break;
                }
            }
            if (WaterTaxConstants.NEWCONNECTION.equalsIgnoreCase(connectionDetails.getApplicationType().getCode())) {
                reportParams.put("conntitle", WordUtils.capitalize(connectionDetails.getApplicationType().getName()).toString());
                reportParams.put("applicationtype", messageSource.getMessage("msg.new.watertap.conn", null, null));
            } else if (WaterTaxConstants.ADDNLCONNECTION.equalsIgnoreCase(connectionDetails.getApplicationType().getCode())) {
                reportParams.put("conntitle", WordUtils.capitalize(connectionDetails.getApplicationType().getName()).toString());
                reportParams.put("applicationtype", messageSource.getMessage("msg.add.watertap.conn", null, null));
            } else {
                reportParams.put("conntitle", WordUtils.capitalize(connectionDetails.getApplicationType().getName()).toString());
                reportParams.put("applicationtype", messageSource.getMessage("msg.changeofuse.watertap.conn", null, null));
            }
            reportParams.put("municipality", cityMunicipalityName);
            reportParams.put("district", districtName);
            reportParams.put("purpose", connectionDetails.getUsageType().getName());
            if(null != workFlowAction) {
                if(workFlowAction.equalsIgnoreCase(WaterTaxConstants.WF_WORKORDER_BUTTON)) {
                    reportParams.put("workorderdate", formatter.format(connectionDetails.getWorkOrderDate()));
                    reportParams.put("workorderno", connectionDetails.getWorkOrderNumber());
                }
                if(workFlowAction.equalsIgnoreCase(WaterTaxConstants.WF_PREVIEW_BUTTON)) {
                    reportParams.put("workorderdate", "");
                    reportParams.put("workorderno", "");
                }
                if(workFlowAction.equalsIgnoreCase(WaterTaxConstants.WF_SIGN_BUTTON)) {
                    reportParams.put("workorderdate", formatter.format(connectionDetails.getWorkOrderDate()));
                    reportParams.put("workorderno", connectionDetails.getWorkOrderNumber());
                    User user = securityUtils.getCurrentUser();
                    reportParams.put("userId", user.getId());
                }
            }
            reportParams.put("workFlowAction", workFlowAction);
            reportParams.put("consumerNumber", connectionDetails.getConnection().getConsumerCode());
            reportParams.put("applicantname", WordUtils.capitalize(ownerName));
            reportParams.put("address", propAddress);
            reportParams.put("doorno", doorno != null ? doorno[0] : "");
            reportParams.put("applicationDate",formatter.format(connectionDetails.getApplicationDate()));
            reportInput = new ReportRequest(WaterTaxConstants.CONNECTION_WORK_ORDER, connectionDetails, reportParams);
        }
        reportOutput = reportService.createReport(reportInput);
        return reportOutput;
    }
    
    public ReportOutput generateReconnectionReport(final WaterConnectionDetails waterConnectionDetails, final String workFlowAction, final String cityMunicipalityName, final String districtName) {
        Map<String, Object> reportParams = new HashMap<String, Object>();
        ReportRequest reportInput = null;
        ReportOutput reportOutput = null;
        if (waterConnectionDetails != null) {
            final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Assignment assignment=null;
            User user=null;
            final AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
                    waterConnectionDetails.getConnection().getPropertyIdentifier(),
                    PropertyExternalService.FLAG_FULL_DETAILS,BasicPropertyStatus.ALL);
            final String doorNo[] = assessmentDetails.getPropertyAddress().split(",");
            String ownerName = "";
            for (final OwnerName names : assessmentDetails.getOwnerNames()) {
                ownerName = names.getOwnerName();
                break;
            }
            List<Assignment>asignList=null;
            Position approverPos=waterTaxUtils.getCityLevelCommissionerPosition("Commissioner");
                        if (approverPos != null) {
                                assignment = assignmentService
                                                .getPrimaryAssignmentForPositionAndDate(
                                                                approverPos.getId(), new Date());
                                if (assignment != null && assignment.getEmployee() != null) {
                                        asignList = new ArrayList<Assignment>();
                                        asignList.add(assignment);
                                } else if (assignment == null && approverPos != null)
                                        asignList = assignmentService.getAssignmentsForPosition(
                                                        approverPos.getId(), new Date());
                                if (!asignList.isEmpty())
                                        user = userService.getUserById(asignList.get(0)
                                                        .getEmployee().getId());
                        }
            reportParams.put("applicationType", WordUtils.capitalize(WaterTaxConstants.RECONNECTIONWITHSLASH));
            reportParams.put("cityName", cityMunicipalityName);
            reportParams.put("district", districtName);
            reportParams.put("applicationDate", formatter.format(waterConnectionDetails.getApplicationDate()));
            reportParams.put("reconnApprovalDate", formatter.format(waterConnectionDetails.getReconnectionApprovalDate()!=null ?waterConnectionDetails.getReconnectionApprovalDate():new Date()));
            reportParams.put("applicantName", ownerName);
            reportParams.put("consumerCode", waterConnectionDetails.getConnection().getConsumerCode());
            reportParams.put("commissionerName",(user !=null && user.getUsername()!=null ? user.getName():ownerName));
            reportParams.put("address", assessmentDetails.getPropertyAddress());
            reportParams.put("houseNo", doorNo[0]);
            user = securityUtils.getCurrentUser();
            reportParams.put("userId", user.getId());
            reportParams.put("workFlowAction", workFlowAction);
            reportInput = new ReportRequest(WaterTaxConstants.RECONNECTION_ESTIMATION_NOTICE, waterConnectionDetails.getEstimationDetails(), reportParams);
        }
        reportOutput = reportService.createReport(reportInput);
        return reportOutput;
    }
    
    public ReportOutput generateClosureConnectionReport(final WaterConnectionDetails waterConnectionDetails, final String workFlowAction, final String cityMunicipalityName, final String districtName) {
        Map<String, Object> reportParams = new HashMap<String, Object>();
        ReportRequest reportInput = null;
        ReportOutput reportOutput = null;
        if (waterConnectionDetails != null) {
            final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            final AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
                    waterConnectionDetails.getConnection().getPropertyIdentifier(),
                    PropertyExternalService.FLAG_FULL_DETAILS,BasicPropertyStatus.ALL);
            final String doorNo[] = assessmentDetails.getPropertyAddress().split(",");
            String ownerName = "";
            for (final OwnerName names : assessmentDetails.getOwnerNames()) {
                ownerName = names.getOwnerName();
                break;
            }

            reportParams.put("applicationType", WordUtils.capitalize(WaterTaxConstants.CLOSURECONN));
            reportParams.put("cityName", cityMunicipalityName);
            reportParams.put("district", districtName);
            reportParams.put("applicationDate", formatter.format(waterConnectionDetails.getApplicationDate()));
            reportParams.put("applicantName", ownerName);
            reportParams.put("consumerCode", waterConnectionDetails.getConnection().getConsumerCode());
            reportParams.put("address", assessmentDetails.getPropertyAddress());
            reportParams.put("houseNo", doorNo[0]);
            reportParams.put("closeApprovalDate", formatter.format(waterConnectionDetails.getCloseApprovalDate()!=null ?waterConnectionDetails.getCloseApprovalDate():new Date()));
            reportParams.put("closeConnectionType", waterConnectionDetails.getCloseConnectionType().equals("T")? "Temporary":"Permanent");
            User user = securityUtils.getCurrentUser();
            reportParams.put("userId", user.getId());
            reportParams.put("workFlowAction", workFlowAction);
            reportInput = new ReportRequest(WaterTaxConstants.CLOSURE_ESTIMATION_NOTICE, waterConnectionDetails.getEstimationDetails(), reportParams);
        }
        reportOutput = reportService.createReport(reportInput);
        return reportOutput;
    }
}
