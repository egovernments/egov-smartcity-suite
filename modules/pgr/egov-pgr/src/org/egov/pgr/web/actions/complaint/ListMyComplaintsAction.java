/*
 * @(#)ListMyComplaintsAction.java 3.0, 23 Jul, 2013 3:29:43 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pgr.web.actions.complaint;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.models.GeoLocation;
import org.egov.infstr.search.SearchQuery;
import org.egov.pgr.constant.PGRConstants;
import org.egov.pgr.domain.entities.ComplaintDetails;
import org.egov.pgr.domain.services.ComplaintDetailService;
import org.egov.web.actions.SearchFormAction;

@ParentPackage("egov")
public class ListMyComplaintsAction extends SearchFormAction {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ListMyComplaintsAction.class);
	private ComplaintDetailService complaintService;
	private List<GeoLocation> geoLocationList = new ArrayList<GeoLocation>();
	private String mode;

	@Override
	public Object getModel() {
		return null;
	}

	@Override
	public SearchQuery prepareQuery(final String sortField, final String sortOrder) {
		return null;
	}

	public String newform() {
		LOGGER.debug("ListMyComplaintsAction | newform | Start");
		if (getMode() == null) {
			setMode("");
		}
		this.searchResult = this.complaintService.searchMyComplaints(Integer.valueOf(EGOVThreadLocals.getUserId()), getMode(), getPage(), getPageSize());
		final List<ComplaintDetails> finalResult = (this.searchResult != null ? this.searchResult.getList() : null);
		if (finalResult != null && !this.mode.equals(PGRConstants.LISTMYCOMPLAINTS_MODE)) {
			for (final ComplaintDetails complaint : finalResult) {
				if (null != complaint.getGeoLocationDetails() && null != complaint.getGeoLocationDetails().getGeoLatLong()) {
					this.geoLocationList.add(complaint.getGeoLocationDetails());
					LOGGER.debug("geoLocationList.size()----->" + this.geoLocationList.size());
				}
			}
		}
		LOGGER.debug("ListMyComplaintsAction | newform | End");
		return NEW;
	}

	public void setComplaintService(final ComplaintDetailService complaintService) {
		this.complaintService = complaintService;
	}

	public void setGeoLocationList(final List<GeoLocation> geoLocationList) {
		this.geoLocationList = geoLocationList;
	}

	public String getMode() {
		return this.mode;
	}

	public void setMode(final String mode) {
		this.mode = mode;
	}

}
