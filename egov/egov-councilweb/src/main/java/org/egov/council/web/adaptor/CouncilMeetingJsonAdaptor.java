/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */

package org.egov.council.web.adaptor;

import static org.egov.council.utils.constants.CouncilConstants.ATTENDANCEFINALIZED;
import static org.egov.council.utils.constants.CouncilConstants.MEETINGUSEDINRMOM;
import static org.egov.council.utils.constants.CouncilConstants.MOM_FINALISED;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.egov.council.entity.CommitteeMembers;
import org.egov.council.entity.CouncilMeeting;
import org.egov.council.entity.CouncilMemberStatus;
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
            int totCommitteMemCount = 0;
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
            List<Long> committeeMembersId=new ArrayList<>();
            if (ATTENDANCEFINALIZED.equalsIgnoreCase(councilMeeting.getStatus().getCode())
                    || MOM_FINALISED.equalsIgnoreCase(councilMeeting.getStatus().getCode())
                    || MEETINGUSEDINRMOM.equalsIgnoreCase(councilMeeting.getStatus().getCode())) {
                totCommitteMemCount = councilMeeting.getMeetingAttendence().size();
                jsonObject.addProperty("totCommitteMemCount", councilMeeting.getMeetingAttendence().size());
            } else if (councilMeeting.getMeetingAttendence() != null) {
                for (CommitteeMembers committeeMembers : councilMeeting.getCommitteeType().getCommiteemembers()) {
                    if (CouncilMemberStatus.ACTIVE.equals(committeeMembers.getCouncilMember().getStatus())) {
                        totCommitteMemCount++;
                        committeeMembersId.add(committeeMembers.getCouncilMember().getId());
                    }
                }
                if (!ATTENDANCEFINALIZED.equalsIgnoreCase(councilMeeting.getStatus().getCode())
                        && !MOM_FINALISED.equalsIgnoreCase(councilMeeting.getStatus().getCode())
                        && !MEETINGUSEDINRMOM.equalsIgnoreCase(councilMeeting.getStatus().getCode()))
                    jsonObject.addProperty("totCommitteMemCount", totCommitteMemCount);
            }

            if (councilMeeting.getMeetingAttendence() != null) {
                for (MeetingAttendence attendence : councilMeeting.getMeetingAttendence()) {
                    if (attendence.getAttendedMeeting()) {
                        if (ATTENDANCEFINALIZED.equalsIgnoreCase(councilMeeting.getStatus().getCode())
                                || MOM_FINALISED.equalsIgnoreCase(councilMeeting.getStatus().getCode())
                                || MEETINGUSEDINRMOM.equalsIgnoreCase(councilMeeting.getStatus().getCode())) {
                            noOfMembersPresent++;
                        }else if(committeeMembersId.indexOf(attendence.getCouncilMember().getId())>-1){
                            noOfMembersPresent++;
                        }
                    }
                }
            }
            noOfMembersAbsent = totCommitteMemCount - noOfMembersPresent;

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
