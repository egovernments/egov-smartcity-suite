package org.egov.pims.empLeave.dao;

import org.egov.pims.dao.PersonalInformationDAO;



/**
 * Defines all DAOs and the concrete factories to get the conrecte DAOs.
 * <p>
 * Either use the <tt>DEFAULT</tt> to get the same concrete RNDDAOFactory
 * throughout your application, or a concrete factory by name, e.g.
 * <tt>RNDDAOFactory.HIBERNATE</tt> is a concrete <tt>RNDHibernateDAOFactory</tt>.
 * <p>
 * Implementation: If you write a new DAO, this class has to know about it.
 * If you add a new persistence mechanism, add an additional concrete factory
 * for it to the enumeration of factories.
 * <p>
 * It probably wouldn't be a bad idea to move the <tt>DEFAULT</tt> setting
 * into external configuration.
 *
 * @author christian.bauer@jboss.com
 */
public abstract class LeaveDAOFactory {

    private static final LeaveDAOFactory EJB3_PERSISTENCE = null;

    private static final LeaveDAOFactory HIBERNATE = new LeaveHibernateDAOFactory ();


    public static LeaveDAOFactory getDAOFactory()
    {
    	return HIBERNATE;
    }
    public abstract LeaveApplicationDAO getLeaveApplicationDAO();
    public abstract LeaveApprovalDAO getLeaveApprovalDAO();
    public abstract LeaveMasterDAO getLeaveMasterDAO();
    public abstract LeaveOpeningBalanceDAO getLeaveOpeningBalanceDAO();
    public abstract TypeOfLeaveMasterDAO getTypeOfLeaveMasterDAO();
    public abstract HolidaysUlbDAO getHolidaysUlbDAO();
    public abstract AttendenceDAO getAttendenceDAO();
    public abstract AttendenceTypeDAO getAttendenceTypeDAO();
    public abstract WdaysconstntsDAO getWdaysconstntsDAO();
    public abstract CompOffDAO getCompOffDAO();
    public abstract CalendarYearDao getCalendarDao();
    
    
    
    
    }
