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

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.tl.entity.License;
import org.egov.tl.entity.LicenseAppType;
import org.egov.tl.entity.LicenseDocumentType;
import org.egov.tl.entity.LicenseStatus;
import org.egov.tl.entity.Licensee;
import org.egov.tl.entity.MotorDetails;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.entity.WorkflowBean;
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

import static org.egov.tl.utils.Constants.BUTTONAPPROVE;
import static org.egov.tl.utils.Constants.LOCALITY;
import static org.egov.tl.utils.Constants.LOCATION_HIERARCHY_TYPE;
import static org.egov.tl.utils.Constants.TRANSACTIONTYPE_CREATE_LICENSE;

@ParentPackage("egov")
@Results({@Result(name = NewTradeLicenseAction.NEW, location = "newTradeLicense-new.jsp"),
        @Result(name = Constants.ACKNOWLEDGEMENT, location = "newTradeLicense-" + Constants.ACKNOWLEDGEMENT + ".jsp"),
        @Result(name = Constants.PFACERTIFICATE, location = "/WEB-INF/jsp/viewtradelicense/viewTradeLicense-" + Constants.PFACERTIFICATE + ".jsp"),
        @Result(name = Constants.MESSAGE, location = "newTradeLicense-" + Constants.MESSAGE + ".jsp"),
        @Result(name = Constants.BEFORE_RENEWAL, location = "newTradeLicense-" + Constants.BEFORE_RENEWAL + ".jsp"),
        @Result(name = Constants.ACKNOWLEDGEMENT_RENEW, location = "newTradeLicense-" + Constants.ACKNOWLEDGEMENT_RENEW + ".jsp")})
public class NewTradeLicenseAction extends BaseLicenseAction {

    private static final long serialVersionUID = 1L;
    private final Logger LOGGER = Logger.getLogger(this.getClass());

    private TradeLicense tradeLicense = new TradeLicense();
    private List<LicenseDocumentType> documentTypes = new ArrayList<>();
    private Map<String, String> ownerShipTypeMap;
    private String mode;

    @Autowired
    @Qualifier("tradeLicenseService")
    private TradeLicenseService tradeLicenseService;

    public NewTradeLicenseAction() {
        this.tradeLicense.setLicensee(new Licensee());
    }

    @Override
    @SkipValidation
    @Action(value = "/newtradelicense/newTradeLicense-newForm")
    public String newForm() {
        this.tradeLicense.setApplicationDate(new Date());
        return super.newForm();
    }

    @Override
    @ValidationErrorPage(Constants.NEW)
    @Action(value = "/newtradelicense/newTradeLicense-approve")
    public String approve() {

        this.tradeLicense = (TradeLicense) this.persistenceService.find("from TradeLicense where id=?", this.getSession().get("model.id"));
        if (this.mode.equalsIgnoreCase(VIEW) && this.tradeLicense != null && !this.tradeLicense.isPaid() &&
                !this.workFlowAction.equalsIgnoreCase(Constants.BUTTONREJECT)) {
            this.prepareNewForm();
            ValidationError vr = new ValidationError("license.fee.notcollected", "license.fee.notcollected");
            throw new ValidationException(Arrays.asList(vr));
        }
        if (BUTTONAPPROVE.equals(this.workFlowAction)) {
            this.license().setCreationAndExpiryDate();
            if (this.license().getTempLicenseNumber() == null) {
                String nextRunningLicenseNumber = this.tradeLicenseService.getNextRunningLicenseNumber(
                        "egtl_license_number");
                this.license().generateLicenseNumber(nextRunningLicenseNumber);
            }
            LicenseStatus activeStatus = (LicenseStatus) this.persistenceService
                    .find("from org.egov.tl.entity.LicenseStatus where code='ACT'");
            this.license().setStatus(activeStatus);
        }

        return super.approve();
    }

    @Override
    @ValidationErrorPage(Constants.NEW)
    @Action(value = "/newtradelicense/newTradeLicense-create")
    public String create() {
        if (this.LOGGER.isDebugEnabled())
            this.LOGGER.debug("Trade license Creation Parameters:<<<<<<<<<<>>>>>>>>>>>>>:" + this.tradeLicense);
        if (this.tradeLicense.getInstalledMotorList() != null) {
            Iterator<MotorDetails> motorDetails = this.tradeLicense.getInstalledMotorList().iterator();
            while (motorDetails.hasNext()) {
                MotorDetails installedMotor = motorDetails.next();
                if (installedMotor != null && installedMotor.getHp() != null && installedMotor.getNoOfMachines() != null
                        && installedMotor.getHp().compareTo(BigDecimal.ZERO) != 0
                        && installedMotor.getNoOfMachines().compareTo(Long.valueOf("0")) != 0)
                    installedMotor.setLicense(this.tradeLicense);
                else
                    motorDetails.remove();
            }

        }
        if (this.LOGGER.isDebugEnabled())
            this.LOGGER.debug(" Create Trade License Application Name of Establishment :"
                    + this.tradeLicense.getNameOfEstablishment());
        LicenseAppType newAppType = (LicenseAppType) this.persistenceService.find("from  LicenseAppType where name='New' ");
        this.tradeLicense.setLicenseAppType(newAppType);
        return create(this.tradeLicense);
    }

