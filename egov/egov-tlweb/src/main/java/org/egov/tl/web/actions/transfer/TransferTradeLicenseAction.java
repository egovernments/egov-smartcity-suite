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
package org.egov.tl.web.actions.transfer;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.tl.entity.License;
import org.egov.tl.entity.Licensee;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.entity.WorkflowBean;
import org.egov.tl.entity.transfer.LicenseTransfer;
import org.egov.tl.service.AbstractLicenseService;
import org.egov.tl.service.TradeLicenseService;
import org.egov.tl.utils.Constants;
import org.egov.tl.web.actions.BaseLicenseAction;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ParentPackage("egov")
@Results({
        @Result(name = "transfer", location = "transferTradeLicense-transfer.jsp"),
        @Result(name = "message", location = "transferTradeLicense-message.jsp"),
        @Result(name = Constants.EDIT, location = "transferTradeLicense-" + Constants.EDIT + ".jsp"),
        @Result(name = "approve", location = "transferTradeLicense-approve.jsp")
})
public class TransferTradeLicenseAction extends BaseLicenseAction {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(TransferTradeLicenseAction.class);

    private TradeLicense tl = new TradeLicense();

    @Autowired
    private TradeLicenseService tradeLicenseService;

    private Long licenseId;

    public TransferTradeLicenseAction() {
        this.tl.setLicenseTransfer(new LicenseTransfer());
        this.tl.setLicensee(new Licensee());
        addRelatedEntity("licenseTransfer.boundary", Boundary.class);
    }

    public TradeLicense getTl() {
        return this.tl;
    }

    public void setTl(TradeLicense tl) {
        this.tl = tl;
    }

    @Override
    public void prepare() {
        super.prepare();
    }

    public void prepareApprove() {
    }

    @Override
    protected AbstractLicenseService licenseService() {
        return this.tradeLicenseService;
    }

    @Override
    public void prepareNewForm() {
        this.setupWorkflowDetails();
        this.setRoleName(this.securityUtils.getCurrentUser().getRoles().toString());
        this.addDropdownData(Constants.DROPDOWN_ZONE_LIST, this.licenseUtils.getAllZone());
        this.addDropdownData(Constants.DROPDOWN_DIVISION_LIST_LICENSEE, new ArrayList<Boundary>());
    }

    @Override
    @SkipValidation
    @Action(value = "/transfer/transferTradeLicense-newForm")
    public String newForm() {
        this.tl = this.tradeLicenseService.getLicenseById(this.licenseId);
        // tl.setLicenseeZoneId(Long.valueOf(tl.getLicensee().getBoundary().getParent().getId()));
        List cityZoneList = new ArrayList();
        cityZoneList = this.licenseUtils.getAllZone();
        this.tl.setLicenseZoneList(cityZoneList);
        Boundary licenseeboundary = this.boundaryService.getBoundaryById(this.tl.getBoundary().getId());
        if (licenseeboundary != null && licenseeboundary.getName().contains("Zone"))
            this.addDropdownData(Constants.DROPDOWN_DIVISION_LIST_LICENSEE, Collections.EMPTY_LIST);
        this.loadAjaxedDropDowns();
        return "transfer";
    }

