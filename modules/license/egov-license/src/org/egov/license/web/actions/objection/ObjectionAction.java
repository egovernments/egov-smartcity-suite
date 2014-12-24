/*
 * @(#)ObjectionAction.java 3.0, 29 Jul, 2013 1:24:26 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.license.web.actions.objection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.docmgmt.DocumentManagerService;
import org.egov.infstr.docmgmt.documents.Notice;
import org.egov.infstr.models.State;
import org.egov.license.domain.entity.License;
import org.egov.license.domain.entity.LicenseStatusValues;
import org.egov.license.domain.entity.WorkflowBean;
import org.egov.license.domain.entity.objection.LicenseObjection;
import org.egov.license.domain.service.objection.ObjectionService;
import org.egov.license.utils.Constants;
import org.egov.license.utils.LicenseUtils;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPage;

import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

public class ObjectionAction extends BaseFormAction {

	private static final long serialVersionUID = 1L;
	private Long licenseId;
	protected WorkflowBean workflowBean = new WorkflowBean();
	protected LicenseUtils licenseUtils;
	private LicenseStatusValues lsv;
	private LicenseObjection objection = new LicenseObjection();
	protected ObjectionService objectionService;
	private License license;
	private DocumentManagerService<Notice> documentManagerService;
	private String roleName;

	public LicenseStatusValues getLsv() {
		return this.lsv;
	}

	public void setLsv(final LicenseStatusValues lsv) {
		this.lsv = lsv;
	}

	public Map<Integer, String> getObjectionReasons() {
		return this.licenseUtils.getObjectionReasons();
	}

	private List<String> activityTypeList;

	public List<String> getActivityTypeList() {
		return this.activityTypeList;
	}

	public void setActivityTypeList(final List<String> activityTypeList) {
		this.activityTypeList = activityTypeList;
	}

	public LicenseUtils getLicenseUtils() {
		return this.licenseUtils;
	}

	public void setLicenseUtils(final LicenseUtils licenseUtils) {
		this.licenseUtils = licenseUtils;
	}

	public WorkflowBean getWorkflowBean() {
		return this.workflowBean;
	}

	public void setWorkflowBean(final WorkflowBean workflowBean) {
		this.workflowBean = workflowBean;
	}

	public Long getLicenseId() {
		return this.licenseId;
	}

	public void setLicenseId(final Long licenseId) {
		this.licenseId = licenseId;
	}

	public ObjectionAction() {
		super();
	}

	public ObjectionService getObjectionService() {
		return this.objectionService;
	}

	public void setObjectionService(final ObjectionService objectionService) {
		this.objectionService = objectionService;
	}

	public License getLicense() {
		return this.license;
	}

	public void setLicense(final License license) {
		this.license = license;
	}

	@Override
	public Object getModel() {
		return this.objection;
	}

	public void setupWorkflowDetails() {
		this.workflowBean.setDepartmentList(this.licenseUtils.getAllDepartments());
		this.workflowBean.setDesignationList(Collections.EMPTY_LIST);
		this.workflowBean.setAppoverUserList(Collections.EMPTY_LIST);
	}

	public LicenseObjection getObjection() {
		return this.objection;
	}

	public void setObjection(final LicenseObjection objection) {
		this.objection = objection;
	}

	public void prepareNewForm() {
		this.setupWorkflowDetails();
	}

	@SkipValidation
	public String newForm() {
		this.license = (License) this.persistenceService.find("from License where id=?", this.licenseId);
		this.objection.setLicense(this.license);
		return Constants.NEW;
	}

	@ValidationErrorPage(Constants.NEW)
	@Validations(requiredFields = { @RequiredFieldValidator(fieldName = "name", message = "", key = Constants.REQUIRED), @RequiredFieldValidator(fieldName = "address", message = "", key = Constants.REQUIRED),
			@RequiredFieldValidator(fieldName = "objectionDate", message = "", key = Constants.REQUIRED), @RequiredFieldValidator(fieldName = "details", message = "", key = Constants.REQUIRED),
			@RequiredFieldValidator(fieldName = "reason", message = "", key = Constants.REQUIRED) })
	public String create() {
		this.objectionService.setContextName(ServletActionContext.getRequest().getContextPath());
		this.objection = this.objectionService.recordObjection(this.objection, this.licenseId, this.workflowBean);
		this.addActionMessage(this.getText("license.objection.succesful") + " " + this.objection.getNumber());
		return "message";
	}

	/**
	 * this will receive response or inspection details
	 * @return
	 */
	public void prepareShowForApproval() {
		this.prepareNewForm();
		final Integer userId = (Integer) session().get(Constants.SESSIONLOGINID);
		if (userId != null) {
			setRoleName(this.licenseUtils.getRolesForUserId(userId));
		}
		this.activityTypeList = new ArrayList<String>();
		this.activityTypeList.add(Constants.ACTIVITY_INSPECTION);
		this.activityTypeList.add(Constants.ACTIVITY_RESPONSE);
	}

	@SkipValidation
	public String showForApproval() {
		this.objectionService.setContextName(ServletActionContext.getRequest().getContextPath());
		this.objection = this.objectionService.findByNamedQuery(LicenseObjection.BY_ID, this.objection.getId());
		Collections.reverse(this.objection.getActivities());
		if (this.objection.getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATECANCELLATIONLETTER)) {
			return this.generateRejCertificate();
		} else if (this.objection.getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATESUSPENSIONLETTER)) {
			return this.generateSusLetter();
		} else {
			return "approve";
		}
	}

	@SkipValidation
	public String approve() {
		final String type = this.objection.getActivities().get(this.objection.getActivities().size() - 1).getType();
		this.objectionService.setContextName(ServletActionContext.getRequest().getContextPath());
		this.objection = this.objectionService.recordResponseOrInspection(this.objection, this.workflowBean);
		if (("Inspection").equalsIgnoreCase(type)) {
			this.addActionMessage(this.getText("license.inspection.succesful"));
		} else if (("Response").equalsIgnoreCase(type)) {
			this.addActionMessage(this.getText("license.response.succesful"));
		} else if (("PreNotice").equalsIgnoreCase(type)) {
			this.addActionMessage(this.getText("license.preliminarynotice.succesful"));
		} else if (("SCNotice").equalsIgnoreCase(type)) {
			this.addActionMessage(this.getText("license.showcausenotice.succesful"));
		} else if (("suspend").equalsIgnoreCase(type)) {
			this.addActionMessage(this.getText("license.suspend.succesful"));
		} else if (("cancelled").equalsIgnoreCase(type)) {
			this.addActionMessage(this.getText("license.cancelled.succesful"));
		} else if (type == null && this.workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONFORWARD)) {
			this.addActionMessage(this.getText("license.forward.succesful"));
		} else if (type == null && this.workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONREJECT)) {
			if (this.objection.getCurrentState().getValue().equals(State.END)) {
				this.addActionMessage(this.getText("license.rejection.end"));
			} else {
				this.addActionMessage(this.getText("license.rejected.succesful"));
			}
		} else if (this.workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONAPPROVE)) {
			this.addActionMessage(this.getText("license.objection.approved"));
		}
		return "message";

	}

	@SkipValidation
	public String preNotice() {
		this.objection = this.objectionService.findByNamedQuery(LicenseObjection.BY_ID, this.objection.getId());
		return "prenotice";
	}

	@SkipValidation
	public String preliminaryNotice() {
		this.objection = this.objectionService.findByNamedQuery(LicenseObjection.BY_ID, this.objection.getId());
		generateNotice("_PreNotice");
		return "prenoticeletter";
	}

	@SkipValidation
	public String scNotice() {
		this.objection = this.objectionService.findByNamedQuery(LicenseObjection.BY_ID, this.objection.getId());
		return "showcausenotice";
	}

	@SkipValidation
	public String showCauseNotice() {
		this.objection = this.objectionService.findByNamedQuery(LicenseObjection.BY_ID, this.objection.getId());
		generateNotice("_SCNotice");
		return "scnoticeletter";
	}

	@SkipValidation
	public String generateRejCertificate() {
		this.lsv = (LicenseStatusValues) this.persistenceService.find("from LicenseStatusValues where license.id=?", this.objection.getLicense().getId());
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
		return this.licenseUtils.getObjectionReasons().get(reasonId);
	}

	@SkipValidation
	public int getSize() {
		return this.objection.getActivities().size();
	}

	public DocumentManagerService<Notice> getDocumentManagerService() {
		return this.documentManagerService;
	}

	public void setDocumentManagerService(final DocumentManagerService<Notice> documentManagerService) {
		this.documentManagerService = documentManagerService;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(final String roleName) {
		this.roleName = roleName;
	}

	public void generateNotice(final String noticetype) {
		if (this.documentManagerService.getDocumentObject(this.objection.getNumber() + noticetype, "egtradelicense") == null) {
			final Notice notice = new Notice();
			System.out.println(this.objection.getNumber() + noticetype);
			notice.setDocumentNumber(this.objection.getNumber() + noticetype);
			notice.setAssociatedObjectId(this.objection.getNumber());
			notice.setDomainName(EGOVThreadLocals.getDomainName());
			notice.setModuleName("egtradelicense");
			notice.setNoticeType(this.objection.getClass().getSimpleName() + noticetype);
			notice.setNoticeDate(new Date());
			this.request.put("noticeObject", notice);
		}
	}
}
