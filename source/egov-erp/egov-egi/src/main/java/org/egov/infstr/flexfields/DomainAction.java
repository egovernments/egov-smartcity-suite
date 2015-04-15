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
