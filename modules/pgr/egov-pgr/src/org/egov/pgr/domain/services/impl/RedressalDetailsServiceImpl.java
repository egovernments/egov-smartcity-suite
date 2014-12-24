/*
 * @(#)RedressalDetailsServiceImpl.java 3.0, 23 Jul, 2013 3:29:42 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pgr.domain.services.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.config.dao.AppConfigValuesDAO;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.rjbac.role.Role;
import org.egov.lib.rjbac.role.ejb.api.RoleService;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.ejb.api.UserService;
import org.egov.pgr.constant.PGRConstants;
import org.egov.pgr.domain.entities.ComplaintDetails;
import org.egov.pgr.domain.entities.ComplaintTypes;
import org.egov.pgr.domain.entities.RedressalDetails;
import org.egov.pgr.domain.services.ComplaintStatusService;
import org.egov.pgr.domain.services.RedressalDetailsService;
import org.egov.pgr.services.persistence.EntityServiceImpl;
import org.egov.pims.commons.Position;
import org.egov.pims.commons.service.EisCommonsService;
import org.egov.pims.service.EisUtilService;
import org.hibernate.Query;

public class RedressalDetailsServiceImpl extends EntityServiceImpl<RedressalDetails, Long> implements RedressalDetailsService {
	private static final Logger LOGGER = Logger.getLogger(RedressalDetailsServiceImpl.class);
	private ComplaintStatusService complaintStatusService;
	private UserService userService;
	private RoleService roleService;
	private AppConfigValuesDAO appConfigValuesDao;
	protected EisCommonsService eisCommonsService;
	private EisUtilService eisService;


	public RedressalDetailsServiceImpl() {
		super(RedressalDetails.class);
	}

	@Override
	public ComplaintDetails createNewRedressal(final ComplaintDetails complaint) {
		complaint.getRedressal().setResponsedate(DateUtils.add(new Date(), Calendar.DAY_OF_MONTH, 1));
		complaint.getRedressal().setComplaintStatus(this.complaintStatusService.findByNamedQuery("getStatusByName", PGRConstants.COMPLAINT_STATUS_REGISTERED));
		complaint.getRedressal().setPosition(getRedressalOfficerPos(complaint.getComplaintType(), complaint.getBoundary(), complaint.getTopLevelbndry()));

		User redressalOfficer = this.eisService.getUserForPosition(complaint.getRedressal().getPosition().getId(),new Date());
		// if there is no user mapped to this position return GO user
		redressalOfficer = ((redressalOfficer == null) ? getGOUser(complaint.getTopLevelbndry()) : redressalOfficer);
		complaint.getRedressal().setRedressalOfficer(redressalOfficer);
		complaint.getRedressal().setActiontype(1);
		complaint.getRedressal().setComplaintDetails(complaint);
		return complaint;
	}

	@Override
	public Position getRedressalOfficerPos(final ComplaintTypes complaintType, final Boundary boundary, final Boundary topLevelbndry) {

		if (null != complaintType && null != boundary) {
			final Position redressalOfficerPos = getROBytypeAndBndry(complaintType, boundary);
			return null != redressalOfficerPos ? redressalOfficerPos : getROByDefaultDepartment(topLevelbndry);

		} else if (null != topLevelbndry) {
			return getROByDefaultDepartment(topLevelbndry);

		} else {

			return null;
		}
	}

	/**
	 * @param topLevelbndry
	 * @return
	 */
	private User getGOUser(final Boundary topLevelbndry) {

		final AppConfigValues appConfigValue = this.appConfigValuesDao.getConfigValuesByModuleAndKey(PGRConstants.MODULE_NAME, "GRIEVANCE_OFFICER_ROLE_NAME").get(0);
		final Role role = this.roleService.getRoleByRoleName(appConfigValue.getValue());
		if (null == role) {

			throw new ValidationException(Arrays.asList(new ValidationError("GRIEVANCE_OFFICER_ROLE", "GRIEVANCE_OFFICER Role is not defined in the System")));
		}
		final List<Role> roleList = new ArrayList<Role>();
		roleList.add(role);
		final List<User> users = this.userService.getAllUsersByRole(roleList, topLevelbndry.getId(), new Date());
		if (null == users || users.size() == 0) {

			LOGGER.error("There is no User mapped to GRIEVANCE_OFFICER Role in the System ");
			throw new ValidationException(Arrays.asList(new ValidationError("GRIEVANCE_OFFICER_USER", "There is no User mapped to GRIEVANCE_OFFICER Role in the System")));
		} else {
			return users.get(0);
		}

	}

	@SuppressWarnings("unchecked")
	private Position getTopLevelBoundryDefault(final Boundary topLevelbndry) {

		User redressalOfficer = null;

		final StringBuffer bndryDefaultQry = new StringBuffer();
		bndryDefaultQry.append("select roleid from EG_DEFAULT where TOPLEVELBNDRYID =").append(topLevelbndry.getId()).append(" and DEFAULTID in (select DEFAULTID from EG_MASTER_DEFAULT where DEFAULTNAME='")
				.append(PGRConstants.EG_DEFAULT_TOPLEVELBNDRY_DEFAULT).append("')");

		final Query query = HibernateUtil.getCurrentSession().createSQLQuery(bndryDefaultQry.toString());
		final List<Integer> roles = query.list();
		if (null != roles && roles.size() > 0) {
			final Role role = this.roleService.getRole(roles.get(0));
			final List<Role> roleList = new ArrayList<Role>();
			roleList.add(role);
			final List<User> users = this.userService.getAllUsersByRole(roleList, topLevelbndry.getId(), new Date());
			redressalOfficer = null != users && users.size() > 0 ? users.get(0) : null;
		}

		if (null != redressalOfficer) {
			return this.eisService.getPrimaryPositionForUser(redressalOfficer.getId(), new Date());
			//return this.eisCommonsService.getPositionByUserId(redressalOfficer.getId());
		} else {
			//return this.eisCommonsService.getPositionByUserId(getGOUser(topLevelbndry).getId());
			return this.eisService.getPrimaryPositionForUser(getGOUser(topLevelbndry).getId(), new Date());
		}

	}

	@SuppressWarnings("unchecked")
	private Position getROByDefaultDepartment(final Boundary topLevelbndry) {

		User redressalOfficer = null;

		final StringBuffer roleQuery = new StringBuffer();
		roleQuery.append("select roleid from EG_DEFAULT where TOPLEVELBNDRYID =").append(topLevelbndry.getId()).append(" and DEFAULTID in (select DEFAULTID from EG_MASTER_DEFAULT where DEFAULTNAME ='").append(PGRConstants.EG_MASTER_DEPARTMENT_AS_DEFAULT)
				.append("')");
		final Query query = HibernateUtil.getCurrentSession().createSQLQuery(roleQuery.toString());

		final List<Integer> roles = query.list();
		if (null != roles && roles.size() > 0) {
			final Role role = this.roleService.getRole(roles.get(0));
			final List<Role> roleList = new ArrayList<Role>();
			roleList.add(role);
			final List<User> users = this.userService.getAllUsersByRole(roleList, topLevelbndry.getId(), new Date());
			redressalOfficer = null != users && users.size() > 0 ? users.get(0) : null;
		}
		//return null != redressalOfficer ? this.eisCommonsService.getPositionByUserId(redressalOfficer.getId()) : getTopLevelBoundryDefault(topLevelbndry);
		return null != redressalOfficer ? this.eisService.getPrimaryPositionForUser(redressalOfficer.getId(), new Date()) : getTopLevelBoundryDefault(topLevelbndry);
	}

	@SuppressWarnings("unchecked")
	private Position getROBytypeAndBndry(final ComplaintTypes complaintType, final Boundary boundary) {

		if (null == boundary) {
			return null;
		} else {

			final StringBuffer query1 = new StringBuffer();
			query1.append("select positionid from EG_ROUTER where PGRFLAG = 1 and COMPLAINTTYPEID =").append(complaintType.getId()).append(" and BNDRYID =").append(boundary.getId());
			final Query query = HibernateUtil.getCurrentSession().createSQLQuery(query1.toString());
			final List<BigDecimal> positionList = query.list();
			if (null != positionList && positionList.size() > 0) {
				return this.eisCommonsService.getPositionById(Integer.valueOf(positionList.get(0).toString()));

			} else {
				return getROBytypeAndBndry(complaintType, boundary.getParent());
			}
		}

	}

	/**
	 * @param complaintStatusService the complaintStatusService to set
	 */
	public void setComplaintStatusService(final ComplaintStatusService complaintStatusService) {
		this.complaintStatusService = complaintStatusService;
	}

	/**
	 * @param userService the userService to set
	 */
	public void setUserService(final UserService userService) {
		this.userService = userService;
	}

	/**
	 * @param roleService the roleService to set
	 */
	public void setRoleService(final RoleService roleService) {
		this.roleService = roleService;
	}

	/**
	 * @param genericHibDao the genericHibDao to set
	 */
	public void setAppConfigValuesDao(final AppConfigValuesDAO appConfigValuesDao) {
		this.appConfigValuesDao = appConfigValuesDao;
	}

	/**
	 * @param eisCommonsService the eisCommonsService to set
	 */
	public void setEisCommonsService(final EisCommonsService eisCommonsService) {
		this.eisCommonsService = eisCommonsService;
	}
	public void setEisService(final EisUtilService eisService) {
		this.eisService = eisService;
	}
	 
}
