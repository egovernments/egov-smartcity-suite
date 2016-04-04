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
package org.egov.tl.web.actions.objection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.eis.web.actions.workflow.GenericWorkFlowAction;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.tl.entity.License;
import org.egov.tl.entity.LicenseStatusValues;
import org.egov.tl.entity.WorkflowBean;
import org.egov.tl.entity.objection.LicenseObjection;
import org.egov.tl.service.objection.ObjectionService;
import org.egov.tl.utils.Constants;
import org.egov.tl.utils.LicenseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

@Results({
        @Result(name = Constants.NEW, location = "objection-" + Constants.NEW + ".jsp"),
        @Result(name = "message", location = "objection-message.jsp"),
        @Result(name = "approve", location = "objection-approve.jsp"),
        @Result(name = "prenotice", location = "objection-prenotice.jsp"),
        @Result(name = "prenoticeletter", location = "objection-prenoticeletter.jsp"),
        @Result(name = "showcausenotice", location = "objection-showcausenotice.jsp"),
        @Result(name = "scnoticeletter", location = "objection-scnoticeletter.jsp")
})
public class ObjectionAction extends GenericWorkFlowAction {

    private static final long serialVersionUID = 1L;

    private WorkflowBean workflowBean = new WorkflowBean();
    private LicenseObjection objection = new LicenseObjection();
    private List<String> activityTypeList;
    private Long licenseId;
    private Map<Integer, String> objectionReasons;
    private LicenseStatusValues lsv;
    private License license;
    private String roleName;

    @Autowired
    @Qualifier("objectionService")
    protected ObjectionService objectionService;
    @Autowired
    protected LicenseUtils licenseUtils;
    @Autowired
    private SecurityUtils securityUtils;


    public ObjectionAction() {
        super();
    }

    public LicenseStatusValues getLsv() {
        return lsv;
    }

    public void setLsv(final LicenseStatusValues lsv) {
        this.lsv = lsv;
    }

    public Map<Integer, String> getObjectionReasons() {
        return licenseUtils.getObjectionReasons();
    }

    public List<String> getActivityTypeList() {
        return activityTypeList;
    }

    public void setActivityTypeList(final List<String> activityTypeList) {
        this.activityTypeList = activityTypeList;
    }

    public LicenseUtils getLicenseUtils() {
        return licenseUtils;
    }

    public void setLicenseUtils(final LicenseUtils licenseUtils) {
        this.licenseUtils = licenseUtils;
    }

    public WorkflowBean getWorkflowBean() {
        return workflowBean;
    }

    public void setWorkflowBean(final WorkflowBean workflowBean) {
        this.workflowBean = workflowBean;
    }

    public Long getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(final Long licenseId) {
        this.licenseId = licenseId;
    }

    public License getLicense() {
        return license;
    }

    public void setLicense(final License license) {
        this.license = license;
    }

    @Override
    public StateAware getModel() {
        return objection;
    }

    public void setupWorkflowDetails() {
        workflowBean.setDepartmentList(licenseUtils.getAllDepartments());
        workflowBean.setDesignationList(Collections.EMPTY_LIST);
        workflowBean.setAppoverUserList(Collections.EMPTY_LIST);
    }

    public LicenseObjection getObjection() {
        return objection;
    }

    public void setObjection(final LicenseObjection objection) {
        this.objection = objection;
    }

    public void prepareNewForm() {
        setupWorkflowDetails();
    }

    @SkipValidation
    @Action(value = "/objection/objection-newForm")
    public String newForm() {
        license = (License) persistenceService.find("from License where id=?", licenseId);
        objection.setLicense(license);
        return Constants.NEW;
    }

    @ValidationErrorPage(Constants.NEW)
    @Validations(
            requiredFields = {@RequiredFieldValidator(
                    fieldName = "name", message = "", key = Constants.REQUIRED), @RequiredFieldValidator(
                    fieldName = "address", message = "", key = Constants.REQUIRED), @RequiredFieldValidator(
                    fieldName = "objectionDate", message = "", key = Constants.REQUIRED), @RequiredFieldValidator(
                    fieldName = "details", message = "", key = Constants.REQUIRED), @RequiredFieldValidator(
                    fieldName = "reason", message = "", key = Constants.REQUIRED)})
    @Action(value = "/objection/objection-create")
    public String create() {
        objectionService.setContextName(ServletActionContext.getRequest().getContextPath());
        objection = objectionService.recordObjection(objection, licenseId, workflowBean);
        addActionMessage(this.getText("license.objection.succesful") + " " + objection.getNumber());
        return "message";
    }

    /**
     * this will receive response or inspection details
     *
     * @return
     */
    public void prepareShowForApproval() {
        prepareNewForm();
        this.setRoleName(this.securityUtils.getCurrentUser().getRoles().toString());
        activityTypeList = new ArrayList<String>();
        activityTypeList.add(Constants.ACTIVITY_INSPECTION);
        activityTypeList.add(Constants.ACTIVITY_RESPONSE);
    }

