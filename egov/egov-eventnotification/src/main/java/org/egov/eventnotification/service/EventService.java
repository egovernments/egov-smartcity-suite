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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.egov.eventnotification.constants.EventnotificationConstant;
import org.egov.eventnotification.entity.Event;
import org.egov.eventnotification.entity.EventDetails;
import org.egov.eventnotification.repository.EventRepository;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.pushbox.application.entity.MessageContent;
import org.egov.pushbox.application.service.PushNotificationService;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
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
    private final EventRepository eventRepository;

    @Autowired
    private FileStoreService fileStoreService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PushNotificationService pushNotificationService;

    @Autowired
    public EventService(final EventRepository eventRepository) {
        this.eventRepository = eventRepository;

    }

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    /**
     * Fetch all the event in descending order
     * @return List<Event>
     */
    public List<Event> findAll(String status) {
        DateFormat formatter = new SimpleDateFormat(EventnotificationConstant.DDMMYYYY);
        List<Event> eventList = null;
        try {
            eventList = eventRepository.getAllEvents(status);
            if (eventList != null)
                for (Event event : eventList) {
                    EventDetails eventDetails = new EventDetails();
                    Date sd = new Date(event.getStartDate());
                    eventDetails.setStartDt(formatter.parse(formatter.format(sd)));
                    Date ed = new Date(event.getEndDate());
                    eventDetails.setEndDt(formatter.parse(formatter.format(ed)));
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
        DateFormat formatter = new SimpleDateFormat(EventnotificationConstant.DDMMYYYY);
        List<Event> eventList = null;
        try {
            Calendar calendar = Calendar.getInstance();
            Date startDate;
            Date endDate;
            startDate = calendar.getTime();
            calendar.add(Calendar.DAY_OF_MONTH, 7);
            endDate = calendar.getTime();
            eventList = eventRepository.getAllEventsByDate(formatter.parse(formatter.format(startDate)).getTime(),
                    formatter.parse(formatter.format(endDate)).getTime(), status);
            if (eventList != null)
                for (Event event : eventList) {
                    EventDetails eventDetails = new EventDetails();
                    Date sd = new Date(event.getStartDate());
                    eventDetails.setStartDt(formatter.parse(formatter.format(sd)));
                    Date ed = new Date(event.getEndDate());
                    eventDetails.setEndDt(formatter.parse(formatter.format(ed)));
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
    public Event findById(Long id) {
        return eventRepository.findOne(id);
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
    public Event persist(Event event)
            throws IOException, ParseException {
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
    public Event update(Event event)
            throws IOException, ParseException {
        Event existingEvent = findById(event.getId());
        if (existingEvent != null) {
            existingEvent.setAddress(event.getAddress());
            existingEvent.setCost(event.getCost());
            existingEvent.setDescription(event.getDescription());
            existingEvent.setEndDate(event.getEndDate());
            existingEvent.setEndTime(event.getEndTime());
            existingEvent.setEventhost(event.getEventhost());
            existingEvent.setEventlocation(event.getEventlocation());
            existingEvent.setEventType(event.getEventType());
            existingEvent.setIspaid(event.getIspaid());
            existingEvent.setName(event.getName());
            existingEvent.setStartDate(event.getStartDate());
            existingEvent.setStartTime(event.getStartTime());
            existingEvent.setVersion(existingEvent.getVersion() + 1);
            existingEvent.setStatus(event.getStatus());
            existingEvent.setUrl(event.getUrl());
            existingEvent.setMessage(event.getMessage());
            if (event.getEventDetails().getFile() != null)
                if (existingEvent.getFilestore() != null) {
                    removeEventWallpaper(existingEvent);
                    eventUploadWallpaper(existingEvent, event);
                } else
                    eventUploadWallpaper(existingEvent, event);
        }

        return existingEvent;
    }

    /**
     * This method is used to search the event.
     * @param eventType
     * @param eventName
     * @param eventHost
     * @return List<Event>
     */
    public List<Event> searchEvent(Event eventObj, String eventDateType) {
        DateFormat formatter = new SimpleDateFormat(EventnotificationConstant.DDMMYYYY);
        Calendar calendar = Calendar.getInstance();
        Calendar calendarEndDate = Calendar.getInstance();
        Date startDate;
        Date endDate;
        if (eventDateType.equalsIgnoreCase(EventnotificationConstant.UPCOMING)) {
            calendar.add(Calendar.DAY_OF_MONTH, 7);
            startDate = calendar.getTime();
            calendarEndDate.setTime(startDate);
            calendarEndDate.add(Calendar.DAY_OF_MONTH, 7);
            endDate = calendarEndDate.getTime();
        } else {
            startDate = calendar.getTime();
            calendar.add(Calendar.DAY_OF_MONTH, 7);
            endDate = calendar.getTime();
        }

        Criteria criteria = getCurrentSession().createCriteria(Event.class);
        if (eventObj.getEventType() != null)
            criteria.add(Restrictions.ilike(EventnotificationConstant.EVENT_EVENTTYPE, eventObj.getEventType() + "%"));
        if (eventObj.getName() != null)
            criteria.add(Restrictions.ilike(EventnotificationConstant.EVENT_NAME, eventObj.getName() + "%"));
        if (eventObj.getEventhost() != null)
            criteria.add(Restrictions.ilike(EventnotificationConstant.EVENT_HOST, eventObj.getEventhost() + "%"));

        try {
            criteria.add(Restrictions.between(EventnotificationConstant.EVENT_STARTDATE,
                    formatter.parse(formatter.format(startDate)).getTime(),
                    formatter.parse(formatter.format(endDate)).getTime()));
            criteria.add(Restrictions.eq(EventnotificationConstant.STATUS_COLUMN, EventnotificationConstant.ACTIVE));
        } catch (ParseException e) {
            LOGGER.error(e.getMessage(), e);
        }

        criteria.addOrder(Order.desc(EventnotificationConstant.EVENT_ID));
        return criteria.list();
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
                                multipartFile.getContentType(), EventnotificationConstant.MODULE_NAME));

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
                                multipartFile.getContentType(), EventnotificationConstant.MODULE_NAME));

    }

    /**
     * Remove the file into filestore
     * @param event
     * @throws IOException
     */
    public void removeEventWallpaper(Event event) throws IOException {

        fileStoreService.delete(event.getFilestore().getFileStoreId(), EventnotificationConstant.MODULE_NAME);

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
        if (null == event.getFilestore())
            messageContent.setImageUrl("");
        else
            messageContent.setImageUrl(event.getFilestore().getFileStoreId());
        messageContent.setMessageBody(event.getMessage());
        messageContent.setMessageId(event.getId());
        messageContent.setModuleName(event.getName());
        messageContent.setNotificationDateTime(new Date().getTime());
        messageContent.setNotificationType(EventnotificationConstant.NOTIFICATION_TYPE_EVENT);
        messageContent.setSenderId(user.getId());
        messageContent.setSenderName(user.getName());
        messageContent.setSendAll(Boolean.TRUE);

        pushNotificationService.sendNotifications(messageContent);
    }
}
