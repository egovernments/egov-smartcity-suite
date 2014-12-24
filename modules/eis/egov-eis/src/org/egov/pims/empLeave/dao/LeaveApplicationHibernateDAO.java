package org.egov.pims.empLeave.dao;

import java.util.Date;
import java.util.List;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.pims.empLeave.model.LeaveApplication;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.model.StatusMaster;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 */
public class LeaveApplicationHibernateDAO extends GenericHibernateDAO implements LeaveApplicationDAO
{
   
    /*
     *
	 * @param persistentClass
	 * @param session
	 */
	public LeaveApplicationHibernateDAO(Class persistentClass, Session session)
	{
		super(persistentClass, session);
	}
	public LeaveApplication getLeaveApplicationByID(Integer id)
	{
		Query qry = getSession().createQuery("from LeaveApplication B where B.id =:id ");
		qry.setInteger("id", id);
		return (LeaveApplication)qry.uniqueResult();
	}
	public LeaveApplication getLeaveApplicationBySancNo(String sanctionNo)
	{
		Query qry = getSession().createQuery("from LeaveApplication B where B.sanctionNo =:sanctionNo ");
		qry.setString("sanctionNo", sanctionNo);
		return (LeaveApplication)qry.uniqueResult();
	}
	public LeaveApplication getLeaveApplicationByEmpAndDate(Date date,PersonalInformation employee)
	{
		
		Query qry = getSession().createQuery("from LeaveApplication B where B.toDate >=:date and B.fromDate <=:date  and B.employeeId =:employee AND B.statusId.name=:statusName ");
		qry.setDate("date", new java.sql.Date(date.getTime()));
		qry.setDate("date", new java.sql.Date(date.getTime()));
		qry.setEntity("employee", employee);
		qry.setString(STR_STATUS_NAME, org.egov.pims.utils.EisConstants.STATUS_APPROVED);
		return (LeaveApplication)qry.uniqueResult();
	}
	/**
	 * returns true if the given employeeId has leave irrespective of leave status
	 */
	public boolean checkLeaveReportsForAnEmployee(Integer employeeId)
	{
		Query qry = getSession().createQuery("from LeaveApplication B where B.employeeId.idPersonalInformation =:employee ");
		qry.setInteger("employee", employeeId);
		return !qry.list().isEmpty();
	}
	
	public boolean checkLeaveForAnEmployee(Integer employee)
	{
		boolean b = false;
		List leaveApplicationList = null;
		Query qry = getSession().createQuery("from LeaveApplication B where B.employeeId.idPersonalInformation =:employee AND B.statusId.name=:statusName ");
		qry.setInteger("employee", employee);
		qry.setString(STR_STATUS_NAME, org.egov.pims.utils.EisConstants.STATUS_APPLIED);
		leaveApplicationList = qry.list();
		if(leaveApplicationList!=null && !leaveApplicationList.isEmpty())
		{
			b = true;
		}
		return b;
	}
	
	public List<LeaveApplication> getLeaveApplicationByEmpStatus(Integer empId, StatusMaster status)throws Exception{
		try{
			List<LeaveApplication> list = null;
			Query qry = getSession().createQuery("from LeaveApplication L where " +
					"L.employeeId.idPersonalInformation = :empId AND L.statusId = :status");
			qry.setEntity("status", status);
			qry.setInteger("empId", empId);
			list = qry.list();
			return list;
		}catch(Exception e){
			
			throw e;
		}		
	}

	
	public List<LeaveApplication> getEncashmentLeaveApplicationByStatus(String statusName)throws Exception{		
		try{
			List<LeaveApplication> list = null;
			Query qry = getSession().createQuery("from LeaveApplication L where " +
					"L.statusId.name = :statusName and " +
					"L.reason = 'Encashment'");
			qry.setString(STR_STATUS_NAME, statusName);			
			list = qry.list();
			return list;
		}catch(Exception e){
			
			throw e;
		}
	}
	private final static String STR_STATUS_NAME="statusName";
}



