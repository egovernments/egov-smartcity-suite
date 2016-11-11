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

package org.egov.tl.service;

import static org.egov.tl.utils.Constants.BUTTONAPPROVE;
import static org.egov.tl.utils.Constants.BUTTONREJECT;

import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.eis.entity.Assignment;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.tl.entity.License;
import org.egov.tl.entity.LicenseAppType;
import org.egov.tl.entity.LicenseDemand;
import org.egov.tl.entity.NatureOfBusiness;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.entity.WorkflowBean;
import org.egov.tl.utils.Constants;
import org.egov.tl.utils.LicenseUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class TradeLicenseService extends AbstractLicenseService<TradeLicense> {

    @Autowired
    private TradeLicenseSmsAndEmailService tradeLicenseSmsAndEmailService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private LicenseUtils licenseUtils;

    @Autowired
    private ModuleService moduleService;

    @Override
    protected NatureOfBusiness getNatureOfBusiness() {
        return natureOfBusinessService.getNatureOfBusinessByName("Permanent");
    }

    @Override
    protected Module getModuleName() {
        return moduleService.getModuleByName("Trade License");
    }

    @Override
    protected void sendEmailAndSMS(final TradeLicense license, final String currentAction) {
        tradeLicenseSmsAndEmailService.sendSmsAndEmail(license, currentAction);
    }

    @Override
    protected Assignment getWorkflowInitiator(final TradeLicense license) {
        return assignmentService.getPrimaryAssignmentForUser(license.getCreatedBy().getId());
    }

    @Override
    protected LicenseAppType getLicenseApplicationTypeForRenew() {
        return licenseAppTypeService.getLicenseAppTypeByName(Constants.RENEWAL_LIC_APPTYPE);
    }

    @Override
    protected LicenseAppType getLicenseApplicationType() {
        return licenseAppTypeService.getLicenseAppTypeByName(Constants.NEW_LIC_APPTYPE);
    }

    @Transactional
    public void updateTradeLicense(final TradeLicense license, final WorkflowBean workflowBean) {
        licenseRepository.save(license);
        tradeLicenseSmsAndEmailService.sendSmsAndEmail(license, workflowBean.getWorkFlowAction());
        licenseApplicationIndexService.createOrUpdateLicenseApplicationIndex(license);
    }

    @Transactional
    public void updateStatusInWorkFlowProgress(TradeLicense license, final String workFlowAction) {

        final BigDecimal currentDemandAmount = recalculateLicenseFee(license.getCurrentDemand());
        final BigDecimal recalDemandAmount = calculateFeeAmount(license);
        final Assignment userAssignment = assignmentService.getPrimaryAssignmentForUser(securityUtils.getCurrentUser().getId());
        final Assignment wfInitiator = getWorkflowInitiator(license);
        if (BUTTONAPPROVE.equals(workFlowAction)) {
            if (license.getLicenseAppType() != null
                    && !license.getLicenseAppType().getName().equals(Constants.RENEWAL_LIC_APPTYPE)) {
                validityService.applyLicenseValidity(license);
                if (license.getTempLicenseNumber() == null)
                    license.setLicenseNumber(licenseNumberUtils.generateLicenseNumber());
            }
            license.setActive(true);
            if (license.getCurrentDemand().getBaseDemand().compareTo(license.getCurrentDemand().getAmtCollected()) == -1
                    || license.getCurrentDemand().getBaseDemand().compareTo(license.getCurrentDemand().getAmtCollected()) == 0)
                license = (TradeLicense) licenseUtils.applicationStatusChange(license,
                        Constants.APPLICATION_STATUS_APPROVED_CODE);
            else
                license = (TradeLicense) licenseUtils.applicationStatusChange(license,
                        Constants.APPLICATION_STATUS_SECONDCOLLECTION_CODE);

        }
        if (BUTTONAPPROVE.equals(workFlowAction) || Constants.BUTTONFORWARD.equals(workFlowAction)) {
            license.setStatus(licenseStatusService.getLicenseStatusByCode(Constants.STATUS_UNDERWORKFLOW));
            if (license.getState().getValue().equals(Constants.WF_REVENUECLERK_APPROVED))
                license = (TradeLicense) licenseUtils.applicationStatusChange(license,
                        Constants.APPLICATION_STATUS_INSPE_CODE);
            if (license.getState().getValue().equals(Constants.WORKFLOW_STATE_REJECTED))
                license = (TradeLicense) licenseUtils.applicationStatusChange(license,
                        Constants.APPLICATION_STATUS_CREATED_CODE);
        }

        if (Constants.GENERATECERTIFICATE.equals(workFlowAction)) {
            license.setStatus(licenseStatusService.getLicenseStatusByCode(Constants.STATUS_ACTIVE));
            // setting license to non-legacy, old license number will be the only tracking
            // to check a license created as legacy or new hereafter.
            license.setLegacy(false);
            license = (TradeLicense) licenseUtils.applicationStatusChange(license,
                    Constants.APPLICATION_STATUS_GENECERT_CODE);
        }
        if (BUTTONREJECT.equals(workFlowAction))
            if (license.getLicenseAppType() != null
                    && license.getLicenseAppType().getName().equals(Constants.RENEWAL_LIC_APPTYPE))
                license.setStatus(licenseStatusService.getLicenseStatusByCode(Constants.STATUS_ACTIVE));
            else if (wfInitiator.equals(userAssignment)) {
                license.setStatus(licenseStatusService.getLicenseStatusByCode(Constants.STATUS_CANCELLED));
                license = (TradeLicense) licenseUtils.applicationStatusChange(license,
                        Constants.APPLICATION_STATUS_CANCELLED);
            } else {
                license.setStatus(licenseStatusService.getLicenseStatusByCode(Constants.STATUS_REJECTED));
                license = (TradeLicense) licenseUtils.applicationStatusChange(license,
                        Constants.APPLICATION_STATUS_REJECTED);
            }
        if (null != license && null != license.getState()
                && license.getState().getValue().contains(Constants.WF_REVENUECLERK_APPROVED)
                && recalDemandAmount.compareTo(currentDemandAmount) == 1)
            updateDemandForChangeTradeArea(license);
    }

    public ReportRequest prepareReportInputData(final License license) {
        final Map<String, Object> reportParams = getReportParamsForCertificate(license, null, null);
        return new ReportRequest("licenseCertificate", license, reportParams);
    }

    public ReportOutput prepareReportInputDataForDig(final License license, final String districtName,
                                                     final String cityMunicipalityName) {
        return reportService.createReport(
                new ReportRequest("licenseCertificate", license, getReportParamsForCertificate(license, districtName,
                        cityMunicipalityName)));
    }

    private Map<String, Object> getReportParamsForCertificate(final License license, final String districtName,
                                                              final String cityMunicipalityName) {
        final Map<String, Object> reportParams = new HashMap<>();
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        final Format formatterYear = new SimpleDateFormat("YYYY");
        reportParams.put("applicationnumber", license.getApplicationNumber());
        reportParams.put("applicantName", license.getLicensee().getApplicantName());
        reportParams.put("licencenumber", license.getLicenseNumber());
        reportParams.put("wardName", license.getBoundary().getName());
        reportParams.put("cscNumber", "");
        reportParams.put("nameOfEstablishment", license.getNameOfEstablishment() != null ? license.getNameOfEstablishment() : "");
        reportParams.put("licenceAddress", license.getAddress());
        reportParams.put("municipality", cityMunicipalityName);
        reportParams.put("district", districtName);
        reportParams.put("subCategory", license.getTradeName() != null ? license.getTradeName().getName() : null);
        reportParams
                .put("appType", license.getLicenseAppType() != null
                        ? "New".equals(license.getLicenseAppType().getName())
                        ? "New Trade" : "Renewal"
                        : "New");
        if (ApplicationThreadLocals.getMunicipalityName().contains("Corporation"))
            reportParams.put("carporationulbType", Boolean.TRUE);
        reportParams.put("municipality", ApplicationThreadLocals.getMunicipalityName());
        final LicenseDemand licenseDemand = license.getLicenseDemand();
        final String startYear = formatterYear.format(licenseDemand.getEgInstallmentMaster().getFromDate());
        final String endYear = formatterYear.format(licenseDemand.getEgInstallmentMaster().getToDate());
        final String installMentYear = startYear + "-" + endYear;
        reportParams.put("installMentYear", installMentYear);
        reportParams.put("applicationdate", formatter.format(license.getApplicationDate()));
        reportParams.put("demandUpdateDate", formatter.format(license.getCurrentDemand().getModifiedDate()));
        reportParams.put("demandTotalamt", license.getCurrentLicenseFee());
        return reportParams;
    }

    public List<TradeLicense> getTradeLicenseForGivenParam(final String paramValue, final String paramType) {
        List<TradeLicense> licenseList = new ArrayList<>();
        if (paramType.equals(Constants.SEARCH_BY_APPNO))
            licenseList = entityQueryService.findAllBy("from License where upper(applicationNumber) like ?", "%"
                    + paramValue.toUpperCase() + "%");
        else if (paramType.equals(Constants.SEARCH_BY_LICENSENO))
            licenseList = entityQueryService.findAllBy("from License where  upper(licenseNumber) like ?", "%"
                    + paramValue.toUpperCase() + "%");
        else if (paramType.equals(Constants.SEARCH_BY_OLDLICENSENO))
            licenseList = entityQueryService.findAllBy("from License where  upper(oldLicenseNumber) like ?", "%"
                    + paramValue.toUpperCase() + "%");
        else if (paramType.equals(Constants.SEARCH_BY_TRADETITLE))
            licenseList = entityQueryService.findAllBy("from License where  upper(nameOfEstablishment) like ?",
                    "%" + paramValue.toUpperCase() + "%");
        else if (paramType.equals(Constants.SEARCH_BY_TRADEOWNERNAME))
            licenseList = entityQueryService.findAllBy(
                    "from License where  upper(licensee.applicantName) like ?", "%" + paramValue.toUpperCase() + "%");
        else if (paramType.equals(Constants.SEARCH_BY_PROPERTYASSESSMENTNO))
            licenseList = entityQueryService.findAllBy("from License where  upper(assessmentNo) like ?", "%"
                    + paramValue.toUpperCase() + "%");
        else if (paramType.equals(Constants.SEARCH_BY_MOBILENO))
            licenseList = entityQueryService.findAllBy("from License where  licensee.mobilePhoneNumber like ?",
                    "%" + paramValue + "%");
        return licenseList;
    }

    public List<TradeLicense> searchTradeLicense(final String applicationNumber, final String licenseNumber,
            final String oldLicenseNumber, final Long categoryId, final Long subCategoryId, final String tradeTitle,
            final String tradeOwnerName, final String propertyAssessmentNo, final String mobileNo, final Boolean isCancelled,
            final Long statusId) {
        final Criteria searchCriteria = entityQueryService.getSession().createCriteria(TradeLicense.class);
        searchCriteria.createAlias("licensee", "licc").createAlias("category", "cat")
                .createAlias("tradeName", "subcat").createAlias("status", "licstatus");

        if (StringUtils.isNotBlank(applicationNumber))
            searchCriteria.add(Restrictions.eq("applicationNumber", applicationNumber).ignoreCase());
        if (StringUtils.isNotBlank(licenseNumber))
            searchCriteria.add(Restrictions.eq("licenseNumber", licenseNumber).ignoreCase());
        if (StringUtils.isNotBlank(oldLicenseNumber))
            searchCriteria.add(Restrictions.eq("oldLicenseNumber", oldLicenseNumber).ignoreCase());
        if (categoryId != null && categoryId != -1)
            searchCriteria.add(Restrictions.eq("cat.id", categoryId));
        if (subCategoryId != null && subCategoryId != -1)
            searchCriteria.add(Restrictions.eq("subcat.id", subCategoryId));
        if (tradeTitle != null && !tradeTitle.isEmpty())
            searchCriteria.add(Restrictions.eq("nameOfEstablishment", tradeTitle).ignoreCase());
        if (StringUtils.isNotBlank(tradeOwnerName))
            searchCriteria.add(Restrictions.eq("licc.applicantName", tradeOwnerName).ignoreCase());
        if (StringUtils.isNotBlank(propertyAssessmentNo))
            searchCriteria.add(Restrictions.eq("assessmentNo", propertyAssessmentNo).ignoreCase());
        if (StringUtils.isNotBlank(mobileNo))
            searchCriteria.add(Restrictions.eq("licc.mobilePhoneNumber", mobileNo));
        if (statusId != null && statusId != -1)
            searchCriteria.add(Restrictions.eq("status.id", statusId));
        if (isCancelled != null && isCancelled.equals(Boolean.TRUE))
            searchCriteria.add(Restrictions.eq("licstatus.statusCode", StringUtils.upperCase("CAN")));
        else
            searchCriteria.add(Restrictions.ne("licstatus.statusCode", StringUtils.upperCase("CAN")));
        searchCriteria.add(Restrictions.isNotNull("applicationNumber"));
        searchCriteria.addOrder(Order.asc("id"));
        return searchCriteria.list();
    }

}