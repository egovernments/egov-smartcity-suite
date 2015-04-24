/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.bpa.constants;

import java.util.Arrays;
import java.util.List;

public class BpaConstants {

	public static final String ZONE_BNDRY_TYPE = "Zone";
	public static final String WARD_BNDRY_TYPE = "Ward";
	public static final String STREET_BNDRY_TYPE = "Street";
	// public static final String ROLE_ID = "124";
	public static final String DATECOMPARETEN = "10";
	public static final String FIFTEEN = "15";
	public static final String THIRTY = "30";
	public static final String BPAMODULENAME = "BPA";
	public static final String CMDALPFORMATSTRING = "LPCMDA";
	public static final String DEPARTMENT_CODE = "BPA_DEPARTMENT_CODE";
	public static final String RENT_COLLECTION = "Fee Collection";
	public static final String SERVICE_CODE = "BPA";
	public static final String EXTD_SERVICE_CODE = "EXTDBPA";
	public static final String COLL_RECEIPTDETAIL_DESC_PREFIX = "Collection ";
	public static final String KEYFORAUTOGENSHOPNO = "AUTOGEN_SHOPNUMBER";
	public static final String FEEGROUPCMDA = "CMDA";
	public static final String FEEGROUPMWGWF = "MWGWF";
	public static final String PROPERTY_ADDRESS = "PROPERTY";
	public static final String OWNER_ADDRESS = "OWNER";
	public static final String REQUEST_TYPE = "REGISTRATION";
	public static final String ACTIONURL = "newform.jsp";
	// Service Types
	public static final String SERVICETYPE_SUBDIV = "SubDivision";
	public static final String SERVICETYPE_LAYOUT = "Layout";
	public static final String SERVICETYPE_DO = "DO";
	public static final String SERVICETYPE_CMDA = "CMDA";
	public static final String BPAAEROLE="BPA AE";
	public static final String BPAAEEROLE="BPA AEE";
	public static final String BPAREVISEDAPPTYPE="Revised";
	public static final String SCRUTINEEFEECODEFORSERVICETYPE0NE="106";
	public static final String SCRUTINEEFEECODEFORSERVICETYPETWO="206";
	public static final String SCRUTINEEFEECODEFORSERVICETYPETHREE="306";
	public static final String SCRUTINEEFEECODEFORSERVICETYPEFOUR="406";
	public static final String SCRUTINEEFEECODEFORSERVICETYPEFIVE="506";
	public static final String SCRUTINEEFEECODEFORSERVICETYPESIX="606";
	public static final String SCRUTINEEFEECODEFORSERVICETYPESEVEN="706";
	// search
	public static final String WDC = "WDC";
	public static final String OBPA = "OBPA";
	
	public static final String VIEWAPPLICATION = "View Application";
	public static final String VIEWPLANFORAUTODCR	="View Plan";
	public static final String VIEWWORKFLOWHISTORY = "View Workflow History";
	public static final String VIEWADMISSIONFEERECEIPT = "View Fees Receipt";
	public static final String VIEWINSPECTIONDETAILS = "View Site Inspection Details";
	public static final String VIEWCHALLANNOTICE = "View challan Notice";
	public static final String VIEWCHALLANRECEIPT = "View challan Receipt";
	public static final String VIEWFEETOBEPAID = "Print Challan";
	public static final String VIEWPLANINGPERMITORDER = "View Planning permit order";
	public static final String PRINTEXTERNALFEEDETAILS = "Print External Fee Details";
	public static final String VIEWBUILDINGPERMITORDER = "View Building permit order";
	public static final String VIEWREJECTIONORDER = "View Rejection order";
	public static final String COLLECTFEE = "Collect Fee";
	public static final String ADDINSPECTIONDETAILS = "Add Inspection Details";
	public static final String ADDSITEINSPECTIONDETAILS = "Add Site Inspection";
	public static final String INSPECTIONSOURCEFOREXISTINGPLAN = "ExistingPerPlan";
	public static final String INSPECTIONSOURCEFOREXISTINGSITE = "ExistingPerSite";
	public static final String INSPECTIONSOURCEFORCONSTRUCTIONPLAN = "ConstructionPerPlan";
	public static final String INSPECTIONSOURCEFORCONSTRUCTIONSITE = "ConstructionPerSite";
	public static final String VIEWLETTERTOPARTY = "View Letter To Party";
	public static final String LETTERTOPARTYREPLY = "Letter To Party Reply";
	public static final String CMDALETTERTOPARTYREPLY = "Letter To CMDA Reply";

