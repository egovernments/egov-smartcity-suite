/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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

import static org.egov.eventnotification.constants.Constants.DRAFT_LIST;
import static org.egov.eventnotification.constants.Constants.EVENT_NOTIFICATION_GROUP;
import static org.egov.eventnotification.constants.Constants.HOUR_LIST;
import static org.egov.eventnotification.constants.Constants.JOB;
import static org.egov.eventnotification.constants.Constants.MESSAGE;
import static org.egov.eventnotification.constants.Constants.MINUTE_LIST;
import static org.egov.eventnotification.constants.Constants.MODE;
import static org.egov.eventnotification.constants.Constants.MODE_DELETE;
import static org.egov.eventnotification.constants.Constants.MODE_VIEW;
import static org.egov.eventnotification.constants.Constants.NOTIFICATION_SCHEDULE;
import static org.egov.eventnotification.constants.Constants.SCHEDULEID;
import static org.egov.eventnotification.constants.Constants.SCHEDULER_REPEAT_LIST;
import static org.egov.eventnotification.constants.Constants.SCHEDULE_CREATE_SUCCESS;
import static org.egov.eventnotification.constants.Constants.SCHEDULE_CREATE_VIEW;
import static org.egov.eventnotification.constants.Constants.SCHEDULE_DELETE_SUCCESS;
import static org.egov.eventnotification.constants.Constants.SCHEDULE_DETAILS_VIEW;
import static org.egov.eventnotification.constants.Constants.SCHEDULE_DISABLED;
import static org.egov.eventnotification.constants.Constants.SCHEDULE_EDITABLE;
import static org.egov.eventnotification.constants.Constants.SCHEDULE_LIST;
import static org.egov.eventnotification.constants.Constants.TO_BE_SCHEDULED;
import static org.egov.eventnotification.constants.Constants.TRIGGER;

import java.util.Date;

import javax.validation.Valid;

import org.egov.eventnotification.constants.Constants;
import org.egov.eventnotification.entity.Drafts;
import org.egov.eventnotification.entity.Schedule;
import org.egov.eventnotification.service.DraftService;
import org.egov.eventnotification.service.DraftTypeService;
import org.egov.eventnotification.service.ScheduleRepeatService;
import org.egov.eventnotification.service.ScheduleService;
import org.egov.eventnotification.utils.EventnotificationUtil;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
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
        model.addAttribute(SCHEDULE_LIST,
                scheduleService.getAllSchedule());
        return Constants.SCHEDULE_VIEW;
    }

    @GetMapping("/schedule/create/{id}")
    public String save(@PathVariable("id") Long id, @ModelAttribute Schedule schedule, Model model) {
        Drafts notificationDrafts = draftService.getDraftById(id);
        schedule.setStatus(TO_BE_SCHEDULED);
        schedule.setMessageTemplate(notificationDrafts.getMessage());
        schedule.setTemplateName(notificationDrafts.getName());
        schedule.setDraftType(notificationDrafts.getDraftType());

        model.addAttribute(NOTIFICATION_SCHEDULE, schedule);
        model.addAttribute(HOUR_LIST, eventnotificationUtil.getAllHour());
        model.addAttribute(MINUTE_LIST, eventnotificationUtil.getAllMinute());
        model.addAttribute(DRAFT_LIST, draftTypeService.getAllDraftType());

        model.addAttribute(SCHEDULER_REPEAT_LIST, scheduleRepeatService.getAllScheduleRepeat());

        return SCHEDULE_CREATE_VIEW;
    }

    @PostMapping("/schedule/create/")
    public String save(@Valid @ModelAttribute Schedule schedule, BindingResult errors, Model model) {

        if (errors.hasErrors()) {
            model.addAttribute(NOTIFICATION_SCHEDULE, schedule);
            model.addAttribute(HOUR_LIST, eventnotificationUtil.getAllHour());
            model.addAttribute(MINUTE_LIST, eventnotificationUtil.getAllMinute());
            model.addAttribute(SCHEDULER_REPEAT_LIST, scheduleRepeatService.getAllScheduleRepeat());
            model.addAttribute(MESSAGE, "msg.notification.schedule.error");
            return SCHEDULE_CREATE_VIEW;
        }

        User user = userService.getCurrentUser();
        scheduleService.saveSchedule(schedule, user);

        String cronExpression = scheduleService.getCronExpression(schedule);
        final JobDetailImpl jobDetail = (JobDetailImpl) beanProvider.getBean("eventnotificationJobDetail");
        final Scheduler scheduler = (Scheduler) beanProvider.getBean("eventnotificationScheduler");
        try {
            jobDetail.setName(JOB.concat(String.valueOf(schedule.getId())));
            jobDetail.getJobDataMap().put(SCHEDULEID, String.valueOf(schedule.getId()));
            jobDetail.getJobDataMap().put("userid", String.valueOf(user.getId()));
            jobDetail.getJobDataMap().put("username", user.getName());

            if (cronExpression == null) {
                final SimpleTriggerImpl trigger = new SimpleTriggerImpl();
                trigger.setName(TRIGGER.concat(String.valueOf(schedule.getId())));
                trigger.setStartTime(new Date(System.currentTimeMillis() + 100000));
                trigger.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
                scheduler.start();
                scheduler.scheduleJob(jobDetail, trigger);
            } else {
                final Trigger trigger = TriggerBuilder.newTrigger()
                        .withIdentity(TRIGGER.concat(String.valueOf(schedule.getId())), EVENT_NOTIFICATION_GROUP)
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
        return SCHEDULE_CREATE_SUCCESS;
    }

    @GetMapping("/schedule/view/{id}")
    public String viewBySchedule(@PathVariable("id") Long id, Model model) {
        Schedule notificationSchedule = scheduleService.getScheduleById(id);
        DateTime sd = new DateTime(notificationSchedule.getStartDate());

        if (sd.isBeforeNow())
            model.addAttribute(SCHEDULE_EDITABLE, false);
        else
            model.addAttribute(SCHEDULE_EDITABLE, true);
        model.addAttribute(NOTIFICATION_SCHEDULE, notificationSchedule);
        model.addAttribute(MODE, MODE_DELETE);
        return SCHEDULE_DETAILS_VIEW;
    }

    @GetMapping(path = { "/schedule/delete/{id}" }, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String deleteSchedule(@PathVariable Long id, Model model) {
        Schedule notificationSchedule = scheduleService.getScheduleById(id);
        notificationSchedule.setStatus(SCHEDULE_DISABLED);

        notificationSchedule = scheduleService.updateSchedule(notificationSchedule);
        final Scheduler scheduler = (Scheduler) beanProvider.getBean("eventnotificationScheduler");
        try {
            scheduler.unscheduleJob(new TriggerKey(TRIGGER + notificationSchedule.getId(), EVENT_NOTIFICATION_GROUP));
            scheduler.deleteJob(new JobKey(JOB.concat(String.valueOf(notificationSchedule.getId()))));
        } catch (final SchedulerException e) {
            throw new ApplicationRuntimeException(e.getMessage(), e);
        }
        model.addAttribute(MESSAGE, "msg.notification.schedule.delete.success");
        return SCHEDULE_DELETE_SUCCESS;
    }
}