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

package org.egov.tl.web.actions;

import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.egov.tl.utils.Constants.ACKNOWLEDGEMENT;
import static org.egov.tl.utils.Constants.APPROVE_PAGE;
import static org.egov.tl.utils.Constants.BEFORE_RENEWAL;
import static org.egov.tl.utils.Constants.BUTTONAPPROVE;
import static org.egov.tl.utils.Constants.BUTTONCANCEL;
import static org.egov.tl.utils.Constants.BUTTONFORWARD;
import static org.egov.tl.utils.Constants.BUTTONGENERATEDCERTIFICATE;
import static org.egov.tl.utils.Constants.BUTTONREJECT;
import static org.egov.tl.utils.Constants.CSCOPERATOR;
import static org.egov.tl.utils.Constants.DROPDOWN_AREA_LIST_LICENSE;
import static org.egov.tl.utils.Constants.DROPDOWN_AREA_LIST_LICENSEE;
import static org.egov.tl.utils.Constants.DROPDOWN_DIVISION_LIST_LICENSE;
import static org.egov.tl.utils.Constants.DROPDOWN_DIVISION_LIST_LICENSEE;
import static org.egov.tl.utils.Constants.GENERATECERTIFICATE;
import static org.egov.tl.utils.Constants.GENERATE_CERTIFICATE;
import static org.egov.tl.utils.Constants.GENERATE_PROVISIONAL_CERTIFICATE;
import static org.egov.tl.utils.Constants.LICENSE_FEE_TYPE;
import static org.egov.tl.utils.Constants.MEESEVA_RESULT_ACK;
import static org.egov.tl.utils.Constants.NEW_APPTYPE_CODE;
import static org.egov.tl.utils.Constants.RENEW_APPTYPE_CODE;
import static org.egov.tl.utils.Constants.REPORT_PAGE;
import static org.egov.tl.utils.Constants.SIGNWORKFLOWACTION;
import static org.egov.tl.utils.Constants.WF_LICENSE_CREATED;
import static org.egov.tl.utils.Constants.WF_PREVIEW_BUTTON;
import static org.egov.tl.utils.Constants.WORKFLOW_STATE_GENERATECERTIFICATE;
import static org.egov.tl.utils.Constants.WORKFLOW_STATE_REJECTED;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.PositionMasterService;
import org.egov.eis.web.actions.workflow.GenericWorkFlowAction;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.integration.service.ThirdPartyService;
import org.egov.infra.persistence.entity.enums.UserType;
import org.egov.infra.reporting.viewer.ReportViewerUtil;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.NumberUtil;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.tl.entity.LicenseCategory;
import org.egov.tl.entity.LicenseDocument;
import org.egov.tl.entity.LicenseDocumentType;
import org.egov.tl.entity.LicenseSubCategory;
import org.egov.tl.entity.NatureOfBusiness;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.entity.WorkflowBean;
import org.egov.tl.service.FeeTypeService;
import org.egov.tl.service.LicenseApplicationService;
import org.egov.tl.service.LicenseConfigurationService;
import org.egov.tl.service.LicenseDocumentTypeService;
import org.egov.tl.service.ProcessOwnerReassignmentService;
import org.egov.tl.service.TradeLicenseService;
import org.egov.tl.utils.LicenseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@ParentPackage("egov")
@Results({
        @Result(name = "collection", type = "redirectAction", location = "licenseBillCollect", params = { "namespace",
                "/integration", "method", "renew" }),
        @Result(name = "tl_approve", type = "redirectAction", location = "viewTradeLicense", params = { "namespace",
                "/viewtradelicense", "method", "showForApproval" }),
        @Result(name = "tl_generateCertificate", type = "redirectAction", location = "../viewtradelicense/viewTradeLicense-generateCertificate"),
        @Result(name = APPROVE_PAGE, location = "newTradeLicense-new.jsp"),
        @Result(name = REPORT_PAGE, location = "newTradeLicense-report.jsp"),
        @Result(name = "digitalSignatureRedirection", location = "newTradeLicense-digitalSignatureRedirection.jsp"),
        @Result(name = MEESEVA_RESULT_ACK, location = "/meeseva/generatereceipt", type = "redirect", params = {
                "prependServletContext", "false", "transactionServiceNumber", "${applicationNo}" }) })
public abstract class BaseLicenseAction extends GenericWorkFlowAction {

