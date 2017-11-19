/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
package org.egov.works.utils;

public class WorksConstants {

    public static final String WORKS = "Works";
    public static final String WORKS_MODULE_NAME = "Works Management";
    public static final String EGF_MODULE_NAME = "EGF";
    public static final String alphaNumericwithspecialchar = "[0-9a-zA-Z-& :,/.()@]+";
    public static final String TEMPLATENAME_COMPLETIONCERTIFICATE = "completionCertificate";
    public static final String PARAMETERNAME_WORKCOMPLETIONINFO = "workCompletionInfo";
    public static final String BILL_ACTION_VIEWCOMPLETIONCERTIFICATE = "View Completion Certificate";
    public static final String BILL_TYPE_PARTBILL = "PartBillType";
    public static final String BILL_TYPE_FINALBILL = "FinalBillType";
    public static final String YES = "yes";
    public static final String NO = "no";
    public static final String PREVIOUS_APPROPRIATION_YEAR = "previous";
    public static final String MODULETYPE = "LINEESTIMATE";
    // Pagination
    public static final int PAGE_SIZE = 30;

    // Named queries
    public static final String QUERY_GETALLMBHEADERSBYBILLID = "getAllMBHeadersbyBillId";
    public static final String QUERY_GETALLWORKORDERACTIVITYWITHMB = "getallWorkOrderActivityWithMB";
    public static final String QUERY_GETALLWORKORDERACTIVITYWITHOUTMB = "getallWorkOrderActivityWithoutMB";
    public static final String QUERY_GETALLMBNOSBYWORKORDERESTIMATE = "getAllMBNosbyWorkEstimate";
    public static final String QUERY_GETACTIVEDEPOSITCODES = "EGW_getActiveDepositCodes";
    public static final String QUERY_GETACTIVEDEPOSITCODES_BY_CODE_OR_DESC = "EGW_getActiveDepositCodesByCodeOrDescription";

    public static final String WORKORDER_LASTSTATUS = "WorkOrder.laststatus";
    public static final String WP_LAST_STATUS = "WP_LAST_STATUS";
    public static final String TENDERRESPONSE_LASTSTATUS = "TenderResponse.laststatus";
    public static final String QUERY_GETSTATUSDATEBYOBJECTID_TYPE_DESC = "getStatusDateByObjectId_Type_Desc";
    public static final String CAPITAL_WORKS = "Capital Works";
    public static final String IMPROVEMENT_WORKS = "Improvement Works";
    public static final String REPAIR_AND_MAINTENANCE = "Repairs and maintenance";
    public static final String DEPOSIT_NO_ASSET_CREATED = "Deposit Works No Asset Created";
    public static final String DEPOSIT_ASSET_CREATED = "Deposit Works Third Party Asset";
    public static final String DEPOSIT__OWN_ASSET_CREATED = "Deposit Works Own Asset";
    public static final String ASSET_LIST = "assestList";
    public static final String COA_LIST = "coaList";
    public static final String KEY_CWIP = "WORKS_CWIP_CODE";
    public static final String KEY_REPAIRS = "WORKS_REPAIRS_AND_MAINTENANCE";
    public static final String KEY_DEPOSIT = "WORKS_DEPOSIT_OTHER_WORKS";
    // For asset Capitalisation
    public static final String ASSET_STATUS_CAPITALISED = "Capitalized";
    public static final String NATUREOFWORKFORASSETCAPITALISATION = "NATUREOFWORKFORASSETCAPITALISATION";
    public static final String NATUREOFWORKFORASSETIMPROVMENT = "NATUREOFWORKFORASSETIMPROVMENT";
    public static final String NATUREOFWORKFORASSETREPAIRANDMAINTAINANCE = "NATUREOFWORKFORASSETREPAIRANDMAINTAINANCE";
    public static final String NATUREOFWORKFORASSETCAPITALISATION_DEFAULTVALUE = "Capital Works - New Asset,Deposit Works - Own Asset";
    public static final String NATUREOFWORKFORASSETIMPROVMENT_DEFAULTVALUE = "Capital Works - Improvement Works";
    public static final String NATUREOFWORKFORASSETREPAIRANDMAINTAINANCE_DEFAULTVALUE = "Repairs and maintenance Works";
    // For Deposit Work Folio
    public static final String QUERY_GETDEPOSITWORKSUSAGELISTFORDEPOSITFOLIO = "getDepositWorksUsageListForDepositFolio";

    // RebatePremium
    public static final String BILL = "BILL";

