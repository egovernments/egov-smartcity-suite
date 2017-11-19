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
package org.egov.stms.transactions.service;

import org.egov.stms.entity.SewerageDemandGenerationLog;
import org.egov.stms.entity.SewerageDemandGenerationLogDetail;
import org.egov.stms.masters.entity.enums.SewerageProcessStatus;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.transactions.entity.SewerageDemandStatusDetails;
import org.egov.stms.transactions.repository.SewerageDemandGenerationLogDetailRepository;
import org.egov.stms.transactions.repository.SewerageDemandGenerationLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.egov.stms.masters.entity.enums.SewerageProcessStatus.COMPLETED;
import static org.egov.stms.masters.entity.enums.SewerageProcessStatus.INCOMPLETE;
import static org.springframework.util.StringUtils.isEmpty;

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

    public List<SewerageDemandGenerationLog> getDemandGenerationLogListByInstallmentYear(final String installmentYear) {
        return demandGenerationLogRepository.findByInstallmentYearOrderByIdDesc(installmentYear);
    }

    public SewerageDemandGenerationLog createDemandGenerationLog(String installmentYearRange) {
        return demandGenerationLogRepository.saveAndFlush(new SewerageDemandGenerationLog(installmentYearRange));
    }
    
    public List<SewerageDemandGenerationLogDetail> getDemandGenerationLogDetailByDemandGenerationLog(
            final SewerageDemandGenerationLog demandGenerationLog) {
        return demandGenerationLogDetailRepository.findByDemandGenerationLogIdOrderByIdDesc(demandGenerationLog.getId());
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
    public SewerageDemandGenerationLogDetail createOrGetDemandGenerationLogDetail(final SewerageDemandGenerationLog demandGenerationLog,
           final SewerageApplicationDetails applicationDetails, final SewerageProcessStatus status,final String detailMsg) {

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
    
    public SewerageDemandStatusDetails getDemandStatusResult(final List<SewerageDemandGenerationLog> logList) {
        final SewerageDemandStatusDetails demandStatus = new SewerageDemandStatusDetails();
        Long noOfSuccess = 0L;
        Long noOfFailure = 0L;
        final List<String> sewrageList = new ArrayList<>();
        List<SewerageDemandGenerationLogDetail> logDetailList;
        for (final SewerageDemandGenerationLog log : logList) {
            logDetailList = getDemandGenerationLogDetailByDemandGenerationLog(log);
            for (final SewerageDemandGenerationLogDetail logDetail : logDetailList) {
                if (isEmpty(sewrageList))
                    sewrageList.add(logDetail.getSewerageApplicationDetails().getApplicationNumber());
                else if (!sewrageList.contains(logDetail.getSewerageApplicationDetails().getApplicationNumber())){
                    if (SewerageProcessStatus.COMPLETED.equals(logDetail.getStatus()))
                        noOfSuccess++;
                    else if (SewerageProcessStatus.INCOMPLETE.equals(logDetail.getStatus()))
                        noOfFailure++;
                }
                sewrageList.add(logDetail.getSewerageApplicationDetails().getApplicationNumber());
            }
        }
        demandStatus.setNoOfSuccess(noOfSuccess);
        demandStatus.setNoOfFailure(noOfFailure);
        return demandStatus;
    }
    
    public List<SewerageDemandStatusDetails> getLogDetailResultList(final List<SewerageDemandGenerationLogDetail> logDetail,
            final SewerageDemandGenerationLog demandGenerationLog, final List<Long> detailList, final boolean value) {
        final List<SewerageDemandStatusDetails> successRecordsList = new ArrayList<>();
        final List<SewerageDemandStatusDetails> failedRecordsList = new ArrayList<>();
        final List<SewerageDemandStatusDetails> resultList = new ArrayList<>();
        SewerageDemandStatusDetails statusObject;
        for (final SewerageDemandGenerationLogDetail detail : logDetail)
            if (!detailList.contains(detail.getSewerageApplicationDetails().getApplicationNumber())) {

                statusObject = new SewerageDemandStatusDetails();
                statusObject.setFinancialYear(demandGenerationLog.getInstallmentYear());
                final SewerageApplicationDetails sewerageApplicationDetails = detail.getSewerageApplicationDetails();
                statusObject.setSewerageApplicationNumber(sewerageApplicationDetails.getApplicationNumber());
                
                statusObject.setStatus(detail.getStatus().toString());
                statusObject.setDetails(detail.getDetail());

                if (SewerageProcessStatus.COMPLETED.equals(detail.getStatus()))
                    successRecordsList.add(statusObject);
                else if (SewerageProcessStatus.INCOMPLETE.equals(detail.getStatus()))
                    failedRecordsList.add(statusObject);
                detailList.add(detail.getSewerageApplicationDetails().getId());
            }
        if (value)
            resultList.addAll(successRecordsList);
        else
            resultList.addAll(failedRecordsList);
        return resultList;
    }

}