    protected static final String WF_INPROGRESS_ERROR_CODE = "WF.INPROGRESS";
    protected static final String WF_INPROGRESS_ERROR_MSG_FORMAT = "Cannot continue, A %s application is already in progress.";
    private static final String WF_ITEM_PROCESSED = "wf.item.processed";
    private static final String MESSAGE = "message";
    private static final String VALIDATE_SUPPORT_DOCUMENT = "error.support.docs";
    private static final String LICENSE_REJECT = "license.rejected";

    protected transient TradeLicense tradeLicense = new TradeLicense();
    protected transient String roleName;
    protected transient String reportId;
    protected transient String fileStoreIds;
    protected transient String ulbCode;
    protected transient String signedFileStoreId;
    protected transient Long feeTypeId;
    protected transient boolean showAgreementDtl;
    protected transient String applicationNo;
    protected transient List<LicenseDocument> licenseDocument = new ArrayList<>();
    protected transient List<HashMap<String, Object>> licenseHistory = new ArrayList<>();
    protected transient WorkflowBean workflowBean = new WorkflowBean();

    protected transient String source;
    protected transient String transactionId;
    protected transient boolean wsPortalRequest;

    @Autowired
    protected transient LicenseUtils licenseUtils;
    @Autowired
    protected transient PositionMasterService positionMasterService;
    @Autowired
    protected transient SecurityUtils securityUtils;
    @Autowired
    protected transient AssignmentService assignmentService;
    @Autowired
    protected transient ReportViewerUtil reportViewerUtil;
    @Autowired
    @Qualifier("tradeLicenseService")
    protected transient TradeLicenseService tradeLicenseService;
    @Autowired
    @Qualifier("feeTypeService")
    protected transient FeeTypeService feeTypeService;
    @Autowired
    protected transient ProcessOwnerReassignmentService processOwnerReassignmentService;
    @Autowired
    @Qualifier("licenseApplicationService")
    protected transient LicenseApplicationService licenseApplicationService;
    @Autowired
    protected transient LicenseConfigurationService licenseConfigurationService;
    @Autowired
    protected transient LicenseDocumentTypeService licenseDocumentTypeService;

    @Autowired
    protected transient ThirdPartyService thirdPartyService;

    protected String message;

    public BaseLicenseAction() {
        this.addRelatedEntity("boundary", Boundary.class);
        this.addRelatedEntity("parentBoundary", Boundary.class);
        this.addRelatedEntity("adminWard", Boundary.class);
        this.addRelatedEntity("licensee.boundary", Boundary.class);
        this.addRelatedEntity("natureOfBusiness", NatureOfBusiness.class);
        this.addRelatedEntity("category", LicenseCategory.class);
        this.addRelatedEntity("tradeName", LicenseSubCategory.class);
    }

    protected TradeLicense license() {
        return tradeLicense;
    }

    @ValidationErrorPage(NEW)
    public String create(TradeLicense license) {
        addNewDocuments();
        populateWorkflowBean();

        if (tradeLicenseService.currentUserIsMeeseva()) {
            license.setApplicationNumber(getApplicationNo());
            licenseApplicationService.createWithMeseva(license, workflowBean);
        } else if (thirdPartyService.isWardSecretaryRequest(wsPortalRequest)) {
            if (ThirdPartyService.validateWardSecretaryRequest(transactionId, source)) {
                addActionMessage(getText("WS.001"));
                return ERROR;
            }
            license.setApplicationSource(source);
            workflowBean.setActionName(NEW_APPTYPE_CODE);
            licenseApplicationService.processWithWardSecretary(license, workflowBean,
                    transactionId);
        } else {
            licenseApplicationService.create(license, workflowBean);
            setMessage(this.getText("license.submission.succesful") + license().getApplicationNumber());
        }
        return tradeLicenseService.currentUserIsMeeseva() ? MEESEVA_RESULT_ACK : ACKNOWLEDGEMENT;
    }

