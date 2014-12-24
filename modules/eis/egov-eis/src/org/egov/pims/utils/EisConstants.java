package org.egov.pims.utils;

public interface EisConstants {

	public static final String INT_TYPE_DESIGNATION = "10";
	public static final String INT_TYPE_PAYSCALEHEAD = "11";
	public static final String COMP_OFF = "CompOff";
	public static final String ABSENT = "Absent";
	public static final String PRESENT = "Present";
	public static final String COMPOFF_ELIG = "CompOffElligibe";
	/* Changed to Leave Paid
	public static final String LEAVE = "Leave";
	public static final String HALFLEAVE = "HalfLeave";
	public static final String TWOHALFLEAVE = "TwoHalfLeaves";
	*/
	public static final String LEAVE_PAID = "LeavePaid";
	public static final String HALFLEAVE_PAID = "HalfLeavePaid";
	public static final String TWOHALFLEAVE_PAID = "TwoHalfLeavesPaid";
	/* Added unpaid leaves*/
	public static final String LEAVE_UNPAID = "LeaveUnpaid";
	public static final String HALFLEAVE_UNPAID = "HalfLeaveUnpaid";
	public static final String TWOHALFLEAVE_UNPAID = "TwoHalfLeavesUnpaid";
	public static final String ISPRORATED = "isProrated";
	
	public static final String HALFPRESENT = "HalfPresent";
	public static final String HOLIDAY = "Holiday";
	public static final String STATUS_APPLIED = "Applied";
	public static final String STATUS_SANCTIONED = "Sanctioned";
	public static final String STATUS_REJECTED = "Rejected";
	public static final String STATUS_APPROVED = "Approved";
	public static final String OBJECTTYPE_LEAVE = "Leave";
	public static final String OBJECTTYPE_DISCIPLINARY = "Disciplinary";
	public static final String OBJECTTYPE_EMPLOYEE = "employee";
	public static final int LEAVE_TYPE_ID_FOR_EL =6; 
	public static final String STATUS_ENCASHED= "Encashed";
	public static final String LEAVE_ENCASHMENT_VALUE="1";
	/* pension--nominee types*/
	public static final Integer NOM_TYPE_SON=1;
	public static final Integer NOM_TYPE_WIFE=2;
	public static final Integer NOM_TYPE_DAUGHTER=3;
	public static final Integer NOM_TYPE_HUSBAND=4;
    public static final String STATUS_TYPE_EMPLOYED="Employed";
    public static final String STATUS_TYPE_RETIRED="Retired";
    public static final String STATUS_TYPE_SUSPENDED="Suspended";
    public static final String STATUS_TYPE_DECEASED="Deceased";
    //Eligibility Status Values
    public static final Integer ELIGIBLE=1;
    public static final Integer NOT_ELIGIBLE=0;
    public static final Integer UNKNOWN=2;
    public static final String bankTransfer="dbt";
	public static final String STATUS_CANCELLED="Cancelled";
	
	public static final String MODULE_PAYROLL="Payroll";
	
	public static final String STATUS_MODULE_TYPE="Employee";
	public static final String MODULE_MASTER="Pension";
	public static final String MODULE_KEY_FOR_BANK="BankPension";
	public static final String MODULE_EMPLOYEE="Employee";
	public static final String OVER_TIME="OverTime";
	
	public static final String MODULE_LEAVEAPP="Leave Application";
	public static final String MODULE_KEY_ISCALENDERBASED="ISCALENDARBASEDYESORNO";
	public static final String MODULE_KEY_COMPVAL="COMPOFF_VALIDITY";
	public static final String MODULE_KEY_LEAVEWF="LeaveAutoOrManualWorkFlow";
	public static final String MANUALWF="Manual";
	public static final String AUTOWF="Auto";
	public static final String MODULE_DISCIPLINARY="Disciplinary";
	public static final String GENERATE_EXCEPTION_DISCIPLINARY="ExceptionCreate";
	public static final String ESS="ess";
	public static final String STATUS_MODULE_TYPE_DISC="DisciplinaryPunishment";
	
	public static final String MODULE_EIS="eis";
	public static final String MODULE_KEY_LEAVEACRUAL_CRONEXPR="LeaveAcrualAutoGenCronExp";
	
	public static final String NAMEDQUERY_BILLNUMBER_BY_ID = "BILLNUMBER_BY_ID";
	public static final String EMP_POSITION="EmpPosition";
	public static final String EMPPOS_CANCELLED="Cancelled";
	public static final String EMPPOS_CREATED="Created";
	
	public static final String REV_CANCELLED="Cancelled";
	public static final String REVERSION="Reversion";
	public static final String REV_CREATED="Created";
	
}
