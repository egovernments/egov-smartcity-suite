/*
 *	@(#)TransactionManager.java		Oct 25, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * eGov PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package org.egov.pims.service;

//@author deepak
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;



import org.egov.exceptions.EGOVException;
import org.egov.commons.CFinancialYear;
import org.egov.exceptions.NoSuchObjectException;
import org.egov.exceptions.TooManyValuesException;
import org.egov.lib.rjbac.user.User;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.commons.Position;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.AssignmentPrd;
import org.egov.pims.model.BankDet;
import org.egov.pims.model.DeptTests;
import org.egov.pims.model.DetOfEnquiryOfficer;
import org.egov.pims.model.DisciplinaryPunishment;
import org.egov.pims.model.DisciplinaryPunishmentApproval;
import org.egov.pims.model.EduDetails;

import org.egov.pims.model.EmployeeDepartment;
import org.egov.pims.model.EmployeeNamePoJo;
import org.egov.pims.model.EmployeeNomineeMaster;
import org.egov.pims.model.EmployeeView;
import org.egov.pims.model.GenericMaster;
import org.egov.pims.model.GradeMaster;
import org.egov.pims.model.ImmovablePropDetails;
import org.egov.pims.model.LangKnown;
import org.egov.pims.model.LtcPirticulars;
import org.egov.pims.model.MovablePropDetails;
import org.egov.pims.model.NomimationPirticulars;
import org.egov.pims.model.PersonAddress;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.model.Probation;
import org.egov.pims.model.Regularisation;
import org.egov.pims.model.ServiceHistory;
import org.egov.pims.model.TecnicalQualification;
import org.egov.pims.model.TrainingPirticulars;

public interface EmployeeService 
{

	public abstract boolean checkDuplication(String name,String className);
	public abstract AssignmentPrd getAssPrdIdForEmployee(Integer empId);
	public abstract void createPersonAddress(PersonAddress personAddress);
	//public abstract String getFinancialYearId(String estDate);

	public abstract PersonalInformation createEmloyee(PersonalInformation egpimsPersonalInformation);
	public  abstract void deleteDetOfEnquiryOfficer(DetOfEnquiryOfficer egpimsDetOfEnquiryOfficer);
	public abstract void createProbation(Probation probation);
	public abstract void  createRegularisation(Regularisation regularisation);
	public abstract void createEducationDetails(EduDetails eduDetails);
	public abstract void createTecnicalQualification(TecnicalQualification tecnicalQualification);
	public abstract void createDeptTests(DeptTests deptTests);
	public abstract void createNomimationPirticulars(NomimationPirticulars nomimationPirticulars);
	public abstract void createImmovablePropDetails(ImmovablePropDetails immovablePropDetails);
	public abstract void createMovablePropDetails(MovablePropDetails movablePropDetails);
	public abstract void updateEmloyee(PersonalInformation egpimsPersonalInformation);
	public abstract PersonalInformation getEmloyeeById(Integer employeeId);
	public abstract GenericMaster getGenericMaster(Integer masterId,String masterName);
	public abstract Probation getProbationId(Integer probationId);

	public abstract ServiceHistory getServiceId(Integer serviceId)throws Exception;
	public abstract PersonalInformation getEmpForUserId(Integer userId);

	public abstract void updateProbation(Probation egpimsProbation);
	public abstract Regularisation getRegularisationById(Integer regularisationId);
	public abstract void updateRegularisation(Regularisation egpimsRegularisation);
	public abstract EduDetails getEduDetailsById(Integer eduDetailsId);
	public abstract void updateEduDetails(EduDetails egpimsEduDetails);
	public abstract TecnicalQualification getTecnicalQualificationById(Integer tecnicalQualificationId);
	public abstract void updateTecnicalQualification(TecnicalQualification egpimsTecnicalQualification);
	public abstract DeptTests getDeptTestsById(Integer deptTestsId);
	public abstract void updateDeptTests(DeptTests egpimsDeptTests);
	public abstract NomimationPirticulars getNomimationPirticularsById(Integer nomimationPirticularsId);
	public void updateNomimationPirticulars(NomimationPirticulars egpimsNomimationPirticulars);
	public abstract ImmovablePropDetails getImmovablePropDetailsById(Integer immovablePropDetailsId);
	public abstract void updateImmovablePropDetails(ImmovablePropDetails egpimsImmovablePropDetails);
	public abstract MovablePropDetails getMovablePropDetailsById(Integer movablePropDetailsId);
	public abstract DisciplinaryPunishment getDisciplinaryPunishmentById(Integer disciplinaryPunishmentId);
	public abstract void updateMovablePropDetails(MovablePropDetails egpimsMovablePropDetails);
	public abstract Map getDisciplinaryPunishmentByEmployeeID(Integer ID);
	public abstract void updateDisciplinaryPunishment(DisciplinaryPunishment egpimsDisciplinaryPunishment);
	public abstract DetOfEnquiryOfficer getEnquiryOfficerById(Integer enquiryOfficerId);
	public abstract void updateDetOfEnquiryOfficer(DetOfEnquiryOfficer egpimsDetOfEnquiryOfficer);
	public abstract AssignmentPrd getAssignmentPrdById(Integer assignmentPrdId);
	public abstract void updateAssignmentPrd(AssignmentPrd assignmentPrd);
	public abstract Assignment getAssignmentById(Integer assignmentId);
	public abstract void updateAssignment(Assignment assignment);
	public abstract LtcPirticulars getLtcPirticularsById(Integer ltcPirticularsId);
	public abstract void updateLtcPirticulars(LtcPirticulars egpimsLtcPirticulars);
	public abstract TrainingPirticulars getTrainingPirticularsById(Integer trainingPirticularsId);
	public abstract void updateTrainingPirticulars(TrainingPirticulars egpimsTrainingPirticulars);
	public abstract void addDeptTests(PersonalInformation personalInformation,DeptTests deptTests);
	public abstract void updateBankDet(BankDet bankDet);
	public abstract void addAssignmentPrd(PersonalInformation personalInformation,AssignmentPrd egEmpAssignmentPrd);
	public abstract void addDetOfEnquiryOfficer(DisciplinaryPunishment disciplinaryPunishment,DetOfEnquiryOfficer detOfEnquiryOfficer);
	public abstract void addBankDets(PersonalInformation personalInformation,BankDet egpimsBankDet);
	public abstract void addDisciplinaryPunishment(PersonalInformation personalInformation,DisciplinaryPunishment egpimsDisciplinaryPunishment) throws SQLException;
	public abstract void addEduDetails(PersonalInformation personalInformation,EduDetails egpimsEduDetails);
	public abstract void addImmovablePropDetailses(PersonalInformation personalInformation,ImmovablePropDetails egpimsImmovablePropDetails);
	public abstract void addLangKnown(PersonalInformation personalInformation,LangKnown egpimsLangKnown);
	public abstract void addLtcPirticulars(PersonalInformation personalInformation,LtcPirticulars ltcPirticulars);
	public abstract void addMovablePropDetails(PersonalInformation personalInformation,MovablePropDetails movablePropDetails);
	public abstract void addNomimationPirticularses(PersonalInformation personalInformation,NomimationPirticulars nomimationPirticulars);
	public abstract void addPersonAddresses(PersonalInformation personalInformation,PersonAddress personAddress);
	public abstract void addProbations(PersonalInformation personalInformation,Probation probation);
	public abstract void addRegularisation(PersonalInformation personalInformation,Regularisation regularisation);
	public abstract void addTecnicalQualification(PersonalInformation personalInformation,TecnicalQualification tecnicalQualification);
	public abstract void addTrainingPirticularses(PersonalInformation personalInformation,TrainingPirticulars trainingPirticulars);
	public abstract void addDisiplinaryApproval(DisciplinaryPunishmentApproval disciplinaryPunishmentApproval) throws SQLException;
	public abstract List getListOfEmpforDept(Integer deptId);
	public abstract List getListOfEmpforDesignation(Integer desigId);
	public abstract Assignment getLatestAssignmentForEmployee(Integer empId);
	public abstract Assignment getAssignmentByEmpAndDate(Date date,Integer empId);
	public abstract EmployeeNamePoJo getNameOfEmployee(Integer empId);
	public  abstract List getHistoryOfEmpForCurrentFinY(Integer empId,java.util.Date givenDate);
	public  abstract List getHistoryOfEmpForGivenFinY(Integer empId,java.util.Date givenDate,CFinancialYear cFinancialYear);
	public  abstract Map getAllPIMap();
	public abstract boolean checkDisciplinaryNo(String disciplinaryNo);
	public abstract boolean checkSanctionNoForDisciplinary(String sanctionNo);
	public abstract List getDisiplinaryApplicationsRejectedEmpID(Integer empId);
	public abstract List getDisiplinaryApplicationsApprovedEmpID(Integer empId);
	public abstract List getDisiplinaryApplicationsAppliedEmpID(Integer empId);
	public abstract List getListByAppNoAndMeMoNo(String applicationNumber ,String chargeMemoNo,Integer empId) ;
	public abstract Map getMapForList(List list);
	public abstract Map getMapForList(List list, String fieldName1, String fieldName2);
	public abstract AssignmentPrd getAssignmentPrdByEmpAndDate(Date date,Integer empId);
	public Integer getNextVal(String seqName);
	public abstract void createBankDet(BankDet egpimsBankDet);
	public abstract void createAssignmentPrd(AssignmentPrd egEmpAssignmentPrd);
	public abstract void createAssignment(Assignment egEmpAssignment);
	public abstract void createDisciplinaryPunishment(DisciplinaryPunishment disciplinaryPunishment);
	public abstract void createEmployeeDepartment(EmployeeDepartment employeeDepartment);
	public abstract void createDetOfEnquiryOfficer(DetOfEnquiryOfficer egpimsDetOfEnquiryOfficer);
	public abstract void createLtcPirticularsFields(LtcPirticulars egpimsLtcPirticulars);
	public abstract void createTrainingPirticularsFields(TrainingPirticulars egpimsTrainingPirticulars);

	public abstract List searchEmployee(Integer departmentId,Integer designationId,String code,String name,Integer status)throws Exception;
	/*
	 * search employee by department,designation,functionary,code and name
	 */
	public abstract List searchEmployee(Integer departmentId,Integer designationId,Integer functionaryId,String code,String name,Integer status)throws Exception;
	@Deprecated
	public abstract List searchEmployee(Integer departmentId,Integer designationId,String code,String name,String searchAll)throws Exception;
	public abstract List searchEmployee(Integer empId)throws Exception;
	public abstract void createLangaugeKnown(LangKnown lanKnown);
	public abstract EmployeeDepartment getEmployeeDepartmentById(Integer iD);
	public abstract void deleteEmpDepForAss(Assignment egEmpAssignment);
	public abstract void deleteLangKnownForEmp(PersonalInformation personalInformation);
	public abstract List getAssPrdIdsForEmployee(Integer empId);
	public abstract Integer getNextVal();
	public abstract boolean checkPos(Integer posId,Date fromDate,Date toDate,Integer empId,String isPrimary);
	public abstract PersonalInformation getEmployeeforPosition(Position higherpos);
	public abstract Position getPositionforEmp(Integer empId);
	public abstract String getEmployeeCode();
	public abstract boolean getHodById(Integer id);
	public abstract List getListOfPersonalInformationByEmpIdsList(List empIdsList);
	public List getListOfEmployeeWithoutAssignment(Date fromdate);/*new*/
	public Assignment getLatestAssignmentForEmployeeByToDate(Integer empId,Date todate) throws Exception;
	public PersonalInformation getEmployee(Integer deptId, Integer designationId, Integer Boundaryid)throws TooManyValuesException, NoSuchObjectException;
	public PersonalInformation getEmployeeByFunctionary(Integer deptId, Integer designationId, Integer Boundaryid,Integer functionaryId)throws TooManyValuesException, NoSuchObjectException;
	public Assignment getLastAssignmentByEmp(Integer empId);
	//to create grade master

	public abstract void createGradeMstr(GradeMaster gradeMstr)throws Exception;

	public abstract GradeMaster getGradeMstrById(Integer gradeId) throws Exception;
	//to update grade master

	public abstract void updateGradeMstr(GradeMaster gradeMstr)throws Exception;
	//to delete grade master
	public abstract void removeGradeMstr(GradeMaster gradeMstr)throws Exception;

	public abstract List getAllGradeMstr()throws Exception;

	//get all degignation based on gradeId
	@Deprecated
	public abstract List getAllDesgBasedOnGrade(Integer gradeId) throws Exception;

	public abstract List<PersonalInformation> getAllEmpByGrade(Integer gradeId);

	public abstract List<PersonalInformation> getAllHodEmpByDept(Integer deptId) throws Exception;
	public abstract List<EmployeeView> getAllHodEmpViewByDept(Integer deptId) throws Exception;

	/*
	 * search employee by department,designation,functionary,code and name and employee type
	 */
	@Deprecated
	public abstract List searchEmployee(Integer departmentId,Integer designationId,Integer functionaryId,String code,String name,Integer status,Integer empType)throws Exception;
	/*
	 * Api to get Employee based on Position Id and Date
	 * toDate will take sysdate if it is not provided.
	 */
	public abstract PersonalInformation  getEmpForPositionAndDate(Date date,Integer posId)throws Exception;


	public abstract List searchEmployeeByGrouping(LinkedList<String> groupingByOrder)throws Exception;


	public abstract List getAllDesignationByDept(Integer deptId);

	public abstract List getAllActiveUsersByGivenDesg(Integer DesgId)throws Exception;

	public abstract List<EmployeeView> getEmployeeWithTempAssignment(Date givenDate,Integer posId);

	public abstract List<EmployeeView> getEmployeeWithTempAssignment(String code,Date givenDate,Integer posId);

	public abstract List getEmpTempAssignment(String code,Date givenDate,Integer posId);

	 public List<Integer> getAssignmentsForEmp(Integer empId,Date givenDate) throws EGOVException;

	 /**
	  * API that will return all positions for a user(temporary and permanent) for a date.
	  * consider system date if date is not provided
	  */

	 public List<Position> getPositionsForUser(User user, Date date)throws EGOVException;

	 public abstract List getEmpPrimaryAssignment(String code,Date givenDate,Integer posId);
	 public  List searchEmployee(Integer status,Date fromDate,Date toDate)throws Exception;


	 /*
		 * Api to get the department for the employee who is HOD of
		 * the department
		 */
		public abstract List getListOfDeptBasedOnHodLogin(String userName);
		/*
		 * Api to get the department for the employee who has logged in
		 *
		 */
		public abstract List getListOfDeptBasedOnUserDept(String userName);

		public abstract boolean isFilterByDept();
		public List<Long> getNomineeMasterForEmp(Integer empId);

		public void  createNomineeMaster(EmployeeNomineeMaster nomineeMaster);
		/**
		 * API to fetch employeeInfo based on department and designation
		 */
		public abstract List<EmployeeView> getEmployeeInfoBasedOnDeptAndDesg(Integer deptId ,Integer desgId);
		public List<EmployeeView> getEmployeeInfoBasedOnDeptAndDate(Integer deptId, Date date);
		public Boolean isEmployeeSuspended(Integer empId);
		public List<EmployeeView> searchEmployee(Integer designationId,String code,String name,
				Integer statusId,Integer empTypeId,Map<String,Integer> finParams)throws Exception;
		public List<PersonalInformation> getAllEmployees();
		public List getListOfUsersNotMappedToEmp() throws Exception;
		public List <PersonalInformation>getEmpListForPositionAndDate(Date dateEntered,Integer posId)throws Exception;
		
		public  List searchEmployeeForNominees(Integer departmentId,Integer designationId,Integer functionaryId,String code,String name,Integer status,Integer empType)throws Exception;
		public DesignationMaster getPresentDesignation(Integer  idPersonalInformation);

}


