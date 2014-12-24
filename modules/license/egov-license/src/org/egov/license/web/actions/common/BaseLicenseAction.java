/*
 * @(#)BaseLicenseAction.java 3.0, 29 Jul, 2013 1:24:27 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.license.web.actions.common;

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
import org.egov.infstr.auditing.model.AuditEntity;
import org.egov.infstr.auditing.model.AuditEvent;
import org.egov.infstr.auditing.model.AuditModule;
import org.egov.infstr.auditing.service.AuditEventService;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.commons.Module;
import org.egov.infstr.models.State;
import org.egov.infstr.utils.NumberToWord;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.ejb.api.UserService;
import org.egov.license.domain.entity.License;
import org.egov.license.domain.entity.LicenseDemand;
import org.egov.license.domain.entity.SubCategory;
import org.egov.license.domain.entity.WorkflowBean;
import org.egov.license.domain.service.BaseLicenseService;
import org.egov.license.utils.Constants;
import org.egov.license.utils.LicenseChecklistHelper;
import org.egov.license.utils.LicenseUtils;
import org.egov.pims.commons.Position;
import org.egov.pims.commons.service.EisCommonsService;
import org.egov.pims.service.EmployeeService;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPage;

@ParentPackage("egov")
@Results({ @Result(name = "collection", type = "redirect", location = "/integration/licenseBillCollect!renew.action", params = {}),
		@Result(name = "tl_editlicense", type = "redirect", location = "/newtradelicense/editTradeLicense!beforeEdit.action", params = {}),
		@Result(name = "tl_approve", type = "redirect", location = "/viewtradelicense/viewTradeLicense!showForApproval.action", params = {}),
		@Result(name = "tl_generateRejCertificate", type = "redirect", location = "/viewtradelicense/viewTradeLicense!generateRejCertificate.action", params = {}),
		@Result(name = "tl_generateCertificate", type = "redirect", location = "/viewtradelicense/viewTradeLicense!generateCertificate.action", params = {}),
		@Result(name = "tl_generateNoc", type = "redirect", location = "/viewtradelicense/viewTradeLicense!generateNoc.action", params = {}),
		@Result(name = "transfertl_editlicense", type = "redirect", location = "/transfer/transferTradeLicense!beforeEdit.action", params = {}),
		@Result(name = "transfertl_approve", type = "redirect", location = "/transfer/transferTradeLicense!showForApproval.action", params = {}),
		@Result(name = "hl_editlicense", type = "redirect", location = "/newhospitallicense/editHospitalLicense!beforeEdit.action", params = {}),
		@Result(name = "hl_approve", type = "redirect", location = "/viewhospitallicense/viewHospitalLicense!showForApproval.action", params = {}),
		@Result(name = "hl_generateCertificate", type = "redirect", location = "/viewhospitallicense/viewHospitalLicense!generateCertificate.action", params = {}),
		@Result(name = "hl_generateRejCertificate", type = "redirect", location = "/viewhospitallicense/viewHospitalLicense!generateRejCertificate.action", params = {}),
		@Result(name = "transferhl_editlicense", type = "redirect", location = "/transfer/transferHospitalLicense!beforeEdit.action", params = {}),
		@Result(name = "transferhl_approve", type = "redirect", location = "/transfer/transferHospitalLicense!showForApproval.action", params = {}),
		@Result(name = "hkr_editlicense", type = "redirect", location = "/newhawkerlicense/editHawkerLicense!beforeEdit.action", params = {}),
		@Result(name = "hkr_approve", type = "redirect", location = "/viewhawkerlicense/viewHawkerLicense!showForApproval.action", params = {}),
		@Result(name = "hkr_generateCertificate", type = "redirect", location = "/viewhawkerlicense/viewHawkerLicense!generateCertificate.action", params = {}),
		@Result(name = "transferhkr_editlicense", type = "redirect", location = "/transfer/transferHawkerlLicense!beforeEdit.action", params = {}),
		@Result(name = "transferhkr_approve", type = "redirect", location = "/transfer/transferHawkerLicense!showForApproval.action", params = {}),
		@Result(name = "hkr_generateRejCertificate", type = "redirect", location = "/viewhawkerlicense/viewHawkerLicense!generateRejCertificate.action", params = {}),
		@Result(name = "wwl_editlicense", type = "redirect", location = "/newwaterworkslicense/editWaterworksLicense!beforeEdit.action", params = {}),
		@Result(name = "wwl_approve", type = "redirect", location = "/viewwaterworkslicense/viewWaterworksLicense!showForApproval.action", params = {}),
		@Result(name = "wwl_generateCertificate", type = "redirect", location = "/viewwaterworkslicense/viewWaterworksLicense!generateCertificate.action", params = {}),
		@Result(name = "transferwwl_editlicense", type = "redirect", location = "/transfer/transferWaterworkslLicense!beforeEdit.action", params = {}),
		@Result(name = "transferwwl_approve", type = "redirect", location = "/transfer/transferWaterworksLicense!showForApproval.action", params = {}),
		@Result(name = "wwl_generateRejCertificate", type = "redirect", location = "/viewwaterworkslicense/viewWaterworksLicense!generateRejCertificate.action", params = {}),
		@Result(name = "vetl_editlicense", type = "redirect", location = "/newlicense/editVeterinaryLicense!beforeEdit.action", params = {}),
		@Result(name = "vetl_approve", type = "redirect", location = "/viewveterinarylicense/viewVeterinaryLicense!showForApproval.action", params = {}),
		@Result(name = "vetl_generateCertificate", type = "redirect", location = "/viewveterinarylicense/viewVeterinaryLicense!generateCertificate.action", params = {}),
		@Result(name = "transfervet_editlicense", type = "redirect", location = "/transfer/transferVeterinarylLicense!beforeEdit.action", params = {}),
		@Result(name = "transfervet_approve", type = "redirect", location = "/transfer/transferVeterinaryLicense!showForApproval.action", params = {}),
		@Result(name = "vetl_generateRejCertificate", type = "redirect", location = "/viewveterinarylicense/viewVeterinaryLicense!generateRejCertificate.action", params = {}),
		@Result(name = "pwd_approve", type = "redirect", location = "/viewpwdcontractorlicense/viewPwdContractorLicense!showForApproval.action", params = {}),
		@Result(name = "pwd_generateCertificate", type = "redirect", location = "/viewpwdcontractorlicense/viewPwdContractorLicense!generateCertificate.action", params = {}),
		@Result(name = "pwd_editlicense", type = "redirect", location = "/newpwdcontractorlicense/editPwdContractorLicense!beforeEdit.action", params = {}),
		@Result(name = "pwd_generateRejCertificate", type = "redirect", location = "/viewpwdcontractorlicense/viewPwdContractorLicense!generateRejCertificate.action", params = {}),
		@Result(name = "ele_approve", type = "redirect", location = "/viewelectricalcontractorlicense/viewElectricalContractorLicense!approveElectrical.action", params = {}),
		@Result(name = "ele_approveRenew", type = "redirect", location = "/viewelectricalcontractorlicense/viewElectricalContractorLicense!approveRenewElectrical.action", params = {}),
		@Result(name = "ele_generateCertificate", type = "redirect", location = "/viewelectricalcontractorlicense/viewElectricalContractorLicense!generateCertificate.action", params = {}),
		@Result(name = "ele_editlicense", type = "redirect", location = "/newelectricalcontractorlicense/editElectricalContractorLicense!beforeEdit.action", params = {}),
		@Result(name = "ele_generateRejCertificate", type = "redirect", location = "/viewelectricalcontractorlicense/viewElectricalContractorLicense!generateRejCertificate.action", params = {}) })
public abstract class BaseLicenseAction extends BaseFormAction {
	private static final long serialVersionUID = 1L;
	// sub classes should add setters for the following beans
	protected EisCommonsService eisCommonsService;
	protected EmployeeService employeeService;
	protected LicenseUtils licenseUtils;
	protected UserService userService;
	protected BoundaryDAO boundaryDAO;
	// sub class should add getter and setter this workflowBean
	protected WorkflowBean workflowBean = new WorkflowBean();
	protected List<String> buildingTypeList;
	protected List<String> genderList;
	protected List<String> selectedCheckList;
	protected List<LicenseChecklistHelper> checkList;
	protected String roleName;
	private AuditEventService auditEventService;

	protected abstract License license();

	protected abstract BaseLicenseService service();

	public BaseLicenseAction() {
		this.addRelatedEntity("boundary", Boundary.class);
		this.addRelatedEntity("licensee.boundary", Boundary.class);
		this.addRelatedEntity("tradeName", SubCategory.class);
	}

	// sub class should get the object of the model and set to license()
	@SkipValidation
	public String approve() {
		this.processWorkflow(NEW);
		return "message";
	}

	@SkipValidation
	public String approveRenew() {
		this.processWorkflow("Renew");
		return "message";
	}

	@ValidationErrorPage(Constants.NEW)
	public String create() {
		try {
			this.setCheckList();
			this.service().create(this.license());
			this.initiateWorkFlowForLicense();
			this.persistenceService.getSession().flush();
			this.addActionMessage(this.getText("license.submission.succesful") + this.license().getApplicationNumber());
		} catch (final RuntimeException e) {
			this.clearMessages();
			this.loadAjaxedDropDowns();
			addActionError("Internal error occurred");
			LOG.error("Error occurred while creating License", e);
			throw e;
		}
		return Constants.ACKNOWLEDGEMENT;
	}

	public String createDraftItems() {
		try {
			this.setCheckList();
			this.service().create(this.license());
			this.initiateWorkFlowForLicenseDraft();
			this.persistenceService.getSession().flush();
			this.addActionMessage(this.getText("license.submission.succesful") + this.license().getApplicationNumber());
		} catch (final RuntimeException e) {
			this.loadAjaxedDropDowns();
			throw e;
		}
		return Constants.ACKNOWLEDGEMENT;
	}

	@SkipValidation
	public String beforeRenew() {

		return "beforeRenew";
	}

	@SkipValidation
	public String renew() {

		this.getSession().put("model.id", this.license().getId());
		try {
			this.service().renew(this.license());
			this.initiateWorkFlowForRenewLicense();
			this.addActionMessage(this.getText("license.renew.submission.succesful") + this.license().getLicenseNumber());
		} catch (final RuntimeException e) {
			this.loadAjaxedDropDowns();
			throw e;
		}
		return Constants.ACKNOWLEDGEMENT_RENEW;
	}

	public void setCheckList() {
		String str = "";
		if (this.selectedCheckList != null) {
			for (final Object obj : this.selectedCheckList) {
				if ((this.selectedCheckList.size() > 1) && !this.selectedCheckList.get(this.selectedCheckList.size() - 1).equals(obj.toString())) {
					str = str.concat(obj.toString()).concat("^");
				} else {
					str = str.concat(obj.toString());
				}
				this.license().setLicenseCheckList(str);
			}
		}
	}

	@ValidationErrorPage(Constants.NEW)
	public String enterExisting() {
		this.service().enterExistingLicense(this.license());
		this.service().updateLicenseForFinalApproval(this.license());
		this.addActionMessage(this.getText("license.entry.succesful") + "  " + this.license().getLicenseNumber());
		this.license().getTradeName().getLicenseType().getModule();
		// TODO to be moved
		/*
		 * if ((module.getModuleName().equals(Constants.PWDLICENSE_MODULENAME))) { this.licenseUtils.updateContractorForDepartment(license(), Constants.LICENSE_STATUS_UPDATE, Constants.LICENSE_STATUS_ACTIVE); }
		 */
		return "viewlicense";
	}

	@SkipValidation
	public String enterExistingForm() {
		this.license().setBuildingType(Constants.BUILDINGTYPE_OWN_BUILDING);
		this.license().getLicensee().setGender(Constants.GENDER_MALE);
		return Constants.NEW;
	}

	public void prepareEnterExistingForm() {
		this.prepareNewForm();
	}

	// create workflow and pushes to drafts
	public void initiateWorkFlowForLicense() {
		this.service().initiateWorkFlowForLicense(this.license(), this.workflowBean);
		final Position position = this.eisCommonsService.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
		if (position != null) {
			this.addActionMessage(this.getText("license.saved.in.inbox"));
		}
	}

	public void endWorkFlowForLicense() {
		this.service().endWorkFlowForLicense(this.license());
	}

	// create workflow and pushes to drafts
	public void initiateWorkFlowForLicenseDraft() {
		this.service().initiateWorkFlowForLicenseDraft(this.license(), this.workflowBean);
		this.addActionMessage(this.getText("license.saved.in.draft"));
	}

	public void initiateWorkFlowForRenewLicense() {
		this.service().initiateWorkFlowForRenewLicense(this.license(), this.workflowBean);
		this.addActionMessage(this.getText("license.saved.in.inbox"));

	}

	public List<String> getBuildingTypeList() {
		return this.buildingTypeList;
	}

	public List<String> getGenderList() {
		return this.genderList;
	}

	@Override
	public Object getModel() {
		return this.license();
	}

	@SkipValidation
	public String newForm() {
		this.license().setBuildingType(Constants.BUILDINGTYPE_OWN_BUILDING);
		this.license().getLicensee().setGender(Constants.GENDER_MALE);
		return Constants.NEW;
	}

	public void prepareCreate() {
		this.prepareNewForm();
	}

	public void prepareNewForm() {
		super.prepare();
		this.buildingTypeList = new ArrayList<String>();
		this.buildingTypeList.add(Constants.BUILDINGTYPE_OWN_BUILDING);
		this.buildingTypeList.add(Constants.BUILDINGTYPE_RENTAL_AGREEMANT);
		this.genderList = new ArrayList<String>();
		this.genderList.add(Constants.GENDER_MALE);
		this.genderList.add(Constants.GENDER_FEMALE);
		this.addDropdownData(Constants.DROPDOWN_AREA_LIST_LICENSE, Collections.EMPTY_LIST);
		this.addDropdownData(Constants.DROPDOWN_AREA_LIST_LICENSEE, Collections.EMPTY_LIST);
		this.addDropdownData(Constants.DROPDOWN_DIVISION_LIST_LICENSE, Collections.EMPTY_LIST);
		this.addDropdownData(Constants.DROPDOWN_DIVISION_LIST_LICENSEE, Collections.EMPTY_LIST);
		this.addDropdownData(Constants.DROPDOWN_ZONE_LIST, this.licenseUtils.getAllZone());
		if (this.getModel().getClass().getSimpleName().equalsIgnoreCase(Constants.ELECTRICALLICENSE_LICENSETYPE)) {
			this.addDropdownData(Constants.DROPDOWN_TRADENAME_LIST, Collections.EMPTY_LIST);
		} else {
			this.addDropdownData(Constants.DROPDOWN_TRADENAME_LIST, this.licenseUtils.getAllTradeNames(this.getModel().getClass().getSimpleName()));
		}

		this.setupWorkflowDetails();
	}

	public void prepareShowForApproval() {
		this.prepareNewForm();
	}

	/**
	 * should be called from the second level only Approve will not end workflow instead it sends to the creator in approved state
	 */
	public void processWorkflow(final String processType) {
		final Module module = this.license().getTradeName().getLicenseType().getModule();
		if (processType.equalsIgnoreCase(NEW)) {
			this.service().processWorkflowForLicense(this.license(), this.workflowBean);
			if ((module.getModuleName().equals(Constants.PWDLICENSE_MODULENAME))) {
				if (this.workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONAPPROVE)) {
					// TODO to be moved
					/*
					 * if (module.getModuleName().equals(Constants.PWDLICENSE_MODULENAME) && license().getIsUpgrade() != null && license().getIsUpgrade().equals('Y') && license().getTempLicenseNumber() != null) { this.licenseUtils.updateContractorForDepartment(this.license(),
					 * Constants.LICENSE_UPGRADE, Constants.LICENSE_STATUS_ACTIVE); } else { this.licenseUtils.createContractorForDepartment(this.license(), Constants.LICENSE_STATUS_ACTIVE); }
					 */
				}
			}
		} else if (processType.equalsIgnoreCase("Renew")) {
			this.service().processWorkflowForRenewLicense(this.license(), this.workflowBean);
			// TODO to be moved
			/*
			 * if ((module.getModuleName().equals(Constants.PWDLICENSE_MODULENAME))) { this.licenseUtils.updateContractorForDepartment(license(), Constants.WORKS_UPDATE_TYPE_RENEW, Constants.LICENSE_STATUS_ACTIVE); }
			 */
		}
		User userByID = null;
		for (final State state : this.license().getState().getHistory()) {
			if (state != null && state.getCreatedBy() != null) {
				if (state.getValue().equals(Constants.WORKFLOW_STATE_NEW)) {
					userByID = this.userService.getUserByID(state.getCreatedBy().getId());
					break;
				}
			}
		}
		if (userByID == null) {
			userByID = this.userService.getUserByID(this.license().getCreatedBy().getId());
		}
		if (this.workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONAPPROVE)) {
			if (this.license().getTradeName().isNocApplicable() != null && this.license().getTradeName().isNocApplicable()) {
				this.addActionMessage(this.getText("license.approved.and.sent.to") + userByID.getUserName() + " " + this.getText("license.for.noc.generation"));
			} else {
				this.addActionMessage(this.getText("license.approved.and.sent.to") + userByID.getUserName() + " " + this.getText("license.for.certificate.generation"));
			}
			doAuditing(getAuditModuleFromLicModule(module.getModuleName()), getAuditEntityFromModule(module.getModuleName()), AuditEvent.CREATED, this.license().getAuditDetails());

		} else if (this.workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONFORWARD)) {
			final User userBy = this.userService.getUserByID(this.workflowBean.getApproverUserId());
			this.addActionMessage(this.getText("license.sent") + " " + userBy.getUserName());
		} else if (this.workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONREJECT)) {
			if (this.license().getState().getPrevious().getValue().contains(Constants.WORKFLOW_STATE_REJECTED)) {
				this.addActionMessage(this.getText("license.rejectedfirst") + userByID.getUserName() + " " + this.getText("license.rejectedlast"));
			} else {
				this.addActionMessage(this.getText("license.rejected") + userByID.getUserName());
			}
		}

		else if (this.workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONGENERATEDCERTIFICATE)) {
			this.addActionMessage(this.getText("license.certifiacte.print.complete.recorded"));
		} else if (this.workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONPRINTCOMPLETED)) {
			this.addActionMessage(this.getText("license.rejection.certifiacte.print.complete.recorded"));
		}
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
		return this.selectedCheckList;
	}

	public void setSelectedCheckList(final List<String> selectedCheckList) {
		this.selectedCheckList = selectedCheckList;
	}

	public List<LicenseChecklistHelper> getCheckList() {
		return this.checkList;
	}

	public void setCheckList(final List<LicenseChecklistHelper> checkList) {
		this.checkList = checkList;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(final String roleName) {
		this.roleName = roleName;
	}

	// sub class should get the object of the model and set to license()
	// /use contains() every where in this api
	// viewTradeLicense!showForApproval is picking id and gets Objects and forwards here
	@SkipValidation
	public String showForApproval() {
		this.getSession().put("model.id", this.license().getId());
		String result = "approve";
		final Integer userId = (Integer) session().get(Constants.SESSIONLOGINID);
		if (userId != null) {
			setRoleName(this.licenseUtils.getRolesForUserId(userId));
		}
		if (this.license().getState().getType().equalsIgnoreCase("TradeLicense")) {
			if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_TYPE_CREATENEWLICENSE)) {
				if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATECERTIFICATE)) {
					result = "tl_generateCertificate";
				} else if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_REJECTED)) {
					result = "tl_editlicense";
				} else if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATEREJCERTIFICATE)) {
					result = "tl_generateRejCertificate";
				} else if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATENOC)) {
					result = "tl_generateNoc";
				} else {
					result = "approve";
				}

			}

			else if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_TYPE_RENEWLICENSE)) {
				if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATECERTIFICATE)) {
					result = "tl_generateCertificate";
				} else if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_REJECTED)) {
					result = "approveRenew";
				} else if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATEREJCERTIFICATE)) {
					result = "tl_generateRejCertificate";
				} else {
					result = "approveRenew";
				}

			}

			else {
				if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_TYPE_TRANSFERLICENSE)) {
					if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATECERTIFICATE)) {
						result = "tl_generateCertificate";
					} else if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_REJECTED)) {
						result = "transfertl_editlicense";
					} else {
						result = "transfertl_approve";
					}
				}
			}
		}
		if (this.license().getState().getType().equalsIgnoreCase("HospitalLicense")) {
			if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_TYPE_CREATENEWLICENSE)) {
				if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATECERTIFICATE)) {
					result = "hl_generateCertificate";
				} else if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_REJECTED)) {
					result = "hl_editlicense";
				} else if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATEREJCERTIFICATE)) {
					result = "hl_generateRejCertificate";
				} else {
					result = "approve";
				}
			}

			else if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_TYPE_RENEWLICENSE)) {
				if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATECERTIFICATE)) {
					result = "hl_generateCertificate";
				} else if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_REJECTED)) {
					result = "approveRenew";
				} else if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATEREJCERTIFICATE)) {
					result = "hl_generateRejCertificate";
				} else {
					result = "approveRenew";
				}

			}

			else {
				if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_TYPE_TRANSFERLICENSE)) {
					if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATECERTIFICATE)) {
						result = "hl_generateCertificate";
					} else if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_REJECTED)) {
						result = "transferhl_editlicense";
					} else {
						result = "transferhl_approve";
					}
				}
			}
		}
		if (this.license().getState().getType().equalsIgnoreCase(Constants.HAWKERLICENSE)) {
			if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_TYPE_CREATENEWLICENSE)) {
				if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATECERTIFICATE)) {
					result = "hkr_generateCertificate";
				} else if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_REJECTED)) {
					result = "hkr_editlicense";
				} else if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATEREJCERTIFICATE)) {
					result = "hkr_generateRejCertificate";
				} else {
					result = "approve";
				}
			} else if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_TYPE_RENEWLICENSE)) {
				if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATECERTIFICATE)) {
					result = "hkr_generateCertificate";
				} else if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_REJECTED)) {
					result = "approveRenew";
				} else if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATEREJCERTIFICATE)) {
					result = "hkr_generateRejCertificate";
				} else {
					result = "approveRenew";
				}

			} else {
				if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_TYPE_TRANSFERLICENSE)) {
					if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATECERTIFICATE)) {
						result = "hkr_generateCertificate";
					} else if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_REJECTED)) {
						result = "transferhkr_editlicense";
					} else {
						result = "transferhkr_approve";
					}
				}
			}
		}
		if (this.license().getState().getType().equalsIgnoreCase(Constants.WATERWORKSLICENSE)) {
			if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_TYPE_CREATENEWLICENSE)) {
				if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATECERTIFICATE)) {
					result = "wwl_generateCertificate";
				} else if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_REJECTED)) {
					result = "wwl_editlicense";
				} else if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATEREJCERTIFICATE)) {
					result = "wwl_generateRejCertificate";
				} else {
					result = "approve";
				}
			} else if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_TYPE_RENEWLICENSE)) {
				if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATECERTIFICATE)) {
					result = "wwl_generateCertificate";
				} else if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_REJECTED)) {
					result = "approveRenew";
				} else if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATEREJCERTIFICATE)) {
					result = "wwl_generateRejCertificate";
				} else {
					result = "approveRenew";
				}

			}

			else {
				if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_TYPE_TRANSFERLICENSE)) {
					if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATECERTIFICATE)) {
						result = "wwl_generateCertificate";
					} else if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_REJECTED)) {
						result = "transferww_editlicense";
					} else {
						result = "transferww_approve";
					}
				}
			}
		}

		if (this.license().getState().getType().equalsIgnoreCase(Constants.VETERINARYLICENSE)) {
			if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_TYPE_CREATENEWLICENSE)) {
				if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATECERTIFICATE)) {
					result = "vetl_generateCertificate";
				} else if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_REJECTED)) {
					result = "vetl_editlicense";
				} else if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATEREJCERTIFICATE)) {
					result = "vetl_generateRejCertificate";
				} else {
					result = "approve";
				}
			} else if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_TYPE_RENEWLICENSE)) {
				if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATECERTIFICATE)) {
					result = "vetl_generateCertificate";
				} else if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_REJECTED)) {
					result = "approveRenew";
				} else if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATEREJCERTIFICATE)) {
					result = "vetl_generateRejCertificate";
				} else {
					result = "approveRenew";
				}

			} else {
				if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_TYPE_TRANSFERLICENSE)) {
					if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATECERTIFICATE)) {
						result = "vetl_generateCertificate";
					} else if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_REJECTED)) {
						result = "transfervet_editlicense";
					} else {
						result = "transfervet_approve";
					}
				}
			}
		}
		if (this.license().getState().getType().equalsIgnoreCase(Constants.PWDCONTRACTORLICENSE)) {
			if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_TYPE_CREATENEWLICENSE)) {
				if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATECERTIFICATE)) {
					result = "pwd_generateCertificate";
				} else if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_REJECTED)) {
					result = "pwd_editlicense";
				} else if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATEREJCERTIFICATE)) {
					result = "pwd_generateRejCertificate";
				} else {
					result = "approve";
				}
			} else if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_TYPE_RENEWLICENSE)) {
				if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATECERTIFICATE)) {
					result = "pwd_generateCertificate";
				} else if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_REJECTED)) {
					result = "approveRenew";
				} else if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATEREJCERTIFICATE)) {
					result = "pwd_generateRejCertificate";
				} else {
					result = "approveRenew";
				}

			}
		}
		if (this.license().getState().getType().equalsIgnoreCase(Constants.ELECTRICALCONTRACTORLICENSE)) {
			if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_TYPE_CREATENEWLICENSE)) {
				if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATECERTIFICATE)) {
					result = "ele_generateCertificate";
				} else if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_REJECTED)) {
					result = "ele_editlicense";
				} else if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATEREJCERTIFICATE)) {
					result = "ele_generateRejCertificate";
				} else {
					result = "ele_approve";
				}
			} else if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_TYPE_RENEWLICENSE)) {
				if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATECERTIFICATE)) {
					result = "ele_generateCertificate";
				} else if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_REJECTED)) {
					result = "ele_approveRenew";
				} else if (this.license().getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATEREJCERTIFICATE)) {
					result = "ele_generateRejCertificate";
				} else {
					result = "ele_approveRenew";
				}
			} else if (this.license().getState().getValue().equals(Constants.WORKFLOW_STATE_NEW)) {
				result = "ele_editlicense";
			}
		}
		return result;
	}

	public void loadAjaxedDropDowns() {
		final CommonAjaxAction comm = new CommonAjaxAction();
		comm.setEmployeeService(this.employeeService);
		comm.setBoundaryDAO(this.boundaryDAO);
		comm.setLicenseUtils(this.licenseUtils);
		// if the zone is loaded from ui which have trigger load division
		// set those list
		// else is not required since empty lists are set in prepare itself
		if (this.license().getLicenseZoneId() != null) {
			comm.setZoneId(this.license().getLicenseZoneId().intValue());
			comm.populateDivisions();
			this.addDropdownData(Constants.DROPDOWN_DIVISION_LIST_LICENSE, comm.getDivisionList());
		}
		if (this.license().getLicenseeZoneId() != null) {
			comm.setZoneId(this.license().getLicenseeZoneId().intValue());
			comm.populateDivisions();
			this.addDropdownData(Constants.DROPDOWN_DIVISION_LIST_LICENSEE, comm.getDivisionList());
		}
		if (this.workflowBean.getDepartmentId() != null) {
			comm.setDepartmentId(this.workflowBean.getDepartmentId());
			comm.ajaxPopulateDesignationsByDept();
			this.workflowBean.setDesignationList(comm.getDesignationList());
		}
		if (this.workflowBean.getDesignationId() != null) {
			comm.setDesignationId(this.workflowBean.getDesignationId());
			comm.ajaxPopulateUsersByDesignation();
			this.workflowBean.setAppoverUserList(comm.getAllActiveUsersByGivenDesg());
		}
	}

	public void setupWorkflowDetails() {
		this.workflowBean.setDepartmentList(this.licenseUtils.getAllDepartments());
		this.workflowBean.setDesignationList(Collections.EMPTY_LIST);
		this.workflowBean.setAppoverUserList(Collections.EMPTY_LIST);
	}

	// this will give current Demand only while saving or immediate after create
	public LicenseDemand getCurrentYearDemand() {
		return this.license().getLicenseDemand();
	}

	// this will give current Demand only while saving or immediate after create
	public String getPayableAmountInWords() {
		String baseDemand = "";

		baseDemand = NumberToWord.amountInWords((getAapplicableDemand(this.license().getCurrentDemand()).doubleValue()));

		return baseDemand;
	}

	public String getCollectedDemandAmountInWords() {
		// this below api will give you the current year Demand from database
		final LicenseDemand currentYearDemand = this.service().getCurrentYearDemand(this.license());
		return NumberToWord.amountInWords(currentYearDemand.getAmtCollected().doubleValue());
	}

	public List<LicenseChecklistHelper> getSelectedChecklist() {
		List<LicenseChecklistHelper> checkList = new ArrayList<LicenseChecklistHelper>();
		checkList = this.service().getLicenseChecklist(this.license());
		return checkList;
	}

	public void setBoundaryDAO(final BoundaryDAO boundaryDAO) {
		this.boundaryDAO = boundaryDAO;
	}

	public boolean isCurrent(final EgDemandDetails dd) {
		boolean isCurrent = false;
		final Installment currInstallment = LicenseUtils.getCurrInstallment(dd.getEgDemandReason().getEgDemandReasonMaster().getEgModule());
		if (currInstallment.getId().intValue() == dd.getEgDemandReason().getEgInstallmentMaster().getId().intValue()) {
			isCurrent = true;
		}
		return isCurrent;

	}

	public BigDecimal getAapplicableDemand(final EgDemand demand) {
		BigDecimal total = BigDecimal.ZERO;
		if (demand.getIsHistory().equals("N")) {
			for (final EgDemandDetails details : demand.getEgDemandDetails()) {
				total = total.add(details.getAmount());
				total = total.subtract(details.getAmtRebate());
			}
		}
		return total;
	}

	public boolean isCitizen() {
		return this.eisCommonsService.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId())) == null;
	}

	public void setAuditEventService(final AuditEventService auditEventService) {
		this.auditEventService = auditEventService;
	}

	public AuditEntity getAuditEntityFromModule(final String module) {
		AuditEntity entity = null;
		if ((module.equals(Constants.ELECTRICALLICENSE_MODULENAME))) {
			entity = AuditEntity.ECL_LIC;
		} else if (module.equals(Constants.HOSPITALLICENSE_MODULENAME)) {
			entity = AuditEntity.HPL_LIC;
		} else if (module.equals(Constants.HAWKERLICENSE_MODULENAME)) {
			entity = AuditEntity.HWKL_LIC;
		} else if (module.equals(Constants.TRADELICENSE_MODULENAME)) {
			entity = AuditEntity.TL_LIC;
		} else if (module.equals(Constants.PWDLICENSE_MODULENAME)) {
			entity = AuditEntity.PWDL_LIC;
		} else if (module.equals(Constants.VETLICENSE_MODULENAME)) {
			entity = AuditEntity.VETL_LIC;
		} else if (module.equals(Constants.WATERWORKSLICENSE_MODULENAME)) {
			entity = AuditEntity.WWL_LIC;
		}

		return entity;
	}

	public AuditModule getAuditModuleFromLicModule(final String module) {
		AuditModule auditModule = null;
		if ((module.equals(Constants.ELECTRICALLICENSE_MODULENAME))) {
			auditModule = AuditModule.ECL;
		} else if (module.equals(Constants.HOSPITALLICENSE_MODULENAME)) {
			auditModule = AuditModule.HPL;
		} else if (module.equals(Constants.HAWKERLICENSE_MODULENAME)) {
			auditModule = AuditModule.HWKL;
		} else if (module.equals(Constants.TRADELICENSE_MODULENAME)) {
			auditModule = AuditModule.TL;
		} else if (module.equals(Constants.PWDLICENSE_MODULENAME)) {
			auditModule = AuditModule.PWDL;
		} else if (module.equals(Constants.VETLICENSE_MODULENAME)) {
			auditModule = AuditModule.VETL;
		} else if (module.equals(Constants.WATERWORKSLICENSE_MODULENAME)) {
			auditModule = AuditModule.WWL;
		}

		return auditModule;
	}

	protected void doAuditing(final AuditModule auditModule, final AuditEntity auditEntity, final String action, final String details) {
		final License license = (License) this.getModel();
		final AuditEvent auditEvent = new AuditEvent(auditModule, auditEntity, action, this.license().getLicenseNumber(), details);
		auditEvent.setPkId(license.getId());
		auditEvent.setDetails2(this.workflowBean.getActionName());
		this.auditEventService.createAuditEvent(auditEvent, this.license().getClass());
	}

	@SkipValidation
	public String auditReport() {
		return "auditReport";
	}
}
