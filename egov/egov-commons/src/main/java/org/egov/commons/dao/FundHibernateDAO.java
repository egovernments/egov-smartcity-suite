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
package org.egov.commons.dao;

import org.egov.commons.Fund;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class FundHibernateDAO {
    @Transactional
    public Fund update(final Fund entity) {
        getCurrentSession().update(entity);
        return entity;
    }

    @Transactional
    public Fund create(final Fund entity) {
        getCurrentSession().persist(entity);
        return entity;
    }

    @Transactional
    public void delete(Fund entity) {
        getCurrentSession().delete(entity);
    }


    public List<Fund> findAll() {
        return (List<Fund>) getCurrentSession().createCriteria(Fund.class).list();
    }

    @PersistenceContext
    private EntityManager entityManager;

    
    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

   
    public List findAllActiveFunds() {
        return getCurrentSession().createQuery("from Fund where isactive = true order by name").list();

    }


    /**
     * This method returns all the <code>Fund</code> records which are active
     * and is a leaf.
     * 
     * @return a <code>List</code> of <code>Fund</code> objects.
     */
    public List findAllActiveIsLeafFunds() {
        return getCurrentSession().createQuery("from Fund where isactive = true and isnotleaf=false order by name")
                .list();
    }

    public Fund fundByCode(final String fundCode) {
        final Query qry = getCurrentSession().createQuery("FROM Fund f WHERE f.code =:fundCode");
        qry.setString("fundCode", fundCode);
        return (Fund) qry.uniqueResult();
    }

    public Fund fundById(Integer id, boolean b) {
        return (Fund) getCurrentSession().get(Fund.class, id);
    }
}