/*
 * PropertyHibernateDAO.java Created on Oct 5, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.pims.dao;

import org.apache.log4j.Logger;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.pims.model.EduDetails;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * This Class implememets the EgpimsEduDetailsInformationDAO for the Hibernate specific
 * Implementation
 *
 * @author Neetu
 * @version 2.00
 */
public class EduDetailsHibernateDAO extends GenericHibernateDAO implements EduDetailsDAO
{
   /*
     *
	 * @param persistentClass
	 * @param session
	 */
	public EduDetailsHibernateDAO(Class persistentClass, Session session)
	{
		super(persistentClass, session);
	}
	public EduDetails getEduDetailsByID(Integer id)
	{
		Query qry = getSession().createQuery("from EduDetails B where B.educationDetailsId =:id ");
	
		qry.setInteger("id", id);
		return (EduDetails)qry.uniqueResult();
	}
}


