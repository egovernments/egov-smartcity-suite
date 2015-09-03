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
package org.egov.tl.domain.web.integration;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.tl.domain.entity.License;
import org.egov.tl.domain.service.integration.LicenseBill;
import org.egov.tl.domain.service.integration.LicenseBillService;

@ParentPackage("egov")
public class LicenseBillCollectAction extends BaseFormAction {

    private static final long serialVersionUID = 1L;
    private Long licenseId;
    private LicenseBillService licenseBillService;
    private LicenseBill licenseBill;
    private String collectXML;

    public String getCollectXML() {
        return collectXML;
    }

    public void setLicenseBillService(final LicenseBillService licenseBillService) {
        this.licenseBillService = licenseBillService;
    }

    public void setLicenseBill(final LicenseBill licenseBill) {
        this.licenseBill = licenseBill;
    }

    public Long getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(final Long licenseId) {
        this.licenseId = licenseId;
    }

    @Override
    public String execute() throws UnsupportedEncodingException, IOException {
        persistenceService.setType(License.class);
        final License license = (License) persistenceService.findById(licenseId, false);
        if (license.isPaid()) {
            ServletActionContext.getResponse().setContentType("text/html");
            ServletActionContext.getResponse().getWriter()
                    .write("<center style='color:red;font-weight:bolder'>License Fee already collected !</center>");
            return null;
        }
        licenseBill.setLicense(license);
        licenseBillService.setLicense(license);
        collectXML = URLEncoder.encode(licenseBillService.getBillXML(licenseBill), "UTF-8");
        return SUCCESS;

    }

    public String renew() throws UnsupportedEncodingException, IOException {
        if (getSession().get("model.id") != null) {
            licenseId = Long.valueOf((Long) getSession().get("model.id"));
            getSession().remove("model.id");
        }
        persistenceService.setType(License.class);
        final License license = (License) persistenceService.findById(licenseId, false);
        licenseBill.setLicense(license);
        licenseBillService.setLicense(license);
        collectXML = URLEncoder.encode(licenseBillService.getBillXML(licenseBill), "UTF-8");
        return SUCCESS;

    }

    @Override
    public Object getModel() {
        return null;
    }

}
