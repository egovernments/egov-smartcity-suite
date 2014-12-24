/*
 * @(#)ComplaintReceivingCenterMasterAction.java 3.0, 23 Jul, 2013 3:29:42 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pgr.web.actions.masters;

import java.util.List;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.pgr.domain.entities.ComplaintReceivingCenter;
import org.egov.pgr.domain.services.CompReceivingCenterService;
import org.egov.pgr.utils.PgrCommonUtils;
import org.egov.web.actions.BaseFormAction;

import com.opensymphony.xwork2.validator.annotations.Validations;

@ParentPackage("egov")
@Validations
public class ComplaintReceivingCenterMasterAction extends BaseFormAction {
	private static final long serialVersionUID = 1;
	private ComplaintReceivingCenter compReceivingCenter = new ComplaintReceivingCenter();
	private CompReceivingCenterService compReceivingCenterService;
	private PgrCommonUtils pgrCommonUtils; 

	@SkipValidation
	public String newform() {
		return NEW;
	}

	@SkipValidation
	public String edit() {
		this.compReceivingCenter = this.compReceivingCenterService.findById(this.compReceivingCenter.getId(),false);
		return EDIT;
	}

	@Override
	@SkipValidation
	public String execute() {
		return list();
	}

	@SkipValidation
	public String list() {
		return INDEX;
	}

	public String create() {
		this.compReceivingCenterService.create(this.compReceivingCenter);
		return SUCCESS;
	}

	public String save() {
		this.compReceivingCenterService.update(this.compReceivingCenter);
		return SUCCESS;
	}

	@Override
	public void validate() {
		if (this.compReceivingCenterService.isExist("receivingCenterName",this.compReceivingCenter.getCenterName())) { 
			addActionError(getText("Masters.complaintreceivingcenter.duplicate.error")); 
		}
	}

	@Override
	public Object getModel() {
		return this.compReceivingCenter;
	}

	public List<ComplaintReceivingCenter> getComplaintReceivingCenters() {
		return this.compReceivingCenterService.findAll();
	}

	public List<String> getCityDropDownList() {
		return this.pgrCommonUtils.getCities();
	}

	public void setCompReceivingCenterService(CompReceivingCenterService compReceivingCenterService) {
		this.compReceivingCenterService = compReceivingCenterService;
	}

	public void setPgrCommonUtils(PgrCommonUtils pgrCommonUtils) {
		this.pgrCommonUtils = pgrCommonUtils;
	}

}