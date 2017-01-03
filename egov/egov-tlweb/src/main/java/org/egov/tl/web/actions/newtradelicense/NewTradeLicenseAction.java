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

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.web.struts.annotation.ValidationErrorPageExt;
import org.egov.pims.commons.Position;
import org.egov.tl.entity.License;
import org.egov.tl.entity.LicenseDocumentType;
import org.egov.tl.entity.Licensee;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.entity.WorkflowBean;
import org.egov.tl.entity.enums.ApplicationType;
import org.egov.tl.service.AbstractLicenseService;
import org.egov.tl.service.TradeLicenseService;
import org.egov.tl.utils.Constants;
import org.egov.tl.web.actions.BaseLicenseAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.egov.tl.utils.Constants.LOCALITY;
import static org.egov.tl.utils.Constants.LOCATION_HIERARCHY_TYPE;

@ParentPackage("egov")
@Results({@Result(name = NewTradeLicenseAction.NEW, location = "newTradeLicense-new.jsp"),
        @Result(name = Constants.ACKNOWLEDGEMENT, location = "newTradeLicense-acknowledgement.jsp"),
        @Result(name = Constants.MESSAGE, location = "newTradeLicense-message.jsp"),
        @Result(name = Constants.BEFORE_RENEWAL, location = "newTradeLicense-beforeRenew.jsp"),
        @Result(name = Constants.ACKNOWLEDGEMENT_RENEW, location = "newTradeLicense-acknowledgement_renew.jsp")})
public class NewTradeLicenseAction extends BaseLicenseAction<TradeLicense> {

    private static final long serialVersionUID = 1L;

    private TradeLicense tradeLicense = new TradeLicense();
    private List<LicenseDocumentType> documentTypes = new ArrayList<>();
    private Map<String, String> ownerShipTypeMap;
    private String mode;
    private String message;
    private String renewAppType;
    @Autowired
    @Qualifier("tradeLicenseService")
    private transient TradeLicenseService tradeLicenseService;


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

    @ValidationErrorPage(Constants.NEW)
    @Action(value = "/newtradelicense/newTradeLicense-create")
    public String create() {
        return super.create(tradeLicense);
    }

    @Override
    @Action(value = "/newtradelicense/newTradeLicense-showForApproval")
    @SkipValidation
    public String showForApproval() throws IOException {
        if (license().getState().getValue().equals(Constants.WF_LICENSE_CREATED) ||
                license().getState().getValue().contains(Constants.WF_STATE_COMMISSIONER_APPROVED_STR)
                        && license().getEgwStatus().getCode().equals(Constants.APPLICATION_STATUS_SECONDCOLLECTION_CODE))
            mode = Constants.ACK_MODE;
        else if (license().getState().getValue().equals(Constants.WF_FIRST_LVL_FEECOLLECTED)
                || license().getState().getValue().equals(Constants.WF_SI_APPROVED))
            mode = VIEW;
        else if (license().getState().getValue().contains(Constants.WORKFLOW_STATE_REJECTED))
            mode = Constants.EDIT_REJECT_MODE;
        else if (license().getState().getValue().contains(Constants.WF_REVENUECLERK_APPROVED))
            mode = Constants.EDIT_APPROVAL_MODE;
        else if (license().getState().getValue().contains(Constants.WF_STATE_DIGISIGN_STR)
                || license().getState().getValue().contains(Constants.WF_STATE_INSPECTION_APPROVED_STR)
                || license().getState().getValue().contains(Constants.WF_SECOND_LVL_FEECOLLECTED)
                || license().getEgwStatus().getCode().equals(Constants.APPLICATION_STATUS_APPROVED_CODE)
                && license().getState().getValue().contains(Constants.WF_STATE_COMMISSIONER_APPROVED_STR))
            mode = Constants.DISABLE_APPROVER_MODE;
        else if (licenseUtils.isDigitalSignEnabled() &&
                license().getState().getValue().equals(Constants.DIGI_ENABLED_WF_SECOND_LVL_FEECOLLECTED)
                || license().getState().getValue().equals(Constants.WF_DIGI_SIGNED)
                || license().getState().getValue().equals(Constants.WF_ACTION_DIGI_SIGN_COMMISSION_NO_COLLECTION))
            mode = Constants.DISABLE_APPROVER_MODE;
        if (license().getState().getValue().contains(Constants.WF_STATE_COMMISSIONER_APPROVED_STR)
                && license().getEgwStatus().getCode().equals(Constants.APPLICATION_STATUS_SECONDCOLLECTION_CODE)
                || license().getState().getValue().contains(Constants.WF_LICENSE_CREATED))
            message = Constants.PENDING_COLLECTION_MSG;
        List<Position> positionList = positionMasterService.getPositionsForEmployee(securityUtils.getCurrentUser().getId(), new Date());
        if (!positionList.isEmpty() && !positionList.contains(license().getState().getOwnerPosition())) {
            ServletActionContext.getResponse().setContentType("text/html");
            ServletActionContext.getResponse().getWriter()
                    .write("<center style='color:red;font-weight:bolder'>Workflow item is in "
                            + license().getCurrentState().getOwnerPosition().getName() + " inbox !</center>");
            return null;
        }
        return super.showForApproval();
    }