	public static final String NEWBUILDINGONVACANTPLOTCODE = "01";
	public static final String APPLICATIONFORDEMOLITIONCODE = "02";
	public static final String RECLASSIFICATION = "08";
	public static final String DEMOLITIONRECONSTRUCTIONCODE = "03";
	public static final String SUBDIVISIONOFLANDCODE = "04";
	public static final String LAYOUTAPPPROVALCODE = "05";
	public static final String ADDITIONALCONSTRUCTIONCODE = "06";
	public static final String CMDACODE = "07";
	public static final String RECLASSIFICATIONCODE = "08";

	public static final List<String> SEARCHDROPDOWNLIST = Arrays.asList(VIEWAPPLICATION, VIEWWORKFLOWHISTORY,
			VIEWADMISSIONFEERECEIPT, VIEWINSPECTIONDETAILS, VIEWCHALLANNOTICE, VIEWCHALLANRECEIPT,
			VIEWPLANINGPERMITORDER, VIEWBUILDINGPERMITORDER, VIEWREJECTIONORDER, COLLECTFEE, ADDINSPECTIONDETAILS,
			ADDSITEINSPECTIONDETAILS, VIEWLETTERTOPARTY, LETTERTOPARTYREPLY, VIEWFEETOBEPAID);

	// EgReasonCategory code
	public static final String CATEGORY_FEE = "FEE";
	public static final String FUND_CODE = "BPA_FUND_CODE";
	public static final String FUNCTION_CODE = "BPA_FUNCTION_CODE";;

	// status
	public static final String APPLICATIONREGISTERED = "Registered";
	public static final String CREATEDLETTERTOPARTY = "Letter To Party Created";
	public static final String LPREPLYRECEIVED = "LPREPLYRECEIVED";
	public static final String CMDACREATEDLETTERTOPARTY ="Letter To CMDA Created";
	public static final String CMDALPREPLYRECEIVED ="Letter to CMDA Reply Received";
	public static final String APPLICANTSIGNUPDATED = "Applicant Signature updated";
	public static final String LETTERTOPARTYSENT = "LP Sent to Applicant";
	public static final String CMDALETTERTOPARTYSENT = "Letter To CMDA Sent";
	public static final String ORDERPREPARED = "Order Prepared";
	public static final String ORDERISSUEDTOAPPLICANT = "Order Issued to Applicant";
	public static final String CHALLANRECEIPTCREATED = "Challan receipt created";
	public static final String REJECTORDERPREPARED = "Unconsidered Order Prepared";
	public static final String REJECTORDERISSUED = "Unconsidered Order Issued";
	public static final String CANCELLED = "Cancelled";
	public static final String STATUSAPPROVED = "Approved";
	public static final String REJECTIONAPPROVED = "Unconsideration Approved";
	public static final String FILECONSIDERATIONCHECKED = "File Consideration Checked";
	public static final String CHALLANNOTICESENT = "Challan Notice Sent to Citizen";
	public static final String CHALLANAMOUNTCOLLECTED = "Challan Amout is Collected";
	public static final String UNDERREJECTION = "Under Unconsideration";
	public static final String CLOSEREGISTRATION = "Close Registration";
	public static final String ADMISSIONFEE = "AdmissionFee";
	public static final String CAPTURE_DD_DETAILS = "Capture DD details";
	public static final String STATUSREJECT = "Reject";

	// new register by citizen
	public static final String CITIZENAPPLICATIONREGISTERED = "CitizenRegisteredApplication";

