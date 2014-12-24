package org.egov.pims.empLeave.service;
//@author Divya,deepak
import java.text.ParseException;
import java.util.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.egov.commons.CFinancialYear;
import org.egov.pims.empLeave.model.CalendarYear;
import org.egov.pims.empLeave.model.CompOff;
import org.egov.pims.empLeave.model.EmployeeAttendenceReport;
import org.egov.pims.empLeave.model.Attendence;
import org.egov.pims.empLeave.model.AttendenceType;
import org.egov.pims.empLeave.model.HolidaysUlb;
import org.egov.pims.empLeave.model.LeaveApplication;
import org.egov.pims.empLeave.model.LeaveApproval;
import org.egov.pims.empLeave.model.LeaveMaster;
import org.egov.pims.empLeave.model.LeaveOpeningBalance;
import org.egov.pims.empLeave.model.TypeOfLeaveMaster;
import org.egov.pims.empLeave.model.Wdaysconstnts;
import org.egov.pims.model.EmployeeView;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.model.StatusMaster;
	/**
	 * Divya,Deepak
	 *
	 */
public interface EmpLeaveService 
{
	public abstract void updateLeaveMaster(LeaveMaster leaveMaster);
	public abstract LeaveOpeningBalance addLeaveOpeningBalance(LeaveOpeningBalance leaveOpeningBalance);
	public abstract TypeOfLeaveMaster getTypeOfLeaveMasterById(Integer typeOfLeaveMasterId);
	public abstract TypeOfLeaveMaster getTypeOfLeaveMasterByName(String name);
	 public abstract LeaveMaster getLeaveMasterById(Integer leaveMasterId);
	public abstract LeaveApproval getLeaveApprovalByApplicationID(Integer applicationId);
	public abstract void updateLeaveApproval(LeaveApproval leaveApproval);
	public abstract void addLeaveApproval(LeaveApproval leaveApproval)throws SQLException;
	public abstract void addCompOff(CompOff compOff);
	public abstract CompOff getCompOffById(Integer compOffId);
	public abstract void updateCompOff(CompOff compOff);
	public List getNoOfWorkingDaysBweenTwoDatesByFinYear(java.util.Date fromDate, java.util.Date toDate,CFinancialYear cfinancial);
	public abstract LeaveApproval getLeaveApprovalById(Integer leaveApprovalId);
	public abstract AttendenceType getAttendenceTypeId(Integer id);
	public abstract AttendenceType getAttendenceTypeByName(String typeName);
	public abstract void updateLeaveApplication(LeaveApplication leaveApplication);
	public abstract  void createLeaveApplication(Integer workingDays ,PersonalInformation employee,String leaveType,String reason);
	public abstract void createLeaveApplication(LeaveApplication leaveApplication);

	public abstract void addLeaveApplication(PersonalInformation employee,LeaveApplication leaveApplication);
	public abstract LeaveApplication getLeaveApplicationById(Integer leaveApplicationId);
	public abstract List getLeaveMastersForDesID(Integer desigId) ;
	public abstract List getLeaveApplicationsRejectedEmpID(Integer empId);
	public abstract List getLeaveApplicationsApprovedEmpID(Integer empId);
	public abstract List getLeaveApplicationsAppliedEmpID(Integer empId);
	public abstract Set getLeaveApplicationsForEmpID(Integer empId) ;
	public abstract boolean checkPayEligible(Integer applicationId);
	public abstract Map getleaveTypesForDesignation(Integer desigId);
	public abstract Integer getNextVal();
	public abstract Float getLeaveBalancesByempIDandTypeId(Integer empId,Integer typeOfLeaveId);
	public abstract  float getLeavesEarnedinTheGivenYear(Integer empId,Integer type,Date givenDate);
	public  abstract Integer getLeavesAvailedinTheYear(Integer empId,Integer type,Date givenDate);
	public abstract Float getMaxEligibleinTheCurrentYear(Integer empId,Integer type,Date givenDate);
	public abstract Float getAvailableLeavs(Integer empId,Integer type,Date givenDate);
	public abstract List getListOfLeaveTranx(Integer empId,CFinancialYear financialY);
	public abstract List getListOfLeaveCard(Integer empId,CFinancialYear financialY);
	public abstract String getApplicationNumber();
	public abstract boolean checkApplicationNoForLeave(String appNo);
	public abstract boolean checkSanctionNoForLeave(String sanctionNo);
	public abstract List getListByAppNoAndLeaveType(String applicationNumber ,Integer leaveType,Integer empId) ;
	public abstract void create(LeaveMaster leaveMaster);
	public abstract HolidaysUlb getHolidayId(Integer holId);
	public abstract List getHolidayListByFinalsialYearId(CFinancialYear cFinancialYear);
	public abstract void createHolidaysUlb(HolidaysUlb holidaysUlb);
	public abstract void updateHolidaysUlb(HolidaysUlb holidaysUlb);
	public abstract Attendence createAttendence(Attendence att);
	public abstract void updateAttendence(Attendence att);
	public abstract Attendence getAttendenceById(Integer id);
	public abstract StatusMaster getStatusMasterByName(String  name);
	public abstract Map getMapOfAttendenceByMonth(Integer month,CFinancialYear cFinancialYear);
	public abstract Map getMapOfLeaveType(Date date , PersonalInformation employee);

