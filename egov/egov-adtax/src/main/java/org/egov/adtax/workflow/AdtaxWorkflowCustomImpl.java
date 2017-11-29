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
import org.egov.adtax.utils.constants.AdvertisementTaxConstants;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.eis.entity.Assignment;
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
        Position pos = null;
        String currentState = "";
       
        Assignment wfInitiator = advertisementWorkFlowService.getWorkFlowInitiator(advertisementPermitDetail);

        if (approvalPosition != null && approvalPosition > 0)
            pos = positionMasterService.getPositionById(approvalPosition);
        Boolean cscOperatorLoggedIn = advertisementWorkFlowService.isCscOperator(user);
        WorkFlowMatrix wfmatrix = null;
        if (cscOperatorLoggedIn || ANONYMOUS_USER.equalsIgnoreCase(user.getName())) {
            currentState = "Third Party operator created";
            wfmatrix = advertisementPermitDetailWorkflowService.getWfMatrix(advertisementPermitDetail.getStateType(), null,
                    null, additionalRule, currentState, null);
            advertisementPermitDetail.setStatus(getStatusByPassingModuleAndCode(wfmatrix));
            if (cscOperatorLoggedIn)
                advertisementPermitDetail.setSource(AdvertisementTaxConstants.CSC_SOURCE);
            else if(ANONYMOUS_USER.equalsIgnoreCase(user.getName()))
                advertisementPermitDetail.setSource(AdvertisementTaxConstants.ONLINE_SOURCE);
            
            advertisementPermitDetail.transition().start()
                    .withSenderName(user.getUsername() + AdvertisementTaxConstants.COLON_CONCATE + user.getName())
                    .withComments(approvalComent).withInitiator(wfInitiator != null ? wfInitiator.getPosition() : null)
                    .withStateValue(wfmatrix.getNextState()).withDateInfo(new Date()).withOwner(pos)
                    .withNextAction(wfmatrix.getNextAction()).withNatureOfTask(AdvertisementTaxConstants.NATURE_OF_WORK);

        } else if (null == advertisementPermitDetail.getState()) { // go by status
            wfmatrix = advertisementPermitDetailWorkflowService.getWfMatrix(advertisementPermitDetail.getStateType(), null,
                    null, additionalRule, AdvertisementTaxConstants.WF_NEW_STATE, null);
            advertisementPermitDetail.setStatus(getStatusByPassingModuleAndCode(wfmatrix));
            advertisementPermitDetail.transition().start()
                    .withSenderName(user.getUsername() + AdvertisementTaxConstants.COLON_CONCATE + user.getName())
                    .withComments(approvalComent).withInitiator(wfInitiator != null ? wfInitiator.getPosition() : null)
                    .withStateValue(wfmatrix.getNextState()).withDateInfo(new Date()).withOwner(pos)
                    .withNextAction(wfmatrix.getNextAction()).withNatureOfTask(AdvertisementTaxConstants.NATURE_OF_WORK);

        } else if (AdvertisementTaxConstants.WF_APPROVE_BUTTON.equalsIgnoreCase(workFlowAction)) {

            wfmatrix = advertisementPermitDetailWorkflowService.getWfMatrix(advertisementPermitDetail.getStateType(), null,
                    null, additionalRule, advertisementPermitDetail.getCurrentState().getValue(), advertisementPermitDetail.getState().getNextAction());
            advertisementPermitDetail.setStatus(egwStatusHibernateDAO
                    .getStatusByModuleAndCode(AdvertisementTaxConstants.APPLICATION_MODULE_TYPE,
                            AdvertisementTaxConstants.APPLICATION_STATUS_APPROVED));
            advertisementPermitDetail.setIsActive(true);
            advertisementPermitDetail.getAdvertisement().setStatus(AdvertisementStatus.ACTIVE);

            // Adding previous record status as inactive.
            if (additionalRule != null && advertisementPermitDetail.getPreviousapplicationid() != null
                    && additionalRule.equalsIgnoreCase(AdvertisementTaxConstants.RENEWAL_ADDITIONAL_RULE)) {
                advertisementPermitDetail.getPreviousapplicationid().setIsActive(false);
                // UPDATE DEMAND BASED ON LATEST RENEWAL DATA.
                advertisementPermitDetail.getAdvertisement().setDemandId(
                        advertisementDemandService.updateDemandOnRenewal(advertisementPermitDetail,
                                advertisementPermitDetail.getAdvertisement().getDemandId()));
            }

            advertisementPermitDetail
                    .setPermissionNumber((beanResolver.getAutoNumberServiceFor(AdvertisementPermitNumberGenerator.class))
                            .getNextAdvertisementPermitNumber(advertisementPermitDetail.getAdvertisement()));
            advertisementPermitDetail.transition().progressWithStateCopy()
                    .withSenderName(user.getUsername() + AdvertisementTaxConstants.COLON_CONCATE + user.getName())
                    .withComments(approvalComent)
                    .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate())
                    .withOwner(wfInitiator != null ? wfInitiator.getPosition() : null)
                    .withNextAction(wfmatrix.getNextAction()).withNatureOfTask(AdvertisementTaxConstants.NATURE_OF_WORK);
        } else if (AdvertisementTaxConstants.WF_REJECT_BUTTON.equalsIgnoreCase(workFlowAction) ||
                AdvertisementTaxConstants.WF_CANCELAPPLICATION_BUTTON.equalsIgnoreCase(workFlowAction) ||
                AdvertisementTaxConstants.WF_CANCELRENEWAL_BUTTON.equalsIgnoreCase(workFlowAction)) {

            // In case of rejection of renewal record, do not change status of advertisement . We need to change previous record
            // as active.
            // if (ApplicationThreadLocals.getUserId().equals(wfInitiator!=null && wfInitiator.getEmployee()!=null
            // ?wfInitiator.getEmployee().getId():0 )) {
            if (advertisementWorkFlowService.validateUserHasSamePositionAsInitiator(ApplicationThreadLocals.getUserId(),
                    (wfInitiator != null ? wfInitiator.getPosition() : null))) {

                advertisementPermitDetail.setStatus(
                        egwStatusHibernateDAO.getStatusByModuleAndCode(AdvertisementTaxConstants.APPLICATION_MODULE_TYPE,
                                AdvertisementTaxConstants.APPLICATION_STATUS_CANCELLED));
                advertisementPermitDetail.transition().end()
                        .withSenderName(user.getUsername() + AdvertisementTaxConstants.COLON_CONCATE + user.getName())
                        .withComments(approvalComent).withDateInfo(currentDate.toDate())
                        .withNextAction(AdvertisementTaxConstants.WF_END_STATE)
                        .withNatureOfTask(AdvertisementTaxConstants.NATURE_OF_WORK);

                if (additionalRule != null && additionalRule.equalsIgnoreCase(AdvertisementTaxConstants.CREATE_ADDITIONAL_RULE))
                    advertisementPermitDetail.getAdvertisement().setStatus(AdvertisementStatus.CANCELLED);
                else {
                    advertisementPermitDetail.getAdvertisement().setStatus(AdvertisementStatus.ACTIVE); // for renewal

                    /*
                     * Activate previous agreement. Update demand as per previous agreement.
                     */
                    if (AdvertisementTaxConstants.WF_CANCELRENEWAL_BUTTON.equalsIgnoreCase(workFlowAction) &&
                            additionalRule != null && advertisementPermitDetail.getPreviousapplicationid() != null
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
                                        .getPreviousapplicationid(), advertisementPermitDetail.getAdvertisement()
                                                .getDemandId()));
                    }

                }
            } else {
                wfmatrix = advertisementPermitDetailWorkflowService.getWfMatrix(advertisementPermitDetail.getStateType(), null,
                        null, additionalRule, AdvertisementTaxConstants.WF_REJECT_STATE, null);
                advertisementPermitDetail.setStatus(getStatusByPassingModuleAndCode(wfmatrix));
                advertisementPermitDetail.transition().progressWithStateCopy()
                        .withSenderName(user.getUsername() + AdvertisementTaxConstants.COLON_CONCATE + user.getName())
                        .withComments(approvalComent)
                        .withStateValue(AdvertisementTaxConstants.WF_REJECT_STATE).withDateInfo(currentDate.toDate())
                        .withOwner(wfInitiator != null ? wfInitiator.getPosition() : null)
                        .withNextAction(wfmatrix.getNextAction())
                        .withNatureOfTask(AdvertisementTaxConstants.NATURE_OF_WORK);// Pending: WORK FLOW INITIATOR IS NULL THEN
                                                                                    // RECORD WILL NOT SHOW IN ANY USER INBOX.
            }

        } else if (AdvertisementTaxConstants.WF_DEMANDNOTICE_BUTTON.equalsIgnoreCase(workFlowAction)) {
            wfmatrix = advertisementPermitDetailWorkflowService.getWfMatrix(advertisementPermitDetail.getStateType(), null,
                    null, additionalRule, advertisementPermitDetail.getCurrentState().getValue(), null);
            advertisementPermitDetail.setStatus(getStatusByPassingModuleAndCode(wfmatrix));
            advertisementPermitDetail.transition().progressWithStateCopy()
                    .withSenderName((wfInitiator != null && wfInitiator.getEmployee() != null
                            ? wfInitiator.getEmployee().getUsername() : "") + AdvertisementTaxConstants.COLON_CONCATE
                            + (wfInitiator != null && wfInitiator.getEmployee() != null ? wfInitiator.getEmployee().getName()
                                    : ""))
                    .withComments(approvalComent)
                    .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate())
                    .withOwner(wfInitiator != null ? wfInitiator.getPosition() : null)
                    .withNextAction(wfmatrix.getNextAction()).withNatureOfTask(AdvertisementTaxConstants.NATURE_OF_WORK);
        } else if (AdvertisementTaxConstants.WF_PERMITORDER_BUTTON.equalsIgnoreCase(workFlowAction)) {
            wfmatrix = advertisementPermitDetailWorkflowService.getWfMatrix(advertisementPermitDetail.getStateType(), null,
                    null, additionalRule, advertisementPermitDetail.getCurrentState().getValue(), null);
            advertisementPermitDetail.setStatus(getStatusByPassingModuleAndCode(wfmatrix));
            advertisementPermitDetail.getAdvertisement().setStatus(AdvertisementStatus.ACTIVE);
            if (wfmatrix.getNextAction().equalsIgnoreCase(AdvertisementTaxConstants.WF_END_STATE))
                advertisementPermitDetail.transition().end()
                        .withSenderName((wfInitiator != null && wfInitiator.getEmployee() != null
                                ? wfInitiator.getEmployee().getUsername() : "") + AdvertisementTaxConstants.COLON_CONCATE
                                + (wfInitiator != null && wfInitiator.getEmployee() != null ? wfInitiator.getEmployee().getName()
                                        : ""))
                        .withComments(approvalComent).withDateInfo(currentDate.toDate())
                        .withNextAction(wfmatrix.getNextAction()).withNatureOfTask(AdvertisementTaxConstants.NATURE_OF_WORK);
        } else {
            wfmatrix = advertisementPermitDetailWorkflowService.getWfMatrix(advertisementPermitDetail.getStateType(), null,
                    null, additionalRule, advertisementPermitDetail.getCurrentState().getValue(), null);
            advertisementPermitDetail.setStatus(getStatusByPassingModuleAndCode(wfmatrix));
            advertisementPermitDetail.transition().progressWithStateCopy()
                    .withSenderName(user.getUsername() + AdvertisementTaxConstants.COLON_CONCATE + user.getName())
                    .withComments(approvalComent)
                    .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate()).withOwner(pos)
                    .withNextAction(wfmatrix.getNextAction()).withNatureOfTask(AdvertisementTaxConstants.NATURE_OF_WORK);

        }
        if (LOG.isDebugEnabled())
            LOG.debug(" WorkFlow Transition Completed ");
    }

    private EgwStatus getStatusByPassingModuleAndCode(WorkFlowMatrix wfmatrix) {
        return egwStatusHibernateDAO
                .getStatusByModuleAndCode(AdvertisementTaxConstants.APPLICATION_MODULE_TYPE, wfmatrix.getNextStatus());
    }
}
