package org.egov.payroll.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.payroll.model.IncrementDetails;
import org.egov.payroll.utils.PayrollConstants;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

public class IncrementDetailsHibernateDAO extends GenericHibernateDAO implements IncrementDetailsDAO{
	public IncrementDetailsHibernateDAO(Class arg0, Session arg1) {
		super(arg0, arg1);		
	}

	public BigDecimal getPendingIncrementAmount(Integer empId,Integer month,Integer finYear)
	{
		BigDecimal list=null;
		
		String qry1 = "SELECT sum(incr.amount) as amount FROM EGPAY_INCREMENTDETAILS incr where incr.FINANCIALYEARID <= :year"
				+ " and ( ADD_MONTHS((SELECT financialyear.STARTINGDATE FROM FINANCIALYEAR where id = incr.financialyearid),incr.month- 4) -"
				+ " ADD_MONTHS((SELECT financialyear.STARTINGDATE FROM FINANCIALYEAR where id =:year ), :month - 4) ) < 0"
				+ " and incr.empid = :empid  and incr.status = :pendingStatus";
		SQLQuery qry = HibernateUtil.getCurrentSession().createSQLQuery(qry1);
		
		qry.setInteger("year", finYear);
		qry.setInteger("month", month);
		qry.setInteger("empid", empId);
		qry.setInteger("pendingStatus",PayrollConstants.EMP_INCREMENT_DETAILS_STATUS_PENDING);
		list=(BigDecimal)qry.uniqueResult();			
		
		return list;
	}
	public boolean resolvePendingIncr(Integer empid,Integer month,Integer year)
	{
		StringBuffer qry=new StringBuffer("UPDATE egpay_IncrementDetails incr set status=:status where empid=:empid and incr.FINANCIALYEARID <= :year "  +
				"and ( ADD_MONTHS((SELECT financialyear.STARTINGDATE FROM FINANCIALYEAR where id = incr.financialyearid),incr.month- 4) -"
					+ " ADD_MONTHS((SELECT financialyear.STARTINGDATE FROM FINANCIALYEAR where id =:year ), :month - 4) ) < 0");
		SQLQuery query = HibernateUtil.getCurrentSession().createSQLQuery(qry.toString());		
		query.setInteger("status",PayrollConstants.EMP_INCREMENT_DETAILS_STATUS_RESOLVED);
		query.setInteger("month",month);
		query.setInteger("year",year);
		query.setInteger("empid",empid);
		query.executeUpdate();
		return true;	
	}
	
	/**
	 * Checking for Increment applied or not
	 */
	public Boolean checkingForIncrementApplied(Integer empid,Integer month,Integer year){
		List<IncrementDetails> listIncrDet = new ArrayList<IncrementDetails>();
		Query qry = getSession().createQuery("from IncrementDetails incr where incr.employee.idPersonalInformation = :empid and incr.financialyear.id = :year " +
					"and incr.month = :month and incr.status = :resolvedStatus");
		qry.setInteger("empid", empid);
		qry.setInteger("year", year);
		qry.setInteger("month", month);
		qry.setInteger("resolvedStatus", PayrollConstants.EMP_INCREMENT_DETAILS_STATUS_RESOLVED);
		listIncrDet = qry.list();
		if(listIncrDet.isEmpty()){
			return false;
		}
		else{
			return true;		
		}
	}
	
}
