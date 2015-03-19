/*
 * @(#)EgovInfrastrUtilInteface.java 3.0, 7 Jun, 2013 10:55:37 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons.utils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.infstr.commons.Module;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryType;

public interface EgovInfrastrUtilInteface {

	/**
	 * A map of BoundaryID vs boundaryName
	 */
	public Map getBoundaryMap();

	/**
	 * A map of departmentID vs departmentName
	 */
	public Map getDeptMap();

	/**
	 * A List of all BoundaryIDs
	 */
	public List getBoundaryList();

	/**
	 * A List of all departmentIDs
	 */
	public List getDeptList();

	/**
	 * A map of BoundaryTypeID vs BoundaryTypeName
	 * @nil
	 */
	public Map getMapOfBoundryTypes();

	/**
	 * A map of RoleID vs RoleName
	 * @nil
	 */
	public Map getRoleIdMap();

	/**
	 * A map of RoleName vs RoleID
	 * @nil
	 */
	public Map getRoleNameMap();

	/**
	 * A map of toplevelid vs a list of (userId vs username seperated by a stringtokeniser)
	 * @nil
	 *//*
	public Map getallUsersAcrossDeptMap();

	*//**
	 * A map of toplevelid vs a map of (userId vs username)
	 * @nil
	 *//*
	public Map getallmapUsersAcrossDeptMap();*/

	/**
	 * A map of userid vs roleid
	 * @nil
	 */
	public Map getuserRoleMap();

	public Map getBoundaryMap(Set bndrySet);

	public List getBoundaryList(Set bndrySet);

	public List getBndryTypeBndryList(Boundary topBoundary, BoundaryType incluedBType);

	/*
	 * Returns Map of AccountName Vs AccountCode
	 */
	public String[] getExcludedAccHeadsForModule(Module module);

	public List getModesList();

	public Map getAllInstallmentYears();

	public Date getFromDateofAssessmentYear(Integer Id);

	public String getBoundaryName(Integer id);

	public String getInstallmentYearsStr(Integer Id);

	public String getReportDate(String toDate, String fromDate, String id);

	public String encodingName(String name);

}