    // Tender Negotiation
    public static final String TENDER_TYPE = "TENDER_TYPE";

    // WORKPROGRESSREPORT
    public static final String WO_STATUS_WOACKNOWLEDGED = "Work Order acknowledged";
    public static final String WO_STATUS_WOCOMMENCED = "Work commenced";
    public static final String WO_STATUS_WOSITEHANDEDOVER = "Site handed over";
    public static final String MILESTONE_MODULE_KEY = "Milestone";
    public static final String TRACK_MILESTONE_MODULE_KEY = "TrackMilestone";
    public static final String DEPTWISE = "deptwise";
    public static final String WORKWISE = "workwise";

    public static final String CONDITION = "CONDITION";
    public static final String GIVEN = "given";
    public static final String STARTED = "started";
    public static final String NOTSTARTED = "notstarted";
    public static final String TOTAL0TO25 = "total0to25";
    public static final String TOTAL26TO50 = "total26to50";
    public static final String TOTAL51TO75 = "total51to75";
    public static final String TOTAL76TO100 = "total76to100";
    public static final String WOVALUE = "wovalue";
    public static final String OVERDUECONTRACTPERIOD = "overduecontractperiod";
    public static final String PAYMENTINFO = "paymentInfo";
    public static final String COMPLETED = "completed";
    public static final String SUBTYPEOFWORK = "SubTypeOfWork";
    public static final String CANCELLED = "cancelled";
    public static final String BALANCE_NO_OF_WORKS = "BalanceWorks";

    public static final String DEFAULT_PROJECTCODE_STATUS = "CREATED";

    public static final String APPROVED = "APPROVED";
    public static final String CANCELLED_STATUS = "CANCELLED";
    public static final String NEW = "NEW";
    public static final String REJECTED = "REJECTED";
    public static final String ADMIN_SANCTIONED_STATUS = "ADMIN_SANCTIONED";
    public static final String EST_PREPARED = "EST_PREPARED";
    public static final String BALANCE_EST = "BALANCE_EST";

    // DEPOSITWORKS
    public static final String WORKS_USER_ROLE = "Works User";
    public static final String ASSISTANT_DESIGNATION = "ASSISTANT";
    public static final String ESTIMATE_FORCLIAMCHARGES_FILENAME = "estimate_claimcharges";
    public static final String APPLICATIONREQUEST_MODULE_KEY = "ApplicationRequest";
    public static final String ADMIN_SANCTION_ESTIMATES = "adminSanEstimates";
    public static final String APPROVED_WORKSPACKAGES = "approvedWorksPackages";
    public static final String APPROVED_TENDERNEGOTIATIONS = "approvedTenderNegotiations";

    public static final String APPROVED_WP_ESTIMATE = "approvedWPLinkedEst";
    public static final String WO_GIVEN_ESTIMATE = "workOrderGivenLinkedEst";

    //
    public static final String CAPITAL_FUND = "Capital Fund";
    // Used in the budget heads and glcodes dropdown of work progress abstract
    // report 2
    public static final String ALL = "All";

    public static final String DEPOSIT_WORKS_THIRDPARTY_ASSET = "Deposit Works - Third Party Asset";
    public static final String DEPOSIT_WORKS_NO_ASSET_CREATED = "Deposit Works - No Asset Created";

    // Offline status for works package
    public static final String TENDER_DOCUMENT_RELEASED = "Tender document released";
    public static final String TYPE_OF_WORK_ROADS = "Roads";
    public static final String TYPE_OF_WORK_BRIDGES = "Bridges";
    public static final String TYPE_OF_WORK_ELECTRICAL = "Electrical";
    public static final String TYPE_OF_WORK_BUILDINGS = "Buildings";
    public static final String TYPE_OF_WORK_STORMWATER_DRAIN = "Storm Water Drain";

    public static final String VALID = "valid";
    public static final String INVALID = "invalid";

    // Tender Scrutinizing committee - Invitation types
    public static final String SINGLE_COVER_SYSTEM = "Single Cover System";
    public static final String TWO_COVER_SYSTEM = "Two Cover System";

    public static final String TENDER_NOTICE_RELEASED_STATUS = "Noticeinvitingtenderreleased";
    public static final String OBJECT_TYPE_WORKS_PACKAGE = "WorksPackage";

    public static final String REPAIRS_MAINTENANCE = "Repairs and Maintenance";

    // Indent Category type
    public static final String MAINTENANCE_WORKS = "Maintenance works";
    public static final String DEPOSIT_WORKS = "Deposit works";

