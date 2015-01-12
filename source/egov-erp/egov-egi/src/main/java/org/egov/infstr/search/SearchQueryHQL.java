/*
 * @(#)SearchQueryHQL.java 3.0, 17 Jun, 2013 3:04:14 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.search;

import java.util.List;

import org.egov.infstr.services.Page;
import org.egov.infstr.services.PersistenceService;

/**
 * Class representing a search query. Stores the query and list of parameters. This can be used to represent HQL queries with a full query string and optional parameters.
 */
public class SearchQueryHQL implements SearchQuery {
	private final String searchQuery;
	private final String countQuery;
	private Object[] params = new Object[0];

	/**
	 * Creates a search query object using the given query and parameters
	 * @param searchQuery The HQL search query
	 * @param countQuery The HQL query which will return the number of records that will be returned by the search query
	 * @param params List of parameters to be passed to the query
	 */
	public SearchQueryHQL(final String searchQuery, final String countQuery, final List<Object> params) {
		this.searchQuery = searchQuery;
		this.countQuery = countQuery;
		if (params != null) {
			this.params = params.toArray();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.egov.infstr.search.SearchQuery#getPage(PersistenceService, int, int)
	 */
	@Override
	public Page getPage(final PersistenceService persistenceService, final int pageNum, final int pageSize) {
		return persistenceService.findPageBy(this.searchQuery, pageNum, pageSize, this.params);
	}

	/*
	 * (non-Javadoc)
	 * @see org.egov.infstr.search.SearchQuery#getCount(org.egov.infstr.services.PersistenceService)
	 */
	@Override
	public int getCount(final PersistenceService persistenceService) {
		if (persistenceService.find(this.countQuery, this.params) == null) {
			return 0;
		} else {
			return ((Long) persistenceService.find(this.countQuery, this.params)).intValue();
		}
	}
}