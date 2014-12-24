package org.egov.dcb.service;

import org.egov.erpcollection.integration.services.CollectionIntegrationService;
import org.egov.infstr.beanfactory.ApplicationContextBeanProvider;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class EgovSpringBeanDefinition {

    private EgovSpringBeanDefinition() {
    }
    
    private static ApplicationContextBeanProvider beanProvider = (ApplicationContextBeanProvider) 
        new ClassPathXmlApplicationContext(
            new String[] { "org/egov/infstr/beanfactory/globalApplicationContext.xml",
                    "org/egov/infstr/beanfactory/egiApplicationContext.xml",
                    "org/egov/infstr/beanfactory/applicationContext-pims.xml",
                    "org/egov/infstr/beanfactory/applicationContext-egf.xml",
                    "org/egov/infstr/beanfactory/applicationContext-erpcollections.xml" })
            .getBean("beanProvider");

	public static CollectionIntegrationService getCollectionIntegrationService() {
		return (CollectionIntegrationService)
		    beanProvider.getBean("collectionIntegrationService");
	}
	
}
