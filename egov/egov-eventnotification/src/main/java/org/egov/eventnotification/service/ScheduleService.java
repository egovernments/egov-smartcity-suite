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
package org.egov.eventnotification.service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.eventnotification.constants.EventnotificationConstant;
import org.egov.eventnotification.entity.EventDetails;
import org.egov.eventnotification.entity.NotificationSchedule;
import org.egov.eventnotification.repository.NotificationScheduleRepository;
import org.egov.infra.admin.master.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ScheduleService {

    private static final Logger LOGGER = Logger.getLogger(ScheduleService.class);

    private final NotificationScheduleRepository notificationscheduleRepository;

    @Autowired
    public ScheduleService(final NotificationScheduleRepository notificationscheduleRepository) {
        this.notificationscheduleRepository = notificationscheduleRepository;

    }

    /**
     * Fetch all the schedule by status
     * @return List<Notificationschedule>
     */
    public List<NotificationSchedule> findAllSchedule(String status) {
        DateFormat formatter = new SimpleDateFormat(EventnotificationConstant.DDMMYYYY);
        List<NotificationSchedule> notificationScheduleList = null;
        try {
            notificationScheduleList = notificationscheduleRepository.getAllNotificationScheduleByStatus(status);
            if (null != notificationScheduleList)
                for (NotificationSchedule notificationSchedule : notificationScheduleList) {
                    EventDetails eventDetails = new EventDetails();
                    Date sd = new Date(notificationSchedule.getStartDate());
                    eventDetails.setStartDt(formatter.parse(formatter.format(sd)));
                    notificationSchedule.setEventDetails(eventDetails);
                }
        } catch (ParseException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return notificationScheduleList;
    }

    /**
     * Fetch schedule by id
     * @return Notificationschedule
     */
    public NotificationSchedule findOne(Long id) {
        return notificationscheduleRepository.findOne(id);
    }

    /**
     * This method create the event.
     * @param event
     * @param files
     * @return Event
     * @throws IOException
     * @throws ParseException
     */
    @Transactional
    public NotificationSchedule persist(NotificationSchedule notificationSchedule)
            throws IOException, ParseException {
        return notificationscheduleRepository.save(notificationSchedule);
    }

    /**
     * Soft delete of schedule notification
     * @param id
     * @param user
     * @return Notificationschedule
     */
    @Transactional
    public NotificationSchedule updateSchedule(Long id, User user) {
        NotificationSchedule notificationSchedule = notificationscheduleRepository.findOne(id);
        if (null != notificationSchedule) {
            notificationSchedule.setStatus(EventnotificationConstant.SCHEDULE_DELETE);
            notificationSchedule.setUpdatedby(user);
            notificationSchedule.setUpdatedDate(new Date().getTime());
        }
        return notificationSchedule;
    }

    /**
     * Soft delete of schedule notification
     * @param id
     * @param user
     * @return Notificationschedule
     */
    @Transactional
    public NotificationSchedule updateScheduleDetails(NotificationSchedule schedule, User user) {
        NotificationSchedule notificationSchedule = notificationscheduleRepository.findOne(schedule.getId());
        if (null != notificationSchedule) {
            notificationSchedule.setMessageTemplate(schedule.getMessageTemplate());
            notificationSchedule.setNotificationType(schedule.getNotificationType());
            notificationSchedule.setRepeat(schedule.getRepeat());
            notificationSchedule.setStartDate(schedule.getStartDate());
            notificationSchedule.setStartTime(schedule.getStartTime());
            notificationSchedule.setTemplatename(schedule.getTemplatename());
            notificationSchedule.setStatus(EventnotificationConstant.SCHEDULED_STATUS);
            notificationSchedule.setUpdatedby(user);
            notificationSchedule.setUpdatedDate(new Date().getTime());
        }
        return notificationSchedule;
    }
}
