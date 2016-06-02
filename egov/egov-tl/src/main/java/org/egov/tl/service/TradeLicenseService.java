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

import org.apache.commons.lang3.StringUtils;
import org.egov.eis.entity.Assignment;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infstr.services.PersistenceService;
import org.egov.tl.entity.License;
import org.egov.tl.entity.LicenseAppType;
import org.egov.tl.entity.LicenseDemand;
import org.egov.tl.entity.LicenseStatus;
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

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.egov.tl.utils.Constants.BUTTONAPPROVE;
import static org.egov.tl.utils.Constants.BUTTONREJECT;

@Transactional(readOnly = true)
public class TradeLicenseService extends AbstractLicenseService<TradeLicense> {

    @Autowired
    private TradeLicenseSmsAndEmailService tradeLicenseSmsAndEmailService;

    @Autowired
    private TradeLicenseUpdateIndexService updateIndexService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private LicenseUtils licenseUtils;

    public TradeLicenseService(final PersistenceService<TradeLicense, Long> licensePersitenceService) {
        super(licensePersitenceService);
    }

    @Override
    protected NatureOfBusiness getNatureOfBusiness() {
        final NatureOfBusiness natureOfBusiness = (NatureOfBusiness) persistenceService
                .find("from org.egov.tl.entity.NatureOfBusiness where   name='Permanent'");
        return natureOfBusiness;
    }

    @Override
    protected Module getModuleName() {
        final Module module = (Module) persistenceService
                .find("from org.egov.infra.admin.master.entity.Module where parentModule is null and name=?",
                        "Trade License");
        return module;
    }

    @Override
    protected void sendEmailAndSMS(final TradeLicense license, final String currentAction) {
        tradeLicenseSmsAndEmailService.sendSmsAndEmail(license, currentAction);
    }

    @Override
    protected Assignment getWorkflowInitiator(final TradeLicense license) {
        final Assignment wfInitiator = assignmentService.getPrimaryAssignmentForUser(license.getCreatedBy().getId());
        return wfInitiator;
    }

    @Override
    protected LicenseAppType getLicenseApplicationTypeForRenew() {
        final LicenseAppType appType = (LicenseAppType) persistenceService
                .find("from org.egov.tl.entity.LicenseAppType where   name='Renew'");
        return appType;
    }

    @Override
    protected LicenseAppType getLicenseApplicationType() {
        final LicenseAppType appType = (LicenseAppType) persistenceService
                .find("from org.egov.tl.entity.LicenseAppType where   name='New'");
        return appType;
    }

    @Transactional
    public void updateTradeLicense(final TradeLicense license, final WorkflowBean workflowBean) {
        licensePersitenceService().persist(license);
        tradeLicenseSmsAndEmailService.sendSmsAndEmail(license, workflowBean.getWorkFlowAction());
        updateIndexService.updateTradeLicenseIndexes(license);
    }

    @Transactional
    public void updateStatusInWorkFlowProgress(TradeLicense license, final String workFlowAction) {
        if (BUTTONAPPROVE.equals(workFlowAction)) {

            if (license.getLicenseAppType() != null
                    && !license.getLicenseAppType().getName().equals(Constants.RENEWAL_LIC_APPTYPE)) {
                validityService.applyLicenseValidity(license);
                if (license.getTempLicenseNumber() == null)
                    license.generateLicenseNumber(getNextRunningLicenseNumber("egtl_license_number"));
            }
            license.setActive(true);
            license = (TradeLicense) licenseUtils.applicationStatusChange(license,
                    Constants.APPLICATION_STATUS_COLLECTION_CODE);
        }
        if (BUTTONAPPROVE.equals(workFlowAction) || Constants.BUTTONFORWARD.equals(workFlowAction)
                && license.getState().getValue().contains(Constants.WF_STATE_SANITORY_INSPECTOR_APPROVAL_PENDING)) {
            final LicenseStatus activeStatus = (LicenseStatus) persistenceService
                    .find("from org.egov.tl.entity.LicenseStatus where code='UWF'");
            license.setStatus(activeStatus);
            if (Constants.BUTTONFORWARD.equals(workFlowAction) && license.getEgwStatus() != null
                    && license.getEgwStatus().getCode().equals(Constants.APPLICATION_STATUS_CREATED_CODE))
                license = (TradeLicense) licenseUtils.applicationStatusChange(license,
                        Constants.APPLICATION_STATUS_INSPE_CODE);
        }
        if (Constants.GENERATECERTIFICATE.equals(workFlowAction)) {
            final LicenseStatus activeStatus = (LicenseStatus) persistenceService
                    .find("from org.egov.tl.entity.LicenseStatus where code='ACT'");
            license.setStatus(activeStatus);
            // setting license to non-legacy, old license number will be the only tracking
            // to check a license created as legacy or new hereafter.
            license.setLegacy(false);
            license = (TradeLicense) licenseUtils.applicationStatusChange(license,
                    Constants.APPLICATION_STATUS_GENECERT_CODE);
        }
        if (BUTTONREJECT.equals(workFlowAction)
                && license.getState().getValue().contains(Constants.WORKFLOW_STATE_REJECTED))
            if (license.getLicenseAppType() != null
                    && license.getLicenseAppType().getName().equals(Constants.RENEWAL_LIC_APPTYPE)) {
                final LicenseStatus activeStatus = (LicenseStatus) persistenceService
                        .find("from org.egov.tl.entity.LicenseStatus where code='ACT'");
                license.setStatus(activeStatus);
            } else {
                final LicenseStatus activeStatus = (LicenseStatus) persistenceService
                        .find("from org.egov.tl.entity.LicenseStatus where code='CAN'");
                license.setStatus(activeStatus);
            }
        if (null != license && null != license.getState()
                && license.getState().getValue().contains(Constants.WF_STATE_SANITORY_INSPECTOR_APPROVAL_PENDING))
            updateDemandForChangeTradeArea(license);
    }

