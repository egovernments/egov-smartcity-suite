package org.egov.pgr.repository;

import org.egov.pgr.entity.ComplaintStatusMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplaintStatusMappingRepository extends JpaRepository<ComplaintStatusMapping, Long>, ComplaintStatusMappingRepoCustom {

}
