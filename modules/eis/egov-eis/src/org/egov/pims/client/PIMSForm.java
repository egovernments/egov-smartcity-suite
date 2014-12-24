/*
 *
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.pims.client;
import java.util.List;

import org.apache.struts.action.ActionForm;


public class PIMSForm extends ActionForm
{

//*********personel information fields ****************
	private String employeeDob ="";
	private String payHeadId ="";
	private String isActive ="";
	private String statusMaster ="";
	private String userFirstName ="";
	private String userMiddleName ="";
	private String userLastName ="";
	private String mainDepartmentId ="";
	private String retirementAge ="";
	private String maturityDate ="";
    private String modifyremarks="";
	private String[] payScaleHeader;
	private String[] effDate ;
	private String[] annualIncrDate ;
	private String[] currBasicPay ;
	
	private	String[] gradeId;
	
	private String name ="";
	private String payCommition ="";
	private String effictiveFrom ="";
	private String amountFrom ="";
	private String amountTo ="";
	private String panNumber ="";
	private String sanctionNo="";
	private String statusId="";
	private String statusTypeId="";
	private String applicationNumber;
	private String dispId="";
	private String deptId ="";
	private String userStatus ="0";
	private	String[] enquiryOfficeName ;
	private	String[] remarks ;
	private	String[] eoDesignation ;
	private	String[] eoNoDate ;
	private	String[] eoReDate ;
	private String[] emploid;
	private String employeeCode ="";
	private String firstName ="";
	private String middleName ="";
	private String lastName ="";
	private	String fatherfirstName ="";
	private	String fathermiddleName ="";
	private	String fatherlastName ="";
	private	String bloodGroup ="";
	private	String motherTounge ="";
	private	String religionId ="";
	private	String communityId ="";
	private	String phand = "0";
	private String gender="0";
	private	String isMed = "0";
	private	String identificationMarks1 ="";
	private	String identificationMarks2 ="";
	private	String dateOfFA ="";
	private	String propertyNopre ="";
	private	String StreetNamepre ="";
	private	String localitypre ="";
	private	String citypre ="";
	private	String pinCodepre ="";
	private	String propertyNoper ="";
	private	String StreetNameper ="";
	private	String localityper ="";
	private	String cityper ="";
	private	String pinCodeper ="";
	private	String mOrId ="";
	private	String lanId ="";
	private	String recruitmentTypeId ="";
	private	String category ="";
	private	String status ="";
	private	String tamillangaugequlified ="";
	private	String payFixIn ="";
	private String isPrimary[] ;
	
	private	String basic ="";
	private	String spl ="";
	private	String ppSg ="";
	private	String annualIncrementId ="";
	private	String gpf ="";
	private	String dateOfRetirement ="";
	private	String presentDepartment ="";
	private	String dutyDepartment ="";
	private	String location ="";
	private	String costCenter ="";
	private String proPostId[];
	private String proFrom[];
	private String proTo[];
	private String proOrderNo[];
	private String proOrderDate[];
	private String regPostId[];
	private String regDate[];
	private String regOrder[];
	private String regOrderDate[];
	private String bank;
	private String deathDate;
	private String branch;
	private String accountNumber;
	private String salaryBank;
	private String designationId[];
	private String[] assignmentOrderNo;
	/*
	 * Added to get Designation Id
	 */
	private String desgId[];
	private String startDateDes[];
	private String endDateDes[];
	private String rangeFrom[];
	private String rangeTo[];
	/*
	 * Added for Position
	 */
	private String posId[];
	private String posName[];
	
	private String dateOfjoin;

//	*********personel information fields ****************
	private String qulification[];
	private String majorSubject[];
	private String monthandYearOfPass[];
	private String universityBoard[];
	private String skillId[];
	private String gradeId1[];
	private String yearOfPassTQ[];

	private String computers;
	private String others;
	private String nameOfTheTestId[];
	private String monthandYearOfPassDT[];

//	*********NOMINATION DETAILS fields ****************
	private String nameOfTheNominee[];
	private String age[];
	private String maritialStatus[];
	private String relationId[];
	private String gpfnd[];
	private String spfgs[];
	private String fbf[];
	private String dcrg[];
	private String pension[];

//	property Details
//	immmovable
	private String propertydiscriptionImm[];
	private String placeImm[];
	private String howAcquiredImm[];
	private String presentValueImm[];
	private String permissionObtainedImm[];
	private String orderNoImm[];
	private String dateImm[];


//	movable
	private String propertydiscriptionMo[];
	private String valueOfTimeOfPurchaseMo[];
	private String howAcquiredMov[];
	private String permissionObtainedMo[];
	private String orderNoMo[];
	private String dateMo[];

//	diciplary
	private String natureOfAlligations ="";
	private String chargeMemoNumber ="";
	private String chargeMemoDate ="";
	private String chargeMemoServedDate ="";
	private String natureOfDisposal ="";
	private	String from ="";
	private	String to ="";
	private	String whetherSuspended ="0";
	private	String absent ="0";
	private	String dos ="";
	private	String dor ="";
   
	private	String subsistence ="0";
	private	String natureOfPunisment ="";
	private	String howSuspention ="";
	private	String punismentOrderDate ="";
	private	String punismenteffectiveDate ="";

//	training pirticulars
	private String course[];
	private String institution[];
	private String city[];
	private String fromTp[];
	private String toTp[];
//	leave pirticulars
	private String elDate[];
	private String noOfDays[];
	private String eLcredit[];
	private String typeOfLeaves[];
	private String maxDays[];
	private String leaveAvailed[];

