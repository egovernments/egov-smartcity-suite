/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */

package org.egov.tl.web.actions.newtradelicense;

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
import org.egov.infra.web.struts.annotation.ValidationErrorPageExt;
import org.egov.pims.commons.Position;
import org.egov.tl.entity.LicenseDocumentType;
import org.egov.tl.entity.Licensee;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.entity.WorkflowBean;
import org.egov.tl.service.LicenseCategoryService;
import org.egov.tl.service.LicenseService;
import org.egov.tl.service.LicenseSubCategoryService;
import org.egov.tl.service.UnitOfMeasurementService;
import org.egov.tl.web.actions.BaseLicenseAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static org.egov.tl.utils.Constants.*;

@ParentPackage("egov")
@Results({@Result(name = NewTradeLicenseAction.NEW, location = "newTradeLicense-new.jsp"),
        @Result(name = ACKNOWLEDGEMENT, location = "newTradeLicense-acknowledgement.jsp"),
        @Result(name = NewTradeLicenseAction.ERROR, location = "newTradeLicense-error.jsp"),
        @Result(name = NewTradeLicenseAction.SUCCESS, location = "newTradeLicense-message.jsp"),
        @Result(name = MESSAGE, type = "redirectAction", location = "success", params = {"message", "${message}"}),
        @Result(name = BEFORE_RENEWAL, location = "newTradeLicense-beforeRenew.jsp"),
        @Result(name = PRINTACK, location = "newTradeLicense-printAck.jsp")})
public class NewTradeLicenseAction extends BaseLicenseAction {

    private static final long serialVersionUID = 1L;

    private String mode;
    private String renewAppType;
    private transient List<LicenseDocumentType> documentTypes = new ArrayList<>();
    private transient Map<String, String> ownerShipTypeMap;

    @Autowired
    private transient BoundaryService boundaryService;
    @Autowired
    @Qualifier("licenseCategoryService")
    private transient LicenseCategoryService licenseCategoryService;
    @Autowired
    @Qualifier("licenseSubCategoryService")
    private transient LicenseSubCategoryService licenseSubCategoryService;
    @Autowired
    @Qualifier("unitOfMeasurementService")
    private transient UnitOfMeasurementService unitOfMeasurementService;

    @Autowired
    private transient LicenseService licenseService;

    public NewTradeLicenseAction() {
        tradeLicense.setLicensee(new Licensee());
    }

    @Override
    @SkipValidation
    @Action(value = "/newtradelicense/newTradeLicense-newForm")
    public String newForm() {
        tradeLicense.setNewWorkflow(true);
        tradeLicense.setApplicationDate(new Date());
        return super.newForm();
    }

    @ValidationErrorPage(NEW)
    @Action(value = "/newtradelicense/newTradeLicense-create")
    public String create() {
        supportDocumentsValidation();
        return super.create(tradeLicense);
    }

    @SkipValidation
    @Action(value = "/newtradelicense/newtradelicense-printAck")
    public String printAck() {
        reportId = reportViewerUtil.addReportToTempCache(
                licenseService.generateAcknowledgement(tradeLicenseService.getLicenseById(tradeLicense.getId())));
        return PRINTACK;
    }

