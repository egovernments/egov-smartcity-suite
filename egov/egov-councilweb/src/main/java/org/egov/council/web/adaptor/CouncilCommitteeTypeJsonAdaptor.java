package org.egov.council.web.adaptor;

import java.lang.reflect.Type;




import org.egov.council.entity.CommitteeType;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class CouncilCommitteeTypeJsonAdaptor implements
		JsonSerializer<CommitteeType> {
	@Override
	public JsonElement serialize(final CommitteeType committeeType,
			final Type type, final JsonSerializationContext jsc) {
		final JsonObject jsonObject = new JsonObject();
		if (committeeType != null) {
			if (committeeType.getName() != null)
				jsonObject.addProperty("name", committeeType.getName());
			else
				jsonObject.addProperty("name", "");
			if (committeeType.getIsActive() != null)
				jsonObject.addProperty("isActive", committeeType.getIsActive());
			else
				jsonObject.addProperty("isActive", "");
			jsonObject.addProperty("id", committeeType.getId());
		}
		return jsonObject;
	}
}
