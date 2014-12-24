/*
 * PropertyHibernateDAO.java Created on Oct 5, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.pims.dao;

import org.apache.log4j.Logger;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.pims.model.DeptTests;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * This Class implememets the DeptTestsDAO for the Hibernate specific
 * Implementation
 *
 * @author Neetu
 * @version 2.00
 */
public class DeptTestsHibernateDAO extends GenericHibernateDAO implements DeptTestsDAO
{
    
    /*
     *
	 * @param persistentClass
	 * @param session
	 */
	public DeptTestsHibernateDAO(Class persistentClass, Session session)
	{
		super(persistentClass, session);
	}
	public DeptTests getDeptTestsByID(Integer deptTestsId)
	{
		Query qry = getSession().createQuery("from DeptTests E where E.deptTestsId =:deptTestsId ");
		qry.setInteger("deptTestsId", deptTestsId);
		return (DeptTests)qry.uniqueResult();
	}
}


