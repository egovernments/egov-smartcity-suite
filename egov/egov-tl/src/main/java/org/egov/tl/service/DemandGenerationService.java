/* eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.tl.service;

import java.math.BigDecimal;
import java.util.List;

import org.egov.commons.CFinancialYear;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentDao;
import org.egov.commons.repository.CFinancialYearRepository;
import org.egov.demand.dao.DemandGenericHibDao;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.demand.model.EgDemandReasonMaster;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.tl.entity.FeeMatrixDetail;
import org.egov.tl.entity.License;
import org.egov.tl.entity.DemandGenerationLog;
import org.egov.tl.entity.DemandGenerationLogDetail;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.entity.enums.ProcessStatus;
import org.egov.tl.repository.DemandGenerationLogDetailRepository;
import org.egov.tl.repository.DemandGenerationLogRepository;
import org.egov.tl.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DemandGenerationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemandGenerationService.class);

    @Autowired
    private CFinancialYearRepository cFinancialYearRepository;

    @Autowired
    private InstallmentDao installmentDao;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    @Qualifier("tradeLicenseService")
    private TradeLicenseService tradeLicenseService;

    @Autowired
    private DemandGenericHibDao demandGenericDao;

    @Autowired
    private FeeMatrixService feeMatrixService;

    @Autowired
    private DemandGenerationLogRepository licenseDemandGenerationRepository;

    @Autowired
    private DemandGenerationLogDetailRepository licenseDemandGenerationDetailRepository;

    public List<CFinancialYear> financialYearList() {
        return cFinancialYearRepository.getAllFinancialYears();
    }

    @Transactional
    public DemandGenerationLog bulkDemandGeneration(DemandGenerationLog demandGenerationLog) {
        final CFinancialYear financialYear = cFinancialYearRepository
                .findByFinYearRange(demandGenerationLog.getInstallmentYear());
        final Module module = moduleService.getModuleByName(Constants.TRADELICENSE_MODULENAME);
        final Installment currentInstallment = installmentDao.getInsatllmentByModuleForGivenDate(module,
                financialYear.getStartingDate());
        final List<TradeLicense> licenses = tradeLicenseService
                .getAllLicensesByNatureOfBusiness(Constants.PERMANENT_NATUREOFBUSINESS);
        ProcessStatus demandGenerationStatus = ProcessStatus.COMPLETED;
        demandGenerationLog = createDemandGenerationLog(demandGenerationLog);
        for (final TradeLicense license : licenses) {
            final DemandGenerationLogDetail demandGenerationLogDetail = createDemandGenerationLogDetail(
                    demandGenerationLog, license);
            try {
                if (!license.getCurrentDemand().getEgInstallmentMaster().equals(currentInstallment)) {
                    final List<FeeMatrixDetail> feeList = feeMatrixService.findFeeList(license);
                    for (final FeeMatrixDetail fm : feeList) {
                        if (fm.getFeeMatrix().getFeeType().getName().contains("Late"))
                            continue;
                        final EgDemandReasonMaster reasonMaster = demandGenericDao
                                .getDemandReasonMasterByCode(fm.getFeeMatrix().getFeeType().getName(), module);
                        final EgDemandReason reason = demandGenericDao.getDmdReasonByDmdReasonMsterInstallAndMod(reasonMaster,
                                currentInstallment,
                                module);
                        license.getLicenseDemand().getEgDemandDetails()
                                .add(EgDemandDetails.fromReasonAndAmounts(fm.getAmount(), reason, BigDecimal.ZERO));
                        tradeLicenseService.recalculateBaseDemand(license.getLicenseDemand());
                        license.getLicenseDemand().setEgInstallmentMaster(currentInstallment);
                        tradeLicenseService.licensePersitenceService().persist(license);

                    }
                    updateDemandGenerationLogDetail(demandGenerationLogDetail, ProcessStatus.COMPLETED,
                            "License demand generated");
                } else {
                    updateDemandGenerationLogDetail(demandGenerationLogDetail, ProcessStatus.COMPLETED,
                            "License demand already exist");
                }
            } catch (final RuntimeException e) {
                LOGGER.error("Error occurred while generating demand", e);
                demandGenerationStatus = ProcessStatus.INCOMPLETE;
                updateDemandGenerationLogDetail(demandGenerationLogDetail, ProcessStatus.INCOMPLETE, "Error : " + e);
            }
        }
        demandGenerationLog.setExecutionStatus(ProcessStatus.COMPLETED);
        demandGenerationLog.setDemandGenerationStatus(demandGenerationStatus);
        return licenseDemandGenerationRepository.save(demandGenerationLog);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public DemandGenerationLog createDemandGenerationLog(final DemandGenerationLog demandGenerationLog) {
        demandGenerationLog.setExecutionStatus(ProcessStatus.INPROGRESS);
        demandGenerationLog.setDemandGenerationStatus(ProcessStatus.INCOMPLETE);
        return licenseDemandGenerationRepository.save(demandGenerationLog);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public DemandGenerationLogDetail createDemandGenerationLogDetail(final DemandGenerationLog demandGenerationLog,
            final License license) {
        final DemandGenerationLogDetail demandGenerationLogDetail = new DemandGenerationLogDetail();
        demandGenerationLogDetail.setLicense(license);
        demandGenerationLogDetail.setDemandGenerationLog(demandGenerationLog);
        demandGenerationLogDetail.setStatus(ProcessStatus.INPROGRESS);
        demandGenerationLog.getDetails().add(demandGenerationLogDetail);
        return licenseDemandGenerationDetailRepository.save(demandGenerationLogDetail);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateDemandGenerationLogDetail(final DemandGenerationLogDetail demandGenerationLogDetail,
            final ProcessStatus status, final String details) {
        demandGenerationLogDetail.setStatus(status);
        demandGenerationLogDetail.setDetail(details);
        licenseDemandGenerationDetailRepository.save(demandGenerationLogDetail);
    }
}
