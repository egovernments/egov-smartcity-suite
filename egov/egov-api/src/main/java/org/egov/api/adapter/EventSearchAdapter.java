/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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
package org.egov.api.adapter;

import static org.egov.eventnotification.constants.Constants.DDMMYYYY;
import static org.egov.eventnotification.constants.Constants.DOUBLE_DEFAULT;
import static org.egov.eventnotification.constants.Constants.EMPTY;
import static org.egov.eventnotification.constants.Constants.EVENT_ADDRESS;
import static org.egov.eventnotification.constants.Constants.EVENT_CONTACTNO;
import static org.egov.eventnotification.constants.Constants.EVENT_COST;
import static org.egov.eventnotification.constants.Constants.EVENT_DESC;
import static org.egov.eventnotification.constants.Constants.EVENT_ENDDATE;
import static org.egov.eventnotification.constants.Constants.EVENT_ENDTIME;
import static org.egov.eventnotification.constants.Constants.EVENT_EVENTTYPE;
import static org.egov.eventnotification.constants.Constants.EVENT_FILENAME;
import static org.egov.eventnotification.constants.Constants.EVENT_FILESTOREID;
import static org.egov.eventnotification.constants.Constants.EVENT_HOST;
import static org.egov.eventnotification.constants.Constants.EVENT_ID;
import static org.egov.eventnotification.constants.Constants.EVENT_ISPAID;
import static org.egov.eventnotification.constants.Constants.EVENT_LOCATION;
import static org.egov.eventnotification.constants.Constants.EVENT_NAME;
import static org.egov.eventnotification.constants.Constants.EVENT_STARTDATE;
import static org.egov.eventnotification.constants.Constants.EVENT_STARTTIME;
import static org.egov.eventnotification.constants.Constants.INTERESTED_COUNT;
import static org.egov.eventnotification.constants.Constants.MAX_TEN;
import static org.egov.eventnotification.constants.Constants.NO;
import static org.egov.eventnotification.constants.Constants.URL;
import static org.egov.eventnotification.constants.Constants.USER_INTERESTED;
import static org.egov.eventnotification.constants.Constants.ZERO;

import java.lang.reflect.Type;

import org.egov.eventnotification.entity.Event;
import org.egov.eventnotification.service.UserEventService;
import org.egov.infra.utils.DateUtils;
import org.joda.time.DateTime;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

public class EventSearchAdapter extends DataAdapter<Event> {

    private UserEventService usereventService;

    public EventSearchAdapter(UserEventService usereventService) {
        this.usereventService = usereventService;
    }

    @Override
    public JsonElement serialize(Event event, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObjectEvent = new JsonObject();
        jsonObjectEvent.addProperty(EVENT_ID, event.getId());
        jsonObjectEvent.addProperty(EVENT_NAME, event.getName());
        jsonObjectEvent.addProperty(EVENT_DESC, event.getDescription());

        DateTime sd = new DateTime(event.getStartDate());
        jsonObjectEvent.addProperty(EVENT_STARTDATE,
                DateUtils.getDate(DateUtils.getDefaultFormattedDate(event.getStartDate()), DDMMYYYY).getTime());
        String startHH = null;
        String startMM = null;
        if (sd.getHourOfDay() < MAX_TEN)
            startHH = ZERO + String.valueOf(sd.getHourOfDay());
        else
            startHH = String.valueOf(sd.getHourOfDay());

        if (sd.getMinuteOfHour() < MAX_TEN)
            startMM = ZERO + String.valueOf(sd.getMinuteOfHour());
        else
            startMM = String.valueOf(sd.getMinuteOfHour());

        jsonObjectEvent.addProperty(EVENT_STARTTIME, startHH + ":" + startMM);

        DateTime ed = new DateTime(event.getEndDate());
        jsonObjectEvent.addProperty(EVENT_ENDDATE,
                DateUtils.getDate(DateUtils.getDefaultFormattedDate(event.getEndDate()), DDMMYYYY).getTime());
        String endHH = null;
        String endMM = null;
        if (ed.getHourOfDay() < MAX_TEN)
            endHH = ZERO + String.valueOf(ed.getHourOfDay());
        else
            endHH = String.valueOf(ed.getHourOfDay());

        if (ed.getMinuteOfHour() < MAX_TEN)
            endMM = ZERO + String.valueOf(ed.getMinuteOfHour());
        else
            endMM = String.valueOf(ed.getMinuteOfHour());

        jsonObjectEvent.addProperty(EVENT_ENDTIME, endHH + ":" + endMM);

        jsonObjectEvent.addProperty(EVENT_HOST, event.getEventhost());
        jsonObjectEvent.addProperty(EVENT_LOCATION, event.getEventlocation());
        jsonObjectEvent.addProperty(EVENT_ADDRESS, event.getAddress());
        jsonObjectEvent.addProperty(EVENT_CONTACTNO, event.getContactNumber());
        jsonObjectEvent.addProperty(EVENT_ISPAID, event.isPaid());
        jsonObjectEvent.addProperty(EVENT_EVENTTYPE, event.getEventType().getName());
        if (event.getFilestore() == null) {
            jsonObjectEvent.addProperty(EVENT_FILESTOREID, EMPTY);
            jsonObjectEvent.addProperty(EVENT_FILENAME, EMPTY);
        } else {
            jsonObjectEvent.addProperty(EVENT_FILESTOREID, event.getFilestore().getFileStoreId());
            jsonObjectEvent.addProperty(EVENT_FILENAME, event.getFilestore().getFileName());
        }

        if (event.getCost() == null)
            jsonObjectEvent.addProperty(EVENT_COST, DOUBLE_DEFAULT);
        else
            jsonObjectEvent.addProperty(EVENT_COST, event.getCost());

        if (event.getUrl() == null)
            jsonObjectEvent.addProperty(URL, EMPTY);
        else
            jsonObjectEvent.addProperty(URL, event.getUrl());

        jsonObjectEvent.addProperty(USER_INTERESTED, NO);

        Long interestedCount = usereventService.countUsereventByEventId(event.getId());
        if (interestedCount == null)
            jsonObjectEvent.addProperty(INTERESTED_COUNT, ZERO);
        else
            jsonObjectEvent.addProperty(INTERESTED_COUNT, String.valueOf(interestedCount));
        return jsonObjectEvent;
    }
}
