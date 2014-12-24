/**
 * 
 */
package org.egov.assets.service.impl;

import org.egov.assets.model.AssetVoucherHeader;
import org.egov.assets.service.AssetVoucherHeaderService;
import org.egov.infstr.services.PersistenceService;

/**
 * @author manoranjan
 *
 */
public class AssetVoucherHeaderImpl extends BaseServiceImpl<AssetVoucherHeader, Long> implements AssetVoucherHeaderService{
	
	public AssetVoucherHeaderImpl(PersistenceService<AssetVoucherHeader, Long> persistenceService) {
		super(persistenceService);
	}
}
