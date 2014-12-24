/*
 * ExceptionHibernateDAO.java Created on Aug 29, 2007
 *
 * Copyright 2007 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
 package org.egov.payroll.dao;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.egov.commons.EgwStatus;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.payroll.model.EmpException;
import org.egov.payroll.services.payslip.PayRollService;
import org.egov.payroll.utils.PayrollConstants;
import org.egov.payroll.utils.PayrollExternalImpl;
import org.egov.payroll.utils.PayrollExternalInterface;
import org.egov.payroll.utils.PayrollManagersUtill;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.PersonalInformation;
import org.hibernate.Query;
import org.hibernate.Session;
import org.egov.exceptions.EGOVException;

/**
 * This Class implememets the SalaryCodesDAO for the Hibernate specific
 * Implementation
 *
 * @author Lokesh
 * @version 2.00
 */

public class ExceptionHibernateDAO extends GenericHibernateDAO implements ExceptionDAO {

    
    public ExceptionHibernateDAO(Class persistentClass, Session session)
    {
        super(persistentClass, session);
    }
    public List getAllExceptionsInStatusByMonthAndYear(Integer month, Integer finYear,EgwStatus status) {
		List empPayrollList = null;
		Query qry = getSession().createQuery("  from EmpPayroll e where e.month =:month and " +
				" e.financialyear.id =:finYear and e.employee.idPersonalInformation in " +
				" (select ex.employee.idPersonalInformation from Exception ex " +
				" where ex.month =:month and ex.financialyear.id =:finYear) and e.status =:status " );
		qry.setInteger("month", month);
		qry.setInteger("finYear", finYear);
		empPayrollList = qry.list();
		return empPayrollList;
    }	
    
    public List<EmpException> getAllExceptionByEmpIdFinYearMonth(Integer empId,Long finYearId,BigDecimal month){
    	List<EmpException> e = new ArrayList<EmpException>();
    	Query qry = getSession().createQuery("from EmpException ex where ex.employee.idPersonalInformation = :empId " +
    				"and ex.financialyear.id = :finYearId and ex.month = :month");
    	qry.setInteger("empId", empId);
    	qry.setLong("finYearId", finYearId);
    	qry.setBigDecimal("month", month);
    	e = qry.list();
    	return e;
    }
    
    public List<EmpException> getAllExceptionByFinYearIdAndMonth(Long finYearId,BigDecimal month){
    	List<EmpException> e = new ArrayList<EmpException>();
    	Query qry = getSession().createQuery("from EmpException ex where ex.financialyear.id = :finYearId and " +
    				"ex.month = :month");
    	qry.setLong("finYearId", finYearId);
    	qry.setBigDecimal("month", month);
    	e = qry.list();
    	return e;
    }
    public boolean isEmpHasExceptionForFullMonth(Integer empid,GregorianCalendar fromdate,GregorianCalendar todate)
    {
    	List e = new ArrayList();
    	Query qry = getSession().createQuery("select ex.employee.idPersonalInformation from EmpException ex "
		+ " where ex.status.moduletype in ('EmpException') and ex.status.description in ('Approved') and (ex.fromDate is null or ex.fromDate<= :startdate ) " +
		" and ex.toDate>=:startdate and ex.toDate<=:enddate and ex.employee.idPersonalInformation=:empId ");
    	qry.setInteger("empId", empid);
    	qry.setDate("startdate", fromdate.getTime());
    	qry.setDate("enddate", todate.getTime());
    	e = qry.list();
    	
    	return e.isEmpty()?true:false;
    }
    public List<EmpException> getActiveExceptionsForEmp(Integer empid,Date fromdate,Date todate)
    {    	
    	Query qry = getSession().createQuery("from EmpException ex "
		+ " where ex.status.moduletype in ('EmpException') and ex.status.description in ('Approved') and (ex.fromDate is null or ex.fromDate<=to_date(:date,'dd/mm/yy') ) and ex.employee.idPersonalInformation=:empid order by ex.fromDate asc ");
    	qry.setInteger("empid", empid);
    	qry.setDate("date", todate);
    	//qry.setDate("enddate", todate);
    	return (List)qry.list();
    }
    
