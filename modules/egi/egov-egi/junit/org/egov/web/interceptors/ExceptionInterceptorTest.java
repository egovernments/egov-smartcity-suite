package org.egov.web.interceptors;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.utils.HibernateUtil;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.XWorkTestCase;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.config.entities.ExceptionMappingConfig;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * Test for ExceptionInterceptor
 * 
 * @author Sahina Bose
 * 
 */
public class ExceptionInterceptorTest extends XWorkTestCase {

	ExceptionInterceptor interceptor;
	ActionInvocation mockInvocation;
	ValueStack stack;
	ActionProxy actionProxy;

	public void testNoException() throws Exception {
		// Mock httpServletRequestMock = new Mock(HttpServletRequest.class);

		this.setUpWithExceptionMappings();

		final Action action = mock(Action.class);
		when(this.mockInvocation.invoke()).thenReturn(Action.SUCCESS);
		when(this.mockInvocation.getAction()).thenReturn(action);
		final String result = this.interceptor.intercept(this.mockInvocation);
		assertEquals(result, Action.SUCCESS);
		assertEquals(HibernateUtil.isMarkedForRollback(), false);
		assertNull(this.stack.findValue("exception"));
	}

	public void testWithException() throws Exception {
		// Mock httpServletRequestMock = new Mock(HttpServletRequest.class);

		this.setUpWithExceptionMappings();
		final Exception exception = new EGOVRuntimeException("test");
		final Action action = mock(Action.class);
		when(this.mockInvocation.invoke()).thenThrow(exception);
		when(this.mockInvocation.getAction()).thenReturn(action);
		final String result = this.interceptor.intercept(this.mockInvocation);
		assertEquals(result, "genericError");
		assertEquals(HibernateUtil.isMarkedForRollback(), true);
		assertNotNull(this.stack.findValue("exception"));
		assertEquals(this.stack.findValue("exception"), exception);
	}

	public void testWithExceptionNoMappings() throws Exception {
		this.setUpWithoutExceptionMappings();
		final Exception exception = new EGOVRuntimeException("test");
		final Action action = mock(Action.class);
		when(this.mockInvocation.invoke()).thenThrow(exception);
		when(this.mockInvocation.getAction()).thenReturn(action);
		try {
			this.interceptor.intercept(this.mockInvocation);
		} catch (final Exception e) {
			assertEquals(e, exception);
			assertEquals(HibernateUtil.isMarkedForRollback(), false);
		}
	}

	private void setUpWithExceptionMappings() {
		final ActionConfig actionConfig = new ActionConfig.Builder("", "", "")
				.addExceptionMapping(
						new ExceptionMappingConfig.Builder("error",
								"java.lang.Exception", "genericError").build())
				.build();
		this.actionProxy = mock(ActionProxy.class);
		when(this.actionProxy.getConfig()).thenReturn(actionConfig);
		when(this.mockInvocation.getProxy()).thenReturn(this.actionProxy);
	}

	private void setUpWithoutExceptionMappings() {
		final ActionConfig actionConfig = new ActionConfig.Builder("", "", "")
				.build();
		this.actionProxy = mock(ActionProxy.class);
		when(this.actionProxy.getConfig()).thenReturn(actionConfig);
		when(this.mockInvocation.getProxy()).thenReturn(this.actionProxy);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.stack = ActionContext.getContext().getValueStack();
		this.mockInvocation = mock(ActionInvocation.class);
		when(this.mockInvocation.getStack()).thenReturn(this.stack);
		when(this.mockInvocation.getInvocationContext()).thenReturn(
				new ActionContext(new HashMap()));
		this.interceptor = new ExceptionInterceptor();
		this.interceptor.init();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		this.interceptor.destroy();
		this.interceptor = null;
		this.mockInvocation = null;
		this.stack = null;
		HibernateUtil.clearMarkedForRollback();
	}

}