    @Override
    @Action(value = "/newtradelicense/newTradeLicense-showForApproval")
    @SkipValidation
    @ValidationErrorPage(ERROR)
    public String showForApproval() {
        documentTypes = this.licenseDocumentTypeService.getDocumentTypesByApplicationType(license().getLicenseAppType());
        if (!license().isNewWorkflow()) {
            if (license().getState().getValue().equals(WF_LICENSE_CREATED)
                    || license().getState().getValue().contains(WF_STATE_COMMISSIONER_APPROVED_STR) && license()
                    .getEgwStatus().getCode().equals(APPLICATION_STATUS_SECONDCOLLECTION_CODE)) {
                mode = ACK_MODE;
                message = PENDING_COLLECTION_MSG;
            } else if (license().getState().getValue().equals(WF_FIRST_LVL_FEECOLLECTED)
                    || license().getState().getValue().equals(WF_SI_APPROVED))
                mode = VIEW;
            else if (license().getState().getValue().contains(WORKFLOW_STATE_REJECTED))
                mode = EDIT_REJECT_MODE;
            else if (license().getState().getValue().contains(WF_REVENUECLERK_APPROVED))
                mode = EDIT_APPROVAL_MODE;
            else if (license().getState().getValue().contains(WF_SECOND_LVL_FEECOLLECTED)
                    || license().getEgwStatus().getCode().equals(APPLICATION_STATUS_APPROVED_CODE)
                    && license().getState().getValue().contains(WF_STATE_COMMISSIONER_APPROVED_STR))
                mode = DISABLE_APPROVER_MODE;
            else if (this.isDigitalSignatureEnabled()
                    && license().getState().getValue().equals(DIGI_ENABLED_WF_SECOND_LVL_FEECOLLECTED)
                    || license().getState().getValue().equals(WF_DIGI_SIGNED)
                    || license().getState().getValue().equals(WF_ACTION_DIGI_SIGN_COMMISSION_NO_COLLECTION))
                mode = DISABLE_APPROVER_MODE;
        }
        if ((STATUS_COLLECTIONPENDING.equals(license().getStatus().getStatusCode())
                || STATUS_ACKNOWLEDGED.equals(license().getStatus().getStatusCode())) && !license().isPaid())
            message = PENDING_COLLECTION_MSG;
        List<Position> positionList = positionMasterService
                .getPositionsForEmployee(securityUtils.getCurrentUser().getId(), new Date());
        Position owner = license().currentAssignee();
        if (!positionList.isEmpty() && !positionList.contains(owner)) {
            throw new ValidationException("error.workflow.inprogress", "License application is with " + owner.getName());
        }
        licenseHistory = tradeLicenseService.populateHistory(tradeLicense);
        return super.showForApproval();
    }

    @Override
    @ValidationErrorPageExt(action = NEW, makeCall = true, toMethod = "prepareShowForApproval")
    @Action(value = "/newtradelicense/newTradeLicense-approve")
    public String approve() {
        supportDocumentsValidationForApproval(tradeLicense);
        if ("Submit".equals(workFlowAction) && mode.equalsIgnoreCase(VIEW)
                && tradeLicense.getState().getValue().contains(WF_STATE_COMMISSIONER_APPROVED_STR)
                && !tradeLicense.isPaid() && !workFlowAction.equalsIgnoreCase(BUTTONREJECT)) {
            prepareNewForm();
            final ValidationError vr = new ValidationError("license.fee.notcollected", "license.fee.notcollected");
            throw new ValidationException(Arrays.asList(vr));
        }
        if (SIGNWORKFLOWACTION.equals(workFlowAction) && !this.isDigitalSignatureEnabled())
            throw new ValidationException("error.digisign.disabled", "error.digisign.disabled");
        return super.approve();
    }

    public void prepareShowForApproval() {
        prepareNewForm();
        licenseHistory = tradeLicenseService.populateHistory(tradeLicense);
        documentTypes = this.licenseDocumentTypeService.getDocumentTypesByApplicationType(license().getLicenseAppType());
    }

    @Override
    @SkipValidation
    @Action(value = "/newtradelicense/newTradeLicense-beforeRenew")
    @ValidationErrorPage(ERROR)
    public String beforeRenew() {
        prepareNewForm();
        documentTypes = licenseDocumentTypeService.getDocumentTypesForRenewApplicationType();
        if (tradeLicense.hasState() && !tradeLicense.transitionCompleted()) {
            throw new ValidationException(WF_INPROGRESS_ERROR_CODE,
                    format(WF_INPROGRESS_ERROR_MSG_FORMAT, tradeLicense.getLicenseAppType().getName()));
        }
        if (!tradeLicense.hasState() || (tradeLicense.hasState() && tradeLicense.getCurrentState().isEnded()))
            currentState = "";
        tradeLicense.setNewWorkflow(true);
        renewAppType = RENEW_APPTYPE_CODE;
        return super.beforeRenew();
    }

