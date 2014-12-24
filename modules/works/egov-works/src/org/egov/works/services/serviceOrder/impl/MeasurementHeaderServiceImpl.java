/**
 * 
 */
package org.egov.works.services.serviceOrder.impl;

import org.egov.infstr.services.PersistenceService;
import org.egov.works.models.serviceOrder.SoMeasurementHeader;
import org.egov.works.services.impl.BaseServiceImpl;
import org.egov.works.services.serviceOrder.MeasurementHeaderService;

/**
 * @author manoranjan
 *
 */
public class MeasurementHeaderServiceImpl extends BaseServiceImpl<SoMeasurementHeader, Long> 
										implements MeasurementHeaderService {

	public MeasurementHeaderServiceImpl(PersistenceService<SoMeasurementHeader, Long> persistenceService) {
		super(persistenceService);
		
	}

}
