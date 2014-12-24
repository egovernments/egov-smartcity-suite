/**
 * 
 */
package org.egov.works.services.serviceOrder;


import java.util.List;

import org.egov.works.models.serviceOrder.ServiceTemplate;
import org.egov.works.services.BaseService;

/**
 * @author manoranjan
 *
 */
public interface ServiceTemplateService extends BaseService<ServiceTemplate, Long> {
	
	/**
	 * 
	 * @return
	 */
	public List<ServiceTemplate> getAllActiveServiceTemplate();

}
