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
        jo.addProperty("complainantName", complaint.getComplainant().getName());
        if (complaint.getLocation() != null) {
            jo.addProperty("locationName", complaint.getLocation().getLocalName());
        } else if (complaint.getLat() > 0 && complaint.getLat() > 0) {
            jo.addProperty("lat", complaint.getLat());
            jo.addProperty("lng", complaint.getLng());
        }
        if (complaint.getComplaintType() != null)
            jo.addProperty("complaintTypeId", complaint.getComplaintType().getId());
        if (complaint.getComplaintType() != null)
            jo.addProperty("complaintTypeName", complaint.getComplaintType().getName());
        if (complaint.getLandmarkDetails() != null)
            jo.addProperty("landmarkDetails", complaint.getLandmarkDetails());
        jo.addProperty("createdDate", complaint.getCreatedDate().toDateTime().toString());
        jo.addProperty("supportDocsSize", complaint.getSupportDocs().size());

        return jo;
    }

}