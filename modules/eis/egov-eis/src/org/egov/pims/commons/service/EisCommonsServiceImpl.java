/*
 *	@(#)GenericCommonsManagerBean.java		Oct 25, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * eGov PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pims.commons.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.ObjectType;
import org.egov.infstr.utils.EGovConfig;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.ejb.api.UserService;
import org.egov.lib.rjbac.user.ejb.server.UserServiceImpl;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.commons.Position;
import org.egov.pims.commons.PositionHeirarchy;
import org.egov.pims.commons.dao.DesignationMasterDAO;
import org.egov.pims.commons.dao.GenericEisDaoFactory;
import org.egov.pims.commons.dao.PositionMasterDAO;
import org.egov.pims.dao.EisDAOFactory;
import org.egov.pims.model.EmployeeView;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.utils.EisManagersUtill;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.type.IntegerType;
/**
 * @author Venkatesh.M.J
 * @version 1.2
 * @since 1.2
 */
public class EisCommonsServiceImpl implements EisCommonsService {


	private static final Logger logger = Logger.getLogger(EisCommonsServiceImpl.class);
	private Session session;
	private static UserService userService;


	private static UserService getUserService(){
		return new UserServiceImpl();
	}
	public  void addPosition(Position position,DesignationMaster desMaster)
	{
		try
		{
			desMaster.addPosition(position);
		}
		catch(Exception e)
		{
			
			throw new EGOVRuntimeException("Exception in deleting Installment."+e.getMessage(),e);
		}

	}
	public  void updatePosition(Position position)
	{
		try
		{
			PositionMasterDAO positionMasterDAO = new PositionMasterDAO();
			positionMasterDAO.updatePosition(position);
		}
		catch(Exception e)
		{
			
			throw new EGOVRuntimeException("Exception in deleting Installment."+e.getMessage(),e);
		}

	}
	public  Position getPositionById(Integer positionId)

	{
		Position pos = null;
		logger.info("InsidegetPositionById :  positionId="+positionId);
		try
		{
			if(positionId != null)
			{
				PositionMasterDAO positionMasterDAO = new PositionMasterDAO();
				pos = positionMasterDAO.getPosition(positionId);
			}
			return pos;
		}
		catch(Exception e)
		{
			
			throw new EGOVRuntimeException("Exception in deleting Installment."+e.getMessage(),e);
		}

	}
    public   Integer getNumberOfPosts(Integer designationId)
    {
    	//Integer noOfPosts = new Integer(0);
    	DesignationMasterDAO designationMasterDAO = new DesignationMasterDAO();
    	DesignationMaster designationMaster =designationMasterDAO.getDesignationMaster(designationId);
    	Integer noOfPosts = designationMaster.getSanctionedPosts();
    	return noOfPosts;


    }
    public   Integer getNumberOfBalancePosts(Integer designationId)
    {
    	
    	DesignationMasterDAO designationMasterDAO = new DesignationMasterDAO();
    	DesignationMaster designationMaster =designationMasterDAO.getDesignationMaster(designationId);
    	Integer noOfPosts = designationMaster.getSanctionedPosts();
    	Integer noOfOutPosts = designationMaster.getOutsourcedPosts();
    	return noOfPosts-noOfOutPosts;

    }

