package org.egov.pims.empLeave.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.egov.commons.CFinancialYear;
import org.egov.pims.empLeave.model.Attendence;
import org.egov.pims.model.PersonalInformation;

public interface AttendenceDAO extends org.egov.infstr.dao.GenericDAO
{
	public Attendence getAttendenceByID(Integer ID) ;
	public Map getMapOfAttendenceByMonth(Integer month,CFinancialYear cFinancialYear) ;
	public List getListOfPresentDaysForEmployeebetweenDates(Date fromDate,Date toDate,PersonalInformation personalInformation);
	public List getListOfAbsentDaysForEmployeebetweenDates(java.util.Date fromDate,java.util.Date toDate,PersonalInformation personalInformation,CFinancialYear cFinancialYear);
	public Attendence checkAttendenceByEmpAndDte(Integer id,java.util.Date date);
	public List getListOfCompOffDaysForEmployeebetweenDates(Date fromDate,Date toDate,PersonalInformation personalInformation);
	public List getListOfHalfPresentDaysForEmployeebetweenDates(java.util.Date fromDate,java.util.Date toDate,PersonalInformation personalInformation);
	public Map getMapOfPaidAndUnpaidDaysForEmployeebetweenDates(java.util.Date fromDate,java.util.Date toDate,PersonalInformation personalInformation);
	public List getListOfOverTimeForAnEmployeeBetweenDates(java.util.Date fromDate,java.util.Date toDate,PersonalInformation personalInformation);
	/**
	 * Getting all attendence list for an employee in between fromDate and toDate
	 */
	public List<Attendence> getAttendenceListInDateRangeForEmployee(Date fromDate, Date toDate, PersonalInformation employee);
	/**
	 * Getting all haliday attendence list for an employee in between fromDate and toDate
	 */
	public List<Attendence> getHolidayAttendenceListInDateRangeForEmployee(Date fromDate, Date toDate, PersonalInformation employee);
	
}

