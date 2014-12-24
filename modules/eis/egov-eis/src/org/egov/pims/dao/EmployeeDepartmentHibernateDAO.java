/*
 * PropertyHibernateDAO.java Created on Oct 5, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.pims.dao;

import java.util.ArrayList;
import java.util.List;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.EmployeeDepartment;
import org.egov.pims.model.PersonalInformation;
import org.hibernate.Query;
import org.hibernate.Session;
import org.egov.pims.model.EmployeeView;

/**
 * This Class implememets the PersonalInformationDAO for the Hibernate specific
 * Implementation
 *
 * @author Neetu
 * @version 2.00
 */
public class EmployeeDepartmentHibernateDAO extends GenericHibernateDAO implements EmployeeDepartmentDAO
{

    /*
     *
	 * @param persistentClass
	 * @param session
	 */
	public EmployeeDepartmentHibernateDAO(Class persistentClass, Session session)
	{
		super(persistentClass, session);
	}
	public EmployeeDepartment getEmployeeDepartmentByID(Integer ID)
	{
		Query qry = getSession().createQuery("from EmployeeDepartment B where B.id =:id ");
		qry.setInteger("id", ID);
		return (EmployeeDepartment)qry.uniqueResult();
	}
	public void deleteByAss(Assignment assignment)
	{
		Query qry = getSession().createQuery("from EmployeeDepartment B where B.assignment =:assignment ");
		qry.setEntity("assignment", assignment);
		if(qry.list()!=null&&!qry.list().isEmpty())
		{
			for(java.util.Iterator iter = qry.list().iterator();iter.hasNext();)
			{
				EmployeeDepartment employeeDepartment = (EmployeeDepartment)iter.next();
				delete(employeeDepartment);
			}
		}
	}

	public  List<PersonalInformation>  getAllHodEmpByDept(Integer deptId) throws Exception{
		List<PersonalInformation> listEmp = null;
		Query qry = getSession().createQuery("select empDept.assignment.assignmentPrd.employeeId from EmployeeDepartment empDept " +
												"where empDept.hodept.id =:deptId ");
		qry.setInteger("deptId", deptId);
		listEmp = qry.list();
		return listEmp;
	}
	public  List<EmployeeView>  getAllHodEmpViewByDept(Integer deptId) throws Exception{
			List<EmployeeView> employeeList = null;
			Query qry = getSession().createQuery("from EmployeeView ev where ev.assignId in(select empDept.assignment.id from EmployeeDepartment empDept " +
													"where empDept.hodept.id =:deptId) ");
			qry.setInteger("deptId", deptId);
			employeeList = qry.list();
			return employeeList;
	}

}


