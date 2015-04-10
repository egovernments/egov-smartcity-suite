/*
 * @(#)UserDelegate.java 3.0, 18 Jun, 2013 1:33:15 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.client.delegate;


public class UserDelegate {
	
	/*private UserService userService ;
	private HeirarchyTypeService heirarchyTypeService;
	private BoundaryServiceOld boundaryService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private DepartmentService departmentService;
	private BoundaryTypeService boundaryTypeService;
	private EgovInfrastrUtil egovInfrastrUtil;

	private static Logger logger = LoggerFactory.getLogger(UserDelegate.class);


	*//**
	 * This method creates a User in the System.
	 * @param User object,int deptid,int roleid,List jurisdictionlist,String hierarachyTypeName
	 * @exception DuplicateElementException
	 *//*
	public void createUser(final User user, final int deptid, final int roleid, final List jurlist, final String hierarachyTypeName) throws DuplicateElementException {

	}

	*//**
	 * This method updates a User in the System.
	 * @param User object,int deptid,int roleid,List jurisdictionlist,String hierarachyTypeName
	 *//*

	public void updateUser(final User user, final int deptid, final int roleid, final List jurlist, final String hierarachyTypeName) {
		Role role = null;
		Department dept = null;
		HeirarchyType ht = null;
		try {
			// JurisdictionManager jurisdictionManager = jmHome.create();
			role = this.roleService.getRole(roleid);
			final Role oldRole = user.getRoles().iterator().next();
			user.removeRole(oldRole);
			user.addRole(role);
			// sets thedepartment of the user
			dept = this.departmentService.getDepartment(deptid);
			//user.setDepartment(dept);
			// gets the HeirarchyType object depending on the hierarachyTypeName passed from the client
			final Set HeirarchyTypesSet = this.heirarchyTypeService.getAllHeirarchyTypes();
			for (final Iterator hierTypeItr = HeirarchyTypesSet.iterator(); hierTypeItr.hasNext();) {
				ht = (HeirarchyType) hierTypeItr.next();
				if (ht.getName().equals(hierarachyTypeName)) {
					break;
				}
			}

			final Set BndryTypeset = new HashSet();
			for (final Object element : user.getAllJurisdictions()) {

				final Jurisdiction ju = (Jurisdiction) element;
				final BoundaryType btyp = ju.getJurisdictionLevel();
				BndryTypeset.add(btyp.getName());
			}

			if (jurlist != null) {
				logger.info("jurlist" + jurlist);
				for (final Iterator listItr = jurlist.iterator(); listItr.hasNext();) {
					// jur = new Jurisdiction();
					final Map jurmap = (Map) listItr.next();
					String bndryTypeName = "";

					if (!jurmap.isEmpty()) {
						for (final Iterator mapItr = jurmap.keySet().iterator(); mapItr.hasNext();) {
							bndryTypeName = (String) mapItr.next();

							final List bndryIdsList = (List) jurmap.get(bndryTypeName);
							// if existing jurisdiction is modified
							if (BndryTypeset.contains(bndryTypeName)) {
								for (final Object element : user.getAllJurisdictions()) {
									final Jurisdiction ju = (Jurisdiction) element;
									final BoundaryType btyp = ju.getJurisdictionLevel();
									if (btyp.getName().equalsIgnoreCase(bndryTypeName)) {
										final Set bndValues = ju.getJurisdictionValues();
										bndValues.clear();
										for (final Iterator i = bndryIdsList.iterator(); i.hasNext();) {
											final Boundary bnd = this.boundaryService.getBoundary(Integer.parseInt((String) i.next()));
											// Boundary bnd =(bm.getBoundary(Integer.parseInt(jurvalues[i])));
											bndValues.add(bnd);
										}
										user.addJurisdiction(ju);
									}
								}
							} else {
								// if Jurisdiction Level itself is changed
								for (final Object element : user.getAllJurisdictions()) {

									final Jurisdiction jurObj = (Jurisdiction) element;
									// if new Jurisdiction is given
									// jur = new Jurisdiction();
									final BoundaryType bt = this.boundaryTypeService.getBoundaryType(bndryTypeName, ht);
									jurObj.setJurisdictionLevel(bt);
									final Set bndValues = jurObj.getJurisdictionValues();
									bndValues.clear();
									for (final Iterator bndryIdsListItr = bndryIdsList.iterator(); bndryIdsListItr.hasNext();) {
										final int bndryId = Integer.parseInt((String) bndryIdsListItr.next());
										final Boundary bndry = this.boundaryService.getBoundary(bndryId);
										bndValues.add(bndry);
										// jurObj.addJurisdictionValue(bndry);
									}
									user.addJurisdiction(jurObj);

								}
							}
						}
					}

				}

			}
			// ***************************testing jursidictions to be updated for the user
			final Set jurs = user.getAllJurisdictions();
			for (final Iterator iter = jurs.iterator(); iter.hasNext();) {
				final Jurisdiction element = (Jurisdiction) iter.next();
				element.getJurisdictionLevel();

				for (final Iterator i = element.getJurisdictionValues().iterator(); i.hasNext();) {
					i.next();
				}
			}
			// **************************************

			logger.info("userid----" + user.getId());
			this.userService.updateUser(user);
			egovInfrastrUtil.resetCache();
			// EgovInfrastrUtil.RESET = true;
		} catch (final Exception exp) {
			logger.info("Exception Encountered!!!" + exp.getMessage());

			exp.printStackTrace();
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Internal Server Error in Updating User:::", exp);
		}
	}

	*//**
	 * This method deletes a User in the System.
	 * @param Integer userid
	 *//*
	public void removeUser(final Long userid) {
		try {
			final User user = this.userService.getUserById(userid);
			//this.userService.removeUser(user);
			egovInfrastrUtil.resetCache();
			// EgovInfrastrUtil.RESET = true;
		} catch (final Exception exp) {
			logger.info("Exception Encountered!!!" + exp.getMessage());

			exp.printStackTrace();
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Internal Server Error deleting user", exp);
		}
	}

	*//**
	 * This method gets a list of top boundries existing in the system
	 * @return List topBndryList
	 *//*
	public List getTopBondaries() {
		List topBndryList = new ArrayList();
		HierarchyType ht = null;
		try {
			ht = this.heirarchyTypeService.getHeirarchyTypeByID(1);
			topBndryList = this.boundaryService.getTopBoundaries(ht);
		} catch (final Exception e) {
			logger.info("Exception Encountered!!!" + e.getMessage());
			throw new EGOVRuntimeException("Internal Server Error in getting list of top Boundries", e);
		}
		return topBndryList;
	}

	*//**
	 * This method gets an user object depending upon the userid
	 * @param userid
	 * @return User object
	 *//*
	public User getUser(final Long userid) {
		User user = null;
		try {
			user = this.userService.getUserById(userid);
		} catch (final Exception e) {
			logger.info("Exception Encountered!!!" + e.getMessage());
			throw new EGOVRuntimeException("Internal Server Error in getting User", e);
		}
		return user;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setHeirarchyTypeService(HeirarchyTypeService heirarchyTypeService) {
		this.heirarchyTypeService = heirarchyTypeService;
	}

	public void setBoundaryService(BoundaryServiceOld boundaryService) {
		this.boundaryService = boundaryService;
	}

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	public void setBoundaryTypeService(BoundaryTypeService boundaryTypeService) {
		this.boundaryTypeService = boundaryTypeService;
	}

	public void setEgovInfrastrUtil(EgovInfrastrUtil egovInfrastrUtil) {
		this.egovInfrastrUtil = egovInfrastrUtil;
	}*/
}
