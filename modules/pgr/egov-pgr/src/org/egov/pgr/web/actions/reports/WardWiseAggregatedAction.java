/*
 * @(#)WardWiseAggregatedAction.java 3.0, 23 Jul, 2013 3:29:44 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pgr.web.actions.reports;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.models.validator.CheckDateFormat;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.pgr.constant.PGRConstants;
import org.egov.pgr.domain.entities.ComplaintStatus;
import org.egov.pgr.domain.services.ComplaintStatusService;
import org.egov.web.actions.SearchFormAction;
import org.egov.web.annotation.ValidationErrorPage;

@ParentPackage("egov")
public class WardWiseAggregatedAction extends SearchFormAction {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(WardWiseAggregatedAction.class);

	@CheckDateFormat(message = "invalid.date")
	private Date fromDate;
	@CheckDateFormat(message = "invalid.date")
	private Date toDate;
	protected List<ComplaintStatus> statusList;
	protected ComplaintStatusService complaintStatusService;

	protected Map<String, Integer> wardWiseAggregate;

	@Override
	public Object getModel() {

		return null;
	}

	public String newform() {

		return NEW;
	}

	@Override
	public SearchQuery prepareQuery(final String sortField, final String sortOrder) {

		final String query = "from BoundaryImpl bndry  where bndry.boundaryType.name='Ward'";
		return new SearchQueryHQL(query, "select distinct count(bndry) " + query, null);
	}

	@ValidationErrorPage(value = "new")
	public String list() {
		LOGGER.debug("WardWiseAggregatedAction | list | Start");

		if (null != this.fromDate && null != this.toDate && this.fromDate.after(this.toDate)) {
			throwValidationException("date", getText("error.fromDate.gtThn.toDate"));
		}
		setPageSize(1000);
		search();
		wardWiseAggregatedReport();
		this.statusList = this.complaintStatusService.getAllComplaintStatus();

		return NEW;
	}

	public Integer getNumberOfComplaints(final String key) {

		final Integer noOfComplaints = this.wardWiseAggregate.get(key);
		return null != noOfComplaints ? noOfComplaints : 0;
	}

	@SuppressWarnings("unchecked")
	private void wardWiseAggregatedReport() {

		final StringBuffer query = new StringBuffer(500);

		query.append("select bndry.bndry_num , status.statusname ,count(compdtl.complaintid) from eggr_complaintdetails compdtl,").append(" eg_boundary_type bndrytype,eg_boundary bndry , eggr_redressaldetails rd , eggr_complaintstatus status")
				.append(" where compdtl.bndry = bndry.id_bndry and bndry.id_bndry_type = bndrytype.id_bndry_type and bndrytype.name='Ward'").append(" and rd.complaintid = compdtl.complaintid and rd.complaintstatusid = status.complaintstatusid");
		if (null != this.fromDate) {
			query.append(" and compdtl.COMPLAINTDATE >= to_date('" + DateUtils.getDefaultFormattedDate(this.fromDate) + "','dd/MM/yyyy')");
		}
		if (null != this.toDate) {
			query.append(" and compdtl.COMPLAINTDATE <= to_date('" + DateUtils.getDefaultFormattedDate(this.toDate) + "','dd/MM/yyyy')");
		}
		query.append(" group by bndry.bndry_num ,status.statusname order by bndry.bndry_num");

		final List<Object[]> list = HibernateUtil.getCurrentSession().createSQLQuery(query.toString()).list();
		this.wardWiseAggregate = new HashMap<String, Integer>();
		for (final Object[] object : list) {

			this.wardWiseAggregate.put(object[0] + "-" + object[1], Integer.valueOf(object[2].toString()));
		}

	}

	protected void throwValidationException(final String key, final String value) {
		LOGGER.error(value);
		throw new ValidationException(Arrays.asList(new ValidationError(key, value)));
	}

	/**
	 * @return the fromDate
	 */
	public Date getFromDate() {
		return this.fromDate;
	}

	/**
	 * @param fromDate the fromDate to set
	 */
	public void setFromDate(final Date fromDate) {
		this.fromDate = fromDate;
	}

	/**
	 * @return the toDate
	 */
	public Date getToDate() {
		return this.toDate;
	}

	/**
	 * @param toDate the toDate to set
	 */
	public void setToDate(final Date toDate) {
		this.toDate = toDate;
	}

	/**
	 * @return the statusList
	 */
	public List<ComplaintStatus> getStatusList() {
		return this.statusList;
	}

	/**
	 * @param statusList the statusList to set
	 */
	public void setStatusList(final List<ComplaintStatus> statusList) {
		this.statusList = statusList;
	}

	/**
	 * @return the complaintStatusService
	 */
	public ComplaintStatusService getComplaintStatusService() {
		return this.complaintStatusService;
	}

	/**
	 * @param complaintStatusService the complaintStatusService to set
	 */
	public void setComplaintStatusService(final ComplaintStatusService complaintStatusService) {
		this.complaintStatusService = complaintStatusService;
	}

	/**
	 * @return the wardWiseAggregate
	 */
	public Map<String, Integer> getWardWiseAggregate() {
		return this.wardWiseAggregate;
	}

	/**
	 * @param wardWiseAggregate the wardWiseAggregate to set
	 */
	public void setWardWiseAggregate(final Map<String, Integer> wardWiseAggregate) {
		this.wardWiseAggregate = wardWiseAggregate;
	}

}
