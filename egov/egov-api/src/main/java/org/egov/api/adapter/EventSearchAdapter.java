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

import static org.egov.eventnotification.utils.constants.EventnotificationConstants.ADDRESS;
import static org.egov.eventnotification.utils.constants.EventnotificationConstants.CONTACT_NO;
import static org.egov.eventnotification.utils.constants.EventnotificationConstants.DDMMYYYY;
import static org.egov.eventnotification.utils.constants.EventnotificationConstants.DESCRIPTION;
import static org.egov.eventnotification.utils.constants.EventnotificationConstants.DOUBLE_DEFAULT;
import static org.egov.eventnotification.utils.constants.EventnotificationConstants.EMPTY;
import static org.egov.eventnotification.utils.constants.EventnotificationConstants.END_DATE;
import static org.egov.eventnotification.utils.constants.EventnotificationConstants.END_TIME;
import static org.egov.eventnotification.utils.constants.EventnotificationConstants.EVENTTYPE;
import static org.egov.eventnotification.utils.constants.EventnotificationConstants.EVENT_COST;
import static org.egov.eventnotification.utils.constants.EventnotificationConstants.EVENT_FILENAME;
import static org.egov.eventnotification.utils.constants.EventnotificationConstants.EVENT_FILESTOREID;
import static org.egov.eventnotification.utils.constants.EventnotificationConstants.EVENT_HOST;
import static org.egov.eventnotification.utils.constants.EventnotificationConstants.EVENT_ID;
import static org.egov.eventnotification.utils.constants.EventnotificationConstants.EVENT_LOC;
import static org.egov.eventnotification.utils.constants.EventnotificationConstants.INTERESTED_COUNT;
import static org.egov.eventnotification.utils.constants.EventnotificationConstants.ISPAID;
import static org.egov.eventnotification.utils.constants.EventnotificationConstants.MAX_TEN;
import static org.egov.eventnotification.utils.constants.EventnotificationConstants.NAME;
import static org.egov.eventnotification.utils.constants.EventnotificationConstants.NO;
import static org.egov.eventnotification.utils.constants.EventnotificationConstants.START_DATE;
import static org.egov.eventnotification.utils.constants.EventnotificationConstants.START_TIME;
import static org.egov.eventnotification.utils.constants.EventnotificationConstants.URL;
import static org.egov.eventnotification.utils.constants.EventnotificationConstants.USER_INTERESTED;
import static org.egov.eventnotification.utils.constants.EventnotificationConstants.ZERO;

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
        jsonObjectEvent.addProperty(NAME, event.getName());
        jsonObjectEvent.addProperty(DESCRIPTION, event.getDescription());

        DateTime sd = new DateTime(event.getStartDate());
        jsonObjectEvent.addProperty(START_DATE,
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

        jsonObjectEvent.addProperty(START_TIME, startHH.concat(":").concat(startMM));

        DateTime ed = new DateTime(event.getEndDate());
        jsonObjectEvent.addProperty(END_DATE,
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

        jsonObjectEvent.addProperty(END_TIME, endHH.concat(":").concat(endMM));

        jsonObjectEvent.addProperty(EVENT_HOST, event.getEventAddress().getEventHost());
        jsonObjectEvent.addProperty(EVENT_LOC, event.getEventAddress().getEventLocation());
        jsonObjectEvent.addProperty(ADDRESS, event.getEventAddress().getAddress());
        jsonObjectEvent.addProperty(CONTACT_NO, event.getEventAddress().getContactNumber());
        jsonObjectEvent.addProperty(ISPAID, event.isPaid());
        jsonObjectEvent.addProperty(EVENTTYPE, event.getEventType().getName());
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

        if (event.getEventAddress().getUrl() == null)
            jsonObjectEvent.addProperty(URL, EMPTY);
        else
            jsonObjectEvent.addProperty(URL, event.getEventAddress().getUrl());

        jsonObjectEvent.addProperty(USER_INTERESTED, NO);

        Long interestedCount = usereventService.countUsereventByEventId(event.getId());
        if (interestedCount == null)
            jsonObjectEvent.addProperty(INTERESTED_COUNT, ZERO);
        else
            jsonObjectEvent.addProperty(INTERESTED_COUNT, String.valueOf(interestedCount));
        return jsonObjectEvent;
    }
}