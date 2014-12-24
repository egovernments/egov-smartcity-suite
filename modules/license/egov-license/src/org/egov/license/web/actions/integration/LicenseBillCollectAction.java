/*
 * @(#)LicenseBillCollectAction.java 3.0, 29 Jul, 2013 1:24:26 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.license.web.actions.integration;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.license.domain.entity.License;
import org.egov.license.domain.service.integration.LicenseBill;
import org.egov.license.domain.service.integration.LicenseBillService;
import org.egov.web.actions.BaseFormAction;

@ParentPackage("egov")
public class LicenseBillCollectAction extends BaseFormAction {

	private static final long serialVersionUID = 1L;
	private Long licenseId;
	private LicenseBillService licenseBillService;
	private LicenseBill licenseBill;
	private String collectXML;

	public String getCollectXML() {
		return this.collectXML;
	}

	public void setLicenseBillService(final LicenseBillService licenseBillService) {
		this.licenseBillService = licenseBillService;
	}

	public void setLicenseBill(final LicenseBill licenseBill) {
		this.licenseBill = licenseBill;
	}

	public Long getLicenseId() {
		return this.licenseId;
	}

	public void setLicenseId(final Long licenseId) {
		this.licenseId = licenseId;
	}

	@Override
	public String execute() throws UnsupportedEncodingException, IOException {
		this.persistenceService.setType(License.class);
		final License license = (License) this.persistenceService.findById(this.licenseId, false);
		if (license.isPaid()) {
			ServletActionContext.getResponse().setContentType("text/html");
			ServletActionContext.getResponse().getWriter().write("<center style='color:red;font-weight:bolder'>License Fee already collected !</center>");
			return null;
		}
		this.licenseBill.setLicense(license);
		this.licenseBillService.setLicense(license);
		this.collectXML = URLEncoder.encode(this.licenseBillService.getBillXML(this.licenseBill), "UTF-8");
		return SUCCESS;

	}

	public String renew() throws UnsupportedEncodingException, IOException {
		if (this.getSession().get("model.id") != null) {
			this.licenseId = (Long.valueOf((Long) this.getSession().get("model.id")));
			this.getSession().remove("model.id");
		}
		this.persistenceService.setType(License.class);
		final License license = (License) this.persistenceService.findById(this.licenseId, false);
		this.licenseBill.setLicense(license);
		this.licenseBillService.setLicense(license);
		this.collectXML = URLEncoder.encode(this.licenseBillService.getBillXML(this.licenseBill), "UTF-8");
		return SUCCESS;

	}

	@Override
	public Object getModel() {
		return null;
	}

}
