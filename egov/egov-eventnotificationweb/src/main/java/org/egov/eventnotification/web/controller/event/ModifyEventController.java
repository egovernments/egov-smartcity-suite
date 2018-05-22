package org.egov.eventnotification.web.controller.event;

import static org.egov.eventnotification.constants.Constants.EVENT;
import static org.egov.eventnotification.constants.Constants.EVENT_LIST;
import static org.egov.eventnotification.constants.Constants.EVENT_STATUS_LIST;
import static org.egov.eventnotification.constants.Constants.HOUR_LIST;
import static org.egov.eventnotification.constants.Constants.MESSAGE;
import static org.egov.eventnotification.constants.Constants.MINUTE_LIST;
import static org.egov.eventnotification.constants.Constants.MODE;
import static org.egov.eventnotification.constants.Constants.MODE_UPDATE;
import static org.egov.eventnotification.constants.Constants.MODE_VIEW;
import static org.egov.eventnotification.constants.Constants.MSG_EVENT_UPDATE_ERROR;
import static org.egov.eventnotification.constants.Constants.MSG_EVENT_UPDATE_SUCCESS;
import static org.egov.eventnotification.constants.Constants.VIEW_EVENTUPDATE;
import static org.egov.eventnotification.constants.Constants.VIEW_EVENTUPDATESUCCESS;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import org.egov.eventnotification.constants.Constants;
import org.egov.eventnotification.entity.Event;
import org.egov.eventnotification.entity.EventStatus;
import org.egov.eventnotification.entity.EventType;
import org.egov.eventnotification.service.EventService;
import org.egov.eventnotification.utils.EventnotificationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ModifyEventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private EventnotificationUtil eventnotificationUtil;

    @ModelAttribute("event")
    public Event getEvent(@PathVariable("id") Long id) {
        return eventService.findByEventId(id);
    }

    /**
     * This method is used for show the event update page based on the event id.
     * @param event
     * @param model
     * @param id
     * @return tiles view
     */
    @GetMapping("/event/update/{id}")
    public String update(@ModelAttribute Event event, Model model) {
        model.addAttribute(HOUR_LIST, eventnotificationUtil.getAllHour());
        model.addAttribute(MINUTE_LIST, eventnotificationUtil.getAllMinute());
        model.addAttribute(EVENT_LIST, new ArrayList<>(Arrays.asList(EventType.values())));
        model.addAttribute(MODE, MODE_UPDATE);
        model.addAttribute(EVENT_STATUS_LIST, new ArrayList<>(Arrays.asList(EventStatus.values())));
        return Constants.VIEW_EVENTUPDATE;
    }

    /**
     * This method is used for update the event.
     * @param event
     * @param files
     * @param model
     * @param redirectAttrs
     * @param request
     * @param errors
     * @param id
     * @return tiles view
     * @throws IOException
     * @throws ParseException
     */
    @PostMapping("/event/update/{id}")
    public String update(@PathVariable("id") Long id, @ModelAttribute Event event,
            RedirectAttributes redirectAttrs,
            BindingResult errors, Model model) throws IOException {

        if (errors.hasErrors()) {
            model.addAttribute(HOUR_LIST, eventnotificationUtil.getAllHour());
            model.addAttribute(MINUTE_LIST, eventnotificationUtil.getAllMinute());
            model.addAttribute(EVENT_LIST, new ArrayList<>(Arrays.asList(EventType.values())));
            model.addAttribute(MODE, MODE_UPDATE);
            model.addAttribute(EVENT_STATUS_LIST, new ArrayList<>(Arrays.asList(EventStatus.values())));
            model.addAttribute(MESSAGE,
                    messageSource.getMessage(MSG_EVENT_UPDATE_ERROR, null, Locale.ENGLISH));
            return VIEW_EVENTUPDATE;
        }
        event.setId(id);
        event.setEndDate(event.getEventDetails().getEndDt().getTime());
        event.setEndTime(event.getEventDetails().getEndHH() + ":" + event.getEventDetails().getEndMM());
        event.setStartDate(event.getEventDetails().getStartDt().getTime());
        event.setStartTime(event.getEventDetails().getStartHH() + ":" + event.getEventDetails().getStartMM());
        eventService.update(event);

        redirectAttrs.addFlashAttribute(EVENT, event);
        model.addAttribute(MESSAGE,
                messageSource.getMessage(MSG_EVENT_UPDATE_SUCCESS, null, Locale.ENGLISH));
        model.addAttribute(MODE, MODE_VIEW);
        return VIEW_EVENTUPDATESUCCESS;
    }
}
