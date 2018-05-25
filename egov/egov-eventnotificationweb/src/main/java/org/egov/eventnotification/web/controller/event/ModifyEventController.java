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
import static org.egov.eventnotification.constants.Constants.VIEW_EVENTUPDATE;
import static org.egov.eventnotification.constants.Constants.VIEW_EVENTUPDATESUCCESS;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import javax.validation.Valid;

import org.egov.eventnotification.constants.Constants;
import org.egov.eventnotification.entity.Event;
import org.egov.eventnotification.entity.EventStatus;
import org.egov.eventnotification.entity.EventType;
import org.egov.eventnotification.service.EventService;
import org.egov.eventnotification.utils.EventnotificationUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
    public String update(@PathVariable("id") Long id, @Valid @ModelAttribute Event event,
            BindingResult errors, Model model) throws IOException {

        if (errors.hasErrors()) {
            model.addAttribute(HOUR_LIST, eventnotificationUtil.getAllHour());
            model.addAttribute(MINUTE_LIST, eventnotificationUtil.getAllMinute());
            model.addAttribute(EVENT_LIST, new ArrayList<>(Arrays.asList(EventType.values())));
            model.addAttribute(MODE, MODE_UPDATE);
            model.addAttribute(EVENT_STATUS_LIST, new ArrayList<>(Arrays.asList(EventStatus.values())));
            model.addAttribute(MESSAGE,
                    messageSource.getMessage("msg.event.update.error", null, Locale.ENGLISH));
            return VIEW_EVENTUPDATE;
        }
        event.setId(id);
        DateTime sd = new DateTime(event.getEventDetails().getStartDt());
        sd = sd.withHourOfDay(Integer.parseInt(event.getEventDetails().getStartHH()));
        sd = sd.withMinuteOfHour(Integer.parseInt(event.getEventDetails().getStartMM()));
        sd = sd.withSecondOfMinute(00);
        event.setStartDate(sd.toDate());

        DateTime ed = new DateTime(event.getEventDetails().getEndDt());
        ed = ed.withHourOfDay(Integer.parseInt(event.getEventDetails().getEndHH()));
        ed = ed.withMinuteOfHour(Integer.parseInt(event.getEventDetails().getEndMM()));
        ed = ed.withSecondOfMinute(00);
        event.setEndDate(ed.toDate());
        eventService.updateEvent(event);

        model.addAttribute(EVENT, event);
        model.addAttribute(MESSAGE,
                messageSource.getMessage("msg.event.update.success", null, Locale.ENGLISH));
        model.addAttribute(MODE, MODE_VIEW);
        return VIEW_EVENTUPDATESUCCESS;
    }
}
