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

package org.egov.restapi.filter;

import org.apache.log4j.Logger;
import org.egov.infra.web.utils.WebUtils;
import org.springframework.stereotype.Component;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
@Component
public class RestRedirect {

	private final static Logger LOG=Logger.getLogger(RestRedirect.class);
	public void redirect(ServletRequest servletRequest, ServletResponse servletResponse)
	{
		try{
			String requesedturi = ((HttpServletRequest) servletRequest).getRequestURI().toString();
			final String domainURL = WebUtils.extractRequestedDomainName((HttpServletRequest) servletRequest);
			LOG.debug("Resolved domain as"+ domainURL);
			String newDomainName="phoenix-qa.egovernments.org";	        
			String newURI="https://"+newDomainName+requesedturi;
			LOG.info("requesedturl:"+requesedturi+"   "+((HttpServletRequest) servletRequest).getRequestURI());
	
			Object redirected = servletRequest.getAttribute("redirected");
			if(redirected!=null )
			{
				String ss=(String) redirected;
			}
			
		URL obj = new URL(newURI);
		HttpsURLConnection conn = (HttpsURLConnection) obj.openConnection();
		conn.setReadTimeout(5000);
		
		

		boolean redirect = false;

		// normally, 3xx is redirect
		int status = conn.getResponseCode();
		if (status != HttpURLConnection.HTTP_OK) {
			if (status == HttpURLConnection.HTTP_MOVED_TEMP
				|| status == HttpURLConnection.HTTP_MOVED_PERM
					|| status == HttpURLConnection.HTTP_SEE_OTHER)
			redirect = true;
		}


		if (redirect) {

			// get redirect url from "location" header field
			String newUrl = conn.getHeaderField("Location");

			// get the cookie if need, for login
			String cookies = conn.getHeaderField("Set-Cookie");

			// open the new connnection again
			HttpURLConnection	conn1 = (HttpURLConnection) new URL(newUrl).openConnection();
		

		}

		BufferedReader in = new BufferedReader(
	                              new InputStreamReader(conn.getInputStream()));
		String inputLine;
		StringBuffer html = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			html.append(inputLine);
		}
		in.close();


		} catch (Exception e) 
		{
		}

	}

}