    public static final String MAINTENANCE_INDENTRC_FUND = "MAINTENANCE_INDENTRC_FUND";
    public static final String PERC_TENDER = "Percentage-Tender";
    public static final String ITEM_RATE_TENDER = "Item-Rate";
    public static final String FINAL_BILL = "Final Bill";
    public static final String SCH_CATEGORY_BRR_NORTH = "BRR-NORTH";
    public static final String SCH_CATEGORY_BRR_SOUTH = "BRR-SOUTH";
    public static final String SCH_CATEGORY_BRR_CENTER = "BRR-CENTER";
    public static final String SOURCE_PAGE_SEARCH = "search";

    public static final String CREATED_STATUS = "CREATED";
    public static final String CHECKED_STATUS = "CHECKED";
    public static final String RESUBMITTED_STATUS = "RESUBMITTED";
    public static final String ACTION_SUBMIT_FOR_APPROVAL = "submit_for_approval";
    public static final String ACTION_APPROVAL = "approval";
    public static final String ACTION_APPROVE = "approve";
    public static final String PROJECTCODE = "PROJECTCODE";
    public static final String EDIT = "edit";
    public static final String END = "END";

    public static final String CONTRACTOR_GRADE_ALPHANUMERIC_ERR_CODE = "contractorGrade.grade.alphaNumeric";
    public static final String CONTRACTOR_GRADE_ALPHANUMERIC_ERR_MSG = "Special Characters are not allowed in Contractor Grade";
    public static final String CONTRACTOR_GRADE_MAX_AMOUNT_INVALID_ERR_CODE = "contractor.grade.maxamount.invalid";
    public static final String CONTRACTOR_GRADE_MAX_AMOUNT_INVALID_ERR_MSG = "Maximum amount must be greater than minimum amount";
    public static final String CONTRACTOR_GRADE_SAVE_SUCCESS_CODE = "contractor.grade.save.success";
    public static final String CONTRACTOR_GRADE_SAVE_SUCCESS_MSG = "contractor.grade.save.success";
    public static final String NO_DATA = "noData";

    public static final String STATUS_MODULE_NAME = "Contractor";
    public static final String EDIT_ENABLE_ROLE_NAME = "Edit Contractor Bank Info";
    public static final String BANK = "bank";
    public static final String NEGOTIATION_DATE_FORMAT_INVALID = "Negotiation date is not valid, should be in dd/MM/yyyy format";
    public static final String NEGOTIATION_DATE = "negDate";
    public static final String VIEW = "view";

    // Action drop downs in search screens
    public static final String ACTION_VIEW = "View";
    public static final String ACTION_VIEW_PDF = "View PDF";
    public static final String ACTION_WF_HISTORY = "WorkFlow History";
    public static final String ACTION_VIEW_DOCUMENT = "View Document";
    public static final String ACTION_COPY_ESTIMATE = "Copy Estimate";
    public static final String WORKS_VIEW_OFFLINE_STATUS_VALUE = "View offline status";
    public static final String WORKS_SETSTATUS_VALUE = "Set offline status";

    public static final String WP_OFFLINE_STATUS = "WP_OFFLINE_STATUS";

    public static final String GRADE = "grade";
    public static final String MIN_AMOUNT = "minAmount";
    public static final String MAX_AMOUNT = "maxAmount";

    public static final String CONTRACTOR_NAME = "contractorName";
    public static final String CONTRACTOR_CODE = "contractorCode";
    public static final String DEPARTMENT_ID = "departmentId";
    public static final String STATUS_ID = "statusId";
    public static final String GRADE_ID = "gradeId";
    public static final String SEARCH_DATE = "searchDate";

    public static final String CANCEL_ACTION = "Cancel";
    public static final String SAVE_ACTION = "Save";
    public static final String SUBMIT_ACTION = "Submit";
    public static final Object REJECT_ACTION = "Reject";
    public static final Object FORWARD_ACTION = "Forward";

    // Module types
    public static final String MODULE_NAME_LINEESTIMATE = "LineEstimate";

    // public static final String NEWLINEESTIMATE = "NEWLINEESTIMATE";

    // public static final String NEWCONTRACTORBILLREGISTER = "NEWCONTRACTORBILLREGISTER";

    // User roles
    public static final String ROLE_SUPERUSER = "SYSTEM";

    public static final String LINEESTIMATE_CREATE = "create";
    public static final String LINEESTIMATE_UPDATE = "update";

