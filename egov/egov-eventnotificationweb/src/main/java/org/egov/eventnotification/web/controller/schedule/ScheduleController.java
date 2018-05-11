package org.egov.eventnotification.web.controller.schedule;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.egov.eventnotification.constants.EventnotificationConstant;
import org.egov.eventnotification.constants.EventnotificationConstant.SCHEDULER_REPEAT;
import org.egov.eventnotification.entity.EventDetails;
import org.egov.eventnotification.entity.NotificationDrafts;
import org.egov.eventnotification.entity.NotificationSchedule;
import org.egov.eventnotification.scheduler.NotificationSchedulerManager;
import org.egov.eventnotification.service.DraftService;
import org.egov.eventnotification.service.ScheduleService;
import org.egov.eventnotification.utils.EventnotificationUtil;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ScheduleController {
    private static final Logger LOGGER = Logger.getLogger(ScheduleController.class);

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private DraftService draftService;

    @Autowired
    private EventnotificationUtil eventnotificationUtil;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserService userService;
    
    @Autowired
    private NotificationSchedulerManager schedulerManager;

    /**
     * This method is used for view all drafts and schedule.
     * @param model
     * @return tiles view
     */
    @RequestMapping(value = { "/schedule/view/" }, method = RequestMethod.GET)
    public String view(Model model) {
        model.addAttribute(EventnotificationConstant.DRAFT_LIST, draftService.findAllDrafts());
        model.addAttribute(EventnotificationConstant.SCHEDULE_LIST,
                scheduleService.findAllSchedule(EventnotificationConstant.SCHEDULED_STATUS));
        return EventnotificationConstant.SCHEDULE_VIEW;
    }

    /**
     * This method is used for load the create schedule page.
     * @param model
     * @param id
     * @param schedule
     * @return tiles view
     */
    @RequestMapping(value = { "/schedule/create/{id}" }, method = RequestMethod.GET)
    public String viewCreateSchedule(@ModelAttribute NotificationSchedule schedule, @PathVariable("id") Long id, Model model) {
        NotificationDrafts notificationDrafts = draftService.findDraftById(id);
        schedule.setStatus(EventnotificationConstant.TO_BE_SCHEDULED);
        schedule.setMessageTemplate(notificationDrafts.getMessage());
        schedule.setTemplatename(notificationDrafts.getName());
        schedule.setNotificationType(notificationDrafts.getType());

        model.addAttribute(EventnotificationConstant.NOTIFICATION_SCHEDULE, schedule);
        model.addAttribute(EventnotificationConstant.HOUR_LIST, eventnotificationUtil.getAllHour());
        model.addAttribute(EventnotificationConstant.MINUTE_LIST, eventnotificationUtil.getAllMinute());
        List<String> repeatList = new ArrayList<>();
        for (SCHEDULER_REPEAT SCHEDULER_REPEAT : EventnotificationConstant.SCHEDULER_REPEAT.values())
            repeatList.add(SCHEDULER_REPEAT.getName());
        model.addAttribute(EventnotificationConstant.SCHEDULER_REPEAT_LIST, repeatList);
        return EventnotificationConstant.SCHEDULE_CREATE_VIEW;
    }

    /**
     * This method is used for create new schedule.
     * @param model
     * @param schedule
     * @param redirectAttrs
     * @return tiles view
     */
    @RequestMapping(value = { "/schedule/create/" }, method = RequestMethod.POST)
    public String saveSchedule(@ModelAttribute NotificationSchedule schedule, Model model, RedirectAttributes redirectAttrs) {
        try {
            User user = userService.getCurrentUser();
            schedule.setStartDate(schedule.getEventDetails().getStartDt().getTime());
            schedule.setStartTime(schedule.getEventDetails().getStartHH() + ":" + schedule.getEventDetails().getStartMM());
            schedule.setStatus(EventnotificationConstant.SCHEDULED_STATUS);
            schedule.setCreatedBy(user);
            schedule.setCreatedDate(new Date().getTime());
            schedule.setUpdatedby(user);
            schedule.setUpdatedDate(new Date().getTime());
            scheduleService.persist(schedule);

            schedulerManager.schedule(schedule, user);
            model.addAttribute(EventnotificationConstant.MESSAGE,
                    messageSource.getMessage(EventnotificationConstant.MSG_SCHEDULED_SUCCESS, null, Locale.ENGLISH));
            model.addAttribute(EventnotificationConstant.MODE, EventnotificationConstant.MODE_VIEW);
            redirectAttrs.addFlashAttribute(EventnotificationConstant.NOTIFICATION_SCHEDULE, schedule);
        } catch (IOException | ParseException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return EventnotificationConstant.SCHEDULE_CREATE_SUCCESS;
    }

    /**
     * This method is used for view schedule details.
     * @param model
     * @param id
     * @return tiles view
     */
    @RequestMapping(value = { "/schedule/view/{id}" }, method = RequestMethod.GET)
    public String viewScheduleDetails(@PathVariable("id") Long id, Model model) {
        NotificationSchedule notificationSchedule = scheduleService.findOne(id);
        DateFormat formatter = new SimpleDateFormat(EventnotificationConstant.DDMMYYYY);
        try {
            EventDetails eventDetails = new EventDetails();
            Date sd = new Date(notificationSchedule.getStartDate());
            eventDetails.setStartDt(formatter.parse(formatter.format(sd)));
            notificationSchedule.setEventDetails(eventDetails);
        } catch (ParseException e) {
            LOGGER.error(e.getMessage(), e);
        }
        model.addAttribute(EventnotificationConstant.NOTIFICATION_SCHEDULE, notificationSchedule);
        model.addAttribute(EventnotificationConstant.MODE, EventnotificationConstant.MODE_DELETE);
        return EventnotificationConstant.SCHEDULE_DETAILS_VIEW;
    }

    /**
     * This method is used for soft delete of schedule.
     * @param model
     * @param schedule
     * @return success
     */
    @RequestMapping(value = { "/schedule/delete/{id}" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String deleteSchedule(@PathVariable("id") Long id, Model model) {
        User user = userService.getCurrentUser();
        NotificationSchedule notificationSchedule = scheduleService.updateSchedule(id, user);
        schedulerManager.removeJob(notificationSchedule);
        model.addAttribute(EventnotificationConstant.MESSAGE,
                messageSource.getMessage(EventnotificationConstant.MSG_SCHEDULED_SUCCESS, null, Locale.ENGLISH));
        return EventnotificationConstant.SCHEDULE_DELETE_SUCCESS;
    }

    /**
     * This method is used for view the update schedule page.
     * @param model
     * @param id
     * @return tiles view
     */
    @RequestMapping(value = { "/schedule/update/{id}" }, method = RequestMethod.GET)
    public String viewUpdateSchedule(@PathVariable("id") Long id, Model model) {
        DateFormat formatter = new SimpleDateFormat(EventnotificationConstant.DDMMYYYY);
        NotificationSchedule notificationSchedule = scheduleService.findOne(id);
        try {
            EventDetails eventDetails = new EventDetails();
            Date sd = new Date(notificationSchedule.getStartDate());
            eventDetails.setStartDt(formatter.parse(formatter.format(sd)));
            eventDetails.setStartHH(notificationSchedule.getStartTime().split(":")[0]);
            eventDetails.setStartMM(notificationSchedule.getStartTime().split(":")[1]);
            notificationSchedule.setEventDetails(eventDetails);
        } catch (ParseException e) {
            LOGGER.error(e.getMessage(), e);
        }
        model.addAttribute(EventnotificationConstant.NOTIFICATION_SCHEDULE, notificationSchedule);
        model.addAttribute(EventnotificationConstant.HOUR_LIST, eventnotificationUtil.getAllHour());
        model.addAttribute(EventnotificationConstant.MINUTE_LIST, eventnotificationUtil.getAllMinute());
        List<String> repeatList = new ArrayList<>();
        for (SCHEDULER_REPEAT SCHEDULER_REPEAT : EventnotificationConstant.SCHEDULER_REPEAT.values())
            repeatList.add(SCHEDULER_REPEAT.getName());
        model.addAttribute(EventnotificationConstant.SCHEDULER_REPEAT_LIST, repeatList);
        model.addAttribute(EventnotificationConstant.MODE, EventnotificationConstant.MODE_VIEW);

        return EventnotificationConstant.SCHEDULE_UPDATE_VIEW;
    }

    /**
     * This method is used for update schedule.
     * @param model
     * @param schedule
     * @param id
     * @param redirectAttrs
     * @return tiles view
     */
    @RequestMapping(value = { "/schedule/update/{id}" }, method = RequestMethod.POST)
    public String updateSchedule(@ModelAttribute NotificationSchedule schedule, @PathVariable("id") Long id, Model model,
            RedirectAttributes redirectAttrs) {
        User user = userService.getCurrentUser();
        schedule.setId(id);
        schedule.setStartDate(schedule.getEventDetails().getStartDt().getTime());
        schedule.setStartTime(schedule.getEventDetails().getStartHH() + ":" + schedule.getEventDetails().getStartMM());
        schedule.setStatus(EventnotificationConstant.SCHEDULED_STATUS);

        NotificationSchedule notificationSchedule = scheduleService.updateScheduleDetails(schedule, user);
        schedulerManager.updateJob(notificationSchedule, user);

        redirectAttrs.addFlashAttribute(EventnotificationConstant.NOTIFICATION_SCHEDULE, notificationSchedule);
        model.addAttribute(EventnotificationConstant.MESSAGE,
                messageSource.getMessage(EventnotificationConstant.MSG_SCHEDULED_UPDATE_SUCCESS, null, Locale.ENGLISH));
        return EventnotificationConstant.SCHEDULE_UPDATE_SUCCESS;
    }

}
