/*
 * @(#)AppDataDAO.java 3.0, 17 Jun, 2013 11:30:31 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.config.dao;

import org.egov.infstr.config.AppData;
import org.egov.infstr.dao.GenericDAO;

public interface AppDataDAO extends GenericDAO {
	public AppData updateAppDataValue(AppData appData);

	public abstract AppData getValueByModuleAndKey(String moduleName, String key);
}
