package org.egov.infra.admin.master.repository;

import java.util.List;

import org.egov.infra.admin.master.entity.HierarchyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HierarchyTypeRepository extends JpaRepository<HierarchyType, Long> {
    HierarchyType findByName(String name);
    List<HierarchyType> findByNameContainingIgnoreCase(String name);
}
