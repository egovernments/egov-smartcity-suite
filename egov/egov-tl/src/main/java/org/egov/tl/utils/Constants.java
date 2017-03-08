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

package org.egov.tl.utils;

import static org.egov.tl.utils.Constants.DATE_FORMAT_YYYYMMDD;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Constants {

    public static final String FILESTORE_MODULECODE = "TL";
    public static final String NEW = "new";
    public static final String VIEW = "view";
    public static final String ROLE_BILLCOLLECTOR = "Collection Operator";
    public static final String WF_STATE_COMMISSIONER_APPROVED_STR = "Commissioner approved";
    public static final String WF_STATE_GENERATE_CERTIFICATE = "Create License:generate Certificate";
    public static final String WF_STATE_SANITORY_INSPECTOR_APPROVAL_PENDING = "Sanitary inspector Approve pending";
    public static final String BUILDINGTYPE_RENTAL_AGREEMANT = "Rental";
    public static final String BUILDINGTYPE_OWN_BUILDING = "Own Building";
    public static final String NEW_LICENSE_REGISTERED = "NEW";
    public static final int PAGE_SIZE = 20;
    public static final String ZONE = "Zone";
    public static final String DIVISION = "Ward";
    public static final String FROM = "from";
    public static final String DROPDOWN_DIVISION_LIST_LICENSE = "divisionListLicense";
    public static final String DROPDOWN_DIVISION_LIST_LICENSEE = "divisionListLicensee";
    public static final String DROPDOWN_AREA_LIST_LICENSE = "areaListLicense";
    public static final String DROPDOWN_AREA_LIST_LICENSEE = "areaListLicense";
    public static final String DROPDOWN_TRADENAME_LIST = "tradeNameList";
    public static final String MESSAGE = "message";
    public static final String GENERATECERTIFICATE = "Generate Certificate";
    public static final String BUTTONAPPROVE = "Approve";
    public static final String BUTTONFORWARD = "Forward";
    public static final String BUTTONREJECT = "Reject";
    public static final String BUTTONSUBMIT = "Submit";
    public static final String BUTTONGENERATEDCERTIFICATE = "GeneratedCertificate";
    public static final String WORKFLOW_STATE_REJECTED = "Rejected";
    public static final String WORKFLOW_STATE_GENERATECERTIFICATE = "Generate Certificate";
    public static final String ACKNOWLEDGEMENT = "acknowledgement";
    public static final String LICENSE_STATUS_ACKNOWLEDGED = "Acknowledged";
    public static final String LICENSE_STATUS_ACTIVE = "Active";
    public static final String LICENSE_STATUS_OBJECTED = "Objected";
    public static final String LICENSE_STATUS_CANCELLED = "Cancelled";
    public static final String LICENSE_STATUS_UNDERWORKFLOW = "UnderWorkflow";
    public static final String STATUS_CANCELLED = "CAN";
    public static final String STATUS_ACTIVE = "ACT";
    public static final String STATUS_ACKNOLEDGED = "ACK";
    public static final String STATUS_UNDERWORKFLOW = "UWF";
    public static final String STATUS_REJECTED = "REJ";
    public static final String TRADELICENSE = "TradeLicense";
    public static final String TRADELICENSEMODULE = "TRADELICENSE";
    public static final String CANCELLED = "CANCELLED";
    public static final String TOTAL_LICENSES = "TOTAL_LICENSES";
    public static final String ZONE_ID = "ZONE_ID";
    public static final String WARD_ID = "WARD_ID";
    public static final String TOTAL_AMOUNT = "TOTAL_AMOUNT";
    public static final String OBJECTED = "OBJECTED";
    public static final String PENDING_RENEWALS = "PENDING_RENEWALS";
    public static final String RENEWED = "RENEWED";
    public static final String WARD = "WARD";
    public static final String WARD_NUM = "WARD_NUM";
    public static final String WARD_NAME = "WARD_NAME";
    public static final String NO_OF_LATE_RENEWALS = "NO_OF_LATE_RENEWALS";
    public static final String TRADE_ID = "TRADE_ID";
    public static final String TRADELICENSE_MODULENAME = "Trade License";
    public static final String ELECTRICALLICENSE_LICENSETYPE = "electricalcontractorlicense";
    public static final String TOTAL_NEW = "TOTAL_NEW";
    public static final String TOTAL_CAN = "TOTAL_CAN";
    public static final String TOTAL_OBJ = "TOTAL_OBJ";
    public static final String TOTAL_AMT = "TOTAL_AMT";
    public static final String TOTAL_PENDING = "TOTAL_PENDING";
    public static final String TOTAL_RENEWED = "TOTAL_RENEWED";
    public static final String TOTAL_ISSUED = "TOTAL_ISSUED";
    public static final String TOTAL_LATEREN = "TOTAL_LATEREN";
    public static final int AMOUNT_PRECISION_DEFAULT = 2;
    public static final String BEFORE_RENEWAL = "beforeRenew";
    public static final String ACKNOWLEDGEMENT_RENEW = "acknowledgement_renew";
    public static final BigDecimal CHQ_BOUNCE_PENALTY = BigDecimal.valueOf(1000);
    public static final String DEMANDRSN_STR_CHQ_BOUNCE_PENALTY = "CHEQUE BOUNCE PENALTY";
    public static final Character DMD_STATUS_CHEQUE_BOUNCED = 'B';
    public static final String DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY = "CHQ_BUNC_PENALTY";
    public static final String DEMANDRSN_REBATE = "REBATE";
    public static final String OWNERSHIP_TYPE_OWN = "Own";
    public static final String OWNERSHIP_TYPE_RENTED = "Rented";
    public static final String OWNERSHIP_TYPE_STATEGOVERNMENT = "State Government";
    public static final String OWNERSHIP_TYPE_CENTRALGOVERNMENT = "Central Government";
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
    public static final String APPLICATION_STATUS_DIGUPDATE_CODE = "DIGITALSIGNPENDING";
    public static final String APPLICATION_STATUS_GENECERT_CODE = "CERTIFICATEGENERATED";
    public static final String APPLICATION_STATUS_REJECTED = "REJECTED";
    public static final String APPLICATION_STATUS_CANCELLED = "CANCELLED";
    public static final String RENEWAL_LIC_APPTYPE = "Renew";
    public static final String STR_FOR_EMAILSUBJECT = "Trade License Application Amount Collected for %s %s";
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
    public static final String FILE_STORE_ID_APPLICATION_NUMBER = "fileStoreIdApplicationNumber";
    public static final String DIGITALSIGNINCLUDEINWORKFLOW = "DIGITALSIGNINCLUDEINWORKFLOW";
    public static final String SIGNED_DOCUMENT_PREFIX = "SN/";
    public static final String ROLE_COMMISSIONERDEPARTEMNT = "Health";
    public static final String PERMANENT_NATUREOFBUSINESS = "Permanent";
    public static final String TEMP_NATUREOFBUSINESS = "Temporary";
    public static final String RENEWAL_NATUREOFWORK = "Renewal of Trade License";
    public static final String NEW_NATUREOFWORK = "New Trade License";
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
    public static final String NEW_LIC_APPTYPE = "New";
    public static final String DELIMITER_COLON = "::";
    public static final String EDIT_APPROVAL_MODE = "editForApproval";
    public static final String DIGI_ENABLED_WF_SECOND_LVL_FEECOLLECTED = "Digital sign-Second level fee collected";
    public static final String WF_ACTION_DIGI_PENDING = "Digital Signature Pending";
    public static final String WF_ACTION_DIGI_SIGN_COMMISSION_NO_COLLECTION = "Digital sign-Commissioner Approved no collection";
    public static final String WF_DIGI_SIGNED = "Digital signed";
    public static final String DELIMITER_HYPEN = "-";
    public static final String REVENUE_WARD = "Ward";
    public static final String APPROVE_PAGE = "approve";
    public static final String GENERATE_CERTIFICATE = "tl_generateCertificate";
    public static final String CLOSURE_ADDITIONAL_RULE = "CLOSURELICENSE";
    public static final String CITY_GRADE_CORPORATION = "Corp";
    public static final String TL_LICENSE_ACT_CORPORATION = "Trade license Act corporation";
    public static final String TL_LICENSE_ACT_DEFAULT = "Trade license Act Muncipalities";
    public static final String CSCOPERATOR = "CSC Operator";
    public static final String PRINTACK = "printAck";
    public static final String PUBLIC_HEALTH_DEPT = "PUBLIC HEALTH AND SANITATION";
    public static final String JA_DESIGNATION = "Junior Assistant";
    public static final String RC_DESIGNATION = "Revenue Clerk";
    public static final String CLOSURE_NATUREOFTASK = "Closure License";
    public static final String COLLECTION_INDEX_NAME="receipts";
    public static final String DATE_FORMAT_YYYYMMDD = "yyyy-MM-dd";
    public static final String COLLECION_BILLING_SERVICE_TL = "Trade License";
    public static final SimpleDateFormat DATEFORMATTER_YYYY_MM_DD = new SimpleDateFormat(DATE_FORMAT_YYYYMMDD);


    private static final Map<String, String> OWNERSHIP_TYPE = new HashMap<>();

    static {

        OWNERSHIP_TYPE.put(OWNERSHIP_TYPE_OWN, OWNERSHIP_TYPE_OWN);
        OWNERSHIP_TYPE.put(OWNERSHIP_TYPE_RENTED, OWNERSHIP_TYPE_RENTED);
        OWNERSHIP_TYPE.put(OWNERSHIP_TYPE_ULB, OWNERSHIP_TYPE_ULB);
        OWNERSHIP_TYPE.put(OWNERSHIP_TYPE_STATEGOVERNMENT, OWNERSHIP_TYPE_STATEGOVERNMENT);
        OWNERSHIP_TYPE.put(OWNERSHIP_TYPE_CENTRALGOVERNMENT, OWNERSHIP_TYPE_CENTRALGOVERNMENT);

    }

    private Constants() {
        // only invariants
    }

    public static Map<String, String> getOwnershipTypes() {
        return Collections.unmodifiableMap(OWNERSHIP_TYPE);
    }
}
