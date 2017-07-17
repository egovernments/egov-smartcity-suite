/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.infra.persistence.service;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@SuppressWarnings("unchecked")
public abstract class HibernateRepository<T> {

    @PersistenceContext
    protected EntityManager entityManager;
    
    protected Class<T> entityType;

    protected HibernateRepository(Class<T> entityType) {
        this.entityType = entityType;
    }

    public T save(T entity) {
        getCurrentSession().saveOrUpdate(entity);
        getCurrentSession().flush();
        return entity;
    }

    public void evict(T entity) {
        getCurrentSession().evict(entity);
    }

    public T create(T entity) {
        getCurrentSession().save(entity);
        getCurrentSession().flush();
        return entity;
    }

    public T merge(T entity) {
        T mergedEntity = (T) getCurrentSession().merge(entity);
        getCurrentSession().flush();
        return mergedEntity;
    }

    public T get(Long id) {
        return getCurrentSession().get(entityType, id);
    }
    
    public T load(Long id) {
        return getCurrentSession().load(entityType, id);
    }
    
    public Criteria createCriteria(Class<T> clazz) {
        return getCurrentSession().createCriteria(clazz);
    }

    public T findByField(String field, String value) {
        Criteria criteria = createCriteria(entityType);
        return (T) criteria.add(Restrictions.eq(field, value)).uniqueResult();
    }

    public List<T> findAll() {
        return getCurrentSession().createQuery(String.format("from %s", entityType.getSimpleName())).list();
    }
    
    public List<T> findAllLike(String fieldName, String value) {
        return createCriteria(entityType).add(Restrictions.ilike(fieldName, value, MatchMode.ANYWHERE)).list();
    }
    
    public Query query(String queryName){
        return getCurrentSession().getNamedQuery(queryName);
    }

    protected void flushAndClear() {
        Session currentSession = getCurrentSession();
        currentSession .flush();
        currentSession .clear();
    }

    protected Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }
}