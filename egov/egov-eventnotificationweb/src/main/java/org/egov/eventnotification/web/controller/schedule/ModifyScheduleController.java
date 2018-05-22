package org.egov.eventnotification.web.controller.schedule;

import static org.egov.eventnotification.constants.Constants.HOUR_LIST;
import static org.egov.eventnotification.constants.Constants.MESSAGE;
import static org.egov.eventnotification.constants.Constants.MINUTE_LIST;
import static org.egov.eventnotification.constants.Constants.MODE;
import static org.egov.eventnotification.constants.Constants.MODE_VIEW;
import static org.egov.eventnotification.constants.Constants.MSG_SCHEDULED_UPDATE_ERROR;
import static org.egov.eventnotification.constants.Constants.MSG_SCHEDULED_UPDATE_SUCCESS;
import static org.egov.eventnotification.constants.Constants.NOTIFICATION_SCHEDULE;
import static org.egov.eventnotification.constants.Constants.SCHEDULED_STATUS;
import static org.egov.eventnotification.constants.Constants.SCHEDULER_REPEAT_LIST;
import static org.egov.eventnotification.constants.Constants.SCHEDULE_UPDATE_SUCCESS;
import static org.egov.eventnotification.constants.Constants.SCHEDULE_UPDATE_VIEW;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.egov.eventnotification.constants.Constants;
import org.egov.eventnotification.entity.NotificationSchedule;
import org.egov.eventnotification.entity.SchedulerRepeat;
import org.egov.eventnotification.scheduler.NotificationSchedulerManager;
import org.egov.eventnotification.service.ScheduleService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ModifyScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private EventnotificationUtil eventnotificationUtil;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationSchedulerManager schedulerManager;

    @ModelAttribute("notificationSchedule")
    public NotificationSchedule getNotificationSchedule(@PathVariable("id") Long id) {
        return scheduleService.findOne(id);
    }

    /**
     * This method is used for view the update schedule page.
     * @param model
     * @param id
     * @return tiles view
     */
    @GetMapping("/schedule/update/{id}")
    public String update(@ModelAttribute NotificationSchedule notificationSchedule, Model model) {
        model.addAttribute(HOUR_LIST, eventnotificationUtil.getAllHour());
        model.addAttribute(MINUTE_LIST, eventnotificationUtil.getAllMinute());
        List<String> repeatList = new ArrayList<>();
        for (SchedulerRepeat schedulerRepeat : SchedulerRepeat.values())
            repeatList.add(schedulerRepeat.getName());

        model.addAttribute(SCHEDULER_REPEAT_LIST, repeatList);
        model.addAttribute(MODE, MODE_VIEW);

        return SCHEDULE_UPDATE_VIEW;
    }

    /**
     * This method is used for update schedule.
     * @param model
     * @param schedule
     * @param id
     * @param redirectAttrs
     * @return tiles view
     */
    @PostMapping("/schedule/update/{id}")
    public String update(@PathVariable("id") Long id, @ModelAttribute NotificationSchedule notificationSchedule,
            RedirectAttributes redirectAttrs, BindingResult errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute(HOUR_LIST, eventnotificationUtil.getAllHour());
            model.addAttribute(MINUTE_LIST, eventnotificationUtil.getAllMinute());
            List<String> repeatList = new ArrayList<>();
            for (SchedulerRepeat schedulerRepeat : SchedulerRepeat.values())
                repeatList.add(schedulerRepeat.getName());

            model.addAttribute(SCHEDULER_REPEAT_LIST, repeatList);
            model.addAttribute(MODE, MODE_VIEW);
            model.addAttribute(Constants.MESSAGE,
                    messageSource.getMessage(MSG_SCHEDULED_UPDATE_ERROR, null, Locale.ENGLISH));

            return SCHEDULE_UPDATE_VIEW;
        }
        User user = userService.getCurrentUser();
        notificationSchedule.setId(id);
        notificationSchedule.setStartDate(notificationSchedule.getEventDetails().getStartDt().getTime());
        notificationSchedule.setStartTime(
                notificationSchedule.getEventDetails().getStartHH() + ":" + notificationSchedule.getEventDetails().getStartMM());
        notificationSchedule.setStatus(SCHEDULED_STATUS);

        scheduleService.update(notificationSchedule);

        schedulerManager.updateJob(notificationSchedule, user);

        redirectAttrs.addFlashAttribute(NOTIFICATION_SCHEDULE, notificationSchedule);
        model.addAttribute(MESSAGE,
                messageSource.getMessage(MSG_SCHEDULED_UPDATE_SUCCESS, null, Locale.ENGLISH));
        return SCHEDULE_UPDATE_SUCCESS;
    }
}
