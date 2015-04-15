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
package org.egov.infstr.client.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.egov.exceptions.AuthorizationException;
import org.egov.exceptions.EGOVRuntimeException;

/**
 * The Class SetThreadLocals. Used to set the EgovThreadLocal values
 */
public class SetThreadLocals implements Filter {

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(SetThreadLocals.class);

	/**
	 * Initialize the filter
	 * @param config the config
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(final FilterConfig config) {

	}

	/**
	 * This will seta the EgovThreadLocal with various data passed through request
	 * @param request the request
	 * @param response the response
	 * @param chain the chain
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ServletException the servlet exception
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
		try {
			setConnectionJndi(request, response);
			chain.doFilter(request, response);
			SetDomainJndiHibFactNames.clearThreadLocals();
		} catch (final AuthorizationException e) {
			throw e;
		} catch (final Throwable e) {
			LOG.error("Error occurred in SetThreadLocals Filter", e);
			throw new EGOVRuntimeException("Internal Server Error", e);
		}

	}

	/**
	 * Destroy.
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {

	}

	/**
	 * Sets the connection jndi.
	 * @param request the request
	 * @param response the response
	 */
	public static void setConnectionJndi(final ServletRequest request, final ServletResponse response) {
		SetDomainJndiHibFactNames.setThreadLocals(request);
	}

}
