package org.egov.pims.model;

// Generated Jul 9, 2007 3:21:09 PM by Hibernate Tools 3.2.0.b9

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import org.apache.log4j.Logger;
import org.egov.commons.EgwStatus;
import org.egov.commons.utils.EntityType;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.UserImpl;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.empLeave.model.LeaveApplication;
import org.egov.pims.empLeave.model.LeaveOpeningBalance;
import org.egov.pims.service.EmployeeService;
import org.hibernate.Query;


/**
 * 
 * @author DivyaShree
 *
 */
public class PersonalInformation implements java.io.Serializable,EntityType
{


	public final static Logger LOGGER = Logger.getLogger(PersonalInformation.class.getClass());
	private PersonalInformation employeeId;
	private EmployeeService employeeService;
	UserImpl	userMaster =null;
	Date maturityDate;
	Department egdeptMstr = null;
	
	private Integer idPersonalInformation;

	private LanguagesQulifiedMaster langQulMstr;

	private ReligionMaster religionMstr;

	private LanguagesKnownMaster languagesKnownMstr;

	private GradeMaster gradeMstr;

	private RecruimentMaster modeOfRecruimentMstr;

	private CommunityMaster communityMstr;

	private CategoryMaster categoryMstr;

	private BloodGroupMaster bloodGroupMstr;

	private TypeOfRecruimentMaster recruitmentTypeMstr;
	//for Posting Type
	private TypeOfPostingMaster postingTypeMstr;
	private PayFixedInMaster payFixedInMstr;

	private String employeeCode;
	private String employeeName;
	private  EmployeeStatusMaster employeeTypeMaster;
	private EgwStatus StatusMaster;
	private String employeeFirstName;
	private String employeeMiddleName;
	private String employeeLastName;
	private String fatherHusbandFirstName;
	private String fatherHusbandMiddleName;
	private String fatherHusbandLastName;
	private String identificationMarks2;
	private String panNumber;
	private Date dateOfBirth;
	/*
	 * death date
	 */
	private Date deathDate;
	/*
	 * deputation date
	 */
	private Date dateOfjoin;
	
	/*
	 * govt order number
	 */
	private String govtOrderNo;
	/*
	 * Retirement date moved from grade
	 */
	private Date retirementDate;

	private String motherTonuge;
	/*
	 * payment method type 
	 */

	private String paymentType;
	private Character gender= Character.valueOf('0');


	private Integer isActive= Integer.valueOf(1);

	private Character isHandicapped= Character.valueOf('0');

	private Character isMedReportAvailable=Character.valueOf('0');

	private Date dateOfFirstAppointment;

	private String identificationMarks1;

	private Character status= Character.valueOf('0');

	private DesignationMaster presentDesignation;

	private String scaleOfPay;

	private BigDecimal basicPay;

	private BigDecimal splPay;

	private BigDecimal ppSgppPay;

	private String gpfAcNumber;

	private Integer retirementAge;

	private Department presentDepartment;

	private String ifOnDutyArrangmentDutyDep;

	private String location;

	private String costCenter;
	
	private User createdBy;
	
	private Date createdTime;
	/*
	 * New column added for last modified date
	 */
	
	private Date lastmodifieddate;
	
	/*
	 * Added to calculate DA in payroll
	 */
	private EmployeeGroupMaster groupCatMstr;
		
	public EmployeeGroupMaster getGroupCatMstr() {
		return groupCatMstr;
	}
	public void setGroupCatMstr(EmployeeGroupMaster groupCatMstr) {
		this.groupCatMstr = groupCatMstr;
	}
	public User getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
private Set<org.egov.pims.model.AssignmentPrd> egpimsAssignmentPrd = new HashSet<org.egov.pims.model.AssignmentPrd>(
			0);
	/**
	 * @return Returns the egpimsEduDetails.
	 */
	public Set<EduDetails> getEgpimsEduDetails() {
		return egpimsEduDetails;
	}
	/**
	 * @param egpimsEduDetails The egpimsEduDetails to set.
	 */
	public void setEgpimsEduDetails(Set<EduDetails> egpimsEduDetails) {
		this.egpimsEduDetails = egpimsEduDetails;
	}
	public void addEduDetails(EduDetails egpimsEduDetails)
	{
		LOGGER.info("Adding EduDetails Detail."+egpimsEduDetails);
		if(getEgpimsEduDetails()!=null)
		getEgpimsEduDetails().add(egpimsEduDetails);
	}







