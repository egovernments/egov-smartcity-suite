package org.egov.lib.rrbac.filter;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptContext;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.egov.exceptions.AuthorizationException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.models.Script;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.ScriptService;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.UserImpl;
import org.egov.lib.rjbac.user.ejb.api.UserService;
import org.egov.lib.rrbac.dao.ActionDAO;
import org.egov.lib.rrbac.model.Action;
import org.egov.lib.rrbac.model.AuthorizationRule;
import org.junit.Before;
import org.junit.Test;

public class RuleInterceptorFilterTest {

	private PersistenceService<Script, Long> scriptService;
	private PersistenceService<AuthorizationRule, Long> authRuleService;
	private UserService userManager;
	private ActionDAO actionDao;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private HttpSession httpSession;
	private FilterChain filterChain;
	private ScriptService mockScriptService;
	private RuleInterceptorFilter ruleInterceptorFilter;

	@Before
	public void setUpInterceptor() {
		this.ruleInterceptorFilter = new RuleInterceptorFilter();
		this.scriptService = mock(PersistenceService.class);
		this.authRuleService = mock(PersistenceService.class);
		this.userManager = mock(UserService.class);
		this.actionDao = mock(ActionDAO.class);
		this.mockScriptService = mock(ScriptService.class);
		this.ruleInterceptorFilter.setScriptExecuter(this.mockScriptService);
		this.ruleInterceptorFilter.setActionDao(this.actionDao);
		this.ruleInterceptorFilter.setAuthRuleService(this.authRuleService);
		this.ruleInterceptorFilter.setDaoService(this.scriptService);
		this.ruleInterceptorFilter.setUserService(this.userManager);
		EGOVThreadLocals.setUserId("1");
		this.request = mock(HttpServletRequest.class);
		this.response = mock(HttpServletResponse.class);
		this.httpSession = mock(HttpSession.class);
		when(this.request.getSession()).thenReturn(this.httpSession);
		this.filterChain = mock(FilterChain.class);

	}

	@Test
	public void testDoFilter() throws Exception {
		this.ruleInterceptorFilter.init(null);
		this.ruleInterceptorFilter.destroy();
		when(this.httpSession.getAttribute("com.egov.user.LoginUserId"))
				.thenReturn(1);
		when(this.request.getParameter(anyString())).thenReturn("1", "", "1");
		when(this.request.getRequestURI()).thenReturn("/egi/location.jsp");
		when(this.request.getContextPath()).thenReturn("/egi");
		final User user = new UserImpl();
		when(this.userManager.getUserByID(anyInt())).thenReturn(user);
		when(this.actionDao.findActionByURL(anyString(), anyString()))
				.thenReturn(new Action());
		final List<AuthorizationRule> authRuleList = new ArrayList<AuthorizationRule>();
		final AuthorizationRule authRule = new AuthorizationRule();
		authRule.setObjectType("org.egov.infstr.models.BaseModel");
		final Script script = new Script("scriptname",
				authRule.getObjectType(), "script");
		authRule.setScript(script);
		authRuleList.add(authRule);
		when(
				this.authRuleService.findAllByNamedQuery(anyString(),
						any(Action.class))).thenReturn(authRuleList);
		final List<Script> scriptList = new ArrayList<Script>();
		scriptList.add(script);
		when(this.scriptService.findAllBy(anyString(), anyLong())).thenReturn(
				scriptList);
		final List<Script> scriptList2 = new ArrayList<Script>();
		final Script script2 = mock(Script.class);
		final List<String> values = mock(ArrayList.class);
		when(values.get(0)).thenReturn("true");
		when(values.get(1)).thenReturn("trueasas");
		scriptList2.add(script2);
		when(
				this.mockScriptService.executeScript(any(String.class),
						any(ScriptContext.class))).thenReturn(values);
		this.ruleInterceptorFilter.doFilter(this.request, this.response,
				this.filterChain);
		when(values.get(0)).thenReturn("false");
		try {
			this.ruleInterceptorFilter.doFilter(this.request, this.response,
					this.filterChain);
		} catch (final AuthorizationException e) {
			assertTrue(true);
		}
		try {
			when(this.scriptService.findAllBy(anyString(), anyLong()))
					.thenReturn(new ArrayList<Script>());
			this.ruleInterceptorFilter.doFilter(this.request, this.response,
					this.filterChain);
		} catch (final EGOVRuntimeException e) {
			assertTrue(true);
		}

	}

}
