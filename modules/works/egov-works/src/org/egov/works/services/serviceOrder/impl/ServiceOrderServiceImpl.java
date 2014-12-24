/**
 * 
 */
package org.egov.works.services.serviceOrder.impl;

import org.egov.infstr.services.PersistenceService;
import org.egov.works.models.serviceOrder.ServiceOrder;
import org.egov.works.services.impl.BaseServiceImpl;
import org.egov.works.services.serviceOrder.ServiceOrderService;

/**
 * @author manoranjan
 *
 */
public class ServiceOrderServiceImpl extends BaseServiceImpl<ServiceOrder, Long> implements ServiceOrderService {
	
	public ServiceOrderServiceImpl(PersistenceService<ServiceOrder, Long> persistenceService) {
		super(persistenceService);
		
	}

	
}