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

package org.egov.adtax.workflow;

import static org.egov.adtax.utils.constants.AdvertisementTaxConstants.ANONYMOUS_USER;

import java.util.Date;

import org.egov.adtax.autonumber.AdvertisementPermitNumberGenerator;
import org.egov.adtax.entity.AdvertisementPermitDetail;
import org.egov.adtax.entity.enums.AdvertisementStatus;
import org.egov.adtax.service.AdvertisementDemandService;
import org.egov.adtax.service.AdvertisementService;
import org.egov.adtax.utils.constants.AdvertisementTaxConstants;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.EisCommonService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.pims.commons.Position;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class ApplicationCommonWorkflow.
 */
public abstract class AdtaxWorkflowCustomImpl implements AdtaxWorkflowCustom {

    private static final Logger LOG = LoggerFactory.getLogger(AdtaxWorkflowCustomImpl.class);

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<AdvertisementPermitDetail> advertisementPermitDetailWorkflowService;

    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;

    @Autowired
    private AutonumberServiceBeanResolver beanResolver;

    @Autowired
    private AdvertisementDemandService advertisementDemandService;

    @Autowired
    private AdvertisementWorkFlowService advertisementWorkFlowService;
    
    @Autowired
    private AdvertisementService advertisementService;
    
    @Autowired
    private EisCommonService eisCommonService;

    @Autowired
    public AdtaxWorkflowCustomImpl() {

    }

    @Override
    @Transactional
    public void createCommonWorkflowTransition(final AdvertisementPermitDetail advertisementPermitDetail,
            final Long approvalPosition, final String approvalComent, final String additionalRule,
            final String workFlowAction) {
        if (LOG.isDebugEnabled())
            LOG.debug(" Create WorkFlow Transition Started  ...");

        final User user = securityUtils.getCurrentUser();
        final DateTime currentDate = new DateTime();
        Assignment wfInitiator = advertisementWorkFlowService.getWorkFlowInitiator(advertisementPermitDetail);
        Position pos = getApprovalPosition(approvalPosition);
        Boolean cscOperatorLoggedIn = advertisementWorkFlowService.isCscOperator(user);
        if ((cscOperatorLoggedIn || ANONYMOUS_USER.equalsIgnoreCase(user.getName()))
                && advertisementPermitDetail.getState() == null)
            createByThirdParty(advertisementPermitDetail, approvalComent, additionalRule, user, wfInitiator, pos,
                    cscOperatorLoggedIn);
        else if (null == advertisementPermitDetail.getState())
            create(advertisementPermitDetail, approvalComent, additionalRule, user, wfInitiator, pos);
        else if (AdvertisementTaxConstants.WF_APPROVE_BUTTON.equalsIgnoreCase(workFlowAction))
            approve(advertisementPermitDetail, approvalComent, additionalRule, user, currentDate, wfInitiator);
        else if (AdvertisementTaxConstants.WF_REJECT_BUTTON.equalsIgnoreCase(workFlowAction) ||
                AdvertisementTaxConstants.WF_CANCELAPPLICATION_BUTTON.equalsIgnoreCase(workFlowAction) ||
                AdvertisementTaxConstants.WF_CANCELRENEWAL_BUTTON.equalsIgnoreCase(workFlowAction))
            rejectOrCancel(advertisementPermitDetail, approvalComent, additionalRule, workFlowAction, user,
                    currentDate, wfInitiator);
        else if (AdvertisementTaxConstants.WF_DEMANDNOTICE_BUTTON.equalsIgnoreCase(workFlowAction))
            updateOnDemandGeneration(advertisementPermitDetail, approvalComent, additionalRule, currentDate, wfInitiator);
        else if (AdvertisementTaxConstants.WF_PERMITORDER_BUTTON.equalsIgnoreCase(workFlowAction))
            updateOnPermitOrderGeneration(advertisementPermitDetail, approvalComent, additionalRule, currentDate, wfInitiator);
        else
            update(advertisementPermitDetail, approvalComent, additionalRule, user, currentDate, pos);
        if (LOG.isDebugEnabled())
            LOG.debug(" WorkFlow Transition Completed ");
    }

