/*
 * @(#)DocumentManagerAction.java 3.0, 14 Jun, 2013 1:22:00 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.web.actions.docmgmt;

import static org.egov.infstr.docmgmt.DocumentManagerService.PROP_DOC_NUM;
import static org.egov.infstr.docmgmt.DocumentManagerService.PROP_MODULE_NAME;
import static org.egov.infstr.utils.DateUtils.getFormattedDate;
import static org.egov.infstr.utils.StringUtils.escapeSpecialChars;
import static org.egov.infstr.utils.StringUtils.hasSpecialChars;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
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
import java.util.Set;

import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.docmgmt.AssociatedFile;
import org.egov.infstr.docmgmt.DocumentManagerService;
import org.egov.infstr.docmgmt.DocumentObject;
import org.egov.infstr.docmgmt.documents.Notice;
import org.egov.infstr.services.PersistenceService;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.UserImpl;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.egov.web.annotation.ValidationErrorPageExt;

/**
 * Document Management User Interface to interact with {@link DocumentManagerService} to accomplish Document Management.
 */
@ParentPackage("egov")
public class DocumentManagerAction extends BaseFormAction {

	private static final long serialVersionUID = 1L;
	protected static final Logger LOG = LoggerFactory.getLogger(DocumentManagerAction.class);
	protected transient DocumentObject documentObject = new DocumentObject();
	protected transient List<String> fileInfo = new ArrayList<String>();
	protected transient List<String> fileCaption = new ArrayList<String>();
	protected transient List<String> fileNames = new ArrayList<String>();
	protected transient String docNumber;
	protected transient String moduleName;
	protected transient String actionCommand;
	protected static final String SEARCH_ACTION = "search";
	protected transient DocumentManagerService<DocumentObject> docManagerService;
	protected transient PersistenceService<UserImpl, Integer> userPersistenceService;

	/**
	 * Sets the doc number.
	 * @param docNumber the new doc number
	 */
	public void setDocNumber(final String docNumber) {
		this.docNumber = docNumber;
	}

	/**
	 * Gets the module name.
	 * @return the module name
	 */
	public String getModuleName() {
		return this.moduleName;
	}

	/**
	 * Sets the module name.
	 * @param moduleName the new module name
	 */
	public void setModuleName(final String moduleName) {
		this.moduleName = moduleName;
	}

	/**
	 * Gets the file info.
	 * @return the file info
	 */
	public List<String> getFileInfo() {
		return this.fileInfo;
	}

	/**
	 * Sets the file info.
	 * @param fileInfo the new file info
	 */
	public void setFileInfo(final List<String> fileInfo) {
		this.fileInfo = fileInfo;
	}

	/**
	 * Sets the file caption.
	 * @param fileCaption the new file caption
	 */
	public void setFileCaption(final List<String> fileCaption) {
		this.fileCaption = fileCaption;
	}

	/**
	 * Gets the action command.
	 * @return the action command
	 */
	public String getActionCommand() {
		return this.actionCommand;
	}

	/**
	 * Sets the action command.
	 * @param actionCommand the new action command
	 */
	public void setActionCommand(final String actionCommand) {
		this.actionCommand = actionCommand;
	}

	/**
	 * Gets the file names.
	 * @return the file names
	 */
	public List<String> getFileNames() {
		return this.fileNames;
	}

	/**
	 * Sets the file names.
	 * @param fileNames the new file names
	 */
	public void setFileNames(final List<String> fileNames) {
		this.fileNames = fileNames;
	}

	/**
	 * Sets the user service.
	 * @param userService the user service
	 */
	public void setUserPersistenceService(final PersistenceService<UserImpl, Integer> userPersistenceService) {
		this.userPersistenceService = userPersistenceService;
	}

	/**
	 * Sets the document manager service.
	 * @param docManagerService the new document manager service
	 */
	public void setDocumentManagerService(final DocumentManagerService<DocumentObject> docManagerService) {
		this.docManagerService = docManagerService;
	}

	/*
	 * (non-Javadoc)
	 * @see com.opensymphony.xwork2.ModelDriven#getModel()
	 */
	@Override
	public DocumentObject getModel() {
		return this.documentObject;
	}

