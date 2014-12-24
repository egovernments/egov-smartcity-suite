package org.egov.infstr.config.dao;

import java.util.Date;
import java.util.List;

import org.egov.infstr.config.AppConfig;
import org.egov.infstr.config.AppConfigValues;
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
