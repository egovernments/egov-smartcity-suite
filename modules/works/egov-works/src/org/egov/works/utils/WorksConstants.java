package org.egov.works.utils;

public class WorksConstants {
	
	public static final String WORKS="Works";
	public static final String alphaNumericwithspecialchar="[0-9a-zA-Z-& :,/.()@]+";  
	public static final String TEMPLATENAME_COMPLETIONCERTIFICATE="completionReport";
	public static final String PARAMETERNAME_WORKCOMPLETIONINFO="workCompletionInfo";
	public static final String BILL_ACTION_VIEWCOMPLETIONCERTIFICATE="View Completion Certificate";
	public static final String BILL_TYPE_PARTBILL="PartBillType";
	public static final String BILL_TYPE_FINALBILL="FinalBillType";
	public static final String YES="yes";
	public static final String NO="no";
	public static final String BUDGET_CONSUMPTION_STATUS="ADMIN_SANCTIONED";

	// Pagination
	public static final int PAGE_SIZE=30;
	
	// Named queries
	public static final String QUERY_GETALLMBHEADERSBYBILLID="getAllMBHeadersbyBillId";
	public static final String QUERY_GETALLWORKORDERACTIVITYWITHMB="getallWorkOrderActivityWithMB";
	public static final String QUERY_GETALLWORKORDERACTIVITYWITHOUTMB="getallWorkOrderActivityWithoutMB";
	public static final String QUERY_GETALLMBNOSBYWORKORDERESTIMATE="getAllMBNosbyWorkEstimate";
	public static final String QUERY_GETACTIVEDEPOSITCODES = "EGW_getActiveDepositCodes";
	public static final String QUERY_GETACTIVEDEPOSITCODES_BY_CODE_OR_DESC = "EGW_getActiveDepositCodesByCodeOrDescription";
	
	public static final String WORKORDER_LASTSTATUS="WorkOrder.laststatus";
	public static final String QUERY_GETSTATUSDATEBYOBJECTID_TYPE_DESC="getStatusDateByObjectId_Type_Desc";
	public static final String CAPITAL_WORKS = "Capital Works";
	public static final String IMPROVEMENT_WORKS = "Improvement Works";
	public static final String REPAIR_AND_MAINTENANCE = "Repairs and maintenance";
	public static final String DEPOSIT_NO_ASSET_CREATED = "Deposit Works No Asset Created";
	public static final String DEPOSIT_ASSET_CREATED = "Deposit Works Third Party Asset";
	public static final String DEPOSIT__OWN_ASSET_CREATED = "Deposit Works Own Asset";
	public static final String ASSET_LIST="assestList";
	public static final String COA_LIST="coaList";
	public static final String KEY_CWIP = "WORKS_CWIP_CODE";
	public static final String KEY_REPAIRS ="WORKS_REPAIRS_AND_MAINTENANCE";
	public static final String KEY_DEPOSIT ="WORKS_DEPOSIT_OTHER_WORKS";
	//For asset Capitalisation
	public static final String ASSET_STATUS_CAPITALISED="Capitalized";
	public static final String NATUREOFWORKFORASSETCAPITALISATION="NATUREOFWORKFORASSETCAPITALISATION";
	public static final String NATUREOFWORKFORASSETIMPROVMENT="NATUREOFWORKFORASSETIMPROVMENT";
	public static final String NATUREOFWORKFORASSETREPAIRANDMAINTAINANCE="NATUREOFWORKFORASSETREPAIRANDMAINTAINANCE";
	public static final String NATUREOFWORKFORASSETCAPITALISATION_DEFAULTVALUE="Capital Works - New Asset,Deposit Works - Own Asset";
	public static final String NATUREOFWORKFORASSETIMPROVMENT_DEFAULTVALUE="Capital Works - Improvement Works";
	public static final String NATUREOFWORKFORASSETREPAIRANDMAINTAINANCE_DEFAULTVALUE="Repairs and maintenance Works";
	//For Deposit Work Folio
	public static final String QUERY_GETDEPOSITWORKSUSAGELISTFORDEPOSITFOLIO="getDepositWorksUsageListForDepositFolio";

	//RebatePremium
	public static final String BILL="BILL";

	//Tender Negotiation
	public static final String TENDER_TYPE="TENDER_TYPE";
	
	public static final String TEMPLATENAME_CONTRACTCERTIFICATE="contractCertificate";
	public static final String PARAMETERNAME_WORKCONTRACTNFO="workContractInfo";
	public static final String BILL_ACTION_VIEWCONTRACTCERTIFICATE="View Contract Certificate";
	
	public static final String EXPENDITURETYPE_CONTINGENT="Expense";
	
	public static final String MBPERCENTAPPCONFIGKEY="MB_PERCENTAGE_LEVEL";
	public static final String MBPERCENTCONFIGVALUE="TotalMBValue"; 
	public static final String MBAMTABOVEAPPROVEDAMT="MB_AMT_ABOVE_APPOVED_AMT";
	
	//Notification and Scheduler for workcompletion date change
	public static final String NOOFDAYSBEFOREWOCOMPLETIONDATENOTIFICATION="NO_OF_DAYS_BEFORE WO_COMPLETION_NOTIFICATION";
	public static final String APPROVED="APPROVED";
	public static final String WORKCOMPLETIONDETAIL="WorkCompletionDetail";
	
	//Create Contractor
	public static final String CODE="code";
	public static final String NAME="name";
	public static final String CORRESPONDENCE_ADDRESS="correspondeneceAddress";
	public static final String PAYMENT_ADDRESS="paymentAddress";
	public static final String CONTACT_PERSON="contactPerson";
	public static final String EMAIL="email";
	public static final String NARRATION="narration";
	public static final String PAN_NUMBER="panNumber";
	public static final String TIN_NUMBER="tinNumber";
	public static final String BANK_CODE="bankCode";
	public static final String IFSC_CODE="ifscCode";
	public static final String BANK_ACCOUNT="bankAccount";
	public static final String PWD_APPROVAL_CODE="pwdApprovalCode";
	public static final String REGISTRATION_NUMBER="registrationNumber";
	public static final String STATUS="status";
	public static final String CONTRACTOR_CLASS="class";
	public static final String START_DATE="startDate";
	public static final String END_DATE="endDate";
	public static final String DEPT_CODE="deptCode";
	public static final String CONTRACTOR_UPDATE_TYPE="contractorUpdateType";
	public static final String DEPT_CODE_PW="PW";
	public static final String DEPT_CODE_WW="WW";
	public static final String DEPT_CODE_GD="GD";
	public static final String DEPT_CODE_ELEC="Elec";
	
} 
