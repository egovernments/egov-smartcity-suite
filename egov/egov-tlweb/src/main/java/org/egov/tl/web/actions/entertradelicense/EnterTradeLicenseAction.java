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

package org.egov.tl.web.actions.entertradelicense;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Installment;
import org.egov.demand.model.EgDemandDetails;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.web.struts.annotation.ValidationErrorPageExt;
import org.egov.tl.entity.LicenseDocumentType;
import org.egov.tl.entity.Licensee;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.entity.enums.ApplicationType;
import org.egov.tl.service.AbstractLicenseService;
import org.egov.tl.service.TradeLicenseService;
import org.egov.tl.utils.Constants;
import org.egov.tl.web.actions.BaseLicenseAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.egov.tl.utils.Constants.LOCALITY;
import static org.egov.tl.utils.Constants.LOCATION_HIERARCHY_TYPE;

@ParentPackage("egov")
@Results({
        @Result(name = EnterTradeLicenseAction.NEW, location = "enterTradeLicense-new.jsp"),
        @Result(name = "update", location = "enterTradeLicense-update.jsp"),
        @Result(name = "viewlicense", type = "redirectAction", location = "viewTradeLicense-view", params = { "namespace",
                "/viewtradelicense", "model.id", "${model.id}" }) })
public class EnterTradeLicenseAction extends BaseLicenseAction<TradeLicense> {
    private static final long serialVersionUID = 1L;

    private TradeLicense tradeLicense = new TradeLicense();
    private List<LicenseDocumentType> documentTypes = new ArrayList<>();
    private Map<String, String> ownerShipTypeMap = new HashMap<>();
    private Map<Integer, Integer> legacyInstallmentwiseFees = new TreeMap<>();
    private Map<Integer, Boolean> legacyFeePayStatus = new TreeMap<>();
    private String licenseNumber;

    @Autowired
    @Qualifier("tradeLicenseService")
    private transient TradeLicenseService tradeLicenseService;

    public EnterTradeLicenseAction() {
        tradeLicense.setLicensee(new Licensee());
    }

    @Override
    @SkipValidation
    @Action(value = "/entertradelicense/enterTradeLicense-enterExistingForm")
    public String enterExistingForm() {
        tradeLicense.setApplicationDate(new Date());
        for (final Installment installment : tradeLicenseService.getLastFiveYearInstallmentsForLicense()) {
            legacyInstallmentwiseFees.put(installment.getInstallmentNumber(), 0);
            legacyFeePayStatus.put(installment.getInstallmentNumber(), false);
        }
        return super.newForm();
    }

    @ValidationErrorPage(Constants.NEW)
    @Action(value = "/entertradelicense/enterTradeLicense-enterExisting")
    public String create() {
        return super.enterExisting(tradeLicense, legacyInstallmentwiseFees, legacyFeePayStatus);
    }

    @SkipValidation
    @Action(value = "/entertradelicense/update-form")
    public String showLegacyUpdateForm() {
        if (!license().isNew())
            tradeLicense = tradeLicenseService.getLicenseById(license().getId());
        if (tradeLicense != null && tradeLicense.isLegacy() && tradeLicense.hasState())
            throw new ValidationException("legacy.license.modify.excp", "You can't modify this license");
        prepareFeeDetails();
        return "update";
    }

    @Action(value = "/entertradelicense/update")
    @ValidationErrorPageExt(action = "update", makeCall = true, toMethod = "prepareFeeDetails")
    public String update() {
        if (tradeLicenseService.checkOldLicenseNumberIsDuplicated(tradeLicense))
            throw new ValidationException("TL-001", "TL-001", tradeLicense.getOldLicenseNumber());
        tradeLicenseService.updateLegacyLicense(tradeLicense, legacyInstallmentwiseFees, legacyFeePayStatus);
        return "viewlicense";
    }

    public void prepareUpdate() {
        commonFormPrepare();
    }

    public void prepareFeeDetails() {
        prepareUpdate();
        for (final Installment installment : tradeLicenseService.getLastFiveYearInstallmentsForLicense()) {
            legacyInstallmentwiseFees.put(installment.getInstallmentNumber(), 0);
            legacyFeePayStatus.put(installment.getInstallmentNumber(), false);
        }
        for (final EgDemandDetails demandDetail : license().getCurrentDemand().getEgDemandDetails()) {
            legacyInstallmentwiseFees.put(demandDetail.getEgDemandReason().getEgInstallmentMaster().getInstallmentNumber(),
                    demandDetail.getAmount().intValue());
            legacyFeePayStatus.put(demandDetail.getEgDemandReason().getEgInstallmentMaster().getInstallmentNumber(),
                    demandDetail.getAmtCollected().compareTo(BigDecimal.ZERO) != 0 &&
                            demandDetail.getAmtCollected().compareTo(demandDetail.getAmount()) == 0);
        }
    }

    @Override
    public void prepareNewForm() {
        commonFormPrepare();
    }

    public void commonFormPrepare() {
        if (StringUtils.isNotBlank(licenseNumber))
            tradeLicense = tradeLicenseService.getLicenseByLicenseNumber(licenseNumber);
        else if (!license().isNew())
            tradeLicense = tradeLicenseService.getLicenseById(license().getId());
        super.prepareNewForm();
        setDocumentTypes(tradeLicenseService.getDocumentTypesByApplicationType(ApplicationType.NEW));
        setOwnerShipTypeMap(Constants.OWNERSHIP_TYPE);
        addDropdownData("localityList", boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(
                LOCALITY, LOCATION_HIERARCHY_TYPE));
        addDropdownData("tradeTypeList", tradeLicenseService.getAllNatureOfBusinesses());
        addDropdownData("categoryList", licenseCategoryService.getCategories());
        addDropdownData("subCategoryList", tradeLicense.getCategory() == null ? Collections.emptyList()
                : licenseSubCategoryService.getSubCategoriesByCategory(tradeLicense.getCategory().getId()));
        if (license().getAgreementDate() != null)
            setShowAgreementDtl(true);
    }

    @Override
    public TradeLicense getModel() {
        return tradeLicense;
    }

    @Override
    protected TradeLicense license() {
        return tradeLicense;
    }

    @Override
    protected AbstractLicenseService<TradeLicense> licenseService() {
        return tradeLicenseService;
    }

    public List<LicenseDocumentType> getDocumentTypes() {
        return documentTypes;
    }

    public void setDocumentTypes(final List<LicenseDocumentType> documentTypes) {
        this.documentTypes = documentTypes;
    }

    public Map<String, String> getOwnerShipTypeMap() {
        return ownerShipTypeMap;
    }

    public void setOwnerShipTypeMap(final Map<String, String> ownerShipTypeMap) {
        this.ownerShipTypeMap = ownerShipTypeMap;
    }

    public Map<Integer, Integer> getLegacyInstallmentwiseFees() {
        return legacyInstallmentwiseFees;
    }

    public void setLegacyInstallmentwiseFees(final Map<Integer, Integer> legacyInstallmentwiseFees) {
        this.legacyInstallmentwiseFees = legacyInstallmentwiseFees;
    }

    public Map<Integer, Boolean> getLegacyFeePayStatus() {
        return legacyFeePayStatus;
    }

    public void setLegacyFeePayStatus(final Map<Integer, Boolean> legacyFeePayStatus) {
        this.legacyFeePayStatus = legacyFeePayStatus;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(final String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }
}