	public void removeEduDetails(EduDetails egpimsEduDetails)
	{

		getEgpimsEduDetails().remove(egpimsEduDetails);
	}

/**
 * @return Returns the egpimsTecnicalQualification.
 */
public Set<TecnicalQualification> getEgpimsTecnicalQualification() {
	return egpimsTecnicalQualification;
}
/**
 * @param egpimsTecnicalQualification The egpimsTecnicalQualification to set.
 */
public void setEgpimsTecnicalQualification(
		Set<TecnicalQualification> egpimsTecnicalQualification) {
	this.egpimsTecnicalQualification = egpimsTecnicalQualification;
}

public void addTecnicalQualification(TecnicalQualification egpimsTecnicalQualification)
	{

		if(getEgpimsTecnicalQualification()!=null)
		getEgpimsTecnicalQualification().add(egpimsTecnicalQualification);



	}
	public void removeTecnicalQualification(TecnicalQualification egpimsTecnicalQualification)
	{

		getEgpimsTecnicalQualification().remove(egpimsTecnicalQualification);
	}



	public void addRegularisation(Regularisation egpimsRegularisation)
	{

		if(getEgpimsRegularisations()!=null)
		getEgpimsRegularisations().add(egpimsRegularisation);



	}
	public void removeRegularisation(Regularisation egpimsRegularisation)
	{

		getEgpimsRegularisations().remove(egpimsRegularisation);
	}


	public void addImmovablePropDetailses(ImmovablePropDetails egpimsImmovablePropDetails)
	{

		if(getEgpimsRegularisations()!=null)
		getEgpimsImmovablePropDetailses().add(egpimsImmovablePropDetails);
	}
	public void removeImmovablePropDetailses(ImmovablePropDetails egpimsImmovablePropDetails)
	{

		getEgpimsImmovablePropDetailses().remove(egpimsImmovablePropDetails);
	}
	/**
	 * 
	 */
	public void addEmployeeMasterDetails(EmployeeNomineeMaster nomineeMaster)
	{
		if(getEgpimsNomineeMaster()!=null)
		{
			getEgpimsNomineeMaster().add(nomineeMaster);
		}
	}

	private Set<Regularisation> egpimsRegularisations = new HashSet<Regularisation>(
			0);
			private Set<DeptTests> egpimsDeptTests = new HashSet<DeptTests>(
			0);
	private Set<EduDetails> egpimsEduDetails = new HashSet<EduDetails>(
			0);
	private Set<TecnicalQualification> egpimsTecnicalQualification = new HashSet<TecnicalQualification>(
			0);

	private Set<ImmovablePropDetails> egpimsImmovablePropDetailses = new HashSet<ImmovablePropDetails>(
			0);
	private Set<LeaveApplication> leaveApplicationSet = new HashSet<LeaveApplication>(
			0);

	private Set<LeaveOpeningBalance> leaveOpeningBalanceSet = new HashSet<LeaveOpeningBalance>(
			0);

	private Set<PersonAddress> egpimsPersonAddresses = new HashSet<PersonAddress>(
			0);

		private Set<Probation> egpimsProbations = new HashSet<Probation>(
			0);
		
		private Set<ServiceHistory>  egpimsServiceHistory = new HashSet<ServiceHistory>(0);
		
		//For NomineeMaster
		
		private Set<EmployeeNomineeMaster> egpimsNomineeMaster = new HashSet<EmployeeNomineeMaster>(0);

	private Set<TrainingPirticulars> egpimsTrainingPirticularses = new HashSet<TrainingPirticulars>(
			0);

	private Set<BankDet> egpimsBankDets = new HashSet<BankDet>(0);

	private Set<DisciplinaryPunishment> egpimsDisciplinaryPunishments = new HashSet<DisciplinaryPunishment>(
			0);

	private Set<MovablePropDetails> egpimsMovablePropDetailses = new HashSet<MovablePropDetails>(
			0);

	private Set<LtcPirticulars> egpimsLtcPirticularses = new HashSet<LtcPirticulars>(
			0);



	private Set<LangKnown> egpimsLangKnowns = new HashSet<LangKnown>(
			0);



