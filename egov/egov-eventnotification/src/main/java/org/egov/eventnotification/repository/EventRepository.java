package org.egov.eventnotification.repository;

import org.egov.eventnotification.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This is a respository class for event entity class
 * @author somvit
 *
 */
public interface EventRepository extends JpaRepository<Event, Long>{

}
