package org.egov.eventnotification.web.controller.schedule;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.egov.eventnotification.constants.Constants;
import org.egov.eventnotification.entity.NotificationDrafts;
import org.egov.eventnotification.entity.NotificationSchedule;
import org.egov.eventnotification.entity.SchedulerRepeat;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ScheduleController {

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
    @GetMapping("/schedule/view/")
    public String view(Model model) {
        model.addAttribute(Constants.DRAFT_LIST, draftService.findAllDrafts());
        model.addAttribute(Constants.SCHEDULE_LIST,
                scheduleService.findAllSchedule());
        return Constants.SCHEDULE_VIEW;
    }

    /**
     * This method is used for load the create schedule page.
     * @param model
     * @param id
     * @param schedule
     * @return tiles view
     */
    @GetMapping("/schedule/create/{id}")
    public String save(@ModelAttribute NotificationSchedule schedule, @PathVariable("id") Long id, Model model) {
        NotificationDrafts notificationDrafts = draftService.findDraftById(id);
        schedule.setStatus(Constants.TO_BE_SCHEDULED);
        schedule.setMessageTemplate(notificationDrafts.getMessage());
        schedule.setTemplatename(notificationDrafts.getName());
        schedule.setNotificationType(notificationDrafts.getType());

        model.addAttribute(Constants.NOTIFICATION_SCHEDULE, schedule);
        model.addAttribute(Constants.HOUR_LIST, eventnotificationUtil.getAllHour());
        model.addAttribute(Constants.MINUTE_LIST, eventnotificationUtil.getAllMinute());
        List<String> repeatList = new ArrayList<>();
        for (SchedulerRepeat schedulerRepeat : SchedulerRepeat.values())
            repeatList.add(schedulerRepeat.getName());

        model.addAttribute(Constants.SCHEDULER_REPEAT_LIST, repeatList);

        return Constants.SCHEDULE_CREATE_VIEW;
    }

    /**
     * This method is used for create new schedule.
     * @param model
     * @param schedule
     * @param redirectAttrs
     * @return tiles view
     */
    @PostMapping("/schedule/create/")
    public String save(@ModelAttribute NotificationSchedule schedule, Model model, RedirectAttributes redirectAttrs,
            BindingResult errors) {

        if (errors.hasErrors()) {
            model.addAttribute(Constants.NOTIFICATION_SCHEDULE, schedule);
            model.addAttribute(Constants.HOUR_LIST, eventnotificationUtil.getAllHour());
            model.addAttribute(Constants.MINUTE_LIST, eventnotificationUtil.getAllMinute());
            List<String> repeatList = new ArrayList<>();
            for (SchedulerRepeat schedulerRepeat : SchedulerRepeat.values())
                repeatList.add(schedulerRepeat.getName());

            model.addAttribute(Constants.SCHEDULER_REPEAT_LIST, repeatList);
            model.addAttribute(Constants.MESSAGE,
                    messageSource.getMessage(Constants.MSG_SCHEDULED_ERROR, null, Locale.ENGLISH));
            return Constants.SCHEDULE_CREATE_VIEW;
        }

        User user = userService.getCurrentUser();
        scheduleService.save(schedule, user);

        schedulerManager.schedule(schedule, user);
        model.addAttribute(Constants.MESSAGE,
                messageSource.getMessage(Constants.MSG_SCHEDULED_SUCCESS, null, Locale.ENGLISH));
        model.addAttribute(Constants.MODE, Constants.MODE_VIEW);
        redirectAttrs.addFlashAttribute(Constants.NOTIFICATION_SCHEDULE, schedule);
        return Constants.SCHEDULE_CREATE_SUCCESS;
    }

    /**
     * This method is used for view schedule details.
     * @param model
     * @param id
     * @return tiles view
     */
    @GetMapping("/schedule/view/{id}")
    public String viewBySchedule(@PathVariable("id") Long id, Model model) {
        NotificationSchedule notificationSchedule = scheduleService.findOne(id);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(notificationSchedule.getEventDetails().getStartDt());
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(notificationSchedule.getStartTime().split(":")[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(notificationSchedule.getStartTime().split(":")[1]));

        if (calendar.getTime().before(new Date()))
            model.addAttribute(Constants.SCHEDULE_EDITABLE, false);
        else
            model.addAttribute(Constants.SCHEDULE_EDITABLE, true);
        model.addAttribute(Constants.NOTIFICATION_SCHEDULE, notificationSchedule);
        model.addAttribute(Constants.MODE, Constants.MODE_DELETE);
        return Constants.SCHEDULE_DETAILS_VIEW;
    }

    /**
     * This method is used for soft delete of schedule.
     * @param model
     * @param schedule
     * @return success
     */
    @GetMapping(path = { "/schedule/delete/{id}" }, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String deleteSchedule(@PathVariable("id") Long id, Model model) {
        User user = userService.getCurrentUser();
        NotificationSchedule notificationSchedule = scheduleService.updateScheduleStatus(id, user, Constants.SCHEDULE_DISABLED);
        schedulerManager.removeJob(notificationSchedule);
        model.addAttribute(Constants.MESSAGE,
                messageSource.getMessage(Constants.MSG_SCHEDULED_SUCCESS, null, Locale.ENGLISH));
        return Constants.SCHEDULE_DELETE_SUCCESS;
    }
}
