package org.egov.pgr.entity.es;

import java.util.Objects;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;

import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.pgr.entity.Complaint;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Component;

@Component
public class ComplaintCustomMapper extends CustomMapper<Complaint, ComplaintIndex>{
	
	@Override
	public void mapAtoB(Complaint complaint,ComplaintIndex complaintIndex, MappingContext context)
	{
		complaintIndex.setComplainantName(complaint.getComplainant().getName());
		complaintIndex.setComplainantMobile(complaint.getComplainant().getMobile());
		complaintIndex.setComplainantEmail(complaint.getComplainant().getEmail());
		complaintIndex.setComplaintTypeName(complaint.getComplaintType().getName());
		complaintIndex.setComplaintTypeCode(complaint.getComplaintType().getCode());
		complaintIndex.setComplaintStatusName(complaint.getStatus().getName());
		complaintIndex.setAssigneeName(complaint.getAssignee().getName());
		complaintIndex.setDepartmentName(complaint.getDepartment().getName());
		complaintIndex.setDepartmentCode(complaint.getDepartment().getCode());
		complaintIndex.setLocalityName(complaint.getLocation().getName());
		complaintIndex.setLocalityNo(complaint.getChildLocation().getBoundaryNum().toString());
	    if(Objects.nonNull(complaint.getChildLocation().getLongitude()) && 
	    		Objects.nonNull(complaint.getChildLocation().getLatitude()))
		complaintIndex.setLocalityGeo(new GeoPoint(complaint.getChildLocation().getLatitude(),
				complaint.getChildLocation().getLongitude()));
		complaintIndex.setWardName(complaint.getChildLocation().getName());
		complaintIndex.setWardNo(complaint.getChildLocation().getBoundaryNum().toString());
		if(Objects.nonNull(complaint.getLocation().getLongitude()) && 
	    		Objects.nonNull(complaint.getLocation().getLatitude()))
		complaintIndex.setWardGeo(new GeoPoint(complaint.getLocation().getLatitude(),
				complaint.getLocation().getLongitude()));
//		complaintIndex.setComplaintGeo();
//		complaintIndex.setSatisfactionIndex(); //Approach to be decided 
	}
}
