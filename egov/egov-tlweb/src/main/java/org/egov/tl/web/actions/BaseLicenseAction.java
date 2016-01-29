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

import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.EgwStatus;
import org.egov.commons.Installment;
import org.egov.demand.model.EgDemand;
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
import org.egov.infra.persistence.entity.enums.UserType;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.reporting.viewer.ReportViewerUtil;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infstr.utils.NumberToWord;
import org.egov.pims.commons.Position;
import org.egov.tl.entity.License;
import org.egov.tl.entity.LicenseCategory;
import org.egov.tl.entity.LicenseDemand;
import org.egov.tl.entity.LicenseSubCategory;
import org.egov.tl.entity.NatureOfBusiness;
import org.egov.tl.entity.WorkflowBean;
import org.egov.tl.service.AbstractLicenseService;
import org.egov.tl.service.FeeTypeService;
import org.egov.tl.service.TradeLicenseSmsAndEmailService;
import org.egov.tl.service.TradeLicenseUpdateIndexService;
import org.egov.tl.service.masters.LicenseCategoryService;
import org.egov.tl.service.masters.LicenseSubCategoryService;
import org.egov.tl.service.masters.UnitOfMeasurementService;
import org.egov.tl.utils.Constants;
import org.egov.tl.utils.LicenseChecklistHelper;
import org.egov.tl.utils.LicenseUtils;
import org.egov.tl.web.actions.domain.CommonAjaxAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @author mani for Implementing New License action
 */
@ParentPackage("egov")
@Results({
        @Result(name = "collection", type = "redirectAction", location = "licenseBillCollect",
                params = {"namespace", "/integration", "method", "renew"}),
        @Result(name = "tl_editlicense", type = "redirectAction", location = "editTradeLicense-beforeEdit",
                params = {"namespace", "/newtradelicense"}),
        @Result(name = "tl_approve", type = "redirectAction", location = "viewTradeLicense",
                params = {"namespace", "/viewtradelicense", "method", "showForApproval"}),
        @Result(name = "tl_generateRejCertificate", type = "redirectAction", location = "viewTradeLicense",
                params = {"namespace", "/viewtradelicense", "method", "generateRejCertificate"}),
        @Result(name = "tl_generateCertificate", type = "redirectAction", location = "viewTradeLicense",
                params = {"namespace", "/viewtradelicense", "method", "generateCertificate"}),
        @Result(name = "tl_generateNoc", type = "redirectAction", location = "viewTradeLicense",
                params = {"namespace", "/viewtradelicense", "method", "generateNoc"}),
        @Result(name = "transfertl_editlicense", type = "redirectAction", location = "transferTradeLicense",
                params = {"namespace", "/transfer", "method", "beforeEdit"}),
        @Result(name = "transfertl_approve", type = "redirectAction", location = "transferTradeLicense",
                params = {"namespace", "/transfer", "method", "showForApproval"}),
        @Result(name = "approve", location = "newTradeLicense-new.jsp"),
        @Result(name = "report", location = "newTradeLicense-report.jsp")})
public abstract class BaseLicenseAction<T extends License> extends GenericWorkFlowAction {
    public static final String LICENSECERTIFICATE = "licenseCertificate";
    private static final long serialVersionUID = 1L;

    protected final Map<String, Object> reportParams = new HashMap<String, Object>();
    protected WorkflowBean workflowBean = new WorkflowBean();
    protected List<String> buildingTypeList;
    protected List<String> genderList;
    protected List<String> selectedCheckList;
    protected List<LicenseChecklistHelper> checkList;
    protected String roleName;
    @Autowired
    protected TradeLicenseSmsAndEmailService tradeLicenseSmsAndEmailService;
    protected Integer reportId = -1;
    private Long feeTypeId;
    private Long parentBndryId;

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
    private ReportService reportService;
    @Autowired
    @Qualifier("feeTypeService")
    private FeeTypeService feeTypeService;
    
    @Autowired
    private TradeLicenseUpdateIndexService updateIndexService;

