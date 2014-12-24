package org.egov.assets.service.impl;

import org.egov.assets.model.AssetRevaluation;
import org.egov.assets.service.AssetRevaluationService;
import org.egov.infstr.services.PersistenceService;

public class AssetRevaluationImpl extends BaseServiceImpl<AssetRevaluation, Long> implements AssetRevaluationService{
	
	public AssetRevaluationImpl(PersistenceService<AssetRevaluation, Long> persistenceService) {
		super(persistenceService);
	}
}
