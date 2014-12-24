/*
 * PropertyHibernateDAO.java Created on Oct 5, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.pims.dao;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Session;

/**
 * This Class implememets the PersonalInformationDAO for the Hibernate specific
 * Implementation
 *
 * @author Neetu
 * @version 2.00
 */
public class PayFixedInMasterHibernateDAO extends GenericHibernateDAO implements PayFixedInMasterDAO
{
   
	public PayFixedInMasterHibernateDAO(Class persistentClass, Session session)
	{
		super(persistentClass, session);
	}
}