    public BaseLicenseAction() {
        this.addRelatedEntity("boundary", Boundary.class);
        this.addRelatedEntity("licensee.boundary", Boundary.class);
        this.addRelatedEntity("buildingType", NatureOfBusiness.class);
        this.addRelatedEntity("category", LicenseCategory.class);
        this.addRelatedEntity("tradeName", LicenseSubCategory.class);
    }

    protected abstract T license();

    protected abstract AbstractLicenseService<T> licenseService();

    @ValidationErrorPage(Constants.NEW)
    public String create(T license) {
        try {
            this.setCheckList();
            populateWorkflowBean();
            EgwStatus statusChange = (EgwStatus) persistenceService
                    .find("from org.egov.commons.EgwStatus where moduletype=? and code=?",Constants.TRADELICENSEMODULE,Constants.APPLICATION_STATUS_CREATED_CODE);
            license().setEgwStatus(statusChange);
            license().setParentBoundary(boundaryService.getBoundaryById(parentBndryId));
            licenseService().create(license, workflowBean);
            this.updateIndexService.updateTradeLicenseIndexes(license);
        } catch (RuntimeException e) {
            loadAjaxedDropDowns();
            throw e;
        }
        addActionMessage(this.getText("license.submission.succesful") + license().getApplicationNumber());
        return Constants.ACKNOWLEDGEMENT;
    }

    @ValidationErrorPage(Constants.NEW)
    public String enterExisting(T license, Map<Integer, BigDecimal> legacyInstallmentwiseFees) {
        this.setCheckList();
        license().setParentBoundary(boundaryService.getBoundaryById(parentBndryId)); 
        licenseService().enterExistingLicense(license, legacyInstallmentwiseFees);
        addActionMessage(this.getText("license.entry.succesful") + "  " + license().getLicenseNumber());

        return "viewlicense";
    }

