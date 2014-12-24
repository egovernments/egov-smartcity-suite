/*
 * @(#)UserCounterDAO.java 3.0, 14 Jun, 2013 3:19:23 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.security.terminal.dao;

import java.util.Date;
import java.util.List;

import org.egov.infstr.dao.GenericDAO;
import org.egov.lib.security.terminal.model.UserCounterMap;

public interface UserCounterDAO extends GenericDAO<UserCounterMap, Integer> {

	void deleteCounters(int counterId);

	List<UserCounterMap> getLocationBasedUserCounterMapForCurrentDate(Integer locId);

	List<UserCounterMap> getTerminalBasedUserCounterMapForCurrentDate(Integer locId);

	boolean checkUserCounter(Integer userId, Date fromDate, Date toDate);

	List<UserCounterMap> getUserCounterMapForLocationId(Integer Id);

	List<UserCounterMap> getUserCounterMapForTerminalId(Integer Id);

	List<UserCounterMap> getUserCounterMapForUserId(Integer Id);
}
