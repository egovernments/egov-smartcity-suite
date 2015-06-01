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
package org.egov.infra.web.taglib;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.egov.infra.web.utils.ERPWebApplicationContext;
import org.egov.infstr.beanfactory.ApplicationContextBeanProvider;
import org.egov.infstr.commons.Module;
import org.egov.infstr.commons.service.GenericCommonsService;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Tag to display a task bar with modules that are enabled for a client deployment
 * 
 * @author sahinab
 */
public class ModuleBarTag extends BodyTagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5230093699581863032L;

	/**
	 * returns an HTML task bar containing links to module that are enabled for the client deployment.
	 */
	@Override
	public int doStartTag() throws JspTagException {
		try {
			final ApplicationContextBeanProvider provider = new ApplicationContextBeanProvider();
			provider.setApplicationContext(WebApplicationContextUtils.getWebApplicationContext(ERPWebApplicationContext.getServletContext()));
			final GenericCommonsService genericCommonService = (GenericCommonsService) provider.getBean("genericCommonService");
			final List<Module> modules = genericCommonService.getAllModules();

			final StringBuffer strBuf = new StringBuffer();
			strBuf.append("<div align=\"center\" >");
			strBuf.append("<div id=\"nav\">");
			if (modules != null && modules.size() > 0) {
				strBuf.append("<ul id=\"navmenu\" >");
				for (final Module module : modules) {
					if (module.getIsEnabled()) {
						strBuf.append("<li>");
						// get module's base url
						strBuf.append("<a href=\"").append("url").append("\" >");
						strBuf.append(module.getModuleName()).append("</a>");
						strBuf.append("</li>");
					}
				}
				strBuf.append("</ul>");
			}

			strBuf.append("</div>");
			strBuf.append("</div>");
			this.pageContext.getOut().print(strBuf.toString());
		} catch (final IOException e) {
			throw new JspTagException(e);
		}
		return EVAL_PAGE;
	}

}
