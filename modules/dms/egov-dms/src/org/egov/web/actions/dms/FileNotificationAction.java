/*
 * @(#)FileNotificationAction.java 3.0, 16 Jul, 2013 11:33:56 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.web.actions.dms;

import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.egov.infstr.docmgmt.DocumentManagerService.PROP_DOC_NUM;
import static org.egov.infstr.utils.DateUtils.getFormattedDate;
import static org.egov.infstr.utils.StringUtils.hasSpecialChars;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.displaytag.pagination.PaginatedList;
import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;
import org.egov.dms.models.FileCategory;
import org.egov.dms.models.FilePriority;
import org.egov.dms.models.FileSource;
import org.egov.dms.models.Notification;
import org.egov.dms.models.NotificationFile;
import org.egov.dms.services.DMSConstants;
import org.egov.dms.services.FileManagementService;
import org.egov.infstr.ValidationException;
import org.egov.infstr.auditing.model.AuditEntity;
import org.egov.infstr.auditing.model.AuditEvent;
import org.egov.infstr.auditing.model.AuditModule;
import org.egov.infstr.auditing.service.AuditEventService;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.docmgmt.AssociatedFile;
import org.egov.infstr.docmgmt.DocumentManagerService;
import org.egov.infstr.docmgmt.DocumentObject;
import org.egov.infstr.models.StateAware;
import org.egov.infstr.services.EISServeable;
import org.egov.infstr.services.Page;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.workflow.NotificationGroup;
import org.egov.lib.rjbac.user.User;
import org.egov.pims.commons.Position;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.utils.EgovPaginatedList;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 * The Class FileNotificationAction.
 */
@ParentPackage("egov")
public class FileNotificationAction extends BaseFormAction {
	
	private static final long serialVersionUID = 1L;
	private transient List<String> comment = new ArrayList<String>();
	private transient NotificationFile notificationFile = new NotificationFile();
	private transient FileManagementService fileMgmtService;
	private transient DocumentManagerService<DocumentObject> docMngrService;
	private transient PersistenceService<NotificationFile, Long> notificationFilePersistenceService;
	private transient PersistenceService<NotificationGroup, Long> notificationGroupPersistenceService;
	private transient PersistenceService<Notification, Long> notificationPersistenceService;
	private transient EISServeable eisService;
	private transient List<FileCategory> fileSubCategories = new ArrayList<FileCategory>();
	private transient final Set<NotificationGroup> notificationGroups = new HashSet<NotificationGroup>();
	private transient final List<Long> notificationGroupIds = new ArrayList<Long>();
	private transient final List<String> fileComment = new ArrayList<String>();
	private transient final List<String> fileNames = new ArrayList<String>();
	private transient PaginatedList pagedResults;
	private transient boolean deleteAll;
	private transient Long id;
	private transient int page;
	private transient int reportSize;
	private transient String mode;
	private transient AuditEventService auditEventService;
	
	/**
	 * Instantiates a new file notification action. Attaches all the related entity which is common
	 * to subclasses
	 */
	public FileNotificationAction() {
		super();
		this.addRelatedEntity("fileCategory", FileCategory.class);
		this.addRelatedEntity("fileSubcategory", FileCategory.class);
		this.addRelatedEntity("filePriority", FilePriority.class);
		this.addRelatedEntity("fileSource", FileSource.class);
		this.addRelatedEntity("sender.userSource", FileSource.class);
	}
	
	/**
	 * Prepares all the drop downs and other entities and attaches with the model
	 */
	@Override
	public void prepare() {
		super.prepare();
		this.setupDropdownDataExcluding("fileCategory", "fileSubCategory", "notificationGroups");
		this.addDropdownData("fileCategoryList", this.persistenceService.findAllBy("from org.egov.dms.models.FileCategory where parent is null"));
		this.addDropdownData("notificationGroupsList", this.persistenceService.findAllBy("from org.egov.infstr.workflow.NotificationGroup where active='Y' and effectiveDate <= sysdate"));
		if (this.notificationFile.getFileCategory() != null) {
			this.setFileSubCategories(this.notificationFile.getFileCategory().getId());
		}
	}
	