	public static final String PRIMARYSTATUSAPPROVE = "Approve";
	public static final String PRIMARYSTATUSREJECT = "Reject";
	// workflow
	public static final String WF_NEW_STATE = "NEW";
	public static final String WF_CREATED_STATE = "CREATED";
	public static final String WF_APPROVE_STATE = "APPROVED";
	public static final String WF_END_STATE = "END";
	public static final String REGISTRATIONMODULE = "BPAREGISTRATION";
	public static final String SCRIPT_SAVE = "save";
	public static final String SCRIPT_REJECT = "reject";
	public static final String BPAREGISTRATIONMODULE = "BPAREGISTRATION";
	public static final String NEWBPAREGISTRATIONMODULE = "NEWBPAREGISTRATION";
	public static final String INSPECTED = "Considered";
	public static final String SANCTIONEDFEE = "Sanction Fees";
	public static final String INSPECTIONSCHEDULED = "Site Inspection Scheduled";
	public static final String FEESCREATED = "File Consideration Checked";
	public static final String BPALETTERTOPARTY = "BPALETTERTOPARTY";
	public static final String BPAAPPLICATIONREGISTRATION = "ApplicationRegistration";
	public static final String DISPATCHRECORD = "Dispatch Record";
	public static final String LPAPPROVEDSTATE="LP-Approved";
	public static final String WF_CANCELLED = "Cancelled";
	public static final String ROLELISTFORBPA = "ROLELIST_BPA";
	public static final String ADDITONALRULEREJECTBPA = "RejectBPA";
	public static final String SURVEYORDOCUPLOAD = "DocUPloadForSurveyor";

	// report
	public static final String GENERAL = "General";
	public static final String GREENCHANNEL = "Green Channel";
	public static final String REGISTRATIONACKNOWLEDGEMENT = "RegistrationAck";
	public static final String REGISTRATIONEXTNACKNOWLEDGEMENT = "RegistrationAckExtn";
	public static final String DOCUMENTHISTORYPRINT = "DocumentHistoryExtn";
	public static final String INSPECTIONDETAILSEXTN = "InspectionDetailsExtn";
	public static final String DOCKETSHEETPRINT = "DocketSheet";
	
	
	public static final String INSPECTIONDETAILS = "inspectiondetails";
	public static final String LETTERTOPARTYDETAILS = "lettertoparty";
	public static final String LETTERTOCMDA = "lettertoCMDA";
	public static final String FEEDETAILS = "feedetails";
	public static final String NOACCESS = "NOACCESS";
	public static final String LPREPLYACKREPORT = "LpReplyAck";
	
	public static final String LPREPLYEXTNACKREPORT = "LpReplyExtnAck";
	
	public static final String EXPTERNALFEEACKREPORT = "ExternalFeeAck";
	public static final String EXPTERNALFEEEXTNACKREPORT = "ExternalFeeAckExtn";
	
	public static final String LETTERTOPARTYNOTICE = "LetterToPartyNotice";
	public static final String REJECTIONNOTICE = "RejectionNotice";
	public static final String BUILDINGPERMIT = "BuildingPermit";
	public static final String PLANPERMIT = "PlanPermit";
	public static final String BUILDINGPERMITCITIZEN = "BuildingPermitCitizen";
	public static final String PLANPERMITCITIZEN = "PlanPermitCitizen";
	
	public static final String REJECTIONNOTICEEXTN = "RejectionNoticeExtn";
	
	public static final String BUILDINGPERMITEXTN = "BuildingPermitExtn";
	public static final String PLANPERMITEXTN = "PlanPermitExtn";
	public static final String PLANPERMITFORDEMOLITIONEXTN = "PlanPermitDemolitionExtn";
	public static final String PLANPERMITFORDEMOLITION = "PlanPermitDemolition";
	public static final String PLANPERMITFORDEMOLITIONCITIZEN = "PlanPermitDemolitionExtnCitizen"; 
	public static final String PLANPERMITFORRECLASSIFICATIONEXTNCITIZEN = "PlanPermitReclassificationExtnCitizen";
	
	public static final String PLANPERMITFORRECLASSIFICATION = "PlanPermitReclassification";
	public static final String PLANPERMITFORRECLASSIFICATIONEXTN = "PlanPermitReclassificationExtn";
	public static final String REGISTRATIONDETAILS = "registrationdetails";

