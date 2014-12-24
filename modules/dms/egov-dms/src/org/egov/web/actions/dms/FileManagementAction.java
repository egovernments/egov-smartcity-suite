/*
 * @(#)FileManagementAction.java 3.0, 16 Jul, 2013 11:33:49 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.web.actions.dms;

import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.egov.infstr.utils.DateUtils.getFormattedDate;
import static org.egov.infstr.utils.StringUtils.escapeSpecialChars;
import static org.egov.infstr.utils.StringUtils.escapeJavaScript;
import static org.egov.infstr.utils.StringUtils.hasSpecialChars;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.EgwStatus;
import org.egov.dms.models.ExternalUser;
import org.egov.dms.models.FileAssignment;
import org.egov.dms.models.FileCategory;
import org.egov.dms.models.FilePriority;
import org.egov.dms.models.FileSource;
import org.egov.dms.models.GenericFile;
import org.egov.dms.models.InboundFile;
import org.egov.dms.models.InternalFile;
import org.egov.dms.models.InternalUser;
import org.egov.dms.models.OutboundFile;
import org.egov.dms.services.DMSConstants;
import org.egov.dms.services.FileManagementService;
import org.egov.dms.services.FileService;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.exceptions.NoSuchObjectException;
import org.egov.exceptions.TooManyValuesException;
import org.egov.infstr.auditing.model.AuditEntity;
import org.egov.infstr.auditing.model.AuditEvent;
import org.egov.infstr.auditing.model.AuditModule;
import org.egov.infstr.auditing.service.AuditEventService;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.docmgmt.AssociatedFile;
import org.egov.infstr.docmgmt.DocumentManagerService;
import org.egov.infstr.docmgmt.DocumentObject;
import org.egov.infstr.models.State;
import org.egov.infstr.services.EISServeable;
import org.egov.infstr.utils.DateUtils;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.admbndry.BoundaryType;
import org.egov.lib.admbndry.BoundaryTypeDAO;
import org.egov.lib.admbndry.HeirarchyType;
import org.egov.lib.admbndry.HeirarchyTypeDAO;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.commons.Position;
import org.egov.pims.model.EmployeeView;
import org.egov.pims.service.EmployeeService;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPage;

import com.opensymphony.xwork2.ActionContext;

/**
 * The Class FileManagementAction.
 */
@ParentPackage("egov")
@Result(name="auditReport",type="redirect",location="auditReport",params={"moduleName","DMS","namespace","/egi/auditing","method","searchForm","actionName","auditReport","prependServletContext","false"})
public class FileManagementAction extends BaseFormAction {
	
	private static final long serialVersionUID = 1L;
	private transient List<String> comment = new ArrayList<String>();
	private transient final Map<String, Object> ajaxReqParam = new HashMap<String, Object>();
	protected transient FileManagementService fileMgmtService;
	protected transient BoundaryDAO boundaryDAO;
	protected transient BoundaryTypeDAO boundaryTypeDAO;
	protected transient HeirarchyTypeDAO heirarchyTypeDAO;
	protected transient EISServeable eisService;
	protected transient DocumentManagerService<DocumentObject> docMngrService;
	protected transient EmployeeService employeeService;
	protected transient ExternalUser externalUser = new ExternalUser();
	protected transient InternalUser internalUser = new InternalUser();
	protected transient String forwardUserType = DMSConstants.USER_INTERNAL;
	protected transient String fileNumber;
	protected transient String fileName;
	protected transient String outboundFileNumber;
	protected transient List<String> fileComment = new ArrayList<String>();
	protected transient List<String> fileNames = new ArrayList<String>();
	private transient List<Boundary> secndLvlBndryList = new ArrayList<Boundary>();
	private transient List<FileCategory> fileSubCategories = new ArrayList<FileCategory>();
	protected transient List<DesignationMaster> designations = new ArrayList<DesignationMaster>();
	private transient List<EmployeeView> empInfoList = new ArrayList<EmployeeView>();
	private transient String workflowComment = "";
	protected transient String actionName;
	protected transient HttpServletRequest httpRequest;
	protected transient boolean saveAsDraft;
	protected transient boolean isDraftEdit;
	private FileService fileService;
	private transient AuditEventService auditEventService;
	
	/**
	 * Instantiates a new file management action. Attaches all the related entity which is common to
	 * subclasses
	 */
	public FileManagementAction() {
		super();
		this.addRelatedEntity("department", DepartmentImpl.class);
		this.addRelatedEntity("firstLevelBndry", BoundaryImpl.class);
		this.addRelatedEntity("secondLevelBndry", BoundaryImpl.class);
		this.addRelatedEntity("fileCategory", FileCategory.class);
		this.addRelatedEntity("fileSubcategory", FileCategory.class);
		this.addRelatedEntity("fileStatus", EgwStatus.class);
		this.addRelatedEntity("fileSource", FileSource.class);
		this.addRelatedEntity("filePriority", FilePriority.class);
	}
	
