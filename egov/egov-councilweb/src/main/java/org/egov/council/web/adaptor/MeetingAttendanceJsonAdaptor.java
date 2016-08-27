package org.egov.council.web.adaptor;

import java.lang.reflect.Type;

import org.egov.council.entity.CouncilMeeting;
import org.egov.council.entity.MeetingAttendence;
import org.egov.infra.utils.StringUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class MeetingAttendanceJsonAdaptor implements JsonSerializer<MeetingAttendence> {
    @Override
    public JsonElement serialize(final MeetingAttendence attendence, final Type type, final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (attendence != null) {
            if (attendence.getMeeting().getCommitteeType() != null)
                jsonObject.addProperty("committeeType", attendence.getMeeting().getCommitteeType().getName());
            else
                jsonObject.addProperty("committeeType", StringUtils.EMPTY);
            if (attendence.getMeeting().getMeetingDate() != null)
                jsonObject.addProperty("meetingDate", attendence.getMeeting().getMeetingDate().toString());
            else
                jsonObject.addProperty("meetingDate", StringUtils.EMPTY);
            
                if (attendence.getCommitteeMembers().getCouncilMember().getElectionWard() != null)
                    jsonObject.addProperty("electionWard", attendence.getCommitteeMembers().getCouncilMember().getElectionWard().getName());
                else
                    jsonObject.addProperty("electionWard",  StringUtils.EMPTY);
                if (attendence.getCommitteeMembers().getCouncilMember().getDesignation() != null)
                    jsonObject.addProperty("designation", attendence.getCommitteeMembers().getCouncilMember().getDesignation().getName());
                else
                    jsonObject.addProperty("designation", StringUtils.EMPTY);
                if (attendence.getCommitteeMembers().getCouncilMember().getQualification() != null)
                    jsonObject.addProperty("qualification", attendence.getCommitteeMembers().getCouncilMember().getQualification().getName());
                else
                    jsonObject.addProperty("qualification", StringUtils.EMPTY);
                if (attendence.getCommitteeMembers().getCouncilMember().getPartyAffiliation() != null)
                    jsonObject.addProperty("partyAffiliation", attendence.getCommitteeMembers().getCouncilMember().getPartyAffiliation().getName());
                else
                    jsonObject.addProperty("partyAffiliation", StringUtils.EMPTY);
                if (attendence.getCommitteeMembers().getCouncilMember().getName() != null)
                    jsonObject.addProperty("name", attendence.getCommitteeMembers().getCouncilMember().getName());
                else
                    jsonObject.addProperty("name", StringUtils.EMPTY);
                if (attendence.getCommitteeMembers().getCouncilMember().getMobileNumber() != null)
                    jsonObject.addProperty("mobileNumber", attendence.getCommitteeMembers().getCouncilMember().getMobileNumber());
                else
                    jsonObject.addProperty("mobileNumber", StringUtils.EMPTY);
                if (attendence.getCommitteeMembers().getCouncilMember().getResidentialAddress() != null)
                    jsonObject.addProperty("address", attendence.getCommitteeMembers().getCouncilMember().getResidentialAddress());
                else
                    jsonObject.addProperty("address", StringUtils.EMPTY);
               
            jsonObject.addProperty("id", attendence.getMeeting().getId());
        }
        return jsonObject;
        }
}