    public static final String WF_STATE_CREATED = "Created";
    public static final String WF_STATE_CHECKED = "Checked";
    public static final String WF_STATE_REJECTED = "Rejected";
    public static final String WF_STATE_CANCELLED = "Cancelled";

    public static final String FILESTORE_MODULECODE = "WMS";

    public static final String ABSTRACTESTIMATE = "AbstractEstimate";
    public static final String WORKORDER = "WorkOrder";
    public static final String CONTRACTORBILL = "CONTRACTORBILL";
    public static final String MBHEADER = "MBHeader";

    public static final String BOUNDARY_TYPE_WARD = "Ward";
    public static final String HIERARCHY_TYPE_ADMINISTRATION = "ADMINISTRATION";
    public static final String BOUNDARY_TYPE_CITY = "City";

    public static final String APPCONFIG_KEY_ENGINEERINCHARGE_DESIGNATION = "ENGINEERINCHARGE_DESIGNATION";

    public static final String WORKFLOWTYPE_DISPLAYNAME = "Estimate";

    public static final String WORKFLOWTYPE_CBR_DISPLAYNAME = "Contractor Bill";

    public static final String BILL_EXPENDITURE_TYPE = "Works";

    public static final String APPCONFIG_KEY_DESIGNATION_TECHSANCTION_AUTHORITY = "DESIGNATION_TECHSANCTION_AUTHORITY";

    public static final String APPCONFIG_KEY_BUDGETCHECK_REQUIRED = "budgetCheckRequired";

    public static final String DESIGNATION_COMMISSIONER = "Commissioner";

    public static final String CONTRACTOR_NETPAYABLE_PURPOSE = "Creditors-Contractor Payable";

    public static final String ACCOUNTDETAIL_TYPE_CONTRACTOR = "contractor";

    public static final String HEADING_WORK_PROGRESS_REGISTER_REPORT = "Work Progress Register Report";

    public static final String HEADING_ESTIMATE_ABSTRACT_REPORT_BY_DEPARTMENT_WISE = "Estimate Abstract Report By Department Wise";

    public static final String HEADING_ESTIMATE_ABSTRACT_REPORT_BY_TYPE_OF_WORK_WISE = "Estimate Abstract Report By Type Of Work Wise";

    public static final String HEADING_ESTIMATE_APPROPRIATION_REGISTER_REPORT = "Estimate Appropriation Register Report";

    public static final String SLUM_WORK = "SLUM_WORK";

    public static final String NON_SLUM_WORK = "NON_SLUM_WORK";

    public static final String LOCATION_BOUNDARYTYPE = "Locality";

    public static final String LOCATION_HIERARCHYTYPE = "LOCATION";

    public static final String NATUREOFWORK_EXPENDITURETYPE_CAPITAL = "CAPITAL";

    public static final String NATUREOFWORK_EXPENDITURETYPE_REVENUE = "REVENUE";

    public static final String CONTRACTOR_RETENTION_MONEY_PURPOSE = "RETENTION_MONEY";

    public static final String CONTRACTOR_SECURITY_DEPOSITE_PURPOSE = "Security Deposit";

    public static final String[] CONTRACTOR_REFUND_PURPOSE = { CONTRACTOR_RETENTION_MONEY_PURPOSE,
            CONTRACTOR_SECURITY_DEPOSITE_PURPOSE };

    public final static String SECTOR_COLUMN_HEADER_NAME = "SECTOR";

    public final static String DISTRICT_COLUMN_HEADER_NAME = "District Name";

    public final static String ULB_COLUMN_HEADER_NAME = "ULB Name";

    public final static String WORKSMILESTONE_TYPEOFWORKNAME_COLUMN_NAME = "lineestimatetypeofworkname";

    public final static String WORKSMILESTONE_DISTNAME_COLUMN_NAME = "distname";

    public final static String WORKSMILESTONE_ULBNAME_COLUMN_NAME = "ulbname";

    public final static String WORKSMILESTONE_ULBCODE_COLUMN_NAME = "ulbcode";

    public final static String WORKSMILESTONE_LOASTATUS_COLUMN_NAME = "loastatus";

    public final static String WORKSMILESTONE_MILESTONESTATUS_COLUMN_NAME = "milestonestatus";

    public final static String WORKSMILESTONE_ESTIMATEDETAILID_COLUMN_NAME = "lineestimatedetailid";

    public final static String ACTIVE = "Active";

    public static final String ALPHANUMERICWITHALLSPECIALCHAR = "[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+";
}
