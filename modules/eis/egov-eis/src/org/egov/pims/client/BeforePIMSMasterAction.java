                                            /*
 * @(#)BeforeRegAction.java	1.00 Jan 31 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package org.egov.pims.client;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.Bank;
import org.egov.commons.Bankbranch;
import org.egov.commons.CFunction;
import org.egov.commons.EgwStatus;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.commons.Position;
import org.egov.pims.model.EmployeeGroupMaster;
import org.egov.pims.model.GradeMaster;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;
import org.egov.pims.utils.EisConstants;


public class BeforePIMSMasterAction extends DispatchAction
{
	private static  final Logger LOGGER = Logger.getLogger(BeforePIMSMasterAction.class);
	private EmployeeService employeeService;
	PIMSForm pIMSForm = new PIMSForm();
	public ActionForward beforeCreate(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws IOException,ServletException
	{
		String target = "";
		try
	    {
			saveToken(req);

			populate(req);
			req.getSession().setAttribute("viewMode","create");
			String appened ="";
			if(req.getParameter(STR_MASTER)!=null)
			{
				appened = req.getParameter(STR_MASTER).trim();
			}

			/*
			else
				if((String)req.getSession().getAttribute(STR_MASTER)!=null)
					appened = ((String)req.getSession().getAttribute("master")).trim();
			*/
			req.setAttribute(STR_MASTER,appened);
			target = "createScreen"+appened;
		}
		catch(Exception e)
		{
			target = "error";
			LOGGER.error("Exception Encountered!!!"+e.getMessage());
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
		}
		return mapping.findForward(target);
	}

	public ActionForward setIdForDetailsModify(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws IOException,ServletException
	{
		String target = "";
		try
		{
			saveToken(req);
			populate(req);
			req.getSession().setAttribute("viewMode","modify");
			String appened ="";
			if(req.getParameter(STR_MASTER)!=null)
			{
				appened = req.getParameter(STR_MASTER).trim();
			}

			if (EisConstants.MODULE_EMPLOYEE.equals(appened))
			{
				// need to get Employee object
				Integer id= Integer.valueOf(req.getParameter("Id"));
				PersonalInformation emp= employeeService.getEmloyeeById(id);
				if(emp.getIsActive()==1)
				{
					pIMSForm.setEmployeeActiveCheckbox(true);
				}
				else
				{
					pIMSForm.setEmployeeActiveCheckbox(false);
				}
				req.setAttribute("isSuspended", employeeService.isEmployeeSuspended(emp.getId()));
				req.setAttribute("employeeOb", emp);
			}

			target = "createScreen"+appened;
		}
		catch(Exception e)
		{
			target = "error";
			LOGGER.error("Exception Encountered!!!"+e.getMessage());
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
		}

		return mapping.findForward(target);

	}

	public ActionForward setIdForDetailsView(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws IOException,ServletException
	{
		String target = "";
		try
		{
			populate(req);
			req.getSession().setAttribute("viewMode","view");
			String appened ="";

			if(req.getParameter(STR_MASTER)!=null)
			{
				appened = req.getParameter(STR_MASTER).trim();
			}
			if (EisConstants.MODULE_EMPLOYEE.equals(appened)) 
			{
				// need to get Employee object
				PersonalInformation emp=null;
				Integer id= Integer.valueOf(req.getParameter("Id")==null?EGOVThreadLocals.getUserId():req.getParameter("Id"));
				if(req.getParameter("Id")==null)
				{
					if(req.getParameter(EisConstants.ESS)!=null && !EisConstants.ESS.trim().equals("1") ) 
					{
					emp=employeeService.getEmpForUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
					}
				}
				else
				{
				 emp= employeeService.getEmloyeeById(Integer.valueOf(req.getParameter("Id")));
				}
				req.setAttribute("isSuspended", employeeService.isEmployeeSuspended(emp.getId()));
				req.setAttribute("employeeOb", emp);
			}

			target = "createScreen"+appened;
		}
		catch(Exception e)
		{
			target = "error";
			LOGGER.error(e.getMessage());
     		throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
		}

		return mapping.findForward(target);

	}
	private void populate(HttpServletRequest req)
	{
		try {
			//for lite
			ArrayList fundMasterList=(ArrayList)EgovMasterDataCaching.getInstance().get("egi-fund");
			req.getSession().setAttribute("fundMap",getFundMap(fundMasterList));
			ArrayList functionMasterList=(ArrayList)EgovMasterDataCaching.getInstance().get("egi-function");
			req.getSession().setAttribute("functionMap",getFunctionMap(functionMasterList));
			ArrayList functionaryMasterList=(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-Functionary");
			req.getSession().setAttribute("functionaryMap",getFunctionaryMap(functionaryMasterList));
			ArrayList deptMasterList=(ArrayList)EgovMasterDataCaching.getInstance().get("egi-department");
			req.getSession().setAttribute("deptmap",getDepartmentMap(deptMasterList));
			ArrayList designationMasterList=(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-DesignationMaster");
			req.getSession().setAttribute("mapOfDesignation",getDsig(designationMasterList));
			ArrayList posList=(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-Position");
			req.getSession().setAttribute("positionMap",getPositionMap(posList));
			ArrayList statusMasterList=(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-EgwStatus");
			req.getSession().setAttribute("statusMasterMap",getStatusMasterMap(statusMasterList));
			ArrayList gradeMasterList=(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-GradeMaster");
			req.getSession().setAttribute("gradeMap",getGradeMap(gradeMasterList));

			
			//if(payrollModule!=null && payrollModule.getIsEnabled())
			//{
			ArrayList bloodGroupList=(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-BloodGroupMaster");
			req.getSession().setAttribute("bloodGroupMap",employeeService.getMapForList(bloodGroupList));
			ArrayList langKnownMasterList=(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-LanguagesKnownMaster");
			req.getSession().setAttribute("langKnownMap",employeeService.getMapForList(langKnownMasterList));

			ArrayList employeeStatusMasterList=(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-EmployeeStatusMaster");
			req.getSession().setAttribute("employeeStatusMasterMap",employeeService.getMapForList(employeeStatusMasterList));

			ArrayList bankList=(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-Bank");
			req.getSession().setAttribute("bankMap",getBankMap(bankList));
			ArrayList branchList=(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-BANKBRANCH");
			req.getSession().setAttribute("branchMap",getBranchMap(branchList));

			ArrayList howAcquiredMasterList=(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-HowAcquiredMaster");
			req.getSession().setAttribute("howAcquiredMasterMap",employeeService.getMapForList(howAcquiredMasterList));
			ArrayList howAcqMasterList=(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-HowAcquiredMaster");
			req.getSession().setAttribute("mapOfHowAcq",employeeService.getMapForList(howAcqMasterList));
			ArrayList catMasterList=(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-CategoryMaster");
			req.getSession().setAttribute("catMap",employeeService.getMapForList(catMasterList));
			ArrayList commMasterList=(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-CommunityMaster");
			req.getSession().setAttribute("commMap",employeeService.getMapForList(commMasterList));

			ArrayList langQualiMasterList=(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-LanguagesQulifiedMaster");
			req.getSession().setAttribute("langQualiMap",employeeService.getMapForList(langQualiMasterList));
			ArrayList recruimentMasterMasterList=(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-RecruimentMaster");
			req.getSession().setAttribute("recruimentMasterMap",employeeService.getMapForList(recruimentMasterMasterList));
			ArrayList religionMasterList=(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-ReligionMaster");
			req.getSession().setAttribute("religionMap",employeeService.getMapForList(religionMasterList));
			ArrayList testNameMasterList=(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-TestNameMaster");
			req.getSession().setAttribute("testNameMasterMap",employeeService.getMapForList(testNameMasterList));
			ArrayList payFixedInMasterList=(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-PayFixedInMaster");
			req.getSession().setAttribute("payFixedInMaster",employeeService.getMapForList(payFixedInMasterList));
			ArrayList typeOfRecMasterList=(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-TypeOfRecruimentMaster");
			req.getSession().setAttribute("recruimentMap",employeeService.getMapForList(typeOfRecMasterList));
			ArrayList typeOfPostingMstrList=(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-PostingMaster");
			req.getSession().setAttribute("postingMap",employeeService.getMapForList(typeOfPostingMstrList));
			
			ArrayList skillMasterList=(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-SkillMaster");
			req.getSession().setAttribute("skillMap",employeeService.getMapForList(skillMasterList));

			ArrayList statusList=(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-StatusMaster");
			req.getSession().setAttribute("statusMap",employeeService.getMapForList(statusList));
			
			req.getSession().setAttribute("empUserNotMappedList", getEmpUserNotMappedList());
			
			ArrayList<EmployeeGroupMaster> grpMstrList = (ArrayList<EmployeeGroupMaster>) EgovMasterDataCaching.getInstance().get("egEmp-EmployeeGroupMaster");
			req.getSession().setAttribute("groupMasterMap",employeeService.getMapForList(grpMstrList));
			 

		} catch (EGOVRuntimeException e) {
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);

		}
	}
	
	
	private List getEmpUserNotMappedList()
	{
		List empNotMapUserId;
		try {
			empNotMapUserId = employeeService.getListOfUsersNotMappedToEmp();
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
		}
		return empNotMapUserId;
	}
	
	private Map getBankMap(ArrayList bankList) {
		LinkedHashMap depMap = new LinkedHashMap();
		try {
			for(Iterator iter = bankList.iterator();iter.hasNext();)
			{
				Bank bank = (Bank)iter.next();
				depMap.put(bank.getId(), bank.getName());
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
		}
		return depMap;
	}
	private Map getBranchMap(ArrayList branchList)
	{
		LinkedHashMap depMap = new LinkedHashMap();
		try {
			for(Iterator iter = branchList.iterator();iter.hasNext();)
			{
				Bankbranch branch = (Bankbranch)iter.next();
				depMap.put(branch.getId(), branch.getBranchname());
				LOGGER.debug(">>>>>>>>>>>>>>>branchmap"+depMap);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
		}
		return depMap;
	}





	private Map getPositionMap(ArrayList list)
	{
		LinkedHashMap depMap = new LinkedHashMap();
		try {
			for(Iterator iter = list.iterator();iter.hasNext();)
			{
				Position position = (Position)iter.next();
				depMap.put(position.getId(), position.getName());
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
		}
		return depMap;
	}
	
	private Map getGradeMap(ArrayList list)
	{
		LinkedHashMap gradeMap = new LinkedHashMap();
		try {
			for(Iterator iter = list.iterator();iter.hasNext();)
			{
				GradeMaster grade = (GradeMaster)iter.next();
				gradeMap.put(grade.getId(), grade.getName());
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
		}
		return gradeMap;
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
public Map getFundMap(ArrayList list)
{
	LinkedHashMap depMap = new LinkedHashMap();
	try {
		for(Iterator iter = list.iterator();iter.hasNext();)
		{
			Fund fund = (Fund)iter.next();
			depMap.put(fund.getId(), fund.getName());
		}
	} catch (Exception e) {
		LOGGER.error(e.getMessage());
		throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
	}
	return depMap;
}
public Map getFunctionMap(ArrayList list)
{
	LinkedHashMap depMap = new LinkedHashMap();
	try {
		for(Iterator iter = list.iterator();iter.hasNext();)
		{
			CFunction cFunction = (CFunction)iter.next();
			depMap.put(cFunction.getId(), cFunction.getName());
		}
	} catch (Exception e) {
		LOGGER.error(e.getMessage());
		throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
	}
	return depMap;
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

public Map getStatusMasterMap(ArrayList list)
{
	Map statusMasterMap = new HashMap();
	try {
		for(Iterator iter = list.iterator();iter.hasNext();)
		{
			EgwStatus statusMaster  = (EgwStatus)iter.next();
			statusMasterMap.put(statusMaster.getId(), statusMaster.getDescription());
		}
	} catch (Exception e) {
		LOGGER.error(e.getMessage());
		throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
	}
	return statusMasterMap;
}


	public EmployeeService getEmployeeService() {
		return employeeService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}



	private static final String STR_MASTER="master";

	private static final String STR_EXCEPTION= "Exception:";

}