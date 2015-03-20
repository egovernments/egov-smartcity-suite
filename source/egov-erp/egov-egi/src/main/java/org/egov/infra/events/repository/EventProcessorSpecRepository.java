package org.egov.infra.events.repository;

import org.egov.infra.events.entity.EventProcessorSpec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventProcessorSpecRepository extends JpaRepository<EventProcessorSpec, Long> {
    EventProcessorSpec findByModuleAndEventCode(String module, String eventCode);
}
