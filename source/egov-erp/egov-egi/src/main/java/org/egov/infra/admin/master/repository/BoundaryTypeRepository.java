package org.egov.infra.admin.master.repository;


import org.egov.infra.admin.master.entity.BoundaryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoundaryTypeRepository extends JpaRepository<BoundaryType, Long> {
	
	BoundaryType findByName(String name);
	
	@Query("select bt from BoundaryType bt where bt.hierarchyType.name=:hierarchyName and bt.hierarchy=:hierarchyLevel")
    public BoundaryType findByHierarchyTypeNameAndLevel(@Param("hierarchyName") String name,@Param("hierarchyLevel")Integer hierarchyLevel);
}
