package org.egov.eventnotification.repository;

import static org.egov.eventnotification.constants.Constants.ACTIVE;
import static org.egov.eventnotification.constants.Constants.EVENT_EVENTTYPE;
import static org.egov.eventnotification.constants.Constants.EVENT_HOST;
import static org.egov.eventnotification.constants.Constants.EVENT_ID;
import static org.egov.eventnotification.constants.Constants.EVENT_NAME;
import static org.egov.eventnotification.constants.Constants.EVENT_STARTDATE;
import static org.egov.eventnotification.constants.Constants.STATUS_COLUMN;
import static org.egov.eventnotification.constants.Constants.UPCOMING;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.eventnotification.entity.Event;
import org.egov.eventnotification.repository.custom.EventRepositoryCustom;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;

public class EventRepositoryImpl implements EventRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Event> searchEvent(Event eventObj, String eventDateType) {
        DateTime calendar = new DateTime();
        DateTime calendarEndDate = null;
        Date startDate;
        Date endDate;
        if (eventDateType.equalsIgnoreCase(UPCOMING)) {
            calendar = calendar.plusDays(7);
            calendar = calendar.withHourOfDay(0);
            calendar = calendar.withMinuteOfHour(0);
            calendar = calendar.withSecondOfMinute(0);
            startDate = calendar.toDate();
            calendarEndDate = new DateTime(startDate);
            calendarEndDate = calendarEndDate.plusDays(6);
            calendarEndDate = calendarEndDate.withHourOfDay(23);
            calendarEndDate = calendarEndDate.withMinuteOfHour(59);
            calendarEndDate = calendarEndDate.withSecondOfMinute(0);
            endDate = calendarEndDate.toDate();
        } else {
            calendar = calendar.withHourOfDay(0);
            calendar = calendar.withMinuteOfHour(0);
            calendar = calendar.withSecondOfMinute(0);
            startDate = calendar.toDate();
            calendarEndDate = new DateTime(startDate);
            calendarEndDate = calendarEndDate.plusDays(6);
            calendarEndDate = calendarEndDate.withHourOfDay(23);
            calendarEndDate = calendarEndDate.withMinuteOfHour(59);
            calendarEndDate = calendarEndDate.withSecondOfMinute(0);
            endDate = calendarEndDate.toDate();
        }

        Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Event.class);
        if (eventObj.getEventType() != null)
            criteria.add(
                    Restrictions.ilike(EVENT_EVENTTYPE, eventObj.getEventType(), MatchMode.ANYWHERE));
        if (eventObj.getName() != null)
            criteria.add(Restrictions.ilike(EVENT_NAME, eventObj.getName(), MatchMode.ANYWHERE));
        if (eventObj.getEventhost() != null)
            criteria.add(Restrictions.ilike(EVENT_HOST, eventObj.getEventhost(), MatchMode.ANYWHERE));

        criteria.add(Restrictions.between(EVENT_STARTDATE, startDate, endDate));
        criteria.add(Restrictions.eq(STATUS_COLUMN, ACTIVE.toUpperCase()));

        criteria.addOrder(Order.desc(EVENT_ID));
        return criteria.list();
    }

}
