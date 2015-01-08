/*
 * @(#)SearchQueryNamed.java 3.0, 17 Jun, 2013 3:04:27 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.search;

import java.util.List;

import org.egov.infstr.services.Page;
import org.egov.infstr.services.PersistenceService;

/**
 * Represents a named query being used for search functionality
 */
public class SearchQueryNamed implements SearchQuery {
	private final String searchQueryName;
	private final String countQueryName;
	private Object[] params;

	/**
	 * Creates the searched query using given named query and parameters
	 * @param searchQueryName Name of the query
	 * @param countQueryName Name of the query to fetch number of records returned by search query
	 * @param params List of parameters to be passed to the query
	 */
	public SearchQueryNamed(final String searchQueryName, final String countQueryName, final List<Object> params) {
		this.searchQueryName = searchQueryName;
		this.countQueryName = countQueryName;
		if (params != null) {
			this.params = params.toArray();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.egov.infstr.search.SearchQuery#execute(org.egov.infstr.services. PersistenceService, int, int)
	 */
	@Override
	public Page getPage(final PersistenceService persistenceService, final int pageNum, final int pageSize) {
		return persistenceService.findPageByNamedQuery(this.searchQueryName, pageNum, pageSize, this.params);
	}

	/*
	 * (non-Javadoc)
	 * @see org.egov.infstr.search.SearchQuery#getCount(org.egov.infstr.services.PersistenceService)
	 */
	@Override
	public int getCount(final PersistenceService persistenceService) {
		return ((Long) persistenceService.findByNamedQuery(this.countQueryName, this.params)).intValue();
	}
}
