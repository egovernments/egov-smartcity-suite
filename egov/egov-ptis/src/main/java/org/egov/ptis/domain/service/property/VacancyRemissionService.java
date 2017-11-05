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
package org.egov.ptis.domain.service.property;

import static java.lang.Boolean.FALSE;
import static org.egov.ptis.constants.PropertyTaxConstants.ADDITIONAL_COMMISSIONER_DESIGN;
import static org.egov.ptis.constants.PropertyTaxConstants.ANONYMOUS_USER;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_BAL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ASSISTANT_COMMISSIONER_DESIGN;
import static org.egov.ptis.constants.PropertyTaxConstants.CITY_GRADE_CORPORATION;
import static org.egov.ptis.constants.PropertyTaxConstants.COMMISSIONER_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.CURRENTYEAR_FIRST_HALF;
import static org.egov.ptis.constants.PropertyTaxConstants.CURRENTYEAR_SECOND_HALF;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_BAL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_SECONDHALF_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_STR_EDUCATIONAL_CESS;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_STR_GENERAL_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_STR_LIBRARY_CESS;
import static org.egov.ptis.constants.PropertyTaxConstants.DEPUTY_COMMISSIONER_DESIGN;
import static org.egov.ptis.constants.PropertyTaxConstants.DIGITAL_SIGNATURE_PENDING;
import static org.egov.ptis.constants.PropertyTaxConstants.JUNIOR_ASSISTANT;
import static org.egov.ptis.constants.PropertyTaxConstants.NATURE_VACANCY_REMISSION;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTYTAX_ROLEFORNONEMPLOYEE;
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;
import static org.egov.ptis.constants.PropertyTaxConstants.SENIOR_ASSISTANT;
import static org.egov.ptis.constants.PropertyTaxConstants.SOURCE_ONLINE;
import static org.egov.ptis.constants.PropertyTaxConstants.VR_APP_STATUS_REJECTED;
import static org.egov.ptis.constants.PropertyTaxConstants.VR_SPECIALNOTICE_TEMPLATE;
import static org.egov.ptis.constants.PropertyTaxConstants.VR_STATUS_APPROVED;
import static org.egov.ptis.constants.PropertyTaxConstants.VR_STATUS_COMMISSIONER_FORWARD_PENDING;
import static org.egov.ptis.constants.PropertyTaxConstants.VR_STATUS_MONTHLY_UPDATE;
import static org.egov.ptis.constants.PropertyTaxConstants.VR_STATUS_NOTICE_GENERATED;
import static org.egov.ptis.constants.PropertyTaxConstants.VR_STATUS_REJECTED;
import static org.egov.ptis.constants.PropertyTaxConstants.VR_STATUS_REJECTION_ACK_GENERATED;
import static org.egov.ptis.constants.PropertyTaxConstants.VR_STATUS_WORKFLOW;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_APPROVE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_FORWARD;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_NOTICE_GENERATE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_REJECT;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_COMMISSIONER_APPROVAL_PENDING;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_REJECTED;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_UD_REVENUE_INSPECTOR_APPROVAL_PENDING;
import static org.egov.ptis.constants.PropertyTaxConstants.ZONAL_COMMISSIONER_DESIGN;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.egov.commons.CFinancialYear;
import org.egov.commons.Installment;
import org.egov.demand.model.EgDemandDetails;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.messaging.MessagingService;
import org.egov.infra.persistence.entity.Address;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.ApplicationNumberGenerator;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.pims.commons.Position;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.enums.TransactionType;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.BasicPropertyImpl;
import org.egov.ptis.domain.entity.property.DocumentType;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyID;
import org.egov.ptis.domain.entity.property.PropertyOwnerInfo;
import org.egov.ptis.domain.entity.property.VacancyRemission;
import org.egov.ptis.domain.entity.property.VacancyRemissionApproval;
import org.egov.ptis.domain.entity.property.VacancyRemissionDetails;
import org.egov.ptis.domain.repository.vacancyremission.VacancyRemissionApprovalRepository;
import org.egov.ptis.domain.repository.vacancyremission.VacancyRemissionRepository;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

@Service
@Transactional(readOnly = true)
public class VacancyRemissionService {

    private static final Logger LOG = LoggerFactory.getLogger(VacancyRemissionService.class);

    @Autowired
    private VacancyRemissionRepository vacancyRemissionRepository;

    @Autowired
    private VacancyRemissionApprovalRepository vacancyRemissionApprovalRepository;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<VacancyRemission> vacancyRemissionWorkflowService;

    @Autowired
    private PropertyTaxUtil propertyTaxUtil;

    @Autowired
    private PtDemandDao ptDemandDAO;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private ApplicationNumberGenerator applicationNumberGenerator;

    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource ptisMessageSource;

    @Autowired
    private MessagingService messagingService;

    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;

    @Autowired
    private ReportService reportService;

    @Autowired
    private UserService userService;

    @Autowired
    private AppConfigValueService appConfigValuesService;

    public static final String REJECTION_ACK_TEMPLATE = "vacancyRemission_rejectionAck";

    public VacancyRemission getApprovedVacancyRemissionForProperty(final String upicNo) {
        return vacancyRemissionRepository.findByUpicNo(upicNo);
    }