    @Override
    @ValidationErrorPageExt(action = BEFORE_RENEWAL, makeCall = true, toMethod = "prepareRenew")
    @Action(value = "/newtradelicense/newTradeLicense-renewal")
    public String renew() {
        supportDocumentsValidation();
        return super.renew();
    }

    public void prepareRenew() {
        prepareNewForm();
        renewAppType = RENEW_APPTYPE_CODE;
        documentTypes = licenseDocumentTypeService.getDocumentTypesForRenewApplicationType();
    }

    @Override
    public void prepareNewForm() {
        super.prepareNewForm();
        if (license() != null && license().getId() != null)
            tradeLicense = tradeLicenseService.getLicenseById(license().getId());
        documentTypes = licenseDocumentTypeService.getDocumentTypesForNewApplication();
        setOwnerShipTypeMap(OWNERSHIP_TYPE);
        final List<Boundary> localityList = boundaryService
                .getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(LOCALITY, LOCATION_HIERARCHY_TYPE);
        addDropdownData("localityList", localityList);
        addDropdownData("tradeTypeList", tradeLicenseService.getAllNatureOfBusinesses());
        addDropdownData("categoryList", licenseCategoryService.getCategoriesOrderByName());
        addDropdownData("uomList", unitOfMeasurementService.getAllActiveUOM());
        addDropdownData("subCategoryList", tradeLicense.getCategory() == null ? Collections.emptyList()
                : licenseSubCategoryService.getSubCategoriesByCategory(tradeLicense.getCategory().getId()));
        if (license() != null && license().getAgreementDate() != null)
            setShowAgreementDtl(true);
    }

    @Override
    public TradeLicense getModel() {
        return tradeLicense;
    }

    public WorkflowBean getWorkflowBean() {
        return workflowBean;
    }

    public void setWorkflowBean(final WorkflowBean workflowBean) {
        this.workflowBean = workflowBean;
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

    public String getMode() {
        return mode;
    }

    public void setMode(final String mode) {
        this.mode = mode;
    }

    @Override
    public String getAdditionalRule() {
        if (tradeLicense.isNewWorkflow()) {
            if (!securityUtils.currentUserIsEmployee())
                return RENEW_APPTYPE_CODE.equals(renewAppType) ? CSCOPERATORRENEWLICENSE : CSCOPERATORNEWLICENSE;
            else if (license().isCollectionPending())
                return tradeLicense.isNewApplication() ? NEWLICENSECOLLECTION : RENEWLICENSECOLLECTION;
            else if (RENEW_APPTYPE_CODE.equals(renewAppType) || tradeLicense != null && tradeLicense.isReNewApplication())
                return RENEWLICENSE;
            else
                return NEWLICENSE;
        } else {
            if (RENEW_APPTYPE_CODE.equals(renewAppType) || tradeLicense.isReNewApplication())
                return RENEW_ADDITIONAL_RULE;
            else
                return NEW_ADDITIONAL_RULE;
        }
    }


    public void prepareSave() {
        prepareNewForm();
    }

    @ValidationErrorPageExt(action = NEW, makeCall = true, toMethod = "prepareShowForApproval")
    @Action(value = "/newtradelicense/newTradeLicense-save")
    public String save() {
        supportDocumentsValidationForApproval(tradeLicense);
        addNewDocuments();
        tradeLicenseService.save(tradeLicense);
        setMessage(this.getText("license.saved.succesful"));
        return MESSAGE;
    }

    public void prepareApprove() {
        prepareNewForm();
    }

    @Action(value = "/newtradelicense/success")
    public String successPage() {
        return SUCCESS;
    }
}