	/*
	 * (non-Javadoc)
	 * @see org.egov.web.actions.BaseFormAction#prepare()
	 */
	@Override
	public void prepare() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("No Prepare implementation");
		}
	}

	/**
	 * Populates Document Manger view in ADD mode.
	 * @return String view name
	 * @throws RuntimeException the runtime exception
	 */
	@Override
	public String execute() throws RuntimeException {
		boolean hasDocNum = ((this.docNumber != null) && !this.docNumber.isEmpty());
		final boolean hasModuleName = ((this.moduleName != null) && !this.moduleName.isEmpty());
		// If Auto generation value is Y then set Msg for DocNumber and generates DocNumber while
		// saving
		if (!hasDocNum && this.docManagerService.isAutoGenerateDocNumber()) {
			this.docNumber = this.getText("DocMngr.AutoDocNo.Msg");
			hasDocNum = true;
		}
		// If document number is not provided or auto generated show view with action error
		if (!hasDocNum) {
			this.addFieldError(PROP_DOC_NUM, this.getText("DocMngr.DocNum.Missing"));
			this.actionCommand = "";
		}
		if (!hasModuleName) {
			this.addFieldError("moduleName", this.getText("DocMngr.ModuleName.Missing"));
			this.actionCommand = "";
		} else if (hasDocNum && hasModuleName) {
			// Setting form submit command to call addDocument method in this Action.
			this.documentObject.setDocumentNumber(this.docNumber);
			this.documentObject.setModuleName(this.moduleName);
			this.actionCommand = "!addDocument";
		}
		return SUCCESS;
	}

	/**
	 * Saves when user clicks Save button in the ADD mode.
	 * @return String view
	 * @throws IllegalAccessException the illegal access exception
	 * @throws RepositoryException the repository exception
	 * @throws RuntimeException the runtime exception
	 */
	@ValidationErrorPage(SUCCESS)
	public String addDocument() throws IllegalAccessException, RepositoryException, RuntimeException {
		final List<String> addedFileNames = this.addAssociatedFiles(this.documentObject.getAssociatedFiles());
		if (this.getText("DocMngr.AutoDocNo.Msg").equals(this.documentObject.getDocumentNumber()) && this.docManagerService.isAutoGenerateDocNumber()) {
			this.documentObject.setDocumentNumber(this.docManagerService.generateDocumentNumber());
		}
		this.docNumber = this.documentObject.getDocumentNumber();
		this.documentObject = this.docManagerService.addDocumentObject(this.documentObject);
		this.addActionMessage(this.getText("DocMngr.Create.Success") + this.documentObject.getDocumentNumber());
		if (!addedFileNames.isEmpty()) {
			this.addActionMessage(this.getText("DocMngr.FileAttach.Success") + addedFileNames);
		}
		// go to edit mode
		return this.editDocument();
	}

	/**
	 * When user clicks Submit button from Edit mode this method will be invoked and handles file attachments.
	 * @return String view
	 * @throws IllegalAccessException the illegal access exception
	 * @throws RepositoryException the repository exception
	 * @throws RuntimeException the runtime exception
	 */
	@ValidationErrorPageExt(action = SUCCESS, makeCall = true, toMethod = "editDocument")
	public String updateDocument() throws IllegalAccessException, RepositoryException, RuntimeException {
		final String searchTag = this.documentObject.getTags();
		this.docNumber = this.documentObject.getDocumentNumber();
		this.documentObject = this.docManagerService.getDocumentObject(this.documentObject.getDocumentNumber(), this.documentObject.getModuleName());
		this.documentObject.setTags(searchTag);
		final List<String> removedFileNames = this.removeAssociatedFiles(this.documentObject.getAssociatedFiles());
		final List<String> addedFileNames = this.addAssociatedFiles(this.documentObject.getAssociatedFiles());
		this.docManagerService.updateDocumentObject(this.documentObject);
		if (!addedFileNames.isEmpty()) {
			this.addActionMessage(this.getText("DocMngr.FileAttach.Success") + addedFileNames);
		}
		if (!removedFileNames.isEmpty()) {
			this.addActionMessage(this.getText("DocMngr.FileDetach.Success") + removedFileNames);
		}
		this.addActionMessage(this.getText("DocMngr.Update.Success") + this.documentObject.getDocumentNumber());
		return this.editDocument();
	}

	/**
	 * Called when user ask for Document Object for editing normally re-attaching or adding files.
	 * @return String view
	 * @throws RuntimeException the runtime exception
	 */
	public String editDocument() throws RuntimeException {
		this.viewDocument(); // Call to viewDocument method to prepare Document Object before
		// editing
		this.actionCommand = "!updateDocument"; // Setting form submit command to call
		// attachDocument method in this Action.
		return SUCCESS;
	}

	/**
	 * Populates Document Manager view in VIEW mode for the given Document Number.
	 * @return String view
	 * @throws RuntimeException the runtime exception
	 */
	@ValidationErrorPage(SUCCESS)
	public String viewDocument() throws RuntimeException {
		if ((this.docNumber == null) || this.docNumber.isEmpty()) {
			this.addFieldError(PROP_DOC_NUM, this.getText("DocMngr.DocNum.Missing"));
		} else {
			// Getting the Document object from JackRabbit Repository using the Document Number
			// passed
			try {
				if (this.documentObject.getJcrUUID() == null) {
					this.documentObject = this.docManagerService.getDocumentObject(this.docNumber, this.moduleName);
				} else {
					this.documentObject = this.docManagerService.getDocumentObjectByUuid(this.documentObject.getJcrUUID());
				}
			} catch (final ValidationException e) {
				if (this.documentObject.getJcrUUID() == null) {
					throw e;
				}
			}
			if (this.documentObject == null) {
				throw new ValidationException(PROP_DOC_NUM, "DocMngr.DocNum.NotExist", this.docNumber);
			}
			this.fileNames.clear();
			this.fileCaption.clear();
			this.fileInfo.clear();
			final Set<AssociatedFile> files = this.documentObject.getAssociatedFiles();
			for (final AssociatedFile srcFile : files) {
				this.fileNames.add(srcFile.getFileName());
				this.fileInfo.add(this.loadFileInfo(srcFile));
			}
			this.actionCommand = "";
		}
		return SUCCESS;
	}

	/**
	 * Constructing file information for view.
	 * @param srcFile the src file
	 * @return String fileInfo
	 * @throws RuntimeException the runtime exception
	 */
	protected String loadFileInfo(final AssociatedFile srcFile) throws RuntimeException {
		final StringBuilder fileInf = new StringBuilder();
		final User createdUser = this.userPersistenceService.findById(srcFile.getCreatedBy(), false);
		final User modifiedUser = this.userPersistenceService.findById(srcFile.getModifiedBy(), false);
		final String lineBreak = "<br/>";
		fileInf.append("<b>File Name : </b>").append(srcFile.getFileName()).append(lineBreak);
		fileInf.append("<b>Created By : </b>").append(createdUser.getFirstName()).append(" ").append(createdUser.getLastName() == null ? "" : createdUser.getLastName()).append("<br/>");
		fileInf.append("<b>Created Date : </b>").append(srcFile.getCreatedDate()).append(lineBreak);
		fileInf.append("<b>Modified By : </b>").append(modifiedUser.getFirstName()).append(" ").append(modifiedUser.getLastName() == null ? "" : modifiedUser.getLastName()).append("<br/>");
		fileInf.append("<b>Modified Date : </b>").append(srcFile.getModifiedDate()).append(lineBreak).append(lineBreak);
		fileInf.append("<b>File Remarks : </b>").append(lineBreak).append(srcFile.getRemarks());
		return fileInf.toString();
	}

	/**
	 * For adding uploaded files.
	 * @param associatedFiles the associated files
	 * @return the list
	 * @throws RuntimeException the runtime exception
	 * @List of added fileNames
	 */
	protected List<String> addAssociatedFiles(final Set<AssociatedFile> associatedFiles) throws RuntimeException {
		if (ServletActionContext.getRequest() instanceof MultiPartRequestWrapper) {
			final MultiPartRequestWrapper multiWrapper = (MultiPartRequestWrapper) ServletActionContext.getRequest();
			if (multiWrapper.getFiles("upload") != null) {
				final List<File> uploadFiles = new ArrayList<File>(Arrays.asList(multiWrapper.getFiles("upload")));
				final List<String> fileNames = new ArrayList<String>(Arrays.asList(multiWrapper.getFileNames("upload")));
				final List<String> mimeTypes = new ArrayList<String>(Arrays.asList(multiWrapper.getContentTypes("upload")));
				final Iterator<String> fileItr = fileNames.iterator();
				int index = -1;
				final Date currentDate = new Date();
				final List<String> invalidFile = new ArrayList<String>();
				final List<String> tempFileName = new ArrayList<String>();
				while (fileItr.hasNext()) {
					index++;
					final String fileName = fileItr.next();
					if (tempFileName.contains(fileName)) {
						fileItr.remove();
						this.addActionMessage(this.getText("DocMngr.FileAttach.Skip") + fileName);
						continue;
					} else {
						tempFileName.add(fileName);
					}
					final String extension = fileName.substring(fileName.lastIndexOf('.'));
					if (extension.startsWith(".exe") || mimeTypes.get(index).contains("exe") || mimeTypes.get(index).contains("octet-stream")) {
						LOG.debug("This filetype is not allowed - extension = " + extension + " ,mimetype = " + mimeTypes.get(index));
						fileItr.remove();
						invalidFile.add(fileName);
						continue;
					} else {
						final AssociatedFile associatedFile = new AssociatedFile();
						FileInputStream fileStream = null;
						if ((this.fileCaption.get(index) == null) || this.fileCaption.get(index).isEmpty()) {
							associatedFile.setFileName(fileName);
						} else {
							associatedFile.setFileName(new StringBuffer(this.fileCaption.get(index)).append(extension).toString());
						}
						if (hasSpecialChars(associatedFile.getFileName())) {
							fileItr.remove();
							this.addActionMessage(this.getText("DocMngr.File.NameInvalid") + associatedFile.getFileName());
							continue;
						}
						try {
							fileStream = new FileInputStream(uploadFiles.get(index));
						} catch (final FileNotFoundException exp) {
							fileItr.remove();
							this.addActionMessage(this.getText("DocMngr.File.NotValid") + associatedFile.getFileName());
							continue;
						}
						associatedFile.setFileInputStream(fileStream);
						associatedFile.setMimeType(mimeTypes.get(index));
						associatedFile.setLength(uploadFiles.get(index).length());
						associatedFile.setCreatedDate(currentDate);
						associatedFile.setModifiedDate(currentDate);
						associatedFile.setCreatedBy(Integer.valueOf(EGOVThreadLocals.getUserId()));
						associatedFile.setModifiedBy(Integer.valueOf(EGOVThreadLocals.getUserId()));
						if (this.fileInfo.get(index).equals("enter file remarks")) {
							associatedFile.setRemarks(" ");
						} else {
							associatedFile.setRemarks(this.fileInfo.get(index));
						}
						if (!associatedFiles.add(associatedFile)) {
							throw new ValidationException(PROP_DOC_NUM, "DocMngr.File.Exist", associatedFile.getFileName());
						}
					}
				}
				if (!invalidFile.isEmpty()) {
					this.addActionMessage(this.getText("DocMngr.FileAttach.Invalid") + invalidFile);
				}
				return fileNames;
			}
		}
		return Collections.emptyList();
	}

	/**
	 * For removing files from associated files.
	 * @param associatedFiles the associated files
	 * @return the list
	 * @throws RuntimeException the runtime exception
	 * @List of removed fileNames
	 */
	protected List<String> removeAssociatedFiles(final Set<AssociatedFile> associatedFiles) throws RuntimeException {
		// If Files are marked for delete / detach from Document and delete files copied to the
		// download resources
		if (!this.fileNames.isEmpty()) {
			final AssociatedFile file = new AssociatedFile();
			for (final String fileName : this.fileNames) {
				file.setFileName(fileName);
				associatedFiles.remove(file);
			}
		}
		return this.fileNames;
	}

	/* FOR GENERIC SEARCH DOCUMENT */
	/** The type. */
	protected String type;

	/**
	 * Sets the type.
	 * @param type the new type
	 */
	public void setType(final String type) {
		this.type = type;
	}

	/**
	 * Gets the type.
	 * @return the type
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * Calls to search page.
	 * @return the string
	 * @throws RuntimeException the runtime exception
	 */
	public String search() throws RuntimeException {
		return SEARCH_ACTION + this.type;// returns view based on the type passed.
	}

	/**
	 * On Search button clicks calls this function.
	 * @return the string
	 * @throws RuntimeException the runtime exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public String searchDocument() throws RuntimeException, IOException {
		final HttpServletResponse httpResponse = ServletActionContext.getResponse();
		try {
			if (this.type.isEmpty() || this.moduleName.isEmpty()) {
				LOG.warn("Module Name or Type is missing in the request Parameter ");
				httpResponse.getWriter().write("");
			} else {
				final HttpServletRequest request = ServletActionContext.getRequest();
				final Class<?> searchClass = this.getSearchClass(this.type);
				final BeanInfo beanInfo = java.beans.Introspector.getBeanInfo(searchClass);
				final PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
				final HashMap<String, String> searchArgs = new HashMap<String, String>();
				for (final PropertyDescriptor propDesc : props) {
					final String[] searchVal = request.getParameterValues(propDesc.getName());
					if ((searchVal != null) && (searchVal.length != 0)) {
						if ((searchVal.length > 1) && !searchVal[0].isEmpty() && !searchVal[1].isEmpty()) {
							searchArgs.put(propDesc.getName(), searchVal[0] + "#" + searchVal[1]);
						} else if (!searchVal[0].isEmpty()) {
							searchArgs.put(propDesc.getName(), searchVal[0]);
						}
					}
				}
				if (searchArgs.isEmpty()) {
					httpResponse.getWriter().write("");
				} else {
					searchArgs.put(PROP_MODULE_NAME, this.moduleName);
					final List<DocumentObject> documentObjects = this.docManagerService.searchDocumentObject(searchClass, searchArgs);
					if (documentObjects.isEmpty()) {
						httpResponse.getWriter().write("");
					} else {
						final String resultData = this.loadSearchData(documentObjects).toString();
						httpResponse.getWriter().write(resultData);
					}
				}
			}
		} catch (final Exception e) {
			httpResponse.getWriter().write("");
			LOG.error("Error occurred while searching Document of Type : " + this.type, e);
		}
		return null;
	}

	/**
	 * Constructs json string based on the search result.
	 * @param documentObjects the document objects
	 * @return StringBuffer json search data string
	 * @throws RuntimeException the runtime exception
	 */
	protected StringBuilder loadSearchData(final List<DocumentObject> documentObjects) throws RuntimeException {
		final StringBuilder searchItem = new StringBuilder();
		searchItem.append("[");
		final StringBuilder fileNames = new StringBuilder();
		for (final DocumentObject documentObject : documentObjects) {
			final Set<AssociatedFile> files = documentObject.getAssociatedFiles();
			fileNames.setLength(0);
			if (!files.isEmpty()) {
				final Iterator<AssociatedFile> filitr = files.iterator();
				while (filitr.hasNext()) {
					fileNames.append(filitr.next().getFileName()).append("^~");
				}
				fileNames.delete(fileNames.length() - 2, fileNames.length());
			}
			if (documentObject instanceof Notice) {
				final Notice notice = (Notice) documentObject;
				searchItem.append("{documentNumber:'<a href=\"/egi/docmgmt/ajaxFileDownload!downloadNotice.action?docNumber=").append(notice.getDocumentNumber()).append("&fileName=");
				searchItem.append(fileNames).append("&moduleName=").append(this.moduleName).append("\" target=\"_blank\">").append(notice.getDocumentNumber()).append("</a>',");
				searchItem.append("noticeType:'").append(notice.getNoticeType()).append("',");
				searchItem.append("noticeDate:'").append(getFormattedDate(notice.getNoticeDate(), "dd/MM/yyyy hh:mm a")).append("',");
				searchItem.append("associatedObjectId:'").append(notice.getAssociatedObjectId() == null ? "" : notice.getAssociatedObjectId()).append("',");
				searchItem.append("addressedTo:'").append(notice.getAddressedTo() == null ? "" : escapeSpecialChars(notice.getAddressedTo())).append("',");
				searchItem.append("address:'").append(notice.getAddress() == null ? "" : escapeSpecialChars(notice.getAddress())).append("',");
				searchItem.append("files:'<img src=\"../images/download.gif\" onclick=\"showDownloadList(event)\"/><input type=\"hidden\" value=\"").append(notice.getDocumentNumber() + "#" + fileNames.toString()).append("\"/>'},");
			}
		}
		searchItem.deleteCharAt(searchItem.length() - 1);
		searchItem.append("]");
		return searchItem;
	}

	/**
	 * Support method for display Associated file using Tag lib.
	 * @return jsonString of file information
	 * @throws RuntimeException the runtime exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public String viewDocumentObjectFiles() throws RuntimeException, IOException {
		final DocumentObject docObject = this.docManagerService.getDocumentObject(this.docNumber, this.moduleName);
		final StringBuilder response = new StringBuilder();
		response.append("[");
		if (docObject != null) {
			for (final AssociatedFile file : docObject.getAssociatedFiles()) {
				response.append("{fileName:'").append(file.getFileName()).append("'");
				response.append(",docNumber:'").append(docObject.getDocumentNumber()).append("'");
				response.append("},");
			}
			response.deleteCharAt(response.length() - 1);
		}
		response.append("]");
		final HttpServletResponse httpResponse = ServletActionContext.getResponse();
		httpResponse.getWriter().write(response.toString());
		return null;
	}

	/**
	 * Gets the search class.
	 * @param forType the for type
	 * @return the search class
	 * @throws RuntimeException the runtime exception
	 * @throws ClassNotFoundException the class not found exception
	 */
	private Class<?> getSearchClass(final String forType) throws RuntimeException, ClassNotFoundException {
		Class<?> searchType = null;
		try {
			searchType = Class.forName("org.egov.infstr.docmgmt.documents." + forType, true, Thread.currentThread().getContextClassLoader());
		} catch (final ClassNotFoundException e) {
			searchType = Class.forName("org.egov.infstr.docmgmt." + forType, true, Thread.currentThread().getContextClassLoader());
		}
		return searchType;
	}
}
