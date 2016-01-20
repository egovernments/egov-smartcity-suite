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

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infstr.services.PersistenceService;
import org.egov.tl.entity.License;
import org.egov.tl.entity.LicenseAppType;
import org.egov.tl.entity.LicenseDocumentType;
import org.egov.tl.entity.Licensee;
import org.egov.tl.entity.MotorDetails;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.service.AbstractLicenseService;
import org.egov.tl.service.TradeLicenseService;
import org.egov.tl.service.masters.LicenseCategoryService;
import org.egov.tl.service.masters.LicenseSubCategoryService;
import org.egov.tl.service.masters.UnitOfMeasurementService;
import org.egov.tl.utils.Constants;
import org.egov.tl.web.actions.BaseLicenseAction;
import org.egov.tl.web.actions.domain.CommonTradeLicenseAjaxAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.egov.tl.utils.Constants.LOCALITY;
import static org.egov.tl.utils.Constants.LOCATION_HIERARCHY_TYPE;
import static org.egov.tl.utils.Constants.TRANSACTIONTYPE_CREATE_LICENSE;

@ParentPackage("egov")
@Results({
        @Result(name = EnterTradeLicenseAction.NEW, location = "enterTradeLicense-new.jsp"),
        @Result(name = "viewlicense", type = "redirectAction", location = "viewTradeLicense-view",
                params = {"namespace", "/viewtradelicense", "model.id", "${model.id}"})})
public class EnterTradeLicenseAction extends BaseLicenseAction {
    private static final long serialVersionUID = 1L;

    private TradeLicense tradeLicense = new TradeLicense();
    private List<LicenseDocumentType> documentTypes = new ArrayList<>();
    private Map<String, String> ownerShipTypeMap;

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
        return super.newForm();
    }

    @Override
    @ValidationErrorPage(Constants.NEW)
    @Action(value = "/entertradelicense/enterTradeLicense-enterExisting")
    public String create() {
        try {
            return super.enterExisting(tradeLicense);
        } catch (ApplicationRuntimeException e) {
            throw new ValidationException("oldLicenseNumber", e.getMessage(), tradeLicense.getOldLicenseNumber());
        }
    }

    @Override
    public void prepareNewForm() {
        super.prepareNewForm();
        if (license() != null && license().getId() != null)
            tradeLicense = (TradeLicense) persistenceService.find("from TradeLicense where id=?", license().getId());
        setDocumentTypes(tradeLicenseService.getDocumentTypesByTransaction(TRANSACTIONTYPE_CREATE_LICENSE));
        tradeLicense.setHotelGradeList(tradeLicense.populateHotelGradeList());
        tradeLicense.setHotelSubCatList(this.tradeLicenseService.getHotelCategoriesForTrade());
        setOwnerShipTypeMap(Constants.OWNERSHIP_TYPE);
        addDropdownData("localityList", boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(
                LOCALITY, LOCATION_HIERARCHY_TYPE));
        addDropdownData("tradeTypeList", this.tradeLicenseService.getAllNatureOfBusinesses());
        addDropdownData("categoryList", licenseCategoryService.findAll());
        addDropdownData("uomList", unitOfMeasurementService.findAllActiveUOM());
        addDropdownData("subCategoryList", tradeLicense.getCategory() == null ? Collections.emptyList() :
                licenseSubCategoryService.findAllSubCategoryByCategory(tradeLicense.getCategory().getId()));

    }

    @Override
    public License getModel() {
        return tradeLicense;
    }

    @Override
    protected License license() {
        return tradeLicense;
    }

    @Override
    protected AbstractLicenseService licenseService() {
        return this.tradeLicenseService;
    }

    public List<LicenseDocumentType> getDocumentTypes() {
        return documentTypes;
    }

    public void setDocumentTypes(List<LicenseDocumentType> documentTypes) {
        this.documentTypes = documentTypes;
    }

    public Map<String, String> getOwnerShipTypeMap() {
        return ownerShipTypeMap;
    }

    public void setOwnerShipTypeMap(Map<String, String> ownerShipTypeMap) {
        this.ownerShipTypeMap = ownerShipTypeMap;
    }

}