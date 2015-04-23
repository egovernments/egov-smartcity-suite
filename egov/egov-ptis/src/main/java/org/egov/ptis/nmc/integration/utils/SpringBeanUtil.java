package org.egov.ptis.nmc.integration.utils;

import org.egov.infstr.beanfactory.ApplicationContextBeanProvider;
import org.egov.ptis.nmc.util.PropertyTaxNumberGenerator;
import org.egov.ptis.nmc.util.PropertyTaxUtil;
import org.egov.ptis.service.collection.PropertyTaxCollection;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringBeanUtil {

	private SpringBeanUtil() {
	}

	private static ApplicationContextBeanProvider beanProvider = (ApplicationContextBeanProvider) new ClassPathXmlApplicationContext(
			new String[] { "org/egov/infstr/beanfactory/globalApplicationContext.xml",
					"org/egov/infstr/beanfactory/egiApplicationContext.xml",
					"org/egov/infstr/beanfactory/applicationContext-pims.xml",
					"org/egov/infstr/beanfactory/applicationContext-ptis.xml" }).getBean("beanProvider");

	public static PropertyTaxCollection getPropertyTaxCollection() {
		return (PropertyTaxCollection) beanProvider.getBean("propertyTaxCollection");
	}

	public static PropertyTaxUtil getPropertyTaxUtil() {
		return (PropertyTaxUtil) beanProvider.getBean("propertyTaxUtil");
	}

	public static PropertyTaxNumberGenerator getPropertyTaxNumberGenerator() {
		return (PropertyTaxNumberGenerator) beanProvider.getBean("propertyTaxNumberGenerator");
	}

}