    @ValidationErrorPage("transfer")
    @Action(value = "/transfer/transferTradeLicense-create")
    public String create() {
        TransferTradeLicenseAction.LOGGER.debug("Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + this.tl);
        if (this.tl.getLicenseeZoneId() != null && this.tl.getLicenseTransfer().getBoundary() == null) {
            Boundary licenseeboundary = this.boundaryService.getBoundaryById(this.tl.getLicenseeZoneId());
            this.tl.getLicenseTransfer().setBoundary(licenseeboundary);
        }
        LicenseTransfer licenseTransfer = this.tl.getLicenseTransfer();
        this.tl = this.tradeLicenseService.getLicenseById(this.tl.getId());
        this.tradeLicenseService.transferLicense(this.tl, licenseTransfer);
        try {
            //ts.initiateWorkFlowForTransfer(license(), workflowBean);
            /*
             * if (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONAPPROVE)) doAuditing(AuditModule.TL,
             * AuditEntity.TL_LIC, "TRANSFER LICENSE", tl.getAuditDetails());
             */
        } catch (ApplicationRuntimeException e) {
            throw new ValidationException(Arrays.asList(new ValidationError("license.workflow.already.Started",
                    "File is some other workflow cannot proceed with the action")));
        }
        this.setMessages();
        this.persistenceService.persist(this.tl);
        TransferTradeLicenseAction.LOGGER.debug("Exiting from the create method:<<<<<<<<<<>>>>>>>>>>>>>:");
        return "message";
    }

    @ValidationErrorPage("edit")
    @Action(value = "/transfer/transferTradeLicense-edit")
    public String edit() {
        TransferTradeLicenseAction.LOGGER.debug("Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + this.tl);
        if (this.tl.getLicenseeZoneId() != null && this.tl.getLicenseTransfer().getBoundary() == null) {
            Boundary licenseeboundary = this.boundaryService.getBoundaryById(this.tl.getLicenseeZoneId());
            this.tl.getLicenseTransfer().setBoundary(licenseeboundary);
        }
        LicenseTransfer licenseTransfer = this.tl.getLicenseTransfer();
        this.tl = this.tradeLicenseService.getLicenseById(this.tl.getId());
        licenseTransfer.setLicense(this.tl);
        this.tl.setLicenseTransfer(licenseTransfer);
        this.persistenceService.persist(this.tl);
        this.tradeLicenseService.processWorkFlowForTransfer(this.license(), this.workflowBean);
        this.setMessages();
        TransferTradeLicenseAction.LOGGER.debug("Exiting from the edit method:<<<<<<<<<<>>>>>>>>>>>>>:");
        return "message";
    }

    @SkipValidation
    @Override
    public String approve() {
        TransferTradeLicenseAction.LOGGER.debug("Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + this.tl);
        this.tl = this.tradeLicenseService.getLicenseById(this.tl.getId());
        this.tradeLicenseService.processWorkFlowForTransfer(this.license(), this.workflowBean);
        /*
         * if (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONAPPROVE)) doAuditing(AuditModule.TL,
         * AuditEntity.TL_LIC, "TRANSFER LICENSE", tl.getAuditDetails());
         */

        this.setMessages();
        TransferTradeLicenseAction.LOGGER.debug("Exiting from the approve method:<<<<<<<<<<>>>>>>>>>>>>>:");
        return "message";
    }

    private void setMessages() {
        TransferTradeLicenseAction.LOGGER.debug("Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + this.tl);
        /*if (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONSAVE)) {*/
        this.userService.getUserById(this.license().getCreatedBy().getId());
        this.addActionMessage(getText("license.transfer.submission.succesful")
                + this.license().getLicenseTransfer().getOldApplicationNumber());
        /*} else if (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONAPPROVE)) {
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
            addActionMessage(this.getText("license.transfer.certifiacte.print.complete.recorded"));*/
        TransferTradeLicenseAction.LOGGER.debug("Exiting from the setMessages method:<<<<<<<<<<>>>>>>>>>>>>>:");
    }

    public void prepareBeforeEdit() {
        TransferTradeLicenseAction.LOGGER.debug("Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + this.tl);
        this.prepareShowForApproval();
        TransferTradeLicenseAction.LOGGER.debug("Exiting from the prepareBeforeEdit method:<<<<<<<<<<>>>>>>>>>>>>>:");
    }

    @SkipValidation
    @Action(value = "/transfer/transferTradeLicense-beforeEdit")
    public String beforeEdit() {
        TransferTradeLicenseAction.LOGGER.debug("Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + this.tl);
        this.showForApproval();
        List cityZoneList = new ArrayList();
        cityZoneList = this.licenseUtils.getAllZone();
        this.tl.setLicenseZoneList(cityZoneList);
        Boundary licenseeboundary = this.boundaryService.getBoundaryById(this.tl.getLicenseTransfer().getBoundary().getId());
        if (licenseeboundary.getName().contains("Zone"))
            this.addDropdownData(Constants.DROPDOWN_DIVISION_LIST_LICENSEE, Collections.EMPTY_LIST);
        else
            this.addDropdownData(Constants.DROPDOWN_DIVISION_LIST_LICENSEE,
                    new ArrayList(this.tl.getLicenseTransfer().getBoundary().getParent().getChildren()));

        TransferTradeLicenseAction.LOGGER.debug("Exiting from the beforeEdit method:<<<<<<<<<<>>>>>>>>>>>>>:");
        return Constants.EDIT;
    }

    @Override
    public void prepareShowForApproval() {
        TransferTradeLicenseAction.LOGGER.debug("Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + this.tl);
        this.prepareNewForm();
        TransferTradeLicenseAction.LOGGER.debug("Exiting from the prepareShowForApproval method:<<<<<<<<<<>>>>>>>>>>>>>:");
    }

    @Override
    @SkipValidation
    @Action(value = "/transfer/transferTradeLicense-showForApproval")
    public String showForApproval() {
        TransferTradeLicenseAction.LOGGER.debug("Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + this.tl);
        Long id = null;
        if (this.tl.getId() == null)
            if (this.getSession().get("model.id") != null) {
                id = (Long) this.getSession().get("model.id");
                this.getSession().remove("model.id");
            } else
                id = this.tl.getId();
        this.tl = this.tradeLicenseService.getLicenseById(id);
        System.out.println(this.tl.getLicenseTransfer().getBoundary().getId());
        System.out.println(this.tl.getLicenseTransfer().getBoundary().getName());
        this.loadAjaxedDropDowns();
        this.setRoleName(this.securityUtils.getCurrentUser().getRoles().toString());
        TransferTradeLicenseAction.LOGGER.debug("Exiting from the showForApproval method:<<<<<<<<<<>>>>>>>>>>>>>:");
        return "approve";
    }

    @Override
    public License getModel() {
        return this.tl;
    }

    public WorkflowBean getWorkflowBean() {
        return this.workflowBean;
    }

    public void setWorkflowBean(WorkflowBean workflowBean) {
        this.workflowBean = workflowBean;
    }

    @Override
    protected TradeLicense license() {
        return this.tl;
    }

    public Long getLicenseId() {
        return this.licenseId;
    }

    public void setLicenseId(Long licenseId) {
        this.licenseId = licenseId;
    }
}