/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.infstr.security.spring;

import org.egov.infra.admin.master.entity.Action;
import org.egov.infra.admin.master.service.ActionService;
import org.egov.infra.security.utils.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * ObjectDefinitionSource for Spring security filter to determine access based
 * on url
 *
 * @author sahina bose
 */
@Component
public class EGovFilterInvocationDefinitionSource implements FilterInvocationSecurityMetadataSource {

    private List<String> excludePatterns = new ArrayList<>();

    @Autowired
    private ActionService actionService;

    public void setExcludePatterns(final List<String> excludePatterns) {
        this.excludePatterns = excludePatterns;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(final Object object) {
        final FilterInvocation invocation = (FilterInvocation) object;
        final String contextRoot = invocation.getHttpRequest().getContextPath().replace("/", "");
        return lookupAttributes(contextRoot, invocation.getRequestUrl());
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return Collections.unmodifiableCollection(new ArrayList<ConfigAttribute>());
    }

    @Override
    public boolean supports(final Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

    private Collection<ConfigAttribute> lookupAttributes(final String contextRoot, final String url) {
        if (url.startsWith(SecurityConstants.LOGIN_URI) || url.startsWith(SecurityConstants.PUBLIC_URI) || isPatternExcluded(url))
            return Collections.emptyList();
        else {
            final Action action = actionService.getActionByUrlAndContextRoot(url, contextRoot);
            if (action != null) {
                final List<ConfigAttribute> configAttributes = new ArrayList<>();
                action.getRoles().forEach(role ->
                        configAttributes.add(new SecurityConfig(role.getName()))
                );
                return configAttributes;
            }
        }
        return Collections.emptyList();
    }

    private Boolean isPatternExcluded(final String pattern) {
        return excludePatterns.parallelStream().anyMatch(excludePattern -> pattern.startsWith(excludePattern.trim()));
    }

}
