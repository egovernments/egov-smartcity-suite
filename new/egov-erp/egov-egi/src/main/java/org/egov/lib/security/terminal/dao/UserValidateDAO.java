/*
 * @(#)UserValidateDAO.java 3.0, 14 Jun, 2013 3:24:19 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.security.terminal.dao;

import org.egov.lib.security.terminal.model.Location;
import org.egov.lib.security.terminal.model.UserValidate;

public interface UserValidateDAO {

	boolean validateUser(UserValidate obj);

	boolean validateUserLocation(UserValidate obj);

	boolean validateUserTerminal(UserValidate obj);

	Location getLocationByIP(String ipAddress);

	Location getTerminalByIP(String ipAddress);

	boolean validateActiveUserForPeriod(String userName);
}
