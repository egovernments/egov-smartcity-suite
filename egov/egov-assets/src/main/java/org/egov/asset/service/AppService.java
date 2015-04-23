package org.egov.asset.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfigValues;

public class AppService {
	private static final Logger logger = Logger.getLogger(AppService.class);
	private GenericHibernateDaoFactory genericHibDao;
	
	
	/**
	 * This method will return the value in AppConfigValue table for the given module and key. 
	 * @param moduleName
	 * @param key
	 * @return
	 */
	public List<AppConfigValues> getAppConfigValue(String moduleName,String key){
		return genericHibDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(
				moduleName, key);
	}
	
	/**
	 * This method assumes that there will be one value for module and key.
	 * Return value in app config if found, otherwise return null.
	 * @param moduleName
	 * @param key
	 * @return
	 */
	public String getUniqueAppConfigValue(String moduleName,String key){
		List<AppConfigValues> resultList = getAppConfigValue(moduleName, key);
		logger.info("Found AppConfig "+resultList.get(0));
		return resultList.get(0).getValue();
	}
	
	
	/**
	 * This method assumes that there will be one value for Asset module.
	 * Return value in app config if found, otherwise return null.
	 * @param moduleName
	 * @param key
	 * @return
	 */
	public String getUniqueAppConfigValue(String key){
		return getUniqueAppConfigValue("Assets", key);
	}
	

	// Spring Injection
	public void setGenericHibDao(GenericHibernateDaoFactory genericHibDao) {
		this.genericHibDao = genericHibDao;
	}

}