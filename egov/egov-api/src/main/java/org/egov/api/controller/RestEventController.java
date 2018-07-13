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

import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.egov.eventnotification.utils.Constants.ACTIVE;
import static org.egov.eventnotification.utils.Constants.MODULE_NAME;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.egov.api.adapter.DraftsAdapter;
import org.egov.api.adapter.EventAdapter;
import org.egov.api.adapter.EventSearchAdapter;
import org.egov.api.adapter.InterestedCountAdapter;
import org.egov.api.adapter.TemplateParameterAdapter;
import org.egov.api.adapter.TemplateSubCategoryAdapter;
import org.egov.api.controller.core.ApiController;
import org.egov.api.controller.core.ApiResponse;
import org.egov.eventnotification.entity.UserEvent;
import org.egov.eventnotification.entity.contracts.EventSearch;
import org.egov.eventnotification.entity.contracts.UserEventRequest;
import org.egov.eventnotification.service.DraftService;
import org.egov.eventnotification.service.EventService;
import org.egov.eventnotification.service.TemplateParameterService;
import org.egov.eventnotification.service.TemplateSubCategoryService;
import org.egov.eventnotification.service.UserEventService;
import org.egov.infra.utils.FileStoreUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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

    @Autowired
    private EventService eventService;

    @Autowired
    private DraftService draftService;

    @Autowired
    private UserEventService userEventService;

    @Autowired
    private TemplateSubCategoryService templateSubCategoryService;

    @Autowired
    private TemplateParameterService templateParameterService;

    @Autowired
    private FileStoreUtils fileStoreUtils;

    @GetMapping(path = "/events", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAllEvent() {
        return getResponseHandler()
                .setDataAdapter(new EventAdapter(userEventService))
                .success(eventService.getAllOngoingEvent(ACTIVE.toUpperCase()));
    }

    @GetMapping(path = "/event/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getEvent(@PathVariable long id, @RequestParam(required = false) Long userid) {
        return getResponseHandler()
                .setDataAdapter(new EventAdapter(userEventService))
                .success(eventService.getEventById(id));
    }

    @GetMapping(path = "/event/search", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> searchEvent(EventSearch eventSearch) {

        return getResponseHandler()
                .setDataAdapter(new EventSearchAdapter(userEventService))
                .success(eventService.searchEvent(eventSearch));
    }

    @GetMapping(path = "/draft/search", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> searchDraft(@RequestParam(required = false) String type,
            @RequestParam(required = false) String name) {

        return getResponseHandler()
                .setDataAdapter(new DraftsAdapter())
                .success(draftService.searchDraft(type, name));
    }

    @GetMapping(path = "/draft/getCategoriesForModule/{moduleId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getCategoriesForModule(@PathVariable long moduleId) {
        return getResponseHandler()
                .setDataAdapter(new TemplateSubCategoryAdapter())
                .success(templateSubCategoryService.getCategoriesForModule(moduleId));
    }

    @GetMapping(path = "/draft/getParametersForCategory/{categoryId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getParametersForCategory(@PathVariable long categoryId) {
        return getResponseHandler()
                .setDataAdapter(new TemplateParameterAdapter())
                .success(templateParameterService.getParametersForCategory(categoryId));
    }

    @PostMapping(path = "/event/interested", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveUserEvent(@Valid @RequestBody UserEventRequest userEventRequest, BindingResult errors) {
        ApiResponse res = ApiResponse.newInstance();

        if (errors.hasErrors()) {
            String errorMessage = EMPTY;
            for (FieldError fieldError : errors.getFieldErrors())
                errorMessage = errorMessage
                        .concat(fieldError.getField().concat(" ").concat(fieldError.getDefaultMessage()).concat(" <br>"));
            return res.error(errorMessage);
        }

        UserEvent userEvent = userEventService.saveUserEvent(userEventRequest.getUserid(), userEventRequest.getEventid());
        if (userEvent == null)
            return res.error(getMessage("user.event.already.exists"));
        else {
            Long interestedCount = userEventService.countUsereventByEventId(userEvent.getEvent().getId());
            return res.setDataAdapter(new InterestedCountAdapter()).success(interestedCount);
        }
    }

    @GetMapping(path = "/event/{id}/download/{fileStoreId}", produces = APPLICATION_JSON_VALUE)
    public void downloadEventImage(@PathVariable final Long id, @PathVariable final String fileStoreId,
            final HttpServletResponse response) {
        fileStoreUtils.writeToHttpResponseStream(fileStoreId, MODULE_NAME, response);
    }
}