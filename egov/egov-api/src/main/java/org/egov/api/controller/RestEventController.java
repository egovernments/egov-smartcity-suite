package org.egov.api.controller;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.egov.api.controller.core.ApiResponse;
import org.egov.api.controller.core.ApiUrl;
import org.egov.eventnotification.constants.EventnotificationConstant;
import org.egov.eventnotification.entity.Event;
import org.egov.eventnotification.entity.Userevent;
import org.egov.eventnotification.service.EventService;
import org.egov.eventnotification.service.UsereventService;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
public class RestEventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private UsereventService usereventService;

    @Autowired
    private MessageSource messageSource;

    /**
     * This method is used for fetch all events
     * @return json string
     */
    @RequestMapping(value = ApiUrl.GET_ALL_EVENT, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getAllEvent() {
        List<Event> eventList = eventService.findAll(new Date(), EventnotificationConstant.ACTIVE);

        JsonArray jsonArrayEvent = new JsonArray();
        for (Event event : eventList) {
            JsonObject jsonObjectEvent = new JsonObject();
            jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_ID, event.getId());
            jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_NAME, event.getName());
            jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_DESC, event.getDescription());
            jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_STARTDATE, event.getStartDate());
            jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_STARTTIME, event.getStartTime());
            jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_ENDDATE, event.getEndDate());
            jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_ENDTIME, event.getEndTime());
            jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_HOST, event.getEventhost());
            jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_LOCATION, event.getEventlocation());
            jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_ADDRESS, event.getAddress());
            jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_ISPAID, event.getIspaid());
            jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_EVENTTYPE, event.getEventType());
            jsonObjectEvent.addProperty(EventnotificationConstant.INTERESTED_COUNT, "");
            if (event.getFilestore() == null) {
                jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_FILESTOREID, "");
                jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_FILENAME, "");
            } else {
                jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_FILESTOREID, event.getFilestore().getFileStoreId());
                jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_FILENAME, event.getFilestore().getFileName());
            }

            if (event.getCost() == null)
                jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_COST, 0.0);
            else
                jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_COST, event.getCost());
            
            if (event.getUrl() == null)
                jsonObjectEvent.addProperty(EventnotificationConstant.URL, "");
            else
                jsonObjectEvent.addProperty(EventnotificationConstant.URL, event.getUrl());

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

        JsonObject jsonObjectEvent = new JsonObject();
        jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_ID, event.getId());
        jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_NAME, event.getName());
        jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_DESC, event.getDescription());
        jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_STARTDATE, event.getStartDate());
        jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_STARTTIME, event.getStartTime());
        jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_ENDDATE, event.getEndDate());
        jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_ENDTIME, event.getEndTime());
        jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_HOST, event.getEventhost());
        jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_LOCATION, event.getEventlocation());
        jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_ADDRESS, event.getAddress());
        jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_ISPAID, event.getIspaid());
        jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_EVENTTYPE, event.getEventType());
        jsonObjectEvent.addProperty(EventnotificationConstant.INTERESTED_COUNT, usereventService.countUsereventByEventId(event.getId()));
        if (event.getFilestore() == null) {
            jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_FILESTOREID, "");
            jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_FILENAME, "");
        } else {
            jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_FILESTOREID, event.getFilestore().getFileStoreId());
            jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_FILENAME, event.getFilestore().getFileName());
        }
        if (event.getCost() == null)
            jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_COST, 0.0);
        else
            jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_COST, event.getCost());
        
        if (event.getUrl() == null)
            jsonObjectEvent.addProperty(EventnotificationConstant.URL, "");
        else
            jsonObjectEvent.addProperty(EventnotificationConstant.URL, event.getUrl());

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
    public String searchEvent(@RequestParam(required = false, value = EventnotificationConstant.EVENT_EVENTTYPE) String eventType,
            @RequestParam(required = false, value = EventnotificationConstant.EVENT_NAME) String name,
            @RequestParam(required = false, value = EventnotificationConstant.EVENT_EVENTHOST) String eventHost,
            @RequestParam(required = false, value = EventnotificationConstant.STATUS) String status,
            @RequestParam(required = true, value = EventnotificationConstant.EVENT_DATE_TYPE) String eventDateType) {
        if (status == null)
            status = EventnotificationConstant.ACTIVE;
        List<Event> eventList = eventService.searchEvent(eventType, name, eventHost, status,eventDateType);
        JsonArray jsonArrayEvent = new JsonArray();
        for (Event event : eventList) {
            JsonObject jsonObjectEvent = new JsonObject();
            jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_ID, event.getId());
            jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_NAME, event.getName());
            jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_DESC, event.getDescription());
            jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_STARTDATE, event.getStartDate());
            jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_STARTTIME, event.getStartTime());
            jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_ENDDATE, event.getEndDate());
            jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_ENDTIME, event.getEndTime());
            jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_HOST, event.getEventhost());
            jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_LOCATION, event.getEventlocation());
            jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_ADDRESS, event.getAddress());
            jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_ISPAID, event.getIspaid());
            jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_EVENTTYPE, event.getEventType());
            jsonObjectEvent.addProperty(EventnotificationConstant.INTERESTED_COUNT, "");
            if (event.getFilestore() == null) {
                jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_FILESTOREID, "");
                jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_FILENAME, "");
            } else {
                jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_FILESTOREID, event.getFilestore().getFileStoreId());
                jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_FILENAME, event.getFilestore().getFileName());
            }
            if (event.getCost() == null)
                jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_COST, 0.0);
            else
                jsonObjectEvent.addProperty(EventnotificationConstant.EVENT_COST, event.getCost());
            
            if (event.getUrl() == null)
                jsonObjectEvent.addProperty(EventnotificationConstant.URL, "");
            else
                jsonObjectEvent.addProperty(EventnotificationConstant.URL, event.getUrl());

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
            + ApiUrl.INTERESTED, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String saveUserEvent(@RequestBody String jsonData) {
        JSONObject requestObject = (JSONObject) JSONValue.parse(jsonData);
        JSONObject responseObject = new JSONObject();
        if (!requestObject.containsKey(EventnotificationConstant.USERID)
                && !requestObject.containsKey(EventnotificationConstant.EVENTID))
            responseObject.put(EventnotificationConstant.FAIL, messageSource.getMessage("error.fail.event", null,
                    "Unable to process your request now.", Locale.getDefault()));
        else {
            Userevent userevent = usereventService.persistUserevent(
                    requestObject.get(EventnotificationConstant.USERID).toString(),
                    requestObject.get(EventnotificationConstant.EVENTID).toString());
            if (userevent == null)
                responseObject.put(EventnotificationConstant.FAIL, messageSource.getMessage("error.fail.event", null,
                        "Unable to process your request now.", Locale.getDefault()));
            else
                responseObject.put(EventnotificationConstant.SUCCESS,
                        messageSource.getMessage("msg.event.success", null, "Success.", Locale.getDefault()));
            responseObject.put(EventnotificationConstant.SUCCESS,usereventService.countUsereventByEventId(userevent.getEventid().getId()));
        }
        return responseObject.toJSONString();
    }

}
