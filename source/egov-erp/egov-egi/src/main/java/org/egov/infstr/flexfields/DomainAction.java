/*
 * @(#)DomainAction.java 3.0, 17 Jun, 2013 12:56:31 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.flexfields;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.exceptions.EGOVRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DomainAction extends DispatchAction {

	private static final Logger LOG = LoggerFactory.getLogger(DomainAction.class);
	private DomainDAO domainDAO;

	public DomainAction(DomainDAO domainDAO) {
		this.domainDAO = domainDAO;
	}

	public ActionForward createDomain(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws ServletException {
		String target = "";
		try {
			final DomainForm domainform = (DomainForm) form;
			final String domainName = domainform.getDomainName();
			final String domainDesc = domainform.getDomainDesc();
			final String buttonType = domainform.getForward();

			if (domainDesc == null) {
				throw new EGOVRuntimeException("ActionForm value cannot be null");
			}

			final Domain obj = new Domain();
			obj.setDomainName(domainName);
			obj.setDomainDesc(domainDesc);

			domainDAO.createDomain(obj);

			target = buttonType;
		} catch (final Exception ex) {
			target = "error";
			LOG.error("Error occurred while creating Domain",ex);
			throw new EGOVRuntimeException("Exception occured -----> " + ex.getMessage());
		}
		return mapping.findForward(target);
	}

	public ActionForward updateDomain(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws ServletException {
		String target = "";
		try {
			final DomainForm domainform = (DomainForm) form;
			final String id = domainform.getId();
			final String domainName = domainform.getDomainName();
			final String domainDesc = domainform.getDomainDesc();
			final String buttonType = domainform.getForward();

			if (id == null || domainDesc == null) {
				throw new EGOVRuntimeException("ActionForm value cannot be null");
			}

			final Domain obj = new Domain();
			obj.setId(Integer.parseInt(id));
			obj.setDomainName(domainName);
			obj.setDomainDesc(domainDesc);

			domainDAO.updateDomain(obj);

			target = buttonType;
		} catch (final Exception ex) {
			target = "error";
			LOG.error("Error occurred while updating Domain",ex);
			throw new EGOVRuntimeException("Exception occured -----> " + ex.getMessage());
		}
		return mapping.findForward(target);
	}

	public ActionForward deleteDomain(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws ServletException {
		String target = "";
		try {
			final DomainForm domainform = (DomainForm) form;
			final String id = domainform.getId();
			final String buttonType = domainform.getForward();

			if (id == null) {
				throw new EGOVRuntimeException("ActionForm value cannot be null");
			}

			domainDAO.deleteDomain(Integer.parseInt(id));

			target = buttonType;
		} catch (final Exception ex) {
			target = "error";
			LOG.error("Error occurred while deleting Domain",ex);
			throw new EGOVRuntimeException("Exception occured -----> " + ex.getMessage());
		}
		return mapping.findForward(target);
	}

	public ActionForward loadModifyData(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws ServletException {
		String target = "";
		try {
			final DomainForm domainform = (DomainForm) form;
			final String id = domainform.getId();
			final String buttonType = domainform.getForward();

			if (id == null) {
				throw new EGOVRuntimeException("ActionForm value cannot be null");
			}

			final Domain obj = domainDAO.getDomain(Integer.parseInt(id));

			domainform.setDomainName(obj.getDomainName());
			domainform.setDomainDesc(obj.getDomainDesc());

			req.setAttribute("domainform", domainform);
			req.setAttribute("buttonType", buttonType);
			target = buttonType;
		} catch (final Exception ex) {
			target = "error";
			LOG.error("Error occurred while loading Domain",ex);
			throw new EGOVRuntimeException("Exception occured -----> " + ex.getMessage());
		}
		return mapping.findForward(target);
	}

	public ActionForward loadCreateData(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws ServletException {
		String target = "";
		try {
			final DomainForm domainform = (DomainForm) form;
			final String buttonType = domainform.getForward();
			req.setAttribute("buttonType", buttonType);
			target = buttonType;
		} catch (final Exception ex) {
			target = "error";
			LOG.error("Error occurred while loading Domain",ex);
			throw new EGOVRuntimeException("Exception occured -----> " + ex.getMessage());
		}
		return mapping.findForward(target);
	}

}
