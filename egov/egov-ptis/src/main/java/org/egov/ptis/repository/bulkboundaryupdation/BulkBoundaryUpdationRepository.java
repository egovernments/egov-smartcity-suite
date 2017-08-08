package org.egov.ptis.repository.bulkboundaryupdation;

import org.egov.ptis.domain.entity.property.view.PropertyMVInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BulkBoundaryUpdationRepository
		extends JpaRepository<PropertyMVInfo, Long>, JpaSpecificationExecutor<PropertyMVInfo> {

}
