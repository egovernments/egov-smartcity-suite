package org.egov.pims.empLeave.model;

public class EmployeeAttendenceReport implements java.io.Serializable
{
	Integer noOfWorkingDaysInMonth = null;
	Integer noOfWorkingDaysbetweenDates = null;
	Integer employeeId= null;
	Integer daysInMonth = null;
	Float noOfPresents = null;
	Float noOfAbsents = null;
	Integer noOfCompOff = null;
	Float noOfPaidleaves = null;
	Integer noOfHalfPresents = null;
	Float noOfUnPaidleaves = null;
	Integer noOfDaysfromDateOfJoin = null;
	Float noOfPaidDays = null;
	Float noOfAbsentInHolidays = null;
	Integer noOfOverTime = null;
	public Float getNoOfAbsents() {
		return noOfAbsents;
	}
	public void setNoOfAbsents(Float noOfAbsents) {
		this.noOfAbsents = noOfAbsents;
	}
	
	
	public Float getNoOfPresents() {
		return noOfPresents;
	}
	public void setNoOfPresents(Float noOfPresents) {
		this.noOfPresents = noOfPresents;
	}
	public Integer getNoOfWorkingDaysInMonth() {
		return noOfWorkingDaysInMonth;
	}
	public void setNoOfWorkingDaysInMonth(Integer noOfWorkingDaysInMonth) {
		this.noOfWorkingDaysInMonth = noOfWorkingDaysInMonth;
	}
	public Integer getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}
	public Integer getDaysInMonth() {
		return daysInMonth;
	}
	public void setDaysInMonth(Integer daysInMonth) {
		this.daysInMonth = daysInMonth;
	}
	public Float getNoOfPaidleaves() {
		return noOfPaidleaves;
	}
	public void setNoOfPaidleaves(Float noOfPaidleaves) {
		this.noOfPaidleaves = noOfPaidleaves;
	}
	public Float getNoOfUnPaidleaves() {
		return noOfUnPaidleaves;
	}
	public void setNoOfUnPaidleaves(Float noOfUnPaidleaves) {
		this.noOfUnPaidleaves = noOfUnPaidleaves;
	}
	public Integer getNoOfWorkingDaysbetweenDates() {
		return noOfWorkingDaysbetweenDates;
	}
	public void setNoOfWorkingDaysbetweenDates(Integer noOfWorkingDaysbetweenDates) {
		this.noOfWorkingDaysbetweenDates = noOfWorkingDaysbetweenDates;
	}
	public Integer getNoOfCompOff() {
		return noOfCompOff;
	}
	public void setNoOfCompOff(Integer noOfCompOff) {
		this.noOfCompOff = noOfCompOff;
	}
	public Integer getNoOfHalfPresents() {
		return noOfHalfPresents;
	}
	public void setNoOfHalfPresents(Integer noOfHalfPresents) {
		this.noOfHalfPresents = noOfHalfPresents;
	}
	public Integer getNoOfDaysfromDateOfJoin() {
		return noOfDaysfromDateOfJoin;
	}
	public void setNoOfDaysfromDateOfJoin(Integer noOfDaysfromDateOfJoin) {
		this.noOfDaysfromDateOfJoin = noOfDaysfromDateOfJoin;
	}
	public Float getNoOfPaidDays() {
		return noOfPaidDays;
	}
	public void setNoOfPaidDays(Float noOfPaidDays) {
		this.noOfPaidDays = noOfPaidDays;
	}
	public Float getNoOfAbsentInHolidays() {
		return noOfAbsentInHolidays;
	}
	public void setNoOfAbsentInHolidays(Float noOfAbsentInHolidays) {
		this.noOfAbsentInHolidays = noOfAbsentInHolidays;
	}
	public Integer getNoOfOverTime() {
		return noOfOverTime;
	}
	public void setNoOfOverTime(Integer noOfOverTime) {
		this.noOfOverTime = noOfOverTime;
	}
	
	
}
