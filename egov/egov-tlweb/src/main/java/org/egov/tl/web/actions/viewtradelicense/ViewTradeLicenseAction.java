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

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.reporting.viewer.ReportViewerUtil;
import org.egov.infra.web.struts.annotation.ValidationErrorPageExt;
import org.egov.tl.entity.LicenseStatus;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.entity.WorkflowBean;
import org.egov.tl.service.AbstractLicenseService;
import org.egov.tl.service.TradeLicenseService;
import org.egov.tl.utils.Constants;
import org.egov.tl.web.actions.BaseLicenseAction;
import org.springframework.beans.factory.annotation.Autowired;

@ParentPackage("egov")
@Results({
        @Result(name = "auditReport", type = "redirectAction", location = "auditReport", params = { "moduleName", "TL",
                "namespace",
                "/egi/auditing", "method", "searchForm", "actionName", "auditReport", "prependServletContext", "false" }),
        @Result(name = "duplicate", location = "viewTradeLicense-duplicate.jsp"),
        @Result(name = Constants.CNCCERTIFICATE, location = "viewTradeLicense-" + Constants.CNCCERTIFICATE + ".jsp"),
        @Result(name = Constants.PFACERTIFICATE, location = "viewTradeLicense-" + Constants.PFACERTIFICATE + ".jsp"),
        @Result(name = "report", location = "viewTradeLicense-report.jsp")
})
public class ViewTradeLicenseAction extends BaseLicenseAction<TradeLicense> implements ServletRequestAware {
    private static final long serialVersionUID = 1L;

    private final String CITIZENUSER = "citizenUser";
    protected TradeLicense tradeLicense = new TradeLicense();
    private String rejectreason;
    private HttpSession session;
    private HttpServletRequest requestObj;
    protected String reportId;
    private String applicationNo;
    private Long userId;

    @Autowired
    private ReportService reportService;
    @Autowired
    private TradeLicenseService tradeLicenseService;
    @Autowired
    private ReportViewerUtil reportViewerUtil;

    /**
     * @return the rejectreason
     */
    public String getRejectreason() {
        return rejectreason;
    }

    /**
     * @param rejectreason the rejectreason to set
     */
    public void setRejectreason(final String rejectreason) {
        this.rejectreason = rejectreason;
    }

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

    @Action(value = "/viewtradelicense/viewTradeLicense-view")
    public String view() {
        if (license() != null && license().getId() != null)
            tradeLicense = tradeLicenseService.getLicenseById(license().getId());
        else if (applicationNo != null && !applicationNo.isEmpty())
            tradeLicense = tradeLicenseService.getLicenseByApplicationNumber(applicationNo);
        return Constants.VIEW;
    }

    @Action(value = "/viewtradelicense/viewTradeLicense-viewCitizen")
    public String viewCitizen() {
        session = requestObj.getSession();
        final User user = userService.getUserByUsername(CITIZENUSER);
        userId = user.getId();
        ApplicationThreadLocals.setUserId(userId);
        session.setAttribute("com.egov.user.LoginUserName", user.getName());
        tradeLicense = tradeLicenseService.getLicenseById(license().getId());
        return Constants.VIEW;
    }

    @Action(value = "/viewtradelicense/viewTradeLicense-generateCertificate")
    public String generateCertificate() {
        setLicenseIdIfServletRedirect();
        tradeLicense = tradeLicenseService.getLicenseById(license().getId());
        /*
         * if (this.documentManagerService.getDocumentObject(this.tradeLicense.getApplicationNumber(), "egtradelicense") == null)
         * { ViewTradeLicenseAction.LOGGER.debug("Creating Certificate object for DMS"); final Notice notice = new Notice();
         * notice.setDocumentNumber(this.tradeLicense.getApplicationNumber());
         * notice.setDomainName(EGOVThreadLocals.getDomainName()); notice.setModuleName("egtradelicense");
         * notice.setNoticeType(this.license().getClass().getSimpleName() + "-Certificate"); notice.setNoticeDate(new Date());
         * this.request.put("noticeObject", notice); }
         */
        reportId = reportViewerUtil
                .addReportToTempCache(reportService.createReport(tradeLicenseService.prepareReportInputData(license())));
        return "report";
    }

    public String generateNoc() {
        setLicenseIdIfServletRedirect();
        tradeLicense = tradeLicenseService.getLicenseById(tradeLicense.getId());
        return "noc";
    }

    public String createNoc() {
        setLicenseIdIfServletRedirect();
        final TradeLicense modifiedTL = tradeLicense;
        tradeLicense = tradeLicenseService.getLicenseById(license().getId());
        tradeLicense.setSandBuckets(modifiedTL.getSandBuckets());
        tradeLicense.setWaterBuckets(modifiedTL.getWaterBuckets());
        tradeLicense.setDcpExtinguisher(modifiedTL.getDcpExtinguisher());
        final String runningNumber = tradeLicenseService.getNextRunningLicenseNumber(Constants.TL_PROVISIONAL_NOC_NUMBER)
                .toString();
        tradeLicense.generateNocNumber(runningNumber);
        // this.service().endWorkFlowForLicense(tradeLicense);
        final LicenseStatus activeStatus = (LicenseStatus) persistenceService
                .find("from org.egov.tl.entity.LicenseStatus where code='ACT'");
        tradeLicense.setStatus(activeStatus);
        tradeLicenseService.licensePersitenceService().update(tradeLicense);
        return "createnoc";
    }

    public String duplicateNoc() {
        return "duplicatenoc";
    }

    public String generateDuplicateNoc() {
        setLicenseIdIfServletRedirect();
        tradeLicense = tradeLicenseService.getLicenseById(license().getId());
        return "createnoc";
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

    @Override
    @SkipValidation
    public void setServletRequest(final HttpServletRequest arg0) {
        requestObj = arg0;
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

}
