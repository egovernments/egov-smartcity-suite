/*
 * PropertyHibernateDAO.java Created on Oct 5, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.pims.dao;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.pims.model.LangKnown;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * This Class implememets the PersonalInformationDAO for the Hibernate specific
 * Implementation
 *
 * @author Neetu
 * @version 2.00
 */
public class LangKnownHibernateDAO extends GenericHibernateDAO implements LangKnownDAO
{
    
    /*
     *
	 * @param persistentClass
	 * @param session
	 */
	public LangKnownHibernateDAO(Class persistentClass, Session session)
	{
		super(persistentClass, session);
	}
	public LangKnown getLangKnownByID(Integer id)
	{
		Query qry = getSession().createQuery("from LangKnown B where B.id =:id ");
		qry.setInteger("id", id);
		return (LangKnown)qry.uniqueResult();
	}
}