	/**
	 * Prepares all the drop downs and other entities and attaches with the model
	 */
	@Override
	public void prepare() {
		super.prepare();
		this.setNonModelRelationShip("externalUser.userSource", FileSource.class);
		this.setNonModelRelationShip("internalUser.position", Position.class);
		this.setNonModelRelationShip("internalUser.department", DepartmentImpl.class);
		this.setNonModelRelationShip("internalUser.designation", DesignationMaster.class, "designationId");
		this.setupDropdownDataExcluding("fileCategory", "fileSubCategory", "fileStatus", "firstLevelBndry", "secondLevelBndry", "internalUser.position", "internalUser.designation", "internalUser.department");
		this.addDropdownData("fileCategoryList", this.persistenceService.findAllBy("from org.egov.dms.models.FileCategory where parent is null"));
		this.addDropdownData("fileStatusList", this.persistenceService.findAllBy("from org.egov.commons.EgwStatus where moduletype=?", "DMS_FILESTATUS"));
		final GenericFile genericFile = (GenericFile) this.getModel();
		// load fileSubcategory
		if (genericFile.getFileCategory() != null) {
			this.setFileSubCategories(genericFile.getFileCategory().getId());
		}
		// load secondlevelbndry
		try {
			if (genericFile.getFirstLevelBndry() != null) {
				this.setSecondLevelBndryList(String.valueOf(genericFile.getFirstLevelBndry().getId()));
			}
		} catch (final Exception e) {
			LOG.error("Unable to load Boundary", e);
			this.addActionError(getText("err.unabletoloadbndry"));
		}
	}
	
	/**
	 * Returns empty GenericFile, Subclass have to override it.
	 */
	public Object getModel() {
		return new GenericFile();
	}
	
