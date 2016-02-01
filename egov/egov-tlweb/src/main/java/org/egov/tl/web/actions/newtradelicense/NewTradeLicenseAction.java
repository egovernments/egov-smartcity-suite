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

import static org.egov.tl.utils.Constants.BUTTONAPPROVE;
import static org.egov.tl.utils.Constants.LOCALITY;
import static org.egov.tl.utils.Constants.LOCATION_HIERARCHY_TYPE;
import static org.egov.tl.utils.Constants.TRANSACTIONTYPE_CREATE_LICENSE;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.EgwStatus;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.tl.entity.License;
import org.egov.tl.entity.LicenseDocumentType;
import org.egov.tl.entity.LicenseStatus;
import org.egov.tl.entity.Licensee;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.entity.WorkflowBean;
import org.egov.tl.service.AbstractLicenseService;
import org.egov.tl.service.FeeTypeService;
import org.egov.tl.service.TradeLicenseService;
import org.egov.tl.utils.Constants;
import org.egov.tl.web.actions.BaseLicenseAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@ParentPackage("egov")
@Results({@Result(name = NewTradeLicenseAction.NEW, location = "newTradeLicense-new.jsp"),
        @Result(name = Constants.ACKNOWLEDGEMENT, location = "newTradeLicense-" + Constants.ACKNOWLEDGEMENT + ".jsp"),
        @Result(name = Constants.PFACERTIFICATE, location = "/WEB-INF/jsp/viewtradelicense/viewTradeLicense-" + Constants.PFACERTIFICATE + ".jsp"),
        @Result(name = Constants.MESSAGE, location = "newTradeLicense-" + Constants.MESSAGE + ".jsp"),
        @Result(name = Constants.BEFORE_RENEWAL, location = "newTradeLicense-" + Constants.BEFORE_RENEWAL + ".jsp"),
        @Result(name = Constants.ACKNOWLEDGEMENT_RENEW, location = "newTradeLicense-" + Constants.ACKNOWLEDGEMENT_RENEW + ".jsp")})
public class NewTradeLicenseAction extends BaseLicenseAction {

    private static final long serialVersionUID = 1L;

    private TradeLicense tradeLicense = new TradeLicense();
    private List<LicenseDocumentType> documentTypes = new ArrayList<>();
    private Map<String, String> ownerShipTypeMap;
    private String mode;
    @Autowired
    @Qualifier("tradeLicenseService")
    private TradeLicenseService tradeLicenseService;

    public NewTradeLicenseAction() {
        tradeLicense.setLicensee(new Licensee());
    }

    @Override
    @SkipValidation
    @Action(value = "/newtradelicense/newTradeLicense-newForm")
    public String newForm() {
        tradeLicense.setApplicationDate(new Date());
      
        return super.newForm();
    }

    @Override
    @ValidationErrorPage(Constants.NEW)
    @Action(value = "/newtradelicense/newTradeLicense-approve")
    public String approve() {

        tradeLicense = this.tradeLicenseService.getLicenseById((Long) getSession().get("model.id"));
      if ("Submit".equals(workFlowAction) && mode.equalsIgnoreCase(VIEW) &&  tradeLicense.getState().getValue().equals(Constants.WF_STATE_COLLECTION_PENDING) && tradeLicense != null && !tradeLicense.isPaid() &&
                !workFlowAction.equalsIgnoreCase(Constants.BUTTONREJECT)) {
            prepareNewForm();
            ValidationError vr = new ValidationError("license.fee.notcollected", "license.fee.notcollected");
            throw new ValidationException(Arrays.asList(vr));
        }
        if (BUTTONAPPROVE.equals(workFlowAction)) {
            license().setCreationAndExpiryDate();
            if (license().getTempLicenseNumber() == null) {
                String nextRunningLicenseNumber = tradeLicenseService.getNextRunningLicenseNumber(
                        "egtl_license_number").toString();
                license().generateLicenseNumber(nextRunningLicenseNumber);
              
             }
            EgwStatus statusChange = (EgwStatus) persistenceService
                    .find("from org.egov.commons.EgwStatus where moduletype=? and code=?",Constants.TRADELICENSEMODULE,Constants.APPLICATION_STATUS_APPROVED_CODE);
            license().setEgwStatus(statusChange);
            
        }
        if(BUTTONAPPROVE.equals(workFlowAction) || ((Constants.BUTTONFORWARD.equals(workFlowAction) && tradeLicense.getState().getValue().equals(Constants.WF_STATE_INSPECTION_PENDING) )))
        {
          LicenseStatus activeStatus = (LicenseStatus) persistenceService
                    .find("from org.egov.tl.entity.LicenseStatus where code='UWF'");
            license().setStatus(activeStatus);
            if(Constants.BUTTONFORWARD.equals(workFlowAction) && license().getEgwStatus()!=null && license().getEgwStatus().getCode().equals(Constants.APPLICATION_STATUS_CREATED_CODE) ){
                EgwStatus statusChange = (EgwStatus) persistenceService
                        .find("from org.egov.commons.EgwStatus where moduletype=? and code=?",Constants.TRADELICENSEMODULE,Constants.APPLICATION_STATUS_INSPE_CODE);
                license().setEgwStatus(statusChange);
            }
        }
        if(Constants.GENERATECERTIFICATE.equals(workFlowAction)){
            LicenseStatus activeStatus = (LicenseStatus) persistenceService
                    .find("from org.egov.tl.entity.LicenseStatus where code='ACT'");
            license().setStatus(activeStatus);
            }
        return super.approve();
    }

