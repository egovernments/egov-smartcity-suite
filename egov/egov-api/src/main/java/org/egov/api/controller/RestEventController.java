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

import static org.egov.api.controller.core.ApiUrl.CATEGORY_ID_PATH_PARAM;
import static org.egov.api.controller.core.ApiUrl.EVENT_ID_PATH_PARAM;
import static org.egov.api.controller.core.ApiUrl.EVENT_IMAGE_DOWNLOAD;
import static org.egov.api.controller.core.ApiUrl.GET_ALL_EVENT;
import static org.egov.api.controller.core.ApiUrl.GET_CATEGORY_FOR_MODULE;
import static org.egov.api.controller.core.ApiUrl.GET_EVENT;
import static org.egov.api.controller.core.ApiUrl.GET_PARAMETERS_FOR_CATEGORY;
import static org.egov.api.controller.core.ApiUrl.INTERESTED;
import static org.egov.api.controller.core.ApiUrl.MODULE_ID_PATH_PARAM;
import static org.egov.api.controller.core.ApiUrl.SEARCH_DRAFT_PATH_PARAM;
import static org.egov.api.controller.core.ApiUrl.SEARCH_EVENT;
import static org.egov.eventnotification.constants.Constants.CATEGORY;
import static org.egov.eventnotification.constants.Constants.CATEGORY_ID;
import static org.egov.eventnotification.constants.Constants.CATEGORY_NAME;
import static org.egov.eventnotification.constants.Constants.DRAFT_ID;
import static org.egov.eventnotification.constants.Constants.DRAFT_NAME;
import static org.egov.eventnotification.constants.Constants.DRAFT_NOTIFICATION_TYPE;
import static org.egov.eventnotification.constants.Constants.ERROR;
import static org.egov.eventnotification.constants.Constants.ERROR_FAIL_EVENT;
import static org.egov.eventnotification.constants.Constants.ERROR_FAIL_EVENTUSER;
import static org.egov.eventnotification.constants.Constants.EVENTID;
import static org.egov.eventnotification.constants.Constants.EVENT_ADDRESS;
import static org.egov.eventnotification.constants.Constants.EVENT_COST;
import static org.egov.eventnotification.constants.Constants.EVENT_DATE_TYPE;
import static org.egov.eventnotification.constants.Constants.EVENT_DESC;
import static org.egov.eventnotification.constants.Constants.EVENT_ENDDATE;
import static org.egov.eventnotification.constants.Constants.EVENT_ENDTIME;
import static org.egov.eventnotification.constants.Constants.EVENT_EVENTHOST;
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
import static org.egov.eventnotification.constants.Constants.FAIL;
import static org.egov.eventnotification.constants.Constants.INTERESTED_COUNT;
import static org.egov.eventnotification.constants.Constants.MODULE;
import static org.egov.eventnotification.constants.Constants.MODULE_NAME;
import static org.egov.eventnotification.constants.Constants.MSG_EVENT_SUCCESS;
import static org.egov.eventnotification.constants.Constants.NO;
import static org.egov.eventnotification.constants.Constants.NOTIFICATION_MESSAGE;
import static org.egov.eventnotification.constants.Constants.PARAMETER_ID;
import static org.egov.eventnotification.constants.Constants.PARAMETER_NAME;
import static org.egov.eventnotification.constants.Constants.SUCCESS;
import static org.egov.eventnotification.constants.Constants.SUCCESS1;
import static org.egov.eventnotification.constants.Constants.URL;
import static org.egov.eventnotification.constants.Constants.USERID;
import static org.egov.eventnotification.constants.Constants.USER_INTERESTED;
import static org.egov.eventnotification.constants.Constants.YES;
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
import org.egov.eventnotification.constants.Constants;
import org.egov.eventnotification.entity.CategoryParameters;
import org.egov.eventnotification.entity.Event;
import org.egov.eventnotification.entity.ModuleCategory;
import org.egov.eventnotification.entity.NotificationDrafts;
import org.egov.eventnotification.entity.UserEvent;
import org.egov.eventnotification.service.DraftService;
import org.egov.eventnotification.service.EventService;
import org.egov.eventnotification.service.UserEventService;
import org.egov.infra.filestore.service.FileStoreService;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    private UserEventService usereventService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;

    /**
     * This method is used for fetch all events
     * @return json string
     */
    @GetMapping(path = GET_ALL_EVENT, produces = APPLICATION_JSON_VALUE)
    public String getAllEvent() {
        List<Event> eventList = eventService.findAllOngoingEvent(Constants.ACTIVE);

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
            jsonObjectEvent.addProperty(INTERESTED_COUNT, "");
            if (event.getFilestore() == null) {
                jsonObjectEvent.addProperty(EVENT_FILESTOREID, "");
                jsonObjectEvent.addProperty(EVENT_FILENAME, "");
            } else {
                jsonObjectEvent.addProperty(EVENT_FILESTOREID, event.getFilestore().getId());
                jsonObjectEvent.addProperty(EVENT_FILENAME, event.getFilestore().getFileName());
            }

            if (event.getCost() == null)
                jsonObjectEvent.addProperty(EVENT_COST, 0.0);
            else
                jsonObjectEvent.addProperty(EVENT_COST, event.getCost());

            if (event.getUrl() == null)
                jsonObjectEvent.addProperty(URL, "");
            else
                jsonObjectEvent.addProperty(URL, event.getUrl());

            jsonObjectEvent.addProperty(USER_INTERESTED, NO);

            jsonArrayEvent.add(jsonObjectEvent);

        }

        return jsonArrayEvent.toString();
    }

    /**
     * This method is used for fetch event by id.
     * @param id
     * @return json string
     */
    @GetMapping(path = GET_EVENT + EVENT_ID_PATH_PARAM, produces = APPLICATION_JSON_VALUE)
    public String getEvent(@PathVariable long id, @RequestParam(required = false, value = USERID) Long userId) {
        ApiResponse.newInstance();
        Event event = eventService.findByEventId(id);

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
        jsonObjectEvent.addProperty(INTERESTED_COUNT,
                usereventService.countUsereventByEventId(event.getId()));
        if (event.getFilestore() == null) {
            jsonObjectEvent.addProperty(EVENT_FILESTOREID, "");
            jsonObjectEvent.addProperty(EVENT_FILENAME, "");
        } else {
            jsonObjectEvent.addProperty(EVENT_FILESTOREID, event.getFilestore().getId());
            jsonObjectEvent.addProperty(EVENT_FILENAME, event.getFilestore().getFileName());
        }
        if (event.getCost() == null)
            jsonObjectEvent.addProperty(EVENT_COST, 0.0);
        else
            jsonObjectEvent.addProperty(EVENT_COST, event.getCost());

        if (event.getUrl() == null)
            jsonObjectEvent.addProperty(URL, "");
        else
            jsonObjectEvent.addProperty(URL, event.getUrl());

        UserEvent userevent = usereventService.getUsereventByEventAndUser(event.getId(), userId);
        if (userevent == null)
            jsonObjectEvent.addProperty(USER_INTERESTED, NO);
        else
            jsonObjectEvent.addProperty(USER_INTERESTED, YES);

        return jsonObjectEvent.toString();
    }

    /**
     * This method is used for search event.
     * @param eventType
     * @param eventName
     * @param eventHost
     * @return json string
     */
    @GetMapping(path = SEARCH_EVENT, produces = APPLICATION_JSON_VALUE)
    public String searchEvent(@RequestParam(required = false, value = EVENT_EVENTTYPE) String eventType,
            @RequestParam(required = false, value = EVENT_NAME) String name,
            @RequestParam(required = false, value = EVENT_EVENTHOST) String eventHost,
            @RequestParam(required = true, value = EVENT_DATE_TYPE) String eventDateType) {

        Event eventObj = new Event();
        eventObj.setEventType(eventType);
        eventObj.setName(name);
        eventObj.setEventhost(eventHost);
        List<Event> eventList = eventService.searchEvent(eventObj, eventDateType);
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
            jsonObjectEvent.addProperty(INTERESTED_COUNT, "");
            if (event.getFilestore() == null) {
                jsonObjectEvent.addProperty(EVENT_FILESTOREID, "");
                jsonObjectEvent.addProperty(EVENT_FILENAME, "");
            } else {
                jsonObjectEvent.addProperty(EVENT_FILESTOREID, event.getFilestore().getId());
                jsonObjectEvent.addProperty(EVENT_FILENAME, event.getFilestore().getFileName());
            }
            if (event.getCost() == null)
                jsonObjectEvent.addProperty(EVENT_COST, 0.0);
            else
                jsonObjectEvent.addProperty(EVENT_COST, event.getCost());

            if (event.getUrl() == null)
                jsonObjectEvent.addProperty(URL, "");
            else
                jsonObjectEvent.addProperty(URL, event.getUrl());

            jsonObjectEvent.addProperty(USER_INTERESTED, NO);

            jsonArrayEvent.add(jsonObjectEvent);

        }
        return jsonArrayEvent.toString();
    }

    @GetMapping(path = SEARCH_DRAFT_PATH_PARAM, produces = APPLICATION_JSON_VALUE)
    public String searchDraft(@RequestParam(required = false, value = DRAFT_NOTIFICATION_TYPE) String type,
            @RequestParam(required = false, value = DRAFT_NAME) String name,
            @RequestParam(required = false, value = MODULE_NAME) Long module) {

        NotificationDrafts draftObj = new NotificationDrafts();
        draftObj.setType(type);
        draftObj.setName(name);
        List<NotificationDrafts> draftList = draftService.searchDraft(draftObj);

        JsonArray jsonArrayEvent = new JsonArray();
        for (NotificationDrafts draft : draftList) {
            JsonObject jsonObjectDraft = new JsonObject();
            jsonObjectDraft.addProperty(DRAFT_ID, draft.getId());
            jsonObjectDraft.addProperty(DRAFT_NAME, draft.getName());
            jsonObjectDraft.addProperty(DRAFT_NOTIFICATION_TYPE, draft.getType());

            if (null != draft.getModule()) {
                JsonObject jsonObjectModule = new JsonObject();
                if (StringUtils.isNotBlank(draft.getModule().getName()))
                    jsonObjectModule.addProperty(EVENT_NAME, draft.getModule().getName());
                if (null != draft.getModule().getId())
                    jsonObjectModule.addProperty(EVENT_ID, draft.getModule().getId());
                jsonObjectDraft.add(MODULE, jsonObjectModule);
            }

            if (null != draft.getCategory()) {
                JsonObject jsonObjectCategory = new JsonObject();
                if (StringUtils.isNotBlank(draft.getCategory().getName()))
                    jsonObjectCategory.addProperty(EVENT_NAME, draft.getCategory().getName());
                if (null != draft.getCategory().getId())
                    jsonObjectCategory.addProperty(EVENT_ID, draft.getCategory().getId());
                jsonObjectDraft.add(CATEGORY, jsonObjectCategory);
            }
            jsonObjectDraft.addProperty(NOTIFICATION_MESSAGE, draft.getMessage());
            jsonArrayEvent.add(jsonObjectDraft);
        }
        return jsonArrayEvent.toString();
    }

    @GetMapping(path = GET_CATEGORY_FOR_MODULE + MODULE_ID_PATH_PARAM, produces = APPLICATION_JSON_VALUE)
    public String getCategoriesForModule(@PathVariable long moduleId) {
        List<ModuleCategory> categoryList = draftService.getCategoriesForModule(moduleId);
        JsonArray jsonArrayEvent = new JsonArray();
        for (ModuleCategory category : categoryList) {
            JsonObject jsonObjectEvent = new JsonObject();
            jsonObjectEvent.addProperty(CATEGORY_ID, category.getId());
            jsonObjectEvent.addProperty(CATEGORY_NAME, category.getName());
            jsonArrayEvent.add(jsonObjectEvent);
        }
        return jsonArrayEvent.toString();
    }

    @GetMapping(path = GET_PARAMETERS_FOR_CATEGORY + CATEGORY_ID_PATH_PARAM, produces = APPLICATION_JSON_VALUE)
    public String getParametersForCategory(@PathVariable long categoryId) {
        List<CategoryParameters> parametersList = draftService.getParametersForCategory(categoryId);
        JsonArray jsonArrayEvent = new JsonArray();
        for (CategoryParameters parameter : parametersList) {
            JsonObject jsonObjectEvent = new JsonObject();
            jsonObjectEvent.addProperty(PARAMETER_ID, parameter.getId());
            jsonObjectEvent.addProperty(PARAMETER_NAME, parameter.getName());
            jsonArrayEvent.add(jsonObjectEvent);
        }
        return jsonArrayEvent.toString();
    }

    /**
     * This method is used for fetch event by id.
     * @param id
     * @return json string
     */
    @PostMapping(path = GET_EVENT + INTERESTED, produces = APPLICATION_JSON_VALUE)
    public String saveUserEvent(@RequestBody String jsonData) {
        JSONObject requestObject = (JSONObject) JSONValue.parse(jsonData);
        JSONObject responseObject = new JSONObject();
        if (requestObject.containsKey(USERID)
                && requestObject.containsKey(EVENTID)) {
            UserEvent userevent = usereventService.save(
                    Long.parseLong(requestObject.get(USERID).toString()),
                    Long.parseLong(requestObject.get(EVENTID).toString()));
            if (userevent == null)
                responseObject.put(FAIL, messageSource.getMessage(ERROR_FAIL_EVENT, null,
                        ERROR, Locale.getDefault()));
            else {
                responseObject.put(SUCCESS,
                        messageSource.getMessage(MSG_EVENT_SUCCESS, null, SUCCESS1, Locale.getDefault()));
                Long interestedCount = usereventService
                        .countUsereventByEventId(Long.parseLong(requestObject.get(EVENTID).toString()));
                if (interestedCount == null)
                    responseObject.put(INTERESTED_COUNT, 0);
                else
                    responseObject.put(INTERESTED_COUNT,
                            usereventService.countUsereventByEventId(userevent.getEventId()));
            }

        } else
            responseObject.put(FAIL, messageSource.getMessage(ERROR_FAIL_EVENTUSER, null,
                    ERROR, Locale.getDefault()));

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

    @GetMapping(path = EVENT_IMAGE_DOWNLOAD, produces = APPLICATION_JSON_VALUE)
    public void downloadEventImage(@PathVariable final Long id, @PathVariable final String fileStoreId,
            final HttpServletResponse response) throws IOException {
        try {
            Event event = eventService.findByEventId(id);
            File downloadFile = fileStoreService.fetch(fileStoreId, MODULE_NAME);
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