	/**
	 * Returns empty GenericFile, Subclass have to override it.
	 */
	public Object getModel() {
		return this.notificationFile;
	}
	
	@Override
	@SkipValidation
	public String execute() {
		if (this.fileMgmtService.isAutoGenerateFileNumber()) {
			this.notificationFile.setFileNumber(this.getText("DMS.AutoFileNo.Msg"));
		}
		return SUCCESS;
	}
	
	/**
	 * Creates the file. Called while submitting create file action form.
	 * @return the string
	 * @throws Exception
	 */
	public String createFileNotification() throws Exception {
		// Checks File Number has to be generated automatically
		String fileNumber = null;
		if (this.fileMgmtService.isAutoGenerateFileNumber()) {
			// Then generate the File Number
			fileNumber = this.fileMgmtService.generateFileNumber();
		} else {
			// Then get the user entered file number
			fileNumber = this.notificationFile.getFileNumber();
		}
		// Creates Content Management for the uploaded documents.
		DocumentObject associatedDoc = new DocumentObject();
		associatedDoc.setDocumentNumber(fileNumber);
		associatedDoc.setModuleName(DMSConstants.MODULE_NAME);
		this.processFileAttachments(associatedDoc.getAssociatedFiles());
		associatedDoc = this.docMngrService.addDocumentObject(associatedDoc);
		this.notificationFile.setFileNumber(fileNumber);
		this.notificationFile.setFileReceivedOrSentDate(notificationFile.getFileDate());
		this.notificationFile.setNotificationGroups(this.notificationGroups);		
		this.notificationFilePersistenceService.persist(this.notificationFile);
		final ArrayList<Integer> positionIds = new ArrayList<Integer>();
		for (final NotificationGroup notificationGroup : this.notificationGroups) {
			final Set<Position> postions = notificationGroup.getMembers();
			for (final Position position : postions) {
				if (!positionIds.contains(position.getId())) {
					positionIds.add(position.getId());
					final Notification notification = new Notification();
					notification.setPosition(position);
					notification.setFile(this.notificationFile);
					this.notificationPersistenceService.persist(notification);
				}
			}
		}
		this.getFileAttachments(associatedDoc.getJcrUUID(), true);
		doAuditing(AuditEvent.CREATED);
		this.deleteAll = true;
		return "view";
	}
	
	@SkipValidation
	public String viewFileNotification() {
		if (this.id == null) {
			this.addActionMessage(this.getText("DMS.FileNum.Missing"));
			return ERROR;
		} else {
			this.notificationFile = (NotificationFile) this.notificationFilePersistenceService.findById(this.id, false);
		}
		if (this.notificationFile == null) {
			this.addActionMessage(this.getText("DMS.FileNum.Missing"));
			throw new RuntimeException("File Notification for the given File ID does not exist");
		} else {
			this.getFileAttachments(this.notificationFile.getFileNumber(), false);
		}
		this.deleteAll = false;
		return "view";
	}
	
