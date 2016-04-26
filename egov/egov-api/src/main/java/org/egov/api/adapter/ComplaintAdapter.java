package org.egov.api.adapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONArray;

import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.pgr.entity.Complaint;

import com.google.gson.JsonArray;
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
        
        
        //sorting files based on index
        List<FileStoreMapper> supportDocs=new ArrayList<FileStoreMapper>();
        supportDocs.addAll(complaint.getSupportDocs());
        
        Collections.sort(supportDocs, new Comparator<FileStoreMapper>() {
			@Override
			public int compare(FileStoreMapper f1, FileStoreMapper f2) {
				// TODO Auto-generated method stub
				return f1.getIndexId().compareTo(f2.getIndexId());
			}
        });
        
        JsonArray jsonArry=new JsonArray();
        for(FileStoreMapper file:supportDocs)
        {
            JsonObject fileobj=new JsonObject();
            fileobj.addProperty("fileId", file.getFileStoreId());
            fileobj.addProperty("fileContentType", file.getContentType());
            fileobj.addProperty("fileIndexId", file.getIndexId());
            jsonArry.add(fileobj);
        }
        
        jo.add("supportDocs", jsonArry);

        return jo;
    }

}