    public VacancyRemission getLatestRejectAckGeneratedVacancyRemissionForProperty(final String upicNo) {
        VacancyRemission vacancyRemission = null;
        final List<VacancyRemission> rejectedRemissionList = vacancyRemissionRepository
                .findAllRejectionAckGeneratedForUpicNo(upicNo);
        if (!rejectedRemissionList.isEmpty())
            vacancyRemission = rejectedRemissionList.get(0);
        return vacancyRemission;
    }

    public VacancyRemission getLatestSpecialNoticeGeneratedVacancyRemissionForProperty(final String upicNo) {
        VacancyRemission vacancyRemission = null;
        final List<VacancyRemission> approvedRemissionList = vacancyRemissionRepository
                .findAllSpecialNoticesGeneratedForUpicNo(upicNo);
        if (!approvedRemissionList.isEmpty())
            vacancyRemission = approvedRemissionList.get(0);
        return vacancyRemission;
    }

    public VacancyRemission getVacancyRemissionById(final Long id) {
        return vacancyRemissionRepository.findOne(id);
    }

    public List<VacancyRemission> getAllVacancyRemissionByUpicNo(final String upicNo) {
        return vacancyRemissionRepository.getAllVacancyRemissionByUpicNo(upicNo);
    }

    public VacancyRemission getRejectedVacancyRemissionForProperty(final String upicNo) {
        return vacancyRemissionRepository.findRejectedByUpicNo(upicNo);
    }

