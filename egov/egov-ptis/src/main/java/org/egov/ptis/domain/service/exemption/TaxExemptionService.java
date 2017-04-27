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

package org.egov.ptis.domain.service.exemption;

import static java.lang.Boolean.FALSE;
import static org.egov.ptis.constants.PropertyTaxConstants.ADDITIONAL_COMMISSIONER_DESIGN;
import static org.egov.ptis.constants.PropertyTaxConstants.ANONYMOUS_USER;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ASSISTANT_COMMISSIONER_DESIGN;
import static org.egov.ptis.constants.PropertyTaxConstants.COMMISSIONER_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.CURRENTYEAR_FIRST_HALF;
import static org.egov.ptis.constants.PropertyTaxConstants.CURRENTYEAR_SECOND_HALF;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_STR_EDUCATIONAL_CESS;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_STR_GENERAL_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_STR_LIBRARY_CESS;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_STR_UNAUTHORIZED_PENALTY;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_STR_VACANT_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEPUTY_COMMISSIONER_DESIGN;
import static org.egov.ptis.constants.PropertyTaxConstants.EXEMPTION;
import static org.egov.ptis.constants.PropertyTaxConstants.JUNIOR_ASSISTANT;
import static org.egov.ptis.constants.PropertyTaxConstants.NATURE_TAX_EXEMPTION;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_OFFICER_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.SENIOR_ASSISTANT;
import static org.egov.ptis.constants.PropertyTaxConstants.SOURCE_ONLINE;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_CANCELLED;
import static org.egov.ptis.constants.PropertyTaxConstants.UD_REVENUE_INSPECTOR_APPROVAL_PENDING;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_APPROVE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_FORWARD;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_REJECT;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_ASSISTANT_APPROVAL_PENDING;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_COMMISSIONER_APPROVAL_PENDING;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_DIGITAL_SIGNATURE_PENDING;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_REJECTED;
import static org.egov.ptis.constants.PropertyTaxConstants.ZONAL_COMMISSIONER_DESIGN;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_TAX_EXEMTION;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.egov.commons.Installment;
import org.egov.demand.model.EgDemandDetails;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.messaging.MessagingService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.ApplicationNumberGenerator;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.Position;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.enums.TransactionType;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Document;
import org.egov.ptis.domain.entity.property.DocumentType;
import org.egov.ptis.domain.entity.property.Floor;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyDetail;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyOwnerInfo;
import org.egov.ptis.domain.entity.property.TaxExemptionReason;
import org.egov.ptis.domain.repository.vacancyremission.VacancyRemissionRepository;
import org.egov.ptis.domain.service.property.PropertyPersistenceService;
import org.egov.ptis.domain.service.property.PropertyService;
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
@Transactional
public class TaxExemptionService extends PersistenceService<PropertyImpl, Long> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaxExemptionService.class);

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private PropertyPersistenceService propertyPerService;

    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<PropertyImpl> propertyWorkflowService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private PropertyTaxUtil propertyTaxUtil;

    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;

    PropertyImpl propertyModel = new PropertyImpl();

    @Autowired
    private ApplicationNumberGenerator applicationNumberGenerator;

    @Autowired
    private PtDemandDao ptDemandDAO;

    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource ptisMessageSource;

    @Autowired
    private MessagingService messagingService;

    @Autowired
    private AssignmentService assignmentService;
    
    @Autowired
    private VacancyRemissionRepository vacancyRemissionRepository;

    Property property = null;

    public TaxExemptionService() {
        super(PropertyImpl.class);
    }

    public TaxExemptionService(final Class<PropertyImpl> type) {
        super(type);
    }

    @Transactional
    public BasicProperty saveProperty(final Property newProperty, final Property oldProperty, final Character status,
            final String approvalComment, final String workFlowAction, final Long approvalPosition,
            final String taxExemptedReason, final Boolean propertyByEmployee, final String additionalRule) {

        TaxExemptionReason taxExemptionReason=null;
        final BasicProperty basicProperty = oldProperty.getBasicProperty();
        final PropertyDetail propertyDetail = oldProperty.getPropertyDetail();
        propertyDetail.getDateOfCompletion();
        propertyModel = (PropertyImpl) newProperty;
        propertyModel.setStatus(status);
        if (propertyModel.getApplicationNo() == null)
            propertyModel.setApplicationNo(applicationNumberGenerator.generate());

        final Map<String, Installment> yearwiseInstMap = propertyTaxUtil.getInstallmentsForCurrYear(new Date());
        final Installment installmentFirstHalf = yearwiseInstMap.get(CURRENTYEAR_FIRST_HALF);
        final Installment installmentSecondHalf = yearwiseInstMap.get(CURRENTYEAR_SECOND_HALF);
        Date effectiveDate = null;
        if(SOURCE_ONLINE.equalsIgnoreCase(propertyModel.getSource()) && ApplicationThreadLocals.getUserId() == null) 
            ApplicationThreadLocals.setUserId(securityUtils.getCurrentUser().getId());
        /*
         * While converting an exempted property to non-exempted property, effective date will be the installment from date of the
         * current installment. Else, effective date will be the starting date of the next installment
         */
        if (DateUtils.between(new Date(), installmentFirstHalf.getFromDate(), installmentFirstHalf.getToDate())) {
            if (StringUtils.isNotBlank(taxExemptedReason) && !taxExemptedReason.equals("-1"))
                effectiveDate = installmentSecondHalf.getFromDate();
            else
                effectiveDate = installmentFirstHalf.getFromDate();
        } else if (StringUtils.isNotBlank(taxExemptedReason) && !taxExemptedReason.equals("-1"))
            effectiveDate = org.apache.commons.lang3.time.DateUtils.addYears(installmentFirstHalf.getFromDate(), 1);
        else
            effectiveDate = installmentSecondHalf.getFromDate();

        if (!propertyModel.getPropertyDetail().getPropertyTypeMaster().getCode()
                .equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
            propertyService.getLowestDtOfCompFloorWise(propertyDetail.getFloorDetails());
        else
            propertyDetail.getDateOfCompletion();
        for (final Floor floor : propertyModel.getPropertyDetail().getFloorDetails()) {
            propertyPerService.applyAuditing(floor);
            floor.setPropertyDetail(propertyModel.getPropertyDetail());
        }
        if (StringUtils.isNotBlank(taxExemptedReason) && !taxExemptedReason.equals("-1")) {
            taxExemptionReason = (TaxExemptionReason) propertyPerService.find(
                    "From TaxExemptionReason where id = ?", Long.valueOf(taxExemptedReason));
            propertyModel.setTaxExemptedReason(taxExemptionReason);
            propertyModel.setIsExemptedFromTax(Boolean.TRUE);
        } else {
            propertyModel.setTaxExemptedReason(null);
            propertyModel.setIsExemptedFromTax(Boolean.FALSE);
        }
        propertyModel.setEffectiveDate(effectiveDate);
        basicProperty.setUnderWorkflow(Boolean.TRUE);
        final Set<Ptdemand> newPtdemandSet = propertyModel.getPtDemandSet();
        final Set<EgDemandDetails> demandDetailSet = new HashSet<EgDemandDetails>();

        if (StringUtils.isNotBlank(taxExemptedReason) && !taxExemptedReason.equals("-1")) {
            // Do not do anything
        } else // Remove all the previous demands until the current installment
        if (StringUtils.isNotBlank(workFlowAction) && !workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_REJECT))
            for (final Ptdemand ptdemand : newPtdemandSet)
                if (ptdemand.getEgInstallmentMaster().equals(installmentFirstHalf)) {
                    for (final EgDemandDetails demandDetails : ptdemand.getEgDemandDetails())
                        if (demandDetails.getInstallmentStartDate().equals(effectiveDate)
                                || demandDetails.getInstallmentStartDate().after(effectiveDate))
                            demandDetailSet.add(demandDetails);
                    ptdemand.getEgDemandDetails().clear();
                    ptdemand.getEgDemandDetails().addAll(demandDetailSet);
                }

        for (final Ptdemand ptdemand : newPtdemandSet)
            propertyPerService.applyAuditing(ptdemand.getDmdCalculations());
        if(taxExemptionReason != null)
            processAndStoreApplicationDocuments(propertyModel, taxExemptionReason.getCode());
        propertyModel.setBasicProperty(basicProperty);
        basicProperty.addProperty(propertyModel);
        transitionWorkFlow(propertyModel, approvalComment, workFlowAction, approvalPosition, additionalRule,
                propertyByEmployee);
        propertyService.updateIndexes(propertyModel, APPLICATION_TYPE_TAX_EXEMTION);
        return propertyPerService.update(basicProperty);

    }

    @Transactional
    public void updateProperty(final Property newProperty, final String comments, final String workFlowAction,
            final Long approverPosition, final Boolean propertyByEmployee, final String additionalRule) {
        transitionWorkFlow((PropertyImpl) newProperty, comments, workFlowAction, approverPosition, additionalRule,
                propertyByEmployee);
        propertyService.updateIndexes(propertyModel, APPLICATION_TYPE_TAX_EXEMTION);
        propertyPerService.update(newProperty.getBasicProperty());
    }

    private void transitionWorkFlow(final PropertyImpl property, final String approvarComments,
            final String workFlowAction, Long approverPosition, final String additionalRule,
            final Boolean propertyByEmployee) {

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("WorkFlow Transition For Demolition Started  ...");
        final User user = securityUtils.getCurrentUser();
        final DateTime currentDate = new DateTime();
        Position pos = null;
        String currentState;
        Assignment wfInitiator = null;
        Assignment assignment = null;
        String approverDesignation = "";
        String nextAction = "";

        if (!propertyByEmployee || ANONYMOUS_USER.equalsIgnoreCase(user.getName())) {
            currentState = "Created";
            if (propertyService.isCscOperator(user)) {
                assignment = propertyService.getMappedAssignmentForCscOperator(property.getBasicProperty());
                wfInitiator = assignment;
            } else {
                assignment = propertyService.getUserPositionByZone(property.getBasicProperty(), false);
                wfInitiator = assignment;
            }
            if (null != assignment)
                approverPosition = assignment.getPosition().getId();
        } else {
            currentState = null;
            if (null != approverPosition && approverPosition != 0) {
                assignment = assignmentService.getAssignmentsForPosition(approverPosition, new Date())
                        .get(0);
                assignment.getEmployee().getName().concat("~")
                        .concat(assignment.getPosition().getName());
                approverDesignation = assignment.getDesignation().getName();
            }
        }

        String loggedInUserDesignation = "";
        if (property.getState() != null)
            loggedInUserDesignation = getLoggedInUserDesignation(property.getCurrentState().getOwnerPosition().getId(),
                    securityUtils.getCurrentUser());

        if (loggedInUserDesignation.equals(JUNIOR_ASSISTANT) || loggedInUserDesignation.equals(SENIOR_ASSISTANT))
            loggedInUserDesignation = null;

        if (WFLOW_ACTION_STEP_FORWARD.equalsIgnoreCase(workFlowAction)
                && (approverDesignation.equalsIgnoreCase(ASSISTANT_COMMISSIONER_DESIGN) ||
                        approverDesignation.equalsIgnoreCase(DEPUTY_COMMISSIONER_DESIGN)
                        || approverDesignation.equalsIgnoreCase(ADDITIONAL_COMMISSIONER_DESIGN)
                        || approverDesignation.equalsIgnoreCase(ZONAL_COMMISSIONER_DESIGN) ||
                        approverDesignation.equalsIgnoreCase(COMMISSIONER_DESGN)))
            if (property.getCurrentState().getNextAction().equalsIgnoreCase(WF_STATE_DIGITAL_SIGNATURE_PENDING))
                nextAction = WF_STATE_DIGITAL_SIGNATURE_PENDING;
            else {
                final String designation = approverDesignation.split(" ")[0];
                if (designation.equalsIgnoreCase(COMMISSIONER_DESGN))
                    nextAction = WF_STATE_COMMISSIONER_APPROVAL_PENDING;
                else
                    nextAction = new StringBuilder().append(designation).append(" ")
                            .append(WF_STATE_COMMISSIONER_APPROVAL_PENDING)
                            .toString();
            }
        if (property.getId() != null && property.getState() != null)
            wfInitiator = propertyService.getWorkflowInitiator(property);
        else if (wfInitiator == null)
            wfInitiator = propertyTaxCommonUtils.getWorkflowInitiatorAssignment(user.getId());

        if (WFLOW_ACTION_STEP_REJECT.equalsIgnoreCase(workFlowAction)) {
            if (wfInitiator.getPosition().equals(property.getState().getOwnerPosition())) {
                property.transition().end().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvarComments).withDateInfo(currentDate.toDate());
                property.setStatus(STATUS_CANCELLED);
                property.getBasicProperty().setUnderWorkflow(FALSE);
            } else {
                final Assignment assignmentOnreject = getUserAssignmentOnReject(loggedInUserDesignation, property);
                if (assignmentOnreject != null) {
                    nextAction = UD_REVENUE_INSPECTOR_APPROVAL_PENDING;
                    wfInitiator = assignmentOnreject;
                } else
                    nextAction = WF_STATE_ASSISTANT_APPROVAL_PENDING;
                final String stateValue = property.getCurrentState().getValue().split(":")[0] + ":" + WF_STATE_REJECTED;
                property.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvarComments).withStateValue(stateValue).withDateInfo(currentDate.toDate())
                        .withOwner(wfInitiator.getPosition()).withNextAction(nextAction);
                buildSMS(property, workFlowAction);
            }

        } else {
            if (WFLOW_ACTION_STEP_APPROVE.equalsIgnoreCase(workFlowAction))
                pos = property.getCurrentState().getOwnerPosition();
            else if (null != approverPosition && approverPosition != -1 && !approverPosition.equals(Long.valueOf(0)))
                pos = positionMasterService.getPositionById(approverPosition);
            WorkFlowMatrix wfmatrix;
            if (null == property.getState()) {
                wfmatrix = propertyWorkflowService.getWfMatrix(property.getStateType(), null, null, additionalRule,
                        currentState, null);
                property.transition().start().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvarComments).withStateValue(wfmatrix.getNextState())
                        .withDateInfo(new Date()).withOwner(pos).withNextAction(wfmatrix.getNextAction())
                        .withNatureOfTask(NATURE_TAX_EXEMPTION)
                        .withInitiator(wfInitiator != null ? wfInitiator.getPosition() : null);
                // to be enabled once acknowledgement feature is developed
                // buildSMS(property, workFlowAction);
            } else if (property.getCurrentState().getNextAction().equalsIgnoreCase("END"))
                property.transition().end().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvarComments).withDateInfo(currentDate.toDate());
            else {
                wfmatrix = propertyWorkflowService.getWfMatrix(property.getStateType(), null, null, additionalRule,
                        property.getCurrentState().getValue(), property.getCurrentState().getNextAction(), null,
                        loggedInUserDesignation);
                property.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvarComments).withStateValue(wfmatrix.getNextState())
                        .withDateInfo(currentDate.toDate()).withOwner(pos)
                        .withNextAction(StringUtils.isNotBlank(nextAction) ? nextAction : wfmatrix.getNextAction());

                if (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_APPROVE))
                    buildSMS(property, workFlowAction);
            }
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug(" WorkFlow Transition Completed for Demolition ...");
    }

    public void addModelAttributes(final Model model, final BasicProperty basicProperty) {
        if (null != basicProperty.getWFProperty())
            property = basicProperty.getWFProperty();
        else
            property = basicProperty.getActiveProperty();
        final Ptdemand ptDemand = ptDemandDAO.getNonHistoryCurrDmdForProperty(property);
        if (ptDemand != null && ptDemand.getDmdCalculations() != null && ptDemand.getDmdCalculations().getAlv() != null)
            model.addAttribute("ARV", ptDemand.getDmdCalculations().getAlv());
        else
            model.addAttribute("ARV", BigDecimal.ZERO);
        model.addAttribute("propertyByEmployee", propertyService.isEmployee(securityUtils.getCurrentUser())
                && !ANONYMOUS_USER.equalsIgnoreCase(securityUtils.getCurrentUser().getName()));
        if (!property.getIsExemptedFromTax()) {
            Map<String, Map<String, BigDecimal>> demandCollMap;
            try {
                demandCollMap = propertyTaxUtil.prepareDemandDetForView(property,
                        propertyTaxCommonUtils.getCurrentInstallment());

                final Map<String, BigDecimal> currentTaxDetails = propertyService.getCurrentTaxDetails(demandCollMap, new Date());
                model.addAttribute("currTax", currentTaxDetails.get(CURR_DMD_STR));
                model.addAttribute("eduCess", currentTaxDetails.get(DEMANDRSN_STR_EDUCATIONAL_CESS));
                model.addAttribute("currTaxDue",
                        currentTaxDetails.get(CURR_DMD_STR).subtract(currentTaxDetails.get(CURR_COLL_STR)));
                model.addAttribute("libraryCess", currentTaxDetails.get(DEMANDRSN_STR_LIBRARY_CESS));
                model.addAttribute("totalArrDue",
                        currentTaxDetails.get(ARR_DMD_STR).subtract(currentTaxDetails.get(ARR_COLL_STR)));
                BigDecimal propertyTax = BigDecimal.ZERO;
                if (null != currentTaxDetails.get(DEMANDRSN_STR_GENERAL_TAX))
                    propertyTax = currentTaxDetails.get(DEMANDRSN_STR_GENERAL_TAX);
                else
                    propertyTax = currentTaxDetails.get(DEMANDRSN_STR_VACANT_TAX);
                final BigDecimal totalTax = propertyTax
                        .add(currentTaxDetails.get(DEMANDRSN_STR_LIBRARY_CESS) == null ? BigDecimal.ZERO
                                : currentTaxDetails.get(DEMANDRSN_STR_LIBRARY_CESS))
                        .add(currentTaxDetails.get(DEMANDRSN_STR_EDUCATIONAL_CESS) == null ? BigDecimal.ZERO
                                : currentTaxDetails.get(DEMANDRSN_STR_EDUCATIONAL_CESS));
                model.addAttribute("propertyTax", propertyTax);
                if (currentTaxDetails.get(DEMANDRSN_STR_UNAUTHORIZED_PENALTY) != null) {
                    model.addAttribute("unauthorisedPenalty", currentTaxDetails.get(DEMANDRSN_STR_UNAUTHORIZED_PENALTY));
                    model.addAttribute("totalTax", totalTax.add(currentTaxDetails.get(DEMANDRSN_STR_UNAUTHORIZED_PENALTY)));
                    model.addAttribute("showUnauthorisedPenalty", "yes");
                } else {
                    model.addAttribute("totalTax", totalTax);
                    model.addAttribute("showUnauthorisedPenalty", "no");
                }
            } catch (final Exception e) {

                throw new ApplicationRuntimeException("Exception in addModelAttributes : " + e);
            }
        }

    }

    public Boolean isPropertyByEmployee(final Property property) {
        return propertyService.isEmployee(property.getCreatedBy());
    }

    public BasicProperty saveProperty(final Property newProperty, final Property oldProperty, final Character status,
            final String approvalComment, final String workFlowAction, final Long approvalPosition,
            final String taxExemptedReason, final Boolean propertyByEmployee, final String additionalRule,
            final HashMap<String, String> meesevaParams) {
        return saveProperty(newProperty, oldProperty, status, approvalComment, workFlowAction, approvalPosition,
                taxExemptedReason, propertyByEmployee, EXEMPTION);

    }

    public void buildSMS(final Property property, final String workFlowAction) {
        for (final PropertyOwnerInfo ownerInfo : property.getBasicProperty().getPropertyOwnerInfo())
            if (StringUtils.isNotBlank(ownerInfo.getOwner().getMobileNumber()))
                buildSms(property, ownerInfo.getOwner(), workFlowAction);
    }

    private void buildSms(final Property property, final User user, final String workFlowAction) {
        final String assessmentNo = property.getBasicProperty().getUpicNo();
        final String mobileNumber = user.getMobileNumber();
        final String applicantName = user.getName();
        String smsMsg = "";
        if (workFlowAction.equals(WFLOW_ACTION_STEP_FORWARD)) {
            // to be enabled once acknowledgement feature is developed
            /*
             * smsMsg = messageSource.getMessage("msg.initiateexemption.sms", new String[] { applicantName, assessmentNo }, null);
             */
        } else if (workFlowAction.equals(WFLOW_ACTION_STEP_REJECT))
            smsMsg = ptisMessageSource.getMessage("msg.rejectexemption.sms", new String[] { applicantName, assessmentNo,
                    ApplicationThreadLocals.getMunicipalityName() }, null);
        else if (workFlowAction.equals(WFLOW_ACTION_STEP_APPROVE)) {
            final Installment installment = propertyTaxUtil.getInstallmentListByStartDate(new Date()).get(0);
            final Date effectiveDate = org.apache.commons.lang3.time.DateUtils.addDays(installment.getToDate(), 1);
            smsMsg = ptisMessageSource.getMessage("msg.approveexemption.sms", new String[] { applicantName, assessmentNo,
                    new SimpleDateFormat("dd/MM/yyyy").format(effectiveDate), ApplicationThreadLocals.getMunicipalityName() },
                    null);
        }

        if (StringUtils.isNotBlank(mobileNumber))
            messagingService.sendSMS(mobileNumber, smsMsg);

    }

    public String getLoggedInUserDesignation(final Long posId, final User user) {
        final List<Assignment> loggedInUserAssign = assignmentService.getAssignmentByPositionAndUserAsOnDate(
                posId, user.getId(), new Date());
        return !loggedInUserAssign.isEmpty() ? loggedInUserAssign.get(0).getDesignation().getName() : null;
    }

    public Assignment getUserAssignmentOnReject(final String loggedInUserDesignation, final PropertyImpl property) {
        Assignment assignmentOnreject = null;
        if (loggedInUserDesignation.equalsIgnoreCase(REVENUE_OFFICER_DESGN)
                || loggedInUserDesignation.equalsIgnoreCase(ASSISTANT_COMMISSIONER_DESIGN) ||
                loggedInUserDesignation.equalsIgnoreCase(ADDITIONAL_COMMISSIONER_DESIGN)
                || loggedInUserDesignation.equalsIgnoreCase(DEPUTY_COMMISSIONER_DESIGN) ||
                loggedInUserDesignation.equalsIgnoreCase(COMMISSIONER_DESGN) ||
                loggedInUserDesignation.equalsIgnoreCase(ZONAL_COMMISSIONER_DESIGN))
            assignmentOnreject = propertyService.getUserOnRejection(property);

        return assignmentOnreject;

    }

    public Assignment getWfInitiator(final PropertyImpl property) {
        return propertyService.getWorkflowInitiator(property);
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
    
    public List<DocumentType> getDocuments(final TransactionType transactionType) {
        return propertyService.getDocumentTypesForTransactionType(transactionType);
    }

    @Transactional
    public void processAndStoreApplicationDocuments(final PropertyImpl property, String exemptionCode) {
        final TransactionType transactionType = getTransactionTypeByExemptionCode(exemptionCode);
        for (final Document applicationDocument : property.getTaxExemptionDocumentsProxy()) {
            if (applicationDocument.getFile() != null)
                property.getTaxExemptionDocuments().clear();
        }
        if (!property.getTaxExemptionDocumentsProxy().isEmpty())
            for (final Document applicationDocument : property.getTaxExemptionDocumentsProxy()) {
                if (applicationDocument.getFile() != null) {
                    applicationDocument.setType(vacancyRemissionRepository.findDocumentTypeByNameAndTransactionType(
                            applicationDocument.getType().getName(), transactionType));
                    applicationDocument.setFiles(propertyService.addToFileStore(applicationDocument.getFile()));
                    property.addTaxExemptionDocuments(applicationDocument);
                }

            }
    }

    public TransactionType getTransactionTypeByExemptionCode(String exemptionCode) {
        TransactionType transactionType;
        if (exemptionCode.equals(PropertyTaxConstants.EXEMPTION_PUBLIC_WORSHIP))
            transactionType = TransactionType.TE_PUBLIC_WORSHIP;
        else if (exemptionCode.equals(PropertyTaxConstants.EXEMPTION_CHOULTRY))
            transactionType = TransactionType.TE_CHOULTRY;
        else if (exemptionCode.equals(PropertyTaxConstants.EXEMPTION_EDU_INST))
            transactionType = TransactionType.TE_EDU_INST;
        else if (exemptionCode.equals(PropertyTaxConstants.EXEMPTION_EXSERVICE))
            transactionType = TransactionType.TE_EXSERVICE;
        else
            transactionType = TransactionType.TE_PENSIONER_NGO;
        return transactionType;
    }

}