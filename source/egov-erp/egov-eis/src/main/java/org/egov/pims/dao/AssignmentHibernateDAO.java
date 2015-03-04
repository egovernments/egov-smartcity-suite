/*
 * PropertyHibernateDAO.java Created on Oct 5, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.pims.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.EmployeeView;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;


/**
 * This Class implememets the DeptTestsDAO for the Hibernate specific
 * Implementation
 *
 * @author Neetu
 * @version 2.00
 */
public class AssignmentHibernateDAO extends GenericHibernateDAO implements AssignmentDAO
{
    private final static Logger LOGGER = Logger.getLogger(AssignmentHibernateDAO.class.getClass());
    
    @PersistenceContext
	private EntityManager entityManager;
    
    @Override
	public Session  getCurrentSession() {
		return entityManager.unwrap(Session.class);
	}
    /*
     *
	 * @param persistentClass
	 * @param session
	 */
	public AssignmentHibernateDAO(Class persistentClass, SessionFactory session)
	{
		super(persistentClass, session.getCurrentSession());
	}
	public Assignment getAssignmentById(Integer id)
	{
		return (Assignment)getCurrentSession().get(Assignment.class, id);
	}
	public List getListOfEmployeeWithoutAssignment(Date fromdate)
	{
		List employeeAssignList = null;
		try
		{
		StringBuffer query=new StringBuffer(" select distinct ev.id from EmployeeView ev where ev.isActive=1 and  " +
		"(ev.fromDate > :fromdate OR ev.toDate < :fromdate) AND (ev.dateOfFirstAppointment <= :fromdate) and " + "(ev.id not in (select ev.id from  ev where (ev.fromDate <= :fromdate and ev.toDate >=:fromdate)))");
 
			Query qry = getCurrentSession().createQuery(query.toString());
			
			// qry.setString("date", formatter.format(todate.getTime()));
			qry.setDate("fromdate",fromdate);
			employeeAssignList = qry.list();
			
	}catch (HibernateException he) {
		LOGGER.error(he.getMessage());
		throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(),he);
	} catch (Exception he)
	{
		LOGGER.error(he.getMessage());
		throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(),he);
	}
			return employeeAssignList;
}
	public Assignment getLatestAssignmentForEmployeeByToDate(Integer empId,Date todate) throws Exception
	{
		Assignment assignment = null;
		try
		{
			StringBuffer query=new StringBuffer(" select  ev.assignment from EmployeeView ev where ev.assignment.isPrimary = 'Y' and " +
											"ev.id = :empid and ev.fromDate <= :todate and rownum=1 order by ev.toDate desc ");
			Query qry = getCurrentSession().createQuery(query.toString());
			
			// qry.setString("date", formatter.format(todate.getTime()));
			qry.setDate("todate",todate );
			qry.setInteger("empid",empId );		
			
			assignment = (Assignment)qry.uniqueResult();
			
		}catch (HibernateException he)
		{
			LOGGER.error(he.getMessage());
			throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(),he);
		} catch (Exception he) 
		{
			LOGGER.error(he.getMessage());
			throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(),he);
		}
		return assignment;
	}
	
	/**
	 * Api to get Employee Report with Temporary Assignemt
	 * @param Date
	 * @param Position Id
	 * @return employeeView
	 */
	
	public  List<EmployeeView> getEmployeeWithTempAssignment(Date givenDate,Integer posId) 
	{

		List<EmployeeView> employeeAssignList = null;
		try
		{
			StringBuffer query = null;
		
			query =new StringBuffer("from EmployeeView ev where ev.isActive=1 and ev.assignment.isPrimary='N'");
		
		
		if(givenDate!=null)
		{
			query.append(" and ev.fromDate <= :givenDate and ev.toDate >=:givenDate");
		}
		if(posId!=null && posId!=0)
		{
			query.append(" and ev.position.id =:posId ");
		}
 
			Query qry = getCurrentSession().createQuery(query.toString());
			if(givenDate!=null)
			{
				qry.setDate("givenDate",givenDate);
			}
			if(posId!=null && posId!=0)
			{
				qry.setInteger("posId", posId);
			}
			
			employeeAssignList = (List)qry.list();
			
			
	}catch (HibernateException he) {
		LOGGER.error(he.getMessage());
		throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(),he);
	} catch (Exception he)
	{
		LOGGER.error(he.getMessage());
		throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(),he);
	}
	
	return employeeAssignList;

	}
	
	/**
	 * Api to get Employee Report with Temporary Assignemt. New Api Added based on Story enhancement
	 * @param Date
	 * @param Position Id
	 * @param employee code
	 * @return employeeView
	 */
	public  List<EmployeeView> getEmployeeWithTempAssignment(String code,Date givenDate,Integer posId) 
	{

		List<EmployeeView> employeeAssignList = null;
		try
		{
			StringBuffer query = null;
		
			query =new StringBuffer("from EmployeeView ev where ev.isActive=1 and ev.assignment.isPrimary='N'");
		
			if(code!=null && !code.equals(""))
			{
				query.append(" and upper(trim(ev.employeeCode)) = :code");		
				
				
			}
			
			if(givenDate==null && posId==0 )
			{
						query.append(" and ((ev.toDate IS NULL AND ev.fromDate <= SYSDATE) " +
						"OR (ev.fromDate <= SYSDATE AND ev.toDate > SYSDATE)OR " +
						"(ev.fromDate IN (SELECT MAX (evn.fromDate)  FROM EmployeeView  evn WHERE evn.id = ev.id  and evn.assignment.isPrimary='N'))) ");
			}
			else
			{
				if(givenDate!=null)
				{
					query.append(" and ev.fromDate <= :givenDate and ev.toDate >=:givenDate");
				}
				
				if(posId!=null && posId!=0)
				{
					query.append(" and ev.position.id =:posId ");
				}
			}
			
		
			Query qry = getCurrentSession().createQuery(query.toString());
			if(givenDate!=null)
			{
				qry.setDate("givenDate",givenDate);
			}
			if(posId!=null && posId!=0)
			{
				qry.setInteger("posId", posId);
			}
			if(code!=null && !code.equals(""))
			{
				qry.setString("code", code);
			}
			employeeAssignList = (List)qry.list();
			
			
	}catch (HibernateException he) {
		LOGGER.error(he.getMessage());
		throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(),he);
	} catch (Exception he)
	{
		LOGGER.error(he.getMessage());
		throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(),he);
	}
	
	return employeeAssignList;

	}
	
	private final static String STR_EXCEPTION="Exception:";
	
}