/*
 * @(#)InboundFileManagementAction.java 3.0, 16 Jul, 2013 11:35:34 AM
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
import org.egov.dms.models.InboundFile;
import org.egov.dms.models.InternalUser;
import org.egov.dms.services.DMSConstants;
import org.egov.infstr.auditing.model.AuditEntity;
import org.egov.infstr.auditing.model.AuditEvent;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.models.State;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.workflow.SimpleWorkflowService;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.commons.Position;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.PersonalInformation;

/**
 * The Class InboundFileManagementAction. Sub class of {@link FileManagementAction}, All the
 * business logic to process an Inbound File will be handled at here.
 */
public class InboundFileManagementAction extends FileManagementAction {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(InboundFileManagementAction.class);
	private InboundFile inboundFile = new InboundFile();
	private transient SimpleWorkflowService<InboundFile> inboundFileWorkflowService;
	private transient PersistenceService<InboundFile, Long> inboundFilePersistenceService;
	
	/**
	 * Instantiates a new inbound file management action. Adds related entity to InboundFile model.
	 */
	public InboundFileManagementAction() {
		super();
		LOG.debug("Initializing INBOUND File Management Action");
		this.inboundFile.setReceiver(new InternalUser());
		this.inboundFile.setSender(new ExternalUser());
		this.addRelatedEntity("receiver.department", DepartmentImpl.class);
		this.addRelatedEntity("receiver.position", Position.class);
		this.addRelatedEntity("sender.userSource", FileSource.class);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void prepare() {
		if (this.inboundFile.getId() != null) {
			this.inboundFile = (InboundFile) this.inboundFilePersistenceService.findById(this.inboundFile.getId(), false);
			this.fileNumber = this.inboundFile.getFileNumber();
		}
		super.prepare();
		// assign the logged in user to receiver
		if(this.inboundFile.getId() == null) {
			PersonalInformation loggedInEmp = employeeService.getEmpForUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
			if(loggedInEmp==null) {
				this.addActionError(getText("err.notanemp"));
			}
			Assignment currAssignment = loggedInEmp.getAssignment(DateUtils.today());
			this.inboundFile.getReceiver().setDepartment(this.inboundFile.getReceiver().getDepartment() == null ? (DepartmentImpl) currAssignment.getDeptId():this.inboundFile.getReceiver().getDepartment());
			this.inboundFile.getReceiver().setPosition(this.inboundFile.getReceiver().getPosition() == null ? (Position) currAssignment.getPosition():this.inboundFile.getReceiver().getPosition());
			this.inboundFile.getReceiver().setDesignation(this.inboundFile.getReceiver().getDesignation()==null ? currAssignment.getDesigId():this.inboundFile.getReceiver().getDesignation());
			
		}
		// load designation
		if (this.inboundFile.getReceiver().getDepartment() != null) {
			this.designations = this.eisService.getAllDesignationByDept(this.inboundFile.getReceiver().getDepartment().getId(), new Date());
			// load users
			if (this.inboundFile.getReceiver().getDesignation() != null) {
				this.setEmpInfoList(this.inboundFile.getReceiver().getDepartment().getId().toString(), this.inboundFile.getReceiver().getDesignation().getDesignationId().toString());
			}
		}
		this.setRelationShip("receiver.designation", DesignationMaster.class, "designationId");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getModel() {
		return this.inboundFile;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@SkipValidation
	public String execute() {
		this.setupBeforeCreateFile(this.inboundFile);
		return SUCCESS;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean acceptableParameterName(final String paramName) {
		return super.acceptableParameterName(paramName);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String createFile() throws Exception {
		super.createFile();
		LOG.debug("Start Creating INBOUND File for File # "+this.fileNumber);
		this.inboundFile.setFileNumber(this.fileNumber);
		this.inboundFile.setDocNumber(this.fileNumber);
		// Add file assignment details
		final FileAssignment assignment = new FileAssignment();
		assignment.setUserType(DMSConstants.USER_INTERNAL);
		assignment.setInternalUser(this.inboundFile.getReceiver());
		this.inboundFile.addFileAssignmentDetail(assignment);
		final Position receiverPosition = this.inboundFile.getReceiver().getPosition();
		final Date currentDate = new Date();
		this.inboundFile.getReceiver().setEmployeeInfo(this.employeeService.getEmpForPositionAndDate(currentDate, receiverPosition.getId()));
		// Creating and starting Workflow for Inbound File
		final Position loginUserPos = this.eisService.getPrimaryPositionForUser(Integer.valueOf(EGOVThreadLocals.getUserId()), currentDate);
		if (loginUserPos == null) {
			throw new EGOVRuntimeException(getText("LOGINUSER.NOPOS"));
		}
		this.inboundFileWorkflowService.start(this.inboundFile, loginUserPos);		
		if (!this.saveAsDraft) {
			final State inboundFileState = new State(InboundFile.class.getSimpleName(), this.inboundFile.getFileStatus().getDescription(), "INPROGRESS", receiverPosition, "Inbound File Created with File Number : " + this.fileNumber);
			this.inboundFileWorkflowService.transition(this.inboundFile, inboundFileState);
		}
		this.inboundFile.setFileType("Inbound");
		doAuditing(AuditEntity.DMS_INBOUNDFILE,AuditEvent.CREATED);
		LOG.debug("INBOUND File successfully created for File # "+this.fileNumber);
		return "view";
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@SkipValidation
	public String editFile() throws RuntimeException {
		super.editFile();
		this.inboundFile.setSysDate(this.inboundFile.getCreatedDate());
		return EDIT;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String saveFile() throws Exception {
		LOG.debug("Start Updating INBOUND File for File #"+this.inboundFile.getFileNumber());
		super.saveFile();		
		// Handles forwarding the File Workflow
		if (this.saveAsDraft) {
			this.inboundFilePersistenceService.persist(this.inboundFile);
			doAuditing(AuditEntity.DMS_INBOUNDFILE,AuditEvent.MODIFIED+" and Saved in Draft");
		} else {
			this.inboundFileWorkflowService.transition("FORWARD", this.inboundFile, "Forwarded with comment:" + this.getWorkflowComment());
			doAuditing(AuditEntity.DMS_INBOUNDFILE,AuditEvent.MODIFIED+" and Forwarded");
		}		
		LOG.debug("INBOUND File successfully updated for File #"+this.inboundFile.getFileNumber());
		return "view";
	}
	
	@Override
	public String saveAsDraft() throws Exception {
		super.saveAsDraft();
		return (this.inboundFile.getId() == null) ? this.createFile() : this.saveFile();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@SkipValidation
	public String viewFile() throws RuntimeException {
		super.viewFile();
		this.inboundFile=(InboundFile)getFileService().getFileByNumber(GenericFile.INBOUND_BY_FILE_NUM,this.fileNumber);
		this.inboundFile.setSysDate(this.inboundFile.getCreatedDate());
		return "view";
	}
	
	@Override
	public void validate() {
		super.validate();
		if (!this.isDraftEdit && !this.actionName.equals("saveFile")) {
			if ((this.httpRequest.getParameter("receiver.department") == null) || this.httpRequest.getParameter("receiver.department").equals("")) {
				this.addFieldError("receiver.department", this.getText("receiver.department.required"));
			}
			if ((this.httpRequest.getParameter("receiver.designation") == null) || this.httpRequest.getParameter("receiver.designation").equals("") || this.httpRequest.getParameter("receiver.designation").equals("-1")) {
				this.addFieldError("receiver.designation", this.getText("receiver.designation.required"));
			}
			if ((this.httpRequest.getParameter("receiver.position") == null) || this.httpRequest.getParameter("receiver.position").equals("") || this.httpRequest.getParameter("receiver.position").equals("-1")) {
				this.addFieldError("receiver.position", this.getText("receiver.position.required"));
			}
			if ((this.httpRequest.getParameter("sender.userSource") == null) || this.httpRequest.getParameter("sender.userSource").trim().intern().equals("")) {
				this.addFieldError("sender.userSource", this.getText("sender.userSource.required"));
			}
			if ((this.httpRequest.getParameter("sender.userName") == null) || this.httpRequest.getParameter("sender.userName").trim().intern().equals("")) {
				this.addFieldError("sender.userName", this.getText("sender.userName.required"));
			}
		}
	}

	public void setInboundFileWorkflowService(
			SimpleWorkflowService<InboundFile> inboundFileWorkflowService) {
		this.inboundFileWorkflowService = inboundFileWorkflowService;
	}

	public void setInboundFilePersistenceService(
			PersistenceService<InboundFile, Long> inboundFilePersistenceService) {
		this.inboundFilePersistenceService = inboundFilePersistenceService;
	}
}

