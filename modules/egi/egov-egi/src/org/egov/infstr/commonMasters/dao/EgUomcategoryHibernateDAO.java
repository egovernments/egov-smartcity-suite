/*
 * @(#)EgUomcategoryHibernateDAO.java 3.0, 17 Jun, 2013 11:19:07 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.commonMasters.dao;

import org.egov.infstr.commonMasters.EgUomcategory;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Session;

public class EgUomcategoryHibernateDAO extends GenericHibernateDAO {
	public EgUomcategoryHibernateDAO() {
		super(EgUomcategory.class, null);
	}

	public EgUomcategoryHibernateDAO(final Class persistentClass, final Session session) {
		super(persistentClass, session);
	}

}
