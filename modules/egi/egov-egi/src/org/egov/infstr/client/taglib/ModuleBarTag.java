package org.egov.infstr.client.taglib;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.egov.infstr.beanfactory.ApplicationContextBeanProvider;
import org.egov.infstr.commons.Module;
import org.egov.infstr.commons.service.GenericCommonsService;
import org.egov.web.utils.ERPWebApplicationContext;
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
