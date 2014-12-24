/*
 * ExceptionMstrHibernateDAO.java Created on Aug 29, 2007
 *
 * Copyright 2007 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
 package org.egov.payroll.dao;

import java.util.ArrayList;
import java.util.List;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * This Class implememets the SalaryCodesDAO for the Hibernate specific
 * Implementation
 *
 * @author Lokesh
 * @version 2.00
 */

public class ExceptionMstrHibernateDAO extends GenericHibernateDAO implements ExceptionMstrDAO {

    
    public ExceptionMstrHibernateDAO(Class persistentClass, Session session)
    {
        super(persistentClass, session);
    }
    
    public List getAllDistinctTypeExceptionMstr(){
    	List e = new ArrayList();    	
    	Query qry = getSession().createQuery("select distinct(em.type) from ExceptionMstr em");    	
    	e = qry.list();
    	return e;
    }

}

