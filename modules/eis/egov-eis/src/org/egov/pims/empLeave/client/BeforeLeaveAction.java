package org.egov.pims.empLeave.client;


import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.pims.empLeave.model.LeaveApplication;
import org.egov.pims.empLeave.service.EmpLeaveService;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;

public class BeforeLeaveAction extends DispatchAction
{
	private static  final Logger LOGGER = Logger.getLogger(BeforeLeaveAction.class);
	private EmployeeService employeeService;
    private EmpLeaveService eisLeaveService;
    
    
	public ActionForward beforeCreate(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws IOException,ServletException
	{
		String target = "";  
		
		try
	    {
			LeaveForm leaveForm=(LeaveForm)form;
			saveToken(req);
			req.getSession().setAttribute("viewMode","create");
			ArrayList statusList=(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-StatusMaster");
			req.getSession().setAttribute("statusMap",employeeService.getMapForList(statusList));
			ArrayList deptMasterList=(ArrayList)EgovMasterDataCaching.getInstance().get("egi-department");
			req.getSession().setAttribute("departmentList",deptMasterList);
			
			ArrayList desgList=(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-DesignationMaster");
			req.getSession().setAttribute("designationList",desgList);
			
			String appened="";
			setEmpIdToReq(req,leaveForm);
			if(req.getParameter("ess")!=null && req.getParameter("ess").equals("1")) 
			 {
				leaveForm.setEss("1");
			 }
			
			if(req.getParameter(STR_MASTER)!=null)
			{
				appened = req.getParameter(STR_MASTER).trim();
			}
			/*
			else
				appened = ((String)req.getSession().getAttribute(STR_MASTER)).trim(); 
			*/
			//req.setAttribute("", eisLeaveService.getleaveTypesForDesignation(Integer.valueOf(leaveForm.getDesigId())); 
			target = "createScreen"+appened;
			LOGGER.info("target"+target);
		}
		catch(Exception ex)
		{

			target = "error";
			LOGGER.debug("Exception Encountered!!!"+ex.getMessage());
			throw new EGOVRuntimeException("Exception:" + ex.getMessage(),ex);
		}
		return mapping.findForward(target);
	}

	public ActionForward setIdForDetailsModify(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws IOException,ServletException
	{
		String target = "";
		try
		{
			LeaveForm leaveForm=(LeaveForm)form;
			saveToken(req);
			req.getSession().setAttribute("viewMode","modify");
			String appened="";
			setDesigIdToFormBean(req,leaveForm); 
			
			if(req.getParameter(STR_MASTER)!=null)
			{
				appened = req.getParameter(STR_MASTER).trim();
			}
			/*
			else
				appened = ((String)req.getSession().getAttribute(STR_MASTER)).trim();
*/
			target = "createScreen"+appened;
		}
		catch(Exception e)
		{
			target = "error";
			LOGGER.debug("Exception Encountered!!!"+e.getMessage());
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}

		return mapping.findForward(target);

	}
	public ActionForward setIdForDetailsView(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws IOException,ServletException
	{ 
		String target = "";
		try
		{
			LeaveForm leaveForm=(LeaveForm)form;
			req.getSession().setAttribute("viewMode","view");
			String appened="";
			setDesigIdToFormBean(req,leaveForm); 
			if(req.getParameter(STR_MASTER)!=null) 
			{
				appened = req.getParameter(STR_MASTER).trim();
			}
			/*
			else
				appened = ((String)req.getSession().getAttribute(STR_MASTER)).trim();
*/
			target = "createScreen"+appened; 

		}
		catch(Exception e)
		{
			target = "error";

			LOGGER.debug("Exception Encountered!!!"+e.getMessage());
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}

		
		return mapping.findForward(target);

	}
	private void setEmpIdToReq(ServletRequest req,LeaveForm leaveform)
	{
		Integer empId=null;
		if(req.getParameter("Id")==null)
		{
			 if(req.getParameter("ess")!=null && req.getParameter("ess").equals("1")) 
			 {
				if( employeeService.getEmpForUserId(Integer.valueOf(EGOVThreadLocals.getUserId()))==null)
				{
					throw new EGOVRuntimeException("Logged in User is not an employee  ");
				}
				else
				{
			 	empId=employeeService.getEmpForUserId(Integer.valueOf(EGOVThreadLocals.getUserId())).getId(); 
				}
			 }
		}
		else
		{
			empId = Integer.valueOf(req.getParameter("Id").trim()); 
		}
		 
		if(empId==null )
		{
			if(req.getParameter("leaveapplicationId")==null)
			{
				throw new EGOVRuntimeException("Id/leaveapplicationId or ess parameter not set in the request");
			}
		}
		else
		{
		req.setAttribute("Id",  empId);
		
		PersonalInformation egpimsPersonalInformation = employeeService.getEmloyeeById(empId);
		leaveform.setEmpCode(egpimsPersonalInformation.getEmployeeCode());
		leaveform.setEmployeeName(egpimsPersonalInformation.getEmployeeName());
		leaveform.setEmpId(egpimsPersonalInformation.getIdPersonalInformation().toString());
		 Assignment ass = employeeService.getLatestAssignmentForEmployee(egpimsPersonalInformation.getIdPersonalInformation());
		 if(ass!=null && ass.getDesigId()!=null && ass.getDesigId().getDesignationId()!=null )
		 {
			 leaveform.setDesigId( ass.getDesigId().getDesignationId().toString()); 
		 }
		}
	}
	private void setDesigIdToFormBean(ServletRequest req,LeaveForm leaveForm) 
	{
		Integer leaveAppId=null;
		LeaveApplication leaveApp=null;
		if(req.getParameter("leaveapplicationId")==null  )
		{
			 if(req.getParameter("ess")!=null && req.getParameter("ess").equals("1")) 
			 {
				 leaveApp=(LeaveApplication)req.getAttribute("leaveApplication");
				 leaveAppId=Integer.valueOf(leaveApp.getId().toString()); 
			 }
			 else if(req.getAttribute("leaveApplication")!=null)
			 {
				 leaveApp=(LeaveApplication)req.getAttribute("leaveApplication");
				 leaveAppId=Integer.valueOf(leaveApp.getId().toString()); 
			 }
		}
		else
		{
			leaveAppId = Integer.valueOf(req.getParameter("leaveapplicationId").trim());
			
		}
		if(leaveAppId!=null){
			leaveApp = eisLeaveService.getLeaveApplicationById(leaveAppId);
			leaveForm.setEmpCode(leaveApp.getEmployeeId().getEmployeeCode());
			leaveForm.setEmployeeName(leaveApp.getEmployeeId().getEmployeeName());
			leaveForm.setAvailableLeaves(leaveApp.getNoOfLeavesAvai().toString());
			leaveForm.setDesigId(leaveApp.getDesigId().getDesignationId().toString());
			leaveForm.setFromDate(leaveApp.getFromDate().toString());
			leaveForm.setToDate(leaveApp.getToDate().toString());
			leaveForm.setDateFromDate(leaveApp.getFromDate());//to show in jsp
			leaveForm.setDateToDate(leaveApp.getToDate());//to show in jsp
			leaveForm.setApplicationNumber(leaveApp.getApplicationNumber());
			leaveForm.setEncashment(leaveApp.getTypeOfLeaveMstr().getIsEncashable());
			leaveForm.setTypeOfLeaveMstr(leaveApp.getTypeOfLeaveMstr().getId().toString());
			leaveForm.setReason(leaveApp.getReason());
			leaveForm.setWorkingDays(leaveApp.getWorkingDays().toString());
			}
		/*
		if(req.getParameter("leaveapplicationId")==null  )
		{
			 if(req.getParameter("ess")!=null && req.getParameter("ess").equals("1")) 
			 {
				 leaveApp=(LeaveApplication)req.getAttribute("leaveApplication"); 
				 leaveAppId=Integer.valueOf(leaveApp.getId().toString()); 
				// req.setAttribute("ess","1");
			 }
			 else if(req.getAttribute("leaveApplication")!=null)
			 {
				 leaveApp=(LeaveApplication)req.getAttribute("leaveApplication");
				 leaveAppId=Integer.valueOf(leaveApp.getId().toString()); 
			 }
		}
		else
		{
			leaveAppId = Integer.valueOf(req.getParameter("leaveapplicationId").trim());
			
		}
		req.setAttribute("leaveapplicationId",  leaveAppId);*/
	}
	
	private final static String STR_MASTER="master";


	public EmployeeService getEmployeeService() {
		return employeeService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public EmpLeaveService getEisLeaveService() {
		return eisLeaveService;
	}

	public void setEisLeaveService(EmpLeaveService eisLeaveService) {
		this.eisLeaveService = eisLeaveService;
	}
	
	

}