/*
 * @(#)ComplaintGenericReportAction.java 3.0, 23 Jul, 2013 3:29:43 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pgr.web.actions.reports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.infstr.models.GeoLocation;
import org.egov.infstr.services.GeoLocationConstants;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.pgr.domain.entities.ComplaintDetails;
import org.egov.pgr.domain.entities.ComplaintReceivingCenter;
import org.egov.pgr.domain.entities.ComplaintStatus;
import org.egov.pgr.domain.services.ComplaintDetailService;
import org.egov.pgr.web.actions.complaint.BaseComplaintAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.hibernate.Query;

@ParentPackage("egov")
public class ComplaintGenericReportAction extends BaseComplaintAction {
	private static final long serialVersionUID = 1L;
	private transient List<ComplaintReceivingCenter> receivingCenterList;
	private transient List<ComplaintStatus> complaintStatusList;
	private static final Logger LOGGER = Logger.getLogger(ComplaintGenericReportAction.class);
	private static final String SEARCH_VIEW = "search";
	private Integer dateselection;
	private transient List<ComplaintDetails> complaintDtlsList;
	private Date complaintFromDate;
	private Date complaintToDate;
	private Integer zone;
	private Integer ward;

	@Override
	public void prepare() {
		super.prepare();
		LOGGER.debug("ComplaintGenericAction | prepare | Start");
		this.receivingCenterList = this.compReceivingCenterService.getAllComplaintReceivingCenter();
		addDropdownData("receivingCenter", this.receivingCenterList);
		addDropdownData("zoneList", this.pgrCommonUtils.getAllZoneOfHTypeAdmin());
		addDropdownData("wardList", Collections.EMPTY_LIST);
		addDropdownData("departmentList", this.pgrCommonUtils.getAllDepartments());
		addDropdownData("userList", Collections.EMPTY_LIST);
		this.boundaryFields.add("zone");
		this.boundaryFields.add("ward");
		this.complaintStatusList = this.complaintStatusService.getAllComplaintStatus();
		addDropdownData("complaintStatus", this.complaintStatusList);
	}

	public String newForm() {

		return SEARCH_VIEW;
	}

	@ValidationErrorPage(value = SEARCH_VIEW)
	public String search() {
		LOGGER.debug("ComplaintGenericAction | Generic Search | Start");
		final Map<String, Object> queryMapParam = getQuerParamMap();
		if (null != queryMapParam && !(queryMapParam.isEmpty())) {
			this.complaintDtlsList = this.complaintService.getComplaintDetails(queryMapParam);
		}
		final List<GeoLocation> geoLocationList = getGeoLocationList();
		if (!geoLocationList.isEmpty()) {
			ServletActionContext.getRequest().setAttribute(GeoLocationConstants.GEOLOCATIONLIST_ATTRIBUTE, geoLocationList);
		}

		return SEARCH_VIEW;
	}

	private Map<String, Object> getQuerParamMap() {
		final Map<String, Object> queryMapParam = new HashMap<String, Object>();
		queryMapParam.put("FROM_DATE", getComplaintFromDate());
		queryMapParam.put("TO_DATE", getComplaintToDate());
		queryMapParam.put("ZONE", getZone());
		queryMapParam.put("WARD", getWard());
		queryMapParam.put("COMPLAINTDETAILS", this.complaintDetails);
		return queryMapParam;
	}

	private List<GeoLocation> getGeoLocationList() {
		final List<GeoLocation> geoLocationList = new ArrayList<GeoLocation>();
		for (final ComplaintDetails compDtls : this.complaintDtlsList) {
			final Map<String, Object> markerOptData = new HashMap<String, Object>();
			if (null != compDtls.getGeoLocationDetails() && null != compDtls.getGeoLocationDetails().getGeoLatLong() && null != compDtls.getGeoLocationDetails().getGeoLatLong().getLatitude()
					&& null != compDtls.getGeoLocationDetails().getGeoLatLong().getLongitude()) {
				final GeoLocation locMap = compDtls.getGeoLocationDetails();
				if (null != compDtls.getPriority()) {
					if (compDtls.getPriority().getId().equals(Long.valueOf(0))) {
						markerOptData.put(GeoLocationConstants.MARKEROPTION_ICON, GeoLocationConstants.MARKEROPTION_ICON_BLUE);
					} else if (compDtls.getPriority().getId().equals(Long.valueOf(1))) {
						markerOptData.put(GeoLocationConstants.MARKEROPTION_ICON, GeoLocationConstants.MARKEROPTION_ICON_RED);
					} else if (compDtls.getPriority().getId().equals(Long.valueOf(2))) {
						markerOptData.put(GeoLocationConstants.MARKEROPTION_ICON, GeoLocationConstants.MARKEROPTION_ICON_YELLOW);
					} else if (compDtls.getPriority().getId().equals(Long.valueOf(3))) {
						markerOptData.put(GeoLocationConstants.MARKEROPTION_ICON, GeoLocationConstants.MARKEROPTION_ICON_GREEN);
					}
					markerOptData.put(GeoLocationConstants.MARKEROPTION_TITLE, compDtls.getComplaintNumber());
					locMap.setMarkerOptionData(markerOptData);
				}
				geoLocationList.add(locMap);
			}
		}
		return geoLocationList;
	}

	public Date getViewedBy(final String complaintNumber) {

		final Query query = HibernateUtil.getCurrentSession().createSQLQuery(" select VIEWDATE from EG_VIEW where COMPLAINTNUMBER='" + complaintNumber + "'");
		return query.uniqueResult() == null ? null : (Date)query.uniqueResult();
	}

	public ComplaintDetailService getComplaintService() {
		return this.complaintService;
	}

	@Override
	public void setComplaintService(final ComplaintDetailService complaintService) {
		this.complaintService = complaintService;
	}

	public Integer getDateselection() {
		return this.dateselection;
	}

	public void setDateselection(final Integer dateselection) {
		this.dateselection = dateselection;
	}

	public List<ComplaintDetails> getComplaintDtlsList() {
		return this.complaintDtlsList;
	}

	public void setComplaintDtlsList(final List<ComplaintDetails> complaintDtlsList) {
		this.complaintDtlsList = complaintDtlsList;
	}

	public Date getComplaintFromDate() {
		return this.complaintFromDate;
	}

	public void setComplaintFromDate(final Date complaintFromDate) {
		this.complaintFromDate = complaintFromDate;
	}

	public Date getComplaintToDate() {
		return this.complaintToDate;
	}

	public void setComplaintToDate(final Date complaintToDate) {
		this.complaintToDate = complaintToDate;
	}

	public Integer getWard() {
		return this.ward;
	}

	public void setWard(final Integer ward) {
		this.ward = ward;
	}

	public Integer getZone() {
		return this.zone;
	}

	public void setZone(final Integer zone) {
		this.zone = zone;
	}
}