/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */

package org.egov.pims.dao;

import org.apache.log4j.Logger;
import org.egov.eis.entity.Jurisdiction;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.commons.exception.NoSuchObjectException;
import org.egov.commons.exception.TooManyValuesException;
import org.egov.pims.commons.Designation;
import org.egov.pims.model.PersonalInformation;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PersonalInformationHibernateDAO implements PersonalInformationDAO
{
    
	private static final Logger LOGGER = Logger.getLogger(PersonalInformationHibernateDAO.class); 
	
	private final static String STR_CURRDATE= "currDate";
	
	@Autowired
	private BoundaryService boundaryService;
	
	@PersistenceContext
	private EntityManager entityManager;
    
	public Session  getCurrentSession() {
		return entityManager.unwrap(Session.class);
	}
	

	public PersonalInformation getPersonalInformationByID(Integer idPersonalInformation)
	{
		Query qry = getCurrentSession().createQuery("from PersonalInformation P where P.idPersonalInformation =:idPersonalInformation ");
		qry.setInteger("idPersonalInformation", idPersonalInformation);
		return (PersonalInformation)qry.uniqueResult();
	}

	public Map getAllPIMap()
	{
		try
		{
			Query qry = getCurrentSession().createQuery("from PersonalInformation P order by P.idPersonalInformation");
			Map<Integer,String> retMap = new LinkedHashMap<Integer,String>();
			for (Iterator iter = qry.iterate(); iter.hasNext();)
			{
				PersonalInformation egpimsPersonalInformation = (PersonalInformation)iter.next();
				retMap.put(egpimsPersonalInformation.getIdPersonalInformation(),egpimsPersonalInformation.getEmployeeCode());
			}

			return retMap;
		}
		catch (Exception e)
		{
				throw new ApplicationRuntimeException("Hibernate Exception : "+e.getMessage(),e);
		}
	}

	public PersonalInformation getPersonalInformationByUserId(Long userId) 
	{
		Query qry = getCurrentSession().createQuery("from PersonalInformation P where P.userMaster.id =:userId ");
		qry.setLong("userId", userId);
		return (PersonalInformation)qry.uniqueResult();
		
	}
	
	public void deleteLangKnownForEmp(PersonalInformation personalInformation)
	{
		Query qry = getCurrentSession().createSQLQuery("delete  from EGEIS_LANG_KNOWN B where B.id = :id ");
		qry.setInteger("id", personalInformation.getIdPersonalInformation());
		
	}
	public List getListOfPersonalInformationByEmpIdsList(List empIdsList)
	{
		List <PersonalInformation> list = null;
		if(empIdsList!=null && !empIdsList.isEmpty())
		{			
			Query qry = getCurrentSession().createQuery("from PersonalInformation per where per.idPersonalInformation in (:empIdsList) order by per.employeeCode");
			if(empIdsList.size() <= 1000)
			{
				qry.setParameterList("empIdsList", empIdsList);		
				list = qry.list();
				return list;
			}
			else
			{
				//If it exceeds 1000, get the list for each 1000 employees iteratively.
				int noOfSublists = empIdsList.size()/1000;
				int remainingItems= empIdsList.size() % 1000 ;		
				int initialVal=0;
				List <PersonalInformation> listFinal = new ArrayList();
				for(int i=1; i<=noOfSublists; i++)
				{				
					qry.setParameterList("empIdsList", empIdsList.subList(initialVal,i*1000));		
					list = qry.list();
					listFinal.addAll(list);
					initialVal=i*1000;
				}
				qry.setParameterList("empIdsList", empIdsList.subList(initialVal,initialVal+remainingItems));		
				list = qry.list();
				listFinal.addAll(list);						
				return listFinal;
			}						
		}
		else
		{
			return list;
		}
		
	}
	
	public List getListOfUsersByBoundaryId(Long boundaryId) throws NoSuchObjectException
	{
		List userObjList = new ArrayList();
		List bndryObjList = boundaryService.getParentBoundariesByBoundaryId(boundaryId);
		Date currDate =new Date();
		if(!bndryObjList.isEmpty())
		{
			Query qry = getCurrentSession().createQuery("select J FROM JurisdictionValues JurVal, Jurisdiction J  where " +
						"JurVal.boundary in (:bndryObjList) and JurVal.userJurLevel.id=J.id and JurVal.isHistory='N' and J.user.active=true and "+
						"(" +
						"(JurVal.toDate IS NULL and JurVal.fromDate <= :currDate) " +
						"OR " +
						"(JurVal.fromDate <= :currDate and JurVal.toDate >= :currDate)) ");
			qry.setParameterList("bndryObjList",bndryObjList);
			qry.setDate(STR_CURRDATE,currDate);

			for (Iterator iter = qry.iterate(); iter.hasNext();)
			{
				Jurisdiction jurObj = (Jurisdiction)iter.next();
				//userObjList.add(jurObj.getUser());
			}
		}
		return userObjList;

 	}
	/**
	 * To get list of users which belong to given boundary. If give boundary is Zone, then it will get all the wards 
	 * under that zone and search for users for that Zone and wards within that zone. 
	 * @param boundaryId
	 * @return
	 * @throws NoSuchObjectException
	 */
	public List getListOfUsersForGivenBoundaryId(Long boundaryId) throws NoSuchObjectException
	{
		List userObjList = new ArrayList();
		List bndryObjList = new ArrayList();	
			
		//get All Children of given boundary
		bndryObjList = boundaryService.getChildBoundariesByBoundaryId(boundaryId);
		//Add parent boundary
		Boundary bnd = boundaryService.getBoundaryById(boundaryId);
		if(bnd != null)
			bndryObjList.add(bnd);
		Date currDate =new Date();
		if(!bndryObjList.isEmpty())
		{
			Query qry = getCurrentSession().createQuery("select J FROM JurisdictionValues JurVal, Jurisdiction J  where " +
						"JurVal.boundary in (:bndryObjList) and JurVal.userJurLevel.id=J.id and JurVal.isHistory='N' and J.user.active=true and "+
						"(" +
						"(JurVal.toDate IS NULL and JurVal.fromDate <= :currDate) " +
						"OR " +
						"(JurVal.fromDate <= :currDate and JurVal.toDate >= :currDate)) ");
			qry.setParameterList("bndryObjList",bndryObjList);
			qry.setDate(STR_CURRDATE,currDate);

			for (Iterator iter = qry.iterate(); iter.hasNext();)
			{
				Jurisdiction jurObj = (Jurisdiction)iter.next();
				//userObjList.add(jurObj.getUser());
			}
		}
		return userObjList;

 	}
	/**
	 * This is used for workflow
	 * Getting employee by passing dept,desig,boundary
	 * @param deptId
	 * @param designationId
	 * @param Boundaryid
	 * @return temAssigned employee if temp Assignement is present otherwise primary assigned employee 
	 * @throws TooManyValuesException
	 * @throws NoSuchObjectException
	 */
	public PersonalInformation getEmployee(Integer deptId, Integer designationId, Long boundaryId)throws TooManyValuesException, NoSuchObjectException
	{
		PersonalInformation personalInformation= null;
		Query qry1=null;
		try
		{
			List userList = new ArrayList();
			List<PersonalInformation> empList;
			Date currDate =new Date();
			if(boundaryId!=null && boundaryId!=0)
			{
				//FIXME: should  take actual instance of boundary
				//fixed
			userList = getListOfUsersByBoundaryId(boundaryId);
			} 
			if(userList.isEmpty())
			{
				throw new NoSuchObjectException("user.Obj.null");
				
			}
			else{
				qry1 = getCurrentSession().createQuery("select P from PersonalInformation P, Assignment A where" +
						" P.idPersonalInformation=A.employee.idPersonalInformation and " +
						" A.deptId.id=:deptId and" +
						" A.desigId.designationId=:designationId and " +
						" A.isPrimary = 'N' and " +
						" P.userMaster in (:userObjList) and (" +
						"(A.toDate IS NULL and A.fromDate <= :currDate) " +
						"OR " +
						"(A.fromDate <= :currDate and A.toDate >= :currDate))");  
		qry1.setInteger("deptId",deptId);
		qry1.setInteger("designationId", designationId);
		qry1.setParameterList("userObjList",userList);
		qry1.setDate(STR_CURRDATE,currDate);
		empList = qry1.list();
		if(empList.size()==0){
				qry1 = getCurrentSession().createQuery("select P from PersonalInformation P, Assignment A where" +
						" P.idPersonalInformation=A.employee.idPersonalInformation and " +
						" A.deptId.id=:deptId and" +
						" A.desigId.designationId=:designationId and " +
						" A.isPrimary = 'Y' and " +
						" P.userMaster in (:userObjList) and (" +
						"(A.toDate IS NULL and A.fromDate <= :currDate) " +
						"OR " +
						"(A.fromDate <= :currDate and A.toDate >= :currDate))");  
				qry1.setInteger("deptId",deptId);
				qry1.setInteger("designationId", designationId);
				qry1.setParameterList("userObjList",userList);
				qry1.setDate(STR_CURRDATE,currDate);
				empList = qry1.list();
				if(empList.isEmpty()){
					throw new NoSuchObjectException("personalinformation.object.notFound");
				}
				if(empList.size()>1)
				{
					throw new TooManyValuesException("personalinformation.object.Foundmorethanone");

				}
				if(empList.size()==1)
				{
					personalInformation = empList.get(0);
				}
		}
		else if(empList.size()>1){
			throw new TooManyValuesException("tempAssigned.personalinformation.object.Foundmorethanone");
		}
		else if(empList.size()==1){
			personalInformation = empList.get(0);
		}

			}
		return (personalInformation);

		}
		 catch(Exception e)
        {
           throw new ApplicationRuntimeException(e.getMessage(),e);
        }


	}
	
	
	/**
	 * This is used for workflow
	 * Getting employee by passing deptId,desigId,boundaryId,functionaryId
	 * @param deptId
	 * @param designationId
	 * @param Boundaryid
	 * @return temAssigned employee if tempAssignement is present otherwise primary assigned employee
	 * @throws TooManyValuesException
	 * @throws NoSuchObjectException
	 */
	public PersonalInformation getEmployeeByFunctionary(Long deptId, Long designationId, Long boundaryId,Integer functionaryId)throws TooManyValuesException, NoSuchObjectException
	{
		PersonalInformation personalInformation= null;
		Query qry1=null;
		try
		{
			List userList = new ArrayList();
			List<PersonalInformation> empList;
			Date currDate =new Date();
			if(boundaryId!=null && boundaryId!=0)
			{
				//FIXME: should  take actual instance of boundary
				//fixed
			userList = getListOfUsersByBoundaryId(boundaryId);
			} 
			if(userList.isEmpty())
			{
				
				throw new NoSuchObjectException("user.Obj.null");
			}
			else
			{
				qry1 = getCurrentSession().createQuery("select P from PersonalInformation P, Assignment A where" +
						" P.idPersonalInformation=A.employee.idPersonalInformation and " +
						" A.deptId.id=:deptId and" +
						" A.desigId.designationId=:designationId and " +
						"A.functionary.id=:functionaryId and " +
						" A.isPrimary = 'N' and "+
						" P.userMaster in (:userObjList) and (" +
						"(A.toDate IS NULL and A.fromDate <= :currDate) " +
						"OR " +
						"(A.fromDate <= :currDate and A.toDate >= :currDate))");  
		qry1.setLong("deptId",deptId);
		qry1.setLong("designationId", designationId);
		qry1.setInteger("functionaryId", functionaryId);
		qry1.setParameterList("userObjList",userList);
		qry1.setDate(STR_CURRDATE,currDate);
		empList = qry1.list();
		if(empList.size()==0){
			qry1 = getCurrentSession().createQuery("select P from PersonalInformation P, Assignment A where" +
					" P.idPersonalInformation=A.employee.idPersonalInformation and " +
					" A.deptId.id=:deptId and" +
					" A.desigId.designationId=:designationId and " +
					"A.functionary.id=:functionaryId and " +
					" A.isPrimary = 'Y' and " +
					" P.userMaster in (:userObjList) and (" +
					"(A.toDate IS NULL and A.fromDate <= :currDate) " +
					"OR " +
					"(A.fromDate <= :currDate and A.toDate >= :currDate))");  
			qry1.setLong("deptId",deptId);
			qry1.setLong("designationId", designationId);
			qry1.setInteger("functionaryId", functionaryId);
			qry1.setParameterList("userObjList",userList);
			qry1.setDate(STR_CURRDATE,currDate);
			empList = qry1.list();
			if(empList.size() == 0){
				throw new NoSuchObjectException("personalinformation.object.notFound");
			}
			if(empList.size()>1)
			{
				throw new TooManyValuesException("personalinformation.object.Foundmorethanone");

			}
			if(empList.size()==1)
			{
				personalInformation = empList.get(0);
			}

		}else if(empList.size()>1){
			throw new TooManyValuesException("personalinformation.object.Foundmorethanone");
		}
		else if(empList.size()==1){
			personalInformation = empList.get(0);
		}
		
			}
		return (personalInformation);

		}
		 catch(Exception e)
        {
			
           throw new ApplicationRuntimeException(e.getMessage(),e);
        }


	}
	
	
	
	
	/**
	  * Returning temporary  assigned employee object by pepartment,designation,functionary,date 
	  * @param deptId
	  * @param DesigId
	  * @param functionaryId
	  * @param onDate
	  * @return Employee
	  * @throws Exception 
	  */
	 public PersonalInformation getTempAssignedEmployeeByDeptDesigFunctionaryDate(Integer deptId, Integer desigId, Integer functionaryId, Date onDate) throws Exception{
		 PersonalInformation tempAssignedEemployee = null;
		 LOGGER.info("Inside temp assigned emp API-----------");
		 List<PersonalInformation> listEmployee = null;
		 Query qry = getCurrentSession().createQuery("select A.employee from Assignment A where " +
								"A.deptId.id=:deptId and " +
								"A.desigId.designationId=:desigId and " +
								"A.functionary.id=:functionaryId and " +
								"A.isPrimary = 'N' and " +
								"((A.toDate IS NULL and A.fromDate <= :onDate) OR " +
								"(A.fromDate <= :onDate and A.toDate >= :onDate))");
		qry.setInteger("deptId",deptId);
		qry.setInteger("desigId", desigId);
		qry.setInteger("functionaryId", functionaryId);
		qry.setDate("onDate",onDate);
		LOGGER.info("Inside temp assigned emp API query-----------"+qry.getQueryString());
		listEmployee = qry.list();
		if(listEmployee.size()==0){
        	throw new NoSuchObjectException("tempAssigned.personalinformation.object.notFound");
		}
		if(listEmployee.size()>1){
			throw new TooManyValuesException("tempAssigned.personalinformation.object.Foundmorethanone");
		}
		if(listEmployee.size()==1){
			tempAssignedEemployee = listEmployee.get(0);
		}
		 return tempAssignedEemployee;
	 }
	 
	 public List getAllDesignationByDept(Integer deptId)throws TooManyValuesException, NoSuchObjectException
		{
		 	List<Designation> desgMstr = null;
			try
			{
				
				
					Query qry = getCurrentSession().createQuery("from Designation dm where dm.deptId =:deptId");
					qry.setInteger("deptId",deptId);
					LOGGER.info("QUERY TEST-----------"+qry.getQueryString());
					desgMstr = qry.list();
				
				
			}
			 catch(Exception e)
		        {
					
		           throw new ApplicationRuntimeException("system.error", e);
		        }
			 


		
		 	return desgMstr;
		}
	
	 public List getAllActiveUsersByGivenDesg(Integer desgId)
	 {
		 List<User> userList = null; 
			
			try {					
						Query qry = getCurrentSession().createQuery("from User u where u.id in (select ev.userMaster.id from EmployeeView ev where ev.desigId.designationId =:desgId) and u.active=true ");
						qry.setInteger("desgId",desgId);					
						userList = qry.list();
						
					
				} catch (RuntimeException e) {
					throw new ApplicationRuntimeException("Exception while getting users for given designation",e);
					
				}
		
				
		 return userList;
	 }
	 
	 public List<PersonalInformation> getAllEmpByGrade(Integer gradeId) throws Exception
	 {
		 List<PersonalInformation> listEmployee = null;
		 Query qry = getCurrentSession().createQuery("select distinct A.employee from Assignment A where A.gradeId.id=:gradeId ");

		qry.setInteger("gradeId",gradeId);
		listEmployee = qry.list();
		return listEmployee;
	 }
	
	 /**
	  * This is used for getting the users (both active and inactive) who are not mapped to any of the employees
	  */	 
	 public  List getListOfUsersNotMappedToEmp()
	 {
		 Query qry = getCurrentSession().createQuery("from User UI where id not in("+
			"select userMaster.id from PersonalInformation  where  userMaster.id is not null) order by UI.userName");
		 
		 return qry.list();
	 }

	@Override
	@Transactional
	public void create(final PersonalInformation egpimsPersonalInformation) {
		getCurrentSession().save(egpimsPersonalInformation);
	}

	@Override
	@Transactional
	public void update(final PersonalInformation egpimsPersonalInformation) {
		getCurrentSession().update(egpimsPersonalInformation);
	}

}


