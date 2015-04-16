/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
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
