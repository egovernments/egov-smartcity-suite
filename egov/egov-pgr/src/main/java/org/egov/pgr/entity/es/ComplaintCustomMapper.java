package org.egov.pgr.entity.es;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;

import org.egov.pgr.entity.Complaint;
import org.springframework.stereotype.Component;

@Component
public class ComplaintCustomMapper extends ConfigurableMapper{

	protected void configure(MapperFactory factory) {
		factory.classMap(Complaint.class, ComplaintIndex.class)
        .field("complainant.name", "complainantName")
        .field("complainant.mobile", "complainantMobile")
        .field("complainant.email", "complainantEmail")
        .field("complaintType.name","complaintTypeName")
        .field("assignee.name", "assigneeName")
        .field("department.name", "departmentName")
        .field("location.name", "locationName")
        .field("location.boundaryLocation", "wardGeo")
        .field("childLocation.name", "localityName")
        .field("childLocation.boundaryLocation", "localityGeo")
        .field("status.name", "complaintStatusName")
        .byDefault()
        .register();
    }
}
