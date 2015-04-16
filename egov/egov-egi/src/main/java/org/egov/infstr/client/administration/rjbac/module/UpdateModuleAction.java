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
package org.egov.infstr.client.administration.rjbac.module;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.client.EgovAction;
import org.egov.infstr.commons.Module;
import org.egov.infstr.commons.service.GenericCommonsService;
import org.egov.infstr.utils.StringUtils;

public class UpdateModuleAction extends EgovAction {
	private static final Logger LOG = LoggerFactory.getLogger(UpdateModuleAction.class);
	private GenericCommonsService genericCommonsService;

	public void setGenericCommonsService(final GenericCommonsService genericCommonsService) {
		this.genericCommonsService = genericCommonsService;
	}

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {

		String target = "failure";
		try {
			final ModuleForm moduleForm = (ModuleForm) form;
			Module module = null;
			if (StringUtils.isNotBlank(moduleForm.getModuleId())) {
				module = this.genericCommonsService.getModuleByID(Integer.valueOf(moduleForm.getModuleId()));
				if (module == null) {
					throw new EGOVRuntimeException("Module does not exist");
				}
			} else {
				throw new EGOVRuntimeException("Module Id could not found");
			}

			if (StringUtils.isNotBlank(moduleForm.getModuleNameLocal())) {
				module.setModuleNameLocal(moduleForm.getModuleNameLocal());
			}
			if (StringUtils.isNotBlank(moduleForm.getModuleDescription())) {
				module.setModuleDescription(moduleForm.getModuleDescription());
			}
			if (StringUtils.isNotBlank(moduleForm.getParentModuleId())) {
				final Module parentModule = this.genericCommonsService.getModuleByID(Integer.valueOf(moduleForm.getParentModuleId()));
				if (parentModule != null) {
					module.setParent(parentModule);
				}
			}
			module.setIsEnabled(moduleForm.getIsEnabled());
			if (StringUtils.isNotBlank(moduleForm.getBaseURL())) {
				module.setBaseUrl(moduleForm.getBaseURL());
			}
			if (StringUtils.isNotBlank(moduleForm.getOrderNumber())) {
				module.setOrderNumber(new Integer(moduleForm.getOrderNumber()));
			}
			this.genericCommonsService.updateModule(module);
			req.setAttribute("module", module);
			req.setAttribute("MESSAGE", "Module successfully modified");
			this.resetToken(req);
			target = "success";

		} catch (final Exception c) {
			LOG.error("Error occurred while tring to update Module",c);
			req.setAttribute("MESSAGE", "Request cannot be processed due to an internal server error");
			throw new EGOVRuntimeException("Error occurred while tring to update Module");

		}
		return mapping.findForward(target);
	}

}
