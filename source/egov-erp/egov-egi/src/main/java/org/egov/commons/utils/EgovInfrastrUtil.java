/*
 * @(#)EgovInfrastrUtil.java 3.0, 7 Jun, 2013 10:53:37 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons.utils;

import org.egov.commons.Installment;
import org.egov.commons.service.CommonsService;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.User;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.commons.Module;
import org.egov.infstr.utils.EGovConfig;
import org.egov.infstr.utils.ExcludeBndryType;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryType;
import org.egov.lib.admbndry.HeirarchyType;
import org.egov.lib.admbndry.ejb.api.BoundaryService;
import org.egov.lib.admbndry.ejb.api.BoundaryTypeService;
import org.egov.lib.admbndry.ejb.api.HeirarchyTypeService;
import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.dept.ejb.api.DepartmentService;
import org.egov.lib.rjbac.role.Role;
import org.egov.lib.rjbac.role.ejb.api.RoleService;
import org.egov.lib.rjbac.user.dao.UserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class EgovInfrastrUtil implements EgovInfrastrUtilInteface {
	private static Map resetMap = new HashMap();
	private static final Integer idAll = new Integer(0);
	private static final Integer idBefore = new Integer(1);
	private static final Integer idAfter = new Integer(2);
	private static final Integer idBetween = new Integer(3);
	private static final Integer idCurrent = new Integer(4);
	private BoundaryTypeService boundaryTypeService;
	private BoundaryService boundaryService;
	private HeirarchyTypeService heirarchyTypeService;
	private DepartmentService departmentService;
	private RoleService roleService;
	private CommonsService commonsService;
	private UserDAO userDAO;

	static String installmentForYr2000 = EGovConfig.getProperty("ID_INSTALLMENT_2000", "", "PT");

	private static HashMap modeslistMap = new HashMap();
	private static HashMap deptlistMap = new HashMap();

	private static HashMap bndrymap = new LinkedHashMap();
	private static HashMap deptmap = new HashMap();
	private static HashMap bndrylistMap = new HashMap();

	private static ArrayList modeslist = new ArrayList();
	private static Map bndryTypeBndryMap = new HashMap();
	private static ArrayList deptlist = new ArrayList();
	private static HashMap map = new HashMap();
	private static HashMap Rolename = new HashMap();
	private static HashMap RoleIdname = new HashMap();
	private static HashMap userIDlistMap = new HashMap();
	private static HashMap userIDMap = new HashMap();
	private static HashMap userRoleMap = new HashMap();
	private static Logger logger = LoggerFactory.getLogger(EgovInfrastrUtil.class);
	private static Map moduleWiseAccountHeads = new HashMap();
	private static Map moduleWiseInstallments = new HashMap();
	private static Map trLocationNameAndIdMap = new HashMap();
	private static Map trLocationAndIdMap = new HashMap();
	private static Map modulesMap = new HashMap();
	private static Map acHeadsNameMap = new HashMap();
	private static Map installmentIdAndYearsMap = new TreeMap();
	private static Map installmentIdAndFromDateMap = new HashMap();
	private static Map bankAccountMap_WithAccountNo = new HashMap();
	private static Map bankAccountMap_WithoutAccountNo = new HashMap();

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public void setBoundaryTypeService(final BoundaryTypeService boundaryTypeService) {
		this.boundaryTypeService = boundaryTypeService;
	}

	public void setBoundaryService(final BoundaryService boundaryService) {
		this.boundaryService = boundaryService;
	}

	public void setHeirarchyTypeService(final HeirarchyTypeService heirarchyTypeService) {
		this.heirarchyTypeService = heirarchyTypeService;
	}

	public void setDepartmentService(final DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	public void setRoleService(final RoleService roleService) {
		this.roleService = roleService;
	}

	public void setCommonsService(final CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	public void resetCache() {
		final String domainName = EGOVThreadLocals.getDomainName();
		if (resetMap.containsKey(domainName)) {
			resetMap.remove(domainName);
			resetMap.put(domainName, "Y");
		} else {
			resetMap.put(domainName, "Y");
		}
	}

	/*@Override
	public Map getallUsersAcrossDeptMap() {
		final String domainName = EGOVThreadLocals.getDomainName();
		if (userIDlistMap.isEmpty() || !userIDlistMap.containsKey(domainName) || resetMap.get(domainName).equals("Y")) {
			this.updateUserIdListMap();
		}
		final Map domainUserIDlistMap = (Map) userIDlistMap.get(domainName);

		return domainUserIDlistMap;

	}

	@Override
	public Map getallmapUsersAcrossDeptMap() {
		final String domainName = EGOVThreadLocals.getDomainName();
		if (userIDMap.isEmpty() || !userIDMap.containsKey(domainName) || resetMap.get(domainName).equals("Y")) {
			this.updateUserIdListMap();
		}
		final Map domainUserIDMap = (Map) userIDMap.get(domainName);

		return domainUserIDMap;
	}*/

	@Override
	public Map getRoleIdMap() {
		final String domainName = EGOVThreadLocals.getDomainName();
		if (RoleIdname.isEmpty() || !RoleIdname.containsKey(domainName) || resetMap.get(domainName).equals("Y")) {
			this.updateUserRoleMap();
		}
		final Map domainRoleIdname = (Map) RoleIdname.get(domainName);
		return domainRoleIdname;
	}

	@Override
	public Map getRoleNameMap() {
		final String domainName = EGOVThreadLocals.getDomainName();
		if (Rolename.isEmpty() || !Rolename.containsKey(domainName) || resetMap.get(domainName).equals("Y")) {
			this.updateUserRoleMap();
		}
		final Map domainRolename = (Map) Rolename.get(domainName);

		return domainRolename;
	}

	@Override
	public Map getBoundaryMap() {
		final String domainName = EGOVThreadLocals.getDomainName();
		if (bndrymap.isEmpty() || !bndrymap.containsKey(domainName) || resetMap.get(domainName).equals("Y")) {
			this.updateBoundaryMap();
		}
		final Map domainBndrymap = (Map) bndrymap.get(domainName);

		return domainBndrymap;
	}

	@Override
	public String getBoundaryName(final Integer id) {
		final String domainName = EGOVThreadLocals.getDomainName();

		if (bndrymap.isEmpty() || !bndrymap.containsKey(domainName) || resetMap.get(domainName).equals("Y")) {
			this.updateBoundaryMap();
		}
		final Map domainBndrymap = (Map) bndrymap.get(domainName);

		return (String) domainBndrymap.get(id);
	}

	@Override
	public Map getDeptMap() {
		final String domainName = EGOVThreadLocals.getDomainName();

		if (deptmap.isEmpty() || !deptmap.containsKey(domainName) || resetMap.get(domainName).equals("Y")) {
			this.updateDeptMap();
		}
		map = (HashMap) deptmap.get(domainName);
		return map;
	}

	@Override
	public List getBoundaryList() {

		final String domainName = EGOVThreadLocals.getDomainName();

		if (bndrylistMap.isEmpty() || !bndrylistMap.containsKey(domainName) || resetMap.get(domainName).equals("Y")) {
			this.updateBoundaryMap();
		}
		final List bndrylist = (ArrayList) bndrylistMap.get(domainName);
		return bndrylist;
	}

	@Override
	public List getModesList() {
		final String domainName = EGOVThreadLocals.getDomainName();
		if (modeslistMap.isEmpty() || !modeslistMap.containsKey(domainName) || resetMap.get(domainName).equals("Y")) {
			this.updateModesListMap();
		}
		final List modelist = (ArrayList) modeslistMap.get(domainName);
		return modelist;
	}

	@Override
	public List getDeptList() {
		final String domainName = EGOVThreadLocals.getDomainName();
		if (deptlistMap.isEmpty() || !deptlistMap.containsKey(domainName) || resetMap.get(domainName).equals("Y")) {
			this.updateDeptMap();
		}
		final List domainDeptlist = (ArrayList) deptlistMap.get(domainName);
		return domainDeptlist;
	}

	@Override
	public Map getMapOfBoundryTypes() {
		final String domainName = EGOVThreadLocals.getDomainName();

		if (map.isEmpty() || !map.containsKey(domainName) || resetMap.get(domainName).equals("Y")) {
			this.updateBoundaryMap();
		}
		final Map domainBndrytype = (Map) map.get(domainName);
		return domainBndrytype;
	}

	@Override
	public Map getuserRoleMap() {
		final String domainName = EGOVThreadLocals.getDomainName();

		if (userRoleMap.isEmpty() || !userRoleMap.containsKey(domainName) || resetMap.get(domainName).equals("Y")) {
			this.updateUserRoleMap();
		}
		final Map domainUserRoleMap = (Map) userRoleMap.get(domainName);
		return domainUserRoleMap;
	}

	@Override
	public List getBndryTypeBndryList(final Boundary topBoundary, final BoundaryType incluedBType) {
		final String domainName = EGOVThreadLocals.getDomainName();

		if (bndryTypeBndryMap.isEmpty() || !bndryTypeBndryMap.containsKey(domainName) || resetMap.get(domainName).equals("Y")) {

			this.updateBoundaryMap();
		}
		final Map domainBndryTypeBndryMap = (Map) bndryTypeBndryMap.get(domainName);
		return (List) ((Map) domainBndryTypeBndryMap.get(topBoundary)).get(incluedBType);
	}

	@Override
	public String[] getExcludedAccHeadsForModule(final Module module) {
		String[] excludeAccHeads = null;
		if (module.getModuleName().equalsIgnoreCase("EG-PT-TAX")) {
			excludeAccHeads = EGovConfig.getProperty("EXCLUDE_ACC_HEADS", "", "PT").split(",");
		}
		return excludeAccHeads;
	}

	private static void getclild(final List l, final BoundaryType boundaryType) {
		if (boundaryType.getChildBoundaryTypes() != null && !boundaryType.getChildBoundaryTypes().isEmpty()) {
			final Set childBndryTypes = boundaryType.getChildBoundaryTypes();
			for (final Iterator itr3 = childBndryTypes.iterator(); itr3.hasNext();) {
				final BoundaryType chBndryType = (BoundaryType) itr3.next();
				l.add(chBndryType);
				getclild(l, chBndryType);
			}
		}
	}

	/* Added by Poornima on 01-03-2006 to get All Installment Years */
	@Override
	public Map getAllInstallmentYears() {
		final String domainName = EGOVThreadLocals.getDomainName();
		if (installmentIdAndYearsMap.isEmpty() || !installmentIdAndYearsMap.containsKey(domainName) || resetMap.get(domainName).equals("Y")) {
			this.updateInstYearMap();
		}
		final Map domaininstallmentIdAndYearsMap = (Map) installmentIdAndYearsMap.get(domainName);
		return domaininstallmentIdAndYearsMap;
	}

	/*
	 * @param Id Returns the From Date of the Assessment Year. added by Sapna
	 */
	@Override
	public Date getFromDateofAssessmentYear(final Integer Id) {
		final String domainName = EGOVThreadLocals.getDomainName();
		if (installmentIdAndFromDateMap.isEmpty() || !installmentIdAndFromDateMap.containsKey(domainName) || resetMap.get(domainName).equals("Y")) {
			this.updateInstYearMap();
		}
		final Map domainInstallmentIdAndFromDateMap = (Map) installmentIdAndFromDateMap.get(domainName);
		return (Date) domainInstallmentIdAndFromDateMap.get(Id);
	}

	/*
	 * This method returns Installment Year in this format YYYY-YY
	 */
	@Override
	public String getInstallmentYearsStr(final Integer Id) {
		final String domainName = EGOVThreadLocals.getDomainName();
		if (installmentIdAndYearsMap.isEmpty() || !installmentIdAndYearsMap.containsKey(domainName) || resetMap.get(domainName).equals("Y")) {
			this.updateInstYearMap();
		}
		final Map domainInstallmentIdAndYearsMap = (Map) installmentIdAndYearsMap.get(domainName);
		return (String) domainInstallmentIdAndYearsMap.get(Id);
	}

	private synchronized void updateInstYearMap() {
		final String domainName = EGOVThreadLocals.getDomainName();
		try {
			final SimpleDateFormat sdfyr = new SimpleDateFormat("yyyy");
			final Map tempInstallmentIdAndYearsMap = new TreeMap();
			final Map tempInstallmentIdAndFromDateMap = new TreeMap();

			final List Installmentslist = this.commonsService.getAllInstallments();
			final Iterator itrInstmt = Installmentslist.iterator();
			while (itrInstmt.hasNext()) {
				String strInsYear = "";
				Date fromDate = null;
				Date insYear = null;
				int nextYear = 0;
				String nextyr = "";
				final Installment installment = (Installment) itrInstmt.next();
				final Integer id = installment.getId();
				if (installmentForYr2000 != null && !installmentForYr2000.equals("") && id.equals(new Integer(installmentForYr2000))) {
					insYear = installment.getInstallmentYear();
					strInsYear = sdfyr.format(insYear);
					fromDate = installment.getFromDate();
					nextYear = Integer.valueOf(strInsYear).intValue() + 1;
					nextyr = Integer.valueOf(nextYear).toString();
					final String name = "Prior To";
					strInsYear = name + "  " + nextyr;
				} else {
					insYear = installment.getInstallmentYear();
					strInsYear = sdfyr.format(insYear);
					fromDate = installment.getFromDate();
					nextYear = Integer.valueOf(strInsYear).intValue() + 1;
					nextyr = Integer.valueOf(nextYear).toString();
					nextyr = nextyr.substring(2);
					strInsYear = strInsYear + "-" + nextyr;
				}
				tempInstallmentIdAndYearsMap.put(id, strInsYear);
				tempInstallmentIdAndFromDateMap.put(id, fromDate);
			}
			installmentIdAndYearsMap.put(domainName, tempInstallmentIdAndYearsMap);
			installmentIdAndFromDateMap.put(domainName, tempInstallmentIdAndFromDateMap);
		} catch (final Exception sqe) {
			throw new EGOVRuntimeException(sqe.getMessage());
		}
		resetMap.put(domainName, "N");
	}

	private synchronized void updateBoundaryMap() {
		final String domainName = EGOVThreadLocals.getDomainName();
		try {
			final HeirarchyType htype = this.heirarchyTypeService.getHeirarchyTypeByID(1);
			final List topLevelBndryList = this.boundaryService.getTopBoundaries(htype);
			Iterator topLevelBndryIter = topLevelBndryList.iterator();
			final BoundaryType topLevelBndryType = this.boundaryTypeService.getTopBoundaryType(htype);
			final HashMap tempbndrymap = new HashMap();
			final List boundaryTypeList = new ArrayList();
			final HashMap tempmap = new HashMap();

			// gets all its clildbndryTypes reccursively
			getclild(boundaryTypeList, topLevelBndryType);
			// adds the topmost bndryType to the list of boundary type
			boundaryTypeList.add(topLevelBndryType);
			// iterating through the topboundary list
			final List bndrylist = new ArrayList();
			while (topLevelBndryIter.hasNext()) {
				final Boundary topbndry = (Boundary) topLevelBndryIter.next();
				final Iterator boundaryTypeitr = boundaryTypeList.iterator();
				// iterating through the boundary types
				while (boundaryTypeitr.hasNext()) {
					final BoundaryType boundaryType1 = (BoundaryType) boundaryTypeitr.next();
					// getting the boundary sets for each boundarytype and the topmostboundary
					final List getAllBoundariesset = this.boundaryService.getAllBoundaries(boundaryType1, topbndry.getId().intValue());
					final Iterator getAllBoundariessetitr = getAllBoundariesset.iterator();
					while (getAllBoundariessetitr.hasNext()) {
						final Boundary boundary = (Boundary) getAllBoundariessetitr.next();
						// adding the boundaryid and the name to the static bndrymap
						final String bndryNumNameStr = boundary.getBoundaryNum() + ".  " + boundary.getName();
						tempbndrymap.put(boundary.getId(), bndryNumNameStr);
						// adding the boundaryids to static bndrylist
						bndrylist.add(boundary.getId());
					}

				}

			}
			bndrymap.put(domainName, tempbndrymap);
			bndrylistMap.put(domainName, bndrylist);
			final Iterator boundarytypeitr = boundaryTypeList.iterator();
			// iterating through all the BoundaryTypes
			while (boundarytypeitr.hasNext()) {
				final BoundaryType boundaryty = (BoundaryType) boundarytypeitr.next();
				// adding the BoundaryTypeid and the BoundaryTypename to the static map
				tempmap.put(boundaryty.getId(), boundaryty.getName());

			}
			map.put(domainName, tempmap);

			Map temp1BndryTypeBndryMap = new HashMap();
			final Map temp2BndryTypeBndryMap = new HashMap();

			topLevelBndryIter = topLevelBndryList.iterator();
			while (topLevelBndryIter.hasNext()) {
				final Boundary topLevelBoundary = (Boundary) topLevelBndryIter.next();
				if (!bndryTypeBndryMap.isEmpty() && bndryTypeBndryMap.containsKey(domainName)) {
					temp1BndryTypeBndryMap = (Map) bndryTypeBndryMap.get(domainName);
				}
				Map bTBndry = null;
				if (!temp1BndryTypeBndryMap.isEmpty()) {
					bTBndry = new HashMap();
					bTBndry = (Map) temp1BndryTypeBndryMap.get(topLevelBoundary);
				}
				if (bTBndry == null) {
					bTBndry = new HashMap();
					temp2BndryTypeBndryMap.put(topLevelBoundary, bTBndry);
					// bndryTypeBndryMap.put(topLevelBoundary,bTBndry);
				}
				final ArrayList al = (ArrayList) ExcludeBndryType.getExcludeType();
				final Map domainMap = (Map) map.get(domainName);
				final Map typeMapm1 = getExtractMap(al, domainMap);
				final Set typeset = new TreeSet(typeMapm1.keySet());
				final Iterator itr = typeset.iterator();

				while (itr.hasNext()) {
					final Integer parentType = (Integer) itr.next();
					final String typeName = (String) typeMapm1.get(parentType);
					final BoundaryType btype = this.boundaryTypeService.getBoundaryType(typeName, htype);
					List bTypeBndryList = (List) bTBndry.get(btype);
					if (bTypeBndryList == null) {
						bTypeBndryList = new ArrayList();
						bTBndry.put(btype, bTypeBndryList);
					}
					final List bondrySet = this.boundaryService.getAllBoundaries(btype, topLevelBoundary.getId().intValue());
					if (bondrySet != null && !bondrySet.isEmpty()) {
						final Iterator bsetitr = bondrySet.iterator();
						// List bndryidlist =new ArrayList();
						while (bsetitr.hasNext()) {
							final Boundary bundary = (Boundary) bsetitr.next();
							bTypeBndryList.add(bundary);

						}

					}
				}
			}
			if (!temp2BndryTypeBndryMap.isEmpty()) {
				bndryTypeBndryMap.put(domainName, temp2BndryTypeBndryMap);
			}

		} catch (final Exception sqe) {
			throw new EGOVRuntimeException(sqe.getMessage());
		}
		resetMap.put(domainName, "N");
	}

	private synchronized void updateDeptMap() {
		final String domainName = EGOVThreadLocals.getDomainName();
		try {
			final List deptList = this.departmentService.getAllDepartments();
			final Iterator deptIter = deptList.iterator();
			final HashMap tempdeptmap = new HashMap();
			// iterating through all the Department objects
			while (deptIter.hasNext()) {
				final Department department = (Department) deptIter.next();
				// adding the departmentid and the departmentname to the static deptmap
				tempdeptmap.put(department.getId(), department.getDeptName());

				// adding the departmentids to static deptlist
				deptlist.add(department.getId());
			}
			deptlistMap.put(domainName, deptlist);
			deptmap.put(domainName, tempdeptmap);

		} catch (final Exception sqe) {
			throw new EGOVRuntimeException(sqe.getMessage());
		}
		resetMap.put(domainName, "N");
	}

	/*private synchronized void updateUserIdListMap() {
		final String domainName = EGOVThreadLocals.getDomainName();
		try {
			final HeirarchyType htype = this.heirarchyTypeService.getHeirarchyTypeByID(1);
			final List topLevelBndryList = this.boundaryService.getTopBoundaries(htype);
			Iterator topLevelBndryIter = topLevelBndryList.iterator();
			final HashMap tempuserIDMap = new HashMap();
			List userIDlist = null;
			Map userIDmap = null;
			topLevelBndryIter = topLevelBndryList.iterator();
			// iterate through the above list
			while (topLevelBndryIter.hasNext()) {
				final Boundary topbndry = (Boundary) topLevelBndryIter.next();
				// get all the departments
				final List depList = this.departmentService.getAllDepartments();
				final Iterator deptitr = depList.iterator();
				userIDlist = new ArrayList();
				userIDmap = new HashMap();
				// iterate through the depts
				while (deptitr.hasNext()) {
					final Department department = (Department) deptitr.next();
					// get All Users By Dept and the toplevel boundaryid
					final List userList = this.departmentService.getAllUsersByDept(department, topbndry.getId().intValue());
					final Iterator useritr = userList.iterator();
					// iterate through the users object
					Set roles = null;
					Role role = null;
					Integer roleID = null;
					List l = null;
					String name = "";
					String roleName = "";
					while (useritr.hasNext()) {
						final User user = (User) useritr.next();
						final String fromuserName = ""
								+ (user.getFirstName() == null ? "" : user.getFirstName()) + " " + (user.getMiddleName() == null ? "" : user.getMiddleName()) + " " + (user.getLastName() == null ? ""
										: user.getLastName());
						final Long userID = user.getId();
						final Department department1 = user.getDepartment();
						roles = user.getRoles();
						if (roles != null && !roles.isEmpty()) {
							l = new ArrayList(roles);
							role = (Role) l.get(0);
							roleID = role.getId();
						}
						if (roleID != null) {
							roleName = (String) ((Map) RoleIdname.get(domainName)).get(roleID);
							name = "" + fromuserName + "/" + roleName + "-" + department1.getDeptName();
						} else {
							name = "" + fromuserName + "-" + department1.getDeptName();
						}
						// add the users to users list
						userIDlist.add(userID.toString() + "|*|" + name);
						// populate the userIDmap of userids and usernames
						userIDmap.put(userID, name);
					}

				}
				// populate the static userIDMap by putting the toplevelbndryid and above userIDmap
				tempuserIDMap.put(topbndry.getId(), userIDmap);
				userIDMap.put(domainName, tempuserIDMap);
				// populate the static userIDlistMap by putting the toplevelbndryid and above userIDlist
				userIDlistMap.put(topbndry.getId(), userIDlist);

			}

		} catch (final Exception sqe) {
			throw new EGOVRuntimeException(sqe.getMessage());
		}
		resetMap.put(domainName, "N");
	}*/

	private synchronized void updateModesListMap() {
		final String domainName = EGOVThreadLocals.getDomainName();
		try {
			final String modeString = EGovConfig.getProperty("MODES", "", "COLLECTION_MODES");
			String[] modeArray = null;
			final List tempModesList = new ArrayList();
			modeArray = modeString.split(",");
			for (final String element : modeArray) {
				tempModesList.add(element);
			}

			modeslistMap.put(domainName, tempModesList);
		} catch (final Exception sqe) {
			throw new EGOVRuntimeException(sqe.getMessage());
		}
		resetMap.put(domainName, "N");
	}

	private synchronized void updateUserRoleMap() {
		final String domainName = EGOVThreadLocals.getDomainName();
		try {

			final HashMap tempRolename = new HashMap();
			final HashMap tempRoleIdname = new HashMap();
			final HashMap tempuserRoleMap = new HashMap();
			final HeirarchyType htype = this.heirarchyTypeService.getHeirarchyTypeByID(1);
			final List topLevelBndryList = this.boundaryService.getTopBoundaries(htype);
			topLevelBndryList.iterator();

			// commented to fetch only PGR Roles
			final List roleList = this.getAllPGRRoles(this.roleService);
			Iterator roleListitr = roleList.iterator();
			// iterating through all the Roles
			while (roleListitr.hasNext()) {
				final Role role = (Role) roleListitr.next();
				// adding the Roleid and the Rolename to the static RoleIdname
				tempRoleIdname.put(role.getId(), role.getRoleName());
				// adding the Rolename and the Roleid to the static Rolename
				tempRolename.put(role.getRoleName(), role.getId());

			}
			RoleIdname.put(domainName, tempRoleIdname);
			Rolename.put(domainName, tempRolename);

			removeExcludedRole(roleList);

			roleListitr = roleList.iterator();
			// iterate through all the roles
			while (roleListitr.hasNext()) {
				final Role role = (Role) roleListitr.next();
				topLevelBndryList.iterator();
				Iterator userListitr = null;
				List userList = null;
				// iterate through the top levelboundaries
				final String roleUsers = role.getRoleName() + ",";
				// get the users list by Role and toplevelboundary
				userList = userDAO.getAllUserForRoles(roleUsers, new Date());
				// if the role is citizen then userList is empty amd it goes to else group
				if (userList != null && !userList.isEmpty()) {
					userListitr = userList.iterator();
					// iterate through the userlist
					while (userListitr.hasNext()) {
						final User user = (User) userListitr.next();
						// populate the static userRoleMap by putting the userid and roleid
						tempuserRoleMap.put(user.getId(), role.getId());

					}
				}
				// here is the users list for citizen

				else {
					userList = userDAO.getAllUserForRoles(roleUsers, new Date());
					userListitr = userList.iterator();
					// iterate through the userlist
					while (userListitr.hasNext()) {
						final User user = (User) userListitr.next();
						// populate the static userRoleMap by putting the userid and roleid
						tempuserRoleMap.put(user.getId(), role.getId());

					}

				}

				userRoleMap.put(domainName, tempuserRoleMap);

			}

		} catch (final Exception sqe) {
			throw new EGOVRuntimeException(sqe.getMessage());
		}

		resetMap.put(domainName, "N");
	}

	private List getAllPGRRoles(final RoleService rmang) {
		final String roleNames = EGovConfig.getProperty("pgr_config.xml", "ROLENAMES", "", "PGR");
		final String[] roles = roleNames.split(",");
		final List rolesList = new ArrayList();
		Role r = null;
		for (final String role : roles) {
			r = rmang.getRoleByRoleName(role);
			if (r != null) {
				rolesList.add(r);
			}
		}
		return rolesList;

	}

	@Override
	public Map getBoundaryMap(final Set bndrySet) {
		final HashMap bndrymap = new HashMap();
		try {
			if (bndrySet != null && !bndrySet.isEmpty()) {
				final Iterator bndryIter = bndrySet.iterator();
				while (bndryIter.hasNext()) {
					final Boundary bndry = (Boundary) bndryIter.next();
					bndrymap.put(bndry.getId(), bndry.getName());
				}
			}

		} catch (final Exception e) {
			throw new EGOVRuntimeException(e.getMessage());
		}
		return bndrymap;

	}

	@Override
	public List getBoundaryList(final Set bndrySet) {

		final ArrayList bndrylist = new ArrayList();
		try {
			if (bndrySet != null && !bndrySet.isEmpty()) {
				final Iterator bndryIter = bndrySet.iterator();
				while (bndryIter.hasNext()) {
					final Boundary bndry = (Boundary) bndryIter.next();
					bndrylist.add(bndry.getId());

				}
			}
		} catch (final Exception e) {
			throw new EGOVRuntimeException(e.getMessage());
		}

		return bndrylist;

	}

	private static Map getExtractMap(final List al, final Map typeMap) {
		final Set set = typeMap.keySet();
		final HashMap hm = new HashMap(typeMap);
		for (final Iterator itr = set.iterator(); itr.hasNext();) {
			final Integer i = (Integer) itr.next();
			final String s = (String) typeMap.get(i);
			for (final Iterator itr1 = al.iterator(); itr1.hasNext();) {
				final String s1 = (String) itr1.next();
				if (s.equals(s1)) {
					hm.remove(i);

				}
			}
		}
		return hm;
	}

	/*
	 * This method written especially To display the selected dates in reports from which date to which
	 */
	@Override
	public String getReportDate(final String fromDate, final String toDate, final String id) {

		String displayDate = null;
		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		final java.util.Date date = new java.util.Date();
		if (id != null && !id.equals("") && id.equals(idAll.toString())) {
			if (fromDate != null && !fromDate.equals("")) {
				displayDate = "From:" + " " + fromDate + "  " + "To:" + " " + sdf.format(date);// all
			}
		}

		else if (id != null && !id.equals("") && id.equals(idBefore.toString())) {
			if (toDate != null && !toDate.equals("")) {
				displayDate = "From:" + " " + fromDate + "  " + "To:" + " " + toDate;// before
			}
		}

		else if (id != null && !id.equals("") && id.equals(idAfter.toString())) {
			if (fromDate != null) {
				displayDate = "From:" + " " + fromDate + "  " + "To:" + " " + sdf.format(date);// after
			}

		} else if (id != null && !id.equals("") && id.equals(idBetween.toString())) {
			if (fromDate != null && !fromDate.equals("") && toDate != null && !toDate.equals("")) {
				displayDate = "From:" + " " + fromDate + "  " + "To:" + " " + toDate;// between
			}
		}

		else if (id != null && !id.equals("") && id.equals(idCurrent.toString()))// current
		{
			displayDate = "CurrentDate" + " " + sdf.format(date);
		}
		return displayDate;

	}

	@Override
	@Deprecated
	public String encodingName(String name) {
		return org.egov.infstr.utils.StringUtils.encodeString(name);
	}

	private static void removeExcludedRole(final List roleList) {
		final Iterator iter = roleList.iterator();
		final String[] temp = new String[10];
		final String[] excluded_roles = EGovConfig.getArray("EXCLUDE_ROLES", temp, "GENERAL");
		final List exRoleList = Arrays.asList(excluded_roles);
		if (exRoleList.size() != 0) {
			while (iter.hasNext()) {
				final Role role = (Role) iter.next();
				if (exRoleList.contains(role.getRoleName())) {
					iter.remove();
				}
			}
		}
	}

}
