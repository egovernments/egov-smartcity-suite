package org.egov.pims.client;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.EgwStatus;
import org.egov.commons.Functionary;
import org.egov.infstr.services.EISServeable;
import org.egov.infstr.utils.EGovConfig;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.service.EmployeeService;
import org.egov.pims.utils.EisConstants;
import org.egov.pims.utils.EisManagersUtill;

/*
 *@(#)BeforeRegAction.java	1.00 Jan 31 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

public class BeforeSearchAction  extends DispatchAction
{
	private static  final Logger LOGGER = Logger.getLogger(BeforeSearchAction.class);
	private EmployeeService employeeService;
	private static final String STR_MODULE="module";
	private static final String STR_EXCEPTION= "Exception:";
	private EISServeable eisService;
	List<String> mandatoryFields = new ArrayList<String>(); 
	

	public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws IOException,ServletException
	{
		String target = "searchEmployee";
		try
	    {
			
			String id ="";
			if(req.getParameter(STR_MODULE)!=null)
			{
				req.setAttribute(STR_MODULE,req.getParameter(STR_MODULE));
			}
			if(((String)req.getAttribute(STR_MODULE)).equals("Employee")||((String)req.getAttribute(STR_MODULE)).equals("Pension"))
			{
				target = "searchEmployee";
			}
			else if(((String)req.getAttribute(STR_MODULE)).equals("Attendence"))
			{
				setAttendanceAppConfigValuesinReq(req);
				target = "searchEmployeeForAttendence";
			}
			else if(((String)req.getAttribute(STR_MODULE)).equals("EmployeeRetirement"))
			{
				target = "searchEmployeeForRetirement";
			}
			else if(((String)req.getAttribute(STR_MODULE)).equals("EmployeeMatuarity"))
			{
				target = "searchEmployeeForMatuarity";
			}
			else if(((String)req.getAttribute("module")).equals("EmployeeByStatusAndDateRange"))
			{
				req.getSession().removeAttribute("searchForm");
				target = "searchEmployeeByStatusAndDateRange";
			}
			else if (((String)req.getAttribute(STR_MODULE)).equals("Payslip"))
			{
			     target= "empPayroll";
			}
			else
			{
				target = "attendenceReport";
			}
			if(req.getParameter("Id")==null)
			{
				
				id = (String)req.getAttribute("Id");
				LOGGER.debug("id"+id);
			}
			else
			{
				id = req.getParameter("Id");
				req.setAttribute("Id",id);
				LOGGER.debug("id"+id);
			}
			String viewMode ="";
			if(req.getParameter("mode")!=null)
			{
				viewMode = req.getParameter("mode");
				req.setAttribute("mode",viewMode);
			}
			String masters ="";
			if(req.getParameter("masters")!=null)
			{
				masters = req.getParameter("masters");
				req.setAttribute("masters",masters);
			}
			if(((String)req.getAttribute(STR_MODULE)).equals("Employee")&& 
					req.getParameter("masters")!=null && req.getParameter("masters").equals("Employee"))
			{
			}	
			populate(req);
		}
		catch(Exception e)
		{
			target = "error";
			LOGGER.error("Exception Encountered!!!"+e.getMessage());
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
		}
		
		return mapping.findForward(target);
	}

	private void populate(HttpServletRequest req)   
	{
		try {
			HttpSession session=req.getSession();
			EgovMasterDataCaching egovMasterCaching=EgovMasterDataCaching.getInstance();
			ArrayList designationMasterList=(ArrayList)egovMasterCaching.get("egEmp-DesignationMaster");
			session.setAttribute("designationMasterList", designationMasterList);			
			session.setAttribute("mapOfDesignation",getDsig(designationMasterList));
			ArrayList deptMasterList=(ArrayList)egovMasterCaching.get("egEmp-department");
			List fYMasterList=EisManagersUtill.getCommonsService().getAllFinancialYearList();
			session.setAttribute("finMap",getFinMap(fYMasterList));
			ArrayList functionaryMasterList=(ArrayList)egovMasterCaching.get("egEmp-Functionary");
			session.setAttribute("functionaryMap",getFunctionaryMap(functionaryMasterList));
			ArrayList<CFunction> functionMasterList=(ArrayList)egovMasterCaching.get("egEmp-function");
			session.setAttribute("functionMap",getFunctionMap(functionMasterList));
			
			session.setAttribute("deptList",getEisService().getDeptsForUser());
			session.setAttribute("allDeptList",deptMasterList);
			egovMasterCaching.removeFromCache("egEmp-EgwStatus");
			List<EgwStatus> statusMasterList=(ArrayList<EgwStatus>)egovMasterCaching.get("egEmp-EgwStatus");
			List<EgwStatus> tmpstatusMasterList = new ArrayList<EgwStatus>() ;
			if(((String)req.getAttribute(STR_MODULE)).equals("Employee")  && ( req.getParameter("mode").toString().equals("Modify") || ( req.getParameter("mode").toString().equals("Reports") && req.getAttribute("masters").toString().equals("Leave") ) || ( (req.getParameter("mode").toString().equals("Create") || req.getParameter("mode").toString().equals("View") || req.getParameter("mode").toString().equals("Modify")  )&& req.getAttribute("masters").toString().equals("LeaveApplication") ) ))
			{
				EgwStatus retiredObj = null;
				for(EgwStatus egwStatus : statusMasterList )
				{
					if(!egwStatus.getDescription().equals(EisConstants.STATUS_TYPE_RETIRED))
					{
						tmpstatusMasterList.add(egwStatus);
					}
				}
				req.getSession().setAttribute("statusMasterList",tmpstatusMasterList);
			}
			else
			{
				req.getSession().setAttribute("statusMasterList",statusMasterList);
			}
			
			ArrayList employeeStatusMasterList=(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-EmployeeStatusMaster");
			req.getSession().setAttribute("employeeStatusMasterList",employeeStatusMasterList);
		}catch (EGOVRuntimeException e) {
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}
	
	public Map getFunctionaryMap(ArrayList list)
	{
		LinkedHashMap depMap = new LinkedHashMap();
		try {
			for(Iterator iter = list.iterator();iter.hasNext();)
			{
				Functionary functionary = (Functionary)iter.next();
				depMap.put(functionary.getId(), functionary.getName());
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
		}
		return depMap;
	}
		
	public Map getFunctionMap(ArrayList<CFunction> functionList)
	{
		LinkedHashMap<Long, String> functionMap = new LinkedHashMap<Long, String>();
		for(CFunction functionObj : functionList )
		{
			functionMap.put(functionObj.getId(), functionObj.getName());
		}
		return functionMap;
	}
	
	public Map getDepartmentMap(ArrayList list)
	{
		LinkedHashMap depMap = new LinkedHashMap();
		try {
			for(Iterator iter = list.iterator();iter.hasNext();)
			{
				DepartmentImpl department = (DepartmentImpl)iter.next();
				depMap.put(department.getId(), department.getDeptName());
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
		}
		return depMap;
	}
	
	private Map getDsig(ArrayList list)
	{
		LinkedHashMap desMap = new LinkedHashMap();
		try {
			for(Iterator iter = list.iterator();iter.hasNext();)
			{
				DesignationMaster desig = (DesignationMaster)iter.next();
				desMap.put(desig.getDesignationId(), desig.getDesignationName());
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
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
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
		}
		return finMap;
	}
	
	/**
	 * @return the employeeService
	 */
	public EmployeeService getEmployeeService() {
		return employeeService;
	}

	/**
	 * @param employeeService the employeeService to set
	 */
	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public EISServeable getEisService() {
		return eisService;
	}

	public void setEisService(EISServeable eisService) {
		this.eisService = eisService;
	}

	
	
	private void setAttendanceAppConfigValuesinReq(HttpServletRequest req){
		
		String isCompOffEnabled = EGovConfig.getAppConfigValue("EIS", "CompOffEnabled", "Y");
		req.setAttribute("CompOffEnabled", isCompOffEnabled);
		String isOTEnabled = EGovConfig.getAppConfigValue("EIS", "OTEnabled", "Y");
		req.setAttribute("OTEnabled", isOTEnabled);
	}
	

	
}