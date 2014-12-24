/*
 * @(#)ComplaintTypeMasterAction.java 3.0, 23 Jul, 2013 3:29:42 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pgr.web.actions.masters;

import java.util.List;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.lib.rjbac.dept.Department;
import org.egov.pgr.domain.entities.Category;
import org.egov.pgr.domain.entities.ComplaintGroup;
import org.egov.pgr.domain.entities.ComplaintTypes;
import org.egov.pgr.domain.services.CategoryService;
import org.egov.pgr.domain.services.ComplaintGroupService;
import org.egov.pgr.domain.services.ComplaintTypeService;
import org.egov.pgr.utils.PgrCommonUtils;
import org.egov.web.actions.BaseFormAction;

import com.opensymphony.xwork2.validator.annotations.Validations;

@ParentPackage("egov")
@Validations
public class ComplaintTypeMasterAction extends BaseFormAction {
	private static final long serialVersionUID = 2;
	private ComplaintTypes complaintTypes = new ComplaintTypes();
	private ComplaintTypeService complaintTypeService;
	private ComplaintGroupService complaintGroupService;
	private CategoryService categoryService;
	private PgrCommonUtils pgrCommonUtils;
	
	@SkipValidation
	public String newform() {
		return NEW;
	}

	@SkipValidation
	public String edit() {
		this.complaintTypes = this.complaintTypeService.findById(this.complaintTypes.getId(), false);
		return EDIT;
	}

	/**
	 * ComplaintGroup list, Complaint Department list & category lists
	 */

	@Override
	@SkipValidation
	public String execute() throws Exception {
		return list();
	}

	@SkipValidation
	public String list() {
		return INDEX;
	}

	public String create() {
		this.complaintTypeService.create(this.complaintTypes);
		return SUCCESS;
	}

	public String save() {
		this.complaintTypeService.update(this.complaintTypes);			
		return SUCCESS;
	}

	@Override
	public void validate() {
		if (this.complaintTypeService.isExist("name", this.complaintTypes.getName())) { 
			addActionError(getText("Masters.complainttype.duplicate.error")); 
		}		
	}

	@Override
	public Object getModel() {
		return this.complaintTypes;
	}

	public List<Department> getDepartments() {
		return this.pgrCommonUtils.getAllDepartments();
	}

	public List<ComplaintGroup> getComplaintGroups() {
		return this.complaintGroupService.findAll();
	}

	public List<Category> getCategories() {
		return this.categoryService.findAll();
	}

	public List<ComplaintTypes> getComplaintTypes() {
		return this.complaintTypeService.findAll();
	}

	public void setComplaintTypeService(ComplaintTypeService complaintTypeService) {
		this.complaintTypeService = complaintTypeService;
	}

	public void setComplaintGroupService(ComplaintGroupService complaintGroupService) {
		this.complaintGroupService = complaintGroupService;
	}

	public void setCategoryService(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	public void setPgrCommonUtils(PgrCommonUtils pgrCommonUtils) {
		this.pgrCommonUtils = pgrCommonUtils;
	}

}
