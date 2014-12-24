/*
 * @(#)InternalFileManagementAction.java 3.0, 16 Jul, 2013 11:35:41 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.web.actions.dms;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.dms.models.FileAssignment;
import org.egov.dms.models.GenericFile;
import org.egov.dms.models.InternalFile;
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
import org.egov.pims.model.EmployeeView;
import org.egov.pims.model.PersonalInformation;

/**
 * The Class InternalFileManagementAction. Sub class of {@link FileManagementAction}, All the
 * business logic to process an Internal File will be handled at here.
 */
public class InternalFileManagementAction extends FileManagementAction {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(InternalFileManagementAction.class);
	private InternalFile internalFile = new InternalFile();	
	private transient List<DesignationMaster> senderDesignations = new ArrayList<DesignationMaster>();
	private transient List<EmployeeView> senderEmpInfoList = new ArrayList<EmployeeView>();
	private transient SimpleWorkflowService<InternalFile> internalFileWorkflowService;
	private transient PersistenceService<InternalFile, Long> internalFilePersistenceService;
	/**
	 * Instantiates a new internal file management action. Adds related entity specific to
	 * InternalFile model
	 */
	public InternalFileManagementAction() {
		super();
		LOG.debug("Initializing INTERNAL File Management Action");
		this.internalFile.setReceiver(new InternalUser());
		this.internalFile.setSender(new InternalUser());
		this.addRelatedEntity("receiver.department", DepartmentImpl.class);
		this.addRelatedEntity("receiver.position", Position.class);
		this.addRelatedEntity("sender.department", DepartmentImpl.class);
		this.addRelatedEntity("sender.position", Position.class);
		this.addRelatedEntity("department", DepartmentImpl.class);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void prepare() {
		if (this.internalFile.getId() != null) {
			this.internalFile = (InternalFile) this.internalFilePersistenceService.findById(this.internalFile.getId(), false);
			this.fileNumber = this.internalFile.getFileNumber();
		}
		super.prepare();
		// assign the logged in user to sender
		if(this.internalFile.getId() == null) {
			PersonalInformation loggedInEmp = employeeService.getEmpForUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
			Assignment currAssignment = loggedInEmp.getAssignment(DateUtils.today());
			this.internalFile.getSender().setDepartment(this.internalFile.getSender().getDepartment() == null ? (DepartmentImpl) currAssignment.getDeptId():this.internalFile.getSender().getDepartment());
			this.internalFile.getSender().setPosition(this.internalFile.getSender().getPosition() == null ? (Position) currAssignment.getPosition():this.internalFile.getSender().getPosition());
			this.internalFile.getSender().setDesignation(this.internalFile.getSender().getDesignation()==null ? currAssignment.getDesigId():this.internalFile.getSender().getDesignation());
			
		}
		if (this.internalFile.getSender().getDepartment() != null) {
			this.senderDesignations = this.eisService.getAllDesignationByDept(this.internalFile.getSender().getDepartment().getId(), new Date());
			// load users
			if (this.internalFile.getSender().getDesignation() != null) {
				final HashMap<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("departmentId", this.internalFile.getSender().getDepartment().getId().toString());
				paramMap.put("designationId", this.internalFile.getSender().getDesignation().getDesignationId().toString());
				this.senderEmpInfoList = new ArrayList<EmployeeView>();
				this.senderEmpInfoList.addAll((List<EmployeeView>) this.eisService.getEmployeeInfoList(paramMap));
			}
		}
		// load designation
		if (this.internalFile.getReceiver().getDepartment() != null) {
			this.designations = this.eisService.getAllDesignationByDept(this.internalFile.getReceiver().getDepartment().getId(), new Date());
			// load users
			if (this.internalFile.getReceiver().getDesignation() != null) {
				this.setEmpInfoList(this.internalFile.getReceiver().getDepartment().getId().toString(), this.internalFile.getReceiver().getDesignation().getDesignationId().toString());
			}
		}
		this.setRelationShip("receiver.designation", DesignationMaster.class, "designationId");
		this.setRelationShip("sender.designation", DesignationMaster.class, "designationId");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getModel() {
		return this.internalFile;
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
	@SkipValidation
	public String execute() {
		this.setupBeforeCreateFile(this.internalFile);
		return SUCCESS;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String createFile() throws Exception {
		super.createFile();
		LOG.debug("Start Creating INTERNAL File for File#"+this.fileNumber);
		this.internalFile.setFileNumber(this.fileNumber);
		this.internalFile.setDocNumber(this.fileNumber);
		// Add file assignment details
		final FileAssignment assignment = new FileAssignment();
		assignment.setUserType(DMSConstants.USER_INTERNAL);
		assignment.setInternalUser(this.internalFile.getReceiver());
		this.internalFile.addFileAssignmentDetail(assignment);
		final Position receiverPosition = this.internalFile.getReceiver().getPosition();
		final Position senderPosition = this.internalFile.getSender().getPosition();
		final Date currentDate = new Date();
		this.internalFile.getReceiver().setEmployeeInfo(this.employeeService.getEmpForPositionAndDate(currentDate, receiverPosition.getId()));
		this.internalFile.getSender().setEmployeeInfo(this.employeeService.getEmpForPositionAndDate(currentDate, senderPosition.getId()));
		// Starts and Transact Internal File Workflow to selected FileStatus
		final Position loginUserPos = this.eisService.getPrimaryPositionForUser(Integer.valueOf(EGOVThreadLocals.getUserId()), currentDate);
		if (loginUserPos == null) {
			throw new EGOVRuntimeException(getText("LOGINUSER.NOPOS"));
		}
		this.internalFileWorkflowService.start(this.internalFile, loginUserPos);
		if (!this.saveAsDraft) {			
			final State internalFileState = new State(InternalFile.class.getSimpleName(), this.internalFile.getFileStatus().getDescription(), "INPROGRESS", receiverPosition, "Internal File Created with File Number : " + this.fileNumber);
			this.internalFileWorkflowService.transition(this.internalFile, internalFileState);
		} 
		this.internalFile.setFileType("Internal");
		doAuditing(AuditEntity.DMS_INTERNALFILE,AuditEvent.CREATED);
		LOG.debug("INTERNAL File successfully created for File# "+this.fileNumber);
		return "view";
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@SkipValidation
	public String viewFile() throws RuntimeException {
		super.viewFile();
		this.internalFile=(InternalFile)getFileService().getFileByNumber(GenericFile.INTERNAL_BY_FILE_NUM,this.fileNumber);
		this.internalFile.setSysDate(this.internalFile.getCreatedDate());
		return "view";
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@SkipValidation
	public String editFile() throws RuntimeException {
		super.editFile();
		this.internalFile.setSysDate(this.internalFile.getCreatedDate());
		return EDIT;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String saveFile() throws Exception {
		LOG.debug("Start Updating INTERNAL File for File# "+this.internalFile.getFileNumber());
		super.saveFile();		
		if (this.saveAsDraft) {
			this.internalFilePersistenceService.persist(this.internalFile);
			doAuditing(AuditEntity.DMS_INTERNALFILE,AuditEvent.MODIFIED+" and Saved in Draft");
		} else {
			this.internalFileWorkflowService.transition("FORWARD", this.internalFile, "Forwarded with comment:" + this.getWorkflowComment());
			doAuditing(AuditEntity.DMS_INTERNALFILE,AuditEvent.MODIFIED+" and Forwarded");
		}
		LOG.debug("INTERNAL File successfully updated for File# "+this.internalFile.getFileNumber());
		return "view";
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String saveAsDraft() throws Exception {
		super.saveAsDraft();
		return (this.internalFile.getId() == null) ? this.createFile() : this.saveFile();
	}
	
	@Override
	public void validate() {
		super.validate();
		if (!this.isDraftEdit && !this.actionName.equals("saveFile")) {
			if ((this.httpRequest.getParameter("receiver.department") == null) || this.httpRequest.getParameter("receiver.department").trim().intern().equals("")) {
				this.addFieldError("receiver.department", this.getText("receiver.department.required"));
			}
			if ((this.httpRequest.getParameter("receiver.designation") == null) || this.httpRequest.getParameter("receiver.designation").equals("") || this.httpRequest.getParameter("receiver.designation").equals("-1")) {
				this.addFieldError("receiver.designation", this.getText("receiver.designation.required"));
			}
			if ((this.httpRequest.getParameter("receiver.position") == null) || this.httpRequest.getParameter("receiver.position").equals("") || this.httpRequest.getParameter("receiver.position").equals("-1")) {
				this.addFieldError("receiver.position", this.getText("receiver.position.required"));
			}
			if ((this.httpRequest.getParameter("sender.department") == null) || this.httpRequest.getParameter("sender.department").equals("")) {
				this.addFieldError("sender.department", this.getText("sender.department.required"));
			}
			if ((this.httpRequest.getParameter("sender.designation") == null) || this.httpRequest.getParameter("sender.designation").equals("") || this.httpRequest.getParameter("sender.designation").equals("-1")) {
				this.addFieldError("sender.designation", this.getText("sender.designation.required"));
			}
			if ((this.httpRequest.getParameter("sender.position") == null) || this.httpRequest.getParameter("sender.position").trim().equals("") || this.httpRequest.getParameter("sender.position").equals("-1")) {
				this.addFieldError("sender.position", this.getText("sender.position.required"));
			}
		}
	}
	public List<DesignationMaster> getSenderDesignationList() {
		return this.senderDesignations;
	}
	public List<EmployeeView> getSenderEmpInfoList() {
		return this.senderEmpInfoList;
	}

	public void setInternalFileWorkflowService(
			SimpleWorkflowService<InternalFile> internalFileWorkflowService) {
		this.internalFileWorkflowService = internalFileWorkflowService;
	}

	public void setInternalFilePersistenceService(
			PersistenceService<InternalFile, Long> internalFilePersistenceService) {
		this.internalFilePersistenceService = internalFilePersistenceService;
	}
}

