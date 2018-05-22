package org.egov.eventnotification.repository;

import static org.egov.eventnotification.constants.Constants.ACTIVE;
import static org.egov.eventnotification.constants.Constants.DDMMYYYY;
import static org.egov.eventnotification.constants.Constants.EVENT_EVENTTYPE;
import static org.egov.eventnotification.constants.Constants.EVENT_HOST;
import static org.egov.eventnotification.constants.Constants.EVENT_ID;
import static org.egov.eventnotification.constants.Constants.EVENT_NAME;
import static org.egov.eventnotification.constants.Constants.EVENT_STARTDATE;
import static org.egov.eventnotification.constants.Constants.STATUS_COLUMN;
import static org.egov.eventnotification.constants.Constants.UPCOMING;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.egov.eventnotification.entity.Event;
import org.egov.eventnotification.repository.custom.EventRepositoryCustom;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;

public class EventRepositoryImpl implements EventRepositoryCustom {

    private static final Logger LOGGER = Logger.getLogger(EventRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Event> searchEvent(Event eventObj, String eventDateType) {
        DateFormat formatter = new SimpleDateFormat(DDMMYYYY);
        DateTime calendar = new DateTime();
        DateTime calendarEndDate = null;
        Date startDate;
        Date endDate;
        if (eventDateType.equalsIgnoreCase(UPCOMING)) {
            calendar.plusDays(8);
            startDate = calendar.toDate();
            calendarEndDate = new DateTime(calendar.getYear(), calendar.getMonthOfYear(), calendar.getDayOfMonth(), 0, 0, 0, 0);
            calendarEndDate.plusDays(7);
            endDate = calendarEndDate.toDate();
        } else {
            startDate = calendar.toDate();
            calendar.plusDays(7);
            endDate = calendar.toDate();
        }

        Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Event.class);
        if (eventObj.getEventType() != null)
            criteria.add(
                    Restrictions.ilike(EVENT_EVENTTYPE, eventObj.getEventType(), MatchMode.ANYWHERE));
        if (eventObj.getName() != null)
            criteria.add(Restrictions.ilike(EVENT_NAME, eventObj.getName(), MatchMode.ANYWHERE));
        if (eventObj.getEventhost() != null)
            criteria.add(Restrictions.ilike(EVENT_HOST, eventObj.getEventhost(), MatchMode.ANYWHERE));

        try {
            criteria.add(Restrictions.between(EVENT_STARTDATE,
                    formatter.parse(formatter.format(startDate)).getTime(),
                    formatter.parse(formatter.format(endDate)).getTime()));
            criteria.add(Restrictions.eq(STATUS_COLUMN, ACTIVE.toUpperCase()));
        } catch (ParseException e) {
            LOGGER.error(e.getMessage(), e);
        }

        criteria.addOrder(Order.desc(EVENT_ID));
        return criteria.list();
    }

}
