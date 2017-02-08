/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2017>  eGovernments Foundation
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
package org.egov.stms.transactions.service;

import static  org.egov.stms.masters.entity.enums.SewerageProcessStatus.COMPLETED;
import static  org.egov.stms.masters.entity.enums.SewerageProcessStatus.INCOMPLETE;

import org.egov.stms.entity.SewerageDemandGenerationLog;
import org.egov.stms.entity.SewerageDemandGenerationLogDetail;
import org.egov.stms.masters.entity.enums.SewerageProcessStatus;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.transactions.repository.SewerageDemandGenerationLogDetailRepository;
import org.egov.stms.transactions.repository.SewerageDemandGenerationLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SewerageDemandGenerationLogService {

    @Autowired
    private SewerageDemandGenerationLogRepository demandGenerationLogRepository;

    @Autowired
    private SewerageDemandGenerationLogDetailRepository demandGenerationLogDetailRepository;

    public SewerageDemandGenerationLog getDemandGenerationLogByInstallmentYear(String installmentYearRange) {
        return demandGenerationLogRepository.findByInstallmentYear(installmentYearRange);
    }

    public SewerageDemandGenerationLog createDemandGenerationLog(String installmentYearRange) {
        return demandGenerationLogRepository.saveAndFlush(new SewerageDemandGenerationLog(installmentYearRange));
    }

    @Transactional
    public SewerageDemandGenerationLog completeDemandGenerationLog(SewerageDemandGenerationLog demandGenerationLog) {
        demandGenerationLog.setDemandGenerationStatus(COMPLETED);
        for (SewerageDemandGenerationLogDetail detail : demandGenerationLog.getDetails()) {
            if (detail.getStatus().equals(INCOMPLETE)) {
                demandGenerationLog.setDemandGenerationStatus(INCOMPLETE);
                break;
            }
        }
        demandGenerationLog.setExecutionStatus(COMPLETED);
        return demandGenerationLogRepository.saveAndFlush(demandGenerationLog);
    }

    /**
     * Note:Do not add @Transactional
     * @param demandGenerationLog
     * @param applicationDetails
     * @param status
     * @param detailMsg
     * @return
     */
    public SewerageDemandGenerationLogDetail createOrGetDemandGenerationLogDetail(SewerageDemandGenerationLog demandGenerationLog,
            SewerageApplicationDetails applicationDetails, SewerageProcessStatus status, String detailMsg) {

        SewerageDemandGenerationLogDetail logDetail = demandGenerationLogDetailRepository
                .findByDemandGenerationLogIdAndSewerageApplicationDetailsId(demandGenerationLog.getId(),
                        applicationDetails.getId());
        if (logDetail == null) {
            logDetail = new SewerageDemandGenerationLogDetail();
            logDetail.setSewerageApplicationDetails(applicationDetails);
            logDetail.setDemandGenerationLog(demandGenerationLog);
            logDetail.setStatus(status);
            logDetail.setDetail(detailMsg);
            demandGenerationLog.getDetails().add(logDetail);
            logDetail = demandGenerationLogDetailRepository.saveAndFlush(logDetail);
        }
        return logDetail;
    }

    @Transactional
    public SewerageDemandGenerationLogDetail completeDemandGenerationLogDetail(
            SewerageDemandGenerationLogDetail demandGenerationLogDetail) {
        return demandGenerationLogDetailRepository.saveAndFlush(demandGenerationLogDetail);
    }

}