/*
 * @(#)GenericCommonsManagerBean.java 3.0, 17 Jun, 2013 11:24:16 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.commons.service;

import java.util.List;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.commons.Module;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;

public class GenericCommonsServiceImpl implements GenericCommonsService {

	private GenericHibernateDaoFactory genericHibernateDaoFactory;

	public GenericCommonsServiceImpl(GenericHibernateDaoFactory genericHibernateDaoFactory) {
		this.genericHibernateDaoFactory = genericHibernateDaoFactory;
	}

	@Override
	public Module getModuleByID(final Integer id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void createModule(final Module module) {
	    throw new UnsupportedOperationException();
	}

	@Override
	public void deleteModule(final Module module) {
	    throw new UnsupportedOperationException();
	}

	@Override
	public void updateModule(final Module module) {
	    throw new UnsupportedOperationException();
	}

	@Override
	public Module getModuleByName(final String moduleName) {
	    throw new UnsupportedOperationException();
	}

	@Override
	public List<Module> getAllModules() {
	    throw new UnsupportedOperationException();

	}

	
}
