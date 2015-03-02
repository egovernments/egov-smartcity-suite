package org.egov.pgr.repository;

import java.util.List;

import org.egov.pgr.entity.ComplaintType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplaintTypeRepository extends JpaRepository<ComplaintType, Long> {
    
    ComplaintType findByName(String name);
    List<ComplaintType> findByNameContainingIgnoreCase(String name);
}
