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
package org.egov.restapi.filter;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.http.HttpHeaders.REFERER;
import static org.egov.commons.entity.Source.APONLINE;
import static org.egov.commons.entity.Source.CARD;
import static org.egov.commons.entity.Source.ESEVA;
import static org.egov.commons.entity.Source.SOFTTECH;
import static org.egov.commons.entity.Source.LEADWINNER;
import static org.egov.infra.config.core.ApplicationThreadLocals.getCityCode;
import static org.egov.infra.config.core.ApplicationThreadLocals.setCityCode;
import static org.egov.infra.config.core.ApplicationThreadLocals.setDomainName;
import static org.egov.infra.config.core.ApplicationThreadLocals.setTenantID;

import java.io.IOException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.egov.commons.entity.Source;
import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.restapi.config.properties.RestAPIApplicationProperties;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.google.common.base.Charsets;

//This is an unnecessary class, the existence of this filter is due to implementer is not ready to
//change their existing system to call appropriate url from their apps.
public class ApiFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(ApiFilter.class);
    private static final String SOURCE = "source";
    private static final String ULB_CODE = "ulbCode";
    private static final String RESTAPI_ERROR_CODE = "RESTAPI.001";
    private static final EnumMap<Source, List<String>> SOURCE_IP_MAPPING = new EnumMap<>(Source.class);

    @Autowired
    private CityService cityService;

    @Autowired
    private RestAPIApplicationProperties restAPIProperties;

    @Autowired
    @Qualifier("ulbCodeMap")
    private Map<String, String> ulbCodeMap;

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain filterChain)
            throws IOException,
            ServletException {
        final MultiReadRequestWrapper multiReadRequestWrapper = new MultiReadRequestWrapper((HttpServletRequest) request);
        final String ulbCode = validateAndExtractULBCode(multiReadRequestWrapper);
        if (isNotBlank(ulbCode)) {
            final boolean diffDestination = !ulbCode.equals(getCityCode());
            LOG.info("Requested ULB Code :- reached : {}, destination : {}. Altering tenant info : {}",
                    getCityCode(), ulbCode, diffDestination);
            if (diffDestination) {
                final String tenantId = ulbCodeMap.get(ulbCode);
                setTenantID(tenantId);
                final City city = cityService.getCityByCode(ulbCode);
                setDomainName(city.getDomainURL());
                setCityCode(ulbCode);
            }
        } else
            throw new ApplicationRuntimeException("Could not obtain ULB Code from the request");

        filterChain.doFilter(multiReadRequestWrapper, response);
    }

    private String validateAndExtractULBCode(final MultiReadRequestWrapper request) throws IOException {
        final String referrer = request.getHeader(REFERER);
        if (isNotBlank(referrer)) {
            final HttpSession session = request.getSession();
            final Optional<Map.Entry<Source, List<String>>> resolvedIP = SOURCE_IP_MAPPING.entrySet().parallelStream()
                    .filter(e -> e.getValue().parallelStream().anyMatch(referrer::contains)).findFirst();
            if (resolvedIP.isPresent())
                session.setAttribute(SOURCE, resolvedIP.get().getKey());
            else
                throw new ApplicationRuntimeException(RESTAPI_ERROR_CODE);
        } else
            throw new ApplicationRuntimeException(RESTAPI_ERROR_CODE);

        final String ulbCode = request.getParameter(ULB_CODE);
        return isBlank(ulbCode) ? new JSONObject(IOUtils.toString(request.getInputStream(), Charsets.UTF_8))
                .get(ULB_CODE).toString() : ulbCode;

    }

    @Override
    public void init(final FilterConfig arg0) throws ServletException {
        // This has to be externalized to automatically pick from config
        SOURCE_IP_MAPPING.put(APONLINE, restAPIProperties.aponlineIPAddress());
        SOURCE_IP_MAPPING.put(ESEVA, restAPIProperties.esevaIPAddress());
        SOURCE_IP_MAPPING.put(SOFTTECH, restAPIProperties.softtechIPAddress());
        SOURCE_IP_MAPPING.put(CARD, restAPIProperties.cardIPAddress());
        SOURCE_IP_MAPPING.put(LEADWINNER, restAPIProperties.leadwinnerIPAddress());
    }

    @Override
    public void destroy() {
        // Do nothing
    }
}