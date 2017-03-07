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

package org.egov.tl.web.actions.integration;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.tl.entity.License;
import org.egov.tl.service.TradeLicenseService;
import org.egov.tl.service.integration.LicenseBill;
import org.egov.tl.service.integration.LicenseBillService;
import org.egov.tl.utils.LicenseNumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Map;

import static com.opensymphony.xwork2.Action.SUCCESS;

@ParentPackage("egov")
@Results({@Result(name = "showfees", location = "license-showfees.jsp"),
        @Result(name = SUCCESS, location = "licenseBillCollect.jsp")})
public class LicenseBillCollectAction extends BaseFormAction {

    private static final long serialVersionUID = 1L;
    private Long licenseId;
    private LicenseBill licenseBill;
    private String collectXML;

    @Autowired
    private LicenseBillService licenseBillService;

    @Autowired
    private LicenseNumberUtils licenseNumberUtils;

    @Autowired
    @Qualifier("tradeLicenseService")
    private TradeLicenseService tradeLicenseService;

    private Map<String, Map<String, BigDecimal>> outstandingFee;

    @Override
    public String execute() throws IOException {
        final License license = tradeLicenseService.getLicenseById(licenseId);
        setOutstandingFee(licenseBillService.getPaymentFee(license));
        setLicenseId(licenseId);
        return "showfees";
    }

    @Action(value = "/integration/licenseBillCollect-collectfees")
    public String payFee() throws IOException {
        final License license = tradeLicenseService.getLicenseById(licenseId);
        if (license.isPaid()) {
            ServletActionContext.getResponse().setContentType("text/html");
            ServletActionContext.getResponse().getWriter()
                    .write("<center style='color:red;font-weight:bolder'>License Fee already collected !</center>");
            return null;
        }
        licenseBill.setLicense(license);
        licenseBill.setReferenceNumber(licenseNumberUtils.generateBillNumber());
        licenseBillService.setLicense(license);
        collectXML = URLEncoder.encode(licenseBillService.getBillXML(licenseBill), "UTF-8");
        return SUCCESS;
    }

    @Override
    public Object getModel() {
        return null;
    }

    public String getCollectXML() {
        return collectXML;
    }

    public Long getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(final Long licenseId) {
        this.licenseId = licenseId;
    }

    public void setLicenseBill(LicenseBill licenseBill) {
        this.licenseBill = licenseBill;
    }

    public Map<String, Map<String, BigDecimal>> getOutstandingFee() {
        return outstandingFee;
    }

    public void setOutstandingFee(Map<String, Map<String, BigDecimal>> outstandingFee) {
        this.outstandingFee = outstandingFee;
    }
}
