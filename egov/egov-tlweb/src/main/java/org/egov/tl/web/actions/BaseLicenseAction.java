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

package org.egov.tl.web.actions;

import static org.egov.tl.utils.Constants.APPROVE_PAGE;
import static org.egov.tl.utils.Constants.GENERATE_CERTIFICATE;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Installment;
import org.egov.demand.model.EgDemandDetails;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.eis.service.EisCommonService;
import org.egov.eis.service.PositionMasterService;
import org.egov.eis.web.actions.workflow.GenericWorkFlowAction;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.persistence.entity.enums.UserType;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.reporting.viewer.ReportViewerUtil;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.NumberToWord;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.pims.commons.Position;
import org.egov.tl.entity.License;
import org.egov.tl.entity.LicenseCategory;
import org.egov.tl.entity.LicenseDemand;
import org.egov.tl.entity.LicenseSubCategory;
import org.egov.tl.entity.NatureOfBusiness;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.entity.WorkflowBean;
import org.egov.tl.service.AbstractLicenseService;
import org.egov.tl.service.FeeTypeService;
import org.egov.tl.service.TradeLicenseService;
import org.egov.tl.service.TradeLicenseSmsAndEmailService;
import org.egov.tl.service.LicenseCategoryService;
import org.egov.tl.service.LicenseSubCategoryService;
import org.egov.tl.service.UnitOfMeasurementService;
import org.egov.tl.utils.Constants;
import org.egov.tl.utils.LicenseUtils;
import org.egov.tl.web.actions.domain.CommonAjaxAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

import java.util.List;
import java.util.Map;

@ParentPackage("egov")
@Results({
        @Result(name = "collection", type = "redirectAction", location = "licenseBillCollect", params = {"namespace",
                "/integration", "method", "renew"}),
        @Result(name = "tl_editlicense", type = "redirectAction", location = "editTradeLicense-beforeEdit", params = {
                "namespace", "/newtradelicense"}),
        @Result(name = "tl_approve", type = "redirectAction", location = "viewTradeLicense", params = {"namespace",
                "/viewtradelicense", "method", "showForApproval"}),
        @Result(name = "tl_generateRejCertificate", type = "redirectAction", location = "viewTradeLicense", params = {
                "namespace", "/viewtradelicense", "method", "generateRejCertificate"}),
        @Result(name = "tl_generateCertificate", type = "redirectAction", location = "viewTradeLicense", params = {
                "namespace", "/viewtradelicense", "method", "generateCertificate"}),
        @Result(name = "approve", location = "newTradeLicense-new.jsp"),
        @Result(name = "report", location = "newTradeLicense-report.jsp"),
        @Result(name = "digitalSignatureRedirection", location = "newTradeLicense-digitalSignatureRedirection.jsp")})
public abstract class BaseLicenseAction<T extends License> extends GenericWorkFlowAction {
    private static final long serialVersionUID = 1L;

    protected WorkflowBean workflowBean = new WorkflowBean();
    protected List<String> buildingTypeList;
    protected String roleName;
    protected String reportId;
    protected boolean showAgreementDtl;
    @Autowired
    protected LicenseUtils licenseUtils;
    @Autowired
    protected PositionMasterService positionMasterService;
    @Autowired
    protected SecurityUtils securityUtils;
    @Autowired
    protected AssignmentService assignmentService;
    @Autowired
    protected BoundaryService boundaryService;
    @Autowired
    protected DesignationService designationService;
    @Autowired
    protected EisCommonService eisCommonService;
    @Autowired
    protected UserService userService;
    @Autowired
    @Qualifier("licenseCategoryService")
    protected LicenseCategoryService licenseCategoryService;
    @Autowired
    @Qualifier("licenseSubCategoryService")
    protected LicenseSubCategoryService licenseSubCategoryService;
    @Autowired
    @Qualifier("unitOfMeasurementService")
    protected UnitOfMeasurementService unitOfMeasurementService;
    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;
    private Long feeTypeId;
    private String fileStoreIds;
    private String ulbCode;
    private String signedFileStoreId;
    private TradeLicenseSmsAndEmailService tradeLicenseSmsAndEmailService;
    @Autowired
    private TradeLicenseService tradeLicenseService;
    @Autowired
    private ReportService reportService;
    @Autowired
    @Qualifier("feeTypeService")
    private FeeTypeService feeTypeService;
    @Autowired
    private ReportViewerUtil reportViewerUtil;

