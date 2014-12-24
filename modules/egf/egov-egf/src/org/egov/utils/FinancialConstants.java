package org.egov.utils;

import org.egov.infstr.utils.EGovConfig;

/**
 * 
 * @author Manikanta 
 * will have all statuses of bill
 *
 */
public interface FinancialConstants {  
	//bill types ie module types used in egw_status
		public static final String SALARYBILL="SALBILL";
		//this will be used by External Systems 
		public static final String CONTINGENCYBILL="CBILL";
		//this is the one which is used by financials from 13/03/2010 
		public static final String CONTINGENCYBILL_FIN="EXPENSEBILL";
		public static final String SUPPLIERBILL="PURCHBILL";
		public static final String CONTRACTORBILL="WORKSBILL";

	// Standard Bill Types or Expernditure type
		
	public static final String STANDARD_EXPENDITURETYPE_CONTINGENT="Expense";
	public static final String STANDARD_EXPENDITURETYPE_WORKS="Works";
	public static final String STANDARD_EXPENDITURETYPE_PURCHASE="Purchase";
	public static final String STANDARD_EXPENDITURETYPE_SALARY="Salary";
	
	//Status for Salary Bills
	public static final String SALARYBILL_CREATED_STATUS="Created";
	public static final String SALARYBILL_CANCELLED_STATUS="Cancelled";
	public static final String SALARYBILL_APPROVED_STATUS="Approved";
	public static final String SALARYBILL_PASSED_STATUS="Passed";
	public static final String SALARYBILL_PAID_STATUS="paid";
	//Status for Contingent Bills
	public static final String CONTINGENCYBILL_CREATED_STATUS="CREATED";
	public static final String CONTINGENCYBILL_CANCELLED_STATUS="Cancelled";
	public static final String CONTINGENCYBILL_APPROVED_STATUS="Approved";
	public static final String CONTINGENCYBILL_PASSED_STATUS="Voucher Created";
	public static final String CONTINGENCYBILL_PAID_STATUS="payment confirmed";
	//Status for Supplier Bills
	public static final String SUPPLIERBILL_CREATED_STATUS="Pending";
	public static final String SUPPLIERBILL_CANCELLED_STATUS="Cancelled";
	//public static final String SUPPLIERBILL_APPROVED_STATUS="Approved";
	public static final String SUPPLIERBILL_PASSED_STATUS="Passed";
	public static final String SUPPLIERBILL_PAID_STATUS="Paid";
	//Status for Contrator Bills
	public static final String CONTRACTORBILL_CREATED_STATUS="Pending";
	public static final String CONTRACTORBILL_CANCELLED_STATUS="Cancelled";
	//public static final String CONTRACTORBILL_APPROVED_STATUS="Approved";
	public static final String CONTRACTORBILL_PASSED_STATUS="Passed";
	public static final String CONTRACTORBILL_PAID_STATUS="Paid";
	//vouchertypes
	public final String APPLCONFIGNAME="egf_config.xml";
	public final String CATEGORYFORVNO="vouchernumberformat";
	public final String CATEGORYFORDEFALULTPURPOSEID="defaultValues";
	
	public static final String RECEIPT_VOUCHERNO_TYPE =	EGovConfig.getProperty(APPLCONFIGNAME,"receipt","",CATEGORYFORVNO);
	public static final String PAYMENT_VOUCHERNO_TYPE =	EGovConfig.getProperty(APPLCONFIGNAME,"payment","",CATEGORYFORVNO);
	public static final String JOURNAL_VOUCHERNO_TYPE = EGovConfig.getProperty(APPLCONFIGNAME,"journal","",CATEGORYFORVNO);
	public static final String CONTRA_VOUCHERNO_TYPE  = EGovConfig.getProperty(APPLCONFIGNAME,"contra","",CATEGORYFORVNO);
	public static final String PURCHBILL_VOUCHERNO_TYPE = EGovConfig.getProperty(APPLCONFIGNAME,"purchasejv","",CATEGORYFORVNO);
	public static final String WORKSBILL_VOUCHERNO_TYPE = EGovConfig.getProperty(APPLCONFIGNAME,"worksjv","",CATEGORYFORVNO);
	public static final String FIXEDASSET_VOUCHERNO_TYPE = EGovConfig.getProperty(APPLCONFIGNAME,"fixedassetjv","",CATEGORYFORVNO);
	public static final String SALBILL_VOUCHERNO_TYPE = EGovConfig.getProperty(APPLCONFIGNAME,"salaryjv","",CATEGORYFORVNO);
	public static final String CBILL_VOUCHERNO_TYPE   =	EGovConfig.getProperty(APPLCONFIGNAME,"contingentjv","",CATEGORYFORVNO);
	public static final String VOUCHERNO_TYPE_LENGTH  = EGovConfig.getProperty(APPLCONFIGNAME,"length","",CATEGORYFORVNO);
	public static final String VOUCHERNO_TYPE_SUBLENGTH  = EGovConfig.getProperty(APPLCONFIGNAME,"sublength","",CATEGORYFORVNO);
		
