package org.egov.billsaccounting.dao;
/*
 * Created on Feb 16, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.billsaccounting.model.EgwWorksMis;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * @author Admin
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EgwWorksMisHibernateDAO extends GenericHibernateDAO
{

	public EgwWorksMisHibernateDAO(final Class persistentClass,final  Session session)
	{
			super(persistentClass, session);
	}
     public EgwWorksMis findByWorkOrderId(final String workOrderId)
     {
        final Query qry = getSession().createQuery("from EgwWorksMis ewm where ewm.worksdetail =:workOrderId");
         qry.setString("workOrderId",workOrderId);           
         return (EgwWorksMis)qry.uniqueResult();
     } 
}