    @Override
    public void prepareNewForm() {
        super.prepareNewForm();
        if (this.license() != null && this.license().getId() != null)
            this.tradeLicense = (TradeLicense) this.persistenceService.find("from TradeLicense where id=?", this.license().getId());
        this.setDocumentTypes(this.tradeLicenseService.getDocumentTypesByTransaction(TRANSACTIONTYPE_CREATE_LICENSE));
        this.tradeLicense.setHotelGradeList(this.tradeLicense.populateHotelGradeList());
        this.tradeLicense.setHotelSubCatList(this.tradeLicenseService.getHotelCategoriesForTrade());
        this.setOwnerShipTypeMap(Constants.OWNERSHIP_TYPE);
        List<Boundary> localityList = this.boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(
                LOCALITY, LOCATION_HIERARCHY_TYPE);
        this.addDropdownData("localityList", localityList);
        this.addDropdownData("tradeTypeList", this.tradeLicenseService.getAllNatureOfBusinesses());
        this.addDropdownData("categoryList", this.licenseCategoryService.findAll());
        this.addDropdownData("uomList", this.unitOfMeasurementService.findAllActiveUOM());

        CommonTradeLicenseAjaxAction ajaxTradeLicenseAction = new CommonTradeLicenseAjaxAction();
        this.populateSubCategoryList(ajaxTradeLicenseAction, this.tradeLicense.getCategory() != null);

    }


    /**
     * @param ajaxTradeLicenseAction
     * @param categoryPopulated
     */
    protected void populateSubCategoryList(CommonTradeLicenseAjaxAction ajaxTradeLicenseAction, boolean categoryPopulated) {
        if (categoryPopulated) {
            ajaxTradeLicenseAction.setCategoryId(this.tradeLicense.getCategory().getId());
            ajaxTradeLicenseAction.setLicenseSubCategoryService(this.licenseSubCategoryService);
            ajaxTradeLicenseAction.populateSubCategory();
            this.addDropdownData("subCategoryList", ajaxTradeLicenseAction.getSubCategoryList());
        } else
            this.addDropdownData("subCategoryList", Collections.emptyList());
    }


    @Override
    @ValidationErrorPage(Constants.BEFORE_RENEWAL)
    @SkipValidation
    @Action(value = "/newtradelicense/newTradeLicense-renewal")
    public String renew() {

            this.LOGGER.debug("Trade license renew Parameters:<<<<<<<<<<>>>>>>>>>>>>>:"
                    + this.tradeLicense);
            BigDecimal deduction = this.tradeLicense.getDeduction();
            BigDecimal otherCharges = this.tradeLicense.getOtherCharges();
            BigDecimal swmFee = this.tradeLicense.getSwmFee();
            this.tradeLicense = this.tradeLicenseService.licensePersitenceService().findById(this.tradeLicense.getId(),false);
            this.tradeLicense.setOtherCharges(otherCharges);
            this.tradeLicense.setDeduction(deduction);
            this.tradeLicense.setSwmFee(swmFee);
            this.LOGGER
                    .debug("Renew Trade License Application Name of Establishment:<<<<<<<<<<>>>>>>>>>>>>>:"
                            + this.tradeLicense.getNameOfEstablishment());
            return super.renew();
    }

    @Override
    @SkipValidation
    @Action(value = "/newtradelicense/newTradeLicense-beforeRenew")
    public String beforeRenew() {
        this.LOGGER
                .debug("Entering in the beforeRenew method:<<<<<<<<<<>>>>>>>>>>>>>:");
        this.tradeLicense = this.tradeLicenseService.licensePersitenceService()
                .find("from License where id=?", this.tradeLicense.getId());

        License license = this.tradeLicense;

        if (!license.getState().getValue().isEmpty()) {
            license.transition(true).withStateValue("");
        }

        this.LOGGER.debug("Exiting from the beforeRenew method:<<<<<<<<<<>>>>>>>>>>>>>:");
        return super.beforeRenew();
    }

    /*
     * Invoked from Workflow users Inbox
     */
    @Override
    @Action(value = "/newtradelicense/newTradeLicense-showForApproval")
    public String showForApproval() {
        this.mode = VIEW;
        return super.showForApproval();
    }

    @Override
    public License getModel() {
        return this.tradeLicense;
    }

    public WorkflowBean getWorkflowBean() {
        return this.workflowBean;
    }

    public void setWorkflowBean(WorkflowBean workflowBean) {
        this.workflowBean = workflowBean;
    }

    @Override
    protected TradeLicense license() {
        return this.tradeLicense;
    }

    @Override
    protected AbstractLicenseService licenseService() {
        return this.tradeLicenseService;
    }

    public List<LicenseDocumentType> getDocumentTypes() {
        return this.documentTypes;
    }

    public void setDocumentTypes(List<LicenseDocumentType> documentTypes) {
        this.documentTypes = documentTypes;
    }

    public Map<String, String> getOwnerShipTypeMap() {
        return this.ownerShipTypeMap;
    }

    public void setOwnerShipTypeMap(Map<String, String> ownerShipTypeMap) {
        this.ownerShipTypeMap = ownerShipTypeMap;
    }

    public String getMode() {
        return this.mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}