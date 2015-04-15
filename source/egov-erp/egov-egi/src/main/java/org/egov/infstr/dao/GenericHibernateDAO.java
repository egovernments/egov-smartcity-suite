/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.infstr.dao;

import org.egov.exceptions.EGOVRuntimeException;
import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;

import java.io.Serializable;
import java.util.List;

/**
 * Implements the generic CRUD data access operations using Hibernate APIs.
 * <p>
 * To write a DAO, subclass and parameterize this class with your persistent class. Of course, 
 * assuming that you have a traditional 1:1 approach for Entity:DAO design.
 * <p>
 * You have to inject the <tt>Class</tt> object of the persistent class and a current 
 * Hibernate <tt>Session</tt> to construct a DAO.
 * @param <T> the generic type
 * @param <ID> the generic id type
 * @see RNDHibernateDAOFactory
 * @author christian.bauer@jboss.com
 */
public class GenericHibernateDAO<T, ID extends Serializable> implements GenericDAO<T, ID> {

	/** The persistent class. */
	private Class<T> persistentClass;

	/** The hibernate session. */
	private Session session;

	/**
	 * Gets the hibernate session.
	 * @return the hibernate session
	 */
	protected Session getCurrentSession() {
		return session;
	}

	/**
	 * Instantiates a GenericHibernateDAO.
	 * @param persistentClass the persistent class
	 * @param session the session
	 */
	public GenericHibernateDAO(Class<T> persistentClass, Session session) {
		this.persistentClass = persistentClass;
		this.session = session;
	}

	/**
	 * Gets the persistent class.
	 * @return the persistent class
	 */
	public Class<T> getPersistentClass() {
		return persistentClass;
	}

	/**
	 * Gets an entity for the given entity id.
	 * @param id the id
	 * @param lock the lock
	 * @return the t
	 * @see org.egov.infstr.dao.GenericDAO#findById(java.io.Serializable, boolean)
	 */
	public T findById(ID id, boolean lock) {
		T entity;
		if (lock) {
			entity = (T) getCurrentSession().load(getPersistentClass(), id, LockOptions.UPGRADE);
		} else {
			entity = (T) getCurrentSession().load(getPersistentClass(), id);

		}
		return entity;
	}

	/**
	 * Gets all entity.
	 * @return the list
	 * @see org.egov.infstr.dao.GenericDAO#findAll()
	 */
	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		return getCurrentSession().createCriteria(getPersistentClass()).list();
	}

	/**
	 * Gets list of entities for the given example entity.
	 * @param exampleT the example type entity
	 * @return the list
	 * @see org.egov.infstr.dao.GenericDAO#findByExample(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public List<T> findByExample(T exampleT) {
		Criteria crit = getCurrentSession().createCriteria(getPersistentClass());
		return crit.add(Example.create(exampleT)).list();
	}

	/**
	 * To persist or update a ENtity.
	 * @param entity the entity
	 * @return the t
	 * @see org.egov.infstr.dao.GenericDAO#create(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public T create(T entity) {
		getCurrentSession().saveOrUpdate(entity);
		return entity;
	}

	/**
	 * To update an Entity.
	 * @param entity the entity
	 * @return the t
	 * @see org.egov.infstr.dao.GenericDAO#update(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public T update(T entity) {
		getCurrentSession().saveOrUpdate(entity);
		return entity;
	}

	/**
	 * Delete an entity.
	 * @param entity the entity
	 * @see org.egov.infstr.dao.GenericDAO#delete(java.lang.Object)
	 */
	public void delete(T entity) {
		getCurrentSession().delete(entity);
	}

	/**
	 * Find entity using the criteria provided Use this inside subclasses as a convenience method.
	 * @param criterion the criterion
	 * @return the list
	 */
	@SuppressWarnings("unchecked")
	protected List<T> findByCriteria(Criterion... criterion) {
		Criteria crit = getCurrentSession().createCriteria(getPersistentClass());
		for (Criterion c : criterion) {
			crit.add(c);
		}
		return crit.list();
	}

}
