package org.egov.pims.empLeave.dao;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.pims.empLeave.model.CompOff;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * This Class implememets the PersonalInformationDAO for the Hibernate specific
 * Implementation
 *

 */
public class CompOffHibernateDAO extends GenericHibernateDAO implements CompOffDAO
{
   
    /*
     *
	 * @param persistentClass
	 * @param session
	 */
	public CompOffHibernateDAO(Class persistentClass, Session session)
	{
		super(persistentClass, session);
	}
	public CompOff getCompOffByID(Integer id)
	{
		Query qry = getSession().createQuery("from CompOff B where B.id =:id ");
		qry.setInteger("id", id);
		return (CompOff)qry.uniqueResult();
	}


}



