package org.egov.infstr.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.pims.commons.Position;
import org.junit.Test;

public class StateTest {

	@Test
	public void isNew() {
		final State state = new State();
		assertFalse(state.isNew());
		state.setValue(State.NEW);
		assertTrue(state.isNew());
	}

	@Test
	public void getHistoryReturnsSelf() {
		final State state = new State();
		state.setValue(State.NEW);
		assertEquals(1, state.getHistory().size());
		assertEquals(state, state.getHistory().get(0));
	}

	@Test
	public void setPreviousMaintainsConsistentChaining() {
		final State state1 = new State("test", "STATE1", new Position(),
				"commet1");
		final State state2 = new State("test", "STATE2", new Position(),
				"commet1");
		state2.setPrevious(state1);
		assertEquals(state2, state1.getNext());
	}

	@Test
	public void setNextMaintainsConsistentChaining() {
		final State state1 = new State("test", "STATE1", new Position(),
				"commet1");
		final State state2 = new State("test", "STATE2", new Position(),
				"commet1");
		state1.setNext(state2);
		assertEquals(state1, state2.getPrevious());
	}

	@Test
	public void cannotSetSelfAsNextState() {
		final State state1 = new State("test", "STATE1", new Position(),
				"commet1");
		try {
			state1.setNext(state1);
			fail();
		} catch (final EGOVRuntimeException e) {
			assertEquals("state.self_reference", e.getMessage());
		}
	}

	@Test
	public void cannotSetSelfAsPreviousState() {
		final State state1 = new State("test", "STATE1", new Position(),
				"commet1");
		try {
			state1.setPrevious(state1);
			fail();
		} catch (final EGOVRuntimeException e) {
			assertEquals("state.self_reference", e.getMessage());
		}
	}

	@Test
	public void getHistoryWithMultipleEntries() {
		final State state0 = new State("test", "STATE0", new Position(),
				"commet1");
		final State state1 = new State("test", "STATE1", new Position(),
				"commet1");
		final State state2 = new State("test", "STATE2", new Position(),
				"commet1");
		state2.setPrevious(state1);
		state1.setPrevious(state0);

		assertEquals(Arrays.asList(state2, state1, state0), state2.getHistory());
		assertEquals(Arrays.asList(state1, state0), state1.getHistory());
		assertEquals(Arrays.asList(state0), state0.getHistory());
	}

	@Test
	public void getText1() {
		final State state = new State("test", "STATE0", new Position(),
				"commet1");
		assertEquals("commet1", state.getText1());
	}
}
