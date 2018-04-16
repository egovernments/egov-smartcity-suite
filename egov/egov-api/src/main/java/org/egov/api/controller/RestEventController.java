package org.egov.api.controller;

import java.text.SimpleDateFormat;
import java.util.List;

import org.egov.api.controller.core.ApiResponse;
import org.egov.api.controller.core.ApiUrl;
import org.egov.eventnotification.entity.Event;
import org.egov.eventnotification.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@org.springframework.web.bind.annotation.RestController
public class RestEventController {
	
	@Autowired
	private EventService eventService;
	
	@RequestMapping(value = ApiUrl.GET_ALL_EVENT, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String getAllEvent() {
		ApiResponse res = ApiResponse.newInstance();
		List<Event> eventList = eventService.findAll();
		JsonArray jsonArrayEvent = new JsonArray();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		for(Event event : eventList) {
			JsonObject jsonObjectEvent = new JsonObject();
			jsonObjectEvent.addProperty("id", event.getId());
			jsonObjectEvent.addProperty("name", event.getName());
			jsonObjectEvent.addProperty("description", event.getDescription());
			jsonObjectEvent.addProperty("startDate", sdf.format(event.getStartDate()));
			jsonObjectEvent.addProperty("startTime", event.getStartTime());
			jsonObjectEvent.addProperty("endDate", sdf.format(event.getStartDate()));
			jsonObjectEvent.addProperty("endTime", event.getEndTime());
			jsonObjectEvent.addProperty("eventhost", event.getEventhost());
			jsonObjectEvent.addProperty("eventlocation", event.getEventlocation());
			jsonObjectEvent.addProperty("address", event.getAddress());
			jsonObjectEvent.addProperty("ispaid", event.getIspaid());
			jsonObjectEvent.addProperty("eventType", event.getEventType());
			if(event.getCost() ==null) {
				jsonObjectEvent.addProperty("cost", 0.0);
			}else {
				jsonObjectEvent.addProperty("cost", event.getCost());
			}
			
			jsonArrayEvent.add(jsonObjectEvent);
			
		}
		return jsonArrayEvent.toString();
	}
	
	@RequestMapping(value = ApiUrl.GET_EVENT+"/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String getEvent(@PathVariable long id) {
		ApiResponse res = ApiResponse.newInstance();
		Event event = eventService.findById(id);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		JsonObject jsonObjectEvent = new JsonObject();
		jsonObjectEvent.addProperty("id", event.getId());
		jsonObjectEvent.addProperty("name", event.getName());
		jsonObjectEvent.addProperty("description", event.getDescription());
		jsonObjectEvent.addProperty("startDate", sdf.format(event.getStartDate()));
		jsonObjectEvent.addProperty("startTime", event.getStartTime());
		jsonObjectEvent.addProperty("endDate", sdf.format(event.getStartDate()));
		jsonObjectEvent.addProperty("endTime", event.getEndTime());
		jsonObjectEvent.addProperty("eventhost", event.getEventhost());
		jsonObjectEvent.addProperty("eventlocation", event.getEventlocation());
		jsonObjectEvent.addProperty("address", event.getAddress());
		jsonObjectEvent.addProperty("ispaid", event.getIspaid());
		jsonObjectEvent.addProperty("eventType", event.getEventType());
		if(event.getCost() ==null) {
			jsonObjectEvent.addProperty("cost", 0.0);
		}else {
			jsonObjectEvent.addProperty("cost", event.getCost());
		}
		
		
		return jsonObjectEvent.toString();
	}
	
	@RequestMapping(value = ApiUrl.SEARCH_EVENT, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String searchEvent(@RequestParam(required = false, value = "eventType") String eventType,
			@RequestParam(required = false, value = "eventName") String eventName,
			@RequestParam(required = false, value = "eventHost") String eventHost) {
		ApiResponse res = ApiResponse.newInstance();
		List<Event> eventList = eventService.searchEvent(eventType, eventName, eventHost);
		JsonArray jsonArrayEvent = new JsonArray();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		for(Event event : eventList) {
			JsonObject jsonObjectEvent = new JsonObject();
			jsonObjectEvent.addProperty("id", event.getId());
			jsonObjectEvent.addProperty("name", event.getName());
			jsonObjectEvent.addProperty("description", event.getDescription());
			jsonObjectEvent.addProperty("startDate", sdf.format(event.getStartDate()));
			jsonObjectEvent.addProperty("startTime", event.getStartTime());
			jsonObjectEvent.addProperty("endDate", sdf.format(event.getStartDate()));
			jsonObjectEvent.addProperty("endTime", event.getEndTime());
			jsonObjectEvent.addProperty("eventhost", event.getEventhost());
			jsonObjectEvent.addProperty("eventlocation", event.getEventlocation());
			jsonObjectEvent.addProperty("address", event.getAddress());
			jsonObjectEvent.addProperty("ispaid", event.getIspaid());
			jsonObjectEvent.addProperty("eventType", event.getEventType());
			if(event.getCost() ==null) {
				jsonObjectEvent.addProperty("cost", 0.0);
			}else {
				jsonObjectEvent.addProperty("cost", event.getCost());
			}
			
			jsonArrayEvent.add(jsonObjectEvent);
			
		}
		return jsonArrayEvent.toString();
	}

}
