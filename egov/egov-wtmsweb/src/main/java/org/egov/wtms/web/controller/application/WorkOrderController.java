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
package org.egov.wtms.web.controller.application;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.WordUtils;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.pims.commons.Designation;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.OwnerName;
import org.egov.ptis.domain.model.enums.BasicPropertyStatus;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.utils.PropertyExtnUtils;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/application")
public class WorkOrderController {

    @Autowired
    private ReportService reportService;

    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;

    public static final String CONNECTIONWORKORDER = "connectionWorkOrder";
    @Autowired
    private PropertyExtnUtils propertyExtnUtils;
    private final Map<String, Object> reportParams = new HashMap<String, Object>();
    private ReportRequest reportInput = null;
    private ReportOutput reportOutput = null;
    String errorMessage = "";
    private String workFlowAction = "";
    @Autowired
    private WaterConnectionDetailsService wcdService;
    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;
    
    @Autowired
    private DesignationService designationService;
    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private SecurityUtils securityUtils;

    @RequestMapping(value = "/workorder", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<byte[]> createWorkOrderReport(final HttpServletRequest request,
            final HttpSession session) {
        final WaterConnectionDetails connectionDetails = wcdService
                .findByApplicationNumber(request.getParameter("pathVar"));
        workFlowAction = (String) session.getAttribute(WaterTaxConstants.WORKFLOW_ACTION);
        final Boolean isDigSignPending = Boolean.parseBoolean(request.getParameter("isDigSignPending"));
        if (isDigSignPending)
            workFlowAction = request.getParameter("workFlowAction");
        if (null != workFlowAction && !workFlowAction.isEmpty()
                && workFlowAction.equalsIgnoreCase(WaterTaxConstants.WF_WORKORDER_BUTTON))
            validateWorkOrder(connectionDetails, true);
        if (!errorMessage.isEmpty())
            return redirect();
        return generateReport(connectionDetails, session);
    }

    private ResponseEntity<byte[]> generateReport(final WaterConnectionDetails connectionDetails,
            final HttpSession session) {
        if (null != connectionDetails) {
            final AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
                    connectionDetails.getConnection().getPropertyIdentifier(),
                    PropertyExternalService.FLAG_FULL_DETAILS, BasicPropertyStatus.ACTIVE);
            final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            final String doorno[] = assessmentDetails.getPropertyAddress().split(",");
            String ownerName = "";
            double total = 0;
            String commissionerName = "";
            Designation desgn=designationService.getDesignationByName(WaterTaxConstants.DESG_COMM_NAME);
            if(desgn!=null){
                List<Assignment>assignList=assignmentService.getAllActiveAssignments(desgn.getId());
                commissionerName = !assignList.isEmpty()?assignList.get(0).getEmployee().getName():"";
            }
            for (final OwnerName names : assessmentDetails.getOwnerNames()) {
                ownerName = names.getOwnerName();
                break;
            }

            if (WaterTaxConstants.NEWCONNECTION.equalsIgnoreCase(connectionDetails.getApplicationType().getCode())) {
                reportParams.put("conntitle",
                        WordUtils.capitalize(connectionDetails.getApplicationType().getName()).toString());
                reportParams.put("applicationType", messageSource.getMessage("msg.new.watertap.conn", null, null));
            } else if (WaterTaxConstants.ADDNLCONNECTION
                    .equalsIgnoreCase(connectionDetails.getApplicationType().getCode())) {
                reportParams.put("conntitle",
                        WordUtils.capitalize(connectionDetails.getApplicationType().getName()).toString());
                reportParams.put("applicationType", messageSource.getMessage("msg.add.watertap.conn", null, null));
            } else {
                reportParams.put("conntitle",
                        WordUtils.capitalize(connectionDetails.getApplicationType().getName()).toString());
                reportParams.put("applicationType",
                        messageSource.getMessage("msg.changeofuse.watertap.conn", null, null));
            }
            reportParams.put("municipality", session.getAttribute("citymunicipalityname"));
            reportParams.put("district", session.getAttribute("districtName"));
            reportParams.put("purpose", connectionDetails.getUsageType().getName());
            if (null != workFlowAction) {
                if (workFlowAction.equalsIgnoreCase(WaterTaxConstants.WF_WORKORDER_BUTTON)) {
                    reportParams.put("workOrderDate", formatter.format(connectionDetails.getWorkOrderDate()));
                    reportParams.put("workOrderNo", connectionDetails.getWorkOrderNumber());
                }
                if (workFlowAction.equalsIgnoreCase(WaterTaxConstants.WF_PREVIEW_BUTTON)) {
                    reportParams.put("workOrderDate", "");
                    reportParams.put("workOrderNo", "");
                }
                if (workFlowAction.equalsIgnoreCase(WaterTaxConstants.WF_SIGN_BUTTON)) {
                    reportParams.put("workOrderDate", formatter.format(connectionDetails.getWorkOrderDate()));
                    reportParams.put("workOrderNo", connectionDetails.getWorkOrderNumber());
                    final User user = securityUtils.getCurrentUser();
                    reportParams.put("userId", user.getId());
                }
            }
            reportParams.put("workFlowAction", workFlowAction);
            reportParams.put("consumerNumber", connectionDetails.getConnection().getConsumerCode());
            reportParams.put("applicantName", WordUtils.capitalize(ownerName));
            reportParams.put("applicantionDate", formatter.format(connectionDetails.getApplicationDate()));
            reportParams.put("address", assessmentDetails.getPropertyAddress());
            reportParams.put("doorno", doorno[0]);
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
            reportParams.put("commissionerName", commissionerName);
            total = connectionDetails.getDonationCharges()
                    + connectionDetails.getFieldInspectionDetails().getSecurityDeposit()
                    + connectionDetails.getFieldInspectionDetails().getRoadCuttingCharges()
                    + connectionDetails.getFieldInspectionDetails().getSupervisionCharges();
            reportParams.put("total", total);
            reportInput = new ReportRequest(CONNECTIONWORKORDER, connectionDetails, reportParams);
        }
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("content-disposition", "inline;filename=Work Order.pdf");
        reportOutput = reportService.createReport(reportInput);
        return new ResponseEntity<byte[]>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);
    }

    public void validateWorkOrder(final WaterConnectionDetails connectionDetails, final Boolean isView) {

        if (null != connectionDetails && connectionDetails.getLegacy())
            errorMessage = messageSource.getMessage("err.validate.workorder.for.legacy", new String[] { "" }, null);
        else if (isView && null == connectionDetails.getWorkOrderNumber())
            errorMessage = messageSource.getMessage("err.validate.workorder.view",
                    new String[] { connectionDetails.getApplicationNumber() }, null);
        else if (!isView && !connectionDetails.getStatus().getCode()
                .equalsIgnoreCase(WaterTaxConstants.APPLICATION_STATUS_WOGENERATED))
            errorMessage = messageSource.getMessage("err.validate.workorder.view",
                    new String[] { connectionDetails.getApplicationNumber() }, null);
    }

    @RequestMapping(value = "/workorder/view/{applicationNumber}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<byte[]> viewReport(@PathVariable final String applicationNumber,
            final HttpSession session) {
        final WaterConnectionDetails connectionDetails = wcdService.findByApplicationNumber(applicationNumber);
        validateWorkOrder(connectionDetails, true);
        if (!errorMessage.isEmpty())
            return redirect();
        return generateReport(connectionDetails, session);
    }

    private ResponseEntity<byte[]> redirect() {
        errorMessage = "<html><body><p style='color:red;border:1px solid gray;padding:15px;'>" + errorMessage
                + "</p></body></html>";
        final byte[] byteData = errorMessage.getBytes();
        errorMessage = "";
        return new ResponseEntity<byte[]>(byteData, HttpStatus.CREATED);
    }
}
