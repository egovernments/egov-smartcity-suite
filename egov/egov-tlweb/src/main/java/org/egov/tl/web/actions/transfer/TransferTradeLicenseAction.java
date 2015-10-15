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
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.tl.web.actions.transfer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.tl.domain.entity.License;
import org.egov.tl.domain.entity.Licensee;
import org.egov.tl.domain.entity.TradeLicense;
import org.egov.tl.domain.entity.WorkflowBean;
import org.egov.tl.domain.entity.transfer.LicenseTransfer;
import org.egov.tl.domain.service.BaseLicenseService;
import org.egov.tl.domain.service.TradeService;
import org.egov.tl.utils.Constants;
import org.egov.tl.web.actions.BaseLicenseAction;
import org.springframework.beans.factory.annotation.Autowired;

@ParentPackage("egov")
@Results({
@Result(name = "transfer", location = "transferTradeLicense-transfer.jsp"),
@Result(name = "message", location = "transferTradeLicense-message.jsp"),
@Result(name = Constants.EDIT, location = "transferTradeLicense-"+Constants.EDIT+".jsp"),
@Result(name = "approve", location = "transferTradeLicense-approve.jsp")
})
public class TransferTradeLicenseAction extends BaseLicenseAction {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(TransferTradeLicenseAction.class);
    private TradeService ts;
    protected TradeLicense tl = new TradeLicense();
    @Autowired
    private BoundaryService boundaryService;
    @Autowired
    private UserService userService;
    @Autowired
    private SecurityUtils securityUtils;
    private Long licenseId;

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
        final Long userId = securityUtils.getCurrentUser().getId();
        if (userId != null)
            setRoleName(licenseUtils.getRolesForUserId(userId));
        addDropdownData(Constants.DROPDOWN_ZONE_LIST, licenseUtils.getAllZone());
        addDropdownData(Constants.DROPDOWN_DIVISION_LIST_LICENSEE, new ArrayList<Boundary>());
    }

    @Override
    @SkipValidation
@Action(value="/transfer/transferTradeLicense-newForm")
    public String newForm() {
        tl = (TradeLicense) ts.getPersistenceService().find("from TradeLicense where id=?",licenseId);
       // tl.setLicenseeZoneId(Long.valueOf(tl.getLicensee().getBoundary().getParent().getId()));
        List cityZoneList = new ArrayList();
        cityZoneList = licenseUtils.getAllZone();
        tl.setLicenseZoneList(cityZoneList);
        final Boundary licenseeboundary = boundaryService.getBoundaryById(tl.getBoundary().getId());
        if (licenseeboundary!=null && licenseeboundary.getName().contains("Zone"))
            addDropdownData(Constants.DROPDOWN_DIVISION_LIST_LICENSEE, Collections.EMPTY_LIST);
        loadAjaxedDropDowns();
        return "transfer";
    }

    @Override
    @ValidationErrorPage("transfer")
    @Action(value="/transfer/transferTradeLicense-create")
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
            //ts.initiateWorkFlowForTransfer(license(), workflowBean);
            /*
             * if (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONAPPROVE)) doAuditing(AuditModule.TL,
             * AuditEntity.TL_LIC, "TRANSFER LICENSE", tl.getAuditDetails());
             */
        } catch (final ApplicationRuntimeException e) {
            throw new ValidationException(Arrays.asList(new ValidationError("license.workflow.already.Started",
                    "File is some other workflow cannot proceed with the action")));
        }
        setMessages();
        persistenceService.persist(tl);
        LOGGER.debug("Exiting from the create method:<<<<<<<<<<>>>>>>>>>>>>>:");
        return "message";
    }

    @ValidationErrorPage("edit")
@Action(value="/transfer/transferTradeLicense-edit")
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
        ts.processWorkFlowForTransfer(license(), workflowBean);
        setMessages();
        LOGGER.debug("Exiting from the edit method:<<<<<<<<<<>>>>>>>>>>>>>:");
        return "message";
    }

    @SkipValidation
    @Override
    public String approve() {
        LOGGER.debug("Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + tl);
        tl = (TradeLicense) persistenceService.find("from TradeLicense where id=?", tl.getId());
        ts.processWorkFlowForTransfer(license(), workflowBean);
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
        /*if (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONSAVE)) {*/
            userService.getUserById(license().getCreatedBy().getId());
            addActionMessage(this.getText("license.transfer.submission.succesful")
                    + license().getLicenseTransfer().getOldApplicationNumber());
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
        LOGGER.debug("Exiting from the setMessages method:<<<<<<<<<<>>>>>>>>>>>>>:");
    }

    public void prepareBeforeEdit() {
        LOGGER.debug("Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + tl);
        prepareShowForApproval();
        LOGGER.debug("Exiting from the prepareBeforeEdit method:<<<<<<<<<<>>>>>>>>>>>>>:");
    }

    @SkipValidation
@Action(value="/transfer/transferTradeLicense-beforeEdit")
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
@Action(value="/transfer/transferTradeLicense-showForApproval")
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
        final Long userId = securityUtils.getCurrentUser().getId();
        if (userId != null)
            setRoleName(licenseUtils.getRolesForUserId(userId));
        LOGGER.debug("Exiting from the showForApproval method:<<<<<<<<<<>>>>>>>>>>>>>:");
        return "approve";
    }

    @Override
    public License getModel() {
        return tl;
    }

    public void setTs(final TradeService ts) {
        this.ts = ts;
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

    public Long getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(Long licenseId) {
        this.licenseId = licenseId;
    }
}