	// Lp CheckList Type
	// Reclassification

	public static final String AGRICULTURE = "Agriculture";
	public static final String INDUSTRIAL = "Industrial";
	public static final String MIXEDRESIDENTIAL = "Mixed Residential";
	public static final String PRIMARYRESIDENSIAL = "Primary Residential";
	public static final String INSTITUTIONAL = "Institutional";
	public static final String COMMERCIAL = "Commercial";
	public static final String OPENSPACERESERVATION = "Open space Reservation";
	public static final String RECOMMENDED = "Recommended";
	public static final String NOTRECOMMENDED = "Not Recommended";

	public static final String LPCHKLISTTYPE_REPLY = "LETTERTOPARTYREPLY";

	// checklist master
	public static final String DOCUMENTATION = "DOCUMENTATION";
	public static final String LP = "LP";
	public static final String UNCONSIDERED = "UNCONSIDERED";
	// public static final String Inspection = "Inspection";
	public static final String FORWARDWORKFLOWSTATUS = "forward";
	public static final String FORWARDWORKFLOWSTATUSFORCMDA = "Forward";
	public static final String APPROVEDWORKFLOWSTATUS ="Approved";
	public static final String REJECTWORKFLOWSTATUS = "Reject";
	public static final String MSG_ADMINISSIONFEECOLLECTED = "Admission Fee collected";
	public static final String STATUS_APPLICATIONREGISTERED = "Application Registered";
	public static final String SAVELETTERTOPARTY = "save lettertoparty";
	// Citizen Report
	public static final String REJECTION = "Unconsidered";
	public static final String PERMIT = "Permit Issued";
	public static final String LETTERTOPARTY = "Letter to Party";
	public static final String TOTALST = "Total";
	public static final String PERMITISSUED = "DPermit Issued";
	public static final String LETTERTOPARTYABSTRACT = "GLetter to Party";
	public static final String APPLICATION = "Application Pending";
	// public static final String ApplicationPending="";
	public static final String REJECTIONABSTRACT = "EUnconsidered";
	public static final String SANCTION = "Sanction";
	public static final String INSPECTIONREP = "Inspection";
	public static final String ADVICEABSTRACT = "C Advice";
	public static final String APPLICATIONABSTRACT = "FApplication";
	public static final String DISP = "BDisposed";
	public static final String LPAPP = "ILPTOTAL";
	public static final String REFERENCE = "HLRefDept";
	// Order Update Actions
	public static final String UPDATESIGNATURE = "Update Signature";
	public static final String ORDERISSUED = "Order Issued";
	public static final String REJECTORDPREP = "Unconsidered Order Prepared";
	public static final String REJECTORDISSUED = "Unconsidered Order Issued";
	public static final String ADDCHALLANSENTDATE = "Add Challan Sent Date";

	public static final String FEECOLLECTIONMESSAGE = "Fee Collection : Plan Submission Number -";
	public static final String PRINTBUILDINGPERMIT = "Print Building Permit";
	public static final String PRINTPLANPERMIT = "Print Plan Permit";
	public static final String INSPECTION = "INSPECTION";
	public static final String VIEWINSPECTIONSCHEDULE = "View InspectionSchedule";
	public static final String WORKFLOWSTATUSFORWARDEDTOAEE = "Forwarded to AEE";
	public static final String BUILDINGMEASUREMENT = "Building Measurement Update";
	public static final String LABOURWELFACECHARGEDETAILS = "Labour welfare charges details";
	public static final String INFRASTRUCTURECHARGEDETAILS = "Infrastructure and Amenities charges details";
	public static final String EXTERNALFEELABOURWELFARE = "LabourWelfare";
	public static final String EXTERNALFEECMDA = "CMDA";
	public static final String EXTERNALFEENODATAFOUND = "";
	
	public static final String FORWARDEDTOEESTATE = "Forwarded to EE";
	public static final String FORWADREDTOSESTATE  = "Forwarded to SE";
	public static final String FORWARDEDTORDCSTATE = "Forwarded to Approval";