    public ReportRequest prepareReportInputData(final License license) {
        final Map<String, Object> reportParams = getReportParamsForCertificate(license, null, null);
        return new ReportRequest("licenseCertificate", license, reportParams);
    }

    public ReportOutput prepareReportInputDataForDig(final License license, final String districtName, final String cityMunicipalityName) {
        return reportService.createReport(new ReportRequest("licenseCertificate", license, getReportParamsForCertificate(license, districtName,
                cityMunicipalityName)));
    }

    private Map<String, Object> getReportParamsForCertificate(final License license, final String districtName,
            final String cityMunicipalityName) {
        final Map<String, Object> reportParams = new HashMap<String, Object>();
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
                        ? license.getLicenseAppType().getName() != null && license.getLicenseAppType().getName().equals("New")
                                ? "New Trade" : "Renewal"
                        : "New");
        if (ApplicationThreadLocals.getMunicipalityName().contains("Corporation"))
            reportParams.put("carporationulbType", Boolean.TRUE);
        reportParams.put("municipality", ApplicationThreadLocals.getMunicipalityName());
        final LicenseDemand licenseDemand = license.getLicenseDemand();
        final String startYear = formatterYear.format(licenseDemand.getEgInstallmentMaster().getFromDate());
        final String EndYear = formatterYear.format(licenseDemand.getEgInstallmentMaster().getToDate());
        final String installMentYear = startYear + "-" + EndYear;
        reportParams.put("installMentYear", installMentYear);
        reportParams.put("applicationdate", formatter.format(license.getApplicationDate()));
        reportParams.put("demandUpdateDate", formatter.format(license.getCurrentDemand().getModifiedDate()));
        reportParams.put("demandTotalamt", license.getCurrentLicenseFee());
        return reportParams;
    }

    public List<TradeLicense> getTradeLicenseForGivenParam(final String paramValue, final String paramType) {
        List<TradeLicense> licenseList = new ArrayList<>();
        if (paramType.equals(Constants.SEARCH_BY_APPNO))
            licenseList = licensePersitenceService.findAllBy("from License where upper(applicationNumber) like ?", "%"
                    + paramValue.toUpperCase() + "%");
        else if (paramType.equals(Constants.SEARCH_BY_LICENSENO))
            licenseList = licensePersitenceService.findAllBy("from License where  upper(licenseNumber) like ?", "%"
                    + paramValue.toUpperCase() + "%");
        else if (paramType.equals(Constants.SEARCH_BY_OLDLICENSENO))
            licenseList = licensePersitenceService.findAllBy("from License where  upper(oldLicenseNumber) like ?", "%"
                    + paramValue.toUpperCase() + "%");
        else if (paramType.equals(Constants.SEARCH_BY_TRADETITLE))
            licenseList = licensePersitenceService.findAllBy("from License where  upper(nameOfEstablishment) like ?",
                    "%" + paramValue.toUpperCase() + "%");
        else if (paramType.equals(Constants.SEARCH_BY_TRADEOWNERNAME))
            licenseList = licensePersitenceService.findAllBy(
                    "from License where  upper(licensee.applicantName) like ?", "%" + paramValue.toUpperCase() + "%");
        else if (paramType.equals(Constants.SEARCH_BY_PROPERTYASSESSMENTNO))
            licenseList = licensePersitenceService.findAllBy("from License where  upper(propertyNo) like ?", "%"
                    + paramValue.toUpperCase() + "%");
        else if (paramType.equals(Constants.SEARCH_BY_MOBILENO))
            licenseList = licensePersitenceService.findAllBy("from License where  licensee.mobilePhoneNumber like ?",
                    "%" + paramValue + "%");
        return licenseList;
    }

    public List<TradeLicense> searchTradeLicense(final String applicationNumber, final String licenseNumber,
            final String oldLicenseNumber, final Long categoryId, final Long subCategoryId, final String tradeTitle,
            final String tradeOwnerName, final String propertyAssessmentNo, final String mobileNo) {
        final Criteria searchCriteria = persistenceService.getSession().createCriteria(TradeLicense.class);
        searchCriteria.createAlias("licensee", "licc").createAlias("category", "cat")
                .createAlias("tradeName", "subcat");

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
            searchCriteria.add(Restrictions.eq("propertyNo", propertyAssessmentNo).ignoreCase());
        if (StringUtils.isNotBlank(mobileNo))
            searchCriteria.add(Restrictions.eq("licc.mobilePhoneNumber", mobileNo));
        searchCriteria.add(Restrictions.isNotNull("applicationNumber"));
        searchCriteria.addOrder(Order.asc("id"));
        return searchCriteria.list();
    }
}