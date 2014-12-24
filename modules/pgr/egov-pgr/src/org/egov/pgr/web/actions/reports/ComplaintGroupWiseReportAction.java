/*
 * @(#)ComplaintGroupWiseReportAction.java 3.0, 23 Jul, 2013 3:29:42 PM
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
import org.apache.struts2.ServletActionContext;
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
import org.egov.pgr.domain.services.ComplaintGroupService;
import org.egov.pgr.domain.services.ComplaintStatusService;
import org.egov.web.actions.SearchFormAction;
import org.egov.web.annotation.ValidationErrorPage;

@ParentPackage("egov")
public class ComplaintGroupWiseReportAction extends SearchFormAction {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ComplaintGroupWiseReportAction.class);

	@CheckDateFormat(message = "invalid.date")
	private Date fromDate;
	@CheckDateFormat(message = "invalid.date")
	private Date toDate;
	private Integer group;
	protected List<ComplaintStatus> statusList;
	protected ComplaintStatusService complaintStatusService;
	protected ComplaintGroupService complaintGroupService;
	protected Map<String, Integer> groupStatus;

	@Override
	public Object getModel() {

		return null;
	}

	@Override
	public void prepare() {
		super.prepare();
		addDropdownData("groupList", this.complaintGroupService.findAll());
	}

	@Override
	public SearchQuery prepareQuery(final String sortField, final String sortOrder) {
		Integer.valueOf(ServletActionContext.getRequest().getSession().getAttribute(PGRConstants.SESSION_ATTRIBUTE_TOP_LEVEL_BOUNDARY).toString());
		final StringBuffer query = new StringBuffer(400);
		query.append("from ComplaintTypes ct where  ct.complaintGroup.id=").append(this.group);

		return new SearchQueryHQL("select DISTINCT ct " + query, " select DISTINCT count(ct) " + query, null);
	}

	public String newform() {

		return NEW;
	}

	@ValidationErrorPage(value = "new")
	public String list() {
		LOGGER.debug("ComplaintGroupWiseReportAction | list | Start");
		if (null == this.group || this.group == -1) {
			throwValidationException("group", getText("error.group.required"));
		}
		if (null != this.fromDate && null != this.toDate && this.fromDate.after(this.toDate)) {
			throwValidationException("date", getText("error.fromDate.gtThn.toDate"));
		}
		setPageSize(1000);
		search();
		getGroupWiseStatusReport();
		this.statusList = this.complaintStatusService.getAllComplaintStatus();

		return NEW;
	}

	public Integer getNumberOfComplaints(final String key) {
		return this.groupStatus.get(key) == null ? 0 : this.groupStatus.get(key);
	}

	@SuppressWarnings("unchecked")
	private void getGroupWiseStatusReport() {

		final Integer topLevelbndry = Integer.valueOf(ServletActionContext.getRequest().getSession().getAttribute(PGRConstants.SESSION_ATTRIBUTE_TOP_LEVEL_BOUNDARY).toString());
		final StringBuffer query = new StringBuffer();

		query.append(" SELECT ct.complainttypeid, cs.statusname, COUNT (cd.complaintid) FROM eggr_complaintdetails cd, eggr_complaintgroup cg, eggr_redressaldetails rd,eggr_complaintstatus cs, eggr_complainttypes ct")
				.append(" WHERE cg.id_complaintgroup = ct.complaintgroup_id AND ct.complainttypeid = cd.complainttype AND cd.complaintid= rd.complaintid AND rd.complaintstatusid = cs.complaintstatusid AND cd.toplevelbndry =").append(topLevelbndry)
				.append(" AND cg.id_complaintgroup =").append(this.group);
		if (null != this.fromDate) {
			query.append(" and  cd.COMPLAINTDATE >= to_date('" + DateUtils.getDefaultFormattedDate(this.fromDate) + "','dd/MM/yyyy')");
		}
		if (null != this.toDate) {
			query.append(" and  cd.COMPLAINTDATE <= to_date('" + DateUtils.getDefaultFormattedDate(this.toDate) + "','dd/MM/yyyy')");
		}
		query.append(" GROUP BY ct.complainttypeid, cs.statusname ORDER BY ct.complainttypeid, cs.statusname");

		final List<Object[]> list = HibernateUtil.getCurrentSession().createSQLQuery(query.toString()).list();
		this.groupStatus = new HashMap<String, Integer>();
		for (final Object[] object : list) {

			this.groupStatus.put(object[0] + "-" + object[1], Integer.valueOf(object[2].toString()));
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
	 * @param complaintGroupService the complaintGroupService to set
	 */
	public void setComplaintGroupService(final ComplaintGroupService complaintGroupService) {
		this.complaintGroupService = complaintGroupService;
	}

	/**
	 * @return the group
	 */
	public Integer getGroup() {
		return this.group;
	}

	/**
	 * @param group the group to set
	 */
	public void setGroup(final Integer group) {
		this.group = group;
	}

	/**
	 * @return the groupStatus
	 */
	public Map<String, Integer> getGroupStatus() {
		return this.groupStatus;
	}

	/**
	 * @param groupStatus the groupStatus to set
	 */
	public void setGroupStatus(final Map<String, Integer> groupStatus) {
		this.groupStatus = groupStatus;
	}

}
