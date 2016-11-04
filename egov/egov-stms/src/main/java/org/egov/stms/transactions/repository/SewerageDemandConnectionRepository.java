package org.egov.stms.transactions.repository;

import org.egov.demand.model.EgDemand;
import org.egov.stms.transactions.entity.SewerageDemandConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SewerageDemandConnectionRepository extends JpaRepository<SewerageDemandConnection, Long> {
    
    SewerageDemandConnection findByDemand(EgDemand demand);

}
