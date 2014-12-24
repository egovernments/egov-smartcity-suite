/*
 * @(#)AjaxPgrAction.java 3.0, 23 Jul, 2013 3:29:42 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pgr.web.actions.common;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.config.dao.AppConfigValuesDAO;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.rjbac.dept.dao.DepartmentDAO;
import org.egov.lib.rjbac.user.User;
import org.egov.pgr.constant.PGRConstants;
import org.egov.pgr.utils.PgrCommonUtils;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.service.EisUtilService;
import org.egov.web.actions.BaseFormAction;

public class AjaxPgrAction extends BaseFormAction {

	private static final Logger LOGGER = Logger.getLogger(AjaxPgrAction.class);
	private static final long serialVersionUID = 1L;
	private List<Boundary> wardList = new LinkedList<Boundary>();
	private List<Boundary> streetList = new LinkedList<Boundary>();
	private List<Boundary> areaList = new LinkedList<Boundary>();
	private List<User> userList = new LinkedList<User>();
	private Integer zoneId;
	private Integer wardId;
	private Integer departmentId;
	private Integer designationId;
	private AppConfigValuesDAO appConfigValuesDAO;
	private EisUtilService eisService;
	private List<DesignationMaster> designationList;
	private PgrCommonUtils pgrCommonUtils;

	@Override
	public Object getModel() {
		return null;
	}

	public String populateWard() {
		this.wardList = this.pgrCommonUtils.populateWard(this.zoneId);
		return PGRConstants.ACTION_METHOD_RETURN_WARDS;
	}

	/**
	 * Populate the Area list by ward
	 */
	public String populateArea() {
		final AppConfigValues appConfigValue = this.appConfigValuesDAO.getConfigValuesByModuleAndKey(PGRConstants.MODULE_NAME, "LOCATIONHTYPE").get(0);
		this.areaList = this.pgrCommonUtils.populateArea(appConfigValue.getValue(), this.wardId);
		return PGRConstants.ACTION_METHOD_RETURN_AREAS;
	}

	public String populateStreets() throws Exception {
		final AppConfigValues appConfigValue = this.appConfigValuesDAO.getConfigValuesByModuleAndKey(PGRConstants.MODULE_NAME, "LOCATIONHTYPE").get(0);
		this.streetList = this.pgrCommonUtils.populateStreets(appConfigValue.getValue(), this.wardId);
		return PGRConstants.ACTION_METHOD_RETURN_STREETS;
	}

	public String populateUser() {
		try {
			this.userList = new DepartmentDAO().getAllUsersByDept(new DepartmentDAO().getDepartment(this.departmentId), 1);
		} catch (final Exception e) {
			LOGGER.error("Error while loading user - users." + e.getMessage());
			addFieldError("location", "Unable to load user information");
			throw new EGOVRuntimeException("Unable to load user information", e);
		}

		return "users";
	}

	public String ajaxLoadDesg() {
		LOGGER.debug("AjaxPgrAction | ajaxLoadDesg | Start ");

		this.designationList = this.eisService.getAllDesignationByDept(this.departmentId, new Date());

		return "desg";
	}

	public String ajaxLoadUser() {
		LOGGER.debug("AjaxPgrAction | ajaxLoadUser | Start ");

		this.userList = this.eisService.getUsersByDeptAndDesig(this.departmentId, this.designationId, new Date());

		return "users";
	}

	public List<Boundary> getStreetList() {
		return this.streetList;
	}

	public void setStreetList(final List<Boundary> streetList) {
		this.streetList = streetList;
	}

	public List<Boundary> getAreaList() {
		return this.areaList;
	}

	public void setAreaList(final List<Boundary> areaList) {
		this.areaList = areaList;
	}

	public List<Boundary> getWardList() {
		return this.wardList;
	}

	public void setWardList(final List<Boundary> wardList) {
		this.wardList = wardList;
	}

	public Integer getWardId() {
		return this.wardId;
	}

	public void setWardId(final int wardId) {
		this.wardId = wardId;
	}

	public Integer getDepartmentId() {
		return this.departmentId;
	}

	public void setDepartmentId(final Integer departmentId) {
		this.departmentId = departmentId;
	}

	public List<User> getUserList() {
		return this.userList;
	}

	public void setAppConfigValuesDAO(final AppConfigValuesDAO appConfigValuesDAO) {
		this.appConfigValuesDAO = appConfigValuesDAO;
	}

	/**
	 * @return the zoneId
	 */
	public Integer getZoneId() {
		return this.zoneId;
	}

	/**
	 * @param zoneId the zoneId to set
	 */
	public void setZoneId(final Integer zoneId) {
		this.zoneId = zoneId;
	}

	/**
	 * @param wardId the wardId to set
	 */
	public void setWardId(final Integer wardId) {
		this.wardId = wardId;
	}

	/**
	 * @param eisService the eisService to set
	 */
	public void setEisService(final EisUtilService eisService) {
		this.eisService = eisService;
	}

	/**
	 * @return the designationId
	 */
	public Integer getDesignationId() {
		return this.designationId;
	}

	/**
	 * @param designationId the designationId to set
	 */
	public void setDesignationId(final Integer designationId) {
		this.designationId = designationId;
	}

	/**
	 * @return the designationList
	 */
	public List<DesignationMaster> getDesignationList() {
		return this.designationList;
	}

	public void setPgrCommonUtils(final PgrCommonUtils pgrCommonUtils) {
		this.pgrCommonUtils = pgrCommonUtils;
	}

}
