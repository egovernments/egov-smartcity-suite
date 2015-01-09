package org.egov.infstr.junit.utils;

import org.egov.exceptions.EGOVRuntimeException;
import org.infinispan.manager.DefaultCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;

public class TestUtils {
	private static SimpleNamingContextBuilder builder;
	
	public static void activateInitialContext() {
		if (builder == null) {
			try {
				builder = new SimpleNamingContextBuilder();
				ApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[] { "classpath:appContextTest.xml"});
				builder.bind("java:jcrDatabasePool",applicationContext.getBean("oracleDS"));
				builder.bind("java:jboss/infinispan/container/master-data",new DefaultCacheManager(false));
				builder.activate();
			} catch (Exception e) {
				throw new EGOVRuntimeException("Error occurred while activating InitialContext",e);
			}
		}
	}
}
