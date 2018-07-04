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
package org.egov.eventnotification.web.controller.event;

import static org.egov.eventnotification.utils.constants.Constants.ALTERROR;
import static org.egov.eventnotification.utils.constants.Constants.EVENT;
import static org.egov.eventnotification.utils.constants.Constants.EVENT_LIST;
import static org.egov.eventnotification.utils.constants.Constants.EVENT_STATUS_LIST;
import static org.egov.eventnotification.utils.constants.Constants.HOUR_LIST;
import static org.egov.eventnotification.utils.constants.Constants.MESSAGE;
import static org.egov.eventnotification.utils.constants.Constants.MINUTE_LIST;
import static org.egov.eventnotification.utils.constants.Constants.MODE;
import static org.egov.eventnotification.utils.constants.Constants.MODE_UPDATE;
import static org.egov.eventnotification.utils.constants.Constants.MODE_VIEW;
import static org.egov.eventnotification.utils.constants.Constants.VIEWNAME;

import java.util.Arrays;

import javax.validation.Valid;

import org.egov.eventnotification.entity.Event;
import org.egov.eventnotification.entity.enums.EventStatus;
import org.egov.eventnotification.service.EventService;
import org.egov.eventnotification.service.EventTypeService;
import org.egov.eventnotification.utils.EventNotificationUtil;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ModifyEventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private EventNotificationUtil eventNotificationUtil;

    @Autowired
    private EventTypeService eventTypeService;

    @ModelAttribute("event")
    public Event getEvent(@PathVariable Long id) {
        return eventService.getEventById(id);
    }

    @GetMapping("/event/update/{id}")
    public String update(@ModelAttribute Event event, Model model) {
        model.addAttribute(HOUR_LIST, eventNotificationUtil.getAllHour());
        model.addAttribute(MINUTE_LIST, eventNotificationUtil.getAllMinute());
        model.addAttribute(EVENT_LIST, eventTypeService.getAllEventType());
        model.addAttribute(MODE, MODE_UPDATE);
        model.addAttribute(EVENT_STATUS_LIST, Arrays.asList(EventStatus.values()));
        return "event-update";
    }

    @PostMapping("/event/update/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute Event event,
            BindingResult errors, Model model) {

        if (errors.hasErrors()) {
            model.addAttribute(HOUR_LIST, eventNotificationUtil.getAllHour());
            model.addAttribute(MINUTE_LIST, eventNotificationUtil.getAllMinute());
            model.addAttribute(EVENT_LIST, eventTypeService.getAllEventType());
            model.addAttribute(MODE, MODE_UPDATE);
            model.addAttribute(EVENT_STATUS_LIST, Arrays.asList(EventStatus.values()));
            model.addAttribute(MESSAGE, "msg.event.update.error");
            return "event-update";
        }

        eventService.updateEvent(event);
        model.addAttribute(EVENT, event);
        model.addAttribute(MESSAGE, "msg.event.update.success");
        model.addAttribute(MODE, MODE_VIEW);
        return "event-update-success";
    }

    @ExceptionHandler(ApplicationRuntimeException.class)
    public ModelAndView configurationErrors(ApplicationRuntimeException exception) {
        ModelAndView model = new ModelAndView();
        model.setViewName(VIEWNAME);
        model.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        model.addObject(ALTERROR, HttpStatus.INTERNAL_SERVER_ERROR.toString());
        return model;
    }
}