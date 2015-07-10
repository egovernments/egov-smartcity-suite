package org.egov.api.adapter;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import org.egov.portal.entity.Citizen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

public class UserAdapter extends DataAdapter<Citizen> {

	@Override
	public JsonElement serialize(Citizen citizen, Type type, JsonSerializationContext context) {
		JsonObject jo = new JsonObject();
		if(citizen.getName() != null)
		  jo.addProperty("name", citizen.getName());
		jo.addProperty("emailId", citizen.getEmailId());
		jo.addProperty("mobileNumber", citizen.getMobileNumber());
		jo.addProperty("userName", citizen.getUsername());
		if(citizen.getAltContactNumber() != null)
			  jo.addProperty("altContactNumber", citizen.getAltContactNumber());
		if(citizen.getGender() != null)
			  jo.addProperty("gender", citizen.getGender().name());
		if(citizen.getPan() != null)
		  jo.addProperty("panCard", citizen.getPan());
		if(citizen.getDob() != null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String dt =sdf.format(citizen.getDob());			
			jo.addProperty("dob", dt);
		}
		if(citizen.getAadhaarNumber() != null)
		  jo.addProperty("aadhaarCard", citizen.getAadhaarNumber());
		if(citizen.getLocale() != null)
		  jo.addProperty("preferredLanguage", citizen.getLocale());
		return jo;
	}

}