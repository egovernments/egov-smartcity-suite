/*
 * @(#)ComplaintGroupMasterAction.java 3.0, 23 Jul, 2013 3:29:42 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pgr.web.actions.masters;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.pgr.domain.entities.ComplaintGroup;
import org.egov.pgr.domain.services.ComplaintGroupService;
import org.egov.web.actions.BaseFormAction;

import com.opensymphony.xwork2.validator.annotations.Validations;

@ParentPackage("egov")
@Validations
public class ComplaintGroupMasterAction extends BaseFormAction {

	private static final long serialVersionUID = 1;
	private final static Logger LOGGER = Logger.getLogger(ComplaintGroupMasterAction.class);
	private ComplaintGroup complaintGroup = new ComplaintGroup();
	private ComplaintGroupService complaintGroupService;

	@SkipValidation
	public String edit() {
		if (this.complaintGroup.getId() != null) { 
			this.complaintGroup = complaintGroupService.findById(this.complaintGroup.getId(),false); 
		}
		return EDIT;
	}

	@Override
	@SkipValidation
	public String execute() throws Exception {
		return list();
	}

	@SkipValidation
	public String list() {
		return INDEX;
	}

	@SkipValidation
	public String newform() {
		return NEW;
	}

	public String create() {
		this.complaintGroupService.create(this.complaintGroup);
		return SUCCESS;
	}

	public String save() {
		this.complaintGroupService.update(this.complaintGroup);
		return SUCCESS;
	}

	/**
	 * Validation for Complaint Group Name
	 **/
	@Override
	public void validate() {
		try {
			if (this.complaintGroupService.isExist("complaintGroupName", this.complaintGroup.getComplaintGroupName())) { 
				addActionError(getText("Masters.complaintgroup.duplicate.error")); 
			}	

		} catch (final Exception e) {
			addActionError(getText("Masters.complaintgroup.duplicate.error"));
			LOGGER.error(e);
		}
	}

	/**
	 * To get the Model Object
	 **/
	@Override
	public Object getModel() {
		return this.complaintGroup;
	}

	public List<ComplaintGroup> getComplaintGroupList() {
		return this.complaintGroupService.findAll();
	}

	public void setComplaintGroupService(ComplaintGroupService complaintGroupService) {
		this.complaintGroupService = complaintGroupService;
	}
}
