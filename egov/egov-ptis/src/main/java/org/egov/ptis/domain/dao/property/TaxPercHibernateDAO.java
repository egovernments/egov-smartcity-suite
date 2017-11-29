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

import org.apache.log4j.Logger;
import org.egov.ptis.domain.entity.property.Category;
import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.egov.ptis.domain.entity.property.TaxPerc;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository(value = "taxPercDAO")
@Transactional(readOnly = true)
public class TaxPercHibernateDAO implements TaxPercDAO {
	private static final Logger LOGGER = Logger.getLogger(TaxPercHibernateDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	private Session getCurrentSession() {
		return entityManager.unwrap(Session.class);
	}

	@Override
	public TaxPerc getTaxPerc(Category category, PropertyUsage propertyUsage, BigDecimal amount,
			Date date) {
		LOGGER.info("getTaxPerc invoked");
		TaxPerc taxPerc = null;
		Criteria crit = getCurrentSession().createCriteria(TaxPerc.class);
		if (category != null) {
			crit.add(Restrictions.eq("category", category));
		}
		if (propertyUsage != null) {
			crit.add(Restrictions.eq("propertyUsage", propertyUsage));
		}
		if (amount != null) {
			crit.add(Restrictions.le("fromAmt", amount));
			crit.add(Restrictions.ge("toAmt", amount));
		}
		if (date != null) {
			crit.add(Restrictions.lt("fromDate", date));
			crit.add(Restrictions.gt("toDate", date));
		}
		if (crit.list().size() == 1) {
			taxPerc = (TaxPerc) crit.uniqueResult();
		}
		return taxPerc;
	}

	@Override
	public Float getTaxPerc(Integer usageId) {
		LOGGER.info("getTaxPerc for a given usageId invoked");
		Query qry = getCurrentSession().createQuery(
				"select P.tax_perc from TaxPerc P where P.propertyUsage.idUsage = :usageId");
		qry.setInteger("usageId", usageId);
		// qry.setMaxResults(1);
		return (Float) qry.uniqueResult();
	}

	@Override
	public TaxPerc findById(Integer id, boolean lock) {

		return null;
	}

	@Override
	public List<TaxPerc> findAll() {

		return null;
	}

	@Override
	public TaxPerc create(TaxPerc taxPerc) {

		return null;
	}

	@Override
	public void delete(TaxPerc taxPerc) {


	}

	@Override
	public TaxPerc update(TaxPerc taxPerc) {

		return null;
	}
}
