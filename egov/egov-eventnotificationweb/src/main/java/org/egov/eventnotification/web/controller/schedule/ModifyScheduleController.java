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

import static org.egov.eventnotification.constants.EventNotificationConstants.*;
import java.util.Date;

import javax.validation.Valid;

import org.egov.eventnotification.entity.Schedule;
import org.egov.eventnotification.service.DraftTypeService;
import org.egov.eventnotification.service.ScheduleRepeatService;
import org.egov.eventnotification.service.ScheduleService;
import org.egov.eventnotification.utils.EventnotificationUtil;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.joda.time.DateTime;
import org.quartz.CronScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ModifyScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private EventnotificationUtil eventnotificationUtil;

    @Autowired
    private ScheduleRepeatService scheduleRepeatService;

    @Autowired
    private DraftTypeService draftTypeService;

    @Autowired
    private ApplicationContext beanProvider;

    @ModelAttribute("schedule")
    public Schedule getNotificationSchedule(@PathVariable Long id) {
        return scheduleService.getScheduleById(id);
    }

    @GetMapping("/schedule/update/{id}")
    public String update(@ModelAttribute Schedule schedule, Model model) {
        model.addAttribute(HOUR_LIST, eventnotificationUtil.getAllHour());
        model.addAttribute(MINUTE_LIST, eventnotificationUtil.getAllMinute());
        model.addAttribute(SCHEDULER_REPEAT_LIST, scheduleRepeatService.getAllScheduleRepeat());
        model.addAttribute(MODE, MODE_VIEW);
        model.addAttribute(DRAFT_LIST, draftTypeService.getAllDraftType());

        return SCHEDULE_UPDATE_VIEW;
    }

    @PostMapping("/schedule/update/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute Schedule schedule,
            BindingResult errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute(HOUR_LIST, eventnotificationUtil.getAllHour());
            model.addAttribute(MINUTE_LIST, eventnotificationUtil.getAllMinute());
            model.addAttribute(SCHEDULER_REPEAT_LIST, scheduleRepeatService.getAllScheduleRepeat());
            model.addAttribute(MODE, MODE_VIEW);
            model.addAttribute(MESSAGE, "msg.notification.schedule.update.error");

            return SCHEDULE_UPDATE_VIEW;
        }
        schedule.setId(id);
        DateTime sd = new DateTime(schedule.getEventDetails().getStartDt());
        sd = sd.withHourOfDay(Integer.parseInt(schedule.getEventDetails().getStartHH()));
        sd = sd.withMinuteOfHour(Integer.parseInt(schedule.getEventDetails().getStartMM()));
        sd = sd.withSecondOfMinute(00);
        schedule.setStartDate(sd.toDate());
        schedule.setStatus(SCHEDULED_STATUS);

        scheduleService.updateSchedule(schedule);

        String cronExpression = scheduleService.getCronExpression(schedule);
        final Scheduler scheduler = (Scheduler) beanProvider.getBean("eventnotificationScheduler");
        try {

            if (cronExpression == null) {
                TriggerKey triggerKey = new TriggerKey(ApplicationThreadLocals.getTenantID().concat("_")
                        .concat(TRIGGER.concat(String.valueOf(schedule.getId()))),
                        EVENT_NOTIFICATION_GROUP);
                final SimpleTriggerImpl trigger = new SimpleTriggerImpl();
                trigger.setName(TRIGGER.concat(String.valueOf(schedule.getId())));
                trigger.setStartTime(new Date(System.currentTimeMillis() + 100000));
                trigger.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
                scheduler.rescheduleJob(triggerKey, trigger);
                if (!scheduler.isShutdown())
                    scheduler.start();
            } else {
                TriggerKey triggerKey = new TriggerKey(ApplicationThreadLocals.getTenantID().concat("_")
                        .concat(TRIGGER.concat(String.valueOf(schedule.getId()))),
                        EVENT_NOTIFICATION_GROUP);
                final Trigger trigger = TriggerBuilder.newTrigger()
                        .withIdentity(TRIGGER.concat(String.valueOf(schedule.getId())),
                                EVENT_NOTIFICATION_GROUP)
                        .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build();
                scheduler.rescheduleJob(triggerKey, trigger);
                if (!scheduler.isShutdown())
                    scheduler.start();
            }

        } catch (final SchedulerException e) {
            throw new ApplicationRuntimeException(e.getMessage(), e);
        }

        model.addAttribute(NOTIFICATION_SCHEDULE, schedule);
        model.addAttribute(MESSAGE, "msg.notification.schedule.update.success");
        return SCHEDULE_UPDATE_SUCCESS;
    }
}