    public List<EmpException> getAllExceptionsForUserByFinYrAndMonth(Integer userid,Integer finyrid,Integer month) throws Exception{
    	List<EmpException> exceptions = new ArrayList<EmpException>();
    	PayrollExternalInterface payrollExternalInterface=PayrollManagersUtill.getPayrollExterInterface();
    	PayRollService payRollService = PayrollManagersUtill.getPayRollService();
    	//Date startdate=payManager.getStartDateOfMonthByMonthAndFinYear(month,finyrid);
    	PersonalInformation employee = payrollExternalInterface.getEmpForUserId(userid);
    	if (employee==null)
    		{
    		throw new EGOVException("Logged in user is not mapped to an employee");
    		}
    	Date enddate=payRollService.getEndDateOfMonthByMonthAndFinYear(month,finyrid);
    	// setting the date to start date of month
    	GregorianCalendar startdate=new GregorianCalendar();    	
    	startdate.setTime(enddate);
    	startdate.set(Calendar.DATE,1);    	
    	Assignment assignmentTo = payrollExternalInterface.getAssignmentByEmpAndDate(enddate,employee.getIdPersonalInformation());
    	if( assignmentTo!= null && assignmentTo.getPosition() != null)
    	{
	    	Query qry = getSession().createQuery("select exp from EmpException exp, PersonalInformation P, Assignment A, " +
	    			"AssignmentPrd APR, Position Pos, PositionHeirarchy PosHir, ObjectType O where " +
	    			"exp.employee.idPersonalInformation = P.idPersonalInformation and " +
	    			"P.idPersonalInformation = APR.employeeId.idPersonalInformation and " +
	    			"APR.id = A.assignmentPrd.id and " +
	    			"APR.fromDate <= :currentDate and (APR.toDate >= :currentDate or APR.toDate = null) and " +
	    			"A.position.id = Pos.id and " +
	    			"Pos.id = PosHir.posFrom.id and " +
	    			"PosHir.objectType.id = O.id and " +
	    			"O.type = :type and "+
	    			"PosHir.posTo = :posTo and "+    			
	    		//	"exp.status = :status and " +
	    			" ((exp.fromDate <= :currentDate and (exp.toDate >= :currentDate or ( exp.toDate< :currentDate and exp.toDate>= :startdate ) or exp.toDate is null )) or exp.fromDate is null )");
	    	qry.setDate("currentDate", enddate);
	    	qry.setDate("startdate", startdate.getTime());
	    	qry.setString("type", "Exception");
	    	qry.setEntity("posTo", assignmentTo.getPosition());    	
	    	/* if(status == null )
	    	qry.setEntity("status", PayrollManagersUtill.getCommonsManager().getStatusByModuleAndDescription(PayrollConstants.EMP_EXCEPTION_MODILE,PayrollConstants.EMP_EXCEPTION_CREATED_STATUS));
	    	else
	    		qry.setEntity("status",status); */
	    	exceptions = qry.list();
	    	return exceptions;
    	}else
    		{
    		return null;
    		}
    }
    public List<EmpException> getAllExceptionsForUserByFinYrAndMonthAndStatus(Integer userid,Integer finyrid,Integer month,EgwStatus status) throws Exception{
    	PayrollExternalInterface payrollExternalInterface=PayrollManagersUtill.getPayrollExterInterface();
    	List<EmpException> exceptions = new ArrayList<EmpException>();
    	PayRollService payRollService = PayrollManagersUtill.getPayRollService();
    	Date enddate=payRollService.getEndDateOfMonthByMonthAndFinYear(month,finyrid);
//    	 setting the date to start date of month
    	GregorianCalendar startdate=new GregorianCalendar();    	
    	startdate.setTime(enddate);
    	startdate.set(Calendar.DATE,1);
    	PersonalInformation employee = payrollExternalInterface.getEmpForUserId(userid);
    	Assignment assignmentTo = payrollExternalInterface.getLatestAssignmentForEmployee(employee.getIdPersonalInformation());
    	Query qry = getSession().createQuery("select exp from EmpException exp, PersonalInformation P, Assignment A, " +
    			"AssignmentPrd APR, Position Pos, PositionHeirarchy PosHir, ObjectType O where " +
    			"exp.employee.idPersonalInformation = P.idPersonalInformation and " +
    			"P.idPersonalInformation = APR.employeeId.idPersonalInformation and " +
    			"APR.id = A.assignmentPrd.id and " +
    			"APR.fromDate <= :currentDate and (APR.toDate >= :currentDate or APR.toDate = null) and " +
    			"A.position.id = Pos.id and " +
    			"Pos.id = PosHir.posFrom.id and " +
    			"PosHir.objectType.id = O.id and " +
    			"O.type = :type and "+
    			"PosHir.posTo = :posTo and "+    			
    			"exp.status = :status and " +
    			" ((exp.fromDate <= :currentDate and (exp.toDate >= :currentDate or ( exp.toDate< :currentDate and exp.toDate>= :startdate ) or exp.toDate is null )) or exp.fromDate is null )");
    	qry.setDate("currentDate", enddate);
    	qry.setDate("startdate", startdate.getTime());
    	qry.setString("type", "Exception");
    	qry.setEntity("posTo", assignmentTo.getPosition());    	
    	if(status == null )
    	{
    		qry.setEntity("status", payrollExternalInterface.getStatusByModuleAndDescription(PayrollConstants.EMP_EXCEPTION_MODILE,PayrollConstants.EMP_EXCEPTION_CREATED_STATUS));
    	}
    	else
    		{
    		qry.setEntity("status",status);
    		}
    	exceptions = qry.list();
    	return exceptions;
    }
    
    
    public List<EmpException> getAllEmpException(Long finYearId, BigDecimal month, String status){
    	List<EmpException> listEmpException = new ArrayList<EmpException>();
    	String query = "from EmpException ex where ex.financialyear.id = :finYearId and ex.month = :month ";
    	if(status != null){
    		query += "and ex.status.description = :status";
    	}
    	Query qry = getSession().createQuery(query);
		qry.setLong("finYearId", finYearId);
		qry.setBigDecimal("month", month);
		if(status != null){
			qry.setString("status", status);
		}
		listEmpException = qry.list();		
		return listEmpException;
    }
    
}

