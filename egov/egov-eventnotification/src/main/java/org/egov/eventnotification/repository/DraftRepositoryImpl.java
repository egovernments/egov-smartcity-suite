package org.egov.eventnotification.repository;

import static org.egov.eventnotification.constants.Constants.DRAFT_ID;
import static org.egov.eventnotification.constants.Constants.DRAFT_NAME;
import static org.egov.eventnotification.constants.Constants.DRAFT_NOTIFICATION_TYPE;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.egov.eventnotification.entity.NotificationDrafts;
import org.egov.eventnotification.repository.custom.DraftRepositoryCustom;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class DraftRepositoryImpl implements DraftRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<NotificationDrafts> searchDraft(NotificationDrafts draftObj) {
        Criteria criteria = entityManager.unwrap(Session.class).createCriteria(NotificationDrafts.class);
        if (StringUtils.isNotBlank(draftObj.getType()))
            criteria.add(Restrictions.ilike(DRAFT_NOTIFICATION_TYPE,
                    draftObj.getType(), MatchMode.ANYWHERE));
        if (StringUtils.isNotBlank(draftObj.getName()))
            criteria.add(Restrictions.ilike(DRAFT_NAME, draftObj.getName(), MatchMode.ANYWHERE));
        criteria.addOrder(Order.desc(DRAFT_ID));
        return criteria.list();
    }
}
