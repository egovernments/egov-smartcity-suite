package org.egov.api.adapter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import org.egov.infra.workflow.entity.StateHistory;

import java.lang.reflect.Type;

public class ComplaintStatusAdapter extends DataAdapter<StateHistory> {

	@Override
	public JsonElement serialize(StateHistory stateHistory, Type type, JsonSerializationContext context) {
		JsonObject jo = new JsonObject();
		jo.addProperty("stateId", stateHistory.getState().getId());
		jo.addProperty("value", stateHistory.getValue());
		jo.addProperty("ownerPosotion", stateHistory.getOwnerPosition().getName());
		jo.addProperty("ownerUser", stateHistory.getOwnerPosition().getName());
		if(stateHistory.getOwnerUser() != null)
		  jo.addProperty("createdBy", stateHistory.getOwnerUser().getUsername());
		jo.addProperty("createdDate", stateHistory.getCreatedDate().toString());
		jo.addProperty("lastModifiedBy", stateHistory.getLastModifiedBy().getUsername());
		jo.addProperty("lastModifiedDate", stateHistory.getLastModifiedDate().toString());
		jo.addProperty("comments", stateHistory.getComments());		
		return jo;
	}

}
