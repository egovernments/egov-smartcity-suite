package org.egov.infstr.workflow;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.models.Script;
import org.egov.infstr.models.State;
import org.egov.infstr.models.StateAware;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.ScriptService;
import org.egov.pims.commons.Position;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import javax.script.ScriptContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

@Ignore
public class SimpleWorkflowTest {
	PersistenceService<StateAware, Long> persistenceService;
	SimpleWorkflowService<StateAware> wf;

	@Before
	public void setupWFService() {
		this.persistenceService = createMock(PersistenceService.class);
		this.persistenceService.setType(StateAware.class);
		replay(this.persistenceService);
		this.wf = new SimpleWorkflowService<StateAware>(
				this.persistenceService, StateAware.class);
		reset(this.persistenceService);
	}

	@Test
	public void startWorkflow() {
		final StateAware stateAware = getStateAwareObject();
		final Position owner = new Position();
		this.persistenceService.persist(stateAware);
		expectLastCall().andReturn(stateAware);
		replay(this.persistenceService);
		this.wf.start(stateAware, owner);
		assertNotNull(stateAware.getCurrentState());
		assertEquals(State.NEW, stateAware.getCurrentState().getValue());
		assertEquals(owner, stateAware.getCurrentState().getOwner());
	}

	@Test
	public void tryToStartAnInProgressWorkflowThrowsException() {
		final StateAware stateAware = getStateAwareObject();
		final Position owner = new Position();
		stateAware.changeState(State.NEW, owner, "comment");
		try {
			this.wf.start(stateAware, owner);
			fail();
		} catch (final EGOVRuntimeException e) {
			assertEquals("workflow.already.started", e.getMessage());
		}
	}

	@Test
	public void basicRuleBasedTransition() {
		final StateAware stateAware = getStateAwareObject();
		final String action = "nextState";
		this.persistenceService.findAllByNamedQuery(Action.BY_NAME_AND_TYPE,
				action, stateAware.getStateType());
		expectLastCall().andReturn(Collections.EMPTY_LIST);
		final List<Script> scripts = Arrays.asList(new Script("test", "jython",
				"wfItem.changeState('NEW_STATE',None,'comment')"));
		this.persistenceService.findAllByNamedQuery(Script.BY_NAME,
				stateAware.getStateType() + ".workflow." + action);
		expectLastCall().andReturn(scripts).times(2);
		this.persistenceService.persist(stateAware);
		expectLastCall().andReturn(stateAware).times(2);
		replay(this.persistenceService);
		final Position owner = new Position();
		stateAware.changeState(State.NEW, null, owner, "comment");
		this.wf.transition(
				new Action(action, stateAware.getStateType(), "test"),
				stateAware, "comment");
		assertEquals("NEW_STATE", stateAware.getCurrentState().getValue());
		this.wf.transition(action, stateAware, "comment");
		assertEquals("NEW_STATE", stateAware.getCurrentState().getValue());

	}

