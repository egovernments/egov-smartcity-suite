/*
 * EarningsHibernateDAO.java.java Created on Aug 29, 2007
 *
 * Copyright 2007 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
 package org.egov.payroll.dao;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Session;

/**
 * This Class implememets the SalaryCodesDAO for the Hibernate specific
 * Implementation
 *
 * @author Lokesh
 * @version 2.00
 */

public class EarningsHibernateDAO extends GenericHibernateDAO implements EarningsDAO {

    
    public EarningsHibernateDAO(Class persistentClass, Session session)
    {
        super(persistentClass, session);
    }
    

}

