/*
 * @(#)CityWebsiteService.java 3.0, 11 Jun, 2013 11:12:14 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.admbndry.ejb.api;

import java.util.List;

import org.egov.exceptions.DuplicateElementException;
import org.egov.infra.admin.master.entity.CityWebsite;

public interface CityWebsiteService {

	void create(CityWebsite cityWebsite) throws DuplicateElementException;

	List<CityWebsite> getCityWebsite(Integer bndryid);

	void remove(CityWebsite cityWebsite);
}
