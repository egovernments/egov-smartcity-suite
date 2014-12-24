/*
 * @(#)BeforeRegAction.java	1.00 Jan 31 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pims.commons.client;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.ObjectType;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.pims.commons.Position;
import org.egov.pims.commons.PositionHeirarchy;
import org.egov.pims.commons.dao.PositionMasterDAO;
import org.egov.pims.commons.service.EisCommonsService;
import org.egov.pims.utils.EisManagersUtill;

public class BeforePositionHeirarchyMasterAction extends DispatchAction
{
	private static  final Logger logger = Logger.getLogger(BeforeDesignationMasterAction.class);
	private EisCommonsService eisCommonsService;
	

	public ActionForward beforCreate(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws IOException,ServletException
	{
		String target = "";
		try
	    {
			populate(req);
			req.getSession().setAttribute("mode","create");
		 	target = "showObjectTypeScreen"; 
		}
		catch(Exception e)
		{

			target = "error";
			logger.debug(e.getMessage());
			HibernateUtil.rollbackTransaction();
			 throw new EGOVRuntimeException(e.getMessage(),e);

		}
		return mapping.findForward(target);
	}

	public ActionForward setIdForDetails(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws IOException,ServletException
	{
		String target = "";
		String alertMessage=null;
		PosForm PosForm = (PosForm)form;
		Set tpSet = new HashSet();
		try
		{
			String Id= req.getParameter("Id");
			String positionFrom = PosForm.getPositionFrom()[0];
			String positionTo = PosForm.getPositionTo()[0];
			Integer posFrom = null;
			Integer posTo = null;
			
			if(!StringUtils.trimToEmpty(positionFrom).equals(""))
			{
				posFrom = new Integer(positionFrom);
				req.setAttribute("pos", "positionFrom");
			}
			
			if(!StringUtils.trimToEmpty(positionTo).equals(""))
			{
				posTo = new Integer(positionTo);
				req.setAttribute("pos", "positionTo");
			}
			
			if(!StringUtils.trimToEmpty(Id).equals(""))
			{
				
					tpSet = EisManagersUtill.getEisCommonsService().getSetOfPositionByPositionsAndObjectType(posFrom,posTo,new Integer(Id));
					if(tpSet!=null && tpSet.size() > 100)	
					{
						target="listExceeds100";
						alertMessage = "List has exceeded 100. Please refine your Search.";		
					}
					else
					{
						req.setAttribute("tpSetList",tpSet);
						target = "createScreen";
					}
							
					populate(req);
					req.getSession().setAttribute("Id",req.getParameter("Id"));
					req.getSession().setAttribute("viewMode",req.getParameter("viewMode"));

			}
			
		}
		catch(Exception e)
		{
			target = "error";
			logger.debug(e.getMessage());
			HibernateUtil.rollbackTransaction();
			 throw new EGOVRuntimeException(e.getMessage(),e);

		}
		req.setAttribute("alertMessage", alertMessage);
		return mapping.findForward(target);
	}
	public ActionForward saveValues(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws IOException,ServletException
	{
		String target = "";
		String positionFrom[]= null;
		String positionTo[]= null;
		String posHirId[]= null;
		try {
			PositionMasterDAO positionMasterDAO = new PositionMasterDAO();
			
			PosForm PosForm = (PosForm)form;
			positionFrom=PosForm.getPositionFrom();
			positionTo=PosForm.getPositionTo();
			posHirId=PosForm.getPositionHeirId();
			int ArrLennmtst = posHirId.length;
			List position = new ArrayList();
			if(!positionFrom[0].equals("0"))
			{
				for (int i = 0; i < ArrLennmtst; i++)
				{
					PositionHeirarchy positionHeirarchy = new PositionHeirarchy();
					if(!positionFrom[i].equals("0"))
					{
						positionHeirarchy.setId(new Integer(posHirId[i]));
						if(positionFrom[i]!=null&&!positionFrom[i].equals(""))
						{
							Position posFrom = positionMasterDAO.getPosition(new Integer(positionFrom[i]));
							positionHeirarchy.setPosFrom(posFrom);
						}
						if(positionTo[i]!=null&&!positionTo[i].equals(""))
						{
							Position posTo= positionMasterDAO.getPosition(new Integer(positionTo[i]));
							positionHeirarchy.setPosTo(posTo);
						}
						
						}
					position.add(positionHeirarchy);
				}
				
				Set tpSet = new HashSet();
				tpSet.addAll(position);
				req.setAttribute("tpSetList", tpSet);
			}
			
			target="viewPage";
		} catch (Exception e) {
			
			target = "error";
			logger.debug(e.getMessage());
			HibernateUtil.rollbackTransaction();
			 throw new EGOVRuntimeException(e.getMessage(),e);

		}
		return mapping.findForward(target);
	}
	private Map getPositionMap(ArrayList list)
	{
		Map depMap = new HashMap();
		for(Iterator iter = list.iterator();iter.hasNext();)
		{
			Position position = (Position)iter.next();
			depMap.put(position.getId(), position.getName());
		}
		return depMap;
	}
	private void populate(HttpServletRequest req) {
		ArrayList positionList=(ArrayList)EgovMasterDataCaching.getInstance().get("egi-Position");
		req.getSession().setAttribute("positionMap",getPositionMap(positionList));
		ArrayList objTypeList=(ArrayList)EgovMasterDataCaching.getInstance().get("egi-ObjectType");
		req.getSession().setAttribute("objTypeMap",getobjMap(objTypeList));


	}
	private Map getobjMap(List objTypeList)
	{
		Map depMap = new HashMap();
		for(Iterator iter = objTypeList.iterator();iter.hasNext();)
		{
			ObjectType objType = (ObjectType)iter.next();
			depMap.put(objType.getId(), objType.getType());
		}
		return depMap;
	}

	public EisCommonsService getEisCommonsService() {
		return eisCommonsService;
	}

	public void setEisCommonsService(EisCommonsService eisCommonsService) {
		this.eisCommonsService = eisCommonsService;
	}
	
}