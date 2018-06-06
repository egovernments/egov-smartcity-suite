/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */

package org.egov.tl.service;

import org.egov.commons.dao.InstallmentHibDao;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.tl.entity.License;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.service.es.LicenseApplicationIndexService;
import org.egov.tl.utils.LicenseNumberUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.egov.infra.reporting.engine.ReportFormat.PDF;
import static org.egov.infra.reporting.util.ReportUtil.CONTENT_TYPES;
import static org.egov.tl.utils.Constants.AUTO;
import static org.egov.tl.utils.Constants.NEW_LIC_APPTYPE;
import static org.egov.tl.utils.Constants.STATUS_ACKNOWLEDGED;
import static org.egov.tl.utils.Constants.STATUS_ACTIVE;
import static org.egov.tl.utils.Constants.STATUS_CANCELLED;
import static org.egov.tl.utils.Constants.STATUS_COLLECTIONPENDING;
import static org.egov.tl.utils.Constants.STATUS_REJECTED;
import static org.egov.tl.utils.Constants.STATUS_UNDERWORKFLOW;
import static org.egov.tl.utils.Constants.TL_FILE_STORE_DIR;
import static org.egov.tl.utils.Constants.TRADE_LICENSE;

@Service
@Transactional(readOnly = true)
public class LicenseNewApplicationService {

    @Autowired
    private LicenseService licenseService;

    @Autowired
    private InstallmentHibDao installmentDao;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private ValidityService validityService;

    @Autowired
    private LicenseNumberUtils licenseNumberUtils;

    @Autowired
    private LicenseStatusService licenseStatusService;

    @Autowired
    private LicenseAppTypeService licenseAppTypeService;

    @Autowired
    private TradeLicenseService tradeLicenseService;

    @Autowired
    private FileStoreService fileStoreService;

    @Autowired
    private LicenseConfigurationService licenseConfigurationService;

    @Autowired
    private TradeLicenseSmsAndEmailService tradeLicenseSmsAndEmailService;

    @Autowired
    private LicenseApplicationIndexService licenseApplicationIndexService;

    @Autowired
    private LicenseCitizenPortalService licenseCitizenPortalService;

    @Autowired
    private LicenseNewApplicationProcessflowService newApplicationProcessflowService;

    @Transactional
    public License create(TradeLicense tradeLicense) {
        licenseService.processSupportDocuments(tradeLicense);
        tradeLicense.setLicenseAppType(licenseAppTypeService.getLicenseAppTypeByName(NEW_LIC_APPTYPE));
        tradeLicenseService.raiseNewDemand(tradeLicense);
        newApplicationProcessflowService.startNewApplicationProcessflow(tradeLicense);
        if (AUTO.equals(tradeLicense.getApplicationNumber())) {
            tradeLicense.setApplicationNumber(licenseNumberUtils.generateApplicationNumber());
        }
        tradeLicense.setApplicationDate(new Date());
        tradeLicense.setNewWorkflow(true);
        tradeLicense.setStatus(licenseStatusService.getLicenseStatusByCode(STATUS_ACKNOWLEDGED));
        tradeLicense.getLicensee().setLicense(tradeLicense);
        licenseService.update(tradeLicense);
        licenseApplicationIndexService.createOrUpdateLicenseApplicationIndex(tradeLicense);
        tradeLicenseSmsAndEmailService.sendSmsAndEmail(tradeLicense, tradeLicense.getWorkflowContainer().getWorkFlowAction());
        if (securityUtils.currentUserIsCitizen()) {
            licenseCitizenPortalService.onCreate(tradeLicense);
        }
        return tradeLicense;
    }

    @Transactional
    public void saveApplication(TradeLicense tradeLicense) {
        BigDecimal currentDemandAmount = tradeLicenseService.recalculateLicenseFee(tradeLicense.getCurrentDemand());
        BigDecimal feematrixDmdAmt = tradeLicenseService.calculateFeeAmount(tradeLicense);
        if (feematrixDmdAmt.compareTo(currentDemandAmount) >= 0) {
            tradeLicenseService.updateDemandForChangeTradeArea(tradeLicense);
        }
        licenseService.processSupportDocuments(tradeLicense);
        licenseService.update(tradeLicense);
    }

    @Transactional
    public void forwardApplication(TradeLicense tradeLicense) {
        licenseService.processSupportDocuments(tradeLicense);
        updateDemand(tradeLicense);
        newApplicationProcessflowService.processForward(tradeLicense);
        tradeLicense.setStatus(licenseStatusService.getLicenseStatusByCode(STATUS_UNDERWORKFLOW));
        licenseService.update(tradeLicense);
        licenseApplicationIndexService.createOrUpdateLicenseApplicationIndex(tradeLicense);
        licenseCitizenPortalService.onUpdate(tradeLicense);
        tradeLicenseSmsAndEmailService.sendSmsAndEmail(tradeLicense, tradeLicense.getWorkflowContainer().getWorkFlowAction());
    }

