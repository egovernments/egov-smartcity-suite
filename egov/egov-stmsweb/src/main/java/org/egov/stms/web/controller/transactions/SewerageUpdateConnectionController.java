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

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.ValidationException;

import org.apache.commons.lang3.StringUtils;
import org.egov.commons.service.UOMService;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.DateUtils;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.PropertyTaxDetails;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.stms.masters.entity.FeesDetailMaster;
import org.egov.stms.masters.entity.enums.OwnerOfTheRoad;
import org.egov.stms.masters.entity.enums.PropertyType;
import org.egov.stms.masters.service.FeesDetailMasterService;
import org.egov.stms.transactions.charges.SewerageChargeCalculationService;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.transactions.entity.SewerageApplicationDetailsDocument;
import org.egov.stms.transactions.entity.SewerageConnectionEstimationDetails;
import org.egov.stms.transactions.entity.SewerageConnectionFee;
import org.egov.stms.transactions.entity.SewerageFieldInspection;
import org.egov.stms.transactions.entity.SewerageFieldInspectionDetails;
import org.egov.stms.transactions.service.SewerageApplicationDetailsService;
import org.egov.stms.transactions.service.SewerageConnectionFeeService;
import org.egov.stms.transactions.service.SewerageConnectionService;
import org.egov.stms.transactions.service.SewerageDemandService;
import org.egov.stms.transactions.service.SewerageEstimationDetailsService;
import org.egov.stms.transactions.service.SewerageFieldInspectionDetailsService;
import org.egov.stms.transactions.service.SewerageThirdPartyServices;
import org.egov.stms.utils.SewerageInspectionDetailsComparatorById;
import org.egov.stms.utils.SewerageTaxUtils;
import org.egov.stms.utils.constants.SewerageTaxConstants;
import org.egov.stms.web.controller.utils.SewerageApplicationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_FEEPAID;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_TYPE_NAME_NEWCONNECTION;
import static org.egov.stms.utils.constants.SewerageTaxConstants.EDIT_DONATION_CHARGE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.NEWSEWERAGECONNECTION;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_CSCCREATED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_ANONYMOUSCREATED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WF_STATE_CONNECTION_EXECUTION_BUTTON;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_CREATED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_INSPECTIONFEEPAID;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_WARDSECRETARYCREATED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.CHANGEINCLOSETS;
import static org.egov.stms.utils.constants.SewerageTaxConstants.CLOSESEWERAGECONNECTION;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WF_STATE_REJECTED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WFPA_REJECTED_INSPECTIONFEE_COLLECTION;
import static org.egov.stms.utils.constants.SewerageTaxConstants.FEES_ESTIMATIONCHARGES_CODE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_INITIALAPPROVED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_FEECOLLECTIONPENDING;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_DEEAPPROVED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WFLOW_ACTION_STEP_FORWARD;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WF_ESTIMATION_NOTICE_BUTTON;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WFLOW_ACTION_STEP_REJECT;
import static org.egov.stms.utils.constants.SewerageTaxConstants.PREVIEWWORKFLOWACTION;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WF_CLOSERACKNOWLDGEENT_BUTTON;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WFLOW_ACTION_STEP_CANCEL;
import static org.egov.stms.utils.constants.SewerageTaxConstants.FEES_DONATIONCHARGE_CODE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.FEES_SEWERAGETAX_CODE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.getPipeScrewSizes;
import static org.egov.stms.utils.constants.SewerageTaxConstants.MODE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.NOT_AUTHORIZED;
@Controller
@RequestMapping(value = "/transactions")
public class SewerageUpdateConnectionController extends GenericWorkFlowController {

    private static final String APPROVAL_COMENT = "approvalComent";
    private static final String NEW = "NEW";
    private static final String IS_INSPECTION_FEE_PAID = "isInspectionFeePaid";
    private static final String ADDITIONAL_RULE = "additionalRule";
    private static final String CURRENT_STATE = "currentState";
    private static final String SEWERAGE_APPLICATION_DETAILS = "sewerageApplicationDetails";
    private static final String NEWCONNECTION_EDIT = "newconnection-edit";
    private static final String INSPECTIONDATE = "inspectionDate";
    
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
    private FeesDetailMasterService feesDetailMasterService;

    @Autowired
    private UOMService uOMService;

    @Autowired
    private SewerageChargeCalculationService sewerageChargeCalculationService;

