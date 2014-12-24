/*
 * @(#)WardWiseKmlReportAction.java 3.0, 23 Jul, 2013 3:29:44 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pgr.web.actions.reports;

import java.math.BigDecimal;
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
import org.egov.infstr.services.GeoLocationConstants;
import org.egov.infstr.services.GeoLocationService;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryType;
import org.egov.lib.admbndry.HeirarchyType;
import org.egov.lib.admbndry.ejb.api.BoundaryService;
import org.egov.lib.admbndry.ejb.api.BoundaryTypeService;
import org.egov.lib.admbndry.ejb.api.HeirarchyTypeService;
import org.egov.pgr.constant.PGRConstants;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPage;

@ParentPackage("egov")
public class WardWiseKmlReportAction extends BaseFormAction {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(WardWiseKmlReportAction.class);
	protected GenericHibernateDaoFactory genericHibDao;
	protected HeirarchyTypeService heirarchyTypeService;
	protected BoundaryTypeService boundaryTypeService;
	protected BoundaryService boundaryService;
	@CheckDateFormat(message = "invalid.date")
	private Date fromDate;

	@CheckDateFormat(message = "invalid.date")
	private Date toDate;

	private Map<String, BigDecimal> wardWiseComplaints;

	@Override
	public Object getModel() {

		return null;
	}

	public String newform() {
		LOGGER.debug("WardWiseKmlReportAction | newform");

		return NEW;
	}

	@SuppressWarnings("unchecked")
	@ValidationErrorPage(value = "new")
	public String search() {

		final HeirarchyType htype = getHeirarchyType();
		final BoundaryType bndryType = this.boundaryTypeService.getBoundaryType(PGRConstants.WARD_BOUNDARY_TYPE_NAME, htype);

		final Integer topLevelBoundary = Integer.valueOf(ServletActionContext.getRequest().getSession().getAttribute(PGRConstants.SESSION_ATTRIBUTE_TOP_LEVEL_BOUNDARY).toString());
		final List<Boundary> lstOfBoundaries = this.boundaryService.getAllBoundaries(bndryType, topLevelBoundary);

		if (null == lstOfBoundaries || lstOfBoundaries.size() <= 0) {
			throwValidationException("boundaryError", getText("error.boundary.ward.null", new String[] { bndryType.getName(), topLevelBoundary.toString() }));
		}

		final StringBuffer query = new StringBuffer(250);
		query.append("select bndry.bndry_num , count(compdtl.complaintid) from eggr_complaintdetails compdtl, ").append(" eg_boundary_type bndrytype,eg_boundary bndry where compdtl.bndry = bndry.id_bndry ")
				.append(" and bndry.id_bndry_type = bndrytype.id_bndry_type and bndrytype.id_bndry_type=" + bndryType.getId());

		if (null != this.fromDate) {
			query.append(" and compdtl.COMPLAINTDATE >= to_date('" + DateUtils.getDefaultFormattedDate(this.fromDate) + "','dd/MM/yyyy')");
		}
		if (null != this.toDate) {
			query.append(" and compdtl.COMPLAINTDATE <= to_date('" + DateUtils.getDefaultFormattedDate(this.toDate) + "','dd/MM/yyyy')");
		}
		query.append(" group by bndry.bndry_num  order by bndry.bndry_num");

		final List<Object[]> list = HibernateUtil.getCurrentSession().createSQLQuery(query.toString()).list();

		this.wardWiseComplaints = new HashMap<String, BigDecimal>();

		final Map<String, BigDecimal> wardWiseExistingComp = new HashMap<String, BigDecimal>();

		for (final Object[] object : list) {

			wardWiseExistingComp.put(object[0].toString(), BigDecimal.valueOf(Double.valueOf(object[1].toString())));
		}

		for (final Boundary boundary : lstOfBoundaries) {
			final String bndryNum = boundary.getBoundaryNum().toString();
			final BigDecimal noOfComplaint = wardWiseExistingComp.get(bndryNum);
			this.wardWiseComplaints.put(bndryNum, null != noOfComplaint ? noOfComplaint : BigDecimal.ZERO);
		}
		GeoLocationService.setKmlDataToCacheAndRequest(this.wardWiseComplaints, GeoLocationConstants.COLORCODES, null);
		return NEW;

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
	 * @return the wardWiseComplaints
	 */
	public Map<String, BigDecimal> getWardWiseComplaints() {
		return this.wardWiseComplaints;
	}

	/**
	 * @param wardWiseComplaints the wardWiseComplaints to set
	 */
	public void setWardWiseComplaints(final Map<String, BigDecimal> wardWiseComplaints) {
		this.wardWiseComplaints = wardWiseComplaints;
	}

	/**
	 * @param boundaryService the boundaryService to set
	 */
	public void setBoundaryService(final BoundaryService boundaryService) {
		this.boundaryService = boundaryService;
	}

}
