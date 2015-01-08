/*
 * @(#)SearchQueryCriteria.java 3.0, 17 Jun, 2013 3:04:02 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.search;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.services.Page;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;

/**
 * Class representing a search query that uses hibernate Criteria. Can be used by search forms that use hibernate Criteria for fetching search results.
 */
public class SearchQueryCriteria implements SearchQuery {
	private final Criteria searchCriteria;
	private final Criteria countCriteria;

	public SearchQueryCriteria(final Criteria searchCriteria, final Criteria countCriteria) {
		if (searchCriteria == countCriteria) {
			throw new EGOVRuntimeException("Search Criteria cannot be same as Count Criteria. Please pass different objects!");
		}

		this.searchCriteria = searchCriteria;

		this.countCriteria = countCriteria;
		this.countCriteria.setProjection(Projections.rowCount());
	}

	/*
	 * (non-Javadoc)
	 * @see org.egov.infstr.search.SearchQuery#getCount(org.egov.infstr.services. PersistenceService)
	 */
	@Override
	public int getCount(final PersistenceService persistenceService) {
		return ((Integer) this.countCriteria.uniqueResult()).intValue();
	}

	/*
	 * (non-Javadoc)
	 * @seeorg.egov.infstr.search.SearchQuery#getPage(org.egov.infstr.services. PersistenceService, int, int)
	 */
	@Override
	public Page getPage(final PersistenceService persistenceService, final int pageNum, final int pageSize) {
		return new Page(this.searchCriteria, pageNum, pageSize);
	}
}
