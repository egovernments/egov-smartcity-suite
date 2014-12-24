package org.egov.pims.empLeave.dao;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.CFinancialYear;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.pims.empLeave.model.Attendence;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.EmployeeView;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.utils.EisConstants;
import org.egov.pims.utils.EisManagersUtill;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 */
public class AttendenceHibernateDAO extends GenericHibernateDAO implements AttendenceDAO
{
    private final static  Logger LOGGER = Logger.getLogger(AttendenceHibernateDAO.class.getClass());
    /*
     *
	 * @param persistentClass
	 * @param session
	 */
	public AttendenceHibernateDAO(Class persistentClass, Session session)
	{
		super(persistentClass, session);
	}
	public Attendence getAttendenceByID(Integer id)
	{
		Query qry = getSession().createQuery("from Attendence B where B.id =:id ");
		qry.setInteger("id", id);
		return (Attendence)qry.uniqueResult();
	}
	public List getListOfPresentDaysForEmployeebetweenDates(java.util.Date fromDate,java.util.Date toDate,PersonalInformation personalInformation)
	{
		Query qry = getSession().createQuery("from Attendence B where B.attDate >=:fromDate and   B.attDate <=:toDate   and B.employee = :personalInformation and B.attendenceType.name = :Present");
		qry.setDate(STR_FROMDATE, new java.sql.Date(fromDate.getTime()));
		qry.setDate(STR_TODATE,  new java.sql.Date(toDate.getTime()));
		qry.setEntity(STR_PERSONALINFO, personalInformation);
		qry.setString("Present", EisConstants.PRESENT);
		return qry.list();
		
	}
	
	public List getListOfAbsentDaysForEmployeebetweenDates(java.util.Date fromDate,java.util.Date toDate,PersonalInformation personalInformation,CFinancialYear cFinancialYear)
	{
		List attList = new ArrayList();
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
			//logger.info("inhere1"+EisManagersUtill.getEmpLeaveManager().getNoOfWorkingDaysBweenTwoDatesByFinYear(fromDate,toDate,cFinancialYear));
			for(Iterator iter = EisManagersUtill.getEmpLeaveService().getNoOfWorkingDaysBweenTwoDatesForEmployee(fromDate, toDate, personalInformation).iterator();iter.hasNext();)
			{
				String wd = (String)iter.next();				
				Attendence attendence = checkAttendenceByEmpAndDte(personalInformation.getIdPersonalInformation(),sdf.parse(wd));
				if(attendence==null)
				{					
					Attendence attendenceNull = new Attendence();
					attList.add(attendenceNull);
				}
				else
				{
					if(attendence.attendenceType.getName().equals(org.egov.pims.utils.EisConstants.ABSENT))
					{						
						attList.add(attendence);
					}
				}
				
				
			}
		}catch(Exception e)
		{
			
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		
		}
			