//	Availed Particulars
	private String bYear[];
	private String leaveTypeAvailed[];
	private String claimed[];
	private String orderNo[];
	private String dateAvailed[];

	//ass
	public String[] fromDate;
	public String[] toDate;
	public String[] fundId;
	public String[] functionId;
    private String dateInput;
	public String[] departmentIdOfHod;
	
	
	public String[] functionaryId;
	public String[] ptcallocation;
	public String[] reportsTo;
    private List employeeList;
    
    private String[] comments;
    private String[] commentDate;
    private String[] reason;
    private String[] serviceDocNo;
    private String[] serviceOrderNo;
    private String[] payScale;
    private String[] eduDocNo;
    private String[] techDocNo;
    
  //added to make an employee active or inactive
    private boolean employeeActiveCheckbox=false;
    
  //added empGrpMstr, which is need for DA calculation
    private String empGrpMstr;

	public String getEmpGrpMstr() {
		return empGrpMstr;
	}
	public void setEmpGrpMstr(String empGrpMstr) {
		this.empGrpMstr = empGrpMstr;
	}
	public boolean getEmployeeActiveCheckbox() {
		return employeeActiveCheckbox;
	}
	public void setEmployeeActiveCheckbox(boolean employeeActiveCheckbox) {
		this.employeeActiveCheckbox = employeeActiveCheckbox;
	}
	/**
	 * @return Returns the fromDate.
	 */
	public String[] getFromDate() {
		return fromDate;
	}
	/**
	 * @param fromDate The fromDate to set.
	 */
	public void setFromDate(String[] fromDate) {
		this.fromDate = fromDate;
	}
	/**
	 * @return Returns the functionaryId.
	 */
	public String[] getFunctionaryId() {
		return functionaryId;
	}
	/**
	 * @param functionaryId The functionaryId to set.
	 */
	public void setFunctionaryId(String[] functionaryId) {
		this.functionaryId = functionaryId;
	}
	/**
	 * @return Returns the functionId.
	 */
	public String[] getFunctionId() {
		return functionId;
	}
	/**
	 * @param functionId The functionId to set.
	 */
	public void setFunctionId(String[] functionId) {
		this.functionId = functionId;
	}
	/**
	 * @return Returns the fundId.
	 */
	public String[] getFundId() {
		return fundId;
	}
	/**
	 * @param fundId The fundId to set.
	 */
	public void setFundId(String[] fundId) {
		this.fundId = fundId;
	}
	/**
	 * @return Returns the ptcallocation.
	 */
	public String[] getPtcallocation() {
		return ptcallocation;
	}
	/**
	 * @param ptcallocation The ptcallocation to set.
	 */
	public void setPtcallocation(String[] ptcallocation) {
		this.ptcallocation = ptcallocation;
	}
	/**
	 * @return Returns the reportsTo.
	 */
	public String[] getReportsTo() {
		return reportsTo;
	}
	/**
	 * @param reportsTo The reportsTo to set.
	 */
	public void setReportsTo(String[] reportsTo) {
		this.reportsTo = reportsTo;
	}
	/**
	 * @return Returns the toDate.
	 */
	public String[] getToDate() {
		return toDate;
	}
	/**
	 * @param toDate The toDate to set.
	 */
	public void setToDate(String[] toDate) {
		this.toDate = toDate;
	}
	/**
	 * @param deptId The deptId to set.
	 */

	/**
	 * @return Returns the punEffectDate.
	 */
	public String getPunEffectDate() {
		return PunEffectDate;
	}
	/**
	 * @param punEffectDate The punEffectDate to set.
	 */
	public void setPunEffectDate(String punEffectDate) {
		PunEffectDate = punEffectDate;
	}
	/**
	 * @return Returns the wsp.
	 */
	public String getWsp() {
		return wsp;
	}
	/**
	 * @param wsp The wsp to set.
	 */
	public void setWsp(String wsp) {
		this.wsp = wsp;
	}
	private	String wsp ="0";

	private	String PunEffectDate ="";






/**
 * @return Returns the absent.
 */
public String getAbsent() {
	return absent;
}
/**
 * @param absent The absent to set.
 */
public void setAbsent(String absent) {
	this.absent = absent;
}
/**
 * @return Returns the accountNumber.
 */
public String getAccountNumber() {
	return accountNumber;
}
/**
 * @param accountNumber The accountNumber to set.
 */
public void setAccountNumber(String accountNumber) {
	this.accountNumber = accountNumber;
}
/**
 * @return Returns the age.
 */
public String[] getAge() {
	return age;
}
/**
 * @param age The age to set.
 */
public void setAge(String[] age) {
	this.age = age;
}
/**
 * @return Returns the bank.
 */
public String getBank() {
	return bank;
}
/**
 * @param bank The bank to set.
 */
public void setBank(String bank) {
	this.bank = bank;
}
/**
 * @return Returns the bloodGroup.
 */
public String getBloodGroup() {
	return bloodGroup;
}
/**
 * @param bloodGroup The bloodGroup to set.
 */
public void setBloodGroup(String bloodGroup) {
	this.bloodGroup = bloodGroup;
}
/**
 * @return Returns the branch.
 */
public String getBranch() {
	return branch;
}
/**
 * @param branch The branch to set.
 */
public void setBranch(String branch) {
	this.branch = branch;
}
/**
 * @return Returns the bYear.
 */
public String[] getBYear() {
	return bYear;
}
/**
 * @param year The bYear to set.
 */
public void setBYear(String[] year) {
	this.bYear = year;
}
/**
 * @return Returns the category.
 */
public String getCategory() {
	return category;
}
/**
 * @param category The category to set.
 */
public void setCategory(String category) {
	this.category = category;
}
/**
 * @return Returns the chargeMemoDate.
 */
public String getChargeMemoDate() {
	return chargeMemoDate;
}
/**
 * @param chargeMemoDate The chargeMemoDate to set.
 */
public void setChargeMemoDate(String chargeMemoDate) {
	this.chargeMemoDate = chargeMemoDate;
}
/**
 * @return Returns the chargeMemoNumber.
 */
public String getChargeMemoNumber() {
	return chargeMemoNumber;
}
/**
 * @param chargeMemoNumber The chargeMemoNumber to set.
 */
public void setChargeMemoNumber(String chargeMemoNumber) {
	this.chargeMemoNumber = chargeMemoNumber;
}
/**
 * @return Returns the chargeMemoServedDate.
 */
public String getChargeMemoServedDate() {
	return chargeMemoServedDate;
}
/**
 * @param chargeMemoServedDate The chargeMemoServedDate to set.
 */
public void setChargeMemoServedDate(String chargeMemoServedDate) {
	this.chargeMemoServedDate = chargeMemoServedDate;
}
/**
 * @return Returns the city.
 */
public String[] getCity() {
	return city;
}
/**
 * @param city The city to set.
 */
public void setCity(String[] city) {
	this.city = city;
}
/**
 * @return Returns the cityper.
 */
public String getCityper() {
	return cityper;
}
/**
 * @param cityper The cityper to set.
 */
public void setCityper(String cityper) {
	this.cityper = cityper;
}
/**
 * @return Returns the citypre.
 */
public String getCitypre() {
	return citypre;
}
/**
 * @param citypre The citypre to set.
 */
public void setCitypre(String citypre) {
	this.citypre = citypre;
}
/**
 * @return Returns the claimed.
 */
public String[] getClaimed() {
	return claimed;
}
/**
 * @param claimed The claimed to set.
 */
public void setClaimed(String[] claimed) {
	this.claimed = claimed;
}
/**
 * @return Returns the communityId.
 */