    @Transactional
    public void approveApplication(TradeLicense tradeLicense) {
        licenseService.processSupportDocuments(tradeLicense);
        updateDemand(tradeLicense);
        newApplicationProcessflowService.processApprove(tradeLicense);
        if (isBlank(tradeLicense.getLicenseNumber())) {
            tradeLicense.setLicenseNumber(licenseNumberUtils.generateLicenseNumber());
        }
        tradeLicense.setApprovedBy(securityUtils.getCurrentUser());
        if (!tradeLicense.isCollectionPending() && !licenseConfigurationService.digitalSignEnabled()) {
            tradeLicense.setActive(true);
            tradeLicense.setStatus(licenseStatusService.getLicenseStatusByCode(STATUS_ACTIVE));
            FileStoreMapper fileStore = fileStoreService
                    .store(tradeLicenseService.generateLicenseCertificate(tradeLicense, false).getReportOutputData(),
                            tradeLicense.generateCertificateFileName() + ".pdf", CONTENT_TYPES.get(PDF), TL_FILE_STORE_DIR);
            tradeLicense.setCertificateFileId(fileStore.getFileStoreId());
            validityService.applyLicenseValidity(tradeLicense);
        } else if (tradeLicense.isCollectionPending()) {
            tradeLicense.setStatus(licenseStatusService.getLicenseStatusByCode(STATUS_COLLECTIONPENDING));
        } else {
            tradeLicense.setStatus(licenseStatusService.getLicenseStatusByCode(STATUS_UNDERWORKFLOW));
        }
        licenseService.update(tradeLicense);
        licenseApplicationIndexService.createOrUpdateLicenseApplicationIndex(tradeLicense);
        licenseCitizenPortalService.onUpdate(tradeLicense);
        tradeLicenseSmsAndEmailService.sendSmsAndEmail(tradeLicense, tradeLicense.getWorkflowContainer().getWorkFlowAction());
    }

    @Transactional
    public void rejectApplication(TradeLicense tradeLicense) {
        licenseService.processSupportDocuments(tradeLicense);
        updateDemand(tradeLicense);
        newApplicationProcessflowService.processReject(tradeLicense);
        tradeLicense.setStatus(licenseStatusService.getLicenseStatusByCode(STATUS_REJECTED));
        licenseService.update(tradeLicense);
        licenseApplicationIndexService.createOrUpdateLicenseApplicationIndex(tradeLicense);
        licenseCitizenPortalService.onUpdate(tradeLicense);
        tradeLicenseSmsAndEmailService.sendSmsAndEmail(tradeLicense, tradeLicense.getWorkflowContainer().getWorkFlowAction());
    }

    @Transactional
    public void cancelApplication(TradeLicense tradeLicense) {
        licenseService.processSupportDocuments(tradeLicense);
        newApplicationProcessflowService.processCancellation(tradeLicense);
        tradeLicense.setStatus(licenseStatusService.getLicenseStatusByCode(STATUS_CANCELLED));
        licenseService.update(tradeLicense);
        licenseApplicationIndexService.createOrUpdateLicenseApplicationIndex(tradeLicense);
        licenseCitizenPortalService.onUpdate(tradeLicense);
        tradeLicenseSmsAndEmailService.sendSmsAndEmail(tradeLicense, tradeLicense.getWorkflowContainer().getWorkFlowAction());
    }

    @Transactional
    public License generateCertificateForDigiSign(TradeLicense tradeLicense) {
        licenseService.processSupportDocuments(tradeLicense);
        FileStoreMapper fileStore = fileStoreService
                .store(tradeLicenseService.generateLicenseCertificate(tradeLicense, false).getReportOutputData(),
                        tradeLicense.generateCertificateFileName() + ".pdf", CONTENT_TYPES.get(PDF), TL_FILE_STORE_DIR);
        tradeLicense.setCertificateFileId(fileStore.getFileStoreId());
        licenseService.update(tradeLicense);
        return tradeLicense;
    }

    public void digiSignTransition(TradeLicense tradeLicense) {
        newApplicationProcessflowService.processDigiSign(tradeLicense);
        tradeLicense.setStatus(licenseStatusService.getLicenseStatusByCode(STATUS_ACTIVE));
        tradeLicense.setActive(true);
        validityService.applyLicenseValidity(tradeLicense);
        licenseService.update(tradeLicense);
        licenseCitizenPortalService.onUpdate(tradeLicense);
        tradeLicenseSmsAndEmailService.sendSMsAndEmailOnDigitalSign(tradeLicense);
        licenseApplicationIndexService.createOrUpdateLicenseApplicationIndex(tradeLicense);
    }

    private void updateDemand(TradeLicense tradeLicense) {
        BigDecimal currentDemandAmount = tradeLicenseService.recalculateLicenseFee(tradeLicense.getCurrentDemand());
        BigDecimal feematrixDmdAmt = tradeLicenseService.calculateFeeAmount(tradeLicense);
        if (feematrixDmdAmt.compareTo(currentDemandAmount) >= 0) {
            tradeLicenseService.updateDemandForChangeTradeArea(tradeLicense);
        }
        tradeLicense.setCollectionPending(!tradeLicense.isPaid());
    }

    public boolean validateCommencementDate(TradeLicense tradeLicense) {
        Date fromRange = installmentDao.getInsatllmentByModuleForGivenDate(
                moduleService.getModuleByName(TRADE_LICENSE), new Date()).getFromDate();
        Date toRange = installmentDao.getInsatllmentByModuleForGivenDate(
                moduleService.getModuleByName(TRADE_LICENSE), new DateTime().plusYears(1).toDate()).getToDate();

        return tradeLicense.getCommencementDate().before(fromRange) || tradeLicense.getCommencementDate().after(toRange);
    }

}