	// SMS & EMAIL
	public static final String SMSEMAILSAVE = "SaveWithoutFee";
	public static final String SMSEMAILAPPROVE = "Approve";
	public static final String SMSEMAILAPPROVELETTERTOPARTY = "Approve LetterToParty";
	public static final String CMDASMSEMAILAPPROVELETTERTOPARTY = "Approve LetterToCMDA";
	public static final String SMSEMAILLP = "LetterToParty";
	public static final String SMSEMAILINSPECTION = "Inspection";
	public static final String SMSEMAILSECURITYKEY = "securityKey";
	public static final String SMSCHALLANNOTICESENTDATE = "challNoticeSentDate";
	public static final String SMSEMAILASSISTANTTOAEEONSAVE = "ToAEEonSave";
	public static final String SMSEMAILASSISTANTTOAEEONLP = "ToAEEonLP";

	public static final String OFFICIALSDESIGLISTFORSMS = "OFFICIALS_DESIGLIST_SMS_BPA";
	// modes
	public static final String MODEVIEW = "view";
	public static final String MODEREJECT = "reject";
	public static final String MODENOEDIT = "noEdit";
	public static final String MODEFROMMODIFY = "frommodify";
	public static final String DUECHECKFORPROPERTY = "DueCheckForProperty";
	public static final String TOWNPLANNINGFUNCTIONARY = "TP";
	public static final String ASSISTANTDESIGNATION = "ASSISTANT";
	public static final String PIMSMODULETYPE = "Employee";
	public static final String PIMSEMPLOYEDCODE = "Employed";
	public static final String TERRACE = "Terrace";
	public static final String BASEMENT = "Basement";
	public static final String STILT = "Stilt";
	public static final String TILEDROOF = "Tiledroof";
	public static final String GROUND = "Ground Floor";
	public static final String GROUNDFLOORFORREPORT = "GroundFloor";
	public static final String AUTO_GENERATION_INSPCTIONDATES = "AUTO_GENERATION_INSPCTIONDATES";
	public static final String HOLIDAYLIST_INCLUDED = "HOLIDAYLIST_INCLUDED";
	public static final String IS_SATURDAY_HOLIDAY = "IS_SATURDAY_HOLIDAY";
	public static final String IS_SECONDSATURDAY_HOLIDAY = "IS_SECONDSATURDAY_HOLIDAY";
	public static final String IS_SUNDAY_HOLIDAY = "IS_SUNDAY_HOLIDAY";
	public static final String SMS_MOBILE_NUMBER = "919604424242";
	public static final String SMSAEEONAUTOGENERATIONOFSITEINSPECTIONDATE = "ToAEEonSiteInspectionDateGeneration";
	public static final String CITYNAME = "CORPORATION OF CHENNAI";
	public static final String INSPECTIONSTARTDATE_EMAILBODYDETAILS = "Dear {0},\n \n You have registered Building Plan Application with Plan Submission Number {1} . Your Site will be Inspected on date {2} .  \n \n This is a computer generated email and does not need any signature.\n\nThanks,\n\n{3}";
	public static final String INSPECTIONSTARTDATE_EMAIL_SUBJECT = "Site will be Inspected on date {0} .";
	public static final String INSPECTIONSTARTDATE_SMS_SUBJECT = "The Site Inspection date Auto generated for ( {0} ) on {1} .";
	public static final String INSPECTIONSTARTDATE_SMSBODYDETAILS = "Dear {0},You have registered Building Plan Application with Plan Submission Number {1}.  Your Site will be Inspected on date {2}";

	// Object type to maintain History
	public static final String OBJECTTYPEBPAREGISTRATION = "BPA_REGISTRATION";
	public static final String BPAREGISTRATIONFEEMODULE = "BPAREGISTRATIONFEE";
	public static final String BPAREGISTRATIONFEEMODULESTATUSAPPROVED = "Approved";
	public static final String ADDREVISEDFEE = "Add Revised Fee";
	public static final String RevisedFeeInitiated = "RevisedFeeInitiated";
	public static final String ACKNOWLEDGEMENTNUMBERFORMAT = "00000";
	public static final String SMSEMAILREVISEDFEEAPPROVE = "RevisedFeeApprove";
	public static final String CMDAFEE = "CMDA";
	public static final String COCFEE = "COC";
	public static final String MWGWFFEE = "MWGWF";
	public static final String FeeRemarks = "1.Latest Property Tax receipt. \n" + "2.8 copies of corrected plan.\n"
			+ "3.Filled up statistical details. \n";

