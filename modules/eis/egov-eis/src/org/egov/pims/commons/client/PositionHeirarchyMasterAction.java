/*
 * RegisterAction.java	1.00 Jan 31 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pims.commons.client;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.exceptions.DuplicateElementException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.ObjectType;
import org.egov.commons.dao.ObjectTypeDAO;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.pims.commons.Position;
import org.egov.pims.commons.PositionHeirarchy;
import org.egov.pims.commons.dao.PositionHeirarchyDAO;
import org.egov.pims.commons.dao.PositionHeirarchyHibernateDAO;
import org.egov.pims.commons.dao.PositionMasterDAO;
import org.egov.pims.commons.service.EisCommonsService;
import org.egov.pims.utils.EisManagersUtill;

public class PositionHeirarchyMasterAction extends DispatchAction
{
	public final static  Logger LOGGER = Logger.getLogger(PositionHeirarchyMasterAction.class.getClass());
    private EisCommonsService eisCommonsService;
	
	public void setEisCommonsService(EisCommonsService eisCommonsService) {
		this.eisCommonsService = eisCommonsService;
	}


	public ActionForward saveDetails(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	 throws IOException,ServletException
	{  
		String target =null;
		String alertMessage=null;
		String strErrorMsg;
		try
		{
			EgovMasterDataCaching.getInstance().removeFromCache("egi-Position");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-ObjectType");
			setPosition(req);
			target = "success";
			alertMessage="Executed successfully";

		}
		catch(DuplicateElementException invalidExp)
		{
			
			strErrorMsg = invalidExp.getMessage();
			target="failure";
			alertMessage=strErrorMsg;
		}
		catch(Exception ex)
		{
		   target = "error";
		   LOGGER.info("Exception Encountered!!!"+ex.getMessage());
		   throw new EGOVRuntimeException("Exception:" + ex.getMessage(),ex);
		   
		  
		}
		req.setAttribute("alertMessage", alertMessage);
		return mapping.findForward(target);
}
	
private void setPosition(HttpServletRequest req)throws DuplicateElementException
{
		String tpId[]=req.getParameterValues("positionHeirId");
		ObjectTypeDAO objectTypeDAO = new ObjectTypeDAO();
		PositionMasterDAO positionMasterDAO = new PositionMasterDAO();
		String objId= req.getParameter("objId").trim();
		req.getSession().setAttribute("objId", objId);
		//getAllDaos(objId,PositionhHierarchy pos,);
		String positionFrom[]= req.getParameterValues("positionFrom");
		String positionTo[]= req.getParameterValues("positionTo");
		String deletePositionSet[]= req.getParameterValues("deletePositionSet");
		PositionHeirarchy position =null;
		int ArrLennmtst = tpId.length;
		try{
		if(!positionFrom[0].equals("0"))
		{
			for (int len = 0; len < ArrLennmtst; len++)
			{
				if(!positionFrom[len].equals("0")&&!positionFrom[len].equals(""))
				{
					if(tpId[len].equals("0"))
					{
						position =new PositionHeirarchy();
						getPositionHierarchy(positionFrom[len],positionTo[len],position,objId,objectTypeDAO,positionMasterDAO);
						EisManagersUtill.getEisCommonsService().createPositionHeirarchy(position);
					}
					else
		 			{
						position = EisManagersUtill.getEisCommonsService().getPositionHeirarchyById(Integer.valueOf(tpId[len]));
						getPositionHierarchy(positionFrom[len],positionTo[len],position,objId,objectTypeDAO,positionMasterDAO);
						EisManagersUtill.getEisCommonsService().updatePositionHeirarchy(position);
					}
				}
			}
			//get the actions to be deleted
			PositionHeirarchyDAO objDao = (PositionHeirarchyDAO) new PositionHeirarchyHibernateDAO(PositionHeirarchy.class,HibernateUtil.getCurrentSession());
			if(deletePositionSet!=null)
			{
				for(int i=0;i<deletePositionSet.length;i++)
				{
					if(deletePositionSet[i] != null && !deletePositionSet[i].equals(""))
					{
						position = EisManagersUtill.getEisCommonsService().getPositionHeirarchyById(Integer.valueOf(deletePositionSet[i]));
						if(position != null)
						{
							objDao.delete(position);
							LOGGER.info("<<<<<<< Action deleted Successfully !!");
															
						}

					}
					
				}
			}
			
			
		 }
		}
		catch(Exception e){
			   
			LOGGER.info("Exception Encountered!!!"+e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
		
		
	}
    protected PositionHeirarchy getPositionHierarchy(String posFrom,String posTo,PositionHeirarchy posHierarchy,String objId,ObjectTypeDAO objectTypeDAO,PositionMasterDAO positionMasterDAO)
    {
		ObjectType objectType =  objectTypeDAO.getObjectType(Integer.valueOf(objId));
		try 
		{
	    	if(posFrom!=null&&!posFrom.equals(""))
			{
				
				Position posFromm = positionMasterDAO.getPosition(Integer.valueOf(posFrom));
	    		posHierarchy.setPosFrom(posFromm);
			}
		
			if(posTo!=null&&!posTo.equals(""))
			{
				Position posToo= positionMasterDAO.getPosition(Integer.valueOf(posTo));
				posHierarchy.setPosTo(posToo);
			}
		
		boolean check=EisManagersUtill.getEisCommonsService().checkPositionHeirarchy(Integer.valueOf(posFrom), Integer.valueOf(posTo), Integer.valueOf(objectType.getId()),null);
		if(check)
		{
			throw new DuplicateElementException("Combination of PostionFrom and PositionTo Should be unique");
		}
		else
		{
			posHierarchy.setObjectType(objectType);
		}
    }
		catch (Exception e)
		{
			LOGGER.error(e.getMessage());
		}
		return posHierarchy;
    }

    


}

