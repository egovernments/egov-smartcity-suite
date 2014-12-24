/*
 * @(#)ComplaintDetailServiceImpl.java 3.0, 23 Jul, 2013 3:29:44 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pgr.domain.services.impl;


import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.displaytag.pagination.PaginatedList;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.services.Page;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.utils.EGovConfig;
import org.egov.infstr.utils.UtilityMethods;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.lib.admbndry.BoundaryType;
import org.egov.lib.admbndry.CityWebsiteImpl;
import org.egov.lib.admbndry.ejb.api.BoundaryService;
import org.egov.lib.admbndry.ejb.api.BoundaryTypeService;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.lib.rjbac.dept.ejb.api.DepartmentService;
import org.egov.pgr.constant.PGRConstants;
import org.egov.pgr.domain.entities.ComplaintDetails;
import org.egov.pgr.domain.entities.ComplaintTypes;
import org.egov.pgr.domain.services.ComplaintDetailService;
import org.egov.pgr.domain.services.ComplaintTypeService;
import org.egov.pgr.domain.services.PriorityService;
import org.egov.pgr.services.persistence.EntityServiceImpl;
import org.egov.pgr.utils.PgrCommonUtils;
import org.egov.web.utils.EgovPaginatedList;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class ComplaintDetailServiceImpl extends EntityServiceImpl<ComplaintDetails, Long> implements ComplaintDetailService {

	private static final Logger LOGGER = Logger.getLogger(ComplaintDetailServiceImpl.class);
	private BoundaryTypeService boundaryTypeService;
	private BoundaryService boundaryService;
	private ComplaintTypeService complaintTypeService;
	private DepartmentService departmentService;
	private PriorityService priorityService;

	public ComplaintDetailServiceImpl() {
		super(ComplaintDetails.class);

	}

	@Override
	public PaginatedList searchMyComplaints(final Integer userId, final String mode, final int page, final int pagesize) {
		LOGGER.debug("ComplaintDetailServiceImpl | searchMyComplaints | Start");
		final Criteria criteria = getBuildCriteria(userId, mode);
		final Criteria issueLineCountCriteria = getBuildCriteria(userId, mode);
		final Page complaintPage = new Page(criteria, page, pagesize);
		issueLineCountCriteria.setProjection(Projections.rowCount());
		final int count = ((Long) issueLineCountCriteria.uniqueResult()).intValue();
		final EgovPaginatedList pagedResults = new EgovPaginatedList(complaintPage, count);
		return pagedResults;

	}

	@Override
	public PaginatedList searchComplaintsByName(final HashMap<String, Object> hashMap, final int page, final int pagesize) {
		LOGGER.debug("ComplaintDetailServiceImpl | searchComplaintsByName | Start");
		final Criteria criteria = getBuildCriteriaByName(hashMap);
		final Criteria issueLineCountCriteria = getBuildCriteriaByName(hashMap);
		final Page complaintPage = new Page(criteria, page, pagesize);
		issueLineCountCriteria.setProjection(Projections.rowCount());
		final int count = ((Long) issueLineCountCriteria.uniqueResult()).intValue();
		final EgovPaginatedList pagedResults = new EgovPaginatedList(complaintPage, count);
		return pagedResults;

	}

	@Override
	public ComplaintDetails createNewComplaint(final ComplaintDetails complaint) {
		LOGGER.debug("ComplaintDetailServiceImpl | createNewComplaint | Start");
		if (null == complaint.getComplaintNumber() || StringUtils.isEmpty(complaint.getComplaintNumber())) {
			complaint.setComplaintNumber(generateComplaintNum());
		}
		final Boundary topLevelBoundary = this.boundaryService.getBoundary(Integer.valueOf(ServletActionContext.getRequest().getSession().getAttribute(PGRConstants.SESSION_ATTRIBUTE_TOP_LEVEL_BOUNDARY).toString()));
		complaint.setTopLevelbndry(topLevelBoundary);
		complaint.setComplaintDate(new Date());
		complaint.setEscalatedTime(new Date());
		complaint.setExpiryDate(getComplaintExpiryDate(null != complaint.getComplaintType() ? complaint.getComplaintType().getId() : null, complaint.getEscalatedTime()));

		if (null != complaint.getComplaintType() && null != complaint.getComplaintType().getId()) {

			final ComplaintTypes complaintType = this.complaintTypeService.findById(complaint.getComplaintType().getId(), false);
			complaint.setDepartment(complaintType.getDepartment());
		} else if (null != complaint.getLocBndry()) {
			final BoundaryType btypeforgivenLevel = this.boundaryTypeService.getBoundaryType("Zone", complaint.getLocBndry().getBoundaryType().getHeirarchyType());
			final int bndryZoneInt = PgrCommonUtils.getParentForGivenLevel(complaint.getLocBndry(), btypeforgivenLevel);
			if (bndryZoneInt != 0) {
				complaint.setDepartment((DepartmentImpl) this.departmentService.getDepartment(this.boundaryService.getBoundary(bndryZoneInt).getName()));
			}
		}
		complaint.setIsEscalated(0);
		complaint.setPriority(this.priorityService.findByNamedQuery("getPriorityByName", "No Priority"));
		return complaint;
	}

	@Override
	public Date getComplaintExpiryDate(final Long complaintType, final Date escaDate) {
		int noOfDays = 0;
		if (null != complaintType) {
			noOfDays = this.complaintTypeService.findById(complaintType, false).getNoofdays();
		} else {
			noOfDays = Integer.parseInt(EGovConfig.getProperty("pgr_config.xml", "DEFAULT_NO_DAYS", "", "PGR"));
		}

		return DateUtils.add(escaDate, Calendar.DAY_OF_MONTH, noOfDays);

	}
	
	@Override
	public List<ComplaintDetails> getComplaintDetails(final Map<String, Object> queryMapParam) {
		final Criteria criteria = getGenericComplaintDtls(queryMapParam);
		return criteria.list();
	}
	
	@Override
	public List<ComplaintDetails> getComplaintsEligibleForEscalation() {
		final CityWebsiteImpl cityWebsite = (CityWebsiteImpl)this.getSession().getNamedQuery(CityWebsiteImpl.QUERY_CITY_BY_URL).setString("url", EGOVThreadLocals.getDomainName()).uniqueResult();
		final Integer topLevelBoundaryId = cityWebsite.getBoundaryId().getBndryId();
		final Criteria criteria = getSession().createCriteria(ComplaintDetails.class, "complaint").createAlias("complaint.redressal", "redressalObj").createAlias("redressalObj.complaintStatus", "complaintStatusObj");

		criteria.add(Restrictions.eq("topLevelbndry.id", topLevelBoundaryId));
		criteria.add(Restrictions.ne("complaintStatusObj.name", PGRConstants.COMPLAINT_STATUS_COMPLETED));
		criteria.add(Restrictions.ne("complaintStatusObj.name", PGRConstants.COMPLAINT_STATUS_REJECTED));
		criteria.add(Restrictions.ne("complaintStatusObj.name", PGRConstants.COMPLAINT_STATUS_WITHDRAWN));
		criteria.add(Restrictions.lt("expiryDate", new Date()));
		return criteria.list();
	}

	private Criteria getBuildCriteria(final Integer userId, final String mode) {
		LOGGER.debug("ComplaintDetailServiceImpl | getBuildCriteria | Start");
		Criteria criteria = null;
		criteria = getSession().createCriteria(ComplaintDetails.class, "complaint").createAlias("complaint.redressal", "redressalObj").createAlias("redressalObj.redressalOfficer", "redressalOfficerObj")
				.createAlias("redressalObj.complaintStatus", "complaintStatusObj");
		criteria.add(Restrictions.eq("redressalOfficerObj.id", userId));

		if ("".equals(mode) || !PGRConstants.LISTMYCOMPLAINTS_MODE.equals(mode)) {
			criteria.add(Restrictions.ne("complaintStatusObj.name", PGRConstants.COMPLAINT_STATUS_COMPLETED));
			criteria.add(Restrictions.ne("complaintStatusObj.name", PGRConstants.COMPLAINT_STATUS_REJECTED));
			criteria.add(Restrictions.ne("complaintStatusObj.name", PGRConstants.COMPLAINT_STATUS_WITHDRAWN));
		} else {
			criteria.add(Restrictions.eq("complaintStatusObj.name", PGRConstants.COMPLAINT_STATUS_COMPLETED));
		}
		return criteria;
	}

	private Criteria getBuildCriteriaByName(final HashMap<String, Object> hashMap) {
		Criteria criteria = null;
		criteria = getSession().createCriteria(ComplaintDetails.class, "complaint");
		if (hashMap.get("NAME") != null && !"".equals(hashMap.get("NAME"))) {
			criteria.add(Restrictions.ilike("complaint.firstName", (String) hashMap.get("NAME"), MatchMode.ANYWHERE));
		}

		if (hashMap.get("INITIALS") != null && !"".equals(hashMap.get("INITIALS"))) {
			criteria.add(Restrictions.ilike("complaint.initials", (String) hashMap.get("INITIALS"), MatchMode.ANYWHERE));
		}

		if (hashMap.get("LASTNAME") != null && !"".equals(hashMap.get("LASTNAME"))) {
			criteria.add(Restrictions.ilike("complaint.lastName", (String) hashMap.get("LASTNAME"), MatchMode.ANYWHERE));
		}

		if (hashMap.get("EMAIL") != null && !"".equals(hashMap.get("EMAIL"))) {
			criteria.add(Restrictions.ilike("complaint.email", (String) hashMap.get("EMAIL"), MatchMode.ANYWHERE));
		}

		if (hashMap.get("PHONENO") != null && !"".equals(hashMap.get("PHONENO"))) {
			final Criterion telephone = Restrictions.ilike("complaint.telePhone", hashMap.get("PHONENO"));
			final Criterion mobile = Restrictions.ilike("complaint.mobileNumber", hashMap.get("PHONENO"));
			final Disjunction disjunction = Restrictions.disjunction();
			disjunction.add(telephone);
			disjunction.add(mobile);
			criteria.add(disjunction);

		}
		if (hashMap.get("ADDRESS") != null && !"".equals(hashMap.get("ADDRESS"))) {
			final Criterion complaintLocation = Restrictions.ilike("complaint.address", (String) hashMap.get("ADDRESS"), MatchMode.ANYWHERE);
			final Criterion address = Restrictions.ilike("complaint.complaintlocation", (String) hashMap.get("ADDRESS"), MatchMode.ANYWHERE);
			final Criterion pincode = Restrictions.ilike("complaint.pincode", (String) hashMap.get("ADDRESS"), MatchMode.ANYWHERE);
			final Disjunction disjunction = Restrictions.disjunction();
			disjunction.add(complaintLocation);
			disjunction.add(address);
			disjunction.add(pincode);
			criteria.add(disjunction);
		}
		criteria.list();
		return criteria;
	}

	private String generateComplaintNum() {

		return UtilityMethods.getRandomString();
	}

	private Criteria getGenericComplaintDtls(final Map<String, Object> queryMapParam) {
		Criteria criteria = null;
		final Date fromDate = (Date) queryMapParam.get("FROM_DATE");
		final Date toDate = (Date) queryMapParam.get("TO_DATE");
		final Integer zoneId = (Integer) queryMapParam.get("ZONE");
		final Integer wardId = (Integer) queryMapParam.get("WARD");
		final ComplaintDetails complaintDtls = (ComplaintDetails) queryMapParam.get("COMPLAINTDETAILS");
		criteria = getSession().createCriteria(ComplaintDetails.class, "complaintDtls").createAlias("complaintDtls.redressal", "redressalObj").createAlias("redressalObj.redressalOfficer", "redressalOfficerObj")
				.createAlias("redressalObj.complaintStatus", "complaintStatusObj");
		if (null != fromDate && null == toDate) {
			criteria.add(Restrictions.ge("complaintDate", fromDate));
		} else if (null != toDate && null == fromDate) {
			criteria.add(Restrictions.le("complaintDate", toDate));
		} else if (null != fromDate && null != toDate) {
			criteria.add(Restrictions.between("complaintDate", fromDate, toDate));
		}
		if (null != zoneId && zoneId != -1) {
			final BoundaryDAO boundaryDAO = new BoundaryDAO();
			if (null != wardId && wardId != -1) {
				final Boundary boundary = boundaryDAO.getBoundary(wardId);
				criteria.add(Restrictions.eq("locBndry", boundary));
			} else {
				final Boundary boundary = boundaryDAO.getBoundary(zoneId);
				criteria.add(Restrictions.eq("locBndry", boundary));
			}
		}
		if (null != complaintDtls) {
			if (null != complaintDtls.getRedressal()) {
				if (null != complaintDtls.getRedressal().getComplaintStatus()) {
					criteria.add(Restrictions.eq("complaintStatusObj.id", complaintDtls.getRedressal().getComplaintStatus().getId()));
				}
				if (null != complaintDtls.getRedressal().getRedressalOfficer()) {
					criteria.add(Restrictions.eq("redressalOfficerObj.id", complaintDtls.getRedressal().getRedressalOfficer().getId()));
				}
			}
			if (null != complaintDtls.getDepartment()) {
				criteria.add(Restrictions.eq("department.id", complaintDtls.getDepartment().getId()));
			}
			if (null != complaintDtls.getReceivingCenter()) {
				criteria.add(Restrictions.eq("receivingCenter.id", complaintDtls.getReceivingCenter().getId()));

			}
			if ((null != complaintDtls.getOthervalue()) && (!(org.egov.infstr.utils.StringUtils.isEmpty(complaintDtls.getOthervalue())))) {
				criteria.add(Restrictions.eq("othervalue", complaintDtls.getOthervalue()));
			}
		}
		return criteria;
	}


	/**
	 * @param boundaryService the boundaryService to set
	 */
	public void setBoundaryService(final BoundaryService boundaryService) {
		this.boundaryService = boundaryService;
	}

	/**
	 * @param complaintTypeService the complaintTypeService to set
	 */
	public void setComplaintTypeService(final ComplaintTypeService complaintTypeService) {
		this.complaintTypeService = complaintTypeService;
	}

	/**
	 * @param boundaryTypeService the boundaryTypeService to set
	 */
	public void setBoundaryTypeService(final BoundaryTypeService boundaryTypeService) {
		this.boundaryTypeService = boundaryTypeService;
	}

	/**
	 * @param departmentService the departmentService to set
	 */
	public void setDepartmentService(final DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	/**
	 * @param priorityService the priorityService to set
	 */
	public void setPriorityService(final PriorityService priorityService) {
		this.priorityService = priorityService;
	}

}
