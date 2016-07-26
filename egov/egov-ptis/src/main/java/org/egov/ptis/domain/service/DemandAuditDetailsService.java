package org.egov.ptis.domain.service;

import org.egov.ptis.domain.entity.property.DemandAuditDetails;
import org.egov.ptis.domain.repository.master.DemandAuditDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DemandAuditDetailsService {

    private final DemandAuditDetailsRepository demandAuditDetailsRepository;

    @Autowired
    public DemandAuditDetailsService(final DemandAuditDetailsRepository demandAuditDetailsRepository) {
        this.demandAuditDetailsRepository = demandAuditDetailsRepository;
    }

    @Transactional
    public DemandAuditDetails saveDemandDetails(final DemandAuditDetails demandAuditDetails) {
        return demandAuditDetailsRepository.save(demandAuditDetails);

    }

}
