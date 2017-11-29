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

package org.egov.infstr.services;

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.utils.Page;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infstr.models.BaseModel;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.Path.Node;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Old persistence service
 * @deprecated use Repositories
 **/
@Transactional(readOnly = true)
@Deprecated
public class PersistenceService<T, ID extends Serializable> {
    private static final Logger LOG = LoggerFactory.getLogger(PersistenceService.class);

    @PersistenceContext
    protected EntityManager entityManager;

    private Class<T> type;

    @Autowired
    @Qualifier("entityValidator")
    private LocalValidatorFactoryBean entityValidator;

    public PersistenceService(final Class<T> type) {
        this.type = type;
    }

    public Class<T> getType() {
        return this.type;
    }

    public Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    public void validate(final T model) {
        final List<ValidationError> errors = this.validateModel(model);
        if (!errors.isEmpty())
            throw new ValidationException(errors);
    }

    public List<ValidationError> validateModel(final T model) {
        LOG.debug("Validating Model");
        final List<ValidationError> errors = new ArrayList<>();
        if (model == null) {
            errors.add(new ValidationError("", "model.null"));
            return errors;
        }
        final Set<ConstraintViolation<T>> constraintViolations = entityValidator.validate(model);
        for (final ConstraintViolation<T> constraintViolation : constraintViolations) {
            final Iterator<Node> nodes = constraintViolation.getPropertyPath().iterator();
            while (nodes.hasNext())
                errors.add(new ValidationError(nodes.next().getName(), constraintViolation.getMessage()));
        }
        if (model instanceof BaseModel) {
            final BaseModel basemodel = (BaseModel) model;
            final List<ValidationError> dependentValMessages = basemodel.validate();
            if (dependentValMessages != null)
                errors.addAll(dependentValMessages);
        }
        return errors;
    }

    public T find(final String query, final Object... params) {
        final List<T> results = findAllBy(query, params);
        return results.isEmpty() ? null : results.get(0);
    }

    public T find(final String query) {
        final Query q = getSession().createQuery(query);
        return (T) q.uniqueResult();
    }

    protected T findById(final ID id) {
        return id == null ? null : getSession().get(this.type, id);
    }

    public List<T> findAllBy(final String query, final Object... params) {
        final Query q = getQueryWithParams(query, params);
        return q.list();
    }

    /**
     * @param query
     * @param pageNumber used to determine the offset from which to return the results
     * @param pageSize   Number of records to be returned in the page. If null then all
     *                   records that match query are returned
     * @param params
     * @return
     */
    public Page findPageBy(final String query, final Integer pageNumber, final Integer pageSize,
                           final Object... params) {
        final Query q = getQueryWithParams(query, params);
        return new Page(q, pageNumber, pageSize);
    }

    private Query getQueryWithParams(final String query, final Object... params) {
        final Query q = getSession().createQuery(query);
        int index = 0;
        for (final Object param : params) {
            q.setParameter(index, param);
            index++;
        }
        return q;
    }

    public List<T> findAllByNamedQuery(final String namedQuery, final Object... params) {
        final Query q = getNamedQueryWithParams(namedQuery, params);
        return q.list();
    }

    /**
     * @param namedQuery
     * @param pageNumber used to determine the offset from which to return the results
     * @param pageSize   Number of records to be returned in the page. If null then all
     *                   records that match query are returned
     * @param params
     * @return Page instance that can be used to implement pagination
     */
    public Page findPageByNamedQuery(final String namedQuery, final Integer pageNumber, final Integer pageSize,
                                     final Object... params) {
        final Query q = getNamedQueryWithParams(namedQuery, params);
        return new Page(q, pageNumber, pageSize);
    }

    private Query getNamedQueryWithParams(final String namedQuery, final Object... params) {
        final Query q = getSession().getNamedQuery(namedQuery);
        int index = 0;
        for (final Object param : params) {
            if (param instanceof Collection)
                q.setParameterList(String.valueOf("param_" + index), (Collection) param);
            else
                q.setParameter(index, param);
            index++;
        }
        return q;
    }

