/*
 * Created on Sep 9, 2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.egov.masters.dao;

import org.egov.commons.Accountdetailtype;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * @author Sathish P
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AccountdetailtypeHibernateDAO extends GenericHibernateDAO
{
	public AccountdetailtypeHibernateDAO(Class persistentClass, Session session)
	{
			super(persistentClass, session);
	}
	
	public Accountdetailtype getAccountdetailtypeByName(String name)
	{
		Query qry =HibernateUtil.getCurrentSession().createQuery("from Accountdetailtype where name =:name");
		qry.setString("name", name);
		return (Accountdetailtype)qry.uniqueResult();
	}
}