/*
 * @(#)ComplaintAction.java 3.0, 23 Jul, 2013 3:29:43 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pgr.web.actions.complaint;

import java.io.PrintWriter;
import java.util.Collections;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rjbac.user.ejb.server.UserServiceImpl;
import org.egov.pgr.constant.PGRConstants;
import org.egov.pgr.domain.services.PriorityService;
import org.egov.pims.commons.Position;
import org.egov.pims.service.EmployeeService;
import org.hibernate.Query;

@ParentPackage("egov")
public class ComplaintAction extends BaseComplaintAction {
	private static final Logger LOGGER = Logger.getLogger(ComplaintAction.class);
	private static final long serialVersionUID = 1L;
	private UserServiceImpl userService;
	private PriorityService priorityService;
	private String mode;
	private Integer zone;
	private Integer ward;
	private Integer area;
	private Integer street;
	private String actionName;
	private EmployeeService employeeService;

	@Override
	public void prepare() {

		this.complaintDetails = this.complaintService.findById(this.complaintDetails.getId(), false);
		super.prepare();
	}

	public String getViewedBy(final String complaintNumber) {

		final Query query = HibernateUtil.getCurrentSession().createSQLQuery(" select USERID from EG_VIEW where COMPLAINTNUMBER='" + complaintNumber + "'");
		if (null != query.list()) {

			return this.userService.getUserByID(Integer.valueOf(query.list().get(0).toString())).getUserName();
		} else {
			return null;
		}

	}

	@SkipValidation
	public String view() {
		LOGGER.debug("ComplaintAction | view | Start >> " + this.complaintDetails.getId());

		final EgovMasterDataCaching masterCache = EgovMasterDataCaching.getInstance();
		addDropdownData("departmentList", masterCache.get("egi-department"));
		addDropdownData("designationList", Collections.EMPTY_LIST);
		addDropdownData("userList", Collections.EMPTY_LIST);
		addDropdownData("statusList", this.complaintStatusService.getStatusByUserOrCitizen());
		addDropdownData("priorityList", this.priorityService.findAll());

		addDropdownData("zoneList", this.pgrCommonUtils.getAllZoneOfHTypeAdmin());
		addDropdownData("wardList", Collections.EMPTY_LIST);
		addDropdownData("areaList", Collections.EMPTY_LIST);
		addDropdownData("streetList", Collections.EMPTY_LIST);
		addDropdownData("top5Complaints", this.complaintTypeService.getTop5Complaints());

		this.boundaryFields.add("zone");
		this.boundaryFields.add("ward");
		this.boundaryFields.add("area");
		this.boundaryFields.add("street");

		return "view";
	}

	public String saveOrForward() {

		this.complaintDetails.getRedressal().setComplaintStatus(this.complaintStatusService.findById(this.complaintDetails.getRedressal().getComplaintStatus().getId(), false));

		String status = this.complaintDetails.getRedressal().getComplaintStatus().getName();

		Position position = this.eisCommonsService.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
		if (PGRConstants.COMPLAINT_STATUS_COMPLETED.equalsIgnoreCase(status) || PGRConstants.COMPLAINT_STATUS_REJECTED.equalsIgnoreCase(status)) {

			status = "END";

		}

		if ("FORWARD".equalsIgnoreCase(this.actionName)) {

			position = this.eisCommonsService.getPositionByUserId(Integer.valueOf(this.parameters.get("approverUserId")[0]));
			addActionMessage(getText("complaint.forward", new String[] { this.employeeService.getEmployeeforPosition(position).getEmployeeFirstName() }));
		} else {

			addActionMessage(getText("complaint.save"));
		}

		this.complaintDetails.changeState(status, "", position, null != this.parameters.get("comments") ? this.parameters.get("comments")[0] : "");
		this.complaintDetails.getRedressal().setRedressalOfficer(this.eisCommonsService.getUserforPosition(position));
		this.complaintDetails.getRedressal().setPosition(position);
		final Integer boundary = this.street != null && this.street != -1 ? this.street : this.area != null && this.area != -1 ? this.area : this.ward != null && this.ward != -1 ? this.ward : this.zone != null && this.zone != -1 ? this.zone : null;
		if (null != boundary) {
			this.complaintDetails.setLocBndry(this.boundaryService.getBoundary(boundary.intValue()));
			this.complaintDetails.setBoundary(getBoundry(boundary));
		}

		return "message";
	}

	public String getGeoDisplayUrl() {
		try {
			final HttpServletResponse res = ServletActionContext.getResponse();
			res.setContentType("text/xml");
			final StringBuffer markerDesc = new StringBuffer(1000);
			markerDesc.append("<tr><td><b>Complaint Number</b></td><td>" + this.complaintDetails.getComplaintNumber()).append("</td>").append("<tr><td><b> Title</b></td><td>" + this.complaintDetails.getTitle()).append("</td>")
					.append("<tr><td><b> Person Name</b></td><td>" + this.complaintDetails.getFirstName()).append("</td>").append("<tr><td><b> Mode</b></td><td>" + this.complaintDetails.getCompReceivingModes().getCompMode()).append("</td>");
			markerDesc.append("</tr>");
			final PrintWriter out = res.getWriter();
			out.println(markerDesc.toString());
			out.flush();
			out.close();
		} catch (final Exception e) {
			LOGGER.error(e.getMessage());
		}

		return null;
	}

	/**
	 * @param userService the userService to set
	 */
	public void setUserService(final UserServiceImpl userService) {
		this.userService = userService;
	}

	/**
	 * @param priorityService the priorityService to set
	 */
	public void setPriorityService(final PriorityService priorityService) {
		this.priorityService = priorityService;
	}

	/**
	 * @return the mode
	 */
	public String getMode() {
		return this.mode;
	}

	/**
	 * @return the zone
	 */
	public Integer getZone() {
		return this.zone;
	}

	/**
	 * @param zone the zone to set
	 */
	public void setZone(final Integer zone) {
		this.zone = zone;
	}

	/**
	 * @return the ward
	 */
	public Integer getWard() {
		return this.ward;
	}

	/**
	 * @param ward the ward to set
	 */
	public void setWard(final Integer ward) {
		this.ward = ward;
	}

	/**
	 * @return the area
	 */
	public Integer getArea() {
		return this.area;
	}

	/**
	 * @param area the area to set
	 */
	public void setArea(final Integer area) {
		this.area = area;
	}

	/**
	 * @return the street
	 */
	public Integer getStreet() {
		return this.street;
	}

	/**
	 * @param street the street to set
	 */
	public void setStreet(final Integer street) {
		this.street = street;
	}

	/**
	 * @param mode the mode to set
	 */
	public void setMode(final String mode) {
		this.mode = mode;
	}

	/**
	 * @return the actionName
	 */
	public String getActionName() {
		return this.actionName;
	}

	/**
	 * @param actionName the actionName to set
	 */
	public void setActionName(final String actionName) {
		this.actionName = actionName;
	}

	/**
	 * @param employeeService the employeeService to set
	 */
	public void setEmployeeService(final EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

}
