/*
 * @(#)DepartmentWiseReportAction.java 3.0, 23 Jul, 2013 3:29:43 PM
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
import org.egov.lib.rjbac.dept.ejb.api.DepartmentService;
import org.egov.pgr.constant.PGRConstants;
import org.egov.pgr.domain.entities.ComplaintStatus;
import org.egov.pgr.domain.services.ComplaintStatusService;
import org.egov.web.actions.SearchFormAction;
import org.egov.web.annotation.ValidationErrorPage;

@ParentPackage("egov")
public class DepartmentWiseReportAction extends SearchFormAction {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(DepartmentWiseReportAction.class);
	private DepartmentService departmentService;
	@CheckDateFormat(message = "invalid.date")
	private Date fromDate;
	@CheckDateFormat(message = "invalid.date")
	private Date toDate;
	private Integer department;

	protected Map<String, Integer> compDeptStatus;
	protected List<ComplaintStatus> statusList;

	protected ComplaintStatusService complaintStatusService;

	@Override
	public Object getModel() {

		return null;
	}

	@Override
	public void prepare() {
		super.prepare();
		addDropdownData("department", this.departmentService.getAllDepartments());
	}

	public String newform() {

		return NEW;
	}

	@Override
	public SearchQuery prepareQuery(final String sortField, final String sortOrder) {
		LOGGER.debug("DepartmentWiseReportAction | prepareQuery | Start");
		final Integer topLevelbndry = Integer.valueOf(ServletActionContext.getRequest().getSession().getAttribute(PGRConstants.SESSION_ATTRIBUTE_TOP_LEVEL_BOUNDARY).toString());
		final StringBuffer query = new StringBuffer(400);
		query.append("from ComplaintDetails cd where  cd.department.id=" + this.department).append(" and cd.topLevelbndry=" + topLevelbndry);
		return new SearchQueryHQL("select DISTINCT cd.complaintType " + query, " select DISTINCT count(cd.complaintType) " + query, null);
	}

	@ValidationErrorPage(value = "new")
	public String list() {
		LOGGER.debug("DepartmentWiseReportAction | list | Start");
		if (null == this.department || this.department == -1) {
			throwValidationException("department", getText("error.dept.required"));
		}
		if (null != this.fromDate && null != this.toDate && this.fromDate.after(this.toDate)) {
			throwValidationException("date", getText("error.fromDate.gtThn.toDate"));
		}
		setPageSize(1000);
		search();
		getDeptWiseStatusReport();
		this.statusList = this.complaintStatusService.getAllComplaintStatus();

		return NEW;
	}

	public Integer getNumberOfComplaints(final String key) {

		final Integer noOfComplaints = this.compDeptStatus.get(key);
		return null != noOfComplaints ? noOfComplaints : 0;
	}

	@SuppressWarnings("unchecked")
	private void getDeptWiseStatusReport() {

		final Integer topLevelbndry = Integer.valueOf(ServletActionContext.getRequest().getSession().getAttribute(PGRConstants.SESSION_ATTRIBUTE_TOP_LEVEL_BOUNDARY).toString());
		final StringBuffer query = new StringBuffer(300);

		query.append(" SELECT  ct.COMPLAINTTYPEID, cs.statusname, COUNT(cd.complaintid) FROM EGGR_COMPLAINTDETAILS cd, ").append(" EG_DEPARTMENT d,EGGR_REDRESSALDETAILS rd,EGGR_COMPLAINTSTATUS cs, EGGR_COMPLAINTTYPES ct")
				.append(" WHERE d.id_dept = cd.deptid  AND ct.COMPLAINTTYPEID=cd.COMPLAINTTYPE AND cd.complaintid = rd.complaintid ").append(" AND rd.complaintstatusid = cs.complaintstatusid AND cd.toplevelbndry=").append(topLevelbndry)
				.append(" AND d.id_dept=").append(this.department);
		if (null != this.fromDate) {
			query.append(" and  cd.COMPLAINTDATE >= to_date('" + DateUtils.getDefaultFormattedDate(this.fromDate) + "','dd/MM/yyyy')");
		}
		if (null != this.toDate) {
			query.append(" and  cd.COMPLAINTDATE <= to_date('" + DateUtils.getDefaultFormattedDate(this.toDate) + "','dd/MM/yyyy')");
		}
		query.append(" GROUP BY ct.COMPLAINTTYPEID, cs.statusname ORDER BY ct.COMPLAINTTYPEID, cs.statusname");

		final List<Object[]> list = HibernateUtil.getCurrentSession().createSQLQuery(query.toString()).list();
		this.compDeptStatus = new HashMap<String, Integer>();
		for (final Object[] object : list) {

			this.compDeptStatus.put(object[0] + "-" + object[1], Integer.valueOf(object[2].toString()));
		}
	}

	protected void throwValidationException(final String key, final String value) {
		LOGGER.error(value);
		throw new ValidationException(Arrays.asList(new ValidationError(key, value)));
	}

	public Date getFromDate() {
		return this.fromDate;
	}

	public void setFromDate(final Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return this.toDate;
	}

	public void setToDate(final Date toDate) {
		this.toDate = toDate;
	}

	/**
	 * @param departmentService the departmentService to set
	 */
	public void setDepartmentService(final DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	/**
	 * @param department the department to set
	 */
	public void setDepartment(final Integer department) {
		this.department = department;
	}

	/**
	 * @return the department
	 */
	public Integer getDepartment() {
		return this.department;
	}

	/**
	 * @return the compDeptStatus
	 */
	public Map<String, Integer> getCompDeptStatus() {
		return this.compDeptStatus;
	}

	/**
	 * @param compDeptStatus the compDeptStatus to set
	 */
	public void setCompDeptStatus(final Map<String, Integer> compDeptStatus) {
		this.compDeptStatus = compDeptStatus;
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
	 * @param complaintStatusService the complaintStatusService to set
	 */
	public void setComplaintStatusService(final ComplaintStatusService complaintStatusService) {
		this.complaintStatusService = complaintStatusService;
	}

}
