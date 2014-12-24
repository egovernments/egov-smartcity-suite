/*
 * @(#)Page.java 3.0, 17 Jun, 2013 3:07:24 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.services;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;

public class Page {

	final private List results;
	final private Integer pageSize;
	final private int pageNumber;

	/**
	 * @param query Hibernate Query object that is executed to obtain list
	 * @param pageNumber The page number
	 * @param pageSize No of records to be returned. If this parameter is null, all the records are returned. The page parameter will be ignored in this case.
	 */
	public Page(final Query query, Integer pageNumber, final Integer pageSize) {
		if (pageNumber < 1) {
			pageNumber = 1;
		}

		this.pageNumber = pageNumber;
		if (pageSize != null && pageSize > 0) {
			query.setFirstResult((pageNumber - 1) * pageSize);
			query.setMaxResults(pageSize + 1);
			this.pageSize = pageSize;
		} else {
			this.pageSize = -1;
		}
		this.results = query.list();
	}

	/**
	 * @param criteria Hibernate Criteria object that is executed to obtain list
	 * @param pageNumber The page number
	 * @param pageSize No of records to be returned. If this parameter is null, all the records are returned. The page parameter will be ignored in this case.
	 */
	public Page(final Criteria criteria, Integer pageNumber, final Integer pageSize) {
		if (pageNumber < 1) {
			pageNumber = 1;
		}

		this.pageNumber = pageNumber;

		if (pageSize != null && pageSize > 0) {
			criteria.setFirstResult((pageNumber - 1) * pageSize);
			criteria.setMaxResults(pageSize + 1);
			this.pageSize = pageSize;
		} else {
			this.pageSize = -1;
		}
		this.results = criteria.list();
	}

	public boolean isNextPage() {
		return (this.pageSize != -1 && this.results.size() > this.pageSize);
	}

	public boolean isPreviousPage() {
		return this.pageNumber > 0;
	}

	public List getList() {
		return isNextPage() ? this.results.subList(0, this.pageSize) : this.results;
	}

	public Integer getPageNo() {
		return this.pageNumber;
	}

	public Integer getPageSize() {
		return this.pageSize;
	}
}
