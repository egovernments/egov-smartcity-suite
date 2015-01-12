/*
 * @(#)SearchFormAction.java 3.0, 14 Jun, 2013 12:48:31 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.web.actions;

import org.displaytag.pagination.PaginatedList;
import org.displaytag.properties.SortOrderEnum;
import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.services.Page;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.web.utils.EgovPaginatedList;
import org.hibernate.FlushMode;

/**
 * Generic Search Form Action. Can be extended by any action class that intends to provide 
 * search functionality. Supports pagination. Extending class must implement the method {@link SearchFormAction#prepareQuery()} 
 * that prepares the query based on criteria entered by user, and returns an object of {@link SearchQuery}
 */
public abstract class SearchFormAction extends BaseFormAction {
	private static final long serialVersionUID = 1L;
	private static final String SORT_ORDER_ASCENDING = "asc";
	private static final String SORT_ORDER_DESCENDING = "desc";
	private static final String DEFAULT_TABLE_ID = "currentRowObject";

	/**
	 * The page number of search results
	 */
	private int pageNum = 1;

	/**
	 * Number of records to be printed in one page (default = 20)
	 */
	private int pageSize = 20;

	/**
	 * In case of "export", display tag encodes some request parameters. 
	 * This encoder is used to get the encoded names of the parameters so that their values can be fetched.
	 */
	private ParamEncoder paramEncoder = new ParamEncoder(DEFAULT_TABLE_ID);

	private String sortField;
	private SortOrderEnum sortOrder = SortOrderEnum.ASCENDING;
	private SearchQuery searchQuery;
	protected PaginatedList searchResult;

	/**
	 * Sets the display tag table id. This is used to create the param encoder. 
	 * The param encoder is used to fetch data from encoded request parameters. 
	 * The request parameters related to display tag are encoded in case 
	 * the attribute "export" is set to true in the "display:table" tag.
	 * @param tableId The id of the display tag table (in "display:table" tag)
	 */
	public void setTableId(final String tableId) {
		this.paramEncoder = new ParamEncoder(tableId);
	}

	/**
	 * @param pageNum the page number to set
	 */
	public void setPage(final int pageNum) {
		this.pageNum = pageNum;
	}

	/**
	 * @return the current page number
	 */
	public int getPage() {
		return this.pageNum;
	}

	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(final int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return this.pageSize;
	}

	/**
	 * @return Field on which data is to be sorted
	 */
	public String getSort() {
		return this.sortField;
	}

	/**
	 * @param sortField Field on which data is to be sorted
	 */
	public void setSort(final String sortField) {
		this.sortField = sortField;
	}

	/**
	 * @return Sort order (asc/desc)
	 */
	public String getSortDir() {
		return this.sortOrder == SortOrderEnum.ASCENDING ? SORT_ORDER_ASCENDING : SORT_ORDER_DESCENDING;
	}

	/**
	 * @param sortDir Sort order (asc/desc)
	 */
	public void setSortDir(final String sortDir) {
		this.sortOrder = sortDir.equals(SORT_ORDER_ASCENDING) ? SortOrderEnum.ASCENDING : SortOrderEnum.DESCENDING;
	}

	/**
	 * @param sortDir Sort order (asc/desc)
	 */
	public void setDir(final String sortDir) {
		setSortDir(sortDir);
	}

	/**
	 * @return Sort order (asc/desc)
	 */
	public String getDir() {
		return getSortDir();
	}

	/**
	 * @return the search result
	 */
	public PaginatedList getSearchResult() {
		return this.searchResult;
	}

	/**
	 * @param sortField the field on which data is to be sorted. 
	 * This will be same as the value of "property" attribute in display:column 
	 * tag (when sortable=true and user clicks on the column to sort it). 
	 * In case no sorting is required, this parameter will be passed as null.
	 * @param sortOrder the sort order (asc/desc)
	 * @return the query to be used for fetching the search results.
	 */
	public abstract SearchQuery prepareQuery(String sortField, String sortOrder);

	/**
	 * Checks if we are in "export mode". If yes, pagination will NOT be performed and the 
	 * <code>search</code> method will fetch ALL the records for given filter criteria.
	 * @return true if user is trying to "export" the search results to a file, else false.
	 */
	private boolean isExportMode() {
		return this.parameters.get(this.paramEncoder.encodeParameterName(TableTagParameters.PARAMETER_EXPORTTYPE)) != null;
	}

	/**
	 * The Search action method. This will internally call the method 
	 * {@link SearchFormAction#prepareQuery()} to get the query to be executed. 
	 * It then executes the query and creates the paginated search result.
	 * @return success
	 */
	public String search() {
		if (this.searchQuery == null) {
			this.searchQuery = prepareQuery(this.sortField, getSortDir());
		}

		// Mark the session as read only
		HibernateUtil.getCurrentSession().setFlushMode(FlushMode.MANUAL);

		// do not perform pagination in case user is trying
		// to export the search results to a file
		if (isExportMode()) {
			this.pageSize = -1;
			this.pageNum = 1;
		}

		final Page resultPage = this.searchQuery.getPage(this.persistenceService, this.pageNum, this.pageSize);
		final int searchCount = this.searchQuery.getCount(this.persistenceService);
		this.searchResult = new EgovPaginatedList(resultPage, searchCount, this.sortField, this.sortOrder);

		return SUCCESS;
	}
}
