package org.egov.tl.web.actions.search;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.tl.domain.entity.TradeLicense;
import org.egov.tl.domain.service.BaseLicenseService;
import org.egov.tl.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.validator.annotations.Validations;

@ParentPackage("egov")
@Validations
@Results({
    @Result(name = SearchLicenseAction.COMMON_FORM, location = "searchLicense-commonForm.jsp"),
    @Result(name = SearchLicenseAction.CANCEL_LICENSE, type = "redirectAction", location = "cancelLicense-newForm", params = {
            "namespace", "/cancellation", "licenseId", "${licenseId}" })
})
public class SearchLicenseAction extends BaseFormAction {

    /**
     *
     */
    private static final long serialVersionUID = 2620387601260939372L;
    protected static final String COMMON_FORM = "commonForm";
    private String mode;
    private String licenseNumber;
    private Long licenseId;
    @Autowired
    private BaseLicenseService baseLicenseService;
    public static final String CANCEL_LICENSE = "Cancel License";

    @Override
    public Object getModel() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Generalised method to give search license screen to perform different transactions like cancel, Objection, Suspension,
     * Renewal Notice etc
     * @return
     */
    @SkipValidation
    @Action(value = "/search/searchLicense-commonForm")
    public String commonForm() {
        return COMMON_FORM;
    }

    /**
     * Generalised method to redirect the form page to different transactional form pages
     * @return
     */
    @ValidationErrorPage(value = COMMON_FORM)
    @Action(value = "/search/searchLicense-commonSearch")
    public String commonSearch() {
        final TradeLicense tradeLicense = baseLicenseService.getTradeLicenseByLicenseNum(licenseNumber);
        if (tradeLicense == null) {
            addActionError(getText("validation.license.doesnot.exists"));
            return COMMON_FORM;
        }
        if (CANCEL_LICENSE.equals(mode))
            if (tradeLicense.getStatus() != null &&
            (tradeLicense.getStatus().getStatusCode().equalsIgnoreCase(Constants.STATUS_CANCELLED) ||
                    tradeLicense.getStatus().getStatusCode().equalsIgnoreCase(Constants.STATUS_UNDERWORKFLOW))) {
                addActionError(getText("validation.cannotPerform.licenseCancel"));
                return COMMON_FORM;
            }
        licenseId = tradeLicense.getId();
        return mode;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(final String mode) {
        this.mode = mode;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(final String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public Long getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(final Long licenseId) {
        this.licenseId = licenseId;
    }

}