    @Override
    @ValidationErrorPageExt(action = Constants.NEW, makeCall = true, toMethod = "prepareShowForApproval")
    @Action(value = "/newtradelicense/newTradeLicense-approve")
    public String approve() {
        final BigDecimal newTradeAreWt = tradeLicense.getTradeArea_weight();
        tradeLicense = tradeLicenseService.getLicenseById((Long) getSession().get("model.id"));
        if (null != license().getState()
                && license().getState().getValue().contains(Constants.WF_REVENUECLERK_APPROVED))
            tradeLicense.setTradeArea_weight(newTradeAreWt);
        if ("Submit".equals(workFlowAction) && mode.equalsIgnoreCase(VIEW)
                && tradeLicense.getState().getValue().contains(Constants.WF_STATE_COMMISSIONER_APPROVED_STR)
                && tradeLicense != null
                && !tradeLicense.isPaid() &&
                !workFlowAction.equalsIgnoreCase(Constants.BUTTONREJECT)) {
            prepareNewForm();
            final ValidationError vr = new ValidationError("license.fee.notcollected", "license.fee.notcollected");
            throw new ValidationException(Arrays.asList(vr));
        }
        return super.approve();
    }

    @Override
    @SkipValidation
    @Action(value = "/newtradelicense/newTradeLicense-beforeRenew")
    public String beforeRenew() throws IOException {
        prepareNewForm();
        setDocumentTypes(tradeLicenseService.getDocumentTypesByApplicationType(ApplicationType.NEW));
        if (tradeLicense.getEgwStatus() != null
                && !tradeLicense.getEgwStatus().getCode().equalsIgnoreCase(Constants.APPLICATION_STATUS_SECONDCOLLECTION_CODE)
                && tradeLicense.getLicenseAppType() != null
                && tradeLicense.getLicenseAppType().getName().equals(Constants.RENEWAL_LIC_APPTYPE)) {
            ServletActionContext.getResponse().setContentType("text/html");
            ServletActionContext.getResponse().getWriter()
                    .write("<center style='color:red;font-weight:bolder'>Renewal workflow is in progress !</center>");
            return null;
        }
        if (!tradeLicense.hasState() || "Closed".equals(tradeLicense.getCurrentState().getValue()))
            currentState = "";
        renewAppType = Constants.RENEWAL_LIC_APPTYPE;
        return super.beforeRenew();
    }

    @Override
    @ValidationErrorPageExt(action = Constants.BEFORE_RENEWAL, makeCall = true, toMethod = "prepareRenew")
    @Action(value = "/newtradelicense/newTradeLicense-renewal")
    public String renew() {
        return super.renew();
    }

    public void prepareRenew() {
        prepareNewForm();
    }

    @Override
    public void prepareNewForm() {
        super.prepareNewForm();
        if (license() != null && license().getId() != null)
            tradeLicense = tradeLicenseService.getLicenseById(license().getId());
        setDocumentTypes(tradeLicenseService.getDocumentTypesByApplicationType(ApplicationType.NEW));
        setOwnerShipTypeMap(Constants.OWNERSHIP_TYPE);
        final List<Boundary> localityList = boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(
                LOCALITY, LOCATION_HIERARCHY_TYPE);
        addDropdownData("localityList", localityList);
        addDropdownData("tradeTypeList", tradeLicenseService.getAllNatureOfBusinesses());
        addDropdownData("categoryList", licenseCategoryService.getCategories());
        addDropdownData("uomList", unitOfMeasurementService.getAllActiveUOM());
        addDropdownData("subCategoryList", tradeLicense.getCategory() == null ? Collections.emptyList()
                : licenseSubCategoryService.getSubCategoriesByCategory(tradeLicense.getCategory().getId()));
        if (license() != null && license().getAgreementDate() != null)
            setShowAgreementDtl(true);
    }

    @Override
    public License getModel() {
        return tradeLicense;
    }

    public WorkflowBean getWorkflowBean() {
        return workflowBean;
    }

    public void setWorkflowBean(final WorkflowBean workflowBean) {
        this.workflowBean = workflowBean;
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

    public String getMode() {
        return mode;
    }

    public void setMode(final String mode) {
        this.mode = mode;
    }

    @Override
    public String getAdditionalRule() {
        if (renewAppType != null && renewAppType.equals(Constants.RENEWAL_LIC_APPTYPE)
                || tradeLicense != null && tradeLicense.getLicenseAppType() != null
                && tradeLicense.getLicenseAppType().getName().equals(Constants.RENEWAL_LIC_APPTYPE))
            return Constants.RENEW_ADDITIONAL_RULE;
        else
            return Constants.NEW_ADDITIONAL_RULE;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }
}