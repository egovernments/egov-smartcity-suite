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

import java.lang.reflect.Type;

import org.egov.eventnotification.constants.Constants;
import org.egov.eventnotification.entity.Event;
import org.egov.eventnotification.service.UserEventService;
import org.egov.infra.utils.DateUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

public class EventAdapter extends DataAdapter<Event> {

    private UserEventService usereventService;

    public EventAdapter(UserEventService usereventService) {
        this.usereventService = usereventService;
    }

    @Override
    public JsonElement serialize(Event event, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObjectEvent = new JsonObject();
        jsonObjectEvent.addProperty(Constants.EVENT_ID, event.getId());
        jsonObjectEvent.addProperty(Constants.NAME, event.getName());
        jsonObjectEvent.addProperty(Constants.EVENT_DESC, event.getDescription());

        jsonObjectEvent.addProperty(Constants.EVENT_STARTDATE,
                DateUtils.getDate(DateUtils.getDefaultFormattedDate(event.getStartDate()), Constants.DDMMYYYY).getTime());
        jsonObjectEvent.addProperty(Constants.EVENT_STARTTIME,
                event.getEventDetails().getStartHH() + ":" + event.getEventDetails().getStartMM());

        jsonObjectEvent.addProperty(Constants.EVENT_ENDDATE,
                DateUtils.getDate(DateUtils.getDefaultFormattedDate(event.getEndDate()), Constants.DDMMYYYY).getTime());
        jsonObjectEvent.addProperty(Constants.EVENT_ENDTIME,
                event.getEventDetails().getEndHH() + ":" + event.getEventDetails().getEndMM());

        jsonObjectEvent.addProperty(Constants.EVENT_HOST, event.getEventhost());
        jsonObjectEvent.addProperty(Constants.EVENT_LOCATION, event.getEventlocation());
        jsonObjectEvent.addProperty(Constants.EVENT_ADDRESS, event.getAddress());
        jsonObjectEvent.addProperty(Constants.EVENT_CONTACTNO, event.getContactNumber());
        jsonObjectEvent.addProperty(Constants.EVENT_ISPAID, event.isPaid());
        jsonObjectEvent.addProperty(Constants.EVENT_EVENTTYPE, event.getEventType().getName());
        if (event.getFilestore() == null) {
            jsonObjectEvent.addProperty(Constants.EVENT_FILESTOREID, Constants.EMPTY);
            jsonObjectEvent.addProperty(Constants.EVENT_FILENAME, Constants.EMPTY);
        } else {
            jsonObjectEvent.addProperty(Constants.EVENT_FILESTOREID, event.getFilestore().getFileStoreId());
            jsonObjectEvent.addProperty(Constants.EVENT_FILENAME, event.getFilestore().getFileName());
        }

        if (event.getCost() == null)
            jsonObjectEvent.addProperty(Constants.EVENT_COST, Constants.DOUBLE_DEFAULT);
        else
            jsonObjectEvent.addProperty(Constants.EVENT_COST, event.getCost());

        if (event.getUrl() == null)
            jsonObjectEvent.addProperty(Constants.URL, Constants.EMPTY);
        else
            jsonObjectEvent.addProperty(Constants.URL, event.getUrl());

        jsonObjectEvent.addProperty(Constants.USER_INTERESTED, Constants.NO);

        Long interestedCount = usereventService.countUsereventByEventId(event.getId());
        if (interestedCount == null)
            jsonObjectEvent.addProperty(Constants.INTERESTED_COUNT, Constants.ZERO);
        else
            jsonObjectEvent.addProperty(Constants.INTERESTED_COUNT, String.valueOf(interestedCount));

        return jsonObjectEvent;
    }
}
