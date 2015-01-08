/*
 * @(#)AjaxFileDownloadAction.java 3.0, 14 Jun, 2013 1:36:58 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.web.actions.docmgmt;

import java.io.InputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.infstr.docmgmt.AssociatedFile;
import org.egov.infstr.docmgmt.DocumentManagerService;
import org.egov.infstr.docmgmt.DocumentObject;
import org.egov.web.actions.BaseFormAction;

import com.opensymphony.xwork2.Action;

@Results({ @Result(name = Action.SUCCESS, type = "stream", location = "fileStream", params = { "contentType", "${contentType}", "contentDisposition", "attachment; filename=${fileName}" }),
		@Result(name = "RENDER_NOTICE", type = "redirect", location = "/commons/htmlFileRenderer.jsp") })
public class AjaxFileDownloadAction extends BaseFormAction {

	private static final long serialVersionUID = 1L;
	private String contentType;
	private String fileName;
	private InputStream fileStream;
	private String docNumber;
	private String moduleName;
	private Long contentLength;

	private transient DocumentManagerService<DocumentObject> documentManagerService;

	public String getContentType() {
		return this.contentType;
	}

	public Long getContentLength() {
		return this.contentLength;
	}

	public String getFileName() {
		return this.fileName;
	}

	@Override
	public Object getModel() {
		return null;
	}

	public InputStream getFileStream() {
		return this.fileStream;
	}

	public String getDocNumber() {
		return this.docNumber;
	}

	public void setDocNumber(final String docNumber) {
		this.docNumber = docNumber;
	}

	public void setDocumentManagerService(final DocumentManagerService<DocumentObject> documentManagerService) {
		this.documentManagerService = documentManagerService;
	}

	public void setFileName(final String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String execute() {
		final AssociatedFile file = this.documentManagerService.getFileFromDocumentObject(this.docNumber, this.moduleName, this.fileName);
		if (file == null) {
			addActionError(getText("DocMngr.file.unavailable"));
			return Action.ERROR;
		}
		this.fileStream = file.getFileInputStream();
		this.contentType = file.getMimeType();
		this.contentLength = file.getLength();
		return SUCCESS;
	}

	public String downloadNotice() {
		final AssociatedFile file = this.documentManagerService.getFileFromDocumentObject(this.docNumber, this.moduleName, this.fileName);
		if (file == null) {
			addActionError(getText("DocMngr.file.unavailable"));
			return Action.ERROR;
		}
		ServletActionContext.getServletContext().setAttribute("fileStream", file.getFileInputStream());
		return "RENDER_NOTICE";
	}

	@Override
	public void validate() {

		if (this.docNumber == null || StringUtils.isBlank(this.docNumber)) {
			addActionError(getText("docNumber.missing", "Document number is missing"));
		}

		if (this.moduleName == null || StringUtils.isBlank(this.moduleName)) {
			addActionError(getText("moduleName.missing", "Module name is missing"));
		}

		if (this.fileName == null || StringUtils.isBlank(this.fileName)) {
			addActionError(getText("fileName.missing", "File name is missing"));
		}
	}

	public void setModuleName(final String moduleName) {
		this.moduleName = moduleName;
	}

}
