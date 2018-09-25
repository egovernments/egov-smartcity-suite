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
package org.egov.restapi.web.security.oauth2.config;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.restapi.web.security.oauth2.entity.SecuredResource;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

/**
 * Configuration to protect the Api resources with Oauth2 Security
 *
 * @author subhash
 *
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    private static final Logger LOGGER = Logger.getLogger(ResourceServerConfiguration.class);
    private static final String APIS_CONFIG = "config/restapi-secured-apis-config.json";
    private static final String APIS_CONFIG_OVERRIDE = "config/restapi-secured-apis-config-override.json";
    private static final String RESOURCE_ID = "egov-restapi";

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(RESOURCE_ID).stateless(false);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.requestMatchers().and();
        configurePatterns(http);
        http.exceptionHandling()
                .accessDeniedHandler(new OAuth2AccessDeniedHandler());
    }

    private void configurePatterns(HttpSecurity http) throws Exception {
        getSecuredResourceFromResource().getResources().forEach(record -> {
            try {
                ExpressionUrlAuthorizationConfigurer<HttpSecurity>.AuthorizedUrl authorizedUrl = http.authorizeRequests()
                        .antMatchers(record.getUrl());
                if (StringUtils.isNotEmpty(record.getRoles()))
                    authorizedUrl.access(record.getRoles());
                else
                    authorizedUrl.authenticated();
            } catch (Exception e) {
                throw new ApplicationRuntimeException("Exception occured while configuring: ", e);
            }
        });

    }

    private SecuredResource getSecuredResourceFromResource() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
        mapper.configure(SerializationConfig.Feature.AUTO_DETECT_FIELDS, true);
        return mapper.readValue(getResourcesConfig().getInputStream(),
                SecuredResource.class);
    }

    private Resource getResourcesConfig() {
        Resource res = new ClassPathResource(APIS_CONFIG_OVERRIDE);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Overridden config present:" + res.exists());
        if (!res.exists())
            res = new ClassPathResource(APIS_CONFIG);
        return res;
    }

}