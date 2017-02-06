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
package org.egov.wtms.utils.constants;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class WaterTaxConstants {
    public static final String FILESTORE_MODULECODE = "WTMS";
    public static final String MODULE_NAME = "Water Tax Management";
    public static final String PROPERTY_MODULE_NAME = "Property Tax";
    public static final String DATAENTRYEDIT = "DATAENTRYEDIT";
    public static final String MODULETYPE = "WATERTAXAPPLICATION";
    public static final String DASH_DELIM = "-";
    public static final String APPROVED = "APPROVED";
    public static final String CREATEWORKFLOWSTATE = "CREATED";
    public static final String APPLICATION_NUMBER = "applicationNumber";
    public static final String NEWCONNECTION = "NEWCONNECTION";
    public static final String METERED = "Metered";
    public static final String CONNECTIONTYPE_METERED = "METERED";
    public static final String NON_METERED = "Non-metered";
    public static final String NON_METERED_CODE = "NON_METERED";
    public static final String EGMODULES_NAME = "Water Tax";
    public static final String EGMODULE_NAME = "Water Tax Management";
    public static final String WATERTAX_SECURITY_CHARGE = "WTAXSECURITY";
    public static final String WATERTAX_DONATION_CHARGE = "WTAXDONATION";
    public static final String WATERTAX_ROADCUTTING_CHARGE = "WTAXROADCUTTING";
    public static final String WATERTAX_SUPERVISION_CHARGE ="WTAXSUPERVISION";
    public static final String CONNECTION_FEE = "Connection fee";
    public static final String ADDNLCONNECTION = "ADDNLCONNECTION";
    public static final String CLOSINGCONNECTION = "CLOSINGCONNECTION";
    public static final String RECONNECTIONCONNECTION = "RECONNECTION";
    public static final String RECONNECTIONWITHSLASH = "Re-Connection";
    public static final String CHANGEOFUSE = "CHANGEOFUSE";
    public static final String CLOSURECONN = "Closure Of Connection";
    public static final String SMSEMAILTYPEADDITONALCONNCREATE = "additionalconncreate";
    public static final String SMSEMAILTYPEADDITONALCONNAPPROVE = "additionalconnapprove";
    public static final String SMSEMAILTYPENEWCONNAPPROVE = "newconnapprove";
    public static final String SMSEMAILTYPENEWCONNCREATE = "newconncreate";
    public static final String SMSEMAILTYPENEWCONNEXECUTION = "newconnexecutiondate";
    public static final String SMSEMAILTYPENEWCONNESTNOTICE = "newconnestnotice";
    public static final String SMSEMAILTYPEADDCONNESTNOTICE = "addconnestnotice";
    public static final String DONATIONMASTER = "DONATIONMASTER";
    public static final String REVENUE_WARD = "WARD";
    public static final String SYSTEM = "SYSTEM";
    public static final String REVENUE_HIERARCHY_TYPE = "REVENUE";
    public static final String EDITCOLLECTION = "EDITCOLLECTION";

    public static final String SMSEMAILTYPENEWCONNFEEPAID = "newconnfeepaid";
    public static final String SMSEMAILTYPEADDCONNFEEPAID = "addconnfeepaid";

    public static final String SMSEMAILTYPECHANGEOFUSECREATE = "changeofusecreate";
    public static final String SMSEMAILTYPECHANGEOFUSEAPPROVE = "changeofuseapprove";
    public static final String SMSEMAILTYPECHANGEOFUSENOTICE = "changeofusenotice";
    public static final String SMSEMAILTYPECHANGEOFUSEEXECUTION = "changeofuseexecution";
    public static final String SMSEMAILTYPECHANGEOFUSEREJECTION = "changeofuserejection";
    public static final String SMSEMAILTYPECHANGEOFUSEFEEPAID = "changeofusefeepaid";
    public static final String SMSEMAILTYPECLOSINGCONNAPPROVE = "closureofconnectionapprove";
    public static final String SMSEMAILTYPECLOSINGCONNSANCTIONED = "closureofconnectionsanctioned";
    public static final String SMSEMAILTYPERECONNECTIONAPPROVE = "reconnectionofconnectionapprove";
    public static final String SMSEMAILTYPERECONNECTIONSANCTIONED = "reconnectionofconnectionsanctioned";

    public static final String CATEGORY_BPL = "BPL";
    public static final String WATERTAX_FIELDINSPECTION_CHARGE = "WTAXFIELDINSPEC";
    // User Roles
    public static final String ROLE_CSCOPERTAOR = "CSC Operator";
    public static final String ROLE_ULBOPERATOR = "ULB Operator";
    public static final String ROLE_BILLCOLLECTOR = "Collection Operator";
    public static final String ROLE_CLERKFORADONI = "Senior Assistant,Junior Assistant";
    public static final String ROLE_APPROVERROLE = "Water Tax Approver";
    public static final String ROLE_COMMISSIONERDEPARTEMNT = "Administration";
    public static final String ROLE_SUPERUSER = "Super User";
    public static final String ROLE_CITIZEN = "Citizen";
    public static final String ROLE_OPERATOR = "Operator";
    public static final String ROLE_ADMIN = "Property Administrator";

    // Rest API constants
    public static final String CURR_DMD_STR = "CURR_DMD";
    public static final String ARR_DMD_STR = "ARR_DMD";
    public static final String CURR_COLL_STR = "CURR_COLL";
    public static final String ARR_COLL_STR = "ARR_COLL";

    public static final String CONSUMERCODE_NOT_EXIST_ERR_CODE = "WTAX100";
    public static final String WTAXDETAILS_PROPERTYID_NOT_EXIST_ERR_MSG_PREFIX = "Water Connection details with Assessment Number ";
    public static final String PROPERTYID_NOT_EXIST_ERR_CODE = "WTAX101";
    public static final String WTAXDETAILS_CONSUMER_CODE_NOT_EXIST_ERR_MSG_PREFIX = "Water Connection details with Consumer code ";
    public static final String WTAXDETAILS_NOT_EXIST_ERR_MSG_SUFFIX = " does not exist";
    public static final String OPEN = "OPEN";

    public static final String BILLTYPE_AUTO = "AUTO";
    public static final String BILLTYPE_MANUAL = "MANUAL";
    public static final String CITIZENUSER = "9999999999";
    public static final String BILLTYPE_ONLINE = "ONLINE";
    public static final String PERMENENTCLOSE = "Permanent";
    public static final String TEMPERARYCLOSE = "Temporary";
    public static final String PERMENENTCLOSECODE = "P";
    public static final String TEMPERARYCLOSECODE = "T";
    public static final String DEMANDISHISTORY = "N";

    public static final String WF_STATE_REJECTED = "Rejected";
    public static final String WFLOW_ACTION_STEP_REJECT = "Reject";
    public static final String WF_STATE_REVENUE_CLERK_APPROVAL_PENDING = "Revenenu Clerk Approval Pending";
    public static final String WF_STATE_TAP_EXECUTION_DATE = "Execute Tap";
    public static final String WF_STATE_CLERK_APPROVED = "Clerk approved";
    public static final String WF_STATE_PAYMENT_DONE_AGT_ESTIMATION = "Payment done against Estimation";
    public static final String WF_STATE_COMMISSIONER_APPROVED = "Commissioner Approved";
    public static final String WF_STATE_CLOSURE_FORWARED_APPROVER="Close forwared By Approver";
    public static final String WF_STATE_RECONN_FORWARED_APPROVER="ReConn forwared By Approver";
    public static final String WF_STATE_COLSURE_APPROVED="Close approve By Comm";
    public static final String WF_STATE_RECONN_APPROVED="Reconnection approve By Comm";
    public static final String WF_STATE_STATE_FORWARD="Application Approval Pending";
    public static final String WF_STATE_ASSISTANT_ENGINEER_APPROVED = "Asst engg approved";
    public static final String WF_STATE_WORK_ORDER_GENERETED = "Work order generated";

    public static final String APPROVEWORKFLOWACTION = "Approve";
    public static final String FORWARDWORKFLOWACTION = "Forward";
    public static final String SUBMITWORKFLOWACTION = "Submit";
    public static final String  EXECUTIVEENGINEERFORWARDED="Engineer Executive Forwarded";
    public static final String  SUPERINTENDANTENGINEERFORWARDED="Superintendent Executive Forwarded";

    public static final String  EXECUTIVEENGINEERAPPROVED="Executive Engineer Approved";
    public static final String  DIGITALSIGNATUREUPDATED="Digital Signature Updated";


    public static final String BPL_CATEGORY = "BPL";
    public static final String HIERARCHYNAME_ADMIN = "ADMINISTRATION";

    public static final String WF_STATE_BUTTON_GENERATEESTIMATE = "Generate Estimation Notice";
    public static final String WF_STATE_TAP_EXECUTION_DATE_BUTTON = "Execute Tap";

    // Application status
    public static final String APPLICATION_STATUS_CREATED = "CREATED";
    public static final String APPLICATION_STATUS_APPROVED = "APPROVED";
    public static final String APPLICATION_STATUS_VERIFIED = "VERIFIED";
    public static final String APPLICATION_STATUS_ESTIMATENOTICEGEN = "ESTIMATIONNOTICEGENERATED";
    public static final String APPLICATION_STATUS_FEEPAID = "ESTIMATIONAMOUNTPAID";
    public static final String APPLICATION_STATUS_WOGENERATED = "WORKORDERGENERATED";
    public static final String APPLICATION_STATUS_SANCTIONED = "SANCTIONED";
    public static final String APPLICATION_STATUS_CANCELLED = "CANCELLED";

    public static final String APPLICATION_STATUS_CLOSERINITIATED = "CLOSERINITIATED";
    public static final String APPLICATION_STATUS_CLOSERINPROGRESS = "CLOSERINPROGRESS";
    public static final String APPLICATION_STATUS_CLOSERAPRROVED = "CLOSERAPPROVED";
    public static final String APPLICATION_STATUS_CLOSERSANCTIONED = "CLOSERSANCTIONED";
    public static final String WORKFLOW_CLOSUREADDITIONALRULE = "CLOSECONNECTION";
    public static final String WORKFLOW_RECONNCTIONINITIATED = "RECONNECTIONINITIATED";
    public static final String APPLICATION_STATUS__RECONNCTIONINPROGRESS = "RECONNECTIONINPROGRESS";
    public static final String APPLICATION_STATUS__RECONNCTIONAPPROVED = "RECONNECTIONAPPROVED";
    public static final String APPLICATION_STATUS__RECONNCTIONSANCTIONED = "RECONNECTIONSANCTIONED";
    public static final String APPLICATION_STATUS_RECONNDIGSIGNPENDING = "RECONNDIGSIGNPENDING";
    public static final String APPLICATION_STATUS_CLOSERDIGSIGNPENDING = "CLOSUREDIGSIGNPENDING";
    public static final String APPLICATION_STATUS_DIGITALSIGNPENDING = "DIGITALSIGNATUREPENDING";
    public static final String SEARCH_MENUTREE_APPLICATIONTYPE_CLOSURE = "CLOSURECONNECTION";
    public static final String SEARCH_MENUTREE_APPLICATIONTYPE_METERED = "METERENTRY";
    public static final String SEARCH_MENUTREE_APPLICATIONTYPE_COLLECTTAX = "COLLECTTAX";
    public static final String APPLICATION_STATUS_CLOSER = "Close Connection By AE";
    public static final String APPLICATION_STATUS_RE_CONN = "ReConnection By AE";

    // appconfig key
    public static final String SENDSMSFORWATERTAX = "SENDSMSFORWATERTAX";
    public static final String WATERTAXWORKFLOWDEPARTEMENT = "DEPARTMENTFORWORKFLOW";
    public static final String CLERKDESIGNATIONFORCSCOPERATOR = "CLERKDESIGNATIONFORCSCOPERATOR";
    public static final String SENDEMAILFORWATERTAX = "SENDEMAILFORWATERTAX";
    public static final String DEPTCODEGENBILL = "DEPTCODEFORGENERATEBILL";
    public static final String SERVEICECODEGENBILL = "SERVICECODEFORGENERATEBILL";
    public static final String ESTSERVICECODEGENBILL = "ESTSERVICECODEFORGENERATEBILL";
    public static final String FUNCTIONARYCODEGENBILL = "FUNCTIONARYCODEFORGENERATEBILL";
    public static final String FUNDSOURCEGENBILL = "FUNDSOURCECODEFORGENERATEBILL";
    public static final String FUNDCODEGENBILL = "FUNDCODEFORGENERATEBILL";

    public static final String NEWCONNECTIONALLOWEDIFPTDUE = "NEWCONNECTIONALLOWEDIFPTDUE";
    public static final String MULTIPLENEWCONNECTIONFORPID = "MULTIPLENEWCONNECTIONFORPID";
    public static final String DOCUMENTREQUIREDFORBPL = "DOCUMENTREQUIREDFORBPL";
    public static final String ROLEFORNONEMPLOYEEINWATERTAX = "ROLEFORNONEMPLOYEEINWATERTAX";
    public static final String ROLESFORLOGGEDINUSER = "RolesForSearchWAterTaxConnection";
    public static final String ENABLEDIGITALSIGNATURE = "ENABLEDIGITALSIGNATURE";
    public static final String ENABLEDEMANEDBILLSCHEDULAR = "ENABLEBILLSCHEDULAR";
    public static final String APPCONFIGVALUEOFENABLED = "YES";

    // this is just another name to new connection
    public static final String PRIMARYCONNECTION = "Primary Connection";
    public static final String CONN_NAME_ADDNLCONNECTION = "Additional Connection";

    public static final String NEW_CONNECTION_MATRIX_ADDL_RULE = "NEWCONNECTION";
    public static final String FEE_COLLECTION_COMMENT = "Water connection fee collected";

    public static final String ADDRULE_FOR_NEW_CONNECTION = "NEWCONNECTION";
    public static final String ADDRULE_FOR_ADD_CONNECTION = "ADDNLCONNECTION";
    public static final String ADDRULE_FOR_CHANGE_OF_USE = "CHANGEOFUSE";
    public static final String WF_WORKORDER_BUTTON = "Generate WorkOrder";
    public static final String YEARLY = "Yearly";
    public static final String MONTHLY = "Monthly";

    public static final String WATERTAXREASONCODE = "WTAXCHARGES";
    public static final String WF_ESTIMATION_NOTICE_BUTTON = "Generate Estimation Notice";
    public static final String WF_CLOSERACKNOWLDGEENT_BUTTON = "Generate Acknowledgement";
    public static final Character DMD_STATUS_CHEQUE_BOUNCED = 'B';
    public static final String WF_RECONNECTIONACKNOWLDGEENT_BUTTON = "Generate Reconnection Ack";
    public static final String WATER_RATES_NONMETERED_PTMODULE = "Property Tax";

    public static final String CURR_DUE = "CurrentDue";
    public static final String ARR_DUE = "ArrearDue";
    public static final String ARR_INSTALFROM_STR = "ARR_INSTALL_FROM";

    public static final String WATER_CONN_BILLNO_SEQ = "SEQ_BILLNO_";

    public static final String RESIDENTIAL = "RESIDENTIAL";
    public static final String WFLOW_ACTION_STEP_THIRDPARTY_CREATED = "Created";
    public static final String NONMETEREDDEMANDREASON = "Water Charges";
    public static final String MASTERSTATUSACTIVE = "ACTIVE";
    public static final String MASTERSTATUSINACTIVE = "IN ACTIVE";
    public static final String COLLECTION_STRING_SERVICE_CODE = "WT";
    public static final String GENERATEBILL = "GENERATEBILL";
    public static final String PREVIOUS_SECOND_HALF = "Previous 2nd Half";

    public static final String DEMANDREASONANDGLCODEMAP = "DemandReasonGlcodeMap";

    public static final String PAYMENT_TYPE_PARTIALLY = "Partially";
    public static final String PAYMENT_TYPE_FULLY = "Fully";
    public static final String PAYMENT_TYPE_ADVANCE = "Advance";
    public static final String CONTENT_DISPOSITION = "Content-disposition";
    // HashMap map b/n Demand reason string and code
    public static final LinkedHashMap<String, String> NON_METERED_DMDRSN_CODE_MAP = new LinkedHashMap<String, String>() {
        private static final long serialVersionUID = -9153822216362973956L;

        {
            put(WATERTAXREASONCODE, NONMETEREDDEMANDREASON);
        }
    };
    public static final String TOTAL_AMOUNT = "amount";
    public final static String PAID_BY = "paidBy";
    public static final String DEMANDRSN_CODE_ADVANCE = "WTADVANCE";
    public static final String DEMANDRSN_REASON_ADVANCE = "Advance";
    public static final String CURRENTYEAR_FIRST_HALF = "Current 1st Half";
    public static final String CURRENTYEAR_SECOND_HALF = "Current 2nd Half";
    public static final String GLCODE_FOR_ADVANCE = "3504106";
    public static final String [] DESG_COMM = {"Commissioner"};

    public static final List<String> ORDERED_DEMAND_RSNS_LIST = Arrays.asList(WATERTAXREASONCODE,
            DEMANDRSN_CODE_ADVANCE);

    // List for GLCodes for Current Taxes

    public static final HashMap<String, Integer> DEMAND_REASON_ORDER_MAP = new HashMap<String, Integer>() {
        private static final long serialVersionUID = -376251525790947906L;

        {
            put(WATERTAXREASONCODE, 0);
            put(DEMANDRSN_CODE_ADVANCE, 1);
        }
    };
    public static final Integer MAX_ADVANCES_ALLOWED = 10;
    public static final String THIRD_PARTY_ERR_CODE_SUCCESS = "WTMS-REST-0";
    public static final String THIRD_PARTY_ERR_MSG_SUCCESS = "SUCCESS";
    public static final String MEESEVA_REDIRECT_URL = "/meeseva/generatereceipt?transactionServiceNumber=";
    public static final String USERNAME_ANONYMOUS = "anonymous";
    public static final String USERNAME_MEESEVA = "meeseva";

    public static final String PREVIEWWORKFLOWACTION = "Preview";
    public static final String SIGNWORKFLOWACTION = "Sign";
    public static final String WF_PREVIEW_BUTTON = "Preview";
    public static final String WF_SIGN_BUTTON = "Sign";

    public static final String CONNECTION_WORK_ORDER = "connectionWorkOrder";
    public static final String SIGNED_DOCUMENT_PREFIX = "SN/";

    public static final String MODE = "mode";
    public static final String APPROVAL_POSITION = "approvalPosition";
    public static final String APPROVAL_COMMENT = "approvalComment";
    public static final String WORKFLOW_ACTION = "workFlowAction";
    public static final String NOTICE_TYPE_SPECIAL_NOTICE = "Special Notice";
    public static final String DIGITAL_SIGNATURE_PENDING = "Digital Signature Pending";
    public static final String WATER_TAP_CONNECTION = "Water Tap Connection";
    public static final String FILE_STORE_ID_APPLICATION_NUMBER = "fileStoreIdApplicationNumber";
    public static final String[] CREATECONNECTIONDMDDESC = { "Water Security Charges", "Water Donation Charges",
            "Water Estimation Charges" };
    public static final String[] WATERCHARGESDMDDESC = { "Water Charges" };

    public static final String CLOSECONNECTION = "CLOSECONNECTION";
    public static final String APPLICATIONSTATUSOPEN = "Open";
    public static final String APPLICATIONSTATUSCLOSED = "Closed";
    public static final String APPLICATIONSTATUSALL = "All";

    public static final String RECONNECTION_ESTIMATION_NOTICE = "ReconnacknowlgementNotice";
    public static final String CLOSURE_ESTIMATION_NOTICE = "CloserConnectionAcknowldgemt";
    public static final String SIGN_ALL = "SIGN_ALL";
    public static final String PDFEXTENTION = ".pdf";
    public static final String APPLICATIONPDFNAME = "application/pdf";
    public static final String DOCUMENTNAMEFORBILL = "DemandBill";
    public static final Integer QUARTZ_BULKBILL_JOBS = 10;
    public static final String WATERCHARGES_CONSUMERCODE = "consumerCode";
    public static final String APPLICATION_TAX_INDEX_NAME = "applications";
    public static final String COLLECTION_INDEX_NAME = "receipts";
    public static final String WATER_TAX_INDEX_NAME = "waterchargeconsumer";
    public static final String COLLECION_BILLING_SERVICE_WTMS = "Water Tax";
    public static final BigDecimal BIGDECIMAL_100 = new BigDecimal("100");
    
    public static final String DASHBOARD_GROUPING_DISTRICTWISE = "district";
    public static final String DASHBOARD_GROUPING_ULBWISE = "ulb";
    public static final String DASHBOARD_GROUPING_REGIONWISE = "region";
    public static final String DASHBOARD_GROUPING_GRADEWISE = "grade";
    public static final String DASHBOARD_GROUPING_WARDWISE = "ward";
    public static final String DASHBOARD_GROUPING_CITYWISE = "city";
    public static final String DATE_FORMAT_YYYYMMDD = "yyyy-MM-dd";
    public static final SimpleDateFormat DATEFORMATTER_YYYY_MM_DD = new SimpleDateFormat(DATE_FORMAT_YYYYMMDD);
    public static final String WATERCHARGETOTALDEMAND = "totalDemand";
    public static final String REGIONNAMEAGGREGATIONFIELD = "regionName";
    public static final String DISTRICTNAMEAGGREGATIONFIELD = "districtName";
    public static final String CITYNAMEAGGREGATIONFIELD = "cityName";
    public static final String CITYGRADEAGGREGATIONFIELD =  "cityGrade";
    public static final String REVENUEWARDAGGREGATIONFIELD = "revenueWard";
    public static final String CITYCODEAGGREGATIONFIELD = "cityCode";
    
    public static final String RESIDENTIALCONNECTIONTYPEFORDASHBOARD = "RESIDENTIAL";
    
    
    
    public static final String COMMERCIALCONNECTIONTYPEFORDASHBOARD = "COMMERCIAL";

    //Designation
    public static final String COMMISSIONER_DESGN = "Commissioner";
    public static final String EXECUTIVE_ENGINEER_DESIGN = "Executive engineer";
    public static final String MUNICIPAL_ENGINEER_DESIGN = "Municipal Engineer";
    public static final String SUPERIENTEND_ENGINEER_DESIGN = "Superintendent Engineer";
    public static final String TAP_INSPPECTOR_DESIGN = "Tap Inspector";
    public static final String ASSISTANT_ENGINEER_DESIGN = "Assistant engineer";
    public static final String JUNIOR_OR_SENIOR_ASSISTANT_DESIGN = "Senior Assistant,Junior Assistant";

    public static final String SENIOR_ASSISTANT_DESIGN = "Senior Assistant";
    public static final String JUNIOR_ASSISTANT_DESIGN = "Junior Assistant";



}