public String getCommunityId() {
	return communityId;
}
/**
 * @param communityId The communityId to set.
 */
public void setCommunityId(String communityId) {
	this.communityId = communityId;
}
/**
 * @return Returns the costCenter.
 */
public String getCostCenter() {
	return costCenter;
}
/**
 * @param costCenter The costCenter to set.
 */
public void setCostCenter(String costCenter) {
	this.costCenter = costCenter;
}
/**
 * @return Returns the course.
 */
public String[] getCourse() {
	return course;
}
/**
 * @param course The course to set.
 */
public void setCourse(String[] course) {
	this.course = course;
}
/**
 * @return Returns the dateAvailed.
 */
public String[] getDateAvailed() {
	return dateAvailed;
}
/**
 * @param dateAvailed The dateAvailed to set.
 */
public void setDateAvailed(String[] dateAvailed) {
	this.dateAvailed = dateAvailed;
}
/**
 * @return Returns the dateImm.
 */
public String[] getDateImm() {
	return dateImm;
}
/**
 * @param dateImm The dateImm to set.
 */
public void setDateImm(String[] dateImm) {
	this.dateImm = dateImm;
}
/**
 * @return Returns the dateMo.
 */
public String[] getDateMo() {
	return dateMo;
}
/**
 * @param dateMo The dateMo to set.
 */
public void setDateMo(String[] dateMo) {
	this.dateMo = dateMo;
}
/**
 * @return Returns the dateOfFA.
 */
public String getDateOfFA() {
	return dateOfFA;
}
/**
 * @param dateOfFA The dateOfFA to set.
 */
public void setDateOfFA(String dateOfFA) {
	this.dateOfFA = dateOfFA;
}
/**
 * @return Returns the dateOfRetirement.
 */
public String getDateOfRetirement() {
	return dateOfRetirement;
}
/**
 * @param dateOfRetirement The dateOfRetirement to set.
 */
public void setDateOfRetirement(String dateOfRetirement) {
	this.dateOfRetirement = dateOfRetirement;
}
/**
 * @return Returns the dcrg.
 */
public String[] getDcrg() {
	return dcrg;
}
/**
 * @param dcrg The dcrg to set.
 */
public void setDcrg(String[] dcrg) {
	this.dcrg = dcrg;
}
/**
 * @return Returns the designationId.
 */
public String[] getDesignationId() {
	return designationId;
}
/**
 * @param designationId The designationId to set.
 */
public void setDesignationId(String[] designationId) {
	this.designationId = designationId;
}
/**
 * @return Returns the dor.
 */
public String getDor() {
	return dor;
}
/**
 * @param dor The dor to set.
 */
public void setDor(String dor) {
	this.dor = dor;
}
/**
 * @return Returns the dos.
 */
public String getDos() {
	return dos;
}
/**
 * @param dos The dos to set.
 */
public void setDos(String dos) {
	this.dos = dos;
}
/**
 * @return Returns the dutyDepartment.
 */
public String getDutyDepartment() {
	return dutyDepartment;
}
/**
 * @param dutyDepartment The dutyDepartment to set.
 */
public void setDutyDepartment(String dutyDepartment) {
	this.dutyDepartment = dutyDepartment;
}
/**
 * @return Returns the eLcredit.
 */
public String[] getELcredit() {
	return eLcredit;
}
/**
 * @param lcredit The eLcredit to set.
 */
public void setELcredit(String[] eLcredit) {
	this.eLcredit = eLcredit;
}
/**
 * @return Returns the elDate.
 */
public String[] getElDate() {
	return elDate;
}
/**
 * @param elDate The elDate to set.
 */
public void setElDate(String[] elDate) {
	this.elDate = elDate;
}
/**
 * @return Returns the employeeCode.
 */
public String getEmployeeCode() {
	return employeeCode;
}
/**
 * @param employeeCode The employeeCode to set.
 */
public void setEmployeeCode(String employeeCode) {
	this.employeeCode = employeeCode;
}
/**
 * @return Returns the employeeDob.
 */
public String getEmployeeDob() {
	return employeeDob;
}
/**
 * @param employeeDob The employeeDob to set.
 */
public void setEmployeeDob(String employeeDob) {
	this.employeeDob = employeeDob;
}
public String getPanNumber() {
	return panNumber;
}
/**
 * @param employeeDob The employeeDob to set.
 */
public void setPanNumber(String panNumber) {
	this.panNumber = panNumber;
}

/**
 * @return Returns the endDateDes.
 */
public String[] getEndDateDes() {
	return endDateDes;
}
/**
 * @param endDateDes The endDateDes to set.
 */
public void setEndDateDes(String[] endDateDes) {
	this.endDateDes = endDateDes;
}

/**
 * @return Returns the fatherfirstName.
 */
public String getFatherfirstName() {
	return fatherfirstName;
}
/**
 * @param fatherfirstName The fatherfirstName to set.
 */
public void setFatherfirstName(String fatherfirstName) {
	this.fatherfirstName = fatherfirstName;
}
	/**
	 * @return Returns the enquiryOfficeName.
	 */
	public String[] getEnquiryOfficeName() {
		return enquiryOfficeName;
	}
	/**
	 * @param enquiryOfficeName The enquiryOfficeName to set.
	 */
	public void setEnquiryOfficeName(String[] enquiryOfficeName) {
		this.enquiryOfficeName = enquiryOfficeName;
	}
	/**
	 * @return Returns the eoDesignation.
	 */
	public String[] getEoDesignation() {
		return eoDesignation;
	}
	/**
	 * @param eoDesignation The eoDesignation to set.
	 */
	public void setEoDesignation(String[] eoDesignation) {
		this.eoDesignation = eoDesignation;
	}
	/**
	 * @return Returns the eoNoDate.
	 */
	public String[] getEoNoDate() {
		return eoNoDate;
	}
	/**
	 * @param eoNoDate The eoNoDate to set.
	 */
	public void setEoNoDate(String[] eoNoDate) {
		this.eoNoDate = eoNoDate;
	}
	/**
	 * @return Returns the eoReDate.
	 */
	public String[] getEoReDate() {
		return eoReDate;
	}
	/**
	 * @param eoReDate The eoReDate to set.
	 */
	public void setEoReDate(String[] eoReDate) {
		this.eoReDate = eoReDate;
	}
/**
 * @return Returns the fatherlastName.
 */
public String getFatherlastName() {
	return fatherlastName;
}
/**
 * @param fatherlastName The fatherlastName to set.
 */
public void setFatherlastName(String fatherlastName) {
	this.fatherlastName = fatherlastName;
}
/**
 * @return Returns the fathermiddleName.
 */
