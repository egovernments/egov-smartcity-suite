/*
 * @(#)OutboundFileManagementAction.java 3.0, 16 Jul, 2013 11:35:54 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.web.actions.dms;

import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.dms.models.ExternalUser;
import org.egov.dms.models.FileAssignment;
import org.egov.dms.models.FileSource;
import org.egov.dms.models.GenericFile;
import org.egov.dms.models.InternalUser;
import org.egov.dms.models.OutboundFile;
import org.egov.dms.services.DMSConstants;
import org.egov.infstr.auditing.model.AuditEntity;
import org.egov.infstr.auditing.model.AuditEvent;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.models.State;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.workflow.SimpleWorkflowService;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.pims.commons.Position;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.PersonalInformation;

/**
 * The Class OutboundFileManagementAction. Sub class of {@link FileManagementAction}, All the
 * business logic to process an Outbound File will be handled at here.
 */
public class OutboundFileManagementAction extends FileManagementAction {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(OutboundFileManagementAction.class);
	private OutboundFile outboundFile = new OutboundFile();
	private transient PersistenceService<OutboundFile, Long> outboundFilePersistenceService;
	private transient SimpleWorkflowService<OutboundFile> outboundFileWorkflowService;
	/**
	 * Instantiates a new outbound file management action. Adds related entity to the OutboundFile
	 * model
	 */
	public OutboundFileManagementAction() {
		super();
		LOG.debug("Initializing OUTBOUND File Management Action");
		this.outboundFile.setReceiver(new ExternalUser());
		this.outboundFile.setSender(new InternalUser());
		this.addRelatedEntity("receiver.userSource", FileSource.class);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void prepare() {
		if (this.outboundFile.getId() != null) {
			this.outboundFile = (OutboundFile) this.outboundFilePersistenceService.findById(this.outboundFile.getId(), false);
			this.fileNumber = this.outboundFile.getFileNumber();
		}
		super.prepare();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getModel() {
		return this.outboundFile;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@SkipValidation
	public String execute() {
		this.setupBeforeCreateFile(this.outboundFile);
		return SUCCESS;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String createFile() throws Exception {
		super.createFile();
		LOG.debug("Start Creating OUTBOUND File for File# "+this.fileNumber);
		this.outboundFile.setFileNumber(this.fileNumber);
		this.outboundFile.setDocNumber(this.fileNumber);
		// Adds file assignment details
		final FileAssignment assignment = new FileAssignment();
		assignment.setUserType(DMSConstants.USER_EXTERNAL);
		assignment.setExternalUser(this.outboundFile.getReceiver());
		this.outboundFile.addFileAssignmentDetail(assignment);
		final Date currentDate = new Date();
		final Position senderPosition = this.eisService.getPrimaryPositionForUser(Integer.valueOf(EGOVThreadLocals.getUserId()), currentDate);
		if (senderPosition == null) {
			throw new EGOVRuntimeException(getText("LOGINUSER.NOPOS"));
		}
		final PersonalInformation senderInfo = this.employeeService.getEmpForPositionAndDate(currentDate, senderPosition.getId());
		this.outboundFile.getSender().setEmployeeInfo(senderInfo);
		final Assignment senderAssignment = senderInfo.getAssignment(currentDate);
		this.outboundFile.getSender().setDepartment((DepartmentImpl) senderAssignment.getDeptId());
		this.outboundFile.getSender().setDesignation(senderAssignment.getDesigId());
		this.outboundFile.getSender().setPosition(senderPosition);
		// Start and Transact OutboundFile workflow for FileStatus selected
		this.outboundFileWorkflowService.start(this.outboundFile, senderPosition);
		if (!this.saveAsDraft) {
			final State inboundFileState = new State(OutboundFile.class.getSimpleName(), this.outboundFile.getFileStatus().getDescription(), "INPROGRESS", senderPosition, "Outbound File Created with File Number : " + this.fileNumber);
			this.outboundFileWorkflowService.transition(this.outboundFile, inboundFileState);
		}
		this.outboundFile.setFileType("Outbound");
		doAuditing(AuditEntity.DMS_OUTBOUNDFILE,AuditEvent.CREATED);
		LOG.debug("OUTBOUND File successfully created for File# "+this.fileNumber);
		return "view";
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@SkipValidation
	public String viewFile() throws RuntimeException {
		super.viewFile();
		this.outboundFile=(OutboundFile)getFileService().getFileByNumber(GenericFile.OUTBOUND_BY_FILE_NUM ,this.fileNumber);
		this.outboundFile.setSysDate(this.outboundFile.getCreatedDate());
		return "view";
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@SkipValidation
	public String editFile() throws RuntimeException {
		super.editFile();
		this.outboundFile.setSysDate(this.outboundFile.getCreatedDate());
		return EDIT;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String saveFile() throws Exception {
		LOG.debug("Start Updating OUTBOUND File for File# "+this.outboundFile.getFileNumber());
		super.saveFile();		
		if (this.saveAsDraft) {
			this.outboundFilePersistenceService.persist(this.outboundFile);
			doAuditing(AuditEntity.DMS_OUTBOUNDFILE,AuditEvent.MODIFIED+" and Saved In Draft");
		} else {
			this.outboundFileWorkflowService.transition("FORWARD", this.outboundFile, "Forwarded with comment:" + this.getWorkflowComment());
			doAuditing(AuditEntity.DMS_OUTBOUNDFILE,AuditEvent.MODIFIED+" and Forwared");
		}
		LOG.debug("OUTBOUND File successfully updated for File# "+this.outboundFile.getFileNumber());
		return "view";
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String saveAsDraft() throws Exception {
		super.saveAsDraft();
		return (this.outboundFile.getId() == null) ? this.createFile() : this.saveFile();
	}
	
	@Override
	public void validate() {
		super.validate();
		if (!this.isDraftEdit && !this.actionName.equals("saveFile")) {
			if ((this.httpRequest.getParameter("receiver.userSource") == null) || this.httpRequest.getParameter("receiver.userSource").trim().intern().equals("")) {
				this.addFieldError("receiver.userSource", this.getText("receiver.userSource.required"));
			}
			if ((this.httpRequest.getParameter("receiver.userName") == null) || this.httpRequest.getParameter("receiver.userName").trim().intern().equals("")) {
				this.addFieldError("receiver.userName", this.getText("receiver.userName.required"));
			}
		}
	}

	public void setOutboundFilePersistenceService(
			PersistenceService<OutboundFile, Long> outboundFilepersistenceService) {
		this.outboundFilePersistenceService = outboundFilepersistenceService;
	}

	public void setOutboundFileWorkflowService(
			SimpleWorkflowService<OutboundFile> outboundFileWorkflowService) {
		this.outboundFileWorkflowService = outboundFileWorkflowService;
	}
}
