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

package org.egov.ptis.domain.service.demolition;

import org.apache.commons.lang3.StringUtils;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentDao;
import org.egov.demand.model.EgDemandDetails;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.notification.service.NotificationService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.Position;
import org.egov.ptis.client.bill.PTBillServiceImpl;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.PropertyTypeMasterDAO;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyDetail;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyOwnerInfo;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.ptis.domain.entity.property.VacantProperty;
import org.egov.ptis.domain.service.property.PropertyPersistenceService;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.exceptions.TaxCalculatorExeption;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.hibernate.FlushMode;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.egov.ptis.constants.PropertyTaxConstants.*;

public class PropertyDemolitionService extends PersistenceService<PropertyImpl, Long> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyDemolitionService.class);

    @Autowired
    private PropertyTypeMasterDAO propertyTypeMasterDAO;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private PropertyPersistenceService propertyPerService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    private PTBillServiceImpl ptBillServiceImpl;

    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<PropertyImpl> propertyWorkflowService;

    @Autowired
    private PropertyTaxUtil propertyTaxUtil;

    @Autowired
    private PtDemandDao ptDemandDAO;

    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource ptisMessageSource;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private InstallmentDao installmentDao;

    @Autowired
    private ModuleService moduleDao;

    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;

    @Autowired
    private AssignmentService assignmentService;

    public PropertyDemolitionService() {
        super(PropertyImpl.class);
    }

    public PropertyDemolitionService(final Class<PropertyImpl> type) {
        super(type);
    }

    @Transactional
    public BasicProperty saveProperty(final Property oldProperty, final Property newProperty, final Character status,
            final String comments,
            final String workFlowAction, final Long approverPosition, final String additionalRule) throws TaxCalculatorExeption {
        PropertyImpl propertyModel;
        final BasicProperty basicProperty = oldProperty.getBasicProperty();
        final PropertyTypeMaster propTypeMstr = propertyTypeMasterDAO.getPropertyTypeMasterByCode(OWNERSHIP_TYPE_VAC_LAND);
        propertyModel = (PropertyImpl) newProperty;
        newProperty.getPropertyDetail().setPropertyTypeMaster(propTypeMstr);
        newProperty.getBasicProperty().setPropOccupationDate(newProperty.getPropertyDetail().getDateOfCompletion());
        final String areaOfPlot = String.valueOf(propertyModel.getPropertyDetail().getSitalArea().getArea());
        propertyModel = propertyService.createProperty(propertyModel, areaOfPlot, PROPERTY_MODIFY_REASON_FULL_DEMOLITION,
                propertyModel.getPropertyDetail().getPropertyTypeMaster().getId().toString(), null, null, status, null,
                null, null, null, null, null, null, null, null, null, FALSE);
        if(SOURCE_ONLINE.equalsIgnoreCase(propertyModel.getSource()) && ApplicationThreadLocals.getUserId() == null) 
            ApplicationThreadLocals.setUserId(securityUtils.getCurrentUser().getId());
        final Map<String, Installment> yearwiseInstMap = propertyTaxUtil.getInstallmentsForCurrYear(new Date());
        final Installment installmentFirstHalf = yearwiseInstMap.get(CURRENTYEAR_FIRST_HALF);
        final Installment installmentSecondHalf = yearwiseInstMap.get(CURRENTYEAR_SECOND_HALF);
        Date effectiveDate;
        if (DateUtils.between(new Date(), installmentFirstHalf.getFromDate(), installmentFirstHalf.getToDate()))
            effectiveDate = installmentFirstHalf.getFromDate();
        else
            effectiveDate = installmentSecondHalf.getFromDate();
        propertyModel.setBasicProperty(basicProperty);
        propertyModel.setEffectiveDate(effectiveDate);
        if (!propertyModel.getPropertyDetail().getPropertyTypeMaster().getCode().equals(OWNERSHIP_TYPE_VAC_LAND))
            propertyService.changePropertyDetail(propertyModel, new VacantProperty(), 0);
        propertyModel.getPropertyDetail().setCategoryType(VACANTLAND_PROPERTY_CATEGORY);
        basicProperty.setUnderWorkflow(TRUE);
        propertyModel.setBasicProperty(basicProperty);
        propertyModel.setPropertyModifyReason(DEMOLITION);
        basicProperty.addProperty(propertyModel);
        getSession().setFlushMode(FlushMode.MANUAL);
        transitionWorkFlow(propertyModel, comments, workFlowAction, approverPosition, additionalRule);
        final Installment currInstall = propertyTaxCommonUtils.getCurrentInstallment();

        final Property modProperty = propertyService.createDemand(propertyModel, effectiveDate);
        Ptdemand currPtDmd = null;
        for (final Ptdemand demand : modProperty.getPtDemandSet())
            if ("N".equalsIgnoreCase(demand.getIsHistory()))
                if (demand.getEgInstallmentMaster().equals(currInstall)) {
                    currPtDmd = demand;
                    break;
                }
        Ptdemand oldCurrPtDmd = null;
        for (final Ptdemand ptDmd : oldProperty.getPtDemandSet())
            if ("N".equalsIgnoreCase(ptDmd.getIsHistory()))
                if (ptDmd.getEgInstallmentMaster().equals(currInstall)) {
                    oldCurrPtDmd = ptDmd;
                    break;
                }

        Installment effectiveInstall;
        final Module module = moduleDao.getModuleByName(PTMODULENAME);
        effectiveInstall = installmentDao.getInsatllmentByModuleForGivenDate(module, effectiveDate);
        propertyService.addArrDmdDetToCurrentDmd(oldCurrPtDmd, currPtDmd, effectiveInstall, true);
        basicProperty.addProperty(modProperty);
        for (final Ptdemand ptDemand : modProperty.getPtDemandSet())
            propertyPerService.applyAuditing(ptDemand.getDmdCalculations());
        adjustCollection(oldCurrPtDmd, currPtDmd, effectiveInstall);
        propertyService.updateIndexes(propertyModel, APPLICATION_TYPE_DEMOLITION);
        propertyPerService.update(basicProperty);
        getSession().flush();
        return basicProperty;
    }

    private Ptdemand adjustCollection(final Ptdemand oldCurrPtDmd, final Ptdemand currPtDmd, final Installment effectiveInstall) {
        BigDecimal totalColl = BigDecimal.ZERO;

        for (final EgDemandDetails oldDmdDtls : oldCurrPtDmd.getEgDemandDetails())
            if (oldDmdDtls.getInstallmentStartDate().equals(effectiveInstall.getFromDate())
                    || oldDmdDtls.getInstallmentStartDate().after(effectiveInstall.getFromDate()))
                totalColl = totalColl.add(oldDmdDtls.getAmtCollected());
        if (totalColl.compareTo(BigDecimal.ZERO) > 0) {

            for (final EgDemandDetails dmdDtls : currPtDmd.getEgDemandDetails())
                if (dmdDtls.getInstallmentStartDate().equals(effectiveInstall.getFromDate())
                        || dmdDtls.getInstallmentStartDate().after(effectiveInstall.getFromDate()))
                    if (dmdDtls.getAmount().compareTo(totalColl) >= 0) {
                        dmdDtls.setAmtCollected(totalColl);
                        totalColl = BigDecimal.ZERO;
                    } else {
                        dmdDtls.setAmtCollected(dmdDtls.getAmount());
                        totalColl = totalColl.subtract(dmdDtls.getAmount());
                    }
            if (totalColl.compareTo(BigDecimal.ZERO) > 0) {
                EgDemandDetails newDtls;
                final Map<String, Installment> yearwiseInstMap = propertyTaxUtil.getInstallmentsForCurrYear(new Date());
                final Installment installment = yearwiseInstMap.get(CURRENTYEAR_SECOND_HALF);
                newDtls = ptBillServiceImpl.insertDemandDetails(ADVANCE_DMD_RSN_CODE, totalColl,
                        installment);
                currPtDmd.addEgDemandDetails(newDtls);
            }
        }
        return currPtDmd;
    }

    public void updateProperty(final Property newProperty, final String comments, final String workFlowAction,
            final Long approverPosition,
            final String additionalRule) {
        transitionWorkFlow((PropertyImpl) newProperty, comments, workFlowAction, approverPosition, additionalRule);
        propertyService.updateIndexes((PropertyImpl) newProperty, APPLICATION_TYPE_DEMOLITION);
        propertyPerService.update(newProperty.getBasicProperty());
        getSession().flush();
    }

    public Assignment getUserAssignment(final User user, final Property property) {
        Assignment assignment;
        if (propertyService.isCscOperator(user))
            assignment = propertyService.getMappedAssignmentForCscOperator(property.getBasicProperty());
        else
            assignment = propertyService.getUserPositionByZone(property.getBasicProperty(), false);
        return assignment;
    }

    private void transitionWorkFlow(final PropertyImpl property, final String approvarComments, final String workFlowAction,
            Long approverPosition, final String additionalRule) {

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("WorkFlow Transition For Demolition Started  ...");
        final User user = securityUtils.getCurrentUser();
        final DateTime currentDate = new DateTime();
        Position pos = null;
        Assignment wfInitiator = null;
        Assignment assignment;
        String approverDesignation = "";
        String nextAction = "";
        String currentState = "";
        if (isNotEmployee() || ANONYMOUS_USER.equalsIgnoreCase(user.getName())) {
            currentState = "Created";
            assignment = getUserAssignment(user, property);
            if (null != assignment) {
                approverPosition = assignment.getPosition().getId();
                approverDesignation = assignment.getDesignation().getName();
                wfInitiator = assignment;
            }
        } else if (isApproverPosNotNull(approverPosition)) {
            currentState = null;
            assignment = assignmentService.getAssignmentsForPosition(approverPosition, new Date())
                    .get(0);
            approverDesignation = assignment.getDesignation().getName();
        }
        if (property.getId() != null)
            wfInitiator = propertyService.getWorkflowInitiator(property);
        else if (wfInitiator == null)
            wfInitiator = propertyTaxCommonUtils.getWorkflowInitiatorAssignment(user.getId());

        if (isWFForwardOrROOrCommisionner(workFlowAction, approverDesignation))
            nextAction = wfForwardOrCommisionner(property, approverDesignation);

        String loggedInUserDesignation = "";
        if (property.getState() != null)
            loggedInUserDesignation = getLoggedInUserDesignation(property.getCurrentState().getOwnerPosition().getId(),
                    securityUtils.getCurrentUser());

        if (isJuniorOrSenAssistant(loggedInUserDesignation))
            loggedInUserDesignation = null;

        if (isReject(workFlowAction))
            wFReject(property, approvarComments, workFlowAction, user, currentDate, wfInitiator, loggedInUserDesignation);
        else {
            if (isWfApprove(workFlowAction))
                pos = property.getCurrentState().getOwnerPosition();
            else if (isApproverPos(approverPosition))
                pos = positionMasterService.getPositionById(approverPosition);
            else if (isWfSign(workFlowAction))
                pos = wfInitiator.getPosition();
            WorkFlowMatrix wfmatrix;
            if (null == property.getState()) {
                wfmatrix = propertyWorkflowService.getWfMatrix(property.getStateType(), null, null, additionalRule,
                        currentState, null);
                property.transition().start().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvarComments).withStateValue(wfmatrix.getNextState())
                        .withDateInfo(new Date()).withOwner(pos).withNextAction(wfmatrix.getNextAction())
                        .withNatureOfTask(NATURE_DEMOLITION)
                        .withInitiator(wfInitiator != null ? wfInitiator.getPosition() : null);
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
                    wFApprove(property, workFlowAction);
            }
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug(" WorkFlow Transition Completed for Demolition ...");
    }

    private boolean isJuniorOrSenAssistant(final String loggedInUserDesignation) {
        return loggedInUserDesignation.equals(JUNIOR_ASSISTANT) || loggedInUserDesignation.equals(SENIOR_ASSISTANT);
    }

    private boolean isApproverPosNotNull(final Long approverPosition) {
        return null != approverPosition && approverPosition != 0;
    }

    private boolean isNotEmployee() {
        return !propertyService.isEmployee(securityUtils.getCurrentUser());
    }

    private boolean isWfSign(final String workFlowAction) {
        return WFLOW_ACTION_STEP_SIGN.equalsIgnoreCase(workFlowAction);
    }

    private boolean isApproverPos(final Long approverPosition) {
        return null != approverPosition && approverPosition != -1 && !approverPosition.equals(Long.valueOf(0));
    }

    private boolean isWfApprove(final String workFlowAction) {
        return WFLOW_ACTION_STEP_APPROVE.equalsIgnoreCase(workFlowAction);
    }

    private boolean isReject(final String workFlowAction) {
        return WFLOW_ACTION_STEP_REJECT.equalsIgnoreCase(workFlowAction);
    }

    private void wFReject(final PropertyImpl property, final String approvarComments, final String workFlowAction,
            final User user, final DateTime currentDate, Assignment wfInitiator, final String loggedInUserDesignation) {
        String nextAction;
        if (wfInitiator != null && wfInitiator.getPosition().equals(property.getState().getOwnerPosition())) {
            property.transition().end().withSenderName(user.getUsername() + "::" + user.getName())
                    .withComments(approvarComments).withDateInfo(currentDate.toDate()).withNextAction(null).withOwner((Position)null);
            property.setStatus(STATUS_CANCELLED);
            property.getBasicProperty().setUnderWorkflow(FALSE);
        } else {
            final Assignment assignmentOnreject = getUserAssignmentOnReject(loggedInUserDesignation, property);
            if (assignmentOnreject != null) {
                nextAction = UD_REVENUE_INSPECTOR_APPROVAL_PENDING;
                wfInitiator = assignmentOnreject;
            } else
                nextAction = WF_STATE_ASSISTANT_APPROVAL_PENDING;
            if (wfInitiator != null) {
                final String stateValue = property.getCurrentState().getValue().split(":")[0] + ":" + WF_STATE_REJECTED;
                property.transition().progressWithStateCopy()
                        .withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvarComments)
                        .withStateValue(stateValue)
                        .withDateInfo(currentDate.toDate())
                        .withOwner(wfInitiator.getPosition()).withNextAction(nextAction);
                buildSMS(property, workFlowAction);
            }
        }
    }

    private String wfForwardOrCommisionner(final PropertyImpl property, final String approverDesignation) {
        String nextAction;
        if (property.getCurrentState().getNextAction().equalsIgnoreCase(WF_STATE_DIGITAL_SIGNATURE_PENDING))
            nextAction = WF_STATE_DIGITAL_SIGNATURE_PENDING;
        else {
            final String designation = approverDesignation.split(" ")[0];
            if (designation.equalsIgnoreCase(COMMISSIONER_DESGN))
                nextAction = WF_STATE_COMMISSIONER_APPROVAL_PENDING;
            else if (REVENUE_OFFICER_DESGN.equalsIgnoreCase(approverDesignation))
                nextAction = WF_STATE_REVENUE_OFFICER_APPROVAL_PENDING;
            else
                nextAction = new StringBuilder().append(designation).append(" ")
                        .append(WF_STATE_COMMISSIONER_APPROVAL_PENDING)
                        .toString();
        }
        return nextAction;
    }

    private boolean isWFForwardOrROOrCommisionner(final String workFlowAction, final String approverDesignation) {
        return WFLOW_ACTION_STEP_FORWARD.equalsIgnoreCase(workFlowAction)
                && isRoOrCommissioner(approverDesignation);
    }

    private void wFApprove(final PropertyImpl property, final String workFlowAction) {
        buildSMS(property, workFlowAction);
        final String clientSpecificDmdBill = propertyTaxCommonUtils
                .getAppConfigValue(APPCONFIG_CLIENT_SPECIFIC_DMD_BILL, PTMODULENAME);
        if ("Y".equalsIgnoreCase(clientSpecificDmdBill))
            propertyTaxCommonUtils
                    .makeExistingDemandBillInactive(property.getBasicProperty().getUpicNo());
        else
            propertyTaxUtil.makeTheEgBillAsHistory(property.getBasicProperty());
    }

    private boolean isRoOrCommissioner(final String loggedInUserDesignation) {
        boolean isany;
        if (!REVENUE_OFFICER_DESGN.equalsIgnoreCase(loggedInUserDesignation))
            isany = isCommissioner(loggedInUserDesignation);
        else
            isany = true;
        return isany;
    }

    private boolean isCommissioner(final String loggedInUserDesignation) {
        boolean isanyone;
        if (!ASSISTANT_COMMISSIONER_DESIGN.equalsIgnoreCase(loggedInUserDesignation)
                || !ADDITIONAL_COMMISSIONER_DESIGN.equalsIgnoreCase(loggedInUserDesignation))
            isanyone = isDeputyOrAbove(loggedInUserDesignation);
        else
            isanyone = true;
        return isanyone;
    }

    private boolean isDeputyOrAbove(final String loggedInUserDesignation) {
        boolean isanyone = false;
        if (COMMISSIONER_DESIGNATIONS.contains(loggedInUserDesignation))
            isanyone = true;
        return isanyone;
    }

    public void validateProperty(final Property property, final BindingResult errors, final HttpServletRequest request) {
        final PropertyDetail propertyDetail = property.getPropertyDetail();
        if (StringUtils.isBlank(propertyDetail.getPattaNumber()))
            errors.rejectValue("propertyDetail.pattaNumber", "pattaNumber.required");
        if (StringUtils.isBlank(propertyDetail.getSurveyNumber()))
            errors.rejectValue("propertyDetail.surveyNumber", "surveyNumber.required");
        if (null == propertyDetail.getSitalArea().getArea())
            errors.rejectValue("propertyDetail.sitalArea.area", "vacantLandArea.required");
        if (null == propertyDetail.getMarketValue())
            errors.rejectValue("propertyDetail.marketValue", "marketValue.required");
        if (null == propertyDetail.getCurrentCapitalValue())
            errors.rejectValue("propertyDetail.currentCapitalValue", "currCapitalValue.required");
        if (StringUtils.isBlank(property.getBasicProperty().getPropertyID().getNorthBoundary()))
            errors.rejectValue("basicProperty.propertyID.northBoundary", "northBoundary.required");
        if (StringUtils.isBlank(property.getBasicProperty().getPropertyID().getEastBoundary()))
            errors.rejectValue("basicProperty.propertyID.eastBoundary", "eastBoundary.required");
        if (StringUtils.isBlank(property.getBasicProperty().getPropertyID().getWestBoundary()))
            errors.rejectValue("basicProperty.propertyID.westBoundary", "westBoundary.required");
        if (StringUtils.isBlank(property.getBasicProperty().getPropertyID().getSouthBoundary()))
            errors.rejectValue("basicProperty.propertyID.southBoundary", "southBoundary.required");
        if (StringUtils.isBlank(property.getDemolitionReason()))
            errors.rejectValue("demolitionReason", "demolitionReason.required");
        validateAssignmentForCscUser(property, errors);
    }

    public void validateAssignmentForCscUser(final Property property, final BindingResult errors) {
        if (isNotEmployee() && property.getBasicProperty() != null) {
            final Assignment assignment = propertyService.isCscOperator(securityUtils.getCurrentUser())
                    ? propertyService.getAssignmentByDeptDesigElecWard(property.getBasicProperty())
                    : null;
            if (assignment == null && propertyService.getUserPositionByZone(property.getBasicProperty(), false) == null)
                errors.reject("notexists.position", "notexists.position");
        }
    }

    public void addModelAttributes(final Model model, final BasicProperty basicProperty) {
        Property property = null;
        if (null != basicProperty.getProperty())
            property = basicProperty.getProperty();
        else
            property = basicProperty.getActiveProperty();
        final Ptdemand ptDemand = ptDemandDAO.getNonHistoryCurrDmdForProperty(property);
        if (ptDemand != null && ptDemand.getDmdCalculations() != null && ptDemand.getDmdCalculations().getAlv() != null)
            model.addAttribute("ARV", ptDemand.getDmdCalculations().getAlv());
        else
            model.addAttribute("ARV", BigDecimal.ZERO);
        if (!basicProperty.getActiveProperty().getIsExemptedFromTax())
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
                LOGGER.error("Exception in addModelAttributes : ", e);
                throw new ApplicationRuntimeException("Exception in addModelAttributes : " + e);
            }
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
             * smsMsg = messageSource.getMessage("demolition.ack.sms", new String[] { applicantName, assessmentNo }, null);
             */
        } else if (workFlowAction.equals(WFLOW_ACTION_STEP_REJECT))
            smsMsg = ptisMessageSource.getMessage("demolition.rejection.sms", new String[] { applicantName, assessmentNo,
                    ApplicationThreadLocals.getMunicipalityName() }, null);
        else if (workFlowAction.equals(WFLOW_ACTION_STEP_APPROVE)) {
            Installment effectiveInstallment = null;
            final Map<String, Installment> yearwiseInstMap = propertyTaxUtil.getInstallmentsForCurrYear(new Date());
            final Installment installmentFirstHalf = yearwiseInstMap.get(CURRENTYEAR_FIRST_HALF);
            final Installment installmentSecondHalf = yearwiseInstMap.get(CURRENTYEAR_SECOND_HALF);
            Date effectiveDate;
            Map<String, BigDecimal> demandMap = null;
            BigDecimal totalTax;

            /*
             * If demolition is done in 1st half, then fetch the total tax amount for the 2nd half, else fetch the total tax for
             * next installment 1st half and display in the SMS.
             */
            if (DateUtils.between(new Date(), installmentFirstHalf.getFromDate(), installmentFirstHalf.getToDate()))
                effectiveInstallment = installmentSecondHalf;
            else {
                final Module module = moduleDao.getModuleByName(PTMODULENAME);
                effectiveDate = org.apache.commons.lang3.time.DateUtils.addDays(installmentSecondHalf.getToDate(), 1);
                effectiveInstallment = installmentDao.getInsatllmentByModuleForGivenDate(module, effectiveDate);
            }
            demandMap = propertyTaxUtil.getTaxDetailsForInstallment(property, effectiveInstallment, installmentFirstHalf);
            totalTax = demandMap.get(DEMANDRSN_STR_VACANT_TAX) == null ? BigDecimal.ZERO : demandMap.get(DEMANDRSN_STR_VACANT_TAX)
                    .add(demandMap.get(DEMANDRSN_STR_LIBRARY_CESS) == null ? BigDecimal.ZERO
                            : demandMap.get(DEMANDRSN_STR_LIBRARY_CESS));
            smsMsg = ptisMessageSource.getMessage("demolition.approval.sms", new String[] { applicantName, assessmentNo,
                    totalTax.toString(), new SimpleDateFormat("dd/MM/yyyy").format(effectiveInstallment.getFromDate()),
                    ApplicationThreadLocals.getMunicipalityName() },
                    null);
        }

        if (StringUtils.isNotBlank(mobileNumber))
            notificationService.sendSMS(mobileNumber, smsMsg);

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

    public String getLoggedInUserDesignation(final Long posId, final User user) {
        final List<Assignment> loggedInUserAssign = assignmentService.getAssignmentByPositionAndUserAsOnDate(
                posId, user.getId(), new Date());
        return !loggedInUserAssign.isEmpty() ? loggedInUserAssign.get(0).getDesignation().getName() : null;
    }

    public Assignment getWfInitiator(final PropertyImpl property) {
        return propertyService.getWorkflowInitiator(property);
    }
    
    public BasicProperty saveProperty(final Property oldProperty, final Property newProperty, final Character status,
            final String comments,
            final String workFlowAction, final Long approverPosition, final String additionalRule,
            final Map<String, String> meesevaParams) throws TaxCalculatorExeption {
         return saveProperty(oldProperty, newProperty, status, comments, workFlowAction,
                approverPosition, DEMOLITION);
    }

}
