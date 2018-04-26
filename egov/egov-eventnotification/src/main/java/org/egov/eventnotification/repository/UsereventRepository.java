package org.egov.eventnotification.repository;

import java.util.List;

import org.egov.eventnotification.entity.Event;
import org.egov.eventnotification.entity.Userevent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsereventRepository extends JpaRepository<Userevent, Long> {

    @Query("select COUNT(*) from Userevent ue where ue.eventid.id = :eventid")
    public Long countUsereventByEventId(@Param("eventid") Long eventid);
    
    @Query("select ue from Userevent ue where ue.eventid.id = :eventid and ue.userid.id = :userid")
    public Userevent getUsereventByEventAndUser(@Param("eventid") Long eventid,@Param("userid") Long userid);
}
