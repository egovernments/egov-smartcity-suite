package org.egov.egf.web.adaptor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.egov.commons.CFunction;

import java.lang.reflect.Type;

public class FunctionJsonAdaptor implements JsonSerializer<CFunction> {
	@Override
	public JsonElement serialize(final CFunction function, final Type type,
			final JsonSerializationContext jsc) {
		final JsonObject jsonObject = new JsonObject();
		if (function != null) {
			if (function.getName() != null)
				jsonObject.addProperty("name", function.getName());
			else
				jsonObject.addProperty("name", "");
			if (function.getIsActive() != null)
				jsonObject.addProperty("isActive", function.getIsActive());
			else
				jsonObject.addProperty("isActive", "");
			if (function.getCode() != null)
				jsonObject.addProperty("code", function.getCode());
			else
				jsonObject.addProperty("code", "");
			if (function.getParentId()!= null)
				jsonObject.addProperty("parentType", function.getParentId().getName());
			else
				jsonObject.addProperty("parentType", "");
			jsonObject.addProperty("id", function.getId());
		}
		return jsonObject;
	}
}