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
package org.egov.tl.transfer.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.workflow.service.WorkflowService;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.tl.domain.entity.License;
import org.egov.tl.domain.entity.Licensee;
import org.egov.tl.domain.entity.TradeLicense;
import org.egov.tl.domain.entity.WorkflowBean;
import org.egov.tl.domain.entity.transfer.LicenseTransfer;
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
@Validations(requiredFields = {
        @RequiredFieldValidator(fieldName = "licenseTransfer.oldApplicantName", message = "", key = Constants.REQUIRED),
        @RequiredFieldValidator(fieldName = "licenseTransfer.oldApplicationDate", message = "", key = Constants.REQUIRED),
        @RequiredFieldValidator(fieldName = "licenseTransfer.oldNameOfEstablishment", message = "", key = Constants.REQUIRED),
        @RequiredFieldValidator(fieldName = "licenseTransfer.oldAddress.houseNo", message = "", key = Constants.REQUIRED),
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
        @StringLengthFieldValidator(fieldName = "licenseTransfer.oldUid", maxLength = "12", message = "", key = "Maximum length for UID is 12") }, intRangeFields = { @IntRangeFieldValidator(fieldName = "licenseTransfer.oldAddress.pinCode", min = "100000", max = "999999", message = "", key = "Minimum and Maximum length for Pincode is 6 and all Digit Cannot be 0") })
public class TransferTradeLicenseAction extends BaseLicenseAction {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(TransferTradeLicenseAction.class);
    private TradeService ts;
    protected TradeLicense tl = new TradeLicense();
    @Autowired
    private BoundaryService boundaryService;
    @Autowired
    private UserService userService;

    public TransferTradeLicenseAction() {
        super();
        tl.setLicenseTransfer(new LicenseTransfer());
        tl.setLicensee(new Licensee());
        this.addRelatedEntity("licenseTransfer.boundary", Boundary.class);
    }

    public TradeLicense getTl() {
        return tl;
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
        ts.getPersistenceService().setType(TradeLicense.class);
        return ts;
    }

    @Override
    public void prepareNewForm() {
        setupWorkflowDetails();
        final Integer userId = (Integer) session().get(Constants.SESSIONLOGINID);
        if (userId != null)
            setRoleName(licenseUtils.getRolesForUserId(userId));
        addDropdownData(Constants.DROPDOWN_ZONE_LIST, licenseUtils.getAllZone());
        addDropdownData(Constants.DROPDOWN_DIVISION_LIST_LICENSEE, new ArrayList<Boundary>());
    }

    @Override
    @SkipValidation
    public String newForm() {
        tl = (TradeLicense) ts.getPersistenceService().find("from TradeLicense where id=?", tl.getId());
        tl.setLicenseeZoneId(Long.valueOf(tl.getLicensee().getBoundary().getParent().getId()));
        List cityZoneList = new ArrayList();
        cityZoneList = licenseUtils.getAllZone();
        tl.setLicenseZoneList(cityZoneList);
        final Boundary licenseeboundary = boundaryService.getBoundaryById(tl.getLicensee().getBoundary().getId());
        if (licenseeboundary.getName().contains("Zone"))
            addDropdownData(Constants.DROPDOWN_DIVISION_LIST_LICENSEE, Collections.EMPTY_LIST);
        loadAjaxedDropDowns();
        return "transfer";
    }

