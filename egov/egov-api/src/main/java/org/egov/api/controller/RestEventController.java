package org.egov.api.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.api.controller.core.ApiResponse;
import org.egov.api.controller.core.ApiUrl;
import org.egov.eventnotification.constants.EventnotificationConstant;
import org.egov.eventnotification.entity.Event;
import org.egov.eventnotification.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * This is a Event rest controller. This is used for expose the rest API of event.
 * @author somvit
 *
 */
@org.springframework.web.bind.annotation.RestController
public class RestEventController implements EventnotificationConstant {

    @Autowired
    private EventService eventService;

    /**
     * This method is used for fetch all events
     * @return json string
     */
    @RequestMapping(value = ApiUrl.GET_ALL_EVENT, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getAllEvent() {
        List<Event> eventList = eventService.findAll(new Date());
        
        JsonArray jsonArrayEvent = new JsonArray();
        SimpleDateFormat sdf = new SimpleDateFormat(DDMMYYYY);
        for (Event event : eventList) {
            JsonObject jsonObjectEvent = new JsonObject();
            jsonObjectEvent.addProperty(EVENT_ID, event.getId());
            jsonObjectEvent.addProperty(EVENT_NAME, event.getName());
            jsonObjectEvent.addProperty(EVENT_DESC, event.getDescription());
            jsonObjectEvent.addProperty(EVENT_STARTDATE, event.getStartDate());
            jsonObjectEvent.addProperty(EVENT_STARTTIME, event.getStartTime());
            jsonObjectEvent.addProperty(EVENT_ENDDATE, event.getEndDate());
            jsonObjectEvent.addProperty(EVENT_ENDTIME, event.getEndTime());
            jsonObjectEvent.addProperty(EVENT_HOST, event.getEventhost());
            jsonObjectEvent.addProperty(EVENT_LOCATION, event.getEventlocation());
            jsonObjectEvent.addProperty(EVENT_ADDRESS, event.getAddress());
            jsonObjectEvent.addProperty(EVENT_ISPAID, event.getIspaid());
            jsonObjectEvent.addProperty(EVENT_EVENTTYPE, event.getEventType());
            if (event.getFilestore() != null) {
                jsonObjectEvent.addProperty(EVENT_FILESTOREID, event.getFilestore().getFileStoreId());
                jsonObjectEvent.addProperty(EVENT_FILENAME, event.getFilestore().getFileName());
            } else {
                jsonObjectEvent.addProperty(EVENT_FILESTOREID, "");
                jsonObjectEvent.addProperty(EVENT_FILENAME, "");
            }
            if (event.getCost() == null)
                jsonObjectEvent.addProperty(EVENT_COST, 0.0);
            else
                jsonObjectEvent.addProperty(EVENT_COST, event.getCost());

            jsonArrayEvent.add(jsonObjectEvent);

        }
        
        return jsonArrayEvent.toString();
    }

    /**
     * This method is used for fetch event by id.
     * @param id
     * @return json string
     */
    @RequestMapping(value = ApiUrl.GET_EVENT
            + ApiUrl.EVENT_ID, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getEvent(@PathVariable long id) {
        ApiResponse.newInstance();
        Event event = eventService.findById(id);
        SimpleDateFormat sdf = new SimpleDateFormat(DDMMYYYY);

        JsonObject jsonObjectEvent = new JsonObject();
        jsonObjectEvent.addProperty(EVENT_ID, event.getId());
        jsonObjectEvent.addProperty(EVENT_NAME, event.getName());
        jsonObjectEvent.addProperty(EVENT_DESC, event.getDescription());
        jsonObjectEvent.addProperty(EVENT_STARTDATE, event.getStartDate());
        jsonObjectEvent.addProperty(EVENT_STARTTIME, event.getStartTime());
        jsonObjectEvent.addProperty(EVENT_ENDDATE, event.getEndDate());
        jsonObjectEvent.addProperty(EVENT_ENDTIME, event.getEndTime());
        jsonObjectEvent.addProperty(EVENT_HOST, event.getEventhost());
        jsonObjectEvent.addProperty(EVENT_LOCATION, event.getEventlocation());
        jsonObjectEvent.addProperty(EVENT_ADDRESS, event.getAddress());
        jsonObjectEvent.addProperty(EVENT_ISPAID, event.getIspaid());
        jsonObjectEvent.addProperty(EVENT_EVENTTYPE, event.getEventType());
        if (event.getFilestore() != null) {
            jsonObjectEvent.addProperty(EVENT_FILESTOREID, event.getFilestore().getFileStoreId());
            jsonObjectEvent.addProperty(EVENT_FILENAME, event.getFilestore().getFileName());
        } else {
            jsonObjectEvent.addProperty(EVENT_FILESTOREID, "");
            jsonObjectEvent.addProperty(EVENT_FILENAME, "");
        }
        if (event.getCost() == null)
            jsonObjectEvent.addProperty(EVENT_COST, 0.0);
        else
            jsonObjectEvent.addProperty(EVENT_COST, event.getCost());

        return jsonObjectEvent.toString();
    }

    /**
     * This method is used for search event.
     * @param eventType
     * @param eventName
     * @param eventHost
     * @return json string
     */
    @RequestMapping(value = ApiUrl.SEARCH_EVENT, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String searchEvent(@RequestParam(required = false, value = EVENT_EVENTTYPE) String eventType,
            @RequestParam(required = false, value = EVENT_EVENTNAME) String eventName,
            @RequestParam(required = false, value = EVENT_EVENTHOST) String eventHost) {
        ApiResponse.newInstance();
        List<Event> eventList = eventService.searchEvent(eventType, eventName, eventHost,new Date());
        JsonArray jsonArrayEvent = new JsonArray();
        for (Event event : eventList) {
            JsonObject jsonObjectEvent = new JsonObject();
            jsonObjectEvent.addProperty(EVENT_ID, event.getId());
            jsonObjectEvent.addProperty(EVENT_NAME, event.getName());
            jsonObjectEvent.addProperty(EVENT_DESC, event.getDescription());
            jsonObjectEvent.addProperty(EVENT_STARTDATE, event.getStartDate());
            jsonObjectEvent.addProperty(EVENT_STARTTIME, event.getStartTime());
            jsonObjectEvent.addProperty(EVENT_ENDDATE, event.getEndDate());
            jsonObjectEvent.addProperty(EVENT_ENDTIME, event.getEndTime());
            jsonObjectEvent.addProperty(EVENT_HOST, event.getEventhost());
            jsonObjectEvent.addProperty(EVENT_LOCATION, event.getEventlocation());
            jsonObjectEvent.addProperty(EVENT_ADDRESS, event.getAddress());
            jsonObjectEvent.addProperty(EVENT_ISPAID, event.getIspaid());
            jsonObjectEvent.addProperty(EVENT_EVENTTYPE, event.getEventType());
            if (event.getFilestore() != null) {
                jsonObjectEvent.addProperty(EVENT_FILESTOREID, event.getFilestore().getFileStoreId());
                jsonObjectEvent.addProperty(EVENT_FILENAME, event.getFilestore().getFileName());
            } else {
                jsonObjectEvent.addProperty(EVENT_FILESTOREID, "");
                jsonObjectEvent.addProperty(EVENT_FILENAME, "");
            }
            if (event.getCost() == null)
                jsonObjectEvent.addProperty(EVENT_COST, 0.0);
            else
                jsonObjectEvent.addProperty(EVENT_COST, event.getCost());

            jsonArrayEvent.add(jsonObjectEvent);

        }
        return jsonArrayEvent.toString();
    }

}
