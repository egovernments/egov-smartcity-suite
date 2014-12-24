/*
 * @(#)CollectionLicenseAction.java 3.0, 29 Jul, 2013 1:24:26 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.license.web.actions.citizen.collection;

import java.io.IOException;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.dao.UserDAO;
import org.egov.license.domain.entity.License;
import org.egov.license.domain.service.integration.LicenseBill;
import org.egov.license.domain.service.integration.LicenseBillService;
import org.egov.web.actions.BaseFormAction;

@ParentPackage("egov")
public class CollectionLicenseAction extends BaseFormAction {
	private final Logger LOGGER = Logger.getLogger(getClass());
	private static final long serialVersionUID = 1L;
	private LicenseBillService licenseBillService;
	private LicenseBill licenseBill;
	private String collectXML;
	private String appNumber;
	private Long userId;
	private License license;
	private final String GETDETAILSBYAPPLICATIONNUMBER = "getLicenseDetailsByApplicationNumber";

	@Override
	public void prepare() {
		this.LOGGER.debug("Entered into prepare method");
		final UserDAO userDao = new UserDAO();
		final User usr = userDao.getUserByName("citizenUser").get(0);
		setUserId(usr.getId().longValue());
		EGOVThreadLocals.setUserId(usr.getId().toString());
		this.LOGGER.debug("Exit from prepare method");
	}

	@SuppressWarnings("unchecked")
	public String onLinePaymentMode() throws IOException {
		this.LOGGER.debug("Entered inside onLinePaymentMode method " + "applicationNumber:" + this.appNumber);
		this.persistenceService.setType(License.class);
		if (StringUtils.isNotBlank(this.getAppNumber()) && StringUtils.isNotEmpty(this.getAppNumber())) {
			this.license = (License) this.persistenceService.findByNamedQuery(this.GETDETAILSBYAPPLICATIONNUMBER, this.appNumber);
			if (this.license.isPaid()) {
				ServletActionContext.getResponse().setContentType("text/html");
				ServletActionContext.getResponse().getWriter().write("<center style='color:red;font-weight:bolder'>License Fee already collected !</center>");
				return null;
			}
		}
		this.licenseBill.setLicense(this.license);
		this.licenseBillService.setLicense(this.license);
		this.collectXML = URLEncoder.encode(this.licenseBillService.getBillXML(this.licenseBill), "UTF-8");
		this.LOGGER.info("Exiting method onLinePaymentMode, collectXML: " + this.collectXML);
		return "collectOnLine";
	}

	@Override
	public Object getModel() {
		return null;
	}

	public LicenseBillService getLicenseBillService() {
		return this.licenseBillService;
	}

	public LicenseBill getLicenseBill() {
		return this.licenseBill;
	}

	public String getCollectXML() {
		return this.collectXML;
	}

	public void setLicenseBillService(final LicenseBillService licenseBillService) {
		this.licenseBillService = licenseBillService;
	}

	public void setLicenseBill(final LicenseBill licenseBill) {
		this.licenseBill = licenseBill;
	}

	public void setCollectXML(final String collectXML) {
		this.collectXML = collectXML;
	}

	public String getAppNumber() {
		return this.appNumber;
	}

	public void setAppNumber(final String appNumber) {
		this.appNumber = appNumber;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(final Long userId) {
		this.userId = userId;
	}

}