	@SkipValidation
	public String execute() {
		return SUCCESS;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.egov.web.actions.BaseFormAction#acceptableParameterName(java.lang.String)
	 */
	@Override
	public boolean acceptableParameterName(final String paramName) {
		final List<String> nonAcceptable = Arrays.asList(new String[] { "receiver.designation", "sender.designation", "internalUser.position", "internalUser.designation", "internalUser.department", "externalUser.userSource" });
		final boolean retValue = super.acceptableParameterName(paramName);
		return retValue ? !nonAcceptable.contains(paramName) : retValue;
	}
	
	/**
	 * Sets relationship which doesn't have an id field or field name is different
	 * @param relationshipName
	 * @param relClass
	 * @param relId
	 */
	protected void setNonModelRelationShip(final String relationshipName, final Class relClass, final String... relId) {
		if ((relId != null) && (relId.length > 0)) {
			this.setRelationShip(relationshipName, relClass, relId[0], false);
		} else {
			this.setRelationShip(relationshipName, relClass, "id", false);
		}
	}
	
	/**
	 * Sets the relation ship.
	 * @param relationshipName
	 * @param relClass
	 * @param relId
	 */
	protected void setRelationShip(final String relationshipName, final Class relClass, final String relId) {
		this.setRelationShip(relationshipName, relClass, relId, true);
	}
	
	/**
	 * Sets the relation ship.
	 * @param relationshipName
	 * @param relClass
	 * @param relId
	 * @param isModel
	 */
	private void setRelationShip(final String relationshipName, final Class relClass, final String relId, final boolean isModel) {
		final String[] ids = this.parameters.get(relationshipName);
		Object relation = null;
		try {
			if ((ids != null) && (ids.length > 0)) {
				final String identifier = ids[0];
				if ((identifier != null) && (identifier.length() > 0)) {
					if (relClass.getMethod("get" + relId.substring(0, 1).toUpperCase() + relId.substring(1)).getReturnType().getSimpleName().equals("Long")) {
						relation = this.getPersistenceService().find("from " + relClass.getName() + " where " + relId + "=?", Long.parseLong(identifier));
					} else {
						relation = this.getPersistenceService().find("from " + relClass.getName() + " where " + relId + "=?", Integer.parseInt(identifier));
					}
					if (isModel) {
						this.setValue(relationshipName, relation);
					} else {
						ActionContext.getContext().getValueStack().setValue(relationshipName, relation);
					}
				}
			}
		} catch (final NoSuchMethodException e) {
			throw new EGOVRuntimeException("Method get" + relId.substring(0, 1).toUpperCase() + relId.substring(1) + " not found", e);
		}
	}
	
	/**
	 * Sets the up before create file.
	 * @param genericFile the new up before create file
	 */
	protected void setupBeforeCreateFile(final GenericFile genericFile) {
		// Checks File Number has to be generated automatically
		if (this.fileMgmtService.isAutoGenerateFileNumber()) {
			// Then set the File Number value defined in the package.properties - key
			// DMS.AutoFileNo.Msg
			genericFile.setFileNumber(this.getText("DMS.AutoFileNo.Msg"));
		}
	}
	
	/**
	 * Creates the file. Called while submitting create file action form.
	 * @return the string
	 * @throws Exception
	 */
	@ValidationErrorPage(SUCCESS)
	protected String createFile() throws Exception {
		// Checks File Number has to be generated automatically
		if (this.fileMgmtService.isAutoGenerateFileNumber()) {
			// Then generate the File Number
			this.fileNumber = this.fileMgmtService.generateFileNumber();
		} else {
			// Then get the user entered file number
			this.fileNumber = ((GenericFile) this.getModel()).getFileNumber();
		}
		// Creates Content Management for the uploaded documents.
		DocumentObject associatedDoc = new DocumentObject();
		associatedDoc.setDocumentNumber(this.fileNumber);
		associatedDoc.setModuleName(DMSConstants.MODULE_NAME);
		this.processFileAttachments(associatedDoc.getAssociatedFiles());
		associatedDoc = this.docMngrService.addDocumentObject(associatedDoc);
		this.getFileAttachments(associatedDoc.getJcrUUID(), true);
		return SUCCESS;
	}
	
	/**
	 * View file. Called while accessing File Management action in View mode
	 * @return the string
	 * @throws RuntimeException
	 */
	public String viewFile() throws RuntimeException {
		this.beforeShowFile();
		return SUCCESS;
	}
	
	/**
	 * Edits the file. Called while accessing File Management action in Edit mode
	 * @return the string
	 * @throws RuntimeException
	 */
	public String editFile() throws RuntimeException {
		this.beforeShowFile();
		this.isDraftEdit = true;
		return EDIT;
	}
	
	/**
	 * Save file. Called while saving a file in Edit mode.
	 * @return the string
	 * @throws Exception
	 */
	@ValidationErrorPage(SUCCESS)
	public String saveFile() throws Exception {
		// Update the Content Management with the newly uploaded documents.
		final DocumentObject documentObject = this.docMngrService.getDocumentObject(this.fileNumber, DMSConstants.MODULE_NAME);
		if (documentObject != null) {
			this.checkFileCommentChanges(documentObject.getAssociatedFiles());
			this.processFileAttachments(documentObject.getAssociatedFiles());
			this.docMngrService.updateDocumentObject(documentObject);
			this.getFileAttachments(documentObject.getDocumentNumber(), false);
		}
		final String fileStatus = ((GenericFile) this.getModel()).getFileStatus().getCode();
		// Check the forward user type and populate the file movement history.
		if (!this.saveAsDraft && !"CLOSED".equalsIgnoreCase(fileStatus) && !"COMPLETED".equalsIgnoreCase(fileStatus)) {
			final FileAssignment fileAssignment = new FileAssignment();
			if ("INTERNAL".equals(this.forwardUserType)) {
				fileAssignment.setUserType("INTERNAL");
				fileAssignment.setInternalUser(this.internalUser);
				this.internalUser.setEmployeeInfo(this.employeeService.getEmpForPositionAndDate(new Date(), this.internalUser.getPosition().getId()));
			} else {
				fileAssignment.setUserType("EXTERNAL");
				fileAssignment.setExternalUser(this.externalUser);
			}
			((GenericFile) this.getModel()).addFileAssignmentDetail(fileAssignment);
		}
		return SUCCESS;
	}
	
	/**
	 * Saves the file in updating user Inbox or draft
	 */
	@SkipValidation
	public String saveAsDraft() throws Exception {
		this.saveAsDraft = true;
		return SUCCESS;
	}
	
	/**
	 * Before show file. Called before View and Edit Mode to check the fileNumber Exist. Then
	 * populate all the uploaded file for the given fileNumber
	 */
	protected void beforeShowFile() {
		if ((this.fileNumber == null) || this.fileNumber.isEmpty()) {
			this.addFieldError("fileNumber", this.getText("DMS.FileNum.Missing"));
		} else {
			this.getFileAttachments(this.fileNumber, false);
		}
	}
	
	/**
	 * Gets the file attachments from CMS for the given fileId. fileId can be either Document Number
	 * or JCR UUID notified by isJcruuid
	 * @param fileId
	 * @param isJcruuid
	 * @return the file attachments
	 */
	public DocumentObject getFileAttachments(final String fileId, final boolean isJcruuid) {
		DocumentObject documentObject = null;
		if (isJcruuid) {
			documentObject = this.docMngrService.getDocumentObjectByUuid(fileId);
		} else {
			documentObject = this.docMngrService.getDocumentObject(fileId, DMSConstants.MODULE_NAME);
		}
		// Fills all the File Name and corresponding File Comment to show in Edit and View mode.
		this.fileNames.clear();
		this.fileComment.clear();
		if (documentObject != null) {
			final Set<AssociatedFile> files = documentObject.getAssociatedFiles();
			for (final AssociatedFile srcFile : files) {
				this.fileNames.add(srcFile.getFileName());
				this.fileComment.add(srcFile.getRemarks());
			}
		}
		return documentObject;
	}
	
	/**
	 * Gets the file comment history. Called when user clicks View History link. This will build the
	 * Comment History using the provided fileNumber and fileName
	 * @return the file comment history
	 * @throws IOException
	 */
	@SkipValidation
	public void getFileCommentHistory() throws IOException {
		final DocumentObject documentObject = this.docMngrService.getDocumentObject(this.fileNumber, DMSConstants.MODULE_NAME);
		final List<DocumentObject> documentObjects = this.docMngrService.getVersionHistory(documentObject.getPath());
		final StringBuilder fileHistoryView = new StringBuilder();
		int count = 0;
		for (final DocumentObject docObject : documentObjects) {
			if (++count != documentObjects.size()) {
				final Set<AssociatedFile> files = docObject.getAssociatedFiles();
				for (final AssociatedFile srcFile : files) {
					if (srcFile.getFileName().equals(this.fileName)) {
						fileHistoryView.append("<b>").append(srcFile.getRemarks()).append("<br/>").append("Commented By : <i>").append(this.employeeService.getEmpForUserId(srcFile.getModifiedBy()).getEmployeeName()).append("&nbsp; on : ").append(
								DateUtils.getFormattedDate(srcFile.getModifiedDate(), "dd/MM/yyyy hh:mm a")).append("</i></b>").append("<hr size='1'/>");
					}
				}
			}
		}
		ServletActionContext.getResponse().getWriter().write(fileHistoryView.toString());
	}
	
	@SkipValidation
	public void getFileHistory() throws IOException {
		final GenericFile genericFile = (GenericFile) this.persistenceService.getSession().get(GenericFile.class, Long.parseLong(ServletActionContext.getRequest().getParameter("id")));
		final State state = genericFile.getCurrentState();
		final List<FileAssignment> fileAssignments = genericFile.getFileAssignmentDetails();
		int index = 0;
		if (state != null) {
			final StringBuilder fileHistory = new StringBuilder("");
			final List<State> stateHistory = state.getHistory();
			stateHistory.remove(stateHistory.size() - 1);
			Collections.reverse(stateHistory);
			fileHistory.append("[");
			for (final State fileState : stateHistory) {
				if (!fileState.getValue().equalsIgnoreCase("CLOSED") && !fileState.getValue().equalsIgnoreCase("COMPLETED")) {
					final FileAssignment fileAssignment = fileAssignments.get(index);
					fileHistory.append("{Id:'").append(fileState.getId()).append("',");
					fileHistory.append("Date:'").append(getFormattedDate(fileState.getCreatedDate(), "dd/MM/yyyy hh:mm a")).append("',");
					String senderDept = null;
					String senderName = null;
					String receiverDept = null;
					String forwardDtls = null;
					String outboundFileNumber = EMPTY;
					if (fileAssignment.getUserType().equals("INTERNAL")) {
						if (index == 0) {
							if (genericFile.getFileType().equalsIgnoreCase("Internal")) {
								senderDept = ((InternalFile) genericFile).getSender().getDepartment().getDeptName();
								senderName = ((InternalFile) genericFile).getSender().getEmployeeInfo().getEmployeeName() +" / "+((InternalFile) genericFile).getSender().getPosition().getName();
							} else if (genericFile.getFileType().equalsIgnoreCase("Inbound")) {
								senderDept = ((InboundFile) genericFile).getSender().getUserSource().getName();
								senderName = ((InboundFile) genericFile).getSender().getUserName();
							} else if (genericFile.getFileType().equalsIgnoreCase("Outbound")) {
								senderDept = ((OutboundFile) genericFile).getSender().getDepartment().getDeptName();
								senderName = ((OutboundFile) genericFile).getSender().getEmployeeInfo().getEmployeeName()+" / "+((OutboundFile) genericFile).getSender().getPosition().getName();
							}
						} else {
							if (fileAssignments.get(index - 1).getUserType().equals("INTERNAL")) {
								senderDept = fileAssignments.get(index - 1).getInternalUser().getDepartment().getDeptName();
								senderName = fileAssignments.get(index - 1).getInternalUser().getEmployeeInfo().getEmployeeName()+" / "+fileAssignments.get(index - 1).getInternalUser().getPosition().getName();
							} else {
								senderDept = fileAssignments.get(index - 1).getExternalUser().getUserSource().getName();
								senderName = fileAssignments.get(index - 1).getExternalUser().getUserName();
							}
						}
						receiverDept = fileAssignment.getInternalUser().getDepartment().getDeptName();
						forwardDtls = fileAssignment.getInternalUser().getEmployeeInfo().getEmployeeName() + " / " + fileAssignment.getInternalUser().getPosition().getName();
					} else {
						if (index == 0) {
							senderDept = ((OutboundFile) genericFile).getSender().getDepartment().getDeptName();
							senderName = ((OutboundFile) genericFile).getSender().getEmployeeInfo().getEmployeeName()+" / "+((OutboundFile) genericFile).getSender().getPosition().getName();
						} else {
							if (fileAssignments.get(index - 1).getUserType().equals("INTERNAL")) {
								senderDept = fileAssignments.get(index - 1).getInternalUser().getDepartment().getDeptName();
								senderName = fileAssignments.get(index - 1).getInternalUser().getEmployeeInfo().getEmployeeName()+" / "+fileAssignments.get(index - 1).getInternalUser().getPosition().getName();
							} else {
								senderDept = fileAssignments.get(index - 1).getExternalUser().getUserSource().getName();
								senderName = fileAssignments.get(index - 1).getExternalUser().getUserName();
							}
						}
						receiverDept = fileAssignment.getExternalUser().getUserSource().getName();
						forwardDtls = fileAssignment.getExternalUser().getUserSource().getName() + "\r\n" + fileAssignment.getExternalUser().getUserName() + "\r\n" + fileAssignment.getExternalUser().getUserAddress();
						outboundFileNumber = fileAssignment.getExternalUser().getOutboundFileNumber();
					}
					fileHistory.append("SenderDepartment:'").append(senderDept).append("',");
					fileHistory.append("Sender:'").append(senderName).append("',");
					fileHistory.append("Task:'").append(fileState.getType()).append("',");
					fileHistory.append("Status:'").append(fileState.getValue()).append("',");
					fileHistory.append("ForwardingType:'").append(fileAssignment.getUserType()).append("',");
					fileHistory.append("ReceiverDepartment:'").append(receiverDept).append("',");
					fileHistory.append("ForwardingDetails:'").append(escapeJavaScript(escapeSpecialChars(forwardDtls))).append("',");
					fileHistory.append("OutboundFileNumber:'").append(escapeJavaScript(escapeSpecialChars(outboundFileNumber  == null ? EMPTY : outboundFileNumber))).append("',");
					fileHistory.append("Comments:'").append(fileState.getText1() == null ? EMPTY : escapeJavaScript(escapeSpecialChars(fileState.getText1()))).append("'},");
					index++;
				}
			}
			fileHistory.deleteCharAt(fileHistory.length() - 1);
			fileHistory.append("]");
			ServletActionContext.getResponse().getWriter().write(fileHistory.toString());
			return;
		}
		ServletActionContext.getResponse().getWriter().write(ERROR);
	}
	
	/**
	 * Process the uploaded files to attach with Document Object-AssociatedFile
	 * @param associatedFiles
	 */
	public void processFileAttachments(final Set<AssociatedFile> associatedFiles) {
		if (ServletActionContext.getRequest() instanceof MultiPartRequestWrapper) {
			final MultiPartRequestWrapper multiWrapper = (MultiPartRequestWrapper) ServletActionContext.getRequest();
			// Checks file if any file is uploaded
			if (multiWrapper.getFiles("file") != null) {
				final List<File> uploadFiles = new ArrayList<File>(Arrays.asList(multiWrapper.getFiles("file")));
				final List<String> fileNames = new ArrayList<String>(Arrays.asList(multiWrapper.getFileNames("file")));
				final List<String> mimeTypes = new ArrayList<String>(Arrays.asList(multiWrapper.getContentTypes("file")));
				final Iterator<String> fileItr = fileNames.iterator();
				int index = -1;
				final Date currentDate = new Date();
				final List<String> invalidFile = new ArrayList<String>();
				final List<String> tempFileName = new ArrayList<String>();
				while (fileItr.hasNext()) {
					index++;
					final String fileName = fileItr.next();
					// Checks duplicate file upload
					if (tempFileName.contains(fileName)) {
						fileItr.remove();
						this.addActionMessage(this.getText("DMS.FileAttach.Skip") + fileName);
						continue;
					} else {
						tempFileName.add(fileName);
					}
					// Checks .exe file upload
					final String extension = fileName.substring(fileName.lastIndexOf('.'));
					if (extension.startsWith(".exe")) {
						fileItr.remove();
						invalidFile.add(fileName);
						continue;
					} else {
						// If file is valid create an AssociatedFile object and add it in to the
						// associatedFile list
						// Existing files in the associatedFile list will be replaced with the new
						// AssociatedFile object
						final AssociatedFile associatedFile = new AssociatedFile();
						FileInputStream fileStream = null;
						associatedFile.setFileName(fileName);
						// Checks file name is valid
						if (hasSpecialChars(associatedFile.getFileName())) {
							fileItr.remove();
							this.addActionMessage(this.getText("DMS.File.NameInvalid") + associatedFile.getFileName());
							continue;
						}
						try {
							fileStream = new FileInputStream(uploadFiles.get(index));
						} catch (final FileNotFoundException exp) {
							fileItr.remove();
							this.addActionMessage(this.getText("DMS.File.NotValid") + associatedFile.getFileName());
							continue;
						}
						associatedFile.setFileInputStream(fileStream);
						associatedFile.setMimeType(mimeTypes.get(index));
						associatedFile.setLength(uploadFiles.get(index).length());
						associatedFile.setCreatedDate(currentDate);
						associatedFile.setModifiedDate(currentDate);
						associatedFile.setCreatedBy(Integer.valueOf(EGOVThreadLocals.getUserId()));
						associatedFile.setModifiedBy(Integer.valueOf(EGOVThreadLocals.getUserId()));
						associatedFile.setRemarks(this.comment.get(index));
						// Adding new AssociatedFile object in the associatedFile list
						if (!associatedFiles.add(associatedFile)) {
							final AssociatedFile file = new AssociatedFile();
							file.setFileName(fileName);
							associatedFiles.remove(file);
							associatedFiles.add(associatedFile);
						}
					}
				}
				// adding action error if there is any invalid files
				if (!invalidFile.isEmpty()) {
					this.addActionMessage(this.getText("DMS.FileAttach.Invalid") + invalidFile);
				}
			}
		}
	}
	
	/**
	 * Check only file comment got changed. if file comment got changed the remove the file from
	 * DocumentObject's associatedFile list and recreate and re-attach a AssociatedFile with the
	 * comment update
	 * @param associatedFiles
	 */
	public void checkFileCommentChanges(final Set<AssociatedFile> associatedFiles) {
		if (!this.fileComment.isEmpty()) {
			final Date currentDate = new Date();
			final AssociatedFile tempFile = new AssociatedFile();
			final Iterator<AssociatedFile> asocitdFileItr = associatedFiles.iterator();
			final HashMap<String, AssociatedFile> fileHolder = new HashMap<String, AssociatedFile>();
			while (asocitdFileItr.hasNext()) {
				final AssociatedFile associatedFile = asocitdFileItr.next();
				fileHolder.put(associatedFile.getFileName(), associatedFile);
			}
			for (final String comment : this.fileComment) {
				if (!comment.isEmpty()) {
					final String[] commentComp = comment.split("#FILENAME#");
					if (fileHolder.containsKey(commentComp[1])) {
						tempFile.setFileName(commentComp[1]);
						associatedFiles.remove(tempFile);
						final AssociatedFile associatedFile = fileHolder.get(commentComp[1]);
						associatedFile.setRemarks(commentComp[0]);
						associatedFile.setModifiedDate(currentDate);
						associatedFile.setModifiedBy(Integer.valueOf(EGOVThreadLocals.getUserId()));
						associatedFiles.add(associatedFile);
					}
				}
			}
		}
		this.fileComment.clear();
	}
	
	/**
	 * Gets the first level boundries.
	 * @return the first level boundries
	 * @throws NoSuchObjectException
	 * @throws TooManyValuesException
	 */
	public List<BoundaryImpl> getFirstLevelBoundries() throws NoSuchObjectException, TooManyValuesException {
		final HeirarchyType heirarchyType = this.heirarchyTypeDAO.getHierarchyTypeByName("ADMINISTRATION");
		final BoundaryType boundaryType = this.boundaryTypeDAO.getBoundaryType("City", heirarchyType);
		final List<BoundaryImpl> cities = this.boundaryDAO.getAllBoundariesByBndryTypeId(boundaryType.getId());
		final List<BoundaryImpl> firstLevel = new ArrayList<BoundaryImpl>();
		for (final BoundaryImpl city : cities) {
			firstLevel.addAll(city.getChildren());
		}
		return firstLevel;
	}
	
	/**
	 * Gets the second level boundries.
	 * @return the second level boundries
	 * @throws Exception
	 */
	@SkipValidation
	public String getSecondLevelBoundries() throws Exception {
		this.setSecondLevelBndryList(String.valueOf(ServletActionContext.getRequest().getParameter("firstLevelBndry")));
		return "bndryResults";
	}
	
	/**
	 * Sets the second level bndry list.
	 * @param firstLevelBndryId the new second level bndry list
	 * @throws Exception
	 */
	private void setSecondLevelBndryList(final String firstLevelBndryId) throws Exception {
		this.secndLvlBndryList = this.boundaryDAO.getChildBoundaries(firstLevelBndryId);
	}
	
	/**
	 * Gets the second level bndry list.
	 * @return the second level bndry list
	 */
	public List<Boundary> getSecondLevelBndryList() {
		return this.secndLvlBndryList;
	}
	
	/**
	 * Gets the file subcategories.
	 * @return the file subcategories
	 * @throws Exception
	 */
	@SkipValidation
	public String getFileSubcategories() throws Exception {
		this.setFileSubCategories(Long.valueOf(ServletActionContext.getRequest().getParameter("fileCategory")));
		return "subCategoryResults";
	}
	
	/**
	 * Sets the file sub categories.
	 * @param catId the new file sub categories
	 */
	private void setFileSubCategories(final Long catId) {
		this.fileSubCategories = this.persistenceService.findAllBy("from org.egov.dms.models.FileCategory where parent.id = ? ", catId);
	}
	
	/**
	 * Gets the file sub categories list.
	 * @return the file sub categories list
	 */
	public List<FileCategory> getFileSubCategoriesList() {
		return this.fileSubCategories;
	}
	
	/**
	 * Populate designation.
	 * @return the string
	 * @throws IOException
	 */
	@SkipValidation
	public String populateDesignation() throws IOException {
		this.designations = this.eisService.getAllDesignationByDept(Integer.valueOf(ServletActionContext.getRequest().getParameter("deptId")), new Date());
		return "designationResults";
	}
	
	/**
	 * Gets the designation list.
	 * @return the designation list
	 */
	public List<DesignationMaster> getDesignationList() {
		return this.designations;
	}
	
	/**
	 * Populate users by dept and desig.
	 * @return the string
	 * @throws IOException
	 */
	@SkipValidation
	public String populateUsersByDeptAndDesig() throws IOException {
		this.setEmpInfoList(ServletActionContext.getRequest().getParameter("deptId"), ServletActionContext.getRequest().getParameter("desigId"));
		return "userResults";
	}
	
	/**
	 * Sets the emp info list.
	 * @param departmentId
	 * @param designationId
	 */
	protected void setEmpInfoList(final String departmentId, final String designationId) {
		final HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("departmentId", departmentId);
		paramMap.put("designationId", designationId);
		this.empInfoList = new ArrayList<EmployeeView>();
		this.empInfoList.addAll((List<EmployeeView>) this.eisService.getEmployeeInfoList(paramMap));
	}
	
	/**
	 * Gets the emp info list.
	 * @return the emp info list
	 */
	public List<EmployeeView> getEmpInfoList() {
		return this.empInfoList;
	}
	
	/**
	 * Gets the primary position for user.
	 * @param userId
	 * @return the primary position for user
	 */
	public Position getPrimaryPositionForUser(final Integer userId) {
		return this.eisService.getPrimaryPositionForUser(userId, new Date());
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.opensymphony.xwork2.ActionSupport#validate()
	 */
	@Override
	public void validate() {
		this.actionName = ServletActionContext.getActionMapping().getMethod().toString();
		this.httpRequest = ServletActionContext.getRequest();
		if ((this.request.get("fileNumber") == null) || this.request.get("fileNumber").equals("")) {
			this.addFieldError("fileNumber", this.getText("DMS.fileNumber.Required"));
		}
		if ((this.request.get("fileDate") == null) || this.request.get("fileDate").equals("")) {
			this.addFieldError("fileDate", this.getText("DMS.fileDate.Required"));
		}
		if ((this.request.get("fileReceivedOrSentDate") == null) || this.request.get("fileReceivedOrSentDate").equals("")) {
			this.addFieldError("fileReceivedOrSentDate", this.getText("DMS.fileReceivedOrSentDate.Required"));
		} else {
			final Date dt = (Date) this.request.get("fileReceivedOrSentDate");
			if (dt.after(new Date())) {
				this.addFieldError("fileReceivedOrSentDate", this.getText("DMS.fileReceivedOrSentDate.Invalid"));
			}
		}
		if ((this.request.get("fileCategory") == null) || this.request.get("fileCategory").equals("")) {
			this.addFieldError("fileCategory", this.getText("DMS.fileCategory.Required"));
		}
		if ((this.request.get("fileHeading") == null) || this.request.get("fileHeading").equals("")) {
			this.addFieldError("fileHeading", this.getText("DMS.fileHeading.Required"));
		}
		if ((this.request.get("fileStatus") == null) || this.request.get("fileStatus").equals("")) {
			this.addFieldError("fileStatus", this.getText("DMS.fileStatus.Required"));
		}
		if ("saveFile".equals(this.actionName) && (this.request.get("fileStatus") != null) && !"CLOSED".equalsIgnoreCase(((EgwStatus) this.request.get("fileStatus")).getCode())
				&& !"COMPLETED".equalsIgnoreCase(((EgwStatus) this.request.get("fileStatus")).getCode())) {
			if ("INTERNAL".equals(this.forwardUserType)) {
				if ((this.getInternalUser().getDepartment() == null) || this.getInternalUser().getDepartment().equals("")) {
					this.addFieldError("internalUser.department", this.getText("internalUser.department.required"));
				}
				if ((this.getInternalUser().getDesignation() == null) || this.getInternalUser().getDesignation().equals("") || this.getInternalUser().getDesignation().equals("-1")) {
					this.addFieldError("internalUser.designation", this.getText("internalUser.designation.required"));
				}
				if ((this.getInternalUser().getPosition() == null) || this.getInternalUser().getPosition().equals("") || this.getInternalUser().getPosition().equals("-1")) {
					this.addFieldError("internalUser.position", this.getText("internalUser.position.required"));
				}
			} else if ("EXTERNAL".equals(this.forwardUserType)) {
				if ((this.getExternalUser().getUserSource() == null) || this.getExternalUser().getUserSource().equals("")) {
					this.addFieldError("externalUser.userSource", this.getText("externalUser.userSource.required"));
				}
				if ((this.getExternalUser().getUserName() == null) || this.getExternalUser().getUserName().trim().intern().equals("")) {
					this.addFieldError("externalUser.userName", this.getText("externalUser.userName.required"));
				}
			}
		}
	}
	
	public boolean hasErrors() {
		final boolean hasErrors = super.hasErrors();
		if (hasErrors) {
			this.getFileAttachments(this.fileNumber, false);
		}
		return hasErrors;
	}
	
	/**
	 * Sets the eis service.
	 * @param eisService the new eis service
	 */
	public void setEisService(final EISServeable eisService) {
		this.eisService = eisService;
	}
	
	/**
	 * Sets the boundary dao.
	 * @param boundaryDAO the new boundary dao
	 */
	public void setBoundaryDAO(final BoundaryDAO boundaryDAO) {
		this.boundaryDAO = boundaryDAO;
	}
	
	/**
	 * Gets the current date.
	 * @return the current date
	 */
	public String getCurrentDate() {
		return DateUtils.getDefaultFormattedDate(new Date());
	}
	
	/**
	 * Sets the category id.
	 * @param categoryId the new category id
	 */
	public void setCategoryId(final String categoryId) {
		this.ajaxReqParam.put("categoryId", categoryId);
	}
	
	/**
	 * Sets the dept id.
	 * @param deptId the new dept id
	 */
	public void setDeptId(final String deptId) {
		this.ajaxReqParam.put("deptId", deptId);
	}
	
	/**
	 * Sets the desig id.
	 * @param desigId the new desig id
	 */
	public void setDesigId(final Integer desigId) {
		this.ajaxReqParam.put("desigId", desigId);
	}
	
	/**
	 * Sets the parent id.
	 * @param parentId the new parent id
	 */
	public void setParentId(final String parentId) {
		this.ajaxReqParam.put("parentId", parentId);
	}
	
	/**
	 * Sets the boundary type dao.
	 * @param boundaryTypeDAO the new boundary type dao
	 */
	public void setBoundaryTypeDAO(final BoundaryTypeDAO boundaryTypeDAO) {
		this.boundaryTypeDAO = boundaryTypeDAO;
	}
	
	/**
	 * Sets the heirarchy type dao.
	 * @param heirarchyTypeDAO the new heirarchy type dao
	 */
	public void setHeirarchyTypeDAO(final HeirarchyTypeDAO heirarchyTypeDAO) {
		this.heirarchyTypeDAO = heirarchyTypeDAO;
	}
	
	/**
	 * Sets the file mgmt service.
	 * @param fileMgmtService the new file mgmt service
	 */
	public void setFileMgmtService(final FileManagementService fileMgmtService) {
		this.fileMgmtService = fileMgmtService;
	}
	
	/**
	 * Gets the comment.
	 * @return the comment
	 */
	public List<String> getComment() {
		return this.comment;
	}
	
	/**
	 * Sets the comment.
	 * @param comment the new comment
	 */
	public void setComment(final List<String> comment) {
		this.comment = comment;
	}
	
	/**
	 * Sets the document manager service.
	 * @param docMngrService the new document manager service
	 */
	public void setDocumentManagerService(final DocumentManagerService<DocumentObject> docMngrService) {
		this.docMngrService = docMngrService;
	}
	
	/**
	 * Sets the eis manager.
	 * @param eisManager the new eis manager
	 */
	public void setEmployeeService(final EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
	
	/**
	 * Gets the external user.
	 * @return the external user
	 */
	public ExternalUser getExternalUser() {
		return this.externalUser;
	}
	
	/**
	 * Sets the external user.
	 * @param externalUser the new external user
	 */
	public void setExternalUser(final ExternalUser externalUser) {
		this.externalUser = externalUser;
	}
	
	/**
	 * Gets the internal user.
	 * @return the internal user
	 */
	public InternalUser getInternalUser() {
		return this.internalUser;
	}
	
	/**
	 * Sets the internal user.
	 * @param internalUser the new internal user
	 */
	public void setInternalUser(final InternalUser internalUser) {
		this.internalUser = internalUser;
	}
	
	/**
	 * Gets the forward user type.
	 * @return the forward user type
	 */
	public String getForwardUserType() {
		return this.forwardUserType;
	}
	
	/**
	 * Sets the forward user type.
	 * @param forwardUserType the new forward user type
	 */
	public void setForwardUserType(final String forwardUserType) {
		this.forwardUserType = forwardUserType;
	}
	
	/**
	 * Sets the file number.
	 * @param fileNumber the new file number
	 */
	public void setFileNumber(final String fileNumber) {
		this.fileNumber = fileNumber;
	}
	
	/**
	 * Gets the file comment.
	 * @return the file comment
	 */
	public List<String> getFileComment() {
		return this.fileComment;
	}
	
	/**
	 * Gets the file names.
	 * @return the file names
	 */
	public List<String> getFileNames() {
		return this.fileNames;
	}
	
	/**
	 * Sets the file comment.
	 * @param fileComment the new file comment
	 */
	public void setFileComment(final List<String> fileComment) {
		this.fileComment = fileComment;
	}
	
	/**
	 * Gets the workflow comment.
	 * @return the workflow comment
	 */
	public String getWorkflowComment() {
		return this.workflowComment;
	}
	
	/**
	 * Sets the workflow comment.
	 * @param workflowComment the new workflow comment
	 */
	public void setWorkflowComment(final String workflowComment) {
		this.workflowComment = workflowComment;
	}
	
	/**
	 * Gets the id.
	 * @return the id
	 */
	public Long getId() {
		return ((GenericFile) this.getModel()).getId();
	}
	
	/**
	 * Sets the id.
	 * @param fileId the new id
	 */
	public void setId(final Long fileId) {
		((GenericFile) this.getModel()).setId(fileId);
	}
	
	/**
	 * Sets the file name.
	 * @param fileName the new file name
	 */
	public void setFileName(final String fileName) {
		this.fileName = fileName;
	}
	
	/**
	 * @return the isDraftEdit
	 */
	public boolean getIsDraftEdit() {
		return this.isDraftEdit;
	}
	
	/**
	 * @param isDraftEdit the isDraftEdit to set
	 */
	public void setIsDraftEdit(final boolean isDraftEdit) {
		this.isDraftEdit = isDraftEdit;
	}

	public FileService getFileService() {
		return fileService;
	}

	public void setFileService(FileService fileService) {
		this.fileService = fileService;
	}

	public String getOutboundFileNumber() {
		return outboundFileNumber;
	}

	public void setOutboundFileNumber(String outboundFileNumber) {
		this.outboundFileNumber = outboundFileNumber;
	}
	
	public void setAuditEventService(AuditEventService auditEventService) {
		this.auditEventService = auditEventService;
	}

	protected void doAuditing(AuditEntity auditEntity, String action) {
		GenericFile genericFile = (GenericFile)this.getModel();
		final String details = new StringBuffer("[ File Type : ").
								append(genericFile.getFileType()).append(", File Date : ").append(DateUtils.getDefaultFormattedDate(genericFile.getFileDate())).
								append(", File Category : ").append(genericFile.getFileCategory().getName()).append(", File Sub-Category : ").
								append(genericFile.getFileSubcategory() == null ? "" : genericFile.getFileSubcategory().getName()).
								append(", File Heading : ").append(genericFile.getFileHeading()).append(", File Status : ").append(genericFile.getFileStatus().getDescription()).append(" ]").toString();
		final AuditEvent auditEvent = new AuditEvent(AuditModule.DMS, auditEntity, action, genericFile.getFileNumber(), details);
		auditEvent.setPkId(genericFile.getId());
		this.auditEventService.createAuditEvent(auditEvent, this.getModel().getClass());
	}
	
	@SkipValidation
	public String auditReport() {
		return "auditReport";
	}
}
