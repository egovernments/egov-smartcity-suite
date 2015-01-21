/*
 * @(#)AddressTypeHibernateDAO.java 3.0, 7 Jun, 2013 9:08:35 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.address.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.lib.address.model.AddressTypeMaster;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

public class AddressTypeHibernateDAO extends GenericHibernateDAO implements AddressTypeDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(AddressTypeHibernateDAO.class);
	
	/**
	 * Instantiates a new address type hibernate dao.
	 * @param persistentClass the persistent class
	 * @param session the session
	 */
	public AddressTypeHibernateDAO(Class persistentClass, Session session) {
		super(persistentClass, session);
	}
	
	/* (non-Javadoc)
	 * @see org.egov.lib.address.dao.AddressTypeDAO#getAddressType(java.lang.String)
	 */
	public AddressTypeMaster getAddressType(String addTypeName) {
		try {
			Query qry = getCurrentSession().createQuery("from AddressTypeMaster ATM where ATM.addressTypeName =:addTypeName ");
			qry.setString("addTypeName", addTypeName);
			AddressTypeMaster addtypeMaster = (AddressTypeMaster) qry.uniqueResult();
			return addtypeMaster;
		} catch (HibernateException ex) {
			LOGGER.error("Error occurred in getAddressType",ex);
			throw new EGOVRuntimeException("Error occurred in getAddressType",ex);
		} catch (Exception e) {
			LOGGER.error("Error occurred in getAddressType",e);
			throw new EGOVRuntimeException("Error occurred in getAddressType",e);
		}
		
	}
	
}
