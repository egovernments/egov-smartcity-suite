package org.egov.api.adapter;

import java.lang.reflect.Type;

import org.egov.pgr.entity.ComplaintType;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

public class ComplaintTypeAdapter extends DataAdapter<ComplaintType> {

	@Override
	public JsonElement serialize(ComplaintType compaintType, Type arg1, JsonSerializationContext arg2) {
		JsonObject jo = new JsonObject();
		jo.addProperty("name", compaintType.getName());
		jo.addProperty("id", compaintType.getId());
		jo.addProperty("typeImage", compaintType.getCode()+".jpg");		
		jo.addProperty("description", null != compaintType.getDescription() ? compaintType.getDescription() : "N/A");
		return jo;
	}

}