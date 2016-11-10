package org.egov.pgr.entity.es;

import java.util.Objects;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;

import org.egov.pgr.entity.Complaint;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Component;

@Component
public class ComplaintCustomMapper extends CustomMapper<Complaint, ComplaintIndex> {

    @Override
    public void mapAtoB(final Complaint complaint, final ComplaintIndex complaintIndex, final MappingContext context) {
        complaintIndex.setComplainantName(complaint.getComplainant().getName());
        complaintIndex.setComplainantMobile(complaint.getComplainant().getMobile());
        complaintIndex.setComplainantEmail(complaint.getComplainant().getEmail());
        complaintIndex.setComplaintTypeName(complaint.getComplaintType().getName());
        complaintIndex.setComplaintTypeCode(complaint.getComplaintType().getCode());
        complaintIndex.setComplaintStatusName(complaint.getStatus().getName());
        complaintIndex.setAssigneeId(complaint.getAssignee().getId());
        complaintIndex.setAssigneeName(complaint.getAssignee().getName());
        complaintIndex.setDepartmentName(complaint.getDepartment().getName());
        complaintIndex.setDepartmentCode(complaint.getDepartment().getCode());
        if (Objects.nonNull(complaint.getChildLocation())) {
            complaintIndex.setLocalityName(complaint.getChildLocation().getName());
            complaintIndex.setLocalityNo(complaint.getChildLocation().getBoundaryNum().toString());
            if (Objects.nonNull(complaint.getChildLocation().getLongitude()) &&
                    Objects.nonNull(complaint.getChildLocation().getLatitude()))
                complaintIndex.setLocalityGeo(new GeoPoint(complaint.getChildLocation().getLatitude(),
                        complaint.getChildLocation().getLongitude()));
        }
        if (Objects.nonNull(complaint.getLocation())) {
            complaintIndex.setWardName(complaint.getLocation().getName());
            complaintIndex.setWardNo(complaint.getLocation().getBoundaryNum().toString());
            if (Objects.nonNull(complaint.getLocation().getLongitude()) &&
                    Objects.nonNull(complaint.getLocation().getLatitude()))
                complaintIndex.setWardGeo(new GeoPoint(complaint.getLocation().getLatitude(),
                        complaint.getLocation().getLongitude()));
        }
        if (Objects.nonNull(complaint.getCitizenFeedback()))
            complaintIndex.setSatisfactionIndex(complaint.getCitizenFeedback().ordinal());
        if (Objects.nonNull(complaint.getLat()) && Objects.nonNull(complaint.getLng()))
            complaintIndex.setComplaintGeo(new GeoPoint(complaint.getLat(), complaint.getLng()));
    }
}