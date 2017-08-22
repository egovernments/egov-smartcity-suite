/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.api.controller.core;

public class ApiUrl {

	public final static String API_V1_0 = "/api/v1.0";

	/**
	 * Complaint
	 */
	public final static String COMPLAINT_GET_TYPES = "/complaint/getAllTypes";
	
	public final static String COMPLAINT_TYPES_BY_CATEGORIES = "/complaint/getComplaintCategories";

	public final static String COMPLAINT_GET_FREQUENTLY_FILED_TYPES = "/complaint/getFrequentlyFiledTypes";

	public final static String COMPLAINT_CREATE = "/complaint/create";

	public final static String COMPLAINT_UPLOAD_SUPPORT_DOCUMENT = "/complaint/{complaintNo}/uploadSupportDocument";
	
	public final static String COMPLAINT_DOWNLOAD_SUPPORT_DOCUMENT = "/complaint/{complaintNo}/downloadSupportDocument";
	
	public final static String COMPLAINT_DOWNLOAD_SUPPORT_DOCUMENT_BY_ID = "/complaint/downloadfile/{fileStoreId}";
	
	public final static String COMPLAINT_GET_LOCATION = "/complaint/getLocation";

	public final static String COMPLAINT_LATEST = "/complaint/latest/{page}/{pageSize}";

	public final static String COMPLAINT_NEARBY = "/complaint/nearby/{page}/{pageSize}";

	public final static String COMPLAINT_SEARCH = "/complaint/search";

	public final static String COMPLAINT_DETAIL = "/complaint/{complaintNo}/detail";

	public final static String COMPLAINT_STATUS = "/complaint/{complaintNo}/status";
	
	public final static String COMPLAINT_UPDATE_STATUS = "/complaint/{complaintNo}/updateStatus";
	
	public final static String COMPLAINT_HISTORY = "/complaint/{complaintNo}/complaintHistory";
	
	public final static String COMPLAINT_RESOLVED_UNRESOLVED_COUNT = "/complaint/count";
	
	/*
	 * User log
	 */
	public final static String USER_DEVICE_LOG = "/addDeviceLog";


	/**
	 * Citizen
	 */
	public final static String CITIZEN_REGISTER = "/createCitizen";

	public final static String CITIZEN_ACTIVATE = "/activateCitizen";

	public final static String CITIZEN_LOGIN = "/citizen/login";

	public final static String CITIZEN_PASSWORD_RECOVER = "/recoverPassword";

	public final static String CITIZEN_GET_PROFILE = "/citizen/getProfile";

	public final static String CITIZEN_UPDATE_PROFILE = "/citizen/updateProfile";

	public final static String CITIZEN_LOGOUT = "/citizen/logout";

	public final static String CITIZEN_GET_MY_COMPLAINT = "/citizen/getMyComplaint/{page}/{pageSize}";
	
	public final static String CITIZEN_COMPLAINT_COUNT = "/citizen/getMyComplaint/count";
	
	public final static String CITIZEN_SEND_OTP = "/sendOTP";
		
	/*
	 * Employee
	 */
	public final static String EMPLOYEE_INBOX_LIST_WFT_COUNT = "/employee/inbox";
	
	public final static String EMPLOYEE_INBOX_LIST_FILTER_BY_WFT = "/employee/inbox/{workFlowType}/{resultsFrom}/{resultsTo}";
	
	public final static String EMPLOYEE_COMPLAINT_ACTIONS = "/employee/complaint/{complaintNo}/complaintActions";
	
	public final static String EMPLOYEE_FORWARD_DEPT_DESIGNATION_USER = "/employee/forwardDetails";
	
	public final static String EMPLOYEE_UPDATE_COMPLAINT = "/employee/complaint/update/{complaintNo}";
	
	public final static String EMPLOYEE_SEARCH_INBOX = "/employee/inbox/search/{pageno}/{limit}";
	
	public final static String EMPLOYEE_LOGOUT = "/employee/logout";

	public final static String EMPLOYEE_GET_ROUTED_COMPLAINT = "/employee/routedcomplaints/{page}/{pageSize}";
	public final static String EMPLOYEE_GET_ROUTED_COMPLAINT_COUNT = "/employee/routedcomplaintcount";

}