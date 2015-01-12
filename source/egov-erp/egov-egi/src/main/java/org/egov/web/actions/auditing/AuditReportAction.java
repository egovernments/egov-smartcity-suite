/*
 * @(#)AuditReportAction.java 3.0, 21 Jun, 2013 5:58:10 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.web.actions.auditing;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infstr.auditing.model.AuditEntity;
import org.egov.infstr.auditing.model.AuditEvent;
import org.egov.infstr.auditing.model.AuditModule;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.lib.rjbac.role.Role;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.UserImpl;
import org.egov.lib.rjbac.user.dao.UserDAO;
import org.egov.web.actions.SearchFormAction;
import org.egov.web.annotation.ValidationErrorPage;

@ParentPackage("egov")
public class AuditReportAction extends SearchFormAction {

	private static final String ENTITY = "entity";
	private static final Logger LOGGER = Logger.getLogger(AuditReportAction.class);
	private static final String SEARCH_RESULT = "searchResult";
	private static final String SEARCH_FORM = "searchForm";
	private Map<AuditModule, String> moduleMap = new HashMap<AuditModule, String>();
	private AuditModule moduleName;
	private AuditModule defaultModule = AuditModule.INFRA;
	private String entityName;
	private String userName;
	private Date fromDate;
	private Date toDate;
	private String target;
	private List<AuditEvent> eventList;
	private String searchValue;
	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	private static final String USER_SEARCH_RESULTS = "user";
	private List<UserImpl> userList = new ArrayList<UserImpl>();
	private String query;
	private List<AuditEntity> entityList = new ArrayList<AuditEntity>();
	public static final String SESSIONLOGINID = "com.egov.user.LoginUserId";
	private String roleName;

	@Override
	public Object getModel() {
		return null;
	}
	
	@Override
	public void prepare() {
		Integer userId = (Integer) session().get(SESSIONLOGINID);
		if (userId != null) {
			setRoleName(getRolesForUserId(userId));
		}
		 
		if (moduleName != null && moduleName.equals("-1")) {
			dropdownData.put("entityList", Collections.EMPTY_LIST);
		} else {
			dropdownData.put("entityList", entityList);
		}
	}
	
	@Override
	public void validate() {
		if (fromDate != null && !fromDate.equals("") && !fromDate.equals("DD/MM/YYYY")) {
			if (fromDate.after(new Date())) {
				addActionError("From Date should be less than or equal to current date");
			}
			if (toDate == null || toDate.equals("") || toDate.equals("DD/MM/YYYY")) {
				addActionError("Please enter To Date");
			}
		} 
		
		if (toDate != null && !toDate.equals("") && !toDate.equals("DD/MM/YYYY")) {
			if (toDate.after(new Date())) {
				addActionError("To Date should be less than or equal to current date");
			}
			if (fromDate == null || fromDate.equals("") || fromDate.equals("DD/MM/YYYY")) {
				addActionError("Please enter From Date");
			}
		} 
		
		if ((moduleName == null || moduleName.equals("") || moduleName.equals("-1"))
				&& (userName == null || userName.equals(""))
				&& (fromDate == null || fromDate.equals("") || fromDate.equals("DD/MM/YYYY"))
				&& (toDate == null || toDate.equals("") || toDate.equals("DD/MM/YYYY"))) {
			addActionError("Please select anyone of the Search Criteria");
		}
		
		if (hasActionErrors()) {
			prepareModuleMap();
		}
	}
	
	@SkipValidation
	public String searchForm() {
		prepareModuleMap();
		if (hasActionErrors()) {
			return ERROR;
		}
		return SEARCH_FORM;
	}

	private void prepareModuleMap() {
		if (roleName.contains("SUPERUSER")) {
			if (moduleName != null && !moduleName.equals("")) {
				setDefaultModule(moduleName);
			}
			for(AuditModule auditModule : AuditModule.values()) {
				moduleMap.put(auditModule, auditModule.moduleName);
			}
		} else {
			if (moduleName == null || moduleName.equals("")) {
				addActionError("Module Name is Mandatory");
			} else {
				setDefaultModule(moduleName);
				moduleMap.put(moduleName, moduleName.moduleName);
			}
		}
	}

	@Override
	@ValidationErrorPage(value = SEARCH_FORM)
	public String search() {
		LOGGER.debug("Entered into search method");
		LOGGER.debug("Module Name : " + moduleName + ", " + "Entity Name : " + entityName + ", " + "User Name : "
				+ userName + ", " + "From Date : " + fromDate + ", " + "To Date : " + toDate);
		target = SEARCH_RESULT;
		prepareModuleMap();
		if (hasActionErrors()) {
			return ERROR;
		}
		super.search();
		return SEARCH_FORM;
	}
	
	@SkipValidation
	public String populateEntities() throws IOException {
		if (moduleName != null && !moduleName.equals("-1")) {
			for (String entityName : moduleName.getAuditEntities()) {
				entityList.add(AuditEntity.getAuditEntityByName(entityName));
			}
		}
		return ENTITY;
	}
	
	@Override
	public SearchQuery prepareQuery(String sortField, String sortDir) {
		LOGGER.debug("Entered into prepareQuery method");
		LOGGER.debug("Sort Field : " + sortField + ", " + "Sort Dir : " + sortDir);
		StringBuilder searchQueryString = new StringBuilder("select event");
		StringBuilder countQueryString = new StringBuilder("select count(event)");
		StringBuilder fromString = new StringBuilder(" from org.egov.infstr.auditing.model.AuditEvent event");

		final String orderByString = " order by event.eventDate desc";

		Map<String, Object> map = getCriteriaString();
		String searchQuery = searchQueryString.append(fromString).append(map.get("criteriaString")).append(
				orderByString).toString();
		LOGGER.debug("Search Query : " + searchQuery);
		String countQuery = countQueryString.append(fromString).append(map.get("criteriaString")).append(orderByString)
				.toString();
		LOGGER.debug("Count Query : " + countQuery);
		LOGGER.debug("Exit from prepareQuery method");
		return new SearchQueryHQL(searchQuery, countQuery, (ArrayList<Object>) map.get("params"));
	}
	
	private Map<String, Object> getCriteriaString() {
		LOGGER.debug("Entered into getCriteriaString method");
		LOGGER.debug("Module Name : " + moduleName + ", " + "Entity Name : " + entityName + ", " + "User Name : "
				+ userName + ", " + "From Date : " + fromDate + ", " + "To Date : " + toDate);
		StringBuffer srchString = new StringBuffer();
		Map<String, Object> map = new HashMap<String, Object>();
		ArrayList<Object> params = new ArrayList<Object>();
		StringBuilder criteriaString;
		criteriaString = new StringBuilder(" where event.id is not null");

		if (moduleName != null && !moduleName.equals("") && !moduleName.equals("-1")) {
			criteriaString.append(" and event.auditModule = ?");
			srchString.append(" Module Name: ").append(moduleName.moduleName).append(", ");
			params.add(moduleName);
		}
		if (entityName != null && !entityName.equals("") && !entityName.equals("-1")) {
			criteriaString.append(" and event.auditEntity = ?");
			srchString.append(" Entity Name: ").append(entityName).append(", ");
			params.add(AuditEntity.getAuditEntityByName(entityName));
		}
		if (userName != null && !userName.equals("")) {
			criteriaString.append(" and event.userName = ?");
			srchString.append(" User Name: ").append(userName).append(", ");
			params.add(userName);
		}
		if (fromDate != null && !fromDate.equals("DD/MM/YYYY")) {
			criteriaString.append(" and event.eventDate >= ?");
			srchString.append(" FromDate: ").append(dateFormat.format(fromDate)).append(", ");
			params.add(fromDate);
		}
		if (toDate != null && !toDate.equals("DD/MM/YYYY")) {
			srchString.append(" ToDate: ").append(dateFormat.format(toDate)).append(" ");
			Calendar nextDate = Calendar.getInstance();
			nextDate.setTime(toDate);
			nextDate.add(Calendar.DATE, 1);
			criteriaString.append(" and event.eventDate <= ?");
			params.add(nextDate.getTime());
		}
		setSearchValue(srchString.toString());
		map.put("criteriaString", criteriaString);
		map.put("params", params);
		LOGGER.debug("Criteria String : " + criteriaString);
		LOGGER.debug("Exit from getCriteriaString method");
		return map;
	}
	
	@SkipValidation
	public List<UserImpl> getAllUsers() {
		if (StringUtils.isNotBlank(query)) {
			userList.addAll(persistenceService.findAllBy(
					"from UserImpl where upper(userName) like ? || '%' and isActive=1 ", query.toUpperCase()));

		}
		return userList;
	}
	
	private String getRolesForUserId(Integer userId) {
		LOGGER.debug("Entered into getRolesForUserId method");
		LOGGER.debug("User id : " + userId);
		UserDAO userDao = new UserDAO();
		String roleName;
		List<String> roleNameList = new ArrayList<String>();
		User user = userDao.getUserByID(userId);
		for (Role role : user.getRoles()) {
			roleName = role.getRoleName() != null ? role.getRoleName() : "";
			roleNameList.add(roleName);
		}
		LOGGER.debug("Exit from method getRolesForUserId with return value : " + roleNameList.toString().toUpperCase());
		return roleNameList.toString().toUpperCase();
	}
	
	@SkipValidation
	public String ajaxUserNames() {		
		return USER_SEARCH_RESULTS; 
	}
	
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	
	public AuditModule getModuleName() {
		return moduleName;
	}

	public void setModuleName(AuditModule moduleName) {
		this.moduleName = moduleName;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public Map<AuditModule, String> getModuleMap() {
		return moduleMap;
	}

	public void setModuleMap(Map<AuditModule, String> moduleMap) {
		this.moduleMap = moduleMap;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public List<AuditEvent> getEventList() {
		return eventList;
	}

	public void setEventList(List<AuditEvent> eventList) {
		this.eventList = eventList;
	}

	public String getSearchValue() {
		return searchValue;
	}

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}

	public List<AuditEntity> getEntityList() {
		return entityList;
	}

	public void setEntityList(List<AuditEntity> entityList) {
		this.entityList = entityList;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public AuditModule getDefaultModule() {
		return defaultModule;
	}

	public void setDefaultModule(AuditModule defaultModule) {
		this.defaultModule = defaultModule;
	}

}
