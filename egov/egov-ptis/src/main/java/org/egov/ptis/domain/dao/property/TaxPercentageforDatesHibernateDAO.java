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
import org.egov.infra.exception.ApplicationRuntimeException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository(value = "taxPercentageforDatesDAO")
@Transactional(readOnly = true)
public class TaxPercentageforDatesHibernateDAO implements TaxPercentageforDatesDAO {
	private static final Logger LOGGER = Logger.getLogger(TaxPercentageforDatesHibernateDAO.class);

	@PersistenceContext
	private EntityManager entityManager;
		
	private Session getCurrentSession() {
		return entityManager.unwrap(Session.class);
	}
	
	@Override
	public List getTaxPercentageforDates(Integer type, BigDecimal amount,
			Date installmentStartDate, Date installmentEndDate) {

		try {
			List results = getCurrentSession()
					.createSQLQuery(
							"select * from EGPT_TAXRATES er where  er.TO_DATE BETWEEN to_date(?) "
									+ "and to_date(?) and er.FROM_AMOUNT<? and type = ?")
					.setParameter(0, installmentStartDate.getTime())
					.setParameter(1, installmentEndDate.getTime()).setParameter(2, amount)
					.setParameter(3, type).list();
			LOGGER.info("size of the retList array is " + results.size());

			return results;

		} catch (HibernateException e) {
			LOGGER.info("Exception in  getTaxPercentageforDates--- TaxPercentageforDatesHibernateDao--"
					+ e.getMessage());
			throw new ApplicationRuntimeException("Hibernate Exception : " + e.getMessage(), e);
		} catch (Exception e1) {
			LOGGER.info("Exception in  getTaxPercentageforDates--- TaxPercentageforDatesHibernateDao--"
					+ e1.getMessage());
			throw new ApplicationRuntimeException("Exception : " + e1.getMessage(), e1);
		}
	}
}
