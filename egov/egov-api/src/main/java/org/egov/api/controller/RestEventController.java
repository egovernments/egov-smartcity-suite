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
import static org.egov.eventnotification.constants.Constants.ACTIVE;
import static org.egov.eventnotification.constants.Constants.DDMMYYYY;
import static org.egov.eventnotification.constants.Constants.DOUBLE_DEFAULT;
import static org.egov.eventnotification.constants.Constants.EMPTY;
import static org.egov.eventnotification.constants.Constants.ERROR;
import static org.egov.eventnotification.constants.Constants.EVENT_ADDRESS;
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
import static org.egov.eventnotification.constants.Constants.FAIL;
import static org.egov.eventnotification.constants.Constants.INTERESTED_COUNT;
import static org.egov.eventnotification.constants.Constants.MAX_TEN;
import static org.egov.eventnotification.constants.Constants.MODULE_NAME;
import static org.egov.eventnotification.constants.Constants.NO;
import static org.egov.eventnotification.constants.Constants.SUCCESS;
import static org.egov.eventnotification.constants.Constants.SUCCESS1;
import static org.egov.eventnotification.constants.Constants.URL;
import static org.egov.eventnotification.constants.Constants.USER_INTERESTED;
import static org.egov.eventnotification.constants.Constants.YES;
import static org.egov.eventnotification.constants.Constants.ZERO;
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
import org.apache.log4j.Logger;
import org.egov.api.adapter.CategoryParametersAdapter;
import org.egov.api.adapter.ModuleCategoryAdapter;
import org.egov.api.adapter.NotificationDraftsAdapter;
import org.egov.api.controller.core.ApiController;
import org.egov.eventnotification.entity.Event;
import org.egov.eventnotification.entity.NotificationDrafts;
import org.egov.eventnotification.entity.UserEvent;
import org.egov.eventnotification.service.DraftService;
import org.egov.eventnotification.service.EventService;
import org.egov.eventnotification.service.UserEventService;
import org.egov.infra.utils.DateUtils;
import org.joda.time.DateTime;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
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
public class RestEventController extends ApiController {
    private static final Logger LOGGER = Logger.getLogger(RestEventController.class);
    @Autowired
    private EventService eventService;

    @Autowired
    private DraftService draftService;

    @Autowired
    private UserEventService usereventService;

    @Autowired
    private MessageSource messageSource;

