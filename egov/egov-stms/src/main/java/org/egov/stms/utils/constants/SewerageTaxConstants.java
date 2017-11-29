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
package org.egov.stms.utils.constants;

import java.util.LinkedHashMap;

public class SewerageTaxConstants {

    public static final String FILESTORE_MODULECODE = "STMS";
    public static final String MODULE_NAME = "Sewerage Tax Management";
    public static final String APPL_INDEX_MODULE_NAME = "Sewerage Tax";
    public static final String APPLICATION_NUMBER = "applicationNumber";
    public static final String NEWSEWERAGECONNECTION = "NEWSEWERAGECONNECTION";
    public static final String CHANGEINCLOSETS = "CHANGEINCLOSETS";
    public static final String CHANGEINCLOSETS_NOCOLLECTION = "CHANGEINCLOSETS NOCOLLECTION";
    public static final String MODULETYPE = "SEWERAGETAXAPPLICATION";
    public static final String REVENUE_WARD = "WARD";
    public static final String CLOSESEWERAGECONNECTION = "CLOSESEWERAGECONNECTION";

    // appconfig keys
    public static final String NEWCONNECTIONALLOWEDIFPTDUE = "NEWCONNECTIONALLOWEDIFPTDUE";
    public static final String APPCONFIG_COLLECT_INSPECTIONFEE = "SEWERAGE_COLLECTINSPECTION_FEE";
    public static final String APPCONFIG_NUMBEROFMONTHS_ADVANCESEWERAGETAX = "SEWERAGE_ADVANCESEWERAGETAX_INMONTHS";
    public static final String APPCONFIG_COLLECT_LEGACY_DONATIONCHARGE = "LEGACY_DONATION_CHARGE";


    // application status constants - start
    public static final String STATUS ="status";
    public static final String APPLICATION_STATUS_CREATED = "CREATED";
    public static final String APPLICATION_STATUS_FINALAPPROVED = "FINALAPPROVED";
    public static final String APPLICATION_STATUS_ESTIMATENOTICEGEN = "ESTIMATIONNOTICEGENERATED";
    public static final String APPLICATION_STATUS_FEEPAID = "ESTIMATIONAMOUNTPAID";
    public static final String APPLICATION_STATUS_WOGENERATED = "WORKORDERGENERATED";
    public static final String APPLICATION_STATUS_CANCELLED = "CANCELLED";
    public static final String APPLICATION_STATUS_SANCTIONED = "SANCTIONED";
    public static final String APPLICATION_STATUS_INSPECTIONFEEPENDING = "INSPECTIONFEEPENDING";
    public static final String APPLICATION_STATUS_INSPECTIONFEEPAID = "INSPECTIONFEEPAID";
    public static final String APPLICATION_STATUS_INITIALAPPROVED = "INITIALAPPROVED";
    public static final String APPLICATION_STATUS_FIELDINSPECTED = "FIELDINSPECTED";
    public static final String APPLICATION_STATUS_REJECTED = "REJECTED";
    public static final String APPLICATION_STATUS_DEEAPPROVED = "DEEAPPROVED";
    public static final String APPLICATION_STATUS_COLLECTINSPECTIONFEE = "COLLECTINSPECTIONFEE";
    public static final String APPLICATION_STATUS_SANCTIONED_WITH_DEMAND_NOT_PAID = "SANCTIONEDWITHDEMAND";
    public static final String APPLICATION_STATUS_CLOSERSANCTIONED = "CLOSERSANCTIONED";
    public static final String APPLICATION_STATUS_FINAL_APPROVED = "Final Approved";
    // application status constants - end

    // workflow related - start
    public static final String COLLECTION_REMARKS = "Collection done";
    public static final String WORKFLOWTYPE_DISPLAYNAME = "Sewerage Connection";

    // current states
    public static final String WF_STATE_REJECTED = "Rejected";
    public static final String WF_CLOSE_COONNECTION_STATE_REJECTED = " Close Connection Rejected";
    public static final String WF_STATE_CLERK_APPROVED = "Clerk Approved";
    public static final String WF_STATE_DEPUTY_EXE_APPROVED = "Deputy Exe Engineer Approved";
    public static final String WF_STATE_ASSISTANT_APPROVED = "Assistant Engineer Approved";
    public static final String WF_STATE_INSPECTIONFEE_PENDING = "Inspection Fee Pending";
    public static final String WF_STATE_INSPECTIONFEE_COLLECTED = "Inspection Fee Collected";
    public static final String WF_STATE_ESTIMATIONNOTICE_GENERATED = "Estimation Notice Generated";
    public static final String WF_STATE_PAYMENTDONE = "Payment Done Against Estimation";
    public static final String WF_STATE_EE_APPROVED = "Executive Engineer Approved";
    public static final String WF_STATE_WO_GENERATED = "Work Order Generated";
    public static final String WF_STATE_CLOSECONNECTION_NOTICEGENERATION_PENDING = "Close Connection Notice Generation Pending";

