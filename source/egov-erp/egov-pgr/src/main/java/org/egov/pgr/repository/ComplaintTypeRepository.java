package org.egov.pgr.repository;

import org.egov.pgr.entity.ComplaintType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintTypeRepository extends JpaRepository<ComplaintType, Long> {

    List<ComplaintType> findByNameContainingIgnoreCaseOrderByNameAsc(String name);

    List<ComplaintType> findByDepartmentId(Long id);
}
