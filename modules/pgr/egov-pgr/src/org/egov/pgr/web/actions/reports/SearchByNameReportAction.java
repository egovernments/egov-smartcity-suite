/*
 * @(#)SearchByNameReportAction.java 3.0, 23 Jul, 2013 3:29:42 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pgr.web.actions.reports;

import java.util.HashMap;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infstr.search.SearchQuery;
import org.egov.pgr.domain.services.ComplaintDetailService;
import org.egov.web.actions.SearchFormAction;
import org.egov.web.annotation.ValidationErrorPage;

@ParentPackage("egov")
public class SearchByNameReportAction extends SearchFormAction {

	private static final long serialVersionUID = 1L;
	private String name = null;
	private String initials = null;
	private String lastName = null;
	private String phoneNo = null;
	private String email = null;
	private String address = null;
	private ComplaintDetailService complaintService;

	public ComplaintDetailService getComplaintService() {
		return this.complaintService;
	}

	public void setComplaintService(final ComplaintDetailService complaintService) {
		this.complaintService = complaintService;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getInitials() {
		return this.initials;
	}

	public void setInitials(final String initials) {
		this.initials = initials;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNo() {
		return this.phoneNo;
	}

	public void setPhoneNo(final String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(final String address) {
		this.address = address;
	}

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchQuery prepareQuery(final String sortField, final String sortOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	@SkipValidation
	public String newform() {

		return NEW;
	}

	@Override
	@ValidationErrorPage(value = NEW)
	public String search() {
		final HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("NAME", getName());
		hashMap.put("INITIALS", getInitials());
		hashMap.put("LASTNAME", getLastName());
		hashMap.put("PHONENO", getPhoneNo());
		hashMap.put("ADDRESS", getAddress());
		hashMap.put("EMAIL", getEmail());

		this.searchResult = this.complaintService.searchComplaintsByName(hashMap, getPage(), getPageSize());

		return NEW;
	}

	@Override
	public void validate() {

		if ((getName() == null || "".equals(getName())) && (getInitials() == null || "".equals(getInitials())) && (getLastName() == null || "".equals(getLastName())) && (getPhoneNo() == null || "".equals(getPhoneNo()))
				&& (getAddress() == null || "".equals(getAddress())) && (getEmail() == null || "".equals(getEmail()))) {
			addActionError(getMessage("select.atleast.one.value"));
		}
	}

	private String getMessage(final String string) {
		return getText(string);
	}

}
