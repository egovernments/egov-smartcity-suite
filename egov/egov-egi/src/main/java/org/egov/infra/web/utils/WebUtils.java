/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */

package org.egov.infra.web.utils;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonSerializer;
import org.egov.infra.exception.ApplicationRuntimeException;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.egov.infra.web.support.json.adapter.HibernateProxyTypeAdapter.FACTORY;

public class WebUtils {

    /**
     * This will return only domain name from http request <br/>
     * eg: http://www.domain.com/cxt/xyz will return www.domain.com http://somehost:8090/cxt/xyz will return somehost
     **/
    public static String extractRequestedDomainName(final HttpServletRequest httpRequest) {
        final String requestURL = httpRequest.getRequestURL().toString();
        return extractRequestedDomainName(requestURL);
    }

    /**
     * This will return only domain name from given requestUrl <br/>
     * eg: http://www.domain.com/cxt/xyz will return www.domain.com http://somehost:8090/cxt/xyz will return somehost
     **/
    public static String extractRequestedDomainName(final String requestURL) {
        final int domainNameStartIndex = requestURL.indexOf("://") + 3;
        String domainName = requestURL.substring(domainNameStartIndex, requestURL.indexOf('/', domainNameStartIndex));
        if (domainName.contains(":"))
            domainName = domainName.split(":")[0];
        return domainName;
    }

    /**
     * This will return full domain name including http scheme and optionally with contextroot depends on 'withContext' value eg:
     * http://www.domain.com/cxt/xyz withContext value as true will return http://www.domain.com/cxt/ <br/>
     * http://www.domain.com/cxt/xyz withContext value as false will return http://www.domain.com
     **/
    public static String extractRequestDomainURL(final HttpServletRequest httpRequest, final boolean withContext) {
        final StringBuffer url = httpRequest.getRequestURL();
        final String uri = httpRequest.getRequestURI();
        return withContext ? url.substring(0, url.length() - uri.length() + httpRequest.getContextPath().length()) + "/"
                : url.substring(0, url.length() - uri.length());
    }

    public static String extractQueryParamsFromUrl(final String url) {
        return url.substring(url.indexOf("?") + 1, url.length());
    }

    public static String extractURLWithoutQueryParams(final String url) {
        return url.substring(0, url.indexOf("?"));
    }

    public static String currentContextPath(final ServletRequest request) {
        return request.getServletContext().getContextPath().toUpperCase().replace("/", EMPTY);
    }

    public static <T> String toJSON(Collection<T> objects, Class<? extends T> objectClazz, Class<? extends JsonSerializer<T>> adptorClazz) {
        try {
            return new GsonBuilder().
                    registerTypeAdapterFactory(FACTORY).
                    registerTypeAdapter(objectClazz, adptorClazz.newInstance()).create().toJson(objects);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ApplicationRuntimeException("Could not convert object list to json string", e);
        }
    }

    public static <T> String toJSON(T object, Class<? extends JsonSerializer<T>> adptorClazz) {
        try {
            return new GsonBuilder().
                    registerTypeAdapterFactory(FACTORY).
                    registerTypeAdapter(object.getClass(), adptorClazz.newInstance()).create().toJson(object);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ApplicationRuntimeException("Could not convert object to json string", e);
        }
    }
}