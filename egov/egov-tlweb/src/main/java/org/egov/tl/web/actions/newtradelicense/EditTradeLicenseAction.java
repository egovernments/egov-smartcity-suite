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

package org.egov.tl.web.actions.newtradelicense;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.web.struts.annotation.ValidationErrorPageExt;
import org.egov.tl.entity.License;
import org.egov.tl.entity.LicenseAppType;
import org.egov.tl.entity.LicenseDocumentType;
import org.egov.tl.entity.Licensee;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.entity.WorkflowBean;
import org.egov.tl.entity.enums.ApplicationType;
import org.egov.tl.service.AbstractLicenseService;
import org.egov.tl.service.FeeMatrixService;
import org.egov.tl.service.TradeLicenseService;
import org.egov.tl.utils.Constants;
import org.egov.tl.web.actions.BaseLicenseAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.egov.tl.utils.Constants.LOCALITY;
import static org.egov.tl.utils.Constants.LOCATION_HIERARCHY_TYPE;

@ParentPackage("egov")
@Results({
        @Result(name = Constants.EDIT, location = "editTradeLicense-edit.jsp"),
        @Result(name = Constants.NEW, location = "newTradeLicense-new.jsp"),
        @Result(name = Constants.MESSAGE, location = "editTradeLicense-message.jsp")
})
public class EditTradeLicenseAction extends BaseLicenseAction {
    private static final long serialVersionUID = 1L;

    private TradeLicense tradeLicense = new TradeLicense();
    private boolean isOldLicense;
    private List<LicenseDocumentType> documentTypes = new ArrayList<>();
    private String mode;
    private Map<String, String> ownerShipTypeMap;
    private Long id;

    @Autowired
    @Qualifier("tradeLicenseService")
    private TradeLicenseService tradeLicenseService;

    @Autowired
    private FeeMatrixService feeMatrixService;

    public EditTradeLicenseAction() {
        this.tradeLicense.setLicensee(new Licensee());
    }

    @Override
    public License getModel() {
        return this.tradeLicense;
    }

    public void setModel(TradeLicense tradeLicense) {
        this.tradeLicense = tradeLicense;
    }

    public void prepareBeforeEdit() {
        this.prepareNewForm();
        setDocumentTypes(tradeLicenseService.getDocumentTypesByApplicationType(ApplicationType.NEW));        
        Long id = null;
        if (this.tradeLicense.getId() == null)
            if (this.getSession().get("model.id") != null) {
                id = (Long) this.getSession().get("model.id");
                this.getSession().remove("model.id");
            } else
                id = this.tradeLicense.getId();
        else
            id = this.tradeLicense.getId();
        this.tradeLicense = this.tradeLicenseService.getLicenseById(id);
        if (this.tradeLicense.getOldLicenseNumber() != null)
            this.isOldLicense = StringUtils.isNotBlank(this.tradeLicense.getOldLicenseNumber());
        Boundary licenseboundary = this.boundaryService.getBoundaryById(this.tradeLicense.getBoundary().getId());
        if (licenseboundary.getName().contains("Zone"))
            this.addDropdownData(Constants.DROPDOWN_DIVISION_LIST_LICENSE, Collections.EMPTY_LIST);
        this.setRoleName(this.securityUtils.getCurrentUser().getRoles().toString());
        this.setOwnerShipTypeMap(Constants.OWNERSHIP_TYPE);
        List<Boundary> localityList = this.boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(
                LOCALITY, LOCATION_HIERARCHY_TYPE);
        this.addDropdownData("localityList", localityList);
        this.addDropdownData("tradeTypeList", this.tradeLicenseService.getAllNatureOfBusinesses());
        this.addDropdownData("categoryList", this.licenseCategoryService.getCategories());
        this.addDropdownData("uomList", this.unitOfMeasurementService.getAllActiveUOM());
        addDropdownData("subCategoryList", tradeLicense.getCategory() == null ? Collections.emptyList() :
                licenseSubCategoryService.getSubCategoriesByCategory(tradeLicense.getCategory().getId()));

    }


    @SkipValidation
    @Action(value = "/newtradelicense/editTradeLicense-beforeEdit")
    public String beforeEdit() {
        this.mode = EDIT;
        return NEW;
    }

    public void setupBeforeEdit() {
        this.prepareBeforeEdit();
        this.setupWorkflowDetails();
    }

    @Override
    public void prepare() {
        if (this.id != null) {
            this.tradeLicense = this.tradeLicenseService.getLicenseById(this.id);
        }
    }

    @ValidationErrorPageExt(
            action = "edit", makeCall = true, toMethod = "setupBeforeEdit")
    @Action(value = "/newtradelicense/editTradeLicense-edit")
    public String edit() {
        if (this.tradeLicense.getState() == null && !this.isOldLicense)
            this.tradeLicenseService.transitionWorkFlow(this.tradeLicense, this.workflowBean);
        if (!this.isOldLicense)
            this.processWorkflow(NEW);
        this.tradeLicenseService.processAndStoreDocument(this.tradeLicense.getDocuments(), this.tradeLicense);

        LicenseAppType newAppType = (LicenseAppType) this.persistenceService.find("from  LicenseAppType where name='New' ");
        this.tradeLicense.setLicenseAppType(newAppType);

        this.tradeLicense = (TradeLicense) this.persistenceService.update(this.tradeLicense);
        return Constants.MESSAGE;

    }

    @Override
    public boolean acceptableParameterName(String paramName) {
        List<String> nonAcceptable = Arrays.asList("licensee.boundary.parent", "boundary.parent",
                "tradeName.name");
        boolean retValue = super.acceptableParameterName(paramName);
        return retValue ? !nonAcceptable.contains(paramName) : retValue;
    }

    public WorkflowBean getWorkflowBean() {
        return this.workflowBean;
    }

    public void setWorkflowBean(WorkflowBean workflowBean) {
        this.workflowBean = workflowBean;
    }

    @Override
    protected License license() {
        return this.tradeLicense;
    }

    @Override
    protected AbstractLicenseService licenseService() {
        return this.tradeLicenseService;
    }

    public boolean getIsOldLicense() {
        return this.isOldLicense;
    }

    public void setIsOldLicense(boolean isOldLicense) {
        this.isOldLicense = isOldLicense;
    }

    public List<LicenseDocumentType> getDocumentTypes() {
        return this.documentTypes;
    }

    public void setDocumentTypes(List<LicenseDocumentType> documentTypes) {
        this.documentTypes = documentTypes;
    }

    public String getMode() {
        return this.mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Map<String, String> getOwnerShipTypeMap() {
        return this.ownerShipTypeMap;
    }

    public void setOwnerShipTypeMap(Map<String, String> ownerShipTypeMap) {
        this.ownerShipTypeMap = ownerShipTypeMap;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}