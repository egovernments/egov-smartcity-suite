package org.egov.works.mb.service;

import java.util.Date;
import java.util.List;

import org.egov.works.mb.entity.MBDetails;
import org.egov.works.mb.entity.MBHeader.MeasurementBookStatus;
import org.egov.works.mb.repository.MBDetailsRepository;
import org.egov.works.utils.WorksConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MBDetailsService {

    private final MBDetailsRepository mBDetailsRepository;

    @Autowired
    public MBDetailsService(final MBDetailsRepository mBDetailsRepository) {
        this.mBDetailsRepository = mBDetailsRepository;
    }

    public List<Object[]> getActivitiesByContractorBillForApprovedMB(final Long contractorBillId) {
        return mBDetailsRepository.getActivitiesByContractorBill(contractorBillId, WorksConstants.APPROVED);
    }

    public List<MBDetails> getActivitiesByContractorBillTillDate(final Long workOrderEstimateId, final Date billCreatedDate) {
        return mBDetailsRepository.getActivitiesByContractorBillTillDate(workOrderEstimateId, WorksConstants.APPROVED,billCreatedDate);
    }
        
    public List<MBDetails> getMBDetailsByWorkOrderActivity(Long woaId) {
        return mBDetailsRepository.getMBDetailsByWorkOrderActivity(woaId, MeasurementBookStatus.APPROVED.toString());
    }

}
