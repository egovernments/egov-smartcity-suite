package org.egov.model.repository;

import java.util.List;

import org.egov.commons.CChartOfAccounts;
import org.egov.model.recoveries.Recovery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecoveryRepository extends JpaRepository<Recovery, Long> {

    public List<Recovery> findByIsactive(Boolean isActive);
    public List<Recovery> findByChartofaccounts(CChartOfAccounts chartofaccounts);
    public List<Recovery> findByIsactiveAndRemittanceModeOrderByType(Boolean isActive,Character remittanceMode);

}