package org.egov.eventnotification.repository;

import java.util.List;

import org.egov.eventnotification.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository to access {@link Event} instance
 * @author somvit
 *
 */
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("select e from Event e where e.startDate >= :startDate and e.endDate >= :startDate and e.status = :status order by e.id desc ")
    public List<Event> getAllEventsByDate(@Param("startDate") Long startDate, @Param("status") String status);
}