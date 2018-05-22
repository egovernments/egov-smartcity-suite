package org.egov.eventnotification.repository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.egov.eventnotification.constants.Constants;
import org.egov.eventnotification.entity.Event;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class EventRepositoryImpl {

    private static final Logger LOGGER = Logger.getLogger(EventRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    public List<Event> searchEvent(Event eventObj, String eventDateType) {
        DateFormat formatter = new SimpleDateFormat(Constants.DDMMYYYY);
        Calendar calendar = Calendar.getInstance();
        Calendar calendarEndDate = Calendar.getInstance();
        Date startDate;
        Date endDate;
        if (eventDateType.equalsIgnoreCase(Constants.UPCOMING)) {
            calendar.add(Calendar.DAY_OF_MONTH, 8);
            startDate = calendar.getTime();
            calendarEndDate.setTime(startDate);
            calendarEndDate.add(Calendar.DAY_OF_MONTH, 7);
            endDate = calendarEndDate.getTime();
        } else {
            startDate = calendar.getTime();
            calendar.add(Calendar.DAY_OF_MONTH, 7);
            endDate = calendar.getTime();
        }

        Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Event.class);
        if (eventObj.getEventType() != null)
            criteria.add(
                    Restrictions.ilike(Constants.EVENT_EVENTTYPE, eventObj.getEventType(), MatchMode.ANYWHERE));
        if (eventObj.getName() != null)
            criteria.add(Restrictions.ilike(Constants.EVENT_NAME, eventObj.getName(), MatchMode.ANYWHERE));
        if (eventObj.getEventhost() != null)
            criteria.add(Restrictions.ilike(Constants.EVENT_HOST, eventObj.getEventhost(), MatchMode.ANYWHERE));

        try {
            criteria.add(Restrictions.between(Constants.EVENT_STARTDATE,
                    formatter.parse(formatter.format(startDate)).getTime(),
                    formatter.parse(formatter.format(endDate)).getTime()));
            criteria.add(Restrictions.eq(Constants.STATUS_COLUMN, Constants.ACTIVE.toUpperCase()));
        } catch (ParseException e) {
            LOGGER.error(e.getMessage(), e);
        }

        criteria.addOrder(Order.desc(Constants.EVENT_ID));
        return criteria.list();
    }

}