    public String approve() {
        populateWorkflowBean();
        if (WF_PREVIEW_BUTTON.equals(workFlowAction))
            return redirectToPrintCertificate();
        if (SIGNWORKFLOWACTION.equals(workFlowAction))
            return digitalSignRedirection();
        if (license().isNewWorkflow()) {
            WorkFlowMatrix wfmatrix = licenseApplicationService.getWorkflowAPI(license(), workflowBean);
            if (!license().getCurrentState().getValue().equals(wfmatrix.getCurrentState())) {
                setMessage(this.getText(WF_ITEM_PROCESSED));
                return MESSAGE;
            }
            if (GENERATE_PROVISIONAL_CERTIFICATE.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {
                reportId = reportViewerUtil
                        .addReportToTempCache(tradeLicenseService.generateLicenseCertificate(license(), true));
                return REPORT_PAGE;
            }
            addNewDocuments();
            licenseApplicationService.updateLicense(license(), workflowBean);
            successMessage();
            if (GENERATECERTIFICATE.equalsIgnoreCase(workflowBean.getWorkFlowAction()))
                return GENERATE_CERTIFICATE;
            else
                return MESSAGE;
        } else {
            tradeLicenseService.updateStatusInWorkFlowProgress(license(), workFlowAction);
            if (!GENERATECERTIFICATE.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {
                WorkFlowMatrix wfmatrix = tradeLicenseService.getWorkFlowMatrixApi(license(), workflowBean);
                if (!license().getCurrentState().getValue().equals(wfmatrix.getCurrentState())) {
                    setMessage(this.getText(WF_ITEM_PROCESSED));
                    return MESSAGE;
                }
            }
            if (GENERATECERTIFICATE.equalsIgnoreCase(workflowBean.getWorkFlowAction())
                    && license().getCurrentState().isEnded()) {
                setMessage(this.getText(WF_ITEM_PROCESSED));
                return MESSAGE;
            }
            processWorkflow();
            addNewDocuments();
            tradeLicenseService.updateTradeLicense(license(), workflowBean);
            return GENERATECERTIFICATE
                    .equalsIgnoreCase(workflowBean.getWorkFlowAction()) ? GENERATE_CERTIFICATE : MESSAGE;
        }
    }

    private String redirectToPrintCertificate() {
        reportId = reportViewerUtil.addReportToTempCache(tradeLicenseService.generateLicenseCertificate(license(),
                false));
        return REPORT_PAGE;
    }

    private String digitalSignRedirection() {
        tradeLicenseService.generateAndStoreCertificate(license());
        license().setDigiSignedCertFileStoreId(license().getCertificateFileId());
        tradeLicenseService.save(license());
        fileStoreIds = license().getCertificateFileId();
        ulbCode = ApplicationThreadLocals.getCityCode();
        applicationNo = license().getApplicationNumber();
        return "digitalSignatureRedirection";
    }

    protected void populateWorkflowBean() {
        workflowBean.setApproverPositionId(approverPositionId);
        workflowBean.setApproverComments(approverComments);
        workflowBean.setWorkFlowAction(workFlowAction);
        workflowBean.setCurrentState(currentState);
        workflowBean.setAdditionaRule(additionalRule);
        workflowBean.setCurrentDesignation(currentDesignation);
    }

    @SkipValidation
    public String beforeRenew() {
        return BEFORE_RENEWAL;
    }

    @SkipValidation
    public String renew() {
        addNewDocuments();
        populateWorkflowBean();
        if (tradeLicenseService.currentUserIsMeeseva()) {
            license().setApplicationNumber(getApplicationNo());
            licenseApplicationService.renewWithMeeseva(license(), workflowBean);
        } else if (thirdPartyService.isWardSecretaryRequest(wsPortalRequest)) {
            if (ThirdPartyService.validateWardSecretaryRequest(transactionId, source)) {
                throw new ApplicationRuntimeException("WS.001");
            }
            license().setApplicationSource(source);
            workflowBean.setActionName(RENEW_APPTYPE_CODE);
            licenseApplicationService.processWithWardSecretary(license(), workflowBean,
                    transactionId);
        } else {
            licenseApplicationService.renew(license(), workflowBean);
            setMessage(this.getText("license.renew.submission.succesful")
                    + " " + license().getApplicationNumber());
        }
        return tradeLicenseService.currentUserIsMeeseva() ? MEESEVA_RESULT_ACK : ACKNOWLEDGEMENT;
    }

    @Override
    public TradeLicense getModel() {
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
        addDropdownData(DROPDOWN_AREA_LIST_LICENSE, Collections.emptyList());
        addDropdownData(DROPDOWN_AREA_LIST_LICENSEE, Collections.emptyList());
        addDropdownData(DROPDOWN_DIVISION_LIST_LICENSE, Collections.emptyList());
        addDropdownData(DROPDOWN_DIVISION_LIST_LICENSEE, Collections.emptyList());
        setupWorkflowDetails();
        feeTypeId = feeTypeService.findByName(LICENSE_FEE_TYPE).getId();
    }

    /**
     * should be called from the second level only Approve will not end workflow instead it sends to the creator in approved state
     */
    public void processWorkflow() {
        // Both New And Renew Workflow handling in same API(transitionWorkFlow)
        tradeLicenseService.transitionWorkFlow(license(), workflowBean);
        successMessage();

    }

    private void successMessage() {
        if (BUTTONAPPROVE.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {
            setMessage(this.getText("license.approved.success"));
        } else if (BUTTONFORWARD.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {
            List<Assignment> assignments = assignmentService.getAssignmentsForPosition(workflowBean.getApproverPositionId());
            String nextDesgn = assignments.isEmpty() ? EMPTY : assignments.get(0).getDesignation().getName();
            String userName = assignments.isEmpty() ? EMPTY : assignments.get(0).getEmployee().getName();
            setMessage(this.getText("license.sent") + " " + nextDesgn + " - " + userName);
        } else if (BUTTONREJECT.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {
            rejectActionMessage();
        } else if (BUTTONCANCEL.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {
            setMessage(this.getText(LICENSE_REJECT) + license().getApplicationNumber());
        } else if (BUTTONGENERATEDCERTIFICATE.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {
            setMessage(this.getText("license.certifiacte.print.complete.recorded"));
        }
    }

    private void rejectActionMessage() {
        if (license().isNewWorkflow()) {
            if (BUTTONREJECT.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {
                Position currentOwner = license().currentAssignee();
                Designation designation = currentOwner.getDeptDesig().getDesignation();
                List<Assignment> assignments = assignmentService.getAssignmentsForPosition(currentOwner.getId());
                String userName = assignments.isEmpty() ? EMPTY : assignments.get(0).getEmployee().getName();
                setMessage(new StringBuilder(25).append(this.getText("license.rejectedfirst"))
                        .append(SPACE).append(designation.getName() + " - ").append(userName).toString());
            } else {
                setMessage(this.getText(LICENSE_REJECT) + license().getApplicationNumber());
            }

        } else {
            User user = getInitiatorUserObj();
            if (user != null && !UserType.EMPLOYEE.equals(user.getType())) {
                List<Assignment> assignments = assignmentService.getAssignmentsForPosition(license().getState()
                        .getInitiatorPosition().getId());
                user = assignments.get(0).getEmployee();
            }
            if (license().getState().getValue().contains(WORKFLOW_STATE_REJECTED)) {
                Position creatorPosition = license().getState().getInitiatorPosition();
                setMessage(new StringBuilder(25).append(this.getText("license.rejectedfirst"))
                        .append(SPACE).append(creatorPosition.getDeptDesig().getDesignation().getName() + " - ")
                        .append(user == null ? EMPTY : user.getName()).toString());

            } else {
                setMessage(this.getText(LICENSE_REJECT) + license().getApplicationNumber());
            }
        }
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
    public String showForApproval() {
        getModel().setId(license().getId());
        String result = APPROVE_PAGE;
        setRoleName(securityUtils.getCurrentUser().getRoles().toString());
        if (license().isNewApplication()) {
            result = license()
                    .getState().getValue()
                    .contains(WORKFLOW_STATE_GENERATECERTIFICATE) ? GENERATE_CERTIFICATE : APPROVE_PAGE;
        }
        return result;
    }

    public void setupWorkflowDetails() {
        workflowBean.setDepartmentList(licenseUtils.getAllDepartments());
        workflowBean.setDesignationList(Collections.emptyList());
        workflowBean.setAppoverUserList(Collections.emptyList());
    }

    public String getPayableAmountInWords() {
        return NumberUtil.amountInWords(license().getTotalBalance());
    }

    public Map<String, Map<String, BigDecimal>> getOutstandingFee() {
        return tradeLicenseService.getOutstandingFee(license());
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

    public String getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(final String applicationNo) {
        this.applicationNo = applicationNo;
    }

    public boolean hasCscOperatorRole() {
        return securityUtils.getCurrentUser().hasRole(CSCOPERATOR);
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
        licenseDocument.removeIf(document -> document.getUploadsFileName().isEmpty());
        licenseDocument
                .forEach(document -> document.setType(tradeLicenseService.getLicenseDocumentType(document.getType().getId())));
        license().getDocuments().addAll(licenseDocument);
    }

    @Override
    public List<String> getValidActions() {
        List<String> validActions = new ArrayList<>();
        if (null == getModel() || null == getModel().getId() || getModel().getCurrentState() == null
                || getModel().getCurrentState().getValue().endsWith("NEW")
                || (getModel() != null && getModel().getCurrentState() != null ? getModel().getCurrentState().isEnded() : false))
            validActions = Arrays.asList("Save");
        else if (getModel().hasState())
            if (getModel().isNewWorkflow())
                validActions.addAll(this.customizedWorkFlowService.getNextValidActions(getModel()
                        .getStateType(), getWorkFlowDepartment(), getAmountRule(),
                        getAdditionalRule(), getModel().getCurrentState().getValue(),
                        getPendingActions(), getModel().getCreatedDate(),
                        "%" + license().getCurrentState().getOwnerPosition()
                                .getDeptDesig().getDesignation().getName() + "%"));
            else
                validActions.addAll(super.getValidActions());
        validActions.removeIf(validAction -> "Reassign".equals(validAction)
                && getModel().getState().getCreatedBy().getId().equals(ApplicationThreadLocals.getUserId()));
        return validActions;
    }

    @Override
    public String getCurrentDesignation() {
        return license().hasState() ? "%" + license().getCurrentState().getOwnerPosition()
                .getDeptDesig().getDesignation().getName() + "%" : EMPTY;
    }

    public Boolean forwardEnabled() {
        if (getModel() == null || getModel().getCurrentState() == null)
            return false;
        else
            return this.customizedWorkFlowService.getWfMatrix(getModel()
                    .getStateType(), getWorkFlowDepartment(), getAmountRule(),
                    getAdditionalRule(), getModel().getCurrentState().getValue(),
                    getPendingActions(), new Date(), getCurrentDesignation()).isForwardEnabled();

    }

    public String getEnabledFields() {
        if (getModel().hasState()) {
            WorkFlowMatrix workFlowMatrix = this.customizedWorkFlowService.getWfMatrix(getModel()
                    .getStateType(), getWorkFlowDepartment(), getAmountRule(),
                    getAdditionalRule(), getModel().getCurrentState().getValue(),
                    this.getPendingActions(), getModel().getCreatedDate(),
                    "%" + license().getCurrentState().getOwnerPosition()
                            .getDeptDesig().getDesignation().getName() + "%");
            return workFlowMatrix.getEnableFields();
        } else
            return "all";
    }

    public boolean isDigitalSignatureEnabled() {
        return licenseConfigurationService.digitalSignEnabled();
    }

    public void supportDocumentsValidation() {
        List<LicenseDocument> supportDocs = licenseDocument
                .stream()
                .filter(document -> document.getType().isMandatory() && document.getUploads().isEmpty())
                .collect(Collectors.toList());

        if (!supportDocs.isEmpty())
            throw new ValidationException(VALIDATE_SUPPORT_DOCUMENT, VALIDATE_SUPPORT_DOCUMENT);
    }

    public void supportDocumentsValidationForApproval(TradeLicense license) {

        List<LicenseDocument> supportDocs = licenseDocument
                .stream()
                .filter(document -> document.getType().isMandatory() && document.getUploads().isEmpty())
                .collect(Collectors.toList());

        List<LicenseDocument> existingDocs = new ArrayList<>();
        if (license.getDocuments().stream().anyMatch(document -> !document.getFiles().isEmpty())) {
            existingDocs = license.getDocuments()
                    .stream()
                    .filter(document -> document.getType().getApplicationType().equals(
                            license.getLicenseAppType()))
                    .collect(Collectors.toList());
        }

        List<Long> supportDocType = supportDocs.stream().map(LicenseDocument::getType).map(LicenseDocumentType::getId)
                .collect(Collectors.toList());

        List<Long> existingDocsType = existingDocs.stream().map(LicenseDocument::getType).map(LicenseDocumentType::getId)
                .collect(Collectors.toList());

        if (!supportDocs.isEmpty() && supportDocs.stream().anyMatch(document -> document.getUploads().isEmpty()) &&
                (existingDocs.isEmpty()
                        || !supportDocType.stream().filter(
                                licenseDocumentType -> !existingDocsType.contains(licenseDocumentType))
                                .collect(Collectors.toList()).isEmpty())) {
            throw new ValidationException(VALIDATE_SUPPORT_DOCUMENT, VALIDATE_SUPPORT_DOCUMENT);
        }
    }

    @Override
    public String getPendingActions() {
        return getModel() != null && getModel().getCurrentState() != null ? getModel().getState().getNextAction() : null;
    }

    public boolean currentUserIsCitizenOrAnonymous() {
        return securityUtils.currentUserIsCitizen() || SecurityUtils.currentUserIsAnonymous();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public boolean isWsPortalRequest() {
        return wsPortalRequest;
    }

    public void setWsPortalRequest(boolean wsPortalRequest) {
        this.wsPortalRequest = wsPortalRequest;
    }

}