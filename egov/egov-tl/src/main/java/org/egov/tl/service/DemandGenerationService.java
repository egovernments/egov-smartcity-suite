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

import org.egov.commons.CFinancialYear;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentDao;
import org.egov.commons.service.CFinancialYearService;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.config.core.EnvironmentSettings;
import org.egov.infra.exception.ApplicationValidationException;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.tl.entity.DemandGenerationLog;
import org.egov.tl.entity.DemandGenerationLogDetail;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.entity.contracts.DemandGenerationRequest;
import org.egov.tl.utils.LicenseUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.egov.infra.persistence.utils.PersistenceUtils.flushBatchUpdate;
import static org.egov.tl.entity.enums.ProcessStatus.COMPLETED;
import static org.egov.tl.entity.enums.ProcessStatus.INCOMPLETE;

@Service
@Transactional(readOnly = true)
public class DemandGenerationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemandGenerationService.class);
    private static final String SUCCESSFUL = "Successful";
    private static final String ERROR_MSG = "Error occurred while generating demand for license {}";

    @Autowired
    private DemandGenerationLogService demandGenerationLogService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CFinancialYearService financialYearService;

    @Autowired
    private InstallmentDao installmentDao;

    @Autowired
    @Qualifier("tradeLicenseService")
    private TradeLicenseService licenseService;

    @Autowired
    private LicenseRenewalNotificationService renewalNotificationService;

    @Autowired
    private LicenseUtils licenseUtils;

    private int batchSize;

    @Autowired
    public DemandGenerationService(EnvironmentSettings environmentSettings) {
        this.batchSize = environmentSettings.getBatchUpdateSize();
    }

    @Transactional
    public DemandGenerationLog getDemandGenerationLog(CFinancialYear financialYear) {
        DemandGenerationLog demandGenerationLog = demandGenerationLogService
                .getDemandGenerationLogByInstallmentYear(financialYear.getFinYearRange());
        if (demandGenerationLog == null) {
            DemandGenerationLog previousDemandGenLog = demandGenerationLogService
                    .getPreviousInstallmentDemandGenerationLog(financialYear.getFinYearRange());
            if (previousDemandGenLog != null && previousDemandGenLog.getDemandGenerationStatus().equals(INCOMPLETE))
                demandGenerationLog = previousDemandGenLog;
            else
                demandGenerationLog = createDemandGenerationLog(financialYear);
        }
        return demandGenerationLog;
    }

    @Transactional
    public DemandGenerationLog createDemandGenerationLog(CFinancialYear financialYear) {
        if (!installmentYearValidForDemandGeneration(financialYear))
            throw new ApplicationValidationException("TL-006");

        return demandGenerationLogService.createDemandGenerationLog(financialYear.getFinYearRange());
    }

    @Transactional
    public DemandGenerationLog updateDemandGenerationLog(CFinancialYear financialYear) {
        return demandGenerationLogService.completeDemandGenerationLog(demandGenerationLogService
                .getDemandGenerationLogByInstallmentYear(financialYear.getFinYearRange()));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, timeout = 7200)
    public Collection<DemandGenerationLogDetail> generateDemand(DemandGenerationRequest demandGenerationRequest) {
        DemandGenerationLog demandGenerationLog = demandGenerationLogService
                .getDemandGenerationLogByInstallmentYear(demandGenerationRequest.getInstallmentYear());
        List<DemandGenerationLogDetail> demandGenerationLogDetails = new ArrayList<>();
        if (demandGenerationLog != null && !demandGenerationRequest.getLicenseIds().isEmpty()) {
            Module module = licenseUtils.getModule();
            CFinancialYear financialYear = financialYearService.getFinacialYearByYearRange(demandGenerationRequest.getInstallmentYear());
            Installment installment = installmentDao.getInsatllmentByModuleForGivenDate(module, financialYear.getStartingDate());
            int batchUpdateCount = 0;
            for (Long licenseId : demandGenerationRequest.getLicenseIds()) {
                TradeLicense license = licenseService.getLicenseById(licenseId);
                DemandGenerationLogDetail demandGenerationLogDetail = demandGenerationLogService.
                        createOrGetDemandGenerationLogDetail(demandGenerationLog, license);
                try {
                    if (!installment.equals(license.getCurrentDemand().getEgInstallmentMaster())) {
                        licenseService.raiseDemand(license.getId(), module, installment);
                        demandGenerationLogDetail.setDetail(SUCCESSFUL);
                        renewalNotificationService.notifyLicenseRenewal(license, installment);
                    }
                    demandGenerationLogDetail.setStatus(COMPLETED);
                    demandGenerationLogService.completeDemandGenerationLogDetail(demandGenerationLogDetail);
                } catch (RuntimeException e) {
                    LOGGER.warn(ERROR_MSG, license.getLicenseNumber(), e);
                    demandGenerationLogService.updateDemandGenerationLogDetailOnException(demandGenerationLog, demandGenerationLogDetail, e);
                }
                flushBatchUpdate(entityManager, ++batchUpdateCount, batchSize);
                demandGenerationLogDetails.add(demandGenerationLogDetail);
            }
        }

        return demandGenerationLogDetails;
    }

    @Transactional
    public boolean generateLicenseDemand(Long licenseId, boolean forPrevYear) {
        boolean generationSuccess = true;
        try {
            TradeLicense license = licenseService.getLicenseById(licenseId);
            Installment installment = installmentDao.getInsatllmentByModuleForGivenDate(licenseUtils.getModule(),
                    forPrevYear ? new DateTime(license.getDemand().getEgInstallmentMaster().getToDate()).plusDays(1).toDate()
                            : new DateTime().withMonthOfYear(4).withDayOfMonth(1).toDate());
            licenseService.raiseDemand(license.getId(), licenseUtils.getModule(), installment);
            markDemandGenerationLogAsCompleted(license, SUCCESSFUL);
            renewalNotificationService.notifyLicenseRenewal(license, installment);
        } catch (ValidationException e) {
            LOGGER.warn(ERROR_MSG, e);
            generationSuccess = false;
        }
        return generationSuccess;
    }

    @Transactional
    public void markDemandGenerationLogAsCompleted(TradeLicense license, String details) {
        Optional<DemandGenerationLogDetail> demandGenerationLogDetail = demandGenerationLogService
                .getIncompleteLogDetailsByLicenseId(license.getId());
        if (demandGenerationLogDetail.isPresent()) {
            DemandGenerationLogDetail logDetail = demandGenerationLogDetail.get();
            logDetail.setStatus(COMPLETED);
            logDetail.setDetail(details);
            demandGenerationLogService.completeDemandGenerationLogDetail(logDetail);
            demandGenerationLogService.completeDemandGenerationLog(logDetail.getDemandGenerationLog());
        }
    }

    private boolean installmentYearValidForDemandGeneration(CFinancialYear installmentYear) {
        DateTime currentDate = new DateTime();
        DateTime startOfCalenderDate = new DateTime(installmentYear.getStartingDate()).monthOfYear().
                withMinimumValue().dayOfMonth().withMinimumValue().withTimeAtStartOfDay();
        DateTime endOfCalenderDate = startOfCalenderDate.monthOfYear().withMaximumValue().dayOfMonth().
                withMaximumValue().millisOfDay().withMaximumValue();
        return currentDate.isAfter(startOfCalenderDate) && currentDate.isBefore(endOfCalenderDate);
    }
}