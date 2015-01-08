/*
 * @(#)LocationDAO.java 3.0, 14 Jun, 2013 3:13:40 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.security.terminal.dao;

import java.util.ArrayList;

import org.egov.infstr.dao.GenericDAO;
import org.egov.lib.security.terminal.model.Location;

public interface LocationDAO extends GenericDAO {

	/**
	 * Gets the location id by location name and counter.
	 * @param locationName the location name
	 * @param counterName the counter name
	 * @return the location id by location name and counter
	 */
	Location getLocationIdByLocationNameAndCounter(String locationName, String counterName);

	/**
	 * Gets the counters by location.
	 * @param locationId the location id
	 * @return the counters by location
	 */
	ArrayList<Location> getCountersByLocation(int locationId);

	/**
	 * Check ip address.
	 * @param ipValue the ip value
	 * @return true, if successful
	 */
	boolean checkIPAddress(String ipValue);

	/**
	 * Check counter.
	 * @param ipValue the ip value
	 * @return true, if successful
	 */
	boolean checkCounter(String ipValue);

}
