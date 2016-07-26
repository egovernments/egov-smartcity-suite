package org.egov.council.repository;

import org.egov.council.entity.CouncilMeeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouncilMeetingRepository extends JpaRepository<CouncilMeeting, Long> {

    CouncilMeeting findByMeetingNumber(String meetingNumber);

    CouncilMeeting findById(Long id);

}