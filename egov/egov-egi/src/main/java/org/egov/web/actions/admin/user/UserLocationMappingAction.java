/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.web.actions.admin.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.repository.UserRepository;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.lib.security.terminal.dao.UserCounterDAO;
import org.egov.lib.security.terminal.model.Location;
import org.egov.lib.security.terminal.model.UserCounterMap;

import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("egov")
public class UserLocationMappingAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private PersistenceService persistenceService;
	private UserCounterDAO userCounterDao;
	private UserService userService;

	public void setUserService(final UserService userService) {
		this.userService = userService;
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
				jsonString.append("{").append("'id':").append(userCounterMap.getId()).append(",'userName':'").append(userCounterMap.getUserId().getUsername()).append("',");
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
		final List<User> users = Collections.emptyList();// this.userRepository.getAllUserByUserNameLike(userName);
		if (!users.isEmpty()) {
			final StringBuilder jsonString = new StringBuilder();
			jsonString.append("[");
			for (final User user : users) {
				jsonString.append("{").append("'id':'").append(user.getId()).append("','userName':'").append(user.getUsername()).append("'},");
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
		final boolean overLaps = this.userCounterDao.checkUserCounter(this.userService.getUserByUsername(userName).getId(), fromDate, toDate);
		ServletActionContext.getResponse().getWriter().write("{'success':" + overLaps + "}");
		return null;
	}

	public String addMapping() throws IOException {
		try {

			final int locationId = Integer.parseInt(ServletActionContext.getRequest().getParameter("location"));
			final String userName = ServletActionContext.getRequest().getParameter("userName");
			final Date fromDate = DateUtils.getDate(ServletActionContext.getRequest().getParameter("fromDate"), DateUtils.DFT_DATE_FORMAT);
			final Date toDate = DateUtils.getDate(ServletActionContext.getRequest().getParameter("toDate"), DateUtils.DFT_DATE_FORMAT);
			final User user = (User) this.userService.getUserByUsername(userName);
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
			userCounterMap.setUserId((User) this.userService.getUserByUsername(userName));
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
