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
package org.egov.bpa.services.extd.common;


import org.apache.log4j.Logger;
import org.egov.bpa.constants.BpaConstants;
import org.egov.commons.EgwStatus;
import org.egov.commons.Functionary;
import org.egov.eis.entity.Assignment;
import org.egov.eis.entity.EmployeeView;
import org.egov.eis.service.EmployeeService;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EisUtilService;
import org.egov.pims.service.EmployeeServiceOld;
import org.egov.pims.utils.EisManagersUtill;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

//import org.egov.lib.rjbac.user.dao.UserDAO;
//import org.egov.pims.service.EisManager;

@SuppressWarnings("unchecked")

public class BpaPimsInternalExtnServiceFactory {

	private EisUtilService eisService;	
	private PersistenceService persistenceService;
	//private EisManager eisMgr;//Phionix TODO
	private EmployeeServiceOld employeeServiceOld;
	@Autowired
	private EmployeeService employeeService;
	
	private Logger logger=Logger.getLogger(BpaPimsInternalExtnServiceFactory.class);
   // <bean id="userDao" class="org.egov.lib.rjbac.user.dao.UserDAO" scope="prototype"/>
	//private UserDAO userDao;
	private BpaCommonExtnService bpaCommonExtnService;
	
	public BpaCommonExtnService getBpaCommonExtnService() {
		return bpaCommonExtnService;
	}

	public void setBpaCommonExtnService(BpaCommonExtnService bpaCommonService) {
		this.bpaCommonExtnService = bpaCommonService;
	}

