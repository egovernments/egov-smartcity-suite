package org.egov.restapi.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.properties.ApplicationProperties;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.restapi.constants.RestRedirectConstants;
import org.springframework.beans.factory.annotation.Autowired;

public class ApiFilter implements Filter {

	private final static Logger LOG=Logger.getLogger(ApiFilter.class);

	@Autowired
	CityService cityService;
	@Autowired
	private ApplicationProperties applicationProperties;

	@Autowired
	RestRedirect restRedirect;

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		MultiReadHttpServletRequest multiReadRequest = new MultiReadHttpServletRequest((HttpServletRequest) servletRequest);
		
		
		
		String ulbCode = null;
		byte[]  b=new byte[5000];
		ulbCode = servletRequest.getParameter("ulbCode");
			if(ulbCode==null)
			{
				JSONObject jsonObject =null;
				String jb = new String();
				String line = null;
				try {
					ServletInputStream inputStream = multiReadRequest.getInputStream();
					inputStream.read(b);
					jb=new String(b);
				} catch (Exception e) { /*report an error*/ }

				try {
					jsonObject = JSONObject.fromObject(jb.toString());
				} catch (Exception e) {
					e.printStackTrace();
				
		           throw new RuntimeException("Invalid Json");
				}	


				if(jsonObject!=null)
					ulbCode=jsonObject.getString("ulbCode");
				else
					throw new RuntimeException("Invalid Json ULB Code is not Passed");
			
			}

		if(ulbCode!=null)
		{
			//LOG.info(cityService.findAll());
			//City cityByCode = cityService.getCityByCode(ulbCode);
			
			String domainUrl=RestRedirectConstants.getCode_ulbNames().get(ulbCode);
			
			if(! EgovThreadLocals.getDomainName().equalsIgnoreCase(domainUrl))
			{
				LOG.info("Request Reached Different city. Need to change domain details");
				EgovThreadLocals.setTenantID(applicationProperties.getProperty("tenant." + domainUrl));
				EgovThreadLocals.setDomainName(domainUrl);
				EgovThreadLocals.setCityCode(ulbCode);
			}else
			{
				LOG.info("Request Reached Same city. No need to change domain details");
			}

		}


		filterChain.doFilter(multiReadRequest, servletResponse);	
	}

	

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}