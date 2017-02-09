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

package org.egov.tl.web.actions.viewtradelicense;

import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.eis.entity.Assignment;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.reporting.viewer.ReportViewerUtil;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.web.struts.annotation.ValidationErrorPageExt;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.entity.WorkflowBean;
import org.egov.tl.service.AbstractLicenseService;
import org.egov.tl.service.TradeLicenseService;
import org.egov.tl.utils.Constants;
import org.egov.tl.web.actions.BaseLicenseAction;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

@ParentPackage("egov")
@Results({@Result(name = "report", location = "viewTradeLicense-report.jsp"),
        @Result(name = "message", location = "viewTradeLicense-message.jsp")})
public class ViewTradeLicenseAction extends BaseLicenseAction<TradeLicense> {
    private static final long serialVersionUID = 1L;

    protected TradeLicense tradeLicense = new TradeLicense();
    protected String reportId;
    private String applicationNo;
    private Long licenseid;

    @Autowired
    private ReportService reportService;
    @Autowired
    private TradeLicenseService tradeLicenseService;
    @Autowired
    private ReportViewerUtil reportViewerUtil;

    @Override
    public TradeLicense getModel() {
        return tradeLicense;

    }

    @Override
    @Action(value = "/viewtradelicense/viewTradeLicense-showForApproval")
    public String showForApproval() throws IOException {
        tradeLicense = tradeLicenseService.getLicenseById(license().getId());
        return super.showForApproval();
    }

    @Actions({
            @Action(value = "/viewtradelicense/viewTradeLicense-view"),
            @Action(value = "/public/viewtradelicense/viewTradeLicense-view")
    })
    public String view() {
        if (license() != null && license().getId() != null)
            tradeLicense = tradeLicenseService.getLicenseById(license().getId());
        else if (applicationNo != null && !applicationNo.isEmpty())
            tradeLicense = tradeLicenseService.getLicenseByApplicationNumber(applicationNo);
        return Constants.VIEW;
    }

    @ValidationErrorPage("report")
    @Action(value = "/viewtradelicense/viewTradeLicense-generateCertificate")
    public String generateCertificate() {
        setLicenseIdIfServletRedirect();
        tradeLicense = tradeLicenseService.getLicenseById(license().getId());
        reportId = reportViewerUtil
                .addReportToTempCache(reportService.createReport(tradeLicenseService.prepareReportInputData(license())));
        return "report";
    }

    private void setLicenseIdIfServletRedirect() {
        if (tradeLicense.getId() == null)
            if (getSession().get("model.id") != null) {
                tradeLicense.setId(Long.valueOf((Long) getSession().get("model.id")));
                getSession().remove("model.id");
            }
    }


    @SkipValidation
    @Action(value = "/viewtradelicense/viewTradeLicense-generateRejCertificate")
    public String generateRejCertificate() {
        setLicenseIdIfServletRedirect();
        tradeLicense = tradeLicenseService.getLicenseById(license().getId());
        return "rejCertificate";
    }

    @SkipValidation
    @Action(value = "/viewtradelicense/viewTradeLicense-certificateForRej")
    public String certificateForRej() {
        getSession().get("model.id");
        tradeLicense = tradeLicenseService.getLicenseById(license().getId());
        return "certificateForRej";
    }

    @Action(value = "/viewtradelicense/viewTradeLicense-duplicateCertificate")
    public String duplicateCertificate() {
        return "duplicate";
    }

    @Override
    protected TradeLicense license() {
        return tradeLicense;
    }

    @Override
    @SkipValidation
    @ValidationErrorPageExt(action = "approve", makeCall = true, toMethod = "setupWorkflowDetails")
    public String approve() {
        setRoleName(securityUtils.getCurrentUser().getRoles().toString());
        return super.approve();
    }

    @Override
    @SkipValidation
    @ValidationErrorPageExt(action = "approveRenew", makeCall = true, toMethod = "setupWorkflowDetails")
    public String approveRenew() {
        setRoleName(securityUtils.getCurrentUser().getRoles().toString());
        tradeLicense = tradeLicenseService.getLicenseById(license().getId());
        return super.approveRenew();
    }

    @Override
    protected AbstractLicenseService<TradeLicense> licenseService() {
        return tradeLicenseService;
    }

    public WorkflowBean getWorkflowBean() {
        return workflowBean;
    }

    public void setWorkflowBean(final WorkflowBean workflowBean) {
        this.workflowBean = workflowBean;
    }

    public String getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(final String applicationNo) {
        this.applicationNo = applicationNo;
    }

    @Override
    public String getReportId() {
        return reportId;
    }


    @Action(value = "/viewtradelicense/viewTradeLicense-closure")
    public String viewClosure() {
        if (license() != null && license().getId() != null)
            tradeLicense = tradeLicenseService.getLicenseById(license().getId());
        else if (applicationNo != null && !applicationNo.isEmpty())
            tradeLicense = tradeLicenseService.getLicenseByApplicationNumber(applicationNo);
        return "closure";
    }

    @Action(value = "/viewtradelicense/viewTradeLicense-cancelLicense")
    public String updateLicenseClosure() {
        populateWorkflowBean();
        if (getLicenseid() != null) {
            tradeLicense = tradeLicenseService.getLicenseById(getLicenseid());
            tradeLicenseService.cancelLicenseWorkflow(tradeLicense, workflowBean);
            if (workflowBean.getWorkFlowAction().contains(Constants.BUTTONFORWARD)) {
                List<Assignment> assignments = assignmentService.getAssignmentsForPosition(workflowBean.getApproverPositionId());
                String nextDesgn = !assignments.isEmpty() ? assignments.get(0).getDesignation().getName() : "";
                final String userName = !assignments.isEmpty() ? assignments.get(0).getEmployee().getName() : "";
                addActionMessage(this.getText("license.closure.sent") + " " + nextDesgn + " - " + userName);
            } else if (workflowBean.getWorkFlowAction().contains(Constants.BUTTONREJECT)) {
                if (license().getState().getValue().contains(Constants.WORKFLOW_STATE_REJECTED)) {
                    List<Assignment> assignments = assignmentService.getAssignmentsForPosition(license().getState().getInitiatorPosition().getId());
                    final String userName = !assignments.isEmpty() ? assignments.get(0).getEmployee().getName() : "";
                    addActionMessage(this.getText("license.closure.rejectedfirst") + (" " + license().getState().getInitiatorPosition().getDeptDesig().getDesignation().getName() + " - ") + " " + userName);
                } else addActionMessage(this.getText("license.closure.rejected") + " " + license().getLicenseNumber());


            } else
                addActionMessage(this.getText("license.closure.msg") + license().getLicenseNumber());
        }
        return "message";
    }

    @Override
    public String getAdditionalRule() {
        return Constants.CLOSURE_ADDITIONAL_RULE;
    }

    public Long getLicenseid() {
        return licenseid;
    }

    public void setLicenseid(Long licenseid) {
        this.licenseid = licenseid;
    }


}
