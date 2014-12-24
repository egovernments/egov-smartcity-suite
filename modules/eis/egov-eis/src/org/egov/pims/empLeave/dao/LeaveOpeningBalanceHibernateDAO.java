package org.egov.pims.empLeave.dao;

import java.util.List;


import org.apache.log4j.Logger;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.pims.empLeave.model.LeaveOpeningBalance;
import org.egov.pims.empLeave.service.EmpLeaveService;
import org.egov.pims.empLeave.service.EmpLeaveService;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * This Class implememets the PersonalInformationDAO for the Hibernate specific
 * Implementation
 *
 * @author Neetu
 * @version 2.00
 */
public class LeaveOpeningBalanceHibernateDAO extends GenericHibernateDAO implements LeaveOpeningBalanceDAO
{
    private final static Logger LOGGER = Logger.getLogger(LeaveOpeningBalanceHibernateDAO.class.getClass());
    /*
     *
	 * @param persistentClass
	 * @param session
	 */
	public LeaveOpeningBalanceHibernateDAO(Class persistentClass, Session session)
	{
		super(persistentClass, session);
	}
	public LeaveOpeningBalance getLeaveOpeningBalanceByID(Integer id)
	{
		Query qry = getSession().createQuery("from LeaveOpeningBalance B where B.id =:id ");
		qry.setInteger("id", id);
		return (LeaveOpeningBalance)qry.uniqueResult();
	}
	
	public LeaveOpeningBalance getLeaveOpeningBalanceByEmpID(Integer empId,Integer leaveType){
		LeaveOpeningBalance lvOpeningBlnce = null;
		List<LeaveOpeningBalance> list = null;
		Query qry = getSession().createQuery("from LeaveOpeningBalance B where B.employeeId.idPersonalInformation =:empId and " +
											"B.typeOfLeaveMstr.id =:leaveType ");
		qry.setInteger("empId", empId);
		qry.setInteger("leaveType", leaveType);
		list = qry.list();
		for(LeaveOpeningBalance tempLvOpening : list)
		{
			lvOpeningBlnce = tempLvOpening;
		}
		return lvOpeningBlnce;
	}
}



