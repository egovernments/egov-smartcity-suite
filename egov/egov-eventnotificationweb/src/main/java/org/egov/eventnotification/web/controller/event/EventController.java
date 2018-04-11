package org.egov.eventnotification.web.controller.event;

import org.egov.eventnotification.utils.EventnotificationUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/event/")
public class EventController {
	
	@RequestMapping(value = "view/", method = RequestMethod.GET)
    public String view(final Model model) {
		model.addAttribute("eventList", null);
        model.addAttribute("mode", "view");
        return "event-view";
	}
	
	@RequestMapping(value = "create/", method = RequestMethod.GET)
    public String create(final Model model) {
		model.addAttribute("hourList", EventnotificationUtil.getAllHour());
		model.addAttribute("minuteList", EventnotificationUtil.getAllMinute());
		model.addAttribute("mode", "create");
        return "event-create";
	}

}
