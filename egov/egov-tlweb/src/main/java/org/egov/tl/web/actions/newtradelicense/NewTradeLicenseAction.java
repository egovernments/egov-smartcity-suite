/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.utils.DateUtils;
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
import org.egov.tl.web.actions.BaseLicenseAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.egov.tl.utils.Constants.*;

@ParentPackage("egov")
@Results({@Result(name = NewTradeLicenseAction.NEW, location = "newTradeLicense-new.jsp"),
        @Result(name = ACKNOWLEDGEMENT, location = "newTradeLicense-acknowledgement.jsp"),
        @Result(name = MESSAGE, location = "newTradeLicense-message.jsp"),
        @Result(name = BEFORE_RENEWAL, location = "newTradeLicense-beforeRenew.jsp"),
        @Result(name = PRINTACK, location = "newTradeLicense-printAck.jsp"),
        @Result(name = ACKNOWLEDGEMENT_RENEW, location = "newTradeLicense-acknowledgement_renew.jsp")})
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

    @Autowired
    @Qualifier("parentMessageSource")
    private transient MessageSource licenseMessageSource;

    @Autowired
    private transient ReportService reportService;

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
        return super.create(tradeLicense);
    }

    @SkipValidation
    @Action(value = "/newtradelicense/newtradelicense-printAck")
    public String printAck() {
        reportId = reportViewerUtil.addReportToTempCache(
                getReportParamsForAcknowdgement(tradeLicenseService.getLicenseById(tradeLicense.getId())));
        return PRINTACK;
    }

    public ReportOutput getReportParamsForAcknowdgement(final TradeLicense license) {
        final HttpServletRequest request = ServletActionContext.getRequest();
        final String cityName = request.getSession().getAttribute("citymunicipalityname").toString();
        final String city = request.getSession().getAttribute("cityname").toString();
        final Map<String, Object> reportParams = new HashMap<>();
        reportParams.put("municipality", cityName);
        reportParams.put("cityname", city);
        reportParams.put("wardName", license.getParentBoundary().getName());
        reportParams.put("applicantName", license.getLicensee().getApplicantName());
        reportParams.put("acknowledgementNo", license.getApplicationNumber());
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        reportParams.put("currentDate", formatter.format(new Date()));
        reportParams.put("licenceAddress", license.getAddress());
        reportParams.put("dueDate", formatter.format(calculateDueDate(license)));
        reportParams.put("Party's Copy", "Party's Copy");
        reportParams.put("Office's Copy", "Office's Copy");
        reportParams.put("ApplicationCentre", licenseMessageSource.getMessage("msg.application.centre",
                new String[]{license.getApplicationNumber()}, Locale.getDefault()));
        reportParams.put("appType", license.getLicenseAppType() != null
                ? NEW_LIC_APPTYPE.equals(license.getLicenseAppType().getName()) ? "New Trade" : "Renewal of Trade"
                : "New");

        final ReportRequest reportInput = new ReportRequest("tl_acknowledgement", license, reportParams);

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("content-disposition", "inline;filename=Acknowledgement.pdf");
        return reportService.createReport(reportInput);

    }

    public Date calculateDueDate(TradeLicense license) {
        Date dueDate;
        Date currentDate = new Date();
        String slaNewTradeLicense = licenseMessageSource.getMessage("msg.newTradeLicense.sla",
                new String[]{license.getApplicationNumber()}, Locale.getDefault());
        String slaRenewTradeLicense = licenseMessageSource.getMessage("msg.renewTradeLicense.sla",
                new String[]{license.getApplicationNumber()}, Locale.getDefault());
        if (license.isNewApplication())
            dueDate = DateUtils.addDays(currentDate, Integer.parseInt(slaNewTradeLicense));
        else
            dueDate = DateUtils.addDays(currentDate, Integer.parseInt(slaRenewTradeLicense));
        return dueDate;

    }

    @Override
    @Action(value = "/newtradelicense/newTradeLicense-showForApproval")
    @SkipValidation
    public String showForApproval() throws IOException {
        documentTypes = tradeLicenseService.getDocumentTypesByApplicationType(ApplicationType.valueOf(license()
                .getLicenseAppType().getName().toUpperCase()));
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
            else if (licenseUtils.isDigitalSignEnabled()
                    && license().getState().getValue().equals(DIGI_ENABLED_WF_SECOND_LVL_FEECOLLECTED)
                    || license().getState().getValue().equals(WF_DIGI_SIGNED)
                    || license().getState().getValue().equals(WF_ACTION_DIGI_SIGN_COMMISSION_NO_COLLECTION))
                mode = DISABLE_APPROVER_MODE;
        }
        if (license().isCollectionPending() || STATUS_ACKNOWLEDGED.equals(license().getStatus().getStatusCode()))
            message = PENDING_COLLECTION_MSG;
        List<Position> positionList = positionMasterService
                .getPositionsForEmployee(securityUtils.getCurrentUser().getId(), new Date());
        Position owner = license().currentAssignee();
        if (!positionList.isEmpty() && !positionList.contains(owner)) {
            ServletActionContext.getResponse().setContentType("text/html");
            ServletActionContext.getResponse().getWriter()
                    .write("<center style='color:red;font-weight:bolder'>Workflow item is in "
                            + owner.getName() + " inbox !</center>");
            return null;
        }
        licenseHistory = tradeLicenseService.populateHistory(tradeLicense);
        return super.showForApproval();
    }

    @Override
    @ValidationErrorPageExt(action = NEW, makeCall = true, toMethod = "prepareShowForApproval")
    @Action(value = "/newtradelicense/newTradeLicense-approve")
    public String approve() {
        if ("Submit".equals(workFlowAction) && mode.equalsIgnoreCase(VIEW)
                && tradeLicense.getState().getValue().contains(WF_STATE_COMMISSIONER_APPROVED_STR)
                && !tradeLicense.isPaid() && !workFlowAction.equalsIgnoreCase(BUTTONREJECT)) {
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
        documentTypes = tradeLicenseService.getDocumentTypesByApplicationType(ApplicationType.RENEW);
        if (tradeLicense.hasState() && !tradeLicense.transitionCompleted()) {
            ServletActionContext.getResponse().setContentType("text/html");
            ServletActionContext.getResponse().getWriter()
                    .write("<center style='color:red;font-weight:bolder'>Renewal workflow is in progress !</center>");
            return null;
        }
        if (!tradeLicense.hasState() || (tradeLicense.hasState() && tradeLicense.getCurrentState().isEnded()))
            currentState = "";
        tradeLicense.setNewWorkflow(true);
        renewAppType = RENEWAL_LIC_APPTYPE;
        return super.beforeRenew();
    }

    @Override
    @ValidationErrorPageExt(action = BEFORE_RENEWAL, makeCall = true, toMethod = "prepareRenew")
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
        documentTypes = tradeLicenseService.getDocumentTypesByApplicationType(ApplicationType.NEW);
        setOwnerShipTypeMap(getOwnershipTypes());
        final List<Boundary> localityList = boundaryService
                .getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(LOCALITY, LOCATION_HIERARCHY_TYPE);
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
        if (tradeLicense.isNewWorkflow()) {
            if (!securityUtils.currentUserIsEmployee())
                return RENEWAL_LIC_APPTYPE.equals(renewAppType) ? CSCOPERATORRENEWLICENSE : CSCOPERATORNEWLICENSE;
            else if (license().isCollectionPending())
                return tradeLicense.isNewApplication() ? NEWLICENSECOLLECTION : RENEWLICENSECOLLECTION;
            else if (RENEWAL_LIC_APPTYPE.equals(renewAppType) || tradeLicense != null && tradeLicense.isReNewApplication())
                return RENEWLICENSE;
            else
                return NEWLICENSE;
        } else {//TODO to be removed
            if (RENEWAL_LIC_APPTYPE.equals(renewAppType) || tradeLicense != null && tradeLicense.isReNewApplication())
                return RENEW_ADDITIONAL_RULE;
            else
                return NEW_ADDITIONAL_RULE;
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public void prepareSave() {
        tradeLicense.setId((Long) getSession().get("model.id"));
        prepareNewForm();
    }

    @ValidationErrorPage(NEW)
    @Action(value = "/newtradelicense/newTradeLicense-save")
    public String save() {
        tradeLicenseService.save(tradeLicense);
        addActionMessage(this.getText("license.saved.succesful"));
        return MESSAGE;
    }

    public void prepareApprove() {
        tradeLicense.setId((Long) getSession().get("model.id"));
        prepareNewForm();
    }
}