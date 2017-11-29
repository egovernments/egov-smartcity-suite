/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */

package org.egov.infra.web.struts.actions;

import org.displaytag.pagination.PaginatedList;
import org.displaytag.properties.SortOrderEnum;
import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;
import org.egov.infra.persistence.utils.Page;
import org.egov.infra.web.utils.EgovPaginatedList;
import org.egov.infstr.search.SearchQuery;

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
