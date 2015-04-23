/*
 * TaxPercHibernateDAO.java Created on Oct 5, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

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


