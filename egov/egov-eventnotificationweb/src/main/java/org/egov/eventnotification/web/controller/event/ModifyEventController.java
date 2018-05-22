package org.egov.eventnotification.web.controller.event;

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
        model.addAttribute(Constants.HOUR_LIST, eventnotificationUtil.getAllHour());
        model.addAttribute(Constants.MINUTE_LIST, eventnotificationUtil.getAllMinute());
        model.addAttribute(Constants.EVENT_LIST, new ArrayList<>(Arrays.asList(EventType.values())));
        model.addAttribute(Constants.MODE, Constants.MODE_UPDATE);
        model.addAttribute(Constants.EVENT_STATUS_LIST, new ArrayList<>(Arrays.asList(EventStatus.values())));
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
    public String update(@ModelAttribute Event event, @PathVariable("id") Long id,
            RedirectAttributes redirectAttrs,
            BindingResult errors, Model model) throws IOException {

        if (errors.hasErrors()) {
            model.addAttribute(Constants.HOUR_LIST, eventnotificationUtil.getAllHour());
            model.addAttribute(Constants.MINUTE_LIST, eventnotificationUtil.getAllMinute());
            model.addAttribute(Constants.EVENT_LIST, new ArrayList<>(Arrays.asList(EventType.values())));
            model.addAttribute(Constants.MODE, Constants.MODE_UPDATE);
            model.addAttribute(Constants.EVENT_STATUS_LIST, new ArrayList<>(Arrays.asList(EventStatus.values())));
            model.addAttribute(Constants.MESSAGE,
                    messageSource.getMessage(Constants.MSG_EVENT_UPDATE_ERROR, null, Locale.ENGLISH));
            return Constants.VIEW_EVENTUPDATE;
        }
        event.setId(id);
        event.setEndDate(event.getEventDetails().getEndDt().getTime());
        event.setEndTime(event.getEventDetails().getEndHH() + ":" + event.getEventDetails().getEndMM());
        event.setStartDate(event.getEventDetails().getStartDt().getTime());
        event.setStartTime(event.getEventDetails().getStartHH() + ":" + event.getEventDetails().getStartMM());
        eventService.update(event);

        redirectAttrs.addFlashAttribute(Constants.EVENT, event);
        model.addAttribute(Constants.MESSAGE,
                messageSource.getMessage(Constants.MSG_EVENT_UPDATE_SUCCESS, null, Locale.ENGLISH));
        model.addAttribute(Constants.MODE, Constants.MODE_VIEW);
        return Constants.VIEW_EVENTUPDATESUCCESS;
    }
}