	public abstract  Set listOfWorkingDaysInMonth(int monthId,CFinancialYear cFinancialYear);
	public abstract int calculateNoOfWorkingDaysBweenTwoDates(java.util.Date fromDate, java.util.Date toDate);
	public abstract Set listOfDaysWorkedForAnEmployeeForGivenMonth(int month,PersonalInformation personalInformation,CFinancialYear cFinancialYear,Boolean b,int CurYear);
	public abstract Set listOfDaysAbsentForAnEmployeeForGivenMonth(int month,PersonalInformation personalInformation,CFinancialYear cFinancialYear,Boolean b,int CurYear);
	public abstract Map listOfPaidDaysForAnEmployeeForGivenMonth(int month,PersonalInformation personalInformation,CFinancialYear cFinancialYear,Boolean b,int CurYear);
	public abstract EmployeeAttendenceReport getEmployeeAttendenceReport(int month,PersonalInformation personalInformation,CFinancialYear cFinancialYear);
	public abstract Set getHolidaySet(CFinancialYear cFinancialYear);
	
	public abstract List getListOfLeaveMastersForDesID(Integer desigId) ;
	public abstract Attendence checkAttendenceByEmpAndDte(Integer empId, java.util.Date toDate);
	public abstract Wdaysconstnts getWDconstant();
	public abstract List getAllWDconstant();
	public abstract Wdaysconstnts getWdaysconstntsByID(Integer id);
	public abstract Integer getActiveWdaysconstnts();
	public abstract void updateWdaysconstnts(Wdaysconstnts wdaysconstnts);
	public abstract List getHolidaysUlbsFotFinalsialYearId(CFinancialYear cFinancialYear);
	//set of holiday list based on calendar year
	public abstract List getHolidayListByCalendarYearID(CalendarYear calendarYear);
	public abstract void deleteAttendence(Attendence att);
	public abstract void RejectLeaves(String cityURL, String jndi,String hibFactName);
	public abstract void populateLeaveBalences(String cityURL, String jndi, String hibFactName);
	public abstract List getNoOfWorkingDaysBweenTwoDates(java.util.Date fromDate, java.util.Date toDate);
	public  abstract EmployeeAttendenceReport getEmployeeAttendenceReportBetweenTwoDates(Date fromDate, Date toDate,PersonalInformation personalInformation);
	public abstract Set listOfDaysWorkedForAnEmployeebetweenTwoDates(Date fromDate,Date toDate,PersonalInformation personalInformation) throws Exception;
	public abstract Set listOfDaysAbsentForAnEmployeeForGivendates(Date fromDate,Date toDate,PersonalInformation personalInformation);
	public abstract Map listOfPaidDaysForAnEmployeeForGivenDates(Date fromDate,Date toDate,PersonalInformation personalInformation);
	public abstract List getListCompOffObjects(Integer empId);
	public abstract Set listOfCompOffsForAnEmployeeForGivenMonth(int month,PersonalInformation personalInformation,CFinancialYear cFinancialYear,Boolean b,int CurYear);
	public abstract Set listOfDaysCompOffsForAnEmployeeForGivendates(Date fromDate,Date toDate,PersonalInformation personalInformation);
	public Map searchEmployeeForAttendence(Integer departmentId,Integer designationId,String code,String name,String searchAll, 
			String finYear, String monthId,Integer funId,String fromSearchName,String toSearchName,Integer functionId,Integer employeeTypeId,Integer billId);
	/*
	 * for leave open balance
	 */
	public  abstract List searchEmployeeForLeaveOpeningBalance(Integer departmentId,Integer designationId,String code,String name,Integer finYear,Integer TypeOfLeaveMstr)throws Exception;
	public  List<EmployeeAttendenceReport> searchEmployeeForAttRept(Integer designationId,String code,String name,String searchAll,String month,String finYear,Map<String,Integer> finParams);
	public abstract boolean checkLeaveReportsForAnEmployee(Integer empId);
	public abstract boolean checkLeave(Integer empId);
	public abstract Set listHalfPresentForAnEmployeebetweenTwoDates(Date fromDate,Date toDate,PersonalInformation personalInformation);
	public abstract Set  listHalfPresentForAnEmployeeForGivenMonth(int month,PersonalInformation personalInformation,CFinancialYear cFinancialYear,Boolean b,int CurYear);
	public abstract Integer getNextValForAttId();
	public abstract Integer getNextValForCompOffId();
	
