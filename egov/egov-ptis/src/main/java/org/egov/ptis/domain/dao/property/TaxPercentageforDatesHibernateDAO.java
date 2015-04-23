/* TaxPercentageforDatesHibernateDAO.java Created on june 13, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.ptis.domain.dao.property;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;


/**
 * This Class implememets the TaxPercentageforDatesDAO for the Hibernate specific 
 * Implementation 
 * 
 * @author Lokesh
 * @version 2.00
 */

public class TaxPercentageforDatesHibernateDAO extends GenericHibernateDAO implements TaxPercentageforDatesDAO
{
	private static final Logger LOGGER = Logger.getLogger(TaxPercentageforDatesHibernateDAO.class);
	
	/**
	 * @param persistentClass
	 * @param session
	 */
	
	public TaxPercentageforDatesHibernateDAO(Class persistentClass, Session session)
	{
		super(persistentClass, session);
	}
	
	public List getTaxPercentageforDates(Integer type, BigDecimal amount,Date installmentStartDate,Date installmentEndDate)
	{
	
		try
 {
			List results = HibernateUtil
					.getCurrentSession()
					.createSQLQuery(
							"select * from EGPT_TAXRATES er where  er.TO_DATE BETWEEN to_date(?) "
									+ "and to_date(?) and er.FROM_AMOUNT<? and type = ?")
					.setParameter(0, installmentStartDate.getTime())
					.setParameter(1, installmentEndDate.getTime())
					.setParameter(2, amount).setParameter(3, type).list();
			LOGGER.info("size of the retList array is " + results.size());

			return results;

		} catch (HibernateException e) {
			LOGGER.info("Exception in  getTaxPercentageforDates--- TaxPercentageforDatesHibernateDao--"
					+ e.getMessage());
			throw new EGOVRuntimeException("Hibernate Exception : "
					+ e.getMessage(), e);
		}
		catch (Exception e1)
		{
			LOGGER.info("Exception in  getTaxPercentageforDates--- TaxPercentageforDatesHibernateDao--"+e1.getMessage());
			throw new EGOVRuntimeException("Exception : "+e1.getMessage(),e1);
		}
	}
}	