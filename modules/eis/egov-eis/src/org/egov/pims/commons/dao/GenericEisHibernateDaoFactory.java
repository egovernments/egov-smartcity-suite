/*
 * Created on Oct 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.egov.pims.commons.dao;

import org.egov.infstr.utils.HibernateUtil;
import org.egov.pims.commons.PositionHeirarchy;
import org.hibernate.Session;

/**
 * @author Venkatesh.M.J
 * @version 2.0
 * @since 2.0
*/

public class GenericEisHibernateDaoFactory extends GenericEisDaoFactory {

	/**
	 *
	 */
	public GenericEisHibernateDaoFactory() {
		super();
		// TODO Auto-generated constructor stub
	}

	   protected Session getCurrentSession()
	   {
        // Get a Session and begin a database transaction. If the current
        // thread/EJB already has an open Sessio n and an ongoing Transaction,
        // this is a no-op and only returns a reference to the current Session.
        HibernateUtil.beginTransaction();
        return HibernateUtil.getCurrentSession();
	   }

	
	/* (non-Javadoc)
	 * @see org.egov.infstr.commons.dao.GenericDaoFactory#getModuleDao()
	 */
	

	
	public  PositionHeirarchyDAO getPositionHeirarchyDAO()
	{
		return new PositionHeirarchyHibernateDAO(PositionHeirarchy.class,getCurrentSession());
	}
	
	
}
