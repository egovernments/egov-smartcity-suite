/*
 * PropertyHibernateDAO.java Created on Oct 5, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.pims.dao;

import org.apache.log4j.Logger;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.pims.model.MovablePropDetails;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * This Class implememets the PersonalInformationDAO for the Hibernate specific
 * Implementation
 *
 * @author Neetu
 * @version 2.00
 */
public class MovablePropDetailsHibernateDAO extends GenericHibernateDAO implements MovablePropDetailsDAO
{
    
    /*
     *
	 * @param persistentClass
	 * @param session
	 */
	public MovablePropDetailsHibernateDAO(Class persistentClass, Session session)
	{
		super(persistentClass, session);
	}
	public MovablePropDetails getMovablePropDetailsByID(Integer id)
	{
		Query qry = getSession().createQuery("from MovablePropDetails B where B.movPropertyDetailsId =:id ");
		qry.setInteger("id", id);
		return (MovablePropDetails)qry.uniqueResult();
	}
}


