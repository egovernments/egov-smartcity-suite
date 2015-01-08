/*
 * @(#)GenericCommonsManagerBean.java 3.0, 17 Jun, 2013 11:24:16 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.commons.service;

import java.util.List;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.commons.Module;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.lib.address.dao.AddressTypeDAO;
import org.egov.lib.address.model.AddressTypeMaster;

public class GenericCommonsServiceImpl implements GenericCommonsService {

	@Override
	public Module getModuleByID(final Integer id) {
		try {
			return (Module) GenericDaoFactory.getDAOFactory().getModuleDao().findById(id, false);
		} catch (final Exception e) {
			throw new EGOVRuntimeException("Exception in searching Module." + e.getMessage(), e);
		}
	}

	@Override
	public void createModule(final Module module) {
		try {
			GenericDaoFactory.getDAOFactory().getModuleDao().create(module);
		} catch (final Exception e) {
			throw new EGOVRuntimeException("Exception in Creating Module." + e.getMessage(), e);
		}
	}

	@Override
	public void deleteModule(final Module module) {
		try {
			GenericDaoFactory.getDAOFactory().getModuleDao().delete(module);
		} catch (final Exception e) {
			throw new EGOVRuntimeException("Exception in Deleting Module." + e.getMessage(), e);
		}
	}

	@Override
	public void updateModule(final Module module) {
		try {
			GenericDaoFactory.getDAOFactory().getModuleDao().update(module);
		} catch (final Exception e) {
			throw new EGOVRuntimeException("Exception in Updating Module." + e.getMessage(), e);
		}
	}

	@Override
	public Module getModuleByName(final String moduleName) {
		try {
			return GenericDaoFactory.getDAOFactory().getModuleDao().getModuleByName(moduleName);
		} catch (final Exception e) {
			throw new EGOVRuntimeException("Exception in searching Module." + e.getMessage(), e);
		}
	}

	@Override
	public List<Module> getAllModules() {
		try {
			return GenericDaoFactory.getDAOFactory().getModuleDao().findAll();
		} catch (final Exception e) {
			throw new EGOVRuntimeException("Exception in finding all Modules." + e.getMessage(), e);
		}

	}

	@Override
	public AddressTypeMaster getAddressType(final String addrTypeName) {
		try {
			final AddressTypeDAO addTypDao = GenericDaoFactory.getDAOFactory().getAddressTypeDao();

			return addTypDao.getAddressType(addrTypeName);
		} catch (final Exception e) {
			throw new EGOVRuntimeException("Exception in searching Transaction by voucher number." + e.getMessage(), e);
		}

	}
}
