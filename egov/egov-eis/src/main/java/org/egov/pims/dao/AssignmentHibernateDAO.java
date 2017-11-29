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
import org.egov.eis.entity.Assignment;
import org.egov.eis.entity.EmployeeView;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;


public class AssignmentHibernateDAO implements AssignmentDAO
{
    private final static Logger LOGGER = Logger.getLogger(AssignmentHibernateDAO.class.getClass());
    
    @PersistenceContext
	private EntityManager entityManager;
    
    public Session  getCurrentSession() {
		return entityManager.unwrap(Session.class);
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
		throw new ApplicationRuntimeException(STR_EXCEPTION + he.getMessage(),he);
	} catch (Exception he)
	{
		LOGGER.error(he.getMessage());
		throw new ApplicationRuntimeException(STR_EXCEPTION + he.getMessage(),he);
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
			throw new ApplicationRuntimeException(STR_EXCEPTION + he.getMessage(),he);
		} catch (Exception he) 
		{
			LOGGER.error(he.getMessage());
			throw new ApplicationRuntimeException(STR_EXCEPTION + he.getMessage(),he);
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
		throw new ApplicationRuntimeException(STR_EXCEPTION + he.getMessage(),he);
	} catch (Exception he)
	{
		LOGGER.error(he.getMessage());
		throw new ApplicationRuntimeException(STR_EXCEPTION + he.getMessage(),he);
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
		
			query =new StringBuffer("from EmployeeView ev where ev.isActive=true and ev.assignment.isPrimary='N'");
		
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
		throw new ApplicationRuntimeException(STR_EXCEPTION + he.getMessage(),he);
	} catch (Exception he)
	{
		LOGGER.error(he.getMessage());
		throw new ApplicationRuntimeException(STR_EXCEPTION + he.getMessage(),he);
	}
	
	return employeeAssignList;

	}

	@Override
	@Transactional
	public void create(final Assignment egEmpAssignment) {
		getCurrentSession().save(egEmpAssignment);
	}

	@Override
	@Transactional
	public void update(final Assignment assignment) {
		getCurrentSession().update(assignment);
	}

	private final static String STR_EXCEPTION="Exception:";
	
}