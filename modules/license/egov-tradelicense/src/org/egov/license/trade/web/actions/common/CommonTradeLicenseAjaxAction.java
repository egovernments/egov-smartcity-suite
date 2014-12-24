/*
 * @(#)CommonTradeLicenseAjaxAction.java 3.0, 25 Jul, 2013 4:57:53 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.license.trade.web.actions.common;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.license.utils.LicenseUtils;
import org.egov.web.actions.BaseFormAction;

import com.opensymphony.xwork2.Action;

@Result(name = Action.SUCCESS, type = "redirect", location = "CommonTradeLicenseAjaxAction.action")
@Results({ @Result(name = "AJAX_RESULT", type = "stream", location = "returnStream", params = { "contentType", "text/plain" }) })
@ParentPackage("egov")
public class CommonTradeLicenseAjaxAction extends BaseFormAction {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(CommonTradeLicenseAjaxAction.class);
	protected LicenseUtils licenseUtils;
	private BoundaryDAO boundaryDAO;
	private int zoneId;
	private List<Boundary> divisionList = new LinkedList<Boundary>();

	/**
	 * Populate wards.
	 * @return the string
	 */
	public String populateDivisions() {
		try {
			final Boundary boundary = this.boundaryDAO.getBoundary(this.zoneId);
			final String cityName = this.licenseUtils.getAllCity().get(0).getName();
			if (!boundary.getName().equals(cityName)) {
				this.divisionList = this.boundaryDAO.getChildBoundaries(String.valueOf(this.zoneId));
			}
		} catch (final Exception e) {
			LOGGER.error("populateDivisions() - Error while loading divisions ." + e.getMessage());
			this.addFieldError("divisions", "Unable to load division information");
			throw new EGOVRuntimeException("Unable to load division information", e);
		}
		return "ward";
	}

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Convenience method to get the response
	 * @return current response
	 */

	public HttpServletResponse getServletResponse() {
		return ServletActionContext.getResponse();
	}

	public LicenseUtils getLicenseUtils() {
		return this.licenseUtils;
	}

	public void setLicenseUtils(final LicenseUtils licenseUtils) {
		this.licenseUtils = licenseUtils;
	}

	public BoundaryDAO getBoundaryDAO() {
		return this.boundaryDAO;
	}

	public void setBoundaryDAO(final BoundaryDAO boundaryDAO) {
		this.boundaryDAO = boundaryDAO;
	}

	public int getZoneId() {
		return this.zoneId;
	}

	public void setZoneId(final int zoneId) {
		this.zoneId = zoneId;
	}

	public List<Boundary> getDivisionList() {
		return this.divisionList;
	}

	public void setDivisionList(final List<Boundary> divisionList) {
		this.divisionList = divisionList;
	}

}
