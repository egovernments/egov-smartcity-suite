/*
 * @(#)GenericDaoFactory.java 3.0, 17 Jun, 2013 11:24:59 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.commons.dao;

import org.egov.infstr.config.dao.AppConfigValuesDAO;
import org.egov.infstr.config.dao.AppDataDAO;
import org.egov.infstr.utils.EGovConfig;
import org.egov.lib.address.dao.AddressDAO;
import org.egov.lib.address.dao.AddressTypeDAO;

public abstract class GenericDaoFactory {

	private static final GenericDaoFactory EJB3_PERSISTENCE = null;
	private static final GenericDaoFactory HIBERNATE = new GenericHibernateDaoFactory();
	private static final GenericDaoFactory retFac = resolveDAOFactory();

	public static GenericDaoFactory getDAOFactory() {
		return retFac;
	}

	private static GenericDaoFactory resolveDAOFactory() {
		final String method = EGovConfig.getProperty("COMMONS-FACTORY-IMPL", "HIBERNATE", "PTIS");
		if (method != null) {
			if (method.trim().equalsIgnoreCase("HIBERNATE")) {
				return HIBERNATE;
			} else {
				return EJB3_PERSISTENCE;
			}
		}
		return null;
	}

	public abstract ModuleDao getModuleDao();

	public abstract AddressTypeDAO getAddressTypeDao();

	public abstract AddressDAO getAddressDao();

	public abstract AppConfigValuesDAO getAppConfigValuesDAO();

	public abstract AppDataDAO getAppDataDAO();

}
