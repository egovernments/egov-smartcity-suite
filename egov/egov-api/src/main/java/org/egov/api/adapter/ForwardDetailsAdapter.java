package org.egov.api.adapter;

import java.lang.reflect.Type;

import org.egov.api.model.ForwardDetails;
import org.egov.infra.admin.master.entity.User;
import org.egov.pims.commons.Designation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

public class ForwardDetailsAdapter extends DataAdapter<ForwardDetails> {
	
	@Override
	public JsonElement serialize(ForwardDetails src, Type typeOfSrc,
			JsonSerializationContext context) {
	
		JsonObject jsonObject=new JsonObject();
		
		if(src.getUsers()!=null)
		{
			JsonArray jUsers=new JsonArray();
			for(User user : src.getUsers())
			{
				JsonObject jUser=new JsonObject();
				jUser.addProperty("id", user.getId());
				jUser.addProperty("name", user.getName());
				jUsers.add(jUser);
			}
			jsonObject.add("users", jUsers);
			
		}
		else if(src.getDesignations()!=null){
			JsonArray jDesingations=new JsonArray();
			for(Designation designation : src.getDesignations())
			{
				JsonObject jDes=new JsonObject();
				jDes.addProperty("id", designation.getId());
				jDes.addProperty("name", designation.getName());
				jDesingations.add(jDes);
			}
			jsonObject.add("designations", jDesingations);
		}
		
		return jsonObject;
	}
	
}
