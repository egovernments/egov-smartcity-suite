/*
 * @(#)EgovPaginatedList.java 3.0, 7 Jun, 2013 11:03:52 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.web.utils;

import java.util.List;

import org.displaytag.pagination.PaginatedList;
import org.displaytag.properties.SortOrderEnum;
import org.egov.infstr.services.Page;

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
