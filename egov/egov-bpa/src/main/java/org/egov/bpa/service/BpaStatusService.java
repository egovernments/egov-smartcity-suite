package org.egov.bpa.service;

import org.egov.bpa.application.entity.BpaStatus;
import org.egov.bpa.repository.BpaStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BpaStatusService {

    private final BpaStatusRepository bpaStatusRepository;

    @Autowired
    public BpaStatusService(final BpaStatusRepository bpaStatusRepository) {
        this.bpaStatusRepository = bpaStatusRepository;
    }

    public BpaStatus findByModuleTypeAndCode(final String moduleType, final String code) {
        return bpaStatusRepository.findByModuleTypeContainingIgnoreCaseAndCode(moduleType, code);
    }
}
