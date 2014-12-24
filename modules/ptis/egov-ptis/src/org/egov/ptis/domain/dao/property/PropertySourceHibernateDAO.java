/*
 * PropertySourceHibernateDAO.java Created on Oct 5, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.ptis.domain.dao.property;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.ptis.domain.entity.property.PropertySource;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * This Class implememets the PropertySourceDAO for the Hibernate specific 
 * Implementation 
 * 
 * @author Neetu
 * @version 2.00
 */

public class PropertySourceHibernateDAO extends GenericHibernateDAO implements PropertySourceDAO
{
	/**
	 * @param persistentClass
	 * @param session
	 */
	public PropertySourceHibernateDAO(Class persistentClass, Session session)
	{
		super(persistentClass, session);
	}
	public PropertySource getPropertySourceByCode(String propSrcCode)
	{
		Query qry = getSession().createQuery("from PropertySource PS where PS.propSrcCode =:propSrcCode ");
		qry.setString("propSrcCode",propSrcCode);
		return  (PropertySource)qry.uniqueResult();
	}
}
  