    // validactions
    public static final String WFLOW_ACTION_STEP_REJECT = "Reject";
    public static final String WFLOW_ACTION_STEP_CANCEL = "Cancel";
    public static final String WFLOW_ACTION_STEP_FORWARD = "Forward";
    public static final String WF_ESTIMATION_NOTICE_BUTTON = "Generate Estimation Notice";
    public static final String WF_STATE_CONNECTION_EXECUTION_BUTTON = "Execute Connection";
    public static final String WF_CLOSERACKNOWLDGEENT_BUTTON = "Generate Acknowledgement";
    public static final String WF_WORKORDER_BUTTON = "Generate Work Order";
    public static final String WF_STATE_CONNECTION_CLOSE_BUTTON = "Generate Close Connection Notice";

    // Pendingactions
    public static final String WF_INSPECTIONFEE_COLLECTION = "Inspection Fee Collection";
    public static final String WFPA_REJECTED_INSPECTIONFEE_COLLECTION = "Rejected Inspection Fee Collection";
    // workflow related - end

    public static final String CLERKDESIGNATIONFORCSCOPERATOR = "CLERKDESIGNATIONFORCSCOPERATOR";
    public static final String SEWERAGETAXWORKFLOWDEPARTEMENT = "DEPARTMENTFORWORKFLOW";

    // designations
    public static final String DESIGNATION_DEPUTY_EXE_ENGINEER = "deputy executive engineer";
    public static final String DESIGNATION_EXE_ENGINEER = "executive engineer";
    public static final String DESIGNATION_COMMISSIONER = "Commissioner";

    // User roles
    public static final String ROLE_SUPERUSER = "SYSTEM";
    public static final String ROLE_EXECUTIVEDEPARTEMNT = "Engineering";
    public static final String ROLE_DEPUTYDEPARTEMNT = "Engineering";
    public static final String ROLE_CSCOPERTAOR = "CSC Operator";
    public static final String ROLE_ULBOPERATOR = "ULB Operator";
    public static final String ROLE_COLLECTIONOPERATOR = "Collection Operator";
    public static final String ROLE_CITIZEN = "CITIZEN";
    public static final String ROLE_SEWERAGETAX_CREATOR = "Sewerage Tax Creator";
    public static final String ROLE_SEWERAGETAX_ADMINISTRATOR = "Sewerage Tax Administrator";
    public static final String ROLE_SEWERAGETAX_APPROVER = "Sewerage Tax Approver";
    public static final String ROLE_SEWERAGETAX_REPORTVIEWER = "Sewerage Tax Report Viewer";
    public static final String ROLE_STMS_VIEW_ACCESS_ROLE = "STMS_VIEW_ACCESS_ROLE";

    public static final String MODE = "mode";
    public static final String APPROVAL_POSITION = "approvalPosition";
    public static final String APPROVAL_COMMENT = "approvalComment";
    public static final String WORKFLOW_ACTION = "workFlowAction";

    public static final String SUBMITWORKFLOWACTION = "Submit";
    public static final String APPROVEWORKFLOWACTION = "Approve";

    public static final String PREVIEWWORKFLOWACTION = "Preview";

