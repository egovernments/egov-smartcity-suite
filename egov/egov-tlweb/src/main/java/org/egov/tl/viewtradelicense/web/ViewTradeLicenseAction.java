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
package org.egov.tl.viewtradelicense.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.web.struts.annotation.ValidationErrorPageExt;
import org.egov.infra.workflow.service.WorkflowService;
import org.egov.tl.domain.entity.License;
import org.egov.tl.domain.entity.LicenseStatus;
import org.egov.tl.domain.entity.TradeLicense;
import org.egov.tl.domain.entity.WorkflowBean;
import org.egov.tl.domain.service.BaseLicenseService;
import org.egov.tl.domain.service.TradeService;
import org.egov.tl.domain.web.BaseLicenseAction;
import org.egov.tl.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;

@ParentPackage("egov")
@Result(name = "auditReport", type = "redirect", location = "auditReport", params = { "moduleName", "TL", "namespace",
        "/egi/auditing", "method", "searchForm", "actionName", "auditReport", "prependServletContext", "false" })
public class ViewTradeLicenseAction extends BaseLicenseAction implements ServletRequestAware {
    private static final Logger LOGGER = Logger.getLogger(ViewTradeLicenseAction.class);
    private static final long serialVersionUID = 1L;
    private TradeService ts;
    protected TradeLicense tradeLicense = new TradeLicense();
    // private DocumentManagerService<Notice> documentManagerService;
    private String rejectreason;
    private HttpSession session = null;
    private HttpServletRequest requestObj;
    private Long userId;
    private final String CITIZENUSER = "citizenUser";
    @Autowired
    private UserService userService;

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
    public Object getModel() {
        return tradeLicense;

    }

    @SuppressWarnings("rawtypes")
    public void setTradeLicenseWorkflowService(final WorkflowService tradeLicenseWorkflowService) {
    }

    public void setTs(final TradeService ts) {
        this.ts = ts;
    }

