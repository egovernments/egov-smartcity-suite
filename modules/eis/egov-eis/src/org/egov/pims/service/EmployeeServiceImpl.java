package org.egov.pims.service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;


import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.Accountdetailkey;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CFinancialYear;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.CommonsDaoFactory;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.commons.service.CommonsService;
import org.egov.exceptions.NoSuchObjectException;
import org.egov.exceptions.TooManyValuesException;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.SessionFactory;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.ejb.api.UserService;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.commons.Position;
import org.egov.pims.dao.AssignmentDAO;
import org.egov.pims.dao.AssignmentPrdDAO;
import org.egov.pims.dao.BankDetDAO;
import org.egov.pims.dao.DeptTestsDAO;
import org.egov.pims.dao.DetOfEnquiryOfficerDAO;
import org.egov.pims.dao.DisciplinaryPunishmentApprovalDAO;
import org.egov.pims.dao.DisciplinaryPunishmentDAO;
import org.egov.pims.dao.EduDetailsDAO;
import org.egov.pims.dao.EisDAOFactory;
import org.egov.pims.dao.EmployeeDepartmentDAO;
import org.egov.pims.dao.GenericMasterDAO;
import org.egov.pims.dao.GradeMasterDao;
import org.egov.pims.dao.ImmovablePropDetailsDAO;
import org.egov.pims.dao.LangKnownDAO;
import org.egov.pims.dao.LtcPirticularsDAO;
import org.egov.pims.dao.MovablePropDetailsDAO;
import org.egov.pims.dao.NomimationPirticularsDAO;
import org.egov.pims.dao.PersonAddressDAO;
import org.egov.pims.dao.PersonalInformationDAO;
import org.egov.pims.dao.ProbationDAO;
import org.egov.pims.dao.RegularisationDAO;
import org.egov.pims.dao.TecnicalQualificationDAO;
import org.egov.pims.dao.TrainingPirticularsDAO;
import org.egov.pims.empLeave.model.CalendarYear;
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
import org.egov.pims.model.SearchEmpDTO;
import org.egov.pims.model.ServiceHistory;
import org.egov.pims.model.TecnicalQualification;
import org.egov.pims.model.TrainingPirticulars;
import org.egov.pims.utils.EisConstants;
import org.egov.pims.utils.EisManagersUtill;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;