	private Set<NomimationPirticulars> egpimsNomimationPirticularses = new HashSet<NomimationPirticulars>(0);
	

	private Set<DetOfEnquiryOfficer> egpimsDetailsEnquiryOfficers = new HashSet<DetOfEnquiryOfficer>(
			0);

	
	
	public Set<DetOfEnquiryOfficer> getEgpimsDetailsEnquiryOfficers() {
		return egpimsDetailsEnquiryOfficers;
	}
	public void setEgpimsDetailsEnquiryOfficers(
			Set<DetOfEnquiryOfficer> egpimsDetailsEnquiryOfficers) {
		this.egpimsDetailsEnquiryOfficers = egpimsDetailsEnquiryOfficers;
	}
	public PersonalInformation()
	{
	}


	public Integer getIdPersonalInformation()
	{
		return this.idPersonalInformation;
	}
	
	public Integer getId()
	{
		return this.idPersonalInformation;
	}
	
	public void setIdPersonalInformation(Integer idPersonalInformation)
	{
		this.idPersonalInformation = idPersonalInformation;
	}
public BloodGroupMaster getBloodGroupMstr()
	{
		return this.bloodGroupMstr;
	}

	public void setBloodGroupMstr(BloodGroupMaster bloodGroupMstr)
	{
		this.bloodGroupMstr = bloodGroupMstr;
	}

	public ReligionMaster getReligionMstr()
	{
		return this.religionMstr;
	}

	public void setReligionMstr(ReligionMaster religionMstr)
	{
		this.religionMstr = religionMstr;
	}

	public LanguagesKnownMaster getLanguagesKnownMstr()
	{
		return this.languagesKnownMstr;
	}

	public void setLanguagesKnownMstr(
			LanguagesKnownMaster languagesKnownMstr)
	{
		this.languagesKnownMstr = languagesKnownMstr;
	}

	public GradeMaster getGradeMstr()
	{
		return this.gradeMstr;
	}
	public void setGradeMstr(GradeMaster gradeMstr)
	{
		this.gradeMstr = gradeMstr;
	}
	public CommunityMaster getCommunityMstr()
	{
		return this.communityMstr;
	}
	public void setCommunityMstr(CommunityMaster communityMstr)
	{
		this.communityMstr = communityMstr;
	}
	public CategoryMaster getCategoryMstr()
	{
		return this.categoryMstr;
	}
	public void setCategoryMstr(CategoryMaster categoryMstr)
	{
		this.categoryMstr = categoryMstr;
	}
	public TypeOfRecruimentMaster getRecruitmentTypeMstr()
	{
		return this.recruitmentTypeMstr;
	}
	public void setRecruitmentTypeMstr(
			TypeOfRecruimentMaster recruitmentTypeMstr)
	{
		this.recruitmentTypeMstr = recruitmentTypeMstr;
	}
	public PayFixedInMaster getPayFixedInMstr()
	{
		return this.payFixedInMstr;
	}
	public void setPayFixedInMstr(
			PayFixedInMaster payFixedInMstr)
	{
		this.payFixedInMstr = payFixedInMstr;
	}

	public String getEmployeeCode()
	{
		return this.employeeCode;
	}

	public void setEmployeeCode(Integer employeeCode)
	{
		this.employeeCode = String.valueOf(employeeCode);
	}

	public void setEmployeeCode(String employeeCode)
	{
		this.employeeCode = employeeCode;
	}

	public Date getDateOfBirth()
	{
		return this.dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth)
	{
		this.dateOfBirth = dateOfBirth;
	}


	public String getMotherTonuge()
	{
		return this.motherTonuge;
	}

	public void setMotherTonuge(String motherTonuge)
	{
		this.motherTonuge = motherTonuge;
	}

	public Character getGender()
	{
		return this.gender;
	}

	public void setGender(Character gender)
	{
		this.gender = gender;
	}

	public Character getIsHandicapped()
	{
		return this.isHandicapped;
	}

	public void setIsHandicapped(Character isHandicapped)
	{
		this.isHandicapped = isHandicapped;
	}

	public Character getIsMedReportAvailable()
	{
		return this.isMedReportAvailable;
	}

	public void setIsMedReportAvailable(Character isMedReportAvailable)
	{
		this.isMedReportAvailable = isMedReportAvailable;
	}

	public Date getDateOfFirstAppointment()
	{
		return this.dateOfFirstAppointment;
	}

