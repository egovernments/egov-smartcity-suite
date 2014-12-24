/*
 * @(#)WardWiseReportAction.java 3.0, 23 Jul, 2013 3:29:43 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pgr.web.actions.reports;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.exceptions.NoSuchObjectException;
import org.egov.exceptions.TooManyValuesException;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.models.validator.CheckDateFormat;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryType;
import org.egov.lib.admbndry.HeirarchyType;
import org.egov.lib.admbndry.ejb.api.BoundaryService;
import org.egov.lib.admbndry.ejb.api.BoundaryTypeService;
import org.egov.lib.admbndry.ejb.api.HeirarchyTypeService;
import org.egov.pgr.constant.PGRConstants;
import org.egov.pgr.domain.entities.ComplaintStatus;
import org.egov.pgr.domain.services.ComplaintStatusService;
import org.egov.web.actions.SearchFormAction;
import org.egov.web.annotation.ValidationErrorPage;

@ParentPackage("egov")
public class WardWiseReportAction extends SearchFormAction {

	private static final Logger LOGGER = Logger.getLogger(WardWiseReportAction.class);
	private static final long serialVersionUID = 1L;

	protected GenericHibernateDaoFactory genericHibDao;
	protected HeirarchyTypeService heirarchyTypeService;
	protected BoundaryTypeService boundaryTypeService;
	protected BoundaryService boundaryService;
	protected ComplaintStatusService complaintStatusService;
	protected List<ComplaintStatus> statusList;
	protected Map<String, Integer> compBndryStatus;

	@CheckDateFormat(message = "invalid.date")
	private Date fromDate;

	@CheckDateFormat(message = "invalid.date")
	private Date toDate;

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
		LOGGER.debug("WardWiseReportAction | search | Start");
		if (null != this.fromDate && null != this.toDate && this.fromDate.after(this.toDate)) {
			throwValidationException("date", getText("error.fromDate.gtThn.toDate"));
		}
		validateHLevel();
		setPageSize(1000);
		search();

		this.statusList = this.complaintStatusService.getAllComplaintStatus();
		noOfComplaintsByBoundaryAndStatus();
		return NEW;
	}

	public Integer getNumberOfComplaints(final String key) {

		final Integer noOfComplaints = this.compBndryStatus.get(key);
		return null != noOfComplaints ? noOfComplaints : 0;
	}

	private void validateHLevel() {

		final Boundary topLevelBoundary = this.boundaryService.getBoundary(Integer.valueOf(ServletActionContext.getRequest().getSession().getAttribute(PGRConstants.SESSION_ATTRIBUTE_TOP_LEVEL_BOUNDARY).toString()));
		final HeirarchyType htype = getHeirarchyType();
		final BoundaryType childBndrytype = this.boundaryTypeService.getBoundaryType(PGRConstants.WARD_BOUNDARY_TYPE_NAME, htype);
		if (childBndrytype.getHeirarchy() <= topLevelBoundary.getBoundaryType().getHeirarchy()) {
			throwValidationException("error.chldBndryTyp.isGtThan.prntBndryTyp", getText("error.chldBndryTyp.isGtThan.prntBndryTyp"));
		}

	}

	@SuppressWarnings("unchecked")
	private void noOfComplaintsByBoundaryAndStatus() {

		final StringBuffer query = new StringBuffer(500);
		final HeirarchyType htype = getHeirarchyType();
		final BoundaryType bndryType = this.boundaryTypeService.getBoundaryType(PGRConstants.WARD_BOUNDARY_TYPE_NAME, htype);

		query.append("select bndry.bndry_num , status.statusname ,count(compdtl.complaintid) from eggr_complaintdetails compdtl,").append(" eg_boundary_type bndrytype,eg_boundary bndry , eggr_redressaldetails rd , eggr_complaintstatus status where compdtl")
				.append(".bndry=bndry.id_bndry and bndry.id_bndry_type=bndrytype.id_bndry_type and bndrytype.id_bndry_type=" + bndryType.getId()).append(" and rd.complaintid = compdtl.complaintid and rd.complaintstatusid = status.complaintstatusid");
		if (null != this.fromDate) {
			query.append(" and compdtl.COMPLAINTDATE >= to_date('" + DateUtils.getDefaultFormattedDate(this.fromDate) + "','dd/MM/yyyy')");
		}
		if (null != this.toDate) {
			query.append(" and compdtl.COMPLAINTDATE <= to_date('" + DateUtils.getDefaultFormattedDate(this.toDate) + "','dd/MM/yyyy')");
		}
		query.append(" group by bndry.bndry_num ,status.statusname order by bndry.bndry_num");

		final List<Object[]> list = HibernateUtil.getCurrentSession().createSQLQuery(query.toString()).list();
		this.compBndryStatus = new HashMap<String, Integer>();
		for (final Object[] object : list) {

			this.compBndryStatus.put(object[0] + "-" + object[1], Integer.valueOf(object[2].toString()));
		}

	}

	private HeirarchyType getHeirarchyType() {
		HeirarchyType htype = null;
		final List<AppConfigValues> configValues = this.genericHibDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(PGRConstants.MODULE_NAME, "HTYPENAME");
		if (configValues == null || configValues.isEmpty()) {
			throwValidationException("APPCONFIG_HTYPENAME", "Appconfig HTYPENAME is not defined in the system");
		}
		final String hTypeName = configValues.get(0).getValue();
		if (StringUtils.isBlank(hTypeName)) {
			throwValidationException("APPCONFIG_HTYPENAME_VALUE", "Appconfig HTYPENAME value is not defined in the system");
		}		
		try {
			htype = this.heirarchyTypeService.getHierarchyTypeByName(hTypeName);
		} catch (final TooManyValuesException e) {
			LOGGER.error(e.getMessage());
			throwValidationException(e.getMessage(), e.getMessage());
		} catch (final NoSuchObjectException e) {
			LOGGER.error(e.getMessage());
			throwValidationException(e.getMessage(), e.getMessage());
		}
		return htype;
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

	@Override
	public Object getModel() {
		return null;
	}

	public void setBoundaryService(final BoundaryService boundaryService) {
		this.boundaryService = boundaryService;
	}

	/**
	 * @param genericHibDao the genericHibDao to set
	 */
	public void setGenericHibDao(final GenericHibernateDaoFactory genericHibDao) {
		this.genericHibDao = genericHibDao;
	}

	/**
	 * @param heirarchyTypeService the heirarchyTypeService to set
	 */
	public void setHeirarchyTypeService(final HeirarchyTypeService heirarchyTypeService) {
		this.heirarchyTypeService = heirarchyTypeService;
	}

	/**
	 * @param boundaryTypeService the boundaryTypeService to set
	 */
	public void setBoundaryTypeService(final BoundaryTypeService boundaryTypeService) {
		this.boundaryTypeService = boundaryTypeService;
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

	/**
	 * @return the compBndryStatus
	 */
	public Map<String, Integer> getCompBndryStatus() {
		return this.compBndryStatus;
	}

	/**
	 * @param compBndryStatus the compBndryStatus to set
	 */
	public void setCompBndryStatus(final Map<String, Integer> compBndryStatus) {
		this.compBndryStatus = compBndryStatus;
	}

}
