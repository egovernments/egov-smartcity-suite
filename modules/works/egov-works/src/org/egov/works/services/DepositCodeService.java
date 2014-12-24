/**
 * 
 */
package org.egov.works.services;

import java.util.List;

import org.egov.commons.service.EntityTypeService;
import org.egov.infstr.services.Page;
import org.egov.infstr.services.PersistenceService;
import org.egov.works.models.masters.DepositCode;
import org.egov.works.utils.WorksConstants;

/**
 * Entity Type Service for deposit code
 */
public class DepositCodeService extends PersistenceService<DepositCode, Long>
		implements EntityTypeService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.egov.commons.service.EntityTypeService#filterActiveEntities(java.
	 * lang.String, int, java.lang.Integer)
	 */
	@Override
	public List<DepositCode> filterActiveEntities(String filterKey,
			int maxRecords, Integer accountDetailTypeId) {
		Integer pageSize = (maxRecords > 0 ? maxRecords : null);
		String param = "%" + filterKey.toUpperCase() + "%";
		
		Page page = findPageByNamedQuery(
				WorksConstants.QUERY_GETACTIVEDEPOSITCODES_BY_CODE_OR_DESC, 1, pageSize, param, param);
		
		return (List<DepositCode>) page.getList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.egov.commons.service.EntityTypeService#getAllActiveEntities(java.
	 * lang.Integer)
	 */
	@Override
	public List<DepositCode> getAllActiveEntities(Integer accountDetailTypeId) {
		return findAllByNamedQuery(WorksConstants.QUERY_GETACTIVEDEPOSITCODES);
	}
}