    public T findByNamedQuery(final String namedQuery, final Object... params) {
        final List<T> results = findAllByNamedQuery(namedQuery, params);
        return results.isEmpty() ? null : results.get(0);
    }

    @Transactional
    public T persist(final T model) {
        validate(model);
        getSession().saveOrUpdate(model);
        return model;
    }

    @Transactional
    public T merge(final T model) {
        validate(model);
        return (T) getSession().merge(model);
    }

    @Transactional
    public T create(final T entity) {
        validate(entity);
        final Long id = (Long) getSession().save(entity);
        return getSession().load(this.type, id);
    }

    public T load(final Serializable id, Class cls) {
        return (T) getSession().load(cls, id);
    }

    @Transactional
    public void delete(final T entity) {
        getSession().delete(entity);
    }


    public List<T> findAll() {
        return getSession().createCriteria(this.type).list();
    }


    public List<T> findByExample(final T exampleT) {
        final Criteria criteria = getSession().createCriteria(this.type);
        return criteria.add(Example.create(exampleT)).list();
    }


    public T findById(final ID id, final boolean lock) {
        return findById(id);
    }

    public T findByIdWithJoinFetch(final ID id, final String joinFetchPropertyName) {
        return (T) getSession().createCriteria(type).setFetchMode(joinFetchPropertyName, FetchMode.JOIN)
                .add(Restrictions.idEq(id)).uniqueResult();
    }

    @Transactional
    public T update(final T entity) {
        validate(entity);
        getSession().update(entity);
        return entity;
    }

    public List<T> findAll(final String... orderByFields) {
        final Criteria c = getSession().createCriteria(this.type);
        for (final String orderBy : orderByFields)
            c.addOrder(Order.asc(orderBy).ignoreCase());
        return c.list();
    }

    public String getNamedQuery(final String namedQuery) {
        return getSession().getNamedQuery(namedQuery).getQueryString();
    }

    public void addIndexparams(final Map<String, List> indexparams, final String key, final Object... values) {
        final List objparams = new ArrayList();
        for (final Object value : values)
            objparams.add(value);
        indexparams.put(key, objparams);
    }

    public void addFilterCriteriaForObject(final Map<String, List> params, final Criteria c,
                                           final String... orderbyFields) {
        for (final Map.Entry<String, List> entry : params.entrySet())
            if (entry.getKey().contains("date") || entry.getKey().contains("Date"))
                c.add(Restrictions.between(entry.getKey(), entry.getValue().get(0), entry.getValue().get(1)));
            else
                c.add(Restrictions.eq(entry.getKey(), entry.getValue().get(0)));
        for (final String orderBy : orderbyFields)
            c.addOrder(Order.asc(orderBy).ignoreCase());
    }

    /**
     * This method is a workaround to apply auditing field values for JPA entity when JPA mixed with hbm based
     * entities, this will be removed in future once modules are migrated to JPA annotation.
     **/
    public void applyAuditing(AbstractAuditable auditable) {
        Date currentDate = new Date();
        if (auditable.isNew()) {
            auditable.setCreatedBy(getSession().load(User.class, ApplicationThreadLocals.getUserId()));
            auditable.setCreatedDate(currentDate);
        }
        auditable.setLastModifiedBy(getSession().load(User.class, ApplicationThreadLocals.getUserId()));
        auditable.setLastModifiedDate(currentDate);
    }

    public void applyAuditing(BaseModel baseModel) {
        Date currentDate = new Date();
        if (baseModel.getId() == null) {
            baseModel.setCreatedBy(getSession().load(User.class, ApplicationThreadLocals.getUserId()));
            baseModel.setCreatedDate(currentDate);
        }
        baseModel.setModifiedBy(getSession().load(User.class, ApplicationThreadLocals.getUserId()));
        baseModel.setModifiedDate(currentDate);
    }

}