	@SkipValidation
	public String deleteFileNotification() {
		Query query = null;
		if (this.deleteAll) {
			query = this.persistenceService.getSession().createQuery("delete from org.egov.dms.models.Notification where file=:file");
			query.setLong("file", this.id);
		} else {
			query = this.persistenceService.getSession().createQuery("delete from org.egov.dms.models.Notification where file=:file and position=:position");
			query.setLong("file", this.id);
			query.setInteger("position", this.eisService.getPrimaryPositionForUser(Integer.valueOf(EGOVThreadLocals.getUserId()), new Date()).getId());
		}
		query.executeUpdate();
		if (this.deleteAll) {
			this.addActionMessage(this.getText("DMS.FileNotification.Deleted"));
		} else {
			this.addActionMessage(this.getText("DMS.FileNotification.Inbox.Deleted"));
		}
		return "view";
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
	 * Process the uploaded files to attach with Document Object-AssociatedFile
	 * @param associatedFiles
	 */
	private void processFileAttachments(final Set<AssociatedFile> associatedFiles) {
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
							throw new ValidationException(PROP_DOC_NUM, "DocMngr.File.Exist", associatedFile.getFileName());
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
	
	@SkipValidation
	public void loadFileNotificationData() throws IOException {
		try {
			final List<Position> positions = this.eisService.getPositionsForUser(Integer.valueOf(EGOVThreadLocals.getUserId()), new Date());
			final List<StateAware> notificationFiles = new ArrayList<StateAware>();
			for (final Position position : positions) {
				notificationFiles.addAll(this.getFileNotification(position.getId()));
			}
			Collections.reverse(notificationFiles);
			final StringBuilder notificationItem = new StringBuilder("");
			if (!notificationFiles.isEmpty()) {
				notificationItem.append("[");
				final Date currentDate = new Date();
				for (final StateAware notificationFile : notificationFiles) {
					notificationItem.append("{Id:'").append("#").append(notificationFile.getId()).append("',");
					notificationItem.append("Date:'").append(getFormattedDate(notificationFile.getCreatedDate(), "dd/MM/yyyy hh:mm a")).append("',");
					final Position position = this.eisService.getPrimaryPositionForUser(notificationFile.getCreatedBy().getId(), currentDate);
					final User user = this.eisService.getUserForPosition(position.getId(), currentDate);
					final StringBuilder senderName = new StringBuilder();
					senderName.append(position == null ? "UNKNOWN" : position.getName()).append(" / ").append(user == null ? "UNKNOWN" : user.getFirstName() + " " + (user.getLastName() == null ? EMPTY : user.getLastName()));
					notificationItem.append("Sender:'").append(senderName).append("',");
					notificationItem.append("Task:'").append("File Notification").append("',");
					notificationItem.append("Status:'").append("Notification").append("',");
					notificationItem.append("Details:'").append(notificationFile.getStateDetails()).append("',");
					notificationItem.append("Link:'").append("/dms/dms/fileNotification!viewFileNotification.action?id=").append(notificationFile.getId()).append("'},");
				}
				notificationItem.deleteCharAt(notificationItem.length() - 1);
				notificationItem.append("]");
			}
			ServletActionContext.getResponse().getWriter().write(notificationItem.toString());
		} catch (final RuntimeException e) {
			LOG.error("Error occurred while getting File Notification, Cause : " + e.getMessage(), e);
			ServletActionContext.getResponse().getWriter().write(ERROR);
		}
	}
	
	@SkipValidation
	public List<StateAware> getFileNotification(final Integer positionId) {
		final List<Notification> notifications = this.notificationPersistenceService.findAllBy("from org.egov.dms.models.Notification where position.id = ?", positionId);
		final List<StateAware> notificationFiles = new ArrayList<StateAware>();
		for (final Notification notification : notifications) {
			notificationFiles.add(notification.getFile());
		}
		return notificationFiles;
	}
	
	@SkipValidation
	public String search() {
		return "search";
	}
	
	@SkipValidation
	public String searchFileNotification() throws IOException, ParseException {
		final HttpServletRequest request = ServletActionContext.getRequest();
		final Criteria criteria = this.buildSearchCriteria(request, false);
		if (this.page == 0) {
			this.reportSize = (Integer) this.buildSearchCriteria(request, true).uniqueResult();
		}
		final ParamEncoder paramEncoder = new ParamEncoder("currentRowObject");
		final boolean isReport = this.parameters.get(paramEncoder.encodeParameterName(TableTagParameters.PARAMETER_EXPORTTYPE)) != null;
		final Page page = new Page(criteria, isReport ? 1 : this.page, isReport ? null : 10);
		this.pagedResults = new EgovPaginatedList(page, this.reportSize, null, null);
		request.setAttribute("hasResult", !page.getList().isEmpty());
		return "search";
	}
	
	private Criteria buildSearchCriteria(final HttpServletRequest request, final boolean isForRowCount) throws ParseException {
		final Criteria criteria = this.getPersistenceService().getSession().createCriteria(NotificationFile.class);
		if (isForRowCount) {
			criteria.setProjection(Projections.rowCount());
		}
		if (isNotBlank(request.getParameter("fileNumber"))) {
			criteria.add(Restrictions.eq("fileNumber", request.getParameter("fileNumber")));
		} else {
			if (isNotBlank(request.getParameter("fileCategory"))) {
				criteria.add(Restrictions.eq("fileCategory.id", Long.valueOf(request.getParameter("fileCategory"))));
			}
			if (isNotBlank(request.getParameter("fileHeading"))) {
				criteria.add(Restrictions.like("fileHeading", "%" + request.getParameter("fileHeading") + "%"));
			}
			if (isNotBlank(request.getParameter("userSource"))) {
				criteria.createAlias("sender", "sdr").createAlias("sdr.userSource", "usrSrc");
				criteria.add(Restrictions.eq("usrSrc.id",Long.valueOf(request.getParameter("userSource"))));
			}
			if (isNotBlank(request.getParameter("startDate")) && isBlank(request.getParameter("endDate"))) {
				criteria.add(Restrictions.ge("fileDate", DateUtils.constructDateRange(request.getParameter("startDate"), request.getParameter("startDate"))[0]));
			} else if (isBlank(request.getParameter("startDate")) && isNotBlank(request.getParameter("endDate"))) {
				criteria.add(Restrictions.le("fileDate", DateUtils.constructDateRange(request.getParameter("endDate"), request.getParameter("endDate"))[0]));
			} else if (isNotBlank(request.getParameter("startDate")) && isNotBlank(request.getParameter("endDate"))) {
				final Date[] dates = DateUtils.constructDateRange(request.getParameter("startDate"), request.getParameter("endDate"));
				criteria.add(Restrictions.between("fileDate", dates[0], dates[1]));
			}
			criteria.addOrder(Order.desc("fileDate"));
		}
		return criteria;
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
	
	@Override
	public void validate() {
		final HttpServletRequest httpRequest = ServletActionContext.getRequest();
		if ((this.request.get("fileNumber") == null) || this.request.get("fileNumber").equals("")) {
			this.addFieldError("fileNumber", this.getText("DMS.fileNumber.Required"));
		}
		if ((this.request.get("fileDate") == null) || this.request.get("fileDate").equals("")) {
			this.addFieldError("fileDate", this.getText("DMS.fileDate.Required"));
		}
		if ((this.request.get("fileCategory") == null) || this.request.get("fileCategory").equals("")) {
			this.addFieldError("fileCategory", this.getText("DMS.fileCategory.Required"));
		}
		if ((this.request.get("fileHeading") == null) || this.request.get("fileHeading").equals("")) {
			this.addFieldError("fileHeading", this.getText("DMS.fileHeading.Required"));
		}
		if ((httpRequest.getParameter("sender.userSource") == null) || httpRequest.getParameter("sender.userSource").trim().intern().equals("")) {
			this.addFieldError("sender.userSource", this.getText("sender.userSource.required"));
		}
		if ((httpRequest.getParameter("sender.userName") == null) || httpRequest.getParameter("sender.userName").trim().intern().equals("")) {
			this.addFieldError("sender.userName", this.getText("sender.userName.required"));
		}
		if (this.notificationGroups.isEmpty()) {
			this.addFieldError("notificationGroups", this.getText("notificationGroups.required"));
		}
	}
	
	/**
	 * Sets the file mgmt service.
	 * @param fileMgmtService the new file mgmt service
	 */
	public void setFileMgmtService(final FileManagementService fileMgmtService) {
		this.fileMgmtService = fileMgmtService;
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
	
	public void setNotificationFilePersistenceService(
			PersistenceService<NotificationFile, Long> notificationFilePersistenceService) {
		this.notificationFilePersistenceService = notificationFilePersistenceService;
	}

	public void setNotificationGroupPersistenceService(
			PersistenceService<NotificationGroup, Long> notificationGroupPersistenceService) {
		this.notificationGroupPersistenceService = notificationGroupPersistenceService;
	}

	public void setNotificationPersistenceService(
			PersistenceService<Notification, Long> notificationPersistenceService) {
		this.notificationPersistenceService = notificationPersistenceService;
	}

	/**
	 * @return the notificationGroups
	 */
	public List<Long> getNotificationGroupsIds() {
		return this.notificationGroupIds;
	}
	
	/**
	 * @param notificationGroups the notificationGroups to set
	 */
	public void setNotificationGroupsIds(final List<Long> notificationGroupsIds) {
		this.notificationGroups.clear();
		for (final Long notifyGrpId : notificationGroupsIds) {
			this.notificationGroups.add((NotificationGroup) this.notificationGroupPersistenceService.findById(notifyGrpId, false));
		}
	}
	
	public List<String> getFileComment() {
		return this.fileComment;
	}
	
	public List<String> getFileNames() {
		return this.fileNames;
	}
	
	public boolean getDeleteAll() {
		return this.deleteAll;
	}
	
	public void setDeleteAll(final boolean deleteAll) {
		this.deleteAll = deleteAll;
	}
	
	public void setId(final Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return this.id;
	}
	
	public void setEisService(final EISServeable eisService) {
		this.eisService = eisService;
	}
	
	/**
	 * @return the mode
	 */
	public String getMode() {
		return this.mode;
	}
	
	/**
	 * @param mode the mode to set
	 */
	public void setMode(final String mode) {
		this.mode = mode;
	}
	
	/**
	 * @return the pagedResults
	 */
	public PaginatedList getPagedResults() {
		return this.pagedResults;
	}
	
	/**
	 * @param pagedResults the pagedResults to set
	 */
	public void setPagedResults(final PaginatedList pagedResults) {
		this.pagedResults = pagedResults;
	}
	
	/**
	 * @param page the page to set
	 */
	public void setPage(final int page) {
		this.page = page;
	}
	
	/**
	 * @return the reportSize
	 */
	public int getReportSize() {
		return this.reportSize;
	}
	
	/**
	 * @param reportSize the reportSize to set
	 */
	public void setReportSize(final int reportSize) {
		this.reportSize = reportSize;
	}
	
	/**
	 * @return the page
	 */
	public int getPage() {
		return this.page;
	}
	
	public void setAuditEventService(AuditEventService auditEventService) {
		this.auditEventService = auditEventService;
	}

	protected void doAuditing(String action) {
		final NotificationFile notificationFile = (NotificationFile)getModel();
		final String details = new StringBuffer("[Notification Date : ").append(DateUtils.getDefaultFormattedDate(notificationFile.getFileDate())).
								append(", File Category : ").append(notificationFile.getFileCategory().getName()).append(", File Sub-Category : ").
								append(notificationFile.getFileSubcategory() == null ? "" : notificationFile.getFileSubcategory().getName()).
								append(", File Heading : ").append(notificationFile.getFileHeading()).append(", Receiver Group : ").append(notificationFile.getNotificationGroups()).append(" ]").toString();
		final AuditEvent auditEvent = new AuditEvent(AuditModule.DMS, AuditEntity.DMS_FILENOTIFICATION, action, notificationFile.getFileNumber(), details);
		auditEvent.setPkId(notificationFile.getId());
		this.auditEventService.createAuditEvent(auditEvent, NotificationFile.class);
	}
}
