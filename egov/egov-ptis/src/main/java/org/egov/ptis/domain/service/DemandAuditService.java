package org.egov.ptis.domain.service;

import org.egov.ptis.domain.entity.property.DemandAudit;
import org.egov.ptis.domain.repository.master.DemandAuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DemandAuditService {

    
    private final DemandAuditRepository demandAuditRepository;
    
    @Autowired
    public DemandAuditService(final DemandAuditRepository demandAuditRepository) {
        this.demandAuditRepository = demandAuditRepository;
    }

    @Transactional
    public DemandAudit saveDetails(final DemandAudit demandAudit) {
        return demandAuditRepository.save(demandAudit);

    }

}
