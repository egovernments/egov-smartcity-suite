/*
 * PropertyHibernateDAO.java Created on Oct 5, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.pims.dao;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.pims.model.ImmovablePropDetails;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * This Class implememets the PersonalInformationDAO for the Hibernate specific
 * Implementation
 *
 * @author Neetu
 * @version 2.00
 */
public class ImmovablePropDetailsHibernateDAO extends GenericHibernateDAO implements ImmovablePropDetailsDAO
{
    
    /*
     *
	 * @param persistentClass
	 * @param session
	 */
	public ImmovablePropDetailsHibernateDAO(Class persistentClass, Session session)
	{
		super(persistentClass, session);
	}
	public ImmovablePropDetails getImmovablePropDetailsByID(Integer id)
	{
		Query qry = getSession().createQuery("from ImmovablePropDetails B where B.immPropertyDetailsId =:id ");
		qry.setInteger("id", id);
		return (ImmovablePropDetails)qry.uniqueResult();
	}
}


