/*
 * @(#)NotificationGroupAction.java 3.0, 16 Jul, 2013 11:35:49 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.web.actions.dms;

import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.egov.infstr.utils.DateUtils.getFormattedDate;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.exceptions.NoSuchObjectException;
import org.egov.exceptions.TooManyValuesException;
import org.egov.infstr.auditing.model.AuditEntity;
import org.egov.infstr.auditing.model.AuditEvent;
import org.egov.infstr.auditing.model.AuditModule;
import org.egov.infstr.auditing.service.AuditEventService;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.utils.StringUtils;
import org.egov.infstr.workflow.NotificationGroup;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.admbndry.BoundaryType;
import org.egov.lib.admbndry.BoundaryTypeDAO;
import org.egov.lib.admbndry.HeirarchyType;
import org.egov.lib.admbndry.HeirarchyTypeDAO;
import org.egov.lib.rjbac.user.UserImpl;
import org.egov.pims.commons.Position;
import org.egov.pims.dao.PersonalInformationDAO;
import org.egov.pims.model.EmployeeView;
import org.egov.web.actions.BaseFormAction;
import org.hibernate.Query;

@ParentPackage("egov")
public class NotificationGroupAction extends BaseFormAction {
	
	private static final long serialVersionUID = 1L;
	private transient NotificationGroup notificationGroup = new NotificationGroup();
	private transient final List<BoundaryImpl> firstLevelBndries = new ArrayList<BoundaryImpl>();
	private transient BoundaryDAO boundaryDAO;
	private transient BoundaryTypeDAO boundaryTypeDAO;
	private transient HeirarchyTypeDAO heirarchyTypeDAO;
	private transient final Map<String, List<Integer>> params = new HashMap<String, List<Integer>>();
	private transient PersonalInformationDAO personalInfoDao;
	private transient PersistenceService<NotificationGroup, Long> notificationGroupPersistenceService;
	private transient AuditEventService auditEventService;
	
	public NotificationGroupAction() {
		super();
		this.addRelatedEntity("members", Position.class);
	}
	
	@Override
	public Object getModel() {
		return this.notificationGroup;
	}
	
	@SkipValidation
	public String execute() {
		return SUCCESS;
	}
	
	@SkipValidation
	public String edit() {
		this.view();
		return SUCCESS;
	}
	
	public String save() {
		if (this.notificationGroup.getId() == null) {
			doAuditing(AuditEvent.CREATED);
		} else {
			doAuditing(AuditEvent.MODIFIED);
		}
		this.notificationGroupPersistenceService.persist(this.notificationGroup);
		return "view";
	}
	
	@SkipValidation
	public String delete() {
		if ((ServletActionContext.getRequest().getQueryString() != null) && ServletActionContext.getRequest().getQueryString().contains("id")) {
			return this.view();
		} else {
			final Query query = this.persistenceService.getSession().createSQLQuery("delete from EGDMS_NOTIFICATION_USER where GROUP_ID=:id");
			query.setLong("id", this.notificationGroup.getId());
			query.executeUpdate();
			this.notificationGroup = this.notificationGroupPersistenceService.findById(this.notificationGroup.getId(), false);
			doAuditing(AuditEvent.DELETED);
			this.notificationGroupPersistenceService.delete(notificationGroup);
			this.notificationGroup = new NotificationGroup();
			this.addActionMessage("DMS.NOTIFYGRP.DELETE.SUCCESS");
		}
		return SUCCESS;
	}
	
	@SkipValidation
	public String view() {
		if (this.notificationGroup.getId() == null) {
			this.addActionMessage("DMS.NOTIFYGRP.ID.MISSING");
		} else {
			this.notificationGroup = (NotificationGroup) this.notificationGroupPersistenceService.findById(this.notificationGroup.getId(), false);
			if (this.notificationGroup == null) {
				this.addActionMessage("DMS.NOTIFYGRP.NO.NOTIFYGRP");
				this.notificationGroup = new NotificationGroup();
				return SUCCESS;
			}
		}
		return "view";
	}
	
	public void prepare() {
		this.addDropdownData("departments", this.persistenceService.findAllBy("from org.egov.lib.rjbac.dept.DepartmentImpl order by deptName"));
		this.addDropdownData("designations", this.persistenceService.findAllBy("from org.egov.pims.commons.DesignationMaster order by designationName"));
		final Map<String, Class> relationships = this.getRelationships();
		for (final Entry<String, Class> rel : relationships.entrySet()) {
			this.setRelationship(rel.getKey(), rel.getValue());
		}
	}
	
	public void setRelationship(final String relationshipName, final Class class1) {
		final String[] ids = this.parameters.get(relationshipName);
		final Set<Position> relation = new HashSet<Position>();
		if ((ids != null) && (ids.length > 0)) {
			for (final String id : ids) {
				if ((id != null) && (id.length() > 0)) {
					relation.add((Position) this.getPersistenceService().find("from " + class1.getName() + " where id=?", Integer.parseInt(id)));
				}
			}
		}
		this.setValue(relationshipName, relation);
	}
	
	@SkipValidation
	public List<BoundaryImpl> getFirstLevelBoundries() throws NoSuchObjectException, TooManyValuesException {
		final HeirarchyType heirarchyType = this.heirarchyTypeDAO.getHierarchyTypeByName("ADMINISTRATION");
		final BoundaryType boundaryType = this.boundaryTypeDAO.getBoundaryType("City", heirarchyType);
		final List<BoundaryImpl> cities = this.boundaryDAO.getAllBoundariesByBndryTypeId(boundaryType.getId());
		for (final BoundaryImpl city : cities) {
			this.firstLevelBndries.addAll(city.getChildren());
		}
		return this.firstLevelBndries;
	}
	
	public void setFirstLevelIds(final List<Integer> firstLevelIds) {
		this.params.put("firstLevelIds", firstLevelIds);
	}
	
	@SkipValidation
	public void getSecondLevelBndries() throws IOException {
		final Query qry = this.persistenceService.getSession().createQuery("from BoundaryImpl where parent is not null and parent.id in (:firstLevelIds) and isHistory='N' order by parent.id,name");
		qry.setParameterList("firstLevelIds", this.params.get("firstLevelIds"));
		final List<BoundaryImpl> boundaries = qry.list();
		final StringBuilder secondLvlBndrys = new StringBuilder("[");
		for (final Boundary boundary : boundaries) {
			secondLvlBndrys.append("{name : '").append(boundary.getName()).append("',id:").append(boundary.getId()).append("},");
		}
		secondLvlBndrys.deleteCharAt(secondLvlBndrys.length() - 1);
		secondLvlBndrys.append("]");
		ServletActionContext.getResponse().getWriter().write(secondLvlBndrys.toString());
	}
	
	public void setSecondLevelIds(final List<Integer> secondLevelIds) {
		this.params.put("secondLevelIds", secondLevelIds);
	}
	
	public void setDepartmentIds(final List<Integer> departmentIds) {
		this.params.put("departmentIds", departmentIds);
	}
	
	public void setDesignationIds(final List<Integer> designationIds) {
		this.params.put("designationIds", designationIds);
	}
	
	@SkipValidation
	public void getPositions() throws IOException, NoSuchObjectException {
		final StringBuilder queryStr = new StringBuilder("from EmployeeView where ((toDate is null and fromDate <= SYSDATE ) OR (fromDate <= SYSDATE AND toDate > SYSDATE))");
		if (this.params.containsKey("departmentIds")) {
			queryStr.append("AND deptId.id IN (:departmentIds) ");
		}
		if (this.params.containsKey("designationIds")) {
			queryStr.append("AND desigId.designationId IN (:designationIds) ");
		}
		if (this.params.containsKey("secondLevelIds")) {
			queryStr.append("AND userMaster IN (:users) ");
		}
		queryStr.append("order by upper(employeeName) asc");
		final Query query = this.persistenceService.getSession().createQuery(queryStr.toString());
		if (this.params.containsKey("designationIds")) {
			query.setParameterList("designationIds", this.params.get("designationIds"));
		}
		if (this.params.containsKey("departmentIds")) {
			query.setParameterList("departmentIds", this.params.get("departmentIds"));
		}
		if (this.params.containsKey("secondLevelIds")) {
			final List<UserImpl> users = new ArrayList<UserImpl>();
			for (final Integer boundaryId : this.params.get("secondLevelIds")) {
				users.addAll(this.personalInfoDao.getListOfUsersByBoundaryId(boundaryId));
			}
			query.setParameterList("users", users);
		}
		
		final Set<EmployeeView> employees = new LinkedHashSet<EmployeeView>(query.list());
		if (employees.isEmpty()) {
			ServletActionContext.getResponse().getWriter().write(ERROR);
		} else {
			final StringBuilder positionsStr = new StringBuilder("[");
			for (final EmployeeView employee : employees) {
				final Position position = employee.getPosition();
				positionsStr.append("{name : '").append(employee.getEmployeeName()).append(" - ").append(position.getName()).append("',id:").append(position.getId()).append("},");
			}
			positionsStr.deleteCharAt(positionsStr.length() - 1);
			positionsStr.append("]");
			ServletActionContext.getResponse().getWriter().write(positionsStr.toString());
		}
	}
	
	@SkipValidation
	public String search() {
		return "search";
	}
	
	@SkipValidation
	public void searchNotificationGroup() throws ParseException, IOException {
		try {
			final HttpServletRequest request = ServletActionContext.getRequest();
			final StringBuilder ntfyGrp = new StringBuilder("from org.egov.infstr.workflow.NotificationGroup where groupName IS NOT NULL ");
			if (request.getParameter("groupName") != null) {
				ntfyGrp.append("and groupName =:name ");
			}
			if (request.getParameter("active") != null) {
				ntfyGrp.append("and active =:active ");
			}
			if ((request.getParameter("effDtFrom") != null) && (request.getParameter("effDtTo") == null)) {
				ntfyGrp.append("and effectiveDate >= :fromDate ");
			} else if ((request.getParameter("effDtFrom") == null) && (request.getParameter("effDtTo") != null)) {
				ntfyGrp.append("and effectiveDate <= :toDate ");
			} else if ((request.getParameter("effDtFrom") != null) && (request.getParameter("effDtTo") != null)) {
				ntfyGrp.append("and effectiveDate between :fromDate and :toDate ");
			}
			ntfyGrp.append(" order by groupName");
			final Query query = this.getPersistenceService().getSession().createQuery(ntfyGrp.toString());
			if (request.getParameter("groupName") != null) {
				query.setString("name", request.getParameter("groupName"));
			}
			if (request.getParameter("active") != null) {
				query.setString("active", request.getParameter("active"));
			}
			if ((request.getParameter("effDtFrom") != null) && (request.getParameter("effDtTo") == null)) {
				query.setDate("fromDate", DateUtils.constructDateRange(request.getParameter("effDtFrom"), request.getParameter("effDtFrom"))[0]);
			} else if ((request.getParameter("effDtFrom") == null) && (request.getParameter("effDtTo") != null)) {
				query.setDate("toDate", DateUtils.constructDateRange(request.getParameter("effDtTo"), request.getParameter("effDtTo"))[1]);
			} else if ((request.getParameter("effDtFrom") != null) && (request.getParameter("effDtTo") != null)) {
				final Date[] range = DateUtils.constructDateRange(request.getParameter("effDtFrom"), request.getParameter("effDtTo"));
				query.setDate("fromDate", range[0]);
				query.setDate("toDate", range[1]);
			}
			final List<NotificationGroup> notificationGroups = query.list();
			if ((notificationGroups != null) && !notificationGroups.isEmpty()) {
				ntfyGrp.setLength(0);
				ntfyGrp.append("[");
				for (final NotificationGroup notificationGroup : notificationGroups) {
					ntfyGrp.append("{Id:'").append(notificationGroup.getId()).append("',");
					ntfyGrp.append("Name:'").append(notificationGroup.getGroupName()).append("',");
					ntfyGrp.append("Desc:'").append(StringUtils.escapeJavaScript(StringUtils.emptyIfNull(notificationGroup.getGroupDesc()))).append("',");
					ntfyGrp.append("Effective:'").append(getFormattedDate(notificationGroup.getEffectiveDate(), "dd/MM/yyyy hh:mm a")).append("',");
					ntfyGrp.append("Active:'").append(notificationGroup.getActive() == 'Y' ? "Yes" : "No").append("',");
					ntfyGrp.append("Link:'").append("../dms/notificationGroup!##.action?id=").append(notificationGroup.getId()).append("'},");
				}
				ntfyGrp.deleteCharAt(ntfyGrp.length() - 1);
				ntfyGrp.append("]");
				ServletActionContext.getResponse().getWriter().write(ntfyGrp.toString());
			} else {
				ServletActionContext.getResponse().getWriter().write("");
			}
		} catch (final RuntimeException e) {
			ServletActionContext.getResponse().getWriter().write(ERROR);
		}
	}
	
	public void setPositionsIds(final List<Integer> positionIds) {
		this.params.put("positionIds", positionIds);
	}
	
	@SkipValidation
	public void populateGroupName() throws IOException {
		final List<NotificationGroup> notificationGroups = this.notificationGroupPersistenceService.findAllBy("from org.egov.infstr.workflow.NotificationGroup where lower(groupName) like ?", ServletActionContext.getRequest().getParameter("query").toLowerCase(Locale.ENGLISH)+"%");
		final StringBuilder notifyGrp = new StringBuilder(EMPTY);
		for (final NotificationGroup notificationGroup : notificationGroups) {
			notifyGrp.append(notificationGroup.getGroupName()).append("\n");
		}
		ServletActionContext.getResponse().getWriter().write(notifyGrp.toString());
	}
	
	public void setBoundaryDAO(final BoundaryDAO boundaryDAO) {
		this.boundaryDAO = boundaryDAO;
	}
	
	public void setBoundaryTypeDAO(final BoundaryTypeDAO boundaryTypeDAO) {
		this.boundaryTypeDAO = boundaryTypeDAO;
	}
	
	public void setHeirarchyTypeDAO(final HeirarchyTypeDAO heirarchyTypeDAO) {
		this.heirarchyTypeDAO = heirarchyTypeDAO;
	}
	
	public void setPersonalInfoDao(final PersonalInformationDAO personalInfoDao) {
		this.personalInfoDao = personalInfoDao;
	}
	
	public void setNotificationGroupPersistenceService(
			PersistenceService<NotificationGroup, Long> notificationGroupPersistenceService) {
		this.notificationGroupPersistenceService = notificationGroupPersistenceService;
	}

	public void setAuditEventService(AuditEventService auditEventService) {
		this.auditEventService = auditEventService;
	}

	@SkipValidation
	public void checkGroupNameExist() throws IOException {
		ServletActionContext.getResponse().getWriter().write(this.checkGroupNameExist(ServletActionContext.getRequest().getParameter("groupName")) ? "not exist" : "exist");
	}
	
	public boolean checkGroupNameExist(final String groupName) {
		return this.notificationGroupPersistenceService.find("from org.egov.infstr.workflow.NotificationGroup where name=?", groupName) == null;
	}
	
	public void validate() {
		if ((this.request.get("groupName") == null) || this.request.get("groupName").equals("")) {
			this.addFieldError("groupName", this.getText("DMS.NOTIFYGRP.GRPNAME.REQUIRED"));
		}
		if ((this.request.get("active") == null) || this.request.get("active").equals("")) {
			this.addFieldError("active", this.getText("DMS.NOTIFYGRP.GRPACTIVE.REQUIRED"));
		}
		if ((this.request.get("effectiveDate") == null) || this.request.get("effectiveDate").equals("")) {
			this.addFieldError("effectiveDate", this.getText("DMS.NOTIFYGRP.EFFDATE.REQUIRED"));
		}
		if (((this.request.get("members") == null) || ((HashSet) this.request.get("members")).isEmpty())) {
			this.addFieldError("members", this.getText("DMS.NOTIFYGRP.MEMBERS.REQUIRED"));
		}
	}
	
	private void doAuditing(String action) {
		final String details = new StringBuffer("[Group Name : ").append(this.notificationGroup.getGroupName()).
											append(", Desc : ").append(this.notificationGroup.getGroupDesc()).append("]").toString();
		final AuditEvent auditEvent = new AuditEvent(AuditModule.DMS, AuditEntity.DMS_NOTIFICATIONGRP, action, this.notificationGroup.getGroupName(), details);
		auditEvent.setPkId(this.notificationGroup.getId());
		this.auditEventService.createAuditEvent(auditEvent, NotificationGroup.class);
	}
}
