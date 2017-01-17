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
import org.egov.commons.service.CFinancialYearService;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.config.properties.ApplicationProperties;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.tl.entity.DemandGenerationLog;
import org.egov.tl.entity.DemandGenerationLogDetail;
import org.egov.tl.entity.License;
import org.egov.tl.entity.enums.ProcessStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.egov.infra.persistence.utils.PersistenceUtils.flushBatchUpdate;
import static org.egov.tl.utils.Constants.PERMANENT_NATUREOFBUSINESS;
import static org.egov.tl.utils.Constants.TRADELICENSE_MODULENAME;

@Service
@Transactional(readOnly = true)
public class DemandGenerationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemandGenerationService.class);
    private static final String LICENSE_NOT_ACTIVE = "License Not Active";
    private static final String SUCCESSFUL = "Successful";
    private static final String DEMAND_EXIST = "Demand exist";

    @Autowired
    public DemandGenerationLogService demandGenerationLogService;

    @PersistenceContext
    public EntityManager entityManager;

    @Autowired
    private CFinancialYearService financialYearService;

    @Autowired
    private InstallmentDao installmentDao;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    @Qualifier("tradeLicenseService")
    private AbstractLicenseService licenseService;

    private int batchSize = 0;

    @Autowired
    public DemandGenerationService(ApplicationProperties applicationProperties) {
        this.batchSize = applicationProperties.getBatchUpdateSize();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, timeout = 7200)
    public DemandGenerationLog generateDemand(String installmentYear) {
        DemandGenerationLog demandGenerationLog = demandGenerationLogService.getDemandGenerationLogByInstallmentYear(installmentYear);
        if (demandGenerationLog != null)
            return demandGenerationLog;
        demandGenerationLog = demandGenerationLogService.createDemandGenerationLog(installmentYear);
        return tryLicenseDemandGeneration(demandGenerationLog, false);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, timeout = 7200)
    public DemandGenerationLog regenerateDemand(String installmentYear) {
        DemandGenerationLog demandGenerationLog = demandGenerationLogService.getDemandGenerationLogByInstallmentYear(installmentYear);
        return tryLicenseDemandGeneration(demandGenerationLog, true);
    }

    private DemandGenerationLog tryLicenseDemandGeneration(DemandGenerationLog demandGenerationLog, boolean regeneration) {
        CFinancialYear financialYear = financialYearService.getFinacialYearByYearRange(demandGenerationLog.getInstallmentYear());
        Module module = moduleService.getModuleByName(TRADELICENSE_MODULENAME);
        Installment givenInstallment = installmentDao.getInsatllmentByModuleForGivenDate(module, financialYear.getStartingDate());
        if (givenInstallment == null)
            throw new ValidationException("TL-005", "TL-005");
        demandGenerationLog.setDemandGenerationStatus(ProcessStatus.INPROGRESS);
        List<License> licenses = licenseService.getAllLicensesByNatureOfBusiness(PERMANENT_NATUREOFBUSINESS);
        int batchUpdateCount = 0;
        for (License license : licenses) {
            DemandGenerationLogDetail demandGenerationLogDetail = demandGenerationLogService.createOrGetDemandGenerationLogDetail(demandGenerationLog, license);
            try {
                if (!license.getIsActive()) {
                    demandGenerationLogDetail.setDetail(LICENSE_NOT_ACTIVE);
                } else if (regeneration || !givenInstallment.equals(license.getCurrentDemand().getEgInstallmentMaster())) {
                    licenseService.raiseDemand(license, module, givenInstallment);
                    demandGenerationLogDetail.setDetail(SUCCESSFUL);
                } else {
                    demandGenerationLogDetail.setDetail(DEMAND_EXIST);
                }
                demandGenerationLogDetail.setStatus(ProcessStatus.COMPLETED);
                demandGenerationLogService.updateDemandGenerationLogDetail(demandGenerationLogDetail);
                flushBatchUpdate(entityManager, ++batchUpdateCount, batchSize);
            } catch (RuntimeException e) {
                LOGGER.warn("Error occurred while generating demand for license {}", license.getLicenseNumber(), e);
                demandGenerationLog.setDemandGenerationStatus(ProcessStatus.INCOMPLETE);
                updateDemandGenerationLogDetailOnException(demandGenerationLogDetail, e);
            }
        }
        demandGenerationLog.setExecutionStatus(ProcessStatus.COMPLETED);
        return demandGenerationLogService.updateDemandGenerationLog(demandGenerationLog);
    }

    private DemandGenerationLogDetail updateDemandGenerationLogDetailOnException(DemandGenerationLogDetail logDetail,
                                                                                 RuntimeException exception) {
        String error;
        if (exception instanceof ValidationException)
            error = ((ValidationException) exception).getErrors().get(0).getMessage();
        else
            error = "Error : " + exception;
        logDetail.setStatus(ProcessStatus.INCOMPLETE);
        logDetail.setDetail(error);
        return demandGenerationLogService.updateDemandGenerationLogDetail(logDetail);
    }
}