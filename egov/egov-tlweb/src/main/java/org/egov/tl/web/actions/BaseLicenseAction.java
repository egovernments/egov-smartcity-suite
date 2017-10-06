/*
 * eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) <2017>  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 * 	Further, all user interfaces, including but not limited to citizen facing interfaces,
 *         Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *         derived works should carry eGovernments Foundation logo on the top right corner.
 *
 * 	For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 * 	For any further queries on attribution, including queries on brand guidelines,
 *         please contact contact@egovernments.org
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.tl.web.actions;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Installment;
import org.egov.demand.model.EgDemandDetails;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.PositionMasterService;
import org.egov.eis.web.actions.workflow.GenericWorkFlowAction;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.persistence.entity.enums.UserType;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.viewer.ReportViewerUtil;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.NumberToWord;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.pims.commons.Position;
import org.egov.tl.entity.License;
import org.egov.tl.entity.LicenseCategory;
import org.egov.tl.entity.LicenseDemand;
import org.egov.tl.entity.LicenseDocument;
import org.egov.tl.entity.LicenseSubCategory;
import org.egov.tl.entity.NatureOfBusiness;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.entity.WorkflowBean;
import org.egov.tl.service.AbstractLicenseService;
import org.egov.tl.service.FeeTypeService;
import org.egov.tl.service.LicenseCategoryService;
import org.egov.tl.service.ProcessOwnerReassignmentService;
import org.egov.tl.service.LicenseSubCategoryService;
import org.egov.tl.service.TradeLicenseService;
import org.egov.tl.service.TradeLicenseSmsAndEmailService;
import org.egov.tl.service.UnitOfMeasurementService;
import org.egov.tl.utils.LicenseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.egov.tl.utils.Constants.*;

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
        @Result(name = "tl_generateCertificate", type = "redirectAction", location = "../viewtradelicense/viewTradeLicense-generateCertificate"),
        @Result(name = "approve", location = "newTradeLicense-new.jsp"),
        @Result(name = "report", location = "newTradeLicense-report.jsp"),
        @Result(name = "digitalSignatureRedirection", location = "newTradeLicense-digitalSignatureRedirection.jsp"),
        @Result(name = MEESEVA_RESULT_ACK, location = "/meeseva/generatereceipt", type = "redirect",
                params = {"prependServletContext", "false", "transactionServiceNumber", "${applicationNo}"})})
public abstract class BaseLicenseAction<T extends License> extends GenericWorkFlowAction {
    private static final long serialVersionUID = 1L;
    private static final String WF_ITEM_PROCESSED = "wf.item.processed";
    private static final String MESSAGE = "message";

    protected transient WorkflowBean workflowBean = new WorkflowBean();
    protected transient List<String> buildingTypeList;
    protected transient String roleName;
    protected transient String reportId;
    protected transient List<HashMap<String, Object>> licenseHistory = new ArrayList<>();
    protected transient boolean showAgreementDtl;
    protected transient String applicationNo;
    protected List<LicenseDocument> licenseDocument = new ArrayList<>();

    @Autowired
    protected transient LicenseUtils licenseUtils;
    @Autowired
    protected transient PositionMasterService positionMasterService;
    @Autowired
    protected transient SecurityUtils securityUtils;
    @Autowired
    protected transient AssignmentService assignmentService;
    @Autowired
    protected transient BoundaryService boundaryService;
    @Autowired
    @Qualifier("licenseCategoryService")
    protected transient LicenseCategoryService licenseCategoryService;
    @Autowired
    @Qualifier("licenseSubCategoryService")
    protected transient LicenseSubCategoryService licenseSubCategoryService;
    @Autowired
    @Qualifier("unitOfMeasurementService")
    protected transient UnitOfMeasurementService unitOfMeasurementService;
    @Autowired
    @Qualifier("fileStoreService")
    protected transient FileStoreService fileStoreService;
    @Autowired
    protected transient ReportViewerUtil reportViewerUtil;
    private transient String fileStoreIds;
    private transient String ulbCode;
    private transient String signedFileStoreId;
    private transient Long feeTypeId;
    private transient boolean hasCscOperatorRole;
    private transient TradeLicenseSmsAndEmailService tradeLicenseSmsAndEmailService;
    @Autowired
    private transient TradeLicenseService tradeLicenseService;
    @Autowired
    @Qualifier("feeTypeService")
    private transient FeeTypeService feeTypeService;

    @Autowired
    private transient ProcessOwnerReassignmentService processOwnerReassignmentService;

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

    @ValidationErrorPage(NEW)
    public String create(final T license) {
        addNewDocuments();
        populateWorkflowBean();
        if (tradeLicenseService.currentUserIsMeeseva()) {
            licenseService().createWithMeseva(license, workflowBean);
            applicationNo = license.getApplicationNumber();
        } else {
            licenseService().create(license, workflowBean);
            addActionMessage(this.getText("license.submission.succesful") + license().getApplicationNumber());
            setHasCscOperatorRole(securityUtils.getCurrentUser().getRoles().toString().contains(CSCOPERATOR));
        }

        return tradeLicenseService.currentUserIsMeeseva() ? MEESEVA_RESULT_ACK : ACKNOWLEDGEMENT;
    }

    // sub class should get the object of the model and set to license()
    public String approve() {

        if (WF_PREVIEW_BUTTON.equals(workFlowAction))
            return redirectToPrintCertificate();
        if (SIGNWORKFLOWACTION.equals(workFlowAction))
            return digitalSignRedirection();
        tradeLicenseService.updateStatusInWorkFlowProgress((TradeLicense) license(), workFlowAction);
        populateWorkflowBean();
        if (!GENERATECERTIFICATE.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {
            WorkFlowMatrix wfmatrix = tradeLicenseService.getWorkFlowMatrixApi(license(), workflowBean);
            if (!license().getCurrentState().getValue().equals(wfmatrix.getCurrentState())) {
                addActionMessage(this.getText(WF_ITEM_PROCESSED));
                return MESSAGE;
            }
        }
        if (GENERATECERTIFICATE.equalsIgnoreCase(workflowBean.getWorkFlowAction()) && "END".equalsIgnoreCase(license().getCurrentState().getValue())) {
            addActionMessage(this.getText(WF_ITEM_PROCESSED));
            return MESSAGE;
        }
        processWorkflow(NEW);
        addNewDocuments();
        tradeLicenseService.updateTradeLicense((TradeLicense) license(), workflowBean);
        if (GENERATECERTIFICATE.equalsIgnoreCase(workflowBean.getWorkFlowAction()))
            return GENERATE_CERTIFICATE;
        else
            return MESSAGE;

    }

    private String redirectToPrintCertificate() {
        reportId = reportViewerUtil.addReportToTempCache(tradeLicenseService.generateLicenseCertificate(license(),false));
        return "report";
    }

    private String digitalSignRedirection() {
        ReportOutput reportOutput = tradeLicenseService.generateLicenseCertificate(license(),false);
        if (reportOutput != null) {
            String fileName = SIGNED_DOCUMENT_PREFIX + license().getApplicationNumber() + ".pdf";
            final InputStream fileStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
            final FileStoreMapper fileStore = fileStoreService.store(fileStream, fileName, "application/pdf", FILESTORE_MODULECODE);
            license().setDigiSignedCertFileStoreId(fileStore.getFileStoreId());
            licenseService().save(license());
            fileStoreIds = fileStore.getFileStoreId();
            ulbCode = ApplicationThreadLocals.getCityCode();
            applicationNo = license().getApplicationNumber();
        }
        return "digitalSignatureRedirection";
    }

    @SkipValidation
    public String approveRenew() {
        populateWorkflowBean();
        if (!GENERATECERTIFICATE.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {
            WorkFlowMatrix wfmatrix = tradeLicenseService.getWorkFlowMatrixApi(license(), workflowBean);
            if (!license().getCurrentState().getValue().equals(wfmatrix.getCurrentState())) {
                addActionMessage(this.getText(WF_ITEM_PROCESSED));
                return MESSAGE;
            }
        }
        if (GENERATECERTIFICATE.equalsIgnoreCase(workflowBean.getWorkFlowAction()) && "END".equalsIgnoreCase(license().getCurrentState().getValue())) {
            addActionMessage(this.getText(WF_ITEM_PROCESSED));
            return MESSAGE;
        }
        processWorkflow(RENEWAL_LIC_APPTYPE);
        return MESSAGE;
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
        return BEFORE_RENEWAL;
    }

    @SkipValidation
    public String renew() {
        addNewDocuments();
        populateWorkflowBean();
        if (tradeLicenseService.currentUserIsMeeseva()) {
            licenseService().renewWithMeeseva(license(), workflowBean);
            applicationNo = license().getApplicationNumber();
        } else {
            licenseService().renew(license(), workflowBean);
            addActionMessage(this.getText("license.renew.submission.succesful") + " " + license().getApplicationNumber());
            setHasCscOperatorRole(securityUtils.getCurrentUser().getRoles().toString().contains(CSCOPERATOR));
        }
        return tradeLicenseService.currentUserIsMeeseva() ? MEESEVA_RESULT_ACK : ACKNOWLEDGEMENT;
    }

    @SkipValidation
    public String enterExistingForm() {
        return NEW;
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
        return NEW;
    }

    public void prepareCreate() {
        prepareNewForm();
    }

    public void prepareNewForm() {
        prepare();
        buildingTypeList = new ArrayList<>();
        buildingTypeList.add(BUILDINGTYPE_OWN_BUILDING);
        buildingTypeList.add(BUILDINGTYPE_RENTAL_AGREEMANT);
        addDropdownData(DROPDOWN_AREA_LIST_LICENSE, Collections.emptyList());
        addDropdownData(DROPDOWN_AREA_LIST_LICENSEE, Collections.emptyList());
        addDropdownData(DROPDOWN_DIVISION_LIST_LICENSE, Collections.emptyList());
        addDropdownData(DROPDOWN_DIVISION_LIST_LICENSEE, Collections.emptyList());
        addDropdownData(DROPDOWN_TRADENAME_LIST,
                licenseUtils.getAllTradeNames(getModel().getClass().getSimpleName()));

        setupWorkflowDetails();
        feeTypeId = feeTypeService.findByName(LICENSE_FEE_TYPE).getId();
    }

    public void prepareShowForApproval() {
        prepareNewForm();
    }

    /**
     * should be called from the second level only Approve will not end workflow
     * instead it sends to the creator in approved state
     */
    public void processWorkflow(final String processType) {

        // Both New And Renew Workflow handling in same API(transitionWorkFlow)
        if (NEW.equalsIgnoreCase(processType) && !BUTTONSUBMIT.equals(workFlowAction))
            licenseService().transitionWorkFlow(license(), workflowBean);
        if (BUTTONAPPROVE.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {
            addActionMessage(this.getText("license.approved.success"));
        } else if (BUTTONFORWARD.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {
            List<Assignment> assignments = assignmentService
                    .getAssignmentsForPosition(workflowBean.getApproverPositionId());
            String nextDesgn = !assignments.isEmpty() ? assignments.get(0).getDesignation().getName() : "";
            final String userName = !assignments.isEmpty() ? assignments.get(0).getEmployee().getName() : "";
            addActionMessage(this.getText("license.sent") + " " + nextDesgn + " - " + userName);
        } else if (BUTTONREJECT.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {
            rejectActionMessage();
        } else if (BUTTONGENERATEDCERTIFICATE.equalsIgnoreCase(workflowBean.getWorkFlowAction()))
            addActionMessage(this.getText("license.certifiacte.print.complete.recorded"));

    }

    private void rejectActionMessage() {
        User user = getInitiatorUserObj();
        if (user != null && (!UserType.EMPLOYEE.equals(user.getType()))) {
            List<Assignment> assignments = assignmentService.getAssignmentsForPosition(license().getState().getInitiatorPosition().getId());
            user = assignments.get(0).getEmployee();
        }
        if (license().getState().getValue().contains(WORKFLOW_STATE_REJECTED)) {
            Position creatorPosition = license().getState().getInitiatorPosition();
            addActionMessage(this.getText("license.rejectedfirst") + " " + (creatorPosition.getDeptDesig().getDesignation().getName() + " - ")
                    + (user != null ? user.getName() : ""));

        } else
            addActionMessage(this.getText("license.rejected") + license().getApplicationNumber());
    }

    private User getInitiatorUserObj() {
        User user = null;
        for (final StateHistory state : license().getState().getHistory())
            if (state.getValue().contains(WF_LICENSE_CREATED)) {
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
        if (license().isNewApplication()) {
            if (license().getState().getValue().contains(WORKFLOW_STATE_GENERATECERTIFICATE))
                result = GENERATE_CERTIFICATE;
            else
                result = APPROVE_PAGE;

        }
        return result;
    }

    public void setupWorkflowDetails() {
        workflowBean.setDepartmentList(licenseUtils.getAllDepartments());
        workflowBean.setDesignationList(Collections.emptyList());
        workflowBean.setAppoverUserList(Collections.emptyList());
    }

    public Boolean hasJuniorOrSeniorAssistantRole() {
        List<Position> userPositions = positionMasterService.getPositionsForEmployee(ApplicationThreadLocals.getUserId());
        return userPositions
                .stream()
                .anyMatch(position -> (position.getDeptDesig().getDesignation().getName().equalsIgnoreCase(JA_DESIGNATION)
                        || position.getDeptDesig().getDesignation().getName().equalsIgnoreCase(SA_DESIGNATION)));
    }

    public Boolean reassignEnabled() {
        return processOwnerReassignmentService.reassignmentEnabled();
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
        final Installment currInstallment = licenseUtils
                .getCurrInstallment(dd.getEgDemandReason().getEgDemandReasonMaster().getEgModule());
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

    public String getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(final String applicationNo) {
        this.applicationNo = applicationNo;
    }

    public boolean isHasCscOperatorRole() {
        return hasCscOperatorRole;
    }

    public void setHasCscOperatorRole(boolean hasCscOperatorRole) {
        this.hasCscOperatorRole = hasCscOperatorRole;
    }

    public List<HashMap<String, Object>> getLicenseHistory() {
        return licenseHistory;
    }

    public void setLicenseHistory(List<HashMap<String, Object>> licenseHistory) {
        this.licenseHistory = licenseHistory;
    }

    public List<LicenseDocument> getLicenseDocument() {
        return licenseDocument;
    }

    public void setLicenseDocument(List<LicenseDocument> licenseDocument) {
        this.licenseDocument = licenseDocument;
    }

    public void addNewDocuments() {
        licenseDocument.removeIf(licenseDocument -> licenseDocument.getUploadsFileName().isEmpty());
        licenseDocument.forEach(tempDocument -> {
            tempDocument.setType(tradeLicenseService.getLicenseDocumentType(tempDocument.getType().getId()));
        });
        license().getDocuments().addAll(licenseDocument);
    }

}
