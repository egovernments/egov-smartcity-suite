package org.egov.infstr.client.taglib;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

import org.egov.infstr.beanfactory.ApplicationContextBeanProvider;
import org.egov.infstr.commons.Module;
import org.egov.infstr.commons.service.GenericCommonsService;
import org.egov.web.utils.ERPWebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class IfModuleEnabledTag extends BodyTagSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String moduleName = null;

	public String getModuleName() {
		return this.moduleName;
	}

	public void setModuleName(final String moduleName) {
		this.moduleName = moduleName;
	}

	@Override
	public int doStartTag() throws JspTagException {
		Module module = null;

		// no params specified
		if (null == this.moduleName || "".equals(this.moduleName)) {
			return Tag.SKIP_BODY;
		}

		if (this.moduleName != null) {
			final ApplicationContextBeanProvider provider = new ApplicationContextBeanProvider();
			provider.setApplicationContext(WebApplicationContextUtils.getWebApplicationContext(ERPWebApplicationContext.getServletContext()));
			final GenericCommonsService genericCommonService = (GenericCommonsService) provider.getBean("genericCommonService");
			module = genericCommonService.getModuleByName(this.moduleName);
		}

		if (module != null) {
			// if module set as enabled display content of tag
			if (module.getIsEnabled()) {
				return Tag.EVAL_BODY_INCLUDE;
			}

		}

		return Tag.SKIP_BODY;
	}
}
