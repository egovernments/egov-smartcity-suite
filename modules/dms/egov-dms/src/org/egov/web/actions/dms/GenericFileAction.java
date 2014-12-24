/*
 * @(#)GenericFileAction.java 3.0, 16 Jul, 2013 11:34:51 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.web.actions.dms;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.egov.dms.models.GenericFile;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.web.actions.BaseFormAction;

/**
 * this is wrapper class to redirect to respective actions based on file type
 */
//using ActionChainResult type to get request params across action chain
@ParentPackage("egov")
@Result(name = "new", type = "chain", location = "${actionName}", params = {"namespace", "/dms", "method", "viewFile" })
public class GenericFileAction extends BaseFormAction {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(GenericFileAction.class);

	private String fileNumber;
	private String actionName;
	private Long fileId;
	private final Map<String, String> actionMap = new HashMap<String, String>();
	private GenericFile genericFile;

	@Override
	public Object getModel() {
		return this.genericFile;
	}

	public GenericFileAction() {
		this.actionMap.put("INTERNAL", "internalFileManagement");
		this.actionMap.put("INBOUND", "inboundFileManagement");
		this.actionMap.put("OUTBOUND", "outboundFileManagement");
	}

	@Override
	public void prepare() {
		super.prepare();

		this.genericFile = new GenericFile();
		final GenericFile file = (GenericFile) HibernateUtil
				.getCurrentSession().get(GenericFile.class, this.fileId);
		if (file == null) {
			this.addActionError(getText("err.filentexist") + this.fileId);
		}
		this.genericFile = file;
		setActionName(this.actionMap.get(file.getFileType().trim()));
	}

	@Override
	public String execute() {
		LOGGER.info("execute");
		return NEW;
	}

	public String getFileNumber() {
		return this.fileNumber;
	}

	public void setFileNumber(final String fileNumber) {
		this.fileNumber = fileNumber;
	}

	public String getActionName() {
		return this.actionName;
	}

	public void setActionName(final String actionName) {
		this.actionName = actionName;
	}

	public void validation() {
		if (this.fileId == null || this.fileId.equals("")) {
			this.addActionError(getText("err.fileidreq"));
		}
	}

	public Long getFileId() {
		return this.fileId;
	}

	public void setFileId(final Long fileId) {
		this.fileId = fileId;
	}
}
