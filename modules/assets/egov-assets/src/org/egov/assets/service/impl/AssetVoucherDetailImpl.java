/**
 * 
 */
package org.egov.assets.service.impl;

import org.egov.assets.model.AssetVoucherDetail;
import org.egov.assets.service.AssetVoucherDetailService;
import org.egov.infstr.services.PersistenceService;

/**
 * @author manoranjan
 *
 */
public class AssetVoucherDetailImpl extends BaseServiceImpl<AssetVoucherDetail, Long> implements AssetVoucherDetailService{

	public AssetVoucherDetailImpl(PersistenceService<AssetVoucherDetail, Long> persistenceService) {
		super(persistenceService);
	}
}
