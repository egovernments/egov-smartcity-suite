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
package org.egov.infstr.client.administration.rjbac.user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserJurisdictionAction extends DispatchAction {
	
	private static final Logger LOG = LoggerFactory.getLogger(UserJurisdictionAction.class);
	private final UserService userService = new UserService();
	
	public ActionForward saveJurisdiction(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
		String target = "";
		final UserForm userForm = (UserForm) form;
		try {
			String userIdStr = "";
			if (request.getParameter("userid") != null) {
				userIdStr = request.getParameter("userid");
			}
			final User user = userService.getUserById(Long.valueOf(userIdStr));
			final String id[] = userForm.getBndryID();
			for (int i = 0; i < id.length; i++) {
				final Long bndryId = Long.valueOf(id[i]);
				//final Boundary bndry = boundaryService.getBoundaryById(bndryId);
				//final BoundaryType bndryType = bndry.getBoundaryType();
				//final JurisdictionValues jurValueModifyObj = userService.getJurisdictionValueByBndryIdAndUserId(bndryId, user.getId());
				//final Jurisdiction jurisdiction = userService.getJurisdictionByBndryTypeIdAndUserId(bndryType.getId(), user.getId());
				/*if (userForm.getSelCheck()[i].equals("yes") && (jurValueModifyObj != null) && (bndry != null) && bndry.getBoundaryType().getId().equals(bndryType.getId()) && (jurisdiction != null)) {
					jurValueModifyObj.setIsHistory('Y');
					final Jurisdiction jurModifyObj = jurValueModifyObj.getUserJurLevel();
					jurModifyObj.setJurisdictionLevel(bndryType);
					jurModifyObj.setUser(user);
					final JurisdictionValues jurVal = new JurisdictionValues();
					jurVal.setBoundary(bndry);
					String fromDate = userForm.getFromDate1()[i];
					fromDate = fromDate.substring(3, 5) + "/" + fromDate.substring(0, 2) + "/" + fromDate.substring(6, 10);
					if ((userForm.getToDate1()[i] != null) && !userForm.getToDate1()[i].equals("")) {
						String toDate = userForm.getToDate1()[i];
						toDate = toDate.substring(3, 5) + "/" + toDate.substring(0, 2) + "/" + toDate.substring(6, 10);
						jurVal.setToDate(DateUtils.getDate(toDate, DateUtils.DFT_DATE_FORMAT));
					}
					jurVal.setFromDate(DateUtils.getDate(fromDate, DateUtils.DFT_DATE_FORMAT));
					jurVal.setIsHistory('N');
					jurVal.setUserJurLevel(jurModifyObj);
					jurModifyObj.addJurisdictionValue(jurVal);
				}
				if ((jurValueModifyObj == null) && userForm.getSelCheck()[i].equals("no") && (jurisdiction != null) && (bndry != null) && bndry.getBoundaryType().getId().equals(bndryType.getId())) {
					jurisdiction.setJurisdictionLevel(bndryType);
					jurisdiction.setUser(user);
					final JurisdictionValues jurVal = new JurisdictionValues();
					jurVal.setBoundary(bndry);
					String fromDate = userForm.getFromDate1()[i];
					fromDate = fromDate.substring(3, 5) + "/" + fromDate.substring(0, 2) + "/" + fromDate.substring(6, 10);
					if ((userForm.getToDate1()[i] != null) && !userForm.getToDate1()[i].equals("")) {
						String toDate = userForm.getToDate1()[i];
						toDate = toDate.substring(3, 5) + "/" + toDate.substring(0, 2) + "/" + toDate.substring(6, 10);
						jurVal.setToDate(DateUtils.getDate(toDate, DateUtils.DFT_DATE_FORMAT));
					}
					jurVal.setFromDate(DateUtils.getDate(fromDate, DateUtils.DFT_DATE_FORMAT));
					jurVal.setUserJurLevel(jurisdiction);
					jurVal.setIsHistory('N');
					jurisdiction.addJurisdictionValue(jurVal);
				}
				if ((jurValueModifyObj == null) && (bndry != null) && userForm.getSelCheck()[i].equals("no") && (jurisdiction == null) && bndry.getBoundaryType().getId().equals(bndryType.getId())) {
					final JurisdictionValues jurval = new JurisdictionValues();
					final Jurisdiction jur = new Jurisdiction();
					jur.setJurisdictionLevel(bndryType);
					String fromDate = userForm.getFromDate1()[i];
					fromDate = fromDate.substring(3, 5) + "/" + fromDate.substring(0, 2) + "/" + fromDate.substring(6, 10);
					if ((userForm.getToDate1()[i] != null) && !userForm.getToDate1()[i].equals("")) {
						String toDate = userForm.getToDate1()[i];
						toDate = toDate.substring(3, 5) + "/" + toDate.substring(0, 2) + "/" + toDate.substring(6, 10);
						jurval.setToDate(DateUtils.getDate(toDate, DateUtils.DFT_DATE_FORMAT));
					}
					jurval.setFromDate(DateUtils.getDate(fromDate, DateUtils.DFT_DATE_FORMAT));
					jurval.setBoundary(bndry);
					jurval.setUserJurLevel(jur);
					jur.setUser(user);
					jur.setUpdateTime(new Date());
					jurval.setIsHistory('N');
					jur.addJurisdictionValue(jurval);
					//user.addJurisdiction(jur);
				}*/
			}
			final String msg = "The Jurisdiction for the user has been updated Successfully!!";
			request.setAttribute("MESSAGE", msg);
			target = "success";
		} catch (final EGOVRuntimeException rexp) {
			target = "failure";
			LOG.error("Error occurred in user jurisdiction update", rexp);
			throw new EGOVRuntimeException("Error occurred in user jurisdiction update", rexp);
		} catch (final Exception c) {
			LOG.error("Error occurred in user jurisdiction update", c);
			target = "error";
			request.setAttribute("MESSAGE", "Error View Jurisdiction !!");
			throw new EGOVRuntimeException("Error occurred in user jurisdiction update", c);
		}
		return mapping.findForward(target);
	}
	
	public ActionForward viewJurisdiction(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
		String target = "";
		try {
			String userIdStr = "";
			if (request.getParameter("userid") != null) {
				userIdStr = request.getParameter("userid");
				/*final User user = userService.getUserByID(Long.valueOf(userIdStr));
				request.setAttribute("user", user);*/
			}
			/*final Set jurObj = userService.getAllJurisdictionsForUser(Long.valueOf(userIdStr));
			if (jurObj != null) {
				request.setAttribute("jurObj", jurObj);
			}*/
			target = "view";
		} catch (final EGOVRuntimeException rexp) {
			target = "failure";
			LOG.error("Error occurred while getting user jurisdiction data", rexp);
			throw new EGOVRuntimeException("Error occurred while getting user jurisdiction data", rexp);
		} catch (final Exception c) {
			LOG.error("Error occurred while getting user jurisdiction data", c);
			target = "error";
			request.setAttribute("MESSAGE", "Error View Jurisdiction !!");
			throw new EGOVRuntimeException("Error occurred while getting user jurisdiction data", c);
		}
		return mapping.findForward(target);
	}
}