	public void setDateOfFirstAppointment(Date dateOfFirstAppointment)
	{
		this.dateOfFirstAppointment = dateOfFirstAppointment;
	}

	public String getIdentificationMarks1()
	{
		return this.identificationMarks1;
	}

	public void setIdentificationMarks1(String identificationMarks1)
	{
		this.identificationMarks1 = identificationMarks1;
	}

	public Character getStatus()
	{
		return this.status;
	}

	public void setStatus(Character status)
	{
		this.status = status;
	}

	
	public void setPresentDesignation(DesignationMaster presentDesignation)
	{
		this.presentDesignation = presentDesignation;
	}

	public String getScaleOfPay()
	{
		return this.scaleOfPay;
	}

	public void setScaleOfPay(String scaleOfPay)
	{
		this.scaleOfPay = scaleOfPay;
	}

	public BigDecimal getBasicPay()
	{
		return this.basicPay;
	}

	public void setBasicPay(BigDecimal basicPay)
	{
		this.basicPay = basicPay;
	}

	public BigDecimal getSplPay()
	{
		return this.splPay;
	}

	public void setSplPay(BigDecimal splPay)
	{
		this.splPay = splPay;
	}

	public BigDecimal getPpSgppPay()
	{
		return this.ppSgppPay;
	}

	public void setPpSgppPay(BigDecimal ppSgppPay)
	{
		this.ppSgppPay = ppSgppPay;
	}

	public String getGpfAcNumber()
	{
		return this.gpfAcNumber;
	}

	public void setGpfAcNumber(String gpfAcNumber)
	{
		this.gpfAcNumber = gpfAcNumber;
	}

	

	public Integer getRetirementAge() {
		return retirementAge;
	}
	public void setRetirementAge(Integer retirementAge) {
		this.retirementAge = retirementAge;
	}
	public Department getPresentDepartment()
	{
		return new DepartmentImpl();

	}

	public void setPresentDepartment(Department presentDepartment)
	{
		this.presentDepartment = presentDepartment;
	}

	public String getIfOnDutyArrangmentDutyDep()
	{
		return this.ifOnDutyArrangmentDutyDep;
	}

	public void setIfOnDutyArrangmentDutyDep(String ifOnDutyArrangmentDutyDep)
	{
		this.ifOnDutyArrangmentDutyDep = ifOnDutyArrangmentDutyDep;
	}

