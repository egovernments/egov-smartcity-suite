/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
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