/**
 * @author deepak
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class  EmployeeServiceImpl  implements EmployeeService{

	/* (non-Javadoc)
	 * @see org.egov.infstr.utils.ejb.support.AbstractStatelessSessionBean#ejbCreate()
	 */
	private static final Logger logger = Logger.getLogger(EmployeeServiceImpl.class);
	private EisUtilService eisService;
	private PersistenceService persistenceService;
	private CommonsService commonsService;
	private Session session;


	public PersistenceService getPersistenceService() {
		return persistenceService;
	}

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}
	private final static Logger LOGGER = Logger.getLogger(EmployeeServiceImpl.class);
	
	private static Session getCurrentSession()
	{				
		return HibernateUtil.getCurrentSession();
	}
	
     @Deprecated
	public List searchEmployee(Integer departmentId,Integer designationId,String code,String name,String searchAll)throws Exception
	{
		
		//session = HibernateUtil.getCurrentSession();
	List<EmployeeView> employeeList = null;
		try
		{

					String mainStr = "from EmployeeView ev where" ;
					if(code!=null&&!code.equals(""))
					{
						mainStr +=" upper(trim(ev.employeeCode)) = :employeeCode and ";
					}
					if(departmentId.intValue() != 0)
					{
						mainStr +=" ev.deptId.id= :deptId and ";
					}
					if(designationId.intValue() != 0)
					{
						mainStr += " ev.desigId.designationId = :designationId and ";
					}
					if(name!= null && !name.equals(""))
					{
						mainStr += " trim(upper(ev.employeeName))  like '%"+name.trim().toUpperCase()+"%' and ";
					}
					/*
					if(code!=null&&!code.equals(""))
					{
						mainStr +=" where upper(trim(ev.employeeCode)) = :employeeCode ";
					}
					else
					{
					*/
						if((searchAll.equals("false") && designationId.intValue() != 0))
						{
							mainStr +=" ((ev.toDate is null and ev.fromDate <= SYSDATE ) OR (ev.fromDate <= SYSDATE AND ev.toDate > SYSDATE)) and ev.isActive = '1' ";
						}
						else if((searchAll.equals("true") && designationId.intValue() != 0))
						{
							mainStr +=" ((ev.toDate is null and ev.fromDate <= SYSDATE ) OR (ev.fromDate <= SYSDATE AND ev.toDate > SYSDATE)) ";
						}
						//Inspite of SearchAll is true or false, if employee code is entered, search for all active and inactive employees
						else if(code!=null && !code.equals(""))
						{
							mainStr +="  ((ev.toDate IS NULL AND ev.fromDate <= SYSDATE) OR (ev.fromDate <= SYSDATE AND ev.toDate > SYSDATE) " +
									" OR (ev.fromDate IN (SELECT MAX (evn.fromDate)  FROM EmployeeView  evn   " +
									" WHERE evn.id = ev.id AND NOT EXISTS  (SELECT evn2.id FROM EmployeeView evn2 WHERE evn2.id = ev.id AND " +
									" ((evn2.toDate  IS NULL AND evn2.fromDate <= SYSDATE) OR (evn2.fromDate <= SYSDATE AND evn2.toDate > SYSDATE)) )))) ";
						}
						else if(searchAll.equals("true") && designationId.intValue() == 0)
						{
							mainStr +="  ((ev.toDate IS NULL AND ev.fromDate <= SYSDATE) OR (ev.fromDate <= SYSDATE AND ev.toDate > SYSDATE) " +
									" OR (ev.fromDate IN (SELECT MAX (evn.fromDate)  FROM EmployeeView  evn   " +
									" WHERE evn.id = ev.id AND NOT EXISTS  (SELECT evn2.id FROM EmployeeView evn2 WHERE evn2.id = ev.id AND " +
									" ((evn2.toDate  IS NULL AND evn2.fromDate <= SYSDATE) OR (evn2.fromDate <= SYSDATE AND evn2.toDate > SYSDATE)) )))) ";
						}
						else
						{
							mainStr +=" ((ev.toDate is null and ev.fromDate <= SYSDATE ) OR (ev.fromDate <= SYSDATE AND ev.toDate > SYSDATE)) ";
						}
					//}


					Query qry = null;
						qry = getCurrentSession().createQuery(mainStr);
					LOGGER.info("qryqryqryqry"+qry.toString());
					if(code!=null&&!code.equals(""))
					{
						qry.setString("employeeCode", code.trim().toUpperCase());
					}
					if(departmentId.intValue() != 0)
					{
						qry.setInteger("deptId", departmentId);
					}
					if(designationId.intValue() != 0)
					{
						qry.setInteger("designationId", designationId);
					}
					employeeList = (List)qry.list();


	} catch (HibernateException he) {
		LOGGER.error(he);
		//HibernateUtil.rollbackTransaction();
		throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
	} catch (Exception he) {
		LOGGER.error(he);

		//HibernateUtil.rollbackTransaction();
		throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);

	}
		return employeeList;
}
	public List searchEmployee(Integer departmentId,Integer designationId,String code,String name,Integer status)throws Exception
	{


		//session = HibernateUtil.getCurrentSession();
	List<EmployeeView> employeeList = null;
		try
		{

					String mainStr = "from EmployeeView ev where ev.isPrimary='Y' and " ;
					if(code!=null&&!code.equals(""))
					{
						mainStr +=" upper(trim(ev.employeeCode)) = :employeeCode and ";
					}
					if(departmentId.intValue() != 0)
					{
						mainStr +=" ev.deptId.id= :deptId and ";
					}
					if(designationId.intValue() != 0)
					{
						mainStr += " ev.desigId.designationId = :designationId and ";
					}
					if(name!= null && !name.equals(""))
					{
						mainStr += " trim(upper(ev.employeeName))  like '%"+name.trim().toUpperCase()+"%' and ";
					}
					if(status.intValue() != 0 )
					{
						mainStr += " ev.employeeStatus.id = :employeeStatus and ";
					}
					/*
					if(code!=null&&!code.equals(""))
					{
						mainStr +=" where upper(trim(ev.employeeCode)) = :employeeCode ";
					}
					else
					{
					*/

					if( status.intValue() != 0 && designationId.intValue() == 0)
					{
						mainStr +=" ((ev.toDate is null and ev.fromDate <= SYSDATE ) OR (ev.fromDate <= SYSDATE AND ev.toDate > SYSDATE)) and ev.employeeStatus.id = :employeeStatus ";
					}
					else if(status.intValue() == 0 && designationId.intValue() != 0)
					{
						mainStr +=" ((ev.toDate is null and ev.fromDate <= SYSDATE ) OR (ev.fromDate <= SYSDATE AND ev.toDate > SYSDATE)) ";
					}
						//Inspite of SearchAll is true or false, if employee code is entered, search for all active and inactive employees
						else if(code!=null && !code.equals(""))
						{
							mainStr +="  ((ev.toDate IS NULL AND ev.fromDate <= SYSDATE) OR (ev.fromDate <= SYSDATE AND ev.toDate > SYSDATE) " +
									" OR (ev.fromDate IN (SELECT MAX (evn.fromDate)  FROM EmployeeView  evn   " +
									" WHERE evn.id = ev.id AND NOT EXISTS  (SELECT evn2.id FROM EmployeeView evn2 WHERE evn2.id = ev.id AND " +
									" ((evn2.toDate  IS NULL AND evn2.fromDate <= SYSDATE) OR (evn2.fromDate <= SYSDATE AND evn2.toDate > SYSDATE)) )))) ";
						}
						else if(status.intValue() != 0 ||designationId.intValue() == 0)
						{
							mainStr +="  ((ev.toDate IS NULL AND ev.fromDate <= SYSDATE) OR (ev.fromDate <= SYSDATE AND ev.toDate > SYSDATE) " +
									" OR (ev.fromDate IN (SELECT MAX (evn.fromDate)  FROM EmployeeView  evn   " +
									" WHERE evn.id = ev.id AND NOT EXISTS  (SELECT evn2.id FROM EmployeeView evn2 WHERE evn2.id = ev.id AND " +
									" ((evn2.toDate  IS NULL AND evn2.fromDate <= SYSDATE) OR (evn2.fromDate <= SYSDATE AND evn2.toDate > SYSDATE)) )))) ";
						}
						else
						{
							mainStr +=" ((ev.toDate is null and ev.fromDate <= SYSDATE ) OR (ev.fromDate <= SYSDATE AND ev.toDate > SYSDATE)) ";
						}
					//}

					Query qry = null;
						qry = getCurrentSession().createQuery(mainStr);
					LOGGER.info("qryqryqryqry"+qry.toString());
					if(code!=null&&!code.equals(""))
					{
						qry.setString("employeeCode", code.trim().toUpperCase());
					}
					if(departmentId.intValue() != 0)
					{
						qry.setInteger("deptId", departmentId);
					}
					if(designationId.intValue() != 0)
					{
						qry.setInteger("designationId", designationId);
					}
					if(status.intValue() != 0)
					{
						qry.setInteger("employeeStatus", status);
					}
					employeeList = (List)qry.list();


	} catch (HibernateException he) {
		LOGGER.error(he);
		//HibernateUtil.rollbackTransaction();
		throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
	} catch (Exception he) {
		LOGGER.error(he);

		//HibernateUtil.rollbackTransaction();
		throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);

	}
		return employeeList;
}



	/*
	 * search employee by department,designation,functionary,code and name
	 */
	public List searchEmployee(Integer departmentId,Integer designationId,Integer functionaryId,String code,String name,Integer status) throws Exception{
		//session = HibernateUtil.getCurrentSession();
		List<EmployeeView> employeeList = null;
		try
		{

					String mainStr = "from EmployeeView ev where  " ;
					if(code!=null&&!code.equals(""))
					{
						mainStr +=" upper(trim(ev.employeeCode)) = :employeeCode and ";
					}
					if(departmentId.intValue() != 0)
					{
						mainStr +=" ev.deptId.id= :deptId and ";
					}
					if(designationId.intValue() != 0)
					{
						mainStr += " ev.desigId.designationId = :designationId and ";
					}
					if(functionaryId.intValue() !=0)
					{
						mainStr += " ev.functionary.id = :functionaryId and ";
					}

					if(name!= null && !name.equals(""))
					{
						mainStr += " trim(upper(ev.employeeName))  like '%"+name.trim().toUpperCase()+"%' and ";
					}
					if(status.intValue() != 0 )
					{
						mainStr += " ev.employeeStatus.id = :employeeStatus and ";
					}
					/*
					if(code!=null&&!code.equals(""))
					{
						mainStr +=" where upper(trim(ev.employeeCode)) = :employeeCode ";
					}
					else
					{
					*/

					if( status.intValue() != 0 && designationId.intValue() == 0)
					{
						mainStr +=" ((ev.toDate is null and ev.fromDate <= SYSDATE ) OR (ev.fromDate <= SYSDATE AND ev.toDate > SYSDATE)) and ev.employeeStatus.id = :employeeStatus ";
					}
					else if(status.intValue() == 0 && designationId.intValue() != 0)
					{
						mainStr +=" ((ev.toDate is null and ev.fromDate <= SYSDATE ) OR (ev.fromDate <= SYSDATE AND ev.toDate > SYSDATE)) ";
					}
						//Inspite of SearchAll is true or false, if employee code is entered, search for all active and inactive employees
						else if(code!=null && !code.equals(""))
						{
							mainStr +="  ((ev.toDate IS NULL AND ev.fromDate <= SYSDATE) OR (ev.fromDate <= SYSDATE AND ev.toDate > SYSDATE) " +
									" OR (ev.fromDate IN (SELECT MAX (evn.fromDate)  FROM EmployeeView  evn   " +
									" WHERE evn.id = ev.id AND NOT EXISTS  (SELECT evn2.id FROM EmployeeView evn2 WHERE evn2.id = ev.id AND " +
									" ((evn2.toDate  IS NULL AND evn2.fromDate <= SYSDATE) OR (evn2.fromDate <= SYSDATE AND evn2.toDate > SYSDATE)) )))) ";
						}
						else if(status.intValue() != 0 ||designationId.intValue() == 0)
						{
							mainStr +="  ((ev.toDate IS NULL AND ev.fromDate <= SYSDATE) OR (ev.fromDate <= SYSDATE AND ev.toDate > SYSDATE) " +
									" OR (ev.fromDate IN (SELECT MAX (evn.fromDate)  FROM EmployeeView  evn   " +
									" WHERE evn.id = ev.id AND NOT EXISTS  (SELECT evn2.id FROM EmployeeView evn2 WHERE evn2.id = ev.id AND " +
									" ((evn2.toDate  IS NULL AND evn2.fromDate <= SYSDATE) OR (evn2.fromDate <= SYSDATE AND evn2.toDate > SYSDATE)) )))) ";
						}
						else
						{
							mainStr +=" ((ev.toDate is null and ev.fromDate <= SYSDATE ) OR (ev.fromDate <= SYSDATE AND ev.toDate > SYSDATE)) ";
						}
					//}


					Query qry = null;
						qry = getCurrentSession().createQuery(mainStr);
					LOGGER.info("qryqryqryqry"+qry.toString());
					if(code!=null&&!code.equals(""))
					{
						qry.setString("employeeCode", code.trim().toUpperCase());
					}
					if(departmentId.intValue() != 0)
					{
						qry.setInteger("deptId", departmentId);
					}
					if(designationId.intValue() != 0)
					{
						qry.setInteger("designationId", designationId);
					}
					if(functionaryId.intValue() != 0)
					{
						qry.setInteger("functionaryId", functionaryId);
					}
					if(status.intValue() != 0)
					{
						qry.setInteger("employeeStatus", status);
					}
					employeeList = (List)qry.list();


	} catch (HibernateException he) {
		LOGGER.error(he);
		//HibernateUtil.rollbackTransaction();
		throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
	} catch (Exception he) {
		LOGGER.error(he);

		//HibernateUtil.rollbackTransaction();
		throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);

	}
		return employeeList;
	}
	/**
	 *
	 * @param designationId
	 * @param code
	 * @param name
	 * @param status
	 * @param empType
	 * @param finParams department,functionary,function optional
	 * @return
	 * @throws Exception
	 */
	public  List<EmployeeView> searchEmployee(Integer designationId,String code,String name,Integer status,Integer empType,Map<String,Integer> finParams)throws Exception
	{

		//session = HibernateUtil.getCurrentSession();
		List<EmployeeView> employeeList = new ArrayList<EmployeeView>();
		Integer departmentId=finParams.get("departmentId")==null?0:finParams.get("departmentId");
		Integer functionaryId=finParams.get("functionaryId")==null?0:finParams.get("functionaryId");
		Integer functionId=finParams.get("functionId")==null?0:finParams.get("functionId");
		Integer userId = finParams.get("userId")==null?0:finParams.get("userId");
		Integer isActive = finParams.get("isActive")==null?0:finParams.get("isActive");
	try
	{
				Query qry = null;
				if(code!=null&&!code.equals(""))
				{
					logger.info(" Search by Code "+ code);
					eisService = new EisUtilService();
					persistenceService = new PersistenceService();
					eisService.setPersistenceService(persistenceService);
					persistenceService.setSessionFactory(new SessionFactory());
					persistenceService.setType(EmployeeServiceImpl.class);
					List<EmployeeView> list=eisService.getPersistenceService().findAllBy(" from EmployeeView ev where upper(ev.employeeCode) like ? ", code);
					Iterator itr = list.iterator();
					EmployeeView ev = null;
					EmployeeView ev1 = null;
					Assignment ass = null;
					Date date = new Date();
					PersonalInformation emp = null;
					while(itr.hasNext())
					{
						ev = (EmployeeView)itr.next();
						emp = getEmloyeeById(ev.getId());
						break;
					}
					if(emp != null)
						ass = emp.getAssignment(date); //Returns current/latest primary/temp assignment as on current date
					Iterator itr1= list.iterator();
					while(itr1.hasNext())
					{
						ev1 = (EmployeeView)itr1.next();
						
						if(ass!=null && ev1.getAssignment().equals(ass))
						{
							logger.info("ev.assId="+ev1.getAssignment().getId());
							logger.info("ass.assId="+ass.getId());
							logger.info("emp " +ev1.getEmployeeName() + " : "+ev1.getPosition().getName()+ " : "+ev1.getIsPrimary());
							break;
						}
					}
					if(ev1 != null)
						employeeList.add(ev1);
				}
				else
				{
					logger.info(" Search by other params ");
					String mainStr = "from EmployeeView ev where " ;
					if(departmentId.intValue() != 0)
					{
						mainStr +=" ev.deptId.id= :deptId and ";
					}
					if(designationId.intValue() != 0)
					{
						mainStr += " ev.desigId.designationId = :designationId and ";
					}
					if(functionaryId.intValue() !=0)
					{
						mainStr += " ev.functionary.id = :functionaryId and ";
					}
					if(functionId!=null && functionId.intValue() !=0)
					{
						mainStr += " ev.functionId.id = :functionId and ";
					}

					if(name!= null && !name.equals(""))
					{
						mainStr += " trim(upper(ev.employeeName))  like '%"+name.trim().toUpperCase()+"%' and ";
					}
					if(status.intValue() != 0 )
					{
						mainStr += " ev.employeeStatus.id = :employeeStatus and ";
					}
					if(empType.intValue()!=0)
					{
						mainStr += " ev.employeeType.id=:employeeType and ";
					}
					if(isActive.intValue()!=0)
					{
					mainStr += " ev.userMaster.isActive=:isActive  and ";
					}
					if(userId.intValue()!=0)// to query the employee using employee username
					{
					mainStr +=" ev.userMaster.id= :userId and ";
					}
					mainStr +=" upper(ev.isPrimary)='Y' AND ((  ev.toDate IS NULL AND ev.fromDate <= TO_DATE(SYSDATE,'dd-MM-yyy')) OR ( ev.fromDate <= TO_DATE(SYSDATE,'dd-MM-yyy') AND ev.toDate >= TO_DATE(SYSDATE,'dd-MM-yyy')) " +
					" OR (ev.fromDate IN (SELECT MAX (evn.fromDate)  FROM EmployeeView  evn   " +
					" WHERE evn.id = ev.id  AND NOT EXISTS  (SELECT evn2.id FROM EmployeeView evn2 WHERE evn2.id = ev.id AND " +
					" ((evn2.toDate  IS NULL AND evn2.fromDate <= TO_DATE(SYSDATE,'dd-MM-yyy')) OR (evn2.fromDate <= TO_DATE(SYSDATE,'dd-MM-yyy') AND evn2.toDate >= TO_DATE(SYSDATE,'dd-MM-yyy'))) )))) ";


					qry = getCurrentSession().createQuery(mainStr);
					if(departmentId.intValue() != 0)
					{
						qry.setInteger("deptId", departmentId);
					}
					if(designationId.intValue() != 0)
					{
						qry.setInteger("designationId", designationId);
					}
					if(functionaryId.intValue() != 0)
					{
						qry.setInteger("functionaryId", functionaryId);
					}
					if(status.intValue() != 0)
					{
						qry.setInteger("employeeStatus", status);
					}
					if(empType.intValue() != 0)
					{
						qry.setInteger("employeeType",empType);
					}
					if(isActive.intValue()!=0)
					{
						qry.setInteger("isActive",isActive);
					}
					if(userId.intValue()!=0)
					{
						qry.setInteger("userId",userId);
					}
					employeeList = (List)qry.list();
			}




} catch (HibernateException he) {
	LOGGER.error(he);
	//HibernateUtil.rollbackTransaction();
	throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
} catch (Exception he) {
	LOGGER.error(he);

	//HibernateUtil.rollbackTransaction();
	throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);

}
	return employeeList;
}
	
	public  List searchEmployeeForNominees(Integer departmentId,Integer designationId,Integer functionaryId,String code,String name,Integer status,Integer empType)throws Exception
	{
		
		//session = HibernateUtil.getCurrentSession();
		List<EmployeeView> employeeList = null;
	try
	{
				
				String mainStr = "select distinct ev from EmployeeView ev,EmployeeNomineeMaster enm where ev.id=enm.employeeId.id and " ;
				if(code!=null&&!code.equals(""))
				{
					mainStr +=" upper(trim(ev.employeeCode)) = :employeeCode and ";
					name="";
				}
				if(departmentId.intValue() != 0)
				{
					mainStr +=" ev.deptId.id= :deptId and ";
				}
				if(designationId.intValue() != 0)
				{
					mainStr += " ev.desigId.designationId = :designationId and ";
				}
				if(functionaryId.intValue() !=0)
				{
					mainStr += " ev.functionary.id = :functionaryId and ";
				}
				
				if(name!= null && !name.equals(""))
				{
					mainStr += " trim(upper(ev.employeeName))  like :empName and ";
				}
				if(status.intValue() != 0 )
				{
					mainStr += " ev.employeeStatus.id = :employeeStatus and ";
				}
				if(empType.intValue()!=0)
				{
					mainStr += " ev.employeeType.id=:employeeType and ";
				}
				
				mainStr +="  ((ev.toDate IS NULL AND ev.fromDate <= TO_DATE(SYSDATE,'dd-MM-yyy')) OR (upper(ev.isPrimary)='Y' and ev.fromDate <= TO_DATE(SYSDATE,'dd-MM-yyy') AND ev.toDate >= TO_DATE(SYSDATE,'dd-MM-yyy')) " +
				" OR (ev.fromDate IN (SELECT MAX (evn.fromDate)  FROM EmployeeView  evn   " +
				" WHERE evn.id = ev.id AND upper(ev.isPrimary)='Y' AND NOT EXISTS  (SELECT evn2.id FROM EmployeeView evn2 WHERE evn2.id = ev.id AND " +
				" ((evn2.toDate  IS NULL AND evn2.fromDate <= TO_DATE(SYSDATE,'dd-MM-yyy')) OR (evn2.fromDate <= TO_DATE(SYSDATE,'dd-MM-yyy') AND evn2.toDate >= TO_DATE(SYSDATE,'dd-MM-yyy'))) )))" +
				" ) ";
				
					
				
				
				
				Query qry = null;
					qry = getCurrentSession().createQuery(mainStr);
				LOGGER.info("qryqryqryqry"+qry.toString());
				if(code!=null&&!code.equals(""))
				{
					qry.setString("employeeCode", code.trim().toUpperCase());
				}
				if(name!= null && !name.equals(""))
				{
					qry.setString("empName","%"+name.trim().toUpperCase()+"%");
				}
				if(departmentId.intValue() != 0)
				{
					qry.setInteger("deptId", departmentId);
				}
				if(designationId.intValue() != 0)
				{
					qry.setInteger("designationId", designationId);
				}
				if(functionaryId.intValue() != 0)
				{
					qry.setInteger("functionaryId", functionaryId);
				}
				if(status.intValue() != 0)
				{
					qry.setInteger("employeeStatus", status);
				}
				if(empType.intValue() != 0)
				{
					qry.setInteger("employeeType",empType);
				}
				employeeList = (List)qry.list();
				

} catch (HibernateException he) {
	LOGGER.error(he);
	//HibernateUtil.rollbackTransaction();
	throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
} catch (Exception he) {
	LOGGER.error(he);
	   
	//HibernateUtil.rollbackTransaction();
	throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);

}
	return employeeList;
}
	
	@Deprecated
	/**
	 *
	 * Api to get the employee with primary assignment for currentdate.
	 * If there is no Assignment for the current date then it pick's up the employee with  lastest assignment ie: Maximum( From Date)
	 * @param departmentId
	 * @param designationId
	 * @param functionaryId
	 * @param code
	 * @param name
	 * @param status
	 * @param empType
	 * @return List of EmployeeView for current or Lastest Assignment.
	 * @throws Exception
	 */
	public  List searchEmployee(Integer departmentId,Integer designationId,Integer functionaryId,String code,String name,Integer status,Integer empType)throws Exception
	{

		//session = HibernateUtil.getCurrentSession();
		List<EmployeeView> employeeList = null;
	try
	{

				String mainStr = "from EmployeeView ev where " ;
				if(code!=null&&!code.equals(""))
				{
					mainStr +=" upper(trim(ev.employeeCode)) = :employeeCode and ";
					name="";
				}
				if(departmentId.intValue() != 0)
				{
					mainStr +=" ev.deptId.id= :deptId and ";
				}
				if(designationId.intValue() != 0)
				{
					mainStr += " ev.desigId.designationId = :designationId and ";
				}
				if(functionaryId.intValue() !=0)
				{
					mainStr += " ev.functionary.id = :functionaryId and ";
				}

				if(name!= null && !name.equals(""))
				{
					mainStr += " trim(upper(ev.employeeName))  like :empName and ";
				}
				if(status.intValue() != 0 )
				{
					mainStr += " ev.employeeStatus.id = :employeeStatus and ";
				}
				if(empType.intValue()!=0)
				{
					mainStr += " ev.employeeType.id=:employeeType and ";
				}

				mainStr +="  ((ev.toDate IS NULL AND ev.fromDate <= TO_DATE(SYSDATE,'dd-MM-yyy')) OR (upper(ev.isPrimary)='Y' and ev.fromDate <= TO_DATE(SYSDATE,'dd-MM-yyy') AND ev.toDate >= TO_DATE(SYSDATE,'dd-MM-yyy')) " +
				" OR (ev.fromDate IN (SELECT MAX (evn.fromDate)  FROM EmployeeView  evn   " +
				" WHERE evn.id = ev.id AND upper(ev.isPrimary)='Y' AND NOT EXISTS  (SELECT evn2.id FROM EmployeeView evn2 WHERE evn2.id = ev.id AND " +
				" ((evn2.toDate  IS NULL AND evn2.fromDate <= TO_DATE(SYSDATE,'dd-MM-yyy')) OR (evn2.fromDate <= TO_DATE(SYSDATE,'dd-MM-yyy') AND evn2.toDate >= TO_DATE(SYSDATE,'dd-MM-yyy'))) )))) ";





				Query qry = null;
					qry = getCurrentSession().createQuery(mainStr);
				LOGGER.info("qryqryqryqry"+qry.toString());
				if(code!=null&&!code.equals(""))
				{
					qry.setString("employeeCode", code.trim().toUpperCase());
				}
				if(departmentId.intValue() != 0)
				{
					qry.setInteger("deptId", departmentId);
				}
				if(name!= null && !name.equals(""))
				{
					qry.setString("empName","%"+name.trim().toUpperCase()+"%");
				}
				if(designationId.intValue() != 0)
				{
					qry.setInteger("designationId", designationId);
				}
				if(functionaryId.intValue() != 0)
				{
					qry.setInteger("functionaryId", functionaryId);
				}
				if(status.intValue() != 0)
				{
					qry.setInteger("employeeStatus", status);
				}
				if(empType.intValue() != 0)
				{
					qry.setInteger("employeeType",empType);
				}
				employeeList = (List)qry.list();


} catch (HibernateException he) {
	LOGGER.error(he);
	//HibernateUtil.rollbackTransaction();
	throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
} catch (Exception he) {
	LOGGER.error(he);

	//HibernateUtil.rollbackTransaction();
	throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);

}
	return employeeList;
}
	public List<EmployeeView> searchEmployeeByGrouping(LinkedList<String> groupingByOrder)throws Exception
	{
		//session = HibernateUtil.getCurrentSession();
		List<EmployeeView> employeeList =null;
		try
		{
			String mainStr = "from EmployeeView ev where " ;

			String orderByStr = "";
			for(int i=0;i<groupingByOrder.size();i++)
			{
				if(groupingByOrder.get(i).toString().equals("FundCode"))
				{
					if(!orderByStr.equals(""))
					{
						orderByStr =orderByStr +", ";
					}
					orderByStr = orderByStr +" ev.assignment.fundId.code";
				}
				else if(groupingByOrder.get(i).toString().equals("FunctionCode"))
				{
					if(!orderByStr.equals(""))
					{
						orderByStr =orderByStr +", ";
					}
					orderByStr = orderByStr +" ev.assignment.functionId.code";
				}
				else if(groupingByOrder.get(i).toString().equals("DeptCode"))
				{
					if(!orderByStr.equals(""))
					{
						orderByStr =orderByStr +", ";
					}
					orderByStr = orderByStr +" ev.assignment.deptId.deptCode";
				}
			}

			if(!orderByStr.equals(""))
			{
				orderByStr = " order by " +orderByStr;
			}
			mainStr +=" ((ev.toDate is null and ev.fromDate <= SYSDATE ) OR (ev.fromDate <= SYSDATE AND ev.toDate > SYSDATE)) and ev.assignment.isPrimary='Y' ";

			mainStr = mainStr + orderByStr;

			Query qry = null;
			qry = getCurrentSession().createQuery(mainStr);

			LOGGER.info("Query in search Employee by grouping=="+qry.toString());

			employeeList = (List)qry.list();

		} catch (HibernateException he) {
			LOGGER.error("Exception ==="+he.getMessage());
			throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
		} catch (Exception e) {
			LOGGER.error("Exception ==="+ e.getMessage());
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);

		}
		return employeeList;
	}

	public List searchEmployee(Integer empId)throws Exception
	{
		//session = HibernateUtil.getCurrentSession();
		ArrayList<SearchEmpDTO> dataElCol = new ArrayList<SearchEmpDTO>();
		List employeeList = null;

		try
		{

					String mainStr = "select ev.employeeCode,ev.employeeName,ev.id,ev.desigId.designationId, ev.deptId.id ,ev.fromDate,ev.toDate from EmployeeView ev where ev.id = :empId";
					Query qry = getCurrentSession().createQuery(mainStr);

					if(empId.intValue() != 0)
					{
						qry.setInteger("empId", empId);

					}
					employeeList = qry.list();


					for(Iterator iter = employeeList.iterator();iter.hasNext();)
					{
							Object [] objArray = (Object [])iter.next();
							addEmployee(objArray,dataElCol);
					}



	} catch (HibernateException he) {
		LOGGER.error(he);
		   //HibernateUtil.rollbackTransaction();
		throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
	} catch (Exception he) {
		LOGGER.error(he);

		//HibernateUtil.rollbackTransaction();
		throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);

	}

				return dataElCol;
	}
	private void addEmployee(Object [] objArray,ArrayList<SearchEmpDTO> dataElColt )
	{
		try {
			int len = objArray.length;

			Integer id = Integer.valueOf(0);
			String empCode="";
			String fName="";
			Integer desid = null;
			Integer deptId = null;
			java.sql.Date fromDate = null;
			java.sql.Date toDate = null;

			for(int i=0;i<len;i++)
			{

				if(i==0)
				{
					empCode = (String)objArray[i];

				}
				else if(i==1)
				{
					fName = (String)objArray[i];

				}
				else if(i==2)
				{
					id = (Integer)objArray[i];

				}
				else if(i==3)
				{
					desid = (Integer)objArray[i];

				}
				else if(i==4)
				{
					deptId = (Integer)objArray[i];

				}
				else if(i==5)
				{
					fromDate =  (java.sql.Date)objArray[i];

				}
				else if(i==6)
				{
					toDate = (java.sql.Date)objArray[i];

				}
				if (i==6)
				{
					dataElColt.add(new SearchEmpDTO(desid,deptId,empCode,fName,id,fromDate,toDate));
				}
			}
		} catch (Exception e) {

			LOGGER.error(e);
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}
	}
	public Assignment getAssignmentByEmpAndDate(Date date,Integer empId)
	{
		//session = HibernateUtil.getCurrentSession();
		Assignment assignment = null;
		try
		{

			if(empId != null)
			{
				String	mainStr = " select 	ev.assignment  from EmployeeView ev  where ev.assignment.isPrimary = 'Y' and ev.id = :empId and ((ev.toDate is null and ev.fromDate <= :date1 ) OR (ev.fromDate <= :date2 AND ev.toDate >= :date3 ))";
				Query qry = getCurrentSession().createQuery(mainStr);
				qry.setInteger("empId", empId);
				qry.setDate("date1", new java.sql.Date(date.getTime()));
				qry.setDate("date2", new java.sql.Date(date.getTime()));
				qry.setDate("date3", new java.sql.Date(date.getTime()));
				
				if(qry.list()!=null && !qry.list().isEmpty())
				{					
						assignment = (Assignment)qry.list().get(0);
				}
			}
			
		}
		catch (HibernateException he)
		{
				LOGGER.error(he);
				//HibernateUtil.rollbackTransaction();
				throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
		} catch (Exception he)
		{
				LOGGER.error(he);
				//HibernateUtil.rollbackTransaction();
				throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
		}
			return assignment;
	}
	public Assignment getLatestAssignmentForEmployee(Integer empId)
	{
		//changing sysdate to current date of java
		Date currDate = new Date();
		session = HibernateUtil.getCurrentSession();
		Assignment assignment = null;
		try
		{
			String mainStr = "";
			mainStr = " select ev.assignment from EmployeeView ev where ev.assignment.isPrimary = 'Y' and ev.id = :empId and ((ev.toDate is null and ev.fromDate <= :sysDate ) OR (ev.fromDate <= :sysDate AND ev.toDate >= :sysDate))";
			Query qry = session.createQuery(mainStr);

			if(empId != null)
			{
				qry.setInteger("empId", empId);
				qry.setDate("sysDate", new java.sql.Date(currDate.getTime()));
			}
			if(qry.list()!=null&&!qry.list().isEmpty())
			{
				for(Iterator iter = qry.list().iterator();iter.hasNext();)
				{
					assignment = (Assignment)iter.next();
				}
			}
		}
		catch (HibernateException he) {
				LOGGER.error(he);
				//HibernateUtil.rollbackTransaction();
				throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
			} catch (Exception he)
			{
				LOGGER.error(he);
				//HibernateUtil.rollbackTransaction();
				throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
			}
			return assignment;
	}

	public  List getListByAppNoAndMeMoNo(String applicationNumber ,String chargeMemoNo,Integer empId)
	{
		List<DisciplinaryPunishment> list = new ArrayList<DisciplinaryPunishment>();
		//session = HibernateUtil.getCurrentSession();

		try
		{
			String mainStr = "";
			mainStr = "select dp.disciplinaryPunishmentId  from DisciplinaryPunishment dp,PersonalInformation pi  where  dp.employeeId = pi.idPersonalInformation and dp.employeeId = :empId ";
			if(chargeMemoNo!=null&&!chargeMemoNo.equals(""))
				mainStr +=" and upper(trim(dp.chargeMemoNo)) = :chargeMemoNo ";
			if(applicationNumber!=null&&!applicationNumber.equals(""))
				mainStr +=" and upper(trim(dp.applicationNumber)) = :applicationNumber ";
			Query qry = getCurrentSession().createQuery(mainStr);
			if(empId != null)
			{
				qry.setInteger("empId", empId);
			}
			if(chargeMemoNo!=null&&!chargeMemoNo.equals(""))
			{
				qry.setString("chargeMemoNo", chargeMemoNo);
			}
			if(applicationNumber!=null&&!applicationNumber.equals(""))
			{
				qry.setString("applicationNumber", applicationNumber);
			}
			if(qry.list()!=null&&!qry.list().isEmpty())
			{
				Integer desigId = null;
				for(Iterator iter = qry.list().iterator();iter.hasNext();)
				{
					desigId = (Integer)iter.next();
					list.add(getDisciplinaryPunishmentById(desigId));
				}
			}
		}
		catch (HibernateException he)
		{
				LOGGER.error(he);
				//HibernateUtil.rollbackTransaction();
				throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
		} catch (Exception he)
		{
				LOGGER.error(he);
				//HibernateUtil.rollbackTransaction();
				throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
		}
	return 	list;
	}


	public AssignmentPrd getAssignmentPrdByEmpAndDate(Date date,Integer empId)
	{
		//session = HibernateUtil.getCurrentSession();
		AssignmentPrd assignmentPrd = new AssignmentPrd();
		try
		{
			String mainStr = "";
			mainStr = " select 	ev.assignmentPrd  from EmployeeView ev  where ev.id = :empId and ((ev.toDate is null and ev.fromDate <= :date1 ) OR (ev.fromDate <= :date2 AND ev.toDate > :date3 ))";
			Query qry = getCurrentSession().createQuery(mainStr);

			if(empId != null)
			{
				qry.setInteger("empId", empId);
				qry.setDate("date1", new java.sql.Date(date.getTime()));
				qry.setDate("date2", new java.sql.Date(date.getTime()));
				qry.setDate("date3", new java.sql.Date(date.getTime()));
			}
			List retList = qry.list();
			if(retList!=null&&!retList.isEmpty())
			{

				for(Iterator iter = retList.iterator();iter.hasNext();)
				{
					assignmentPrd = (AssignmentPrd)iter.next();

				}

			}
		}
		catch (HibernateException he) {
				LOGGER.error(he);
				//HibernateUtil.rollbackTransaction();
				throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
			} catch (Exception he) {
				LOGGER.error(he);

				//HibernateUtil.rollbackTransaction();
				throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);

	}

			return assignmentPrd;

	}

	public AssignmentPrd getAssPrdIdForEmployee(Integer empId)
	{

		//session = HibernateUtil.getCurrentSession();
		AssignmentPrd assignmentprd = new AssignmentPrd();
		try
		{
			String mainStr = "";
			mainStr = " select 	ev.assignmentPrd  from EmployeeView ev  where ev.id = :empId and ((ev.toDate is null and ev.fromDate <= SYSDATE ) OR (ev.fromDate <= SYSDATE AND ev.toDate > SYSDATE))";
			Query qry = getCurrentSession().createQuery(mainStr);

			if(empId != null)
			{
				qry.setInteger("empId", empId);

			}
			if(qry.list()!=null&&!qry.list().isEmpty())
			{

				for(Iterator<AssignmentPrd> iter = qry.list().iterator();iter.hasNext();)
				{
					assignmentprd = (AssignmentPrd)iter.next();

				}

			}
		}
		catch (HibernateException he) {
				LOGGER.error(he);
				//HibernateUtil.rollbackTransaction();
				throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
			} catch (Exception he) {
				LOGGER.error(he);
				   //HibernateUtil.rollbackTransaction();

				throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);

	}

			return assignmentprd;
	}

	public  List<SearchEmpDTO> getHistoryOfEmpForCurrentFinY(Integer empId,java.util.Date givenDate)
	{
		List<SearchEmpDTO> list = new ArrayList<SearchEmpDTO>();
		CFinancialYear financialYear=null;
		CalendarYear calYear=null;
		SimpleDateFormat smt = new SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault());
		String finId = null;
		java.util.Date stFyDate=null;
		Date myGivenDate = givenDate;
		//String finId = EisManagersUtill.getCommonsManager().getCurrYearFiscalId();
		//session = HibernateUtil.getCurrentSession();
		try
		{
			if(EisManagersUtill.getEmpLeaveService().isLeaveCalendarBased())
			{
				if(myGivenDate==null)
				{
					myGivenDate = new Date();
					finId =smt.format(myGivenDate);
				}
				else
				{
					finId =smt.format(myGivenDate);
				}
				calYear=EisManagersUtill.getEmpLeaveService().getCalendarYearById(Long.valueOf(finId));
				stFyDate = calYear.getStartingDate();
			}
			else
			{
				//fix for leave report encahshed leave type where from date will be null
				if(myGivenDate==null)
				{
					myGivenDate = new Date();
					finId = smt.format(myGivenDate);
				}
				else
				{
				finId = smt.format(myGivenDate);

				}
				financialYear = EisManagersUtill.getCommonsService().findFinancialYearById(Long.valueOf(finId));
				stFyDate =  financialYear.getStartingDate();
			}


			String mainStr = "select ev.desigId.designationId, ev.fromDate,ev.toDate  from EmployeeView ev  where ev.id = :empId and  ( ev.toDate is null or ev.toDate >= :startingFy ) and  ev.fromDate <  :givenDate ";
			Query qry = getCurrentSession().createQuery(mainStr);
			if(empId != null)
			{
				qry.setInteger("empId", empId);
				qry.setDate("startingFy", new java.sql.Date(stFyDate.getTime()));
				qry.setDate("givenDate", myGivenDate);
			}
			List listOfHistory = qry.list();
			if(qry.list()!=null&&!qry.list().isEmpty())
			{
				for(Iterator iter = listOfHistory.iterator();iter.hasNext();)
				{
						Object [] objArray = (Object [])iter.next();
						addListEmployeeHistory(empId,objArray,list,myGivenDate,stFyDate);
				}
			}
		}
		catch (HibernateException he)
		{
			LOGGER.error(he);
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
		} catch (Exception he)
		{
			LOGGER.error(he);
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);

		}
		return list;
	}


	public  List getHistoryOfEmpForGivenFinY(Integer empId,java.util.Date givenDate,CFinancialYear financialYear)
	{
		List<SearchEmpDTO> list = new ArrayList<SearchEmpDTO>();
		//String finId = EisManagersUtill.getCommonsManager().getCurrYearFiscalId();
		//session = HibernateUtil.getCurrentSession();
		try
		{
		//	CFinancialYear financialYear = EisManagersUtill.getCommonsManager().findFinancialYearById(new Long(finId));
			java.util.Date stFyDate =  financialYear.getStartingDate();
			String mainStr = "select ev.desigId.designationId, ev.fromDate,ev.toDate  from EmployeeView ev  where ev.id = :empId and  ( ev.toDate is null or ev.toDate >= :startingFy ) and  ev.fromDate <  :givenDate ";
			Query qry = getCurrentSession().createQuery(mainStr);
			if(empId != null)
			{
				qry.setInteger("empId", empId);
				qry.setDate("startingFy", new java.sql.Date(stFyDate.getTime()));
				qry.setDate("givenDate", givenDate);
			}
			List listOfHistory = qry.list();
			if(qry.list()!=null&&!qry.list().isEmpty())
			{
				for(Iterator iter = listOfHistory.iterator();iter.hasNext();)
				{
						Object [] objArray = (Object [])iter.next();
						addListEmployeeHistory(empId,objArray,list,givenDate,stFyDate);
				}
			}
		}
		catch (HibernateException he)
		{
			LOGGER.error(he);
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
		} catch (Exception he)
		{
			LOGGER.error(he);
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);

		}
		return list;
	}

	private void addListEmployeeHistory(Integer empId,Object [] objArray,List<SearchEmpDTO> l,java.util.Date givenDate,java.util.Date stFyDate)
	{
		try {
			int len = objArray.length;
			java.sql.Date todesDate =null;
			java.sql.Date fromdesDate =null;
			Integer desig =null;
			for(int i=0;i<len;i++)
			{
				if(i==1)
				{
					fromdesDate =(java.sql.Date)objArray[i];
				}
				else if(i==0)
				{
					desig =(Integer)objArray[i];
				}
				else if(i==2)
				{
					todesDate =(java.sql.Date)objArray[i];
					if(todesDate==null)
					{
						todesDate = new java.sql.Date(givenDate.getTime());
					}
					else
					{
						if(todesDate.getTime()>givenDate.getTime())
						{
							todesDate = new java.sql.Date(givenDate.getTime());
						}
					}
					if(fromdesDate.getTime() < stFyDate.getTime())
					{
						fromdesDate =  new java.sql.Date(stFyDate.getTime());
					}
					l.add(new SearchEmpDTO(desig,Integer.valueOf(0),"","",Integer.valueOf(empId),fromdesDate,todesDate));
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error(e);
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}
	}

	public  boolean checkDisciplinaryNo(String disciplinaryNo)
	{
		//session = HibernateUtil.getCurrentSession();
		boolean dispExist = false;

			Query qry = getCurrentSession().createQuery("select dp.id from  DisciplinaryPunishment dp where upper(dp.applicationNumber) = :disciplinaryNo ");
			if(disciplinaryNo != null )
			{
				qry.setString("disciplinaryNo", disciplinaryNo);

			}
			if(qry.list()!=null&&!qry.list().isEmpty())
			{
				Object obj = null;
				for(Iterator iter = qry.list().iterator();iter.hasNext();)
				{
					obj = (Object)iter.next();
					dispExist = true;
				}

			}


		return dispExist;
	}

	public PersonalInformation getEmployeeforPosition(Position pos)
	{
		User uerImpl= null;
		//session = HibernateUtil.getCurrentSession();
		PersonalInformation personalInformation = new PersonalInformation();
		try
		{
			
						String mainStr = "";
			mainStr = " select 	id  from EG_EIS_EMPLOYEEINFO ev  where ev.POS_ID = :pos and ((ev.to_Date is null and ev.from_Date <= SYSDATE ) OR (ev.from_Date <= SYSDATE AND ev.to_Date > SYSDATE))";
			Query qry = getCurrentSession().createSQLQuery(mainStr).addScalar("id", IntegerType.INSTANCE);;

			if(pos != null)
			{
				qry.setEntity("pos", pos);
			}
			if(qry.list()!=null&&!qry.list().isEmpty())
			{
				for(Iterator iter = qry.list().iterator();iter.hasNext();)
				{
					Integer id = (Integer)iter.next();
					personalInformation = EisManagersUtill.getEmployeeService().getEmloyeeById(id);
				}
			}
		}
		catch (HibernateException he) {
				LOGGER.error(he);
				//HibernateUtil.rollbackTransaction();
				throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
			} catch (Exception he)
			{
				LOGGER.error(he);
				//HibernateUtil.rollbackTransaction();
				throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
			}
			return personalInformation;

	}
	public Position getPositionforEmp(Integer empId)
	{
		Position position= null;
		//session = HibernateUtil.getCurrentSession();
		List list = null;
		try
		{
			
						String mainStr = "";
			mainStr = " select 	POS_ID  from EG_EIS_EMPLOYEEINFO ev  where ev.ID = :empId and ((ev.to_Date is null and ev.from_Date <= SYSDATE ) OR (ev.from_Date <= SYSDATE AND ev.to_Date >= SYSDATE))";
			Query qry = getCurrentSession().createSQLQuery(mainStr).addScalar("POS_ID", IntegerType.INSTANCE);

			if(empId != null)
			{
				qry.setInteger("empId", empId);
			}
			list = qry.list();
			if(list!=null && !list.isEmpty())
			{
				for(Iterator iter = qry.list().iterator();iter.hasNext();)
				{
					Integer id = (Integer)iter.next();
					if(id != null)
					 position = EisManagersUtill.getEisCommonsService().getPositionById(id);
				}
			}
		}
		catch (HibernateException he) {
				LOGGER.error(he);
				throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
			} catch (Exception he)
			{
				LOGGER.error(he);
				throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
			}
			return position;

	}

	public  boolean checkSanctionNoForDisciplinary(String sanctionNo)
	{

		//session = HibernateUtil.getCurrentSession();
		boolean b = false;

			Query qry = getCurrentSession().createQuery("select dp.id from  DisciplinaryPunishmentApproval dp where upper(dp.sanctionNo) = :sanctionNo ");
			if(sanctionNo != null )
			{
				qry.setString("sanctionNo", sanctionNo);

			}
			if(qry.list()!=null&&!qry.list().isEmpty())
			{
				Object obj = null;
				for(Iterator iter = qry.list().iterator();iter.hasNext();)
				{
					obj = (Object)iter.next();
					b = true;
				}

			}

		return b;
}

	public Integer getNextVal(String seqName)
	{

		//session = HibernateUtil.getCurrentSession();
		Integer id = Integer.valueOf(0);
		try
		{
			Query qry = getCurrentSession().createSQLQuery("SELECT  "+seqName+".nextval as id from dual").addScalar("id", IntegerType.INSTANCE);
			if(qry.list()!=null&&!qry.list().isEmpty())
			{
				Integer obj = null;
				for(Iterator iter = qry.list().iterator();iter.hasNext();)
				{
					obj = (Integer)iter.next();
					id = obj;
				}

			}

		}
		catch (HibernateException he) {
				LOGGER.error(he);
				throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
			} catch (Exception he) {
				LOGGER.error(he);
				   //HibernateUtil.rollbackTransaction();

				throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);

	}
		return id;
}
	public boolean checkDuplication(String name,String className)
	{

		//session = HibernateUtil.getCurrentSession();
		boolean b = false;
		try
		{
			Query qry = getCurrentSession().createQuery("from "+className+" CA where trim(upper(CA.name)) = :name ");
			qry.setString("name", name);
			Iterator iter = qry.iterate();

			if(iter.hasNext())
			{

				b = true;
			}

		}
		catch (HibernateException he) {
					LOGGER.error(he);
				   //HibernateUtil.rollbackTransaction();
				throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
			} catch (Exception he) {
					LOGGER.error(he);
				   //HibernateUtil.rollbackTransaction();

				throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);

	}
		return b;
	}


	List appliedDisiplinaryApplications = new ArrayList();
	List rejectedDisiplinaryApplications = new ArrayList();
	List approvedDisiplinaryApplications = new ArrayList();


	public  void createEmployeeDepartment(EmployeeDepartment employeeDepartment)
	{
		try {
			EmployeeDepartmentDAO employeeDepartmentDAO = EisDAOFactory.getDAOFactory().getEmployeeDepartmentDAO();
			employeeDepartmentDAO.create(employeeDepartment);
		} catch (Exception e) {
			LOGGER.error(e);
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}
	}
	public  void deleteEmpDepForAss(Assignment egEmpAssignment)
	{
		try {
			EmployeeDepartmentDAO employeeDepartmentDAO = EisDAOFactory.getDAOFactory().getEmployeeDepartmentDAO();
			employeeDepartmentDAO.deleteByAss(egEmpAssignment);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error(e);
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}
	}
	public  List getDisiplinaryApplicationsRejectedEmpID(Integer empId)
	{
		try {
			setApprovedDisiplinaryForEmpID(empId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error(e);
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}
		return rejectedDisiplinaryApplications;
	}
	public  List getDisiplinaryApplicationsApprovedEmpID(Integer empId)
	{
		try {
			setApprovedDisiplinaryForEmpID(empId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error(e);
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}
		return approvedDisiplinaryApplications;
	}
	public  List getDisiplinaryApplicationsAppliedEmpID(Integer empId)
	{
		try {
			setApprovedDisiplinaryForEmpID(empId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error(e);
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}
		return appliedDisiplinaryApplications;
	}
	/*public  String getFinancialYearId(String estDate)
	{
		FinancialYearDAO finYearDAO=CommonsDaoFactory.getDAOFactory().getFinancialYearDAO();
		return finYearDAO.getFinancialYearId(estDate);
	}*/


	public  void createPersonAddress(PersonAddress personAddress)
	{
		try {
			PersonAddressDAO personAddressDAO = EisDAOFactory.getDAOFactory().getPersonAddressDAO();
			personAddressDAO.create(personAddress);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error(e);
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}
	}
	public  PersonalInformation getEmpForUserId(Integer userId)
	{
		PersonalInformationDAO egpimsPersonalInformationDAO = EisDAOFactory.getDAOFactory().getPersonalInformationDAO();
		return egpimsPersonalInformationDAO.getPersonalInformationByUserId(userId);
	}
	public void  createProbation(Probation probation)
	{
		try {
			ProbationDAO probationDAO = EisDAOFactory.getDAOFactory().getProbationDAO();
			probationDAO.create(probation);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error(e);
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}
	}
	public void  createRegularisation(Regularisation regularisation)
	{
		try {
			RegularisationDAO regularisationDAO = EisDAOFactory.getDAOFactory().getRegularisationDAO();
			regularisationDAO.create(regularisation);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error(e);
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}
	}

	public  void createLangaugeKnown(LangKnown lanKnown)
	{

		try {
			LangKnownDAO langKnownDAO = EisDAOFactory.getDAOFactory().getLangKnownDAO();
			langKnownDAO.create(lanKnown);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error(e);
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}
	}
	public  void createEducationDetails(EduDetails eduDetails)
	{
		try {
			EduDetailsDAO eduDetailsDAO = EisDAOFactory.getDAOFactory().getEduDetailsDAO();
			eduDetailsDAO.create(eduDetails);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error(e);
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}
	}
	public  void createTecnicalQualification(TecnicalQualification tecnicalQualification)
	{
		try {
			TecnicalQualificationDAO tecnicalQualificationDAO = EisDAOFactory.getDAOFactory().getTecnicalQualificationDAO();
			tecnicalQualificationDAO.create(tecnicalQualification);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error(e);
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}
	}
	public  void createDeptTests(DeptTests deptTests)
	{
		try {
			DeptTestsDAO deptTestsDAO = EisDAOFactory.getDAOFactory().getDeptTestsDAO();
			deptTestsDAO.create(deptTests);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error(e);
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}
	}
	public  void createNomimationPirticulars(NomimationPirticulars nomimationPirticulars)
	{
		try {
			NomimationPirticularsDAO nomimationPirticularsDAO = EisDAOFactory.getDAOFactory().getNomimationPirticularsDAO();
			nomimationPirticularsDAO.create(nomimationPirticulars);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error(e);
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}

	}
	public  void createImmovablePropDetails(ImmovablePropDetails immovablePropDetails)
	{
		try {
			ImmovablePropDetailsDAO immovablePropDetailsDAO = EisDAOFactory.getDAOFactory().getImmovablePropDetailsDAO();
			immovablePropDetailsDAO.create(immovablePropDetails);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error(e);
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}
	}
	public  void createMovablePropDetails(MovablePropDetails movablePropDetails)
	{
		try {
			MovablePropDetailsDAO movablePropDetailsDAO = EisDAOFactory.getDAOFactory().getMovablePropDetailsDAO();
			movablePropDetailsDAO.create(movablePropDetails);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error(e);
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}
	}
	public  void createBankDet(BankDet egpimsBankDet)
	{
		try {
			BankDetDAO egpimsBankDetDAO = EisDAOFactory.getDAOFactory().getBankDetDAO();
			egpimsBankDetDAO.create(egpimsBankDet);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error(e);
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}
	}
	public  void createAssignmentPrd(AssignmentPrd egEmpAssignmentPrd)
	{
		try {
			AssignmentPrdDAO egpimsAssignmentPrdDAO = EisDAOFactory.getDAOFactory().getAssignmentPrdDAO();
			egpimsAssignmentPrdDAO.create(egEmpAssignmentPrd);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error(e);
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}
	}
	public  void createAssignment(Assignment egEmpAssignment)
	{
		try {
			AssignmentDAO egpimsAssignmentDAO = EisDAOFactory.getDAOFactory().getAssignmentDAO();
			egpimsAssignmentDAO.create(egEmpAssignment);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error(e);
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}
	}
	public  void createDisciplinaryPunishment(DisciplinaryPunishment disciplinaryPunishment)

	{
		try {
			DisciplinaryPunishmentDAO egpimsDisciplinaryPunishmentDAO = EisDAOFactory.getDAOFactory().getDisciplinaryPunishmentDAO();
			egpimsDisciplinaryPunishmentDAO.create(disciplinaryPunishment);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error(e);
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}
	}
	public  void createDetOfEnquiryOfficer(DetOfEnquiryOfficer egpimsDetOfEnquiryOfficer)
	{
		try {
			DetOfEnquiryOfficerDAO detOfEnquiryOfficerDAO = EisDAOFactory.getDAOFactory().getDetOfEnquiryOfficerDAO();
			detOfEnquiryOfficerDAO.create(egpimsDetOfEnquiryOfficer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error(e);
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}

	}
	public  void deleteDetOfEnquiryOfficer(DetOfEnquiryOfficer egpimsDetOfEnquiryOfficer)
	{
		try {
			DetOfEnquiryOfficerDAO detOfEnquiryOfficerDAO = EisDAOFactory.getDAOFactory().getDetOfEnquiryOfficerDAO();
			detOfEnquiryOfficerDAO.delete(egpimsDetOfEnquiryOfficer);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			LOGGER.error(e);
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}

	}

	public  void createLtcPirticularsFields(LtcPirticulars egpimsLtcPirticulars)
	{
		try {
			LtcPirticularsDAO egpimsLtcPirticularsDAO = EisDAOFactory.getDAOFactory().getLtcPirticularsDAO();
			egpimsLtcPirticularsDAO.create(egpimsLtcPirticulars);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error(e);
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}
	}
	public void createTrainingPirticularsFields(TrainingPirticulars egpimsTrainingPirticulars)
	{
		try {
			TrainingPirticularsDAO egpimsTrainingPirticularsDAO = EisDAOFactory.getDAOFactory().getTrainingPirticularsDAO();
			egpimsTrainingPirticularsDAO.create(egpimsTrainingPirticulars);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error(e);
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}
	}

	public PersonalInformation createEmloyee(PersonalInformation egpimsPersonalInformation)
	{
		PersonalInformation personalInformation = null;
		try
		{
			PersonalInformationDAO egpimsPersonalInformationDAO = EisDAOFactory.getDAOFactory().getPersonalInformationDAO();
			if(egpimsPersonalInformation!=null)
			{
				personalInformation =  (PersonalInformation)egpimsPersonalInformationDAO.create(egpimsPersonalInformation);
				CommonsService cm = EisManagersUtill.getCommonsService();
				String[] attrName=(cm.getAccountdetailtypeAttributename(null,"Employee")).split("#");
				Accountdetailkey adk=new Accountdetailkey();
				Query qry= getCurrentSession().createQuery(" from Accountdetailtype where id=:id ");
				qry.setInteger("id", Integer.parseInt(attrName[0]));
				adk.setAccountdetailtype((Accountdetailtype)qry.uniqueResult());
				adk.setGroupid(1);
				adk.setDetailkey(personalInformation.getIdPersonalInformation());
				adk.setDetailname(attrName[1]);
				//adk.setAccountdetailtype(Integer.parseInt(attrName[0]));
				cm.createAccountdetailkey(adk);
			}
		}
		catch(Exception e)
		{
			LOGGER.error(e);
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}

		return personalInformation;
	}

public void updateEmloyee(PersonalInformation egpimsPersonalInformation)
{
	try {
		PersonalInformationDAO egpimsPersonalInformationDAO = EisDAOFactory.getDAOFactory().getPersonalInformationDAO();
		if(egpimsPersonalInformation!=null)
			egpimsPersonalInformationDAO.update(egpimsPersonalInformation);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		LOGGER.error(e);
		//HibernateUtil.rollbackTransaction();
		throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
	}
}
public PersonalInformation getEmloyeeById(Integer employeeId)
{
	PersonalInformation egpimsPersonalInformation = null;
	PersonalInformationDAO egpimsPersonalInformationDAO = EisDAOFactory.getDAOFactory().getPersonalInformationDAO();
	egpimsPersonalInformation = 	egpimsPersonalInformationDAO.getPersonalInformationByID(employeeId);
	return egpimsPersonalInformation;
}
public EmployeeDepartment getEmployeeDepartmentById(Integer iD)
{
	EmployeeDepartment employeeDepartment = null;
	EmployeeDepartmentDAO employeeDepartmentDao= EisDAOFactory.getDAOFactory().getEmployeeDepartmentDAO();
	employeeDepartment = 	employeeDepartmentDao.getEmployeeDepartmentByID(iD);
	return employeeDepartment;
}
public GenericMaster getGenericMaster(Integer masterId,String masterName)
{
	GenericMaster genericMaster =null;
	GenericMasterDAO genericMasterDAO = new GenericMasterDAO();
	genericMaster = (GenericMaster)genericMasterDAO.getGenericMaster(masterId.intValue(),masterName);
	return genericMaster;
}
public  void addDisiplinaryApproval(DisciplinaryPunishmentApproval disciplinaryPunishmentApproval) throws SQLException
{
	try {
		DisciplinaryPunishmentApprovalDAO disciplinaryPunishmentApprovalDAO =  EisDAOFactory.getDAOFactory().getDisciplinaryPunishmentApprovalDAO();
		disciplinaryPunishmentApprovalDAO.create(disciplinaryPunishmentApproval);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		LOGGER.error(e);
		//HibernateUtil.rollbackTransaction();
		throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
	}

}

public DisciplinaryPunishmentApproval getDisciplinaryPunishmentApprovalById(Integer id)
{
	DisciplinaryPunishmentApproval disciplinaryPunishmentApproval = null;
	DisciplinaryPunishmentApprovalDAO disciplinaryPunishmentApprovalDAO =  EisDAOFactory.getDAOFactory().getDisciplinaryPunishmentApprovalDAO();
	disciplinaryPunishmentApproval = disciplinaryPunishmentApprovalDAO.getDisciplinaryPunishmentApprovalByID(id);
	return disciplinaryPunishmentApproval;
}

public EduDetails getEduDetailsById(Integer eduDetailsId)
{
	EduDetails eduDetails =null;
	EduDetailsDAO egpimsEduDetailsDAO =  EisDAOFactory.getDAOFactory().getEduDetailsDAO();
	eduDetails = egpimsEduDetailsDAO.getEduDetailsByID(eduDetailsId);
	return eduDetails;
}
public void updateEduDetails(EduDetails egpimsEduDetails)
{
	try {
		EduDetailsDAO egpimsEduDetailsDAO =  EisDAOFactory.getDAOFactory().getEduDetailsDAO();
		if(egpimsEduDetails!=null)
			egpimsEduDetailsDAO.update(egpimsEduDetails);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		LOGGER.error(e);
		//HibernateUtil.rollbackTransaction();
		throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
	}
}

public Regularisation getRegularisationById(Integer regularisationId)
{
	Regularisation egpimsRegularisation = null;
	RegularisationDAO egpimsRegularisationDAO =  EisDAOFactory.getDAOFactory().getRegularisationDAO();
	egpimsRegularisation = egpimsRegularisationDAO.getRegularisationByID(regularisationId);
	return egpimsRegularisation;
}
public Probation getProbationId(Integer probationId)
{
	Probation egpimsProbation = null;
	ProbationDAO egpimsProbationDAO =  EisDAOFactory.getDAOFactory().getProbationDAO();
	egpimsProbation = egpimsProbationDAO.getProbationByID(probationId);
	return egpimsProbation;
}
public void updateRegularisation(Regularisation egpimsRegularisation)
{
	try {
		ProbationDAO egpimsProbationDAO =  EisDAOFactory.getDAOFactory().getProbationDAO();
		if(egpimsRegularisation!=null)
			egpimsProbationDAO.update(egpimsRegularisation);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		LOGGER.error(e);
		//HibernateUtil.rollbackTransaction();
		throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
	}
}
public void updateProbation(Probation egpimsProbation)
{
	try {
		ProbationDAO egpimsProbationDAO =  EisDAOFactory.getDAOFactory().getProbationDAO();
		if(egpimsProbation!=null)
			egpimsProbationDAO.update(egpimsProbation);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		LOGGER.error(e);
		//HibernateUtil.rollbackTransaction();
		throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
	}
}
public TecnicalQualification getTecnicalQualificationById(Integer tecnicalQualificationId)
{
	TecnicalQualification egpimsTecnicalQualification = null;
	TecnicalQualificationDAO tecnicalQualificationDAO =  EisDAOFactory.getDAOFactory().getTecnicalQualificationDAO();
	egpimsTecnicalQualification = tecnicalQualificationDAO.getTecnicalQualificationByID(tecnicalQualificationId);
	return egpimsTecnicalQualification;
}
public void updateTecnicalQualification(TecnicalQualification egpimsTecnicalQualification)
{
	try {
		TecnicalQualificationDAO tecnicalQualificationDAO =  EisDAOFactory.getDAOFactory().getTecnicalQualificationDAO();
		if(egpimsTecnicalQualification!=null)
			tecnicalQualificationDAO.update(egpimsTecnicalQualification);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		LOGGER.error(e);
		//HibernateUtil.rollbackTransaction();
		throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
	}
}
public DeptTests getDeptTestsById(Integer deptTestsId)
{
	DeptTests egpimsDeptTests =null;
	DeptTestsDAO egpimsDeptTestsDAO =  EisDAOFactory.getDAOFactory().getDeptTestsDAO();
	egpimsDeptTests = egpimsDeptTestsDAO.getDeptTestsByID(deptTestsId);
	return egpimsDeptTests;
}
public void updateDeptTests(DeptTests egpimsDeptTests)
{
	try {
		DeptTestsDAO egpimsDeptTestsDAO =  EisDAOFactory.getDAOFactory().getDeptTestsDAO();
		if(egpimsDeptTests!=null)
			egpimsDeptTestsDAO.update(egpimsDeptTests);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		LOGGER.error(e);
		//HibernateUtil.rollbackTransaction();
		throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
	}
}
public AssignmentPrd getAssignmentPrdById(Integer assignmentPrdId)
{
	AssignmentPrd assignmentPrd = null;
	AssignmentPrdDAO assignmentPrdDAO =  EisDAOFactory.getDAOFactory().getAssignmentPrdDAO();
	assignmentPrd = assignmentPrdDAO.getAssignmentPrdById(assignmentPrdId);
	return assignmentPrd;
}
public void updateAssignmentPrd(AssignmentPrd assignmentPrd)
{
	try {
		AssignmentPrdDAO assignmentPrdDAO =  EisDAOFactory.getDAOFactory().getAssignmentPrdDAO();
		if(assignmentPrd!=null)
			assignmentPrdDAO.update(assignmentPrd);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		LOGGER.error(e);
		//HibernateUtil.rollbackTransaction();
		throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
	}

}



public Assignment getAssignmentById(Integer assignmentId)
{
	Assignment assignment = null;
	AssignmentDAO assignmentDAO =  EisDAOFactory.getDAOFactory().getAssignmentDAO();
	assignment = assignmentDAO.getAssignmentById(assignmentId);
	return assignment;
}
public void updateAssignment(Assignment assignment)
{
	try {
		AssignmentDAO assignmentDAO =  EisDAOFactory.getDAOFactory().getAssignmentDAO();
		if(assignment!=null)
			assignmentDAO.update(assignment);
	} catch (RuntimeException e) {
		// TODO Auto-generated catch block
		LOGGER.error(e);
		//HibernateUtil.rollbackTransaction();
		throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
	}
}


public NomimationPirticulars getNomimationPirticularsById(Integer nomimationPirticularsId)
{
	NomimationPirticulars egpimsNomimationPirticulars =null;
	NomimationPirticularsDAO nomimationPirticularsDAO =  EisDAOFactory.getDAOFactory().getNomimationPirticularsDAO();
	egpimsNomimationPirticulars = nomimationPirticularsDAO.getNomimationPirticularsByID(nomimationPirticularsId);
	return egpimsNomimationPirticulars;
}
public void updateNomimationPirticulars(NomimationPirticulars egpimsNomimationPirticulars)
{
	try {
		NomimationPirticularsDAO nomimationPirticularsDAO =  EisDAOFactory.getDAOFactory().getNomimationPirticularsDAO();
		if(egpimsNomimationPirticulars!=null)
			nomimationPirticularsDAO.update(egpimsNomimationPirticulars);
	} catch (RuntimeException e) {
		// TODO Auto-generated catch block
		LOGGER.error(e);
		//HibernateUtil.rollbackTransaction();
		throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
	}
}
public ImmovablePropDetails getImmovablePropDetailsById(Integer immovablePropDetailsId)
{
	ImmovablePropDetails egpimsImmovablePropDetails = null;
	ImmovablePropDetailsDAO immovablePropDetailsDAO =  EisDAOFactory.getDAOFactory().getImmovablePropDetailsDAO();
	egpimsImmovablePropDetails = immovablePropDetailsDAO.getImmovablePropDetailsByID(immovablePropDetailsId);
	return egpimsImmovablePropDetails;
}
public void updateImmovablePropDetails(ImmovablePropDetails egpimsImmovablePropDetails)
{
	try {
		ImmovablePropDetailsDAO immovablePropDetailsDAO =  EisDAOFactory.getDAOFactory().getImmovablePropDetailsDAO();
		if(egpimsImmovablePropDetails!=null)
			immovablePropDetailsDAO.update(egpimsImmovablePropDetails);
	} catch (RuntimeException e) {
		// TODO Auto-generated catch block
		LOGGER.error(e);
		//HibernateUtil.rollbackTransaction();
		throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
	}
}

public MovablePropDetails getMovablePropDetailsById(Integer movablePropDetailsId)
{
	MovablePropDetails egpimsMovablePropDetails = null;
	MovablePropDetailsDAO egpimsMovablePropDetailsDAO =  EisDAOFactory.getDAOFactory().getMovablePropDetailsDAO();
	egpimsMovablePropDetails = egpimsMovablePropDetailsDAO.getMovablePropDetailsByID(movablePropDetailsId);
	return egpimsMovablePropDetails;
}
public void updateMovablePropDetails(MovablePropDetails egpimsMovablePropDetails)
{
	try {
		MovablePropDetailsDAO egpimsMovablePropDetailsDAO =  EisDAOFactory.getDAOFactory().getMovablePropDetailsDAO();
		if(egpimsMovablePropDetails!=null)
			egpimsMovablePropDetailsDAO.update(egpimsMovablePropDetails);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		LOGGER.error(e);
		//HibernateUtil.rollbackTransaction();
		throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
	}
}
public void updateDisciplinaryPunishment(DisciplinaryPunishment egpimsDisciplinaryPunishment)
{
	try {
		DisciplinaryPunishmentDAO egpimsDisciplinaryPunishmentDAO = EisDAOFactory.getDAOFactory().getDisciplinaryPunishmentDAO();
		if(egpimsDisciplinaryPunishment!=null)
			egpimsDisciplinaryPunishmentDAO.update(egpimsDisciplinaryPunishment);
	} catch (RuntimeException e) {
		// TODO Auto-generated catch block
		LOGGER.error(e);
		//HibernateUtil.rollbackTransaction();
		throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
	}
}
public DetOfEnquiryOfficer getEnquiryOfficerById(Integer enquiryOfficerId)
{
	DetOfEnquiryOfficer egpimsDetOfEnquiryOfficer = null;
	DetOfEnquiryOfficerDAO egpimsDetOfEnquiryOfficerDAO =EisDAOFactory.getDAOFactory().getDetOfEnquiryOfficerDAO();
	egpimsDetOfEnquiryOfficer = egpimsDetOfEnquiryOfficerDAO.getDetOfEnquiryOfficerByID(enquiryOfficerId);
	return egpimsDetOfEnquiryOfficer;
}
public void updateDetOfEnquiryOfficer(DetOfEnquiryOfficer egpimsDetOfEnquiryOfficer)
{
	try {
		DetOfEnquiryOfficerDAO egpimsDetOfEnquiryOfficerDAO =EisDAOFactory.getDAOFactory().getDetOfEnquiryOfficerDAO();
		if(egpimsDetOfEnquiryOfficer!=null)
			egpimsDetOfEnquiryOfficerDAO.update(egpimsDetOfEnquiryOfficer);
	} catch (RuntimeException e) {
		// TODO Auto-generated catch block
		LOGGER.error(e);
		//HibernateUtil.rollbackTransaction();
		throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
	}
}
public void updateTrainingPirticulars(TrainingPirticulars egpimsTrainingPirticulars)
{
	try {
		TrainingPirticularsDAO egpimsTrainingPirticularsDAO =  EisDAOFactory.getDAOFactory().getTrainingPirticularsDAO();
		if(egpimsTrainingPirticulars!=null)
			egpimsTrainingPirticularsDAO.update(egpimsTrainingPirticulars);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		LOGGER.error(e);
		//HibernateUtil.rollbackTransaction();
		throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
	}
}
public TrainingPirticulars getTrainingPirticularsById(Integer trainingPirticularsId)
{
	TrainingPirticulars trainingPirticulars = null;
	TrainingPirticularsDAO egpimsTrainingPirticularsDAO =  EisDAOFactory.getDAOFactory().getTrainingPirticularsDAO();
	trainingPirticulars = egpimsTrainingPirticularsDAO.getTrainingPirticularsByID(trainingPirticularsId);
	return trainingPirticulars;

}
public LtcPirticulars getLtcPirticularsById(Integer ltcPirticularsId)
{
	LtcPirticulars egpimsLtcPirticulars = null;
	LtcPirticularsDAO egpimsLtcPirticularsDAO =EisDAOFactory.getDAOFactory().getLtcPirticularsDAO();
	egpimsLtcPirticulars = egpimsLtcPirticularsDAO.getLtcPirticularsByID(ltcPirticularsId);
	return egpimsLtcPirticulars;
}
public void updateLtcPirticulars(LtcPirticulars egpimsLtcPirticulars)
{
	try {
		LtcPirticularsDAO egpimsLtcPirticularsDAO =EisDAOFactory.getDAOFactory().getLtcPirticularsDAO();
		if(egpimsLtcPirticulars!=null)
			egpimsLtcPirticularsDAO.update(egpimsLtcPirticulars);
	} catch (RuntimeException e) {
		// TODO Auto-generated catch block
		LOGGER.error(e);
		//HibernateUtil.rollbackTransaction();
		throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
	}
}

public DisciplinaryPunishment getDisciplinaryPunishmentById(Integer disciplinaryPunishmentId)
{
	DisciplinaryPunishment disciplinaryPunishment = null;
	DisciplinaryPunishmentDAO disciplinaryPunishmentDao =EisDAOFactory.getDAOFactory().getDisciplinaryPunishmentDAO();
	disciplinaryPunishment = disciplinaryPunishmentDao.getDisciplinaryPunishmentByID(disciplinaryPunishmentId);
	return disciplinaryPunishment;
}
public void updateBankDet(BankDet bankDet)
{
	try {
		BankDetDAO egpimsBankDetDAO = EisDAOFactory.getDAOFactory().getBankDetDAO();
		if(bankDet!=null)
			egpimsBankDetDAO.update(bankDet);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		LOGGER.error(e);
		//HibernateUtil.rollbackTransaction();
		throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
	}
}
public void addAssignmentPrd(PersonalInformation personalInformation,AssignmentPrd assignmentPrd)
{
 try {
	personalInformation.addAssignmentPrd(assignmentPrd);
} catch (Exception e) {
	// TODO Auto-generated catch block
	LOGGER.error(e);
	//HibernateUtil.rollbackTransaction();
	throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
}
}
public void addBankDets(PersonalInformation personalInformation,BankDet bankDet)
{
 try {
	personalInformation.addBankDets(bankDet);
} catch (Exception e) {
	// TODO Auto-generated catch block
	LOGGER.error(e);
	//HibernateUtil.rollbackTransaction();
	throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
}
}
public Map getDisciplinaryPunishmentByEmployeeID(Integer ID)
{

		PersonalInformationDAO egpimsPersonalInformationDAO = EisDAOFactory.getDAOFactory().getPersonalInformationDAO();
		Map dispunMap =new HashMap();
	  try
		{
			PersonalInformation egpimsPersonalInformation = egpimsPersonalInformationDAO.getPersonalInformationByID(ID);
			Set dispunSet = egpimsPersonalInformation.getEgpimsDisciplinaryPunishments();
			Iterator iter = dispunSet.iterator();
			while(iter.hasNext())
			{
				DisciplinaryPunishment disciplinaryPunishment =(DisciplinaryPunishment)iter.next();
				dispunMap.put(disciplinaryPunishment.getId(),disciplinaryPunishment.getApplicationNumber());
			}

		}
		catch (Exception e)
		{
				LOGGER.error(e);
				//HibernateUtil.rollbackTransaction();
				throw new EGOVRuntimeException("Hibernate Exception : "+e.getMessage(),e);
		}
return dispunMap;
}

public void addDisciplinaryPunishment(PersonalInformation personalInformation,DisciplinaryPunishment disciplinaryPunishment) throws SQLException
{
 try {
	personalInformation.addDisciplinaryPunishment(disciplinaryPunishment);
} catch (Exception e) {
	// TODO Auto-generated catch block
	LOGGER.error(e);
	//HibernateUtil.rollbackTransaction();
	throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
}
}
public void addEduDetails(PersonalInformation personalInformation,EduDetails eduDetails)
{
 try {
	personalInformation.addEduDetails(eduDetails);
} catch (Exception e) {
	// TODO Auto-generated catch block
	LOGGER.error(e);
	//HibernateUtil.rollbackTransaction();
	throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
}
}
public void addImmovablePropDetailses(PersonalInformation personalInformation,ImmovablePropDetails immovablePropDetails)
{
 try {
	personalInformation.addImmovablePropDetailses(immovablePropDetails);
} catch (RuntimeException e) {
	// TODO Auto-generated catch block
	LOGGER.error(e);
	//HibernateUtil.rollbackTransaction();
	throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
}
}
public void addLangKnown(PersonalInformation personalInformation,LangKnown langKnown)
{
 try {
	personalInformation.addLangKnown(langKnown);
} catch (RuntimeException e) {
	// TODO Auto-generated catch block
	LOGGER.error(e);
	//HibernateUtil.rollbackTransaction();
	throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
}
}
public void addLtcPirticulars(PersonalInformation personalInformation,LtcPirticulars ltcPirticulars)
{
 try {
	personalInformation.addLtcPirticulars(ltcPirticulars);
} catch (RuntimeException e) {
	// TODO Auto-generated catch block
	LOGGER.error(e);
	//HibernateUtil.rollbackTransaction();
	throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
}
}
public void addMovablePropDetails(PersonalInformation personalInformation,MovablePropDetails movablePropDetails)
{
 try {
	personalInformation.addMovablePropDetails(movablePropDetails);
} catch (RuntimeException e) {
	// TODO Auto-generated catch block
	LOGGER.error(e);
	//HibernateUtil.rollbackTransaction();
	throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
}
}
public void addNomimationPirticularses(PersonalInformation personalInformation,NomimationPirticulars nomimationPirticulars)
{
 try {
	personalInformation.addNomimationPirticularses(nomimationPirticulars);
} catch (RuntimeException e) {
	// TODO Auto-generated catch block
	LOGGER.error(e);
	//HibernateUtil.rollbackTransaction();
	throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
}
}
public void addPersonAddresses(PersonalInformation personalInformation,PersonAddress personAddress)
{
 try {
	personalInformation.addPersonAddresses(personAddress);
} catch (RuntimeException e) {
	// TODO Auto-generated catch block
	LOGGER.error(e);
	//HibernateUtil.rollbackTransaction();
	throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
}
}
public void addProbations(PersonalInformation personalInformation,Probation probation)
{
 try {
	personalInformation.addProbations(probation);
} catch (RuntimeException e) {
	// TODO Auto-generated catch block
	LOGGER.error(e);
	//HibernateUtil.rollbackTransaction();
	throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
}
}
public void addRegularisation(PersonalInformation personalInformation,Regularisation regularisation)
{
 try {
	personalInformation.addRegularisation(regularisation);
} catch (RuntimeException e) {
	// TODO Auto-generated catch block
	LOGGER.error(e);
	//HibernateUtil.rollbackTransaction();
	throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
}
}
public void addTecnicalQualification(PersonalInformation personalInformation,TecnicalQualification tecnicalQualification)
{
 try {
	personalInformation.addTecnicalQualification(tecnicalQualification);
} catch (RuntimeException e) {
	// TODO Auto-generated catch block
	LOGGER.error(e);
	//HibernateUtil.rollbackTransaction();
	throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
}
}
public void addTrainingPirticularses(PersonalInformation personalInformation,TrainingPirticulars trainingPirticulars)
{
 try {
	personalInformation.addTrainingPirticularses(trainingPirticulars);
} catch (RuntimeException e) {
	// TODO Auto-generated catch block
	LOGGER.error(e);
	//HibernateUtil.rollbackTransaction();
	throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
}
}
public void addDeptTests(PersonalInformation personalInformation,DeptTests deptTests)
{
	 try {
		personalInformation.addDeptTests(deptTests);
	} catch (RuntimeException e) {
		// TODO Auto-generated catch block
		LOGGER.error(e);
		//HibernateUtil.rollbackTransaction();
		throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
	}
}
public void addDetOfEnquiryOfficer(DisciplinaryPunishment disciplinaryPunishment,DetOfEnquiryOfficer detOfEnquiryOfficer)
{
	try {
		disciplinaryPunishment.addDetOfEnquiryOfficer(detOfEnquiryOfficer);
	} catch (RuntimeException e) {
		// TODO Auto-generated catch block
		LOGGER.error(e);
		//HibernateUtil.rollbackTransaction();
		throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
	}
}


public List getListOfEmpforDept(Integer deptId)
{
	PersonalInformation personalInformation =null;
	List listOfEmpOfSameDept = new ArrayList();
	try
	{
		Collection employeeList = searchEmployee(Integer.valueOf(deptId),Integer.valueOf(0),"","","false");
		if(!employeeList.isEmpty())
		{
			Iterator iter =employeeList.iterator();
			while(iter.hasNext())
			{
				EmployeeView cataEl = (EmployeeView)iter.next();
				personalInformation =(PersonalInformation)getEmloyeeById(cataEl.getId());
				listOfEmpOfSameDept.add(personalInformation);

			}
		}

	}catch(Exception e){LOGGER.error(e);
	//HibernateUtil.rollbackTransaction();
	throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);}
	return listOfEmpOfSameDept;
}
public List getListOfEmpforDesignation(Integer desigId)
{

	PersonalInformation personalInformation =null;
	List listOfEmpOfSameDesig = new ArrayList();

	try
	{
		Collection employeeList = searchEmployee(Integer.valueOf(0),Integer.valueOf(desigId),"","","false");
		if(!employeeList.isEmpty())
		{
			Iterator iter =employeeList.iterator();
			while(iter.hasNext())
			{
				EmployeeView cataEl = (EmployeeView)iter.next();
				personalInformation =(PersonalInformation)getEmloyeeById(cataEl.getId());
				listOfEmpOfSameDesig.add(personalInformation);

			}
		}

	}catch(Exception e){LOGGER.error(e);
	//HibernateUtil.rollbackTransaction();
	throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);}
	return listOfEmpOfSameDesig;
}
public EmployeeNamePoJo getNameOfEmployee(Integer empId)
{
	try {
		EmployeeNamePoJo employeeNamePoJo =null;
		PersonalInformation personalInformation = getEmloyeeById(Integer.valueOf(empId));
		  String fn ="";
		  String mn ="";
		  String ln ="";
		  if(personalInformation.getEmployeeFirstName()!=null)
		  {
			fn = personalInformation.getEmployeeFirstName();
		  }
		  if(personalInformation.getEmployeeMiddleName()!=null)
		  {
		  	mn = personalInformation.getEmployeeMiddleName();
		  }
		  if(personalInformation.getEmployeeLastName()!=null)
		  {
		  	ln = personalInformation.getEmployeeLastName();
		  }
		  employeeNamePoJo = new EmployeeNamePoJo(fn,mn,ln);
		return employeeNamePoJo;
	} catch (Exception e) {
		// TODO Auto-generated catch block
		LOGGER.error(e);
		//HibernateUtil.rollbackTransaction();
		throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
	}

}




public   Map getAllPIMap()
{
	PersonalInformationDAO personalInformationDAO = EisDAOFactory.getDAOFactory().getPersonalInformationDAO();
	return personalInformationDAO.getAllPIMap();
}

public  Map getMapForList(List list)
{
	Map<Integer,String> retMap = new LinkedHashMap<Integer,String>();
		try
		{

		for (Iterator iter = list.iterator(); iter.hasNext();)
		{
			Object object = (Object)iter.next();
			Class classObj = object.getClass();

			Field id = classObj.getField("id");
			Field name = classObj.getField("name");
			retMap.put((Integer)id.get(object), (String)name.get(object));

		}
	}catch(NoSuchFieldException nfe)
	{
		//HibernateUtil.rollbackTransaction();
		throw new EGOVRuntimeException("Exception:" + nfe.getMessage(),nfe);
	}
	catch(IllegalAccessException  iac)
	{
		//HibernateUtil.rollbackTransaction();
		throw new EGOVRuntimeException("Exception:" + iac.getMessage(),iac);
	}
		return retMap;

}

/**
 * Returns Map for a given list
 * @param list
 * @return
 */
public  Map getMapForList(List list, String fieldName1, String fieldName2)
{
	Map<Integer,String> retMap = new LinkedHashMap<Integer,String>();
	try
	{
		String id   =  null;
		String  name =  null;
		Long longObj = null;
		for (Iterator iter = list.iterator(); iter.hasNext();)
		{
			Object object = (Object)iter.next();

			id 		= 	(String)BeanUtils.getProperty(object,fieldName1);
			name 	= 	(String)BeanUtils.getProperty(object,fieldName2);
			if(id != null)
			{
				retMap.put(Integer.valueOf(id), (String)name);
			}
		}
	} catch(IllegalAccessException  iac){

		//HibernateUtil.rollbackTransaction();
		throw new EGOVRuntimeException("Exception:" + iac.getMessage(),iac);
	} catch (InvocationTargetException e) {
		LOGGER.error(e);
		//HibernateUtil.rollbackTransaction();
		throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
	} catch (NoSuchMethodException e) {
		LOGGER.error(e);
		//HibernateUtil.rollbackTransaction();
		throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
	}
	return retMap;

}
private  void setApprovedDisiplinaryForEmpID(Integer empId)
{

	/*try {
		appliedDisiplinaryApplications = new ArrayList();
		rejectedDisiplinaryApplications = new ArrayList();
		approvedDisiplinaryApplications = new ArrayList();

		PersonalInformationDAO egpimsPersonalInformationDAO = EisDAOFactory.getDAOFactory().getPersonalInformationDAO();
		PersonalInformation personalInformation = getEmloyeeById(empId);
		Set disciplinaryApplicationSet =  personalInformation.getEgpimsDisciplinaryPunishments();
		for(Iterator iter = disciplinaryApplicationSet.iterator();iter.hasNext();)
		{
			DisciplinaryPunishment disciplinaryPunishments =(DisciplinaryPunishment)iter.next();
			if(disciplinaryPunishments.getStatusId().getId().intValue()==1)
				appliedDisiplinaryApplications.add(disciplinaryPunishments);
			else if(disciplinaryPunishments.getStatusId().getId().intValue()==3)
				rejectedDisiplinaryApplications.add(disciplinaryPunishments);
			else if(disciplinaryPunishments.getStatusId().getId().intValue()==2)
				approvedDisiplinaryApplications.add(disciplinaryPunishments);
		}
	} catch (Exception e) {
		// TODO Auto-generated catch block
		LOGGER.error(e);
		//HibernateUtil.rollbackTransaction();
		throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);

	}*/

}
public  void deleteLangKnownForEmp(PersonalInformation personalInformation)
{
	try {
		PersonalInformationDAO personalInformationDAO = EisDAOFactory.getDAOFactory().getPersonalInformationDAO();
		personalInformationDAO.deleteLangKnownForEmp(personalInformation);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		LOGGER.error(e);
		//HibernateUtil.rollbackTransaction();
		throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
	}
}

public  List getAssPrdIdsForEmployee(Integer empId)
{

		//session = HibernateUtil.getCurrentSession();
		AssignmentPrd assignmentprd = new AssignmentPrd();
		List empSet = new ArrayList();
		try
		{
			String mainStr = "";
			mainStr = " select 	ev.assignmentPrd  from EmployeeView ev  where ev.id = :empId and ((ev.toDate is null ) OR (ev.fromDate <= SYSDATE AND ev.toDate > SYSDATE) OR (ev.fromDate > SYSDATE AND ev.toDate > SYSDATE)  OR (ev.fromDate < SYSDATE AND ev.toDate < SYSDATE) ) order by ev.fromDate asc";
			Query qry = getCurrentSession().createQuery(mainStr);

			if(empId != null)
			{
				qry.setInteger("empId", empId);

			}
			if(qry.list()!=null&&!qry.list().isEmpty())
			{

				for(Iterator iter = qry.list().iterator();iter.hasNext();)
				{
					assignmentprd = (AssignmentPrd)iter.next();
					empSet.add(assignmentprd);

				}

			}
		}
		catch (HibernateException he) {
				LOGGER.error(he);
				   //HibernateUtil.rollbackTransaction();
				throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
			} catch (Exception he) {
				LOGGER.error(he);
				   //HibernateUtil.rollbackTransaction();

				throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);

	}

			return empSet;

}
public Integer getNextVal()
{

	//session = HibernateUtil.getCurrentSession();
	Integer id = Integer.valueOf(0);
	try
	{
		Query qry = getCurrentSession().createSQLQuery("SELECT SEQ_DIS_APP.nextval as id from dual").addScalar("id", IntegerType.INSTANCE);

		if(qry.list()!=null&&!qry.list().isEmpty())
		{
			Integer obj = null;
			for(Iterator iter = qry.list().iterator();iter.hasNext();)
			{
				obj = (Integer)iter.next();
				id = obj;
			}

		}

	}
	catch (HibernateException he) {
		LOGGER.error(he);
		   //HibernateUtil.rollbackTransaction();
		throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
	} catch (Exception he) {
		LOGGER.error(he);
		   //HibernateUtil.rollbackTransaction();

		throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);

}
	return id;
}