    @Autowired
    private SewerageConnectionFeeService sewerageConnectionFeeService;

    @Autowired
    private SewerageConnectionService sewerageConnectionService;

    @Autowired
    private SewerageEstimationDetailsService sewerageEstimationDetailsService;

    @Autowired
    private SewerageFieldInspectionDetailsService sewerageFieldInspectionDetailsService;

    @Autowired
    private PropertyExternalService propertyExternalService;

    @Autowired
    private SewerageThirdPartyServices sewerageThirdPartyServices;

    @Autowired
    private SewerageApplicationValidator sewerageApplicationValidator;

    @Autowired
    private SewerageDemandService sewerageDemandService;

    @ModelAttribute(SEWERAGE_APPLICATION_DETAILS)
    public SewerageApplicationDetails getSewerageApplicationDetails(@PathVariable final String applicationNumber) {
        return sewerageApplicationDetailsService.findByApplicationNumber(applicationNumber);
    }

    @GetMapping("/citizenupdate/{applicationNumber}")
    public String citizenview(final Model model, @PathVariable final String applicationNumber, final HttpServletRequest request) {
        Boolean isInspectionFeePaid = Boolean.FALSE;
        final SewerageApplicationDetails sewerageApplicationDetails = sewerageApplicationDetailsService
                .findByApplicationNumber(applicationNumber);
        if (sewerageApplicationDetails.getEstimationDetails() != null
                && !sewerageApplicationDetails.getEstimationDetails().isEmpty())
            sewerageApplicationDetails.setEstimationDetailsForUpdate(sewerageApplicationDetails.getEstimationDetails());

        if (sewerageApplicationDetails.getFieldInspections() != null
                && !sewerageApplicationDetails.getFieldInspections().isEmpty()
                && sewerageApplicationDetails.getFieldInspections().get(0) != null
                && sewerageApplicationDetails.getFieldInspections().get(0).getFieldInspectionDetails() != null)
            sewerageApplicationDetails.getFieldInspections().get(0).setFieldInspectionDetailsForUpdate(
                    sewerageApplicationDetails.getFieldInspections().get(0).getFieldInspectionDetails());
        if (sewerageApplicationDetails.getCurrentDemand() != null
                && !sewerageDemandService.checkAnyTaxIsPendingToCollect(sewerageApplicationDetails.getCurrentDemand()))
            isInspectionFeePaid = Boolean.TRUE;
        model.addAttribute(IS_INSPECTION_FEE_PAID, isInspectionFeePaid);
        model.addAttribute(ADDITIONAL_RULE, sewerageApplicationDetails.getApplicationType().getCode());
        sewerageApplicationDetails.getWorkflowContainer().setAdditionalRule(sewerageApplicationDetails.getApplicationType().getCode());
        sewerageApplicationDetails.getWorkflowContainer().setCurrentDesignation(sewerageApplicationDetails.hasState()
                ? "%" + sewerageApplicationDetails.getCurrentState().getOwnerPosition()
                .getDeptDesig().getDesignation().getName() + "%" : EMPTY);
        prepareWorkflow(model, sewerageApplicationDetails, sewerageApplicationDetails.getWorkflowContainer());
        model.addAttribute("isCitizenPortalUser", securityUtils.currentUserIsCitizen());
        model.addAttribute(CURRENT_STATE, sewerageApplicationDetails.getCurrentState().getValue());
        model.addAttribute(SEWERAGE_APPLICATION_DETAILS, sewerageApplicationDetails);
        return NEWCONNECTION_EDIT;

    }