    public static final String VIEW = "View";
    public static final String COLLECTDONATIONCHARHGES = "Collect Fee";
    public static final String GENERATEREJECTIONNOTICE = "Generate Rejection Notice";
    public static final String VIEWURL = "/stms/existing/sewerage/view/{consumerno}/{assessmentno}";
    public static final String COLLECTDONATIONCHARHGESURL = "/stms/collection/generatebill/{consumerno}/{assessmentno}";
    public static final String VIEWDCBURL = "/stms/reports/sewerageRateReportView/{consumerno}/{assessmentno}";
    public static final String CLOSECONNECTION_ACTIONDROPDOWN = "Close Sewerage Connection";
    public static final String CLOSESEWERAGECONNECTIONURL = "/stms/transactions/closeConnection/{shscNumber}";
    public static final String GENERATEBEMANDBILL = "Generate Demand Bill";
    public static final String MODIFYLEGACYCONNECTION = "Modify Legacy Connection";
    public static final String GENERATEBEMANDBILLURL = "/stms/reports/generate-sewerage-demand-bill/{consumerno}/{assessmentno}";
    public static final String MODIFYLEGACYCONNECTIONURL = "/stms/transactions/sewerage/sewerageLegacyApplication-updateForm/{consumerno}/{assessmentno}";
    // Elastic Search Constants
    public static final String SEARCHABLE_SHSCNO = "searchable.shscnumber";
    public static final String CLAUSES_CITYNAME = "clauses.cityname";
    public static final String SEARCHABLE_CONSUMER_NAME = "searchable.consumername";
    public static final String CLAUSES_MOBILENO = "clauses.mobilenumber";
    public static final String CLAUSES_DOORNO = "clauses.doorno";
    public static final String CLAUSES_REVWARD_NAME = "clauses.revwardname";
    public static final String CLAUSES_APPLICATION_DATE = "clauses.applicationdate";
    public static final String SEARCHABLE_WO_NOTICE = "searchable.workordernumber";
    public static final String SEARCHABLE_EM_NOTICE = "searchable.estimationnumber";
    public static final String SEARCHABLE_DEMAND_BILL = "searchable.demandbillnumber";
    public static final String CLAUSES_WO_NOTICE_DATE = "clauses.workorderdate";
    public static final String CLAUSES_EM_NOTICE_DATE = "clauses.estimationdate";
    public static final String CLAUSES_DEMAND_BILL_DATE = "clauses.demandbilldate";
    public static final String CLAUSES_CHANNEL = "clauses.channel";
    public static final String CLAUSES_RECEIPT = "clauses.receiptcreator";
    public static final String CLAUSES_BILLING_SERVICE = "clauses.billingservice";
    public static final String CLAUSES_STATUS = "clauses.status";
    public static final String SEARCHABLE_RECEIPT_DATE = "searchable.receiptdate";
    public static final String CLAUSES_PROPERTY_TYPE = "clauses.propertytype";
    public static final String CLAUSES_ISACTIVE = "clauses.isactive";
    public static final String SEARCHABLE_APPLICATIONNO = "searchable.consumernumber";
    public static final String CLAUSES_APPLICATIONNO = "clauses.applicationnumber";
    public static final String CLAUSES_CC_NOTICE_DATE = "clauses.closurenoticedate";

    public static final LinkedHashMap<Integer, Integer> PIPE_SCREW_SIZE = new LinkedHashMap<Integer, Integer>() {
        /**
         *
         */
        private static final long serialVersionUID = -1063445500884125741L;

        {
            put(1, 1);
            put(2, 2);
            put(3, 3);
            put(4, 4);
            put(5, 5);
            put(6, 6);
            put(7, 7);
            put(8, 8);
            put(9, 9);
            put(10, 10);
            put(11, 11);
            put(12, 12);
            put(13, 13);
            put(14, 14);
            put(15, 15);
        }
    };

    // Fees Master Code - start
    public static final String FEES_ESTIMATIONCHARGES_CODE = "ESTIMATIONCHARGE";
    public static final String FEES_DONATIONCHARGE_CODE = "DONATIONCHARGE";
    public static final String FEE_INSPECTIONCHARGE = "INSPECTIONCHARGE";
    public static final String FEES_SEWERAGETAX_CODE = "SEWERAGETAX";
    public static final String FEES_ADVANCE_CODE = "SEWERAGEADVANCE";
    // Fees Master Code - end

    public static final String SEWAREGE_FUCNTION_CODE = "SEWERAGE_FUNCTION_CODE";
    public static final String COLL_RECEIPTDETAIL_DESC_PREFIX = "Collection";
    public static final String BILL_TYPE_AUTO = "AUTO";
    public static final String STRING_DEPARTMENT_CODE = "REV";
    public static final String STRING_SERVICE_CODE = "STAX";
    public static final String EST_STRING_SERVICE_CODE = "SWT-EST";
    public static final String DEFAULT_FUNCTIONARY_CODE = "1";
    public static final String DEFAULT_FUND_SRC_CODE = "01";
    public static final String DEFAULT_FUND_CODE = "01";
    public static final String DISPLAY_MESSAGE = "Sewerage Tax Collection";
    public static final String COLLECTION_WF_ACTION = null;

    public static final String VIEWDCB = "View DCB";

    public static final String TOTALDEMAND = "Total Demand";
    public static final String TOTALCOLLECTED = "Total Colleced";
    public static final String PENDINGDEMANDAMOUNT = "Pending Demand Amount";
    public static final String PENALTYAMOUNT = "Penalty Amount";

    public static final String ESTIMATIONCHARGE = "Estimation Charge";
    public static final String DONATIONCHARGE = "Donation Charge";
    public static final String INSPECTIONCHARGE = "Inspection Charges";
    public static final String SEWERAGETAX = "Sewerage Tax";
    public static final String INSTALLMENT = "Installment";
    public static final String ARREARSEWERAGETAX = "ArrearSewrgeTax";