	public String getLocation()
	{
		return this.location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	public String getCostCenter()
	{
		return this.costCenter;
	}

	public void setCostCenter(String costCenter)
	{
		this.costCenter = costCenter;
	}

	public Set<Regularisation> getEgpimsRegularisations()
	{
		return this.egpimsRegularisations;
	}

	public void setEgpimsRegularisations(
			Set<Regularisation> egpimsRegularisations)
	{
		this.egpimsRegularisations = egpimsRegularisations;
	}

	public Set<ImmovablePropDetails> getEgpimsImmovablePropDetailses()
	{
		return this.egpimsImmovablePropDetailses;
	}

	public void setEgpimsImmovablePropDetailses(
			Set<ImmovablePropDetails> egpimsImmovablePropDetailses)
	{
		this.egpimsImmovablePropDetailses = egpimsImmovablePropDetailses;
	}

	public void addPersonAddresses(PersonAddress egpimsPersonAddress)
	{

		if(getEgpimsPersonAddresses()!=null)
		getEgpimsPersonAddresses().add(egpimsPersonAddress);
	}
	public void removePersonAddresses(PersonAddress egpimsPersonAddress)
	{

		getEgpimsPersonAddresses().remove(egpimsPersonAddress);
	}

	public Set<PersonAddress> getEgpimsPersonAddresses()
	{
		return this.egpimsPersonAddresses;
	}

	public void setEgpimsPersonAddresses(
			Set<PersonAddress> egpimsPersonAddresses)
	{
		this.egpimsPersonAddresses = egpimsPersonAddresses;
	}

		public void addProbations(Probation egpimsProbation)
	{

		if(getEgpimsProbations()!=null)
		getEgpimsProbations().add(egpimsProbation);
	}
	public void removeProbations(Probation egpimsProbation)
	{

		getEgpimsProbations().remove(egpimsProbation);
	}
	public Set<Probation> getEgpimsProbations()
	{
		return this.egpimsProbations;
	}

	public void setEgpimsProbations(Set<Probation> egpimsProbations)
	{
		this.egpimsProbations = egpimsProbations;
	}

	public Set<TrainingPirticulars> getEgpimsTrainingPirticularses()
	{
		return this.egpimsTrainingPirticularses;
	}

	public void setEgpimsTrainingPirticularses(
			Set<TrainingPirticulars> egpimsTrainingPirticularses)
	{
		this.egpimsTrainingPirticularses = egpimsTrainingPirticularses;
	}
	public void addTrainingPirticularses(TrainingPirticulars egpimsTrainingPirticulars)
	{

		if(getEgpimsTrainingPirticularses()!=null)
		getEgpimsTrainingPirticularses().add(egpimsTrainingPirticulars);
	}
	public void removeTrainingPirticularses(TrainingPirticulars egpimsTrainingPirticulars)
	{

		getEgpimsTrainingPirticularses().remove(egpimsTrainingPirticulars);
	}
	public Set<BankDet> getEgpimsBankDets()
	{
		return this.egpimsBankDets;
	}

	public void setEgpimsBankDets(Set<BankDet> egpimsBankDets)
	{
		this.egpimsBankDets = egpimsBankDets;
	}
	public void addBankDets(BankDet egpimsBankDet)
	{

		if(getEgpimsBankDets()!=null)
		getEgpimsBankDets().add(egpimsBankDet);
	}
	public void removeBankDets(BankDet egpimsBankDet)
	{

		getEgpimsBankDets().remove(egpimsBankDet);
	}
	public Set<DisciplinaryPunishment> getEgpimsDisciplinaryPunishments()
	{
		return this.egpimsDisciplinaryPunishments;
	}

	public void setEgpimsDisciplinaryPunishments(
			Set<DisciplinaryPunishment> egpimsDisciplinaryPunishments)
	{
		this.egpimsDisciplinaryPunishments = egpimsDisciplinaryPunishments;
	}
	public void addDisciplinaryPunishment(DisciplinaryPunishment egpimsDisciplinaryPunishment)
	{

		if(getEgpimsDisciplinaryPunishments()!=null)
		getEgpimsDisciplinaryPunishments().add(egpimsDisciplinaryPunishment);
	}
	public void removeDisciplinaryPunishment(DisciplinaryPunishment egpimsDisciplinaryPunishment)
	{

		getEgpimsDisciplinaryPunishments().remove(egpimsDisciplinaryPunishment);
	}
	public Set<MovablePropDetails> getEgpimsMovablePropDetailses()
	{
		return this.egpimsMovablePropDetailses;
	}

	public void setEgpimsMovablePropDetailses(
			Set<MovablePropDetails> egpimsMovablePropDetailses)
	{
		this.egpimsMovablePropDetailses = egpimsMovablePropDetailses;
	}
	public void addMovablePropDetails(MovablePropDetails egpimsMovablePropDetails)
	{

		if(getEgpimsMovablePropDetailses()!=null)
		getEgpimsMovablePropDetailses().add(egpimsMovablePropDetails);
	}
	public void removeMovablePropDetails(MovablePropDetails egpimsMovablePropDetails)
	{

		getEgpimsMovablePropDetailses().remove(egpimsMovablePropDetails);
	}
	public Set<LtcPirticulars> getEgpimsLtcPirticularses()
	{
		return this.egpimsLtcPirticularses;
	}

	public void setEgpimsLtcPirticularses(
			Set<LtcPirticulars> egpimsLtcPirticularses)
	{
		this.egpimsLtcPirticularses = egpimsLtcPirticularses;
	}
	public void addLtcPirticulars(LtcPirticulars egpimsLtcPirticulars)
	{

		if(getEgpimsLtcPirticularses()!=null)
		getEgpimsLtcPirticularses().add(egpimsLtcPirticulars);
	}
	public void removeLtcPirticulars(LtcPirticulars egpimsLtcPirticulars)
	{

		getEgpimsLtcPirticularses().remove(egpimsLtcPirticulars);
	}

		public Set<LangKnown> getEgpimsLangKnowns()
	{
		return this.egpimsLangKnowns;
	}

	public void setEgpimsLangKnowns(Set<LangKnown> egpimsLangKnowns)
	{
		this.egpimsLangKnowns = egpimsLangKnowns;
	}
	public void addLangKnown(LangKnown egpimsLangKnown)
	{

		if(getEgpimsLangKnowns()!=null)
		getEgpimsLangKnowns().add(egpimsLangKnown);
	}
	public void removeLangKnown(LangKnown egpimsLangKnown)
	{

		getEgpimsLangKnowns().remove(egpimsLangKnown);
	}


	public Set<NomimationPirticulars> getEgpimsNomimationPirticularses()
	{
		return this.egpimsNomimationPirticularses;
	}

	public void setEgpimsNomimationPirticularses(
			Set<NomimationPirticulars> egpimsNomimationPirticularses)
	{
		this.egpimsNomimationPirticularses = egpimsNomimationPirticularses;
	}
public void addNomimationPirticularses(NomimationPirticulars egpimsNomimationPirticulars)
	{

		if(getEgpimsNomimationPirticularses()!=null)
		getEgpimsNomimationPirticularses().add(egpimsNomimationPirticulars);
	}
	public void removeNomimationPirticularses(NomimationPirticulars egpimsNomimationPirticulars)
	{

		getEgpimsNomimationPirticularses().remove(egpimsNomimationPirticulars);
	}
			/**
			 * @return Returns the egpimsDeptTests.
			 */
			public Set<DeptTests> getEgpimsDeptTests() {
				return egpimsDeptTests;
			}
			/**
			 * @param egpimsDeptTests The egpimsDeptTests to set.
			 */
			public void setEgpimsDeptTests(Set<DeptTests> egpimsDeptTests) {
				this.egpimsDeptTests = egpimsDeptTests;
			}
			public void addDeptTests(DeptTests egpimsDeptTests)
				{

					if(getEgpimsDeptTests()!=null)
					getEgpimsDeptTests().add(egpimsDeptTests);
				}
				public void removeDeptTests(DeptTests EgpimsDeptTests)
				{

					getEgpimsDeptTests().remove(EgpimsDeptTests);
	}

	/**
	 * @return Returns the egdeptMstr.
	 */
	public Department getEgdeptMstr() {
		return egdeptMstr;
	}
	/**
	 * @param egdeptMstr The egdeptMstr to set.
	 */
	public void setEgdeptMstr(Department egdeptMstr) {
		this.egdeptMstr = egdeptMstr;
	}
	/**
	 * @return Returns the isActive.
	 */
	public Integer getIsActive() {
		return isActive;
	}
	/**
	 * @param isActive The isActive to set.
	 */
	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}
			/**
			 * @return Returns the userMaster.
			 */
			public UserImpl getUserMaster() {
				return userMaster;
			}
			/**
			 * @param userMaster The userMaster to set.
			 */
			public void setUserMaster(UserImpl userMaster) {
				this.userMaster = userMaster;
			}
/**
 * @return Returns the egpimsAssignmentPrd.
 */
public Set<org.egov.pims.model.AssignmentPrd> getEgpimsAssignmentPrd() {
	return egpimsAssignmentPrd;
}
/**
 * @param egpimsAssignmentPrd The egpimsAssignmentPrd to set.
 */
public void setEgpimsAssignmentPrd(
		Set<org.egov.pims.model.AssignmentPrd> egpimsAssignmentPrd) {
	this.egpimsAssignmentPrd = egpimsAssignmentPrd;
}
public void addAssignmentPrd(AssignmentPrd egEmpAssignmentPrd)
{

	if(getEgpimsAssignmentPrd()!=null)
		getEgpimsAssignmentPrd().add(egEmpAssignmentPrd);
}
public void removeAssignmentPrd(AssignmentPrd egEmpAssignmentPrd)
{

	getEgpimsAssignmentPrd().remove(egEmpAssignmentPrd);
}
public String getEmployeeFirstName() {
	return employeeFirstName;
}
public void setEmployeeFirstName(String employeeFirstName) {
	this.employeeFirstName = employeeFirstName;
}
public String getEmployeeLastName() {
	return employeeLastName;
}
public void setEmployeeLastName(String employeeLastName) {
	this.employeeLastName = employeeLastName;
}
public String getEmployeeMiddleName() {
	return employeeMiddleName;
}
public void setEmployeeMiddleName(String employeeMiddleName) {
	this.employeeMiddleName = employeeMiddleName;
}
public String getFatherHusbandFirstName() {
	return fatherHusbandFirstName;
}
public void setFatherHusbandFirstName(String fatherHusbandFirstName) {
	this.fatherHusbandFirstName = fatherHusbandFirstName;
}
public String getFatherHusbandLastName() {
	return fatherHusbandLastName;
}
public void setFatherHusbandLastName(String fatherHusbandLastName) {
	this.fatherHusbandLastName = fatherHusbandLastName;
}
public String getFatherHusbandMiddleName() {
	return fatherHusbandMiddleName;
}
public void setFatherHusbandMiddleName(String fatherHusbandMiddleName) {
	this.fatherHusbandMiddleName = fatherHusbandMiddleName;
}
public String getIdentificationMarks2() {
	return identificationMarks2;
}
public void setIdentificationMarks2(String identificationMarks2) {
	this.identificationMarks2 = identificationMarks2;
}
public String getPanNumber() {
	return panNumber;
}
public void setPanNumber(String panNumber) {
	this.panNumber = panNumber;
}

public LanguagesQulifiedMaster getLangQulMstr() {
	return langQulMstr;
}
public void setLangQulMstr(LanguagesQulifiedMaster langQulMstr) {
	this.langQulMstr = langQulMstr;
}
public RecruimentMaster getModeOfRecruimentMstr() {
	return modeOfRecruimentMstr;
}
public void setModeOfRecruimentMstr(RecruimentMaster modeOfRecruimentMstr) {
	this.modeOfRecruimentMstr = modeOfRecruimentMstr;
}
public Set<LeaveApplication> getLeaveApplicationSet() {
	return leaveApplicationSet;
}
public void setLeaveApplicationSet(Set<LeaveApplication> leaveApplicationSet) {
	this.leaveApplicationSet = leaveApplicationSet;
}
public void addLeaveApplication(LeaveApplication leaveApplication)
{

	if(getLeaveApplicationSet()!=null)
		getLeaveApplicationSet().add(leaveApplication);
}
public void removeLeaveApplication(LeaveApplication leaveApplication)
{

	if(getLeaveApplicationSet()!=null)
		getLeaveApplicationSet().remove(leaveApplication);
}
public Set<LeaveOpeningBalance> getLeaveOpeningBalanceSet() {
	return leaveOpeningBalanceSet;
}
public void setLeaveOpeningBalanceSet(
		Set<LeaveOpeningBalance> leaveOpeningBalanceSet) {
	this.leaveOpeningBalanceSet = leaveOpeningBalanceSet;
}
public void addLeaveOpeningBalance(LeaveOpeningBalance leaveOpeningBalance)
{

	if(getLeaveOpeningBalanceSet()!=null)
		getLeaveOpeningBalanceSet().add(leaveOpeningBalance);
}
public void removeLeaveOpeningBalance(LeaveOpeningBalance leaveOpeningBalance)
{

	if(getLeaveOpeningBalanceSet()!=null)
		getLeaveOpeningBalanceSet().remove(leaveOpeningBalance);
}
public void setEmployeeService(EmployeeService employeeService) {
	this.employeeService = employeeService;
}
public PersonalInformation getEmployeeId() {
	return employeeId;
}
public void setEmployeeId(PersonalInformation employeeId) {
	this.employeeId = employeeId;
}
public String getEmployeeName() {
	return employeeName;
}
public void setEmployeeName(String employeeName) {
	this.employeeName = employeeName;
}

public EmployeeStatusMaster getEmployeeTypeMaster() {
	return employeeTypeMaster;
}
public void setEmployeeTypeMaster(EmployeeStatusMaster employeeTypeMaster) {
	this.employeeTypeMaster = employeeTypeMaster;
}
public Date getMaturityDate() {
	return maturityDate;
}
public void setMaturityDate(Date maturityDate) {
	this.maturityDate = maturityDate;
}