    @GetMapping("/update/{applicationNumber}")
    public String view(final Model model, @PathVariable final String applicationNumber, final HttpServletRequest request) {
        final SewerageApplicationDetails sewerageApplicationDetails = sewerageApplicationDetailsService
                .findByApplicationNumber(applicationNumber);
        if (!sewerageApplicationDetailsService.isApplicationOwner(securityUtils.getCurrentUser(), sewerageApplicationDetails))
            return NOT_AUTHORIZED;
        if (sewerageApplicationDetails.getApplicationType().getCode().equalsIgnoreCase(CHANGEINCLOSETS))
            return "redirect:/transactions/modifyConnection-update/" + applicationNumber;
        else if (sewerageApplicationDetails.getApplicationType().getCode()
                .equalsIgnoreCase(CLOSESEWERAGECONNECTION))
            return "redirect:/transactions/closeSewerageConnection-update/" + applicationNumber;

        if (sewerageApplicationDetails.getEstimationDetails() != null
                && !sewerageApplicationDetails.getEstimationDetails().isEmpty())
            sewerageApplicationDetails.setEstimationDetailsForUpdate(sewerageApplicationDetails.getEstimationDetails());

        if (sewerageApplicationDetails.getFieldInspections() != null
                && !sewerageApplicationDetails.getFieldInspections().isEmpty()
                && sewerageApplicationDetails.getFieldInspections().get(0) != null
                && sewerageApplicationDetails.getFieldInspections().get(0).getFieldInspectionDetails() != null)
            sewerageApplicationDetails.getFieldInspections().get(0).setFieldInspectionDetailsForUpdate(
                    sewerageApplicationDetails.getFieldInspections().get(0).getFieldInspectionDetails());
        model.addAttribute(SEWERAGE_APPLICATION_DETAILS, sewerageApplicationDetails);
        setCommonDetails(sewerageApplicationDetails, model, request);
        return loadViewData(model, request, sewerageApplicationDetails);
    }

