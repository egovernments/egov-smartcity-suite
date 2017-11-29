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

package org.egov.infra.web.utils;

import org.displaytag.pagination.PaginatedList;
import org.displaytag.properties.SortOrderEnum;
import org.egov.infra.persistence.utils.Page;

import java.util.List;

/**
 * The Class EgovPaginatedList.
 * @author Sahina
 */
public class EgovPaginatedList implements PaginatedList {
	
	/** The full list size. */
	private int fullListSize;
	
	/** The page number. */
	private int pageNumber;
	
	/** The list. */
	private List list;
	
	/** The objects per page. */
	private int objectsPerPage;
	
	/** The sort criterion. */
	private String sortCriterion;
	
	/** The sort direction. */
	private SortOrderEnum sortDirection = SortOrderEnum.ASCENDING;
	
	/**
	 * Instantiates a new egov paginated list.
	 * @param page the page
	 */
	public EgovPaginatedList(final Page page, int fullListSize) {
		this(page, fullListSize, null, SortOrderEnum.ASCENDING);
	}

	/**
	 * Instantiates a new egov paginated list.
	 * 
	 * @param page
	 *            the page
	 * @param fullListSize
	 *            Size of the full list
	 * @param sortCriterion
	 *            Sort criteria (field name)
	 * @param sortDir
	 *            Sort direction (ascending/descending)
	 */
	public EgovPaginatedList(final Page page, int fullListSize,
			final String sortCriterion, final SortOrderEnum sortDir) {
		super();
		this.pageNumber = page.getPageNo();
		this.objectsPerPage = page.getPageSize();
		this.fullListSize = fullListSize;
		this.list = page.getList();
		this.sortCriterion = sortCriterion;
		this.sortDirection = sortDir;
	}
	
	/**
	 * Instantiates a new egov paginated list.
	 * @param pageNumber the page number
	 * @param objectsPerPage the objects per page
	 */
	public EgovPaginatedList(final int pageNumber, final int objectsPerPage) {
		super();
		if (pageNumber < 0) {
			this.pageNumber  = 0;
		} else {
			this.pageNumber = pageNumber;
		}
		this.objectsPerPage = objectsPerPage;
	}
	
	/**
	 * Instantiates a new egov paginated list.
	 * @param page the page
	 * @param sortCriterion the sort criterion
	 * @param sortDir the sort dir
	 */
	public EgovPaginatedList(final Page page, final String sortCriterion, final SortOrderEnum sortDir) {
		super();
		this.pageNumber = page.getPageNo();
		this.objectsPerPage = page.getPageSize();
		this.sortCriterion = sortCriterion;
		this.sortDirection = sortDir;
	}
	
	/**
	 * Instantiates a new egov paginated list.
	 * @param pageNumber the page number
	 * @param objectsPerPage the objects per page
	 * @param sortCriterion the sort criterion
	 * @param sortDir the sort dir
	 */
	public EgovPaginatedList(final int pageNumber, final int objectsPerPage, final String sortCriterion, final SortOrderEnum sortDir) {
		super();
		if (pageNumber < 0) {
			this.pageNumber = 0;
		} else {
			this.pageNumber = pageNumber;
		}
		this.objectsPerPage = objectsPerPage;
		this.sortCriterion = sortCriterion;
		this.sortDirection = sortDir;
	}
	
	/* (non-Javadoc)
	 * @see org.displaytag.pagination.PaginatedList#getFullListSize()
	 */
	@Override
	public int getFullListSize() {
		return this.fullListSize;
	}
	
	/**
	 * Sets the full list size.
	 * @param fullListSize the new full list size
	 */
	public void setFullListSize(final int fullListSize) {
		this.fullListSize = fullListSize;
	}
	
	/* (non-Javadoc)
	 * @see org.displaytag.pagination.PaginatedList#getList()
	 */
	@Override
	public List getList() {
		return this.list;
	}
	
	/**
	 * Sets the list.
	 * @param list the new list
	 */
	public void setList(final List list) {
		this.list = list;
	}
	
	/* (non-Javadoc)
	 * @see org.displaytag.pagination.PaginatedList#getObjectsPerPage()
	 */
	@Override
	public int getObjectsPerPage() {
		return this.objectsPerPage;
	}
	
	/**
	 * Sets the objects per page.
	 * @param objectsPerPage the new objects per page
	 */
	public void setObjectsPerPage(final int objectsPerPage) {
		this.objectsPerPage = objectsPerPage;
	}
	
	/* (non-Javadoc)
	 * @see org.displaytag.pagination.PaginatedList#getPageNumber()
	 */
	@Override
	public int getPageNumber() {
		return this.pageNumber;
	}
	
	/**
	 * Sets the page number.
	 * @param pageNumber the new page number
	 */
	public void setPageNumber(final int pageNumber) {
		if (pageNumber < 0) {
			this.pageNumber = 0;
		} else {
			this.pageNumber = pageNumber;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.displaytag.pagination.PaginatedList#getSearchId()
	 */
	@Override
	public String getSearchId() {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.displaytag.pagination.PaginatedList#getSortCriterion()
	 */
	@Override
	public String getSortCriterion() {
		return this.sortCriterion;
	}
	
	/**
	 * Sets the sort criterion.
	 * @param sortCriterion the new sort criterion
	 */
	public void setSortCriterion(final String sortCriterion) {
		this.sortCriterion = sortCriterion;
	}
	
	/* (non-Javadoc)
	 * @see org.displaytag.pagination.PaginatedList#getSortDirection()
	 */
	@Override
	public SortOrderEnum getSortDirection() {
		return this.sortDirection;
	}
	
	/**
	 * Sets the sort direction.
	 * @param sortDirection the new sort direction
	 */
	public void setSortDirection(final SortOrderEnum sortDirection) {
		this.sortDirection = sortDirection;
	}
}