public String getFathermiddleName() {
	return fathermiddleName;
}
/**
 * @param fathermiddleName The fathermiddleName to set.
 */
public void setFathermiddleName(String fathermiddleName) {
	this.fathermiddleName = fathermiddleName;
}
/**
 * @return Returns the fbf.
 */
public String[] getFbf() {
	return fbf;
}
/**
 * @param fbf The fbf to set.
 */
public void setFbf(String[] fbf) {
	this.fbf = fbf;
}
/**
 * @return Returns the firstName.
 */
public String getFirstName() {
	return firstName;
}
/**
 * @param firstName The firstName to set.
 */
public void setFirstName(String firstName) {
	this.firstName = firstName;
}
/**
 * @return Returns the from.
 */
public String getFrom() {
	return from;
}
/**
 * @param from The from to set.
 */
public void setFrom(String from) {
	this.from = from;
}
/**
 * @return Returns the fromTp.
 */
public String[] getFromTp() {
	return fromTp;
}
/**
 * @param fromTp The fromTp to set.
 */
public void setFromTp(String[] fromTp) {
	this.fromTp = fromTp;
}
/**
 * @return Returns the gpf.
 */
public String getGpf() {
	return gpf;
}
/**
 * @param gpf The gpf to set.
 */
public void setGpf(String gpf) {
	this.gpf = gpf;
}
/**
 * @return Returns the gpfnd.
 */
public String[] getGpfnd() {
	return gpfnd;
}
/**
 * @param gpfnd The gpfnd to set.
 */
public void setGpfnd(String[] gpfnd) {
	this.gpfnd = gpfnd;
}

/**
 * @return Returns the howAcquiredImm.
 */
public String[] getHowAcquiredImm() {
	return howAcquiredImm;
}
/**
 * @param howAcquiredImm The howAcquiredImm to set.
 */
public void setHowAcquiredImm(String[] howAcquiredImm) {
	this.howAcquiredImm = howAcquiredImm;
}
/**
 * @return Returns the howAcquiredMov.
 */
public String[] getHowAcquiredMov() {
	return howAcquiredMov;
}
/**
 * @param howAcquiredMov The howAcquiredMov to set.
 */
public void setHowAcquiredMov(String[] howAcquiredMov) {
	this.howAcquiredMov = howAcquiredMov;
}
/**
 * @return Returns the howSuspention.
 */
public String getHowSuspention() {
	return howSuspention;
}
/**
 * @param howSuspention The howSuspention to set.
 */
public void setHowSuspention(String howSuspention) {
	this.howSuspention = howSuspention;
}
/**
 * @return Returns the identificationMarks1.
 */
public String getIdentificationMarks1() {
	return identificationMarks1;
}
/**
 * @param identificationMarks1 The identificationMarks1 to set.
 */
public void setIdentificationMarks1(String identificationMarks1) {
	this.identificationMarks1 = identificationMarks1;
}
/**
 * @return Returns the identificationMarks2.
 */
public String getIdentificationMarks2() {
	return identificationMarks2;
}
/**
 * @param identificationMarks2 The identificationMarks2 to set.
 */
public void setIdentificationMarks2(String identificationMarks2) {
	this.identificationMarks2 = identificationMarks2;
}
/**
 * @return Returns the institution.
 */
public String[] getInstitution() {
	return institution;
}
/**
 * @param institution The institution to set.
 */
public void setInstitution(String[] institution) {
	this.institution = institution;
}
/**
 * @return Returns the isMed.
 */
public String getIsMed() {
	return isMed;
}
/**
 * @param isMed The isMed to set.
 */
public void setIsMed(String isMed) {
	this.isMed = isMed;
}
/**
 * @return Returns the lanId.
 */
public String getLanId() {
	return lanId;
}
/**
 * @param lanId The lanId to set.
 */
public void setLanId(String lanId) {
	this.lanId = lanId;
}
/**
 * @return Returns the lastName.
 */
public String getLastName() {
	return lastName;
}
/**
 * @param lastName The lastName to set.
 */
public void setLastName(String lastName) {
	this.lastName = lastName;
}
/**
 * @return Returns the leaveAvailed.
 */
public String[] getLeaveAvailed() {
	return leaveAvailed;
}
/**
 * @param leaveAvailed The leaveAvailed to set.
 */
public void setLeaveAvailed(String[] leaveAvailed) {
	this.leaveAvailed = leaveAvailed;
}
/**
 * @return Returns the leaveTypeAvailed.
 */
public String[] getLeaveTypeAvailed() {
	return leaveTypeAvailed;
}
/**
 * @param leaveTypeAvailed The leaveTypeAvailed to set.
 */
public void setLeaveTypeAvailed(String[] leaveTypeAvailed) {
	this.leaveTypeAvailed = leaveTypeAvailed;
}
/**
 * @return Returns the localityper.
 */
public String getLocalityper() {
	return localityper;
}
/**
 * @param localityper The localityper to set.
 */
public void setLocalityper(String localityper) {
	this.localityper = localityper;
}
/**
 * @return Returns the localitypre.
 */
public String getLocalitypre() {
	return localitypre;
}
/**
 * @param localitypre The localitypre to set.
 */
public void setLocalitypre(String localitypre) {
	this.localitypre = localitypre;
}
/**
 * @return Returns the location.
 */
public String getLocation() {
	return location;
}
/**
 * @param location The location to set.
 */
public void setLocation(String location) {
	this.location = location;
}
/**
 * @return Returns the majorSubject.
 */
public String[] getMajorSubject() {
	return majorSubject;
}
/**
 * @param majorSubject The majorSubject to set.
 */
public void setMajorSubject(String[] majorSubject) {
	this.majorSubject = majorSubject;
}
/**
 * @return Returns the maritialStatus.
 */
public String[] getMaritialStatus() {
	return maritialStatus;
}
/**
 * @param maritialStatus The maritialStatus to set.
 */
public void setMaritialStatus(String[] maritialStatus) {
	this.maritialStatus = maritialStatus;
}
/**
 * @return Returns the maxDays.
 */
public String[] getMaxDays() {
	return maxDays;
}
/**
 * @param maxDays The maxDays to set.
 */
public void setMaxDays(String[] maxDays) {
	this.maxDays = maxDays;
}
/**
 * @return Returns the middleName.
 */
public String getMiddleName() {
	return middleName;
}
/**
 * @param middleName The middleName to set.
 */
public void setMiddleName(String middleName) {
	this.middleName = middleName;
}
/**
 * @return Returns the monthandYearOfPass.
 */