    public  Position getPositionByUserId(Integer userId)
    {

		Position userPosition = null;
		session = HibernateUtil.getCurrentSession();
		//PersonalInformation personalInformation = new PersonalInformation();
		Date currentDate = new Date();
		try
		{

			String mainStr = "";
			mainStr = " select POS_ID from EG_EIS_EMPLOYEEINFO ev where ev.USER_ID = :userid and ((ev.to_Date is null and ev.from_Date <= :thisDate ) " +
					" OR (ev.from_Date <= :thisDate AND ev.to_Date >= :thisDate)) and ev.IS_PRIMARY ='Y'";
			Query qry = session.createSQLQuery(mainStr).addScalar("POS_ID", IntegerType.INSTANCE);
			qry.setInteger ("userid", userId);
			qry.setDate("thisDate", currentDate);
			List retList = qry.list();
			if(retList!=null && !retList.isEmpty())
			{
				Integer posId = null;
				for(Iterator iter = retList.iterator();iter.hasNext();)
				{
					posId = (Integer)iter.next();
				}
				if (posId != null)
				{
					userPosition = getPositionById(posId);
				}
			}
		}
		catch (HibernateException he) {
				
				throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(),he);
			} catch (Exception he)
			{
				throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(),he);
			}
			return userPosition;
 
    }

    public  PositionHeirarchy createPositionHeirarchy(PositionHeirarchy positionHeirarchy)
    {
    	PositionHeirarchy posHeirarchy = null;
    	try
		{
    		posHeirarchy = (PositionHeirarchy)GenericEisDaoFactory.getDAOFactory().getPositionHeirarchyDAO().create(positionHeirarchy);
		}
		catch(Exception e)
		{
			
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
    	return posHeirarchy;
    }
    public  void updatePositionHeirarchy(PositionHeirarchy positionHeirarchy)
    {
    	try
		{
    		GenericEisDaoFactory.getDAOFactory().getPositionHeirarchyDAO().update(positionHeirarchy);
		}
		catch(Exception e)
		{
			
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
    }
    public  PositionHeirarchy getPositionHeirarchyById(Integer posHeiId)
    {
    	PositionHeirarchy posHeirarchy = null;
    	try
		{
    		posHeirarchy = (PositionHeirarchy)GenericEisDaoFactory.getDAOFactory().getPositionHeirarchyDAO().findById(posHeiId, false);
		}
		catch(Exception e)
		{
			
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
    	return posHeirarchy;
    }

    public Set getSetOfPosForDesignationId(Integer desigId)
    {
    	Set set = null;
    	DesignationMasterDAO designationMasterDAO = new DesignationMasterDAO();
    	DesignationMaster designationMaster = designationMasterDAO.getDesignationMaster(desigId);
    	set =designationMaster.getPositionSet();
    	return set;
    }

    public  Set getSetOfPositionHeirarchyForObjTypeId(Integer objTypeId)
    {
    	Set set = null;
    	try
		{
    		set = GenericEisDaoFactory.getDAOFactory().getPositionHeirarchyDAO().getSetOfPosHeirForObjType(objTypeId);
		}
		catch(Exception e)
		{
			
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
    	return set;
    }

    public  Position getSuperiourPositionByPosition(Position position,ObjectType obType)
    {
    	Position pos = null;
    	try
		{
    		pos = GenericEisDaoFactory.getDAOFactory().getPositionHeirarchyDAO().getHigherPos(position,obType);
		}
		catch(Exception e)
		{
			
			throw new EGOVRuntimeException("Exception in getSuperiourPositionByPosition :"+e.getMessage(),e);
		}
    	return pos;
    }

    public  User getSupUserforPositionandObjectType(Position pos,ObjectType objType)
	{
    	User uerImpl= null;
    	if(pos != null)
    	{
    		uerImpl= getSuperiorForGivenPosition(pos,objType);
    	}
		return uerImpl;
	}

    public  User getSupUserforUserandObjectType(User user,ObjectType objType)
	{
    	User uerImpl = null;
    	Position userPos = getPositionForUserByIdAndDate(user.getId(), new Date());
    	uerImpl= getSuperiorForGivenPosition(userPos,objType);
		return uerImpl;
	}
	private  User getSuperiorForGivenPosition(Position pos,ObjectType objectType)
	{
		User uerImpl= null;
		Position p = pos;
		Position higherpos  = getSuperiourPositionByPosition(p,objectType);
		if(higherpos!=null)
		{
			uerImpl =getUserforPosition(higherpos);
		}
		return uerImpl;
	}

	public Position getPositionForUserByIdAndDate(Integer userId, Date assignDate)
	{
		Position userPosition = null;
		session = HibernateUtil.getCurrentSession();
		//PersonalInformation personalInformation = new PersonalInformation();
		try
		{

			String mainStr = "";
			mainStr = " select POS_ID from EG_EIS_EMPLOYEEINFO ev where ev.USER_ID = :userid and ((ev.to_Date is null and ev.from_Date <= :thisDate ) OR (ev.from_Date <= :thisDate AND ev.to_Date > :thisDate))";
			Query qry = session.createSQLQuery(mainStr).addScalar("POS_ID", IntegerType.INSTANCE);
			qry.setInteger ("userid", userId);
			qry.setDate("thisDate", assignDate);
			List retList = qry.list();
			if(retList!=null && !retList.isEmpty())
			{
				Integer posId = null;
				for(Iterator iter = retList.iterator();iter.hasNext();)
				{
					posId = (Integer)iter.next();
				}
				if (posId != null)
				{
					userPosition = getPositionById(posId);
				}
			}
		}
		catch (HibernateException he) {
				throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(),he);
			} catch (Exception he)
			{
				throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(),he);
			}
			return userPosition;

	}

	public User getUserforPosition(Position pos)
	{
		User uerImpl= null;
		session = HibernateUtil.getCurrentSession();
		
		//PersonalInformation personalInformation = new PersonalInformation();
		try
		{
			
		    String mainStr = "";
			mainStr = " select 	USER_ID  from EG_EIS_EMPLOYEEINFO ev  where ev.POS_ID = :pos and ((ev.to_Date is null and ev.from_Date <= SYSDATE ) OR (ev.from_Date <= SYSDATE AND ev.to_Date > SYSDATE))";
			Query qry = session.createSQLQuery(mainStr).addScalar("USER_ID", IntegerType.INSTANCE);

			if(pos != null)
			{
				qry.setEntity("pos", pos);
			}
			if(qry.list()!=null&&!qry.list().isEmpty())
			{
				for(Iterator iter = qry.list().iterator();iter.hasNext();)
				{
					Integer userId = (Integer)iter.next();
					uerImpl = getUserService().getUserByID(userId);
				}
			}
		}
		catch (HibernateException he) {
				throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(),he);
			} catch (Exception he)
			{
				throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(),he);
			}
			return uerImpl;

	}
	
	/**
	 * Api to check for unique constraint
	 */
	public boolean checkPositionHeirarchy(Integer positionFrom,Integer positionTo,Integer objectType,String positionHirId)
	{
		session = HibernateUtil.getCurrentSession();
		boolean b=false;
		Query qry = null;
		//PersonalInformation personalInformation = new PersonalInformation();
		try
		{
				String main="from PositionHeirarchy pos where  pos.objectType.id=:objectType";
						
				
				if(positionFrom!=null)
				{
					main+=" and pos.posFrom.id=:positionFrom";
				}
				if(positionTo!=null)
				{
					main+=" and pos.posTo.id=:positionTo";
				}
				if(positionHirId!=null)
				{
					main+=" and pos.id <>:positionHirId";
				}
				qry=session.createQuery(main);
				
				if(positionFrom!=null)
				{
				qry.setInteger("positionFrom",positionFrom);
				}
				if(positionTo!=null)
				{
					qry.setInteger("positionTo", positionTo);
				}
				if(positionHirId!=null)
				{
					qry.setInteger("positionHirId", Integer.valueOf(positionHirId));
				}
		        qry.setInteger("objectType", objectType);
		        if(qry.list()!=null && !qry.list().isEmpty())
		        {
		        	b=true;
		        }
		        
			
		}
		catch (HibernateException he) {
				throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(),he);
			} catch (Exception he)
			{
				throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(),he);
			}
			
			return b;
		
	}
	/**
	 * Api To Search Based on Postions and Object Type
	 */
	public Set getSetOfPositionByPositionsAndObjectType(Integer positionFrom,Integer positionTo,Integer objectType)
	{
		session = HibernateUtil.getCurrentSession();
		Set<PositionHeirarchy> set = new HashSet<PositionHeirarchy>();
		Query qry = null;
		//PersonalInformation personalInformation = new PersonalInformation();
		try
		{
				String main="from PositionHeirarchy pos where  pos.objectType.id=:objectType ";
				if(positionFrom!=null)
				{
					main+=" and pos.posFrom.id=:positionFrom";
				}
				if(positionTo!=null)
				{
					main+=" and pos.posTo.id=:positionTo";
				}
				main=main+" order by pos.posFrom.name";
				qry=session.createQuery(main);
				
				if(positionFrom!=null)
				{
				qry.setInteger("positionFrom",positionFrom);
				}
				if(positionTo!=null)
				{
					qry.setInteger("positionTo", positionTo);
				}
				
		        qry.setInteger("objectType", objectType);
		        
		        if(qry.list()!=null&&!qry.list().isEmpty())
				{
					Collection c = (Collection)qry.list();
					set.addAll(c);
				}
			
		}
		catch (HibernateException he)
			{
				throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(),he);
			} catch (Exception he)
			{
				throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(),he);
			}
			
		return set;
	}
	public Boolean isEmployeeAutoGenerateCodeYesOrNo()
	{
		String employeeAutoGenCodeYesOrNo=EGovConfig.getAppConfigValue("Employee","EMPAUTOGENERATECODE","no");
		boolean autoGenCode = false;
		if("yes".equalsIgnoreCase(employeeAutoGenCodeYesOrNo))
		{
			autoGenCode=true;
		}
		
		return autoGenCode;
	}
	 public Boolean checkEmpCode(String empCode)
	 {
		 boolean checkEmpCode = false;
		 Query qry = null;
		 session = HibernateUtil.getCurrentSession();
		 try
		 {
			String main="from PersonalInformation where employeeCode=:employeeCode";
			qry=session.createQuery(main);
			qry.setString("employeeCode", empCode);
			if(qry.list()!=null && !qry.list().isEmpty())
			{
				checkEmpCode = true;
			}
		 }
		 catch (HibernateException he) {
				
				throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(),he);
			} catch (Exception he)
			{
				
				throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(),he);
			}
		 return checkEmpCode;
	 }
	 
	 public  Position getPositionByName(String positionName){
		 
		    session = HibernateUtil.getCurrentSession();
			Query qry = null;
			try
			 {
				String main= "from Position where name=:positionName";
				qry=session.createQuery(main);
				if(positionName!=null && !positionName.equals(""))
				{
					qry.setString("positionName", positionName);
				}
				
				return (Position)qry.uniqueResult();
				
			 }
			 catch (HibernateException he) {
					throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(),he);
				} catch (Exception he)
				{
					throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(),he);
				}
				
		 }
		
	 /**
		 * This method returns the current position id  of the user
		 * 
		 * @param user the user whose designation is needed.
		 * 
		 * 
		 * @return the position id as integer 
		 */
	public Position getCurrentPositionByUser(User user)
	{
		Position position = null;
		try{
		if (null != user){
			PersonalInformation personalInfo = EisManagersUtill.getEmployeeService().getEmpForUserId(user.getId());
			position = EisManagersUtill.getEmployeeService().getPositionforEmp(personalInfo.getIdPersonalInformation());
			
		}
		}catch(Exception e)
		{
			
			throw new EGOVRuntimeException("Exception in getCurrentPositionByUser :"+e.getMessage(),e);
		}
		return position;
	}
	/**
	 * This method returns the position superior to what the user is in, 
	 * This superior position is for the object type specified. The object type is accepted as a string
	 * and the superior position returned
	 * 
	 * @param position the current position.
	 * @param obType The object Type name for which superior approval position is needed
	 * 
	 * @return the position of type Position 
	 */
	public  Position getSuperiorPositionByObjType(Position position,String obType){
		Position nextPos = null;
		logger.info("In getSuperiorPositionByObjType ");
		try{
			ObjectType objType = EisManagersUtill.getCommonsService().getObjectTypeByType(obType);
			
			if (null != objType){
				nextPos = this.getSuperiourPositionByPosition(position, objType);
				
			}
		}catch(Exception e)
		{
			
			throw new EGOVRuntimeException("Exception in getSuperiourPositionByObjType :"+e.getMessage(),e);
		}
		return nextPos;
	}
	
	/*public Department getDepartmentForUser(User user){
		Department dept = null;
		if (null != user){
			PersonalInformation emp = EisManagersUtill.getEisManager().getEmpForUserId(user.getId());
			if (null != emp){
				dept = emp.getEgdeptMstr();
			}
		}
		return dept;
	}*/
	
	
	/*public Integer getDesignationForPosition(Position position){
		Integer designation = null;
		if (null != position){
			designation = position.getDesigId().getDesignationId();
			return designation;
		}
		return designation;
	}*/
	/**
	 * This method returns the inferior position for given position
	 * @param position
	 * @param obType
	 * @return
	 */
	public  Position getInferiorPositionByObjType(Position position, String obType)
    {
    	Position pos = null;
    	try
		{
    		ObjectType objType = EisManagersUtill.getCommonsService().getObjectTypeByType(obType);
    		pos = GenericEisDaoFactory.getDAOFactory().getPositionHeirarchyDAO().getLowerPos(position,objType);
		}
		catch(Exception e)
		{
			
			throw new EGOVRuntimeException("Exception in getInferiorPositionByPosition :"+e.getMessage(),e);
		}
    	return pos;
    }
	
	public  Position getInferiorPositionByPosition(Position position,ObjectType obType)
    {
    	Position pos = null;
    	try
		{
    		pos = GenericEisDaoFactory.getDAOFactory().getPositionHeirarchyDAO().getLowerPos(position,obType);
		}
		catch(Exception e)
		{
			
			throw new EGOVRuntimeException("Exception in getInferiorPositionByPosition :"+e.getMessage(),e);
		}
    	return pos;
    }
	public  User getUserForPosition(Integer posId, Date date)
	{
		User user = null;
		session = HibernateUtil.getCurrentSession();
		try
		{
			
			
			String mainStr = "";
			mainStr = " select USER_ID from EG_EIS_EMPLOYEEINFO ev where ev.pos_id = :posId and ((ev.to_Date is null and ev.from_Date <= :thisDate ) OR (ev.from_Date <= :thisDate AND ev.to_Date > :thisDate))";
			Query qry = session.createSQLQuery(mainStr).addScalar("USER_ID", IntegerType.INSTANCE);
			qry.setInteger ("posId", posId);
			qry.setDate("thisDate", date);
			List retList = qry.list();
			if(retList!=null && !retList.isEmpty())
			{
				Integer userId = null;
				for(Iterator iter = retList.iterator();iter.hasNext();)
				{
					userId = (Integer)iter.next();
				}
				if (userId != null)
				{
					user = getUserService().getUserByID(userId);
				}
			}
		}
		catch (HibernateException he) {
				throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(),he);
			} catch (Exception he)
			{
				throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(),he);
			}
			return user;
		
	}
	/**
	 * Api to get unique designation based on dept and functionary
	 * @param deptId
	 * @param functionaryId
	 * @return unique designation from view if dept and functionary is 0
	 * else based on dept and functionary
	 * @throws Exception
	 */
	 public List<DesignationMaster> getDesigantionBasedOnFuncDept(Integer deptId,Integer functionaryId) throws Exception
		{
		 	
			session = HibernateUtil.getCurrentSession();
			List<EmployeeView> employeeList = null;
			List<DesignationMaster> desgMstr = new ArrayList<DesignationMaster>();
			try
			{				
				String mainStr = "";
				String subQry = " from EmployeeView ev ";
				if( ((deptId!=null && deptId!=0) && functionaryId==0 ))
				{
					subQry+=" where ev.deptId = :deptId ";
				}
				else if(( (functionaryId!=null && functionaryId!=0) && deptId==0))
				{
					subQry+= " where ev.functionary =:functionaryId";
				}
				else if(deptId!=null && deptId!=0 && functionaryId!=null && functionaryId!=0 )
				{
					subQry+= " where ev.deptId = :deptId and ev.functionary =:functionaryId";
				}
				
				subQry=	"select distinct ev.desigId.designationId "+subQry;
				mainStr ="from DesignationMaster dm   where dm.designationId in( "+subQry+"  ) "; 
					
				Query query = session.createQuery(mainStr);
				if(deptId!=null && deptId!=0)
				{
					query.setInteger("deptId", deptId);
				}
				
				if(functionaryId!=null && functionaryId!=0)
				{
					query.setInteger("functionaryId", functionaryId);
				}
				
				desgMstr=(List<DesignationMaster>)query.list();
				
			}
			catch(Exception e){
				
				throw new EGOVRuntimeException(e.getMessage(),e);
			}
			return desgMstr;
			
		}
	 
	 
	 
	/**
	  * Returning temporary  assigned employee object by department,designation,functionary,date 
	  * @param deptId
	  * @param DesigId
	  * @param functionaryId
	  * @param onDate
	  * @return Employee
	  * @throws Exception 
	  */
	 public PersonalInformation getTempAssignedEmployeeByDeptDesigFunctionaryDate(Integer deptId, Integer desigId, Integer functionaryId, Date onDate) throws Exception{
		 return EisDAOFactory.getDAOFactory().getPersonalInformationDAO().getTempAssignedEmployeeByDeptDesigFunctionaryDate(deptId, desigId, functionaryId, onDate);
	 }
	 
	 private final static String STR_EXCEPTION="Exception:";
	
}
