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
package org.egov.works.services.impl;

import org.egov.commons.CFinancialYear;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.works.services.BaseService;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class BaseServiceImpl<T, ID extends Serializable> implements BaseService<T, ID> {

    protected PersistenceService<T, ID> persistenceService;
    protected PersistenceService genericService;

    /**
     * Public constructor for creating a new BaseServiceImpl.
     *
     * @param genericDAO
     */
    public BaseServiceImpl(final PersistenceService<T, ID> persistenceService) {
        super();
        this.persistenceService = persistenceService;
    }

    /**
     * Generic method to get an object based on class and identifier. An ObjectRetrievalFailureException Runtime Exception is
     * thrown if nothing is found.
     *
     * @param id
     * @param lock
     * @return
     */
    @Override
    public T findById(final ID id, final boolean lock) {
        return persistenceService.findById(id, lock);
    }

    /**
     * Generic method used to get all objects of a particular type. This is the same as lookup up all rows in a table.
     *
     * @return list of objects.
     */
    @Override
    public List<T> findAll() {
        return persistenceService.findAll();
    }

    /**
     * Generic method to search similar objects.
     *
     * @param exampleT
     * @return
     */
    @Override
    public List<T> findByExample(final T exampleT) {
        return persistenceService.findByExample(exampleT);
    }

    /**
     * Generic method to save an object.
     *
     * @param entity
     * @return the created
     */
    @Override
    public T create(final T entity) {
        return persistenceService.create(entity);
    }

    /**
     * Generic method to validate and save/update the entity
     *
     * @param entity
     * @return
     */
    @Override
    public T persist(final T entity) {
        return persistenceService.persist(entity);
    }

    /**
     * Generic method to merge entity
     *
     * @param model
     * @return
     */
    @Override
    public T merge(final T model) {
        return persistenceService.merge(model);
    }

    /**
     * Generic method to delete an object based on class and id
     *
     * @param entity
     */
    @Override
    public void delete(final T entity) {
        persistenceService.delete(entity);
    }

    /**
     * Generic method to update an object.
     *
     * @param entity
     * @return the updated object
     */
    @Override
    public T update(final T entity) {
        return persistenceService.update(entity);
    }

    /**
     * Find object by query and parameter.
     *
     * @param query
     * @param params
     * @return object.
     */
    @Override
    public T find(final String query, final Object... params) {
        return persistenceService.find(query, params);
    }

    /**
     * Find object by query.
     *
     * @param query
     * @return object.
     */
    @Override
    public T find(final String query) {
        return persistenceService.find(query);
    }

    /**
     * Find list of object by query and parameter.
     *
     * @param query
     * @param params
     * @return List of object
     */
    @Override
    public List<T> findAllBy(final String query, final Object... params) {
        return persistenceService.findAllBy(query, params);
    }

    /**
     * Find list of object by query and parameter.
     *
     * @param namedQuery
     * @param params
     * @return List of object
     */
    @Override
    public List<T> findAllByNamedQuery(final String namedQuery, final Object... params) {
        return persistenceService.findAllByNamedQuery(namedQuery, params);
    }

    /**
     * find object by named query and parameter.
     *
     * @param namedQuery
     * @param params
     * @return object.
     */
    @Override
    public T findByNamedQuery(final String namedQuery, final Object... params) {
        return persistenceService.findByNamedQuery(namedQuery, params);
    }

    /**
     * search list of object by query and offset range.
     *
     * @param queryString
     * @param pageNumber
     * @param pageSize
     * @return list of object.
     */
    // public List<T> search(String queryString,int pageNumber,int pageSize){
    // return persistenceService.search(queryString, pageNumber, pageSize);
    // }

    /**
     * Search list of objects by query.
     *
     * @param queryString
     * @return list of objects
     */
    // public List<T> search(String queryString){
    // return persistenceService.search(queryString);
    // }

    public void setGenericService(final PersistenceService genericService) {
        this.genericService = genericService;
    }

    /**
     * This method retrieves the <code>CFinancialYear</code> for the given date.
     *
     * @param date an instance of <code>Date</code> for which the financial year is to be retrieved.
     * @return
     */
    @Override
    public CFinancialYear getCurrentFinancialYear(final Date date) {
        List<CFinancialYear> financialYear = null;
        financialYear = persistenceService
                .getSession()
                .createQuery(
                        "from CFinancialYear cfinancialyear where ? between "
                                + "cfinancialyear.startingDate and cfinancialyear.endingDate")
                .setDate(0, date).list();

        if (financialYear == null || financialYear != null && financialYear.isEmpty())
            throw new ValidationException(Arrays.asList(new ValidationError("financialyear",
                    "estimate.estimateDate.financialyear.invalid")));
        else
            return financialYear.get(0);
    }

}
