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
package org.egov.wtms.web.controller.application;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.ReportGenerationService;
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
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @Autowired
    private PropertyExtnUtils propertyExtnUtils;

    @Autowired
    private WaterConnectionDetailsService wcdService;

    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;

    @Autowired
    private ReportGenerationService reportGenerationService;

    @RequestMapping(value = "/workorder", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> createWorkOrderReport(final HttpServletRequest request,
            final HttpSession session) {
        String workFlowAction;
        String errorMessage = "";
        ReportOutput reportOutput = null;
        final WaterConnectionDetails connectionDetails = wcdService
                .findByApplicationNumber(request.getParameter("pathVar"));
        workFlowAction = (String) session.getAttribute(WaterTaxConstants.WORKFLOW_ACTION);
        final Boolean isDigSignPending = Boolean.parseBoolean(request.getParameter("isDigSignPending"));
        if (isDigSignPending)
            workFlowAction = request.getParameter("workFlowAction");
        if (null != workFlowAction && !workFlowAction.isEmpty()
                && workFlowAction.equalsIgnoreCase(WaterTaxConstants.WF_WORKORDER_BUTTON))
            errorMessage = validateWorkOrder(connectionDetails, true);
        if (!errorMessage.isEmpty())
            return redirect(errorMessage);
        reportOutput = reportGenerationService.generateWorkOrderNotice(connectionDetails, workFlowAction);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("content-disposition", "inline;filename=Work Order.pdf");
        return new ResponseEntity<>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);
    }

    public String validateWorkOrder(final WaterConnectionDetails connectionDetails, final Boolean isView) {
        String errorMessage = "";
        if (connectionDetails != null)
            if (connectionDetails.getLegacy())
                errorMessage = messageSource.getMessage("err.validate.workorder.for.legacy", new String[] { "" }, null);
            else if (isView && connectionDetails.getWorkOrderNumber() == null)
                errorMessage = messageSource.getMessage("err.validate.workorder.view",
                        new String[] { connectionDetails.getApplicationNumber() }, null);
            else if (!isView && !connectionDetails.getStatus().getCode()
                    .equalsIgnoreCase(WaterTaxConstants.APPLICATION_STATUS_WOGENERATED))
                errorMessage = messageSource.getMessage("err.validate.workorder.view",
                        new String[] { connectionDetails.getApplicationNumber() }, null);
        return errorMessage;
    }

    @RequestMapping(value = "/workorder/view/{applicationNumber}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> viewReport(@PathVariable final String applicationNumber,
            final HttpSession session) {
        ReportOutput reportOutput = null;
        final WaterConnectionDetails connectionDetails = wcdService.findByApplicationNumber(applicationNumber);
        final String errorMessage = validateWorkOrder(connectionDetails, true);
        if (!errorMessage.isEmpty())
            return redirect(errorMessage);
        reportOutput = reportGenerationService.generateWorkOrderNotice(connectionDetails, null);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("content-disposition", "inline;filename=Work Order.pdf");
        return new ResponseEntity<>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);
    }

    private ResponseEntity<byte[]> redirect(final String errorMessage) {
        final String formattedErrorMsg = "<html><body><p style='color:red;border:1px solid gray;padding:15px;'>" + errorMessage
                + "</p></body></html>";
        final byte[] byteData = formattedErrorMsg.getBytes();
        return new ResponseEntity<>(byteData, HttpStatus.CREATED);
    }
}
