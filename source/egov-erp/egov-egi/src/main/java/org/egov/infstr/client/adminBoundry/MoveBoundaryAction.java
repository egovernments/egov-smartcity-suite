/*
 * @(#)MoveBoundaryAction.java 3.0, 18 Jun, 2013 2:22:44 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.client.adminBoundry;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infstr.client.EgovAction;
import org.egov.infstr.client.delegate.MoveBoundaryDelegate;

public class MoveBoundaryAction extends EgovAction {
	private final static Logger LOG = LoggerFactory.getLogger(MoveBoundaryAction.class);
	private MoveBoundaryDelegate moveBoundaryDelegate;

	/**
	 * This method to handle Move Boundary
	 * @param ActionMapping mapping
	 * @param ActionForm form
	 * @param HttpServletRequest req
	 * @param HttpServletResponse res
	 * @return ActionForward
	 */

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws ServletException, EGOVRuntimeException, Exception {

		String target = null;
		try {
			target = req.getParameter("target");
			final String mode = req.getParameter("mode");

			if ((target != null) && (mode != null) && target.equals("AJAX")) {
				List<Boundary> boundaryList = null;
				String jsonString = null;
				if (mode.equals("load_tree")) {
					boundaryList = this.moveBoundaryDelegate.getChildBoundaries(req.getParameter("boundaryId"));
					jsonString = new String(this.createJsonBoundaryList(boundaryList));
				} else if (mode.equals("load_move_data")) {
					boundaryList = this.moveBoundaryDelegate.getAllParentBoundaries(req.getParameter("boundaryId"));
					jsonString = new String(this.createJsonBoundaryList(boundaryList));
				} else if (mode.equals("move_boundary")) {
					final Boundary boundary = this.moveBoundaryDelegate.moveBoundary(req.getParameter("boundaryId"), req.getParameter("parentBoundaryId"));
					if (boundary != null) {
						jsonString = "({status : 'moved',message:'successfully moved'})";
					} else {
						jsonString = "({status : 'failed',message:'Failed to move Boundary'})";
					}
				}

				req.setAttribute("ajaxResponse", jsonString);
			} else {
				target = "success";
			}

		} catch (final Exception e) {
			LOG.error("Error occurred at Move Boundary, Cause : " + e.getMessage());
			req.setAttribute("ajaxResponse", "({status:'failed',message:'Error occurred at Boundary Move'})");
		}
		return mapping.findForward(target);
	}

	/**
	 * To set MoveBoundaryDelegate
	 * @param moveBoundaryDelegate
	 */
	public void setMoveBoundaryDelegate(final MoveBoundaryDelegate moveBoundaryDelegate) {
		this.moveBoundaryDelegate = moveBoundaryDelegate;
	}

	/**
	 * Used to create Json string of boundary name and id from the given Boundary List
	 * @param boundaryList
	 * @return {@link StringBuffer}
	 * @throws Exception;
	 */
	private StringBuffer createJsonBoundaryList(final List<Boundary> boundaryList) throws Exception {

		try {
			if ((boundaryList != null) && (boundaryList.size() > 0)) {

				final StringBuffer name = new StringBuffer();
				final StringBuffer value = new StringBuffer();
				name.append("({name : [");
				value.append("value : [");
				for (final Boundary boundary : boundaryList) {
					name.append("'").append(boundary.getName()).append("',");
					value.append(boundary.getId()).append(",");
				}
				name.deleteCharAt(name.length() - 1);
				value.deleteCharAt(value.length() - 1);
				name.append("],").append(value).append("]").append("})");
				return name;
			} else {
				return new StringBuffer("({name:[],value:[]})");
			}
		} catch (final Exception e) {
			LOG.error("Error occurred while creating Json string from Boundary List, Cause : " + e.getMessage());
			throw e;
		}
	}
}
