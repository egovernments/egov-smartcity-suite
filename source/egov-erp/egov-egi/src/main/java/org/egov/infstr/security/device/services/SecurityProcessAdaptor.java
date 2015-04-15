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
package org.egov.infstr.security.device.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The Interface SecurityProcessAdaptor.
 * The base interface to all Device specific security processing 
 */
public interface SecurityProcessAdaptor {
	
	/**
	 * Process the client specific login request and return the response message.
	 * @param request the request
	 * @return the string
	 */
	String processLoginRequest(HttpServletRequest request);
	
	/**
	 * Checks if its is login request or not.
	 * @param request the request
	 * @return true, if is login request
	 */
	boolean isLoginRequest(HttpServletRequest request);
	
	/**
	 * Do any other operation before invoking filter chain.
	 * @param request the request
	 * @param response the response
	 */
	void doBeforeFilterChain(HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * Gets the content type.
	 * @return the content type
	 */
	String getContentType();
}
