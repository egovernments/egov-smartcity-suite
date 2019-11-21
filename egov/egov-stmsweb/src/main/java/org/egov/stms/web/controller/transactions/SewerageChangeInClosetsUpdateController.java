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
import java.text.ParseException;
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
import javax.validation.ValidationException;

import org.apache.commons.lang3.StringUtils;
import org.egov.commons.service.UOMService;
import org.egov.demand.model.EgDemandDetails;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.filestore.entity.FileStoreMapper;
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
import org.egov.stms.transactions.entity.SewerageConnectionEstimationDetails;
import org.egov.stms.transactions.entity.SewerageConnectionFee;
import org.egov.stms.transactions.entity.SewerageFieldInspection;
import org.egov.stms.transactions.entity.SewerageFieldInspectionDetails;
import org.egov.stms.transactions.service.SewerageApplicationDetailsService;
import org.egov.stms.transactions.service.SewerageConnectionFeeService;
import org.egov.stms.transactions.service.SewerageDemandService;
import org.egov.stms.transactions.service.SewerageEstimationDetailsService;
import org.egov.stms.transactions.service.SewerageFieldInspectionDetailsService;
import org.egov.stms.transactions.service.SewerageThirdPartyServices;
import org.egov.stms.transactions.service.SewerageWorkflowService;
import org.egov.stms.utils.SewerageInspectionDetailsComparatorById;
import org.egov.stms.utils.SewerageTaxUtils;
import org.egov.stms.web.controller.utils.SewerageApplicationValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import static java.math.BigDecimal.ZERO;
import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WF_STATE_REJECTED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WFPA_REJECTED_INSPECTIONFEE_COLLECTION;
import static org.egov.stms.utils.constants.SewerageTaxConstants.getPipeScrewSizes;
import static org.egov.stms.utils.constants.SewerageTaxConstants.FEES_ESTIMATIONCHARGES_CODE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_INITIALAPPROVED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_INSPECTIONFEEPAID;
import static org.egov.stms.utils.constants.SewerageTaxConstants.CHANGEINCLOSETS_NOCOLLECTION;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_DEEAPPROVED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.FEES_SEWERAGETAX_CODE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.FEES_DONATIONCHARGE_CODE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.FEES_ADVANCE_CODE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_CREATED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_FEECOLLECTIONPENDING;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WFLOW_ACTION_STEP_FORWARD;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WF_ESTIMATION_NOTICE_BUTTON;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WFLOW_ACTION_STEP_REJECT;
import static org.egov.stms.utils.constants.SewerageTaxConstants.PREVIEWWORKFLOWACTION;
import static org.egov.stms.utils.constants.SewerageTaxConstants.NEWSEWERAGECONNECTION;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WF_STATE_CONNECTION_EXECUTION_BUTTON;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WF_CLOSERACKNOWLDGEENT_BUTTON;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WFLOW_ACTION_STEP_CANCEL;

@Controller
@RequestMapping(value = "/transactions")
public class SewerageChangeInClosetsUpdateController extends GenericWorkFlowController {

    private static final String PENDING_ACTIONS = "pendingActions";
    private static final String CHANGE_IN_CLOSETS_EDIT = "changeInClosets-edit";
    private static final String ADDITIONAL_RULE = "additionalRule";
    private static final String SEWERAGE_APPLCATION_DETAILS = "sewerageApplcationDetails";
    private static final String INSPECTIONDATE = "inspectionDate";
    private static final String SHOW_APPROVAL_DETAILS = "showApprovalDtls";
    private static final String EDIT = "edit";
    private static final Logger LOG = LoggerFactory.getLogger(SewerageChangeInClosetsUpdateController.class);
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
    private SewerageEstimationDetailsService sewerageEstimationDetailsService;

    @Autowired
    private SewerageFieldInspectionDetailsService sewerageFieldInspectionDetailsService;

    @Autowired
    private PropertyExternalService propertyExternalService;

    @Autowired
    private SewerageThirdPartyServices sewerageThirdPartyServices;

    @Autowired
    private SewerageDemandService sewerageDemandService;

    @Autowired
    private SewerageApplicationValidator sewerageApplicationValidator;

    @Autowired
    private SewerageWorkflowService sewerageWorkflowService;

    @Autowired
    private DepartmentService departmentService;

