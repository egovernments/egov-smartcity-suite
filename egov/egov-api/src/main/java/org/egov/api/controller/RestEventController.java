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
package org.egov.api.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.egov.api.controller.core.ApiResponse;
import org.egov.api.controller.core.ApiUrl;
import org.egov.eventnotification.constants.EventnotificationConstant;
import org.egov.eventnotification.entity.CategoryParameters;
import org.egov.eventnotification.entity.Event;
import org.egov.eventnotification.entity.ModuleCategory;
import org.egov.eventnotification.entity.NotificationDrafts;
import org.egov.eventnotification.entity.Userevent;
import org.egov.eventnotification.service.DraftService;
import org.egov.eventnotification.service.EventService;
import org.egov.eventnotification.service.UsereventService;
import org.egov.infra.filestore.service.FileStoreService;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    private static final Logger LOGGER = Logger.getLogger(RestEventController.class);
    @Autowired
    private EventService eventService;
    
    @Autowired
    private DraftService draftService; 

    @Autowired
    private UsereventService usereventService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;

    /**
     * This method is used for fetch all events
     * @return json string
     */
    @RequestMapping(value = ApiUrl.GET_ALL_EVENT, method = GET, produces = APPLICATION_JSON_VALUE)
    public String getAllEvent() {
        List<Event> eventList = eventService.findAllOngoingEvent(EventnotificationConstant.ACTIVE);

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
            
            jsonObjectEvent.addProperty(EventnotificationConstant.USER_INTERESTED,EventnotificationConstant.NO);

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
            + ApiUrl.EVENT_ID, method = GET, produces = APPLICATION_JSON_VALUE)
    public String getEvent(@PathVariable long id,@RequestParam(required = false, value = EventnotificationConstant.USERID) Long userId) {
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
        jsonObjectEvent.addProperty(EventnotificationConstant.INTERESTED_COUNT,
                usereventService.countUsereventByEventId(event.getId()));
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
        
        Userevent userevent = usereventService.getUsereventByEventAndUser(event.getId(),userId);
        if(null == userevent)
            jsonObjectEvent.addProperty(EventnotificationConstant.USER_INTERESTED,EventnotificationConstant.NO);
        else
            jsonObjectEvent.addProperty(EventnotificationConstant.USER_INTERESTED,EventnotificationConstant.YES);
        
        return jsonObjectEvent.toString();
    }

    /**
     * This method is used for search event.
     * @param eventType
     * @param eventName
     * @param eventHost
     * @return json string
     */
    @RequestMapping(value = ApiUrl.SEARCH_EVENT, method = GET, produces = APPLICATION_JSON_VALUE)
    public String searchEvent(@RequestParam(required = false, value = EventnotificationConstant.EVENT_EVENTTYPE) String eventType,
            @RequestParam(required = false, value = EventnotificationConstant.EVENT_NAME) String name,
            @RequestParam(required = false, value = EventnotificationConstant.EVENT_EVENTHOST) String eventHost,
            @RequestParam(required = true, value = EventnotificationConstant.EVENT_DATE_TYPE) String eventDateType) {

        Event eventObj = new Event();
        eventObj.setEventType(eventType);
        eventObj.setName(name);
        eventObj.setEventhost(eventHost);
        List<Event> eventList = eventService.searchEvent(eventObj, eventDateType);
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
            
            jsonObjectEvent.addProperty(EventnotificationConstant.USER_INTERESTED,EventnotificationConstant.NO);

            jsonArrayEvent.add(jsonObjectEvent);

        }
        return jsonArrayEvent.toString();
    }
    
    @RequestMapping(value = ApiUrl.SEARCH_DRAFT, method = GET, produces = APPLICATION_JSON_VALUE)
    public String searchDraft(@RequestParam(required = false, value = EventnotificationConstant.DRAFT_NOTIFICATION_TYPE) String type,
            @RequestParam(required = false, value = EventnotificationConstant.DRAFT_NAME) String name,
            @RequestParam(required = false, value = EventnotificationConstant.MODULE_NAME) Long module) {

    	NotificationDrafts draftObj = new NotificationDrafts(); 
    	draftObj.setType(type);
    	draftObj.setName(name);
    	List<NotificationDrafts> draftList = draftService.searchDraft(draftObj);

        JsonArray jsonArrayEvent = new JsonArray();
        for (NotificationDrafts draft : draftList) {
            JsonObject jsonObjectDraft = new JsonObject();
            jsonObjectDraft.addProperty(EventnotificationConstant.DRAFT_ID, draft.getId());
            jsonObjectDraft.addProperty(EventnotificationConstant.DRAFT_NAME, draft.getName());
            jsonObjectDraft.addProperty(EventnotificationConstant.DRAFT_NOTIFICATION_TYPE, draft.getType());
            
            if(null != draft.getModule()) {
            	JsonObject jsonObjectModule = new JsonObject();
            	if(StringUtils.isNotBlank(draft.getModule().getName()))
            		jsonObjectModule.addProperty("name", draft.getModule().getName());
            	if(null != draft.getModule().getId()) 
            		jsonObjectModule.addProperty("id", draft.getModule().getId());
            	jsonObjectDraft.add("module", jsonObjectModule);
            }
            
            if(null != draft.getCategory()) {
            	JsonObject jsonObjectCategory = new JsonObject();
            	if(StringUtils.isNotBlank(draft.getCategory().getName()))
            		jsonObjectCategory.addProperty("name", draft.getCategory().getName());
            	if(null != draft.getCategory().getId()) 
            		jsonObjectCategory.addProperty("id", draft.getCategory().getId());
            	jsonObjectDraft.add("category", jsonObjectCategory);
            }
            jsonObjectDraft.addProperty("message", draft.getMessage());
            jsonArrayEvent.add(jsonObjectDraft);
        }
        return jsonArrayEvent.toString();
    }
    
    @RequestMapping(value = ApiUrl.GET_CATEGORY_FOR_MODULE
            + ApiUrl.MODULE_ID, method = GET, produces = APPLICATION_JSON_VALUE)
    public String getCategoriesForModule(@PathVariable long moduleId) {
        List<ModuleCategory> categoryList = draftService.getCategoriesForModule(moduleId); 
        JsonArray jsonArrayEvent = new JsonArray();
        for (ModuleCategory category : categoryList) {
            JsonObject jsonObjectEvent = new JsonObject();
            jsonObjectEvent.addProperty(EventnotificationConstant.CATEGORY_ID, category.getId());
            jsonObjectEvent.addProperty(EventnotificationConstant.CATEGORY_NAME, category.getName());
            jsonArrayEvent.add(jsonObjectEvent);
        }
        return jsonArrayEvent.toString();
    }
    
    @RequestMapping(value = ApiUrl.GET_PARAMETERS_FOR_CATEGORY
            + ApiUrl.CATEGORY_ID, method = GET, produces = APPLICATION_JSON_VALUE)
    public String getParametersForCategory(@PathVariable long categoryId) {
        List<CategoryParameters> parametersList = draftService.getParametersForCategory(categoryId); 
        JsonArray jsonArrayEvent = new JsonArray();
        for (CategoryParameters parameter : parametersList) {
            JsonObject jsonObjectEvent = new JsonObject();
            jsonObjectEvent.addProperty(EventnotificationConstant.PARAMETER_ID, parameter.getId());
            jsonObjectEvent.addProperty(EventnotificationConstant.PARAMETER_NAME, parameter.getName());
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
            + ApiUrl.INTERESTED, method = POST, produces = APPLICATION_JSON_VALUE)
    public String saveUserEvent(@RequestBody String jsonData) {
        JSONObject requestObject = (JSONObject) JSONValue.parse(jsonData);
        JSONObject responseObject = new JSONObject();
        if (requestObject.containsKey(EventnotificationConstant.USERID)
                && requestObject.containsKey(EventnotificationConstant.EVENTID)) {
            Userevent userevent = usereventService.persistUserevent(
                    requestObject.get(EventnotificationConstant.USERID).toString(),
                    requestObject.get(EventnotificationConstant.EVENTID).toString());
            if (userevent == null)
                responseObject.put(EventnotificationConstant.FAIL, messageSource.getMessage("error.fail.event", null,
                        "Unable to process your request now.", Locale.getDefault()));
            else {
                responseObject.put(EventnotificationConstant.SUCCESS,
                        messageSource.getMessage("msg.event.success", null, "Success.", Locale.getDefault()));
                Long interestedCount = usereventService
                        .countUsereventByEventId(Long.parseLong(requestObject.get(EventnotificationConstant.EVENTID).toString()));
                if (null == interestedCount)
                    responseObject.put(EventnotificationConstant.INTERESTED_COUNT, 0);
                else
                    responseObject.put(EventnotificationConstant.INTERESTED_COUNT,
                            usereventService.countUsereventByEventId(userevent.getEventid().getId()));
            }

        } else
            responseObject.put(EventnotificationConstant.FAIL, messageSource.getMessage("error.fail.event", null,
                    "Unable to process your request now.", Locale.getDefault()));

        return responseObject.toJSONString();
    }

    /**
     * This will download the support document of the complaint.
     *
     * @param complaintNo
     * @param fileNo
     * @param isThumbnail
     * @return file
     */

    @RequestMapping(value = ApiUrl.EVENT_IMAGE_DOWNLOAD, method = GET)
    public void downloadEventImage(@PathVariable final Long id, @PathVariable final String fileStoreId,
            final HttpServletResponse response) throws IOException {
        try {
            Event event = eventService.findById(id);
            File downloadFile = fileStoreService.fetch(fileStoreId, EventnotificationConstant.MODULE_NAME);
            long contentLength = downloadFile.length();

            response.setHeader("Content-Length", String.valueOf(contentLength));
            if (event.getFilestore() == null)
                response.setHeader("Content-Disposition", "attachment;filename=index.jpeg");
            else
                response.setHeader("Content-Disposition", "attachment;filename=" + event.getFilestore().getFileName());
            response.setContentType(Files.probeContentType(downloadFile.toPath()));
            final OutputStream out = response.getOutputStream();
            IOUtils.write(FileUtils.readFileToByteArray(downloadFile), out);
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}