    public BaseLicenseAction() {
        this.addRelatedEntity("boundary", Boundary.class);
        this.addRelatedEntity("parentBoundary", Boundary.class);
        this.addRelatedEntity("licensee.boundary", Boundary.class);
        this.addRelatedEntity("natureOfBusiness", NatureOfBusiness.class);
        this.addRelatedEntity("category", LicenseCategory.class);
        this.addRelatedEntity("tradeName", LicenseSubCategory.class);
    }

    protected abstract T license();

    protected abstract AbstractLicenseService<T> licenseService();

    @ValidationErrorPage(Constants.NEW)
    public String create(final T license) {
        populateWorkflowBean();
        licenseService().create(license, workflowBean);
        addActionMessage(this.getText("license.submission.succesful") + license().getApplicationNumber());
        return Constants.ACKNOWLEDGEMENT;
    }

    @ValidationErrorPage(Constants.NEW)
    public String enterExisting(final T license, final Map<Integer, Integer> legacyInstallmentwiseFees,
                                final Map<Integer, Boolean> legacyFeePayStatus) {
        licenseService().createLegacyLicense(license, legacyInstallmentwiseFees, legacyFeePayStatus);
        addActionMessage(this.getText("license.entry.succesful") + "  " + license().getLicenseNumber());

        return "viewlicense";
    }

    // sub class should get the object of the model and set to license()
    public String approve() {

        if (workFlowAction.equals(Constants.WF_PREVIEW_BUTTON))
            return redirectToPrintCertificate();
        if (workFlowAction.equals(Constants.SIGNWORKFLOWACTION))
            return digitalSignRedirection();
        tradeLicenseService.updateStatusInWorkFlowProgress((TradeLicense) license(), workFlowAction);
        processWorkflow(NEW);
        tradeLicenseService.updateTradeLicense((TradeLicense) license(), workflowBean);
        if (Constants.GENERATECERTIFICATE.equalsIgnoreCase(workflowBean.getWorkFlowAction()))
            return redirectToPrintCertificate();
        else
            return "message";

    }

    private String redirectToPrintCertificate() {
        reportId = reportViewerUtil.addReportToTempCache(reportService.createReport(tradeLicenseService.prepareReportInputData(license())));
        return "report";
    }

    private String digitalSignRedirection() {
        final HttpServletRequest request = ServletActionContext.getRequest();
        final String cityMunicipalityName = (String) request.getSession().getAttribute("citymunicipalityname");
        final String districtName = (String) request.getSession().getAttribute("districtName");
        ReportOutput reportOutput = tradeLicenseService.prepareReportInputDataForDig(license(), districtName, cityMunicipalityName);

        if (reportOutput != null) {
            String fileName = Constants.SIGNED_DOCUMENT_PREFIX + license().getApplicationNumber() + ".pdf";
            final InputStream fileStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
            final FileStoreMapper fileStore = fileStoreService.store(fileStream, fileName, "application/pdf", Constants.FILESTORE_MODULECODE);
            license().setDigiSignedCertFileStoreId(fileStore.getFileStoreId());
            licenseService().save(license());
            setFileStoreIds(fileStore.getFileStoreId());
            setUlbCode(ApplicationThreadLocals.getCityCode());
        }
        return "digitalSignatureRedirection";
    }

    @SkipValidation
    public String approveRenew() {
        processWorkflow(Constants.RENEWAL_LIC_APPTYPE);
        return "message";
    }

    protected void populateWorkflowBean() {
        workflowBean.setApproverPositionId(approverPositionId);
        workflowBean.setApproverComments(approverComments);
        workflowBean.setWorkFlowAction(workFlowAction);
        workflowBean.setCurrentState(currentState);
        workflowBean.setAdditionaRule(additionalRule);
    }

    @SkipValidation
    public String beforeRenew() throws IOException {
        return Constants.BEFORE_RENEWAL;
    }

    @SkipValidation
    public String renew() {
        populateWorkflowBean();
        licenseService().renew(license(), workflowBean);
        addActionMessage(this.getText("license.renew.submission.succesful") + license().getLicenseNumber());
        return Constants.ACKNOWLEDGEMENT_RENEW;
    }

    @SkipValidation
    public String enterExistingForm() {
        return Constants.NEW;
    }

    public void prepareEnterExistingForm() {
        prepareNewForm();
    }

    // create workflow and pushes to drafts
    public void initiateWorkFlowForLicense() {
        final Position position = positionMasterService.getCurrentPositionForUser(ApplicationThreadLocals.getUserId());
        if (position != null)
            addActionMessage(this.getText("license.saved.in.inbox"));
    }

