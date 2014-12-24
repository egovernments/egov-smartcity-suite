/*
 * EgRemittanceGldtlHibernateDAO.java Created on Oct 10, 2007
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.deduction.dao;

import org.apache.log4j.Logger;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Session;

/**
 * TODO Brief Description of the purpose of the class/interface
 * 
 * @author Sathish
 * @version 1.00 
 */

public class EgRemittanceGldtlHibernateDAO extends GenericHibernateDAO
{
	public EgRemittanceGldtlHibernateDAO(Class persistentClass, Session session)
	{
		super(persistentClass, session);
	}
}

