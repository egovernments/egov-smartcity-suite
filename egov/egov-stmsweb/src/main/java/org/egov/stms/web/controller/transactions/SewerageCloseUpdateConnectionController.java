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
package org.egov.stms.web.controller.transactions;

import static org.egov.stms.utils.constants.SewerageTaxConstants.FILESTORE_MODULECODE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.NOTICE_TYPE_CLOSER_NOTICE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WFLOW_ACTION_STEP_CANCEL;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WF_STATE_CONNECTION_CLOSE_BUTTON;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPROVEWORKFLOWACTION;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.ValidationException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.reporting.engine.ReportFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.stms.masters.entity.enums.PropertyType;
import org.egov.stms.notice.entity.SewerageNotice;
import org.egov.stms.notice.service.SewerageNoticeService;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.transactions.entity.SewerageApplicationDetailsDocument;
import org.egov.stms.transactions.service.SewerageApplicationDetailsService;
import org.egov.stms.transactions.service.SewerageConnectionService;
import org.egov.stms.transactions.service.SewerageThirdPartyServices;
import org.egov.stms.utils.SewerageTaxUtils;
import org.egov.stms.web.controller.utils.SewerageApplicationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(value = "/transactions")
public class SewerageCloseUpdateConnectionController extends GenericWorkFlowController {

    private static final String SEWERAGE_APPLICATION_DETAILS = "sewerageApplicationDetails";

    private static final Logger LOGGER = Logger.getLogger(SewerageCloseUpdateConnectionController.class);

    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;

    @Autowired
    private SewerageApplicationDetailsService sewerageApplicationDetailsService;

    @Autowired
    private SewerageTaxUtils sewerageTaxUtils;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private SewerageThirdPartyServices sewerageThirdPartyServices;

    @Autowired
    private SewerageConnectionService sewerageConnectionService;

    @Autowired
    private SewerageNoticeService sewerageNoticeService;

    @Autowired
    private SewerageApplicationValidator sewerageApplicationValidator;

    @ModelAttribute(SEWERAGE_APPLICATION_DETAILS)
    public SewerageApplicationDetails getSewerageApplicationDetails(@PathVariable final String applicationNumber) {
        return sewerageApplicationDetailsService
                .findByApplicationNumber(applicationNumber);
    }

    @GetMapping("/closeSewerageConnection-update/{applicationNumber}")
    public String view(final Model model, @PathVariable final String applicationNumber, final HttpServletRequest request) {
        final SewerageApplicationDetails sewerageApplicationDetails = sewerageApplicationDetailsService
                .findByApplicationNumber(applicationNumber);
        model.addAttribute(SEWERAGE_APPLICATION_DETAILS, sewerageApplicationDetails);

        final AssessmentDetails propertyOwnerDetails = sewerageThirdPartyServices
                .getPropertyDetails(sewerageApplicationDetails.getConnection().getShscNumber(), request);
        if (propertyOwnerDetails != null)
            model.addAttribute("propertyOwnerDetails", propertyOwnerDetails);

        model.addAttribute("stateType", sewerageApplicationDetails.getClass().getSimpleName());
        model.addAttribute(SEWERAGE_APPLICATION_DETAILS, sewerageApplicationDetails);
        model.addAttribute("approvalDepartmentList", departmentService.getAllDepartments());
        model.addAttribute("currentState", sewerageApplicationDetails.getCurrentState().getValue());
        sewerageApplicationDetails.getWorkflowContainer().setAdditionalRule(sewerageApplicationDetails.getApplicationType().getCode());
        prepareWorkflow(model, sewerageApplicationDetails, sewerageApplicationDetails.getWorkflowContainer());
        model.addAttribute("additionalRule", sewerageApplicationDetails.getApplicationType().getCode());
        model.addAttribute("propertyTypes", PropertyType.values());
        final List<SewerageApplicationDetailsDocument> docList = sewerageConnectionService
                .getSewerageApplicationDoc(sewerageApplicationDetails);
        model.addAttribute("documentNamesList", docList);
        return "closeSewerageConnection";
    }