	public EgwStatus getStatusMaster() {
		return StatusMaster;
	}
	public void setStatusMaster(EgwStatus statusMaster) {
		StatusMaster = statusMaster;
	}
	public Date getDeathDate() {
		return deathDate;
	}
	public void setDeathDate(Date deathDate) {
		this.deathDate = deathDate;
	}
	public Date getLastmodifieddate() {
		return lastmodifieddate;
	}
	public void setLastmodifieddate(Date lastmodifieddate) {
		this.lastmodifieddate = lastmodifieddate;
	}
	public Date getDateOfjoin() {
		return dateOfjoin;
	}
	public void setDateOfjoin(Date dateOfjoin) {
		this.dateOfjoin = dateOfjoin;
	}
	
	public Date getRetirementDate() {
		return retirementDate;
	}
	public void setRetirementDate(Date retirementDate) {
		this.retirementDate = retirementDate;
	}
	public String getGovtOrderNo() {
		return govtOrderNo;
	}
	public void setGovtOrderNo(String govtOrderNo) {
		this.govtOrderNo = govtOrderNo;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public Set<ServiceHistory> getEgpimsServiceHistory() {
		return egpimsServiceHistory;
	}
	public void setEgpimsServiceHistory(Set<ServiceHistory> egpimsServiceHistory) {
		this.egpimsServiceHistory = egpimsServiceHistory;
	}
	
	public void addService(ServiceHistory egpimsService)
	{

		if(getEgpimsServiceHistory()!=null)
			getEgpimsServiceHistory().add(egpimsService);
	}
	public void removeService(ServiceHistory egpimsService)
	{

		getEgpimsServiceHistory().remove(egpimsService);
	}
	public TypeOfPostingMaster getPostingTypeMstr() {
		return postingTypeMstr;
	}
	public void setPostingTypeMstr(TypeOfPostingMaster postingTypeMstr) {
		this.postingTypeMstr = postingTypeMstr;
	}
	
	public String getBankaccount() {
		
		return null;
	}
	
	public String getBankname() {
		
		return null;
	}
	
	public String getCode() {
		if(employeeCode!=null)
		{
		   return this.employeeCode.toString();
		}
		else
		{
			return null;
		}
	}
	
	public String getIfsccode() {
		
		return null;
	}
	
	public String getModeofpay() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getName() {
		// TODO Auto-generated method stub
		return this.employeeName;
	}
	
	public String getPanno() {
		// TODO Auto-generated method stub
		return this.panNumber;
	}
	
	public String getTinno() {
		// TODO Auto-generated method stub
		return null;
	}
	public Set<EmployeeNomineeMaster> getEgpimsNomineeMaster() {
		return egpimsNomineeMaster;
	}
	public void setEgpimsNomineeMaster(
			Set<EmployeeNomineeMaster> egpimsNomineeMaster) {
		this.egpimsNomineeMaster = egpimsNomineeMaster;
	}
	public Integer getEntityId()
	{
		return Integer.valueOf(idPersonalInformation);
	}
	@Override
	public String getEntityDescription() {
		
		return getName();
	}
	/**
	 * returns current/last primary/temporary assignment on given Date 
	 * @param toDate
	 * @return Assignment 
	 */
	@SuppressWarnings("unchecked")
	public Assignment getAssignment(Date toDate)
	{
		Assignment assignment=null;
		List<Assignment> assignmentList;
		StringBuilder stringbuilder=new StringBuilder(100); 
		stringbuilder.append("from Assignment ass where  ass.assignmentPrd.employeeId.idPersonalInformation = :empid and ass.isPrimary=:primary" +
				"  and (ass.assignmentPrd.fromDate <=:fromDate and (ass.assignmentPrd.toDate>=:toDate or ass.assignmentPrd.toDate is null) " +
				" or (:toDate >  "+
				" (select max(prd.toDate) from AssignmentPrd prd where prd.id=ass.assignmentPrd.id and prd.employeeId.idPersonalInformation=:empid ) ) )" +
				" ORDER BY ass.assignmentPrd.toDate DESC  " );
		Query assignmentHql=HibernateUtil.getCurrentSession().
		createQuery(stringbuilder.toString());
		assignmentHql.setDate("fromDate", toDate).
		setDate("toDate", toDate).
		setLong("empid", Long.valueOf(this.getIdPersonalInformation())).
		setCharacter("primary", 'Y');
		assignmentList= assignmentHql.list();
		
		if(assignmentList.isEmpty())
		{
			assignmentList=assignmentHql.
			setCharacter("primary", 'N').list();
			if(!assignmentHql.list().isEmpty())
			{
				assignment=assignmentList.get(0);
			}
		}
		else
		{ 
			assignment=(Assignment)assignmentHql.list().get(0);
		}
		
		return assignment;
	}
}
