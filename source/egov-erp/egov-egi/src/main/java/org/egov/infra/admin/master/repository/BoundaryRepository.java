package org.egov.infra.admin.master.repository;

import java.util.List;

import org.egov.infra.admin.master.entity.Boundary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoundaryRepository extends JpaRepository<Boundary, Long> {
    
    String QUERY_BOUNDARY_BY_BOUNDARYTYPE = "select b from Boundary b where b.boundaryType.id = :boundaryTypeId";
    
    Boundary findByName(String name);
    List<Boundary> findByNameContainingIgnoreCase(String name);
    
    @Query(QUERY_BOUNDARY_BY_BOUNDARYTYPE)
    List<Boundary> findAllByBoundaryTypeId(@Param("boundaryTypeId") Long boundaryTypeId);
    
    @Query(QUERY_BOUNDARY_BY_BOUNDARYTYPE)
    Page<Boundary> findPageByBoundaryTypeId(@Param("boundaryTypeId") Long boundaryTypeId, Pageable page);
}