    @ValidationErrorPage(Constants.NEW)
    @Action(value = "/newtradelicense/newTradeLicense-create")
    public String create() {
        try {
            return super.create(tradeLicense);
        } catch (RuntimeException e) {
            ValidationError vr=new ValidationError(e.getMessage(), e.getMessage());
            throw new ValidationException(Arrays.asList(vr) ); 
        }
    }
    
    @Override
    public void prepareNewForm() {
        super.prepareNewForm();
        if (license() != null && license().getId() != null)
            tradeLicense = this.tradeLicenseService.getLicenseById(license().getId());
        setDocumentTypes(tradeLicenseService.getDocumentTypesByTransaction(TRANSACTIONTYPE_CREATE_LICENSE));
        tradeLicense.setHotelGradeList(tradeLicense.populateHotelGradeList());
        tradeLicense.setHotelSubCatList(tradeLicenseService.getHotelCategoriesForTrade());
        setOwnerShipTypeMap(Constants.OWNERSHIP_TYPE);
        List<Boundary> localityList = boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(
                LOCALITY, LOCATION_HIERARCHY_TYPE);
        addDropdownData("localityList", localityList);
        addDropdownData("tradeTypeList", tradeLicenseService.getAllNatureOfBusinesses());
        addDropdownData("categoryList", licenseCategoryService.findAll());
        addDropdownData("uomList", unitOfMeasurementService.findAllActiveUOM());
        addDropdownData("subCategoryList", tradeLicense.getCategory() == null ? Collections.emptyList() :
                licenseSubCategoryService.findAllSubCategoryByCategory(tradeLicense.getCategory().getId()));
    }

    @Override
    @ValidationErrorPage(Constants.BEFORE_RENEWAL)
    @SkipValidation
    @Action(value = "/newtradelicense/newTradeLicense-renewal")
    public String renew() {
        BigDecimal deduction = tradeLicense.getDeduction();
        BigDecimal otherCharges = tradeLicense.getOtherCharges();
        BigDecimal swmFee = tradeLicense.getSwmFee();
        tradeLicense = this.tradeLicenseService.getLicenseById(tradeLicense.getId());
        tradeLicense.setOtherCharges(otherCharges);
        tradeLicense.setDeduction(deduction);
        tradeLicense.setSwmFee(swmFee);
        return super.renew();
    }

    @Override
    @SkipValidation
    @Action(value = "/newtradelicense/newTradeLicense-beforeRenew")
    public String beforeRenew() {
        tradeLicense = this.tradeLicenseService.getLicenseById(tradeLicense.getId());
        License license = tradeLicense;
        if (!license.getState().getValue().isEmpty()) {
            license.transition(true).withStateValue("");
        }
        return super.beforeRenew();
    }

    /*
     * Invoked from Workflow users Inbox
     */
    @Override
    @Action(value = "/newtradelicense/newTradeLicense-showForApproval")
    public String showForApproval() {
        if(license().getStatus().getName().equals(Constants.LICENSE_STATUS_ACKNOWLEDGED)
                ||license().getStatus().getName().equals(Constants.LICENSE_STATUS_UNDERWORKFLOW)){
        mode = VIEW;
        }
        return super.showForApproval();
    }

    @Override
    public License getModel() {
        return tradeLicense;
    }

    public WorkflowBean getWorkflowBean() {
        return workflowBean;
    }

    public void setWorkflowBean(WorkflowBean workflowBean) {
        this.workflowBean = workflowBean;
    }

    @Override
    protected TradeLicense license() {
        return tradeLicense;
    }

    @Override
    protected AbstractLicenseService licenseService() {
        return tradeLicenseService;
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

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}