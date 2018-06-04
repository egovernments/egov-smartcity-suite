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
import static org.egov.eventnotification.constants.Constants.EVENTID;
import static org.egov.eventnotification.constants.Constants.FAIL;
import static org.egov.eventnotification.constants.Constants.INTERESTED_COUNT;
import static org.egov.eventnotification.constants.Constants.MODULE_NAME;
import static org.egov.eventnotification.constants.Constants.SUCCESS;
import static org.egov.eventnotification.constants.Constants.USERID;
import static org.egov.eventnotification.constants.Constants.ZERO;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.egov.api.adapter.CategoryParametersAdapter;
import org.egov.api.adapter.DraftsAdapter;
import org.egov.api.adapter.EventAdapter;
import org.egov.api.adapter.EventSearchAdapter;
import org.egov.api.adapter.ModuleCategoryAdapter;
import org.egov.api.controller.core.ApiController;
import org.egov.eventnotification.entity.DraftType;
import org.egov.eventnotification.entity.Drafts;
import org.egov.eventnotification.entity.Event;
import org.egov.eventnotification.entity.EventType;
import org.egov.eventnotification.entity.UserEvent;
import org.egov.eventnotification.service.CategoryParametersService;
import org.egov.eventnotification.service.DraftService;
import org.egov.eventnotification.service.EventService;
import org.egov.eventnotification.service.ModuleCategoryService;
import org.egov.eventnotification.service.UserEventService;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
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
    private ModuleCategoryService moduleCategoryService;

    @Autowired
    private CategoryParametersService categoryParametersService;

    @GetMapping(path = GET_ALL_EVENT, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAllEvent() {
        try {
            return getResponseHandler()
                    .setDataAdapter(new EventAdapter(usereventService))
                    .success(eventService.getAllOngoingEvent(ACTIVE.toUpperCase()));
        } catch (final Exception e) {
            LOGGER.error(EGOV_API_ERROR, e);
            return getResponseHandler().error(getMessage(SERVER_ERROR_KEY));
        }
    }

    @GetMapping(path = GET_EVENT + EVENT_ID_PATH_PARAM, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getEvent(@PathVariable long id, @RequestParam(required = false) Long userid) {
        try {
            return getResponseHandler()
                    .setDataAdapter(new EventAdapter(usereventService))
                    .success(eventService.getEventById(id));
        } catch (final Exception e) {
            LOGGER.error(EGOV_API_ERROR, e);
            return getResponseHandler().error(getMessage(SERVER_ERROR_KEY));
        }
    }

    @GetMapping(path = SEARCH_EVENT, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> searchEvent(@RequestParam(required = false) String eventType,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String eventHost,
            @RequestParam String eventDateType) {

        Event eventObj = new Event();
        EventType eventTypeObj = new EventType();
        eventTypeObj.setName(eventType);
        eventObj.setEventType(eventTypeObj);
        eventObj.setName(name);
        eventObj.setEventhost(eventHost);
        try {
            return getResponseHandler()
                    .setDataAdapter(new EventSearchAdapter(usereventService))
                    .success(eventService.searchEvent(eventObj, eventDateType));
        } catch (final Exception e) {
            LOGGER.error(EGOV_API_ERROR, e);
            return getResponseHandler().error(getMessage(SERVER_ERROR_KEY));
        }
    }

    @GetMapping(path = SEARCH_DRAFT_PATH_PARAM, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> searchDraft(@RequestParam(required = false) String type,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long module) {

        Drafts draftObj = new Drafts();
        DraftType draftType = new DraftType();
        draftType.setName(type);
        draftObj.setDraftType(draftType);
        draftObj.setName(name);

        try {
            return getResponseHandler()
                    .setDataAdapter(new DraftsAdapter())
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
                    .success(moduleCategoryService.getCategoriesForModule(moduleId));
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
                    .success(categoryParametersService.getParametersForCategory(categoryId));
        } catch (final Exception e) {
            LOGGER.error(EGOV_API_ERROR, e);
            return getResponseHandler().error(getMessage(SERVER_ERROR_KEY));
        }
    }

    @PostMapping(path = GET_EVENT + INTERESTED, produces = APPLICATION_JSON_VALUE)
    public String saveUserEvent(@RequestBody String jsonData) {
        JSONObject requestObject = (JSONObject) JSONValue.parse(jsonData);
        JSONObject responseObject = new JSONObject();
        if (requestObject.containsKey(USERID)
                && requestObject.containsKey(EVENTID)) {
            UserEvent userEvent = usereventService.saveUserEvent(Long.parseLong(requestObject.get(USERID).toString()),
                    Long.parseLong(requestObject.get(EVENTID).toString()));
            if (userEvent == null)
                responseObject.put(FAIL, getMessage("error.fail.event"));
            else {
                responseObject.put(SUCCESS, getMessage("msg.event.success"));
                Long interestedCount = usereventService.countUsereventByEventId(userEvent.getEvent().getId());
                if (interestedCount == null)
                    responseObject.put(INTERESTED_COUNT, ZERO);
                else
                    responseObject.put(INTERESTED_COUNT, String.valueOf(interestedCount));
            }

        } else
            responseObject.put(FAIL, getMessage("error.fail.eventuser"));

        return responseObject.toJSONString();
    }

    @GetMapping(path = EVENT_IMAGE_DOWNLOAD, produces = APPLICATION_JSON_VALUE)
    public void downloadEventImage(@PathVariable final Long id, @PathVariable final String fileStoreId,
            final HttpServletResponse response) throws IOException {
        try {
            Event event = eventService.getEventById(id);
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