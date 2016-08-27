package org.egov.council.repository;

import java.util.List;

import org.egov.council.entity.CouncilMeeting;
import org.egov.council.entity.MeetingAttendence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingAttendanceRepository extends JpaRepository<MeetingAttendence, Long> {

    List<MeetingAttendence> findByMeeting(CouncilMeeting meeting);

}