    public List<String> getBuildingTypeList() {
        return buildingTypeList;
    }

    public void setBuildingTypeList(final List<String> buildingTypeList) {
        this.buildingTypeList = buildingTypeList;
    }

    @Override
    public StateAware getModel() {
        return license();
    }

    @SkipValidation
    public String newForm() {
        return Constants.NEW;
    }

    public void prepareCreate() {
        prepareNewForm();
    }

    public void prepareNewForm() {
        prepare();
        buildingTypeList = new ArrayList<>();
        buildingTypeList.add(Constants.BUILDINGTYPE_OWN_BUILDING);
        buildingTypeList.add(Constants.BUILDINGTYPE_RENTAL_AGREEMANT);
        addDropdownData(Constants.DROPDOWN_AREA_LIST_LICENSE, Collections.emptyList());
        addDropdownData(Constants.DROPDOWN_AREA_LIST_LICENSEE, Collections.emptyList());
        addDropdownData(Constants.DROPDOWN_DIVISION_LIST_LICENSE, Collections.emptyList());
        addDropdownData(Constants.DROPDOWN_DIVISION_LIST_LICENSEE, Collections.emptyList());
        if (getModel().getClass().getSimpleName().equalsIgnoreCase(Constants.ELECTRICALLICENSE_LICENSETYPE))
            addDropdownData(Constants.DROPDOWN_TRADENAME_LIST, Collections.emptyList());
        else
            addDropdownData(Constants.DROPDOWN_TRADENAME_LIST,
                    licenseUtils.getAllTradeNames(getModel().getClass().getSimpleName()));

        setupWorkflowDetails();
        feeTypeId = feeTypeService.findByName(Constants.LICENSE_FEE_TYPE).getId();
    }

    public void prepareShowForApproval() {
        prepareNewForm();
    }

    /**
     * should be called from the second level only Approve will not end workflow instead it sends to the creator in approved state
     */
    public void processWorkflow(final String processType) {
        populateWorkflowBean();
        // Both New And Renew Workflow handling in same API(transitionWorkFlow)
        if (processType.equalsIgnoreCase(NEW) && !Constants.BUTTONSUBMIT.equals(workFlowAction))
            licenseService().transitionWorkFlow(license(), workflowBean);
        if (workflowBean.getWorkFlowAction().equalsIgnoreCase(Constants.BUTTONAPPROVE)) {
            addActionMessage(this.getText("license.approved.success"));
        } else if (workflowBean.getWorkFlowAction().equalsIgnoreCase(Constants.BUTTONFORWARD)) {
            List<Assignment> assignments = assignmentService.getAssignmentsForPosition(workflowBean.getApproverPositionId());
            String nextDesgn = !assignments.isEmpty() ? assignments.get(0).getDesignation().getName() : "";
            final String userName = !assignments.isEmpty() ? assignments.get(0).getEmployee().getName() : "";
            addActionMessage(this.getText("license.sent") + " " + nextDesgn + " - " + userName);
        } else if (workflowBean.getWorkFlowAction().equalsIgnoreCase(Constants.BUTTONREJECT)) {
            rejectActionMessage();
        } else if (workflowBean.getWorkFlowAction().equalsIgnoreCase(Constants.BUTTONGENERATEDCERTIFICATE))
            addActionMessage(this.getText("license.certifiacte.print.complete.recorded"));
    }

    private void rejectActionMessage() {
        User user = getInitiatorUserObj();
        if (license().getState().getValue().contains(Constants.WORKFLOW_STATE_REJECTED)) {
            Position creatorPosition = license().getState().getInitiatorPosition();
            addActionMessage(this.getText("license.rejectedfirst") + (creatorPosition.getDeptDesig().getDesignation().getName() + " - ") + " "
                    + user != null ? user.getName() : "");

        } else
            addActionMessage(this.getText("license.rejected") + license().getApplicationNumber());
    }

