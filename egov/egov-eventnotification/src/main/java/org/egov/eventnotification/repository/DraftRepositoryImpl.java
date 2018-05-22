package org.egov.eventnotification.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.egov.eventnotification.constants.Constants;
import org.egov.eventnotification.entity.NotificationDrafts;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class DraftRepositoryImpl {

    @PersistenceContext
    private EntityManager entityManager;

    public List<NotificationDrafts> searchDraft(NotificationDrafts draftObj) {
        Criteria criteria = entityManager.unwrap(Session.class).createCriteria(NotificationDrafts.class);
        if (StringUtils.isNotBlank(draftObj.getType()))
            criteria.add(Restrictions.ilike(Constants.DRAFT_NOTIFICATION_TYPE,
                    draftObj.getType(), MatchMode.ANYWHERE));
        if (StringUtils.isNotBlank(draftObj.getName()))
            criteria.add(Restrictions.ilike(Constants.DRAFT_NAME, draftObj.getName(), MatchMode.ANYWHERE));
        criteria.addOrder(Order.desc(Constants.DRAFT_ID));
        return criteria.list();
    }
}