    @Override
    @ValidationErrorPage("transfer")
    public String create() {
        LOGGER.debug("Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + tl);
        if (tl.getLicenseeZoneId() != null && tl.getLicenseTransfer().getBoundary() == null) {
            final Boundary licenseeboundary = boundaryService.getBoundaryById(tl.getLicenseeZoneId());
            tl.getLicenseTransfer().setBoundary(licenseeboundary);
        }
        final LicenseTransfer licenseTransfer = tl.getLicenseTransfer();
        tl = (TradeLicense) persistenceService.find("from TradeLicense where id=?", tl.getId());
        ts.transferLicense(tl, licenseTransfer);
        try {
            // ts.initiateWorkFlowForTransfer(license(), workflowBean);
            /*
             * if (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONAPPROVE)) doAuditing(AuditModule.TL,
             * AuditEntity.TL_LIC, "TRANSFER LICENSE", tl.getAuditDetails());
             */
        } catch (final EGOVRuntimeException e) {
            throw new ValidationException(Arrays.asList(new ValidationError("license.workflow.already.Started",
                    "File is some other workflow cannot proceed with the action")));
        }
        setMessages();
        LOGGER.debug("Exiting from the create method:<<<<<<<<<<>>>>>>>>>>>>>:");
        return "message";
    }

    @ValidationErrorPage("edit")
    public String edit() {
        LOGGER.debug("Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + tl);
        if (tl.getLicenseeZoneId() != null && tl.getLicenseTransfer().getBoundary() == null) {
            final Boundary licenseeboundary = boundaryService.getBoundaryById(tl.getLicenseeZoneId());
            tl.getLicenseTransfer().setBoundary(licenseeboundary);
        }
        final LicenseTransfer licenseTransfer = tl.getLicenseTransfer();
        tl = (TradeLicense) persistenceService.find("from TradeLicense where id=?", tl.getId());
        licenseTransfer.setLicense(tl);
        tl.setLicenseTransfer(licenseTransfer);
        persistenceService.persist(tl);
        // ts.processWorkFlowForTransfer(license(), workflowBean);
        setMessages();
        LOGGER.debug("Exiting from the edit method:<<<<<<<<<<>>>>>>>>>>>>>:");
        return "message";
    }

    @SkipValidation
    @Override
    public String approve() {
        LOGGER.debug("Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + tl);
        tl = (TradeLicense) persistenceService.find("from TradeLicense where id=?", tl.getId());
        // ts.processWorkFlowForTransfer(license(), workflowBean);
        /*
         * if (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONAPPROVE)) doAuditing(AuditModule.TL,
         * AuditEntity.TL_LIC, "TRANSFER LICENSE", tl.getAuditDetails());
         */

        setMessages();
        LOGGER.debug("Exiting from the approve method:<<<<<<<<<<>>>>>>>>>>>>>:");
        return "message";
    }

    private void setMessages() {
        LOGGER.debug("Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + tl);
        if (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONSAVE)) {
            userService.getUserById(license().getCreatedBy().getId());
            addActionMessage(this.getText("license.transfer.submission.succesful")
                    + license().getLicenseTransfer().getOldApplicationNumber());
        } else if (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONAPPROVE)) {
            final User userByID = userService.getUserById(license().getCreatedBy().getId());
            addActionMessage(this.getText("license.transfer.approved.and.sent.to") + " " + userByID.getName() + " "
                    + this.getText("license.for.certificate.generation"));
        } else if (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONFORWARD)) {
            final User userByID = userService.getUserById(workflowBean.getApproverUserId());
            addActionMessage(this.getText("license.transfer.sent") + userByID.getName());
        } else if (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONREJECT)) {
            if (license().getState().getValue().equals("END"))
                addActionMessage(this.getText("license.transfer.appl.fully.rejected")
                        + license().getLicenseTransfer().getOldApplicationNumber());
            else
                addActionMessage(this.getText("license.transfer.rejected") + license().getCreatedBy().getName());
        } else if (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONGENERATEDCERTIFICATE))
            addActionMessage(this.getText("license.transfer.certifiacte.print.complete.recorded"));
        LOGGER.debug("Exiting from the setMessages method:<<<<<<<<<<>>>>>>>>>>>>>:");
    }

    public void prepareBeforeEdit() {
        LOGGER.debug("Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + tl);
        prepareShowForApproval();
        LOGGER.debug("Exiting from the prepareBeforeEdit method:<<<<<<<<<<>>>>>>>>>>>>>:");
    }

    @SkipValidation
    public String beforeEdit() {
        LOGGER.debug("Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + tl);
        showForApproval();
        List cityZoneList = new ArrayList();
        cityZoneList = licenseUtils.getAllZone();
        tl.setLicenseZoneList(cityZoneList);
        final Boundary licenseeboundary = boundaryService.getBoundaryById(tl.getLicenseTransfer().getBoundary().getId());
        if (licenseeboundary.getName().contains("Zone"))
            addDropdownData(Constants.DROPDOWN_DIVISION_LIST_LICENSEE, Collections.EMPTY_LIST);
        else
            addDropdownData(Constants.DROPDOWN_DIVISION_LIST_LICENSEE,
                    new ArrayList(tl.getLicenseTransfer().getBoundary().getParent().getChildren()));

        LOGGER.debug("Exiting from the beforeEdit method:<<<<<<<<<<>>>>>>>>>>>>>:");
        return Constants.EDIT;
    }

    @Override
    public void prepareShowForApproval() {
        LOGGER.debug("Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + tl);
        prepareNewForm();
        LOGGER.debug("Exiting from the prepareShowForApproval method:<<<<<<<<<<>>>>>>>>>>>>>:");
    }

    @Override
    @SkipValidation
    public String showForApproval() {
        LOGGER.debug("Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + tl);
        Long id = null;
        if (tl.getId() == null)
            if (getSession().get("model.id") != null) {
                id = (Long) getSession().get("model.id");
                getSession().remove("model.id");
            }
            else
                id = tl.getId();
        tl = (TradeLicense) persistenceService.find("from TradeLicense where id=?", id);
        System.out.println(tl.getLicenseTransfer().getBoundary().getId());
        System.out.println(tl.getLicenseTransfer().getBoundary().getName());
        loadAjaxedDropDowns();
        final Integer userId = (Integer) session().get(Constants.SESSIONLOGINID);
        if (userId != null)
            setRoleName(licenseUtils.getRolesForUserId(userId));
        LOGGER.debug("Exiting from the showForApproval method:<<<<<<<<<<>>>>>>>>>>>>>:");
        return "approve";
    }

    @Override
    public Object getModel() {
        return tl;
    }

    public void setTs(final TradeService ts) {
        this.ts = ts;
    }

    protected WorkflowService workflowService() {
        return null;
    }

    public WorkflowBean getWorkflowBean() {
        return workflowBean;
    }

    public void setWorkflowBean(final WorkflowBean workflowBean) {
        this.workflowBean = workflowBean;
    }

    @Override
    protected License license() {
        return tl;
    }
}