public String[] getMonthandYearOfPass() {
	return monthandYearOfPass;
}
/**
 * @param monthandYearOfPass The monthandYearOfPass to set.
 */
public void setMonthandYearOfPass(String[] monthandYearOfPass) {
	this.monthandYearOfPass = monthandYearOfPass;
}
/**
 * @return Returns the monthandYearOfPassDT.
 */
public String[] getMonthandYearOfPassDT() {
	return monthandYearOfPassDT;
}
/**
 * @param monthandYearOfPassDT The monthandYearOfPassDT to set.
 */
public void setMonthandYearOfPassDT(String[] monthandYearOfPassDT) {
	this.monthandYearOfPassDT = monthandYearOfPassDT;
}
/**
 * @return Returns the mOrId.
 */
public String getMOrId() {
	return mOrId;
}
/**
 * @param orId The mOrId to set.
 */
public void setMOrId(String mOrId) {
	this.mOrId = mOrId;
}
/**
 * @return Returns the motherTounge.
 */
public String getMotherTounge() {
	return motherTounge;
}
/**
 * @param motherTounge The motherTounge to set.
 */
public void setMotherTounge(String motherTounge) {
	this.motherTounge = motherTounge;
}
/**
 * @return Returns the nameOfTheNominee.
 */
public String[] getNameOfTheNominee() {
	return nameOfTheNominee;
}
/**
 * @param nameOfTheNominee The nameOfTheNominee to set.
 */
public void setNameOfTheNominee(String[] nameOfTheNominee) {
	this.nameOfTheNominee = nameOfTheNominee;
}
/**
 * @return Returns the nameOfTheTestId.
 */
public String[] getNameOfTheTestId() {
	return nameOfTheTestId;
}
/**
 * @param nameOfTheTestId The nameOfTheTestId to set.
 */
public void setNameOfTheTestId(String[] nameOfTheTestId) {
	this.nameOfTheTestId = nameOfTheTestId;
}
/**
 * @return Returns the natureOfAlligations.
 */
public String getNatureOfAlligations() {
	return natureOfAlligations;
}
/**
 * @param natureOfAlligations The natureOfAlligations to set.
 */
public void setNatureOfAlligations(String natureOfAlligations) {
	this.natureOfAlligations = natureOfAlligations;
}
/**
 * @return Returns the natureOfDisposal.
 */
public String getNatureOfDisposal() {
	return natureOfDisposal;
}
/**
 * @param natureOfDisposal The natureOfDisposal to set.
 */
public void setNatureOfDisposal(String natureOfDisposal) {
	this.natureOfDisposal = natureOfDisposal;
}
/**
 * @return Returns the natureOfPunisment.
 */
public String getNatureOfPunisment() {
	return natureOfPunisment;
}
/**
 * @param natureOfPunisment The natureOfPunisment to set.
 */
public void setNatureOfPunisment(String natureOfPunisment) {
	this.natureOfPunisment = natureOfPunisment;
}
/**
 * @return Returns the noOfDays.
 */
public String[] getNoOfDays() {
	return noOfDays;
}
/**
 * @param noOfDays The noOfDays to set.
 */
public void setNoOfDays(String[] noOfDays) {
	this.noOfDays = noOfDays;
}
/**
 * @return Returns the orderNo.
 */
public String[] getOrderNo() {
	return orderNo;
}
/**
 * @param orderNo The orderNo to set.
 */
public void setOrderNo(String[] orderNo) {
	this.orderNo = orderNo;
}
/**
 * @return Returns the orderNoImm.
 */
public String[] getOrderNoImm() {
	return orderNoImm;
}
/**
 * @param orderNoImm The orderNoImm to set.
 */
public void setOrderNoImm(String[] orderNoImm) {
	this.orderNoImm = orderNoImm;
}
/**
 * @return Returns the orderNoMo.
 */
public String[] getOrderNoMo() {
	return orderNoMo;
}
/**
 * @param orderNoMo The orderNoMo to set.
 */
public void setOrderNoMo(String[] orderNoMo) {
	this.orderNoMo = orderNoMo;
}
/**
 * @return Returns the payFixIn.
 */
public String getPayFixIn() {
	return payFixIn;
}
/**
 * @param payFixIn The payFixIn to set.
 */
public void setPayFixIn(String payFixIn) {
	this.payFixIn = payFixIn;
}
/**
 * @return Returns the pension.
 */
public String[] getPension() {
	return pension;
}
/**
 * @param pension The pension to set.
 */
public void setPension(String[] pension) {
	this.pension = pension;
}
/**
 * @return Returns the permissionObtainedImm.
 */
public String[] getPermissionObtainedImm() {
	return permissionObtainedImm;
}
/**
 * @param permissionObtainedImm The permissionObtainedImm to set.
 */
public void setPermissionObtainedImm(String[] permissionObtainedImm) {
	this.permissionObtainedImm = permissionObtainedImm;
}
/**
 * @return Returns the permissionObtainedMo.
 */
public String[] getPermissionObtainedMo() {
	return permissionObtainedMo;
}
/**
 * @param permissionObtainedMo The permissionObtainedMo to set.
 */
public void setPermissionObtainedMo(String[] permissionObtainedMo) {
	this.permissionObtainedMo = permissionObtainedMo;
}
/**
 * @return Returns the phand.
 */
public String getPhand() {
	return phand;
}
/**
 * @param phand The phand to set.
 */
public void setPhand(String phand) {
	this.phand = phand;
}
/**
 * @return Returns the pinCodeper.
 */
public String getPinCodeper() {
	return pinCodeper;
}
/**
 * @param pinCodeper The pinCodeper to set.
 */
public void setPinCodeper(String pinCodeper) {
	this.pinCodeper = pinCodeper;
}
/**
 * @return Returns the pinCodepre.
 */
public String getPinCodepre() {
	return pinCodepre;
}
/**
 * @param pinCodepre The pinCodepre to set.
 */
public void setPinCodepre(String pinCodepre) {
	this.pinCodepre = pinCodepre;
}
/**
 * @return Returns the placeImm.
 */
public String[] getPlaceImm() {
	return placeImm;
}
/**
 * @param placeImm The placeImm to set.
 */
public void setPlaceImm(String[] placeImm) {
	this.placeImm = placeImm;
}
/**
 * @return Returns the ppSg.
 */
public String getPpSg() {
	return ppSg;
}
/**
 * @param ppSg The ppSg to set.
 */
public void setPpSg(String ppSg) {
	this.ppSg = ppSg;
}
/**
 * @return Returns the presentDepartment.
 */
