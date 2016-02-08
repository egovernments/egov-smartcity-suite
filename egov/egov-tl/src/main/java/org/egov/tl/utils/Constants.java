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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Constants {
        public static final String NEW = "new";
        public static final String EDIT = "edit";
        public static final String VIEW = "view";
        public static final String REQUIRED = "Required";
        public static final String UPGRADE = "upgrade";

        public static final String TRADELICENSE_TRADECODE = "TRADELICENSE_TRADECODE";
        public static final String LICENSE_NUMBER = "LICENSE_NUMBER";
        public static final String TEMP_LICENSE_NUMBER = "TLNT_LICENSE_NUMBER";
        public static final String TEMP_LICENSE_NUMBER_PREFIX = "TLNT";
        public static final String NOTICE_NUMBER = "NOTICE_NUMBER";
        public static final String SOURCE_TYPE = "SYSTEM";
        

        public static final String GENDER_MALE = "Male";
        public static final String GENDER_FEMALE = "Female";
        public static final String DROP_DOWNDATAS = "dropDownDatas";
        public static final String LOCATION_OWNER = "license";

        // Demand details
        public static final String PENALTY = "LATE RENEWAL PENALTY";
        public static final String PENALTY_CODE = "LR PENALTY";
        public static final String DEMAND_REASON_MASTER_VALUE = "Trade License Fee";
        public static final String FUND_CODE = "FUND_CODE";
        public static final char DEMANDSTATUS_ONE = '1';
        public static final int LICENSENUMBER_LENGTH = 5;
        public static final String[] monthName = { "Jan", "Feb", "Mar", "Apr",
                        "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

        // Workflow states & actions
        public static final String WF_ACTION_FEE_COLLECTED = "FEE_COLLECTED";
        public static final String WF_ACTION_LICENSE_GENERATED = "LICENSE_GENERATION";
        public static final String WF_ACTION_SAVE_DRAFT = "SAVE_DRAFT";
        public static final String WF_ACTION_SAVE_NEW = "SAVE_NEW";
        public static final String WF_ACTION_ENTER_TRADE = "ENTER_TRADE";
        public static final String WF_ACTION_APPROVE_NEW = "APPROVE_NEW";
        public static final String WF_ACTION_REJECT_NEW = "REJECT_NEW";
        public static final String WF_ACTION_RENEWAL = "RENEWAL";
        public static final String WORKFLOW_STATE_TYPE_END = "END";
        public static final String WF_STATE_Fee_Collected = "Fee Collected";
        public static final String WF_STATE_SI_Approval_Pending = "SI Approval Pending";
        public static final String WF_STATE_AHO_Approval_Pending = "AHO Approval Pending";
        public static final String WF_STATE_ARO_Approval_Pending_Health = "ARO Approval Pending Health";
        public static final String WF_STATE_AEE_Approval_Pending = "AEE Approval Pending";
        public static final String WF_STATE_ARO_Approval_Pending_Town_Planning = "ARO Approval Pending Town-Planning";
        public static final String WF_STATE_ZO_Approval_Pending = "ZO Approval Pending";
        public static final String WF_STATE_COLLECTION_PENDING = "Create License:Commissioner Approved";
        public static final String WF_STATE_INSPECTION_PENDING="Create License:Sanitary inspector Approve pending";
        public static final String WF_STATE_GENERATE_CERTIFICATE="Create License:generate Certificate";
        public static final String WF_STATE_LICENSE_NOT_APPROVED = "License Not Approved";
        public static final String WF_STATE_RENEWAL_PRINT_PENDING = "Renewal Print Pending";
        public static final String WF_STATE_SANITORY_INSPECTOR_APPROVAL_PENDING = "Sanitary inspector Approve pending";

        // JSP Pages
        public static final String VIEW_RESULT_PAGE = "result";
        public static final String VIEW_LICENSE = "view";
        public static final String VIEW_ACK_NOTICE = "ack";
        public static final String Trade_Details_ACK = "tradeDetailsAck";
        public static final String SEARCH_PAGE = "search";
        public static final String SEARCH_ADVANCED_PAGE = "searchAdvanced";
        public static final String APPROVAL_PAGE = "approval";
        public static final String EDIT_APPROVAL_PAGE = "editApproval";
        public static final String NEW_REJECTED_PAGE = "newRejected";
        public static final String SUBMIT_DRAFT_PAGE = "submitDraft";
        public static final String CANCEL_LICENSE_PAGE = "cancelLicense";
        public static final String CANCEL_Result_MSG_PAGE = "cancelResultMsg";
        public static final String RECORD_OBJECTION_PAGE = "recordObjection";
        public static final String LICENSE_OBJECTION_NOTICE_PAGE = "objectionNotice";
        public static final String REVOKE_LICENSE_PAGE = "revokeLicense";
        public static final String REPRINT_LICENSE_PAGE = "reprintLicense";
        public static final String NAME_TRANSFER_PAGE = "nametransfer";
        public static final String COLLECT_TAX_PAGE = "collecttax";
        public static final String GENERATE_LICENSE_PAGE = "generatelicense";
        public static final String DEACTIVATE_TRADE_PAGE = "deactivateTrade";
        public static final String RENEWAL_PAGE = "renewal";
        public static final String PRINTLICENSE_FAILED_PAGE = "failed";

        // validator constants
        public static final String numericiwithdot = "[0-9]+[.]*[0-9]*";
        public static final String alphabetsWithSpaceAndDot = "[A-Za-z]|[A-Za-z][A-Za-z. ]+";
        public static final String PINCODE = "[0-9]{6}";

        public static final String REASON_DUPLICATE_LICENSE = "duplicateLicense";
        public static final String MODIFIED_TRADE_MODE = "ModifiedTrade";
        public static final String CONSERVANCYFEE_BASEDON_FLAT = "Flat";
        public static final String CONSERVANCYFEE_BASEDON_PERCENTAGE = "Percentage";
        public static final String BUILDINGTYPE_RENTAL_AGREEMANT = "Rental";
        public static final String BUILDINGTYPE_OWN_BUILDING = "Own Building";
        public static final String LICENSE_APP_TYPE_NEW = "NEW";
        public static final String LICENSE_APP_TYPE_RENEW = "RENEWAL";
        public static final String NEW_LICENSE_REGISTERED = "NEW";
        public static final String FEE_BASED_ON_AREA = "Area";
        public static final String FEE_BASED_ON_WEIGHT = "Weight";
        public static final String CREATE_ACTION = "create";

        // user designations
        public static final String DESIGNATION_NAME_FOR_ASSISTANT = "ASSISTANT";
        public static final String DESIGNATION_NAME_FOR_LI = "LICENCE INSPECTOR";
        public static final String DESIGNATION_NAME_FOR_SI = "SANITARY INSPECTOR";
        public static final String DESIGNATION_NAME_FOR_AHO = "ASSISTANT HEALTH OFFICER";
        public static final String DESIGNATION_NAME_FOR_ARO = "ARO";
        public static final String DESIGNATION_NAME_FOR_AEE = "ASSISTANT EXECUTIVE ENGINEER";
        public static final String DESIGNATION_NAME_FOR_ZO = "Zonal Officer";

        // Search & Reports
        public static final String REPORTS_MODULE = "reportsmodule";
        public static final int PAGE_SIZE = 20;
        public static final String PARAM_PAGE = "page";
        public static final String ZONE_WISE_REPORT = "zoneWiseReport";
        public static final String WARD_WISE_REPORT = "wardWiseReport";
        public static final String LATE_RENEWALS_REPORT = "lateRenewals";
        public static final String TRADE_WISE_REPORT = "tradeWiseReport";
        public static final String ZONE = "Zone";
        public static final String DIVISION = "Ward";
        public static final String CITY = "City";
        public static final String CITY_WISE_REPORT = "cityWiseReport";

        // Trade Departments
        public static final String HEALTH = "Health";
        public static final String TOWN_PLANNING = "Town-Planning";
        public static final String HEALTH_AND_TOWN_PLANNING = "Health and Town-Planning";

        // Length
        public static final int TEXT_AREA_MAX_LENGTH = 1024;
        public static final int TEXT_FIELD_MAX_LENGTH = 256;
        public static final int TEXT_FIELD_ORDER_REFER_LENGTH = 40;

        // Genereal
        public static final String NO = "N";
        public static final char YES = 'Y';
        public static final String DATE_FORMAT = "dd/MM/yyyy";
        public static final String DEFAULT_SELECT = "-1";
        public static final String EMPTY_STRING = "";
        public static final String BACKSLASH = "/";

        // Area,Weight License fee master constants
        public static final String SLAB_FROM_AREA = "fromarea";
        public static final String SLAB_TO_AREA = "toarea";
        public static final String SLAB_FROM_WEIGHT = "fromweight";
        public static final String SLAB_TO_WEIGHT = "toweight";
        public static final String FROM = "from";
        public static final String TO = "to";
        public static final String MOTOR_HP = "motorhorsepower";
        public static final String NO_OF_MACHINES = "noofmachines";
        public static final String LIC_FEE = "licfee";

        // Constants for Drop Down data
        public static final String DROPDOWN_SCHEDULE_NO_LIST = "schedulenoList";
        public static final String DROPDOWN_DIVISION_LIST_LICENSE = "divisionListLicense";
        public static final String DROPDOWN_DIVISION_LIST_LICENSEE = "divisionListLicensee";
        public static final String DROPDOWN_PRIMARY_TRADE_LIST = "primaryTradeList";
        public static final String DROPDOWN_AREA_LIST_LICENSE = "areaListLicense";
        public static final String DROPDOWN_AREA_LIST_LICENSEE = "areaListLicense";
        public static final String DROPDOWN_LOCATION_LIST = "locationList";
        public static final String DROPDOWN_STREET_LIST = "streetList";
        public static final String DROPDOWN_ZONE_LIST = "zoneList";
        public static final String DROPDOWN_TRADENAME_LIST = "tradeNameList";
        public static final String DROPDOWN_NOTICE_LIST = "noticetypelist";
        public static final String DROPDOWN_PRENOTICE = "LicenseObjection_PreNotice";
        public static final String DROPDOWN_SCNOTICE = "LicenseObjection_SCNotice";
        public static final String DROPDOWN_SUSNOTICE = "LicenseObjection_suspend";
        public static final String DROPDOWN_CANNOTICE = "LicenseObjection_cancelled";

        // Constants for Related entities
        public static final String RELATED_ENTITY_ZONE = "zone";
        public static final String RELATED_ENTITY_DIVISION = "division";
        public static final String RELATED_ENTITY_AREA = "area";
        public static final String RELATED_ENTITY_LOCATION = "location";
        public static final String RELATED_ENTITY_STREET = "street";
        public static final String RELATED_ENTITY_TRADE_SUB_CATEGORY = "tradeSubCategoryMaster";

        // For Renewal
        public static final String RENEWAL_NOTICE_TYPE = "RENEWAL";

        // All the queries
        public static final String CHECK_FOR_EXISTING_PFAFEE_TYPE = "CHECK_FOR_EXISTING_PFAFEE_TYPE";
        public static final String GET_ALL_LICENSE_APP_TYPE = "GET_ALL_LICENSE_APP_TYPE";
        public static final String GET_ALL_TRADE_SUBCATEGORY = "GET_ALL_TRADE_SUBCATEGORY";
        public static final String GET_ALL_SCHEDULE_LIST = "GET_ALL_SCHEDULE_LIST";
        public static final String GET_AREA_LICENSEFEE_SLAB_WITH_EFFECTIVETO = "GET_AREA_LICENSEFEE_SLAB_WITH_EFFECTIVETO";
        public static final String GET_AREA_LICENSEFEE_WITH_FROM_TO_AND_SUBCATEGORY = "GET_AREA_LICENSEFEE_WITH_FROM_TO_AND_SUBCATEGORY";
        public static final String GET_AREA_LICENSEFEE_WITH_SLABRANGE_MATCHES_ID = "GET_AREA_LICENSEFEE_WITH_SLABRANGE_MATCHES_ID";
        public static final String GET_TRADESUBCATEGORY_LIST_BY_SCHEDULE_AND_FEEBASEDON = "GET_TRADESUBCATEGORY_LIST_BY_SCHEDULE_AND_FEEBASEDON";
        public static final String GET_ALL_CONSERVANCY_FEE = "GET_ALL_CONSERVANCY_FEE";
        public static final String GET_ALL_MOTOR_ERECTION_SLAB = "GET_ALL_MOTOR_ERECTION_SLAB";
        public static final String GET_ERECTIONFEE_WITH_SLABRANGE_MATCHES = "GET_ERECTIONFEE_WITH_SLABRANGE_MATCHES";
        public static final String CHECK_FOR_EXISTING_SCHEDULE_CODE = "CHECK_FOR_EXISTING_SCHEDULE_CODE";
        public static final String CHECK_FOR_EXISTING_TRADE_CATEGORY = "CHECK_FOR_EXISTING_TRADE_CATEGORY";
        public static final String CHECK_FOR_EXISTING_TRADE_NATURE = "CHECK_FOR_EXISTING_TRADE_NATURE";
        public static final String GET_ALL_TRADE_NATURE_LIST = "GET_ALL_TRADE_NATURE_LIST";
        public static final String GET_ALL_TRADE_CATEGORY_LIST = "GET_ALL_TRADE_CATEGORY_LIST";
        public static final String CHECK_FOR_EXISTING_TRADE_NUMBER = "CHECK_FOR_EXISTING_TRADE_NUMBER";
        public static final String CHECK_FOR_EXISTING_TRADE_NAME = "CHECK_FOR_EXISTING_TRADE_NAME";
        public static final String GET_WEIGHT_LICENSEFEE_SLAB_WITH_EFFECTIVETO = "GET_WEIGHT_LICENSEFEE_SLAB_WITH_EFFECTIVETO";
        public static final String GET_WEIGHT_LICENSEFEE_WITH_FROM_TO_AND_SUBCATEGORY = "GET_WEIGHT_LICENSEFEE_WITH_FROM_TO_AND_SUBCATEGORY";
        public static final String GET_WEIGHT_LICENSEFEE_WITH_SLABRANGE_MATCHES_ID = "GET_WEIGHT_LICENSEFEE_WITH_SLABRANGE_MATCHES_ID";
        public static final String GET_TRADESUBCATEGORY_LIST_BY_SCHEDULE = "GET_TRADESUBCATEGORY_LIST_BY_SCHEDULE";
        public static final String GET_INSTALLED_MOTORS_BY_TRADE = "GET_INSTALLED_MOTORS_BY_TRADE";
        public static final String GET_OTHER_TRADESUBCATEGORIES_BY_TRADE = "GET_OTHER_TRADESUBCATEGORIES_BY_TRADE";
        public static final String GET_INSTALLED_MOTORS_BY_TRADE_HISTORY = "GET_INSTALLED_MOTORS_BY_TRADE_HISTORY";
        public static final String GET_OTHER_TRADESUBCATEGORIES_BY_TRADE_HISTORY = "GET_OTHER_TRADESUBCATEGORIES_BY_TRADE_HISTORY";
        public static final String GET_ERECTION_FEE_BY_HORSEPOWER = "GET_ERECTION_FEE_BY_HORSEPOWER";
        public static final String GET_WEIGHT_LICENSEFEE = "GET_WEIGHT_LICENSEFEE";
        public static final String GET_AREA_LICENSEFEE = "GET_AREA_LICENSEFEE";
        public static final String GET_WEIGHT_LICENSEFEE_BY_SUBCATEGORYID = "GET_WEIGHT_LICENSEFEE_BY_SUBCATEGORYID";
        public static final String GET_AREA_LICENSEFEE_BY_SUBCATEGORYID = "GET_AREA_LICENSEFEE";
        public static final String GET_CONSERVANCY_FEE_BY_APPTYPE_AND_SUBCAT = "GET_CONSERVANCY_FEE_BY_APPTYPE_AND_SUBCAT";
        public static final String GET_PFA_FEE_BY_APPTYPE = "GET_PFA_FEE_BY_APPTYPE";
        public static final String CHECK_FOR_EXISTING_APP_TYPE = "CHECK_FOR_EXISTING_APP_TYPE";
        public static final String GET_ALL_TRADE_SUBCATEGORY_BY_FEEBASEDON = "GET_ALL_TRADE_SUBCATEGORY_BY_FEEBASEDON";
        public static final String GET_INSTALLMENT_BY_ID = "GET_INSTALLMENT_BY_ID";
        public static final String GET_INSTALLMENT_BY_YEAR_MODULE = "GET_INSTALLMENT_BY_YEAR_MODULE";
        public static final String FIND_INSTALLMENT_BY_YEAR_MODULE = "FIND_INSTALLMENT_BY_YEAR_MODULE";
        public static final String CHECK_FOR_EXISTING_LICENSE_NUMBER = "CHECK_FOR_EXISTING_LICENSE_NUMBER";
        public static final String GET_TRADE_SUB_CATEGORY_BY_ID = "GET_TRADE_SUB_CATEGORY_BY_ID";
        public static final String GET_USER_BY_ID = "GET_USER_BY_ID";
        public static final String GET_MODULE_BY_MODULE_NAME = "GET_MODULE_BY_MODULE_NAME";
        public static final String GET_DEMAND_REASON_MASTER_BY_MODULE_NAME = "GET_DEMAND_REASON_MASTER_BY_MODULE_NAME";
        public static final String GET_DEMAND_REASON_BY_INSTALLMENT_AND_DEMAND_REASON_MASTER = "GET_DEMAND_REASON_BY_INSTALLMENT_AND_DEMAND_REASON_MASTER";
        public static final String GET_TRADELICENSE_DEMAND_BY_LICENSE = "GET_TRADELICENSE_DEMAND_BY_LICENSE";
        public static final String GET_LICENSEDETAILS_BY_TRADE_AND_STATUS = "GET_LICENSEDETAILS_BY_TRADE_AND_STATUS";
        public static final String QUERY_ALL_APPTYPE = "getLicenseAppType";
        public static final String QUERY_PFAFEE_BY_EFFECTIVETO = "getPfaFeeByEffectiveTo";
        public static final String GET_LICENSECANCELINFO_BY_LICENSE = "GET_LICENSECANCELINFO_BY_LICENSE";
        public static final String GET_LICENSE_REVOKE_INFO_BY_LICENSE = "GET_LICENSE_REVOKE_INFO_BY_LICENSE";
        public static final String GET_TRADE_DEPARTMENTS = "GET_TRADE_DEPARTMENTS";
        public static final String GET_LICENSE_OBJECTION_INFO_BY_LICENSE = "GET_LICENSE_OBJECTION_INFO_BY_LICENSE";
        public static final String GET_LICENSEDETAILS_BY_TRADE = "GET_LICENSEDETAILS_BY_TRADE";
        public static final String GET_AREA_LICENSEFEE_WITH_TRADESUBCATEGORYMASTER_DATE_NUll = "GET_AREA_LICENSEFEE_WITH_TRADESUBCATEGORYMASTER_DATE_NUll";
        public static final String GET_EXISTING_MOTOR_ERECTIONFEE_WITH_SLABRANGE_MATCHES = "GET_EXISTING_MOTOR_ERECTIONFEE_WITH_SLABRANGE_MATCHES";
        public static final String GET_WEIGHT_LICENSEFEE_WITH_EFFECTIVE_TO_AND_SUBCATEGORY = "GET_WEIGHT_LICENSEFEE_WITH_EFFECTIVE_TO_AND_SUBCATEGORY";
        public static final String GET_TRADELICENSE_DEMAND_BY_LICENSE_AND_HISTORY = "GET_TRADELICENSE_DEMAND_BY_LICENSE_AND_HISTORY";
        public static final String GET_INSTALLMENT_BY_MODULE = "GET_INSTALLMENT_BY_MODULE";
        public static final String GET_SCHEDULE_BY_SCHEDULECODE = "GET_SCHEDULE_BY_SCHEDULECODE";
        public static final String GET_PARENTTRADE_ID_BY_TRADE_ID = "GET_PARENTTRADE_ID_BY_TRADE_ID";
        public static final String GET_DEMAND_REASON_MASTER_BY_REASON_MASTER = "GET_DEMAND_REASON_MASTER_BY_REASON_MASTER";
        public static final String GET_DEMAND_REASON_BY_INSTALLMENT_AND_DEMAND_REASON_MASTER_ID = "GET_DEMAND_REASON_BY_INSTALLMENT_AND_DEMAND_REASON_MASTER_ID";
        public static final String GET_AREA_LICENSEFEE_BY_INSTALLMENT = "GET_AREA_LICENSEFEE_BY_INSTALLMENT";
        public static final String GET_WEIGHT_LICENSEFEE_BY_INSTALLMENT = "GET_WEIGHT_LICENSEFEE_BY_INSTALLMENT";
        public static final String GET_ERECTION_FEE_BY_HORSEPOWER_AND_INSTALLMENT = "GET_ERECTION_FEE_BY_HORSEPOWER_AND_INSTALLMENT";
        public static final String GET_CONSERVANCY_FEE_BY_APPTYPE_AND_SUBCAT_AND_INSTALLMENT = "GET_CONSERVANCY_FEE_BY_APPTYPE_AND_SUBCAT_AND_INSTALLMENT";
        public static final String GET_PFA_FEE_BY_APPTYPE_AND_INSTALLMENT = "GET_PFA_FEE_BY_APPTYPE_AND_INSTALLMENT";
        public static final String FIND_RENEWAL_BY_LICENSE_DETAIL = "FIND_RENEWAL_BY_LICENSE_DETAIL";
        public static final String GET_TRADELICENSE_DEMAND_BY_LICENSE_AND_INSTALLMENT = "GET_TRADELICENSE_DEMAND_BY_LICENSE_AND_INSTALLMENT";
        public static final String FIND_RENEWAL_COUNT_BY_ZONE = "FIND_RENEWAL_COUNT_BY_ZONE";
        public static final String FIND_RENEWAL_COUNT_BY_DIVISION = "FIND_RENEWAL_COUNT_BY_DIVISION";
        public static final String FIND_LATERENEWAL_COUNT_BY_DIVISION = "FIND_LATERENEWAL_COUNT_BY_DIVISION";
        public static final String FIND_RENEWAL_COUNT_BY_TRADE = "FIND_RENEWAL_COUNT_BY_TRADE";
        public static final String FIND_TOTAL_RENEWAL_AMOUNT_BY_ZONE = "FIND_TOTAL_RENEWAL_AMOUNT_BY_ZONE";
        public static final String FIND_TOTAL_RENEWAL_AMOUNT_BY_DIVISION = "FIND_TOTAL_RENEWAL_AMOUNT_BY_DIVISION";
        public static final String FIND_TOTAL_RENEWAL_AMOUNT_BY_TRADE = "FIND_TOTAL_RENEWAL_AMOUNT_BY_TRADE";
        public static final String FIND_TOTAL_OBJECTION_BY_ZONE = "FIND_TOTAL_OBJECTION_BY_ZONE";
        public static final String FIND_TOTAL_OBJECTION_BY_DIVISION = "FIND_TOTAL_OBJECTION_BY_DIVISION";
        public static final String FIND_TOTAL_OBJECTION_BY_TRADE = "FIND_TOTAL_OBJECTION_BY_TRADE";
        public static final int APPLICATIONNO_LENGTH = 10;
        public static final int LICENSENO_LENGTH = 10;
        public static final Integer REASON_CANCELLATION_NO_1 = 1;
        public static final String REASON_CANCELLATION_VALUE_1 = "Citizen Objection";
        public static final Integer REASON_CANCELLATION_NO_2 = 2;
        public static final String REASON_CANCELLATION_VALUE_2 = "Expiry of license";
        public static final Integer REASON_CANCELLATION_NO_3 = 3;
        public static final Integer REASON_REVOKESUSPENTION_NO_4 = 4;

        public static final String REASON_CANCELLATION_VALUE_3 = "Court Order";
        public static final String MESSAGE = "message";
        public static final String GENERATECERTIFICATE = "Generate Certificate";
        public static final String BUTTONAPPROVE = "Approve";
        
        public static final String BUTTONFORWARD = "Forward";
        public static final String BUTTONREJECT = "Reject";
        public static final String BUTTONSUBMIT = "Submit";
        public static final String BUTTONGENERATEDCERTIFICATE = "GeneratedCertificate";
        public static final String BUTTONGENERATEDREJECTCERTIFICATE = "GeneratedREJECTCertificate";
        public static final String BUTTONPRINTCOMPLETED = "PrintCompleted";
        public static final String BUTTONGENERATEDREJECTIONLETTER = "GeneratedRejectionLetter";
        public static final String BUTTONGENERATEDPN = "GeneratedPN";
        public static final String BUTTONGENERATEDSCN = "GeneratedSCN";
        public static final String WORKFLOW_STATE_NEW = "NEW";
        public static final String WORKFLOW_STATE_APPROVED = "Approved";
        public static final String WORKFLOW_STATE_REJECTED = "Rejected";
        public static final String WORKFLOW_STATE_FORWARDED = "Forwarded";
        public static final String WORKFLOW_STATE_COLLECTED = "Fee Collected";
        public static final String WORKFLOW_STATE_GENERATECERTIFICATE = "Generate Certificate";
        public static final String WORKFLOW_STATE_GENERATENOC = "Generate NOC";
        public static final String WORKFLOW_STATE_GENERATEREJECTIONLETTER = "Generate Rejection Letter";
        public static final String WORKFLOW_STATE_GENERATESUSPENSIONLETTER = "Generate Suspension Letter";
        public static final String WORKFLOW_STATE_GENERATECANCELLATIONLETTER = "Generate Cancellation Letter";
        public static final String WORKFLOW_STATE_TYPE_CREATENEWLICENSE = "Create License:";
        public static final String WORKFLOW_STATE_TYPE_RENEWLICENSE = "Renew License:";
        public static final String WORKFLOW_STATE_TYPE_MODIFYLICENSE = "Modify License:";
        public static final String WORKFLOW_STATE_TYPE_TRANSFERLICENSE = "Transfer License:";
        public static final String WORKFLOW_STATE_GENERATEREJCERTIFICATE = "Generate Rejection Certificate";
        public static final String WORKFLOW_STATE_TYPE_OBJECTLICENSE = "Object Licenses:";
        public static final String WORKFLOW_STATE_PIGENERATED = "PIDone";
        public static final String WORKFLOW_STATE_PNGENERATED = "PN Issued";
        public static final String WORKFLOW_STATE_SCNGENERATED = "SCN Issued";
        public static final String ACKNOWLEDGEMENT = "acknowledgement";
        public static final String LICENSE_STATUS_ACKNOWLEDGED = "Acknowledged";
        public static final String LICENSE_STATUS_ACTIVE = "Active";
        public static final String LICENSE_STATUS_REJECTED = "Rejected";
        public static final String LICENSE_STATUS_OBJECTED = "Objected";
        public static final String LICENSE_STATUS_SUSPENDED = "Suspended";
        public static final String LICENSE_STATUS_CANCELLED = "Cancelled";
        public static final String LICENSE_STATUS_EXPIRED = "Expired";
        public static final String LICENSE_STATUS_UNDERWORKFLOW = "UnderWorkflow";
        public static final String alphaNumericwithspecialchar = "[0-9a-zA-Z-& :,/.()@]+";

        // For Objection
        public static final String STATUS_OBJECTED = "OBJ";
        public static final String STATUS_REJECTED = "REJ";
        public static final String STATUS_SUSPENDED = "SUS";
        public static final String STATUS_CANCELLED = "CAN";
        public static final String STATUS_ACTIVE = "ACT";
        public static final String STATUS_ACKNOLEDGED = "ACK";
        public static final String STATUS_EXPIRED = "EXP";
        public static final String STATUS_UNDERWORKFLOW = "UWF";

        public static final Integer REASON_OBJECTION_NO_1 = 1;
        public static final String REASON_OBJECTION_VALUE_1 = "Citizen Complaint";
        public static final Integer REASON_OBJECTION_NO_2 = 2;
        public static final String REASON_OBJECTION_VALUE_2 = "Internal Inspection";
        public static final Integer REASON_OBJECTION_NO_3 = 3;
        public static final String REASON_OBJECTION_VALUE_3 = "Hotel Gradation";
        public static final String OBJECTIONNUMBERPREFIX = "LICENSE_OBJECTION";
        public static final String OBJECTIONNOICENUMBERPREFIX = "OBJECTION_NOTICE";

        public static final String ACTIVITY_INSPECTION = "Inspection";
        public static final String ACTIVITY_RESPONSE = "Response";

        public static final String RENEWALNOTICE = "renewalNotice";
        public static final int RENEWALTIMEPERIOD = -90;

        public static final String CNC = "CNC";
        public static final String PFA = "PFA";
        public static final String CNCCERTIFICATE = "cncCertificate";
        public static final String PFACERTIFICATE = "pfaCertificate";

        // For Checklist
        public static final String HOSPITAL_CHECKLIST1 = "Copies of Doctors, Nurses and Staff qualification details";
        public static final String HOSPITAL_CHECKLIST2 = "Current Property Tax receipt";
        public static final String HOSPITAL_CHECKLIST3 = "Agreement Deed (if rental)";
        public static final String HOSPITAL_CHECKLIST4 = "Map Sanctioned";
        public static final String HOSPITAL_CHECKLIST5 = "Neighbours NOC";
        public static final String HOSPITAL_CHECKLIST6 = "Fire NOC (NMC)";
        public static final String HOSPITAL_CHECKLIST7 = "MPCB Pollution Control";
        public static final String HOSPITAL_CHECKLIST8 = "Copy of license certificate";
        public static final String HOSPITAL_CHECKLIST9 = "Fire Fighting Instrument";

        public static final String HKR = "HKR";
        public static final String HKR_APPL = "HKR-APPL";
        public static final String HAWKERLICENSE = "HawkerLicense";
        public static final String TRADELICENSE = "TradeLicense";
        public static final String TRADELICENSEMODULE = "TRADELICENSE";
        
        public static final String HOSPITALLICENSE = "HospitalLicense";
        public static final String PWDCONTRACTORLICENSE = "PwdContractorLicense";
        public static final String ELECTRICALCONTRACTORLICENSE = "ElectricalContractorLicense";
        public static final int GRACEPERIOD = 1;

        public static final String WWS = "WWS";
        public static final String WWS_APPL = "WWS-APPL";
        public static final String WATERWORKSLICENSE = "WaterworksLicense";
        public static final String WATERWORKS = "Plumber License Fee";
        public static final String SUBCATEGORY_BY_NAME = "SUBCATEGORY_BY_NAME";

        public static final String VETERINARYLICENSE = "VeterinaryLicense";

        // Reports

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
        public static final String TRADELICENSE_LICENSETYPE = "tradelicense";
        public static final String HOSPITALLICENSE_MODULENAME = "Hospital License";
        public static final String HOSPITALLICENSE_LICENSETYPE = "hospitallicense";
        public static final String HAWKERLICENSE_MODULENAME = "Hawker License";
        public static final String HAWKERLICENSE_LICENSETYPE = "hawkerlicense";
        public static final String WATERWORKSLICENSE_MODULENAME = "Waterworks License";
        public static final String WATERWORKSLICENSE_LICENSETYPE = "waterworkslicense";
        public static final String VETLICENSE_MODULENAME = "Veterinary License";
        public static final String VETLICENSE_LICENSETYPE = "veterinarylicense";
        public static final String PWDLICENSE_MODULENAME = "PwdContractor License";
        public static final String PWDLICENSE_LICENSETYPE = "pwdcontractorlicense";
        public static final String ELECTRICALLICENSE_MODULENAME = "ElectricalContractor License";
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
        public static final BigDecimal CHQ_BOUNCE_PENALTY = BigDecimal
                        .valueOf(1000);
        public static final String DEMANDRSN_STR_CHQ_BOUNCE_PENALTY = "CHEQUE BOUNCE PENALTY";
        public static final Character DMD_STATUS_CHEQUE_BOUNCED = 'B';
        public static final Character DMD_STATUS_CHQ_BOUNCE_AMOUNT_PAID = 'P';
        public static final Character DMD_STATUS_NO_CHQ_BOUNCED = 'N';
        public static final String DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY = "CHQ_BUNC_PENALTY";
        public static final String DEMANDRSN_REBATE = "REBATE";
        public static final String DEMANDRSN_CODE_GENERAL_TAX = "GEN_TAX";

        // Hospital License
        public static final String[] TYPEOFAREA = { "Residential", "Commercial" };

        public static final String[] HOSPITAL_TYPE = { "Allopathic", "Ayush" };
        public static final String[] TYPEOFFIRM = { "Individual",
                        "Joint Stock Company", "Hindu Undivided Family",
                        "Registered Partnership Firm" };

        public static final String[] LICENSE_CATEGORY = { "Hospital", "Clinic" };

        public static final TreeMap<String, String> HOSPITAL_TYPES = new TreeMap<String, String>() {
                /**
         *
         */
                private static final long serialVersionUID = 3913747971019907907L;

                {
                        put("Allopathic", "Allopathic");
                        put("Ayush", "Ayush");
                }
        };

        // TreeMap for hospital sub type allopathic
        public static final TreeMap<String, String> HOSPITAL_SUBTYPE_ALLOPATHIC_HOSP = new TreeMap<String, String>() {
                /**
         *
         */
                private static final long serialVersionUID = -1696336859471578718L;

                {
                        put("General Hospital", "General Hospital");
                        put("Maternity  Home (Gynecology)", "Maternity  Home (Gynecology)");
                        put("Surgical Hospital", "Surgical Hospital");
                        put("Pediatric Hospital", "Pediatric Hospital");
                        put("Ophthalmic Hospital", "Ophthalmic Hospital");
                        put("Ortho", "Ortho");
                        put("Multispeciality", "Multispeciality");
                        put("Speciality", "Speciality");
                        put("Research Center", "Research Center");
                        put("Nursing home", "Nursing home");
                        put("Community Health Centre", "Community Health Centre");
                        put("Sanatorium", "Sanatorium");
                        put("Pathology Laboratory", "Pathology Laboratory");
                        put("Hematology Laboratory", "Hematology Laboratory");
                        put("Biochemistry  Laboratory", "Biochemistry  Laboratory");
                        put("Microbiology Laboratory", "Microbiology Laboratory");
                        put("Genetics Laboratory", "Genetics Laboratory");
                        put("Collection Centre", "Collection Centre");
                        put("Super Speciality Hospital", "Super Speciality Hospital");
                        put("Cancer Hospital", "Cancer Hospital");
                        put("Physical and Metaly Challenged Hospital",
                                        "Physical and Metaly Challenged Hospital");
                        put("Others", "Others");
                }
        };

        public static final TreeMap<String, String> HOSPITAL_SUBTYPE_ALLOPATHIC_CLINIC = new TreeMap<String, String>() {
                /**
         *
         */
                private static final long serialVersionUID = 3947415644930340600L;

                {
                        put("ENT (Ear, Nose And Throat)", "ENT (Ear, Nose And Throat)");
                        put("Ophthalmic", "Ophthalmic");
                        put("Dermatology", "Dermatology");
                        put("Dentist", "Dentist");
                        put("Physician", "Physician");
                        put("Pediatric", "Pediatric");
                        put("Ortho", "Ortho");
                        put("Radiologist", "Radiologist");
                        put("Pathology", "Pathology");
                        put("Medicine", "Medicine");
                        put("Gynecology And Obstetrics", "Gynecology And Obstetrics");
                        put("Single Practitioner", "Single Practitioner");
                        put("Polyclinic", "Polyclinic");
                        put("Sub-centre", "Sub-centre");
                        put("Physiotherapy Clinic", "Physiotherapy Clinic");
                        put("Occupational Therapy", "Occupational Therapy");
                        put("Infertility", "Infertility");
                        put("Day Care Centre", "Day Care Centre");
                        put("Dialysis Centre", "Dialysis Centre");
                        put("Integrated Counseling and Testing Centre( ICTC)",
                                        "Integrated Counseling and Testing Centre( ICTC)");
                        put("Wellness/Fitness Centre", "Wellness/Fitness Centre");
                        put("X Ray Centre", "X Ray Centre");
                        put("Mammography Centre", "Mammography Centre");
                        put("Bone Densitometry Centre", "Bone Densitometry Centre");
                        put("Sonography Centre", "Sonography Centre");
                        put("Color Doppler Centre", "Color Doppler Centre");
                        put("CT Scan Centre", "CT Scan Centre");
                        put("Magnetic Resonance Imaging (MRI) Centre",
                                        "Magnetic Resonance Imaging (MRI) Centre");
                        put("Positron Emission Tomography (PET) Scan Centre",
                                        "Positron Emission Tomography (PET) Scan Centre");
                        put("Electro Myo Graphy (EMG) Centre",
                                        "Electro Myo Graphy (EMG) Centre");
                        put("Others", "Others");
                }
        };

        // TreeMap for hospital sub type ayush
        public static final TreeMap<String, String> HOSPITAL_SUBTYPE_AYUSH_HOSP = new TreeMap<String, String>() {
                /**
         *
         */
                private static final long serialVersionUID = 1923352736848787384L;

                {
                        put("Ayurvedic Hospital", "Ayurvedic Hospital");
                        put("Unani Hospital", "Unani Hospital");
                        put("Homeopathic Hospital", "Homeopathic Hospital");
                        put("Yoga/Naturopathic Hospital", "Yoga/Naturopathic Hospital");
                        put("Sidha", "Sidha");
                        put("Others", "Others");
                }
        };

        // TreeMap for hospital sub type ayush Clinic
        public static final TreeMap<String, String> HOSPITAL_SUBTYPE_AYUSH_CLINIC = new TreeMap<String, String>() {
                /**
         *
         */
                private static final long serialVersionUID = 7689151937226854434L;

                {
                        put("Ayurvedic Clinic", "Ayurvedic Cinic");
                        put("Unani Clinic", "Unani Clinic");
                        put("Homeopathic Clinic", "Homeopathic Clinic");
                        put("Naturopathic Clinic", "Naturopathic Clinic");
                        put("Others", "Others");
                }
        };

        public static final String HOSPITAL_TYPE_ALLOPATHIC = "Allopathic";
        public static final String HOSPITAL_TYPE_AYUSH = "Ayush";

        public static final String HOSPITAL_CATEGORY_HOSP = "Hospital";
        public static final String HOSPITAL_CATEGORY_CLINIC = "Clinic";

        public static final String DROPDOWN_LIST_HOSPITAL_SUBTYPE = "hospitalSubTypeList";

        // For PwdContractorLicense Check list
        public static final String PWDCONTRACTOR_CHECKLIST1 = "Sales Tax/Tin No";
        public static final String PWDCONTRACTOR_CHECKLIST2 = "Financial Statement / IT Certificate";
        public static final String PWDCONTRACTOR_CHECKLIST3 = "solvency certificate";
        public static final String PWDCONTRACTOR_CHECKLIST4 = "professional tax certificate";
        public static final String PWDCONTRACTOR_CHECKLIST5 = "provident fund certificate";
        public static final String PWDCONTRACTOR_CHECKLIST6 = "undertaking certificate";
        public static final String PWDCONTRACTOR_CHECKLIST7 = "Application copies / in 2,3";
        public static final String PWDCONTRACTOR_CHECKLIST8 = "partnership deed/ownership deed(if Applicable)";
        public static final String PWDCONTRACTOR_CHECKLIST9 = "Ownership Certificate";
        public static final String PWDCONTRACTOR_CHECKLIST10 = "List Of Works Which Are Done and Progress";
        public static final String PWDCONTRACTOR_CHECKLIST11 = "List Of Machine Required For Work";
        public static final String PWDCONTRACTOR_CHECKLIST12 = "List of technical staff with proper doc’s";
        public static final String PWDCONTRACTOR_CHECKLIST13 = "Attested copy of PTR/PTE";
        public static final String PWDCONTRACTOR_CHECKLIST14 = "electric license(if applicable)";
        public static final String PWDCONTRACTOR_CHECKLIST15 = "Oath for not coming In Black List";
        public static final String PWDCONTRACTOR_CHECKLIST16 = "2 passport size photos";
        public static final String PWDCONTRACTOR_CHECKLIST17 = "Other";
        public static final String PWDCONTRACTOR_CHECKLIST18 = "Old Certificate";

        public static final String DEMAND_REASON_REGN_FEE = "Registration Fee";
        public static final String WORKFLOW_REG_FEE_STATE_COLLECTED = "Registration Fee Collected";

        public static final String[] HOTELGRADE = { "Grade A", "Grade B", "Grade C" };

        public static final String GLCODE_FOR_TAXREBATE = "1401111";

        public static final String POLE_SUPPLIER_CONTRACTORS = "SCT";
        public static final String PUMP_MAINTENANCE = "PMT";
        public static final String CONSULTANTS = "CON";
        public static final String BRAND_OWNER = "BRL";
        public static final String DISTRIBUTOR = "DBT";
        public static final String LIFT_CONTRACTORS = "LCT";
        public static final String CONTRACTORS = "CTR";
        public static final String MAINTENANCE_CONTRACTORS = "MTN";
        public static final String BOT_CONTRACTORS = "BOT";
        public static final String FIRE_CONTRACTORS = "FRC";

        // PWD Contractor License
        public static final String[] TYPEOFFIRMPWD = {
                        "Individual/Sole Proprietor", "Joint Stock Company",
                        "Hindu Undivided Family", "Registered Partnership Firm",
                        "Limited Company", "UnEmployeed Engineer", "Partnership" };
        public static final String CLASS8 = "8";
        public static final String CLASS9 = "9";
        public static final String MAX_AMOUNT_WORKS_ESTIMATE = "10000";

        public static final BigDecimal LAKH = BigDecimal.valueOf(100000);

        public static final String SESSIONLOGINID = "com.egov.user.LoginUserId";

        // Works Integration
        public static final String WORKS_KEY_CODE = "code";
        public static final String WORKS_KEY_DEPT_CODE = "deptCode";
        public static final String WORKS_KEY_NAME = "name";
        public static final String WORKS_KEY_CORRES_ADDR = "correspondeneceAddress";
        public static final String WORKS_KEY_PAYMENT_ADDR = "paymentAddress";
        public static final String WORKS_KEY_CONTACT_PERSON = "contactPerson";
        public static final String WORKS_KEY_EMAIL = "email";
        public static final String WORKS_KEY_NARRATION = "narration";
        public static final String WORKS_KEY_PAN_NUMBER = "panNumber";
        public static final String WORKS_KEY_TIN_NUMBER = "tinNumber";
        public static final String WORKS_KEY_BANK_CODE = "bankCode";
        public static final String WORKS_KEY_IFSC_CODE = "ifscCode";
        public static final String WORKS_KEY_BANK_ACCOUNT = "bankAccount";
        public static final String WORKS_KEY_APPROVAL_CODE = "pwdApprovalCode";
        public static final String WORKS_KEY_REG_NUM = "registrationNumber";
        public static final String WORKS_KEY_STATUS = "status";
        public static final String WORKS_KEY_CLASS = "class";
        public static final String WORKS_KEY_STARTDATE = "startDate";
        public static final String WORKS_KEY_END_DATE = "endDate";
        public static final String WORKS_KEY_CONTRACTOR_UPDATE_TYPE = "contractorUpdateType";

        public static final String PWD_DEPT_CODE = "PW";
        public static final String ELECTRICAL_DEPT_CODE = "Elec";
        public static final String WORKS_UPDATE_TYPE_RENEW = "Renewal";
        public static final String LICENSE_STATUS_INACTIVE = "Inactive";
        public static final String LICENSE_STATUS_UPDATE = "Update";
        public static final String LICENSE_UPGRADE = "Upgrade";
        public static final String CITY_NAME = "Nagpur Municipal Corporation";
        public static final String MODIFYENTER = "MODIFYENTER";
        public static final String DOCUMENTSIZE = "0";
        public static final String TL_PROVISIONAL_NOC_NUMBER = "TL_PRO_NOC_NUMBER";
        public static final String ENTER_LICENSE = "Enter License";
        // Hawker License
        public static final TreeMap<String, String> HAWKER_TYPE = new TreeMap<String, String>() {
                /**
         *
         */
                private static final long serialVersionUID = 6964943974528285503L;

                {
                        put("Moving", "Moving");
                        put("Stationary", "Stationary");
                }
        };
        public static final String CITIZENUSER = "citizenUser";

        public static final String VIOLATION_FEE_DEMAND_REASON = "Violation Fee";

        public static final String GLCODE_VIOLATIONFEE = "1402001";

        public static final String TRANSACTIONTYPE_CREATE_LICENSE = "Create License";

        public static final String OWNERSHIP_TYPE_OWN = "Own";
        public static final String OWNERSHIP_TYPE_RENTED = "Rented";
        public static final String OWNERSHIP_TYPE_STATEGOVERNMENT = "State Government";
        public static final String OWNERSHIP_TYPE_CENTRALGOVERNMENT = "Central Government";
        public static final String OWNERSHIP_TYPE_ULB = "ULB";

        // OwnerShip Type
        public static final Map<String, String> OWNERSHIP_TYPE = new HashMap<String, String>() {

                {
                        put(OWNERSHIP_TYPE_OWN, OWNERSHIP_TYPE_OWN);
                        put(OWNERSHIP_TYPE_RENTED, OWNERSHIP_TYPE_RENTED);
                        put(OWNERSHIP_TYPE_ULB, OWNERSHIP_TYPE_ULB);
                        put(OWNERSHIP_TYPE_STATEGOVERNMENT, OWNERSHIP_TYPE_STATEGOVERNMENT);
                        put(OWNERSHIP_TYPE_CENTRALGOVERNMENT,
                                        OWNERSHIP_TYPE_CENTRALGOVERNMENT);

                }
        };

        public static final String LOCALITY = "locality";
        public static final String LOCATION_HIERARCHY_TYPE = "LOCATION";
        public static final String LICENSE_BILLNO_SEQ = "SEQ_BILLNO_";
        
        public static final String LICENSE_FEE_TYPE="License Fee";
        
        public static final String APPLICATION_STATUS_CREATED_CODE = "CREATED";
        public static final String APPLICATION_STATUS_INSPE_CODE = "INSPECTIONDONE";
        public static final String APPLICATION_STATUS_APPROVED_CODE = "APPROVED";
        public static final String APPLICATION_STATUS_COLLECTION_CODE = "COLLECTIONAMOUNTPAID";
        public static final String APPLICATION_STATUS_DIGUPDATE_CODE = "DIGITALSIGNUPDATED";
        public static final String APPLICATION_STATUS_GENECERT_CODE = "CERTIFICATEGENERATED";
        public static final String STR_WITH_APPLICANT_NAME = " Dear ";
        
        public static final String STR_WITH_LICENCE_NUMBER=" Trade License with TIN No.";
        public static final String STR_FOR_SUBMISSION = "/- and the Amount is Collected  @ Rs. ";
        public static final String STR_FOR_SUBMISSION_DATE="/- per year w.e.f ";
        public static final String STR_FOR_CITYMSG=". \nThanks,\n";
        public static final String REVENUE_HIERARCHYTYPE = "REVENUE";
        public static final String STR_FOR_EMAILSUBJECT="Trade License application Amount Collected for TIN No.";
        
        public static final String SEARCH_BY_APPNO = "ApplicationNumber";
        public static final String SEARCH_BY_LICENSENO = "LicenseNumber";
        public static final String SEARCH_BY_OLDLICENSENO = "OldLicenseNumber";
        public static final String SEARCH_BY_TRADETITLE = "TradeTitle";
        public static final String SEARCH_BY_TRADEOWNERNAME = "TradeOwnerName";
        public static final String SEARCH_BY_PROPERTYASSESSMENTNO = "PropertyAssessmentNo";
        public static final String SEARCH_BY_MOBILENO = "MobileNo";
        
        public static final String TL_APPROVER_ROLENAME = "TLApprover";
        public static final String TL_CREATOR_ROLENAME = "TLCreator";
}
