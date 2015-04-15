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
package org.egov.lib.security.terminal.client;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.security.terminal.model.UserCounterMap;
import org.hibernate.Query;

public class UserCounterReportsAction extends DispatchAction {

	protected static final Logger LOGGER = LoggerFactory.getLogger(UserCounterReportsAction.class);

	public ActionForward generateUserCounterReports(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws ServletException {
		String target = "";
		final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		final SimpleDateFormat formatter1 = new SimpleDateFormat("MM/dd/yyyy");
		try {
			final UserCounterReportsForm userCounterReportsForm = (UserCounterReportsForm) form;
			String isLoc;
			if ("1".equals(userCounterReportsForm.getIsLoc())) {
				isLoc = "1";
			} else {
				isLoc = "0";
			}
			List<UserCounterMap> userCounterList = new ArrayList<UserCounterMap>();
			if (!StringUtils.trimToEmpty(userCounterReportsForm.getFromDate()).equals("") && !StringUtils.trimToEmpty(userCounterReportsForm.getToDate()).equals("")) {
				final String fromDate = formatter1.format(formatter.parse(userCounterReportsForm.getFromDate()));
				final String toDate = formatter1.format(formatter.parse(userCounterReportsForm.getToDate()));
				final Query qry = HibernateUtil.getCurrentSession().createQuery(
						"select u from UserCounterMap u,Location l " + "where u.counterId=l.id and " + "l.isLocation=:isLoc and " + "(" + "(u.toDate IS NULL AND u.fromDate >= :fromDate) " + "OR " + "(u.fromDate >= :fromDate AND u.toDate <= :toDate))");
				qry.setDate("fromDate", DateUtils.getDate(fromDate, DateUtils.DFT_DATE_FORMAT));
				qry.setDate("toDate", DateUtils.getDate(toDate, DateUtils.DFT_DATE_FORMAT));
				qry.setString("isLoc", isLoc);
				userCounterList = qry.list();

			}
			if (!StringUtils.trimToEmpty(userCounterReportsForm.getFromDate()).equals("") && StringUtils.trimToEmpty(userCounterReportsForm.getToDate()).equals("")) {
				final String fromDate = formatter1.format(formatter.parse(userCounterReportsForm.getFromDate()));
				final Date currDate = new Date();
				final Query qry = HibernateUtil.getCurrentSession().createQuery(
						"select u from UserCounterMap u,Location l " + "where u.counterId=l.id and " + "l.isLocation=:isLoc and " + "(" + "(u.toDate IS NULL AND u.fromDate >= :fromDate) " + "OR " + "(u.fromDate >= :fromDate AND u.toDate <= :currDate))");
				qry.setDate("fromDate", DateUtils.getDate(fromDate, DateUtils.DFT_DATE_FORMAT));
				qry.setDate("currDate", currDate);
				qry.setString("isLoc", isLoc);
				userCounterList = qry.list();

			}
			if (StringUtils.trimToEmpty(userCounterReportsForm.getFromDate()).equals("") && StringUtils.trimToEmpty(userCounterReportsForm.getToDate()).equals("")) {
				final Date currDate = new Date();
				final Query qry = HibernateUtil.getCurrentSession().createQuery(
						"select u from UserCounterMap u,Location l " + "where u.counterId=l.id and " + "l.isLocation=:isLoc and " + "(" + "(u.toDate IS NULL AND u.fromDate>= :currDate) " + "OR " + "(u.fromDate >= :currDate AND u.toDate <= :currDate))");

				qry.setDate("currDate", currDate);
				qry.setString("isLoc", isLoc);
				userCounterList = qry.list();
			}
			req.setAttribute("userCounterList", userCounterList);
			target = "success";
		} catch (final Exception ex) {
			target = "error";
			LOGGER.error("Exception occurred in UserCounterReportAction ", ex);
			throw new EGOVRuntimeException("Exception occurred in UserCounterReportAction ", ex);
		}
		return mapping.findForward(target);
	}

}
