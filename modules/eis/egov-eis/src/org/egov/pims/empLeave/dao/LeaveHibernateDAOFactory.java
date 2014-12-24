package org.egov.pims.empLeave.dao;


import org.egov.infstr.utils.HibernateUtil;
import org.egov.pims.empLeave.model.Attendence;
import org.egov.pims.empLeave.model.AttendenceType;
import org.egov.pims.empLeave.model.CalendarYear;
import org.egov.pims.empLeave.model.HolidaysUlb;
import org.egov.pims.empLeave.model.LeaveApplication;
import org.egov.pims.empLeave.model.LeaveApproval;
import org.egov.pims.empLeave.model.LeaveMaster;
import org.egov.pims.empLeave.model.LeaveOpeningBalance;
import org.egov.pims.empLeave.model.TypeOfLeaveMaster;
import org.egov.pims.empLeave.model.Wdaysconstnts;
import org.hibernate.Session;


/**
 * Returns Hibernate-specific instances of DAOs.
 * <p>
 * One of the responsiblities of the factory is to inject a Hibernate Session
 * into the DAOs. You can customize the getCurrentSession() method if you
 * are not using the default strategy, which simply delegates to
 * <tt>HibernateUtil.getCurrentSession()</tt>, and also starts a transaction
 * lazily, if none exists already for the current thread or current EJB.
 * <p>
 * If for a particular DAO there is no additional non-CRUD functionality, we use
 * an inner class to implement the interface in a generic way. This allows clean
 * refactoring later on, should the interface implement business data access
 * methods at some later time. Then, we would externalize the implementation into
 * its own first-level class. We can't use anonymous inner classes for this trick
 * because they can't extend or implement an interface and they can't include
 * constructors.
 *
 * @author christian.bauer@jboss.com
 */
public class LeaveHibernateDAOFactory extends LeaveDAOFactory
{
	
    protected Session getCurrentSession()
    {
        HibernateUtil.beginTransaction();
        return HibernateUtil.getCurrentSession();
    }
    public  LeaveApplicationDAO getLeaveApplicationDAO()
    {
		return new LeaveApplicationHibernateDAO(LeaveApplication.class, getCurrentSession());
	}
    public  LeaveApprovalDAO getLeaveApprovalDAO()
    {
		return new LeaveApprovalHibernateDAO(LeaveApproval.class, getCurrentSession());
	}
    public  LeaveMasterDAO getLeaveMasterDAO()
    {
		return new LeaveMasterHibernateDAO(LeaveMaster.class, getCurrentSession());
	}
    public  LeaveOpeningBalanceDAO getLeaveOpeningBalanceDAO()
    {
		return new LeaveOpeningBalanceHibernateDAO(LeaveOpeningBalance.class, getCurrentSession());
	}
    
    public  TypeOfLeaveMasterDAO getTypeOfLeaveMasterDAO()
    {
    	return new TypeOfLeaveMasterHibernateDAO(TypeOfLeaveMaster.class, getCurrentSession());
    }
    public  HolidaysUlbDAO getHolidaysUlbDAO()
    {
    	return new HolidaysUlbHibernateDAO(HolidaysUlb.class, getCurrentSession());
    	
    }
    public  AttendenceDAO getAttendenceDAO()
    {
    	return new AttendenceHibernateDAO(Attendence.class, getCurrentSession());
    }
    public  AttendenceTypeDAO getAttendenceTypeDAO()
    {
    	return new AttendenceTypeHibernateDAO(AttendenceType.class, getCurrentSession());
    }
    public  WdaysconstntsDAO getWdaysconstntsDAO()
    {
    	return new WdaysconstntsHibernateDAO(Wdaysconstnts.class, getCurrentSession());
    }
    public  CompOffDAO getCompOffDAO()
    {
    	return new CompOffHibernateDAO(CompOffDAO.class, getCurrentSession());
    }
	
	public CalendarYearDao getCalendarDao() {
		
		return new CalendarYearHibernateDAO(CalendarYear.class,getCurrentSession());
	}
}
