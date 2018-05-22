package org.egov.eventnotification.repository;

import java.util.List;

import org.egov.eventnotification.entity.ModuleCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleCategoryRepository extends JpaRepository<ModuleCategory, Long> {

    List<ModuleCategory> findByModuleId(Long moduleId);

}
