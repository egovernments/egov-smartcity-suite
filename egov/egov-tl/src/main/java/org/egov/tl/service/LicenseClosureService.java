/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.tl.entity.License;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.repository.LicenseRepository;
import org.egov.tl.service.es.LicenseApplicationIndexService;
import org.egov.tl.utils.LicenseUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.egov.infra.utils.DateUtils.currentDateToDefaultDateFormat;
import static org.egov.tl.utils.Constants.APPLICATION_STATUS_CANCELLED;
import static org.egov.tl.utils.Constants.BUTTONAPPROVE;
import static org.egov.tl.utils.Constants.CLOSURE_ADDITIONAL_RULE;
import static org.egov.tl.utils.Constants.DELIMITER_COLON;
import static org.egov.tl.utils.Constants.LICENSE_STATUS_CANCELLED;
import static org.egov.tl.utils.Constants.WF_DIGI_SIGNED;

@Service
@Transactional(readOnly = true)
public class LicenseClosureService {

    @Autowired
    private CityService cityService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private LicenseUtils licenseUtils;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    private LicenseStatusService licenseStatusService;

    @Autowired
    private TradeLicenseService tradeLicenseService;

    @Autowired
    private TradeLicenseSmsAndEmailService tradeLicenseSmsAndEmailService;

    @Autowired
    private LicenseApplicationIndexService licenseApplicationIndexService;

    @Autowired
    private LicenseCitizenPortalService licenseCitizenPortalService;

    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<TradeLicense> licenseWorkflowService;

    public ReportOutput generateClosureEndorsementNotice(License license) {
        Map<String, Object> reportParams = new HashMap<>();
        reportParams.put("License", license);
        reportParams.put("currentDate", currentDateToDefaultDateFormat());
        reportParams.put("municipality", cityService.getMunicipalityName());
        return reportService.createReport(
                new ReportRequest("tl_closure_endorsement_notice", license, reportParams));
    }

    @Transactional
    public License approveClosure(String applicationNumber) {
        User user = securityUtils.getCurrentUser();
        License license = new License();
        if (isNotBlank(applicationNumber)) {
            license = licenseRepository.findByApplicationNumber(applicationNumber);
            DateTime currentDate = new DateTime();
            license.setActive(false);
            license.setClosed(true);
            WorkFlowMatrix wfmatrix = this.licenseWorkflowService.getWfMatrix(license.getStateType(), null,
                    null, CLOSURE_ADDITIONAL_RULE, license.getCurrentState().getValue(), null);
            license.transition().end()
                    .withSenderName(user.getUsername() + DELIMITER_COLON + user.getName())
                    .withComments(WF_DIGI_SIGNED).withStateValue(wfmatrix.getNextState())
                    .withDateInfo(currentDate.toDate()).withOwner(tradeLicenseService.getCommissionerPosition())
                    .withNextAction(wfmatrix.getNextAction());
            licenseUtils.applicationStatusChange(license, APPLICATION_STATUS_CANCELLED);
            license.setStatus(licenseStatusService.getLicenseStatusByName(LICENSE_STATUS_CANCELLED));
            licenseRepository.save(license);
            tradeLicenseSmsAndEmailService.sendSMsAndEmailOnClosure(license, BUTTONAPPROVE);
            licenseCitizenPortalService.onUpdate((TradeLicense) license);
            licenseApplicationIndexService.createOrUpdateLicenseApplicationIndex(license);
        }
        return license;
    }
}
