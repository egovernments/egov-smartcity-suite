/*
 * @(#)AppConfigTagUtil.java 3.0, 10 Jun, 2013 11:25:00 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.utils;

import javax.servlet.ServletContext;

import org.egov.infstr.config.dao.AppConfigValuesHibernateDAO;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class AppConfigTagUtil {
	public static String getAppConfigValue(String key, String moduleName, ServletContext servletContext) {
		AppConfigValuesHibernateDAO appConfigValue = (AppConfigValuesHibernateDAO) WebApplicationContextUtils.getWebApplicationContext(servletContext).getBean("appConfigValuesDAO");
		return appConfigValue.getConfigValuesByModuleAndKey(moduleName, key).get(0).getValue();
	}
}
