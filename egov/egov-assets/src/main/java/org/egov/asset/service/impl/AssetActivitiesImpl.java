package org.egov.asset.service.impl;

import org.egov.asset.model.AssetActivities;
import org.egov.asset.service.AssetActivitiesService;
import org.egov.infstr.services.PersistenceService;

public class AssetActivitiesImpl extends BaseServiceImpl<AssetActivities, Long> implements AssetActivitiesService{

	public AssetActivitiesImpl(PersistenceService<AssetActivities, Long> persistenceService) {
		super(persistenceService);
	}

}