    public static final String CHANGENOOFCLOSET = "Change number of seats";
    public static final String CHANGENOOFCLOSETURL = "/stms/transactions/modifyConnection/{shscNumber}";

    // Sms And Email Code

    public static final String SENDSMSFORSEWERAGETAX = "SENDSMSFORSEWERAGETAX";
    public static final String SENDEMAILFORSEWERAGETAX = "SENDEMAILFORSEWERAGETAX";

    // Sms And Email Code For New Connection
    public static final String SMSEMAILTYPENEWCONNCREATE = "newconnectioncreate";
    public static final String SMSEMAILTYPENEWCONNCREATEFORNOINSFEE = "newconnectioncreatefornoinsfee";
    public static final String SMSEMAILTYPENEWCONNESTNOTICE = "newconnectionestmationnotice";
    public static final String SMSEMAILTYPENEWCONNDEEAPPROVE = "newconnectiondeeapprove";
    public static final String SMSEMAILTYPENEWCONNFEEPAID = "newconnectionfeepaid";
    public static final String SMSEMAILTYPENEWCONNEXECUTION = "newconnectionexecutiondate";
    public static final String SMSEMAILTYPENEWCONNFINALAPPROVE = "newconnectionfinalapprove";
    public static final String SMSEMAILTYPECLOSINGCONNAPPROVE = "closureofconnectionapprove";
    public static final String SMSEMAILTYPECLOSINGCONNSANCTIONED = "closureofconnectionsanctioned";
    public static final String SMSEMAILTYPENEWCONNREJECT = "newconnectionreject";

    // Sms And Email Code For Change In Closets
    public static final String SMSEMAILTYPE_CHANGEINCLOSETS_CONN = "changeinclosetsofconnectionupdate";
    public static final String SMSEMAILTYPE_CHANGEINCLOSETS_CONN_NOINSFEE = "changeinclosetsofconnectionupdatefornoinsfee";
    public static final String SMSEMAILTYPE_CHANGEINCLOSETS_CONN_ESTNOTICE = "changeinclosetsofconnectionestmationnotice";
    public static final String SMSEMAILTYPE_CHANGEINCLOSETS_CONN_DEEAPPROVE = "changeinclosetsofconnectiondeeapprove";
    public static final String SMSEMAILTYPE_CHANGEINCLOSETS_CONN_FEEPAID = "changeinclosetsofconnectionfeepaid";
    public static final String SMSEMAILTYPE_CHANGEINCLOSETS_CONN_EXECUTION = "changeinclosetsofconnectionexecutiondate";
    public static final String SMSEMAILTYPE_CHANGEINCLOSETS_CONN_FINALAPPROVE = "changeinclosetsofconnectionfinalapprove";
    public static final String SMSEMAILTYPE_CHANGEINCLOSETS_CONN_APPROVE = "changeinclosetsofconnectionapprove";
    public static final String SMSEMAILTYPE_CHANGEINCLOSETS_CONN_SANCTIONED = "changeinclosetsofconnectionsanctioned";
    public static final String SMSEMAILTYPE_CHANGEINCLOSETS_CONN_REJECT = "changeinclosetsofconnectionreject";

    // Sms And Email Code For Close Sewerage Connection
    public static final String SMSEMAILTYPE_CLOSESEWERAGE_CONN_CREATE = "closesewerageconnectioncreate";
    public static final String SMSEMAILTYPE_CLOSESEWERAGE_CONN_EEAPPROVE = "closesewerageconnectioneeapprove";
    public static final String SMSEMAILTYPE_CLOSESEWERAGE_CONN_REJECT = "closesewerageconnectionreject";

    public static final String DEMANDISHISTORY = "N";

    // Notice Types
    public static final String NOTICE_TYPE_ESTIMATION_NOTICE = "Estimation Notice";
    public static final String NOTICE_TYPE_WORK_ORDER_NOTICE = "Workorder Notice";
    public static final String NOTICE_TYPE_CLOSER_NOTICE = "Close Connection Notice";
    public static final String NOTICE_TYPE_REJECTION_NOTICE = "Rejection Notice";
    public static final String NOTICE_TYPE_DEMAND_BILL_NOTICE = "Demand Bill";
    public static final String NOTICE_ESTIMATION = "EM";
    public static final String NOTICE_WORK_ORDER = "WO";
    public static final String NOTICE_REJECTION = "RN";
    public static final String NOTICE_CLOSE_CONNECTION = "CC";

    public static final String DOCTYPE_OTHERS = "Others";

