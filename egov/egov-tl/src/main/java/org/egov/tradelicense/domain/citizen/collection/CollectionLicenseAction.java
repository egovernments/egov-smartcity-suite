/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency, 
 *     accountability and the service delivery of the government  organizations.
 *  
 *      Copyright (C) <2015>  eGovernments Foundation
 *  
 *      The updated version of eGov suite of products as by eGovernments Foundation 
 *      is available at http://www.egovernments.org
 *  
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *  
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *  
 *      You should have received a copy of the GNU General Public License
 *      along with this program. If not, see http://www.gnu.org/licenses/ or 
 *      http://www.gnu.org/licenses/gpl.html .
 *  
 *      In addition to the terms of the GPL license to be adhered to in using this
 *      program, the following additional terms are to be complied with:
 *  
 *  	1) All versions of this program, verbatim or modified must carry this 
 *  	   Legal Notice.
 *  
 *  	2) Any misrepresentation of the origin of the material is prohibited. It 
 *  	   is required that all modified versions of this material be marked in 
 *  	   reasonable ways as different from the original version.
 *  
 *  	3) This license does not grant any rights to any user of the program 
 *  	   with regards to rights under trademark law for use of the trade names 
 *  	   or trademarks of eGovernments Foundation.
 *  
 *    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.tradelicense.domain.citizen.collection;

import java.io.IOException;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.config.ParentPackage;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.dao.UserDAO;
import org.egov.tradelicense.domain.entity.License;
import org.egov.tradelicense.domain.service.integration.LicenseBill;
import org.egov.tradelicense.domain.service.integration.LicenseBillService;
import org.egov.web.actions.BaseFormAction;

/**
 * the class CollectionLicenseAction
 * 
 * @author Laxman
 */

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

	public void prepare() {
		LOGGER.debug("Entered into prepare method");
		UserDAO userDao = new UserDAO();
		User usr = (User) userDao.getUserByName("citizenUser").get(0);
		setUserId(usr.getId().longValue());
		EGOVThreadLocals.setUserId(usr.getId().toString());
		LOGGER.debug("Exit from prepare method");
	}

	@SuppressWarnings("unchecked")
	public String onLinePaymentMode() throws IOException {
		LOGGER.debug("Entered inside onLinePaymentMode method " + "applicationNumber:" + appNumber);
		this.persistenceService.setType(License.class);
		if (StringUtils.isNotBlank(this.getAppNumber()) && StringUtils.isNotEmpty(this.getAppNumber())) {
			license = (License) this.persistenceService.findByNamedQuery(GETDETAILSBYAPPLICATIONNUMBER, appNumber);
			if (license.isPaid()) {
				ServletActionContext.getResponse().setContentType("text/html");
				ServletActionContext.getResponse().getWriter()
				        .write("<center style='color:red;font-weight:bolder'>License Fee already collected !</center>");
				return null;
			}
		}
		this.licenseBill.setLicense(license);
		this.licenseBillService.setLicense(license);
		this.collectXML = URLEncoder.encode(this.licenseBillService.getBillXML(this.licenseBill), "UTF-8");
		LOGGER.info("Exiting method onLinePaymentMode, collectXML: " + collectXML);
		return "collectOnLine";
	}

	@Override
	public Object getModel() {
		return null;
	}

	public LicenseBillService getLicenseBillService() {
		return licenseBillService;
	}

	public LicenseBill getLicenseBill() {
		return licenseBill;
	}

	public String getCollectXML() {
		return collectXML;
	}

	public void setLicenseBillService(LicenseBillService licenseBillService) {
		this.licenseBillService = licenseBillService;
	}

	public void setLicenseBill(LicenseBill licenseBill) {
		this.licenseBill = licenseBill;
	}

	public void setCollectXML(String collectXML) {
		this.collectXML = collectXML;
	}

	public String getAppNumber() {
		return appNumber;
	}

	public void setAppNumber(String appNumber) {
		this.appNumber = appNumber;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
