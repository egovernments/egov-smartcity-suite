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
package org.egov.eventnotification.service;

import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.egov.eventnotification.utils.Constants.ACTIVE;
import static org.egov.eventnotification.utils.Constants.MODULE_NAME;
import static org.egov.infra.utils.DateUtils.endOfToday;
import static org.egov.infra.utils.DateUtils.startOfGivenDate;
import static org.egov.infra.utils.DateUtils.startOfToday;
import static org.egov.infra.utils.DateUtils.today;

import java.util.Date;
import java.util.List;

import org.egov.eventnotification.entity.Event;
import org.egov.eventnotification.entity.contracts.EventSearch;
import org.egov.eventnotification.repository.EventRepository;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.pushbox.entity.contracts.MessageContent;
import org.egov.pushbox.entity.contracts.MessageContentDetails;
import org.egov.pushbox.service.PushNotificationService;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author somvit
 *
 */
@Service
@Transactional(readOnly = true)
public class EventService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventService.class);

    private static final int MIN_NUMBER_OF_REQUESTS = 1;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private FileStoreService fileStoreService;

    @Autowired
    private PushNotificationService pushNotificationService;

    @Autowired
    private UserService userService;

    @Autowired
    private EventDetailsService eventDetailsService;

    public List<Event> getAllEventByStatus(String status) {
        List<Event> eventList = null;
        eventList = eventRepository.findByStatusAndStartDateGreaterThanOrderByIdDesc(status, today());
        if (!eventList.isEmpty())
            for (Event event : eventList)
                eventDetailsService.populateEventDetails(event);
        return eventList;
    }

    public List<Event> getAllOngoingEvent(String status) {
        List<Event> eventList = null;
        Date startDate;
        Date endDate;
        startDate = startOfToday().toDate();
        endDate = endOfToday().plusDays(6).toDate();
        eventList = eventRepository.findByStatusAndStartDateIsBetweenOrderByIdDesc(status,
                startDate, endDate);
        if (!eventList.isEmpty())
            for (Event event : eventList)
                eventDetailsService.populateEventDetails(event);
        return eventList;
    }

    public Event getEventById(Long id) {
        Event event = eventRepository.findOne(id);
        eventDetailsService.populateEventDetails(event);
        return event;
    }

    @Transactional
    public Event saveEvent(Event event) {
        try {
            DateTime sd = startOfGivenDate(new DateTime(event.getDetails().getStartDt()))
                    .withHourOfDay(Integer.parseInt(event.getDetails().getStartHH()))
                    .withMinuteOfHour(Integer.parseInt(event.getDetails().getStartMM()));
            event.setStartDate(sd.toDate());

            DateTime ed = startOfGivenDate(new DateTime(event.getDetails().getEndDt()))
                    .withHourOfDay(Integer.parseInt(event.getDetails().getEndHH()))
                    .withMinuteOfHour(Integer.parseInt(event.getDetails().getEndMM()));
            event.setEndDate(ed.toDate());
            event.setStatus(ACTIVE.toUpperCase());

            if (event.getDetails().getFile() != null)
                eventDetailsService.eventUploadWallpaper(event);

            eventRepository.saveAndFlush(event);
            sendPushMessage(event);

            return event;
        } catch (final Exception e) {
            LOGGER.error("Error : Encountered an exception while save an event", e);
            throw new ApplicationRuntimeException(e.getMessage(), e);
        }
    }

    @Transactional
    public Event updateEvent(Event updatedEvent) {
        try {
            DateTime sd = startOfGivenDate(new DateTime(updatedEvent.getDetails().getStartDt()))
                    .withHourOfDay(Integer.parseInt(updatedEvent.getDetails().getStartHH()))
                    .withMinuteOfHour(Integer.parseInt(updatedEvent.getDetails().getStartMM()));
            updatedEvent.setStartDate(sd.toDate());

            DateTime ed = startOfGivenDate(new DateTime(updatedEvent.getDetails().getEndDt()))
                    .withHourOfDay(Integer.parseInt(updatedEvent.getDetails().getEndHH()))
                    .withMinuteOfHour(Integer.parseInt(updatedEvent.getDetails().getEndMM()));
            updatedEvent.setEndDate(ed.toDate());

            if (updatedEvent.getDetails().getFile()[0].getSize() > MIN_NUMBER_OF_REQUESTS)
                eventDetailsService.eventUploadWallpaper(updatedEvent);
            return eventRepository.save(updatedEvent);
        } catch (final Exception e) {
            LOGGER.error("Error : Encountered an exception while update an event", e);
            throw new ApplicationRuntimeException(e.getMessage(), e);
        }
    }

    public List<Event> searchEvent(EventSearch eventSearch) {
        return eventRepository.searchEvent(eventSearch);
    }

    public void removeEventWallpaper(Event event) {
        fileStoreService.delete(String.valueOf(event.getFilestore()), MODULE_NAME);
    }

    private void sendPushMessage(Event event) {
        User user = userService.getCurrentUser();
        DateTime calendar = new DateTime(event.getStartDate());
        DateTime calendarEnd = new DateTime(event.getEndDate());

        MessageContent messageContent = new MessageContent();
        MessageContentDetails messageDetails = new MessageContentDetails();
        messageContent.setCreatedDateTime(new Date().getTime());
        messageDetails.setEventAddress(event.getAddress().getAddress());
        messageDetails.setEventDateTime(calendar.getMillis());
        messageDetails.setEventLocation(event.getAddress().getEventLocation());
        messageContent.setExpiryDate(calendarEnd.getMillis());
        if (event.getFilestore() == null)
            messageContent.setImageUrl(EMPTY);
        else
            messageContent.setImageUrl(String.valueOf(event.getFilestore().getFileStoreId()));
        messageContent.setMessageBody(event.getMessage());
        messageContent.setMessageId(event.getId());
        messageContent.setModuleName(event.getName());
        messageContent.setNotificationDateTime(new Date().getTime());
        messageContent.setNotificationType("Event");
        messageContent.setSenderId(user.getId());
        messageContent.setSenderName(user.getName());
        messageDetails.setSendAll(Boolean.TRUE);
        if (event.getAddress().getUrl() == null)
            messageContent.setUrl(EMPTY);
        else
            messageContent.setUrl(event.getAddress().getUrl());

        messageContent.setCityName(ApplicationThreadLocals.getCityName());
        messageContent.setUlbCode(ApplicationThreadLocals.getCityCode());
        messageContent.setDetails(messageDetails);
        
        pushNotificationService.sendNotifications(messageContent);
    }
}