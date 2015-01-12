package org.egov.infstr.utils;

import org.junit.Before;
import org.junit.Test;

import servletunit.HttpServletRequestSimulator;
import servletunit.ServletConfigSimulator;

public class RequestUtilsTest {
	private ServletConfigSimulator config;
	private HttpServletRequestSimulator req;

	@Before
	public void setup() {
		this.config = new ServletConfigSimulator();
		this.req = new HttpServletRequestSimulator(
				this.config.getServletContext());
	}

	@Test
	public void getRequestParamsAsXMLString() {
		this.req.addParameter("test1", "test1Value");
		this.req.addParameter("bool1", "bool1Value");
		final String str = RequestUtils.getRequestParamsAsXMLString(this.req);
		System.out.println(str);
	}

	@Test
	public void getRequestParamsAsXMLStringWithNoParams() {
		this.req.setAttribute("test1", "test1Value");
		final String str = RequestUtils.getRequestParamsAsXMLString(this.req);
		System.out.println(str);
	}

}
