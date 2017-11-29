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

package org.egov.council.service.workflow;

import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.council.entity.CouncilPreamble;
import org.egov.council.utils.constants.CouncilConstants;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional(readOnly = true)
public class PreambleWorkflowCustomImpl implements PreambleWorkflowCustom {
    private static final Logger LOG = LoggerFactory.getLogger(PreambleWorkflowCustomImpl.class);

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;

    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<CouncilPreamble> councilPreambleWorkflowService;

    @Autowired
    private UserService userService;

    @Override
    public void createCommonWorkflowTransition(CouncilPreamble councilPreamble, Long approvalPosition,
            String approvalComent, String workFlowAction) {
        if (LOG.isDebugEnabled())
            LOG.debug(" Create WorkFlow Transition Started  ...");

        final User user = securityUtils.getCurrentUser();
        final DateTime currentDate = new DateTime();
        User currentUser = null;
        Position pos = null;
        Assignment wfInitiator = null;
        String currState = "";
        WorkFlowMatrix wfmatrix = null;

        if (null != councilPreamble.getId()) {
            currentUser = userService.getUserById(councilPreamble.getCreatedBy().getId());
            wfInitiator = assignmentService.getPrimaryAssignmentForUser(councilPreamble.getCreatedBy().getId());

        }

        if (null != approvalPosition && approvalPosition != -1 && !approvalPosition.equals(Long.valueOf(0)))
            pos = positionMasterService.getPositionById(approvalPosition);
        else
            pos = wfInitiator != null ? wfInitiator.getPosition() : null;

        // New Entry
        if (null == councilPreamble.getState()) {

            wfmatrix = councilPreambleWorkflowService.getWfMatrix(councilPreamble.getStateType(), null, null, null,
                    CouncilConstants.WF_NEW_STATE, null);
            councilPreamble.setStatus(getStatusByPassingModuleAndCode(wfmatrix));
            councilPreamble.transition().start()
                    .withSenderName(user.getUsername() + CouncilConstants.COLON_CONCATE + user.getName())
                    .withComments(approvalComent).withStateValue(wfmatrix.getNextState()).withDateInfo(new Date())
                    .withOwner(pos).withNextAction(wfmatrix.getNextAction())
                    .withNatureOfTask(CouncilConstants.NATURE_OF_WORK);

        } // End workflow on execute connection
		else if (CouncilConstants.WF_STATE_REJECT
				.equalsIgnoreCase(workFlowAction)) {
			councilPreamble.setStatus(getStatusByPassingStatusCode("REJECTED"));
			councilPreamble.transition().end()
					.withSenderName(user.getUsername() + CouncilConstants.COLON_CONCATE + user.getName())
					.withComments(approvalComent)
					.withDateInfo(currentDate.toDate())
					.withNatureOfTask(CouncilConstants.NATURE_OF_WORK);
		
		} else if (CouncilConstants.WF_APPROVE_BUTTON.equalsIgnoreCase(workFlowAction)) {
            wfmatrix = councilPreambleWorkflowService.getWfMatrix(councilPreamble.getStateType(), null, null, null,
                    councilPreamble.getCurrentState().getValue(), null);
            councilPreamble.setStatus(getStatusByPassingModuleAndCode(wfmatrix));
            if (wfmatrix.getNextAction().equalsIgnoreCase("END")) {
                councilPreamble.transition().end()
                        .withSenderName(user.getUsername() + CouncilConstants.COLON_CONCATE + user.getName())
                        .withComments(approvalComent).withDateInfo(currentDate.toDate())
                        .withNatureOfTask(CouncilConstants.NATURE_OF_WORK);
            } else {
                councilPreamble.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(wfmatrix.getNextState())
                        .withDateInfo(currentDate.toDate()).withOwner(pos).withNextAction(wfmatrix.getNextAction())
                        .withNatureOfTask(CouncilConstants.NATURE_OF_WORK);
            }
        } else if (CouncilConstants.WF_PROVIDE_INFO_BUTTON.equalsIgnoreCase(workFlowAction)) { 
        	if (ApplicationThreadLocals.getUserId().equals(
                wfInitiator != null && wfInitiator.getEmployee() != null ? wfInitiator.getEmployee().getId() : 0)) {
            councilPreamble.setStatus(getStatusByPassingStatusCode("REJECTED"));
            councilPreamble.transition().end()
                    .withSenderName(user.getUsername() + CouncilConstants.COLON_CONCATE + user.getName())
                    .withComments(approvalComent).withDateInfo(currentDate.toDate())
                    .withNatureOfTask(CouncilConstants.NATURE_OF_WORK);
        } else {
            wfmatrix = councilPreambleWorkflowService.getWfMatrix(councilPreamble.getStateType(), null, null, null,
                    CouncilConstants.WF_REJECT_STATE, null);
            councilPreamble.transition().progressWithStateCopy()
                    .withSenderName(user.getUsername() + CouncilConstants.COLON_CONCATE + user.getName())
                    .withComments(approvalComent).withStateValue(CouncilConstants.WF_REJECT_STATE)
                    .withDateInfo(currentDate.toDate())
                    .withOwner(wfInitiator != null ? wfInitiator.getPosition() : null)
                    .withNextAction(wfmatrix.getNextAction()).withNatureOfTask(CouncilConstants.NATURE_OF_WORK);
        }
        	}      
        else {

            wfmatrix = councilPreambleWorkflowService.getWfMatrix(councilPreamble.getStateType(), null, null, null,
                    councilPreamble.getCurrentState().getValue(), null);

            councilPreamble.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                    .withComments(approvalComent).withStateValue(wfmatrix.getNextState())
                    .withDateInfo(currentDate.toDate()).withOwner(pos).withNextAction(wfmatrix.getNextAction())
                    .withNatureOfTask(CouncilConstants.NATURE_OF_WORK);
        }
        if (LOG.isDebugEnabled())
            LOG.debug(" WorkFlow Transition Completed  ...");
    }

    private EgwStatus getStatusByPassingModuleAndCode(WorkFlowMatrix wfmatrix) {
        return egwStatusHibernateDAO.getStatusByModuleAndCode(CouncilConstants.PREAMBLE_MODULE_TYPE,
                wfmatrix.getNextStatus());
    }

    private EgwStatus getStatusByPassingStatusCode(String statusCode) {
        return egwStatusHibernateDAO.getStatusByModuleAndCode(CouncilConstants.PREAMBLE_MODULE_TYPE, statusCode);
    }

}
