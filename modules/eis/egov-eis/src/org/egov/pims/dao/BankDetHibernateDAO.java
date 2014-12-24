/*
 * PropertyHibernateDAO.java Created on Oct 5, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.pims.dao;

import java.math.BigDecimal;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.pims.model.BankDet;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * This Class implememets the PersonalInformationDAO for the Hibernate specific
 * Implementation
 *
 * @author Neetu
 * @version 2.00
 */
public class BankDetHibernateDAO extends GenericHibernateDAO implements BankDetDAO
{
    
    /*
     *
	 * @param persistentClass
	 * @param session
	 */
	public BankDetHibernateDAO(Class persistentClass, Session session)
	{
		super(persistentClass, session);
	}
	public BankDet getBankDetByID(BigDecimal id)
	{
		Query qry = getSession().createQuery("from BankDet B where B.id =:id ");
		qry.setBigDecimal("id", id);
		return (BankDet)qry.uniqueResult();
	}
}


