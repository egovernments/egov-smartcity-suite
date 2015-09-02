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
package org.egov.license.trade.newtradelicense.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infstr.auditing.model.AuditEntity;
import org.egov.infstr.auditing.model.AuditEvent;
import org.egov.infstr.auditing.model.AuditModule;
import org.egov.infstr.utils.StringUtils;
import org.egov.lib.address.model.Address;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.lib.rjbac.user.ejb.api.UserManager;
import org.egov.license.domain.entity.License;
import org.egov.license.domain.entity.Licensee;
import org.egov.license.domain.entity.WorkflowBean;
import org.egov.license.domain.service.BaseLicenseService;
import org.egov.license.domain.web.BaseLicenseAction;
import org.egov.license.trade.domain.entity.TradeLicense;
import org.egov.license.trade.domain.service.TradeService;
import org.egov.license.utils.Constants;
import org.egov.pims.commons.service.EisCommonsManager;
import org.egov.pims.service.EisManager;

import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import com.opensymphony.xwork2.validator.annotations.IntRangeFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import org.egov.web.annotation.ValidationErrorPageExt;

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
		 LOGGER.debug("Entering in the prepareBeforeEdit method:<<<<<<<<<<>>>>>>>>>>>>>:");
		this.prepareNewForm();
		if (this.tradeLicense.getId() == null) {
			if (this.getSession().get("model.id") != null) {
				this.tradeLicense.setId(Long.valueOf((Long) this.getSession().get("model.id")));
				this.getSession().remove("model.id");
			}
		
		}

		// this.persistenceService.setType(TradeLicense.class);
		this.tradeLicense = (TradeLicense) this.persistenceService.find("from TradeLicense where id = ?",this.tradeLicense.getId());
		if(this.tradeLicense.getOldLicenseNumber()!=null)
		this.isOldLicense = StringUtils.isNotBlank(this.tradeLicense.getOldLicenseNumber());
		Boundary licenseboundary = (new BoundaryDAO()).getBoundary(tradeLicense.getBoundary().getId().intValue());
		Boundary licenseeboundary = (new BoundaryDAO()).getBoundary(tradeLicense.getLicensee().getBoundary().getId().intValue());
		List cityZoneList = new ArrayList();
		cityZoneList = this.licenseUtils.getAllZone();
		this.tradeLicense.setLicenseZoneList(cityZoneList);
		if (licenseboundary.getName().contains("Zone")) {
			this.addDropdownData(Constants.DROPDOWN_DIVISION_LIST_LICENSE, Collections.EMPTY_LIST);
		} else {
			if(this.tradeLicense.getLicensee().getBoundary()!=null)
			this.addDropdownData(Constants.DROPDOWN_DIVISION_LIST_LICENSE,
			        new ArrayList(this.tradeLicense.getBoundary().getParent().getChildren()));
		}

		if (licenseeboundary.getName().contains("Zone")) {
			this.addDropdownData(Constants.DROPDOWN_DIVISION_LIST_LICENSEE, Collections.EMPTY_LIST);
		} else {
			if(this.tradeLicense.getLicensee().getBoundary()!=null)
				this.addDropdownData(Constants.DROPDOWN_DIVISION_LIST_LICENSEE, new ArrayList(this.tradeLicense.getLicensee().getBoundary()
			        .getParent().getChildren()));
		}
		
		Integer userId = (Integer) session().get(Constants.SESSIONLOGINID);
		if (userId != null) {
			setRoleName(licenseUtils.getRolesForUserId(userId));
		}

		LOGGER.debug("Exiting from the prepareBeforeEdit method:<<<<<<<<<<>>>>>>>>>>>>>:");
	}

	@SkipValidation
	public String beforeEdit() {
		return Constants.EDIT;
	}


	public void setupBeforeEdit() {
		LOGGER.debug("Entering in the setupBeforeEdit method:<<<<<<<<<<>>>>>>>>>>>>>:");
		this.prepareBeforeEdit();
		this.setupWorkflowDetails();
		LOGGER.debug("Exiting from the setupBeforeEdit method:<<<<<<<<<<>>>>>>>>>>>>>:");
	}

	@Validations(
			requiredFields = { 
					@RequiredFieldValidator(fieldName = "licenseeZoneId", message = "", key = Constants.REQUIRED),
					@RequiredFieldValidator(fieldName = "licenseZoneId", message = "", key = Constants.REQUIRED),
					@RequiredFieldValidator(fieldName = "licensee.address.houseNo", message = "", key = Constants.REQUIRED),
					@RequiredFieldValidator(fieldName = "nameOfEstablishment", message = "", key = Constants.REQUIRED),
					@RequiredFieldValidator(fieldName = "address.houseNo", message = "", key = Constants.REQUIRED),
					@RequiredFieldValidator(fieldName = "buildingType", message = "", key = Constants.REQUIRED) 
					},
			emails = {
					@EmailValidator(message = "Please enter the valid Email Id", fieldName="licensee.emailId", key="Please enter the valid Email Id")
					},
			stringLengthFields
			= { 
					@StringLengthFieldValidator(fieldName = "nameOfEstablishment", maxLength = "100",  message = "", key = "Name of Establishment can be upto 100 characters long"),
					@StringLengthFieldValidator(fieldName = "address.houseNo", maxLength = "10",  message = "", key = "Maximum  length for house number is 10"),
					@StringLengthFieldValidator(fieldName = "address.streetAddress2", maxLength = "10",  message = "", key = "Maximum  length for house number is 10"),
					@StringLengthFieldValidator(fieldName = "phoneNumber", maxLength = "15",  message = "", key = "Maximum  length for Phone number is 15"),
					@StringLengthFieldValidator(fieldName = "remarks", maxLength = "500",  message = "", key = "Remarks can be upto 500 characters long"),
					@StringLengthFieldValidator(fieldName = "licensee.address.houseNo", maxLength = "10",  message = "", key = "Maximum  length for house number is 10"),
					@StringLengthFieldValidator(fieldName = "licensee.address.streetAddress2", maxLength = "10",  message = "", key = "Maximum  length for house number is 10"),
					@StringLengthFieldValidator(fieldName = "licensee.phoneNumber", maxLength = "15",  message = "", key = "Phone number should be upto 15 numbers"),
					@StringLengthFieldValidator(fieldName = "licensee.mobilePhoneNumber", maxLength = "15",  message = "", key = "Maximum length for Phone Number is 15"),
					@StringLengthFieldValidator(fieldName = "licensee.uid", maxLength = "12",  message = "", key = "Maximum length for UID is 12")},
			intRangeFields = {
					@IntRangeFieldValidator(fieldName = "noOfRooms", min="1", max = "999",  message = "", key = "Number of rooms should be in the range 1 to 999"),
					@IntRangeFieldValidator(fieldName = "address.pinCode", min="100000", max = "999999",  message = "", key = "Minimum and Maximum length for Pincode is 6 and all Digit Cannot be 0"),
					@IntRangeFieldValidator(fieldName = "licensee.address.pinCode", min="100000", max = "999999",  message = "", key = "Minimum and Maximum length for Pincode is 6 and all Digit Cannot be 0")
					}
			)
	
	@ValidationErrorPageExt(
			action = "edit", makeCall = true, toMethod = "setupBeforeEdit")
	public String edit() {
		 LOGGER.debug("Edit Trade License Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + this.tradeLicense.toString());
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
			Boundary licenseboundary = (new BoundaryDAO()).getBoundary(modifiedTL.getLicenseZoneId().intValue());
			this.tradeLicense.setBoundary(licenseboundary);
		}else {
			this.tradeLicense.setBoundary(modifiedTL.getBoundary());
		}
		
		if (modifiedTL.getLicenseeZoneId() != null && modifiedTL.getLicensee().getBoundary() == null) {
			Boundary licenseeboundary = (new BoundaryDAO()).getBoundary(modifiedTL.getLicenseeZoneId().intValue());
			this.tradeLicense.getLicensee().setBoundary(licenseeboundary);
		}else {
			this.tradeLicense.getLicensee().setBoundary(modifiedTL.getLicensee().getBoundary());
		}
		this.persistenceService.update(this.tradeLicense);
		if(this.tradeLicense.getState()==null && !this.isOldLicense)
		{
			initiateWorkFlowForLicense();
		}
		if (!this.isOldLicense) {
			this.processWorkflow(NEW);
		}
		this.addActionMessage(this.getText("license.update.succesful"));
		if(this.tradeLicense.getOldLicenseNumber()!=null)
			doAuditing(AuditModule.TL,AuditEntity.TL_LIC, AuditEvent.MODIFIED, this.tradeLicense.getAuditDetails());
		 LOGGER.debug("Exiting from the edit method:<<<<<<<<<<>>>>>>>>>>>>>:");
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

	public void setEisCommonsManager(final EisCommonsManager eisCommonsManager) {
		this.eisCommonsManager = eisCommonsManager;
	}

	public void setEisManager(final EisManager eisManager) {
		this.eisManager = eisManager;
	}

	public void setTs(final TradeService ts) {
		this.ts = ts;
	}

	public void setUserManager(final UserManager usrManager) {
		this.userManager = usrManager;
	}

	public void setWorkflowBean(final WorkflowBean workflowBean) {
		this.workflowBean = workflowBean;
	}

	public boolean getIsOldLicense() {
		return this.isOldLicense;
	}

	public void setIsOldLicense(boolean isOldLicense) {
		this.isOldLicense = isOldLicense;
	}

}
