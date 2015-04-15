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
