package org.egov.web.interceptors;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.egov.web.annotation.ValidationErrorPageExt;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.XWorkTestCase;
import com.opensymphony.xwork2.util.ValueStack;

public class ValidationInterceptorTest extends XWorkTestCase {
	ValidationInterceptor interceptor;
	ActionInvocation mockInvocation;
	ValueStack stack;
	ActionProxy actionProxy;
	Method method;

	public void testValidationErrorWithArgs() throws Exception {
		final BaseFormAction action = new BaseFormAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public String validationError() {
				return "error";
			}

			@Override
			public Object getModel() {
				validationError();
				create();
				return null;
			}

			public String create() {
				return SUCCESS;
			}
		};
		action.getModel();
		final Date date = new Date();
		final List<ValidationError> list = new ArrayList<ValidationError>();
		list.add(new ValidationError("Date", "Date {0} is not in {1} format",
				new String[] { date.toString(), "dd/MM/yyyy" }));
		final Exception exception = new ValidationException(list);
		when(this.mockInvocation.invoke()).thenThrow(exception);
		when(this.mockInvocation.getAction()).thenReturn(action);
		when(this.mockInvocation.getProxy().getMethod()).thenReturn(
				"validationError");
		this.interceptor.intercept(this.mockInvocation);
		assertEquals(("Date " + date + " is not in " + "dd/MM/yyyy format"),
				action.getText(list.get(0).getMessage(), list.get(0)
						.getMessage(), list.get(0).getArgs()));
		when(this.mockInvocation.getProxy().getMethod()).thenReturn("create");
		this.interceptor.intercept(this.mockInvocation);
		action.addActionError("action error");
		this.interceptor.intercept(this.mockInvocation);
		final Method mm = this.interceptor.getClass().getDeclaredMethod(
				"invokeActionMethod", Method.class, Object.class);
		mm.setAccessible(true);
		mm.invoke(this.interceptor,
				action.getClass().getDeclaredMethod("create"), action);
		when(this.mockInvocation.getProxy().getMethod()).thenThrow(exception);
		when(
				BaseFormAction.class.isAssignableFrom(this.mockInvocation
						.getAction().getClass())).thenReturn(false);
		try {
			this.interceptor.intercept(this.mockInvocation);
		} catch (final ValidationException e) {
			assertNotNull(e);
		}
	}

	private BaseFormAction getBaseFormAction() {
		final BaseFormAction action = new BaseFormAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public String validationError() {
				return "error";
			}

			@Override
			public Object getModel() {
				validationError();
				return null;
			}
		};
		return action;
	}

	public void testValidationErrorWithoutArgs() throws Exception {
		final BaseFormAction action = getBaseFormAction();
		action.getModel();
		final List<ValidationError> list = new ArrayList<ValidationError>();
		list.add(new ValidationError("Date",
				"Date  is not in dd/MM/yyyy format"));
		final Exception exception = new ValidationException(list);
		when(this.mockInvocation.invoke()).thenThrow(exception);
		when(this.mockInvocation.getAction()).thenReturn(action);
		when(this.mockInvocation.getProxy().getMethod()).thenReturn(
				"validationError");

		this.interceptor.intercept(this.mockInvocation);
		assertEquals(("Date  is not in dd/MM/yyyy format"), action.getText(list
				.get(0).getMessage(), list.get(0).getMessage()));
		assertNull(list.get(0).getArgs());
	}

	public void testValidationExceptionWithArgs() throws Exception {
		final ActionSupport action = getBaseFormAction();
		final Date date = new Date();
		final Exception exception = new ValidationException("Date",
				"Date {0} is not in {1} format", new String[] {
						date.toString(), "dd/MM/yyyy" });
		when(this.mockInvocation.invoke()).thenThrow(exception);
		when(this.mockInvocation.getAction()).thenReturn(action);
		when(this.mockInvocation.getProxy().getMethod()).thenReturn(
				"validationError");

		this.interceptor.intercept(this.mockInvocation);
		assertEquals(("Date " + date + " is not in " + "dd/MM/yyyy format"),
				action.getText("Date", "Date {0} is not in {1} format",
						new String[] { date.toString(), "dd/MM/yyyy" }));
	}

	public void testExceptionThrownIfNotBaseFormAction() throws Exception {
		final ActionSupport action = getBaseFormAction();
		final Date date = new Date();
		final List<ValidationError> list = new ArrayList<ValidationError>();
		list.add(new ValidationError("Date", "Date {0} is not in {1} format",
				new String[] { date.toString(), "dd/MM/yyyy" }));
		final Exception exception = new ValidationException(list);
		when(this.mockInvocation.invoke()).thenThrow(exception);
		when(this.mockInvocation.getAction()).thenReturn(action);
		when(this.mockInvocation.getProxy().getMethod()).thenReturn(
				"validationError");
		try {
			this.interceptor.intercept(this.mockInvocation);
		} catch (final ValidationException validexp) {
		}
	}

	public void testMethodAnnotationValidationErrorPage() throws Exception {
		final BaseFormAction action = new BaseFormAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@ValidationErrorPage(value = "validationError")
			public String validationError() {
				return "error";
			}

			@Override
			public Object getModel() {
				validationError();
				return null;
			}
		};
		action.getModel();
		when(this.mockInvocation.invoke()).thenReturn("validationError");
		when(this.mockInvocation.getAction()).thenReturn(action);
		when(this.mockInvocation.getProxy().getMethod()).thenReturn(
				"validationError");
		final String result = this.interceptor.intercept(this.mockInvocation);
		assertEquals("validationError", result);
	}

	public void testMethodAnnotationValidationErrorPageExt() throws Exception {
		final BaseFormAction action = new BaseFormAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@ValidationErrorPageExt(action = "SUCCESS", makeCall = true, toMethod = "validationError")
			public String validationError() {
				return "error";
			}

			@Override
			public Object getModel() {
				validationError();
				assertTrue(true);
				return null;
			}
		};
		action.getModel();
		when(this.mockInvocation.invoke()).thenReturn("validationError");
		when(this.mockInvocation.getAction()).thenReturn(action);
		when(this.mockInvocation.getProxy().getMethod()).thenReturn(
				"validationError");
		final String result = this.interceptor.intercept(this.mockInvocation);
		assertEquals("validationError", result);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.mockInvocation = mock(ActionInvocation.class);
		when(this.mockInvocation.getInvocationContext()).thenReturn(
				new ActionContext(new HashMap()));
		this.actionProxy = mock(ActionProxy.class);
		when(this.mockInvocation.getProxy()).thenReturn(this.actionProxy);
		this.interceptor = new ValidationInterceptor();
		this.interceptor.init();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		this.interceptor.destroy();
		this.interceptor = null;
		this.mockInvocation = null;
		HibernateUtil.clearMarkedForRollback();
	}
}
