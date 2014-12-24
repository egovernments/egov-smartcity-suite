/*
 * PropertyCreationReasonsHibernateDAO Created on Oct 5, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.ptis.domain.dao.property;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Session;

/**
 * This Class implememets the PropertyCreationReasonsDAO for the Hibernate specific 
 * Implementation 
 * 
 * @author Neetu
 * @version 2.00
 */

public class PropertyCreationReasonsHibernateDAO extends GenericHibernateDAO implements PropertyCreationReasonsDAO
{
	/**
	 * @param persistentClass
	 * @param session
	 */
	public PropertyCreationReasonsHibernateDAO(Class persistentClass, Session session)
	{
		super(persistentClass, session);
	}
}
  