	public LeaveOpeningBalance getLeaveOpeningBalanceByEmpID(Integer empId,Integer leaveType);
	public HolidaysUlb getHolidayById(Integer holidayId);
	public void deleteHoliday(HolidaysUlb holiday);
	public List<LeaveApplication> getEncashmentLeaveApplicationByStatus(String statusName)throws Exception;
	public List<LeaveApplication> getLeaveApplicationByEmpStatus(Integer empId, StatusMaster status)throws Exception;
	public List getListOfAccumulativeTypeOfLeaves();
	
	/*
	 * Api for Calendar based
	 */
	public abstract String getCurrentYearId(); 
	public abstract String getYearIdByGivenDate(String estDate);
	public abstract CalendarYear getCalendarYearById(Long id);
    
	public abstract Boolean isLeaveCalendarBased();

	public abstract List getAllCalendarYearList()throws Exception;
	public abstract Boolean isHolidayEnclosed() throws Exception;
	public abstract int calculateNoOfWorkingDaysForHolidayEnclosed(java.util.Date fromDate, java.util.Date toDate,
			CFinancialYear cfinancial)throws Exception;
	public abstract List getNoOfWorkingDaysByFinYearForReport(
			java.util.Date fromDate, java.util.Date toDate,
			CFinancialYear cfinancial)throws Exception;
	
	public abstract Boolean checkForCompOffApprove(Integer AttId) throws Exception; 
	
	public abstract Boolean isSelfApproval();
	public Set listOfHolidaysForTwoDates(java.util.Date fromDate, java.util.Date toDate);

	public abstract Boolean isLeaveAvailForDateEmpIdStatus(Date givenDate,Long empid) ; 
    /**
     * Api for Leave Auto/Manual WorkFlow Config
     */
	
	public abstract Boolean isLeaveWfAutoOrManaul();
	
	/**
	 * Getting Number of working days for an empoloyee in between fromDate and toDate by considering 
	 *      "holdiy from attendence if present otherwise from Ulb holidaySet"
	 * @param fromDate
	 * @param toDate
	 * @param employee
	 * @return
	 */
	public List getNoOfWorkingDaysBweenTwoDatesForEmployee(java.util.Date fromDate, java.util.Date toDate, PersonalInformation employee);
	
	/**
	 * Returning set of holidays in string format for an employee in between two dates
	 * @param fromDate
	 * @param toDate
	 * @param employee
	 * @return
	 */
	public Set<String> getHolidaySetForEmployeInDateRange(Date fromDate, Date toDate, PersonalInformation employee);
	public List getAccumulatedLeaveTypes();

}