	public static final String HEIRARCHYTYPE = "ADMINISTRATION";
	public static final String BOUNDARYTYPE = "Zone";
	public static final String LETTERTOPARTYCREATED = "Letter To Party Created";
	public static final String APPLICATIONCLOSED = "Application Closed";
	public static final String DISCARDREGISTRATION = "Discard";
	public static final String MODIFYREGISTRATION = "Modify";
	public static final String PRINTPPCERTIFICATEFORCITIZEN = "Print Plan Permit";
	public static final String PRINTBPCERTIFICATEFORCITIZEN = "Print Building Permit";

	public static final String WORKFLOWSTATUSFORWARDEDTOAE = "Forwarded to AE";
	public static final String WORKFLOWSTATUSFORWARDEDTOAEORAEE ="Forward to AEORAEE";
	public static final String ADDAPPROVALINFORMATION = "Add/Modify Approval Information";
	public static final String COLLECTFEEDETAILS = "Add FeeDetails";
	public static final String ADDNEWSITEINSPECTIONDETAILS ="Add New SiteInspection";
	public static final String MODIFYFEEDETAILS = "Modify FeeDetails";
	// For LP notice
	public static final String NORTHREGION = "North Region";
	public static final String SOUTHREGION = "South Region";
	public static final String CENTRALREGION = "Central Region";
	public static final String ASSISTANTADDRESS = "Assistant Engineer/Assistant Executive Engineer, \nTown Planning Approval Section,";
	public static final String NORTHREGION_ADDRESS = "Regional Office North, \nCorporation of Chennai, \nNo.105,Basin Bridge Road, \nRoyapuram, Chennai - 600079.";
	public static final String SOUTHREGION_ADDRESS = "Regional Office South, \nCorporation of Chennai, \n115, Dr. Muthulakshmi Salai, \nAdyar, Chennai - 600020.";
	public static final String CENTRALREGION_ADDRESS = "Regional Office Center, \nCorporation of Chennai, \nNo.36 G, Pulla Avenue, \nShenoy Nagar, Chennai - 600040.";

	// For new Bpa registration changes
	public static final String EXECUTIVEENGINEERDESIGNATION = "EXECUTIVE ENGINEER";
	public static final String ASSISTANTEXECUTIVEENGINEERDESIGNATION = "ASSISTANT EXECUTIVE ENGINEER";
	public static final String ASSISNTENGINEERDESIGNATION = "ASSISTANT ENGINEER";
	// License Surveyor
	public static final String SURVEYOR_DETAILTYPE = "License Surveyor";

	// NEWBPAREGISTRATION - Letter to party status
	public static final String LETTERTOPARTYRAISED = "Letter To Party Raised By LS";
	public static final String LETTERTOPARTYREPLYRECEIVED = "LP Reply received for LS";
	public static final String LETTERTOPARTYNOTICEEXTN = "LetterToPartyNoticeExtn";
	public static final String LPREPLYACKREPORTEXTN = "LpReplyAckExtn";
	public static final String CMDALPREPLYACKREPORTEXTN = "LpReplyCmdaAckExtn";
	public static final String CMDALPREPLYACKREPORT= "LpReplyCmdaAck";
	public static final String CMDALPREPLYNOTICEACKREPORT= "LpReplyCmdaNoticeAckExtn";
	public static final String CMDALPREPLYNOTICEACKOLDREPORT= "LpReplyCmdaNoticeAck";
	//new register by citizen
    public static final String SITEINSPECTIONSCHEDULEDBYLS ="Site Insp Scheduled By LS"; 
    public static final String APPLICATIONFORWARDEDTOLS ="Application Forwarded to LS";
    public static final String PORTALUSERSURVEYORROLE="PORTALUSERSURVEYOR";
    public static final String PORTALUSERCITIZENROLE="PORTALUSERCITIZEN";
    public static final String CREATELETTERTOPARTY = "Create Letter To Party";
    public static final String ADDINSPECTIONDATE = "Add InspectionDate"; 
    public static final String ADDSITEINSPECTIONDETAIL = "Add Site Inspection Detail";
    public static final String CREATELETTERTOCMDA = "Create Letter To CMDA";
    
