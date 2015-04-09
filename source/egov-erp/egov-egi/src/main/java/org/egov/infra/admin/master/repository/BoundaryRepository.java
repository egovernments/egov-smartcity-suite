package org.egov.infra.admin.master.repository;

import java.util.List;

import org.egov.infra.admin.master.entity.Boundary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoundaryRepository extends JpaRepository<Boundary, Long> {
    Boundary findByName(String name);
    List<Boundary> findByNameContainingIgnoreCase(String name);
    
    @Query("select b from Boundary b where b.boundaryType.id = :boundaryTypeId")
    List<Boundary> findAllByBoundaryTypeId(@Param("boundaryTypeId") Long boundaryTypeId);
}
