/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *     accountability and the service delivery of the government  organizations.
 *
 *      Copyright (C) <2015>  eGovernments Foundation
 *
 *      The updated version of eGov suite of products as by eGovernments Foundation
 *      is available at http://www.egovernments.org
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with this program. If not, see http://www.gnu.org/licenses/ or
 *      http://www.gnu.org/licenses/gpl.html .
 *
 *      In addition to the terms of the GPL license to be adhered to in using this
 *      program, the following additional terms are to be complied with:
 *
 *  	1) All versions of this program, verbatim or modified must carry this
 *  	   Legal Notice.
 *
 *  	2) Any misrepresentation of the origin of the material is prohibited. It
 *  	   is required that all modified versions of this material be marked in
 *  	   reasonable ways as different from the original version.
 *
 *  	3) This license does not grant any rights to any user of the program
 *  	   with regards to rights under trademark law for use of the trade names
 *  	   or trademarks of eGovernments Foundation.
 *
 *    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.tl.web.actions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
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
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.utils.NumberToWord;
import org.egov.pims.commons.Position;
import org.egov.tl.domain.entity.License;
import org.egov.tl.domain.entity.LicenseCategory;
import org.egov.tl.domain.entity.LicenseDemand;
import org.egov.tl.domain.entity.LicenseSubCategory;
import org.egov.tl.domain.entity.NatureOfBusiness;
import org.egov.tl.domain.entity.TradeLicense;
import org.egov.tl.domain.entity.UnitOfMeasurement;
import org.egov.tl.domain.entity.WorkflowBean;
import org.egov.tl.domain.service.BaseLicenseService;
import org.egov.tl.utils.Constants;
import org.egov.tl.utils.LicenseChecklistHelper;
import org.egov.tl.utils.LicenseUtils;
import org.egov.tl.web.actions.domain.CommonAjaxAction;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author mani for Implementing New License action
 */
@ParentPackage("egov")
@Results({
        @Result(name = "collection", type = "redirectAction", location = "licenseBillCollect", params = {
                "namespace", "/integration", "method", "renew" }),
        @Result(name = "tl_editlicense", type = "redirectAction", location = "editTradeLicense-beforeEdit", params = {
                "namespace", "/newtradelicense"}),
        @Result(name = "tl_approve", type = "redirectAction", location = "viewTradeLicense", params = {
                "namespace", "/viewtradelicense", "method", "showForApproval" }),
        @Result(name = "tl_generateRejCertificate", type = "redirectAction", location = "viewTradeLicense", params = {
                "namespace", "/viewtradelicense", "method", "generateRejCertificate" }),
        @Result(name = "tl_generateCertificate", type = "redirectAction", location = "viewTradeLicense", params = {
                "namespace", "/viewtradelicense", "method", "generateCertificate" }),
        @Result(name = "tl_generateNoc", type = "redirectAction", location = "viewTradeLicense", params = {
                "namespace", "/viewtradelicense", "method", "generateNoc" }),
        @Result(name = "transfertl_editlicense", type = "redirectAction", location = "transferTradeLicense", params = {
                "namespace", "/transfer", "method", "beforeEdit" }),
        @Result(name = "transfertl_approve", type = "redirectAction", location = "transferTradeLicense", params = {
                "namespace", "/transfer", "method", "showForApproval" }),
        @Result(name = "approve", location="newTradeLicense-new.jsp")
})
public abstract class BaseLicenseAction extends GenericWorkFlowAction {
    private static final long serialVersionUID = 1L;
    // sub classes should add setters for the following beans
    @Autowired
    protected LicenseUtils licenseUtils;
    @Autowired
    private UserService userService;
    // sub class should add getter and setter this workflowBean
    protected WorkflowBean workflowBean = new WorkflowBean();
    protected List<String> buildingTypeList;
    protected List<String> genderList;
    protected List<String> selectedCheckList;
    protected List<LicenseChecklistHelper> checkList;
    protected String roleName;
    @Autowired
    private PositionMasterService positionMasterService;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private SimpleWorkflowService<License> licenseWorkflowService;
    @Autowired
    private BoundaryService boundaryService;
    @Autowired
    private DesignationService designationService;
    @Autowired
    private EisCommonService eisCommonService;
    protected abstract License license();
    protected abstract BaseLicenseService service();
    protected String ackMessage;

