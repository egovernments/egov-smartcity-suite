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
package org.egov.tl.newtradelicense.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.persistence.entity.PermanentAddress;
import org.egov.infra.web.struts.annotation.ValidationErrorPageExt;
import org.egov.tl.domain.entity.License;
import org.egov.tl.domain.entity.Licensee;
import org.egov.tl.domain.entity.TradeLicense;
import org.egov.tl.domain.entity.WorkflowBean;
import org.egov.tl.domain.service.BaseLicenseService;
import org.egov.tl.domain.service.TradeService;
import org.egov.tl.domain.web.BaseLicenseAction;
import org.egov.tl.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;

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
    @Autowired
    private BoundaryService boundaryService;

    public EditTradeLicenseAction() {
        super();
        tradeLicense.setLicensee(new Licensee());
        tradeLicense.setAddress(new PermanentAddress());
        tradeLicense.getLicensee().setAddress(new PermanentAddress());
    }

    /* to log errors and debugging information */
    private final Logger LOGGER = Logger.getLogger(getClass());

    @Override
    public Object getModel() {
        return tradeLicense;
    }

    public void prepareBeforeEdit() {
        LOGGER.debug("Entering in the prepareBeforeEdit method:<<<<<<<<<<>>>>>>>>>>>>>:");
        prepareNewForm();
        Long id = null;
        if (tradeLicense.getId() == null)
            if (getSession().get("model.id") != null) {
                id = (Long) getSession().get("model.id");
                getSession().remove("model.id");
            }
            else
                id = tradeLicense.getId();
        // this.persistenceService.setType(TradeLicense.class);
        tradeLicense = (TradeLicense) persistenceService.find("from TradeLicense where id = ?", id);
        if (tradeLicense.getOldLicenseNumber() != null)
            isOldLicense = org.apache.commons.lang.StringUtils.isNotBlank(tradeLicense.getOldLicenseNumber());
        final Boundary licenseboundary = boundaryService.getBoundaryById(tradeLicense.getBoundary().getId());
        final Boundary licenseeboundary = boundaryService.getBoundaryById(tradeLicense.getLicensee().getBoundary().getId());
        List cityZoneList = new ArrayList();
        cityZoneList = licenseUtils.getAllZone();
        tradeLicense.setLicenseZoneList(cityZoneList);
        if (licenseboundary.getName().contains("Zone"))
            addDropdownData(Constants.DROPDOWN_DIVISION_LIST_LICENSE, Collections.EMPTY_LIST);
        else if (tradeLicense.getLicensee().getBoundary() != null)
            addDropdownData(Constants.DROPDOWN_DIVISION_LIST_LICENSE,
                    new ArrayList(tradeLicense.getBoundary().getParent().getChildren()));

        if (licenseeboundary.getName().contains("Zone"))
            addDropdownData(Constants.DROPDOWN_DIVISION_LIST_LICENSEE, Collections.EMPTY_LIST);
        else if (tradeLicense.getLicensee().getBoundary() != null)
            addDropdownData(Constants.DROPDOWN_DIVISION_LIST_LICENSEE, new ArrayList(tradeLicense.getLicensee().getBoundary()
                    .getParent().getChildren()));

        final Integer userId = (Integer) session().get(Constants.SESSIONLOGINID);
        if (userId != null)
            setRoleName(licenseUtils.getRolesForUserId(userId));

        LOGGER.debug("Exiting from the prepareBeforeEdit method:<<<<<<<<<<>>>>>>>>>>>>>:");
    }

    @SkipValidation
    public String beforeEdit() {
        return Constants.EDIT;
    }

    public void setupBeforeEdit() {
        LOGGER.debug("Entering in the setupBeforeEdit method:<<<<<<<<<<>>>>>>>>>>>>>:");
        prepareBeforeEdit();
        setupWorkflowDetails();
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
                    @EmailValidator(message = "Please enter the valid Email Id", fieldName = "licensee.emailId", key = "Please enter the valid Email Id")
            },
            stringLengthFields
            = {
                    @StringLengthFieldValidator(fieldName = "nameOfEstablishment", maxLength = "100", message = "", key = "Name of Establishment can be upto 100 characters long"),
                    @StringLengthFieldValidator(fieldName = "address.houseNo", maxLength = "10", message = "", key = "Maximum  length for house number is 10"),
                    @StringLengthFieldValidator(fieldName = "address.streetAddress2", maxLength = "10", message = "", key = "Maximum  length for house number is 10"),
                    @StringLengthFieldValidator(fieldName = "phoneNumber", maxLength = "15", message = "", key = "Maximum  length for Phone number is 15"),
                    @StringLengthFieldValidator(fieldName = "remarks", maxLength = "500", message = "", key = "Remarks can be upto 500 characters long"),
                    @StringLengthFieldValidator(fieldName = "licensee.address.houseNo", maxLength = "10", message = "", key = "Maximum  length for house number is 10"),
                    @StringLengthFieldValidator(fieldName = "licensee.address.streetAddress2", maxLength = "10", message = "", key = "Maximum  length for house number is 10"),
                    @StringLengthFieldValidator(fieldName = "licensee.phoneNumber", maxLength = "15", message = "", key = "Phone number should be upto 15 numbers"),
                    @StringLengthFieldValidator(fieldName = "licensee.mobilePhoneNumber", maxLength = "15", message = "", key = "Maximum length for Phone Number is 15"),
                    @StringLengthFieldValidator(fieldName = "licensee.uid", maxLength = "12", message = "", key = "Maximum length for UID is 12") },
            intRangeFields = {
                    @IntRangeFieldValidator(fieldName = "noOfRooms", min = "1", max = "999", message = "", key = "Number of rooms should be in the range 1 to 999"),
                    @IntRangeFieldValidator(fieldName = "address.pinCode", min = "100000", max = "999999", message = "", key = "Minimum and Maximum length for Pincode is 6 and all Digit Cannot be 0"),
                    @IntRangeFieldValidator(fieldName = "licensee.address.pinCode", min = "100000", max = "999999", message = "", key = "Minimum and Maximum length for Pincode is 6 and all Digit Cannot be 0")
            }
            )
            @ValidationErrorPageExt(
                    action = "edit", makeCall = true, toMethod = "setupBeforeEdit")
            public String edit() {
        LOGGER.debug("Edit Trade License Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + tradeLicense.toString());
        persistenceService.setType(TradeLicense.class);
        final TradeLicense modifiedTL = tradeLicense;
        tradeLicense = (TradeLicense) persistenceService.findById(modifiedTL.getId(), false);

        // Licensee details
        tradeLicense.getLicensee().getAddress().setHouseNoBldgApt(modifiedTL.getLicensee().getAddress().getHouseNoBldgApt());
        tradeLicense.getLicensee().getAddress().setStreetRoadLine(modifiedTL.getLicensee().getAddress().getStreetRoadLine());
        tradeLicense.getLicensee().getAddress().setPinCode(modifiedTL.getLicensee().getAddress().getPinCode());
        tradeLicense.getLicensee().setPhoneNumber(modifiedTL.getLicensee().getPhoneNumber());
        tradeLicense.getLicensee().setMobilePhoneNumber(modifiedTL.getLicensee().getMobilePhoneNumber());
        tradeLicense.getLicensee().setEmailId(modifiedTL.getLicensee().getEmailId());
        tradeLicense.getLicensee().setUid(modifiedTL.getLicensee().getUid());
        tradeLicense.setNameOfEstablishment(modifiedTL.getNameOfEstablishment());

        // License details
        tradeLicense.getAddress().setHouseNoBldgApt(modifiedTL.getAddress().getHouseNoBldgApt());
        tradeLicense.getAddress().setStreetRoadLine(modifiedTL.getAddress().getStreetRoadLine());
        tradeLicense.getAddress().setPinCode(modifiedTL.getAddress().getPinCode());
        tradeLicense.setPhoneNumber(modifiedTL.getPhoneNumber());
        tradeLicense.setBuildingType(modifiedTL.getBuildingType());
        tradeLicense.setRentPaid(modifiedTL.getRentPaid());
        tradeLicense.setNoOfRooms(modifiedTL.getNoOfRooms());
        tradeLicense.setRemarks(modifiedTL.getRemarks());
        tradeLicense.setDocNumber(modifiedTL.getDocNumber());
        if (modifiedTL.getLicenseZoneId() != null && modifiedTL.getBoundary() == null) {
            final Boundary licenseboundary = boundaryService.getBoundaryById(modifiedTL.getLicenseZoneId());
            tradeLicense.setBoundary(licenseboundary);
        } else
            tradeLicense.setBoundary(modifiedTL.getBoundary());

        if (modifiedTL.getLicenseeZoneId() != null && modifiedTL.getLicensee().getBoundary() == null) {
            final Boundary licenseeboundary = boundaryService.getBoundaryById(modifiedTL.getLicenseeZoneId());
            tradeLicense.getLicensee().setBoundary(licenseeboundary);
        } else
            tradeLicense.getLicensee().setBoundary(modifiedTL.getLicensee().getBoundary());
        persistenceService.update(tradeLicense);
        if (tradeLicense.getState() == null && !isOldLicense)
            initiateWorkFlowForLicense();
        if (!isOldLicense)
            processWorkflow(NEW);
        addActionMessage(this.getText("license.update.succesful"));
        /*
         * if (tradeLicense.getOldLicenseNumber() != null) doAuditing(AuditModule.TL, AuditEntity.TL_LIC, AuditEvent.MODIFIED,
         * tradeLicense.getAuditDetails());
         */
        LOGGER.debug("Exiting from the edit method:<<<<<<<<<<>>>>>>>>>>>>>:");
        return Constants.MESSAGE;

    }

    @Override
    public boolean acceptableParameterName(final String paramName) {
        final List<String> nonAcceptable = Arrays.asList(new String[] { "licensee.boundary.parent", "boundary.parent",
                "tradeName.name" });
        final boolean retValue = super.acceptableParameterName(paramName);
        return retValue ? !nonAcceptable.contains(paramName) : retValue;
    }

    public WorkflowBean getWorkflowBean() {
        return workflowBean;
    }

    @Override
    protected License license() {
        return tradeLicense;
    }

    @Override
    protected BaseLicenseService service() {
        ts.getPersistenceService().setType(TradeLicense.class);
        return ts;
    }

    public void setTs(final TradeService ts) {
        this.ts = ts;
    }

    public void setWorkflowBean(final WorkflowBean workflowBean) {
        this.workflowBean = workflowBean;
    }

    public boolean getIsOldLicense() {
        return isOldLicense;
    }

    public void setIsOldLicense(final boolean isOldLicense) {
        this.isOldLicense = isOldLicense;
    }

}