    @Transactional
    public VacancyRemission saveVacancyRemission(final VacancyRemission vacancyRemission, Long approvalPosition,
            final String approvalComent, final String additionalRule, final String workFlowAction,
            final Boolean propertyByEmployee) {
        if (LOG.isDebugEnabled())
            LOG.debug(" Create WorkFlow Transition Started  ...");
        final User user = securityUtils.getCurrentUser();
        final DateTime currentDate = new DateTime();
        Position pos = null;
        Assignment wfInitiator = null;
        String currentState;
        Assignment assignment;
        String approverDesignation = "";
        String nextAction = "";
        String loggedInUserDesignation;
        loggedInUserDesignation = getLoggedInUserDesignation(vacancyRemission, user);
        if(SOURCE_ONLINE.equalsIgnoreCase(vacancyRemission.getSource()) && ApplicationThreadLocals.getUserId() == null) 
            ApplicationThreadLocals.setUserId(securityUtils.getCurrentUser().getId());
        if (!propertyByEmployee || ANONYMOUS_USER.equalsIgnoreCase(user.getName())) {
            currentState = "Created";
            if (propertyService.isCscOperator(user)) {
                assignment = propertyService.getMappedAssignmentForCscOperator(vacancyRemission.getBasicProperty());
                wfInitiator = assignment;
            } else {
                assignment = propertyService.getUserPositionByZone(vacancyRemission.getBasicProperty(), false);
                wfInitiator = assignment;
            }
            if (null != assignment)
                approvalPosition = assignment.getPosition().getId();
        } else {
            currentState = null;
            if (null != approvalPosition && approvalPosition != 0) {
                assignment = assignmentService.getAssignmentsForPosition(approvalPosition, new Date())
                        .get(0);
                assignment.getEmployee().getName().concat("~")
                        .concat(assignment.getPosition().getName());
                approverDesignation = assignment.getDesignation().getName();
            }
        }

        if (WFLOW_ACTION_STEP_FORWARD.equalsIgnoreCase(workFlowAction) && vacancyRemission.getId() == null
                && (approverDesignation.equalsIgnoreCase(ASSISTANT_COMMISSIONER_DESIGN) ||
                        approverDesignation.equalsIgnoreCase(DEPUTY_COMMISSIONER_DESIGN)
                        || approverDesignation.equalsIgnoreCase(ADDITIONAL_COMMISSIONER_DESIGN)
                        || approverDesignation.equalsIgnoreCase(ZONAL_COMMISSIONER_DESIGN) ||
                        approverDesignation.equalsIgnoreCase(COMMISSIONER_DESGN))) {

            final String designation = approverDesignation.split(" ")[0];
            nextAction = getWorkflowNextAction(designation);
        }

        if (vacancyRemission.getId() != null && (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_REJECT)
                || workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_NOTICE_GENERATE)))
            wfInitiator = getWorkflowInitiator(vacancyRemission);
        else if (wfInitiator == null)
            wfInitiator = propertyTaxCommonUtils.getWorkflowInitiatorAssignment(user.getId());

        if (StringUtils.isBlank(vacancyRemission.getApplicationNumber()))
            vacancyRemission.setApplicationNumber(applicationNumberGenerator.generate());
        if (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_NOTICE_GENERATE)) {
            if (wfInitiator.getPosition().equals(vacancyRemission.getState().getOwnerPosition())) {
                vacancyRemission.setStatus(VR_STATUS_REJECTION_ACK_GENERATED);
                vacancyRemission.transition().end().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withDateInfo(currentDate.toDate());
                vacancyRemission.getBasicProperty().setUnderWorkflow(false);
            }
        } else if (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_REJECT)) {
            final String stateValue = WF_STATE_REJECTED;
            vacancyRemission.setStatus(VR_STATUS_REJECTED);
            vacancyRemission.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                    .withComments(approvalComent).withStateValue(stateValue).withDateInfo(currentDate.toDate())
                    .withOwner(wfInitiator.getPosition()).withNextAction("Application Rejected");
            buildSMS(vacancyRemission, workFlowAction);
        } else {
            if (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_FORWARD))
                vacancyRemission.setStatus(VR_STATUS_WORKFLOW);
            else if (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_APPROVE))
                vacancyRemission.setStatus(VR_STATUS_APPROVED);

            if (null != approvalPosition && approvalPosition != -1 && !approvalPosition.equals(Long.valueOf(0)))
                pos = positionMasterService.getPositionById(approvalPosition);
            WorkFlowMatrix wfmatrix = null;
            if (null == vacancyRemission.getState()) {
                wfmatrix = vacancyRemissionWorkflowService.getWfMatrix(vacancyRemission.getStateType(), null, null,
                        additionalRule, currentState, null);
                vacancyRemission.transition().start().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(wfmatrix.getNextState()).withDateInfo(new Date())
                        .withOwner(pos).withNextAction(nextAction).withNatureOfTask(NATURE_VACANCY_REMISSION)
                        .withInitiator(wfInitiator != null ? wfInitiator.getPosition() : null);
                vacancyRemission.getBasicProperty().setUnderWorkflow(true);
            } else {
                wfmatrix = vacancyRemissionWorkflowService.getWfMatrix(vacancyRemission.getStateType(), null, null,
                        additionalRule, vacancyRemission.getCurrentState().getValue(),
                        vacancyRemission.getCurrentState().getNextAction(), null, loggedInUserDesignation);

                if (wfmatrix != null)
                    if ("END".equalsIgnoreCase(wfmatrix.getNextAction())) {
                        vacancyRemission.transition().progressWithStateCopy()
                                .withSenderName(user.getUsername() + "::" + user.getName())
                                .withComments(approvalComent).withStateValue(wfmatrix.getNextState())
                                .withDateInfo(currentDate.toDate()).withNextAction(VR_STATUS_MONTHLY_UPDATE);

                    } else
                        vacancyRemission.transition().progressWithStateCopy()
                                .withSenderName(user.getUsername() + "::" + user.getName())
                                .withComments(approvalComent).withStateValue(wfmatrix.getNextState())
                                .withDateInfo(currentDate.toDate()).withOwner(pos)
                                .withNextAction(wfmatrix.getNextAction());
                if (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_APPROVE))
                    buildSMS(vacancyRemission, workFlowAction);
            }
        }
        if (LOG.isDebugEnabled())
            LOG.debug(" WorkFlow Transition Completed  ...");
        return vacancyRemissionRepository.save(vacancyRemission);
    }

    /**
     * Provides 50% rebate on the next installment taxes
     * @param vacancyRemission
     * @param demandInstallment
     * @param effectiveInstallment
     */
    private void updateDemandDetailsWithRebate(final VacancyRemission vacancyRemission, final Installment demandInstallment,
            final Installment effectiveInstallment) {
        final Set<Ptdemand> activePropPtDemandSet = vacancyRemission.getBasicProperty().getActiveProperty().getPtDemandSet();
        Ptdemand currPtDemand = null;
        for (final Ptdemand ptDemand : activePropPtDemandSet)
            if (ptDemand.getIsHistory().equalsIgnoreCase("N"))
                if (ptDemand.getEgInstallmentMaster().equals(demandInstallment)) {
                    currPtDemand = ptDemand;
                    break;
                }
        for (final EgDemandDetails dmdDet : currPtDemand.getEgDemandDetails())
            if (dmdDet.getInstallmentStartDate().equals(effectiveInstallment.getFromDate()))
                dmdDet.setAmount(dmdDet.getAmount().divide(new BigDecimal("2")).setScale(0,
                        BigDecimal.ROUND_HALF_UP));
        ptDemandDAO.update(currPtDemand);
    }

    public void addModelAttributes(final Model model, final BasicProperty basicProperty) {
        final Property property = basicProperty.getActiveProperty();
        model.addAttribute("property", property);
        final Ptdemand ptDemand = ptDemandDAO.getNonHistoryCurrDmdForProperty(property);
        if (ptDemand != null && ptDemand.getDmdCalculations() != null && ptDemand.getDmdCalculations().getAlv() != null)
            model.addAttribute("ARV", ptDemand.getDmdCalculations().getAlv());
        else
            model.addAttribute("ARV", BigDecimal.ZERO);
        if (!basicProperty.getActiveProperty().getIsExemptedFromTax()) {
            try {
                // Based on the current installment, fetch tax details for the respective installment
                final Map<String, Map<String, BigDecimal>> demandCollMap = propertyTaxUtil.prepareDemandDetForView(property,
                        propertyTaxCommonUtils.getCurrentInstallment());
                final Map<String, BigDecimal> currentTaxDetails = propertyService.getCurrentTaxDetails(demandCollMap, new Date());
                model.addAttribute("propertyTax", currentTaxDetails.get(DEMANDRSN_STR_GENERAL_TAX));
                model.addAttribute("eduCess", currentTaxDetails.get(DEMANDRSN_STR_EDUCATIONAL_CESS) == null ? BigDecimal.ZERO
                        : currentTaxDetails.get(DEMANDRSN_STR_EDUCATIONAL_CESS));
                model.addAttribute("libraryCess", currentTaxDetails.get(DEMANDRSN_STR_LIBRARY_CESS) == null ? BigDecimal.ZERO
                        : currentTaxDetails.get(DEMANDRSN_STR_LIBRARY_CESS));
                model.addAttribute("currTax", currentTaxDetails.get(CURR_DMD_STR));
                model.addAttribute("currTaxDue", currentTaxDetails.get(CURR_BAL_STR));
                model.addAttribute("totalTax", currentTaxDetails.get(CURR_DMD_STR));
                model.addAttribute("totalArrDue", currentTaxDetails.get(ARR_BAL_STR));
            } catch (final Exception e) {
                LOG.error("Exception in addModelAttributes : ", e);
                throw new ApplicationRuntimeException("Exception in addModelAttributes : " + e);
            }
            model.addAttribute("propertyByEmployee", checkIfEmployee(getLoggedInUser()));
        }
    }

    public Boolean checkIfEmployee(final User user) {
        return propertyService.isEmployee(user) && !ANONYMOUS_USER.equalsIgnoreCase(user.getName());
    }

    public String getInitiatorName(final VacancyRemission vacancyRemission) {
        String initiatorName;
        Assignment assignment = new Assignment();
        if (checkIfEmployee(vacancyRemission.getCreatedBy()))
            assignment = assignmentService.getPrimaryAssignmentForUser(vacancyRemission.getCreatedBy().getId());
        else
            assignment = assignmentService
                    .getPrimaryAssignmentForPositon(
                            vacancyRemission.getStateHistory().get(0).getOwnerPosition().getId());
        initiatorName = assignment.getEmployee().getName().concat("~").concat(assignment.getPosition().getName());
        return initiatorName;
    }

    public Assignment getWorkflowInitiator(final VacancyRemission vacancyRemission) {
        Assignment wfInitiator;
        if (checkIfEmployee(vacancyRemission.getCreatedBy())) {
            if (vacancyRemission.getState() != null && vacancyRemission.getState().getInitiatorPosition() != null)
                wfInitiator = propertyTaxCommonUtils.getUserAssignmentByPassingPositionAndUser(
                        vacancyRemission.getCreatedBy(), vacancyRemission.getState().getInitiatorPosition());
            else
                wfInitiator = assignmentService.getPrimaryAssignmentForUser(vacancyRemission.getCreatedBy().getId());
        } else if (!vacancyRemission.getStateHistory().isEmpty())
            wfInitiator = assignmentService.getPrimaryAssignmentForPositon(
                    vacancyRemission.getStateHistory().get(0).getOwnerPosition().getId());
        else
            wfInitiator = assignmentService
                    .getPrimaryAssignmentForPositon(vacancyRemission.getState().getOwnerPosition().getId());

        return wfInitiator;
    }

    @Transactional
    public void saveRemissionDetails(final VacancyRemission vacancyRemission) {
        vacancyRemissionRepository.save(vacancyRemission);
    }

    @Transactional
    public void rejectVacancyRemission(final VacancyRemission vacancyRemission, final String commnets,
            final HttpServletRequest request) {
        final User user = securityUtils.getCurrentUser();
        Position wfInitiator;
        if (vacancyRemission != null) {
            wfInitiator = vacancyRemission.getState().getInitiatorPosition();

            vacancyRemission.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                    .withComments(commnets).withStateValue(VR_APP_STATUS_REJECTED).withDateInfo(new Date())
                    .withOwner(wfInitiator).withNextAction("Generate Notice");
            vacancyRemission.setStatus(VR_STATUS_REJECTED);
            buildSMS(vacancyRemission, "Reject");
        }
        vacancyRemissionRepository.save(vacancyRemission);
    }

    public List<VacancyRemissionDetails> getMonthlyDetailsHistory(final VacancyRemission vacancyRemission) {
        List<VacancyRemissionDetails> historyList = new ArrayList<>();
        if (!vacancyRemission.getVacancyRemissionDetails().isEmpty()) {
            historyList = vacancyRemission.getVacancyRemissionDetails();
            Collections.reverse(historyList);
        }
        return historyList;
    }

    public VacancyRemissionApproval getVacancyRemissionApprovalById(final Long id) {
        return vacancyRemissionApprovalRepository.findOne(id);
    }

    @Transactional
    public void saveVacancyRemissionApproval(final VacancyRemissionApproval vacancyRemissionApproval,
            final Long approvalPosition, final String approvalComent, final String additionalRule,
            final String workFlowAction) {
        if (LOG.isDebugEnabled())
            LOG.debug(" Create WorkFlow Transition Started  ...");
        final User user = securityUtils.getCurrentUser();
        final DateTime currentDate = new DateTime();
        Position pos = null;
        Assignment wfInitiator;
        Assignment assignment;
        String approverDesignation = "";
        String nextAction = "";
        List<Assignment> loggedInUserAssign;
        String loggedInUserDesignation = "";

        wfInitiator = getInitiatorOnWFAction(vacancyRemissionApproval, workFlowAction);
        if (wfInitiator == null)
            wfInitiator = getWorkflowInitiatorAssignment(user.getId());

        if (null != approvalPosition && approvalPosition != 0) {
            assignment = assignmentService.getAssignmentsForPosition(approvalPosition, new Date())
                    .get(0);
            approverDesignation = assignment.getDesignation().getName();
        }

        if (vacancyRemissionApproval.getState() != null) {
            loggedInUserAssign = assignmentService.getAssignmentByPositionAndUserAsOnDate(
                    vacancyRemissionApproval.getCurrentState().getOwnerPosition().getId(), user.getId(), new Date());
            loggedInUserDesignation = !loggedInUserAssign.isEmpty() ? loggedInUserAssign.get(0).getDesignation().getName() : null;
        }

        if (WFLOW_ACTION_STEP_FORWARD.equalsIgnoreCase(workFlowAction)
                && (approverDesignation.equalsIgnoreCase(ASSISTANT_COMMISSIONER_DESIGN) ||
                        approverDesignation.equalsIgnoreCase(DEPUTY_COMMISSIONER_DESIGN)
                        || approverDesignation.equalsIgnoreCase(ADDITIONAL_COMMISSIONER_DESIGN)
                        || approverDesignation.equalsIgnoreCase(ZONAL_COMMISSIONER_DESIGN) ||
                        approverDesignation.equalsIgnoreCase(COMMISSIONER_DESGN))){
            if (vacancyRemissionApproval.getStatus().equals(VR_STATUS_APPROVED))
                nextAction = DIGITAL_SIGNATURE_PENDING;
            else {
                final String designation = approverDesignation.split(" ")[0];
                nextAction = getApprovalAsNextAction(designation);
            }
        }

        if (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_NOTICE_GENERATE)) {
            if (VR_STATUS_APPROVED.equalsIgnoreCase(vacancyRemissionApproval.getStatus())) {
                vacancyRemissionApproval.setStatus(VR_STATUS_NOTICE_GENERATED);
                vacancyRemissionApproval.transition().end().withSenderName(user.getName()).withComments(approvalComent)
                        .withDateInfo(currentDate.toDate());

            } else {
                vacancyRemissionApproval.setStatus(VR_STATUS_REJECTION_ACK_GENERATED);
                vacancyRemissionApproval.transition().end().withSenderName(user.getName()).withComments(approvalComent)
                        .withDateInfo(currentDate.toDate());
            }

        } else if (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_REJECT)) {
            if (wfInitiator != null)
                if (wfInitiator.getPosition().equals(vacancyRemissionApproval.getCurrentState().getOwnerPosition())) {
                    vacancyRemissionApproval.transition().end().withSenderName(user.getUsername() + "::" + user.getName())
                            .withComments(approvalComent).withDateInfo(currentDate.toDate());
                    vacancyRemissionApproval.setStatus(VR_STATUS_REJECTED);
                    vacancyRemissionApproval.getVacancyRemission().getBasicProperty().setUnderWorkflow(FALSE);
                } else {
                    vacancyRemissionApproval.transition().progress().withSenderName(user.getName()).withComments(approvalComent)
                            .withStateValue(WF_STATE_REJECTED).withDateInfo(currentDate.toDate())
                            .withOwner(wfInitiator.getPosition())
                            .withNextAction(WF_STATE_UD_REVENUE_INSPECTOR_APPROVAL_PENDING);
                    vacancyRemissionApproval.setStatus(VR_STATUS_REJECTED);
                }
        } else {
            if (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_FORWARD) && vacancyRemissionApproval.getStatus() == null)
                vacancyRemissionApproval.setStatus(VR_STATUS_WORKFLOW);
            else if (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_APPROVE)) {
                vacancyRemissionApproval.setStatus(VR_STATUS_APPROVED);
                vacancyRemissionApproval.setIsApproved(true);
            }
            if (WFLOW_ACTION_STEP_APPROVE.equalsIgnoreCase(workFlowAction))
                pos = vacancyRemissionApproval.getCurrentState().getOwnerPosition();
            else if (null != approvalPosition && approvalPosition != -1 && !approvalPosition.equals(Long.valueOf(0)))
                pos = positionMasterService.getPositionById(approvalPosition);

            WorkFlowMatrix wfmatrix;
            if (null == vacancyRemissionApproval.getState()) {
                wfmatrix = vacancyRemissionWorkflowService.getWfMatrix(vacancyRemissionApproval.getStateType(), null,
                        null, additionalRule, null, null);
                vacancyRemissionApproval.transition().start().withSenderName(user.getName())
                        .withComments(approvalComent).withStateValue(wfmatrix.getNextState()).withDateInfo(new Date())
                        .withOwner(pos).withNextAction(wfmatrix.getNextAction()).withNatureOfTask(NATURE_VACANCY_REMISSION)
                        .withInitiator(wfInitiator != null ? wfInitiator.getPosition() : null);
            } else if ("END".equalsIgnoreCase(vacancyRemissionApproval.getCurrentState().getNextAction()))
                vacancyRemissionApproval.transition().end().withSenderName(user.getName())
                        .withComments(approvalComent).withDateInfo(currentDate.toDate());
            else {
                wfmatrix = vacancyRemissionWorkflowService.getWfMatrix(vacancyRemissionApproval.getStateType(), null,
                        null, additionalRule, vacancyRemissionApproval.getCurrentState().getValue(),
                        vacancyRemissionApproval.getCurrentState().getNextAction(), null, loggedInUserDesignation);
                vacancyRemissionApproval.transition().progress().withSenderName(user.getName())
                        .withComments(approvalComent).withStateValue(wfmatrix.getNextState())
                        .withDateInfo(currentDate.toDate()).withOwner(pos)
                        .withNextAction(StringUtils.isNotBlank(nextAction) ? nextAction : wfmatrix.getNextAction());

                if (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_APPROVE))
                    wFApprove(vacancyRemissionApproval);

            }
        }
        if (LOG.isDebugEnabled())
            LOG.debug(" WorkFlow Transition Completed  ...");
        vacancyRemissionApprovalRepository.save(vacancyRemissionApproval);

    }

    private void wFApprove(final VacancyRemissionApproval vacancyRemissionApproval) {
        final Map<String, Installment> installmentMap = propertyTaxUtil.getInstallmentsForCurrYear(new Date());
        final Installment installmentFirstHalf = installmentMap.get(CURRENTYEAR_FIRST_HALF);
        final Installment installmentSecondHalf = installmentMap.get(CURRENTYEAR_SECOND_HALF);
        /*
         * If VR is done in 1st half, provide 50% rebate on taxes of the 2nd half
         */
        if (DateUtils.between(vacancyRemissionApproval.getVacancyRemission().getVacancyToDate(),
                installmentFirstHalf.getFromDate(), installmentFirstHalf.getToDate()))
            updateDemandDetailsWithRebate(vacancyRemissionApproval.getVacancyRemission(), installmentFirstHalf,
                    installmentSecondHalf);
    }

    public User getLoggedInUser() {
        return securityUtils.getCurrentUser();
    }

    public VacancyRemission saveVacancyRemission(final VacancyRemission vacancyRemission, final Long approvalPosition,
            final String approvalComent, final String additionalRule, final String workFlowAction,
            final Boolean propertyByEmployee, final HashMap<String, String> meesevaParams) {
        return saveVacancyRemission(vacancyRemission, approvalPosition, approvalComent, additionalRule, workFlowAction,
                propertyByEmployee);

    }

    public void buildSMS(final VacancyRemission vacancyRemission, final String workFlowAction) {
        for (final PropertyOwnerInfo ownerInfo : vacancyRemission.getBasicProperty().getPropertyOwnerInfo())
            if (StringUtils.isNotBlank(ownerInfo.getOwner().getMobileNumber()))
                buildSms(vacancyRemission, ownerInfo.getOwner(), workFlowAction);
    }

    private void buildSms(final VacancyRemission vacancyRemission, final User user, final String workFlowAction) {
        final String assessmentNo = vacancyRemission.getBasicProperty().getUpicNo();
        final String mobileNumber = user.getMobileNumber();
        final String applicantName = user.getName();
        String smsMsg = "";
        if (workFlowAction.equals(WFLOW_ACTION_STEP_FORWARD)) {
            // to be enabled once acknowledgement feature is developed
            /*
             * smsMsg = messageSource.getMessage("vacancyremission.ack.sms", new String[] { applicantName, assessmentNo }, null);
             */
        } else if (workFlowAction.equals(WFLOW_ACTION_STEP_REJECT))
            smsMsg = ptisMessageSource.getMessage("vacancyremission.rejection.sms", new String[] { applicantName, assessmentNo,
                    ApplicationThreadLocals.getMunicipalityName() }, null);
        else if (workFlowAction.equals(WFLOW_ACTION_STEP_APPROVE))
            smsMsg = ptisMessageSource.getMessage("vacancyremission.approval.sms", new String[] { applicantName, assessmentNo,
                    ApplicationThreadLocals.getMunicipalityName() }, null);

        if (StringUtils.isNotBlank(mobileNumber))
            messagingService.sendSMS(mobileNumber, smsMsg);

    }

    public List<DocumentType> getDocuments(final TransactionType transactionType) {
        return propertyService.getDocumentTypesForTransactionType(transactionType);
    }

    public DocumentType getDocType(final String docname) {
        return vacancyRemissionRepository.findDocumentTypeByNameAndTransactionType(docname, TransactionType.VACANCYREMISSION);

    }

    public ReportOutput generateReport(final VacancyRemission vacancyRemission, final HttpServletRequest request,
            final String approvedUser, final String noticeNo) {
        ReportRequest reportInput;
        ReportOutput reportOutput = null;
        CFinancialYear financialYear;
        if (vacancyRemission != null) {
            final Map<String, Object> reportParams = new HashMap<>();
            final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            final String cityName = request.getSession().getAttribute("citymunicipalityname").toString();
            final String cityGrade = request.getSession().getAttribute("cityGrade") != null
                    ? request.getSession().getAttribute("cityGrade").toString() : null;
            Boolean isCorporation;
            if (StringUtils.isNoneBlank(cityGrade)
                    && CITY_GRADE_CORPORATION.equalsIgnoreCase(cityGrade))
                isCorporation = true;
            else
                isCorporation = false;
            final BasicPropertyImpl basicProperty = vacancyRemission.getBasicProperty();
            final Address ownerAddress = basicProperty.getAddress();
            final PropertyID propertyId = basicProperty.getPropertyID();
            reportParams.put("isCorporation", isCorporation);
            reportParams.put("cityName", cityName);
            reportParams.put("userSignature", securityUtils.getCurrentUser().getSignature() != null
                    ? new ByteArrayInputStream(securityUtils.getCurrentUser().getSignature()) : "");
            reportParams.put("loggedInUsername",
                    userService.getUserById(ApplicationThreadLocals.getUserId()).getName());
            reportParams.put("approvedDate", formatter.format(vacancyRemission.getState().getCreatedDate()));
            reportParams.put("approverName", approvedUser);
            reportParams.put("applicationDate", formatter.format(vacancyRemission.getCreatedDate()));
            reportParams.put("currentDate", formatter.format(new Date()));
            reportParams.put("noticeNo", noticeNo);
            reportParams.put("ownerName", basicProperty.getFullOwnerName());
            reportParams.put("houseNo", ownerAddress.getHouseNoBldgApt());
            reportParams.put("assessmentNo", basicProperty.getUpicNo());
            reportParams.put("locality", propertyId.getLocality().getName());
            reportParams.put("vrFromDate", formatter.format(vacancyRemission.getVacancyFromDate()));
            reportParams.put("vrToDate", formatter.format(vacancyRemission.getVacancyToDate()));
            final int noOfMonths = DateUtils.noOfMonthsBetween(vacancyRemission.getVacancyFromDate(),
                    vacancyRemission.getVacancyToDate());
            reportParams.put("totalMonths", noOfMonths);
            final Map<String, BigDecimal> currentDemand = ptDemandDAO
                    .getDemandCollMap(vacancyRemission.getBasicProperty().getProperty());
            final BigDecimal halfYearTax = currentDemand.get(CURR_SECONDHALF_DMD_STR);
            financialYear = propertyTaxUtil.getFinancialYearforDate(new Date());
            reportParams.put("financialYear", financialYear.getFinYearRange());
            reportParams.put("halfYearTax", halfYearTax.toString());
            reportParams.put("newTax", halfYearTax.divide(BigDecimal.valueOf(2)).setScale(2).toString());
            reportInput = new ReportRequest(VR_SPECIALNOTICE_TEMPLATE, vacancyRemission, reportParams);
            reportOutput = reportService.createReport(reportInput);
        }
        return reportOutput;
    }

    public String getLoggedInUserDesignation(final Long posId, final User user) {
        final List<Assignment> loggedInUserAssign = assignmentService.getAssignmentByPositionAndUserAsOnDate(
                posId, user.getId(), new Date());
        return !loggedInUserAssign.isEmpty() ? loggedInUserAssign.get(0).getDesignation().getName() : null;
    }

    /**
     * API to get workflow initiator assignment in Property Tax.
     *
     * @return assignment
     */
    public Assignment getWorkflowInitiatorAssignment(final Long userId) {
        Assignment wfInitiatorAssignment = null;
        if (userId != null) {
            final List<Assignment> assignmentList = assignmentService.getAllActiveEmployeeAssignmentsByEmpId(userId);
            for (final Assignment assignment : assignmentList)
                if (assignment.getDesignation().getName().equals(PropertyTaxConstants.REVENUE_INSPECTOR_DESGN)
                        && assignment.getEmployee().isActive()) {
                    wfInitiatorAssignment = assignment;
                    break;
                }
        }
        return wfInitiatorAssignment;
    }

    public Assignment getWorkflowInitiator(final StateAware property) {
        Assignment wfInitiator;
        List<Assignment> assignment;
        if (isEmployee(property.getCreatedBy())) {
            if (isStateNotNull(property))
                wfInitiator = getWfInitiatorIfStateNotNull(property);
            else
                wfInitiator = assignmentService.getPrimaryAssignmentForUser(property
                        .getCreatedBy().getId());
        } else if (property.getState().getInitiatorPosition() != null) {
            assignment = assignmentService.getAssignmentsForPosition(property.getState().getInitiatorPosition().getId(),
                    new Date());
            wfInitiator = getActiveAssignment(assignment);
            if (wfInitiator == null && !property.getStateHistory().isEmpty())
                wfInitiator = assignmentService.getPrimaryAssignmentForPositon(property.getStateHistory().get(0)
                        .getOwnerPosition().getId());
        } else
            wfInitiator = assignmentService.getPrimaryAssignmentForPositon(property.getState().getOwnerPosition()
                    .getId());
        return wfInitiator;
    }

    private Assignment getWfInitiatorIfStateNotNull(final StateAware property) {
        List<Assignment> assignment;
        Assignment wfInitiator = null;
        final Assignment assgmnt = propertyTaxCommonUtils.getUserAssignmentByPassingPositionAndUser(property
                .getCreatedBy(), property.getState().getInitiatorPosition());
        if (assgmnt != null && assgmnt.getEmployee().isActive())
            wfInitiator = assgmnt;
        if (wfInitiator == null) {
            assignment = assignmentService.getAssignmentsForPosition(property.getState().getInitiatorPosition().getId(),
                    new Date());
            wfInitiator = getActiveAssignment(assignment);
        }
        return wfInitiator;
    }

    private boolean isStateNotNull(final StateAware property) {
        return property.getState() != null && property.getState().getInitiatorPosition() != null;
    }

    private Assignment getActiveAssignment(final List<Assignment> assignment) {
        Assignment wfInitiator = null;
        for (final Assignment assign : assignment)
            if (assign.getEmployee().isActive()) {
                wfInitiator = assign;
                break;
            }
        return wfInitiator;
    }

    public Boolean isEmployee(final User user) {
        for (final Role role : user.getRoles())
            for (final AppConfigValues appconfig : getThirdPartyUserRoles())
                if (role != null && role.getName().equals(appconfig.getValue()))
                    return false;
        return true;
    }

    public List<AppConfigValues> getThirdPartyUserRoles() {

        final List<AppConfigValues> appConfigValueList = appConfigValuesService.getConfigValuesByModuleAndKey(
                PTMODULENAME, PROPERTYTAX_ROLEFORNONEMPLOYEE);

        return !appConfigValueList.isEmpty() ? appConfigValueList : null;

    }

    @Transactional
    public void closeVacancyRemission(final VacancyRemission vacancyRemission) {
        final User user = securityUtils.getCurrentUser();
        vacancyRemission.transition().end().withSenderName(user.getName()).withDateInfo(new Date());
        if (!VR_STATUS_APPROVED.equals(vacancyRemission.getStatus())) {
            vacancyRemission.setStatus(VR_STATUS_REJECTION_ACK_GENERATED);
            vacancyRemission.getBasicProperty().setUnderWorkflow(false);
        }
    }
    
    private String getLoggedInUserDesignation(final VacancyRemission vacancyRemission, final User user) {
        String loggedInUserDesignation = "";
        List<Assignment> loggedInUserAssign;
        if (vacancyRemission.getState() != null) {
            loggedInUserAssign = assignmentService.getAssignmentByPositionAndUserAsOnDate(
                    vacancyRemission.getCurrentState().getOwnerPosition().getId(), user.getId(), new Date());
            loggedInUserDesignation = !loggedInUserAssign.isEmpty()
                    ? loggedInUserAssign.get(0).getDesignation().getName() : "";
        }
        if (JUNIOR_ASSISTANT.equals(loggedInUserDesignation) || SENIOR_ASSISTANT.equals(loggedInUserDesignation))
            loggedInUserDesignation = "";

        return loggedInUserDesignation;
    }
    
    private String getWorkflowNextAction(String designation) {
        String nextAction;
        if (COMMISSIONER_DESGN.equalsIgnoreCase(designation))
            nextAction = VR_STATUS_COMMISSIONER_FORWARD_PENDING;
        else {
            nextAction = new StringBuilder().append(designation).append(" ")
                    .append(VR_STATUS_COMMISSIONER_FORWARD_PENDING).toString();
        }
        return nextAction;

    }
    
    private String getApprovalAsNextAction(String designation) {
        String nextAction;
        if (designation.equalsIgnoreCase(COMMISSIONER_DESGN))
            nextAction = WF_STATE_COMMISSIONER_APPROVAL_PENDING;
        else
            nextAction = new StringBuilder().append(designation).append(" ")
                    .append(WF_STATE_COMMISSIONER_APPROVAL_PENDING).toString();
        return nextAction;

    }

    private Assignment getInitiatorOnWFAction(final VacancyRemissionApproval vacancyRemissionApproval,
            String wfAction) {
        Assignment wfInitiator = null;
        if (vacancyRemissionApproval.getId() != null && WFLOW_ACTION_STEP_REJECT.equalsIgnoreCase(wfAction))
            wfInitiator = assignmentService
                    .getPrimaryAssignmentForUser(vacancyRemissionApproval.getCreatedBy().getId());
        if (vacancyRemissionApproval.getId() != null && WFLOW_ACTION_STEP_NOTICE_GENERATE.equalsIgnoreCase(wfAction))
            wfInitiator = assignmentService
                    .getPrimaryAssignmentForUser(vacancyRemissionApproval.getVacancyRemission().getCreatedBy().getId());

        return wfInitiator;
    }
    
    public BigDecimal getWaterTaxDues(final String assessmentNo, final HttpServletRequest request){
        return propertyService.getWaterTaxDues(assessmentNo, request).get(PropertyTaxConstants.WATER_TAX_DUES) == null ? BigDecimal.ZERO : new BigDecimal(
                Double.valueOf((Double) propertyService.getWaterTaxDues(assessmentNo, request).get(PropertyTaxConstants.WATER_TAX_DUES)));
    }
    
    public Boolean isUnderWtmsWF(String assessmentNo, final HttpServletRequest request){
        return propertyService.getWaterTaxDues(assessmentNo, request).get(PropertyTaxConstants.UNDER_WTMS_WF) == null
                ? FALSE
                : Boolean.valueOf((Boolean) propertyService.getWaterTaxDues(assessmentNo, request)
                        .get(PropertyTaxConstants.UNDER_WTMS_WF));
    }
    
}
