/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2017  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.pgr.elasticsearch.entity.contract;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.egov.pgr.elasticsearch.entity.ComplaintIndex;
import org.egov.pgr.entity.Complaint;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ComplaintCustomMapper extends CustomMapper<Complaint, ComplaintIndex> {

    @Override
    public void mapAtoB(final Complaint complaint, final ComplaintIndex complaintIndex, final MappingContext context) {
        complaintIndex.setComplainantName(complaint.getComplainant().getName());
        complaintIndex.setComplainantMobile(complaint.getComplainant().getMobile());
        complaintIndex.setComplainantEmail(complaint.getComplainant().getEmail());
        complaintIndex.setComplaintTypeName(complaint.getComplaintType().getName());
        complaintIndex.setComplaintTypeCode(complaint.getComplaintType().getCode());
        complaintIndex.setCategoryId(complaint.getComplaintType().getCategory().getId());
        complaintIndex.setCategoryName(complaint.getComplaintType().getCategory().getName());
        complaintIndex.setComplaintStatusName(complaint.getStatus().getName());
        complaintIndex.setAssigneeId(complaint.getAssignee().getId());
        complaintIndex.setAssigneeName(complaint.getAssignee().getName());
        complaintIndex.setDepartmentName(complaint.getDepartment().getName());
        complaintIndex.setDepartmentCode(complaint.getDepartment().getCode());
        complaintIndex.setReceivingMode(complaint.getReceivingMode().getCode());
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