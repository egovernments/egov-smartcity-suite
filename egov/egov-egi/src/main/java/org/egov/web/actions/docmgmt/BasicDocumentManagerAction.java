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

import static org.egov.infstr.utils.StringUtils.hasSpecialChars;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.jcr.RepositoryException;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.docmgmt.AssociatedFile;
import org.egov.infstr.docmgmt.DocumentManagerService;
import org.egov.web.annotation.ValidationErrorPage;
import org.egov.web.annotation.ValidationErrorPageExt;

@ParentPackage("egov")
public class BasicDocumentManagerAction extends DocumentManagerAction {

	private static final long serialVersionUID = 1L;

	/**
	 * {@inheritDoc}
	 **/
	@Override
	public String execute() throws RuntimeException {
		return super.execute();
	}

	/**
	 * {@inheritDoc}
	 **/
	@Override
	@ValidationErrorPage(SUCCESS)
	public String addDocument() throws IllegalAccessException, RepositoryException, RuntimeException {
		return super.addDocument();
	}

	/**
	 * {@inheritDoc}
	 **/
	@Override
	@ValidationErrorPageExt(action = SUCCESS, makeCall = true, toMethod = "editDocument")
	public String updateDocument() throws IllegalAccessException, RepositoryException, RuntimeException {
		return super.updateDocument();
	}

	/**
	 * {@inheritDoc}
	 **/
	@Override
	public String editDocument() throws RuntimeException {
		return super.editDocument();
	}

	/**
	 * {@inheritDoc}
	 **/
	@Override
	@ValidationErrorPage(SUCCESS)
	public String viewDocument() {
		return super.viewDocument();
	}

	/**
	 * {@inheritDoc}
	 **/
	@Override
	public String search() throws RuntimeException {
		return super.search();
	}

	/**
	 * {@inheritDoc}
	 **/
	@Override
	public String searchDocument() throws RuntimeException, IOException {
		return super.searchDocument();
	}

	/**
	 * {@inheritDoc}
	 **/
	@Override
	protected List<String> removeAssociatedFiles(final Set<AssociatedFile> associatedFiles) {
		return super.removeAssociatedFiles(associatedFiles);
	}

	/**
	 * {@inheritDoc}
	 **/
	@Override
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
					final String extension = fileName.substring(fileName.lastIndexOf("."));
					if (extension.startsWith(".exe") || mimeTypes.get(index).contains("exe") || mimeTypes.get(index).contains("octet-stream")) {
						LOG.debug("This filetype is not allowed - extension = " + extension + " ,mimetype = " + mimeTypes.get(index));
						fileItr.remove();
						invalidFile.add(fileName);
						continue;
					} else {
						if (hasSpecialChars(fileName)) {
							fileItr.remove();
							this.addActionMessage(this.getText("DocMngr.File.NameInvalid") + fileName);
							continue;
						}
						final AssociatedFile associatedFile = new AssociatedFile();
						FileInputStream fileStream = null;
						try {
							fileStream = new FileInputStream(uploadFiles.get(index));
						} catch (final FileNotFoundException exp) {
							fileItr.remove();
							this.addActionMessage(this.getText("DocMngr.File.NotValid") + fileName);
							continue;
						}
						associatedFile.setFileInputStream(fileStream);
						associatedFile.setFileName(fileName);
						associatedFile.setMimeType(mimeTypes.get(index));
						associatedFile.setLength(uploadFiles.get(index).length());
						associatedFile.setCreatedDate(currentDate);
						associatedFile.setModifiedDate(currentDate);
						associatedFile.setCreatedBy(Integer.valueOf(EGOVThreadLocals.getUserId()));
						associatedFile.setModifiedBy(Integer.valueOf(EGOVThreadLocals.getUserId()));
						associatedFile.setRemarks("");
						if (!associatedFiles.add(associatedFile)) {
							throw new ValidationException(DocumentManagerService.PROP_DOC_NUM, "DocMngr.File.Exist", associatedFile.getFileName());
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
}