	//default glcodes for transactions
	public static final String SALBILL_DEFAULT_PURPOSEID=EGovConfig.getProperty(APPLCONFIGNAME, "salaryBillDefaultPurposeId","",CATEGORYFORDEFALULTPURPOSEID);
	public static final String PURCHBILL_DEFAULT_PURPOSEID=EGovConfig.getProperty(APPLCONFIGNAME, "purchaseBillDefaultPurposeId","",CATEGORYFORDEFALULTPURPOSEID);
	public static final String WORKSBILL_DEFAULT_PURPOSEID=	EGovConfig.getProperty(APPLCONFIGNAME, "worksBillDefaultPurposeId","",CATEGORYFORDEFALULTPURPOSEID);	
	public static final String CBILL_DEFAULT_PURPOSEID=	EGovConfig.getProperty(APPLCONFIGNAME, "cBillDefaultPurposeId","",CATEGORYFORDEFALULTPURPOSEID);
	
	public static final String ModulesForBillAcctModify= EGovConfig.getProperty(APPLCONFIGNAME,"MODULESFORBILLSACCOUNTINGMODIFY","","general");
	//Instrument or Cheque related
	public static final String INSTRUMENT_DEPOSITED_STATUS = "Deposited";
	public static final String INSTRUMENT_CREATED_STATUS = "New";
	public static final String INSTRUMENT_RECONCILED_STATUS = "Reconciled";
	public static final String INSTRUMENT_DISHONORED_STATUS = "Dishonored";
	public static final String INSTRUMENT_CANCELLED_STATUS="Cancelled";
	public static final String INSTRUMENT_SURRENDERED_STATUS="Surrendered";
	public static final String INSTRUMENT_SURRENDERED_FOR_REASSIGN_STATUS="Surrender_For_Reassign";
	
	
	public static final String INSTRUMENT_TYPE_CHEQUE="cheque";
	public static final String INSTRUMENT_TYPE_CASH="cash";
	public static final String INSTRUMENT_TYPE_CARD="card";
	public static final String INSTRUMENT_TYPE_DD="dd";
	public static final String INSTRUMENT_TYPE_BANK = "bank";
	public static final String INSTRUMENT_TYPE_ADVICE	= "advice";
	public static final String INSTRUMENT_TYPE_ONLINE	= "online";
		
	public static final String IS_PAYCHECK_ONE = "1";
	public static final String IS_PAYCHECK_ZERO = "0";
	
	//Standard Voucher types
	public static final String STANDARD_VOUCHER_TYPE_CONTRA="Contra";
	public static final String STANDARD_VOUCHER_TYPE_PAYMENT="Payment";
	public static final String STANDARD_VOUCHER_TYPE_RECEIPT="Receipt";
	public static final String STANDARD_VOUCHER_TYPE_JOURNAL="Journal Voucher";
	//Contra related - Voucher Names
	public static final String CONTRAVOUCHER_NAME_BTOB="BankToBank";
	public static final String CONTRAVOUCHER_NAME_BTOC="BankToCash";
	public static final String CONTRAVOUCHER_NAME_CTOB="CashToBank";
	public static final String CONTRAVOUCHER_NAME_PAYIN="Pay in slip";
	//Payment related - Voucher Names
	public static final String PAYMENTVOUCHER_NAME_BILL="Bill Payment";
	public static final String PAYMENTVOUCHER_NAME_DIRECTBANK="Direct Bank Payment";
	public static final String	PAYMENTVOUCHER_NAME_REMITTANCE	="Remittance Payment";
	public static final String INTERESTVOUCHER_NAME_BANKENTRY="Bank Entry";
	public static final String BANKCHARGESVOUCHER_NAME_BANKENTRY="Bank Entry";
	
	//Journal -Voucher Names
	public static final String JOURNALVOUCHER_NAME_GENERAL="JVGeneral";
	public static final String JOURNALVOUCHER_NAME_SUPPLIERJOURNAL="Supplier Journal";
	public static final String JOURNALVOUCHER_NAME_CONTRACTORJOURNAL="Contractor Journal";
	public static final String JOURNALVOUCHER_NAME_SALARYJOURNAL="Salary Journal";
	public static final String JOURNALVOUCHER_NAME_EXPENSEJOURNAL="Expense Journal";
	
	
	public static final Integer REVERSALVOUCHERSTATUS=2;
	public static final Integer REVERSEDVOUCHERSTATUS=1;
	public static final Integer CANCELLEDVOUCHERSTATUS=4;
	public static final Integer PREAPPROVEDVOUCHERSTATUS=5;
	public static final Integer CREATEDVOUCHERSTATUS=0;
	//Direct bank payment and payments
	public static final String MODEOFPAYMENT_CHEQUE="cheque";
	public static final String MODEOFPAYMENT_CASH="cash";
	public static final String MODEOFPAYMENT_RTGS="rtgs";
	//Receipt 
	public static final String MODEOFCOLLECTION_CHEQUE="cheque";
	public static final String MODEOFCOLLECTION_OTHER="other";
	public static final String MODEOFCOLLECTION_BANK="bank";
	public static final String	CBILL_DEFAULTCHECKLISTNAME	= "Others";  
	
	public static final String MULTIPLE="MULTIPLE";
	
	public static final String MODULE_NAME_APPCONFIG="EGF";
	
	public static final String WORKFLOWENDSTATE="END";  
	
	
	public static final String TYPEOFACCOUNT_PAYMENTS = "PAYMENTS";
	public static final String TYPEOFACCOUNT_RECEIPTS = "RECEIPTS";
	public static final String TYPEOFACCOUNT_RECEIPTS_PAYMENTS="RECEIPTS_PAYMENTS";
	
	
	
                                 
}
