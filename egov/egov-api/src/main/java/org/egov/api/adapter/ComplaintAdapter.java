/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.api.adapter;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.utils.StringUtils;
import org.egov.pgr.entity.Complaint;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
        jo.addProperty("complainantMobileNo", complaint.getComplainant().getMobile());
        jo.addProperty("complainantEmail", StringUtils.isNotBlank(complaint.getComplainant().getEmail())?complaint.getComplainant().getEmail():"");
        jo.addProperty("citizenFeedback", complaint.getCitizenFeedback()!=null?complaint.getCitizenFeedback().name():"");
        
        if(complaint.getReceivingMode()!=null)
        {
        	JsonObject receivingMode=new JsonObject();
        	receivingMode.addProperty("name", complaint.getReceivingMode().getName());
        	receivingMode.addProperty("code", complaint.getReceivingMode().getCode());
        	jo.add("receivingMode", receivingMode);
        }
        
        if(complaint.getPriority()!=null)
        {
        	JsonObject priority=new JsonObject();
        	priority.addProperty("name", complaint.getPriority().getName());
        	priority.addProperty("code", complaint.getPriority().getCode());
        	priority.addProperty("weight", complaint.getPriority().getWeight());
        	jo.add("priority", priority);
        }
        
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
        
       /* Collections.sort(supportDocs, new Comparator<FileStoreMapper>() {
			@Override
			public int compare(FileStoreMapper f1, FileStoreMapper f2) {

				return f1.getIndexId().compareTo(f2.getIndexId());
			}
        });*/
        
        JsonArray jsonArry=new JsonArray();
        for(FileStoreMapper file:supportDocs)
        {
            JsonObject fileobj=new JsonObject();
            fileobj.addProperty("fileId", file.getFileStoreId());
            fileobj.addProperty("fileContentType", file.getContentType());
           // fileobj.addProperty("fileIndexId", file.getIndexId());
            jsonArry.add(fileobj);
        }
        
        jo.add("supportDocs", jsonArry);

        return jo;
    }

}
