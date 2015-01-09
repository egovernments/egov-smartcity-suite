package org.egov.infstr.workflow;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.models.Script;
import org.egov.infstr.models.StateAware;
import org.egov.infstr.services.PersistenceService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class ActionTest {
	private PersistenceService service;
	private Action action;

	@Before
	public void setupAction() {
		this.action = new Action("hello", "estimate", "description");
		this.service = createMock(PersistenceService.class);
	}

	@Test(expected = EGOVRuntimeException.class)
	public void tryToGetANonexistentScript() {
		final StateAware stateAware = getStateAwareObject();
		this.service.findAllByNamedQuery(Script.BY_NAME,
				stateAware.getStateType() + ".workflow.hello");
		expectLastCall().andReturn(Collections.EMPTY_LIST);
		this.service.findAllByNamedQuery(Script.BY_NAME,
				stateAware.getStateType() + ".workflow");
		expectLastCall().andReturn(Collections.EMPTY_LIST);
		replay(this.service);
		this.action.getScript(stateAware, this.service);
	}

	public StateAware getStateAwareObject() {
		final StateAware stateAware = new StateAware() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public String getStateDetails() {
				return null;
			}
		};
		return stateAware;
	}

	@Test
	public void getAnExistingScript() {
		final StateAware stateAware = getStateAwareObject();
		this.service.findAllByNamedQuery(Script.BY_NAME,
				stateAware.getStateType() + ".workflow.hello");
		final Script script = new Script(stateAware.getStateType()
				+ ".workflow.hello", "python", "result='test'");
		expectLastCall().andReturn(Arrays.asList(script));
		replay(this.service);
		assertSame(script, this.action.getScript(stateAware, this.service));
	}

	@Test
	public void getDefaultScript() {
		final StateAware stateAware = getStateAwareObject();
		this.service.findAllByNamedQuery(Script.BY_NAME,
				stateAware.getStateType() + ".workflow.hello");
		expectLastCall().andReturn(Collections.EMPTY_LIST);
		final Script script = new Script(stateAware.getStateType()
				+ ".workflow.hello", "python", "result='test'");
		this.service.findAllByNamedQuery(Script.BY_NAME,
				stateAware.getStateType() + ".workflow");
		expectLastCall().andReturn(Arrays.asList(script));
		replay(this.service);
		assertSame(script, this.action.getScript(stateAware, this.service));
	}

	@Test
	@Ignore
	public void executeAnAction() {
		final StateAware stateAware = getStateAwareObject();
		this.service.findAllByNamedQuery(Script.BY_NAME,
				stateAware.getStateType() + ".workflow.hello");
		final Script script = new Script(stateAware.getStateType()
				+ ".workflow.hello", "python", "result='test'");
		expectLastCall().andReturn(Arrays.asList(script));
		replay(this.service);
		final Object results = this.action.execute(stateAware, this.service);
		assertEquals("test", results);
	}

	@Test
	@Ignore
	public void executeAnActionWithComment() {
		final StateAware stateAware = getStateAwareObject();
		this.service.findAllByNamedQuery(Script.BY_NAME,
				stateAware.getStateType() + ".workflow.hello");
		final Script script = new Script(stateAware.getStateType()
				+ ".workflow.hello", "python", "result='test'");
		expectLastCall().andReturn(Arrays.asList(script));
		replay(this.service);
		final Object results = this.action.execute(stateAware, this.service,
				"Comments");
		assertEquals("test", results);
	}
}
