/*
 * @(#)EGovFilterInvocationDefinitionSource.java 3.0, 18 Jun, 2013 4:10:16 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.security.spring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.egov.infra.admin.master.entity.Role;
import org.egov.infstr.security.utils.SecurityConstants;
import org.egov.lib.rrbac.dao.ActionHibernateDAO;
import org.egov.lib.rrbac.model.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

/**
 * ObjectDefinitionSource for Acegi filter to determine access based on url
 * 
 * @author sahina bose
 */
public class EGovFilterInvocationDefinitionSource implements FilterInvocationSecurityMetadataSource {

	private static final Logger LOG = LoggerFactory.getLogger(EGovFilterInvocationDefinitionSource.class);
	private List<String> excludePatterns = new ArrayList<String>();
	
	private ActionHibernateDAO actionDao;
	

	public void setActionDao(ActionHibernateDAO actionDao) {
		this.actionDao = actionDao;
	}

	public void setExcludePatterns(final List<String> excludePatterns) {
		this.excludePatterns = excludePatterns;
	}

	private Boolean isPatternExcluded(final String pattern) {
		for (final String excludePattern : this.excludePatterns) {
			if (pattern.startsWith(excludePattern.trim())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		final FilterInvocation invocation = (FilterInvocation) object;
		final String url = invocation.getRequestUrl();
		final String contextRoot = invocation.getHttpRequest().getContextPath();
		return this.lookupAttributes(contextRoot, url);
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return Collections.unmodifiableCollection(new ArrayList<ConfigAttribute>());
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return FilterInvocation.class.isAssignableFrom(clazz);
	}

	private Collection<ConfigAttribute> lookupAttributes(final String contextPath, final String url) {
		if (url.startsWith(SecurityConstants.LOGIN_URI) || url.startsWith(SecurityConstants.PUBLIC_URI) || this.isPatternExcluded(url)) {
			return null;
		} else {
			Action action = this.actionDao.findActionByURL(contextPath, url);
			if (action == null) {
				LOG.warn("No action mapping exists for url: " + url);
				action = this.actionDao.findActionByName("DEFAULT");
			}

			final Set<Role> actionRoles = action.getRoles();
			if (actionRoles != null && !actionRoles.isEmpty()) {
				final List<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
				for (final Role role : actionRoles) {
					configAttributes.add(new SecurityConfig(role.getName()));
				}
				return configAttributes;
			}
		}
		return null;
	}

}
