/*
 * Created on Sep 8, 2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.egov.masters.dao;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Session;

/**
 * @author Sathish P
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AccountEntityHibernateDAO extends GenericHibernateDAO
{

	public AccountEntityHibernateDAO(Class persistentClass, Session session)
	{
			super(persistentClass, session);
	}
}