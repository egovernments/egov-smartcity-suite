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

/**
 * @author Iliyaraja S
 * @version 1.00
 */

public class RecoverySetupAction extends DispatchAction {
	public static final Logger LOGGER = Logger.getLogger(RecoverySetupAction.class);
	
	public ActionForward toLoad(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		String target = "";
		try {
			req.getSession().setAttribute("mode", "create");
			target = "success";
		} catch (Exception ex) {
			target = "error";
			LOGGER.error("Exception encountered in toLoad", ex);
			HibernateUtil.rollbackTransaction();
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
			HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception encountered in toView", ex);
			
		}
		return mapping.findForward(target);
	}
	
	public ActionForward toModify(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		String target = "";
		try {
			
			req.getSession().setAttribute("mode", "searchModify");
			target = "success";
		} catch (Exception ex) {
			target = "error";
			LOGGER.error("Exception encountered in toModify", ex);
			HibernateUtil.rollbackTransaction();
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
			HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception encountered in viewRecoveryMaster", ex);
			
		}
		return mapping.findForward(target);
	}
	
	public ActionForward beforeModifyRecoveryMaster(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		String target = "";
		try {
			
			RecoverySetupForm rsf = (RecoverySetupForm) form;
			RecoverySetupDelegate rsDelegate = new RecoverySetupDelegate();
			rsDelegate.getRecoveryAndPopulateBean(rsf);
			req.getSession().setAttribute("mode", "modify");
			target = "success";
			
		} catch (Exception ex) {
			target = "error";
			LOGGER.error("Exception encountered in beforeModifyRecoveryMaster", ex);
			HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception encountered in beforeModifyRecoveryMaster", ex);
			
		}
		return mapping.findForward(target);
	}
	
	public ActionForward createRecoveryMaster(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		String target = "";
		String alertMessage = null;
		try {
			
			RecoverySetupForm rsf = (RecoverySetupForm) form;
			RecoverySetupDelegate rsDelegate = new RecoverySetupDelegate();
			Integer userId = (Integer) req.getSession().getAttribute("com.egov.user.LoginUserId");
			rsDelegate.createRecovery(rsf, userId);
			
			req.setAttribute("buttonType", req.getParameter("button"));
			HibernateUtil.getCurrentSession().flush();
			alertMessage = "Executed successfully";
			target = "success";
		} catch (Exception ex) {
			target = "error";
			LOGGER.error("Exception encountered in createRecoveryMaster", ex);
			HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception encountered in createRecoveryMaster", ex);
			
		}
		req.setAttribute("alertMessage", alertMessage);
		return mapping.findForward(target);
	}
	
	public ActionForward modifyRecoveryMaster(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		String target = "";
		String alertMessage = null;
		try {
			RecoverySetupForm rsf = (RecoverySetupForm) form;
			RecoverySetupDelegate rsDelegate = new RecoverySetupDelegate();
			Integer userId = (Integer) req.getSession().getAttribute("com.egov.user.LoginUserId");
			
			rsDelegate.modifyRecovery(rsf, userId);
			
			req.setAttribute("buttonType", req.getParameter("button"));
			//HibernateUtil.getCurrentSession().flush();
			alertMessage = "Updated successfully";
			target = "success";
		} catch (Exception ex) {
			target = "error";
			LOGGER.error("Exception encountered in modifyRecoveryMaster", ex);
			HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception encountered in modifyRecoveryMaster", ex);
			
		}
		req.setAttribute("alertMessage", alertMessage);
		return mapping.findForward(target);
	}
	
}
