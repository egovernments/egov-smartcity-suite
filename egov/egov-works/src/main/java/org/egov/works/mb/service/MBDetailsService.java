package org.egov.works.mb.service;

import java.util.Date;
import java.util.List;

import org.egov.works.mb.entity.MBDetails;
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

    public List<MBDetails> getActivitiesByContractorBillTillDate(final Long contractorBillId, final Date billCreatedDate) {
        return mBDetailsRepository.getActivitiesByContractorBillTillDate(contractorBillId, WorksConstants.APPROVED,billCreatedDate);
    }
}