public String getPresentDepartment() {
	return presentDepartment;
}
/**
 * @param presentDepartment The presentDepartment to set.
 */
public void setPresentDepartment(String presentDepartment) {
	this.presentDepartment = presentDepartment;
}
/**
 * @return Returns the presentValueImm.
 */
public String[] getPresentValueImm() {
	return presentValueImm;
}
/**
 * @param presentValueImm The presentValueImm to set.
 */
public void setPresentValueImm(String[] presentValueImm) {
	this.presentValueImm = presentValueImm;
}
/**
 * @return Returns the proFrom.
 */
public String[] getProFrom() {
	return proFrom;
}
/**
 * @param proFrom The proFrom to set.
 */
public void setProFrom(String[] proFrom) {
	this.proFrom = proFrom;
}

/**
 * @return Returns the proTo.
 */
public String[] getProTo() {
	return proTo;
}
/**
 * @param proTo The proTo to set.
 */
public void setProTo(String[] proTo) {
	this.proTo = proTo;
}
/**
 * @return Returns the proOrderDate.
 */
public String[] getProOrderDate() {
	return proOrderDate;
}
/**
 * @param proOrderDate The proOrderDate to set.
 */
public void setProOrderDate(String[] proOrderDate) {
	this.proOrderDate = proOrderDate;
}
/**
 * @return Returns the proOrderNo.
 */
public String[] getProOrderNo() {
	return proOrderNo;
}
/**
 * @param proOrderNo The proOrderNo to set.
 */
public void setProOrderNo(String[] proOrderNo) {
	this.proOrderNo = proOrderNo;
}
/**
 * @return Returns the propertydiscriptionImm.
 */
public String[] getPropertydiscriptionImm() {
	return propertydiscriptionImm;
}
/**
 * @param propertydiscriptionImm The propertydiscriptionImm to set.
 */
public void setPropertydiscriptionImm(String[] propertydiscriptionImm) {
	this.propertydiscriptionImm = propertydiscriptionImm;
}
/**
 * @return Returns the propertydiscriptionMo.
 */
public String[] getPropertydiscriptionMo() {
	return propertydiscriptionMo;
}
/**
 * @param propertydiscriptionMo The propertydiscriptionMo to set.
 */
public void setPropertydiscriptionMo(String[] propertydiscriptionMo) {
	this.propertydiscriptionMo = propertydiscriptionMo;
}
/**
 * @return Returns the propertyNoper.
 */
public String getPropertyNoper() {
	return propertyNoper;
}
/**
 * @param propertyNoper The propertyNoper to set.
 */
public void setPropertyNoper(String propertyNoper) {
	this.propertyNoper = propertyNoper;
}
/**
 * @return Returns the propertyNopre.
 */
public String getPropertyNopre() {
	return propertyNopre;
}
/**
 * @param propertyNopre The propertyNopre to set.
 */
public void setPropertyNopre(String propertyNopre) {
	this.propertyNopre = propertyNopre;
}
/**
 * @return Returns the proPostId.
 */
public String[] getProPostId() {
	return proPostId;
}
/**
 * @param proPostId The proPostId to set.
 */
public void setProPostId(String[] proPostId) {
	this.proPostId = proPostId;
}
/**
 * @return Returns the punismenteffectiveDate.
 */
public String getPunismenteffectiveDate() {
	return punismenteffectiveDate;
}
/**
 * @param punismenteffectiveDate The punismenteffectiveDate to set.
 */
public void setPunismenteffectiveDate(String punismenteffectiveDate) {
	this.punismenteffectiveDate = punismenteffectiveDate;
}
/**
 * @return Returns the punismentOrderDate.
 */
public String getPunismentOrderDate() {
	return punismentOrderDate;
}
/**
 * @param punismentOrderDate The punismentOrderDate to set.
 */
public void setPunismentOrderDate(String punismentOrderDate) {
	this.punismentOrderDate = punismentOrderDate;
}
/**
 * @return Returns the qulification.
 */
public String[] getQulification() {
	return qulification;
}
/**
 * @param qulification The qulification to set.
 */
public void setQulification(String[] qulification) {
	this.qulification = qulification;
}
/**
 * @return Returns the rangeFrom.
 */
public String[] getRangeFrom() {
	return rangeFrom;
}
/**
 * @param rangeFrom The rangeFrom to set.
 */
public void setRangeFrom(String[] rangeFrom) {
	this.rangeFrom = rangeFrom;
}
/**
 * @return Returns the rangeTo.
 */
public String[] getRangeTo() {
	return rangeTo;
}
/**
 * @param rangeTo The rangeTo to set.
 */
public void setRangeTo(String[] rangeTo) {
	this.rangeTo = rangeTo;
}
/**
 * @return Returns the recruitmentTypeId.
 */
public String getRecruitmentTypeId() {
	return recruitmentTypeId;
}
/**
 * @param recruitmentTypeId The recruitmentTypeId to set.
 */
public void setRecruitmentTypeId(String recruitmentTypeId) {
	this.recruitmentTypeId = recruitmentTypeId;
}
/**
 * @return Returns the regDate.
 */
public String[] getRegDate() {
	return regDate;
}
/**
 * @param regDate The regDate to set.
 */
public void setRegDate(String[] regDate) {
	this.regDate = regDate;
}
/**
 * @return Returns the regOrder.
 */
public String[] getRegOrder() {
	return regOrder;
}
/**
 * @param regOrder The regOrder to set.
 */
public void setRegOrder(String[] regOrder) {
	this.regOrder = regOrder;
}
/**
 * @return Returns the regOrderDate.
 */
public String[] getRegOrderDate() {
	return regOrderDate;
}
/**
 * @param regOrderDate The regOrderDate to set.
 */
public void setRegOrderDate(String[] regOrderDate) {
	this.regOrderDate = regOrderDate;
}
/**
 * @return Returns the regPostId.
 */
public String[] getRegPostId() {
	return regPostId;
}
/**
 * @param regPostId The regPostId to set.
 */
public void setRegPostId(String[] regPostId) {
	this.regPostId = regPostId;
}
/**
 * @return Returns the relationId.
 */
public String[] getRelationId() {
	return relationId;
}
/**
 * @param relationId The relationId to set.
 */
public void setRelationId(String[] relationId) {
	this.relationId = relationId;
}
/**
 * @return Returns the religionId.
 */
public String getReligionId() {
	return religionId;
}
/**
 * @param religionId The religionId to set.
 */
public void setReligionId(String religionId) {
	this.religionId = religionId;
}

/**
 * @return Returns the spfgs.
 */
public String[] getSpfgs() {
	return spfgs;
}
/**
 * @param spfgs The spfgs to set.
 */
