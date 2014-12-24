package org.egov.pims.model;

// Generated Jul 9, 2007 3:21:09 PM by Hibernate Tools 3.2.0.b9

import java.util.Date;


/**
 * DetOfEnquiryOfficer
 */
public class DetOfEnquiryOfficer implements java.io.Serializable
{

	private Integer enquiryOfficerId;
	private DisciplinaryPunishment disciplinarypunishmentId;
	private String enquiryOfficerCode;
	private Integer empId;
	public PersonalInformation employeeId;
	private String designation;
	private String enquiryOfficerName;
	private Date nominatedDate;
	private Date reportDate;

	public DetOfEnquiryOfficer()
	{
	}

	public DetOfEnquiryOfficer(Integer enquiryOfficerId)
	{
		this.enquiryOfficerId = enquiryOfficerId;
	}
	public String getEnquiryOfficerCode() {
		return enquiryOfficerCode;
	}

	public void setEnquiryOfficerCode(String enquiryOfficerCode) {
		this.enquiryOfficerCode = enquiryOfficerCode;
	}



	public Integer getEnquiryOfficerId()
	{
		return this.enquiryOfficerId;
	}

	public void setEnquiryOfficerId(Integer enquiryOfficerId)
	{
		this.enquiryOfficerId = enquiryOfficerId;
	}

	public String getDesignation()
	{
		return this.designation;
	}

	public void setDesignation(
			String designation)
	{
		this.designation= designation;
	}


	public String getEnquiryOfficerName()
	{
		return this.enquiryOfficerName;
	}

	public void setEnquiryOfficerName(String enquiryOfficerName)
	{
		this.enquiryOfficerName = enquiryOfficerName;
	}

	public Date getNominatedDate()
	{
		return this.nominatedDate;
	}

	public void setNominatedDate(Date nominatedDate)
	{
		this.nominatedDate = nominatedDate;
	}

	public Date getReportDate()
	{
		return this.reportDate;
	}

	public void setReportDate(Date reportDate)
	{
		this.reportDate = reportDate;
	}

	public DisciplinaryPunishment getDisciplinarypunishmentId() {
		return disciplinarypunishmentId;
	}

	public void setDisciplinarypunishmentId(
			DisciplinaryPunishment disciplinarypunishmentId) {
		this.disciplinarypunishmentId = disciplinarypunishmentId;
	}

	public PersonalInformation getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(PersonalInformation employeeId) {
		this.employeeId = employeeId;
	}

	public Integer getEmpId() {
		return empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}


}
