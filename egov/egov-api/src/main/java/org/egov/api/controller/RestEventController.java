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
package org.egov.api.controller;

import static org.egov.eventnotification.utils.constants.EventnotificationConstants.ACTIVE;
import static org.egov.eventnotification.utils.constants.EventnotificationConstants.FAIL;
import static org.egov.eventnotification.utils.constants.EventnotificationConstants.INTERESTED_COUNT;
import static org.egov.eventnotification.utils.constants.EventnotificationConstants.MODULE_NAME;
import static org.egov.eventnotification.utils.constants.EventnotificationConstants.SUCCESS;
import static org.egov.eventnotification.utils.constants.EventnotificationConstants.ZERO;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.egov.api.adapter.CategoryParametersAdapter;
import org.egov.api.adapter.DraftsAdapter;
import org.egov.api.adapter.EventAdapter;
import org.egov.api.adapter.EventSearchAdapter;
import org.egov.api.adapter.ModuleCategoryAdapter;
import org.egov.api.controller.core.ApiController;
import org.egov.eventnotification.entity.UserEvent;
import org.egov.eventnotification.entity.contracts.EventSearch;
import org.egov.eventnotification.entity.contracts.UserEventRequest;
import org.egov.eventnotification.service.CategoryParametersService;
import org.egov.eventnotification.service.DraftService;
import org.egov.eventnotification.service.EventService;
import org.egov.eventnotification.service.ModuleCategoryService;
import org.egov.eventnotification.service.UserEventService;
import org.egov.infra.utils.FileStoreUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author somvit
 *
 */
@RestController
public class RestEventController extends ApiController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestEventController.class);

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

    @Autowired
    private FileStoreUtils fileStoreUtils;

    @GetMapping(path = "/events", produces = APPLICATION_JSON_VALUE)
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

    @GetMapping(path = "/event/{id}", produces = APPLICATION_JSON_VALUE)
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

    @GetMapping(path = "/event/search", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> searchEvent(EventSearch eventSearch) {

        try {
            return getResponseHandler()
                    .setDataAdapter(new EventSearchAdapter(usereventService))
                    .success(eventService.searchEvent(eventSearch));
        } catch (final Exception e) {
            LOGGER.error(EGOV_API_ERROR, e);
            return getResponseHandler().error(getMessage(SERVER_ERROR_KEY));
        }
    }

    @GetMapping(path = "/draft/search", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> searchDraft(@RequestParam(required = false) String type,
            @RequestParam(required = false) String name) {

        try {
            return getResponseHandler()
                    .setDataAdapter(new DraftsAdapter())
                    .success(draftService.searchDraft(type, name));
        } catch (final Exception e) {
            LOGGER.error(EGOV_API_ERROR, e);
            return getResponseHandler().error(getMessage(SERVER_ERROR_KEY));
        }
    }

    @GetMapping(path = "/draft/getCategoriesForModule/{moduleId}", produces = APPLICATION_JSON_VALUE)
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

    @GetMapping(path = "/draft/getParametersForCategory/{categoryId}", produces = APPLICATION_JSON_VALUE)
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

    @PostMapping(path = "/event/interested", produces = APPLICATION_JSON_VALUE)
    public String saveUserEvent(@RequestBody UserEventRequest userEventRequest) {
        JSONObject responseObject = new JSONObject();
        if (userEventRequest == null)
            responseObject.put(FAIL, getMessage("error.fail.eventuser"));
        else {
            UserEvent userEvent = usereventService.saveUserEvent(Long.parseLong(userEventRequest.getUserid()),
                    Long.parseLong(userEventRequest.getEventid()));
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
        }

        return responseObject.toJSONString();
    }

    @GetMapping(path = "/event/{id}/download/{fileStoreId}", produces = APPLICATION_JSON_VALUE)
    public void downloadEventImage(@PathVariable final Long id, @PathVariable final String fileStoreId,
            final HttpServletResponse response) throws IOException {
        try {
            fileStoreUtils.writeToHttpResponseStream(fileStoreId, MODULE_NAME, response);
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}