		return attList;
		
	}
	public List getListOfCompOffDaysForEmployeebetweenDates(java.util.Date fromDate,java.util.Date toDate,PersonalInformation personalInformation)
	{
		Query qry = getSession().createQuery("from Attendence B where B.attDate >=:fromDate and   B.attDate <=:toDate  and B.employee = :personalInformation and B.attendenceType.name = :compOff");
		qry.setDate(STR_FROMDATE, new java.sql.Date(fromDate.getTime()));
		qry.setDate(STR_TODATE,  new java.sql.Date(toDate.getTime()));
		
		qry.setEntity(STR_PERSONALINFO, personalInformation);
		qry.setString("compOff", EisConstants.COMP_OFF);
		return qry.list();
		
	}
	
	public Map getMapOfAttendenceByMonth(Integer month,CFinancialYear cFinancialYear) 
	{
		Map map = new HashMap();
		Query qry = getSession().createQuery("from Attendence B where B.month =:month and B.financialId = :financialId  ");
		qry.setInteger("month", month);
		qry.setEntity("financialId", cFinancialYear);
		if(!qry.list().isEmpty())
		{
			map  = getMapForList(qry.list());
		}
		return map;
		
	}
	
	private Map getMapForList(List list)
	{
		Map map = new HashMap();
		Map dayVsObj = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
		for(Iterator iter =list.iterator();iter.hasNext();)
		{
			Attendence attendence = (Attendence)iter.next();
			if(map.containsKey(attendence.getEmployee().getIdPersonalInformation()))
			{
				dayVsObj = (Map)map.get(attendence.getEmployee().getIdPersonalInformation());
				dayVsObj.put(Integer.valueOf(sdf.format(attendence.getAttDate()).substring(0,2).trim()),attendence);
			}
			else
			{
				
				dayVsObj = new HashMap();
				dayVsObj.put(Integer.valueOf(sdf.format(attendence.getAttDate()).substring(0,2).trim()),attendence);
				map.put(attendence.getEmployee().getIdPersonalInformation(),dayVsObj);
			}
			
		}
		return map;
	}
	public Attendence checkAttendenceByEmpAndDte(Integer id,java.util.Date date)
	{
		Attendence attendence = null;
		Query qry = getSession().createQuery("from Attendence B where B.employee.idPersonalInformation =:id and B.attDate =:date   ");
		qry.setInteger("id", id);
		qry.setDate("date", new java.sql.Date(date.getTime()));
		if(qry.uniqueResult()!=null)
		{
			attendence = (Attendence)qry.uniqueResult();
		}	
			return attendence;
	}
	
	public List getListOfHalfPresentDaysForEmployeebetweenDates(java.util.Date fromDate,java.util.Date toDate,PersonalInformation personalInformation)
	{
		Query qry = getSession().createQuery("from Attendence B where B.attDate >=:fromDate and   B.attDate <=:toDate   and B.employee = :personalInformation and B.attendenceType.name = :HalfPresent");
		qry.setDate(STR_FROMDATE, new java.sql.Date(fromDate.getTime()));
		qry.setDate(STR_TODATE,  new java.sql.Date(toDate.getTime()));
		qry.setEntity(STR_PERSONALINFO, personalInformation);
		qry.setString("HalfPresent", EisConstants.HALFPRESENT);
		return qry.list();
		
	}
	
	public Map getMapOfPaidAndUnpaidDaysForEmployeebetweenDates(java.util.Date fromDate,java.util.Date toDate,PersonalInformation personalInformation)
	{
		Map retMap = null;
		LOGGER.info("<<<<<<<<<<<<<< Inside getMapOfPaidAndUnpaidLeavesForEmployeebetweenDates :");
		LOGGER.info("<<<<<<<<<<<<<<111 fromDate = "+fromDate+", toDate = "+toDate+", empcode = "+personalInformation.getEmployeeCode() + ", empName = "+personalInformation.getEmployeeName());
		/*
		 *  Modified the query to exclude the presents which are marked on Holidays. 
		 *  The attendance Id which is marked as present will have an entry in compOff table, if compoff provided is true.
		 */
		//Query qry = getSession().createSQLQuery("SELECT ATT_TYPE.TYPE_VALUE, COUNT(*) FROM EGEIS_ATTENDENCE ATT, EGEIS_ATT_TYPE ATT_TYPE where ATT.TYPE_ID = ATT_TYPE.ID AND ATT.ATT_DATE >= :fromDate and   ATT.ATT_DATE <= :toDate and ATT.EMP_ID=:id  GROUP BY ATT_TYPE.TYPE_VALUE ");		
		Query qry = getSession().createSQLQuery("SELECT ATT_TYPE.TYPE_VALUE, COUNT(*) FROM EGEIS_ATTENDENCE ATT, EGEIS_ATT_TYPE ATT_TYPE where ATT.TYPE_ID = ATT_TYPE.ID "+ 
				" AND ATT.ID NOT IN (SELECT COMPOFF.ATT_ID FROM EGEIS_COMPOFF COMPOFF, EGEIS_ATTENDENCE ATT WHERE "+
				" COMPOFF.ATT_ID = ATT.ID AND ATT.EMP_ID = :id) AND ATT.ATT_DATE >= :fromDate and ATT.ATT_DATE <= :toDate "+
				" and ATT.EMP_ID=:id  GROUP BY ATT_TYPE.TYPE_VALUE ");
		qry.setDate(STR_FROMDATE, new java.sql.Date(fromDate.getTime()));
		qry.setDate(STR_TODATE, new java.sql.Date(toDate.getTime()));
		qry.setInteger("id", personalInformation.getIdPersonalInformation());		
		List list = qry.list();	
		if(list != null && !list.isEmpty())
		{
			retMap = new HashMap();
			for(Iterator itr=list.iterator(); itr.hasNext();)
			{
				Object [] objArray = (Object [])itr.next();
				if(objArray != null)
				{					
					String typename = (String)objArray[0];
					LOGGER.info("<<<<<<<<<<<<<< typename ="+typename);
					Integer leaveCount = null;
					if(objArray[1] != null)
					{
						leaveCount = Integer.valueOf(((BigDecimal)(objArray[1])).intValue());
					}
					LOGGER.info("<<<<<<<<<<<<<< leaveCount ="+leaveCount);
					retMap.put(typename,leaveCount);															
				}
			}
			
		}
		LOGGER.info("<<<<<<<<<<<<<< retMap ="+retMap);
		return retMap;		
	}
	public List getListOfOverTimeForAnEmployeeBetweenDates(java.util.Date fromDate,java.util.Date toDate,PersonalInformation personalInformation)
	{
		Query qry = getSession().createQuery("from Attendence B where B.attDate >=:fromDate and   B.attDate <=:toDate   and B.employee = :personalInformation and B.attendenceType.name = :overTime");
		qry.setDate(STR_FROMDATE, new java.sql.Date(fromDate.getTime()));
		qry.setDate(STR_TODATE,  new java.sql.Date(toDate.getTime()));
		qry.setEntity(STR_PERSONALINFO, personalInformation);
		qry.setString("overTime", EisConstants.OVER_TIME);
		return qry.list();
	}
	
	/**
	 * Getting all attendence list for an employee in between fromDate and toDate
	 */
	public List<Attendence> getAttendenceListInDateRangeForEmployee(Date fromDate, Date toDate, PersonalInformation employee){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
		try{
			fromDate = sdf.parse(sdf.format(fromDate));
			toDate = sdf.parse(sdf.format(toDate));
		}catch (Exception e) {
			throw new EGOVRuntimeException("Some problem in date convertion");
		}
		Criteria criteria = getSession().createCriteria(Attendence.class,"att")
		.add(Restrictions.eq("att.employee", employee))    	
    	.add(Restrictions.between("att.attDate",fromDate,toDate))
    	.addOrder(Order.asc("att.attDate"));    	
    	
		//ProjectionList projections = Projections.projectionList();
    	//criteria.setProjection(projections);    	
    	return (List<Attendence>)criteria.list();
	}
	
	/**
	 * Getting all haliday attendence list for an employee in between fromDate and toDate
	 */
	public List<Attendence> getHolidayAttendenceListInDateRangeForEmployee(Date fromDate, Date toDate, PersonalInformation employee){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
		try{
			fromDate = sdf.parse(sdf.format(fromDate));
			toDate = sdf.parse(sdf.format(toDate));
		}catch (Exception e) {
			throw new EGOVRuntimeException("Some problem in date convertion");
		}
		Criteria criteria = getSession().createCriteria(Attendence.class,"att").createAlias("attendenceType", "attType")
		.add(Restrictions.eq("att.employee", employee))    	
    	.add(Restrictions.between("att.attDate",fromDate,toDate))
    	.add(Restrictions.or(Restrictions.eq("attType.name", EisConstants.HOLIDAY),Restrictions.eq("attType.name", EisConstants.COMPOFF_ELIG)))
    	.addOrder(Order.asc("att.attDate"));    	
    	
		LOGGER.info("getHolidayAttendenceListInDateRangeForEmployee qry-----"+criteria.getAlias());
		//ProjectionList projections = Projections.projectionList();
    	//criteria.setProjection(projections);    	
    	return (List<Attendence>)criteria.list();
	}
	
	
	
	private final static String STR_FROMDATE="fromDate";
	private final static String STR_TODATE="toDate";
	private final static String STR_PERSONALINFO="personalInformation";
}



