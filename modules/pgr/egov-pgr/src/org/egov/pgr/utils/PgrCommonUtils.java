/*
 * @(#)PgrCommonUtils.java 3.0, 23 Jul, 2013 3:29:42 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pgr.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.lib.admbndry.BoundaryType;
import org.egov.lib.admbndry.HeirarchyType;
import org.egov.lib.admbndry.ejb.api.BoundaryTypeService;
import org.egov.lib.admbndry.ejb.api.HeirarchyTypeService;
import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.dept.ejb.api.DepartmentService;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.ejb.api.UserService;
import org.egov.pgr.constant.PGRConstants;

public class PgrCommonUtils {

	private final static Logger LOGGER = Logger.getLogger(PgrCommonUtils.class);
	private BoundaryDAO boundaryDao;
	private BoundaryTypeService boundaryTypeService;
	private HeirarchyTypeService heirarchyTypeService;
	private DepartmentService departmentService;
	private UserService userService;

	public void setCitizenUser() {
		final User user = userService.getUserByUserName(PGRConstants.CITIZEN_USER);
		ServletActionContext.getRequest().getSession(false).setAttribute("com.egov.user.LoginUserName", user.getUserName());
		EGOVThreadLocals.setUserId(String.valueOf(user.getId()));
	}
	
	public  List<String> getCities() {
		final HeirarchyType hType = getHeirarchyType(PGRConstants.HIERARCHY_TYPE_ADMINISTRATION);
		return boundaryDao.getTopBoundaries(hType);
		
	}
	
	public List<Boundary> getAllZoneOfHTypeAdmin() {
		final HeirarchyType hType = getHeirarchyType(PGRConstants.HIERARCHY_TYPE_ADMINISTRATION);
		final BoundaryType bType = this.boundaryTypeService.getBoundaryType(PGRConstants.ZONE_BOUNDARY_TYPE_NAME, hType);
		return this.boundaryDao.getAllBoundariesByBndryTypeId(bType.getId());
	}

	public List<Boundary> populateWard(final Integer zone) {
		try {
			return this.boundaryDao.getChildBoundaries(String.valueOf(zone));
		} catch (final Exception e) {
			LOGGER.error("Error occurred while getting Zone", e);
			throw new EGOVRuntimeException("Error occurred while getting Zone", e);
		}
	}

	public List<Boundary> populateArea(final String hierarchyTypeName, final Integer ward) {
		return getCrossHeirarchyChildren(hierarchyTypeName, ward, PGRConstants.AREA_BOUNDARY_TYPE_NAME);
	}

	public List<Boundary> populateStreets(final String hierarchyTypeName, final Integer area) {
		return getCrossHeirarchyChildren(hierarchyTypeName, area, PGRConstants.STREET_BOUNDARY_TYPE_NAME);
	}

	public List<Department> getAllDepartments() {
		return this.departmentService.getAllDepartments();
	}
	
	public User getUser(final Integer userID) {
		return this.userService.getUserByID(userID);
	}
	
	public static int getParentForGivenLevel(final Boundary bnd, final BoundaryType btypeforgivenLevel) {
		int bndruyInt = 0;
		if (bnd == null || btypeforgivenLevel == null) {
			return 0;
		}
		if (bnd.getBoundaryType().getHeirarchyType() != btypeforgivenLevel.getHeirarchyType()) {
			return 0;
		}
		if (bnd.getBoundaryType().getHeirarchy() < btypeforgivenLevel.getHeirarchy()) {
			return 0;
		}
		if (bnd.getBoundaryType().equals(btypeforgivenLevel)) {
			bndruyInt = bnd.getId().intValue();
			return bndruyInt;
		} else {
			Boundary bnd1 = null;
			bnd1 = bnd.getParent();
			if (bnd1 != null) {
				return getParentForGivenLevel(bnd1, btypeforgivenLevel);
			} else {
				return bndruyInt;
			}
		}
	}

	public static Boolean isValidEmail(final String email) {
		if (null == email) {
			return Boolean.FALSE;
		} else {
			return Pattern.compile(PGRConstants.EMAIL_PATTERN).matcher(email).matches();
		}

	}

	private List<Boundary> getCrossHeirarchyChildren(final String hierarchyTypeName, final Integer ward, final String boundaryType) {
		final HeirarchyType hType = getHeirarchyType(hierarchyTypeName);
		final BoundaryType childBoundaryType = this.boundaryTypeService.getBoundaryType(boundaryType, hType);
		final Boundary parentBoundary = this.boundaryDao.getBoundaryById(ward);
		return new LinkedList(this.boundaryDao.getCrossHeirarchyChildren(parentBoundary, childBoundaryType));
	}

	private HeirarchyType getHeirarchyType(final String name) {
		try {
			return this.heirarchyTypeService.getHierarchyTypeByName(name);
		} catch (final EGOVException e) {
			LOGGER.error("Error while loading_Heirarchy Type", e);
			throw new EGOVRuntimeException("Error while loading_Heirarchy Type", e);
		}
	}
	
	public void setBoundaryDao(final BoundaryDAO boundaryDao) {
		this.boundaryDao = boundaryDao;
	}

	public void setBoundaryTypeService(final BoundaryTypeService boundaryTypeService) {
		this.boundaryTypeService = boundaryTypeService;
	}

	public void setHeirarchyTypeService(final HeirarchyTypeService heirarchyTypeService) {
		this.heirarchyTypeService = heirarchyTypeService;
	}

	public void setDepartmentService(final DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	public void setUserService(final UserService userService) {
		this.userService = userService;
	}

}