    public BaseLicenseAction() {
        this.addRelatedEntity("boundary", Boundary.class);
        this.addRelatedEntity("licensee.boundary", Boundary.class);
        this.addRelatedEntity("buildingType", NatureOfBusiness.class);
        this.addRelatedEntity("category", LicenseCategory.class);
        this.addRelatedEntity("uom", UnitOfMeasurement.class);
        this.addRelatedEntity("tradeName", LicenseSubCategory.class);
    }

    // sub class should get the object of the model and set to license()
    @SuppressWarnings("unchecked")
    public String approve() {
        processWorkflow(NEW);
        persistenceService.persist(license());
        // Generate PFA Certificate on final approval
        if (workflowBean.getWorkFlowAction().equalsIgnoreCase(Constants.BUTTONAPPROVE)) {
            return Constants.PFACERTIFICATE;
        } else
            return "message";
    }

    @SkipValidation
    public String approveRenew() {
        processWorkflow("Renew");
        return "message";
    }
    public String create() {
    
    	return "";
    }
    

    @SuppressWarnings("unchecked")
    @ValidationErrorPage(Constants.NEW)
    public String create(TradeLicense license) {
        try {
            this.setCheckList();
            service().processAndStoreDocument(license().getDocuments());
            service().create(license);
            populateWorkflowBean();
            service().transitionWorkFlow(license(), workflowBean);
            persistenceService.applyAuditing(license().getState());
            persistenceService.create(license());
        } catch (final RuntimeException e) {
            loadAjaxedDropDowns();
            throw e;
        }
        addActionMessage(this.getText("license.submission.succesful") + license().getApplicationNumber());
        return Constants.ACKNOWLEDGEMENT;
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
          //  service().create(license());  //mani when made changes from license to tradelicesne
            
           // initiateWorkFlowForLicense();
            persistenceService.getSession().flush();
        } catch (final RuntimeException e) {
            loadAjaxedDropDowns();
            throw e;
        }
        addActionMessage(this.getText("license.submission.succesful") + license().getApplicationNumber());
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
            service().renew(license());
            // initiateWorkFlowForRenewLicense();
            addActionMessage(this.getText("license.renew.submission.succesful") + license().getLicenseNumber());
        } catch (final RuntimeException e) {
            loadAjaxedDropDowns();
            throw e;
        }
        return Constants.ACKNOWLEDGEMENT_RENEW;
    }

    public String createViolationFee()
    {
        getSession().put("model.id", license().getId());
        try {
            service().createDemandForViolationFee(license());
            addActionMessage(this.getText("license.violation.fee.save") + license().getLicenseNumber());
        } catch (final RuntimeException e) {
            loadAjaxedDropDowns();
            throw e;
        }

        return "message";
    }

    public void setCheckList() {
        String str = "";
        if (selectedCheckList != null)
            for (final Object obj : selectedCheckList) {
                if (selectedCheckList.size() > 1 && !selectedCheckList.get(selectedCheckList.size() - 1).equals(obj.toString()))
                    str = str.concat(obj.toString()).concat("^");
                else
                    str = str.concat(obj.toString());
                license().setLicenseCheckList(str);
            }
    }

    @ValidationErrorPage(Constants.NEW)
    public String enterExisting() {
        service().enterExistingLicense(license());
        service().updateLicenseForFinalApproval(license());
        addActionMessage(this.getText("license.entry.succesful") + "  " + license().getLicenseNumber());
        final Module module = license().getTradeName().getLicenseType().getModule();
        /*if (module.getName().equals(Constants.PWDLICENSE_MODULENAME))
            licenseUtils.updateContractorForDepartment(license(), Constants.LICENSE_STATUS_UPDATE,
                    Constants.LICENSE_STATUS_ACTIVE);*/
        return "viewlicense";
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
        // service().initiateWorkFlowForLicense(license(), workflowBean);
        final Position position = positionMasterService.getCurrentPositionForUser(EgovThreadLocals.getUserId());
        if (position != null)
            addActionMessage(this.getText("license.saved.in.inbox"));
    }

    /*
     * public void endWorkFlowForLicense() { service().endWorkFlowForLicense(license()); }
     */

    // create workflow and pushes to drafts
    /*
     * public void initiateWorkFlowForLicenseDraft() { service().initiateWorkFlowForLicenseDraft(license(), workflowBean);
     * addActionMessage(this.getText("license.saved.in.draft")); }
     */

    /*
     * public void initiateWorkFlowForRenewLicense() { service().initiateWorkFlowForRenewLicense(license(), workflowBean);
     * addActionMessage(this.getText("license.saved.in.inbox")); }
     */

    public List<String> getBuildingTypeList() {
        return buildingTypeList;
    }

    public List<String> getGenderList() {
        return genderList;
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
        super.prepare();
        buildingTypeList = new ArrayList<String>();
        buildingTypeList.add(Constants.BUILDINGTYPE_OWN_BUILDING);
        buildingTypeList.add(Constants.BUILDINGTYPE_RENTAL_AGREEMANT);
        genderList = new ArrayList<String>();
        genderList.add(Constants.GENDER_MALE);
        genderList.add(Constants.GENDER_FEMALE);
        addDropdownData(Constants.DROPDOWN_AREA_LIST_LICENSE, Collections.EMPTY_LIST);
        addDropdownData(Constants.DROPDOWN_AREA_LIST_LICENSEE, Collections.EMPTY_LIST);
        addDropdownData(Constants.DROPDOWN_DIVISION_LIST_LICENSE, Collections.EMPTY_LIST);
        addDropdownData(Constants.DROPDOWN_DIVISION_LIST_LICENSEE, Collections.EMPTY_LIST);
      //  addDropdownData(Constants.DROPDOWN_ZONE_LIST, licenseUtils.getAllZone());
        if (getModel().getClass().getSimpleName().equalsIgnoreCase(Constants.ELECTRICALLICENSE_LICENSETYPE))
            addDropdownData(Constants.DROPDOWN_TRADENAME_LIST, Collections.EMPTY_LIST);
        else
            addDropdownData(Constants.DROPDOWN_TRADENAME_LIST,
                    licenseUtils.getAllTradeNames(getModel().getClass().getSimpleName())); 

        setupWorkflowDetails();
    }

    public void prepareShowForApproval() {
        prepareNewForm();
    }

    /**
     * should be called from the second level only Approve will not end workflow instead it sends to the creator in approved state
     */
    public void processWorkflow(final String processType) {
        populateWorkflowBean();
        if (processType.equalsIgnoreCase(NEW)) {
            service().transitionWorkFlow(license(), workflowBean);
        } else if (processType.equalsIgnoreCase("Renew")) {
            service().processWorkflowForRenewLicense(license(), workflowBean);
             
        }
        User user = null;
        for (final StateHistory state : license().getState().getHistory())
            if (state != null && state.getCreatedBy() != null)
                if (state.getValue().equals(Constants.WORKFLOW_STATE_NEW)) {
                    user = state.getCreatedBy();
                    break;
                }
        if (user == null)
            user = license().getCreatedBy();
        if (workflowBean.getWorkFlowAction().equalsIgnoreCase(Constants.BUTTONAPPROVE)) {
            if (license().getTradeName().isNocApplicable() != null && license().getTradeName().isNocApplicable())
                addActionMessage(this.getText("license.approved.and.sent.to") + user.getName() + " "
                        + this.getText("license.for.noc.generation"));
            else
                addActionMessage(this.getText("license.approved.success"));

        } else if (workflowBean.getWorkFlowAction().equalsIgnoreCase(Constants.BUTTONFORWARD)) {
            final String userName = assignmentService.getPrimaryAssignmentForPositon(workflowBean.getApproverPositionId())
                    .getEmployee().getUsername();
            addActionMessage(this.getText("license.sent") + " " + userName);
        } else if (workflowBean.getWorkFlowAction().equalsIgnoreCase(Constants.BUTTONREJECT)) {
            if (license().getState().getValue().contains(Constants.WORKFLOW_STATE_REJECTED))
                addActionMessage(this.getText("license.rejectedfirst") + user.getName() + " " +
                        this.getText("license.rejectedlast"));
            else
                addActionMessage(this.getText("license.rejected") +
                        license().getApplicationNumber());
        }
        else if (workflowBean.getWorkFlowAction().equalsIgnoreCase(Constants.BUTTONGENERATEDCERTIFICATE))
            addActionMessage(this.getText("license.certifiacte.print.complete.recorded"));
        else if (workflowBean.getWorkFlowAction().equalsIgnoreCase(Constants.BUTTONPRINTCOMPLETED))
            addActionMessage(this.getText("license.rejection.certifiacte.print.complete.recorded"));
    }

    public void setBuildingTypeList(final List<String> buildingTypeList) {
        this.buildingTypeList = buildingTypeList;
    }

    public void setGenderList(final List<String> genderList) {
        this.genderList = genderList;
    }

    public void setLicenseUtils(final LicenseUtils licenseUtils) {
        this.licenseUtils = licenseUtils;
    }

    public List<String> getSelectedCheckList() {
        return selectedCheckList;
    }

    public void setSelectedCheckList(final List<String> selectedCheckList) {
        this.selectedCheckList = selectedCheckList;
    }

    public List<LicenseChecklistHelper> getCheckList() {
        return checkList;
    }

    public void setCheckList(final List<LicenseChecklistHelper> checkList) {
        this.checkList = checkList;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(final String roleName) {
        this.roleName = roleName;
    }

    // sub class should get the object of the model and set to license()
    // /use contains() every where in this api
    // viewTradeLicense!showForApproval is picking id and gets Objects and forwards here
    @SkipValidation
    public String showForApproval() {
        getSession().put("model.id", license().getId());
        String result = "approve";
        final Long userId = securityUtils.getCurrentUser().getId();
        if (userId != null)
            setRoleName(licenseUtils.getRolesForUserId(userId));
        if (license().getState().getValue().contains(Constants.WORKFLOW_STATE_TYPE_CREATENEWLICENSE)) {
            if (license().getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATECERTIFICATE))
                result = "tl_generateCertificate";
            else if (license().getState().getValue().contains(Constants.WORKFLOW_STATE_REJECTED))
                result = "tl_editlicense";
            else if (license().getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATEREJCERTIFICATE))
                result = "tl_generateRejCertificate";
            else if (license().getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATENOC))
                result = "tl_generateNoc";
            else
                result = "approve";

        } else if (license().getState().getValue().contains(Constants.WORKFLOW_STATE_TYPE_RENEWLICENSE)) {
            if (license().getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATECERTIFICATE))
                result = "tl_generateCertificate";
            else if (license().getState().getValue().contains(Constants.WORKFLOW_STATE_REJECTED))
                result = "approveRenew";
            else if (license().getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATEREJCERTIFICATE))
                result = "tl_generateRejCertificate";
            else
                result = "approveRenew";

        } else if (license().getState().getValue().contains(Constants.WORKFLOW_STATE_TYPE_TRANSFERLICENSE))
            if (license().getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATECERTIFICATE))
                result = "tl_generateCertificate";
            else if (license().getState().getValue().contains(Constants.WORKFLOW_STATE_REJECTED))
                result = "transfertl_editlicense";
            else
                result = "transfertl_approve";
        return result;
    }

    public void loadAjaxedDropDowns() {
        final CommonAjaxAction commonAjaxAction = new CommonAjaxAction();
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
            addDropdownData(Constants.DROPDOWN_DIVISION_LIST_LICENSE, commonAjaxAction.getDivisionList());
        }
        if (license().getLicenseeZoneId() != null) {
            commonAjaxAction.setZoneId(license().getLicenseeZoneId().intValue());
            commonAjaxAction.populateDivisions();
            addDropdownData(Constants.DROPDOWN_DIVISION_LIST_LICENSEE, commonAjaxAction.getDivisionList());
        }
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
        workflowBean.setDesignationList(Collections.EMPTY_LIST);
        workflowBean.setAppoverUserList(Collections.EMPTY_LIST);
    }

    // this will give current Demand only while saving or immediate after create
    public LicenseDemand getCurrentYearDemand() {
        return license().getLicenseDemand();
    }

    // this will give current Demand only while saving or immediate after create
    public String getPayableAmountInWords() {
        String baseDemand = "";

        baseDemand = NumberToWord.amountInWords(getAapplicableDemand(license().getCurrentDemand()).doubleValue());

        return baseDemand;
    }

    public String getCollectedDemandAmountInWords() {
        // this below api will give you the current year Demand from database
        final LicenseDemand currentYearDemand = service().getCurrentYearDemand(license());
        return NumberToWord.amountInWords(currentYearDemand.getAmtCollected().doubleValue());
    }

    public List<LicenseChecklistHelper> getSelectedChecklist() {
        List<LicenseChecklistHelper> checkList = new ArrayList<LicenseChecklistHelper>();
        checkList = service().getLicenseChecklist(license());
        return checkList;
    }

    public boolean isCurrent(final EgDemandDetails dd)
    {
        boolean isCurrent = false;
        final Installment currInstallment = licenseUtils.getCurrInstallment(dd.getEgDemandReason().getEgDemandReasonMaster()
                .getEgModule());
        if (currInstallment.getId().intValue() == dd.getEgDemandReason().getEgInstallmentMaster().getId().intValue())
            isCurrent = true;
        return isCurrent;

    }

    public BigDecimal getAapplicableDemand(final EgDemand demand)
    {
        // TODO: Code was reviewed by Satyam, No changes required
        BigDecimal total = BigDecimal.ZERO;
        if (demand.getIsHistory().equals("N"))
            for (final EgDemandDetails details : demand.getEgDemandDetails()) {
                total = total.add(details.getAmount());
                total = total.subtract(details.getAmtRebate());
            }
        return total;
    }

    public boolean isCitizen() {
        return positionMasterService.getCurrentPositionForUser(EgovThreadLocals.getUserId()) == null;
    }

    /*
     * public void setAuditEventService(final AuditEventService auditEventService) { this.auditEventService = auditEventService; }
     * public AuditEntity getAuditEntityFromModule(final String module) { AuditEntity entity = null; if
     * (module.equals(Constants.ELECTRICALLICENSE_MODULENAME)) entity = AuditEntity.ECL_LIC; else if
     * (module.equals(Constants.HOSPITALLICENSE_MODULENAME)) entity = AuditEntity.HPL_LIC; else if
     * (module.equals(Constants.HAWKERLICENSE_MODULENAME)) entity = AuditEntity.HWKL_LIC; else if
     * (module.equals(Constants.TRADELICENSE_MODULENAME)) entity = AuditEntity.TL_LIC; else if
     * (module.equals(Constants.PWDLICENSE_MODULENAME)) entity = AuditEntity.PWDL_LIC; else if
     * (module.equals(Constants.VETLICENSE_MODULENAME)) entity = AuditEntity.VETL_LIC; else if
     * (module.equals(Constants.WATERWORKSLICENSE_MODULENAME)) entity = AuditEntity.WWL_LIC; return entity; } public AuditModule
     * getAuditModuleFromLicModule(final String module) { AuditModule auditModule = null; if
     * (module.equals(Constants.ELECTRICALLICENSE_MODULENAME)) auditModule = AuditModule.ECL; else if
     * (module.equals(Constants.HOSPITALLICENSE_MODULENAME)) auditModule = AuditModule.HPL; else if
     * (module.equals(Constants.HAWKERLICENSE_MODULENAME)) auditModule = AuditModule.HWKL; else if
     * (module.equals(Constants.TRADELICENSE_MODULENAME)) auditModule = AuditModule.TL; else if
     * (module.equals(Constants.PWDLICENSE_MODULENAME)) auditModule = AuditModule.PWDL; else if
     * (module.equals(Constants.VETLICENSE_MODULENAME)) auditModule = AuditModule.VETL; else if
     * (module.equals(Constants.WATERWORKSLICENSE_MODULENAME)) auditModule = AuditModule.WWL; return auditModule; } protected void
     * doAuditing(final AuditModule auditModule, final AuditEntity auditEntity, final String action, final String details) { final
     * License license = (License) getModel(); final AuditEvent auditEvent = new AuditEvent(auditModule, auditEntity, action,
     * license().getLicenseNumber(), details); auditEvent.setPkId(license.getId());
     * auditEvent.setDetails2(workflowBean.getActionName()); auditEventService.createAuditEvent(auditEvent, license().getClass());
     * }
     */

    @SkipValidation
    public String auditReport() {
        return "auditReport";
    }
    
    /*public void transitionWorkFlow(final License license) {
        final DateTime currentDate = new DateTime();
        final User user = securityUtils.getCurrentUser();
        final Assignment userAssignment = assignmentService.getPrimaryAssignmentForUser(user.getId());
        Position pos = null;
        Assignment wfInitiator = null;

        if (null != license.getId())
            wfInitiator = getWorkflowInitiator(license);

        if (BUTTONREJECT.equalsIgnoreCase(workFlowAction)) {
            if (wfInitiator.equals(userAssignment)) {
                license.transition(true).end().withSenderName(user.getName()).withComments(approverComments)
                        .withDateInfo(currentDate.toDate());
            } else {
                final String stateValue = license.getCurrentState().getValue();
                license.transition(true).withSenderName(user.getName()).withComments(approverComments)
                        .withStateValue(stateValue).withDateInfo(currentDate.toDate())
                        .withOwner(wfInitiator.getPosition()).withNextAction("Assistant Health Officer approval pending");
            }

        } else {
            if (null != approverPositionId && approverPositionId != -1)
                pos = (Position) persistenceService.find("from Position where id=?", approverPositionId);
            else if (BUTTONAPPROVE.equalsIgnoreCase(workFlowAction))
                pos = wfInitiator.getPosition();
            if (null == license.getState()) {
                final WorkFlowMatrix wfmatrix = licenseWorkflowService.getWfMatrix(license.getStateType(), null,
                        null, null, currentState, null);
                license.transition().start().withSenderName(user.getName()).withComments(approverComments)
                        .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate()).withOwner(pos)
                        .withNextAction(wfmatrix.getNextAction());
            } else if (license.getCurrentState().getNextAction().equalsIgnoreCase("END"))
                license.transition(true).end().withSenderName(user.getName()).withComments(approverComments)
                        .withDateInfo(currentDate.toDate());
            else {
                final WorkFlowMatrix wfmatrix = licenseWorkflowService.getWfMatrix(license.getStateType(), null,
                        null, null, license.getCurrentState().getValue(), null);
                license.transition(true).withSenderName(user.getName()).withComments(approverComments)
                        .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate()).withOwner(pos)
                        .withNextAction(wfmatrix.getNextAction());
            }
        }
        if (approverName != null && !approverName.isEmpty() && !approverName.equalsIgnoreCase("----Choose----")) {
            final String approvalmesg = " Succesfully Forwarded to : ";
            ackMessage = ackMessage == null ? approvalmesg : ackMessage + approvalmesg;
        } else if (workFlowAction != null && workFlowAction.equalsIgnoreCase("cancel")) {
            final String approvalmesg = " Succesfully Cancelled.";
            ackMessage = ackMessage == null ? approvalmesg : ackMessage + approvalmesg;
        }
    }*/
    
    protected Assignment getWorkflowInitiator(final License license) {
        Assignment wfInitiator;
        if (!license.getStateHistory().isEmpty())
            wfInitiator = assignmentService.getPrimaryAssignmentForPositon(license.getStateHistory().get(0)
                    .getOwnerPosition().getId());
        else
            wfInitiator = assignmentService.getPrimaryAssignmentForPositon(license.getState().getOwnerPosition()
                    .getId());
        return wfInitiator;
    }
}
