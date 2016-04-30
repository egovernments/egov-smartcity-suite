package org.egov.model.repository;

import org.egov.commons.CChartOfAccounts;
import org.egov.model.recoveries.Recovery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecoveryRepository extends JpaRepository<Recovery, Long> {

    public List<Recovery> findByIsactive(Boolean isActive);
    public List<Recovery> findByChartofaccounts(CChartOfAccounts chartofaccounts);
    public List<Recovery> findByIsactiveAndRemittanceModeOrderByType(Boolean isActive,Character remittanceMode);

}