    @Override
    public String showForApproval() {
        LOGGER.debug("Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + tradeLicense);
        tradeLicense = (TradeLicense) persistenceService.find("from TradeLicense where id=?", license().getId());
        LOGGER.debug("Exiting from the showForApproval method:<<<<<<<<<<>>>>>>>>>>>>>:");
        return super.showForApproval();
    }

    public String view() {
        LOGGER.debug("Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + tradeLicense);
        tradeLicense = (TradeLicense) persistenceService.find("from TradeLicense where id=?", tradeLicense.getId());
        LOGGER.debug("Exiting from the view method:<<<<<<<<<<>>>>>>>>>>>>>:");
        return Constants.VIEW;
    }

    public String viewCitizen() {
        LOGGER.debug("Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + tradeLicense);
        session = requestObj.getSession();
        final User user = userService.getUserByUsername(CITIZENUSER);
        userId = user.getId();
        EgovThreadLocals.setUserId(userId);
        session.setAttribute("com.egov.user.LoginUserName", user.getName());
        tradeLicense = (TradeLicense) persistenceService.find("from TradeLicense where id=?", tradeLicense.getId());
        LOGGER.debug("Exiting from the view Citizen method:<<<<<<<<<<>>>>>>>>>>>>>:");
        return Constants.VIEW;
    }

    public String generateCertificate() {
        LOGGER.debug("Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + tradeLicense);
        String certificate = Constants.CNCCERTIFICATE;
        setLicenseIdIfServletRedirect();

        tradeLicense = (TradeLicense) persistenceService.find("from TradeLicense where id=?", tradeLicense.getId());
        /*
         * if (this.documentManagerService.getDocumentObject(this.tradeLicense.getApplicationNumber(), "egtradelicense") == null)
         * { ViewTradeLicenseAction.LOGGER.debug("Creating Certificate object for DMS"); final Notice notice = new Notice();
         * notice.setDocumentNumber(this.tradeLicense.getApplicationNumber());
         * notice.setDomainName(EGOVThreadLocals.getDomainName()); notice.setModuleName("egtradelicense");
         * notice.setNoticeType(this.license().getClass().getSimpleName() + "-Certificate"); notice.setNoticeDate(new Date());
         * this.request.put("noticeObject", notice); }
         */
        tradeLicense.setIsCertificateGenerated(true);
        if (tradeLicense.getFeeTypeStr().equalsIgnoreCase(Constants.PFA))
            certificate = Constants.PFACERTIFICATE;
        else if (tradeLicense.getFeeTypeStr().equalsIgnoreCase(Constants.CNC))
            certificate = Constants.CNCCERTIFICATE;
        LOGGER.debug("Exiting from the generateCertificate method:<<<<<<<<<<>>>>>>>>>>>>>:");
        return certificate;
    }

    public String generateNoc() {
        LOGGER.debug("Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + tradeLicense);
        setLicenseIdIfServletRedirect();
        tradeLicense = (TradeLicense) persistenceService.find("from TradeLicense where id=?", tradeLicense.getId());
        LOGGER.debug("Exiting from the generate NOC method:<<<<<<<<<<>>>>>>>>>>>>>:");
        return "noc";
    }

    @SuppressWarnings("unchecked")
    public String createNoc() {
        LOGGER.debug("Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + tradeLicense);
        persistenceService.setType(TradeLicense.class);
        setLicenseIdIfServletRedirect();
        final TradeLicense modifiedTL = tradeLicense;
        tradeLicense = (TradeLicense) persistenceService.findById(modifiedTL.getId(), false);
        tradeLicense.setSandBuckets(modifiedTL.getSandBuckets());
        tradeLicense.setWaterBuckets(modifiedTL.getWaterBuckets());
        tradeLicense.setDcpExtinguisher(modifiedTL.getDcpExtinguisher());
        final String runningNumber = service().getNextRunningLicenseNumber(Constants.TL_PROVISIONAL_NOC_NUMBER);
        tradeLicense.generateNocNumber(runningNumber);
        // this.service().endWorkFlowForLicense(tradeLicense);
        final LicenseStatus activeStatus = (LicenseStatus) persistenceService
                .find("from org.egov.tl.domain.entity.LicenseStatus where code='ACT'");
        tradeLicense.setStatus(activeStatus);
        persistenceService.update(tradeLicense);
        LOGGER.debug("Exiting from the generate NOC method:<<<<<<<<<<>>>>>>>>>>>>>:");
        return "createnoc";
    }

    public String duplicateNoc()
    {
        return "duplicatenoc";
    }

    public String generateDuplicateNoc()
    {
        setLicenseIdIfServletRedirect();
        tradeLicense = (TradeLicense) persistenceService.find("from TradeLicense where id=?", tradeLicense.getId());
        return "createnoc";
    }

    private void setLicenseIdIfServletRedirect() {
        LOGGER.debug("Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + tradeLicense);
        if (tradeLicense.getId() == null)
            /**
             * TODO -- Fix this
             */
            if (getSession().get("model.id") != null) {
                /*
                 * this.tradeLicense.setId(Long.valueOf((Long) this.getSession().get("model.id")));
                 * this.getSession().remove("model.id");
                 */
            }
        LOGGER.debug("Exiting from the setLicenseIdIfServletRedirect method:<<<<<<<<<<>>>>>>>>>>>>>:");
    }

    @SkipValidation
    public String generateRejCertificate() {
        LOGGER.debug("Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + tradeLicense);
        setLicenseIdIfServletRedirect();
        tradeLicense = (TradeLicense) persistenceService.find("from TradeLicense where id=?", tradeLicense.getId());
        LOGGER.debug("Exiting from the generateRejCertificate method:<<<<<<<<<<>>>>>>>>>>>>>:");
        return "rejCertificate";
    }

    @SkipValidation
    public String certificateForRej() {
        LOGGER.debug("Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + tradeLicense);
        getSession().get("model.id");
        tradeLicense = (TradeLicense) persistenceService.find("from TradeLicense where id=?", license().getId());
        LOGGER.debug("Exiting from the certificateForRej method:<<<<<<<<<<>>>>>>>>>>>>>:");
        return "certificateForRej";
    }

    public String duplicateCertificate() {
        return "duplicate";
    }

    @Override
    protected License license() {
        return tradeLicense;
    }

    @Override
    @SkipValidation
    @ValidationErrorPageExt(action = "approve", makeCall = true, toMethod = "setupWorkflowDetails")
    public String approve() {
        LOGGER.debug("Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + tradeLicense);
        final Integer userId = (Integer) session().get(Constants.SESSIONLOGINID);
        if (userId != null)
            setRoleName(licenseUtils.getRolesForUserId(userId));
        final String docNumber = tradeLicense.getDocNumber();
        tradeLicense = (TradeLicense) persistenceService.find("from TradeLicense where id=?", tradeLicense.getId());
        tradeLicense.setDocNumber(docNumber);
        LOGGER.debug("Exiting from the approve method:<<<<<<<<<<>>>>>>>>>>>>>:");
        return super.approve();
    }

    @Override
    @SkipValidation
    @ValidationErrorPageExt(
            action = "approveRenew", makeCall = true, toMethod = "setupWorkflowDetails")
    public String approveRenew() {
        LOGGER.debug("Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + tradeLicense);
        final Integer userId = (Integer) session().get(Constants.SESSIONLOGINID);
        if (userId != null)
            setRoleName(licenseUtils.getRolesForUserId(userId));
        tradeLicense = (TradeLicense) persistenceService.find("from TradeLicense where id=?", tradeLicense.getId());
        LOGGER.debug("Exiting from the approveRenew method:<<<<<<<<<<>>>>>>>>>>>>>:");
        return super.approveRenew();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected BaseLicenseService service() {
        ts.getPersistenceService().setType(TradeLicense.class);
        return ts;
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

}
