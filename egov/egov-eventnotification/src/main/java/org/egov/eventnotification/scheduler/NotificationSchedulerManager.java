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
package org.egov.eventnotification.scheduler;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.eventnotification.constants.Constants;
import org.egov.eventnotification.entity.NotificationSchedule;
import org.egov.eventnotification.utils.SchedulerUtil;
import org.egov.infra.admin.master.entity.User;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.entity.property.DefaultersInfo;
import org.egov.ptis.domain.service.report.ReportService;
import org.hibernate.Query;
import org.quartz.JobDataMap;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * this api will contains all scheduler , that will run for different-2 purpose.
 * @author somvit
 *
 */

@Service
public class NotificationSchedulerManager {

    private static final Logger LOGGER = Logger.getLogger(NotificationSchedulerManager.class);

    @Autowired
    private SchedulerUtil schedulerUtil;

    @Autowired
    private PropertyTaxUtil propertyTaxUtil;

    @Autowired
    private ReportService reportService;

    /**
     * all scheduler job will be configure here.
     */
    public void schedule(NotificationSchedule notificationschedule, User user) {

        Query query = propertyTaxUtil.prepareQueryforDefaultersReport(-1l, "1", "100000", 10000, "PRIVATE", "PT");
        List<DefaultersInfo> defaultersList = reportService.getDefaultersInformation(query, "1 Year", 10);

        schedulerUtil.setGroupName(Constants.NOTIFICATION_JOB);
        schedulerUtil.setName(Constants.JOB + notificationschedule.getId());
        schedulerUtil.setTriggerName(Constants.TRIGGER + notificationschedule.getId());
        schedulerUtil.setRepeatCount(0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(notificationschedule.getStartDate()));
        int hours = Integer.parseInt(notificationschedule.getStartTime().split(":")[0]);
        int minutes = Integer.parseInt(notificationschedule.getStartTime().split(":")[1]);

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(Constants.USER, user);
        jobDataMap.put(Constants.SCHEDULEID, notificationschedule.getId());
        jobDataMap.put(Constants.DEFAULTERS_LIST, defaultersList);
        // configure the scheduler time
        switch (notificationschedule.getRepeat().toLowerCase()) {
        case Constants.SCHEDULE_DAY:
            String dailyCronExpression = "0 " + minutes + " " + hours + " ? * * *";

            try {
                jobDataMap.put(Constants.JOB_TYPE, Constants.DAILY_JOB_TYPE);
                schedulerUtil.addSchedule(NotificationSchedulerJob.class, dailyCronExpression, jobDataMap);
            } catch (SchedulerException e) {
                LOGGER.error(e.getMessage(), e);
            }

            break;
        case Constants.SCHEDULE_MONTH:
            String monthlyCronExpression = "0 " + minutes + " " + hours + " " + calendar.get(Calendar.DAY_OF_MONTH) + " * ? *";

            try {
                jobDataMap.put(Constants.JOB_TYPE, Constants.MONTHLY_JOB_TYPE);
                schedulerUtil.addSchedule(NotificationSchedulerJob.class, monthlyCronExpression, jobDataMap);
            } catch (SchedulerException e) {
                LOGGER.error(e.getMessage(), e);
            }

            break;
        case Constants.SCHEDULE_YEAR:
            String yearlyCronExpression = "0 " + minutes + " " + hours + " "
                    + calendar.get(Calendar.DAY_OF_MONTH) + " " + (calendar.get(Calendar.MONTH) + 1) + " ? *";

            try {
                jobDataMap.put(Constants.JOB_TYPE, Constants.YEARLY_JOB_TYPE);
                schedulerUtil.addSchedule(NotificationSchedulerJob.class, yearlyCronExpression, jobDataMap);
            } catch (SchedulerException e) {
                LOGGER.error(e.getMessage(), e);
            }
            break;
        default:
            calendar.set(Calendar.HOUR_OF_DAY, hours);
            calendar.set(Calendar.MINUTE, minutes);
            try {
                jobDataMap.put(Constants.JOB_TYPE, Constants.ONETIME_JOB_TYPE);
                schedulerUtil.addSchedule(calendar.getTime(), NotificationSchedulerJob.class, jobDataMap);
            } catch (SchedulerException e) {
                LOGGER.error(e.getMessage(), e);
            }
            break;
        }
    }

