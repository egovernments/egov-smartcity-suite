package org.egov.pims.reports.services;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.SessionFactory;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.empLeave.model.LeaveApplication;
import org.egov.pims.model.DisciplinaryPunishment;
import org.egov.pims.model.EmployeeView;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;


public class EisReportService {
private SessionFactory sessionFactory;
private PersistenceService persistenceService;

/**
 * @return the persistenceService
 */
public PersistenceService getPersistenceService() {
	return persistenceService;
}

/**
 * @param persistenceService the persistenceService to set
 */
public void setPersistenceService(PersistenceService persistenceService) {
	this.persistenceService = persistenceService;
}

private static final Logger logger=Logger.getLogger(EisReportService.class);
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public List<EmployeeView> getEmpHistory(Integer designationId,String code,String name,Integer status,Integer type,Map<String,Integer> finParams) throws Exception{
		Session session = HibernateUtil.getCurrentSession();
		List<EmployeeView> employeeList = null;
		Integer departmentId=finParams.get("departmentId")==null?0:finParams.get("departmentId");
		Integer functionaryId=finParams.get("functionaryId")==null?0:finParams.get("functionaryId");
		Integer functionId=finParams.get("functionId")==null?0:finParams.get("functionId");
		try
		{
					
					String mainStr = "from EmployeeView ev where  " ;
					if(departmentId.intValue() != 0)
					{
						mainStr +=" ev.deptId.id= :deptId and ";
					}
					if(functionaryId.intValue() !=0)
					{
						mainStr += " ev.functionary.id = :functionaryId and ";
					}
					if(functionId.intValue() !=0)
					{
						mainStr += " ev.functionId.id = :functionId and ";
					}
					if(code!=null&&!code.equals(""))
					{
						mainStr +=" upper(trim(ev.employeeCode)) = :employeeCode and ";
					}
					if(designationId.intValue() != 0)
					{
						mainStr += " ev.desigId.designationId = :designationId and ";
					}
					
					if(name!= null && !name.equals(""))
					{
						mainStr += " trim(upper(ev.employeeName))  like '%"+name.trim().toUpperCase()+"%' and ";
					}
					if(status.intValue() != 0 )
					{
						mainStr += " ev.employeeStatus.id = :employeeStatus and ";
					}
					
						
					if( status.intValue() != 0 && designationId.intValue() == 0)
					{
						mainStr +=" ((ev.toDate is null and ev.fromDate <= SYSDATE ) OR (ev.fromDate <= SYSDATE AND ev.toDate > SYSDATE)) and ev.employeeStatus.id = :employeeStatus ";
					}
					else if(status.intValue() == 0 && designationId.intValue() != 0)
					{
						mainStr +=" ((ev.toDate is null and ev.fromDate <= SYSDATE ) OR (ev.fromDate <= SYSDATE AND ev.toDate > SYSDATE)) ";
					}
						//Inspite of SearchAll is true or false, if employee code is entered, search for all active and inactive employees
						else if(code!=null && !code.equals(""))
						{
							mainStr +="  ((ev.toDate IS NULL AND ev.fromDate <= SYSDATE) OR (ev.fromDate <= SYSDATE AND ev.toDate > SYSDATE) " +
									" OR (ev.fromDate IN (SELECT MAX (evn.fromDate)  FROM EmployeeView  evn   " +
									" WHERE evn.id = ev.id AND NOT EXISTS  (SELECT evn2.id FROM EmployeeView evn2 WHERE evn2.id = ev.id AND " +
									" ((evn2.toDate  IS NULL AND evn2.fromDate <= SYSDATE) OR (evn2.fromDate <= SYSDATE AND evn2.toDate > SYSDATE)) )))) ";
						}
						else if(status.intValue() != 0 ||designationId.intValue() == 0)
						{
							mainStr +="  ((ev.toDate IS NULL AND ev.fromDate <= SYSDATE) OR (ev.fromDate <= SYSDATE AND ev.toDate > SYSDATE) " +
									" OR (ev.fromDate IN (SELECT MAX (evn.fromDate)  FROM EmployeeView  evn   " +
									" WHERE evn.id = ev.id AND NOT EXISTS  (SELECT evn2.id FROM EmployeeView evn2 WHERE evn2.id = ev.id AND " +
									" ((evn2.toDate  IS NULL AND evn2.fromDate <= SYSDATE) OR (evn2.fromDate <= SYSDATE AND evn2.toDate > SYSDATE)) )))) ";
						}
						else
						{
							mainStr +=" ((ev.toDate is null and ev.fromDate <= SYSDATE ) OR (ev.fromDate <= SYSDATE AND ev.toDate > SYSDATE)) ";
						}
					//}
					
					
					Query qry = null;
						qry = session.createQuery(mainStr);
					logger.info("qryqryqryqry"+qry.toString());
					if(departmentId.intValue() != 0)
					{
						qry.setInteger("deptId", departmentId);
					}
					if(functionaryId.intValue() != 0)
					{
						qry.setInteger("functionaryId", functionaryId);
					}
					if(functionId.intValue() != 0)
					{
						qry.setLong("functionId", functionId);
					}
					if(code!=null&&!code.equals(""))
					{
						qry.setString("employeeCode", code.trim().toUpperCase());
					}
					if(designationId.intValue() != 0)
					{
						qry.setInteger("designationId", designationId);
					}
					if(status.intValue() != 0)
					{
						qry.setInteger("employeeStatus", status);
					}
					employeeList = (List)qry.list();
					

	} catch (HibernateException he) {
		logger.error(he);
		throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
	} catch (Exception he) {
		logger.error(he);
		   
		throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);

	}
		return employeeList;
	}
	/*public List<EmployeeAttendenceReport> getEmpAttReport(
			Integer departmentId, Integer designationId, String code,
			String name, String searchAll, String month, String finYear,
			Integer fun) {
		ArrayList<EmployeeAttendenceReport> dataElCol = new ArrayList<EmployeeAttendenceReport>();		
		try {
			CFinancialYear financialYear = EisManagersUtill.getCommonsManager().findFinancialYearById(Long.valueOf(finYear));
			List employeeList = searchEmployeeForAtt(departmentId,designationId, code, name, searchAll, fun);
			//FIXME : This employeeList has multiple rows for same employee with different assignment periods.			
			EmployeeView employeeView = null;
			LOGGER.info("kkkkkkkkkkkkkkkkkk" + employeeList);
			for (Iterator iter = employeeList.iterator(); iter.hasNext();) 
			{
				employeeView = (EmployeeView) iter.next();
				EmployeeAttendenceReport employeeAttendenceReport = null;
				if (month != null && !month.equals("")) 
				{
					employeeAttendenceReport = getEmployeeAttendenceReport(Integer.valueOf(month), EisManagersUtill.getEisManager().getEmloyeeById(employeeView.getId()),financialYear);
					employeeAttendenceReport.setEmployeeId(employeeView.getId());
					LOGGER.info("kkkkkkkkkkkkkkkkkk" + employeeView.getId());
					dataElCol.add(employeeAttendenceReport);
				}

			}
		} catch (Exception he) {
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(), he);
		}
		return dataElCol;

	}*/
	
	public List<LeaveApplication> getEmployeeLeaveRecsForDept(Integer departmentId, Date fromDate, Date toDate) {
		Calendar today = Calendar.getInstance();
		Date todayDate = today.getTime();
		if (departmentId == null )
			throw new EGOVRuntimeException("Department is required");
		if (fromDate == null) {
			today.set(Calendar.DAY_OF_MONTH, 1);
			fromDate = today.getTime();
		}
		if (toDate == null) {
			today.set(Calendar.DAY_OF_MONTH, today.getMaximum(Calendar.DAY_OF_MONTH));
			toDate = today.getTime();
		}
		
		Criteria crit = sessionFactory.getSession().createCriteria(LeaveApplication.class)
		.createAlias("employeeId", "employee")
		.createAlias("employeeId.egpimsAssignmentPrd", "employeeAssignmentPrd", Criteria.LEFT_JOIN)
		.createAlias("employeeId.egpimsAssignmentPrd.egpimsAssignment", "employeeAssignment" , Criteria.LEFT_JOIN)
		.add(Restrictions.and((Restrictions.le("employeeAssignmentPrd.fromDate", todayDate)),
				Restrictions.ge("employeeAssignmentPrd.toDate", todayDate)))
		.add(Restrictions.eq("employeeAssignment.deptId.id" , departmentId))
		.add(Restrictions.and(Restrictions.ge("fromDate", fromDate),Restrictions.le("toDate", toDate)));
		
		return crit.list();   
		
	}
	
	public List<Object[]> getEmployeeLeaveDatesForDept(Integer departmentId, Date fromDate, Date toDate) {
		Calendar today = Calendar.getInstance();
		Date todayDate = today.getTime();
		if (departmentId == null )
			throw new EGOVRuntimeException("Department is required");
		if (fromDate == null) {
			today.set(Calendar.DAY_OF_MONTH, 1);
			fromDate = today.getTime();
		}
		if (toDate == null) {
			today.set(Calendar.DAY_OF_MONTH, today.getMaximum(Calendar.DAY_OF_MONTH));
			toDate = today.getTime();
		}
		
		/**
		 * Uses a cross join of Integer table to get numbers from 0 to 999. This is used to add to fromDate to get each date of the leave application
		 */
		Query qry = sessionFactory.getSession().createQuery("select l.fromDate + H.id*100 + T.id*10 + U.id , l from LeaveApplication l, EmployeeView v " +
				", IntegerTable H, IntegerTable T, IntegerTable U " +
				"where l.employeeId.id = v.id " +
				" and (v.fromDate <= :todayDate1 and " +
				" v.toDate >= :todayDate2) and " +
				" v.deptId.id = :departmentId and " +
				" l.statusId.name not in ('Cancelled', 'Rejected') and " +
				" (l.fromDate >= :fromDate) and " +
				" ((l.fromDate + H.id*100 + T.id*10 + U.id) <= l.toDate " +
				" and (l.fromDate + H.id*100 + T.id*10 + U.id) <= :toDate )" +
				" order by (l.fromDate + H.id*100 + T.id*10 + U.id) asc" 
				);
		qry.setDate("todayDate1", todayDate); 
		qry.setDate("todayDate2", todayDate);
		qry.setDate("fromDate", fromDate);
		qry.setDate("toDate", toDate); 
		qry.setInteger("departmentId", departmentId);
		return qry.list();
		
	}
	
	
	public String getEmpCountAndDesigByDept(Integer deptId)
    {   		
		String selectClause = "Select dept.DEPT_CODE,dept.dept_name, desig.designation_name, count(*) ";
		String fromClause = getFromClause(deptId);
		String groupbyClause=" GROUP BY dept.DEPT_CODE,dept.dept_name, desig.designation_name";
		String orderbyClause=" order by dept.DEPT_CODE,dept.dept_name, desig.designation_name";
    	String qryString =selectClause+fromClause+groupbyClause+orderbyClause;    	
    	return qryString;
   }
	

	
	private String getFromClause(Integer deptId)
	{
		String fromClause = "FROM eg_eis_employeeinfo ev, eg_department dept, eg_designation desig  WHERE ev.designationid = desig.designationid and ev.dept_id = dept.id_dept and";
			if(deptId != null)
				fromClause+=" dept.id_dept= "+deptId + " and ";
		fromClause+="  ev.IS_PRIMARY='Y' AND ev.from_date <= ?  AND ev.TO_DATE >= ?";
		return fromClause;				
	}
	
	
	public String getCountEmpCountAndDesigByDept(Integer deptId)
    {    					     
		String qryString = "Select count(distinct concat(dept.DEPT_CODE,desig.designation_name)) ";  
    	qryString+=getFromClause(deptId); 
    	return qryString;				
   }
	public List<DisciplinaryPunishment> getDisciplinaryActionsPendingForPeriod(Date fromDate, Date toDate) {
		Query qry = sessionFactory.getSession().createQuery(" from DisciplinaryPunishment " +
				"where chargeMemoDate between :fromDate and :toDate " +
				"and status.moduletype='DisciplinaryPunishment' and status.code not in ('Approved', 'Rejected')");
		qry.setDate("fromDate", fromDate);
		qry.setDate("toDate", toDate); 
		return qry.list(); 
	}
	
	 public List<DesignationMaster> getDesignationList(String codelike)
	 {
			Criteria criteria = sessionFactory.getSession().createCriteria(DesignationMaster.class, "designation");
			criteria.add(Restrictions.ilike("designationName", codelike, MatchMode.START));
			return criteria.list();
	 }
	
	 public List getAllDepartments() {
			try {
				Query qry = sessionFactory.getSession().createQuery("FROM DepartmentImpl DI");
				return qry.list();
			} catch (HibernateException e) {
				logger.error("Exception occurred in getAllDepartments",e);			
				HibernateUtil.rollbackTransaction();
				throw new EGOVRuntimeException("Exception occurred in getAllDepartments",e);
			}
		}
	 
		public String getcountEmpByDeptDesg(Integer posId){
			String qryString = "Select count(*) from  ";  
	    	qryString+=getEmpDesigByDept(posId); 
	    	return qryString;	
		}
		public String getEmpDesigByDept(Integer posId){
			String query="";
			if(posId != null){
				 query="(select egpos.position_name,ev.code,ev.name,ev.from_date,ev.to_date,is_primary  "+
					      "from eg_eis_employeeinfo ev, eg_position egpos where ev.pos_id= ?  "+
	                     " and ev.pos_id=egpos.id and "+
			            "((ev.from_date<= ? and ev.to_date>= ?) or  " +
	                      "(ev.from_date<= ? and ev.to_date IS NULL))  "+
	                      "union  "+
	                      "select egpos.position_name,null,null,null,null,null  "+
	                      "from eg_position egpos where egpos.id = ?"+
			              " AND egpos.id not in  "+
	                      "(select ev.pos_id from eg_eis_employeeinfo ev where " +
	                      "((ev.from_date<= ? and ev.to_date>= ?) or (ev.from_date<= ? and ev.to_date IS NULL))))";
			}
			else		
			{
				
				query="(SELECT egpos.position_name AS posname,ev.code,ev.NAME,ev.from_date,ev.TO_DATE,is_primary  "+
				      "FROM eg_eis_employeeinfo ev, EG_POSITION egpos WHERE ev.pos_id=egpos.ID AND egpos.ID IN   "+
				      "(SELECT ID FROM EG_POSITION pos WHERE pos.id_deptdesig IN ( SELECT ID  FROM EGEIS_DEPTDESIG dp  "+
				      "WHERE  dp.dept_id=? AND dp.DESIG_ID= ? )) AND "+
				      "((ev.from_date<= ? AND ev.TO_DATE>= ?) OR (ev.from_date<=? AND ev.TO_DATE IS NULL))  "+
				      "UNION  "+
				      "SELECT egpos.position_name,NULL,NULL,NULL,NULL,NULL  "+
				      "FROM  EG_POSITION egpos,EGEIS_DEPTDESIG  desig WHERE egpos.id_deptdesig=desig.ID AND  "+
				      "desig.dept_id=? AND desig.desig_id=? AND  "+
				      "egpos.ID NOT IN  (SELECT pos.ID FROM EG_POSITION pos, eg_eis_employeeinfo ev,EGEIS_DEPTDESIG  desig WHERE  " +
				      " pos.ID=ev.pos_id AND pos.id_deptdesig=desig.ID AND  desig.dept_id=? AND desig.desig_id=? AND   "+
				      "((ev.from_DATE<= ? AND ev.TO_DATE>=? ) OR " +
				      "(ev.from_date<=? AND ev.TO_DATE IS NULL))) ) order by posname";
			}
			return query;
			
		}
       
	
		
}


