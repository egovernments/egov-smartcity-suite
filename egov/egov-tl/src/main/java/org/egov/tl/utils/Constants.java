/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */

package org.egov.tl.utils;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public final class Constants {

    public static final String TL_FILE_STORE_DIR = "TL";
    public static final String NEW = "new";
    public static final String VIEW = "view";
    public static final String WF_STATE_COMMISSIONER_APPROVED_STR = "Commissioner approved";
    public static final String WF_STATE_SANITORY_INSPECTOR_APPROVAL_PENDING = "Sanitary inspector Approve pending";
    public static final String FROM = "from";
    public static final String DROPDOWN_DIVISION_LIST_LICENSE = "divisionListLicense";
    public static final String DROPDOWN_DIVISION_LIST_LICENSEE = "divisionListLicensee";
    public static final String DROPDOWN_AREA_LIST_LICENSE = "areaListLicense";
    public static final String DROPDOWN_AREA_LIST_LICENSEE = "areaListLicense";
    public static final String MESSAGE = "message";
    public static final String GENERATECERTIFICATE = "Generate Certificate";
    public static final String BUTTONAPPROVE = "Approve";
    public static final String BUTTONFORWARD = "Forward";
    public static final String BUTTONREJECT = "Reject";
    public static final String BUTTONCANCEL = "Cancel";
    public static final String BUTTONGENERATEDCERTIFICATE = "GeneratedCertificate";
    public static final String WORKFLOW_STATE_REJECTED = "Rejected";
    public static final String WORKFLOW_STATE_GENERATECERTIFICATE = "Generate Certificate";
    public static final String ACKNOWLEDGEMENT = "acknowledgement";
    public static final String LICENSE_STATUS_ACKNOWLEDGED = "Acknowledged";
    public static final String STATUS_ACKNOWLEDGED = "ACK";
    public static final String LICENSE_STATUS_ACTIVE = "Active";
    public static final String LICENSE_STATUS_CANCELLED = "Cancelled";
    public static final String LICENSE_STATUS_UNDERWORKFLOW = "UnderWorkflow";
    public static final String STATUS_CANCELLED = "CAN";
    public static final String STATUS_ACTIVE = "ACT";
    public static final String STATUS_UNDERWORKFLOW = "UWF";
    public static final String STATUS_REJECTED = "REJ";
    public static final String TRADELICENSE = "TradeLicense";
    public static final String TRADELICENSEMODULE = "TRADELICENSE";
    public static final String WARD = "WARD";
    public static final String BEFORE_RENEWAL = "beforeRenew";
    public static final String OWNERSHIP_TYPE_OWN = "OWN";
    public static final String OWNERSHIP_TYPE_OWN_VALUE = "Own";
    public static final String OWNERSHIP_TYPE_RENTED = "Rented";
    public static final String OWNERSHIP_TYPE_STATEGOVERNMENT = "State Government";
    public static final String OWNERSHIP_TYPE_STATEGOVERNMENT_KEY = "STATE_GOVERNMENT";
    public static final String OWNERSHIP_TYPE_CENTRALGOVERNMENT = "Central Government";
    public static final String OWNERSHIP_TYPE_CENTRALGOVERNMENT_KEY = "CENTRAL_GOVERNMENT";
    public static final String OWNERSHIP_TYPE_ULB = "ULB";
    public static final String LOCALITY = "locality";
    public static final String LOCATION_HIERARCHY_TYPE = "LOCATION";
    public static final String LICENSE_BILLNO_SEQ = "SEQ_BILLNO_";
    public static final String LICENSE_FEE_TYPE = "License Fee";
    public static final String APPLICATION_STATUS_CREATED_CODE = "CREATED";
    public static final String APPLICATION_STATUS_INSPE_CODE = "INSPECTIONDONE";
    public static final String APPLICATION_STATUS_APPROVED_CODE = "APPROVED";
    public static final String APPLICATION_STATUS_SECONDCOLLECTION_CODE = "SECONDLVLCOLLECTIONPENDING";
    public static final String APPLICATION_STATUS_FIRSTCOLLECTIONDONE_CODE = "FIRSTLVLCOLLECTIONDONE";
    public static final String APPLICATION_STATUS_GENECERT_CODE = "CERTIFICATEGENERATED";
    public static final String APPLICATION_STATUS_REJECTED = "REJECTED";
    public static final String APPLICATION_STATUS_CANCELLED = "CANCELLED";
    public static final String SEARCH_BY_APPNO = "ApplicationNumber";
    public static final String SEARCH_BY_LICENSENO = "LicenseNumber";
    public static final String SEARCH_BY_OLDLICENSENO = "OldLicenseNumber";
    public static final String SEARCH_BY_TRADETITLE = "TradeTitle";
    public static final String SEARCH_BY_TRADEOWNERNAME = "TradeOwnerName";
    public static final String SEARCH_BY_PROPERTYASSESSMENTNO = "PropertyAssessmentNo";
    public static final String SEARCH_BY_MOBILENO = "MobileNo";
    public static final String TL_APPROVER_ROLENAME = "TLApprover";
    public static final String TL_CREATOR_ROLENAME = "TLCreator";
    public static final String REVENUE_HIERARCHY_TYPE = "REVENUE";
    public static final String PENALTY_DMD_REASON_CODE = "Penalty";
    public static final String SIGNWORKFLOWACTION = "Sign";
    public static final String WF_PREVIEW_BUTTON = "Preview";
    public static final String PERMANENT_NATUREOFBUSINESS = "Permanent";
    public static final String TEMP_NATUREOFBUSINESS = "Temporary";
    public static final String WF_FIRST_LVL_FEECOLLECTED = "First level fee collected";
    public static final String WF_SI_APPROVED = "SI/MHO approved";
    public static final String WF_REVENUECLERK_APPROVED = "Revenue clerk approved";
    public static final String WF_LICENSE_CREATED = "License Created";
    public static final String EDIT_REJECT_MODE = "editForReject";
    public static final String ACK_MODE = "ACK";
    public static final String DISABLE_APPROVER_MODE = "disableApprover";
    public static final String PENDING_COLLECTION_MSG = "Pending for Collection";
    public static final String WF_SECOND_LVL_FEECOLLECTED = "Second level fee collected";
    public static final String WF_COMMISSIONER_APPRVD_WITHOUT_COLLECTION = "Commissioner approved no collection";
    public static final String WF_CERTIFICATE_GEN_PENDING = "Certificate generation pending";
    public static final String RENEW_ADDITIONAL_RULE = "RENEWTRADELICENSE";
    public static final String NEW_ADDITIONAL_RULE = "NEWTRADELICENSE";
    public static final String NEW_APPTYPE_CODE = "NEW";
    public static final String RENEW_APPTYPE_CODE = "RENEW";
    public static final String CLOSURE_APPTYPE_CODE = "CLOSURE";
    public static final String DELIMITER_COLON = "::";
    public static final String EDIT_APPROVAL_MODE = "editForApproval";
    public static final String DIGI_ENABLED_WF_SECOND_LVL_FEECOLLECTED = "Digital sign-Second level fee collected";
    public static final String WF_ACTION_DIGI_PENDING = "Digital Signature Pending";
    public static final String WF_ACTION_DIGI_SIGN_COMMISSION_NO_COLLECTION = "Digital sign-Commissioner Approved no collection";
    public static final String WF_DIGI_SIGNED = "Digital signed";
    public static final String REVENUE_WARD = "Ward";
    public static final String APPROVE_PAGE = "approve";
    public static final String GENERATE_CERTIFICATE = "tl_generateCertificate";
    public static final String CLOSURE_ADDITIONAL_RULE = "CLOSURELICENSE";
    public static final String CITY_GRADE_CORPORATION = "Corp";
    public static final String CSCOPERATOR = "CSC Operator";
    public static final String PRINTACK = "printAck";
    public static final String PUBLIC_HEALTH_DEPT_CODE = "PHS";
    public static final String SA_DESIGNATION_CODE = "SASST";
    public static final String JA_DESIGNATION_CODE = "JASST";
    public static final String BILL_TYPE_AUTO = "AUTO";
    public static final String TRADE_LICENSE = "Trade License";
    public static final String TL_SERVICE_CODE = "TL";
    public static final String COMMISSIONER_DESGN = "Commissioner";
    public static final String MEESEVA_RESULT_ACK = "meesevaAck";
    public static final String MEESEVAOPERATOR = "MeeSeva Operator";
    public static final String WARDSECRETARY = "WardSecretary Operator";
    public static final String NEWLICENSECOLLECTION = "NEWLICENSECOLLECTION";
    public static final String RENEWLICENSECOLLECTION = "RENEWLICENSECOLLECTION";
    public static final String NEWLICENSE = "NEWLICENSE";
    public static final String RENEWLICENSE = "RENEWLICENSE";
    public static final String CSCOPERATORRENEWLICENSE = "CSCOPERATORRENEWLICENSE";
    public static final String CSCOPERATORNEWLICENSE = "CSCOPERATORNEWLICENSE";
    public static final String NEWLICENSEREJECT = "NEWLICENSEREJECT";
    public static final String RENEWLICENSEREJECT = "RENEWLICENSEREJECT";
    public static final String GENERATE_PROVISIONAL_CERTIFICATE = "Generate Provisional Certificate";
    public static final String REPORT_PAGE = "report";
    public static final String STATUS_COLLECTIONPENDING = "COLLECTIONPENDING";
    public static final String EXTERNAL_CLOSURE_LICENSE = "EXTERNALCLOSUREAPPLICATION";
    public static final String CLOSURE_LICENSE_REJECT = "CLOSURELICENSEREJECT";
    public static final String AUTO = "Auto";
    public static final String RENEW_WITHOUT_FEE = "RENEWALWITHOUTFEE";
    public static final String COMPLETED = "Completed";
    public static final String ADMIN_WARD = "Ward";
    public static final String DISABLED_PAYMENT_MODES = "DISABLED_PAYMENT_MODES";
    public static final String LICENSE_NUMBER = "licenseNumber";
    public static final Map<String, String> OWNERSHIP_TYPE = new ImmutableMap.Builder<String, String>()
            .put(OWNERSHIP_TYPE_OWN, OWNERSHIP_TYPE_OWN_VALUE).put(OWNERSHIP_TYPE_RENTED.toUpperCase(), OWNERSHIP_TYPE_RENTED)
            .put(OWNERSHIP_TYPE_ULB, OWNERSHIP_TYPE_ULB).put(OWNERSHIP_TYPE_STATEGOVERNMENT_KEY, OWNERSHIP_TYPE_STATEGOVERNMENT)
            .put(OWNERSHIP_TYPE_CENTRALGOVERNMENT_KEY, OWNERSHIP_TYPE_CENTRALGOVERNMENT).build();

    public static final String VIEW_LINK = "%s/tl/license/show/%s";

    public static final String WARDSCRETARY_OPERATOR_ROLE = "WardSecretary Operator";
    public static final String WARDSECRETARY_TRANSACTIONID_CODE = "transactionId";
    public static final String WARDSECRETARY_WSPORTAL_REQUEST = "wsPortalRequest";
    public static final String WARDSECRETARY_SOURCE_CODE = "source";
    public static final String WARDSECRETARY_USER_NAME = "wardsecretary";
    public static final String NOTICE_TYPE_REJECTION_NOTICE = "Rejection Notice";
    public static final String MODULE_NAME = "Trade License";
    public static final String DEMAND_VOUCHER_POSTING="DEMAND_VOUCHER_POSTING";
    public static final String TLREASONCODE = "License Fee";
    public static final String TLREASONCODEPENALTY = "Penalty";
    public static final String WF_LICENSE_RENEWAL = "Renewal Initiated";
    public static final String ADMIN_HIERARCHY = "ADMINISTRATION";
    public static final String DROPDOWN_CLASSIFICATION_LIST = "classificationList";
    public static final String DROPDOWN_EMPLOYERS_LIST = "employersList";

    private Constants() {
        // only invariants
    }
}
