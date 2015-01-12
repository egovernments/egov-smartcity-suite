/*
 * @(#)SetupBoundryAction.java 3.0, 18 Jun, 2013 2:24:49 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.client.adminBoundry;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.client.EgovAction;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.ejb.api.BoundaryService;
import org.egov.lib.admbndry.ejb.api.CityWebsiteService;
import org.egov.lib.admbndry.ejb.server.BoundaryServiceImpl;
import org.egov.lib.admbndry.ejb.server.CityWebsiteServiceImpl;

public class SetupBoundryAction extends EgovAction {
	private static final Logger LOG = LoggerFactory.getLogger(SetupBoundryAction.class);
	private final BoundaryService boundaryService = new BoundaryServiceImpl();
	private final CityWebsiteService cityWebsiteService = new CityWebsiteServiceImpl();

	/**
	 * This method is used to get all the top boundries and set the list in session Calls the setup method 
	 * in EgovAction class that sets a list of all the departments in the session
	 * @param ActionMapping mapping
	 * @param ActionForm form
	 * @param HttpServletRequest req
	 * @param HttpServletResponse res
	 * @return ActionForward
	 **/
	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {

		final BoundryForm boundryForm = (BoundryForm) form;
		String target = "";
		this.saveToken(req);
		if ("UPDATE".equals(req.getParameter("bool"))) {
			try {
				final HttpSession session = req.getSession();
				session.setAttribute("heirarchyType", Integer.parseInt(boundryForm.getHeirarchyType()));
				final int boundaryId = Integer.valueOf(boundryForm.getBndryId());
				final Boundary bndry = this.boundaryService.getBoundaryById(boundaryId);
				session.setAttribute("boundaryNum", bndry.getBoundaryNum());
				if (bndry.getParent() != null) {
					session.setAttribute("BndryIdValue", bndry.getParent().getId().intValue());
				} else {
					session.setAttribute("BndryIdValue", boundaryId);
				}

				session.setAttribute("bndryTypeHeirarchyLevel", (int) bndry.getBoundaryType().getHeirarchy());

				boundryForm.setName(bndry.getName());
				if (bndry.getBndryNameLocal() != null) {
					boundryForm.setCityNameLocal(bndry.getBndryNameLocal());
				}
				if (bndry.getBoundaryNum() != null) {
					boundryForm.setBoundaryNum(bndry.getBoundaryNum().toString());
				}
				final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				final SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
				boundryForm.setFromDate(formatter1.format(formatter.parse(bndry.getFromDate().toString())));
				if (bndry.getToDate() != null) {
					boundryForm.setToDate(formatter1.format(formatter.parse(bndry.getToDate().toString())));
				}
				final List cityList = this.cityWebsiteService.getCityWebsite(Integer.valueOf(boundaryId));
				req.getSession().setAttribute("cityList", cityList);
				target = "update";
				session.setAttribute("operation", "edit");
				session.setAttribute("bndry", bndry);

			} catch (final Exception c) {
				LOG.error("Boundary update cannot be processed due to an internal server error.", c);
				req.setAttribute("MESSAGE", "Boundary update cannot be processed due to an internal server error.");
				throw new EGOVRuntimeException("Boundary update cannot be processed due to an internal server error.");
			}

		} else if ("DELETE".equals(req.getParameter("bool"))) {

			try {
				final HttpSession session = req.getSession();
				session.setAttribute("heirarchyType", Integer.parseInt(boundryForm.getHeirarchyType()));
				final int boundaryId = Integer.parseInt(boundryForm.getBndryId());
				final Boundary bndry = this.boundaryService.getBoundaryById(boundaryId);
				session.setAttribute("boundaryNum", bndry.getBoundaryNum());
				if (bndry.getParent() != null) {
					session.setAttribute("BndryIdValue", bndry.getParent().getId().intValue());
				} else {
					session.setAttribute("BndryIdValue", boundaryId);
				}

				session.setAttribute("bndryTypeHeirarchyLevel", (int) bndry.getBoundaryType().getHeirarchy());

				session.setAttribute("operation", "delete");
				session.setAttribute("name", bndry.getName());
				req.getSession().setAttribute("cityList", this.cityWebsiteService.getCityWebsite(Integer.valueOf(boundaryId)));
				target = "update";
			} catch (final Exception c) {
				LOG.error("Boundary delete cannot be processed due to an internal server error.",c);
				req.setAttribute("MESSAGE", "Boundary delete cannot be processed due to an internal server error.");
				throw new EGOVRuntimeException("Boundary delete cannot be processed due to an internal server error.");
			}

		}
		return mapping.findForward(target);

	}
}
