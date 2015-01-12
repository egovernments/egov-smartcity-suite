/*
 * @(#)RelationService.java 3.0, 14 Jun, 2013 12:10:30 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.commons.Relation;
import org.egov.commons.utils.EntityType;
import org.egov.infstr.services.PersistenceService;

public class RelationService extends PersistenceService<Relation, Integer> implements EntityTypeService, BidderTypeService {
	/**
	 * since it is mapped to only one AccountDetailType -creditor it ignores the input parameter
	 */
	@Override
	public List<EntityType> getAllActiveEntities(final Integer accountDetailTypeId) {
		final List<EntityType> entities = new ArrayList<EntityType>();
		entities.addAll(findAllBy("from Relation r where r.isactive=?", true));
		return entities;
	}

	@Override
	public List<EntityType> filterActiveEntities(String filterKey, final int maxRecords, final Integer accountDetailTypeId) {
		final Integer pageSize = (maxRecords > 0 ? maxRecords : null);
		final List<EntityType> entities = new ArrayList<EntityType>();
		filterKey = "%" + filterKey + "%";
		final String qry = "from Relation r where upper(code) like upper(?) or upper(name) like upper(?) and r.isactive=? order by code,name";
		entities.addAll(findPageBy(qry, 0, pageSize, filterKey, filterKey, true).getList());
		return entities;
	}

	@Override
	public List<Relation> getAllActiveBidders() {
		final List<Relation> entities = new ArrayList<Relation>();
		entities.addAll(findAllBy("from Relation r where r.isactive=? and relationtype.name=?", true, "Supplier"));
		return entities;
	}

	@Override
	public Relation getBidderById(final Integer bidderId) {
		return findById(bidderId, Boolean.TRUE);
	}

	@Override
	public Relation getBidderByCode(final String code) {
		return find("from Relation where code=?", code);
	}
}
