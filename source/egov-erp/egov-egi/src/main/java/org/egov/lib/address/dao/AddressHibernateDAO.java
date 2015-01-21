/*
 * @(#)AddressHibernateDAO.java 3.0, 7 Jun, 2013 9:00:29 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.address.dao;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.lib.address.model.Address;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddressHibernateDAO extends GenericHibernateDAO implements AddressDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(AddressHibernateDAO.class);

	/**
	 * @param persistentClass
	 * @param session
	 */
	public AddressHibernateDAO(Class persistentClass, Session session) {
		super(persistentClass, session);

	}

	public Address getAddress(Integer addressID) {
		try {
			Query qry = getCurrentSession().createQuery("from Address AM where AM.addressID =:addressID ");
			qry.setInteger("addressID", addressID);
			return (Address) qry.uniqueResult();
		} catch (HibernateException ex) {
			LOGGER.error("Error occurred in getAddressType", ex);
			throw new EGOVRuntimeException("Error occurred in getAddressType", ex);
		} catch (Exception e) {
			LOGGER.error("Error occurred in getAddressType", e);
			throw new EGOVRuntimeException("Error occurred in getAddressType", e);

		}

	}

}
