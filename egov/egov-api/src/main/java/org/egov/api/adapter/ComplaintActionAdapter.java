package org.egov.api.adapter;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import org.egov.api.model.ComplaintAction;
import org.egov.infra.admin.master.entity.Department;
import org.egov.pgr.entity.ComplaintStatus;

import java.lang.reflect.Type;

public class ComplaintActionAdapter extends DataAdapter<ComplaintAction> {
	
	@Override
	public JsonElement serialize(ComplaintAction complaintDetails, Type typeOfSrc,
			JsonSerializationContext context) {
		// TODO Auto-generated method stub
		JsonObject jo = new JsonObject(); 
		
		JsonArray jsonArry=new JsonArray();
		for(ComplaintStatus complaintStatus:complaintDetails.getStatus())
		{
			JsonObject jObj=new JsonObject();
			jObj.addProperty("id", complaintStatus.getId());
			jObj.addProperty("name", complaintStatus.getName());
			jsonArry.add(jObj);
		}
		
		jo.add("status", jsonArry);
		
		jsonArry=new JsonArray();
		for(Department dept:complaintDetails.getApprovalDepartments())
		{
			JsonObject jObj=new JsonObject();
			jObj.addProperty("id", dept.getId());
			jObj.addProperty("name", dept.getName());
			jsonArry.add(jObj);
		}
		
		jo.add("department", jsonArry);
		
		return jo;
	}

}
