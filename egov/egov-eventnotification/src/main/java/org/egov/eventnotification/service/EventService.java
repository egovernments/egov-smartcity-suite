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
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.eventnotification.constants.Constants;
import org.egov.eventnotification.entity.Event;
import org.egov.eventnotification.entity.EventDetails;
import org.egov.eventnotification.repository.EventRepository;
import org.egov.eventnotification.repository.EventRepositoryImpl;
import org.egov.eventnotification.utils.DateFormatUtil;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.pushbox.application.entity.MessageContent;
import org.egov.pushbox.application.service.PushNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * This is a service class. It is used for event related business logic like fetch the event using entity manager etc.
 * @author somvit
 *
 */
@Service
@Transactional(readOnly = true)
public class EventService {

    private static final Logger LOGGER = Logger.getLogger(EventService.class);

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private FileStoreService fileStoreService;

    @Autowired
    private PushNotificationService pushNotificationService;

    @Autowired
    private EventRepositoryImpl eventRepositoryImpl;

    @Autowired
    private DateFormatUtil dateFormatUtil;

    /**
     * Fetch all the event in descending order
     * @return List<Event>
     */
    public List<Event> findAllByStatus(String status) {
        List<Event> eventList = null;
        try {
            eventList = eventRepository.findByStatusOrderByIdDesc(status);
            if (!eventList.isEmpty())
                for (Event event : eventList) {
                    EventDetails eventDetails = new EventDetails();
                    Date sd = new Date(event.getStartDate());
                    eventDetails.setStartDt(dateFormatUtil.getDateInDDMMYYYY(sd));
                    Date ed = new Date(event.getEndDate());
                    eventDetails.setEndDt(dateFormatUtil.getDateInDDMMYYYY(ed));
                    event.setEventDetails(eventDetails);
                }
        } catch (ParseException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return eventList;
    }

    /**
     * Fetch all the event in descending order
     * @return List<Event>
     */
    public List<Event> findAllOngoingEvent(String status) {
        List<Event> eventList = null;
        try {
            Calendar calendar = Calendar.getInstance();
            Date startDate;
            Date endDate;
            startDate = calendar.getTime();
            calendar.add(Calendar.DAY_OF_MONTH, 7);
            endDate = calendar.getTime();
            eventList = eventRepository.getAllEventsByDate(dateFormatUtil.getDateInDDMMYYYY(startDate).getTime(),
                    dateFormatUtil.getDateInDDMMYYYY(endDate).getTime(), status);
            if (!eventList.isEmpty())
                for (Event event : eventList) {
                    EventDetails eventDetails = new EventDetails();
                    Date sd = new Date(event.getStartDate());
                    eventDetails.setStartDt(dateFormatUtil.getDateInDDMMYYYY(sd));
                    Date ed = new Date(event.getEndDate());
                    eventDetails.setEndDt(dateFormatUtil.getDateInDDMMYYYY(ed));
                    event.setEventDetails(eventDetails);
                }
        } catch (ParseException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return eventList;
    }

    /**
     * This method fetch the event by id
     * @param id
     * @return Event
     */
    public Event findByEventId(Long id) {
        Event event = eventRepository.findOne(id);
        try {
            EventDetails eventDetails = new EventDetails();
            Date sd = new Date(event.getStartDate());
            eventDetails.setStartDt(dateFormatUtil.getDateInDDMMYYYY(sd));
            Date ed = new Date(event.getEndDate());
            eventDetails.setEndDt(dateFormatUtil.getDateInDDMMYYYY(ed));

            String[] st = event.getStartTime().split(":");
            eventDetails.setStartHH(st[0]);
            eventDetails.setStartMM(st[1]);
            String[] et = event.getEndTime().split(":");
            eventDetails.setEndHH(et[0]);
            eventDetails.setEndMM(et[1]);
            event.setEventDetails(eventDetails);
        } catch (ParseException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return event;
    }

    /**
     * This method create the event.
     * @param event
     * @return Event
     * @throws IOException
     * @throws ParseException
     */
    @Transactional
    public Event save(Event event) throws IOException {
        event.setStartDate(event.getEventDetails().getStartDt().getTime());
        event.setEndDate(event.getEventDetails().getEndDt().getTime());
        event.setStartTime(event.getEventDetails().getStartHH() + ":" + event.getEventDetails().getStartMM());
        event.setEndTime(event.getEventDetails().getEndHH() + ":" + event.getEventDetails().getEndMM());
        event.setStatus(Constants.ACTIVE.toUpperCase());

        if (event.getEventDetails().getFile() != null)
            eventUploadWallpaper(event);

        return eventRepository.save(event);
    }

    /**
     * This method is used to update the event
     * @param event
     * @param files
     * @return Event
     * @throws IOException
     * @throws ParseException
     */
    @Transactional
    public Event update(Event updatedEvent) throws IOException {
        if (updatedEvent.getEventDetails().getFile()[0].getSize() > 1)
            eventUploadWallpaper(updatedEvent);
        return eventRepository.save(updatedEvent);
    }

    /**
     * This method is used to search the event.
     * @param eventType
     * @param eventName
     * @param eventHost
     * @return List<Event>
     */
    public List<Event> searchEvent(Event eventObj, String eventDateType) {
        return eventRepositoryImpl.searchEvent(eventObj, eventDateType);
    }

    /**
     * This method is used to upload the file into filestore
     * @param event
     * @throws IOException
     */
    public void eventUploadWallpaper(Event event) throws IOException {

        for (MultipartFile multipartFile : event.getEventDetails().getFile())
            if (!multipartFile.isEmpty())
                event.setFilestore(
                        fileStoreService.store(multipartFile.getInputStream(), multipartFile.getOriginalFilename(),
                                multipartFile.getContentType(), Constants.MODULE_NAME));

    }

    /**
     * This method is used to upload the file into filestore
     * @param event
     * @param existingEvent
     * @throws IOException
     */
    public void eventUploadWallpaper(Event existingEvent, Event event) throws IOException {

        for (MultipartFile multipartFile : event.getEventDetails().getFile())
            if (!multipartFile.isEmpty())
                existingEvent.setFilestore(
                        fileStoreService.store(multipartFile.getInputStream(), multipartFile.getOriginalFilename(),
                                multipartFile.getContentType(), Constants.MODULE_NAME));

    }

    /**
     * Remove the file into filestore
     * @param event
     * @throws IOException
     */
    public void removeEventWallpaper(Event event) throws IOException {

        fileStoreService.delete(String.valueOf(event.getFilestore()), Constants.MODULE_NAME);

    }

    /**
     * Create a push message and sent it to pushbox
     * @param event
     * @param user
     */
    public void sendPushMessage(Event event, User user) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(event.getStartDate()));
        int hours = Integer.parseInt(event.getStartTime().split(":")[0]);
        int minutes = Integer.parseInt(event.getStartTime().split(":")[1]);
        calendar.set(Calendar.HOUR, hours);
        calendar.set(Calendar.MINUTE, minutes);

        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTime(new Date(event.getEndDate()));
        int hoursEnd = Integer.parseInt(event.getEndTime().split(":")[0]);
        int minutesEnd = Integer.parseInt(event.getEndTime().split(":")[1]);
        calendarEnd.set(Calendar.HOUR, hoursEnd);
        calendarEnd.set(Calendar.MINUTE, minutesEnd);

        MessageContent messageContent = new MessageContent();
        messageContent.setCreatedDateTime(new Date().getTime());
        messageContent.setEventAddress(event.getAddress());
        messageContent.setEventDateTime(calendar.getTimeInMillis());
        messageContent.setEventLocation(event.getEventlocation());
        messageContent.setExpiryDate(calendarEnd.getTimeInMillis());
        if (event.getFilestore() == null)
            messageContent.setImageUrl("");
        else
            messageContent.setImageUrl(String.valueOf(event.getFilestore()));
        messageContent.setMessageBody(event.getMessage());
        messageContent.setMessageId(event.getId());
        messageContent.setModuleName(event.getName());
        messageContent.setNotificationDateTime(new Date().getTime());
        messageContent.setNotificationType(Constants.NOTIFICATION_TYPE_EVENT);
        messageContent.setSenderId(user.getId());
        messageContent.setSenderName(user.getName());
        messageContent.setSendAll(Boolean.TRUE);
        if (event.getUrl() == null)
            messageContent.setUrl("");
        else
            messageContent.setUrl(event.getUrl());

        pushNotificationService.sendNotifications(messageContent);
    }
}
