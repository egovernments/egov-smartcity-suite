/*
 * @(#)ObjectionService.java 3.0, 29 Jul, 2013 1:24:28 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.license.domain.service.objection;

import java.util.Date;

import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.Sequence;
import org.egov.infstr.utils.SequenceGenerator;
import org.egov.infstr.workflow.SimpleWorkflowService;
import org.egov.license.domain.entity.License;
import org.egov.license.domain.entity.LicenseStatus;
import org.egov.license.domain.entity.WorkflowBean;
import org.egov.license.domain.entity.objection.Activity;
import org.egov.license.domain.entity.objection.LicenseObjection;
import org.egov.license.domain.entity.objection.Notice;
import org.egov.license.domain.service.BaseLicenseService;
import org.egov.license.utils.Constants;
import org.egov.pims.commons.Position;
import org.egov.pims.commons.service.EisCommonsService;

public class ObjectionService extends PersistenceService<LicenseObjection, Long> {
	protected EisCommonsService eisCommonsService;
	protected SequenceGenerator sequenceGenerator;
	protected BaseLicenseService licenseService;
	private SimpleWorkflowService<LicenseObjection> objectionWorkflowService;
	private String contextName;

	public void setContextName(final String contextName) {
		this.contextName = contextName;
	}

	// set the licenseService in applicationcontext.xml
	// eg:TradeService
	@SuppressWarnings("unchecked")
	public LicenseObjection recordObjection(final LicenseObjection objection, final Long licenseId, final WorkflowBean workflowBean) {
		// Objection objection=license.getObjections().get(0);
		final String runningNumber = this.getNextRunningNumber(Constants.OBJECTIONNUMBERPREFIX);
		objection.generateNumber(runningNumber);
		final License license = (License) this.licenseService.getPersistenceService().find("from License where id=?", licenseId);
		objection.setLicense(license);
		this.persist(objection);
		this.initiateWorkflow(objection, workflowBean);
		return objection;
	}

	private void initiateWorkflow(final LicenseObjection objection, final WorkflowBean workflowBean) {
		final Position position = this.eisCommonsService.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
		this.objectionWorkflowService.start(objection, position, workflowBean.getComments());
		final LicenseStatus objectedStatus = (LicenseStatus) this.licenseService.getPersistenceService().find("from LicenseStatus where code='OBJ'");
		objection.getLicense().setStatus(objectedStatus);
		this.processWorkflow(objection, workflowBean);
	}

	public SimpleWorkflowService<LicenseObjection> getObjectionWorkflowService() {
		return this.objectionWorkflowService;
	}

	public void setObjectionWorkflowService(final SimpleWorkflowService<LicenseObjection> objectionWorkflowService) {
		this.objectionWorkflowService = objectionWorkflowService;
	}

	private void processWorkflow(final LicenseObjection objection, final WorkflowBean workflowBean) {
		if (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONAPPROVE)) {
			Position position = this.eisCommonsService.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
			objection.changeState(Constants.WORKFLOW_STATE_TYPE_OBJECTLICENSE + Constants.WORKFLOW_STATE_APPROVED, position, workflowBean.getComments());
			objection.getCurrentState().setText2(this.contextName);
			position = this.eisCommonsService.getPositionByUserId(objection.getCreatedBy().getId());
			objection.changeState(Constants.WORKFLOW_STATE_TYPE_OBJECTLICENSE + Constants.WORKFLOW_STATE_GENERATESUSPENSIONLETTER, position, workflowBean.getComments());
		} else if (objection.getState().getValue().equalsIgnoreCase(Constants.NEW) && workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONFORWARD)
				&& ((objection.getActivities() != null && !objection.getActivities().isEmpty()) && ("Inspection").equalsIgnoreCase(objection.getActivities().get(0).getType()))) {
			final Position nextPosition = this.eisCommonsService.getPositionByUserId(workflowBean.getApproverUserId());
			objection.changeState(Constants.WORKFLOW_STATE_TYPE_OBJECTLICENSE + Constants.WORKFLOW_STATE_PIGENERATED, nextPosition, workflowBean.getComments());
		} else if (objection.getState().getValue().equalsIgnoreCase(Constants.NEW) && workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONFORWARD)
				&& ((objection.getActivities() != null && !objection.getActivities().isEmpty()) && ("Response").equalsIgnoreCase(objection.getActivities().get(0).getType()))) {
			final Position nextPosition = this.eisCommonsService.getPositionByUserId(workflowBean.getApproverUserId());
			objection.changeState(Constants.WORKFLOW_STATE_TYPE_OBJECTLICENSE + Constants.NEW, nextPosition, workflowBean.getComments());
		} else if (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONFORWARD)) {
			final Position nextPosition = this.eisCommonsService.getPositionByUserId(workflowBean.getApproverUserId());
			objection.changeState(Constants.WORKFLOW_STATE_TYPE_OBJECTLICENSE + Constants.WORKFLOW_STATE_FORWARDED, nextPosition, workflowBean.getComments());
		} else if (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONREJECT)) {
			if (objection.getState().getValue().contains(Constants.WORKFLOW_STATE_REJECTED)) {
				final Position position = this.eisCommonsService.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
				objection.changeState(Constants.WORKFLOW_STATE_TYPE_OBJECTLICENSE + Constants.WORKFLOW_STATE_GENERATECANCELLATIONLETTER, position, workflowBean.getComments());
			} else {
				final Position position = this.eisCommonsService.getPositionByUserId(objection.getCreatedBy().getId());
				objection.changeState(Constants.WORKFLOW_STATE_TYPE_OBJECTLICENSE + Constants.WORKFLOW_STATE_REJECTED, position, workflowBean.getComments());
			}
		} else if (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONGENERATEDCERTIFICATE)) {
			final Position position = this.eisCommonsService.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
			this.objectionWorkflowService.end(objection, position);
			final LicenseStatus activeStatus = (LicenseStatus) this.licenseService.getPersistenceService().find("from LicenseStatus where code='SUS'");
			objection.getLicense().setStatus(activeStatus);
		} else if (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONGENERATEDREJECTIONLETTER)) {
			final Position position = this.eisCommonsService.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
			this.objectionWorkflowService.end(objection, position);
			// identify the state for all objections
			final LicenseStatus activeStatus = (LicenseStatus) this.licenseService.getPersistenceService().find("from LicenseStatus where code='REJ'");
			objection.getLicense().setStatus(activeStatus);
		} else if (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONGENERATEDPN)) {
			final Position position = this.eisCommonsService.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
			objection.changeState(Constants.WORKFLOW_STATE_TYPE_OBJECTLICENSE + Constants.WORKFLOW_STATE_PNGENERATED, position, workflowBean.getComments());
			// identify the state for all objections
			final LicenseStatus activeStatus = (LicenseStatus) this.licenseService.getPersistenceService().find("from LicenseStatus where code='ACT'");
			objection.getLicense().setStatus(activeStatus);
		} else if (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONGENERATEDSCN)) {
			final Position position = this.eisCommonsService.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
			objection.changeState(Constants.WORKFLOW_STATE_TYPE_OBJECTLICENSE + Constants.WORKFLOW_STATE_SCNGENERATED, position, workflowBean.getComments());
			// identify the state for all objections
			final LicenseStatus activeStatus = (LicenseStatus) this.licenseService.getPersistenceService().find("from LicenseStatus where code='ACT'");
			objection.getLicense().setStatus(activeStatus);
		}
		objection.getCurrentState().setText2(this.contextName);
	}

	public BaseLicenseService getLicenseService() {
		return this.licenseService;
	}

	public void setLicenseService(final BaseLicenseService licenseService) {
		this.licenseService = licenseService;
	}

	public String getNextRunningNumber(final String type) {
		final Sequence seq = this.sequenceGenerator.getNextNumberWithFormat(type, Constants.APPLICATIONNO_LENGTH, new Character('0'));
		return seq.getFormattedNumber();
	}

	public void setEisCommonsService(final EisCommonsService eisCommonsService) {
		this.eisCommonsService = eisCommonsService;
	}

	public void setSequenceGenerator(final SequenceGenerator sequenceGenerator) {
		this.sequenceGenerator = sequenceGenerator;
	}

	public LicenseObjection recordResponseOrInspection(LicenseObjection objection, final WorkflowBean workflowBean) {
		if (objection.getActivities().get(objection.getActivities().size() - 1).getType() != null || workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONAPPROVE)) {
			final Activity activity = objection.getActivities().get(objection.getActivities().size() - 1);
			objection = this.find("from org.egov.license.domain.entity.objection.LicenseObjection where id=?", objection.getId());
			activity.setObjection(objection);
			objection.getActivities().add(activity);
			if (objection.getActivities().get(objection.getActivities().size() - 1).getType() != null
					&& (objection.getActivities().get(objection.getActivities().size() - 1).getType().equals("PreNotice") || objection.getActivities().get(objection.getActivities().size() - 1).getType().equals("SCNotice")
							|| objection.getActivities().get(objection.getActivities().size() - 1).getType().equals("suspend") || objection.getActivities().get(objection.getActivities().size() - 1).getType().equals("cancelled"))) {
				final Notice notice = getNotices(objection.getActivities().get(objection.getActivities().size() - 1).getType(), objection);
				notice.setObjection(objection);
				objection.getNotices().add(notice);
			}
			this.persist(objection);
			this.processWorkflow(objection, workflowBean);
		} else {
			objection = this.find("from org.egov.license.domain.entity.objection.LicenseObjection where id=?", objection.getId());
			this.processWorkflow(objection, workflowBean);
			/*
			 * int userId = workflowBean.getApproverUserId(); if (userId == -1) { if(objection.getCurrentState().getValue().equals(State.NEW)) { this.objectionWorkflowService.end(objection, objection.getCurrentState().getOwner()); return objection; } } final Position nextPosition = userId == -1 ?
			 * objection.getState().getPrevious().getOwner() : this.eisCommonsManager.getPositionByUserId(userId); objection.changeState(Constants.WORKFLOW_STATE_TYPE_OBJECTLICENSE + (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONREJECT) ? Constants.WORKFLOW_STATE_REJECTED :
			 * Constants.WORKFLOW_STATE_FORWARDED), nextPosition, workflowBean.getComments()); objection.getCurrentState().setText2(contextName);
			 */
		}
		return objection;
	}

	private Notice getNotices(final String noticeType, final LicenseObjection objection) {
		final Notice notice = new Notice();
		if (objection.getActivities().get(objection.getActivities().size() - 1).getType().equals(noticeType)) {
			final String runningNumber = this.getNextRunningNumber(Constants.OBJECTIONNOICENUMBERPREFIX);
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
