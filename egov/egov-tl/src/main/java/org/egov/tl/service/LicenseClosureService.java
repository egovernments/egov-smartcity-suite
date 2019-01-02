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

import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.service.es.LicenseApplicationIndexService;
import org.egov.tl.utils.LicenseNumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.egov.infra.reporting.engine.ReportFormat.PDF;
import static org.egov.infra.reporting.util.ReportUtil.CONTENT_TYPES;
import static org.egov.infra.utils.DateUtils.currentDateToDefaultDateFormat;
import static org.egov.tl.utils.Constants.AUTO;
import static org.egov.tl.utils.Constants.BUTTONAPPROVE;
import static org.egov.tl.utils.Constants.CLOSURE_APPTYPE_CODE;
import static org.egov.tl.utils.Constants.LICENSE_STATUS_ACKNOWLEDGED;
import static org.egov.tl.utils.Constants.LICENSE_STATUS_ACTIVE;
import static org.egov.tl.utils.Constants.LICENSE_STATUS_CANCELLED;
import static org.egov.tl.utils.Constants.LICENSE_STATUS_UNDERWORKFLOW;
import static org.egov.tl.utils.Constants.STATUS_REJECTED;
import static org.egov.tl.utils.Constants.TL_FILE_STORE_DIR;

@Service
@Transactional(readOnly = true)
public class LicenseClosureService extends LicenseService {

    @Autowired
    private CityService cityService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private FileStoreService fileStoreService;

    @Autowired
    private LicenseStatusService licenseStatusService;

    @Autowired
    private LicenseNumberUtils licenseNumberUtils;

    @Autowired
    private LicenseAppTypeService licenseAppTypeService;

    @Autowired
    private TradeLicenseSmsAndEmailService tradeLicenseSmsAndEmailService;

    @Autowired
    private LicenseApplicationIndexService licenseApplicationIndexService;

    @Autowired
    private LicenseCitizenPortalService licenseCitizenPortalService;

    @Autowired
    private LicenseClosureProcessflowService licenseClosureProcessflowService;

    @Autowired
    private DemandGenerationService demandGenerationService;

    public ReportOutput generateClosureEndorsementNotice(TradeLicense license) {
        Map<String, Object> reportParams = new HashMap<>();
        reportParams.put("License", license);
        reportParams.put("currentDate", currentDateToDefaultDateFormat());
        reportParams.put("municipality", cityService.getMunicipalityName());
        return reportService.createReport(new ReportRequest("tl_closure_endorsement_notice", license, reportParams));
    }

    @Transactional
    public TradeLicense generateClosureEndorsement(TradeLicense license) {
        FileStoreMapper fileStore = fileStoreService
                .store(generateClosureEndorsementNotice(license).asInputStream(),
                        license.generateCertificateFileName() + ".pdf", CONTENT_TYPES.get(PDF), TL_FILE_STORE_DIR);
        license.setDigiSignedCertFileStoreId(fileStore.getFileStoreId());
        processSupportDocuments(license);
        update(license);
        return license;
    }

    @Transactional
    public TradeLicense approveClosure(String applicationNumber) {
        TradeLicense license = getLicenseByApplicationNumber(applicationNumber);
        license.setActive(false);
        license.setClosed(true);
        license.setStatus(licenseStatusService.getLicenseStatusByName(LICENSE_STATUS_CANCELLED));
        licenseClosureProcessflowService.processApproval(license);
        update(license);
        demandGenerationService.markDemandGenerationLogAsCompleted(license, LICENSE_STATUS_CANCELLED);
        licenseApplicationIndexService.createOrUpdateLicenseApplicationIndex(license);
        licenseCitizenPortalService.onUpdate(license);
        tradeLicenseSmsAndEmailService.sendLicenseClosureMessage(license, BUTTONAPPROVE);
        return license;
    }

    @Transactional
    public TradeLicense createClosure(TradeLicense license) {
        processSupportDocuments(license);
        licenseClosureProcessflowService.startClosureProcessflow(license);
        if (AUTO.equals(license.getApplicationNumber()))
            license.setApplicationNumber(licenseNumberUtils.generateApplicationNumber());
        license.setNewWorkflow(true);
        license.setApplicationDate(new Date());
        license.setStatus(licenseStatusService.getLicenseStatusByName(LICENSE_STATUS_ACKNOWLEDGED));
        license.setLicenseAppType(licenseAppTypeService.getLicenseAppTypeByCode(CLOSURE_APPTYPE_CODE));
        update(license);
        licenseApplicationIndexService.createOrUpdateLicenseApplicationIndex(license);
        tradeLicenseSmsAndEmailService.sendLicenseClosureMessage(license, license.getWorkflowContainer().getWorkFlowAction());
        if (securityUtils.currentUserIsCitizen())
            licenseCitizenPortalService.onCreate(license);
        return license;
    }

    @Transactional
    public void cancelClosure(TradeLicense license) {
        if (license.getState().getExtraInfo() != null)
            license.setLicenseAppType(licenseAppTypeService.getLicenseAppTypeByName(license.extraInfo().getOldAppType()));
        license.setStatus(licenseStatusService.getLicenseStatusByName(LICENSE_STATUS_ACTIVE));
        processSupportDocuments(license);
        licenseClosureProcessflowService.processCancellation(license);
        update(license);
        licenseApplicationIndexService.createOrUpdateLicenseApplicationIndex(license);
        licenseCitizenPortalService.onUpdate(license);
    }

    @Transactional
    public void rejectClosure(TradeLicense license) {
        processSupportDocuments(license);
        license.setStatus(licenseStatusService.getLicenseStatusByCode(STATUS_REJECTED));
        licenseClosureProcessflowService.processRejection(license);
        update(license);
        licenseApplicationIndexService.createOrUpdateLicenseApplicationIndex(license);
        licenseCitizenPortalService.onUpdate(license);
    }

    @Transactional
    public void forwardClosure(TradeLicense license) {
        processSupportDocuments(license);
        license.setStatus(licenseStatusService.getLicenseStatusByName(LICENSE_STATUS_UNDERWORKFLOW));
        licenseClosureProcessflowService.processForward(license);
        update(license);
        licenseApplicationIndexService.createOrUpdateLicenseApplicationIndex(license);
        licenseCitizenPortalService.onUpdate(license);
    }

}
