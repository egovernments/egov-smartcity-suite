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
package org.egov.tradelicense.revokesuspension.web;

import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.persistence.entity.Address;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.tradelicense.domain.entity.License;
import org.egov.tradelicense.domain.entity.LicenseStatusValues;
import org.egov.tradelicense.domain.entity.Licensee;
import org.egov.tradelicense.domain.entity.SubCategory;
import org.egov.tradelicense.domain.entity.TradeLicense;
import org.egov.tradelicense.domain.service.BaseLicenseService;
import org.egov.tradelicense.domain.service.TradeService;
import org.egov.tradelicense.domain.web.BaseLicenseAction;
import org.egov.tradelicense.utils.Constants;
import org.egov.tradelicense.utils.LicenseUtils;

import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

public class RevokeSuspensionAction extends BaseLicenseAction {

    private static final Logger LOGGER = Logger.getLogger(RevokeSuspensionAction.class);

    protected LicenseUtils licenseUtils;
    private Date revokeDate;
    private String revokeRemarks;
    private static final long serialVersionUID = 1L;
    private TradeService ts;
    private Long licenseId;
    private License license = new TradeLicense();

    @Override
    public void setLicenseUtils(final LicenseUtils licenseUtils) {
        this.licenseUtils = licenseUtils;
    }

    public void setTs(final TradeService ts) {
        this.ts = ts;
    }

    public Long getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(final Long licenseId) {
        this.licenseId = licenseId;
    }

    public License getLicense() {
        return license;
    }

    public void setLicense(final License license) {
        this.license = license;
    }

    @Override
    @SkipValidation
    public String newForm() {
        license = (License) persistenceService.find("from License where id=?", licenseId);
        return Constants.NEW;
    }

    public RevokeSuspensionAction() {
        this.addRelatedEntity("boundary", Boundary.class);
        this.addRelatedEntity("address", Address.class);
        this.addRelatedEntity("licensee", Licensee.class);
        this.addRelatedEntity("licensee.address", Address.class);
        this.addRelatedEntity("tradeName", SubCategory.class);

    }

    @Override
    public void prepare() {
        super.prepare();

    }

    @Override
    public Object getModel() {
        return license;
    }

    public void setModel(final License license) {
        this.license = license;
    }

    @SkipValidation
    @SuppressWarnings("unchecked")
    @ValidationErrorPage("revokesuspension")
    @Validations(
            requiredFields = { @RequiredFieldValidator(
                    fieldName = "revokeDate", message = "", key = Constants.REQUIRED), @RequiredFieldValidator(
                    fieldName = "remarks", message = "", key = Constants.REQUIRED) })
    public String confirmRevokeSuspension() {
        LOGGER.debug("Revoke Suspension Action Elements are:<<<<<<<<<<>>>>>>>>>>>>>:" + toString());
        license = (License) persistenceService.find("from License where id=?", licenseId);
        final LicenseStatusValues licenseStatusValues = licenseUtils.getCurrentStatus(license);
        if (licenseStatusValues != null)
            licenseStatusValues.setActive(false);
        final LicenseStatusValues newLicenseStatusValues = new LicenseStatusValues();
        newLicenseStatusValues.setReferenceDate(revokeDate);
        newLicenseStatusValues.setRemarks(revokeRemarks);
        newLicenseStatusValues.setPreviousStatusVal(licenseStatusValues);
        ts.revokeSuspendedLicense(license, licenseUtils, newLicenseStatusValues);
        addActionMessage(this.getText("license.revoke.succesful"));
        LOGGER.debug("Revoke Suspension Action License Number:<<<<<<<<<<>>>>>>>>>>>>>:" + license.getLicenseNumber());
        return "message";
    }

    public Date getRevokeDate() {
        return revokeDate;
    }

    public void setRevokeDate(final Date revokeDate) {
        this.revokeDate = revokeDate;
    }

    public String getRevokeRemarks() {
        return revokeRemarks;
    }

    public void setRevokeRemarks(final String revokeRemarks) {
        this.revokeRemarks = revokeRemarks;
    }

    @Override
    protected License license() {
        return license;
    }

    @Override
    protected BaseLicenseService service() {
        return null;
    }

    @SkipValidation
    public String getObjectionReason(final int reasonId) {
        return licenseUtils.getObjectionReasons().get(reasonId);
    }

    @Override
    public String toString()
    {
        final StringBuilder str = new StringBuilder();
        str.append("RevokeSuspensionAction={");
        str.append("  licenseUtils=").append(licenseUtils == null ? "null" : licenseUtils.toString());
        str.append("  revokeDate=").append(revokeDate == null ? "null" : revokeDate.toString());
        str.append("  revokeRemarks=").append(revokeRemarks == null ? "null" : revokeRemarks.toString());
        str.append("  ts=").append(ts == null ? "null" : ts.toString());
        str.append("  licenseId=").append(licenseId == null ? "null" : licenseId.toString());
        str.append("  license=").append(license == null ? "null" : license.toString());
        return str.toString();
    }
}
