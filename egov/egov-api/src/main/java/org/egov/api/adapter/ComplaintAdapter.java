package org.egov.api.adapter;

import java.lang.reflect.Type;
import org.egov.pgr.entity.Complaint;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

public class ComplaintAdapter extends DataAdapter<Complaint> {

    @Override
    public JsonElement serialize(Complaint complaint, Type type, JsonSerializationContext context) {
        JsonObject jo = new JsonObject(); 
        jo.addProperty("detail", complaint.getDetails());
        jo.addProperty("crn", complaint.getCrn());
        jo.addProperty("status", complaint.getStatus().getName());
        jo.addProperty("lastModifiedBy", complaint.getLastModifiedBy().getUsername());
        jo.addProperty("lastModifiedDate", complaint.getLastModifiedDate().toString());
        jo.addProperty("complainantName", complaint.getCreatedBy().getName());
        
        if (complaint.getLat() > 0 && complaint.getLng() > 0) {
            jo.addProperty("lat", complaint.getLat());
            jo.addProperty("lng", complaint.getLng());
        } else if (complaint.getLocation() != null) {
        	if(complaint.getChildLocation().getLocalName() !=null)
        	{
        		jo.addProperty("childLocationName", complaint.getChildLocation().getLocalName());
        	}
            jo.addProperty("locationName", complaint.getLocation().getLocalName());
        }
        
        if (complaint.getComplaintType() != null) {
            jo.addProperty("complaintTypeId", complaint.getComplaintType().getId());
            jo.addProperty("complaintTypeName", complaint.getComplaintType().getName());
            jo.addProperty("complaintTypeImage", complaint.getComplaintType().getCode()+".jpg");
        }                 
        if (complaint.getLandmarkDetails() != null)
            jo.addProperty("landmarkDetails", complaint.getLandmarkDetails());
        jo.addProperty("createdDate", complaint.getCreatedDate().toString());
        jo.addProperty("supportDocsSize", complaint.getSupportDocs().size());

        return jo;
    }

}