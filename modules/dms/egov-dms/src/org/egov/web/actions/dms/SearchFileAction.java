/*
 * @(#)SearchFileAction.java 3.0, 16 Jul, 2013 11:35:59 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.web.actions.dms;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.dms.models.GenericFile;
import org.egov.dms.models.OutboundFile;
import org.egov.exceptions.NoSuchObjectException;
import org.egov.exceptions.TooManyValuesException;
import org.egov.infstr.models.State;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.admbndry.BoundaryType;
import org.egov.lib.admbndry.BoundaryTypeDAO;
import org.egov.lib.admbndry.HeirarchyType;
import org.egov.lib.admbndry.HeirarchyTypeDAO;
import org.egov.web.actions.BaseFormAction;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

@ParentPackage("egov")
public class SearchFileAction extends BaseFormAction {
	
	private static final long serialVersionUID = -4631998962558295941L;
	private transient BoundaryDAO boundaryDAO;
	private transient BoundaryTypeDAO boundaryTypeDAO;
	private transient HeirarchyTypeDAO heirarchyTypeDAO;
	private transient final Map<String, Object> ajaxReqParam = new HashMap<String, Object>();
	private transient Integer departmentId;
	private transient String fileType;
	private List<GenericFile> genericfileList = new ArrayList<GenericFile>();
	private List<String> fileListDropDown;
	private transient String fileNumber;
	private transient Date fileDateFrom;
	private transient Date fileDateTo;
	private transient Integer firstLevelBndry;
	private transient Integer secondLevelBndry;
	private transient Date fileReceivedDateFrom;
	private transient Date fileReceivedDateTo;
	private transient Integer fileStatusId;
	private transient Long fileSourceId;
	private transient Long fileCategoryId;
	private transient Long fileSubCategoryId;
	private transient Long filePriorityId;
	private transient String module;
	private transient String outboundFileNumber;
	private String mode = "search";
	
	
	
	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	@Override
	public Object getModel() {
		return null;
	}
	
	@Override
	public void prepare() {
		super.prepare();
		this.addDropdownData("departmentList", this.getPersistenceService().findAllBy("from DepartmentImpl order by deptName"));
		this.addDropdownData("fileStatusList", this.getPersistenceService().findAllBy("from EgwStatus where  lower(moduletype) like 'dms_filestatus' order by id"));
		this.addDropdownData("fileSourceList", this.getPersistenceService().findAllBy("from FileSource  order by name"));
		this.addDropdownData("fileCatList", this.getPersistenceService().findAllBy("from FileCategory where parent is  null order by name"));
		this.addDropdownData("fileSubCatList", this.getPersistenceService().findAllBy(" from FileCategory where parent is not null order by name"));
		this.addDropdownData("filePriorityList", this.getPersistenceService().findAllBy(" from FilePriority  order by name"));
		try {
			this.addDropdownData("sources", this.getFirstLevelBoundries());
		} catch (final NoSuchObjectException e) {
			e.printStackTrace();
		} catch (final TooManyValuesException e) {
			e.printStackTrace();
		}
		this.addDropdownData("fileTypeList", this.getPersistenceService().findAllBy(" from AppConfigValues  where key.module like 'DMS' and key.keyName like 'filetypes'  order by id"));
	}
	
	@Override
	public String execute() {
		return SUCCESS;
	}
	
	public String report() {
		mode="report";
		return SUCCESS;
	}
	
	public String search() {
		this.setGenericfileList(this.createSearchQuery().list());
		return SUCCESS;
	}
	
	// get all the wards
	public List<BoundaryImpl> getFirstLevelBoundries() throws NoSuchObjectException, TooManyValuesException {
		final HeirarchyType heirarchyType = this.heirarchyTypeDAO.getHierarchyTypeByName("ADMINISTRATION");
		final BoundaryType boundaryType = this.boundaryTypeDAO.getBoundaryType("City", heirarchyType);
		final List<BoundaryImpl> cities = this.boundaryDAO.getAllBoundariesByBndryTypeId(boundaryType.getId());
		final List<BoundaryImpl> firstLevel = new ArrayList<BoundaryImpl>();
		for (final BoundaryImpl city : cities) {
			firstLevel.addAll(city.getChildren());
		}
		return firstLevel;
	}
	
	/*
	 * Gets the zone list for the given ward
	 */
	public void getSecondLevelBoundries() throws Exception {
		final List<Boundary> firstLevel = this.boundaryDAO.getChildBoundaries(this.ajaxReqParam.get("parentId").toString());
		if ((firstLevel == null) || firstLevel.isEmpty()) {
			ServletActionContext.getResponse().getWriter().write(ERROR);
		} else {
			final StringBuilder wardStr = new StringBuilder("[");
			for (final Boundary boundary : firstLevel) {
				wardStr.append("{name : '").append(boundary.getName()).append("',id:").append(boundary.getId()).append("},");
			}
			wardStr.deleteCharAt(wardStr.length() - 1);
			wardStr.append("]");
			ServletActionContext.getResponse().getWriter().write(wardStr.toString());
		}
	}
	
	public void setHeirarchyTypeDAO(final HeirarchyTypeDAO heirarchyTypeDAO) {
		this.heirarchyTypeDAO = heirarchyTypeDAO;
	}
	
	public void setBoundaryDAO(final BoundaryDAO boundaryDAO) {
		this.boundaryDAO = boundaryDAO;
	}
	
	public void setBoundaryTypeDAO(final BoundaryTypeDAO boundaryTypeDAO) {
		this.boundaryTypeDAO = boundaryTypeDAO;
	}
	
	public Integer getDepartmentId() {
		return this.departmentId;
	}
	
	public void setDepartmentId(final Integer departmentId) {
		this.departmentId = departmentId;
	}
	
	public String getFileType() {
		return this.fileType;
	}
	
	public void setFileType(final String fileType) {
		this.fileType = fileType;
	}
	
	public Long getFileCategoryId() {
		return this.fileCategoryId;
	}
	
	public void setFileCategoryId(final Long fileCategoryId) {
		this.fileCategoryId = fileCategoryId;
	}
	
	public List<GenericFile> getGenericfileList() {
		return this.genericfileList;
	}
	
	public void setGenericfileList(final List<GenericFile> genericfileList) {
		this.genericfileList = genericfileList;
	}
	
	public List<String> getFileListDropDown() {
		return this.fileListDropDown;
	}
	
	public Date getFileDateFrom() {
		return this.fileDateFrom;
	}
	
	public void setFileDateFrom(final Date fileDateFrom) {
		this.fileDateFrom = fileDateFrom;
	}
	
	public String getFileNumber() {
		return this.fileNumber;
	}
	
	public void setFileNumber(final String fileNumber) {
		this.fileNumber = fileNumber;
	}
	
	public Date getFileDateTo() {
		return this.fileDateTo;
	}
	
	public void setFileDateTo(final Date fileDateTo) {
		this.fileDateTo = fileDateTo;
	}
	
	public Integer getFirstLevelBndry() {
		return this.firstLevelBndry;
	}
	
	public void setFirstLevelBndry(final Integer firstLevelBndry) {
		this.firstLevelBndry = firstLevelBndry;
	}
	
	public Integer getSecondLevelBndry() {
		return this.secondLevelBndry;
	}
	
	public void setSecondLevelBndry(final Integer secondLevelBndry) {
		this.secondLevelBndry = secondLevelBndry;
	}
	
	public Date getFileReceivedDateFrom() {
		return this.fileReceivedDateFrom;
	}
	
	public void setFileReceivedDateFrom(final Date fileReceivedDateFrom) {
		this.fileReceivedDateFrom = fileReceivedDateFrom;
	}
	
	public Date getFileReceivedDateTo() {
		return this.fileReceivedDateTo;
	}
	
	public void setFileReceivedDateTo(final Date fileReceivedDateTo) {
		this.fileReceivedDateTo = fileReceivedDateTo;
	}
	
	public Integer getFileStatusId() {
		return this.fileStatusId;
	}
	
	public void setFileStatusId(final Integer fileStatusId) {
		this.fileStatusId = fileStatusId;
	}
	
	public Long getFileSourceId() {
		return this.fileSourceId;
	}
	
	public void setFileSourceId(final Long fileSourceId) {
		this.fileSourceId = fileSourceId;
	}
	
	public Long getFileSubCategoryId() {
		return this.fileSubCategoryId;
	}
	
	public void setFileSubCategoryId(final Long fileSubCategoryId) {
		this.fileSubCategoryId = fileSubCategoryId;
	}
	
	public Long getFilePriorityId() {
		return this.filePriorityId;
	}
	
	public void setFilePriorityId(final Long filePriorityId) {
		this.filePriorityId = filePriorityId;
	}
	
	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}
	
	public String getOutboundFileNumber() {
		return outboundFileNumber;
	}

	public void setOutboundFileNumber(String outboundFileNumber) {
		this.outboundFileNumber = outboundFileNumber;
	}

	private Criteria createSearchQuery() {
		final Criteria criteria = this.getPersistenceService().getSession().createCriteria(GenericFile.class, "genericFile");
		
		if (StringUtils.isNotBlank(this.getFileNumber())) {
			criteria.add(Restrictions.eq("fileNumber", this.getFileNumber()));
		}
		else {
		if(this.getDepartmentId()!=null && !this.getDepartmentId().equals(""))
		{
			criteria.add(Restrictions.eq("department.id", this.getDepartmentId()));
		}
		if(this.getFileCategoryId()!=null && !this.getFileCategoryId().equals(""))
		{
			criteria.add(Restrictions.eq("fileCategory.id", this.getFileCategoryId()));
		}
		if( this.getFileType()!=null && !this.getFileType().equals(""))
		{
			criteria.add((Restrictions.eq("fileType", this.getFileType())));
		}
		if ((this.getFileStatusId() != null) && !this.getFileStatusId().equals("")) {
			criteria.add(Restrictions.eq("fileStatus.id", this.getFileStatusId()));
		}
		if ((this.getFileSubCategoryId() != null) && !this.getFileSubCategoryId().equals("")) {
			criteria.add(Restrictions.eq("fileSubcategory.id", this.getFileSubCategoryId()));
		}
		if (this.getFileType().equals("INBOUND")) {
			if ((this.getFileSourceId() != null) && !this.getFileSourceId().equals("")) {
				criteria.add(Restrictions.eq("sender.fileSource.id", this.getFileSourceId()));
			}
		} else if (this.getFileType().equals("OUTBOUND")) {
			if ((this.getFileSourceId() != null) && !this.getFileSourceId().equals("")) {
				criteria.add(Restrictions.eq("receiver.fileSource.id", this.getFileSourceId()));
			}
		}
		if ((this.getFilePriorityId() != null) && !this.getFilePriorityId().equals("")) {
			criteria.add(Restrictions.eq("filePriority.id", this.getFilePriorityId()));
		}
		if ((this.getFirstLevelBndry() != null) && !this.getFirstLevelBndry().equals("")) {
			criteria.add(Restrictions.eq("firstLevelBndry.id", this.getFirstLevelBndry()));
		}
		if ((this.getSecondLevelBndry() != null) && !this.getSecondLevelBndry().equals("")) {
			criteria.add(Restrictions.eq("secondLevelBndry.id", this.getSecondLevelBndry()));
		}
		if ((this.getFileDateFrom() != null) && !this.getFileDateFrom().equals("")) {
			if ((this.getFileDateTo() != null) && !this.getFileDateTo().equals("")) {
				criteria.add(Restrictions.and(Restrictions.ge("fileDate", this.getFileDateFrom()), Restrictions.le("fileDate", this.getFileDateTo())));
			} else {
				criteria.add(Restrictions.ge("fileDate", this.getFileDateFrom()));
			}
		} else if ((this.getFileDateTo() != null) && !this.getFileDateTo().equals("")) {
			criteria.add(Restrictions.le("fileDate", this.getFileDateTo()));
		}
		if ((this.getFileReceivedDateFrom() != null) && !this.getFileReceivedDateFrom().equals("")) {
			if ((this.getFileReceivedDateTo() != null) && !this.getFileReceivedDateTo().equals("")) {
				criteria.add(Restrictions.and(Restrictions.ge("fileReceivedOrSentDate", this.getFileDateFrom()), Restrictions.le("fileReceivedOrSentDate", this.getFileReceivedDateTo())));
			} else {
				criteria.add(Restrictions.ge("fileReceivedOrSentDate", this.getFileReceivedDateFrom()));
			}
		} else if ((this.getFileReceivedDateTo() != null) && !this.getFileReceivedDateTo().equals("")) {
			criteria.add(Restrictions.le("fileReceivedOrSentDate", this.getFileReceivedDateFrom()));
		}

		if (this.getOutboundFileNumber() != null && !"".equals(this.getOutboundFileNumber())) {
			//either receiver outbound file number for outbound file is set or fileAssignment outbound file number is set
			criteria.createAlias("fileAssignmentDetails", "fileAssignment")
			 .createAlias("fileAssignment.externalUser", "externalUser");
			DetachedCriteria subCrit = DetachedCriteria.forClass(OutboundFile.class).createAlias("receiver", "receiver")
										.add(Restrictions.eq("receiver.outboundFileNumber", this.getOutboundFileNumber()))
										.setProjection(Projections.distinct(Projections.projectionList()
										.add(Projections.property("id"))));;
			criteria.add(Restrictions.disjunction().add(
					Subqueries.propertyIn("id", subCrit))
					.add(Restrictions.eq("externalUser.outboundFileNumber", this.getOutboundFileNumber())));
		}
			
		}
		criteria.createAlias("state", "state").add(Restrictions.ne("state.value",State.NEW));
		criteria.addOrder(Order.asc("fileNumber"));
		return criteria;
	}
	
	public void setParentId(final String parentId) {
		this.ajaxReqParam.put("parentId", parentId);
	}
}
