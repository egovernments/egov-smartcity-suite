package org.egov.ptis.domain.repository.master;

import org.egov.ptis.domain.entity.property.DemandAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository 
public interface DemandAuditRepository extends JpaRepository<DemandAudit, Long> {

    DemandAudit findById(Long id);
}
