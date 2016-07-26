package org.egov.council.web.adaptor;

import java.lang.reflect.Type;

import org.egov.council.entity.CouncilMeeting;

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
            jsonObject.addProperty("id", councilMeeting.getId());
        }
        return jsonObject;
    }
}