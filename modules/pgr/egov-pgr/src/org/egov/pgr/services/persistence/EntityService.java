/*
 * @(#)EntityService.java 3.0, 23 Jul, 2013 3:29:42 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pgr.services.persistence;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.egov.commons.CFinancialYear;
import org.egov.infstr.services.PersistenceService;

/**
 * Base service class which will talk to PersistenceService
 * @author prashant.gaurav
 * @param <T> type variable
 * @param <ID> primary key for the type T
 */
public interface EntityService<T, ID extends Serializable> {

	/**
	 * Generic method to get an object based on class and identifier. An ObjectRetrievalFailureException Runtime Exception is thrown if nothing is found.
	 * @param id
	 * @param lock
	 * @return
	 */
	T findById(ID id, boolean lock);

	/**
	 * Generic method used to get all objects of a particular type. This is the same as lookup up all rows in a table.
	 * @return list of objects.
	 */
	List<T> findAll();

	/**
	 * Generic method to search similar objects.
	 * @param exampleT
	 * @return
	 */
	List<T> findByExample(T exampleT);

	/**
	 * Generic method to save an object.
	 * @param entity
	 * @return the created
	 */
	T create(T entity);

	/**
	 * Generic method to validate and save/update the entity
	 * @param entity
	 * @return
	 */
	T persist(T entity);

	/**
	 * Generic method to merge entity
	 * @param model
	 * @return
	 */
	T merge(T model);

	/**
	 * Generic method to delete an object based on class and id
	 * @param entity
	 */
	void delete(T entity);

	/**
	 * Generic method to update an object.
	 * @param entity
	 * @return the updated object
	 */
	T update(T entity);

	/**
	 * Find object by query and parameter.
	 * @param query
	 * @param params
	 * @return object.
	 */
	T find(String query, Object... params);

	/**
	 * Find object by query.
	 * @param query
	 * @return object.
	 */
	T find(String query);

	/**
	 * Find list of object by query and parameter.
	 * @param query
	 * @param params
	 * @return List of object
	 */
	List<T> findAllBy(String query, Object... params);

	/**
	 * Find list of object by query and parameter.
	 * @param namedQuery
	 * @param params
	 * @return List of object
	 */
	List<T> findAllByNamedQuery(String namedQuery, Object... params);

	/**
	 * find object by named query and parameter.
	 * @param namedQuery
	 * @param params
	 * @return object.
	 */
	T findByNamedQuery(String namedQuery, Object... params);

	/**
	 * This method retrieves the <code>CFinancialYear</code> for the given date.
	 * @param date an instance of <code>Date</code> for which the financial year is to be retrieved.
	 * @return <code>CFinancialYear</code>
	 */
	CFinancialYear getCurrentFinancialYear(Date date);

	/**
	 * Provider for other {@link PersistenceService} services
	 * @return <code>PersistenceService<T, ID></code>
	 */
	PersistenceService<T, ID> getGenericService();
	
	boolean isExist(String keyFieldName, Object keyValue);
}