	public static final String APPLICATION_FWDED_TO_LS = "Application Forwarded to LS";
	public static final String BPAEXTN_STATUS_FORWARDED_TO_EE = "FORWARDED_TO_EE";
	
	public static final String SMSEMAILLPR = "LetterToPartyReply";
	public static final String PAYFEEONLINE ="Pay Fee Online";
	 public static final String FWD_EE = "Forward To EE"; 
	 public static final String INSPECTEDBYLS = "Site Inspected By LS"; 
	 public static final String REGION_BNDRY_TYPE="Region";
	 public static final String NEWREGISTRATION_CITIZENREGISTERED="CitizenRegisteredApplication";
	 public static final String NEWREGISTRATION_WFSTATETYPE="RegistrationExtn";
	 public static final String LECREATED_WFSTATETYPE="created";
	 public static final String LESAVED_WFSTATETYPE="LetterToParty saved";
	 public static final String LPRAISED_WFNEXTACTION="LP-Raised";
	 public static final String CMDALPRAISED_WFNEXTACTION="LetterToCMDA-Raised";
	 public static final String NEWBPACANCELLEDSTATUS = "CANCELLED";
	 
	 //feesDescription
	 public static final String ROADCUTCHARGESFORTNEB = "Road cut charges - TNEB";
	 public static final String ROADCUTCHARGESFORSEWERAGE = "Road cut charges - CMWSSB SEWERAGE";
	 public static final String ROADCUTCHARGERFORWATER = "Road cut charges - CMWSSB, WATER";
	 
	 //
	 public static final String NEWAPPTYPE = "New";
	 public static final String DOCKET_PARKINGGNRLVOILATION_CONSTRUCTIONSTAGEREAR="Usage of construction in the rear";
	 public static final String INSP_DWELLUNIT_APPCONFKEY = "INSPECTION_DWELLUNIT_DATE";
	 public static final String FWD_AEORAEE = "Forward To AE/AEE"; 
	 public static final String UNAPPROVED_LAYOUT_TYPE="UnApproved Layout";
	 public static final String STATENAME="TAMILNADU";
	 public static final String REGISTRATION_OBJECT_TYPE="Registration";
	 public static final String CITY_NAME="Chennai";
	 public static final String BPA_FILE_UPLOAD_LOCATION="BPA_FILE_UPLOAD_LOCATION";
	 
	 
	 public static final String VIEWED_SURVEYOR_INSPECTION = "VIEWED SURVEYOR INSPECTION";
	 public static final String VIEWED_AE_AEE_INSPECTION = "VIEWED_AE/AEE INSPECTION";
	 public static final String VIEWED_SURVEYOR_DOCDETAILS = "VIEWED SURVEYOR DOCDETAILS";
	 public static final String VIEWED_AE_AEE_DOCDETAILS = "VIEWED AE/AEE DOCDETAILS";
	 public static final String VIEWED_AUTODCR_DETAILS = "VIEWED AUTODCRDETAILS";
	 public static final String VIEWED_DOCKETSHEET = "VIEWED DOCKETSHEET";
	 public static final String SENDMESSAGEANDSMSWITHOLDPSNNUMBER="SENDMESSAGEANDSMSWITHOLDPSNNUMBER";
	 public static final String LETTERTOCMDA_WF_INITIALSTATUS = "LetterToCMDA Initiated";
	 public static final String SAVELETTERTOCMDA = "save lettertocmda";
	 public static final String CREATEDCMDALETTERTOPARTY = "Letter To CMDA Created";
	 public static final String SMSEMAILCMDALP = "LetterToCMDA";
	 public static final String SMSEMAILAPPROVECMDALETTERTOPARTY = "Approve LetterToCMDA"; 
}