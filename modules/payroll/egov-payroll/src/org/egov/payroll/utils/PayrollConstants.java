package org.egov.payroll.utils;

public interface PayrollConstants {

	public static final String PAYSLIP_CANCELLED_STATUS = "Cancelled";
	public static final String SAL_ADV_TYPE_INTEREST = "interest";
	public static final String SAL_ADV_TYPE_NONINTEREST = "nonInterest";
	public static final String SAL_ADV_INTEREST_TYPE_SIMPLE = "simple";
	public static final String SAL_ADV_INTEREST_TYPE_REDUCING = "reducingBalance";
	public static final String SAL_ADV_PAYMENT_TYPE_CHEQUE = "cheque";
	public static final String SAL_ADV_PAYMENT_TYPE_CASH = "cash";
	public static final String SAL_ADV_PAYMENT_TYPE_DIRECT_BANK = "dbt";
	public static final String SAL_ADVANCE_MODULE = "Salaryadvance";
	public static final String SAL_ADVANCE_SANCTION = "Sanctioned";
	public static final String SAL_ADVANCE_CREATE = "Created";
	public static final String SAL_ADVANCE_DISBURSE = "Disbursed";
	public static final String PAYSLIP_MODULE = "PaySlip";
	//public static final String PAYSLIP_DEPT_APPROVED_STATUS = "AccountsApproved";
	public static final String PAYSLIP_CREATED_STATUS = "Created";
	public static final String EMP_EXCEPTION_MODILE = "EmpException";
	public static final String EMP_EXCEPTION_CREATED_STATUS = "Created";
	public static final String EMP_EXCEPTION_APPROVED_STATUS = "Approved";
	public static final String EMP_EXCEPTION_CLOSED_STATUS = "Closed";
	public static final String EMP_PAYSLIP_PAYTYPE_NORMAL = "Normal PaySlip";
	public static final String EMP_PAYSLIP_PAYTYPE_EXCEPTION= "Exception PaySlip";
	public static final String EMP_PAYSLIP_PAYTYPE_LEAVE_ENCASHMENT= "Leave Encashment PaySlip";
	public static final String EMP_PAYSLIP_PAYTYPE_FINAL_SETTLEMENT= "Final Settlement  PaySlip";
	public static final String EMP_PAYSLIP_PAYTYPE_ARREAR_PAYMENT= "Arrear PaySlip";
	public static final String EMP_EXCEPTION_WFKEY = "ExceptionWorkflow";
	public static final String Deduction_BankLoan = "Deduction-BankLoan";
	public static final String Deduction_Other = "Deduction-Other";
	public static final String Deduction_Advance = "Deduction-Advance";
	public static final String Deduction_Tax = "Deduction-Tax";
	public static final Integer DEDUCTION_TAX_TYPE= 3;
	public static final Integer DEDUCTION_OTHER_TYPE= 5;
	//public static final Integer EMP_PAYSLIP_PAYTYPE_FINALSET= 4;
	public static final Integer BATCH_FAILURE_DETAILS_STATUS_OPEN= 1;
	public static final Integer BATCH_FAILURE_DETAILS_STATUS_RESOLVED= 2;
	public static final Integer EMP_INCREMENT_DETAILS_STATUS_PENDING= 1;
	public static final Integer EMP_INCREMENT_DETAILS_STATUS_RESOLVED= 2;
	public static final Integer BATCH_GEN_STATUS_START= 1;
	public static final Integer BATCH_GEN_STATUS_CLOSE= 2;
	public static final Integer BATCH_GEN_STATUS_FAILED= 3;
	public static final Integer BATCH_GEN_STATUS_JOBDELETED= 4;
	public static final Integer BATCH_GEN_STATUS_TRIGGERED= 5;
	public static final String SAL_ADVANCE_REJECT = "Rejected";