    public String loadViewData(final Model model, final HttpServletRequest request,
                               final SewerageApplicationDetails sewerageApplicationDetails) {
        model.addAttribute("stateType", sewerageApplicationDetails.getClass().getSimpleName());

        AppConfigValues editDonationCharge = sewerageTaxUtils.getAppConfigValues(EDIT_DONATION_CHARGE);
        model.addAttribute("editdonationcharge",
                editDonationCharge != null && ("YES").equalsIgnoreCase(editDonationCharge.getValue()) ? true : false);
        for (final SewerageFieldInspection fieldInspection : sewerageApplicationDetails.getFieldInspections())
            Collections.sort(fieldInspection.getFieldInspectionDetails(), new SewerageInspectionDetailsComparatorById());

        model.addAttribute(CURRENT_STATE, sewerageApplicationDetails.getCurrentState().getValue());
        if (sewerageApplicationDetails.getCurrentState().getValue().equalsIgnoreCase(WF_STATE_REJECTED))
            if (sewerageTaxUtils.isInspectionFeeCollectionRequired())
                model.addAttribute("pendingActions", WFPA_REJECTED_INSPECTIONFEE_COLLECTION);
            else
                model.addAttribute("pendingActions", WF_STATE_REJECTED);
        if (sewerageApplicationDetails.getCurrentState().getNatureOfTask().equals(APPLICATION_TYPE_NAME_NEWCONNECTION)
                && sewerageApplicationDetails.getStatus().getCode().equals(APPLICATION_STATUS_FEEPAID)) {
            sewerageApplicationDetails.getWorkflowContainer().setAdditionalRule(NEWSEWERAGECONNECTION);
        } else
            sewerageApplicationDetails.getWorkflowContainer().
                    setAdditionalRule(sewerageApplicationDetails.getApplicationType().getCode());


        prepareWorkflow(model, sewerageApplicationDetails, sewerageApplicationDetails.getWorkflowContainer());

        model.addAttribute(SEWERAGE_APPLICATION_DETAILS, sewerageApplicationDetails);
        model.addAttribute("applicationHistory",
                sewerageApplicationDetailsService.populateHistory(sewerageApplicationDetails));
        model.addAttribute("approvalDepartmentList", departmentService.getAllDepartments());

        model.addAttribute("pipeSize", getPipeScrewSizes());
        model.addAttribute("roadOwner", OwnerOfTheRoad.values());

        model.addAttribute("uomList", uOMService.findAllOrderByCategory());

        final Map<String, String> modelParams = sewerageApplicationDetailsService
                .showApprovalDetailsByApplcationCurState(sewerageApplicationDetails);
        model.addAttribute(MODE, modelParams.get(MODE));
        model.addAttribute("showApprovalDtls", modelParams.get("showApprovalDtls"));

        if ("edit".equalsIgnoreCase(modelParams.get(MODE))) {
            final FeesDetailMaster fdm = feesDetailMasterService.findByCodeAndIsActive(
                    FEES_ESTIMATIONCHARGES_CODE, true);
            final List<SewerageConnectionFee> connectionFeeList = sewerageConnectionFeeService
                    .findAllByApplicationDetailsAndFeesDetail(sewerageApplicationDetails, fdm);
            if (connectionFeeList.isEmpty())
                createSewerageConnectionFee(sewerageApplicationDetails,
                        FEES_ESTIMATIONCHARGES_CODE);
        }

        if (sewerageApplicationDetails.getStatus() != null &&
                (sewerageApplicationDetails.getStatus().getCode()
                        .equalsIgnoreCase(APPLICATION_STATUS_INITIALAPPROVED) ||
                        sewerageApplicationDetails.getStatus().getCode()
                                .equalsIgnoreCase(APPLICATION_STATUS_INSPECTIONFEEPAID)))
            populateDonationSewerageTax(sewerageApplicationDetailsService.findBy(sewerageApplicationDetails.getId()));

        if (sewerageApplicationDetails != null && sewerageApplicationDetails.getFieldInspections() != null &&
                !sewerageApplicationDetails.getFieldInspections().isEmpty()) {
            sewerageApplicationDetails.getFieldInspections().get(0).setApplicationDetails(sewerageApplicationDetails);
            if (StringUtils.isNotBlank(request.getParameter(INSPECTIONDATE)))
                sewerageApplicationDetails.getFieldInspections().get(0)
                        .setInspectionDate(DateUtils.toDateUsingDefaultPattern(request.getParameter(INSPECTIONDATE)));
        }
        if (sewerageApplicationDetails != null
                && sewerageApplicationDetails.getStatus() != null
                && (APPLICATION_STATUS_CSCCREATED.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode())
                || APPLICATION_STATUS_ANONYMOUSCREATED.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode())
                || APPLICATION_STATUS_WARDSECRETARYCREATED.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode()))
                && NEW.equalsIgnoreCase(sewerageApplicationDetails.getState().getValue())) {
            model.addAttribute("isReassignEnabled", sewerageTaxUtils.isReassignEnabled());
        }
        // Pending: To Support Documents Re-Attachment on Edit mode
        if ("editOnReject".equals(modelParams.get(MODE))) {
            final List<SewerageApplicationDetailsDocument> docList = sewerageConnectionService
                    .getSewerageApplicationDoc(sewerageApplicationDetails);
            model.addAttribute("documentNamesList", docList);

        } else if (sewerageApplicationDetails.getConnectionDetail().getPropertyType() != null)
            model.addAttribute("documentNamesList",
                    sewerageConnectionService.getSewerageApplicationDoc(sewerageApplicationDetails));

        if (sewerageApplicationDetails.getConnectionDetail().getPropertyType() != null) {
            final BigDecimal sewerageTaxDue = sewerageApplicationDetailsService.getTotalAmount(sewerageApplicationDetails);
            model.addAttribute("sewerageTaxDue", sewerageTaxDue);
        }
        model.addAttribute("propertyTypes", PropertyType.values());
        return NEWCONNECTION_EDIT;
    }

    private void setCommonDetails(final SewerageApplicationDetails sewerageApplicationDetails, final Model model,
                                  final HttpServletRequest request) {
        final String assessmentNumber = sewerageApplicationDetails.getConnectionDetail()
                .getPropertyIdentifier();
        final AssessmentDetails propertyOwnerDetails = sewerageThirdPartyServices.getPropertyDetails(assessmentNumber,
                request);
        if (propertyOwnerDetails != null)
            model.addAttribute("propertyOwnerDetails", propertyOwnerDetails);
        final PropertyTaxDetails propertyTaxDetails = propertyExternalService.getPropertyTaxDetails(assessmentNumber, null, null);
        model.addAttribute("propertyTax", propertyTaxDetails.getTotalTaxAmt());
    }

    public void createSewerageConnectionFee(final SewerageApplicationDetails sewerageApplicationDetails, final String feeCode) {
        final List<FeesDetailMaster> inspectionFeeList = feesDetailMasterService.findAllActiveFeesDetailByFeesCode(feeCode);
        for (final FeesDetailMaster feeDetailMaster : inspectionFeeList) {
            final SewerageConnectionFee connectionFee = new SewerageConnectionFee();
            connectionFee.setFeesDetail(feeDetailMaster);
            if (feeDetailMaster.getIsFixedRate())
                connectionFee.setAmount(feeDetailMaster.getAmount().doubleValue());
            connectionFee.setApplicationDetails(sewerageApplicationDetails);
            sewerageApplicationDetails.getConnectionFees().add(connectionFee);
        }
    }

    @PostMapping("/update/{applicationNumber}")
    public String update(@Valid @ModelAttribute SewerageApplicationDetails sewerageApplicationDetails,
                         final BindingResult resultBinder, final HttpServletRequest request,
                         final HttpSession session, final Model model, @RequestParam("files") final MultipartFile[] files) {
        String mode = EMPTY;
        boolean generateDemandVoucher = false;
        WorkflowContainer workflowContainer = sewerageApplicationDetails.getWorkflowContainer();
        String workFlowAction = workflowContainer.getWorkFlowAction();
        sewerageApplicationValidator.validateNewApplicationUpdate(sewerageApplicationDetails, resultBinder, workFlowAction);

        if (workFlowAction != null && WF_STATE_CONNECTION_EXECUTION_BUTTON.equalsIgnoreCase(workFlowAction)) {
            final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            if (sewerageApplicationDetails.getConnection().getExecutionDate() == null)
                resultBinder.rejectValue("connection.executionDate", "err.executiondate.required.validate");
            else if (isNotEmpty(sewerageApplicationDetails.getConnection().getExecutionDate().toString())) {
                formatter.format(sewerageApplicationDetails.getConnection().getExecutionDate());
                if (sewerageApplicationDetails.getConnection().getExecutionDate()
                        .compareTo(sewerageApplicationDetails.getApplicationDate()) < 0)
                    resultBinder.reject("err.connectionexecution.date.validate",
                            new String[]{formatter.format(sewerageApplicationDetails.getApplicationDate())},
                            "err.connectionexecution.date.validate");
            }
        }
        if (request.getParameter(MODE) != null)
            mode = request.getParameter(MODE);

        if (resultBinder.hasErrors()) {
            model.addAttribute("sewerageApplcationDetails", sewerageApplicationDetails);
            loadViewData(model, request, sewerageApplicationDetails);
            return NEWCONNECTION_EDIT;
        }
        
        
        if (isNotBlank(workFlowAction) && (WFLOW_ACTION_STEP_FORWARD.equalsIgnoreCase(workFlowAction)   
                 && sewerageApplicationDetails.getWorkflowContainer().getApproverPositionId() != null
                && sewerageApplicationDetails.getWorkflowContainer().getApproverPositionId() != 0)) {
            boolean isValidApprover = sewerageApplicationDetailsService.isValidApprover(sewerageApplicationDetails);    
            if (!isValidApprover) {
                model.addAttribute("approverError", "Invalid approver");
                model.addAttribute("sewerageApplcationDetails", sewerageApplicationDetails);
                loadViewData(model, request, sewerageApplicationDetails);
                model.addAttribute(MODE, "error");
                return NEWCONNECTION_EDIT;
            }
        }

        if ((sewerageApplicationDetails.getStatus().getCode().equalsIgnoreCase(APPLICATION_STATUS_CREATED)
                || sewerageApplicationDetails.getStatus().getCode().equalsIgnoreCase(APPLICATION_STATUS_INSPECTIONFEEPAID)
                || sewerageApplicationDetails.getStatus().getCode().equalsIgnoreCase(APPLICATION_STATUS_FEECOLLECTIONPENDING)
                || sewerageApplicationDetails.getStatus().getCode().equalsIgnoreCase(APPLICATION_STATUS_INITIALAPPROVED)
                || sewerageApplicationDetails.getStatus().getCode().equalsIgnoreCase(APPLICATION_STATUS_DEEAPPROVED))
                && "edit".equalsIgnoreCase(mode))
            if (WFLOW_ACTION_STEP_FORWARD.equalsIgnoreCase(workFlowAction)
                    || WF_ESTIMATION_NOTICE_BUTTON.equalsIgnoreCase(workFlowAction)) {

                final List<SewerageConnectionEstimationDetails> existingSewerage = populateEstimationDetails(
                        sewerageApplicationDetails);

                final List<SewerageFieldInspectionDetails> existingInspection = populateInspectionDetails(
                        sewerageApplicationDetails, request, files);

                populateFeesDetails(sewerageApplicationDetails);
                sewerageApplicationDetailsService.save(sewerageApplicationDetails);
                if (existingSewerage != null && !existingSewerage.isEmpty())
                    sewerageEstimationDetailsService.deleteAllInBatch(existingSewerage);

                if (existingInspection != null && !existingInspection.isEmpty())
                    sewerageFieldInspectionDetailsService.deleteAllInBatch(existingInspection);

            } else if (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_REJECT)) {
                sewerageApplicationDetailsService.getCurrentSession().evict(sewerageApplicationDetails);
                sewerageApplicationDetails = sewerageApplicationDetailsService.findBy(sewerageApplicationDetails
                        .getId());
                sewerageApplicationDetails.setWorkflowContainer(workflowContainer);
            }

        if ((sewerageApplicationDetails.getStatus().getCode() != null
                && sewerageApplicationDetails.getStatus().getCode().equalsIgnoreCase(APPLICATION_STATUS_INITIALAPPROVED)
                || sewerageApplicationDetails.getStatus().getCode().equalsIgnoreCase(APPLICATION_STATUS_DEEAPPROVED)
                || sewerageApplicationDetails.getStatus().getCode().equalsIgnoreCase(APPLICATION_STATUS_INSPECTIONFEEPAID))
                && !workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_REJECT)) {
            populateDonationSewerageTax(sewerageApplicationDetails); 
            if(sewerageApplicationDetails.getStatus().getCode().equalsIgnoreCase(APPLICATION_STATUS_INITIALAPPROVED) 
            		&& SewerageTaxConstants.NEWSEWERAGECONNECTION.equalsIgnoreCase(sewerageApplicationDetails.getApplicationType().getCode())
            		&& SewerageTaxConstants.WF_STATE_ASSISTANT_APPROVED.equalsIgnoreCase(sewerageApplicationDetails.getState().getValue())) {
            	generateDemandVoucher = true;
            }
        }
        if (!resultBinder.hasErrors()) {
            try {
                if (workFlowAction != null && workFlowAction.equalsIgnoreCase(PREVIEWWORKFLOWACTION)
                        && sewerageApplicationDetails.getApplicationType().getCode()
                        .equals(NEWSEWERAGECONNECTION))
                    return "redirect:/transactions/workorder?pathVar="
                            + sewerageApplicationDetails.getApplicationNumber();
                sewerageApplicationDetailsService
                        .updateSewerageApplicationDetails(sewerageApplicationDetails, mode, request, session, generateDemandVoucher);
            } catch (final ValidationException e) {
                throw new ValidationException(e);
            }
            if (workFlowAction != null && !workFlowAction.isEmpty()
                    && workFlowAction.equalsIgnoreCase(WF_ESTIMATION_NOTICE_BUTTON))
                return "redirect:/transactions/estimationnotice?pathVar="
                        + sewerageApplicationDetails.getApplicationNumber();
            if (workFlowAction != null && !workFlowAction.isEmpty()
                    && workFlowAction.equalsIgnoreCase(WF_CLOSERACKNOWLDGEENT_BUTTON))
                return "redirect:/applications/acknowlgementNotice?pathVar="
                        + sewerageApplicationDetails.getApplicationNumber();
            if (workFlowAction != null && !workFlowAction.isEmpty()
                    && workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_CANCEL))
                return "redirect:/transactions/rejectionnotice?pathVar="
                        + sewerageApplicationDetails.getApplicationNumber() + "&" + "approvalComent=" + workflowContainer.getApproverComments();
            final Assignment currentUserAssignment = assignmentService.getPrimaryAssignmentForGivenRange(securityUtils
                    .getCurrentUser().getId(), new Date(), new Date());
            String nextDesign;
            Assignment assignObj = null;
            List<Assignment> asignList = Collections.emptyList();
            Long approverPosition = workflowContainer.getApproverPositionId();
            if (approverPosition == null || approverPosition == 0) {
                approverPosition = sewerageApplicationDetails.getState().getInitiatorPosition().getId();
            }

            if (approverPosition != null)
                assignObj = assignmentService.getPrimaryAssignmentForPositon(approverPosition);
            if (assignObj != null) {
                asignList = new ArrayList<>();
                asignList.add(assignObj);
            } else if (assignObj == null && approverPosition != null)
                asignList = assignmentService.getAssignmentsForPosition(approverPosition, new Date());

            nextDesign = asignList.isEmpty() ? StringUtils.EMPTY : asignList.get(0).getDesignation().getName();

            final String pathVars = sewerageApplicationDetails.getApplicationNumber() + ","
                    + sewerageTaxUtils.getApproverName(approverPosition) + ","
                    + (currentUserAssignment != null ? currentUserAssignment.getDesignation().getName() : "") + ","
                    + (nextDesign != null ? nextDesign : "");
            return "redirect:/transactions/application-success?pathVars=" + pathVars;
        } else
            return loadViewData(model, request, sewerageApplicationDetails);
    }

    private void populateDonationSewerageTax(final SewerageApplicationDetails sewerageApplicationDetails) {

        final FeesDetailMaster donationCharge = feesDetailMasterService.findByCodeAndIsActive(
                FEES_DONATIONCHARGE_CODE, true);
        final List<SewerageConnectionFee> donationChargeList = sewerageConnectionFeeService
                .findAllByApplicationDetailsAndFeesDetail(sewerageApplicationDetails, donationCharge);
        if (donationChargeList.isEmpty()) {

            final SewerageConnectionFee connectionFee = new SewerageConnectionFee();
            connectionFee.setFeesDetail(donationCharge);
            connectionFee.setAmount(sewerageChargeCalculationService.calculateDonationCharges(
                    sewerageApplicationDetails).doubleValue());
            connectionFee.setApplicationDetails(sewerageApplicationDetails);
            sewerageApplicationDetails.getConnectionFees().add(connectionFee);
        }

        final FeesDetailMaster sewerageTax = feesDetailMasterService.findByCodeAndIsActive(
                FEES_SEWERAGETAX_CODE, true);
        final List<SewerageConnectionFee> sewerageTaxFeeList = sewerageConnectionFeeService
                .findAllByApplicationDetailsAndFeesDetail(sewerageApplicationDetails, sewerageTax);
        if (sewerageTaxFeeList.isEmpty()) {
            SewerageConnectionFee connectionFee;
            connectionFee = new SewerageConnectionFee();
            connectionFee.setFeesDetail(sewerageTax);
            connectionFee.setAmount(sewerageChargeCalculationService.calculateSewerageCharges(
                    sewerageApplicationDetails).doubleValue());
            connectionFee.setApplicationDetails(sewerageApplicationDetails);
            sewerageApplicationDetails.getConnectionFees().add(connectionFee);
        } else
            for (SewerageConnectionFee connectionFees : sewerageApplicationDetails.getConnectionFees()) {
                if (connectionFees.getFeesDetail().equals(sewerageTax))
                    connectionFees.setAmount(sewerageChargeCalculationService.calculateSewerageCharges(
                            sewerageApplicationDetails).doubleValue());
            }

    }

    private List<SewerageConnectionEstimationDetails> populateEstimationDetails(
            final SewerageApplicationDetails sewerageApplicationDetails) {

        final List<SewerageConnectionEstimationDetails> sewerageConnectionEstimationDetailList = new ArrayList<>();
        final List<SewerageConnectionEstimationDetails> existingSewerage = new ArrayList<>();

        if (!sewerageApplicationDetails.getEstimationDetailsForUpdate().isEmpty()) {
            for (final SewerageConnectionEstimationDetails estimationDetails : sewerageApplicationDetails
                    .getEstimationDetails())
                existingSewerage.add(estimationDetails);
            for (final SewerageConnectionEstimationDetails estimationDetail : sewerageApplicationDetails
                    .getEstimationDetailsForUpdate())
                if (validSewerageEstimationDetail(estimationDetail)) {
                    final SewerageConnectionEstimationDetails estimationDtl = new SewerageConnectionEstimationDetails();
                    estimationDtl.setAmount(estimationDetail.getAmount());
                    estimationDtl.setItemDescription(estimationDetail.getItemDescription());
                    estimationDtl.setQuantity(estimationDetail.getQuantity());
                    estimationDtl.setUnitOfMeasurement(estimationDetail.getUnitOfMeasurement());
                    estimationDtl.setUnitRate(estimationDetail.getUnitRate());
                    estimationDtl.setApplicationDetails(sewerageApplicationDetails);
                    sewerageConnectionEstimationDetailList.add(estimationDtl);

                }
        }
        sewerageApplicationDetails.setEstimationDetails(sewerageConnectionEstimationDetailList);
        if (!existingSewerage.isEmpty())
            return existingSewerage;
        return Collections.emptyList();
    }

    private void populateFeesDetails(final SewerageApplicationDetails sewerageApplicationDetails) {
        if (!sewerageApplicationDetails.getConnectionFees().isEmpty())
            for (final SewerageConnectionFee scf : sewerageApplicationDetails.getConnectionFees())
                scf.setApplicationDetails(sewerageApplicationDetails);
    }

    @SuppressWarnings("unused")
    private List<SewerageFieldInspectionDetails> populateInspectionDetails(
            final SewerageApplicationDetails sewerageApplicationDetails,
            final HttpServletRequest request, final MultipartFile[] files) {
        new ArrayList<SewerageFieldInspectionDetails>();
        final String inspectionDate = request.getParameter(INSPECTIONDATE);

        final List<SewerageFieldInspectionDetails> existingInspectionDtlList = new ArrayList<>();

        if (!sewerageApplicationDetails.getFieldInspections().isEmpty())
            for (final SewerageFieldInspection sewerageFieldInspection : sewerageApplicationDetails
                    .getFieldInspections()) {
                final List<SewerageFieldInspectionDetails> sewerageFieldInspectionDetailList = new ArrayList<>();

                sewerageFieldInspection.setApplicationDetails(sewerageApplicationDetails);
                sewerageFieldInspection.setInspectionDate(DateUtils.toDateUsingDefaultPattern(inspectionDate));
                final Set<FileStoreMapper> fileStoreSet = sewerageTaxUtils.addToFileStore(files);
                Iterator<FileStoreMapper> fsIterator = null;
                if (fileStoreSet != null && !fileStoreSet.isEmpty())
                    fsIterator = fileStoreSet.iterator();

                if (fsIterator != null && fsIterator.hasNext())
                    sewerageFieldInspection.setFileStore(fsIterator.next());

                if (!sewerageFieldInspection.getFieldInspectionDetailsForUpdate().isEmpty()) {

                    for (final SewerageFieldInspectionDetails fieldInspectionDtls : sewerageFieldInspection
                            .getFieldInspectionDetails())
                        existingInspectionDtlList.add(fieldInspectionDtls);

                    for (final SewerageFieldInspectionDetails fieldInspectionDtl : sewerageFieldInspection
                            .getFieldInspectionDetailsForUpdate())
                        if (validSewerageFieldInspectionDetails(fieldInspectionDtl)) {
                            final SewerageFieldInspectionDetails sewerageFieldInspectionDetails = new SewerageFieldInspectionDetails();
                            sewerageFieldInspectionDetails.setNoOfPipes(fieldInspectionDtl.getNoOfPipes());
                            sewerageFieldInspectionDetails.setPipeSize(fieldInspectionDtl.getPipeSize());
                            sewerageFieldInspectionDetails.setPipeLength(fieldInspectionDtl.getPipeLength());
                            sewerageFieldInspectionDetails.setScrewSize(fieldInspectionDtl.getScrewSize());
                            sewerageFieldInspectionDetails.setNoOfScrews(fieldInspectionDtl.getNoOfScrews());
                            sewerageFieldInspectionDetails.setDistance(fieldInspectionDtl.getDistance());
                            sewerageFieldInspectionDetails.setRoadDigging(fieldInspectionDtl.isRoadDigging());
                            sewerageFieldInspectionDetails.setRoadLength(fieldInspectionDtl.getRoadLength());
                            sewerageFieldInspectionDetails.setRoadOwner(fieldInspectionDtl.getRoadOwner());
                            sewerageFieldInspectionDetails.setFieldInspection(sewerageFieldInspection);
                            sewerageFieldInspectionDetailList.add(sewerageFieldInspectionDetails);
                        }
                }
                sewerageFieldInspection.setFieldInspectionDetails(sewerageFieldInspectionDetailList);
            }

        if (existingInspectionDtlList != null)
            return existingInspectionDtlList;
        return Collections.emptyList();
    }

    private boolean validSewerageFieldInspectionDetails(
            final SewerageFieldInspectionDetails sewerageFieldInspectionDetails) {
        return sewerageFieldInspectionDetails != null && sewerageFieldInspectionDetails.getNoOfPipes() != null
                && sewerageFieldInspectionDetails.getNoOfPipes() != 0;
    }

    private boolean validSewerageEstimationDetail(
            final SewerageConnectionEstimationDetails sewerageConnectionEstimationDetails) {
        return sewerageConnectionEstimationDetails != null && isNotBlank(sewerageConnectionEstimationDetails.getItemDescription());
    }
}