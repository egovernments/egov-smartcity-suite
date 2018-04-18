package org.egov.eventnotification.web.controller.event;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.egov.eventnotification.constants.EventnotificationConstant;
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

/**
 * This is the EventController class. Which is basically used to create, view and update the event.
 * @author somvit
 *
 */
@Controller
@RequestMapping(value = "/event/")
public class EventController {
	
	@Autowired
	private EventService eventService;
	
	/**
	 * This method is used for view all event and view event by id.
	 * @param model
	 * @param id
	 * @return tiles view
	 */
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
	
	/**
	 * This method is used for show the create event page. It will take fetch all the hours, minutes and event type.
	 * @param event
	 * @param model
	 * @return tiles view
	 */
	@RequestMapping(value = "create/", method = RequestMethod.GET)
    public String newEvent(@ModelAttribute Event event,Model model) {
		model.addAttribute("event", event);
		model.addAttribute("hourList", EventnotificationUtil.getAllHour());
		model.addAttribute("minuteList", EventnotificationUtil.getAllMinute());
		List eventList = new ArrayList<>(Arrays.asList(EventnotificationConstant.EVENT_TYPE.values()));
		model.addAttribute("eventList",eventList);
		model.addAttribute("mode", "create");
        return "event-create";
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
	@RequestMapping(value = "create/", method = RequestMethod.POST)
    public String create(@ModelAttribute("event") Event event, @RequestParam("file") MultipartFile[] files, Model model,
    		RedirectAttributes redirectAttrs, HttpServletRequest request,BindingResult errors) throws IOException, ParseException {
		event.setStartTime(event.getStartHH()+":"+event.getStartMM());
		event.setEndTime(event.getEndHH()+":"+event.getEndMM());
		
		eventService.persist(event, files);
		
		redirectAttrs.addFlashAttribute("event", event);
		model.addAttribute("message", "Event created successfully.");
        model.addAttribute("mode", "view");
        return "event-success";
	}
	
	/**
	 * This method is used for show the event update page based on the event id.
	 * @param event
	 * @param model
	 * @param id
	 * @return tiles view
	 */
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    public String viewUpdate(@ModelAttribute Event event,Model model,@PathVariable("id") Long id) {
		Event eventObj = eventService.findById(id);
		String []st = eventObj.getStartTime().split(":");
		eventObj.setStartHH(st[0]);
		eventObj.setStartMM(st[1]);
		String []et = eventObj.getEndTime().split(":");
		eventObj.setEndHH(et[0]);
		eventObj.setEndMM(et[1]);
		model.addAttribute("event", eventObj);
        model.addAttribute("hourList", EventnotificationUtil.getAllHour());
		model.addAttribute("minuteList", EventnotificationUtil.getAllMinute());
		List eventList = new ArrayList<>(Arrays.asList(EventnotificationConstant.EVENT_TYPE.values()));
		model.addAttribute("eventList",eventList);
		model.addAttribute("mode", "update");
        return "event-update";
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
	@RequestMapping(value = "update/{id}", method = RequestMethod.POST)
    public String update(@ModelAttribute("event") Event event, @RequestParam("file") MultipartFile[] files, Model model,
    		RedirectAttributes redirectAttrs, HttpServletRequest request,BindingResult errors,@PathVariable("id") Long id) throws IOException, ParseException {
		event.setId(id);
		event.setStartTime(event.getStartHH()+":"+event.getStartMM());
		event.setEndTime(event.getEndHH()+":"+event.getEndMM());
		
		eventService.update(event, files);
		
		redirectAttrs.addFlashAttribute("event", event);
		model.addAttribute("message", "Event updated successfully.");
        model.addAttribute("mode", "view");
        return "event-update-success";
	}

}