	//For PensionDetails object status
	public static final String PENSIONDETAILS_MODULE = "PensionDetails";	
	public static final String PENSIONDETAILS_CREATED = "Created";
	public static final String PENSIONDETAILS_SUBMITED = "Submitted";
	public static final String PENSIONDETAILS_APPROVED = "Approved";
	public static final String PENSIONDETAILS_GRATUITY_DISBURSED = "GratuityDisbursed";
	public static final String PENSIONDETAILS_SUSPEND = "Suspended";
	public static final String PENSIONDETAILS_PENSION_TERMINATED = "PensionTerminated";
	public static final String PENSIONDETAILS_CANCELLED = "Cancelled";
	public static final String EMPLOYEE = "employee";
	public static final String NOMINEE = "nominee";
	public static final String EMPLOYEE_MODULE = "Employee";
	public static final String EMPLOYEE_EMPLOYED = "Employed";
	public static final String EMPLOYEE_RETIRED = "Retired";
	public static final String EMPLOYEE_DECEASED = "Deceased";
	public static final String EMPLOYEE_SUSPENDED = "Suspended";
	public static final String NOMINEE_MODULE = "Nominee";
	public static final String PENSION_PAYMENT_TYPE_CHEQUE = "cheque";
	public static final String PENSION_PAYMENT_TYPE_CASH = "cash";
	public static final String PENSION_PAYMENT_TYPE_DBT = "dbt";
	public static final String PENSION_PAYMENT_MODULE = "PensionPayment";
	public static final String PENSION_PAYMENT_CREATED = "Created";
	public static String PENSION_PAYMENT_DISBURSED = "Disbursed";
	public static String NOMINEE_TYPE_WIFE = "WIFE";
	public static String NOMINEE_TYPE_HUSBAND = "HUSBAND";
	public static String NOMINEE_TYPE_SON = "SON";
	public static String NOMINEE_TYPE_DAUGHTER = "DAUGHTER";
	public static String PAYSLIP_FINAL_STATUS = "AuditApproved";
	public static String SALCODE_GPF = "GPFSUB";
	public static String SALCODE_GPF_ADV = "GPFADV";

	public static String SALCODE_FEST_ADV = "FESTADV";
	
	
	
	
	//Purpose id for pension module
	public static final String GRATUITY_PAYABLE_PURPOSE_ID = "33";
	public static final String PENSION_PAYABLE_PURPOSE_ID = "34";
	public static final String EXPENSES_PAYABLE_PURPOSE_ID = "28";
	
	public static final String DETAIL_AMOUNT = "DetailAmount";
	
	//For application context
	public static final String APPLICATION_CONTEXT_KEY  = "applicationContext";  
	
	public static final String AUDIT_APPROVED = "AuditApproved";
	
	//ARF related constants
	public static final String PAYROLL_ARF_NUMBER_GENERATER_SCRIPT="payroll.arfnumber.generator";
	public static final String GENERATEADVANCEBPVURL = "Generate Advance Bpv";
	public static final String ADVANCE_WF_TYPE = "AdvanceWfType";
	
	//workflow type
	public static final String WORKFLOW_MANUAL = "Manual";
	public static final String WORKFLOW_AUTO = "Auto";
	public static final String WORKFLOW_SEMI = "SemiManual";
	
	//Payhead names as in db
	public static final String BASIC="BASIC";
	public static final String GRADEPAY="GP";
	
	public static final String CATEGORYMASTER_CATTYPE_DEDUCTUION="D";
	public static final String ISPAYSLIPEXIST_FOREMP_FROMDATE ="ISPAYSLIPEXIST_FOREMP_FROMDATE";
	public static final String PAYSLIP_PERIOD_OVERLAP_MSG="Payslip Period is overlapping, cannot generate Payslip";
	
	//Audit entity constants
	public static final String CREATE_PAYSCALE="Create Payscale";
	public static final String MODIFY_PAYSCALE="Modify Payscale";
	public static final String CREATE_PAYHEAD="Create Payhead";
	public static final String MODIFY_PAYHEAD="Modify Payhead";
}
