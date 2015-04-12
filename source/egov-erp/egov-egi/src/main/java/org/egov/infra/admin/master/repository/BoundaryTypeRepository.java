package org.egov.infra.admin.master.repository;

import java.util.List;

import org.egov.infra.admin.master.entity.BoundaryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoundaryTypeRepository extends JpaRepository<BoundaryType, Long> {
	
	BoundaryType findByName(String name);
	
	@Query("select bt from BoundaryType bt where bt.hierarchyType.name=:hierarchyName and bt.hierarchy=:hierarchyLevel")
        public BoundaryType findByHierarchyTypeNameAndLevel(@Param("hierarchyName") String name,@Param("hierarchyLevel")Long hierarchyLevel);
	
	@Query("select bt from BoundaryType bt where bt.hierarchyType.id=:hierarchyId")
        public List<BoundaryType> findByHierarchyTypeId(@Param("hierarchyId") Long id);
	
	@Query("select bt from BoundaryType bt where bt.parent.id=:parentId")
        public BoundaryType findByParent(@Param("parentId") Long parentId);
	
	@Query("select bt from BoundaryType bt where bt.id = :id and bt.hierarchyType.id = :hierarchyId")
	public BoundaryType findByIdAndHierarchy(@Param("id") Long id, @Param("hierarchyId") Long hierarchyId);
 
}
