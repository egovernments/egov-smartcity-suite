/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency, 
 *    accountability and the service delivery of the government  organizations.
 * 
 *     Copyright (C) <2015>  eGovernments Foundation
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
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org
 ******************************************************************************/
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
