/*
 * @(#)TransferTradeLicenseAction.java 3.0, 25 Jul, 2013 4:57:53 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.license.trade.web.actions.transfer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.auditing.model.AuditEntity;
import org.egov.infstr.auditing.model.AuditModule;
import org.egov.infstr.workflow.WorkflowService;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.ejb.api.UserService;
import org.egov.license.domain.entity.License;
import org.egov.license.domain.entity.Licensee;
import org.egov.license.domain.entity.WorkflowBean;
import org.egov.license.domain.entity.transfer.LicenseTransfer;
import org.egov.license.domain.service.BaseLicenseService;
import org.egov.license.trade.domain.entity.TradeLicense;
import org.egov.license.trade.domain.service.TradeService;
import org.egov.license.utils.Constants;
import org.egov.license.web.actions.common.BaseLicenseAction;
import org.egov.web.annotation.ValidationErrorPage;

import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import com.opensymphony.xwork2.validator.annotations.IntRangeFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

@ParentPackage("egov")
@Validations(requiredFields = { @RequiredFieldValidator(fieldName = "licenseTransfer.oldApplicantName", message = "", key = Constants.REQUIRED), @RequiredFieldValidator(fieldName = "licenseTransfer.oldApplicationDate", message = "", key = Constants.REQUIRED),
		@RequiredFieldValidator(fieldName = "licenseTransfer.oldNameOfEstablishment", message = "", key = Constants.REQUIRED), @RequiredFieldValidator(fieldName = "licenseTransfer.oldAddress.houseNo", message = "", key = Constants.REQUIRED),
		@RequiredFieldValidator(fieldName = "licenseeZoneId", message = "", key = Constants.REQUIRED) },

emails = { @EmailValidator(message = "Please enter the valid Email Id", fieldName = "licenseTransfer.oldEmailId", key = "Please enter the valid Email Id") }, stringLengthFields = {
		@StringLengthFieldValidator(fieldName = "licenseTransfer.oldNameOfEstablishment", maxLength = "100", message = "", key = "Name of Establishment can be upto 100 characters"),
		@StringLengthFieldValidator(fieldName = "licenseTransfer.oldApplicantName", maxLength = "100", message = "", key = "Applicant Name can be upto 100 characters"),
		@StringLengthFieldValidator(fieldName = "licenseTransfer.oldAddress.houseNo", maxLength = "10", message = "", key = "Maximum  length for house number is 10"),
		@StringLengthFieldValidator(fieldName = "licenseTransfer.oldAddress.streetAddress2", maxLength = "10", message = "", key = "Maximum  length for house number is 10"),
		@StringLengthFieldValidator(fieldName = "licenseTransfer.oldAddress.streetAddress1", maxLength = "500", message = "", key = "Remaining address can be upto 500 characters long"),
		@StringLengthFieldValidator(fieldName = "licenseTransfer.oldPhoneNumber", maxLength = "15", message = "", key = "Maximum  length for Phone number is 15"),
		@StringLengthFieldValidator(fieldName = "licenseTransfer.oldMobileNumber", maxLength = "15", message = "", key = "Maximum length for Phone Number is 15"),
		@StringLengthFieldValidator(fieldName = "licenseTransfer.oldHomePhoneNumber", maxLength = "15", message = "", key = "Phone number should be upto 15 numbers"),
		@StringLengthFieldValidator(fieldName = "licenseTransfer.oldUid", maxLength = "12", message = "", key = "Maximum length for UID is 12") }, intRangeFields = { @IntRangeFieldValidator(fieldName = "licenseTransfer.oldAddress.pinCode", min = "100000",
		max = "999999", message = "", key = "Minimum and Maximum length for Pincode is 6 and all Digit Cannot be 0") })
public class TransferTradeLicenseAction extends BaseLicenseAction {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(TransferTradeLicenseAction.class);
	private TradeService ts;
	protected TradeLicense tl = new TradeLicense();

	public TransferTradeLicenseAction() {
		super();
		this.tl.setLicenseTransfer(new LicenseTransfer());
		this.tl.setLicensee(new Licensee());
		this.addRelatedEntity("licenseTransfer.boundary", Boundary.class);
	}

	public TradeLicense getTl() {
		return this.tl;
	}

	public void setTl(final TradeLicense tl) {
		this.tl = tl;
	}

	@Override
	public void prepare() {
		super.prepare();
	}

	public void prepareApprove() {
	}

	@Override
	protected BaseLicenseService service() {
		this.ts.getPersistenceService().setType(TradeLicense.class);
		return this.ts;
	}

	@Override
	public void prepareNewForm() {
		this.setupWorkflowDetails();
		final Integer userId = (Integer) session().get(Constants.SESSIONLOGINID);
		if (userId != null) {
			setRoleName(this.licenseUtils.getRolesForUserId(userId));
		}
		this.addDropdownData(Constants.DROPDOWN_ZONE_LIST, this.licenseUtils.getAllZone());
		this.addDropdownData(Constants.DROPDOWN_DIVISION_LIST_LICENSEE, new ArrayList<Boundary>());
	}

	@Override
	@SkipValidation
	public String newForm() {
		this.tl = (TradeLicense) this.ts.getPersistenceService().find("from TradeLicense where id=?", this.tl.getId());
		this.tl.setLicenseeZoneId(Long.valueOf(this.tl.getLicensee().getBoundary().getParent().getId()));
		List cityZoneList = new ArrayList();
		cityZoneList = this.licenseUtils.getAllZone();
		this.tl.setLicenseZoneList(cityZoneList);
		final Boundary licenseeboundary = (new BoundaryDAO()).getBoundary(this.tl.getLicensee().getBoundary().getId().intValue());
		if (licenseeboundary.getName().contains("Zone")) {
			this.addDropdownData(Constants.DROPDOWN_DIVISION_LIST_LICENSEE, Collections.EMPTY_LIST);
		}
		this.loadAjaxedDropDowns();
		return "transfer";
	}

	@Override
	@ValidationErrorPage("transfer")
	public String create() {
		LOGGER.debug("Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + this.tl);
		if (this.tl.getLicenseeZoneId() != null && this.tl.getLicenseTransfer().getBoundary() == null) {
			final Boundary licenseeboundary = (new BoundaryDAO()).getBoundary(this.tl.getLicenseeZoneId().intValue());
			this.tl.getLicenseTransfer().setBoundary(licenseeboundary);
		}
		final LicenseTransfer licenseTransfer = this.tl.getLicenseTransfer();
		this.tl = (TradeLicense) this.persistenceService.find("from TradeLicense where id=?", this.tl.getId());
		this.ts.transferLicense(this.tl, licenseTransfer);
		try {
			this.ts.initiateWorkFlowForTransfer(this.license(), this.workflowBean);
			if (this.workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONAPPROVE)) {
				doAuditing(AuditModule.TL, AuditEntity.TL_LIC, "TRANSFER LICENSE", this.tl.getAuditDetails());
			}
		} catch (final EGOVRuntimeException e) {
			throw new ValidationException(Arrays.asList(new ValidationError("license.workflow.already.Started", "File is some other workflow cannot proceed with the action")));
		}
		this.setMessages();
		LOGGER.debug("Exiting from the create method:<<<<<<<<<<>>>>>>>>>>>>>:");
		return "message";
	}

	@ValidationErrorPage("edit")
	public String edit() {
		LOGGER.debug("Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + this.tl);
		if (this.tl.getLicenseeZoneId() != null && this.tl.getLicenseTransfer().getBoundary() == null) {
			final Boundary licenseeboundary = (new BoundaryDAO()).getBoundary(this.tl.getLicenseeZoneId().intValue());
			this.tl.getLicenseTransfer().setBoundary(licenseeboundary);
		}
		final LicenseTransfer licenseTransfer = this.tl.getLicenseTransfer();
		this.tl = (TradeLicense) this.persistenceService.find("from TradeLicense where id=?", this.tl.getId());
		licenseTransfer.setLicense(this.tl);
		this.tl.setLicenseTransfer(licenseTransfer);
		this.persistenceService.persist(this.tl);
		this.ts.processWorkFlowForTransfer(this.license(), this.workflowBean);
		this.setMessages();
		LOGGER.debug("Exiting from the edit method:<<<<<<<<<<>>>>>>>>>>>>>:");
		return "message";
	}

	@SkipValidation
	@Override
	public String approve() {
		LOGGER.debug("Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + this.tl);
		this.tl = (TradeLicense) this.persistenceService.find("from TradeLicense where id=?", this.tl.getId());
		this.ts.processWorkFlowForTransfer(this.license(), this.workflowBean);
		if (this.workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONAPPROVE)) {
			doAuditing(AuditModule.TL, AuditEntity.TL_LIC, "TRANSFER LICENSE", this.tl.getAuditDetails());
		}

		this.setMessages();
		LOGGER.debug("Exiting from the approve method:<<<<<<<<<<>>>>>>>>>>>>>:");
		return "message";
	}

	private void setMessages() {
		LOGGER.debug("Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + this.tl);
		if (this.workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONSAVE)) {
			this.userService.getUserByID(this.license().getCreatedBy().getId());
			this.addActionMessage(this.getText("license.transfer.submission.succesful") + this.license().getLicenseTransfer().getOldApplicationNumber());
		} else if (this.workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONAPPROVE)) {
			final User userByID = this.userService.getUserByID(this.license().getCreatedBy().getId());
			this.addActionMessage(this.getText("license.transfer.approved.and.sent.to") + " " + userByID.getUserName() + " " + this.getText("license.for.certificate.generation"));
		} else if (this.workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONFORWARD)) {
			final User userByID = this.userService.getUserByID(this.workflowBean.getApproverUserId());
			this.addActionMessage(this.getText("license.transfer.sent") + userByID.getUserName());
		} else if (this.workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONREJECT)) {
			if (this.license().getState().getValue().equals("END")) {
				this.addActionMessage(this.getText("license.transfer.appl.fully.rejected") + this.license().getLicenseTransfer().getOldApplicationNumber());
			} else {
				this.addActionMessage(this.getText("license.transfer.rejected") + this.license().getCreatedBy().getUserName());
			}
		} else if (this.workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONGENERATEDCERTIFICATE)) {
			this.addActionMessage(this.getText("license.transfer.certifiacte.print.complete.recorded"));
		}
		LOGGER.debug("Exiting from the setMessages method:<<<<<<<<<<>>>>>>>>>>>>>:");
	}

	public void prepareBeforeEdit() {
		LOGGER.debug("Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + this.tl);
		this.prepareShowForApproval();
		LOGGER.debug("Exiting from the prepareBeforeEdit method:<<<<<<<<<<>>>>>>>>>>>>>:");
	}

	@SkipValidation
	public String beforeEdit() {
		LOGGER.debug("Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + this.tl);
		this.showForApproval();
		List cityZoneList = new ArrayList();
		cityZoneList = this.licenseUtils.getAllZone();
		this.tl.setLicenseZoneList(cityZoneList);
		final Boundary licenseeboundary = (new BoundaryDAO()).getBoundary(this.tl.getLicenseTransfer().getBoundary().getId().intValue());
		if (licenseeboundary.getName().contains("Zone")) {
			this.addDropdownData(Constants.DROPDOWN_DIVISION_LIST_LICENSEE, Collections.EMPTY_LIST);
		} else {
			this.addDropdownData(Constants.DROPDOWN_DIVISION_LIST_LICENSEE, new ArrayList(this.tl.getLicenseTransfer().getBoundary().getParent().getChildren()));
		}

		LOGGER.debug("Exiting from the beforeEdit method:<<<<<<<<<<>>>>>>>>>>>>>:");
		return Constants.EDIT;
	}

	@Override
	public void prepareShowForApproval() {
		LOGGER.debug("Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + this.tl);
		this.prepareNewForm();
		LOGGER.debug("Exiting from the prepareShowForApproval method:<<<<<<<<<<>>>>>>>>>>>>>:");
	}

	@Override
	@SkipValidation
	public String showForApproval() {
		LOGGER.debug("Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + this.tl);
		if (this.tl.getId() == null) {
			if (this.getSession().get("model.id") != null) {
				this.tl.setId(Long.valueOf((Long) this.getSession().get("model.id")));
				this.getSession().remove("model.id");
			}
		}
		this.tl = (TradeLicense) this.persistenceService.find("from TradeLicense where id=?", this.license().getId());
		System.out.println(this.tl.getLicenseTransfer().getBoundary().getId());
		System.out.println(this.tl.getLicenseTransfer().getBoundary().getName());
		this.loadAjaxedDropDowns();
		final Integer userId = (Integer) session().get(Constants.SESSIONLOGINID);
		if (userId != null) {
			setRoleName(this.licenseUtils.getRolesForUserId(userId));
		}
		LOGGER.debug("Exiting from the showForApproval method:<<<<<<<<<<>>>>>>>>>>>>>:");
		return "approve";
	}

	@Override
	public Object getModel() {
		return this.tl;
	}

	public void setTs(final TradeService ts) {
		this.ts = ts;
	}

	protected WorkflowService workflowService() {
		return null;
	}

	public WorkflowBean getWorkflowBean() {
		return this.workflowBean;
	}

	public void setWorkflowBean(final WorkflowBean workflowBean) {
		this.workflowBean = workflowBean;
	}

	@Override
	protected License license() {
		return this.tl;
	}

	public void setUserService(final UserService userService) {
		this.userService = userService;
	}
}
