/*
 * @(#)SearchQuery.java 3.0, 17 Jun, 2013 3:03:47 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.search;

import org.egov.infstr.services.Page;
import org.egov.infstr.services.PersistenceService;

/**
 * Interface that defines a search query. Implementing classes must provide a method to execute the query and a method to get total number of records that will be returned by the search query.
 */
public interface SearchQuery {
	/**
	 * @param persistenceService Persistence service to be used for executing the query
	 * @param pageNum Page number
	 * @param pageSize Page size
	 * @return Page object created using results of the search query execution for given page number and page size
	 */
	public Page getPage(PersistenceService persistenceService, int pageNum, int pageSize);

	/**
	 * @param persistenceService Persistence service that can be used for executing the query
	 * @return Total number records that will be returned by the search query
	 */
	public int getCount(PersistenceService persistenceService);
}
