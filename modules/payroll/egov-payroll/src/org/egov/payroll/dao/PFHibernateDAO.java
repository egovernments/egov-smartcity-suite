package org.egov.payroll.dao;

import java.util.List;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * @author Jagadeesan
 *
 */
public class PFHibernateDAO extends GenericHibernateDAO  implements PFDAO
{

	/**
	 * @param persistentClass
	 * @param session
	 */
	public PFHibernateDAO(Class persistentClass, Session session) 
	{
		super(persistentClass, session);
	}

	public List getPFHeaderInfo()
	{
		Query qry = getSession().createQuery(" from PFHeader where pfType='GPF' ");
		return qry.list();
	}
	public List getPFDetails()
	{
		Query qry = getSession().createQuery(" from PFDetails pfd ");
		return qry.list();
	}
	public void deleteAllPFDetails(Integer pfHeaderId)
	{
		Query qry = getSession().createQuery(" delete from PFDetails pfd where pfd.pfHeaderId=:pfHeaderId");
		qry.setInteger("pfHeaderId", pfHeaderId);
		qry.executeUpdate();
	}
}

