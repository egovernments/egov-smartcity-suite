/*
 * PropertyHibernateDAO.java Created on Oct 5, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.pims.dao;

import org.apache.log4j.Logger;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.pims.model.Regularisation;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * This Class implememets the PersonalInformationDAO for the Hibernate specific
 * Implementation
 *
 * @author Neetu
 * @version 2.00
 */
public class RegularisationHibernateDAO extends GenericHibernateDAO implements RegularisationDAO
{
   
    /*
     *
	 * @param persistentClass
	 * @param session
	 */
	public RegularisationHibernateDAO(Class persistentClass, Session session)
	{
		super(persistentClass, session);
	}
	public Regularisation getRegularisationByID(Integer id)
	{
		Query qry = getSession().createQuery("from Regularisation B where B.regularisationId =:id ");
		qry.setInteger("id", id);
		return (Regularisation)qry.uniqueResult();
	}
}


