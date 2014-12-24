package org.egov.pims.empLeave.client;


import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.pims.empLeave.dao.LeaveDAOFactory;
import org.egov.pims.empLeave.dao.TypeOfLeaveMasterDAO;
import org.egov.pims.empLeave.model.TypeOfLeaveMaster;
import org.egov.pims.empLeave.service.EmpLeaveService;
import org.egov.pims.service.EmployeeService;


public class AfterLeaveTypeAction extends DispatchAction
{

	public final static  Logger LOGGER = Logger.getLogger(AfterLeaveTypeAction.class.getClass());
	private EmpLeaveService eisLeaveService;
	private EmployeeService employeeService;
	public ActionForward saveDetails(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	 throws IOException,ServletException
	{
		String target =null;
		String alertMessage=null;
		
		try
		{    
			LeaveTypeForm leaveTypeForm = (LeaveTypeForm)form;
			TypeOfLeaveMaster typeOfLeaveMaster = new TypeOfLeaveMaster();
			TypeOfLeaveMasterDAO leaveMasterDAO = LeaveDAOFactory.getDAOFactory().getTypeOfLeaveMasterDAO();
		
				if(req.getParameter(STR_NAME)!=null&&!req.getParameter(STR_NAME).equals(""))
				{
					typeOfLeaveMaster.setName(req.getParameter(STR_NAME));
				}
				if(leaveTypeForm.getAccumulate().equals("0"))
				{
					typeOfLeaveMaster.setAccumulate(Character.valueOf('0'));
				}
				else
				{
					typeOfLeaveMaster.setAccumulate(Character.valueOf('1'));
				}
				if(leaveTypeForm.getPayElegible().equals("0"))
				{
					typeOfLeaveMaster.setPayElegible(Character.valueOf('0'));
				}
				else
				{
					typeOfLeaveMaster.setPayElegible(Character.valueOf('1'));
				}
				if(leaveTypeForm.getIsHalfDay().equals("0"))
				{
					typeOfLeaveMaster.setIsHalfDay(Character.valueOf('0'));
				}
				else
				{
					typeOfLeaveMaster.setIsHalfDay(Character.valueOf('1'));
				}
				if(leaveTypeForm.getIsEncashable().equals("0"))
				{
					typeOfLeaveMaster.setIsEncashable(Character.valueOf('0'));
				}
				else
				{
					typeOfLeaveMaster.setIsEncashable(Character.valueOf('1'));
				}
				leaveMasterDAO.create(typeOfLeaveMaster);
				EgovMasterDataCaching.getInstance().removeFromCache(STR_TYPEOFLEAVEMSTR);
			LOGGER.info(" inside saveDetails ");
		//	HibernateUtil.getCurrentSession().flush();
			target = "success";
			alertMessage="Leave Created Successfully";

		}
		catch(Exception ex)
		{
		   target = STR_ERROR;
		   LOGGER.error(ex.getMessage());
		   throw new EGOVRuntimeException(STR_EXCEPTION + ex.getMessage(),ex);
		}
		req.setAttribute(STR_ALERTMESSAGE, alertMessage);
		return mapping.findForward(target);
}



	public ActionForward modifyDetails(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	 throws IOException,ServletException
{

	String target =null;
	String alertMessage=null;

	try
	{
		LOGGER.info(" inside modifyDetails ");
		LeaveTypeForm leaveTypeForm = (LeaveTypeForm)form;
		TypeOfLeaveMasterDAO leaveMasterDAO = LeaveDAOFactory.getDAOFactory().getTypeOfLeaveMasterDAO();
		TypeOfLeaveMaster typeOfLeaveMaster = eisLeaveService.getTypeOfLeaveMasterById(Integer.valueOf(leaveTypeForm.getTypeId()));
			if(req.getParameter(STR_NAME)!=null&&!req.getParameter(STR_NAME).equals(""))
			{
				typeOfLeaveMaster.setName(req.getParameter(STR_NAME));
			}
			if(leaveTypeForm.getAccumulate().equals("0"))
			{
				typeOfLeaveMaster.setAccumulate(Character.valueOf('0'));
			}
			else
			{
				typeOfLeaveMaster.setAccumulate(Character.valueOf('1'));
			}
			if(leaveTypeForm.getPayElegible().equals("0"))
			{
				typeOfLeaveMaster.setPayElegible(Character.valueOf('0'));
			}
			else
			{
				typeOfLeaveMaster.setPayElegible(Character.valueOf('1'));
			}
			if(leaveTypeForm.getIsHalfDay().equals("0"))
			{
				typeOfLeaveMaster.setIsHalfDay(Character.valueOf('0'));
			}
			else
			{
				typeOfLeaveMaster.setIsHalfDay(Character.valueOf('1'));
			}
			if(leaveTypeForm.getIsEncashable().equals("0"))
			{
				typeOfLeaveMaster.setIsEncashable(Character.valueOf('0'));
			}
			else
			{
				typeOfLeaveMaster.setIsEncashable(Character.valueOf('1'));
			}
			leaveMasterDAO.update(typeOfLeaveMaster);
			EgovMasterDataCaching.getInstance().removeFromCache(STR_TYPEOFLEAVEMSTR);
		HibernateUtil.getCurrentSession().flush();
		target = "modifyfinal";
		alertMessage="Executed successfully";
	}
	catch(Exception ex)
	{
	   target = STR_ERROR;
	   LOGGER.info("Exception Encountered!!!"+ex.getMessage());
	   throw new EGOVRuntimeException(STR_EXCEPTION + ex.getMessage(),ex);
	}
	req.setAttribute(STR_ALERTMESSAGE, alertMessage);
	return mapping.findForward(target);

}

public ActionForward deleteDetails(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	 throws IOException,ServletException
{

	String target =null;
	String alertMessage=null;

	try
	{
		LOGGER.info(" inside modifyDetails ");
		LeaveTypeForm leaveTypeForm = (LeaveTypeForm)form;
		TypeOfLeaveMasterDAO leaveMasterDAO = LeaveDAOFactory.getDAOFactory().getTypeOfLeaveMasterDAO();
		TypeOfLeaveMaster typeOfLeaveMaster = eisLeaveService.getTypeOfLeaveMasterById(Integer.valueOf(leaveTypeForm.getTypeId()));
			if(req.getParameter(STR_NAME)!=null&&!req.getParameter(STR_NAME).equals(""))
			{
				typeOfLeaveMaster.setName(req.getParameter(STR_NAME));
			}
			if(leaveTypeForm.getAccumulate().equals("0"))
			{
				typeOfLeaveMaster.setAccumulate(Character.valueOf('0'));
			}
			else
			{
				typeOfLeaveMaster.setAccumulate(Character.valueOf('1'));
			}
			if(leaveTypeForm.getPayElegible().equals("0"))
			{
				typeOfLeaveMaster.setPayElegible(Character.valueOf('0'));
			}
			else
			{
				typeOfLeaveMaster.setPayElegible(Character.valueOf('1'));
			}
			if(leaveTypeForm.getIsHalfDay().equals("0"))
			{
				typeOfLeaveMaster.setIsHalfDay(Character.valueOf('0'));
			}
			else
			{
				typeOfLeaveMaster.setIsHalfDay(Character.valueOf('1'));
			}
			if(leaveTypeForm.getIsEncashable().equals("0"))
			{
				typeOfLeaveMaster.setIsEncashable(Character.valueOf('0'));
			}
			else 
			{
				typeOfLeaveMaster.setIsEncashable(Character.valueOf('1'));
			}
			leaveMasterDAO.update(typeOfLeaveMaster);
			EgovMasterDataCaching.getInstance().removeFromCache(STR_TYPEOFLEAVEMSTR);
		HibernateUtil.getCurrentSession().flush();
		target = "modifyfinal";
		alertMessage="Executed successfully";
	}
	catch(Exception ex)
	{
	   target = STR_ERROR;
	   LOGGER.info("Exception Encountered!!!"+ex.getMessage());
	   throw new EGOVRuntimeException(STR_EXCEPTION + ex.getMessage(),ex);
	}
	req.setAttribute(STR_ALERTMESSAGE, alertMessage);
	return mapping.findForward(target);

}
public ActionForward viewDetails(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
throws IOException,ServletException
{

	String target =null;
	String alertMessage=null;

	try
	{
		LOGGER.info(" inside modifyDetails ");
		LeaveTypeForm leaveTypeForm = (LeaveTypeForm)form;
		TypeOfLeaveMasterDAO leaveMasterDAO = LeaveDAOFactory.getDAOFactory().getTypeOfLeaveMasterDAO();
		TypeOfLeaveMaster typeOfLeaveMaster = eisLeaveService.getTypeOfLeaveMasterById(Integer.valueOf(leaveTypeForm.getTypeId()));
			if(req.getParameter(STR_NAME)!=null&&!req.getParameter(STR_NAME).equals(""))
			{
				typeOfLeaveMaster.setName(req.getParameter(STR_NAME));
			}
			if(leaveTypeForm.getAccumulate().equals("0"))
			{
				typeOfLeaveMaster.setAccumulate(Character.valueOf('0'));
			}
			else
			{
				typeOfLeaveMaster.setAccumulate(Character.valueOf('1'));
			}
			if(leaveTypeForm.getPayElegible().equals("0"))
			{
				typeOfLeaveMaster.setPayElegible(Character.valueOf('0'));
			}
			else
			{
				typeOfLeaveMaster.setPayElegible(Character.valueOf('1'));
			}
			if(leaveTypeForm.getIsHalfDay().equals("0"))
			{
				typeOfLeaveMaster.setIsHalfDay(Character.valueOf('0'));
			}
			else
			{
				typeOfLeaveMaster.setIsHalfDay(Character.valueOf('1'));
			}
			if(leaveTypeForm.getIsEncashable().equals("0"))
			{
				typeOfLeaveMaster.setIsEncashable(Character.valueOf('0'));
			}
			else
			{
				typeOfLeaveMaster.setIsEncashable(Character.valueOf('1'));
			}
			leaveMasterDAO.update(typeOfLeaveMaster);
			EgovMasterDataCaching.getInstance().removeFromCache(STR_TYPEOFLEAVEMSTR);
		HibernateUtil.getCurrentSession().flush();
		target = "modifyfinal";
		alertMessage="Executed successfully";
	}
	catch(Exception ex)
	{
	   target = STR_ERROR;
	   LOGGER.info("Exception Encountered!!!"+ex.getMessage());
	   throw new EGOVRuntimeException(STR_EXCEPTION + ex.getMessage(),ex);
	}
	req.setAttribute(STR_ALERTMESSAGE, alertMessage);
	return mapping.findForward(target);

}
private void populate(HttpServletRequest req)
{
	
	ArrayList typeLeaveMasterList=(ArrayList)EgovMasterDataCaching.getInstance().get(STR_TYPEOFLEAVEMSTR);
	req.getSession().setAttribute("mapOfLeaveMaster",employeeService.getMapForList(typeLeaveMasterList));
	
	
}



public EmpLeaveService getEisLeaveService() {
	return eisLeaveService;
}

public void setEisLeaveService(EmpLeaveService eisLeaveService) {
	this.eisLeaveService = eisLeaveService;
}

public EmployeeService getEmployeeService() {
	return employeeService;
}

public void setEmployeeService(EmployeeService employeeService) {
	this.employeeService = employeeService;
}



private final static String STR_TYPEOFLEAVEMSTR="egEmp-TypeOfLeaveMaster";
private final static String STR_NAME="name";
private final static String STR_EXCEPTION="Exception:";
private final static String STR_ALERTMESSAGE="alertMessage";
private final static String STR_ERROR="error";
}