    @PostMapping("/closeSewerageConnection-update/{applicationNumber}")
    public String update(@Valid @ModelAttribute final SewerageApplicationDetails sewerageApplicationDetails,
                         final BindingResult resultBinder, final HttpServletRequest request, final HttpSession session, final Model model,
                         @RequestParam("files") final MultipartFile[] files, final HttpServletResponse response) {
        String workFlowAction = sewerageApplicationDetails.getWorkflowContainer().getWorkFlowAction();

        sewerageApplicationValidator.validateUpdateClosureApplication(sewerageApplicationDetails, resultBinder, workFlowAction);

        if (resultBinder.hasErrors()) {
            model.addAttribute(SEWERAGE_APPLICATION_DETAILS, sewerageApplicationDetails);
            final AssessmentDetails propertyOwnerDetails = sewerageThirdPartyServices
                    .getPropertyDetails(sewerageApplicationDetails.getConnection().getShscNumber(), request);
            if (propertyOwnerDetails != null)
                model.addAttribute("propertyOwnerDetails", propertyOwnerDetails);
            model.addAttribute("stateType", sewerageApplicationDetails.getClass().getSimpleName());
            model.addAttribute(SEWERAGE_APPLICATION_DETAILS, sewerageApplicationDetails);
            model.addAttribute("approvalDepartmentList", departmentService.getAllDepartments());
            model.addAttribute("currentState", sewerageApplicationDetails.getCurrentState().getValue());
            sewerageApplicationDetails.getWorkflowContainer().setAdditionalRule(sewerageApplicationDetails.getApplicationType().getCode());
            prepareWorkflow(model, sewerageApplicationDetails, sewerageApplicationDetails.getWorkflowContainer());
            model.addAttribute("additionalRule", sewerageApplicationDetails.getApplicationType().getCode());
            model.addAttribute("propertyTypes", PropertyType.values());
            final List<SewerageApplicationDetailsDocument> docList = sewerageConnectionService
                    .getSewerageApplicationDoc(sewerageApplicationDetails);
            model.addAttribute("documentNamesList", docList);
            return "closeSewerageConnection";
        }

        try {
            if (workFlowAction != null && !workFlowAction.isEmpty()
                    && workFlowAction.equalsIgnoreCase(APPROVEWORKFLOWACTION)) {
                final SewerageApplicationDetails parentSewerageAppDtls = sewerageApplicationDetailsService
                        .findByShscNumberAndIsActive(sewerageApplicationDetails.getConnection().getShscNumber());
                if (parentSewerageAppDtls != null) {
                    parentSewerageAppDtls.setActive(false);
                    sewerageApplicationDetails.setParent(parentSewerageAppDtls);
                }
            }

            sewerageApplicationDetailsService.updateCloseSewerageApplicationDetails(sewerageApplicationDetails,
                    null, request, session);
        } catch (final ValidationException e) {
            throw new ValidationException(e.getMessage());
        }
        if (workFlowAction != null && !workFlowAction.isEmpty()){
            if(workFlowAction.equalsIgnoreCase(WF_STATE_CONNECTION_CLOSE_BUTTON)
	                && sewerageApplicationDetails.getClosureNoticeNumber() != null)
	            return "redirect:/transactions/viewcloseconnectionnotice/" + sewerageApplicationDetails.getApplicationNumber()
	                    + "?closureNoticeNumber=" + sewerageApplicationDetails.getClosureNoticeNumber();
	        if (WFLOW_ACTION_STEP_CANCEL.equalsIgnoreCase(workFlowAction))
	            return "redirect:/transactions/rejectionnotice?pathVar="
	                    + sewerageApplicationDetails.getApplicationNumber() + "&" + "approvalComent="
	                    + sewerageApplicationDetails.getWorkflowContainer().getApproverComments();
        }

        final Assignment currentUserAssignment = assignmentService.getPrimaryAssignmentForGivenRange(securityUtils
                .getCurrentUser().getId(), new Date(), new Date());
        Assignment assignObj = null;
        List<Assignment> assignmentList = new ArrayList<>();
        Long approvalPosition = sewerageApplicationDetails.getWorkflowContainer().getApproverPositionId();
		if (sewerageTaxUtils.isAnonymousUser(securityUtils.getCurrentUser())
				&& (approvalPosition == null || approvalPosition == 0))
			approvalPosition = sewerageApplicationDetails.getState().getOwnerPosition().getId();
        if (approvalPosition == null || approvalPosition == 0)
            approvalPosition = assignmentService.getPrimaryAssignmentForUser(sewerageApplicationDetails.getCreatedBy().getId())
                    .getPosition().getId();
        if (approvalPosition != null)
            assignObj = assignmentService.getPrimaryAssignmentForPositon(approvalPosition);
        if (assignObj != null) {
            assignmentList.add(assignObj);
        } else if (assignObj == null && approvalPosition != null)
            assignmentList = assignmentService.getAssignmentsForPosition(approvalPosition, new Date());
        String nextDesign = assignmentList.isEmpty() ? "" : assignmentList.get(0).getDesignation().getName();
        final String pathVars = sewerageApplicationDetails.getApplicationNumber() + ","
                + sewerageTaxUtils.getApproverName(approvalPosition) + ","
                + (currentUserAssignment != null ? currentUserAssignment.getDesignation().getName() : "") + ","
                + (nextDesign != null ? nextDesign : "");
        return "redirect:/transactions/closeConnection-success?pathVars=" + pathVars;
    }

    @GetMapping("/viewcloseconnectionnotice/{applicationNumber}")
    public ResponseEntity<byte[]> viewCloseConnectionNotice(
            @ModelAttribute final SewerageApplicationDetails sewerageApplicationDetails,
            final HttpSession session, final HttpServletResponse response, final HttpServletRequest request) {
        final ReportOutput reportOutput = new ReportOutput();
        final HttpHeaders headers = new HttpHeaders();
        SewerageNotice sewerageNotice = null;
        File file = null;
        FileStoreMapper fmp = null;
        final String closureNoticeNumber = request.getParameter("closureNoticeNumber");
        if (closureNoticeNumber != null)
            sewerageNotice = sewerageNoticeService.findByNoticeNoAndNoticeType(closureNoticeNumber,
                    NOTICE_TYPE_CLOSER_NOTICE);
        if (sewerageNotice != null)
            fmp = sewerageNotice.getFileStore();
        if (fmp != null)
            file = fileStoreService.fetch(fmp, FILESTORE_MODULECODE);
        try {
            if (file != null)
                reportOutput.setReportOutputData(FileUtils.readFileToByteArray(file));
            reportOutput.setReportFormat(ReportFormat.PDF);
        } catch (final IOException ioe) {
            LOGGER.error("Exception while generating close connection notice", ioe);
            throw new ValidationException(ioe.getMessage());
        }
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("content-disposition", "inline;filename=CloseConnectionNotice.pdf");
        return new ResponseEntity<>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);
    }
}
