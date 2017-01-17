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

import org.egov.tl.entity.DemandGenerationLog;
import org.egov.tl.entity.DemandGenerationLogDetail;
import org.egov.tl.entity.License;
import org.egov.tl.entity.enums.ProcessStatus;
import org.egov.tl.repository.DemandGenerationLogDetailRepository;
import org.egov.tl.repository.DemandGenerationLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DemandGenerationLogService {

    @Autowired
    private DemandGenerationLogRepository demandGenerationLogRepository;

    @Autowired
    private DemandGenerationLogDetailRepository demandGenerationLogDetailRepository;

    public DemandGenerationLog getDemandGenerationLogByInstallmentYear(String installmentYear) {
        return demandGenerationLogRepository.findByInstallmentYear(installmentYear);
    }

    @Transactional
    public DemandGenerationLog createDemandGenerationLog(String installmentYear) {
        return demandGenerationLogRepository.saveAndFlush(new DemandGenerationLog(installmentYear));
    }

    @Transactional
    public DemandGenerationLog updateDemandGenerationLog(DemandGenerationLog generationLog) {
        return demandGenerationLogRepository.saveAndFlush(generationLog);
    }

    @Transactional
    public DemandGenerationLogDetail createOrGetDemandGenerationLogDetail(DemandGenerationLog generationLog, License license) {

        DemandGenerationLogDetail logDetail = demandGenerationLogDetailRepository.
                findByDemandGenerationLogIdAndLicenseId(generationLog.getId(), license.getId());
        if (logDetail == null) {
            logDetail = new DemandGenerationLogDetail();
            logDetail.setLicense(license);
            logDetail.setDemandGenerationLog(generationLog);
            logDetail.setStatus(ProcessStatus.INPROGRESS);
            generationLog.getDetails().add(logDetail);
            logDetail = demandGenerationLogDetailRepository.saveAndFlush(logDetail);
        }
        return logDetail;
    }

    @Transactional
    public DemandGenerationLogDetail updateDemandGenerationLogDetail(DemandGenerationLogDetail logDetail) {
        return demandGenerationLogDetailRepository.saveAndFlush(logDetail);
    }

}