public void setSpfgs(String[] spfgs) {
	this.spfgs = spfgs;
}
/**
 * @return Returns the spl.
 */
public String getSpl() {
	return spl;
}
/**
 * @param spl The spl to set.
 */
public void setSpl(String spl) {
	this.spl = spl;
}
/**
 * @return Returns the startDateDes.
 */
public String[] getStartDateDes() {
	return startDateDes;
}
/**
 * @param startDateDes The startDateDes to set.
 */
public void setStartDateDes(String[] startDateDes) {
	this.startDateDes = startDateDes;
}

/**
 * @return Returns the status.
 */
public String getStatus() {
	return status;
}
/**
 * @param status The status to set.
 */
public void setStatus(String status) {
	this.status = status;
}
/**
 * @return Returns the streetNameper.
 */
public String getStreetNameper() {
	return StreetNameper;
}
/**
 * @param streetNameper The streetNameper to set.
 */
public void setStreetNameper(String StreetNameper) {
	this.StreetNameper = StreetNameper;
}
/**
 * @return Returns the streetNamepre.
 */
public String getStreetNamepre() {
	return StreetNamepre;
}
/**
 * @param streetNamepre The streetNamepre to set.
 */
public void setStreetNamepre(String StreetNamepre) {
	this.StreetNamepre = StreetNamepre;
}
/**
 * @return Returns the subsistence.
 */
public String getSubsistence() {
	return subsistence;
}
/**
 * @param subsistence The subsistence to set.
 */
public void setSubsistence(String subsistence) {
	this.subsistence = subsistence;
}
/**
 * @return Returns the tamillangaugequlified.
 */
public String getTamillangaugequlified() {
	return tamillangaugequlified;
}
/**
 * @param tamillangaugequlified The tamillangaugequlified to set.
 */
public void setTamillangaugequlified(String tamillangaugequlified) {
	this.tamillangaugequlified = tamillangaugequlified;
}
/**
 * @return Returns the to.
 */
public String getTo() {
	return to;
}
/**
 * @param to The to to set.
 */
public void setTo(String to) {
	this.to = to;
}
/**
 * @return Returns the toTp.
 */
public String[] getToTp() {
	return toTp;
}
/**
 * @param toTp The toTp to set.
 */
public void setToTp(String[] toTp) {
	this.toTp = toTp;
}
/**
 * @return Returns the typeOfLeaves.
 */
public String[] getTypeOfLeaves() {
	return typeOfLeaves;
}
/**
 * @param typeOfLeaves The typeOfLeaves to set.
 */
public void setTypeOfLeaves(String[] typeOfLeaves) {
	this.typeOfLeaves = typeOfLeaves;
}
/**
 * @return Returns the typewritingEnglish.
 */
/**
 * @return Returns the universityBoard.
 */
public String[] getUniversityBoard() {
	return universityBoard;
}
/**
 * @param universityBoard The universityBoard to set.
 */
public void setUniversityBoard(String[] universityBoard) {
	this.universityBoard = universityBoard;
}
/**
 * @return Returns the valueOfTimeOfPurchaseMo.
 */
public String[] getValueOfTimeOfPurchaseMo() {
	return valueOfTimeOfPurchaseMo;
}
/**
 * @param valueOfTimeOfPurchaseMo The valueOfTimeOfPurchaseMo to set.
 */
public void setValueOfTimeOfPurchaseMo(String[] valueOfTimeOfPurchaseMo) {
	this.valueOfTimeOfPurchaseMo = valueOfTimeOfPurchaseMo;
}
/**
 * @return Returns the whetherSuspended.
 */
public String getWhetherSuspended() {
	return whetherSuspended;
}
/**
 * @param whetherSuspended The whetherSuspended to set.
 */