    // sub class should get the object of the model and set to license()
    public String approve() {
        processWorkflow(NEW);
        if(license().getEgwStatus()!=null && license().getEgwStatus().getCode().equals(Constants.APPLICATION_STATUS_COLLECTION_CODE))
        {
            EgwStatus statusChange = (EgwStatus) persistenceService
                    .find("from org.egov.commons.EgwStatus where moduletype=? and code=?",Constants.TRADELICENSEMODULE,Constants.APPLICATION_STATUS_GENECERT_CODE);
            license().setEgwStatus(statusChange);
        }
        licenseService().licensePersitenceService().persist(license());
        this.tradeLicenseSmsAndEmailService.sendSmsAndEmail(license(), workflowBean.getWorkFlowAction());
        this.updateIndexService.updateTradeLicenseIndexes(license());
        // Generate PFA Certificate on final approval
        if (Constants.GENERATECERTIFICATE.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {
            reportId = ReportViewerUtil.addReportToSession(reportService.createReport(prepareReportInputData(license())), getSession());
            return "report";
            //return Constants.PFACERTIFICATE;
        } else
            return "message";

    }

    private ReportRequest prepareReportInputData(License license) {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Format formatterYear = new SimpleDateFormat("YYYY");
        reportParams.put("applicationnumber", license.getApplicationNumber());
        reportParams.put("applicantName", license.getLicensee().getApplicantName());
        reportParams.put("licencenumber", license.getLicenseNumber());
        reportParams.put("wardName", license.getBoundary().getName());
        reportParams.put("cscNumber", "");
        reportParams.put("nameOfEstablishment", license.getNameOfEstablishment());
        reportParams.put("licenceAddress", license.getAddress());
        reportParams.put("subCategory", license.getTradeName().getName());
        reportParams.put("appType", "New");
        if(EgovThreadLocals.getMunicipalityName().contains("Corporation"))
        {
            reportParams.put("carporationulbType", Boolean.TRUE);
        }
        reportParams.put("municipality", EgovThreadLocals.getMunicipalityName());
        List<LicenseDemand> licDemandList = new ArrayList<LicenseDemand>(license.getDemandSet());
        String startYear = formatterYear.format(licDemandList.get(0).getEgInstallmentMaster().getFromDate());
        String EndYear = formatterYear.format(licDemandList.get(0).getEgInstallmentMaster().getToDate());
        String installMentYear = startYear + "-" + EndYear;
        reportParams.put("installMentYear", installMentYear);
        reportParams.put("applicationdate", formatter.format(license.getApplicationDate()));
        reportParams.put("demandUpdateDate", formatter.format(license.getCurrentDemand().getModifiedDate()));
        BigDecimal demandamt = BigDecimal.ZERO;

        for (EgDemandDetails deDet : license.getCurrentDemand().getEgDemandDetails()) {
            if (deDet.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                demandamt = demandamt.add(deDet.getAmount());
            }

        }
        reportParams.put("demandTotalamt", demandamt);
        return new ReportRequest(LICENSECERTIFICATE, license, reportParams);
    }

    @SkipValidation
    public String approveRenew() {
        processWorkflow("Renew");
        return "message";
    }

    protected void populateWorkflowBean() {
        workflowBean.setApproverPositionId(approverPositionId);
        workflowBean.setApproverComments(approverComments);
        workflowBean.setWorkFlowAction(workFlowAction);
        workflowBean.setCurrentState(currentState);
    }

    public String createDraftItems() {
        try {
            this.setCheckList();
            // licenseService().create(license()); //mani when made changes from
            // license to tradelicesne

            // initiateWorkFlowForLicense();
            persistenceService.getSession().flush();
        } catch (RuntimeException e) {
            loadAjaxedDropDowns();
            throw e;
        }
        addActionMessage(this.getText("license.submission.succesful")
                + license().getApplicationNumber());
        return Constants.ACKNOWLEDGEMENT;
    }

    @SkipValidation
    public String beforeRenew() {
        return Constants.BEFORE_RENEWAL;
    }

    @SkipValidation
    public String renew() {

        getSession().put("model.id", license().getId());
        try {
            licenseService().renew(license());
            // initiateWorkFlowForRenewLicense();
            addActionMessage(this.getText("license.renew.submission.succesful")
                    + license().getLicenseNumber());
        } catch (RuntimeException e) {
            loadAjaxedDropDowns();
            throw e;
        }
        return Constants.ACKNOWLEDGEMENT_RENEW;
    }

    public String createViolationFee() {
        getSession().put("model.id", license().getId());
        try {
            licenseService().createDemandForViolationFee(license());
            addActionMessage(this.getText("license.violation.fee.save")
                    + license().getLicenseNumber());
        } catch (RuntimeException e) {
            loadAjaxedDropDowns();
            throw e;
        }

        return "message";
    }

    public void setCheckList() {
        String str = "";
        if (selectedCheckList != null)
            for (Object obj : selectedCheckList) {
                if (selectedCheckList.size() > 1
                        && !selectedCheckList.get(selectedCheckList.size() - 1)
                        .equals(obj.toString()))
                    str = str.concat(obj.toString()).concat("^");
                else
                    str = str.concat(obj.toString());
                license().setLicenseCheckList(str);
            }
    }

    @SkipValidation
    public String enterExistingForm() {
        license().getLicensee().setGender(Constants.GENDER_MALE);
        return Constants.NEW;
    }

    public void prepareEnterExistingForm() {
        prepareNewForm();
    }

    // create workflow and pushes to drafts
    public void initiateWorkFlowForLicense() {
        // licenseService().initiateWorkFlowForLicense(license(), workflowBean);
        Position position = positionMasterService
                .getCurrentPositionForUser(EgovThreadLocals.getUserId());
        if (position != null)
            addActionMessage(this.getText("license.saved.in.inbox"));
    }

    public List<String> getBuildingTypeList() {
        return buildingTypeList;
    }

    public void setBuildingTypeList(List<String> buildingTypeList) {
        this.buildingTypeList = buildingTypeList;
    }

    public List<String> getGenderList() {
        return genderList;
    }

    public void setGenderList(List<String> genderList) {
        this.genderList = genderList;
    }

    @Override
    public StateAware getModel() {
        return license();
    }

    @SkipValidation
    public String newForm() {
        license().getLicensee().setGender(Constants.GENDER_MALE);
        return Constants.NEW;
    }

    public void prepareCreate() {
        prepareNewForm();
    }

    public void prepareNewForm() {
        this.prepare();
        buildingTypeList = new ArrayList<String>();
        buildingTypeList.add(Constants.BUILDINGTYPE_OWN_BUILDING);
        buildingTypeList.add(Constants.BUILDINGTYPE_RENTAL_AGREEMANT);
        genderList = new ArrayList<String>();
        genderList.add(Constants.GENDER_MALE);
        genderList.add(Constants.GENDER_FEMALE);
        addDropdownData(Constants.DROPDOWN_AREA_LIST_LICENSE,
                Collections.emptyList());
        addDropdownData(Constants.DROPDOWN_AREA_LIST_LICENSEE,
                Collections.emptyList());
        addDropdownData(Constants.DROPDOWN_DIVISION_LIST_LICENSE,
                Collections.emptyList());
        addDropdownData(Constants.DROPDOWN_DIVISION_LIST_LICENSEE,
                Collections.emptyList());
        // addDropdownData(Constants.DROPDOWN_ZONE_LIST,
        // licenseUtils.getAllZone());
        if (getModel().getClass().getSimpleName()
                .equalsIgnoreCase(Constants.ELECTRICALLICENSE_LICENSETYPE))
            addDropdownData(Constants.DROPDOWN_TRADENAME_LIST,
                    Collections.emptyList());
        else
            addDropdownData(Constants.DROPDOWN_TRADENAME_LIST,
                    licenseUtils.getAllTradeNames(getModel().getClass()
                            .getSimpleName()));

        setupWorkflowDetails();
        feeTypeId=feeTypeService.findByName(Constants.LICENSE_FEE_TYPE).getId(); 
    }

    public void prepareShowForApproval() {
        prepareNewForm();
    }

    /**
     * should be called from the second level only Approve will not end workflow
     * instead it sends to the creator in approved state
     */
    public void processWorkflow(String processType) {
        populateWorkflowBean();
        if (processType.equalsIgnoreCase(NEW)) {
            if(!Constants.BUTTONSUBMIT.equals(workFlowAction))
            licenseService().transitionWorkFlow(license(), workflowBean);
        } else if (processType.equalsIgnoreCase("Renew")) {
           if(! Constants.BUTTONSUBMIT.equals(workFlowAction))
            licenseService().processWorkflowForRenewLicense(license(), workflowBean);

        }
        User user = null;
        for (StateHistory state : license().getState().getHistory())
            if (state != null && state.getCreatedBy() != null)
                if (state.getValue().equals(Constants.WORKFLOW_STATE_NEW)) {
                    user = state.getCreatedBy();
                    break;
                }
        if (user == null)
            user = license().getCreatedBy();
        if (workflowBean.getWorkFlowAction().equalsIgnoreCase(
                Constants.BUTTONAPPROVE)) {
            if (license().getTradeName().isNocApplicable() != null
                    && license().getTradeName().isNocApplicable())
                addActionMessage(this.getText("license.approved.and.sent.to")
                        + user.getName() + " "
                        + this.getText("license.for.noc.generation"));
            else
                addActionMessage(this.getText("license.approved.success"));

        } else if (workflowBean.getWorkFlowAction().equalsIgnoreCase(
                Constants.BUTTONFORWARD)) {
            String userName = assignmentService
                    .getPrimaryAssignmentForPositon(
                            workflowBean.getApproverPositionId()).getEmployee()
                    .getUsername();
            addActionMessage(this.getText("license.sent") + " " + userName);
        } else if (workflowBean.getWorkFlowAction().equalsIgnoreCase(
                Constants.BUTTONREJECT)) {
            if (license().getState().getValue()
                    .contains(Constants.WORKFLOW_STATE_REJECTED))
                addActionMessage(this.getText("license.rejectedfirst")
                        + user.getName() + " "
                        + this.getText("license.rejectedlast"));
            else
                addActionMessage(this.getText("license.rejected")
                        + license().getApplicationNumber());
        } else if (workflowBean.getWorkFlowAction().equalsIgnoreCase(
                Constants.BUTTONGENERATEDCERTIFICATE))
            addActionMessage(this.getText("license.certifiacte.print.complete.recorded"));
        else if (workflowBean.getWorkFlowAction().equalsIgnoreCase(
                Constants.BUTTONPRINTCOMPLETED))
            addActionMessage(this.getText("license.rejection.certifiacte.print.complete.recorded"));
    }

    public void setLicenseUtils(LicenseUtils licenseUtils) {
        this.licenseUtils = licenseUtils;
    }

    public List<String> getSelectedCheckList() {
        return selectedCheckList;
    }

    public void setSelectedCheckList(List<String> selectedCheckList) {
        this.selectedCheckList = selectedCheckList;
    }

    public List<LicenseChecklistHelper> getCheckList() {
        return checkList;
    }

    public void setCheckList(List<LicenseChecklistHelper> checkList) {
        this.checkList = checkList;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    // sub class should get the object of the model and set to license()
    // /use contains() every where in this api
    // viewTradeLicense!showForApproval is picking id and gets Objects and
    // forwards here
    @SkipValidation
    public String showForApproval() {
        getSession().put("model.id", license().getId());
        String result = "approve";
        Long userId = securityUtils.getCurrentUser().getId();
        if (userId != null)
            setRoleName(licenseUtils.getRolesForUserId(userId));
        if (license().getState().getValue()
                .contains(Constants.WORKFLOW_STATE_TYPE_CREATENEWLICENSE)) {
            if (license().getState().getValue()
                    .contains(Constants.WORKFLOW_STATE_GENERATECERTIFICATE))
                result = "tl_generateCertificate";
            else if (license().getState().getValue()
                    .contains(Constants.WORKFLOW_STATE_REJECTED))
                result = "tl_editlicense";
            else if (license().getState().getValue()
                    .contains(Constants.WORKFLOW_STATE_GENERATEREJCERTIFICATE))
                result = "tl_generateRejCertificate";
            else if (license().getState().getValue()
                    .contains(Constants.WORKFLOW_STATE_GENERATENOC))
                result = "tl_generateNoc";
            else
                result = "approve";

        } else if (license().getState().getValue()
                .contains(Constants.WORKFLOW_STATE_TYPE_RENEWLICENSE)) {
            if (license().getState().getValue()
                    .contains(Constants.WORKFLOW_STATE_GENERATECERTIFICATE))
                result = "tl_generateCertificate";
            else if (license().getState().getValue()
                    .contains(Constants.WORKFLOW_STATE_REJECTED))
                result = "approveRenew";
            else if (license().getState().getValue()
                    .contains(Constants.WORKFLOW_STATE_GENERATEREJCERTIFICATE))
                result = "tl_generateRejCertificate";
            else
                result = "approveRenew";

        } else if (license().getState().getValue()
                .contains(Constants.WORKFLOW_STATE_TYPE_TRANSFERLICENSE))
            if (license().getState().getValue()
                    .contains(Constants.WORKFLOW_STATE_GENERATECERTIFICATE))
                result = "tl_generateCertificate";
            else if (license().getState().getValue()
                    .contains(Constants.WORKFLOW_STATE_REJECTED))
                result = "transfertl_editlicense";
            else
                result = "transfertl_approve";
        return result;
    }

    public void loadAjaxedDropDowns() {
        CommonAjaxAction commonAjaxAction = new CommonAjaxAction();
        commonAjaxAction.setLicenseUtils(licenseUtils);
        commonAjaxAction.setBoundaryService(boundaryService);
        commonAjaxAction.setEisCommonService(eisCommonService);
        commonAjaxAction.setDesignationService(designationService);

        // if the zone is loaded from ui which have trigger load division
        // set those list
        // else is not required since empty lists are set in prepare itself
        if (license().getLicenseZoneId() != null) {
            commonAjaxAction.setZoneId(license().getLicenseZoneId().intValue());
            commonAjaxAction.populateDivisions();
            addDropdownData(Constants.DROPDOWN_DIVISION_LIST_LICENSE,
                    commonAjaxAction.getDivisionList());
        }
        if (license().getLicenseeZoneId() != null) {
            commonAjaxAction
                    .setZoneId(license().getLicenseeZoneId().intValue());
            commonAjaxAction.populateDivisions();
            addDropdownData(Constants.DROPDOWN_DIVISION_LIST_LICENSEE,
                    commonAjaxAction.getDivisionList());
        }
        if (workflowBean.getDepartmentId() != null) {
            commonAjaxAction.setDepartmentId(workflowBean.getDepartmentId());
            commonAjaxAction.ajaxPopulateDesignationsByDept();
            workflowBean.setDesignationList(commonAjaxAction
                    .getDesignationList());
        }
        if (workflowBean.getDesignationId() != null) {
            commonAjaxAction.setDesignationId(workflowBean.getDesignationId());
            commonAjaxAction.ajaxPopulateUsersByDesignation();
            workflowBean.setAppoverUserList(commonAjaxAction
                    .getAllActiveUsersByGivenDesg());
        }
    }

    public void setupWorkflowDetails() {
        workflowBean.setDepartmentList(licenseUtils.getAllDepartments());
        workflowBean.setDesignationList(Collections.emptyList());
        workflowBean.setAppoverUserList(Collections.emptyList());
    }

    // this will give current Demand only while saving or immediate after create
    public LicenseDemand getCurrentYearDemand() {
        return license().getLicenseDemand();
    }

    // this will give current Demand only while saving or immediate after create
    public String getPayableAmountInWords() {
        String baseDemand = "";

        baseDemand = NumberToWord.amountInWords(getAapplicableDemand(
                license().getCurrentDemand()).doubleValue());

        return baseDemand;
    }

    public String getCollectedDemandAmountInWords() {
        // this below api will give you the current year Demand from database
        LicenseDemand currentYearDemand = licenseService().getCurrentYearDemand(
                license());
        return NumberToWord.amountInWords(currentYearDemand.getAmtCollected()
                .doubleValue());
    }

    public List<LicenseChecklistHelper> getSelectedChecklist() {
        List<LicenseChecklistHelper> checkList = new ArrayList<LicenseChecklistHelper>();
        checkList = licenseService().getLicenseChecklist(license());
        return checkList;
    }

    public boolean isCurrent(EgDemandDetails dd) {
        boolean isCurrent = false;
        Installment currInstallment = licenseUtils.getCurrInstallment(dd
                .getEgDemandReason().getEgDemandReasonMaster().getEgModule());
        if (currInstallment.getId().intValue() == dd.getEgDemandReason()
                .getEgInstallmentMaster().getId().intValue())
            isCurrent = true;
        return isCurrent;

    }

    public BigDecimal getAapplicableDemand(EgDemand demand) {
        // TODO: Code was reviewed by Satyam, No changes required
        BigDecimal total = BigDecimal.ZERO;
        if (demand.getIsHistory().equals("N"))
            for (EgDemandDetails details : demand.getEgDemandDetails()) {
                total = total.add(details.getAmount());
                total = total.subtract(details.getAmtRebate());
            }
        return total;
    }

    public boolean isCitizen() {
        return securityUtils.currentUserType().equals(UserType.CITIZEN);
    }

    protected Assignment getWorkflowInitiator(License license) {
        Assignment wfInitiator;
        if (!license.getStateHistory().isEmpty())
            wfInitiator = assignmentService
                    .getPrimaryAssignmentForPositon(license.getStateHistory()
                            .get(0).getOwnerPosition().getId());
        else
            wfInitiator = assignmentService
                    .getPrimaryAssignmentForPositon(license.getState()
                            .getOwnerPosition().getId());
        return wfInitiator;
    }

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }
    
    public Long getFeeTypeId() {
        return feeTypeId;
    }

    public void setFeeTypeId(Long feeTypeId) {
        this.feeTypeId = feeTypeId;
    }

    public Long getParentBndryId() {
        return parentBndryId;
    }

    public void setParentBndryId(Long parentBndryId) {
        this.parentBndryId = parentBndryId;
    }

}
