/*
 * @(#)SearchQuerySQL.java 3.0, 17 Jun, 2013 3:04:47 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.search;

import java.math.BigDecimal;
import java.util.List;

import org.egov.infstr.services.Page;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.Query;

/**
 * Class representing a search query. Stores the query and list of parameters. This can be used to represent SQL queries with a full query string and optional parameters.
 * @author manoranjan
 */
public class SearchQuerySQL implements SearchQuery {
	private final String searchQuery;
	private final String countQuery;
	private Object[] params = new Object[0];

	/**
	 * Creates a search query object using the given query and parameters
	 * @param searchQuery The SQL search query
	 * @param countQuery The SQL query which will return the number of records that will be returned by the search query
	 * @param params List of parameters to be passed to the query
	 */
	public SearchQuerySQL(final String searchQuery, final String countQuery, final List<Object> params) {
		this.searchQuery = searchQuery;
		this.countQuery = countQuery;
		if (params != null) {
			this.params = params.toArray();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.egov.infstr.search.SearchQuery#getCount(org.egov.infstr.services.PersistenceService)
	 */
	@Override
	public int getCount(final PersistenceService persistenceService) {
		final Query q = getSQLQueryWithParams(persistenceService, this.countQuery);
		return ((BigDecimal) q.uniqueResult()).intValue();
	}

	/*
	 * (non-Javadoc)
	 * @see org.egov.infstr.search.SearchQuery#getPage(org.egov.infstr.services.PersistenceService, int, int)
	 */
	@Override
	public Page getPage(final PersistenceService persistenceService, final int pageNum, final int pageSize) {
		final Query q = getSQLQueryWithParams(persistenceService, this.searchQuery);
		return new Page(q, pageNum, pageSize);
	}

	/**
	 * Creates an SQL query and also sets parameters in to it if required
	 * @param persistenceService Persistence service used for creating the query
	 * @param query The SQL query string
	 * @return The created Query object
	 */
	private Query getSQLQueryWithParams(final PersistenceService persistenceService, final String query) {
		final Query q = persistenceService.getSession().createSQLQuery(query);

		if (this.params != null && this.params.length > 0) {
			for (int index = 0; index < this.params.length; index++) {
				q.setParameter(index, this.params[index]);
			}
		}
		return q;
	}
}