    /**
     * This method is used for fetch all events
     * @return json string
     */
    @GetMapping(path = GET_ALL_EVENT, produces = APPLICATION_JSON_VALUE)
    public String getAllEvent() {
        List<Event> eventList = eventService.findAllOngoingEvent(ACTIVE.toUpperCase());

        JsonArray jsonArrayEvent = new JsonArray();
        for (Event event : eventList) {
            JsonObject jsonObjectEvent = new JsonObject();
            jsonObjectEvent.addProperty(EVENT_ID, event.getId());
            jsonObjectEvent.addProperty(EVENT_NAME, event.getName());
            jsonObjectEvent.addProperty(EVENT_DESC, event.getDescription());

            jsonObjectEvent.addProperty(EVENT_STARTDATE,
                    DateUtils.getDate(DateUtils.getDefaultFormattedDate(event.getStartDate()), DDMMYYYY).getTime());
            jsonObjectEvent.addProperty(EVENT_STARTTIME,
                    event.getEventDetails().getStartHH() + ":" + event.getEventDetails().getStartMM());

            jsonObjectEvent.addProperty(EVENT_ENDDATE,
                    DateUtils.getDate(DateUtils.getDefaultFormattedDate(event.getEndDate()), DDMMYYYY).getTime());
            jsonObjectEvent.addProperty(EVENT_ENDTIME,
                    event.getEventDetails().getEndHH() + ":" + event.getEventDetails().getEndMM());

            jsonObjectEvent.addProperty(EVENT_HOST, event.getEventhost());
            jsonObjectEvent.addProperty(EVENT_LOCATION, event.getEventlocation());
            jsonObjectEvent.addProperty(EVENT_ADDRESS, event.getAddress());
            jsonObjectEvent.addProperty(EVENT_ISPAID, event.isIspaid());
            jsonObjectEvent.addProperty(EVENT_EVENTTYPE, event.getEventType());
            if (event.getFilestore() != null) {
                jsonObjectEvent.addProperty(EVENT_FILESTOREID, event.getFilestore().getFileStoreId());
                jsonObjectEvent.addProperty(EVENT_FILENAME, event.getFilestore().getFileName());
            } else {
                jsonObjectEvent.addProperty(EVENT_FILESTOREID, EMPTY);
                jsonObjectEvent.addProperty(EVENT_FILENAME, EMPTY);
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
    public String getEvent(@PathVariable long id, @RequestParam(required = false) Long userId) {
        Event event = eventService.findByEventId(id);
        JsonObject jsonObjectEvent = new JsonObject();
        jsonObjectEvent.addProperty(EVENT_ID, event.getId());
        jsonObjectEvent.addProperty(EVENT_NAME, event.getName());
        jsonObjectEvent.addProperty(EVENT_DESC, event.getDescription());

        jsonObjectEvent.addProperty(EVENT_STARTDATE,
                DateUtils.getDate(DateUtils.getDefaultFormattedDate(event.getStartDate()), DDMMYYYY).getTime());
        jsonObjectEvent.addProperty(EVENT_STARTTIME,
                event.getEventDetails().getStartHH() + ":" + event.getEventDetails().getStartMM());

        jsonObjectEvent.addProperty(EVENT_ENDDATE,
                DateUtils.getDate(DateUtils.getDefaultFormattedDate(event.getEndDate()), DDMMYYYY).getTime());
        jsonObjectEvent.addProperty(EVENT_ENDTIME, event.getEventDetails().getEndHH() + ":" + event.getEventDetails().getEndMM());

        jsonObjectEvent.addProperty(EVENT_HOST, event.getEventhost());
        jsonObjectEvent.addProperty(EVENT_LOCATION, event.getEventlocation());
        jsonObjectEvent.addProperty(EVENT_ADDRESS, event.getAddress());
        jsonObjectEvent.addProperty(EVENT_ISPAID, event.isIspaid());
        jsonObjectEvent.addProperty(EVENT_EVENTTYPE, event.getEventType());
        if (event.getFilestore() != null) {
            jsonObjectEvent.addProperty(EVENT_FILESTOREID, event.getFilestore().getFileStoreId());
            jsonObjectEvent.addProperty(EVENT_FILENAME, event.getFilestore().getFileName());
        } else {
            jsonObjectEvent.addProperty(EVENT_FILESTOREID, EMPTY);
            jsonObjectEvent.addProperty(EVENT_FILENAME, EMPTY);
        }
        if (event.getCost() == null)
            jsonObjectEvent.addProperty(EVENT_COST, DOUBLE_DEFAULT);
        else
            jsonObjectEvent.addProperty(EVENT_COST, event.getCost());

        if (event.getUrl() == null)
            jsonObjectEvent.addProperty(URL, EMPTY);
        else
            jsonObjectEvent.addProperty(URL, event.getUrl());
        if (userId != null) {
            UserEvent userEvent = usereventService.getUsereventByEventAndUser(id, userId);
            if (userEvent == null)
                jsonObjectEvent.addProperty(USER_INTERESTED, NO);
            else
                jsonObjectEvent.addProperty(USER_INTERESTED, YES);
        } else
            jsonObjectEvent.addProperty(USER_INTERESTED, NO);

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
    public String searchEvent(@RequestParam(required = false) String eventType,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String eventHost,
            @RequestParam String eventDateType) {

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
            jsonObjectEvent.addProperty(EVENT_ISPAID, event.isIspaid());
            jsonObjectEvent.addProperty(EVENT_EVENTTYPE, event.getEventType());
            if (event.getFilestore() != null) {
                jsonObjectEvent.addProperty(EVENT_FILESTOREID, event.getFilestore().getFileStoreId());
                jsonObjectEvent.addProperty(EVENT_FILENAME, event.getFilestore().getFileName());
            } else {
                jsonObjectEvent.addProperty(EVENT_FILESTOREID, EMPTY);
                jsonObjectEvent.addProperty(EVENT_FILENAME, EMPTY);
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

            jsonArrayEvent.add(jsonObjectEvent);

        }

        return jsonArrayEvent.toString();
    }

    @GetMapping(path = SEARCH_DRAFT_PATH_PARAM, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> searchDraft(@RequestParam(required = false) String type,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long module) {

        NotificationDrafts draftObj = new NotificationDrafts();
        draftObj.setType(type);
        draftObj.setName(name);

        try {
            return getResponseHandler()
                    .setDataAdapter(new NotificationDraftsAdapter())
                    .success(draftService.searchDraft(draftObj));
        } catch (final Exception e) {
            LOGGER.error(EGOV_API_ERROR, e);
            return getResponseHandler().error(getMessage(SERVER_ERROR_KEY));
        }
    }

    @GetMapping(path = GET_CATEGORY_FOR_MODULE + MODULE_ID_PATH_PARAM, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getCategoriesForModule(@PathVariable long moduleId) {
        try {
            return getResponseHandler()
                    .setDataAdapter(new ModuleCategoryAdapter())
                    .success(draftService.getCategoriesForModule(moduleId));
        } catch (final Exception e) {
            LOGGER.error(EGOV_API_ERROR, e);
            return getResponseHandler().error(getMessage(SERVER_ERROR_KEY));
        }
    }

    @GetMapping(path = GET_PARAMETERS_FOR_CATEGORY + CATEGORY_ID_PATH_PARAM, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getParametersForCategory(@PathVariable long categoryId) {
        try {
            return getResponseHandler()
                    .setDataAdapter(new CategoryParametersAdapter())
                    .success(draftService.getParametersForCategory(categoryId));
        } catch (final Exception e) {
            LOGGER.error(EGOV_API_ERROR, e);
            return getResponseHandler().error(getMessage(SERVER_ERROR_KEY));
        }
    }

    /**
     * This method is used for fetch event by id.
     * @param id
     * @return json string
     */
    @PostMapping(path = GET_EVENT + INTERESTED, produces = APPLICATION_JSON_VALUE)
    public String saveUserEvent(@RequestBody UserEvent userEvent) {
        JSONObject responseObject = new JSONObject();
        if (userEvent != null) {
            userEvent = usereventService.saveUserEvent(userEvent.getUserId(), userEvent.getEventId());
            if (userEvent == null)
                responseObject.put(FAIL, messageSource.getMessage("error.fail.event", null,
                        ERROR, Locale.getDefault()));
            else {
                responseObject.put(SUCCESS,
                        messageSource.getMessage("msg.event.success", null, SUCCESS1, Locale.getDefault()));
                Long interestedCount = usereventService.countUsereventByEventId(userEvent.getEventId());
                if (interestedCount == null)
                    responseObject.put(INTERESTED_COUNT, 0);
                else
                    responseObject.put(INTERESTED_COUNT,
                            usereventService.countUsereventByEventId(userEvent.getEventId()));
            }

        } else
            responseObject.put(FAIL, messageSource.getMessage("error.fail.eventuser", null,
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
