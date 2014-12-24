/*
 * ConstructionTypeSetHibernateDAO.java Created on Oct 5, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.ptis.domain.dao.property;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Session;

/**
 * This Class implememets the ConstructionTypeSetDAO for the Hibernate specific 
 * Implementation 
 * 
 * @author Neetu
 * @version 2.00
 */

public class ConstructionTypeSetHibernateDAO extends GenericHibernateDAO implements ConstructionTypeSetDAO
{
	/**
	 * @param persistentClass
	 * @param session
	 */
	public ConstructionTypeSetHibernateDAO(Class persistentClass, Session session)
	{
		super(persistentClass, session);
	}
}
  

