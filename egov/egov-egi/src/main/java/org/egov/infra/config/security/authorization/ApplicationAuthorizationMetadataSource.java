/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */

package org.egov.infra.config.security.authorization;

import org.egov.infra.admin.master.entity.Action;
import org.egov.infra.admin.master.service.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.egov.infra.security.utils.SecurityConstants.LOGIN_URI;
import static org.egov.infra.security.utils.SecurityConstants.PUBLIC_URI;

public class ApplicationAuthorizationMetadataSource implements FilterInvocationSecurityMetadataSource {

    private List<String> excludePatterns = new ArrayList<>();

    @Autowired
    private ActionService actionService;

    public void setExcludePatterns(List<String> excludePatterns) {
        this.excludePatterns = excludePatterns;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) {
        FilterInvocation invocation = (FilterInvocation) object;
        String contextRoot = invocation.getHttpRequest().getContextPath().replace("/", "");
        return lookupAttributes(contextRoot, invocation.getRequestUrl());
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return Collections.unmodifiableCollection(new ArrayList<ConfigAttribute>());
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

    private Collection<ConfigAttribute> lookupAttributes(String contextRoot, String url) {
        if (url.startsWith(LOGIN_URI) || url.startsWith(PUBLIC_URI) || isPatternExcluded(url))
            return Collections.emptyList();
        else {
            Action action = actionService.getActionByUrlAndContextRoot(url, contextRoot);
            if (action != null) {
                List<ConfigAttribute> configAttributes = new ArrayList<>();
                action.getRoles().forEach(role ->
                        configAttributes.add(new SecurityConfig(role.getName()))
                );
                return configAttributes;
            }
        }
        return Collections.emptyList();
    }

    private Boolean isPatternExcluded(String pattern) {
        return excludePatterns
                .parallelStream()
                .anyMatch(excludePattern -> pattern.startsWith(excludePattern.trim()));
    }

}