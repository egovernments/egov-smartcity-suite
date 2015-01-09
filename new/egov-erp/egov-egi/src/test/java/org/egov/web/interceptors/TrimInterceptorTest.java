package org.egov.web.interceptors;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.XWorkTestCase;
import org.apache.struts2.StrutsStatics;
import org.junit.Ignore;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Ignore
public class TrimInterceptorTest extends XWorkTestCase {
	TrimInterceptor interceptor;
	ActionInvocation mockInvocation;
	HashMap contextMap;
	HttpServletRequest request;

	public void testParameters() throws Exception {
		setUpParameters(new String[] { "abc" }, new String[] { "  hhh  " });
		when(this.mockInvocation.getInvocationContext()).thenReturn(
				new ActionContext(this.contextMap));
		this.interceptor.intercept(this.mockInvocation);
		assertEquals("hhh", getParameterValue("abc"));
	}

	public void testParametersWithNoValues() throws Exception {
		setUpParameters(new String[] { "abc" }, new String[] {});
		when(this.mockInvocation.getInvocationContext()).thenReturn(
				new ActionContext(this.contextMap));
		this.interceptor.intercept(this.mockInvocation);
		assertEquals(null, getParameterValue("abc"));
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.mockInvocation = mock(ActionInvocation.class);
		this.request = mock(HttpServletRequest.class);
		this.contextMap = new HashMap();
		this.contextMap.put(StrutsStatics.HTTP_REQUEST, this.request);
		this.interceptor = new TrimInterceptor();
		this.interceptor.init();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		this.interceptor.destroy();
		this.interceptor = null;
		this.mockInvocation = null;
		this.contextMap = null;
	}

	private void setUpParameters(final String[] paramNames,
			final String[] paramValues) {
		final Map params = new HashMap();
		for (int i = 0; i < paramNames.length; i++) {
			params.put(paramNames[i], "irrelevant what this is");
			if (paramValues.length > i) {
				when(this.request.getParameterValues(paramNames[i]))
						.thenReturn(new String[] { paramValues[i] });
			} else {
				when(this.request.getParameterValues(paramNames[i]))
						.thenReturn(null);
			}
		}
		this.contextMap.put(ActionContext.PARAMETERS, params);
	}

	private String getParameterValue(final String key) {
		final Map parameterMap = ((Map) this.contextMap
				.get(ActionContext.PARAMETERS));
		final String[] values = (String[]) parameterMap.get(key);
		if (values != null) {
			return values[0];
		} else {
			return null;
		}
	}
}
