/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *     accountability and the service delivery of the government  organizations.
 *
 *      Copyright (C) <2015>  eGovernments Foundation
 *
 *      The updated version of eGov suite of products as by eGovernments Foundation
 *      is available at http://www.egovernments.org
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with this program. If not, see http://www.gnu.org/licenses/ or
 *      http://www.gnu.org/licenses/gpl.html .
 *
 *      In addition to the terms of the GPL license to be adhered to in using this
 *      program, the following additional terms are to be complied with:
 *
 *  	1) All versions of this program, verbatim or modified must carry this
 *  	   Legal Notice.
 *
 *  	2) Any misrepresentation of the origin of the material is prohibited. It
 *  	   is required that all modified versions of this material be marked in
 *  	   reasonable ways as different from the original version.
 *
 *  	3) This license does not grant any rights to any user of the program
 *  	   with regards to rights under trademark law for use of the trade names
 *  	   or trademarks of eGovernments Foundation.
 *
 *    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.tl.domain.service.objection;

import static org.egov.tl.utils.Constants.BUTTONAPPROVE;
import static org.egov.tl.utils.Constants.BUTTONREJECT;

import java.util.Date;

import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.EisCommonService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.Sequence;
import org.egov.infstr.utils.SequenceGenerator;
import org.egov.infstr.workflow.WorkFlowMatrix;
import org.egov.pims.commons.Position;
import org.egov.pims.commons.service.PositionService;
import org.egov.tl.domain.entity.License;
import org.egov.tl.domain.entity.LicenseStatus;
import org.egov.tl.domain.entity.WorkflowBean;
import org.egov.tl.domain.entity.objection.Activity;
import org.egov.tl.domain.entity.objection.LicenseObjection;
import org.egov.tl.domain.entity.objection.Notice;
import org.egov.tl.domain.service.BaseLicenseService;
import org.egov.tl.utils.Constants;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

public class ObjectionService extends PersistenceService<LicenseObjection, Long> {

    @Autowired
    private PositionMasterService positionMasterService;
    @Autowired
    private EisCommonService eisCommonService;
    protected SequenceGenerator sequenceGenerator;
    @Autowired
    protected BaseLicenseService licenseService;
    @Autowired
    private SimpleWorkflowService<LicenseObjection> objectionWorkflowService;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private PositionService positionService;

    public void setContextName(final String contextName) {
    }

    // set the licenseService in applicationcontext.xml
    // eg:TradeService
    @SuppressWarnings("unchecked")
    public LicenseObjection recordObjection(final LicenseObjection objection, final Long licenseId,
            final WorkflowBean workflowBean) {
        final String runningNumber = getNextRunningNumber(Constants.OBJECTIONNUMBERPREFIX);
        objection.generateNumber(runningNumber);
        final License license = (License) licenseService.getPersistenceService().find("from License where id=?", licenseId);
        objection.setLicense(license);
        persist(objection);
        initiateWorkflow(objection, workflowBean);
        return objection;
    }

    private void initiateWorkflow(final LicenseObjection objection, final WorkflowBean workflowBean) {
        final LicenseStatus objectedStatus = (LicenseStatus) licenseService.getPersistenceService().find(
                "from LicenseStatus where code='OBJ'");
        objection.getLicense().setStatus(objectedStatus);
        processWorkflow(objection, workflowBean);
    }

