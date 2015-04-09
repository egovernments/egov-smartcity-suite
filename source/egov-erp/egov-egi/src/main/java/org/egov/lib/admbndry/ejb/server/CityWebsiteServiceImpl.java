/*
 * @(#)CityWebsiteServiceImpl.java 3.0, 11 Jun, 2013 12:19:37 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.admbndry.ejb.server;

import java.util.List;

import org.egov.exceptions.DuplicateElementException;
import org.egov.infra.admin.master.entity.CityWebsite;
import org.egov.lib.admbndry.CityWebsiteDAO;
import org.egov.lib.admbndry.ejb.api.CityWebsiteService;

public class CityWebsiteServiceImpl implements CityWebsiteService {

    private CityWebsiteDAO cityWebsiteDAO;

    public void setCityWebsiteDAO(final CityWebsiteDAO cityWebsiteDAO) {
        this.cityWebsiteDAO = cityWebsiteDAO;
    }

    @Override
    public void create(CityWebsite cityWebsite) throws DuplicateElementException {
        // TODO Auto-generated method stub

    }

    @Override
    public void remove(CityWebsite cityWebsite) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<CityWebsite> getCityWebsite(Integer bndryid) {
        // TODO Auto-generated method stub
        return null;
    }
}