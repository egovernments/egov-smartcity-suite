package org.egov.payroll.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.payroll.reports.SearchAction;
import org.egov.payroll.utils.PayrollConstants;
import org.hibernate.Query;
import org.hibernate.Session;

public class BatchFailureDetailsHibernateDAO extends GenericHibernateDAO implements BatchFailureDetailsDAO {
	private static final Logger logger = Logger.getLogger(SearchAction.class);
	public BatchFailureDetailsHibernateDAO(Class persistentClass, Session session)
    {
        super(persistentClass, session);
    }

	/*
	public void updateIsHistory(Integer month,Integer year,Integer deptid,Integer functionaryId, Integer billNumberId) {
		String querystr="UPDATE BatchFailureDetails bfdet set isHistory='Y' where isHistory='N' and bfdet.month=:month and bfdet.financialyear.id=:year and bfdet.status=:status";
		if(deptid!=null && deptid.intValue()!=0)
			{
			querystr=querystr+" and bfdet.department.id=:deptid";
			}
		if(functionaryId!=null && functionaryId.intValue()!=0)
			{
			querystr=querystr+" and bfdet.functionary.id=:functionaryId";
			}
		if(billNumberId != null && billNumberId.intValue() != 0)
		{
			querystr=querystr+" and bfdet.billNumber.id=:billNumberId";
		}
		Query qry = getSession().createQuery(querystr);
		qry.setInteger("status",PayrollConstants.BATCH_FAILURE_DETAILS_STATUS_OPEN);
		qry.setInteger("year",year);
		qry.setInteger("month",month);
		if(deptid!=null && deptid.intValue()!=0)
			{
			qry.setInteger("deptid",deptid);
			}
		if(functionaryId!=null && functionaryId.intValue()!=0)
			{
			qry.setInteger("functionaryId",functionaryId);
			}
		if(billNumberId != null && billNumberId.intValue() != 0)
		{
			qry.setInteger("billNumberId",billNumberId);
		}
		qry.executeUpdate();		
	}
	*/
	public void updateIsHistory(Integer month,Integer year,List empIdsList){
		String querystr="UPDATE BatchFailureDetails bfdet set isHistory='Y' where isHistory='N' and bfdet.month=:month and bfdet.financialyear.id=:year and bfdet.status=:status";		
		if(null != empIdsList && empIdsList.size() > 0)
		{
			querystr=querystr+" and bfdet.employee.idPersonalInformation in (:empIdsList)";
		}		
		Query qry = getSession().createQuery(querystr);
		qry.setInteger("status",PayrollConstants.BATCH_FAILURE_DETAILS_STATUS_OPEN);
		qry.setInteger("year",year);
		qry.setInteger("month",month);
		if(null != empIdsList && empIdsList.size() > 0)
		{
			qry.setParameterList("empIdsList",empIdsList);
		}		
		qry.executeUpdate();
	}
	
	public List getPendingPaySlipsList(Integer finyr, Integer month, Integer deptid, Integer empid, Integer type,Integer functionaryId,Integer billNoId,Integer errPay) {
		StringBuffer qry=new StringBuffer("select bf from BatchFailureDetails bf where bf.isHistory='N' and bf.status=:status ");		
        if(billNoId!=null && billNoId>0 && !"".equals(billNoId))
        {              
                qry.append(" and bf.billNumber.id="+billNoId);                             
        }     
		if(finyr!=null)
				{
			qry.append(" and bf.financialyear.id=".concat(finyr.toString()));
				}
		
		if(month!=null)
		{
			qry.append(" and bf.month="+month);				
		}
		if(deptid!=null)
		{
			qry.append(" and bf.department.id="+deptid);
		}
		if(empid!=null)
		{
			qry.append(" and bf.employee.id="+empid);
		}
		if(type!=null)
		{
			qry.append(" and bf.payType="+type);
		}
		if(functionaryId!=null)
		{
			qry.append(" and bf.functionary.id="+functionaryId);
		}
		if(errPay == 0)
		{
			qry.append(" and bf.remarks !='Payslip already exists for current month'");
		}
	
		 Query query = getSession().createQuery(qry.toString());
         query.setInteger("status",PayrollConstants.BATCH_FAILURE_DETAILS_STATUS_OPEN);
         logger.debug("query "+query);
         return (List)query.list();
		
			
	}
	public boolean resolvePaySlipFailure(Integer empid,Integer month,Integer year,Integer paytype)
	{
		StringBuffer qry=new StringBuffer("UPDATE BatchFailureDetails set status=:status where isHistory='N' and month=:month and financialyear.id=:year and employee.idPersonalInformation=:empid and payType.id=:paytype");
		Query query = getSession().createQuery(qry.toString());
		query.setInteger("status",PayrollConstants.BATCH_FAILURE_DETAILS_STATUS_RESOLVED);
		query.setInteger("month",month);
		query.setInteger("year",year);
		query.setInteger("empid",empid);
		query.setInteger("paytype",paytype);
		query.executeUpdate();
		return true;	
	}

	/* (non-Javadoc)
	 * @see org.egov.payroll.dao.BatchFailureDetailsDAO#getFailurePaySlipForEmpbyMonthFinyrAndPaytype(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	public boolean isEmphasFailureForMonthFinyrPaytype(Integer empid, Integer month, Integer year, Integer paytype) {
		StringBuffer qry=new StringBuffer("select distinct(bf) from BatchFailureDetails bf where isHistory='N' and month=:month and financialyear.id=:year and employee.idPersonalInformation=:empid and payType.id=:paytype and status=:status");
		Query query = getSession().createQuery(qry.toString());
		query.setInteger("status",PayrollConstants.BATCH_FAILURE_DETAILS_STATUS_OPEN);
		query.setInteger("month",month);
		query.setInteger("year",year);
		query.setInteger("empid",empid);
		query.setInteger("paytype",paytype);
		ArrayList list=(ArrayList)query.list();
		if(!list.isEmpty())
		{
			return true;
		}
		return true;
	}

}