    @SkipValidation
    @Action(value = "/objection/objection-showForApproval")
    public String showForApproval() {
        objectionService.setContextName(ServletActionContext.getRequest().getContextPath());
        objection = objectionService.findByNamedQuery(LicenseObjection.BY_ID, objection.getId());
        Collections.reverse(objection.getActivities());
        if (objection.getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATECANCELLATIONLETTER))
            return generateRejCertificate();
        else if (objection.getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATESUSPENSIONLETTER))
            return generateSusLetter();
        else
            return "approve";
    }

    @SkipValidation
    public String approve() {
        final String type = objection.getActivities().get(objection.getActivities().size() - 1).getType();
        objectionService.setContextName(ServletActionContext.getRequest().getContextPath());
        objection = objectionService.recordResponseOrInspection(objection, workflowBean);
        if ("Inspection".equalsIgnoreCase(type))
            addActionMessage(this.getText("license.inspection.succesful"));
        else if ("Response".equalsIgnoreCase(type))
            addActionMessage(this.getText("license.response.succesful"));
        else if ("PreNotice".equalsIgnoreCase(type))
            addActionMessage(this.getText("license.preliminarynotice.succesful"));
        else if ("SCNotice".equalsIgnoreCase(type))
            addActionMessage(this.getText("license.showcausenotice.succesful"));
        else if ("suspend".equalsIgnoreCase(type))
            addActionMessage(this.getText("license.suspend.succesful"));
        else if ("cancelled".equalsIgnoreCase(type))
            addActionMessage(this.getText("license.cancelled.succesful"));
        else if (type == null && workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONFORWARD))
            addActionMessage(this.getText("license.forward.succesful"));
        else if (type == null && workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONREJECT)) {
            if (objection.getCurrentState().getValue().equals("end"))
                addActionMessage(this.getText("license.rejection.end"));
            else
                addActionMessage(this.getText("license.rejected.succesful"));
        } else if (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONAPPROVE))
            addActionMessage(this.getText("license.objection.approved"));
        return "message";

    }

    @SkipValidation
    @Action(value = "/objection/objection-preNotice")
    public String preNotice() {
        objection = objectionService.findByNamedQuery(LicenseObjection.BY_ID, objection.getId());
        return "prenotice";
    }

    @SkipValidation
    @Action(value = "/objection/objection-preliminaryNotice")
    public String preliminaryNotice() {
        objection = objectionService.findByNamedQuery(LicenseObjection.BY_ID, objection.getId());
        generateNotice("_PreNotice");
        return "prenoticeletter";
    }

    @SkipValidation
    @Action(value = "/objection/objection-scNotice")
    public String scNotice() {
        objection = objectionService.findByNamedQuery(LicenseObjection.BY_ID, objection.getId());
        return "showcausenotice";
    }

    @SkipValidation
    @Action(value = "/objection/objection-showCauseNotice")
    public String showCauseNotice() {
        objection = objectionService.findByNamedQuery(LicenseObjection.BY_ID, objection.getId());
        generateNotice("_SCNotice");
        return "scnoticeletter";
    }

    @SkipValidation
    public String generateRejCertificate() {
        lsv = (LicenseStatusValues) persistenceService.find("from LicenseStatusValues where license.id=?", objection.getLicense()
                .getId());
        generateNotice("_cancelled");
        return "cancellationletter";
    }

    @SkipValidation
    public String generateSusLetter() {
        generateNotice("_suspend");
        return "suspensionletter";
    }

    @SkipValidation
    public String getObjectionReason(final int reasonId) {
        return licenseUtils.getObjectionReasons().get(reasonId);
    }

    @SkipValidation
    public int getSize() {
        return objection.getActivities().size();
    }

    /*public DocumentManagerService<Notice> getDocumentManagerService() {
        return documentManagerService;
    }

    public void setDocumentManagerService(
            final DocumentManagerService<Notice> documentManagerService) {
        this.documentManagerService = documentManagerService;
    }*/

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(final String roleName) {
        this.roleName = roleName;
    }

    /**
     * TODO -- Fix me
     */
    public void generateNotice(final String noticetype) {
        /*if (documentManagerService.getDocumentObject(objection.getNumber() + noticetype, "egtradelicense") == null) {
            final Notice notice = new Notice();
            System.out.println(objection.getNumber() + noticetype);
            notice.setDocumentNumber(objection.getNumber() + noticetype);
            notice.setAssociatedObjectId(objection.getNumber());
            notice.setDomainName(EGOVThreadLocals.getDomainName());
            notice.setModuleName("egtradelicense");
            notice.setNoticeType(objection.getClass().getSimpleName() + noticetype);
            notice.setNoticeDate(new Date());
            request.put("noticeObject", notice);
        }*/
    }
}