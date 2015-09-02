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
package org.egov.tradelicense.domain.web.objection;

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
import org.egov.tradelicense.domain.entity.License;
import org.egov.tradelicense.domain.entity.LicenseStatusValues;
import org.egov.tradelicense.domain.entity.WorkflowBean;
import org.egov.tradelicense.domain.entity.objection.LicenseObjection;
import org.egov.tradelicense.domain.service.objection.ObjectionService;
import org.egov.tradelicense.utils.Constants;
import org.egov.tradelicense.utils.LicenseUtils;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPage;

import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

public class ObjectionAction extends BaseFormAction {
	
	private static final long serialVersionUID = 1L;
	private Long licenseId;
	protected WorkflowBean workflowBean = new WorkflowBean();
	protected LicenseUtils licenseUtils;
	private Map<Integer, String> objectionReasons;
	private LicenseStatusValues lsv;
	private LicenseObjection objection = new LicenseObjection();
	protected ObjectionService objectionService;
	private License license;
	private DocumentManagerService<Notice> documentManagerService;
	private String roleName;

	public LicenseStatusValues getLsv() {
		return this.lsv;
	}

	public void setLsv(LicenseStatusValues lsv) {
		this.lsv = lsv;
	}

	public Map<Integer, String> getObjectionReasons() {
		return this.licenseUtils.getObjectionReasons();
	}

	private List<String> activityTypeList;

	public List<String> getActivityTypeList() {
		return this.activityTypeList;
	}

	public void setActivityTypeList(List<String> activityTypeList) {
		this.activityTypeList = activityTypeList;
	}

	public LicenseUtils getLicenseUtils() {
		return this.licenseUtils;
	}

	public void setLicenseUtils(LicenseUtils licenseUtils) {
		this.licenseUtils = licenseUtils;
	}

	public WorkflowBean getWorkflowBean() {
		return this.workflowBean;
	}

	public void setWorkflowBean(WorkflowBean workflowBean) {
		this.workflowBean = workflowBean;
	}

	public Long getLicenseId() {
		return this.licenseId;
	}

	public void setLicenseId(Long licenseId) {
		this.licenseId = licenseId;
	}

	public ObjectionAction() {
		super();
	}

	public ObjectionService getObjectionService() {
		return this.objectionService;
	}

	public void setObjectionService(ObjectionService objectionService) {
		this.objectionService = objectionService;		
	}

	public License getLicense() {
		return this.license;
	}

	public void setLicense(License license) {
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

	public void setObjection(LicenseObjection objection) {
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
	@Validations(
			requiredFields = { @RequiredFieldValidator(
					fieldName = "name", message = "", key = Constants.REQUIRED), @RequiredFieldValidator(
					fieldName = "address", message = "", key = Constants.REQUIRED), @RequiredFieldValidator(
					fieldName = "objectionDate", message = "", key = Constants.REQUIRED), @RequiredFieldValidator(
					fieldName = "details", message = "", key = Constants.REQUIRED), @RequiredFieldValidator(
					fieldName = "reason", message = "", key = Constants.REQUIRED) })
	public String create() {
		this.objectionService.setContextName(ServletActionContext.getRequest().getContextPath());
		this.objection = this.objectionService.recordObjection(this.objection, this.licenseId, this.workflowBean);
		this.addActionMessage(this.getText("license.objection.succesful") + " " + this.objection.getNumber());
		return "message";
	}

	/**
	 * this will receive response or inspection details
	 * 
	 * @return
	 */
	public void prepareShowForApproval() {
		this.prepareNewForm();
		Integer userId = (Integer) session().get(Constants.SESSIONLOGINID);
		if (userId != null) {
			setRoleName(licenseUtils.getRolesForUserId(userId));
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
		final String type = this.objection.getActivities().get(objection.getActivities().size()-1).getType();
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
		} else if (type == null && this.workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONFORWARD)){
			this.addActionMessage(this.getText("license.forward.succesful"));
		} else if (type == null && this.workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONREJECT)){
			if (objection.getCurrentState().getValue().equals(State.END)) {
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
		return documentManagerService;
	}

	public void setDocumentManagerService(
			DocumentManagerService<Notice> documentManagerService) {
		this.documentManagerService = documentManagerService;
	}
	
	public String getRoleName() {
    	return roleName;
    }

	public void setRoleName(String roleName) {
    	this.roleName = roleName;
    }

	public void generateNotice(String noticetype) {
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
