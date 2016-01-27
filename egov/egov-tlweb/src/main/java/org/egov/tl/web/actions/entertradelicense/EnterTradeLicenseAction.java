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

import static org.egov.tl.utils.Constants.LOCALITY;
import static org.egov.tl.utils.Constants.LOCATION_HIERARCHY_TYPE;
import static org.egov.tl.utils.Constants.TRANSACTIONTYPE_CREATE_LICENSE;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Installment;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.tl.entity.LicenseDocumentType;
import org.egov.tl.entity.Licensee;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.service.AbstractLicenseService;
import org.egov.tl.service.FeeTypeService;
import org.egov.tl.service.TradeLicenseService;
import org.egov.tl.utils.Constants;
import org.egov.tl.web.actions.BaseLicenseAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@ParentPackage("egov")
@Results({
        @Result(name = EnterTradeLicenseAction.NEW, location = "enterTradeLicense-new.jsp"),
        @Result(name = "viewlicense", type = "redirectAction", location = "viewTradeLicense-view", params = { "namespace",
                "/viewtradelicense", "model.id", "${model.id}" }) })
public class EnterTradeLicenseAction extends BaseLicenseAction<TradeLicense> {
    private static final long serialVersionUID = 1L;

    private TradeLicense tradeLicense = new TradeLicense();
    private List<LicenseDocumentType> documentTypes = new ArrayList<>();
    private Map<String, String> ownerShipTypeMap = new HashMap<>();
    private Map<Integer, BigDecimal> legacyInstallmentwiseFees = new TreeMap<>();
    private Long feeTypeId;
    @Autowired
    @Qualifier("feeTypeService")
    private FeeTypeService feeTypeService;
    @Autowired
    @Qualifier("tradeLicenseService")
    private TradeLicenseService tradeLicenseService;

    public EnterTradeLicenseAction() {
        tradeLicense.setLicensee(new Licensee());
    }

    @Override
    @SkipValidation
    @Action(value = "/entertradelicense/enterTradeLicense-enterExistingForm")
    public String enterExistingForm() {
        tradeLicense.setApplicationDate(new Date());
        for (final Installment installment : tradeLicenseService.getLastFiveYearInstallmentsForLicense())
            legacyInstallmentwiseFees.put(installment.getInstallmentNumber(), BigDecimal.ZERO);
        return super.newForm();
    }

    @ValidationErrorPage(Constants.NEW)
    @Action(value = "/entertradelicense/enterTradeLicense-enterExisting")
    public String create() {
        try {
            return super.enterExisting(tradeLicense, legacyInstallmentwiseFees);
        } catch (final ApplicationRuntimeException e) {
            throw new ValidationException("oldLicenseNumber", e.getMessage(), tradeLicense.getOldLicenseNumber());
        }
    }

    @Override
    public void prepareNewForm() {
        super.prepareNewForm();
        if (license() != null && license().getId() != null)
            tradeLicense = tradeLicenseService.getLicenseById(license().getId());
        setDocumentTypes(tradeLicenseService.getDocumentTypesByTransaction(TRANSACTIONTYPE_CREATE_LICENSE));
        setOwnerShipTypeMap(Constants.OWNERSHIP_TYPE);
        addDropdownData("localityList", boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(
                LOCALITY, LOCATION_HIERARCHY_TYPE));
        addDropdownData("tradeTypeList", tradeLicenseService.getAllNatureOfBusinesses());
        addDropdownData("categoryList", licenseCategoryService.findAll());
        addDropdownData("subCategoryList", tradeLicense.getCategory() == null ? Collections.emptyList()
                : licenseSubCategoryService.findAllSubCategoryByCategory(tradeLicense.getCategory().getId()));
        feeTypeId=feeTypeService.findByName(Constants.LICENSE_FEE_TYPE).getId(); 

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

    public Map<Integer, BigDecimal> getLegacyInstallmentwiseFees() {
        return legacyInstallmentwiseFees;
    }

    public void setLegacyInstallmentwiseFees(final Map<Integer, BigDecimal> legacyInstallmentwiseFees) {
        this.legacyInstallmentwiseFees = legacyInstallmentwiseFees;
    }

    public Long getFeeTypeId() {
        return feeTypeId;
    }

    public void setFeeTypeId(Long feeTypeId) {
        this.feeTypeId = feeTypeId;
    }

}