    @ModelAttribute("sewerageApplicationDetails")
    public SewerageApplicationDetails getSewerageApplicationDetails(@PathVariable final String applicationNumber) {
        return sewerageApplicationDetailsService.findByApplicationNumber(applicationNumber);
    }

    @GetMapping("/citizenmodifyConnection-update/{applicationNumber}")
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

        model.addAttribute("isInspectionFeePaid", isInspectionFeePaid);
        model.addAttribute(SEWERAGE_APPLCATION_DETAILS, sewerageApplicationDetails);
        model.addAttribute(ADDITIONAL_RULE, sewerageApplicationDetails.getApplicationType().getCode());
        model.addAttribute("currentState", sewerageApplicationDetails.getCurrentState().getValue());
        sewerageApplicationDetails.getWorkflowContainer().setAdditionalRule(sewerageApplicationDetails.getApplicationType().getCode());
        prepareWorkflow(model, sewerageApplicationDetails, sewerageApplicationDetails.getWorkflowContainer());
        return CHANGE_IN_CLOSETS_EDIT;
    }

    @GetMapping("/modifyConnection-update/{applicationNumber}")
    public String view(final Model model, @PathVariable final String applicationNumber, final HttpServletRequest request) {
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
        model.addAttribute(SEWERAGE_APPLCATION_DETAILS, sewerageApplicationDetails);
        setCommonDetails(sewerageApplicationDetails, model, request);
        return loadViewData(model, request, sewerageApplicationDetails);
    }

    private void setCommonDetails(final SewerageApplicationDetails sewerageApplicationDetails, final Model model,
                                  final HttpServletRequest request) {
        final String assessmentNumber = sewerageApplicationDetails.getConnectionDetail().getPropertyIdentifier();
        final AssessmentDetails propertyOwnerDetails = sewerageThirdPartyServices.getPropertyDetails(assessmentNumber, request);
        if (propertyOwnerDetails != null)
            model.addAttribute("propertyOwnerDetails", propertyOwnerDetails);
        final PropertyTaxDetails propertyTaxDetails = propertyExternalService.getPropertyTaxDetails(assessmentNumber,
                null, null);
        model.addAttribute("propertyTax", propertyTaxDetails.getTotalTaxAmt());
    }

    private String loadViewData(final Model model, final HttpServletRequest request,
                                final SewerageApplicationDetails sewerageApplicationDetails) {
        String inspectionDate = EMPTY;
        String additionalRule = sewerageApplicationDetails.getApplicationType().getCode();
        model.addAttribute("stateType", sewerageApplicationDetails.getClass().getSimpleName());
        for (final SewerageFieldInspection fieldInspection : sewerageApplicationDetails.getFieldInspections())
            Collections.sort(fieldInspection.getFieldInspectionDetails(), new SewerageInspectionDetailsComparatorById());
        if (sewerageApplicationDetails.getCurrentState().getValue().equalsIgnoreCase(WF_STATE_REJECTED))
            if (sewerageTaxUtils.isInspectionFeeCollectionRequired())
                model.addAttribute(PENDING_ACTIONS, WFPA_REJECTED_INSPECTIONFEE_COLLECTION);
            else
                model.addAttribute(PENDING_ACTIONS, WF_STATE_REJECTED);
        model.addAttribute("sewerageApplicationDetails", sewerageApplicationDetails);
        model.addAttribute("applicationHistory",
                sewerageApplicationDetailsService.populateHistory(sewerageApplicationDetails));
        model.addAttribute("approvalDepartmentList", departmentService.getAllDepartments());
        model.addAttribute("pipeSize", getPipeScrewSizes());
        model.addAttribute("roadOwner", OwnerOfTheRoad.values());
        model.addAttribute("uomList", uOMService.findAllOrderByCategory());
        final Map<String, String> modelParams = sewerageApplicationDetailsService
                .showApprovalDetailsByApplcationCurState(sewerageApplicationDetails);
        model.addAttribute("mode", modelParams.get("mode"));
        model.addAttribute(SHOW_APPROVAL_DETAILS, modelParams.get(SHOW_APPROVAL_DETAILS));

        if (EDIT.equalsIgnoreCase(modelParams.get("mode"))) {
            final FeesDetailMaster fdm = feesDetailMasterService.findByCodeAndIsActive(FEES_ESTIMATIONCHARGES_CODE, true);
            final List<SewerageConnectionFee> connectionFeeList = sewerageConnectionFeeService
                    .findAllByApplicationDetailsAndFeesDetail(sewerageApplicationDetails, fdm);
            if (connectionFeeList.isEmpty())
                createSewerageConnectionFee(sewerageApplicationDetails, FEES_ESTIMATIONCHARGES_CODE);
        }
        if (request.getParameter(INSPECTIONDATE) != null)
            inspectionDate = request.getParameter(INSPECTIONDATE);

        if (sewerageApplicationDetails != null && sewerageApplicationDetails.getFieldInspections() != null
                && !sewerageApplicationDetails.getFieldInspections().isEmpty()) {
            final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            sewerageApplicationDetails.getFieldInspections().get(0).setApplicationDetails(sewerageApplicationDetails);
            if (StringUtils.isNotBlank(inspectionDate))
                try {
                    sewerageApplicationDetails.getFieldInspections().get(0).setInspectionDate(format.parse(inspectionDate));
                } catch (final ParseException e) {
                    LOG.error("Exception while parsing date" + e);
                }

        }
        if (sewerageApplicationDetails.getStatus() != null &&
                (APPLICATION_STATUS_INITIALAPPROVED.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode())
                        || APPLICATION_STATUS_INSPECTIONFEEPAID
                        .equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode())))
            populateDonationSewerageTax(sewerageApplicationDetails);

        // After modification if demand reduced, sewerage tax collection shold not be done. Hence directly fwd application from
        // DEE to EE
        if (sewerageApplicationDetails.getStatus() != null &&
                APPLICATION_STATUS_INITIALAPPROVED.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode())) {
            if (!checkAnyTaxIsPendingToCollect(sewerageApplicationDetails)) {
                additionalRule = CHANGEINCLOSETS_NOCOLLECTION;
                model.addAttribute(SHOW_APPROVAL_DETAILS, "yes");
            }
        } else if (sewerageApplicationDetails.getStatus() != null &&
                APPLICATION_STATUS_DEEAPPROVED.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode())
                && !sewerageDemandService
                .checkAnyTaxIsPendingToCollectExcludingAdvance(sewerageApplicationDetails.getCurrentDemand()))
            additionalRule = CHANGEINCLOSETS_NOCOLLECTION;
        model.addAttribute("currentState", sewerageApplicationDetails.getCurrentState().getValue());
        sewerageApplicationDetails.getWorkflowContainer().setAdditionalRule(additionalRule);
        prepareWorkflow(model, sewerageApplicationDetails, sewerageApplicationDetails.getWorkflowContainer());
        if (sewerageApplicationDetails.getConnectionDetail().getPropertyType() != null) {
            final BigDecimal sewerageTaxDue = sewerageApplicationDetailsService.getTotalAmount(sewerageApplicationDetails);
            model.addAttribute("sewerageTaxDue", sewerageTaxDue);
        }
        model.addAttribute("propertyTypes", PropertyType.values());
        return CHANGE_IN_CLOSETS_EDIT;
    }

    private Boolean checkAnyTaxIsPendingToCollect(final SewerageApplicationDetails sewerageApplicationDetails) {
        final SewerageApplicationDetails oldSewerageAppDtls = sewerageApplicationDetailsService
                .findByShscNumberAndIsActive(sewerageApplicationDetails.getConnection().getShscNumber());
        BigDecimal oldDonationCharge = ZERO;
        BigDecimal oldSewerageTax = ZERO;
        BigDecimal oldApplicationAdvanceAmount = ZERO;
        BigDecimal currentDonationCharge = ZERO;
        BigDecimal currentSewerageTax = ZERO;
        BigDecimal currentEstimationCharge = ZERO;

        if (oldSewerageAppDtls != null) {
            for (final SewerageConnectionFee oldSewerageConnectionFee : oldSewerageAppDtls.getConnectionFees())
                if (FEES_SEWERAGETAX_CODE.equalsIgnoreCase(oldSewerageConnectionFee.getFeesDetail().getCode()))
                    oldSewerageTax = oldSewerageTax.add(BigDecimal.valueOf(oldSewerageConnectionFee.getAmount()));
                else if (FEES_DONATIONCHARGE_CODE.equalsIgnoreCase(oldSewerageConnectionFee.getFeesDetail().getCode()))
                    oldDonationCharge = oldDonationCharge.add(BigDecimal.valueOf(oldSewerageConnectionFee.getAmount()));

            if (oldSewerageAppDtls.getCurrentDemand() != null)
                for (final EgDemandDetails dmdDtl : oldSewerageAppDtls.getCurrentDemand().getEgDemandDetails())
                    if (FEES_ADVANCE_CODE.equalsIgnoreCase(dmdDtl.getEgDemandReason().getEgDemandReasonMaster().getCode()))
                        oldApplicationAdvanceAmount = oldApplicationAdvanceAmount.add(dmdDtl.getAmount());
        }

        for (final SewerageConnectionFee scf : sewerageApplicationDetails.getConnectionFees())
            if (FEES_SEWERAGETAX_CODE.equalsIgnoreCase(scf.getFeesDetail().getCode()))
                currentSewerageTax = currentSewerageTax.add(BigDecimal.valueOf(scf.getAmount()));
            else if (FEES_DONATIONCHARGE_CODE.equalsIgnoreCase(scf.getFeesDetail().getCode()))
                currentDonationCharge = currentDonationCharge.add(BigDecimal.valueOf(scf.getAmount()));
            else if (FEES_ESTIMATIONCHARGES_CODE.equalsIgnoreCase(scf.getFeesDetail().getCode()))
                currentEstimationCharge = currentEstimationCharge.add(BigDecimal.valueOf(scf.getAmount()));

        return currentDonationCharge.compareTo(oldDonationCharge) > 0
                || currentSewerageTax.compareTo(oldSewerageTax.add(oldApplicationAdvanceAmount)) > 0
                || currentEstimationCharge.compareTo(ZERO) > 0;
    }

    private void createSewerageConnectionFee(final SewerageApplicationDetails sewerageApplicationDetails, final String feeCode) {
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

    @PostMapping("/modifyConnection-update/{applicationNumber}")
    public String update(@ModelAttribute SewerageApplicationDetails sewerageApplicationDetails,
                         final BindingResult resultBinder, final HttpServletRequest request, final HttpSession session, final Model model,
                         @RequestParam("files") final MultipartFile[] files) {
        String mode = EMPTY;
        WorkflowContainer workflowContainer = sewerageApplicationDetails.getWorkflowContainer();
        String workFlowAction = workflowContainer.getWorkFlowAction();
        if (request.getParameter("mode") != null)
            mode = request.getParameter("mode");

        sewerageApplicationValidator.validateChangeInClosetsUpdateApplication(sewerageApplicationDetails, resultBinder,
                workFlowAction);

        if (resultBinder.hasErrors()) {
            model.addAttribute(SEWERAGE_APPLCATION_DETAILS, sewerageApplicationDetails);
            loadViewData(model, request, sewerageApplicationDetails);
            return CHANGE_IN_CLOSETS_EDIT;
        }
        if ((APPLICATION_STATUS_CREATED.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode())
                || APPLICATION_STATUS_INSPECTIONFEEPAID.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode())
                || APPLICATION_STATUS_FEECOLLECTIONPENDING.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode())
                || APPLICATION_STATUS_INITIALAPPROVED.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode())
                || APPLICATION_STATUS_DEEAPPROVED.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode()))
                && EDIT.equalsIgnoreCase(mode))
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

            } else if (WFLOW_ACTION_STEP_REJECT.equalsIgnoreCase(workFlowAction)) {
                sewerageApplicationDetailsService.getCurrentSession().evict(sewerageApplicationDetails);
                sewerageApplicationDetails = sewerageApplicationDetailsService.findBy(sewerageApplicationDetails
                        .getId());
                sewerageApplicationDetails.setWorkflowContainer(workflowContainer);
            }

        if ((sewerageApplicationDetails.getStatus().getCode() != null
                && APPLICATION_STATUS_INITIALAPPROVED.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode())
                || APPLICATION_STATUS_DEEAPPROVED.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode())
                || APPLICATION_STATUS_INSPECTIONFEEPAID.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode()))
                && !WFLOW_ACTION_STEP_REJECT.equalsIgnoreCase(workFlowAction))
            populateDonationSewerageTax(sewerageApplicationDetails);

        Long approvalPosition = workflowContainer.getApproverPositionId();
        if (!resultBinder.hasErrors()) {
            try {
                if (isNotBlank(workFlowAction)) {
                    if (PREVIEWWORKFLOWACTION.equalsIgnoreCase(workFlowAction)
                            && NEWSEWERAGECONNECTION.equals(sewerageApplicationDetails.getApplicationType().getCode()))
                        return "redirect:/transactions/workorder?pathVar="
                                + sewerageApplicationDetails.getApplicationNumber();

                    if (WF_STATE_CONNECTION_EXECUTION_BUTTON.equalsIgnoreCase(workFlowAction)) {
                        final SewerageApplicationDetails parentSewerageAppDtls = sewerageApplicationDetailsService
                                .findByShscNumberAndIsActive(sewerageApplicationDetails.getConnection().getShscNumber());
                        if (parentSewerageAppDtls != null) {
                            parentSewerageAppDtls.setActive(false);
                            sewerageApplicationDetails.setParent(parentSewerageAppDtls);
                        }
                    }
                }
                sewerageApplicationDetailsService.updateSewerageApplicationDetails(sewerageApplicationDetails, mode, request, session);
            } catch (final ValidationException e) {
                throw new ValidationException(e);
            }
            if (isNotBlank(workFlowAction)) {
                if (WF_ESTIMATION_NOTICE_BUTTON.equalsIgnoreCase(workFlowAction))
                    return "redirect:/transactions/estimationnotice?pathVar="
                            + sewerageApplicationDetails.getApplicationNumber();
                if (WF_CLOSERACKNOWLDGEENT_BUTTON.equalsIgnoreCase(workFlowAction))
                    return "redirect:/applications/acknowlgementNotice?pathVar="
                            + sewerageApplicationDetails.getApplicationNumber();
                if (WFLOW_ACTION_STEP_CANCEL.equalsIgnoreCase(workFlowAction))
                    return "redirect:/transactions/rejectionnotice?pathVar="
                            + sewerageApplicationDetails.getApplicationNumber() + "&" + "approvalComent="
                            + workflowContainer.getApproverComments();
            }
            final Assignment currentUserAssignment = assignmentService.getPrimaryAssignmentForGivenRange(securityUtils
                    .getCurrentUser().getId(), new Date(), new Date());
            String nextDesign;
            Assignment assignObj = null;
            List<Assignment> asignList = new ArrayList<>();

            if (approvalPosition == null || approvalPosition == 0) {
                Assignment workflowInitiatorAssignment = sewerageWorkflowService.getWorkFlowInitiator(sewerageApplicationDetails);
                if (workflowInitiatorAssignment != null)
                    approvalPosition = (workflowInitiatorAssignment).getPosition()
                            .getId();
            }

            if (approvalPosition != null)
                assignObj = assignmentService.getPrimaryAssignmentForPositon(approvalPosition);
            if (assignObj != null) {
                asignList.add(assignObj);
            } else if (assignObj == null && approvalPosition != null)
                asignList = assignmentService.getAssignmentsForPosition(approvalPosition, new Date());

            nextDesign = asignList.isEmpty() ? EMPTY : asignList.get(0).getDesignation().getName();

            final String pathVars = sewerageApplicationDetails.getApplicationNumber() + ","
                    + sewerageTaxUtils.getApproverName(approvalPosition) + ","
                    + (currentUserAssignment == null ? EMPTY : currentUserAssignment.getDesignation().getName()) + ","
                    + (nextDesign == null ? EMPTY : nextDesign);
            return "redirect:/transactions/changeInClosets-success?pathVars=" + pathVars;
        } else
            return loadViewData(model, request, sewerageApplicationDetails);
    }

    private void populateDonationSewerageTax(final SewerageApplicationDetails sewerageApplicationDetails) {

        final FeesDetailMaster donationCharge = feesDetailMasterService.findByCodeAndIsActive(FEES_DONATIONCHARGE_CODE, true);
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

        final FeesDetailMaster sewerageTax = feesDetailMasterService.findByCodeAndIsActive(FEES_SEWERAGETAX_CODE, true);
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
                if (validSewerageConnectioEstimationDetail(estimationDetail)) {
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
        return sewerageFieldInspectionDetails != null
                && sewerageFieldInspectionDetails.getNoOfPipes() != null && sewerageFieldInspectionDetails.getNoOfPipes() != 0;
    }

    private boolean validSewerageConnectioEstimationDetail(
            final SewerageConnectionEstimationDetails sewerageConnectionEstimationDetails) {
        return sewerageConnectionEstimationDetails != null && sewerageConnectionEstimationDetails.getItemDescription() != null;
    }
}