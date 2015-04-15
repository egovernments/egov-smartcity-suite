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
