package org.egov.council.autonumber;


import org.egov.council.entity.CouncilMeeting;
import org.springframework.stereotype.Service;

@Service
public interface CouncilMeetingNumberGenerator {

	public String getNextNumber(CouncilMeeting councilMeeting);

}