	public PersistenceService getPersistenceService() {
		return persistenceService;
	}

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}
	/*
	
	public UserDAO getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDAO userDao) {
		this.userDao = userDao;
	}

	public EisManager getEisMgr() {
		return eisMgr;
	}

	public void setEisMgr(EisManager eisMgr) {
		this.eisMgr = eisMgr;
	}*/

	public EisUtilService getEisService() {
		return eisService;
	}

	public void setEisService(EisUtilService eisService) {
		this.eisService = eisService;
	}
	
	/**
	 * 
	 * @param departmentId
	 * @param desgId
	 * @return
	 */
	
	public List<EmployeeView> getApproverListWithPositionsByPassingDeptAndDesignationId(String departmentId,String desgId) 
	{
		logger.info("...inside getApproverListByPassingDeptAndDesignationId....");
		HashMap<String,String> paramMap = new HashMap<String, String>();
		if(departmentId!=null && !departmentId.equals(""))
			paramMap.put("departmentId",departmentId);
		if(desgId!=null && !desgId.equals(""))			
			paramMap.put("designationId", desgId);
		List<EmployeeView> empList;
		List<User> approverList = new ArrayList<User>();
		empList = eisService.getEmployeeInfoList(paramMap);
		return empList;
	}
	
	public List<User> getApproverListByPassingDeptAndDesignationId(String departmentId,String desgId) 
	{
		logger.info("...inside getApproverListByPassingDeptAndDesignationId....");
		HashMap<String,String> paramMap = new HashMap<String, String>();
		if(departmentId!=null && !departmentId.equals(""))
			paramMap.put("departmentId",departmentId);
		if(desgId!=null && !desgId.equals(""))			
			paramMap.put("designationId", desgId);
		List<EmployeeView> empList;
		List<User> approverList = new ArrayList<User>();
		empList = eisService.getEmployeeInfoList(paramMap);
		for (EmployeeView emp : empList) {
			if (emp.getEmployee() != null)
				approverList.add((User)emp.getEmployee());

		}
		return approverList;
	}
	
	/**
	 * 
	 * @param deptId
	 * @param date
	 * @return
	 */
	public List<Designation> getAllDesignationByDeptId(Integer deptId,Date date)
	{
		List<Designation> designationList=new ArrayList<Designation>();
		if(date==null || date.equals(""))
		{
			date= new Date();
		}
		if(deptId==null)
			throw new EGOVRuntimeException("Department id is required.");
		else{
			designationList = eisService.getAllDesignationByDept(deptId, date);
		}
		return designationList;
	}
	
	/**
	 * 
	 * @param egstoresAdvanceRequisition
	 * @return
	 *  This method returns Assignment for CurrentLoginUser
	 */
	
	public Assignment getLatestAssignmentForCurrentLoginUser() {
		PersonalInformation personalInformation=null;
		if(getCurrentUserId()!=0){
			personalInformation = EisManagersUtill.getEmployeeService().getEmpForUserId((long)getCurrentUserId());
			//personalInformation=eisMgr.getEmpForUserId(getCurrentUserId());		    
		}
		Assignment assignment=null;
		if(personalInformation!=null){
			//TODo Phionix : employeeService.getLatestAssignmentForEmployee(
			assignment=employeeServiceOld.getLatestAssignmentForEmployee(personalInformation.getIdPersonalInformation());
			//assignment=eisMgr.getLatestAssignmentForEmployee(personalInformation.getIdPersonalInformation());			
		}
		return assignment; 
	}
	
	public Long getCurrentUserId() {
		return (EgovThreadLocals.getUserId());
	}
	
	/**
	 * 
	 * @param userId
	 * @return
	 */
	public Position getPositionByUserId(Long userId)
	{
		if(userId==null)
			throw new EGOVRuntimeException("User id is null");
		Position pos=eisService.getPrimaryPositionForUser((long)userId, new Date());
		//TODO Phionix
		return pos;
	}
	
	/**
	 * 
	 * @param positionId
	 * @param date
	 * @return
	 */
	public User getUserForPosition(Integer positionId, Date date) 
	{
		if(positionId==null)
			throw new EGOVRuntimeException("Position Id is null");
		User user=eisService.getUserForPosition((long)positionId, date);
		//pionix TODO
		return user;
	}
	
	public Assignment getAssignmentByUserId(Integer userId)
	{
		PersonalInformation personalInformation=null;
		if(userId!=0){
			PersonalInformation personalInfo = EisManagersUtill.getEmployeeService().getEmpForUserId((long)getCurrentUserId());
			//Pionix TODO EisManager changed into EisManagerUtil
			//personalInformation=eisMgr.getEmpForUserId(getCurrentUserId());		    
		}
		Assignment assignment=null;
		if(personalInformation!=null){
			//TODo Phionix : employeeService.getLatestAssignmentForEmployee(
			assignment=employeeServiceOld.getLatestAssignmentForEmployee(personalInformation.getIdPersonalInformation());
			
			//assignment=eisMgr.getLatestAssignmentForEmployee(personalInformation.getIdPersonalInformation());			
		}
		return assignment; 
	}
	//TODO Pionix changed from Intiger into Long
	public Long getPrimaryDepartmentforLoggedinUser(){
		Long deptId=null;
		Long userId=(EgovThreadLocals.getUserId());		
		PersonalInformation employee= (PersonalInformation) persistenceService.find("from PersonalInformation where userMaster.id=?",userId);
		if(employee!=null){
			List<Assignment> assignmentList=eisService.getPrimartAssignmentForGivenDateRange(employee.getId(), new Date(), null);
			if(assignmentList!=null&&assignmentList.size()!=0){
				//Getting the department id from primary assignment of the user
				if(assignmentList.get(0).getDepartment()!=null){
					 deptId=assignmentList.get(0).getDepartment().getId();
					
				}
			}
		}
		return deptId;
	}
	

	public List<EmployeeView> getEmployeeInfoList() {
		List<EmployeeView>  empList=new ArrayList();
		HashMap paramMap=new HashMap();
		Long userId=(EgovThreadLocals.getUserId());
		
		//Getting the employee Id from personal information to get primary assignments by passing user id
		PersonalInformation employee= (PersonalInformation) persistenceService.find("from PersonalInformation where userMaster.id=?",userId);
		if(employee!=null){
			List<Assignment> assignmentList=eisService.getPrimartAssignmentForGivenDateRange(employee.getId(), new Date(), null);
			if(assignmentList!=null&&assignmentList.size()!=0){
				//Getting the department id from primary assignment of the user
				if(assignmentList.get(0).getDepartment()!=null){
					Long deptId=assignmentList.get(0).getDepartment().getId();
					paramMap.put("departmentId", Long.toString(deptId));
				}
				Designation designation=	(Designation) persistenceService.find("from Designation where designationName=?",BpaConstants.ASSISTANTDESIGNATION);
				Functionary functionary=	(Functionary) persistenceService.find("from Functionary where name=?",BpaConstants.TOWNPLANNINGFUNCTIONARY);
				EgwStatus status=(EgwStatus) persistenceService.find("from EgwStatus mod where moduletype=? and code=?",BpaConstants.PIMSMODULETYPE,BpaConstants.PIMSEMPLOYEDCODE);
				if(designation!=null){
					paramMap.put("designationId",designation.getId() );	 	
				}
				if(functionary!=null)
				paramMap.put("functionaryId", Integer.toString(functionary.getId()));
				if(status!=null)
					paramMap.put("status", Integer.toString(status.getId()));
			
				empList= eisService.getEmployeeInfoList(paramMap);
			}
		}
		return empList;
	}
	
	/**
	 * @Description Returns the position of an employee 
	 * @param boundaryImpl
	 * @return
	 */
	public Position getEmployeeInfoList(Boundary boundaryImpl) { 
		List<EmployeeView>  empList=new ArrayList();
		Long boundaryId=null;
		Long userId=(EgovThreadLocals.getUserId());
		//PHIONIX TODO
		if (boundaryImpl.getBoundaryType() != null) {
			if (boundaryImpl.getBoundaryType().getName().equalsIgnoreCase("zone")) {
				boundaryId = boundaryImpl.getId();
			} else if (boundaryImpl.getBoundaryType().getName().equalsIgnoreCase("ward")
					&& boundaryImpl.getParent() != null) {
				boundaryId = boundaryImpl.getParent().getId();
			}
		}
		empList = getEmployeeList(userId,boundaryId);
		if (empList != null && empList.size() != 0) {
			return empList.get(0).getPosition();
		}
		return null; 
	}
		
	/*
	 * get PrimaryPosition By passing userId 
	 */
	public Position getEmployeeInfoListForLPCreator(Long userId){ 
		if(userId!=null){
		Position pos=getPositionByUserId(userId);
		if(pos!=null)
		{
			return pos;
		}
	}
		return null;
				
	}	
	
	//Getting the employee Id from personal information to get primary assignments by passing user id
	/**
	 * @Description Returns EmployeeView list based on designation, functionary, status and boundary
	 * @param userId
	 * @param boundaryId
	 * @return
	 */
	public List<EmployeeView> getEmployeeList(Long userId, Long boundaryId) {
		HashMap paramMap=new HashMap();
		List<EmployeeView>  empList=new ArrayList();
		
		//Getting the employee Id from personal information to get primary assignments by passing user id
		PersonalInformation employee= (PersonalInformation) persistenceService.find("from PersonalInformation where userMaster.id=?",userId);
		if (employee != null) {
			List<Assignment> assignmentList = eisService.getPrimartAssignmentForGivenDateRange(employee.getId(), new Date(), null);
			
			if (assignmentList != null && assignmentList.size() != 0) {
				//Getting the department id from primary assignment of the user
				if (assignmentList.get(0).getDepartment() != null) {
					Long deptId = assignmentList.get(0).getDepartment().getId();
					paramMap.put("departmentId", Long.toString(deptId));
				}
				Designation designation =	(Designation) persistenceService.find("from Designation where designationName=?",BpaConstants.ASSISTANTDESIGNATION);
				Functionary functionary = (Functionary) persistenceService.find("from Functionary where name=?",BpaConstants.TOWNPLANNINGFUNCTIONARY);
				EgwStatus status = (EgwStatus) persistenceService.find("from EgwStatus mod where moduletype=? and code=?",BpaConstants.PIMSMODULETYPE,BpaConstants.PIMSEMPLOYEDCODE);
				if (designation != null) {
					paramMap.put("designationId",designation.getId() );	 	
				}
				if (functionary != null) {
					paramMap.put("functionaryId", Integer.toString(functionary.getId()));
				}
				if (status != null) {
					paramMap.put("status", Integer.toString(status.getId()));
				}
				if(boundaryId != null)  {
					paramMap.put("boundaryId", Long.toString(boundaryId));
				}
				empList = eisService.getEmployeeInfoList(paramMap);
			}
		}
		return empList;
	}
	
	/* In this API,
	 * 1. Get all "Assistant" Designation employees with Functionary as "TP" 
	 * 2. Get all boundary type as "Zone" where heirarchy type as "ADMINISTRATION".
	 * 3. Check any user mapped with zonal jurisdiction values. by passing boundary type and user id.
	 * Get randomly the first user who mapped to zone.
	 */
	//
	public Position getAssistantDesignationEmployeeInfoList(Boundary boundaryImpl) {
		
		List<EmployeeView>  empList=new ArrayList();
		HashMap paramMap=new HashMap();
		Long boundaryId=null;
		//Integer boundaryTypeId=null;
		if(boundaryImpl!=null){
		if (boundaryImpl.getBoundaryType() != null) {
				if (boundaryImpl.getBoundaryType().getName().equalsIgnoreCase("zone")) {
					boundaryId = boundaryImpl.getId();
				} else if (boundaryImpl.getBoundaryType().getName().equalsIgnoreCase("ward")
						&& boundaryImpl.getParent() != null) {
					boundaryId = boundaryImpl.getParent().getId();
				}
			}
				Designation designation=	(Designation) persistenceService.find("from Designation where designationName=?",BpaConstants.ASSISTANTDESIGNATION);
				Functionary functionary=	(Functionary) persistenceService.find("from Functionary where name=?",BpaConstants.TOWNPLANNINGFUNCTIONARY);
				EgwStatus status=(EgwStatus) persistenceService.find("from EgwStatus mod where moduletype=? and code=?",BpaConstants.PIMSMODULETYPE,BpaConstants.PIMSEMPLOYEDCODE);
				
				//Get zone as Boundary Type
				BoundaryType bndryType=getBpaCommonExtnService().getBoundaryTypeByPassingBoundaryTypeAndHierarchy(BpaConstants.BOUNDARYTYPE,BpaConstants.HEIRARCHYTYPE);
				
				if(designation!=null){
					paramMap.put("designationId",designation.getId() );	 	
				}
				if(functionary!=null)
				paramMap.put("functionaryId", Integer.toString(functionary.getId()));
				if(status!=null)
					paramMap.put("status", Integer.toString(status.getId()));
				if(boundaryId != null)  {
					paramMap.put("boundaryId", Long.toString(boundaryId));
				}
				//Get all "Assistant" Designation employees with Functionary as "TP" 
				empList= eisService.getEmployeeInfoList(paramMap);
				if (empList != null && empList.size() != 0) {
					
					return empList.get(0).getPosition();
				}
				
		 }
		
		return null;
	}

	/* In this API,
	 * 1. Get all "Executive Engineer" Designation employees with Functionary as "TP" 
	 * 2. Get all boundary type as "Zone" where heirarchy type as "ADMINISTRATION".
	 * 3. Check any user mapped with zonal jurisdiction values. by passing boundary type and user id.
	 * Get randomly the first user who mapped to zone.
	 */
	//
	public Position getExecEngineerDesignationEmployeeInfoList(Boundary boundaryImpl) {
		
		List<EmployeeView>  empList=new ArrayList();
		HashMap paramMap=new HashMap();
		Long boundaryId=null;
		//Integer boundaryTypeId=null;
		if(boundaryImpl!=null){
		if (boundaryImpl.getBoundaryType() != null) {
				if (boundaryImpl.getBoundaryType().getName().equalsIgnoreCase("zone")) {
					boundaryId = boundaryImpl.getId();
				} else if (boundaryImpl.getBoundaryType().getName().equalsIgnoreCase("ward")
						&& boundaryImpl.getParent() != null) {
					boundaryId = boundaryImpl.getParent().getId();
				}
			}
				Designation designation=	(Designation) persistenceService.find("from Designation where designationName=?",BpaConstants.EXECUTIVEENGINEERDESIGNATION);
				Functionary functionary=	(Functionary) persistenceService.find("from Functionary where name=?",BpaConstants.TOWNPLANNINGFUNCTIONARY);
				EgwStatus status=(EgwStatus) persistenceService.find("from EgwStatus mod where moduletype=? and code=?",BpaConstants.PIMSMODULETYPE,BpaConstants.PIMSEMPLOYEDCODE);
				
				//Get zone as Boundary Type
				BoundaryType bndryType=getBpaCommonExtnService().getBoundaryTypeByPassingBoundaryTypeAndHierarchy(BpaConstants.BOUNDARYTYPE,BpaConstants.HEIRARCHYTYPE);
				
				if(designation!=null){
					paramMap.put("designationId",designation.getId() );	 	
				}
				//TODO: CHECK AND ADD THIS CONDITION. WHETHER USER NEED TO MAP TO "TP" FUNCTIONARY
				if(functionary!=null)
				paramMap.put("functionaryId", Integer.toString(functionary.getId()));
				
				if(status!=null)
					paramMap.put("status", Integer.toString(status.getId()));
				if(boundaryId != null)  {
					paramMap.put("boundaryId", Long.toString(boundaryId));
				}
				//Get all "Executive Engineer" Designation employees with Functionary as "TP" 
				empList= eisService.getEmployeeInfoList(paramMap);
				if (empList != null && empList.size() != 0) {
					System.out.println(empList.get(0).getPosition().getName());
					return empList.get(0).getPosition();
				}
			 }
		
		return null;
	}
	/* In this API,
	 * 1. Get all employees with Functionary as "TP"  
	 * 2. Get all boundary type as "Zone" where heirarchy type as "ADMINISTRATION".
	 * 3. Check any user mapped with zonal jurisdiction values. by passing boundary type and user id.
	 * Get randomly the first user who mapped to zone.
	 */
	
	public List<EmployeeView> getEmployeeInfoList(Boundary boundaryImpl, String designationName,String roleList) {
		List<EmployeeView>  empList=new ArrayList();
		HashMap paramMap=new HashMap();
		Long boundaryId=null;
		//Integer boundaryTypeId=null;
		if(boundaryImpl!=null){
		if (boundaryImpl.getBoundaryType() != null) {
				if (boundaryImpl.getBoundaryType().getName().equalsIgnoreCase("zone")) {
					boundaryId = boundaryImpl.getId();
				} else if (boundaryImpl.getBoundaryType().getName().equalsIgnoreCase("ward")
						&& boundaryImpl.getParent() != null) {
					boundaryId = boundaryImpl.getParent().getId();
				}
			}
				Designation designation=	(Designation) persistenceService.find("from Designation where designationName=?",designationName);
				Functionary functionary=	(Functionary) persistenceService.find("from Functionary where name=?",BpaConstants.TOWNPLANNINGFUNCTIONARY);
				EgwStatus status=(EgwStatus) persistenceService.find("from EgwStatus mod where moduletype=? and code=?",BpaConstants.PIMSMODULETYPE,BpaConstants.PIMSEMPLOYEDCODE);
				
				//Get zone as Boundary Type
				BoundaryType bndryType=getBpaCommonExtnService().getBoundaryTypeByPassingBoundaryTypeAndHierarchy(BpaConstants.BOUNDARYTYPE,BpaConstants.HEIRARCHYTYPE);
				
				if(designation!=null){
					paramMap.put("designationId",designation.getId());	 	
				}
				if(functionary!=null)
				paramMap.put("functionaryId", Integer.toString(functionary.getId()));
				if(status!=null)
					paramMap.put("status", Integer.toString(status.getId())); 
				if(boundaryId != null)  {
					paramMap.put("boundaryId", Long.toString(boundaryId));
				}
				if(roleList!=null){
					paramMap.put("roleList",Arrays.asList(roleList.split(",")));
				}
				//Get all "Assistant" Designation employees with Functionary as "TP" 
				empList= eisService.getEmployeeInfoList(paramMap);
				/*if (empList != null && empList.size() != 0) {
					return empList.get(0).getPosition();
				}
				return empList;
				*/
		 }
		return empList;
	}
	/*
	 * API to get list of designation's of the Employee by passing employee and department.
	 */
	public List<EmployeeView> getEVforDesignationsListByEmpAndDept(PersonalInformation emp,Integer approverDepartment){
		
		HashMap<String,Object> criteriaParams =new HashMap<String,Object>();
		criteriaParams.put("departmentId", approverDepartment.toString());
		criteriaParams.put("code", emp.getCode());
		return eisService.getEmployeeInfoList(criteriaParams);
		
	}
	

}
