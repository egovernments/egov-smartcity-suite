package org.egov.council.web.adaptor;

import java.lang.reflect.Type;

import org.egov.council.entity.CouncilMeeting;
import org.egov.council.entity.MeetingMOM;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class CouncilMeetingJsonAdaptor implements JsonSerializer<CouncilMeeting> {
    @Override
    public JsonElement serialize(final CouncilMeeting councilMeeting, final Type type, final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (councilMeeting != null) {

            if (councilMeeting.getMeetingNumber() != null)
                jsonObject.addProperty("meetingNumber", councilMeeting.getMeetingNumber());
            else
                jsonObject.addProperty("meetingNumber", "");
            if (councilMeeting.getCommitteeType() != null)
                jsonObject.addProperty("committeeType", councilMeeting.getCommitteeType().getName());
            else
                jsonObject.addProperty("committeeType", "");
            if (councilMeeting.getMeetingDate() != null)
                jsonObject.addProperty("meetingDate", councilMeeting.getMeetingDate().toString());
            else
                jsonObject.addProperty("meetingDate", "");
            if (councilMeeting.getMeetingDate() != null)
                jsonObject.addProperty("meetingLocation", councilMeeting.getMeetingLocation());
            else
                jsonObject.addProperty("meetingLocation", "");
            if (councilMeeting.getMeetingTime() != null)
                jsonObject.addProperty("meetingTime", councilMeeting.getMeetingTime());
            else
                jsonObject.addProperty("meetingTime", "");
            if (councilMeeting.getMeetingMOMs() != null)
            	for (MeetingMOM meetingMOM : councilMeeting.getMeetingMOMs()) {
            		 if (meetingMOM.getResolutionDetail() != null)
            			 jsonObject.addProperty("resolutionDetail", meetingMOM.getResolutionDetail());
            		 else
                         jsonObject.addProperty("resolutionDetail", "");
            		 if (meetingMOM.getResolutionStatus() != null)
            		jsonObject.addProperty("resolutionStatus", meetingMOM.getResolutionStatus().getCode());
            		 else
                         jsonObject.addProperty("resolutionStatus", "");
            		 if(meetingMOM.getPreamble().getDepartment()!=null)
            			 jsonObject.addProperty("department", meetingMOM.getPreamble().getDepartment().getName());
            		 else
            			 jsonObject.addProperty("department", "");
				}
            jsonObject.addProperty("id", councilMeeting.getId());
        }
        return jsonObject;
    }
}