    private void update(final AdvertisementPermitDetail advertisementPermitDetail, final String approvalComent,
            final String additionalRule, final User user, final DateTime currentDate, Position pos) {
        WorkFlowMatrix wfmatrix = advertisementPermitDetailWorkflowService.getWfMatrix(advertisementPermitDetail.getStateType(), null,
                null, additionalRule, advertisementPermitDetail.getCurrentState().getValue(), null);
        if (!eisCommonService.isValidAppover(wfmatrix, pos)) {
            advertisementPermitDetail.setValidApprover(Boolean.FALSE);
            return;
        }
        advertisementPermitDetail.setStatus(getStatusByPassingModuleAndCode(wfmatrix));
        advertisementPermitDetail.transition().progressWithStateCopy()
                .withSenderName(user.getUsername() + AdvertisementTaxConstants.COLON_CONCATE + user.getName())
                .withComments(approvalComent)
                .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate()).withOwner(pos)
                .withNextAction(wfmatrix.getNextAction()).withNatureOfTask(AdvertisementTaxConstants.NATURE_OF_WORK);
    }

    private void createByThirdParty(final AdvertisementPermitDetail advertisementPermitDetail,
            final String approvalComent, final String additionalRule, final User user, Assignment wfInitiator, Position pos,
            Boolean cscOperatorLoggedIn) {
        String currentState = "Third Party operator created";
        WorkFlowMatrix wfmatrix = advertisementPermitDetailWorkflowService.getWfMatrix(advertisementPermitDetail.getStateType(),
                null,
                null, additionalRule, currentState, null);
        if (!eisCommonService.isValidAppover(wfmatrix, pos)) {
            advertisementPermitDetail.setValidApprover(Boolean.FALSE);
            return;
        }
        advertisementPermitDetail.setStatus(getStatusByPassingModuleAndCode(wfmatrix));
        setSource(advertisementPermitDetail, user, cscOperatorLoggedIn);
        advertisementPermitDetail.transition().start()
                .withSLA(advertisementService.calculateDueDate(advertisementPermitDetail))
                .withSenderName(user.getUsername() + AdvertisementTaxConstants.COLON_CONCATE + user.getName())
                .withComments(approvalComent).withInitiator(getWfInitiatorPosition(wfInitiator))
                .withStateValue(wfmatrix.getNextState()).withDateInfo(new Date()).withOwner(pos)
                .withNextAction(wfmatrix.getNextAction()).withNatureOfTask(AdvertisementTaxConstants.NATURE_OF_WORK);
    }

    private void create(final AdvertisementPermitDetail advertisementPermitDetail, final String approvalComent,
            final String additionalRule, final User user, Assignment wfInitiator, Position pos) {
        // go by status
        WorkFlowMatrix wfmatrix = advertisementPermitDetailWorkflowService.getWfMatrix(advertisementPermitDetail.getStateType(),
                null,
                null, additionalRule, AdvertisementTaxConstants.WF_NEW_STATE, null);
        if (!eisCommonService.isValidAppover(wfmatrix, pos)) {
            advertisementPermitDetail.setValidApprover(Boolean.FALSE);
            return;
        }
        advertisementPermitDetail.setStatus(getStatusByPassingModuleAndCode(wfmatrix));
        advertisementPermitDetail.transition().start()
                .withSLA(advertisementService.calculateDueDate(advertisementPermitDetail))
                .withSenderName(user.getUsername() + AdvertisementTaxConstants.COLON_CONCATE + user.getName())
                .withComments(approvalComent).withInitiator(getWfInitiatorPosition(wfInitiator))
                .withStateValue(wfmatrix.getNextState()).withDateInfo(new Date()).withOwner(pos)
                .withNextAction(wfmatrix.getNextAction()).withNatureOfTask(AdvertisementTaxConstants.NATURE_OF_WORK);
    }

    private void updateOnPermitOrderGeneration(final AdvertisementPermitDetail advertisementPermitDetail,
            final String approvalComent, final String additionalRule, final DateTime currentDate, Assignment wfInitiator) {
        WorkFlowMatrix wfmatrix = advertisementPermitDetailWorkflowService.getWfMatrix(advertisementPermitDetail.getStateType(), null,
                null, additionalRule, advertisementPermitDetail.getCurrentState().getValue(), null);
        advertisementPermitDetail.setStatus(getStatusByPassingModuleAndCode(wfmatrix));
        advertisementPermitDetail.getAdvertisement().setStatus(AdvertisementStatus.ACTIVE);
        if (wfmatrix.getNextAction().equalsIgnoreCase(AdvertisementTaxConstants.WF_END_STATE))
            advertisementPermitDetail.transition().end()
                    .withSenderName(getSenderName(wfInitiator))
                    .withComments(approvalComent).withDateInfo(currentDate.toDate())
                    .withNextAction(wfmatrix.getNextAction()).withNatureOfTask(AdvertisementTaxConstants.NATURE_OF_WORK);
    }

    private void updateOnDemandGeneration(final AdvertisementPermitDetail advertisementPermitDetail, final String approvalComent,
            final String additionalRule, final DateTime currentDate, Assignment wfInitiator) {
        WorkFlowMatrix wfmatrix = advertisementPermitDetailWorkflowService.getWfMatrix(advertisementPermitDetail.getStateType(), null,
                null, additionalRule, advertisementPermitDetail.getCurrentState().getValue(), null);
        advertisementPermitDetail.setStatus(getStatusByPassingModuleAndCode(wfmatrix));
        advertisementPermitDetail.transition().progressWithStateCopy()
                .withSenderName(getSenderName(wfInitiator))
                .withComments(approvalComent)
                .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate())
                .withOwner(getWfInitiatorPosition(wfInitiator))
                .withNextAction(wfmatrix.getNextAction()).withNatureOfTask(AdvertisementTaxConstants.NATURE_OF_WORK);
    }

    private void approve(final AdvertisementPermitDetail advertisementPermitDetail, final String approvalComent,
            final String additionalRule, final User user, final DateTime currentDate, Assignment wfInitiator) {
        WorkFlowMatrix wfmatrix = advertisementPermitDetailWorkflowService.getWfMatrix(advertisementPermitDetail.getStateType(),
                null,
                null, additionalRule, advertisementPermitDetail.getCurrentState().getValue(),
                advertisementPermitDetail.getState().getNextAction());
        advertisementPermitDetail.setStatus(egwStatusHibernateDAO
                .getStatusByModuleAndCode(AdvertisementTaxConstants.APPLICATION_MODULE_TYPE,
                        AdvertisementTaxConstants.APPLICATION_STATUS_APPROVED));
        advertisementPermitDetail.setIsActive(true);
        advertisementPermitDetail.getAdvertisement().setStatus(AdvertisementStatus.ACTIVE);

        // Adding previous record status as inactive.
        makePreviousApplicationInactive(advertisementPermitDetail, additionalRule);

        advertisementPermitDetail
                .setPermissionNumber(beanResolver.getAutoNumberServiceFor(AdvertisementPermitNumberGenerator.class)
                        .getNextAdvertisementPermitNumber(advertisementPermitDetail.getAdvertisement()));
        advertisementPermitDetail.transition().progressWithStateCopy()
                .withSenderName(new StringBuilder(user.getUsername()).append(AdvertisementTaxConstants.COLON_CONCATE)
                        .append(user.getName()).toString())
                .withComments(approvalComent)
                .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate())
                .withOwner(getWfInitiatorPosition(wfInitiator))
                .withNextAction(wfmatrix.getNextAction()).withNatureOfTask(AdvertisementTaxConstants.NATURE_OF_WORK);
    }

    private void rejectOrCancel(final AdvertisementPermitDetail advertisementPermitDetail, final String approvalComent,
            final String additionalRule, final String workFlowAction, final User user, final DateTime currentDate,
            Assignment wfInitiator) {
        WorkFlowMatrix wfmatrix;
        if (advertisementWorkFlowService.validateUserHasSamePositionAsInitiator(ApplicationThreadLocals.getUserId(),
                getWfInitiatorPosition(wfInitiator))) {

            advertisementPermitDetail.setStatus(
                    egwStatusHibernateDAO.getStatusByModuleAndCode(AdvertisementTaxConstants.APPLICATION_MODULE_TYPE,
                            AdvertisementTaxConstants.APPLICATION_STATUS_CANCELLED));
            advertisementPermitDetail.transition().end()
                    .withSenderName(user.getUsername() + AdvertisementTaxConstants.COLON_CONCATE + user.getName())
                    .withComments(approvalComent).withDateInfo(currentDate.toDate())
                    .withNextAction(AdvertisementTaxConstants.WF_END_STATE)
                    .withNatureOfTask(AdvertisementTaxConstants.NATURE_OF_WORK);

            if (isAdditionalRuleCreateAdvertisement(additionalRule))
                advertisementPermitDetail.getAdvertisement().setStatus(AdvertisementStatus.CANCELLED);
            else {
                advertisementPermitDetail.getAdvertisement().setStatus(AdvertisementStatus.ACTIVE); // for renewal

                /*
                 * Activate previous agreement. Update demand as per previous agreement.
                 */
                activatePreviousAgreementOnRenewalCancel(advertisementPermitDetail, additionalRule, workFlowAction);

            }
        } else {
            wfmatrix = advertisementPermitDetailWorkflowService.getWfMatrix(advertisementPermitDetail.getStateType(), null,
                    null, additionalRule, AdvertisementTaxConstants.WF_REJECT_STATE, null);
            advertisementPermitDetail.setStatus(getStatusByPassingModuleAndCode(wfmatrix));
            advertisementPermitDetail.transition().progressWithStateCopy()
                    .withSenderName(user.getUsername() + AdvertisementTaxConstants.COLON_CONCATE + user.getName())
                    .withComments(approvalComent)
                    .withStateValue(AdvertisementTaxConstants.WF_REJECT_STATE).withDateInfo(currentDate.toDate())
                    .withOwner(getWfInitiatorPosition(wfInitiator))
                    .withNextAction(wfmatrix.getNextAction())
                    .withNatureOfTask(AdvertisementTaxConstants.NATURE_OF_WORK);// Pending: WORK FLOW INITIATOR IS NULL THEN
                                                                                // RECORD WILL NOT SHOW IN ANY USER INBOX.
        }
    }

    private void activatePreviousAgreementOnRenewalCancel(final AdvertisementPermitDetail advertisementPermitDetail,
            final String additionalRule, final String workFlowAction) {
        if (AdvertisementTaxConstants.WF_CANCELRENEWAL_BUTTON.equalsIgnoreCase(workFlowAction) &&
                advertisementPermitDetail.getPreviousapplicationid() != null && additionalRule != null
                && additionalRule.equalsIgnoreCase(AdvertisementTaxConstants.RENEWAL_ADDITIONAL_RULE)) {

            advertisementPermitDetail.getPreviousapplicationid().setIsActive(true);
            advertisementPermitDetail.getPreviousapplicationid().setStatus(
                    egwStatusHibernateDAO.getStatusByModuleAndCode(
                            AdvertisementTaxConstants.APPLICATION_MODULE_TYPE,
                            AdvertisementTaxConstants.APPLICATION_STATUS_ADTAXPERMITGENERATED));

            advertisementPermitDetail.setIsActive(false);
            // UPDATE DEMAND BASED ON LATEST RENEWAL DATA.
            advertisementPermitDetail.getAdvertisement().setDemandId(
                    advertisementDemandService.updateDemandOnRenewal(advertisementPermitDetail
                            .getPreviousapplicationid(),
                            advertisementPermitDetail.getAdvertisement()
                                    .getDemandId()));
        }
    }

    private boolean isAdditionalRuleCreateAdvertisement(final String additionalRule) {
        return additionalRule != null && additionalRule.equalsIgnoreCase(AdvertisementTaxConstants.CREATE_ADDITIONAL_RULE);
    }

    private Position getWfInitiatorPosition(Assignment wfInitiator) {
        return wfInitiator != null ? wfInitiator.getPosition() : null;
    }

    private String getSenderName(Assignment wfInitiator) {
        return new StringBuilder(wfInitiator != null && wfInitiator.getEmployee() != null
                ? wfInitiator.getEmployee().getUsername()
                : "").append(AdvertisementTaxConstants.COLON_CONCATE)
                        .append(wfInitiator != null && wfInitiator.getEmployee() != null ? wfInitiator.getEmployee().getName()
                                : "")
                        .toString();
    }

    private void makePreviousApplicationInactive(final AdvertisementPermitDetail advertisementPermitDetail,
            final String additionalRule) {
        if (additionalRule != null && advertisementPermitDetail.getPreviousapplicationid() != null
                && additionalRule.equalsIgnoreCase(AdvertisementTaxConstants.RENEWAL_ADDITIONAL_RULE)) {
            advertisementPermitDetail.getPreviousapplicationid().setIsActive(false);
            // UPDATE DEMAND BASED ON LATEST RENEWAL DATA.
            advertisementPermitDetail.getAdvertisement().setDemandId(
                    advertisementDemandService.updateDemandOnRenewal(advertisementPermitDetail,
                            advertisementPermitDetail.getAdvertisement().getDemandId()));
        }
    }

    private void setSource(final AdvertisementPermitDetail advertisementPermitDetail, final User user,
            Boolean cscOperatorLoggedIn) {
        if (cscOperatorLoggedIn)
            advertisementPermitDetail.setSource(AdvertisementTaxConstants.CSC_SOURCE);
        else if (ANONYMOUS_USER.equalsIgnoreCase(user.getName()))
            advertisementPermitDetail.setSource(AdvertisementTaxConstants.ONLINE_SOURCE);
    }

    private Position getApprovalPosition(final Long approvalPosition) {
        if (approvalPosition != null && approvalPosition > 0)
            return positionMasterService.getPositionById(approvalPosition);
        return null;
    }

    private EgwStatus getStatusByPassingModuleAndCode(WorkFlowMatrix wfmatrix) {
        return egwStatusHibernateDAO
                .getStatusByModuleAndCode(AdvertisementTaxConstants.APPLICATION_MODULE_TYPE, wfmatrix.getNextStatus());
    }
}
