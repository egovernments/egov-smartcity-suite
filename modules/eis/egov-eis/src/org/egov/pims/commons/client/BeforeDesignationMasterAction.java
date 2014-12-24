package org.egov.pims.commons.client;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import org.egov.commons.utils.EntityType;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.pims.commons.Position;

/*
 * @(#)BeforeRegAction.java	1.00 Jan 31 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
public class BeforeDesignationMasterAction extends DispatchAction {
	private static final Logger LOGGER = Logger.getLogger(BeforeDesignationMasterAction.class);
	private PersistenceService persistenceService;

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	public ActionForward beforCreate(ActionMapping mapping, ActionForm form, HttpServletRequest req,
			HttpServletResponse res) throws IOException, ServletException {
		String target = "";
		try {
			saveToken(req);
			ArrayList deptMasterList = (ArrayList) EgovMasterDataCaching.getInstance().get("egi-department");
			List<EntityType> drawingOffList = getDrawingOfficerList();
			req.getSession().setAttribute("drawingOffList", drawingOffList);
			req.getSession().setAttribute("deptmap", getDepartmentMap(deptMasterList));
			req.getSession().setAttribute(STR_VIEWMODE, "create");
			target = "createScreen";
		} catch (Exception e) {

			target = STR_ERROR;
			LOGGER.error("error--" + e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(), e);

		}
		return mapping.findForward(target);
	}

	public ActionForward beforeModify(ActionMapping mapping, ActionForm form, HttpServletRequest req,
			HttpServletResponse res) throws IOException, ServletException {
		String target = "";
		try {
			LOGGER.info(">>> inside beforeModify");
			List<EntityType> drawingOffList = getDrawingOfficerList();
			req.getSession().setAttribute("drawingOffList", drawingOffList);
			target = "modify";
			req.getSession().setAttribute("mode", "modify");
		} catch (Exception e) {
			target = STR_ERROR;
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(), e);

		}
		return mapping.findForward(target);
	}

	public ActionForward beforDelete(ActionMapping mapping, ActionForm form, HttpServletRequest req,
			HttpServletResponse res) throws IOException, ServletException {
		String target = "";
		try {
			LOGGER.info(">>> inside beforView");
			target = "delete";
			List<EntityType> drawingOffList = getDrawingOfficerList();
			req.getSession().setAttribute("drawingOffList", drawingOffList);
			req.getSession().setAttribute("mode", "delete");
		} catch (Exception e) {
			target = STR_ERROR;
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(), e);

		}
		return mapping.findForward(target);
	}

	public ActionForward beforView(ActionMapping mapping, ActionForm form, HttpServletRequest req,
			HttpServletResponse res) throws IOException, ServletException {
		String target = "";
		try {
			LOGGER.info(">>> inside beforView");
			target = "view";
			List<EntityType> drawingOffList = getDrawingOfficerList();
			req.getSession().setAttribute("drawingOffList", drawingOffList);
			req.getSession().setAttribute("mode", "view");
		} catch (Exception e) {
			target = STR_ERROR;
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(), e);

		}
		return mapping.findForward(target);
	}

	public ActionForward setIdForDetails(ActionMapping mapping, ActionForm form, HttpServletRequest req,
			HttpServletResponse res) throws IOException, ServletException {
		String target = "";
		try {
			LOGGER.info(">>> req.getParameter(Id) " + req.getParameter("Id") + " mode  "
					+ req.getParameter(STR_VIEWMODE));
			req.getSession().setAttribute("Id", req.getParameter("Id"));
			req.getSession().setAttribute(STR_VIEWMODE, req.getParameter(STR_VIEWMODE));
			List<EntityType> drawingOffList = getDrawingOfficerList();
			req.getSession().setAttribute("drawingOffList", drawingOffList);
			target = "createScreen";
		} catch (Exception e) {
			target = STR_ERROR;
			LOGGER.error("Exception Encountered!!!" + e.getMessage());
			throw new EGOVRuntimeException("Exception occured -----> " + e.getMessage());

		}
		return mapping.findForward(target);
	}

	public Map getDepartmentMap(ArrayList list) {
		Map depMap = new HashMap();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			DepartmentImpl department = (DepartmentImpl) iter.next();
			depMap.put(department.getId(), department.getDeptName());
		}
		return depMap;
	}

	public ActionForward checkForDuplicateDO(ActionMapping mapping, ActionForm form, HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		LOGGER.info("BeforeDesignationMasterAction.checkForDuplicateDO--------");
		Integer doId = Integer.parseInt(req.getParameter("doId"));
		EntityType drawingOfficer = (EntityType) persistenceService.find("from DrawingOfficer where id=?", doId);
		Position position = (Position) persistenceService
				.find("from Position where drawingOfficer = ?", drawingOfficer);
		if (position != null) {
			if (!"".equals(req.getParameter("posId"))) {
				Integer positionId = Integer.parseInt(req.getParameter("posId"));
				if (positionId.equals(position.getId())) {
					res.getWriter().write("true");
				} else {
					res.getWriter().write("false");
				}
			} else {
				res.getWriter().write("false");
			}
		} else {
			res.getWriter().write("true");
		}
		return null;
	}

	private List getDrawingOfficerList() {
		return persistenceService.findAllBy("from DrawingOfficer order by code");
	}

	private final static String STR_VIEWMODE = "viewMode";
	private final static String STR_ERROR = "error";
}