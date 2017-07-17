package org.egov.council.web.adaptor;

import java.lang.reflect.Type;

import org.egov.council.entity.CouncilMember;
import org.egov.infra.utils.StringUtils;

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
                jsonObject.addProperty("electionWard", StringUtils.EMPTY);
            if (councilMember.getDesignation() != null)
                jsonObject.addProperty("designation", councilMember.getDesignation().getName());
            else
                jsonObject.addProperty("designation", StringUtils.EMPTY);
            if (councilMember.getPartyAffiliation() != null)
                jsonObject.addProperty("partyAffiliation", councilMember.getPartyAffiliation().getName());
            else
                jsonObject.addProperty("partyAffiliation", StringUtils.EMPTY);
            if (councilMember.getName() != null)
                jsonObject.addProperty("name", councilMember.getName());
            else
                jsonObject.addProperty("name", StringUtils.EMPTY);
            if (councilMember.getStatus() != null)
                jsonObject.addProperty("status", councilMember.getStatus().name());
            else
                jsonObject.addProperty("status", StringUtils.EMPTY);
            if (councilMember.getCategory() != null)
                jsonObject.addProperty("category", councilMember.getCategory());
            else
                jsonObject.addProperty("category", StringUtils.EMPTY);
            jsonObject.addProperty("id", councilMember.getId());
        }
        return jsonObject;
    }
}