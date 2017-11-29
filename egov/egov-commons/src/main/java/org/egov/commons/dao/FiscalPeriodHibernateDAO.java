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

import org.apache.log4j.Logger;
import org.egov.commons.CFiscalPeriod;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Repository
public class FiscalPeriodHibernateDAO   implements FiscalPeriodDAO {
    @Transactional
    public CFiscalPeriod update(final CFiscalPeriod entity) {
        getCurrentSession().update(entity);
        return entity;
    }

    @Transactional
    public CFiscalPeriod create(final CFiscalPeriod entity) {
        getCurrentSession().persist(entity);
        return entity;
    }

    @Transactional
    public void delete(CFiscalPeriod entity) {
        getCurrentSession().delete(entity);
    }

    public CFiscalPeriod findById(Number id, boolean lock) {
        return (CFiscalPeriod) getCurrentSession().load(CFiscalPeriod.class, id);
    }

    public List<CFiscalPeriod> findAll() {
        return (List<CFiscalPeriod>) getCurrentSession().createCriteria(CFiscalPeriod.class).list();
    }

    @PersistenceContext
    private EntityManager entityManager;

    
    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    private final Logger logger = Logger.getLogger(getClass().getName());

    public String getFiscalPeriodIds(String financialYearId) {
        logger.info("Obtained session");
        StringBuffer result = new StringBuffer();
        Query query = getCurrentSession().createQuery(
                "select cfiscalperiod.id from CFiscalPeriod cfiscalperiod where cfiscalperiod.financialYearId = '"
                        + financialYearId + "'  ");
        ArrayList list = (ArrayList) query.list();
        if (list.size() > 0) {
            if (list.get(0) == null)
                return 0.0 + "";
            else {
                for (int i = 0; i < list.size(); i++) {
                    result.append(list.get(i).toString());
                    if (list.size() - i != 1)
                        result.append(",");
                }
            }
        } else
            return 0.0 + "";
        return result.toString();
    }

    /**
	 * 
	 */
    public CFiscalPeriod getFiscalPeriodByDate(Date voucherDate) {
        Query query = getCurrentSession().createQuery(
                "from CFiscalPeriod fp where  :voucherDate between fp.startingDate and fp.endingDate");
        query.setDate("voucherDate", voucherDate);
        query.setCacheable(true);
        return (CFiscalPeriod) query.uniqueResult();
    }

}