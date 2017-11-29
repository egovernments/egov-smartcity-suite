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

package org.egov.adtax.service;

import static org.springframework.util.StringUtils.isEmpty;

import java.util.ArrayList;
import java.util.List;

import org.egov.adtax.entity.Advertisement;
import org.egov.adtax.entity.AdvertisementDemandGenerationLog;
import org.egov.adtax.entity.AdvertisementDemandGenerationLogDetail;
import org.egov.adtax.entity.enums.ProcessStatus;
import org.egov.adtax.repository.AdTaxDemandGenerationLogDetailRepository;
import org.egov.adtax.repository.AdTaxDemandGenerationLogRepository;
import org.egov.adtax.search.contract.AdvertisementDemandStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AdTaxDemandGenerationLogService {

    @Autowired
    private AdTaxDemandGenerationLogRepository adTaxDemandGenerationLogRepository;

    @Autowired
    private AdTaxDemandGenerationLogDetailRepository adTaxDemandGenerationLogDetailRepository;

    public List<AdvertisementDemandGenerationLog> getDemandGenerationLogByInstallmentYear(final String installmentYear) {
        return adTaxDemandGenerationLogRepository.findByInstallmentYearOrderByIdDesc(installmentYear);
    }

    public List<AdvertisementDemandGenerationLogDetail> getDemandGenerationLogDetailByDemandGenerationLog(
            final AdvertisementDemandGenerationLog demandGenerationLog) {
        return adTaxDemandGenerationLogDetailRepository.findByDemandGenerationLogIdOrderByIdDesc(demandGenerationLog.getId());
    }

    public AdvertisementDemandGenerationLog createDemandGenerationLog(final String installmentYear) {
        return adTaxDemandGenerationLogRepository.saveAndFlush(new AdvertisementDemandGenerationLog(installmentYear));
    }

    public AdvertisementDemandGenerationLog updateDemandGenerationLog(
            final AdvertisementDemandGenerationLog demandGenerationLog) {
        return adTaxDemandGenerationLogRepository.saveAndFlush(demandGenerationLog);
    }

    public AdvertisementDemandGenerationLogDetail createOrGetDemandGenerationLogDetail(
            final AdvertisementDemandGenerationLog generationLog, final Advertisement advertisement, final ProcessStatus status,
            final String detailMessage) {
        AdvertisementDemandGenerationLogDetail logDetail = adTaxDemandGenerationLogDetailRepository
                .findByDemandGenerationLogIdAndAdvertisementId(
                        generationLog.getId(), advertisement.getId());
        if (logDetail == null) {
            logDetail = new AdvertisementDemandGenerationLogDetail();
            logDetail.setDemandGenerationLog(generationLog);
            logDetail.setAdvertisement(advertisement);
            logDetail.setStatus(status);
            logDetail.setDetail(detailMessage);

            generationLog.addDetails(logDetail);
            logDetail = adTaxDemandGenerationLogDetailRepository.saveAndFlush(logDetail);
        }
        return logDetail;
    }

    @Transactional
    public AdvertisementDemandGenerationLogDetail updateDemandGenerationLogDetail(
            final AdvertisementDemandGenerationLogDetail logDetail) {
        return adTaxDemandGenerationLogDetailRepository.saveAndFlush(logDetail);
    }

    public List<AdvertisementDemandGenerationLog> findAllOrderByIdDesc() {
        return adTaxDemandGenerationLogRepository.findAll(new Sort(Sort.Direction.DESC, "id"));
    }

    public List<AdvertisementDemandStatus> getLogDetailResultList(final List<AdvertisementDemandGenerationLogDetail> logDetail,
            final AdvertisementDemandGenerationLog demandGenerationLog, final List<Long> detailList, final boolean value) {
        final List<AdvertisementDemandStatus> successRecordsList = new ArrayList<>();
        final List<AdvertisementDemandStatus> failedRecordsList = new ArrayList<>();
        final List<AdvertisementDemandStatus> resultList = new ArrayList<>();
        AdvertisementDemandStatus statusObject;
        for (final AdvertisementDemandGenerationLogDetail detail : logDetail)
            if (!detailList.contains(detail.getAdvertisement().getId())) {

                statusObject = new AdvertisementDemandStatus();
                statusObject.setFinancialYear(demandGenerationLog.getInstallmentYear());
                final Advertisement advertisement = detail.getAdvertisement();
                statusObject.setAdvertisementNumber(advertisement.getAdvertisementNumber());
                statusObject.setStatus(detail.getStatus().toString());
                statusObject.setDetails(detail.getDetail());

                if (ProcessStatus.COMPLETED.equals(detail.getStatus()))
                    successRecordsList.add(statusObject);
                else if (ProcessStatus.INCOMPLETE.equals(detail.getStatus()))
                    failedRecordsList.add(statusObject);
                detailList.add(detail.getAdvertisement().getId());
            }
        if (value)
            resultList.addAll(successRecordsList);
        else
            resultList.addAll(failedRecordsList);
        return resultList;
    }

    public AdvertisementDemandStatus getDemandStatusResult(final List<AdvertisementDemandGenerationLog> logList) {
        final AdvertisementDemandStatus demandStatus = new AdvertisementDemandStatus();
        Long noOfSuccess = 0L;
        Long noOfFailure = 0L;
        final List<String> advertisementNoList = new ArrayList<>();
        List<AdvertisementDemandGenerationLogDetail> logDetailList;
        for (final AdvertisementDemandGenerationLog log : logList) {
            logDetailList = getDemandGenerationLogDetailByDemandGenerationLog(log);
            for (final AdvertisementDemandGenerationLogDetail logDetail : logDetailList) {
                if (isEmpty(advertisementNoList))
                    advertisementNoList.add(logDetail.getAdvertisement().getAdvertisementNumber());
                else if (!advertisementNoList.contains(logDetail.getAdvertisement().getAdvertisementNumber()))
                    if (ProcessStatus.COMPLETED.equals(logDetail.getStatus()))
                        noOfSuccess++;
                    else if (ProcessStatus.INCOMPLETE.equals(logDetail.getStatus()))
                        noOfFailure++;

                advertisementNoList.add(logDetail.getAdvertisement().getAdvertisementNumber());
            }
        }
        demandStatus.setNoOfSuccess(noOfSuccess);
        demandStatus.setNoOfFailure(noOfFailure);
        return demandStatus;
    }
}