	@Test
	public void manualTransition() {
		final StateAware stateAware = getStateAwareObject();
		this.persistenceService.persist(stateAware);
		expectLastCall().andReturn(stateAware);
		replay(this.persistenceService);
		final String string = "hello";
		this.wf.transition(stateAware, new State("test", string,
				new Position(), "comment"));
		assertEquals(string, stateAware.getCurrentState().getValue());
		verify(this.persistenceService);
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
	public void actionWithNoStateChange() {
		final StateAware stateAware = getStateAwareObject();
		final Position newOwner = new Position();
		final Position owner = newOwner;
		stateAware.changeState(State.NEW, owner, "comment");
		this.persistenceService.persist(stateAware);
		expectLastCall().andReturn(stateAware);
		replay(this.persistenceService);
		this.wf.transition(stateAware, newOwner, "comment");
		assertEquals(State.NEW, stateAware.getCurrentState().getValue());
		assertEquals(newOwner, stateAware.getCurrentState().getOwner());
	}

	@Test
	public void stateChangeCheckPrevAndNext() {
		final StateAware wfItem = getStateAwareObject();
		final Position owner = new Position();
		this.wf.start(wfItem, owner);
		expectLastCall().andReturn(wfItem);
		wfItem.changeState("State2", owner, "something");
		this.persistenceService.persist(wfItem);
		expectLastCall().andReturn(wfItem);
		replay(this.persistenceService);
		assertEquals(State.NEW, wfItem.getCurrentState().getPrevious()
				.getValue());
		assertEquals("State2", wfItem.getCurrentState().getPrevious().getNext()
				.getValue());
	}

	@Test
	public void actionTransitionWithNextAction() {
		final StateAware stateAware = getStateAwareObject();
		final Position newOwner = new Position();
		final Position owner = newOwner;
		stateAware.changeState(State.NEW, owner, "comment");
		this.persistenceService.persist(stateAware);
		expectLastCall().andReturn(stateAware);
		replay(this.persistenceService);
		this.wf.transition(stateAware, newOwner, "nextAction", "comment");
		assertEquals(State.NEW, stateAware.getCurrentState().getValue());
		assertEquals(newOwner, stateAware.getCurrentState().getOwner());
		assertEquals("nextAction", stateAware.getCurrentState().getNextAction());
	}

	@Test
	public void testEndWorkflow() {
		StateAware stateAware = getStateAwareObject();
		final State state = new State(stateAware.getStateType(), State.NEW,
				null, new Position(), "comment");
		stateAware.setState(state);
		final PersistenceService persistenceService = Mockito
				.mock(PersistenceService.class);
		final StateAware stateAware2 = getStateAwareObject();
		final State state2 = new State(stateAware.getStateType(), State.END,
				null, new Position(), "comment");
		stateAware2.setState(state2);
		Mockito.when(persistenceService.persist(stateAware)).thenReturn(
				stateAware2);
		this.wf = new SimpleWorkflowService<StateAware>(persistenceService);
		this.wf.setType(StateAware.class);
		stateAware = this.wf.end(stateAware, new Position());
		assertEquals(State.END, stateAware.getCurrentState().getValue());
		try {
			stateAware = this.wf.end(stateAware, new Position());
		} catch (final EGOVRuntimeException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testTransitionWithActionName() {
		final List actions = new ArrayList<Action>();
		final Action action = Mockito.mock(Action.class);
		Mockito.when(
				action.execute(Matchers.any(StateAware.class),
						Matchers.any(PersistenceService.class),
						Matchers.any(SimpleWorkflowService.class),
						Matchers.anyString()))
				.thenReturn(getStateAwareObject());
		actions.add(action);
		final PersistenceService persistenceService = Mockito
				.mock(PersistenceService.class);
		this.wf = new SimpleWorkflowService<StateAware>(persistenceService);
		persistenceService.setType(StateAware.class);
		StateAware stateAware = getStateAwareObject();
		final State state = new State(stateAware.getStateType(), State.NEW,
				null, new Position(), "comment");
		stateAware.setState(state);
		Mockito.when(
				persistenceService.findAllByNamedQuery(Matchers.anyString(),
						Matchers.anyString(), Matchers.anyString()))
				.thenReturn(actions);
		Mockito.when(persistenceService.persist(Matchers.any(StateAware.class)))
				.thenReturn(stateAware);
		stateAware = this.wf.transition("actionName", stateAware, "comment");
		assertNotNull(stateAware);
	}

	@Test
	public void testGetValidActions() {
		final PersistenceService persistence = Mockito
				.mock(PersistenceService.class);
		final ScriptService service = Mockito.mock(ScriptService.class);
		final Script script = Mockito.mock(Script.class);
		final ArrayList<String> actionNames = new ArrayList<String>();
		actionNames.add("actionname");
		final ArrayList<Script> scripts = new ArrayList<Script>();
		scripts.add(script);
		Mockito.when(
				service.executeScript(Matchers.any(Script.class),
						Matchers.any(ScriptContext.class))).thenReturn(
				actionNames);
		Mockito.when(
				persistence.findAllByNamedQuery(Matchers.anyString(),
						Matchers.anyString())).thenReturn(scripts);
		Mockito.when(
				persistence.findAllByNamedQuery(Matchers.anyString(),
						Matchers.anyString(), Matchers.anyString()))
				.thenReturn(new ArrayList<Action>());
		this.wf = new SimpleWorkflowService<StateAware>(persistence);
		this.wf.setScriptExecutionService(service);
		assertNotNull(this.wf.getValidActions(getStateAwareObject()));
		final ArrayList<Action> actions = new ArrayList<Action>();
		actions.add(Mockito.mock(Action.class));
		Mockito.when(
				persistence.findAllByNamedQuery(Matchers.anyString(),
						Matchers.anyString(), Matchers.anyString()))
				.thenReturn(actions);
		assertNotNull(this.wf.getValidActions(getStateAwareObject()));
	}
}