public void setWhetherSuspended(String whetherSuspended) {
	this.whetherSuspended = whetherSuspended;
}
	/**
	 * @return Returns the basic.
	 */
	public String getBasic() {
		return basic;
	}
	/**
	 * @param basic The basic to set.
	 */
	public void setBasic(String basic) {
		this.basic = basic;
	}
	/**
	 * @return Returns the annualIncrementId.
	 */
	public String getAnnualIncrementId() {
		return annualIncrementId;
	}
	/**
	 * @param annualIncrementId The annualIncrementId to set.
	 */
	public void setAnnualIncrementId(String annualIncrementId) {
		this.annualIncrementId = annualIncrementId;
	}

	/**
	 * @return Returns the salaryBank.
	 */
	public String getSalaryBank() {
		return salaryBank;
	}
	/**
	 * @param salaryBank The salaryBank to set.
	 */
	public void setSalaryBank(String salaryBank) {
		this.salaryBank = salaryBank;
	}

	/**
	 * @return Returns the computers.
	 */
	public String getComputers() {
		return computers;
	}
	/**
	 * @param computers The computers to set.
	 */
	public void setComputers(String computers) {
		this.computers = computers;
	}
	/**
	 * @return Returns the others.
	 */
	public String getOthers() {
		return others;
	}
	/**
	 * @param others The others to set.
	 */
	public void setOthers(String others) {
		this.others = others;
	}
	/**
	 * @return Returns the skillId.
	 */
	public String[] getSkillId() {
		return skillId;
	}
	/**
	 * @param skillId The skillId to set.
	 */
	public void setSkillId(String[] skillId) {
		this.skillId = skillId;
	}
	/**
	 * @return Returns the yearOfPassTQ.
	 */
	public String[] getYearOfPassTQ() {
		return yearOfPassTQ;
	}
	/**
	 * @param yearOfPassTQ The yearOfPassTQ to set.
	 */
	public void setYearOfPassTQ(String[] yearOfPassTQ) {
		this.yearOfPassTQ = yearOfPassTQ;
	}
	/**
	 * @return Returns the gradeId1.
	 */
	public String[] getGradeId1() {
		return gradeId1;
	}
	/**
	 * @param gradeId1 The gradeId1 to set.
	 */
	public void setGradeId1(String[] gradeId1) {
		this.gradeId1 = gradeId1;
	}
	/**
	 * @return Returns the deptId.
	 */
	public String getDeptId() {
		return deptId;
	}
	/**
	 * @param deptId The deptId to set.
	 */
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	/**
	 * @return Returns the userStatus.
	 */
	public String getUserStatus() {
		return userStatus;
	}
	/**
	 * @param userStatus The userStatus to set.
	 */
	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}
	
	public String[] getDepartmentIdOfHod() {
		return departmentIdOfHod;
	}
	public void setDepartmentIdOfHod(String[] departmentIdOfHod) {
		this.departmentIdOfHod = departmentIdOfHod;
	}
	public String[] getRemarks() {
		return remarks;
	}
	public void setRemarks(String[] remarks) {
		this.remarks = remarks;
	}
	public String getSanctionNo() {
		return sanctionNo;
	}
	public void setSanctionNo(String sanctionNo) {
		this.sanctionNo = sanctionNo;
	}
	public String getDispId() {
		return dispId;
	}
	public void setDispId(String dispId) {
		this.dispId = dispId;
	}
	public String getStatusId() {
		return statusId;
	}
	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}
	public String getApplicationNumber() {
		return applicationNumber;
	}
	public void setApplicationNumber(String applicationNumber) {
		this.applicationNumber = applicationNumber;
	}
	public String getAmountFrom() {
		return amountFrom;
	}
	public void setAmountFrom(String amountFrom) {
		this.amountFrom = amountFrom;
	}
	public String getAmountTo() {
		return amountTo;
	}
	public void setAmountTo(String amountTo) {
		this.amountTo = amountTo;
	}
	public String getEffictiveFrom() {
		return effictiveFrom;
	}
	public void setEffictiveFrom(String effictiveFrom) {
		this.effictiveFrom = effictiveFrom;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPayCommition() {
		return payCommition;
	}
	public void setPayCommition(String payCommition) {
		this.payCommition = payCommition;
	}
	public String getPayHeadId() {
		return payHeadId;
	}
	public void setPayHeadId(String payHeadId) {
		this.payHeadId = payHeadId;
	}
	public String getStatusMaster() {
		return statusMaster;
	}
	public void setStatusMaster(String statusMaster) {
		this.statusMaster = statusMaster;
	}
	public String[] getPayScaleHeader() {
		return payScaleHeader;
	}
	public void setPayScaleHeader(String[] payScaleHeader) {
		this.payScaleHeader = payScaleHeader;
	}
	public String[] getEffDate() {
		return effDate;
	}
	public void setEffDate(String[] effDate) {
		this.effDate = effDate;
	}
	public String[] getAnnualIncrDate() {
		return annualIncrDate;
	}
	public void setAnnualIncrDate(String[] annualIncrDate) {
		this.annualIncrDate = annualIncrDate;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public String getUserFirstName() {
		return userFirstName;
	}
	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}
	public String getUserLastName() {
		return userLastName;
	}
	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}
	public String getUserMiddleName() {
		return userMiddleName;
	}
	public void setUserMiddleName(String userMiddleName) {
		this.userMiddleName = userMiddleName;
	}
	public String getMainDepartmentId() {
		return mainDepartmentId;
	}
	public void setMainDepartmentId(String mainDepartmentId) {
		this.mainDepartmentId = mainDepartmentId;
	}
	public String getRetirementAge() {
		return retirementAge;
	}
	public void setRetirementAge(String retirementAge) {
		this.retirementAge = retirementAge;
	}
	
	public String getMaturityDate() {
		return maturityDate;
	}
	public void setMaturityDate(String maturityDate) {
		this.maturityDate = maturityDate;
	}
	public String[] getCurrBasicPay() {
		return currBasicPay;
	}
	public void setCurrBasicPay(String[] currBasicPay) {
		this.currBasicPay = currBasicPay;
	}
	public String getModifyremarks() {
		return modifyremarks;
	}
	public void setModifyremarks(String modifyremarks) {
		this.modifyremarks = modifyremarks;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String[] getEmploid() {
		return emploid;
	}
	public void setEmploid(String[] emploid) {
		this.emploid = emploid;
	}
	public String getDateInput() {
		return dateInput;
	}
	public void setDateInput(String dateInput) {
		this.dateInput = dateInput;
	}
	

	
	public List getEmployeeList() {
		return employeeList;
	}
	public void setEmployeeList(List employeeList) {
		this.employeeList = employeeList;
	}
	public String getStatusTypeId() {
		return statusTypeId;
	}
	public void setStatusTypeId(String statusTypeId) {
		this.statusTypeId = statusTypeId;
	}
	public String[] getDesgId() {
		return desgId;
	}
	public void setDesgId(String[] desgId) {
		this.desgId = desgId;
	}
	public String[] getPosId() {
		return posId;
	}
	public void setPosId(String[] posId) {
		this.posId = posId;
	}
	public String[] getPosName() {
		return posName;
	}
	public void setPosName(String[] posName) {
		this.posName = posName;
	}
	public String[] getGradeId() {
		return gradeId;
	}
	public void setGradeId(String[] gradeId) {
		this.gradeId = gradeId;
	}
	public String[] getComments() {
		return comments;
	}
	public void setComments(String[] comments) {
		this.comments = comments;
	}
	public String[] getCommentDate() {
		return commentDate;
	}
	public void setCommentDate(String[] commentDate) {
		this.commentDate = commentDate;
	}
	public String[] getReason() {
		return reason;
	}
	public void setReason(String[] reason) {
		this.reason = reason;
	}
	public String getDateOfjoin() {
		return dateOfjoin;
	}
	public void setDateOfjoin(String dateOfjoin) {
		this.dateOfjoin = dateOfjoin;
	}
	public String[] getIsPrimary() {
		return isPrimary;
	}
	public void setIsPrimary(String[] isPrimary) {
		this.isPrimary = isPrimary;
	}
	public String[] getAssignmentOrderNo() {
		return assignmentOrderNo;
	}
	public void setAssignmentOrderNo(String[] assignmentOrderNo) {
		this.assignmentOrderNo = assignmentOrderNo;
	}
	public String getDeathDate() {
		return deathDate;
	}
	public void setDeathDate(String deathDate) {
		this.deathDate = deathDate;
	}
	public String[] getServiceDocNo() {
		return serviceDocNo;
	}
	public void setServiceDocNo(String[] serviceDocNo) {
		this.serviceDocNo = serviceDocNo;
	}
	public String[] getServiceOrderNo() {
		return serviceOrderNo;
	}
	public void setServiceOrderNo(String[] serviceOrderNo) {
		this.serviceOrderNo = serviceOrderNo;
	}
	public String[] getPayScale() {
		return payScale;
	}
	public void setPayScale(String[] payScale) {
		this.payScale = payScale;
	}
	public String[] getEduDocNo() {
		return eduDocNo;
	}
	public void setEduDocNo(String[] eduDocNo) {
		this.eduDocNo = eduDocNo;
	}
	public String[] getTechDocNo() {
		return techDocNo;
	}
	public void setTechDocNo(String[] techDocNo) {
		this.techDocNo = techDocNo;
	}
	
	
}