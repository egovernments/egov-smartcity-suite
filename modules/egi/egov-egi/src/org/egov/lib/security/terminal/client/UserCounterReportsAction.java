/*
 * @(#)UserCounterReportsAction.java 3.0, 14 Jun, 2013 3:56:34 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
