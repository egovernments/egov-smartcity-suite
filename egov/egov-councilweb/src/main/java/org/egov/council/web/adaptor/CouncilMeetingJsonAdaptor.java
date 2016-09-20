/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2016>  eGovernments Foundation
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

package org.egov.council.web.adaptor;

import java.lang.reflect.Type;

import org.egov.council.entity.CouncilMeeting;
import org.egov.council.entity.MeetingAttendence;
import org.egov.infra.utils.StringUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class CouncilMeetingJsonAdaptor implements JsonSerializer<CouncilMeeting> {
    @Override
    public JsonElement serialize(final CouncilMeeting councilMeeting, final Type type,
            final JsonSerializationContext jsc) {
            final JsonObject jsonObject = new JsonObject();
            int noOfMembersPresent = 0;
            int noOfMembersAbsent = 0;
            if (councilMeeting != null) {
            if (councilMeeting.getMeetingNumber() != null)
                jsonObject.addProperty("meetingNumber", councilMeeting.getMeetingNumber());
            else
                jsonObject.addProperty("meetingNumber", StringUtils.EMPTY);
            if (councilMeeting.getCommitteeType() != null)
                jsonObject.addProperty("committeeType", councilMeeting.getCommitteeType().getName());
            else
                jsonObject.addProperty("committeeType", StringUtils.EMPTY);
            if (councilMeeting.getMeetingDate() != null)
                jsonObject.addProperty("meetingDate", councilMeeting.getMeetingDate().toString());
            else
                jsonObject.addProperty("meetingDate", StringUtils.EMPTY);
            if (councilMeeting.getMeetingDate() != null)
                jsonObject.addProperty("meetingLocation", councilMeeting.getMeetingLocation());
            else
                jsonObject.addProperty("meetingLocation", StringUtils.EMPTY);
            if (councilMeeting.getMeetingTime() != null)
                jsonObject.addProperty("meetingTime", councilMeeting.getMeetingTime());
            else
                jsonObject.addProperty("meetingTime", StringUtils.EMPTY);
            
            if (councilMeeting.getStatus() != null)
                jsonObject.addProperty("meetingStatus", councilMeeting.getStatus().getCode());
            else
                jsonObject.addProperty("meetingStatus", StringUtils.EMPTY);
            if (councilMeeting.getMeetingDate() != null)
                jsonObject.addProperty("meetingCreatedDate", councilMeeting.getCreatedDate().toString());
            else
                jsonObject.addProperty("meetingCreatedDate", StringUtils.EMPTY);
            
          /*  if (councilMeeting.getMeetingMOMs() != null)
                for (MeetingMOM meetingMOM : councilMeeting.getMeetingMOMs()) {
                    if (meetingMOM.getResolutionDetail() != null)
                        jsonObject.addProperty("resolutionDetail", meetingMOM.getResolutionDetail());
                    else
                        jsonObject.addProperty("resolutionDetail", StringUtils.EMPTY);
                    if (meetingMOM.getResolutionStatus() != null)
                        jsonObject.addProperty("resolutionStatus", meetingMOM.getResolutionStatus().getCode());
                    else
                        jsonObject.addProperty("resolutionStatus", StringUtils.EMPTY);
                    if (meetingMOM.getPreamble().getDepartment() != null)
                        jsonObject.addProperty("department", meetingMOM.getPreamble().getDepartment().getName());
                    else
                        jsonObject.addProperty("department", StringUtils.EMPTY);
                }*/
            if (councilMeeting.getMeetingAttendence() != null)
                jsonObject.addProperty("totCommitteMemCount", councilMeeting.getMeetingAttendence().size());
            else
                jsonObject.addProperty("totCommitteMemCount", StringUtils.EMPTY);
            if(councilMeeting.getMeetingAttendence() != null){
                for (MeetingAttendence attendence : councilMeeting.getMeetingAttendence()) {
                    if (attendence.getAttendedMeeting() == true) {
                        noOfMembersPresent++;
                    } else {
                        noOfMembersAbsent++;
                    }
                }
            }
            if (councilMeeting.getMeetingAttendence() != null)
                jsonObject.addProperty("noOfMembersPresent", noOfMembersPresent);
            else
                jsonObject.addProperty("noOfMembersPresent", StringUtils.EMPTY);
            if (councilMeeting.getMeetingAttendence() != null)
                jsonObject.addProperty("noOfMembersAbsent", noOfMembersAbsent);
            else
                jsonObject.addProperty("noOfMembersAbsent", StringUtils.EMPTY);

            jsonObject.addProperty("id", councilMeeting.getId());
        }
        return jsonObject;
    }
}