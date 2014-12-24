package org.egov.pims.empLeave.client;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.pims.empLeave.service.EmpLeaveService;
import org.egov.pims.utils.EisManagersUtill;

public class AfterOpeningBalanceAction extends DispatchAction 
{
	public final static Logger LOGGER = Logger.getLogger(AfterOpeningBalanceAction.class.getClass());
	private EmpLeaveService eisLeaveService;


	/**
	 * @return the eisLeaveManagr
	 */
	public EmpLeaveService getEisLeaveService() {
		return eisLeaveService;
	}


	/**
	 * @param eisLeaveManagr the eisLeaveManagr to set
	 */
	public void setEisLeaveService(EmpLeaveService eisLeaveService) {
		this.eisLeaveService = eisLeaveService;
	}
	public ActionForward executeSearch(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	 throws IOException,ServletException
	 {
		String target =null;
		@SuppressWarnings("unused")
		String alertMessage=null;
		
		try
		{
			LeaveBalanceForm leaveBalanceForm = (LeaveBalanceForm)form;
			String typeOfLeaveMstr=leaveBalanceForm.getTypeOfLeaveMstr();
			
   			String finYear="";
   			if(EisManagersUtill.getEmpLeaveService().isLeaveCalendarBased())
   				finYear = EisManagersUtill.getCommonsService().getCurrYearFiscalId();
   			else
   				finYear = leaveBalanceForm.getFinYear();
   			
   			req.getSession().setAttribute("typeOfLeaveMstr", typeOfLeaveMstr);
   			req.getSession().setAttribute("finYear", finYear);
			//EmpLeaveService eisLeaveManager = getEisLeaveManagr();
			
			List employeeList =(List)eisLeaveService.searchEmployeeForLeaveOpeningBalance(new Integer(leaveBalanceForm.getDepartmentId()),new Integer(leaveBalanceForm.getDesignationId()),leaveBalanceForm.getCode(),leaveBalanceForm.getName(),new Integer(finYear),new Integer(leaveBalanceForm.getTypeOfLeaveMstr()));
			if(employeeList!=null && employeeList.size() > 100)	
			{
				target="attExceeds100";
				alertMessage = "List of Employees exceeds 100. Please refine your Search.";		
			}
			else
			{
				req.setAttribute("employeeList",employeeList);
				target = "successEmp";
				
			}
		}
		catch(Exception ex)
		{
		   target = "error";
		   LOGGER.info("Exception Encountered from AfterBalance"+ex.getMessage());
		   HibernateUtil.rollbackTransaction();
		   throw new EGOVRuntimeException("Exception:" + ex.getMessage(),ex);
		}
		
		req.setAttribute("alertMessage", alertMessage);
		return mapping.findForward(target);
	
	 }
}
