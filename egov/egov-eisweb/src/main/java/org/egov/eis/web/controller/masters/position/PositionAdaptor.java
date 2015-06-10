package org.egov.eis.web.controller.masters.position;

import java.lang.reflect.Type;

import org.egov.pims.commons.Position;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class PositionAdaptor implements JsonSerializer<Position> {
	@Override
    public JsonElement serialize(final Position position, final Type type,
            final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", null != position.getName() ? position.getName() : "NA");
        jsonObject.addProperty("outSourcedPost", position.getIsPostOutsourced());
        jsonObject.addProperty("positionId", position.getId());
        jsonObject.addProperty("departmentName", position.getDeptDesig().getDepartment().getName());
        jsonObject.addProperty("designationName", position.getDeptDesig().getDesignation().getName());
        jsonObject.addProperty("isOutSourced", (position.isPostOutsourced()?1:0));
        jsonObject.addProperty("outSourcedPostCount", position.getDeptDesig().getOutsourcedPosts());
        jsonObject.addProperty("sanctionedPostCount", position.getDeptDesig().getSanctionedPosts());
        return jsonObject;
    }
}
