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

import org.egov.commons.CFinancialYear;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentDao;
import org.egov.commons.repository.CFinancialYearRepository;
import org.egov.demand.dao.DemandGenericHibDao;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.demand.model.EgDemandReasonMaster;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.config.properties.ApplicationProperties;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.tl.entity.DemandGenerationLog;
import org.egov.tl.entity.DemandGenerationLogDetail;
import org.egov.tl.entity.FeeMatrixDetail;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.entity.enums.ProcessStatus;
import org.egov.tl.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private ApplicationProperties applicationProperties;

    @Autowired
    public DemandGenerationLogService demandGenerationLogService;

    public List<CFinancialYear> financialYearList() {
        return cFinancialYearRepository.getAllFinancialYears();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, timeout = 7200)
    public DemandGenerationLog bulkDemandGeneration(DemandGenerationLog demandGenerationLog) {
        final DemandGenerationLog existingDemandGenLog = demandGenerationLogService
                .findByInstallmentYear(demandGenerationLog.getInstallmentYear());
        if (existingDemandGenLog != null)
            return existingDemandGenLog;
        final CFinancialYear financialYear = cFinancialYearRepository
                .findByFinYearRange(demandGenerationLog.getInstallmentYear());
        final Module module = moduleService.getModuleByName(Constants.TRADELICENSE_MODULENAME);
        final Installment currentInstallment = installmentDao.getInsatllmentByModuleForGivenDate(module,
                financialYear.getStartingDate());
        final List<TradeLicense> licenses = tradeLicenseService
                .getAllLicensesByNatureOfBusiness(Constants.PERMANENT_NATUREOFBUSINESS);
        ProcessStatus demandGenerationStatus = ProcessStatus.COMPLETED;
        demandGenerationLog = demandGenerationLogService.createDemandGenerationLog(demandGenerationLog);
        int batchUpdateCount = 0;
        final int batchSize = applicationProperties.getBatchUpdateSize();
        for (final TradeLicense license : licenses) {
            final DemandGenerationLogDetail demandGenerationLogDetail = demandGenerationLogService
                    .createDemandGenerationLogDetail(demandGenerationLog, license);
            try {
                if (!license.getCurrentDemand().getEgInstallmentMaster().equals(currentInstallment)) {
                    if (!license.getIsActive()) {
                        demandGenerationLogService.updateDemandGenerationLogDetail(demandGenerationLogDetail,
                                ProcessStatus.INCOMPLETE,
                                "License Not Active");
                        continue;
                    }
                    calculateAndPersistFeeForDemand(module, currentInstallment, license);
                    batchUpdateFlush(++batchUpdateCount, batchSize);
                    demandGenerationLogService.updateDemandGenerationLogDetail(demandGenerationLogDetail, ProcessStatus.COMPLETED,
                            "Successful");
                } else
                    demandGenerationLogService.updateDemandGenerationLogDetail(demandGenerationLogDetail, ProcessStatus.COMPLETED,
                            "Demand exist");
            } catch (final RuntimeException e) {
                demandGenerationStatus = ProcessStatus.INCOMPLETE;
                updateDemandGenerationLogDetailOnException(demandGenerationLogDetail, e);
            }
        }
        return demandGenerationLogService.updateDemandGenerationLog(demandGenerationLog, ProcessStatus.COMPLETED,
                demandGenerationStatus);

    }

    private void updateDemandGenerationLogDetailOnException(final DemandGenerationLogDetail demandGenerationLogDetail,
            final RuntimeException e) {
        LOGGER.error("Error occurred while generating demand", e);
        String error;
        if (e instanceof ValidationException)
            error = ((ValidationException) e).getErrors().get(0).getMessage();
        else
            error = "Error : " + e;
        demandGenerationLogService.updateDemandGenerationLogDetail(demandGenerationLogDetail, ProcessStatus.INCOMPLETE, error);
    }

    private void batchUpdateFlush(final int batchUpdateCount, final int batchSize) {
        if (batchUpdateCount % batchSize == 0) {
            tradeLicenseService.licensePersitenceService().getSession().flush();
            tradeLicenseService.licensePersitenceService().getSession().clear();
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, timeout = 7200)
    public DemandGenerationLog demandRegeneration(final DemandGenerationLog demandGenerationLog) {
        final CFinancialYear financialYear = cFinancialYearRepository
                .findByFinYearRange(demandGenerationLog.getInstallmentYear());
        final Module module = moduleService.getModuleByName(Constants.TRADELICENSE_MODULENAME);
        final Installment currentInstallment = installmentDao.getInsatllmentByModuleForGivenDate(module,
                financialYear.getStartingDate());
        ProcessStatus demandGenerationStatus = ProcessStatus.COMPLETED;
        final DemandGenerationLog existingDemandGenLog = demandGenerationLogService
                .findByInstallmentYear(demandGenerationLog.getInstallmentYear());
        if (existingDemandGenLog != null)
            demandGenerationLogService.createDemandGenerationLog(existingDemandGenLog);
        int batchUpdateCount = 0;
        final int batchSize = applicationProperties.getBatchUpdateSize();
        for (final DemandGenerationLogDetail demandGenerationLogDetail : existingDemandGenLog.getDetails()) {
            final TradeLicense license = (TradeLicense) demandGenerationLogDetail.getLicense();
            try {
                if (!license.getCurrentDemand().getEgInstallmentMaster().equals(currentInstallment)) {
                    if (!demandGenerationLogDetail.getLicense().getIsActive()) {
                        demandGenerationLogService.updateDemandGenerationLogDetail(demandGenerationLogDetail,
                                ProcessStatus.INCOMPLETE, "License Not Active");
                        continue;
                    }
                    calculateAndPersistFeeForDemand(module, currentInstallment, license);
                    batchUpdateFlush(++batchUpdateCount, batchSize);
                    demandGenerationLogService.updateDemandGenerationLogDetail(demandGenerationLogDetail, ProcessStatus.COMPLETED,
                            "Successful");
                } else
                    demandGenerationLogService.updateDemandGenerationLogDetail(demandGenerationLogDetail, ProcessStatus.COMPLETED,
                            "Demand exist");
            } catch (final RuntimeException e) {
                demandGenerationStatus = ProcessStatus.INCOMPLETE;
                updateDemandGenerationLogDetailOnException(demandGenerationLogDetail, e);
            }
        }
        return demandGenerationLogService.updateDemandGenerationLog(existingDemandGenLog, ProcessStatus.COMPLETED,
                demandGenerationStatus);

    }

    private void calculateAndPersistFeeForDemand(final Module module, final Installment currentInstallment,
            final TradeLicense license) {
        final List<FeeMatrixDetail> feeList = feeMatrixService.findFeeList(license);
        for (final FeeMatrixDetail fm : feeList) {
            if (fm.getFeeMatrix().getFeeType().getName().contains("Late"))
                continue;
            final EgDemandReasonMaster reasonMaster = demandGenericDao
                    .getDemandReasonMasterByCode(fm.getFeeMatrix().getFeeType().getName(), module);
            final EgDemandReason reason = demandGenericDao.getDmdReasonByDmdReasonMsterInstallAndMod(reasonMaster,
                    currentInstallment,
                    module);
            final EgDemandDetails licenseDemandDetail = getReasonWiseDemandDetails(license.getCurrentDemand()).get(reason);
            if (licenseDemandDetail == null)
                license.getLicenseDemand().getEgDemandDetails()
                        .add(EgDemandDetails.fromReasonAndAmounts(fm.getAmount(), reason, BigDecimal.ZERO));
            else
                licenseDemandDetail.setAmount(fm.getAmount());
            license.getLicenseDemand().setEgInstallmentMaster(currentInstallment);
        }
        tradeLicenseService.recalculateBaseDemand(license.getLicenseDemand());
        tradeLicenseService.licensePersitenceService().persist(license);
    }

    public Map<EgDemandReason, EgDemandDetails> getReasonWiseDemandDetails(final EgDemand currentDemand) {
        final Map<EgDemandReason, EgDemandDetails> reasonWiseDemandDetails = new HashMap<EgDemandReason, EgDemandDetails>();
        if (currentDemand == null) {
        } else
            for (final EgDemandDetails dmdDet : currentDemand.getEgDemandDetails())
                if (dmdDet.getEgDemandReason().getEgDemandReasonMaster().getCode().equals(Constants.LICENSE_FEE_TYPE))
                    reasonWiseDemandDetails.put(dmdDet.getEgDemandReason(), dmdDet);

        return reasonWiseDemandDetails;
    }
}
