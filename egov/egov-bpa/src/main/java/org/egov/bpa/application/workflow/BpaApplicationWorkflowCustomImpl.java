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
package org.egov.bpa.application.workflow;

import java.util.Date;

import org.egov.bpa.application.entity.BpaApplication;
import org.egov.bpa.application.entity.BpaStatus;
import org.egov.bpa.service.BpaStatusService;
import org.egov.bpa.utils.BpaConstants;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.security.utils.SecurityUtils;
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
public abstract class BpaApplicationWorkflowCustomImpl implements BpaApplicationWorkflowCustom {
    private static final Logger LOG = LoggerFactory.getLogger(BpaApplicationWorkflowCustomImpl.class);


    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<BpaApplication> bpaApplicationWorkflowService;

    @Autowired
    private BpaStatusService bpaStatusService;

    @Autowired
    private BpaWorkFlowService bpaWorkFlowService;

    @Autowired
    public BpaApplicationWorkflowCustomImpl() {

    }
    
    @Override
    @Transactional
    public void createCommonWorkflowTransition(final BpaApplication application,
            final Long approvalPosition, final String approvalComent, final String additionalRule,
            final String workFlowAction) {

        if (LOG.isDebugEnabled())
            LOG.debug(" Create WorkFlow Transition Started  ...");
        final User user = securityUtils.getCurrentUser();
        final DateTime currentDate = new DateTime();
        Position pos = null;
        Assignment wfInitiator = null ;
        if(application.getCreatedBy()!=null)
        wfInitiator = bpaWorkFlowService.getWorkFlowInitiator(application);

        if (approvalPosition != null && approvalPosition > 0)
            pos = positionMasterService.getPositionById(approvalPosition);

        WorkFlowMatrix wfmatrix ;
        if (null == application.getState()) { // go by status
            wfmatrix = bpaApplicationWorkflowService.getWfMatrix(application.getStateType(), null,
                    null, additionalRule, BpaConstants.WF_NEW_STATE, null);

            if (wfmatrix != null) {
                //application.setStatus(getStatusByCurrentMatrxiStatus(wfmatrix));
                application.transition().start()
                        .withSenderName(user.getUsername() + BpaConstants.COLON_CONCATE + user.getName())
                        .withComments(approvalComent).withInitiator(wfInitiator != null ? wfInitiator.getPosition() : null)
                        .withStateValue(wfmatrix.getNextState()).withDateInfo(new Date()).withOwner(pos)
                        .withNextAction(wfmatrix.getNextAction()).withNatureOfTask(BpaConstants.NATURE_OF_WORK);
            }

        } else if (BpaConstants.WF_APPROVE_BUTTON.equalsIgnoreCase(workFlowAction)) {

            wfmatrix = bpaApplicationWorkflowService.getWfMatrix(application.getStateType(), null,
                    null, additionalRule, application.getCurrentState().getValue(), null);
            application.setStatus(getStatusByPassingCode(BpaConstants.APPLICATION_STATUS_APPROVED));

            application.transition().progressWithStateCopy()
                    .withSenderName(user.getUsername() + BpaConstants.COLON_CONCATE + user.getName())
                    .withComments(approvalComent)
                    .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate())
                    .withOwner(wfInitiator != null ? wfInitiator.getPosition() : null)
                    .withNextAction(wfmatrix.getNextAction()).withNatureOfTask(BpaConstants.NATURE_OF_WORK);
        } else if (BpaConstants.WF_REJECT_BUTTON.equalsIgnoreCase(workFlowAction) ||
                BpaConstants.WF_CANCELAPPLICATION_BUTTON.equalsIgnoreCase(workFlowAction)) {

            if (bpaWorkFlowService.validateUserHasSamePositionAsInitiator(ApplicationThreadLocals.getUserId(),
                    wfInitiator != null ? wfInitiator.getPosition() : null)) {

                application.setStatus(getStatusByPassingCode(BpaConstants.APPLICATION_STATUS_CANCELLED));
                application.transition().end()
                        .withSenderName(user.getUsername() + BpaConstants.COLON_CONCATE + user.getName())
                        .withComments(approvalComent).withDateInfo(currentDate.toDate())
                        .withNextAction(BpaConstants.WF_END_STATE).withNatureOfTask(BpaConstants.NATURE_OF_WORK);

                if (additionalRule != null && additionalRule.equalsIgnoreCase(BpaConstants.CREATE_ADDITIONAL_RULE_CREATE))
                    application.setStatus(getStatusByPassingCode(BpaConstants.APPLICATION_STATUS_CANCELLED));

            } else {
                // IN case of rejection, we need to search "superindent" who forwarded record first time and assign.-- Pending
                // work
                wfmatrix = bpaApplicationWorkflowService.getWfMatrix(application.getStateType(), null,
                        null, additionalRule, BpaConstants.WF_REJECT_STATE, null);
                application.setStatus(getStatusByCurrentMatrxiStatus(wfmatrix));
                application.transition().progressWithStateCopy()
                        .withSenderName(user.getUsername() + BpaConstants.COLON_CONCATE + user.getName())
                        .withComments(approvalComent)
                        .withStateValue(BpaConstants.WF_REJECT_STATE).withDateInfo(currentDate.toDate())
                        .withOwner(wfInitiator != null ? wfInitiator.getPosition() : null)
                        .withNextAction(wfmatrix.getNextAction())
                        .withNatureOfTask(BpaConstants.NATURE_OF_WORK);// TODO: WORK FLOW INITIATOR IS NULL THEN RECORD WILL NOT
                                                                       // SHOW IN ANY USER INBOX.
            }

        } else {
            wfmatrix = bpaApplicationWorkflowService.getWfMatrix(application.getStateType(), null,
                    null, additionalRule, application.getCurrentState().getValue(), null);

            if (wfmatrix != null) {
                application.setStatus(getStatusByCurrentMatrxiStatus(wfmatrix));

                if (wfmatrix.getNextAction().equalsIgnoreCase(BpaConstants.WF_END_STATE)) {
                    application.transition().end().withSenderName((wfInitiator != null && wfInitiator.getEmployee() != null
                            ? wfInitiator.getEmployee().getUsername() : "") + BpaConstants.COLON_CONCATE
                            + (wfInitiator != null && wfInitiator.getEmployee() != null
                                    ? wfInitiator.getEmployee().getName()
                                    : ""))
                            .withComments(approvalComent).withDateInfo(currentDate.toDate())
                            .withNextAction(wfmatrix.getNextAction()).withNatureOfTask(BpaConstants.NATURE_OF_WORK);
                } else {
                    application.transition().progressWithStateCopy()
                            .withSenderName(user.getUsername() + BpaConstants.COLON_CONCATE + user.getName())
                            .withComments(approvalComent)
                            .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate()).withOwner(pos)
                            .withNextAction(wfmatrix.getNextAction()).withNatureOfTask(BpaConstants.NATURE_OF_WORK);
                }
            }
        }
        if (LOG.isDebugEnabled())
            LOG.debug(" WorkFlow Transition Completed ");
    }

    private BpaStatus getStatusByCurrentMatrxiStatus(WorkFlowMatrix wfmatrix) {
        if (wfmatrix != null && wfmatrix.getNextStatus() != null && !"".equals(wfmatrix.getNextStatus()))
            return bpaStatusService
                    .findByModuleTypeAndCode(BpaConstants.BPASTATUS_MODULETYPE, wfmatrix.getNextStatus());
        return null;
    }

    private BpaStatus getStatusByPassingCode(String code) {
        if (code != null && !"".equals(code))
            return bpaStatusService
                    .findByModuleTypeAndCode(BpaConstants.BPASTATUS_MODULETYPE, code);
        return null;
    }

}
