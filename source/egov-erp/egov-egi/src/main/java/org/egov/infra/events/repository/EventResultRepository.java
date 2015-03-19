package org.egov.infra.events.repository;

import org.egov.infra.events.entity.EventResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventResultRepository extends JpaRepository<EventResult, Long> {

}
