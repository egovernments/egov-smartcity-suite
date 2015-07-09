/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency, 
 *    accountability and the service delivery of the government  organizations.
 * 
 *     Copyright (C) <2015>  eGovernments Foundation
 * 
 *     The updated version of eGov suite of products as by eGovernments Foundation 
 *     is available at http://www.egovernments.org
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or 
 *     http://www.gnu.org/licenses/gpl.html .
 * 
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 * 
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
/*
 * RecoverySetupAction.java Created on Oct 5, 2007
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.deduction.client;

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
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Iliyaraja S
 * @version 1.00
 */
@Transactional(readOnly=true)
public class RecoverySetupAction extends DispatchAction {
	public static final Logger LOGGER = Logger.getLogger(RecoverySetupAction.class);
	
	public ActionForward toLoad(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		String target = "";
		try {
			//req.HibernateUtil.getCurrentSession().setAttribute("mode", "create");
			saveToken(req);
			target = "success";
		} catch (Exception ex) {
			target = "error";
			LOGGER.error("Exception encountered in toLoad", ex);
			 
		}
		return mapping.findForward(target);
	}
	
	public ActionForward toView(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		String target = "";
		try {
			req.getSession().setAttribute("mode", "searchView");
			target = "success";
		} catch (Exception ex) {
			target = "error";
			LOGGER.error("Exception encountered in toView", ex);
			 
			throw new EGOVRuntimeException("Exception encountered in toView", ex);
			
		}
		return mapping.findForward(target);
	}
	@Transactional
	public ActionForward toModify(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		String target = "";
		try {
			
			req.getSession().setAttribute("mode", "searchModify");
			target = "success";
		} catch (Exception ex) {
			target = "error";
			LOGGER.error("Exception encountered in toModify", ex);
			 
			throw new EGOVRuntimeException("Exception encountered in toModify", ex);
			
		}
		return mapping.findForward(target);
	}
	
	public ActionForward viewRecoveryMaster(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		String target = "";
		try {
			RecoverySetupForm rsf = (RecoverySetupForm) form;
			RecoverySetupDelegate rsDelegate = new RecoverySetupDelegate();
			rsDelegate.getRecoveryAndPopulateBean(rsf);
			
			req.getSession().setAttribute("mode", "view");
			target = "success";
			
		} catch (Exception ex) {
			target = "error";
			LOGGER.error("Exception encountered in viewRecoveryMaster", ex);
			 
			throw new EGOVRuntimeException("Exception encountered in viewRecoveryMaster", ex);
			
		}
		return mapping.findForward(target);
	}
	
	public ActionForward beforeModifyRecoveryMaster(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		String target = "";
		try {
			saveToken(req);
			RecoverySetupForm rsf = (RecoverySetupForm) form;
			RecoverySetupDelegate rsDelegate = new RecoverySetupDelegate();
			rsDelegate.getRecoveryAndPopulateBean(rsf);
			req.getSession().setAttribute("mode", "modify");
			target = "success";
			
		} catch (Exception ex) {
			target = "error";
			LOGGER.error("Exception encountered in beforeModifyRecoveryMaster", ex);
			 
			throw new EGOVRuntimeException("Exception encountered in beforeModifyRecoveryMaster", ex);
			
		}
		return mapping.findForward(target);
	}
	@Transactional
	public ActionForward createRecoveryMaster(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		String target = "";
		String alertMessage = null;
		try {
			if (isTokenValid(req) ) {
				resetToken(req);
			RecoverySetupForm rsf = (RecoverySetupForm) form;
			RecoverySetupDelegate rsDelegate = new RecoverySetupDelegate();
			Integer userId = (Integer) req.getSession().getAttribute("com.egov.user.LoginUserId");
			rsDelegate.createRecovery(rsf, userId);
			
			req.setAttribute("buttonType", req.getParameter("button"));
		HibernateUtil.getCurrentSession().flush();
			alertMessage = "Executed successfully";
			target = "success";
		} 
		else{
			target="tokenerror";
		}
		}
			catch (Exception ex) {
			target = "error";
			LOGGER.error("Exception encountered in createRecoveryMaster", ex);
			 
			throw new EGOVRuntimeException("Exception encountered in createRecoveryMaster", ex);
			
		}
		req.setAttribute("alertMessage", alertMessage);
		return mapping.findForward(target);
	}
	@Transactional
	public ActionForward modifyRecoveryMaster(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		String target = "";
		String alertMessage = null;
		try {
			if (isTokenValid(req) ) {
				resetToken(req);
			RecoverySetupForm rsf = (RecoverySetupForm) form;
			RecoverySetupDelegate rsDelegate = new RecoverySetupDelegate();
			Integer userId = (Integer) req.getSession().getAttribute("com.egov.user.LoginUserId");
			
			rsDelegate.modifyRecovery(rsf, userId);
			
			req.setAttribute("buttonType", req.getParameter("button"));
			//HibernateUtil.getCurrentSession().flush();
			alertMessage = "Updated successfully";
			target = "success";
		}else{
			target="tokenerror";
		}
			}
		catch (Exception ex) {
			target = "error";
			LOGGER.error("Exception encountered in modifyRecoveryMaster", ex);
			 
			throw new EGOVRuntimeException("Exception encountered in modifyRecoveryMaster", ex);
			
		}
		req.setAttribute("alertMessage", alertMessage);
		return mapping.findForward(target);
	}
	
}