    /**
     * Delete the existing job.
     */
    public void removeJob(NotificationSchedule notificationschedule) {
        try {
            schedulerUtil.setGroupName(Constants.NOTIFICATION_JOB);
            schedulerUtil.setName(Constants.JOB + notificationschedule.getId());
            schedulerUtil.setTriggerName(Constants.TRIGGER + notificationschedule.getId());
            schedulerUtil.removeSchedule();

            LOGGER.info("Schedule Job with name " + schedulerUtil.getName() + " is deleted");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * Modify the existing job.
     */
    public void updateJob(NotificationSchedule newSchedule, User user) {
        String scheduleTrigger = Constants.TRIGGER + newSchedule.getId();

        Calendar newTime = Calendar.getInstance();
        newTime.setTime(new Date(newSchedule.getStartDate()));
        int newhours = Integer.parseInt(newSchedule.getStartTime().split(":")[0]);
        int newminutes = Integer.parseInt(newSchedule.getStartTime().split(":")[1]);

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(Constants.USER, user);
        jobDataMap.put(Constants.SCHEDULEID, newSchedule.getId());

        Query query = propertyTaxUtil.prepareQueryforDefaultersReport(-1l, "1", "100000", 10000, "PRIVATE", "PT");
        List<DefaultersInfo> defaultersList = reportService.getDefaultersInformation(query, "1 Year", 10);
        jobDataMap.put(Constants.DEFAULTERS_LIST, defaultersList);
        // configure the scheduler time
        switch (newSchedule.getRepeat().toLowerCase()) {
        case Constants.SCHEDULE_DAY:
            String dailyCronExpression = "0 " + newminutes + " " + newhours + " ? * * *";

            try {
                jobDataMap.put(Constants.JOB_TYPE, Constants.DAILY_JOB_TYPE);
                TriggerKey triggerKey = new TriggerKey(scheduleTrigger, Constants.NOTIFICATION_JOB);
                schedulerUtil.updateSchedule(NotificationSchedulerJob.class, dailyCronExpression, jobDataMap, triggerKey);
            } catch (SchedulerException e) {
                LOGGER.error(e.getMessage(), e);
            }
            break;
        case Constants.SCHEDULE_MONTH:
            String monthlyCronExpression = "0 " + newminutes + " " + newhours + " " + newTime.get(Calendar.DAY_OF_MONTH)
                    + " * ? *";

            try {
                jobDataMap.put(Constants.JOB_TYPE, Constants.MONTHLY_JOB_TYPE);
                TriggerKey triggerKey = new TriggerKey(scheduleTrigger, Constants.NOTIFICATION_JOB);
                schedulerUtil.updateSchedule(NotificationSchedulerJob.class, monthlyCronExpression, jobDataMap,
                        triggerKey);
            } catch (SchedulerException e) {
                LOGGER.error(e.getMessage(), e);
            }
            break;
        case Constants.SCHEDULE_YEAR:
            String yearlyCronExpression = "0 " + newminutes + " " + newhours + " "
                    + newTime.get(Calendar.DAY_OF_MONTH) + " " + (newTime.get(Calendar.MONTH) + 1) + " ? *";

            try {
                jobDataMap.put(Constants.JOB_TYPE, Constants.YEARLY_JOB_TYPE);
                TriggerKey triggerKey = new TriggerKey(scheduleTrigger, Constants.NOTIFICATION_JOB);
                schedulerUtil.updateSchedule(NotificationSchedulerJob.class, yearlyCronExpression, jobDataMap, triggerKey);
            } catch (SchedulerException e) {
                LOGGER.error(e.getMessage(), e);
            }
            break;
        default:
            newTime.set(Calendar.HOUR_OF_DAY, newhours);
            newTime.set(Calendar.MINUTE, newminutes);

            try {
                jobDataMap.put(Constants.JOB_TYPE, Constants.ONETIME_JOB_TYPE);
                TriggerKey triggerKey = new TriggerKey(scheduleTrigger, Constants.NOTIFICATION_JOB);
                schedulerUtil.updateSchedule(newTime.getTime(), NotificationSchedulerJob.class, jobDataMap, triggerKey);
            } catch (SchedulerException e) {
                LOGGER.error(e.getMessage(), e);
            }
            break;
        }
    }
}
