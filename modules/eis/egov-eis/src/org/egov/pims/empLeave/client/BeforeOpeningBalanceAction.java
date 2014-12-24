package org.egov.pims.empLeave.client;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.CFinancialYear;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.empLeave.model.CalendarYear;
import org.egov.pims.service.EmployeeService;
import org.egov.pims.utils.EisManagersUtill;

public class BeforeOpeningBalanceAction extends DispatchAction 
{
	private static  final Logger LOGGER = Logger.getLogger(BeforeOpeningBalanceAction.class);
	private EmployeeService employeeService;
	
	
	public ActionForward view(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws IOException,ServletException
	{
		
		String target = "";
		try
	    {
			populate(req);
			String mode ="";
			SimpleDateFormat sqlUtil = new SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault());
			String  currentfinYear = EisManagersUtill.getCommonsService().getCurrYearFiscalId();
			CFinancialYear financialY =EisManagersUtill.getCommonsService().findFinancialYearById(Long.valueOf(currentfinYear));
			req.getSession().setAttribute("currentfinYear", currentfinYear);
			req.getSession().setAttribute("financialY", financialY);
			
			Date currentDate = new Date();
   		    String calYearId=EisManagersUtill.getEmpLeaveService().getYearIdByGivenDate(sqlUtil.format(currentDate));
			CalendarYear calendarYr = EisManagersUtill.getEmpLeaveService().getCalendarYearById(Long.valueOf(calYearId));
			
			req.getSession().setAttribute("calYearId", calYearId);
			req.getSession().setAttribute("calendarYr", calendarYr);
			
   		    
			if(req.getParameter("mode")==null)
			{
				
				mode = ((String)req.getSession().getAttribute("mode")).trim();
			}
			else
			{
				mode = req.getParameter("mode").trim();
			}
			
			req.getSession().setAttribute("viewMode",mode);
			target = "viewjsp";
		}
		catch(Exception e)
		{
			target = "error";
			LOGGER.debug("Exception Encountered!!!"+e.getMessage());
			 HibernateUtil.rollbackTransaction();
			 throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
		}
		return mapping.findForward(target);
	}
	private void populate(HttpServletRequest req)
	{

		try {
			/*
			 * get all designation
			 */
			ArrayList designationMasterList=(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-DesignationMaster");
			req.getSession().setAttribute("mapOfDesignation",getDsig(designationMasterList));
			/*
			 * get all department
			 */
			ArrayList deptMasterList=(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-department");
			req.getSession().setAttribute("deptmap",getDepartmentMap(deptMasterList));
			/*
			 * get all financial year
			 */
			List fYMasterList=EisManagersUtill.getCommonsService().getAllFinancialYearList();
			req.getSession().setAttribute("finMap",getFinMap(fYMasterList));
			/*
			 * get all leave master
			 */
			ArrayList typeMasterList=(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-TypeOfLeaveMaster");
			req.getSession().setAttribute("typeOfLeaveMap",employeeService.getMapForList(typeMasterList));
			
			/*
			 * get all calendar year
			 */
			List calMasterList=EisManagersUtill.getEmpLeaveService().getAllCalendarYearList();
			req.getSession().setAttribute("calendarMap", getCalendarMap(calMasterList));
			
		} catch (EGOVRuntimeException e) {
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
		}
		catch(Exception e)
		{
			
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
		}
		
		
	}
	public Map getDepartmentMap(ArrayList list)
	{
		LinkedHashMap<Integer, String> depMap = new LinkedHashMap<Integer, String>();
		try {
			for(Iterator iter = list.iterator();iter.hasNext();)
			{
				DepartmentImpl department = (DepartmentImpl)iter.next();
				depMap.put(department.getId(), department.getDeptName());
			}
		} catch (Exception e) {
			
			
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
		}
		return depMap;
	}
	private Map getDsig(ArrayList list)
	{
		LinkedHashMap<Integer, String> desMap = new LinkedHashMap<Integer, String>();
		try {
			for(Iterator iter = list.iterator();iter.hasNext();)
			{
				DesignationMaster desig = (DesignationMaster)iter.next();
				desMap.put(desig.getDesignationId(), desig.getDesignationName());
			}
		} catch (Exception e) {
			
			
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
		}
		return desMap;
		
	}
	public Map getFinMap(List list)
	{
		Map<Long,String> finMap = new TreeMap<Long,String>();
		try {
			for(Iterator iter = list.iterator();iter.hasNext();)
			{
				CFinancialYear cFinancialYear = (CFinancialYear)iter.next();
				finMap.put(cFinancialYear.getId(), cFinancialYear.getFinYearRange());
			}
		} catch (Exception e) {
			
			
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
		}
		return finMap;
	}
	
	public Map getCalendarMap(List list)
	{
		Map<Long,String> calendarMap = new TreeMap<Long,String>();
		try {
			for(Iterator iter = list.iterator();iter.hasNext();)
			{
				CalendarYear calendayYear = (CalendarYear)iter.next();
				calendarMap.put(calendayYear.getId(), calendayYear.getCalendarYear());
			}
		} catch (Exception e) {
			
			
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
		}
		return calendarMap;
	}
	
	
	public EmployeeService getEmployeeService() {
		return employeeService;
	}
	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	private final static String STR_EXCEPTION = "Exception:";
	
}
