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

package org.egov.infra.web.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This Filter is used to improve ui performance by setting Cache-Control header to static resources like js,css,jpg,gif,etc.
 */
public class CacheControlFilter implements Filter {

    private static final String EXPIRE_HEADER = "Expires";
    private static final String ETAG_HEADER = "ETag";
    private static final String CACHE_CONTROL_HEADER = "Cache-Control";
    private static final String PRAGMA_HEADER = "Pragma";
    public static final long DEFAULT_EXPIRES_SECONDS = 30 * 24 * 60 * 60;

    private long expireInSeconds = 0;

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        if (filterConfig.getInitParameter("expireInSeconds") == null)
            expireInSeconds = DEFAULT_EXPIRES_SECONDS;
        else
            expireInSeconds = Long.valueOf(filterConfig.getInitParameter("expireInSeconds"));
    }
    
    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        final HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader(CACHE_CONTROL_HEADER, "public,max-age=" + expireInSeconds);
        httpServletResponse.setDateHeader(EXPIRE_HEADER, System.currentTimeMillis() + expireInSeconds * 1000L);
        httpServletResponse.setHeader(PRAGMA_HEADER, null);
        httpServletResponse.setHeader(ETAG_HEADER, null);
        chain.doFilter(request, httpServletResponse);
    }

    @Override
    public void destroy() {
    }
}