private String getNextValForCode()
{

	//session = HibernateUtil.getCurrentSession();
	Integer id = Integer.valueOf(0);
	try
	{
		Query qry = getCurrentSession().createSQLQuery("SELECT CODE AS id FROM EG_EMPLOYEE emp  WHERE emp.CODE =(SELECT MAX(code) FROM EG_EMPLOYEE )  FOR UPDATE ").addScalar("id", IntegerType.INSTANCE);
		if(qry.list()!=null&&!qry.list().isEmpty())
		{
			Integer obj = null;
			for(Iterator iter = qry.list().iterator();iter.hasNext();)
			{
				obj = (Integer)iter.next();
				if(obj==null)
				{
					id = 1;
				}
				else
				{
					id = obj+1;
				}
			}
		}
	}
	catch (HibernateException he) {
		LOGGER.error(he);
		   //HibernateUtil.rollbackTransaction();
		throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
	} catch (Exception he) {
		LOGGER.error(he);
		   //HibernateUtil.rollbackTransaction();

		throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);

}
	return String.valueOf(id);
}

public String getEmployeeCode()
{
	LOGGER.info("getNextValForCode"+getNextValForCode());
	return getNextValForCode();
}


public  boolean checkPos(Integer posId,Date fromDate,Date toDate,Integer empId,String isPrimary)
{

	boolean b = false;
	//session = HibernateUtil.getCurrentSession();

	try
	{
		Query qry =null;

		if(fromDate != null && toDate != null)
		{
			String main = "from Assignment ev  where ev.isPrimary =:isPrimary and ev.position.id = :posId and ";
			if(empId!=null)
			{
				main +="ev.assignmentPrd.employeeId.idPersonalInformation <>:empId and ";
			}
			main +=	"((ev.assignmentPrd.toDate is null ) or " +
					" (ev.assignmentPrd.fromDate <= :fromDate and ev.assignmentPrd.toDate >= :toDate) or " +
					" (ev.assignmentPrd.toDate <= :toDate and ev.assignmentPrd.toDate >= :fromDate) or " +
					" (ev.assignmentPrd.fromDate >= :fromDate and ev.assignmentPrd.fromDate <= :toDate))  ";

			qry=getCurrentSession().createQuery(main);

		}
		else if(fromDate != null  &&  toDate==null)
		{
			qry = getCurrentSession().createQuery("from Assignment ev  where ev.position.id = :posId and ((ev.assignmentPrd.toDate is null ) or (ev.assignmentPrd.fromDate <= :fromDate AND ev.assignmentPrd.toDate >= :fromDate))");

		}
		if(posId != null )
		{
			qry.setInteger("posId", posId);

		}
		if(empId!=null)
		{
			qry.setInteger("empId", empId);

		}
		if(isPrimary!=null)
		{
			qry.setCharacter("isPrimary", Character.valueOf(isPrimary.charAt(0)));
		}
		if(fromDate != null && toDate != null)
		{
			qry.setDate("fromDate", new java.sql.Date (fromDate.getTime()));
			qry.setDate("toDate", new java.sql.Date (toDate.getTime()));

		}
		else if(fromDate != null  &&  toDate==null)
		{
			qry.setDate("fromDate", new java.sql.Date (fromDate.getTime()));
		}

		if(qry.list()!=null&&!qry.list().isEmpty())
		{
				b = true;
		}

	}
	catch (HibernateException he) {
			LOGGER.error(he);
			   //HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
		} catch (Exception he) {
			LOGGER.error(he);
			   //HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);

}
	return b;

}
public boolean getHodById(Integer id)
{
	AssignmentDAO egpimsAssignmentDAO = EisDAOFactory.getDAOFactory().getAssignmentDAO();
	return egpimsAssignmentDAO.getHodById(id);

}
public List getListOfPersonalInformationByEmpIdsList(List empIdsList)
{
	List list = null;
	try
	{
		if(empIdsList != null && !empIdsList.isEmpty())
		{
			PersonalInformationDAO personalInformationDAO = EisDAOFactory.getDAOFactory().getPersonalInformationDAO();
			list = (List)personalInformationDAO.getListOfPersonalInformationByEmpIdsList(empIdsList);
		}
	}catch (HibernateException he) {
		LOGGER.error(he);
		//HibernateUtil.rollbackTransaction();
		throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
	} catch (Exception he) {
		LOGGER.error(he);
		//HibernateUtil.rollbackTransaction();
		throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
	}
	return list;
}
public List getListOfEmployeeWithoutAssignment(Date fromdate)
{
List list = null;
try
{

	AssignmentDAO noassignmentDao = EisDAOFactory.getDAOFactory(). getAssignmentDAO();

	list = (List)noassignmentDao.getListOfEmployeeWithoutAssignment( fromdate);

}catch (HibernateException he) {
	LOGGER.error(he);
	//HibernateUtil.rollbackTransaction();
	throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
} catch (Exception he) {
	LOGGER.error(he);
	//HibernateUtil.rollbackTransaction();
	throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
}
return list;
}
	public Assignment getLatestAssignmentForEmployeeByToDate(Integer empId,Date todate) throws Exception
	{
		Assignment assignment = null;
		try
		{

			AssignmentDAO noassignmentDao = EisDAOFactory.getDAOFactory(). getAssignmentDAO();

			assignment = (Assignment)noassignmentDao.getLatestAssignmentForEmployeeByToDate( empId , todate );

		}catch (HibernateException he) {
			LOGGER.error(he);
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
		} catch (Exception he) {
			LOGGER.error(he);
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
		}
		return assignment;
	}


	/**
	 * This is used for workflow
	 * Getting employee by passing dept,desig,boundary
	 * @param deptId
	 * @param designationId
	 * @param Boundaryid
	 * @return employee
	 * @throws TooManyValuesException
	 * @throws NoSuchObjectException
	 */
	public PersonalInformation getEmployee(Integer deptId, Integer designationId, Integer Boundaryid)throws TooManyValuesException, NoSuchObjectException
	{
		PersonalInformation personalInformation= null;

		try
		{
			if(deptId!=0 && deptId!=null && designationId!=0 && designationId!=null  && Boundaryid!=0 && Boundaryid!=null)
			{
				PersonalInformationDAO personalInformationDAO = EisDAOFactory.getDAOFactory().getPersonalInformationDAO();
				personalInformation = personalInformationDAO.getEmployee(deptId, designationId, Boundaryid);
			}

		}
		 catch(Exception e)
	        {
	           throw new EGOVRuntimeException("system.error", e);
	        }
		return personalInformation;
	}


	/**
	 * This is used for workflow
	 * Getting employee by passing dept,desig,boundary,functionary
	 * @param deptId
	 * @param designationId
	 * @param Boundaryid
	 * @return employee
	 * @throws TooManyValuesException
	 * @throws NoSuchObjectException
	 */
	public PersonalInformation getEmployeeByFunctionary(Integer deptId, Integer designationId, Integer Boundaryid,Integer functionaryId)throws TooManyValuesException, NoSuchObjectException
	{
		PersonalInformation personalInformation= null;

		try
		{
			if(deptId!=0 && deptId!=null && designationId!=0 && designationId!=null  && Boundaryid!=0 && Boundaryid!=null && functionaryId!=0 && functionaryId!=null)
			{
				PersonalInformationDAO personalInformationDAO = EisDAOFactory.getDAOFactory().getPersonalInformationDAO();
				personalInformation = personalInformationDAO.getEmployeeByFunctionary(deptId, designationId, Boundaryid,functionaryId);
			}

		}
		 catch(Exception e)
	        {
	           throw new EGOVRuntimeException("system.error", e);
	        }
		return personalInformation;
	}

	public Assignment getLastAssignmentByEmp(Integer empId)
	{
		//session = HibernateUtil.getCurrentSession();
		Assignment assignment = null;
		try{
			String mainStr = "";
			mainStr = "select ev.assignment from EmployeeView ev  where ev.id = :empId and nvl(ev.toDate,sysdate) in(select max(nvl(ev1.toDate,sysdate)) from EmployeeView ev1 where ev1.id = :empId)";
			Query qry = getCurrentSession().createQuery(mainStr);
			if(empId != null)
			{
				qry.setInteger("empId", empId);
			}
			if(qry.list()!=null&&!qry.list().isEmpty())
			{
				for(Iterator iter = qry.list().iterator();iter.hasNext();)
				{
					assignment = (Assignment)iter.next();
					break;
				}
			}
		}catch (HibernateException he) {
				LOGGER.error(he);
				//HibernateUtil.rollbackTransaction();
				throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
			} catch (Exception he)
			{
				LOGGER.error(he);
				//HibernateUtil.rollbackTransaction();
				throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
			}
			return assignment;
	}
	public ServiceHistory getServiceId(Integer id)
	{
		    //session = HibernateUtil.getCurrentSession();
			Query qry = getCurrentSession().createQuery("from ServiceHistory S where S.idService =:id ");
			qry.setInteger("id", id);
			return (ServiceHistory)qry.uniqueResult();

	}
	public  void createGradeMstr(GradeMaster gradeMstr)
	{
		try {
			   GradeMasterDao gradeDao=EisDAOFactory.getDAOFactory().getEisGradeDAO();
			   gradeDao.create(gradeMstr);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error(e);
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}
	}
	public  GradeMaster getGradeMstrById(Integer gradeId)
	{
		GradeMaster gradeMst = null;
		try {

			   GradeMasterDao gradeDao=EisDAOFactory.getDAOFactory().getEisGradeDAO();
			   gradeMst= gradeDao.getGradeMstrById(gradeId);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error(e);
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}
		return gradeMst;
	}
	public void updateGradeMstr(GradeMaster gradeMstr)
	{
		try {
			   GradeMasterDao gradeDao=EisDAOFactory.getDAOFactory().getEisGradeDAO();
			   gradeDao.update(gradeMstr);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error(e);
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}
	}
	public void removeGradeMstr(GradeMaster gradeMstr)throws RuntimeException
	{
		try {
			   GradeMasterDao gradeDao=EisDAOFactory.getDAOFactory().getEisGradeDAO();
			   gradeDao.delete(gradeMstr);

		} catch (Exception e) {

			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}
	}
	public  List getAllGradeMstr()
	{
		List gradeMstrList  = null;
		try {

			   GradeMasterDao gradeDao=EisDAOFactory.getDAOFactory().getEisGradeDAO();
			   gradeMstrList= gradeDao.getAllGradeMstr();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error(e);
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}
		return gradeMstrList;
	}

	@Deprecated
	/**
	 * Designations are no longer linked to grade.
	 */
	public List getAllDesgBasedOnGrade(Integer gradeId)
	{
		List gradeMstrList  = null;
		try {

			   GradeMasterDao gradeDao=EisDAOFactory.getDAOFactory().getEisGradeDAO();
			   gradeMstrList=gradeDao.getAllDesgBasedOnGrade(gradeId);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error(e);
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}
		return gradeMstrList;
	}

	public List<PersonalInformation> getAllEmpByGrade(Integer gradeId)
	{
		List<PersonalInformation> empList  = null;
		try {

			PersonalInformationDAO pimsDao=EisDAOFactory.getDAOFactory().getPersonalInformationDAO();
			empList=pimsDao.getAllEmpByGrade(gradeId);

		} catch (Exception e) {

			LOGGER.error(e);
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}
		return empList;
	}


	public  List<PersonalInformation>  getAllHodEmpByDept(Integer deptId) throws Exception{
		return EisDAOFactory.getDAOFactory().getEmployeeDepartmentDAO().getAllHodEmpByDept(deptId);
	}

	public  List<EmployeeView>  getAllHodEmpViewByDept(Integer deptId) throws Exception{
			return EisDAOFactory.getDAOFactory().getEmployeeDepartmentDAO().getAllHodEmpViewByDept(deptId);
	}

	public PersonalInformation getEmpForPositionAndDate(Date dateEntered,Integer posId)throws Exception
	{

		//session = HibernateUtil.getCurrentSession();
		PersonalInformation personalInformation = null;
		try
		{
			Query qry =null;
			if(dateEntered!=null)
			{
				qry = getCurrentSession().createQuery("select ev.id from EmployeeView ev  where ev.position = :posId and ((ev.toDate is null ) or (ev.fromDate <= :fromDate AND ev.toDate >= :fromDate))");

			}
			else if(dateEntered == null)
			{
				qry = getCurrentSession().createQuery("select ev.id from EmployeeView ev  where ev.position = :posId and ((ev.toDate is null ) or (ev.fromDate <=  TO_DATE(SYSDATE,'dd-MM-yyy') AND ev.toDate >=  TO_DATE(SYSDATE,'dd-MM-yyy')))");
			}
			if(posId != null )
			{
				qry.setInteger("posId", posId);

			}
			if(dateEntered != null)
			{
				qry.setDate("fromDate", new java.sql.Date (dateEntered.getTime()));
			}


			if(qry.list()!=null&&!qry.list().isEmpty())
			{
				for(Iterator iter = qry.list().iterator();iter.hasNext();)
				{
					Integer id = (Integer)iter.next();
					personalInformation = EisManagersUtill.getEmployeeService().getEmloyeeById(id);
				}
			}

		}
		catch (HibernateException he)
			{
				LOGGER.error(he);
				throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
			} catch (Exception he)
			{
				LOGGER.error(he);
				throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
			}



		return personalInformation;
	}
