/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.tl.web.actions.citizen.collection;

import java.io.IOException;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.tl.domain.entity.License;
import org.egov.tl.domain.service.integration.LicenseBill;
import org.egov.tl.domain.service.integration.LicenseBillService;
import org.springframework.beans.factory.annotation.Autowired;

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
    @Autowired
    private UserService userService;

    @Override
    public void prepare() {
        LOGGER.debug("Entered into prepare method");
        final User usr = userService.getUserByUsername("citizenUser");
        setUserId(usr.getId().longValue());
        EgovThreadLocals.setUserId(usr.getId());
        LOGGER.debug("Exit from prepare method");
    }

    @SuppressWarnings("unchecked")
    public String onLinePaymentMode() throws IOException {
        LOGGER.debug("Entered inside onLinePaymentMode method " + "applicationNumber:" + appNumber);
        persistenceService.setType(License.class);
        if (StringUtils.isNotBlank(getAppNumber()) && StringUtils.isNotEmpty(getAppNumber())) {
            license = (License) persistenceService.findByNamedQuery(GETDETAILSBYAPPLICATIONNUMBER, appNumber);
            if (license.isPaid()) {
                ServletActionContext.getResponse().setContentType("text/html");
                ServletActionContext.getResponse().getWriter()
                .write("<center style='color:red;font-weight:bolder'>License Fee already collected !</center>");
                return null;
            }
        }
        licenseBill.setLicense(license);
        licenseBillService.setLicense(license);
        collectXML = URLEncoder.encode(licenseBillService.getBillXML(licenseBill), "UTF-8");
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
        return appNumber;
    }

    public void setAppNumber(final String appNumber) {
        this.appNumber = appNumber;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

}
