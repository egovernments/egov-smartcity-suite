package org.egov.pims.empLeave.client;

import java.io.IOException;

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
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.commons.dao.DesignationMasterDAO;
import org.egov.pims.empLeave.model.LeaveMaster;
import org.egov.pims.empLeave.model.TypeOfLeaveMaster;
import org.egov.pims.empLeave.service.EmpLeaveService;
/*
 * deepak yn
 * creates all the masters for an employee
 *
 *
 */
public class AfterLeaveMasterAction extends DispatchAction
{
	public final static Logger LOGGER = Logger.getLogger(AfterLeaveMasterAction.class.getClass());
	private EmpLeaveService eisLeaveService;
	public ActionForward saveLeaveMaster(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws IOException,ServletException
	{
	String target =null;
	String alertMessage=null;
	
		try
		{
			LeaveMasterForm leaveMasterForm = (LeaveMasterForm)form;
			String designationId = req.getParameter("designationId");
			DesignationMasterDAO designationMasterDAO = new DesignationMasterDAO();
			DesignationMaster designationMaster = designationMasterDAO.getDesignationMaster(Integer.valueOf(designationId.trim()).intValue());
			String[] typeOfLeaveMstr = leaveMasterForm.getTypeOfLeaveMstr();
			String[] leaveMstrId = leaveMasterForm.getLeaveMstrId();
			int ArrLen = typeOfLeaveMstr.length;
			LOGGER.info("ArrLen"+ArrLen);
			if(!typeOfLeaveMstr[0].equals("0"))
			{
				for (int len = 0; len < ArrLen; len++)
				{
					if(!typeOfLeaveMstr[len].equals("0"))
					{
						LOGGER.info("len "+len +"typeOfLeaveMstr["+len+"] "+typeOfLeaveMstr[len] );
						LeaveMaster leaveMaster = null;
						if(leaveMstrId[len].equals("0"))
						{
							leaveMaster = new LeaveMaster();
							leaveMaster.setDesigId(designationMaster);
							setFields(leaveMaster,leaveMasterForm,len);
							eisLeaveService.create(leaveMaster);
						}
						else
						{
							leaveMaster= eisLeaveService.getLeaveMasterById(Integer.valueOf(leaveMstrId[len]));
							setFields(leaveMaster,leaveMasterForm,len);
							eisLeaveService.updateLeaveMaster(leaveMaster);
						}

					}

				}
			}
			HibernateUtil.getCurrentSession().flush();
			req.getSession().setAttribute("mode","save");
			target = "success";
			alertMessage="Executed saving LeaveMaster successfully";

	}catch(Exception ex)
		{
		   target = "error";
		   LOGGER.info("Exception Encountered!!!"+ex.getMessage());
		   HibernateUtil.rollbackTransaction();
		   throw new EGOVRuntimeException("Exception:" + ex.getMessage(),ex);
		}
	
		req.setAttribute("alertMessage", alertMessage);
		return mapping.findForward(target);
	}

	private void setFields(LeaveMaster leaveMaster,LeaveMasterForm leaveMasterForm,int len)
	{
		try {
			String[] typeOfLeaveMstr = null;
			String[] noOfDays = null;
			typeOfLeaveMstr = leaveMasterForm.getTypeOfLeaveMstr();
			noOfDays = leaveMasterForm.getNoOfDays();
			if(!typeOfLeaveMstr[len].equals("0"))
			{
				TypeOfLeaveMaster typeOfLeaveMaster =eisLeaveService.getTypeOfLeaveMasterById(Integer.valueOf(typeOfLeaveMstr[len]));
				leaveMaster.setTypeOfLeaveMstr(typeOfLeaveMaster);
			}
			if(noOfDays[len]!=null&&!noOfDays[len].equals(""))
			{
				leaveMaster.setNoOfDays(Integer.valueOf(noOfDays[len]));
			}
		} catch (Exception e) {
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}
		
	}

	public EmpLeaveService getEisLeaveService() {
		return eisLeaveService;
	}

	public void setEisLeaveService(EmpLeaveService eisLeaveService) {
		this.eisLeaveService = eisLeaveService;
	}
	
	
	
	
}