    private void processWorkflow(final LicenseObjection objection, final WorkflowBean workflowBean) {
        /*
         * if (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONAPPROVE)) { Position position =
         * positionMasterService.getCurrentPositionForUser(EgovThreadLocals.getUserId());
         * objection.changeState(Constants.WORKFLOW_STATE_TYPE_OBJECTLICENSE + Constants.WORKFLOW_STATE_APPROVED, position,
         * workflowBean.getComments()); objection.getCurrentState().setText2(contextName); position =
         * this.eisCommonsManager.getPositionByUserId(objection.getCreatedBy().getId());
         * objection.changeState(Constants.WORKFLOW_STATE_TYPE_OBJECTLICENSE + Constants.WORKFLOW_STATE_GENERATESUSPENSIONLETTER,
         * position, workflowBean.getComments()); } else if (objection.getState().getValue().equalsIgnoreCase(Constants.NEW) &&
         * workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONFORWARD) && ((objection.getActivities() != null &&
         * !objection.getActivities().isEmpty()) && ("Inspection").equalsIgnoreCase(objection.getActivities().get(0).getType())))
         * { final Position nextPosition = this.eisCommonsManager.getPositionByUserId(workflowBean.getApproverUserId());
         * objection.changeState(Constants.WORKFLOW_STATE_TYPE_OBJECTLICENSE + Constants.WORKFLOW_STATE_PIGENERATED, nextPosition,
         * workflowBean.getComments()); } else if (objection.getState().getValue().equalsIgnoreCase(Constants.NEW) &&
         * workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONFORWARD) && ((objection.getActivities() != null &&
         * !objection.getActivities().isEmpty()) && ("Response").equalsIgnoreCase(objection.getActivities().get(0).getType()))) {
         * final Position nextPosition = this.eisCommonsManager.getPositionByUserId(workflowBean.getApproverUserId());
         * objection.changeState(Constants.WORKFLOW_STATE_TYPE_OBJECTLICENSE + Constants.NEW, nextPosition,
         * workflowBean.getComments()); } else if (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONFORWARD)) { final
         * Position nextPosition = this.eisCommonsManager.getPositionByUserId(workflowBean.getApproverUserId());
         * objection.changeState(Constants.WORKFLOW_STATE_TYPE_OBJECTLICENSE + Constants.WORKFLOW_STATE_FORWARDED, nextPosition,
         * workflowBean.getComments()); } else if (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONREJECT)) { if
         * (objection.getState().getValue().contains(Constants.WORKFLOW_STATE_REJECTED)) { final Position position =
         * this.eisCommonsManager.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
         * objection.changeState(Constants.WORKFLOW_STATE_TYPE_OBJECTLICENSE +
         * Constants.WORKFLOW_STATE_GENERATECANCELLATIONLETTER, position, workflowBean.getComments()); } else { final Position
         * position = this.eisCommonsManager.getPositionByUserId(objection.getCreatedBy().getId());
         * objection.changeState(Constants.WORKFLOW_STATE_TYPE_OBJECTLICENSE + Constants.WORKFLOW_STATE_REJECTED, position,
         * workflowBean.getComments()); } } else if
         * (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONGENERATEDCERTIFICATE)) { final Position position =
         * this.eisCommonsManager.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
         * this.objectionWorkflowService.end(objection, position); final LicenseStatus activeStatus = (LicenseStatus)
         * this.licenseService.getPersistenceService().find("from LicenseStatus where code='SUS'");
         * objection.getLicense().setStatus(activeStatus); } else if
         * (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONGENERATEDREJECTIONLETTER)) { final Position position =
         * this.eisCommonsManager.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
         * this.objectionWorkflowService.end(objection, position); // identify the state for all objections final LicenseStatus
         * activeStatus = (LicenseStatus) this.licenseService.getPersistenceService().find("from LicenseStatus where code='REJ'");
         * objection.getLicense().setStatus(activeStatus); } else if
         * (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONGENERATEDPN)) { final Position position =
         * this.eisCommonsManager.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
         * objection.changeState(Constants.WORKFLOW_STATE_TYPE_OBJECTLICENSE + Constants.WORKFLOW_STATE_PNGENERATED, position,
         * workflowBean.getComments()); // identify the state for all objections final LicenseStatus activeStatus =
         * (LicenseStatus) this.licenseService.getPersistenceService().find("from LicenseStatus where code='ACT'");
         * objection.getLicense().setStatus(activeStatus); } else if
         * (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONGENERATEDSCN)) { final Position position =
         * this.eisCommonsManager.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
         * objection.changeState(Constants.WORKFLOW_STATE_TYPE_OBJECTLICENSE + Constants.WORKFLOW_STATE_SCNGENERATED, position,
         * workflowBean.getComments()); // identify the state for all objections final LicenseStatus activeStatus =
         * (LicenseStatus) this.licenseService.getPersistenceService().find("from LicenseStatus where code='ACT'");
         * objection.getLicense().setStatus(activeStatus); } objection.getCurrentState().setText2(contextName);
         */
        final DateTime currentDate = new DateTime();
        final User user = securityUtils.getCurrentUser();
        final Assignment userAssignment = assignmentService.getPrimaryAssignmentForUser(user.getId());
        Position pos = null;
        Assignment wfInitiator = null;

        if (null != objection.getId())
            wfInitiator = getWorkflowInitiator(objection);

        if (BUTTONREJECT.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {
            if (wfInitiator.equals(userAssignment)) {
                objection.transition(true).end().withSenderName(user.getName()).withComments(workflowBean.getApproverComments())
                        .withDateInfo(currentDate.toDate());
            } else {
                final String stateValue = objection.getCurrentState().getValue();
                objection.transition(true).withSenderName(user.getName()).withComments(workflowBean.getApproverComments())
                        .withStateValue(stateValue).withDateInfo(currentDate.toDate())
                        .withOwner(wfInitiator.getPosition()).withNextAction("Assistant Health Officer approval pending");
            }

        } else {
            if (null != workflowBean.getApproverPositionId() && workflowBean.getApproverPositionId() != -1)
                pos = positionService.find("from Position where id=?", workflowBean.getApproverPositionId());
            else if (BUTTONAPPROVE.equalsIgnoreCase(workflowBean.getWorkFlowAction()))
                pos = wfInitiator.getPosition();
            if (null == objection.getState()) {
                final WorkFlowMatrix wfmatrix = objectionWorkflowService.getWfMatrix(objection.getStateType(), null,
                        null, null, workflowBean.getCurrentState(), null);
                objection.transition().start().withSenderName(user.getName()).withComments(workflowBean.getApproverComments())
                        .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate()).withOwner(pos)
                        .withNextAction(wfmatrix.getNextAction());
            } else if (objection.getCurrentState().getNextAction().equalsIgnoreCase("END"))
                objection.transition(true).end().withSenderName(user.getName()).withComments(workflowBean.getApproverComments())
                        .withDateInfo(currentDate.toDate());
            else {
                final WorkFlowMatrix wfmatrix = objectionWorkflowService.getWfMatrix(objection.getStateType(), null,
                        null, null, objection.getCurrentState().getValue(), null);
                objection.transition(true).withSenderName(user.getName()).withComments(workflowBean.getApproverComments())
                        .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate()).withOwner(pos)
                        .withNextAction(wfmatrix.getNextAction());
            }
        }
    }
    
    protected Assignment getWorkflowInitiator(final LicenseObjection objection) {
        Assignment wfInitiator;
        if (!objection.getStateHistory().isEmpty())
            wfInitiator = assignmentService.getPrimaryAssignmentForPositon(objection.getStateHistory().get(0)
                    .getOwnerPosition().getId());
        else
            wfInitiator = assignmentService.getPrimaryAssignmentForPositon(objection.getState().getOwnerPosition()
                    .getId());
        return wfInitiator;
    }

    public BaseLicenseService getLicenseService() {
        return licenseService;
    }

    public void setLicenseService(final BaseLicenseService licenseService) {
        this.licenseService = licenseService;
    }

    public String getNextRunningNumber(final String type) {
        final Sequence seq = sequenceGenerator.getNextNumberWithFormat(type, Constants.APPLICATIONNO_LENGTH, new Character('0'));
        return seq.getFormattedNumber();
    }

    public void setSequenceGenerator(final SequenceGenerator sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }

    public LicenseObjection recordResponseOrInspection(LicenseObjection objection, final WorkflowBean workflowBean) {
        if (objection.getActivities().get(objection.getActivities().size() - 1).getType() != null
                || workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONAPPROVE)) {
            final Activity activity = objection.getActivities().get(objection.getActivities().size() - 1);
            objection = this.find("from org.egov.tl.domain.entity.objection.LicenseObjection where id=?", objection.getId());
            activity.setObjection(objection);
            objection.getActivities().add(activity);
            if (objection.getActivities().get(objection.getActivities().size() - 1).getType() != null &&
                    (objection.getActivities().get(objection.getActivities().size() - 1).getType().equals("PreNotice") ||
                            objection.getActivities().get(objection.getActivities().size() - 1).getType().equals("SCNotice") ||
                            objection.getActivities().get(objection.getActivities().size() - 1).getType().equals("suspend") ||
                    objection.getActivities().get(objection.getActivities().size() - 1).getType().equals("cancelled"))) {
                final Notice notice = getNotices(objection.getActivities().get(objection.getActivities().size() - 1).getType(),
                        objection);
                notice.setObjection(objection);
                objection.getNotices().add(notice);
            }
            persist(objection);
            processWorkflow(objection, workflowBean);
        } else {
            objection = this.find("from org.egov.tl.domain.entity.objection.LicenseObjection where id=?", objection.getId());
            processWorkflow(objection, workflowBean);
            /*
             * int userId = workflowBean.getApproverUserId(); if (userId == -1) {
             * if(objection.getCurrentState().getValue().equals(State.NEW)) { this.objectionWorkflowService.end(objection,
             * objection.getCurrentState().getOwner()); return objection; } } final Position nextPosition = userId == -1 ?
             * objection.getState().getPrevious().getOwner() : this.eisCommonsManager.getPositionByUserId(userId);
             * objection.changeState(Constants.WORKFLOW_STATE_TYPE_OBJECTLICENSE +
             * (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONREJECT) ? Constants.WORKFLOW_STATE_REJECTED :
             * Constants.WORKFLOW_STATE_FORWARDED), nextPosition, workflowBean.getComments());
             * objection.getCurrentState().setText2(contextName);
             */
        }
        return objection;
    }

    private Notice getNotices(final String noticeType, final LicenseObjection objection) {
        final Notice notice = new Notice();
        if (objection.getActivities().get(objection.getActivities().size() - 1).getType().equals(noticeType)) {
            final String runningNumber = getNextRunningNumber(Constants.OBJECTIONNOICENUMBERPREFIX);
            notice.generateNumber(runningNumber);
            // notice.setNoticeNumber(objection.getNumber());
            notice.setDocNumber(objection.getNumber() + "_" + noticeType);
            notice.setNoticeType(objection.getClass().getSimpleName() + "_" + noticeType);
            notice.setNoticeDate(new Date());
            notice.setModuleName("egtradelicense");
        }
        return notice;
    }
}
