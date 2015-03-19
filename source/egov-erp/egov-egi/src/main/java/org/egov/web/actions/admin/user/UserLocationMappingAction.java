/*
 * @(#)UserLocationMappingAction.java 3.0, 14 Jun, 2013 1:44:52 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.web.actions.admin.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.entity.UserImpl;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.lib.rjbac.user.dao.UserDAO;
import org.egov.lib.security.terminal.dao.UserCounterDAO;
import org.egov.lib.security.terminal.model.Location;
import org.egov.lib.security.terminal.model.UserCounterMap;

import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("egov")
public class UserLocationMappingAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private PersistenceService persistenceService;
	private UserCounterDAO userCounterDao;
	private UserDAO userDao;

	public void setUserDao(final UserDAO userDao) {
		this.userDao = userDao;
	}

	public void setUserCounterDao(final UserCounterDAO userCounterDao) {
		this.userCounterDao = userCounterDao;
	}

	public void setPersistenceService(final PersistenceService service) {
		this.persistenceService = service;
	}

	private String locationJSON;

	@Override
	public String execute() throws Exception {
		final List<Location> locations = new ArrayList<Location>();
		for (final Location location : (ArrayList<Location>) EgovMasterDataCaching.getInstance().get("egi-locationparent")) {
			if (location.getIsLocation() == 1) {
				locations.add(location);
			}
		}
		this.locationJSON = getJSONString(locations);
		return SUCCESS;
	}

	private String getJSONString(final List<Location> locations) {
		final StringBuilder jsonString = new StringBuilder();
		jsonString.append("[");
		for (final Location location : locations) {
			jsonString.append("{").append("'locationId':'").append(location.getId()).append("','locationName':'").append(location.getName()).append("'},");
		}
		jsonString.deleteCharAt(jsonString.length() - 1);
		jsonString.append("]");
		return jsonString.toString();
	}

	public String getLocation() {
		return this.locationJSON;
	}

	public String show() throws Exception {
		final String showAll = ServletActionContext.getRequest().getParameter("showAll");
		final int locationId = Integer.parseInt(ServletActionContext.getRequest().getParameter("location"));
		List<UserCounterMap> userCounterMapList = null;
		if (showAll == null) {
			userCounterMapList = this.userCounterDao.getLocationBasedUserCounterMapForCurrentDate(locationId);
		} else {
			userCounterMapList = this.userCounterDao.getUserCounterMapForLocationId(locationId);
		}

		if (userCounterMapList == null || userCounterMapList.isEmpty()) {
			ServletActionContext.getResponse().getWriter().write("{'success':false, 'data':[]}");
		} else {
			final StringBuilder jsonString = new StringBuilder();
			jsonString.append("[");
			for (final UserCounterMap userCounterMap : userCounterMapList) {
				jsonString.append("{").append("'id':").append(userCounterMap.getId()).append(",'userName':'").append(userCounterMap.getUserId().getUserName()).append("',");
				jsonString.append("'fromDate':'").append(DateUtils.getDefaultFormattedDate(userCounterMap.getFromDate())).append("','toDate':'").append(DateUtils.getDefaultFormattedDate(userCounterMap.getToDate())).append("'},");
			}
			jsonString.deleteCharAt(jsonString.length() - 1);
			jsonString.append("]");
			ServletActionContext.getResponse().getWriter().write("{'success':true, 'data':" + jsonString + "}");
		}

		return null;
	}

	public String showUsers() throws Exception {
		final String userName = ServletActionContext.getRequest().getParameter("query");
		final List<User> users = this.userDao.getAllUserByUserNameLike(userName);
		if (!users.isEmpty()) {
			final StringBuilder jsonString = new StringBuilder();
			jsonString.append("[");
			for (final User user : users) {
				jsonString.append("{").append("'id':'").append(user.getId()).append("','userName':'").append(user.getUserName()).append("'},");
			}
			jsonString.deleteCharAt(jsonString.length() - 1);
			jsonString.append("]");
			ServletActionContext.getResponse().getWriter().write("{'success':true, 'data':" + jsonString + "}");
		} else {
			ServletActionContext.getResponse().getWriter().write("{'success':false}");
		}
		return null;
	}

	public String checkMappingOverlaps() throws IOException {
		final String userName = ServletActionContext.getRequest().getParameter("userName");
		final Date fromDate = DateUtils.getDate(ServletActionContext.getRequest().getParameter("fromDate"), "MM/dd/yyyy");
		final Date toDate = DateUtils.getDate(ServletActionContext.getRequest().getParameter("toDate"), "MM/dd/yyyy");
		final boolean overLaps = this.userCounterDao.checkUserCounter(this.userDao.getUserByUserName(userName).getId(), fromDate, toDate);
		ServletActionContext.getResponse().getWriter().write("{'success':" + overLaps + "}");
		return null;
	}

	public String addMapping() throws IOException {
		try {

			final int locationId = Integer.parseInt(ServletActionContext.getRequest().getParameter("location"));
			final String userName = ServletActionContext.getRequest().getParameter("userName");
			final Date fromDate = DateUtils.getDate(ServletActionContext.getRequest().getParameter("fromDate"), DateUtils.DFT_DATE_FORMAT);
			final Date toDate = DateUtils.getDate(ServletActionContext.getRequest().getParameter("toDate"), DateUtils.DFT_DATE_FORMAT);
			final UserImpl user = (UserImpl) this.userDao.getUserByUserName(userName);
			if (this.userCounterDao.checkUserCounter(user.getId(), fromDate, toDate)) {
				ServletActionContext.getResponse().getWriter().write("{'success':false}");
			} else {
				final UserCounterMap userCounterMap = new UserCounterMap();
				this.persistenceService.setType(Location.class);
				userCounterMap.setCounterId((Location) this.persistenceService.findById(locationId, false));
				userCounterMap.setUserId(user);
				userCounterMap.setFromDate(fromDate);
				userCounterMap.setToDate(toDate);
				userCounterMap.setModifiedDate(new Date());
				userCounterMap.setModifiedBy(Integer.parseInt(EGOVThreadLocals.getUserId()));
				this.userCounterDao.create(userCounterMap);
				ServletActionContext.getResponse().getWriter().write("{'success':true,'id':" + userCounterMap.getId() + "}");
			}

		} catch (final RuntimeException e) {
			ServletActionContext.getResponse().getWriter().write("{'success':false}");
		}
		return null;
	}

	public String updateMapping() throws IOException {
		try {
			final int locationId = Integer.parseInt(ServletActionContext.getRequest().getParameter("location"));
			final int counterId = Integer.parseInt(ServletActionContext.getRequest().getParameter("id"));
			final String userName = ServletActionContext.getRequest().getParameter("userName");
			final Date fromDate = DateUtils.getDate(ServletActionContext.getRequest().getParameter("fromDate"), DateUtils.DFT_DATE_FORMAT);
			final Date toDate = DateUtils.getDate(ServletActionContext.getRequest().getParameter("toDate"), DateUtils.DFT_DATE_FORMAT);
			final UserCounterMap userCounterMap = this.userCounterDao.findById(counterId, false);
			this.persistenceService.setType(Location.class);
			userCounterMap.setCounterId((Location) this.persistenceService.findById(locationId, false));
			userCounterMap.setUserId((UserImpl) this.userDao.getUserByUserName(userName));
			userCounterMap.setFromDate(fromDate);
			userCounterMap.setToDate(toDate);
			userCounterMap.setModifiedDate(new Date());
			userCounterMap.setModifiedBy(Integer.parseInt(EGOVThreadLocals.getUserId()));
			this.userCounterDao.update(userCounterMap);
			ServletActionContext.getResponse().getWriter().write("{'success':true}");
		} catch (final RuntimeException e) {
			ServletActionContext.getResponse().getWriter().write("{'success':false}");
		}
		return null;
	}

	public String deleteMapping() throws IOException {
		try {
			final int counterId = Integer.parseInt(ServletActionContext.getRequest().getParameter("id"));
			this.userCounterDao.delete(this.userCounterDao.findById(counterId, false));
			ServletActionContext.getResponse().getWriter().write("{'success':true}");
		} catch (final RuntimeException e) {
			ServletActionContext.getResponse().getWriter().write("{'success':false}");
		}
		return null;
	}
}
