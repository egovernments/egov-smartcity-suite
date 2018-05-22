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
package org.egov.eventnotification.web.controller.event;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.egov.eventnotification.constants.Constants;
import org.egov.eventnotification.entity.Event;
import org.egov.eventnotification.entity.EventType;
import org.egov.eventnotification.service.EventService;
import org.egov.eventnotification.utils.EventnotificationUtil;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * This is the EventController class. Which is basically used to create, view and update the event.
 * @author somvit
 *
 */
@Controller
@RequestMapping(value = Constants.API_EVENT)
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private EventnotificationUtil eventnotificationUtil;

    @Autowired
    private UserService userService;

    /**
     * This method is used for view all event and view event by id.
     * @param model
     * @param id
     * @return tiles view
     */
    @GetMapping(Constants.API_VIEW)
    public String view(final Model model) {
        model.addAttribute(Constants.EVENT_LIST,
                eventService.findAllByStatus(Constants.ACTIVE.toUpperCase()));
        model.addAttribute(Constants.MODE, Constants.MODE_VIEW);
        model.addAttribute(Constants.EVENT_TYPE_LIST, new ArrayList<>(Arrays.asList(EventType.values())));
        return Constants.VIEW_EVENTVIEW;
    }

    /**
     * This method is used for view all event and view event by id.
     * @param model
     * @param id
     * @return tiles view
     */
    @GetMapping(Constants.API_VIEW_ID)
    public String viewByEvent(final Model model, @PathVariable(Constants.EVENT_ID) Long id) {
        model.addAttribute(Constants.EVENT, eventService.findByEventId(id));
        model.addAttribute(Constants.MODE, Constants.MODE_VIEW);
        return Constants.VIEW_EVENTVIEWRESULT;
    }

    /**
     * This method is used for show the create event page. It will take fetch all the hours, minutes and event type.
     * @param event
     * @param model
     * @return tiles view
     */
    @GetMapping(Constants.API_CREATE)
    public String save(@ModelAttribute Event event, Model model) {
        model.addAttribute(Constants.EVENT, event);
        model.addAttribute(Constants.HOUR_LIST, eventnotificationUtil.getAllHour());
        model.addAttribute(Constants.MINUTE_LIST, eventnotificationUtil.getAllMinute());
        model.addAttribute(Constants.EVENT_LIST, new ArrayList<>(Arrays.asList(EventType.values())));
        model.addAttribute(Constants.MODE, Constants.MODE_CREATE);
        return Constants.VIEW_EVENTCREATE;
    }

    /**
     * This method is used for create event page.
     * @param event
     * @param files
     * @param model
     * @param redirectAttrs
     * @param request
     * @param errors
     * @return tiles view
     * @throws IOException
     * @throws ParseException
     */
    @PostMapping(Constants.API_CREATE)
    public String save(@ModelAttribute(Constants.EVENT) Event event,
            Model model,
            RedirectAttributes redirectAttrs, HttpServletRequest request, BindingResult errors)
            throws IOException {

        if (errors.hasErrors()) {
            model.addAttribute(Constants.MODE, Constants.MODE_CREATE);
            model.addAttribute(Constants.HOUR_LIST, eventnotificationUtil.getAllHour());
            model.addAttribute(Constants.MINUTE_LIST, eventnotificationUtil.getAllMinute());
            model.addAttribute(Constants.EVENT_LIST, new ArrayList<>(Arrays.asList(EventType.values())));
            model.addAttribute(Constants.MESSAGE,
                    messageSource.getMessage(Constants.MSG_EVENT_CREATE_ERROR, null, Locale.ENGLISH));
            return Constants.VIEW_EVENTCREATE;
        }

        eventService.save(event);
        User user = userService.getCurrentUser();
        eventService.sendPushMessage(event, user);
        redirectAttrs.addFlashAttribute(Constants.EVENT, event);
        model.addAttribute(Constants.MESSAGE,
                messageSource.getMessage(Constants.MSG_EVENT_CREATE_SUCCESS, null, Locale.ENGLISH));
        model.addAttribute(Constants.MODE, Constants.MODE_VIEW);
        return Constants.VIEW_EVENTSUCCESS;
    }
}