    private User getInitiatorUserObj() {
        User user = null;
        for (final StateHistory state : license().getState().getHistory())
            if (state.getValue().contains(Constants.WF_LICENSE_CREATED)) {
                user = state.getCreatedBy();
                break;
            }
        return user;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(final String roleName) {
        this.roleName = roleName;
    }

    // sub class should get the object of the model and set to license()
    // /use contains() every where in this api
    // viewTradeLicense!showForApproval is picking id and gets Objects and
    // forwards here
    @SkipValidation
    public String showForApproval() throws IOException {
        getSession().put("model.id", license().getId());
        String result = APPROVE_PAGE;
        setRoleName(securityUtils.getCurrentUser().getRoles().toString());
        if (license().getLicenseAppType().getName().equals(Constants.NEW_LIC_APPTYPE)) {
            if (license().getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATECERTIFICATE))
                result = GENERATE_CERTIFICATE;
            else result = APPROVE_PAGE;

        } else if (license().getState().getValue().contains(Constants.WORKFLOW_STATE_TYPE_RENEWLICENSE)) {
            if (license().getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATECERTIFICATE))
                result = GENERATE_CERTIFICATE;
            else result = "approveRenew";
        }
        return result;
    }

    public void loadAjaxedDropDowns() {
        final CommonAjaxAction commonAjaxAction = new CommonAjaxAction();
        commonAjaxAction.setLicenseUtils(licenseUtils);
        commonAjaxAction.setBoundaryService(boundaryService);
        commonAjaxAction.setEisCommonService(eisCommonService);
        commonAjaxAction.setDesignationService(designationService);

        if (workflowBean.getDepartmentId() != null) {
            commonAjaxAction.setDepartmentId(workflowBean.getDepartmentId());
            commonAjaxAction.ajaxPopulateDesignationsByDept();
            workflowBean.setDesignationList(commonAjaxAction.getDesignationList());
        }
        if (workflowBean.getDesignationId() != null) {
            commonAjaxAction.setDesignationId(workflowBean.getDesignationId());
            commonAjaxAction.ajaxPopulateUsersByDesignation();
            workflowBean.setAppoverUserList(commonAjaxAction.getAllActiveUsersByGivenDesg());
        }
    }

    public void setupWorkflowDetails() {
        workflowBean.setDepartmentList(licenseUtils.getAllDepartments());
        workflowBean.setDesignationList(Collections.emptyList());
        workflowBean.setAppoverUserList(Collections.emptyList());
    }

    public LicenseDemand getCurrentYearDemand() {
        return license().getLicenseDemand();
    }

    public String getPayableAmountInWords() {
        return NumberToWord.amountInWords(license().getTotalBalance().doubleValue());
    }

    public String getCollectedDemandAmountInWords() {
        return NumberToWord.amountInWords(license().getLicenseDemand().getAmtCollected().doubleValue());
    }

    public boolean isCurrent(final EgDemandDetails dd) {
        boolean isCurrent = false;
        final Installment currInstallment = licenseUtils.getCurrInstallment(dd.getEgDemandReason()
                .getEgDemandReasonMaster().getEgModule());
        if (currInstallment.getId().intValue() == dd.getEgDemandReason().getEgInstallmentMaster().getId().intValue())
            isCurrent = true;
        return isCurrent;

    }

    public Map<String, Map<String, BigDecimal>> getOutstandingFee() {
        return this.licenseService().getOutstandingFee(this.license());
    }

    public boolean isCitizen() {
        return securityUtils.currentUserType().equals(UserType.CITIZEN);
    }

    public String getReportId() {
        return reportId;
    }

    public Long getFeeTypeId() {
        return feeTypeId;
    }

    public void setFeeTypeId(final Long feeTypeId) {
        this.feeTypeId = feeTypeId;
    }

    public boolean isShowAgreementDtl() {
        return showAgreementDtl;
    }

    public void setShowAgreementDtl(final boolean showAgreementDtl) {
        this.showAgreementDtl = showAgreementDtl;
    }

    public String getFileStoreIds() {
        return fileStoreIds;
    }

    public void setFileStoreIds(final String fileStoreIds) {
        this.fileStoreIds = fileStoreIds;
    }

    public String getUlbCode() {
        return ulbCode;
    }

    public void setUlbCode(final String ulbCode) {
        this.ulbCode = ulbCode;
    }

    public String getSignedFileStoreId() {
        return signedFileStoreId;
    }

    public void setSignedFileStoreId(final String signedFileStoreId) {
        this.signedFileStoreId = signedFileStoreId;
    }

    public TradeLicenseSmsAndEmailService getTradeLicenseSmsAndEmailService() {
        return tradeLicenseSmsAndEmailService;
    }

    public void setTradeLicenseSmsAndEmailService(final TradeLicenseSmsAndEmailService tradeLicenseSmsAndEmailService) {
        this.tradeLicenseSmsAndEmailService = tradeLicenseSmsAndEmailService;
    }

}
