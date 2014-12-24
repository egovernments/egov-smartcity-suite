/*
 * PayScaleDetailsHibernateDAO.java Created on Aug 29, 2007
 *
 * Copyright 2007 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.payroll.dao;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Session;

/**
 * This Class implememets the PayScaleDetailsHibernateDAO for the Hibernate
 * specific Implementation
 * 
 * @author Lokesh
 * @version 2.00
 */

public class PayScaleDetailsHibernateDAO extends GenericHibernateDAO implements
		PayScaleDetailsDAO {

	public PayScaleDetailsHibernateDAO(Class persistentClass, Session session) {
		super(persistentClass, session);
	}
	
}
