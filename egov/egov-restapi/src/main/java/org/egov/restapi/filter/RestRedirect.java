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
				System.out.println(ss);
			}
			
		URL obj = new URL(newURI);
		HttpsURLConnection conn = (HttpsURLConnection) obj.openConnection();
		conn.setReadTimeout(5000);
		
		
		System.out.println("Request URL ... " + newURI);

		boolean redirect = false;

		// normally, 3xx is redirect
		int status = conn.getResponseCode();
		if (status != HttpURLConnection.HTTP_OK) {
			if (status == HttpURLConnection.HTTP_MOVED_TEMP
				|| status == HttpURLConnection.HTTP_MOVED_PERM
					|| status == HttpURLConnection.HTTP_SEE_OTHER)
			redirect = true;
		}

		System.out.println("Response Code ... " + status);

		if (redirect) {

			// get redirect url from "location" header field
			String newUrl = conn.getHeaderField("Location");

			// get the cookie if need, for login
			String cookies = conn.getHeaderField("Set-Cookie");

			// open the new connnection again
			HttpURLConnection	conn1 = (HttpURLConnection) new URL(newUrl).openConnection();
		
			System.out.println("Redirect to URL : " + newUrl);

		}

		BufferedReader in = new BufferedReader(
	                              new InputStreamReader(conn.getInputStream()));
		String inputLine;
		StringBuffer html = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			html.append(inputLine);
		}
		in.close();

		System.out.println("URL Content... \n" + html.toString());
		System.out.println("Done");
			 
		} catch (Exception e) 
		{
			e.printStackTrace();
		}

	}

}



