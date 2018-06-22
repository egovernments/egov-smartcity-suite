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
package org.egov.eventnotification.web.controller.schedule;

import static org.egov.eventnotification.constants.ConstantsHelper.DRAFT_LIST;
import static org.egov.eventnotification.constants.ConstantsHelper.HOUR_LIST;
import static org.egov.eventnotification.constants.ConstantsHelper.MINUTE_LIST;
import static org.egov.eventnotification.constants.ConstantsHelper.MODE;
import static org.egov.eventnotification.constants.ConstantsHelper.MODE_DELETE;
import static org.egov.eventnotification.constants.ConstantsHelper.MODE_VIEW;
import static org.egov.eventnotification.constants.ConstantsHelper.NOTIFICATION_SCHEDULE;
import static org.egov.eventnotification.constants.ConstantsHelper.SCHEDULER_REPEAT_LIST;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.egov.eventnotification.entity.Drafts;
import org.egov.eventnotification.entity.Schedule;
import org.egov.eventnotification.service.DraftService;
import org.egov.eventnotification.service.DraftTypeService;
import org.egov.eventnotification.service.ScheduleRepeatService;
import org.egov.eventnotification.service.ScheduleService;
import org.egov.eventnotification.utils.EventnotificationUtil;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.joda.time.DateTime;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ScheduleController {
    private static final String MESSAGE = "message";
    private static final String EVENT_NOTIFICATION_GROUP = "EVENT_NOTIFICATION_GROUP";
    private static final String TRIGGER = "eventNotificationTrigger";
    private static final String JOB = "eventNotificationJob";

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private DraftService draftService;

    @Autowired
    private EventnotificationUtil eventnotificationUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private ScheduleRepeatService scheduleRepeatService;

    @Autowired
    private DraftTypeService draftTypeService;

    @Autowired
    private ApplicationContext beanProvider;

    @GetMapping("/schedule/view/")
    public String view(Model model) {
        model.addAttribute(DRAFT_LIST, draftService.getAllDrafts());
        model.addAttribute("scheduleList",
                scheduleService.getAllSchedule());
        return "schedule-view";
    }

    @GetMapping("/schedule/create/{id}")
    public String save(@PathVariable Long id, @ModelAttribute Schedule schedule, Model model) {
        Drafts notificationDrafts = draftService.getDraftById(id);
        schedule.setStatus("To be Scheduled");
        schedule.setMessageTemplate(notificationDrafts.getMessage());
        schedule.setTemplateName(notificationDrafts.getName());
        schedule.setDraftType(notificationDrafts.getDraftType());
        schedule.setModule(notificationDrafts.getModule());

        model.addAttribute(NOTIFICATION_SCHEDULE, schedule);
        model.addAttribute(HOUR_LIST, eventnotificationUtil.getAllHour());
        model.addAttribute(MINUTE_LIST, eventnotificationUtil.getAllMinute());
        model.addAttribute(DRAFT_LIST, draftTypeService.getAllDraftType());

        model.addAttribute(SCHEDULER_REPEAT_LIST, scheduleRepeatService.getAllScheduleRepeat());

        return "schedule-create-view";
    }

    /**
     * This method save the new schedule data and create a new schedule job using dynamic cron expression and scheduloe a job.
     * @param schedule
     * @param errors
     * @param request
     * @param model
     * @return
     */
    @PostMapping("/schedule/create/")
    public String save(@Valid @ModelAttribute Schedule schedule, BindingResult errors, HttpServletRequest request, Model model) {

        if (errors.hasErrors()) {
            model.addAttribute(NOTIFICATION_SCHEDULE, schedule);
            model.addAttribute(HOUR_LIST, eventnotificationUtil.getAllHour());
            model.addAttribute(MINUTE_LIST, eventnotificationUtil.getAllMinute());
            model.addAttribute(SCHEDULER_REPEAT_LIST, scheduleRepeatService.getAllScheduleRepeat());
            model.addAttribute(MESSAGE, "msg.notification.schedule.error");
            return "schedule-create-view";
        }

        User user = userService.getCurrentUser();
        scheduleService.saveSchedule(schedule, user);

        String cronExpression = scheduleService.getCronExpression(schedule);
        final JobDetailImpl jobDetail = (JobDetailImpl) beanProvider.getBean("eventnotificationJobDetail");
        final Scheduler scheduler = (Scheduler) beanProvider.getBean("eventnotificationScheduler");
        try {
            jobDetail.setName(ApplicationThreadLocals.getTenantID().concat("_")
                    .concat(JOB.concat(String.valueOf(schedule.getId()))));
            jobDetail.getJobDataMap().put("scheduleId", String.valueOf(schedule.getId()));
            String fullURL = request.getRequestURL().toString();
            jobDetail.getJobDataMap().put("contextURL", fullURL.substring(0, StringUtils.ordinalIndexOf(fullURL, "/", 3)));

            if (cronExpression == null) {
                final SimpleTriggerImpl trigger = new SimpleTriggerImpl();
                trigger.setName(ApplicationThreadLocals.getTenantID().concat("_")
                        .concat(TRIGGER.concat(String.valueOf(schedule.getId()))));
                trigger.setStartTime(new Date(System.currentTimeMillis() + 100000));
                trigger.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
                scheduler.start();
                scheduler.scheduleJob(jobDetail, trigger);
            } else {
                final Trigger trigger = TriggerBuilder.newTrigger()
                        .withIdentity(ApplicationThreadLocals.getTenantID().concat("_")
                                .concat(TRIGGER.concat(String.valueOf(schedule.getId()))),
                                EVENT_NOTIFICATION_GROUP)
                        .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build();
                scheduler.start();
                scheduler.scheduleJob(jobDetail, trigger);
            }

        } catch (final SchedulerException e) {
            throw new ApplicationRuntimeException(e.getMessage(), e);
        }
        model.addAttribute(MESSAGE, "msg.notification.schedule.success");
        model.addAttribute(MODE, MODE_VIEW);
        model.addAttribute(NOTIFICATION_SCHEDULE, schedule);
        return "schedule-create-success";
    }

    @GetMapping("/schedule/view/{id}")
    public String viewBySchedule(@PathVariable Long id, Model model) {
        Schedule notificationSchedule = scheduleService.getScheduleById(id);
        DateTime sd = new DateTime(notificationSchedule.getStartDate());

        if (sd.isBeforeNow())
            model.addAttribute("scheduleEditable", false);
        else
            model.addAttribute("scheduleEditable", true);
        model.addAttribute(NOTIFICATION_SCHEDULE, notificationSchedule);
        model.addAttribute(MODE, MODE_DELETE);
        return "schedule-details-view";
    }

    /**
     * This method update the schedule status and unschedule a job. Once job is unschedule it cannot be resumed again.
     * @param id
     * @param model
     * @return
     */
    @GetMapping(path = { "/schedule/delete/{id}" }, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String deleteSchedule(@PathVariable Long id, Model model) {
        Schedule notificationSchedule = scheduleService.getScheduleById(id);
        notificationSchedule.setStatus("Disabled");

        notificationSchedule = scheduleService.updateSchedule(notificationSchedule);
        final Scheduler scheduler = (Scheduler) beanProvider.getBean("eventnotificationScheduler");
        try {
            scheduler.unscheduleJob(new TriggerKey(ApplicationThreadLocals.getTenantID().concat("_")
                    .concat(TRIGGER.concat(String.valueOf(notificationSchedule.getId()))),
                    EVENT_NOTIFICATION_GROUP));
            scheduler.deleteJob(new JobKey(JOB.concat(String.valueOf(notificationSchedule.getId()))));
        } catch (final SchedulerException e) {
            throw new ApplicationRuntimeException(e.getMessage(), e);
        }
        model.addAttribute(MESSAGE, "msg.notification.schedule.delete.success");
        return "success";
    }
}