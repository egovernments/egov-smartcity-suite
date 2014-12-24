/**
 * 
 */
package org.egov.works.services.serviceOrder.impl;

import java.util.List;

import org.egov.infstr.services.PersistenceService;
import org.egov.works.models.serviceOrder.ServiceTemplate;
import org.egov.works.services.impl.BaseServiceImpl;
import org.egov.works.services.serviceOrder.ServiceTemplateService;

/**
 * @author manoranjan
 *
 */
public class ServiceTemplateServiceImpl extends BaseServiceImpl<ServiceTemplate, Long> implements ServiceTemplateService {

	public ServiceTemplateServiceImpl(PersistenceService<ServiceTemplate, Long> persistenceService) {
		super(persistenceService);
		
	}

	public List<ServiceTemplate> getAllActiveServiceTemplate(){
		
		return findAllByNamedQuery("getAllActiveServiceTemplates");
	}

	
}
