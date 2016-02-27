package org.egov.model.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.CChartOfAccounts;
import org.egov.model.recoveries.Recovery;
import org.egov.model.repository.RecoveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RecoveryService {

    private final RecoveryRepository recoveryRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public RecoveryService(final RecoveryRepository recoveryRepository) {
        this.recoveryRepository = recoveryRepository;
    }

    @Transactional
    public Recovery create(final Recovery recovery) {
        return recoveryRepository.save(recovery);
    }

    @Transactional
    public Recovery update(final Recovery recovery) {
        return recoveryRepository.save(recovery);
    }

    public List<Recovery> findAll() {
        return recoveryRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }

    public Recovery findOne(Long id) {
        return recoveryRepository.findOne(id);
    }

    public List<Recovery> search(Recovery recovery) {
        if (recovery != null && recovery.getChartofaccounts() != null && recovery.getChartofaccounts().getId() != null)
            return recoveryRepository.findByChartofaccounts(recovery.getChartofaccounts());
        else
            return recoveryRepository.findAll();
    }

    public List<Recovery> getAllActiveRecoverys() {
        return recoveryRepository.findByIsactive(true);
    }

    public List<Recovery> getByAccountCode(CChartOfAccounts chartOfAccounts) {
        return recoveryRepository.findByChartofaccounts(chartOfAccounts);
    }
    
    public List<Recovery> getAllActiveAutoRemitTds()
    {
        return recoveryRepository.findByIsactiveAndRemittanceModeOrderByType(true, 'A');
    }
}