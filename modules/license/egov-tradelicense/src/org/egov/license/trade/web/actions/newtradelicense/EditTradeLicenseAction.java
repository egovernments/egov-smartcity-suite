/*
 * @(#)EditTradeLicenseAction.java 3.0, 25 Jul, 2013 4:57:53 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.license.trade.web.actions.newtradelicense;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infstr.auditing.model.AuditEntity;
import org.egov.infstr.auditing.model.AuditEvent;
import org.egov.infstr.auditing.model.AuditModule;
import org.egov.lib.address.model.Address;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.lib.rjbac.user.ejb.api.UserService;
import org.egov.license.domain.entity.License;
import org.egov.license.domain.entity.Licensee;
import org.egov.license.domain.entity.WorkflowBean;
import org.egov.license.domain.service.BaseLicenseService;
import org.egov.license.trade.domain.entity.TradeLicense;
import org.egov.license.trade.domain.service.TradeService;
import org.egov.license.utils.Constants;
import org.egov.license.web.actions.common.BaseLicenseAction;
import org.egov.pims.commons.service.EisCommonsService;
import org.egov.pims.service.EmployeeService;
import org.egov.web.annotation.ValidationErrorPageExt;

import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import com.opensymphony.xwork2.validator.annotations.IntRangeFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

@ParentPackage("egov")
public class EditTradeLicenseAction extends BaseLicenseAction {
	private static final long serialVersionUID = 1L;
	private TradeLicense tradeLicense = new TradeLicense();
	private TradeService ts;
	private boolean isOldLicense = false;

	public EditTradeLicenseAction() {
		super();
		this.tradeLicense.setLicensee(new Licensee());
		this.tradeLicense.setAddress(new Address());
		this.tradeLicense.getLicensee().setAddress(new Address());
	}

	/* to log errors and debugging information */
	private final Logger LOGGER = Logger.getLogger(getClass());

	@Override
	public Object getModel() {
		return this.tradeLicense;
	}

	public void prepareBeforeEdit() {
		this.LOGGER.debug("Entering in the prepareBeforeEdit method:<<<<<<<<<<>>>>>>>>>>>>>:");
		this.prepareNewForm();
		if (this.tradeLicense.getId() == null) {
			if (this.getSession().get("model.id") != null) {
				this.tradeLicense.setId(Long.valueOf((Long) this.getSession().get("model.id")));
				this.getSession().remove("model.id");
			}

		}

		// this.persistenceService.setType(TradeLicense.class);
		this.tradeLicense = (TradeLicense) this.ts.getPersistenceService().findById(this.tradeLicense.getId(), false);
		if (this.tradeLicense.getOldLicenseNumber() != null) {
			this.isOldLicense = org.apache.commons.lang.StringUtils.isNotBlank(this.tradeLicense.getOldLicenseNumber());
		}
		final Boundary licenseboundary = (new BoundaryDAO()).getBoundary(this.tradeLicense.getBoundary().getId().intValue());
		final Boundary licenseeboundary = (new BoundaryDAO()).getBoundary(this.tradeLicense.getLicensee().getBoundary().getId().intValue());
		List cityZoneList = new ArrayList();
		cityZoneList = this.licenseUtils.getAllZone();
		this.tradeLicense.setLicenseZoneList(cityZoneList);
		if (licenseboundary.getName().contains("Zone")) {
			this.addDropdownData(Constants.DROPDOWN_DIVISION_LIST_LICENSE, Collections.EMPTY_LIST);
		} else {
			if (this.tradeLicense.getLicensee().getBoundary() != null) {
				this.addDropdownData(Constants.DROPDOWN_DIVISION_LIST_LICENSE, new ArrayList(this.tradeLicense.getBoundary().getParent().getChildren()));
			}
		}

		if (licenseeboundary.getName().contains("Zone")) {
			this.addDropdownData(Constants.DROPDOWN_DIVISION_LIST_LICENSEE, Collections.EMPTY_LIST);
		} else {
			if (this.tradeLicense.getLicensee().getBoundary() != null) {
				this.addDropdownData(Constants.DROPDOWN_DIVISION_LIST_LICENSEE, new ArrayList(this.tradeLicense.getLicensee().getBoundary().getParent().getChildren()));
			}
		}

		final Integer userId = (Integer) session().get(Constants.SESSIONLOGINID);
		if (userId != null) {
			setRoleName(this.licenseUtils.getRolesForUserId(userId));
		}

		this.LOGGER.debug("Exiting from the prepareBeforeEdit method:<<<<<<<<<<>>>>>>>>>>>>>:");
	}

	@SkipValidation
	public String beforeEdit() {
		return Constants.EDIT;
	}

	public void setupBeforeEdit() {
		this.LOGGER.debug("Entering in the setupBeforeEdit method:<<<<<<<<<<>>>>>>>>>>>>>:");
		this.prepareBeforeEdit();
		this.setupWorkflowDetails();
		this.LOGGER.debug("Exiting from the setupBeforeEdit method:<<<<<<<<<<>>>>>>>>>>>>>:");
	}

	@Validations(requiredFields = { @RequiredFieldValidator(fieldName = "licenseeZoneId", message = "", key = Constants.REQUIRED), @RequiredFieldValidator(fieldName = "licenseZoneId", message = "", key = Constants.REQUIRED),
			@RequiredFieldValidator(fieldName = "licensee.address.houseNo", message = "", key = Constants.REQUIRED), @RequiredFieldValidator(fieldName = "nameOfEstablishment", message = "", key = Constants.REQUIRED),
			@RequiredFieldValidator(fieldName = "address.houseNo", message = "", key = Constants.REQUIRED), @RequiredFieldValidator(fieldName = "buildingType", message = "", key = Constants.REQUIRED) }, emails = { @EmailValidator(
			message = "Please enter the valid Email Id", fieldName = "licensee.emailId", key = "Please enter the valid Email Id") }, stringLengthFields = {
			@StringLengthFieldValidator(fieldName = "nameOfEstablishment", maxLength = "100", message = "", key = "Name of Establishment can be upto 100 characters long"),
			@StringLengthFieldValidator(fieldName = "address.houseNo", maxLength = "10", message = "", key = "Maximum  length for house number is 10"),
			@StringLengthFieldValidator(fieldName = "address.streetAddress2", maxLength = "10", message = "", key = "Maximum  length for house number is 10"),
			@StringLengthFieldValidator(fieldName = "phoneNumber", maxLength = "15", message = "", key = "Maximum  length for Phone number is 15"),
			@StringLengthFieldValidator(fieldName = "remarks", maxLength = "500", message = "", key = "Remarks can be upto 500 characters long"),
			@StringLengthFieldValidator(fieldName = "licensee.address.houseNo", maxLength = "10", message = "", key = "Maximum  length for house number is 10"),
			@StringLengthFieldValidator(fieldName = "licensee.address.streetAddress2", maxLength = "10", message = "", key = "Maximum  length for house number is 10"),
			@StringLengthFieldValidator(fieldName = "licensee.phoneNumber", maxLength = "15", message = "", key = "Phone number should be upto 15 numbers"),
			@StringLengthFieldValidator(fieldName = "licensee.mobilePhoneNumber", maxLength = "15", message = "", key = "Maximum length for Phone Number is 15"),
			@StringLengthFieldValidator(fieldName = "licensee.uid", maxLength = "12", message = "", key = "Maximum length for UID is 12") }, intRangeFields = {
			@IntRangeFieldValidator(fieldName = "noOfRooms", min = "1", max = "999", message = "", key = "Number of rooms should be in the range 1 to 999"),
			@IntRangeFieldValidator(fieldName = "address.pinCode", min = "100000", max = "999999", message = "", key = "Minimum and Maximum length for Pincode is 6 and all Digit Cannot be 0"),
			@IntRangeFieldValidator(fieldName = "licensee.address.pinCode", min = "100000", max = "999999", message = "", key = "Minimum and Maximum length for Pincode is 6 and all Digit Cannot be 0") })
	@ValidationErrorPageExt(action = "edit", makeCall = true, toMethod = "setupBeforeEdit")
	public String edit() {
		this.LOGGER.debug("Edit Trade License Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + this.tradeLicense.toString());
		this.persistenceService.setType(TradeLicense.class);
		final TradeLicense modifiedTL = this.tradeLicense;
		this.tradeLicense = (TradeLicense) this.persistenceService.findById(modifiedTL.getId(), false);

		// Licensee details
		this.tradeLicense.getLicensee().getAddress().setHouseNo(modifiedTL.getLicensee().getAddress().getHouseNo());
		this.tradeLicense.getLicensee().getAddress().setStreetAddress2(modifiedTL.getLicensee().getAddress().getStreetAddress2());
		this.tradeLicense.getLicensee().getAddress().setPinCode(modifiedTL.getLicensee().getAddress().getPinCode());
		this.tradeLicense.getLicensee().setPhoneNumber(modifiedTL.getLicensee().getPhoneNumber());
		this.tradeLicense.getLicensee().setMobilePhoneNumber(modifiedTL.getLicensee().getMobilePhoneNumber());
		this.tradeLicense.getLicensee().setEmailId(modifiedTL.getLicensee().getEmailId());
		this.tradeLicense.getLicensee().setUid(modifiedTL.getLicensee().getUid());
		this.tradeLicense.setNameOfEstablishment(modifiedTL.getNameOfEstablishment());

		// License details
		this.tradeLicense.getAddress().setHouseNo(modifiedTL.getAddress().getHouseNo());
		this.tradeLicense.getAddress().setStreetAddress2(modifiedTL.getAddress().getStreetAddress2());
		this.tradeLicense.getAddress().setPinCode(modifiedTL.getAddress().getPinCode());
		this.tradeLicense.setPhoneNumber(modifiedTL.getPhoneNumber());
		this.tradeLicense.setBuildingType(modifiedTL.getBuildingType());
		this.tradeLicense.setRentPaid(modifiedTL.getRentPaid());
		this.tradeLicense.setNoOfRooms(modifiedTL.getNoOfRooms());
		this.tradeLicense.setRemarks(modifiedTL.getRemarks());
		this.tradeLicense.setDocNumber(modifiedTL.getDocNumber());
		if (modifiedTL.getLicenseZoneId() != null && modifiedTL.getBoundary() == null) {
			final Boundary licenseboundary = (new BoundaryDAO()).getBoundary(modifiedTL.getLicenseZoneId().intValue());
			this.tradeLicense.setBoundary(licenseboundary);
		} else {
			this.tradeLicense.setBoundary(modifiedTL.getBoundary());
		}

		if (modifiedTL.getLicenseeZoneId() != null && modifiedTL.getLicensee().getBoundary() == null) {
			final Boundary licenseeboundary = (new BoundaryDAO()).getBoundary(modifiedTL.getLicenseeZoneId().intValue());
			this.tradeLicense.getLicensee().setBoundary(licenseeboundary);
		} else {
			this.tradeLicense.getLicensee().setBoundary(modifiedTL.getLicensee().getBoundary());
		}
		this.persistenceService.update(this.tradeLicense);
		if (this.tradeLicense.getState() == null && !this.isOldLicense) {
			initiateWorkFlowForLicense();
		}
		if (!this.isOldLicense) {
			this.processWorkflow(NEW);
		}
		this.addActionMessage(this.getText("license.update.succesful"));
		if (this.tradeLicense.getOldLicenseNumber() != null) {
			doAuditing(AuditModule.TL, AuditEntity.TL_LIC, AuditEvent.MODIFIED, this.tradeLicense.getAuditDetails());
		}
		this.LOGGER.debug("Exiting from the edit method:<<<<<<<<<<>>>>>>>>>>>>>:");
		return Constants.MESSAGE;

	}

	@Override
	public boolean acceptableParameterName(final String paramName) {
		final List<String> nonAcceptable = Arrays.asList(new String[] { "licensee.boundary.parent", "boundary.parent", "tradeName.name" });
		final boolean retValue = super.acceptableParameterName(paramName);
		return retValue ? !nonAcceptable.contains(paramName) : retValue;
	}

	public WorkflowBean getWorkflowBean() {
		return this.workflowBean;
	}

	@Override
	protected License license() {
		return this.tradeLicense;
	}

	@Override
	protected BaseLicenseService service() {
		this.ts.getPersistenceService().setType(TradeLicense.class);
		return this.ts;
	}

	public void setEisCommonsService(final EisCommonsService eisCommonsService) {
		this.eisCommonsService = eisCommonsService;
	}

	public void setEmployeeService(final EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public void setTs(final TradeService ts) {
		this.ts = ts;
	}

	public void setUserService(final UserService userService) {
		this.userService = userService;
	}

	public void setWorkflowBean(final WorkflowBean workflowBean) {
		this.workflowBean = workflowBean;
	}

	public boolean getIsOldLicense() {
		return this.isOldLicense;
	}

	public void setIsOldLicense(final boolean isOldLicense) {
		this.isOldLicense = isOldLicense;
	}

}
