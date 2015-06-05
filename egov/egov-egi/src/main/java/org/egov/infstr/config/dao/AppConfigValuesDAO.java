/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.infstr.config.dao;

import java.util.Date;
import java.util.List;

import org.egov.infra.admin.master.entity.AppConfig;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infstr.dao.GenericDAO;

public interface AppConfigValuesDAO extends GenericDAO<AppConfigValues, Integer> {

	/**
	 * Gets the App config values by moduleName and keyName.
	 * @param moduleName the module name
	 * @param keyName the key name
	 * @return the AppConfigValues by module and key
	 */
	List<AppConfigValues> getConfigValuesByModuleAndKey(String moduleName, String keyName);

	/**
	 * Creates the app config value.
	 * @param appconfigvalues the new app config values
	 * @return the AppConfigValues
	 */
	AppConfigValues createAppConfigValues(AppConfigValues appconfigvalues);

	/**
	 * Gets the app config keys.
	 * @param moduleName the module name
	 * @return the AppConfig
	 */
	List<AppConfig> getAppConfigKeys(String moduleName);

	/**
	 * Gets the AppConfig object by keyName and moduleName.
	 * @param keyName the key name
	 * @param moduleName the module name
	 * @return the AppConfig
	 */
	AppConfig getConfigKeyByName(String keyName, String moduleName);

	/**
	 * Gets the AppConfigValues by moduleName, keyName and effectiveFrom date passed.
	 * @param moduleName the module name
	 * @param keyName the key name
	 * @param date the date
	 * @return the AppConfigValues
	 */
	AppConfigValues getAppConfigValueByDate(String moduleName, String keyName, Date effectiveFrom);

	/**
	 * Gets all the AppConfigValues by moduleName, keyName and effectiveFrom date passed.
	 * @param String the module name
	 * @param String the key name
	 * @param Date the effective from date
	 * @return the AppConfigValues list
	 */
	List<AppConfigValues> getAppConfigValues(String moduleName, String keyName, Date effectiveFrom);

	/**
	 * Gets the all app config module names.
	 * @return the all app config module names
	 */
	List<String> getAllAppConfigModule();
}
