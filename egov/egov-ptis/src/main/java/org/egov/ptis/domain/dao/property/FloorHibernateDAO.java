/*
 * FloorHibernateDAO.java Created on Oct 5, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.ptis.domain.dao.property;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Session;

/**
 * This Class implememets the FloorDAO for the Hibernate specific 
 * Implementation 
 * 
 * @author Neetu
 * @version 2.00
 */

public class FloorHibernateDAO extends GenericHibernateDAO implements FloorDAO
{
	/**
	 * @param persistentClass
	 * @param session
	 */
	public FloorHibernateDAO(Class persistentClass, Session session)
	{
		super(persistentClass, session);
	}
}
  

