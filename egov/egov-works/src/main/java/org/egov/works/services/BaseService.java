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
package org.egov.works.services;

import org.egov.commons.CFinancialYear;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Base service class which will talk to PersistenceService
 * @author prashant.gaurav
 *
 * @param <T> type variable
 * @param <ID> primary key for the type T
 */
public interface BaseService<T, ID extends Serializable> {

    /**
     * Generic method to get an object based on class and identifier. An ObjectRetrievalFailureException Runtime Exception is
     * thrown if nothing is found.
     *
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
     *
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
     * search list of object by query and offset range.
     * @param queryString
     * @param pageNumber
     * @param pageSize
     * @return list of object.
     */
    // List<T> search(String queryString,int pageNumber,int pageSize);

    /**
     * Search list of objects by query.
     * @param queryString
     * @return list of objects
     */
    // List<T> search(String queryString);

    /**
     * This method retrieves the <code>CFinancialYear</code> for the given date.
     *
     * @param date an instance of <code>Date</code> for which the financial year is to be retrieved.
     * @return <code>CFinancialYear</code>
     */
    CFinancialYear getCurrentFinancialYear(Date date);

}
