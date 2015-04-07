/*
 * @(#)HeirarchyTypeManagerBean.java 3.0, 11 Jun, 2013 12:21:54 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.admbndry.ejb.server;

import java.util.NoSuchElementException;
import java.util.Set;

import org.egov.exceptions.DuplicateElementException;
import org.egov.exceptions.NoSuchObjectException;
import org.egov.exceptions.TooManyValuesException;
import org.egov.infra.admin.master.entity.HierarchyType;
import org.egov.lib.admbndry.HeirarchyTypeDAO;
import org.egov.lib.admbndry.ejb.api.HeirarchyTypeService;

public class HeirarchyTypeServiceImpl implements HeirarchyTypeService {

	private HeirarchyTypeDAO heirarchyTypeDAO;

	public void setHeirarchyTypeDAO(final HeirarchyTypeDAO heirarchyTypeDAO) {
		this.heirarchyTypeDAO = heirarchyTypeDAO;
	}

	@Override
	public void create(final HierarchyType heirarchyType) throws DuplicateElementException {
		this.heirarchyTypeDAO.create(heirarchyType);

	}

	@Override
	public void update(final HierarchyType heirarchyType) throws NoSuchElementException {
		this.heirarchyTypeDAO.update(heirarchyType);

	}

	@Override
	public void remove(final HierarchyType heirarchyType) throws NoSuchElementException {
		this.heirarchyTypeDAO.remove(heirarchyType);
	}

	@Override
	public HierarchyType getHeirarchyTypeByID(final int heirarchyTypeId) {
		return this.heirarchyTypeDAO.getHeirarchyTypeByID(heirarchyTypeId);
	}

	@Override
	public Set<HierarchyType> getAllHeirarchyTypes() {
		return this.heirarchyTypeDAO.getAllHeirarchyTypes();
	}

	@Override
	public HierarchyType getHierarchyTypeByName(final String name) throws NoSuchObjectException, TooManyValuesException {
		return this.heirarchyTypeDAO.getHierarchyTypeByName(name);
	}
}
