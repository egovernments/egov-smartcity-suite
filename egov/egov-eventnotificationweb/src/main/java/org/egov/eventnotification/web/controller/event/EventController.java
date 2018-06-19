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

import static org.egov.eventnotification.constants.ConstantsHelper.EVENT;
import static org.egov.eventnotification.constants.ConstantsHelper.EVENT_LIST;
import static org.egov.eventnotification.constants.ConstantsHelper.EVENT_TYPE_LIST;
import static org.egov.eventnotification.constants.ConstantsHelper.HOUR_LIST;
import static org.egov.eventnotification.constants.ConstantsHelper.MINUTE_LIST;
import static org.egov.eventnotification.constants.ConstantsHelper.MODE;
import static org.egov.eventnotification.constants.ConstantsHelper.MODE_CREATE;
import static org.egov.eventnotification.constants.ConstantsHelper.MODE_VIEW;

import java.io.IOException;

import javax.validation.Valid;

import org.egov.eventnotification.entity.Event;
import org.egov.eventnotification.service.EventService;
import org.egov.eventnotification.service.EventTypeService;
import org.egov.eventnotification.utils.EventnotificationUtil;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author somvit
 *
 */
@Controller
@RequestMapping(value = "/event/")
public class EventController {

    private static final String ACTIVE = "Active";
    private static final String MESSAGE = "message";

    @Autowired
    private EventService eventService;

    @Autowired
    private EventnotificationUtil eventnotificationUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private EventTypeService eventTypeService;

    @GetMapping("view/")
    public String view(@ModelAttribute Event event, final Model model) {
        model.addAttribute(EVENT_LIST,
                eventService.getAllEventByStatus(ACTIVE.toUpperCase()));
        model.addAttribute(MODE, MODE_VIEW);
        model.addAttribute(EVENT_TYPE_LIST, eventTypeService.getAllEventType());
        return "event-view";
    }

    @GetMapping("view/{id}")
    public String viewByEvent(@PathVariable Long id, final Model model) {
        model.addAttribute(EVENT, eventService.getEventById(id));
        model.addAttribute(MODE, MODE_VIEW);
        return "event-view-result";
    }

    @GetMapping("create/")
    public String save(@ModelAttribute Event event, Model model) {
        model.addAttribute(EVENT, event);
        model.addAttribute(HOUR_LIST, eventnotificationUtil.getAllHour());
        model.addAttribute(MINUTE_LIST, eventnotificationUtil.getAllMinute());
        model.addAttribute(EVENT_LIST, eventTypeService.getAllEventType());
        model.addAttribute(MODE, MODE_CREATE);
        return "event-create";
    }

    @PostMapping("create/")
    public String save(@Valid @ModelAttribute Event event,
            BindingResult errors, Model model)
            throws IOException {

        if (errors.hasErrors()) {
            model.addAttribute(MODE, MODE_CREATE);
            model.addAttribute(HOUR_LIST, eventnotificationUtil.getAllHour());
            model.addAttribute(MINUTE_LIST, eventnotificationUtil.getAllMinute());
            model.addAttribute(EVENT_LIST, eventTypeService.getAllEventType());
            model.addAttribute(MESSAGE, "msg.event.create.error");
            return "event-create";
        }

        eventService.saveEvent(event);
        User user = userService.getCurrentUser();
        eventService.sendPushMessage(event, user);
        model.addAttribute(EVENT, event);
        model.addAttribute(MESSAGE, "msg.event.create.success");
        model.addAttribute(MODE, MODE_VIEW);
        return "event-success";
    }
}