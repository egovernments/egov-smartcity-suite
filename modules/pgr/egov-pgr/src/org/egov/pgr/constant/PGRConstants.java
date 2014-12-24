/*
 * @(#)PGRConstants.java 3.0, 23 Jul, 2013 3:29:42 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pgr.constant;

import java.util.Locale;

public interface PGRConstants {

	String MODULE_NAME = "PGR";
	Locale LOCALE = new Locale("en", "IN");
	String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	String EG_OBJECT_TYPE_COMPLAINT = "Complaint";
	String COMPLAINT_STATUS_REGISTERED = "REGISTERED";
	String COMPLAINT_STATUS_PROCESSING = "PROCESSING";
	String COMPLAINT_STATUS_FORWARDED = "FORWARDED";
	String COMPLAINT_STATUS_COMPLETED = "COMPLETED";
	String COMPLAINT_STATUS_REJECTED = "REJECTED";
	String COMPLAINT_STATUS_REOPENED = "REOPENED";
	String COMPLAINT_STATUS_NOTCOMPLETED = "NOTCOMPLETED";
	String COMPLAINT_STATUS_WITHDRAWN = "WITHDRAWN";
	String VALIDATION_MESSAGE_REQUIRED = "required";
	String ACTION_METHOD_RETURN_WARDS = "wards";
	String ACTION_METHOD_RETURN_AREAS = "areas";
	String ACTION_METHOD_RETURN_STREETS = "streets";
	String HIERARCHY_TYPE_LOCATION = "LOCATION";
	String HIERARCHY_TYPE_ADMINISTRATION = "ADMINISTRATION";
	String EG_MASTER_DEPARTMENT_AS_DEFAULT = "DEPARTMENT_DEFAULT";
	String EG_DEFAULT_TOPLEVELBNDRY_DEFAULT = "TOPLEVELBNDRY_DEFAULT";
	String SESSION_ATTRIBUTE_TOP_LEVEL_BOUNDARY = "org.egov.topBndryID";
	String WARD_BOUNDARY_TYPE_NAME = "Ward";	
	String ZONE_BOUNDARY_TYPE_NAME = "Zone";	
	String STREET_BOUNDARY_TYPE_NAME = "Street";
	String AREA_BOUNDARY_TYPE_NAME = "Area";
	String PGR_GOOGLE_MAP_URL_REDIRECT_PATH = "/pgr/common/complaint!view.action?model.id=";
	String PGR_GOOGLE_MAP_URL_DISPLAY = "/pgr/common/complaint!getGeoDisplayUrl.action?model.id=";
	String LISTMYCOMPLAINTS_MODE = "completed";
	String CITIZEN_USER = "COMPLAINANT";
}
