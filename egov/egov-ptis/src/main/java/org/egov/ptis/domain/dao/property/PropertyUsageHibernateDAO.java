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
package org.egov.ptis.domain.dao.property;

import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Repository(value = "propertyUsageDAO")
@Transactional(readOnly = true)
public class PropertyUsageHibernateDAO implements PropertyUsageDAO {

    @PersistenceContext
    private EntityManager entityManager;

    private Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    /**
     * To get Property Usage by Usage Code
     * 
     * @param usageCode
     * @param fromDate
     * @return PropertyUsage
     * */
    @Override
    public PropertyUsage getPropertyUsage(String usageCode) {
        Query qry = getCurrentSession().createQuery(
                "from PropertyUsage PU where PU.usageCode = :usageCode AND PU.usageCode IS NOT NULL AND ("
                        + "(PU.toDate IS NULL AND PU.fromDate <= :currDate) " + "OR "
                        + "(PU.fromDate <= :currDate AND PU.toDate >= :currDate)) ");
        qry.setString("usageCode", usageCode);
        qry.setDate("currDate", new Date());
        return (PropertyUsage) qry.uniqueResult();
    }

    /**
     * To get Property Usage by Usage Code and From Date given
     * 
     * @param usageCode
     * @param fromDate
     * @return PropertyUsage
     * */
    @Override
    public PropertyUsage getPropertyUsage(String usageCode, Date fromDate) {
        Query qry = getCurrentSession().createQuery(
                "from PropertyUsage PU where PU.usageCode = :usageCode AND PU.usageCode IS NOT NULL AND ("
                        + "(PU.toDate IS NULL AND PU.fromDate <= :fromDate) " + "OR "
                        + "(PU.fromDate <= :fromDate AND PU.toDate >= :fromDate)) ");
        qry.setString("usageCode", usageCode);
        qry.setDate("fromDate", fromDate);
        return (PropertyUsage) qry.uniqueResult();
    }

    /**
     * To get All active Property Usages
     * 
     * @return List of PropertyUsages
     * */
    @Override
    public List<PropertyUsage> getAllActivePropertyUsage() {
        Query qry = getCurrentSession().createQuery(
                "from PropertyUsage PU where PU.isEnabled = " + 1);
        return qry.list();
    }

    /**
     * To get All Property Usages
     * 
     * @return List of PropertyUsages
     * */
    @Override
    public List<PropertyUsage> getAllPropertyUsage() {
        Query qry = getCurrentSession().createQuery(
                "from PropertyUsage PU where ("
                        + "(PU.toDate IS NULL AND PU.fromDate <= :currDate) " + "OR "
                        + "(PU.fromDate <= :currDate AND PU.toDate >= :currDate))");
        qry.setDate("currDate", new Date());
        return qry.list();
    }

    @Override
    public List getPropUsageAscOrder() {
        Criteria criteria = getCurrentSession().createCriteria(PropertyUsage.class).addOrder(
                Order.asc("id"));
        return criteria.list();
    }

    @Override
    public PropertyUsage findById(Long id, boolean lock) {
        return (PropertyUsage) getCurrentSession().createQuery("from PropertyUsage where id = ?").setParameter(0, id)
                .uniqueResult();
    }

    @Override
    public PropertyUsage create(PropertyUsage propertyUsage) {
        getCurrentSession().save(propertyUsage);
        getCurrentSession().flush();
        return propertyUsage;
    }

    @Override
    public void delete(PropertyUsage propertyUsage) {
        getCurrentSession().delete(propertyUsage);
    }

    @Override
    public List<PropertyUsage> findAll() {
        return getCurrentSession().createQuery("From PropertyUsage order by usageName").list();
    }
}
