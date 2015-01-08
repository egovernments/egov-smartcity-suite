/*
 * @(#)CityWebsiteServiceImpl.java 3.0, 11 Jun, 2013 12:19:37 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.admbndry.ejb.server;

import java.util.List;

import org.egov.exceptions.DuplicateElementException;
import org.egov.lib.admbndry.CityWebsite;
import org.egov.lib.admbndry.CityWebsiteDAO;
import org.egov.lib.admbndry.ejb.api.CityWebsiteService;

public class CityWebsiteServiceImpl implements CityWebsiteService {

	private CityWebsiteDAO cityWebsiteDAO;

	public void setCityWebsiteDAO(final CityWebsiteDAO cityWebsiteDAO) {
		this.cityWebsiteDAO = cityWebsiteDAO;
	}

	@Override
	public void create(final CityWebsite cityWebsite) throws DuplicateElementException {
		this.cityWebsiteDAO.create(cityWebsite);

	}

	@Override
	public List<CityWebsite> getCityWebsite(final Integer bndryid) {
		return this.cityWebsiteDAO.getCityWebsite(bndryid);
	}

	@Override
	public void remove(final CityWebsite cityWebsite) {
		this.cityWebsiteDAO.remove(cityWebsite);

	}
}