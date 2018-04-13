package org.egov.eventnotification.repository;

import java.util.List;

import org.egov.eventnotification.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EventRepository extends JpaRepository<Event, Long>{

}
