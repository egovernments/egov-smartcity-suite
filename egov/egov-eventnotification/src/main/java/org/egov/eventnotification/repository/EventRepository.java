package org.egov.eventnotification.repository;

import org.egov.eventnotification.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long>{

}
