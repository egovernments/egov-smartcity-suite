package org.egov.eventnotification.repository;

import java.util.List;

import org.egov.eventnotification.entity.ModuleCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ModuleCategoryRepository extends JpaRepository<ModuleCategory, Long> {
	
	@Query("select c from ModuleCategory c where c.module.id = :module_id ")
    public List<ModuleCategory> getCategoriesForModule(@Param("module_id") Long moduleId);
	
}
