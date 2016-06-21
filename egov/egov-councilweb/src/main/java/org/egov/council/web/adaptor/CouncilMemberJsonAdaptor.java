package org.egov.council.web.adaptor;

import java.lang.reflect.Type;

import org.egov.council.entity.CouncilMember;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class CouncilMemberJsonAdaptor implements JsonSerializer<CouncilMember> {
    @Override
    public JsonElement serialize(final CouncilMember councilMember, final Type type, final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (councilMember != null) {
            if (councilMember.getElectionWard() != null)
                jsonObject.addProperty("electionWard", councilMember.getElectionWard().getName());
            else
                jsonObject.addProperty("electionWard", "");
            if (councilMember.getDesignation() != null)
                jsonObject.addProperty("designation", councilMember.getDesignation().getName());
            else
                jsonObject.addProperty("designation", "");
            if (councilMember.getPartyAffiliation() != null)
                jsonObject.addProperty("partyAffiliation", councilMember.getPartyAffiliation().getName());
            else
                jsonObject.addProperty("partyAffiliation", "");
            if (councilMember.getName() != null)
                jsonObject.addProperty("name", councilMember.getName());
            else
                jsonObject.addProperty("name", "");
            jsonObject.addProperty("id", councilMember.getId());
        }
        return jsonObject;
    }
}