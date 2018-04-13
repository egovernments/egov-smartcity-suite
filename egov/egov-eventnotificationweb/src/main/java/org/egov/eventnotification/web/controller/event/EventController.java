package org.egov.eventnotification.web.controller.event;

import java.io.IOException;
import java.text.ParseException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.egov.eventnotification.entity.Event;
import org.egov.eventnotification.service.EventService;
import org.egov.eventnotification.utils.EventnotificationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/event/")
public class EventController {
	
	@Autowired
	private EventService eventService;
	
	@RequestMapping(value = {"view/","view/{id}"}, method = RequestMethod.GET)
    public String view(final Model model,@PathVariable("id") Optional<Long> id) {
		if(!id.isPresent()) {
			model.addAttribute("eventList", eventService.findAll());
	        model.addAttribute("mode", "view");
	        return "event-view";
		}else {
			Event event = eventService.findById(id.get());
			model.addAttribute("event", event);
	        model.addAttribute("mode", "view");
	        return "event-view-result";
		}
		
		
	}
		
	@RequestMapping(value = "create/", method = RequestMethod.GET)
    public String newEvent(@ModelAttribute Event event,Model model) {
		model.addAttribute("event", event);
		model.addAttribute("hourList", EventnotificationUtil.getAllHour());
		model.addAttribute("minuteList", EventnotificationUtil.getAllMinute());
		model.addAttribute("mode", "create");
        return "event-create";
	}
	
	@RequestMapping(value = "create/", method = RequestMethod.POST)
    public String create(@ModelAttribute("event") Event event, @RequestParam("file") MultipartFile[] files, Model model,
    		RedirectAttributes redirectAttrs, HttpServletRequest request,BindingResult errors) throws IOException, ParseException {
		System.out.println("name===>>>"+request.getAttribute("name"));
		event.setStartTime(event.getStartHH()+":"+event.getStartMM());
		event.setEndTime(event.getEndHH()+":"+event.getEndMM());
		
		eventService.persist(event, files);
		
		redirectAttrs.addFlashAttribute("event", event);
		model.addAttribute("message", "Event created successfully.");
        model.addAttribute("mode", "view");
        return "event-success";
	}

}