/**
 * Returns list of employees for a given position and date
 * @param dateEntered
 * @param posId
 * @return
 * @throws Exception
 */
	public List<PersonalInformation> getEmpListForPositionAndDate(Date dateEntered,Integer posId)throws Exception
	{

		//session = HibernateUtil.getCurrentSession();
		PersonalInformation personalInformation = null;
		List<PersonalInformation> empList = null;
		try
		{
			Query qry =null;
			if(dateEntered!=null)
			{
				qry = getCurrentSession().createQuery("select distinct ev.id from EmployeeView ev  where ev.position = :posId and ((ev.toDate is null ) or (ev.fromDate <= :fromDate AND ev.toDate >= :fromDate))");

			}
			else if(dateEntered == null)
			{
				qry = getCurrentSession().createQuery("select distinct ev.id from EmployeeView ev  where ev.position = :posId and ((ev.toDate is null ) or (ev.fromDate <=  TO_DATE(SYSDATE,'dd-MM-yyy') AND ev.toDate >=  TO_DATE(SYSDATE,'dd-MM-yyy')))");
			}
			if(posId != null )
			{
				qry.setInteger("posId", posId);

			}
			if(dateEntered != null)
			{
				qry.setDate("fromDate", new java.sql.Date (dateEntered.getTime()));
			}


			if(qry.list()!=null&&!qry.list().isEmpty())
			{
				empList = new ArrayList();
				for(Iterator iter = qry.list().iterator();iter.hasNext();)
				{
					Integer id = (Integer)iter.next();
					personalInformation = EisManagersUtill.getEmployeeService().getEmloyeeById(id);
					empList.add(personalInformation);
				}
			}

		}
		catch (HibernateException he)
			{
				LOGGER.error(he);
				throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
			} catch (Exception he)
			{
				LOGGER.error(he);
				throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
			}



		return empList;
	}

	/**
     * return all distinct Designations to which employees are assigned in the given department for current date.
     * This list includes primary as well as secondary assignments
     * If there is No Designation for the given department then returns the empty list
     * @param departmentId
     * @param givenDate
     * @return DesignationMaster List
     */
	public List getAllDesignationByDept(Integer deptId)
	{

		Integer departmentId = deptId;
		List<DesignationMaster> designationMstrObj = new ArrayList<DesignationMaster>();
		eisService = new EisUtilService();
		persistenceService = new PersistenceService();
		 eisService.setPersistenceService(persistenceService);
		 persistenceService.setSessionFactory(new SessionFactory());
		 persistenceService.setType(EmployeeServiceImpl.class);
		designationMstrObj=(List<DesignationMaster>)eisService.getAllDesignationByDept(departmentId, new Date());
    	return designationMstrObj;
	}

	/**
	 * Returns the list of active users who are assigned to the given designation.
	 * The designation can be a primary or temporary assignment of the user. The API does not currently
	 * check for active assignments
	 * @param DesgId
	 * @return the list of active users who are assigned to the given designation.
	 * @throws Exception
	 */

	public List getAllActiveUsersByGivenDesg(Integer DesgId)throws Exception
	{
		List userList  = null;
		try
		{
			if(DesgId!=null && DesgId!=0)
			{
				PersonalInformationDAO personalInformationDAO = EisDAOFactory.getDAOFactory().getPersonalInformationDAO();
				userList = personalInformationDAO.getAllActiveUsersByGivenDesg(DesgId);

			}

		}
		 catch(Exception e)
	        {
	           throw new EGOVRuntimeException("system.error", e);
	        }
		return userList;
	}

	/**
	 * Returns the list of employees (EmployeeView) who have a temporary assignment as on the given date assigned
	 * to the given position
	 * @param givenDate
	 * @param posId
	 * @return
	 */
	public List<EmployeeView> getEmployeeWithTempAssignment(Date givenDate,Integer posId)
	{

		List list = null;
		try
		{

			AssignmentDAO noassignmentDao = EisDAOFactory.getDAOFactory(). getAssignmentDAO();

			list = (List)noassignmentDao.getEmployeeWithTempAssignment(givenDate,posId);

		}catch (HibernateException he) {

				throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
		} catch (Exception he) {
			throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
		}

		return list;



	}

	/**
	 * Returns the list of employees (EmployeeView) who have a temporary assignment as on the given date assigned
	 * to the given position and has the employee code equal to the given code
	 * @param givenDate
	 * @param posId
	 * @return
	 */
	public List<EmployeeView> getEmployeeWithTempAssignment(String code,Date givenDate,Integer posId)
	{

		List list = null;
		try
		{

			AssignmentDAO noassignmentDao = EisDAOFactory.getDAOFactory(). getAssignmentDAO();

			list = (List)noassignmentDao.getEmployeeWithTempAssignment(code,givenDate,posId);

		}catch (HibernateException he) {

				throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
		} catch (Exception he) {
			throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
		}

		return list;



	}

	/**
	 * Returns a list of temporary Assignments as on given date for employees that have
	 * the given code and position. If any of the parameters are null, the parameter is ignored.
	 * For instance, if givenDate is null, all temporary assignments for employee with given code and
	 * when assigned to givenPosition will be returned
	 * @param code
	 * @param givenDate
	 * @param posId
	 * @return List of Assignment
	 */
	public  List getEmpTempAssignment(String code,Date givenDate,Integer posId)
	{


		//session = HibernateUtil.getCurrentSession();
		List assignment = null;
		try{
			String mainStr = "";
			mainStr = "from Assignment ev  where ev.isPrimary='N'";

			if(code!=null && !code.equals(""))
			{
				mainStr+= "and ev.assignmentPrd.employeeId.employeeCode =:code ";
			}
			if(givenDate!=null)
			{
				mainStr+=" and ev.assignmentPrd.fromDate <= :givenDate and ev.assignmentPrd.toDate >=:givenDate";
			}

			if(posId!=null && posId!=0)
			{
				mainStr+=" and ev.position.id =:posId ";
			}
			Query qry = getCurrentSession().createQuery(mainStr);
			if(code != null && !code.equals(""))
			{
				qry.setString("code", code);
			}
			if(givenDate!=null)
			{
				qry.setDate("givenDate",givenDate);
			}
			if(posId!=null && posId!=0)
			{
				qry.setInteger("posId", posId);
			}

			assignment = qry.list();
		}catch (HibernateException he) {
				LOGGER.error(he);
				//HibernateUtil.rollbackTransaction();
				throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
			} catch (Exception he)
			{
				LOGGER.error(he);
				//HibernateUtil.rollbackTransaction();
				throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
			}
			return assignment;


	}

	/**
	 * Returns a list of assignment ids. All assignments for the employee based on employee id and
	 * that fall in the given date will be returned.
	 * @param empId - Required parameter. If null is passed, the API throws an EGOVException
	 * @param givenDate. Date as on which the assignments need to be returned. If this parameter is null,
	 * 					 the current date is considered
	 * @return List of Assignment Ids
	 * @throws EGOVException
	 */
	public List<Integer> getAssignmentsForEmp(Integer empId,Date givenDate)throws EGOVException
	{
		List list = null;
		Query query =null;
		try
		{

			StringBuffer stringbuffer =new StringBuffer(" select 	ASS_ID  from EG_EIS_EMPLOYEEINFO ev  where ev.ID = :empId");

			if(empId == null)
			{
				throw new EGOVException("EmployeeId  Not provided");
			}
			else if(givenDate==null)
			{
				stringbuffer.append(" and ((ev.to_Date is null and ev.from_Date <= SYSDATE ) OR (ev.from_Date <= SYSDATE AND ev.to_Date >= SYSDATE))");

			}
			else
			{
				stringbuffer.append(" and  ev.from_Date <= :givenDate AND ev.to_Date >= :givenDate");
			}
			query = HibernateUtil.getCurrentSession().createSQLQuery(stringbuffer.toString()).addScalar("ASS_ID", IntegerType.INSTANCE);

			if(query.getQueryString().contains(":givenDate"))
			{
				query.setDate("givenDate", givenDate);
			}

			query.setInteger("empId", empId);

		}
		catch (HibernateException hibException) {
				LOGGER.error(hibException.getMessage());
				throw new EGOVException("HibernateException:" + hibException.getMessage(),hibException);
			}

			return query.list();

	}
	/**
	 * API that will return all positions for a user(temporary and permanent) for a date.
	 *
	 * @param user. Required. User object for which the positions are queried
	 * @param date Will consider current date if date is not provided
	 * @return
	 * @throws EGOVException
	 */

	public List<Position> getPositionsForUser(User user, Date date)throws EGOVException
	{

		//session = HibernateUtil.getCurrentSession();
		List<Position> positionList = new ArrayList<Position>();
		Integer pos = null;
		try{
			String mainStr = "";

				mainStr="select a.position.id from Assignment a where a.assignmentPrd.employeeId.userMaster.id =:userId";


			if(date!=null)
			{
				mainStr += " and ((a.assignmentPrd.toDate is null and a.assignmentPrd.fromDate<= :date) or (a.assignmentPrd.fromDate <= :date and a.assignmentPrd.toDate >= :date))";
			}
			else
			{
				mainStr += " and ((a.assignmentPrd.toDate is null and a.assignmentPrd.fromDate<= TO_DATE(SYSDATE,'dd-MM-yyy')) or (a.assignmentPrd.fromDate <= TO_DATE(SYSDATE,'dd-MM-yyy') and a.assignmentPrd.toDate >= TO_DATE(SYSDATE,'dd-MM-yyy')))";
			}
			Query qry = getCurrentSession().createQuery(mainStr);
			if(user != null )
			{
				qry.setInteger("userId", user.getId());
			}
			if(date!=null)
			{
				qry.setDate("date",date);
			}



			if(qry.list()!=null&&!qry.list().isEmpty())
			{

				for(Iterator iter = qry.list().iterator();iter.hasNext();)
				{
					pos = (Integer)iter.next();
					Position position=EisManagersUtill.getEisCommonsService().getPositionById(pos);
					positionList.add(position);
				}
			}

		}catch (HibernateException he) {
				LOGGER.error(he);
				//HibernateUtil.rollbackTransaction();
				throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
			} catch (Exception he)
			{
				LOGGER.error(he);
				//HibernateUtil.rollbackTransaction();
				throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
			}
		return positionList;
	}

	/**
	 * Returns a list of primary Assignments as on given date for employees that have
	 * the given code and position. If any of the parameters are null, the parameter is ignored.
	 * For instance, if givenDate is null, all primary assignments for employee with given code and
	 * when assigned to givenPosition will be returned
	 *
	 * @param code
	 * @param givenDate
	 * @param posId
	 * @return
	 */
	public  List getEmpPrimaryAssignment(String code,Date givenDate,Integer posId)
	{


		//session = HibernateUtil.getCurrentSession();
		List assignment = null;
		try{
			String mainStr = "";
			mainStr = "from Assignment ev  where ev.isPrimary='Y'";

			if(code!=null && !code.equals(""))
			{
				mainStr+= "and ev.assignmentPrd.employeeId.employeeCode =:code ";
			}
			if(givenDate!=null)
			{
				mainStr+=" and ev.assignmentPrd.fromDate <= :givenDate and ev.assignmentPrd.toDate >=:givenDate";
			}

			if(posId!=null && posId!=0)
			{
				mainStr+=" and ev.position.id =:posId ";
			}
			Query qry = getCurrentSession().createQuery(mainStr);
			if(code != null && !code.equals(""))
			{
				qry.setString("code", code);
			}
			if(givenDate!=null)
			{
				qry.setDate("givenDate",givenDate);
			}
			if(posId!=null && posId!=0)
			{
				qry.setInteger("posId", posId);
			}

			assignment = qry.list();
		}catch (HibernateException he) {
				LOGGER.error(he);
				//HibernateUtil.rollbackTransaction();
				throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
			} catch (Exception he)
			{
				LOGGER.error(he);
				//HibernateUtil.rollbackTransaction();
				throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
			}
			return assignment;


	}
	/**
	 * Api Used to get the report for employee,Retired,Deceased,Suspended based on the dates.
	 * For Employed, Retired and Deceased, the employee's assignment dates are considered.
	 * Whereas for Suspended status, the Disciplinary Action details are considered.
	 * @param status
	 * @param fromDate
	 * @param toDate
	 * @return List of Assignment
	 * @throws Exception
	 */
	public  List searchEmployee(Integer status,Date fromDate,Date toDate)throws Exception
	{
		//session = HibernateUtil.getCurrentSession();
		List<Assignment> employeeList = new ArrayList<Assignment>();
		String mainStr = "";
		try
		{
				EgwStatus statusType=EisManagersUtill.getCommonsService().getEgwStatusById(status);

				if(statusType.getModuletype().equals("Employee") && statusType.getDescription().equals("Employed"))
				{

					if(status.intValue() != 0)
					{
						mainStr = "from Assignment ev where ev.assignmentPrd.employeeId.StatusMaster.id =:employeeStatus" ;
					}
					if(fromDate != null && toDate!=null)
					{
						/*mainStr += " and ev.assignmentPrd.employeeId.dateOfFirstAppointment >= :fromDate " +
							" and ev.assignmentPrd.employeeId.dateOfFirstAppointment <= :toDate and ev.assignmentPrd.fromDate<=sysdate and ev.assignmentPrd.toDate >=sysdate" ;
						*/
						//enhanced to avoid to show all assignment
						mainStr += " and ev.assignmentPrd.employeeId.dateOfFirstAppointment >= :fromDate " +
						" and ev.assignmentPrd.employeeId.dateOfFirstAppointment <= :toDate " +
						" and ((ev.assignmentPrd.fromDate<=sysdate and ev.assignmentPrd.toDate >=sysdate) " +
						" or (ev.assignmentPrd.fromDate in (select max(prd.fromDate) from AssignmentPrd prd where ev.assignmentPrd.employeeId.idPersonalInformation = prd.employeeId.idPersonalInformation " +
						" and not exists (select prd1.id from AssignmentPrd prd1 where prd1.employeeId.idPersonalInformation=ev.assignmentPrd.employeeId.idPersonalInformation " +
						" and ( prd1.employeeId.dateOfFirstAppointment >= :fromDate and prd1.employeeId.dateOfFirstAppointment <= :toDate and prd1.fromDate<= sysdate and prd1.toDate >= sysdate) )))) " ;


					}

				}
				else if(statusType.getModuletype().equals("Employee") && statusType.getDescription().equals("Retired"))
				{
					if(status.intValue() != 0)
					{
						mainStr = "from Assignment ev where ev.assignmentPrd.employeeId.StatusMaster.id =:employeeStatus" ;
					}
					if(fromDate != null && toDate!=null)
					{
						/*mainStr += " and ev.assignmentPrd.employeeId.retirementDate >= :fromDate " +
							" and ev.assignmentPrd.employeeId.retirementDate <= :toDate and ev.assignmentPrd.fromDate<=sysdate and ev.assignmentPrd.toDate >=sysdate" ;*/

						//enhanced to avoid to show all assignment
						mainStr += " and ev.assignmentPrd.employeeId.retirementDate >= :fromDate " +
						" and ev.assignmentPrd.employeeId.retirementDate <= :toDate " +
						" and ((ev.assignmentPrd.fromDate<=sysdate and ev.assignmentPrd.toDate >=sysdate) " +
						" or (ev.assignmentPrd.fromDate in (select max(prd.fromDate) from AssignmentPrd prd where ev.assignmentPrd.employeeId.idPersonalInformation = prd.employeeId.idPersonalInformation " +
						" and not exists (select prd1.id from AssignmentPrd prd1 where prd1.employeeId.idPersonalInformation=ev.assignmentPrd.employeeId.idPersonalInformation " +
						" and ( prd1.employeeId.retirementDate >= :fromDate and prd1.employeeId.retirementDate <= :toDate and prd1.fromDate<= sysdate and prd1.toDate >= sysdate) )))) " ;

					}
				}
				else if(statusType.getModuletype().equals("Employee") && statusType.getDescription().equals("Deceased"))
				{
					if(status.intValue() != 0)
					{
						mainStr = "from Assignment ev where ev.assignmentPrd.employeeId.StatusMaster.id =:employeeStatus" ;
					}
					if(fromDate != null && toDate!=null)
					{
						//enhanced to avoid to show all assignment
						mainStr += " and ev.assignmentPrd.employeeId.deathDate >= :fromDate " +
							" and ev.assignmentPrd.employeeId.deathDate <= :toDate " +
							" and ((ev.assignmentPrd.fromDate<=sysdate and ev.assignmentPrd.toDate >=sysdate) " +
							" or (ev.assignmentPrd.fromDate in (select max(prd.fromDate) from AssignmentPrd prd where ev.assignmentPrd.employeeId.idPersonalInformation = prd.employeeId.idPersonalInformation " +
							" and not exists (select prd1.id from AssignmentPrd prd1 where prd1.employeeId.idPersonalInformation=ev.assignmentPrd.employeeId.idPersonalInformation " +
							" and ( prd1.employeeId.deathDate >= :fromDate and prd1.employeeId.deathDate <= :toDate and prd1.fromDate<= sysdate and prd1.toDate >= sysdate) )))) " ;
					}
				}
				else if(statusType.getModuletype().equals("Employee") && statusType.getDescription().equals("Suspended"))
				{
					if(status.intValue() != 0)
					{
						mainStr = "from Assignment ev where ev.assignmentPrd.employeeId.StatusMaster.id =:employeeStatus" ;
					}
					if(fromDate != null && toDate!=null)
					{
						mainStr += " and ev.assignmentPrd.employeeId.idPersonalInformation in " +
								"(select D.employeeId.idPersonalInformation from DisciplinaryPunishment D where " +
								" D.dateOfSuspension >= :fromDate and D.dateOfSuspension <= :toDate ) " ;

					}


				}


			Query qry = null;

			qry = getCurrentSession().createQuery(mainStr);
			logger.info("Query----"+qry.toString());

			if(status.intValue() != 0)
			{
				qry.setInteger("employeeStatus", status);
			}
			if(fromDate != null)
			{
				qry.setDate("fromDate", new java.sql.Date (fromDate.getTime()));
			}

			if(toDate != null)
			{
				qry.setDate("toDate", new java.sql.Date (toDate.getTime()));
			}

			employeeList = (List)qry.list();

		} catch (HibernateException he) {
			throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
		} catch (Exception he) {
			throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);

		}
		return employeeList;
	}

	/**
	 * Returns the list of departments assigned to the user. This list will return entries only
	 * in case the user is an HOD
	 * @param userName
	 * @return
	 */
	public  List getListOfDeptBasedOnHodLogin(String userName)
	{
		//session = HibernateUtil.getCurrentSession();
		List deptList= new ArrayList();
		try {
			Query qry =null;
			if(userName!=null)
			{
				qry = getCurrentSession().createQuery("from DepartmentImpl where id in (select hodept.id from EmployeeDepartment where assignment.assignmentPrd.employeeId.userMaster.userName=:userName )");
			}

			if(userName!=null)
			{
				qry.setString("userName", userName);
			}

			if(qry!=null && qry.list()!=null && !qry.list().isEmpty())
			{
				for(Iterator iter = qry.list().iterator();iter.hasNext();)
				{
					DepartmentImpl deptImpl = (DepartmentImpl)iter.next();
					deptList.add(deptImpl);
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}
		return deptList;
	}

	/**
	 * Returns the list of departments that the user is assigned to. This could be through primary
	 * as well as temporary assignments.
	 * @param userName
	 * @return List of DepartmentImpl
	 */
	public  List getListOfDeptBasedOnUserDept(String userName)
	{
		//session = HibernateUtil.getCurrentSession();
		List deptList= new ArrayList();
		try {
			Query qry =null;
			if(userName!=null)
			{
				qry = getCurrentSession().createQuery("from DepartmentImpl where id in (select deptId.id from Assignment where assignmentPrd.employeeId.userMaster.userName=:userName " +
						" and assignmentPrd.fromDate <= sysdate and assignmentPrd.toDate >= sysdate )");
			}

			if(userName!=null)
			{
				qry.setString("userName", userName);
			}

			if(qry!=null && qry.list()!=null && !qry.list().isEmpty())
			{
				for(Iterator iter = qry.list().iterator();iter.hasNext();)
				{
					DepartmentImpl deptImpl = (DepartmentImpl)iter.next();
					deptList.add(deptImpl);
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}
		return deptList;
	}

	public  boolean isFilterByDept()
	  {
	      boolean isFilterByDept = false;
	      String filterByDept = "NO";
	      AppConfigValues configValue = GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("EIS-PAYROLL","FILTERBYDEPT",new Date());
	      if (null != configValue){
	          filterByDept=configValue.getValue();
	          filterByDept = filterByDept.toUpperCase();
	      }
	      //if selfApproval is true then single step approval
	      if("YES".equals(filterByDept))
	      {
	          isFilterByDept=true;
	      }
	      return isFilterByDept;
	  }

	public List<Long> getNomineeMasterForEmp(Integer empId)
	{

		List list = null;
		Query query =null;
		try
		{

			StringBuffer stringbuffer =new StringBuffer(" select ID  from egeis_nominee_master mst  where mst.EMP_ID = :empId");
			query = HibernateUtil.getCurrentSession().createSQLQuery(stringbuffer.toString()).addScalar("ID", LongType.INSTANCE);

			if(empId != null)
			{
				query.setInteger("empId", empId);
			}
		}
		catch (HibernateException hibException) {
				LOGGER.error(hibException.getMessage());
				throw new EGOVRuntimeException("HibernateException:" + hibException.getMessage(),hibException);
			}

			return query.list();

	}
	public void createNomineeMaster(EmployeeNomineeMaster nomineeMaster)
	{
		//session = HibernateUtil.getCurrentSession();
		try
		{

			if(nomineeMaster!=null)
			{
				CommonsService cm = EisManagersUtill.getCommonsService();
				String[] attrName=(cm.getAccountdetailtypeAttributename(null,"Nominee")).split("#");
				Accountdetailkey adk=new Accountdetailkey();
				Query qry= getCurrentSession().createQuery(" from Accountdetailtype where id=:id ");
				qry.setInteger("id", Integer.parseInt(attrName[0]));
				adk.setAccountdetailtype((Accountdetailtype)qry.uniqueResult());
				adk.setGroupid(1);
				adk.setDetailkey(Integer.valueOf(nomineeMaster.getId().toString()));
				adk.setDetailname(attrName[1]);
				//adk.setAccountdetailtype(Integer.parseInt(attrName[0]));
				cm.createAccountdetailkey(adk);
			}
		}
		catch(Exception e)
		{
			LOGGER.error(e);
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}


	}

	/**
	 * Returns the list of Employees who are assigned to the given department and designation
	 * as of current date.
	 * @param deptId - Integer representing id of department
	 * @param desgId - Integer representing id of designation
	 * @return List of EmployeeView
	 */
	public List<EmployeeView> getEmployeeInfoBasedOnDeptAndDesg(Integer deptId ,Integer desgId)
	{
		List<EmployeeView> employeeList = new ArrayList<EmployeeView>();
		Criteria criteria=HibernateUtil.getCurrentSession().createCriteria(EmployeeView.class).
		createAlias("deptId","department").createAlias("desigId", "designation").
		add(Restrictions.eq("department.id", deptId)).add(Restrictions.eq("designation.designationId",desgId))
		.add(Restrictions.and(Restrictions.le("fromDate",new Date()), Restrictions.ge("toDate",new Date())))
		.add(Restrictions.eq("isPrimary", 'Y')).add(Restrictions.eq("isActive",1));
		return criteria.list();


	}

	/**
	 * Returns the list of Employees who are assigned to the given department
	 * as of given date.
	 * @param deptId - Integer representing id of department
	 * @param date -  Given date. If null, current date is assumed.
	 * @return List of EmployeeView
	 */
	public List<EmployeeView> getEmployeeInfoBasedOnDeptAndDate(Integer deptId, Date date)
	{
		if(date == null)
			date = new Date();
		List<EmployeeView> employeeList = new ArrayList<EmployeeView>();
		Criteria criteria=HibernateUtil.getCurrentSession().createCriteria(EmployeeView.class).
		createAlias("deptId","department").add(Restrictions.eq("department.id", deptId))
		.add(Restrictions.eq("isActive",1))
		.add(Restrictions.and(Restrictions.le("fromDate",date), Restrictions.ge("toDate",date)));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	/**
	 * returns true when the employee suspended in the current period
	 *
	 * @return
	 */
	public Boolean isEmployeeSuspended(Integer empId)
	{
		Date currentDate=new Date();
		Criteria criteria=HibernateUtil.getCurrentSession().createCriteria(DisciplinaryPunishment.class);
		criteria.createAlias("employeeId", "emp").
		createAlias("emp.StatusMaster", "empstatus").
		createAlias("status", "status").
		add(Restrictions.eq("whetherSuspended", '1')).
		add(Restrictions.and(
				Restrictions.ilike("status.moduletype", EisConstants.STATUS_MODULE_TYPE_DISC),
				Restrictions.ilike("status.code",EisConstants.STATUS_APPROVED))	).
		add(Restrictions.and(Restrictions.le("dateOfSuspension", currentDate),
				Restrictions.or(Restrictions.ge("dateOfReinstatement", currentDate), Restrictions.isNull("dateOfReinstatement"))
				)).
		add(Restrictions.ilike("empstatus.description", "Employed")).
		add(Restrictions.eq("emp.id", empId));
		return !criteria.list().isEmpty();
	}
	/**
	 * returns all the employees who has active assignment period
	 * @return
	 */
	public List<PersonalInformation> getAllEmployees()
	{
		return HibernateUtil.getCurrentSession().createQuery("" +
				"select distinct	prd.employeeId from EmployeeView empview left join empview.assignmentPrd as prd"
				+" where (sysdate between empview.fromDate  and empview.toDate or " +
						" (empview.toDate is null and empview.fromDate<=sysdate))").list();
	}

	/**
	 * List of users not mapped to any of the employees
	 * @return empUserMapList
	 */
	public List getListOfUsersNotMappedToEmp()
	{
		List empUserMapList  = null;
		try {

			PersonalInformationDAO pimsDao=EisDAOFactory.getDAOFactory().getPersonalInformationDAO();
			empUserMapList=pimsDao.getListOfUsersNotMappedToEmp();

		} catch (Exception e) {

			LOGGER.error(e);
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}
		return empUserMapList;
	}
	
	public DesignationMaster getPresentDesignation(Integer  idPersonalInformation)
	{
		Assignment assignment = getLatestAssignmentForEmployee(idPersonalInformation);
		return assignment.getDesigId();
	}


	public CommonsService getCommonsService() {
		return commonsService;
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	
	
	
}


