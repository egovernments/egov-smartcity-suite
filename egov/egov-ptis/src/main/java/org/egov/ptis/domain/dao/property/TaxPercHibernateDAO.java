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
package org.egov.ptis.domain.dao.property;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.ptis.domain.entity.property.Category;
import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.egov.ptis.domain.entity.property.TaxPerc;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;


/**
 * This Class implememets the VacantPropertyDAO for the Hibernate specific 
 * Implementation 
 * 
 * @author Neetu
 * @version 2.00
 */

public class TaxPercHibernateDAO extends GenericHibernateDAO implements TaxPercDAO
{
	private static final Logger LOGGER = Logger.getLogger(TaxPercHibernateDAO.class);

	/**
	 * @param persistentClass
	 * @param session
	 */

	public TaxPercHibernateDAO(Class persistentClass, Session session)
	{
		super(persistentClass, session);
	}

	public TaxPerc getTaxPerc(Category category, PropertyUsage propertyUsage,BigDecimal amount,Date date) 
	{
		LOGGER.info("getTaxPerc invoked");
		TaxPerc taxPerc = null;
		Criteria crit = getCurrentSession().createCriteria(TaxPerc.class);
		if(category!=null)
		{
			crit.add(Expression.eq("category", category));
		}
		if(propertyUsage!=null)
		{
			crit.add(Expression.eq("propertyUsage", propertyUsage));
		}
		if(amount!=null)
		{
			crit.add(Expression.le("fromAmt", amount));
			crit.add(Expression.ge("toAmt", amount));
		}
		if(date!=null)
		{
			crit.add(Expression.lt("fromDate", date));
			crit.add(Expression.gt("toDate", date));
		}
		if(crit.list().size()==1)
		{
			taxPerc = (TaxPerc)crit.uniqueResult();
		}
		return taxPerc;
	}

	public Float getTaxPerc(Integer usageId) 
	{
		LOGGER.info("getTaxPerc for a given usageId invoked");
		Query qry = getCurrentSession().createQuery("select P.tax_perc from TaxPerc P where P.propertyUsage.idUsage = :usageId");
		qry.setInteger("usageId", usageId);	   
		//qry.setMaxResults(1);
		return (Float)qry.uniqueResult();
	}
}


