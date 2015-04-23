/*
 * PropertyStatusHibernateDAO.java Created on Oct 5, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.ptis.domain.dao.property;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.ptis.domain.entity.property.PropertyStatus;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * This Class implememets the PropertyStatusDAO for the Hibernate specific 
 * Implementation 
 * 
 * @author Neetu
 * @version 2.00
 */

public class PropertyStatusHibernateDAO extends GenericHibernateDAO implements PropertyStatusDAO
{
	/**
	 * @param persistentClass
	 * @param session
	 */
	public PropertyStatusHibernateDAO(Class persistentClass, Session session)
	{
		super(persistentClass, session);
	}
	
	public PropertyStatus getPropertyStatusByName(String name) 
	{
		Query qry = getCurrentSession().createQuery("from PropertyStatus PS where PS.name =:name ");
		qry.setString("name", name);        
		return (PropertyStatus)qry.uniqueResult();
	}
	public PropertyStatus getPropertyStatusByCode(String statusCode)
	{
		Query qry = getCurrentSession().createQuery("from PropertyStatus PS where PS.statusCode =:statusCode ");
		qry.setString("statusCode", statusCode);        
		return (PropertyStatus)qry.uniqueResult();
	}
}
  