    // DCB Report wardwise
    public static final String BOUNDARYTYPE_WARD = "Ward";
    public static final String HIERARCHYTYPE_REVENUE = "REVENUE";
    public static final String BOUNDARYTYPE_LOCALITY = "locality";
    public static final String HIERARCHYTYPE_LOCATION = "LOCATION";

    // property types
    public static final String RESIDENTIAL = "RESIDENTIAL";
    public static final String NONRESIDENTIAL = "NON_RESIDENTIAL";
    public static final String MIXED = "MIXED";
    public static final String ALL = "ALL";

    public static final String APPLICATION_TYPE_NAME_NEWCONNECTION = "New Sewerage Connection";
    public static final String APPLICATION_TYPE_NAME_CHANGEINCLOSETS = "Change In Closets";
    public static final String APPLICATION_TYPE_NAME_CLOSECONNECTION = "Close Sewerage Connection";
    public static final String GROUPBYFIELD = "groupByField";
    
    public static final String TOTALRESULTTOBEFETCH = "Number of Records used in next year demand generation";
    
    public static final String SLAFORNEWSEWERAGECONNECTION = "SLAFORNEWSEWERAGECONNECTION";
    public static final String SLAFORCHANGEINCLOSET = "SLAFORCHANGEINCLOSET";
    public static final String SEWERAGE_MONTHLY_RATES ="SEWERAGE MONTHLY RATES BY MULTIPLE CLOSETS";
    public static final String EDIT_DONATION_CHARGE ="EDIT_DONATION_CHARGE";
    public static final String MODIFYLEGACYCONNECTIONACTIONDROPDOWN="MODIFYLEGACYCONNECTION";
    
    //CSC operator related constants
    public static final String CSC_OPERATOR_ROLE = "CSC Operator";
    public static final String SEWERAGE_WORKFLOWDESIGNATION_FOR_CSCOPERATOR = "SEWERAGEDESIGNATIONFORCSCOPERATORWORKFLOW";
    public static final String SEWERAGE_WORKFLOWDEPARTEMENT_FOR_CSCOPERATOR = "SEWERAGEDEPARTMENTFORCSCOPERATORWORKFLOW";
    public static final String SEWERAGEROLEFORNONEMPLOYEE="SEWERAGEROLEFORNONEMPLOYEE";
    public static final String APPLICATION_PDF = "application/pdf";
    public static final String APPLICATION_STATUS_CSCCREATED = "CSCCREATED";
        
   //Online 
    public static final String Online = "Online";
    public static final String ANONYMOUS_USER = "Anonymous";
    public static final String APPLICATION_STATUS_ANONYMOUSCREATED = "ANONYMOUSCREATED";
    
    //Citizen
    public static final String APPLICATION_STATUS_CITIZENCREATED = "CITIZENCREATED";
    public static final String APPLICATION_STATUS_FEECOLLECTIONPENDING = "FEECOLLECTIONPENDING";

    public static final String PROPERTYID_NOT_EXIST_ERR_CODE = "STAX101";
    public static final String STAXDETAILS_CONSUMER_CODE_NOT_EXIST_ERR_MSG_PREFIX = "Sewerage Connection details with Consumer code ";
    public static final String STAXDETAILS_NOT_EXIST_ERR_MSG_SUFFIX = " does not exist";
    public static final String THIRD_PARTY_ERR_CODE_SUCCESS = "STAX-REST-0";
    public static final String THIRD_PARTY_ERR_MSG_SUCCESS = "SUCCESS";
    public static final String SEWERAGE_BILLNUMBER = "SEQ_SEWERAGEBILL_NUMBER";
    public static final String BILLTYPE_MANUAL = "MANUAL";
    public static final String PAYMENT_TYPE_PARTIALLY = "Partially";
    public static final String PAYMENT_TYPE_FULLY = "Fully";
    //reassignment
    public static final String APPCONFKEY_REASSIGN_BUTTONENABLED = "REASSIGN_BUTTONENABLED_SEWERAGE";
    public static final String SEWERAGE_DEPARTEMENT_FOR_REASSIGNMENT = "SEWERAGEDEPARTMENTFORREASSIGNMENT";  
    //Rest Api
    public static final String CURR_DMD_STR = "CURR_DMD";
    public static final String ARR_DMD_STR = "ARR_DMD";
    public static final String CURR_COLL_STR = "CURR_COLL";
    public static final String ARR_COLL_STR = "ARR_COLL";
    public static final String STAXDETAILS_PROPERTYID_NOT_EXIST_ERR_MSG_PREFIX = "Sewerage Connection details with Assessment Number ";



    
}