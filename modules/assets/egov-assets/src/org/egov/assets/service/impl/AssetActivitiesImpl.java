package org.egov.assets.service.impl;

import org.egov.assets.model.AssetActivities;
import org.egov.assets.service.AssetActivitiesService;
import org.egov.infstr.services.PersistenceService;

public class AssetActivitiesImpl extends BaseServiceImpl<AssetActivities, Long> implements AssetActivitiesService{

	public AssetActivitiesImpl(PersistenceService<AssetActivities, Long> persistenceService) {
		super(persistenceService);
	}

}
