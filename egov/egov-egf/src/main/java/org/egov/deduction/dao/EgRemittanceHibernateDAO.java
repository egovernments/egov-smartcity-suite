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


package org.egov.deduction.dao;

import org.apache.log4j.Logger;
import org.egov.commons.CFinancialYear;
import org.egov.commons.Fund;
import org.egov.deduction.model.EgRemittance;
import org.egov.model.recoveries.Recovery;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.type.StringType;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * TODO Brief Description of the purpose of the class/interface
 *
 * @author Sathish
 * @version 1.00
 */
@Transactional(readOnly = true)
public class EgRemittanceHibernateDAO {
    private final static Logger LOGGER = Logger.getLogger(EgRemittanceHibernateDAO.class);
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public EgRemittance update(final EgRemittance entity) {
        getCurrentSession().update(entity);
        return entity;
    }

    @Transactional
    public EgRemittance create(final EgRemittance entity) {
        getCurrentSession().persist(entity);
        return entity;
    }

    @Transactional
    public void delete(EgRemittance entity) {
        getCurrentSession().delete(entity);
    }

    public EgRemittance findById(Number id, boolean lock) {
        return (EgRemittance) getCurrentSession().load(EgRemittance.class, id);
    }

    public List<EgRemittance> findAll() {
        return (List<EgRemittance>) getCurrentSession().createQuery("from EgRemittance").list();
    }

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    public List<EgRemittance> getEgRemittanceFilterBy(final Fund fund, final Recovery recovery, final String month,
                                                      final CFinancialYear financialyear) {
        Query qry;
        final StringBuffer qryStr = new StringBuffer();
        qryStr.append("From EgRemittance rmt where rmt.voucherheader.type = 'Payment' and rmt.voucherheader.status = 0");
        if (fund != null) {
            qryStr.append(" and (rmt.fund = :fund)");
        }
        if (recovery != null) {
            qryStr.append(" and (rmt.tds = :recovery)");
        }
        if (month != null) {
            qryStr.append(" and (rmt.month = :month)");
        }
        if (financialyear != null) {
            qryStr.append(" and (rmt.financialyear = :financialyear)");
        }

        qryStr.append(" order by upper(rmt.tds.type)");
        qry = getCurrentSession().createQuery(qryStr.toString());

        if (fund != null)
            qry.setParameter("fund", fund);
        if (recovery != null)
            qry.setParameter("recovery", recovery);
        if (month != null)
            qry.setParameter("month", month, StringType.INSTANCE);
        if (financialyear != null)
            qry.setParameter("financialyear", financialyear);

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("qryStr " + qryStr.toString());
        return qry.list();
    }

}