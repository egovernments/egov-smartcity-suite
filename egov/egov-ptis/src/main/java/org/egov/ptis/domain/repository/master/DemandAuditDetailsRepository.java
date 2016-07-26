package org.egov.ptis.domain.repository.master;

import org.egov.ptis.domain.entity.property.DemandAuditDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository 
public interface DemandAuditDetailsRepository extends JpaRepository<DemandAuditDetails, Long> {

    DemandAuditDetails findById(Long id);
}