/*
 * PropertyHibernateDAO.java Created on Oct 5, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.pims.dao;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.pims.model.AssignmentPrd;
import org.hibernate.Query;
import org.hibernate.Session;


/**
 * This Class implememets the DeptTestsDAO for the Hibernate specific
 * Implementation
 *
 * @author Neetu
 * @version 2.00
 */
public class AssignmentPrdHibernateDAO extends GenericHibernateDAO implements AssignmentPrdDAO
{
    
    /*
     *
	 * @param persistentClass
	 * @param session
	 */
	public AssignmentPrdHibernateDAO(Class persistentClass, Session session)
	{
		super(persistentClass, session);
	}
	public AssignmentPrd getAssignmentPrdById(Integer id)
	{
		Query qry = getSession().createQuery("from AssignmentPrd E where E.id =:id ");
		return (AssignmentPrd)getSession().get(AssignmentPrd.class, id);
		//return (AssignmentPrd)getSession().load(AssignmentPrd.class, id);
		//qry.setInteger("id", id);
		//System.out.println(qry.getQueryString());
		//return (AssignmentPrd)qry.uniqueResult();
	}
	
	
	

}


