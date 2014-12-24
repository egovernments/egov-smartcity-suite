package org.egov.assets.service.impl;

import org.egov.assets.model.AssetDepreciation;
import org.egov.assets.service.AssetDepreciationService;
import org.egov.infstr.services.PersistenceService;

public class AssetDepreciationImpl extends BaseServiceImpl<AssetDepreciation, Long> implements AssetDepreciationService{

	public AssetDepreciationImpl(PersistenceService<AssetDepreciation, Long> persistenceService) {
		super(persistenceService);
	}

}
