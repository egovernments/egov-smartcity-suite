package org.egov.eventnotification.repository;

import org.egov.eventnotification.entity.TemplateModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateModuleRepository extends JpaRepository<TemplateModule, Long> {

}
