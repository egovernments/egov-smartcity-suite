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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.exceptions.EGOVRuntimeException;

public class AttributeTypeAction extends DispatchAction {

	private static final Logger LOG = LoggerFactory.getLogger(AttributeTypeAction.class);

	public ActionForward createAttributeType(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws ServletException {
		String target = "";
		try {
			final AttributeTypeForm typeform = (AttributeTypeForm) form;
			final String domainId = typeform.getApplDomainId();
			final String attributeName = typeform.getAttributeName();
			final String attributeDataType = typeform.getAttributeDataType();
			final String defaultValue = typeform.getDefaultValue();
			final String isRequired = typeform.getIsRequired();
			final String isList = typeform.getIsList();
			final String key[] = typeform.getKey();
			final String value[] = typeform.getValue();

			final String buttonType = typeform.getForward();

			if (domainId == null || attributeName == null || attributeDataType == null) {
				throw new EGOVRuntimeException("ActionForm value cannot be null");
			}

			final AttributeType obj = new AttributeType();
			obj.setAttributeName(attributeName);
			obj.setAttributeDataType(attributeDataType);
			obj.setDefaultValue(defaultValue);
			obj.setApplDomainId(Integer.parseInt(domainId));

			if (isRequired != null && isRequired.equalsIgnoreCase("on")) {
				obj.setIsRequired("1");
			} else {
				obj.setIsRequired("0");
			}
			if (isList != null && isList.equalsIgnoreCase("on")) {
				obj.setIsList("1");
			} else {
				obj.setIsList("0");
			}
			obj.setKey(key);
			obj.setValue(value);

			final AttributeTypeIF daoObj = new AttributeTypeDAO();
			daoObj.createAttributeType(obj);

			target = buttonType;
		} catch (final Exception ex) {
			target = "error";
			LOG.error("Error occurred in Attibute Type Action",ex);
			throw new EGOVRuntimeException("Exception occured -----> " + ex.getMessage());
		}
		return mapping.findForward(target);
	}

	public ActionForward updateAttributeType(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws ServletException {
		String target = "";
		try {
			final AttributeTypeForm typeform = (AttributeTypeForm) form;
			final String id = typeform.getId();
			final String domainId = typeform.getApplDomainId();
			final String attributeName = typeform.getAttributeName();
			final String attributeDataType = typeform.getAttributeDataType();
			final String defaultValue = typeform.getDefaultValue();
			final String buttonType = typeform.getForward();
			final String isRequired = typeform.getIsRequired();
			final String isList = typeform.getIsList();
			final String key[] = typeform.getKey();
			final String value[] = typeform.getValue();

			if (id == null || domainId == null || attributeName == null || attributeDataType == null) {
				throw new EGOVRuntimeException("ActionForm value cannot be null");
			}

			final AttributeType obj = new AttributeType();
			obj.setId(Integer.parseInt(id));
			obj.setAttributeName(attributeName);
			obj.setAttributeDataType(attributeDataType);
			obj.setDefaultValue(defaultValue);
			obj.setApplDomainId(Integer.parseInt(domainId));
			if (isRequired != null && isRequired.equalsIgnoreCase("on")) {
				obj.setIsRequired("1");
			} else {
				obj.setIsRequired("0");
			}
			if (isList != null && isList.equalsIgnoreCase("on")) {
				obj.setIsList("1");
			} else {
				obj.setIsList("0");
			}
			obj.setKey(key);
			obj.setValue(value);

			final AttributeTypeIF daoObj = new AttributeTypeDAO();
			daoObj.updateAttributeType(obj);

			target = buttonType;
		} catch (final Exception ex) {
			target = "error";
			LOG.error("Exception -----> " + ex.getMessage());
			throw new EGOVRuntimeException("Exception occured -----> " + ex.getMessage());
		}
		return mapping.findForward(target);
	}

	public ActionForward deleteAttributeType(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws ServletException {
		String target = "";
		try {
			final AttributeTypeForm typeform = (AttributeTypeForm) form;

			final String id = typeform.getId();
			final String buttonType = typeform.getForward();

			if (id == null) {
				throw new EGOVRuntimeException("ActionForm value cannot be null");
			}

			final AttributeTypeIF daoObj = new AttributeTypeDAO();
			daoObj.deleteAttributeType(Integer.parseInt(id));

			target = buttonType;
		} catch (final Exception ex) {
			target = "error";
			LOG.error("Error occurred while deleting Attribute Type",ex);
			throw new EGOVRuntimeException("Exception occured -----> " + ex.getMessage());
		}
		return mapping.findForward(target);
	}

	public ActionForward loadModifyData(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws ServletException {
		String target = "";
		try {
			final AttributeTypeForm typeform = (AttributeTypeForm) form;

			final String id = typeform.getId();
			final String buttonType = typeform.getForward();

			if (id == null) {
				throw new EGOVRuntimeException("ActionForm value cannot be null");
			}

			final AttributeTypeIF daoObj = new AttributeTypeDAO();
			final AttributeType obj = daoObj.getAttributeType(Integer.parseInt(id));

			typeform.setApplDomainId(Integer.toString(obj.getApplDomainId()));
			typeform.setAttributeName(obj.getAttributeName());
			typeform.setAttributeDataType(obj.getAttributeDataType());
			typeform.setDefaultValue(obj.getDefaultValue());

			if (obj.getIsList().equalsIgnoreCase("1")) {
				typeform.setIsList("on");
				req.setAttribute("isList", "true");
			} else {
				req.setAttribute("isList", "false");
				typeform.setIsList("off");
			}
			if (obj.getIsRequired().equalsIgnoreCase("1")) {
				typeform.setIsRequired("on");
			} else {
				typeform.setIsRequired("off");
			}
			typeform.setKey(obj.getKey());
			typeform.setValue(obj.getValue());

			req.setAttribute("typeform", typeform);
			req.setAttribute("buttonType", buttonType);
			target = buttonType;
		} catch (final Exception ex) {
			target = "error";
			LOG.error("Error occurred while loading Attribute Type",ex);
			throw new EGOVRuntimeException("Exception occured -----> " + ex.getMessage());
		}
		return mapping.findForward(target);
	}

	public ActionForward loadCreateData(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws ServletException {
		String target = "";
		try {
			final AttributeTypeForm typeform = (AttributeTypeForm) form;
			final String buttonType = typeform.getForward();
			req.setAttribute("buttonType", buttonType);
			target = buttonType;
		} catch (final Exception ex) {
			target = "error";
			LOG.error("Error occurred while loading Attribute Type",ex);
			throw new EGOVRuntimeException("Exception occured -----> " + ex.getMessage());
		}
		return mapping.findForward(target);
	}
}
