/*
 * Created on May 10, 2007
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.ptis.domain.dao.property;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Session;

/**
 * This Class implememets the PropertyModifyReasonsDAO for the Hibernate specific 
 * Implementation 
 * 
 * @author Ramakrishna
 * @version 2.00
 */
public class PropertyModifyReasonsHibernateDAO extends GenericHibernateDAO implements PropertyModifyReasonsDAO
{
	public PropertyModifyReasonsHibernateDAO(Class persistentClass, Session session)
	{
		super(persistentClass, session);
	}

}
