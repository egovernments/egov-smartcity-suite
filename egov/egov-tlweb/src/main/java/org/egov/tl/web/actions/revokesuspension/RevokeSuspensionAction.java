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
package org.egov.tl.web.actions.revokesuspension;

import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.persistence.entity.Address;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.tl.entity.License;
import org.egov.tl.entity.LicenseStatusValues;
import org.egov.tl.entity.LicenseSubCategory;
import org.egov.tl.entity.Licensee;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.service.AbstractLicenseService;
import org.egov.tl.service.TradeLicenseService;
import org.egov.tl.utils.Constants;
import org.egov.tl.web.actions.BaseLicenseAction;

import java.util.Date;

@Results({
        @Result(name = Constants.NEW, location = "revokeSuspension-" + Constants.NEW + ".jsp")
})
public class RevokeSuspensionAction extends BaseLicenseAction {

    private static final long serialVersionUID = 1L;

    private Long licenseId;
    private TradeLicense license = new TradeLicense();
    private Date revokeDate;
    private String revokeRemarks;

    private TradeLicenseService tradeLicenseService;

    public RevokeSuspensionAction() {
        addRelatedEntity("boundary", Boundary.class);
        addRelatedEntity("address", Address.class);
        addRelatedEntity("licensee", Licensee.class);
        addRelatedEntity("licensee.address", Address.class);
        addRelatedEntity("tradeName", LicenseSubCategory.class);

    }

    public Long getLicenseId() {
        return this.licenseId;
    }

    public void setLicenseId(Long licenseId) {
        this.licenseId = licenseId;
    }

    @Override
    @SkipValidation
    @Action(value = "/revokesuspension/revokeSuspension-newForm")
    public String newForm() {
        this.license = this.tradeLicenseService.getLicenseById(this.licenseId);
        return Constants.NEW;
    }

    @Override
    public void prepare() {
        super.prepare();

    }

    @Override
    public License getModel() {
        return this.license;
    }

    public void setModel(TradeLicense license) {
        this.license = license;
    }

    @SkipValidation
    @ValidationErrorPage("revokesuspension")
    @Validations(
            requiredFields = {@RequiredFieldValidator(
                    fieldName = "revokeDate", message = "", key = Constants.REQUIRED), @RequiredFieldValidator(
                    fieldName = "remarks", message = "", key = Constants.REQUIRED)})
    public String confirmRevokeSuspension() {
        this.license = this.tradeLicenseService.getLicenseById(this.licenseId);
        LicenseStatusValues licenseStatusValues = this.licenseUtils.getCurrentStatus(this.license);
        if (licenseStatusValues != null)
            licenseStatusValues.setActive(false);
        LicenseStatusValues newLicenseStatusValues = new LicenseStatusValues();
        newLicenseStatusValues.setReferenceDate(this.revokeDate);
        newLicenseStatusValues.setRemarks(this.revokeRemarks);
        newLicenseStatusValues.setPreviousStatusVal(licenseStatusValues);
        this.tradeLicenseService.revokeSuspendedLicense(this.license, this.licenseUtils, newLicenseStatusValues);
        this.addActionMessage(getText("license.revoke.succesful"));
        return "message";
    }

    public Date getRevokeDate() {
        return this.revokeDate;
    }

    public void setRevokeDate(Date revokeDate) {
        this.revokeDate = revokeDate;
    }

    public String getRevokeRemarks() {
        return this.revokeRemarks;
    }

    public void setRevokeRemarks(String revokeRemarks) {
        this.revokeRemarks = revokeRemarks;
    }

    @Override
    protected License license() {
        return this.license;
    }

    @Override
    protected AbstractLicenseService licenseService() {
        return this.tradeLicenseService;
    }

    @SkipValidation
    public String getObjectionReason(int reasonId) {
        return this.licenseUtils.getObjectionReasons().get(reasonId);
    }

}
